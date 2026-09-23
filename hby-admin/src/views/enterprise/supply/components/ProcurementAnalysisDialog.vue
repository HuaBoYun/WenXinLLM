<template>
  <el-dialog title="采购分析" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <div v-loading="loading">
      <!-- 总览卡片 -->
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="采购总金额(元)" :value="analysis.totalAmount || 0" /></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="采购总笔数" :value="analysis.totalCount || 0" /></el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover"><el-statistic title="平均采购金额(元)" :value="analysis.avgAmount || 0" /></el-card>
        </el-col>
      </el-row>

      <!-- 按类型统计 -->
      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按采购类型统计</b></div>
        <el-table :data="typeTableData" border size="small">
          <el-table-column prop="type" label="采购类型" />
          <el-table-column prop="count" label="笔数" width="100" />
          <el-table-column prop="amount" label="金额(元)" width="150" />
        </el-table>
      </el-card>

      <!-- 按状态统计 -->
      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按状态统计</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.statusStats || {})" :key="key">
            <el-tag :type="getStatusType(key)" style="margin:4px">{{ key }}: {{ val }}笔</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <!-- 供应商TOP5 -->
      <el-card shadow="never">
        <div slot="header"><b>供应商采购金额TOP5</b></div>
        <el-table :data="analysis.supplierTop5 || []" border size="small">
          <el-table-column type="index" label="排名" width="60" />
          <el-table-column prop="supplier" label="供应商" />
          <el-table-column prop="amount" label="采购金额(元)" width="150" />
        </el-table>
      </el-card>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ProcurementAnalysisDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, analysis: {} } },
  computed: {
    typeTableData() {
      const typeStats = this.analysis.typeStats || {}
      const typeAmountStats = this.analysis.typeAmountStats || {}
      return Object.keys(typeStats).map(key => ({ type: key, count: typeStats[key], amount: typeAmountStats[key] || 0 }))
    }
  },
  watch: {
    visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAnalysis() } }
  },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '待审批': 'warning', '已审批': 'primary', '采购中': 'info', '已完成': 'success', '已取消': 'danger' }[s] || 'info' },
    async loadAnalysis() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/procurement/analysis', method: 'get' })
        if (res && res.data) this.analysis = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
