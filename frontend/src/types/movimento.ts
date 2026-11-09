export type TipoMovimentacao = 'ENTRADA' | 'SAIDA'

export interface RegistrarEntradaRequest {
  quantidade: number
}

export interface RegistrarSaidaRequest {
  quantidade: number
  valorVenda: number
}

export interface MovimentoEstoque {
  id: number
  produtoId: number
  tipo: TipoMovimentacao
  quantidade: number
  valorVenda: number | null
  custoUnitario: number
  dataMovimentacao: string
  saldoAtual: number
}
