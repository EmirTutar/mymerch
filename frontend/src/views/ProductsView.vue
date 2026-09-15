<script setup>
import { ref, computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useFetch } from '../composables/useFetch.js'
import ProductCard from '../components/ProductCard.vue'
import ProductForm from '../components/ProductForm.vue'

const { data: products, loading, error } = useFetch('/api/products')
const showForm = ref(false)
const search = ref('')

// computed() berechnet sich automatisch neu, sobald "products" oder "search"
// sich ändern - kein manuelles Neu-Filtern nötig, wie bei einer normalen Funktion.
const filteredProducts = computed(() => {
  if (!products.value) return []
  const term = search.value.trim().toLowerCase()
  if (!term) return products.value
  return products.value.filter((product) => product.name.toLowerCase().includes(term))
})

function handleCreated(newProduct) {
  products.value.push(newProduct)
  showForm.value = false
}
</script>

<template>
  <div>
    <section class="hero">
      <div class="halftone-bg hero-texture" aria-hidden="true"></div>
      <h1>Unsere Produkte</h1>
      <p class="hero-sub">Frisch bedruckt. Direkt versendet.</p>
    </section>

    <div class="toolbar">
      <input
        v-model="search"
        type="search"
        class="search-input"
        placeholder="Produkte durchsuchen…"
      />
      <button class="link-btn" @click="showForm = !showForm">
        {{ showForm ? '− Formular schließen' : '+ Neues Produkt anlegen' }}
      </button>
    </div>

    <Transition name="panel">
      <div v-if="showForm" class="admin-panel">
        <ProductForm @created="handleCreated" />
      </div>
    </Transition>

    <p v-if="loading" class="status-text">Lade Produkte&hellip;</p>
    <p v-else-if="error" class="status-text error">Fehler beim Laden: {{ error }}</p>
    <p v-else-if="products.length === 0" class="status-text">Keine Produkte vorhanden.</p>
    <p v-else-if="filteredProducts.length === 0" class="status-text">
      Keine Produkte gefunden für "{{ search }}".
    </p>

    <TransitionGroup v-else name="card" tag="div" class="grid">
      <RouterLink
        v-for="product in filteredProducts"
        :key="product.id"
        :to="`/products/${product.id}`"
        class="card-link"
      >
        <ProductCard :id="product.id" :name="product.name" :price="product.price" />
      </RouterLink>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.hero {
  position: relative;
  overflow: hidden;
  background: var(--ink);
  color: var(--paper);
  padding: 3rem 2rem;
  margin-bottom: 1.5rem;
}

.hero-texture {
  position: absolute;
  inset: 0;
  color: var(--paper);
  background-image: radial-gradient(var(--paper) 1px, transparent 1px);
  opacity: 0.12;
}

.hero h1 {
  position: relative;
  font-size: clamp(2rem, 6vw, 3.2rem);
  color: var(--paper);
}

.hero-sub {
  position: relative;
  font-family: var(--font-mono);
  margin: 0.6rem 0 0;
  opacity: 0.75;
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.search-input {
  font-family: var(--font-body);
  font-size: 0.9rem;
  padding: 0.5rem 0.75rem;
  border: 2px solid var(--ink);
  background: var(--paper);
  color: var(--ink);
  min-width: 220px;
}

.search-input:focus-visible {
  outline: 2px solid var(--riso-blue);
  outline-offset: 2px;
}

.link-btn {
  background: none;
  border: none;
  font-family: var(--font-mono);
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--riso-blue);
  cursor: pointer;
  padding: 0;
}

.link-btn:hover {
  text-decoration: underline;
}

.admin-panel {
  border: 2px dashed var(--line);
  background: var(--paper-dim);
  padding: 1.25rem;
  margin-bottom: 2rem;
}

.status-text {
  font-family: var(--font-mono);
  opacity: 0.7;
}

.status-text.error {
  color: var(--riso-red);
  opacity: 1;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1.5rem;
}

.card-link {
  text-decoration: none;
  color: inherit;
}

/* Panel: sanftes Auf-/Zuklappen des Formulars */
.panel-enter-active,
.panel-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.panel-enter-from,
.panel-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

/* Karten: Ein-/Ausblenden + sanftes Umsortieren bei der Suche */
.card-enter-active,
.card-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}
.card-enter-from,
.card-leave-to {
  opacity: 0;
  transform: translateY(8px);
}
.card-move {
  transition: transform 0.2s ease;
}

@media (prefers-reduced-motion: reduce) {
  .panel-enter-active,
  .panel-leave-active,
  .card-enter-active,
  .card-leave-active,
  .card-move {
    transition: none;
  }
}
</style>
