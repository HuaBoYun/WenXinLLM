<template>
  <div class="sales-management-tab">
    <el-card class="overview-card">
      <div slot="header"><span>销售管理概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-money stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.totalSales || 0 }}</div><div class="stat-label">销售额(元)</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-document stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.orderCount || 0 }}</div><div class="stat-label">订单数量</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-user stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.customerCount || 0 }}</div><div class="stat-label">客户数量</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item"><i class="el-icon-success stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.completionRate || 0 }}%</div><div class="stat-label">完成率</div></div></div>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>销售订单管理</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新建订单</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="订单状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px;">
            <el-option label="待确认" value="待确认"></el-option>
            <el-option label="已确认" value="已确认"></el-option>
            <el-option label="生产中" value="生产中"></el-option>
            <el-option label="已发货" value="已发货"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryForm.customerName" placeholder="请输入" clearable style="width: 140px;"></el-input>
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="queryForm.productName" placeholder="请输入" clearable style="width: 140px;"></el-input>
        </el-form-item>
        <el-form-item label="销售员">
          <el-input v-model="queryForm.salesperson" placeholder="请输入" clearable style="width: 120px;"></el-input>
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width: 240px;"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="orderList" border v-loading="loading" style="width: 100%;">
        <el-table-column prop="orderNo" label="订单号" width="140"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="productName" label="产品名称" width="120"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" align="right"><template slot-scope="scope">{{ scope.row.quantity || 0 }} 件</template></el-table-column>
        <el-table-column prop="unitPrice" label="单价(元)" width="100" align="right"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额(元)" width="120" align="right"></el-table-column>
        <el-table-column prop="orderDate" label="下单日期" width="110"></el-table-column>
        <el-table-column prop="deliveryDate" label="交货日期" width="110"></el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '已确认' ? 'primary' : 'warning'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="salesperson" label="销售员" width="80"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="客户名称" prop="customerName" :rules="[{ required: true, message: '请输入客户名称' }]"><el-input v-model="formData.customerName" placeholder="请输入客户名称"></el-input></el-form-item>
        <el-form-item label="产品名称" prop="productName"><el-input v-model="formData.productName" placeholder="请输入产品名称"></el-input></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="数量" prop="quantity"><el-input-number v-model="formData.quantity" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="单价" prop="unitPrice"><el-input-number v-model="formData.unitPrice" :min="0" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="下单日期" prop="orderDate"><el-date-picker v-model="formData.orderDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="交货日期" prop="deliveryDate"><el-date-picker v-model="formData.deliveryDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="待确认" value="待确认"></el-option><el-option label="已确认" value="已确认"></el-option><el-option label="生产中" value="生产中"></el-option><el-option label="已发货" value="已发货"></el-option><el-option label="已完成" value="已完成"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="销售员" prop="salesperson"><el-input v-model="formData.salesperson" placeholder="请输入销售员"></el-input></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getSalesOrderList, addSalesOrder, updateSalesOrder, deleteSalesOrder, getSalesOrderStatistics } from '@/api/enterprise/operation'
export default {
  name: 'SalesManagementTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return {
      loading: false,
      queryForm: { status: '', customerName: '', productName: '', salesperson: '', dateRange: null, pageNumber: 1, pageSize: 20 },
      total: 0, overview: {}, orderList: [],
      dialogVisible: false, dialogTitle: '新增销售订单', isEdit: false,
      formData: { customerName: '', productName: '', quantity: 0, unitPrice: 0, orderDate: '', deliveryDate: '', status: '待确认', salesperson: '', remark: '' }
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
          status: this.queryForm.status,
          customerName: this.queryForm.customerName,
          productName: this.queryForm.productName,
          salesperson: this.queryForm.salesperson,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize
        }
        if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
          params.orderDateBegin = this.queryForm.dateRange[0]
          params.orderDateEnd = this.queryForm.dateRange[1]
        }
        const res = await getSalesOrderList(params)
        const p = this.extractData(res)
        this.orderList = p && p.tlist ? p.tlist : []
        this.total = p && p.totalRecord ? p.totalRecord : 0
      } catch (e) { console.error(e) } finally { this.loading = false }
    },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getSalesOrderStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {} } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { status: '', customerName: '', productName: '', salesperson: '', dateRange: null, pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '新增销售订单'; this.formData = { customerName: '', productName: '', quantity: 0, unitPrice: 0, orderDate: '', deliveryDate: '', status: '待确认', salesperson: '', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, orderNo: 'SO' + Date.now() }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑销售订单'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        try {
          const submitData = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }
          if (submitData.quantity && submitData.unitPrice) submitData.totalAmount = submitData.quantity * submitData.unitPrice
          if (this.isEdit) { await updateSalesOrder(submitData); this.$message.success('更新成功') }
          else { await addSalesOrder(submitData); this.$message.success('新增成功') }
          this.dialogVisible = false; this.loadData(); this.loadStatistics()
        } catch (e) { this.$message.error('操作失败') }
      })
    },
    async handleDelete(row) { try { await this.$confirm('确认删除该销售订单？', '提示', { type: 'warning' }); await deleteSalesOrder(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.sales-management-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
