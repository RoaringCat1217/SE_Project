<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>搜索结果</span>
            </div>
        </template>

        
        <el-table stripe height="431" :data="SearchData.slice((currentPage - 1) * pageSize, currentPage * pageSize)"
            style="width: 100%" @row-click="handleSearchList">
            <el-table-column prop="post_id" label="序号" align="center" width="100" />
            <el-table-column prop="title" label="标题" align="center" />
            <el-table-column prop="group_name" label="所属分组" align="center"/>
            <el-table-column prop="content" label="内容" align="center" width="350" />
        </el-table>

        <div id="pagination">
            <el-pagination background v-model:current-page="currentPage" v-model:page-size="pageSize"
                layout="prev, pager, next,jumper,-> ,total" :total="SearchData.length"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>

    </el-card>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api/index'
import router from '@/router'

const route = useRoute();

let currentPage = ref(1);
const pageSize = ref(10);



const handleCurrentChange = (page) => {
    //页码更改方法
    currentPage = page;
}


const SearchData = reactive([]);

let params = { keywords: route.params.query.split(/\s+/) };
onMounted(() => {
    console.log(params)
    api.getSearchResult(params).then(
        res => {
            //console.log(res.data)
            if (res.data.code === 1) {
                for (let i = 0; i < res.data.number; i++) {
                    SearchData[i] = res.data.entry[i];
                    if(SearchData[i].content.length > 15){
                        SearchData[i].content = SearchData[i].content.slice(0,15) + '...';
                    }
                }
            }
        }
    )
});

const handleSearchList = (row, column, event) => {
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