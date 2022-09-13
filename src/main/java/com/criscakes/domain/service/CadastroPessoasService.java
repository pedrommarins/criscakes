package com.criscakes.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.criscakes.domain.exceptions.EntidadeEmUsoException;
import com.criscakes.domain.exceptions.EntidadeNaoEncontradaException;
import com.criscakes.domain.model.Pessoas;
import com.criscakes.domain.repository.PessoasRepository;

@Service
public class CadastroPessoasService {
	
	@Autowired
	private PessoasRepository pessoasRepository;
	

	
	public Pessoas salvar (Pessoas pessoas) {
		return pessoasRepository.save(pessoas);
	}
	
	public void excluir(Long pessoasId) {
		try {

			pessoasRepository.deleteById(pessoasId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de cidade com código %d", pessoasId));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Cidade de código %d não pode ser removido, pois está em uso", pessoasId));
		}
	}

}


