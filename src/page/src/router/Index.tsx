import {Router} from 'vue-router'
import industryInitialRouter from './Industry'

const initialRouter = (router: Router) => {
  router.addRoute(
      {
        path: '/',
        name: 'index-local',
        meta: {
          title: 'Local',
        },
        component: () => import ('../page/Local.vue'),
      },
  )
  industryInitialRouter(router)
}

export default initialRouter

