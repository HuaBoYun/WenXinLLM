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
        label-width="150px"
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
          <el-form-item label="审计项目类型">
            <el-input
              v-model="formData.auditProjectType"
              :style="{ width: '100%' }"
              placeholder="请输入审计项目类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位全称">
            <el-input
              v-model="formData.auditedEntityName"
              :style="{ width: '100%' }"
              placeholder="请输入被审计单位全称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位级次">
            <el-input
              v-model="formData.auditedEntityLevel"
              :style="{ width: '100%' }"
              placeholder="请输入被审计单位级次"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计内容">
            <el-input
              v-model="formData.auditContent"
              :style="{ width: '100%' }"
              placeholder="请输入审计内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计组织方式">
            <el-input
              v-model="formData.auditOrganizationMode"
              :style="{ width: '100%' }"
              placeholder="请输入审计组织方式"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长">
            <el-input
              v-model="formData.teamLeader"
              :style="{ width: '100%' }"
              placeholder="请输入组长"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审">
            <el-input
              v-model="formData.principalAuditor"
              :style="{ width: '100%' }"
              placeholder="请输入主审"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>审计规模</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="审计工作量（人日）">
            <el-input
              v-model="formData.auditWorkload"
              :style="{ width: '100%' }"
              placeholder="请输入审计工作量（人日）"
              @input="inputNum($event, 'auditWorkload')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计资金量（万元）">
            <el-input
              v-model="formData.auditFundAmount"
              :style="{ width: '100%' }"
              placeholder="请输入审计资金量（万元）"
              @input="inputNum($event, 'auditFundAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其中境外审计资金量（万元）">
            <el-input
              v-model="formData.overseasAuditFundAmount"
              :style="{ width: '100%' }"
              placeholder="请输入其中境外审计资金量（万元）"
              @input="inputNum($event, 'overseasAuditFundAmount')"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>计划执行情况</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="截至目前审计状态">
            <el-input
              v-model="formData.currentAuditStatus"
              :style="{ width: '100%' }"
              placeholder="请输入截至目前审计状态"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划完成现场审计时间">
            <el-date-picker
              v-model="formData.plannedOnSiteAuditCompletionDate"
              type="date"
              placeholder="请输入计划完成现场审计时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否已完成现场审计">
            <el-select
              v-model="formData.isOnSiteAuditCompleted"
              placeholder="请选择是否已完成现场审计"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划审计报告出具时间">
            <el-date-picker
              v-model="formData.plannedAuditReportIssueDate"
              type="date"
              placeholder="请输入计划审计报告出具时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否已出具审计报告">
            <el-select
              v-model="formData.isAuditReportIssued"
              placeholder="请选择是否已出具审计报告"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
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
    auditPlanExecutionStatisticsGetDetail,
    auditPlanExecutionStatisticsMergeInfo,
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
          auditContent: '',
          auditFundAmount: '',
          auditOrganizationMode: '',
          auditProjectName: '',
          auditProjectType: '',
          auditWorkload: '',
          auditedEntityLevel: '',
          auditedEntityName: '',
          createTime: '',
          currentAuditStatus: '',
          id: '',
          isAuditReportIssued: '',
          isOnSiteAuditCompleted: '',
          overseasAuditFundAmount: '',
          plannedAuditReportIssueDate: '',
          plannedAuditReportIssueDateEnd: '',
          plannedAuditReportIssueDateStart: '',
          plannedOnSiteAuditCompletionDate: '',
          plannedOnSiteAuditCompletionDateEnd: '',
          plannedOnSiteAuditCompletionDateStart: '',
          principalAuditor: '',
          remarks: '',
          teamLeader: '',
          unitName: '',
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
           //数字输入
     inputNum(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        // if (
        //   sanitizedValue.startsWith('0') &&
        //   sanitizedValue.length > 1 &&
        //   sanitizedValue[1] !== '.'
        // ) {
        //   sanitizedValue = sanitizedValue.slice(1)
        // }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        // if (sanitizedValue === '' || sanitizedValue === '0') {
        //   sanitizedValue = ''
        // }
        // 更新输入框的值
        this.formData[key] = sanitizedValue
      },
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
          } = await auditPlanExecutionStatisticsGetDetail({ id: row.id })
          console.log(data)
          this.formData = data
          // this.fetchData()
        }
        this.dialogVisible = true
      },
      close() {
        this.formData = {
          auditContent: '',
          auditFundAmount: '',
          auditOrganizationMode: '',
          auditProjectName: '',
          auditProjectType: '',
          auditWorkload: '',
          auditedEntityLevel: '',
          auditedEntityName: '',
          createTime: '',
          currentAuditStatus: '',
          id: '',
          isAuditReportIssued: '',
          isOnSiteAuditCompleted: '',
          overseasAuditFundAmount: '',
          plannedAuditReportIssueDate: '',
          plannedAuditReportIssueDateEnd: '',
          plannedAuditReportIssueDateStart: '',
          plannedOnSiteAuditCompletionDate: '',
          plannedOnSiteAuditCompletionDateEnd: '',
          plannedOnSiteAuditCompletionDateStart: '',
          principalAuditor: '',
          remarks: '',
          teamLeader: '',
          unitName: ''
        }
        this.tableData = []
        this.select = []
        this.dialogVisible = false
        this.$emit('fetchData')
      },
      add(row, i) {
        this.$refs['edit'].showEdit(null, 'add')
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
            const res = await auditPlanExecutionStatisticsMergeInfo({
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
