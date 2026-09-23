<template>
  <div class="cost-control-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="控制期间" prop="controlPeriod">
          <el-date-picker
            v-model="searchForm.controlPeriod"
            type="month"
            placeholder="请选择控制期间"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="成本中心" prop="costCenterId">
          <el-select
            v-model="searchForm.costCenterId"
            placeholder="请选择成本中心"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="控制类型" prop="controlType">
          <el-select
            v-model="searchForm.controlType"
            placeholder="请选择控制类型"
            clearable
            style="width: 150px"
          >
            <el-option label="预算控制" :value="1" />
            <el-option label="限额控制" :value="2" />
            <el-option label="审批控制" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态" prop="warningStatus">
          <el-select
            v-model="searchForm.warningStatus"
            placeholder="请选择预警状态"
            clearable
            style="width: 120px"
          >
            <el-option label="正常" :value="1" />
            <el-option label="预警" :value="2" />
            <el-option label="超支" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAddControl">新增控制规则</el-button>
      <el-button type="success" @click="handleBatchEnable" :disabled="!multipleSelection.length">
        批量启用
      </el-button>
      <el-button type="warning" @click="handleWarningSettings">预警设置</el-button>
      <el-button type="info" @click="handleControlReport">控制报告</el-button>
      <el-button type="danger" @click="handleBatchDisable" :disabled="!multipleSelection.length">
        批量停用
      </el-button>
    </div>

    <!-- 控制概览 -->
    <div class="control-overview">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="overview-card normal-centers">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-title">正常中心</div>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.normalCenters }}</div>
              <div class="card-desc">成本控制正常</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="overview-card warning-centers">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-title">预警中心</div>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.warningCenters }}</div>
              <div class="card-desc">需要关注</div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="overview-card exceed-centers">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-close"></i>
              </div>
              <div class="card-title">超支中心</div>
            </div>
            <div class="card-content">
              <div class="card-value">{{ overview.exceedCenters }}</div>
              <div class="card-desc">需要紧急处理</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="controlNo" label="控制编号" width="150" />
        <el-table-column prop="costCenterName" label="成本中心" min-width="180" show-overflow-tooltip />
        <el-table-column prop="controlTypeName" label="控制类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getControlTypeTag(scope.row.controlType)">
              {{ scope.row.controlTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="usageRate" label="使用率" width="120" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.usageRate" 
              :color="getProgressColor(scope.row.usageRate)"
              :stroke-width="8"
            />
          </template>
        </el-table-column>
        <el-table-column prop="warningStatusName" label="预警状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getWarningStatusTag(scope.row.warningStatus)">
              {{ scope.row.warningStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
              {{ scope.row.isEnabled === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled === 1 ? 'danger' : 'success'" 
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '停用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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

    <!-- 新增/编辑控制规则对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="控制编号" prop="controlNo">
              <el-input v-model="formData.controlNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成本中心" prop="costCenterId">
              <el-select v-model="formData.costCenterId" placeholder="请选择成本中心" filterable style="width: 100%">
                <el-option
                  v-for="item in costCenterOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="控制类型" prop="controlType">
              <el-select v-model="formData.controlType" placeholder="请选择控制类型" style="width: 100%">
                <el-option label="预算控制" :value="1" />
                <el-option label="限额控制" :value="2" />
                <el-option label="审批控制" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制金额" prop="controlAmount">
              <el-input-number
                v-model="formData.controlAmount"
                placeholder="请输入控制金额"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警阈值" prop="warningThreshold">
              <el-input-number
                v-model="formData.warningThreshold"
                placeholder="请输入预警阈值(%)"
                :precision="0"
                :min="0"
                :max="100"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制策略" prop="controlStrategy">
              <el-select v-model="formData.controlStrategy" placeholder="请选择控制策略" style="width: 100%">
                <el-option label="严格控制" :value="1" />
                <el-option label="预警提醒" :value="2" />
                <el-option label="记录超支" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="控制说明" prop="controlDesc">
          <el-input
            v-model="formData.controlDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入控制说明"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-radio-group v-model="formData.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostControlPage,
  saveOrUpdateCostControl,
  updateCostControlStatus,
  batchUpdateCostControlStatus,
  getCostControlOverview,
  getCostCenterPage
} from '@/api/financialSharing/costCenter'

export default {
  name: 'CostControl',
  data() {
    return {
      loading: false,
      searchForm: {
        controlPeriod: '',
        costCenterId: '',
        controlType: '',
        warningStatus: ''
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      overview: {
        normalCenters: 0,
        warningCenters: 0,
        exceedCenters: 0
      },
      // 成本中心下拉数据，搜索 + 表单两处共用
      costCenterOptions: [],
      dialogVisible: false,
      dialogTitle: '',
      formData: {
        controlId: null,
        controlNo: '',
        costCenterId: '',
        controlType: 1,
        controlAmount: 0,
        warningThreshold: 80,
        controlStrategy: 1,
        controlDesc: '',
        isEnabled: 1
      },
      formRules: {
        costCenterId: [
          { required: true, message: '请选择成本中心', trigger: 'change' }
        ],
        controlType: [
          { required: true, message: '请选择控制类型', trigger: 'change' }
        ],
        controlAmount: [
          { required: true, message: '请输入控制金额', trigger: 'blur' }
        ],
        warningThreshold: [
          { required: true, message: '请输入预警阈值', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadCostCenterOptions()
    this.loadData()
    this.loadOverview()
  },
  methods: {
    /**
     * 加载成本中心下拉选项（搜索 + 表单共用）
     */
    async loadCostCenterOptions() {
      try {
        const res = await getCostCenterPage({ pageNum: 1, pageSize: 999 })
        if (res.code === 1) {
          const list = (res.data && (res.data.tlist || res.data.list)) || []
          this.costCenterOptions = list.map(item => ({
            id: String(item.centerId || item.CENTER_ID || item.id),
            name: item.centerName || item.CENTER_NAME || item.name || '',
            code: item.centerCode || item.CENTER_CODE || ''
          }))
        }
      } catch (error) {
        console.error('加载成本中心列表失败:', error)
      }
    },
    /**
     * 把后端 Map 风格的 key 统一成前端小驼峰。
     * 兼容场景：
     *   1) CONTROL_NO（达梦无双引号 alias 的标准输出）→ controlNo
     *   2) controlNo（已经是驼峰）→ 原样保留
     * 该工具仅本组件使用，避免污染全局 utils。
     */
    normalizeKeys(row) {
      if (!row || typeof row !== 'object') return row
      const out = {}
      Object.keys(row).forEach(key => {
        let camel = key
        if (/[A-Z]/.test(key) && key.includes('_')) {
          // SNAKE_CASE → camelCase
          camel = key.toLowerCase().replace(/_([a-z0-9])/g, (_, c) => c.toUpperCase())
        }
        out[camel] = row[key]
      })
      return out
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const res = await getCostControlPage(params)
        if (res.code === 1) {
          const list = res.data.tlist || res.data.list || []
          this.tableData = list.map(this.normalizeKeys)
          this.pagination.total = res.data.totalRecord || res.data.total || 0
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadOverview() {
      try {
        const res = await getCostControlOverview()
        if (res.code === 1) {
          // overview 已是驼峰，仍走 normalize 增强容错
          const data = res.data ? this.normalizeKeys(res.data) : {}
          this.overview = {
            normalCenters: data.normalCenters || 0,
            warningCenters: data.warningCenters || 0,
            exceedCenters: data.exceedCenters || 0
          }
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAddControl() {
      this.dialogTitle = '新增控制规则'
      this.formData = {
        controlId: null,
        controlNo: '',
        costCenterId: '',
        controlType: 1,
        controlAmount: 0,
        warningThreshold: 80,
        controlStrategy: 1,
        controlDesc: '',
        isEnabled: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑控制规则'
      // mapper 返回字段大部分已是小驼峰，但 controlAmount 字段在列表 SQL 里 alias 成了 budgetAmount，
      // 这里做一次兼容映射，避免编辑时金额回填 0
      const pick = (...keys) => {
        for (const k of keys) {
          if (row[k] !== undefined && row[k] !== null && row[k] !== '') return row[k]
        }
        return undefined
      }
      this.formData = {
        controlId: pick('controlId', 'CONTROL_ID') || null,
        controlNo: pick('controlNo', 'CONTROL_NO') || '',
        costCenterId: pick('costCenterId', 'centerId', 'CENTER_ID')
          ? String(pick('costCenterId', 'centerId', 'CENTER_ID'))
          : '',
        controlType: Number(pick('controlType', 'CONTROL_TYPE') || 1),
        controlAmount: Number(pick('controlAmount', 'budgetAmount', 'CONTROL_AMOUNT') || 0),
        warningThreshold: Number(pick('warningThreshold', 'WARNING_THRESHOLD') || 80),
        controlStrategy: Number(pick('controlStrategy', 'CONTROL_STRATEGY') || 1),
        controlDesc: pick('controlDesc', 'CONTROL_DESC') || '',
        isEnabled: Number(pick('isEnabled', 'IS_ENABLED') !== undefined
          ? pick('isEnabled', 'IS_ENABLED')
          : 1)
      }
      this.dialogVisible = true
    },
    handleView(row) {
      const pick = (...keys) => {
        for (const k of keys) {
          if (row[k] !== undefined && row[k] !== null && row[k] !== '') return row[k]
        }
        return ''
      }
      const typeMap = { 1: '预算控制', 2: '限额控制', 3: '审批控制' }
      const statusMap = { 1: '正常', 2: '预警', 3: '超支' }
      const strategyMap = { 1: '严格控制', 2: '预警提醒', 3: '记录超支' }
      const ctrlType = Number(pick('controlType')) || ''
      const warnStatus = Number(pick('warningStatus')) || ''
      const strategy = Number(pick('controlStrategy')) || ''
      const isEnabled = Number(pick('isEnabled'))
      const content = `
        <p><b>控制规则编号：</b>${pick('controlNo', 'controlId') || '-'}</p>
        <p><b>成本中心：</b>${pick('costCenterName') || '-'}</p>
        <p><b>控制类型：</b>${typeMap[ctrlType] || pick('controlTypeName') || '-'}</p>
        <p><b>控制金额：</b>${pick('controlAmount', 'budgetAmount') || 0}</p>
        <p><b>实际金额：</b>${pick('actualAmount') || 0}</p>
        <p><b>使用率：</b>${pick('usageRate') || 0}%</p>
        <p><b>预警阈值：</b>${pick('warningThreshold') || 0}%</p>
        <p><b>控制策略：</b>${strategyMap[strategy] || '-'}</p>
        <p><b>预警状态：</b>${statusMap[warnStatus] || pick('warningStatusName') || '-'}</p>
        <p><b>控制说明：</b>${pick('controlDesc') || '-'}</p>
        <p><b>状态：</b>${isEnabled === 1 ? '启用' : '停用'}</p>
      `
      this.$alert(content, '控制规则详情', { dangerouslyUseHTMLString: true })
    },
    handleToggleStatus(row) {
      const action = row.isEnabled === 1 ? '停用' : '启用'
      const newStatus = row.isEnabled === 1 ? 0 : 1
      this.$confirm(`确认${action}该控制规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await updateCostControlStatus(row.controlId, newStatus)
          if (res.code === 1) {
            this.$message.success(`${action}成功`)
            this.loadData()
          } else {
            this.$message.error(res.msg || `${action}失败`)
          }
        } catch (error) {
          this.$message.error(`${action}失败`)
        }
      }).catch(() => {})
    },
    handleBatchEnable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要启用的控制规则')
        return
      }
      this.$confirm(`确认批量启用选中的${this.multipleSelection.length}条控制规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const controlIds = this.multipleSelection.map(item => String(item.controlId))
          const res = await batchUpdateCostControlStatus(controlIds, 1)
          if (res.code === 1) {
            const succ = (res.data && res.data.successCount) || controlIds.length
            this.$message.success(`批量启用成功（${succ} 条）`)
            this.loadData()
            this.loadOverview()
          } else {
            this.$message.error(res.msg || '批量启用失败')
          }
        } catch (error) {
          this.$message.error('批量启用失败')
        }
      }).catch(() => {})
    },
    handleBatchDisable() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要停用的控制规则')
        return
      }
      this.$confirm(`确认批量停用选中的${this.multipleSelection.length}条控制规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const controlIds = this.multipleSelection.map(item => String(item.controlId))
          const res = await batchUpdateCostControlStatus(controlIds, 0)
          if (res.code === 1) {
            const succ = (res.data && res.data.successCount) || controlIds.length
            this.$message.success(`批量停用成功（${succ} 条）`)
            this.loadData()
            this.loadOverview()
          } else {
            this.$message.error(res.msg || '批量停用失败')
          }
        } catch (error) {
          this.$message.error('批量停用失败')
        }
      }).catch(() => {})
    },
    handleWarningSettings() {
      const content = `
        <p><b>当前预警配置：</b></p>
        <p>预警阈值：预算执行率超过 80% 时预警</p>
        <p>严重阈值：预算执行率超过 100% 时严重预警</p>
        <p>通知方式：系统消息通知</p>
        <p style="margin-top:10px;color:#909399;">如需调整预警规则，请在控制规则列表中编辑对应规则的阈值</p>
      `
      this.$alert(content, '预警设置', { dangerouslyUseHTMLString: true })
    },
    async handleControlReport() {
      try {
        const res = await getCostControlOverview(this.searchForm || {})
        if (res.code === 1) {
          const data = res.data || {}
          const content = `
            <p><b>正常中心数：</b>${data.normalCenters || 0}</p>
            <p><b>预警中心数：</b>${data.warningCenters || 0}</p>
            <p><b>超标中心数：</b>${data.exceedCenters || 0}</p>
            <p><b>整体执行率：</b>${data.overallExecutionRate || 0}%</p>
            <p><b>总预算：</b>${data.totalBudget || 0}</p>
            <p><b>总实际：</b>${data.totalActual || 0}</p>
          `
          this.$alert(content, '成本控制报告', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(res.msg || '获取报告失败')
        }
      } catch (error) {
        this.$message.error('获取控制报告失败')
      }
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          try {
            const res = await saveOrUpdateCostControl(this.formData)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
              this.loadOverview()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    getControlTypeTag(type) {
      const tagMap = {
        1: 'primary',
        2: 'success',
        3: 'warning'
      }
      return tagMap[type] || 'info'
    },
    getWarningStatusTag(status) {
      const tagMap = {
        1: 'success',
        2: 'warning',
        3: 'danger'
      }
      return tagMap[status] || 'info'
    },
    getProgressColor(percentage) {
      if (percentage < 70) return '#67C23A'
      if (percentage < 90) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-control-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
  
  .el-button {
    margin-right: 10px;
  }
}

.control-overview {
  margin-bottom: 20px;
  
  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    
    .card-header {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      
      .card-icon {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        
        i {
          font-size: 20px;
          color: white;
        }
      }
      
      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }
    
    .card-content {
      .card-value {
        font-size: 32px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }
      
      .card-desc {
        font-size: 14px;
        color: #909399;
      }
    }
    
    &.normal-centers .card-icon {
      background: linear-gradient(135deg, #67C23A 0%, #85CE61 100%);
    }
    
    &.warning-centers .card-icon {
      background: linear-gradient(135deg, #E6A23C 0%, #F0A020 100%);
    }
    
    &.exceed-centers .card-icon {
      background: linear-gradient(135deg, #F56C6C 0%, #F78989 100%);
    }
  }
}

.table-container {
  background: white;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.dialog-footer {
  text-align: right;
}
</style>
