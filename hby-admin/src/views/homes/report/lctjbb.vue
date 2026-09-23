<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <el-row>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor1">
              <img src="@/assets/personal/whole_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">所有流程</div>
              <div class="textCount">79</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor2">
              <img src="@/assets/personal/run_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">运行时流程</div>
              <div class="textCount">1</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor3">
              <img src="@/assets/personal/pause_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">暂停的流程</div>
              <div class="textCount">0</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor4">
              <img src="@/assets/personal/restart_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">重启过的流程</div>
              <div class="textCount">7</div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row style="margin-top: 60px">
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor5">
              <img src="@/assets/personal/expired_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">待审超时的流程</div>
              <div class="textCount">0</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor6">
              <img src="@/assets/personal/error_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">异常的流程</div>
              <div class="textCount">11</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor7">
              <img src="@/assets/personal/notvalid_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">处理人无效流程</div>
              <div class="textCount">1</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="tabCard">
            <div class="itemIcon iconColor8">
              <img src="@/assets/personal/status_error_flow.png" />
            </div>
            <div class="tabText">
              <div class="textTitle">流程队列</div>
              <div class="textCount">0</div>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="6">
          <Pie1 />
        </el-col>
        <el-col :span="6">
          <Pie2 />
        </el-col>
        <el-col :span="6">
          <Pie3 />
        </el-col>
        <el-col :span="6">
          <Pie4 />
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <Line5 />
        </el-col>
        <el-col :span="12">
          <Line6 />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import Pie1 from './components/Pie1'
import Pie2 from './components/Pie2'
import Pie3 from './components/Pie3'
import Pie4 from './components/Pie4'
import Line5 from './components/Line5'
import Line6 from './components/Line6'
export default {
  components: {
    Pie1,
    Pie2,
    Pie3,
    Pie4,
    Line5,
    Line6,
  },
  data() {
    return {}
  },
  methods: {},
}
</script>
<style scoped lang="scss">
.system-log-container {
  background: #f6f8f9 !important;
  padding: 0 !important;
}
.secondCard {
  margin-top: -5px !important;
  padding-top: 10px;
}
.pagination {
  margin-bottom: 20px !important;
}

.tabCard {
  width: 195px;
  min-height: 84px;
  margin: 0 auto;
  background-color: #ffffff;
  box-shadow: 0px 1px 3px #c7c6c6;
  -webkit-box-shadow: 0px 1px 3px #c7c6c6;
  box-sizing: border-box;
  border-radius: 3px;
  -webkit-border-radius: 3px;
  display: flex;

  .itemIcon {
    width: 60px;
    height: 60px;
    position: relative;
    top: -10px;
    left: 10px;

    img {
      width: 100%;
      position: absolute;
      top: 0;
      left: 0;
      border-radius: 5px;
    }
  }

  .iconColor1 {
    background-color: #f8ca43;
    box-shadow: 0px 0px 8px 0px #f8ca43;
  }

  .iconColor2 {
    background-color: #e34e4b;
    box-shadow: 0px 0px 8px 0px #e34e4b;
  }

  .iconColor3 {
    background-color: #69c381;
    box-shadow: 0px 0px 8px 0px #69c381;
  }

  .iconColor4 {
    background-color: #6f9be5;
    box-shadow: 0px 0px 8px 0px #6f9be5;
  }

  .iconColor5 {
    background-color: #75d0f2;
    box-shadow: 0px 0px 8px 0px #75d0f2;
  }

  .iconColor6 {
    background-color: #ec73c1;
    box-shadow: 0px 0px 8px 0px #ec73c1;
  }

  .iconColor7 {
    background-color: #e380e7;
    box-shadow: 0px 0px 8px 0px #e380e7;
  }

  .iconColor8 {
    background-color: #4bddb4;
    box-shadow: 0px 0px 8px 0px #4bddb4;
  }

  .tabText {
    max-width: 50%;
    width: 50%;
    position: relative;
    left: 15%;
    vertical-align: middle;

    .textTitle {
      font-size: 13px;
      margin-bottom: 5px;
      color: #999999;
      margin-top: 20px;
    }

    .textCount {
      font-size: 25px;
      font-weight: bold;
    }
  }
}
</style>