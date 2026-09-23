<template>
  <el-dialog title="库存分析" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <div v-loading="loading">
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="库存总价值(元)" :value="analysis.totalValue || 0" /></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="物料种类" :value="analysis.totalCount || 0" /></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="库存总数量" :value="analysis.totalStock || 0" /></el-card>
        </el-col>
      </el-row>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按分类统计</b></div>
        <el-table :data="categoryTableData" border size="small">
          <el-table-column prop="category" label="分类" />
          <el-table-column prop="count" label="物料数" width="100" />
          <el-table-column prop="value" label="库存价值(元)" width="150" />
        </el-table>
      </el-card>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>库存状态分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.statusStats || {})" :key="key">
            <el-tag :type="getStatusType(key)" size="medium" style="margin:4px;font-size:14px">{{ key }}: {{ val }}种</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never">
        <div slot="header"><b>按仓库分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.warehouseStats || {})" :key="key">
            <el-tag type="info" size="medium" style="margin:4px">{{ key }}: {{ val }}种</el-tag>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'InventoryAnalysisDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, analysis: {} } },
  computed: {
    categoryTableData() {
      const stats = this.analysis.categoryStats || {}
      const valueStats = this.analysis.categoryValueStats || {}
      return Object.keys(stats).map(key => ({ category: key, count: stats[key], value: valueStats[key] || 0 }))
    }
  },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAnalysis() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '正常': 'success', '偏低': 'warning', '过高': 'primary', '缺货': 'danger' }[s] || 'info' },
    async loadAnalysis() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/inventory/analysis', method: 'get' })
        if (res && res.data) this.analysis = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
