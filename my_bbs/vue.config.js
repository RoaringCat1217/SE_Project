const { defineConfig } = require('@vue/cli-service')
const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')

module.exports = defineConfig({
  transpileDependencies: true,
  //解决网络跨域请求
  devServer: {
    proxy: {
      '/api': {
        target: 'http://47.93.251.137:3000/',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  configureWebpack:{
    plugins:[
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      })
    ]
  },
  css: {
    loaderOptions: {
      sass: {
        additionalData: 
        `
          @import "@/styles/variables.scss";  // scss文件地址
          @import "@/styles/mixin.scss";     // scss文件地址
          @import "@/styles/sidebar.scss";
        `
      }
    }
  }
})
