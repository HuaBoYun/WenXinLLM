<template>
  <div class="index-container">
    <el-card style="margin-bottom: 10px">
      <div class="title-text">智能法务管理驾驶舱</div>
    </el-card>
    <el-row :gutter="10">
      <!-- <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24"></el-col> -->
      <!-- 切换法务模块默认进入报表驾驶舱，不预加载会议/学习/公告列表；进入对应菜单时再调用列表接口。 -->
      <!-- <el-col :lg="24" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card
          style="width: 100%; height: 400px; padding: 10px; margin-bottom: 10px"
        >
          <Messages style="height: 340px; margin-bottom: 20px" />
        </el-card>
      </el-col>
      <el-col :lg="24" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card
          style="width: 100%; height: 400px; padding: 10px; margin-bottom: 10px"
        >
          <Xxyd style="height: 300px; margin-bottom: 20px" />
        </el-card>
      </el-col>
      <el-col :lg="24" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card
          style="width: 100%; height: 400px; padding: 10px; margin-bottom: 10px"
        >
          <Tzgg style="height: 300px; margin-bottom: 20px" />
        </el-card>
      </el-col> -->

      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <histogram1 />
        </el-card>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <histogram2 />
        </el-card>
      </el-col>

      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card style="width: 100%; height: 360px; margin-bottom: 10px">
          <Pie1 />
        </el-card>
      </el-col>
      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card style="width: 100%; height: 360px; margin-bottom: 10px">
          <Pie2 />
        </el-card>
      </el-col>
      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <el-card style="width: 100%; height: 360px; margin-bottom: 10px">
          <Pie3 />
        </el-card>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <histogram3 />
        </el-card>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <histogram4 />
        </el-card>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <Line4 />
        </el-card>
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <el-card
          style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
        >
          <Line3 />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  // import Branch from './components/Branch'
  // import ChinaMap from './components/ChinaMap'
  // import First from './components/First'
  // import IconList from './components/IconList'
  import histogram1 from './components/histogram1'
  import histogram2 from './components/histogram2'
  import histogram3 from './components/histogram3'
  import histogram4 from './components/histogram4'
  // import Line1 from './components/Line1'
  // import Line2 from './components/Line2'
  import Line3 from './components/Line3'
  import Line4 from './components/Line4'
  // import Messages from './components/Message'
  // import Order from './components/Order'
  import Pie1 from './components/Pie1'
  import Pie2 from './components/Pie2'
  import Pie3 from './components/Pie3'
  // import Rank from './components/Rank'
  // import Second from './components/Second'
  // import TGL from './components/TGL'
  // import Three from './components/Three'
  // import Trend from './components/Trend'
  // import Xxyd from './components/Xxyd'
  // import Tzgg from './components/tzgg'

  export default {
    name: 'Index',
    components: {
      Pie1,
      Pie2,
      Pie3,
      // Line1,
      // Line2,
      Line3,
      Line4,
      // Messages,
      // First,
      // Second,
      // Three,
      // TGL,
      // ChinaMap,
      // IconList,
      // Branch,
      // Trend,
      // Rank,
      // Order,
      // Xxyd,
      // Tzgg,
      histogram1,
      histogram2,
      histogram3,
      histogram4,
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
  .title-text {
    font-size: 26px;
    text-align: center;
    color: #ff8c00;
    font-weight: bold;
  }
</style>
