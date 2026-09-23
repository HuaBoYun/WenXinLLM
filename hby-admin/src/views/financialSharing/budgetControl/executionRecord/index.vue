<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">总执行次数</div>
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">通过次数</div>
            <div class="stat-value success">{{ statistics.passCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">阻止次数</div>
            <div class="stat-value danger">{{ statistics.blockCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">通过率</div>
            <div class="stat-value primary">{{ statistics.passRate ? statistics.passRate.toFixed(2) : 0 }}%</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="来源系统">
        <el-select v-model="queryForm.sourceSystem" placeholder="请选择来源系统" clearable>
          <el-option label="凭证系统" value="VOUCHER" />
          <el-option label="付款系统" value="PAYMENT" />
          <el-option label="合同系统" value="CONTRACT" />
          <el-option label="采购系统" value="PURCHASE" />
        </el-select>
      </el-form-item>
      <el-form-item label="单据编号">
        <el-input v-model="queryForm.sourceDocCode" placeholder="请输入单据编号" clearable />
      </el-form-item>
      <el-form-item label="科目编码">
        <el-input v-model="queryForm.subjectCode" placeholder="请输入科目编码" clearable />
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="请输入期间" clearable />
      </el-form-item>
      <el-form-item label="控制结果">
        <el-select v-model="queryForm.controlResult" placeholder="请选择控制结果" clearable>
          <el-option label="通过" value="PASS" />
          <el-option label="阻止" value="BLOCK" />
          <el-option label="警告" value="WARN" />
          <el-option label="待审批" value="APPROVE" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="来源系统" prop="sourceSystem" width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sourceSystem === 'VOUCHER'" type="primary" size="small">凭证系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'PAYMENT'" type="success" size="small">付款系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'CONTRACT'" type="warning" size="small">合同系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'PURCHASE'" type="info" size="small">采购系统</el-tag>
          <span v-else style="color:#909399">{{ scope.row.sourceSystem || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据类型" prop="sourceDocType" width="100" />
      <el-table-column label="单据编号" prop="sourceDocCode" width="150" />
      <el-table-column label="组织" prop="orgId" width="120" />
      <el-table-column label="科目编码" prop="subjectCode" width="120" />
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="申请金额" prop="applyAmount" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.applyAmount ? scope.row.applyAmount.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="可用预算" prop="availableAmount" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.availableAmount ? scope.row.availableAmount.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="控制结果" prop="controlResult" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.controlResult === 'PASS'" type="success">通过</el-tag>
          <el-tag v-else-if="scope.row.controlResult === 'BLOCK'" type="danger">阻止</el-tag>
          <el-tag v-else-if="scope.row.controlResult === 'WARN'" type="warning">警告</el-tag>
          <el-tag v-else-if="scope.row.controlResult === 'APPROVE'" type="info">待审批</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="控制消息" prop="controlMessage" min-width="200" show-overflow-tooltip />
      <el-table-column label="执行时间" prop="executeTime" width="160" />
      <el-table-column label="操作" fixed="right" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleView(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 详情对话框 -->
    <el-dialog title="执行记录详情" :visible.sync="detailVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="记录ID">{{ detailData.recordId }}</el-descriptions-item>
        <el-descriptions-item label="规则ID">{{ detailData.ruleId }}</el-descriptions-item>
        <el-descriptions-item label="来源系统">{{ sourceSystemText(detailData.sourceSystem) }}</el-descriptions-item>
        <el-descriptions-item label="单据类型">{{ detailData.sourceDocType }}</el-descriptions-item>
        <el-descriptions-item label="单据ID">{{ detailData.sourceDocId }}</el-descriptions-item>
        <el-descriptions-item label="单据编号">{{ detailData.sourceDocCode }}</el-descriptions-item>
        <el-descriptions-item label="组织ID">{{ detailData.orgId }}</el-descriptions-item>
        <el-descriptions-item label="科目编码">{{ detailData.subjectCode }}</el-descriptions-item>
        <el-descriptions-item label="期间">{{ detailData.period }}</el-descriptions-item>
        <el-descriptions-item label="申请金额">{{ detailData.applyAmount }}</el-descriptions-item>
        <el-descriptions-item label="预算总额">{{ detailData.budgetAmount }}</el-descriptions-item>
        <el-descriptions-item label="已用金额">{{ detailData.usedAmount }}</el-descriptions-item>
        <el-descriptions-item label="可用金额">{{ detailData.availableAmount }}</el-descriptions-item>
        <el-descriptions-item label="控制结果">
          <el-tag v-if="detailData.controlResult === 'PASS'" type="success">通过</el-tag>
          <el-tag v-else-if="detailData.controlResult === 'BLOCK'" type="danger">阻止</el-tag>
          <el-tag v-else-if="detailData.controlResult === 'WARN'" type="warning">警告</el-tag>
          <el-tag v-else-if="detailData.controlResult === 'APPROVE'" type="info">待审批</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="控制消息" :span="2">{{ detailData.controlMessage }}</el-descriptions-item>
        <el-descriptions-item label="执行时间">{{ detailData.executeTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryExecutionRecordPage, queryExecutionRecordById, queryExecutionRecordStatistics } from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'ExecutionRecord',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      statistics: {},
      dateRange: [],
      queryForm: {
        sourceSystem: '',
        sourceDocCode: '',
        subjectCode: '',
        period: '',
        controlResult: '',
        startTime: '',
        endTime: '',
        pageNumber: 1,
        pageSize: 10
      },
      detailVisible: false,
      detailData: {}
    }
  },
  created() {
    this.getStatistics()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryForm.startTime = this.dateRange[0]
        this.queryForm.endTime = this.dateRange[1]
      }
      queryExecutionRecordPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records
          this.total = response.data.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getStatistics() {
      const param = {
        period: this.queryForm.period,
        startTime: this.queryForm.startTime,
        endTime: this.queryForm.endTime
      }
      queryExecutionRecordStatistics(param).then(response => {
        if (response.code === 1) {
          this.statistics = response.data
        }
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getStatistics()
      this.getList()
    },
    handleReset() {
      this.dateRange = []
      this.queryForm = {
        sourceSystem: '',
        sourceDocCode: '',
        subjectCode: '',
        period: '',
        controlResult: '',
        startTime: '',
        endTime: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getStatistics()
      this.getList()
    },
    /** 来源系统文本映射 */
    sourceSystemText(v) {
      const map = { VOUCHER: '凭证系统', PAYMENT: '付款系统', CONTRACT: '合同系统', PURCHASE: '采购系统' }
      return map[v] || v || '-'
    },
    /** 控制结果文本映射 */
    controlResultText(v) {
      const map = { PASS: '通过', BLOCK: '阻止', WARN: '警告', APPROVE: '待审批' }
      return map[v] || v || '-'
    },
    handleView(row) {
      queryExecutionRecordById(row.recordId).then(response => {
        if (response.code === 1) {
          this.detailData = response.data
          this.detailVisible = true
        }
      })
    },
    /**
     * 导出 Excel:
     * 走前端方案——直接复用 queryExecutionRecordPage 拉一页 9999 条 (覆盖当前过滤条件),
     * 用 export_json_to_excel 工具生成 xlsx 让浏览器直接下载.
     * 后端 exportRecords 接口是 TODO 没实现 + 没应用过滤条件, 所以直接绕开.
     */
    handleExport() {
      this.$confirm('确认导出当前查询结果吗?', '提示', { type: 'warning' }).then(() => {
        // 用现成的查询接口拉全部匹配数据, 一次最多 9999 条
        const exportParam = { ...this.queryForm, pageNumber: 1, pageSize: 9999 }
        // 时间范围跟列表保持一致
        if (this.dateRange && this.dateRange.length === 2) {
          exportParam.startTime = this.dateRange[0]
          exportParam.endTime = this.dateRange[1]
        }
        const loadingInstance = this.$loading({ text: '正在导出...', target: '.app-container' })
        queryExecutionRecordPage(exportParam).then(async response => {
          if (response.code !== 1 || !response.data || !response.data.records) {
            this.$message.error(response.msg || '查询数据失败')
            return
          }
          const records = response.data.records
          if (records.length === 0) {
            this.$message.warning('没有可导出的数据')
            return
          }
          // 动态加载 excel.js, 避免首屏多打 200KB
          const { export_json_to_excel } = await import('@/utils/excel')
          const header = ['来源系统', '单据类型', '单据编号', '组织ID', '科目编码', '期间', '申请金额', '可用预算', '控制结果', '控制消息', '执行时间']
          const data = records.map(r => [
            this.sourceSystemText(r.sourceSystem),
            r.sourceDocType || '',
            r.sourceDocCode || '',
            r.orgId || '',
            r.subjectCode || '',
            r.period || '',
            r.applyAmount != null ? Number(r.applyAmount).toFixed(2) : '0.00',
            r.availableAmount != null ? Number(r.availableAmount).toFixed(2) : '0.00',
            this.controlResultText(r.controlResult),
            r.controlMessage || '',
            r.executeTime || ''
          ])
          // 文件名带时间戳, 避免覆盖
          const now = new Date()
          const ts = `${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}_${String(now.getHours()).padStart(2, '0')}${String(now.getMinutes()).padStart(2, '0')}`
          export_json_to_excel({ header, data, filename: `执行记录_${ts}`, autoWidth: true })
          this.$message.success(`导出成功，共 ${records.length} 条`)
        }).catch(err => {
          console.error('导出失败:', err)
          this.$message.error('导出失败：' + (err.message || '未知错误'))
        }).finally(() => {
          loadingInstance.close()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.stat-card {
  text-align: center;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-value.success {
  color: #67c23a;
}
.stat-value.danger {
  color: #f56c6c;
}
.stat-value.primary {
  color: #409eff;
}
.mb20 {
  margin-bottom: 20px;
}
</style>

