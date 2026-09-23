<template>
  <div class="ukey-vendor-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-key"></i>
            UKey厂商管理
          </h2>
          <p class="page-description">管理Ukey设备厂商信息，包括厂商资质、产品型号、技术支持和合作状态</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增厂商
          </el-button>
          <el-button type="success" icon="el-icon-check" @click="handleCertification">
            资质认证
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 厂商统计卡片 -->
    <div class="vendor-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-key"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总厂商数</div>
                <div class="card-value">{{ totalVendors }}</div>
                <div class="card-change">已合作厂商</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon certified-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="card-info">
                <div class="card-title">认证厂商</div>
                <div class="card-value">{{ certifiedVendors }}</div>
                <div class="card-change positive">资质认证</div>
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
                <div class="card-title">活跃厂商</div>
                <div class="card-value">{{ activeVendors }}</div>
                <div class="card-change positive">正常合作</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon product-icon">
                <i class="el-icon-goods"></i>
              </div>
              <div class="card-info">
                <div class="card-title">产品型号</div>
                <div class="card-value">{{ productModels }}</div>
                <div class="card-change">支持型号</div>
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
          <el-form-item label="厂商名称">
            <el-input
              v-model="listQuery.vendorName"
              placeholder="请输入厂商名称"
              style="width: 200px;"
              clearable
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="厂商类型">
            <el-select
              v-model="listQuery.vendorType"
              placeholder="请选择厂商类型"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="item in vendorTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="认证状态">
            <el-select
              v-model="listQuery.certificationStatus"
              placeholder="请选择认证状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="已认证" value="CERTIFIED" />
              <el-option label="待认证" value="PENDING" />
              <el-option label="未认证" value="UNCERTIFIED" />
            </el-select>
          </el-form-item>
          <el-form-item label="合作状态">
            <el-select
              v-model="listQuery.cooperationStatus"
              placeholder="请选择合作状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常合作" value="ACTIVE" />
              <el-option label="暂停合作" value="SUSPENDED" />
              <el-option label="终止合作" value="TERMINATED" />
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
          <span class="title-text">厂商列表</span>
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
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="isColumnVisible('vendorCode')" label="厂商编码" prop="vendorCode" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <el-tag size="small" type="info">{{ row.vendorCode }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('vendorName')" label="厂商名称" prop="vendorName" align="center" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="vendor-name">
              <i :class="getVendorIcon(row.vendorType)"></i>
              <span>{{ row.vendorName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('vendorType')" label="厂商类型" prop="vendorType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getVendorTypeColor(row.vendorType)" size="small">
              {{ getVendorTypeName(row.vendorType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('certificationStatus')" label="认证状态" prop="certificationStatus" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getCertificationStatusColor(row.certificationStatus)" size="small">
              {{ getCertificationStatusName(row.certificationStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('cooperationStatus')" label="合作状态" prop="cooperationStatus" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getCooperationStatusColor(row.cooperationStatus)" size="small">
              {{ getCooperationStatusName(row.cooperationStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('productModels')" label="产品型号" prop="productModels" min-width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span class="product-models">{{ row.productModels }}</span>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('contactInfo')" label="联系方式" prop="contactInfo" align="center" width="180" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span class="contact-info">{{ row.contactInfo }}</span>
          </template>
        </el-table-column>

        <el-table-column label="启用状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="280" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-medal" @click="handleVendorCertification(row)">
                认证
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button
                v-if="row.status!='deleted'"
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

    <!-- 表格设置对话框 -->
    <el-dialog
      title="表格设置"
      :visible.sync="dialogTableSettingVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="table-setting-content">
        <el-form label-width="120px" size="small">
          <el-form-item label="显示列设置">
            <el-checkbox-group v-model="visibleColumns">
              <el-checkbox
                v-for="column in allColumns"
                :key="column.prop"
                :label="column.prop"
              >
                {{ column.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="每页显示条数">
            <el-select v-model="customPageSize" placeholder="选择每页显示条数" style="width: 120px;">
              <el-option label="10条" :value="10" />
              <el-option label="20条" :value="20" />
              <el-option label="50条" :value="50" />
              <el-option label="100条" :value="100" />
            </el-select>
          </el-form-item>
          <el-form-item label="表格高度">
            <el-radio-group v-model="tableHeight">
              <el-radio label="auto">自适应</el-radio>
              <el-radio label="400">固定400px</el-radio>
              <el-radio label="600">固定600px</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSettings">确定</el-button>
      </div>
    </el-dialog>

    <!-- 厂商认证对话框 -->
    <el-dialog
      title="厂商认证"
      :visible.sync="dialogCertificationVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="certification-content">
        <el-form :model="certificationForm" label-width="120px">
          <el-form-item label="厂商名称">
            <el-input v-model="certificationForm.vendorName" readonly />
          </el-form-item>
          <el-form-item label="认证类型">
            <el-select v-model="certificationForm.certificationType" placeholder="请选择认证类型" style="width: 100%;">
              <el-option label="资质认证" value="QUALIFICATION" />
              <el-option label="安全认证" value="SECURITY" />
              <el-option label="技术认证" value="TECHNICAL" />
              <el-option label="服务认证" value="SERVICE" />
            </el-select>
          </el-form-item>
          <el-form-item label="认证机构">
            <el-input v-model="certificationForm.certificationAuthority" placeholder="请输入认证机构名称" />
          </el-form-item>
          <el-form-item label="认证有效期">
            <el-date-picker
              v-model="certificationForm.expiryDate"
              type="date"
              placeholder="选择认证有效期"
              style="width: 100%;"
            />
          </el-form-item>
          <el-form-item label="认证文件">
            <el-upload
              class="upload-demo"
              drag
              action="#"
              :auto-upload="false"
              :on-change="handleFileChange"
              :file-list="certificationForm.fileList"
              :limit="5"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">只能上传pdf/doc/docx文件，且不超过10MB</div>
            </el-upload>
          </el-form-item>
          <el-form-item label="备注说明">
            <el-input
              v-model="certificationForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入认证相关的备注说明"
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCertificationVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCertification">提交认证</el-button>
      </div>
    </el-dialog>

    <!-- 厂商配置对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增厂商' : '编辑厂商'"
      :visible.sync="dialogFormVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 700px; margin-left:50px;"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="厂商编码" prop="vendorCode">
              <el-input v-model="temp.vendorCode" placeholder="请输入厂商编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="厂商名称" prop="vendorName">
              <el-input v-model="temp.vendorName" placeholder="请输入厂商名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="厂商类型" prop="vendorType">
              <el-select v-model="temp.vendorType" placeholder="请选择厂商类型" style="width: 100%;">
                <el-option
                  v-for="item in vendorTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="厂商等级" prop="vendorLevel">
              <el-select v-model="temp.vendorLevel" placeholder="请选择厂商等级" style="width: 100%;">
                <el-option label="A级" value="A" />
                <el-option label="B级" value="B" />
                <el-option label="C级" value="C" />
                <el-option label="D级" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="认证状态" prop="certificationStatus">
              <el-select v-model="temp.certificationStatus" placeholder="请选择认证状态" style="width: 100%;">
                <el-option label="已认证" value="CERTIFIED" />
                <el-option label="待认证" value="PENDING" />
                <el-option label="未认证" value="UNCERTIFIED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合作状态" prop="cooperationStatus">
              <el-select v-model="temp.cooperationStatus" placeholder="请选择合作状态" style="width: 100%;">
                <el-option label="正常合作" value="ACTIVE" />
                <el-option label="暂停合作" value="SUSPENDED" />
                <el-option label="终止合作" value="TERMINATED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="产品型号" prop="productModels">
          <el-input v-model="temp.productModels" placeholder="请输入支持的产品型号，多个型号用逗号分隔" />
        </el-form-item>

        <el-form-item label="联系方式" prop="contactInfo">
          <el-input v-model="temp.contactInfo" placeholder="请输入联系方式（电话、邮箱等）" />
        </el-form-item>

        <el-form-item label="厂商地址" prop="address">
          <el-input v-model="temp.address" placeholder="请输入厂商地址" />
        </el-form-item>

        <el-form-item label="厂商描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入厂商描述"
          />
        </el-form-item>

        <el-form-item label="启用状态">
          <el-switch
            v-model="temp.isEnabled"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
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
  </div>
</template>

<script>
import {
  getUkeyVendorList,
  createUkeyVendor,
  updateUkeyVendor,
  deleteUkeyVendor,
  getUkeyVendorStatistics,
  exportUkeyVendor
} from '@/api/globalTreasurer/treasuryCommon'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { isResponseSuccess, handleResponseData, getErrorMessage } from '../../utils'

export default {
  name: 'UkeyVendorManage',
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
      multipleSelection: [],
      dialogFormVisible: false,
      dialogTableSettingVisible: false,
      dialogCertificationVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        vendorCode: '',
        vendorName: '',
        vendorType: '',
        vendorLevel: '',
        certificationStatus: '',
        cooperationStatus: '',
        productModels: '',
        contactInfo: '',
        address: '',
        description: '',
        isEnabled: 1
      },
      rules: {
        vendorCode: [{ required: true, message: '厂商编码不能为空', trigger: 'blur' }],
        vendorName: [{ required: true, message: '厂商名称不能为空', trigger: 'blur' }],
        vendorType: [{ required: true, message: '厂商类型不能为空', trigger: 'change' }],
        vendorLevel: [{ required: true, message: '厂商等级不能为空', trigger: 'change' }],
        certificationStatus: [{ required: true, message: '认证状态不能为空', trigger: 'change' }],
        cooperationStatus: [{ required: true, message: '合作状态不能为空', trigger: 'change' }]
      },
      vendorTypes: [
        { label: '硬件厂商', value: 'HARDWARE' },
        { label: '软件厂商', value: 'SOFTWARE' },
        { label: '集成厂商', value: 'INTEGRATION' },
        { label: '服务厂商', value: 'SERVICE' }
      ],
      // 统计数据
      totalVendors: 0,
      certifiedVendors: 0,
      activeVendors: 0,
      productModels: 0,
      listQuery: {
        page: 1,
        limit: 20,
        vendorName: undefined,
        vendorType: undefined,
        certificationStatus: undefined,
        cooperationStatus: undefined
      },
      // 表格设置相关
      allColumns: [
        { prop: 'vendorCode', label: '厂商编码' },
        { prop: 'vendorName', label: '厂商名称' },
        { prop: 'vendorType', label: '厂商类型' },
        { prop: 'certificationStatus', label: '认证状态' },
        { prop: 'cooperationStatus', label: '合作状态' },
        { prop: 'productModels', label: '产品型号' },
        { prop: 'contactInfo', label: '联系方式' }
      ],
      visibleColumns: ['vendorCode', 'vendorName', 'vendorType', 'certificationStatus', 'cooperationStatus', 'productModels', 'contactInfo'],
      customPageSize: 10,
      tableHeight: 'auto',
      // 认证表单
      certificationForm: {
        vendorId: undefined,
        vendorName: '',
        certificationType: '',
        certificationAuthority: '',
        expiryDate: '',
        fileList: [],
        remark: ''
      }
    }
  },
  created() {
    this.loadTableSettings()
    this.getList()
    this.loadStatistics()
  },
  methods: {
    /**
     * 加载统计数据
     */
    async loadStatistics() {
      try {
        const response = await getUkeyVendorStatistics()
        if (isResponseSuccess(response)) {
          // 统计数据直接从response.data取，不走handleResponseData（那个是列表专用的）
          const data = response.data || {}
          this.totalVendors = data.totalVendors || 0
          this.certifiedVendors = data.certifiedVendors || 0
          this.activeVendors = data.activeVendors || 0
          this.productModels = data.productModels || 0
        }
      } catch (error) {
        console.warn('加载统计数据失败:', error)
        // 设置默认值
        this.totalVendors = 0
        this.certifiedVendors = 0
        this.activeVendors = 0
        this.productModels = 0
      }
    },

    /**
     * 获取数据列表
     */
    async getList() {
      this.listLoading = true
      try {
        const response = await getUkeyVendorList(this.listQuery)

        if (isResponseSuccess(response)) {
          const rawData = response.data || {}
          this.list = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
        } else {
          this.$message.error(getErrorMessage(response))
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取UKey厂商列表失败:', error)
        this.$message.error('获取数据失败，请检查网络连接')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 搜索过滤
     */
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    /**
     * 重置搜索
     */
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        vendorName: undefined,
        vendorType: undefined,
        certificationStatus: undefined,
        cooperationStatus: undefined
      }
      this.getList()
    },

    /**
     * 多选变化
     */
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    /**
     * 表格设置
     */
    handleTableSetting() {
      this.dialogTableSettingVisible = true
    },

    /**
     * 应用表格设置
     */
    applyTableSettings() {
      // 应用每页显示条数
      this.listQuery.limit = this.customPageSize
      this.listQuery.page = 1

      // 刷新表格
      this.getList()

      // 保存设置到localStorage
      const settings = {
        visibleColumns: this.visibleColumns,
        customPageSize: this.customPageSize,
        tableHeight: this.tableHeight
      }
      localStorage.setItem('ukeyVendor_tableSettings', JSON.stringify(settings))

      this.dialogTableSettingVisible = false
      this.$message.success('表格设置已保存')
    },

    /**
     * 加载表格设置
     */
    loadTableSettings() {
      try {
        const settings = localStorage.getItem('ukeyVendor_tableSettings')
        if (settings) {
          const parsed = JSON.parse(settings)
          this.visibleColumns = parsed.visibleColumns || this.visibleColumns
          this.customPageSize = parsed.customPageSize || this.customPageSize
          this.tableHeight = parsed.tableHeight || this.tableHeight

          if (parsed.customPageSize) {
            this.listQuery.limit = parsed.customPageSize
          }
        }
      } catch (error) {
        console.warn('加载表格设置失败:', error)
      }
    },

    /**
     * 判断列是否显示
     */
    isColumnVisible(prop) {
      return this.visibleColumns.includes(prop)
    },

    /**
     * 文件选择处理
     */
    handleFileChange(file, fileList) {
      this.certificationForm.fileList = fileList
    },

    /**
     * 重置认证表单
     */
    resetCertificationForm() {
      this.certificationForm = {
        vendorId: undefined,
        vendorName: '',
        certificationType: '',
        certificationAuthority: '',
        expiryDate: '',
        fileList: [],
        remark: ''
      }
    },

    /**
     * 创建新记录
     */
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 编辑记录
     */
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 查看详情
     */
    handleView(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'view'
      this.dialogFormVisible = true
    },

    /**
     * 重置临时对象
     */
    resetTemp() {
      this.temp = {
        id: undefined,
        vendorCode: '',
        vendorName: '',
        vendorType: '',
        vendorLevel: '',
        certificationStatus: '',
        cooperationStatus: '',
        productModels: '',
        contactInfo: '',
        address: '',
        description: '',
        isEnabled: 1
      }
    },

    /**
     * 创建数据
     */
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await createUkeyVendor(this.temp)
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.message || '创建失败')
            }
          } catch (error) {
            this.$message.error('创建失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 更新数据
     */
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await updateUkeyVendor(this.temp)
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
              this.loadStatistics()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            this.$message.error('更新失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 删除记录
     */
    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteUkeyVendor(row.id)
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.loadStatistics()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败，请检查网络连接')
        }
      })
    },

    /**
     * 厂商认证
     */
    handleCertification() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要认证的厂商')
        return
      }

      this.$confirm('确定要对选中的厂商进行批量认证吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.batchCertification()
      })
    },

    /**
     * 批量认证
     */
    async batchCertification() {
      try {
        this.listLoading = true
        const vendorIds = this.multipleSelection.map(item => item.id)

        // 这里应该调用批量认证API
        // const response = await batchVendorCertification(vendorIds)

        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))

        this.$message.success(`成功为${vendorIds.length}个厂商提交认证申请`)
        this.getList()
        this.loadStatistics()
      } catch (error) {
        console.error('批量认证失败:', error)
        this.$message.error('批量认证失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 单个厂商认证
     */
    handleVendorCertification(row) {
      this.resetCertificationForm()
      this.certificationForm.vendorId = row.id
      this.certificationForm.vendorName = row.vendorName
      this.dialogCertificationVisible = true
    },

    /**
     * 提交认证
     */
    async submitCertification() {
      if (!this.certificationForm.certificationType) {
        this.$message.warning('请选择认证类型')
        return
      }
      if (!this.certificationForm.certificationAuthority) {
        this.$message.warning('请输入认证机构')
        return
      }

      try {
        // 构建认证数据
        const certificationData = {
          vendorId: this.certificationForm.vendorId,
          certificationType: this.certificationForm.certificationType,
          certificationAuthority: this.certificationForm.certificationAuthority,
          expiryDate: this.certificationForm.expiryDate,
          remark: this.certificationForm.remark,
          attachments: this.certificationForm.fileList.map(file => ({
            name: file.name,
            url: file.url || ''
          }))
        }

        // 这里应该调用认证API
        // const response = await submitVendorCertification(certificationData)

        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))

        this.$message.success('认证申请提交成功，请等待审核')
        this.dialogCertificationVisible = false
        this.getList()
        this.loadStatistics()
      } catch (error) {
        console.error('提交认证失败:', error)
        this.$message.error('提交认证失败，请稍后重试')
      }
    },

    /**
     * 导出数据
     */
    async handleExport() {
      try {
        this.listLoading = true
        const params = new URLSearchParams(this.listQuery)
        const response = await fetch(`/qqsk/financial/basicConfig/ukeyVendor/export?${params}`, {
          method: 'GET',
          headers: {
            'Authorization': localStorage.getItem('token') || ''
          }
        })

        if (response.ok) {
          const blob = await response.blob()
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `UKey厂商管理_${new Date().toLocaleDateString()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败，请稍后重试')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 状态变化
     */
    async handleStatusChange(row) {
      try {
        const response = await updateUkeyVendor({
          id: row.id,
          isEnabled: row.isEnabled
        })
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('状态更新成功')
        } else {
          // 回滚状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
          this.$message.error(response.message || '状态更新失败')
        }
      } catch (error) {
        // 回滚状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
        this.$message.error('状态更新失败，请检查网络连接')
      }
    },

    /**
     * 获取厂商图标
     */
    getVendorIcon(type) {
      const iconMap = {
        'HARDWARE': 'el-icon-cpu',
        'SOFTWARE': 'el-icon-monitor',
        'INTEGRATION': 'el-icon-connection',
        'SERVICE': 'el-icon-service'
      }
      return iconMap[type] || 'el-icon-key'
    },

    /**
     * 获取厂商类型名称
     */
    getVendorTypeName(type) {
      const typeMap = {
        'HARDWARE': '硬件厂商',
        'SOFTWARE': '软件厂商',
        'INTEGRATION': '集成厂商',
        'SERVICE': '服务厂商'
      }
      return typeMap[type] || type
    },

    /**
     * 获取厂商类型颜色
     */
    getVendorTypeColor(type) {
      const colorMap = {
        'HARDWARE': 'primary',
        'SOFTWARE': 'success',
        'INTEGRATION': 'warning',
        'SERVICE': 'info'
      }
      return colorMap[type] || 'info'
    },

    /**
     * 获取认证状态名称
     */
    getCertificationStatusName(status) {
      const statusMap = {
        'CERTIFIED': '已认证',
        'PENDING': '待认证',
        'UNCERTIFIED': '未认证'
      }
      return statusMap[status] || status
    },

    /**
     * 获取认证状态颜色
     */
    getCertificationStatusColor(status) {
      const colorMap = {
        'CERTIFIED': 'success',
        'PENDING': 'warning',
        'UNCERTIFIED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    /**
     * 获取合作状态名称
     */
    getCooperationStatusName(status) {
      const statusMap = {
        'ACTIVE': '正常合作',
        'SUSPENDED': '暂停合作',
        'TERMINATED': '终止合作'
      }
      return statusMap[status] || status
    },

    /**
     * 获取合作状态颜色
     */
    getCooperationStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'TERMINATED': 'danger'
      }
      return colorMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.ukey-vendor-manage {
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

  .vendor-overview {
    margin-bottom: 20px;

    .overview-card {
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }

      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;

        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          font-size: 24px;
          color: white;

          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }

          &.certified-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &.active-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &.product-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
        }

        .card-info {
          flex: 1;

          .card-title {
            font-size: 14px;
            color: #606266;
            margin-bottom: 4px;
          }

          .card-value {
            font-size: 28px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .card-change {
            font-size: 12px;
            color: #909399;

            &.positive {
              color: #67c23a;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;

    .search-form {
      .demo-form-inline {
        .el-form-item {
          margin-bottom: 0;
        }
      }
    }
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

    .vendor-name {
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .product-models {
      font-family: monospace;
      color: #606266;
    }

    .contact-info {
      color: #606266;
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-dialog__body {
  padding: 20px 20px 10px 20px;
}

// 表格设置对话框样式
.table-setting-content {
  .el-form-item {
    margin-bottom: 15px;
  }

  .el-checkbox-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .el-checkbox {
    margin-right: 0;
  }
}

// 认证对话框样式
.certification-content {
  .upload-demo {
    .el-upload-dragger {
      width: 100%;
    }
  }
}
</style>
