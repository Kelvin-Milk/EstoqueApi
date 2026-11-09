export interface ProdutoPorTipo {
  id: number
  codigo: string
  descricao: string
  quantidadeSaida: number
  quantidadeDisponivel: number
}

export interface LucroPorProduto {
  id: number
  codigo: string
  descricao: string
  quantidadeSaida: number
  totalLucro: number
}