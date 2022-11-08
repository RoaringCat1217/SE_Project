import axios from '../utils/request'
import path from './path'

const api = {
    //这里可以定义一些网络请求的函数！！！在其他组件中可以import api来直接调用这里的方法！实现axios的封装
    handleLogin(data) {
        return axios.get(path.login,{params:data});
    }
}

export default api;