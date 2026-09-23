<template>
  <el-dialog
    title="表达式管理"
    :visible.sync="dialogVisible"
    width="1600px"
    :close-on-click-modal="false"
    @close="handleClose"
    class="expression-dialog"
  >
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus">新增表达式</el-button>
          <el-button @click="handleBatchEnable" icon="el-icon-check">批量启用</el-button>
          <el-button @click="handleBatchDisable" icon="el-icon-close">批量禁用</el-button>
          <el-button @click="handleBatchDelete" icon="el-icon-delete" type="danger">批量删除</el-button>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索表达式名称、编码或描述"
            style="width: 300px; margin-right: 10px;"
            @keyup.enter.native="handleSearch"
          >
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
          <el-button @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选条件 -->
    <div class="filter-bar">
      <el-form :model="queryForm" inline>
        <el-form-item label="表达式类型:">
          <el-select v-model="queryForm.expressionType" placeholder="全部类型" clearable style="width: 150px;">
            <el-option label="逻辑表达式" value="LOGICAL" />
            <el-option label="算术表达式" value="ARITHMETIC" />
            <el-option label="比较表达式" value="COMPARISON" />
            <el-option label="函数表达式" value="FUNCTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="表达式分类:">
          <el-select v-model="queryForm.expressionCategory" placeholder="全部分类" clearable style="width: 150px;">
            <el-option label="财务审计" value="FINANCIAL_AUDIT" />
            <el-option label="风险控制" value="RISK_CONTROL" />
            <el-option label="合规检查" value="COMPLIANCE_CHECK" />
            <el-option label="数据验证" value="DATA_VALIDATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态:">
          <el-select v-model="queryForm.isEnabled" placeholder="全部状态" clearable style="width: 120px;">
            <el-option label="启用" value="Y" />
            <el-option label="禁用" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否模板:">
          <el-select v-model="queryForm.isTemplate" placeholder="全部" clearable style="width: 120px;">
            <el-option label="是" value="Y" />
            <el-option label="否" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表达式列表 -->
    <div class="table-container">
      <el-table
        ref="expressionTable"
        :data="tableData"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
        @row-dblclick="handleRowEdit"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="expressionCode" label="表达式编码" width="150" show-overflow-tooltip />
        <el-table-column prop="expressionName" label="表达式名称" width="200" show-overflow-tooltip />
        <el-table-column prop="expressionType" label="类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getExpressionTypeTag(scope.row.expressionType)">
              {{ getExpressionTypeText(scope.row.expressionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expressionCategory" label="分类" width="120" show-overflow-tooltip />
        <el-table-column prop="outputType" label="输出类型" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getOutputTypeTag(scope.row.outputType)">
              {{ scope.row.outputType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complexityLevel" label="复杂度" width="80" align="center">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.complexityLevel"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}"
            />
          </template>
        </el-table-column>
        <el-table-column prop="usageCount" label="使用次数" width="100" align="center" />
        <el-table-column prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              active-value="Y"
              inactive-value="N"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="isTemplate" label="模板" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isTemplate === 'Y'" type="success" size="mini">是</el-tag>
            <el-tag v-else type="info" size="mini">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isSystem" label="系统预置" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isSystem === 'Y'" type="warning" size="mini">是</el-tag>
            <el-tag v-else type="info" size="mini">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)" style="margin-left: 5px;">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="test">测试</el-dropdown-item>
                <el-dropdown-item command="template" v-if="scope.row.isTemplate === 'N'">设为模板</el-dropdown-item>
                <el-dropdown-item command="code">生成代码</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
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

    <!-- 表达式编辑对话框 -->
    <ExpressionEditDialog
      :visible.sync="editDialogVisible"
      :expression-data="currentExpression"
      @refresh="loadExpressionList"
    />

    <!-- 表达式查看对话框 -->
    <ExpressionViewDialog
      :visible.sync="viewDialogVisible"
      :expression-data="currentExpression"
    />

    <!-- 表达式测试对话框 -->
    <ExpressionTestDialog
      :visible.sync="testDialogVisible"
      :expression-data="currentExpression"
    />

    <!-- 表达式复制对话框 -->
    <ExpressionCopyDialog
      :visible.sync="copyDialogVisible"
      :expression-data="currentExpression"
      @refresh="loadExpressionList"
    />

    <!-- 代码生成对话框 -->
    <ExpressionCodeDialog
      :visible.sync="codeDialogVisible"
      :expression-data="currentExpression"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getExpressionList,
  deleteExpression,
  batchDeleteExpression,
  updateExpressionStatus,
  batchUpdateExpressionStatus,
  searchExpressions
} from '@/api/mxgl'
import ExpressionEditDialog from './ExpressionEditDialog'
import ExpressionViewDialog from './ExpressionViewDialog'
import ExpressionTestDialog from './ExpressionTestDialog'
import ExpressionCopyDialog from './ExpressionCopyDialog'
import ExpressionCodeDialog from './ExpressionCodeDialog'

export default {
  name: 'ExpressionDialog',
  components: {
    ExpressionEditDialog,
    ExpressionViewDialog,
    ExpressionTestDialog,
    ExpressionCopyDialog,
    ExpressionCodeDialog
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
      tableData: [],
      total: 0,
      selectedRows: [],
      searchKeyword: '',
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        expressionName: '',
        expressionType: '',
        expressionCategory: '',
        isEnabled: '',
        isTemplate: ''
      },
      currentExpression: null,
      editDialogVisible: false,
      viewDialogVisible: false,
      testDialogVisible: false,
      copyDialogVisible: false,
      codeDialogVisible: false
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
        this.loadExpressionList()
      }
    }
  },
  methods: {
    // 加载表达式列表
    async loadExpressionList() {
      this.loading = true
      try {
        const response = await getExpressionList(this.queryForm)
        if (response.code === 1) {
          // 🔥 修复数据结构适配 - 示例云JsonBean格式
          this.tableData = response.data || []  // data直接是数组
          this.total = response.result?.total || 0  // 分页信息在result中

          console.log('表达式列表加载成功:', {
            dataLength: this.tableData.length,
            total: this.total,
            responseData: response.data,
            responseResult: response.result
          })
        } else {
          this.$message.error(response.msg || '获取表达式列表失败')
        }
      } catch (error) {
        this.$message.error('获取表达式列表失败')
        console.error('获取表达式列表失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 新增表达式
    handleAdd() {
      this.currentExpression = null
      this.editDialogVisible = true
    },

    // 编辑表达式
    handleEdit(row) {
      this.currentExpression = { ...row }
      this.editDialogVisible = true
    },

    // 双击编辑
    handleRowEdit(row) {
      this.handleEdit(row)
    },

    // 查看表达式
    handleView(row) {
      this.currentExpression = { ...row }
      this.viewDialogVisible = true
    },

    // 删除表达式
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个表达式吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteExpression(row.expressionId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadExpressionList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
          console.error('删除表达式失败:', error)
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的表达式')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 个表达式吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const expressionIds = this.selectedRows.map(row => row.expressionId).join(',')
        const response = await batchDeleteExpression({ expressionIds })
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadExpressionList()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
          console.error('批量删除表达式失败:', error)
        }
      }
    },

    // 状态变更
    async handleStatusChange(row) {
      try {
        const response = await updateExpressionStatus({
          expressionId: row.expressionId,
          isEnabled: row.isEnabled
        })
        if (response.code === 1) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.msg || '状态更新失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      } catch (error) {
        this.$message.error('状态更新失败')
        // 恢复原状态
        row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        console.error('更新表达式状态失败:', error)
      }
    },

    // 批量启用
    async handleBatchEnable() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要启用的表达式')
        return
      }

      try {
        const expressionIds = this.selectedRows.map(row => row.expressionId).join(',')
        const response = await batchUpdateExpressionStatus({
          expressionIds,
          isEnabled: 'Y'
        })
        if (response.code === 1) {
          this.$message.success('批量启用成功')
          this.loadExpressionList()
        } else {
          this.$message.error(response.msg || '批量启用失败')
        }
      } catch (error) {
        this.$message.error('批量启用失败')
        console.error('批量启用表达式失败:', error)
      }
    },

    // 批量禁用
    async handleBatchDisable() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要禁用的表达式')
        return
      }

      try {
        const expressionIds = this.selectedRows.map(row => row.expressionId).join(',')
        const response = await batchUpdateExpressionStatus({
          expressionIds,
          isEnabled: 'N'
        })
        if (response.code === 1) {
          this.$message.success('批量禁用成功')
          this.loadExpressionList()
        } else {
          this.$message.error(response.msg || '批量禁用失败')
        }
      } catch (error) {
        this.$message.error('批量禁用失败')
        console.error('批量禁用表达式失败:', error)
      }
    },

    // 搜索
    async handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.loadExpressionList()
        return
      }

      this.loading = true
      try {
        const response = await searchExpressions({
          keyword: this.searchKeyword,
          isEnabled: 'Y'
        })
        if (response.code === 1) {
          this.tableData = response.data || []
          this.total = this.tableData.length
        } else {
          this.$message.error(response.msg || '搜索失败')
        }
      } catch (error) {
        this.$message.error('搜索失败')
        console.error('搜索表达式失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadExpressionList()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        pageNum: 1,
        pageSize: 20,
        expressionName: '',
        expressionType: '',
        expressionCategory: '',
        isEnabled: '',
        isTemplate: ''
      }
      this.searchKeyword = ''
      this.loadExpressionList()
    },

    // 刷新
    handleRefresh() {
      this.loadExpressionList()
    },

    // 选择变更
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 分页大小变更
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNum = 1
      this.loadExpressionList()
    },

    // 当前页变更
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.loadExpressionList()
    },

    // 下拉菜单命令
    handleCommand(command, row) {
      this.currentExpression = { ...row }
      switch (command) {
        case 'copy':
          this.copyDialogVisible = true
          break
        case 'test':
          this.testDialogVisible = true
          break
        case 'template':
          this.handleSetTemplate(row)
          break
        case 'code':
          this.codeDialogVisible = true
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 设为模板
    async handleSetTemplate(row) {
      try {
        await this.$confirm('确定要将此表达式设为模板吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })

        // TODO: 调用设为模板的API
        this.$message.success('设为模板成功')
        this.loadExpressionList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('设为模板失败')
        }
      }
    },

    // 获取表达式类型标签
    getExpressionTypeTag(type) {
      const tagMap = {
        'LOGICAL': 'primary',
        'ARITHMETIC': 'success',
        'COMPARISON': 'warning',
        'FUNCTION': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取表达式类型文本
    getExpressionTypeText(type) {
      const textMap = {
        'LOGICAL': '逻辑',
        'ARITHMETIC': '算术',
        'COMPARISON': '比较',
        'FUNCTION': '函数'
      }
      return textMap[type] || type
    },

    // 获取输出类型标签
    getOutputTypeTag(type) {
      const tagMap = {
        'BOOLEAN': 'success',
        'NUMBER': 'primary',
        'STRING': 'info',
        'DATE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.searchKeyword = ''
      this.selectedRows = []
      this.$emit('refresh')
    }
  }
}
</script>

<style scoped>
.expression-dialog {
  height: 90vh;
}

.toolbar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.filter-bar {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 4px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
