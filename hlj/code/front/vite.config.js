import { defineConfig, loadEnv,splitVendorChunkPlugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from "unplugin-auto-import/vite";
import OptimizationPersist from 'vite-plugin-optimize-persist'
import PkgConfig from 'vite-plugin-package-config'
function pathResolve(dir) {
  return resolve(__dirname, ".", dir);
}
import { resolve } from 'path' // 主要用于alias文件路径别名
// https://vitejs.dev/config/
export default defineConfig(({ command, mode, ssrBuild }) => {
  const viteEnv = loadEnv(mode, process.cwd())
  const { VITE_PUBLIC_PATH } = viteEnv
  return {
    plugins: [
      vue(),
      AutoImport({
        imports: ["vue", "vue-router"]//自动导入
      }),
      PkgConfig.default(),   //预构建配置,加快热启动
      OptimizationPersist.default(),  //预构建配置,加快热启动
      splitVendorChunkPlugin(),  //产物分块策略
    ],
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
    base: VITE_PUBLIC_PATH,
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
    // publicDir:false,
    // 打包配置
    build: {
      outDir: `dist`, //输出路径
      cors: true,
      assetsDir: 'static', // 指定生成静态资源的存放路径
      minify: 'terser', // 混淆器，terser构建后文件体积更小，配合terserOptions
      terserOptions: {
        compress: {
          drop_console: true, // 生产环境移除console
          drop_debugger: true // 生产环境移除debugger
        }
      },
      chunkSizeWarningLimit: 1500,
      rollupOptions: { //底层分包配置
        output: {
          assetFileNames: assetInfo => {
            var info = assetInfo.name.split('.')
            var extType = info[info.length - 1]
            if (
              /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/i.test(assetInfo.name)
            ) {
              extType = 'media'
            } else if (/\.(png|jpe?g|gif|svg)(\?.*)?$/.test(assetInfo.name)) {
              extType = 'img'
            } else if (/\.(woff2?|eot|ttf|otf)(\?.*)?$/i.test(assetInfo.name)) {
              extType = 'fonts'
            }
            return `static/${extType}/[name]-[hash][extname]`
          },
          chunkFileNames: 'static/js/[name]-[hash].js',
          entryFileNames: 'static/js/[name]-[hash].js',
          manualChunks: (id) => {
            if (id.includes('node_modules')) {
              return id.toString().split('node_modules/')[1].split('/')[0].toString()
            }
          },
        }
      }
    },
    define: {
      'process.env': viteEnv
    },
    
    // 本地运行配置，及反向代理配置
    server: {
      port: 9004,
      // host: "192.168.2.80",
      // 是否开启 https
      https: false,
      cors: true, // 默认启用并允许任何源
      open: false, // 在服务器启动时自动在浏览器中打开应用程序
    },
  }
})
