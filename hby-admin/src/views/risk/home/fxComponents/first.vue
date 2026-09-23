<template>
  <div style="background-color: #f5f7f8">
    <el-card>
      <el-row :gutter="10"></el-row>
      <el-row :gutter="10">
        <el-col :span="6">
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ zs }}</div>
                <p>风险总数量</p>
              </div>
              <div>
                <i class="el-icon-money"></i>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ ysp }}</div>
                <p>已审批确认风险数量</p>
              </div>
              <div>
                <i class="el-icon-collection"></i>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ wsp }}</div>
                <p>未审批确认风险数量</p>
              </div>
              <div>
                <i class="el-icon-umbrella"></i>
              </div>
            </div>
          </el-col>
        </el-col>
        <el-col :span="12">
          <div style="border: 1px solid #d2d2d2">
            <chat11 :risks="risks"></chat11>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="border: 1px solid #d2d2d2">
            <chat17 :yiBan="yiBan" :zhongDa="zhongDa"></chat17>
          </div>
        </el-col>
        <!-- <el-col :span="6">
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ yiBan + zhongDa }}</div>
                <p>风险事件总数量</p>
              </div>
              <div>
                <i class="el-icon-umbrella"></i>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ yiBan }}</div>
                <p>一般风险事件数量</p>
              </div>
              <div>
                <i class="el-icon-umbrella"></i>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="one-item">
              <div>
                <div class="title">{{ zhongDa }}</div>
                <p>重大风险事件数量</p>
              </div>
              <div>
                <i class="el-icon-umbrella"></i>
              </div>
            </div>
          </el-col>
        </el-col> -->
      </el-row>
      <el-col :span="8">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat14></chat14>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat12></chat12>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat16></chat16>
        </div>
      </el-col>
      <el-col :span="24">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat15></chat15>
        </div>
      </el-col>
      <el-col :span="24">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat13></chat13>
        </div>
      </el-col>
      <el-col :span="24">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <chat18></chat18>
        </div>
      </el-col>
      <el-col :span="24">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <FxList></FxList>
        </div>
      </el-col>
      <el-col :span="24">
        <div style="border: 1px solid #d2d2d2; margin-bottom: 10px">
          <RiskTop10></RiskTop10>
        </div>
      </el-col>
    </el-card>
  </div>
</template>

<script>
  // 接口
  import {
    riskCatnameRisks,
    numberEventsRisks,
    riskNumbers,
  } from '@/api/risk/home.js'

  import chat11 from './echat11.vue'
  import chat17 from './echat17.vue'
  import chat12 from './echat12.vue'
  import chat13 from './echat14.vue'
  import chat14 from './echat14.vue'
  import chat15 from './echat15.vue'
  import chat16 from './echat16.vue'
  import chat18 from './echat18.vue'
  import FxList from './fxList.vue'
  import RiskTop10 from './RiskTop10.vue'

  export default {
    components: {
      chat11,
      chat17,
      chat12,
      chat13,
      chat14,
      chat15,
      chat18,
      chat16,
      FxList,
      RiskTop10,
    },
    data() {
      return {
        wsp: 0,
        ysp: 0,
        zs: 0,
        yiBan: 0,
        zhongDa: 0,
        orgid: '1000',
        risks: [],
      }
    },
    /**
     * @description: 初始化，调用接口，获取6个数量
     * @param {*}
     * @return {*}
     */
    created() {
      riskCatnameRisks().then((res) => {
        if (res && res.code == 1) {
          this.risks = res.data.data
          // 一般风险和最大风险
          // this.risksYiBan = res.data.risksYiBan || 0
          // this.riskszhongDa = res.data.riskszhongDa || 0
        }
      })
      numberEventsRisks().then((res) => {
        if (res && res.code == 1) {
          // 一般风险事件和最大风险事件
          this.yiBan = res.data.yiBan || 0
          this.zhongDa = res.data.zhongDa || 0
        }
      })
      riskNumbers().then((res) => {
        if (res && res.code == 1) {
          // 一般风险事件和最大风险事件
          this.wsp = res.data.wsp || 0
          this.ysp = res.data.ysp || 0
          this.zs = res.data.zs || 0
        }
      })
    },
  }
</script>

<style scoped lang="scss">
  .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 5px;
    margin-bottom: 10px;
    border: 1px solid #d2d2d2;
    .title {
      font-size: 24px;
      font-weight: 700;
    }
  }
  i {
    font-size: 24px;
  }
</style>
