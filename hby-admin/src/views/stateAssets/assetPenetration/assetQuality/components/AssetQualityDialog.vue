<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="qualityForm"
      :model="qualityForm"
      :rules="rules"
      label-width="120px"
      :disabled="dialogType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="qualityForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input v-model="qualityForm.assetName" placeholder="请输入资产名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产类别" prop="assetCategory">
            <el-select v-model="qualityForm.assetCategory" placeholder="请选择资产类别" style="width: 100%;">
              <el-option label="固定资产" value="FIXED_ASSETS"></el-option>
              <el-option label="流动资产" value="CURRENT_ASSETS"></el-option>
              <el-option label="无形资产" value="INTANGIBLE_ASSETS"></el-option>
              <el-option label="投资性资产" value="INVESTMENT_ASSETS"></el-option>
              <el-option label="金融资产" value="FINANCIAL_ASSETS"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产价值(万元)" prop="assetValue">
            <el-input-number
              v-model="qualityForm.assetValue"
              :min="0"
              placeholder="请输入资产价值"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="质量等级" prop="qualityLevel">
            <el-select v-model="qualityForm.qualityLevel" placeholder="请选择质量等级" style="width: 100%;">
              <el-option label="优质" value="EXCELLENT"></el-option>
              <el-option label="良好" value="GOOD"></el-option>
              <el-option label="一般" value="AVERAGE"></el-option>
              <el-option label="较差" value="POOR"></el-option>
              <el-option label="风险" value="RISK"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质量评分" prop="qualityScore">
            <el-input-number
              v-model="qualityForm.qualityScore"
              :min="0"
              :max="100"
              placeholder="请输入质量评分"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="qualityForm.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
              <el-option label="低风险" value="LOW"></el-option>
              <el-option label="中风险" value="MEDIUM"></el-option>
              <el-option label="高风险" value="HIGH"></el-option>
              <el-option label="极高风险" value="CRITICAL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估状态" prop="assessmentStatus">
            <el-select v-model="qualityForm.assessmentStatus" placeholder="请选择评估状态" style="width: 100%;">
              <el-option label="待评估" value="PENDING"></el-option>
              <el-option label="评估中" value="ASSESSING"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="质量描述" prop="qualityDescription">
        <el-input
          v-model="qualityForm.qualityDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入质量描述"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addAssetQuality, updateAssetQuality } from '@/api/stateAssets/assetQuality'

export default {
  name: 'AssetQualityDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assetData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      submitLoading: false,
      qualityForm: {
        enterpriseName: '',
        assetName: '',
        assetCategory: '',
        assetValue: 0,
        qualityLevel: '',
        qualityScore: 0,
        riskLevel: '',
        assessmentStatus: 'PENDING',
        qualityDescription: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        assetCategory: [
          { required: true, message: '请选择资产类别', trigger: 'change' }
        ],
        assetValue: [
          { required: true, message: '请输入资产价值', trigger: 'blur' }
        ],
        qualityLevel: [
          { required: true, message: '请选择质量等级', trigger: 'change' }
        ],
        qualityScore: [
          { required: true, message: '请输入质量评分', trigger: 'blur' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新增资产质量',
        edit: '编辑资产质量',
        view: '查看资产质量'
      }
      return titleMap[this.dialogType] || '资产质量'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.qualityForm = {
          enterpriseName: '',
          assetName: '',
          assetCategory: '',
          assetValue: 0,
          qualityLevel: '',
          qualityScore: 0,
          riskLevel: '',
          assessmentStatus: 'PENDING',
          qualityDescription: ''
        }
      } else {
        this.qualityForm = { ...this.assetData }
      }
    },

    handleSubmit() {
      if (this.dialogType === 'view') {
        this.handleClose()
        return
      }
      this.$refs.qualityForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.dialogType === 'add') {
              await addAssetQuality(this.qualityForm)
              this.$message.success('新增成功')
            } else {
              await updateAssetQuality(this.qualityForm)
              this.$message.success('更新成功')
            }
            this.$emit('refresh')
            this.handleClose()
          } catch (error) {
            this.$message.error(this.dialogType === 'add' ? '新增失败' : '更新失败')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.qualityForm.resetFields()
    }
  }
}
</script>
