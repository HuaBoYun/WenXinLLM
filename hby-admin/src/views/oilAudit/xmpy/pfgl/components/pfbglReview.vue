<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1200px"
      @close="close"
    >
      <div class="reviewContent">
        <!-- 外部监管考核 -->
        <el-table
          v-if="examineType === 1"
          :data="tableData"
          :span-method="objectSpanMethod"
          :key="tableKey1"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column prop="examineEmphasis" label="考评重点" width="170">
            <template #default="{ row }">
              <div style="text-align: right">
                {{ row.examineEmphasis }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="totalScore" label="分值" width="60">
            <template #default="{ row }">
              {{ row.totalScore }}
            </template>
          </el-table-column>
          <el-table-column prop="secondContent" label="考评内容" width="220">
            <template #default="{ row }">
              {{ row.secondContent }}
            </template>
          </el-table-column>
          <el-table-column prop="sonGradeCriterion" label="评分标准">
            <template #default="{ row }">
              {{
                row.sonGradeCriterion
                  ? row.sonGradeCriterion + ' (' + row.sonScore + '分）'
                  : ''
              }}
            </template>
          </el-table-column>

          <!-- <el-table-column prop="scope" label="自评分" width="100">
            <template #default="{ row }">
              <el-input-number
                v-if="row.id !== 'total'"
                placeholder="分数"
                controls-position="right"
                @change="handleChange(row.deductMarks, row)"
                v-model="row.deductMarks"
                :min="1"
                :max="Number(row.sonScore)"
              ></el-input-number>
              <template v-else>
                <div style="text-align: center">{{ row.deductMarks }}</div>
              </template>
            </template>
          </el-table-column>
          <el-table-column
            prop="scope"
            label="相关材料情况（上传附件）"
            width="100"
          >
            <template>
              <el-button type="primary" disabled>查看</el-button>
            </template>
          </el-table-column> -->
        </el-table>

        <!-- 子单位考核 -->
        <el-table
          v-else
          :data="tableData"
          :span-method="objectSpanMethod"
          border
          style="width: 100%"
          :key="tableKey2"
          v-loading="loading"
        >
          <el-table-column prop="ONE" label="序号">
            <template #default="{ row }">
              {{ IndexTypes[row.INDEX] }}
            </template>
          </el-table-column>
          <el-table-column prop="examineEmphasis" label="工作目标">
            <template #default="{ row }">
              {{ row.examineEmphasis }}
            </template>
          </el-table-column>
          <el-table-column prop="THREE" label="工作内容" width="400">
            <template #default="{ row }">
              {{ row.sonContent }}
            </template>
          </el-table-column>
          <el-table-column prop="FOUR" label="执行主体">
            <template #default="{ row }">
              {{ row.sonExecutiveBody }}
            </template>
          </el-table-column>
          <el-table-column prop="FIVE" label="时限">
            <template #default="{ row }">
              {{ row.sonTimeLimit }}
            </template>
          </el-table-column>
          <el-table-column prop="score" label="随属公司考核分值">
            <template #default="{ row }">
              {{ row.sonScore !== undefined ? row.sonScore + '分' : '' }}
            </template>
          </el-table-column>
          <el-table-column prop="score" label="自评分" width="100">
            <template #default="{ row }">
              <el-input-number
                v-if="row.id !== 'total'"
                placeholder="分数"
                controls-position="right"
                @change="handleChange(row.selfGrade, row)"
                v-model="row.selfGrade"
                :min="1"
                :max="Number(row.sonScore)"
              ></el-input-number>
              <template v-else>{{ row.selfGrade }}</template>
            </template>
          </el-table-column>
          <el-table-column prop="scope" label="总部评分" width="100">
            <template #default="{ row }">
              <el-input-number
                v-if="row.id !== 'total'"
                placeholder="分数"
                controls-position="right"
                @change="handleChange(row.headOfficeGrade, row)"
                v-model="row.headOfficeGrade"
                :min="0"
                :max="Number(row.sonScore)"
              ></el-input-number>
              <template v-else>{{ row.headOfficeGrade }}</template>
            </template>
          </el-table-column>
          <el-table-column
            prop="scope"
            label="相关材料情况（上传附件）"
            width="100"
          >
            <template>
              <el-button type="primary" disabled>查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <template #footer>
        <el-button @click="close">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { getDetail } from '@/oapi/fwgl/pfgl/pfbgl'
  import { dataNoRepeat, formatRowSpan } from '@/utils/common'

  export default {
    name: 'pfbglReview',
    data() {
      return {
        title: '评分表预览',
        loading: false,
        dialogFormVisible: false,
        examineType: 1,
        firstList: [],
        tableData: [],
        tableKey1: 'a',
        tableKey2: 'b',
        IndexTypes: [
          '一',
          '二',
          '三',
          '四',
          '五',
          '六',
          '七',
          '八',
          '九',
          '十',
          '十一',
          '十二',
          '十三',
          '十四',
          '十五',
          '十六',
          '十七',
          '十八',
          '十九',
          '二十',
        ],
      }
    },
    created() {},
    methods: {
      showModal(data) {
        this.dialogFormVisible = true
        if (data) {
          this.scoreTransaction = data.scoreTransaction
          this.loading = true
          getDetail({ id: data.scoreTransaction })
            .then(async (res) => {
              this.loading = false
              if (res && res.data && res.data.length) {
                this.examineType = res.data[0].examineType
                this.firstList = dataNoRepeat(res.data)
                this.tableData = formatRowSpan(this.firstList)

                if (this.examineType === 1) {
                  this.tableData.push({
                    id: 'total',
                    examineEmphasis: '合计',
                    deductMarks: 0,
                    colSpan: [1, 1, 1, 1],
                    rowSpan: [1, 1, 1, 1],
                  })
                } else {
                  this.tableData.push({
                    id: 'total',
                    examineEmphasis: '合计',
                    selfGrade: 0,
                    headOfficeGrade: 0,
                    colSpan: [1, 1, 1, 1, 1, 1],
                    rowSpan: [1, 1, 1, 1, 1, 1],
                  })
                }

                this.tableKey1 = 'aaa'
                this.tableKey2 = 'bbb'
              }
            })
            .catch((err) => {
              this.loading = false
            })
        }
      },
      handleChange(v, row) {
        this.countTotalScore()
      },
      countTotalScore() {
        let deductMarks = 0
        let selfGrade = 0
        let headOfficeGrade = 0

        this.tableData.map((x) => {
          if (x.id !== 'total') {
            if (this.examineType === 1) {
              deductMarks += x.deductMarks || 0
            } else {
              selfGrade += x.selfGrade || 0
              headOfficeGrade += x.headOfficeGrade || 0
            }
          } else {
            x.deductMarks = deductMarks
            x.selfGrade = selfGrade
            x.headOfficeGrade = headOfficeGrade
          }
        })
      },
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        const rowSpan = row.rowSpan[columnIndex]
        const colSpan = row.colSpan[columnIndex]
        if (columnIndex <= 3) {
          return {
            rowspan: rowSpan,
            colspan: colSpan,
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1,
          }
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style scoped>
  .reviewContent /deep/ .el-input-number__decrease,
  .reviewContent /deep/ .el-input-number__increase {
    display: none !important;
  }
  .reviewContent /deep/ .el-input-number {
    width: 100% !important;
  }
  .reviewContent /deep/ .el-input-number input {
    padding-right: 15px !important;
  }
</style>
