
import { useEffect, useState } from 'react'
import './App.css'

import {
  buscarCategorias,
  buscarProdutos,
  buscarMovimentacoes,
  cadastrarCategoria,
  cadastrarProduto,
  cadastrarMovimentacao,
  atualizarProduto
} from './services/api'

function App() {
  const [categorias, setCategorias] = useState([])
  const [produtos, setProdutos] = useState([])
  const [movimentacoes, setMovimentacoes] = useState([])

  const [nomeCategoria, setNomeCategoria] = useState('')
  const [descricaoCategoria, setDescricaoCategoria] = useState('')

  const [nomeProduto, setNomeProduto] = useState('')
  const [codigoProduto, setCodigoProduto] = useState('')
  const [precoProduto, setPrecoProduto] = useState('')
  const [quantidadeEstoque, setQuantidadeEstoque] = useState('')
  const [estoqueMinimo, setEstoqueMinimo] = useState('')
  const [categoriaId, setCategoriaId] = useState('')

  const [produtoEditandoId, setProdutoEditandoId] = useState(null)
  const [formEdicao, setFormEdicao] = useState({
    nome: '',
    codigo: '',
    preco: '',
    estoqueMinimo: '',
    categoriaId: ''
  })

  const [produtoMovimentacao, setProdutoMovimentacao] = useState('')
  const [tipoMovimentacao, setTipoMovimentacao] = useState('ENTRADA')
  const [quantidadeMovimentacao, setQuantidadeMovimentacao] = useState('')
  const [observacao, setObservacao] = useState('')

  const [carregando, setCarregando] = useState(true)
  const [salvando, setSalvando] = useState(false)

  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregarDados() {
    try {
      const [categoriasData, produtosData, movimentacoesData] =
        await Promise.all([
          buscarCategorias(),
          buscarProdutos(),
          buscarMovimentacoes()
        ])

      setCategorias(categoriasData)
      setProdutos(produtosData)
      setMovimentacoes(movimentacoesData)
    } catch (error) {
      setErro(error.message)
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarDados()
  }, [])

  async function handleCadastrarCategoria(event) {
    event.preventDefault()
    setErro('')
    setSucesso('')

    try {
      setSalvando(true)

      await cadastrarCategoria({
        nome: nomeCategoria.trim(),
        descricao: descricaoCategoria.trim()
      })

      setNomeCategoria('')
      setDescricaoCategoria('')
      setSucesso('Categoria cadastrada com sucesso!')

      await carregarDados()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  async function handleCadastrarProduto(event) {
    event.preventDefault()
    setErro('')
    setSucesso('')

    try {
      setSalvando(true)

      await cadastrarProduto({
        nome: nomeProduto.trim(),
        codigo: codigoProduto.trim(),
        preco: Number(precoProduto),
        quantidadeEstoque: Number(quantidadeEstoque),
        estoqueMinimo: Number(estoqueMinimo),
        categoriaId: Number(categoriaId)
      })

      setNomeProduto('')
      setCodigoProduto('')
      setPrecoProduto('')
      setQuantidadeEstoque('')
      setEstoqueMinimo('')
      setCategoriaId('')

      setSucesso('Produto cadastrado com sucesso!')

      await carregarDados()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  function iniciarEdicao(produto) {
    setProdutoEditandoId(produto.id)

    setFormEdicao({
      nome: produto.nome,
      codigo: produto.codigo,
      preco: produto.preco,
      estoqueMinimo: produto.estoqueMinimo,
      categoriaId: String(produto.categoriaId)
    })

    setErro('')
    setSucesso('')
  }

  function cancelarEdicao() {
    setProdutoEditandoId(null)

    setFormEdicao({
      nome: '',
      codigo: '',
      preco: '',
      estoqueMinimo: '',
      categoriaId: ''
    })
  }

  async function handleAtualizarProduto(event) {
    event.preventDefault()
    setErro('')
    setSucesso('')

    const produtoOriginal = produtos.find(
      (produto) => produto.id === produtoEditandoId
    )

    if (!produtoOriginal) {
      setErro('Produto não encontrado.')
      return
    }

    try {
      setSalvando(true)

      await atualizarProduto(produtoEditandoId, {
        nome: formEdicao.nome.trim(),
        codigo: formEdicao.codigo.trim(),
        preco: Number(formEdicao.preco),
        quantidadeEstoque: produtoOriginal.quantidadeEstoque,
        estoqueMinimo: Number(formEdicao.estoqueMinimo),
        categoriaId: Number(formEdicao.categoriaId)
      })

      cancelarEdicao()
      setSucesso('Produto atualizado com sucesso!')

      await carregarDados()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  async function handleCadastrarMovimentacao(event) {
    event.preventDefault()
    setErro('')
    setSucesso('')

    try {
      setSalvando(true)

      await cadastrarMovimentacao({
        produtoId: Number(produtoMovimentacao),
        tipo: tipoMovimentacao,
        quantidade: Number(quantidadeMovimentacao),
        observacao: observacao.trim()
      })

      setProdutoMovimentacao('')
      setTipoMovimentacao('ENTRADA')
      setQuantidadeMovimentacao('')
      setObservacao('')

      setSucesso('Movimentação registrada com sucesso!')

      await carregarDados()
    } catch (error) {
      setErro(error.message)
    } finally {
      setSalvando(false)
    }
  }

  return (
    <div className="container">
      <header>
        <h1>StockManager</h1>
        <p>Sistema de Gerenciamento de Estoque</p>
      </header>

      <main>
        <h2>Painel de Controle</h2>

        {carregando && <p>Carregando dados...</p>}

        {erro && <p style={{ color: 'red' }}>{erro}</p>}
        {sucesso && <p style={{ color: 'green' }}>{sucesso}</p>}

        {!carregando && (
          <>
            <div className="cards">
              <div className="card">
                <h3>Produtos</h3>
                <p>{produtos.length} cadastrados</p>
              </div>

              <div className="card">
                <h3>Categorias</h3>
                <p>{categorias.length} cadastradas</p>
              </div>

              <div className="card">
                <h3>Movimentações</h3>
                <p>{movimentacoes.length} registradas</p>
              </div>
            </div>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Cadastrar categoria</h2>

              <form onSubmit={handleCadastrarCategoria}>
                <div>
                  <label>Nome da categoria</label>
                  <input
                    type="text"
                    value={nomeCategoria}
                    onChange={(event) => setNomeCategoria(event.target.value)}
                    placeholder="Ex.: Informática"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Descrição</label>
                  <input
                    type="text"
                    value={descricaoCategoria}
                    onChange={(event) => setDescricaoCategoria(event.target.value)}
                    placeholder="Descrição da categoria"
                  />
                </div>

                <button type="submit" disabled={salvando}>
                  {salvando ? 'Salvando...' : 'Cadastrar categoria'}
                </button>
              </form>
            </section>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Cadastrar produto</h2>

              <form onSubmit={handleCadastrarProduto}>
                <div>
                  <label>Nome do produto</label>
                  <input
                    type="text"
                    value={nomeProduto}
                    onChange={(event) => setNomeProduto(event.target.value)}
                    placeholder="Ex.: Mouse sem fio"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Código</label>
                  <input
                    type="text"
                    value={codigoProduto}
                    onChange={(event) => setCodigoProduto(event.target.value)}
                    placeholder="Ex.: MOUSE001"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Preço</label>
                  <input
                    type="number"
                    min="0.01"
                    step="0.01"
                    value={precoProduto}
                    onChange={(event) => setPrecoProduto(event.target.value)}
                    placeholder="Ex.: 89.90"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Quantidade em estoque</label>
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={quantidadeEstoque}
                    onChange={(event) => setQuantidadeEstoque(event.target.value)}
                    placeholder="Ex.: 20"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Estoque mínimo</label>
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={estoqueMinimo}
                    onChange={(event) => setEstoqueMinimo(event.target.value)}
                    placeholder="Ex.: 5"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Categoria</label>
                  <select
                    value={categoriaId}
                    onChange={(event) => setCategoriaId(event.target.value)}
                    required
                  >
                    <option value="">Selecione uma categoria</option>
                    {categorias.map((categoria) => (
                      <option key={categoria.id} value={categoria.id}>
                        {categoria.nome}
                      </option>
                    ))}
                  </select>
                </div>

                <button type="submit" disabled={salvando || categorias.length === 0}>
                  {salvando ? 'Salvando...' : 'Cadastrar produto'}
                </button>
              </form>
            </section>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Movimentação de estoque</h2>

              <form onSubmit={handleCadastrarMovimentacao}>
                <div>
                  <label>Produto</label>
                  <select
                    value={produtoMovimentacao}
                    onChange={(event) => setProdutoMovimentacao(event.target.value)}
                    required
                  >
                    <option value="">Selecione um produto</option>
                    {produtos.map((produto) => (
                      <option key={produto.id} value={produto.id}>
                        {produto.nome} - Estoque: {produto.quantidadeEstoque}
                      </option>
                    ))}
                  </select>
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Tipo de movimentação</label>
                  <select
                    value={tipoMovimentacao}
                    onChange={(event) => setTipoMovimentacao(event.target.value)}
                    required
                  >
                    <option value="ENTRADA">Entrada</option>
                    <option value="SAIDA">Saída</option>
                  </select>
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Quantidade</label>
                  <input
                    type="number"
                    min="1"
                    step="1"
                    value={quantidadeMovimentacao}
                    onChange={(event) => setQuantidadeMovimentacao(event.target.value)}
                    placeholder="Ex.: 5"
                    required
                  />
                </div>

                <div style={{ marginTop: '15px' }}>
                  <label>Observação</label>
                  <input
                    type="text"
                    value={observacao}
                    onChange={(event) => setObservacao(event.target.value)}
                    placeholder="Ex.: Reposição de estoque"
                  />
                </div>

                <button
                  type="submit"
                  disabled={salvando || produtos.length === 0}
                >
                  {salvando ? 'Registrando...' : 'Registrar movimentação'}
                </button>
              </form>
            </section>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Categorias cadastradas</h2>

              {categorias.length === 0 ? (
                <p>Nenhuma categoria cadastrada.</p>
              ) : (
                <ul>
                  {categorias.map((categoria) => (
                    <li key={categoria.id}>
                      <strong>{categoria.nome}</strong>
                      {' - '}
                      {categoria.descricao || 'Sem descrição'}
                    </li>
                  ))}
                </ul>
              )}
            </section>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Produtos cadastrados</h2>

              {produtos.length === 0 ? (
                <p>Nenhum produto cadastrado.</p>
              ) : (
                <ul>
                  {produtos.map((produto) => (
                    <li key={produto.id} style={{ marginBottom: '15px' }}>
                      <strong>{produto.nome}</strong>
                      {' - '}Código: {produto.codigo}
                      {' - '}Preço: R$ {Number(produto.preco).toFixed(2)}
                      {' - '}Estoque: {produto.quantidadeEstoque}
                      {' - '}Categoria: {produto.categoriaNome}

                      <div style={{ marginTop: '8px' }}>
                        <button
                          type="button"
                          onClick={() => iniciarEdicao(produto)}
                          disabled={salvando}
                        >
                          Editar
                        </button>
                      </div>

                      {produtoEditandoId === produto.id && (
                        <form
                          onSubmit={handleAtualizarProduto}
                          style={{
                            marginTop: '15px',
                            padding: '15px',
                            backgroundColor: '#f1f5f9',
                            borderRadius: '8px'
                          }}
                        >
                          <h3>Editar produto</h3>

                          <div style={{ marginTop: '10px' }}>
                            <label>Nome</label>
                            <input
                              type="text"
                              value={formEdicao.nome}
                              onChange={(event) =>
                                setFormEdicao({
                                  ...formEdicao,
                                  nome: event.target.value
                                })
                              }
                              required
                            />
                          </div>

                          <div style={{ marginTop: '10px' }}>
                            <label>Código</label>
                            <input
                              type="text"
                              value={formEdicao.codigo}
                              onChange={(event) =>
                                setFormEdicao({
                                  ...formEdicao,
                                  codigo: event.target.value
                                })
                              }
                              required
                            />
                          </div>

                          <div style={{ marginTop: '10px' }}>
                            <label>Preço</label>
                            <input
                              type="number"
                              min="0.01"
                              step="0.01"
                              value={formEdicao.preco}
                              onChange={(event) =>
                                setFormEdicao({
                                  ...formEdicao,
                                  preco: event.target.value
                                })
                              }
                              required
                            />
                          </div>

                          <div style={{ marginTop: '10px' }}>
                            <label>Estoque mínimo</label>
                            <input
                              type="number"
                              min="0"
                              step="1"
                              value={formEdicao.estoqueMinimo}
                              onChange={(event) =>
                                setFormEdicao({
                                  ...formEdicao,
                                  estoqueMinimo: event.target.value
                                })
                              }
                              required
                            />
                          </div>

                          <div style={{ marginTop: '10px' }}>
                            <label>Categoria</label>
                            <select
                              value={formEdicao.categoriaId}
                              onChange={(event) =>
                                setFormEdicao({
                                  ...formEdicao,
                                  categoriaId: event.target.value
                                })
                              }
                              required
                            >
                              <option value="">Selecione uma categoria</option>
                              {categorias.map((categoria) => (
                                <option key={categoria.id} value={categoria.id}>
                                  {categoria.nome}
                                </option>
                              ))}
                            </select>
                          </div>

                          <p style={{ marginTop: '10px' }}>
                            Estoque atual: {produto.quantidadeEstoque}
                          </p>

                          <button type="submit" disabled={salvando}>
                            {salvando ? 'Salvando...' : 'Salvar alterações'}
                          </button>

                          <button
                            type="button"
                            onClick={cancelarEdicao}
                            disabled={salvando}
                            style={{ marginLeft: '10px' }}
                          >
                            Cancelar
                          </button>
                        </form>
                      )}
                    </li>
                  ))}
                </ul>
              )}
            </section>

            <section className="card" style={{ marginTop: '30px' }}>
              <h2>Histórico de movimentações</h2>

              {movimentacoes.length === 0 ? (
                <p>Nenhuma movimentação registrada.</p>
              ) : (
                <ul>
                  {movimentacoes.map((movimentacao) => (
                    <li key={movimentacao.id}>
                      {movimentacao.tipo} - {movimentacao.produtoNome}
                      {' - '}Quantidade: {movimentacao.quantidade}
                      {' - '}Data: {new Date(movimentacao.dataMovimentacao).toLocaleString('pt-BR')}
                      {' - '}{movimentacao.observacao || 'Sem observação'}
                    </li>
                  ))}
                </ul>
              )}
            </section>
          </>
        )}
      </main>
    </div>
  )
}

export default App
