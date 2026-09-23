<template>
  <el-dialog title="风险监控" :visible.sync="dialogVisible" width="950px" @close="handleClose">
    <div v-loading="loading">
      <el-alert :title="`共 ${monitorList.length} 条未解决风险正在监控中，按风险评分降序排列`" type="warning" :closable="false" style="margin-bottom:16px" />
      <el-table :data="monitorList" border stripe size="small">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="riskNo" label="风险编号" width="120" />
        <el-table-column prop="riskName" label="风险名称" width="160" show-overflow-tooltip />
        <el-table-column prop="supplierName" label="供应商" width="140" />
        <el-table-column prop="riskType" label="类型" width="90">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getTypeColor(scope.row.riskType)">{{ scope.row.riskType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="等级" width="80">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="评分" width="70">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.riskScore >= 70 ? '#F56C6C' : scope.row.riskScore >= 50 ? '#E6A23C' : '#67C23A', fontWeight: 'bold' }">{{ scope.row.riskScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskDescription" label="风险描述" width="200" show-overflow-tooltip />
        <el-table-column prop="controlMeasure" label="管控措施" width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="负责人" width="80" />
      </el-table>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'RiskMonitorDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, monitorList: [] } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadMonitor() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getTypeColor(t) { return { '质量风险': 'danger', '交付风险': 'warning', '价格风险': 'primary', '合规风险': 'info' }[t] || 'info' },
    getLevelType(l) { return { '低风险': 'success', '中风险': 'warning', '高风险': 'danger', '极高风险': 'danger' }[l] || 'info' },
    getStatusType(s) { return { '已识别': 'info', '处理中': 'warning', '监控中': 'primary' }[s] || 'info' },
    async loadMonitor() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/risk/monitor', method: 'get' })
        if (res && res.data) this.monitorList = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
