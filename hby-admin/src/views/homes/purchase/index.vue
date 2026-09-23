<template>
  <div class="system-log-container">
    <el-row :gutter="10">
      <el-col>
        <div class="top"></div>
      </el-col>
      <el-col :span="24">
        <firstLeft></firstLeft>
      </el-col>
      <el-col :span="24">
        <second></second>
      </el-col>
      <el-col :span="15">
        <thirdLeft></thirdLeft>
      </el-col>
      <el-col :span="9">
        <thirdRight></thirdRight>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import firstLeft from './components/firstLeft.vue'
  import second from './components/seconds.vue'
  import thirdLeft from './components/thirdLeft.vue'
  import thirdRight from './components/thirdRight.vue'
  export default {
    name: 'Purchase',
    components: { firstLeft, second, thirdLeft, thirdRight },
    data() {
      return {}
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background-color: #f6f8f9;
  }
  .top {
    background: url('https://enterprise.e-cology.com.cn/page/resource/userfile/image/caigoul.png')
      center center / cover no-repeat;
    height: 150px;
  }
</style>
