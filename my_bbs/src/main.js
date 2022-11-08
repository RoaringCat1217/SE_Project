import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import axios from 'axios'
import elementIcon from './plugins/icons'

//import ElementPlus from 'element-plus'
//import 'element-plus/dist/index.css'

const app = createApp(App);
//将axios挂载到全局，之后可以用this.$axios来在组件中使用aixos不用再import
app.config.globalProperties.$axios = axios;

app.use(store).use(router).use(elementIcon).mount('#app')
