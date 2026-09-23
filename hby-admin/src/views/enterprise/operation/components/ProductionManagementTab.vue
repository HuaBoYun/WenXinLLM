<template>
  <div class="production-management-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>生产管理概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <i class="el-icon-document stat-icon" style="color: #409EFF"></i>
            <div class="stat-info"><div class="stat-value">{{ overview.totalPlans || 0 }}</div><div class="stat-label">生产计划数</div></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <i class="el-icon-success stat-icon" style="color: #67C23A"></i>
            <div class="stat-info"><div class="stat-value">{{ overview.completionRate || 0 }}%</div><div class="stat-label">完成率</div></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <i class="el-icon-data-analysis stat-icon" style="color: #E6A23C"></i>
            <div class="stat-info"><div class="stat-value">{{ overview.capacityUtilization || 0 }}%</div><div class="stat-label">产能利用率</div></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <i class="el-icon-star-on stat-icon" style="color: #F56C6C"></i>
            <div class="stat-info"><div class="stat-value">{{ overview.qualityRate || 0 }}%</div><div class="stat-label">质量合格率</div></div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <div slot="header">
        <span>生产计划管理</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新建计划</el-button>
        </div>
      </div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="计划名称">
          <el-input v-model="queryForm.planName" placeholder="请输入" clearable style="width: 150px;"></el-input>
        </el-form-item>
        <el-form-item label="计划类型">
          <el-select v-model="queryForm.planType" placeholder="请选择" clearable style="width: 120px;">
            <el-option label="日计划" value="日计划"></el-option>
            <el-option label="月计划" value="月计划"></el-option>
            <el-option label="季度计划" value="季度计划"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px;">
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="待开始" value="待开始"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 240px;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="planList" border v-loading="loading" style="width: 100%;">
        <el-table-column prop="planName" label="计划名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="planType" label="计划类型" width="100" align="center"></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="120"></el-table-column>
        <el-table-column prop="productionLine" label="生产线" width="100"></el-table-column>
        <el-table-column prop="plannedQuantity" label="计划产量" width="100" align="right">
          <template slot-scope="scope">{{ scope.row.plannedQuantity || 0 }} 件</template>
        </el-table-column>
        <el-table-column prop="actualQuantity" label="实际产量" width="100" align="right">
          <template slot-scope="scope">{{ scope.row.actualQuantity || 0 }} 件</template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="120" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="Number(scope.row.completionRate) || 0" :stroke-width="8"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="110"></el-table-column>
        <el-table-column prop="manager" label="负责人" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'primary' : 'info'" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="计划名称" prop="planName" :rules="[{ required: true, message: '请输入计划名称' }]">
          <el-input v-model="formData.planName" placeholder="请输入计划名称"></el-input>
        </el-form-item>
        <el-form-item label="计划类型" prop="planType">
          <el-select v-model="formData.planType" placeholder="请选择" style="width: 100%;">
            <el-option label="日计划" value="日计划"></el-option>
            <el-option label="月计划" value="月计划"></el-option>
            <el-option label="季度计划" value="季度计划"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称"></el-input>
        </el-form-item>
        <el-form-item label="生产线" prop="productionLine">
          <el-input v-model="formData.productionLine" placeholder="请输入生产线"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划产量" prop="plannedQuantity">
              <el-input-number v-model="formData.plannedQuantity" :min="0" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际产量" prop="actualQuantity">
              <el-input-number v-model="formData.actualQuantity" :min="0" style="width: 100%;"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="formData.endDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择" style="width: 100%;">
            <el-option label="待开始" value="待开始"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="manager">
          <el-input v-model="formData.manager" placeholder="请输入负责人"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getProductionPlanList, addProductionPlan, updateProductionPlan, deleteProductionPlan, getProductionPlanStatistics } from '@/api/enterprise/operation'

export default {
  name: 'ProductionManagementTab',
  props: {
    enterpriseId: { type: String, default: '' },
    enterpriseName: { type: String, default: '' }
  },
  data() {
    return {
      loading: false,
      queryForm: { planName: '', planType: '', status: '', dateRange: null, pageNumber: 1, pageSize: 20 },
      total: 0,
      overview: {},
      planList: [],
      dialogVisible: false,
      dialogTitle: '新增生产计划',
      isEdit: false,
      formData: { planName: '', planType: '', productName: '', productionLine: '', plannedQuantity: 0, actualQuantity: 0, startDate: '', endDate: '', status: '待开始', manager: '', remark: '' }
    }
  },
  watch: {
    enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics() } }, immediate: true }
  },
  methods: {
    extractData(response) {
      if (!response) return null
      const resData = response.data || response
      return resData && resData.pageInfo ? resData.pageInfo : resData
    },
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        // 构建查询参数，处理时间范围
        const params = {
          enterpriseId: this.enterpriseId,
          planType: this.queryForm.planType,
          status: this.queryForm.status,
          planName: this.queryForm.planName,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize
        }
        if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
          params.startDateBegin = this.queryForm.dateRange[0]
          params.startDateEnd = this.queryForm.dateRange[1]
        }
        const res = await getProductionPlanList(params)
        const pageData = this.extractData(res)
        this.planList = pageData && pageData.tlist ? pageData.tlist : []
        this.total = pageData && pageData.totalRecord ? pageData.totalRecord : 0
      } catch (e) { console.error('加载生产计划失败:', e) } finally { this.loading = false }
    },
    async loadStatistics() {
      if (!this.enterpriseId) return
      try {
        const res = await getProductionPlanStatistics({ enterpriseId: this.enterpriseId })
        this.overview = this.extractData(res) || {}
      } catch (e) { console.error('加载统计失败:', e) }
    },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { planName: '', planType: '', status: '', dateRange: null, pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() {
      this.isEdit = false; this.dialogTitle = '新增生产计划'
      this.formData = { planName: '', planType: '', productName: '', productionLine: '', plannedQuantity: 0, actualQuantity: 0, startDate: '', endDate: '', status: '待开始', manager: '', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true; this.dialogTitle = '编辑生产计划'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        try {
          const submitData = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }
          if (submitData.plannedQuantity > 0) {
            submitData.completionRate = Math.round((submitData.actualQuantity || 0) / submitData.plannedQuantity * 100 * 100) / 100
          }
          if (this.isEdit) {
            await updateProductionPlan(submitData)
            this.$message.success('更新成功')
          } else {
            await addProductionPlan(submitData)
            this.$message.success('新增成功')
          }
          this.dialogVisible = false
          this.loadData()
          this.loadStatistics()
        } catch (e) { this.$message.error('操作失败：' + (e.message || '未知错误')) }
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该生产计划？', '提示', { type: 'warning' })
        await deleteProductionPlan(row.id)
        this.$message.success('删除成功')
        this.loadData()
        this.loadStatistics()
      } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') }
    }
  }
}
</script>

<style scoped>
.production-management-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
