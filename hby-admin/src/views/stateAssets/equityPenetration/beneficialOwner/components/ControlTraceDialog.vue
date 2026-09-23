<template>
  <el-dialog title="控制链路追溯" :visible.sync="dialogVisible" width="620px" :before-close="handleClose">
    <div v-loading="loading">
      <!-- 基本信息 -->
      <el-descriptions v-if="ownerData.controllerEnterpriseName" :column="2" border size="small" style="margin-bottom: 16px">
        <el-descriptions-item label="控制人">{{ ownerData.controllerEnterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="被控制企业">{{ ownerData.controlledEnterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="控制比例">{{ ownerData.totalShareholdingRatio }}%</el-descriptions-item>
        <el-descriptions-item label="控制层级">{{ ownerData.controlLevel }}层</el-descriptions-item>
      </el-descriptions>

      <!-- 控制链路时间线 -->
      <div v-if="traceData.length > 0">
        <div style="font-size: 13px; color: #606266; margin-bottom: 12px;">
          <i class="el-icon-guide"></i> 控制链路（共 {{ traceData.length }} 个节点）
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in traceData"
            :key="index"
            :type="item.isStart ? 'primary' : item.isEnd ? 'success' : 'warning'"
            :icon="item.isStart ? 'el-icon-s-flag' : item.isEnd ? 'el-icon-location' : 'el-icon-arrow-down'"
            :size="item.isStart || item.isEnd ? 'large' : 'normal'"
            :color="item.isStart ? '#409EFF' : item.isEnd ? '#67C23A' : '#E6A23C'"
          >
            <div class="trace-step-title">
              <el-tag size="mini" :type="item.isStart ? 'primary' : item.isEnd ? 'success' : 'warning'">
                第{{ item.step }}层
              </el-tag>
              <span style="margin-left: 8px; font-weight: 600;">{{ item.entityName }}</span>
              <el-tag v-if="item.isStart" size="mini" type="info" style="margin-left: 8px">起点</el-tag>
              <el-tag v-if="item.isEnd" size="mini" type="success" style="margin-left: 8px">终点</el-tag>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
      <el-empty v-else description="暂无追溯数据，请先设置控制路径"></el-empty>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { traceControlSource } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'ControlTraceDialog',
  props: {
    visible: { type: Boolean, default: false },
    ownerData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      traceData: []
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
      if (val && this.ownerData.controllerId) {
        this.fetchData()
      } else if (!val) {
        this.traceData = []
      }
    }
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await traceControlSource({ controllerId: this.ownerData.controllerId })
        if (res.result === 200) {
          this.traceData = res.data || []
        } else {
          this.$message.error(res.msg || '获取追溯数据失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
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
.trace-step-title {
  display: flex;
  align-items: center;
  font-size: 13px;
  padding: 4px 0;
}
</style>