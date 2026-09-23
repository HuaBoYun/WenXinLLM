<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A;">
              <i class="el-icon-circle-check" />
            </div>
            <div class="stat-info">
              <div class="stat-label">总释放金额</div>
              <div class="stat-value">{{ formatAmount(statistics.totalReleaseAmount) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF;">
              <i class="el-icon-document" />
            </div>
            <div class="stat-info">
              <div class="stat-label">释放次数</div>
              <div class="stat-value">{{ statistics.releaseCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C;">
              <i class="el-icon-s-data" />
            </div>
            <div class="stat-info">
              <div class="stat-label">平均释放金额</div>
              <div class="stat-value">{{ formatAmount(statistics.avgReleaseAmount) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="业务组织ID">
        <el-input v-model="queryForm.bizOrgId" placeholder="请输入业务组织ID" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="科目编码">
        <el-input v-model="queryForm.subjectCode" placeholder="请输入科目编码" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="如202601" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="来源系统">
        <el-input v-model="queryForm.sourceSystem" placeholder="请输入来源系统" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="操作人姓名">
        <el-input v-model="queryForm.operateUser" placeholder="请输入操作人姓名" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="queryForm.startTime"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 180px"
        />
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="queryForm.endTime"
          type="datetime"
          placeholder="选择结束时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 180px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleBatchRelease">批量释放</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="记录ID" prop="recordId" width="180" show-overflow-tooltip />
      <el-table-column label="业务组织" prop="bizOrgName" width="160" show-overflow-tooltip>
        <template slot-scope="scope">
          <!-- 优先展示名称, 没名称就兜底显示 ID, 都没有就 -  -->
          {{ scope.row.bizOrgName || scope.row.bizOrgId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="科目" prop="subjectName" width="160" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.subjectName || scope.row.subjectCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="释放金额" prop="applyAmount" width="130" align="right">
        <template slot-scope="scope">
          <span style="color: #67C23A; font-weight: bold;">
            {{ formatAmount(scope.row.applyAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="来源系统" prop="sourceSystem" width="120" />
      <el-table-column label="来源单据ID" prop="sourceDocId" width="180" show-overflow-tooltip />
      <el-table-column label="控制消息" prop="controlMessage" min-width="200" show-overflow-tooltip />
      <el-table-column label="执行时间" prop="executeTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.executeTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作人" prop="createUserName" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.createUserName || scope.row.createUser || '-' }}
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

    <!-- 批量释放对话框 -->
    <el-dialog title="批量释放预算" :visible.sync="batchReleaseDialogVisible" width="800px" @close="closeBatchReleaseDialog">
      <el-form ref="batchReleaseForm" :model="batchReleaseForm" label-width="120px">
        <el-form-item label="释放数据">
          <el-input
            v-model="batchReleaseForm.releaseData"
            type="textarea"
            :rows="10"
            placeholder="请输入释放数据，格式：组织ID,科目编码,期间,释放金额,释放原因,来源系统,来源单据ID（每行一条）"
          />
          <div style="margin-top: 10px; color: #909399; font-size: 12px;">
            示例：ORG001,5001,202601,10000.00,业务取消,ERP,DOC001
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchReleaseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchRelease">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryReleaseRecords, getReleaseRecordStatistics, batchReleaseBudget } from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetRelease',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        bizOrgId: '',
        subjectCode: '',
        period: '',
        sourceSystem: '',
        operateUser: '',
        startTime: '',
        endTime: '',
        pageNumber: 1,
        pageSize: 10
      },
      statistics: {
        totalReleaseAmount: 0,
        releaseCount: 0,
        avgReleaseAmount: 0
      },
      batchReleaseDialogVisible: false,
      batchReleaseForm: {
        releaseData: ''
      }
    }
  },
  mounted() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    getList() {
      this.loading = true
      queryReleaseRecords(this.queryForm).then(response => {
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
      getReleaseRecordStatistics(this.queryForm).then(response => {
        if (response.code === 1) {
          this.statistics = response.data
        }
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
      this.getStatistics()
    },
    handleReset() {
      this.queryForm = {
        bizOrgId: '',
        subjectCode: '',
        period: '',
        sourceSystem: '',
        operateUser: '',
        startTime: '',
        endTime: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
      this.getStatistics()
    },
    handleBatchRelease() {
      this.batchReleaseDialogVisible = true
      this.batchReleaseForm.releaseData = ''
    },
    closeBatchReleaseDialog() {
      this.batchReleaseForm.releaseData = ''
    },
    submitBatchRelease() {
      if (!this.batchReleaseForm.releaseData) {
        this.$message.warning('请输入释放数据')
        return
      }

      // 解析释放数据
      const lines = this.batchReleaseForm.releaseData.split('\n').filter(line => line.trim())
      const releaseRequests = []

      for (const line of lines) {
        const parts = line.split(',').map(p => p.trim())
        if (parts.length < 7) {
          this.$message.error(`数据格式错误：${line}`)
          return
        }

        releaseRequests.push({
          orgId: parts[0],
          subjectCode: parts[1],
          period: parts[2],
          releaseAmount: parseFloat(parts[3]),
          releaseReason: parts[4],
          sourceSystem: parts[5],
          sourceDocId: parts[6]
        })
      }

      // 提交批量释放
      const request = {
        releaseRequests: releaseRequests
      }

      batchReleaseBudget(request).then(response => {
        if (response.code === 1) {
          const result = response.data
          this.$message.success(`批量释放完成：成功${result.successCount}条，失败${result.failCount}条`)

          if (result.errorMessages && result.errorMessages.length > 0) {
            this.$alert(result.errorMessages.join('\n'), '失败详情', {
              confirmButtonText: '确定',
              type: 'warning'
            })
          }

          this.batchReleaseDialogVisible = false
          this.getList()
          this.getStatistics()
        } else {
          this.$message.error(response.msg || '批量释放失败')
        }
      }).catch(error => {
        this.$message.error('批量释放失败：' + error.message)
      })
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined) {
        return '0.00'
      }
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    formatDateTime(dateTime) {
      if (!dateTime) {
        return ''
      }
      const date = new Date(dateTime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.query-form {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
</style>


