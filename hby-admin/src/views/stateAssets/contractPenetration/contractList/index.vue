<template>
  <div class="contract-list-page">
    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card class="stat-card" shadow="never" :style="{'border-left':'4px solid '+s.color}">
          <div class="stat-inner">
            <div><div class="stat-num" :style="{color:s.color}">{{ s.value }}</div><div class="stat-lbl">{{ s.label }}</div></div>
            <i :class="s.icon" :style="{fontSize:'28px',color:s.color,opacity:.3}"/>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px;">
      <el-form :inline="true" :model="query" size="small">
        <el-form-item label="企业名称"><el-input v-model="query.companyName" placeholder="请输入" clearable style="width:130px;"/></el-form-item>
        <el-form-item label="合同类型">
          <el-select v-model="query.contractType" placeholder="全部" clearable style="width:120px;">
            <el-option v-for="t in typeOptions" :key="t" :label="t" :value="t"/>
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="query.complianceStatus" placeholder="全部" clearable style="width:120px;">
            <el-option label="合规" value="COMPLIANT"/><el-option label="违规" value="VIOLATION"/><el-option label="待审" value="PENDING"/>
          </el-select>
        </el-form-item>
        <el-form-item label="数据来源">
          <el-select v-model="query.dataSource" placeholder="全部" clearable style="width:120px;">
            <el-option label="内部系统" value="INTERNAL"/><el-option label="外部接入" value="EXTERNAL"/>
          </el-select>
        </el-form-item>
        <el-form-item label="是否重大"><el-select v-model="query.isMajor" placeholder="全部" clearable style="width:100px;"><el-option label="是" :value="true"/><el-option label="否" :value="false"/></el-select></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <!-- 操作栏+表格 -->
    <el-card shadow="never">
      <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
        <span style="font-weight:600;">合同台账</span>
        <div>
          <el-button size="small" type="primary" icon="el-icon-plus" @click="handleAdd">新增合同</el-button>
          <el-button size="small" plain icon="el-icon-upload2" @click="handleImport">批量导入</el-button>
          <el-button size="small" plain icon="el-icon-download" @click="handleExport">导出</el-button>
          <el-button size="small" type="success" plain icon="el-icon-refresh" @click="handleSync">数据同步</el-button>
        </div>
      </div>
      <el-table :data="filteredList" border :row-class-name="tableRowClass" @row-click="handleView" style="cursor:pointer;">
        <el-table-column prop="contractNo" label="合同编号" width="145" fixed/>
        <el-table-column prop="contractName" label="合同名称" min-width="160"/>
        <el-table-column prop="contractType" label="合同类型" width="100">
          <template slot-scope="{row}">
            <span class="con-tag" :style="contractTypeTagStyle(row.contractType)">{{ row.contractType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="companyName" label="签订企业" width="130"/>
        <el-table-column prop="counterpartyName" label="合同对方" width="160"/>
        <el-table-column prop="amount" label="金额(万元)" width="110" align="right">
          <template slot-scope="{row}">
            <span :style="{color: row.isMajor?'#FA8C16':'#303133', fontWeight: row.isMajor?'700':'400'}">{{ row.amount.toLocaleString() }}</span>
            <el-tag v-if="row.isMajor" size="mini" type="warning" style="margin-left:4px;">重大</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签订日期" width="105"/>
        <el-table-column prop="expireDate" label="到期日期" width="105">
          <template slot-scope="{row}">
            <span :style="{color: row.expireStatus==='EXPIRED'?'#F5222D':row.expireStatus==='EXPIRING_SOON'?'#FA8C16':'#303133'}">{{ row.expireDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="来源" width="90" align="center">
          <template slot-scope="{row}">
            <span class="con-tag" :style="dataSourceTagStyle(row.dataSource)">{{ row.dataSource==='INTERNAL'?'内部':'外部' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="complianceStatus" label="合规状态" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.complianceStatus==='COMPLIANT'?'success':row.complianceStatus==='VIOLATION'?'danger':'warning'" size="small">{{ row.complianceStatus==='COMPLIANT'?'合规':row.complianceStatus==='VIOLATION'?'违规':'待审' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionProgress" label="履行进度" width="130">
          <template slot-scope="{row}">
            <el-progress :percentage="row.executionProgress" :color="row.executionProgress<60?'#F5222D':row.executionProgress<80?'#FA8C16':'#52C41A'" :stroke-width="8"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button size="mini" type="text" @click.stop="handleView(row)">详情</el-button>
            <el-divider direction="vertical"/>
            <el-button size="mini" type="text" icon="el-icon-time" @click.stop="goLifecycle(row)">生命周期</el-button>
            <el-divider direction="vertical"/>
            <el-button size="mini" type="text" @click.stop="handleEdit(row)">编辑</el-button>
            <el-divider direction="vertical"/>
            <el-button size="mini" type="text" style="color:#F5222D;" @click.stop="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px;text-align:right;">
        <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="pageSize" :current-page="pageNumber" @current-change="val => { pageNumber = val; loadData() }"/>
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="form.id ? '编辑合同' : '新增合同'" :visible.sync="dialogVisible" width="640px">
      <el-form :model="form" label-width="90px" size="small" :rules="rules" ref="formRef">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName"><el-input v-model="form.contractName" placeholder="请输入合同名称"/></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contractType">
              <el-select v-model="form.contractType" style="width:100%;">
                <el-option v-for="t in typeOptions" :key="t" :label="t" :value="t"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签订企业" prop="companyName"><el-input v-model="form.companyName" placeholder="请输入企业名称"/></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同对方" prop="counterpartyName"><el-input v-model="form.counterpartyName" placeholder="请输入对方名称"/></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额(万)" prop="amount"><el-input-number v-model="form.amount" :min="0" style="width:100%;"/></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据来源">
              <el-select v-model="form.dataSource" style="width:100%;">
                <el-option label="内部系统" value="INTERNAL"/><el-option label="外部接入" value="EXTERNAL"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签订日期" prop="signDate"><el-date-picker v-model="form.signDate" type="date" value-format="yyyy-MM-dd" style="width:100%;"/></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="expireDate"><el-date-picker v-model="form.expireDate" type="date" value-format="yyyy-MM-dd" style="width:100%;"/></el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="是否重大合同"><el-switch v-model="form.isMajor" active-text="是" inactive-text="否"/></el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认保存</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情抽屉 -->
    <el-drawer title="合同详情" :visible.sync="detailVisible" size="500px" direction="rtl">
      <div v-if="currentRow" style="padding:0 24px 24px;">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="合同编号">{{ currentRow.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="合同类型"><span class="con-tag" :style="contractTypeTagStyle(currentRow.contractType)">{{ currentRow.contractType }}</span></el-descriptions-item>
          <el-descriptions-item label="合同名称" :span="2">{{ currentRow.contractName }}</el-descriptions-item>
          <el-descriptions-item label="签订企业">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="合同对方">{{ currentRow.counterpartyName }}</el-descriptions-item>
          <el-descriptions-item label="合同金额">{{ currentRow.amount.toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="数据来源"><span class="con-tag" :style="dataSourceTagStyle(currentRow.dataSource)">{{ currentRow.dataSource==='INTERNAL'?'内部系统':'外部接入' }}</span></el-descriptions-item>
          <el-descriptions-item label="签订日期">{{ currentRow.signDate }}</el-descriptions-item>
          <el-descriptions-item label="到期日期"><span :style="{color:currentRow.expireStatus==='EXPIRED'?'#F5222D':currentRow.expireStatus==='EXPIRING_SOON'?'#FA8C16':'#303133'}">{{ currentRow.expireDate }}</span></el-descriptions-item>
          <el-descriptions-item label="合规状态">
            <el-tag :type="currentRow.complianceStatus==='COMPLIANT'?'success':currentRow.complianceStatus==='VIOLATION'?'danger':'warning'" size="small">{{ currentRow.complianceStatus==='COMPLIANT'?'合规':currentRow.complianceStatus==='VIOLATION'?'违规':'待审' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否重大">{{ currentRow.isMajor?'是':'否' }}</el-descriptions-item>
          <el-descriptions-item label="履行进度" :span="2">
            <el-progress :percentage="currentRow.executionProgress" :color="currentRow.executionProgress<60?'#F5222D':currentRow.executionProgress<80?'#FA8C16':'#52C41A'"/>
          </el-descriptions-item>
        </el-descriptions>
        <div style="margin-top:16px;display:flex;gap:10px;">
          <el-button size="small" type="primary" @click="$router.push({ path: '/monitorExecute/Htsmzq', query: { contractNo: currentRow.contractNo } })">查看生命周期</el-button>
          <el-button size="small" @click="$router.push({ path: '/monitorExecute/Htlxjk1', query: { contractNo: currentRow.contractNo } })">履行监控</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 批量导入弹窗 -->
    <el-dialog title="批量导入合同" :visible.sync="importVisible" width="500px">
      <div style="text-align:center;padding:20px 0;">
        <el-upload
          ref="importUpload"
          :auto-upload="false"
          :limit="1"
          accept=".xlsx,.xls"
          :on-change="handleFileChange"
          :on-exceed="() => $message.warning('只能上传一个文件')"
          drag
          action=""
        >
          <i class="el-icon-upload" style="font-size:40px;color:#409EFF;"/>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">仅支持 .xlsx / .xls 格式，表头顺序：合同编号、合同名称、合同类型、签订企业、合同对方、金额(万元)、签订日期、到期日期、数据来源、合规状态、履行进度(%)、是否重大</div>
        </el-upload>
      </div>
      <div slot="footer">
        <el-button @click="importVisible=false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="submitImport">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 生命周期弹窗 -->
    <el-dialog title="合同生命周期" :visible.sync="lifecycleVisible" width="720px" top="5vh">
      <div v-loading="lifecycleLoading">
        <div v-if="lifecycleData" style="padding:0 10px;">
          <!-- 合同基本信息 -->
          <el-descriptions :column="3" border size="small" style="margin-bottom:16px;">
            <el-descriptions-item label="合同编号">{{ lifecycleData.contractNo }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ lifecycleData.contractName }}</el-descriptions-item>
            <el-descriptions-item label="合同类型">{{ lifecycleData.contractType }}</el-descriptions-item>
            <el-descriptions-item label="合同金额">{{ lifecycleData.amount ? Number(lifecycleData.amount).toLocaleString() + '万元' : '—' }}</el-descriptions-item>
            <el-descriptions-item label="已付金额">{{ lifecycleData.paidAmount ? Number(lifecycleData.paidAmount).toLocaleString() + '万元' : '—' }}</el-descriptions-item>
            <el-descriptions-item label="合规状态">
              <el-tag :type="lifecycleData.complianceStatus==='COMPLIANT'?'success':'danger'" size="mini">{{ lifecycleData.complianceStatus==='COMPLIANT'?'合规':'违规' }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <!-- 生命周期时间线 -->
          <div class="lifecycle-title">生命周期节点</div>
          <el-timeline v-if="lifecycleData.nodes && lifecycleData.nodes.length">
            <el-timeline-item
              v-for="(node, idx) in lifecycleData.nodes"
              :key="idx"
              :type="node.type || 'primary'"
              :timestamp="node.time"
              placement="top"
            >
              <el-card shadow="never" class="lifecycle-node-card">
                <div style="display:flex;align-items:center;justify-content:space-between;">
                  <span style="font-weight:600;">{{ node.step }}</span>
                  <el-tag :type="node.statusTagType || 'info'" size="mini">{{ node.statusTag }}</el-tag>
                </div>
                <div v-if="node.remark" style="margin-top:6px;font-size:12px;color:#8c8c8c;">{{ node.remark }}</div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无生命周期数据" :image-size="60"/>
          <!-- 付款信息 -->
          <div v-if="lifecycleData.payment" style="margin-top:12px;">
            <div class="lifecycle-title">付款信息</div>
            <el-row :gutter="16">
              <el-col :span="12">
                <div class="payment-item">
                  <span class="payment-label">已付金额</span>
                  <span class="payment-value" style="color:#52C41A;">{{ lifecycleData.payment.paid ? Number(lifecycleData.payment.paid).toLocaleString() : 0 }}万元</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="payment-item">
                  <span class="payment-label">逾期金额</span>
                  <span class="payment-value" style="color:#F5222D;">{{ lifecycleData.payment.overdue ? Number(lifecycleData.payment.overdue).toLocaleString() : 0 }}万元</span>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <el-empty v-else-if="!lifecycleLoading" description="未找到合同数据" :image-size="80"/>
      </div>
      <div slot="footer">
        <el-button @click="lifecycleVisible=false">关闭</el-button>
        <el-button type="primary" size="small" @click="goLifecyclePage">查看完整生命周期</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContractList, addContract, updateContract, deleteContract, syncInternalContracts, exportContracts, importContracts, getContractStatistics, getContractLifecycle } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'
export default {
  name: 'ContractListMgmt',
  data() {
    return {
      list: [],
      total: 0,
      loading: false,
      pageNumber: 1,
      pageSize: 15,
      query: { companyName: '', contractType: '', complianceStatus: '', dataSource: '', isMajor: '', counterpartyName: '' },
      typeOptions: ['采购合同', '销售合同', '工程合同', '租赁合同', '金融合同', '劳务合同', '服务合同'],
      dialogVisible: false,
      detailVisible: false,
      currentRow: null,
      form: {},
      rules: { contractName: [{ required: true, message: '请输入合同名称' }], contractType: [{ required: true, message: '请选择合同类型' }], companyName: [{ required: true, message: '请输入企业名称' }], counterpartyName: [{ required: true, message: '请输入对方名称' }], amount: [{ required: true, message: '请输入合同金额' }] },
      importVisible: false,
      importLoading: false,
      importFile: null,
      // 生命周期弹窗
      lifecycleVisible: false,
      lifecycleLoading: false,
      lifecycleData: null,
      lifecycleContractNo: '',
      // 统计数据（来自后端全量统计接口）
      statistics: { totalCount: 0, totalAmount: 0, majorCount: 0, complianceRate: 0 },
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
    filteredList() {
      return this.list
    },
    statCards() {
      const s = this.statistics
      const totalAmt = (Number(s.totalAmount) / 10000).toFixed(2)
      return [
        { label: '合同总量', value: s.totalCount, color: '#1677FF', icon: 'el-icon-document' },
        { label: '合同金额(亿)', value: totalAmt, color: '#0050A0', icon: 'el-icon-money' },
        { label: '重大合同', value: s.majorCount, color: '#FA8C16', icon: 'el-icon-star-on' },
        { label: '审批合规率', value: s.complianceRate + '%', color: '#52C41A', icon: 'el-icon-finished' },
      ]
    },
  },
  created() {
    // 支持从其他页面跳转时带入 counterpartyName 查询参数
    if (this.$route.query.counterpartyName) {
      this.query.counterpartyName = this.$route.query.counterpartyName
    }
    this.loadData(); this.loadStatistics()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getContractList({ ...this.query, pageNumber: this.pageNumber, pageSize: this.pageSize })
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.list = []
        }
      } catch (e) {
        this.list = []
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getContractStatistics()
        if (res && res.result === 200 && res.data) {
          this.statistics = res.data
        }
      } catch (e) { /* 统计加载失败不影响主流程 */ }
    },
    resetQuery() { this.query = { companyName: '', contractType: '', complianceStatus: '', dataSource: '', isMajor: '' }; this.pageNumber = 1; this.loadData() },
    tableRowClass({ row }) {
      if (row.complianceStatus === 'VIOLATION') return 'row-violation'
      if (row.expireStatus === 'EXPIRED') return 'row-expired'
      if (row.expireStatus === 'EXPIRING_SOON') return 'row-expiring'
      return ''
    },
    handleAdd() { this.form = { dataSource: 'INTERNAL', isMajor: false }; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.dialogVisible = true },
    handleView(row) { this.currentRow = row; this.detailVisible = true },
    goLifecycle(row) {
      const contractNo = row.contractNo || row.contractCode || ''
      this.lifecycleContractNo = contractNo
      this.lifecycleVisible = true
      this.loadLifecycleData(contractNo)
    },
    async loadLifecycleData(contractNo) {
      this.lifecycleLoading = true
      this.lifecycleData = null
      try {
        const res = await getContractLifecycle(contractNo)
        if (res && res.result === 200 && res.data) {
          this.lifecycleData = res.data
        } else {
          this.$message.error(res.msg || '获取生命周期数据失败')
        }
      } catch (e) {
        this.$message.error('获取生命周期数据失败')
      } finally {
        this.lifecycleLoading = false
      }
    },
    goLifecyclePage() {
      this.lifecycleVisible = false
      this.$router.push({ path: '/monitorExecute/Htsmzq', query: { contractNo: this.lifecycleContractNo } })
    },
    handleDelete(row) {
      this.$confirm('确认删除该合同？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteContract(row.contractId || row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功'); this.loadData(); this.loadStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.formRef.validate(async valid => {
        if (!valid) return
        try {
          const payload = {
            contractName: this.form.contractName,
            contractType: this.form.contractType,
            companyName: this.form.companyName,
            counterpartyName: this.form.counterpartyName,
            contractAmount: this.form.amount,
            dataSource: this.form.dataSource,
            signDate: this.form.signDate,
            expiryDate: this.form.expireDate,
            isMajor: this.form.isMajor ? '1' : '0',
          }
          let res
          if (this.form.contractId) {
            res = await updateContract({ ...payload, contractId: this.form.contractId })
          } else {
            res = await addContract(payload)
          }
          if (res && res.result === 200) {
            this.$message.success('保存成功'); this.dialogVisible = false; this.loadData(); this.loadStatistics()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (e) { this.$message.error('保存失败') }
      })
    },
    contractTypeTagStyle(v) {
      const map = { '采购合同': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' }, '销售合同': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' }, '工程合同': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' }, '租赁合同': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' }, '金融合同': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' }, '劳务合同': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' }, '服务合同': { background: '#F0FFF4', color: '#389E0D', border: '1px solid #95DE64' } }
      return map[v] || {}
    },
    dataSourceTagStyle(v) {
      return v === 'INTERNAL' ? { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' } : { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' }
    },
    // ========== 批量导入 ==========
    handleImport() {
      this.importFile = null
      this.importVisible = true
      this.$nextTick(() => { if (this.$refs.importUpload) this.$refs.importUpload.clearFiles() })
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    async submitImport() {
      if (!this.importFile) {
        this.$message.warning('请先选择要导入的文件')
        return
      }
      this.importLoading = true
      try {
        const res = await importContracts(this.importFile)
        if (res && res.result === 200) {
          const data = res.data
          this.$message.success(`导入完成：成功${data.successCount}条，失败${data.failCount}条`)
          if (data.errors && data.errors.length > 0) {
            this.$notify({ title: '导入提示', message: data.errors.join('\n'), type: 'warning', duration: 8000 })
          }
          this.importVisible = false
          this.loadData(); this.loadStatistics()
        } else {
          this.$message.error(res.msg || '导入失败')
        }
      } catch (e) {
        this.$message.error('导入失败')
      } finally {
        this.importLoading = false
      }
    },
    // ========== 导出 ==========
    async handleExport() {
      try {
        const res = await exportContracts(this.query)
        // 响应拦截器对blob类型返回完整axios response对象 { data: Blob, status, headers }
        const blob = res instanceof Blob ? res : (res && res.data instanceof Blob ? res.data : null)
        if (blob) {
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '合同台账_' + new Date().toISOString().slice(0, 10) + '.xls'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败')
        }
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    // ========== 数据同步 ==========
    handleSync() {
      this.$confirm('确认同步内部合同数据？', '数据同步', { type: 'info' }).then(async () => {
        const loading = this.$loading({ text: '正在同步数据...' })
        try {
          const res = await syncInternalContracts()
          if (res && res.result === 200) {
            this.$message.success(`同步完成，共同步${res.data.syncCount}条记录`)
            this.loadData(); this.loadStatistics()
          } else {
            this.$message.error(res.msg || '同步失败')
          }
        } catch (e) {
          this.$message.error('同步失败')
        } finally {
          loading.close()
        }
      }).catch(() => {})
    },
  },
}
</script>

<style lang="scss" scoped>
.contract-list-page { padding: 16px; background: #f5f7fa; min-height: 100%; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; justify-content: space-between; padding: 4px 0; }
.stat-num { font-size: 26px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: #8c8c8c; margin-top: 4px; }
.con-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
::v-deep .row-violation td { background: #FFF1F0 !important; }
::v-deep .row-expired td { background: #FFF1F0 !important; }
::v-deep .row-expiring td { background: #FFFBE6 !important; }
.lifecycle-title { font-weight: 600; font-size: 14px; margin-bottom: 12px; padding-left: 8px; border-left: 3px solid; border-color: var(--theme-color, #1677FF); }
.lifecycle-node-card { padding: 8px 12px; }
.payment-item { background: #f9f9f9; border-radius: 6px; padding: 12px 16px; text-align: center; }
.payment-label { display: block; font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.payment-value { font-size: 18px; font-weight: 600; }
</style>
