<template>
  <div class="content">
    <el-tabs v-model="activeName">
      <el-tab-pane label="标杆研究" name="first">
        <div class="container">
          <div class="right">
            <div class="right-container">
              <div>
                <span class="number">1</span>
                办公室党支部开展 “铭记历史，不忘初心”主题党日活动
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">2</span>
                国史讲堂：新中国70年社会主义制度成就
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">3</span>
                全国劳动模范和先进工作者表彰大会隆重举行
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div class="container_left">
                <span class="number">4</span>
                <span class="pp">
                  习近平在中央全面依法治国工作会议上强调
                  坚定不移走中国特色社会主义法治道路
                </span>
              </div>
              <div class="container_right">
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">5</span>
                开展“关爱健康·共建美好家园”志愿服务活动
              </div>
              <div>
                <div>2022-02-28</div>
              </div>
            </div>
            <div class="right-container">
              <div>
                <span class="number">6</span>
                缅怀革命先烈 传承不朽精神
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
    .container_left {
      width: 75%;
      display: flex;
    }
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
  .pp {
    display: inline-block;
    overflow: hidden; // 文字超长隐藏
    text-overflow: ellipsis; // 显示...
    white-space: nowrap; // 单行显示
    width: 100%;
    margin-left: 5px;
  }
</style>
