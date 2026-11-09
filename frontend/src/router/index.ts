import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/produtos' },
    { path: '/produtos', name: 'produtos', component: () => import('@/views/ProdutosView.vue') },
    {
      path: '/relatorios/produtos-por-tipo',
      name: 'produtos-por-tipo',
      component: () => import('@/views/ProdutosPorTipoView.vue'),
    },
    {
      path: '/relatorios/lucro-por-produto',
      name: 'lucro-por-produto',
      component: () => import('@/views/LucroPorProdutoView.vue'),
    },
  ],
})

export default router
