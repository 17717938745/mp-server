import {createApp} from 'vue';
import store from '../store/Index';
import Index from '../Index.vue';
import LeadButton from '../page/html5/product/components/account/LeadButton.vue';
import Bridge from '../util/bridge';
import { createRouter, createWebHistory, Router, RouteRecordRaw } from 'vue-router';
import { moduleMapToRouterTreeList } from '@/util/RouterUtil'
import { deepMerge } from '@/util/ObjectUtil'
import { getStorage } from '@/util/StorageUtil'
import { Lazyload } from 'vant'
import { getEnv } from '@/util/EnvUtil'

const router: Router = createRouter({
  history: createWebHistory(''),
  routes: [],
});
router.onError((e, to, from) => {
  console.log(`router push error, ${e}, ${from.path} > ${to.path}`);
  return Promise.resolve(to);
});

const moduleMap: Record<string,
  () => Promise<{
    [key: string]: any;
    // @ts-ignore
  }>> = import.meta.glob(`../page/**`);
export const basePathList: string[] = ['..', 'page'];

const routerList: RouteRecordRaw[] = moduleMapToRouterTreeList(moduleMap, basePathList);

Promise.all([
  // 0
  import('../router/Index'),
  // 1
  import('../store/Index'),
  // 2, 3
  import('element-plus'),
  import('element-plus/lib/locale/lang/zh-cn'),  // 4
  // @ts-ignore
  // import('/third/element-plus@2.2.6/dist/index.css'),
  import('element-plus/dist/index.css'),
  // 5, 6
  // @ts-ignore
  import('vant'),
  import('vant/lib/index.css'),
  // 7
  // @ts-ignore
  import('bootstrap'),
]).then((list: any[]) => {
  const initialRouter = list[0].default;
  initialRouter(router);
  const app = createApp(Index)
    // router
    .use(router)
    // store
    .use(store)
    // element-plus
    .use(list[2].default, { locale: list[3].default })
    // element-plus css
    .use(list[4].default)
    // vant
    .use(list[5].default)
    .use(Lazyload);
  const styleList: any[] = [
    {
      label: '默认',
      value: {
        spacingHorizontal: {
          paddingLeft: '15px',
          paddingRight: '15px',
        },
        spacingVertical: {
          paddingTop: '15px',
          paddingBottom: '15px',
        },
        searchSection: {
          height: '30px',
          padding: '0px 15px',
        },
        tabSection: {
          height: '50px',
        },
        tabActive: {
          color: '#dc2828',
        },
        tabInactive: {
          color: '#19191E',
        },
        itemCenter: {
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        },
        subtitle: {
          fontSize: '15px',
          textAlign: 'center',
          color: '#ffffff',
        },
      },
    },

    {
      label: '自定义',
      value: {
        spacingHorizontal: {
          paddingLeft: '15px',
          paddingRight: '15px',
        },
        spacingVertical: {
          paddingTop: '15px',
          paddingBottom: '15px',
        },
        searchSection: {
          height: '30px',
          padding: '0px 15px',
        },
        tabSection: {
          height: '50px',
        },
        tabActive: {
          color: '#38c5c5',
        },
        tabInactive: {
          color: '#2c2ce5',
        },
        itemCenter: {
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        },
        subtitle: {
          fontSize: '15px',
          textAlign: 'center',
          color: '#844b1c',
        },
      },
    },
  ];
  const index: number = Number(getStorage('styleIndex') || '0');
  app.config.globalProperties.$styleList = styleList;
  app.config.globalProperties.$style = deepMerge(styleList[index].value);

  app.mount('#app');
});
