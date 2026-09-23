<template>
  <div class="product-info-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-goods"></i>
          产品信息管理
        </h1>
        <p class="page-description">管理产品基础信息、BOM清单、成本结构等产品档案信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增产品
        </el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImport">
          导入产品
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">
          导出产品
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="产品编码" prop="productCode">
          <el-input v-model="queryForm.productCode" placeholder="请输入产品编码" clearable />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="queryForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="产品类型" prop="productType">
          <el-select v-model="queryForm.productType" placeholder="请选择产品类型" clearable>
            <el-option label="原材料" value="1" />
            <el-option label="半成品" value="2" />
            <el-option label="产成品" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态" prop="isEnabled">
          <el-select v-model="queryForm.isEnabled" placeholder="请选择启用状态" clearable>
            <el-option label="启用" value="1" />
            <el-option label="停用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="productList"
        row-key="productId"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="产品编码" prop="productCode" width="120" />
        <el-table-column label="产品名称" prop="productName" min-width="150" />
        <el-table-column label="产品类型" prop="productTypeName" width="100" />
        <el-table-column label="规格型号" prop="specification" width="120" />
        <el-table-column label="计量单位" prop="unit" width="80" />
        <el-table-column label="标准成本" prop="standardCost" width="120" align="right">
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.standardCost) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="当前库存" prop="currentStock" width="100" align="right" />
        <el-table-column label="启用状态" prop="isEnabled" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
              {{ scope.row.isEnabled === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="150" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleBOM(scope.row)">BOM</el-button>
            <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        background
        class="pagination"
        :current-page="queryForm.pageNumber"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="dataForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品编码" prop="productCode">
              <el-input v-model="formData.productCode" placeholder="请输入产品编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="formData.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品类型" prop="productType">
              <el-select v-model="formData.productType" placeholder="请选择产品类型" style="width: 100%">
                <el-option label="原材料" value="1" />
                <el-option label="半成品" value="2" />
                <el-option label="产成品" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="unit">
              <el-input v-model="formData.unit" placeholder="请输入计量单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规格型号" prop="specification">
              <el-input v-model="formData.specification" placeholder="请输入规格型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准成本" prop="standardCost">
              <el-input-number
                v-model="formData.standardCost"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入标准成本"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="启用状态" prop="isEnabled">
              <el-radio-group v-model="formData.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全库存" prop="safetyStock">
              <el-input-number
                v-model="formData.safetyStock"
                :precision="2"
                :min="0"
                style="width: 100%"
                placeholder="请输入安全库存"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="产品描述" prop="productDesc">
          <el-input
            v-model="formData.productDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入产品描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- BOM管理对话框 -->
    <el-dialog
      title="BOM清单管理"
      :visible.sync="bomDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div class="bom-header">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddBOM">
          新增BOM项
        </el-button>
      </div>
      <el-table :data="bomList" border stripe>
        <el-table-column label="物料编码" prop="materialCode" width="120" />
        <el-table-column label="物料名称" prop="materialName" min-width="150" />
        <el-table-column label="用量" prop="quantity" width="100" align="right" />
        <el-table-column label="单位" prop="unit" width="80" />
        <el-table-column label="单价" prop="unitPrice" width="100" align="right">
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.unitPrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="金额" prop="amount" width="120" align="right">
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEditBOM(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDeleteBOM(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bomDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getProductInfoList, createProductInfo, updateProductInfo, deleteProductInfo, getProductBomInfo, exportProductCostDataBlob, importProductCostData, createBom, updateBom, deleteBom } from '@/api/financialSharing/productCost'


export default {
  name: 'ProductInfo',
  components: {
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      productList: [],
      bomList: [],
      selectedRows: [],
      total: 0,
      dialogVisible: false,
      bomDialogVisible: false,
      dialogTitle: '',
      queryForm: {
        pageNumber: 1,
        pageSize: 15,
        productCode: '',
        productName: '',
        productType: '',
        isEnabled: ''
      },
      formData: {
        productId: null,
        productCode: '',
        productName: '',
        productType: '',
        specification: '',
        unit: '',
        standardCost: 0,
        safetyStock: 0,
        isEnabled: 1,
        productDesc: ''
      },
      formRules: {
        productCode: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: '请输入产品名称', trigger: 'blur' }
        ],
        productType: [
          { required: true, message: '请选择产品类型', trigger: 'change' }
        ],
        unit: [
          { required: true, message: '请输入计量单位', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取产品列表
    async getList() {
      this.loading = true
      try {
        const response = await getProductInfoList(this.queryForm)
        if (response.code === 200) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.productList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增产品'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑产品'
      this.dialogVisible = true
      this.formData = { ...row }
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看产品'
      this.dialogVisible = true
      this.formData = { ...row }
    },

    // BOM管理
    async handleBOM(row) {
      this.bomDialogVisible = true
      try {
        const response = await getProductBomInfo(row.productId)
        if (response.code === 200) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.bomList = response.data || []
        }
      } catch (error) {
        this.$message.error('获取BOM清单失败：' + error.message)
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该产品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteProductInfo(row.productId)
          if (response.code === 200) {
            // axios拦截器会将后端的 code=1 转换为 code=200
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmit() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const api = this.formData.productId ? updateProductInfo : createProductInfo
            const response = await api(this.formData)
            if (response.code === 200) {
              // axios拦截器会将后端的 code=1 转换为 code=200
              this.$message.success(this.formData.productId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      this.formData = {
        productId: null,
        productCode: '',
        productName: '',
        productType: '',
        specification: '',
        unit: '',
        standardCost: 0,
        safetyStock: 0,
        isEnabled: 1,
        productDesc: ''
      }
      this.$nextTick(() => {
        this.$refs.dataForm && this.$refs.dataForm.clearValidate()
      })
    },

    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 导入
    handleImport() {
      // 创建隐藏的文件输入
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        try {
          const res = await importProductCostData(formData)
          if (res.code === 200) {
            this.$message.success('导入成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '导入失败')
          }
        } catch (error) {
          this.$message.error('导入失败：' + error.message)
        }
      }
      input.click()
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportProductCostDataBlob(this.queryForm || {})
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '产品成本数据.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    // 新增BOM项
    async handleAddBOM() {
      try {
        await this.$prompt('请输入BOM物料名称', '新增BOM项', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '物料名称'
        }).then(async ({ value }) => {
          if (!value) {
            this.$message.warning('物料名称不能为空')
            return
          }
          const res = await createBom({ materialName: value, productId: this.formData.productId })
          if (res.code === 200) {
            this.$message.success('新增BOM项成功')
            // 刷新BOM列表
            if (this.formData.productId) {
              const bomRes = await getProductBomInfo(this.formData.productId)
              if (bomRes.code === 200) {
                this.bomList = bomRes.data || []
              }
            }
          } else {
            this.$message.error(res.msg || '新增BOM项失败')
          }
        })
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 编辑BOM项
    async handleEditBOM(row) {
      try {
        const { value } = await this.$prompt('请输入新的物料名称', '编辑BOM项', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.materialName || '',
          inputPlaceholder: '物料名称'
        })
        if (!value) {
          this.$message.warning('物料名称不能为空')
          return
        }
        const res = await updateBom({ ...row, materialName: value })
        if (res.code === 200) {
          this.$message.success('编辑BOM项成功')
          if (this.formData.productId) {
            const bomRes = await getProductBomInfo(this.formData.productId)
            if (bomRes.code === 200) {
              this.bomList = bomRes.data || []
            }
          }
        } else {
          this.$message.error(res.msg || '编辑BOM项失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 删除BOM项
    async handleDeleteBOM(row) {
      try {
        await this.$confirm('确认删除该BOM项吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await deleteBom(row.bomId)
        if (res.code === 200) {
          this.$message.success('删除BOM项成功')
          if (this.formData.productId) {
            const bomRes = await getProductBomInfo(this.formData.productId)
            if (bomRes.code === 200) {
              this.bomList = bomRes.data || []
            }
          }
        } else {
          this.$message.error(res.msg || '删除BOM项失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 格式化金额
    formatAmount(amount) {
      if (amount == null || amount === '') return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.product-info-container {
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
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.bom-header {
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}
</style>
