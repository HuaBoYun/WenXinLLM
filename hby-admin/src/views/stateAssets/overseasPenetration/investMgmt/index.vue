<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-money"></i><span>境外投资管理</span></div>
      <div class="page-header-desc">管理境外投资项目审批、国别分布与投资回报分析</div>
    </div>

    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="6" v-for="(card, idx) in statCards" :key="idx">
        <el-card shadow="hover" :body-style="{ padding: '16px' }">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap" :style="{ background: card.bg }"><i :class="card.icon" :style="{ color: card.color }"></i></div>
            <div class="stat-info"><div class="stat-value">{{ card.value }}</div><div class="stat-label">{{ card.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width: 180px" /></el-form-item>
        <el-form-item label="投资国家"><el-input v-model="queryForm.country" placeholder="请输入" clearable style="width: 150px" /></el-form-item>
        <el-form-item label="投资类型"><el-select v-model="queryForm.investType" placeholder="请选择" clearable style="width: 120px"><el-option label="新设" value="NEW" /><el-option label="并购" value="MA" /><el-option label="增资" value="INCREASE" /></el-select></el-form-item>
        <el-form-item label="审批状态"><el-select v-model="queryForm.approvalStatus" placeholder="请选择" clearable style="width: 120px"><el-option label="已批准" value="APPROVED" /><el-option label="审批中" value="PENDING" /><el-option label="已驳回" value="REJECTED" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border @selection-change="val => multipleSelection = val" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="100" align="center" />
        <el-table-column label="投资类型" width="80" align="center">
          <template slot-scope="scope"><el-tag size="small">{{ { NEW: '新设', MA: '并购', INCREASE: '增资' }[scope.row.investType] || scope.row.investType }}</el-tag></template>
        </el-table-column>
        <el-table-column label="投资金额(万$)" prop="investAmount" width="130" align="right" />
        <el-table-column label="持股比例(%)" prop="shareholdingRatio" width="100" align="center" />
        <el-table-column label="审批状态" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="{ APPROVED: 'success', PENDING: 'warning', REJECTED: 'danger' }[scope.row.approvalStatus]" size="small">{{ { APPROVED: '已批准', PENDING: '审批中', REJECTED: '已驳回' }[scope.row.approvalStatus] || '-' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="预期收益率" width="100" align="center">
          <template slot-scope="scope"><span>{{ scope.row.expectedReturn != null ? scope.row.expectedReturn + '%' : '-' }}</span></template>
        </el-table-column>
        <el-table-column label="实际收益率" width="100" align="center">
          <template slot-scope="scope"><span :style="{ color: scope.row.actualReturn != null ? (scope.row.actualReturn >= 0 ? '#52C41A' : '#F5222D') : '' }">{{ scope.row.actualReturn != null ? scope.row.actualReturn + '%' : '-' }}</span></template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="{ add: '新增境外投资', edit: '编辑境外投资' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="企业名称" prop="unitName"><el-input v-model="form.unitName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="所在国家" prop="country"><el-input v-model="form.country" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="投资类型" prop="investType"><el-select v-model="form.investType" style="width:100%"><el-option label="新设" value="NEW" /><el-option label="并购" value="MA" /><el-option label="增资" value="INCREASE" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="投资金额(万$)" prop="investAmount"><el-input-number v-model="form.investAmount" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="持股比例(%)"><el-input-number v-model="form.shareholdingRatio" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="审批状态"><el-select v-model="form.approvalStatus" style="width:100%"><el-option label="已批准" value="APPROVED" /><el-option label="审批中" value="PENDING" /><el-option label="已驳回" value="REJECTED" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预期收益率(%)"><el-input-number v-model="form.expectedReturn" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="境外投资详情" :visible.sync="detailVisible" width="750px" :close-on-click-modal="false">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="企业名称">{{ detailData.companyName || detailData.unitName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在国家">{{ detailData.country || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目名称">{{ detailData.projectName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="投资类型">{{ { NEW: '新设', MA: '并购', INCREASE: '增资' }[detailData.investType] || detailData.investType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="投资金额(万$)">{{ detailData.investAmount != null ? detailData.investAmount : '-' }}</el-descriptions-item>
        <el-descriptions-item label="持股比例(%)">{{ detailData.shareholdingRatio != null ? detailData.shareholdingRatio : '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批状态"><el-tag :type="{ APPROVED: 'success', PENDING: 'warning', REJECTED: 'danger' }[detailData.approvalStatus]" size="small">{{ { APPROVED: '已批准', PENDING: '审批中', REJECTED: '已驳回' }[detailData.approvalStatus] || '-' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="审批编号">{{ detailData.approvalNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批日期">{{ detailData.approvalDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级"><el-tag :type="{ LOW: 'success', MEDIUM: 'warning', HIGH: 'danger' }[detailData.riskLevel]" size="small">{{ { LOW: '低', MEDIUM: '中', HIGH: '高' }[detailData.riskLevel] || detailData.riskLevel || '-' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="预期收益率(%)">{{ detailData.expectedReturn != null ? detailData.expectedReturn : '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际收益率(%)"><span :style="{ color: detailData.actualReturn != null ? (detailData.actualReturn >= 0 ? '#52C41A' : '#F5222D') : '' }">{{ detailData.actualReturn != null ? detailData.actualReturn : '-' }}</span></el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="detailVisible = false">关 闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getOverseasInvestList, getOverseasInvestStats, getOverseasInvestDetail, addOverseasInvest, updateOverseasInvest, deleteOverseasInvest, batchDeleteOverseasInvest } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasInvestMgmt',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', country: '', investType: '', approvalStatus: '' },
      dialogVisible: false, dialogType: 'add', detailVisible: false, detailData: null, form: {},
      rules: {
        unitName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        country: [{ required: true, message: '请输入所在国家', trigger: 'blur' }],
        investType: [{ required: true, message: '请选择投资类型', trigger: 'change' }],
        investAmount: [{ required: true, message: '请输入投资金额', trigger: 'blur' }]
      },
      statCards: [
        { label: '投资总额(亿$)', value: '-', icon: 'el-icon-coin', color: '#FA8C16', bg: '#FFF7E6' },
        { label: '在投项目', value: '-', icon: 'el-icon-s-order', color: '#1677FF', bg: '#EBF1FF' },
        { label: '预期收益(亿$)', value: '-', icon: 'el-icon-top', color: '#52C41A', bg: '#F6FFED' },
        { label: '实际收益(亿$)', value: '-', icon: 'el-icon-data-line', color: '#F5222D', bg: '#FFF1F0' }
      ]
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      await this.fetchData()
      try {
        const res = await getOverseasInvestStats()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.investTotal != null) this.statCards[0].value = d.investTotal
          if (d.investCount != null) this.statCards[1].value = d.investCount + '项'
          if (d.expectedReturn != null) this.statCards[2].value = d.expectedReturn
          if (d.actualReturn != null) this.statCards[3].value = d.actualReturn
        }
      } catch (e) { console.warn('投资统计数据加载失败', e) }
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasInvestList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = { approvalStatus: 'PENDING' }; this.dialogVisible = true; this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate()) },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        try {
          // 同步企业名称到 companyName 字段（表格展示用）
          if (this.form.unitName) {
            this.form.companyName = this.form.unitName
          }
          const api = this.dialogType === 'add' ? addOverseasInvest : updateOverseasInvest
          const res = await api(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败，请稍后重试')
        }
      })
    },
    async handleView(row) {
      // 从数据库获取完整详情数据
      try {
        const res = await getOverseasInvestDetail(row.investId)
        if (res && res.result === 200 && res.data) {
          this.detailData = res.data
        } else {
          // 如果接口异常，降级使用列表行数据
          this.detailData = { ...row }
        }
      } catch (e) {
        this.detailData = { ...row }
      }
      this.detailVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除「${row.companyName || row.unitName || ''}」？`, '提示', { type: 'warning' })
        const res = await deleteOverseasInvest(row.investId)
        if (res && res.result === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', { type: 'warning' })
        const ids = this.multipleSelection.map(item => item.investId)
        const res = await batchDeleteOverseasInvest({ ids })
        if (res && res.result === 200) {
          this.$message.success('批量删除成功')
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '批量删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card-inner { display: flex; align-items: center; }
.stat-icon-wrap { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-card { border-radius: 6px; }
.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }
</style>
