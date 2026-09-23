<template>
  <el-card>
    <div class="title">预警提醒</div>
    <el-col :span="12">
      <div class="ti">单据填写平均耗时</div>
      <el-progress type="circle" :percentage="10" :format="show1"></el-progress>
    </el-col>
    <el-col :span="12">
      <div class="ti">业务审批评价耗时</div>
      <el-progress type="circle" :percentage="30" :format="show2"></el-progress>
    </el-col>
    <el-col :span="12" style="margin-top: 10px">
      <div class="ti">财务审批平均耗时</div>
      <el-progress type="circle" :percentage="15" :format="show3"></el-progress>
    </el-col>
    <el-col :span="12" style="margin-top: 10px">
      <div class="ti">财务核算评价耗时</div>
      <el-progress type="circle" :percentage="70" :format="show4"></el-progress>
    </el-col>
  </el-card>
</template>

<script>
  export default {
    methods: {
      show1() {
        return '19 秒/单'
      },
      show2() {
        return '30 秒/单'
      },
      show3() {
        return '15 秒/单'
      },
      show4() {
        return '70 秒/单'
      },
    },
  }
</script>

<style scoped lang="scss">
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 10px 0;
    margin-bottom: 10px;
  }
  .ti {
    margin-bottom: 5px;
  }
</style>
