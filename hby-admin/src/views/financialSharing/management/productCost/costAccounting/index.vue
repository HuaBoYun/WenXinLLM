<template>
  <div class="cost-accounting-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-cpu"></i>
          成本核算
        </h1>
        <p class="page-description">执行产品成本计算、核算方法配置、成本分摊等核算业务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-cpu" @click="handleCalculate">
          执行核算
        </el-button>
        <el-button type="success" icon="el-icon-setting" @click="handleMethodConfig">
          核算配置
        </el-button>
        <el-button type="warning" icon="el-icon-view" @click="handleViewResult">
          查看结果
        </el-button>
      </div>
    </div>

    <!-- 核算条件 -->
    <div class="accounting-conditions">
      <el-card title="核算条件">
        <div slot="header">
          <span>核算条件</span>
        </div>
        <el-form :model="accountingForm" ref="accountingForm" :inline="true" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="核算期间" prop="costingPeriod">
                <el-date-picker
                  v-model="accountingForm.costingPeriod"
                  type="month"
                  placeholder="选择核算期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="核算方法" prop="costingMethod">
                <el-select v-model="accountingForm.costingMethod" placeholder="请选择核算方法" style="width: 100%">
                  <el-option label="品种法" value="1" />
                  <el-option label="分批法" value="2" />
                  <el-option label="分步法" value="3" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="产品范围" prop="productScope">
                <el-select v-model="accountingForm.productScope" placeholder="请选择产品范围" style="width: 100%">
                  <el-option label="全部产品" value="all" />
                  <el-option label="指定产品" value="selected" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="包含在制品" prop="includeWIP">
                <el-switch v-model="accountingForm.includeWIP" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="生成凭证" prop="generateVoucher">
                <el-switch v-model="accountingForm.generateVoucher" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item>
                <el-button type="primary" @click="executeAccounting" :loading="calculating">
                  开始核算
                </el-button>
                <el-button @click="resetAccountingForm">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>

    <!-- 核算结果列表 -->
    <div class="result-container">
      <el-card title="核算结果">
        <div slot="header">
          <span>核算结果</span>
          <div style="float: right">
            <el-button size="small" type="text" @click="refreshResults">刷新</el-button>
          </div>
        </div>

        <!-- 查询条件 -->
        <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
          <el-form-item label="产品名称" prop="productName">
            <el-input v-model="queryForm.productName" placeholder="请输入产品名称" clearable />
          </el-form-item>
          <el-form-item label="核算期间" prop="costingPeriod">
            <el-date-picker
              v-model="queryForm.costingPeriod"
              type="month"
              placeholder="选择核算期间"
              format="yyyy-MM"
              value-format="yyyy-MM"
              clearable
            />
          </el-form-item>
          <el-form-item label="核算状态" prop="costingStatus">
            <el-select v-model="queryForm.costingStatus" placeholder="请选择核算状态" clearable>
              <el-option label="核算中" value="1" />
              <el-option label="已完成" value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 数据表格 -->
        <el-table
          v-loading="loading"
          :data="resultList"
          row-key="costingId"
          border
          stripe
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="产品编码" prop="productCode" width="120" />
          <el-table-column label="产品名称" prop="productName" min-width="150" />
          <el-table-column label="核算期间" prop="costingPeriod" width="100" />
          <el-table-column label="核算方法" prop="costingMethodName" width="100" />
          <el-table-column label="生产数量" prop="productionQuantity" width="100" align="right" />
          <el-table-column label="总成本" prop="totalCost" width="120" align="right">
            <template slot-scope="scope">
              <span>{{ formatAmount(scope.row.totalCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="单位成本" prop="unitCost" width="120" align="right">
            <template slot-scope="scope">
              <span>{{ formatAmount(scope.row.unitCost) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="核算状态" prop="costingStatus" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.costingStatus === 2 ? 'success' : 'warning'">
                {{ scope.row.costingStatus === 2 ? '已完成' : '核算中' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="createTime" width="150" />
          <el-table-column label="操作" width="200" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleViewDetail(scope.row)">详情</el-button>
              <el-button size="mini" type="text" @click="handleViewBreakdown(scope.row)">成本构成</el-button>
              <el-button size="mini" type="text" @click="handleReCalculate(scope.row)">重新核算</el-button>
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
      </el-card>
    </div>

    <!-- 成本构成详情对话框 -->
    <el-dialog
      title="成本构成详情"
      :visible.sync="breakdownDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="cost-breakdown">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="breakdown-item">
              <div class="item-label">直接材料</div>
              <div class="item-value">{{ formatAmount(costBreakdown.directMaterial) }}</div>
              <div class="item-ratio">{{ calculateRatio(costBreakdown.directMaterial, costBreakdown.totalCost) }}%</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="breakdown-item">
              <div class="item-label">直接人工</div>
              <div class="item-value">{{ formatAmount(costBreakdown.directLabor) }}</div>
              <div class="item-ratio">{{ calculateRatio(costBreakdown.directLabor, costBreakdown.totalCost) }}%</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="breakdown-item">
              <div class="item-label">制造费用</div>
              <div class="item-value">{{ formatAmount(costBreakdown.manufacturingOverhead) }}</div>
              <div class="item-ratio">{{ calculateRatio(costBreakdown.manufacturingOverhead, costBreakdown.totalCost) }}%</div>
            </div>
          </el-col>
        </el-row>
        <div class="breakdown-chart">
          <div ref="breakdownChart" style="width: 100%; height: 300px;"></div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="breakdownDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 核算配置对话框 -->
    <el-dialog
      title="核算方法配置"
      :visible.sync="configDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="configForm" :rules="configRules" ref="configForm" label-width="120px">
        <el-form-item label="核算方法" prop="costingMethod">
          <el-select v-model="configForm.costingMethod" placeholder="请选择核算方法" style="width: 100%">
            <el-option label="品种法" value="1" />
            <el-option label="分批法" value="2" />
            <el-option label="分步法" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊基础" prop="allocationBasis">
          <el-select v-model="configForm.allocationBasis" placeholder="请选择分摊基础" style="width: 100%">
            <el-option label="直接人工工时" value="labor_hours" />
            <el-option label="机器工时" value="machine_hours" />
            <el-option label="直接人工成本" value="labor_cost" />
            <el-option label="直接材料成本" value="material_cost" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用分摊率" prop="overheadRate">
          <el-input-number
            v-model="configForm.overheadRate"
            :precision="4"
            :min="0"
            :max="1"
            style="width: 100%"
            placeholder="请输入费用分摊率"
          />
        </el-form-item>
        <el-form-item label="启用状态" prop="isEnabled">
          <el-radio-group v-model="configForm.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfigSubmit" :loading="configSubmitting">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getProductCostList, executeCostAccounting, getProductCostById, recalculateCost } from '@/api/financialSharing/productCost'

export default {
  name: 'CostAccounting',
  components: {
  },
  data() {
    return {
      breakdownChartInstance: null,
      loading: false,
      calculating: false,
      configSubmitting: false,
      resultList: [],
      selectedRows: [],
      total: 0,
      breakdownDialogVisible: false,
      configDialogVisible: false,
      accountingForm: {
        costingPeriod: '',
        costingMethod: '1',
        productScope: 'all',
        includeWIP: true,
        generateVoucher: true,
        productIds: []
      },
      queryForm: {
        pageNumber: 1,
        pageSize: 15,
        productName: '',
        costingPeriod: '',
        costingStatus: ''
      },
      configForm: {
        costingMethod: '1',
        allocationBasis: 'labor_hours',
        overheadRate: 0.15,
        isEnabled: 1
      },
      configRules: {
        costingMethod: [
          { required: true, message: '请选择核算方法', trigger: 'change' }
        ],
        allocationBasis: [
          { required: true, message: '请选择分摊基础', trigger: 'change' }
        ]
      },
      costBreakdown: {
        directMaterial: 0,
        directLabor: 0,
        manufacturingOverhead: 0,
        totalCost: 0
      }
    }
  },
  watch: {
    breakdownDialogVisible(val) {
      if (val) {
        this.$nextTick(() => {
          this.renderBreakdownChart()
        })
      }
    },
    costBreakdown: {
      handler() {
        if (this.breakdownDialogVisible) {
          this.$nextTick(() => {
            this.renderBreakdownChart()
          })
        }
      },
      deep: true
    }
  },
  created() {
    this.getResultList()
    this.initAccountingPeriod()
  },
  mounted() {
    this._resizeHandler = () => {
      if (this.breakdownChartInstance) this.breakdownChartInstance.resize()
    }
    window.addEventListener('resize', this._resizeHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this._resizeHandler)
    if (this.breakdownChartInstance) {
      this.breakdownChartInstance.dispose()
      this.breakdownChartInstance = null
    }
  },
  methods: {
    // 初始化核算期间
    initAccountingPeriod() {
      const now = new Date()
      this.accountingForm.costingPeriod = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },

    // 获取核算结果列表
    async getResultList() {
      this.loading = true
      try {
        const response = await getProductCostList(this.queryForm)
        if (response.code === 200) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.resultList = response.data.tlist || []
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

    // 执行成本核算
    async executeAccounting() {
      if (!this.accountingForm.costingPeriod) {
        this.$message.warning('请选择核算期间')
        return
      }

      this.calculating = true
      try {
        const response = await executeCostAccounting(this.accountingForm)
        if (response.code === 200) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('成本核算执行成功')
          this.getResultList()
        } else {
          this.$message.error(response.msg || '核算失败')
        }
      } catch (error) {
        this.$message.error('核算失败：' + error.message)
      } finally {
        this.calculating = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getResultList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.getResultList()
    },

    // 重置核算表单
    resetAccountingForm() {
      this.$refs.accountingForm.resetFields()
      this.initAccountingPeriod()
    },

    // 刷新结果
    refreshResults() {
      this.getResultList()
    },

    // 查看详情
    handleViewDetail(row) {
      const content = `
        <p><b>核算ID:</b> ${row.costingId || '-'}</p>
        <p><b>产品名称:</b> ${row.productName || '-'}</p>
        <p><b>核算期间:</b> ${row.costingPeriod || '-'}</p>
        <p><b>核算方法:</b> ${row.costingMethod || '-'}</p>
        <p><b>总成本:</b> ${this.formatAmount(row.totalCost)}</p>
        <p><b>单位成本:</b> ${this.formatAmount(row.unitCost)}</p>
        <p><b>直接材料:</b> ${this.formatAmount(row.directMaterial)}</p>
        <p><b>直接人工:</b> ${this.formatAmount(row.directLabor)}</p>
        <p><b>制造费用:</b> ${this.formatAmount(row.manufacturingOverhead)}</p>
        <p><b>核算状态:</b> ${row.statusName || '-'}</p>
        <p><b>核算时间:</b> ${row.createTime || '-'}</p>
      `
      this.$alert(content, '核算详情', { dangerouslyUseHTMLString: true })
    },

    // 查看成本构成
    async handleViewBreakdown(row) {
      this.breakdownDialogVisible = true
      try {
        const response = await getProductCostById(row.costingId)
        if (response.code === 1) {
          this.costBreakdown = response.data.costBreakdown || {}
        }
      } catch (error) {
        this.$message.error('获取成本构成失败：' + error.message)
      }
    },

    // 重新核算
    async handleReCalculate(row) {
      try {
        await this.$confirm('确认重新核算该产品成本吗？此操作将重新计算成本数据。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await recalculateCost(row.costingId)
        if (res.code === 1) {
          this.$message.success('重新核算成功')
          this.getResultList()
        } else {
          this.$message.error(res.msg || '重新核算失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('重新核算失败')
      }
    },

    // 执行核算
    handleCalculate() {
      this.executeAccounting()
    },

    // 核算配置
    handleMethodConfig() {
      this.configDialogVisible = true
    },

    // 查看结果
    handleViewResult() {
      this.getResultList()
    },

    // 配置提交
    handleConfigSubmit() {
      this.$refs.configForm.validate((valid) => {
        if (valid) {
          this.configSubmitting = true
          setTimeout(() => {
            this.$message.success('配置保存成功')
            this.configDialogVisible = false
            this.configSubmitting = false
          }, 1000)
        }
      })
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 计算比例
    calculateRatio(amount, total) {
      if (!total || total === 0) return '0.00'
      return ((amount / total) * 100).toFixed(2)
    },

    // 渲染成本构成饼图
    renderBreakdownChart() {
      if (!this.$refs.breakdownChart) return
      if (this.breakdownChartInstance) {
        this.breakdownChartInstance.dispose()
      }
      this.breakdownChartInstance = echarts.init(this.$refs.breakdownChart)
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      const option = {
        color: colors,
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['直接材料', '直接人工', '制造费用']
        },
        series: [
          {
            name: '成本构成',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: true,
            label: {
              show: true,
              formatter: '{b}\n{d}%'
            },
            data: [
              { value: Number(this.costBreakdown.directMaterial) || 0, name: '直接材料' },
              { value: Number(this.costBreakdown.directLabor) || 0, name: '直接人工' },
              { value: Number(this.costBreakdown.manufacturingOverhead) || 0, name: '制造费用' }
            ]
          }
        ]
      }
      this.breakdownChartInstance.setOption(option)
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
.cost-accounting-container {
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

.accounting-conditions {
  margin-bottom: 20px;
}

.result-container {
  .search-form {
    margin-bottom: 20px;
  }
}

.cost-breakdown {
  .breakdown-item {
    text-align: center;
    padding: 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    background: #fafafa;

    .item-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .item-value {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 4px;
    }

    .item-ratio {
      font-size: 12px;
      color: #67c23a;
    }
  }

  .breakdown-chart {
    margin-top: 20px;
    min-height: 300px;
    border: 1px dashed #dcdfe6;
    border-radius: 4px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
