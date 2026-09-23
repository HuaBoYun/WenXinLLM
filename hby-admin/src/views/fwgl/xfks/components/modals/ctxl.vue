<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1200px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card style="margin-top: 20px">
          <div class="qu-content">
            <p>【{{ quData.quType }}】{{ quData.content }}</p>
            <div v-if="quData.quType === 1 || quData.quType === 3">
              <el-radio-group v-model="answerValues[0]" readonly>
                <el-radio
                  v-for="an in quData.answerList"
                  :label="an.id"
                  readonly
                  :key="an.id"
                >
                  {{ an.abc }}.{{ an.content }}
                </el-radio>
              </el-radio-group>
            </div>

            <!-- 多选题 -->
            <div v-if="quData.quType === 2">
              <el-checkbox-group v-model="answerValues" readonly>
                <el-checkbox
                  v-for="an in quData.answerList"
                  :label="an.id"
                  :key="an.id"
                >
                  {{ an.abc }}.{{ an.content }}
                </el-checkbox>
              </el-checkbox-group>
            </div>

            <div
              v-if="analysisShow"
              style="margin-top: 20px; color: #1890ff; font-weight: bold"
            >
              正确答案：{{ rightTags.join(' ') }}
            </div>
          </div>
        </el-card>

        <el-card
          v-if="analysisShow"
          class="qu-analysis"
          style="margin-top: 20px"
        >
          整题解析：
          <p>{{ quData.analysis }}</p>
          <p v-if="!quData.analysis">暂无解析内容！</p>
        </el-card>

        <el-card
          v-if="analysisShow"
          class="qu-analysis"
          style="margin-top: 20px"
        >
          选项解析：
          <div
            v-for="(an, index) in quData.answerList"
            class="qu-analysis-line"
            :key="index"
          >
            <template v-if="an.analysis">
              <p style="color: #555">{{ an.content }}：</p>
              <p style="color: #1890ff">{{ an.analysis }}</p>
            </template>
          </div>
          <p v-if="analysisCount === 0">暂无选项解析</p>
        </el-card>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button type="primary" @click="handNext">继续下一题</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { nextQu, fetchDetail } from '@/api/fwgl/xfks/wdcj'

  export default {
    name: 'ctxl',
    data() {
      return {
        title: '错题训练',
        dialogFormVisible: false,
        examId: '',
        quId: '',
        tags: [
          'A',
          'B',
          'C',
          'D',
          'E',
          'F',
          'G',
          'H',
          'I',
          'J',
          'K',
          'L',
          'M',
          'N',
        ],
        analysisShow: false,
        quData: {},
        answerValues: [],
        rightValues: [],
        rightTags: [],

        loading: false,
      }
    },

    created() {},
    methods: {
      // 清理值
      clearValues() {
        this.answerValues = []
        this.rightValues = []
        this.analysisShow = false
        this.rightTags = []
      },

      // 查找试卷详情
      fetchQuDetail(id) {
        // 当前赋值
        this.quId = id
        this.clearValues()
        // const mockData = {
        //   analysis: '',
        //   answerList: [
        //     {
        //       analysis: '',
        //       content: '正确',
        //       id: '1461505894155153409',
        //       image: '',
        //       isRight: true,
        //       quId: '1461505894142570497',
        //     },
        //     {
        //       analysis: '',
        //       content: '错误',
        //       id: '1461505894159347714',
        //       image: '',
        //       isRight: false,
        //       quId: '1461505894142570497',
        //     },
        //   ],
        //   content: '咖啡的故乡是非洲吗？',
        //   createTime: '2021-11-19 09:25:33',
        //   id: '1461505894142570497',
        //   image: '',
        //   level: 1,
        //   quType: 3,
        //   remark: '',
        //   repoIds: ['1265561101609795585'],
        //   updateTime: '2021-11-19 09:25:33',
        // }

        // this.quData = mockData

        fetchDetail(id).then((response) => {
          // 题目信息
          this.quData = response.data

          // 保存正确答案
          this.quData.answerList.forEach((an, index) => {
            an.abc = this.tags[index]

            // 用户选定的
            if (an.isRight) {
              this.rightValues.push(an.id)
              this.rightTags.push(an.abc)
            }
          })
        })
      },

      fetchNextQu() {
        // 查找下一个
        nextQu({ examId: this.examId, quId: this.quId.id }).then((response) => {
          if (response.data.id) {
            this.fetchQuDetail({ id: response.data.id })
          } else {
            this.close()
          }
        })
      },
      onCancel() {
        //this.$router.push({ name: 'ListTran' })
        this.$router.push({ name: 'BookList' })
      },

      handNext() {
        // 直接显示下一个
        if (this.analysisShow) {
          // 正确显示下一个
          this.fetchNextQu()
        } else {
          // 直接判断正确性
          if (this.rightValues.join(',') === this.answerValues.join(',')) {
            this.$message({
              message: '回答正确，你好棒哦！',
              type: 'success',
            })

            // 正确显示下一个

            const res = this.fetchNextQu()
            //
          } else {
            // 错误显示解析
            this.analysisShow = true

            this.$message({
              message: '很遗憾，又做错了呢，请参考答案解析！',
              type: 'error',
            })
          }
        }
      },
      showModal(data) {
        this.dialogFormVisible = true
        this.examId = data
        this.fetchNextQu()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {},
    },
  }
</script>

<style scoped>
  .qu-content div {
    line-height: 30px;
  }

  .qu-analysis p {
    color: #555;
    font-size: 14px;
  }
  .qu-analysis-line {
    margin-top: 20px;
    border-bottom: #eee 1px solid;
  }

  .el-checkbox-group label,
  .el-radio-group label {
    width: 100%;
  }
</style>
