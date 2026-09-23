<template>
  <div style="background: #f7f7f7">
    <el-row :gutter="20">
      <el-col :xs="12" :sm="12" :lg="{ span: '4-8' }" class="card-panel-col">
        <el-card>
          <div class="headCard">
            <div class="cardIcon1"></div>
            <div class="cardTitle">
              <div class="cardTitleText">全部</div>
              <div class="cardTitleNum">605</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="{ span: '4-8' }" class="card-panel-col">
        <el-card>
          <div class="headCard">
            <div class="cardIcon2"></div>
            <div class="cardTitle">
              <div class="cardTitleText">我的草稿</div>
              <div class="cardTitleNum">36</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="{ span: '4-8' }" class="card-panel-col">
        <el-card>
          <div class="headCard">
            <div class="cardIcon3"></div>
            <div class="cardTitle">
              <div class="cardTitleText">待审中</div>
              <div class="cardTitleNum">144</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="{ span: '4-8' }" class="card-panel-col">
        <el-card>
          <div class="headCard">
            <div class="cardIcon4"></div>
            <div class="cardTitle">
              <div class="cardTitleText">驳回</div>
              <div class="cardTitleNum">7</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="{ span: '4-8' }" class="card-panel-col">
        <el-card>
          <div class="headCard">
            <div class="cardIcon5"></div>
            <div class="cardTitle">
              <div class="cardTitleText">结束</div>
              <div class="cardTitleNum">158</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card>
          <div class="secondTitle">最近使用流程</div>
          <el-divider></el-divider>
          <div class="secondView">
            <div class="viewCard">
              <div class="viewCardTitle">员工请假审批流程</div>
              <div class="viewCardText">考勤管理</div>
            </div>
            <div class="viewCard">
              <div class="viewCardTitle">固定资产延期验收说明</div>
              <div class="viewCardText">固定资产管理</div>
            </div>
            <div class="viewCard">
              <div class="viewCardTitle">公司印章签批流程</div>
              <div class="viewCardText">印章管理</div>
            </div>
            <div class="viewCard">
              <div class="viewCardTitle">新员工入职申请流程</div>
              <div class="viewCardText">新员工入职管理</div>
            </div>
            <div class="viewCard">
              <div class="viewCardTitle">订单确认流程</div>
              <div class="viewCardText">ERP集成</div>
            </div>
            <div class="viewCard">
              <div class="viewCardTitle">集成项目立项评审..</div>
              <div class="viewCardText">生产制造管理</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <el-col :span="24">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="流程概览" name="first">
            <el-row>
              <el-col :span="24">
                <div class="searchView">
                  <input
                    type="text"
                    placeholder="搜索流程"
                    class="search_input"
                  />
                  <div class="search_btn search_icon"></div>
                </div>
              </el-col>
            </el-row>
            <div class="viewBody">
              <el-row :gutter="10">
                <el-col :span="4">
                  <div class="bodyNav">
                    <div
                      class="navLi"
                      :class="index == navSelect ? 'navLiSelects' : ''"
                      v-for="(item, index) in navList"
                      :key="index"
                      @click="setNalValue(index)"
                    >
                      <div
                        :class="index == navSelect ? 'navLiSelect' : 'navLiDot'"
                      ></div>
                      {{ item }}
                    </div>
                  </div>
                </el-col>
                <el-col :span="20">
                  <div class="bodyMenu">
                    <div v-for="(item, index) in menuList" :key="index">
                      <div
                        class="menuHead"
                        :class="'headColor' + ((index % 5) + 1)"
                      >
                        <div
                          class="menuTitle"
                          :class="'menuColor' + ((index % 5) + 1)"
                        >
                          {{ item.tab }}
                        </div>
                      </div>
                      <div class="menuBody">
                        <div
                          class="menuCate"
                          v-for="(item2, index2) in item.list"
                          :key="index2"
                        >
                          <div
                            class="menuCateTitle"
                            :class="'menuTextColor' + ((index % 5) + 1)"
                          >
                            {{ item2.title }}
                          </div>
                          <div
                            class="menuCateList"
                            v-for="(item3, index3) in item2.list"
                            :key="index3"
                          >
                            <div class="menuCateIcon"></div>
                            {{ item3.title }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的收藏" name="second">配置管理</el-tab-pane>
        </el-tabs>
      </el-col>
    </el-card>
  </div>
</template>
<script>
  export default {
    name: 'lcgl',
    data() {
      return {
        activeName: 'first',
        navList: [
          '演示流程',
          '人事管理',
          '财务管理',
          '客户管理',
          '产品相关',
          'IT管理',
          '行政管理',
          '营销管理',
          '综合管理',
          '其他流程',
          '【制造】采购管理',
          '【制造】流程与信息化',
          '【制造】线索到回款',
          '项目演示专用',
          'OPS流程',
        ],
        navSelect: 0,
        menuList: [
          {
            tab: '演示流程 ',
            list: [
              {
                title: 'ERP集成',
                list: [
                  {
                    title: '订单确认流程',
                  },
                  {
                    title: '送货流程',
                  },
                ],
              },
              {
                title: '　　　',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
            ],
          },
          {
            tab: '人事管理',
            list: [
              {
                title: '新员工入职管理',
                list: [
                  {
                    title: '新员工入职申请流程',
                  },
                  {
                    title: '员工住宿、工卡、座位申请流程',
                  },
                ],
              },
              {
                title: '转正管理',
                list: [
                  {
                    title: '员工申请流程',
                  },
                ],
              },
              {
                title: '人事调整管理',
                list: [
                  { title: '外派人员申请流程' },
                  { title: '员工内部调岗申请流程' },
                  { title: '员工异动申请流程' },
                  { title: '外派人员申请流程' },
                  { title: '外派人员申请流程' },
                ],
              },
            ],
          },
          {
            tab: '财务管理',
            list: [
              {
                title: '财务管理',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
              {
                title: '固定资产管理',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
            ],
          },
          {
            tab: '客户管理',
            list: [
              {
                title: '客户管理',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
            ],
          },
          {
            tab: '产品相关',
            list: [
              {
                title: '产品相关',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
            ],
          },
          {
            tab: 'IT管理',
            list: [
              {
                title: 'IT管理',
                list: [
                  {
                    title: '请假申请',
                  },
                  {
                    title: '报销申请',
                  },
                  {
                    title: '合同起草',
                  },
                ],
              },
            ],
          },
        ],
      }
    },
    methods: {
      handleClick(tab, event) {},
      setNalValue(index) {
        this.navSelect = index
      },
    },
    mounted() {},
  }
</script>
<style lang="scss" scoped>
  .el-row {
    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-col {
    border-radius: 4px;
  }

  .el-col-lg-4-8 {
    width: 20%;
  }

  .headCard {
    display: flex;
    justify-content: space-between;
    padding-left: 10px;
    padding-right: 10px;

    .cardIcon1 {
      width: 45px;
      height: 45px;
      background: url('../../../assets/personal/icon_01.png');
      background-size: 100%;
    }

    .cardIcon2 {
      width: 45px;
      height: 45px;
      background: url('../../../assets/personal/icon_02.png');
      background-size: 100%;
    }

    .cardIcon3 {
      width: 45px;
      height: 45px;
      background: url('../../../assets/personal/icon_03.png');
      background-size: 100%;
    }

    .cardIcon4 {
      width: 45px;
      height: 45px;
      background: url('../../../assets/personal/icon_04.png');
      background-size: 100%;
    }

    .cardIcon5 {
      width: 45px;
      height: 45px;
      background: url('../../../assets/personal/icon_05.png');
      background-size: 100%;
    }

    .cardTitle {
      height: 45px;

      .cardTitleText {
        color: #1989fa;
        text-align: right;
      }

      .cardTitleNum {
        font-size: 24px;
        font-weight: 500;
        text-align: right;
      }
    }
  }

  .secondTitle {
    color: #1989fa;
    font-size: 18px;
  }

  .secondView {
    display: flex;

    .viewCard {
      position: relative;
      display: inline-block;
      width: 193px;
      height: 78px;
      background-color: #edf3ff;
      font-size: 12px;
      margin: 10px 20px 10px 6px;
      padding: 0 14px 0 20px;
      min-height: 32px;
      border-top: 1px solid #b8d1fa;
      border-bottom: 1px solid #b8d1fa;
      border-left: 3px solid #4285f4;
      border-right: none;
      cursor: pointer;
      vertical-align: top;

      .viewCardTitle {
        font-size: 15px;
        font-weight: 500;
        margin-top: 15px;
        overflow: hidden;
        white-space: nowrap; //内容超出不换行
        text-overflow: ellipsis;
      }

      .viewCardText {
        font-size: 13px;
        color: #4285f4;
        margin-top: 8px;
      }
    }

    .viewCard::before {
      content: '';
      display: inline-block;
      width: 14px;
      height: 65px;
      position: absolute;
      top: -1px;
      right: -13px;
      border-top: 1px solid #b8d1fa;
      border-bottom: none;
      border-right: 1px solid #b8d1fa;
      border-left: none;
      border-top-right-radius: 4px;
      background-color: #edf3ff;
    }

    .viewCard::after {
      content: '';
      display: inline-block;
      width: 14px;
      height: 14px;
      position: absolute;
      bottom: -1px;
      right: -14px;
      background-image: url('../../../assets/personal/img_jiaobiao.png');
      background-size: contain;
      background-repeat: no-repeat;
      background-position: top left;
    }
  }

  .searchView {
    margin-top: 5px;
    margin-bottom: 10px;

    .search_input {
      display: inline-block;
      border-top: 1px solid #eee;
      border-right: none;
      border-bottom: 1px solid #eee;
      border-left: 1px solid #eee;
      border-top-left-radius: 3px;
      border-bottom-left-radius: 3px;
      width: 240px;
      height: 32px;
      background-color: #fff;
      padding: 0 6px;
      vertical-align: middle;
    }

    .search_btn {
      width: 48px;
      height: 32px;
      border: 1px solid #4285f4;
      background-color: #4285f4;
      display: inline-block;
      vertical-align: middle;
      border-top-right-radius: 3px;
      border-bottom-right-radius: 3px;
      background-size: 36%;
      background-repeat: no-repeat;
      background-position: center;
      cursor: pointer;
    }

    .search_icon {
      background-image: url('../../../assets/personal/profile_navTop_ftsearch.png');
    }
  }

  .viewBody {
    background-color: #f6f7fa;
    padding-top: 2px;
    margin-bottom: 20px;

    .bodyNav {
      background-color: #fff;
      padding-left: 6px;

      .navLi {
        color: #333;
        font-size: 14px;
        line-height: 40px;
        height: 40px;
        cursor: pointer;

        .navLiSelect {
          content: '';
          display: inline-block;
          width: 10px;
          height: 12px;
          margin-right: 4px;
          background-image: url('../../../assets/personal/icon_Process.png');
          z-index: 10;
          background-repeat: no-repeat;
          background-size: contain;
          background-position: center;
        }

        .navLiDot {
          content: '';
          display: inline-block;
          width: 4px;
          height: 4px;
          margin-right: 8px;
          background-color: #ccc;
          margin-top: 18px;
          border-radius: 2px;
          z-index: 9;
        }
      }

      .navLiSelects {
        font-weight: 600;
        color: #1989fa;
      }
    }
  }

  .bodyMenu {
    background-color: #fff;
    .menuHead {
      margin-left: 15px;
      cursor: pointer;

      .menuTitle {
        display: inline-block;
        color: #fff;
        line-height: 36px;
        padding: 0 16px;
        /* font-size: 16px; */
        font-size: 14px;
        border-radius: 2px 2px 0px 0px;
      }

      .menuColor1 {
        background-color: #4285f4;
      }

      .menuColor2 {
        background-color: #22b77d;
      }

      .menuColor3 {
        background-color: #816fe1;
      }

      .menuColor4 {
        background-color: #5d62a8;
      }
      .menuColor5 {
        background-color: #5482b6;
      }
    }

    .headColor1 {
      background-color: #eaf0fb;
    }
    .headColor2 {
      background-color: #e8f4f1;
    }
    .headColor3 {
      background-color: #f2f0fb;
    }
    .headColor4 {
      background-color: #ecedf5;
    }
    .headColor5 {
      background-color: #eceff6;
    }

    .menuBody {
      background-color: #fafafc;
      /* border: 1px solid #F1F1F1; */
      padding: 6px 0 6px 0;
      min-height: 34px;

      .menuCate {
        margin: 14px 30px 6px 30px;

        .menuTextColor1 {
          color: #4285f4;
        }
        .menuTextColor2 {
          color: #22b77d;
        }
        .menuTextColor3 {
          color: #816fe1;
        }
        .menuTextColor4 {
          color: #5d62a8;
        }
        .menuTextColor5 {
          color: #5482b6;
        }

        .menuCateTitle {
          height: 22px;
          font-size: 16px;
          line-height: 22px;
        }

        .menuCateList {
          display: inline-block;
          width: 45%;
          vertical-align: top;
          color: #443c3c;
          line-height: 30px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;

          .menuCateIcon {
            content: '';
            display: inline-block;
            width: 13px;
            height: 15px;
            background-image: url('../../../assets/personal/icon_liuche.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center top;
            vertical-align: middle;
            margin-right: 4px;
          }
        }
      }
    }
  }
</style>
