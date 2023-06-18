package com.gmail.andersoninfonet.gpc.repositories;

import com.gmail.andersoninfonet.gpc.models.entities.Pessoa;
import com.gmail.andersoninfonet.gpc.models.enums.EntityStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query(value = "SELECT p FROM Pessoa p WHERE p.id=:id AND p.status=:status")
    Pessoa findByIdAndStatus(Long id, EntityStatus status);

    Page<Pessoa> findAllByStatus(Pageable pageable, EntityStatus status);

}
