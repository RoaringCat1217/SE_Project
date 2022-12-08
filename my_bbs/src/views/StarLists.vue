<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>我的收藏</span>
            </div>
        </template>

        <el-table stripe height="431" :data="starListData.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
            style="width: 100%" @row-click="handlePostList">
            <el-table-column prop="post_id" label="序号" align="center" width="150" />
            <el-table-column prop="group_name" label="所在版面" align="center" width="200" />
            <el-table-column prop="title" label="帖子标题" align="center" />
            <el-table-column prop="time" label="发帖时间" align="center" width="250" />
        </el-table>

        <div id="pagination">
            <el-pagination background v-model:current-page="currentPage" v-model:page-size="pageSize"
                layout="prev, pager, next,jumper,-> ,total" :total="starListData.length"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>

    </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import api from '@/api/index'
import router from '@/router'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

let starListData = reactive([]);
const route = useRoute();
const store = useStore();

//未登录的状态下无法发贴


//分页器参数
let currentPage = ref(1);
const pageSize = ref(10);

const handleCurrentChange = (page) => {
    //页码更改方法
    currentPage = page;
}

let params = {username: store.getters.getUsername };
onMounted(() => {
    api.getStarList(params).then(
        res => {
            if (res.data.code === 1) {
                for (let i = 0; i < res.data.entry.length; i++) {
                    starListData[i] = res.data.entry[i];
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