package br.com.fiap.Portaria.repository;

import br.com.fiap.Portaria.entity.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda, Integer> {
    Optional<Encomenda> findByTokenEncomenda(String tokenEncomenda);

    List<Encomenda> findByFoiRetiradaFalse();
}
