import {defineConfig, loadEnv} from 'vite'
import Uni from '@dcloudio/vite-plugin-uni'
// @ts-ignore
import path from 'path'

// @ts-ignore
export default ({mode}) => {
  // @ts-ignore
  const env: string = loadEnv(mode, process.cwd()).VITE_ENV
  // @ts-ignore
  const module: string = loadEnv(mode, process.cwd()).VITE_MODULE
  // console.log(`env: ${env}, module: ${module}`)

  return defineConfig({
    base: '',
    resolve: {
      alias: {
        // @ts-ignore
        "@": path.resolve(__dirname, "src"),
        // @ts-ignore
        "@util": path.resolve(__dirname, "../page/src/util")
      }
    },
    plugins: [Uni({})],
    build: {
      rollupOptions: {
        input: {
          main: 'mp.html',
        }
      },
    },
  })
}
