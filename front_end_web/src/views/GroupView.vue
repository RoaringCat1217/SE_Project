<template>
    <div id="group-list">
        <div class="group-block" v-for="(group, index) in groups" :key="index">
            <el-card :body-style="{ padding: '20px' }" class="content-card">
                <div class="group-icon">
                    <el-icon :size="180" color="#6c8e67">
                        <FolderOpened />
                    </el-icon>
                </div>
                <div style="padding: 14px">
                    <div class="bottom">
                        <div class="title">{{ group.group_name }}</div>
                        <el-button text class="button" @click="enterGroup(group.group_name)">进入版面
                        </el-button>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>
  
<script setup>
import { ref, reactive, onMounted } from "vue"
import api from '@/api/index'
import router from '@/router'
import { useStore } from 'vuex'

const store = useStore();
const groups = reactive([]);

onMounted(() => {
    api.getGroups().then(
        res => {
            for (let i = 0; i < res.data.groups.length; i++) {
                groups[i] = { group_name: res.data.groups[i] };
            }
            // console.log(groups);
        }
    );
});

const enterGroup = (group_name) => {
    //console.log(group_name);
    let path = '/group' + '/' + group_name;
    router.push(path);
    store.commit('setPath', path);
}
</script>
  
<style lang="scss" scoped>
.group-list {
    position: relative;
}

.group-block {
    display: inline-block;
    margin: 50px;
}

.group-icon {
    padding-top: 30px;
    padding-left: 10px;
}

.title {
    font-family: fantasy;
    font-weight: bolder;
    color: #6c8e67;
    font-size: 2.5em;
    //text-align: center; //水平居中
    //position: relative;
    //top: auto;
    //display: block;
    transform: translateX(10px);
}

.bottom {
    margin-top: 40px;
    line-height: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
}

.button {
    margin-top: 10px;
    background-color: #6c8e67;
    padding: 5;
    min-height: auto;
    position: relative;
    font-family: system-ui;
    font-weight: bold;
    color: white;

    :hover {
        color: #6c8e67;
    }
}

.image {
    width: 100%;
    display: block;
    //left: 92px;
}

.content-card {
    width: 500px;
    height: 400px;
}

.el-row {
    margin-bottom: 30px;
}
</style>