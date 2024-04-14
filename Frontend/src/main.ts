import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
// import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import { createPinia } from 'pinia'
import piniaPluginPersist from 'pinia-plugin-persist'

const pinia = createPinia()
pinia.use(piniaPluginPersist)
// createApp(App).use(Antd).use(store).use(router).use(pinia).mount('#app')
createApp(App).use(Antd).use(router).use(pinia).mount('#app')

