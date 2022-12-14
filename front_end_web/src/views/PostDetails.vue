<template>
  <div>
    <el-card class="card">
      <template #header>
        <div class="card-header">
          <span>{{ post_info.title }}</span>
        </div>
      </template>
      <el-container>
        <el-aside width="200px">
          <div class="poster-info">
            <div class="poster-avatar">
              <el-avatar :src="posterAvatar" :size="100" />
            </div>
            <div class="user-name">
              {{ post_info.poster_name }}
            </div>
          </div>
        </el-aside>
        <el-container>
          <el-main>
            <div class="content">{{ post_info.content }}</div>
          </el-main>
          <el-footer>
            <span class="poster-time">发表于 {{ post_info.time }}</span>
            <div class="like-star" v-if="isLogin">
              <img
                alt="Like"
                v-bind:src="likeImage[post_info.liked]"
                height="30"
                width="30"
                v-on:click="onClickLike()"
                style="cursor: pointer"
                class="like"
              />
              <span class="like-num">{{ post_info.likes }}</span>
              <img
                alt="Star"
                v-bind:src="starImage[post_info.stared]"
                height="30"
                width="30"
                v-on:click="onClickStar()"
                style="cursor: pointer"
                class="star"
              />
            </div>
          </el-footer>
        </el-container>
      </el-container>
    </el-card>

    <div class="reply">
      <div class="reply-list">
        <el-card
          v-for="(comment, index) in commentList.slice(
            (currentPage - 1) * pageSize,
            currentPage * pageSize
          )"
          :key="index"
        >
          <template #header>
            <div class="card-header">
              <span>第{{ (currentPage - 1) * pageSize + index + 1 }}楼</span>
            </div>
          </template>
          <el-container>
            <el-aside width="200px">
              <div class="poster-info">
                <div class="poster-avatar">
                  <el-avatar :src="comment.replyer_avatar" :size="100" />
                </div>
                <div class="user-name">
                  {{ comment.replyer_name }}
                </div>
              </div>
            </el-aside>
            <el-container>
              <el-main>
                <div class="content">{{ comment.content }}</div>
                <div class="reference">
                  {{ comment.replied_name }} 在 ta 的帖子中提到：<br /><br />
                  {{ comment.replied_content }}
                </div>
              </el-main>
              <el-footer>
                <span class="poster-time">发表于 {{ comment.reply_time }}</span>
                <div class="like-star">
                  <button
                    class="reply-button"
                    @click="handleReply(comment.reply_id)"
                  >
                    <el-icon :size="20">
                      <Edit />
                    </el-icon>
                    回帖
                  </button>
                </div>
              </el-footer>
            </el-container>
          </el-container>
        </el-card>

        <el-dialog v-model="dialogVisible" title="快速回帖" center>
          <el-input
            v-model="reply_content"
            maxlength="1000"
            :rows="10"
            placeholder="请输入回复的内容"
            show-word-limit
            type="textarea"
          />
          <template #footer>
            <span class="dialog-footer">
              <button
                class="commit-button"
                @click="handleCommit(store.getters.getRepliedId)"
              >
                提交
              </button>
            </span>
          </template>
        </el-dialog>

        <div id="pagination">
          <el-pagination
            background
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            layout="prev, pager, next,jumper,-> ,total"
            :total="commentList.length"
          >
          </el-pagination>
        </div>
      </div>
    </div>

    <div class="new-reply" v-if="isLogin">
      <p>回复主贴</p>
      <el-form
        ref="replyRef"
        :model="replyInfo"
        label-width="50px"
        :rules="commitRules"
      >
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="replyInfo.content"
            :rows="10"
            type="textarea"
            placeholder="请输入帖子正文内容"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <button class="commit-button" @click="commitReply">发布</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import api from "@/api/index";
import router from "@/router";
import { useRoute } from "vue-router";
import { useStore } from "vuex";

import likeHollow from "../assets/like_hollow.png";
import likeSolid from "../assets/like_solid.png";
import starHollow from "../assets/star_hollow.png";
import starSolid from "../assets/star_solid.png";

const route = useRoute();
const store = useStore();

//获取登录状态
const isLogin = computed(() => {
  return store.getters.getToken === "true" ? true : false;
});

const initAvatar = ref(
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
);
const likeImage = [likeHollow, likeSolid];
const starImage = [starHollow, starSolid];

const commentList = ref([]);

let dialogVisible = ref(false);
const reply_content = ref("");

let posterAvatar = ref("");

let post_info = reactive({});

//分页器
let currentPage = ref(1);
const pageSize = ref(10);

//评论主贴
const replyInfo = reactive({
  content: "",
});
const replyRef = ref(null);

//表单校验
const commitRules = reactive({
  content: [
    {
      required: true,
      message: "回复内容不能为空",
      trigger: "blur",
    },
    {
      max: 1000,
      message: "回复长度不能超过一千个字符",
      trigger: "blur",
    },
  ],
});

//回复主贴
const commitReply = () => {
  let params = {
    post_id: route.params.postId,
    username: store.getters.getUsername,
    ...replyInfo,
  };
  replyRef.value.validate(async (valid) => {
    if (valid) {
      await api
        .newComment(params)
        .then((res) => {
          if (res.data.code === 1) {
            ElMessage({
              message: "回复成功！",
              type: "success",
              duration: 1000,
            });
            //刷新页面
            router.go(0);
          } else {
            ElMessage({
              message: "发帖失败！",
              type: "error",
              duration: 1000,
            });
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  });
};

onMounted(() => {
  let post_params = {
    username: store.getters.getUsername,
    post_id: route.params.postId,
  };

  let comment_params = {
    post_id: route.params.postId,
  };

  api.getPostDetails(post_params).then((res) => {
    if (res.data.code === 1) {
      for (let i in res.data) {
        post_info[i] = res.data[i];
      }
      //console.log(post_info)
      api.getUserAvatar({ username: post_info.poster_name }).then((res) => {
        if (res.data.code === 1) {
          if (res.data.image == "") {
            posterAvatar.value = initAvatar.value;
          } else {
            posterAvatar.value = res.data.image.replace(/\s/g, "+");
          }
        }
      });
    }
  });

  api.getReply(comment_params).then(async (res) => {
    if (res.data.code === 1) {
      commentList.value = res.data.entry;
      console.log(commentList.value);
      for (let i in res.data.entry) {
        let poster_id = res.data.entry[i].replyer_name;
        await api.getUserAvatar({ username: poster_id }).then((res) => {
          // 头像显示
          if (res.data.code === 1) {
            if (res.data.image == "") {
              commentList.value[i]["replyer_avatar"] = initAvatar.value;
            } else {
              commentList.value[i]["replyer_avatar"] = res.data.image.replace(
                /\s/g,
                "+"
              );
            }
          }
        });
      }
    }
  });
});

const onClickLike = () => {
  let params = {
    username: store.getters.getUsername,
    post_id: route.params.postId,
  };
  api.like(params).then((res) => {
    if (res.data.code !== 1) {
      alert("服务器错误，请稍后重试");
      return;
    }
    post_info.likes = res.data.likes;
    post_info.liked = (post_info.liked + 1) % 2;
  });
};

const onClickStar = () => {
  let params = {
    username: store.getters.getUsername,
    post_id: route.params.postId,
  };
  api.star(params).then((res) => {
    if (res.data.code != 1) {
      alert("服务器错误，请稍后重试");
      return;
    }
    post_info.stared = (post_info.stared + 1) % 2;
  });
};

//回复评论
//拿到要回复的评论的id
const handleReply = (post_id) => {
  store.commit("setRepliedId", post_id);
  dialogVisible.value = true;
};

const handleCommit = (post_id) => {
  console.log(post_id);
  if (reply_content.value == "") {
    ElMessage({
      message: "请输入帖子内容",
      type: "error",
      duration: 1000,
    });
  } else {
    let params = {
      post_id: post_id,
      username: store.getters.getUsername,
      content: reply_content.value,
    };
    console.log(params);
    api.newComment(params).then((res) => {
      if (res.data.code !== 1) {
        ElMessage({
          message: "服务器错误！请稍后重试！",
          type: "error",
          duration: 1000,
        });
        return;
      } else {
        dialogVisible.value = false;
        ElMessage({
          message: "回复成功",
          type: "success",
          duration: 1000,
        });
        router.go(0);
      }
      //刷新页面
    });
  }
};
</script>

<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-family: system-ui;
  font-size: 1.5em;
  font-weight: bold;
}

.reply {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-family: system-ui;
    font-size: 1em;
    color: #eeeeee;
    height: 2px;
  }

  :deep(.el-card__header) {
    background-color: #6c8e67;
  }
}

.card {
  width: 1000px;
  margin-left: 150px;
  margin-right: 100px;
}

.user-name {
  padding: 20px 0 30px;
  font-family: system-ui;
  font-weight: 500;
  font-size: 1.2em;
}

.content {
  text-align: left;
  font-weight: bold;
  font-family: system-ui;
  font-size: 1.2em;
}

.reference {
  border-left: 5px #888888 solid;
  padding-left: 10px;
  margin-top: 15px;
  text-align: left;
  color: #888888;
  font-family: system-ui;
}

.poster-time {
  margin-top: 0px;
  margin-right: 480px;
  padding-left: 0px;
  font-size: 0.8em;
}

.like-star {
  margin-left: 550px;
  margin-top: 0px;
  display: flex;
  align-items: center;

  .like {
    margin-right: 5px;
  }

  .like-num {
    margin-right: 50px;
  }
}

.commit-button {
  width: 20%;
  height: 50px;
  border-radius: 20px;
  border: none;
  outline: none;
  background-color: #6c8e67;
  color: #fff;
  font-size: 1em;
  cursor: pointer;
}

.reply-button {
  display: flex;
  width: 40%;
  height: 30px;
  justify-content: space-between;
  align-items: center;
  border-radius: 10px;
  border: none;
  outline: none;
  margin-left: 50px;
  background-color: #6c8e67;
  color: #fff;
  font-size: 1em;
  font-family: system-ui;
  cursor: pointer;
}

.reply {
  margin-top: 20px;
  width: 1000px;
  margin-left: 150px;
  margin-right: 100px;
}

.reply-list {
  border-top: 1px #eeeeee solid;
  padding-top: 20px;
}

.new-reply {
  border-top: 1px #eeeeee solid;
  margin-top: 30px;

  p {
    text-align: left;
    padding-left: 5px;
    font-family: system-ui;
    font-size: 1.2em;
    font-weight: bold;
    color: #6c8e67;
  }
}

:deep(.el-footer) {
  border-top: 1px #eeeeee solid;
  height: 40px;
  margin-left: 20px;
  padding-left: 0px;
}

:deep(.el-aside) {
  border-right: 1px #eeeeee solid;
}

.reply-header {
  font-family: system-ui;
  font-size: 1.5em;
  font-weight: bolder;
  padding-right: 850px;
}

#pagination {
  padding-top: 20px;
  text-align: center;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #6c8e67;
}

:deep(.el-pagination) {
  --el-pagination-hover-color: #6c8e67;

  .el-input {
    --el-input-focus-border-color: #6c8e67;
  }
}

:deep(.el-dialog__title) {
  font-family: system-ui;
  font-size: 1.5em;
  font-weight: bolder;
  color: #6c8e67;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
