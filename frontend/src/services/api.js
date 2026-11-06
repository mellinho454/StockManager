
const API_URL = "http://localhost:8080/api";

export async function buscarCategorias() {
  const resposta = await fetch(`${API_URL}/categorias`);
  if (!resposta.ok) throw new Error("Erro ao buscar categorias.");
  return resposta.json();
}

export async function buscarProdutos() {
  const resposta = await fetch(`${API_URL}/produtos`);
  if (!resposta.ok) throw new Error("Erro ao buscar produtos.");
  return resposta.json();
}

export async function buscarMovimentacoes() {
  const resposta = await fetch(`${API_URL}/movimentacoes`);
  if (!resposta.ok) throw new Error("Erro ao buscar movimentações.");
  return resposta.json();
}

export async function cadastrarCategoria(categoria) {
  const resposta = await fetch(`${API_URL}/categorias`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(categoria)
  });

  if (!resposta.ok) {
    const erro = await resposta.json();
    throw new Error(erro.mensagem || "Erro ao cadastrar categoria.");
  }

  return resposta.json();
}

export async function cadastrarProduto(produto) {
  const resposta = await fetch(`${API_URL}/produtos`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(produto)
  });

  if (!resposta.ok) {
    const erro = await resposta.json();
    throw new Error(erro.mensagem || "Erro ao cadastrar produto.");
  }

  return resposta.json();
}

export async function cadastrarMovimentacao(movimentacao) {
  const resposta = await fetch(`${API_URL}/movimentacoes`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(movimentacao)
  });

  if (!resposta.ok) {
    const erro = await resposta.json();
    throw new Error(
      erro.mensagem || "Erro ao registrar movimentação."
    );
  }

  return resposta.json();
}

export async function atualizarProduto(id, produto) {
  const resposta = await fetch(`${API_URL}/produtos/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(produto)
  });

  if (!resposta.ok) {
    const erro = await resposta.json();
    throw new Error(erro.mensagem || "Erro ao atualizar produto.");
  }

  return resposta.json();
}
