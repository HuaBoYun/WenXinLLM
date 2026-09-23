<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划分析管理</h2>
      <p>管理资金计划的各类分析报告，包括差异分析、趋势分析、风险分析等</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.analysisType"
        placeholder="分析类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in analysisTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.isValid"
        placeholder="有效性"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="有效" :value="1" />
        <el-option label="无效" :value="0" />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="创建开始日期"
        end-placeholder="创建结束日期"
        value-format="yyyy-MM-dd"
        class="filter-item"
        style="width: 260px"
        @change="handleDateRangeChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        新建分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showQualityAnalysis"
      >
        质量分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-trend-charts"
        @click="showTrendAnalysis"
      >
        趋势分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="danger"
        icon="el-icon-delete"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
    </div>
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">分析总数</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.validCount || 0 }}</div>
            <div class="statistics-label">有效分析</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgQualityScore || 0 }}分</div>
            <div class="statistics-label">平均质量分</div>
          </div>
          <i class="el-icon-star-on statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.highQualityCount || 0 }}</div>
            <div class="statistics-label">高质量分析</div>
          </div>
          <i class="el-icon-trophy statistics-icon"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="分析编号"
        prop="analysisNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.analysisNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="分析类型"
        prop="analysisType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="analysisTypeTagMap[row.analysisType]">
            {{ analysisTypeMap[row.analysisType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="分析周期"
        prop="analysisPeriod"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.analysisPeriod || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="质量分数"
        prop="qualityScore"
        width="100"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span :class="getQualityScoreClass(row.qualityScore)">
            {{ row.qualityScore || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="有效性"
        prop="isValid"
        width="80"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="row.isValid ? 'success' : 'danger'" size="mini">
            {{ row.isValid ? '有效' : '无效' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="有效期至"
        prop="validUntil"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.validUntil">{{ row.validUntil | parseTime('{y}-{m}-{d}') }}</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="价值评估"
        prop="valueAssessment"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.valueAssessment" :type="valueTagMap[row.valueAssessment]" size="mini">
            {{ valueMap[row.valueAssessment] }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="模板来源"
        prop="templateId"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.templateId" type="info" size="mini">
            模板生成
          </el-tag>
          <span v-else class="text-muted">手工创建</span>
        </template>
      </el-table-column>
      <el-table-column
        label="分析内容"
        prop="analysisContent"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ getContentSummary(row.analysisContent) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createdTime"
        width="160"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span v-if="row.createdTime">{{ row.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="!row.isValid"
            size="mini"
            type="success"
            @click="handleValidate(row)"
          >
            验证
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
          <el-dropdown
            trigger="click"
            @command="(command) => handleCommand(command, row)"
          >
            <el-button size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="detail">查看详情</el-dropdown-item>
              <el-dropdown-item command="copy">复制分析</el-dropdown-item>
              <el-dropdown-item command="export">导出报告</el-dropdown-item>
              <el-dropdown-item command="template">生成模板</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 新增/编辑分析对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新建分析' : '编辑分析'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="100px" style="width:500px;margin-left:30px;">
        <el-form-item label="分析编号" prop="analysisNo">
          <el-input v-model="temp.analysisNo" placeholder="请输入分析编号" />
        </el-form-item>
        <el-form-item label="分析类型" prop="analysisType">
          <el-select v-model="temp.analysisType" placeholder="请选择分析类型" style="width:100%">
            <el-option v-for="item in analysisTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析周期" prop="analysisPeriod">
          <el-input v-model="temp.analysisPeriod" placeholder="如：2024年Q1" />
        </el-form-item>
        <el-form-item label="质量分数">
          <el-input-number v-model="temp.qualityScore" :min="0" :max="100" :precision="0" style="width:100%" placeholder="0~100" />
        </el-form-item>
        <el-form-item label="价值评估">
          <el-select v-model="temp.valueAssessment" placeholder="请选择价值评估" style="width:100%" clearable>
            <el-option label="高价值" value="HIGH" />
            <el-option label="中等价值" value="MEDIUM" />
            <el-option label="低价值" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期至">
          <el-date-picker v-model="temp.validUntil" type="date" placeholder="选择有效期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="分析内容">
          <el-input v-model="temp.analysisContent" type="textarea" :rows="4" placeholder="请输入分析内容" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 验证对话框 -->
    <el-dialog
      title="分析验证"
      :visible.sync="validateDialogVisible"
      width="500px"
      @close="resetValidateForm"
    >
      <el-form
        ref="validateForm"
        :model="validateForm"
        :rules="validateRules"
        label-width="100px"
      >
        <el-form-item label="分析编号">
          <el-input v-model="validateForm.analysisNo" disabled />
        </el-form-item>
        <el-form-item label="分析类型">
          <el-input v-model="validateForm.analysisTypeText" disabled />
        </el-form-item>
        <el-form-item label="质量分数" prop="qualityScore">
          <el-input-number
            v-model="validateForm.qualityScore"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="请输入质量分数"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="价值评估" prop="valueAssessment">
          <el-select v-model="validateForm.valueAssessment" placeholder="请选择价值评估" style="width: 100%">
            <el-option label="高价值" value="HIGH" />
            <el-option label="中等价值" value="MEDIUM" />
            <el-option label="低价值" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期至" prop="validUntil">
          <el-date-picker
            v-model="validateForm.validUntil"
            type="date"
            placeholder="选择有效期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="验证说明">
          <el-input
            v-model="validateForm.validateNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入验证说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="validateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmValidate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 质量分析弹窗 -->
    <el-dialog title="质量分析" :visible.sync="qualityDialogVisible" width="700px" @opened="renderQualityChart">
      <div ref="qualityChart" style="width:100%;height:360px;" />
    </el-dialog>

    <!-- 趋势分析弹窗 -->
    <el-dialog title="趋势分析" :visible.sync="trendDialogVisible" width="700px" @opened="renderTrendChart">
      <div ref="trendChart" style="width:100%;height:360px;" />
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanAnalysisPage, createFundPlanAnalysis, updateFundPlanAnalysis, deleteFundPlanAnalysis,
         getFundPlanAnalysisSummary, validateFundPlanAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'
import * as echarts from 'echarts'

export default {
  name: 'FundPlanAnalysis',
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        analysisType: undefined,
        isValid: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-createdTime'
      },
      dateRange: null,
      summaryInfo: {},
      validateDialogVisible: false,
      validateForm: {
        analysisId: null,
        analysisNo: '',
        analysisTypeText: '',
        qualityScore: null,
        valueAssessment: '',
        validUntil: null,
        validateNotes: ''
      },
      validateRules: {
        qualityScore: [
          { required: true, message: '请输入质量分数', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: '质量分数必须在0-100之间', trigger: 'blur' }
        ],
        valueAssessment: [
          { required: true, message: '请选择价值评估', trigger: 'change' }
        ],
        validUntil: [
          { required: true, message: '请选择有效期', trigger: 'change' }
        ]
      },
      analysisTypeOptions: [
        { label: '差异分析', value: 'VARIANCE' },
        { label: '趋势分析', value: 'TREND' },
        { label: '预测分析', value: 'FORECAST' },
        { label: '风险分析', value: 'RISK' },
        { label: '绩效分析', value: 'PERFORMANCE' },
        { label: '敏感性分析', value: 'SENSITIVITY' }
      ],
      analysisTypeMap: {
        'VARIANCE': '差异分析',
        'TREND': '趋势分析',
        'FORECAST': '预测分析',
        'RISK': '风险分析',
        'PERFORMANCE': '绩效分析',
        'SENSITIVITY': '敏感性分析'
      },
      analysisTypeTagMap: {
        'VARIANCE': 'primary',
        'TREND': 'success',
        'FORECAST': 'info',
        'RISK': 'danger',
        'PERFORMANCE': 'warning',
        'SENSITIVITY': ''
      },
      valueMap: {
        'HIGH': '高价值',
        'MEDIUM': '中等价值',
        'LOW': '低价值'
      },
      valueTagMap: {
        'HIGH': 'success',
        'MEDIUM': 'warning',
        'LOW': 'info'
      },
      multipleSelection: [],
      dialogFormVisible: false,
      dialogStatus: '',
      qualityDialogVisible: false,
      trendDialogVisible: false,
      qualityDialogVisible: false,
      trendDialogVisible: false,
      temp: {
        analysisId: undefined,
        analysisNo: '',
        analysisType: '',
        analysisPeriod: '',
        analysisContent: ''
      },
      rules: {
        analysisNo: [{ required: true, message: '分析编号不能为空', trigger: 'blur' }],
        analysisType: [{ required: true, message: '分析类型不能为空', trigger: 'change' }],
        analysisPeriod: [{ required: true, message: '分析周期不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanAnalysisPage(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    getSummaryInfo() {
      getFundPlanAnalysisSummary(this.listQuery).then(response => {
        if (response.code === 1) {
          const d = response.data || {}
          // 达梦数据库 Map key 可能全大写，做兼容处理
          this.summaryInfo = {
            totalCount: d.totalCount || d.TOTALCOUNT || 0,
            validCount: d.validCount || d.VALIDCOUNT || 0,
            avgQualityScore: d.avgQualityScore || d.AVGQUALITYSCORE || 0,
            highQualityCount: d.highQualityCount || d.HIGHQUALITYCOUNT || 0
          }
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
    },
    handleDateRangeChange(val) {
      if (val) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'qualityScore') {
        this.sortByQualityScore(order)
      } else if (prop === 'createdTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByQualityScore(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+qualityScore'
      } else {
        this.listQuery.sort = '-qualityScore'
      }
      this.handleFilter()
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+createdTime'
      } else {
        this.listQuery.sort = '-createdTime'
      }
      this.handleFilter()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.temp = {
        analysisId: undefined,
        analysisNo: '',
        analysisType: '',
        analysisPeriod: '',
        analysisContent: ''
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate() })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate() })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createFundPlanAnalysis(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({ title: '成功', message: '新增成功', type: 'success', duration: 2000 })
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '新增失败')
            }
          }).catch(() => { this.$message.error('新增失败') })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateFundPlanAnalysis(this.temp).then(response => {
            if (response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({ title: '成功', message: '更新成功', type: 'success', duration: 2000 })
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(() => { this.$message.error('更新失败') })
        }
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) return
      this.$confirm(`确认批量删除选中的 ${this.multipleSelection.length} 条分析记录?`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        Promise.all(this.multipleSelection.map(row => deleteFundPlanAnalysis(row.analysisId))).then(() => {
          this.$notify({ title: '成功', message: '批量删除成功', type: 'success', duration: 2000 })
          this.multipleSelection = []
          this.getList()
          this.getSummaryInfo()
        }).catch(() => { this.$message.error('批量删除失败') })
      })
    },
    handleValidate(row) {
      this.validateForm = {
        analysisId: row.analysisId,
        analysisNo: row.analysisNo,
        analysisTypeText: this.analysisTypeMap[row.analysisType],
        qualityScore: row.qualityScore || 80,
        valueAssessment: row.valueAssessment || 'MEDIUM',
        validUntil: row.validUntil || new Date(Date.now() + 90 * 24 * 60 * 60 * 1000), // 默认90天后
        validateNotes: ''
      }
      this.validateDialogVisible = true
    },
    confirmValidate() {
      this.$refs.validateForm.validate(valid => {
        if (valid) {
          const validateData = {
            analysisId: this.validateForm.analysisId,
            qualityScore: this.validateForm.qualityScore,
            valueAssessment: this.validateForm.valueAssessment,
            validUntil: this.validateForm.validUntil,
            isValid: 1,
            validateNotes: this.validateForm.validateNotes
          }

          updateFundPlanAnalysis(validateData).then(response => {
            if (response.code === 1) {
              this.$message.success('验证成功')
              this.validateDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '验证失败')
            }
          })
        }
      })
    },
    resetValidateForm() {
      this.validateForm = {
        analysisId: null,
        analysisNo: '',
        analysisTypeText: '',
        qualityScore: null,
        valueAssessment: '',
        validUntil: null,
        validateNotes: ''
      }
      if (this.$refs.validateForm) {
        this.$refs.validateForm.resetFields()
      }
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该分析记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanAnalysis(row.analysisId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.list.splice(index, 1)
            this.total--
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyAnalysis(row)
          break
        case 'export':
          this.exportAnalysis(row)
          break
        case 'template':
          this.generateTemplate(row)
          break
      }
    },
    showDetail(row) {
      this.handleUpdate(row)
    },
    copyAnalysis(row) {
      this.temp = Object.assign({}, row)
      this.temp.analysisId = undefined
      this.temp.analysisNo = `${row.analysisNo}_COPY`
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate() })
    },
    exportAnalysis(row) {
      import('@/utils/excel').then(excel => {
        const header = ['分析编号', '分析类型', '分析周期', '质量分数', '是否有效', '有效期至', '价值评估', '分析内容', '验证说明', '创建人', '创建时间']
        const data = [[
          row.analysisNo || '',
          this.analysisTypeMap[row.analysisType] || row.analysisType || '',
          row.analysisPeriod || '',
          row.qualityScore != null ? row.qualityScore : '',
          row.isValid === 1 ? '有效' : '无效',
          row.validUntil ? new Date(row.validUntil).toLocaleDateString('zh-CN') : '',
          this.valueMap[row.valueAssessment] || '',
          row.analysisContent || '',
          row.validateNotes || '',
          row.createdByName || '',
          row.createdTime ? new Date(row.createdTime).toLocaleString('zh-CN') : ''
        ]]
        excel.export_json_to_excel({
          header,
          data,
          filename: `分析报告_${row.analysisNo || row.analysisId}`
        })
      })
    },
    generateTemplate(row) {
      import('@/utils/excel').then(excel => {
        const header = ['分析编号', '分析类型', '分析周期', '质量分数(0-100)', '价值评估(HIGH/MEDIUM/LOW)', '有效期至(yyyy-MM-dd)', '分析内容', '验证说明']
        excel.export_json_to_excel({
          header,
          data: [],
          filename: '资金计划分析导入模板'
        })
      })
    },
    showQualityAnalysis() {
      this.qualityDialogVisible = true
    },
    renderQualityChart() {
      // 按分析类型分组，统计平均质量分
      const typeMap = {}
      this.list.forEach(item => {
        if (!item.analysisType) return
        if (!typeMap[item.analysisType]) typeMap[item.analysisType] = { total: 0, count: 0 }
        if (item.qualityScore != null) {
          typeMap[item.analysisType].total += item.qualityScore
          typeMap[item.analysisType].count++
        }
      })
      const categories = Object.keys(typeMap).map(k => this.analysisTypeMap[k] || k)
      const values = Object.keys(typeMap).map(k => {
        const g = typeMap[k]
        return g.count > 0 ? parseFloat((g.total / g.count).toFixed(1)) : 0
      })

      const existing = echarts.getInstanceByDom(this.$refs.qualityChart)
      if (existing) existing.dispose()
      const chart = echarts.init(this.$refs.qualityChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '8%', top: '10%', containLabel: true },
        xAxis: { type: 'category', data: categories, axisLabel: { interval: 0 } },
        yAxis: { type: 'value', name: '平均质量分', min: 0, max: 100 },
        series: [{
          name: '平均质量分',
          type: 'bar',
          data: values,
          itemStyle: {
            color: (params) => {
              const v = params.value
              if (v >= 90) return '#67C23A'
              if (v >= 80) return '#E6A23C'
              if (v >= 70) return '#409EFF'
              return '#F56C6C'
            }
          },
          label: { show: true, position: 'top', formatter: '{c}分' }
        }]
      })
    },
    showTrendAnalysis() {
      this.trendDialogVisible = true
    },
    renderTrendChart() {
      // 按创建月份统计分析数量，兼容字符串/Date对象/时间戳
      const monthMap = {}
      this.list.forEach(item => {
        if (!item.createdTime) return
        let dateStr
        if (typeof item.createdTime === 'string') {
          dateStr = item.createdTime.replace(/-/g, '/') // Safari 兼容
        } else {
          dateStr = new Date(item.createdTime).toISOString()
        }
        const d = new Date(dateStr)
        if (isNaN(d.getTime())) return
        const month = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
        monthMap[month] = (monthMap[month] || 0) + 1
      })
      const months = Object.keys(monthMap).sort()
      const counts = months.map(m => monthMap[m])

      // 销毁旧实例，避免重复 init 报错
      const existing = echarts.getInstanceByDom(this.$refs.trendChart)
      if (existing) existing.dispose()
      const chart = echarts.init(this.$refs.trendChart)

      if (months.length === 0) {
        chart.setOption({
          title: { text: '暂无数据', left: 'center', top: 'middle', textStyle: { color: '#999', fontSize: 14 } }
        })
        return
      }

      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '8%', top: '10%', containLabel: true },
        xAxis: { type: 'category', data: months, axisLabel: { rotate: 30 } },
        yAxis: { type: 'value', name: '分析数量', minInterval: 1 },
        series: [{
          name: '分析数量',
          type: 'line',
          data: counts,
          smooth: true,
          symbol: 'circle',
          itemStyle: { color: '#409EFF' },
          areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [{ offset: 0, color: 'rgba(64,158,255,0.4)' }, { offset: 1, color: 'rgba(64,158,255,0.05)' }]
          }},
          label: { show: true, position: 'top' }
        }]
      })
    },
    getQualityScoreClass(score) {
      if (score >= 90) return 'text-success'
      if (score >= 80) return 'text-warning'
      if (score >= 70) return 'text-info'
      return 'text-danger'
    },
    getContentSummary(content) {
      if (!content) return '-'
      return content.length > 50 ? content.substring(0, 50) + '...' : content
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    margin: 0 0 8px 0;
    color: #303133;
    font-size: 24px;
    font-weight: 500;
  }

  p {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }
}

.filter-container {
  padding: 10px 0;
  margin-bottom: 20px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  position: relative;
  overflow: hidden;

  .statistics-content {
    padding: 20px;

    .statistics-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }

    .statistics-label {
      font-size: 14px;
      color: #909399;
    }
  }

  .statistics-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 40px;
    color: #E4E7ED;
  }
}

.text-success {
  color: #67C23A !important;
  font-weight: 500;
}

.text-warning {
  color: #E6A23C !important;
  font-weight: 500;
}

.text-info {
  color: #409EFF !important;
  font-weight: 500;
}

.text-danger {
  color: #F56C6C !important;
  font-weight: 500;
}

.text-muted {
  color: #C0C4CC !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
    font-weight: 500;
  }

  .text-warning {
    color: #E6A23C;
    font-weight: 500;
  }

  .text-info {
    color: #409EFF;
    font-weight: 500;
  }

  .text-danger {
    color: #F56C6C;
    font-weight: 500;
  }

  .text-muted {
    color: #C0C4CC;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
