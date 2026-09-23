<template>
  <el-dialog
    title="控制链详情分析"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="detail-container">
      <!-- 基本信息 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>基本信息</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="企业名称">
            {{ chainData.enterpriseName }}
          </el-descriptions-item>
          <el-descriptions-item label="控制链总数">
            {{ chainData.totalChains || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="直接控制链">
            {{ chainData.directChains || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="间接控制链">
            {{ chainData.indirectChains || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="平均控制强度">
            <el-progress
              :percentage="chainData.avgControlStrength || 0"
              :color="getControlStrengthColor(chainData.avgControlStrength)"
              :show-text="true"
              :format="(percentage) => `${percentage}%`"
            />
          </el-descriptions-item>
          <el-descriptions-item label="平均链长">
            {{ chainData.avgChainLength || 0 }}
          </el-descriptions-item>
          <el-descriptions-item label="最大链长">
            <el-tag :type="(chainData.maxChainLength || 0) > 4 ? 'danger' : 'primary'" size="small">
              {{ chainData.maxChainLength || 0 }} 级
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="环路数">
            <el-tag :type="(chainData.loopCount || 0) > 0 ? 'danger' : 'success'" size="small">
              {{ chainData.loopCount || 0 }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="统计周期">
            {{ chainData.statPeriod }}
          </el-descriptions-item>
          <el-descriptions-item label="统计时间">
            {{ chainData.statTime }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ chainData.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ chainData.updateTime }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 风险分布 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>风险分布</span>
        </div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="高风险数">
            <el-tag type="danger" size="small">{{ chainData.highRiskCount || 0 }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="中风险数">
            <el-tag type="warning" size="small">{{ chainData.mediumRiskCount || 0 }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="低风险数">
            <el-tag type="success" size="small">{{ chainData.lowRiskCount || 0 }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 控制链结构分析 -->
      <el-card shadow="never">
        <div slot="header">
          <span>控制链结构分析</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="直接控制占比">
            {{ directRatio }}%
          </el-descriptions-item>
          <el-descriptions-item label="间接控制占比">
            {{ indirectRatio }}%
          </el-descriptions-item>
          <el-descriptions-item label="链长风险评估">
            <el-tag :type="(chainData.maxChainLength || 0) > 4 ? 'danger' : (chainData.maxChainLength || 0) > 2 ? 'warning' : 'success'" size="small">
              {{ (chainData.maxChainLength || 0) > 4 ? '高风险（链条过长）' : (chainData.maxChainLength || 0) > 2 ? '中等风险' : '低风险' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="环路风险评估">
            <el-tag :type="(chainData.loopCount || 0) > 0 ? 'danger' : 'success'" size="small">
              {{ (chainData.loopCount || 0) > 0 ? '存在环路，需关注' : '无环路' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ControlChainDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    chainData: {
      type: Object,
      default: () => ({})
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
    directRatio() {
      const total = this.chainData.totalChains || 0
      if (total === 0) return 0
      return ((this.chainData.directChains || 0) / total * 100).toFixed(1)
    },
    indirectRatio() {
      const total = this.chainData.totalChains || 0
      if (total === 0) return 0
      return ((this.chainData.indirectChains || 0) / total * 100).toFixed(1)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getControlStrengthColor(strength) {
      if (strength >= 80) return '#F56C6C'
      if (strength >= 60) return '#E6A23C'
      if (strength >= 40) return '#409EFF'
      return '#67C23A'
    }
  }
}
</script>

<style scoped>
.detail-container {
  max-height: 70vh;
  overflow-y: auto;
}
.mb-20 {
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>
