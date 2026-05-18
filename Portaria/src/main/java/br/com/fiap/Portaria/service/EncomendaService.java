package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.client.MLClient;
import br.com.fiap.Portaria.dto.EncomendaRequestDTO;
import br.com.fiap.Portaria.dto.EncomendaResponseDTO;
import br.com.fiap.Portaria.entity.*;
import br.com.fiap.Portaria.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EncomendaService {

    private static final Logger log = LoggerFactory.getLogger(EncomendaService.class);

    @Autowired
    private EncomendaRepository encomendaRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EncomendaProducer encomendaProducer;

    @Autowired
    private RetiradaRepository retiradaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PortariaRepository portariaRepository;

    @Autowired
    private MLClient mlClient;

    public List<EncomendaResponseDTO> listarTodas() {
        return encomendaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EncomendaResponseDTO> buscarPorId(Integer id) {
        return encomendaRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public EncomendaResponseDTO buscarPorToken(String token) {
        Encomenda encomenda = encomendaRepository.findByTokenEncomenda(token)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada para o token: " + token));
        return toResponseDTO(encomenda);
    }

    public EncomendaResponseDTO salvar(EncomendaRequestDTO dto, String email) {
        validarDados(dto);

        Encomenda encomenda = montarEncomenda(dto);
        Portaria portaria = resolverPortaria(email);

        Retirada retiradaSalva = criarRetiradaPendente(encomenda, portaria);
        encomenda.setRetirada(retiradaSalva);

        Encomenda salva = encomendaRepository.save(encomenda);

        encomendaProducer.notificarEncomendaRecebida(
                "Nova encomenda recebida: " + salva.getDescricao() +
                        " | Morador ID: " + salva.getMorador().getIdMorador()
        );

        // ML chamado de forma não-bloqueante — falha no ML não derruba o fluxo principal
        preverTempoRetirada();

        return toResponseDTO(salva);
    }

    public EncomendaResponseDTO atualizar(Integer id, EncomendaRequestDTO dto) {
        return encomendaRepository.findById(id)
                .map(encomenda -> {
                    if (dto.getDescricao() != null) encomenda.setDescricao(dto.getDescricao());
                    if (dto.getOrigem() != null) encomenda.setOrigem(dto.getOrigem());

                    if (dto.getMoradorId() != null) {
                        Morador morador = moradorRepository.findById(dto.getMoradorId())
                                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
                        encomenda.setMorador(morador);
                    }

                    return toResponseDTO(encomendaRepository.save(encomenda));
                })
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada"));
    }

    public void deletar(Integer id) {
        if (!encomendaRepository.existsById(id)) {
            throw new RuntimeException("Encomenda não encontrada");
        }
        encomendaRepository.deleteById(id);
    }

    private Integer buscarProximoIdEncomenda() {
        Query query = entityManager.createNativeQuery("SELECT NVL(MAX(ID_ENCOMENDA), 0) + 1 FROM TPL_ENCOMENDA");
        return ((Number) query.getSingleResult()).intValue();
    }

    private String gerarToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder token = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 5; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        return token.toString();
    }

    // AQUI ESTÁ A CORREÇÃO ENVIANDO AS DATAS:
    private EncomendaResponseDTO toResponseDTO(Encomenda encomenda) {
        EncomendaResponseDTO.MoradorResumoDTO moradorResumo = null;
        if (encomenda.getMorador() != null) {
            moradorResumo = new EncomendaResponseDTO.MoradorResumoDTO(
                    encomenda.getMorador().getIdMorador(),
                    encomenda.getMorador().getNome()
            );
        }
        return new EncomendaResponseDTO(
                encomenda.getIdEncomenda(),
                encomenda.getTokenEncomenda(),
                encomenda.getOrigem(),
                encomenda.getDescricao(),
                encomenda.getFoiRetirada(),
                encomenda.getDataRecebida(),  // Nova linha adicionada
                encomenda.getRetiradaEm(),    // Nova linha adicionada
                moradorResumo
        );
    }

    private void validarDados(EncomendaRequestDTO dto) {
        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RuntimeException("Descrição da encomenda é obrigatória");
        }
        if (dto.getMoradorId() == null) {
            throw new RuntimeException("Morador é obrigatório para registrar encomenda");
        }
    }

    private Encomenda montarEncomenda(EncomendaRequestDTO dto) {
        Encomenda encomenda = new Encomenda();
        encomenda.setDescricao(dto.getDescricao());
        encomenda.setOrigem(dto.getOrigem());
        encomenda.setFoiRetirada(false);
        encomenda.setTokenEncomenda(gerarToken());
        encomenda.setDataRecebida(new Date());

        Morador morador = moradorRepository.findById(dto.getMoradorId())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        encomenda.setMorador(morador);

        Integer proximoId = buscarProximoIdEncomenda();
        encomenda.setIdEncomenda(proximoId);

        return encomenda;
    }

    private Portaria resolverPortaria(String email) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuarioLogado.getIdPortaria() != null) {
            return portariaRepository.findById(usuarioLogado.getIdPortaria())
                    .orElseThrow(() -> new RuntimeException("Portaria não encontrada"));
        }
        return portariaRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Portaria padrão não encontrada"));
    }

    private Retirada criarRetiradaPendente(Encomenda encomenda, Portaria portaria) {
        Retirada retirada = new Retirada();
        Integer proximoId = ((Number) entityManager
                .createNativeQuery("SELECT NVL(MAX(ID_RETIRADA), 0) + 1 FROM TPL_RETIRADA")
                .getSingleResult()).intValue();
        retirada.setIdRetirada(proximoId);
        retirada.setTokenRetirada(encomenda.getTokenEncomenda());
        retirada.setMorador(encomenda.getMorador());
        retirada.setPortaria(portaria);
        return retiradaRepository.save(retirada);
    }

    private void preverTempoRetirada() {
        try {
            Map<String, Object> dadosML = Map.of(
                    "weight_in_gms", 2000,
                    "cost_of_the_product", 150,
                    "discount_offered", 10,
                    "prior_purchases", 3,
                    "customer_care_calls", 2,
                    "mode_of_shipment", 1,
                    "product_importance", 1
            );
            Map<String, Object> predicao = mlClient.predict(dadosML);
            log.info("Previsão de retirada: {}", predicao.get("predicao"));
        } catch (Exception e) {
            // ML indisponível não deve impedir o registro da encomenda
            log.warn("Serviço de ML indisponível, previsão ignorada: {}", e.getMessage());
        }
    }
}
