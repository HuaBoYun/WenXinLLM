<template>
  <div class="data-quality-tab">
    <!-- 查询表单 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px" class="mb-20">
      <el-form-item label="检查类型">
        <el-select v-model="queryForm.checkType" placeholder="请选择检查类型" clearable style="width: 150px;">
          <el-option label="完整性检查" value="COMPLETENESS"></el-option>
          <el-option label="准确性检查" value="ACCURACY"></el-option>
          <el-option label="一致性检查" value="CONSISTENCY"></el-option>
          <el-option label="及时性检查" value="TIMELINESS"></el-option>
          <el-option label="有效性检查" value="VALIDITY"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="质量等级">
        <el-select v-model="queryForm.qualityLevel" placeholder="请选择质量等级" clearable style="width: 150px;">
          <el-option label="优秀" value="EXCELLENT"></el-option>
          <el-option label="良好" value="GOOD"></el-option>
          <el-option label="一般" value="FAIR"></el-option>
          <el-option label="较差" value="POOR"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检查状态">
        <el-select v-model="queryForm.checkStatus" placeholder="请选择检查状态" clearable style="width: 150px;">
          <el-option label="待检查" value="PENDING"></el-option>
          <el-option label="检查中" value="CHECKING"></el-option>
          <el-option label="已完成" value="COMPLETED"></el-option>
          <el-option label="检查失败" value="FAILED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="检查时间">
        <el-date-picker
          v-model="queryForm.checkTimeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        <el-button type="success" @click="handleQualityCheck" icon="el-icon-search">执行检查</el-button>
        <el-button type="warning" @click="handleDataClean" icon="el-icon-brush">数据清洗</el-button>
        <el-button type="info" @click="handleQualityReport" icon="el-icon-document">质量报告</el-button>
      </el-form-item>
    </el-form>

    <!-- 质量概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="quality-card">
          <div class="quality-content">
            <div class="quality-icon completeness">
              <i class="el-icon-check"></i>
            </div>
            <div class="quality-info">
              <div class="quality-score">{{ qualityStats.completenessScore || 0 }}%</div>
              <div class="quality-label">完整性评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="quality-card">
          <div class="quality-content">
            <div class="quality-icon accuracy">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="quality-info">
              <div class="quality-score">{{ qualityStats.accuracyScore || 0 }}%</div>
              <div class="quality-label">准确性评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="quality-card">
          <div class="quality-content">
            <div class="quality-icon consistency">
              <i class="el-icon-success"></i>
            </div>
            <div class="quality-info">
              <div class="quality-score">{{ qualityStats.consistencyScore || 0 }}%</div>
              <div class="quality-label">一致性评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="quality-card">
          <div class="quality-content">
            <div class="quality-icon overall">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="quality-info">
              <div class="quality-score">{{ qualityStats.overallScore || 0 }}%</div>
              <div class="quality-label">综合评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      stripe
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="enterpriseName" label="企业名称" width="160" show-overflow-tooltip></el-table-column>
      <el-table-column prop="checkType" label="检查类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getCheckTypeTag(scope.row.checkType)">
            {{ scope.row.checkType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="checkItem" label="检查项" min-width="200" show-overflow-tooltip></el-table-column>
      <el-table-column prop="checkResult" label="检查结果" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.checkResult === '通过' ? 'success' : 'danger'">
            {{ scope.row.checkResult }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="errorDesc" label="问题描述" min-width="200" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.errorDesc || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="dataSource" label="数据来源" width="120" align="center"></el-table-column>
      <el-table-column prop="checkTime" label="检查时间" width="160" align="center"></el-table-column>
      <el-table-column label="操作" width="300" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="primary" @click="handleRecheck(scope.row)" icon="el-icon-refresh">重新检查</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" icon="el-icon-delete">删除</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'details', row: scope.row}" icon="el-icon-info">问题详情</el-dropdown-item>
              <el-dropdown-item :command="{action: 'rules', row: scope.row}" icon="el-icon-setting">检查规则</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">检查历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'export', row: scope.row}" icon="el-icon-download">导出报告</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 质量检查对话框 -->
    <QualityCheckDialog
      :visible.sync="checkDialogVisible"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />

    <!-- 数据清洗对话框 -->
    <DataCleanDialog
      :visible.sync="cleanDialogVisible"
      :quality-data="currentRow"
      @refresh="getList"
    />

    <!-- 质量详情对话框 -->
    <QualityDetailDialog
      :visible.sync="detailDialogVisible"
      :quality-data="currentRow"
    />

    <!-- 问题修复对话框 -->
    <IssueFixDialog
      :visible.sync="fixDialogVisible"
      :quality-data="currentRow"
      @refresh="getList"
    />
  </div>
</template>

<script>
import {
  getDataQualityList,
  performDataQualityCheck,
  performDataClean,
  generateQualityReport,
  getDataQualityStatistics,
  deleteDataQuality
} from '@/api/enterprise/data'
import Pagination from '@/components/Pagination'
import QualityCheckDialog from './QualityCheckDialog'
import DataCleanDialog from './DataCleanDialog'
import QualityDetailDialog from './QualityDetailDialog'
import IssueFixDialog from './IssueFixDialog'

export default {
  name: 'DataQualityTab',
  components: {
    Pagination,
    QualityCheckDialog,
    DataCleanDialog,
    QualityDetailDialog,
    IssueFixDialog
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      qualityStats: {},
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        checkType: '',
        qualityLevel: '',
        checkStatus: '',
        checkTimeRange: null
      },
      
      // 对话框状态
      checkDialogVisible: false,
      cleanDialogVisible: false,
      detailDialogVisible: false,
      fixDialogVisible: false,
      currentRow: {}
    }
  },
  watch: {
    enterpriseId: {
      handler() {
        this.getList()
      },
      immediate: true
    }
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }
      // 处理检查时间范围
      if (this.queryForm.checkTimeRange && this.queryForm.checkTimeRange.length === 2) {
        const fmt = (d) => { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth() + 1).padStart(2, '0') + '-' + String(dt.getDate()).padStart(2, '0') }
        params.entryDateStart = fmt(this.queryForm.checkTimeRange[0])
        params.entryDateEnd = fmt(this.queryForm.checkTimeRange[1])
      }
      delete params.checkTimeRange

      getDataQualityList(params).then(response => {
        const data = response.data || {}
        this.tableData = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || this.tableData.length
        this.loading = false
        // 从列表数据计算统计
        this.computeQualityStats()
      }).catch(() => {
        this.tableData = []
        this.total = 0
        this.loading = false
      })
    },

    // 从列表数据计算质量统计
    computeQualityStats() {
      const list = this.tableData
      const passCount = list.filter(i => i.checkResult === '通过').length
      const total = list.length || 1
      const passRate = Math.round(passCount / total * 100 * 10) / 10
      // 按检查类型计算各维度评分
      const completeness = list.filter(i => i.checkType === '完整性检查')
      const accuracy = list.filter(i => i.checkType === '准确性检查')
      const consistency = list.filter(i => i.checkType === '一致性检查')
      const getScore = (arr) => arr.length > 0 ? Math.round(arr.filter(i => i.checkResult === '通过').length / arr.length * 100) : 0
      this.qualityStats = {
        completenessScore: completeness.length > 0 ? getScore(completeness) : passRate,
        accuracyScore: accuracy.length > 0 ? getScore(accuracy) : passRate,
        consistencyScore: consistency.length > 0 ? getScore(consistency) : passRate,
        overallScore: passRate
      }
    },

    // 加载质量统计
    loadQualityStats() {
      getDataQualityStatistics().then(response => {
        if (response.data) {
          this.qualityStats = response.data
        }
      }).catch(() => {})
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        checkType: '',
        qualityLevel: '',
        checkStatus: '',
        checkTimeRange: null
      }
      this.getList()
    },

    // 执行质量检查
    handleQualityCheck() {
      this.checkDialogVisible = true
    },

    // 数据清洗
    handleDataClean() {
      this.cleanDialogVisible = true
    },

    // 质量报告 - 生成并下载
    async handleQualityReport() {
      try {
        const loading = this.$loading({ lock: true, text: '正在生成质量报告...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
        const response = await generateQualityReport({
          enterpriseId: this.enterpriseId
        })
        loading.close()
        if (response.result == 200 && response.data) {
          const data = response.data
          this.$alert(`<div style="line-height:2">
            <p><strong>报告ID：</strong>${data.reportId || '-'}</p>
            <p><strong>生成时间：</strong>${data.generateTime || '-'}</p>
            <p><strong>检查总数：</strong>${data.totalChecks || 0}</p>
            <p><strong>通过数：</strong>${data.passCount || 0}</p>
            <p><strong>失败数：</strong>${data.failCount || 0}</p>
            <p><strong>通过率：</strong>${data.passRate || 0}%</p>
            <p><strong>状态：</strong>${data.status || '-'}</p>
          </div>`, '质量报告', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定'
          })
        } else {
          this.$message.error(response.msg || '质量报告生成失败')
        }
      } catch (error) {
        this.$message.error('质量报告生成失败：' + (error.message || '网络错误'))
      }
    },

    // 查看
    handleView(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 重新检查
    async handleRecheck(row) {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在重新检查...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        
        await performDataQualityCheck({
          qualityId: row.qualityId
        })
        
        loading.close()
        this.$message.success('重新检查完成')
        this.getList()
        this.$emit('refresh')
      } catch (error) {
        this.$message.error('重新检查失败')
      }
    },

    // 修复问题
    handleFix(row) {
      this.currentRow = { ...row }
      this.fixDialogVisible = true
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'details':
          this.handleView(row)
          break
        case 'rules':
          this.handleViewRules(row)
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'export':
          this.handleExportReport(row)
          break
      }
    },

    // 查看检查规则
    handleViewRules(row) {
      const ruleMap = {
        '完整性检查': '检查数据字段是否完整填写，必填项不能为空',
        '准确性检查': '检查数据值是否在合理范围内，数值类型是否正确',
        '一致性检查': '检查关联数据之间是否一致，如资产负债表平衡关系',
        '及时性检查': '检查数据是否在规定时间内提交',
        '有效性检查': '检查数据格式是否符合规范要求'
      }
      const rule = ruleMap[row.checkType] || '通用数据质量检查规则'
      this.$alert(`<div style="line-height:2">
        <p><strong>检查类型：</strong>${row.checkType || '-'}</p>
        <p><strong>检查项：</strong>${row.checkItem || '-'}</p>
        <p><strong>规则说明：</strong>${rule}</p>
        <p><strong>数据来源：</strong>${row.dataSource || '-'}</p>
      </div>`, '检查规则详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },

    // 查看检查历史
    async handleViewHistory(row) {
      try {
        const response = await getDataQualityList({
          enterpriseId: row.enterpriseId,
          checkType: row.checkType,
          pageNumber: 1,
          pageSize: 20
        })
        if (response.result == 200 && response.data) {
          const list = response.data.tlist || []
          if (list.length > 0) {
            const historyHtml = list.slice(0, 10).map(item =>
              `<tr><td>${item.checkTime || '-'}</td><td>${item.checkResult || '-'}</td><td>${item.errorDesc || '-'}</td></tr>`
            ).join('')
            this.$alert(`<table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;">
              <thead><tr><th>检查时间</th><th>结果</th><th>问题描述</th></tr></thead>
              <tbody>${historyHtml}</tbody>
            </table>`, '检查历史记录', {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确定',
              customClass: 'wide-dialog'
            })
          } else {
            this.$message.info('暂无检查历史记录')
          }
        }
      } catch (error) {
        this.$message.error('获取检查历史失败')
      }
    },

    // 导出报告
    async handleExportReport(row) {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在导出报告...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        const { exportDataQuality } = require('@/api/enterprise/data')
        const response = await exportDataQuality()
        loading.close()
        // 兼容blob响应
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `数据质量报告_${new Date().getTime()}.csv`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出报告失败：' + (error.message || '网络错误'))
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除该质量检查记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在删除...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await deleteDataQuality(row.id)
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('删除成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      this.getList()
    },

    // 获取检查类型标签
    getCheckTypeTag(type) {
      const tagMap = {
        'COMPLETENESS': 'primary',
        'ACCURACY': 'success',
        'CONSISTENCY': 'warning',
        'TIMELINESS': 'info',
        'VALIDITY': 'danger'
      }
      return tagMap[type] || 'info'
    },

    // 获取检查类型文本
    getCheckTypeText(type) {
      const textMap = {
        'COMPLETENESS': '完整性',
        'ACCURACY': '准确性',
        'CONSISTENCY': '一致性',
        'TIMELINESS': '及时性',
        'VALIDITY': '有效性'
      }
      return textMap[type] || type
    },

    // 获取质量等级标签
    getQualityLevelTag(level) {
      const tagMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'FAIR': 'warning',
        'POOR': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取质量等级文本
    getQualityLevelText(level) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'FAIR': '一般',
        'POOR': '较差'
      }
      return textMap[level] || level
    },

    // 获取检查状态标签
    getCheckStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'CHECKING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取检查状态文本
    getCheckStatusText(status) {
      const textMap = {
        'PENDING': '待检查',
        'CHECKING': '检查中',
        'COMPLETED': '已完成',
        'FAILED': '检查失败'
      }
      return textMap[status] || status
    },

    // 获取质量评分颜色
    getQualityColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取问题数量样式类
    getIssueCountClass(count) {
      if (count === 0) return 'issue-none'
      if (count <= 5) return 'issue-low'
      if (count <= 10) return 'issue-medium'
      return 'issue-high'
    }
  }
}
</script>

<style scoped>
.data-quality-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.quality-card {
  height: 100px;
}

.quality-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.quality-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  margin-right: 15px;
}

.quality-icon.completeness {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.quality-icon.accuracy {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.quality-icon.consistency {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.quality-icon.overall {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.quality-info {
  flex: 1;
}

.quality-score {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.quality-label {
  font-size: 12px;
  color: #909399;
}

.issue-none {
  color: #67C23A;
  font-weight: bold;
}

.issue-low {
  color: #E6A23C;
}

.issue-medium {
  color: #F56C6C;
}

.issue-high {
  color: #F56C6C;
  font-weight: bold;
}
</style>
