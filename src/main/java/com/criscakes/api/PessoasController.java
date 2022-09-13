package com.criscakes.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.criscakes.domain.exceptions.EntidadeEmUsoException;
import com.criscakes.domain.exceptions.EntidadeNaoEncontradaException;
import com.criscakes.domain.model.Pessoas;
import com.criscakes.domain.model.Produtos;
import com.criscakes.domain.repository.PessoasRepository;
import com.criscakes.domain.service.CadastroPessoasService;

@RestController
@RequestMapping("/produtos")
public class PessoasController {

	@Autowired
	private PessoasRepository pessoasRepository;

	@Autowired
	private CadastroPessoasService cadastroPessoasService;

	@GetMapping
	public List<Pessoas> listar() {
		return pessoasRepository.findAll();
	}

	@GetMapping("/{produtosId}")
	public ResponseEntity<Pessoas> buscar(@PathVariable Long pessoasId) {
		Optional<Pessoas> pessoas = pessoasRepository.findById(pessoasId);

		if (pessoas.isPresent()) {
			return ResponseEntity.ok(pessoas.get());
		}
		return ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Pessoas pessoas) {
		try {

			pessoas = cadastroPessoasService.salvar(pessoas);
			return ResponseEntity.status(HttpStatus.CREATED).body(pessoas);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{pessoasId}")
	public ResponseEntity<?> atualizar(@PathVariable Long pessoasId, @RequestBody Pessoas pessoas) {
		try {

			Pessoas pessoaAtual = pessoasRepository.findById(pessoasId).orElse(null);

			if (pessoaAtual != null) {
				BeanUtils.copyProperties(pessoas, pessoaAtual, "id");

				pessoaAtual = cadastroPessoasService.salvar(pessoaAtual);
				return ResponseEntity.ok(pessoaAtual);
			}

			return ResponseEntity.notFound().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{pessoasId}")
	public ResponseEntity<?> remover(@PathVariable Long pessoasId) {
		try {
			cadastroPessoasService.excluir(pessoasId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

}
