<template>
  <el-dialog
    title="资产流向监控预警"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="monitor-dialog-container">
      <!-- 监控统计 -->
      <el-row :gutter="20" class="mb-20">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">监控状态</div>
              <div class="stat-value" style="color: #409EFF;">{{ monitorData.status || '正常' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">异常次数</div>
              <div class="stat-value" style="color: #F56C6C;">{{ monitorData.abnormalCount || 0 }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">预警等级</div>
              <div class="stat-value" style="color: #E6A23C;">{{ monitorData.warningLevel || '低' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">最后更新</div>
              <div class="stat-value" style="color: #67C23A;">{{ monitorData.lastUpdate || '今天' }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 监控规则 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>监控规则配置</span>
        </div>
        <el-table :data="monitorRules" stripe border>
          <el-table-column prop="ruleName" label="规则名称" min-width="150" />
          <el-table-column prop="ruleType" label="规则类型" width="120" align="center" />
          <el-table-column prop="threshold" label="阈值" width="100" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'">
                {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editRule(scope.row)">编辑</el-button>
              <el-button size="mini" type="text" @click="deleteRule(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 预警记录 -->
      <el-card>
        <div slot="header">
          <span>预警记录</span>
        </div>
        <el-table :data="warningRecords" stripe border>
          <el-table-column prop="warningTime" label="预警时间" width="160" align="center" />
          <el-table-column prop="warningType" label="预警类型" width="120" align="center" />
          <el-table-column prop="warningLevel" label="预警等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getWarningLevelType(scope.row.warningLevel)">
                {{ scope.row.warningLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningContent" label="预警内容" min-width="200" />
          <el-table-column prop="handlingStatus" label="处理状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.handlingStatus === 'RESOLVED' ? 'success' : 'warning'">
                {{ scope.row.handlingStatus === 'RESOLVED' ? '已处理' : '待处理' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetFlowMonitorDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      monitorData: {},
      monitorRules: [],
      warningRecords: []
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadMonitorData()
      }
    }
  },
  methods: {
    loadMonitorData() {
      this.loading = true
      // 模拟监控数据
      this.monitorData = {
        status: '正常',
        abnormalCount: 2,
        warningLevel: '中',
        lastUpdate: '2024-10-20'
      }

      this.monitorRules = [
        { ruleName: '流转金额异常', ruleType: '金额监控', threshold: '1000万', status: 'ACTIVE' },
        { ruleName: '流转速度异常', ruleType: '速度监控', threshold: '50%', status: 'ACTIVE' },
        { ruleName: '流转路径异常', ruleType: '路径监控', threshold: '5层', status: 'ACTIVE' }
      ]

      this.warningRecords = [
        { warningTime: '2024-10-20 10:30', warningType: '金额异常', warningLevel: '高', warningContent: '资产流转金额超过阈值', handlingStatus: 'RESOLVED' },
        { warningTime: '2024-10-19 15:45', warningType: '速度异常', warningLevel: '中', warningContent: '资产流转速度异常', handlingStatus: 'PENDING' }
      ]

      this.loading = false
    },
    editRule(row) {
      this.$message.info('编辑规则: ' + row.ruleName)
    },
    deleteRule(row) {
      this.$confirm('确认删除该监控规则？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadMonitorData()
      }).catch(() => {})
    },
    getWarningLevelType(level) {
      const typeMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return typeMap[level] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.monitor-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.stat-item {
  text-align: center;
  padding: 10px;
}
.stat-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 18px;
  font-weight: bold;
}
.dialog-footer {
  text-align: right;
}
</style>