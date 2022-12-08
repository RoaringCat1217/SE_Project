<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>{{ $route.params.groupName }}</span>
            </div>
        </template>

        <el-table stripe height="431" :data="postListData.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
            style="width: 100%" @row-click="handlePostList">
            <el-table-column prop="post_id" label="序号" align="center" width="100" />
            <el-table-column prop="title" label="标题" align="center" />
            <el-table-column prop="poster_name" label="发帖人" align="center" width="250" />
        </el-table>

        <div id="pagination">
            <el-pagination background v-model:current-page="currentPage" v-model:page-size="pageSize"
                layout="prev, pager, next,jumper,-> ,total" :total="postListData.length"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>

        <!--发帖模块-->
        <div id="post" v-if="isLogin">
            <p>快捷发帖</p>
            <el-form ref="postRef" :model="postInfo" label-width="50px" :rules="commitRules">
                <el-form-item label="标题" prop="title">
                    <el-col :span="14">
                        <el-input v-model="postInfo.title" placeholder="标题建议不超过20个字符" maxlength="20" show-word-limit />
                    </el-col>
                </el-form-item>
                <el-form-item label="正文" prop="content">
                    <el-input v-model="postInfo.content" :rows="10" type="textarea" placeholder="请输入帖子正文内容"
                        maxlength="1000" show-word-limit />
                </el-form-item>
            </el-form>
            <button class="commit-button" @click="commitPost">发布</button>
        </div>

    </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import api from '@/api/index'
import router from '@/router'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

const postListData = reactive([]);
const route = useRoute();
const store = useStore();

//未登录的状态下无法发贴
const isLogin = computed(() => {
    return store.getters.getToken === 'true' ? true : false;
});

const postInfo = reactive({
    title: '',
    content: ''
});
const postRef = ref(null);

//分页器参数
let currentPage = ref(1);
const pageSize = ref(10);


const handleCurrentChange = (page) => {
    //页码更改方法
    currentPage = page;
}

onMounted(() => {
    api.getPostList({ group_name: route.params.groupName }).then(
        res => {
            if (res.data.code === 1) {
                for (let i = 0; i < res.data.number; i++) {
                    postListData[i] = res.data.entry[i];
                }
            }
        }
    );
})

const handlePostList = (row, column, event) => {
    // console.log(row);
    let path = '/post' + '/' + row.post_id;
    router.push(path);
    store.commit('setPath', path);
}


//发帖
const commitRules = reactive({
    title: [
        {
            required: true,
            message: '标题不能为空',
            trigger: 'blur'
        },
        {
            max: 20,
            message: '标题长度不能超过二十个字符',
            trigger: 'blur'
        }
    ],
    content: [
        {
            required: true,
            message: '正文不能为空',
            trigger: 'blur'
        },
        {
            max: 1000,
            message: '标题长度不能超过一千个字符',
            trigger: 'blur'
        }
    ]
});

//表单校验
const commitPost = () => {
    let params = {
        username: store.getters.getUsername,
        group_name: route.params.groupName,
        ...postInfo
    };
    //console.log(params);
    postRef.value.validate(async (valid) => {
        if (valid) {
            const res = await api.newPost(params)
                .then(res => {
                    //console.log(res.data);
                    if (res.data.code === 1) {
                        ElMessage({
                            message: '发帖成功！',
                            type: 'success',
                            duration: 1000
                        });

                        //路径存储和路由跳转
                        let path = '/post' + '/' + res.data.post_id;
                        router.push(path);
                        store.commit('setPath', path);
                    }
                    else {
                        ElMessage({
                            message: '发帖失败！',
                            type: 'error',
                            duration: 1000
                        });
                    }
                })
                .catch(err => {
                    console.log(err);
                })
        }
    })
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
    color: white;
}

:deep(.el-card__header) {
    background-color: #6c8e67;
}

.box-card {
    width: 1000px;
    margin-left: 150px;
    margin-right: 100px;
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

#post {
    padding-right: 40px;
    padding-top: 50px;
    padding-bottom: 30px;

    p {
        text-align: left;
        padding-left: 5px;
        font-family: system-ui;
        font-size: 1.2em;
        font-weight: bold;
        color: #6c8e67;
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
</style>