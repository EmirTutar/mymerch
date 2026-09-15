import { createRouter, createWebHistory } from 'vue-router'
import ProductsView from './views/ProductsView.vue'
import ProductDetailView from './views/ProductDetailView.vue'
import OrdersView from './views/OrdersView.vue'
import CartView from './views/CartView.vue'

// Jede "route" ist eine Zuordnung: URL-Pfad -> Komponente
// :id ist ein Platzhalter - passt auf /products/1, /products/42, usw.
const routes = [
  { path: '/', redirect: '/products' },
  { path: '/products', component: ProductsView },
  { path: '/products/:id', component: ProductDetailView },
  { path: '/orders', component: OrdersView },
  { path: '/cart', component: CartView },
]

const router = createRouter({
  // createWebHistory nutzt "saubere" URLs wie /products statt /#/products
  history: createWebHistory(),
  routes,
})

export default router
