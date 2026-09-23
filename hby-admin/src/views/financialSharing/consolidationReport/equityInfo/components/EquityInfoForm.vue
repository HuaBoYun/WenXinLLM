<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="140px"
      :disabled="formType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="母公司ID" prop="parentOrgId">
            <el-input v-model="form.parentOrgId" placeholder="请输入母公司ID" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="母公司名称" prop="parentOrgName">
            <el-input v-model="form.parentOrgName" placeholder="请输入母公司名称" maxlength="200" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="子公司ID" prop="subsidiaryOrgId">
            <el-input v-model="form.subsidiaryOrgId" placeholder="请输入子公司ID" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="子公司名称" prop="subsidiaryOrgName">
            <el-input v-model="form.subsidiaryOrgName" placeholder="请输入子公司名称" maxlength="200" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="股权类型" prop="equityType">
            <el-select v-model="form.equityType" placeholder="请选择股权类型">
              <el-option label="直接持股" value="DIRECT" />
              <el-option label="间接持股" value="INDIRECT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="持股比例(%)" prop="holdingRatio">
            <el-input-number
              v-model="form.holdingRatio"
              :min="0"
              :max="100"
              :precision="4"
              :step="0.01"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投资日期" prop="investmentDate">
            <el-date-picker
              v-model="form.investmentDate"
              type="date"
              placeholder="请选择投资日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用" prop="isActive">
            <el-radio-group v-model="form.isActive">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效起始期间" prop="effectiveStartPeriod">
            <el-input v-model="form.effectiveStartPeriod" placeholder="如:202401" maxlength="20" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效终止期间" prop="effectiveEndPeriod">
            <el-input v-model="form.effectiveEndPeriod" placeholder="如:202412" maxlength="20" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="500"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="formType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getEquityById, saveEquity, updateEquity } from '@/api/financialSharing/consolidationReport/equityInfo'

export default {
  name: 'EquityInfoForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    equityId: {
      type: String,
      default: null
    },
    modelId: {
      type: String,
      default: null
    },
    formType: {
      type: String,
      default: 'add' // add/edit/view
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      form: {
        equityId: null,
        modelId: '',
        parentOrgId: '',
        parentOrgName: '',
        subsidiaryOrgId: '',
        subsidiaryOrgName: '',
        equityType: '',
        holdingRatio: null,
        investmentDate: '',
        effectiveStartPeriod: '',
        effectiveEndPeriod: '',
        isActive: 'Y',
        remark: ''
      },
      rules: {
        parentOrgId: [
          { required: true, message: '请输入母公司ID', trigger: 'blur' }
        ],
        parentOrgName: [
          { required: true, message: '请输入母公司名称', trigger: 'blur' }
        ],
        subsidiaryOrgId: [
          { required: true, message: '请输入子公司ID', trigger: 'blur' }
        ],
        subsidiaryOrgName: [
          { required: true, message: '请输入子公司名称', trigger: 'blur' }
        ],
        equityType: [
          { required: true, message: '请选择股权类型', trigger: 'change' }
        ],
        holdingRatio: [
          { required: true, message: '请输入持股比例', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增股权信息',
        edit: '编辑股权信息',
        view: '查看股权信息'
      }
      return titleMap[this.formType] || '股权信息'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        if (this.equityId) {
          this.loadEquityData()
        } else {
          this.form.modelId = this.modelId
        }
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    /** 加载股权信息数据 */
    loadEquityData() {
      getEquityById({ equityId: this.equityId }).then(res => {
        if (res.code === 200 && res.data) {
          this.form = { ...res.data }
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const apiMethod = this.formType === 'add' ? saveEquity : updateEquity
          apiMethod(this.form).then(res => {
            this.submitLoading = false
            if (res.code === 200) {
              this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(() => {
            this.submitLoading = false
            this.$message.error('操作失败')
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.dialogVisible = false
    },
    /** 重置表单 */
    resetForm() {
      this.form = {
        equityId: null,
        modelId: '',
        parentOrgId: '',
        parentOrgName: '',
        subsidiaryOrgId: '',
        subsidiaryOrgName: '',
        equityType: '',
        holdingRatio: null,
        investmentDate: '',
        effectiveStartPeriod: '',
        effectiveEndPeriod: '',
        isActive: 'Y',
        remark: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    }
  }
}
</script>

<style scoped>
</style>


