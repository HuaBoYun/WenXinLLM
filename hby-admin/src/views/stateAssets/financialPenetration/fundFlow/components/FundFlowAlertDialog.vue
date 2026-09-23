<template>
  <el-dialog
    title="资金流向预警设置"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="alert-dialog-container">
      <el-form :model="alertForm" label-width="120px">
        <el-form-item label="预警名称">
          <el-input v-model="alertForm.alertName" placeholder="请输入预警名称" />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="alertForm.alertType" placeholder="请选择预警类型">
            <el-option label="金额异常" value="AMOUNT_ABNORMAL"></el-option>
            <el-option label="频率异常" value="FREQUENCY_ABNORMAL"></el-option>
            <el-option label="路径异常" value="PATH_ABNORMAL"></el-option>
            <el-option label="时间异常" value="TIME_ABNORMAL"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警阈值">
          <el-input v-model.number="alertForm.threshold" placeholder="请输入预警阈值" />
        </el-form-item>
        <el-form-item label="预警等级">
          <el-select v-model="alertForm.alertLevel" placeholder="请选择预警等级">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="alertForm.enabled" />
        </el-form-item>
        <el-form-item label="通知方式">
          <el-checkbox-group v-model="alertForm.notifyMethods">
            <el-checkbox label="邮件" value="EMAIL"></el-checkbox>
            <el-checkbox label="短信" value="SMS"></el-checkbox>
            <el-checkbox label="系统消息" value="SYSTEM_MESSAGE"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="alertForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>

      <!-- 已有预警规则 -->
      <el-card class="mt-20">
        <div slot="header">
          <span>已有预警规则</span>
        </div>
        <el-table :data="existingAlerts" stripe border>
          <el-table-column prop="alertName" label="预警名称" min-width="150" />
          <el-table-column prop="alertType" label="预警类型" width="120" align="center" />
          <el-table-column prop="threshold" label="阈值" width="100" align="center" />
          <el-table-column prop="alertLevel" label="等级" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="getAlertLevelType(scope.row.alertLevel)">
                {{ scope.row.alertLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="enabled" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.enabled ? 'success' : 'info'">
                {{ scope.row.enabled ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editAlert(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" @click="deleteAlert(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSave">保存</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { setFundFlowAlert, getFundFlowAlertList, deleteFundFlowAlert } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowAlertDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      alertForm: {
        alertId: '',
        alertName: '',
        alertType: '',
        threshold: '',
        alertLevel: 'MEDIUM',
        enabled: true,
        notifyMethods: ['SYSTEM_MESSAGE'],
        remark: ''
      },
      existingAlerts: []
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
        this.loadAlerts()
      }
    }
  },
  methods: {
    async loadAlerts() {
      this.loading = true
      try {
        const res = await getFundFlowAlertList()
        this.existingAlerts = (res && res.data) || []
      } catch (error) {
        this.$message.error('获取预警规则列表失败')
        this.existingAlerts = []
      } finally {
        this.loading = false
      }
    },
    getAlertLevelType(level) {
      const typeMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return typeMap[level] || 'info'
    },
    editAlert(row) {
      this.alertForm = {
        alertId: row.alertId || row.id || '',
        alertName: row.alertName || '',
        alertType: row.alertType || '',
        threshold: row.threshold || '',
        alertLevel: row.alertLevel || 'MEDIUM',
        enabled: row.enabled !== undefined ? row.enabled : true,
        notifyMethods: row.notifyMethods || ['SYSTEM_MESSAGE'],
        remark: row.remark || ''
      }
    },
    deleteAlert(row) {
      this.$confirm('确认删除该预警规则？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteFundFlowAlert(row.alertId)
          this.$message.success('删除成功')
          this.loadAlerts()
        } catch (error) {
          this.$message.error('删除预警规则失败')
        }
      }).catch(() => {})
    },
    async handleSave() {
      if (!this.alertForm.alertName) {
        this.$message.warning('请输入预警名称')
        return
      }
      if (!this.alertForm.alertType) {
        this.$message.warning('请选择预警类型')
        return
      }
      this.loading = true
      try {
        const params = { ...this.alertForm }
        if (params.alertId) {
          params.id = params.alertId
        }
        await setFundFlowAlert(params)
        this.$message.success(params.alertId ? '预警规则已更新' : '预警规则已保存')
        this.resetForm()
        this.loadAlerts()
        this.$emit('refresh')
      } catch (error) {
        this.$message.error('保存预警规则失败')
      } finally {
        this.loading = false
      }
    },
    resetForm() {
      this.alertForm = {
        alertId: '',
        alertName: '',
        alertType: '',
        threshold: '',
        alertLevel: 'MEDIUM',
        enabled: true,
        notifyMethods: ['SYSTEM_MESSAGE'],
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
.alert-dialog-container {
  padding: 10px;
}
.mt-20 {
  margin-top: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>