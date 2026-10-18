import { useUserStore } from '@/stores/user'
const { logout } = useUserStore()

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
