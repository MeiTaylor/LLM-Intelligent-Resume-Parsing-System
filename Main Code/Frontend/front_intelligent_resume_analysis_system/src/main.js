import { createApp } from 'vue'
import pinia from './stores'

import App from './Welcome.vue'
import router from './router'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


import '../node_modules/pubsub-js/src/pubsub'
import '../mock/index'
// import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import '@/permission' // permission control

import * as ElementPlusIconsVue from '@element-plus/icons-vue'


import svgIcon from './components/SvgIcon/index.vue'
import 'virtual:svg-icons-register'

const app = createApp(App)

// app.use(createPinia())
app.use(pinia)
app.use(ElementPlus)
app.use(router)
app.component('svg-icon', svgIcon)

app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }

// while(true){
//     console.log("sending request")
//     axios.post('http://192.168.159.1:5168/api/Login/Login',{username: 'admin', password: '123456'},{
//     withCredentials: true
// })
// }

