package com.criscakes.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.criscakes.domain.model.Pessoas;

public interface PessoasRepository extends JpaRepository<Pessoas, Long> {

}
