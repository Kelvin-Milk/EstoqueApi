<template>
  <section>
    <h2>Lucro por produto</h2>

    <DataTable :value="itens" :loading="carregando" data-key="id" striped-rows>
      <template #empty>Nenhum produto cadastrado.</template>

      <Column field="codigo" header="Código" sortable />
      <Column field="descricao" header="Descrição" sortable />
      <Column field="quantidadeSaida" header="Quantidade de saída" sortable />
      <Column field="totalLucro" header="Lucro total" sortable>
        <template #body="{ data }">
          <span :class="{ negativo: data.totalLucro < 0 }">{{
            formatarMoeda(data.totalLucro)
          }}</span>
        </template>
      </Column>

      <template #footer>
        Lucro total:
        <strong :class="{ negativo: lucroTotal < 0 }">{{ formatarMoeda(lucroTotal) }}</strong>
      </template>
    </DataTable>
  </section>
</template>

<style scoped>
.negativo {
  color: var(--p-red-500, #ef4444);
}
</style>

<script setup lang="ts">
import { onMounted } from 'vue'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import { useToast } from 'primevue/usetoast'
import { useLucroPorProduto } from '@/composables/useRelatorios'
import { extrairErro } from '@/api/http'
import { formatarMoeda } from '@/utils/formatacao'

const { itens, carregando, lucroTotal, carregar } = useLucroPorProduto()
const toast = useToast()

onMounted(async () => {
  try {
    await carregar()
  } catch (erro) {
    const problema = extrairErro(erro)
    toast.add({ severity: 'error', summary: problema.title, detail: problema.detail, life: 5000 })
  }
})
</script>
