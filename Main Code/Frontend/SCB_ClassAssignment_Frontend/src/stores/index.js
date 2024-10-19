import { createPinia } from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()

pinia.use(piniaPluginPersistedstate)

export default pinia

// 在这里暴露的原因是因为在组件外使用pinia时可能遇到加载顺序问题