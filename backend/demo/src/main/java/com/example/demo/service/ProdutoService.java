
package com.example.demo.service;

import com.example.demo.domain.Categoria;
import com.example.demo.domain.Produto;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Produto cadastrar(String nome, String codigo, BigDecimal preco,
                             Integer quantidadeEstoque, Integer estoqueMinimo,
                             Long categoriaId) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código do produto é obrigatório.");
        }

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        if (quantidadeEstoque == null || quantidadeEstoque < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }

        if (estoqueMinimo == null || estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }

        codigo = codigo.trim();

        if (produtoRepository.existsByCodigo(codigo)) {
            throw new IllegalArgumentException("Já existe um produto com esse código.");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada."));

        Produto produto = new Produto(
                nome.trim(),
                codigo,
                preco,
                quantidadeEstoque,
                estoqueMinimo,
                categoria
        );

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, String nome, String codigo,
                             BigDecimal preco, Integer quantidadeEstoque,
                             Integer estoqueMinimo, Long categoriaId) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("O código do produto é obrigatório.");
        }

        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        if (quantidadeEstoque == null || quantidadeEstoque < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }

        if (estoqueMinimo == null || estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }

        if (categoriaId == null) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        codigo = codigo.trim();

        boolean codigoEmUso = produtoRepository.findByCodigo(codigo)
                .filter(outro -> !outro.getId().equals(id))
                .isPresent();

        if (codigoEmUso) {
            throw new IllegalArgumentException("Já existe outro produto com esse código.");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new NoSuchElementException("Categoria não encontrada."));

        produto.setNome(nome.trim());
        produto.setCodigo(codigo);
        produto.setPreco(preco);
        produto.setQuantidadeEstoque(quantidadeEstoque);
        produto.setEstoqueMinimo(estoqueMinimo);
        produto.setCategoria(categoria);

        return produtoRepository.save(produto);
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));
    }

    public List<Produto> listarPorCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new NoSuchElementException("Categoria não encontrada.");
        }

        return produtoRepository.findByCategoriaId(categoriaId);
    }
}
