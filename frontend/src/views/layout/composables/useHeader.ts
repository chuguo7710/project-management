import { useAuthStore } from '@/stores/auth'
const { logout } = useAuthStore()

export const useHeader = () => {
  const handleCommand = (command: string) => {
    switch (command) {
      case '1':
        break
      case '2':
        logout()
        break
    }
  }
  return {
    handleCommand,
  }
}
