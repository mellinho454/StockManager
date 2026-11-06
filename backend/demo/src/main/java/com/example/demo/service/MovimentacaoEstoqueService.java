package com.example.demo.service;

import com.example.demo.domain.MovimentacaoEstoque;
import com.example.demo.domain.Produto;
import com.example.demo.domain.enums.TipoMovimentacao;
import com.example.demo.repository.MovimentacaoEstoqueRepository;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueService(
            MovimentacaoEstoqueRepository movimentacaoRepository,
            ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public MovimentacaoEstoque registrar(
            Long produtoId,
            TipoMovimentacao tipo,
            Integer quantidade,
            String observacao) {

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de movimentação é obrigatório.");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado."));

        if (tipo == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeEstoque(
                    produto.getQuantidadeEstoque() + quantidade
            );
        } else {
            if (quantidade > produto.getQuantidadeEstoque()) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para realizar essa saída."
                );
            }

            produto.setQuantidadeEstoque(
                    produto.getQuantidadeEstoque() - quantidade
            );
        }

        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                tipo,
                quantidade,
                observacao,
                produto
        );

        return movimentacaoRepository.save(movimentacao);
    }

    public List<MovimentacaoEstoque> listar() {
        return movimentacaoRepository.findAll();
    }

    public List<MovimentacaoEstoque> listarPorProduto(Long produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new NoSuchElementException("Produto não encontrado.");
        }

        return movimentacaoRepository
                .findByProdutoIdOrderByDataMovimentacaoDesc(produtoId);
    }
}