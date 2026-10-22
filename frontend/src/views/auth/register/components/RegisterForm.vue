<script lang="ts" setup>
import { reactive, ref } from 'vue'
import router from '@/router'

import { ElInput, ElButton } from 'element-plus'

import type { RegisterFormData } from '@/types/user'
import { useRegister } from '../composables/useRegister'

// 外部依赖
const { register } = useRegister()
// 内部状态
const isRegisterLoading = ref(false)
// 内部数据
const formLabelAlign = reactive<RegisterFormData>({
  username: '',
  password: '',
  rePassword: '',
})
// 事件处理
const clickLogin = () => {
  router.push({ name: 'login' })
}
const handleRegister = async () => {
  isRegisterLoading.value = true
  const res = await register(formLabelAlign)
  if (res) {
    await router.push({ name: 'login' })
  }
  isRegisterLoading.value = false
}
// 派生
const passwordInputRef = ref<InstanceType<typeof ElInput> | null>(null)
const rePasswordInputRef = ref<InstanceType<typeof ElInput> | null>(null)
function focusPassword() {
  passwordInputRef.value?.focus()
}
function focusRePassword() {
  rePasswordInputRef.value?.focus()
}
</script>
<template>
  <div class="register-form">
    <div class="register-form-header">御驾亲征</div>

    <el-form
      class="register-form-content"
      label-position="right"
      label-width="auto"
      :model="formLabelAlign"
    >
      <el-form-item label="账户名">
        <el-input
          v-model="formLabelAlign.username"
          autocomplete="off"
          placeholder="请输入用户名"
          @keyup.enter="focusPassword"
        />
      </el-form-item>
      <el-form-item label="密码">
        <el-input
          ref="passwordInputRef"
          v-model="formLabelAlign.password"
          autocomplete="off"
          placeholder="请再次输入用户密码"
          @keyup.enter="focusRePassword"
        />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input
          ref="rePasswordInputRef"
          v-model="formLabelAlign.rePassword"
          autocomplete="off"
          placeholder="请输入再次用户密码"
          @keyup.enter="handleRegister"
        />
      </el-form-item>
    </el-form>

    <div class="register-form-btn">
      <el-button
        class="register-btn"
        type="primary"
        :loading="isRegisterLoading"
        @click="handleRegister"
        >注册</el-button
      >
      <span class="login-btn" @click="clickLogin">登录</span>
    </div>
  </div>
</template>
<style scoped lang="scss">
@use '@/assets/styles/color.scss' as *;
.register-form {
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 26px 34px 28px;
  background-color: #fff;
  border: 1px solid $border-color;
  border-radius: 10px;
  box-shadow: 0 8px 24px 0 $shadow-color;
  .register-form-header {
    margin-bottom: 24px;
    font-size: 24px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  .register-form-btn {
    min-height: 40px;
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    align-items: center;
    .register-btn {
      grid-column: 2/3;
      justify-self: center;
      width: 120px;
    }
    .login-btn {
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
