<template>
  <el-dialog title="控制关系图谱" :visible.sync="dialogVisible" width="720px" :before-close="handleClose">
    <div v-loading="loading">
      <div v-if="mapData && mapData.nodes && mapData.nodes.length">
        <!-- 图谱可视化 -->
        <div class="map-container">
          <div class="map-nodes">
            <div v-for="node in mapData.nodes" :key="node.id" class="map-node-wrapper">
              <div :class="['map-node', node.type === 'controller' ? 'node-controller' : 'node-controlled']">
                <i :class="node.type === 'controller' ? 'el-icon-user-solid' : 'el-icon-office-building'"
                   style="font-size: 20px; display: block; margin-bottom: 6px;"></i>
                <span>{{ node.name }}</span>
              </div>
              <div class="map-node-label">
                <el-tag size="mini" :type="node.type === 'controller' ? 'primary' : 'success'">
                  {{ node.type === 'controller' ? '控制方' : '被控制方' }}
                </el-tag>
              </div>
            </div>
          </div>
          <!-- 控制关系箭头 -->
          <div v-if="mapData.links && mapData.links.length" class="map-arrow-area">
            <div class="map-arrow">
              <div class="arrow-line"></div>
              <div class="arrow-head">▶</div>
            </div>
            <div class="arrow-label">
              <el-tag type="warning" size="small">控制比例: {{ mapData.links[0].ratio }}%</el-tag>
            </div>
          </div>
        </div>

        <!-- 详细信息 -->
        <el-divider content-position="left">控制关系详情</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="控制方">{{ mapData.controllerName }}</el-descriptions-item>
          <el-descriptions-item label="被控制方">{{ mapData.controlledName }}</el-descriptions-item>
          <el-descriptions-item label="控制方式">{{ getControlMethodText(mapData.controlMethod) }}</el-descriptions-item>
          <el-descriptions-item label="综合持股比例">
            <span :style="{ color: getRatioColor(mapData.controlRatio), fontWeight: 'bold' }">
              {{ mapData.controlRatio }}%
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="控制层级">{{ mapData.controlLevel }}层</el-descriptions-item>
          <el-descriptions-item label="控制路径">{{ mapData.controlPath || '—' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else-if="!loading" description="暂无图谱数据"></el-empty>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getControlMap } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'BeneficialOwnerMapDialog',
  props: {
    visible: { type: Boolean, default: false },
    ownerData: { type: Object, default: () => ({}) }
  },
  data() {
    return { loading: false, mapData: null }
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
        this.mapData = null
      }
    }
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getControlMap({ controllerId: this.ownerData.controllerId })
        if (res.result === 200) {
          this.mapData = res.data
        } else {
          this.$message.error(res.msg || '获取图谱数据失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() { this.dialogVisible = false },
    getControlMethodText(m) {
      const map = { SHAREHOLDING: '股权控制', VOTING_RIGHT: '表决权控制', AGREEMENT: '协议控制', MIXED: '混合控制', MANAGEMENT: '管理控制' }
      return map[m] || m || '—'
    },
    getRatioColor(r) {
      if (!r) return '#606266'
      if (r >= 80) return '#F56C6C'
      if (r >= 50) return '#E6A23C'
      return '#67C23A'
    }
  }
}
</script>

<style scoped>
.map-container { text-align: center; padding: 20px 0; position: relative; }
.map-nodes { display: flex; align-items: center; justify-content: center; gap: 80px; }
.map-node-wrapper { text-align: center; }
.map-node {
  width: 110px; height: 110px; border-radius: 50%;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  color: #fff; font-size: 12px; font-weight: 600; padding: 10px; word-break: break-all;
}
.node-controller { background: linear-gradient(135deg, #409EFF, #0050A0); }
.node-controlled { background: linear-gradient(135deg, #67C23A, #3a8a1a); }
.map-node-label { margin-top: 10px; }
.map-arrow-area { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); text-align: center; }
.map-arrow { display: flex; align-items: center; }
.arrow-line { width: 60px; height: 2px; background: #E6A23C; }
.arrow-head { color: #E6A23C; font-size: 14px; }
.arrow-label { margin-top: 4px; }
</style>
