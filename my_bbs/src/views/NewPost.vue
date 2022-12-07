<template>
  <el-container class="container">
    <el-header height="20%">
      <div class="title">发帖</div>
    </el-header>
    <div style="margin: 30px 0" />
    <div>发帖标题</div>
    <el-input
      v-model="textarea_title"
      :rows="2"
      type="textarea"
      placeholder="请在此输入发帖标题"
    />
    <div>请输入发帖内容</div>
    <el-input
      v-model="textarea_content"
      :rows="10"
      type="textarea"
      placeholder="请在此输入发帖标题"
    />
    <el-button class="button" type="success" @click="handlepost"
      >发帖</el-button
    >
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";

const textarea_title = ref("");
const textarea_content = ref("");
const username = "admintest_www";
const group_name = "美食天地";

function handlepost() {
  console.log(textarea_title.value);
  console.log(textarea_content.value);
  if (textarea_content.value == "" || textarea_title.value == "") {
    alert("标题和内容不能为空");
    return;
  }
  axios
    .get("/api/newpost", {
      params: {
        username: username,
        group_name: group_name,
        title: textarea_title.value,
        content: textarea_content.value,
      },
    })
    .then((res) => {
      console.log(res.data);
      if (res.data.code != 1) {
        alert("发帖失败");
      } else {
        alert("发帖成功");
      }
    });
}
</script>
