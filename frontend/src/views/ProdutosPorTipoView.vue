<template>
  <section>
    <h2>Produtos por tipo</h2>

    <div class="filtro">
      <label for="tipo">Tipo de produto</label>
      <Select
        input-id="tipo"
        v-model="tipoId"
        :options="tipos"
        option-label="descricao"
        option-value="id"
        placeholder="Selecione o tipo"
      />
    </div>

    <DataTable :value="itens" :loading="carregando" data-key="id" striped-rows>
      <template #empty>
        {{ tipoId ? 'Nenhum produto cadastrado para este tipo.' : 'Selecione um tipo.' }}
      </template>

      <Column field="codigo" header="Código" sortable />
      <Column field="descricao" header="Descrição" sortable />
      <Column field="quantidadeSaida" header="Quantidade de saída" sortable />
      <Column field="quantidadeDisponivel" header="Quantidade disponível" sortable />
    </DataTable>
  </section>
</template>

<style scoped>
.filtro {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}
</style>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Select from 'primevue/select'
import { useToast } from 'primevue/usetoast'
import { useProdutosPorTipo } from '@/composables/useRelatorios'
import { tipoProdutoApi } from '@/api/tipoProdutoApi'
import { extrairErro } from '@/api/http'
import type { TipoProduto } from '@/types/produto'

const { itens, carregando, buscar } = useProdutosPorTipo()
const tipos = ref<TipoProduto[]>([])
const tipoId = ref<number | null>(null)
const toast = useToast()

onMounted(async () => {
  try {
    const [listaTipos] = await Promise.all([tipoProdutoApi.listar(), buscar(null)])
    tipos.value = listaTipos
  } catch (erro) {
    mostrarErro(erro)
  }
})

watch(tipoId, async (id) => {
  try {
    await buscar(id)
  } catch (erro) {
    mostrarErro(erro)
  }
})

function mostrarErro(erro: unknown) {
  const problema = extrairErro(erro)
  toast.add({ severity: 'error', summary: problema.title, detail: problema.detail, life: 5000 })
}
</script>
