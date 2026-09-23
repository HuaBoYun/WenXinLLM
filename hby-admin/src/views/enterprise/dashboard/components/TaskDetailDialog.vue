<template>
  <el-dialog
    title="任务详情"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="任务编号">
        {{ taskData.taskId || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="任务名称">
        {{ taskData.taskName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="任务类型">
        <el-tag :type="getTaskTypeTag(taskData.taskType)">
          {{ getTaskTypeText(taskData.taskType) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="任务状态">
        <el-tag :type="getStatusType(taskData.status)">
          {{ taskData.status || '-' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="优先级">
        <el-tag :type="getPriorityTag(taskData.priority)">
          {{ getPriorityText(taskData.priority) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ taskData.createTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="截止时间">
        {{ taskData.deadline || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="完成时间">
        {{ taskData.completeTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="创建人">
        {{ taskData.creator || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="执行人">
        {{ taskData.assignee || '-' }}
      </el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">任务进度</el-divider>
    <el-progress
      :percentage="taskData.progress || 0"
      :color="getProgressColor(taskData.progress)"
      :stroke-width="20"
      text-inside
    ></el-progress>
    
    <el-divider content-position="left">任务描述</el-divider>
    <div v-html="taskData.description || '暂无描述'"></div>
    
    <el-divider content-position="left">执行记录</el-divider>
    <el-timeline>
      <el-timeline-item
        v-for="(item, index) in executionHistory"
        :key="index"
        :timestamp="item.timestamp"
        :type="getTimelineType(item.action)"
      >
        <el-card>
          <h4>{{ item.operator }} - {{ item.action }}</h4>
          <p>{{ item.description }}</p>
          <el-tag size="small" :type="getActionType(item.action)">
            {{ item.action }}
          </el-tag>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    
    <el-divider content-position="left">相关附件</el-divider>
    <el-table :data="attachments" border>
      <el-table-column prop="fileName" label="文件名称"></el-table-column>
      <el-table-column prop="fileSize" label="文件大小" width="120"></el-table-column>
      <el-table-column prop="uploadTime" label="上传时间" width="180"></el-table-column>
      <el-table-column prop="uploader" label="上传人" width="120"></el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="downloadFile(scope.row)">
            下载
          </el-button>
          <el-button type="text" size="small" @click="previewFile(scope.row)">
            预览
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="editTask">编辑任务</el-button>
      <el-button type="success" @click="completeTask" v-if="taskData.status === '进行中'">
        完成任务
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
      default: () => ({})
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
    executionHistory() {
      return this.taskData.executionHistory || [
        {
          timestamp: '2024-01-15 10:30:00',
          operator: '张三',
          action: '开始执行',
          description: '任务开始执行'
        },
        {
          timestamp: '2024-01-14 14:20:00',
          operator: '李四',
          action: '创建任务',
          description: '任务创建完成'
        }
      ]
    },
    attachments() {
      return this.taskData.attachments || [
        {
          fileName: '任务说明.pdf',
          fileSize: '2.5MB',
          uploadTime: '2024-01-15 09:30:00',
          uploader: '张三'
        }
      ]
    }
  },
  methods: {
    getTaskTypeTag(type) {
      const typeMap = {
        'collect': 'primary',
        'audit': 'warning',
        'report': 'success',
        'analysis': 'info'
      }
      return typeMap[type] || 'info'
    },
    getTaskTypeText(type) {
      const textMap = {
        'collect': '数据收集',
        'audit': '数据审核',
        'report': '数据上报',
        'analysis': '数据分析'
      }
      return textMap[type] || type
    },
    getStatusType(status) {
      const statusMap = {
        '待开始': 'info',
        '进行中': 'primary',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getPriorityTag(priority) {
      const priorityMap = {
        'high': 'danger',
        'medium': 'warning',
        'low': 'info'
      }
      return priorityMap[priority] || 'info'
    },
    getPriorityText(priority) {
      const textMap = {
        'high': '高',
        'medium': '中',
        'low': '低'
      }
      return textMap[priority] || priority
    },
    getProgressColor(progress) {
      if (progress >= 80) return '#67C23A'
      if (progress >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    getTimelineType(action) {
      const typeMap = {
        '创建任务': 'info',
        '开始执行': 'primary',
        '暂停任务': 'warning',
        '完成任务': 'success',
        '取消任务': 'danger'
      }
      return typeMap[action] || 'info'
    },
    getActionType(action) {
      const typeMap = {
        '创建任务': 'info',
        '开始执行': 'primary',
        '暂停任务': 'warning',
        '完成任务': 'success',
        '取消任务': 'danger'
      }
      return typeMap[action] || 'info'
    },
    downloadFile(row) {
      this.$message.success('文件下载中：' + row.fileName)
    },
    previewFile(row) {
      this.$message.info('预览文件：' + row.fileName)
    },
    editTask() {
      this.$emit('edit', this.taskData)
      this.handleClose()
    },
    completeTask() {
      this.$emit('complete', this.taskData)
      this.handleClose()
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
