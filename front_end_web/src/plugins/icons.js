import * as components from '@element-plus/icons-vue'

//这里是全局配置element plus图标组件，在main.js中导入了这一模块
export default {
    install: (app) => {
        for (const key in components){
            const componentConfig = components[key];
            app.component(componentConfig.name,componentConfig);
        }
    }
}