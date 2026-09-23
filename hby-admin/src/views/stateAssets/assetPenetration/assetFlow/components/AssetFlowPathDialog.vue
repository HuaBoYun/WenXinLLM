<template>
  <el-dialog
    title="资产流向路径分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="path-dialog-container">
      <!-- 路径信息 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>流向路径信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">资产ID</div>
              <div class="info-value">{{ flowData.assetFlowId }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">流向类型</div>
              <div class="info-value">{{ flowData.flowType }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">流转金额</div>
              <div class="info-value">{{ flowData.flowAmount }}万元</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">路径层级</div>
              <div class="info-value">{{ flowData.flowPath }}层</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 路径图表 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>流向路径图</span>
        </div>
        <div ref="pathChart" style="height: 400px;"></div>
      </el-card>

      <!-- 路径详情表格 -->
      <el-card>
        <div slot="header">
          <span>路径详情</span>
        </div>
        <el-table :data="pathDetails" stripe border>
          <el-table-column prop="stepNo" label="步骤" width="80" align="center" />
          <el-table-column prop="fromEntity" label="来源实体" min-width="150" />
          <el-table-column prop="toEntity" label="目标实体" min-width="150" />
          <el-table-column prop="transferAmount" label="转移金额" width="120" align="center" />
          <el-table-column prop="transferDate" label="转移日期" width="150" align="center" />
          <el-table-column prop="transferType" label="转移类型" width="120" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'">
                {{ scope.row.status === 'COMPLETED' ? '已完成' : '进行中' }}
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
import * as echarts from 'echarts'

export default {
  name: 'AssetFlowPathDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      flowData: {},
      pathDetails: [],
      pathChart: null
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
        this.loadPathData()
      }
    }
  },
  mounted() {
    this.initChart()
  },
  beforeDestroy() {
    if (this.pathChart) {
      this.pathChart.dispose()
    }
  },
  methods: {
    initChart() {
      this.pathChart = echarts.init(this.$refs.pathChart)
    },
    loadPathData() {
      this.loading = true
      this.flowData = this.data
      // 模拟路径数据
      this.pathDetails = [
        { stepNo: 1, fromEntity: '示例云科技', toEntity: '子公司A', transferAmount: '500万', transferDate: '2024-01-15', transferType: '资产转移', status: 'COMPLETED' },
        { stepNo: 2, fromEntity: '子公司A', toEntity: '子公司B', transferAmount: '300万', transferDate: '2024-02-20', transferType: '资产注入', status: 'COMPLETED' },
        { stepNo: 3, fromEntity: '子公司B', toEntity: '关联企业', transferAmount: '200万', transferDate: '2024-03-10', transferType: '资产剥离', status: 'COMPLETED' }
      ]
      this.drawPathChart()
      this.loading = false
    },
    drawPathChart() {
      const option = {
        title: { text: '资产流向路径', left: 'center' },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'sankey',
          data: [
            { name: '示例云科技' },
            { name: '子公司A' },
            { name: '子公司B' },
            { name: '关联企业' }
          ],
          links: [
            { source: '示例云科技', target: '子公司A', value: 500 },
            { source: '子公司A', target: '子公司B', value: 300 },
            { source: '子公司B', target: '关联企业', value: 200 }
          ]
        }]
      }
      this.pathChart.setOption(option)
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.path-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.info-item {
  text-align: center;
}
.info-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}
.info-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
.dialog-footer {
  text-align: right;
}
</style>