<template>
  <div class="index-container">
    <el-row :gutter="10">
      <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24">
        <first></first>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <secondLeft></secondLeft>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <secondRight></secondRight>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <thirdLeft></thirdLeft>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <thirdRight></thirdRight>
      </el-col>
      <el-col
        :lg="24"
        :md="24"
        :sm="24"
        :xl="24"
        :xs="24"
        style="margin-top: 10px"
      >
        <fourth></fourth>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <fifthLeft></fifthLeft>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <fifthRight></fifthRight>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <sixthLeft></sixthLeft>
      </el-col>
      <el-col
        :lg="12"
        :md="12"
        :sm="12"
        :xl="12"
        :xs="12"
        style="margin-top: 10px"
      >
        <sixthRight></sixthRight>
      </el-col>
      <el-col
        :lg="24"
        :md="24"
        :sm="24"
        :xl="24"
        :xs="24"
        style="margin-top: 10px"
      >
        <seventh></seventh>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import first from './homes/first.vue'
  import secondLeft from './homes/secondLeft.vue'
  import secondRight from './homes/secondRight.vue'
  import thirdLeft from './homes/thirdLeft.vue'
  import thirdRight from './homes/thirdRight.vue'
  import fourth from './homes/fourth.vue'
  import fifthLeft from './homes/fifthLeft.vue'
  import fifthRight from './homes/fifthRight.vue'
  import sixthLeft from './homes/sixthLeft.vue'
  import sixthRight from './homes/sixthRight.vue'
  import seventh from './homes/seventh.vue'
  export default {
    name: '222',
    components: {
      first,
      secondLeft,
      secondRight,
      thirdLeft,
      thirdRight,
      fourth,
      fifthLeft,
      fifthRight,
      sixthLeft,
      sixthRight,
      seventh,
    },
    data() {
      return {
        activeName: 'first',
      }
    },
  }
</script>

<style scoped lang="scss">
  .index-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
