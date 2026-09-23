<template>
  <div style="background: #f7f7f7">
    <div class="headAll">
      <div class="headList">
        <div
          v-for="(item, index) in list"
          :key="index"
          :class="order == index ? 'selectIndex cardIndex' : 'cardIndex'"
          @click="setIndex(index)"
        >
          {{ item }}
        </div>
      </div>

      <div class="headBody">
        <div class="bodyCard" v-for="(item, index) in tabData">
          <div class="cardHead">{{ item.title }}</div>
          <div
            v-for="(i, index) in item.cardList"
            :key="index"
            class="cardIndex"
          >
            {{ i }}
          </div>
        </div>
      </div>
    </div>

    <el-row>
      <el-col :span="16">
        <div class="tabHead">
          <div
            class="tabIndex"
            v-for="(item, index) in tabList"
            :key="item + '-' + index"
            :class="index == tabIndex ? 'tabSelect' : ''"
            @click="setTabIndex(index)"
          >
            {{ item }}
          </div>
        </div>
        <div style="margin-top: 20px; margin-right: 20px; margin-bottom: 20px">
          <el-card>
            <div class="tab1CardTitle">
              <div class="tab1Title1"></div>
              <div class="tab1Title2">
                您有
                <span class="titleSpan">62</span>
                条
              </div>
              <div class="tab1Title3">需要处理待办</div>
              <div class="tab1Title4"></div>
              <div class="tab1Title5"></div>
            </div>

            <div class="tab1CardBody">
              <div v-for="(item, index) in dataList1" :key="index" class="">
                <div class="cardBodyList1">
                  <el-avatar
                    :size="40"
                    src="https://empty"
                    @error="errorHandler"
                  >
                    <img
                      src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
                    />
                  </el-avatar>

                  <div class="listTitle">
                    <div class="listTitle1">{{ item.title }}</div>
                    <div class="listTitle2">
                      <span class="titleSpan1">{{ item.name }}</span>
                      <span class="titleSpan2">{{ item.time }}</span>
                      <span class="titleSpan3">{{ item.type }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </div>
        <div class="tabHead2">
          <div class="headTitle">
            <div
              class="tabIndex"
              v-for="(item, index) in tabList2"
              :key="index"
              :class="index == tabIndex2 ? 'tabSelect' : ''"
              @click="setTabIndex2(index)"
            >
              {{ item }}
            </div>
          </div>
          <div class="headButton">
            <el-button type="primary" plain>更多</el-button>
          </div>
        </div>
        <div style="margin-top: 20px; margin-right: 20px; margin-bottom: 20px">
          <el-card>
            <div class="tab2CardBody">
              <div
                v-for="(item, index) in dataList2"
                :key="index"
                class="tab2CardList"
              >
                <div>{{ item.title }}</div>
                <div class="cardTitle">
                  <span class="titleSpan1">{{ item.time }}</span>
                  <span class="titleSpan2">{{ item.status }}</span>
                  <span class="titleSpan3">{{ item.name }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="dateHead">
          <div class="headTitle">我的日程</div>
          <div class="headButton">
            <el-button type="primary" plain>更多</el-button>
          </div>
        </div>
        <div style="margin-right: 20px; margin-top: 20px; margin-bottom: 20px">
          <el-card>
            <el-calendar
              :range="['2023-01-30', '2023-02-26']"
              v-model="calendarValue"
            >
              <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
              <template slot="dateCell" slot-scope="{ data }">
                <p :class="data.isSelected ? 'is-selected' : ''">
                  {{ data.day.split('-').slice(1).join('-') }}
                  {{ data.isSelected ? '✔️' : '' }}
                </p>
              </template>
            </el-calendar>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="16">
        <div class="tabHead2">
          <div class="headTitle">
            <div
              class="tabIndex"
              v-for="(item, index) in tabList3"
              :key="index"
              :class="index == tabIndex3 ? 'tabSelect' : ''"
              @click="setTabIndex3(index)"
            >
              {{ item }}
            </div>
          </div>
          <div class="headButton">
            <el-button type="primary" plain>新建</el-button>
            <el-button type="primary" plain>更多</el-button>
          </div>
        </div>
        <div style="margin-top: 20px; margin-right: 20px; margin-bottom: 20px">
          <el-card>
            <div class="tab3CardBody">
              <div
                v-for="(item, index) in dataList3"
                :key="index"
                class="tab3CardList"
              >
                <div>{{ item.title }}</div>
                <div class="cardTitle">
                  <div class="titleSpan1">
                    <div class="titleIcon1"></div>
                    <div class="titleNum1">{{ item.num1 }}</div>
                  </div>
                  <div class="titleSpan2">
                    <div class="titleIcon2"></div>
                    <div class="titleNum2">{{ item.num2 }}</div>
                  </div>
                  <div class="titleSpan3">
                    <div class="titleIcon3"></div>
                    <div class="titleNum3">{{ item.num3 }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="tabHead2">
          <div class="headTitle">
            <div
              class="tabIndex"
              v-for="(item, index) in tabList4"
              :key="index"
              :class="index == tabIndex4 ? 'tabSelect' : ''"
              @click="setTabIndex4(index)"
            >
              {{ item }}
            </div>
          </div>
          <div class="headButton">
            <el-button type="primary" plain>更多</el-button>
          </div>
        </div>
        <div style="margin-top: 20px; margin-right: 20px; margin-bottom: 20px">
          <el-card>
            <div>
              <div
                v-for="(item, index) in dataList4"
                :key="index"
                class="tab4CardList"
              >
                <div
                  class="ListIndex"
                  :class="
                    index == 0
                      ? 'index1'
                      : index == 1
                      ? 'index2'
                      : index == 2
                      ? 'index3'
                      : ''
                  "
                >
                  {{ index + 1 }}
                </div>
                <div class="ListTitle">{{ item }}</div>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        list: ['常用业务', '销售人员', '部门经理', '公司高管'],
        order: 0,
        tabData: [],
        tabData1: [
          {
            title: '考勤相关',
            cardList: [
              '请假申请',
              '转正申请',
              '外派申请',
              '加班申请',
              '补卡申请',
            ],
          },
          {
            title: '业务相关',
            cardList: [
              '合同申请',
              '会议室预定',
              '客户拜访登记',
              '采购申请',
              '项目立项',
              '培训申请',
            ],
          },
          {
            title: '报销相关',
            cardList: [
              '报销申请',
              '借款申请',
              '还款申请',
              '社保查询',
              '出差报销',
              '专项报销',
            ],
          },
          {
            title: '行政相关',
            cardList: [
              '用车申请',
              '用印申请',
              '证件补贴',
              '档案利用登记',
              '办公用品申请',
            ],
          },
        ],
        tabData2: [
          {
            title: '我的客户',
            cardList: [
              '线索信息',
              '拜访记录',
              '商机信息',
              '投标支持',
              '报价模板',
              '伙伴管理',
            ],
          },
          {
            title: '我的合同',
            cardList: [
              '我的采购',
              '起草合同预审',
              '合同模板查询',
              '我要起草合同',
              '我要查询合同',
              '起草报价申请',
              '合同开票申请',
            ],
          },
          {
            title: '我的项目',
            cardList: [
              '我要开票',
              '我要发包',
              '我要派包',
              '工程项目申请',
              '我的工程项目',
              '产品出库申请',
              '提项目备忘录',
            ],
          },
          {
            title: '我的业绩',
            cardList: [
              '我的合同额',
              '业绩完成率',
              '我的已收款',
              '我的应收款',
              '销售立项跟进表',
            ],
          },
        ],
        tabData3: [
          {
            title: '管理相关',
            cardList: [
              '我要开票',
              '我的产能',
              '我的已收款',
              '我的应收款',
              '我要提坏账',
              '我的产能填报',
              '我要提项目周报',
            ],
          },
          {
            title: '产品相关',
            cardList: [
              '我要体验',
              '源码申请',
              '产品出库申请',
              '产品模块申请',
              '许可变更申请',
              '账号及权限申请',
              '我要提产品需求',
            ],
          },
          {
            title: '项目相关',
            cardList: [
              '我要发包',
              '我要派工',
              '我要采购',
              '我的项目信息',
              '项目计划和预算',
              '我要提项目备忘',
              '我要提项目变更',
            ],
          },
          {
            title: '服务相关',
            cardList: [
              '客户巡检',
              '我服务的客户',
              '服务交接报告',
              '投诉处理流程',
              '应续签客户流程',
              '待我处理的问题单',
              '我的客户的问题单',
            ],
          },
        ],
        tabData4: [
          {
            title: '团队客户',
            cardList: [
              '报价申请',
              '运营在线',
              '客户信息',
              '拜访记录',
              '商机信息',
              '销售月报',
              '投标支持',
            ],
          },
          {
            title: '团队合同',
            cardList: [
              '合同预审',
              '合同申请',
              '合同审批',
              '合同查询',
              '完工确认',
              '坏帐记录',
              '我的采购',
            ],
          },
          {
            title: '团队项目',
            cardList: [
              '项目信息',
              '项目开票',
              '项目回款',
              '合同执行计划',
              '工程立项申请',
              '产品出库申请',
            ],
          },
          {
            title: '团队业绩',
            cardList: [
              '团队产能',
              '业绩地图',
              '团队合同总额',
              '团队收款查询',
              '团队经营报表',
              '团队业绩看板',
            ],
          },
        ],
        tabList: ['待办', '待阅'],
        tabIndex: 0,
        tabList2: ['我发起的流程', '我已审的流程'],
        tabIndex2: 0,
        tabList3: ['最新帖子', '签单喜讯'],
        tabIndex3: 0,
        tabList4: ['常用资料', '业务系统'],
        tabIndex4: 0,

        dataList1: [
          {
            title: '12绩效考核有加、减分指标结果值需要您录入数据，请处理',
            name: '张孝昆',
            time: '2023-02-04 19:50',
            type: '绩效考核',
          },
          {
            title: '12绩效考核有指标结果值需要您录入数据，请处理',
            name: '张孝昆',
            time: '2023-02-04 19:50',
            type: '绩效考核',
          },
          {
            title: '请签收: 关于春节放假的通知(来自分发)',
            name: '张孝昆',
            time: '2023-02-04 19:50',
            type: '登记单明细',
          },
        ],

        dataList2: [
          {
            title: 'ceshiceshicesh',
            time: '2023-01-31',
            status: '结束',
            name: '<无>',
          },
          {
            title: '苹果发布大会',
            time: '2023-01-31',
            status: '结束',
            name: '张孝昆',
          },
          {
            title: '加班申请',
            time: '2023-01-30',
            status: '流程中',
            name: '张孝昆',
          },
          {
            title: '张孝昆_申请请假',
            time: '2023-01-30',
            status: '流程中',
            name: '张孝昆',
          },
          {
            title:
              '张孝昆提交的集成项目立项评审(集团公司)申请流程，项目名称:1，项目金额: 111万元',
            time: '2023-01-30',
            status: '流程中',
            name: '张孝昆',
          },
          {
            title: 'ceshiceshicesh',
            time: '2023-01-30',
            status: '创建中',
            name: '<无>',
          },
        ],

        dataList3: [
          {
            title: '9月邀您来北京参加国际山地徒步大会',
            num1: '106',
            num2: '4',
            num3: '1',
          },
          {
            title: '[舞蹈俱乐部]Jazz舞蹈班即将开课啦',
            num1: '109',
            num2: '17',
            num3: '1',
          },
          {
            title: 'sdsad ',
            num1: '8',
            num2: '3',
            num3: '0',
          },
          {
            title: 'demo系统演示',
            num1: '51',
            num2: '1',
            num3: '0',
          },
          {
            title: 'xox发帖测试',
            num1: '5',
            num2: '0',
            num3: '0',
          },
          {
            title: '今天天气很好',
            num1: '13',
            num2: '0',
            num3: '0',
          },
        ],

        dataList4: [
          '中国新版GMP认证',
          '集团各公司上、下班公车详细信息表',
          '公司系统搜索',
          '门户问题FAQ',
          '打印机操作指南',
          '投标管理需求说明书',
        ],
        calendarValue: new Date(),
      }
    },
    mounted() {
      this.tabData = this.tabData1
    },
    methods: {
      setIndex(index) {
        this.order = index

        this.tabData = this['tabData' + (index + 1)]
      },
      setTabIndex(index) {
        this.tabIndex = index
      },
      setTabIndex2(index) {
        this.tabIndex2 = index
      },
      setTabIndex3(index) {
        this.tabIndex3 = index
      },
      setTabIndex4(index) {
        this.tabIndex4 = index
      },

      errorHandler() {
        return true
      },
    },
  }
</script>

<style scoped lang="scss">
  .headAll {
    width: 100%;
    height: 100%;
    background: url('../../../assets/personal/map.png');
  }
  .headList {
    display: flex;
    justify-content: space-between;
    width: 600px;
    height: 45px;
    background-color: #fff;
    margin: 0 auto;
    margin-bottom: 30px;

    .cardIndex {
      height: 45px;
      width: 150px;
      // border: 1px solid #111;
      font-size: 18px;
      line-height: 45px;
      text-align: center;
      cursor: pointer;
    }

    .selectIndex {
      background-color: #eaf2ff;
      color: #4285f4;
      border-bottom: 3px solid #4285f4;
    }
  }

  .headBody {
    width: 1000px;
    // height: 200px;
    display: flex;
    justify-content: space-between;
    margin: 0 auto;
    padding-bottom: 40px;

    .bodyCard {
      width: 150px;
      border-radius: 4px 4px 4px 4px;
      padding-bottom: 20px;
      background: #fff;
      box-shadow: 0px 5px 10px 0px rgb(0 0 0 / 8%);

      .cardHead {
        height: 45px;
        line-height: 45px;
        font-size: 18px;
        text-align: center;
        background-color: #4285f4;
        border-radius: 4px 4px 0px 0px;
        color: #eaf2ff;
        margin-bottom: 10px;
      }

      .cardIndex {
        width: 100%;
        height: 30px;
        line-height: 30px;
        font-size: 16px;
        text-align: center;
        cursor: pointer;
      }

      .cardIndex:hover {
        color: #4285f4;
        background: #d8d8d8;
      }
    }

    // @keyframes swing1 {
    //   0% {
    //     transform: rotate(-5deg);
    //   }
    //   50% {
    //     transform: rotate(5deg);
    //   }
    //   100% {
    //     transform: rotate(-5deg);
    //   }
    // }
  }

  .tabHead {
    margin-top: 20px;
    margin-left: 5px;
    display: flex;
    height: 30px;

    .tabIndex {
      margin-right: 20px;
      height: 30px;
      line-height: 30px;
      font-size: 18px;
      cursor: pointer;
    }

    .tabSelect {
      color: #4285f4;
    }
  }

  .dateHead {
    width: 100%;
    display: flex;
    justify-content: space-between;
    padding-right: 20px;
    height: 30px;
    line-height: 30px;
    margin-top: 20px;

    .headTitle {
      color: #4285f4;
      font-size: 18px;
    }
  }

  .tabHead2 {
    display: flex;
    justify-content: space-between;
    padding-right: 20px;
    height: 30px;
    line-height: 30px;
    margin-top: 20px;
    font-size: 18px;

    .headTitle {
      margin-left: 5px;
      display: flex;

      .tabIndex {
        margin-right: 20px;
        height: 30px;
        line-height: 30px;
        font-size: 18px;
        cursor: pointer;
      }

      .tabSelect {
        color: #4285f4;
      }
    }
  }

  .tab1CardTitle {
    display: flex;
    margin-left: 10px;
    margin-bottom: 10px;

    .tab1Title1 {
      width: 30px;
      height: 30px;
      background: url('../../../assets/personal/e-mail.png') no-repeat;
    }

    .tab1Title2 {
      height: 30px;
      line-height: 30px;
      font-size: 18px;

      .titleSpan {
        margin-left: 10px;
        margin-right: 10px;
        color: #4285f4;
        cursor: pointer;
      }
    }

    .tab1Title3 {
      height: 30px;
      line-height: 30px;
      font-size: 16px;
      //下划线
      text-decoration: underline;
      margin-left: 20px;
      font-weight: 300;
      cursor: pointer;
    }

    .tab1Title4 {
      width: 20px;
      height: 20px;
      margin-top: 6px;
      margin-left: 5px;
      cursor: pointer;
      background: url('../../../assets/personal/refresh.png') no-repeat;
    }

    .tab1Title5 {
      width: 20px;
      height: 20px;
      margin-top: 6px;
      cursor: pointer;
      background: url('../../../assets/personal/fastreview.png') no-repeat;
    }
  }

  .tab1CardBody {
    .cardBodyList1 {
      display: flex;
      margin-bottom: 15px;

      .listTitle {
        margin-left: 15px;
        color: #999999;

        .listTitle2 {
          margin-top: 8px;

          .titleSpan1 {
            margin-right: 10px;
          }
          .titleSpan2 {
            margin-right: 10px;
          }

          .titleSpan3 {
            margin-right: 10px;
            color: #6bcac3;
            cursor: pointer;
          }
        }
      }
    }
  }

  .tab2CardBody {
    .tab2CardList {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;

      .cardTitle {
        width: 208px;
        text-align: left;
      }

      .titleSpan1 {
        margin-right: 15px;
      }
      .titleSpan2 {
        margin-right: 15px;
        text-align: center;
        display: inline-block;
        width: 45px;
      }
      .titleSpan3 {
        margin-right: 15px;
        text-align: center;
        display: inline-block;
        width: 45px;
        font-weight: 600;
      }
    }
  }

  .tab3CardBody {
    .tab3CardList {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;

      .cardTitle {
        display: flex;
        width: 175px;
        text-align: left;
      }

      .titleSpan1 {
        display: flex;
        width: 60px;

        .titleNum1 {
          height: 20px;
          line-height: 20px;
        }

        .titleIcon1 {
          width: 20px;
          height: 20px;
          background: url('../../../assets/personal/read.png') no-repeat;
          margin-right: 3px;
        }
      }
      .titleSpan2 {
        display: flex;
        width: 60px;

        .titleNum2 {
          height: 20px;
          line-height: 20px;
        }

        .titleIcon2 {
          width: 20px;
          height: 20px;
          background: url('../../../assets/personal/comment.png') no-repeat;
          margin-right: 3px;
        }
      }
      .titleSpan3 {
        display: flex;
        width: 60px;

        .titleNum3 {
          height: 20px;
          line-height: 20px;
        }

        .titleIcon3 {
          width: 20px;
          height: 20px;
          background: url('../../../assets/personal/mark.png') no-repeat;
          margin-right: 3px;
        }
      }
    }
  }

  .is-selected {
    color: #1989fa;
  }

  .el-calendar {
    height: 470px;
  }

  .tab4CardList {
    display: flex;
    margin-bottom: 10px;

    .ListIndex {
      height: 20px;
      line-height: 20px;
      width: 20px;
      text-align: center;
      background: #d8d8d8;
      color: #fff;
    }

    .index1 {
      background: #ef7576;
    }

    .index2 {
      background: #f99427;
    }

    .index3 {
      background: #f9bb2d;
    }

    .ListTitle {
      margin-left: 10px;
      height: 20px;
      line-height: 20px;
    }
  }
</style>
