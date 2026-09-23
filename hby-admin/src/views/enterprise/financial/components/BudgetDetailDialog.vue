<template>
  <el-dialog
    title="预算详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="budgetData">
      <!-- 基本信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="预算ID">{{ budgetData.budgetId }}</el-descriptions-item>
          <el-descriptions-item label="预算名称">{{ budgetData.budgetName }}</el-descriptions-item>
          <el-descriptions-item label="预算类型">{{ budgetData.budgetType }}</el-descriptions-item>
          <el-descriptions-item label="预算年度">{{ budgetData.budgetYear }}</el-descriptions-item>
          <el-descriptions-item label="预算状态">
            <el-tag :type="getStatusType(budgetData.budgetStatus)">
              {{ budgetData.budgetStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审批状态">
            <el-tag :type="getApprovalStatusType(budgetData.approvalStatus)">
              {{ budgetData.approvalStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="负责人">{{ budgetData.responsiblePerson }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ budgetData.createTime }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 预算金额信息 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算金额信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="预算总额" :value="budgetData.totalBudgetAmount || 0" suffix="万元" :precision="2">
              <template slot="prefix">
                <i class="el-icon-money" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="已使用金额" :value="budgetData.usedAmount || 0" suffix="万元" :precision="2">
              <template slot="prefix">
                <i class="el-icon-sold-out" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="剩余金额" :value="remainingAmount" suffix="万元" :precision="2">
              <template slot="prefix">
                <i class="el-icon-wallet" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="执行进度" :value="executionProgress" suffix="%" :precision="1">
              <template slot="prefix">
                <i class="el-icon-data-line" style="color: #909399"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-card>

      <!-- 预算执行进度 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算执行进度</span>
        </div>
        <el-progress 
          :percentage="executionProgress" 
          :status="executionProgress >= 100 ? 'success' : executionProgress >= 80 ? 'warning' : null"
          :stroke-width="20"
          text-inside
        />
        <div style="margin-top: 10px;">
          <el-tag v-if="executionProgress < 50" type="success">执行良好</el-tag>
          <el-tag v-else-if="executionProgress < 80" type="warning">需要关注</el-tag>
          <el-tag v-else-if="executionProgress < 100" type="danger">接近预算上限</el-tag>
          <el-tag v-else type="danger">已超预算</el-tag>
        </div>
      </el-card>

      <!-- 预算分项明细 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算分项明细</span>
        </div>
        <el-table :data="budgetItems" border style="width: 100%">
          <el-table-column prop="itemName" label="预算项目" />
          <el-table-column prop="budgetAmount" label="预算金额(万元)" width="120">
            <template slot-scope="scope">
              {{ (scope.row.budgetAmount || 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="usedAmount" label="已用金额(万元)" width="120">
            <template slot-scope="scope">
              {{ (scope.row.usedAmount || 0).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="使用率" width="100">
            <template slot-scope="scope">
              <span :style="{ color: getUsageRateColor(scope.row) }">
                {{ getUsageRate(scope.row) }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getItemStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" />
        </el-table>
      </el-card>

      <!-- 预算描述和备注 -->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>预算说明</span>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="预算描述">
            {{ budgetData.budgetDescription || '暂无描述' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注信息">
            {{ budgetData.remarks || '暂无备注' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="exportDetail">导出详情</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      budgetItems: [
        {
          itemName: '人员费用',
          budgetAmount: 500.00,
          usedAmount: 320.50,
          status: '执行中',
          remarks: '包括工资、奖金、社保等'
        },
        {
          itemName: '办公费用',
          budgetAmount: 200.00,
          usedAmount: 150.30,
          status: '执行中',
          remarks: '办公用品、水电费等'
        },
        {
          itemName: '差旅费用',
          budgetAmount: 100.00,
          usedAmount: 45.20,
          status: '执行中',
          remarks: '出差交通、住宿费用'
        },
        {
          itemName: '培训费用',
          budgetAmount: 80.00,
          usedAmount: 25.00,
          status: '执行中',
          remarks: '员工培训、会议费用'
        }
      ]
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
    },
    remainingAmount() {
      const total = this.budgetData.totalBudgetAmount || 0
      const used = this.budgetData.usedAmount || 0
      return Math.max(0, total - used)
    },
    executionProgress() {
      const total = this.budgetData.totalBudgetAmount || 0
      const used = this.budgetData.usedAmount || 0
      return total > 0 ? Math.round((used / total) * 100) : 0
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    exportDetail() {
      this.$message.success('预算详情导出成功')
    },
    getStatusType(status) {
      const statusMap = {
        '待编制': 'info',
        '编制中': 'warning',
        '已编制': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getApprovalStatusType(status) {
      const statusMap = {
        '待审批': 'warning',
        '初审中': 'primary',
        '终审中': 'primary',
        '审批通过': 'success',
        '审批不通过': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getItemStatusType(status) {
      const statusMap = {
        '未开始': 'info',
        '执行中': 'warning',
        '已完成': 'success',
        '已暂停': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getUsageRate(item) {
      const budget = item.budgetAmount || 0
      const used = item.usedAmount || 0
      return budget > 0 ? Math.round((used / budget) * 100) : 0
    },
    getUsageRateColor(item) {
      const rate = this.getUsageRate(item)
      if (rate < 50) return '#67C23A'
      if (rate < 80) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
.el-statistic {
  text-align: center;
}
</style>
