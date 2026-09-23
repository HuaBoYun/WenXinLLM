<template>
  <div class="supplier-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-user"></i>
          供应商管理
        </h1>
        <p class="page-description">管理供应商档案、分类、评估和合作协议信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
          新增供应商
        </el-button>
        <el-button type="success" icon="el-icon-star-on" @click="batchEvaluate">
          批量评估
        </el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="importSuppliers">
          批量导入
        </el-button>
      </div>
    </div>

    <!-- 供应商统计 -->
    <div class="supplier-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalSuppliers }}</div>
              <div class="stat-label">供应商总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.activeSuppliers }}</div>
              <div class="stat-label">活跃供应商</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon amount">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalPayableAmount) }}</div>
              <div class="stat-label">应付总额</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon new">
              <i class="el-icon-plus"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.newSuppliersThisMonth }}</div>
              <div class="stat-label">本月新增</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="供应商编码">
          <el-input v-model="searchForm.supplierCode" placeholder="请输入供应商编码" clearable />
        </el-form-item>
        <el-form-item label="供应商名称">
          <el-input v-model="searchForm.supplierName" placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="供应商分类">
          <el-select v-model="searchForm.supplierCategory" placeholder="请选择分类" clearable>
            <el-option label="原材料供应商" :value="1" />
            <el-option label="设备供应商" :value="2" />
            <el-option label="服务供应商" :value="3" />
            <el-option label="其他供应商" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商状态">
          <el-select v-model="searchForm.supplierStatus" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="暂停" :value="2" />
            <el-option label="黑名单" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="信用等级">
          <el-select v-model="searchForm.creditLevel" placeholder="请选择信用等级" clearable>
            <el-option label="AAA" value="AAA" />
            <el-option label="AA" value="AA" />
            <el-option label="A" value="A" />
            <el-option label="BBB" value="BBB" />
            <el-option label="BB" value="BB" />
            <el-option label="B" value="B" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="supplierId" label="供应商ID" width="120" />
        <el-table-column prop="supplierCode" label="供应商编码" width="150" />
        <el-table-column prop="supplierName" label="供应商名称" width="200" />
        <el-table-column prop="supplierCategoryName" label="供应商分类" width="120" />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="creditLevel" label="信用等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCreditLevelType(scope.row.creditLevel)">
              {{ scope.row.creditLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payableAmount" label="应付余额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.payableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="supplierStatus" label="供应商状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.supplierStatus)">
              {{ getStatusText(scope.row.supplierStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="editSupplier(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="evaluateSupplier(scope.row)">评估</el-button>
            <el-button size="mini" type="info" @click="viewPayableSummary(scope.row)">应付汇总</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 创建/编辑供应商对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="createDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-tabs v-model="activeFormTab">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="供应商编码" prop="supplierCode">
                  <el-input v-model="createForm.supplierCode" placeholder="请输入供应商编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供应商名称" prop="supplierName">
                  <el-input v-model="createForm.supplierName" placeholder="请输入供应商名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="供应商分类" prop="supplierCategory">
                  <el-select v-model="createForm.supplierCategory" placeholder="请选择分类">
                    <el-option label="原材料供应商" :value="1" />
                    <el-option label="设备供应商" :value="2" />
                    <el-option label="服务供应商" :value="3" />
                    <el-option label="其他供应商" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="供应商状态" prop="supplierStatus">
                  <el-select v-model="createForm.supplierStatus" placeholder="请选择状态">
                    <el-option label="正常" :value="1" />
                    <el-option label="暂停" :value="2" />
                    <el-option label="黑名单" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="统一社会信用代码" prop="creditCode">
                  <el-input v-model="createForm.creditCode" placeholder="请输入统一社会信用代码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="法定代表人" prop="legalRepresentative">
                  <el-input v-model="createForm.legalRepresentative" placeholder="请输入法定代表人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="注册地址" prop="registeredAddress">
              <el-input v-model="createForm.registeredAddress" placeholder="请输入注册地址" />
            </el-form-item>
          </el-tab-pane>

          <!-- 联系信息 -->
          <el-tab-pane label="联系信息" name="contact">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contactPerson">
                  <el-input v-model="createForm.contactPerson" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input v-model="createForm.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="联系邮箱" prop="contactEmail">
                  <el-input v-model="createForm.contactEmail" placeholder="请输入联系邮箱" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="传真号码" prop="faxNumber">
                  <el-input v-model="createForm.faxNumber" placeholder="请输入传真号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="通讯地址" prop="contactAddress">
              <el-input v-model="createForm.contactAddress" placeholder="请输入通讯地址" />
            </el-form-item>
          </el-tab-pane>

          <!-- 财务信息 -->
          <el-tab-pane label="财务信息" name="financial">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="开户银行" prop="bankName">
                  <el-input v-model="createForm.bankName" placeholder="请输入开户银行" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="银行账号" prop="bankAccount">
                  <el-input v-model="createForm.bankAccount" placeholder="请输入银行账号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="信用等级" prop="creditLevel">
                  <el-select v-model="createForm.creditLevel" placeholder="请选择信用等级">
                    <el-option label="AAA" value="AAA" />
                    <el-option label="AA" value="AA" />
                    <el-option label="A" value="A" />
                    <el-option label="BBB" value="BBB" />
                    <el-option label="BB" value="BB" />
                    <el-option label="B" value="B" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="信用额度" prop="creditLimit">
                  <el-input-number
                    v-model="createForm.creditLimit"
                    :precision="2"
                    :min="0"
                    placeholder="请输入信用额度"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="付款方式" prop="paymentMethod">
                  <el-select v-model="createForm.paymentMethod" placeholder="请选择付款方式">
                    <el-option label="现金" :value="1" />
                    <el-option label="银行转账" :value="2" />
                    <el-option label="支票" :value="3" />
                    <el-option label="承兑汇票" :value="4" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="账期天数" prop="paymentTerms">
                  <el-input-number
                    v-model="createForm.paymentTerms"
                    :min="0"
                    placeholder="请输入账期天数"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
        
        <el-form-item label="备注">
          <el-input
            v-model="createForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">保存</el-button>
      </div>
    </el-dialog>

    <!-- 供应商评估对话框 -->
    <el-dialog
      title="供应商评估"
      :visible.sync="evaluateDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div class="evaluate-info">
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="info-item">
              <label>供应商编码：</label>
              <span>{{ evaluateForm.supplierCode }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>供应商名称：</label>
              <span>{{ evaluateForm.supplierName }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-form :model="evaluateForm" :rules="evaluateRules" ref="evaluateForm" label-width="120px">
        <el-form-item label="评估类型" prop="evaluateType">
          <el-select v-model="evaluateForm.evaluateType" placeholder="请选择评估类型">
            <el-option label="质量评估" :value="1" />
            <el-option label="交期评估" :value="2" />
            <el-option label="服务评估" :value="3" />
            <el-option label="综合评估" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估分数" prop="evaluateScore">
          <el-rate
            v-model="evaluateForm.evaluateScore"
            :max="5"
            show-score
            text-color="#ff9900"
            score-template="{value} 分"
          />
        </el-form-item>
        <el-form-item label="评估结果" prop="evaluateResult">
          <el-select v-model="evaluateForm.evaluateResult" placeholder="请选择评估结果">
            <el-option label="优秀" :value="1" />
            <el-option label="良好" :value="2" />
            <el-option label="一般" :value="3" />
            <el-option label="较差" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估意见" prop="evaluateComments">
          <el-input
            v-model="evaluateForm.evaluateComments"
            type="textarea"
            :rows="4"
            placeholder="请输入评估意见"
          />
        </el-form-item>
        <el-form-item label="改进建议">
          <el-input
            v-model="evaluateForm.improvementSuggestions"
            type="textarea"
            :rows="3"
            placeholder="请输入改进建议"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="evaluateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEvaluate">保存评估</el-button>
      </div>
    </el-dialog>

    <!-- 供应商详情对话框 -->
    <el-dialog
      title="供应商详情"
      :visible.sync="detailDialogVisible"
      width="70%"
      :close-on-click-modal="false"
    >
      <el-tabs v-if="detailData" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="供应商编码">{{ detailData.supplierCode }}</el-descriptions-item>
            <el-descriptions-item label="供应商名称">{{ detailData.supplierName }}</el-descriptions-item>
            <el-descriptions-item label="供应商分类">{{ detailData.supplierCategoryName }}</el-descriptions-item>
            <el-descriptions-item label="供应商状态">{{ detailData.supplierStatusName }}</el-descriptions-item>
            <el-descriptions-item label="统一社会信用代码">{{ detailData.creditCode }}</el-descriptions-item>
            <el-descriptions-item label="法定代表人">{{ detailData.legalRepresentative }}</el-descriptions-item>
            <el-descriptions-item label="注册地址" :span="2">{{ detailData.registeredAddress }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 联系信息 -->
        <el-tab-pane label="联系信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="联系人">{{ detailData.contactPerson }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ detailData.contactPhone }}</el-descriptions-item>
            <el-descriptions-item label="联系邮箱">{{ detailData.contactEmail }}</el-descriptions-item>
            <el-descriptions-item label="传真号码">{{ detailData.faxNumber }}</el-descriptions-item>
            <el-descriptions-item label="通讯地址" :span="2">{{ detailData.contactAddress }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 银行信息 -->
        <el-tab-pane label="银行信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="开户银行">{{ detailData.bankName }}</el-descriptions-item>
            <el-descriptions-item label="银行账号">{{ detailData.bankAccount }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 信用信息 -->
        <el-tab-pane label="信用信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="信用等级">{{ detailData.creditLevel }}</el-descriptions-item>
            <el-descriptions-item label="信用额度">{{ detailData.creditLimit }}</el-descriptions-item>
            <el-descriptions-item label="付款方式">{{ detailData.paymentMethodName }}</el-descriptions-item>
            <el-descriptions-item label="账期天数">{{ detailData.paymentTerms }}天</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 其他信息 -->
        <el-tab-pane label="其他信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="应付余额">{{ detailData.payableAmount }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ detailData.remarks }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="editSupplier(detailData)">编辑</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSupplierPage,
  saveOrUpdateSupplier,
  getSupplierDetail,
  getSupplierPayableSummary,
  getSupplierCreditInfo
} from '@/api/financialSharing/payables'

export default {
  name: 'SupplierManagementIndex',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      stats: {
        totalSuppliers: 0,
        activeSuppliers: 0,
        totalPayableAmount: 0,
        newSuppliersThisMonth: 0
      },
      searchForm: {
        supplierCode: '',
        supplierName: '',
        supplierCategory: '',
        supplierStatus: '',
        creditLevel: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      createDialogVisible: false,
      activeFormTab: 'basic',
      createForm: {
        supplierCode: '',
        supplierName: '',
        supplierCategory: '',
        supplierStatus: 1,
        creditCode: '',
        legalRepresentative: '',
        registeredAddress: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        faxNumber: '',
        contactAddress: '',
        bankName: '',
        bankAccount: '',
        creditLevel: '',
        creditLimit: 0,
        paymentMethod: '',
        paymentTerms: 0,
        remarks: ''
      },
      createRules: {
        supplierCode: [{ required: true, message: '请输入供应商编码', trigger: 'blur' }],
        supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
        supplierCategory: [{ required: true, message: '请选择供应商分类', trigger: 'change' }],
        supplierStatus: [{ required: true, message: '请选择供应商状态', trigger: 'change' }],
        contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
      },
      evaluateDialogVisible: false,
      evaluateForm: {
        supplierId: '',
        supplierCode: '',
        supplierName: '',
        evaluateType: '',
        evaluateScore: 0,
        evaluateResult: '',
        evaluateComments: '',
        improvementSuggestions: ''
      },
      evaluateRules: {
        evaluateType: [{ required: true, message: '请选择评估类型', trigger: 'change' }],
        evaluateScore: [{ required: true, message: '请选择评估分数', trigger: 'change' }],
        evaluateResult: [{ required: true, message: '请选择评估结果', trigger: 'change' }],
        evaluateComments: [{ required: true, message: '请输入评估意见', trigger: 'blur' }]
      },
      detailDialogVisible: false,
      detailData: {},
      isEdit: false
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑供应商' : '新增供应商'
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getSupplierPage(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.stats = {
        totalSuppliers: 0,
        activeSuppliers: 0,
        totalPayableAmount: 0,
        newSuppliersThisMonth: 0
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getCreditLevelType(level) {
      const types = { 'AAA': 'success', 'AA': 'success', 'A': 'primary', 'BBB': 'warning', 'BB': 'warning', 'B': 'danger' }
      return types[level] || 'info'
    },
    getStatusType(status) {
      const types = { 1: 'success', 2: 'warning', 3: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 1: '正常', 2: '暂停', 3: '黑名单' }
      return texts[status] || '未知'
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = {
        supplierCode: '',
        supplierName: '',
        supplierCategory: '',
        supplierStatus: '',
        creditLevel: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    showCreateDialog() {
      this.isEdit = false
      this.createDialogVisible = true
      this.activeFormTab = 'basic'
      this.createForm = {
        supplierCode: '',
        supplierName: '',
        supplierCategory: '',
        supplierStatus: 1,
        creditCode: '',
        legalRepresentative: '',
        registeredAddress: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        faxNumber: '',
        contactAddress: '',
        bankName: '',
        bankAccount: '',
        creditLevel: '',
        creditLimit: 0,
        paymentMethod: '',
        paymentTerms: 0,
        remarks: ''
      }
    },
    async handleCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdateSupplier(this.createForm)
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '创建成功')
              this.createDialogVisible = false
              this.loadData()
              this.loadStats()
            }
          } catch (error) {
            this.$message.error(this.isEdit ? '修改失败' : '创建失败')
          }
        }
      })
    },
    async viewDetail(row) {
      try {
        this.loading = true
        const response = await getSupplierDetail(row.supplierId)
        if (response.code === 1) {
          // 显示详情对话框
          this.detailDialogVisible = true
          this.detailData = response.data
        } else {
          this.$message.error(response.msg || '查询详情失败')
        }
      } catch (error) {
        console.error('查询供应商详情失败:', error)
        this.$message.error('查询详情失败')
      } finally {
        this.loading = false
      }
    },
    editSupplier(row) {
      // 如果是从详情对话框点击编辑，先关闭详情对话框
      if (this.detailDialogVisible) {
        this.detailDialogVisible = false
      }
      this.isEdit = true
      this.createDialogVisible = true
      this.activeFormTab = 'basic'
      // 只复制需要的字段，避免传递额外的字段到后端
      this.createForm = {
        supplierId: row.supplierId,
        supplierCode: row.supplierCode,
        supplierName: row.supplierName,
        supplierCategory: row.supplierCategory,
        supplierStatus: row.supplierStatus,
        creditCode: row.creditCode,
        legalRepresentative: row.legalRepresentative,
        registeredAddress: row.registeredAddress,
        contactPerson: row.contactPerson,
        contactPhone: row.contactPhone,
        contactEmail: row.contactEmail,
        faxNumber: row.faxNumber,
        contactAddress: row.contactAddress,
        bankName: row.bankName,
        bankAccount: row.bankAccount,
        creditLevel: row.creditLevel,
        creditLimit: row.creditLimit,
        paymentMethod: row.paymentMethod,
        paymentTerms: row.paymentTerms,
        remarks: row.remarks
      }
    },
    evaluateSupplier(row) {
      this.evaluateDialogVisible = true
      this.evaluateForm = {
        supplierId: row.supplierId,
        supplierCode: row.supplierCode,
        supplierName: row.supplierName,
        evaluateType: '',
        evaluateScore: 0,
        evaluateResult: '',
        evaluateComments: '',
        improvementSuggestions: ''
      }
    },
    async handleEvaluate() {
      this.$refs.evaluateForm.validate(async (valid) => {
        if (valid) {
          try {
            // 这里应该调用评估API
            this.$message.success('评估保存成功')
            this.evaluateDialogVisible = false
            this.loadData()
          } catch (error) {
            this.$message.error('评估保存失败')
          }
        }
      })
    },
    async viewPayableSummary(row) {
      try {
        const response = await getSupplierPayableSummary(row.supplierId)
        if (response.code === 1) {
          this.$message.success('应付汇总查询成功')
          // 这里可以显示应付汇总详情
        }
      } catch (error) {
        this.$message.error('应付汇总查询失败')
      }
    },
    async batchEvaluate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要评估的供应商')
        return
      }
      try {
        await this.$confirm(`确认对选中的${this.selectedRows.length}家供应商执行批量评估？`, '确认', { type: 'warning' })
        this.$message.success('批量评估操作成功')
        this.loadData()
        this.loadStats()
      } catch (e) { if (e !== 'cancel') this.$message.error('批量评估失败') }
    },
    importSuppliers() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 导入处理中...`)
        this.loadData()
        this.loadStats()
      }
      input.click()
    }
  }
}
</script>

<style lang="scss" scoped>
.supplier-management-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #e6a23c;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.supplier-stats {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.active {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.amount {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.new {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-area {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .amount-text {
    color: #e6a23c;
    font-weight: 600;
  }
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.evaluate-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;

    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
      min-width: 100px;
    }
  }
}
</style>
