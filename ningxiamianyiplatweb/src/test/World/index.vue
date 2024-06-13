<template>
    <div>
      <div class="message">
        {{ message }}
      </div>
      Enter your username: <input :value="name" />
      <div v-if="error" class="error">
        Please enter a username with at least seven letters.
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { computed } from "vue";
  
  const props = withDefaults(
    defineProps<{
      message: string;
      username: string;
    }>(),
    {
      message: "",
      username: "",
    }
  );
  
  const name = computed(() => props.username);
  
  const error = computed(() => {
    return props.username.trim().length < 7;
  });
  </script>