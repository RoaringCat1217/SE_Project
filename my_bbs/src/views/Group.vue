<template>
  <el-container class="app-wrapper">
    <el-aside width="200px" class="sidebar-container">
      <Menu />
    </el-aside>
    <el-container class="container">
      <el-header height="20%">
        <div class="title">版面目录</div>
      </el-header>
      <el-scrollbar>
        <el-row gutter="40" class="groupcontent">
          <el-col span="12">
            <el-card :body-style="{ padding: '20px' }" class="content-card">
              <img
                src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                class="image"
              />
              <div style="padding: 14px">
                <span>{{ group_name[0] }}</span>
                <div class="bottom">
                  <el-button
                    text
                    class="button"
                    @click="enterGroup(group_name[0])"
                    >进入版面</el-button
                  >
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col span="12">
            <el-card :body-style="{ padding: '20px' }" class="content-card">
              <img
                src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                class="image"
              />
              <div style="padding: 14px">
                <span>{{ group_name[1] }}</span>
                <div class="bottom">
                  <el-button
                    text
                    class="button"
                    @click="enterGroup(group_name[1])"
                    >进入版面</el-button
                  >
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row gutter="40" class="groupcontent">
          <el-col span="12">
            <el-card :body-style="{ padding: '20px' }" class="content-card">
              <img
                src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                class="image"
              />
              <div style="padding: 14px">
                <span>{{ group_name[2] }}</span>
                <div class="bottom">
                  <el-button
                    text
                    class="button"
                    @click="enterGroup(group_name[2])"
                    >进入版面</el-button
                  >
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-scrollbar>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
let group_name = ref([
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
]);

function enterGroup(group_name_string) {
  console.log(group_name_string);
}
onMounted(() => {
  axios.get("/api/catalogue", {}).then((res) => {
    for (let i = 0; i < res.data.groups.length; i++) {
      group_name.value[i] = res.data.groups[i];
      console.log(group_name.value);
    }
    console.log(group_name.value);
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
.groupcontent {
  left: 160px;
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
.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  background-color: #6c8e67;
  padding: 5;
  min-height: auto;
  position: relative;
  font-family: "黑体";
  font-weight: bold;
  color: white;
  left: 120px;
  :hover {
    color: #6c8e67;
  }
}

.image {
  width: 50%;
  display: block;
  position: relative;
  left: 92px;
}
.content-card {
  width: 400px;
  height: 300px;
}
.el-row {
  margin-bottom: 30px;
}
</style>
