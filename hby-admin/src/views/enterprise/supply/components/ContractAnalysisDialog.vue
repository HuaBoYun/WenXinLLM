<template>
  <el-dialog title="合同分析" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <div v-loading="loading">
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="合同总金额(元)" :value="analysis.totalAmount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="合同总数" :value="analysis.totalCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="即将到期(30天)" :value="analysis.expiringCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="合同类型数" :value="Object.keys(analysis.typeStats || {}).length" /></el-card>
        </el-col>
      </el-row>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按合同类型统计</b></div>
        <el-table :data="typeTableData" border size="small">
          <el-table-column prop="type" label="合同类型" />
          <el-table-column prop="count" label="数量" width="100" />
          <el-table-column prop="amount" label="金额(元)" width="150" />
        </el-table>
      </el-card>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按状态统计</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.statusStats || {})" :key="key">
            <el-tag :type="getStatusType(key)" style="margin:4px">{{ key }}: {{ val }}份</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never">
        <div slot="header"><b>供应商合同金额TOP5</b></div>
        <el-table :data="analysis.supplierTop5 || []" border size="small">
          <el-table-column type="index" label="排名" width="60" />
          <el-table-column prop="supplier" label="供应商" />
          <el-table-column prop="amount" label="合同金额(元)" width="150" />
        </el-table>
      </el-card>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ContractAnalysisDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, analysis: {} } },
  computed: {
    typeTableData() {
      const typeStats = this.analysis.typeStats || {}
      const typeAmountStats = this.analysis.typeAmountStats || {}
      return Object.keys(typeStats).map(key => ({ type: key, count: typeStats[key], amount: typeAmountStats[key] || 0 }))
    }
  },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAnalysis() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '待签署': 'warning', '执行中': 'primary', '已完成': 'success', '已到期': 'info', '已终止': 'danger' }[s] || 'info' },
    async loadAnalysis() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/contract/analysis', method: 'get' })
        if (res && res.data) this.analysis = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
