import {createApp} from 'vue'
import store from '../store/Index'
import Index from '../Index.vue'
import {createRouter, createWebHistory, Router} from 'vue-router'

const router: Router = createRouter({
  history: createWebHistory(''),
  routes: [],
});
router.onError((e, to, from) => {
  console.log(`router push error, ${e}, ${from.path} > ${to.path}`);
  return Promise.resolve(to);
})
Promise.all([
  // 0
  import('../router/Index'),
  // 1
  import('../store/Index'),
  // 2, 3
  import('element-plus'),
  import('element-plus/es/locale/lang/zh-cn'),
  // @ts-ignore 4
  import('element-plus/dist/index.css'),
]).then((list: any[]) => {
  const initialRouter = list[0].default;
  initialRouter(router);
  const app = createApp(Index)
      // router
      .use(router)
      // store
      .use(store)
      // element-plus
      .use(list[2].default, {locale: list[3].default})
      // element-plus css
      .use(list[4].default)
      .mount('#app')
  ;
});
