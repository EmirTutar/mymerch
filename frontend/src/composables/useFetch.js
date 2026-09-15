import { ref, watchEffect, toValue } from 'vue'
import { apiUrl } from '../lib/api.js'

// Ein Composable ist einfach eine Funktion, die mit "use" beginnt und
// reaktive State-Logik bündelt, die mehrere Komponenten gemeinsam brauchen.
// Hier: das "loading/error/data"-Muster, das wir bisher in jeder View
// von Hand wiederholt haben.
//
// `path` darf ein fester String ODER eine reaktive Quelle sein (ref oder
// Funktion, z.B. () => `/api/products/${route.params.id}`) - toValue()
// entpackt beides einheitlich.
export function useFetch(path) {
  const data = ref(null)
  const loading = ref(true)
  const error = ref(null)

  // watchEffect läuft sofort UND jedes Mal erneut, wenn sich eine reaktive
  // Quelle ändert, die synchron (vor dem ersten "await") gelesen wird -
  // genau wie wir es schon für die Produkt-Detailseite genutzt haben.
  watchEffect(async () => {
    const url = apiUrl(toValue(path))
    loading.value = true
    error.value = null
    try {
      const response = await fetch(url)
      if (!response.ok) {
        throw new Error(`Server antwortete mit Status ${response.status}`)
      }
      data.value = await response.json()
    } catch (err) {
      error.value = err.message
    } finally {
      loading.value = false
    }
  })

  return { data, loading, error }
}
