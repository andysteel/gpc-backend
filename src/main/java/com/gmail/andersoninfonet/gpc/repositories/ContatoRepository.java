package com.gmail.andersoninfonet.gpc.repositories;

import com.gmail.andersoninfonet.gpc.models.entities.Contato;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @Query("SELECT c from Contato c JOIN FETCH c.pessoa p WHERE p.id = :pessoaId AND c.status = :status")
    Page<Contato> findByPessoaId(Long pessoaId, Pageable pageable, EntityStatus status);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query(value = "SELECT c FROM Contato c WHERE c.id=:id AND c.status=:status")
    Contato findByIdAndStatus(Long id, EntityStatus status);
}
