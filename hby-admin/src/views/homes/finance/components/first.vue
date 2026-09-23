<template>
  <div class="system-log-container">
    <div class="content">
      <el-row :gutter="10">
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu101.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  368,800
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">收款金额</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu102.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  9809
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">合同支付</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu103.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  6,828
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">费用报销</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu104.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  1471.32
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">预算结余</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu105.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  52.32
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">现金结余</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="card-content">
              <div class="left">
                <img
                  src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/caiwu106.png"
                  alt=""
                />
              </div>
              <div class="right">
                <div class="top">
                  4
                  <span class="unit">万元</span>
                </div>
                <div class="bottom">人工成本</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  const img1 = require('@/assets/indexPage/report/sjzl/01.png')
  const img2 = require('@/assets/indexPage/report/sjzl/02.png')
  const img3 = require('@/assets/indexPage/report/sjzl/03.png')
  export default {
    name: 'Purchase',
    data() {
      return {
        img1: img1,
        img2: img2,
        img3: img3,
      }
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background-color: #f6f8f9;
  }
  .content {
    padding: 20px;
    background-color: #fff;
  }

  .card-content {
    display: flex;
    align-items: center;
    justify-content: space-around;
  }

  .card-content .left {
    margin-right: 10px;
    width: 20%;
  }

  .card-content .left img {
    width: 100%;
  }
  .card-content .right {
    flex: 1;
  }
  .card-content .right .top {
    font-size: 28px;
    color: #000;
    margin-bottom: 6px;
  }

  .card-content .right .bottom {
    font-size: 16px;
    color: #808080;
  }

  .card-content .right .unit {
    font-size: 14px;
    color: #808080;
  }
</style>
