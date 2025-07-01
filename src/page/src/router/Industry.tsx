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

export const refreshRouter = (router: Router, menuTreeList: MenuTree[] = [], initialRouterList: any[] = []): RouteRecordRaw[] => {
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
  routerList.push(...initialRouterList)
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
  refreshRouter(
      router,
      getMenuTreeList(),
      [
        {
          path: '/local',
          name: '/local',
          meta: {
            title: 'Local',
          },
          component: () => import ('../page/Local.vue'),
        },
        {
          path: '/industry',
          name: '/industry/no-auth',
          meta: {
            title: 'Sign in',
          },
          component: () => import ('../page/Industry.vue'),
          children: [
            {
              path: '',
              name: '/industry/sign/alias',
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
              name: '/industry/admin/product/index',
              meta: {
                title: 'Product',
              },
              component: () => import ('../page/industry/admin/product/Index.vue'),
            },
            {
              path: 'public',
              name: '/industry/admin/public',
              meta: {
                title: 'Public',
              },
              children: [
                {
                  path: 'disqualification',
                  name: '/industry/admin/public/disqualification',
                  meta: {
                    title: 'Disqualification order',
                  },
                  component: () => import ('../page/industry/admin/public/Disqualification.vue'),
                },
                {
                  path: 'accident',
                  name: '/industry/admin/public/accident',
                  meta: {
                    title: 'Accident report',
                  },
                  component: () => import ('../page/industry/admin/public/Accident.vue'),
                },
                {
                  path: 'event',
                  name: '/industry/admin/public/event',
                  meta: {
                    title: 'Accident report',
                  },
                  component: () => import ('../page/industry/admin/public/Event.vue'),
                },
                {
                  path: 'quality',
                  name: '/industry/admin/public/quality',
                  meta: {
                    title: 'Accident quality',
                  },
                  component: () => import ('../page/industry/admin/public/Quality.vue'),
                },
                {
                  path: 'crash',
                  name: '/industry/admin/public/crash',
                  meta: {
                    title: 'Accident crash',
                  },
                  component: () => import ('../page/industry/admin/public/Crash.vue'),
                },
                {
                  path: 'trouble',
                  name: '/industry/admin/public/trouble',
                  meta: {
                    title: 'Accident trouble',
                  },
                  component: () => import ('../page/industry/admin/public/Trouble.vue'),
                },
                {
                  path: 'improve',
                  name: '/industry/admin/public/improve',
                  meta: {
                    title: 'Accident improve',
                  },
                  component: () => import ('../page/industry/admin/public/Improve.vue'),
                },
                {
                  path: 'equipment',
                  name: '/industry/admin/public/equipment',
                  meta: {
                    title: 'Machining equipment',
                  },
                  component: () => import ('../page/industry/admin/public/Equipment.vue'),
                },
                {
                  path: 'maintain',
                  name: '/industry/admin/public/maintain',
                  meta: {
                    title: 'Machining equipment repair',
                  },
                  component: () => import ('../page/industry/admin/public/Maintain.vue'),
                },
                {
                  path: 'plan',
                  name: '/industry/admin/public/plan',
                  meta: {
                    title: 'Plan',
                  },
                  component: () => import ('../page/industry/admin/public/Plan.vue'),
                },
                {
                  path: 'assembly',
                  name: '/industry/admin/public/assembly',
                  meta: {
                    title: 'Assembly',
                  },
                  component: () => import ('../page/industry/admin/public/Assembly.vue'),
                },
                {
                  path: 'template',
                  name: '/industry/admin/public/template',
                  meta: {
                    title: '物品借出单',
                  },
                  component: () => import ('../page/industry/admin/public/Template.vue'),
                },
                {
                  path: 'material/index',
                  name: '/industry/admin/public/material/index',
                  meta: {
                    title: '领料表单',
                  },
                  component: () => import ('../page/industry/admin/public/material/Index.vue'),
                },
                {
                  path: 'material/check',
                  name: '/industry/admin/public/material/check',
                  meta: {
                    title: '报检表单',
                  },
                  component: () => import ('../page/industry/admin/public/material/Check.vue'),
                },
              ],
            },
          ],
        },
        {
          path: '/:pathMatch(industry/.*)',
          name: '/industry/404',
          meta: {
            title: 'Sign in',
          },
          redirect: to => {
            console.log(`redirect...`)
            return `${getFullSignUri()}`
          }
        },
      ]
  )
  router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title || to.meta.name || to.name || to.path}`
    next()
  })
}

export default initialRouter

