<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="集团简报" name="first">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>• [集团动态] 123</div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>• [集团动态] 金融知识普及月 金融知识进万家</div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>• [集团动态] 金融知识普及月 金融知识进万家</div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>• [集团动态] 金融知识普及月 金融知识进万家</div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>• [集团动态] 金融知识普及月 金融知识进万家</div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>• [集团动态] 金融知识普及月 金融知识进万家</div>
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
</style>
