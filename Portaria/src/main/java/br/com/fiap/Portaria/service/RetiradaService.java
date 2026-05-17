package br.com.fiap.Portaria.service;

import br.com.fiap.Portaria.dto.RetiradaRequestDTO;
import br.com.fiap.Portaria.dto.RetiradaResponseDTO;
import br.com.fiap.Portaria.entity.Encomenda;
import br.com.fiap.Portaria.entity.Retirada;
import br.com.fiap.Portaria.repository.EncomendaRepository;
import br.com.fiap.Portaria.repository.RetiradaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RetiradaService {

    @Autowired
    private RetiradaRepository retiradaRepository;

    @Autowired
    private EncomendaRepository encomendaRepository;

    @Autowired
    private RetiradaProducer retiradaProducer;

    @Autowired
    private EntityManager entityManager;

    public RetiradaResponseDTO registrarRetirada(RetiradaRequestDTO dto) {
        // busca encomenda pelo token
        Encomenda encomenda = encomendaRepository.findByTokenEncomenda(dto.getEncomenda())
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada para o token: " + dto.getEncomenda()));

        // valida se já foi retirada
        if (Boolean.TRUE.equals(encomenda.getFoiRetirada())) {
            throw new RuntimeException("Encomenda já foi retirada");
        }

        // atualiza a retirada pendente que já existe
        Retirada retirada = encomenda.getRetirada();
        retirada.setDataRetirada(new Date());
        retirada.setMorador(encomenda.getMorador());
        retiradaRepository.save(retirada);

        // atualiza status da encomenda
        encomenda.setFoiRetirada(true);
        encomenda.setRetiradaEm(LocalDateTime.now());
        encomendaRepository.save(encomenda);

        retiradaProducer.notificarRetiradaRealizada(
                "Retirada realizada | MoradorID: " + encomenda.getMorador().getIdMorador() +
                        " | EncomendaID: " + encomenda.getIdEncomenda()
        );

        return toResponseDTO(retirada, encomenda);
    }

    private RetiradaResponseDTO toResponseDTO(Retirada retirada, Encomenda encomenda) {
        return new RetiradaResponseDTO(
                retirada.getIdRetirada(),
                retirada.getDataRetirada(),
                retirada.getTokenRetirada(),
                retirada.getMorador() != null ? retirada.getMorador().getIdMorador() : null,
                encomenda.getIdEncomenda()
        );
    }
}