<template>
  <div class="budget-approval-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card pending">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.pendingCount || 0 }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card approved">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.approvedCount || 0 }}</div>
              <div class="stat-label">本月已审批</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card submit">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-upload"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.submitCount || 0 }}</div>
              <div class="stat-label">本月提交</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预算期间">
          <el-input v-model="queryForm.period" placeholder="模糊查询" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="预算版本">
          <el-input v-model="queryForm.version" placeholder="模糊查询" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="模型名称">
          <el-input v-model="queryForm.modelName" placeholder="模糊查询" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <span>我的待审批</span>
        <div>
          <el-button
            type="success"
            icon="el-icon-check"
            size="small"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchApprove"
          >
            批量通过
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-close"
            size="small"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchReject"
          >
            批量驳回
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
        <el-table-column prop="period" label="预算期间" width="120" align="center" />
        <el-table-column prop="version" label="预算版本" width="120" align="center" />
        <el-table-column prop="nodeName" label="审批节点" width="120" align="center" />
        <el-table-column prop="submitUserName" label="提交人" width="100" align="center" />
        <el-table-column prop="createTime" label="提交时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" icon="el-icon-check" @click="handleApprove(scope.row)">
              通过
            </el-button>
            <el-button type="text" size="small" icon="el-icon-close" @click="handleReject(scope.row)">
              驳回
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryForm.pageNum"
        :limit.sync="queryForm.pageSize"
        @pagination="loadData"
      />
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="查看预算数据"
      :visible.sync="detailDialogVisible"
      width="1000px"
      @close="handleDetailDialogClose"
    >
      <el-form :model="detailForm" label-width="120px" :disabled="true">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预算模型">
              <el-input :value="detailForm.modelName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算表单">
              <el-input :value="detailForm.formName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算期间">
              <el-input :value="detailForm.period" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预算版本">
              <el-input :value="detailForm.version" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="科目编码">
              <el-input :value="detailForm.subjectCode" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主体编码">
              <el-input :value="detailForm.organizationCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input :value="detailForm.description" type="textarea" :rows="2" />
        </el-form-item>

        <el-divider content-position="left">数据录入</el-divider>
        <div v-if="detailFields.length > 0">
          <el-row :gutter="20">
            <el-col v-for="field in detailFields" :key="field.fieldCode" :span="8">
              <el-form-item :label="field.fieldName">
                <el-input :value="formatFieldValue(field)" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-empty v-else description="该表单暂无字段配置" />
      </el-form>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="approvalForm" :model="approvalForm" :rules="approvalRules" label-width="100px">
        <el-form-item label="审批意见" prop="comment">
          <el-input
            v-model="approvalForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmApproval">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getMyPendingList,
  approveData,
  rejectData,
  batchApprove,
  batchReject,
  getApprovalStatistics
} from '@/api/financialSharing/budgetPlanning/budgetApproval'
import { getDataById } from '@/api/financialSharing/budgetPlanning/budgetData'
import { getFormById } from '@/api/financialSharing/budgetPlanning/budgetForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetApproval',
  components: { Pagination },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      queryForm: {
        period: '',
        version: '',
        modelName: '',
        pageNum: 1,
        pageSize: 10
      },
      statistics: {
        pendingCount: 0,
        approvedCount: 0,
        submitCount: 0
      },
      // 审批操作对话框
      dialogVisible: false,
      dialogTitle: '',
      dialogType: '', // approve, reject, batchApprove, batchReject
      approvalForm: {
        comment: ''
      },
      approvalRules: {
        comment: [
          { required: true, message: '请输入审批意见', trigger: 'blur' }
        ]
      },
      currentRow: null,
      // 查看详情对话框
      detailDialogVisible: false,
      detailForm: {
        dataId: '',
        modelName: '',
        formName: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        description: ''
      },
      detailFields: [],
      detailValues: {}
    }
  },
  created() {
    this.loadStatistics()
    this.loadData()
  },
  methods: {
    // 加载统计信息
    async loadStatistics() {
      try {
        const res = await getApprovalStatistics()
        if (res.code === 1) {
          this.statistics = res.data
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    },
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const res = await getMyPendingList(this.queryForm)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        period: '',
        version: '',
        modelName: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    // 多选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 查看 - 弹出详情对话框（不再跳转路由）
    async handleView(row) {
      if (!row.dataId) {
        this.$message.warning('数据不存在')
        return
      }
      try {
        const res = await getDataById({ dataId: row.dataId })
        if (res.code !== 1 || !res.data) {
          this.$message.error(res.msg || '加载详情失败')
          return
        }
        const data = res.data
        this.detailForm = {
          dataId: data.dataId,
          modelName: data.modelName || row.modelName || '',
          formName: data.formName || '',
          period: data.period || '',
          version: data.version || '',
          subjectCode: data.subjectCode || '',
          organizationCode: data.organizationCode || '',
          description: data.description || ''
        }
        // 解析录入值
        this.detailValues = {}
        if (data.dataValues) {
          try {
            this.detailValues = JSON.parse(data.dataValues) || {}
          } catch (e) {
            console.error('解析数据值失败', e)
          }
        }
        // 拉取表单字段配置
        this.detailFields = []
        if (data.formId) {
          try {
            const fr = await getFormById({ formId: data.formId })
            if (fr.code === 1 && fr.data && fr.data.formConfig) {
              const config = JSON.parse(fr.data.formConfig)
              this.detailFields = config.fields || []
            }
          } catch (e) {
            console.error('解析表单配置失败', e)
          }
        }
        this.detailDialogVisible = true
      } catch (error) {
        this.$message.error('加载详情失败: ' + error.message)
      }
    },
    // 字段值格式化（复选框/数组等转可读字符串）
    formatFieldValue(field) {
      const v = this.detailValues[field.fieldCode]
      if (v === undefined || v === null || v === '') return ''
      if (typeof v === 'boolean') return v ? '是' : '否'
      if (Array.isArray(v)) return v.join(',')
      return String(v)
    },
    // 关闭详情对话框
    handleDetailDialogClose() {
      this.detailFields = []
      this.detailValues = {}
    },
    // 审批通过
    handleApprove(row) {
      this.dialogTitle = '审批通过'
      this.dialogType = 'approve'
      this.currentRow = row
      this.dialogVisible = true
    },
    // 审批驳回
    handleReject(row) {
      this.dialogTitle = '审批驳回'
      this.dialogType = 'reject'
      this.currentRow = row
      this.dialogVisible = true
    },
    // 批量通过
    handleBatchApprove() {
      this.dialogTitle = '批量审批通过'
      this.dialogType = 'batchApprove'
      this.dialogVisible = true
    },
    // 批量驳回
    handleBatchReject() {
      this.dialogTitle = '批量审批驳回'
      this.dialogType = 'batchReject'
      this.dialogVisible = true
    },
    // 确认审批
    async handleConfirmApproval() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (!valid) return

        try {
          let res
          if (this.dialogType === 'approve') {
            res = await approveData({
              approvalId: this.currentRow.approvalId,
              comment: this.approvalForm.comment
            })
          } else if (this.dialogType === 'reject') {
            res = await rejectData({
              approvalId: this.currentRow.approvalId,
              comment: this.approvalForm.comment
            })
          } else if (this.dialogType === 'batchApprove') {
            res = await batchApprove({
              approvalIds: this.multipleSelection.map(item => item.approvalId),
              comment: this.approvalForm.comment
            })
          } else if (this.dialogType === 'batchReject') {
            res = await batchReject({
              approvalIds: this.multipleSelection.map(item => item.approvalId),
              comment: this.approvalForm.comment
            })
          }

          if (res.code === 1) {
            this.$message.success(res.msg || '操作成功')
            this.dialogVisible = false
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败: ' + error.message)
        }
      })
    },
    // 关闭审批对话框
    handleDialogClose() {
      this.$refs.approvalForm.resetFields()
      this.currentRow = null
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-approval-container {
  padding: 20px;

  .statistics-row {
    margin-bottom: 20px;

    .stat-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .stat-content {
        display: flex;
        align-items: center;

        .stat-icon {
          font-size: 48px;
          margin-right: 20px;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 5px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }

      &.pending {
        .stat-icon {
          color: #E6A23C;
        }
        .stat-value {
          color: #E6A23C;
        }
      }

      &.approved {
        .stat-icon {
          color: #67C23A;
        }
        .stat-value {
          color: #67C23A;
        }
      }

      &.submit {
        .stat-icon {
          color: #409EFF;
        }
        .stat-value {
          color: #409EFF;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>

