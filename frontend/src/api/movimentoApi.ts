import { http } from './http'
import type {
  MovimentoEstoque,
  RegistrarEntradaRequest,
  RegistrarSaidaRequest,
} from '@/types/movimento'

export const movimentoApi = {
  registrarEntrada: (produtoId: number, dados: RegistrarEntradaRequest) =>
    http.post<MovimentoEstoque>(`/produtos/${produtoId}/entradas`, dados).then((r) => r.data),
  registrarSaida: (produtoId: number, dados: RegistrarSaidaRequest) =>
    http.post<MovimentoEstoque>(`/produtos/${produtoId}/saidas`, dados).then((r) => r.data),
}
