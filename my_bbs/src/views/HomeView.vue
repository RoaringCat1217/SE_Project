<template>
  <el-row :gutter="40">
    <el-col :span="16">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>站内热点</span>
            <!--
            <el-button class="button" size="small" text @click="goHotList">更多</el-button>
            -->
          </div>
        </template>

        <el-table stripe :data="HotListData" height="431" style="width: 100%" @row-click="handleHotList">
          <el-table-column prop="rank" label="排名" align="center" width="100" />
          <el-table-column prop="title" label="标题" align="center"/>
          <el-table-column prop="group_name" label="版面" align="center" width="100" />
          <el-table-column prop="hot_index" label="热度指数" align="center" width="100" />
        </el-table>
      </el-card>

    </el-col>

    <el-col :span="8">
      <el-card class="box-card-2">
        <template #header>
          <div class="card-header">
            <span>热门版面</span>
            <el-button class="button" size="small" text @click="goGroupList">更多</el-button>
          </div>
        </template>

        <el-table stripe :data="GroupData" height="431" :show-header="false" style="width: 100%" @row-click="handleGroup">
          <el-table-column prop="group_name" align="center" label="版面" />
        </el-table>
      </el-card>

    </el-col>

  </el-row>

</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import api from '@/api/index'
import router from '@/router'
import { useStore } from 'vuex'

let HotListData = reactive([]);
let GroupData = reactive([]);

const store = useStore();
const maxItemNum = 10;

onMounted(() => {
  api.getHotList().then(
    res => {
      if (res.data.code === 1) {
        let length = Math.min(maxItemNum, res.data.entry.length);
        for (let i = 0; i < length; i++) {
          HotListData[i] = res.data.entry[i];
        }
      }
    }
  );

  api.getGroups().then(
    res => {
      //成功接收
      if (res.data.code === 1) {
        let length = Math.min(maxItemNum, res.data.groups.length);
        for (let i = 0; i < length; i++) {
          GroupData[i] = { group_name: res.data.groups[i] };
        }
      }
    }
  );
})

const handleGroup = (row, column, event) => {
  // console.log(row);
  let path = '/group' + '/' + row.group_name;
  router.push(path);
  store.commit('setPath', path);
}

const handleHotList = (row, column, event) => {
  // console.log(row.post_id);
  let path = '/post' +'/' + row.post_id;
  router.push(path);
  store.commit('setPath', path);
}

const goHotList = () => {
  let path = '/hot';
  router.push(path);
  store.commit('setPath', path);
}

const goGroupList = () => {
  let path = '/group';
  router.push(path);
  store.commit('setPath', path);
}

const testPost = () => {
  const data = reactive({ keywords: ['测试', '帖子'] });
  api.getSearchResult(data).then(
    res => {
      console.log(res.data.entry);
    }
  )
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


.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.button {
  color: black;
  font-family: system-ui;
  font-size: 13px;
}

.el-button--small {
  padding: 5px 2px;
}

.box-card {
  height: 560px;
  margin-left: 20px;

}

.box-card-2 {
  height: 560px;
  margin-right: 20px;
}
</style>
