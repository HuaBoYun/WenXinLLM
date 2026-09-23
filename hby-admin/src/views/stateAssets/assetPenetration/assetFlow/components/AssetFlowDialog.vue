<template>
  <el-dialog
    :title="dialogType === 'add' ? '新增资产流向' : (dialogType === 'edit' ? '编辑资产流向' : '查看资产流向')"
    :visible.sync="dialogVisible"
    width="70%"
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
          <el-input v-model="formData.enterpriseName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="流向类型" prop="flowType">
          <el-select v-model="formData.flowType" placeholder="请选择流向类型">
            <el-option label="资产转移" value="TRANSFER"></el-option>
            <el-option label="资产注入" value="INJECTION"></el-option>
            <el-option label="资产剥离" value="DIVESTITURE"></el-option>
            <el-option label="资产重组" value="RESTRUCTURING"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="流转金额" prop="flowAmount">
          <el-input v-model.number="formData.flowAmount" placeholder="请输入流转金额" />
        </el-form-item>
        <el-form-item label="流向状态" prop="flowStatus">
          <el-select v-model="formData.flowStatus" placeholder="请选择流向状态">
            <el-option label="进行中" value="ONGOING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已暂停" value="SUSPENDED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="flowStartTime">
          <el-date-picker v-model="formData.flowStartTime" type="datetime" placeholder="请选择开始时间" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <!-- 查看模式 -->
      <div v-else class="view-mode">
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">企业名称</div>
              <div class="info-value">{{ formData.enterpriseName }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">流向类型</div>
              <div class="info-value">{{ formData.flowType }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="mb-20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">流转金额</div>
              <div class="info-value">{{ formData.flowAmount }}万元</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">流向状态</div>
              <div class="info-value">
                <el-tag :type="getStatusType(formData.flowStatus)">
                  {{ getStatusText(formData.flowStatus) }}
                </el-tag>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">开始时间</div>
              <div class="info-value">{{ formData.flowStartTime }}</div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <div class="info-label">备注</div>
              <div class="info-value">{{ formData.remark || '-' }}</div>
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
export default {
  name: 'AssetFlowDialog',
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
        flowStatus: '',
        flowStartTime: '',
        remark: ''
      },
      rules: {
        enterpriseName: [{ required: true, message: '企业名称不能为空', trigger: 'blur' }],
        flowType: [{ required: true, message: '流向类型不能为空', trigger: 'change' }],
        flowAmount: [{ required: true, message: '流转金额不能为空', trigger: 'blur' }],
        flowStatus: [{ required: true, message: '流向状态不能为空', trigger: 'change' }],
        flowStartTime: [{ required: true, message: '开始时间不能为空', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.dialogType = this.type
        if (this.data && Object.keys(this.data).length > 0) {
          this.formData = { ...this.data }
        } else {
          this.resetForm()
        }
      }
    }
  },
  methods: {
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
          // 模拟保存
          setTimeout(() => {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.$emit('save', this.formData)
            this.loading = false
            this.handleClose()
          }, 500)
        }
      })
    },
    resetForm() {
      this.formData = {
        enterpriseName: '',
        flowType: '',
        flowAmount: '',
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