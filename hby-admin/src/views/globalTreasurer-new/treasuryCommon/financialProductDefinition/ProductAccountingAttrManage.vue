<template>
  <div class="product-accounting-attr-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-notebook-2"></i>
            产品会计属性管理
          </h2>
          <p class="page-description">管理金融产品会计属性配置，包括科目映射、核算规则、报表分类和税务处理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增属性
          </el-button>
          <el-button type="success" icon="el-icon-setting" @click="handleMapping">
            科目映射
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 属性统计卡片 -->
    <div class="attr-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总属性数</div>
                <div class="card-value">{{ totalAttributes }}</div>
                <div class="card-change">已配置属性</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon subject-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">会计科目</div>
                <div class="card-value">{{ accountingSubjects }}</div>
                <div class="card-change positive">科目映射</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon rule-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-info">
                <div class="card-title">核算规则</div>
                <div class="card-value">{{ accountingRules }}</div>
                <div class="card-change">规则配置</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">启用属性</div>
                <div class="card-value">{{ activeAttributes }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="属性编码">
            <el-input
              v-model="listQuery.attrCode"
              placeholder="请输入属性编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="属性名称">
            <el-input
              v-model="listQuery.attrName"
              placeholder="请输入属性名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券投资" value="BOND" />
              <el-option label="股票投资" value="EQUITY" />
              <el-option label="基金投资" value="FUND" />
              <el-option label="衍生品投资" value="DERIVATIVE" />
            </el-select>
          </el-form-item>
          <el-form-item label="会计科目类型">
            <el-select
              v-model="listQuery.accountingSubjectType"
              placeholder="请选择会计科目类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="资产类" value="ASSET" />
              <el-option label="负债类" value="LIABILITY" />
              <el-option label="权益类" value="EQUITY" />
              <el-option label="损益类" value="PROFIT_LOSS" />
              <el-option label="成本类" value="COST" />
            </el-select>
          </el-form-item>
          <el-form-item label="核算方式">
            <el-select
              v-model="listQuery.accountingMethod"
              placeholder="请选择核算方式"
              clearable
              style="width: 120px;"
            >
              <el-option label="摊余成本" value="AMORTIZED_COST" />
              <el-option label="公允价值" value="FAIR_VALUE" />
              <el-option label="权益法" value="EQUITY_METHOD" />
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

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="属性编码" prop="attrCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.attrCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="属性名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.attrName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目编码" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubjectCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.accountingSubjectName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会计科目类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountingSubjectTypeColor(row.accountingSubjectType)" size="small">
            {{ getAccountingSubjectTypeName(row.accountingSubjectType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="核算方式" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAccountingMethodColor(row.accountingMethod)" size="small">
            {{ getAccountingMethodName(row.accountingMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ formatTime(row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ formatTime(row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 创建/编辑对话框 -->
    <el-dialog :title="dialogStatus === 'create' ? '新增产品会计属性' : '编辑产品会计属性'" :visible.sync="dialogFormVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="属性编码" prop="attrCode">
              <el-input v-model="temp.attrCode" placeholder="请输入属性编码" :disabled="dialogStatus === 'update'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="属性名称" prop="attrName">
              <el-input v-model="temp.attrName" placeholder="请输入属性名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品类型" prop="productType">
              <el-select v-model="temp.productType" placeholder="请选择产品类型" style="width: 100%;">
                <el-option label="银行理财" value="BANK_WEALTH" />
                <el-option label="债券投资" value="BOND" />
                <el-option label="股票投资" value="EQUITY" />
                <el-option label="基金投资" value="FUND" />
                <el-option label="衍生品投资" value="DERIVATIVE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会计科目编码" prop="accountingSubjectCode">
              <el-input v-model="temp.accountingSubjectCode" placeholder="请输入会计科目编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会计科目名称" prop="accountingSubjectName">
              <el-input v-model="temp.accountingSubjectName" placeholder="请输入会计科目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会计科目类型" prop="accountingSubjectType">
              <el-select v-model="temp.accountingSubjectType" placeholder="请选择会计科目类型" style="width: 100%;">
                <el-option label="资产类" value="ASSET" />
                <el-option label="负债类" value="LIABILITY" />
                <el-option label="权益类" value="EQUITY" />
                <el-option label="损益类" value="PROFIT_LOSS" />
                <el-option label="成本类" value="COST" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="核算方式" prop="accountingMethod">
              <el-select v-model="temp.accountingMethod" placeholder="请选择核算方式" style="width: 100%;">
                <el-option label="摊余成本" value="AMORTIZED_COST" />
                <el-option label="公允价值" value="FAIR_VALUE" />
                <el-option label="权益法" value="EQUITY_METHOD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-radio-group v-model="temp.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="temp.description" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 科目映射对话框 -->
    <el-dialog title="科目映射配置" :visible.sync="mappingDialogVisible" width="1000px" :close-on-click-modal="false">
      <el-table :data="mappingList" border style="width: 100%">
        <el-table-column prop="attrName" label="属性名称" width="200" />
        <el-table-column prop="accountingSubjectCode" label="科目编码" width="120" />
        <el-table-column prop="accountingSubjectName" label="科目名称" width="200" />
        <el-table-column prop="accountingSubjectType" label="科目类型" width="120">
          <template slot-scope="{row}">
            <el-tag size="small">{{ getAccountingSubjectTypeName(row.accountingSubjectType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="映射状态" width="120">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.mappingEnabled"
              :active-value="1"
              :inactive-value="0"
              active-text="启用"
              inactive-text="禁用"
              @change="handleMappingStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="editMapping(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="mappingDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="saveMapping" :loading="mappingSaving">保存映射</el-button>
      </div>
    </el-dialog>

    <!-- 编辑映射对话框 -->
    <el-dialog title="编辑科目映射" :visible.sync="editMappingDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="mappingForm" :model="currentMapping" :rules="mappingRules" label-width="120px">
        <el-form-item label="属性名称">
          <el-input v-model="currentMapping.attrName" disabled />
        </el-form-item>
        <el-form-item label="会计科目编码" prop="accountingSubjectCode">
          <el-input v-model="currentMapping.accountingSubjectCode" placeholder="请输入会计科目编码" />
        </el-form-item>
        <el-form-item label="会计科目名称" prop="accountingSubjectName">
          <el-input v-model="currentMapping.accountingSubjectName" placeholder="请输入会计科目名称" />
        </el-form-item>
        <el-form-item label="会计科目类型" prop="accountingSubjectType">
          <el-select v-model="currentMapping.accountingSubjectType" placeholder="请选择会计科目类型" style="width: 100%;">
            <el-option label="资产类" value="ASSET" />
            <el-option label="负债类" value="LIABILITY" />
            <el-option label="权益类" value="EQUITY" />
            <el-option label="损益类" value="PROFIT_LOSS" />
            <el-option label="成本类" value="COST" />
          </el-select>
        </el-form-item>
        <el-form-item label="映射状态" prop="mappingEnabled">
          <el-radio-group v-model="currentMapping.mappingEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editMappingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEditMapping">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getProductAccountingAttrList,
  createProductAccountingAttr,
  updateProductAccountingAttr,
  deleteProductAccountingAttr,
  exportProductAccountingAttrs,
  batchUpdateMapping
} from '@/api/globalTreasurer/financialProductDefinition/productAccountingAttrManage'

export default {
  name: 'ProductAccountingAttrManage',
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
        attrCode: undefined,
        attrName: undefined,
        productType: undefined,
        accountingSubjectType: undefined,
        accountingMethod: undefined
      },
      totalAttributes: 0,
      accountingSubjects: 0,
      accountingRules: 0,
      activeAttributes: 0,

      // 对话框相关
      dialogFormVisible: false,
      dialogStatus: '',
      mappingDialogVisible: false,
      editMappingDialogVisible: false,
      mappingSaving: false,

      // 临时数据
      temp: {
        attrId: undefined,
        attrCode: '',
        attrName: '',
        productType: '',
        accountingSubjectCode: '',
        accountingSubjectName: '',
        accountingSubjectType: '',
        accountingMethod: '',
        isEnabled: 1,
        description: ''
      },

      // 映射列表
      mappingList: [],

      // 当前编辑的映射
      currentMapping: {
        attrId: undefined,
        attrName: '',
        accountingSubjectCode: '',
        accountingSubjectName: '',
        accountingSubjectType: '',
        mappingEnabled: 1
      },

      // 表单验证规则
      rules: {
        attrCode: [
          { required: true, message: '请输入属性编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        attrName: [
          { required: true, message: '请输入属性名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        productType: [
          { required: true, message: '请选择产品类型', trigger: 'change' }
        ],
        accountingSubjectCode: [
          { required: true, message: '请输入会计科目编码', trigger: 'blur' }
        ],
        accountingSubjectName: [
          { required: true, message: '请输入会计科目名称', trigger: 'blur' }
        ],
        accountingSubjectType: [
          { required: true, message: '请选择会计科目类型', trigger: 'change' }
        ],
        accountingMethod: [
          { required: true, message: '请选择核算方式', trigger: 'change' }
        ]
      },

      // 映射表单验证规则
      mappingRules: {
        accountingSubjectCode: [
          { required: true, message: '请输入会计科目编码', trigger: 'blur' }
        ],
        accountingSubjectName: [
          { required: true, message: '请输入会计科目名称', trigger: 'blur' }
        ],
        accountingSubjectType: [
          { required: true, message: '请选择会计科目类型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    // 格式化时间戳为年-月-日 时:分:秒
    formatTime(timestamp) {
      if (!timestamp) return ''
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    getProductTypeName(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DERIVATIVE': '衍生品投资'
      }
      return typeMap[type] || type
    },
    getProductTypeColor(type) {
      const colorMap = {
        'BANK_WEALTH': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DERIVATIVE': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getAccountingSubjectTypeName(type) {
      const typeMap = {
        'ASSET': '资产类',
        'LIABILITY': '负债类',
        'EQUITY': '权益类',
        'PROFIT_LOSS': '损益类',
        'COST': '成本类'
      }
      return typeMap[type] || type
    },
    getAccountingSubjectTypeColor(type) {
      const colorMap = {
        'ASSET': 'success',
        'LIABILITY': 'warning',
        'EQUITY': 'primary',
        'PROFIT_LOSS': 'info',
        'COST': 'danger'
      }
      return colorMap[type] || 'default'
    },
    getAccountingMethodName(method) {
      const methodMap = {
        'AMORTIZED_COST': '摊余成本',
        'FAIR_VALUE': '公允价值',
        'EQUITY_METHOD': '权益法'
      }
      return methodMap[method] || method
    },
    getAccountingMethodColor(method) {
      const colorMap = {
        'AMORTIZED_COST': 'primary',
        'FAIR_VALUE': 'success',
        'EQUITY_METHOD': 'warning'
      }
      return colorMap[method] || 'default'
    },
    // 获取统计数据
    getStatistics() {
      this.totalAttributes = this.list.length
      this.activeAttributes = this.list.filter(item => item.isEnabled === 1).length
      this.accountingSubjects = [...new Set(this.list.map(item => item.accountingSubjectCode))].length
      this.accountingMethods = [...new Set(this.list.map(item => item.accountingMethod))].length
    },

    // 获取列表数据
    getList() {
      this.listLoading = true
      const params = {
        pageNo: this.listQuery.page,
        pageSize: this.listQuery.limit,
        attrCode: this.listQuery.attrCode,
        attrName: this.listQuery.attrName,
        productType: this.listQuery.productType,
        accountingSubjectType: this.listQuery.accountingSubjectType,
        accountingMethod: this.listQuery.accountingMethod
      }

      getProductAccountingAttrList(params).then(response => {
        console.log('后端返回的完整数据:', response)
        // 响应拦截器返回的是完整JsonBean对象,需要取data字段
        const data = response.data || {}
        console.log('response.data:', data)
        console.log('data.tlist:', data.tlist)
        console.log('data.tlist.length:', data.tlist ? data.tlist.length : 0)
        this.list = data.tlist || []
        this.total = data.totalRecord || 0
        console.log('赋值后的this.list:', this.list)
        console.log('this.list.length:', this.list.length)
        this.getStatistics()
        this.listLoading = false
        // 强制重新渲染表格
        this.tableKey = this.tableKey + 1
      }).catch(error => {
        console.error('获取产品会计属性列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    // 重置搜索条件
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        attrCode: undefined,
        attrName: undefined,
        productType: undefined,
        accountingSubjectType: undefined,
        accountingMethod: undefined
      }
      this.getList()
    },
    // 重置临时数据
    resetTemp() {
      this.temp = {
        attrId: undefined,
        attrCode: '',
        attrName: '',
        productType: '',
        accountingSubjectCode: '',
        accountingSubjectName: '',
        accountingSubjectType: '',
        accountingMethod: '',
        isEnabled: 1,
        description: ''
      }
    },

    // 打开创建对话框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createProductAccountingAttr(this.temp).then(response => {
            console.log('创建产品会计属性 - 后端返回:', response)
            // 响应拦截器返回的是完整JsonBean对象: {code: 1, msg: "创建成功", data: {...}}
            if (response.code === 1 || response.code === '1') {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response.msg || response.message || '创建失败')
            }
          }).catch(error => {
            console.error('创建产品会计属性失败:', error)
            this.$message.error('创建失败')
          })
        }
      })
    },

    // 打开编辑对话框
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateProductAccountingAttr(tempData).then(response => {
            console.log('更新产品会计属性 - 后端返回:', response)
            // 响应拦截器返回的是完整JsonBean对象: {code: 1, msg: "更新成功", data: null}
            if (response.code === 1 || response.code === '1') {
              const index = this.list.findIndex(v => v.attrId === this.temp.attrId)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.getStatistics()
            } else {
              this.$message.error(response.msg || response.message || '更新失败')
            }
          }).catch(error => {
            console.error('更新产品会计属性失败:', error)
            this.$message.error('更新失败')
          })
        }
      })
    },

    // 删除数据
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProductAccountingAttr(row.attrId).then(response => {
          console.log('删除产品会计属性 - 后端返回:', response)
          // 响应拦截器返回的是完整JsonBean对象: {code: 1, msg: "删除成功", data: null}
          if (response.code === 1 || response.code === '1') {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.list.splice(index, 1)
            this.total--
            this.getStatistics()
          } else {
            this.$message.error(response.msg || response.message || '删除失败')
          }
        }).catch(error => {
          console.error('删除产品会计属性失败:', error)
          this.$message.error('删除失败')
        })
      })
    },

    // 科目映射
    handleMapping() {
      this.mappingDialogVisible = true
      // 初始化映射列表，添加mappingEnabled字段
      this.mappingList = this.list.map(item => ({
        ...item,
        mappingEnabled: item.isEnabled || 0
      }))
    },

    // 编辑映射
    editMapping(row) {
      // 打开编辑对话框，填充当前映射数据
      this.currentMapping = {
        attrId: row.attrId,
        attrName: row.attrName,
        accountingSubjectCode: row.accountingSubjectCode,
        accountingSubjectName: row.accountingSubjectName,
        accountingSubjectType: row.accountingSubjectType,
        mappingEnabled: row.mappingEnabled
      }
      this.editMappingDialogVisible = true
      // 清除之前的验证
      this.$nextTick(() => {
        if (this.$refs['mappingForm']) {
          this.$refs['mappingForm'].clearValidate()
        }
      })
    },

    // 确认编辑映射
    confirmEditMapping() {
      this.$refs['mappingForm'].validate((valid) => {
        if (valid) {
          // 在映射列表中找到对应的项并更新
          const index = this.mappingList.findIndex(item => item.attrId === this.currentMapping.attrId)
          if (index !== -1) {
            this.mappingList.splice(index, 1, {
              ...this.mappingList[index],
              accountingSubjectCode: this.currentMapping.accountingSubjectCode,
              accountingSubjectName: this.currentMapping.accountingSubjectName,
              accountingSubjectType: this.currentMapping.accountingSubjectType,
              mappingEnabled: this.currentMapping.mappingEnabled
            })
          }
          this.editMappingDialogVisible = false
          this.$message.success('映射修改成功，请点击"保存映射"按钮保存更改')
        }
      })
    },

    // 处理映射状态变化
    handleMappingStatusChange(row) {
      console.log(`映射状态变更: ${row.attrName} -> ${row.mappingEnabled === 1 ? '启用' : '禁用'}`)
    },

    // 保存映射
    saveMapping() {
      this.mappingSaving = true
      // 准备要保存的数据
      const updateList = this.mappingList.map(item => ({
        attrId: item.attrId,
        accountingSubjectCode: item.accountingSubjectCode,
        accountingSubjectName: item.accountingSubjectName,
        accountingSubjectType: item.accountingSubjectType,
        isEnabled: item.mappingEnabled
      }))

      console.log('批量更新映射:', updateList)

      batchUpdateMapping({ list: updateList }).then(response => {
        console.log('批量更新映射 - 后端返回:', response)
        if (response.code === 1 || response.code === '1') {
          this.$message.success('科目映射保存成功')
          this.mappingDialogVisible = false
          // 刷新列表
          this.getList()
        } else {
          this.$message.error(response.msg || response.message || '保存失败')
        }
      }).catch(error => {
        console.error('批量更新映射失败:', error)
        this.$message.error('保存失败')
      }).finally(() => {
        this.mappingSaving = false
      })
    },

    // 导出配置
    handleExport() {
      const params = {
        ...this.listQuery,
        attrCode: this.listQuery.attrCode,
        attrName: this.listQuery.attrName,
        productType: this.listQuery.productType,
        accountingSubjectType: this.listQuery.accountingSubjectType,
        accountingMethod: this.listQuery.accountingMethod
      }

      exportProductAccountingAttrs(params).then(response => {
        // 处理文件下载
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `产品会计属性配置_${new Date().toLocaleDateString()}.xlsx`
        link.click()

        this.$notify({
          title: '成功',
          message: '导出成功',
          type: 'success',
          duration: 2000
        })
      }).catch(error => {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      })
    }
  }
}
</script>
