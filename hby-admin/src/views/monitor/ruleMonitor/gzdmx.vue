<template>
  <div class="box">
    <div class="mb10">
      功能说明：将自然语言的问题转化为关系型数据库的查询语句SQL，然后从数据库中查询结果并显示
    </div>
    <el-row :gutter="20">
      <el-col :span="4" style="display: flex">
        <div>
          <el-input
            placeholder="请输入内容"
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
        <el-divider class="divider" direction="vertical"></el-divider>
      </el-col>

      <el-col :span="0">
        <div class="mb10">
          <el-form ref="form" :model="form" label-position="top">
            <el-form-item
              label="下面是示例问题，您可以选择并做为参考"
              style="font-weight: 700"
            >
              <el-select
                v-model="form.prompt"
                placeholder="请输入您的问题，然后点击提交问题按钮"
                class="w100"
              >
                <el-option
                  v-for="(item, index) in questionList"
                  :key="index"
                  :label="item"
                  :value="item"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="问题" style="font-weight: 700">
              <el-input
                v-model="form.question"
                type="textarea"
                :rows="5"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button class="w100 btn" @click="submitQuestion">
                提交问题
              </el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-loading="loading">
          <p style="font-weight: 700; margin-top: 60px">转化的SQL</p>
          <p style="min-height: 120px">
            <el-row
              v-for="(item, index) in sqlList"
              :key="index"
              style="margin: 10px 0; font-weight: 500"
            >
              <div
                style="font-weight: 700; font-size: 12px; margin-bottom: 4px"
              >
                {{ item.label }} :
              </div>
              <div>
                <el-input
                  v-model="item.sqlText"
                  readonly
                  type="textarea"
                  :rows="3"
                  class="textAreaStyle"
                ></el-input>
              </div>
            </el-row>
          </p>
          <p style="font-weight: 700; margin-top: 20px">查询数据库的结果</p>
          <p style="min-height: 120px">
            <el-input
              v-model="resultText"
              readonly
              type="textarea"
              :rows="3"
              class="textAreaStyle"
            ></el-input>
          </p>
        </div>
      </el-col>

      <el-col :span="20" v-loading="loading">
        <el-row type="flex" justify="center" :gutter="24">
          <el-col :span="10" class="el-col1">
            <div
              class="chat-bubble"
              v-for="(item, index) in messages"
              :key="index"
            >
              <div class="right-chat" v-if="item.position == 'right'">
                <span>{{ item.content }}</span>
              </div>
              <el-card class="card left-chat" v-else>
                <!-- <vab-icon class="red mini" :icon="currentObj.icon" style="margin-right: 5px" /> -->
                <!-- <i class="el-icon-chat-dot-round" style="margin-right: 5px"></i> -->
                <!-- <span>{{ item.content }}</span> -->
                <p style="font-weight: 700">转化的SQL</p>
                <p style="min-height: 120px">
                  <el-row
                    v-for="(items, index) in item.sqlList"
                    :key="index"
                    style="margin: 10px 0; font-weight: 500"
                  >
                    <div
                      style="
                        font-weight: 700;
                        font-size: 12px;
                        margin-bottom: 4px;
                      "
                    >
                      {{ items.label }} :
                    </div>
                    <div>
                      <el-input
                        v-model="items.sqlText"
                        readonly
                        type="textarea"
                        :rows="3"
                        class="textAreaStyle"
                      ></el-input>
                    </div>
                  </el-row>
                </p>
                <p style="font-weight: 700; margin-top: 20px">
                  查询数据库的结果
                </p>
                <p style="min-height: 80px">
                  <el-input
                    v-model="item.resultText"
                    readonly
                    type="textarea"
                    :rows="3"
                    class="textAreaStyle"
                  ></el-input>
                </p>
              </el-card>
            </div>
          </el-col>
        </el-row>

        <div class="footer">
          <el-row type="flex" justify="center" :gutter="24">
            <el-col :span="10">
              <div class="footer-box">
                <input
                  type="text"
                  name="question"
                  placeholder="输入问题或需求 ..."
                  autocomplete="off"
                  v-model="question"
                />
                <div class="footer-btn" @click="submitQuestion">开始对话</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <el-col :span="0">
        <p>
          以下是数据库的部分示例数据，供您分析参考。您可以在下边的问题输入框里输入要查询的问题，比如“学生杨明哲的数学和物理是多少分”等等，也可以从示例问题中选择问题
        </p>
        <el-table
          :data="tableData"
          style="width: 100%"
          :height="400"
          border
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column
            prop="date"
            label="日期"
            sortable
            width="180"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            sortable
            width="180"
          ></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { generateFinance } from '@/api/ai/index.js'
  import axios from 'axios'
  const questionList = [
    '货币资金',
    '存放中央银行款项',
    '存放同业',
    '存出保证金',
    '内部金融机构资金往来',
    '结算备付金',
    '交易性金融资产',
    '买入返售金融资产',
    '应收股利',
    '应收利息',
    '应收账款',
    '预付账款',
    '期货保证金',
    '应收补贴款',
    '应收出口退税',
    '其他应收款',
    '存货',
    '原材料',
    '库存商品',
    '待摊费用',
    '其它流动资产',
    '长期投资',
    '长期股权投资',
    '长期债权投资',
    '流动资产合计',
    '长期投资合计',
    '长期应收款',
    '固定资产原价',
    '减：累计折旧',
    '固定资产净值',
    '减：固定资产减值准备',
    '固定资产净额',
    '工程物资',
    '在建工程',
    '固定资产清理',
    '待处理固定资产净损失',
    '固定资产合计',
    '无形资产',
    '土地使用权',
    '累计摊销',
    '商誉',
    '独立账户资产',
    '待处理财产损溢',
    '长期待摊费用（递延资产）',
    '固定资产修理 ',
    '固定资产改良支出 ',
    '其它长期资产',
    '特准备物资',
    '无形资产及其他资产合计',
    '递延税款借项',
    '委托贷款',
    '资产总计',
    '短期借款',
    '应付票据',
    '应付账款',
    '预收账款',
    '应付工资',
    '应付福利费',
    '应付股利',
    '应付利息',
    '应交税金',
    '其它应交款',
    '其它应付款',
    '预提费用',
    '预计负债',
    '递延收益',
    '长期负债',
    '流动负债合计',
    '长期借款',
    '应付债券',
    '长期应付款',
    '专项应付款',
    '特准备资金',
    '其它长期负债',
    '长期负债合计',
    '递延借款贷项',
    '负债合计',
    '实收资本(股本)',
    '国家资本',
    '集体资本',
    '法人资本',
    '国有法人资本',
    '集体法人资本',
    '个人资本',
    '外商资本',
    '资本公积',
    '其他综合收益',
    '盈余公积',
    '法定公益金',
    '未确认的投资损失',
    '利润分配',
    '未分配利润',
    '现金股利',
    '外币报表折算差额',
    '所有者权益小计',
    '减：未处理资产损失',
    '所有者权益合计',
    '负债和所有者权益合计',
    '营业收入',
    '主营业务成本',
    '期间费用',
    '资产减值损失',
    '公允价值变动收益',
    '投资收益',
    '对联营企业和合营企业的投资收益',
    '营业利润',
    '营业外收入',
    '营业外支出',
    '非流动资产处置损失',
    '利润总额',
    '所得税费用',
    '净利润',
  ]
  export default {
    name: 'tzfx',
    components: {},
    data() {
      return {
        loading: false,
        form: {
          prompt: '',
          question: '',
        },
        sqlList: [],
        resultText: '',
        questionList: questionList,
        tableData: [
          {
            date: '2016-05-02',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄',
          },
          {
            date: '2016-05-04',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1517 弄',
          },
          {
            date: '2016-05-01',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1519 弄',
          },
          {
            date: '2016-05-03',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1516 弄',
          },
        ],
        searchValue: '',
        question: '', //问题
        messages: [], //对话
        intervalId: null, //定时器
        isLoading: false, //正在回复
        currentObj: {}, //传来的数据
        dialogFormVisible: false,
        newQuestionList: questionList,
      }
    },
    watch: {
      'form.prompt': {
        handler(val) {
          this.generateQuestion()
        },
      },
      searchValue: {
        handler(val) {
          console.log(val)
          if (val) {
            console.log(1)
            let arr = this.questionList
            let profitItems = arr.filter((item) => item.includes(val))
            console.log(profitItems)
            this.newQuestionList = profitItems
          } else {
            console.log(2)
            this.newQuestionList = this.questionList
          }
        },
      },
    },
    methods: {
      // 生成题目
      generateQuestion() {
        this.form.question = this.form.prompt
        // this.loading = true
        // generateFinance({
        //   prompt: this.form.prompt
        // })
        // .then(res => {
        //   if (res && res.code == 1) {
        //     const resData = JSON.parse(res.data || '{}')
        //     this.form.question = resData.text
        //   }
        // })
        // .catch(err => {
        //   console.log('err', err)
        // })
        // .finally(() => {
        //   this.loading = false
        // })
      },
      // 提交问题-查询结果
      submitQuestion() {
        if (!this.question) return this.$message.error('请输入问题！')
        const _that = this
        const arr = _that.messages
        arr.push({ position: 'right', content: _that.question })
        _that.messages = arr
        // xhr对象请求数据
        let xhr = new XMLHttpRequest()
        let url = 'https://hit-mitlab.cn:7860/generate_fin'
        let data = {
          prompt: _that.question,
          database: {
            host: '60.205.166.36',
            port: '5236',
            user: 'HBGRCZHEZ',
            password: 'HBGRCZHEZ',
          },
          // time: { start: 2017, end: 2019 },
          // temperature: 0.1,
          // max_tokens: 2048,
          // "stop" :["\nObservation:","\nObservation :"]
        }

        xhr.onreadystatechange = function () {
          _that.loading = false
          if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const resData = JSON.parse(xhr.responseText)
            _that.resultText = resData.text
            _that.sqlList = []
            if (resData.sql_info) {
              // 动态sql数据
              Object.keys(resData.sql_info).forEach((key) => {
                const label = key
                const sqlText = resData.sql_info[key].accid_sql
                _that.sqlList.push({ label, sqlText })
              })
            }
            const arr = _that.messages
            arr.push({
              position: 'left',
              sqlList: _that.sqlList,
              resultText: _that.resultText,
            })
            _that.messages = arr
            _that.question = ''
          } else {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
              _that.$message.error(`code: [${xhr.status}], msg: 接口请求失败！`)
            }
          }
        }
        xhr.open('POST', url, true)
        xhr.setRequestHeader('Content-Type', 'application/json')
        _that.loading = true
        xhr.send(JSON.stringify(data))
      },
      //提交对话
      submit() {
        if (this.isLoading) {
          return
        }
        if (!this.question) {
          this.$message.error('请输入问题')
          return
        }
        this.isLoading = true
        const arr = this.messages
        arr.push({ position: 'right', content: this.question })
        this.messages = arr
        console.log(1111)
        axios({
          url: `https://hit-mitlab.cn:7860/generate_law`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
          data: {
            prompt: this.question,
            temperature: 1.0,
          },
        }).then((res) => {
          console.log(res)
          // this.addMessage({
          //   position: 'left',
          //   content: res.data.text,
          // })
          const arr = this.messages
          arr.push({ position: 'left', content: res.data.text })
          this.messages = arr
          this.isLoading = false
          this.question = ''
        })
        console.log(2222)
      },
      // 生成对话
      addMessage(message) {
        const arr = this.messages
        arr.push({ ...message, content: '' })
        this.messages = arr
        let i = 0
        const msgLength = message.content.length
        const typingDelay = Math.floor(Math.random() * 100) + 50 // 随机生成打字的延迟时间
        const typeNextLetter = () => {
          this.messages[this.messages.length - 1].content +=
            message.content.charAt(i)
          i++
          if (i <= msgLength) {
            setTimeout(typeNextLetter, typingDelay)
          } else {
            clearTimeout(this.intervalId)
          }
        }
        clearTimeout(this.intervalId)
        this.intervalId = setTimeout(typeNextLetter, 500)
      },
      show(row) {
        this.dialogFormVisible = true
        //拿数据
        this.currentObj = row
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .box {
    padding: 20px;
  }

  .btn {
    border-radius: 8px;
    padding: 10px;
    font-weight: 600;
    font-size: 16px;
    border: 1px solid #e5e7eb;
    background: linear-gradient(to bottom right, #f3f4f6, #e5e7eb);
  }

  .w100 {
    width: 100%;
  }

  .mb10 {
    margin-bottom: 10px;
  }

  .textAreaStyle .el-textarea__inner {
    border-radius: 6px !important;
    box-shadow: 0 0 0 3px transparent, rgba(0, 0, 0, 0.05) 0px 2px 4px 0px inset;
  }

  .questionBox {
    height: calc(100vh - 330px);
    overflow-y: auto;
    padding: 10px;
  }

  .divider {
    height: calc(100vh - 300px);
  }

  .qusetionItem {
    height: 36px;
    line-height: 36px;
    cursor: pointer;
  }

  .system-log-container {
    position: relative;
    background: #ebefef !important;
  }

  .el-col1 {
    padding: 0 20px 100px;
    position: relative;
    height: calc(100vh - 350px);
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
    bottom: 20px;
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

  .red {
    font-size: 48px;
    color: #e50113;
    width: 100px;

    &.mini {
      font-size: 18px;
      margin-right: 3px;
      width: auto;
    }
  }
</style>
