<template>
  <div class="content system-log-container">
    <el-row :gutter="20">
      <el-col :span="5" class="left">
        <div class="leftType">
          <div>
            <el-button type="primary" size="mini" @click="leftTypeStatus = 1">
              数据
            </el-button>
          </div>
          <div>
            <el-button
              type="primary"
              plain
              size="mini"
              @click="leftTypeStatus = 2"
            >
              问句
            </el-button>
          </div>
        </div>
        <div class="leftTree" v-if="leftTypeStatus == 1">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
          ></el-tree>
        </div>
        <div v-else style="padding: 10px">
          <el-input
            placeholder="请输入内容"
            style="width: 100%"
            prefix-icon="el-icon-search"
            v-model="searchValue"
          ></el-input>
          <div class="questionBox">
            <div
              class="qusetionItem"
              @click="question = item"
              v-for="(item, index) in newQuestionList"
              :key="index"
            >
              {{ item }}
            </div>
          </div>
        </div>
      </el-col>
      <el-col
        :span="19"
        class="right"
        v-loading="loading"
        ref="scrollContainer"
      >
        <el-row type="flex" justify="center" :gutter="24">
          <el-col :span="10" class="el-col1">
            <div class="answerBox">
              <el-upload
                class="upload-demo"
                drag
                :limit="1"
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                :action="baseApi + api"
                :headers="{
                  'X-Requested-With': 'XMLHttpRequest',
                  token: token,
                }"
                :on-success="handleAvatarSuccess"
                :on-error="handleAvatarError"
              >
                <i class="el-icon-upload"></i>
                <div class="uploadBtn">
                  <el-button type="primary" plain>上传数据文件</el-button>
                  <el-button type="primary" plain>使用示例数据</el-button>
                </div>
                <div class="el-upload__text">
                  将文件拖到此处，或
                  <em>点击上传</em>
                </div>
                <div class="uploadtips">
                  <div class="el-upload__tip">
                    1.单次可上传1个文件；若多次上传则默认使用最后上传的文件。
                  </div>
                  <div class="el-upload__tip">
                    2.单个文件内容不超过40列，1万行，文件大小不超过10MB；文件编码格式为UTF-8。
                  </div>
                  <div class="el-upload__tip">
                    3.若文件中包含多个sheet，将会被解析为多个表格。一个文件最多解析前5个sheet。
                  </div>
                </div>
              </el-upload>
              <div class="tipBox">
                <div class="el-upload__tip">
                  当前数据模型，包括以下维度和度量：
                </div>
                <div class="el-upload__tip">维度：</div>
                <div class="el-upload__tip">
                  1. 销售表， 包括: 销售姓名, 销售分部等信息
                </div>
                <div class="el-upload__tip">
                  2. 合同表， 包括: 合同名称, 行业名称, 商机类型, 合同类型等信息
                </div>
                <div class="el-upload__tip">
                  3. 合同明细表， 包括: 商机来源, 是否在保, 是否新客等信息
                </div>
                <div class="el-upload__tip">
                  4. 合同签订日期_时间维度， 包括: 年, 年季, 年月, 年周,
                  年月日等信息
                </div>
                <div class="el-upload__tip">
                  5. 地理维， 包括: 省份, 城市等信息
                </div>
                <div class="el-upload__tip">度量：</div>
                <div class="el-upload__tip">
                  1. 合同明细表， 包括: 商机预算, 售前成本, 回款金额, 外采费用,
                  产品模块标价等信息
                </div>
                <div class="el-upload__tip">
                  2. 其他， 包括: 利润, 项目成本等信息
                </div>
                <div class="el-upload__tip">示例问句：</div>
                <div
                  class="el-upload__tip tipBoxQusetion"
                  @click="
                    question = '招标人名称为YSLYT的招标人年度招标数量是多少？'
                  "
                >
                  招标人名称为YSLYT的招标人年度招标数量是多少？
                </div>
                <div
                  class="el-upload__tip tipBoxQusetion"
                  @click="
                    question =
                      '2022年投标人名称为深圳市佳运通电子有限公司的投标人中标次数是多少?'
                  "
                >
                  2022年投标人名称为深圳市佳运通电子有限公司的投标人中标次数是多少?
                </div>
                <div
                  class="el-upload__tip tipBoxQusetion"
                  @click="
                    question =
                      '投标人名称为深圳市佳运通电子有限公司在2022年是否投标即中标?'
                  "
                >
                  投标人名称为深圳市佳运通电子有限公司在2022年是否投标即中标?
                </div>
              </div>
            </div>
            <div
              class="chat-bubble"
              v-for="(item, index) in messages"
              :key="index"
            >
              <el-card class="right-chat" v-if="item.position == 'right'">
                <span>{{ item.content }}</span>
              </el-card>
              <el-card class="card left-chat" v-else>
                <div v-if="item.type.includes('table')">
                  <div class="tableInfo">
                    <div v-for="(i, index) in item.tableList" :key="index">
                      <div class="tableKey">
                        <i class="el-icon-document"></i>
                        {{ i.customTable }}
                      </div>
                      <div>数据表共有{{ i.rowNum }}行，{{ i.cellNum }}列。</div>
                    </div>
                  </div>
                  <div>
                    <el-button type="primary" plain @click="open(item.tableId)">
                      查看详情
                    </el-button>
                    <el-button
                      type="primary"
                      plain
                      @click="setting_open(item.tableId)"
                    >
                      数据配置
                    </el-button>
                  </div>
                </div>
                <!-- <div v-else style="width: 900px; height: 550px">

                </div> -->
                <div v-else style="width: 900px; padding: 20px">
                  <div>{{ item.url }}</div>
                  <iframe
                    v-if="item.chart"
                    style="border: 0"
                    :src="item.chart"
                    width="920"
                    height="550"
                  ></iframe>
                </div>
              </el-card>
            </div>
          </el-col>
        </el-row>
        <div class="footer">
          <el-row type="flex" justify="center" :gutter="24">
            <el-col :span="16">
              <div class="footer-box">
                <input
                  type="text"
                  name="question"
                  v-model="question"
                  placeholder="输入问题或需求 ..."
                  autocomplete="off"
                />
                <div class="footer-btn" @click="start">开始对话</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <dialogtable ref="tableref"></dialogtable>
    <dialogsetting ref="settingref"></dialogsetting>
  </div>
</template>
<script>
  import store from '@/store'
  import {
    getCwztDbInfo,
    getKnowledgeList,
    getTableList,
  } from '@/api/ai/index.js'
  import axios from 'axios'
  import * as echarts from 'echarts'
  import dialogtable from './table.vue'
  import dialogsetting from './setting.vue'
  export default {
    name: 'aifx',
    components: {
      dialogtable,
      dialogsetting,
    },
    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development' ? '/vab-mock-server' : '/api',
        api: '/zbgl/gbi/export/uploadData',
        token: store.getters['user/token'],
        treeData: [
          {
            label: '图表',
            children: [
              {
                label: '折线图',
                id: 'line',
              },
              {
                label: '柱状图',
                id: 'bar',
              },
              {
                label: '饼图',
                id: 'pie',
              },
            ],
          },
        ],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        messages: [],
        chooseTree: {},
        question: '',
        testIndex: 0,
        newQuestionList: [],
        searchValue: '',
        leftTypeStatus: 1,
        config: {},
        loading: false,
        tableId: '',
      }
    },

    created() {},
    mounted() {
      // this.getConfig()
    },
    watch: {
      messages(val) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      },
    },
    methods: {
      getConfig() {
        this.loading = true
        getCwztDbInfo().then((res) => {
          console.log(res.data)
          this.config = res.data
          this.loading = false
        })
        // getKnowledgeList().then(res =>{
        //   console.log(res)
        // })
      },
      //数据弹窗
      open(tableId) {
        this.$refs.tableref.open(tableId)
      },
      //数据弹窗 end
      // 配置弹窗
      setting_open(tableId) {
        this.$refs.settingref.open(tableId)
      },
      start() {
        this.testIndex = this.testIndex + 1
        this.messages.push({ position: 'right', content: this.question })
        this.$nextTick(() => {
          this.test(`${this.chooseTree.id}${this.testIndex}`)
          this.chooseTree = {}
        })
      },
      test(id) {
        let that = this
        that.loading = true
        axios({
          url: `https://hit-mitlab.cn:7860/generate_bid`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            prompt: that.question,
            database: {
              host: '60.205.166.36',
              port: '5236',
              user: 'HBGRCZHEZ',
              password: 'HBGRCZHEZ',
            },
            id: '582101985493061',
            temperature: 0.1,
            max_tokens: 2048,
            top_p: 0.95,
            stop: ['\nObservation:', '\nObservation :'],
          },
        })
          .then((res) => {
            if (res.data.table) {
              this.newTest(res.data)
            } else {
              this.messages.push({
                position: 'left',
                type: '',
                url: res.data.analyse,
              })
            }
            this.loading = false
          })
          .catch((err) => {
            this.loading = false
          })
      },
      newTest(value) {
        axios({
          url: `https://office.wenxin.example.com/api/app/charts/`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: value.table,
        })
          .then((res1) => {
            this.messages.push({
              position: 'left',
              type: '',
              url: value.analyse,
              chart: res1.data.chart,
            })
            this.loading = false
            this.question = ''
          })
          .catch((err) => {
            this.loading = false
          })
      },
      in(id, data) {
        const chats = echarts.init(document.getElementById(id))

        // 指定图表的配置项和数据
        if (id.includes('line')) {
          chats.setOption({
            xAxis: {
              type: 'category',
              data: data.chart.data.xaxis,
            },
            yAxis: {
              type: 'value',
            },
            series: [
              {
                data: Object.values(data.chart.data.yaxis[0])[0],
                type: 'line',
              },
            ],
          })
        } else if (id.includes('pie')) {
          chats.setOption({
            title: {
              // text: 'Referer of a Website',
              // subtext: 'Fake Data',
              left: 'center',
            },
            tooltip: {
              trigger: 'item',
            },
            legend: {
              top: '5%',
              left: 'center',
            },
            series: [
              {
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: [
                  { value: 1048, name: 'Search Engine' },
                  { value: 735, name: 'Direct' },
                  { value: 580, name: 'Email' },
                  { value: 484, name: 'Union Ads' },
                  { value: 300, name: 'Video Ads' },
                ],
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                  },
                },
              },
            ],
          })
        } else if (id.includes('bar')) {
          chats.setOption({
            xAxis: {
              type: 'category',
              data: data.chart.data.xaxis,
            },
            yAxis: {
              type: 'value',
            },
            series: [
              {
                data: Object.values(data.chart.data.yaxis[0])[0],
                type: 'bar',
              },
            ],
          })
        } else {
        }
      },
      handleNodeClick(data) {
        this.chooseTree = data
        this.question = data.label
      },
      scrollToBottom() {
        const container = this.$refs.scrollContainer
        container.scrollTop = container.scrollHeight
      },
      handleAvatarSuccess(response, file, fileList) {
        getTableList({ batchId: response.data }).then((res) => {
          this.messages.push({
            position: 'left',
            type: 'table',
            tableId: response.data,
            tableList: res.data,
          })
        })
      },
      handleAvatarError() {
        this.$message.error('请稍后再试')
      },
    },
  }
</script>
<style scoped lang="scss">
  .content {
    overflow: hidden;

    .left {
      display: flex;
      padding-right: 0 !important;
      box-shadow: 2px 0px 12px 0px rgba(0, 0, 0, 0.1);

      .leftType {
        height: calc(100vh - 60px - 50px - 20px * 2 - 55px);
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 10px 6px;
        border-right: 1px solid #dcdfe5;

        div {
          margin-bottom: 5px;
        }

        .el-button {
          padding: 7px 6px;
          writing-mode: vertical-rl;
          display: flex;
          flex-direction: column;
          align-items: center;
        }
      }

      .leftTree {
        flex: 1;
        padding: 10px;
        // .el-tree {
        //   width: 100%;
        // }
      }
    }

    .right {
      .answerBox {
        // height: calc(100vh - 60px - 50px - 20px * 2 - 55px - 60px);
        .upload-demo {
          width: 700px;
          margin: 20px;
          // height: 370px;
          .uploadBtn {
            margin-bottom: 20px;
          }
          .uploadtips {
            text-align: left;
            padding: 10px;
            padding-left: 20px;
            margin-top: 50px;
            margin-bottom: 10px;
          }
        }
      }

      .el-col1 {
        padding: 0 20px 100px;
        position: relative;
        height: calc(100vh - 275px);
        overflow-y: auto;
        width: 100%;
      }

      .card {
        border-radius: 10px;
      }

      .chat-bubble {
        padding: 10px 0;
      }

      .left-chat {
        line-height: 25px;
        word-break: break-word;
        // width: 700px;
        .tableInfo {
          background: #f6f8fb;
          border-radius: 6px;
          margin-bottom: 8px;
          padding: 12px;
          .tableKey {
            align-items: center;
            display: flex;
            margin-bottom: 4px;
            i {
              margin-right: 5px;
            }
          }
          .tableValue {
            color: #151b26;
            font-family: PingFangSC-Regular;
            font-size: 14px;
            font-weight: 400;
            line-height: 22px;
          }
        }
      }

      .right-chat {
        display: flex;
        justify-content: right;
      }

      .tab-card {
        display: flex;
        align-items: center;
        padding: 20px;
        background-color: #fff;
      }

      .body {
        flex: 1;
      }

      .header {
        font-size: 20px;
        font-weight: 600;
        margin-bottom: 20px;

        .right-btn {
          font-size: 18px;
          font-weight: 600;
          color: #aaa;
        }
      }

      .bottom {
        padding: 20px 0 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: #333;
        font-size: 14px;
      }

      .right-btn {
        font-size: 14px;
        float: right;
        padding: 3px 0;
        color: #333;
      }

      .footer {
        width: 100%;
        position: relative;
        z-index: 99;

        .footer-box {
          width: 100%;
          flex: 1;
          display: flex;
          align-items: center;
          position: relative;
          height: 50px;
          background-color: #333;
          border-radius: 50px;

          input {
            caret-color: #ffa502;
            flex: 1;
            height: 50px;
            border-radius: 50px;
            padding: 12px 20px;
            box-sizing: border-box;
            -webkit-transition: 0.5s;
            background-color: #333;
            transition: 0.5s;
            outline: none;
            border: 0;
            color: #fff;
          }
        }

        .footer-btn {
          position: absolute;
          right: 20px;
          z-index: 9;
          color: #ddd;
          cursor: pointer;

          &:hover {
            color: green;
          }
        }
      }
    }
  }

  .questionBox {
    height: calc(100vh - 330px);
    overflow-y: auto;
    padding: 10px;
  }

  .qusetionItem {
    height: 36px;
    line-height: 36px;
    cursor: pointer;
  }

  .tipBox {
    padding-left: 20px;
    .tipBoxQusetion {
      color: #c0d9d9;
      text-decoration: underline;
      cursor: pointer;
    }
  }

  :deep(.el-tree-node__content) {
    height: 32px !important;
    font-size: 14px;
    color: #2f2e3f;
    margin-bottom: 2px;
  }

  :deep(.el-tree-node__label) {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  :deep(.el-upload) {
    width: 100%;
    height: 100%;
  }
  :deep(.el-upload-dragger) {
    width: 100%;
    height: 100%;
  }
  :deep(.el-dialog__body) {
    padding-top: 0;
  }
  .system-log-container {
    // background: #f6f8f9 !important;
    padding: 0 !important;
  }
</style>
