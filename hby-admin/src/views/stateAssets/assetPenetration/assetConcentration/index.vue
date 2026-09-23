<template>
  <div class="asset-concentration-page">
    <!-- Header -->
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="header-content">
        <h2 class="page-title">资产集中度分析</h2>
        <p class="page-desc">对企业资产集中度进行量化分析，评估资产配置风险，提供优化建议</p>
      </div>
    </div>

    <!-- Stat Cards -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background:#409EFF"><i class="el-icon-office-building"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCompanies || 0 }}</div>
            <div class="stat-label">分析企业数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background:#67C23A"><i class="el-icon-data-analysis"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.avgHhiIndex || '0.00' }}</div>
            <div class="stat-label">平均HHI指数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background:#E6A23C"><i class="el-icon-warning"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.highRiskCount || 0 }}</div>
            <div class="stat-label">高风险企业</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background:#F56C6C"><i class="el-icon-s-data"></i></div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.highConcentrationCount || 0 }}</div>
            <div class="stat-label">高集中度企业</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Filter Form -->
    <div class="filter-section">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="行业">
          <el-input v-model="queryForm.industry" placeholder="请输入行业" clearable />
        </el-form-item>
        <el-form-item label="集中度等级">
          <el-select v-model="queryForm.concentrationLevel" placeholder="请选择" clearable>
            <el-option label="高集中" value="HIGH" />
            <el-option label="中等" value="MEDIUM" />
            <el-option label="低集中" value="LOW" />
            <el-option label="分散" value="DISPERSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable>
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- Toolbar -->
    <div class="toolbar-section">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增分析</el-button>
      <el-button type="success" size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
      <el-button type="warning" size="small" icon="el-icon-cpu" @click="handleBatchCalculate">批量计算</el-button>
    </div>

    <!-- Table -->
    <el-table v-loading="loading" :data="tableData" border stripe style="width:100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="companyName" label="企业名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="industry" label="行业" width="120" show-overflow-tooltip />
      <el-table-column prop="region" label="地区" width="100" />
      <el-table-column prop="totalAssets" label="总资产(万元)" width="130" align="right">
        <template slot-scope="{ row }">{{ formatNumber(row.totalAssets) }}</template>
      </el-table-column>
      <el-table-column prop="assetRatio" label="资产占比(%)" width="110" align="right">
        <template slot-scope="{ row }">{{ row.assetRatio ? row.assetRatio.toFixed(2) : '0.00' }}</template>
      </el-table-column>
      <el-table-column prop="hhiIndex" label="HHI指数" width="100" align="right">
        <template slot-scope="{ row }">{{ row.hhiIndex ? row.hhiIndex.toFixed(4) : '0.0000' }}</template>
      </el-table-column>
      <el-table-column prop="concentrationLevel" label="集中度等级" width="110" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="concentrationTagType(row.concentrationLevel)" size="small">{{ concentrationLevelMap[row.concentrationLevel] || row.concentrationLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="riskTagType(row.riskLevel)" size="small">{{ riskLevelMap[row.riskLevel] || row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="analysisDate" label="分析日期" width="110" align="center" />
      <el-table-column label="操作" width="240" align="center" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click="handleView(row)">详情</el-button>
          <el-button type="text" size="small" @click="handleCalculate(row)">计算</el-button>
          <el-button type="text" size="small" @click="handleChart(row)">图表</el-button>
          <el-dropdown trigger="click" @command="(cmd) => handleCommand(cmd, row)">
            <el-button type="text" size="small">更多<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="edit">编辑</el-dropdown-item>
              <el-dropdown-item command="trend">趋势</el-dropdown-item>
              <el-dropdown-item command="compare">对比</el-dropdown-item>
              <el-dropdown-item command="optimize">优化</el-dropdown-item>
              <el-dropdown-item command="report">报告</el-dropdown-item>
              <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <el-pagination
      class="pagination-section"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="queryForm.pageSize"
      :current-page="queryForm.pageNum"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- Add/Edit Dialog -->
    <el-dialog :title="dialogType === 'add' ? '新增资产集中度分析' : '编辑资产集中度分析'" :visible.sync="formDialogVisible" width="700px" append-to-body>
      <el-form ref="dataForm" :model="formData" :rules="formRules" label-width="110px">
        <input type="hidden" v-model="formData.concentrationId" />
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="formData.companyName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业ID" prop="companyId">
              <el-input v-model="formData.companyId" placeholder="请输入企业ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行业" prop="industry">
              <el-input v-model="formData.industry" placeholder="请输入行业" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地区" prop="region">
              <el-input v-model="formData.region" placeholder="请输入地区" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总资产(万元)" prop="totalAssets">
              <el-input-number v-model="formData.totalAssets" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产占比(%)" prop="assetRatio">
              <el-input-number v-model="formData.assetRatio" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="HHI指数" prop="hhiIndex">
              <el-input-number v-model="formData.hhiIndex" :min="0" :max="1" :precision="4" :step="0.01" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="集中度等级" prop="concentrationLevel">
              <el-select v-model="formData.concentrationLevel" placeholder="请选择" style="width:100%">
                <el-option label="高集中" value="HIGH" />
                <el-option label="中等" value="MEDIUM" />
                <el-option label="低集中" value="LOW" />
                <el-option label="分散" value="DISPERSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主要资产类型" prop="topAssetType">
              <el-input v-model="formData.topAssetType" placeholder="请输入主要资产类型" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要资产占比" prop="topAssetRatio">
              <el-input-number v-model="formData.topAssetRatio" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="formData.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高风险" value="HIGH" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="低风险" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析日期" prop="analysisDate">
              <el-date-picker v-model="formData.analysisDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="formDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog title="资产集中度详情" :visible.sync="detailDialogVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业名称">{{ detailData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="企业ID">{{ detailData.companyId }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ detailData.industry }}</el-descriptions-item>
        <el-descriptions-item label="地区">{{ detailData.region }}</el-descriptions-item>
        <el-descriptions-item label="总资产(万元)">{{ formatNumber(detailData.totalAssets) }}</el-descriptions-item>
        <el-descriptions-item label="资产占比(%)">{{ detailData.assetRatio ? detailData.assetRatio.toFixed(2) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="HHI指数">{{ detailData.hhiIndex ? detailData.hhiIndex.toFixed(4) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="集中度等级">
          <el-tag :type="concentrationTagType(detailData.concentrationLevel)" size="small">{{ concentrationLevelMap[detailData.concentrationLevel] || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="主要资产类型">{{ detailData.topAssetType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="主要资产占比(%)">{{ detailData.topAssetRatio ? detailData.topAssetRatio.toFixed(2) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="riskTagType(detailData.riskLevel)" size="small">{{ riskLevelMap[detailData.riskLevel] || '-' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="分析日期">{{ detailData.analysisDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Chart Dialog -->
    <el-dialog title="资产集中度图表分析" :visible.sync="chartDialogVisible" width="900px" append-to-body @opened="initCharts" @close="destroyCharts">
      <el-row :gutter="20">
        <el-col :span="8">
          <div ref="pieChart" style="height:350px"></div>
        </el-col>
        <el-col :span="8">
          <div ref="barChart" style="height:350px"></div>
        </el-col>
        <el-col :span="8">
          <div ref="gaugeChart" style="height:350px"></div>
        </el-col>
      </el-row>
      <div slot="footer">
        <el-button @click="chartDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Trend Dialog -->
    <el-dialog title="集中度趋势分析" :visible.sync="trendDialogVisible" width="800px" append-to-body @opened="initTrendChart" @close="destroyTrendChart">
      <div ref="trendChart" style="height:400px"></div>
      <div slot="footer">
        <el-button @click="trendDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Compare Dialog -->
    <el-dialog title="企业集中度对比" :visible.sync="compareDialogVisible" width="900px" append-to-body>
      <el-table :data="compareData" border stripe>
        <el-table-column prop="companyName" label="企业名称" min-width="150" />
        <el-table-column prop="industry" label="行业" width="120" />
        <el-table-column prop="totalAssets" label="总资产(万元)" width="130" align="right">
          <template slot-scope="{ row }">{{ formatNumber(row.totalAssets) }}</template>
        </el-table-column>
        <el-table-column prop="hhiIndex" label="HHI指数" width="100" align="right">
          <template slot-scope="{ row }">{{ row.hhiIndex ? row.hhiIndex.toFixed(4) : '-' }}</template>
        </el-table-column>
        <el-table-column prop="concentrationLevel" label="集中度等级" width="110" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="concentrationTagType(row.concentrationLevel)" size="small">{{ concentrationLevelMap[row.concentrationLevel] || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="riskTagType(row.riskLevel)" size="small">{{ riskLevelMap[row.riskLevel] || '-' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="compareDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- Optimize Dialog -->
    <el-dialog title="资产分散优化建议" :visible.sync="optimizeDialogVisible" width="650px" append-to-body>
      <div v-if="optimizeData" class="optimize-content">
        <el-alert v-if="optimizeData.summary" :title="optimizeData.summary" type="info" show-icon :closable="false" style="margin-bottom:16px" />
        <div v-if="optimizeData.suggestions && optimizeData.suggestions.length">
          <h4 style="margin-bottom:10px">优化建议：</h4>
          <el-timeline>
            <el-timeline-item v-for="(item, idx) in optimizeData.suggestions" :key="idx" :type="idx === 0 ? 'primary' : 'info'">
              {{ item }}
            </el-timeline-item>
          </el-timeline>
        </div>
        <div v-else>
          <p>{{ optimizeData.text || '暂无优化建议' }}</p>
        </div>
      </div>
      <div v-else class="optimize-content">
        <p>暂无优化建议数据</p>
      </div>
      <div slot="footer">
        <el-button @click="optimizeDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import {
  getAssetConcentrationList,
  getAssetConcentrationById,
  addAssetConcentration,
  updateAssetConcentration,
  deleteAssetConcentration,
  calculateConcentrationIndex,
  getConcentrationStatistics,
  getConcentrationChartData,
  analyzeConcentrationTrend,
  compareConcentration,
  optimizeAssetDispersion,
  generateConcentrationReport,
  exportAssetConcentration
} from '@/api/stateAssets/assetConcentration'

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
  name: 'AssetConcentration',
  data() {
    return {
      loading: false,
      submitLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      statistics: {},
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        companyName: '',
        industry: '',
        concentrationLevel: '',
        riskLevel: ''
      },
      // Dialogs
      formDialogVisible: false,
      detailDialogVisible: false,
      chartDialogVisible: false,
      trendDialogVisible: false,
      compareDialogVisible: false,
      optimizeDialogVisible: false,
      dialogType: 'add',
      formData: {},
      detailData: {},
      compareData: [],
      optimizeData: null,
      chartRow: null,
      // Charts
      pieChartInstance: null,
      barChartInstance: null,
      gaugeChartInstance: null,
      trendChartInstance: null,
      // Maps
      concentrationLevelMap: {
        HIGH: '高集中',
        MEDIUM: '中等',
        LOW: '低集中',
        DISPERSED: '分散'
      },
      riskLevelMap: {
        HIGH: '高风险',
        MEDIUM: '中风险',
        LOW: '低风险'
      },
      // Form rules
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        companyId: [{ required: true, message: '请输入企业ID', trigger: 'blur' }],
        industry: [{ required: true, message: '请输入行业', trigger: 'blur' }],
        region: [{ required: true, message: '请输入地区', trigger: 'blur' }],
        totalAssets: [{ required: true, message: '请输入总资产', trigger: 'blur' }],
        concentrationLevel: [{ required: true, message: '请选择集中度等级', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        analysisDate: [{ required: true, message: '请选择分析日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    /** 获取列表数据 */
    async getList() {
      this.loading = true
      try {
        const res = await getAssetConcentrationList(this.queryForm)
        if (res.result === 200) {
          this.tableData = res.data.tlist || res.data.list || res.data.records || []
          this.total = res.data.totalRecord || res.data.total || 0
        } else {
          console.warn('查询返回异常', res)
        }
      } catch (e) {
        console.error('请求异常', e)
      } finally {
        this.loading = false
      }
    },
    /** 获取统计数据 */
    async getStatistics() {
      try {
        const res = await getConcentrationStatistics()
        if (res.result === 200) {
          this.statistics = res.data || {}
        }
      } catch (e) {
        console.error('获取统计数据失败', e)
      }
    },
    /** 查询 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.getList()
    },
    /** 重置 */
    resetQuery() {
      this.queryForm = { pageNum: 1, pageSize: 10, companyName: '', industry: '', concentrationLevel: '', riskLevel: '' }
      this.getList()
    },
    /** 分页 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.getList()
    },
    /** 多选 */
    handleSelectionChange(rows) {
      this.selectedRows = rows
    },
    /** 新增 */
    handleAdd() {
      this.dialogType = 'add'
      this.formData = { companyId: '', companyName: '', industry: '', region: '', totalAssets: 0, assetRatio: 0, hhiIndex: 0, concentrationLevel: '', topAssetType: '', topAssetRatio: 0, riskLevel: '', analysisDate: '', remark: '' }
      this.formDialogVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    /** 编辑 */
    handleEdit(row) {
      this.dialogType = 'edit'
      this.formData = { ...row }
      this.formDialogVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },

    /** 提交表单 */
    submitForm() {
      this.$refs.dataForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addAssetConcentration : updateAssetConcentration
          const res = await apiFn(this.formData)
          if (res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.formDialogVisible = false
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          console.error('请求异常', e)
        } finally {
          this.submitLoading = false
        }
      })
    },
    /** 查看详情 */
    async handleView(row) {
      try {
        const res = await getAssetConcentrationById(row.concentrationId)
        if (res.result === 200) {
          this.detailData = res.data || row
        } else {
          this.detailData = row
        }
      } catch (e) {
        this.detailData = row
      }
      this.detailDialogVisible = true
    },
    /** 计算集中度指数 */
    async handleCalculate(row) {
      try {
        const res = await calculateConcentrationIndex({ concentrationId: row.concentrationId, companyId: row.companyId })
        if (res.result === 200) {
          this.$message.success(`计算完成，HHI指数: ${res.data.hhiIndex || '-'}，集中度等级: ${this.concentrationLevelMap[res.data.concentrationLevel] || res.data.concentrationLevel || '-'}`)
          this.getList()
        } else {
          this.$message.error(res.msg || '计算失败')
        }
      } catch (e) {
        this.$message.error('计算请求失败')
      }
    },
    /** 图表分析 */
    async handleChart(row) {
      this.chartRow = row
      this.chartDialogVisible = true
    },
    /** 初始化图表 */
    async initCharts() {
      try {
        const res = await getConcentrationChartData({ concentrationId: this.chartRow.concentrationId, companyId: this.chartRow.companyId })
        const chartData = (res.result === 200 && res.data) ? res.data : {}
        this.$nextTick(() => {
          this.initPieChart(chartData.pieData || [])
          this.initBarChart(chartData.barData || [])
          this.initGaugeChart(this.chartRow.hhiIndex || 0)
        })
      } catch (e) {
        this.$nextTick(() => {
          this.initPieChart([])
          this.initBarChart([])
          this.initGaugeChart(this.chartRow.hhiIndex || 0)
        })
      }
    },
    initPieChart(data) {
      if (this.pieChartInstance) this.pieChartInstance.dispose()
      this.pieChartInstance = echarts.init(this.$refs.pieChart)
      const defaultData = data.length ? data : [{ name: '固定资产', value: 40 }, { name: '流动资产', value: 30 }, { name: '无形资产', value: 20 }, { name: '其他', value: 10 }]
      this.pieChartInstance.setOption({
        title: { text: '资产类型分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        series: [{ type: 'pie', radius: ['30%', '60%'], data: defaultData, label: { fontSize: 11 } }]
      })
    },
    initBarChart(data) {
      if (this.barChartInstance) this.barChartInstance.dispose()
      this.barChartInstance = echarts.init(this.$refs.barChart)
      const defaultData = data.length ? data : [{ name: '制造业', value: 0.35 }, { name: '金融业', value: 0.28 }, { name: '房地产', value: 0.22 }, { name: '科技', value: 0.15 }]
      this.barChartInstance.setOption({
        title: { text: '行业集中度对比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: defaultData.map(d => d.name), axisLabel: { fontSize: 10 } },
        yAxis: { type: 'value', name: 'HHI' },
        series: [{ type: 'bar', data: defaultData.map(d => d.value), itemStyle: { color: '#409EFF' } }]
      })
    },
    initGaugeChart(hhiValue) {
      if (this.gaugeChartInstance) this.gaugeChartInstance.dispose()
      this.gaugeChartInstance = echarts.init(this.$refs.gaugeChart)
      this.gaugeChartInstance.setOption({
        title: { text: 'HHI指数仪表盘', left: 'center', textStyle: { fontSize: 14 } },
        series: [{
          type: 'gauge', min: 0, max: 1, splitNumber: 5,
          axisLine: { lineStyle: { width: 15, color: [[0.25, '#67C23A'], [0.5, '#E6A23C'], [0.75, '#F56C6C'], [1, '#911']] } },
          pointer: { width: 5 },
          detail: { formatter: '{value}', fontSize: 16 },
          data: [{ value: parseFloat(hhiValue.toFixed(4)), name: 'HHI' }]
        }]
      })
    },
    destroyCharts() {
      if (this.pieChartInstance) { this.pieChartInstance.dispose(); this.pieChartInstance = null }
      if (this.barChartInstance) { this.barChartInstance.dispose(); this.barChartInstance = null }
      if (this.gaugeChartInstance) { this.gaugeChartInstance.dispose(); this.gaugeChartInstance = null }
    },
    /** 趋势分析 */
    async handleTrend(row) {
      this.chartRow = row
      this.trendDialogVisible = true
    },
    async initTrendChart() {
      try {
        const res = await analyzeConcentrationTrend({ concentrationId: this.chartRow.concentrationId, companyId: this.chartRow.companyId })
        const trendData = (res.result === 200 && res.data) ? res.data : {}
        this.$nextTick(() => { this.renderTrendChart(trendData) })
      } catch (e) {
        this.$nextTick(() => { this.renderTrendChart({}) })
      }
    },
    renderTrendChart(data) {
      if (this.trendChartInstance) this.trendChartInstance.dispose()
      this.trendChartInstance = echarts.init(this.$refs.trendChart)
      const dates = data.dates || ['2024-01', '2024-02', '2024-03', '2024-04', '2024-05', '2024-06']
      const values = data.values || [0.35, 0.38, 0.32, 0.40, 0.37, 0.34]
      this.trendChartInstance.setOption({
        title: { text: `${this.chartRow.companyName} - HHI指数趋势`, left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', name: 'HHI指数', min: 0, max: 1 },
        series: [{ type: 'line', data: values, smooth: true, areaStyle: { opacity: 0.3 }, itemStyle: { color: '#409EFF' } }]
      })
    },
    destroyTrendChart() {
      if (this.trendChartInstance) { this.trendChartInstance.dispose(); this.trendChartInstance = null }
    },
    /** 对比分析 */
    async handleCompare(row) {
      try {
        const res = await compareConcentration({ concentrationId: row.concentrationId, companyId: row.companyId })
        if (res.result === 200) {
          this.compareData = res.data || []
        } else {
          this.compareData = [row]
        }
      } catch (e) {
        this.compareData = [row]
      }
      this.compareDialogVisible = true
    },
    /** 优化建议 */
    async handleOptimize(row) {
      try {
        const res = await optimizeAssetDispersion({ concentrationId: row.concentrationId, companyId: row.companyId })
        if (res.result === 200) {
          this.optimizeData = res.data || { text: '暂无优化建议' }
        } else {
          this.optimizeData = { text: res.msg || '获取优化建议失败' }
        }
      } catch (e) {
        this.optimizeData = { text: '请求失败，请稍后重试' }
      }
      this.optimizeDialogVisible = true
    },
    /** 生成报告 */
    async handleReport(row) {
      try {
        const res = await generateConcentrationReport({ concentrationId: row.concentrationId, companyId: row.companyId })
        if (res.result === 200 && res.data) {
          const d = res.data
          this.$alert(
            `<div style="line-height:2">
              <p><b>报告标题：</b>${d.reportTitle || '资产集中度分析报告'}</p>
              <p><b>企业名称：</b>${d.companyName || row.companyName}</p>
              <p><b>HHI指数：</b>${d.hhiIndex || '-'}</p>
              <p><b>集中度等级：</b>${this.concentrationLevelMap[d.concentrationLevel] || d.concentrationLevel || '-'}</p>
              <p><b>风险等级：</b>${this.riskLevelMap[d.riskLevel] || d.riskLevel || '-'}</p>
              <p><b>结论：</b>${d.conclusion || '-'}</p>
              <p><b>生成时间：</b>${d.generatedTime || '-'}</p>
            </div>`,
            '集中度分析报告',
            { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
          )
        } else {
          this.$message.warning(res.msg || '报告生成失败')
        }
      } catch (e) {
        this.$message.error('报告生成请求失败')
      }
    },
    /** 删除 */
    handleDelete(row) {
      this.$confirm(`确认删除企业"${row.companyName}"的集中度分析数据？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteAssetConcentration(row.concentrationId)
          if (res.result === 200) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除请求失败')
        }
      }).catch(() => {})
    },
    /** 导出 */
    async handleExport() {
      try {
        const res = await exportAssetConcentration(this.queryForm)
        if (res instanceof Blob || (res && res.type && res.type.indexOf('application') > -1)) {
          const url = window.URL.createObjectURL(new Blob([res]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', '资产集中度分析数据.xlsx')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else if (res.result === 200) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      } catch (e) {
        this.$message.error('导出请求失败')
      }
    },
    /** 批量计算 */
    async handleBatchCalculate() {
      if (!this.selectedRows.length) {
        this.$message.warning('请先选择需要计算的数据')
        return
      }
      this.$confirm(`确认对选中的${this.selectedRows.length}条数据进行批量计算？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const ids = this.selectedRows.map(r => r.concentrationId)
          const res = await calculateConcentrationIndex({ ids })
          if (res.result === 200) {
            this.$message.success('批量计算完成')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '批量计算失败')
          }
        } catch (e) {
          this.$message.error('批量计算请求失败')
        }
      }).catch(() => {})
    },
    /** 更多操作命令 */
    handleCommand(cmd, row) {
      switch (cmd) {
        case 'edit': this.handleEdit(row); break
        case 'trend': this.handleTrend(row); break
        case 'compare': this.handleCompare(row); break
        case 'optimize': this.handleOptimize(row); break
        case 'report': this.handleReport(row); break
        case 'delete': this.handleDelete(row); break
      }
    },
    /** 格式化数字 */
    formatNumber(val) {
      if (val === null || val === undefined) return '-'
      return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    /** 集中度等级标签类型 */
    concentrationTagType(level) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success', DISPERSED: 'info' }
      return map[level] || 'info'
    },
    /** 风险等级标签类型 */
    riskTagType(level) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[level] || 'info'
    }
  },
  beforeDestroy() {
    this.destroyCharts()
    this.destroyTrendChart()
  }
}
</script>

<style scoped>
.asset-concentration-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100%;
}
.page-header {
  border-radius: 8px;
  padding: 24px 30px;
  margin-bottom: 20px;
}
.header-content .page-title {
  color: #fff;
  font-size: 22px;
  margin: 0 0 8px 0;
}
.header-content .page-desc {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  margin: 0;
}
.stat-cards {
  margin-bottom: 20px;
}
.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}
.stat-icon i {
  font-size: 24px;
  color: #fff;
}
.stat-info .stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}
.stat-info .stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
.filter-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px 20px 4px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.toolbar-section {
  margin-bottom: 16px;
}
.pagination-section {
  margin-top: 16px;
  text-align: right;
}
.optimize-content {
  padding: 10px 0;
  line-height: 1.8;
}
</style>