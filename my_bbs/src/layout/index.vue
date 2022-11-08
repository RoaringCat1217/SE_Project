<template>
    <el-container class="app-wrapper">
        <el-aside width="200px" class="sidebar-container">
            <Menu />
        </el-aside>
        <el-container class="container">
            <el-header height="20%" background="green">
                <div class="title">
                    热度榜单
                </div>
            </el-header>
            <el-main>
                <el-table :data="tableData" border style="padding-left;: 10%">
                    <el-table-column prop="group_info" label="Group" width="180" />
                    <el-table-column prop="hot_index" label="Hot Index" width="180" />
                    <el-table-column prop="rank" label="Rank" width="180" />
                    <el-table-column prop="title" label="Title">
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </el-container>
</template>
  
<script setup>
import Menu from './Menu'

const tableData = [
    {
        "group_info": "food",
        "hot_index": 50,
        "rank": 0,
        "title": "blablabla"
    },
    {
        "group_info": "food",
        "hot_index": 50,
        "rank": 1,
        "title": "blablabla"
    }
]

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
  