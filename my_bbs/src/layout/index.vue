<template>
    <el-container class="app-wrapper">
        <el-aside :width="asideWidth" class="sidebar-container">
            <Menu />
        </el-aside>
        <el-container class="container" :class="{ hidderContainer: !$store.getters.getSidebarType }">
            <el-header>
                <Headers />
            </el-header>
            <el-main>
                <router-view />
            </el-main>

        </el-container>
    </el-container>
</template>
  
<script setup>
import router from '@/router';
import Menu from './Menu'
import Headers from './headers'
import { ref, computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore();

const asideWidth = computed(() => {
    return store.getters.getSidebarType ? '210px' : '67px';
});

</script>
  
<style lang="scss" scoped>
.app-container {
    position: relative;
    width: 100%;
    height: 100%;
}

.container {
    width: calc(100% - $sideBarWidth + 10px);
    height: 100%;

    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    transition: all 0.28s;

    &.hidderContainer {
        width: calc(100% - $hideSideBarWidth);
    }
}

::v-deep(.el-header) {
    padding: 0;
    margin: 0;
}

.title {
    font-family: fantasy;
    font-weight: bloder;
    color: #6c8e67;
    font-size: 3em;
    text-align: center; //水平居中
    position: relative;
    top: auto;
    transform: translateY(5vh);
}
</style>
  