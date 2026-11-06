package com.example.demo.domain;

import com.example.demo.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    public MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(TipoMovimentacao tipo, Integer quantidade,
                               String observacao, Produto produto) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.produto = produto;
        this.dataMovimentacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public Produto getProduto() {
        return produto;
    }
}
