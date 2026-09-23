<template>
  <el-dialog
    title="盘点任务详情"
    :visible.sync="visible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-if="taskData" class="task-detail-container">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="任务编号">{{ taskData.taskNumber }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ taskData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="盘点类型">
          <el-tag :type="getInventoryTypeTagType(taskData.inventoryType)" size="small">
            {{ getInventoryTypeText(taskData.inventoryType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(taskData.status)" size="small">
            {{ getStatusText(taskData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划开始日期">{{ taskData.planStartDate }}</el-descriptions-item>
        <el-descriptions-item label="计划结束日期">{{ taskData.planEndDate }}</el-descriptions-item>
        <el-descriptions-item label="实际开始日期">{{ taskData.actualStartDate || '未开始' }}</el-descriptions-item>
        <el-descriptions-item label="实际结束日期">{{ taskData.actualEndDate || '未完成' }}</el-descriptions-item>
        <el-descriptions-item label="盘点范围" :span="2">{{ taskData.scope }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ taskData.principal }}</el-descriptions-item>
        <el-descriptions-item label="参与人员">{{ taskData.participants }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ taskData.creator }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ taskData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ taskData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>

      <div class="task-progress" v-if="taskData.status === 'IN_PROGRESS' || taskData.status === 'COMPLETED'">
        <h4>盘点进度</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="progress-item">
              <div class="progress-label">总资产数</div>
              <div class="progress-value">{{ taskData.totalAssets || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="progress-item">
              <div class="progress-label">已盘点</div>
              <div class="progress-value success">{{ taskData.completedAssets || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="progress-item">
              <div class="progress-label">完成率</div>
              <div class="progress-value">{{ taskData.completionRate || 0 }}%</div>
            </div>
          </el-col>
        </el-row>
        <el-progress 
          :percentage="taskData.completionRate || 0" 
          :color="getProgressColor(taskData.completionRate)"
          style="margin-top: 15px"
        />
      </div>

      <div class="task-assets" v-if="assetsList.length">
        <h4>盘点资产清单</h4>
        <el-table :data="assetsList" border stripe max-height="300">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="assetCode" label="资产编码" width="120" />
          <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="categoryName" label="资产类别" width="120" />
          <el-table-column prop="location" label="存放地点" width="120" />
          <el-table-column prop="status" label="盘点状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'info'" size="mini">
                {{ scope.row.status === 'COMPLETED' ? '已盘点' : '待盘点' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button 
        v-if="taskData && taskData.status === 'PENDING'" 
        type="primary" 
        @click="handleStartTask"
      >
        开始盘点
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TaskDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object,
      default: () => null
    }
  },
  data() {
    return {
      assetsList: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.taskData) {
        this.loadAssetsList()
      }
    }
  },
  methods: {
    loadAssetsList() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.assetsList = []
    },
    handleStartTask() {
      this.$emit('start-task', this.taskData)
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    getProgressColor(percentage) {
      if (percentage < 30) return '#f56c6c'
      if (percentage < 70) return '#e6a23c'
      return '#67c23a'
    },
    getInventoryTypeTagType(type) {
      const typeMap = { 'FULL': 'primary', 'SAMPLE': 'success', 'CYCLE': 'warning' }
      return typeMap[type] || 'default'
    },
    getInventoryTypeText(type) {
      const textMap = { 'FULL': '全面盘点', 'SAMPLE': '抽样盘点', 'CYCLE': '循环盘点' }
      return textMap[type] || type
    },
    getStatusTagType(status) {
      const typeMap = { 'PENDING': 'info', 'IN_PROGRESS': 'warning', 'COMPLETED': 'success', 'CANCELLED': 'danger' }
      return typeMap[status] || 'default'
    },
    getStatusText(status) {
      const textMap = { 'PENDING': '待执行', 'IN_PROGRESS': '进行中', 'COMPLETED': '已完成', 'CANCELLED': '已取消' }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.task-detail-container {
  padding: 10px 0;
}

.task-progress {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;

  h4 {
    margin: 0 0 15px 0;
    color: #303133;
  }

  .progress-item {
    text-align: center;
    padding: 10px;
    background: white;
    border-radius: 4px;

    .progress-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .progress-value {
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      &.success {
        color: #67c23a;
      }
    }
  }
}

.task-assets {
  margin-top: 20px;

  h4 {
    margin: 0 0 10px 0;
    color: #303133;
  }
}
</style>

