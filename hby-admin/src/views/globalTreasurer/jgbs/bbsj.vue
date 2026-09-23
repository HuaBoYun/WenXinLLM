<template>
  <div class="report-data-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>报表数据管理</h2>
      <p>管理监管报送的各类报表数据，确保数据准确性和完整性</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="报表编号">
          <el-input v-model="searchForm.reportCode" placeholder="请输入报表编号" clearable />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="searchForm.reportType" placeholder="请选择报表类型" clearable>
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="损益表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
            <el-option label="风险报告" value="RISK_REPORT" />
            <el-option label="流动性报告" value="LIQUIDITY_REPORT" />
            <el-option label="资本充足率报告" value="CAPITAL_ADEQUACY" />
          </el-select>
        </el-form-item>
        <el-form-item label="监管机构">
          <el-select v-model="searchForm.regulatoryAuthority" placeholder="请选择监管机构" clearable>
            <el-option label="中国人民银行" value="PBOC" />
            <el-option label="银保监会" value="CBIRC" />
            <el-option label="证监会" value="CSRC" />
            <el-option label="外汇管理局" value="SAFE" />
            <el-option label="财政部" value="MOF" />
          </el-select>
        </el-form-item>
        <el-form-item label="报送状态">
          <el-select v-model="searchForm.status" placeholder="请选择报送状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审核" value="PENDING_REVIEW" />
            <el-option label="已审核" value="REVIEWED" />
            <el-option label="已报送" value="SUBMITTED" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="被退回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间">
          <el-date-picker
            v-model="searchForm.reportPeriodRange"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增报表</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-check" size="mini" :disabled="multiple" @click="handleBatchSubmit">批量报送</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="reportList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="报表编号" prop="reportCode" width="150" show-overflow-tooltip />
        <el-table-column label="报表名称" prop="reportName" width="200" show-overflow-tooltip />
        <el-table-column label="报表类型" prop="reportType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReportTypeTag(scope.row.reportType)" size="mini">
              {{ getReportTypeText(scope.row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="监管机构" prop="regulatoryAuthority" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAuthorityTag(scope.row.regulatoryAuthority)" size="mini">
              {{ getAuthorityText(scope.row.regulatoryAuthority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报告期间" prop="reportPeriod" width="100" align="center" />
        <el-table-column label="报送频率" prop="frequency" width="100" align="center">
          <template slot-scope="scope">
            <span class="frequency-text">{{ getFrequencyText(scope.row.frequency) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="截止日期" prop="deadline" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getDeadlineClass(scope.row.deadline)">
              {{ parseTime(scope.row.deadline, '{y}-{m}-{d}') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="数据完整性" prop="dataCompleteness" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.dataCompleteness"
              :color="getCompletenessColor(scope.row.dataCompleteness)"
              :stroke-width="8"
              text-inside
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="validate" icon="el-icon-check">数据校验</el-dropdown-item>
                <el-dropdown-item command="preview" icon="el-icon-view">预览报表</el-dropdown-item>
                <el-dropdown-item command="submit" v-if="scope.row.status === 'REVIEWED'" icon="el-icon-upload">报送</el-dropdown-item>
                <el-dropdown-item command="recall" v-if="scope.row.status === 'SUBMITTED'" icon="el-icon-refresh-left">撤回</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">操作历史</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报表编号" prop="reportCode">
                  <el-input v-model="form.reportCode" placeholder="请输入报表编号" :disabled="form.reportId != null" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报表名称" prop="reportName">
                  <el-input v-model="form.reportName" placeholder="请输入报表名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报表类型" prop="reportType">
                  <el-select v-model="form.reportType" placeholder="请选择报表类型" style="width: 100%">
                    <el-option label="资产负债表" value="BALANCE_SHEET" />
                    <el-option label="损益表" value="INCOME_STATEMENT" />
                    <el-option label="现金流量表" value="CASH_FLOW" />
                    <el-option label="风险报告" value="RISK_REPORT" />
                    <el-option label="流动性报告" value="LIQUIDITY_REPORT" />
                    <el-option label="资本充足率报告" value="CAPITAL_ADEQUACY" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="监管机构" prop="regulatoryAuthority">
                  <el-select v-model="form.regulatoryAuthority" placeholder="请选择监管机构" style="width: 100%">
                    <el-option label="中国人民银行" value="PBOC" />
                    <el-option label="银保监会" value="CBIRC" />
                    <el-option label="证监会" value="CSRC" />
                    <el-option label="外汇管理局" value="SAFE" />
                    <el-option label="财政部" value="MOF" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报告期间" prop="reportPeriod">
                  <el-date-picker
                    v-model="form.reportPeriod"
                    type="month"
                    placeholder="选择报告期间"
                    format="yyyy-MM"
                    value-format="yyyy-MM"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报送频率" prop="frequency">
                  <el-select v-model="form.frequency" placeholder="请选择报送频率" style="width: 100%">
                    <el-option label="日报" value="DAILY" />
                    <el-option label="周报" value="WEEKLY" />
                    <el-option label="月报" value="MONTHLY" />
                    <el-option label="季报" value="QUARTERLY" />
                    <el-option label="半年报" value="SEMI_ANNUAL" />
                    <el-option label="年报" value="ANNUAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="截止日期" prop="deadline">
                  <el-date-picker
                    v-model="form.deadline"
                    type="date"
                    placeholder="选择截止日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%">
                    <el-option label="低" value="LOW" />
                    <el-option label="中" value="MEDIUM" />
                    <el-option label="高" value="HIGH" />
                    <el-option label="紧急" value="URGENT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="报表描述">
              <el-input v-model="form.description" type="textarea" placeholder="请输入报表描述" :rows="3" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="数据配置" name="data">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="数据源" prop="dataSource">
                  <el-select v-model="form.dataSource" placeholder="请选择数据源" style="width: 100%">
                    <el-option label="核心系统" value="CORE_SYSTEM" />
                    <el-option label="财务系统" value="FINANCIAL_SYSTEM" />
                    <el-option label="风险系统" value="RISK_SYSTEM" />
                    <el-option label="外部数据" value="EXTERNAL_DATA" />
                    <el-option label="手工录入" value="MANUAL_INPUT" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数据格式" prop="dataFormat">
                  <el-select v-model="form.dataFormat" placeholder="请选择数据格式" style="width: 100%">
                    <el-option label="Excel" value="EXCEL" />
                    <el-option label="XML" value="XML" />
                    <el-option label="JSON" value="JSON" />
                    <el-option label="CSV" value="CSV" />
                    <el-option label="PDF" value="PDF" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="模板文件" prop="templateFile">
                  <el-input v-model="form.templateFile" placeholder="请输入模板文件路径" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="校验规则" prop="validationRules">
                  <el-input v-model="form.validationRules" placeholder="请输入校验规则" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="数据映射配置">
              <el-input v-model="form.dataMappingConfig" type="textarea" placeholder="请输入数据映射配置" :rows="4" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="报送配置" name="submission">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报送方式" prop="submissionMethod">
                  <el-select v-model="form.submissionMethod" placeholder="请选择报送方式" style="width: 100%">
                    <el-option label="在线报送" value="ONLINE" />
                    <el-option label="文件上传" value="FILE_UPLOAD" />
                    <el-option label="邮件发送" value="EMAIL" />
                    <el-option label="FTP传输" value="FTP" />
                    <el-option label="API接口" value="API" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报送地址" prop="submissionUrl">
                  <el-input v-model="form.submissionUrl" placeholder="请输入报送地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contactPerson">
                  <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="报表详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="报表编号">{{ detailData.reportCode }}</el-descriptions-item>
        <el-descriptions-item label="报表名称">{{ detailData.reportName }}</el-descriptions-item>
        <el-descriptions-item label="报表类型">
          <el-tag :type="getReportTypeTag(detailData.reportType)" size="mini">
            {{ getReportTypeText(detailData.reportType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="监管机构">
          <el-tag :type="getAuthorityTag(detailData.regulatoryAuthority)" size="mini">
            {{ getAuthorityText(detailData.regulatoryAuthority) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ detailData.reportPeriod }}</el-descriptions-item>
        <el-descriptions-item label="报送频率">{{ getFrequencyText(detailData.frequency) }}</el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ parseTime(detailData.deadline, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ getPriorityText(detailData.priority) }}</el-descriptions-item>
        <el-descriptions-item label="数据完整性">{{ detailData.dataCompleteness }}%</el-descriptions-item>
        <el-descriptions-item label="数据源">{{ getDataSourceText(detailData.dataSource) }}</el-descriptions-item>
        <el-descriptions-item label="数据格式">{{ detailData.dataFormat }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="3">
          {{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="报表描述" :span="3">{{ detailData.description }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入报表数据" :visible.sync="importOpen" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils'

export default {
  name: 'ReportData',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 报表列表
      reportList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 是否显示导入弹出层
      importOpen: false,
      // 详情数据
      detailData: {},
      // 活动标签页
      activeTab: 'basic',
      // 上传参数
      upload: {
        open: false,
        title: '',
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: 'Bearer ' + this.$store.getters.token },
        url: process.env.VUE_APP_BASE_API + '/regulatory/report/importData'
      },
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        reportCode: null,
        reportType: null,
        regulatoryAuthority: null,
        status: null,
        reportPeriodStart: null,
        reportPeriodEnd: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        reportCode: '',
        reportType: '',
        regulatoryAuthority: '',
        status: '',
        reportPeriodRange: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reportCode: [
          { required: true, message: '报表编号不能为空', trigger: 'blur' }
        ],
        reportName: [
          { required: true, message: '报表名称不能为空', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '报表类型不能为空', trigger: 'change' }
        ],
        regulatoryAuthority: [
          { required: true, message: '监管机构不能为空', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '报告期间不能为空', trigger: 'change' }
        ],
        frequency: [
          { required: true, message: '报送频率不能为空', trigger: 'change' }
        ],
        deadline: [
          { required: true, message: '截止日期不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    /** 查询报表列表 */
    getList() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.reportList = [
          {
            reportId: 1,
            reportCode: 'RPT001',
            reportName: '月度资产负债表',
            reportType: 'BALANCE_SHEET',
            regulatoryAuthority: 'PBOC',
            reportPeriod: '2025-01',
            frequency: 'MONTHLY',
            deadline: '2025-02-15',
            dataCompleteness: 95,
            status: 'REVIEWED',
            createTime: '2025-01-20 10:00:00'
          }
        ]
        this.total = 1
        this.loading = false
      }, 1000)
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      if (this.searchForm.reportPeriodRange && this.searchForm.reportPeriodRange.length === 2) {
        this.queryParams.reportPeriodStart = this.searchForm.reportPeriodRange[0]
        this.queryParams.reportPeriodEnd = this.searchForm.reportPeriodRange[1]
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        reportCode: '',
        reportType: '',
        regulatoryAuthority: '',
        status: '',
        reportPeriodRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        reportCode: null,
        reportType: null,
        regulatoryAuthority: null,
        status: null,
        reportPeriodStart: null,
        reportPeriodEnd: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增报表'
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.form = { ...row }
      this.open = true
      this.title = '修改报表'
      this.activeTab = 'basic'
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 导入按钮操作 */
    handleImport() {
      this.importOpen = true
    },
    /** 下载模板 */
    importTemplate() {
      this.download('regulatory/report/importTemplate', {}, `report_template_${new Date().getTime()}.xlsx`)
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
      this.importOpen = false
      this.$refs.upload.clearFiles()
      this.$alert(response.msg, '导入结果', { dangerouslyUseHTMLString: true })
      this.getList()
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit()
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.$modal.msgSuccess(this.form.reportId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const reportIds = row.reportId || this.ids
      this.$modal.confirm('是否确认删除报表编号为"' + reportIds + '"的数据项？').then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('regulatory/report/export', {
        ...this.queryParams
      }, `report_data_${new Date().getTime()}.xlsx`)
    },
    /** 批量报送 */
    handleBatchSubmit() {
      this.$modal.confirm('是否确认批量报送所有选中的报表？').then(() => {
        this.$modal.msgSuccess('报送成功')
        this.getList()
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.reportId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'validate':
          this.handleValidate(row)
          break
        case 'preview':
          this.handlePreview(row)
          break
        case 'submit':
          this.handleSubmit(row)
          break
        case 'recall':
          this.handleRecall(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
      }
    },
    /** 数据校验 */
    handleValidate(row) {
      this.$modal.msgSuccess('数据校验通过')
    },
    /** 预览报表 */
    handlePreview(row) {
      this.$modal.msgSuccess('预览报表')
    },
    /** 报送 */
    handleSubmit(row) {
      this.$modal.confirm('是否确认报送报表"' + row.reportName + '"？').then(() => {
        this.$modal.msgSuccess('报送成功')
        this.getList()
      }).catch(() => {})
    },
    /** 撤回 */
    handleRecall(row) {
      this.$modal.confirm('是否确认撤回报表"' + row.reportName + '"？').then(() => {
        this.$modal.msgSuccess('撤回成功')
        this.getList()
      }).catch(() => {})
    },
    /** 操作历史 */
    handleHistory(row) {
      this.$modal.msgSuccess('查看操作历史')
    },
    /** 复制 */
    handleCopy(row) {
      this.$modal.msgSuccess('复制成功')
      this.getList()
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        reportId: null,
        reportCode: null,
        reportName: null,
        reportType: null,
        regulatoryAuthority: null,
        reportPeriod: null,
        frequency: null,
        deadline: null,
        priority: null,
        description: null,
        dataSource: null,
        dataFormat: null,
        templateFile: null,
        validationRules: null,
        dataMappingConfig: null,
        submissionMethod: null,
        submissionUrl: null,
        contactPerson: null,
        contactPhone: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },
    /** 获取报表类型标签 */
    getReportTypeTag(type) {
      const tagMap = {
        'BALANCE_SHEET': 'primary',
        'INCOME_STATEMENT': 'success',
        'CASH_FLOW': 'info',
        'RISK_REPORT': 'warning',
        'LIQUIDITY_REPORT': 'danger',
        'CAPITAL_ADEQUACY': 'primary'
      }
      return tagMap[type] || ''
    },
    /** 获取报表类型文本 */
    getReportTypeText(type) {
      const textMap = {
        'BALANCE_SHEET': '资产负债表',
        'INCOME_STATEMENT': '损益表',
        'CASH_FLOW': '现金流量表',
        'RISK_REPORT': '风险报告',
        'LIQUIDITY_REPORT': '流动性报告',
        'CAPITAL_ADEQUACY': '资本充足率报告'
      }
      return textMap[type] || type
    },
    /** 获取监管机构标签 */
    getAuthorityTag(authority) {
      const tagMap = {
        'PBOC': 'primary',
        'CBIRC': 'success',
        'CSRC': 'warning',
        'SAFE': 'info',
        'MOF': 'danger'
      }
      return tagMap[authority] || ''
    },
    /** 获取监管机构文本 */
    getAuthorityText(authority) {
      const textMap = {
        'PBOC': '央行',
        'CBIRC': '银保监会',
        'CSRC': '证监会',
        'SAFE': '外管局',
        'MOF': '财政部'
      }
      return textMap[authority] || authority
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'PENDING_REVIEW': 'warning',
        'REVIEWED': 'primary',
        'SUBMITTED': 'success',
        'CONFIRMED': 'success',
        'REJECTED': 'danger'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'PENDING_REVIEW': '待审核',
        'REVIEWED': '已审核',
        'SUBMITTED': '已报送',
        'CONFIRMED': '已确认',
        'REJECTED': '被退回'
      }
      return textMap[status] || status
    },
    /** 获取频率文本 */
    getFrequencyText(frequency) {
      const textMap = {
        'DAILY': '日报',
        'WEEKLY': '周报',
        'MONTHLY': '月报',
        'QUARTERLY': '季报',
        'SEMI_ANNUAL': '半年报',
        'ANNUAL': '年报'
      }
      return textMap[frequency] || frequency
    },
    /** 获取优先级文本 */
    getPriorityText(priority) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'URGENT': '紧急'
      }
      return textMap[priority] || priority
    },
    /** 获取数据源文本 */
    getDataSourceText(source) {
      const textMap = {
        'CORE_SYSTEM': '核心系统',
        'FINANCIAL_SYSTEM': '财务系统',
        'RISK_SYSTEM': '风险系统',
        'EXTERNAL_DATA': '外部数据',
        'MANUAL_INPUT': '手工录入'
      }
      return textMap[source] || source
    },
    /** 获取截止日期样式类 */
    getDeadlineClass(deadline) {
      const now = new Date()
      const deadlineDate = new Date(deadline)
      const diffDays = Math.ceil((deadlineDate - now) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'overdue'
      if (diffDays <= 3) return 'urgent'
      if (diffDays <= 7) return 'warning'
      return 'normal'
    },
    /** 获取完整性颜色 */
    getCompletenessColor(completeness) {
      if (completeness >= 95) return '#67c23a'
      if (completeness >= 80) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.report-data-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.frequency-text {
  font-weight: bold;
  color: #409eff;
}

.overdue {
  color: #f56c6c;
  font-weight: bold;
}

.urgent {
  color: #e6a23c;
  font-weight: bold;
}

.warning {
  color: #e6a23c;
}

.normal {
  color: #303133;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
