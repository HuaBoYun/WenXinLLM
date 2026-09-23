<template>
  <el-dialog title="质量分析" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <div v-loading="loading">
      <el-row :gutter="16" style="margin-bottom:20px">
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="检验总数" :value="analysis.totalCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="合格数" :value="analysis.qualifiedCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover"><el-statistic title="不合格数" :value="analysis.unqualifiedCount || 0" /></el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div style="text-align:center">
              <div style="font-size:24px;font-weight:bold;color:#67C23A">{{ analysis.overallQualificationRate || 0 }}%</div>
              <div style="color:#909399;font-size:13px;margin-top:4px">综合合格率</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>按检验类型统计</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.typeStats || {})" :key="key">
            <el-tag type="primary" style="margin:4px">{{ key }}: {{ val }}次</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header"><b>检验结果分布</b></div>
        <el-row :gutter="12">
          <el-col :span="6" v-for="(val, key) in (analysis.resultStats || {})" :key="key">
            <el-tag :type="getResultType(key)" size="medium" style="margin:4px;font-size:14px">{{ key }}: {{ val }}次</el-tag>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never">
        <div slot="header"><b>供应商不合格次数TOP5</b></div>
        <el-table :data="analysis.supplierUnqualifiedTop5 || []" border size="small">
          <el-table-column type="index" label="排名" width="60" />
          <el-table-column prop="supplier" label="供应商" />
          <el-table-column prop="count" label="不合格次数" width="120" />
        </el-table>
      </el-card>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'QualityAnalysisDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, analysis: {} } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAnalysis() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getResultType(r) { return { '合格': 'success', '不合格': 'danger', '待复检': 'warning' }[r] || 'info' },
    async loadAnalysis() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/quality/analysis', method: 'get' })
        if (res && res.data) this.analysis = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
