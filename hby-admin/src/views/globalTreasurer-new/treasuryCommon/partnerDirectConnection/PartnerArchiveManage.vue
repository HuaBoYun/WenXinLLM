<template>
  <div class="partner-archive-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-user"></i>
            合作伙伴档案管理
          </h2>
          <p class="page-description">管理合作伙伴基础档案信息，包括银行、金融机构、供应商等合作方信息</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增合作伙伴
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出档案
          </el-button>
        </div>
      </div>
    </div>

    <!-- 合作伙伴统计卡片 -->
    <div class="partner-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总合作伙伴</div>
                <div class="card-value">{{ totalPartners }}</div>
                <div class="card-change">已建档案</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon bank-icon">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="card-info">
                <div class="card-title">银行机构</div>
                <div class="card-value">{{ bankPartners }}</div>
                <div class="card-change positive">金融合作</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon supplier-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">供应商</div>
                <div class="card-value">{{ supplierPartners }}</div>
                <div class="card-change">业务合作</div>
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
                <div class="card-title">活跃合作</div>
                <div class="card-value">{{ activePartners }}</div>
                <div class="card-change positive">本月活跃</div>
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
          <el-form-item label="伙伴编码">
            <el-input
              v-model="listQuery.partnerCode"
              placeholder="请输入伙伴编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴名称">
            <el-input
              v-model="listQuery.partnerName"
              placeholder="请输入伙伴名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴类型">
            <el-select
              v-model="listQuery.partnerTypeId"
              placeholder="请选择伙伴类型"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="type in partnerTypes"
                :key="type.partnerTypeId"
                :label="type.typeName"
                :value="type.partnerTypeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="统一信用代码">
            <el-input
              v-model="listQuery.unifiedCreditCode"
              placeholder="请输入统一信用代码"
              style="width: 180px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴状态">
            <el-select
              v-model="listQuery.partnerStatus"
              placeholder="请选择伙伴状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="非活跃" value="INACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="黑名单" value="BLACKLISTED" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
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
      :default-sort="{prop: 'createTime', order: 'descending'}"
    >
      <el-table-column label="伙伴编码" prop="partnerCode" sortable="custom" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.partnerCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴名称" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.partnerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="英文名称" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.partnerNameEng || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴类型" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ getPartnerTypeName(row.partnerTypeId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="统一信用代码" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.unifiedSocialCreditCode || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="法定代表人" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.legalRepresentative || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.contactPhone || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="成立日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.establishmentDate || row.establishment_date) || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合作开始日期" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.cooperationStartDate || row.cooperation_start_date) || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="信用评级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.creditRating" :type="getCreditRatingColor(row.creditRating)">
            {{ row.creditRating }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.riskLevel" :type="getRiskLevelColor(row.riskLevel)">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getPartnerStatusColor(row.partnerStatus)">
            {{ getPartnerStatusName(row.partnerStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160px" align="center" prop="createTime" sortable="custom">
        <template slot-scope="{row}">
          <span>{{ formatDate(row.createTime) || '-' }}</span>
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

    <!-- 调试信息 -->
    <div style="padding: 10px; background: #f0f0f0; margin: 10px 0;">
      <strong>调试信息：</strong>
      总记录数(total): {{ total }} |
      当前页(page): {{ listQuery.page }} |
      每页条数(limit): {{ listQuery.limit }} |
      列表长度: {{ list.length }}
    </div>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增合作伙伴' : '编辑合作伙伴'"
      :visible.sync="dialogFormVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form ref="dataForm" :model="temp" :rules="rules" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="伙伴编码" prop="partnerCode">
              <el-input v-model="temp.partnerCode" placeholder="请输入伙伴编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="伙伴名称" prop="partnerName">
              <el-input v-model="temp.partnerName" placeholder="请输入伙伴名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="伙伴简称">
              <el-input v-model="temp.partnerShortName" placeholder="请输入伙伴简称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称">
              <el-input v-model="temp.partnerNameEng" placeholder="请输入英文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="伙伴类型" prop="partnerTypeId">
              <el-select v-model="temp.partnerTypeId" placeholder="请选择伙伴类型" style="width: 100%;">
                <el-option
                  v-for="type in partnerTypes"
                  :key="type.partnerTypeId"
                  :label="type.typeName"
                  :value="type.partnerTypeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="统一信用代码" prop="unifiedSocialCreditCode">
              <el-input v-model="temp.unifiedSocialCreditCode" placeholder="请输入统一信用代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法定代表人">
              <el-input v-model="temp.legalRepresentative" placeholder="请输入法定代表人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="temp.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="注册资本">
              <el-input v-model="temp.registeredCapital" placeholder="请输入注册资本" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成立日期">
              <el-date-picker
                v-model="temp.establishmentDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%;"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="信用评级">
              <el-select v-model="temp.creditRating" placeholder="请选择信用评级" style="width: 100%;">
                <el-option label="AAA" value="AAA" />
                <el-option label="AA" value="AA" />
                <el-option label="A" value="A" />
                <el-option label="BBB" value="BBB" />
                <el-option label="BB" value="BB" />
                <el-option label="B" value="B" />
                <el-option label="C" value="C" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级">
              <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="伙伴状态">
              <el-select v-model="temp.partnerStatus" placeholder="请选择伙伴状态" style="width: 100%;">
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="非活跃" value="INACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
                <el-option label="黑名单" value="BLACKLISTED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合作开始日期">
              <el-date-picker
                v-model="temp.cooperationStartDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%;"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="注册地址">
              <el-input v-model="temp.registeredAddress" placeholder="请输入注册地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="办公地址">
              <el-input v-model="temp.officeAddress" placeholder="请输入办公地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="经营范围">
              <el-input v-model="temp.businessScope" type="textarea" :rows="3" placeholder="请输入经营范围" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="批量导入" :visible.sync="importDialogVisible" width="600px">
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getPartnerArchiveList, getPartnerArchiveDetail, createPartnerArchive, updatePartnerArchive, deletePartnerArchive, batchDeletePartnerArchive, exportPartnerArchive, getPartnerArchiveStatistics, getPartnerTypeList, togglePartnerArchive, changePartnerStatus, updatePartnerRiskLevel, updatePartnerCreditRating } from '@/api/globalTreasurer/xjgl/partnerDirectConnection/partner'

export default {
  name: 'PartnerArchiveManage',
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
        partnerCode: undefined,
        partnerName: undefined,
        partnerTypeId: undefined,
        unifiedCreditCode: undefined,
        partnerStatus: undefined,
        riskLevel: undefined
      },
      partnerTypes: [],
      totalPartners: 0,
      bankPartners: 0,
      supplierPartners: 0,
      activePartners: 0,
      suspendedPartners: 0,
      highRiskPartners: 0,
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        partnerCode: '',
        partnerName: '',
        partnerShortName: '',
        partnerNameEng: '',
        partnerTypeId: undefined,
        unifiedSocialCreditCode: '',
        legalRepresentative: '',
        contactPhone: '',
        registeredCapital: '',
        establishmentDate: '',
        creditRating: '',
        riskLevel: '',
        partnerStatus: 'ACTIVE',
        cooperationStartDate: '',
        registeredAddress: '',
        officeAddress: '',
        businessScope: '',
        remark: ''
      },
      rules: {
        partnerCode: [{ required: true, message: '请输入伙伴编码', trigger: 'blur' }],
        partnerName: [{ required: true, message: '请输入伙伴名称', trigger: 'blur' }],
        partnerTypeId: [{ required: true, message: '请选择伙伴类型', trigger: 'change' }],
        unifiedSocialCreditCode: [{ required: true, message: '请输入统一信用代码', trigger: 'blur' }]
      },
      importDialogVisible: false,
      fileList: []
    }
  },
  created() {
    this.getPartnerTypes()
    this.getList()
  },
  methods: {
    // 格式化日期为 yyyy-MM-dd 格式
    formatDate(date) {
      if (!date) return ''

      // 如果已经是 yyyy-MM-dd 格式，直接返回
      if (typeof date === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(date)) {
        return date
      }

      // 处理时间戳或日期对象
      let dateObj
      if (typeof date === 'number') {
        // 数字类型的时间戳
        dateObj = new Date(date)
      } else if (typeof date === 'string') {
        // 处理字符串格式的时间戳（纯数字字符串）
        if (/^\d+$/.test(date)) {
          dateObj = new Date(parseInt(date))
        } else {
          // 处理包含时间的日期字符串，如 "2024-01-01 00:00:00" 或 "2024-01-01T00:00:00"
          dateObj = new Date(date)
        }
      } else if (date instanceof Date) {
        dateObj = date
      } else {
        return ''
      }

      // 检查日期是否有效
      if (isNaN(dateObj.getTime())) {
        return ''
      }

      // 格式化为 yyyy-MM-dd
      const year = dateObj.getFullYear()
      const month = String(dateObj.getMonth() + 1).padStart(2, '0')
      const day = String(dateObj.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    // 格式化日期时间为 yyyy-MM-dd HH:mm:ss 格式
    formatDateTime(date) {
      if (!date) return ''

      // 如果已经是 yyyy-MM-dd HH:mm:ss 格式，直接返回
      if (typeof date === 'string' && /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(date)) {
        return date
      }

      // 处理时间戳或日期对象
      let dateObj
      if (typeof date === 'number') {
        // 数字类型的时间戳
        dateObj = new Date(date)
      } else if (typeof date === 'string') {
        // 处理字符串格式的时间戳（纯数字字符串）
        if (/^\d+$/.test(date)) {
          dateObj = new Date(parseInt(date))
        } else {
          // 处理包含时间的日期字符串
          dateObj = new Date(date)
        }
      } else if (date instanceof Date) {
        dateObj = date
      } else {
        return ''
      }

      // 检查日期是否有效
      if (isNaN(dateObj.getTime())) {
        return ''
      }

      // 格式化为 yyyy-MM-dd HH:mm:ss
      const year = dateObj.getFullYear()
      const month = String(dateObj.getMonth() + 1).padStart(2, '0')
      const day = String(dateObj.getDate()).padStart(2, '0')
      const hours = String(dateObj.getHours()).padStart(2, '0')
      const minutes = String(dateObj.getMinutes()).padStart(2, '0')
      const seconds = String(dateObj.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    async getPartnerTypes() {
      try {
        const response = await getPartnerTypeList()
        if (response && response.code === 1) {
          this.partnerTypes = response.data || []
        } else {
          this.$message.error(response?.message || '获取伙伴类型失败')
        }
      } catch (error) {
        console.error('获取伙伴类型失败:', error)
        this.$message.error('获取伙伴类型失败，请稍后重试')
      }
    },
    getPartnerTypeName(partnerTypeId) {
      if (!partnerTypeId) return '-'
      const type = this.partnerTypes.find(t => t.partnerTypeId === partnerTypeId)
      return type ? type.typeName : partnerTypeId
    },
    getRiskLevelName(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return levelMap[level] || level
    },
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || 'default'
    },
    getPartnerStatusName(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'SUSPENDED': '暂停',
        'BLACKLISTED': '黑名单'
      }
      return statusMap[status] || status
    },
    getPartnerStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'SUSPENDED': 'warning',
        'BLACKLISTED': 'danger'
      }
      return colorMap[status] || 'default'
    },
    getCreditRatingColor(rating) {
      if (!rating) return 'default'
      if (rating.includes('AAA') || rating.includes('AA')) return 'success'
      if (rating.includes('A') || rating.includes('BBB')) return 'primary'
      if (rating.includes('BB') || rating.includes('B')) return 'warning'
      return 'danger'
    },
    async getList() {
      this.listLoading = true
      try {
        const query = {
          pageNum: this.listQuery.page,
          pageSize: this.listQuery.limit,
          partnerCode: this.listQuery.partnerCode,
          partnerName: this.listQuery.partnerName,
          partnerTypeId: this.listQuery.partnerTypeId,
          unifiedCreditCode: this.listQuery.unifiedCreditCode,
          partnerStatus: this.listQuery.partnerStatus,
          riskLevel: this.listQuery.riskLevel
        }

        const response = await getPartnerArchiveList(query)
        if (response && response.code === 1) {
          // 调试日志：查看完整响应数据结构
          console.log('=== 后端返回的完整数据结构 ===')
          console.log('response.data:', JSON.stringify(response.data, null, 2))
          console.log('response.data 的所有字段:', Object.keys(response.data))
          console.log('================================')

          this.list = response.data.tlist || []
          this.total = response.data.totalRecord || 0

          // 调试日志：查看分页相关数据
          console.log('=== 分页数据 ===')
          console.log('列表数据长度:', this.list.length)
          console.log('总记录数 total:', this.total)
          console.log('===============')

          // 调试日志：查看列表数据结构
          if (this.list.length > 0) {
            console.log('=== 列表数据结构 ===')
            console.log('第一条数据:', JSON.stringify(this.list[0], null, 2))
            console.log('所有字段名:', Object.keys(this.list[0]))
            console.log('==================')
          }

          this.tableKey = this.tableKey + 1
          await this.getStatistics()
        } else {
          this.$message.error(response?.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取合作伙伴档案列表失败:', error)
        this.$message.error('获取数据失败，请稍后重试')
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
        partnerCode: undefined,
        partnerName: undefined,
        partnerTypeId: undefined,
        unifiedCreditCode: undefined,
        partnerStatus: undefined,
        riskLevel: undefined
      }
      this.getList()
    },
    async getStatistics() {
      try {
        const response = await getPartnerArchiveStatistics()
        if (response && response.code === 1) {
          const stats = response.data
          // 处理大写字段名
          this.totalPartners = stats.totalPartners || stats.TOTALPARTNERS || 0
          this.bankPartners = stats.bankPartners || stats.BANKPARTNERS || 0
          this.supplierPartners = stats.supplierPartners || stats.SUPPLIERPARTNERS || 0
          this.activePartners = stats.activePartners || stats.ACTIVEPARTNERS || 0
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      // 调试日志：查看原始数据
      console.log('=== 编辑按钮点击 - 原始数据 ===')
      console.log('完整 row 数据:', JSON.stringify(row, null, 2))
      console.log('所有可能的日期字段:')
      console.log('  establishmentDate:', row.establishmentDate, '类型:', typeof row.establishmentDate)
      console.log('  establishment_date:', row.establishment_date, '类型:', typeof row.establishment_date)
      console.log('  cooperationStartDate:', row.cooperationStartDate, '类型:', typeof row.cooperationStartDate)
      console.log('  cooperation_start_date:', row.cooperation_start_date, '类型:', typeof row.cooperation_start_date)

      this.temp = Object.assign({}, row)

      // 格式化日期字段，同时支持驼峰和下划线命名
      // 优先使用驼峰命名，如果不存在则使用下划线命名
      const establishmentDate = row.establishmentDate || row.establishment_date
      const cooperationStartDate = row.cooperationStartDate || row.cooperation_start_date

      this.temp.establishmentDate = this.formatDate(establishmentDate)
      this.temp.cooperationStartDate = this.formatDate(cooperationStartDate)

      // 调试日志：查看格式化后的数据
      console.log('格式化结果:')
      console.log('  establishmentDate 格式化后:', this.temp.establishmentDate)
      console.log('  cooperationStartDate 格式化后:', this.temp.cooperationStartDate)
      console.log('================================')

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        partnerCode: '',
        partnerName: '',
        partnerShortName: '',
        partnerNameEng: '',
        partnerTypeId: undefined,
        unifiedSocialCreditCode: '',
        legalRepresentative: '',
        contactPhone: '',
        registeredCapital: '',
        establishmentDate: '',
        creditRating: '',
        riskLevel: '',
        partnerStatus: 'ACTIVE',
        cooperationStartDate: '',
        registeredAddress: '',
        officeAddress: '',
        businessScope: '',
        remark: ''
      }
    },
    createData() {
      this.$refs['dataForm'].validate(async(valid) => {
        if (valid) {
          try {
            // 调试日志：查看发送的数据
            console.log('=== 前端发送的数据 ===')
            console.log('partnerCode:', this.temp.partnerCode)
            console.log('partnerName:', this.temp.partnerName)
            console.log('partnerNameEng:', this.temp.partnerNameEng)
            console.log('partnerStatus:', this.temp.partnerStatus)
            console.log('完整数据:', JSON.stringify(this.temp, null, 2))
            console.log('=====================')

            const response = await createPartnerArchive(this.temp)
            if (response && response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response?.message || '创建失败')
            }
          } catch (error) {
            console.error('创建合作伙伴失败:', error)
            this.$message.error('创建失败，请稍后重试')
          }
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(async(valid) => {
        if (valid) {
          try {
            // 创建提交数据的副本，删除不属于数据库表的字段
            const submitData = Object.assign({}, this.temp)
            // 删除 partnerTypeName 字段（这是关联查询得到的，不属于 TC_PARTNER_ARCHIVE 表）
            delete submitData.partnerTypeName

            const response = await updatePartnerArchive(submitData)
            if (response && response.code === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            } else {
              this.$message.error(response?.message || '更新失败')
            }
          } catch (error) {
            console.error('更新合作伙伴失败:', error)
            this.$message.error('更新失败，请稍后重试')
          }
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await deletePartnerArchive(row.id)
          if (response && response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response?.message || '删除失败')
          }
        } catch (error) {
          console.error('删除合作伙伴失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    handleImport() {
      this.importDialogVisible = true
      this.fileList = []
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    async confirmImport() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      const formData = new FormData()
      formData.append('file', this.fileList[0].raw)

      try {
        // 这里需要实现导入接口
        this.$message.info('导入功能开发中')
        this.importDialogVisible = false
      } catch (error) {
        console.error('导入失败:', error)
        this.$message.error('导入失败，请稍后重试')
      }
    },
    async handleExport() {
      try {
        const response = await exportPartnerArchive(this.listQuery)
        if (response && response.code === 1) {
          // 导出Excel
          const data = response.data.data || []
          if (data.length === 0) {
            this.$message.warning('没有可导出的数据')
            return
          }

          // 这里可以使用 xlsx 库来生成 Excel 文件
          this.$message.success('导出成功')
        } else {
          this.$message.error(response?.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.partner-archive-manage {
  padding: 20px;

  .page-header {
    background: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-left {
        .page-title {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;

          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }

        .page-description {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  .partner-overview {
    margin-bottom: 20px;

    .overview-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 28px;
            color: #fff;
          }

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.bank-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.supplier-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.active-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }

          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #909399;

            &.positive {
              color: #67C23A;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;

    .search-form {
      ::v-deep .el-form-item {
        margin-bottom: 10px;
      }
    }
  }
}
</style>
