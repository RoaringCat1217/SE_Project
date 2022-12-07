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
      <img
        alt="Like"
        v-bind:src="starImage[stared]"
        height="20"
        width="20"
        v-on:click="onClickStar()"
      />
    </el-card>
    <el-table :data="commentList" border style="padding-left;: 10%">
      <el-table-column prop="replyer_name" label="评论者" />
      <el-table-column prop="content" label="评论内容" />
      <el-table-column prop="replied_content" label="被评论内容" />
      <el-table-column prop="replied_name" label="被评论人" />
      <el-table-column prop="reply_time" label="评论时间" />
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="small"
            @click="(comment_id = scope.row.reply_id), (dialogVisible = true)"
            >评论</el-button
          ></template
        >
      </el-table-column>
    </el-table>
    <el-input
      v-model="main_post_comment"
      :rows="10"
      type="textarea"
      placeholder="请在此评论主帖"
    />
    <el-button class="button" type="success" @click="replyMainPost"
      >评论主贴</el-button
    >
    <el-dialog v-model="dialogVisible" title="修改个人信息" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item
          label="请输入您的评论"
          :rules="[
            { required: ture },
            { type: 'text', message: '年龄必须为整数' },
          ]"
        >
          <el-input v-model.number="comment_comment" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button_submit" @click="submitComment">
            提交评论
          </el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import qs from "qs";
import likeHollow from "../assets/like_hollow.png";
import likeSolid from "../assets/like_solid.png";
import starHollow from "../assets/star_hollow.png";
import starSolid from "../assets/star_solid.png";
const likeImage = [likeHollow, likeSolid];
const starImage = [starHollow, starSolid];

let poster_name = ref("");
let title = ref("");
let content = ref("");
let liked = ref(0);
let likes = ref(0);
let stared = ref(0);

let commentList = ref([]);
let dialogVisible = ref(false);
let comment_id = ref();
let comment_comment = ref("");
let main_post_comment = ref("");

function replyMainPost() {
  if (main_post_comment.value == "") {
    alert("评论内容不能为空");
    return;
  }
  axios
    .get("/api/newcomment", {
      params: {
        username: "TestAccount",
        post_id: 36,
        content: main_post_comment.value,
      },
    })
    .then((res) => {
      if (res.data.code != 1) {
        alert("评论失败");
      } else {
        alert("评论成功");
      }
    });
}

function submitComment() {
  if (comment_comment.value == "") {
    alert("评论内容不能为空");
    return;
  }
  axios
    .get("/api/newcomment", {
      params: {
        username: "TestAccount",
        post_id: comment_id.value,
        content: comment_comment.value,
      },
    })
    .then((res) => {
      if (res.data.code != 1) {
        alert("评论失败");
      } else {
        alert("评论成功");
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
function onClickStar() {
  axios
    .get("/api/star", {
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
      stared.value = (stared.value + 1) % 2;
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
  axios
    .get("/api/getreply", {
      params: {
        post_id: 36,
      },
    })
    .then((res) => {
      commentList.value = res.data.entry;
      console.log(commentList.value);
    });
});
</script>
