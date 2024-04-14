import { defineStore } from 'pinia'
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
  const loginStatus = ref(false);
  const loginUserId = ref('');
  const loginUserName = ref('');

  return {
    loginStatus,
    loginUserId,
    loginUserName
  };
}, {
  persist: {
    enabled: true
  }
});