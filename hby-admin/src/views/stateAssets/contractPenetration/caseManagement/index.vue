<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-order"></i><span>法律纠纷案件管理</span></div>
      <div class="page-header-right">
        <el-button size="mini" icon="el-icon-back" style="background:rgba(255,255,255,0.2);color:#fff;border-color:rgba(255,255,255,0.4);" @click="$router.push('/stateAssets/contractPenetration/dispute')">返回纠纷总览</el-button>
      </div>
    </div>
    <div class="page-header-desc-bar" :style="{ borderLeft: '3px solid ' + themeColor }">全量案件数据管理，突出重大案件监控，以案促管避免同类纠纷反复发生</div>

    <!-- 搜索区域 -->
    <div class="search-bar" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :inline="true" :model="searchForm" size="small">
        <el-form-item label="案件类型">
          <el-select v-model="searchForm.caseType" placeholder="全部" clearable>
            <el-option v-for="item in caseTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="当前阶段">
          <el-select v-model="searchForm.stage" placeholder="全部" clearable>
            <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="败诉风险">
          <el-select v-model="searchForm.riskLevel" placeholder="全部" clearable>
            <el-option v-for="item in riskOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作栏 -->
    <div class="table-toolbar">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增案件</el-button>
      <el-button type="warning" size="small" icon="el-icon-data-analysis" @click="handleAnalysis">以案促管分析</el-button>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      :row-class-name="getRowClass"
      border
      stripe
      style="width: 100%"
      :style="{ '--table-header-bg': themeColorLight }"
    >
      <el-table-column prop="caseNo" label="案件编号" width="140" />
      <el-table-column prop="caseName" label="案件名称" min-width="180" show-overflow-tooltip />
      <el-table-column prop="caseType" label="案件类型" width="130">
        <template slot-scope="{ row }">
          <el-tag size="small">{{ row.caseType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="标的金额（万元）" width="150" align="right">
        <template slot-scope="{ row }">
          <span :class="row.amount > 1000 ? 'major-amount' : ''">{{ Number(row.amount).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="stage" label="当前阶段" width="100">
        <template slot-scope="{ row }">
          <el-tag size="small" type="info">{{ row.stage }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="riskLevel" label="败诉风险" width="100">
        <template slot-scope="{ row }">
          <el-tag size="small" :type="riskTagType(row.riskLevel)">{{ row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="filingDate" label="立案时间" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
          <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" size="small" class="danger-btn" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrap">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNo"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" @close="resetForm">
      <el-form ref="caseForm" :model="formData" :rules="formRules" label-width="100px" size="small">
        <el-form-item label="案件名称" prop="caseName">
          <el-input v-model="formData.caseName" placeholder="请输入案件名称" />
        </el-form-item>
        <el-form-item label="案件类型" prop="caseType">
          <el-select v-model="formData.caseType" placeholder="请选择" style="width:100%">
            <el-option v-for="item in caseTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="对方单位">
          <el-input v-model="formData.counterpartyName" placeholder="请输入对方单位名称" />
        </el-form-item>
        <el-form-item label="标的金额" prop="amount">
          <el-input v-model.number="formData.amount" placeholder="单位：万元">
            <template slot="append">万元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="当前阶段" prop="stage">
          <el-select v-model="formData.stage" placeholder="请选择" style="width:100%">
            <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="立案时间" prop="filingDate">
          <el-date-picker v-model="formData.filingDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择日期" style="width:100%" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="案件详情" :visible.sync="detailVisible" size="520px" append-to-body>
      <div v-loading="detailLoading" style="padding: 20px">
        <template v-if="detailData">
          <el-descriptions :column="2" size="small" border>
            <el-descriptions-item label="案件编号">{{ detailData.caseNo }}</el-descriptions-item>
            <el-descriptions-item label="案件名称" :span="2">{{ detailData.caseName }}</el-descriptions-item>
            <el-descriptions-item label="案件类型">
              <el-tag size="small">{{ detailData.caseType }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="标的金额">
              <span style="font-weight:700;color:#F5222D">{{ Number(detailData.amount).toLocaleString() }} 万元</span>
            </el-descriptions-item>
            <el-descriptions-item label="当前阶段">
              <el-tag size="small" type="info">{{ detailData.stage }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="败诉风险">
              <el-tag size="small" :type="riskTagType(detailData.riskLevel)">{{ detailData.riskLevel }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="对方单位">{{ detailData.counterpartyName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="审理法院">{{ detailData.courtName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="立案时间">{{ detailData.filingDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detailData.createTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="判决结果" :span="2">{{ detailData.judgmentResult || '-' }}</el-descriptions-item>
            <el-descriptions-item label="和解金额">{{ detailData.settlementAmount ? Number(detailData.settlementAmount).toLocaleString() + ' 万元' : '-' }}</el-descriptions-item>
            <el-descriptions-item label="纠纷原因" :span="2">{{ detailData.disputeReason || '-' }}</el-descriptions-item>
            <el-descriptions-item label="经验教训" :span="2">{{ detailData.lessonsLearned || '-' }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </div>
    </el-drawer>

    <!-- 以案促管分析弹窗 -->
    <el-dialog title="以案促管分析" :visible.sync="analysisVisible" width="720px">
      <div v-loading="analysisLoading">
        <template v-if="analysisData">
          <!-- 总体统计 -->
          <div class="analysis-summary">
            <div class="summary-item">
              <span class="summary-num">{{ analysisData.summary.totalCases }}</span>
              <span class="summary-label">案件总数</span>
            </div>
            <div class="summary-item">
              <span class="summary-num" style="color:#F5222D">{{ analysisData.summary.highRiskCases }}</span>
              <span class="summary-label">高风险案件</span>
            </div>
            <div class="summary-item">
              <span class="summary-num" style="color:#FA8C16">{{ analysisData.summary.ongoingCases }}</span>
              <span class="summary-label">在办案件</span>
            </div>
            <div class="summary-item">
              <span class="summary-num" style="color:#52C41A">{{ analysisData.summary.closedCases }}</span>
              <span class="summary-label">已结案</span>
            </div>
            <div class="summary-item">
              <span class="summary-num" style="color:#873800">{{ Number(analysisData.summary.totalAmount).toLocaleString() }}</span>
              <span class="summary-label">涉案总额(万)</span>
            </div>
          </div>
          <!-- 分布统计 -->
          <el-row :gutter="16" style="margin-top:16px">
            <el-col :span="8">
              <div class="analysis-card">
                <div class="card-title">案件类型分布</div>
                <div v-for="(count, type) in analysisData.typeDistribution" :key="type" class="dist-item">
                  <span>{{ type }}</span>
                  <el-progress :percentage="analysisData.summary.totalCases ? Math.round(count / analysisData.summary.totalCases * 100) : 0" :stroke-width="14" />
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-card">
                <div class="card-title">风险等级分布</div>
                <div v-for="(count, level) in analysisData.riskDistribution" :key="level" class="dist-item">
                  <span><el-tag size="mini" :type="riskTagType(level)">{{ level }}</el-tag></span>
                  <span style="font-weight:600;margin-left:8px">{{ count }} 件</span>
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-card">
                <div class="card-title">阶段分布</div>
                <div v-for="(count, stage) in analysisData.stageDistribution" :key="stage" class="dist-item">
                  <span>{{ stage }}</span>
                  <span style="font-weight:600;margin-left:8px">{{ count }} 件</span>
                </div>
              </div>
            </el-col>
          </el-row>
          <!-- 管理建议 -->
          <div class="analysis-suggestions" style="margin-top:16px">
            <div class="card-title" style="margin-bottom:8px"><i class="el-icon-warning-outline" style="color:#FA8C16;margin-right:4px"></i>管理建议</div>
            <el-alert
              v-for="(suggestion, idx) in analysisData.suggestions"
              :key="idx"
              :title="suggestion"
              type="warning"
              :closable="false"
              show-icon
              style="margin-bottom:8px"
            />
            <el-empty v-if="!analysisData.suggestions || analysisData.suggestions.length === 0" description="暂无管理建议" :image-size="60" />
          </div>
        </template>
      </div>
      <span slot="footer">
        <el-button size="small" @click="analysisVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCaseList, getCaseDetail, addCase, updateCase, deleteCase, getCaseAnalysis } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractCaseManagement',
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
      searchForm: { caseType: '', stage: '', riskLevel: '' },
      dialogVisible: false,
      dialogTitle: '',
      formData: {},
      formRules: {
        caseName: [{ required: true, message: '请输入案件名称', trigger: 'blur' }],
        caseType: [{ required: true, message: '请选择案件类型', trigger: 'change' }],
        amount: [{ required: true, message: '请输入标的金额', trigger: 'blur' }],
      },
      // 详情
      detailVisible: false,
      detailLoading: false,
      detailData: null,
      // 分析
      analysisVisible: false,
      analysisLoading: false,
      analysisData: null,
      // 选项
      caseTypeOptions: [
        { label: '合同纠纷', value: '合同纠纷' },
        { label: '仲裁程序', value: '仲裁程序' },
        { label: '诉讼案件', value: '诉讼案件' },
        { label: '行政复议', value: '行政复议' },
        { label: '其他', value: '其他' },
      ],
      stageOptions: [
        { label: '协商中', value: '协商中' },
        { label: '仲裁中', value: '仲裁中' },
        { label: '一审中', value: '一审中' },
        { label: '二审中', value: '二审中' },
        { label: '执行中', value: '执行中' },
        { label: '已结案', value: '已结案' },
      ],
      riskOptions: [
        { label: '高', value: '高' },
        { label: '中', value: '中' },
        { label: '低', value: '低' },
      ],
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getCaseList({
          ...this.searchForm,
          pageNumber: this.pageNo,
          pageSize: this.pageSize,
        })
        if (res && res.result === 200 && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.tableData = []
          this.total = 0
        }
      } catch (e) {
        this.tableData = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleSearch() { this.pageNo = 1; this.fetchData() },
    handleReset() {
      this.searchForm = { caseType: '', stage: '', riskLevel: '' }
      this.pageNo = 1
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增案件'
      this.formData = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑案件'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleDetail(row) {
      this.detailVisible = true
      this.detailLoading = true
      this.detailData = null
      try {
        const res = await getCaseDetail(row.id)
        if (res && res.result === 200 && res.data) {
          this.detailData = res.data
        } else {
          // 接口失败时使用列表行数据展示
          this.detailData = row
        }
      } catch (e) {
        this.detailData = row
      } finally {
        this.detailLoading = false
      }
    },
    async handleSubmit() {
      this.$refs.caseForm.validate(async (valid) => {
        if (!valid) return
        try {
          const isAdd = !this.formData.id
          const res = await (isAdd ? addCase : updateCase)(this.formData)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`确认删除案件「${row.caseName}」？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteCase(row.id)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
          else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    async handleAnalysis() {
      this.analysisVisible = true
      this.analysisLoading = true
      this.analysisData = null
      try {
        const res = await getCaseAnalysis()
        if (res && res.result === 200 && res.data) {
          this.analysisData = res.data
        } else {
          this.$message.error(res.msg || '获取分析数据失败')
        }
      } catch (e) {
        this.$message.error('获取分析数据失败')
      } finally {
        this.analysisLoading = false
      }
    },
    getRowClass({ row }) {
      return row.amount > 1000 ? 'major-row' : ''
    },
    riskTagType(level) {
      return { '高': 'danger', '中': 'warning', '低': 'success' }[level] || 'info'
    },
    resetForm() { this.formData = {} },
    handleSizeChange(val) { this.pageSize = val; this.fetchData() },
    handlePageChange(val) { this.pageNo = val; this.fetchData() },
  },
}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 0; padding: 14px 20px;
  border-radius: 6px 6px 0 0; color: #fff;
  .page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
}
.page-header-desc-bar { font-size: 13px; color: #666; background: #fff; padding: 8px 20px; border-radius: 0 0 6px 6px; margin-bottom: 14px; border-left: 3px solid; }
.search-bar { background: #fff; padding: 12px 16px 0; border-radius: 6px; margin-bottom: 10px; border-left: 3px solid; }
.table-toolbar { margin: 10px 0 8px; }
.pagination-wrap { margin-top: 12px; text-align: right; }
.major-amount { color: #F5222D; font-weight: 700; }
::v-deep .major-row { background: #fff2f0 !important; }
::v-deep .el-table th { background: var(--table-header-bg, #e6f7ff); }
::v-deep .el-card { border-radius: 6px; }
.danger-btn { color: #F56C6C; }
/* 分析弹窗样式 */
.analysis-summary {
  display: flex; justify-content: space-around; padding: 16px; background: #fafafa; border-radius: 6px;
  .summary-item { text-align: center;
    .summary-num { display: block; font-size: 24px; font-weight: 700; color: #333; }
    .summary-label { font-size: 12px; color: #999; margin-top: 4px; }
  }
}
.analysis-card {
  background: #fafafa; border-radius: 6px; padding: 12px; min-height: 160px;
  .card-title { font-weight: 600; font-size: 13px; color: #333; margin-bottom: 10px; }
  .dist-item { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; font-size: 13px; }
}
.analysis-suggestions {
  background: #fffbe6; border-radius: 6px; padding: 12px;
  .card-title { font-weight: 600; font-size: 13px; color: #333; }
}
</style>