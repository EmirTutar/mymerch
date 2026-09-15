import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// defineStore('cart', () => {...}) ist die "Setup Store"-Syntax:
// sieht aus wie eine normale Komponente (ref = state, computed = getter,
// Funktionen = actions), ist aber global verfügbar statt an eine Komponente gebunden.
export const useCartStore = defineStore('cart', () => {
  // state: die Liste der Warenkorb-Positionen
  const items = ref([])

  // getters: automatisch neu berechnete, abgeleitete Werte
  const totalItems = computed(() =>
    items.value.reduce((sum, item) => sum + item.quantity, 0)
  )
  const totalPrice = computed(() =>
    items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  )

  // actions: Funktionen, die den state verändern
  function addItem(product) {
    const existing = items.value.find((item) => item.id === product.id)
    if (existing) {
      existing.quantity++
    } else {
      items.value.push({
        id: product.id,
        name: product.name,
        price: product.price,
        quantity: 1,
      })
    }
  }

  function removeItem(productId) {
    items.value = items.value.filter((item) => item.id !== productId)
  }

  function setQuantity(productId, quantity) {
    const item = items.value.find((item) => item.id === productId)
    if (item) {
      item.quantity = Math.max(1, quantity)
    }
  }

  function clear() {
    items.value = []
  }

  return { items, totalItems, totalPrice, addItem, removeItem, setQuantity, clear }
})
