<template>
  <div class="quality-control-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="质量管控编码">
          <el-input v-model="searchForm.qualityCode" placeholder="请输入质量管控编码" clearable />
        </el-form-item>
        <el-form-item label="质量管控名称">
          <el-input v-model="searchForm.qualityName" placeholder="请输入质量管控名称" clearable />
        </el-form-item>
        <el-form-item label="质量类型">
          <el-select v-model="searchForm.qualityType" placeholder="请选择质量类型" clearable>
            <el-option label="产品质量" value="PRODUCT_QUALITY" />
            <el-option label="服务质量" value="SERVICE_QUALITY" />
            <el-option label="过程质量" value="PROCESS_QUALITY" />
            <el-option label="系统质量" value="SYSTEM_QUALITY" />
            <el-option label="数据质量" value="DATA_QUALITY" />
            <el-option label="环境质量" value="ENVIRONMENT_QUALITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="质量状态">
          <el-select v-model="searchForm.qualityStatus" placeholder="请选择质量状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="非活跃" value="INACTIVE" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="检测状态">
          <el-select v-model="searchForm.detectionStatus" placeholder="请选择检测状态" clearable>
            <el-option label="待检测" value="PENDING" />
            <el-option label="检测中" value="IN_DETECTION" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已停止" value="STOPPED" />
            <el-option label="检测失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="质量等级">
          <el-select v-model="searchForm.qualityLevel" placeholder="请选择质量等级" clearable>
            <el-option label="优秀" value="EXCELLENT" />
            <el-option label="良好" value="GOOD" />
            <el-option label="一般" value="AVERAGE" />
            <el-option label="较差" value="POOR" />
            <el-option label="很差" value="VERY_POOR" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card" shadow="never">
      <div class="operation-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新增质量管控</el-button>
        <el-button type="success" icon="el-icon-video-play" :disabled="!hasSelection" @click="handleBatchStart">批量开始检测</el-button>
        <el-button type="warning" icon="el-icon-video-pause" :disabled="!hasSelection" @click="handleBatchPause">批量暂停检测</el-button>
        <el-button type="info" icon="el-icon-switch-button" :disabled="!hasSelection" @click="handleBatchStop">批量停止检测</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="!hasSelection" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="primary" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImport">导入数据</el-button>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="qualityCode" label="质量管控编码" width="150" show-overflow-tooltip />
        <el-table-column prop="qualityName" label="质量管控名称" width="200" show-overflow-tooltip />
        <el-table-column prop="qualityType" label="质量类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getQualityTypeTagType(scope.row.qualityType)">
              {{ formatQualityType(scope.row.qualityType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qualityStatus" label="质量状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getQualityStatusTagType(scope.row.qualityStatus)">
              {{ formatQualityStatus(scope.row.qualityStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="detectionStatus" label="检测状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getDetectionStatusTagType(scope.row.detectionStatus)">
              {{ formatDetectionStatus(scope.row.detectionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qualityLevel" label="质量等级" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.qualityLevel" :type="getQualityLevelTagType(scope.row.qualityLevel)">
              {{ formatQualityLevel(scope.row.qualityLevel) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="qualityScore" label="质量评分" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="passRate" label="合格率" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.passRate">{{ scope.row.passRate }}%</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" />
        <el-table-column prop="responsiblePersonName" label="负责人" width="120" show-overflow-tooltip />
        <el-table-column prop="inspectorName" label="检测人员" width="120" show-overflow-tooltip />
        <el-table-column prop="isEnabled" label="启用状态" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="handleToggleEnabled(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)" trigger="click">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="start" :disabled="scope.row.detectionStatus === 'IN_DETECTION'">
                  开始检测
                </el-dropdown-item>
                <el-dropdown-item command="pause" :disabled="scope.row.detectionStatus !== 'IN_DETECTION'">
                  暂停检测
                </el-dropdown-item>
                <el-dropdown-item command="resume" :disabled="scope.row.detectionStatus !== 'PAUSED'">
                  恢复检测
                </el-dropdown-item>
                <el-dropdown-item command="stop" :disabled="scope.row.detectionStatus === 'STOPPED'">
                  停止检测
                </el-dropdown-item>
                <el-dropdown-item command="complete" :disabled="scope.row.detectionStatus !== 'IN_DETECTION'">
                  完成检测
                </el-dropdown-item>
                <el-dropdown-item command="reDetection">重新检测</el-dropdown-item>
                <el-dropdown-item command="generateReport">生成报告</el-dropdown-item>
                <el-dropdown-item command="assignResponsible">分配负责人</el-dropdown-item>
                <el-dropdown-item command="assignInspector">分配检测人员</el-dropdown-item>
                <el-dropdown-item command="setPriority">设置优先级</el-dropdown-item>
                <el-dropdown-item command="sendNotification">发送通知</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 质量管控详情对话框 -->
    <QualityControlDetail
      :visible.sync="detailVisible"
      :quality-control-id="currentQualityControlId"
      :mode="detailMode"
      @refresh="loadData"
    />

    <!-- 完成检测对话框 -->
    <el-dialog title="完成检测" :visible.sync="completeDialogVisible" width="500px">
      <el-form :model="completeForm" label-width="100px">
        <el-form-item label="检测结果" required>
          <el-input v-model="completeForm.detectionResult" type="textarea" rows="3" placeholder="请输入检测结果" />
        </el-form-item>
        <el-form-item label="质量评分" required>
          <el-input-number v-model="completeForm.qualityScore" :min="0" :max="100" :precision="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCompleteConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分配负责人对话框 -->
    <el-dialog title="分配负责人" :visible.sync="assignResponsibleDialogVisible" width="500px">
      <el-form :model="assignResponsibleForm" label-width="100px">
        <el-form-item label="负责人" required>
          <el-select v-model="assignResponsibleForm.responsiblePersonId" placeholder="请选择负责人" filterable>
            <el-option
              v-for="person in responsiblePersonList"
              :key="person.id"
              :label="person.name"
              :value="person.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignResponsibleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignResponsibleConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分配检测人员对话框 -->
    <el-dialog title="分配检测人员" :visible.sync="assignInspectorDialogVisible" width="500px">
      <el-form :model="assignInspectorForm" label-width="100px">
        <el-form-item label="检测人员" required>
          <el-select v-model="assignInspectorForm.inspectorId" placeholder="请选择检测人员" filterable>
            <el-option
              v-for="inspector in inspectorList"
              :key="inspector.id"
              :label="inspector.name"
              :value="inspector.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignInspectorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignInspectorConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 设置优先级对话框 -->
    <el-dialog title="设置优先级" :visible.sync="setPriorityDialogVisible" width="500px">
      <el-form :model="setPriorityForm" label-width="100px">
        <el-form-item label="优先级" required>
          <el-input-number v-model="setPriorityForm.priority" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="优先级权重" required>
          <el-input-number v-model="setPriorityForm.priorityWeight" :min="0.1" :max="10" :precision="2" :step="0.1" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="setPriorityDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSetPriorityConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 发送通知对话框 -->
    <el-dialog title="发送通知" :visible.sync="sendNotificationDialogVisible" width="500px">
      <el-form :model="sendNotificationForm" label-width="100px">
        <el-form-item label="通知类型" required>
          <el-select v-model="sendNotificationForm.notificationType" placeholder="请选择通知类型">
            <el-option label="检测提醒" value="DETECTION_REMINDER" />
            <el-option label="状态变更" value="STATUS_CHANGE" />
            <el-option label="异常警告" value="EXCEPTION_WARNING" />
            <el-option label="完成通知" value="COMPLETION_NOTICE" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容" required>
          <el-input v-model="sendNotificationForm.message" type="textarea" rows="3" placeholder="请输入消息内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sendNotificationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSendNotificationConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getQualityControlPage,
  deleteQualityControl,
  batchDeleteQualityControl,
  enableQualityControl,
  disableQualityControl,
  startDetection,
  stopDetection,
  pauseDetection,
  resumeDetection,
  completeDetection,
  batchStartDetection,
  batchStopDetection,
  batchPauseDetection,
  reDetection,
  generateQualityReport,
  assignResponsiblePerson,
  assignInspector,
  setPriority,
  sendNotification,
  exportQualityControlData,
  formatQualityStatus,
  formatDetectionStatus,
  formatQualityLevel,
  formatQualityType
} from '@/api/managementAccountant/ss/qualityControl'
import QualityControlDetail from './QualityControlDetail'

export default {
  name: 'QualityControlList',
  components: {
    QualityControlDetail
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityStatus: '',
        detectionStatus: '',
        qualityLevel: ''
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      // 对话框控制
      detailVisible: false,
      detailMode: 'view',
      currentQualityControlId: null,
      currentRow: null,
      // 完成检测对话框
      completeDialogVisible: false,
      completeForm: {
        detectionResult: '',
        qualityScore: 0
      },
      // 分配负责人对话框
      assignResponsibleDialogVisible: false,
      assignResponsibleForm: {
        responsiblePersonId: null
      },
      responsiblePersonList: [
        { id: 1, name: '张三' },
        { id: 2, name: '李四' },
        { id: 3, name: '王五' }
      ],
      // 分配检测人员对话框
      assignInspectorDialogVisible: false,
      assignInspectorForm: {
        inspectorId: null
      },
      inspectorList: [
        { id: 1, name: '检测员A' },
        { id: 2, name: '检测员B' },
        { id: 3, name: '检测员C' }
      ],
      // 设置优先级对话框
      setPriorityDialogVisible: false,
      setPriorityForm: {
        priority: 5,
        priorityWeight: 1.0
      },
      // 发送通知对话框
      sendNotificationDialogVisible: false,
      sendNotificationForm: {
        notificationType: '',
        message: ''
      }
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        const response = await getQualityControlPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置搜索
    handleReset() {
      this.searchForm = {
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityStatus: '',
        detectionStatus: '',
        qualityLevel: ''
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增
    handleCreate() {
      this.currentQualityControlId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 查看
    handleView(row) {
      this.currentQualityControlId = row.qualityId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentQualityControlId = row.qualityId
      this.detailMode = 'edit'
      this.detailVisible = true
    },

    // 切换启用状态
    async handleToggleEnabled(row) {
      try {
        const action = row.isEnabled ? enableQualityControl : disableQualityControl
        const response = await action(row.qualityId)
        if (response.success) {
          this.$message.success(row.isEnabled ? '启用成功' : '禁用成功')
        } else {
          row.isEnabled = !row.isEnabled // 回滚状态
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        row.isEnabled = !row.isEnabled // 回滚状态
        console.error('切换启用状态失败:', error)
        this.$message.error('操作失败')
      }
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      this.currentRow = row
      switch (command) {
        case 'start':
          this.handleStartDetection(row)
          break
        case 'pause':
          this.handlePauseDetection(row)
          break
        case 'resume':
          this.handleResumeDetection(row)
          break
        case 'stop':
          this.handleStopDetection(row)
          break
        case 'complete':
          this.handleCompleteDetection(row)
          break
        case 'reDetection':
          this.handleReDetection(row)
          break
        case 'generateReport':
          this.handleGenerateReport(row)
          break
        case 'assignResponsible':
          this.handleAssignResponsible(row)
          break
        case 'assignInspector':
          this.handleAssignInspector(row)
          break
        case 'setPriority':
          this.handleSetPriority(row)
          break
        case 'sendNotification':
          this.handleSendNotification(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 开始检测
    async handleStartDetection(row) {
      try {
        const response = await startDetection(row.qualityId)
        if (response.success) {
          this.$message.success('开始检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始检测失败')
        }
      } catch (error) {
        console.error('开始检测失败:', error)
        this.$message.error('开始检测失败')
      }
    },

    // 暂停检测
    async handlePauseDetection(row) {
      try {
        const response = await pauseDetection(row.qualityId)
        if (response.success) {
          this.$message.success('暂停检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '暂停检测失败')
        }
      } catch (error) {
        console.error('暂停检测失败:', error)
        this.$message.error('暂停检测失败')
      }
    },

    // 恢复检测
    async handleResumeDetection(row) {
      try {
        const response = await resumeDetection(row.qualityId)
        if (response.success) {
          this.$message.success('恢复检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '恢复检测失败')
        }
      } catch (error) {
        console.error('恢复检测失败:', error)
        this.$message.error('恢复检测失败')
      }
    },

    // 停止检测
    async handleStopDetection(row) {
      try {
        const response = await stopDetection(row.qualityId)
        if (response.success) {
          this.$message.success('停止检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '停止检测失败')
        }
      } catch (error) {
        console.error('停止检测失败:', error)
        this.$message.error('停止检测失败')
      }
    },

    // 完成检测
    handleCompleteDetection(row) {
      this.completeForm = {
        detectionResult: '',
        qualityScore: 0
      }
      this.completeDialogVisible = true
    },

    // 确认完成检测
    async handleCompleteConfirm() {
      if (!this.completeForm.detectionResult) {
        this.$message.warning('请输入检测结果')
        return
      }
      if (this.completeForm.qualityScore < 0 || this.completeForm.qualityScore > 100) {
        this.$message.warning('质量评分必须在0-100之间')
        return
      }

      try {
        const response = await completeDetection(
          this.currentRow.qualityId,
          this.completeForm.detectionResult,
          this.completeForm.qualityScore
        )
        if (response.success) {
          this.$message.success('完成检测成功')
          this.completeDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '完成检测失败')
        }
      } catch (error) {
        console.error('完成检测失败:', error)
        this.$message.error('完成检测失败')
      }
    },

    // 重新检测
    async handleReDetection(row) {
      this.$confirm('确定要重新检测吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await reDetection(row.qualityId)
          if (response.success) {
            this.$message.success('重新检测成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '重新检测失败')
          }
        } catch (error) {
          console.error('重新检测失败:', error)
          this.$message.error('重新检测失败')
        }
      })
    },

    // 生成报告
    async handleGenerateReport(row) {
      try {
        const response = await generateQualityReport(row.qualityId)
        if (response.success) {
          this.$message.success('生成报告成功')
          // 可以在这里打开报告查看对话框或下载报告
          console.log('报告内容:', response.data)
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 分配负责人
    handleAssignResponsible(row) {
      this.assignResponsibleForm = {
        responsiblePersonId: row.responsiblePersonId
      }
      this.assignResponsibleDialogVisible = true
    },

    // 确认分配负责人
    async handleAssignResponsibleConfirm() {
      if (!this.assignResponsibleForm.responsiblePersonId) {
        this.$message.warning('请选择负责人')
        return
      }

      const selectedPerson = this.responsiblePersonList.find(
        p => p.id === this.assignResponsibleForm.responsiblePersonId
      )

      try {
        const response = await assignResponsiblePerson(
          this.currentRow.qualityId,
          this.assignResponsibleForm.responsiblePersonId,
          selectedPerson.name
        )
        if (response.success) {
          this.$message.success('分配负责人成功')
          this.assignResponsibleDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '分配负责人失败')
        }
      } catch (error) {
        console.error('分配负责人失败:', error)
        this.$message.error('分配负责人失败')
      }
    },

    // 分配检测人员
    handleAssignInspector(row) {
      this.assignInspectorForm = {
        inspectorId: row.inspectorId
      }
      this.assignInspectorDialogVisible = true
    },

    // 确认分配检测人员
    async handleAssignInspectorConfirm() {
      if (!this.assignInspectorForm.inspectorId) {
        this.$message.warning('请选择检测人员')
        return
      }

      const selectedInspector = this.inspectorList.find(
        i => i.id === this.assignInspectorForm.inspectorId
      )

      try {
        const response = await assignInspector(
          this.currentRow.qualityId,
          this.assignInspectorForm.inspectorId,
          selectedInspector.name
        )
        if (response.success) {
          this.$message.success('分配检测人员成功')
          this.assignInspectorDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '分配检测人员失败')
        }
      } catch (error) {
        console.error('分配检测人员失败:', error)
        this.$message.error('分配检测人员失败')
      }
    },

    // 设置优先级
    handleSetPriority(row) {
      this.setPriorityForm = {
        priority: row.priority || 5,
        priorityWeight: row.priorityWeight || 1.0
      }
      this.setPriorityDialogVisible = true
    },

    // 确认设置优先级
    async handleSetPriorityConfirm() {
      try {
        const response = await setPriority(
          this.currentRow.qualityId,
          this.setPriorityForm.priority,
          this.setPriorityForm.priorityWeight
        )
        if (response.success) {
          this.$message.success('设置优先级成功')
          this.setPriorityDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '设置优先级失败')
        }
      } catch (error) {
        console.error('设置优先级失败:', error)
        this.$message.error('设置优先级失败')
      }
    },

    // 发送通知
    handleSendNotification(row) {
      this.sendNotificationForm = {
        notificationType: '',
        message: ''
      }
      this.sendNotificationDialogVisible = true
    },

    // 确认发送通知
    async handleSendNotificationConfirm() {
      if (!this.sendNotificationForm.notificationType) {
        this.$message.warning('请选择通知类型')
        return
      }
      if (!this.sendNotificationForm.message) {
        this.$message.warning('请输入消息内容')
        return
      }

      try {
        const response = await sendNotification(
          this.currentRow.qualityId,
          this.sendNotificationForm.notificationType,
          this.sendNotificationForm.message
        )
        if (response.success) {
          this.$message.success('发送通知成功')
          this.sendNotificationDialogVisible = false
        } else {
          this.$message.error(response.message || '发送通知失败')
        }
      } catch (error) {
        console.error('发送通知失败:', error)
        this.$message.error('发送通知失败')
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确定要删除质量管控"${row.qualityName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteQualityControl(row.qualityId)
          if (response.success) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 批量开始检测
    async handleBatchStart() {
      const qualityIds = this.selectedRows.map(row => row.qualityId)
      try {
        const response = await batchStartDetection(qualityIds)
        if (response.success) {
          this.$message.success('批量开始检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量开始检测失败')
        }
      } catch (error) {
        console.error('批量开始检测失败:', error)
        this.$message.error('批量开始检测失败')
      }
    },

    // 批量暂停检测
    async handleBatchPause() {
      const qualityIds = this.selectedRows.map(row => row.qualityId)
      try {
        const response = await batchPauseDetection(qualityIds)
        if (response.success) {
          this.$message.success('批量暂停检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量暂停检测失败')
        }
      } catch (error) {
        console.error('批量暂停检测失败:', error)
        this.$message.error('批量暂停检测失败')
      }
    },

    // 批量停止检测
    async handleBatchStop() {
      const qualityIds = this.selectedRows.map(row => row.qualityId)
      try {
        const response = await batchStopDetection(qualityIds)
        if (response.success) {
          this.$message.success('批量停止检测成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量停止检测失败')
        }
      } catch (error) {
        console.error('批量停止检测失败:', error)
        this.$message.error('批量停止检测失败')
      }
    },

    // 批量删除
    handleBatchDelete() {
      const qualityNames = this.selectedRows.map(row => row.qualityName).join('、')
      this.$confirm(`确定要删除选中的质量管控"${qualityNames}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const qualityIds = this.selectedRows.map(row => row.qualityId)
          const response = await batchDeleteQualityControl(qualityIds)
          if (response.success) {
            this.$message.success('批量删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      })
    },

    // 导出数据
    async handleExport() {
      try {
        const response = await exportQualityControlData(this.searchForm)
        if (response.success) {
          this.$message.success('导出成功')
          // 这里可以处理文件下载逻辑
          console.log('导出数据:', response.data)
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 导入数据
    handleImport() {
      this.$message.info('导入功能开发中...')
    },

    // 格式化方法
    formatQualityStatus,
    formatDetectionStatus,
    formatQualityLevel,
    formatQualityType,

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    },

    // 获取标签类型
    getQualityStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    getDetectionStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'IN_DETECTION': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'STOPPED': 'danger',
        'FAILED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    getQualityLevelTagType(level) {
      const typeMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger',
        'VERY_POOR': 'danger'
      }
      return typeMap[level] || 'info'
    },

    getQualityTypeTagType(type) {
      const typeMap = {
        'PRODUCT_QUALITY': 'primary',
        'SERVICE_QUALITY': 'success',
        'PROCESS_QUALITY': 'warning',
        'SYSTEM_QUALITY': 'info',
        'DATA_QUALITY': 'danger',
        'ENVIRONMENT_QUALITY': 'primary'
      }
      return typeMap[type] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-control-list {
  padding: 20px;

  .search-card,
  .operation-card,
  .table-card {
    margin-bottom: 20px;
  }

  .operation-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .dialog-footer {
    text-align: right;
  }

  // 表格样式优化
  .el-table {
    .el-table__header {
      th {
        background-color: #f5f7fa;
        color: #606266;
        font-weight: 600;
      }
    }

    .el-table__row {
      &:hover {
        background-color: #f5f7fa;
      }
    }
  }

  // 标签样式优化
  .el-tag {
    margin: 2px;
    border-radius: 4px;
    font-size: 12px;
    padding: 0 8px;
    height: 24px;
    line-height: 22px;
  }

  // 按钮样式优化
  .el-button--mini {
    padding: 5px 8px;
    font-size: 12px;
    border-radius: 3px;
  }

  // 下拉菜单样式
  .el-dropdown-menu__item {
    font-size: 13px;
    padding: 8px 16px;

    &:hover {
      background-color: #f5f7fa;
      color: #409eff;
    }

    &.is-disabled {
      color: #c0c4cc;
      cursor: not-allowed;
    }
  }

  // 搜索表单样式
  .el-form--inline {
    .el-form-item {
      margin-bottom: 15px;
      margin-right: 20px;
    }

    .el-form-item__label {
      font-weight: 500;
      color: #606266;
    }

    .el-input,
    .el-select {
      width: 200px;
    }
  }

  // 卡片样式
  .el-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .el-card__body {
      padding: 20px;
    }
  }

  // 对话框样式
  .el-dialog {
    border-radius: 8px;

    .el-dialog__header {
      padding: 20px 20px 10px;
      border-bottom: 1px solid #ebeef5;

      .el-dialog__title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .el-dialog__body {
      padding: 20px;
    }

    .el-dialog__footer {
      padding: 10px 20px 20px;
      border-top: 1px solid #ebeef5;
    }
  }

  // 表单样式
  .el-form {
    .el-form-item__label {
      font-weight: 500;
      color: #606266;
    }

    .el-input,
    .el-select,
    .el-textarea {
      width: 100%;
    }

    .el-input-number {
      width: 100%;
    }
  }

  // 分页样式
  .el-pagination {
    .el-pagination__total {
      color: #606266;
      font-weight: 500;
    }

    .el-pager li {
      &.active {
        color: #409eff;
        font-weight: 600;
      }
    }
  }

  // 开关样式
  .el-switch {
    .el-switch__core {
      border-radius: 10px;
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    padding: 10px;

    .operation-buttons {
      flex-direction: column;

      .el-button {
        margin-bottom: 10px;
      }
    }

    .el-form--inline {
      .el-form-item {
        display: block;
        margin-right: 0;

        .el-input,
        .el-select {
          width: 100%;
        }
      }
    }

    .el-table {
      font-size: 12px;

      .el-table__header th,
      .el-table__body td {
        padding: 8px 5px;
      }
    }
  }
}

// 全局样式覆盖
::v-deep {
  .el-table__empty-text {
    color: #909399;
    font-size: 14px;
  }

  .el-loading-text {
    color: #409eff;
    font-size: 14px;
  }

  .el-message-box {
    border-radius: 8px;

    .el-message-box__header {
      .el-message-box__title {
        font-weight: 600;
      }
    }
  }
}
</style>
