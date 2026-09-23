<template>
  <el-dialog title="风险分析" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <div v-loading="loading">
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="风险总数" :value="analysis.totalCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align:center">
              <div style="font-size:24px;font-weight:bold;color:#F56C6C">{{ analysis.avgRiskScore || 0 }}</div>
              <div style="color:#909399;font-size:13px;margin-top:4px">平均风险评分</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="高风险数" :value="(analysis.levelStats || {})['高风险'] || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="极高风险数" :value="(analysis.levelStats || {})['极高风险'] || 0" /></el-card>
        </el-col>
      </el-row>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按风险等级分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.levelStats || {})" :key="key">
            <el-tag :type="getLevelType(key)" size="medium" style="margin:4px;font-size:14px">{{ key }}: {{ val }}项</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按风险类型分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.typeStats || {})" :key="key">
            <el-tag type="primary" style="margin:4px">{{ key }}: {{ val }}项</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按处理状态分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.statusStats || {})" :key="key">
            <el-tag :type="getStatusType(key)" style="margin:4px">{{ key }}: {{ val }}项</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never">
        <div slot="header"><b>供应商风险评分TOP5</b></div>
        <el-table :data="analysis.supplierRiskTop5 || []" border size="small">
          <el-table-column type="index" label="排名" width="60" />
          <el-table-column prop="supplier" label="供应商" />
          <el-table-column prop="riskCount" label="风险数" width="80" />
          <el-table-column prop="totalScore" label="累计评分" width="100" />
        </el-table>
      </el-card>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'RiskAnalysisDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, analysis: {} } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAnalysis() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getLevelType(l) { return { '低风险': 'success', '中风险': 'warning', '高风险': 'danger', '极高风险': 'danger' }[l] || 'info' },
    getStatusType(s) { return { '已识别': 'info', '处理中': 'warning', '已解决': 'success', '监控中': 'primary' }[s] || 'info' },
    async loadAnalysis() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/risk/analysis', method: 'get' })
        if (res && res.data) this.analysis = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
