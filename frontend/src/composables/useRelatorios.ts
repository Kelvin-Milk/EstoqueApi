import { computed, ref } from 'vue'
import { relatorioApi } from '@/api/relatorioApi'
import type { LucroPorProduto, ProdutoPorTipo } from '@/types/relatorio'

export function useProdutosPorTipo() {
    const itens = ref<ProdutoPorTipo[]>([])
    const carregando = ref(false)

    async function buscar(tipoId: number | null) {
        carregando.value = true
        try {
            itens.value = await relatorioApi.produtosPorTipo(tipoId)
        } finally {
            carregando.value = false
        }
    }

    return { itens, carregando, buscar }
}

export function useLucroPorProduto() {
    const itens = ref<LucroPorProduto[]>([])
    const carregando = ref(false)
    const lucroTotal = computed(() => itens.value.reduce((total, item) => total + item.totalLucro, 0))

    async function carregar() {
        carregando.value = true
        try {
            itens.value = await relatorioApi.lucroPorProduto()
        } finally {
            carregando.value = false
        }
    }

    return { itens, carregando, lucroTotal, carregar }
}