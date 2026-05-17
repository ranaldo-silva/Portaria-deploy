package br.com.fiap.Portaria.repository;

import br.com.fiap.Portaria.entity.Apartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApartamentoRepository extends JpaRepository<Apartamento, Integer> {

    @Modifying
    @Transactional
    @Query(value = "CALL INSERIR_APARTAMENTO(:id, :torre, :bloco, :numero)", nativeQuery = true)
    void inserirApartamento(@Param("id") Integer id,
                            @Param("torre") Integer torre,
                            @Param("bloco") String bloco,
                            @Param("numero") String numero);

    @Modifying
    @Transactional
    @Query(value = "CALL ATUALIZAR_APARTAMENTO(:id, :torre, :bloco, :numero)", nativeQuery = true)
    void atualizarApartamento(@Param("id") Integer id,
                              @Param("torre") Integer torre,
                              @Param("bloco") String bloco,
                              @Param("numero") String numero);

    @Modifying
    @Transactional
    @Query(value = "CALL DELETAR_APARTAMENTO(:id)", nativeQuery = true)
    void deletarApartamento(@Param("id") Integer id);
}