<template>
  <el-dialog title="控制路径分析" :visible.sync="dialogVisible" width="750px" :before-close="handleClose">
    <div v-loading="loading">
      <div v-if="chainData && chainData.enterpriseName">
        <el-alert
          :title="`${chainData.controllerName} → ${chainData.enterpriseName} 控制路径`"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom:16px"
        />

        <!-- 路径概览表 -->
        <el-divider content-position="left">所有控制路径</el-divider>
        <el-table :data="pathDetails" border size="small" style="width:100%">
          <el-table-column label="路径编号" type="index" width="80" align="center" />
          <el-table-column label="路径描述" prop="desc" min-width="260" show-overflow-tooltip />
          <el-table-column label="路径长度" prop="length" width="90" align="center">
            <template slot-scope="{ row }">
              <el-tag size="small" :type="row.length > 3 ? 'danger' : 'primary'">{{ row.length }}级</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="综合控制力" prop="strength" width="110" align="center">
            <template slot-scope="{ row }">
              <el-progress
                :percentage="row.strength"
                :color="row.strength < 50 ? '#f56c6c' : '#67c23a'"
                style="width:80px;display:inline-block"
                :show-text="false"
              />
              <span style="margin-left:6px">{{ row.strength }}%</span>
            </template>
          </el-table-column>
          <el-table-column label="路径类型" prop="type" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.type === '直接控制' ? 'success' : 'warning'" size="small">{{ row.type }}</el-tag>
            </template>
          </el-table-column>
        </el-table>

        <!-- 逐步路径明细 -->
        <el-divider content-position="left">路径步骤明细</el-divider>
        <el-table :data="paths" border size="small" style="width:100%">
          <el-table-column label="步骤" prop="step" width="70" align="center" />
          <el-table-column label="起始节点" prop="from" min-width="160" show-overflow-tooltip />
          <el-table-column label="目标节点" prop="to" min-width="160" show-overflow-tooltip />
          <el-table-column label="控制力" prop="strength" width="110" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.strength }}%</span>
            </template>
          </el-table-column>
          <el-table-column label="类型" prop="type" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.type === 'DIRECT' ? 'success' : 'warning'" size="mini">
                {{ row.type === 'DIRECT' ? '直接' : '间接' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <!-- 路径风险评估 -->
        <el-divider content-position="left">路径风险评估</el-divider>
        <el-alert
          v-if="chainData.chainLength > 3"
          title="控制路径较长，信息传导可能存在衰减，建议优化层级结构"
          type="warning"
          show-icon
          :closable="false"
          style="margin-bottom:8px"
        />
        <el-alert
          v-if="chainData.controlStrength < 50"
          :title="`综合控制力仅${chainData.controlStrength}%，存在失控风险`"
          type="error"
          show-icon
          :closable="false"
          style="margin-bottom:8px"
        />
        <el-alert
          v-if="chainData.chainLength <= 3 && chainData.controlStrength >= 50"
          title="控制路径健康，无明显风险"
          type="success"
          show-icon
          :closable="false"
        />
      </div>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { analyzeControlPaths } from '@/api/stateAssets/controlChain'

export default {
  name: 'ControlPathDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      paths: [],
      pathDetails: [],
      loading: false
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
      if (val && this.chainData && this.chainData.chainId) {
        this.fetchControlPaths()
      }
      if (!val) {
        this.paths = []
        this.pathDetails = []
      }
    }
  },
  methods: {
    async fetchControlPaths() {
      this.loading = true
      try {
        const res = await analyzeControlPaths({ chainId: this.chainData.chainId })
        if (res && res.result === 200 && res.data) {
          this.paths = res.data.paths || []
          this.pathDetails = res.data.pathDetails || []
        } else {
          this.$message.error((res && res.msg) || '获取控制路径数据失败')
        }
      } catch (e) {
        console.error('控制路径分析请求失败', e)
        this.$message.error('请求失败，请稍后重试')
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