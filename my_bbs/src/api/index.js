import axios from '../utils/request'
import path from './path'

const api = {
    //这里可以定义一些网络请求的函数！！！在其他组件中可以import api来直接调用这里的方法！实现axios的封装
    handleLogin(data) {
        return axios.get(path.login,{params:data});
    },
    handleRegister(data){
        return axios.get(path.register,{params:data});
    },
    getUserAvatar(data) {
        return axios.get(path.getAvatar,{params:data});
    },
    getHotList() {
        return axios.get(path.hotlist);
    },
    getGroups() {
        return axios.get(path.groups);
    },
    getPostList(data) {
        return axios.get(path.groupInfo,{params:data});
    },
    getPostDetails(data) {
        return axios.get(path.getPost,{params:data});
    },
    getReply(data) {
        return axios.get(path.getReply,{params:data});
    },
    getStarList(data) {
        return axios.get(path.getStars,{params:data});
    },
    newPost(data) {
        return axios.get(path.newPost,{params:data});
    },
    getSearchResult(data) {
        return axios.post(path.search,data);
    }
}

export default api;