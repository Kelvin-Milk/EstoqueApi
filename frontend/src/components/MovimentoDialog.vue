<template>
  <Dialog
    v-model:visible="visivel"
    :header="saida ? 'Registrar saída' : 'Registrar entrada'"
    modal
    :style="{ width: '24rem' }"
  >
    <form v-if="produto" class="formulario" @submit.prevent="registrar">
      <p class="info">
        <strong>{{ produto.codigo }}</strong> – {{ produto.descricao }}<br />
        Estoque atual: {{ produto.quantidadeEstoque }}
      </p>

      <label for="quantidade">Quantidade</label>
      <InputNumber
        input-id="quantidade"
        v-model="form.quantidade"
        :min="1"
        :use-grouping="false"
        :invalid="!!erros.quantidade"
      />
      <small v-if="erros.quantidade" class="erro">{{ erros.quantidade }}</small>

      <template v-if="saida">
        <label for="valorVenda">Valor de venda (unitário)</label>
        <InputNumber
          input-id="valorVenda"
          v-model="form.valorVenda"
          mode="currency"
          currency="BRL"
          locale="pt-BR"
          :invalid="!!erros.valorVenda"
        />
        <small v-if="erros.valorVenda" class="erro">{{ erros.valorVenda }}</small>
      </template>

      <div class="acoes">
        <Button type="button" label="Cancelar" severity="secondary" @click="visivel = false" />
        <Button
          type="submit"
          :label="saida ? 'Registrar saída' : 'Registrar entrada'"
          :severity="saida ? 'warn' : 'success'"
          :loading="salvando"
        />
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

.info {
  margin: 0 0 0.5rem;
}

.erro {
  color: var(--p-red-500, #ef4444);
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
import InputNumber from 'primevue/inputnumber'
import Button from 'primevue/button'
import { useToast } from 'primevue/usetoast'
import { useMovimentos } from '@/composables/useMovimentos'
import { extrairErro } from '@/api/http'
import type { Produto } from '@/types/produto'
import type { TipoMovimentacao } from '@/types/movimento'

const visivel = defineModel<boolean>('visible', { required: true })
const props = defineProps<{ produto: Produto | null; tipo: TipoMovimentacao }>()
const emit = defineEmits<{ registrado: [] }>()
const toast = useToast()
const { registrar: registrarMovimento } = useMovimentos()

const form = reactive({
  quantidade: null as number | null,
  valorVenda: null as number | null,
})
const erros = ref<Record<string, string>>({})
const salvando = ref(false)
const saida = computed(() => props.tipo === 'SAIDA')

watch(visivel, (aberto) => {
  if (!aberto) return
  erros.value = {}
  form.quantidade = null
  form.valorVenda = null
})

async function registrar() {
  if (!props.produto) return
  salvando.value = true
  erros.value = {}
  try {
    const movimento = await registrarMovimento(
      props.produto.id,
      props.tipo,
      form.quantidade as number,
      form.valorVenda,
    )
    toast.add({
      severity: 'success',
      summary: saida.value ? 'Saída registrada' : 'Entrada registrada',
      detail: `Saldo atual: ${movimento.saldoAtual}`,
      life: 3000,
    })
    visivel.value = false
    emit('registrado')
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
