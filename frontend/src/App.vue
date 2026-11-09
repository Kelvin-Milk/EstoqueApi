<template>
  <Toast />
  <ConfirmDialog />

  <header class="topo">
    <h1>Controle de Estoque</h1>
  </header>

  <main class="conteudo">
    <RouterView />
  </main>
</template>

<style scoped>
.topo {
  padding: 1rem 2rem;
  border-bottom: 1px solid var(--p-content-border-color);
}

.conteudo {
  padding: 1.5rem 2rem;
}
</style>

<script setup lang="ts">
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
</script>
