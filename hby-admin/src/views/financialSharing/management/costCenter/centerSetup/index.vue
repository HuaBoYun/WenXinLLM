<template>
  <div class="center-setup-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="成本中心编码" prop="centerCode">
          <el-input
            v-model="searchForm.centerCode"
            placeholder="请输入成本中心编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="成本中心名称" prop="centerName">
          <el-input
            v-model="searchForm.centerName"
            placeholder="请输入成本中心名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="中心类型" prop="centerType">
          <el-select
            v-model="searchForm.centerType"
            placeholder="请选择中心类型"
            clearable
            style="width: 150px"
          >
            <el-option label="成本中心" :value="1" />
            <el-option label="利润中心" :value="2" />
            <el-option label="投资中心" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增成本中心</el-button>
      <el-button type="success" @click="handleBatchEnable" :disabled="!multipleSelection.length">
        批量启用
      </el-button>
      <el-button type="warning" @click="handleBatchDisable" :disabled="!multipleSelection.length">
        批量停用
      </el-button>
      <el-button type="info" @click="handleExport">导出</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="centerCode" label="成本中心编码" width="150" />
        <el-table-column prop="centerName" label="成本中心名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="centerTypeName" label="中心类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCenterTypeTag(scope.row.centerType)">
              {{ scope.row.centerTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parentCenterName" label="上级中心" width="150" show-overflow-tooltip />
        <el-table-column prop="managerName" label="负责人" width="120" />
        <el-table-column prop="allocationMethodName" label="分摊方法" width="120" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
              {{ scope.row.isEnabled === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="primary" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成本中心编码" prop="centerCode">
              <el-input v-model="formData.centerCode" placeholder="请输入成本中心编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成本中心名称" prop="centerName">
              <el-input v-model="formData.centerName" placeholder="请输入成本中心名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="中心类型" prop="centerType">
              <el-select v-model="formData.centerType" placeholder="请选择中心类型" style="width: 100%">
                <el-option label="成本中心" :value="1" />
                <el-option label="利润中心" :value="2" />
                <el-option label="投资中心" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级中心名称" prop="parentCenterName">
              <el-input v-model="formData.parentCenterName" placeholder="请输入上级中心名称" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人名称" prop="managerName">
              <el-input v-model="formData.managerName" placeholder="请输入负责人名称" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分摊方法" prop="costAllocationMethod">
              <el-select v-model="formData.costAllocationMethod" placeholder="请选择分摊方法" style="width: 100%">
                <el-option label="直接分摊" :value="1" />
                <el-option label="阶梯分摊" :value="2" />
                <el-option label="比例分摊" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-radio-group v-model="formData.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="查看成本中心详情"
      :visible.sync="viewDialogVisible"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成本中心编码">
          {{ viewData.centerCode || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="成本中心名称">
          {{ viewData.centerName || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="中心类型">
          <el-tag :type="getCenterTypeTag(viewData.centerType)">
            {{ viewData.centerTypeName || '无' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上级中心">
          {{ viewData.parentCenterName || viewData.parentCenterId || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="中心层级">
          {{ viewData.centerLevel || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="是否叶子节点">
          <el-tag :type="viewData.isLeaf === 1 ? 'success' : 'info'">
            {{ viewData.isLeaf === 1 ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责人">
          {{ viewData.managerName || viewData.managerId || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="分摊方法">
          <el-tag type="warning">
            {{ viewData.allocationMethodName || '无' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.isEnabled === 1 ? 'success' : 'danger'">
            {{ viewData.isEnabled === 1 ? '启用' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账簿ID">
          {{ viewData.bookId || '无' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostCenterPage,
  saveOrUpdateCostCenter,
  deleteCostCenter,
  batchDeleteCostCenter,
  batchUpdateCostCenterStatus,
  getCostCenterOptions,
  getCostCenterById,
  exportCostCenterList
} from '@/api/financialSharing/costCenter'

export default {
  name: 'CostCenterSetup',
  data() {
    return {
      loading: false,
      searchForm: {
        centerCode: '',
        centerName: '',
        centerType: '',
        isEnabled: ''
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      formData: {
        centerId: null,
        centerCode: '',
        centerName: '',
        centerType: '',
        parentCenterId: '',
        parentCenterName: '',
        managerId: '',
        managerName: '',
        costAllocationMethod: 1,
        isEnabled: 1
      },
      // 查看对话框数据
      viewDialogVisible: false,
      viewData: {},
      formRules: {
        centerCode: [
          { required: true, message: '请输入成本中心编码', trigger: 'blur' }
        ],
        centerName: [
          { required: true, message: '请输入成本中心名称', trigger: 'blur' }
        ],
        centerType: [
          { required: true, message: '请选择中心类型', trigger: 'change' }
        ]
      },
      parentCenterOptions: [],
      managerOptions: []
    }
  },
  mounted() {
    this.loadData()
    this.loadParentCenterOptions()

    // 检查是否需要自动打开新增对话框
    if (this.$route.query.action === 'add') {
      this.$nextTick(() => {
        this.handleAdd()
      })
    }
  },
  methods: {
    // 获取用户信息中的bookId和tenantId
    getUserContext() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        console.log('用户信息:', userInfo) // 调试日志

        // 尝试多种可能的bookId路径
        let bookId = userInfo.currentBook?.bookId ||
                     userInfo.bookId ||
                     userInfo.bookInfo?.bookId ||
                     1 // 默认值

        // 尝试多种可能的tenantId路径
        let tenantId = userInfo.currentOrg?.orgid ||
                       userInfo.tenantId ||
                       userInfo.orgId ||
                       1000 // 默认值

        console.log('获取的bookId:', bookId, 'tenantId:', tenantId) // 调试日志
        return { bookId, tenantId }
      } catch (error) {
        console.error('获取用户上下文失败:', error)
        // 返回默认值而不是null
        return { bookId: 1, tenantId: 1000 }
      }
    },
    async loadData() {
      this.loading = true
      try {
        const { bookId, tenantId } = this.getUserContext()

        const response = await getCostCenterPage({
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          bookId,
          tenantId,
          ...this.searchForm
        })

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadParentCenterOptions() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        const response = await getCostCenterOptions({
          isEnabled: 1,
          bookId,
          tenantId
        })
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.parentCenterOptions = response.data || []
        }
      } catch (error) {
        console.error('加载上级中心选项失败:', error)
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增成本中心'
      this.formData = {
        centerId: null,
        centerCode: '',
        centerName: '',
        centerType: '',
        parentCenterId: '',
        managerId: '',
        costAllocationMethod: 1,
        isEnabled: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑成本中心'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        this.loading = true
        const response = await getCostCenterById(row.centerId)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.viewData = response.data || {}
          this.viewDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取成本中心详情失败:', error)
        this.$message.error('获取详情失败')
      } finally {
        this.loading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该成本中心吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 验证ID的有效性
        const centerId = this.validateAndConvertId(row.centerId, row.centerName)
        if (centerId === null) {
          return
        }

        console.log('删除成本中心 - ID:', centerId, '名称:', row.centerName)

        const response = await deleteCostCenter(centerId)
        // axios拦截器会将后端的 code=1 转换为 code=200
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          // 显示后端返回的具体错误信息
          const errorMsg = response.msg || '删除失败'
          console.error('删除失败 - 后端错误:', errorMsg)
          this.$message.error(errorMsg)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败 - 前端错误:', error)
          // 显示具体的错误信息
          const errorMsg = error.response?.data?.msg || error.message || '删除失败，请重试'
          this.$message.error(errorMsg)
        }
      }
    },
    async handleBatchEnable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要启用的成本中心')
        return
      }

      try {
        const centerIds = this.multipleSelection.map(item => item.centerId)
        const response = await batchUpdateCostCenterStatus(centerIds, 1)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('批量启用成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量启用失败')
        }
      } catch (error) {
        console.error('批量启用失败:', error)
        this.$message.error('批量启用失败')
      }
    },
    async handleBatchDisable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要停用的成本中心')
        return
      }

      try {
        const centerIds = this.multipleSelection.map(item => item.centerId)
        const response = await batchUpdateCostCenterStatus(centerIds, 0)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('批量停用成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量停用失败')
        }
      } catch (error) {
        console.error('批量停用失败:', error)
        this.$message.error('批量停用失败')
      }
    },
    async handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的成本中心')
        return
      }

      try {
        await this.$confirm('确认批量删除选中的成本中心吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 验证所有选中的ID
        const validIds = []
        const invalidItems = []

        for (const item of this.multipleSelection) {
          const validatedId = this.validateAndConvertId(item.centerId, item.centerName)
          if (validatedId !== null) {
            validIds.push(validatedId)
          } else {
            invalidItems.push(item.centerName || item.centerId)
          }
        }

        // 如果有无效的ID，提示用户
        if (invalidItems.length > 0) {
          this.$message.error(`以下成本中心ID无效，无法删除：${invalidItems.join(', ')}`)
          return
        }

        console.log('批量删除成本中心 - IDs:', validIds)

        const response = await batchDeleteCostCenter(validIds)
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          // 显示后端返回的具体错误信息
          const errorMsg = response.msg || '批量删除失败'
          console.error('批量删除失败 - 后端错误:', errorMsg)
          this.$message.error(errorMsg)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败 - 前端错误:', error)
          // 显示具体的错误信息
          const errorMsg = error.response?.data?.msg || error.message || '批量删除失败，请重试'
          this.$message.error(errorMsg)
        }
      }
    },
    async handleExport() {
      try {
        await this.$confirm('确认导出成本中心数据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })

        this.loading = true
        const { bookId, tenantId } = this.getUserContext()

        // 构建导出参数
        const exportParams = {
          bookId,
          tenantId
        }

        const response = await exportCostCenterList(exportParams)
        if (response.code === 1) {
          // API 已经触发了浏览器下载，这里只做提示
          // 注意：API resolve 的是扁平结构 { code, message, fileName }，没有 .data 属性
          this.$message.success(response.message || '导出成功')
          if (response.fileName) {
            console.log('导出文件:', response.fileName)
          }
        } else {
          this.$message.error(response.message || response.msg || '导出失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('导出失败:', error)
          // error 可能是 API reject 的 { code, message }，把真实错误信息透出来
          const errMsg = (error && (error.message || error.msg)) || '导出失败'
          this.$message.error(errMsg)
        }
      } finally {
        this.loading = false
      }
    },
    // 验证和转换ID的方法 - 统一使用字符串处理
    validateAndConvertId(id, name) {
      try {
        console.log('原始ID值:', id, typeof id)

        // 检查ID是否存在
        if (id === null || id === undefined || id === '') {
          this.$message.error('成本中心ID为空')
          return null
        }

        // 转换为字符串（统一处理，避免精度丢失）
        const idStr = String(id).trim()

        // 基本格式验证：检查是否为纯数字
        if (!/^\d+$/.test(idStr)) {
          this.$message.error(`成本中心${name || ''}的ID格式无效: ${idStr}`)
          return null
        }

        // 检查是否为负数
        if (idStr.startsWith('-')) {
          this.$message.error(`成本中心${name || ''}的ID必须为正数: ${idStr}`)
          return null
        }

        console.log('验证通过的ID字符串:', idStr)
        // 统一返回字符串，避免JavaScript精度丢失问题
        return idStr

      } catch (error) {
        console.error('ID验证失败:', error)
        this.$message.error(`成本中心${name || ''}的ID验证失败`)
        return null
      }
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const { bookId, tenantId } = this.getUserContext()
            const submitData = {
              ...this.formData,
              bookId,
              tenantId
            }
            const response = await saveOrUpdateCostCenter(submitData)
            if (response.code === 1) {
              // axios拦截器会将后端的 code=1 转换为 code=200
              this.$message.success('保存成功')
              this.dialogVisible = false
              // 重置到第一页并刷新数据
              this.pagination.currentPage = 1
              this.loadData()
              // 刷新父级中心选项列表
              this.loadParentCenterOptions()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          }
        }
      })
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    },
    getCenterTypeTag(type) {
      const tagMap = {
        1: 'primary',
        2: 'success', 
        3: 'warning'
      }
      return tagMap[type] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.center-setup-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;

  .el-button {
    margin-right: 10px;
  }
}

.table-container {
  background: white;
  border-radius: 4px;

  .action-buttons {
    display: flex;
    gap: 8px;
    align-items: center;

    .el-button {
      margin: 0;
      padding: 7px 15px;
    }
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
