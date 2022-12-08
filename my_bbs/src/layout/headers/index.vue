<template>
  <div class="navbar">
    <Hamburger />
    <Breadcrumb />
    <div class="navbar-right">
      <!--这里要实现搜索功能-->
      <el-input v-model="input3" placeholder="搜索相关帖子" class="input-with-select">
        <template #append>
          <el-button :icon="Search" />
        </template>
      </el-input>
    </div>
  </div>
</template>

<script setup>
import Hamburger from './components/hamburger.vue'
import Breadcrumb from './components/breadcrumb.vue'
import { Search } from '@element-plus/icons-vue'

</script>

<style lang="scss" scoped>
.navbar {
  width: 100%;
  height: 60px;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 16px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  position: relative;

  .navbar-right {
    padding-left: 50%;
    padding-right: 15px;
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: flex-end;

    :deep(.navbar-item) {
      display: inline-block;
      //margin-left: 18px;
      font-size: 22px;
      //color: #5a5e66;
      box-sizing: border-box;
      cursor: pointer;
    }
  }
}

.el-input {
  --el-input-focus-border-color: #6c8e67;
}
</style>
