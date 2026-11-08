export interface TipoProdutoResumo {
  id: number
  descricao: string
}

export interface Produto {
  id: number
  codigo: string
  descricao: string
  tipo: TipoProdutoResumo
  quantidadeEstoque: number
  valorFornecedor: number
}

export interface CriarProdutoRequest {
  codigo: string
  descricao: string
  tipoId: number
  valorFornecedor: number
}

export type AtualizarProdutoRequest = Omit<CriarProdutoRequest, 'codigo'>

export interface TipoProduto {
  id: number
  descricao: string
}