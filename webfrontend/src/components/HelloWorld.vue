<template>
  <div class="hello-page">
    <div class="welcome">Hello! Welcome to Our BBS</div>
    <el-button type="primary" class="ping-button" @click="handlePing"
      >Ping</el-button
    >
  </div>
</template>

<script>
// import { ping } from '@/api/ping'
import axios from 'axios'

export default {
  data() {},
  methods: {
    handlePing() {
      axios.get('http://192.168.31.107:8080/api').then((res) => {
        alert(res.data)
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
</style>
