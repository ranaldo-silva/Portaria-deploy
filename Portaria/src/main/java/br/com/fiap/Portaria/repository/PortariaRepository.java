package br.com.fiap.Portaria.repository;

import br.com.fiap.Portaria.entity.Portaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PortariaRepository extends JpaRepository<Portaria, Integer> {

    @Modifying
    @Transactional
    @Query(value = "CALL INSERIR_PORTEIRO(:id, :nomePorteiro, :turno, :contato)", nativeQuery = true)
    void inserirPorteiro(@Param("id") Integer id,
                         @Param("nomePorteiro") String nomePorteiro,
                         @Param("turno") String turno,
                         @Param("contato") String contato);

    @Modifying
    @Transactional
    @Query(value = "CALL ATUALIZAR_PORTEIRO(:id, :nomePorteiro ,:turno, :contato)", nativeQuery = true)
    void atualizarPorteiro(@Param("id") Integer id,
                           @Param("nomePorteiro") String nomePorteiro,
                           @Param("turno") String turno,
                           @Param("contato") String contato);

    @Modifying
    @Transactional
    @Query(value = "CALL DELETAR_PORTEIRO(:id)", nativeQuery = true)
    void deletarPorteiro(@Param("id") Integer id);
}