package com.criscakes.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.criscakes.domain.model.Produtos;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

}
