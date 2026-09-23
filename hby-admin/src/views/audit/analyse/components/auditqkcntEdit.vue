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
        <el-col :span="24" style="margin-top: 16px">
          <el-divider>单位信息</el-divider>
        </el-col>

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
          <el-form-item label="是否设立董事会" prop="hasBoardOfDirectors">
            <el-select
              v-model="formData.hasBoardOfDirectors"
              placeholder="请选择是否设立董事会"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否设立审计委员会" prop="hasAuditCommittee">
            <el-select
              v-model="formData.hasAuditCommittee"
              placeholder="请选择是否设立审计委员会"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否设立总审计师">
            <el-select
              v-model="formData.hasChiefDesigner"
              placeholder="请选择是否设立总审计师"
              :style="{ width: '100%' }"
              @change="changeChiefDesigner"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总审计师职位层级">
            <el-input
              v-model="formData.chiefDesignerPositionLevel"
              :style="{ width: '100%' }"
              placeholder="请输入总审计师职位层级"
              :disabled="formData.hasChiefDesigner == '否'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="总审计师任职方式">
            <el-input
              v-model="formData.chiefDesignerEmploymentMode"
              :style="{ width: '100%' }"
              placeholder="请输入总审计师任职方式"
              :disabled="formData.hasChiefDesigner == '否'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否设置内部审计机构">
            <el-select
              v-model="formData.hasInternalAuditDepartment"
              placeholder="请选择是否设置内部审计机构"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内部审计工作的领导机构">
            <el-input
              v-model="formData.internalAuditLeadershipOrganization"
              :style="{ width: '100%' }"
              placeholder="请输入内部审计工作的领导机构"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内部审计机构层级">
            <el-input
              v-model="formData.internalAuditDepartmentLevel"
              :style="{ width: '100%' }"
              placeholder="请输入内部审计机构层级"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计机构名称" prop="auditDepartmentName">
            <el-input
              v-model="formData.auditDepartmentName"
              :style="{ width: '100%' }"
              placeholder="请输入审计机构名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计机构是否独立">
            <el-select
              v-model="formData.auditDepartmentIsIndependent"
              placeholder="请选择审计机构是否独立"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="非独立审计机构与哪些职能部门合并设置">
            <el-input
              v-model="formData.nonIndependentAuditDepartmentMerged"
              :style="{ width: '100%' }"
              placeholder="请输入非独立审计机构与哪些职能部门合并设置"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本年度内部审计制度制修订个数">
            <el-input-number
              v-model="formData.internalAuditSystemRevisions"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入本年度内部审计制度制修订个数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内部审计部门是否实现差异化考核">
            <el-select
              v-model="formData.hasDifferentiatedPerformanceEvaluation"
              placeholder="请选择内部审计部门是否实现差异化考核"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门负责人绩效考核结果">
            <el-input
              v-model="formData.departmentHeadPerformanceResult"
              :style="{ width: '100%' }"
              placeholder="请输入部门负责人绩效考核结果"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 74px">
          <el-form-item label="部门考核结果">
            <el-input
              v-model="formData.departmentPerformanceResult"
              :style="{ width: '100%' }"
              placeholder="请输入部门考核结果"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制数">
            <el-input-number
              v-model="formData.approvedStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入编制数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实有人员数">
            <el-input-number
              v-model="formData.actualStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入实有人员数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="通讯地址">
            <el-input
              v-model="formData.contactAddress"
              :style="{ width: '100%' }"
              placeholder="请输入通讯地址"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮编">
            <el-input
              v-model="formData.postalCode"
              :style="{ width: '100%' }"
              placeholder="请输入邮编"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>传真号码</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="区号">
            <el-input
              v-model="formData.faxAreaCode"
              :style="{ width: '100%' }"
              placeholder="请输入区号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电话号">
            <el-input
              v-model="formData.faxPhoneNumber"
              :style="{ width: '100%' }"
              placeholder="请输入电话号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>从事审计职责</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本机构是否有此职能">
            <el-select
              v-model="formData.hasAuditFunction"
              placeholder="请选择本机构是否有此职能"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制数">
            <el-input-number
              v-model="formData.auditFunctionApprovedStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入编制数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员数">
            <el-input-number
              v-model="formData.auditFunctionActualStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入实有人员数"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>从事风险管理工作</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="本机构是否有此职能">
            <el-select
              v-model="formData.hasRiskManagementFunction"
              placeholder="请选择本机构是否有此职能"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制数">
            <el-input-number
              v-model="formData.riskManagementApprovedStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入编制数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员数">
            <el-input-number
              v-model="formData.riskManagementActualStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入实有人员数"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>从事内部控制评价工作</el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="本机构是否有此职能">
            <el-select
              v-model="formData.hasInternalControlEvaluationFunction"
              placeholder="请选择本机构是否有此职能"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制数">
            <el-input-number
              v-model="formData.internalControlEvaluationApprovedStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入编制数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员数">
            <el-input-number
              v-model="formData.internalControlEvaluationActualStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入实有人员数"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>其他工作</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作名称(逐一举例，逗号隔开)">
            <el-input
              v-model="formData.otherWorkNames"
              :style="{ width: '100%' }"
              placeholder="请输入工作名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制数">
            <el-input-number
              v-model="formData.otherWorkApprovedStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入编制数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员数">
            <el-input-number
              v-model="formData.otherWorkActualStaffing"
              :style="{ width: '100%' }"
              :min="0"
              label="请输入实有人员数"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>审计分管领导信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名">
            <el-input
              v-model="formData.auditSupervisorName"
              :style="{ width: '100%' }"
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务">
            <el-input
              v-model="formData.auditSupervisorPosition"
              :style="{ width: '100%' }"
              placeholder="请输入职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select
              v-model="formData.auditSupervisorGender"
              placeholder="请选择性别"
              :style="{ width: '100%' }"
            >
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="formData.auditSupervisorBirthdate"
              type="date"
              placeholder="请输入出生日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="籍贯">
            <el-input
              v-model="formData.auditSupervisorNativePlace"
              :style="{ width: '100%' }"
              placeholder="请输入籍贯"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="民族">
            <el-input
              v-model="formData.auditSupervisorEthnicity"
              :style="{ width: '100%' }"
              placeholder="请输入民族"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="办公电话-区号"
            prop="auditSupervisorOfficePhoneAreaCode"
          >
            <el-input
              v-model="formData.auditSupervisorOfficePhoneAreaCode"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-区号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="办公电话-电话"
            prop="auditSupervisorOfficePhoneNumber"
          >
            <el-input
              v-model="formData.auditSupervisorOfficePhoneNumber"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-电话"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮箱">
            <el-input
              v-model="formData.auditSupervisorEmail"
              :style="{ width: '100%' }"
              placeholder="请输入电子邮箱"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24" style="margin-top: 16px">
          <el-divider>审计协管领导信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名">
            <el-input
              v-model="formData.auditAssistantSupervisorName"
              :style="{ width: '100%' }"
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务">
            <el-input
              v-model="formData.auditAssistantSupervisorPosition"
              :style="{ width: '100%' }"
              placeholder="请输入职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select
              v-model="formData.auditAssistantSupervisorGender"
              placeholder="请选择性别"
              :style="{ width: '100%' }"
            >
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="formData.auditAssistantSupervisorBirthdate"
              type="date"
              placeholder="请输入出生日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="籍贯">
            <el-input
              v-model="formData.auditAssistantSupervisorNativePlace"
              :style="{ width: '100%' }"
              placeholder="请输入籍贯"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="民族">
            <el-input
              v-model="formData.auditAssistantSupervisorEthnicity"
              :style="{ width: '100%' }"
              placeholder="请输入民族"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="办公电话-区号"
            prop="auditAssistantSupervisorOfficePhoneAreaCode"
          >
            <el-input
              v-model="formData.auditAssistantSupervisorOfficePhoneAreaCode"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-区号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="办公电话-电话"
            prop="auditAssistantSupervisorOfficePhoneNumber"
          >
            <el-input
              v-model="formData.auditAssistantSupervisorOfficePhoneNumber"
              :style="{ width: '100%' }"
              placeholder="请输入办公电话-电话"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮箱">
            <el-input
              v-model="formData.auditAssistantSupervisorEmail"
              :style="{ width: '100%' }"
              placeholder="请输入电子邮箱"
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
    auditStatisticsGetDetail,
    auditStatisticsMergeInfo,
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
          actualStaffing: '',
          approvedStaffing: '',
          auditAssistantSupervisorBirthdate: '',
          auditAssistantSupervisorEmail: '',
          auditAssistantSupervisorEthnicity: '',
          auditAssistantSupervisorGender: '',
          auditAssistantSupervisorName: '',
          auditAssistantSupervisorNativePlace: '',
          auditAssistantSupervisorOfficePhoneAreaCode: '',
          auditAssistantSupervisorOfficePhoneNumber: '',
          auditAssistantSupervisorPosition: '',
          auditDepartmentIsIndependent: '',
          auditDepartmentName: '',
          auditFunctionActualStaffing: '',
          auditFunctionApprovedStaffing: '',
          auditSupervisorBirthdate: '',
          auditSupervisorEmail: '',
          auditSupervisorEthnicity: '',
          auditSupervisorGender: '',
          auditSupervisorName: '',
          auditSupervisorNativePlace: '',
          auditSupervisorOfficePhoneAreaCode: '',
          auditSupervisorOfficePhoneNumber: '',
          auditSupervisorPosition: '',
          chiefDesignerEmploymentMode: '',
          chiefDesignerPositionLevel: '',
          contactAddress: '',
          createTime: '',
          departmentHeadPerformanceResult: '',
          departmentPerformanceResult: '',
          faxAreaCode: '',
          faxPhoneNumber: '',
          hasAuditCommittee: '',
          hasAuditFunction: '',
          hasBoardOfDirectors: '',
          hasChiefDesigner: '',
          hasDifferentiatedPerformanceEvaluation: '',
          hasInternalAuditDepartment: '',
          hasInternalControlEvaluationFunction: '',
          hasRiskManagementFunction: '',
          id: '',
          internalAuditDepartmentLevel: '',
          internalAuditLeadershipOrganization: '',
          internalAuditSystemRevisions: '',
          internalControlEvaluationActualStaffing: '',
          internalControlEvaluationApprovedStaffing: '',
          nonIndependentAuditDepartmentMerged: '',
          otherWorkActualStaffing: '',
          otherWorkApprovedStaffing: '',
          otherWorkNames: '',
          postalCode: '',
          riskManagementActualStaffing: '',
          riskManagementApprovedStaffing: '',
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
          auditDepartmentName: [
            {
              required: true,
              message: '请输入审计机构名称',
              trigger: 'blur',
            },
          ],
          auditSupervisorOfficePhoneAreaCode: [
            {
              pattern: /^\d{1,15}$/,
              message: '区号格式不正确，请输入1-15位数字',
              trigger: 'blur',
            },
          ],
          auditSupervisorOfficePhoneNumber: [
            {
              pattern: /^\d{1,15}$/,
              message: '电话号码格式不正确，请输入1-15位数字',
              trigger: 'blur',
            },
          ],
          auditAssistantSupervisorOfficePhoneAreaCode: [
            {
              pattern: /^\d{1,15}$/,
              message: '区号格式不正确，请输入1-15位数字',
              trigger: 'blur',
            },
          ],
          auditAssistantSupervisorOfficePhoneNumber: [
            {
              pattern: /^\d{1,15}$/,
              message: '电话号码格式不正确，请输入1-15位数字',
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
        console.log(row)

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
          } = await auditStatisticsGetDetail({ id: row.id })
          console.log(data)
          this.formData = data
          // this.fetchData()
        }
        this.dialogVisible = true
      },
      close() {
        this.formData = {
          actualStaffing: '',
          approvedStaffing: '',
          auditAssistantSupervisorBirthdate: '',
          auditAssistantSupervisorEmail: '',
          auditAssistantSupervisorEthnicity: '',
          auditAssistantSupervisorGender: '',
          auditAssistantSupervisorName: '',
          auditAssistantSupervisorNativePlace: '',
          auditAssistantSupervisorOfficePhoneAreaCode: '',
          auditAssistantSupervisorOfficePhoneNumber: '',
          auditAssistantSupervisorPosition: '',
          auditDepartmentIsIndependent: '',
          auditDepartmentName: '',
          auditFunctionActualStaffing: '',
          auditFunctionApprovedStaffing: '',
          auditSupervisorBirthdate: '',
          auditSupervisorEmail: '',
          auditSupervisorEthnicity: '',
          auditSupervisorGender: '',
          auditSupervisorName: '',
          auditSupervisorNativePlace: '',
          auditSupervisorOfficePhoneAreaCode: '',
          auditSupervisorOfficePhoneNumber: '',
          auditSupervisorPosition: '',
          chiefDesignerEmploymentMode: '',
          chiefDesignerPositionLevel: '',
          contactAddress: '',
          createTime: '',
          departmentHeadPerformanceResult: '',
          departmentPerformanceResult: '',
          faxAreaCode: '',
          faxPhoneNumber: '',
          hasAuditCommittee: '',
          hasAuditFunction: '',
          hasBoardOfDirectors: '',
          hasChiefDesigner: '',
          hasDifferentiatedPerformanceEvaluation: '',
          hasInternalAuditDepartment: '',
          hasInternalControlEvaluationFunction: '',
          hasRiskManagementFunction: '',
          id: '',
          internalAuditDepartmentLevel: '',
          internalAuditLeadershipOrganization: '',
          internalAuditSystemRevisions: '',
          internalControlEvaluationActualStaffing: '',
          internalControlEvaluationApprovedStaffing: '',
          nonIndependentAuditDepartmentMerged: '',
          otherWorkActualStaffing: '',
          otherWorkApprovedStaffing: '',
          otherWorkNames: '',
          postalCode: '',
          riskManagementActualStaffing: '',
          riskManagementApprovedStaffing: '',
          unitName: '',
        }
        this.tableData = []
        this.select = []
        this.dialogVisible = false
        this.$emit('fetchData')
      },
      changeChiefDesigner(val) {
        console.log(val)
        if (val == '否') {
          this.formData.chiefDesignerEmploymentMode = ''
          this.formData.chiefDesignerPositionLevel = ''
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
            const res = await auditStatisticsMergeInfo({
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
