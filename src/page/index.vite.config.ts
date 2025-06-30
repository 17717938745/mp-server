// 前端项目管理工具
import {defineConfig, loadEnv} from 'vite'
// 官方插件，支持Vue
import VuePlugin from '@vitejs/plugin-vue'
// 浏览器兼容插件
import legacy from '@vitejs/plugin-legacy'
// html文本替换插件
import InjectExternals from 'vite-plugin-inject-externals'
// @ts-ignore
import path from 'path'
// @ts-ignore
import {execSync} from 'child_process'

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
const commonVuePlugin = VuePlugin(
    {
      include: [/\.vue$/],
      // 自动根据文件路径设置组件名
      transformAssetUrls: {
        base: null,
        includeAbsolute: false,
      },
    }
)
const commonConfig = {
      define: {
        GLOBAL_LEAD_GIT_INFO: JSON.stringify(GLOBAL_LEAD_GIT_INFO),
      },
      plugins: [
        commonVuePlugin,
      ],
      base: './',
      resolve: {
        alias: {
          "@/util": path.resolve(__dirname, "./src/util"),
          "@/store": path.resolve(__dirname, "./src/store"),
          "@/router": path.resolve(__dirname, "./src/router"),
          "@/typing": path.resolve(__dirname, "./src/typing"),
          "@/component": path.resolve(__dirname, "./src/component")
        }
      }
    },
    vueModule = {
      name: 'vue',
      global: 'Vue',
      path: '/third/vue@3.5.13/dist/vue.global.prod.js',
    },
    vueRouterModule = {
      name: 'vue-router',
      global: 'VueRouter',
      path: '/third/vue-router@4.5.0/dist/vue-router.global.prod.js',
    },
    vuexModule = {
      name: 'vuex',
      global: 'Vuex',
      path: '/third/vuex@4.1.0/dist/vuex.global.prod.js',
    },
    axiosModule = {
      name: 'axios',
      global: 'axios',
      path: '/third/axios@1.8.2/dist/axios.min.js',
    },
    elementPlusModule = {
      name: 'element-plus',
      global: 'ElementPlus',
      path: '/third/element-plus@2.9.6/dist/index.full.min.js',
    },
    elementPlusCssModule = {
      htmlTag: {
        tag: 'link',
        attrs: {
          rel: 'stylesheet',
          href: '/third/element-plus@2.9.6/dist/index.css',
        }
      },
    },
    tinymceVueModule = {
      name: 'tinymce-vue',
      global: 'tinymce',
      path: '/third/@tinymce/tinymce-vue@6.1.0/lib/browser/tinymce-vue.min.js',
    },
    reportPlusCssModule = {
      htmlTag: {
        tag: 'link',
        attrs: {
          rel: 'stylesheet',
          href: '/third/css/report.css',
        }
      },
    }
// const urlPrefix = 'https://douson.natapp4.cc'
const urlPrefix = 'http://localhost:8888'
// @ts-ignore
export default ({mode}) => {
  // @ts-ignore
  const env: string = loadEnv(mode, process.cwd()).VITE_ENV
  // @ts-ignore
  const module: string = loadEnv(mode, process.cwd()).VITE_MODULE
  console.log(`Project stat, env: ${env}, module: ${module}`)
  switch (env) {
    case 'local':
      const devConfig = Object.assign(
          commonConfig,
          {
            server: {
              host: '0.0.0.0',
              port: 9090,
              open: false,
              secure: false,
              proxy: {
                '^/index/img/.*': {
                  target: urlPrefix,
                  changeOrigin: true,
                  secure: false,
                  rewrite: (path: string) => {
                    const targetPath = path
                    console.log(`Convert /index/img/ path: ${path}, targetPath: ${targetPath}`)
                    return targetPath
                  }
                },
                '^/third/.*': {
                  target: urlPrefix,
                  changeOrigin: true,
                  secure: false,
                  rewrite: (path: string) => {
                    const targetPath = path
                    console.log(`Convert /third/, path: ${path}, targetPath: ${targetPath}`)
                    return targetPath
                  }
                },
              }
            },
          }
      )
      return defineConfig(devConfig)
    default:
      const prdConfig = Object.assign(
          commonConfig,
          {
            plugins: [
              commonVuePlugin,
              InjectExternals({
                injectTo: '<!-- Custom placeholder for vite plugin inject externals -->',
                modules: [vueModule, vueRouterModule, vuexModule, axiosModule, reportPlusCssModule]
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
                  outDir: '../../static/industry',
                },
              }, {
                plugins: [
                  commonVuePlugin,
                  InjectExternals({
                    injectTo: '<!-- Custom placeholder for vite plugin inject externals -->',
                    // @ts-ignore
                    modules: [vueModule, vueRouterModule, vuexModule, axiosModule, elementPlusModule, elementPlusCssModule, tinymceVueModule, reportPlusCssModule]
                  }),
                ],
              }
          ))
        default:
          return defineConfig(prdConfig);
      }
  }
}
