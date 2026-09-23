<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="知识专栏" name="first">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>
                <span class="number">1</span>
                董事长参加中国工艺美术行业大会 并当选理事长
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">2</span>
                董事长：创新发展是集团永恒的主题
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">3</span>
                集团与广交集团就长大公司 合作项目成功签约
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">4</span>
                董事长率团访问香港
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">5</span>
                走向知识管理 共享协同市场
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">6</span>
                蓝悦集团以诚相待引得9000“候鸟”归
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="footer">
      <div class="but"><i class="el-icon-plus"></i></div>
      <div class="but"><i class="el-icon-more"></i></div>
    </div>
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
