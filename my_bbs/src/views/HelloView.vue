<template>
  <div class="home">
    <nav>
      <!--路由跳转链接-->
      <router-link to="/">Hello</router-link> |
      <router-link to="/home">Homepage</router-link> |
      <router-link to="/login">Login</router-link> |
      <router-link to="/group">Group</router-link> |
      <router-link to="/userinfo">Userinfo</router-link> |
      <router-link to="/newpost">NewPost</router-link>
    </nav>
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Your Vue.js App" />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";

export default {
  name: "HomeView",
  components: {
    HelloWorld,
  },
};
</script>
