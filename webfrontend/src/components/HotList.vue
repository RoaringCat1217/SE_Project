<template>
  <div class="hot-list-page">
    <div class="hot-item">
      <li v-for="item in userlist" :key="item.rank">
        <p class="p1">热榜排行 {{ item.rank + 1 }}</p>
        <p class="p2">
          帖子标题：{{ item.title }}&nbsp;&nbsp; 热点指数：{{
            item.hot_index
          }}&nbsp;&nbsp; 所属讨论组：{{ item.group_info }}
        </p>
      </li>
    </div>
  </div>
</template>

<script>
// import { ping } from '@/api/ping'
import axios from 'axios'

export default {
  data() {
    return {
      userlist: []
    }
  },
  created() {
    axios.get('/api/hot', {}).then((res) => {
      this.userlist = res.data.entry
    })
  },
  methods: {
    handleLoadHotList() {
      axios.get('/api/hot', {}).then((res) => {
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
