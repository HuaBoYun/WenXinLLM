<template>
  <div class="index-container">
    <el-row :gutter="20">
      <!-- <el-col :lg="24" :md="24" :sm="24" :xl="24" :xs="24"></el-col> -->
      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <Messages />
      </el-col>
      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <Pie1 />
      </el-col>
      <el-col :lg="8" :md="8" :sm="8" :xl="8" :xs="24">
        <Pie2 />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line1 />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line2 />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line3 />
      </el-col>
      <el-col :lg="12" :md="12" :sm="24" :xl="12" :xs="24">
        <Line4 />
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import Branch from '@/views/fwgl/home/components/Branch'
  import ChinaMap from '@/views/fwgl/home/components/ChinaMap'
  import IconList from '@/views/fwgl/home/components/IconList'
  import Order from '@/views/fwgl/home/components/Order'
  import Rank from '@/views/fwgl/home/components/Rank'
  import Trend from '@/views/fwgl/home/components/Trend'
  import First from '@/views/fwgl/home/components/First'
  import Second from '@/views/fwgl/home/components/Second'
  import Three from '@/views/fwgl/home/components/Three'
  import TGL from '@/views/fwgl/home/components/TGL'
  import Pie1 from '@/views/fwgl/home/components/Pie1'
  import Pie2 from '@/views/fwgl/home/components/Pie2'
  import Messages from '@/views/fwgl/home/components/Message'
  import Line1 from '@/views/fwgl/home/components/Line1'
  import Line2 from '@/views/fwgl/home/components/Line2'
  import Line3 from '@/views/fwgl/home/components/Line3'
  import Line4 from '@/views/fwgl/home/components/Line4'

  export default {
    name: 'analysis',
    components: {
      Pie1,
      Pie2,
      Line1,
      Line2,
      Line3,
      Line4,
      Messages,
      First,
      Second,
      Three,
      TGL,
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
