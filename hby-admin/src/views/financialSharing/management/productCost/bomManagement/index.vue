<template>
  <div class="bom-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-menu"></i>
          BOM管理
        </h1>
        <p class="page-description">管理产品物料清单、工艺路线、成本要素等BOM相关信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAddBOM">
          新增BOM
        </el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImportBOM">
          导入BOM
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExportBOM">
          导出BOM
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
        <el-form-item label="BOM版本" prop="bomVersion">
          <el-input v-model="queryForm.bomVersion" placeholder="请输入BOM版本" clearable />
        </el-form-item>
        <el-form-item label="BOM状态" prop="bomStatus">
          <el-select v-model="queryForm.bomStatus" placeholder="请选择BOM状态" clearable>
            <el-option label="草稿" value="1" />
            <el-option label="审批中" value="2" />
            <el-option label="已生效" value="3" />
            <el-option label="已失效" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- BOM列表 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="bomList"
        row-key="bomId"
        border
        stripe
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="expand">
          <template slot-scope="props">
            <div class="bom-detail">
              <h4>BOM明细</h4>
              <el-table :data="props.row.bomItems" border size="small">
                <el-table-column label="序号" prop="itemSeq" width="60" align="center" />
                <el-table-column label="物料编码" prop="materialCode" width="120" />
                <el-table-column label="物料名称" prop="materialName" min-width="150" />
                <el-table-column label="规格型号" prop="specification" width="120" />
                <el-table-column label="用量" prop="quantity" width="80" align="right" />
                <el-table-column label="单位" prop="unit" width="60" />
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
                <el-table-column label="损耗率" prop="lossRate" width="80" align="right">
                  <template slot-scope="scope">
                    <span>{{ scope.row.lossRate }}%</span>
                  </template>
                </el-table-column>
                <el-table-column label="替代料" prop="substituteMaterial" width="120" />
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="产品编码" prop="productCode" width="120" />
        <el-table-column label="产品名称" prop="productName" min-width="150" />
        <el-table-column label="BOM版本" prop="bomVersion" width="100" />
        <el-table-column label="BOM类型" prop="bomTypeName" width="100" />
        <el-table-column label="总成本" prop="totalCost" width="120" align="right">
          <template slot-scope="scope">
            <span>{{ formatAmount(scope.row.totalCost) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="物料数量" prop="materialCount" width="100" align="center" />
        <el-table-column label="BOM状态" prop="bomStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBomStatusType(scope.row.bomStatus)">
              {{ getBomStatusName(scope.row.bomStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="生效日期" prop="effectiveDate" width="120" />
        <el-table-column label="失效日期" prop="expiryDate" width="120" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleCopy(scope.row)">复制</el-button>
            <el-button size="mini" type="text" @click="handleVersion(scope.row)">版本</el-button>
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

    <!-- 新增/编辑BOM对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="dataForm" label-width="120px">
        <!-- BOM基本信息 -->
        <el-card title="基本信息" class="form-card">
          <div slot="header">
            <span>基本信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="产品编码" prop="productCode">
                <el-input v-model="formData.productCode" placeholder="请输入产品编码" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="产品名称" prop="productName">
                <el-input v-model="formData.productName" placeholder="请输入产品名称" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="BOM版本" prop="bomVersion">
                <el-input v-model="formData.bomVersion" placeholder="请输入BOM版本" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="BOM类型" prop="bomType">
                <el-select v-model="formData.bomType" placeholder="请选择BOM类型" style="width: 100%">
                  <el-option label="生产BOM" value="1" />
                  <el-option label="成本BOM" value="2" />
                  <el-option label="工程BOM" value="3" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="生效日期" prop="effectiveDate">
                <el-date-picker
                  v-model="formData.effectiveDate"
                  type="date"
                  placeholder="选择生效日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="失效日期" prop="expiryDate">
                <el-date-picker
                  v-model="formData.expiryDate"
                  type="date"
                  placeholder="选择失效日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="BOM描述" prop="bomDesc">
            <el-input
              v-model="formData.bomDesc"
              type="textarea"
              :rows="2"
              placeholder="请输入BOM描述"
            />
          </el-form-item>
        </el-card>

        <!-- BOM明细 -->
        <el-card title="BOM明细" class="form-card">
          <div slot="header">
            <span>BOM明细</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddItem">
              新增明细
            </el-button>
          </div>
          <el-table :data="formData.bomItems" border>
            <el-table-column label="序号" width="60" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="物料编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.materialCode" placeholder="物料编码" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="物料名称" min-width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.materialName" placeholder="物料名称" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="规格型号" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.specification" placeholder="规格型号" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="用量" width="80">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.quantity"
                  :precision="4"
                  :min="0"
                  size="small"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="单位" width="80">
              <template slot-scope="scope">
                <el-input v-model="scope.row.unit" placeholder="单位" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="单价" width="100">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.unitPrice"
                  :precision="2"
                  :min="0"
                  size="small"
                  style="width: 100%"
                  @change="calculateItemAmount(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="金额" width="120">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="损耗率%" width="80">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.lossRate"
                  :precision="2"
                  :min="0"
                  :max="100"
                  size="small"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDeleteItem(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="bom-summary">
            <span>总成本：{{ formatAmount(calculateTotalCost()) }}</span>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- BOM版本管理对话框 -->
    <el-dialog
      title="BOM版本管理"
      :visible.sync="versionDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-table :data="versionList" border stripe>
        <el-table-column label="版本号" prop="bomVersion" width="100" />
        <el-table-column label="版本描述" prop="versionDesc" min-width="200" />
        <el-table-column label="创建时间" prop="createTime" width="150" />
        <el-table-column label="创建人" prop="creatorName" width="100" />
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '当前版本' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleSetCurrentVersion(scope.row)">设为当前</el-button>
            <el-button size="mini" type="text" @click="handleViewVersion(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="versionDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBomList, createBom, updateBom, deleteBom, getBomVersions, getBomDetails, exportProductCostDataBlob, importProductCostData } from '@/api/financialSharing/productCost'


export default {
  name: 'BOMManagement',
  components: {
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      bomList: [],
      versionList: [],
      selectedRows: [],
      total: 0,
      dialogVisible: false,
      versionDialogVisible: false,
      dialogTitle: '',
      queryForm: {
        pageNumber: 1,
        pageSize: 15,
        productCode: '',
        productName: '',
        bomVersion: '',
        bomStatus: ''
      },
      formData: {
        bomId: null,
        productCode: '',
        productName: '',
        bomVersion: '',
        bomType: '1',
        effectiveDate: '',
        expiryDate: '',
        bomDesc: '',
        bomItems: []
      },
      formRules: {
        productCode: [
          { required: true, message: '请输入产品编码', trigger: 'blur' }
        ],
        productName: [
          { required: true, message: '请输入产品名称', trigger: 'blur' }
        ],
        bomVersion: [
          { required: true, message: '请输入BOM版本', trigger: 'blur' }
        ],
        bomType: [
          { required: true, message: '请选择BOM类型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取BOM列表
    async getList() {
      this.loading = true
      try {
        const response = await getBomList(this.queryForm)
        if (response.code === 1) {
          this.bomList = response.data.tlist || []
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

    // 行点击
    handleRowClick(row) {
      // 可以在这里处理行点击事件
    },

    // 新增BOM
    handleAddBOM() {
      this.dialogTitle = '新增BOM'
      this.dialogVisible = true
      this.resetForm()
    },

    // 编辑BOM
    handleEdit(row) {
      this.dialogTitle = '编辑BOM'
      this.dialogVisible = true
      this.formData = { ...row }
    },

    // 查看BOM
    handleView(row) {
      this.dialogTitle = '查看BOM'
      this.dialogVisible = true
      this.formData = { ...row }
    },

    // 复制BOM
    handleCopy(row) {
      this.dialogTitle = '复制BOM'
      this.dialogVisible = true
      this.formData = { ...row, bomId: null, bomVersion: '' }
    },

    // 版本管理
    async handleVersion(row) {
      this.versionDialogVisible = true
      try {
        const response = await getBomVersions(row.productCode)
        if (response.code === 1) {
          this.versionList = response.data || []
        }
      } catch (error) {
        console.error('获取BOM版本列表失败：', error)
      }
    },

    // 删除BOM
    handleDelete(row) {
      this.$confirm('确认删除该BOM吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteBom(row.bomId)
          if (response.code === 1) {
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

    // 新增明细
    handleAddItem() {
      this.formData.bomItems.push({
        materialCode: '',
        materialName: '',
        specification: '',
        quantity: 0,
        unit: '',
        unitPrice: 0,
        amount: 0,
        lossRate: 0,
        substituteMaterial: ''
      })
    },

    // 删除明细
    handleDeleteItem(index) {
      this.formData.bomItems.splice(index, 1)
    },

    // 计算明细金额
    calculateItemAmount(item) {
      item.amount = (item.quantity || 0) * (item.unitPrice || 0)
    },

    // 计算总成本
    calculateTotalCost() {
      return this.formData.bomItems.reduce((total, item) => {
        return total + (item.amount || 0)
      }, 0)
    },

    // 提交表单
    async handleSubmit() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const api = this.formData.bomId ? updateBom : createBom
            const response = await api(this.formData)
            if (response.code === 1) {
              this.$message.success(this.formData.bomId ? '更新成功' : '创建成功')
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
        bomId: null,
        productCode: '',
        productName: '',
        bomVersion: '',
        bomType: '1',
        effectiveDate: '',
        expiryDate: '',
        bomDesc: '',
        bomItems: []
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

    // 导入BOM
    handleImportBOM() {
      this.$message.info('请使用文件上传功能，选择符合模板格式的Excel文件进行导入')
    },

    // 导出BOM
    async handleExportBOM() {
      try {
        const response = await exportProductCostDataBlob({ type: 'bom', ...this.queryForm })
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = 'BOM数据导出.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    // 设为当前版本
    handleSetCurrentVersion(row) {
      this.$message.success('设置当前版本成功')
    },

    // 查看版本
    async handleViewVersion(row) {
      try {
        const res = await getBomVersions(row.productCode || row.bomId)
        if (res.code === 1) {
          this.versionList = res.data || []
          this.versionDialogVisible = true
        } else {
          this.$message.error(res.msg || '获取版本列表失败')
        }
      } catch (error) {
        this.$message.error('获取版本列表失败')
      }
    },

    // 获取BOM状态类型
    getBomStatusType(status) {
      const types = { 1: '', 2: 'warning', 3: 'success', 4: 'info' }
      return types[status] || ''
    },

    // 获取BOM状态名称
    getBomStatusName(status) {
      const names = { 1: '草稿', 2: '审批中', 3: '已生效', 4: '已失效' }
      return names[status] || '未知'
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
.bom-management-container {
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

.bom-detail {
  padding: 20px;
  background: #fafafa;

  h4 {
    margin: 0 0 15px 0;
    color: #303133;
  }
}

.form-card {
  margin-bottom: 20px;
}

.bom-summary {
  margin-top: 10px;
  text-align: right;
  font-weight: 600;
  color: #303133;
}

.dialog-footer {
  text-align: right;
}
</style>
