<template>
  <div class="registry-list">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">产权登记台账</h2>
        <p class="banner-sub">各级企业产权信息登记 · 工商比对核查 · 批量导入导出</p>
      </div>
    </div>

    <!-- 统计卡（来自后端统计接口） -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="输入企业名称" clearable style="width:180px;"></el-input>
        </el-form-item>
        <el-form-item label="上级企业">
          <el-input v-model="queryForm.parentCompanyName" placeholder="输入上级企业名称" clearable style="width:160px;"></el-input>
        </el-form-item>
        <el-form-item label="股权层级">
          <el-select v-model="queryForm.equityLevel" placeholder="全部" clearable style="width:110px;">
            <el-option v-for="i in 7" :key="i" :label="`第 ${i} 级`" :value="i"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="经营状态">
          <el-select v-model="queryForm.businessStatus" placeholder="全部" clearable style="width:120px;">
            <el-option label="正常经营" value="NORMAL"></el-option>
            <el-option label="连续亏损" value="LOSS"></el-option>
            <el-option label="清算中" value="LIQUIDATION"></el-option>
            <el-option label="已注销" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产权状态">
          <el-select v-model="queryForm.propertyStatus" placeholder="全部" clearable style="width:110px;">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="变动中" value="CHANGING"></el-option>
            <el-option label="冻结" value="FROZEN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工商比对">
          <el-select v-model="queryForm.registrationConsistency" placeholder="全部" clearable style="width:110px;">
            <el-option label="一致" value="CONSISTENT"></el-option>
            <el-option label="差异" value="DIFFERENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否并表">
          <el-select v-model="queryForm.isConsolidated" placeholder="全部" clearable style="width:100px;">
            <el-option label="是" value="Y"></el-option>
            <el-option label="否" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 + 表格 -->
    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="openDialog()">新增登记</el-button>
        <el-button size="small" icon="el-icon-upload2" @click="importDialogVisible = true">批量导入</el-button>
        <el-button size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
        <span class="total-tip">共 <b>{{ total }}</b> 条记录</span>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        size="small"
        border
        :row-class-name="rowClassName"
        @row-click="openDetail"
      >
        <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column label="上级企业" prop="parentCompanyName" min-width="160" show-overflow-tooltip>
          <template slot-scope="{row}">{{ row.parentCompanyName || '-' }}</template>
        </el-table-column>
        <el-table-column label="层级" prop="equityLevel" width="70" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.equityLevel > 5 ? 'danger' : 'primary'" size="mini">第{{ row.equityLevel }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="持股比例(%)" prop="equityRatio" width="110" align="right">
          <template slot-scope="{row}">{{ row.equityRatio != null ? row.equityRatio + '%' : '-' }}</template>
        </el-table-column>
        <el-table-column label="出资额(万元)" prop="investAmount" width="120" align="right">
          <template slot-scope="{row}">{{ row.investAmount != null ? Number(row.investAmount).toLocaleString() : '-' }}</template>
        </el-table-column>
        <el-table-column label="注册资本(万元)" prop="registeredCapital" width="130" align="right">
          <template slot-scope="{row}">{{ row.registeredCapital != null ? Number(row.registeredCapital).toLocaleString() : '-' }}</template>
        </el-table-column>
        <el-table-column label="经营状态" prop="businessStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="businessStatusType(row.businessStatus)" size="mini">{{ businessStatusLabel(row.businessStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="产权状态" prop="propertyStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="propertyStatusType(row.propertyStatus)" size="mini">{{ propertyStatusLabel(row.propertyStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="工商比对" prop="registrationConsistency" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.registrationConsistency === 'CONSISTENT' ? 'success' : 'danger'" size="mini">
              {{ row.registrationConsistency === 'CONSISTENT' ? '一致' : (row.registrationConsistency === 'DIFFERENT' ? '差异' : '-') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="并表" prop="isConsolidated" width="70" align="center">
          <template slot-scope="{row}">
            <span>{{ row.isConsolidated === 'Y' ? '是' : (row.isConsolidated === 'N' ? '否' : '-') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" @click.native.stop>
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click.stop="openDetail(row)">详情</el-button>
            <el-button type="text" size="mini" @click.stop="openDialog(row)">编辑</el-button>
            <el-button type="text" size="mini" style="color:#F5222D;" @click.stop="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-size="queryForm.pageSize"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>


    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="680px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" size="small">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入企业名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级企业">
              <el-input v-model="form.parentCompanyName" placeholder="请输入上级企业名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="股权层级" prop="equityLevel">
              <el-input-number v-model="form.equityLevel" :min="1" :max="10" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="持股比例(%)">
              <el-input-number v-model="form.equityRatio" :min="0" :max="100" :precision="2" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出资额(万元)">
              <el-input-number v-model="form.investAmount" :min="0" :precision="2" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资本(万元)">
              <el-input-number v-model="form.registeredCapital" :min="0" :precision="2" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营状态">
              <el-select v-model="form.businessStatus" style="width:100%;">
                <el-option label="正常经营" value="NORMAL"></el-option>
                <el-option label="连续亏损" value="LOSS"></el-option>
                <el-option label="清算中" value="LIQUIDATION"></el-option>
                <el-option label="已注销" value="CANCELLED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产权状态">
              <el-select v-model="form.propertyStatus" style="width:100%;">
                <el-option label="正常" value="NORMAL"></el-option>
                <el-option label="变动中" value="CHANGING"></el-option>
                <el-option label="冻结" value="FROZEN"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产权类型">
              <el-select v-model="form.rightType" style="width:100%;">
                <el-option label="国有独资" value="SOLE"></el-option>
                <el-option label="国有控股" value="HOLDING"></el-option>
                <el-option label="国有参股" value="PARTICIPATING"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工商比对">
              <el-select v-model="form.registrationConsistency" style="width:100%;">
                <el-option label="一致" value="CONSISTENT"></el-option>
                <el-option label="差异" value="DIFFERENT"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="统一社会信用代码">
              <el-input v-model="form.unifiedCreditCode" placeholder="18位统一社会信用代码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否并表">
              <el-select v-model="form.isConsolidated" style="width:100%;">
                <el-option label="是" value="Y"></el-option>
                <el-option label="否" value="N"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法定代表人">
              <el-input v-model="form.legalRepresentative" placeholder="请输入法定代表人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成立日期">
              <el-date-picker v-model="form.establishDate" type="date" value-format="yyyy-MM-dd" placeholder="选择成立日期" style="width:100%;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="备注信息"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="产权登记详情" :visible.sync="drawerVisible" direction="rtl" size="520px">
      <div class="detail-content" v-if="currentRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="上级企业" :span="2">{{ currentRow.parentCompanyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="股权层级">第 {{ currentRow.equityLevel }} 级</el-descriptions-item>
          <el-descriptions-item label="持股比例">{{ currentRow.equityRatio != null ? currentRow.equityRatio + '%' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="出资额">{{ currentRow.investAmount != null ? Number(currentRow.investAmount).toLocaleString() : '-' }} 万元</el-descriptions-item>
          <el-descriptions-item label="注册资本">{{ currentRow.registeredCapital != null ? Number(currentRow.registeredCapital).toLocaleString() : '-' }} 万元</el-descriptions-item>
          <el-descriptions-item label="经营状态">
            <el-tag :type="businessStatusType(currentRow.businessStatus)" size="mini">{{ businessStatusLabel(currentRow.businessStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="产权状态">
            <el-tag :type="propertyStatusType(currentRow.propertyStatus)" size="mini">{{ propertyStatusLabel(currentRow.propertyStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="产权类型">{{ rightTypeLabel(currentRow.rightType) }}</el-descriptions-item>
          <el-descriptions-item label="工商比对">
            <el-tag :type="currentRow.registrationConsistency === 'CONSISTENT' ? 'success' : 'danger'" size="mini">
              {{ currentRow.registrationConsistency === 'CONSISTENT' ? '一致' : '差异' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否并表">{{ currentRow.isConsolidated === 'Y' ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码" :span="2">{{ currentRow.unifiedCreditCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="法定代表人">{{ currentRow.legalRepresentative || '-' }}</el-descriptions-item>
          <el-descriptions-item label="成立日期">{{ currentRow.establishDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top:16px;text-align:right;">
          <el-button size="small" type="primary" @click="openDialog(currentRow); drawerVisible=false">编辑</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 批量导入弹窗 -->
    <el-dialog title="批量导入产权登记" :visible.sync="importDialogVisible" width="520px" @close="importFile=null">
      <div class="import-tip">
        <el-alert type="info" :closable="false" show-icon>
          <template slot="title">
            请按以下列顺序准备Excel文件（.xlsx/.xls）：<br>
            企业名称、上级企业、股权层级、持股比例(%)、出资额(万元)、注册资本(万元)、<br>
            经营状态(NORMAL/LOSS/LIQUIDATION/CANCELLED)、产权状态(NORMAL/CHANGING/FROZEN)、<br>
            工商比对(CONSISTENT/DIFFERENT)、是否并表(Y/N)、统一社会信用代码、产权类型(SOLE/HOLDING/PARTICIPATING)
          </template>
        </el-alert>
      </div>
      <el-upload
        ref="uploadRef"
        class="upload-area"
        drag
        action="#"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleFileChange"
        :on-remove="() => { importFile = null }"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传 xlsx/xls 文件</div>
      </el-upload>
      <span slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" :disabled="!importFile" @click="submitImport">开始导入</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPropertyList,
  addProperty,
  updateProperty,
  deleteProperty,
  getPropertyStatistics,
  importPropertyData,
  exportPropertyData
} from '@/api/stateAssets/propertyRight'
import { mapGetters } from 'vuex'

export default {
  name: 'PropertyRegistryList',
  data() {
    return {
      loading: false,
      queryForm: {
        companyName: '',
        parentCompanyName: '',
        equityLevel: null,
        businessStatus: '',
        propertyStatus: '',
        registrationConsistency: '',
        isConsolidated: '',
        pageNumber: 1,
        pageSize: 20
      },
      tableData: [],
      total: 0,
      kpiList: [],
      // 新增/编辑弹窗
      dialogVisible: false,
      submitLoading: false,
      isEdit: false,
      form: this.defaultForm(),
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        equityLevel: [{ required: true, message: '请填写股权层级', trigger: 'blur' }]
      },
      // 详情抽屉
      drawerVisible: false,
      currentRow: null,
      // 批量导入
      importDialogVisible: false,
      importLoading: false,
      importFile: null
    }
  },
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
    dialogTitle() { return this.isEdit ? '编辑产权登记' : '新增产权登记' }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
  },
  methods: {
    defaultForm() {
      return {
        propertyId: null,
        companyName: '',
        parentCompanyName: '',
        equityLevel: 2,
        equityRatio: 100,
        investAmount: 0,
        registeredCapital: 0,
        businessStatus: 'NORMAL',
        propertyStatus: 'NORMAL',
        rightType: 'HOLDING',
        registrationConsistency: 'CONSISTENT',
        isConsolidated: 'Y',
        unifiedCreditCode: '',
        legalRepresentative: '',
        establishDate: null,
        remark: ''
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getPropertyList(this.queryForm)
        if (res && res.data) {
          this.tableData = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || res.data.total || this.tableData.length
        } else {
          this.tableData = []
          this.total = 0
        }
      } catch (e) {
        this.tableData = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getPropertyStatistics()
        if (res && res.data) {
          const d = res.data
          this.kpiList = [
            { key: 'total', label: '登记企业总数', value: d.totalCount || 0, color: '#1677FF' },
            { key: 'normal', label: '正常经营', value: d.normalCount || 0, color: '#52C41A' },
            { key: 'loss', label: '亏损企业', value: d.lossCount || 0, color: '#FA8C16' },
            { key: 'cancelled', label: '清算/注销', value: d.cancelledCount || 0, color: '#8C8C8C' }
          ]
        }
      } catch (e) {
        this.kpiList = [
          { key: 'total', label: '登记企业总数', value: 0, color: '#1677FF' },
          { key: 'normal', label: '正常经营', value: 0, color: '#52C41A' },
          { key: 'loss', label: '亏损企业', value: 0, color: '#FA8C16' },
          { key: 'cancelled', label: '清算/注销', value: 0, color: '#8C8C8C' }
        ]
      }
    },
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    resetQuery() {
      this.queryForm = {
        companyName: '',
        parentCompanyName: '',
        equityLevel: null,
        businessStatus: '',
        propertyStatus: '',
        registrationConsistency: '',
        isConsolidated: '',
        pageNumber: 1,
        pageSize: 20
      }
      this.loadData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadData()
    },
    openDialog(row = null) {
      this.isEdit = !!row
      this.form = row ? { ...row } : this.defaultForm()
      this.dialogVisible = true
    },
    openDetail(row) {
      this.currentRow = row
      this.drawerVisible = true
    },
    resetForm() {
      this.$refs.formRef && this.$refs.formRef.resetFields()
      this.form = this.defaultForm()
    },
    async submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          if (this.isEdit) {
            await updateProperty(this.form)
          } else {
            await addProperty(this.form)
          }
          this.$message.success(this.isEdit ? '更新成功' : '新增成功')
          this.dialogVisible = false
          this.loadData()
          this.loadStatistics()
        } catch (e) {
          this.$message.error('操作失败：' + (e.message || '请检查网络'))
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除「${row.companyName}」的产权登记记录？`, '删除确认', { type: 'warning' }).then(async () => {
        try {
          await deleteProperty({ propertyId: row.propertyId })
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    async submitImport() {
      if (!this.importFile) return
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        const res = await importPropertyData(formData)
        if (res && res.code === 1) {
          this.$message.success(res.data || '导入成功')
          this.importDialogVisible = false
          this.importFile = null
          this.$refs.uploadRef && this.$refs.uploadRef.clearFiles()
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error((res && res.msg) || '导入失败')
        }
      } catch (e) {
        this.$message.error('导入失败：' + (e.message || '请检查文件格式'))
      } finally {
        this.importLoading = false
      }
    },
    async handleExport() {
      try {
        const params = {
          companyName: this.queryForm.companyName || undefined,
          businessStatus: this.queryForm.businessStatus || undefined,
          propertyStatus: this.queryForm.propertyStatus || undefined,
          registrationConsistency: this.queryForm.registrationConsistency || undefined
        }
        const res = await exportPropertyData(params)
        // 响应拦截器对blob类型返回完整response对象，实际数据在res.data中
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '产权登记台账.csv'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    rowClassName({ row }) {
      if (row.registrationConsistency === 'DIFFERENT') return 'row-info'
      if (row.businessStatus === 'LOSS' || row.propertyStatus === 'FROZEN') return 'row-warning'
      return ''
    },
    businessStatusLabel(v) { return { NORMAL: '正常经营', LOSS: '连续亏损', LIQUIDATION: '清算中', CANCELLED: '已注销' }[v] || (v || '-') },
    businessStatusType(v) { return { NORMAL: 'success', LOSS: 'warning', LIQUIDATION: 'info', CANCELLED: 'info' }[v] || '' },
    propertyStatusLabel(v) { return { NORMAL: '正常', CHANGING: '变动中', FROZEN: '冻结' }[v] || (v || '-') },
    propertyStatusType(v) { return { NORMAL: 'success', CHANGING: 'warning', FROZEN: 'danger' }[v] || '' },
    rightTypeLabel(v) { return { SOLE: '国有独资', HOLDING: '国有控股', PARTICIPATING: '国有参股' }[v] || (v || '-') }
  }
}
</script>


<style scoped>
.registry-list { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
}
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.search-card { margin-bottom: 12px; }
.table-card { }
.toolbar { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.total-tip { margin-left: auto; font-size: 12px; color: #888; }
.detail-content { padding: 16px; }
.pagination-wrap { margin-top: 12px; text-align: right; }
.import-tip { margin-bottom: 16px; }
.upload-area { width: 100%; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
::v-deep .row-info td { background: #E6F4FF !important; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .el-upload-dragger { width: 100%; }
</style>
