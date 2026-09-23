<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1200px"
      @close="close"
      v-if="formType !== 'review'"
    >
      <div class="reviewContent">
        <div style="margin-bottom: 20px">
          <el-input
            v-model="fillBelongGroupName"
            disabled
            placeholder="请输入填报单位"
            :style="{ width: '20%' }"
          />
          <el-button
            @click="openSelectDep"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </div>
        <!-- 审计工作质量评估 -->
        <el-table
          :data="tableData"
          :span-method="objectSpanMethod"
          :key="tableKey"
          border
          style="width: 100%"
          v-loading="loading"
          show-summary
          :summary-method="getSummaries"
        >
          <el-table-column prop="examineEmphasis" label="评估类别" width="170">
            <template #default="{ row }">
              <div style="text-align: right">
                {{ row.examineEmphasis }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="totalScore" label="总分值" width="70">
            <template #default="{ row }">
              {{ row.totalScore }}
            </template>
          </el-table-column>
          <el-table-column prop="secondContent" label="评估要素" width="220">
            <template #default="{ row }">
              {{ row.secondContent }}
            </template>
          </el-table-column>
          <el-table-column prop="sonGradeCriterion" label="集团公司评估要点" />
          <el-table-column prop="sonScore" label="分值" width="60">
            <template #default="{ row }">
              {{ row.sonScore }}
            </template>
          </el-table-column>

          <el-table-column
            prop="scope"
            label="操作"
            width="100"
            #default="{ row }"
            v-if="formType !== 'review'"
          >
            <template v-if="row.sonScore">
              <el-button type="primary" @click="onScore(row)">评分</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <template #footer>
        <el-button type="primary" @click="save" v-if="formType === 'edit'">
          确 定
        </el-button>
        <el-button @click="close">关 闭</el-button>
      </template>

      <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
      <scoreForm ref="scoreForm" @submit="scoreFormSubmit" />
    </el-dialog>

    <div class="reviewContent" v-else>
      <!-- 审计工作质量评估 -->
      <el-table
        :data="tableData"
        :span-method="objectSpanMethod"
        :key="tableKey"
        border
        style="width: 100%"
        v-loading="loading"
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column prop="examineEmphasis" label="评估类别" width="170">
          <template #default="{ row }">
            <div style="text-align: right">
              {{ row.examineEmphasis }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总分值" width="70">
          <template #default="{ row }">
            {{ row.totalScore }}
          </template>
        </el-table-column>
        <el-table-column prop="secondContent" label="评估要素" width="220">
          <template #default="{ row }">
            {{ row.secondContent }}
          </template>
        </el-table-column>
        <el-table-column prop="sonGradeCriterion" label="集团公司评估要点" />
        <el-table-column prop="sonScore" label="分值" width="60">
          <template #default="{ row }">
            {{ row.sonScore }}
          </template>
        </el-table-column>
        <el-table-column
          prop="evaluationScore"
          label="评估分值"
          width="80"
          v-if="formType === 'review'"
        >
          <template #default="{ row }">
            {{ row.scoreForm ? row.scoreForm.evaluationScore : '' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="adjustedScore"
          label="调整后得分"
          width="100"
          v-if="formType === 'review'"
        >
          <template #default="{ row }">
            {{ row.scoreForm ? row.scoreForm.adjustedScore : '' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="evaluationExplanationAudit"
          label="评估说明"
          width="120"
          v-if="formType === 'review'"
        >
          <template #default="{ row }">
            {{ row.scoreForm ? row.scoreForm.evaluationExplanationAudit : '' }}
          </template>
        </el-table-column>

        <el-table-column
          prop="scope"
          label="操作"
          width="100"
          #default="{ row }"
          v-if="formType !== 'review'"
        >
          <template v-if="row.sonScore">
            <el-button type="primary" @click="onScore(row)">评分</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import { getDetail } from '@/oapi/fwgl/pfgl/pfbgl'
  import { dataNoRepeat, formatRowSpan } from '@/utils/common'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import scoreForm from '@/views/oilAudit/xmpy/sjgzzlpg/scoreForm.vue'
  import { saveOrupdate, getItemDetail } from '@/api/oilAudit/xmpy/sjgzzlpg.js'

  export default {
    name: 'pfbglReview',
    components: { SelectDepartment, scoreForm },
    data() {
      return {
        title: '评分表',
        loading: false,
        dialogFormVisible: false,
        firstList: [],
        tableData: [],
        tableKey: 'a',
        oprationDisabeld: false,
        scoreId: '1843664195120943104',
        fillBelongGroupName: '',
        fillBelongGroup: '',
        id: '',
        formType: '',
      }
    },
    created() {
      if (this.$route.query.id) {
        this.showEdit('review', { ...this.$route.query })
      }
    },
    methods: {
      async showEdit(type, row = {}) {
        this.oprationDisabeld = type === 'detail'
        this.formType = type
        this.dialogFormVisible = true
        this.loading = true
        await getDetail({ id: this.scoreId })
          .then(async (res) => {
            this.loading = false
            if (res && res.data && res.data.length) {
              this.firstList = dataNoRepeat(res.data)
              this.tableData = formatRowSpan(this.firstList)
              this.tableKey = new Date().getTime()
            }
          })
          .finally(() => {
            this.loading = false
          })

        // 获取详情数据~
        if (row && row.id) {
          this.id = row.id
          this.loading = true
          await getItemDetail({ id: row.id })
            .then((res) => {
              if (res && res.code === 200 && res.data) {
                this.fillBelongGroup = res.data.fillBelongGroup
                this.fillBelongGroupName = res.data.fillBelongGroupName
                this.tableData.map((tbItem, i) => {
                  const ql = res.data.qualityAssessmentExtList
                  if (ql && ql.length) {
                    const qlItem = ql.find((x) => x.extId === tbItem.id)
                    if (qlItem) {
                      tbItem.scoreForm = {
                        fillRequire: qlItem.fillRequire,
                        evaluationScore: Number(qlItem.evaluationScore),
                        adjustedScore: Number(qlItem.adjustedScore),
                        evaluationExplanationRegion:
                          qlItem.evaluationExplanationRegion,
                        evaluationExplanationAudit:
                          qlItem.evaluationExplanationAudit,
                        evaluationReference: qlItem.evaluationReference,
                        fileIds: qlItem.fileIds,
                        fileList: qlItem.fileList,
                        id: qlItem.id,
                      }
                    }
                  }
                })
                this.tableKey = new Date().getTime()
              }
            })
            .finally(() => {
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
            deductMarks += x.deductMarks || 0
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
      openSelectDep() {
        this.$refs.audiTree.showEdit()
      },
      getDepartmentInfo(val) {
        this.fillBelongGroupName = val.label
        this.fillBelongGroup = val.id
      },
      onScore(row) {
        const type = this.oprationDisabeld ? 'detail' : 'add'
        this.$refs.scoreForm.showEdit(type, row)
      },
      scoreFormSubmit(data) {
        const activeRow = this.tableData.find((x) => x.id === data.parentId)
        if (activeRow) {
          activeRow.scoreForm = data
        }
        this.tableKey = new Date().getTime()
      },
      async save() {
        if (!this.fillBelongGroupName)
          return this.$message.error('请选择填报单位！')

        // 打开注释，则限制每一项必填
        // const valid = this.tableData.some(x => {
        //   if (!x.scoreForm || (
        //     !x.scoreForm.fillRequire ||
        //     !x.scoreForm.evaluationScore ||
        //     !x.scoreForm.adjustedScore ||
        //     !x.scoreForm.evaluationExplanationRegion ||
        //     !x.scoreForm.evaluationExplanationAudit ||
        //     !x.scoreForm.evaluationReference
        //   )) {
        //     this.$message.error(x.sonGradeCriterion + '，未评分')
        //     return true
        //   }
        // })
        // if (valid) return

        let evaluationScoreTotal = 0
        const qualityAssessmentExtList = this.tableData.map((x) => {
          const scoreForm = x.scoreForm || {}
          if (scoreForm.evaluationScore || scoreForm.evaluationScore === 0) {
            evaluationScoreTotal += Number(scoreForm.evaluationScore)
          }
          return {
            ...scoreForm,
            extId: x.id,
            one: x.examineEmphasis,
            two: x.totalScore,
            three: x.secondContent,
            four: x.sonGradeCriterion,
            five: x.sonScore,
          }
        })
        const params = {
          fillBelongGroup: this.fillBelongGroup,
          fillBelongGroupName: this.fillBelongGroupName,
          id: this.id ? this.id : '',
          qualityAssessmentExtList: qualityAssessmentExtList,
          subjectId: this.scoreId,
          totalScore: evaluationScoreTotal,
        }

        this.loading = true
        saveOrupdate(params)
          .then((res) => {
            this.$message.success('保存成功！')
            this.$emit('fetchData')
            this.close()
          })
          .finally(() => {
            this.loading = false
          })
      },
      getSummaries(param) {
        const { columns, data } = param
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '合计'
            return
          }
          if (index === 1) {
            sums[index] = ''
            return
          }
          let values = []
          if (index === 5 || index === 6) {
            values = data.map((item) =>
              Number(item['scoreForm'][column.property])
            )
          } else {
            values = data.map((item) => Number(item[column.property]))
          }
          if (!values.every((value) => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
          } else {
            sums[index] = ''
          }
        })

        return sums
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.fillBelongGroupName = ''
        this.fillBelongGroup = ''
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
