<template>
  <el-container class="app-wrapper">
    <el-aside width="200px" class="sidebar-container">
      <Menu />
    </el-aside>
    <el-container class="container">
      <el-header height="20%">
        <div class="title">我的个人信息</div>
      </el-header>
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <el-row class="avatar-username">
              <el-col :span="2">
                <el-avatar :src="image_base64" :size="70" />
              </el-col>
              <el-col :span="2" class="username">
                <span>admintest_www</span>
              </el-col>
              <el-col :span="1" :offset="14">
                <el-button class="button" text @click="dialogVisible = true"
                  >修改信息</el-button
                >
              </el-col>
            </el-row>
          </div>
        </template>
        <el-descriptions title="详细资料" :column="3" border>
          <el-descriptions-item
            label="性别"
            label-align="right"
            align="center"
            label-class-name="my-label"
            class-name="my-content"
            width="150px"
            >{{ gender }}</el-descriptions-item
          >
          <el-descriptions-item
            label="年龄"
            label-align="right"
            align="center"
            >{{ age }}</el-descriptions-item
          >
          <el-descriptions-item
            label="电话"
            label-align="right"
            align="center"
            >{{ tele }}</el-descriptions-item
          >
          <el-descriptions-item label="其他" label-align="right" align="center">
            <el-tag size="small" type="info">暂无</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-dialog v-model="dialogVisible" title="修改个人信息" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="用户名" :rules="[{ required: true }]">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="选择您的性别">
              <el-option label="男" value="male" />
              <el-option label="女" value="famale" />
            </el-select>
          </el-form-item>
          <el-form-item
            label="年龄"
            prop="age"
            :rules="[
              { required: false },
              { type: 'number', message: '年龄必须为整数' },
            ]"
          >
            <el-input
              v-model.number="form.age"
              type="text"
              autocomplete="off"
            />
          </el-form-item>
          <el-form-item
            label="电话"
            prop="phone_number"
            :rules="[
              { required: false },
              { type: 'number', message: '电话号码必须为数字' },
            ]"
          >
            <el-input
              v-model.number="form.phone_number"
              type="text"
              autocomplete="off"
            />
          </el-form-item>
          <el-form-item label="用户头像">
            <el-upload
              class="avatar-uploader"
              action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
              :show-file-list="false"
              :on-change="handleUpload"
            >
              <img v-if="uploaded_image" :src="uploaded_image" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
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
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
const dialogVisible = ref(false);
const form = reactive({
  name: "",
  gender: "",
  age: "",
  phone_number: "",
  avatar: "",
});

let uploaded_image = ref("");
let group_name = ref([
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
  "测试版面",
]);
let gender = ref("未知");
let age = ref("未知");
let tele = ref("未知");
let image_base64 = ref("");

function handleUpload(file, fileList) {
  getBase64(file.raw).then((res) => {
    if (file.size > 20480) {
      alert("图片过大，请重新选择图片上传");
      return;
    }
    const params = res;
    form.avatar = params;
    uploaded_image.value = params;
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
  if (form.name == "") {
    alert("您未输入用户名，请输入用户名后再试一次");
    return;
  }
  if (typeof form.age != "number") {
    alert("您输入的年龄不是整数，请重新输入");
    return;
  }
  console.log(form.age);
  console.log(form.gender);
  console.log(typeof form.age);

  let tempgender = "-1";
  if (form.gender) {
    tempgender = form.gender;
  }
  let tempage = -1;
  if (form.age) {
    tempage = form.age;
  }
  let tempphone_number = "-1";
  if (form.phone_number) {
    tempphone_number = form.phone_number;
  }
  let tempavatar = "-1";
  if (form.avatar) {
    tempavatar = form.avatar;
  }
  axios
    .get("/api/updateuserinfo", {
      params: {
        name: form.name,
        gender: tempgender,
        age: tempage,
        phone_number: tempphone_number,
        avatar: tempavatar,
      },
    })
    .then((res) => {
      console.log(res.data.code);
      if (res.data.code == 1) {
        alert("个人信息更新成功，请刷新查看");
      }
    });
}

onMounted(() => {
  axios
    .get("/api/getuserinfo", {
      params: {
        name: "admintest_www",
      },
    })
    .then((res) => {
      if (res.data.gender == "male") {
        gender.value = "♂";
      }
      if (res.data.gender == "female") {
        gender.value = "♀";
      }
      age.value = res.data.age;
      tele.value = res.data.phone_number;

      let str = res.data.avatar;
      str = str.replace(/\s/g, "+");
      image_base64.value = str;
    });
});
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
  font-family: fantasy;
  font-weight: bloder;
  color: #6c8e67;
  font-size: 3em;
  text-align: center; //水平居中
  position: relative;
  top: auto;
  transform: translateY(5vh);
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
  font-family: "黑体";
  font-weight: bold;
  color: white;
  left: 120px;
  :hover {
    color: #6c8e67;
  }
}
.button_submit {
  background-color: #6c8e67;
  font-family: "黑体";
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
}
.card-header {
}
.avatar-username {
  display: flex;
  align-items: center;
}
.el-row {
  margin-bottom: 30px;
}
.username {
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
</style>
