<template>
  <Dialog
    v-model:visible="visivel"
    :header="editando ? 'Editar produto' : 'Novo produto'"
    modal
    :style="{ width: '28rem' }"
  >
    <form class="formulario" @submit.prevent="salvar">
      <label for="codigo">Código</label>
      <InputText id="codigo" v-model="form.codigo" :disabled="editando" :invalid="!!erros.codigo" />
      <small v-if="erros.codigo" class="erro">{{ erros.codigo }}</small>

      <label for="descricao">Descrição</label>
      <InputText id="descricao" v-model="form.descricao" :invalid="!!erros.descricao" />
      <small v-if="erros.descricao" class="erro">{{ erros.descricao }}</small>

      <label for="tipo">Tipo</label>
      <Select
        input-id="tipo"
        v-model="form.tipoId"
        :options="tipos"
        option-label="descricao"
        option-value="id"
        placeholder="Selecione"
        :invalid="!!erros.tipoId"
      />
      <small v-if="erros.tipoId" class="erro">{{ erros.tipoId }}</small>

      <label for="valor">Valor no fornecedor</label>
      <InputNumber
        input-id="valor"
        v-model="form.valorFornecedor"
        mode="currency"
        currency="BRL"
        locale="pt-BR"
        :invalid="!!erros.valorFornecedor"
      />
      <small v-if="erros.valorFornecedor" class="erro">{{ erros.valorFornecedor }}</small>

      <div class="acoes">
        <Button type="button" label="Cancelar" severity="secondary" @click="visivel = false" />
        <Button type="submit" label="Salvar" :loading="salvando" />
      </div>
    </form>
  </Dialog>
</template>

<style scoped>
.formulario {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.erro {
  color: var(--p-red-500);
}

.acoes {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  margin-top: 1rem;
}
</style>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Select from 'primevue/select'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { useProdutos } from '@/composables/useProdutos'
import { extrairErro } from '@/api/http'
import type { Produto, TipoProduto } from '@/types/produto'

const visivel = defineModel<boolean>('visible', { required: true })
const props = defineProps<{ produto: Produto | null; tipos: TipoProduto[] }>()
const emit = defineEmits<{ salvo: [] }>()
const toast = useToast()
const { salvar: salvarProduto } = useProdutos()

const form = reactive({
  codigo: '',
  descricao: '',
  tipoId: null as number | null,
  valorFornecedor: null as number | null,
})
const erros = ref<Record<string, string>>({})
const salvando = ref(false)
const editando = computed(() => props.produto !== null)

watch(visivel, (aberto) => {
  if (!aberto) return
  erros.value = {}
  form.codigo = props.produto?.codigo ?? ''
  form.descricao = props.produto?.descricao ?? ''
  form.tipoId = props.produto?.tipo.id ?? null
  form.valorFornecedor = props.produto?.valorFornecedor ?? null
})

async function salvar() {
  salvando.value = true
  erros.value = {}
  try {
    const dados = {
      descricao: form.descricao,
      tipoId: form.tipoId as number,
      valorFornecedor: form.valorFornecedor as number,
    }
    if (props.produto) {
      await salvarProduto(props.produto.id, dados)
    } else {
      await salvarProduto(null, { ...dados, codigo: form.codigo })
    }
    toast.add({ severity: 'success', summary: 'Produto salvo', life: 3000 })
    visivel.value = false
    emit('salvo')
  } catch (erro) {
    const problema = extrairErro(erro)
    if (problema.campos) {
      erros.value = problema.campos
    } else {
      toast.add({ severity: 'error', summary: problema.title, detail: problema.detail, life: 5000 })
    }
  } finally {
    salvando.value = false
  }
}
</script>
