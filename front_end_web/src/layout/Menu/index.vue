<template>
    <el-menu active-text-color="#ffd04b" background-color="#6c8e67" class="el-menu-vertical-demo" 
        text-color="#fff" :default-active="defaultActive" :collapse="!$store.getters.getSidebarType" router>
        <div id="logo" v-if="$store.getters.getSidebarType">
            MyBBS
        </div>
        <div id="userInfo" v-if="$store.getters.getSidebarType">
            <div class="avatar-container">
                <el-avatar :src="userAvatar" :size="100" v-if="isLogin"/>
                <el-avatar :src="initAvatar" :size="100"  v-else/>
            </div>
            <div class="user-container" v-if="isLogin">
                <div class="user-name">
                {{$store.state.username}}
                </div>
                <div class="logout-button">
                    <button class="login-button" @click="Logout">退出登录</button>
                </div>
            </div>

            <div class="button-container" v-else>
                <button class="login-button" @click="goLogin">登录/注册</button>
            </div> 
        </div>
        <el-menu-item index="/home" @click="savePath('/home')">
                <el-icon>
                    <house />
                </el-icon>
                <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/group" @click="savePath('/group')">
            <el-icon>
                <document />
            </el-icon>
            <span>版面目录</span>
        </el-menu-item>
        <el-menu-item index="/star" @click="savePath('/star')">
            <el-icon>
                <star />
            </el-icon>
            <span>收藏夹</span>
        </el-menu-item>
        <el-menu-item index="/user" @click="savePath('/user')">
            <el-icon>
                <user />
            </el-icon>
            <span>个人资料</span>
        </el-menu-item>
    </el-menu>

</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import router from '@/router';
import api from '@/api/index'

const store = useStore();

//const defaultActive = ref(sessionStorage.getItem('path') || '/home');

// computed用于监听数据的变化
const defaultActive = computed(()=> {
    return store.getters.getPath;
})


const isLogin = computed(() => {
    return store.getters.getToken === 'true' ? true : false;
});


const user_name = store.getters.getUsername;

const initAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
            

onMounted(() => {
    //登录后请求用户信息并保存
    if(isLogin.value){
        let params = {username: user_name};
        api.getUserAvatar(params).then(
            res => {
                let avatarUrl = res.data.image;
                //console.log(avatarUrl);
                //base64空格处理
                if(avatarUrl != null && avatarUrl != ''){
                    avatarUrl = avatarUrl.replace(/\s/g, '+');
                }
                //用户尚未上传头像
                else if(avatarUrl == '') {
                    avatarUrl = initAvatar.value;
                    // console.log(avatarUrl);
                }
                store.commit('setAvatar',avatarUrl);
            }             
        );
    }
 
})

//用户头像的获取
//默认头像
const userAvatar = computed(() => {
    return store.getters.getUserAvatar;
});


const savePath = (path) => {
    store.commit('setPath', path);
}

//未登录，跳转到登陆页面
const goLogin = () => {
    router.replace('/login')
}

//退出登录
const Logout = () => {
    store.commit('setToken', 'false');
    store.commit('setPath', '/home');
    store.commit('setUsername', '');
    store.commit('setAvatar', '');
    router.push('home');
}

</script>

<style lang="scss" scoped>
#logo {
    background-color: #6c8e67;
    font-family: fantasy;
    font-weight: lighter;
    color: white;
    font-size: 3em;
    text-align: center; //水平居中
    position: relative;
    top: auto;
    padding: 10px 0 20px;
}
.userInfo{
    display: block;
}

.avatar-container {
    padding: 10px 0 20px;
}
.button-container {
    padding: 20px 0 40px;

}
.user-container {
    padding: 0 0 30px;
}

.user-name{
    padding:10px 0 30px;
    font-family: system-ui;
    font-weight: 500;
    color: white;
    font-size: 1.5em;
}

.login-button{
  width: 100px;
  height: 45px;
  border-radius: 10px;
  border: none;
  outline: none;
  background-color: #959b94;
  color: #fff;
  font-size: 1em;
  cursor: pointer;
}


</style> 

