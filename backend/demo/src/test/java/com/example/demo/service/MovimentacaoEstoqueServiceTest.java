
package com.example.demo.service;

import com.example.demo.domain.MovimentacaoEstoque;
import com.example.demo.domain.Produto;
import com.example.demo.domain.enums.TipoMovimentacao;
import com.example.demo.repository.MovimentacaoEstoqueRepository;
import com.example.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private MovimentacaoEstoqueService service;

    private Produto produto;

    @BeforeEach
    void configurar() {
        produto = new Produto();
        produto.setQuantidadeEstoque(10);

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));
    }

    @Test
    void deveAumentarEstoqueAoRegistrarEntrada() {
        when(movimentacaoRepository.save(any(MovimentacaoEstoque.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(1L, TipoMovimentacao.ENTRADA, 5, "Compra");

        assertEquals(15, produto.getQuantidadeEstoque());
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveDiminuirEstoqueAoRegistrarSaida() {
        when(movimentacaoRepository.save(any(MovimentacaoEstoque.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(1L, TipoMovimentacao.SAIDA, 3, "Venda");

        assertEquals(7, produto.getQuantidadeEstoque());
        verify(produtoRepository).save(produto);
    }

    @Test
    void naoDevePermitirSaidaMaiorQueEstoque() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(
                        1L, TipoMovimentacao.SAIDA, 20, "Venda"
                )
        );

        assertEquals(
                "Estoque insuficiente para realizar essa saída.",
                erro.getMessage()
        );

        verify(produtoRepository, never()).save(any(Produto.class));
        verify(movimentacaoRepository, never())
                .save(any(MovimentacaoEstoque.class));
    }
}