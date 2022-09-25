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
  }
}