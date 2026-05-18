package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.dto.MoradorRequestDTO;
import br.com.fiap.Portaria.dto.MoradorResponseDTO;
import br.com.fiap.Portaria.entity.Apartamento;
import br.com.fiap.Portaria.entity.Morador;
import br.com.fiap.Portaria.repository.ApartamentoRepository;
import br.com.fiap.Portaria.repository.MoradorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    @Autowired
    private EntityManager entityManager;

    public List<MoradorResponseDTO> listarTodos() {
        return moradorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<MoradorResponseDTO> buscarPorId(Integer id) {
        return moradorRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public MoradorResponseDTO salvar(MoradorRequestDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new RuntimeException("Nome do morador é obrigatório");
        }

        Morador morador = new Morador();
        morador.setNome(dto.getNome());
        morador.setTelefone(dto.getTelefone());
        morador.setEmail(dto.getEmail());

        if (dto.getApartamentoId() != null) {
            Apartamento apartamento = apartamentoRepository.findById(dto.getApartamentoId())
                    .orElseGet(() -> {
                        Apartamento newAp = new Apartamento();
                        newAp.setIdApartamento(dto.getApartamentoId());
                        newAp.setNumero(String.valueOf(dto.getApartamentoId()));
                        newAp.setBloco("A"); // Bloco padrão para evitar falha de chave estrangeira
                        newAp.setTorre(1);
                        return apartamentoRepository.save(newAp);
                    });
            morador.setApartamento(apartamento);
        }

        Integer proximoId = buscarProximoIdMorador();
        morador.setIdMorador(proximoId);

        return toResponseDTO(moradorRepository.save(morador));
    }

    public MoradorResponseDTO atualizar(Integer id, MoradorRequestDTO dto) {
        return moradorRepository.findById(id)
                .map(morador -> {
                    if (dto.getNome() != null) morador.setNome(dto.getNome());
                    if (dto.getTelefone() != null) morador.setTelefone(dto.getTelefone());
                    if (dto.getEmail() != null) morador.setEmail(dto.getEmail());

                    if (dto.getApartamentoId() != null) {
                        Apartamento apartamento = apartamentoRepository.findById(dto.getApartamentoId())
                                .orElseGet(() -> {
                                    Apartamento newAp = new Apartamento();
                                    newAp.setIdApartamento(dto.getApartamentoId());
                                    newAp.setNumero(String.valueOf(dto.getApartamentoId()));
                                    newAp.setBloco("A"); // Bloco padrão para evitar falha de chave estrangeira
                                    newAp.setTorre(1);
                                    return apartamentoRepository.save(newAp);
                                });
                        morador.setApartamento(apartamento);
                    }

                    return toResponseDTO(moradorRepository.save(morador));
                })
                .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
    }

    public void deletar(Integer id) {
        if (!moradorRepository.existsById(id)) {
            throw new RuntimeException("Morador não encontrado");
        }
        moradorRepository.deleteById(id);
    }

    private Integer buscarProximoIdMorador() {
        Query query = entityManager.createNativeQuery("SELECT NVL(MAX(ID_MORADOR), 0) + 1 FROM TPL_MORADOR");
        return ((Number) query.getSingleResult()).intValue();
    }

    private MoradorResponseDTO toResponseDTO(Morador morador) {
        String bloco = null;
        String numero = null;

        if (morador.getApartamento() != null) {
            bloco = morador.getApartamento().getBloco();
            numero = morador.getApartamento().getNumero();
        }

        return new MoradorResponseDTO(
                morador.getIdMorador(),
                morador.getNome(),
                morador.getTelefone(),
                morador.getEmail(),
                bloco,
                numero
        );
    }
}
