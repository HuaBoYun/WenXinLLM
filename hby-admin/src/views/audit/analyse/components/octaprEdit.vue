<template>
  <!-- 审计情况统计表 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    v-if="dialogVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12">
          <el-form-item label="单位名称" prop="unitName">
            <el-input
              v-model="formData.unitName"
              placeholder="请输入单位名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="auditProjectName">
            <el-input
              v-model="formData.auditProjectName"
              :style="{ width: '100%' }"
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="委托中介机构名称">
            <el-input
              v-model="formData.auditFirmName"
              :style="{ width: '100%' }"
              placeholder="请输入委托中介机构名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同签订日期">
            <el-date-picker
              v-model="formData.contractSigningDate"
              type="date"
              placeholder="请输入合同签订日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额（万元）">
            <el-input
              v-model="formData.contractAmount"
              :style="{ width: '100%' }"
              placeholder="请输入合同金额（万元）"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购方式">
            <el-input
              v-model="formData.procurementMethod"
              :style="{ width: '100%' }"
              placeholder="请输入采购方式"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>服务质量评价</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="审计程序得分（满分20分）">
            <el-input-number v-model="formData.auditProcedureScore" @change="handleScore" :style="{ width: '100%' }" :min="0" :max="20" label="请输入审计程序得分（满分20分）" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计底稿得分（满分20分）">
            <el-input-number v-model="formData.auditWorkingPaperScore" @change="handleScore" :style="{ width: '100%' }" :min="0" :max="20" label="请输入审计底稿得分（满分20分）" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计报告得分（满分20分）">
            <el-input-number v-model="formData.auditReportScore" @change="handleScore" :style="{ width: '100%' }" :min="0" :max="20" label="请输入审计报告得分（满分20分）" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员工作时间的投入得分（满分20分）">
            <el-input-number v-model="formData.auditorTimeInvestmentScore" @change="handleScore" :style="{ width: '100%' }" :min="0" :max="20" label="请输入审计人员工作时间的投入得分（满分20分）" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计风险得分（满分20分）">
            <el-input-number v-model="formData.auditRiskScore" @change="handleScore" :style="{ width: '100%' }" :min="0" :max="20" label="请输入审计风险得分（满分20分）" />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both;">
          <el-form-item label="总分">
            <el-input
              v-model="formData.totalScore"
              :style="{ width: '100%' }"
              placeholder="请输入总分"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价结果">
            <el-input
              v-model="formData.evaluationResult"
              :style="{ width: '100%' }"
              placeholder="请输入评价结果"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议改进内容">
            <el-input
              v-model="formData.improvementSuggestions"
              :style="{ width: '100%' }"
              placeholder="请输入建议改进内容"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remarks"
              :style="{ width: '100%' }"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>

      </el-form>
    </el-row>

    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <project-manage2 @projectManage="getChildlistPro2" ref="manage2" />
    <cwsjxmapbOutView ref="cwsjxmapbOutView" type="report" />

    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    thirdPartyEvaluationGetDetail,
    thirdPartyEvaluationMergeInfo,
  } from '@/api/audit/analyse'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import projectManage2 from '@/components/selectPerson.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  export default {
    components: {
      SelectDepartment,
      projectManage1,
      projectManage2,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        tableData: [],
        fileData: [],
        formData: {
          auditFirmName: '',
          auditProjectName: '',
          auditProcedureScore: '',
          auditReportScore: '',
          auditRiskScore: '',
          auditWorkingPaperScore: '',
          auditorTimeInvestmentScore: '',
          contractAmount: '',
          contractSigningDate: '',
          contractSigningDateEnd: '',
          contractSigningDateStart: '',
          createTime: '',
          evaluationResult: '',
          id: '',
          improvementSuggestions: '',
          procurementMethod: '',
          remarks: '',
          totalScore: '',
          unitName: ''
        },
        rules: {
          unitName: [
            {
              required: true,
              message: '请输入单位名称',
              trigger: 'blur',
            },
          ],
          auditProjectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
        },
        dialogVisible: false,
        disabled: false,
        select: [],
        title: '新增',
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      async showEdit(row, title) {
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          // this.disabled = false
        } else if (title == 'add') {
          this.title = '新增'
        }
        this.disabled = title == 'detail'
        if (row) {
          const {
            data: { data },
          } = await thirdPartyEvaluationGetDetail({ id: row.id })
          console.log(data)
          this.formData = data
          // this.fetchData()
        }
        this.dialogVisible = true
      },
      close() {
        this.formData = {
          auditFirmName: '',
          auditProcedureScore: '',
          auditProjectName: '',
          auditReportScore: '',
          auditRiskScore: '',
          auditWorkingPaperScore: '',
          auditorTimeInvestmentScore: '',
          contractAmount: '',
          contractSigningDate: '',
          contractSigningDateEnd: '',
          contractSigningDateStart: '',
          createTime: '',
          evaluationResult: '',
          id: '',
          improvementSuggestions: '',
          procurementMethod: '',
          remarks: '',
          totalScore: '',
          unitName: ''
        }
        this.tableData = []
        this.select = []
        this.dialogVisible = false
        this.$emit('fetchData')
      },
      handleScore() {
        this.formData.totalScore = this.formData.auditProcedureScore * 1 + this.formData.auditReportScore * 1 + this.formData.auditRiskScore * 1 + this.formData.auditWorkingPaperScore * 1 + this.formData.auditorTimeInvestmentScore * 1
        if(this.formData.totalScore >= 90) {
          this.formData.evaluationResult = '优秀'
        } else if (this.formData.totalScore > 80 && this.formData.totalScore < 90) {
          this.formData.evaluationResult = '良好'
        } else if (this.formData.totalScore > 60 && this.formData.totalScore < 80) {
          this.formData.evaluationResult = '一般'
        } else {
          this.formData.evaluationResult = '差'
        }
      },
      handleEdit(row, i) {
        this.$refs['edit'].showEdit({ id: row.id, index: i + 1 }, 'edit')
      },

      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.name
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await thirdPartyEvaluationMergeInfo({
              ...this.formData,
            })
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.tableData = [...this.tableData, ...response.data.data]
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },

      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, 'detail')
      },
      handleCwxmDetail(row, index) {
        if (row.gljhxmlx == '23') {
          this.$refs['cwsjxmapbOutView'].showEdit({ data: row })
        } else {
          this.$refs['edit'].showEdit(row, 'edit')
        }
      },
      getChildlistPro1() {},
      getChildlistPro2() {},
      push1() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage1'].showEdit()
      },
      push2() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage2'].showEdit()
      },

      async getChildlistPro2(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()

      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        this.select = val
        // const curSelected = val.filter((x) => !!x)
        // if (curSelected && curSelected.length) {
        //   curSelected.map((row) => {
        //     if (row && !this.select.some((x) => x.id == row.id)) {
        //       this.select.push(row)
        //     }
        //   })
        // } else {
        //   this.list.map((row) => {
        //     const i = this.select.findIndex((x) => x.id == row.id)
        //     if (i >= 0) {
        //       this.select.splice(i, 1)
        //     }
        //   })
        // }
      },

      xiafa(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.name)
        const names = val.map((res) => res.staffid)
        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }

        //下发通知
        xiafaListNew({
          tableId: '579594969821253',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.select = []
            // this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.multipleSelection = []
          }
        })
      },

      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.fileData
          list.push(file.data)
          this.fileData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
