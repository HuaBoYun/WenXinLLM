<template>
  <el-dialog
    title="项目成本详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="project-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>项目编码：</label>
              <span>{{ projectData.projectCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>项目名称：</label>
              <span>{{ projectData.projectName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>项目类型：</label>
              <el-tag :type="getProjectTypeTag(projectData.projectType)">
                {{ getProjectTypeName(projectData.projectType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>项目状态：</label>
              <el-tag :type="getStatusTag(projectData.status)">
                {{ getStatusName(projectData.status) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>项目经理：</label>
              <span>{{ projectData.projectManager }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>所属部门：</label>
              <span>{{ projectData.department }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>开始日期：</label>
              <span>{{ projectData.startDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>结束日期：</label>
              <span>{{ projectData.endDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>项目进度：</label>
              <el-progress
                :percentage="projectData.progress"
                :color="getProgressColor(projectData.progress)"
                :stroke-width="8"
                style="width: 120px"
              />
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 成本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>成本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">预算金额</div>
              <div class="cost-value budget">
                {{ formatAmount(projectData.budgetAmount) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">实际成本</div>
              <div class="cost-value actual">
                {{ formatAmount(projectData.actualAmount) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">预算差异</div>
              <div class="cost-value" :class="getVarianceClass(projectData.variance)">
                {{ formatAmount(projectData.variance) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">预算执行率</div>
              <div class="cost-value">
                {{ getBudgetExecutionRate(projectData.budgetAmount, projectData.actualAmount) }}%
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 成本构成 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>成本构成</span>
        </div>
        <el-table
          :data="costBreakdown"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="costType"
            label="成本类型"
            width="120"
          />
          <el-table-column
            prop="budgetAmount"
            label="预算金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.budgetAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="actualAmount"
            label="实际成本"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.actualAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="variance"
            label="差异"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.variance)">
                {{ formatAmount(scope.row.variance) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="percentage"
            label="占比"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              {{ scope.row.percentage }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 项目描述 -->
      <el-card shadow="never" class="detail-card" v-if="projectData.description">
        <div slot="header" class="card-header">
          <span>项目描述</span>
        </div>
        <div class="description-content">
          {{ projectData.description }}
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card shadow="never" class="detail-card" v-if="projectData.remark">
        <div slot="header" class="card-header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ projectData.remark }}
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getProjectCostById } from '@/api/financialSharing/specialCost'

export default {
  name: 'ProjectCostDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      projectData: {},
      costBreakdown: []
    }
  },
  methods: {
    async showDetail(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getProjectCostById(row.id)
        if (code === 200) {
          this.projectData = data
          this.costBreakdown = data.costBreakdown || []
        }
      } catch (error) {
        this.$baseMessage('获取项目详情失败', 'error')
      } finally {
        this.loading = false
      }
    },
    handleEdit() {
      this.dialogVisible = false
      this.$parent.$refs.edit.showEdit(this.projectData)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getProjectTypeName(type) {
      const typeMap = {
        '1': '研发项目',
        '2': '建设项目',
        '3': '投资项目',
        '4': '其他项目'
      }
      return typeMap[type] || '未知'
    },
    getProjectTypeTag(type) {
      const tagMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return tagMap[type] || 'info'
    },
    getStatusName(status) {
      const statusMap = {
        '1': '立项中',
        '2': '进行中',
        '3': '已完成',
        '4': '已暂停',
        '5': '已取消'
      }
      return statusMap[status] || '未知'
    },
    getStatusTag(status) {
      const tagMap = {
        '1': 'info',
        '2': 'primary',
        '3': 'success',
        '4': 'warning',
        '5': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getVarianceClass(variance) {
      if (variance > 0) return 'text-danger'
      if (variance < 0) return 'text-success'
      return ''
    },
    getProgressColor(progress) {
      if (progress < 30) return '#f56c6c'
      if (progress < 70) return '#e6a23c'
      return '#67c23a'
    },
    getBudgetExecutionRate(budget, actual) {
      if (!budget || budget === 0) return 0
      return ((actual / budget) * 100).toFixed(1)
    }
  }
}
</script>

<style lang="scss" scoped>
.project-detail {
  .detail-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    font-weight: bold;
    color: #303133;
  }
  
  .detail-item {
    margin-bottom: 15px;
    
    label {
      font-weight: bold;
      color: #606266;
      margin-right: 8px;
    }
    
    span {
      color: #303133;
    }
  }
  
  .cost-item {
    text-align: center;
    padding: 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    
    .cost-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 10px;
    }
    
    .cost-value {
      font-size: 24px;
      font-weight: bold;
      
      &.budget {
        color: #409eff;
      }
      
      &.actual {
        color: #67c23a;
      }
    }
  }
  
  .description-content,
  .remark-content {
    line-height: 1.6;
    color: #606266;
    padding: 10px 0;
  }
}

.text-danger {
  color: #f56c6c;
}

.text-success {
  color: #67c23a;
}

.dialog-footer {
  text-align: right;
}
</style>
