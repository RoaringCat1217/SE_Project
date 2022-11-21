<template>
  <el-container class="app-wrapper">
    <el-aside width="200px" class="sidebar-container">
      <Menu />
    </el-aside>
    <el-container class="container">
      <el-row gutter="50" class="search-row">
        <el-col class="mt-4" span="50">
          <el-input
            v-model="input1"
            placeholder="Please input"
            class="input-with-search"
          >
            <template #append>
              <el-button :icon="Search" round @click="handleSearch"
                >Search</el-button
              >
            </template>
          </el-input>
        </el-col>
      </el-row>
      <el-header height="20%" background="green">
        <div class="title">热度榜单</div>
      </el-header>
      <el-main>
        <el-table
          :data="tableData"
          border
          style="padding-left;: 10%"
          @row-click="handleSelection"
        >
          <el-table-column prop="group_info" label="Group" width="180" />
          <el-table-column prop="hot_index" label="Hot Index" width="180" />
          <el-table-column prop="rank" label="Rank" width="180" />
          <el-table-column prop="title" label="Title"> </el-table-column>
          <el-table-column fixed="right" label="Operations" width="120">
            <img
              alt="Vue logo"
              v-bind:src="images[index]"
              height="20"
              width="20"
              v-on:click="onClick()"
            />
            <el-button link type="primary" size="small">Edit</el-button>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { Search } from "@element-plus/icons-vue";
import Menu from "./Menu";
import axios from "axios";
import imgUrl1 from "../assets/like_hollow.png";
import imgUrl2 from "../assets/like_solid.png";
const images = [imgUrl1, imgUrl2];
const index = ref(0);
const input1 = ref("");
let tableData = ref([
  {
    group_info: "food",
    hot_index: 50,
    rank: 0,
    title: "blablabla",
  },
  {
    group_info: "food",
    hot_index: 50,
    rank: 1,
    title: "blablabla",
  },
]);
function onClick() {
  if (index.value === 0) index.value++;
  axios.get("/api/hot", {}).then((res) => {
    console.log(tableData);
    tableData.value = res.data.entry;
    console.log(res.data.entry);
    console.log(tableData);
  });
}
function handleSelection(row, column, event) {
  console.log(row);
}
function handleSearch() {
  console.log(input1.value);
}
onMounted(() => {
  axios.get("/api/hot", {}).then((res) => {
    tableData.value = res.data.entry;
  });
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
