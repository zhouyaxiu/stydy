import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from "unplugin-auto-import/vite";
import OptimizationPersist from 'vite-plugin-optimize-persist'
import PkgConfig from 'vite-plugin-package-config'
import Components from 'unplugin-vue-components/vite'
import ElementPlus from 'unplugin-element-plus/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
const path = require('path')
import { visualizer } from 'rollup-plugin-visualizer'
function pathResolve(dir) {
  return resolve(__dirname, ".", dir);
}
import { resolve } from 'path' // 主要用于alias文件路径别名
// https://vitejs.dev/config/
export default defineConfig(({ command, mode, ssrBuild }) => {
  const config = loadEnv(mode, './')
  console.log(ElementPlusResolver(), 'ElementPlusResolverElementPlusResolver')
  return {
    plugins: [
      vue(),
      AutoImport({
        imports: ["vue", "vue-router", "pinia"],//自动导入
        // resolvers: [ElementPlusResolver()],
        // dts: "src/auto-imports.d.ts"
      }),
      PkgConfig(),   //预构建配置,加快热启动
      OptimizationPersist(),  //预构建配置,加快热启动
      visualizer({
        open: true,
        filename: "stats.html",
        brotliSize: true
      }),
      // ElementPlus({
      //   resolvers: [ElementPlusResolver()],
      // }),
      Components({
        //   prefix: "ss",
        // dirs: ['src/components'],
        //   extensions: ['vue'],
        //   deep: true,
        dts: false,//或者false
        // dts: "src/components.d.ts",//或者false
        //   directoryAsNamespace: true,
        resolvers: [ElementPlusResolver()],
      })
    ],
    // 是否自动在浏览器打开
    open: true,
    hmr: true,
    // 是否开启 https
    https: false,
    // 服务端渲染
    ssr: false,
    /**
     * 在生产中服务时的基本公共路径。
     * @default '/'
     */
    base: './',
    /**
     * 与“根”相关的目录，构建输出将放在其中。如果目录存在，它将在构建之前被删除。
     * @default 'dist'
     */
    outDir: 'dist',
    resolve: {
      alias: {
        '@': pathResolve("src"),
        // 键必须以斜线开始和结束
        // "/@/": path.resolve(__dirname, "./src"),
        extensions: ['.js', '.ts', '.json'] // 导入时想要省略的扩展名列表
      },
    },
    // 引入第三方的配置
    optimizeDeps: {
      // include: ['schart.js']
    },
    // 打包配置
    build: {
      target: 'modules',
      outDir: `dist`, //输出路径
      assetsDir: 'assets', // 指定生成静态资源的存放路径
      minify: 'terser', // 混淆器，terser构建后文件体积更小，配合terserOptions
      terserOptions: {
        compress: {
          drop_console: true, // 生产环境移除console
          drop_debugger: true // 生产环境移除debugger
        }
      },
      chunkSizeWarningLimit: 1500,
      rollupOptions: {
        output: {
          manualChunks: {
            elementPlus: ["element-plus"]
          }
        }
      }
    },
    define: {
      'process.env': config
    },
    // 用来放公共的sass处理
    css: {
      /* CSS 预处理器 */
      preprocessorOptions: {
        scss: {
          additionalData: '@import "src/assets/css/common.scss";'
        }
      }
    },

    // 本地运行配置，及反向代理配置
    server: {
      port: 8087,
      // host: "192.168.2.63",
      // 是否开启 https
      https: false,
      cors: true, // 默认启用并允许任何源
      open: false, // 在服务器启动时自动在浏览器中打开应用程序

      //反向代理配置，注意rewrite写法
      proxy: {
        '/api': {
          target: config.VITE_BASE_URL, //代理接口
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
  }
})
