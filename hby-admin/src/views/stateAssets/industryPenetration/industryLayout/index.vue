<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document"></i><span>行业布局台账</span></div>
      <div class="page-header-desc">管理集团企业行业分类归属，支持主业认定、多维查询与布局结构分析</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-office-building" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ layoutStats.total }}</div><div class="kpi-label">纳管企业总数</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-circle-check" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ layoutStats.mainBiz }}</div><div class="kpi-label">主业企业数</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-warning-outline" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ layoutStats.nonMainBiz }}</div><div class="kpi-label">非主业企业数</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-pie-chart" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ layoutStats.nonMainRatio }}%</div><div class="kpi-label">非主业占比</div></div></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询 -->
    <el-card shadow="never" style="margin-bottom:10px">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="行业大类">
          <el-select v-model="queryForm.industryGroup" placeholder="全部" clearable style="width:130px">
            <el-option label="能源行业" value="能源行业" />
            <el-option label="金融行业" value="金融行业" />
            <el-option label="制造行业" value="制造行业" />
            <el-option label="基础设施" value="基础设施" />
            <el-option label="公共服务" value="公共服务" />
          </el-select>
        </el-form-item>
        <el-form-item label="主业认定">
          <el-select v-model="queryForm.mainBizType" placeholder="全部" clearable style="width:120px">
            <el-option label="主业" :value="true" />
            <el-option label="非主业" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="竞争力等级">
          <el-select v-model="queryForm.competitiveness" placeholder="全部" clearable style="width:110px">
            <el-option label="强" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="弱" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="全部" clearable style="width:110px">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border size="small"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }" style="width:100%">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="行业大类" prop="industryGroup" width="105" align="center">
          <template slot-scope="s">
            <el-tag :style="industryTagStyle(s.row.industryGroup)" size="small">{{ s.row.industryGroup }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="子行业" prop="subIndustry" width="120" align="center" show-overflow-tooltip />
        <el-table-column label="主业认定" prop="isMainBiz" width="95" align="center">
          <template slot-scope="s">
            <el-tag :type="s.row.isMainBiz ? 'primary' : 'warning'" size="small">{{ s.row.isMainBiz ? '主业' : '非主业' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="年营收(亿)" prop="revenue" width="110" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.revenue }}</span></template>
        </el-table-column>
        <el-table-column label="营收占集团%" prop="revenueRatio" width="110" align="center" />
        <el-table-column label="净利率%" prop="netMargin" width="90" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.netMargin < 0 ? '#F5222D' : '#303133' }">{{ s.row.netMargin }}</span>
          </template>
        </el-table-column>
        <el-table-column label="行业竞争力" prop="competitiveness" width="100" align="center">
          <template slot-scope="s">
            <el-tag :type="{ HIGH: 'success', MEDIUM: 'warning', LOW: 'danger' }[s.row.competitiveness]" size="small">
              {{ { HIGH: '强', MEDIUM: '中', LOW: '弱' }[s.row.competitiveness] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="s">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[s.row.riskLevel]" size="small">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[s.row.riskLevel] }}风险
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="重点监控" prop="isKeyMonitor" width="90" align="center">
          <template slot-scope="s">
            <el-switch :value="s.row.isKeyMonitor" @change="val => handleMonitorChange(s.row, val)" size="mini" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="s">
            <el-button size="mini" type="text" @click="handleView(s.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(s.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F5222D" @click="handleDelete(s.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:15px;text-align:right"
        :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="560px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="110px" size="small">
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业大类" prop="industryGroup">
              <el-select v-model="form.industryGroup" placeholder="请选择" style="width:100%">
                <el-option label="能源行业" value="能源行业" />
                <el-option label="金融行业" value="金融行业" />
                <el-option label="制造行业" value="制造行业" />
                <el-option label="基础设施" value="基础设施" />
                <el-option label="公共服务" value="公共服务" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="子行业" prop="subIndustry">
              <el-input v-model="form.subIndustry" placeholder="如：传统能源、商业银行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主业认定" prop="isMainBiz">
              <el-select v-model="form.isMainBiz" placeholder="请选择" style="width:100%">
                <el-option label="主业" :value="true" />
                <el-option label="非主业" :value="false" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年营收(亿元)" prop="revenue">
              <el-input-number v-model="form.revenue" :precision="1" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="净利率(%)">
              <el-input-number v-model="form.netMargin" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="竞争力等级" prop="competitiveness">
              <el-select v-model="form.competitiveness" placeholder="请选择" style="width:100%">
                <el-option label="强" value="HIGH" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="弱" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高风险" value="HIGH" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="低风险" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重点监控">
              <el-switch v-model="form.isKeyMonitor" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情抽屉 -->
    <el-drawer title="企业行业详情" :visible.sync="drawerVisible" direction="rtl" size="480px">
      <div class="drawer-content" v-if="currentRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="行业大类">
            <el-tag :style="industryTagStyle(currentRow.industryGroup)" size="small">{{ currentRow.industryGroup }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="子行业">{{ currentRow.subIndustry }}</el-descriptions-item>
          <el-descriptions-item label="主业认定">
            <el-tag :type="currentRow.isMainBiz ? 'primary' : 'warning'" size="small">{{ currentRow.isMainBiz ? '主业' : '非主业' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="年营收">{{ currentRow.revenue }}亿元</el-descriptions-item>
          <el-descriptions-item label="营收占比">{{ currentRow.revenueRatio }}%</el-descriptions-item>
          <el-descriptions-item label="净利率">
            <span :style="{ color: currentRow.netMargin < 0 ? '#F5222D' : '#303133' }">{{ currentRow.netMargin }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="行业竞争力">
            <el-tag :type="{ HIGH: 'success', MEDIUM: 'warning', LOW: 'danger' }[currentRow.competitiveness]" size="small">
              {{ { HIGH: '强', MEDIUM: '中', LOW: '弱' }[currentRow.competitiveness] }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[currentRow.riskLevel]" size="small">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[currentRow.riskLevel] }}风险
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="重点监控" :span="2">{{ currentRow.isKeyMonitor ? '是' : '否' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getIndustryLayoutList, saveIndustryLayout, deleteIndustryLayout, updateKeyMonitor } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryLayout',
  mixins: [investThemeMixin],
  computed: {
    layoutStats() {
      const d = this.list
      const main = d.filter(r => r.isMainBiz).length
      const non = d.filter(r => !r.isMainBiz).length
      return { total: this.total, mainBiz: main, nonMainBiz: non, nonMainRatio: this.total ? ((non / this.total) * 100).toFixed(1) : 0 }
    },
    dialogTitle() { return this.form.id ? '编辑行业布局' : '新增行业布局' },
  },
  data() {
    return {
      loading: false, submitLoading: false,
      list: [], total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', industryGroup: '', mainBizType: undefined, competitiveness: '', riskLevel: '' },
      dialogVisible: false, drawerVisible: false, currentRow: null,
      form: { id: null, companyName: '', industryGroup: '', subIndustry: '', isMainBiz: true, revenue: 0, netMargin: 0, competitiveness: 'MEDIUM', riskLevel: 'LOW', isKeyMonitor: false },
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        industryGroup: [{ required: true, message: '请选择行业大类', trigger: 'change' }],
        isMainBiz: [{ required: true, message: '请选择主业认定', trigger: 'change' }],
        competitiveness: [{ required: true, message: '请选择竞争力等级', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
      },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getIndustryLayoutList(this.queryForm)
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: this.queryForm.pageSize, companyName: '', industryGroup: '', mainBizType: undefined, competitiveness: '', riskLevel: '' }
      this.fetchData()
    },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.fetchData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.fetchData() },
    handleAdd() {
      this.form = { id: null, companyName: '', industryGroup: '', subIndustry: '', isMainBiz: true, revenue: 0, netMargin: 0, competitiveness: 'MEDIUM', riskLevel: 'LOW', isKeyMonitor: false }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleView(row) { this.currentRow = row; this.drawerVisible = true },
    handleDelete(row) {
      this.$confirm(`确认删除「${row.companyName}」的行业布局记录？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteIndustryLayout(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleMonitorChange(row, val) {
      try {
        const res = await updateKeyMonitor({ id: row.id, isKeyMonitor: val })
        if (res && res.result === 200) {
          this.$message.success(`已${val ? '开启' : '关闭'}重点监控`)
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '操作失败')
        }
      } catch (e) {
        this.$message.error('操作失败')
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.submitLoading = true
        try {
          const res = await saveIndustryLayout(this.form)
          if (res && res.result === 200) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '保存失败')
          }
        } catch (e) {
          this.$message.error('保存失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    industryTagStyle(v) {
      const map = {
        '能源行业': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '金融行业': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' },
        '制造行业': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '基础设施': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' },
        '公共服务': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
      }
      return map[v] || {}
    },
  },
}
</script>

<style lang="scss" scoped>
.industry-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info .kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-info .kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.drawer-content { padding: 16px 20px; }
::v-deep .el-card { border-radius: 6px; }
</style>
