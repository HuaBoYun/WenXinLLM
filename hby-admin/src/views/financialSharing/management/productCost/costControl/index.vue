<template>
  <div class="cost-control-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-warning"></i>
          成本控制
        </h1>
        <p class="page-description">管理成本预算、监控预警、控制措施等成本控制业务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAddBudget">
          新增预算
        </el-button>
        <el-button type="success" icon="el-icon-bell" @click="handleAlertConfig">
          预警配置
        </el-button>
        <el-button type="warning" icon="el-icon-view" @click="handleControlReport">
          控制报告
        </el-button>
      </div>
    </div>

    <!-- 控制概览 -->
    <div class="control-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="overview-card budget-card">
            <div class="card-icon">
              <i class="el-icon-coin"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overviewData.totalBudget) }}</div>
              <div class="card-label">总预算</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card actual-card">
            <div class="card-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ formatAmount(overviewData.actualCost) }}</div>
              <div class="card-label">实际成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card variance-card">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-content">
              <div class="card-value" :class="overviewData.variance >= 0 ? 'text-danger' : 'text-success'">
                {{ formatAmount(overviewData.variance) }}
              </div>
              <div class="card-label">预算差异</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="overview-card execution-card">
            <div class="card-icon">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overviewData.executionRate }}%</div>
              <div class="card-label">预算执行率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能标签页 -->
    <div class="control-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 预算管理 -->
        <el-tab-pane label="预算管理" name="budget">
          <div class="budget-management">
            <!-- 查询条件 -->
            <el-form :model="budgetQuery" ref="budgetQuery" :inline="true" class="search-form">
              <el-form-item label="产品名称" prop="productName">
                <el-input v-model="budgetQuery.productName" placeholder="请输入产品名称" clearable />
              </el-form-item>
              <el-form-item label="预算期间" prop="budgetPeriod">
                <el-date-picker
                  v-model="budgetQuery.budgetPeriod"
                  type="month"
                  placeholder="选择预算期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  clearable
                />
              </el-form-item>
              <el-form-item label="预算状态" prop="budgetStatus">
                <el-select v-model="budgetQuery.budgetStatus" placeholder="请选择预算状态" clearable>
                  <el-option label="草稿" value="1" />
                  <el-option label="审批中" value="2" />
                  <el-option label="已批准" value="3" />
                  <el-option label="已执行" value="4" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleBudgetQuery">查询</el-button>
                <el-button icon="el-icon-refresh" @click="resetBudgetQuery">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 预算列表 -->
            <el-table
              v-loading="budgetLoading"
              :data="budgetList"
              row-key="budgetId"
              border
              stripe
            >
              <el-table-column label="产品名称" prop="productName" min-width="150" />
              <el-table-column label="预算期间" prop="budgetPeriod" width="100" />
              <el-table-column label="预算金额" prop="budgetAmount" width="120" align="right">
                <template slot-scope="scope">
                  <span>{{ formatAmount(scope.row.budgetAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="实际金额" prop="actualAmount" width="120" align="right">
                <template slot-scope="scope">
                  <span>{{ formatAmount(scope.row.actualAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="差异金额" prop="varianceAmount" width="120" align="right">
                <template slot-scope="scope">
                  <span :class="scope.row.varianceAmount >= 0 ? 'text-danger' : 'text-success'">
                    {{ formatAmount(scope.row.varianceAmount) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="执行率" prop="executionRate" width="100" align="right">
                <template slot-scope="scope">
                  <span>{{ scope.row.executionRate }}%</span>
                </template>
              </el-table-column>
              <el-table-column label="预算状态" prop="budgetStatus" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getBudgetStatusType(scope.row.budgetStatus)">
                    {{ getBudgetStatusName(scope.row.budgetStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleViewBudget(scope.row)">查看</el-button>
                  <el-button size="mini" type="text" @click="handleEditBudget(scope.row)">编辑</el-button>
                  <el-button size="mini" type="text" @click="handleAdjustBudget(scope.row)">调整</el-button>
                  <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDeleteBudget(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 监控预警 -->
        <el-tab-pane label="监控预警" name="monitoring">
          <div class="monitoring-alerts">
            <!-- 预警规则配置 -->
            <el-card title="预警规则" class="alert-rules-card">
              <div slot="header">
                <span>预警规则</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddAlertRule">
                  新增规则
                </el-button>
              </div>
              <el-table :data="alertRules" border stripe>
                <el-table-column label="规则名称" prop="ruleName" min-width="150" />
                <el-table-column label="预警类型" prop="alertType" width="120" />
                <el-table-column label="预警条件" prop="alertCondition" min-width="200" />
                <el-table-column label="预警阈值" prop="threshold" width="100" align="right" />
                <el-table-column label="启用状态" prop="isEnabled" width="100" align="center">
                  <template slot-scope="scope">
                    <el-switch v-model="scope.row.isEnabled" @change="handleRuleStatusChange(scope.row)" />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="handleEditRule(scope.row)">编辑</el-button>
                    <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDeleteRule(scope.row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>

            <!-- 预警信息 -->
            <el-card title="预警信息" class="alert-info-card">
              <div slot="header">
                <span>预警信息</span>
                <div style="float: right">
                  <el-button size="small" type="text" @click="refreshAlerts">刷新</el-button>
                </div>
              </div>
              <el-table :data="alertList" border stripe>
                <el-table-column label="预警时间" prop="alertTime" width="150" />
                <el-table-column label="产品名称" prop="productName" min-width="150" />
                <el-table-column label="预警类型" prop="alertType" width="120" />
                <el-table-column label="预警级别" prop="alertLevel" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getAlertLevelType(scope.row.alertLevel)">
                      {{ scope.row.alertLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="预警内容" prop="alertContent" min-width="200" />
                <el-table-column label="处理状态" prop="handleStatus" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.handleStatus === '已处理' ? 'success' : 'warning'">
                      {{ scope.row.handleStatus }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="handleProcessAlert(scope.row)">处理</el-button>
                    <el-button size="mini" type="text" @click="handleViewAlert(scope.row)">详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 控制措施 -->
        <el-tab-pane label="控制措施" name="measures">
          <div class="control-measures">
            <el-card title="控制措施">
              <div slot="header">
                <span>控制措施</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddMeasure">
                  新增措施
                </el-button>
              </div>
              <el-table :data="measureList" border stripe>
                <el-table-column label="措施名称" prop="measureName" min-width="150" />
                <el-table-column label="措施类型" prop="measureType" width="120" />
                <el-table-column label="适用产品" prop="applicableProducts" min-width="150" />
                <el-table-column label="预期效果" prop="expectedEffect" min-width="200" />
                <el-table-column label="实施状态" prop="implementStatus" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getImplementStatusType(scope.row.implementStatus)">
                      {{ scope.row.implementStatus }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="创建时间" prop="createTime" width="150" />
                <el-table-column label="操作" width="150" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="handleViewMeasure(scope.row)">查看</el-button>
                    <el-button size="mini" type="text" @click="handleEditMeasure(scope.row)">编辑</el-button>
                    <el-button size="mini" type="text" @click="handleImplementMeasure(scope.row)">实施</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 异常处理 -->
        <el-tab-pane label="异常处理" name="exceptions">
          <div class="exception-handling">
            <el-card title="异常成本处理">
              <div slot="header">
                <span>异常成本处理</span>
              </div>
              <el-table :data="exceptionList" border stripe>
                <el-table-column label="异常时间" prop="exceptionTime" width="150" />
                <el-table-column label="产品名称" prop="productName" min-width="150" />
                <el-table-column label="异常类型" prop="exceptionType" width="120" />
                <el-table-column label="异常金额" prop="exceptionAmount" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="text-danger">{{ formatAmount(scope.row.exceptionAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="异常原因" prop="exceptionReason" min-width="200" />
                <el-table-column label="处理状态" prop="handleStatus" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.handleStatus === '已处理' ? 'success' : 'danger'">
                      {{ scope.row.handleStatus }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="handleProcessException(scope.row)">处理</el-button>
                    <el-button size="mini" type="text" @click="handleViewException(scope.row)">详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { getCostControlPage, getBudgetExecution, getCostAlerts, getCostControlEffectiveness, getCostAnomalies, saveOrUpdateCostControl, setCostAlertRules, executeCostControlMeasures } from '@/api/financialSharing/productCost'

export default {
  name: 'CostControl',
  data() {
    return {
      activeTab: 'budget',
      budgetLoading: false,
      overviewData: {
        totalBudget: 0,
        actualCost: 0,
        variance: 0,
        executionRate: 0
      },
      budgetQuery: {
        productName: '',
        budgetPeriod: '',
        budgetStatus: ''
      },
      budgetList: [],
      alertRules: [],
      alertList: [],
      measureList: [],
      exceptionList: []
    }
  },
  created() {
    this.loadOverviewData()
    this.getBudgetList()
    this.getAlertList()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        const response = await getCostControlEffectiveness({})
        if (response.code === 1) {
          const data = response.data || {}
          this.overviewData = {
            totalBudget: data.totalBudget || 0,
            actualCost: data.actualCost || 0,
            variance: data.variance || 0,
            executionRate: data.executionRate || 0
          }
        }
      } catch (error) {
        console.error('获取概览数据失败：', error)
      }
    },

    // 获取预警列表
    async getAlertList() {
      try {
        const response = await getCostAlerts({})
        if (response.code === 1) {
          const data = response.data || {}
          this.alertRules = data.alertRules || []
          this.alertList = data.alertList || []
          this.measureList = data.measureList || []
          this.exceptionList = data.exceptionList || []
        }
      } catch (error) {
        console.error('获取预警数据失败：', error)
      }
    },

    // 获取预算列表
    async getBudgetList() {
      this.budgetLoading = true
      try {
        const response = await getBudgetExecution(this.budgetQuery)
        if (response.code === 1) {
          this.budgetList = response.data.tlist || []
        }
      } catch (error) {
        console.error('获取预算列表失败：', error)
      } finally {
        this.budgetLoading = false
      }
    },

    // 标签页点击
    handleTabClick(tab) {
      this.activeTab = tab.name
    },

    // 预算查询
    handleBudgetQuery() {
      this.getBudgetList()
    },

    // 重置预算查询
    resetBudgetQuery() {
      this.$refs.budgetQuery.resetFields()
      this.getBudgetList()
    },

    // 新增预算
    async handleAddBudget() {
      try {
        const { value } = await this.$prompt('请输入预算名称', '新增预算', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '预算名称'
        })
        if (!value) {
          this.$message.warning('预算名称不能为空')
          return
        }
        const res = await saveOrUpdateCostControl({ budgetName: value })
        if (res.code === 1) {
          this.$message.success('新增预算成功')
          this.getBudgetList()
        } else {
          this.$message.error(res.msg || '新增预算失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 查看预算
    handleViewBudget(row) {
      const content = `
        <p><b>预算名称:</b> ${row.budgetName || '-'}</p>
        <p><b>产品名称:</b> ${row.productName || '-'}</p>
        <p><b>预算期间:</b> ${row.budgetPeriod || '-'}</p>
        <p><b>预算金额:</b> ${row.budgetAmount || 0}</p>
        <p><b>实际金额:</b> ${row.actualAmount || 0}</p>
        <p><b>执行率:</b> ${row.executionRate || 0}%</p>
        <p><b>状态:</b> ${this.getBudgetStatusName(row.budgetStatus)}</p>
      `
      this.$alert(content, '预算详情', { dangerouslyUseHTMLString: true })
    },

    // 编辑预算
    async handleEditBudget(row) {
      try {
        const { value } = await this.$prompt('请输入新的预算名称', '编辑预算', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.budgetName || '',
          inputPlaceholder: '预算名称'
        })
        if (!value) {
          this.$message.warning('预算名称不能为空')
          return
        }
        const res = await saveOrUpdateCostControl({ ...row, budgetName: value })
        if (res.code === 1) {
          this.$message.success('编辑预算成功')
          this.getBudgetList()
        } else {
          this.$message.error(res.msg || '编辑预算失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 调整预算
    async handleAdjustBudget(row) {
      try {
        const { value } = await this.$prompt('请输入调整金额（正数增加，负数减少）', '调整预算', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '调整金额',
          inputPattern: /^-?[0-9]+(\.[0-9]{1,2})?$/,
          inputErrorMessage: '请输入有效的金额'
        })
        const res = await saveOrUpdateCostControl({ ...row, adjustmentAmount: Number(value), adjusted: true })
        if (res.code === 1) {
          this.$message.success('预算调整成功')
          this.getBudgetList()
        } else {
          this.$message.error(res.msg || '预算调整失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 删除预算
    handleDeleteBudget(row) {
      this.$confirm('确认删除该预算吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      })
    },

    // 预警配置
    handleAlertConfig() {
      this.activeTab = 'monitoring'
    },

    // 控制报告
    async handleControlReport() {
      try {
        const res = await getCostControlEffectiveness({})
        if (res.code === 1) {
          const data = res.data || {}
          const content = `
            <p><b>成本控制总体评价：</b>${data.overallRating || '--'}</p>
            <p><b>预算执行率：</b>${data.executionRate || '--'}%</p>
            <p><b>超标项目数：</b>${data.exceedCount || 0}</p>
            <p><b>节约金额：</b>${data.savingAmount || 0}</p>
            <p><b>改进建议：</b>${data.suggestion || '暂无'}</p>
          `
          this.$alert(content, '成本控制报告', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(res.msg || '获取控制报告失败')
        }
      } catch (error) {
        this.$message.error('获取控制报告失败')
      }
    },

    // 新增预警规则
    async handleAddAlertRule() {
      try {
        const { value } = await this.$prompt('请输入预警规则名称', '新增预警规则', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        if (!value) return
        const res = await setCostAlertRules({ ruleName: value, threshold: 100, enabled: true })
        if (res.code === 1) {
          this.$message.success('预警规则创建成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '创建失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 编辑规则
    async handleEditRule(row) {
      try {
        const { value } = await this.$prompt('请输入预警阈值(%)', '编辑规则', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: String(row.threshold || 100),
          inputPattern: /^[0-9]+(\.[0-9]{1,2})?$/,
          inputErrorMessage: '请输入有效数字'
        })
        const res = await setCostAlertRules({ ...row, threshold: Number(value) })
        if (res.code === 1) {
          this.$message.success('规则更新成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '更新失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 删除规则
    async handleDeleteRule(row) {
      try {
        await this.$confirm('确认删除该预警规则？', '删除确认', { type: 'warning' })
        const res = await setCostAlertRules({ ...row, deleted: true })
        if (res.code === 1) {
          this.$message.success('规则删除成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 规则状态变化
    handleRuleStatusChange(row) {
      this.$message.success('规则状态更新成功')
    },

    // 刷新预警
    refreshAlerts() {
      this.getAlertList()
    },

    // 处理预警
    async handleProcessAlert(row) {
      try {
        await this.$confirm('确认处理该预警？将标记为已处理。', '预警处理', { type: 'warning' })
        const res = await executeCostControlMeasures({ alertId: row.alertId, action: 'process' })
        if (res.code === 1) {
          this.$message.success('预警处理成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '处理失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 查看预警
    handleViewAlert(row) {
      const content = `
        <p><b>预警类型：</b>${row.alertType || '-'}</p>
        <p><b>预警级别：</b>${row.level || '-'}</p>
        <p><b>预警内容：</b>${row.description || row.content || '-'}</p>
        <p><b>涉及产品：</b>${row.productName || '-'}</p>
        <p><b>预警金额：</b>${row.amount || 0}</p>
        <p><b>触发时间：</b>${row.alertTime || row.createTime || '-'}</p>
        <p><b>状态：</b>${row.status || '-'}</p>
      `
      this.$alert(content, '预警详情', { dangerouslyUseHTMLString: true })
    },

    // 新增措施
    async handleAddMeasure() {
      try {
        const { value } = await this.$prompt('请输入控制措施描述', '新增控制措施', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        if (!value) return
        const res = await executeCostControlMeasures({ measureName: value, action: 'create' })
        if (res.code === 1) {
          this.$message.success('措施创建成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '创建失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 查看措施
    handleViewMeasure(row) {
      const content = `
        <p><b>措施名称：</b>${row.measureName || '-'}</p>
        <p><b>措施描述：</b>${row.description || '-'}</p>
        <p><b>目标效果：</b>${row.expectedEffect || '-'}</p>
        <p><b>负责人：</b>${row.responsible || '-'}</p>
        <p><b>实施状态：</b>${row.status || '-'}</p>
        <p><b>创建时间：</b>${row.createTime || '-'}</p>
      `
      this.$alert(content, '控制措施详情', { dangerouslyUseHTMLString: true })
    },

    // 编辑措施
    async handleEditMeasure(row) {
      try {
        const { value } = await this.$prompt('请输入措施描述', '编辑措施', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.description || row.measureName || ''
        })
        if (!value) return
        const res = await executeCostControlMeasures({ ...row, description: value, action: 'update' })
        if (res.code === 1) {
          this.$message.success('措施更新成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '更新失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 实施措施
    async handleImplementMeasure(row) {
      try {
        await this.$confirm('确认实施该控制措施？', '实施确认', { type: 'warning' })
        const res = await executeCostControlMeasures({ ...row, status: 'implementing', action: 'implement' })
        if (res.code === 1) {
          this.$message.success('措施已开始实施')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 处理异常
    async handleProcessException(row) {
      try {
        await this.$confirm('确认处理该异常？', '异常处理', { type: 'warning' })
        const res = await executeCostControlMeasures({ exceptionId: row.exceptionId, action: 'processException' })
        if (res.code === 1) {
          this.$message.success('异常处理成功')
          this.getAlertList()
        } else {
          this.$message.error(res.msg || '处理失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },

    // 查看异常
    handleViewException(row) {
      const content = `
        <p><b>异常类型：</b>${row.exceptionType || '-'}</p>
        <p><b>异常描述：</b>${row.description || '-'}</p>
        <p><b>涉及产品：</b>${row.productName || '-'}</p>
        <p><b>异常金额：</b>${row.amount || 0}</p>
        <p><b>发现时间：</b>${row.discoveryTime || row.createTime || '-'}</p>
        <p><b>状态：</b>${row.status || '-'}</p>
      `
      this.$alert(content, '异常详情', { dangerouslyUseHTMLString: true })
    },

    // 获取预算状态类型
    getBudgetStatusType(status) {
      const types = { 1: '', 2: 'warning', 3: 'success', 4: 'info' }
      return types[status] || ''
    },

    // 获取预算状态名称
    getBudgetStatusName(status) {
      const names = { 1: '草稿', 2: '审批中', 3: '已批准', 4: '已执行' }
      return names[status] || '未知'
    },

    // 获取预警级别类型
    getAlertLevelType(level) {
      const types = { '低': 'info', '中': 'warning', '高': 'danger' }
      return types[level] || 'info'
    },

    // 获取实施状态类型
    getImplementStatusType(status) {
      const types = { '计划中': 'info', '实施中': 'warning', '已完成': 'success', '已暂停': 'danger' }
      return types[status] || 'info'
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
.cost-control-container {
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

.control-overview {
  margin-bottom: 20px;

  .overview-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .card-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }
    }

    .card-content {
      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .card-label {
        font-size: 14px;
        color: #909399;
      }
    }

    &.budget-card .card-icon {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.actual-card .card-icon {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.variance-card .card-icon {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.execution-card .card-icon {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
  }
}

.control-tabs {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }

  .alert-rules-card,
  .alert-info-card {
    margin-bottom: 20px;
  }
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}
</style>
