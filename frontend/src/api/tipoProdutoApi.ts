import { http } from './http'
import type { TipoProduto } from '@/types/produto'

export const tipoProdutoApi = {
  listar: () => http.get<TipoProduto[]>('/tipos-produto').then((r) => r.data),
}