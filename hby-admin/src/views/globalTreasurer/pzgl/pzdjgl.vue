<template>
  <div class="bill-registration-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document-add"></i>
            票据登记管理
          </h2>
          <p class="page-description">管理企业票据的登记、录入和基础信息维护</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增票据
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleBatchImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 票据概览卡片 -->
    <div class="bill-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总票据数</div>
                <div class="card-value">{{ totalBills }}</div>
                <div class="card-change">张票据</div>
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
                <div class="card-title">有效票据</div>
                <div class="card-value">{{ activeBills }}</div>
                <div class="card-change positive">正常状态</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expiring-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将到期</div>
                <div class="card-value">{{ expiringBills }}</div>
                <div class="card-change warning">30天内</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">票据金额</div>
                <div class="card-value">{{ totalAmount }}</div>
                <div class="card-change">万元</div>
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
          <el-form-item label="票据号码">
            <el-input
              v-model="listQuery.billNumber"
              placeholder="请输入票据号码"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="票据类型">
            <el-select
              v-model="listQuery.billType"
              placeholder="请选择票据类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
              <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
              <el-option label="支票" value="CHECK" />
              <el-option label="本票" value="PROMISSORY_NOTE" />
              <el-option label="电子票据" value="ELECTRONIC_BILL" />
            </el-select>
          </el-form-item>
          <el-form-item label="票据状态">
            <el-select
              v-model="listQuery.billStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="持有" value="HOLDING" />
              <el-option label="已背书" value="ENDORSED" />
              <el-option label="已贴现" value="DISCOUNTED" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已作废" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="出票日期">
            <el-date-picker
              v-model="listQuery.issueDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 票据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="billList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="票据ID" prop="billId" width="80" align="center" />
        <el-table-column label="票据号码" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.billNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getBillTypeTagType(row.billType)" size="mini">
              {{ getBillTypeText(row.billType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="bill-amount">{{ formatCurrency(row.billAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出票人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.drawerName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收款人" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.payeeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="出票日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.issueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getMaturityDateClass(row.maturityDate)">{{ row.maturityDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusTagType(row.billStatus)" size="mini">
              {{ getStatusText(row.billStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="登记人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.registrarName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'endorse', row: row}">背书转让</el-dropdown-item>
                <el-dropdown-item :command="{action: 'discount', row: row}">申请贴现</el-dropdown-item>
                <el-dropdown-item :command="{action: 'cancel', row: row}">作废票据</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑票据对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-tabs v-model="activeFormTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据号码" prop="billNumber">
                  <el-input v-model="temp.billNumber" placeholder="请输入票据号码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据类型" prop="billType">
                  <el-select v-model="temp.billType" placeholder="请选择票据类型" style="width: 100%;">
                    <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                    <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                    <el-option label="支票" value="CHECK" />
                    <el-option label="本票" value="PROMISSORY_NOTE" />
                    <el-option label="电子票据" value="ELECTRONIC_BILL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据金额" prop="billAmount">
                  <el-input-number
                    v-model="temp.billAmount"
                    :precision="2"
                    :min="0"
                    style="width: 100%;"
                    placeholder="请输入票据金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种">
                  <el-select v-model="temp.currency" placeholder="请选择币种" style="width: 100%;">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="出票日期" prop="issueDate">
                  <el-date-picker
                    v-model="temp.issueDate"
                    type="date"
                    placeholder="选择出票日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="temp.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    style="width: 100%;"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="当事人信息" name="parties">
            <el-form-item label="出票人" prop="drawerName">
              <el-input v-model="temp.drawerName" placeholder="请输入出票人名称" />
            </el-form-item>
            <el-form-item label="出票人账号">
              <el-input v-model="temp.drawerAccount" placeholder="请输入出票人账号" />
            </el-form-item>
            <el-form-item label="收款人" prop="payeeName">
              <el-input v-model="temp.payeeName" placeholder="请输入收款人名称" />
            </el-form-item>
            <el-form-item label="收款人账号">
              <el-input v-model="temp.payeeAccount" placeholder="请输入收款人账号" />
            </el-form-item>
            <el-form-item label="承兑人">
              <el-input v-model="temp.acceptorName" placeholder="请输入承兑人名称" />
            </el-form-item>
            <el-form-item label="承兑银行">
              <el-input v-model="temp.acceptingBank" placeholder="请输入承兑银行名称" />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他信息" name="others">
            <el-form-item label="票据用途">
              <el-select v-model="temp.billPurpose" placeholder="请选择票据用途" style="width: 100%;">
                <el-option label="货款支付" value="PAYMENT" />
                <el-option label="服务费用" value="SERVICE_FEE" />
                <el-option label="投资款项" value="INVESTMENT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="票据来源">
              <el-select v-model="temp.billSource" placeholder="请选择票据来源" style="width: 100%;">
                <el-option label="收取" value="RECEIVED" />
                <el-option label="开具" value="ISSUED" />
                <el-option label="背书取得" value="ENDORSED" />
              </el-select>
            </el-form-item>
            <el-form-item label="保管地点">
              <el-input v-model="temp.storageLocation" placeholder="请输入保管地点" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="temp.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 票据详情对话框 -->
    <el-dialog title="票据详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentBill" class="bill-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="票据号码">{{ currentBill.billNumber }}</el-descriptions-item>
          <el-descriptions-item label="票据类型">{{ getBillTypeText(currentBill.billType) }}</el-descriptions-item>
          <el-descriptions-item label="票据金额">{{ formatCurrency(currentBill.billAmount) }}</el-descriptions-item>
          <el-descriptions-item label="币种">{{ currentBill.currency }}</el-descriptions-item>
          <el-descriptions-item label="出票日期">{{ currentBill.issueDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentBill.maturityDate }}</el-descriptions-item>
          <el-descriptions-item label="出票人">{{ currentBill.drawerName }}</el-descriptions-item>
          <el-descriptions-item label="收款人">{{ currentBill.payeeName }}</el-descriptions-item>
          <el-descriptions-item label="承兑人">{{ currentBill.acceptorName }}</el-descriptions-item>
          <el-descriptions-item label="承兑银行">{{ currentBill.acceptingBank }}</el-descriptions-item>
          <el-descriptions-item label="票据状态">
            <el-tag :type="getStatusTagType(currentBill.billStatus)">
              {{ getStatusText(currentBill.billStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="登记人">{{ currentBill.registrarName }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentBill.remark || '无' }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEdit(currentBill)">编辑票据</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入票据" :visible.sync="dialogImportVisible" width="600px">
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImport">确认导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBillRegistrationPage, createBillRegistration, updateBillRegistration } from '@/api/globalTreasurer/pzgl'
import Pagination from '@/components/Pagination'

export default {
  name: 'BillRegistrationManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        billNumber: undefined,
        billType: undefined,
        billStatus: undefined,
        issueDateRange: undefined
      },
      totalBills: 156,
      activeBills: 142,
      expiringBills: 8,
      totalAmount: 25680.5,
      billList: [],
      multipleSelection: [],
      currentBill: null,
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogImportVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      activeFormTab: 'basic',
      temp: {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: 0,
        currency: 'CNY',
        issueDate: null,
        maturityDate: null,
        drawerName: '',
        drawerAccount: '',
        payeeName: '',
        payeeAccount: '',
        acceptorName: '',
        acceptingBank: '',
        billPurpose: '',
        billSource: '',
        storageLocation: '',
        remark: ''
      },
      rules: {
        billNumber: [{ required: true, message: '票据号码不能为空', trigger: 'blur' }],
        billType: [{ required: true, message: '请选择票据类型', trigger: 'change' }],
        billAmount: [{ required: true, message: '请输入票据金额', trigger: 'blur' }],
        issueDate: [{ required: true, message: '请选择出票日期', trigger: 'change' }],
        maturityDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        drawerName: [{ required: true, message: '出票人不能为空', trigger: 'blur' }],
        payeeName: [{ required: true, message: '收款人不能为空', trigger: 'blur' }]
      },
      importFile: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.billList = [
          {
            billId: 1,
            billNumber: 'BA20240925001',
            billType: 'BANK_ACCEPTANCE',
            billAmount: 1000000.00,
            currency: 'CNY',
            drawerName: '示例云科技有限公司',
            payeeName: '供应商A',
            acceptorName: '中国工商银行',
            acceptingBank: '中国工商银行北京分行',
            issueDate: '2024-09-25',
            maturityDate: '2024-12-25',
            billStatus: 'HOLDING',
            registrarName: '张三',
            billPurpose: 'PAYMENT',
            billSource: 'ISSUED',
            storageLocation: '财务部保险柜',
            remark: '货款支付票据'
          },
          {
            billId: 2,
            billNumber: 'CA20240920002',
            billType: 'COMMERCIAL_ACCEPTANCE',
            billAmount: 500000.00,
            currency: 'CNY',
            drawerName: '客户B公司',
            payeeName: '示例云科技有限公司',
            acceptorName: '客户B公司',
            acceptingBank: '',
            issueDate: '2024-09-20',
            maturityDate: '2024-10-20',
            billStatus: 'HOLDING',
            registrarName: '李四',
            billPurpose: 'PAYMENT',
            billSource: 'RECEIVED',
            storageLocation: '财务部保险柜',
            remark: '销售回款票据'
          }
        ]
        this.total = this.billList.length
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        billNumber: undefined,
        billType: undefined,
        billStatus: undefined,
        issueDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogTitle = '新增票据'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑票据'
      this.dialogFormVisible = true
      this.activeFormTab = 'basic'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentBill = row
      this.dialogDetailVisible = true
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'endorse':
          this.$message({
            type: 'info',
            message: '跳转到背书转让页面'
          })
          break
        case 'discount':
          this.$message({
            type: 'info',
            message: '跳转到票据贴现页面'
          })
          break
        case 'cancel':
          this.handleCancelBill(row)
          break
      }
    },
    handleCancelBill(row) {
      this.$confirm('确认作废该票据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.billStatus = 'CANCELLED'
        this.$message({
          type: 'success',
          message: '票据已作废!'
        })
      })
    },
    handleBatchImport() {
      this.dialogImportVisible = true
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '票据数据导出成功'
      })
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    handleImport() {
      if (!this.importFile) {
        this.$message({
          type: 'warning',
          message: '请选择要导入的文件'
        })
        return
      }
      this.dialogImportVisible = false
      this.$message({
        type: 'success',
        message: '票据数据导入成功'
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.billId = parseInt(Math.random() * 100) + 1024
          this.temp.billStatus = 'HOLDING'
          this.temp.registrarName = '当前用户'
          if (this.temp.issueDate) {
            this.temp.issueDate = this.temp.issueDate.toISOString().slice(0, 10)
          }
          if (this.temp.maturityDate) {
            this.temp.maturityDate = this.temp.maturityDate.toISOString().slice(0, 10)
          }
          this.billList.unshift(this.temp)
          this.total = this.billList.length
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '票据登记成功'
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.temp.issueDate && typeof this.temp.issueDate === 'object') {
            this.temp.issueDate = this.temp.issueDate.toISOString().slice(0, 10)
          }
          if (this.temp.maturityDate && typeof this.temp.maturityDate === 'object') {
            this.temp.maturityDate = this.temp.maturityDate.toISOString().slice(0, 10)
          }
          const index = this.billList.findIndex(v => v.billId === this.temp.billId)
          this.billList.splice(index, 1, this.temp)
          this.dialogFormVisible = false
          this.$message({
            type: 'success',
            message: '票据更新成功'
          })
        }
      })
    },
    resetTemp() {
      this.temp = {
        billId: undefined,
        billNumber: '',
        billType: '',
        billAmount: 0,
        currency: 'CNY',
        issueDate: null,
        maturityDate: null,
        drawerName: '',
        drawerAccount: '',
        payeeName: '',
        payeeAccount: '',
        acceptorName: '',
        acceptingBank: '',
        billPurpose: '',
        billSource: '',
        storageLocation: '',
        remark: ''
      }
    },
    getBillTypeTagType(type) {
      const typeMap = {
        'BANK_ACCEPTANCE': 'success',
        'COMMERCIAL_ACCEPTANCE': 'primary',
        'CHECK': 'warning',
        'PROMISSORY_NOTE': 'info',
        'ELECTRONIC_BILL': 'success'
      }
      return typeMap[type] || 'info'
    },
    getBillTypeText(type) {
      const textMap = {
        'BANK_ACCEPTANCE': '银行承兑汇票',
        'COMMERCIAL_ACCEPTANCE': '商业承兑汇票',
        'CHECK': '支票',
        'PROMISSORY_NOTE': '本票',
        'ELECTRONIC_BILL': '电子票据'
      }
      return textMap[type] || type
    },
    getStatusTagType(status) {
      const typeMap = {
        'HOLDING': 'success',
        'ENDORSED': 'primary',
        'DISCOUNTED': 'warning',
        'MATURED': 'info',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'HOLDING': '持有',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'MATURED': '已到期',
        'CANCELLED': '已作废'
      }
      return textMap[status] || status
    },
    getMaturityDateClass(maturityDate) {
      const today = new Date()
      const maturity = new Date(maturityDate)
      const diffDays = Math.ceil((maturity - today) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'expired-date'
      if (diffDays <= 30) return 'expiring-date'
      return ''
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-registration-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .bill-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.expiring-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
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
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .bill-amount {
    font-weight: 600;
    color: #409EFF;
  }

  .expired-date {
    color: #F56C6C;
    font-weight: 600;
  }

  .expiring-date {
    color: #E6A23C;
    font-weight: 600;
  }

  .bill-detail {
    .el-descriptions {
      margin-bottom: 20px;
    }
    h4 {
      margin: 16px 0 8px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
    p {
      margin: 0;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
