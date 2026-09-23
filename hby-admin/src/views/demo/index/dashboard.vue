<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <trend />
      </el-col>
      <el-col :lg="6" :md="12" :sm="24" :xl="6" :xs="24">
        <branch />
      </el-col>
      <el-col :lg="6" :md="12" :sm="24" :xl="6" :xs="24">
        <rank />
      </el-col>
      <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24">
        <tabs />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Trend from './components/Trend'
  import Branch from './components/Branch'
  import Rank from './components/Rank'
  import Tabs from './components/Tabs'

  export default {
    name: 'Dashboard',
    components: { Trend, Branch, Rank, Tabs },
    data() {
      return {}
    },
    created() {},
    mounted() {},
    methods: {},
  }
</script>

<style lang="scss" scoped>
  .dashboard-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-card {
        height: 300px;

        [class*='-echart'] {
          width: 100%;
          height: 200px;
        }
      }
    }
  }
</style>
