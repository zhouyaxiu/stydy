// 引入必要的模块
const path = require('path') // 引入node的path模块
const webpack = require('webpack') // 引入的webpack,使用lodash
const HtmlWebpackPlugin = require('html-webpack-plugin') // 将html打包
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const CleanWebpackPlugin = require('clean-webpack-plugin')
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')
// const CompressionPlugin = require('compression-webpack-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// console.log(path.resolve(__dirname,'dist')); //物理地址拼接
module.exports = {
  // entry: './src/main.js', //入口文件  在vue-cli main.js
  entry: ['babel-polyfill', './src/main.js'],
  output: { // webpack如何输出
    path: resolve('dist'), // 定位，输出文件的目标路径
    filename: 'js/[name].[chunkhash].js'
  },
  // mode:"production",
  devServer: {// 热重载
    contentBase: [resolve('src'), resolve('static')],
    // 热重载需要监听的文件目录
    compress: true,
    // 启用压缩
    // host: '192.168.1.254',
    // host:'192.168.2.80',
    port: 8086,
    proxy: {
      '/apis': {
        target: 'http://192.168.2.97:8890/DiseShaoXing', // target host
        // target: 'http://192.168.3.97:8080/epiTaiAnProject', // target host
        ws: true, // proxy websockets
        changeOrigin: true, // needed for virtual hosted sites
        pathRewrite: {
          '^/apis': '' // rewrite path
        }
      }
    }
  },
  module: { // 模块的相关配置
    rules: [ // 根据文件的后缀提供一个loader,解析规则
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
          transformToRequire: {
            video: ['src', 'poster'],
            source: 'src',
            img: 'src',
            image: 'xlink:href'
          }
        }
      },
      {
        test: /\.js$/, // es6 => es5
        include: [
          resolve('src'),
          resolve('node_modules/query-string'),
          resolve('node_modules/strict-uri-encode'),
          resolve('node_modules/crypto-js')
        ],
        // exclude: [resolve('node_modules')], //不匹配选项（优先级高于test和include）
        use: 'babel-loader'
      },
      /* {       //图片loader
                test: /\.(png|jpe?g|gif|svg|ttf|woff)(\?.*)?$/,
                use: [
                    {
                        loader: 'url-loader' //根据文件地址加载文件
                    }
                ]
            }, */
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 8192, // 限制打包图片的大小：
          // 如果大于或等于8192Byte，则按照相应的文件名和路径打包图片；如果小于8192Byte，则将图片转成base64格式的字符串。
          name: 'images/[name]-[hash:8].[ext]' // images:图片打包的文件夹；
          // [name].[ext]：设定图片按照本来的文件名和扩展名打包，不用进行额外编码
          // [hash:8]：一个项目中如果两个文件夹中的图片重名，打包图片就会被覆盖，加上hash值的前八位作为图片名，可以避免重名。
        }
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader'
      },
      {
        test: /\.(eot|svg|ttf|woff|woff2?)$/,
        loader: 'url-loader'
      },
      { // 页面中会用到img标签，img引用的图片地址也需要一个loader来处理,这样再打包后的html文件下img就可以正常引用图片路径了
        test: /\.(htm|html)$/,
        use: {
          loader: 'html-withimg-loader',
          options: {
            attrs: ['img:src', 'img:data-src', 'audio:src'],
            minimize: true
          }
        }
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {
              publicPath: '../',
              url: false,
              importLoaders: 1
            }
          },
          'css-loader'
        ]
      },
      {
        test: /\.less$/,
        loader: 'style-loader!css-loader!less-loader'
      }
    ]
  },
  resolve: { // 解析模块的可选项
    // modules: [ ]//模块的查找目录 配置其他的css等文件
    extensions: ['.js', '.vue', '.json', '.jsx', '.less', '.css'], // 用到文件的扩展名
    alias: { // 模快别名列表
      utils: resolve('src/utils'),
      'vue$': 'vue/dist/vue.esm.js',
      '@': resolve('src')
    }
  },
  plugins: [ // 插进的引用, 压缩，分离美化
    // 打包前先清空
    new CleanWebpackPlugin(),
    new MiniCssExtractPlugin({
      // Options similar to the same options in webpackOptions.output
      // both options are optional
      filename: 'css/[name].[contenthash].css',
      chunkFilename: 'css/[id].[contenthash].css'
    }), // [name] 默认  也可以自定义name  声明使用
    new HtmlWebpackPlugin({ // 将模板的头部和尾部添加css和js模板,dist 目录发布到服务器上，项目包。可以直接上线
      file: 'index.html', // 打造单页面运用 最后运行的不是这个
      template: 'index.html' // vue-cli放在跟目录下
    }),
    new CopyWebpackPlugin([ // src下其他的文件直接复制到dist目录下
      { from: resolve('static/'), to: './' }
    ]),
    new webpack.ProvidePlugin({
      // 引用框架 jquery  lodash工具库是很多组件会复用的，省去了import
      '_': 'lodash' // 引用webpack
    }),
    new VueLoaderPlugin()
  ],
  optimization: {
    splitChunks: {
      chunks: 'async',
      minSize: 30000,
      minChunks: 1,
      maxAsyncRequests: 5,
      maxInitialRequests: 3,
      name: true,
      cacheGroups: {
        elementUI: {
          name: 'chunk-elementUI', // 单独将 elementUI 拆包
          priority: 20, // 权重要大于 libs 和 app 不然会被打包进 libs 或者 app
          test: /[\\/]node_modules[\\/]element-ui[\\/]/,
          chunks: 'initial' // 只打包初始时依赖的第三方
        },
        elTreeTransfer: {
          name: 'chunk-el-tree-transfer', // 单独将 elementUI 拆包
          priority: 18, // 权重要大于 libs 和 app 不然会被打包进 libs 或者 app
          test: /[\\/]node_modules[\\/]el-tree-transfer[\\/]/
        },
        makepy: {
          name: 'chunk-makepy', // 单独将 elementUI 拆包
          priority: 17, // 权重要大于 libs 和 app 不然会被打包进 libs 或者 app
          test: /[\\/]node_modules[\\/]makepy[\\/]/
        },
        commons: {
          name: 'chunk-commons',
          test: resolve('src/components'), // 可自定义拓展你的规则
          minChunks: 2, // 最小共用次数
          priority: 5,
          reuseExistingChunk: true
        },
        // 处理入口chunk
        vendors: {
          test: /[\\/]node_modules[\\/]/,
          chunks: 'initial',
          name: 'vendors'
        },
        // 处理异步chunk
        asyncVendors: {
          test: /[\\/]node_modules[\\/]/,
          minChunks: 2,
          chunks: 'async',
          name: 'async-vendors'
        },
        default: {
          minChunks: 2,
          priority: -20,
          reuseExistingChunk: true
        }
      }
    },
    // 为每个入口提取出webpack runtime模块
    runtimeChunk: { name: 'manifest' },
    minimizer: [
      new UglifyJsPlugin({
        uglifyOptions: {
          compress: true
        }
      })
    ]
  }
}
