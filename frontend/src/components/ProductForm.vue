<script setup>
import { ref } from 'vue'
import { apiUrl } from '../lib/api.js'

const emit = defineEmits(['created'])

const form = ref({
  name: '',
  description: '',
  category: '',
  price: null,
  stockQuantity: 0,
})
const submitting = ref(false)
const error = ref(null)

async function handleSubmit() {
  submitting.value = true
  error.value = null
  try {
    const response = await fetch(apiUrl('/api/products'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })
    if (!response.ok) {
      throw new Error(`Server antwortete mit Status ${response.status}`)
    }
    const created = await response.json()
    emit('created', created)
    form.value = { name: '', description: '', category: '', price: null, stockQuantity: 0 }
  } catch (err) {
    error.value = err.message
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <form class="form" @submit.prevent="handleSubmit">
    <span class="eyebrow">Admin</span>
    <h2>Neues Produkt</h2>

    <div class="fields">
      <label>
        <span>Name</span>
        <input v-model="form.name" placeholder="z. B. Tour-Shirt" required />
      </label>
      <label>
        <span>Kategorie</span>
        <input v-model="form.category" placeholder="z. B. Bekleidung" />
      </label>
      <label class="full">
        <span>Beschreibung</span>
        <input v-model="form.description" placeholder="Kurzbeschreibung" />
      </label>
      <label>
        <span>Preis (€)</span>
        <input v-model.number="form.price" type="number" step="0.01" min="0" required />
      </label>
      <label>
        <span>Lagerbestand</span>
        <input v-model.number="form.stockQuantity" type="number" min="0" required />
      </label>
    </div>

    <div class="actions">
      <button type="submit" class="btn-primary riso-shift" :disabled="submitting">
        {{ submitting ? 'Speichere…' : 'Produkt anlegen' }}
      </button>
      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </form>
</template>

<style scoped>
.eyebrow {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--riso-blue);
}

h2 {
  font-size: 1.2rem;
  margin: 0.3rem 0 1rem;
}

.fields {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.9rem;
}

.full {
  grid-column: 1 / -1;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  opacity: 0.75;
}

input {
  font-family: var(--font-body);
  font-size: 0.95rem;
  text-transform: none;
  letter-spacing: normal;
  padding: 0.55rem 0.6rem;
  border: 2px solid var(--ink);
  background: var(--paper);
  color: var(--ink);
}

input:focus-visible {
  outline: 2px solid var(--riso-blue);
  outline-offset: 2px;
}

.actions {
  margin-top: 1.2rem;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.btn-primary {
  background: var(--ink);
  color: var(--paper);
  border: 2px solid var(--ink);
  font-family: var(--font-mono);
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  padding: 0.65rem 1.3rem;
  cursor: pointer;
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.error {
  color: var(--riso-red);
  font-family: var(--font-mono);
  font-size: 0.85rem;
  margin: 0;
}

@media (max-width: 480px) {
  .fields {
    grid-template-columns: 1fr;
  }
}
</style>
