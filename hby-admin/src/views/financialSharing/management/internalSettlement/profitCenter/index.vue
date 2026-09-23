<template>
  <div class="profit-center-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-trophy"></i>
          利润中心管理
        </h1>
        <p class="page-description">管理利润中心的收入成本归集、利润计算分析和绩效考核评价</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新建利润中心
        </el-button>
        <el-button type="success" icon="el-icon-data-analysis" @click="profitAnalysis">
          利润分析
        </el-button>
        <el-button type="warning" icon="el-icon-star-on" @click="performanceEvaluation">
          绩效评价
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon total-centers">
            <i class="el-icon-office-building"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalCenters }}</div>
            <div class="stat-label">利润中心数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon total-revenue">
            <i class="el-icon-coin"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.totalRevenue) }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon total-profit">
            <i class="el-icon-trophy"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ formatAmount(stats.totalProfit) }}</div>
            <div class="stat-label">总利润</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon avg-margin">
            <i class="el-icon-trend-charts"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.avgMargin }}%</div>
            <div class="stat-label">平均利润率</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="中心编码">
          <el-input v-model="queryForm.centerCode" placeholder="请输入中心编码" clearable />
        </el-form-item>
        <el-form-item label="中心名称">
          <el-input v-model="queryForm.centerName" placeholder="请输入中心名称" clearable />
        </el-form-item>
        <el-form-item label="中心类型">
          <el-select v-model="queryForm.centerType" placeholder="请选择中心类型" clearable>
            <el-option label="销售利润中心" :value="1" />
            <el-option label="生产利润中心" :value="2" />
            <el-option label="服务利润中心" :value="3" />
            <el-option label="投资利润中心" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="中心状态">
          <el-select v-model="queryForm.centerStatus" placeholder="请选择中心状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="暂停" :value="2" />
            <el-option label="关闭" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="centerList"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="centerCode" label="中心编码" width="120" />
        <el-table-column prop="centerName" label="中心名称" width="180" />
        <el-table-column prop="centerType" label="中心类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeTag(scope.row.centerType)">
              {{ getTypeName(scope.row.centerType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="managerName" label="负责人" width="100" />
        <el-table-column prop="currentRevenue" label="本期收入" width="120" align="right">
          <template slot-scope="scope">
            <span class="revenue-text">{{ formatAmount(scope.row.currentRevenue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="currentCost" label="本期成本" width="120" align="right">
          <template slot-scope="scope">
            <span class="cost-text">{{ formatAmount(scope.row.currentCost) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="currentProfit" label="本期利润" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.currentProfit >= 0 ? 'profit-positive' : 'profit-negative'">
              {{ formatAmount(scope.row.currentProfit) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="profitMargin" label="利润率" width="80" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.profitMargin >= 0 ? 'margin-positive' : 'margin-negative'">
              {{ scope.row.profitMargin }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="centerStatus" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.centerStatus)">
              {{ getStatusName(scope.row.centerStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleAnalysis(scope.row)">分析</el-button>
            <el-button size="mini" type="text" @click="handleEvaluation(scope.row)">评价</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right;"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="formData" :rules="formRules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="中心编码" prop="centerCode">
              <el-input v-model="formData.centerCode" placeholder="系统自动生成" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中心名称" prop="centerName">
              <el-input v-model="formData.centerName" placeholder="请输入中心名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="中心类型" prop="centerType">
              <el-select v-model="formData.centerType" placeholder="请选择中心类型">
                <el-option label="销售利润中心" :value="1" />
                <el-option label="生产利润中心" :value="2" />
                <el-option label="服务利润中心" :value="3" />
                <el-option label="投资利润中心" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="managerName">
              <el-input v-model="formData.managerName" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上级中心" prop="parentCenterId">
              <el-select v-model="formData.parentCenterId" placeholder="请选择上级中心" clearable>
                <el-option
                  v-for="center in parentCenterList"
                  :key="center.centerId"
                  :label="center.centerName"
                  :value="center.centerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成立日期" prop="establishDate">
              <el-date-picker
                v-model="formData.establishDate"
                type="date"
                placeholder="请选择成立日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业务范围" prop="businessScope">
          <el-input
            v-model="formData.businessScope"
            type="textarea"
            :rows="3"
            placeholder="请输入业务范围描述"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getProfitCenterList, createProfitCenter, updateProfitCenter, deleteProfitCenter, getProfitCenterStats, getProfitCenterTree, getProfitCenterDetail, analyzeProfitCenter, evaluateCenterPerformance } from '@/api/financialSharing/internalSettlement'

export default {
  name: 'ProfitCenter',
  data() {
    return {
      loading: false,
      submitLoading: false,
      centerList: [],
      selectedRows: [],
      parentCenterList: [],
      stats: {
        totalCenters: 0,
        totalRevenue: 0,
        totalProfit: 0,
        avgMargin: 0
      },
      queryForm: {
        centerCode: '',
        centerName: '',
        centerType: '',
        centerStatus: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      formData: {
        centerId: null,
        centerCode: '',
        centerName: '',
        centerType: '',
        managerName: '',
        parentCenterId: '',
        establishDate: '',
        businessScope: '',
        remark: ''
      },
      formRules: {
        centerName: [{ required: true, message: '请输入中心名称', trigger: 'blur' }],
        centerType: [{ required: true, message: '请选择中心类型', trigger: 'change' }],
        managerName: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        establishDate: [{ required: true, message: '请选择成立日期', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
    this.loadStats()
    this.loadParentCenterList()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        const response = await getProfitCenterList(params)
        if (response.code === 1) {
          this.centerList = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        const response = await getProfitCenterStats()
        if (response.code === 1) {
          this.stats = response.data || {
            totalCenters: 0,
            totalRevenue: 0,
            totalProfit: 0,
            avgMargin: 0
          }
        }
      } catch (error) {
        console.error('获取利润中心统计数据失败', error)
      }
    },
    async loadParentCenterList() {
      try {
        const response = await getProfitCenterTree()
        if (response.code === 1) {
          this.parentCenterList = response.data || []
        }
      } catch (error) {
        console.error('获取上级中心列表失败', error)
      }
    },
    handleQuery() {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        centerCode: '',
        centerName: '',
        centerType: '',
        centerStatus: ''
      }
      this.handleQuery()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.dialogTitle = '新建利润中心'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑利润中心'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        const response = await getProfitCenterDetail(row.centerId)
        if (response.code === 1) {
          const data = response.data || row
          const content = `
            <p><b>中心编号：</b>${data.centerCode || ''}</p>
            <p><b>中心名称：</b>${data.centerName || ''}</p>
            <p><b>上级中心：</b>${data.parentCenterName || ''}</p>
            <p><b>负责人：</b>${data.manager || ''}</p>
            <p><b>状态：</b>${data.statusName || (data.isEnabled === 1 ? '启用' : '停用')}</p>
            <p><b>创建时间：</b>${data.createTime || ''}</p>
            <p><b>备注：</b>${data.remark || ''}</p>
          `
          this.$alert(content, '利润中心详情', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        const content = `
          <p><b>中心名称：</b>${row.centerName || ''}</p>
          <p><b>中心编号：</b>${row.centerCode || ''}</p>
        `
        this.$alert(content, '利润中心详情', { dangerouslyUseHTMLString: true })
      }
    },
    async handleAnalysis(row) {
      try {
        const response = await analyzeProfitCenter(row.centerId, {})
        if (response.code === 1) {
          const data = response.data || {}
          const content = `
            <p><b>中心名称：</b>${row.centerName || ''}</p>
            <p><b>收入合计：</b>${data.totalRevenue || 0}</p>
            <p><b>成本合计：</b>${data.totalCost || 0}</p>
            <p><b>利润：</b>${data.profit || 0}</p>
            <p><b>利润率：</b>${data.profitRate || 0}%</p>
          `
          this.$alert(content, '利润分析结果', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '分析失败')
        }
      } catch (error) {
        this.$message.error('利润分析失败')
      }
    },
    async handleEvaluation(row) {
      try {
        const response = await evaluateCenterPerformance(row.centerId, {})
        if (response.code === 1) {
          const data = response.data || {}
          const content = `
            <p><b>中心名称：</b>${row.centerName || ''}</p>
            <p><b>绩效评分：</b>${data.score || '--'}</p>
            <p><b>评价等级：</b>${data.grade || '--'}</p>
            <p><b>营收达成率：</b>${data.revenueRate || '--'}%</p>
            <p><b>成本控制率：</b>${data.costControlRate || '--'}%</p>
          `
          this.$alert(content, '绩效评价结果', { dangerouslyUseHTMLString: true })
        } else {
          this.$message.error(response.msg || '评价失败')
        }
      } catch (error) {
        this.$message.error('绩效评价失败')
      }
    },
    profitAnalysis() {
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.handleAnalysis(this.multipleSelection[0])
      } else {
        this.$message.warning('请选择一个利润中心进行分析')
      }
    },
    performanceEvaluation() {
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        this.handleEvaluation(this.multipleSelection[0])
      } else {
        this.$message.warning('请选择一个利润中心进行评价')
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        const response = this.isEdit 
          ? await updateProfitCenter(this.formData)
          : await createProfitCenter(this.formData)
          
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.fetchData()
        }
      } catch (error) {
        this.$message.error(this.isEdit ? '更新失败' : '创建失败')
      } finally {
        this.submitLoading = false
      }
    },
    resetForm() {
      this.formData = {
        centerId: null,
        centerCode: '',
        centerName: '',
        centerType: '',
        managerName: '',
        parentCenterId: '',
        establishDate: '',
        businessScope: '',
        remark: ''
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    formatAmount(amount) {
      return amount ? `¥${(amount / 10000).toFixed(2)}万` : '¥0.00万'
    },
    getTypeName(type) {
      const types = {
        1: '销售利润中心',
        2: '生产利润中心',
        3: '服务利润中心',
        4: '投资利润中心'
      }
      return types[type] || '未知'
    },
    getTypeTag(type) {
      const tags = {
        1: 'success',
        2: 'primary',
        3: 'warning',
        4: 'info'
      }
      return tags[type] || ''
    },
    getStatusName(status) {
      const statuses = {
        1: '正常',
        2: '暂停',
        3: '关闭'
      }
      return statuses[status] || '未知'
    },
    getStatusTag(status) {
      const tags = {
        1: 'success',
        2: 'warning',
        3: 'danger'
      }
      return tags[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.profit-center-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #e6a23c;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 8px;
    }
  }
}

.stats-row {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }

      &.total-centers {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-revenue {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.total-profit {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
      }

      &.avg-margin {
        background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-card, .table-card {
  margin-bottom: 20px;
}

.revenue-text {
  font-weight: 600;
  color: #67c23a;
}

.cost-text {
  font-weight: 600;
  color: #e6a23c;
}

.profit-positive {
  font-weight: 600;
  color: #67c23a;
}

.profit-negative {
  font-weight: 600;
  color: #f56c6c;
}

.margin-positive {
  font-weight: 600;
  color: #67c23a;
}

.margin-negative {
  font-weight: 600;
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}
</style>
