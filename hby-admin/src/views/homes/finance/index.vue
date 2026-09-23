<template>
  <div class="system-log-container">
    <el-row :gutter="10">
      <el-col>
        <div class="top"></div>
      </el-col>
      <el-col :span="24">
        <first></first>
      </el-col>
      <el-col :span="24">
        <second></second>
      </el-col>
      <el-col :span="16">
        <thirdLeft></thirdLeft>
      </el-col>
      <el-col :span="8">
        <fourLeft></fourLeft>
      </el-col>
      <el-col :span="16">
        <thirdRight></thirdRight>
      </el-col>
      <el-col :span="8">
        <fourRight></fourRight>
      </el-col>
      <el-col :span="16">
        <fivethLeft></fivethLeft>
      </el-col>
      <el-col :span="8">
        <fivethRight></fivethRight>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import first from './components/first.vue'
  import second from './components/seconds.vue'
  import thirdLeft from './components/thirdLeft.vue'
  import thirdRight from './components/thirdRight.vue'
  import fourLeft from './components/fourLeft.vue'
  import fourRight from './components/fourRight.vue'
  import fivethLeft from './components/fivethLeft.vue'
  import fivethRight from './components/fiveRight.vue'

  export default {
    name: '',
    components: {
      first,
      second,
      thirdLeft,
      thirdRight,
      fourLeft,
      fourRight,
      fivethLeft,
      fivethRight,
    },
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
