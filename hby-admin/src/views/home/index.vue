<template>
  <div class="index-container">
    <el-row :gutter="20">
      <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24">
        <icon-list />
      </el-col>
      <el-col :lg="8" :md="8" :sm="24" :xl="8" :xs="24">
        <order />
      </el-col>
      <el-col :lg="8" :md="8" :sm="24" :xl="8" :xs="24">
        <branch />
      </el-col>
      <el-col :lg="8" :md="8" :sm="24" :xl="8" :xs="24">
        <rank />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <trend />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <china-map />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import ChinaMap from './components/ChinaMap'
  import IconList from './components/IconList'
  import Branch from './components/Branch'
  import Trend from './components/Trend'
  import Rank from './components/Rank'
  import Order from './components/Order'

  export default {
    name: 'Index',
    components: {
      ChinaMap,
      IconList,
      Branch,
      Trend,
      Rank,
      Order,
    },
  }
</script>

<style lang="scss" scoped>
  .index-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .access,
      .authorization,
      .version-information {
        min-height: 268px;
      }

      .el-card {
        .el-card__header {
          position: relative;

          .card-header-tag {
            position: absolute;
            top: 15px;
            right: $base-margin;
          }

          > div > span {
            display: flex;
            align-items: center;

            i {
              margin-right: 3px;
            }
          }
        }

        .el-card__body {
          position: relative;
          .echarts {
            width: 100%;
            height: 127px;
          }

          .card-footer-tag {
            position: absolute;
            right: $base-margin;
            bottom: 15px;
          }
        }
      }

      .bottom {
        padding-top: 20px;
        margin-top: 5px;
        color: #595959;
        text-align: left;
        border-top: 1px solid $base-border-color;
      }
    }
  }
</style>
