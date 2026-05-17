package br.com.fiap.Portaria.repository;

import br.com.fiap.Portaria.entity.Morador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Integer> {
    Optional<Morador> findByEmail(String email);
}