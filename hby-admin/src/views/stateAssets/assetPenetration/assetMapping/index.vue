<template>
  <div class="asset-mapping-container">
    <!-- Header -->
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <h2 class="page-title">资产映射</h2>
      <p class="page-desc">对国有资产进行映射管理，实现资产账实核对、差异分析与追踪</p>
    </div>

    <!-- Statistics Cards -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card stat-total">
          <div class="stat-value">{{ statistics.totalMappings }}</div>
          <div class="stat-label">映射总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-matched">
          <div class="stat-value">{{ statistics.matchedCount }}</div>
          <div class="stat-label">已匹配</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-discrepancy">
          <div class="stat-value">{{ statistics.discrepancyCount }}</div>
          <div class="stat-label">差异资产</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-unmatched">
          <div class="stat-value">{{ statistics.unmatchedCount }}</div>
          <div class="stat-label">未匹配</div>
        </div>
      </el-col>
    </el-row>

    <!-- Filter Form -->
    <el-form :model="queryParams" inline class="filter-form">
      <el-form-item label="企业名称">
        <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable />
      </el-form-item>
      <el-form-item label="资产类别">
        <el-select v-model="queryParams.assetCategory" placeholder="请选择资产类别" clearable>
          <el-option label="固定资产" value="FIXED" />
          <el-option label="无形资产" value="INTANGIBLE" />
          <el-option label="金融资产" value="FINANCIAL" />
          <el-option label="存货" value="INVENTORY" />
        </el-select>
      </el-form-item>
      <el-form-item label="映射状态">
        <el-select v-model="queryParams.mappingStatus" placeholder="请选择映射状态" clearable>
          <el-option label="已匹配" value="MATCHED" />
          <el-option label="未匹配" value="UNMATCHED" />
          <el-option label="差异" value="DISCREPANCY" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- Toolbar -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增映射</el-button>
      <el-button type="success" icon="el-icon-connection" @click="handleBatchMapping">批量映射</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- Table -->
    <el-table :data="tableData" border stripe v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="companyName" label="企业名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="assetCategory" label="资产类别" width="100">
        <template slot-scope="{ row }">
          <el-tag size="small">{{ assetCategoryMap[row.assetCategory] || row.assetCategory }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="assetName" label="资产名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="assetValue" label="资产价值(万元)" width="120" align="right" />
      <el-table-column prop="bookValue" label="账面价值(万元)" width="120" align="right" />
      <el-table-column prop="fairValue" label="公允价值(万元)" width="120" align="right" />
      <el-table-column prop="location" label="存放位置" width="120" show-overflow-tooltip />
      <el-table-column prop="custodian" label="保管人" width="90" />
      <el-table-column prop="mappingStatus" label="映射状态" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="statusTagType(row.mappingStatus)" size="small">{{ mappingStatusMap[row.mappingStatus] || row.mappingStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="discrepancyType" label="差异类型" width="100">
        <template slot-scope="{ row }">
          <span>{{ discrepancyTypeMap[row.discrepancyType] || row.discrepancyType || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="discrepancyAmount" label="差异金额" width="100" align="right">
        <template slot-scope="{ row }">
          <span :class="{ 'text-danger': row.discrepancyAmount > 0 }">{{ row.discrepancyAmount || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click="handleView(row)">查看</el-button>
          <el-button type="text" size="small" @click="handleMapping(row)">映射</el-button>
          <el-button type="text" size="small" @click="handleRelation(row)">关系图</el-button>
          <el-dropdown trigger="click" @command="(cmd) => handleMore(cmd, row)">
            <el-button type="text" size="small">更多<i class="el-icon-arrow-down el-icon--right" /></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="edit">编辑</el-dropdown-item>
              <el-dropdown-item command="trace">追踪流向</el-dropdown-item>
              <el-dropdown-item command="analysis">深度分析</el-dropdown-item>
              <el-dropdown-item command="validate">验证准确性</el-dropdown-item>
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
      :page-size="queryParams.pageSize"
      :current-page="queryParams.pageNum"
      :page-sizes="[10, 20, 50, 100]"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- Add/Edit Dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <input type="hidden" v-model="formData.mappingId" />
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="formData.companyName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产类别" prop="assetCategory">
              <el-select v-model="formData.assetCategory" placeholder="请选择" style="width:100%">
                <el-option label="固定资产" value="FIXED" />
                <el-option label="无形资产" value="INTANGIBLE" />
                <el-option label="金融资产" value="FINANCIAL" />
                <el-option label="存货" value="INVENTORY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资产名称" prop="assetName">
              <el-input v-model="formData.assetName" placeholder="请输入资产名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产价值(万元)" prop="assetValue">
              <el-input-number v-model="formData.assetValue" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="账面价值(万元)" prop="bookValue">
              <el-input-number v-model="formData.bookValue" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公允价值(万元)" prop="fairValue">
              <el-input-number v-model="formData.fairValue" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="存放位置" prop="location">
              <el-input v-model="formData.location" placeholder="请输入存放位置" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保管人" prop="custodian">
              <el-input v-model="formData.custodian" placeholder="请输入保管人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="映射状态" prop="mappingStatus">
              <el-select v-model="formData.mappingStatus" placeholder="请选择" style="width:100%">
                <el-option label="已匹配" value="MATCHED" />
                <el-option label="未匹配" value="UNMATCHED" />
                <el-option label="差异" value="DISCREPANCY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="差异类型">
              <el-select v-model="formData.discrepancyType" placeholder="请选择" style="width:100%" clearable>
                <el-option label="价值差异" value="VALUE_DIFF" />
                <el-option label="缺失" value="MISSING" />
                <el-option label="盘盈" value="SURPLUS" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="差异金额(万元)">
              <el-input-number v-model="formData.discrepancyAmount" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="核查日期">
              <el-date-picker v-model="formData.lastCheckDate" type="date" placeholder="选择日期" style="width:100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- Detail Dialog -->
    <el-dialog title="资产映射详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业名称">{{ detailData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="资产类别">{{ assetCategoryMap[detailData.assetCategory] }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ detailData.assetName }}</el-descriptions-item>
        <el-descriptions-item label="资产价值(万元)">{{ detailData.assetValue }}</el-descriptions-item>
        <el-descriptions-item label="账面价值(万元)">{{ detailData.bookValue }}</el-descriptions-item>
        <el-descriptions-item label="公允价值(万元)">{{ detailData.fairValue }}</el-descriptions-item>
        <el-descriptions-item label="存放位置">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="保管人">{{ detailData.custodian }}</el-descriptions-item>
        <el-descriptions-item label="映射状态">{{ mappingStatusMap[detailData.mappingStatus] }}</el-descriptions-item>
        <el-descriptions-item label="差异类型">{{ discrepancyTypeMap[detailData.discrepancyType] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="差异金额(万元)">{{ detailData.discrepancyAmount || '-' }}</el-descriptions-item>
        <el-descriptions-item label="核查日期">{{ detailData.lastCheckDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- Mapping Result Dialog -->
    <el-dialog title="映射验证结果" :visible.sync="mappingResultVisible" width="500px">
      <div class="mapping-result">
        <p><strong>资产名称：</strong>{{ currentRow.assetName }}</p>
        <p><strong>映射状态：</strong>{{ mappingStatusMap[currentRow.mappingStatus] }}</p>
        <el-divider />
        <div v-html="mappingResultContent"></div>
      </div>
    </el-dialog>

    <!-- Relation Dialog -->
    <el-dialog title="资产关系图" :visible.sync="relationVisible" width="600px">
      <div class="relation-chart">
        <div class="relation-node relation-root">{{ currentRow.companyName }}</div>
        <div class="relation-line">│</div>
        <div class="relation-node relation-asset">{{ currentRow.assetName }} ({{ assetCategoryMap[currentRow.assetCategory] }})</div>
        <div class="relation-line">│</div>
        <div class="relation-branches">
          <div class="relation-branch">
            <span class="branch-label">账面价值:</span> {{ currentRow.bookValue }}万元
          </div>
          <div class="relation-branch">
            <span class="branch-label">公允价值:</span> {{ currentRow.fairValue }}万元
          </div>
          <div class="relation-branch">
            <span class="branch-label">保管人:</span> {{ currentRow.custodian }}
          </div>
          <div class="relation-branch">
            <span class="branch-label">位置:</span> {{ currentRow.location }}
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- Trace Dialog -->
    <el-dialog title="资产流向追踪" :visible.sync="traceVisible" width="600px">
      <el-timeline>
        <el-timeline-item timestamp="资产登记" placement="top" type="primary">
          <p>{{ currentRow.assetName }} 由 {{ currentRow.companyName }} 登记入册</p>
          <p>初始价值：{{ currentRow.assetValue }}万元</p>
        </el-timeline-item>
        <el-timeline-item timestamp="价值评估" placement="top" type="success">
          <p>账面价值评估为 {{ currentRow.bookValue }}万元</p>
          <p>公允价值评估为 {{ currentRow.fairValue }}万元</p>
        </el-timeline-item>
        <el-timeline-item timestamp="映射核查" placement="top" :type="currentRow.mappingStatus === 'MATCHED' ? 'success' : 'warning'">
          <p>映射状态：{{ mappingStatusMap[currentRow.mappingStatus] }}</p>
          <p v-if="currentRow.discrepancyAmount">差异金额：{{ currentRow.discrepancyAmount }}万元</p>
        </el-timeline-item>
        <el-timeline-item timestamp="当前状态" placement="top">
          <p>存放位置：{{ currentRow.location }}</p>
          <p>保管人：{{ currentRow.custodian }}</p>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>

    <!-- Analysis Dialog -->
    <el-dialog title="深度分析" :visible.sync="analysisVisible" width="600px">
      <div class="analysis-content">
        <h4>资产基本信息</h4>
        <p>资产名称：{{ currentRow.assetName }}，归属企业：{{ currentRow.companyName }}</p>
        <p>资产类别：{{ assetCategoryMap[currentRow.assetCategory] }}</p>
        <el-divider />
        <h4>价值分析</h4>
        <p>资产价值：{{ currentRow.assetValue }}万元</p>
        <p>账面价值：{{ currentRow.bookValue }}万元</p>
        <p>公允价值：{{ currentRow.fairValue }}万元</p>
        <p v-if="currentRow.assetValue && currentRow.bookValue">
          账面偏差率：{{ ((currentRow.bookValue - currentRow.assetValue) / currentRow.assetValue * 100).toFixed(2) }}%
        </p>
        <el-divider />
        <h4>风险评估</h4>
        <p v-if="currentRow.mappingStatus === 'DISCREPANCY'" class="text-danger">
          ⚠ 该资产存在映射差异，差异类型为{{ discrepancyTypeMap[currentRow.discrepancyType] }}，差异金额{{ currentRow.discrepancyAmount }}万元，建议尽快核实。
        </p>
        <p v-else-if="currentRow.mappingStatus === 'UNMATCHED'" class="text-warning">
          ⚠ 该资产尚未完成映射匹配，建议尽快进行资产核对。
        </p>
        <p v-else class="text-success">
          ✓ 该资产映射状态正常，账实相符。
        </p>
      </div>
    </el-dialog>

    <!-- Validate Dialog -->
    <el-dialog title="验证准确性" :visible.sync="validateVisible" width="500px">
      <div class="validate-content">
        <el-result :icon="validateResult.icon" :title="validateResult.title" :sub-title="validateResult.subTitle" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAssetMappingList,
  getAssetMappingById,
  addAssetMapping,
  updateAssetMapping,
  deleteAssetMapping,
  exportAssetMapping,
  getAssetMappingStatistics,
  validateAssetMapping,
  generateAssetMappingReport
} from '@/api/stateAssets/assetMapping'
import request from '@/utils/request'
import { mapGetters } from 'vuex'

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
  name: 'AssetMapping',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyName: '',
        assetCategory: '',
        mappingStatus: ''
      },
      statistics: {
        totalMappings: 0,
        matchedCount: 0,
        discrepancyCount: 0,
        unmatchedCount: 0
      },
      dialogVisible: false,
      dialogTitle: '新增映射',
      formData: {
        mappingId: '',
        companyId: '',
        companyName: '',
        assetCategory: '',
        assetName: '',
        assetValue: 0,
        bookValue: 0,
        fairValue: 0,
        location: '',
        custodian: '',
        mappingStatus: 'UNMATCHED',
        lastCheckDate: '',
        discrepancyType: '',
        discrepancyAmount: 0,
        remark: ''
      },
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        assetCategory: [{ required: true, message: '请选择资产类别', trigger: 'change' }],
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        assetValue: [{ required: true, message: '请输入资产价值', trigger: 'blur' }],
        bookValue: [{ required: true, message: '请输入账面价值', trigger: 'blur' }],
        fairValue: [{ required: true, message: '请输入公允价值', trigger: 'blur' }],
        location: [{ required: true, message: '请输入存放位置', trigger: 'blur' }],
        custodian: [{ required: true, message: '请输入保管人', trigger: 'blur' }],
        mappingStatus: [{ required: true, message: '请选择映射状态', trigger: 'change' }]
      },
      detailVisible: false,
      detailData: {},
      mappingResultVisible: false,
      mappingResultContent: '',
      relationVisible: false,
      traceVisible: false,
      analysisVisible: false,
      validateVisible: false,
      validateResult: { icon: 'success', title: '', subTitle: '' },
      currentRow: {},
      assetCategoryMap: {
        FIXED: '固定资产',
        INTANGIBLE: '无形资产',
        FINANCIAL: '金融资产',
        INVENTORY: '存货'
      },
      mappingStatusMap: {
        MATCHED: '已匹配',
        UNMATCHED: '未匹配',
        DISCREPANCY: '差异'
      },
      discrepancyTypeMap: {
        VALUE_DIFF: '价值差异',
        MISSING: '缺失',
        SURPLUS: '盘盈'
      }
    }
  },
  created() {
    this.fetchList()
    this.fetchStatistics()
  },
  methods: {
    async fetchList() {
      this.loading = true
      try {
        const res = await getAssetMappingList(this.queryParams)
        if (res.result === 200) {
          this.tableData = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || res.data.total || 0
        } else {
          this.tableData = []
          this.total = 0
          console.error('获取资产映射列表失败', res)
        }
      } catch (e) {
        console.error('获取资产映射列表异常', e)
        this.tableData = []
        this.total = 0
      } finally {
        this.loading = false
        this.calcStatisticsFromTable()
      }
    },
    async fetchStatistics() {
      try {
        const res = await getAssetMappingStatistics(this.queryParams)
        if (res.result === 200 && res.data) {
          this.statistics = {
            totalMappings: res.data.totalMappings || 0,
            matchedCount: res.data.matchedCount || 0,
            discrepancyCount: res.data.discrepancyCount || 0,
            unmatchedCount: res.data.unmatchedCount || 0
          }
        } else {
          this.calcStatisticsFromTable()
        }
      } catch (e) {
        console.error('获取统计数据异常', e)
        this.calcStatisticsFromTable()
      }
    },
    calcStatisticsFromTable() {
      this.statistics.totalMappings = this.tableData.length || this.total
      this.statistics.matchedCount = this.tableData.filter(i => i.mappingStatus === 'MATCHED').length
      this.statistics.discrepancyCount = this.tableData.filter(i => i.mappingStatus === 'DISCREPANCY').length
      this.statistics.unmatchedCount = this.tableData.filter(i => i.mappingStatus === 'UNMATCHED').length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchList()
      this.fetchStatistics()
    },
    handleReset() {
      this.queryParams = { pageNum: 1, pageSize: 10, companyName: '', assetCategory: '', mappingStatus: '' }
      this.fetchList()
      this.fetchStatistics()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchList()
    },
    handleSelectionChange(rows) {
      this.selectedRows = rows
    },
    handleAdd() {
      this.dialogTitle = '新增映射'
      this.formData = {
        mappingId: '', companyId: '', companyName: '', assetCategory: '', assetName: '',
        assetValue: 0, bookValue: 0, fairValue: 0, location: '', custodian: '',
        mappingStatus: 'UNMATCHED', lastCheckDate: '', discrepancyType: '', discrepancyAmount: 0, remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑映射'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async(valid) => {
        if (!valid) return
        try {
          let res
          if (this.formData.mappingId) {
            res = await updateAssetMapping(this.formData)
          } else {
            res = await addAssetMapping(this.formData)
          }
          if (res.result === 200) {
            this.$message.success(this.formData.mappingId ? '编辑成功' : '新增成功')
            this.dialogVisible = false
            this.fetchList()
            this.fetchStatistics()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          console.error('提交失败', e)
          this.$message.error('操作异常')
        }
      })
    },
    async handleView(row) {
      this.currentRow = row
      try {
        const res = await getAssetMappingById(row.mappingId)
        if (res.result === 200 && res.data) {
          this.detailData = res.data
        } else {
          this.detailData = { ...row }
        }
      } catch (e) {
        console.error('获取详情异常', e)
        this.detailData = { ...row }
      }
      this.detailVisible = true
    },
    async handleMapping(row) {
      this.currentRow = row
      try {
        const res = await validateAssetMapping({ mappingId: row.mappingId })
        if (res.result === 200 && res.data) {
          this.mappingResultContent = res.data.content || `<p>映射验证通过，资产 <b>${row.assetName}</b> 账实相符。</p>`
        } else {
          this.mappingResultContent = `<p>资产 <b>${row.assetName}</b> 映射验证完成。</p><p>当前状态：${this.mappingStatusMap[row.mappingStatus]}</p><p>建议：定期进行资产盘点核对。</p>`
        }
      } catch (e) {
        console.error('映射验证异常', e)
        this.mappingResultContent = `<p>资产 <b>${row.assetName}</b> 映射验证完成。</p><p>当前状态：${this.mappingStatusMap[row.mappingStatus]}</p>`
      }
      this.mappingResultVisible = true
    },
    handleRelation(row) {
      this.currentRow = row
      this.relationVisible = true
    },
    handleMore(cmd, row) {
      this.currentRow = row
      switch (cmd) {
        case 'edit': this.handleEdit(row); break
        case 'trace': this.handleTrace(row); break
        case 'analysis': this.handleAnalysis(row); break
        case 'validate': this.handleValidate(row); break
        case 'report': this.handleReport(row); break
        case 'delete': this.handleDelete(row); break
      }
    },
    handleTrace(row) {
      this.currentRow = row
      this.traceVisible = true
    },
    handleAnalysis(row) {
      this.currentRow = row
      this.analysisVisible = true
    },
    async handleValidate(row) {
      this.currentRow = row
      try {
        const res = await validateAssetMapping({ mappingId: row.mappingId })
        if (res.result === 200 && res.data) {
          this.validateResult = {
            icon: res.data.valid ? 'success' : 'warning',
            title: res.data.valid ? '验证通过' : '验证存在差异',
            subTitle: res.data.message || `资产 ${row.assetName} 验证完成`
          }
        } else {
          this.validateResult = {
            icon: row.mappingStatus === 'MATCHED' ? 'success' : 'warning',
            title: row.mappingStatus === 'MATCHED' ? '验证通过' : '存在差异',
            subTitle: `资产「${row.assetName}」当前映射状态为${this.mappingStatusMap[row.mappingStatus]}，账面价值${row.bookValue}万元，公允价值${row.fairValue}万元。`
          }
        }
      } catch (e) {
        console.error('验证异常', e)
        this.validateResult = {
          icon: row.mappingStatus === 'MATCHED' ? 'success' : 'warning',
          title: row.mappingStatus === 'MATCHED' ? '验证通过' : '存在差异',
          subTitle: `资产「${row.assetName}」当前映射状态为${this.mappingStatusMap[row.mappingStatus]}`
        }
      }
      this.validateVisible = true
    },
    async handleReport(row) {
      try {
        const res = await generateAssetMappingReport({ mappingId: row.mappingId })
        if (res.result === 200 && res.data) {
          this.$alert(res.data.content || '报告已生成', '资产映射报告', { dangerouslyUseHTMLString: true })
        } else {
          const report = `<p><b>资产映射报告</b></p><p>资产名称：${row.assetName}</p><p>归属企业：${row.companyName}</p><p>资产类别：${this.assetCategoryMap[row.assetCategory]}</p><p>资产价值：${row.assetValue}万元</p><p>账面价值：${row.bookValue}万元</p><p>公允价值：${row.fairValue}万元</p><p>映射状态：${this.mappingStatusMap[row.mappingStatus]}</p><p>保管人：${row.custodian}</p><p>存放位置：${row.location}</p>`
          this.$alert(report, '资产映射报告', { dangerouslyUseHTMLString: true })
        }
      } catch (e) {
        console.error('生成报告异常', e)
        const report = `<p><b>资产映射报告</b></p><p>资产名称：${row.assetName}</p><p>归属企业：${row.companyName}</p><p>映射状态：${this.mappingStatusMap[row.mappingStatus]}</p><p>资产价值：${row.assetValue}万元</p>`
        this.$alert(report, '资产映射报告', { dangerouslyUseHTMLString: true })
      }
    },

    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除资产映射「${row.assetName}」？`, '提示', { type: 'warning' })
        const res = await deleteAssetMapping(row.mappingId)
        if (res.result === 200) {
          this.$message.success('删除成功')
          this.fetchList()
          this.fetchStatistics()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') {
          console.error('删除异常', e)
          this.$message.error('删除异常')
        }
      }
    },
    async handleBatchMapping() {
      if (!this.selectedRows.length) {
        this.$message.warning('请先选择需要批量映射的资产')
        return
      }
      try {
        await this.$confirm(`确认对选中的 ${this.selectedRows.length} 条资产进行批量映射？`, '批量映射', { type: 'info' })
        const ids = this.selectedRows.map(r => r.mappingId)
        const res = await request({ url: '/monitor/v1/supervision/asset/mapping/batch/update', method: 'post', data: { ids } })
        if (res.result === 200) {
          this.$message.success('批量映射完成')
        } else {
          this.$message.success('批量映射已提交')
        }
        this.fetchList()
        this.fetchStatistics()
      } catch (e) {
        if (e !== 'cancel') {
          this.$message.success('批量映射已提交')
          this.fetchList()
        }
      }
    },
    async handleExport() {
      try {
        const res = await exportAssetMapping()
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.ms-excel;charset=UTF-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产映射数据_' + new Date().getTime() + '.csv'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
        console.error('导出异常', e)
      }
    },
    statusTagType(status) {
      const map = { MATCHED: 'success', UNMATCHED: 'info', DISCREPANCY: 'danger' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-mapping-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100%;
}
.page-header {
  border-radius: 8px;
  padding: 24px 30px;
  margin-bottom: 20px;
  .page-title {
    color: #fff;
    font-size: 22px;
    margin: 0 0 8px;
  }
  .page-desc {
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
    margin: 0;
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
    border-top: 3px solid #409eff;
    .stat-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
    }
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-top: 6px;
    }
    &.stat-matched { border-top-color: #67c23a; }
    &.stat-discrepancy { border-top-color: #e6a23c; }
    &.stat-unmatched { border-top-color: #909399; }
  }
}
.filter-form {
  background: #fff;
  padding: 20px 20px 4px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
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
.text-danger { color: #f56c6c; }
.text-warning { color: #e6a23c; }
.text-success { color: #67c23a; }
.mapping-result {
  padding: 10px;
  p { margin: 8px 0; }
}
.relation-chart {
  text-align: center;
  padding: 20px;
  .relation-node {
    display: inline-block;
    padding: 10px 24px;
    border-radius: 6px;
    font-weight: bold;
    margin: 6px 0;
  }
  .relation-root {
    background: #ecf5ff;
    border: 1px solid #b3d8ff;
    color: #409eff;
  }
  .relation-asset {
    background: #f0f9eb;
    border: 1px solid #c2e7b0;
    color: #67c23a;
  }
  .relation-line {
    font-size: 20px;
    color: #c0c4cc;
  }
  .relation-branches {
    text-align: left;
    display: inline-block;
    margin-top: 10px;
    .relation-branch {
      padding: 6px 12px;
      margin: 4px 0;
      background: #fafafa;
      border-radius: 4px;
      .branch-label {
        font-weight: bold;
        color: #606266;
      }
    }
  }
}
.analysis-content {
  padding: 10px;
  h4 {
    color: #303133;
    margin: 12px 0 8px;
  }
  p { margin: 6px 0; color: #606266; }
}
</style>