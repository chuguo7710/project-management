<script setup lang="ts">
import LoginFormData from './components/LoginForm.vue'
import LoginFormFooter from './components/LoginFooter.vue'
</script>
<template>
  <div class="login">
    <div class="loginForm">
      <LoginFormData />
      <LoginFormFooter />
    </div>
  </div>
</template>
<style scoped>
.login {
  width: 98vw;
  height: 98vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.loginForm {
  width: 350px;
  height: 200px;
  border: 1px solid #dcdfe6;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
