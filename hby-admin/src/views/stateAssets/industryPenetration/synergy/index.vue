<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-connection"></i><span>产业协同分析</span></div>
      <div class="page-header-desc">跨行业协同价值挖掘 · 上下游产业链协同 · 资源共享效益量化</div>
    </div>

    <!-- KPI统计卡 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" :style="{background: ipLightBg}"><i class="el-icon-connection" :style="{color: ipSecondary, fontSize: '26px'}"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ kpiData.projectCount }}</div><div class="kpi-label">产业协同项目数</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-money" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ kpiData.annualSaving }}</div><div class="kpi-label">年度协同节省</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-s-order" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ kpiData.internalTrade }}</div><div class="kpi-label">内部交易总额</div></div></div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card"><div class="kpi-icon-wrap" style="background:#E8F4FF"><i class="el-icon-pie-chart" :style="{color: ipSecondary, fontSize: '26px'}"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ kpiData.coverageRatio }}</div><div class="kpi-label">协同覆盖率</div></div></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 协同矩阵热力图 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <div slot="header" class="card-header-title"><i class="el-icon-connection" :style="{color: ipSecondary}"></i> 行业间协同强度矩阵（颜色越深协同越强）</div>
      <div class="synergy-matrix">
        <div class="matrix-header">
          <div class="matrix-label-cell">↓发起/受益→</div>
          <div class="matrix-col-label" v-for="col in industries" :key="col">{{ col }}</div>
        </div>
        <div class="matrix-row" v-for="row in industries" :key="row">
          <div class="matrix-row-label">{{ row }}</div>
          <div class="matrix-cell" v-for="col in industries" :key="col"
            :style="matrixCellStyle(row, col)"
            :title="`${row}→${col}: ${getMatrixValue(row, col)}分`">
            {{ row === col ? '-' : getMatrixValue(row, col) }}
          </div>
        </div>
      </div>
    </el-card>

    <!-- 协同明细列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header-title">协同项目明细列表</div>
      <el-table :data="synergyList" border size="small" v-loading="loading"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }" style="width:100%">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="协同类型" prop="synergyType" width="130" align="center">
          <template slot-scope="s">
            <el-tag type="primary" size="small">{{ s.row.synergyType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发起行业" prop="fromIndustry" width="105" align="center">
          <template slot-scope="s"><el-tag size="small" :style="industryTagStyle(s.row.fromIndustry)">{{ s.row.fromIndustry }}</el-tag></template>
        </el-table-column>
        <el-table-column label="受益行业" prop="toIndustry" width="105" align="center">
          <template slot-scope="s"><el-tag size="small" :style="industryTagStyle(s.row.toIndustry)">{{ s.row.toIndustry }}</el-tag></template>
        </el-table-column>
        <el-table-column label="涉及企业数" prop="enterpriseCount" width="100" align="center" />
        <el-table-column label="年协同价值(万元)" prop="annualValue" width="140" align="right">
          <template slot-scope="s"><span style="font-weight:600;color:#52C41A">{{ s.row.annualValue.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="协同强度" prop="strength" width="90" align="center">
          <template slot-scope="s">
            <el-tag :type="{ 强: 'success', 中: 'warning', 弱: 'info' }[s.row.strength]" size="small">{{ s.row.strength }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="成熟度" prop="maturity" width="90" align="center">
          <template slot-scope="s">
            <el-tag :type="{ 成熟: 'success', 发展: 'primary', 初始: 'info' }[s.row.maturity]" size="small">{{ s.row.maturity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="协同说明" prop="desc" min-width="180" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getSynergyList, getSynergyMatrix } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustrySynergy',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      synergyList: [],
      matrixData: {},
      kpiData: { projectCount: '-', annualSaving: '-', internalTrade: '-', coverageRatio: '-' },
      industries: ['能源行业', '金融行业', '制造行业', '基础设施', '公共服务'],
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getSynergyList()
        if (res && res.result === 200 && res.data) {
          this.synergyList = res.data.tlist || res.data.list || res.data || []
          if (res.data.kpiData) this.kpiData = res.data.kpiData
        }
      } catch (e) { this.synergyList = [] } finally { this.loading = false }
      try {
        const mr = await getSynergyMatrix()
        if (mr && mr.result === 200 && mr.data) this.matrixData = mr.data
      } catch (e) { /* ignore */ }
    },
    getMatrixValue(row, col) {
      if (row === col) return 0
      return (this.matrixData[row] && this.matrixData[row][col]) || 0
    },
    matrixCellStyle(row, col) {
      if (row === col) return { background: '#f5f5f5', color: '#909399' }
      const val = this.getMatrixValue(row, col)
      const alpha = val / 100
      return {
        background: `rgba(${this.ipPrimaryRgb}, ${alpha * 0.7})`,
        color: val > 50 ? '#fff' : '#303133',
        fontWeight: '600',
      }
    },
    industryTagStyle(v) {
      const map = {
        '能源行业': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '金融行业': { background: this.ipLightBg, color: this.ipBright, border: '1px solid #ADC6FF' },
        '制造行业': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '基础设施': { background: '#E8F4FF', color: this.ipSecondary, border: '1px solid #91CAFF' },
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
.card-header-title { font-size: 14px; font-weight: 600; color: #303133; }

.synergy-matrix { overflow-x: auto; }
.matrix-header { display: flex; }
.matrix-label-cell { width: 90px; min-width: 90px; padding: 10px 8px; font-size: 12px; color: #909399; font-weight: 600; text-align: center; background: #f5f5f5; border: 1px solid #e8e8e8; }
.matrix-col-label { flex: 1; min-width: 90px; padding: 10px 6px; font-size: 12px; font-weight: 600; color: var(--ip-secondary, #0050A0); text-align: center; background: var(--ip-light-bg, #EBF1FF); border: 1px solid #e8e8e8; border-left: none; }
.matrix-row { display: flex; }
.matrix-row-label { width: 90px; min-width: 90px; padding: 14px 8px; font-size: 12px; font-weight: 600; color: var(--ip-secondary, #0050A0); text-align: center; background: var(--ip-light-bg, #EBF1FF); border: 1px solid #e8e8e8; border-top: none; }
.matrix-cell { flex: 1; min-width: 90px; padding: 14px 6px; font-size: 14px; text-align: center; border: 1px solid #e8e8e8; border-top: none; border-left: none; cursor: default; transition: opacity 0.2s;
  &:hover { opacity: 0.85; } }

::v-deep .el-card { border-radius: 6px; }
</style>
