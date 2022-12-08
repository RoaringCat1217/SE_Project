<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
      <span class="no-redirect" v-if="index === breadcrumbList.length - 1">
        {{menu[item.name]}}
      </span>
      <span class="redirect" v-else @click="handleRedirect(item.path)">
        {{menu[item.name]}}
      </span>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { watch, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import ZH from '@/assets/zh.js'

const route = useRoute();
const router = useRouter();
const store = useStore();
const menu = ZH.menus;  //汉化

const breadcrumbList = ref([])

const initBreadcrumbList = () => {
  breadcrumbList.value = route.matched;
  console.log(breadcrumbList.value);
}

const handleRedirect = (path) => {
  router.push(path);
  if(path === '/'){
    path = '/home';
  }
  store.commit('setPath',path);
}

watch(
  route,() => {
    initBreadcrumbList()
  },
  { deep: true, immediate: true }
)
</script>

<style lang="scss" scoped>
.no-redirect {
  color: #97a8be;
  cursor: text;
}
.redirect {
  color: #666;
  font-weight: 600;
  cursor: pointer;
  &:hover {
    color: $menuBg;
  }
}
</style>
