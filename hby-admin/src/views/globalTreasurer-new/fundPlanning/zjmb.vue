<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划模板管理</h2>
      <p>管理资金计划模板，提供标准化的计划制定和分析模板</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.templateType"
        placeholder="模板类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in templateTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.templateStatus"
        placeholder="模板状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option
          v-for="item in templateStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input
        v-model="listQuery.templateName"
        placeholder="模板名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
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
        新建模板
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showUsageStatistics"
      >
        使用统计
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-star-on"
        @click="showQualityAnalysis"
      >
        质量分析
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
            <div class="statistics-label">模板总数</div>
          </div>
          <i class="el-icon-document statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.activeCount || 0 }}</div>
            <div class="statistics-label">活跃模板</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalUsageCount || 0 }}</div>
            <div class="statistics-label">总使用次数</div>
          </div>
          <i class="el-icon-data-line statistics-icon"></i>
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
        label="模板编号"
        prop="templateNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.templateNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="模板名称"
        prop="templateName"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.templateName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="模板类型"
        prop="templateType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="templateTypeTagMap[row.templateType]">
            {{ templateTypeMap[row.templateType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="版本"
        prop="templateVersion"
        width="80"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.templateVersion || 'v1.0' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="状态"
        prop="templateStatus"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="templateStatusTagMap[row.templateStatus]">
            {{ templateStatusMap[row.templateStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="使用次数"
        prop="usageCount"
        width="100"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span :class="getUsageCountClass(row.usageCount)">
            {{ row.usageCount || 0 }}
          </span>
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
        label="最后使用"
        prop="lastUsedTime"
        width="160"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.lastUsedTime">{{ row.lastUsedTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          <span v-else class="text-muted">从未使用</span>
        </template>
      </el-table-column>
      <el-table-column
        label="模板描述"
        prop="templateDescription"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.templateDescription || '-' }}</span>
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
          <span>{{ (row.createdTime || row.createTime) | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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
            v-if="row.templateStatus === 'INACTIVE'"
            size="mini"
            type="success"
            @click="handleActivate(row)"
          >
            激活
          </el-button>
          <el-button
            v-if="row.templateStatus === 'ACTIVE'"
            size="mini"
            type="warning"
            @click="handleDeactivate(row)"
          >
            停用
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
              <el-dropdown-item command="copy">复制模板</el-dropdown-item>
              <el-dropdown-item command="use">使用模板</el-dropdown-item>
              <el-dropdown-item command="export">导出模板</el-dropdown-item>
              <el-dropdown-item command="delete">删除</el-dropdown-item>
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

    <!-- 新增/编辑模板对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增模板' : '编辑模板'" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="100px" style="width:500px;margin-left:30px;">
        <el-form-item label="模板编号" prop="templateNo">
          <el-input v-model="temp.templateNo" placeholder="请输入模板编号" />
        </el-form-item>
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="temp.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型" prop="templateType">
          <el-select v-model="temp.templateType" placeholder="请选择模板类型" style="width:100%">
            <el-option v-for="item in templateTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板版本" prop="templateVersion">
          <el-input v-model="temp.templateVersion" placeholder="如：v1.0" />
        </el-form-item>
        <el-form-item label="模板描述">
          <el-input v-model="temp.templateDescription" type="textarea" :rows="3" placeholder="请输入模板描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 使用模板对话框 -->
    <el-dialog
      title="使用模板"
      :visible.sync="useTemplateDialogVisible"
      width="500px"
      @close="resetUseTemplateForm"
    >
      <el-form
        ref="useTemplateForm"
        :model="useTemplateForm"
        :rules="useTemplateRules"
        label-width="100px"
      >
        <el-form-item label="模板名称">
          <el-input v-model="useTemplateForm.templateName" disabled />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-input v-model="useTemplateForm.templateTypeText" disabled />
        </el-form-item>
        <el-form-item label="目标类型" prop="targetType">
          <el-select v-model="useTemplateForm.targetType" placeholder="请选择目标类型" style="width: 100%">
            <el-option label="创建资金计划" value="PLAN" />
            <el-option label="创建分析报告" value="ANALYSIS" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划名称" prop="targetName">
          <el-input
            v-model="useTemplateForm.targetName"
            placeholder="请输入计划或分析名称"
          />
        </el-form-item>
        <el-form-item label="使用说明">
          <el-input
            v-model="useTemplateForm.useNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入使用说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="useTemplateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUseTemplate">确定</el-button>
      </div>
    </el-dialog>

    <!-- 使用统计弹窗 -->
    <el-dialog title="使用统计" :visible.sync="usageStatisticsVisible" width="700px">
      <el-row :gutter="20" style="margin-bottom:20px;">
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#409EFF;">{{ summaryInfo.totalUsageCount || 0 }}</div>
              <div style="color:#909399;margin-top:6px;">总使用次数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#67C23A;">{{ summaryInfo.totalCount || 0 }}</div>
              <div style="color:#909399;margin-top:6px;">模板总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#E6A23C;">{{ summaryInfo.activeCount || 0 }}</div>
              <div style="color:#909399;margin-top:6px;">活跃模板</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-table :data="usageStatisticsList" border size="small" max-height="320">
        <el-table-column label="模板名称" prop="templateName" show-overflow-tooltip />
        <el-table-column label="模板类型" prop="templateType" width="110" align="center">
          <template slot-scope="{row}">
            <el-tag size="small" :type="templateTypeTagMap[row.templateType]">{{ templateTypeMap[row.templateType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="使用次数" prop="usageCount" width="100" align="center" sortable>
          <template slot-scope="{row}">
            <span :class="getUsageCountClass(row.usageCount)">{{ row.usageCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="templateStatus" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag size="small" :type="templateStatusTagMap[row.templateStatus]">{{ templateStatusMap[row.templateStatus] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最后使用时间" prop="lastUsedTime" width="150" align="center">
          <template slot-scope="{row}">
            <span v-if="row.lastUsedTime">{{ row.lastUsedTime | parseTime('{y}-{m}-{d}') }}</span>
            <span v-else style="color:#C0C4CC;">从未使用</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="usageStatisticsVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 质量分析弹窗 -->
    <el-dialog title="质量分析" :visible.sync="qualityAnalysisVisible" width="700px">
      <el-row :gutter="20" style="margin-bottom:20px;">
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#409EFF;">{{ summaryInfo.avgQualityScore || 0 }}</div>
              <div style="color:#909399;margin-top:6px;">平均质量分</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#67C23A;">{{ qualityHighCount }}</div>
              <div style="color:#909399;margin-top:6px;">优质模板(≥90分)</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div style="text-align:center;">
              <div style="font-size:28px;font-weight:bold;color:#F56C6C;">{{ qualityLowCount }}</div>
              <div style="color:#909399;margin-top:6px;">待优化模板(&lt;70分)</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-table :data="qualityAnalysisList" border size="small" max-height="320">
        <el-table-column label="模板名称" prop="templateName" show-overflow-tooltip />
        <el-table-column label="模板类型" prop="templateType" width="110" align="center">
          <template slot-scope="{row}">
            <el-tag size="small" :type="templateTypeTagMap[row.templateType]">{{ templateTypeMap[row.templateType] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="质量分数" prop="qualityScore" width="100" align="center" sortable>
          <template slot-scope="{row}">
            <span v-if="row.qualityScore != null" :class="getQualityScoreClass(row.qualityScore)">{{ row.qualityScore }}分</span>
            <span v-else style="color:#C0C4CC;">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="质量等级" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.qualityScore >= 90" size="small" type="success">优秀</el-tag>
            <el-tag v-else-if="row.qualityScore >= 80" size="small" type="warning">良好</el-tag>
            <el-tag v-else-if="row.qualityScore >= 70" size="small" type="info">一般</el-tag>
            <el-tag v-else-if="row.qualityScore != null" size="small" type="danger">待优化</el-tag>
            <span v-else style="color:#C0C4CC;">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="使用次数" prop="usageCount" width="90" align="center">
          <template slot-scope="{row}">{{ row.usageCount || 0 }}</template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="qualityAnalysisVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanTemplatePage, createFundPlanTemplate, updateFundPlanTemplate, deleteFundPlanTemplate,
         getFundPlanTemplateSummary, activateFundPlanTemplate, deactivateFundPlanTemplate,
         useFundPlanTemplate } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundPlanTemplate',
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
        templateType: undefined,
        templateStatus: undefined,
        templateName: undefined,
        sort: '-createTime'
      },
      summaryInfo: {},
      usageStatisticsVisible: false,
      qualityAnalysisVisible: false,
      useTemplateDialogVisible: false,
      useTemplateForm: {
        templateId: null,
        templateName: '',
        templateTypeText: '',
        targetType: '',
        targetName: '',
        useNotes: ''
      },
      useTemplateRules: {
        targetType: [
          { required: true, message: '请选择目标类型', trigger: 'change' }
        ],
        targetName: [
          { required: true, message: '请输入计划或分析名称', trigger: 'blur' }
        ]
      },
      templateTypeOptions: [
        { label: '计划模板', value: 'PLAN' },
        { label: '分析模板', value: 'ANALYSIS' },
        { label: '预测模板', value: 'FORECAST' },
        { label: '报告模板', value: 'REPORT' },
        { label: '审批模板', value: 'APPROVAL' }
      ],
      templateStatusOptions: [
        { label: '活跃', value: 'ACTIVE' },
        { label: '停用', value: 'INACTIVE' }
      ],
      templateTypeMap: {
        'PLAN': '计划模板',
        'ANALYSIS': '分析模板',
        'FORECAST': '预测模板',
        'REPORT': '报告模板',
        'APPROVAL': '审批模板'
      },
      templateTypeTagMap: {
        'PLAN': 'primary',
        'ANALYSIS': 'success',
        'FORECAST': 'info',
        'REPORT': 'warning',
        'APPROVAL': 'danger'
      },
      templateStatusMap: {
        'ACTIVE': '活跃',
        'INACTIVE': '停用'
      },
      templateStatusTagMap: {
        'ACTIVE': 'success',
        'INACTIVE': 'info'
      },
      multipleSelection: [],
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        templateId: undefined,
        templateNo: '',
        templateName: '',
        templateType: '',
        templateVersion: 'v1.0',
        templateDescription: ''
      },
      rules: {
        templateNo: [{ required: true, message: '模板编号不能为空', trigger: 'blur' }],
        templateName: [{ required: true, message: '模板名称不能为空', trigger: 'blur' }],
        templateType: [{ required: true, message: '模板类型不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
  },
  computed: {
    usageStatisticsList() {
      return [...this.list].sort((a, b) => (b.usageCount || 0) - (a.usageCount || 0))
    },
    qualityAnalysisList() {
      return [...this.list].sort((a, b) => (b.qualityScore || 0) - (a.qualityScore || 0))
    },
    qualityHighCount() {
      return this.list.filter(t => (t.qualityScore || 0) >= 90).length
    },
    qualityLowCount() {
      return this.list.filter(t => t.qualityScore != null && t.qualityScore < 70).length
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanTemplatePage(this.listQuery).then(response => {
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
      getFundPlanTemplateSummary(null).then(response => {
        if (response.code === 1) {
          const d = response.data || {}
          this.summaryInfo = {
            totalCount: d.TOTALCOUNT || d.totalCount || 0,
            totalUsageCount: d.TOTALUSAGECOUNT || d.totalUsageCount || 0,
            avgQualityScore: d.AVGQUALITYSCORE || d.avgQualityScore || 0,
            activeCount: d.ACTIVECOUNT || d.activeCount || 0
          }
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'usageCount') {
        this.sortByUsageCount(order)
      } else if (prop === 'qualityScore') {
        this.sortByQualityScore(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByUsageCount(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+usageCount'
      } else {
        this.listQuery.sort = '-usageCount'
      }
      this.handleFilter()
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
        this.listQuery.sort = '+createTime'
      } else {
        this.listQuery.sort = '-createTime'
      }
      this.handleFilter()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.temp = {
        templateId: undefined,
        templateNo: '',
        templateName: '',
        templateType: '',
        templateVersion: 'v1.0',
        templateDescription: ''
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
          createFundPlanTemplate(this.temp).then(response => {
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
          updateFundPlanTemplate(this.temp).then(response => {
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
      this.$confirm(`确认批量删除选中的 ${this.multipleSelection.length} 个模板?`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        Promise.all(this.multipleSelection.map(row => deleteFundPlanTemplate(row.templateId))).then(() => {
          this.$notify({ title: '成功', message: '批量删除成功', type: 'success', duration: 2000 })
          this.multipleSelection = []
          this.getList()
          this.getSummaryInfo()
        }).catch(() => { this.$message.error('批量删除失败') })
      })
    },
    handleActivate(row) {
      this.$confirm('确认激活该模板?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        activateFundPlanTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('激活成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '激活失败')
          }
        })
      })
    },
    handleDeactivate(row) {
      this.$confirm('确认停用该模板?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deactivateFundPlanTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('停用成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '停用失败')
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
          this.copyTemplate(row)
          break
        case 'use':
          this.useTemplate(row)
          break
        case 'export':
          this.exportTemplate(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    showDetail(row) {
      this.handleUpdate(row)
    },
    copyTemplate(row) {
      this.temp = Object.assign({}, row)
      this.temp.templateId = undefined
      this.temp.templateNo = `${row.templateNo}_COPY`
      this.temp.templateName = `${row.templateName}_副本`
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs['dataForm'] && this.$refs['dataForm'].clearValidate() })
    },
    useTemplate(row) {
      this.useTemplateForm = {
        templateId: row.templateId,
        templateName: row.templateName,
        templateTypeText: this.templateTypeMap[row.templateType],
        targetType: '',
        targetName: '',
        useNotes: ''
      }
      this.useTemplateDialogVisible = true
    },
    confirmUseTemplate() {
      this.$refs.useTemplateForm.validate(valid => {
        if (valid) {
          const { templateId, targetType, targetName, useNotes } = this.useTemplateForm
          useFundPlanTemplate(templateId, { targetType, targetName, useNotes }).then(response => {
            if (response.code === 1) {
              this.$message.success('模板使用成功')
              this.useTemplateDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.msg || '使用失败')
            }
          })
        }
      })
    },
    resetUseTemplateForm() {
      this.useTemplateForm = {
        templateId: null,
        templateName: '',
        templateTypeText: '',
        targetType: '',
        targetName: '',
        useNotes: ''
      }
      if (this.$refs.useTemplateForm) {
        this.$refs.useTemplateForm.resetFields()
      }
    },
    exportTemplate(row) {
      const token = this.$store.getters.token || localStorage.getItem('token') || ''
      const url = `/vab-mock-server/qqsk/fund/template/${row.templateId}/export`
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `template_${row.templateNo}.json`)
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },
    handleDelete(row) {
      this.$confirm('确认删除该模板?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },
    showUsageStatistics() {
      this.usageStatisticsVisible = true
    },
    showQualityAnalysis() {
      this.qualityAnalysisVisible = true
    },
    getUsageCountClass(count) {
      if (count >= 50) return 'text-success'
      if (count >= 20) return 'text-warning'
      if (count >= 5) return 'text-info'
      return 'text-muted'
    },
    getQualityScoreClass(score) {
      if (score >= 90) return 'text-success'
      if (score >= 80) return 'text-warning'
      if (score >= 70) return 'text-info'
      return 'text-danger'
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
