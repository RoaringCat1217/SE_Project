<template>
  <div class="hamburger-container" @click="toggleClick" id="hamburger">
    <el-icon :size="25" v-if="sidebarType">
      <Fold />
    </el-icon>
    <el-icon :size="25" v-else>
      <Expand />
    </el-icon>
  </div>
</template>

<script setup>
import { useStore } from 'vuex'
import { computed } from 'vue'

const store = useStore();
const sidebarType = computed(() => {
    return store.getters.getSidebarType ? true : false;
})

const toggleClick = () => {
  store.commit('changeSidebarType');
}

</script>

<style lang="scss" scoped>
.hamburger-container {
  margin-left: 10px;
  margin-right: 16px;
  box-sizing: border-box;
  cursor: pointer;
}
</style>
