<template>
  app
  <A ref="refA" />
  <B ref="refB" />
</template>

<script setup>
import A from "./A.vue"
import B from "./B.vue"
const refA = ref(null)
const refB = ref(null)
console.log(refA, 'refA')
console.log(refB, 'refB')
</script>