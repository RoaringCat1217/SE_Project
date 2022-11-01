const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')

module.exports = {
  configureWebpack: config => {
    config.plugins.push(AutoImport({
      resolvers:[ElementPlusResolver()],
    }))
    config.plugins.push(Components({
      resolvers:[ElementPlusResolver()],
    }))
  },
  devServer: {
    https: false,
    hot: "only",
    proxy: {
      '/api': {
        target: 'http://47.93.251.137:3000/',
        changeOrigin: false,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}