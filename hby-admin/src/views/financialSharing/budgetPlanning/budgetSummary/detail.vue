<template>
  <div class="summary-detail-container">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>汇总详情</span>
        <el-button type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="预算模型">
          {{ summaryInfo.modelName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="预算期间">
          {{ summaryInfo.period || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="预算版本">
          {{ summaryInfo.version || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="汇总类型">
          <el-tag v-if="summaryInfo.summaryType === 'SUBJECT'" type="primary" size="small">科目汇总</el-tag>
          <el-tag v-else-if="summaryInfo.summaryType === 'ORGANIZATION'" type="success" size="small">组织汇总</el-tag>
          <el-tag v-else-if="summaryInfo.summaryType === 'PERIOD'" type="warning" size="small">期间汇总</el-tag>
          <el-tag v-else type="info" size="small">自定义汇总</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="汇总方法">
          {{ summaryInfo.summaryMethod || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="数据条数">
          {{ summaryInfo.dataCount || 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="summaryInfo.status === 'COMPLETED'" type="success" size="small">已完成</el-tag>
          <el-tag v-else-if="summaryInfo.status === 'PROCESSING'" type="warning" size="small">处理中</el-tag>
          <el-tag v-else type="danger" size="small">失败</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ summaryInfo.createTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item v-if="summaryInfo.errorMessage" label="错误信息" :span="2">
          <span style="color: #F56C6C">{{ summaryInfo.errorMessage }}</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 汇总结果 -->
    <el-card shadow="never" style="margin-top: 20px">
      <div slot="header" class="card-header">
        <span>汇总结果</span>
        <el-button type="primary" size="small" icon="el-icon-download" @click="handleExport">
          导出
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="detailList"
        border
        stripe
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="dimensionCode" label="维度编码" min-width="150" show-overflow-tooltip />
        <el-table-column prop="dimensionName" label="维度名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="summaryValue" label="汇总值" width="150" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.summaryValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="dataCount" label="数据条数" width="120" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getSummaryById, getSummaryByCondition, exportSummary } from '@/api/financialSharing/budgetPlanning/budgetSummary'

export default {
  name: 'SummaryDetail',
  data() {
    return {
      loading: false,
      summaryId: '',
      summaryInfo: {},
      detailList: []
    }
  },
  created() {
    this.summaryId = this.$route.query.summaryId
    if (this.summaryId) {
      this.loadSummaryInfo()
      this.loadDetailList()
    } else {
      this.$message.error('缺少汇总ID参数')
      this.$router.back()
    }
  },
  methods: {
    // 加载汇总信息
    async loadSummaryInfo() {
      try {
        const res = await getSummaryById({ summaryId: this.summaryId })
        if (res.code === 1) {
          this.summaryInfo = res.data
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      }
    },
    // 加载汇总明细列表
    async loadDetailList() {
      this.loading = true
      try {
        const res = await getSummaryById({ summaryId: this.summaryId })
        if (res.code === 1) {
          const summary = res.data
          // 查询同一模型、期间、版本、类型的所有汇总记录
          const detailRes = await getSummaryByCondition({
            modelId: summary.modelId,
            period: summary.period,
            version: summary.version,
            summaryType: summary.summaryType
          })
          if (detailRes.code === 1) {
            this.detailList = detailRes.data || []
          }
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 格式化数字
    formatNumber(value) {
      if (value === null || value === undefined) {
        return '-'
      }
      return Number(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    // 合计行
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'summaryValue') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatNumber(
              values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                  return prev + value
                } else {
                  return prev
                }
              }, 0)
            )
          } else {
            sums[index] = '-'
          }
        } else if (column.property === 'dataCount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + value
              } else {
                return prev
              }
            }, 0)
          } else {
            sums[index] = '-'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    // 导出
    async handleExport() {
      try {
        const res = await exportSummary({ summaryId: this.summaryId })
        if (res.code === 1) {
          this.$message.success('导出成功')
          // TODO: 下载文件
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败: ' + error.message)
      }
    },
    // 返回
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.summary-detail-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>

