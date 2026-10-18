import { ref } from 'vue'
import { defineStore } from 'pinia'
export const countTs = defineStore('count', () => {
  const count = ref<number>(0)
  const increment = () => {
    count.value++
  }
  const printCount = () => {
    console.log('当前count值为:', count.value)
  }
  return {
    count,
    increment,
    printCount,
  }
})
