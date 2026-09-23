<template>
  <div class="warning-detail">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="预警标题">
        {{ warning.warningTitle }}
      </el-descriptions-item>
      <el-descriptions-item label="风险等级">
        <el-tag :type="getRiskLevelType(warning.warningLevel)">
          {{ warning.warningLevel }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="企业名称">
        {{ warning.companyName }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ formatDateTime(warning.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="预警内容" :span="2">
        {{ warning.warningContent || '账户余额充裕度低于阈值,建议及时补充资金' }}
      </el-descriptions-item>
    </el-descriptions>

    <el-divider>预警详情</el-divider>

    <el-tabs v-model="activeTab" type="border-card">
      <!-- 指标详情 -->
      <el-tab-pane label="指标详情" name="indicators">
        <el-table
          :data="indicatorList"
          style="width: 100%"
          border
        >
          <el-table-column prop="indicatorName" label="指标名称" width="200" />
          <el-table-column prop="actualValue" label="实际值" width="120" />
          <el-table-column prop="threshold" label="阈值" width="120" />
          <el-table-column prop="deviation" label="偏差" width="120">
            <template slot-scope="scope">
              <span :style="{ color: scope.row.deviation < 0 ? '#f56c6c' : '#67c23a' }">
                {{ scope.row.deviation }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="说明" show-overflow-tooltip />
        </el-table>
      </el-tab-pane>

      <!-- 处理建议 -->
      <el-tab-pane label="处理建议" name="suggestions">
        <div class="suggestions-section">
          <el-alert
            v-for="(suggestion, index) in suggestions"
            :key="index"
            :title="suggestion.title"
            :type="suggestion.type"
            :description="suggestion.description"
            show-icon
            :closable="false"
            style="margin-bottom: 15px;"
          />
        </div>
      </el-tab-pane>

      <!-- 历史记录 -->
      <el-tab-pane label="历史记录" name="history">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in historyList"
            :key="index"
            :timestamp="formatDateTime(item.createTime)"
            placement="top"
          >
            <el-card>
              <h4>{{ item.action }}</h4>
              <p>{{ item.description }}</p>
              <p style="color: #909399; font-size: 12px;">操作人: {{ item.operator }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>
    </el-tabs>

    <el-divider></el-divider>

    <div class="action-buttons">
      <el-button type="primary" @click="handleProcess">处理预警</el-button>
      <el-button type="warning" @click="handleIgnore">忽略预警</el-button>
      <el-button @click="handleExport">导出报告</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WarningDetail',
  props: {
    warning: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      activeTab: 'indicators',
      indicatorList: [
        {
          indicatorName: '账户余额充裕度',
          actualValue: '0.65',
          threshold: '0.80',
          deviation: -18.75,
          status: '异常',
          remark: '低于阈值,需要及时补充资金'
        },
        {
          indicatorName: '可用资金充裕度',
          actualValue: '0.72',
          threshold: '0.70',
          deviation: 2.86,
          status: '正常',
          remark: '符合要求'
        },
        {
          indicatorName: '资金周转率',
          actualValue: '1.25',
          threshold: '1.00',
          deviation: 25.00,
          status: '正常',
          remark: '资金周转良好'
        }
      ],
      suggestions: [
        {
          title: '紧急建议',
          type: 'error',
          description: '账户余额充裕度严重不足,建议立即采取以下措施:\n1. 紧急调拨资金补充账户余额\n2. 暂缓非必要支出\n3. 加快应收账款回收'
        },
        {
          title: '中期建议',
          type: 'warning',
          description: '优化资金管理:\n1. 完善资金计划编制\n2. 建立资金预警机制\n3. 加强银行账户管理'
        },
        {
          title: '长期建议',
          type: 'info',
          description: '提升资金管理水平:\n1. 建立资金池管理体系\n2. 优化资金结算模式\n3. 加强资金风险管控'
        }
      ],
      historyList: [
        {
          createTime: new Date(),
          action: '预警生成',
          description: '系统自动生成预警信息',
          operator: '系统'
        },
        {
          createTime: new Date(Date.now() - 3600000),
          action: '数据采集',
          description: '从业务系统采集原始数据',
          operator: '系统'
        }
      ]
    }
  },
  methods: {
    getRiskLevelType(level) {
      const typeMap = {
        '高风险': 'danger',
        '中风险': 'warning',
        '低风险': 'success',
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return typeMap[level] || 'info'
    },
    formatDateTime(time) {
      if (!time) return '-'
      const date = new Date(time)
      return date.toLocaleString('zh-CN')
    },
    handleProcess() {
      this.$confirm('确认处理该预警?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('预警已标记为处理中')
        // 实际应该调用API更新预警状态
      }).catch(() => {})
    },
    handleIgnore() {
      this.$confirm('确认忽略该预警?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('预警已忽略')
        // 实际应该调用API更新预警状态
      }).catch(() => {})
    },
    handleExport() {
      this.$message.info('导出功能开发中...')
      // 实际应该调用API导出预警报告
    }
  }
}
</script>

<style lang="scss" scoped>
.warning-detail {
  .suggestions-section {
    padding: 20px;
  }

  .action-buttons {
    text-align: right;
    padding: 20px 0;
  }
}
</style>

