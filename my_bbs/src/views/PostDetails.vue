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
                        <div class="like-star">
                            <img alt="Like" v-bind:src="likeImage[post_info.liked]" height="30" width="30"
                                v-on:click="onClickLike()" style="cursor:pointer" class="like" />
                            <span class="like-num">{{ post_info.likes }}</span>
                            <img alt="Star" v-bind:src="starImage[post_info.stared]" height="30" width="30"
                                v-on:click="onClickStar()" style="cursor:pointer" class="star" />
                        </div>
                    </el-footer>
                </el-container>
            </el-container>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import api from '@/api/index'
import router from '@/router'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { toRaw } from '@vue/reactivity'
import axios from "axios";

import likeHollow from "../assets/like_hollow.png";
import likeSolid from "../assets/like_solid.png";
import starHollow from "../assets/star_hollow.png";
import starSolid from "../assets/star_solid.png";

const route = useRoute();
const store = useStore();

const likeImage = [likeHollow, likeSolid];
const starImage = [starHollow, starSolid];

let posterAvatar = ref('');

let post_info = reactive({});

let commentList = reactive([]);

let dialogVisible = ref(false);
let comment_id = ref();
let comment_comment = ref("");
let main_post_comment = ref("");

onMounted(() => {
    let post_params = {
        username: store.getters.getUsername,
        post_id: route.params.postId,
    };
    api.getPostDetails(post_params)
        .then((res) => {
            if (res.data.code === 1) {
                for (let i in res.data) {
                    post_info[i] = res.data[i];
                }
                // console.log(post_info)
            }
        });

    let comment_params = {
        post_id: route.params.postId
    }

    api.getReply(comment_params)
        .then((res) => {
            if (res.data.code === 1) {
                commentList.value = res.data.entry;
                //console.log(commentList.value);
            }
        });


    console.log(toRaw(post_info));
    let avatar_params = {
        //username: .poster_name
    }
    api.getUserAvatar(avatar_params)
        .then(res => {
            console.log
            if (res.data.code === 1) {
                console.log(res)
                posterAvatar.value = res.data.image.replace(/\s/g, '+');
                // console.log(posterAvatar);
            }
        });
});

const onClickLike = () => {
    axios
        .get("/api/like", {
            params: {
                username: store.getters.getUsername,
                post_id: route.params.postId,
            },
        })
        .then((res) => {
            if (res.data.code != 1) {
                alert("服务器错误，请稍后重试");
                return;
            }
            post_info.likes = res.data.likes;
            post_info.liked = (post_info.liked + 1) % 2;
        });
}

const onClickStar = () => {
    axios
        .get("/api/star", {
            params: {
                username: store.getters.getUsername,
                post_id: route.params.postId,
            },
        })
        .then((res) => {
            if (res.data.code != 1) {
                alert("服务器错误，请稍后重试");
                return;
            }
            post_info.stared.value = (post_info.stared.value + 1) % 2;
        });
}
</script>

<style lang="scss" scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-family: system-ui;
    font-size: 1.5em;
    font-weight: bold;
    //color: white;
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
    font-size: 1.5em;

}

.content {
    text-align: left;
    font-weight: bold;
    font-family: system-ui;
    font-size: 1.2em
}

.like-star {
    margin-left: 500px;
    margin-top: 10px;

    .like {
        margin-right: 50px;
    }

}
</style>