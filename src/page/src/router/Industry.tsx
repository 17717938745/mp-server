import {Router, RouteRecordRaw} from 'vue-router'
import {MenuTree} from '@/typing/ma/System'
import {getFullSignUri, menuTreeListToRouterTreeList} from '@/util/RouterUtil'
import {getMenuTreeList} from '@/util/StorageUtil'

export const moduleMap: Record<string,
    () => Promise<{
      [key: string]: any
    }>> = import.meta.glob(`../page/industry/**`)
export const indexModuleMap: Record<string,
    () => Promise<{
      [key: string]: any
    }>> = import.meta.glob(`../page/Industry.vue`)
for (let indexModuleMapKey in indexModuleMap) {
  moduleMap[indexModuleMapKey] = indexModuleMap[indexModuleMapKey]
}
export const basePathList: string[] = ['..', 'page']

export const refreshRouter = (router: Router, menuTreeList: MenuTree[] = []): RouteRecordRaw[] => {
  menuTreeList = menuTreeList.length === 0 ? getMenuTreeList() : menuTreeList
  // console.log(`============================================================================================================`)
  // console.log(`menuTreeList: ${JSON.stringify(menuTreeList, null, 2)}`)
  // console.log(`============================================================================================================`)
  const routerList = menuTreeListToRouterTreeList(
      moduleMap,
      basePathList,
      menuTreeList,
      [],
      [],
      [],
      '/',
      true
  )
  // console.log(`============================================================================================================`)
  // console.log(`routerList: ${JSON.stringify(routerList, null, 2)}`)
  // console.log(`============================================================================================================`)
  if (routerList.length > 0) {
    routerList.forEach((route: RouteRecordRaw) => {
      if (route.name && router.hasRoute(route.name)) {
        router.removeRoute(route.name)
      }
      router.addRoute(route)
    })
  }
  return routerList
}
const initialRouter = (router: Router) => {
  console.log(`industry initialRouter...`)
  router.addRoute(
      {
        path: '/material',
        name: '/material',
        meta: {
          title: 'material',
        },
        component: () => import ('../page/industry/admin/temp/Material.vue'),
      },
  )
  router.addRoute(
      {
        path: '/operation',
        name: '/operation',
        meta: {
          title: 'material',
        },
        component: () => import ('../page/industry/admin/temp/Operation.vue'),
      },
  )

  router.addRoute(
      {
        path: '/local',
        name: '/local',
        meta: {
          title: 'Local',
        },
        component: () => import ('../page/Local.vue'),
      },
  )
  router.addRoute(
      {
        path: '/industry',
        name: '/industry/index',
        meta: {
          title: 'Sign in',
        },
        component: () => import ('../page/Industry.vue'),
        children: [
          {
            path: '',
            name: '/industry/',
            meta: {
              title: 'Index page',
            },
            component: () => import ('../page/industry/Sign.vue'),
          },
          {
            path: 'sign',
            name: '/industry/sign',
            meta: {
              title: 'Sign in',
            },
            component: () => import ('../page/industry/Sign.vue'),
          },
          {
            path: 'product',
            name: '/industry/product',
            meta: {
              title: 'Product',
            },
            component: () => import ('../page/industry/admin/product/Index.vue'),
          },
          {
            path: 'public/disqualification',
            name: '/industry/public/disqualification',
            meta: {
              title: 'Disqualification order',
            },
            component: () => import ('../page/industry/admin/public/Disqualification.vue'),
          },
          {
            path: 'public/accident',
            name: '/industry/public/accident',
            meta: {
              title: 'Accident report',
            },
            component: () => import ('../page/industry/admin/public/Accident.vue'),
          },
          {
            path: 'public/event',
            name: '/industry/public/event',
            meta: {
              title: 'Accident report',
            },
            component: () => import ('../page/industry/admin/public/Event.vue'),
          },
          {
            path: 'public/quality',
            name: '/industry/public/quality',
            meta: {
              title: 'Accident quality',
            },
            component: () => import ('../page/industry/admin/public/Quality.vue'),
          },
          {
            path: 'public/crash',
            name: '/industry/public/crash',
            meta: {
              title: 'Accident crash',
            },
            component: () => import ('../page/industry/admin/public/Crash.vue'),
          },
          {
            path: 'public/trouble',
            name: '/industry/public/trouble',
            meta: {
              title: 'Accident trouble',
            },
            component: () => import ('../page/industry/admin/public/Trouble.vue'),
          },
          {
            path: 'public/improve',
            name: '/industry/public/improve',
            meta: {
              title: 'Accident improve',
            },
            component: () => import ('../page/industry/admin/public/Improve.vue'),
          },
          {
            path: 'public/equipment',
            name: '/industry/public/equipment',
            meta: {
              title: 'Machining equipment',
            },
            component: () => import ('../page/industry/admin/public/Equipment.vue'),
          },
          {
            path: 'public/maintain',
            name: '/industry/public/maintain',
            meta: {
              title: 'Machining equipment repair',
            },
            component: () => import ('../page/industry/admin/public/Maintain.vue'),
          },
          {
            path: 'public/plan',
            name: '/industry/public/plan',
            meta: {
              title: 'Plan',
            },
            component: () => import ('../page/industry/admin/public/Plan.vue'),
          },
          {
            path: 'public/template',
            name: '/industry/public/template',
            meta: {
              title: '物品借出单',
            },
            component: () => import ('../page/industry/admin/public/Template.vue'),
          },
        ],
      }
  )
  router.addRoute(
      {
        path: '/:pathMatch(industry/.*)',
        name: '/industry/404',
        meta: {
          title: 'Sign in',
        },
        redirect: to => {
          return `${getFullSignUri()}`
        }
      }
  )
  refreshRouter(router)
  router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title}`
    next()
  })
}

export default initialRouter

