import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
const path = require('path')
function pathResolve(dir) {
  return resolve(__dirname, ".", dir);
}
import { resolve } from 'path' // 主要用于alias文件路径别名

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  const config = loadEnv(mode, './')
  return {
    plugins: [vue()],
    // 是否自动在浏览器打开
    open: true,
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
      },
    },
    // 引入第三方的配置
    optimizeDeps: {
      // include: ['schart.js']
    },
    // 打包配置
    build: {
      target: 'modules',
      outDir: `dist`,//'dist', //输出路径 `dist-${config.VITE_ENV}`
      assetsDir: 'assets', // 指定生成静态资源的存放路径
      minify: 'terser', // 混淆器，terser构建后文件体积更小
    },
    define: {
      'process.env': config,
      __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: 'true'
    },
    // 本地运行配置，及反向代理配置
    server: {
      port: 8086,
      // port: 8090,
      host: "0.0.0.0",
      // 是否开启 https
      https: false,
      cors: true, // 默认启用并允许任何源
      open: true, // 在服务器启动时自动在浏览器中打开应用程序

      //反向代理配置，注意rewrite写法
      /*proxy: {
        '/api': {
          target: config.VITE_BASE_URL, //代理接口
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },*/
    }
  }
})
