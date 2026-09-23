<template>
  <div class="scheme-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          成本估算方案管理
        </h1>
        <p class="page-description">管理成本估算方案的创建、配置、启用和维护</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建方案
        </el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImport">
          导入方案
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">
          导出方案
        </el-button>
      </div>
    </div>

    <!-- 搜索条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="方案编码" prop="schemeCode">
          <el-input
            v-model="queryForm.schemeCode"
            placeholder="请输入方案编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="方案名称" prop="schemeName">
          <el-input
            v-model="queryForm.schemeName"
            placeholder="请输入方案名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="估算类型" prop="estimationType">
          <el-select
            v-model="queryForm.estimationType"
            placeholder="请选择估算类型"
            clearable
            style="width: 200px"
          >
            <el-option label="产品成本估算" :value="1" />
            <el-option label="项目成本估算" :value="2" />
            <el-option label="作业成本估算" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态" prop="isEnabled">
          <el-select
            v-model="queryForm.isEnabled"
            placeholder="请选择启用状态"
            clearable
            style="width: 150px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="schemeCode" label="方案编码" width="150" />
        <el-table-column prop="schemeName" label="方案名称" min-width="200" />
        <el-table-column prop="estimationTypeName" label="估算类型" width="120" />
        <el-table-column prop="description" label="方案描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="isEnabledName" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
              {{ scope.row.isEnabledName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button size="mini" type="text" @click="handleCopy(scope.row)">
              复制
            </el-button>
            <el-button
              size="mini"
              type="text"
              :class="scope.row.isEnabled === 1 ? 'danger-text' : 'success-text'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button size="mini" type="text" class="danger-text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
        class="dialog-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="方案编码" prop="schemeCode">
              <el-input
                v-model="formData.schemeCode"
                placeholder="请输入方案编码"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案名称" prop="schemeName">
              <el-input
                v-model="formData.schemeName"
                placeholder="请输入方案名称"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="估算类型" prop="estimationType">
              <el-select
                v-model="formData.estimationType"
                placeholder="请选择估算类型"
                style="width: 100%"
                :disabled="isView"
              >
                <el-option label="产品成本估算" :value="1" />
                <el-option label="项目成本估算" :value="2" />
                <el-option label="作业成本估算" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="启用状态" prop="isEnabled">
              <el-switch
                v-model="formData.isEnabled"
                :active-value="1"
                :inactive-value="0"
                active-text="启用"
                inactive-text="停用"
                :disabled="isView"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="方案描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入方案描述"
            :disabled="isView"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSchemeManagementPage,
  saveOrUpdateSchemeManagement,
  deleteCostEstimateScheme,
  activateScheme,
  exportCostEstimateDataBlob,
  batchImportCostEstimate
} from '@/api/financialSharing/costEstimation'
import { normalizeKeysArray } from '@/utils/keyNormalize'

export default {
  name: 'SchemeManagement',
  data() {
    return {
      loading: false,
      submitLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        schemeCode: '',
        schemeName: '',
        estimationType: null,
        isEnabled: null
      },
      dialogVisible: false,
      dialogTitle: '',
      isView: false,
      formData: {
        schemeId: null,
        schemeCode: '',
        schemeName: '',
        estimationType: null,
        description: '',
        isEnabled: 1
      },
      formRules: {
        schemeCode: [
          { required: true, message: '请输入方案编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        schemeName: [
          { required: true, message: '请输入方案名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        estimationType: [
          { required: true, message: '请选择估算类型', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    // 计算属性可以在这里添加
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const response = await getSchemeManagementPage(this.queryForm)

        if (response && response.code === 1) {
          // 后端 PageResult: tlist + totalRecord（兼容 list/records/total）
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.list || dataObj.records || []
          this.total = dataObj.totalRecord || dataObj.total || 0

          // 达梦/Oracle 返回大写列名（SCHEMENAME/SCHEMECODE...），统一归一化到驼峰
          this.tableData = normalizeKeysArray(rawList).map(item => {
            // 估算类型名称映射（兼容字符串枚举与数字枚举）
            const typeMap = {
              PRODUCT: '产品成本估算',
              PROJECT: '项目成本估算',
              ACTIVITY: '作业成本估算',
              1: '产品成本估算',
              2: '项目成本估算',
              3: '作业成本估算'
            }
            const typeKey = item.estimationType != null ? item.estimationType : item.schemeType
            item.estimationTypeName = typeMap[typeKey] || (typeKey || '未知')

            // 启用状态名称映射
            item.isEnabledName = Number(item.isEnabled) === 1 ? '启用' : '停用'
            // 兼容方案描述：优先 description，回退 schemeDescription
            if (!item.description && item.schemeDescription) {
              item.description = item.schemeDescription
            }
            return item
          })
        } else if (response && response.msg) {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + (error && error.message ? error.message : ''))
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新建估算方案'
      this.isView = false
      this.resetFormData()
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看估算方案'
      this.isView = true
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑估算方案'
      this.isView = false
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制估算方案'
      this.isView = false
      this.formData = { 
        ...row, 
        schemeId: null,
        schemeCode: row.schemeCode + '_COPY',
        schemeName: row.schemeName + '_副本'
      }
      this.dialogVisible = true
    },

    // 切换状态
    async handleToggleStatus(row) {
      try {
        const action = row.isEnabled === 1 ? '停用' : '启用'
        await this.$confirm(`确认${action}该估算方案吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const newStatus = row.isEnabled === 1 ? 0 : 1
        await activateScheme(row.schemeId, newStatus)

        this.$message.success(`${action}成功`)
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该估算方案吗？删除后不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await deleteCostEstimateScheme(row.schemeId)

        this.$message.success('删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.formRef.validate()
        this.submitLoading = true

        await saveOrUpdateSchemeManagement(this.formData)

        this.$message.success('保存成功')
        this.dialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.submitLoading = false
      }
    },

    // 导入
    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        try {
          const res = await batchImportCostEstimate(formData)
          if (res.code === 1 || res.code === 200) {
            this.$message.success('导入成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '导入失败')
          }
        } catch (error) {
          this.$message.error('导入失败')
        }
      }
      input.click()
    },

    // 导出
    async handleExport() {
      try {
        // 把当前查询条件透传给后端，让导出范围与列表一致
        const exportParam = { ...this.queryForm, type: 'scheme' }
        // 后端实际返回 CSV (text/csv)，axios 拦截器 blob 模式返回 { data, headers, status }
        const resp = await exportCostEstimateDataBlob(exportParam)
        // 真正的 blob 数据在 resp.data；旧代码直接用 resp 等于把整个对象塞进 Blob，导致文件损坏
        const rawData = (resp && resp.data) ? resp.data : resp
        // 统一按 CSV 处理（与后端 Content-Type 一致），扩展名也用 .csv
        const blob = new Blob([rawData], { type: 'text/csv;charset=utf-8' })
        // 优先采用后端 Content-Disposition 的文件名
        let fileName = ''
        try {
          const cd = resp && resp.headers && (resp.headers['content-disposition'] || resp.headers['Content-Disposition'])
          if (cd) {
            const m = cd.match(/filename\*?=(?:UTF-8'')?["']?([^;"'\n]+)["']?/i)
            if (m && m[1]) fileName = decodeURIComponent(m[1].trim())
          }
        } catch (_) { /* ignore */ }
        if (!fileName) {
          const ts = new Date().toISOString().slice(0, 19).replace(/[T:-]/g, '')
          fileName = `成本估算方案_${ts}.csv`
        }
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败：' + (error && error.message ? error.message : ''))
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 对话框关闭
    handleDialogClose() {
      this.$refs.formRef?.resetFields()
      this.resetFormData()
    },

    // 重置表单数据
    resetFormData() {
      this.formData = {
        schemeId: null,
        schemeCode: '',
        schemeName: '',
        estimationType: null,
        description: '',
        isEnabled: 1
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.scheme-management-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .header-right {
    .el-button {
      margin-left: 10px;
    }
  }
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.dialog-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

.danger-text {
  color: #f56c6c !important;
}

.success-text {
  color: #67c23a !important;
}
</style>
