const path = require('path')

module.exports = {
  publicPath: '/',
  assetsDir: 'static',
  outputDir: 'dist',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,

  devServer: {
    port: 10000,
    open: false,
    client: {
      overlay: {
        warnings: false,
        errors: true
      }
    },
    proxy: {
      '/api': {
        target: 'https://127.0.0.1:9000',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },

  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
        '~': path.resolve(__dirname, '.')
      }
    }
  }
}