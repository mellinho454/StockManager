
package com.example.demo.service;

import com.example.demo.domain.Categoria;
import com.example.demo.domain.Produto;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService service;

    private Categoria categoria;

    @BeforeEach
    void configurar() {
        categoria = new Categoria();
    }

    @Test
    void deveCadastrarProdutoValido() {
        when(produtoRepository.existsByCodigo("NOTE001"))
                .thenReturn(false);

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = service.cadastrar(
                "Notebook Lenovo",
                "NOTE001",
                new BigDecimal("3500.00"),
                10,
                3,
                1L
        );

        assertNotNull(resultado);
        assertEquals("Notebook Lenovo", resultado.getNome());
        assertEquals("NOTE001", resultado.getCodigo());
        assertEquals(10, resultado.getQuantidadeEstoque());

        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void naoDeveCadastrarProdutoComPrecoZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(
                        "Notebook",
                        "NOTE002",
                        BigDecimal.ZERO,
                        10,
                        3,
                        1L
                )
        );

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void naoDeveCadastrarProdutoComCodigoDuplicado() {
        when(produtoRepository.existsByCodigo("NOTE001"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(
                        "Notebook",
                        "NOTE001",
                        new BigDecimal("3500.00"),
                        10,
                        3,
                        1L
                )
        );

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void naoDeveCadastrarProdutoComCategoriaInexistente() {
        when(produtoRepository.existsByCodigo("NOTE003"))
                .thenReturn(false);

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> service.cadastrar(
                        "Notebook",
                        "NOTE003",
                        new BigDecimal("3500.00"),
                        10,
                        3,
                        1L
                )
        );

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void deveAtualizarProdutoValido() {
        Produto produto = new Produto(
                "Notebook antigo",
                "NOTE004",
                new BigDecimal("3000.00"),
                5,
                2,
                categoria
        );

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(produtoRepository.findByCodigo("NOTE004"))
                .thenReturn(Optional.empty());

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(produtoRepository.save(any(Produto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = service.atualizar(
                1L,
                "Notebook atualizado",
                "NOTE004",
                new BigDecimal("4000.00"),
                12,
                3,
                1L
        );

        assertEquals("Notebook atualizado", resultado.getNome());
        assertEquals(new BigDecimal("4000.00"), resultado.getPreco());
        assertEquals(12, resultado.getQuantidadeEstoque());

        verify(produtoRepository).save(produto);
    }
}
