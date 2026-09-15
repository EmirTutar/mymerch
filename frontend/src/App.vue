<script setup>
import { watch, ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { useCartStore } from './stores/cart.js'

// Da der Store global ist, sieht JEDE Komponente sofort die aktuelle
// Warenkorb-Anzahl mit - ganz ohne Props durch die ganze App zu reichen.
const cart = useCartStore()

// Kurzer "Bump" auf dem Badge, wenn sich die Anzahl ändert: wir setzen
// kurz eine CSS-Klasse und entfernen sie wieder - simpler als eine
// komplette Animationslibrary für so einen Mini-Effekt.
const bump = ref(false)
watch(
  () => cart.totalItems,
  (next, prev) => {
    if (next === prev) return
    bump.value = false
    // requestAnimationFrame statt setTimeout(0): garantiert, dass der Browser
    // den "false"-Zustand erst gerendert hat, bevor wir wieder auf "true" springen -
    // sonst würde die CSS-Animation manchmal nicht neu starten.
    requestAnimationFrame(() => {
      bump.value = true
    })
  }
)
</script>

<template>
  <div class="shell">
    <header class="topbar">
      <RouterLink to="/products" class="logo">
        mymerch<span class="logo-dot">.</span>
      </RouterLink>
      <nav>
        <RouterLink to="/products">Produkte</RouterLink>
        <RouterLink to="/orders">Bestellungen</RouterLink>
        <RouterLink to="/cart" class="cart-link">
          Warenkorb
          <span
            v-if="cart.totalItems > 0"
            class="cart-badge"
            :class="{ 'cart-badge--bump': bump }"
            @animationend="bump = false"
          >
            {{ cart.totalItems }}
          </span>
        </RouterLink>
      </nav>
    </header>

    <main>
      <!-- mode="out-in": wartet, bis die alte Seite ausgeblendet ist, bevor
           die neue einblendet - verhindert, dass sich beide kurz überlappen -->
      <RouterView v-slot="{ Component }">
        <Transition name="page" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </main>
  </div>
</template>

<style scoped>
.shell {
  min-height: 100vh;
}

.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--ink);
  color: var(--paper);
  padding: 1.1rem 2rem;
}

.logo {
  font-family: var(--font-display);
  font-size: 1.4rem;
  text-decoration: none;
  color: var(--paper);
  text-transform: uppercase;
  letter-spacing: -0.02em;
}

.logo-dot {
  color: var(--riso-red);
}

nav {
  display: flex;
  gap: 2rem;
}

nav :deep(a) {
  font-family: var(--font-mono);
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  text-decoration: none;
  color: var(--paper);
  opacity: 0.65;
  padding-bottom: 0.25rem;
  border-bottom: 2px solid transparent;
  transition: opacity 0.15s ease, border-color 0.15s ease;
}

nav :deep(a:hover) {
  opacity: 1;
}

nav :deep(a.router-link-active) {
  opacity: 1;
  border-bottom-color: var(--riso-red);
}

.cart-link {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
}

.cart-badge {
  background: var(--riso-red);
  color: var(--paper);
  border-radius: 999px;
  font-size: 0.7rem;
  line-height: 1;
  padding: 0.25rem 0.45rem;
  display: inline-block;
}

.cart-badge--bump {
  animation: bump 0.3s ease;
}

@keyframes bump {
  0% {
    transform: scale(1);
  }
  40% {
    transform: scale(1.35);
  }
  100% {
    transform: scale(1);
  }
}

main {
  max-width: 1100px;
  margin: 0 auto;
  padding: 2.5rem 2rem 4rem;
}

/* Seitenübergang zwischen Routen */
.page-enter-active,
.page-leave-active {
  transition: opacity 0.18s ease, transform 0.18s ease;
}
.page-enter-from {
  opacity: 0;
  transform: translateY(6px);
}
.page-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

@media (prefers-reduced-motion: reduce) {
  .cart-badge--bump {
    animation: none;
  }
  .page-enter-active,
  .page-leave-active {
    transition: none;
  }
}

@media (max-width: 600px) {
  .topbar {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
  main {
    padding: 1.5rem 1.25rem 3rem;
  }
}
</style>
