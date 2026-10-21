<script setup lang="ts">
import AuthFooter from './components/AuthFooter.vue'
</script>
<template>
  <div class="auth">
    <router-view class="auth-body" />
    <AuthFooter class="auth-footer" />
  </div>
</template>
<style scoped lang="scss">
.auth {
  box-sizing: border-box;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 34px;
  .auth-body {
    margin: auto 0;
  }
  .auth-footer {
    width: 100%;
    padding: 44px 0 20px;
  }
}
</style>
