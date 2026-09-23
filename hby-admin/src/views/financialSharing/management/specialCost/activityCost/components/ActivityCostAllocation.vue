<template>
  <el-dialog
    title="作业成本分配"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    @close="close"
  >
    <div class="allocation-container">
      <!-- 作业信息 -->
      <el-card shadow="never" class="info-card">
        <div slot="header" class="card-header">
          <span>作业信息</span>
        </div>
        <el-table
          :data="selectedActivities"
          border
          style="width: 100%"
        >
          <el-table-column
            prop="activityCode"
            label="作业编码"
            width="120"
          />
          <el-table-column
            prop="activityName"
            label="作业名称"
            width="150"
          />
          <el-table-column
            prop="costDriver"
            label="成本动因"
            width="120"
          >
            <template slot-scope="scope">
              {{ getCostDriverName(scope.row.costDriver) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="totalCost"
            label="总成本"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.totalCost) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="unallocatedCost"
            label="未分配成本"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              {{ formatAmount(scope.row.unallocatedCost) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="unitCost"
            label="单位成本"
            width="100"
            align="right"
          >
            <template slot-scope="scope">
              {{ scope.row.unitCost.toFixed(4) }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 分配设置 -->
      <el-card shadow="never" class="allocation-card">
        <div slot="header" class="card-header">
          <span>分配设置</span>
        </div>
        <el-form
          ref="allocationForm"
          :model="allocationForm"
          :rules="allocationRules"
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="分配方式" prop="allocationType">
                <el-select
                  v-model="allocationForm.allocationType"
                  placeholder="请选择分配方式"
                  style="width: 100%"
                  @change="handleAllocationTypeChange"
                >
                  <el-option label="按比例分配" value="proportion" />
                  <el-option label="按数量分配" value="quantity" />
                  <el-option label="手工分配" value="manual" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分配日期" prop="allocationDate">
                <el-date-picker
                  v-model="allocationForm.allocationDate"
                  type="date"
                  placeholder="选择分配日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>

      <!-- 分配明细 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>分配明细</span>
          <el-button
            type="primary"
            size="small"
            style="float: right; margin-top: -5px"
            @click="addAllocationItem"
          >
            添加分配项
          </el-button>
        </div>
        <el-table
          :data="allocationItems"
          border
          style="width: 100%"
          :summary-method="getSummaries"
          show-summary
        >
          <el-table-column
            prop="targetObject"
            label="分配对象"
            width="150"
          >
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.targetObject"
                placeholder="选择分配对象"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in allocationTargets"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            prop="allocatedQuantity"
            label="分配数量"
            width="120"
          >
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.allocatedQuantity"
                :precision="2"
                :step="1"
                :min="0"
                style="width: 100%"
                @change="calculateAllocation(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="allocationRate"
            label="分配比例(%)"
            width="120"
          >
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.allocationRate"
                :precision="2"
                :step="1"
                :min="0"
                :max="100"
                style="width: 100%"
                :disabled="allocationForm.allocationType !== 'proportion'"
                @change="calculateAllocationByRate(scope.$index)"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="allocatedAmount"
            label="分配金额"
            width="120"
            align="right"
          >
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.allocatedAmount"
                :precision="2"
                :step="100"
                :min="0"
                style="width: 100%"
                :disabled="allocationForm.allocationType !== 'manual'"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            min-width="150"
          >
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.remark"
                placeholder="备注"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="80"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                style="color: #f56c6c"
                @click="removeAllocationItem(scope.$index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 分配汇总 -->
      <el-card shadow="never" class="summary-card">
        <div slot="header" class="card-header">
          <span>分配汇总</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="summary-item">
              <div class="summary-label">总成本</div>
              <div class="summary-value">{{ formatAmount(totalCost) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-item">
              <div class="summary-label">已分配</div>
              <div class="summary-value allocated">{{ formatAmount(totalAllocated) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-item">
              <div class="summary-label">未分配</div>
              <div class="summary-value unallocated" :class="getUnallocatedClass(totalUnallocated)">
                {{ formatAmount(totalUnallocated) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="summary-item">
              <div class="summary-label">分配率</div>
              <div class="summary-value">{{ allocationPercentage }}%</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button
        type="primary"
        :loading="buttonLoading"
        @click="saveAllocation"
      >
        确认分配
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getActivityCostAllocation } from '@/api/financialSharing/specialCost'

export default {
  name: 'ActivityCostAllocation',
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      selectedActivities: [],
      allocationForm: {
        allocationType: 'proportion',
        allocationDate: ''
      },
      allocationRules: {
        allocationType: [
          { required: true, message: '请选择分配方式', trigger: 'change' }
        ],
        allocationDate: [
          { required: true, message: '请选择分配日期', trigger: 'change' }
        ]
      },
      allocationItems: [],
      allocationTargets: [
        { label: '产品A', value: 'product_a' },
        { label: '产品B', value: 'product_b' },
        { label: '产品C', value: 'product_c' },
        { label: '订单001', value: 'order_001' },
        { label: '订单002', value: 'order_002' },
        { label: '项目X', value: 'project_x' },
        { label: '项目Y', value: 'project_y' }
      ]
    }
  },
  computed: {
    totalCost() {
      return this.selectedActivities.reduce((sum, item) => sum + (item.totalCost || 0), 0)
    },
    totalAllocated() {
      return this.allocationItems.reduce((sum, item) => sum + (item.allocatedAmount || 0), 0)
    },
    totalUnallocated() {
      return this.totalCost - this.totalAllocated
    },
    allocationPercentage() {
      if (this.totalCost === 0) return 0
      return ((this.totalAllocated / this.totalCost) * 100).toFixed(1)
    }
  },
  methods: {
    showAllocation(activities) {
      this.dialogVisible = true
      this.selectedActivities = activities
      this.allocationForm.allocationDate = new Date().toISOString().split('T')[0]
      this.allocationItems = []
      
      // 初始化分配项
      this.addAllocationItem()
    },
    close() {
      this.dialogVisible = false
      this.$refs.allocationForm.resetFields()
      this.allocationItems = []
    },
    handleAllocationTypeChange() {
      // 根据分配方式重新计算
      this.recalculateAllocation()
    },
    addAllocationItem() {
      this.allocationItems.push({
        targetObject: '',
        allocatedQuantity: 0,
        allocationRate: 0,
        allocatedAmount: 0,
        remark: ''
      })
    },
    removeAllocationItem(index) {
      this.allocationItems.splice(index, 1)
    },
    calculateAllocation(index) {
      const item = this.allocationItems[index]
      if (this.allocationForm.allocationType === 'quantity') {
        // 按数量分配
        const totalQuantity = this.allocationItems.reduce((sum, i) => sum + (i.allocatedQuantity || 0), 0)
        if (totalQuantity > 0) {
          item.allocationRate = ((item.allocatedQuantity / totalQuantity) * 100).toFixed(2)
          item.allocatedAmount = (this.totalCost * item.allocatedQuantity / totalQuantity).toFixed(2)
        }
      }
    },
    calculateAllocationByRate(index) {
      const item = this.allocationItems[index]
      if (this.allocationForm.allocationType === 'proportion') {
        // 按比例分配
        item.allocatedAmount = (this.totalCost * item.allocationRate / 100).toFixed(2)
      }
    },
    recalculateAllocation() {
      this.allocationItems.forEach((item, index) => {
        if (this.allocationForm.allocationType === 'quantity') {
          this.calculateAllocation(index)
        } else if (this.allocationForm.allocationType === 'proportion') {
          this.calculateAllocationByRate(index)
        }
      })
    },
    async saveAllocation() {
      this.$refs.allocationForm.validate(async (valid) => {
        if (valid) {
          if (this.allocationItems.length === 0) {
            this.$baseMessage('请添加分配明细', 'warning')
            return
          }
          
          // 验证分配项
          for (const item of this.allocationItems) {
            if (!item.targetObject) {
              this.$baseMessage('请选择分配对象', 'warning')
              return
            }
            if (!item.allocatedAmount || item.allocatedAmount <= 0) {
              this.$baseMessage('分配金额必须大于0', 'warning')
              return
            }
          }
          
          this.buttonLoading = true
          
          try {
            const allocationData = {
              activities: this.selectedActivities.map(a => a.id),
              allocationType: this.allocationForm.allocationType,
              allocationDate: this.allocationForm.allocationDate,
              allocationItems: this.allocationItems
            }
            
            const { code, msg } = await getActivityCostAllocation(allocationData)
            if (code === 200) {
              this.$baseMessage(msg, 'success')
              this.$emit('fetch-data')
              this.close()
            } else {
              this.$baseMessage(msg, 'error')
            }
          } catch (error) {
            this.$baseMessage('分配失败', 'error')
          } finally {
            this.buttonLoading = false
          }
        }
      })
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'allocatedQuantity' || column.property === 'allocationRate' || column.property === 'allocatedAmount') {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            const sum = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
            
            if (column.property === 'allocatedAmount') {
              sums[index] = this.formatAmount(sum)
            } else {
              sums[index] = sum.toFixed(2)
            }
          } else {
            sums[index] = '0.00'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getCostDriverName(driver) {
      const driverMap = {
        'machine_hours': '机器小时',
        'labor_hours': '人工小时',
        'product_quantity': '产品数量',
        'order_quantity': '订单数量',
        'inspection_times': '检验次数'
      }
      return driverMap[driver] || driver
    },
    getUnallocatedClass(amount) {
      if (amount > 0) return 'text-warning'
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-container {
  .info-card,
  .allocation-card,
  .detail-card,
  .summary-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    font-weight: bold;
    color: #303133;
  }
  
  .summary-item {
    text-align: center;
    padding: 15px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    
    .summary-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }
    
    .summary-value {
      font-size: 20px;
      font-weight: bold;
      color: #303133;
      
      &.allocated {
        color: #67c23a;
      }
      
      &.unallocated {
        color: #f56c6c;
      }
    }
  }
}

.text-warning {
  color: #e6a23c;
}

.dialog-footer {
  text-align: right;
}
</style>
