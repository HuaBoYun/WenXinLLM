<template>
  <el-dialog
    title="执行成本结转"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-form :model="executeForm" :rules="executeRules" ref="executeFormRef" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="结转期间" prop="transferPeriod">
            <el-date-picker
              v-model="executeForm.transferPeriod"
              type="month"
              placeholder="选择结转期间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结转类型" prop="transferType">
            <el-select v-model="executeForm.transferType" style="width: 100%">
              <el-option label="销售成本结转" value="SALES_COST" />
              <el-option label="生产成本结转" value="PRODUCTION_COST" />
              <el-option label="期间费用分摊" value="PERIOD_EXPENSE" />
              <el-option label="成本差异结转" value="COST_VARIANCE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="存货分类" prop="categoryIds">
            <el-select
              v-model="executeForm.categoryIds"
              multiple
              placeholder="选择存货分类（可多选）"
              style="width: 100%"
            >
              <el-option
                v-for="category in categoryList"
                :key="category.categoryId"
                :label="category.categoryName"
                :value="category.categoryId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仓库" prop="warehouseIds">
            <el-select
              v-model="executeForm.warehouseIds"
              multiple
              placeholder="选择仓库（可多选）"
              style="width: 100%"
            >
              <el-option
                v-for="warehouse in warehouseList"
                :key="warehouse.warehouseId"
                :label="warehouse.warehouseName"
                :value="warehouse.warehouseId"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否生成凭证" prop="generateVoucher">
            <el-switch
              v-model="executeForm.generateVoucher"
              active-text="是"
              inactive-text="否"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否覆盖已有" prop="overrideExisting">
            <el-switch
              v-model="executeForm.overrideExisting"
              active-text="是"
              inactive-text="否"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="结转说明" prop="description">
        <el-input
          v-model="executeForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入结转说明"
        />
      </el-form-item>

      <!-- 结转规则配置 -->
      <el-divider content-position="left">结转规则配置</el-divider>
      
      <div v-if="executeForm.transferType === 'SALES_COST'">
        <el-form-item label="销售成本科目" prop="salesCostSubject">
          <el-select v-model="executeForm.salesCostSubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存商品科目" prop="inventorySubject">
          <el-select v-model="executeForm.inventorySubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
      </div>

      <div v-if="executeForm.transferType === 'PRODUCTION_COST'">
        <el-form-item label="生产成本科目" prop="productionCostSubject">
          <el-select v-model="executeForm.productionCostSubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原材料科目" prop="materialSubject">
          <el-select v-model="executeForm.materialSubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
      </div>

      <div v-if="executeForm.transferType === 'PERIOD_EXPENSE'">
        <el-form-item label="分摊方式" prop="allocationMethod">
          <el-radio-group v-model="executeForm.allocationMethod">
            <el-radio label="QUANTITY">按数量分摊</el-radio>
            <el-radio label="AMOUNT">按金额分摊</el-radio>
            <el-radio label="WEIGHT">按重量分摊</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="期间费用科目" prop="expenseSubject">
          <el-select v-model="executeForm.expenseSubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
      </div>

      <div v-if="executeForm.transferType === 'COST_VARIANCE'">
        <el-form-item label="成本差异科目" prop="varianceSubject">
          <el-select v-model="executeForm.varianceSubject" style="width: 100%" filterable>
            <el-option
              v-for="subject in subjectList"
              :key="subject.subjectId"
              :label="`${subject.subjectCode} - ${subject.subjectName}`"
              :value="subject.subjectId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="差异处理方式" prop="varianceHandling">
          <el-radio-group v-model="executeForm.varianceHandling">
            <el-radio label="TRANSFER_TO_COST">转入成本</el-radio>
            <el-radio label="TRANSFER_TO_EXPENSE">转入费用</el-radio>
            <el-radio label="PROPORTIONAL_ALLOCATION">按比例分摊</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
    </el-form>

    <!-- 预览结果 -->
    <div v-if="previewData.length > 0" style="margin-top: 20px;">
      <el-divider content-position="left">预览结果</el-divider>
      <el-table :data="previewData" border size="small" max-height="300">
        <el-table-column prop="inventoryName" label="存货名称" width="150" />
        <el-table-column prop="categoryName" label="存货分类" width="120" />
        <el-table-column prop="transferAmount" label="结转金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.transferAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceAmount" label="成本差异" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.varianceAmount >= 0 ? 'positive-amount' : 'negative-amount'">
              {{ formatAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="voucherPreview" label="凭证预览" min-width="200">
          <template slot-scope="scope">
            <div class="voucher-preview">
              <div>借：{{ scope.row.debitSubject }} {{ formatAmount(scope.row.debitAmount) }}</div>
              <div>贷：{{ scope.row.creditSubject }} {{ formatAmount(scope.row.creditAmount) }}</div>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="preview-summary" style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="summary-item">
              <span class="summary-label">预计结转存货：</span>
              <span class="summary-value">{{ previewData.length }}项</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span class="summary-label">预计结转金额：</span>
              <span class="summary-value amount">{{ formatAmount(getTotalTransferAmount()) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="summary-item">
              <span class="summary-label">预计生成凭证：</span>
              <span class="summary-value">{{ executeForm.generateVoucher ? previewData.length : 0 }}张</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="info" @click="handlePreview" :loading="previewLoading">预览结转</el-button>
      <el-button type="primary" @click="handleExecute" :loading="executeLoading">执行结转</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getInventoryCategoryTree, getWarehouseList } from '@/api/financialSharing/inventory'

export default {
  name: 'TransferExecuteDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      previewLoading: false,
      executeLoading: false,
      categoryList: [],
      warehouseList: [],
      subjectList: [],
      previewData: [],
      executeForm: {
        transferPeriod: '',
        transferType: 'SALES_COST',
        categoryIds: [],
        warehouseIds: [],
        generateVoucher: true,
        overrideExisting: false,
        description: '',
        // 销售成本结转相关
        salesCostSubject: '',
        inventorySubject: '',
        // 生产成本结转相关
        productionCostSubject: '',
        materialSubject: '',
        // 期间费用分摊相关
        allocationMethod: 'QUANTITY',
        expenseSubject: '',
        // 成本差异结转相关
        varianceSubject: '',
        varianceHandling: 'TRANSFER_TO_COST'
      },
      executeRules: {
        transferPeriod: [
          { required: true, message: '请选择结转期间', trigger: 'change' }
        ],
        transferType: [
          { required: true, message: '请选择结转类型', trigger: 'change' }
        ],
        categoryIds: [
          { required: true, message: '请选择存货分类', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetForm()
        this.loadBasicData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    resetForm() {
      this.executeForm = {
        transferPeriod: '',
        transferType: 'SALES_COST',
        categoryIds: [],
        warehouseIds: [],
        generateVoucher: true,
        overrideExisting: false,
        description: '',
        salesCostSubject: '',
        inventorySubject: '',
        productionCostSubject: '',
        materialSubject: '',
        allocationMethod: 'QUANTITY',
        expenseSubject: '',
        varianceSubject: '',
        varianceHandling: 'TRANSFER_TO_COST'
      }
      this.previewData = []
      this.$nextTick(() => {
        if (this.$refs.executeFormRef) {
          this.$refs.executeFormRef.clearValidate()
        }
      })
    },

    async loadBasicData() {
      try {
        // 加载存货分类
        const categoryResponse = await getInventoryCategoryTree()
        if (categoryResponse.code === 1) {
          // 转换字段名：将大写字段名转换为小写驼峰格式
          this.categoryList = (categoryResponse.data || []).map(item => ({
            categoryId: item.CATEGORYID || item.categoryId,
            categoryName: item.CATEGORYNAME || item.categoryName,
            categoryCode: item.CATEGORYCODE || item.categoryCode,
            parentId: item.PARENTID || item.parentId,
            level: item.LEVEL || item.level,
            sort: item.SORT || item.sort,
            status: item.STATUS || item.status
          }))
        }

        // 加载仓库列表
        const warehouseResponse = await getWarehouseList()
        if (warehouseResponse.code === 1) {
          this.warehouseList = warehouseResponse.data || []
        }

        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.subjectList = []
      } catch (error) {
        console.error('加载基础数据失败：', error)
      }
    },

    async handlePreview() {
      this.$refs.executeFormRef.validate(async (valid) => {
        if (valid) {
          this.previewLoading = true
          try {
            // 暂未对接预览 API，先以空状态展示，待后端接口提供后接入
            await new Promise(resolve => setTimeout(resolve, 300))
            this.previewData = []
            this.$message.success('预览生成成功')
          } catch (error) {
            this.$message.error('预览失败：' + error.message)
          } finally {
            this.previewLoading = false
          }
        }
      })
    },

    async handleExecute() {
      this.$refs.executeFormRef.validate(async (valid) => {
        if (valid) {
          this.executeLoading = true
          try {
            // 处理过程异步等待（实际由后端 API 完成）
            await new Promise(resolve => setTimeout(resolve, 2000))
            this.$emit('execute', { ...this.executeForm, previewData: this.previewData })
          } catch (error) {
            this.$message.error('执行失败：' + error.message)
          } finally {
            this.executeLoading = false
          }
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
    },

    getTotalTransferAmount() {
      return this.previewData.reduce((total, item) => total + item.transferAmount, 0)
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.voucher-preview {
  font-size: 12px;
  line-height: 1.4;
  
  div {
    margin-bottom: 2px;
  }
}

.preview-summary {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  
  .summary-item {
    .summary-label {
      color: #606266;
      font-size: 14px;
    }
    
    .summary-value {
      font-weight: 600;
      color: #303133;
      
      &.amount {
        color: #f56c6c;
      }
    }
  }
}

.amount {
  color: #f56c6c;
  font-weight: 600;
}

.positive-amount {
  color: #67c23a;
  font-weight: 600;
}

.negative-amount {
  color: #f56c6c;
  font-weight: 600;
}

.dialog-footer {
  text-align: right;
}
</style>
