<template>
  <div class="intelligent-classification-list">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="分类名称">
          <el-input
            v-model="searchForm.classificationName"
            placeholder="请输入分类名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分类类型">
          <el-select
            v-model="searchForm.classificationType"
            placeholder="请选择分类类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in classificationTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类状态">
          <el-select
            v-model="searchForm.classificationStatus"
            placeholder="请选择分类状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in classificationStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类算法">
          <el-select
            v-model="searchForm.classificationAlgorithm"
            placeholder="请选择分类算法"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in classificationAlgorithmOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作区域 -->
    <div class="operation-container">
      <div class="operation-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          创建分类
        </el-button>
        <el-button type="success" icon="el-icon-upload" @click="handleBatchDeploy" :disabled="!hasSelection">
          批量部署
        </el-button>
        <el-button type="warning" icon="el-icon-cpu" @click="handleBatchTrain" :disabled="!hasSelection">
          批量训练
        </el-button>
        <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="!hasSelection">
          批量删除
        </el-button>
      </div>
      <div class="operation-right">
        <el-button icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
        <el-button icon="el-icon-upload2" @click="handleImport">
          导入数据
        </el-button>
        <el-button icon="el-icon-refresh" @click="handleRefresh">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <el-table
        ref="classificationTable"
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="classificationCode" label="分类编号" width="180" sortable="custom" />
        <el-table-column prop="classificationName" label="分类名称" width="200" sortable="custom" />
        <el-table-column prop="classificationType" label="分类类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeTagType(scope.row.classificationType)">
              {{ getTypeLabel(scope.row.classificationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classificationStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.classificationStatus)">
              {{ getStatusLabel(scope.row.classificationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="classificationAlgorithm" label="算法" width="120">
          <template slot-scope="scope">
            {{ getAlgorithmLabel(scope.row.classificationAlgorithm) }}
          </template>
        </el-table-column>
        <el-table-column prop="classificationAccuracy" label="准确率" width="100" sortable="custom">
          <template slot-scope="scope">
            <span :class="getAccuracyClass(scope.row.classificationAccuracy)">
              {{ formatAccuracy(scope.row.classificationAccuracy) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="predictionCount" label="预测次数" width="100" sortable="custom" />
        <el-table-column prop="modelVersion" label="模型版本" width="100" />
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini" type="success">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'train', row: scope.row}">
                  <i class="el-icon-cpu"></i> 训练
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'deploy', row: scope.row}">
                  <i class="el-icon-upload"></i> 部署
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'predict', row: scope.row}">
                  <i class="el-icon-magic-stick"></i> 预测
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'performance', row: scope.row}">
                  <i class="el-icon-monitor"></i> 性能
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'model', row: scope.row}">
                  <i class="el-icon-setting"></i> 模型
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  <i class="el-icon-delete"></i> 删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 导入对话框 -->
    <el-dialog title="导入分类数据" :visible.sync="importDialogVisible" width="600px">
      <el-upload
        ref="upload"
        :action="uploadAction"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :before-upload="beforeUpload"
        :file-list="fileList"
        accept=".xlsx,.xls,.csv"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls/csv文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getClassificationPage,
  deleteClassification,
  batchDeleteClassifications,
  batchDeployClassifications,
  batchTrainClassifications,
  exportClassificationData,
  importClassificationData,
  startTraining,
  deployModel,
  CLASSIFICATION_TYPE_OPTIONS,
  CLASSIFICATION_STATUS_OPTIONS,
  CLASSIFICATION_ALGORITHM_OPTIONS,
  getClassificationTypeLabel,
  getClassificationStatusLabel,
  getClassificationAlgorithmLabel,
  getClassificationStatusTagType,
  formatAccuracy
} from '@/api/managementAccountant/as/intelligentClassification'

export default {
  name: 'IntelligentClassificationList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        classificationName: '',
        classificationType: '',
        classificationStatus: '',
        classificationAlgorithm: ''
      },
      // 表格数据
      tableData: [],
      loading: false,
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      // 排序
      sortField: '',
      sortOrder: '',
      // 选择
      selectedRows: [],
      // 选项
      classificationTypeOptions: CLASSIFICATION_TYPE_OPTIONS,
      classificationStatusOptions: CLASSIFICATION_STATUS_OPTIONS,
      classificationAlgorithmOptions: CLASSIFICATION_ALGORITHM_OPTIONS,
      // 导入
      importDialogVisible: false,
      uploadAction: '',
      fileList: []
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
      try {
        this.loading = true
        const tenantId = this.$store.getters.tenantId
        
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          tenantId,
          ...this.searchForm
        }
        
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const result = await getClassificationPage(params)
        
        this.tableData = result.data.records || []
        this.pagination.total = result.data.total || 0
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

    // 重置
    handleReset() {
      this.searchForm = {
        classificationName: '',
        classificationType: '',
        classificationStatus: '',
        classificationAlgorithm: ''
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
    },

    // 分页
    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },

    // 排序
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    // 选择
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 操作
    handleCreate() {
      this.$router.push('/management-accountant/as/intelligent-classification/create')
    },

    handleView(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/detail/${row.classificationId}`)
    },

    handleEdit(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/edit/${row.classificationId}`)
    },

    // 下拉菜单操作
    handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'train':
          this.handleTrain(row)
          break
        case 'deploy':
          this.handleDeploy(row)
          break
        case 'predict':
          this.handlePredict(row)
          break
        case 'performance':
          this.handlePerformance(row)
          break
        case 'model':
          this.handleModel(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 训练
    async handleTrain(row) {
      try {
        const tenantId = this.$store.getters.tenantId
        await startTraining(tenantId, row.classificationId)
        this.$message.success('训练启动成功')
        this.loadData()
      } catch (error) {
        console.error('训练启动失败:', error)
        this.$message.error('训练启动失败')
      }
    },

    // 部署
    async handleDeploy(row) {
      try {
        const tenantId = this.$store.getters.tenantId
        await deployModel(tenantId, row.classificationId)
        this.$message.success('部署成功')
        this.loadData()
      } catch (error) {
        console.error('部署失败:', error)
        this.$message.error('部署失败')
      }
    },

    handlePredict(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/prediction/${row.classificationId}`)
    },

    handlePerformance(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/performance/${row.classificationId}`)
    },

    handleModel(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/model-management/${row.classificationId}`)
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('此操作将永久删除该分类，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        await deleteClassification(tenantId, row.classificationId)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 批量操作
    async handleBatchDeploy() {
      try {
        await this.$confirm('确定要批量部署选中的分类吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        const classificationIds = this.selectedRows.map(row => row.classificationId)
        
        await batchDeployClassifications(tenantId, classificationIds)
        this.$message.success('批量部署成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量部署失败:', error)
          this.$message.error('批量部署失败')
        }
      }
    },

    async handleBatchTrain() {
      try {
        await this.$confirm('确定要批量训练选中的分类吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        const classificationIds = this.selectedRows.map(row => row.classificationId)
        
        await batchTrainClassifications(tenantId, classificationIds)
        this.$message.success('批量训练启动成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量训练失败:', error)
          this.$message.error('批量训练失败')
        }
      }
    },

    async handleBatchDelete() {
      try {
        await this.$confirm('此操作将永久删除选中的分类，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const tenantId = this.$store.getters.tenantId
        const classificationIds = this.selectedRows.map(row => row.classificationId)
        
        await batchDeleteClassifications(tenantId, classificationIds)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const tenantId = this.$store.getters.tenantId
        const classificationIds = this.selectedRows.length > 0 
          ? this.selectedRows.map(row => row.classificationId)
          : this.tableData.map(row => row.classificationId)
        
        const result = await exportClassificationData(tenantId, classificationIds)
        
        // 处理导出结果
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 导入
    handleImport() {
      this.importDialogVisible = true
    },

    beforeUpload(file) {
      const isValidType = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'text/csv'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        this.$message.error('上传文件只能是 Excel 或 CSV 格式!')
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
      }
      return isValidType && isLt10M
    },

    handleImportSuccess(response, file, fileList) {
      this.$message.success('文件上传成功')
    },

    handleImportError(err, file, fileList) {
      this.$message.error('文件上传失败')
    },

    handleImportConfirm() {
      this.importDialogVisible = false
      this.loadData()
    },

    // 工具方法
    getTypeLabel(type) {
      return getClassificationTypeLabel(type)
    },

    getStatusLabel(status) {
      return getClassificationStatusLabel(status)
    },

    getAlgorithmLabel(algorithm) {
      return getClassificationAlgorithmLabel(algorithm)
    },

    getTypeTagType(type) {
      const typeTagMap = {
        'AUTO': 'primary',
        'MANUAL': 'success',
        'HYBRID': 'warning',
        'RULE_BASED': 'info',
        'ML_BASED': 'danger'
      }
      return typeTagMap[type] || 'default'
    },

    getStatusTagType(status) {
      return getClassificationStatusTagType(status)
    },

    formatAccuracy(accuracy) {
      return formatAccuracy(accuracy)
    },

    getAccuracyClass(accuracy) {
      if (accuracy >= 0.9) return 'accuracy-excellent'
      if (accuracy >= 0.8) return 'accuracy-good'
      if (accuracy >= 0.7) return 'accuracy-normal'
      return 'accuracy-poor'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-classification-list {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.search-container, .operation-container, .table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.operation-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  text-align: right;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.accuracy-excellent {
  color: #67C23A;
  font-weight: bold;
}

.accuracy-good {
  color: #409EFF;
  font-weight: bold;
}

.accuracy-normal {
  color: #E6A23C;
}

.accuracy-poor {
  color: #F56C6C;
}
</style>
