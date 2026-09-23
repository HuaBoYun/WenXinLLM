<template>
  <div class="index-container">
    <el-row :gutter="20">
      <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24">
        <!-- <icon-list /> -->

        <!-- aaa -->
        <roll />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24" class="magtop">
        <!-- <order /> -->

        <div class="title">
          <card-title first="公司公告" second="项目公告" class="card" />
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24" class="magtop">
        <!-- <branch /> -->

        <div class="title">
          <card-title first="最新规范制度" second="" class="card" />
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24" class="magtop">
        <!-- <rank /> -->

        <div class="title">
          <card-title first="员工风采" second="员工活动" class="card" />
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24" class="magtop">
        <!-- <trend /> -->

        <div class="title">
          <card-title first="最新帖子" second="" class="card" />
        </div>
      </el-col>
      <!-- <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <china-map />
      </el-col> -->
    </el-row>
  </div>
</template>

<script>
  import Roll from './components/Roll'
  import CardTitle from './components/CardTitle'
  // import Branch from './components/Branch'
  // import Trend from './components/Trend'
  // import Rank from './components/Rank'
  // import Order from './components/Order'

  export default {
    name: 'Index',
    components: { Roll, CardTitle },
    // components: {
    //   ChinaMap,
    //   IconList,
    //   Branch,
    //   Trend,
    //   Rank,
    //   Order,
    // },
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

      .magtop {
        margin-top: 10px;
      }
    }
  }
</style>
