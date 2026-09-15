<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '../stores/cart.js'
import { apiUrl } from '../lib/api.js'

const cart = useCartStore()
const router = useRouter()

const customerName = ref('')
const customerEmail = ref('')
const submitting = ref(false)
const error = ref(null)

async function checkout() {
  submitting.value = true
  error.value = null
  try {
    const response = await fetch(apiUrl('/api/orders'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerName: customerName.value,
        customerEmail: customerEmail.value,
        orderItems: cart.items.map((item) => ({
          productId: item.id,
          quantity: item.quantity,
        })),
      }),
    })
    if (!response.ok) {
      throw new Error(`Server antwortete mit Status ${response.status}`)
    }
    cart.clear()
    router.push('/orders')
  } catch (err) {
    error.value = err.message
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div>
    <h1>Warenkorb</h1>

    <p v-if="cart.items.length === 0" class="status-text">
      Dein Warenkorb ist leer.
      <RouterLink to="/products">Zu den Produkten &rarr;</RouterLink>
    </p>

    <div v-else class="layout">
      <table class="cart-table">
        <thead>
          <tr>
            <th>Produkt</th>
            <th>Menge</th>
            <th>Summe</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cart.items" :key="item.id">
            <td>{{ item.name }}</td>
            <td>
              <input
                type="number"
                min="1"
                class="qty-input"
                :value="item.quantity"
                @change="cart.setQuantity(item.id, Number($event.target.value))"
              />
            </td>
            <td class="mono">{{ (item.price * item.quantity).toFixed(2) }}&nbsp;€</td>
            <td>
              <button class="remove-btn" @click="cart.removeItem(item.id)">Entfernen</button>
            </td>
          </tr>
        </tbody>
      </table>

      <aside class="checkout">
        <div class="total-row">
          <span>Gesamt</span>
          <span class="mono total-price">{{ cart.totalPrice.toFixed(2) }}&nbsp;€</span>
        </div>

        <form @submit.prevent="checkout">
          <label>
            <span>Name</span>
            <input v-model="customerName" required />
          </label>
          <label>
            <span>E-Mail</span>
            <input v-model="customerEmail" type="email" required />
          </label>

          <button type="submit" class="btn-primary riso-shift" :disabled="submitting">
            {{ submitting ? 'Bestelle…' : 'Jetzt bestellen' }}
          </button>
          <p v-if="error" class="error">{{ error }}</p>
        </form>
      </aside>
    </div>
  </div>
</template>

<style scoped>
h1 {
  margin-bottom: 1.5rem;
}

.status-text {
  font-family: var(--font-mono);
  opacity: 0.75;
}

.status-text a {
  color: var(--riso-blue);
}

.layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 2rem;
  align-items: start;
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  border: 2px solid var(--ink);
}

.cart-table th,
.cart-table td {
  text-align: left;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--line);
}

.cart-table th {
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  background: var(--paper-dim);
}

.qty-input {
  width: 4rem;
  padding: 0.3rem 0.4rem;
  border: 2px solid var(--ink);
  background: var(--paper);
  font-family: var(--font-mono);
}

.remove-btn {
  background: none;
  border: none;
  color: var(--riso-red);
  font-family: var(--font-mono);
  font-size: 0.75rem;
  text-transform: uppercase;
  cursor: pointer;
  padding: 0;
}

.remove-btn:hover {
  text-decoration: underline;
}

.mono {
  font-family: var(--font-mono);
}

.checkout {
  border: 2px solid var(--ink);
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: var(--font-mono);
  padding-bottom: 1rem;
  border-bottom: 2px dashed var(--ink);
}

.total-price {
  font-size: 1.1rem;
  font-weight: 600;
}

form {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
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

input[type='text'],
input[type='email'] {
  text-transform: none;
  letter-spacing: normal;
  font-family: var(--font-body);
}

form input {
  padding: 0.55rem 0.6rem;
  border: 2px solid var(--ink);
  background: var(--paper);
  color: var(--ink);
  font-size: 0.95rem;
}

.btn-primary {
  background: var(--ink);
  color: var(--paper);
  border: 2px solid var(--ink);
  font-family: var(--font-mono);
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  padding: 0.7rem 1.3rem;
  cursor: pointer;
  margin-top: 0.5rem;
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

@media (max-width: 700px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
