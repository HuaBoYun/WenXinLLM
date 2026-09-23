<template>
  <div class="concentration-plan-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-date"></i>
            归集计划管理
          </h2>
          <p class="page-description">管理资金归集计划的制定、执行和监控</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增计划
          </el-button>
          <el-button type="success" icon="el-icon-video-play" @click="handleBatchExecute">
            批量执行
          </el-button>
          <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">
            批量删除
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出计划
          </el-button>
        </div>
      </div>
    </div>

    <!-- 计划概览卡片 -->
    <div class="plan-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总计划数</div>
                <div class="card-value">{{ totalPlans }}</div>
                <div class="card-change">个计划</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon executing-icon">
                <i class="el-icon-loading"></i>
              </div>
              <div class="card-info">
                <div class="card-title">执行中</div>
                <div class="card-value">{{ executingPlans }}</div>
                <div class="card-change warning">进行中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon completed-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已完成</div>
                <div class="card-value">{{ completedPlans }}</div>
                <div class="card-change positive">成功率{{ successRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">归集金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="计划名称">
            <el-input
              v-model="listQuery.planName"
              placeholder="请输入计划名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="计划状态">
            <el-select
              v-model="listQuery.planStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待执行" value="PENDING" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已暂停" value="PAUSED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="执行日期">
            <el-date-picker
              v-model="listQuery.executionDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item label="归集策略">
            <el-select
              v-model="listQuery.strategyId"
              placeholder="请选择策略"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="strategy in availableStrategies"
                :key="strategy.strategyId"
                :label="strategy.strategyName"
                :value="strategy.strategyId"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 计划表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="planList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="计划ID" prop="planId" width="80" align="center" />
        <el-table-column label="计划名称" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.planName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="归集策略" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.strategyName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计划金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="plan-amount">{{ formatCurrency(row.planAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="actual-amount">{{ formatCurrency(row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行进度" width="120px" align="center">
          <template slot-scope="{row}">
            <el-progress :percentage="row.executionProgress" :status="getProgressStatus(row.executionProgress)" />
          </template>
        </el-table-column>
        <el-table-column label="计划状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.planStatus)" size="mini">
              {{ getStatusText(row.planStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行时间" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.executionTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.creatorName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="260" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-button v-if="['PENDING', 'PAUSED'].includes(row.planStatus)" size="mini" type="warning" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button v-if="row.planStatus === 'PENDING'" size="mini" type="success" @click="handleExecute(row)">
              执行
            </el-button>
            <el-button v-if="row.planStatus === 'EXECUTING'" size="mini" type="warning" @click="handlePause(row)">
              暂停
            </el-button>
            <el-button v-if="['PENDING', 'PAUSED'].includes(row.planStatus)" size="mini" type="danger" @click="handleCancel(row)">
              取消
            </el-button>
            <el-button v-if="['PENDING', 'PAUSED', 'CANCELLED'].includes(row.planStatus)" size="mini" type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增计划对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="700px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="temp.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归集策略" prop="strategyId">
              <el-select v-model="temp.strategyId" placeholder="请选择归集策略" style="width: 100%;">
                <el-option
                  v-for="strategy in availableStrategies"
                  :key="strategy.strategyId"
                  :label="strategy.strategyName"
                  :value="strategy.strategyId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划金额" prop="planAmount">
              <el-input-number
                v-model="temp.planAmount"
                :precision="2"
                :min="0"
                style="width: 100%;"
                placeholder="请输入计划金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行时间" prop="executionTime">
              <el-date-picker
                v-model="temp.executionTime"
                type="datetime"
                placeholder="选择执行时间"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="计划描述">
          <el-input v-model="temp.planDescription" type="textarea" :rows="3" placeholder="请输入计划描述" />
        </el-form-item>
        <el-form-item label="风险控制">
          <el-checkbox-group v-model="temp.riskControls">
            <el-checkbox label="AMOUNT_CHECK">金额校验</el-checkbox>
            <el-checkbox label="BALANCE_CHECK">余额检查</el-checkbox>
            <el-checkbox label="TIME_LIMIT">时间限制</el-checkbox>
            <el-checkbox label="MANUAL_CONFIRM">人工确认</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="通知设置">
          <el-checkbox-group v-model="temp.notifications">
            <el-checkbox label="EMAIL">邮件通知</el-checkbox>
            <el-checkbox label="SMS">短信通知</el-checkbox>
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 计划详情对话框 -->
    <el-dialog title="归集计划详情" :visible.sync="dialogDetailVisible" width="900px">
      <div v-if="currentPlan" class="plan-detail">
        <!-- 基本信息 -->
        <el-descriptions :column="3" border class="detail-descriptions">
          <el-descriptions-item label="计划名称">{{ currentPlan.planName }}</el-descriptions-item>
          <el-descriptions-item label="归集策略">{{ currentPlan.strategyName }}</el-descriptions-item>
          <el-descriptions-item label="计划状态">
            <el-tag :type="getStatusTagType(currentPlan.planStatus)">
              {{ getStatusText(currentPlan.planStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="计划金额">{{ formatCurrency(currentPlan.planAmount) }}</el-descriptions-item>
          <el-descriptions-item label="实际金额">{{ formatCurrency(currentPlan.actualAmount) }}</el-descriptions-item>
          <el-descriptions-item label="执行进度">{{ currentPlan.executionProgress }}%</el-descriptions-item>
          <el-descriptions-item label="执行时间">{{ currentPlan.executionTime }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ currentPlan.creatorName }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentPlan.createTime }}</el-descriptions-item>
        </el-descriptions>

        <!-- 执行明细 -->
        <div class="execution-details">
          <h4>执行明细</h4>
          <el-table :data="executionDetails" border size="small" max-height="300">
            <el-table-column label="序号" type="index" width="60" />
            <el-table-column label="源账户" prop="sourceAccountName" width="150" />
            <el-table-column label="目标账户" prop="targetAccountName" width="150" />
            <el-table-column label="归集金额" prop="amount" width="120" align="right">
              <template slot-scope="{row}">
                <span>{{ formatCurrency(row.amount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="执行状态" prop="status" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="getExecutionStatusTagType(row.status)" size="mini">
                  {{ getExecutionStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="执行时间" prop="executionTime" width="150" />
            <el-table-column label="备注" prop="remark" min-width="100" />
          </el-table>
        </div>

        <!-- 计划描述 -->
        <div style="margin-top: 20px;">
          <h4>计划描述</h4>
          <p>{{ currentPlan.planDescription }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentPlan && currentPlan.planStatus === 'PENDING'" type="success" @click="handleExecute(currentPlan)">
          执行计划
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getConcentrationPlanPage, createConcentrationPlan, updateConcentrationPlan, executeConcentrationPlan, pauseConcentrationPlan, cancelConcentrationPlan, batchExecuteConcentrationPlan, getFundConcentrationStrategyPage, deleteConcentrationPlan, batchDeleteConcentrationPlan, exportConcentrationPlan } from '@/api/globalTreasurer/zjjz'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConcentrationPlanManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        planName: undefined,
        planStatus: undefined,
        executionDateRange: undefined,
        strategyId: undefined
      },
      totalPlans: 0,
      executingPlans: 0,
      completedPlans: 0,
      successRate: 0,
      totalAmount: 0,
      planList: [],
      multipleSelection: [],
      availableStrategies: [],
      currentPlan: null,
      executionDetails: [],
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      temp: {
        planId: undefined,
        planName: '',
        strategyId: undefined,
        planAmount: 0,
        executionTime: null,
        planDescription: '',
        riskControls: [],
        notifications: []
      },
      rules: {
        planName: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }],
        strategyId: [{ required: true, message: '请选择归集策略', trigger: 'change' }],
        planAmount: [{ required: true, message: '请输入计划金额', trigger: 'blur' }],
        executionTime: [{ required: true, message: '请选择执行时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadAvailableStrategies()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        console.log('========== listQuery ==========')
        console.log('listQuery:', JSON.stringify(this.listQuery))

        let executionDateRange = null
        if (this.listQuery.executionDateRange && this.listQuery.executionDateRange.length === 2) {
          // 格式化日期为 yyyy-MM-dd 格式
          const formatDate = (date) => {
            const d = new Date(date)
            const year = d.getFullYear()
            const month = String(d.getMonth() + 1).padStart(2, '0')
            const day = String(d.getDate()).padStart(2, '0')
            return `${year}-${month}-${day}`
          }
          executionDateRange = formatDate(this.listQuery.executionDateRange[0]) + ',' + formatDate(this.listQuery.executionDateRange[1])
        }

        const params = {
          pageNo: this.listQuery.page,
          pageSize: this.listQuery.limit,
          planName: this.listQuery.planName || undefined,
          planStatus: this.listQuery.planStatus || undefined,
          strategyId: this.listQuery.strategyId || undefined,
          executionDateRange: executionDateRange
        }
        console.log('发送的查询参数:', JSON.stringify(params))

        const res = await getConcentrationPlanPage(params)
        console.log('查询响应:', JSON.stringify(res))

        if (res.code === 1) {
          this.planList = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          // 更新统计数据
          this.totalPlans = this.total
          this.executingPlans = this.planList.filter(p => p.planStatus === 'EXECUTING').length
          this.completedPlans = this.planList.filter(p => p.planStatus === 'COMPLETED').length
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载计划列表失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.listLoading = false
      }
    },
    async loadAvailableStrategies() {
      try {
        const res = await getFundConcentrationStrategyPage({ pageNo: 1, pageSize: 100 })
        if (res.code === 1) {
          this.availableStrategies = res.data.tlist || []
        }
      } catch (error) {
        console.error('加载策略列表失败:', error)
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        planName: undefined,
        planStatus: undefined,
        executionDateRange: undefined,
        strategyId: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增归集计划'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentPlan = row
      this.loadExecutionDetails(row.planId)
      this.dialogDetailVisible = true
    },
    async handleExecute(row) {
      this.$confirm('确认执行该归集计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await executeConcentrationPlan(row.planId)
          if (res.code === 1) {
            this.$message.success('归集计划已开始执行')
            this.getList()
          } else {
            this.$message.error(res.msg || '执行失败')
          }
        } catch (error) {
          this.$message.error('执行失败')
        }
      })
    },
    async handlePause(row) {
      this.$confirm('确认暂停该归集计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await pauseConcentrationPlan(row.planId)
          if (res.code === 1) {
            this.$message.success('归集计划已暂停')
            this.getList()
          } else {
            this.$message.error(res.msg || '暂停失败')
          }
        } catch (error) {
          this.$message.error('暂停失败')
        }
      })
    },
    async handleCancel(row) {
      this.$confirm('确认取消该归集计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await cancelConcentrationPlan(row.planId)
          if (res.code === 1) {
            this.$message.success('归集计划已取消')
            this.getList()
          } else {
            this.$message.error(res.msg || '取消失败')
          }
        } catch (error) {
          this.$message.error('取消失败')
        }
      })
    },
    async handleBatchExecute() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要执行的计划')
        return
      }
      this.$confirm(`确认批量执行选中的${this.multipleSelection.length}个计划?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const planIds = this.multipleSelection.map(p => p.planId)
          const res = await batchExecuteConcentrationPlan(planIds)
          if (res.code === 1) {
            this.$message.success('批量执行成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '批量执行失败')
          }
        } catch (error) {
          this.$message.error('批量执行失败')
        }
      })
    },
    handleExport() {
      const params = {
        planName: this.listQuery.planName || undefined,
        planStatus: this.listQuery.planStatus || undefined,
        strategyId: this.listQuery.strategyId || undefined
      }
      exportConcentrationPlan(params).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '归集计划_' + new Date().getTime() + '.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.$message.error('导出失败')
      })
    },
    handleEdit(row) {
      this.temp = {
        planId: row.planId,
        planName: row.planName,
        strategyId: row.strategyId,
        planAmount: row.planAmount,
        executionTime: row.executionTime,
        planDescription: row.planDescription,
        // 如果是字符串，转换为数组
        riskControls: typeof row.riskControls === 'string' ? row.riskControls.split(',').filter(s => s.trim()) : (row.riskControls || []),
        notifications: typeof row.notifications === 'string' ? row.notifications.split(',').filter(s => s.trim()) : (row.notifications || [])
      }
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑归集计划'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该归集计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteConcentrationPlan(row.planId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的计划')
        return
      }
      this.$confirm(`确认批量删除选中的 ${this.multipleSelection.length} 个计划?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const planIds = this.multipleSelection.map(p => p.planId)
          const res = await batchDeleteConcentrationPlan(planIds)
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除失败')
        }
      })
    },
    async createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            // 将数组转换为字符串传递给后端
            const dataToSave = {
              ...this.temp,
              riskControls: Array.isArray(this.temp.riskControls) ? this.temp.riskControls.join(',') : this.temp.riskControls,
              notifications: Array.isArray(this.temp.notifications) ? this.temp.notifications.join(',') : this.temp.notifications
            }
            console.log('创建的数据:', JSON.stringify(dataToSave))

            const res = await createConcentrationPlan(dataToSave)
            if (res.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('归集计划创建成功')
              this.getList()
            } else {
              this.$message.error(res.msg || '创建失败')
            }
          } catch (error) {
            this.$message.error('创建失败')
          }
        }
      })
    },
    async updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            console.log('========== 更新归集计划 ==========')
            console.log('更新前的temp:', JSON.stringify(this.temp))

            // 将数组转换为字符串传递给后端
            const dataToSave = {
              ...this.temp,
              riskControls: Array.isArray(this.temp.riskControls) ? this.temp.riskControls.join(',') : this.temp.riskControls,
              notifications: Array.isArray(this.temp.notifications) ? this.temp.notifications.join(',') : this.temp.notifications
            }
            console.log('保存的数据:', JSON.stringify(dataToSave))

            const res = await updateConcentrationPlan(dataToSave)
            console.log('更新响应:', JSON.stringify(res))
            console.log('响应code:', res.code)
            if (res.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('归集计划更新成功')
              this.getList()
            } else {
              console.error('更新失败，响应:', res)
              this.$message.error(res.msg || '更新失败')
            }
          } catch (error) {
            console.error('更新异常:', error)
            this.$message.error('更新失败')
          }
        }
      })
    },
    resetTemp() {
      this.temp = {
        planId: undefined,
        planName: '',
        strategyId: undefined,
        planAmount: 0,
        executionTime: null,
        planDescription: '',
        riskControls: [],
        notifications: []
      }
    },
    loadExecutionDetails(planId) {
      // TODO: 调用实际的API接口
      this.executionDetails = []
    },
    simulateExecution(row) {
      // 模拟执行进度
      const interval = setInterval(() => {
        if (row.executionProgress < 100) {
          row.executionProgress += Math.floor(Math.random() * 20) + 5
          if (row.executionProgress > 100) {
            row.executionProgress = 100
          }
          row.actualAmount = (row.planAmount * row.executionProgress / 100)
        } else {
          row.planStatus = 'COMPLETED'
          clearInterval(interval)
        }
      }, 1000)
    },
    getProgressStatus(progress) {
      if (progress === 100) return 'success'
      if (progress >= 80) return 'warning'
      return null
    },
    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'EXECUTING': 'warning',
        'COMPLETED': 'success',
        'PAUSED': 'warning',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '待执行',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    getExecutionStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'EXECUTING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getExecutionStatusText(status) {
      const textMap = {
        'PENDING': '待执行',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'FAILED': '失败'
      }
      return textMap[status] || status
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      if (isNaN(date.getTime())) return dateStr
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.concentration-plan-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .plan-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.executing-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.completed-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .plan-amount, .actual-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .plan-detail {
    .detail-descriptions {
      margin-bottom: 20px;
    }
    
    .execution-details {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
      }
    }
    
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
