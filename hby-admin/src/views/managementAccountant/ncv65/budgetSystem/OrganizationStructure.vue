<template>
  <div class="organization-structure">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算组织体系管理</h2>
      <p>管理预算组织体系，支持5种组织体系类型</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="体系名称">
          <el-input
            v-model="queryForm.structureName"
            placeholder="请输入体系名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="体系类型">
          <el-select
            v-model="queryForm.structureType"
            placeholder="请选择体系类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in structureTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="控制模式">
          <el-select
            v-model="queryForm.controlMode"
            placeholder="请选择控制模式"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in controlModeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="激活" value="active" />
            <el-option label="停用" value="inactive" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="toolbar-card" shadow="never">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
            新增体系
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            :disabled="!multipleSelection.length"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            :disabled="!multipleSelection.length"
            @click="handleBatchEnable"
          >
            批量启用
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-close"
            :disabled="!multipleSelection.length"
            @click="handleBatchDisable"
          >
            批量禁用
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button icon="el-icon-download" @click="handleExport">
            导出
          </el-button>
          <el-button icon="el-icon-upload2" @click="handleImport">
            导入
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        :empty-text="tableData.length === 0 ? '暂无数据' : ''"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="structureCode" label="体系编码" width="120" />
        <el-table-column prop="structureName" label="体系名称" min-width="150" />
        <el-table-column prop="structureType" label="体系类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStructureTypeTagType(scope.row.structureType)">
              {{ formatStructureType(scope.row.structureType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="controlMode" label="控制模式" width="120">
          <template slot-scope="scope">
            <el-tag :type="getControlModeTagType(scope.row.controlMode)">
              {{ formatControlMode(scope.row.controlMode) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="maxLevels" label="最大层级" width="100" align="center" />
        <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              :value="scope.row.isEnabled === 1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleCopy(scope.row)">
              复制
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体系编码" prop="structureCode">
              <el-input v-model="form.structureCode" placeholder="请输入体系编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体系名称" prop="structureName">
              <el-input v-model="form.structureName" placeholder="请输入体系名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体系类型" prop="structureType">
              <el-select v-model="form.structureType" placeholder="请选择体系类型" style="width: 100%">
                <el-option
                  v-for="item in structureTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制模式" prop="controlMode">
              <el-select v-model="form.controlMode" placeholder="请选择控制模式" style="width: 100%">
                <el-option
                  v-for="item in controlModeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大层级" prop="maxLevels">
              <el-input-number
                v-model="form.maxLevels"
                :min="1"
                :max="20"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch :value="form.isEnabled === 1" @change="val => form.isEnabled = val ? 1 : 0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="体系描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入体系描述"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog
      title="组织体系详情"
      :visible.sync="viewDialogVisible"
      width="800px"
    >
      <el-descriptions :column="2" border size="medium">
        <el-descriptions-item label="体系编码">{{ viewData.structureCode }}</el-descriptions-item>
        <el-descriptions-item label="体系名称">{{ viewData.structureName }}</el-descriptions-item>
        <el-descriptions-item label="体系类型">
          <el-tag :type="getStructureTypeTagType(viewData.structureType)">
            {{ formatStructureType(viewData.structureType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="控制模式">{{ formatControlMode(viewData.controlMode) }}</el-descriptions-item>
        <el-descriptions-item label="最大层级">{{ viewData.maxLevels }}</el-descriptions-item>
        <el-descriptions-item label="启用状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'info'">
            {{ viewData.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ viewData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ viewData.creatorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDate(viewData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ viewData.updaterName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDate(viewData.updateTime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

// ==================== 本地API调用方法 ====================
// 组件内部定义API调用，使用正确的 /glkj 前缀

/**
 * 分页查询组织体系列表
 */
function getOrganizationStructurePageLocal(current, size, params) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/page',
    method: 'post',
    data: { pageNum: current, pageSize: size, ...params },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 创建组织体系
 */
function createOrganizationStructureLocal(data) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/create',
    method: 'post',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 查询组织体系详情
 */
function getOrganizationStructureLocal(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/detail/${structureId}`,
    method: 'get'
  })
}

/**
 * 更新组织体系
 */
function updateOrganizationStructureLocal(structureId, data) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/update/${structureId}`,
    method: 'put',
    data,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 删除组织体系
 */
function deleteOrganizationStructureLocal(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/delete/${structureId}`,
    method: 'delete'
  })
}

/**
 * 批量删除组织体系
 */
function batchDeleteOrganizationStructuresLocal(ids) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/batch-delete',
    method: 'delete',
    data: { ids },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 启用组织体系
 */
function enableOrganizationStructureLocal(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/enable/${structureId}`,
    method: 'put'
  })
}

/**
 * 禁用组织体系
 */
function disableOrganizationStructureLocal(structureId) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/disable/${structureId}`,
    method: 'put'
  })
}

/**
 * 批量更新启用状态
 */
function batchUpdateEnabledLocal(ids, isEnabled) {
  return request({
    url: '/glkj/accountant/budget/organization-structure/batch-update-enabled',
    method: 'put',
    data: { ids, isEnabled },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 复制组织体系
 */
function copyOrganizationStructureLocal(structureId, newName, newCode) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/copy/${structureId}`,
    method: 'post',
    data: { newName, newCode },
    headers: { 'Content-Type': 'application/json;charset=UTF-8' }
  })
}

/**
 * 检查编码是否存在
 */
function checkStructureCodeExistsLocal(structureCode) {
  return request({
    url: `/glkj/accountant/budget/organization-structure/check-code/${structureCode}`,
    method: 'get'
  })
}

export default {
  name: 'OrganizationStructure',
  data() {
    return {
      loading: false,
      dialogVisible: false,
      viewDialogVisible: false,
      viewData: {},
      dialogTitle: '',
      isEdit: false,
      tableData: [],
      multipleSelection: [],
      componentError: null, // 新增: 组件错误状态
      queryForm: {
        structureName: '',
        structureType: '',
        controlMode: '',
        status: ''
      },
      form: {
        structureId: '',
        structureCode: '',
        structureName: '',
        structureType: '',
        controlMode: '',
        maxLevels: 5,
        isEnabled: 1,
        description: '',
        remark: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      structureTypeOptions: [
        { label: '单一集团', value: 'single' },
        { label: '多集团', value: 'multi' },
        { label: '分级管理', value: 'hierarchical' },
        { label: '矩阵式', value: 'matrix' },
        { label: '混合式', value: 'hybrid' }
      ],
      controlModeOptions: [
        { label: '集中式', value: 'centralized' },
        { label: '分散式', value: 'decentralized' },
        { label: '混合式', value: 'hybrid' }
      ],
      rules: {
        structureCode: [
          { required: true, message: '请输入体系编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        structureName: [
          { required: true, message: '请输入体系名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        structureType: [
          { required: true, message: '请选择体系类型', trigger: 'change' }
        ],
        controlMode: [
          { required: true, message: '请选择控制模式', trigger: 'change' }
        ]
      }
    }
  },
  errorCaptured(err, vm, info) {
    // Vue 2.5+ 错误捕获钩子
    console.error('========== 组件错误捕获 ==========')
    console.error('❌ 捕获到错误:', err)
    console.error('❌ 错误组件:', vm?.$options?.name || 'Unknown')
    console.error('❌ 错误信息:', info)
    console.error('❌ 错误堆栈:', err.stack)
    console.error('====================================')

    this.componentError = err
    this.$message.error('页面加载出现异常，请刷新重试')

    // 返回 false 阻止错误继续向上传播
    return false
  },
  mounted() {
    try {
      this.loadData()
    } catch (error) {
      this.$message.error('页面初始化失败')
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm
        }

        const response = await getOrganizationStructurePageLocal(
          this.pagination.current,
          this.pagination.size,
          params
        )

        if (response && response.code === 1) {
          const data = response.data

          if (data) {
            this.tableData = data.tlist || data.list || data.records || []
            this.pagination.total = data.totalRecord || data.total || 0
          } else {
            this.tableData = []
            this.pagination.total = 0
          }
        } else {
          const errorMsg = response?.msg || response?.message || '加载数据失败'
          this.$message.error(errorMsg)
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        structureName: '',
        structureType: '',
        controlMode: '',
        status: ''
      }
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增组织体系'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },

    // 编辑
    async handleEdit(row) {
      this.dialogTitle = '编辑组织体系'
      this.isEdit = true
      try {
        const response = await getOrganizationStructureLocal(row.structureId)
        if (response.code === 1) {
          this.form = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取数据异常:', error)
        this.$message.error('获取数据失败')
      }
    },

    // 提交
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        if (this.isEdit) {
          const response = await updateOrganizationStructureLocal(this.form.structureId, this.form)
          if (response.code === 1) {
            this.$message.success('更新成功')
            this.dialogVisible = false
            this.loadData()
          } else {
            this.$message.error(response.message || '更新失败')
          }
        } else {
          const response = await createOrganizationStructureLocal(this.form)
          if (response.code === 1) {
            this.$message.success('创建成功')
            this.dialogVisible = false
            this.loadData()
          } else {
            this.$message.error(response.message || '创建失败')
          }
        }
      } catch (error) {
        console.error('提交异常:', error)
        if (error !== 'cancel') {
          this.$message.error('操作失败')
        }
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        structureId: '',
        structureCode: '',
        structureName: '',
        structureType: '',
        controlMode: '',
        maxLevels: 5,
        isEnabled: 1,
        description: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    // 格式化体系类型
    formatStructureType(type) {
      const option = this.structureTypeOptions.find(item => item.value === type)
      return option ? option.label : type
    },

    // 格式化控制模式
    formatControlMode(mode) {
      const option = this.controlModeOptions.find(item => item.value === mode)
      return option ? option.label : mode
    },

    // 获取体系类型标签类型
    getStructureTypeTagType(type) {
      const typeMap = {
        single: 'primary',
        multi: 'success',
        hierarchical: 'info',
        matrix: 'warning',
        hybrid: 'danger'
      }
      return typeMap[type] || 'primary'
    },

    // 获取控制模式标签类型
    getControlModeTagType(mode) {
      const modeMap = {
        centralized: 'primary',
        decentralized: 'success',
        hybrid: 'warning'
      }
      return modeMap[mode] || 'primary'
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 查看详情
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },

    // 复制
    async handleCopy(row) {
      this.$prompt('请输入新体系名称和编码', '复制组织体系', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入新体系名称'
      }).then(async({ value }) => {
        try {
          const response = await copyOrganizationStructureLocal(
            row.structureId,
            value,
            row.structureCode + '_copy'
          )
          if (response.code === 1) {
            this.$message.success('复制成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '复制失败')
          }
        } catch (error) {
          console.error('复制异常:', error)
          this.$message.error('复制失败')
        }
      }).catch(() => {})
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该组织体系吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteOrganizationStructureLocal(row.structureId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除异常:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条数据吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const ids = this.multipleSelection.map(item => item.structureId)
        const response = await batchDeleteOrganizationStructuresLocal(ids)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除异常:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 批量启用
    async handleBatchEnable() {
      try {
        await this.$confirm(`确定要启用选中的 ${this.multipleSelection.length} 条数据吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })
        const ids = this.multipleSelection.map(item => item.structureId)
        const response = await batchUpdateEnabledLocal(ids, 1)
        if (response.code === 1) {
          this.$message.success('批量启用成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量启用失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量启用异常:', error)
          this.$message.error('批量启用失败')
        }
      }
    },

    // 批量禁用
    async handleBatchDisable() {
      try {
        await this.$confirm(`确定要禁用选中的 ${this.multipleSelection.length} 条数据吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const ids = this.multipleSelection.map(item => item.structureId)
        const response = await batchUpdateEnabledLocal(ids, 0)
        if (response.code === 1) {
          this.$message.success('批量禁用成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量禁用失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量禁用异常:', error)
          this.$message.error('批量禁用失败')
        }
      }
    },

    // 状态改变
    async handleStatusChange(row) {
      const newStatus = row.isEnabled === 1 ? 0 : 1
      try {
        if (newStatus === 1) {
          const response = await enableOrganizationStructureLocal(row.structureId)
          if (response.code === 1) {
            row.isEnabled = 1
            this.$message.success('启用成功')
          } else {
            this.$message.error(response.message || '启用失败')
          }
        } else {
          const response = await disableOrganizationStructureLocal(row.structureId)
          if (response.code === 1) {
            row.isEnabled = 0
            this.$message.success('禁用成功')
          } else {
            this.$message.error(response.message || '禁用失败')
          }
        }
      } catch (error) {
        console.error('状态改变异常:', error)
        this.$message.error('操作失败')
      }
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      const second = String(d.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    },

    // 导出
    handleExport() {
      this.loading = true
      // 构建查询参数
      const params = new URLSearchParams()
      if (this.queryForm.structureName) params.append('structureName', this.queryForm.structureName)
      if (this.queryForm.structureType) params.append('structureType', this.queryForm.structureType)
      if (this.queryForm.controlMode) params.append('controlMode', this.queryForm.controlMode)
      if (this.queryForm.status) params.append('status', this.queryForm.status)
      const queryStr = params.toString()
      const url = '/glkj/accountant/budget/organization-structure/export' + (queryStr ? '?' + queryStr : '')

      request({
        url: url,
        method: 'get',
        responseType: 'blob'
      }).then((response) => {
        // 创建下载链接
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const downloadUrl = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = downloadUrl
        link.download = '组织体系数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(downloadUrl)
        this.loading = false
        this.$message.success('导出成功')
      }).catch(() => {
        this.loading = false
        this.$message.error('导出失败')
      })
    },

    // 导入
    handleImport() {
      // 触发文件选择
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        this.loading = true
        request({
          url: '/glkj/accountant/budget/organization-structure/import',
          method: 'post',
          data: formData,
          headers: { 'Content-Type': 'multipart/form-data' }
        }).then((response) => {
          this.loading = false
          if (response.code === 1) {
            this.$message.success(response.msg || '导入成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '导入失败')
          }
        }).catch(() => {
          this.loading = false
          this.$message.error('导入失败')
        })
      }
      input.click()
    }
  }
}
</script>

<style scoped>
.organization-structure {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.toolbar-card,
.table-card {
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
