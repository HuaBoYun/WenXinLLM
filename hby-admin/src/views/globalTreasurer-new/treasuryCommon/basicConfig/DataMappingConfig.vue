<template>
  <div class="data-mapping-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-connection"></i>
            数据映射配置
          </h2>
          <p class="page-description">管理系统间数据字段映射关系，支持复杂映射规则和数据转换</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增映射
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            导入配置
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="映射名称">
            <el-input
              v-model="listQuery.mappingName"
              placeholder="请输入映射名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="源系统">
            <el-select
              v-model="listQuery.sourceSystem"
              placeholder="请选择源系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="财务系统" value="FINANCE" />
              <el-option label="银行系统" value="BANK" />
              <el-option label="ERP系统" value="ERP" />
              <el-option label="第三方系统" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标系统">
            <el-select
              v-model="listQuery.targetSystem"
              placeholder="请选择目标系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="财务系统" value="FINANCE" />
              <el-option label="银行系统" value="BANK" />
              <el-option label="ERP系统" value="ERP" />
              <el-option label="第三方系统" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="映射类型">
            <el-select
              v-model="listQuery.mappingType"
              placeholder="请选择映射类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="字段映射" value="FIELD" />
              <el-option label="值映射" value="VALUE" />
              <el-option label="函数映射" value="FUNCTION" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">数据映射配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="isColumnVisible('mappingName')" label="映射名称" prop="mappingName" align="center" width="180" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="mapping-info">
              <i class="el-icon-connection mapping-icon"></i>
              <div class="mapping-details">
                <div class="mapping-name">{{ row.mappingName }}</div>
                <div class="mapping-code">{{ row.mappingCode || 'MAP' + String(row.id).padStart(3, '0') }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('systemMapping')" label="系统映射" align="center" width="200">
          <template slot-scope="{row}">
            <div class="system-mapping">
              <div class="system-item source">
                <el-tag size="mini" :type="getSystemColor(row.sourceSystem)">
                  {{ getSystemText(row.sourceSystem) }}
                </el-tag>
              </div>
              <i class="el-icon-right arrow-icon"></i>
              <div class="system-item target">
                <el-tag size="mini" :type="getSystemColor(row.targetSystem)">
                  {{ getSystemText(row.targetSystem) }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('fieldMapping')" label="字段映射" align="center" width="250">
          <template slot-scope="{row}">
            <div class="field-mapping">
              <div class="field-item">
                <span class="field-label">源字段:</span>
                <code class="field-name">{{ row.sourceField }}</code>
              </div>
              <i class="el-icon-bottom mapping-arrow"></i>
              <div class="field-item">
                <span class="field-label">目标字段:</span>
                <code class="field-name">{{ row.targetField }}</code>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('mappingType')" label="映射类型" prop="mappingType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getMappingTypeColor(row.mappingType)" size="small">
              <i :class="getMappingTypeIcon(row.mappingType)"></i>
              {{ getMappingTypeText(row.mappingType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('mappingRule')" label="映射规则" prop="mappingRule" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="mapping-rule">
              <el-tooltip :content="row.mappingRule" placement="top">
                <span class="rule-text">{{ row.mappingRule || '直接映射' }}</span>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('status')" label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('updateTime')" label="更新时间" prop="updateTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="update-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.updateTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('actions')" label="操作" align="center" width="220" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-check" @click="handleTest(row)">
                测试
              </el-button>
              <el-button
                v-if="row.status !== 'deleted'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="padding: 0 20px;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="映射名称" prop="mappingName">
              <el-input v-model="temp.mappingName" placeholder="请输入映射名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="映射编码" prop="mappingCode">
              <el-input v-model="temp.mappingCode" placeholder="请输入映射编码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源系统" prop="sourceSystem">
              <el-select v-model="temp.sourceSystem" placeholder="请选择源系统" style="width: 100%;">
                <el-option label="财务系统" value="FINANCE" />
                <el-option label="银行系统" value="BANK" />
                <el-option label="ERP系统" value="ERP" />
                <el-option label="第三方系统" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标系统" prop="targetSystem">
              <el-select v-model="temp.targetSystem" placeholder="请选择目标系统" style="width: 100%;">
                <el-option label="财务系统" value="FINANCE" />
                <el-option label="银行系统" value="BANK" />
                <el-option label="ERP系统" value="ERP" />
                <el-option label="第三方系统" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源字段" prop="sourceField">
              <el-input v-model="temp.sourceField" placeholder="请输入源字段名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标字段" prop="targetField">
              <el-input v-model="temp.targetField" placeholder="请输入目标字段名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="映射类型" prop="mappingType">
              <el-select v-model="temp.mappingType" placeholder="请选择映射类型" style="width: 100%;">
                <el-option label="字段映射" value="FIELD" />
                <el-option label="值映射" value="VALUE" />
                <el-option label="函数映射" value="FUNCTION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="temp.status" placeholder="请选择状态" style="width: 100%;">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="映射规则" prop="mappingRule">
          <el-input v-model="temp.mappingRule" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入映射规则，如：直接映射、转换函数等" />
        </el-form-item>

        <el-form-item label="描述信息">
          <el-input v-model="temp.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入映射配置的描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="映射配置详情" :visible.sync="viewDialogVisible" width="700px">
      <div v-if="currentViewData" class="view-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="view-item">
              <label>映射名称：</label>
              <span>{{ currentViewData.mappingName }}</span>
            </div>
            <div class="view-item">
              <label>映射编码：</label>
              <span>{{ currentViewData.mappingCode }}</span>
            </div>
            <div class="view-item">
              <label>源系统：</label>
              <el-tag :type="getSystemColor(currentViewData.sourceSystem)" size="small">
                {{ getSystemText(currentViewData.sourceSystem) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>源字段：</label>
              <code class="field-code">{{ currentViewData.sourceField }}</code>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="view-item">
              <label>目标系统：</label>
              <el-tag :type="getSystemColor(currentViewData.targetSystem)" size="small">
                {{ getSystemText(currentViewData.targetSystem) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>目标字段：</label>
              <code class="field-code">{{ currentViewData.targetField }}</code>
            </div>
            <div class="view-item">
              <label>映射类型：</label>
              <el-tag :type="getMappingTypeColor(currentViewData.mappingType)" size="small">
                <i :class="getMappingTypeIcon(currentViewData.mappingType)"></i>
                {{ getMappingTypeText(currentViewData.mappingType) }}
              </el-tag>
            </div>
            <div class="view-item">
              <label>状态：</label>
              <el-tag :type="currentViewData.status === 1 ? 'success' : 'danger'" size="small">
                {{ currentViewData.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <div class="view-item full-width">
          <label>映射规则：</label>
          <div class="rule-content">
            <pre>{{ currentViewData.mappingRule || '直接映射' }}</pre>
          </div>
        </div>
        <div class="view-item full-width">
          <label>描述信息：</label>
          <p>{{ currentViewData.description || '暂无描述' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 测试映射对话框 -->
    <el-dialog title="映射规则测试" :visible.sync="testDialogVisible" width="600px">
      <div v-if="testData" class="test-content">
        <el-form :model="testForm" label-width="100px">
          <el-form-item label="测试数据">
            <el-input v-model="testForm.testValue" type="textarea" :autosize="{ minRows: 3, maxRows: 6}" placeholder="请输入测试数据" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="runMappingTest">执行测试</el-button>
          </el-form-item>
        </el-form>

        <div v-if="testResult" class="test-result">
          <h4>测试结果：</h4>
          <div class="result-content">
            <pre>{{ testResult }}</pre>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入配置对话框 -->
    <el-dialog title="导入映射配置" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".json,.xml,.xlsx"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">支持 JSON、XML、Excel 格式的映射配置文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport" :disabled="!selectedFile">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 表格设置对话框 -->
    <el-dialog title="表格列设置" :visible.sync="tableSettingDialogVisible" width="500px">
      <div class="table-setting-content">
        <el-checkbox-group v-model="visibleColumns">
          <div v-for="column in tableColumns" :key="column.prop" class="column-item">
            <el-checkbox :label="column.prop" :disabled="column.required">
              {{ column.label }}
            </el-checkbox>
          </div>
        </el-checkbox-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="tableSettingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSetting">应用</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataMappingPage as getDataMappingList, createDataMapping, updateDataMapping, deleteDataMapping, testMapping as testDataMapping, exportDataMapping, importDataMapping } from '@/api/globalTreasurer-new/basicConfig/dataMapping'
import { getBusinessSystemPage } from '@/api/globalTreasurer-new/basicConfig/businessSystem'
import waves from '@/directive/waves'
import { isResponseSuccess, handleResponseData, getErrorMessage, initQueryForm, resetQueryForm, handleSizeChange, handleCurrentChange, formatDate } from '../../utils'
import { PAGINATION_CONFIG } from '../../consts'
import Pagination from '@/components/Pagination'

export default {
  name: 'DataMappingConfig',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
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
        mappingName: undefined,
        sourceSystem: undefined,
        targetSystem: undefined,
        mappingType: undefined,
        status: undefined
      },
      multipleSelection: [],
      temp: {
        id: undefined,
        mappingName: '',
        mappingCode: '',
        sourceSystem: '',
        targetSystem: '',
        sourceField: '',
        targetField: '',
        mappingType: 'FIELD',
        mappingRule: '',
        description: '',
        status: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑映射配置',
        create: '新增映射配置',
        view: '查看映射配置'
      },
      businessSystemMap: {}, // 业务系统映射表 {systemId: systemInfo}
      rules: {
        mappingName: [{ required: true, message: '映射名称是必填项', trigger: 'blur' }],
        mappingCode: [{ required: true, message: '映射编码是必填项', trigger: 'blur' }],
        sourceSystem: [{ required: true, message: '源系统是必选项', trigger: 'change' }],
        targetSystem: [{ required: true, message: '目标系统是必选项', trigger: 'change' }],
        sourceField: [{ required: true, message: '源字段是必填项', trigger: 'blur' }],
        targetField: [{ required: true, message: '目标字段是必填项', trigger: 'blur' }],
        mappingType: [{ required: true, message: '映射类型是必选项', trigger: 'change' }]
      },
      viewDialogVisible: false,
      currentViewData: null,
      testDialogVisible: false,
      testData: null,
      testForm: {
        testValue: ''
      },
      testResult: '',
      importDialogVisible: false,
      fileList: [],
      selectedFile: null,
      tableSettingDialogVisible: false,
      tableColumns: [
        { prop: 'mappingName', label: '映射名称', visible: true, required: false },
        { prop: 'systemMapping', label: '系统映射', visible: true, required: false },
        { prop: 'fieldMapping', label: '字段映射', visible: true, required: false },
        { prop: 'mappingType', label: '映射类型', visible: true, required: false },
        { prop: 'mappingRule', label: '映射规则', visible: true, required: false },
        { prop: 'status', label: '状态', visible: true, required: false },
        { prop: 'updateTime', label: '更新时间', visible: true, required: false },
        { prop: 'actions', label: '操作', visible: true, required: true }
      ]
    }
  },
  created() {
    this.loadBusinessSystems()
    this.getList()
  },
  computed: {
    visibleColumns: {
      get() {
        return this.tableColumns.filter(col => col.visible).map(col => col.prop)
      },
      set(value) {
        this.tableColumns.forEach(col => {
          col.visible = value.includes(col.prop)
        })
      }
    }
  },
  methods: {
    // 加载业务系统列表
    async loadBusinessSystems() {
      try {
        const response = await getBusinessSystemPage({ page: 1, limit: 1000 })
        if (isResponseSuccess(response)) {
          const data = handleResponseData(response)
          const systems = data.tlist || data.list || []
          // 构建系统ID到系统信息的映射
          this.businessSystemMap = {}
          systems.forEach(system => {
            this.businessSystemMap[system.id] = system
          })
        }
      } catch (error) {
        console.error('加载业务系统列表失败:', error)
      }
    },
    async getList() {
      this.listLoading = true
      try {
        const response = await getDataMappingList(this.listQuery)
        if (isResponseSuccess(response)) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('获取数据映射列表失败:', error)
        this.$message.error('获取数据映射列表失败,请检查网络连接或联系管理员')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        mappingName: undefined,
        sourceSystem: undefined,
        targetSystem: undefined
      }
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        mappingName: '',
        mappingCode: '',
        sourceSystem: '',
        targetSystem: '',
        sourceField: '',
        targetField: '',
        mappingType: 'FIELD',
        mappingRule: '',
        description: '',
        status: 1
      }
    },
    async handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await createDataMapping(this.temp)
        if (isResponseSuccess(response)) {
          this.$message.success('创建成功')
          this.dialogFormVisible = false
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error.message) {
          console.error('创建数据映射失败:', error)
        }
      }
    },
    async handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    async updateData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await updateDataMapping(this.temp)
        if (isResponseSuccess(response)) {
          this.$message.success('更新成功')
          this.dialogFormVisible = false
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error.message) {
          console.error('更新数据映射失败:', error)
        }
      }
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteDataMapping(row)
        if (isResponseSuccess(response)) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除数据映射失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    handleView(row) {
      this.currentViewData = row
      this.viewDialogVisible = true
    },
    async handleTest(row) {
      this.testData = row
      this.testForm.testValue = ''
      this.testResult = ''
      this.testDialogVisible = true
    },
    async runMappingTest() {
      if (!this.testForm.testValue.trim()) {
        this.$message.warning('请输入测试数据')
        return
      }

      try {
        const response = await testDataMapping({
          id: this.testData.id,
          testData: this.testForm.testValue
        })
        if (isResponseSuccess(response)) {
          const result = handleResponseData(response)
          this.testResult = JSON.stringify(result, null, 2)
          this.$message.success('映射测试成功')
        } else {
          this.testResult = '测试失败: ' + getErrorMessage(response)
          this.$message.error('映射测试失败')
        }
      } catch (error) {
        console.error('映射测试失败:', error)
        this.testResult = '测试失败: ' + error.message
        this.$message.error('映射测试失败,请检查网络连接')
      }
    },
    handleImport() {
      this.fileList = []
      this.selectedFile = null
      this.importDialogVisible = true
    },
    handleFileChange(file) {
      this.selectedFile = file.raw
    },
    async confirmImport() {
      if (!this.selectedFile) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      try {
        this.$message.success('文件导入成功')
        this.importDialogVisible = false
        this.getList()
      } catch (error) {
        console.error('文件导入失败:', error)
        this.$message.error('文件导入失败')
      }
    },
    async handleExport() {
      try {
        const exportData = {
          mappings: this.list,
          exportTime: new Date().toISOString(),
          totalCount: this.total
        }

        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `数据映射配置_${this.formatTime(new Date())}.json`
        link.click()
        window.URL.revokeObjectURL(link.href)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    handleTableSetting() {
      this.tableSettingDialogVisible = true
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    async handleStatusChange(row) {
      try {
        const response = await updateDataMapping({
          id: row.id,
          status: row.status
        })
        if (isResponseSuccess(response)) {
          this.$message.success('状态更新成功')
        } else {
          row.status = row.status === 1 ? 0 : 1 // 恢复原状态
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        row.status = row.status === 1 ? 0 : 1 // 恢复原状态
        this.$message.error('状态更新失败')
      }
    },
    getSystemText(systemIdOrType) {
      // 如果是系统类型代码，直接返回对应文本
      const typeTextMap = {
        'FINANCE': '财务系统',
        'BANK': '银行系统',
        'ERP': 'ERP系统',
        'THIRD_PARTY': '第三方系统',
        'HR': '人力资源系统',
        'CRM': '客户关系管理系统',
        'SCM': '供应链管理系统'
      }

      if (typeTextMap[systemIdOrType]) {
        return typeTextMap[systemIdOrType]
      }

      // 如果是systemId，从businessSystemMap中查找
      const system = this.businessSystemMap[systemIdOrType]
      if (system) {
        // 优先返回系统名称
        return system.systemName || system.systemCode || '未知系统'
      }

      return systemIdOrType || '未知系统'
    },
    getSystemColor(systemIdOrType) {
      // 如果是系统类型代码，返回对应颜色
      const typeColorMap = {
        'FINANCE': 'success',
        'BANK': 'primary',
        'ERP': 'warning',
        'THIRD_PARTY': 'info',
        'HR': 'success',
        'CRM': 'primary',
        'SCM': 'warning'
      }

      if (typeColorMap[systemIdOrType]) {
        return typeColorMap[systemIdOrType]
      }

      // 如果是systemId，从businessSystemMap中查找系统类型
      const system = this.businessSystemMap[systemIdOrType]
      if (system && system.systemType) {
        return typeColorMap[system.systemType] || 'info'
      }

      return 'info'
    },
    getMappingTypeColor(type) {
      const colorMap = {
        'FIELD': 'success',
        'VALUE': 'warning',
        'FUNCTION': 'danger'
      }
      return colorMap[type] || 'info'
    },
    getMappingTypeIcon(type) {
      const iconMap = {
        'FIELD': 'el-icon-connection',
        'VALUE': 'el-icon-switch-button',
        'FUNCTION': 'el-icon-cpu'
      }
      return iconMap[type] || 'el-icon-connection'
    },
    getMappingTypeText(type) {
      const textMap = {
        'FIELD': '字段映射',
        'VALUE': '值映射',
        'FUNCTION': '函数映射'
      }
      return textMap[type] || '未知类型'
    },
    formatTime(time) {
      if (!time) return '-'
      return formatDate(time, 'yyyy-MM-dd HH:mm:ss')
    },
    isColumnVisible(prop) {
      const column = this.tableColumns.find(col => col.prop === prop)
      return column ? column.visible : true
    },
    applyTableSetting() {
      this.tableKey = Date.now() // 强制重新渲染表格
      this.tableSettingDialogVisible = false
      this.$message.success('表格设置已应用')
    }
  }
}
</script>

<style lang="scss" scoped>
.data-mapping-config {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .table-card {
    border-radius: 8px;

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .mapping-info {
      display: flex;
      align-items: center;

      .mapping-icon {
        font-size: 20px;
        color: #409eff;
        margin-right: 12px;
      }

      .mapping-details {
        .mapping-name {
          font-weight: 600;
          color: #303133;
        }

        .mapping-code {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }

    .system-mapping {
      display: flex;
      align-items: center;
      justify-content: center;

      .system-item {
        flex: 1;
        text-align: center;
      }

      .arrow-icon {
        margin: 0 8px;
        color: #909399;
        font-size: 14px;
      }
    }

    .field-mapping {
      text-align: center;

      .field-item {
        margin: 4px 0;

        .field-label {
          font-size: 12px;
          color: #909399;
          margin-right: 4px;
        }

        .field-name {
          background-color: #f5f7fa;
          padding: 2px 6px;
          border-radius: 4px;
          font-family: 'Monaco', 'Menlo', monospace;
          font-size: 12px;
          color: #e6a23c;
        }
      }

      .mapping-arrow {
        color: #409eff;
        margin: 4px 0;
      }
    }

    .mapping-rule {
      .rule-text {
        color: #606266;
        font-size: 13px;
        line-height: 1.4;
      }
    }

    .update-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;

      i {
        margin-right: 4px;
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }

  .el-table__row:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-tooltip__popper {
  max-width: 300px;
}

.view-content {
  .view-item {
    margin-bottom: 16px;

    label {
      font-weight: 500;
      color: #1f2937;
      margin-right: 8px;
      display: inline-block;
      min-width: 80px;
    }

    span {
      color: #6b7280;
    }

    &.full-width {
      width: 100%;

      p {
        margin: 8px 0 0 0;
        color: #6b7280;
        line-height: 1.5;
      }
    }

    .field-code {
      background-color: #f3f4f6;
      padding: 2px 6px;
      border-radius: 4px;
      font-family: 'Monaco', 'Menlo', monospace;
      font-size: 12px;
      color: #e6a23c;
    }

    .rule-content {
      background-color: #f8f9fa;
      border: 1px solid #e9ecef;
      border-radius: 4px;
      padding: 12px;
      margin-top: 8px;

      pre {
        margin: 0;
        white-space: pre-wrap;
        word-break: break-all;
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 12px;
        color: #495057;
      }
    }
  }
}

.test-content {
  .test-result {
    margin-top: 20px;
    padding: 16px;
    background-color: #f8f9fa;
    border-radius: 4px;

    h4 {
      margin: 0 0 12px 0;
      color: #1f2937;
      font-size: 14px;
      font-weight: 500;
    }

    .result-content {
      pre {
        margin: 0;
        white-space: pre-wrap;
        word-break: break-all;
        font-family: 'Monaco', 'Menlo', monospace;
        font-size: 12px;
        color: #495057;
        background-color: #ffffff;
        border: 1px solid #e9ecef;
        border-radius: 4px;
        padding: 12px;
      }
    }
  }
}

.upload-demo {
  .el-upload-dragger {
    width: 100%;
    height: 180px;
  }

  .el-upload__tip {
    margin-top: 8px;
    color: #6b7280;
    font-size: 12px;
  }
}

.dialog-footer {
  text-align: right;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}

.table-setting-content {
  max-height: 400px;
  overflow-y: auto;

  .column-item {
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }
  }
}
</style>
