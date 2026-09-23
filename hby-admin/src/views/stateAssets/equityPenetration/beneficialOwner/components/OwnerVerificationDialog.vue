<template>
  <el-dialog title="身份验证" :visible.sync="dialogVisible" width="520px" :before-close="handleClose">
    <div v-loading="loading">
      <el-descriptions :column="1" border size="small" style="margin-bottom: 20px">
        <el-descriptions-item label="控制人">{{ ownerData.controllerEnterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="被控制企业">{{ ownerData.controlledEnterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="控制比例">{{ ownerData.totalShareholdingRatio }}%</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="ownerData.confirmationStatus === 'CONFIRMED' ? 'success' : 'warning'">
            {{ ownerData.confirmationStatus === 'CONFIRMED' ? '已验证' : '待验证' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 验证结果展示 -->
      <div v-if="verifyResult" class="verify-result">
        <div class="verify-result-header">
          <i class="el-icon-success verify-icon"></i>
          <span class="verify-msg">{{ verifyResult.message }}</span>
        </div>
        <el-descriptions :column="1" border size="mini" style="margin-top: 12px">
          <el-descriptions-item label="验证状态">
            <el-tag type="success" size="small">{{ verifyResult.status === 'CONFIRMED' ? '已确认' : verifyResult.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="控制人名称">{{ verifyResult.controllerName }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" :loading="loading" @click="handleVerify"
        :disabled="ownerData.confirmationStatus === 'CONFIRMED'">
        {{ ownerData.confirmationStatus === 'CONFIRMED' ? '已验证' : '执行验证' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { verifyBeneficialOwnerIdentity } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'OwnerVerificationDialog',
  props: {
    visible: { type: Boolean, default: false },
    ownerData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      verifyResult: null
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
      if (!val) {
        this.verifyResult = null
      }
    }
  },
  methods: {
    async handleVerify() {
      this.loading = true
      try {
        const res = await verifyBeneficialOwnerIdentity({ controllerId: this.ownerData.controllerId })
        if (res.result === 200) {
          this.verifyResult = res.data
          this.$message.success('验证成功')
          this.$emit('refresh')
        } else {
          this.$message.error(res.msg || '验证失败')
        }
      } catch (e) {
        this.$message.error('验证请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.verify-result {
  padding: 16px;
  background: #f0f9eb;
  border-radius: 4px;
  margin-bottom: 16px;
}
.verify-result-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
}
.verify-icon {
  color: #67c23a;
  font-size: 18px;
  margin-right: 8px;
}
.verify-msg {
  color: #67c23a;
  font-weight: 600;
}
</style>