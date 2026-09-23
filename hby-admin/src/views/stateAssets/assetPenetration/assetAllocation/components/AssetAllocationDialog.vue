<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="allocationForm"
      :model="allocationForm"
      :rules="rules"
      label-width="120px"
      :disabled="dialogType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="allocationForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产类型" prop="assetType">
            <el-select v-model="allocationForm.assetType" placeholder="请选择资产类型" style="width: 100%;">
              <el-option label="固定资产" value="FIXED_ASSETS"></el-option>
              <el-option label="流动资产" value="CURRENT_ASSETS"></el-option>
              <el-option label="无形资产" value="INTANGIBLE_ASSETS"></el-option>
              <el-option label="投资性资产" value="INVESTMENT_ASSETS"></el-option>
              <el-option label="金融资产" value="FINANCIAL_ASSETS"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产规模(万元)" prop="assetAmount">
            <el-input-number
              v-model="allocationForm.assetAmount"
              :min="0"
              :precision="2"
              placeholder="请输入资产规模"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配置比例(%)" prop="allocationRatio">
            <el-input-number
              v-model="allocationForm.allocationRatio"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入配置比例"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属地区" prop="region">
            <el-select v-model="allocationForm.region" placeholder="请选择地区" style="width: 100%;">
              <el-option label="华北地区" value="NORTH_CHINA"></el-option>
              <el-option label="华东地区" value="EAST_CHINA"></el-option>
              <el-option label="华南地区" value="SOUTH_CHINA"></el-option>
              <el-option label="华中地区" value="CENTRAL_CHINA"></el-option>
              <el-option label="西北地区" value="NORTHWEST_CHINA"></el-option>
              <el-option label="西南地区" value="SOUTHWEST_CHINA"></el-option>
              <el-option label="东北地区" value="NORTHEAST_CHINA"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属行业" prop="industry">
            <el-select v-model="allocationForm.industry" placeholder="请选择行业" style="width: 100%;">
              <el-option label="制造业" value="MANUFACTURING"></el-option>
              <el-option label="金融业" value="FINANCE"></el-option>
              <el-option label="房地产业" value="REAL_ESTATE"></el-option>
              <el-option label="建筑业" value="CONSTRUCTION"></el-option>
              <el-option label="交通运输业" value="TRANSPORTATION"></el-option>
              <el-option label="信息技术业" value="IT"></el-option>
              <el-option label="能源业" value="ENERGY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="allocationForm.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
              <el-option label="低风险" value="LOW"></el-option>
              <el-option label="中风险" value="MEDIUM"></el-option>
              <el-option label="高风险" value="HIGH"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收益率(%)" prop="yieldRate">
            <el-input-number
              v-model="allocationForm.yieldRate"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入收益率"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="allocationForm.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addAssetAllocation, updateAssetAllocation } from '@/api/stateAssets/assetAllocation'

export default {
  name: 'AssetAllocationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    allocationData: {
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
      submitting: false,
      allocationForm: {
        enterpriseName: '',
        assetType: '',
        assetAmount: 0,
        allocationRatio: 0,
        region: '',
        industry: '',
        riskLevel: 'LOW',
        yieldRate: 0,
        remark: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        assetType: [
          { required: true, message: '请选择资产类型', trigger: 'change' }
        ],
        assetAmount: [
          { required: true, message: '请输入资产规模', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择所属地区', trigger: 'change' }
        ],
        industry: [
          { required: true, message: '请选择所属行业', trigger: 'change' }
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
        add: '新增资产配置',
        edit: '编辑资产配置',
        view: '查看资产配置'
      }
      return titleMap[this.dialogType] || '资产配置'
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
        this.allocationForm = {
          enterpriseName: '',
          assetType: '',
          assetAmount: 0,
          allocationRatio: 0,
          region: '',
          industry: '',
          riskLevel: 'LOW',
          yieldRate: 0,
          remark: ''
        }
      } else {
        this.allocationForm = {
          allocationId: this.allocationData.allocationId,
          enterpriseName: this.allocationData.enterpriseName || this.allocationData.companyName || '',
          assetType: this.allocationData.assetType || '',
          assetAmount: this.allocationData.assetValue || this.allocationData.assetAmount || 0,
          allocationRatio: this.allocationData.allocationRatio || 0,
          region: this.allocationData.region || '',
          industry: this.allocationData.industry || '',
          riskLevel: this.allocationData.riskLevel || 'LOW',
          yieldRate: this.allocationData.allocationEfficiency || this.allocationData.yieldRate || 0,
          remark: this.allocationData.remark || ''
        }
      }
    },

    handleSubmit() {
      this.$refs.allocationForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const apiFunc = this.dialogType === 'add' ? addAssetAllocation : updateAssetAllocation
            const response = await apiFunc(this.allocationForm)
            if (response && response.result === 200) {
              this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
              this.$emit('refresh')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          } catch (error) {
            this.$message.error('操作失败')
          } finally {
            this.submitting = false
          }
        }
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
      if (this.$refs.allocationForm) {
        this.$refs.allocationForm.resetFields()
      }
    }
  }
}
</script>
