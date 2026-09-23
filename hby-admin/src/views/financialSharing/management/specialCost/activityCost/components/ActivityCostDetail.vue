<template>
  <el-dialog
    title="作业成本详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="activity-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>作业编码：</label>
              <span>{{ activityData.activityCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>作业名称：</label>
              <span>{{ activityData.activityName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>作业类型：</label>
              <el-tag :type="getActivityTypeTag(activityData.activityType)">
                {{ getActivityTypeName(activityData.activityType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>成本动因：</label>
              <span>{{ getCostDriverName(activityData.costDriver) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>动因数量：</label>
              <span>{{ activityData.driverQuantity }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>所属部门：</label>
              <span>{{ activityData.department }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>成本中心：</label>
              <span>{{ activityData.costCenterName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>状态：</label>
              <el-tag :type="getStatusTag(activityData.status)">
                {{ getStatusName(activityData.status) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ activityData.createTime }}</span>
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
              <div class="cost-label">总成本</div>
              <div class="cost-value total">
                {{ formatAmount(activityData.totalCost) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">单位成本</div>
              <div class="cost-value unit">
                {{ formatUnitCost(activityData.unitCost) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">已分配成本</div>
              <div class="cost-value allocated">
                {{ formatAmount(activityData.allocatedCost) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="cost-item">
              <div class="cost-label">未分配成本</div>
              <div class="cost-value unallocated" :class="getUnallocatedClass(activityData.unallocatedCost)">
                {{ formatAmount(activityData.unallocatedCost) }}
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
          :data="costItems"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="costType"
            label="成本类型"
            width="120"
          >
            <template slot-scope="scope">
              {{ getCostTypeName(scope.row.costType) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="amount"
            label="金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.amount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="percentage"
            label="占比"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              {{ getPercentage(scope.row.amount, activityData.totalCost) }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 分配记录 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>分配记录</span>
        </div>
        <el-table
          :data="allocationRecords"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="allocationDate"
            label="分配日期"
            width="120"
          />
          <el-table-column
            prop="targetObject"
            label="分配对象"
            width="150"
          />
          <el-table-column
            prop="allocatedQuantity"
            label="分配数量"
            width="100"
            align="right"
          />
          <el-table-column
            prop="allocatedAmount"
            label="分配金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.allocatedAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="allocationRate"
            label="分配率"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              {{ scope.row.allocationRate }}%
            </template>
          </el-table-column>
          <el-table-column
            prop="operator"
            label="操作人"
            width="100"
          />
          <el-table-column
            prop="remark"
            label="备注"
            show-overflow-tooltip
          />
        </el-table>
      </el-card>

      <!-- 作业描述 -->
      <el-card shadow="never" class="detail-card" v-if="activityData.description">
        <div slot="header" class="card-header">
          <span>作业描述</span>
        </div>
        <div class="description-content">
          {{ activityData.description }}
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card shadow="never" class="detail-card" v-if="activityData.remark">
        <div slot="header" class="card-header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ activityData.remark }}
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
import { getActivityCostById } from '@/api/financialSharing/specialCost'

export default {
  name: 'ActivityCostDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      activityData: {},
      costItems: [],
      allocationRecords: []
    }
  },
  methods: {
    async showDetail(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getActivityCostById(row.id)
        if (code === 200) {
          this.activityData = data
          this.costItems = data.costItems || []
          this.allocationRecords = data.allocationRecords || []
        }
      } catch (error) {
        this.$baseMessage('获取作业详情失败', 'error')
      } finally {
        this.loading = false
      }
    },
    handleEdit() {
      this.dialogVisible = false
      this.$parent.$refs.edit.showEdit(this.activityData)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    formatUnitCost(cost) {
      if (!cost) return '0.0000'
      return cost.toFixed(4)
    },
    getActivityTypeName(type) {
      const typeMap = {
        '1': '生产作业',
        '2': '辅助作业',
        '3': '管理作业',
        '4': '销售作业'
      }
      return typeMap[type] || '未知'
    },
    getActivityTypeTag(type) {
      const tagMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return tagMap[type] || 'info'
    },
    getCostDriverName(driver) {
      const driverMap = {
        'machine_hours': '机器小时',
        'labor_hours': '人工小时',
        'product_quantity': '产品数量',
        'order_quantity': '订单数量',
        'inspection_times': '检验次数'
      }
      return driverMap[driver] || driver
    },
    getCostTypeName(type) {
      const typeMap = {
        'direct_material': '直接材料',
        'direct_labor': '直接人工',
        'manufacturing_overhead': '制造费用',
        'other_cost': '其他费用'
      }
      return typeMap[type] || type
    },
    getStatusName(status) {
      const statusMap = {
        '1': '活跃',
        '2': '暂停',
        '3': '停用'
      }
      return statusMap[status] || '未知'
    },
    getStatusTag(status) {
      const tagMap = {
        '1': 'success',
        '2': 'warning',
        '3': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getUnallocatedClass(amount) {
      if (amount > 0) return 'text-warning'
      return ''
    },
    getPercentage(amount, total) {
      if (!total || total === 0) return 0
      return ((amount / total) * 100).toFixed(1)
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'amount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatAmount(values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0))
          } else {
            sums[index] = '0.00万'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    }
  }
}
</script>

<style lang="scss" scoped>
.activity-detail {
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
      
      &.total {
        color: #409eff;
      }
      
      &.unit {
        color: #67c23a;
      }
      
      &.allocated {
        color: #e6a23c;
      }
      
      &.unallocated {
        color: #f56c6c;
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

.text-warning {
  color: #e6a23c;
}

.dialog-footer {
  text-align: right;
}
</style>
