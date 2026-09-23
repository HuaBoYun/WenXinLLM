<template>
  <div class="app-container procurement-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-warning-outline"></i><span>虚假贸易核查</span></div>
      <div class="page-header-desc">穿透核查贸易业务"五流合一"，识别空转贸易、融资性贸易等虚假贸易行为</div>
    </div>

    <!-- 搜索卡片 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="核查状态">
          <el-select v-model="queryParams.checkStatus" placeholder="全部" clearable>
            <el-option label="待核查" value="0" />
            <el-option label="核查中" value="1" />
            <el-option label="通过" value="2" />
            <el-option label="异常" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="五流合一状态">
          <el-select v-model="queryParams.fiveFlowStatus" placeholder="全部" clearable>
            <el-option label="合格" value="1" />
            <el-option label="不合格" value="2" />
            <el-option label="部分合格" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格卡片 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>核查记录列表</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="tradeNo" label="贸易编号" width="160" />
        <el-table-column prop="tradeName" label="贸易名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="counterparty" label="交易对手" width="140" />
        <el-table-column prop="contractAmount" label="合同金额（万元）" width="150" align="right" />
        <el-table-column prop="checkStatus" label="核查状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="checkStatusMap[row.checkStatus].type" size="small">{{ checkStatusMap[row.checkStatus].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fiveFlowStatus" label="五流合一状态" width="120" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="fiveFlowMap[row.fiveFlowStatus].type" size="small">{{ fiveFlowMap[row.fiveFlowStatus].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkTime" label="核查时间" width="160" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNo"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="560px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" size="small">
        <el-form-item label="贸易编号" prop="tradeNo">
          <el-input v-model="form.tradeNo" placeholder="请输入贸易编号" />
        </el-form-item>
        <el-form-item label="贸易名称" prop="tradeName">
          <el-input v-model="form.tradeName" placeholder="请输入贸易名称" />
        </el-form-item>
        <el-form-item label="所属企业" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入所属企业" />
        </el-form-item>
        <el-form-item label="交易对手" prop="counterparty">
          <el-input v-model="form.counterparty" placeholder="请输入交易对手" />
        </el-form-item>
        <el-form-item label="合同金额（万元）" prop="contractAmount">
          <el-input-number v-model="form.contractAmount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查状态" prop="checkStatus">
          <el-select v-model="form.checkStatus" placeholder="请选择" style="width:100%">
            <el-option label="待核查" value="0" />
            <el-option label="核查中" value="1" />
            <el-option label="通过" value="2" />
            <el-option label="异常" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="五流合一状态" prop="fiveFlowStatus">
          <el-select v-model="form.fiveFlowStatus" placeholder="请选择" style="width:100%">
            <el-option label="合格" value="1" />
            <el-option label="不合格" value="2" />
            <el-option label="部分合格" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="核查时间" prop="checkTime">
          <el-date-picker v-model="form.checkTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择核查时间" style="width:100%" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="五流核查清单" :visible.sync="detailVisible" width="640px">
      <el-descriptions :column="1" border size="small" v-if="currentRow">
        <el-descriptions-item label="贸易编号">{{ currentRow.tradeNo }}</el-descriptions-item>
        <el-descriptions-item label="贸易名称">{{ currentRow.tradeName }}</el-descriptions-item>
        <el-descriptions-item label="交易对手">{{ currentRow.counterparty }}</el-descriptions-item>
        <el-descriptions-item label="合同金额（万元）">{{ currentRow.contractAmount }}</el-descriptions-item>
      </el-descriptions>
      <div class="five-flow-title">五流核查明细</div>
      <el-table :data="fiveFlowDetail" border size="small" style="margin-top:8px">
        <el-table-column prop="flowName" label="流类型" width="120" align="center" />
        <el-table-column prop="desc" label="核查内容" min-width="200" />
        <el-table-column prop="status" label="核查状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.statusType" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer">
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getFakeTradeList, getFakeTradeDetail, addFakeTrade, updateFakeTrade, deleteFakeTrade } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'ProcurementFakeTrade',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: { pageNo: 1, pageSize: 10, checkStatus: '', fiveFlowStatus: '' },
      dialogVisible: false,
      dialogTitle: '新增',
      detailVisible: false,
      currentRow: null,
      form: { tradeNo: '', tradeName: '', counterparty: '', contractAmount: null, checkStatus: '0', fiveFlowStatus: '', checkTime: '', companyName: '' },
      rules: {
        tradeNo: [{ required: true, message: '请输入贸易编号', trigger: 'blur' }],
        tradeName: [{ required: true, message: '请输入贸易名称', trigger: 'blur' }],
        counterparty: [{ required: true, message: '请输入交易对手', trigger: 'blur' }],
      },
      fiveFlowDetail: [],
      checkStatusMap: { '0': { label: '待核查', type: 'info' }, '1': { label: '核查中', type: 'warning' }, '2': { label: '通过', type: 'success' }, '3': { label: '异常', type: 'danger' } },
      fiveFlowMap: { '1': { label: '合格', type: 'success' }, '2': { label: '不合格', type: 'danger' }, '3': { label: '部分合格', type: 'warning' } },
    }
  },
  created() {
    this.fetchList()
  },
  methods: {
    async fetchList() {
      this.loading = true
      try {
        const res = await getFakeTradeList(this.queryParams)
        if (res && res.result === 200 && res.data) {
          this.tableData = res.data.tlist || res.data.records || res.data.list || []
          this.total = res.data.totalRecord || res.data.total || 0
        }
      } catch (e) {
        console.warn('查询虚假贸易列表失败', e)
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryParams.pageNo = 1; this.fetchList() },
    handleReset() { this.queryParams = { pageNo: 1, pageSize: 10, checkStatus: '', fiveFlowStatus: '' }; this.fetchList() },
    handleAdd() { this.dialogTitle = '新增'; this.form = { tradeNo: '', tradeName: '', counterparty: '', contractAmount: null, checkStatus: '0', fiveFlowStatus: '', checkTime: '', companyName: '' }; this.dialogVisible = true },
    handleEdit(row) { this.dialogTitle = '编辑'; this.form = { ...row }; this.dialogVisible = true },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该记录？', '提示', { type: 'warning' })
        await deleteFakeTrade(row.id)
        this.$message.success('删除成功')
        this.fetchList()
      } catch (e) { /* cancelled */ }
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        try {
          if (this.form.id) { await updateFakeTrade(this.form) }
          else { await addFakeTrade(this.form) }
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.fetchList()
        } catch (e) { this.$message.error('保存失败') }
      })
    },
    async handleDetail(row) {
      this.currentRow = row
      try {
        const res = await getFakeTradeDetail(row.id)
        if (res && res.result === 200 && res.data) {
          this.fiveFlowDetail = res.data.fiveFlowDetail || []
        }
      } catch (e) { console.warn('获取详情失败', e) }
      this.detailVisible = true
    },
    handlePageChange(page) { this.queryParams.pageNo = page; this.fetchList() },
    handleSizeChange(size) { this.queryParams.pageSize = size; this.queryParams.pageNo = 1; this.fetchList() },
    handleCurrentChange(page) { this.queryParams.pageNo = page; this.fetchList() },
  }
}
</script>
<style lang="scss" scoped>
.procurement-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.five-flow-title { font-size: 13px; font-weight: 600; color: #303133; margin: 12px 0 4px; }
.pagination-wrap { margin-top: 12px; text-align: right; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>
