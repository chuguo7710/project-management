<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElInput, ElButton } from 'element-plus'
import { useLoginForm } from '../composables/useLoginForm'
import type { LoginFormData } from '../composables/useLoginForm'
const { login } = useLoginForm()
const formLabelAlign = reactive<LoginFormData>({
  username: '',
  password: '',
})
const passwordInputRef = ref<InstanceType<typeof ElInput> | null>(null)
function focusPassword() {
  passwordInputRef.value?.focus()
}
</script>
<template>
  <div class="login-form-data">
    <el-form label-position="right" label-width="auto" :model="formLabelAlign">
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
          placeholder="请输入用户密码"
          :show-password="true"
          @keyup.enter="login(formLabelAlign)"
        />
      </el-form-item>
    </el-form>
  </div>
  <div class="login-form-btn">
    <el-button class="login-btn" type="primary" @click="login(formLabelAlign)">登录</el-button>
  </div>
</template>
<style scoped>
.login-form-btn {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
}
.login-btn {
  grid-column: 2/3;
  justify-self: center;
}
</style>
