<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="待办(99+)" name="first">
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
      </el-tab-pane>
      <el-tab-pane label="待阅(99+)" name="second">待阅</el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
  export default {
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
    height: 280px;
    position: relative;
  }
  .container {
    display: flex;
    padding: 0 15px 0 5px;
  }
  .left {
    width: 40%;
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
    &:nth-child(1) {
      .number {
        background: #df7c79;
        border: 1px solid #df7c79;
      }
    }
    &:nth-child(2) {
      .number {
        background: #eb9943;
        border: 1px solid #eb9943;
      }
    }
    &:nth-child(3) {
      .number {
        background: #f0bd4e;
        border: 1px solid #f0bd4e;
      }
    }
  }
  .right_detail {
    width: 17%;
    display: flex;
    justify-content: space-between;
  }
  .footer {
    display: flex;
    padding-bottom: 10px;
    justify-content: flex-end;
    position: absolute;
    bottom: 0;
    right: 0;
  }
  .but {
    width: 40px;
    height: 25px;
    border: 1px solid #d5d5d5;
    border-radius: 4px;
    text-align: center;
    line-height: 25px;
    margin-right: 10px;
    margin-top: 10px;
  }
  .number {
    display: inline-block;
    width: 15px;
    height: 15px;
    border: 1px solid #d8d8d8;
    border-radius: 3px;
    text-align: center;
    line-height: 13px;
    color: #fff;
    background: #d8d8d8;
    font-size: 10px;
  }
</style>
