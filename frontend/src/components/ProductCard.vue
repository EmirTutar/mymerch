<script setup>
import { useCartStore } from '../stores/cart.js'

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  price: {
    type: Number,
    required: true,
  },
})

const cart = useCartStore()

function addToCart(event) {
  // .prevent + .stop, weil die Karte in einen RouterLink eingebettet ist -
  // sonst würde der Klick zusätzlich zur Detailseite navigieren.
  event.preventDefault()
  event.stopPropagation()
  cart.addItem({ id: props.id, name: props.name, price: props.price })
}
</script>

<template>
  <div class="card riso-shift">
    <div class="card-top halftone-bg" aria-hidden="true"></div>
    <div class="card-body">
      <h3>{{ name }}</h3>
      <div class="card-footer">
        <span class="price">{{ price.toFixed(2) }}&nbsp;€</span>
        <button class="add-btn" @click="addToCart">+ Warenkorb</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  background: var(--paper);
  border: 2px solid var(--ink);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.card-top {
  height: 90px;
  background-color: var(--paper-dim);
  border-bottom: 2px dashed var(--ink);
}

.card-body {
  padding: 1rem 1.1rem 1.2rem;
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
}

h3 {
  font-size: 1rem;
  line-height: 1.15;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
}

.price {
  font-family: var(--font-mono);
  font-size: 0.85rem;
  font-weight: 600;
  background: var(--riso-yellow);
  border: 1.5px solid var(--ink);
  border-radius: 999px;
  padding: 0.2rem 0.7rem;
  white-space: nowrap;
}

.add-btn {
  font-family: var(--font-mono);
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.03em;
  background: var(--ink);
  color: var(--paper);
  border: 1.5px solid var(--ink);
  padding: 0.35rem 0.55rem;
  cursor: pointer;
  white-space: nowrap;
}

.add-btn:hover {
  background: var(--riso-blue);
  border-color: var(--riso-blue);
}
</style>
