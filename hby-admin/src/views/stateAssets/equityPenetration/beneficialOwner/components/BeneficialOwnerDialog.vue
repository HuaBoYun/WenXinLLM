<template>
  <el-dialog
    :title="dialogType === 'add' ? '新增实际控制人' : dialogType === 'view' ? '查看实际控制人' : '编辑实际控制人'"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="140px"
      size="small"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="被控制企业名称" prop="controlledEnterpriseName">
            <el-input
              v-model="form.controlledEnterpriseName"
              placeholder="请输入被控制企业名称"
              maxlength="200"
              :disabled="isViewMode"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制人名称" prop="controllerEnterpriseName">
            <el-input
              v-model="form.controllerEnterpriseName"
              placeholder="请输入控制人名称"
              maxlength="200"
              :disabled="isViewMode"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制人类型" prop="controllerType">
            <el-select
              v-model="form.controllerType"
              placeholder="请选择控制人类型"
              clearable
              style="width: 100%"
              :disabled="isViewMode"
            >
              <el-option label="企业" value="ENTERPRISE"></el-option>
              <el-option label="个人" value="INDIVIDUAL"></el-option>
              <el-option label="政府" value="GOVERNMENT"></el-option>
              <el-option label="机构" value="INSTITUTION"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制人性质" prop="controllerNature">
            <el-select
              v-model="form.controllerNature"
              placeholder="请选择控制人性质"
              clearable
              style="width: 100%"
              :disabled="isViewMode"
            >
              <el-option label="国有" value="STATE_OWNED"></el-option>
              <el-option label="民营" value="PRIVATE"></el-option>
              <el-option label="混合" value="MIXED"></el-option>
              <el-option label="外资" value="FOREIGN"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制方式" prop="controlMethod">
            <el-select
              v-model="form.controlMethod"
              placeholder="请选择控制方式"
              clearable
              style="width: 100%"
              :disabled="isViewMode"
            >
              <el-option label="股权控制" value="SHAREHOLDING"></el-option>
              <el-option label="协议控制" value="AGREEMENT"></el-option>
              <el-option label="表决权控制" value="VOTING_RIGHT"></el-option>
              <el-option label="混合控制" value="MIXED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 控制比例 -->
      <el-divider content-position="left">控制比例</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="直接持股比例(%)" prop="directShareholdingRatio">
            <el-input-number
              v-model="form.directShareholdingRatio"
              :min="0"
              :max="100"
              :step="0.01"
              :precision="2"
              style="width: 100%"
              :disabled="isViewMode"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="间接持股比例(%)" prop="indirectShareholdingRatio">
            <el-input-number
              v-model="form.indirectShareholdingRatio"
              :min="0"
              :max="100"
              :step="0.01"
              :precision="2"
              style="width: 100%"
              :disabled="isViewMode"
            ></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="综合持股比例(%)" prop="totalShareholdingRatio">
            <el-input-number
              v-model="form.totalShareholdingRatio"
              :min="0"
              :max="100"
              :step="0.01"
              :precision="2"
              style="width: 100%"
              :disabled="isViewMode"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表决权比例(%)" prop="votingRightRatio">
            <el-input-number
              v-model="form.votingRightRatio"
              :min="0"
              :max="100"
              :step="0.01"
              :precision="2"
              style="width: 100%"
              :disabled="isViewMode"
            ></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 控制关系 -->
      <el-divider content-position="left">控制关系</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制层级" prop="controlLevel">
            <el-input-number
              v-model="form.controlLevel"
              :min="1"
              :max="10"
              :step="1"
              :precision="0"
              style="width: 100%"
              :disabled="isViewMode"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制路径" prop="controlPath">
            <el-input
              v-model="form.controlPath"
              placeholder="如: 示例集团->国华物流"
              maxlength="500"
              :disabled="isViewMode"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="controlRiskLevel">
            <el-select
              v-model="form.controlRiskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 100%"
              :disabled="isViewMode"
            >
              <el-option label="低" value="LOW"></el-option>
              <el-option label="中" value="MEDIUM"></el-option>
              <el-option label="高" value="HIGH"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="识别方法" prop="identificationMethod">
            <el-select
              v-model="form.identificationMethod"
              placeholder="请选择识别方法"
              clearable
              style="width: 100%"
              :disabled="isViewMode"
            >
              <el-option label="股权穿透" value="SHAREHOLDING"></el-option>
              <el-option label="协议认定" value="AGREEMENT"></el-option>
              <el-option label="算法识别" value="ALGORITHM"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
              maxlength="500"
              show-word-limit
              :disabled="isViewMode"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ isViewMode ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isViewMode" type="primary" @click="handleSubmit" :loading="loading">
        {{ dialogType === 'add' ? '新增' : '更新' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addBeneficialOwner, updateBeneficialOwner, getBeneficialOwnerById } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'BeneficialOwnerDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' }
  },
  data() {
    return {
      loading: false,
      form: {
        controllerId: null,
        controlledEnterpriseName: '',
        controllerEnterpriseName: '',
        controllerType: '',
        controllerNature: '',
        controlMethod: '',
        directShareholdingRatio: null,
        indirectShareholdingRatio: null,
        totalShareholdingRatio: null,
        votingRightRatio: null,
        controlLevel: null,
        controlPath: '',
        controlRiskLevel: 'LOW',
        identificationMethod: '',
        confirmationStatus: 'PENDING',
        controlStatus: 'ACTIVE',
        remark: ''
      },
      rules: {
        controlledEnterpriseName: [{ required: true, message: '请输入被控制企业名称', trigger: 'blur' }],
        controllerEnterpriseName: [{ required: true, message: '请输入控制人名称', trigger: 'blur' }],
        controllerType: [{ required: true, message: '请选择控制人类型', trigger: 'change' }],
        controlMethod: [{ required: true, message: '请选择控制方式', trigger: 'change' }],
        totalShareholdingRatio: [{ required: true, message: '请输入综合持股比例', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    isViewMode() {
      return this.dialogType === 'view'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        if ((this.dialogType === 'edit' || this.dialogType === 'view') && this.formData && this.formData.controllerId) {
          // 直接使用传入的行数据，避免额外请求
          this.form = {
            ...this.form,
            ...this.formData,
            // 确保数值类型正确
            directShareholdingRatio: this.formData.directShareholdingRatio != null ? Number(this.formData.directShareholdingRatio) : null,
            indirectShareholdingRatio: this.formData.indirectShareholdingRatio != null ? Number(this.formData.indirectShareholdingRatio) : null,
            totalShareholdingRatio: this.formData.totalShareholdingRatio != null ? Number(this.formData.totalShareholdingRatio) : null,
            votingRightRatio: this.formData.votingRightRatio != null ? Number(this.formData.votingRightRatio) : null,
          }
        } else if (this.dialogType === 'add') {
          this.resetForm()
        }
      }
    }
  },
  methods: {
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          let response
          if (this.dialogType === 'add') {
            response = await addBeneficialOwner(this.form)
          } else {
            response = await updateBeneficialOwner(this.form)
          }
          if (response.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败')
        } finally {
          this.loading = false
        }
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        controllerId: null,
        controlledEnterpriseName: '',
        controllerEnterpriseName: '',
        controllerType: '',
        controllerNature: '',
        controlMethod: '',
        directShareholdingRatio: null,
        indirectShareholdingRatio: null,
        totalShareholdingRatio: null,
        votingRightRatio: null,
        controlLevel: null,
        controlPath: '',
        controlRiskLevel: 'LOW',
        identificationMethod: '',
        confirmationStatus: 'PENDING',
        controlStatus: 'ACTIVE',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>