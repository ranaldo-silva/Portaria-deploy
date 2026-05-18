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
        Morador morador = new Morador();

        // Quebrando o nome completo recebido em nome e sobrenome
        if (dto.getNome() != null) {
            String[] nomes = dto.getNome().split(" ", 2);
            morador.setNome(nomes[0]);
            morador.setSobrenome(nomes.length > 1 ? nomes[1] : "");
        }
        
        morador.setTelefone(dto.getTelefone());

        // Se passar um ID de apartamento, tenta achar no banco, se não achar, cria um genérico para evitar erro de ForeignKey
        if (dto.getApartamentoId() != null) {
            Apartamento apartamento = apartamentoRepository.findById(dto.getApartamentoId())
                    .orElseGet(() -> {
                        Apartamento newAp = new Apartamento();
                        newAp.setIdApartamento(dto.getApartamentoId());
                        newAp.setNumero(String.valueOf(dto.getApartamentoId()));
                        newAp.setBloco(dto.getBloco() != null && !dto.getBloco().isBlank() ? dto.getBloco() : "A");
                        newAp.setTorre(1);
                        return apartamentoRepository.save(newAp);
                    });
            morador.setApartamento(apartamento);
        }

        Integer proximoId = buscarProximoIdMorador();
        morador.setIdMorador(proximoId);

        Morador salvo = moradorRepository.save(morador);
        return toResponseDTO(salvo);
    }

    public MoradorResponseDTO atualizar(Integer id, MoradorRequestDTO dto) {
        return moradorRepository.findById(id)
                .map(morador -> {
                    if (dto.getNome() != null) {
                        String[] nomes = dto.getNome().split(" ", 2);
                        morador.setNome(nomes[0]);
                        morador.setSobrenome(nomes.length > 1 ? nomes[1] : "");
                    }
                    if (dto.getTelefone() != null) morador.setTelefone(dto.getTelefone());

                    if (dto.getApartamentoId() != null) {
                        Apartamento apartamento = apartamentoRepository.findById(dto.getApartamentoId())
                                .orElseThrow(() -> new RuntimeException("Apartamento não encontrado"));
                        morador.setApartamento(apartamento);
                    }

                    Morador atualizado = moradorRepository.save(morador);
                    return toResponseDTO(atualizado);
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
        MoradorResponseDTO.ApartamentoResumoDTO apResumo = null;
        if (morador.getApartamento() != null) {
            apResumo = new MoradorResponseDTO.ApartamentoResumoDTO(
                    morador.getApartamento().getIdApartamento(),
                    morador.getApartamento().getNumero(),
                    morador.getApartamento().getBloco()
            );
        }
        return new MoradorResponseDTO(
                morador.getIdMorador(),
                morador.getNome(),
                morador.getSobrenome(),
                morador.getTelefone(),
                apResumo
        );
    }
}
