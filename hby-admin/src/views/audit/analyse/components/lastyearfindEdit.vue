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
          <el-form-item label="整改责任单位" prop="rectificationResponsibleUnit">
            <el-input
              v-model="formData.rectificationResponsibleUnit"
              placeholder="请输入整改责任单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计整改类型" prop="auditRectificationType">
            <el-input
              v-model="formData.auditRectificationType"
              :style="{ width: '100%' }"
              placeholder="请输入审计整改类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称">
            <el-input
              v-model="formData.auditProjectName"
              :style="{ width: '100%' }"
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计发现问题简述">
            <el-input
              v-model="formData.issueDescription"
              :style="{ width: '100%' }"
              placeholder="请输入审计发现问题简述"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改完成标准">
            <el-input
              v-model="formData.rectificationCompletionStandard"
              :style="{ width: '100%' }"
              placeholder="请输入整改完成标准"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="细化的整改措施">
            <el-input
              v-model="formData.detailedRectificationMeasures"
              :style="{ width: '100%' }"
              placeholder="请输入细化的整改措施"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计整改期限">
            <el-date-picker
              v-model="formData.rectificationDeadline"
              type="date"
              placeholder="请输入审计整改期限"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>具体责任单位整改责任人</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改第一责任人">
            <el-input
              v-model="formData.primaryRectificationResponsiblePerson"
              :style="{ width: '100%' }"
              placeholder="请输入整改第一责任人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协助整改工作的领导">
            <el-input
              v-model="formData.assistingRectificationLeader"
              :style="{ width: '100%' }"
              placeholder="请输入协助整改工作的领导"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="牵头整改部门责任人及联系电话">
            <el-input
              v-model="formData.leadRectificationDepartmentContact"
              :style="{ width: '100%' }"
              placeholder="请输入牵头整改部门责任人及联系电话"
              @input="inputNum($event, 'leadRectificationDepartmentContact')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配置整改部门责任人及联系电话">
            <el-input
              v-model="formData.coordinatingRectificationDepartmentContact"
              :style="{ width: '100%' }"
              placeholder="请输入配置整改部门责任人及联系电话"
              @input="inputNum($event, 'coordinatingRectificationDepartmentContact')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计部门责任人及联系电话">
            <el-input
              v-model="formData.auditDepartmentContact"
              :style="{ width: '100%' }"
              placeholder="请输入审计部门责任人及联系电话"
              @input="inputNum($event, 'auditDepartmentContact')"
            />
          </el-form-item>
        </el-col>


        <el-col :span="24" style="margin-top: 16px">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="截至目前整改措施落实情况描述">
            <el-input
              v-model="formData.rectificationProgressDescription"
              :style="{ width: '100%' }"
              placeholder="请输入截至目前整改措施落实情况描述"
            />
          </el-form-item>
        </el-col>


        <el-col :span="24" style="margin-top: 16px">
          <el-divider>涉及金额的整改措施</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合计（万元）">
            <el-input
              v-model="formData.totalMonetaryRectificationAmount"
              :style="{ width: '100%' }"
              placeholder="请输入合计（万元）"
              @input="inputNum($event, 'totalMonetaryRectificationAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调整会计账目（万元）">
            <el-input
              v-model="formData.accountingAdjustmentAmount"
              :style="{ width: '100%' }"
              placeholder="请输入调整会计账目（万元）"
              @input="inputNum($event, 'accountingAdjustmentAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="挽回损失（万元）">
            <el-input
              v-model="formData.lossRecoveryAmount"
              :style="{ width: '100%' }"
              placeholder="请输入挽回损失（万元）"
              @input="inputNum($event, 'lossRecoveryAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归还原资金渠道（万元）">
            <el-input
              v-model="formData.originalFundChannelReturnAmount"
              :style="{ width: '100%' }"
              placeholder="请输入归还原资金渠道（万元）"
              @input="inputNum($event, 'originalFundChannelReturnAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="补缴税费（万元）">
            <el-input
              v-model="formData.taxSupplementAmount"
              :style="{ width: '100%' }"
              placeholder="请输入补缴税费（万元）"
              @input="inputNum($event, 'taxSupplementAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他（万元）">
            <el-input
              v-model="formData.otherMonetaryRectificationAmount"
              :style="{ width: '100%' }"
              placeholder="请输入其他（万元）"
              @input="inputNum($event, 'otherMonetaryRectificationAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他的具体方式">
            <el-input
              v-model="formData.otherMonetaryRectificationMethod"
              :style="{ width: '100%' }"
              placeholder="请输入其他的具体方式"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>不涉及金额的整改措施</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="新建指定制度（个）">
            <el-input
              v-model="formData.newSystemsEstablished"
              :style="{ width: '100%' }"
              placeholder="请输入新建指定制度（个）"
              @input="inputNum($event, 'newSystemsEstablished')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="修订完善制度（个）">
            <el-input
              v-model="formData.existingSystemsImproved"
              :style="{ width: '100%' }"
              placeholder="请输入修订完善制度（个）"
              @input="inputNum($event, 'existingSystemsImproved')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优化完善业务流程（个）">
            <el-input
              v-model="formData.businessProcessesOptimized"
              :style="{ width: '100%' }"
              placeholder="请输入优化完善业务流程（个）"
              @input="inputNum($event, 'businessProcessesOptimized')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他措施（个）">
            <el-input
              v-model="formData.otherNonMonetaryRectificationMeasures"
              :style="{ width: '100%' }"
              placeholder="请输入其他措施（个）"
              @input="inputNum($event, 'otherNonMonetaryRectificationMeasures')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="新建、修订制度名称、优化完善业务流程及其他措施具体情况">
            <el-input
              v-model="formData.nonMonetaryRectificationDetails"
              :style="{ width: '100%' }"
              placeholder="请输入新建、修订制度名称、优化完善业务流程及其他措施具体情况"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>追责问题情况</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="组织处理（人次）">
            <el-input
              v-model="formData.personnelActions"
              :style="{ width: '100%' }"
              placeholder="请输入组织处理（人次）"
              @input="inputNum($event, 'personnelActions')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="扣减薪酬（万元）">
            <el-input
              v-model="formData.salaryDeductionAmount"
              :style="{ width: '100%' }"
              placeholder="请输入扣减薪酬（万元）"
              @input="inputNum($event, 'salaryDeductionAmount')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="党纪处分（人次）">
            <el-input
              v-model="formData.partyDisciplinaryActions"
              :style="{ width: '100%' }"
              placeholder="请输入党纪处分（人次）"
              @input="inputNum($event, 'partyDisciplinaryActions')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政务处分（人次）">
            <el-input
              v-model="formData.administrativeSanctions"
              :style="{ width: '100%' }"
              placeholder="请输入政务处分（人次）"
              @input="inputNum($event, 'administrativeSanctions')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否向司法机关移送问题线索">
            <el-select
              v-model="formData.hasJudicialReferral"
              placeholder="请选择是否向司法机关移送问题线索"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="具体情况描述">
            <el-input
              v-model="formData.accountabilityDetails"
              :style="{ width: '100%' }"
              placeholder="请输入具体情况描述"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否提交整改销号申请">
            <el-select
              v-model="formData.hasSubmittedRectificationApplication"
              placeholder="请选择是否提交整改销号申请"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否已完成整改销号">
            <el-select
              v-model="formData.isRectificationCompleted"
              placeholder="请选择是否提交整改销号申请"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="到期未完成整改原因及下一步措施">
            <el-input
              v-model="formData.uncompletedRectificationReason"
              :style="{ width: '100%' }"
              placeholder="请输入到期未完成整改原因及下一步措施"
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
    previousYearAuditIssuesGetDetail,
    previousYearAuditIssuesMergeInfo,
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
          accountabilityDetails: '',
          accountingAdjustmentAmount: '',
          administrativeSanctions: '',
          assistingRectificationLeader: '',
          auditDepartmentContact: '',
          auditProjectName: '',
          auditRectificationType: '',
          businessProcessesOptimized: '',
          coordinatingRectificationDepartmentContact: '',
          createTime: '',
          detailedRectificationMeasures: '',
          existingSystemsImproved: '',
          hasJudicialReferral: '',
          hasSubmittedRectificationApplication: '',
          id: '',
          isRectificationCompleted: '',
          issueDescription: '',
          leadRectificationDepartmentContact: '',
          lossRecoveryAmount: '',
          newSystemsEstablished: '',
          nonMonetaryRectificationDetails: '',
          originalFundChannelReturnAmount: '',
          otherMonetaryRectificationAmount: '',
          otherMonetaryRectificationMethod: '',
          otherNonMonetaryRectificationMeasures: '',
          partyDisciplinaryActions: '',
          personnelActions: '',
          primaryRectificationResponsiblePerson: '',
          rectificationCompletionStandard: '',
          rectificationDeadline: '',
          rectificationDeadlineEnd: '',
          rectificationDeadlineStart: '',
          rectificationProgressDescription: '',
          rectificationResponsibleUnit: '',
          salaryDeductionAmount: '',
          taxSupplementAmount: '',
          totalMonetaryRectificationAmount: '',
          uncompletedRectificationReason: '',
        },
        rules: {
          rectificationResponsibleUnit: [
            {
              required: true,
              message: '请输入整改责任单位',
              trigger: 'blur',
            },
          ],
          auditRectificationType: [
            {
              required: true,
              message: '请输入审计整改类型',
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
          } = await previousYearAuditIssuesGetDetail({ id: row.id })
          console.log(data)
          this.formData = data
          // this.fetchData()
        }
        this.dialogVisible = true
      },
      close() {
        this.formData = {
          accountabilityDetails: '',
          accountingAdjustmentAmount: '',
          administrativeSanctions: '',
          assistingRectificationLeader: '',
          auditDepartmentContact: '',
          auditProjectName: '',
          auditRectificationType: '',
          businessProcessesOptimized: '',
          coordinatingRectificationDepartmentContact: '',
          createTime: '',
          detailedRectificationMeasures: '',
          existingSystemsImproved: '',
          hasJudicialReferral: '',
          hasSubmittedRectificationApplication: '',
          id: '',
          isRectificationCompleted: '',
          issueDescription: '',
          leadRectificationDepartmentContact: '',
          lossRecoveryAmount: '',
          newSystemsEstablished: '',
          nonMonetaryRectificationDetails: '',
          originalFundChannelReturnAmount: '',
          otherMonetaryRectificationAmount: '',
          otherMonetaryRectificationMethod: '',
          otherNonMonetaryRectificationMeasures: '',
          partyDisciplinaryActions: '',
          personnelActions: '',
          primaryRectificationResponsiblePerson: '',
          rectificationCompletionStandard: '',
          rectificationDeadline: '',
          rectificationDeadlineEnd: '',
          rectificationDeadlineStart: '',
          rectificationProgressDescription: '',
          rectificationResponsibleUnit: '',
          salaryDeductionAmount: '',
          taxSupplementAmount: '',
          totalMonetaryRectificationAmount: '',
          uncompletedRectificationReason: '',
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
            const res = await previousYearAuditIssuesMergeInfo({
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
