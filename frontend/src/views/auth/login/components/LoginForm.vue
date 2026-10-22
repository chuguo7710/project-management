<script setup lang="ts">
import { reactive, ref } from 'vue'
import router from '@/router'

import { ElInput, ElButton } from 'element-plus'

import type { LoginFormData } from '@/types/user'
import { useLogin } from '../composables/useLogin'

// 外部依赖
const { login } = useLogin()
// 内部状态
const isLoginLoading = ref(false)
// 内部数据
const loginFormData = reactive<LoginFormData>({
  username: '',
  password: '',
})
// 事件处理
const clickRegister = () => {
  router.push({ name: 'register' })
}
const handleLogin = async () => {
  isLoginLoading.value = true
  const res = await login(loginFormData)
  if (res) {
    await router.push({ name: 'dashboard' })
  }
  isLoginLoading.value = false
}
// 派生
const passwordInputRef = ref<InstanceType<typeof ElInput> | null>(null)
function focusPassword() {
  passwordInputRef.value?.focus()
}
</script>
<template>
  <div class="login-form">
    <div class="login-form-header">御驾亲征</div>

    <el-form
      class="login-form-content"
      label-position="right"
      label-width="auto"
      :model="loginFormData"
    >
      <el-form-item label="账户名">
        <el-input
          v-model="loginFormData.username"
          autocomplete="off"
          placeholder="请输入用户名"
          @keyup.enter="focusPassword"
        />
      </el-form-item>
      <el-form-item label="密码">
        <el-input
          ref="passwordInputRef"
          v-model="loginFormData.password"
          autocomplete="off"
          placeholder="请输入用户密码"
          :show-password="true"
          @keyup.enter="handleLogin"
        />
      </el-form-item>
    </el-form>

    <div class="login-form-btn">
      <el-button class="login-btn" type="primary" :loading="isLoginLoading" @click="handleLogin"
        >登录</el-button
      >
      <span class="register-btn" @click="clickRegister">注册</span>
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;
.login-form {
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 26px 34px 28px;
  background-color: #fff;
  border: 1px solid $border-color;
  border-radius: 10px;
  box-shadow: 0 8px 24px 0 $shadow-color;
  .login-form-header {
    margin-bottom: 24px;
    font-size: 24px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  .login-form-btn {
    min-height: 40px;
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    align-items: center;
    .login-btn {
      grid-column: 2/3;
      justify-self: center;
      width: 120px;
    }
    .register-btn {
      grid-column: 3/4;
      justify-self: end;
      font-size: 13px;
      color: $text-secondary-color;
      cursor: pointer;
      user-select: none;
      transition: color 0.2s ease;
      &:hover {
        color: $primary-color;
        text-decoration: underline;
      }
    }
  }
}
</style>
