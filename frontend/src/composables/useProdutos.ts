import { ref } from 'vue'
import { produtoApi } from '@/api/produtoApi'
import type { Produto, CriarProdutoRequest, AtualizarProdutoRequest } from '@/types/produto'

export function useProdutos() {
  const produtos = ref<Produto[]>([])
  const carregando = ref(false)

  async function carregar() {
    carregando.value = true
    try {
      produtos.value = await produtoApi.listar()
    } finally {
      carregando.value = false
    }
  }

  function salvar(id: number | null, dados: CriarProdutoRequest | AtualizarProdutoRequest) {
    return id === null
      ? produtoApi.criar(dados as CriarProdutoRequest)
      : produtoApi.atualizar(id, dados)
  }

  function excluir(id: number) {
    return produtoApi.excluir(id)
  }

  return { produtos, carregando, carregar, salvar, excluir }
}
