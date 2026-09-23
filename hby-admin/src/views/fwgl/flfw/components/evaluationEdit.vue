<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      v-if="dialogFormVisible"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close()"
      v-loading="loading"
    >
      <el-row :gutter="15">
        <el-form
          ref="form"
          :class="{ disabled: disabled }"
          :disabled="disabled"
          label-width="125px"
          :model="formData"
        >
          <el-col :span="13">
            <el-form-item label="聘用单位" prop="hireUnitName">
              <el-input
                v-model.trim="formData.hireUnitName"
                placeholder="请选择聘用单位"
                :style="{ width: '80%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="showGroupLeader()"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事务所名称" prop="organizationName">
              {{ formData.organizationName }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="委托期限" prop="entrustTimeLimit">
              {{ formData.entrustTimeLimit }}
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <el-table
        :data="tableData"
        border
        show-summary
        :summary-method="getSummaries"
        style="width: 100%"
      >
        <el-table-column
          align="center"
          prop="project"
          label="项目"
          width="180"
        ></el-table-column>
        <el-table-column
          align="center"
          prop="consideration"
          label="考量因素"
        ></el-table-column>
        <el-table-column align="center" prop="score" label="评分">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.score"
              size="mini"
              type="number"
              style="width: 90%"
              :disabled="disabled"
              @input="handleInput(scope.$index, scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="grading"
          label="分值"
        ></el-table-column>
        <el-table-column
          align="center"
          prop="remark"
          label="备注"
        ></el-table-column>
      </el-table>

      <el-row :gutter="15">
        <el-form
          ref="form"
          :class="{ disabled: disabled }"
          :disabled="disabled"
          label-width="125px"
          :model="formData"
          :style="{ marginTop: '30px' }"
        >
          <el-col :span="24">
            <el-form-item label="考核结果" prop="examineGrade">
              {{ formData.examineGrade }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="其他意见或建议" prop="opinions">
              <el-input
                v-model="formData.opinions"
                clearable
                type="textarea"
                row="3"
                placeholder="请输入其他意见或建议"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <template v-if="!disabled" #footer>
        <el-button @click="close()">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>

    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
  </div>
</template>
<script>
  // import {
  //   createData,
  //   editPersonData,
  //   getDefaultPersonInfo,
  // } from '@/api/audit/structure'
  import { legalServiceGrade, fetchApi } from '@/api/fwgl/api'
  import CompanySelectModal from '@/components/CampanySelectModal'

  const { saveOrUpdate, detail } = legalServiceGrade

  export default {
    name: 'xxxx',
    components: { CompanySelectModal },
    data() {
      return {
        title: '新增',
        loading: false,
        dialogFormVisible: false,
        disabled: false,
        formData: {
          hireUnitName: '',
          hireUnit: '',
          organizationName: '',
          entrustTimeLimit: '',
          examineGrade: '不满意',
          opinions: '',
        },
        disabled: false,
        tableData: [
          {
            project: '服务质量',
            consideration:
              '是否按照约定完成工作、工作成效、服务团队的工作态度、文书质量及常年法律服务报告提交是否及时等;',
            score: 0,
            grading: 55,
            remark: '40分以下为不满意，需说明原因',
          },
          {
            project: '沟通合作',
            consideration: '服务团队的响应速度、工作配合度及沟通是否顺畅等;',
            score: 0,
            grading: 40,
            remark: '25分以下为不满意，需说明原因',
          },
          {
            project: '增值服务',
            consideration:
              '是否提供法律培训并协助进行法治宣传教育、是否协助处理各类历史遗留问题及是否提供最新法律法规和案例的相关重要提示等。',
            score: 0,
            grading: 5,
            remark: '根据律师事务所提供的增值服务情况进行打分;',
          },
        ],
        initialTable: [
          {
            project: '服务质量',
            consideration:
              '是否按照约定完成工作、工作成效、服务团队的工作态度、文书质量及常年法律服务报告提交是否及时等;',
            score: 0,
            grading: 55,
            remark: '40分以下为不满意，需说明原因',
          },
          {
            project: '沟通合作',
            consideration: '服务团队的响应速度、工作配合度及沟通是否顺畅等;',
            score: 0,
            grading: 40,
            remark: '25分以下为不满意，需说明原因',
          },
          {
            project: '增值服务',
            consideration:
              '是否提供法律培训并协助进行法治宣传教育、是否协助处理各类历史遗留问题及是否提供最新法律法规和案例的相关重要提示等。',
            score: 0,
            grading: 5,
            remark: '根据律师事务所提供的增值服务情况进行打分;',
          },
        ],
        totalScore: 0,
        personEditId: undefined,
      }
    },
    watch: {
      totalScore: {
        handler(newVal) {
          if (newVal >= 80) this.formData.examineGrade = '满意'
          else this.formData.examineGrade = '不满意'
        },
      },
    },
    methods: {
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
        this.tableData = JSON.parse(JSON.stringify(this.initialTable))
        this.disabled = false
      },
      /**
       * @description: 打开选择组件
       * @return {*}
       */      
      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'hireUnitName',
          idKey: 'hireUnit',
          title: '聘用单位',
        })
      },
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, val.idKey, val.id)
        this.$set(this.formData, val.labelKey, val.label)
      },
      async submitForm() {
        const info = {
          ...this.formData,
          detailListJson: JSON.stringify(this.tableData),
          totalScore: this.totalScore,
        }

        if (!this.formData.hireUnit) return this.$alert('请选择聘用单位')
        else if (!this.formData.opinions) return this.$alert('请填写意见')

        this.loading = true
        const res = await fetchApi(saveOrUpdate, { ...info })
        this.loading = false
        if (res && res.code === 200) {
          this.$baseMessage('保存成功', 'success')
          if (res.data) {
            this.$emit('on-save-success', {
              key: 'gradeId',
              rowItem: {
                ...res.data,
                hireUnitName: this.formData.hireUnitName,
              },
            })
          }
        } else {
          this.$baseMessage('操作失败！', 'error')
        }
        this.close()
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @param {*} parentData 其他数据
       * @return {*}
       */      
      async showEdit(title, row, parentData) {
        this.dialogFormVisible = true

        this.formData.organizationName = parentData.organizationName
        this.formData.entrustTimeLimit =
          parentData.employmentTermStartTime +
          ' - ' +
          parentData.employmentTermEndTime

        if (row && row.id) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })
          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data
            this.$set(this, 'formData', {
              id: res.data.id,
              hireUnitName: res.data.hireUnitName || '',
              hireUnit: res.data.hireUnit || '',
              organizationName: res.data.organizationName || '',
              entrustTimeLimit: res.data.entrustTimeLimit || '',
              examineGrade: res.data.examineGrade || '不满意',
              opinions: res.data.opinions || '',
            })
            this.totalScore = Number(res.data.totalScore)
            this.tableData = JSON.parse(res.data.detailListJson)
          }
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = true
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 表格中输入框input方法
       * @param {*} a 行索引
       * @param {*} b 行数据
       * @return {*}
       */      
      handleInput(a, b) {
        //a是索引
        if (b.score > b.grading) b.score = b.grading
        else if (b.score < 0) b.score = 0
        this.tableData[a] = b
      },
      openPersonModal() {
        this.$refs['person'].showEdit()
      },
      openProjectModal() {
        if (!this.formData.realname) {
          this.$message.error('请先选择人员')
          return
        }
        this.$refs['project'].showEdit()
      },
      handlePersonInfo(v) {
        this.$set(this.formData, `auditors`, v[0].staffid)
        this.$set(this.formData, `realname`, v[0].realname)
      },
      handleProjectInfo(v) {
        this.$set(this.formData, `projectid`, v[0].projectId)
        this.$set(this.formData, `auditProjectName`, v[0].prjoectName)
      },
      /**
       * @description: 计算总分
       * @param {*} param 表格中的数据
       * @return {*}
       */      
      getSummaries(param) {
        const { columns, data } = param
        let aaScore = 0
        for (let i = 0; i < data.length; i++) {
          aaScore += +data[i].score
        }
        this.totalScore = aaScore
        const sums = []
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '总分'
            return
          }
          if (index === 1) {
            sums[index] = '/'
            return
          }
          if (index === 2) {
            sums[index] = this.totalScore + '分'
            return
          }
          if (index === 3) {
            sums[index] = '/'
            return
          }
          if (index === 4) {
            sums[index] = '/'
            return
          }
        })
        return sums
      },
    },
  }
</script>

<style scoped>
  ::v-deep input::-webkit-outer-spin-button,
  ::v-deep input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
  }
  ::v-deep input[type='number'] {
    -moz-appearance: textfield;
  }
</style>
