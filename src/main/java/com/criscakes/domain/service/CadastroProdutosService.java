package com.criscakes.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.criscakes.domain.exceptions.EntidadeEmUsoException;
import com.criscakes.domain.exceptions.EntidadeNaoEncontradaException;
import com.criscakes.domain.model.Produtos;
import com.criscakes.domain.repository.ProdutosRepository;

@Service
public class CadastroProdutosService {
	
	@Autowired
	private ProdutosRepository produtosRepository;
	

	
	public Produtos salvar (Produtos produtos) {
		return produtosRepository.save(produtos);
	}
	
	public void excluir(Long produtosId) {
		try {

			produtosRepository.deleteById(produtosId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de cidade com código %d", produtosId));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Cidade de código %d não pode ser removido, pois está em uso", produtosId));
		}
	}

}


