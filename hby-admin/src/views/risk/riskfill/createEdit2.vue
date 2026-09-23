<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="风险名称" prop="impRiskName">
            <el-input
              v-model="formData.impRiskName"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="牵头领导" prop="impWayStaffName">
            <el-input
              v-model="formData.impWayStaffName"
              clearable
              placeholder="请输入牵头领导"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['SelectPersonModal'].showEdit()"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="牵头责任部门" prop="impWayDeptName">
            <el-input
              v-model="formData.impWayDeptName"
              clearable
              placeholder="请选择牵头责任部门"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['department1'].show(false, [])"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="下发单位" prop="issuedStaffName">
            <el-input
              v-model="formData.issuedStaffName"
              clearable
              placeholder="请选择下发单位"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['SelectPersonModal'].showEdit()"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->

        <el-col :span="12">
          <el-form-item label="公司相关责任单位" prop="impDutyUnitName">
            <el-input
              v-model="formData.impDutyUnitName"
              clearable
              placeholder="请选择公司相关责任单位"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs.companyTreeModel.show()"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="priorities">
            <span slot="label">
              <span>重点事项</span>
              <div style="color:red;font-size: 12px;'">(限2000字以内)</div>
            </span>
            <el-input
              v-model="formData.priorities"
              clearable
              placeholder="请输入重点事项"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item prop="impRiskDetails">
            <span slot="label">
              <span>风险描述</span>
              <div style="color:red;font-size: 12px;'">(限2000字以内)</div>
            </span>
            <el-input
              v-model="formData.impRiskDetails"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <CompanyTreeModel1
      ref="department1"
      :multiple="true"
      @selected="handleSelectDepartment1"
    />
    <CompanyTreeModel2 ref="department2" @selected="handleSelectDepartment2" />
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :multiple="true"
    />
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>

    <selectDept
      ref="companyTreeModel"
      :multiple="true"
      @submit="selectCompany"
    />
  </el-dialog>
</template>

<script>
  // import { sjdwlrsjlrUpdate, sjdwlrsjlrDetail } from '@/oapi/audit/plan'
  import {
    setMajorRiskSaveOrUpdate,
    getMajorRiskDetails,
  } from '@/api/risk/create.js'
  import { formatDay } from '@/utils/index'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'
  import selectDept from './duoxuanDept.vue'

  export default {
    components: {
      CompanyTreeModel1: CompanyTreeModel,
      CompanySelectUserByTree,
      CompanyTreeModel2: CompanyTreeModel,
      SelectPersonModal,
      selectDept,
    },
    inheritAttrs: false,
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        tableData: [],
        formDisabled: false,
        formData: {
          createstaffid: '',
          createtime: '',
          id: '',
          impDutyUnit: '',
          impDutyUnitName: '',
          impIssuedStaffid: '',
          impLssuedDate: '',
          impRiskDetails: '',
          impRiskName: '',
          impWayDept: '',
          impWayDeptName: '',
          impWayStaff: '',
          impWayStaffName: '',
          issuedStaffName: '',
          majorid: '',
          toIssued: 0,
          priorities: '',
          // id: '',
          // impRiskName: '',
          // impRiskDetails: '',
          // impWayStaff: '',
          // impWayStaffName: '',
          // impWayDept: '',
          // impWayDeptName: '',
          // impDutyUnit: '',
          // impDutyUnitName: '',
          // impLssuedUnit: '',
          // issuedStaffName: '',
          // impThisControl: '',
          // impSolutions: '',
          // impTips: '',
          // impTextControl: '',
          // impOther: '',
          // secrectLevelId: '',
          // staffScopeNames: '',
          // staffScopeIds: '',
        },
        rules: {
          impRiskName: [
            { required: true, message: '请输入风险名称', trigger: 'blur' },
          ],
          impRiskDetails: [
            { required: true, message: '请输入风险描述', trigger: 'blur' },
          ],
          priorities: [
            { required: true, message: '请输入重点事项', trigger: 'blur' },
          ],
          impWayStaffName: [
            { required: true, message: '请输入牵头领导', trigger: 'blur' },
          ],
          impWayDeptName: [
            { required: true, message: '请输入牵头责任部门', trigger: 'blur' },
          ],
          impDutyUnitName: [
            {
              required: true,
              message: '请选择公司相关责任单位',
              trigger: 'blur',
            },
          ],
          issuedStaffName: [
            { required: true, message: '请选择下发单位', trigger: 'blur' },
          ],
          impThisControl: [
            {
              required: true,
              message: '请输入本季度风险防控情况',
              trigger: 'blur',
            },
          ],
          impSolutions: [
            {
              required: true,
              message: '请输入已发生的风险事件及应对处置情况',
              trigger: 'blur',
            },
          ],
          impTips: [
            {
              required: true,
              message: '请输入需要提示的问题喝风险',
              trigger: 'blur',
            },
          ],
          impTextControl: [
            {
              required: true,
              message: '请输入下季度主要风险研判及相应防控措施',
              trigger: 'blur',
            },
          ],
          impOther: [
            {
              required: true,
              message: '请输入其他需要说明的情况',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        type: 1,
        disabled: false,
        isAll: false,
      }
    },
    methods: {
      /**
       * type类型: 1-新增, 2-编辑, 3-季度弹窗中编辑
       */
      async showEdit(row, title, isAll = false) {
        console.log('🚀 ~ showEdit ~ row:', row)
        try {
          this.isAll = isAll
          this.dialogFormVisible = true
          this.formDisabled = title === '详情'

          this.formData.majorid = row.fatherId
          if (!row) return

          if (row.id) {
            const {
              data: {
                data: { data },
              },
            } = await getMajorRiskDetails({ id: row.id })
            this.formData = data
          }
        } catch (error) {
          console.error('显示编辑框失败:', error)
          this.$message.error('加载数据失败')
        }
      },
      close() {
        // this.$refs['ruleForm'].resetFields()
        this.formData = {
          priorities: '',
          createstaffid: '',
          createtime: '',
          id: '',
          impDutyUnit: '',
          impDutyUnitName: '',
          impIssuedStaffid: '',
          impLssuedDate: '',
          impRiskDetails: '',
          impRiskName: '',
          impWayDept: '',
          impWayDeptName: '',
          impWayStaff: '',
          impWayStaffName: '',
          issuedStaffName: '',
          majorid: '',
          toIssued: 0,
        }
        console.log(this.formData, 111111)
        this.type = 1
        this.dialogFormVisible = false
        console.log(this.formData, 222222)
      },
      async save() {
        try {
          const valid = await this.$refs['ruleForm'].validate()
          if (!valid) return

          this.loading = true
          const res = await setMajorRiskSaveOrUpdate(this.formData)
          if (res?.code === 1) {
            this.close()
            this.$emit('fetchData', res.data.data)
            this.$message.success('保存成功！')
          } else {
            this.$message.error(res?.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.loading = false
        }
      },
      handleExecutorSelected(checked) {
        this.formData.impWayStaff = checked.staffid
        this.formData.impWayStaffName = checked.realname
      },
      handleSelectDepartment1(val) {
        // console.log('🚀 ~ handleSelectDepartment1 ~ checked:', checked)
        // this.formData.impWayDept = checked.id
        // this.formData.impWayDeptName = checked.name

        const realNames = val.map((item) => item.name).join(',')
        this.$set(this.formData, 'impWayDeptName', realNames)
        const ids = val.map((item) => item.id)
        this.formData.impWayDept = ids.join(',')
      },
      handleSelectDepartment2(checked) {
        this.formData.impDutyUnit = checked.id
        this.formData.impDutyUnitName = checked.name
      },
      selectP(val) {
        // 获取姓名列表并拼接
        const realNames = val.map((item) => item.realname).join(',')
        this.$set(this.formData, 'impWayStaffName', realNames)
        const ids = val.map((item) => item.staffid)
        this.formData.impWayStaff = ids.join(',')
      },

      selectCompany(val) {
        console.log('🚀 ~ selectCompany ~ val:', val)
        // impDutyUnitName
        const realNames = val.map((item) => item.label).join(',')
        this.$set(this.formData, 'impDutyUnitName', realNames)
        const ids = val.map((item) => item.id)
        this.formData.impDutyUnit = ids.join(',')
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
