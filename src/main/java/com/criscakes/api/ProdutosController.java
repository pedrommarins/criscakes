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
import com.criscakes.domain.model.Produtos;
import com.criscakes.domain.repository.ProdutosRepository;
import com.criscakes.domain.service.CadastroProdutosService;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

	@Autowired
	private ProdutosRepository produtosRepository;

	@Autowired
	private CadastroProdutosService cadastroProdutosService;

	@GetMapping
	public List<Produtos> listar() {
		return produtosRepository.findAll();
	}

	@GetMapping("/{produtosId}")
	public ResponseEntity<Produtos> buscar(@PathVariable Long produtosId) {
		Optional<Produtos> produtos = produtosRepository.findById(produtosId);

		if (produtos.isPresent()) {
			return ResponseEntity.ok(produtos.get());
		}
		return ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Produtos produtos) {
		try {

			produtos = cadastroProdutosService.salvar(produtos);
			return ResponseEntity.status(HttpStatus.CREATED).body(produtos);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{produtosId}")
	public ResponseEntity<?> atualizar(@PathVariable Long produtosId, @RequestBody Produtos produtos) {
		try {

			Produtos produtoAtual = produtosRepository.findById(produtosId).orElse(null);

			if (produtoAtual != null) {
				BeanUtils.copyProperties(produtos, produtoAtual, "id");

				produtoAtual = cadastroProdutosService.salvar(produtoAtual);
				return ResponseEntity.ok(produtoAtual);
			}

			return ResponseEntity.notFound().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{produtosId}")
	public ResponseEntity<?> remover(@PathVariable Long produtosId) {
		try {
			cadastroProdutosService.excluir(produtosId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

}
