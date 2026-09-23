<template>
  <el-dialog title="监管报告" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <div class="report-container" ref="reportContent">
      <div class="report-header">
        <h2>负责人监管报告</h2>
        <p class="report-date">报告生成时间：{{ reportDate }}</p>
      </div>
      <el-divider />
      <div class="report-section">
        <h4>一、基本信息</h4>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="负责人">{{ data.leaderName }}</el-descriptions-item>
          <el-descriptions-item label="所属企业">{{ data.company || data.enterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="监管类型">{{ data.supervisionType }}</el-descriptions-item>
          <el-descriptions-item label="监管时间">{{ data.supervisionDate }}</el-descriptions-item>
          <el-descriptions-item label="监管人">{{ data.supervisor }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag size="mini" :type="getRiskType(data.riskLevel)">{{ data.riskLevel }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="report-section">
        <h4>二、整改状态</h4>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="当前状态">
            <el-tag size="mini" :type="getComplianceType(data.complianceStatus || data.rectificationStatus)">
              {{ data.complianceStatus || data.rectificationStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="问题数量">{{ data.issueCount || 0 }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="report-section">
        <h4>三、监管内容</h4>
        <div class="report-text">{{ data.supervisionContent || '暂无' }}</div>
      </div>
      <div class="report-section">
        <h4>四、发现问题</h4>
        <div class="report-text">{{ data.findingDesc || '暂无' }}</div>
      </div>
      <div class="report-section">
        <h4>五、结论与建议</h4>
        <div class="report-text">
          <p v-if="data.riskLevel === '高风险' || data.riskLevel === '极高风险'">
            该负责人当前风险等级为<strong>{{ data.riskLevel }}</strong>，建议加强监管力度，限期整改。
          </p>
          <p v-else-if="data.riskLevel === '中风险'">
            该负责人当前风险等级为<strong>中风险</strong>，建议持续关注，定期复查。
          </p>
          <p v-else>
            该负责人当前风险等级为<strong>低风险</strong>，各项指标正常，继续保持。
          </p>
        </div>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" icon="el-icon-printer" @click="handlePrint">打印报告</el-button>
      <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { exportLeaderSupervision } from '@/api/leader/index'

export default {
  name: 'SupervisionReportDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    reportDate() {
      return new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' })
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    handlePrint() {
      const content = this.$refs.reportContent.innerHTML
      const win = window.open('', '_blank')
      win.document.write(`<html><head><title>监管报告</title><style>
        body{font-family:SimSun,serif;padding:40px;color:#333}
        h2{text-align:center;margin-bottom:5px}
        .report-date{text-align:center;color:#999;font-size:12px}
        h4{margin:16px 0 8px;border-left:3px solid #1677FF;padding-left:8px}
        .report-text{padding:8px 12px;background:#f9f9f9;border-radius:4px;line-height:1.8}
        table{width:100%;border-collapse:collapse;margin:8px 0}
        td,th{border:1px solid #ddd;padding:6px 10px;font-size:13px}
      </style></head><body>${content}</body></html>`)
      win.document.close()
      win.print()
    },
    async handleExport() {
      try {
        const res = await exportLeaderSupervision()
        // 拦截器对blob响应返回完整response对象，取res.data
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        // 尝试从响应头获取文件名
        const disposition = res.headers && res.headers['content-disposition']
        let fileName = '负责人监管报告_' + new Date().getTime() + '.xlsx'
        if (disposition) {
          const match = disposition.match(/filename=(.+)/)
          if (match && match[1]) fileName = decodeURIComponent(match[1])
        }
        link.download = fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
        console.error('导出异常:', e)
      }
    },
    getRiskType(level) {
      const map = { '低风险': 'success', '中风险': 'warning', '高风险': 'danger', '极高风险': 'danger' }
      return map[level] || 'info'
    },
    getComplianceType(status) {
      const map = { '合规': 'success', '基本合规': 'warning', '不合规': 'danger', '待整改': 'info', '已整改': 'success' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-container {
  .report-header { text-align: center; h2 { margin-bottom: 4px; } .report-date { color: #999; font-size: 13px; } }
  .report-section { margin-bottom: 16px; h4 { border-left: 3px solid #1677FF; padding-left: 8px; margin: 12px 0 8px; } }
  .report-text { padding: 10px 14px; background: #f7f8fa; border-radius: 4px; line-height: 1.8; min-height: 40px; color: #555; }
}
.dialog-footer { text-align: right; }
</style>
