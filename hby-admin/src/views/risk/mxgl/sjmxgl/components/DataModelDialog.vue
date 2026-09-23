<template>
  <el-dialog
    title="数据模型管理"
    :visible.sync="dialogVisible"
    width="1600px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    custom-class="data-model-dialog risk-mxgl-sjmxgl-page"
    @close="handleClose"
  >
    <div class="data-model-container risk-mxgl-sjmxgl-page">
      <!-- 工具栏 -->
      <div class="toolbar">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索模型名称、编码或描述"
              clearable
              @clear="handleSearch"
              @keyup.enter.native="handleSearch"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
            </el-input>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-button type="primary" @click="handleAdd" icon="el-icon-plus">
              新建模型
            </el-button>
          </el-col>
        </el-row>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-select v-model="queryForm.modelType" placeholder="选择模型类型" clearable @change="handleSearch">
              <el-option label="财务模型" value="FINANCIAL" />
              <el-option label="风险模型" value="RISK" />
              <el-option label="审计模型" value="AUDIT" />
              <el-option label="业务模型" value="BUSINESS" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="queryForm.status" placeholder="模型状态" clearable @change="handleSearch">
              <el-option label="草稿" value="DRAFT" />
              <el-option label="测试中" value="TESTING" />
              <el-option label="已发布" value="PUBLISHED" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="queryForm.dataSourceId" placeholder="数据源" clearable @change="handleSearch">
              <el-option
                v-for="ds in dataSourceList"
                :key="ds.sourceId"
                :label="ds.sourceName"
                :value="ds.sourceId"
              />
            </el-select>
          </el-col>

        </el-row>
      </div>

      <!-- 统计信息 -->
      <div class="statistics-bar">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">总模型数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.publishedCount || 0 }}</div>
              <div class="stat-label">已发布</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.draftCount || 0 }}</div>
              <div class="stat-label">草稿</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.uniqueModelCount || 0 }}</div>
              <div class="stat-label">独立模型</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 模型列表 -->
      <div class="model-list">
        <el-table
          :data="modelData"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="modelCode" label="模型编码" width="150" show-overflow-tooltip />
          <el-table-column prop="modelName" label="模型名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="modelType" label="模型类型" width="100">
            <template slot-scope="scope">
              <el-tag :type="getModelTypeTagType(scope.row.modelType)">
                {{ getModelTypeLabel(scope.row.modelType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="version" label="版本" width="80" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusTagType(scope.row.status)" size="mini">
                {{ getStatusLabel(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="dataSourceName" label="数据源" width="120" show-overflow-tooltip />
          <el-table-column prop="executionCount" label="执行次数" width="100" />
          <el-table-column prop="lastExecutionTime" label="最后执行" width="160">
            <template slot-scope="scope">
              {{ formatDate(scope.row.lastExecutionTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template slot-scope="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="320" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
              <el-button size="mini" type="warning" @click="handleSqlEdit(scope.row)" icon="el-icon-document">SQL编辑</el-button>
              <el-button size="mini" type="success" @click="handleTest(scope.row)" icon="el-icon-cpu">测试</el-button>
              <el-dropdown @command="handleMoreAction" trigger="click">
                <el-button size="mini" type="text">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                    复制模型
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'versions', row: scope.row}">
                    版本管理
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'publish', row: scope.row}" v-if="scope.row.status === 'TESTING'">
                    发布模型
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'generateCode', row: scope.row}">
                    生成代码
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'generateSqlTemplate', row: scope.row}">
                    生成SQL模板
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryForm.pageNum"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="queryForm.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          />
        </div>
      </div>

      <!-- 批量操作 -->
      <div class="batch-actions" v-if="multipleSelection.length > 0">
        <el-alert
          :title="`已选择 ${multipleSelection.length} 个模型`"
          type="info"
          :closable="false"
        >
          <template slot="default">
            <el-button size="small" type="success" @click="handleBatchPublish">批量发布</el-button>
            <el-button size="small" type="danger" @click="handleBatchDelete">批量删除</el-button>
          </template>
        </el-alert>
      </div>
    </div>

    <!-- 模型编辑对话框 -->
    <DataModelEditDialog
      :visible.sync="editDialogVisible"
      :model-data="currentModel"
      :is-edit="isEdit"
      @success="handleEditSuccess"
    />

    <!-- 模型查看对话框 -->
    <DataModelViewDialog
      :visible.sync="viewDialogVisible"
      :model-data="currentModel"
    />

    <!-- 模型测试对话框 -->
    <DataModelTestDialog
      :visible.sync="testDialogVisible"
      :model-data="currentModel"
    />

    <!-- 版本管理对话框 -->
    <DataModelVersionDialog
      :visible.sync="versionDialogVisible"
      :model-data="currentModel"
      @success="handleEditSuccess"
    />

    <!-- 代码生成对话框 -->
    <DataModelCodeDialog
      :visible.sync="codeDialogVisible"
      :model-data="currentModel"
    />

    <!-- 复制模型对话框 -->
    <el-dialog
      title="复制模型"
      :visible.sync="copyDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
    >
      <div style="margin-bottom: 20px; padding: 12px; background-color: #f4f4f5; border-radius: 4px;">
        <i class="el-icon-info" style="color: #909399; margin-right: 8px;"></i>
        <span style="color: #606266; font-size: 14px;">
          复制模型将创建一个新的数据模型，包含原模型的所有配置信息，状态将重置为"草稿"
        </span>
      </div>

      <el-form :model="copyForm" label-width="120px">
        <el-form-item label="原模型信息">
          <div style="color: #606266; font-size: 14px;">
            <div style="margin-bottom: 8px;">
              <span style="font-weight: 500;">名称：</span>
              {{ currentCopyModel ? currentCopyModel.modelName : '' }}
            </div>
            <div>
              <span style="font-weight: 500;">编码：</span>
              {{ currentCopyModel ? currentCopyModel.modelCode : '' }}
            </div>
          </div>
        </el-form-item>

        <el-divider></el-divider>

        <el-form-item label="新模型编码" required>
          <el-input
            v-model="copyForm.newModelCode"
            placeholder="请输入新模型编码"
            maxlength="50"
            show-word-limit
          >
            <template slot="prepend">CODE</template>
          </el-input>
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            模型编码必须唯一，建议使用英文字母、数字和下划线
          </div>
        </el-form-item>

        <el-form-item label="新模型名称" required>
          <el-input
            v-model="copyForm.newModelName"
            placeholder="请输入新模型名称"
            maxlength="100"
            show-word-limit
          >
            <template slot="prepend">NAME</template>
          </el-input>
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            模型名称用于显示和识别，建议使用有意义的中文名称
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCopyCancel">取消</el-button>
        <el-button type="primary" @click="handleCopyConfirm" :loading="copyLoading">
          <i class="el-icon-document-copy"></i>
          确定复制
        </el-button>
      </div>
    </el-dialog>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getDataModelList,
  getDataModelStatistics,
  deleteDataModel,
  batchDeleteDataModel,
  copyDataModel,
  publishDataModel,
  generateDataModelCode,
  generateSqlTemplate
} from '@/api/mxgl'
import { getDataSourceList } from '@/api/mxgl'
import DataModelEditDialog from './DataModelEditDialog'
import DataModelViewDialog from './DataModelViewDialog'
import DataModelTestDialog from './DataModelTestDialog'
import DataModelVersionDialog from './DataModelVersionDialog'
import DataModelCodeDialog from './DataModelCodeDialog'
// 引入z-index层级管理样式
import './dialog-z-index.css'

export default {
  name: 'DataModelDialog',
  components: {
    DataModelEditDialog,
    DataModelViewDialog,
    DataModelTestDialog,
    DataModelVersionDialog,
    DataModelCodeDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      searchKeyword: '',
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        modelName: '',
        modelType: '',
        status: '',
        dataSourceId: ''
      },
      modelData: [],
      total: 0,
      statistics: {},
      dataSourceList: [],
      multipleSelection: [],
      // 子对话框
      editDialogVisible: false,
      viewDialogVisible: false,
      testDialogVisible: false,
      versionDialogVisible: false,
      codeDialogVisible: false,
      copyDialogVisible: false, // 复制对话框显示状态
      copyLoading: false, // 复制加载状态
      currentCopyModel: null, // 当前复制的模型
      copyForm: { // 复制表单
        newModelCode: '',
        newModelName: ''
      },
      currentModel: {},
      isEdit: false
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadData()
        this.loadStatistics()
        this.loadDataSources()
      }
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        console.log('📊 数据模型列表 - 查询参数:', this.queryForm)
        const response = await getDataModelList(this.queryForm)
        console.log('📊 数据模型列表 - 接口响应:', response)

        // 兼容两种响应格式：
        // 1. 标准格式: {code: 1, data: {total, records}}
        // 2. 直接格式: {total, records} (mock数据格式)
        if (response && response.code !== undefined) {
          // 标准格式
          if (response.code === 1) {
            const data = response.data || {}
            this.modelData = data.records || []
            this.total = data.total || 0
            console.log('✅ 数据模型加载成功(标准格式):', {
              total: this.total,
              records: this.modelData.length
            })
          } else {
            this.$message.error(response.msg || '查询失败')
            console.error('❌ API返回错误:', response.msg)
          }
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.modelData = response.records || []
          this.total = response.total || 0
          console.log('✅ 数据模型加载成功(直接格式):', {
            total: this.total,
            records: this.modelData.length
          })
        } else {
          this.$message.error('查询失败: 响应格式错误')
          console.error('❌ 未知的响应格式:', response)
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('❌ 查询数据模型列表异常:', error)
      } finally {
        this.loading = false
      }
    },

    // 加载统计信息
    async loadStatistics() {
      try {
        const response = await getDataModelStatistics()
        console.log('📊 数据模型统计 - 接口响应:', response)
        if (response && response.code === 1) {
          this.statistics = response.data || {}
          console.log('✅ 统计信息加载成功:', this.statistics)
        } else if (response && typeof response === 'object') {
          // 兼容直接返回数据的格式
          this.statistics = response
          console.log('✅ 统计信息加载成功(直接格式):', this.statistics)
        }
      } catch (error) {
        console.error('❌ 获取统计信息失败:', error)
      }
    },

    // 加载数据源列表
    async loadDataSources() {
      try {
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          isEnabled: 'Y'
        })
        console.log('📊 数据源列表 - 接口响应:', response)
        if (response && response.code === 1) {
          this.dataSourceList = response.data.records || []
          console.log('✅ 数据源列表加载成功:', this.dataSourceList.length, '个数据源')
        } else if (response && response.records) {
          // 兼容直接格式
          this.dataSourceList = response.records || []
          console.log('✅ 数据源列表加载成功(直接格式):', this.dataSourceList.length, '个数据源')
        }
      } catch (error) {
        console.error('❌ 获取数据源列表失败:', error)
      }
    },

    // 搜索
    handleSearch() {
      this.queryForm.modelName = this.searchKeyword
      this.queryForm.pageNum = 1
      this.loadData()
    },



    // 新增
    handleAdd() {
      this.currentModel = {}
      this.isEdit = false
      this.editDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentModel = { ...row }
      this.isEdit = true
      this.editDialogVisible = true
    },

    // SQL编辑
    handleSqlEdit(row) {
      // 通知父组件打开SQL编辑器并传递模型数据
      this.$emit('open-sql-editor', row)
    },

    // 查看
    handleView(row) {
      this.currentModel = { ...row }
      this.viewDialogVisible = true
    },

    // 测试
    handleTest(row) {
      this.currentModel = { ...row }
      this.testDialogVisible = true
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command

      switch (action) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'versions':
          await this.handleVersions(row)
          break
        case 'publish':
          await this.handlePublish(row)
          break
        case 'generateCode':
          await this.handleGenerateCode(row)
          break
        case 'generateSqlTemplate':
          await this.handleGenerateSqlTemplate(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 复制模型
    handleCopy(row) {
      this.copyDialogVisible = true
      this.currentCopyModel = row
      // 设置默认值
      const timestamp = new Date().getTime().toString().slice(-6)
      this.copyForm = {
        newModelCode: `${row.modelCode}_COPY_${timestamp}`,
        newModelName: `${row.modelName}_副本`
      }
    },

    // 确认复制
    async handleCopyConfirm() {
      try {
        // 表单验证
        if (!this.copyForm.newModelCode.trim()) {
          this.$message.error('模型编码不能为空')
          return
        }
        if (!this.copyForm.newModelName.trim()) {
          this.$message.error('模型名称不能为空')
          return
        }

        this.copyLoading = true
        const response = await copyDataModel({
          modelId: this.currentCopyModel.modelId,
          newModelCode: this.copyForm.newModelCode.trim(),
          newModelName: this.copyForm.newModelName.trim()
        })

        if (response.code === 1) {
          this.$message.success('复制成功')
          this.copyDialogVisible = false
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败')
        console.error('复制模型失败:', error)
      } finally {
        this.copyLoading = false
      }
    },

    // 取消复制
    handleCopyCancel() {
      this.copyDialogVisible = false
      this.currentCopyModel = null
      this.copyForm = {
        newModelCode: '',
        newModelName: ''
      }
    },

    // 版本管理
    handleVersions(row) {
      this.currentModel = { ...row }
      this.versionDialogVisible = true
    },

    // 发布模型
    async handlePublish(row) {
      try {
        await this.$confirm('确定要发布此模型吗？发布后将可以在生产环境中使用。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await publishDataModel(row.modelId)
        if (response.code === 1) {
          this.$message.success('发布成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '发布失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('发布失败')
          console.error('发布模型失败:', error)
        }
      }
    },

    // 生成代码
    async handleGenerateCode(row) {
      this.currentModel = { ...row }
      this.codeDialogVisible = true
    },

    // 生成SQL模板
    async handleGenerateSqlTemplate(row) {
      try {
        // 验证模型状态
        if (row.status !== 'PUBLISHED' && row.status !== 'TESTING') {
          this.$message.warning('只有已发布或测试中的模型才能生成SQL模板')
          return
        }

        // 验证SQL语句
        if (!row.sqlStatement || row.sqlStatement.trim() === '') {
          this.$message.warning('模型SQL语句不能为空')
          return
        }

        await this.$confirm(
          `确定要将数据模型"${row.modelName}"生成为SQL模板吗？\n\n生成的模板将包含：\n• SQL语句内容\n• 参数配置\n• 阈值和预警配置\n• 可在SQL模板管理中使用`,
          '生成SQL模板',
          {
            confirmButtonText: '确定生成',
            cancelButtonText: '取消',
            type: 'info',
            dangerouslyUseHTMLString: true
          }
        )

        // 显示加载状态
        const loading = this.$loading({
          lock: true,
          text: '正在生成SQL模板...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        try {
          const response = await generateSqlTemplate(row.modelId)

          if (response.code === 1) {
            const templateData = response.data

            // 显示成功消息
            this.$notify({
              title: '生成成功',
              message: `SQL模板"${templateData.templateName}"已生成\n模板编码：${templateData.templateCode}`,
              type: 'success',
              duration: 5000,
              dangerouslyUseHTMLString: true
            })

            // 可选：询问是否打开SQL模板管理
            this.$confirm('是否立即打开SQL模板管理查看生成的模板？', '提示', {
              confirmButtonText: '打开',
              cancelButtonText: '稍后',
              type: 'info'
            }).then(() => {
              // 触发父组件打开SQL模板管理
              this.$emit('open-sql-template-management', templateData.templateId)
            }).catch(() => {
              // 用户选择稍后，不做任何操作
            })

          } else {
            this.$message.error(response.msg || '生成SQL模板失败')
          }
        } finally {
          loading.close()
        }

      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('生成SQL模板失败')
          console.error('生成SQL模板失败:', error)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除此模型吗？删除后无法恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteDataModel(row.modelId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
          console.error('删除模型失败:', error)
        }
      }
    },



    // 选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 批量发布
    async handleBatchPublish() {
      try {
        await this.$confirm('确定要批量发布选中的模型吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // TODO: 实现批量发布API
        this.$message.success('批量发布成功')
        this.loadData()
        this.loadStatistics()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量发布失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要批量删除选中的模型吗？删除后无法恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const modelIds = this.multipleSelection.map(item => item.modelId)
        const response = await batchDeleteDataModel(modelIds)

        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
          console.error('批量删除模型失败:', error)
        }
      }
    },

    // 编辑成功回调
    handleEditSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.queryForm.pageSize = size
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.queryForm.pageNum = page
      this.loadData()
    },

    // 获取模型类型标签类型
    getModelTypeTagType(type) {
      const typeMap = {
        'FINANCIAL': 'primary',
        'RISK': 'warning',
        'AUDIT': 'success',
        'BUSINESS': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取模型类型标签
    getModelTypeLabel(type) {
      const labelMap = {
        'FINANCIAL': '财务',
        'RISK': '风险',
        'AUDIT': '审计',
        'BUSINESS': '业务'
      }
      return labelMap[type] || type
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'TESTING': 'warning',
        'PUBLISHED': 'success'
      }
      return typeMap[status] || 'info'
    },

    // 获取状态标签
    getStatusLabel(status) {
      const labelMap = {
        'DRAFT': '草稿',
        'TESTING': '测试中',
        'PUBLISHED': '已发布'
      }
      return labelMap[status] || status
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.searchKeyword = ''
      this.queryForm.pageNum = 1
      this.queryForm.modelName = ''
      this.queryForm.modelType = ''
      this.queryForm.status = ''
      this.queryForm.dataSourceId = ''
      this.modelData = []
      this.multipleSelection = []
    }
  }
}
</script>

<style scoped>
.data-model-container {
  padding: 0;
}

.toolbar {
  margin-bottom: 20px;
}

.filter-bar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.statistics-bar {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  padding: 15px;
  border-radius: 4px;
  text-align: center;
  border: 1px solid #e4e7ed;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.model-list {
  background: #fff;
}

.pagination-container {
  padding: 20px 0;
  text-align: right;
}

.batch-actions {
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
