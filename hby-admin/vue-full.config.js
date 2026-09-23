/**
 * @description vue.config.js全局配置
 */
const path = require('path')
const {
  baseURL,
  publicPath,
  assetsDir,
  outputDir,
  lintOnSave,
  transpileDependencies,
  title,
  abbreviation,
  devPort,
  providePlugin,
  build7z,
  buildGzip,
  imageCompression,
} = require('./src/config')
// const rely = require('vue-plugin-rely') // 临时注释掉缺失的模块
const { webpackBarName, webpackBanner } = require('./vab.config')
const { version, author } = require('./package.json')
const Webpack = require('webpack')
const WebpackBar = require('webpackbar')
const FileManagerPlugin = require('filemanager-webpack-plugin')
const dayjs = require('dayjs')
const dateTime = dayjs().format('YYYY-M-D HH:mm:ss')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const productionGzipExtensions = ['html', 'js', 'css', 'svg']
process.env.VUE_APP_TITLE = title
process.env.VUE_APP_AUTHOR = author
process.env.VUE_APP_UPDATE_TIME = dateTime
process.env.VUE_APP_VERSION = version
// process.env.VUE_APP_RELY = rely // 临时注释掉，因为rely模块不存在
const resolve = (dir) => {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath,
  assetsDir,
  outputDir,
  lintOnSave,
  transpileDependencies,
  devServer: {
    hot: true,
    port: devPort,
    open: false,
    noInfo: false,
    overlay: {
      warnings: true,
      errors: true,
    },
    // 注释掉的地方是前端配置代理访问后端的示例
    // baseURL必须为/xxx，而不是后端服务器，请先了解代理逻辑，再设置前端代理
    // ！！！一定要注意！！！
    // 1.这里配置了跨域及代理只针对开发环境生效
    // 2.不建议你在前端配置跨域，建议你后端配置Allow-Origin,Method,Headers，放行token字段，一步到位
    // 3.后端配置了跨域，就不需要前端再配置，会发生Origin冲突
    proxy: {
      [baseURL]: {
        target: `https://127.0.0.1:9000`, // 安静
        // target: "http://7k4krj.natappfree.example",
        // target: "http://192.0.2.16:8067/api",
        // target: `http://192.0.2.200:8763`, // 安静
        // target: `http://192.0.2.200:8763`,
        // target: `http://192.0.2.200:9000`,
        // target: `http://192.0.2.200:9000`,
        // target: `http://192.0.2.17:9000`, //本地
        // target: `http://cn-gz-txy.starryfrp.example:14183/`, //穿透代理
        // target: `http://192.0.2.19/api`, //zhezi代理
        // target: `http://192.0.2.16:9000`,
        // target: ` http://192.0.2.19:88/api`,
        // target: `http://192.0.2.18:88/api`,
        ws: true,
        logLevel: 'debug',
        changeOrigin: true,
        pathRewrite: {
          ['^' + baseURL]: '',
        },
      },
      // 添加 /contract 前缀的代理配置
      '/contract': {
        target: `http://127.0.0.1:8064`, // 直接指向合同服务
        ws: true,
        logLevel: 'debug',
        changeOrigin: true,
        pathRewrite: {
          '^/contract': '', // 移除 /contract 前缀
        },
      },
    },
    // after: require('./mock'),
  },
  pwa: {
    workboxOptions: {
      skipWaiting: true,
      clientsClaim: true,
    },
    themeColor: '#ffffff',
    msTileColor: '#ffffff',
    appleMobileWebAppCapable: 'yes',
    appleMobileWebAppStatusBarStyle: 'black',
    manifestOptions: {
      name: 'Vue Admin Beautiful - Admin Pro',
      short_name: 'Admin Pro',
      background_color: '#ffffff',
    },
  },
  configureWebpack() {
    return {
      resolve: {
        alias: {
          '~': resolve('.'),
          '@': resolve('src'),
          '@form-generator': resolve('src/components/form-generator'),
        },
      },
      plugins: [
        new Webpack.ProvidePlugin(providePlugin),
        new WebpackBar({
          name: webpackBarName,
        }),
      ],
    }
  },
  chainWebpack(config) {
    config.resolve.symlinks(true)
    config.module.rule('svg').exclude.add(resolve('src/icon'))
    config.module
      .rule('svg')
      .exclude.add(resolve('src/components/form-generator/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/components/form-generator/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]',
      })
      .end()
    config.module
      .rule('vabIcon')
      .test(/\.svg$/)
      .include.add(resolve('src/icon'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({ symbolId: 'vab-icon-[name]' })
    config.when(process.env.NODE_ENV === 'development', (config) => {
      config.devtool(false) // 禁用source map以减少内存使用
    })
    config.when(process.env.NODE_ENV === 'production', (config) => {
      config.performance.set('hints', false)
      config.devtool('none')
      config.optimization.splitChunks({
        automaticNameDelimiter: '-',
        chunks: 'all',
        cacheGroups: {
          chunk: {
            name: 'vab-chunk',
            test: /[\\/]node_modules[\\/]/,
            minSize: 131072,
            maxSize: 524288,
            chunks: 'async',
            minChunks: 2,
            priority: 10,
          },
          vue: {
            name: 'vue',
            test: /[\\/]node_modules[\\/](vue(.*)|core-js)[\\/]/,
            chunks: 'initial',
            priority: 20,
          },
          elementUI: {
            name: 'element-ui',
            test: /[\\/]node_modules[\\/]element-ui(.*)[\\/]/,
            priority: 30,
          },
          extra: {
            name: 'vab-extra',
            test: resolve('src/extra'),
            priority: 40,
          },
          // 根据使用模块抽取公共代码
          // echarts: {
          //   name: 'echarts',
          //   test: /[\\/]node_modules[\\/](echarts|zrender|tslib)[\\/]/,
          //   priority: 50,
          // },
        },
      })
      config
        .plugin('banner')
        .use(Webpack.BannerPlugin, [`${webpackBanner}${dateTime}`])
      if (imageCompression)
        config.module
          .rule('images')
          .use('image-webpack-loader')
          .loader('image-webpack-loader')
          .options({
            bypassOnDebug: true,
          })
          .end()
      if (buildGzip)
        config.plugin('compression').use(CompressionWebpackPlugin, [
          {
            filename: '[path][base].gz[query]',
            algorithm: 'gzip',
            test: new RegExp(
              '\\.(' + productionGzipExtensions.join('|') + ')$'
            ),
            threshold: 8192,
            minRatio: 0.8,
          },
        ])
      if (build7z)
        config.plugin('fileManager').use(FileManagerPlugin, [
          {
            events: {
              onEnd: {
                archive: [
                  {
                    source: `./${outputDir}`,
                    destination: `./${outputDir}/${abbreviation}_${dayjs().unix()}.zip`,
                  },
                ],
              },
            },
          },
        ])
    })
  },
  runtimeCompiler: true,
  productionSourceMap: false,
  css: {
    sourceMap: false, // 禁用CSS source map以减少内存使用
    loaderOptions: {
      scss: {
        additionalData(content, loaderContext) {
          const { resourcePath, rootContext } = loaderContext
          const relativePath = path.relative(rootContext, resourcePath)
          if (
            relativePath.replace(/\\/g, '/') !==
            'src/vab/styles/variables/variables.scss'
          )
            return '@import "~@/vab/styles/variables/variables.scss";' + content
          return content
        },
      },
    },
  },
}
