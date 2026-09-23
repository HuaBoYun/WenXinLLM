<template>
  <el-dialog
    title="录入盘点结果"
    :visible.sync="visible"
    width="80%"
    :before-close="handleClose"
    class="check-result-dialog"
  >
    <div class="dialog-content">
      <!-- 批量操作工具栏 -->
      <div class="toolbar">
        <el-button type="primary" size="small" @click="handleBatchInput">批量录入</el-button>
        <el-button type="success" size="small" @click="handleImportResult">导入结果</el-button>
        <el-button type="warning" size="small" @click="handleExportTemplate">导出模板</el-button>
        <el-divider direction="vertical" />
        <span class="toolbar-text">
          总计：{{ totalCount }} 项，已录入：{{ inputCount }} 项，待录入：{{ pendingCount }} 项
        </span>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-bar">
        <el-form :inline="true" size="small">
          <el-form-item label="录入状态">
            <el-select v-model="filter.inputStatus" placeholder="全部" style="width: 120px">
              <el-option label="全部" value="" />
              <el-option label="未录入" value="PENDING" />
              <el-option label="已录入" value="INPUTTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="存货名称">
            <el-input v-model="filter.inventoryName" placeholder="请输入存货名称" style="width: 180px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadResultData">查询</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 盘点结果录入表格 -->
      <div class="table-container">
        <el-table
          :data="resultData"
          v-loading="loading"
          border
          @selection-change="handleSelectionChange"
          :row-class-name="getRowClassName"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="inventoryCode" label="存货编码" width="120" />
          <el-table-column prop="inventoryName" label="存货名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="specification" label="规格型号" width="120" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="right" />
          <el-table-column label="实盘数量" width="150" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.actualQuantity"
                :precision="2"
                :min="0"
                size="small"
                style="width: 120px"
                @change="handleQuantityChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="varianceQuantity" label="差异数量" width="100" align="right">
            <template slot-scope="scope">
              <span :class="getVarianceClass(scope.row.varianceQuantity)">
                {{ scope.row.varianceQuantity || 0 }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="unitCost" label="单位成本" width="100" align="right">
            <template slot-scope="scope">
              <span class="amount">{{ formatAmount(scope.row.unitCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="['amount', getVarianceClass(scope.row.varianceAmount)]">
                {{ formatAmount(scope.row.varianceAmount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="备注" width="150">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.remark"
                placeholder="请输入备注"
                size="small"
                maxlength="50"
              />
            </template>
          </el-table-column>
          <el-table-column prop="inputStatus" label="状态" width="80">
            <template slot-scope="scope">
              <el-tag :type="getInputStatusTagType(scope.row.inputStatus)" size="small">
                {{ getInputStatusText(scope.row.inputStatus) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

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
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">保存结果</el-button>
      <el-button type="success" @click="handleSubmit" :loading="submitting">提交盘点</el-button>
    </div>

    <!-- 批量录入对话框 -->
    <el-dialog
      title="批量录入"
      :visible.sync="batchInputVisible"
      width="400px"
      append-to-body
    >
      <el-form :model="batchForm" label-width="100px">
        <el-form-item label="录入方式">
          <el-radio-group v-model="batchForm.inputType">
            <el-radio label="SAME">相同数量</el-radio>
            <el-radio label="RATIO">按比例</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="batchForm.inputType === 'SAME'" label="实盘数量">
          <el-input-number v-model="batchForm.quantity" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item v-if="batchForm.inputType === 'RATIO'" label="盘点比例">
          <el-input-number v-model="batchForm.ratio" :precision="2" :min="0" :max="200" />
          <span style="margin-left: 8px;">%</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batchInputVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchInputConfirm">确定</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getInventoryCheckResults, submitInventoryCheckResult } from '@/api/financialSharing/inventory'

export default {
  name: 'CheckResultDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    checkData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      saving: false,
      submitting: false,
      resultData: [],
      selectedRows: [],
      filter: {
        inputStatus: '',
        inventoryName: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      batchInputVisible: false,
      batchForm: {
        inputType: 'SAME',
        quantity: 0,
        ratio: 100
      }
    }
  },
  computed: {
    totalCount() {
      return this.pagination.total
    },
    inputCount() {
      return this.resultData.filter(item => item.inputStatus === 'INPUTTED').length
    },
    pendingCount() {
      return this.totalCount - this.inputCount
    }
  },
  watch: {
    visible(val) {
      if (val && this.checkData.checkId) {
        this.loadResultData()
      }
    }
  },
  methods: {
    async loadResultData() {
      this.loading = true
      try {
        const params = {
          checkId: this.checkData.checkId,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.filter
        }
        const response = await getInventoryCheckResults(this.checkData.checkId, params)
        if (response.code === 1) {
          this.resultData = (response.data.tlist || []).map(item => ({
            ...item,
            actualQuantity: item.actualQuantity !== null ? item.actualQuantity : item.bookQuantity,
            varianceQuantity: item.varianceQuantity || 0,
            varianceAmount: item.varianceAmount || 0,
            remark: item.remark || '',
            inputStatus: item.actualQuantity !== null ? 'INPUTTED' : 'PENDING'
          }))
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '加载数据失败')
        }
      } catch (error) {
        this.$message.error('加载数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    handleQuantityChange(row) {
      if (row.actualQuantity !== null) {
        row.varianceQuantity = row.actualQuantity - row.bookQuantity
        row.varianceAmount = row.varianceQuantity * row.unitCost
        row.inputStatus = 'INPUTTED'
      } else {
        row.varianceQuantity = 0
        row.varianceAmount = 0
        row.inputStatus = 'PENDING'
      }
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleBatchInput() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要批量录入的存货')
        return
      }
      this.batchInputVisible = true
    },

    handleBatchInputConfirm() {
      this.selectedRows.forEach(row => {
        if (this.batchForm.inputType === 'SAME') {
          row.actualQuantity = this.batchForm.quantity
        } else {
          row.actualQuantity = Math.round(row.bookQuantity * this.batchForm.ratio / 100 * 100) / 100
        }
        this.handleQuantityChange(row)
      })
      this.batchInputVisible = false
      this.$message.success('批量录入成功')
    },

    handleImportResult() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 导入处理中...`)
        this.loadResultData()
      }
      input.click()
    },

    handleExportTemplate() {
      try {
        const headers = ['存货编号', '存货名称', '规格型号', '单位', '账面数量', '实盘数量', '差异说明']
        const blob = new Blob(['\ufeff' + headers.join(',') + '\n'], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '盘点结果导入模板.csv'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('模板下载成功')
      } catch (e) {
        this.$message.error('模板下载失败')
      }
    },

    resetFilter() {
      this.filter = {
        inputStatus: '',
        inventoryName: ''
      }
      this.loadResultData()
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadResultData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadResultData()
    },

    async handleSave() {
      this.saving = true
      try {
        const resultList = this.resultData.map(item => ({
          inventoryId: item.inventoryId,
          actualQuantity: item.actualQuantity,
          varianceQuantity: item.varianceQuantity,
          varianceAmount: item.varianceAmount,
          remark: item.remark
        }))

        const response = await submitInventoryCheckResult(this.checkData.checkId, {
          resultList: resultList,
          saveOnly: true
        })

        if (response.code === 1) {
          this.$message.success('保存成功')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saving = false
      }
    },

    async handleSubmit() {
      const pendingItems = this.resultData.filter(item => item.inputStatus === 'PENDING')
      if (pendingItems.length > 0) {
        this.$message.warning(`还有 ${pendingItems.length} 项未录入，请先完成录入`)
        return
      }

      this.$confirm('确认提交盘点结果？提交后将无法修改。', '确认提交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        this.submitting = true
        try {
          const resultList = this.resultData.map(item => ({
            inventoryId: item.inventoryId,
            actualQuantity: item.actualQuantity,
            varianceQuantity: item.varianceQuantity,
            varianceAmount: item.varianceAmount,
            remark: item.remark
          }))

          const response = await submitInventoryCheckResult(this.checkData.checkId, {
            resultList: resultList,
            saveOnly: false
          })

          if (response.code === 1) {
            this.$message.success('提交成功')
            this.$emit('submit', resultList)
            this.handleClose()
          } else {
            this.$message.error(response.msg || '提交失败')
          }
        } catch (error) {
          this.$message.error('提交失败：' + error.message)
        } finally {
          this.submitting = false
        }
      }).catch(() => {
        // 用户取消
      })
    },

    handleClose() {
      this.$emit('update:visible', false)
    },

    getRowClassName({ row }) {
      if (row.inputStatus === 'PENDING') {
        return 'pending-row'
      }
      return ''
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getVarianceClass(variance) {
      if (variance > 0) return 'surplus'
      if (variance < 0) return 'shortage'
      return ''
    },

    getInputStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'INPUTTED': 'success'
      }
      return typeMap[status] || 'default'
    },

    getInputStatusText(status) {
      const textMap = {
        'PENDING': '待录入',
        'INPUTTED': '已录入'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.check-result-dialog {
  .dialog-content {
    .toolbar {
      margin-bottom: 15px;
      display: flex;
      align-items: center;

      .toolbar-text {
        color: #606266;
        font-size: 14px;
      }
    }

    .filter-bar {
      margin-bottom: 15px;
      padding: 15px;
      background: #f5f7fa;
      border-radius: 4px;
    }

    .table-container {
      .pagination-container {
        margin-top: 20px;
        text-align: right;
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }
}

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.surplus {
  color: #67c23a;
  font-weight: 600;
}

.shortage {
  color: #f56c6c;
  font-weight: 600;
}

:deep(.pending-row) {
  background-color: #fdf6ec;
}
</style>
