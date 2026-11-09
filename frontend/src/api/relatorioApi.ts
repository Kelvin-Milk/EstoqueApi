import { http } from './http'
import type { LucroPorProduto, ProdutoPorTipo } from '@/types/relatorio'

export const relatorioApi = {
    produtosPorTipo: (tipoId: number | null) =>
        http
            .get<ProdutoPorTipo[]>('/relatorios/produtos-por-tipo', { params: { tipoId: tipoId ?? undefined } })
            .then((r) => r.data),
    lucroPorProduto: () =>
        http.get<LucroPorProduto[]>('/relatorios/lucro-por-produto').then((r) => r.data),
}