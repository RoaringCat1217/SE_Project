<template>
  <div class="title">我的个人信息</div>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <el-row class="avatar-username" :gutter="20">
          <el-col :span="1"></el-col>
          <el-col :span="2">
            <el-avatar :src="image_base64 == '' ? init_base64 : image_base64" :size="100" />
          </el-col>
          <el-col :span="1"></el-col>
          <el-col :span="2" class="username">
            <span>{{username}}</span>
          </el-col>
          <el-col :span="1" :offset="12">
            <el-button class="button" text @click="dialogVisible = true">修改信息</el-button>
          </el-col>
        </el-row>
      </div>
    </template>
    <el-descriptions title="详细资料" :column="3" border>
      <el-descriptions-item label="昵称" label-align="center" align="center" label-class-name="my-label"
        class-name="my-content" width="150px">{{ nickname }}</el-descriptions-item>
      <el-descriptions-item label="性别" label-align="center" align="center" label-class-name="my-label"
        class-name="my-content" width="150px">{{ gender }}</el-descriptions-item>
      <el-descriptions-item label="年龄" label-align="center" align="center">{{ age }}</el-descriptions-item>
      <el-descriptions-item label="电话" label-align="center" align="center">{{ tele }}</el-descriptions-item>
      <el-descriptions-item label="其他" label-align="center" align="center">
        <el-tag size="small" type="info">暂无</el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
  <el-dialog v-model="dialogVisible" title="修改个人信息" width="30%">
    <el-form :model="form" label-width="120px">
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="form.gender" placeholder="选择您的性别">
          <el-option label="男" value="male" />
          <el-option label="女" value="female" />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age" :rules="[
        { required: false },
        { type: 'number', message: '年龄必须为整数' },
      ]">
        <el-input v-model.number="form.age" type="text" autocomplete="off" />
      </el-form-item>
      <el-form-item label="电话" prop="phone_number" :rules="[
        { required: false },
        { type: 'number', message: '电话号码必须为数字' },
      ]">
        <el-input v-model.number="form.phone_number" type="text" autocomplete="off" />
      </el-form-item>
      <el-form-item label="用户头像">
        <el-upload class="avatar-uploader" action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
          :show-file-list="false" :on-change="handleUpload">
          <img v-if="form.avatar" :src="form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button class="button_submit" @click="submitChanges">
          确认修改
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from "vue";
import router from '@/router'
import { useStore } from 'vuex'
import api from "@/api";


const dialogVisible = ref(false);

const store = useStore();
const username = computed(() => {
  return store.getters.getUsername;
});

const init_base64 = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');

const form = reactive({
  nickname: "",
  gender: "",
  age: "",
  phone_number: "",
  avatar: "",
});

let newUserInfo = ref({
  username: store.getters.getUsername,
  gender: "-1",
  age: -1,
  avatar: "-1",
  phone_number: "-1",
  nickname: "-1",
});

let nickname = ref("未设置");
let gender = ref("未设置");
let age = ref("未设置");
let tele = ref("未设置");
let image_base64 = ref("");

function handleUpload(file, fileList) {
  getBase64(file.raw).then((res) => {
    console.log(file.size);
    if (file.size > 102400) {
      alert("图片超过100KB，请重新选择图片上传");
      return;
    }
    const params = res;
    form.avatar = params;
  });
}

function getBase64(file) {
  return new Promise(function (resolve, reject) {
    const reader = new FileReader();
    let imgResult = "";
    reader.readAsDataURL(file);
    reader.onload = function () {
      imgResult = reader.result;
    };
    reader.onerror = function (error) {
      reject(error);
    };
    reader.onloadend = function () {
      resolve(imgResult);
    };
  });
}

function submitChanges() {
  if (form.age) {
    if (typeof form.age != "number") {
      ElMessage({
        message: '输入的年龄应该为整数！',  
        type: 'error',
        duration: 1000
      });
      return;
    }
  }
  
  if (form.nickname != "") {
    newUserInfo.value.nickname = form.nickname;
  }
  if (form.gender != "") {
    newUserInfo.value.gender = form.gender;
  }
  if (form.age) {
    newUserInfo.value.age = form.age;
  }
  if (form.phone_number != "") {
    newUserInfo.value.phone_number = form.phone_number;
  }
  if (form.avatar != "") {
    newUserInfo.value.avatar = form.avatar;
  }
  console.log(newUserInfo.value)
  api.updateUserInfo(newUserInfo.value).then((res) => {
    console.log(res.data.code);
    if (res.data.code != 1) {
      ElMessage({
        message: '个人信息更新失败！',
        type: 'error',
        duration: 1000
      });
    } else {
      dialogVisible.value = false;
      ElMessage({
        message: '个人信息更新成功！',
        type: 'success',
        duration: 1000
      });
      router.go(0);
    }
  });
}

onMounted(() => {
  api.getUserInfo({username: store.getters.getUsername})
    .then(res => {
      if (res.data.gender == "male") {
        gender.value = "男";
      }
      else if (res.data.gender == "female") {
        gender.value = "女";
      }
      if(!res.data.age){
        age.value = "未设置";
      }
      else age.value = res.data.age;

      if(res.data.phone_number == 'None'){
        tele.value = "未设置";
      }
      else tele.value = res.data.phone_number;
      
      if(res.data.nickname == -1){
        nickname.value = "未设置";
      }
      else nickname.value = res.data.nickname;


      let avatar_str = res.data.avatar.replace(/\s/g, "+");
      image_base64.value = avatar_str;
    });
  })


</script>

<style lang="scss" scoped>
.app-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.container {
  width: calc(100% - $sideBarWidth + 10px);
  height: 100%;

  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  transition: all 0.28s;
  padding: 20px;

  &.hidderContainer {
    width: calc(100% - $hideSideBarWidth);
  }
}

.groupcontent {
  left: 160px;
}

::v-deep(.el-header) {
  padding: 0;
  margin: 0;
}

.title {
  font-family: system-ui;
  font-weight: bolder;
  color: #6c8e67;
  font-size: 3em;
  text-align: center; //水平居中
  position: relative;
  top: auto;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  background-color: #6c8e67;
  padding: 5;
  min-height: auto;
  position: relative;
  font-family: sustem-ui;
  font-weight: bold;
  color: white;
  left: 120px;

  :hover {
    color: #6c8e67;
  }
}

.button_submit {
  background-color: #6c8e67;
  font-family: system-ui;
  font-weight: bold;
  color: white;

  :hover {
    color: #6c8e67;
  }
}

.image {
  width: 50%;
  display: block;
  position: relative;
  left: 92px;
}

.box-card {
  margin-top: 50px;

}


.avatar-username {
  display: flex;
  align-items: center;
}

.el-row {
  margin-bottom: 30px;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.username {
  font-family: system-ui;
  font-size:2em;
  font-weight: bold;
}
</style>

