<template>
  <el-dialog title="变更历史" :visible.sync="dialogVisible" width="620px" :before-close="handleClose">
    <div v-loading="loading">
      <!-- 基本信息 -->
      <el-descriptions v-if="ownerData.controllerEnterpriseName" :column="2" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="控制人">{{ ownerData.controllerEnterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="被控制企业">{{ ownerData.controlledEnterpriseName }}</el-descriptions-item>
      </el-descriptions>

      <el-timeline v-if="historyData.length > 0">
        <el-timeline-item
          v-for="(item, index) in historyData"
          :key="index"
          :timestamp="formatTime(item.time)"
          placement="top"
          :type="index === 0 ? 'primary' : ''"
        >
          <el-card shadow="hover" class="history-card">
            <div class="history-action">
              <i class="el-icon-edit-outline" style="margin-right: 4px; color: #409EFF;"></i>
              {{ item.action }}
            </div>
            <div class="history-detail">{{ item.detail }}</div>
            <div class="history-operator">
              <i class="el-icon-user" style="margin-right: 4px;"></i>操作人: {{ item.operator }}
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无变更历史"></el-empty>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getControlHistory } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'OwnerHistoryDialog',
  props: {
    visible: { type: Boolean, default: false },
    ownerData: { type: Object, default: () => ({}) }
  },
  data() {
    return { loading: false, historyData: [] }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val && this.ownerData.controllerId) {
        this.fetchData()
      } else if (!val) {
        this.historyData = []
      }
    }
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getControlHistory({ controllerId: this.ownerData.controllerId })
        if (res.result === 200) {
          this.historyData = res.data || []
        } else {
          this.$message.error(res.msg || '获取变更历史失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() { this.dialogVisible = false },
    formatTime(str) {
      if (!str) return '—'
      return str.replace('T', ' ').substring(0, 19)
    }
  }
}
</script>

<style scoped>
.history-card { padding: 8px; }
.history-action { font-weight: 600; margin-bottom: 6px; color: #303133; }
.history-detail { font-size: 12px; color: #606266; margin-bottom: 4px; }
.history-operator { font-size: 12px; color: #909399; }
</style>
