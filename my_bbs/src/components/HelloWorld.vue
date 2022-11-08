<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    
  </div>
</template>

<script>
import { mapState } from 'vuex'
//或者直接使用 this.$store.state.count也可，多变量时比较复杂
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  computed: {
    ...mapState(['count','name']),  //这里用于计算属性和store中state同名的写法
  }

  /*
  mapState的使用方法：
  computed: {
    tempCountPlusTempCount2() { 
          return this.tempcount+this.tempcount2
    }, 
    ...mapState(['count','name']),
    ...mapState({
        nameAlias: 'name', // string   映射 this.nameAlias 为 store.state.name的值
        countplustempcount: function (state) { // 用普通函数this指向vue实例,但是在箭头函数中this就不是指向vue实例了(实际上是指向vue实例的上一级)，所以这里必须用普通哈数
          return this.tempcount + state.count
        },
        countplustempcount2 (state) {
          return this.tempcount2 + state.count
        } 
    })
}
  */

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
