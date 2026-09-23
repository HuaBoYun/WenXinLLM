<template>
  <div class="supply-chain-tab">
    <el-card class="overview-card">
      <div slot="header"><span>供应链管理概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-office-building stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.supplierCount || 0 }}</div><div class="stat-label">供应商数量</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-money stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.purchaseAmount || 0 }}</div><div class="stat-label">采购金额(元)</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-refresh stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.inventoryTurnover || 0 }}次</div><div class="stat-label">库存周转率</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-time stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.onTimeDelivery || 0 }}%</div><div class="stat-label">准时交付率</div></div></div>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>供应商管理</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新增供应商</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="供应商名称">
          <el-input v-model="queryForm.supplierName" placeholder="请输入" clearable style="width: 140px;"></el-input>
        </el-form-item>
        <el-form-item label="供应商类型">
          <el-select v-model="queryForm.supplierType" placeholder="请选择" clearable style="width: 130px;">
            <el-option label="原材料供应商" value="原材料供应商"></el-option>
            <el-option label="设备供应商" value="设备供应商"></el-option>
            <el-option label="服务供应商" value="服务供应商"></el-option>
            <el-option label="物流供应商" value="物流供应商"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="合作状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px;">
            <el-option label="正常合作" value="正常合作"></el-option>
            <el-option label="暂停合作" value="暂停合作"></el-option>
            <el-option label="终止合作" value="终止合作"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width: 100px;">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入驻时间">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width: 240px;"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="supplierList" border v-loading="loading">
        <el-table-column prop="supplierName" label="供应商名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="supplierType" label="类型" width="120" align="center"></el-table-column>
        <el-table-column prop="cooperationYears" label="合作年限" width="90" align="center"><template slot-scope="scope">{{ scope.row.cooperationYears || 0 }} 年</template></el-table-column>
        <el-table-column prop="supplyAmount" label="供应金额(万)" width="120" align="right"></el-table-column>
        <el-table-column prop="qualityScore" label="质量评分" width="90" align="center"><template slot-scope="scope"><span :style="{color: scope.row.qualityScore >= 90 ? '#67C23A' : scope.row.qualityScore >= 80 ? '#409EFF' : '#E6A23C', fontWeight: 'bold'}">{{ scope.row.qualityScore || 0 }}</span></template></el-table-column>
        <el-table-column prop="deliveryScore" label="交付评分" width="90" align="center"><template slot-scope="scope"><span :style="{color: scope.row.deliveryScore >= 90 ? '#67C23A' : scope.row.deliveryScore >= 80 ? '#409EFF' : '#E6A23C', fontWeight: 'bold'}">{{ scope.row.deliveryScore || 0 }}</span></template></el-table-column>
        <el-table-column prop="overallScore" label="综合评分" width="90" align="center"><template slot-scope="scope"><span :style="{color: scope.row.overallScore >= 90 ? '#67C23A' : scope.row.overallScore >= 80 ? '#409EFF' : '#E6A23C', fontWeight: 'bold'}">{{ scope.row.overallScore || 0 }}</span></template></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="90" align="center"><template slot-scope="scope"><el-tag :type="scope.row.riskLevel === 'LOW' ? 'success' : scope.row.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ scope.row.riskLevel === 'LOW' ? '低' : scope.row.riskLevel === 'MEDIUM' ? '中' : '高' }}</el-tag></template></el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '正常合作' ? 'success' : scope.row.status === '暂停合作' ? 'warning' : 'danger'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="供应商名称" prop="supplierName" :rules="[{ required: true, message: '请输入供应商名称' }]"><el-input v-model="formData.supplierName" placeholder="请输入供应商名称"></el-input></el-form-item>
        <el-form-item label="供应商类型" prop="supplierType"><el-select v-model="formData.supplierType" style="width: 100%;"><el-option label="原材料供应商" value="原材料供应商"></el-option><el-option label="设备供应商" value="设备供应商"></el-option><el-option label="服务供应商" value="服务供应商"></el-option><el-option label="物流供应商" value="物流供应商"></el-option></el-select></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合作年限" prop="cooperationYears"><el-input-number v-model="formData.cooperationYears" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="供应金额" prop="supplyAmount"><el-input-number v-model="formData.supplyAmount" :min="0" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="质量评分" prop="qualityScore"><el-input-number v-model="formData.qualityScore" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="交付评分" prop="deliveryScore"><el-input-number v-model="formData.deliveryScore" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="价格评分" prop="priceScore"><el-input-number v-model="formData.priceScore" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="风险等级" prop="riskLevel"><el-select v-model="formData.riskLevel" style="width: 100%;"><el-option label="低" value="LOW"></el-option><el-option label="中" value="MEDIUM"></el-option><el-option label="高" value="HIGH"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合作状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="正常合作" value="正常合作"></el-option><el-option label="暂停合作" value="暂停合作"></el-option><el-option label="终止合作" value="终止合作"></el-option></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getSupplyList, addSupply, updateSupply, deleteSupply, getSupplyStatistics } from '@/api/enterprise/operation'
export default {
  name: 'SupplyChainTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return {
      loading: false,
      queryForm: { supplierName: '', supplierType: '', status: '', riskLevel: '', dateRange: null, pageNumber: 1, pageSize: 20 },
      total: 0, overview: {}, supplierList: [],
      dialogVisible: false, dialogTitle: '新增供应商', isEdit: false,
      formData: { supplierName: '', supplierType: '', cooperationYears: 0, supplyAmount: 0, qualityScore: 0, deliveryScore: 0, priceScore: 0, riskLevel: 'LOW', status: '正常合作', remark: '' }
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics() } }, immediate: true } },
  methods: {
    extractData(response) { if (!response) return null; const d = response.data || response; return d && d.pageInfo ? d.pageInfo : d },
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const params = {
          enterpriseId: this.enterpriseId,
          supplierName: this.queryForm.supplierName,
          supplierType: this.queryForm.supplierType,
          status: this.queryForm.status,
          riskLevel: this.queryForm.riskLevel,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize
        }
        if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
          params.createTimeBegin = this.queryForm.dateRange[0]
          params.createTimeEnd = this.queryForm.dateRange[1]
        }
        const res = await getSupplyList(params)
        const p = this.extractData(res)
        this.supplierList = p && p.tlist ? p.tlist : []
        this.total = p && p.totalRecord ? p.totalRecord : 0
      } catch (e) { console.error(e) } finally { this.loading = false }
    },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getSupplyStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {} } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { supplierName: '', supplierType: '', status: '', riskLevel: '', dateRange: null, pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '新增供应商'; this.formData = { supplierName: '', supplierType: '', cooperationYears: 0, supplyAmount: 0, qualityScore: 0, deliveryScore: 0, priceScore: 0, overallScore: 0, riskLevel: 'LOW', status: '正常合作', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑供应商'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        try {
          const submitData = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }
          const q = submitData.qualityScore || 0; const d = submitData.deliveryScore || 0; const p = submitData.priceScore || 0
          submitData.overallScore = Math.round((q + d + p) / 3 * 100) / 100
          if (this.isEdit) { await updateSupply(submitData); this.$message.success('更新成功') }
          else { await addSupply(submitData); this.$message.success('新增成功') }
          this.dialogVisible = false; this.loadData(); this.loadStatistics()
        } catch (e) { this.$message.error('操作失败') }
      })
    },
    async handleDelete(row) { try { await this.$confirm('确认删除该供应商？', '提示', { type: 'warning' }); await deleteSupply(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.supply-chain-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
