<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="集团要闻" name="first">
        <div class="container">
          <div class="left">
            <el-carousel trigger="click" height="250px">
              <el-carousel-item v-for="item in 4" :key="item">
                <h3 class="small">{{ item }}</h3>
              </el-carousel-item>
            </el-carousel>
          </div>
          <div class="right">
            <div class="right-container">
              <div>
                •邀请函 | 云上春茗
                悦动未来——华南老客户春茗会（点击进入扫码获取福利）
              </div>
              <div class="right_detail">
                <div>邓姜慧子</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•金融知识普及月 金融知识进万家</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•蓝悦亮相第三十一届中国国际集团博览会</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•青春不打烊，赢战2019</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•青春不打烊，赢战2019</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•青春不打烊，赢战2019</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•青春不打烊，赢战2019</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•拥抱明天，共享未来</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>•拥抱明天，共享未来</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  export default {
    name: '222',
    data() {
      return {
        activeName: 'first',
      }
    },
  }
</script>

<style scoped lang="scss">
  .content {
    background-color: #fff;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }
  .container {
    display: flex;
  }
  .left {
    width: 35%;
    margin-bottom: 10px;
  }
  .right {
    flex: 1;
    margin-left: 10px;
  }
  .right-container {
    display: flex;
    justify-content: space-between;
    margin: 11px 0;
  }
  .right_detail {
    width: 22%;
    display: flex;
    justify-content: space-between;
  }
</style>
