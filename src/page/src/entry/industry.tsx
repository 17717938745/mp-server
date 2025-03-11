import {createApp} from 'vue'
import Index from '../Index.vue'
import {storeOptions} from '@/store/Industry'
import ElementPlus from 'element-plus'
import localeZH from 'element-plus/es/locale/lang/zh-cn'
import initialRouter from '../router/Industry'
import {createRouter, createWebHistory, Router} from 'vue-router'
import {createStore} from 'vuex'

const router: Router = createRouter({
  history: createWebHistory(''),
  routes: []
})
initialRouter(router)

createApp(Index)
.use(ElementPlus, {locale: localeZH})
.use(router)
.use(createStore(storeOptions))
.mount('#app')
