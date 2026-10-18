<script setup lang="ts">
import { ref, computed } from 'vue'
import { countTs } from './www'
const Ts = countTs()
const add = Ts.increment
</script>
<template>
  <div id="app">
    <h>仪盘表</h>
    <button @click="add()">点击</button>
    <button @click="Ts.printCount()">显示</button>
  </div>
</template>
