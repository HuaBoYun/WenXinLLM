<template>
  <div class="financial-common-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-operation"></i>
          影响因素管理
        </h1>
        <p class="page-description">定义和管理影响会计处理的各种因素</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增因素
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 查询条件 -->
      <el-card class="search-card">
        <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
          <el-form-item label="因素编码" prop="factorCode">
            <el-input
              v-model="queryForm.factorCode"
              placeholder="请输入因素编码"
              clearable
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="因素名称" prop="factorName">
            <el-input
              v-model="queryForm.factorName"
              placeholder="请输入因素名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>

          <el-form-item label="因素类型" prop="factorType">
            <el-select
              v-model="queryForm.factorType"
              placeholder="请选择因素类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="(name, value) in FACTOR_TYPE_NAME"
                :key="value"
                :label="name"
                :value="value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="启用状态" prop="isEnabled">
            <el-select
              v-model="queryForm.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option
                v-for="(name, value) in ENABLED_STATUS_NAME"
                :key="value"
                :label="name"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
            <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 数据表格 -->
      <el-card class="table-card">
        <!-- 操作按钮 -->
        <div class="table-operations">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleBatchDelete"
          >批量删除</el-button>
          <el-button
            type="warning"
            icon="el-icon-setting"
            size="mini"
            @click="handleBatchEnable"
          >批量启用</el-button>
          <el-button
            type="info"
            icon="el-icon-setting"
            size="mini"
            @click="handleBatchDisable"
          >批量禁用</el-button>
        </div>

        <el-table
          v-loading="loading"
          :data="factorList"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
          border
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="因素编码" prop="factorCode" width="120" />
          <el-table-column label="因素名称" prop="factorName" width="180" show-overflow-tooltip />
          <el-table-column label="因素类型" prop="factorTypeName" width="120" align="center">
            <template slot-scope="scope">
              <el-tag :type="getFactorTypeColor(scope.row.factorType)" size="mini">
                {{ scope.row.factorTypeName || getFactorTypeName(scope.row.factorType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="计算公式" prop="calculationFormula" width="150" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.calculationFormula || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="因素描述" prop="description" width="200" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.description || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="启用状态" width="100" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isEnabled"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="description" min-width="200" show-overflow-tooltip />
          <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
          <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleView(scope.row)"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          v-show="total > 0"
          background
          :current-page="queryForm.pageNumber"
          :page-size="queryForm.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          style="margin-top: 20px; text-align: right;"
        />
      </el-card>
    </div>

    <!-- 因素表单对话框 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="因素编码" prop="factorCode">
          <el-input v-model="form.factorCode" placeholder="请输入因素编码" :disabled="formType === 'edit'" />
        </el-form-item>
        <el-form-item label="因素名称" prop="factorName">
          <el-input v-model="form.factorName" placeholder="请输入因素名称" />
        </el-form-item>
        <el-form-item label="因素类型" prop="factorType">
          <el-select v-model="form.factorType" placeholder="请选择因素类型" style="width: 100%">
            <el-option
              v-for="(name, value) in FACTOR_TYPE_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="计算公式" v-if="form.factorType === 3">
          <el-input
            v-model="form.calculationFormula"
            type="textarea"
            :rows="2"
            placeholder="请输入计算公式，如：(1-残值率)/使用年限"
          />
        </el-form-item>
        <el-form-item label="因素描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入因素描述"
          />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </el-dialog>

    <!-- 使用影响因素表单组件 -->
    <influence-factor-form
      v-if="false"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import InfluenceFactorForm from '../components/InfluenceFactorForm'
import {
  getInfluenceFactorPage,
  getInfluenceFactorById,
  saveInfluenceFactor,
  updateInfluenceFactor,
  deleteInfluenceFactor,
  updateInfluenceFactorStatus,
  batchDeleteInfluenceFactors,
  batchUpdateInfluenceFactorStatus
} from '@/api/financialSharing/system'

export default {
  name: 'InfluenceFactor',
  components: {
    InfluenceFactorForm
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        factorCode: null,
        factorName: null,
        factorType: null,
        isEnabled: null
      },
      // 加载状态
      loading: true,
      // 表格数据
      factorList: [],
      // 选中数据
      selectedFactors: [],
      // 总条数
      total: 0,
      // 非多个禁用
      multiple: true,
      // 表单相关
      formVisible: false,
      formType: 'add', // add/edit/view
      formTitle: '',
      submitting: false,
      form: {
        factorId: null,
        factorCode: '',
        factorName: '',
        factorType: 1,
        dataType: 'STRING',
        factorValue: '',
        defaultValue: '',
        calculationFormula: '',
        affectScope: '',
        priorityLevel: 1,
        isEnabled: 1,
        description: '',
        tenantId: 1,
        bookId: 1,
        version: 1
      },
      formRules: {
        factorCode: [
          { required: true, message: '请输入因素编码', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '因素编码格式不正确', trigger: 'blur' }
        ],
        factorName: [
          { required: true, message: '请输入因素名称', trigger: 'blur' }
        ],
        factorType: [
          { required: true, message: '请选择因素类型', trigger: 'change' }
        ]
      },
      // 常量定义
      FACTOR_TYPE_NAME: {
        1: '业务类型',
        2: '科目类型',
        3: '辅助核算',
        4: '其他'
      },
      ENABLED_STATUS_NAME: {
        0: '禁用',
        1: '启用'
      }
    }
  },
  mounted() {
    this.loadFactorList()
  },
  methods: {
    /** 加载影响因素列表 */
    async loadFactorList() {
      this.loading = true
      try {
        // 准备请求参数 - 参考系统参数页面的实现
        const params = {
          pageNo: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          factorCode: this.queryForm.factorCode || undefined,
          factorName: this.queryForm.factorName || undefined,
          factorType: this.queryForm.factorType || undefined,
          isEnabled: this.queryForm.isEnabled,
          tenantId: 1 // 添加租户ID参数
        }

        // 调用API获取数据 - 使用导入的API函数
        const response = await getInfluenceFactorPage(params)

        if (response.code === 1) {
          // 正确处理PageResult格式的响应数据
          const pageData = response.data
          if (pageData) {
            this.factorList = pageData.tlist || []
            this.total = pageData.totalRecord || 0
          } else {
            this.factorList = []
            this.total = 0
          }
        } else {
          this.$message.error(response.msg || '获取影响因素列表失败')
          this.factorList = []
          this.total = 0
        }
      } catch (error) {
        console.error('加载影响因素列表失败:', error)
        this.$message.error('加载影响因素列表失败: ' + (error.response?.data?.msg || error.message))
        this.factorList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    /** 加载空状态数据（API 不可用时降级展示） */
    loadMockData() {
      this.factorList = []
      this.total = 0
    },

    /** 查询按钮操作 */
    handleQuery() {
      console.log('=== 查询按钮被点击 ===')
      console.log('查询表单数据:', this.queryForm)

      // 强制更新页码为1
      this.queryForm.pageNumber = 1

      console.log('准备调用 loadFactorList...')
      this.loadFactorList()
      console.log('loadFactorList 调用完成')
    },

    /** 重置按钮操作 */
    handleReset() {
      console.log('=== 重置按钮被点击 ===')
      console.log('当前 $refs:', this.$refs)
      console.log('queryForm ref:', this.$refs.queryForm)

      // 检查表单引用是否存在
      if (this.$refs.queryForm && typeof this.$refs.queryForm.resetFields === 'function') {
        this.$refs.queryForm.resetFields()
        console.log('表单重置完成')
      } else {
        // 手动重置表单数据
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          factorCode: '',
          factorName: '',
          factorType: '',
          isEnabled: ''
        }
        console.log('手动重置表单数据完成')
      }

      this.handleQuery()
      console.log('=== 重置按钮执行完成 ===')
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.selectedFactors = selection
      this.multiple = !selection.length
    },

    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table && this.$refs.table.toggleRowSelection(row)
    },

    /** 状态修改 */
    async handleStatusChange(row) {
      const text = row.isEnabled === 1 ? '启用' : '禁用'
      const originalStatus = row.isEnabled
      try {
        await this.$confirm(`确认要${text}"${row.factorName}"吗？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await updateInfluenceFactorStatus(row.factorId, row.isEnabled)
        if (response.code === 1) {
          this.$message.success(`${text}成功`)
        } else {
          // 恢复原状态
          row.isEnabled = originalStatus
          this.$message.error(response.msg || `${text}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          // 恢复原状态
          row.isEnabled = originalStatus
          console.error('状态更新失败:', error)
          this.$message.error('状态更新失败')
        }
      }
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.formType = 'add'
      this.formTitle = '新增影响因素'
      this.form = {
        factorId: null,
        factorCode: '',
        factorName: '',
        factorType: 1,
        factorCategory: '',
        calculationMethod: '',
        unit: '',
        isEnabled: 1,
        remark: ''
      }
      this.formVisible = true
    },

    /** 编辑按钮操作 */
    handleEdit(row) {
      this.formType = 'edit'
      this.formTitle = '编辑影响因素'
      this.form = { ...row }
      this.formVisible = true
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.formType = 'view'
      this.formTitle = '查看影响因素'
      this.form = { ...row }
      this.formVisible = true
    },

    /** 删除按钮操作 */
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除影响因素"${row.factorName}"吗？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteInfluenceFactor(row.factorId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadFactorList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除影响因素失败:', error)
          this.$message.error('删除失败: ' + (error.response?.data?.msg || error.message))
        }
      }
    },

    /** 批量删除 */
    async handleBatchDelete() {
      if (this.selectedFactors.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      try {
        await this.$confirm(`确认删除选中的${this.selectedFactors.length}条数据吗？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedFactors.map(item => item.factorId)
        const response = await batchDeleteInfluenceFactors(ids)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadFactorList()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除影响因素失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    /** 批量启用 */
    async handleBatchEnable() {
      await this.handleBatchStatusChange(1, '启用')
    },

    /** 批量禁用 */
    async handleBatchDisable() {
      await this.handleBatchStatusChange(0, '禁用')
    },

    /** 批量状态变更 */
    async handleBatchStatusChange(status, action) {
      if (this.selectedFactors.length === 0) {
        this.$message.warning(`请选择要${action}的数据`)
        return
      }
      try {
        await this.$confirm(`确认要批量${action}选中的${this.selectedFactors.length}条数据吗？`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedFactors.map(item => item.factorId)
        const response = await batchUpdateInfluenceFactorStatus(ids, status)
        if (response.code === 1) {
          this.$message.success(`批量${action}成功`)
          this.loadFactorList()
        } else {
          this.$message.error(response.msg || `批量${action}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error(`批量${action}失败:`, error)
          this.$message.error(`批量${action}失败`)
        }
      }
    },

    /** 导出按钮操作 */
    handleExport() {
      try {
        const data = this.list || this.tableData || []
        if (!data.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '影响因素导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) { this.$message.error('导出失败') }
    },

    /** 表单成功回调 */
    handleFormSuccess() {
      this.formVisible = false
      this.loadFactorList()
    },

    /** 提交表单 */
    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          this.saveInfluenceFactor()
        }
      })
    },

    /** 保存影响因素 */
    async saveInfluenceFactor() {
      try {
        // 准备请求数据 - 匹配后端Entity字段
        const requestData = {
          factorCode: this.form.factorCode,
          factorName: this.form.factorName,
          factorType: this.form.factorType,
          factorCategory: this.form.factorCategory,
          calculationMethod: this.form.calculationMethod,
          unit: this.form.unit,
          description: this.form.remark,
          isEnabled: this.form.isEnabled,
          tenantId: 1, // 添加租户ID
          bookId: 1   // 添加账簿ID
        }

        let response
        if (this.formType === 'edit') {
          // 编辑时添加ID
          requestData.factorId = this.form.factorId
          response = await updateInfluenceFactor(requestData)
        } else {
          // 新增
          response = await saveInfluenceFactor(requestData)
        }

        if (response.code === 1) {
          this.$message.success(response.msg || '保存成功')
          this.formVisible = false
          this.loadFactorList()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存影响因素失败:', error)
        this.$message.error('保存失败: ' + (error.response?.data?.msg || error.message))
      } finally {
        this.submitting = false
      }
    },

    /** 重置表单 */
    resetForm() {
      console.log('=== resetForm 开始执行 ===')
      console.log('当前 $refs:', this.$refs)
      console.log('form ref:', this.$refs.form)

      // 检查表单引用是否存在
      if (this.$refs.form && typeof this.$refs.form.resetFields === 'function') {
        this.$refs.form.resetFields()
        console.log('表单重置完成')
      } else {
        console.warn('表单引用不存在或resetFields方法不可用')
      }
      console.log('=== resetForm 执行完成 ===')
    },

    /** 改变每一页请求数量 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.loadFactorList()
    },

    /** 跳转页数 */
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadFactorList()
    },

    /** 获取因素类型颜色 */
    getFactorTypeColor(type) {
      const colorMap = {
        1: 'success',
        2: 'primary',
        3: 'warning',
        4: 'info'
      }
      return colorMap[type] || 'info'
    },

    /** 获取数据类型颜色 */
    getDataTypeColor(type) {
      const colorMap = {
        1: '',
        2: 'success',
        3: 'warning',
        4: 'danger'
      }
      return colorMap[type] || 'info'
    },

    /** 获取因素类型名称 */
    getFactorTypeName(factorType) {
      return this.FACTOR_TYPE_NAME[factorType] || '未知'
    },

    /** 返回上级 */
    goBack() {
      this.$router.push('/financialSharing/platform/common')
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-common-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
}

.content-area {
  .search-card, .table-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    margin-bottom: 24px;

    &:hover {
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    }
  }
}

.search-form {
  .el-form-item {
    margin-bottom: 0;
  }
}

.table-operations {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}
</style>