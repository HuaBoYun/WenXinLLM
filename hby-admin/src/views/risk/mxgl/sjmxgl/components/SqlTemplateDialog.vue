<template>
  <el-dialog
    title="SQL模板管理"
    :visible.sync="dialogVisible"
    width="1400px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    custom-class="sql-template-dialog"
    @close="handleClose"
  >
    <div class="sql-template-container">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-row :gutter="20">
          <el-col :span="18">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索模板名称、描述或标签"
              clearable
              @clear="handleSearch"
              @keyup.enter.native="handleSearch"
              style="width: 100%"
            >
              <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
            </el-input>
          </el-col>
          <el-col :span="6">
            <!-- 预留空间，可用于其他快捷操作 -->
          </el-col>
        </el-row>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select v-model="queryForm.templateCategory" placeholder="选择分类" clearable @change="handleSearch">
              <el-option label="基础查询" value="BASIC" />
              <el-option label="统计分析" value="ANALYSIS" />
              <el-option label="报表查询" value="REPORT" />
              <el-option label="数据维护" value="MAINTENANCE" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="queryForm.databaseType" placeholder="数据库类型" clearable @change="handleSearch">
              <el-option
                v-for="item in databaseTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="queryForm.isSystem" placeholder="模板类型" clearable @change="handleSearch">
              <el-option label="系统预置" value="Y" />
              <el-option label="自定义" value="N" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select v-model="queryForm.isEnabled" placeholder="启用状态" clearable @change="handleSearch">
              <el-option label="已启用" value="Y" />
              <el-option label="已禁用" value="N" />
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
              <div class="stat-label">总模板数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.systemCount || 0 }}</div>
              <div class="stat-label">系统预置</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.enabledCount || 0 }}</div>
              <div class="stat-label">已启用</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ statistics.categoryCount || 0 }}</div>
              <div class="stat-label">分类数量</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增模板</el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchEnable">批量启用</el-button>
          <el-button type="warning" icon="el-icon-close" @click="handleBatchDisable">批量禁用</el-button>
        </div>
      </div>

      <!-- 模板列表 -->
      <div class="template-list">
        <el-table
          :data="templateData"
          v-loading="loading"
          border
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="templateName" label="模板名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="120">
            <template slot-scope="scope">
              <el-tag :type="getCategoryTagType(scope.row.category)">
                {{ getCategoryLabel(scope.row.category) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="databaseType" label="数据库类型" width="100">
            <template slot-scope="scope">
              <el-tag size="mini">{{ scope.row.databaseType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isSystem" label="类型" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isSystem === 'Y' ? 'warning' : 'primary'" size="mini">
                {{ scope.row.isSystem === 'Y' ? '系统' : '自定义' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isEnabled" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="getStatusTagType(scope.row)" size="mini">
                {{ getStatusText(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="usageCount" label="使用次数" width="100" />
          <el-table-column prop="lastUsedTime" label="最后使用" width="160">
            <template slot-scope="scope">
              {{ formatDate(scope.row.lastUsedTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
              <el-button size="mini" type="success" @click="handleCopy(scope.row)" icon="el-icon-document-copy">复制</el-button>
              <el-dropdown @command="handleMoreAction" trigger="click">
                <el-button size="mini" type="text">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{action: 'use', row: scope.row}">
                    使用模板
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'toggle', row: scope.row}">
                    {{ scope.row.isEnabled === 'Y' ? '禁用' : '启用' }}
                  </el-dropdown-item>
                  <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided v-if="scope.row.isSystem !== 'Y'">
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
      <!-- 批量操作框已移至工具栏，避免重复 -->
    </div>

    <!-- 模板编辑对话框 -->
    <SqlTemplateEditDialog
      :visible.sync="editDialogVisible"
      :template-data="currentTemplate"
      :is-edit="isEdit"
      @success="handleEditSuccess"
    />

    <!-- 模板查看对话框 -->
    <SqlTemplateViewDialog
      :visible.sync="viewDialogVisible"
      :data="currentTemplate"
      @edit="handleViewDialogEdit"
      @use="handleViewDialogUse"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getSqlTemplateList,
  getSqlTemplateStatistics,
  getSqlTemplateDetail,
  deleteSqlTemplate,
  batchDeleteSqlTemplate,
  copySqlTemplate,
  toggleSqlTemplateStatus,
  batchUpdateSqlTemplateStatus,
  useSqlTemplate,
  getActiveDataSources
} from '@/api/mxgl'
import SqlTemplateEditDialog from './SqlTemplateEditDialog'
import SqlTemplateViewDialog from './SqlTemplateViewDialog'
// 引入z-index层级管理样式
import './dialog-z-index.css'

export default {
  name: 'SqlTemplateDialog',
  components: {
    SqlTemplateEditDialog,
    SqlTemplateViewDialog
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
        templateName: '',
        templateCategory: '',
        databaseType: '',
        isSystem: '',
        isEnabled: ''
      },
      templateData: [],
      total: 0,
      statistics: {},
      multipleSelection: [],
      // 数据库类型选项
      databaseTypeOptions: [
        { label: '达梦数据库', value: 'DM' },
        { label: 'Oracle', value: 'ORACLE' },
        { label: 'MySQL', value: 'MYSQL' },
        { label: '通用', value: 'ALL' }
      ],
      // 子对话框
      editDialogVisible: false,
      viewDialogVisible: false,
      currentTemplate: {},
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
  mounted() {
    // 组件挂载时预加载数据库类型选项
    this.loadDatabaseTypes()
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadData()
        this.loadStatistics()
        this.loadDatabaseTypes()
      }
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await getSqlTemplateList(this.queryForm)
        console.log('📊 SQL模板列表API响应:', response)

        // 兼容两种响应格式：
        // 1. 标准格式: {code: 1, data: {total, records}}
        // 2. 直接格式: {total, records} (mock数据格式)
        if (response && response.code !== undefined) {
          // 标准格式
          if (response.code === 1) {
            const data = response.data || {}
            this.templateData = data.records || []
            this.total = data.total || 0
            console.log('✅ 模板数据加载成功(标准格式):', {
              total: this.total,
              records: this.templateData.length
            })
          } else {
            this.$message.error(response.msg || '查询失败')
            console.error('❌ API返回错误:', response.msg)
          }
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.templateData = response.records || []
          this.total = response.total || 0
          console.log('✅ 模板数据加载成功(直接格式):', {
            total: this.total,
            records: this.templateData.length
          })
        } else {
          this.$message.error('查询失败: 响应格式错误')
          console.error('❌ 未知的响应格式:', response)
        }
      } catch (error) {
        this.$message.error('查询失败')
        console.error('❌ 查询SQL模板列表异常:', error)
      } finally {
        this.loading = false
      }
    },

    // 加载统计信息
    async loadStatistics() {
      try {
        const response = await getSqlTemplateStatistics()
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },

    // 加载数据库类型选项
    async loadDatabaseTypes() {
      try {
        console.log('开始加载数据库类型选项...')
        const response = await getActiveDataSources()
        console.log('数据源接口响应:', response)

        if (response.code === 1) {
          // 从数据源列表中提取数据库类型
          const dataSources = response.result || []
          console.log('数据源列表:', dataSources)

          const typeSet = new Set()

          // 添加默认选项
          typeSet.add('ALL')

          // 从数据源中提取类型
          dataSources.forEach(ds => {
            console.log('处理数据源:', ds)
            if (ds.sourceType) {
              typeSet.add(ds.sourceType)
              console.log('添加数据库类型:', ds.sourceType)
            }
          })

          console.log('提取到的数据库类型:', Array.from(typeSet))

          // 构建选项列表
          this.databaseTypeOptions = Array.from(typeSet).map(type => {
            const typeMap = {
              'DM': '达梦数据库',
              'ORACLE': 'Oracle',
              'MYSQL': 'MySQL',
              'ALL': '通用'
            }
            return {
              value: type,
              label: typeMap[type] || type
            }
          })

          console.log('最终数据库类型选项:', this.databaseTypeOptions)
        } else {
          console.error('数据源接口返回错误:', response.msg)
          this.$message.error('获取数据库类型失败: ' + response.msg)
        }
      } catch (error) {
        console.error('获取数据库类型失败:', error)
        this.$message.error('获取数据库类型失败')
        // 失败时使用默认选项
      }
    },

    // 搜索
    handleSearch() {
      this.queryForm.templateName = this.searchKeyword
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 新增
    handleAdd() {
      this.currentTemplate = {}
      this.isEdit = false
      this.editDialogVisible = true
    },

    // 编辑
    async handleEdit(row) {
      try {
        this.loading = true

        // 调用API获取模板详情
        const response = await getSqlTemplateDetail(row.templateId)

        if (response.code === 1) {
          this.currentTemplate = response.data
          this.isEdit = true
          this.editDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      } finally {
        this.loading = false
      }
    },

    // 查看
    async handleView(row) {
      try {
        this.loading = true

        // 调用API获取模板详情
        const response = await getSqlTemplateDetail(row.templateId)

        if (response.code === 1) {
          this.currentTemplate = response.data
          this.viewDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      } finally {
        this.loading = false
      }
    },

    // 复制
    async handleCopy(row) {
      try {
        const newName = await this.$prompt('请输入新模板名称', '复制模板', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /\S/,
          inputErrorMessage: '模板名称不能为空'
        })

        const response = await copySqlTemplate({
          templateId: row.templateId,
          newTemplateName: newName.value
        })

        if (response.code === 1) {
          this.$message.success('复制成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复制失败')
          console.error('复制模板失败:', error)
        }
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, row } = command

      switch (action) {
        case 'use':
          await this.handleUseTemplate(row)
          break
        case 'toggle':
          await this.handleToggleStatus(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 使用模板
    async handleUseTemplate(row) {
      try {
        const response = await useSqlTemplate(row.templateId)
        if (response.code === 1) {
          this.$message.success('使用记录已更新')
          this.loadData()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        this.$message.error('操作失败')
        console.error('使用模板失败:', error)
      }
    },

    // 切换状态
    async handleToggleStatus(row) {
      try {
        const newStatus = row.isEnabled === 'Y' ? 'N' : 'Y'
        const action = newStatus === 'Y' ? '启用' : '禁用'

        await this.$confirm(`确定要${action}此模板吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await toggleSqlTemplateStatus({
          templateId: row.templateId,
          isEnabled: newStatus
        })

        if (response.code === 1) {
          this.$message.success(`${action}成功`)
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || `${action}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败')
          console.error('切换模板状态失败:', error)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除此模板吗？删除后无法恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteSqlTemplate(row.templateId)
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
          console.error('删除模板失败:', error)
        }
      }
    },



    // 选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要删除的模板')
          return
        }

        // 检查是否包含系统模板
        const systemTemplates = this.multipleSelection.filter(item => item.isSystem === 'Y')
        if (systemTemplates.length > 0) {
          this.$message.error('选中的模板中包含系统预置模板，无法删除')
          return
        }

        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 个模板吗？删除后无法恢复。`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const templateIds = this.multipleSelection.map(item => item.templateId)
        const response = await batchDeleteSqlTemplate(templateIds)

        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
          this.multipleSelection = []
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
          console.error('批量删除模板失败:', error)
        }
      }
    },

    // 批量启用
    async handleBatchEnable() {
      await this.handleBatchToggleStatus('Y', '启用')
    },

    // 批量禁用
    async handleBatchDisable() {
      await this.handleBatchToggleStatus('N', '禁用')
    },

    // 批量切换状态
    async handleBatchToggleStatus(status, action) {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要操作的模板')
          return
        }

        await this.$confirm(`确定要批量${action}选中的 ${this.multipleSelection.length} 个模板吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const templateIds = this.multipleSelection.map(item => item.templateId)
        const response = await batchUpdateSqlTemplateStatus({
          templateIds: templateIds,
          isEnabled: status  // 统一使用isEnabled参数名
        })

        if (response.code === 1) {
          this.$message.success(`批量${action}成功`)
          this.loadData()
          this.loadStatistics()
          this.multipleSelection = []
        } else {
          this.$message.error(response.msg || `批量${action}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(`批量${action}失败`)
          console.error(`批量${action}模板失败:`, error)
        }
      }
    },

    // 重复的批量删除方法已删除，使用上面的完整版本

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

    // 获取分类标签类型
    getCategoryTagType(category) {
      const typeMap = {
        'BASIC': 'primary',
        'ANALYSIS': 'success',
        'REPORT': 'warning',
        'MAINTENANCE': 'danger'
      }
      return typeMap[category] || 'info'
    },

    // 获取分类标签
    getCategoryLabel(category) {
      const labelMap = {
        'BASIC': '基础查询',
        'ANALYSIS': '统计分析',
        'REPORT': '报表查询',
        'MAINTENANCE': '数据维护'
      }
      return labelMap[category] || category
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    },

    // 获取状态标签类型
    getStatusTagType(row) {
      // 兼容两种状态字段：isEnabled 和 status
      const isEnabled = row.isEnabled
      const status = row.status

      if (isEnabled === 'Y' || status === 'Y' || status === 'ACTIVE') {
        return 'success'
      } else if (isEnabled === 'N' || status === 'N' || status === 'INACTIVE') {
        return 'danger'
      }
      return 'info'
    },

    // 获取状态文本
    getStatusText(row) {
      // 兼容两种状态字段：isEnabled 和 status
      const isEnabled = row.isEnabled
      const status = row.status

      if (isEnabled === 'Y' || status === 'Y' || status === 'ACTIVE') {
        return '启用'
      } else if (isEnabled === 'N' || status === 'N' || status === 'INACTIVE') {
        return '禁用'
      }
      return '未知'
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.searchKeyword = ''
      this.queryForm.pageNum = 1
      this.queryForm.templateName = ''
      this.queryForm.templateCategory = ''
      this.queryForm.databaseType = ''
      this.queryForm.isSystem = ''
      this.queryForm.isEnabled = ''
      this.templateData = []
      this.multipleSelection = []
    },

    // 处理查看对话框的编辑事件
    handleViewDialogEdit(templateData) {
      // 关闭查看对话框
      this.viewDialogVisible = false
      // 设置当前模板数据并打开编辑对话框
      this.currentTemplate = { ...templateData }
      this.isEdit = true
      this.editDialogVisible = true
    },

    // 处理查看对话框的使用事件
    handleViewDialogUse(templateData) {
      // 调用使用模板的方法
      this.handleUseTemplate(templateData)
    }
  }
}
</script>

<style scoped>
.sql-template-container {
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

.search-bar {
  margin-bottom: 15px;
  padding: 10px 0;
}

.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px 0;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.template-list {
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
