import axios, { AxiosError } from 'axios'
import type { ProblemDetail } from '@/types/problem'

export const http = axios.create({ baseURL: '/api/v1' })

export function extrairErro(erro: unknown): ProblemDetail {
  const resposta = (erro as AxiosError<ProblemDetail>).response?.data
  return resposta ?? { title: 'Erro', status: 0, detail: 'Não foi possível conectar à API' }
}
