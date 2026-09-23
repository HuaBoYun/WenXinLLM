<template>
  <div class="fin-risk-wrap">
    <!-- 红色Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务风险预警管理</div>
        <div class="banner-sub">多维度财务风险实时预警，覆盖高杠杆、造假疑点、关联交易异常、费用失控等10类风险规则</div>
      </div>
      <div class="banner-right">
        <div v-for="q in bannerQuadrants" :key="q.label" class="banner-quad">
          <div class="bq-value">{{ q.value }}</div>
          <div class="bq-label">{{ q.label }}</div>
        </div>
      </div>
    </div>

    <!-- 查询 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.level" style="width:120px" clearable>
            <el-option label="高危" value="HIGH"/>
            <el-option label="中危" value="MEDIUM"/>
            <el-option label="低危" value="LOW"/>
          </el-select>
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warnType" style="width:180px" clearable>
            <el-option label="高杠杆风险" value="HIGH_LEVERAGE"/>
            <el-option label="财务造假疑点" value="FRAUD_SUSPICION"/>
            <el-option label="关联交易异常" value="RELATED_PARTY"/>
            <el-option label="费用失控" value="EXPENSE_ABNORMAL"/>
            <el-option label="利润质量下滑" value="PROFIT_QUALITY"/>
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="queryForm.status" style="width:120px" clearable>
            <el-option label="待处置" value="PENDING"/>
            <el-option label="处置中" value="PROCESSING"/>
            <el-option label="已关闭" value="CLOSED"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>财务风险预警台账</span>
        <el-tag type="danger" size="small" style="margin-left:10px">{{ pendingCount }} 条待处置</el-tag>
      </div>
      <el-table :data="warnList" size="small" border :row-class-name="rowClass" v-loading="loading">
        <el-table-column label="预警编号" prop="warnNo" width="168"/>
        <el-table-column label="企业名称" prop="companyName" min-width="130"/>
        <el-table-column label="预警类型" prop="warnType" min-width="200" show-overflow-tooltip/>
        <el-table-column label="风险规则" prop="ruleCode" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag type="danger" size="mini" plain>{{ row.ruleCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="level" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.level==='HIGH'?'danger':row.level==='MEDIUM'?'warning':''" size="mini">
              {{ row.level==='HIGH'?'高危':row.level==='MEDIUM'?'中危':'低危' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="触发时间" prop="triggerTime" width="165"/>
        <el-table-column label="状态" prop="status" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.status==='PENDING'?'danger':row.status==='PROCESSING'?'warning':'info'" size="mini">
              {{ row.status==='PENDING'?'待处置':row.status==='PROCESSING'?'处置中':'已关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="openDetail(row)">详情</el-button>
            <el-button type="text" size="mini" style="color:#1677FF" @click="openHandle(row)" v-if="row.status!=='CLOSED'">处置</el-button>
            <el-button type="text" size="mini" style="color:#52C41A" @click="closeWarning(row)" v-if="row.status==='PROCESSING'">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 处置弹窗 -->
    <el-dialog title="预警处置" :visible.sync="handleVisible" width="560px">
      <div v-if="activeRow.warnNo" style="margin-bottom:12px">
        <el-alert :title="activeRow.warnType" :type="activeRow.level==='HIGH'?'error':'warning'" show-icon :closable="false" style="margin-bottom:12px"/>
        <el-descriptions :column="2" border size="small" style="margin-bottom:12px">
          <el-descriptions-item label="预警编号">{{ activeRow.warnNo }}</el-descriptions-item>
          <el-descriptions-item label="企业名称">{{ activeRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="风险规则">{{ activeRow.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="触发时间">{{ activeRow.triggerTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-form :model="handleForm" label-width="90px" size="small">
        <el-form-item label="处置方式">
          <el-select v-model="handleForm.handleType" style="width:100%">
            <el-option label="下达整改通知" value="RECTIFICATION"/>
            <el-option label="约谈企业负责人" value="INTERVIEW"/>
            <el-option label="委托专项审计" value="AUDIT"/>
            <el-option label="提交监管部门" value="REPORT"/>
          </el-select>
        </el-form-item>
        <el-form-item label="处置说明">
          <el-input type="textarea" v-model="handleForm.handleDesc" :rows="3" placeholder="请填写处置说明..."/>
        </el-form-item>
        <el-form-item label="处置期限">
          <el-date-picker v-model="handleForm.deadline" type="date" placeholder="选择截止日期" style="width:100%"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="handleVisible=false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确认处置</el-button>
      </div>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer :title="`预警详情：${activeRow.warnNo||''}`" :visible.sync="detailVisible" size="480px" direction="rtl">
      <div v-if="activeRow.warnNo" style="padding:20px">
        <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
          <el-descriptions-item label="预警编号">{{ activeRow.warnNo }}</el-descriptions-item>
          <el-descriptions-item label="企业名称">{{ activeRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型" :span="2">{{ activeRow.warnType }}</el-descriptions-item>
          <el-descriptions-item label="风险规则">{{ activeRow.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="activeRow.level==='HIGH'?'danger':'warning'" size="mini">{{ activeRow.level==='HIGH'?'高危':'中危' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="触发条件" :span="2">{{ activeRow.triggerCondition }}</el-descriptions-item>
          <el-descriptions-item label="触发时间" :span="2">{{ activeRow.triggerTime }}</el-descriptions-item>
        </el-descriptions>
        <el-divider>处置进度</el-divider>
        <el-timeline v-if="activeRow.timeline && activeRow.timeline.length">
          <el-timeline-item v-for="t in activeRow.timeline" :key="t.time"
            :type="t.type||'primary'" :timestamp="t.time" placement="top">
            {{ t.content }}
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无处置记录" :image-size="60"/>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getFinancialWarningList, getFinancialWarningDetail, handleFinancialWarning, closeFinancialWarning } from '@/api/stateAssets/financialPenetration'
import { mapGetters } from 'vuex'

const WARN_TYPE_MAP = {
  HIGH_LEVERAGE: '高杠杆风险',
  FRAUD_SUSPICION: '财务造假疑点',
  RELATED_PARTY: '关联交易异常',
  EXPENSE_ABNORMAL: '费用失控',
  PROFIT_QUALITY: '利润质量下滑',
}

export default {
  name: 'FinancialRiskWarning',
  data() {
    return {
      queryForm: { level: '', warnType: '', status: '' },
      bannerQuadrants: [],
      warnList: [],
      loading: false,
      handleVisible: false,
      detailVisible: false,
      activeRow: {},
      handleForm: { handleType: '', handleDesc: '', deadline: '' },
      pagination: { pageNumber: 1, pageSize: 15, total: 0 },
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
    pendingCount() { return this.warnList.filter(r => r.status === 'PENDING').length },
  },
  async mounted() {
    await this.loadData()
  },
  methods: {
    /** 预警类型英文转中文 */
    formatWarnType(type) {
      return WARN_TYPE_MAP[type] || type || '-'
    },
    resetQuery() {
      this.queryForm = { level: '', warnType: '', status: '' }
      this.pagination.pageNumber = 1
      this.loadData()
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getFinancialWarningList({
          ...this.queryForm,
          pageNumber: this.pagination.pageNumber,
          pageSize: this.pagination.pageSize,
        })
        if (res.data && res.data.tlist) {
          this.warnList = res.data.tlist.map(w => ({
            warnNo: w.alertNo || w.id,
            companyName: w.companyName,
            warnType: this.formatWarnType(w.alertTitle || w.alertType),
            ruleCode: w.ruleCode || w.alertType,
            level: w.riskLevel || w.level || 'HIGH',
            triggerTime: w.alertTime || w.createTime,
            status: w.status,
            triggerCondition: w.triggerCondition || w.alertDesc,
            timeline: w.timeline || [],
          }))
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.warnList = []
          this.pagination.total = 0
        }
        this.buildBanner()
      } catch (e) {
        console.error('加载预警数据失败', e)
        this.$message.error('加载预警数据失败')
      } finally {
        this.loading = false
      }
    },
    buildBanner() {
      const list = this.warnList
      this.bannerQuadrants = [
        { label: '高危预警', value: list.filter(r => r.level === 'HIGH').length + '条' },
        { label: '中危预警', value: list.filter(r => r.level === 'MEDIUM').length + '条' },
        { label: '低危预警', value: list.filter(r => r.level === 'LOW').length + '条' },
        { label: '处置中', value: list.filter(r => r.status === 'PROCESSING').length + '条' },
      ]
    },
    openHandle(row) {
      this.activeRow = row
      this.handleForm = { handleType: '', handleDesc: '', deadline: '' }
      this.handleVisible = true
    },
    async openDetail(row) {
      this.activeRow = { ...row }
      this.detailVisible = true
      // 从后端获取完整详情（含处置历史）
      try {
        const res = await getFinancialWarningDetail(row.warnNo)
        if (res.data) {
          this.activeRow = {
            ...this.activeRow,
            triggerCondition: res.data.triggerCondition || row.triggerCondition,
            timeline: res.data.timeline || [],
          }
        }
      } catch (e) {
        console.error('获取预警详情失败', e)
      }
    },
    async submitHandle() {
      if (!this.handleForm.handleType) {
        this.$message.warning('请选择处置方式')
        return
      }
      try {
        const res = await handleFinancialWarning({
          warnNo: this.activeRow.warnNo,
          handleType: this.handleForm.handleType,
          handleDesc: this.handleForm.handleDesc,
          deadline: this.handleForm.deadline,
        })
        if (res.result === 200 || res.code === 1) {
          this.$message.success('处置信息已提交')
          this.handleVisible = false
          await this.loadData()
        } else {
          this.$message.error(res.msg || '处置失败')
        }
      } catch (e) {
        this.$message.error('处置请求失败')
      }
    },
    async closeWarning(row) {
      try {
        await this.$confirm('确认关闭该预警？关闭后不可恢复。', '提示', { type: 'warning' })
        const res = await closeFinancialWarning(row.warnNo)
        if (res.result === 200 || res.code === 1) {
          this.$message.success('预警已关闭')
          await this.loadData()
        } else {
          this.$message.error(res.msg || '关闭失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('关闭请求失败')
      }
    },
    rowClass({ row }) {
      if (row.level === 'HIGH' && row.status !== 'CLOSED') return 'row-danger'
      if (row.level === 'MEDIUM') return 'row-warning'
      return ''
    },
  },
}
</script>

<style scoped lang="scss">
.fin-risk-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.85); max-width: 520px; }
  .banner-right { display: flex; gap: 18px; }
  .banner-quad { text-align: center; background: rgba(255,255,255,0.15); border-radius: 8px; padding: 10px 16px; }
  .bq-value { font-size: 22px; font-weight: 700; color: #fff; }
  .bq-label { font-size: 11px; color: rgba(255,255,255,0.8); margin-top: 2px; }
}
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; gap: 8px; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
