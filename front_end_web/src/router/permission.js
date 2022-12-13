import router from "./index";
import store from "@/store";

const whiteList = ["/login", "/home", "/","/group","/group/"];

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  if (store.getters.getToken === "true") {
    if (to.path === "/login") {
      next("/home");
    } else {
      next();
    }
  } else {
    if (whiteList.includes(to.path)) {
      next();
    } else {
      ElMessage({
        message: "请先登录！",
        type: "warning",
        duration: 1000,
      });
      next("/login");
    }
  }
});
