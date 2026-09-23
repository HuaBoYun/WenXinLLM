<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="流程分析" name="a">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon1 icon1"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">待办最多的流程</div>
                  <div class="tabTitle2">员工请假申请流程</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon2 icon2"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">超时处理最多流程</div>
                  <div class="tabTitle2">员工转正申请流程</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon3 icon3"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">创建量最高的流程</div>
                  <div class="tabTitle2">员工请假申请流程</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon4 icon4"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">平均审批耗时最长的流程</div>
                  <div class="tabTitle2">集团会议</div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <Line1 />
            </el-col>
            <el-col :span="12">
              <Line2 />
            </el-col>
            <el-col :span="12">
              <Line3 />
            </el-col>
            <el-col :span="12">
              <Line4 />
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="节点分析" name="b">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon5 icon1"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">节点平均耗时蓝榜</div>
                  <div class="tabTitle2">审批节点</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon6 icon5"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">驳回次数最多的节点</div>
                  <div class="tabTitle2">起草人</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon7 icon3"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">超时跳过次数最多的节点</div>
                  <div class="tabTitle2">导师意见审核</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon8 icon4"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">待审流程最多的节点</div>
                  <div class="tabTitle2">薪酬专员记录办理</div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <Line5 />
            </el-col>
            <el-col :span="12">
              <Line6 />
            </el-col>
            <el-col :span="12">
              <Line7 />
            </el-col>
            <el-col :span="12">
              <Line8 />
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="人员分析" name="c">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon9 icon1"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">待处理流程最多的员工</div>
                  <div class="tabTitle2">
                    张孝昆(星光标准产品_集团总部_企业大学_培训部)
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon10 icon5"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">被催办最多的员工</div>
                  <div class="tabTitle2">
                    张孝昆(星光标准产品_集团总部_企业大学_培训部)
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon11 icon3"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">超时跳过最多次的员工</div>
                  <div class="tabTitle2">
                    张孝昆(星光标准产品_集团总部_企业大学_培训部)
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="tabCard">
                <div class="magnetIcon tabIcon12 icon4"></div>
                <div class="tabTitle">
                  <div class="tabTitle1">超时转办次数最多的员工</div>
                  <div class="tabTitle2">
                    张孝昆(星光标准产品_集团总部_企业大学_培训部)
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <Line9 />
            </el-col>
            <el-col :span="12">
              <Line10 />
            </el-col>
            <el-col :span="12">
              <Line11 />
            </el-col>
            <el-col :span="12">
              <Line12 />
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
  import Line1 from './components/Line1'
  import Line2 from './components/Line2'
  import Line3 from './components/Line3'
  import Line4 from './components/Line4'
  import Line5 from './components/Line5'
  import Line6 from './components/Line6'
  import Line7 from './components/Line7'
  import Line8 from './components/Line8'
  import Line9 from './components/Line9'
  import Line10 from './components/Line10'
  import Line11 from './components/Line11'
  import Line12 from './components/Line12'
  export default {
    components: {
      Line1,
      Line2,
      Line3,
      Line4,
      Line5,
      Line6,
      Line7,
      Line8,
      Line9,
      Line10,
      Line11,
      Line12,
    },
    data() {
      return {
        activeName: 'a',
        a: '',
      }
    },
    methods: {
      handleClick(tab, event) {},
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }

  .tabCard {
    margin-bottom: 30px;
    display: flex;

    .tabIcon1 {
      background: url('../../../assets/personal/icon-1.png') no-repeat;
    }
    .tabIcon2 {
      background: url('../../../assets/personal/icon-2.png') no-repeat;
    }
    .tabIcon3 {
      background: url('../../../assets/personal/icon-3.png') no-repeat;
    }
    .tabIcon4 {
      background: url('../../../assets/personal/icon-4.png') no-repeat;
    }
    .tabIcon5 {
      background: url('../../../assets/personal/icon-5.png') no-repeat;
    }
    .tabIcon6 {
      background: url('../../../assets/personal/icon-6.png') no-repeat;
    }
    .tabIcon7 {
      background: url('../../../assets/personal/icon-7.png') no-repeat;
    }
    .tabIcon8 {
      background: url('../../../assets/personal/icon-8.png') no-repeat;
    }
    .tabIcon9 {
      background: url('../../../assets/personal/icon-9.png') no-repeat;
    }
    .tabIcon10 {
      background: url('../../../assets/personal/icon-10.png') no-repeat;
    }
    .tabIcon11 {
      background: url('../../../assets/personal/icon-11.png') no-repeat;
    }
    .tabIcon12 {
      background: url('../../../assets/personal/icon-12.png') no-repeat;
    }
    .magnetIcon {
      width: 56px;
      height: 56px;
      line-height: 56px;
      text-align: center;
      border-radius: 50%;
      background-repeat: no-repeat;
      background-position: 50%;
      background-color: #f8f8f8;
      background-size: 48px auto;
      display: inline-block;
    }

    .icon1 {
      background-color: #42c6f5;
    }
    .icon2 {
      background-color: #4285f4;
    }
    .icon3 {
      background-color: #4e42f5;
    }
    .icon4 {
      background-color: #f5b842;
    }
    .icon5 {
      background-color: #f58e42;
    }

    .tabTitle {
      margin-left: 10px;
      line-height: 23px;
      .tabTitle1 {
        margin-top: 8px;
        // margin-bottom: 3px;
        color: #333;
        font-weight: bold;
      }

      .tabTitle2 {
        color: #999;
        font-size: 12px;
      }
    }
  }
</style>
