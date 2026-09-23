<template>
  <el-dialog
    title="批量审批"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="batch-approval-container">
      <el-alert
        title="提示"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      >
        <div slot="default">
          已选择 <strong>{{ selectedItems.length }}</strong> 条记录进行批量审批
        </div>
      </el-alert>

      <div class="selected-list">
        <el-table
          :data="selectedItems"
          border
          stripe
          max-height="300"
        >
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="number" label="单号" width="150" />
          <el-table-column prop="assetCode" label="资产编码" width="120" />
          <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.amount) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-form
        ref="approvalForm"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        size="small"
        style="margin-top: 20px"
      >
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="formData.approvalResult">
            <el-radio label="APPROVED">全部通过</el-radio>
            <el-radio label="REJECTED">全部拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalComment">
          <el-input
            v-model="formData.approvalComment"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BatchApprovalDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    selectedItems: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        approvalResult: 'APPROVED',
        approvalComment: ''
      },
      formRules: {
        approvalResult: [{ required: true, message: '请选择审批结果', trigger: 'change' }],
        approvalComment: [{ required: true, message: '请输入审批意见', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.approvalForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.approvalForm.resetFields()
      this.$emit('update:visible', false)
    },
    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.batch-approval-container {
  padding: 10px 0;
}

.selected-list {
  margin-bottom: 20px;
}
</style>

