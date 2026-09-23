<template>
  <div class="order">
    <el-card class="order-card1" shadow="hover">
      <template #header>
        <vab-icon icon="bar-chart-fill" />
        数据统计
      </template>
      <el-row class="order-card1-content">
        <el-col :span="8">
          <p>签单数量</p>
          <h1>
            <vab-count
              :decimals="countConfig.decimals"
              :duration="countConfig.duration"
              :end-val="countConfig.endVal"
              :prefix="countConfig.prefix"
              :separator="countConfig.separator"
              :start-val="countConfig.startVal"
              :suffix="countConfig.suffix"
            />
          </h1>
        </el-col>
        <el-col :span="8">
          <p>合同数量</p>
          <h1>
            <vab-count
              :decimals="countConfig.decimals"
              :duration="countConfig.duration"
              :end-val="countConfig.endVal"
              :prefix="countConfig.prefix"
              :separator="countConfig.separator"
              :start-val="countConfig.startVal"
              :suffix="countConfig.suffix"
            />
          </h1>
        </el-col>
        <el-col :span="8">
          <p>好评度</p>
          <h1>99%</h1>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
  import VabCount from '@/extra/VabCount'
  import _ from 'lodash'

  export default {
    components: { VabCount },
    data() {
      return {
        countConfig: {
          startVal: 0,
          endVal: _.random(1000, 6000),
          decimals: 0,
          prefix: '',
          suffix: '',
          separator: ',',
          duration: 5000,
        },
      }
    },
  }
</script>

<style lang="scss" scoped>
  .order {
    &-card1 {
      height: 224px;
      background: linear-gradient(to right, #60b2fb, #6485f6);

      &-content {
        text-align: center;
      }

      ::v-deep {
        .el-card {
          &__header,
          &__body {
            color: $base-color-white !important;
          }
        }
      }
    }
    margin-bottom: $base-margin;
  }
</style>
