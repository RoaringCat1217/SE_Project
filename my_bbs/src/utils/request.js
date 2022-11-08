import { promiseTimeout } from "@vueuse/shared";
import axios from "axios";
import queryString from "querystring";

//这里是对网络请求（aixos相关）解决方案的封装，I在./api/index.js中可以定义网络请求相关函数
//其他涉及网络请求的组件都会使用在这里配置的解决方案
const instance = axios.create({
    baseURL: "/api",
    //网络请求的公共配置
    timeout: 5000,
});

const errorHandle = (status, info) => {
    switch (status) {
        case 400:
            console.log("语义有误");
            break;
        case 401:
            console.log("服务器认证失败");
            break;
        case 403:
            console.log("服务器拒绝访问");
            break;
        case 404:
            console.log("地址错误");
            break;
        case 500:
            console.log("服务器遇到意外");
            break;
        case 502:
            console.log("服务器无响应");
            break;
        default:
            console.log(info);
            break;
    }
};

//拦截器
//网页向后台发送请求请求时的拦截器
instance.interceptors.request.use(
    (config) => {
        if (config.method === "post") {
            //qs.stringify 将一个参数对象格式化为字符串
            config.data = queryString.stringify(config.data);
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

//网页获取后台数据时的拦截器
instance.interceptors.response.use(
    (response) => {
        //return response.status === 200 ? Promise.resolve(response) : Promise.reject(response);
        //这里去掉冗余的data包装
        const data = response.data;
        //成功登入
        if (data.code === 1) {
            ElMessage({
                message: '登录成功！',
                type: 'success',
                duration: 1000
            });
            return Promise.resolve(data);
        } else {
            //这里需要后端添加一些更加具体的错误信息
            ElMessage({
                message: '登陆失败！ 错误码：' + data.code,
                type: 'error',
                duration: 1000
            });
            return Promise.reject(new Error("登陆失败！ 错误码："+ data.code));
        }
    },
    (error) => {
        const response = error.response;
        response && ElMessage.error(response.data);
        //我们主要关注错误接受时的信息
        errorHandle(response.status, response.info);
    }
);

export default instance;
