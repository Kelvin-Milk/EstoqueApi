<template>
  <section>
    <div class="cabecalho">
      <h2>Produtos</h2>
      <Button label="Novo produto" icon="pi pi-plus" @click="novo" />
    </div>

    <ProdutoTabela
      :produtos="produtos"
      :carregando="carregando"
      @editar="editar"
      @excluir="confirmarExclusao"
    />

    <ProdutoFormulario
      v-model:visible="formularioVisivel"
      :produto="produtoSelecionado"
      :tipos="tipos"
      @salvo="carregar"
    />
  </section>
</template>

<style scoped>
.cabecalho {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
</style>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Button from 'primevue/button'
import { useConfirm } from 'primevue/useconfirm'
import { useToast } from 'primevue/usetoast'
import ProdutoTabela from '@/components/ProdutoTabela.vue'
import ProdutoFormulario from '@/components/ProdutoFormulario.vue'
import { useProdutos } from '@/composables/useProdutos'
import { tipoProdutoApi } from '@/api/tipoProdutoApi'
import { extrairErro } from '@/api/http'
import type { Produto, TipoProduto } from '@/types/produto'

const { produtos, carregando, carregar, excluir } = useProdutos()
const tipos = ref<TipoProduto[]>([])
const formularioVisivel = ref(false)
const produtoSelecionado = ref<Produto | null>(null)
const confirm = useConfirm()
const toast = useToast()

onMounted(async () => {
  try {
    const [, listaTipos] = await Promise.all([carregar(), tipoProdutoApi.listar()])
    tipos.value = listaTipos
  } catch (erro) {
    mostrarErro(erro)
  }
})

function novo() {
  produtoSelecionado.value = null
  formularioVisivel.value = true
}

function editar(produto: Produto) {
  produtoSelecionado.value = produto
  formularioVisivel.value = true
}

function confirmarExclusao(produto: Produto) {
  confirm.require({
    header: 'Excluir produto',
    message: `Deseja excluir o produto ${produto.codigo}?`,
    acceptLabel: 'Excluir',
    rejectLabel: 'Cancelar',
    accept: async () => {
      try {
        await excluir(produto.id)
        toast.add({ severity: 'success', summary: 'Produto excluído', life: 3000 })
        await carregar()
      } catch (erro) {
        mostrarErro(erro)
      }
    },
  })
}

function mostrarErro(erro: unknown) {
  const problema = extrairErro(erro)
  toast.add({ severity: 'error', summary: problema.title, detail: problema.detail, life: 5000 })
}
</script>
