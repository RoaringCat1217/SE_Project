<template>
  <div class="login-register">
    <div class="title">
      Welcome to our bbs!
    </div>
    <div class="container">
      <!--:class = " { 样式类名:响应式数据 } "  当响应式数据为TRUE的时候，才会有这个样式-->
      <div class="big-box" :class="{ active: isLogin }">
        <!--使用key属性来防止v-if条件渲染时的惰性组件复用-->
        <div class="big-container" key="big-LoginContainer" v-if="isLogin">
          <div class="big-title">账户登录</div>
          <el-form ref="formRef" :model="form" class="login-register-form" :rules="loginRules" status-icon>
            <el-form-item prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" size="large">
                <template #prefix>
                  <el-icon class="el-input__icon">
                    <user />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" size="large">
                <template #prefix>
                  <el-icon class="el-input__icon">
                    <Lock />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
          <button class="big-button" @click="login">登录</button>
        </div>

        <div class="big-container" key="big-RegisterContainer" v-else>
          <div class="big-title">创建账户</div>
          <el-form ref="formRef" :model="form" class="login-register-form" :rules="registerRules" status-icon>
            <el-form-item prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" size="large">
                <template #prefix>
                  <el-icon class="el-input__icon">
                    <user />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="form.password" type="password" autocomplete="off" placeholder="请输入密码" size="large">
                <template #prefix>
                  <el-icon class="el-input__icon">
                    <lock />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="checkPassword">
              <el-input v-model="form.checkPassword" type="password" autocomplete="off" placeholder="再次输入以验证密码"
                size="large">
                <template #prefix>
                  <el-icon class="el-input__icon">
                    <lock />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
          <button class="big-button" @click="register">注册</button>
        </div>

      </div>

      <div class="small-box" :class="{ active: isLogin }">
        <div class="small-container" key="small-RegisterContainer" v-if="isLogin">
          <div class="small-title">欢迎回来!</div>
          <p class="small-content">Go to Register!</p>
          <button class="small-button" @click="changeType">注册</button>
        </div>

        <div class="small-container" key="small-LoginContainer" v-else>
          <div class="small-title">你好，朋友!</div>
          <p class="small-content">Go to Login!</p>
          <button class="small-button" @click="changeType">登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import api from '@/api/index'
import router from '@/router'
import { useStore } from 'vuex'

export default {
  name: 'login-register',
  props: {

  },
  setup(props) {
    //使用Vuex
    const store = useStore();

    const form = reactive({
      username: '',
      password: '',
      checkPassword: ''
    });

    const isLogin = ref(true);
    const formRef = ref(null);

    //各个表单项的验证规则
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error('用户名不能为空'));
      } else if (!/^\w+$/.test(value)) {
        callback(new Error('用户名只能包含数字、英文字母和下划线'));
      } else if (value.length > 20) {
        callback(new Error('用户名长度不能超过二十个字符'));
      } else {
        callback();
      }
    }

    const validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error('密码不能为空'));
      } else if (value.length > 20) {
        callback(new Error('密码长度不能超过二十个字符'));
      } else {
        if (form.checkPassword !== '') {
          if (!formRef.value) return;
          formRef.value.validateField('checkPassword', () => null);
        }
        callback();
      }
    }

    const validateCheck = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请再次输入密码'));
      } else if (value !== form.password) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    }

    // 登录表单验证
    const loginRules = reactive({
      username: [
        {
          required: true,
          validator: validateUsername,
          trigger: 'blur'
        }
      ],
      password: [
        {
          required: true,
          message: '密码不能为空',
          trigger: 'blur'
        },
        {
          max: 20,
          message: '密码长度不能超过二十个字符',
          trigger: 'blur'
        }
      ],
    })

    //注册表单验证
    const registerRules = reactive({
      username: [
        {
          required: true,
          validator: validateUsername,
          trigger: 'blur'
        }
      ],
      password: [
        {
          required: true,
          validator: validatePass,
          trigger: 'blur'
        }
      ],
      checkPassword: [
        {
          validator: validateCheck,
          trigger: 'blur'
        }
      ]
    })

    const changeType = () => {
      isLogin.value = !(isLogin.value);
      form.username = '';
      form.password = '';
      form.checkPassword = '';
    }


    //登录校验与登录
    const login = () => {
      formRef.value.validate(async (valid) => {
        if (valid) {
          //store.dispatch('app/login', form.value);
          const res = await api.handleLogin(form)
            .then(res => {
              //路由页面跳转
              const data = res.data;
              //成功登入
              if (data.code === 1) {
                ElMessage({
                  message: '登录成功！',
                  type: 'success',
                  duration: 1000
                });

                //localStorage里存的都是字符串
                store.commit('setToken','true');
                store.commit('setUsername',form.username);
                //console.log(form);

                //跳转到主页面
                router.replace(store.getters.getPath);
              }
              else {
                ElMessage({
                message: '登录失败！',
                type: 'error',
                duration: 1000
                });
              }
            })
            .catch(err => {
              console.log(err);
            });        
          //表单校验成功  
          console.log('submit!');
        } 
        else {
          console.log('error submit!!');
        }
      })
    }

    //注册校验与注册
    const register = () => {
      formRef.value.validate(async (valid) => {
        if (valid) {
          //store.dispatch('app/login', form.value);
          const res = await api.handleRegister(form)
            .then((res) => {
              const data = res.data;
              //注册成功
              if(data.code === 1){
                router.go(0);
                ElMessage({
                  message: '注册成功！',
                  type: 'success',
                  duration: 1000
                });
              }
              //注册失败
              else if(data.code === 2){
                ElMessage({
                  message: '注册失败！',
                  type: 'error',
                  duration: 1000
                });
              }
              //用户名重复
              else{
                ElMessage({
                  message: '注册失败！用户名重复！',
                  type: 'error',
                  duration: 1000
                });
              }     
            })
          console.log('submit!');
        } else {
          console.log('error submit!!');
          //return false;
        }
      })
    }


    return {
      form,
      formRef,
      isLogin,
      loginRules,
      registerRules,

      changeType,
      login,
      register,
    }
  }
}

</script>

<style lang="scss" scoped>
$shadow-gray: #f0f0f0;
$light-green: #edf7eb;
$dark-green: #6c8e67;
$mid-green: rgb(49, 170, 134);

.login-register {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  position: fixed;
  background: $light-green;
}


.title {
  font-family: fantasy;
  font-weight: lighter;
  color: $dark-green;
  font-size: 5em;
  text-align: center; //水平居中
  position: relative;
  top: auto;
  transform: translateY(5vh);
}

.container {
  width: 60%;
  height: 60%;
  position: relative;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 0 3px $shadow-gray,
    0 0 6px $shadow-gray;
}

.big-box {
  width: 70%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 30%;
  transform: translateX(0%);
  transition: all 1s;
}

.big-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.big-title {
  font-size: 2em;
  font-weight: bolder;
  color: $dark-green;
}


.login-register-form {
  width: 60%;
  height: 40%;
  padding: 1.5em 0;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  /* 均匀排列每个元素 每个元素周围分配相同的空间 */
}

.el-input {
  --el-input-focus-border-color: #6c8e67;
}

.el-input__icon {
  color: $dark-green;
}

.big-button {
  width: 25%;
  height: 10%;
  border-radius: 20px;
  border: none;
  outline: none;
  background-color: $dark-green;
  color: #fff;
  font-size: 1em;
  cursor: pointer;
}

.small-box {
  width: 30%;
  height: 100%;
  background: linear-gradient(135deg, $dark-green, $mid-green);
  position: absolute;
  top: 0;
  left: 0;
  transform: translateX(0%);
  transition: all 1s;
  border-top-left-radius: inherit;
  border-bottom-left-radius: inherit;
}

.small-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.small-title {
  font-size: 2em;
  font-weight: bolder;
  color: #fff;
}

.small-content {
  font-size: 1.2em;
  color: #fff;
  text-align: center;
  padding: 2em 3em;
  line-height: 1.7em;
}

.small-button {
  width: 60%;
  height: 10%;
  border-radius: 20px;
  border: 1px solid #fff;
  outline: none;
  background-color: transparent;
  color: #fff;
  font-size: 1em;
  cursor: pointer;
}

.big-box.active {
  left: 0;
  transition: all 0.5s;
}

.small-box.active {
  left: 100%;
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  border-top-right-radius: inherit;
  border-bottom-right-radius: inherit;
  transform: translateX(-100%);
  transition: all 1s;
}
</style>