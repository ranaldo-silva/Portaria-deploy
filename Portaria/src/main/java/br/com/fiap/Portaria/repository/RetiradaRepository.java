package br.com.fiap.Portaria.repository;

import br.com.fiap.Portaria.entity.Retirada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetiradaRepository extends JpaRepository<Retirada, Integer> {
}
