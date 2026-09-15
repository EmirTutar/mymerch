<script setup>
import { useFetch } from '../composables/useFetch.js'

const { data: orders, loading, error } = useFetch('/api/orders')

const statusColors = {
  PENDING: 'yellow',
  PROCESSING: 'blue',
  SHIPPED: 'blue',
  DELIVERED: 'ink',
  CANCELLED: 'red',
}

function statusClass(status) {
  return `badge-${statusColors[status?.toUpperCase()] ?? 'ink'}`
}
</script>

<template>
  <div>
    <section class="hero">
      <div class="halftone-bg hero-texture" aria-hidden="true"></div>
      <h1>Bestellungen</h1>
    </section>

    <p v-if="loading" class="status-text">Lade Bestellungen&hellip;</p>
    <p v-else-if="error" class="status-text error">Fehler beim Laden: {{ error }}</p>
    <p v-else-if="orders.length === 0" class="status-text">Keine Bestellungen vorhanden.</p>

    <table v-else class="orders-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Kunde</th>
          <th>Status</th>
          <th>Summe</th>
        </tr>
      </thead>
      <TransitionGroup tag="tbody" name="row">
        <tr v-for="order in orders" :key="order.id">
          <td class="mono">#{{ order.id }}</td>
          <td>{{ order.customerName }}</td>
          <td><span class="badge" :class="statusClass(order.status)">{{ order.status }}</span></td>
          <td class="mono">{{ order.totalPrice }}&nbsp;€</td>
        </tr>
      </TransitionGroup>
    </table>
  </div>
</template>

<style scoped>
.hero {
  position: relative;
  overflow: hidden;
  background: var(--ink);
  color: var(--paper);
  padding: 3rem 2rem;
  margin-bottom: 2rem;
}

.hero-texture {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(var(--paper) 1px, transparent 1px);
  opacity: 0.12;
}

.hero h1 {
  position: relative;
  font-size: clamp(2rem, 6vw, 3.2rem);
  color: var(--paper);
}

.status-text {
  font-family: var(--font-mono);
  opacity: 0.7;
}

.status-text.error {
  color: var(--riso-red);
  opacity: 1;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
  border: 2px solid var(--ink);
}

.orders-table th,
.orders-table td {
  text-align: left;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--line);
}

.orders-table th {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  background: var(--paper-dim);
}

.orders-table tbody tr:last-child td {
  border-bottom: none;
}

.mono {
  font-family: var(--font-mono);
  font-size: 0.9rem;
}

.badge {
  display: inline-block;
  font-family: var(--font-mono);
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  padding: 0.2rem 0.6rem;
  border-radius: 999px;
  border: 1.5px solid var(--ink);
}

.badge-yellow {
  background: var(--riso-yellow);
}

.badge-blue {
  background: var(--riso-blue);
  color: var(--paper);
  border-color: var(--riso-blue);
}

.badge-red {
  background: var(--riso-red);
  color: var(--paper);
  border-color: var(--riso-red);
}

.badge-ink {
  background: var(--ink);
  color: var(--paper);
}

.row-enter-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}
.row-enter-from {
  opacity: 0;
  transform: translateY(6px);
}

@media (prefers-reduced-motion: reduce) {
  .row-enter-active {
    transition: none;
  }
}

@media (max-width: 560px) {
  .orders-table {
    font-size: 0.85rem;
  }
  .orders-table th,
  .orders-table td {
    padding: 0.5rem 0.6rem;
  }
}
</style>
