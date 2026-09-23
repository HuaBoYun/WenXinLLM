<template>
  <div class="change-record">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">产权变动登记</h2>
        <p class="banner-sub">全程记录产权变动事项 · 变动类型分类管理 · 审批状态跟踪</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 变动类型说明 -->
    <div class="type-tags">
      <span>变动类型：</span>
      <el-tag v-for="t in changeTypes" :key="t.value" :color="t.bg" :style="{ color: t.color, border: 'none', marginRight: '8px' }" size="small">{{ t.label }}</el-tag>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="输入企业名称" clearable style="width:180px;"></el-input>
        </el-form-item>
        <el-form-item label="变动类型">
          <el-select v-model="queryForm.changeType" placeholder="全部" clearable style="width:130px;">
            <el-option v-for="t in changeTypes" :key="t.value" :label="t.label" :value="t.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="queryForm.approvalStatus" placeholder="全部" clearable style="width:120px;">
            <el-option label="待审批" value="PENDING"></el-option>
            <el-option label="已审批" value="APPROVED"></el-option>
            <el-option label="已拒绝" value="REJECTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="table-card" shadow="never">
      <div class="toolbar">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="openDialog()">新增变动登记</el-button>
        <span class="total-tip">共 <b>{{ totalRecord }}</b> 条记录</span>
      </div>
      <el-table :data="tableData" size="small" border :row-class-name="rowClass" v-loading="loading">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column label="变动类型" prop="changeType" width="110" align="center">
          <template slot-scope="{row}">
            <el-tag :type="changeTypeTag(row.changeType)" size="mini">{{ changeTypeLabel(row.changeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="变动原因" prop="changeReason" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column label="变动前持股%" prop="beforeRatio" width="110" align="right">
          <template slot-scope="{row}">{{ row.beforeRatio != null ? row.beforeRatio + '%' : '-' }}</template>
        </el-table-column>
        <el-table-column label="变动后持股%" prop="afterRatio" width="110" align="right">
          <template slot-scope="{row}">{{ row.afterRatio != null ? row.afterRatio + '%' : '-' }}</template>
        </el-table-column>
        <el-table-column label="变动金额(万元)" prop="changeAmount" width="130" align="right">
          <template slot-scope="{row}">
            <span v-if="row.changeAmount != null" :style="{ color: row.changeAmount > 0 ? '#52C41A' : row.changeAmount < 0 ? '#F5222D' : '#8C8C8C' }">
              {{ row.changeAmount > 0 ? '+' : '' }}{{ Number(row.changeAmount).toLocaleString() }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="审批状态" prop="approvalStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="approvalTagType(row.approvalStatus)" size="mini">{{ approvalLabel(row.approvalStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="变动日期" prop="changeDate" width="110"></el-table-column>
        <el-table-column label="备注" prop="remark" min-width="140" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="showTimeline(row)">时间轴</el-button>
            <el-button type="text" size="mini" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" size="mini" style="color:#F56C6C;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        style="margin-top:12px; text-align:right;"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalRecord"
        :page-size.sync="queryForm.pageSize"
        :current-page.sync="queryForm.pageNumber"
        :page-sizes="[10, 15, 30, 50]"
        @size-change="loadData"
        @current-change="loadData"
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="isEdit ? '编辑变动登记' : '新增变动登记'" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" ref="formRef" label-width="120px" size="small" :rules="formRules">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入企业名称"></el-input>
        </el-form-item>
        <el-form-item label="变动类型" prop="changeType">
          <el-select v-model="form.changeType" style="width:100%;" placeholder="请选择变动类型">
            <el-option v-for="t in changeTypes" :key="t.value" :label="t.label" :value="t.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="变动原因" prop="changeReason">
          <el-input v-model="form.changeReason" placeholder="请输入变动原因"></el-input>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="变动前持股(%)">
              <el-input-number v-model="form.beforeRatio" :min="0" :max="100" :precision="2" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变动后持股(%)">
              <el-input-number v-model="form.afterRatio" :min="0" :max="100" :precision="2" style="width:100%;"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="变动金额(万元)">
          <el-input-number v-model="form.changeAmount" :precision="2" style="width:100%;"></el-input-number>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="变动日期" prop="changeDate">
              <el-date-picker v-model="form.changeDate" type="date" value-format="yyyy-MM-dd" style="width:100%;" placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审批状态">
              <el-select v-model="form.approvalStatus" style="width:100%;">
                <el-option label="待审批" value="PENDING"></el-option>
                <el-option label="已审批" value="APPROVED"></el-option>
                <el-option label="已拒绝" value="REJECTED"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否重大变动">
          <el-radio-group v-model="form.isMajorChange">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">保存</el-button>
      </span>
    </el-dialog>

    <!-- 时间轴抽屉 -->
    <el-drawer title="企业产权变动历史" :visible.sync="timelineDrawer" direction="rtl" size="440px">
      <div class="timeline-wrap" v-if="currentRow">
        <div class="tl-company">{{ currentRow.companyName }}</div>
        <el-timeline v-if="timelineData.length">
          <el-timeline-item
            v-for="item in timelineData"
            :key="item.changeId"
            :timestamp="item.changeDate"
            :type="changeTypeColor(item.changeType)"
            placement="top"
          >
            <el-card size="small" shadow="never" class="tl-card">
              <div class="tl-title">
                <el-tag :type="changeTypeTag(item.changeType)" size="mini">{{ changeTypeLabel(item.changeType) }}</el-tag>
                <span class="tl-amount" :style="{ color: item.changeAmount > 0 ? '#52C41A' : item.changeAmount < 0 ? '#F5222D' : '#888' }">
                  {{ item.changeAmount > 0 ? '+' : '' }}{{ item.changeAmount || 0 }} 万元
                </span>
              </div>
              <div class="tl-ratio">持股比例：{{ item.beforeRatio || 0 }}% → {{ item.afterRatio || 0 }}%</div>
              <div class="tl-remark" v-if="item.changeReason">原因：{{ item.changeReason }}</div>
              <div class="tl-remark" v-if="item.remark">{{ item.remark }}</div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无变动历史"></el-empty>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getChangeList, saveChange, deleteChange, getChangeTimeline } from '@/api/stateAssets/propertyRight'
import { mapGetters } from 'vuex'

export default {
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
  },
  name: 'PropertyChangeRecord',
  data() {
    return {
      loading: false,
      submitLoading: false,
      queryForm: { companyName: '', changeType: '', approvalStatus: '', pageNumber: 1, pageSize: 15 },
      tableData: [],
      totalRecord: 0,
      kpiList: [],
      dialogVisible: false,
      timelineDrawer: false,
      currentRow: null,
      timelineData: [],
      isEdit: false,
      form: this.getDefaultForm(),
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        changeType: [{ required: true, message: '请选择变动类型', trigger: 'change' }],
        changeDate: [{ required: true, message: '请选择变动日期', trigger: 'change' }],
      },
      changeTypes: [
        { value: 'INCREASE', label: '增资', bg: '#F6FFED', color: '#52C41A' },
        { value: 'DECREASE', label: '减资', bg: '#FFF7E6', color: '#FA8C16' },
        { value: 'TRANSFER', label: '产权转让', bg: '#FFF1F0', color: '#F5222D' },
        { value: 'FREE_TRANSFER', label: '无偿划转', bg: '#E6F4FF', color: '#1677FF' },
        { value: 'MERGE', label: '合并', bg: '#F5F5F5', color: '#8C8C8C' },
        { value: 'SPLIT', label: '分立', bg: '#F9F0FF', color: '#722ED1' },
      ],
    }
  },
  mounted() { this.loadData() },
  methods: {
    getDefaultForm() {
      return {
        changeId: '',
        companyName: '',
        changeType: 'TRANSFER',
        changeReason: '',
        beforeRatio: 0,
        afterRatio: 0,
        changeAmount: 0,
        approvalStatus: 'PENDING',
        changeDate: '',
        isMajorChange: '0',
        remark: '',
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getChangeList(this.queryForm)
        if (res && res.data) {
          this.tableData = res.data.tlist || res.data.list || []
          this.totalRecord = res.data.totalRecord || this.tableData.length
        } else {
          this.tableData = []
          this.totalRecord = 0
        }
      } catch (e) {
        console.error('加载数据失败', e)
        this.tableData = []
        this.totalRecord = 0
      }
      this.loading = false
      this.buildKpi()
    },
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    buildKpi() {
      const d = this.tableData
      this.kpiList = [
        { key: 'total', label: '总记录数', value: this.totalRecord, color: '#1677FF' },
        { key: 'pending', label: '待审批', value: d.filter(r => r.approvalStatus === 'PENDING').length, color: '#FA8C16' },
        { key: 'approved', label: '已完成', value: d.filter(r => r.approvalStatus === 'APPROVED').length, color: '#52C41A' },
        { key: 'warning', label: '异常标记', value: d.filter(r => r.isWarning && r.isWarning !== 0).length, color: '#F5222D' },
      ]
    },
    resetQuery() {
      this.queryForm = { companyName: '', changeType: '', approvalStatus: '', pageNumber: 1, pageSize: 15 }
      this.loadData()
    },
    openDialog(row = null) {
      this.isEdit = !!row
      if (row) {
        this.form = { ...row }
      } else {
        this.form = this.getDefaultForm()
      }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    async submitForm() {
      try {
        await this.$refs.formRef.validate()
      } catch (e) {
        return
      }
      this.submitLoading = true
      try {
        const res = await saveChange(this.form)
        if (res && res.result === 200) {
          this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error((res && res.msg) || '保存失败')
        }
      } catch (e) {
        this.$message.error('保存失败：网络异常')
      }
      this.submitLoading = false
    },
    handleDelete(row) {
      this.$confirm('确定删除该变动记录？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        try {
          const res = await deleteChange({ changeId: row.changeId })
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error((res && res.msg) || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败：网络异常')
        }
      }).catch(() => {})
    },
    async showTimeline(row) {
      this.currentRow = row
      try {
        const res = await getChangeTimeline(row.companyName)
        this.timelineData = (res && res.data) || []
      } catch (e) {
        this.timelineData = []
      }
      this.timelineDrawer = true
    },
    rowClass({ row }) {
      if (row.isWarning && row.isWarning !== 0) return 'row-warning'
      if (row.approvalStatus === 'PENDING') return 'row-warning'
      return ''
    },
    changeTypeLabel(v) { return { INCREASE: '增资', DECREASE: '减资', TRANSFER: '产权转让', FREE_TRANSFER: '无偿划转', MERGE: '合并', SPLIT: '分立' }[v] || v },
    changeTypeTag(v) { return { INCREASE: 'success', DECREASE: 'warning', TRANSFER: 'danger', FREE_TRANSFER: 'primary', MERGE: 'info', SPLIT: '' }[v] || '' },
    changeTypeColor(v) { return { INCREASE: 'success', DECREASE: 'warning', TRANSFER: 'danger', FREE_TRANSFER: 'primary', MERGE: 'info', SPLIT: '' }[v] || 'primary' },
    approvalLabel(v) { return { PENDING: '待审批', APPROVED: '已审批', REJECTED: '已拒绝' }[v] || v },
    approvalTagType(v) { return { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }[v] || '' },
  },
}
</script>

<style scoped>
.change-record { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
}
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.type-tags { margin-bottom: 12px; font-size: 13px; color: #555; }
.search-card { margin-bottom: 12px; }
.toolbar { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.total-tip { margin-left: auto; font-size: 12px; color: #888; }
.timeline-wrap { padding: 16px 20px; }
.tl-company { font-size: 15px; font-weight: 700; color: #333; margin-bottom: 16px; }
.tl-card { margin-bottom: 0; }
.tl-title { display: flex; align-items: center; gap: 12px; margin-bottom: 6px; }
.tl-amount { font-size: 14px; font-weight: 700; }
.tl-ratio { font-size: 12px; color: #666; }
.tl-remark { font-size: 12px; color: #aaa; margin-top: 4px; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
</style>
