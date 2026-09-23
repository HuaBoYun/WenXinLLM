<template>
  <div>
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
        style="display: flex; flex-wrap: wrap"
      >
        <!-- <el-col :span="12" v-if="showMJ">
          <el-form-item label="密级" prop="secrectLevelId"
            :rules="[{ required: true, trigger: 'change', message: '请选择密级' }]">
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="formDisabled"
              @change="handleSecretLevelChange"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围"  prop="staffScopeNames"
            :rules="[{ required: true, trigger: 'change', message: '请选择知悉范围' }]">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || formDisabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="风险名称" prop="impRiskName">
            <el-input
              v-model="formData.impRiskName"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
              :disabled="isEdit"
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
              @click="$refs['userTreeRef'].show()"
              :disabled="formDisabled || isEdit"
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
              :disabled="formDisabled || isEdit"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="下发单位" prop="impLssuedUnitName">
            <el-input
              v-model="formData.impLssuedUnitName"
              clearable
              placeholder="请选择下发单位"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              type="primary"
              @click="$refs['company'].show(false, [])"
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
              @click="$refs['department2'].show(false, [])"
              :disabled="formDisabled || isEdit"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点事项" prop="impKeyIssues">
            <span slot="label">
              <span>重点事项</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impKeyIssues"
              clearable
              placeholder="请输入重点事项"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="isEdit"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="impRiskDetails">
            <span slot="label">
              <span>风险描述</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impRiskDetails"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              :disabled="isEdit"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="impThisControl">
            <span slot="label">
              <span>{{ getThisControlLabel }}</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impThisControl"
              clearable
              :placeholder="'请输入' + getThisControlLabel"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="impSolutions">
            <span slot="label">
              <span>已发生的风险事件及应对处置情况</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impSolutions"
              clearable
              placeholder="请输入已发生的风险事件及应对处置情况"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="impTips">
            <span slot="label">
              <span>需要提示的问题和风险</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impTips"
              clearable
              placeholder="请输入需要提示的问题和风险"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="!isFourthQuarter">
          <el-form-item label="" prop="impTextControl">
            <span slot="label">
              <span>{{ getNextControlLabel }}</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impTextControl"
              clearable
              :placeholder="'请输入' + getNextControlLabel"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="其他需要说明的情况" prop="impOther">
            <span slot="label">
              <span>其他需要说明的情况</span>
              <div style="color: red; font-size: 12px">（限2000字以内）</div>
            </span>
            <el-input
              v-model="formData.impOther"
              clearable
              placeholder="请输入其他需要说明的情况"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              maxlength="2000"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div v-if="!formDisabled" style="text-align: right; margin-top: 20px">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
    <CompanyTreeModel1 ref="department1" @selected="handleSelectDepartment1" />
    <CompanyTreeModel2 ref="department2" @selected="handleSelectDepartment2" />
    <!-- <CompanyTreeModal ref="company" @selected="handleSelectCompany" /> -->
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    issuedSaveOrUpdate,
    issuedImplementDetails,
  } from '@/api/risk/riskfill'
  import CompanyTreeModel1 from '@/components/CompanyTreeModel'
  import CompanyTreeModel2 from '@/components/CompanyTreeModel'
  // import CompanyTreeModal from '@/components/CompanyTreeModal'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import ZXPerson from '@/components/selectPerson'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'

  export default {
    name: 'xqjybEdit',
    inheritAttrs: false,
    components: {
      CompanyTreeModel1,
      CompanyTreeModel2,
      // CompanyTreeModal,
      CompanySelectUserByTree,
      ZXPerson,
    },
    props: {
      curRow: {
        type: Object,
        default: () => ({}),
      },
      //判断是不是修改,如果是修改那就不准编辑
      isEdit: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        loading: false,
        formData: {
          id: '',
          reportingId: '',
          impRiskName: '',
          impRiskDetails: '',
          impWayStaff: '',
          impKeyIssues: '',
          impWayStaffName: '',
          impWayDept: '',
          impWayDeptName: '',
          impDutyUnit: '',
          impDutyUnitName: '',
          impLssuedUnit: '',
          impLssuedUnitName: '',
          impThisControl: '',
          impSolutions: '',
          impTips: '',
          impTextControl: '',
          impOther: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        id: '',
        formDisabled: false,
        rules: {
          impRiskName: [
            { required: true, message: '请输入风险名称', trigger: 'blur' },
          ],
          impKeyIssues: [
            { required: true, message: '请输入重点事项', trigger: 'blur' },
          ],
          impRiskDetails: [
            { required: true, message: '请输入风险描述', trigger: 'blur' },
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
          impLssuedUnitName: [
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
        showMJ: false,
        MJoption: [],
        quarterName: '', // 存储季度名称
      }
    },
    computed: {
      // 当前季度风险防控情况标签
      getThisControlLabel() {
        if (!this.quarterName) return '本季度风险防控情况'

        if (this.quarterName.includes('一季度')) {
          return '本季度风险防控情况'
        } else if (this.quarterName.includes('二季度')) {
          return '上半年风险防控情况'
        } else if (this.quarterName.includes('三季度')) {
          return '第三季度风险防控情况'
        } else if (this.quarterName.includes('四季度')) {
          return '第四季度风险防控情况'
        }
        return '本季度风险防控情况'
      },

      // 下季度主要风险研判标签
      getNextControlLabel() {
        if (!this.quarterName) return '下季度主要风险研判及相应防护措施'

        if (this.quarterName.includes('一季度')) {
          return '下季度主要风险研判及相应防护措施'
        } else if (this.quarterName.includes('二季度')) {
          return '下半年主要风险研判及相应防护措施'
        } else if (this.quarterName.includes('三季度')) {
          return '下季度主要风险研判及相应防护措施'
        }
        return '下季度主要风险研判及相应防护措施'
      },

      // 是否是第四季度
      isFourthQuarter() {
        return this.quarterName && this.quarterName.includes('四季度')
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('RiskFillPage')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {
      this.formData.reportingId = this.curRow.id
      console.log('🚀 ~ mounted ~ this.curRow11222:', this.curRow)
      if (this.curRow.id) {
        this.fetchData()
      }

      // 获取季度名称
      if (this.curRow.quartername) {
        this.quarterName = this.curRow.quartername
      }
    },
    methods: {
      // 添加密级改变的处理方法
      handleSecretLevelChange() {
        // 清空知悉范围
        this.formData.staffScopeNames = ''
        this.formData.staffScopeIds = ''
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      async fetchData(row) {
        this.formDisabled = this.curRow.disabled
        const { data } = await issuedImplementDetails({ id: this.curRow.id })
        Object.assign(this.formData, data.data.pageInfo)
        console.log('🚀 ~ fetchData ~ this.formData:', this.formData)

        // 获取季度名称
        if (this.curRow.quartername) {
          this.quarterName = this.curRow.quartername
        }

        // 根据规则动态更新验证信息
        this.updateRules()
      },

      // 更新表单验证规则
      updateRules() {
        if (this.isFourthQuarter) {
          // 四季度不需要验证下季度字段
          this.rules.impTextControl = []
        } else {
          // 其他季度需要验证
          this.rules.impTextControl = [
            {
              required: true,
              message: `请输入${this.getNextControlLabel}`,
              trigger: 'blur',
            },
          ]
        }

        // 更新本季度验证信息
        this.rules.impThisControl = [
          {
            required: true,
            message: `请输入${this.getThisControlLabel}`,
            trigger: 'blur',
          },
        ]
      },

      close() {
        this.$refs['ruleForm'].resetFields()
        this.formData = {
          id: '',
          reportingId: '',
          impRiskName: '',
          impRiskDetails: '',
          impKeyIssues: '',
          impWayStaff: '',
          impWayStaffName: '',
          impWayDept: '',
          impWayDeptName: '',
          impDutyUnit: '',
          impDutyUnitName: '',
          impLssuedUnit: '',
          impLssuedUnitName: '',
          impThisControl: '',
          impSolutions: '',
          impTips: '',
          impTextControl: '',
          impOther: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.$emit('close')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            const res = await issuedSaveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')

              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.name
      },
      handleSelectCompany(checked) {
        this.formData.impLssuedUnit = checked.id
        this.formData.impLssuedUnitName = checked.name
      },
      handleSelectDepartment1(checked) {
        this.formData.impWayDept = checked.id
        this.formData.impWayDeptName = checked.name
      },
      handleSelectDepartment2(checked) {
        this.formData.impDutyUnit = checked.id
        this.formData.impDutyUnitName = checked.name
      },
      handleExecutorSelected(checked) {
        this.formData.impWayStaff = checked.staffid
        this.formData.impWayStaffName = checked.realname
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
