<template>
  <Toast />
  <ConfirmDialog />

  <header class="topo">
    <h1>Controle de Estoque</h1>
    <nav class="menu">
      <RouterLink to="/produtos">Produtos</RouterLink>
      <RouterLink to="/relatorios/produtos-por-tipo">Produtos por tipo</RouterLink>
      <RouterLink to="/relatorios/lucro-por-produto">Lucro por produto</RouterLink>
    </nav>
  </header>

  <main class="conteudo">
    <RouterView />
  </main>
</template>

<style scoped>
.topo {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  border-bottom: 1px solid var(--p-content-border-color, #e2e8f0);
}

.menu {
  display: flex;
  gap: 1.5rem;
}

.menu a {
  color: inherit;
  text-decoration: none;
  opacity: 0.7;
}

.menu a.router-link-active {
  opacity: 1;
  font-weight: 600;
  border-bottom: 2px solid var(--p-primary-color, #10b981);
}

.conteudo {
  padding: 1.5rem 2rem;
}
</style>

<script setup lang="ts">
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
</script>
