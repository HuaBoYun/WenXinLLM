<template>
  <div class="create-adjustment-container">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>{{ isEdit ? '编辑调整单' : '新建调整单' }}</span>
        <el-button type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>

      <el-form ref="adjustmentForm" :model="adjustmentForm" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算模型" prop="modelId">
              <el-select
                v-model="adjustmentForm.modelId"
                placeholder="请选择预算模型"
                filterable
                style="width: 100%"
                @change="handleModelChange"
              >
                <el-option
                  v-for="item in modelList"
                  :key="item.modelId"
                  :label="item.modelName"
                  :value="item.modelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算期间" prop="period">
              <el-input v-model="adjustmentForm.period" placeholder="请输入预算期间，如：2026" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算版本" prop="version">
              <el-input v-model="adjustmentForm.version" placeholder="请输入预算版本，如：V1.0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整类型" prop="adjustmentType">
              <el-select v-model="adjustmentForm.adjustmentType" placeholder="请选择调整类型" style="width: 100%">
                <el-option label="整版调整" value="FULL" />
                <el-option label="零星调整" value="PARTIAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="调整原因" prop="adjustmentReason">
          <el-input v-model="adjustmentForm.adjustmentReason" placeholder="请输入调整原因" />
        </el-form-item>

        <el-form-item label="调整说明">
          <el-input
            v-model="adjustmentForm.adjustmentDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入调整说明"
          />
        </el-form-item>

        <el-form-item label="调整明细" prop="detailList">
          <div class="detail-toolbar">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddDetail">
              添加明细
            </el-button>
            <el-button type="success" size="small" icon="el-icon-download" @click="handleImportFromBudget">
              从预算数据导入
            </el-button>
            <el-button
              type="danger"
              size="small"
              icon="el-icon-delete"
              :disabled="selectedDetails.length === 0"
              @click="handleBatchDeleteDetail"
            >
              批量删除
            </el-button>
          </div>

          <el-table
            :data="adjustmentForm.detailList"
            border
            stripe
            style="margin-top: 10px"
            max-height="400"
            @selection-change="handleDetailSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column label="科目编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.subjectCode" size="small" placeholder="科目编码" />
              </template>
            </el-table-column>
            <el-table-column label="科目名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.subjectName" size="small" placeholder="科目名称" />
              </template>
            </el-table-column>
            <el-table-column label="组织编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.organizationCode" size="small" placeholder="组织编码" />
              </template>
            </el-table-column>
            <el-table-column label="组织名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.organizationName" size="small" placeholder="组织名称" />
              </template>
            </el-table-column>
            <el-table-column label="维度1编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.dimension1Code" size="small" placeholder="维度1编码" />
              </template>
            </el-table-column>
            <el-table-column label="维度1名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.dimension1Name" size="small" placeholder="维度1名称" />
              </template>
            </el-table-column>
            <el-table-column label="原值" width="150" align="right">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.originalValue"
                  size="small"
                  :precision="2"
                  :controls="false"
                  style="width: 100%"
                  @change="handleValueChange(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="调整值" width="150" align="right">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.adjustmentValue"
                  size="small"
                  :precision="2"
                  :controls="false"
                  style="width: 100%"
                  @change="handleValueChange(scope.row)"
                />
              </template>
            </el-table-column>
            <el-table-column label="调整后值" width="150" align="right">
              <template slot-scope="scope">
                <span :style="{ fontWeight: 'bold', color: '#409EFF' }">
                  {{ formatNumber(scope.row.adjustedValue) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="调整原因" min-width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.adjustmentReason" size="small" placeholder="调整原因" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-delete"
                  @click="handleDeleteDetail(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saveLoading" @click="handleSave">保存草稿</el-button>
          <el-button type="success" :loading="submitLoading" @click="handleSaveAndSubmit">保存并提交</el-button>
          <el-button @click="handleBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 从预算数据导入对话框 -->
    <el-dialog
      title="从预算数据导入"
      :visible.sync="importDialogVisible"
      width="80%"
      :close-on-click-modal="false"
    >
      <el-form :inline="true" size="small">
        <el-form-item label="科目编码">
          <el-input v-model="importQuery.subjectCode" placeholder="请输入科目编码" clearable />
        </el-form-item>
        <el-form-item label="组织编码">
          <el-input v-model="importQuery.organizationCode" placeholder="请输入组织编码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearchBudgetData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleResetImportQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="importLoading"
        :data="budgetDataList"
        border
        stripe
        max-height="400"
        @selection-change="handleBudgetDataSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="subjectCode" label="科目编码" width="120" />
        <el-table-column prop="subjectName" label="科目名称" width="150" />
        <el-table-column prop="organizationCode" label="组织编码" width="120" />
        <el-table-column prop="organizationName" label="组织名称" width="150" />
        <el-table-column prop="dimension1Code" label="维度1编码" width="120" />
        <el-table-column prop="dimension1Name" label="维度1名称" width="150" />
        <el-table-column prop="budgetValue" label="预算值" width="150" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.budgetValue) }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="importQuery.pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="importQuery.pageSize"
        :total="importTotal"
        layout="total, sizes, prev, pager, next"
        style="margin-top: 20px; text-align: right"
        @size-change="handleImportSizeChange"
        @current-change="handleImportCurrentChange"
      />

      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  createAdjustment,
  updateAdjustment,
  getAdjustmentWithDetails,
  submitAdjustment
} from '@/api/financialSharing/budgetPlanning/budgetAdjustment'
import { getModelList } from '@/api/financialSharing/budgetPlanning/budgetModel'
import { getBudgetDataList } from '@/api/financialSharing/budgetPlanning/budgetData'

export default {
  name: 'CreateAdjustment',
  data() {
    return {
      isEdit: false,
      saveLoading: false,
      submitLoading: false,
      modelList: [],
      selectedDetails: [],
      adjustmentForm: {
        adjustmentId: '',
        modelId: '',
        period: '',
        version: '',
        adjustmentType: '',
        adjustmentReason: '',
        adjustmentDesc: '',
        detailList: []
      },
      rules: {
        modelId: [{ required: true, message: '请选择预算模型', trigger: 'change' }],
        period: [{ required: true, message: '请输入预算期间', trigger: 'blur' }],
        version: [{ required: true, message: '请输入预算版本', trigger: 'blur' }],
        adjustmentType: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
        adjustmentReason: [{ required: true, message: '请输入调整原因', trigger: 'blur' }],
        detailList: [
          {
            type: 'array',
            required: true,
            message: '请至少添加一条调整明细',
            trigger: 'change',
            validator: (rule, value, callback) => {
              if (!value || value.length === 0) {
                callback(new Error('请至少添加一条调整明细'))
              } else {
                callback()
              }
            }
          }
        ]
      },
      importDialogVisible: false,
      importLoading: false,
      budgetDataList: [],
      selectedBudgetData: [],
      importTotal: 0,
      importQuery: {
        modelId: '',
        period: '',
        version: '',
        subjectCode: '',
        organizationCode: '',
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.loadModelList()
    const adjustmentId = this.$route.query.adjustmentId
    if (adjustmentId) {
      this.isEdit = true
      this.loadAdjustmentData(adjustmentId)
    }
  },
  methods: {
    // 加载预算模型列表
    async loadModelList() {
      try {
        const res = await getModelList({ pageNum: 1, pageSize: 100 })
        if (res.code === 1) {
          this.modelList = res.data.list || []
        }
      } catch (error) {
        console.error('加载预算模型列表失败:', error)
      }
    },
    // 加载调整单数据
    async loadAdjustmentData(adjustmentId) {
      try {
        const res = await getAdjustmentWithDetails({ adjustmentId })
        if (res.code === 1) {
          this.adjustmentForm = res.data
          if (!this.adjustmentForm.detailList) {
            this.adjustmentForm.detailList = []
          }
        } else {
          this.$message.error(res.msg || '加载调整单数据失败')
        }
      } catch (error) {
        this.$message.error('加载调整单数据失败: ' + error.message)
      }
    },
    // 模型变更
    handleModelChange(modelId) {
      const model = this.modelList.find(m => m.modelId === modelId)
      if (model) {
        // 可以根据模型自动填充一些信息
        console.log('选择的模型:', model)
      }
    },
    // 添加明细
    handleAddDetail() {
      this.adjustmentForm.detailList.push({
        subjectCode: '',
        subjectName: '',
        organizationCode: '',
        organizationName: '',
        dimension1Code: '',
        dimension1Name: '',
        dimension2Code: '',
        dimension2Name: '',
        originalValue: 0,
        adjustmentValue: 0,
        adjustedValue: 0,
        adjustmentReason: ''
      })
    },
    // 删除明细
    handleDeleteDetail(index) {
      this.$confirm('确认删除该明细吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.adjustmentForm.detailList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    // 明细选择变更
    handleDetailSelectionChange(val) {
      this.selectedDetails = val
    },
    // 批量删除明细
    handleBatchDeleteDetail() {
      this.$confirm(`确认删除选中的 ${this.selectedDetails.length} 条明细吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.selectedDetails.forEach(detail => {
          const index = this.adjustmentForm.detailList.indexOf(detail)
          if (index > -1) {
            this.adjustmentForm.detailList.splice(index, 1)
          }
        })
        this.$message.success('批量删除成功')
      }).catch(() => {})
    },
    // 值变更时自动计算调整后值
    handleValueChange(row) {
      const originalValue = Number(row.originalValue) || 0
      const adjustmentValue = Number(row.adjustmentValue) || 0
      row.adjustedValue = originalValue + adjustmentValue
    },
    // 格式化数字
    formatNumber(value) {
      if (value === null || value === undefined) {
        return '0.00'
      }
      return Number(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    // 从预算数据导入
    handleImportFromBudget() {
      if (!this.adjustmentForm.modelId) {
        this.$message.warning('请先选择预算模型')
        return
      }
      if (!this.adjustmentForm.period) {
        this.$message.warning('请先输入预算期间')
        return
      }
      if (!this.adjustmentForm.version) {
        this.$message.warning('请先输入预算版本')
        return
      }

      this.importQuery.modelId = this.adjustmentForm.modelId
      this.importQuery.period = this.adjustmentForm.period
      this.importQuery.version = this.adjustmentForm.version
      this.importQuery.pageNum = 1

      this.importDialogVisible = true
      this.loadBudgetData()
    },
    // 加载预算数据
    async loadBudgetData() {
      this.importLoading = true
      try {
        const res = await getBudgetDataList(this.importQuery)
        if (res.code === 1) {
          this.budgetDataList = res.data.list || []
          this.importTotal = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询预算数据失败')
        }
      } catch (error) {
        this.$message.error('查询预算数据失败: ' + error.message)
      } finally {
        this.importLoading = false
      }
    },
    // 查询预算数据
    handleSearchBudgetData() {
      this.importQuery.pageNum = 1
      this.loadBudgetData()
    },
    // 重置导入查询
    handleResetImportQuery() {
      this.importQuery.subjectCode = ''
      this.importQuery.organizationCode = ''
      this.importQuery.pageNum = 1
      this.loadBudgetData()
    },
    // 导入分页
    handleImportSizeChange(val) {
      this.importQuery.pageSize = val
      this.loadBudgetData()
    },
    handleImportCurrentChange(val) {
      this.importQuery.pageNum = val
      this.loadBudgetData()
    },
    // 预算数据选择变更
    handleBudgetDataSelectionChange(val) {
      this.selectedBudgetData = val
    },
    // 确认导入
    confirmImport() {
      if (this.selectedBudgetData.length === 0) {
        this.$message.warning('请至少选择一条预算数据')
        return
      }

      this.selectedBudgetData.forEach(data => {
        this.adjustmentForm.detailList.push({
          dataId: data.dataId,
          subjectCode: data.subjectCode,
          subjectName: data.subjectName,
          organizationCode: data.organizationCode,
          organizationName: data.organizationName,
          dimension1Code: data.dimension1Code,
          dimension1Name: data.dimension1Name,
          dimension2Code: data.dimension2Code,
          dimension2Name: data.dimension2Name,
          originalValue: data.budgetValue || 0,
          adjustmentValue: 0,
          adjustedValue: data.budgetValue || 0,
          adjustmentReason: ''
        })
      })

      this.$message.success(`成功导入 ${this.selectedBudgetData.length} 条明细`)
      this.importDialogVisible = false
    },
    // 保存草稿
    handleSave() {
      this.$refs.adjustmentForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            const api = this.isEdit ? updateAdjustment : createAdjustment
            const res = await api(this.adjustmentForm)
            if (res.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '创建成功')
              this.$router.back()
            } else {
              this.$message.error(res.msg || (this.isEdit ? '修改失败' : '创建失败'))
            }
          } catch (error) {
            this.$message.error((this.isEdit ? '修改失败: ' : '创建失败: ') + error.message)
          } finally {
            this.saveLoading = false
          }
        } else {
          this.$message.warning('请完善表单信息')
        }
      })
    },
    // 保存并提交
    handleSaveAndSubmit() {
      this.$refs.adjustmentForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            // 先保存
            const api = this.isEdit ? updateAdjustment : createAdjustment
            const saveRes = await api(this.adjustmentForm)
            if (saveRes.code === 1) {
              // 再提交
              const adjustmentId = this.adjustmentForm.adjustmentId || saveRes.data
              const submitRes = await submitAdjustment({ adjustmentId })
              if (submitRes.code === 1) {
                this.$message.success('保存并提交成功')
                this.$router.back()
              } else {
                this.$message.error(submitRes.msg || '提交失败')
              }
            } else {
              this.$message.error(saveRes.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('操作失败: ' + error.message)
          } finally {
            this.submitLoading = false
          }
        } else {
          this.$message.warning('请完善表单信息')
        }
      })
    },
    // 返回
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.create-adjustment-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .detail-toolbar {
    margin-bottom: 10px;
  }

  ::v-deep .el-input-number {
    .el-input__inner {
      text-align: right;
    }
  }
}
</style>

