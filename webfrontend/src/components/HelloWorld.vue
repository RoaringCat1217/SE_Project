<template>
  <div class="hello-page">
    <div class="welcome">Hello! Welcome to Our BBS</div>

    <el-button type="primary" class="ping-button" @click="handleLogin"
      >Login</el-button
    >
    <el-button type="primary" class="ping-button" @click="handleRegister"
      >Register</el-button
    >
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="User" name="first">User</el-tab-pane>
      <el-tab-pane label="Config" name="second">Config</el-tab-pane>
      <el-tab-pane label="Role" name="third">Role</el-tab-pane>
      <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// import { ping } from '@/api/ping'
import axios from 'axios'

export default {
  data() {
    return {
      msg: 'ping',
      username: 'admintest',
      password: '666',
      respData: {
        code: 'asda'
      }
    }
  },

  methods: {
    handlePing() {
      axios.get('http://192.168.31.107:8080/api', {}).then((res) => {
        alert(res.data)
      })
    },
    handleLogin() {
      axios
        .get('/api/login', {
          params: {
            username: this.username,
            password: this.password
          }
        })
        .then((res) => {
          console.log(res)
          if (res.data.code === 1) alert('Login succeeded!')
          else if (res.data.code === 2) alert('Login failed! Please retry.')
        })
    },
    handleRegister() {
      axios
        .get('/api/register', {
          params: {
            username: this.username,
            password: this.password
          }
        })
        .then((res) => {
          console.log(res.data.code)
          if (res.data.code === 1) {
            alert('Login succeeded!')
          } else if (res.data.code === 2) {
            alert('Register failed! Please retry.')
          } else {
            if (res.data.code === 3) {
              alert('Username repeated! Please use another name instead.')
            }
          }
        })
    }
  }
}
</script>

<style lang="scss" scoped>
$bg: #edf7eb;
$fg: #6c8e67;
$light_gray: #6c8e67;
$cursor: #fff;

.hello-page {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-content: center;
}
.hello-page .welcome {
  font-family: fantasy;
  font-weight: lighter;
  color: $fg;
  font-size: 80px;
  text-align: center;
  margin: auto;
  background-color: #ffffff;
  border-radius: 20px;
  width: 1200px;
  position: relative;
  top: 70px;
}
.ping-button {
  font-family: fantasy;
  font-weight: 100;
  font-size: 25px;
  text-align: center;
  background: $fg;
  margin: auto;
  width: 100px;
  height: 40px;
  position: relative;
  top: -120px;
  border: 0;
}
.demo-tabs {
  color: red;
}
</style>
