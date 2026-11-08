import { http } from './http'
import type { Produto, CriarProdutoRequest, AtualizarProdutoRequest } from '@/types/produto'

export const produtoApi = {
  listar: () => http.get<Produto[]>('/produtos').then((r) => r.data),
  buscar: (id: number) => http.get<Produto>(`/produtos/${id}`).then((r) => r.data),
  criar: (dados: CriarProdutoRequest) => http.post<Produto>('/produtos', dados).then((r) => r.data),
  atualizar: (id: number, dados: AtualizarProdutoRequest) =>
    http.put<Produto>(`/produtos/${id}`, dados).then((r) => r.data),
  excluir: (id: number) => http.delete(`/produtos/${id}`),
}