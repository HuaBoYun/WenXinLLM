<template>
  <div class="asset-flow-container">
    <!-- Header -->
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <h2 class="page-title">资产流向追踪</h2>
    </div>

    <!-- Statistics Cards -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ statistics.totalFlows || 0 }}</div>
          <div class="stat-label">流向总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ (statistics.totalAmount || 0).toFixed(2) }}</div>
          <div class="stat-label">流转总额(万元)</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ statistics.relatedPartyCount || 0 }}</div>
          <div class="stat-label">关联方交易</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ statistics.highRiskCount || 0 }}</div>
          <div class="stat-label">高风险流向</div>
        </div>
      </el-col>
    </el-row>

    <!-- Filter Form -->
    <el-form :model="queryParams" inline class="filter-form">
      <el-form-item label="源企业名称">
        <el-input v-model="queryParams.sourceCompanyName" placeholder="请输入源企业名称" clearable />
      </el-form-item>
      <el-form-item label="目标企业名称">
        <el-input v-model="queryParams.targetCompanyName" placeholder="请输入目标企业名称" clearable />
      </el-form-item>
      <el-form-item label="资产类型">
        <el-select v-model="queryParams.assetType" placeholder="请选择" clearable>
          <el-option v-for="item in assetTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="流向类型">
        <el-select v-model="queryParams.flowType" placeholder="请选择" clearable>
          <el-option v-for="item in flowTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="风险等级">
        <el-select v-model="queryParams.riskLevel" placeholder="请选择" clearable>
          <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- Toolbar -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增追踪</el-button>
      <el-button type="success" icon="el-icon-connection" :disabled="multipleSelection.length === 0" @click="handleBatchTrace">批量追踪</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- Table -->
    <el-table v-loading="loading" :data="tableData" border stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="sourceCompanyName" label="源企业" min-width="140" show-overflow-tooltip />
      <el-table-column prop="targetCompanyName" label="目标企业" min-width="140" show-overflow-tooltip />
      <el-table-column prop="assetType" label="资产类型" width="110" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="assetTypeTagMap[row.assetType] || 'info'" size="small">{{ assetTypeMap[row.assetType] || row.assetType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="assetName" label="资产名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="flowAmount" label="流向金额(万元)" width="130" align="right">
        <template slot-scope="{ row }">{{ row.flowAmount ? Number(row.flowAmount).toFixed(2) : '0.00' }}</template>
      </el-table-column>
      <el-table-column prop="flowType" label="流向类型" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="flowTypeTagMap[row.flowType] || 'info'" size="small">{{ flowTypeMap[row.flowType] || row.flowType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="flowDate" label="流向日期" width="110" align="center" />
      <el-table-column prop="isRelatedParty" label="是否关联方" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.isRelatedParty === 'Y' ? 'danger' : 'success'" size="small">{{ row.isRelatedParty === 'Y' ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="riskLevelTagMap[row.riskLevel] || 'info'" size="small">{{ riskLevelMap[row.riskLevel] || row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click="handleView(row)">查看</el-button>
          <el-button type="text" size="small" @click="handleTrace(row)">追踪</el-button>
          <el-button type="text" size="small" @click="handlePath(row)">路径图</el-button>
          <el-dropdown trigger="click" @command="(cmd) => handleMoreCommand(cmd, row)">
            <el-button type="text" size="small">更多<i class="el-icon-arrow-down el-icon--right" /></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="edit">编辑</el-dropdown-item>
              <el-dropdown-item command="analysis">深度分析</el-dropdown-item>
              <el-dropdown-item command="predict">预测流向</el-dropdown-item>
              <el-dropdown-item command="monitor">实时监控</el-dropdown-item>
              <el-dropdown-item command="report">生成报告</el-dropdown-item>
              <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <el-pagination
      class="pagination"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="queryParams.pageSize"
      :current-page="queryParams.pageNum"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- Add/Edit Dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源企业名称" prop="sourceCompanyName">
              <el-input v-model="formData.sourceCompanyName" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标企业名称" prop="targetCompanyName">
              <el-input v-model="formData.targetCompanyName" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产类型" prop="assetType">
              <el-select v-model="formData.assetType" placeholder="请选择" style="width:100%">
                <el-option v-for="item in assetTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产名称" prop="assetName">
              <el-input v-model="formData.assetName" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流向金额(万元)" prop="flowAmount">
              <el-input-number v-model="formData.flowAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流向类型" prop="flowType">
              <el-select v-model="formData.flowType" placeholder="请选择" style="width:100%">
                <el-option v-for="item in flowTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="流向日期" prop="flowDate">
              <el-date-picker v-model="formData.flowDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="formData.riskLevel" placeholder="请选择" style="width:100%">
                <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否关联方" prop="isRelatedParty">
              <el-select v-model="formData.isRelatedParty" placeholder="请选择" style="width:100%">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流向原因" prop="flowReason">
              <el-input v-model="formData.flowReason" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog title="流向详情" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="源企业">{{ detailData.sourceCompanyName }}</el-descriptions-item>
        <el-descriptions-item label="目标企业">{{ detailData.targetCompanyName }}</el-descriptions-item>
        <el-descriptions-item label="资产类型">{{ assetTypeMap[detailData.assetType] || detailData.assetType }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ detailData.assetName }}</el-descriptions-item>
        <el-descriptions-item label="流向金额(万元)">{{ detailData.flowAmount }}</el-descriptions-item>
        <el-descriptions-item label="流向类型">{{ flowTypeMap[detailData.flowType] || detailData.flowType }}</el-descriptions-item>
        <el-descriptions-item label="流向日期">{{ detailData.flowDate }}</el-descriptions-item>
        <el-descriptions-item label="是否关联方">{{ detailData.isRelatedParty === 'Y' ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ riskLevelMap[detailData.riskLevel] || detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">{{ approvalStatusMap[detailData.approvalStatus] || detailData.approvalStatus }}</el-descriptions-item>
        <el-descriptions-item label="流向原因" :span="2">{{ detailData.flowReason }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Trace Dialog -->
    <el-dialog title="资产追踪" :visible.sync="traceVisible" width="600px" append-to-body>
      <div v-loading="traceLoading" class="trace-content">
        <el-alert v-if="traceResult" :title="traceResult.title" :description="traceResult.description" type="info" :closable="false" show-icon />
        <div v-if="traceResult && traceResult.path" class="trace-path">
          <p><strong>追踪路径:</strong></p>
          <p>{{ traceResult.path }}</p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="traceVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Path Dialog -->
    <el-dialog title="流向路径图" :visible.sync="pathVisible" width="600px" append-to-body>
      <div v-loading="pathLoading" class="path-content">
        <el-timeline v-if="pathData.length > 0">
          <el-timeline-item v-for="(item, index) in pathData" :key="index" :timestamp="item.time" :type="item.type" placement="top">
            <el-card shadow="hover">
              <h4>{{ item.title }}</h4>
              <p>{{ item.content }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无路径数据" />
      </div>
      <div slot="footer">
        <el-button @click="pathVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Analysis Dialog -->
    <el-dialog title="深度分析" :visible.sync="analysisVisible" width="600px" append-to-body>
      <div v-loading="analysisLoading" class="analysis-content">
        <el-alert v-if="analysisResult" :title="analysisResult.title" type="warning" :closable="false" show-icon />
        <div v-if="analysisResult" class="analysis-detail">
          <p v-for="(line, idx) in analysisResult.lines" :key="idx">{{ line }}</p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="analysisVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Predict Dialog -->
    <el-dialog title="流向预测" :visible.sync="predictVisible" width="600px" append-to-body>
      <div v-loading="predictLoading" class="predict-content">
        <el-alert v-if="predictResult" :title="predictResult.title" type="success" :closable="false" show-icon />
        <div v-if="predictResult" class="predict-detail">
          <p v-for="(line, idx) in predictResult.lines" :key="idx">{{ line }}</p>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="predictVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Monitor Dialog -->
    <el-dialog title="实时监控" :visible.sync="monitorVisible" width="600px" append-to-body>
      <div class="monitor-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="监控对象">{{ monitorData.target }}</el-descriptions-item>
          <el-descriptions-item label="监控状态">
            <el-tag :type="monitorData.status === '正常' ? 'success' : 'danger'" size="small">{{ monitorData.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最近流向">{{ monitorData.lastFlow }}</el-descriptions-item>
          <el-descriptions-item label="监控频率">{{ monitorData.frequency }}</el-descriptions-item>
          <el-descriptions-item label="预警阈值">{{ monitorData.threshold }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="monitorVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAssetFlowList, getAssetFlowById, addAssetFlow, updateAssetFlow, deleteAssetFlow, exportAssetFlow } from '@/api/stateAssets/assetFlow'
import { mapGetters } from 'vuex'
import request from '@/utils/request'

export default {
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
  name: 'AssetFlow',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sourceCompanyName: '',
        targetCompanyName: '',
        assetType: '',
        flowType: '',
        riskLevel: ''
      },
      statistics: {
        totalFlows: 0,
        totalAmount: 0,
        relatedPartyCount: 0,
        highRiskCount: 0
      },
      // Options
      assetTypeOptions: [
        { value: 'FUND', label: '资金' },
        { value: 'EQUITY', label: '股权' },
        { value: 'FIXED', label: '固定资产' },
        { value: 'INTANGIBLE', label: '无形资产' },
        { value: 'OTHER', label: '其他' }
      ],
      flowTypeOptions: [
        { value: 'INVEST', label: '投资' },
        { value: 'TRANSFER', label: '转移' },
        { value: 'DISPOSE', label: '处置' },
        { value: 'LEASE', label: '租赁' }
      ],
      riskLevelOptions: [
        { value: 'HIGH', label: '高风险' },
        { value: 'MEDIUM', label: '中风险' },
        { value: 'LOW', label: '低风险' }
      ],
      // Maps
      assetTypeMap: { FUND: '资金', EQUITY: '股权', FIXED: '固定资产', INTANGIBLE: '无形资产', OTHER: '其他' },
      assetTypeTagMap: { FUND: 'warning', EQUITY: 'success', FIXED: '', INTANGIBLE: 'info', OTHER: 'info' },
      flowTypeMap: { INVEST: '投资', TRANSFER: '转移', DISPOSE: '处置', LEASE: '租赁' },
      flowTypeTagMap: { INVEST: 'success', TRANSFER: 'warning', DISPOSE: 'danger', LEASE: 'info' },
      riskLevelMap: { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' },
      riskLevelTagMap: { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' },
      approvalStatusMap: { APPROVED: '已审批', PENDING: '待审批', REJECTED: '已驳回' },
      // Dialog states
      dialogVisible: false,
      dialogTitle: '新增追踪',
      isEdit: false,
      formData: {},
      formRules: {
        sourceCompanyName: [{ required: true, message: '请输入源企业名称', trigger: 'blur' }],
        targetCompanyName: [{ required: true, message: '请输入目标企业名称', trigger: 'blur' }],
        assetType: [{ required: true, message: '请选择资产类型', trigger: 'change' }],
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        flowAmount: [{ required: true, message: '请输入流向金额', trigger: 'blur' }],
        flowType: [{ required: true, message: '请选择流向类型', trigger: 'change' }],
        flowDate: [{ required: true, message: '请选择流向日期', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        isRelatedParty: [{ required: true, message: '请选择是否关联方', trigger: 'change' }]
      },
      detailVisible: false,
      detailData: {},
      traceVisible: false,
      traceLoading: false,
      traceResult: null,
      pathVisible: false,
      pathLoading: false,
      pathData: [],
      analysisVisible: false,
      analysisLoading: false,
      analysisResult: null,
      predictVisible: false,
      predictLoading: false,
      predictResult: null,
      monitorVisible: false,
      monitorData: {}
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getAssetFlowList(this.queryParams)
        if (res.result === 200) {
          this.tableData = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || res.data.total || 0
          this.getStatistics()
        } else {
          console.error('获取列表失败:', res.msg)
        }
      } catch (e) {
        console.error('获取列表异常:', e)
      } finally {
        this.loading = false
      }
    },
    async getStatistics() {
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/statistics', method: 'post', data: this.queryParams })
        if (res.result === 200 && res.data) {
          this.statistics = res.data
          return
        }
      } catch (e) {
        // fallback
      }
      this.statistics = {
        totalFlows: this.total || this.tableData.length,
        totalAmount: this.tableData.reduce((s, r) => s + (Number(r.flowAmount) || 0), 0),
        relatedPartyCount: this.tableData.filter(r => r.isRelatedParty === 'Y').length,
        highRiskCount: this.tableData.filter(r => r.riskLevel === 'HIGH').length
      }
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleReset() {
      this.queryParams = { pageNum: 1, pageSize: 10, sourceCompanyName: '', targetCompanyName: '', assetType: '', flowType: '', riskLevel: '' }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleAdd() {
      this.dialogTitle = '新增追踪'
      this.isEdit = false
      this.formData = { flowAmount: 0, isRelatedParty: 'N', riskLevel: 'LOW' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑追踪'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        try {
          const apiFn = this.isEdit ? updateAssetFlow : addAssetFlow
          const res = await apiFn(this.formData)
          if (res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
            this.dialogVisible = false
            this.getList()
          } else {
            console.error('操作失败:', res.msg)
          }
        } catch (e) {
          console.error('提交异常:', e)
        }
      })
    },
    async handleView(row) {
      try {
        const res = await getAssetFlowById(row.flowId)
        if (res.result === 200 && res.data) {
          this.detailData = res.data
        } else {
          this.detailData = { ...row }
        }
      } catch (e) {
        this.detailData = { ...row }
      }
      this.detailVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该流向记录?', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteAssetFlow(row.flowId)
          if (res.result === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            console.error('删除失败:', res.msg)
          }
        } catch (e) {
          console.error('删除异常:', e)
        }
      }).catch(() => {})
    },
    async handleExport() {
      try {
        const res = await exportAssetFlow()
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产流向追踪_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
        console.error('导出异常:', e)
      }
    },
    async handleTrace(row) {
      this.traceVisible = true
      this.traceLoading = true
      this.traceResult = null
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/trace', method: 'post', data: { flowId: row.flowId } })
        if (res.result === 200 && res.data && res.data.path) {
          this.traceResult = {
            title: '资产追踪结果',
            description: `资产「${res.data.assetName || row.assetName}」从「${res.data.sourceName || row.sourceCompanyName}」流向「${res.data.targetName || row.targetCompanyName}」`,
            path: res.data.path
          }
          this.traceLoading = false
          return
        }
      } catch (e) {
        // fallback
      }
      this.traceResult = {
        title: '资产追踪结果',
        description: `资产「${row.assetName}」从「${row.sourceCompanyName}」流向「${row.targetCompanyName}」`,
        path: `${row.sourceCompanyName} → ${row.targetCompanyName}, 资产: ${row.assetName}, 金额: ${row.flowAmount}万元`
      }
      this.traceLoading = false
    },
    async handlePath(row) {
      this.pathVisible = true
      this.pathLoading = true
      this.pathData = []
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/path', method: 'post', data: { flowId: row.flowId } })
        if (res.result === 200 && res.data && res.data.length > 0) {
          this.pathData = res.data
          this.pathLoading = false
          return
        }
      } catch (e) {
        // fallback
      }
      this.pathData = [
        { time: row.flowDate || '起始', title: row.sourceCompanyName, content: `发起${this.flowTypeMap[row.flowType] || '流转'}, 资产: ${row.assetName}`, type: 'primary' },
        { time: row.flowDate || '流转中', title: '资产流转', content: `金额: ${row.flowAmount}万元, 类型: ${this.assetTypeMap[row.assetType] || row.assetType}`, type: 'warning' },
        { time: row.flowDate || '到达', title: row.targetCompanyName, content: `接收资产, 风险等级: ${this.riskLevelMap[row.riskLevel] || row.riskLevel}`, type: 'success' }
      ]
      this.pathLoading = false
    },
    async handleAnalysis(row) {
      this.analysisVisible = true
      this.analysisLoading = true
      this.analysisResult = null
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/statistics', method: 'post', data: { flowId: row.flowId, type: 'analysis' } })
        if (res.result === 200 && res.data && res.data.title) {
          this.analysisResult = res.data
          this.analysisLoading = false
          return
        }
      } catch (e) {
        // fallback
      }
      this.analysisResult = {
        title: `「${row.assetName}」深度分析报告`,
        lines: [
          `分析对象: ${row.sourceCompanyName} → ${row.targetCompanyName}`,
          `资产类型: ${this.assetTypeMap[row.assetType] || row.assetType}, 金额: ${row.flowAmount}万元`,
          `流向类型: ${this.flowTypeMap[row.flowType] || row.flowType}`,
          `风险等级: ${this.riskLevelMap[row.riskLevel] || row.riskLevel}`,
          `是否关联方: ${row.isRelatedParty === 'Y' ? '是' : '否'}`,
          `流向日期: ${row.flowDate || '-'}`,
          ``,
          `【分析结论】`,
          row.riskLevel === 'HIGH' ? '⚠️ 该笔资产流向风险较高，建议加强监控并进行合规审查' : '✅ 该笔资产流向风险可控，建议持续关注后续变动'
        ]
      }
      this.analysisLoading = false
    },
    async handlePredict(row) {
      this.predictVisible = true
      this.predictLoading = true
      this.predictResult = null
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/predict', method: 'post', data: { flowId: row.flowId } })
        if (res.result === 200 && res.data && res.data.title) {
          this.predictResult = res.data
          this.predictLoading = false
          return
        }
      } catch (e) {
        // fallback
      }
      const predictMap = { INVEST: '后续可能产生股权变更或分红回流', TRANSFER: '资产可能继续向下游企业转移', DISPOSE: '资产处置后资金可能回流至源企业', LEASE: '租赁到期后可能续租或转为购买' }
      this.predictResult = {
        title: `「${row.assetName}」流向预测`,
        lines: [
          `当前流向: ${row.sourceCompanyName} → ${row.targetCompanyName}`,
          `流向类型: ${this.flowTypeMap[row.flowType] || row.flowType}`,
          `预测趋势: ${predictMap[row.flowType] || '暂无明确预测趋势'}`,
          `风险预警: ${row.riskLevel === 'HIGH' ? '高风险，建议提前制定应对方案' : '风险可控'}`,
          `建议措施: 持续跟踪该资产后续流向变化`
        ]
      }
      this.predictLoading = false
    },
    handleMonitor(row) {
      this.monitorData = {
        target: `${row.sourceCompanyName} → ${row.targetCompanyName} (${row.assetName})`,
        status: row.riskLevel === 'HIGH' ? '预警' : '正常',
        lastFlow: `${row.flowDate} ${this.flowTypeMap[row.flowType] || row.flowType} ${row.flowAmount}万元`,
        frequency: '每日监控',
        threshold: row.riskLevel === 'HIGH' ? '500万元' : '1000万元'
      }
      this.monitorVisible = true
    },
    handleReport(row) {
      const report = [
        `【资产流向报告】`,
        `报告时间: ${new Date().toLocaleString()}`,
        `━━━━━━━━━━━━━━━━━━━━`,
        `源企业: ${row.sourceCompanyName}`,
        `目标企业: ${row.targetCompanyName}`,
        `资产名称: ${row.assetName}`,
        `资产类型: ${this.assetTypeMap[row.assetType] || row.assetType}`,
        `流向金额: ${row.flowAmount}万元`,
        `流向类型: ${this.flowTypeMap[row.flowType] || row.flowType}`,
        `流向日期: ${row.flowDate}`,
        `风险等级: ${this.riskLevelMap[row.riskLevel] || row.riskLevel}`,
        `关联方交易: ${row.isRelatedParty === 'Y' ? '是' : '否'}`,
        `━━━━━━━━━━━━━━━━━━━━`,
        `备注: ${row.remark || '无'}`
      ].join('\n')
      this.$alert(report, '资产流向报告', { confirmButtonText: '关闭', customClass: 'report-alert' })
    },
    async handleBatchTrace() {
      if (this.multipleSelection.length === 0) return
      this.traceVisible = true
      this.traceLoading = true
      this.traceResult = null
      const names = this.multipleSelection.map(r => `${r.sourceCompanyName}→${r.targetCompanyName}`).join('\n')
      const totalAmount = this.multipleSelection.reduce((s, r) => s + (Number(r.flowAmount) || 0), 0)
      try {
        const ids = this.multipleSelection.map(r => r.flowId)
        const res = await request({ url: '/monitor/v1/supervision/asset/flow/trace', method: 'post', data: { flowIds: ids } })
        if (res.result === 200 && res.data && res.data.path) {
          this.traceResult = res.data
          this.traceLoading = false
          return
        }
      } catch (e) {
        // fallback
      }
      this.traceResult = {
        title: `批量追踪结果 (${this.multipleSelection.length}条)`,
        description: `涉及流转总额: ${totalAmount.toLocaleString()}万元`,
        path: names
      }
      this.traceLoading = false
    },
    handleMoreCommand(command, row) {
      switch (command) {
        case 'edit': this.handleEdit(row); break
        case 'analysis': this.handleAnalysis(row); break
        case 'predict': this.handlePredict(row); break
        case 'monitor': this.handleMonitor(row); break
        case 'report': this.handleReport(row); break
        case 'delete': this.handleDelete(row); break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-flow-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100%;
}

.page-header {
  padding: 20px 24px;
  border-radius: 8px;
  margin-bottom: 20px;

  .page-title {
    color: #fff;
    margin: 0;
    font-size: 20px;
    font-weight: 600;
  }
}

.stat-cards {
  margin-bottom: 20px;

  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    transition: transform 0.3s;

    &:hover {
      transform: translateY(-2px);
    }

    .stat-value {
      font-size: 28px;
      font-weight: 700;
      color: #1a3a5c;
      margin-bottom: 8px;
    }

    .stat-label {
      font-size: 14px;
      color: #909399;
    }
  }
}

.filter-form {
  background: #fff;
  padding: 20px 20px 4px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.toolbar {
  margin-bottom: 16px;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.pagination {
  margin-top: 16px;
  text-align: right;
}

.trace-content,
.path-content,
.analysis-content,
.predict-content {
  min-height: 120px;
}

.trace-path {
  margin-top: 16px;
  padding: 12px;
  background: #f4f4f5;
  border-radius: 4px;
}

.analysis-detail,
.predict-detail {
  margin-top: 16px;
  padding: 12px;
  background: #f4f4f5;
  border-radius: 4px;
  line-height: 2;
}
</style>
