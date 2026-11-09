import { movimentoApi } from '@/api/movimentoApi'
import type { TipoMovimentacao } from '@/types/movimento'

export function useMovimentos() {
  function registrar(
    produtoId: number,
    tipo: TipoMovimentacao,
    quantidade: number,
    valorVenda: number | null,
  ) {
    return tipo === 'ENTRADA'
      ? movimentoApi.registrarEntrada(produtoId, { quantidade })
      : movimentoApi.registrarSaida(produtoId, { quantidade, valorVenda: valorVenda as number })
  }

  return { registrar }
}
