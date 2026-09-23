<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF;">
              <i class="el-icon-sort" />
            </div>
            <div class="stat-info">
              <div class="stat-label">总转移金额</div>
              <div class="stat-value">{{ formatAmount(statistics.totalTransferAmount) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A;">
              <i class="el-icon-document" />
            </div>
            <div class="stat-info">
              <div class="stat-label">转移次数</div>
              <div class="stat-value">{{ statistics.transferCount }}</div>
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
              <div class="stat-label">平均转移金额</div>
              <div class="stat-value">{{ formatAmount(statistics.avgTransferAmount) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="源组织ID">
        <el-input v-model="queryForm.fromOrgId" placeholder="请输入源组织ID" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="源科目编码">
        <el-input v-model="queryForm.fromSubjectCode" placeholder="请输入源科目编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="目标组织ID">
        <el-input v-model="queryForm.toOrgId" placeholder="请输入目标组织ID" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="目标科目编码">
        <el-input v-model="queryForm.toSubjectCode" placeholder="请输入目标科目编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="如202601" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item label="来源系统">
        <el-input v-model="queryForm.sourceSystem" placeholder="请输入来源系统" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleSingleTransfer">单个转移</el-button>
        <el-button type="warning" icon="el-icon-s-operation" @click="handleBatchTransfer">批量转移</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="记录ID" prop="recordId" width="180" />
      <el-table-column label="源组织ID" prop="orgId" width="120" />
      <el-table-column label="源科目编码" prop="subjectCode" width="120" />
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="转移金额" prop="applyAmount" width="130" align="right">
        <template slot-scope="scope">
          <span style="color: #409EFF; font-weight: bold;">
            {{ formatAmount(scope.row.applyAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="来源系统" prop="sourceSystem" width="120" />
      <el-table-column label="来源单据ID" prop="sourceDocId" width="180" />
      <el-table-column label="转移详情" prop="controlMessage" min-width="300" show-overflow-tooltip />
      <el-table-column label="执行时间" prop="executeTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.executeTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作人" prop="createUserName" width="120" align="center">
        <template slot-scope="scope">
          <!-- 优先展示姓名, 没有则兜底显示 staffId, 都没有就 -  -->
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

    <!-- 单个转移对话框 -->
    <el-dialog title="预算转移" :visible.sync="singleTransferDialogVisible" width="600px" @close="closeSingleTransferDialog">
      <el-form ref="singleTransferForm" :model="singleTransferForm" :rules="transferRules" label-width="120px">
        <el-divider content-position="left">源预算</el-divider>
        <el-form-item label="源组织ID" prop="fromOrgId">
          <el-input v-model="singleTransferForm.fromOrgId" placeholder="请输入源组织ID" />
        </el-form-item>
        <el-form-item label="源科目编码" prop="fromSubjectCode">
          <el-input v-model="singleTransferForm.fromSubjectCode" placeholder="请输入源科目编码" />
        </el-form-item>
        <el-form-item label="源期间" prop="fromPeriod">
          <el-input v-model="singleTransferForm.fromPeriod" placeholder="如202601" />
        </el-form-item>
        <el-divider content-position="left">目标预算</el-divider>
        <el-form-item label="目标组织ID" prop="toOrgId">
          <el-input v-model="singleTransferForm.toOrgId" placeholder="请输入目标组织ID" />
        </el-form-item>
        <el-form-item label="目标科目编码" prop="toSubjectCode">
          <el-input v-model="singleTransferForm.toSubjectCode" placeholder="请输入目标科目编码" />
        </el-form-item>
        <el-form-item label="目标期间" prop="toPeriod">
          <el-input v-model="singleTransferForm.toPeriod" placeholder="如202601" />
        </el-form-item>
        <el-divider content-position="left">转移信息</el-divider>
        <el-form-item label="转移金额" prop="transferAmount">
          <el-input v-model.number="singleTransferForm.transferAmount" placeholder="请输入转移金额" type="number" />
        </el-form-item>
        <el-form-item label="转移原因" prop="transferReason">
          <el-input v-model="singleTransferForm.transferReason" type="textarea" :rows="3" placeholder="请输入转移原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="singleTransferDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSingleTransfer">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量转移对话框 -->
    <el-dialog title="批量转移预算" :visible.sync="batchTransferDialogVisible" width="900px">
      <el-form ref="batchTransferForm" :model="batchTransferForm" label-width="120px">
        <el-form-item label="转移数据">
          <el-input
            v-model="batchTransferForm.transferData"
            type="textarea"
            :rows="12"
            placeholder="请输入转移数据，格式：源组织ID,源科目编码,源期间,目标组织ID,目标科目编码,目标期间,转移金额,转移原因,来源系统,来源单据ID（每行一条）"
          />
          <div style="margin-top: 10px; color: #909399; font-size: 12px;">
            示例：ORG001,5001,202601,ORG002,5002,202602,10000.00,预算调整,ERP,DOC001
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchTransferDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchTransfer">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryTransferRecords,
  getTransferRecordStatistics,
  transferBudgetEnhanced,
  batchTransferBudget
} from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetTransfer',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        fromOrgId: '',
        fromSubjectCode: '',
        toOrgId: '',
        toSubjectCode: '',
        period: '',
        sourceSystem: '',
        pageNumber: 1,
        pageSize: 10
      },
      statistics: {
        totalTransferAmount: 0,
        transferCount: 0,
        avgTransferAmount: 0
      },
      singleTransferDialogVisible: false,
      singleTransferForm: {
        fromOrgId: '',
        fromSubjectCode: '',
        fromPeriod: '',
        toOrgId: '',
        toSubjectCode: '',
        toPeriod: '',
        transferAmount: null,
        transferReason: '',
        sourceSystem: 'MANUAL',
        sourceDocId: ''
      },
      transferRules: {
        fromOrgId: [{ required: true, message: '请输入源组织ID', trigger: 'blur' }],
        fromSubjectCode: [{ required: true, message: '请输入源科目编码', trigger: 'blur' }],
        fromPeriod: [{ required: true, message: '请输入源期间', trigger: 'blur' }],
        toOrgId: [{ required: true, message: '请输入目标组织ID', trigger: 'blur' }],
        toSubjectCode: [{ required: true, message: '请输入目标科目编码', trigger: 'blur' }],
        toPeriod: [{ required: true, message: '请输入目标期间', trigger: 'blur' }],
        transferAmount: [
          { required: true, message: '请输入转移金额', trigger: 'blur' },
          { type: 'number', message: '转移金额必须为数字', trigger: 'blur' }
        ],
        transferReason: [{ required: true, message: '请输入转移原因', trigger: 'blur' }]
      },
      batchTransferDialogVisible: false,
      batchTransferForm: {
        transferData: ''
      }
    }
  },
  mounted() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    // 获取转移记录列表
    getList() {
      this.loading = true
      queryTransferRecords(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 获取统计数据
    getStatistics() {
      getTransferRecordStatistics(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.statistics = response.data
        }
      })
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
      this.getStatistics()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        fromOrgId: '',
        fromSubjectCode: '',
        toOrgId: '',
        toSubjectCode: '',
        period: '',
        sourceSystem: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
      this.getStatistics()
    },
    // 打开单个转移对话框
    handleSingleTransfer() {
      this.singleTransferDialogVisible = true
    },
    // 关闭单个转移对话框
    closeSingleTransferDialog() {
      this.$refs.singleTransferForm.resetFields()
      this.singleTransferForm = {
        fromOrgId: '',
        fromSubjectCode: '',
        fromPeriod: '',
        toOrgId: '',
        toSubjectCode: '',
        toPeriod: '',
        transferAmount: null,
        transferReason: '',
        sourceSystem: 'MANUAL',
        sourceDocId: ''
      }
    },
    // 提交单个转移
    submitSingleTransfer() {
      this.$refs.singleTransferForm.validate(valid => {
        if (valid) {
          this.loading = true
          transferBudgetEnhanced(this.singleTransferForm).then(response => {
            if (response.code === 1) {
              this.$message.success('转移成功')
              this.singleTransferDialogVisible = false
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.msg || '转移失败')
            }
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    // 打开批量转移对话框
    handleBatchTransfer() {
      this.batchTransferDialogVisible = true
      this.batchTransferForm.transferData = ''
    },
    // 提交批量转移
    submitBatchTransfer() {
      if (!this.batchTransferForm.transferData.trim()) {
        this.$message.warning('请输入转移数据')
        return
      }

      // 解析CSV数据
      const lines = this.batchTransferForm.transferData.trim().split('\n')
      const transferRequests = []

      for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim()
        if (!line) continue

        const parts = line.split(',')
        if (parts.length < 8) {
          this.$message.error(`第${i + 1}行数据格式错误，至少需要8个字段`)
          return
        }

        transferRequests.push({
          fromOrgId: parts[0].trim(),
          fromSubjectCode: parts[1].trim(),
          fromPeriod: parts[2].trim(),
          toOrgId: parts[3].trim(),
          toSubjectCode: parts[4].trim(),
          toPeriod: parts[5].trim(),
          transferAmount: parseFloat(parts[6].trim()),
          transferReason: parts[7].trim(),
          sourceSystem: parts[8] ? parts[8].trim() : 'MANUAL',
          sourceDocId: parts[9] ? parts[9].trim() : ''
        })
      }

      if (transferRequests.length === 0) {
        this.$message.warning('没有有效的转移数据')
        return
      }

      this.loading = true
      batchTransferBudget({ transferRequests }).then(response => {
        if (response.code === 1) {
          const data = response.data
          let message = `批量转移完成：成功${data.successCount}条，失败${data.failCount}条`
          if (data.failCount > 0) {
            message += '\n失败原因：\n' + data.errorMessages.join('\n')
            this.$alert(message, '批量转移结果', {
              confirmButtonText: '确定',
              type: 'warning'
            })
          } else {
            this.$message.success(message)
          }
          this.batchTransferDialogVisible = false
          this.getList()
          this.getStatistics()
        } else {
          this.$message.error(response.msg || '批量转移失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 格式化金额
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return dateTime.replace('T', ' ').substring(0, 19)
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
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
  margin-right: 15px;
}

.stat-icon i {
  font-size: 28px;
  color: white;
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
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}
</style>

