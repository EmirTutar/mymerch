<script setup>
import { ref } from 'vue'
import { useRoute, useRouter, RouterLink } from 'vue-router'
import { useCartStore } from '../stores/cart.js'
import { useFetch } from '../composables/useFetch.js'
import { apiUrl } from '../lib/api.js'

const route = useRoute()
const router = useRouter()
const cart = useCartStore()

// Die Pfad-Funktion () => `...${route.params.id}` macht den Fetch reaktiv:
// wechselt man direkt von Produkt 1 zu Produkt 2, lädt useFetch automatisch neu.
const { data: product, loading, error } = useFetch(() => `/api/products/${route.params.id}`)

const editing = ref(false)
const editForm = ref(null)
const saving = ref(false)
const confirmingDelete = ref(false)
const deleting = ref(false)
const actionError = ref(null)

function startEditing() {
  // Kopie statt Referenz, damit Tippen im Formular nicht sofort die Anzeige verändert,
  // bevor gespeichert wurde ({ ...product } erzeugt ein neues, flaches Objekt).
  editForm.value = { ...product.value }
  editing.value = true
}

async function saveEdit() {
  saving.value = true
  actionError.value = null
  try {
    const response = await fetch(apiUrl(`/api/products/${product.value.id}`), {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editForm.value),
    })
    if (!response.ok) {
      throw new Error(`Server antwortete mit Status ${response.status}`)
    }
    product.value = await response.json()
    editing.value = false
  } catch (err) {
    actionError.value = err.message
  } finally {
    saving.value = false
  }
}

async function confirmDelete() {
  deleting.value = true
  actionError.value = null
  try {
    const response = await fetch(apiUrl(`/api/products/${product.value.id}`), { method: 'DELETE' })
    if (!response.ok) {
      throw new Error(`Server antwortete mit Status ${response.status}`)
    }
    router.push('/products')
  } catch (err) {
    actionError.value = err.message
    deleting.value = false
  }
}
</script>

<template>
  <div>
    <RouterLink to="/products" class="back-link">&larr; Zurück zur Übersicht</RouterLink>

    <p v-if="loading" class="status-text">Lade Produkt&hellip;</p>
    <p v-else-if="error" class="status-text error">Fehler beim Laden: {{ error }}</p>

    <article v-else-if="product && !editing" class="detail">
      <div class="visual halftone-bg" aria-hidden="true"></div>

      <div class="info">
        <span v-if="product.category" class="badge">{{ product.category }}</span>
        <h1>{{ product.name }}</h1>
        <p v-if="product.description" class="description">{{ product.description }}</p>

        <div class="meta">
          <span class="price">{{ product.price.toFixed(2) }}&nbsp;€</span>
          <span class="stock">Lager: {{ product.stockQuantity }}</span>
        </div>

        <div class="actions">
          <button class="add-btn riso-shift" @click="cart.addItem(product)">
            In den Warenkorb
          </button>
          <button class="text-btn" @click="startEditing">Bearbeiten</button>

          <template v-if="!confirmingDelete">
            <button class="text-btn danger" @click="confirmingDelete = true">Löschen</button>
          </template>
          <template v-else>
            <span class="confirm-text">Wirklich löschen?</span>
            <button class="text-btn danger" :disabled="deleting" @click="confirmDelete">
              {{ deleting ? 'Lösche…' : 'Ja, löschen' }}
            </button>
            <button class="text-btn" @click="confirmingDelete = false">Abbrechen</button>
          </template>
        </div>

        <p v-if="actionError" class="inline-error">{{ actionError }}</p>
      </div>
    </article>

    <form v-else-if="product && editing" class="edit-form" @submit.prevent="saveEdit">
      <h2>Produkt bearbeiten</h2>

      <label>
        <span>Name</span>
        <input v-model="editForm.name" required />
      </label>
      <label>
        <span>Kategorie</span>
        <input v-model="editForm.category" />
      </label>
      <label>
        <span>Beschreibung</span>
        <input v-model="editForm.description" />
      </label>
      <div class="edit-row">
        <label>
          <span>Preis (€)</span>
          <input v-model.number="editForm.price" type="number" step="0.01" min="0" required />
        </label>
        <label>
          <span>Lagerbestand</span>
          <input v-model.number="editForm.stockQuantity" type="number" min="0" required />
        </label>
      </div>

      <div class="actions">
        <button type="submit" class="add-btn riso-shift" :disabled="saving">
          {{ saving ? 'Speichere…' : 'Speichern' }}
        </button>
        <button type="button" class="text-btn" @click="editing = false">Abbrechen</button>
      </div>

      <p v-if="actionError" class="inline-error">{{ actionError }}</p>
    </form>
  </div>
</template>

<style scoped>
.back-link {
  display: inline-block;
  font-family: var(--font-mono);
  font-size: 0.85rem;
  text-decoration: none;
  color: var(--riso-blue);
  margin-bottom: 1.5rem;
}

.back-link:hover {
  text-decoration: underline;
}

.status-text {
  font-family: var(--font-mono);
  opacity: 0.7;
}

.status-text.error {
  color: var(--riso-red);
  opacity: 1;
}

.detail {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 2rem;
  border: 2px solid var(--ink);
}

.visual {
  background: var(--paper-dim);
  border-right: 2px dashed var(--ink);
  min-height: 260px;
}

.info {
  padding: 1.75rem 1.75rem 1.75rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.badge {
  align-self: flex-start;
  font-family: var(--font-mono);
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  background: var(--riso-blue);
  color: var(--paper);
  padding: 0.2rem 0.6rem;
  border-radius: 999px;
}

.info h1 {
  font-size: clamp(1.6rem, 4vw, 2.2rem);
}

.description {
  opacity: 0.8;
  line-height: 1.5;
  max-width: 50ch;
}

.meta {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-top: 0.5rem;
}

.price {
  font-family: var(--font-mono);
  font-size: 1.1rem;
  font-weight: 600;
  background: var(--riso-yellow);
  border: 1.5px solid var(--ink);
  border-radius: 999px;
  padding: 0.3rem 0.9rem;
}

.stock {
  font-family: var(--font-mono);
  font-size: 0.8rem;
  opacity: 0.65;
}

.add-btn {
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

.add-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.9rem;
  margin-top: 0.5rem;
}

.text-btn {
  background: none;
  border: none;
  font-family: var(--font-mono);
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--riso-blue);
  cursor: pointer;
  padding: 0;
}

.text-btn:hover {
  text-decoration: underline;
}

.text-btn.danger {
  color: var(--riso-red);
}

.confirm-text {
  font-family: var(--font-mono);
  font-size: 0.8rem;
  opacity: 0.75;
}

.inline-error {
  color: var(--riso-red);
  font-family: var(--font-mono);
  font-size: 0.85rem;
}

.edit-form {
  border: 2px solid var(--ink);
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.9rem;
  max-width: 480px;
}

.edit-form h2 {
  font-size: 1.2rem;
  margin-bottom: 0.3rem;
}

.edit-form label {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  opacity: 0.75;
}

.edit-form input {
  font-family: var(--font-body);
  font-size: 0.95rem;
  text-transform: none;
  letter-spacing: normal;
  padding: 0.55rem 0.6rem;
  border: 2px solid var(--ink);
  background: var(--paper);
  color: var(--ink);
}

.edit-row {
  display: flex;
  gap: 0.9rem;
}

.edit-row label {
  flex: 1;
}

@media (max-width: 640px) {
  .detail {
    grid-template-columns: 1fr;
  }
  .visual {
    border-right: none;
    border-bottom: 2px dashed var(--ink);
    min-height: 160px;
  }
  .info {
    padding: 1.5rem;
  }
}
</style>
