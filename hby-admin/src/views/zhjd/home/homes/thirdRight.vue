<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="下属公司动态" name="first">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>
                <span class="number">1</span>
                培训管理制度
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">2</span>
                蓝悦晨早简报
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">3</span>
                蓝悦集团三年调整优化发展总结表彰大会隆重召开
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">4</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">5</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">6</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="会议纪要" name="second">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>
                <span class="number">1</span>
                测试
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">2</span>
                P1-指引-项目启动会议
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">3</span>
                公司总经办、销售部讨论会纪要
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">4</span>
                销售部门Q2会议纪要
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">5</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">6</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="资金周报" name="third">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>
                <span class="number">1</span>
                李文毅2023-01-15至2023-01-21周总结
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">2</span>
                李文毅2019-10-27至2019-11-02周总结
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">3</span>
                罗力2019-10-27至2019-11-02周总结
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">4</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">5</span>
                [集团动态] 金融知识普及月 金融知识进万家
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">6</span>
                [集团动态] 金融知识普及月 金融知识进万家
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
