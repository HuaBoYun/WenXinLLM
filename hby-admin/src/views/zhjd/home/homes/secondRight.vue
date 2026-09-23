<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="任免" name="first">
        <div class="title">本月（2.1 - 2.28）</div>
        <div class="list">
          <div class="list_content">
            <div>20</div>
            <div>在职</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>入职</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>转正</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>合同</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>离职</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>周年</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>生日</div>
          </div>
          <div class="list_content">
            <div>0</div>
            <div>调岗</div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="公示" name="second">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>有限合伙企业国有权益登记暂行规定</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>对政务部门发起督办</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>集团19年第十次财务月报</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>2019.10最新会议报告</div>
              <div class="right_detail">
                <div>李小五</div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>蓝悦新产品概念展示</div>
              <div class="right_detail">
                <div>李小五</div>
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
  }
  .right_detail {
    width: 25%;
    display: flex;
    justify-content: space-between;
  }
  .list {
    display: flex;
    justify-content: space-around;
  }
  .list_content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .title {
    padding: 20px 0 20px 20px;
    text-align: left;
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
</style>
