<template>
  <el-dialog
    :title="dialogType === 'add' ? '新增资金流向' : (dialogType === 'edit' ? '编辑资金流向' : '查看资金流向')"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="flow-dialog-container">
      <el-form
        v-if="dialogType !== 'view'"
        :model="formData"
        :rules="rules"
        ref="form"
        label-width="120px"
      >
        <el-form-item label="企业名称" prop="enterpriseName">
          <el-input v-model="formData.enterpriseName" placeholder="请输入企业名称" style="width: 260px" />
        </el-form-item>
        <el-form-item label="流向类型" prop="flowType">
          <el-select v-model="formData.flowType" placeholder="请选择流向类型" style="width: 260px">
            <el-option label="资金流入" value="INFLOW"></el-option>
            <el-option label="资金流出" value="OUTFLOW"></el-option>
            <el-option label="内部转移" value="INTERNAL_TRANSFER"></el-option>
            <el-option label="投资收回" value="INVESTMENT_RECOVERY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="流转金额(万)" prop="flowAmount">
          <el-input v-model.number="formData.flowAmount" placeholder="请输入流转金额" style="width: 260px" />
        </el-form-item>
        <el-form-item label="资金性质" prop="fundNature">
          <el-select v-model="formData.fundNature" placeholder="请选择资金性质" style="width: 260px">
            <el-option label="经营性" value="OPERATING"></el-option>
            <el-option label="投资性" value="INVESTING"></el-option>
            <el-option label="筹资性" value="FINANCING"></el-option>
            <el-option label="自有资金" value="OWN_FUND"></el-option>
            <el-option label="借入资金" value="BORROWED_FUND"></el-option>
            <el-option label="投资资金" value="INVESTMENT_FUND"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="流向状态" prop="flowStatus">
          <el-select v-model="formData.flowStatus" placeholder="请选择流向状态" style="width: 260px">
            <el-option label="进行中" value="ONGOING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已暂停" value="SUSPENDED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="flowStartTime">
          <el-date-picker v-model="formData.flowStartTime" type="month" placeholder="请选择月份" value-format="yyyy-MM" style="width: 260px" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" style="width: 260px" :rows="3" />
        </el-form-item>
      </el-form>

      <!-- 查看模式 -->
      <div v-else class="view-mode">
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">源企业</div>
              <div class="info-value">{{ viewData.fromCompany || viewData.enterpriseName || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">目标企业</div>
              <div class="info-value">{{ viewData.toCompany || '-' }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">流向类型</div>
              <div class="info-value">
                <el-tag :type="getFlowTypeTag(viewData.flowType)">
                  {{ formatFlowType(viewData.flowType) }}
                </el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">交易类型</div>
              <div class="info-value">{{ viewData.purpose || '-' }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">流转金额</div>
              <div class="info-value">{{ viewData.amount || 0 }} 万元</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">资金性质</div>
              <div class="info-value">{{ formatFundNature(viewData.fundNature) }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">发生时间</div>
              <div class="info-value">{{ viewData.occurTime || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">风险描述</div>
              <div class="info-value">{{ viewData.riskDesc || '-' }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSave">保存</el-button>
      <el-button @click="handleClose">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addFundFlow, updateFundFlow } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) },
    type: { type: String, default: 'view' }
  },
  data() {
    return {
      loading: false,
      dialogType: 'view',
      formData: {
        enterpriseName: '',
        flowType: '',
        flowAmount: '',
        fundNature: '',
        flowStatus: '',
        flowStartTime: '',
        remark: ''
      },
      rules: {
        enterpriseName: [{ required: true, message: '企业名称不能为空', trigger: 'blur' }],
        flowType: [{ required: true, message: '流向类型不能为空', trigger: 'change' }],
        flowAmount: [{ required: true, message: '流转金额不能为空', trigger: 'blur' }],
        fundNature: [{ required: true, message: '资金性质不能为空', trigger: 'change' }],
        flowStatus: [{ required: true, message: '流向状态不能为空', trigger: 'change' }],
        flowStartTime: [{ required: true, message: '开始时间不能为空', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    // 查看模式直接使用原始数据（列表返回的字段名）
    viewData() {
      return this.data || {}
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.dialogType = this.type
        if (this.type === 'edit' && this.data && Object.keys(this.data).length > 0) {
          // 编辑模式：映射列表字段到表单字段
          this.formData = {
            id: this.data.id || '',
            enterpriseName: this.data.fromCompany || this.data.enterpriseName || '',
            flowType: this.data.purpose || this.data.flowType || '',
            flowAmount: this.data.amount || this.data.flowAmount || '',
            fundNature: this.data.fundNature || '',
            flowStatus: this.data.flowStatus || 'ONGOING',
            flowStartTime: this.data.occurTime || this.data.flowStartTime || '',
            remark: this.data.remark || ''
          }
        } else if (this.type === 'add') {
          this.resetForm()
        }
      }
    }
  },
  methods: {
    formatFlowType(value) {
      const map = {
        'INFLOW': '资金流入',
        'OUTFLOW': '资金流出',
        'INTERNAL_TRANSFER': '内部转移',
        'INVESTMENT_RECOVERY': '投资收回',
        'ABNORMAL': '重大关联',
        'NORMAL': '常规关联',
      }
      return map[value] || value || '-'
    },
    getFlowTypeTag(value) {
      const map = {
        'INFLOW': 'success',
        'OUTFLOW': 'warning',
        'INTERNAL_TRANSFER': 'info',
        'INVESTMENT_RECOVERY': '',
        'ABNORMAL': 'danger',
        'NORMAL': 'success',
      }
      return map[value] || 'info'
    },
    formatFundNature(value) {
      const map = {
        'OPERATING': '经营性',
        'INVESTING': '投资性',
        'FINANCING': '筹资性',
        'OWN_FUND': '自有资金',
        'BORROWED_FUND': '借入资金',
        'INVESTMENT_FUND': '投资资金',
        'OTHER': '其他',
      }
      return map[value] || value || '-'
    },
    getStatusType(status) {
      const typeMap = {
        'ONGOING': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'info'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停'
      }
      return textMap[status] || status
    },
    handleSave() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          // 格式化period为yyyy-MM，避免超出数据库列长度
          let period = this.formData.flowStartTime || ''
          if (period && period.length > 7) period = period.substring(0, 7)
          const params = {
            companyId: this.formData.enterpriseName,
            partyName: this.formData.enterpriseName,
            transactionType: this.formData.flowType,
            transactionAmount: this.formData.flowAmount,
            relationType: this.formData.fundNature,
            period: period,
            remark: this.formData.remark
          }
          if (this.formData.id) {
            params.id = this.formData.id
          }
          const apiCall = this.dialogType === 'add' ? addFundFlow(params) : updateFundFlow(params)
          apiCall.then(res => {
            if (res.code === 1 || res.result === 200 || res.data === true) {
              this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
              this.$emit('save', this.formData)
              this.handleClose()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(err => {
            this.$message.error(err.message || '请求失败，请稍后重试')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },
    resetForm() {
      this.formData = {
        enterpriseName: '',
        flowType: '',
        flowAmount: '',
        fundNature: '',
        flowStatus: '',
        flowStartTime: '',
        remark: ''
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.flow-dialog-container {
  padding: 10px;
}
.view-mode {
  padding: 20px;
}
.mb-20 {
  margin-bottom: 20px;
}
.info-item {
  padding: 10px;
}
.info-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}
.info-value {
  font-size: 14px;
  color: #303133;
}
.dialog-footer {
  text-align: right;
}
</style>