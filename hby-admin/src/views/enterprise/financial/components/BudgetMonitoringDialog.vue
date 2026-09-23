<template>
  <el-dialog
    title="预算监控"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="budgetData">
      <!-- 监控概览 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>预算执行监控概览</span>
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
            <el-statistic title="已使用" :value="budgetData.usedAmount || 0" suffix="万元" :precision="2">
              <template slot="prefix">
                <i class="el-icon-sold-out" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="剩余预算" :value="remainingAmount" suffix="万元" :precision="2">
              <template slot="prefix">
                <i class="el-icon-wallet" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="执行率" :value="executionRate" suffix="%" :precision="1">
              <template slot="prefix">
                <i class="el-icon-data-line" style="color: #909399"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-card>

      <!-- 监控设置 -->
      <el-card class="box-card" style="margin-bottom: 20px;">
        <div slot="header" class="clearfix">
          <span>监控设置</span>
        </div>
        <el-form :model="monitoringForm" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预警阈值">
                <el-input-number
                  v-model="monitoringForm.warningThreshold"
                  :min="0"
                  :max="100"
                  :precision="0"
                  style="width: 100%"
                />
                <span style="margin-left: 10px; color: #909399;">%</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="监控频率">
                <el-select v-model="monitoringForm.frequency" style="width: 100%">
                  <el-option label="每日" value="daily" />
                  <el-option label="每周" value="weekly" />
                  <el-option label="每月" value="monthly" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="监控人员">
            <el-input v-model="monitoringForm.monitoringPersonnel" placeholder="请输入监控人员" />
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 预警信息 -->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>预警信息</span>
        </div>
        <el-table :data="warningList" border style="width: 100%">
          <el-table-column prop="warningType" label="预警类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getWarningType(scope.row.warningType)">
                {{ scope.row.warningType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningContent" label="预警内容" />
          <el-table-column prop="warningLevel" label="预警级别" width="100">
            <template slot-scope="scope">
              <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
                {{ scope.row.warningLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningTime" label="预警时间" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleWarning(scope.row)">处理</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="saveMonitoringSettings">保存设置</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetMonitoringDialog',
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
      monitoringForm: {
        warningThreshold: 80,
        frequency: 'weekly',
        monitoringPersonnel: ''
      },
      warningList: [
        {
          warningType: '预算超支',
          warningContent: '人员费用预算使用率已达85%，接近预警阈值',
          warningLevel: '中等',
          warningTime: '2024-03-15 10:30:00',
          status: '待处理'
        },
        {
          warningType: '执行缓慢',
          warningContent: '差旅费用预算执行进度低于预期',
          warningLevel: '低',
          warningTime: '2024-03-14 14:20:00',
          status: '已处理'
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
    executionRate() {
      const total = this.budgetData.totalBudgetAmount || 0
      const used = this.budgetData.usedAmount || 0
      return total > 0 ? Math.round((used / total) * 100) : 0
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    saveMonitoringSettings() {
      this.$message.success('监控设置保存成功')
      this.$emit('refresh')
    },
    handleWarning(warning) {
      this.$message.info(`处理预警: ${warning.warningContent}`)
    },
    getWarningType(type) {
      const typeMap = {
        '预算超支': 'danger',
        '执行缓慢': 'warning',
        '异常支出': 'danger'
      }
      return typeMap[type] || 'info'
    },
    getWarningLevelType(level) {
      const levelMap = {
        '高': 'danger',
        '中等': 'warning',
        '低': 'info'
      }
      return levelMap[level] || 'info'
    },
    getStatusType(status) {
      const statusMap = {
        '待处理': 'warning',
        '已处理': 'success',
        '已忽略': 'info'
      }
      return statusMap[status] || 'info'
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
