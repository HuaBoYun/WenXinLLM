<template>
  <el-dialog
    title="合规检查详情"
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 检查概况 -->
      <el-tab-pane label="检查概况" name="overview">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="企业名称">{{ checkData.enterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="检查类型">{{ getCheckTypeText(checkData.checkType) }}</el-descriptions-item>
          <el-descriptions-item label="检查期间">{{ formatDateRange(checkData.checkPeriod) }}</el-descriptions-item>
          <el-descriptions-item label="检查方式">{{ getCheckMethodText(checkData.checkMethod) }}</el-descriptions-item>
          <el-descriptions-item label="检查人员">{{ checkData.inspector }}</el-descriptions-item>
          <el-descriptions-item label="检查日期">{{ checkData.checkDate }}</el-descriptions-item>
          <el-descriptions-item label="合规状态">
            <el-tag :type="getComplianceStatusType(checkData.complianceStatus)">
              {{ getComplianceStatusText(checkData.complianceStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelType(checkData.riskLevel)">
              {{ getRiskLevelText(checkData.riskLevel) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="合规评分" :span="2">
            <el-progress :percentage="checkData.complianceScore" :color="getScoreColor(checkData.complianceScore)"></el-progress>
          </el-descriptions-item>
        </el-descriptions>
      </el-tab-pane>
      
      <!-- 检查结果 -->
      <el-tab-pane label="检查结果" name="results">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card title="检查项目" shadow="hover">
              <el-table :data="checkItems" size="small" max-height="400">
                <el-table-column prop="category" label="检查类别" width="120"></el-table-column>
                <el-table-column prop="item" label="检查项目" min-width="150"></el-table-column>
                <el-table-column prop="result" label="检查结果" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getResultType(scope.row.result)" size="small">
                      {{ scope.row.result }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="score" label="得分" width="80" align="center"></el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card title="合规评分分布" shadow="hover">
              <div id="complianceScoreChart" style="height: 350px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <!-- 问题清单 -->
      <el-tab-pane label="问题清单" name="issues">
        <el-table :data="issuesList" border size="small">
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="category" label="问题类别" width="120"></el-table-column>
          <el-table-column prop="description" label="问题描述" min-width="200"></el-table-column>
          <el-table-column prop="severity" label="严重程度" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getSeverityType(scope.row.severity)" size="small">
                {{ scope.row.severity }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="impact" label="影响范围" width="120"></el-table-column>
          <el-table-column prop="suggestion" label="整改建议" min-width="180"></el-table-column>
          <el-table-column prop="deadline" label="整改期限" width="120" align="center"></el-table-column>
          <el-table-column prop="status" label="整改状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <!-- 整改跟踪 -->
      <el-tab-pane label="整改跟踪" name="rectification">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in rectificationTimeline"
            :key="index"
            :timestamp="item.date"
            :type="item.type"
            placement="top"
          >
            <el-card>
              <h4>{{ item.title }}</h4>
              <p>{{ item.description }}</p>
              <div v-if="item.attachments" style="margin-top: 10px;">
                <el-tag v-for="file in item.attachments" :key="file" size="small" style="margin-right: 5px;">
                  <i class="el-icon-document"></i> {{ file }}
                </el-tag>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
      
      <!-- 合规建议 -->
      <el-tab-pane label="合规建议" name="suggestions">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card title="制度完善建议" shadow="hover">
              <ul class="suggestion-list">
                <li>完善财务管理制度，明确各项业务流程和操作规范</li>
                <li>建立健全内部控制制度，加强风险防控机制</li>
                <li>制定资金管理办法，规范资金使用和审批流程</li>
                <li>完善预算管理制度，提升预算编制和执行质量</li>
              </ul>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card title="流程优化建议" shadow="hover">
              <ul class="suggestion-list">
                <li>优化财务审批流程，提高审批效率和质量</li>
                <li>建立财务数据质量控制机制，确保数据准确性</li>
                <li>完善财务监督检查机制，定期开展合规检查</li>
                <li>加强财务人员培训，提升专业能力和合规意识</li>
              </ul>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <el-card title="技术改进建议" shadow="hover">
              <ul class="suggestion-list">
                <li>升级财务信息系统，提升数据处理和分析能力</li>
                <li>建立财务数据仓库，实现数据集中管理和共享</li>
                <li>引入财务机器人，提高财务处理效率</li>
                <li>建立财务风险预警系统，及时识别和防范风险</li>
              </ul>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card title="管理提升建议" shadow="hover">
              <ul class="suggestion-list">
                <li>建立财务绩效考核体系，激发财务团队积极性</li>
                <li>完善财务沟通协调机制，加强部门间协作</li>
                <li>建立财务知识管理体系，促进经验分享和传承</li>
                <li>定期开展财务管理评估，持续改进管理水平</li>
              </ul>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleExport">导出检查报告</el-button>
      <el-button @click="handleGenerateRectification">生成整改计划</el-button>
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import {
  getCheckItems,
  exportComplianceData,
  generateComplianceReport
} from '@/api/stateAssets/financialCompliance'

export default {
  name: 'ComplianceCheckDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    checkData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'overview',
      checkItems: [],
      issuesList: [],
      rectificationTimeline: []
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadData()
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    },
    activeTab(val) {
      if (val === 'results') {
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  methods: {
    loadData() {
      if (!this.checkData || (!this.checkData.id && !this.checkData.complianceId)) return
      this.loadCheckItems()
      this.parseIssuesList()
      this.buildRectificationTimeline()
    },

    async loadCheckItems() {
      const complianceId = this.checkData.complianceId || this.checkData.id
      try {
        const res = await getCheckItems({ complianceId })
        if (res && res.data && Array.isArray(res.data) && res.data.length > 0) {
          this.checkItems = res.data
        } else {
          this.buildCheckItemsFromProp()
        }
      } catch (e) {
        this.buildCheckItemsFromProp()
      }
    },

    buildCheckItemsFromProp() {
      const checkScope = this.checkData.checkScope
      if (!checkScope) {
        this.checkItems = []
        return
      }
      let scopeList = []
      try {
        scopeList = typeof checkScope === 'string' ? JSON.parse(checkScope) : checkScope
      } catch (e) {
        scopeList = []
      }
      if (!Array.isArray(scopeList)) {
        this.checkItems = []
        return
      }
      const scopeNameMap = {
        'FINANCIAL_SYSTEM': '财务制度',
        'INTERNAL_CONTROL': '内控制度',
        'BUDGET_MANAGEMENT': '预算管理',
        'FUND_MANAGEMENT': '资金管理',
        'ASSET_MANAGEMENT': '资产管理',
        'INVESTMENT_MANAGEMENT': '投资管理'
      }
      const baseScore = this.checkData.complianceScore || 80
      this.checkItems = scopeList.map((item, index) => {
        const variance = (index % 3 === 0) ? 0 : (index % 3 === 1) ? -5 : -15
        const score = Math.max(0, Math.min(100, Number(baseScore) + variance))
        let result = '合规'
        if (score < 70) result = '不合规'
        else if (score < 85) result = '基本合规'
        const scopeKey = typeof item === 'string' ? item : (item.category || '')
        return {
          category: scopeNameMap[scopeKey] || scopeKey,
          item: (scopeNameMap[scopeKey] || scopeKey) + '检查',
          result: result,
          score: score
        }
      })
    },

    parseIssuesList() {
      const issues = this.checkData.issues
      if (!issues) {
        this.issuesList = []
        return
      }
      try {
        const parsed = typeof issues === 'string' ? JSON.parse(issues) : issues
        if (Array.isArray(parsed)) {
          this.issuesList = parsed.map(item => ({
            category: item.category || '综合',
            description: item.description || (typeof item === 'string' ? item : JSON.stringify(item)),
            severity: item.severity || '中等',
            impact: item.impact || '-',
            suggestion: item.suggestion || '-',
            deadline: item.deadline || '-',
            status: item.status || '未开始'
          }))
        } else if (typeof parsed === 'object') {
          this.issuesList = [{
            category: parsed.category || '综合',
            description: parsed.description || JSON.stringify(parsed),
            severity: parsed.severity || '中等',
            impact: parsed.impact || '-',
            suggestion: parsed.suggestion || '-',
            deadline: parsed.deadline || '-',
            status: parsed.status || '未开始'
          }]
        }
      } catch (e) {
        // issues 是纯文本字符串
        this.issuesList = [{
          category: '综合',
          description: issues,
          severity: '中等',
          impact: this.checkData.riskLevel === 'HIGH' || this.checkData.riskLevel === 'CRITICAL' ? '重大影响' : '一般影响',
          suggestion: this.checkData.rectificationRequirements || '待制定整改方案',
          deadline: this.checkData.rectificationDeadline ? String(this.checkData.rectificationDeadline).substring(0, 10) : '-',
          status: this.checkData.rectificationStatus === 'COMPLETED' ? '已完成' : (this.checkData.rectificationStatus === 'IN_PROGRESS' ? '整改中' : '未开始')
        }]
      }
    },

    buildRectificationTimeline() {
      const timeline = []
      const data = this.checkData
      const checkDate = data.checkDate ? String(data.checkDate).substring(0, 10) : '-'
      // 始终添加问题识别节点
      timeline.push({
        date: checkDate,
        title: '问题识别',
        description: `通过${this.getCheckTypeText(data.checkType) || '合规检查'}发现${this.issuesList.length}个问题`,
        type: 'primary'
      })
      if (data.rectificationRequirements) {
        timeline.push({
          date: checkDate,
          title: '整改计划制定',
          description: data.rectificationRequirements,
          type: 'success'
        })
      }
      if (data.rectificationStatus === '整改中' || data.rectificationStatus === 'IN_PROGRESS') {
        timeline.push({
          date: '-',
          title: '整改措施实施',
          description: '正在实施各项整改措施',
          type: 'warning'
        })
      }
      if (data.rectificationStatus === '已完成' || data.rectificationStatus === 'COMPLETED') {
        timeline.push({
          date: '-',
          title: '整改完成',
          description: '整改已完成验收',
          type: 'success'
        })
      }
      this.rectificationTimeline = timeline
    },

    initCharts() {
      this.initComplianceScoreChart()
    },
    
    initComplianceScoreChart() {
      const chartDom = document.getElementById('complianceScoreChart')
      if (!chartDom) return
      const chart = echarts.init(chartDom)
      // 根据实际checkItems数据统计各分数段数量
      let excellent = 0
      let good = 0
      let average = 0
      let poor = 0
      this.checkItems.forEach(item => {
        const score = item.score || 0
        if (score >= 90) excellent++
        else if (score >= 80) good++
        else if (score >= 70) average++
        else poor++
      })
      const option = {
        title: {
          text: '合规评分分布',
          left: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          data: [
            { value: excellent, name: '优秀(90-100分)', itemStyle: { color: '#67C23A' } },
            { value: good, name: '良好(80-89分)', itemStyle: { color: '#409EFF' } },
            { value: average, name: '一般(70-79分)', itemStyle: { color: '#E6A23C' } },
            { value: poor, name: '较差(60-69分)', itemStyle: { color: '#F56C6C' } }
          ],
          label: {
            show: true,
            formatter: '{b}: {c}项'
          }
        }]
      }
      chart.setOption(option)
    },
    
    getCheckTypeText(type) {
      const typeMap = {
        'SYSTEM_EXECUTION': '制度执行检查',
        'PROCESS_COMPLIANCE': '流程合规检查',
        'VIOLATION_CHECK': '违规行为检查',
        'RECTIFICATION_CHECK': '整改措施检查'
      }
      return typeMap[type] || type
    },
    
    getCheckMethodText(method) {
      const methodMap = {
        'ON_SITE': '现场检查',
        'REMOTE': '远程检查',
        'DOCUMENT': '文档审查',
        'SYSTEM': '系统检查'
      }
      return methodMap[method] || method
    },
    
    getComplianceStatusText(status) {
      const statusMap = {
        'COMPLIANT': '合规',
        'BASICALLY_COMPLIANT': '基本合规',
        'NON_COMPLIANT': '不合规',
        'PENDING': '待检查'
      }
      return statusMap[status] || status
    },
    
    getComplianceStatusType(status) {
      const typeMap = {
        'COMPLIANT': 'success',
        'BASICALLY_COMPLIANT': 'warning',
        'NON_COMPLIANT': 'danger',
        'PENDING': 'info'
      }
      return typeMap[status] || 'info'
    },
    
    getRiskLevelText(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return levelMap[level] || level
    },
    
    getRiskLevelType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return typeMap[level] || 'info'
    },
    
    getResultType(result) {
      const typeMap = {
        '合规': 'success',
        '基本合规': 'warning',
        '不合规': 'danger'
      }
      return typeMap[result] || 'info'
    },
    
    getSeverityType(severity) {
      const typeMap = {
        '轻微': 'success',
        '中等': 'warning',
        '严重': 'danger'
      }
      return typeMap[severity] || 'info'
    },
    
    getStatusType(status) {
      const typeMap = {
        '未开始': 'info',
        '整改中': 'warning',
        '已完成': 'success'
      }
      return typeMap[status] || 'info'
    },
    
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },
    
    formatDateRange(dateRange) {
      if (Array.isArray(dateRange) && dateRange.length === 2) {
        return `${dateRange[0]} 至 ${dateRange[1]}`
      }
      return dateRange || ''
    },
    
    async handleExport() {
      try {
        const res = await exportComplianceData({ complianceId: this.checkData.complianceId })
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `合规检查报告_${this.checkData.enterpriseName || ''}_${this.checkData.checkDate || ''}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('检查报告导出成功')
      } catch (e) {
        this.$message.error('导出失败: ' + (e.message || '未知错误'))
      }
    },

    async handleGenerateRectification() {
      try {
        const res = await generateComplianceReport({
          complianceId: this.checkData.complianceId,
          enterpriseName: this.checkData.enterpriseName,
          issues: this.issuesList
        })
        if (res.code === 1) {
          this.$message.success('整改计划生成成功')
          this.$emit('rectification-generated', res.data)
        } else {
          this.$message.error(res.msg || '生成失败')
        }
      } catch (e) {
        this.$message.error('生成整改计划失败: ' + (e.message || '未知错误'))
      }
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.suggestion-list {
  margin: 0;
  padding-left: 20px;
  line-height: 2;
}

.suggestion-list li {
  margin: 10px 0;
  color: #666;
}
</style>
