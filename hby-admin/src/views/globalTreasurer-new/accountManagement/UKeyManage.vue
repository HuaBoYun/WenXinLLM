<template>
  <div class="ukey-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-cpu"></i>
            U盾信息管理
          </h2>
          <p class="page-description">管理银企直联U盾设备信息，包括U盾绑定、状态监控、证书管理和安全配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增U盾
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleBatchSync">
            批量同步
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- U盾统计卡片 -->
    <div class="ukey-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总U盾数</div>
                <div class="card-value">{{ totalUKeys }}</div>
                <div class="card-change">个设备</div>
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
                <div class="card-title">正常使用</div>
                <div class="card-value">{{ activeUKeys }}</div>
                <div class="card-change positive">{{ activeRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon expired-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">即将过期</div>
                <div class="card-value">{{ expiringUKeys }}</div>
                <div class="card-change warning">30天内</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon blocked-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">已锁定</div>
                <div class="card-value">{{ blockedUKeys }}</div>
                <div class="card-change negative">需处理</div>
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
          <el-form-item label="U盾ID">
            <el-input
              v-model="listQuery.ukeyId"
              placeholder="请输入U盾ID"
              style="width: 120px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="序列号">
            <el-input
              v-model="listQuery.ukeyNo"
              placeholder="请输入序列号"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="账户号码">
            <el-input
              v-model="listQuery.accountNumber"
              placeholder="请输入账户号码"
              style="width: 180px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="U盾状态">
            <el-select
              v-model="listQuery.ukeyStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="ACTIVE" />
              <el-option label="锁定" value="LOCKED" />
              <el-option label="过期" value="EXPIRED" />
              <el-option label="停用" value="DISABLED" />
            </el-select>
          </el-form-item>
          <el-form-item label="绑定日期">
            <el-date-picker
              v-model="listQuery.bindDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button v-waves class="filter-item" type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- U盾表格 -->
    <el-card class="table-card" shadow="never">
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
        <el-table-column label="U盾ID" prop="ukeyId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.ukeyId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="序列号" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="serial-number">{{ row.ukeyNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户号码" width="180px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewAccount(row)">{{ row.accountNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="账户名称" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.accountName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="U盾类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getUKeyTypeTagType(row.ukeyType)" size="mini">
              {{ getUKeyTypeText(row.ukeyType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="U盾状态" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="getUKeyStatusTagType(row.ukeyStatus)">
              {{ getUKeyStatusText(row.ukeyStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="绑定日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.effectiveDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span :class="getExpireDateClass(row.expiryDate)">{{ row.expiryDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="最后使用" width="150px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.lastUsedTime || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="使用人" width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.holderName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看
            </el-button>
            <el-button v-if="row.ukeyStatus === 'ACTIVE'" size="mini" type="warning" @click="handleLock(row)">
              锁定
            </el-button>
            <el-button v-if="row.ukeyStatus === 'LOCKED'" size="mini" type="success" @click="handleUnlock(row)">
              解锁
            </el-button>
            <el-button size="mini" type="info" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑U盾对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 100%; padding: 0 20px;">
        <el-form-item label="选择账户" prop="accountId">
          <el-select v-model="temp.accountId" placeholder="请选择要绑定的账户" style="width: 100%;" @change="handleAccountChange">
            <el-option
              v-for="account in availableAccounts"
              :key="account.accountId"
              :label="`${account.accountNumber} - ${account.accountName}`"
              :value="account.accountId"
            />
          </el-select>
        </el-form-item>
        <el-row :gutter="20" v-if="temp.accountId">
          <el-col :span="12">
            <el-form-item label="账户号码">
              <el-input v-model="temp.accountNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户名称">
              <el-input v-model="temp.accountName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="序列号" prop="ukeyNo">
              <el-input v-model="temp.ukeyNo" placeholder="请输入U盾序列号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="U盾类型" prop="ukeyType">
              <el-select v-model="temp.ukeyType" placeholder="请选择U盾类型" style="width: 100%;">
                <el-option label="工行U盾" value="ICBC" />
                <el-option label="建行U盾" value="CCB" />
                <el-option label="农行U盾" value="ABC" />
                <el-option label="中行U盾" value="BOC" />
                <el-option label="招行U盾" value="CMB" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="U盾状态" prop="ukeyStatus">
              <el-select v-model="temp.ukeyStatus" placeholder="请选择U盾状态" style="width: 100%;">
                <el-option label="正常" value="ACTIVE" />
                <el-option label="锁定" value="LOCKED" />
                <el-option label="停用" value="DISABLED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="绑定日期" prop="effectiveDate">
              <el-date-picker
                v-model="temp.effectiveDate"
                type="date"
                placeholder="选择绑定日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expiryDate">
              <el-date-picker
                v-model="temp.expiryDate"
                type="date"
                placeholder="选择到期日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="使用人" prop="holderName">
              <el-input v-model="temp.holderName" placeholder="请输入使用人姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="temp.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号">
              <el-input v-model="temp.holderIdCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="证书路径">
          <el-input v-model="temp.certificatePath" placeholder="请输入证书文件路径" />
        </el-form-item>
        <el-form-item label="设备描述">
          <el-input v-model="temp.deviceDescription" type="textarea" :rows="2" placeholder="请输入设备描述信息" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
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

    <!-- U盾详情对话框 -->
    <el-dialog title="U盾详情" :visible.sync="dialogDetailVisible" width="800px">
      <div v-if="currentUKey" class="ukey-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="U盾ID">{{ currentUKey.ukeyId }}</el-descriptions-item>
          <el-descriptions-item label="U盾状态">
            <el-tag :type="getUKeyStatusTagType(currentUKey.ukeyStatus)">
              {{ getUKeyStatusText(currentUKey.ukeyStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="序列号">{{ currentUKey.ukeyNo }}</el-descriptions-item>
          <el-descriptions-item label="U盾类型">{{ getUKeyTypeText(currentUKey.ukeyType) }}</el-descriptions-item>
          <el-descriptions-item label="账户号码">{{ currentUKey.accountNumber }}</el-descriptions-item>
          <el-descriptions-item label="账户名称">{{ currentUKey.accountName }}</el-descriptions-item>
          <el-descriptions-item label="绑定日期">{{ currentUKey.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ currentUKey.expiryDate }}</el-descriptions-item>
          <el-descriptions-item label="使用人">{{ currentUKey.holderName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentUKey.contactPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ currentUKey.holderIdCard || '-' }}</el-descriptions-item>
          <el-descriptions-item label="最后使用时间">{{ currentUKey.lastUsedTime || '未使用' }}</el-descriptions-item>
          <el-descriptions-item label="证书路径">{{ currentUKey.certificatePath || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentUKey.createTime }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentUKey.deviceDescription" style="margin-top: 20px;">
          <h4>设备描述</h4>
          <p>{{ currentUKey.deviceDescription }}</p>
        </div>
        <div v-if="currentUKey.remark" style="margin-top: 20px;">
          <h4>备注信息</h4>
          <p>{{ currentUKey.remark }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentUKey && currentUKey.ukeyStatus === 'ACTIVE'" type="warning" @click="handleLock(currentUKey)">
          锁定U盾
        </el-button>
        <el-button v-if="currentUKey && currentUKey.ukeyStatus === 'LOCKED'" type="success" @click="handleUnlock(currentUKey)">
          解锁U盾
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUKeyPage, getUKeyStatusCount, createUKey, updateUKey, lockUKey, unlockUKey, deleteUKey, batchDeleteUKeys, exportUKeyInfo } from '@/api/globalTreasurer/zhgl'
import { getAccountInfoPage } from '@/api/globalTreasurer/zhgl'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'UKeyManage',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        ukeyId: undefined,
        ukeyNo: undefined,
        accountNumber: undefined,
        ukeyStatus: undefined,
        bindDateRange: undefined
      },
      totalUKeys: 0,
      activeUKeys: 0,
      expiringUKeys: 0,
      blockedUKeys: 0,
      activeRate: 0,
      multipleSelection: [],
      availableAccounts: [],
      temp: {
        ukeyId: undefined,
        accountId: undefined,
        accountName: '',
        accountNumber: '',
        ukeyNo: '',
        ukeyType: '',
        ukeyStatus: 'ACTIVE',
        effectiveDate: null,
        expiryDate: null,
        holderName: '',
        contactPhone: '',
        holderIdCard: '',
        certificatePath: '',
        deviceDescription: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogDetailVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑U盾信息',
        create: '新增U盾信息'
      },
      currentUKey: null,
      rules: {
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
        ukeyNo: [{ required: true, message: '请输入序列号', trigger: 'blur' }],
        ukeyType: [{ required: true, message: '请选择U盾类型', trigger: 'change' }],
        ukeyStatus: [{ required: true, message: '请选择U盾状态', trigger: 'change' }],
        effectiveDate: [{ required: true, message: '请选择绑定日期', trigger: 'change' }],
        expiryDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        holderName: [{ required: true, message: '请输入使用人姓名', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
    this.loadAvailableAccounts()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getUKeyPage(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          const data = response.data
          this.list = data.records || data.tlist || data.list || []
          this.total = data.total || data.totalRecord || this.list.length
        } else {
          this.list = []
          this.total = 0
          this.$message.error(response.message || '获取数据失败')
        }
      } catch (error) {
        console.error('获取U盾列表失败:', error)
        this.list = []
        this.total = 0
        this.$message.error('获取数据失败，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    async updateStatistics() {
      try {
        const response = await getUKeyStatusCount()
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code) && response.data) {
          const data = response.data
          this.totalUKeys = Number(data.totalUKeys) || 0
          this.activeUKeys = Number(data.activeUKeys) || 0
          this.expiringUKeys = Number(data.expiringUKeys) || 0
          this.blockedUKeys = Number(data.blockedUKeys) || 0
          this.activeRate = this.totalUKeys > 0 ? Math.round((this.activeUKeys / this.totalUKeys) * 100) : 0
        }
      } catch (error) {
        console.error('获取U盾统计失败:', error)
      }
    },
    async loadAvailableAccounts() {
      try {
        const response = await getAccountInfoPage({ pageNum: 1, pageSize: 1000 })
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          const data = response.data
          this.availableAccounts = data.records || data.tlist || data.list || []
        } else {
          this.availableAccounts = []
        }
      } catch (error) {
        console.error('获取可用账户失败:', error)
        this.availableAccounts = []
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        ukeyId: undefined,
        ukeyNo: undefined,
        accountNumber: undefined,
        ukeyStatus: undefined,
        bindDateRange: undefined
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
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
      this.temp = Object.assign({}, row)
      this.temp.effectiveDate = row.effectiveDate ? new Date(row.effectiveDate) : null
      this.temp.expiryDate = row.expiryDate ? new Date(row.expiryDate) : null
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleViewDetail(row) {
      this.currentUKey = row
      this.dialogDetailVisible = true
    },
    handleViewAccount(row) {
      this.$message.info(`账户：${row.accountName || row.accountNumber || ''}`)
    },
    handleAccountChange(accountId) {
      const account = this.availableAccounts.find(acc => acc.accountId === accountId)
      if (account) {
        this.temp.accountNumber = account.accountNumber
        this.temp.accountName = account.accountName
      }
    },
    handleLock(row) {
      this.$confirm('确认锁定该U盾设备?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await lockUKey(row.ukeyId)
          if (response && response.code === 1) {
            row.ukeyStatus = 'LOCKED'
            this.$message({ type: 'success', message: 'U盾已锁定!' })
            this.updateStatistics()
          } else {
            this.$message.error(response.msg || '锁定失败')
          }
        } catch (error) {
          console.error('锁定失败:', error)
          this.$message.error('锁定失败，请稍后重试')
        }
      })
    },
    handleUnlock(row) {
      this.$confirm('确认解锁该U盾设备?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await unlockUKey(row.ukeyId)
          if (response && response.code === 1) {
            row.ukeyStatus = 'ACTIVE'
            this.$message({ type: 'success', message: 'U盾已解锁!' })
            this.updateStatistics()
          } else {
            this.$message.error(response.msg || '解锁失败')
          }
        } catch (error) {
          console.error('解锁失败:', error)
          this.$message.error('解锁失败，请稍后重试')
        }
      })
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('确认删除该U盾信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteUKey(row.ukeyId)
        if (response && response.code === 1) {
          this.$message.success('删除成功!')
          this.getList()
          this.updateStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    handleBatchSync() {
      if (this.multipleSelection.length === 0) {
        this.$message({ type: 'warning', message: '请选择要批量删除的U盾' })
        return
      }
      this.$confirm(`确认批量删除选中的${this.multipleSelection.length}个U盾信息?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const ukeyIds = this.multipleSelection.map(item => item.ukeyId)
          const response = await batchDeleteUKeys(ukeyIds)
          if (response && response.code === 1) {
            this.$message({ type: 'success', message: '批量删除成功!' })
            this.getList()
            this.updateStatistics()
          } else {
            this.$message.error(response.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败，请稍后重试')
        }
      })
    },
    async handleExport() {
      try {
        this.$message.info('正在导出数据...')
        const params = {
          ukeyNo: this.listQuery.ukeyNo,
          accountNumber: this.listQuery.accountNumber,
          ukeyStatus: this.listQuery.ukeyStatus
        }
        const response = await exportUKeyInfo(params)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = 'U盾信息列表.xlsx'
        a.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    async createData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await createUKey(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('U盾信息创建成功')
          this.getList()
          this.updateStatistics()
        } else {
          this.$message.error(response.msg || '创建失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('创建失败:', error)
          this.$message.error('创建失败，请稍后重试')
        }
      }
    },
    async updateData() {
      try {
        await this.$refs['dataForm'].validate()
        const response = await updateUKey(this.temp)
        if (response && response.code === 1) {
          this.dialogFormVisible = false
          this.$message.success('U盾信息更新成功')
          this.getList()
          this.updateStatistics()
        } else {
          this.$message.error(response.msg || '更新失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('更新失败:', error)
          this.$message.error('更新失败，请稍后重试')
        }
      }
    },
    resetTemp() {
      this.temp = {
        ukeyId: undefined,
        accountId: undefined,
        accountName: '',
        accountNumber: '',
        ukeyNo: '',
        ukeyType: '',
        ukeyStatus: 'ACTIVE',
        effectiveDate: null,
        expiryDate: null,
        holderName: '',
        contactPhone: '',
        holderIdCard: '',
        certificatePath: '',
        deviceDescription: '',
        remark: ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'ukeyId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.ukeyId - b.ukeyId)
      } else {
        this.list.sort((a, b) => b.ukeyId - a.ukeyId)
      }
    },
    getUKeyTypeTagType(ukeyType) {
      const typeMap = {
        'ICBC': 'primary',
        'CCB': 'success',
        'ABC': 'warning',
        'BOC': 'info',
        'CMB': 'danger',
        'OTHER': 'default'
      }
      return typeMap[ukeyType] || 'default'
    },
    getUKeyTypeText(ukeyType) {
      const textMap = {
        'ICBC': '工行U盾',
        'CCB': '建行U盾',
        'ABC': '农行U盾',
        'BOC': '中行U盾',
        'CMB': '招行U盾',
        'OTHER': '其他'
      }
      return textMap[ukeyType] || ukeyType
    },
    getUKeyStatusTagType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'LOCKED': 'danger',
        'EXPIRED': 'warning',
        'DISABLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getUKeyStatusText(status) {
      const textMap = {
        'ACTIVE': '正常',
        'LOCKED': '锁定',
        'EXPIRED': '过期',
        'DISABLED': '停用'
      }
      return textMap[status] || status
    },
    getExpireDateClass(expireDate) {
      if (!expireDate) return ''
      const today = new Date()
      const expire = new Date(expireDate)
      const diffTime = expire - today
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) {
        return 'expired-date'
      } else if (diffDays <= 30) {
        return 'expiring-date'
      } else {
        return 'normal-date'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.ukey-manage {
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
            color: #67C23A;
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

  .ukey-overview {
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
          &.expired-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.blocked-icon {
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
            &.negative {
              color: #F56C6C;
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

  .serial-number {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #606266;
  }

  .expired-date {
    color: #F56C6C;
    font-weight: 600;
  }

  .expiring-date {
    color: #E6A23C;
    font-weight: 600;
  }

  .normal-date {
    color: #67C23A;
  }

  .ukey-detail {
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
