<template>
  <DataTable :value="produtos" :loading="carregando" data-key="id" striped-rows>
    <template #empty>Nenhum produto cadastrado.</template>

    <Column field="codigo" header="Código" sortable />
    <Column field="descricao" header="Descrição" sortable />
    <Column field="tipo.descricao" header="Tipo" sortable />
    <Column header="Valor no fornecedor">
      <template #body="{ data }">{{ moeda.format(data.valorFornecedor) }}</template>
    </Column>
    <Column field="quantidadeEstoque" header="Estoque" sortable />
    <Column header="Ações">
      <template #body="{ data }">
        <Button
          icon="pi pi-pencil"
          text
          rounded
          aria-label="Editar"
          @click="emit('editar', data)"
        />
        <Button
          icon="pi pi-trash"
          text
          rounded
          severity="danger"
          aria-label="Excluir"
          @click="emit('excluir', data)"
        />
      </template>
    </Column>
  </DataTable>
</template>

<script setup lang="ts">
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import type { Produto } from '@/types/produto'

defineProps<{ produtos: Produto[]; carregando: boolean }>()

const emit = defineEmits<{
  editar: [produto: Produto]
  excluir: [produto: Produto]
}>()

const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
</script>
