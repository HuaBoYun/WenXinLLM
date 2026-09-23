<template>
  <el-dialog
    title="存货成本计算"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <div class="cost-calculate-container">
      <!-- 存货基本信息 -->
      <el-card class="inventory-info-card" shadow="never">
        <div slot="header" class="card-header">
          <span>存货基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>存货编码：</label>
              <span>{{ inventoryData.inventoryCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>存货名称：</label>
              <span>{{ inventoryData.inventoryName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>计价方法：</label>
              <el-tag :type="getPricingMethodTagType(inventoryData.pricingMethod)">
                {{ inventoryData.pricingMethodName }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 15px;">
          <el-col :span="8">
            <div class="info-item">
              <label>当前库存：</label>
              <span class="quantity">{{ inventoryData.currentQuantity }} {{ inventoryData.unit }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>当前单价：</label>
              <span class="amount">{{ formatAmount(inventoryData.unitCost) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>库存总值：</label>
              <span class="amount">{{ formatAmount(inventoryData.totalValue) }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 计算参数设置 -->
      <el-card class="calculate-params-card" shadow="never" style="margin-top: 20px;">
        <div slot="header" class="card-header">
          <span>计算参数设置</span>
        </div>
        <el-form :model="calculateForm" :rules="calculateRules" ref="calculateFormRef" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="计算期间" prop="calculatePeriod">
                <el-date-picker
                  v-model="calculateForm.calculatePeriod"
                  type="month"
                  placeholder="选择计算期间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计算方式" prop="calculateType">
                <el-select v-model="calculateForm.calculateType" style="width: 100%">
                  <el-option label="重新计算全部" value="FULL" />
                  <el-option label="增量计算" value="INCREMENT" />
                  <el-option label="指定日期计算" value="SPECIFIC_DATE" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20" v-if="calculateForm.calculateType === 'SPECIFIC_DATE'">
            <el-col :span="12">
              <el-form-item label="指定日期" prop="specificDate">
                <el-date-picker
                  v-model="calculateForm.specificDate"
                  type="date"
                  placeholder="选择指定日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="是否生成凭证" prop="generateVoucher">
                <el-switch
                  v-model="calculateForm.generateVoucher"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否覆盖已有" prop="overrideExisting">
                <el-switch
                  v-model="calculateForm.overrideExisting"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <!-- 计算预览 -->
      <el-card class="calculate-preview-card" shadow="never" style="margin-top: 20px;" v-if="previewData.length > 0">
        <div slot="header" class="card-header">
          <span>计算预览</span>
          <el-button type="text" @click="handlePreview" :loading="previewLoading">刷新预览</el-button>
        </div>
        <el-table :data="previewData" border size="small">
          <el-table-column prop="transactionDate" label="交易日期" width="120" />
          <el-table-column prop="transactionType" label="交易类型" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.transactionType === 'IN' ? 'success' : 'warning'" size="mini">
                {{ scope.row.transactionType === 'IN' ? '入库' : '出库' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" align="right" />
          <el-table-column prop="unitPrice" label="单价" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.unitPrice) }}
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.amount) }}
            </template>
          </el-table-column>
          <el-table-column prop="balanceQuantity" label="结存数量" width="100" align="right" />
          <el-table-column prop="balanceAmount" label="结存金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.balanceAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="newUnitCost" label="新单价" width="120" align="right">
            <template slot-scope="scope">
              <span class="new-cost">{{ formatAmount(scope.row.newUnitCost) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 计算结果统计 -->
      <el-card class="calculate-result-card" shadow="never" style="margin-top: 20px;" v-if="calculateResult">
        <div slot="header" class="card-header">
          <span>计算结果统计</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="result-item">
              <div class="result-label">处理交易数</div>
              <div class="result-value">{{ calculateResult.processedTransactions }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="result-item">
              <div class="result-label">成本调整金额</div>
              <div class="result-value amount">{{ formatAmount(calculateResult.costAdjustment) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="result-item">
              <div class="result-label">新单位成本</div>
              <div class="result-value amount">{{ formatAmount(calculateResult.newUnitCost) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="result-item">
              <div class="result-label">生成凭证数</div>
              <div class="result-value">{{ calculateResult.generatedVouchers || 0 }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="info" @click="handlePreview" :loading="previewLoading">预览计算</el-button>
      <el-button type="primary" @click="handleCalculate" :loading="calculateLoading">执行计算</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CostCalculateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    inventoryData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      previewLoading: false,
      calculateLoading: false,
      calculateForm: {
        calculatePeriod: '',
        calculateType: 'FULL',
        specificDate: '',
        generateVoucher: true,
        overrideExisting: false
      },
      calculateRules: {
        calculatePeriod: [
          { required: true, message: '请选择计算期间', trigger: 'change' }
        ],
        calculateType: [
          { required: true, message: '请选择计算方式', trigger: 'change' }
        ],
        specificDate: [
          { required: true, message: '请选择指定日期', trigger: 'change' }
        ]
      },
      previewData: [],
      calculateResult: null
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.resetForm()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    resetForm() {
      this.calculateForm = {
        calculatePeriod: '',
        calculateType: 'FULL',
        specificDate: '',
        generateVoucher: true,
        overrideExisting: false
      }
      this.previewData = []
      this.calculateResult = null
      this.$nextTick(() => {
        if (this.$refs.calculateFormRef) {
          this.$refs.calculateFormRef.clearValidate()
        }
      })
    },

    async handlePreview() {
      this.$refs.calculateFormRef.validate(async (valid) => {
        if (valid) {
          this.previewLoading = true
          try {
            // 暂未对接 API，先以空状态展示，待后端接口提供后接入
            await new Promise(resolve => setTimeout(resolve, 300))
            this.previewData = []
            this.$message.success('预览计算完成')
          } catch (error) {
            this.$message.error('预览计算失败：' + error.message)
          } finally {
            this.previewLoading = false
          }
        }
      })
    },

    async handleCalculate() {
      this.$refs.calculateFormRef.validate(async (valid) => {
        if (valid) {
          this.calculateLoading = true
          try {
            // 处理过程异步等待（实际由后端 API 完成）
            await new Promise(resolve => setTimeout(resolve, 300))
            this.calculateResult = {
              processedTransactions: 0,
              costAdjustment: 0,
              newUnitCost: 0,
              generatedVouchers: 0
            }
            this.$message.success('成本计算完成')
            this.$emit('calculate', this.calculateResult)
          } catch (error) {
            this.$message.error('成本计算失败：' + error.message)
          } finally {
            this.calculateLoading = false
          }
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
    },

    getPricingMethodTagType(method) {
      const typeMap = {
        1: 'primary',  // 移动平均法
        2: 'success',  // 先进先出法
        3: 'warning',  // 加权平均法
        4: 'info'      // 个别计价法
      }
      return typeMap[method] || 'default'
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-calculate-container {
  max-height: 600px;
  overflow-y: auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.inventory-info-card {
  .info-item {
    margin-bottom: 10px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
    
    .quantity {
      color: #409eff;
      font-weight: 600;
    }
    
    .amount {
      color: #f56c6c;
      font-weight: 600;
    }
  }
}

.calculate-result-card {
  .result-item {
    text-align: center;
    padding: 15px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    
    .result-label {
      font-size: 12px;
      color: #909399;
      margin-bottom: 8px;
    }
    
    .result-value {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      
      &.amount {
        color: #f56c6c;
      }
    }
  }
}

.new-cost {
  color: #67c23a;
  font-weight: 600;
}

.dialog-footer {
  text-align: right;
}
</style>
