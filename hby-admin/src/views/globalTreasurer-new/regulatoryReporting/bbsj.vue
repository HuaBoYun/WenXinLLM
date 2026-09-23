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
        <el-form-item label="报表名称">
          <el-input v-model="searchForm.reportName" placeholder="请输入报表名称" clearable />
        </el-form-item>
        <el-form-item label="监管机构">
          <el-select v-model="searchForm.authorityId" placeholder="请选择监管机构" clearable>
            <el-option
              v-for="item in authorityOptions"
              :key="item.authorityId"
              :label="item.authorityName"
              :value="item.authorityId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="报送状态">
          <el-select v-model="searchForm.reportStatus" placeholder="请选择报送状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已生成" value="GENERATED" />
            <el-option label="已验证" value="VALIDATED" />
            <el-option label="已提交" value="SUBMITTED" />
            <el-option label="已通过" value="ACCEPTED" />
            <el-option label="已退回" value="REJECTED" />
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
        ref="table"
        v-loading="loading"
        :data="reportList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="报表编号" prop="reportNo" width="150" show-overflow-tooltip />
        <el-table-column label="报表名称" prop="reportName" width="200" show-overflow-tooltip />
        <el-table-column label="监管机构" prop="authorityName" width="120" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.authorityName || getAuthorityText(scope.row.authorityId) }}
          </template>
        </el-table-column>
        <el-table-column label="报告期间" prop="reportPeriod" width="100" align="center" />
        <el-table-column label="截止日期" prop="dueDate" width="120" align="center">
          <template slot-scope="scope">
            <span :class="getDeadlineClass(scope.row.dueDate)">
              {{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="提交日期" prop="submitDate" width="120" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.submitDate ? parseTime(scope.row.submitDate, '{y}-{m}-{d}') : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="reportStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.reportStatus)">
              {{ getStatusText(scope.row.reportStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createdTime" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}') }}</span>
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
                <el-dropdown-item command="submit" v-if="scope.row.reportStatus === 'VALIDATED'" icon="el-icon-upload">报送</el-dropdown-item>
                <el-dropdown-item command="recall" v-if="scope.row.reportStatus === 'SUBMITTED'" icon="el-icon-refresh-left">撤回</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">操作历史</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
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
                <el-form-item label="报表编号" prop="reportNo">
                  <el-input v-model="form.reportNo" placeholder="请输入报表编号" :disabled="form.reportId != null" />
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
                <el-form-item label="监管机构" prop="authorityId">
                  <el-select v-model="form.authorityId" placeholder="请选择监管机构" style="width: 100%">
                    <el-option
                      v-for="item in authorityOptions"
                      :key="item.authorityId"
                      :label="item.authorityName"
                      :value="item.authorityId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
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
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="截止日期">
                  <el-date-picker
                    v-model="form.dueDate"
                    type="date"
                    placeholder="选择截止日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="报送方式">
                  <el-select v-model="form.submissionMethod" placeholder="请选择报送方式" style="width: 100%">
                    <el-option label="在线报送" value="ONLINE" />
                    <el-option label="文件上传" value="FILE_UPLOAD" />
                    <el-option label="邮件发送" value="EMAIL" />
                    <el-option label="FTP传输" value="FTP" />
                    <el-option label="API接口" value="API" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="报送状态">
                  <el-select v-model="form.reportStatus" placeholder="请选择报送状态" style="width: 100%">
                    <el-option label="草稿" value="DRAFT" />
                    <el-option label="已生成" value="GENERATED" />
                    <el-option label="已验证" value="VALIDATED" />
                    <el-option label="已提交" value="SUBMITTED" />
                    <el-option label="已通过" value="ACCEPTED" />
                    <el-option label="已退回" value="REJECTED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="确认编号">
                  <el-input v-model="form.acknowledgmentNo" placeholder="请输入确认编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
            <el-form-item label="退回原因" v-if="form.reportStatus === 'REJECTED'">
              <el-input v-model="form.rejectReason" type="textarea" placeholder="请输入退回原因" :rows="3" />
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
        <el-descriptions-item label="报表编号">{{ detailData.reportNo }}</el-descriptions-item>
        <el-descriptions-item label="报表名称">{{ detailData.reportName }}</el-descriptions-item>
        <el-descriptions-item label="监管机构">
          {{ detailData.authorityName || getAuthorityText(detailData.authorityId) }}
        </el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ detailData.reportPeriod }}</el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ parseTime(detailData.dueDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="提交日期">{{ detailData.submitDate ? parseTime(detailData.submitDate, '{y}-{m}-{d}') : '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交方式">{{ detailData.submissionMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确认编号">{{ detailData.acknowledgmentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.reportStatus)">
            {{ getStatusText(detailData.reportStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="3">
          {{ parseTime(detailData.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="退回原因" :span="3" v-if="detailData.rejectReason">{{ detailData.rejectReason }}</el-descriptions-item>
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
import Pagination from '@/components/Pagination'
import { getReportList, addReport, updateReport, delReport, validateReport, submitReport, batchSubmitReports, copyReport, recallReport, getReportHistory, getReportById, exportReportData, getActiveAuthorities } from '@/api/globalTreasurer/jgbs'
import { getToken } from '@/utils/token'

export default {
  name: 'ReportData',
  components: { Pagination },
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 选中的行数据
      selectedRows: [],
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
        isUploading: false,
        title: '',
        updateSupport: 0,
        headers: { Authorization: 'Bearer ' + getToken() },
        url: process.env.VUE_APP_BASE_API + '/regulatory/report/importData'
      },
      // 监管机构选项（从数据库加载）
      authorityOptions: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        reportName: null,
        authorityId: null,
        reportStatus: null,
        reportPeriodStart: null,
        reportPeriodEnd: null
      },
      // 搜索表单
      searchForm: {
        reportName: '',
        authorityId: '',
        reportStatus: '',
        reportPeriodRange: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reportNo: [
          { required: true, message: '报表编号不能为空', trigger: 'blur' }
        ],
        reportName: [
          { required: true, message: '报表名称不能为空', trigger: 'blur' }
        ],
        authorityId: [
          { required: true, message: '监管机构不能为空', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '报告期间不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.loadAuthorityOptions()
  },
  methods: {
    parseTime,
    /** 加载监管机构选项 */
    loadAuthorityOptions() {
      getActiveAuthorities().then(response => {
        if (response.code === 1 && response.data) {
          this.authorityOptions = response.data.rows || response.data || []
        } else {
          this.authorityOptions = response.data || []
        }
      }).catch(error => {
        console.error('获取监管机构选项失败:', error)
        this.authorityOptions = []
      })
    },
    /** 查询报表列表 */
    getList() {
      this.loading = true
      getReportList(this.queryParams).then(response => {
        if (response.code === 1) {
          // 兼容两种响应格式：
          // 格式1: {code: 1, data: {rows: [...], total: N}}
          // 格式2: {code: 1, data: [...], result: {total: N}}
          if (response.data && response.data.rows) {
            this.reportList = response.data.rows || []
            this.total = response.data.total || 0
          } else {
            this.reportList = response.data || []
            this.total = response.result ? response.result.total : 0
          }
        } else {
          this.$message.error(response.msg || '查询失败')
          this.reportList = []
          this.total = 0
        }
        this.loading = false
      }).catch(() => {
        this.$message.error('查询报表列表失败')
        this.reportList = []
        this.total = 0
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      this.queryParams.reportName = this.searchForm.reportName || null
      this.queryParams.authorityId = this.searchForm.authorityId || null
      this.queryParams.reportStatus = this.searchForm.reportStatus || null
      if (this.searchForm.reportPeriodRange && this.searchForm.reportPeriodRange.length === 2) {
        this.queryParams.reportPeriodStart = this.searchForm.reportPeriodRange[0]
        this.queryParams.reportPeriodEnd = this.searchForm.reportPeriodRange[1]
      } else {
        this.queryParams.reportPeriodStart = null
        this.queryParams.reportPeriodEnd = null
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        reportName: '',
        authorityId: '',
        reportStatus: '',
        reportPeriodRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        reportName: null,
        authorityId: null,
        reportStatus: null,
        reportPeriodStart: null,
        reportPeriodEnd: null
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.activeTab = 'basic'
      this.open = true
      this.title = '新增报表'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const updateRow = row && row.reportId ? row : (this.selectedRows.length === 1 ? this.selectedRows[0] : null)
      if (updateRow) {
        this.form = { ...updateRow }
      }
      this.activeTab = 'basic'
      this.open = true
      this.title = '修改报表'
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
      const link = document.createElement('a')
      link.href = '/vab-mock-server/qqsk/globalTreasurer/regulatory/report/importTemplate'
      link.download = `report_template_${new Date().getTime()}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
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
          if (this.form.reportId) {
            // 修改
            updateReport(this.form).then(response => {
              if (response.code === 1) {
                this.$message.success('修改成功')
                this.open = false
                this.getList()
              } else {
                this.$message.error(response.msg || '修改失败')
              }
            }).catch(() => {
              this.$message.error('修改报表失败')
            })
          } else {
            // 新增 - 设置提交日期为今天
            const today = new Date()
            const year = today.getFullYear()
            const month = String(today.getMonth() + 1).padStart(2, '0')
            const day = String(today.getDate()).padStart(2, '0')
            this.form.submitDate = `${year}-${month}-${day}`
            addReport(this.form).then(response => {
              if (response.code === 1) {
                this.$message.success('新增成功')
                this.open = false
                this.getList()
              } else {
                this.$message.error(response.msg || '新增失败')
              }
            }).catch(() => {
              this.$message.error('新增报表失败')
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const reportIds = row.reportId || this.ids.join(',')
      const deleteCount = row ? 1 : this.ids.length
      this.$confirm('是否确认删除报表编号为"' + reportIds + '"的数据项？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delReport(reportIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            // 清除选中状态
            this.ids = []
            this.selectedRows = []
            this.single = true
            this.multiple = true
            // 如果删除后当前页没有数据了，回到前一页
            const newTotal = this.total - deleteCount
            const totalPage = Math.ceil(newTotal / this.queryParams.size)
            if (this.queryParams.current > totalPage && this.queryParams.current > 1) {
              this.queryParams.current = this.queryParams.current - 1
            }
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除报表失败')
        })
      }).catch(() => {})
    },
    /** 导出按钮操作 - 已选择则导出选中数据，否则导出当前页 */
    handleExport() {
      const exportParams = { ...this.queryParams }
      if (this.ids.length > 0) {
        exportParams.reportIds = this.ids.join(',')
      }
      exportReportData(exportParams).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = `report_data_${new Date().getTime()}.xlsx`
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message({ type: 'success', message: '导出成功' })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message({ type: 'error', message: '导出失败，请稍后重试' })
      })
    },
    /** 批量报送 */
    handleBatchSubmit() {
      if (this.ids.length === 0) {
        this.$message.warning('请选择要报送的报表')
        return
      }
      this.$confirm('是否确认批量报送所有选中的报表？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchSubmitReports(this.ids, 'ONLINE').then(response => {
          if (response.code === 1) {
            this.$message.success('报送成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '报送失败')
          }
        }).catch(() => {
          this.$message.error('批量报送失败')
        })
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.reportId)
      this.selectedRows = selection
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
      validateReport(row.reportId).then(response => {
        if (response.code === 1) {
          this.$message.success('数据校验通过')
          this.getList()
        } else {
          this.$message.error(response.msg || '数据校验失败')
        }
      }).catch(() => {
        this.$message.error('数据校验请求失败')
      })
    },
    /** 预览报表 */
    handlePreview(row) {
      getReportById(row.reportId).then(response => {
        if (response.code === 1 && response.data) {
          const data = response.data;
          this.$alert(`
            <div style="max-height:400px;overflow:auto;">
              <p><b>报告编号:</b> ${data.reportNo || ''}</p>
              <p><b>报告名称:</b> ${data.reportName || ''}</p>
              <p><b>报告状态:</b> ${data.reportStatus || ''}</p>
              <p><b>提交方式:</b> ${data.submissionMethod || ''}</p>
              <p><b>提交日期:</b> ${data.submitDate || ''}</p>
              <p><b>生成时间:</b> ${data.generatedTime || ''}</p>
              <p><b>验证时间:</b> ${data.validatedTime || ''}</p>
              <p><b>确认编号:</b> ${data.acknowledgmentNo || ''}</p>
              <p><b>拒绝原因:</b> ${data.rejectReason || '无'}</p>
            </div>
          `, '报表预览 - ' + data.reportName, {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          });
        } else {
          this.$message.error('获取报表详情失败');
        }
      });
    },
    /** 报送 */
    handleSubmit(row) {
      this.$confirm('是否确认报送报表"' + row.reportName + '"？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitReport(row.reportId, 'ONLINE').then(response => {
          if (response.code === 1) {
            this.$message.success('报送成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '报送失败')
          }
        }).catch(() => {
          this.$message.error('报送请求失败')
        })
      }).catch(() => {})
    },
    /** 撤回 */
    handleRecall(row) {
      this.$confirm('是否确认撤回报表"' + row.reportName + '"？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return recallReport(row.reportId);
      }).then(response => {
        if (response.code === 1) {
          this.$message.success('撤回成功');
          this.getList();
        } else {
          this.$message.error(response.msg || '撤回失败');
        }
      }).catch(() => {})
    },
    /** 操作历史 */
    handleHistory(row) {
      getReportHistory(row.reportId).then(response => {
        if (response.code === 1 && response.data) {
          const history = response.data;
          let html = '<div style="max-height:300px;overflow:auto;">';
          if (history.length === 0) {
            html += '<p>暂无操作历史记录</p>';
          } else {
            history.forEach(item => {
              html += `<p><b>${item.action || ''}</b> - ${item.time || ''} ${item.remark || ''}</p>`;
            });
          }
          html += '</div>';
          this.$alert(html, '操作历史 - ' + row.reportName, {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          });
        }
      });
    },
    /** 复制 */
    handleCopy(row) {
      this.$prompt('请输入新报表名称', '复制报表', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.reportName + '_副本'
      }).then(({ value }) => {
        const newReportNo = row.reportNo + '_COPY_' + Date.now()
        copyReport(row.reportId, newReportNo, value).then(response => {
          if (response.code === 1) {
            this.$message.success('复制成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '复制失败')
          }
        }).catch(() => {
          this.$message.error('复制请求失败')
        })
      }).catch(() => {})
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
        reportNo: null,
        reportName: null,
        authorityId: null,
        reportPeriod: null,
        dueDate: null,
        reportStatus: 'DRAFT',
        submissionMethod: null,
        acknowledgmentNo: null,
        rejectReason: null,
        remark: null
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
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
        'AUTH001': 'primary',
        'AUTH002': 'success',
        'AUTH003': 'warning',
        'AUTH004': 'info',
        'AUTH005': 'danger',
        'AUTH006': ''
      }
      return tagMap[authority] || ''
    },
    /** 获取监管机构文本 */
    getAuthorityText(authorityId) {
      // 优先从动态加载的选项中查找
      if (this.authorityOptions && this.authorityOptions.length > 0) {
        const found = this.authorityOptions.find(item => item.authorityId === authorityId)
        if (found) return found.authorityName
      }
      // 兜底使用静态映射
      const textMap = {
        'AUTH001': '央行',
        'AUTH002': '银保监会',
        'AUTH003': '证监会',
        'AUTH004': '外管局',
        'AUTH005': '财政部',
        'AUTH006': '税务总局'
      }
      return textMap[authorityId] || authorityId
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'GENERATED': 'warning',
        'VALIDATED': 'primary',
        'SUBMITTED': '',
        'ACCEPTED': 'success',
        'REJECTED': 'danger'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'GENERATED': '已生成',
        'VALIDATED': '已验证',
        'SUBMITTED': '已提交',
        'ACCEPTED': '已通过',
        'REJECTED': '已退回'
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
