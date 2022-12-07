<template>
  <el-container class="container">
    <el-header height="20%">
      <div class="title">帖子详情</div>
    </el-header>
    <div style="margin: 30px 0" />
    <el-card>
      <div>帖子标题: {{ title }}</div>
      <div>发帖者: {{ poster_name }}</div>
      <div>帖子内容: {{ content }}</div>
      <div>点赞数: {{ likes }}</div>
      <img
        alt="Like"
        v-bind:src="likeImage[liked]"
        height="20"
        width="20"
        v-on:click="onClickLike()"
      />
    </el-card>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import qs from "qs";
import likeHollow from "../assets/like_hollow.png";
import likeSolid from "../assets/like_solid.png";
const likeImage = [likeHollow, likeSolid];

let poster_name = ref("");
let title = ref("");
let content = ref("");
let params1 = ref({ keywords: ["测试"] });
let liked = ref(0);
let likes = ref(0);
let stared = ref(0);

function handlepost() {
  console.log(textarea_title.value);
  console.log(textarea_content.value);
  axios
    .get("/api/newpost", {
      params: {
        username: username,
        group_name: group_name,
        title: textarea_title,
        content: textarea_content,
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
function onClickLike() {
  axios
    .get("/api/like", {
      params: {
        username: "TestAccount",
        post_id: 36,
      },
    })
    .then((res) => {
      if (res.data.code != 1) {
        alert("服务器错误，请稍后重试");
        return;
      }
      likes.value = res.data.likes;
      liked.value = (liked.value + 1) % 2;
    });
}
onMounted(() => {
  axios
    .get("/api/getpost", {
      params: {
        username: "TestAccount",
        post_id: 36,
      },
    })
    .then((res) => {
      poster_name.value = res.data.poster_name;
      title.value = res.data.title;
      content.value = res.data.content;
      liked.value = res.data.liked;
      likes.value = res.data.likes;
      stared.value = res.data.stared;
      console.log(res.data);
    });

  axios.post("/api/search", params1.value).then((res) => {
    console.log(res.data);
  });
});
</script>
