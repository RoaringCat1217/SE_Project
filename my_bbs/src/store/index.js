//这里是vuex store的配置文件
import { createStore } from "vuex";

//Vuex的核心作用就是帮我们管理组件之间状态的
export default createStore({
  // 所有数据放在这里
  state: {
    token: localStorage.getItem("token") || "false",
    username: localStorage.getItem("username") || "",
    userAvatar: localStorage.getItem("userAvatar") || "",
    pth: sessionStorage.getItem("path") || '/home', //当前页面的路由
    sidebarType: true, // true为显示侧边栏，false为隐藏侧边栏
    repliedId: localStorage.getItem("repliedId") || -1,
  },
  getters: {
    getToken(state) {
      return state.token;
    },
    getUsername(state) {
      return state.username;
    },
    getUserAvatar(state) {
      return state.userAvatar;
    },
    getPath(state) {
      return state.pth;
    },
    getSidebarType(state) {
      return state.sidebarType;
    },
    getRepliedId(state) {
      return state.repliedId;
    }
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    setUsername(state, username) {
      state.username = username;
      localStorage.setItem("username", username);
    },
    setAvatar(state, url) {
      state.userAvatar = url;
      localStorage.setItem("userAvatar", url);
    },
    setPath(state, path) {
      state.pth = path;
      sessionStorage.setItem("path", path);
    },
    setRepliedId(state,post_id) {
      state.repliedId = post_id;
      localStorage.setItem("repliedId", post_id);
    },

    changeSidebarType(state) {
      state.sidebarType = !state.sidebarType;
    },
  },
  actions: {},
  modules: {},
});
