// 前端项目管理工具
import {defineConfig, loadEnv} from 'vite'
// 官方插件，支持Vue
import VuePlugin from '@vitejs/plugin-vue'
// html文本替换插件
import InjectExternals from 'vite-plugin-inject-externals'
// @ts-ignore
import path from 'path'
// 浏览器兼容插件
import legacy from '@vitejs/plugin-legacy'
// @ts-ignore
import {execSync} from 'child_process'
// @ts-ignore
import {visualizer} from 'rollup-plugin-visualizer'

export const GLOBAL_LEAD_GIT_INFO = {
  // SHA
  GIT_SHA: execSync('git rev-parse --short HEAD').toString().trim(),
  //HASH
  GIT_HASH: execSync('git rev-parse HEAD').toString().trim(),
  // 提交时间
  GIT_COMMIT_TIME: execSync('git log -1 --format=%cI --date=local').toString().trim(),
  // HASH
  CURRENT_BRANCH: execSync('git log -1 --format=%cI --date=local').toString().trim(),
  // 最后提交 message
  GIT_LAST_COMMIT_MESSAGE: execSync('git show -s --format=%s').toString().trim()
}

const commonConfig = {
      define: {
        GLOBAL_LEAD_GIT_INFO: JSON.stringify(GLOBAL_LEAD_GIT_INFO),
      },
      plugins: [
        VuePlugin(),
      ],
      base: './',
      resolve: {
        alias: {
          // @ts-ignore
          "@": path.resolve(__dirname, "src"),
          // @ts-ignore
          "@util": path.resolve(__dirname, "src/util"),
          // @ts-ignore
          "@typing": path.resolve(__dirname, "src/typing")
        }
      }
    },
    vueModule = {
      name: 'vue',
      global: 'Vue',
      path: '/third/vue@3.2.37/dist/vue.global.prod.js',
    },
    vueRouterModule = {
      name: 'vue-router',
      global: 'VueRouter',
      path: '/third/vue-router@4.0.16/dist/vue-router.global.prod.js',
    },
    vuexModule = {
      name: 'vuex',
      global: 'Vuex',
      path: '/third/vuex@4.0.2/dist/vuex.global.prod.js',
    },
    axiosModule = {
      name: 'axios',
      global: 'axios',
      path: '/third/axios@0.27.2/dist/axios.min.js',
    },
    elementPlusModule = {
      name: 'element-plus',
      global: 'ElementPlus',
      // path: '/third/element-plus@2.2.16/dist/index.full.min.js',
      path: '/third/element-plus@2.2.6/dist/index.full.min.js',
    },
    elementPlusCssModule = {
      htmlTag: {
        tag: 'link',
        attrs: {
          rel: 'stylesheet',
          href: '/third/element-plus@2.2.6/dist/index.css',
        }
      },
    }

// @ts-ignore
export default ({mode}) => {
  // @ts-ignore
  const env: string = loadEnv(mode, process.cwd()).VITE_ENV
  // @ts-ignore
  const module: string = loadEnv(mode, process.cwd()).VITE_MODULE
  console.log(`env: ${env}, module: ${module}`)
  switch (env) {
    case 'local':
      const devConfig = Object.assign(
          commonConfig,
          {
            server: {
              host: '0.0.0.0',
              port: 9000,
              open: true,
              proxy: {
                '^/api/.*': {
                  target: 'http://localhost',
                  changeOrigin: true,
                  rewrite: (path: string) => path
                },
              }
            }
          }
      )
      return defineConfig(devConfig)
    default:
      const prdConfig = Object.assign(
          commonConfig,
          {
            plugins: [
              VuePlugin(),
              InjectExternals({
                injectTo: '<!-- Custom placeholder for vite plugin inject externals -->',
                modules: [vueModule, vueRouterModule, vuexModule, axiosModule]
              }),
              legacy({
                targets: ['> 1%, last 1 version, ie >= 11'],
                additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
              }),
            ],
          }
      )
      switch (module) {
        case 'industry':
          // @ts-ignore
          return defineConfig(Object.assign(
              prdConfig,
              {
                base: '/industry/',
                build: {
                  rollupOptions: {
                    input: {
                      main: 'industry.html',
                    },
                  },
                  outDir: 'industry',
                },
              }, {
                plugins: [
                  VuePlugin(),
                  InjectExternals({
                    injectTo: '<!-- Custom placeholder for vite plugin inject externals -->',
                    // @ts-ignore
                    modules: [vueModule, vueRouterModule, vuexModule, axiosModule, elementPlusModule, elementPlusCssModule]
                  }),
                ],
              }
          ))
        default:
          return defineConfig(prdConfig);
      }
  }
}
