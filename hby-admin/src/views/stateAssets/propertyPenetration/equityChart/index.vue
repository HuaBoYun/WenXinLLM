<template>
  <div class="equity-chart" v-loading="loading">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">股权穿透图</h2>
        <p class="banner-sub">可视化股权层级图谱 · 逐层穿透展开 · 异常节点高亮</p>
      </div>
    </div>

    <!-- KPI行 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}{{ kpi.unit }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 规则提示 -->
    <el-alert type="warning" :closable="false" style="margin-bottom:12px;">
      <span>央企管控原则：股权层级原则上不超过 <b>5级</b>，超过5级的企业节点将以红色标注并触发预警（PRO-W01）</span>
    </el-alert>

    <!-- 操作栏 -->
    <el-card class="ctrl-card" shadow="never">
      <div class="ctrl-bar">
        <el-select v-model="selectedRoot" placeholder="选择集团顶层企业" style="width:220px;" size="small" @change="handleRootChange">
          <el-option v-for="r in rootList" :key="r.companyId" :label="r.companyName" :value="r.companyId"></el-option>
        </el-select>
        <span class="ctrl-label">穿透层数：</span>
        <el-slider v-model="maxDepth" :min="1" :max="7" :step="1" show-stops style="width:160px;" @change="handleDepthChange"></el-slider>
        <span class="depth-val">{{ maxDepth }}层</span>
        <el-radio-group v-model="viewMode" size="small" style="margin-left:20px;">
          <el-radio-button label="tree">树形图</el-radio-button>
          <el-radio-button label="table">明细表</el-radio-button>
        </el-radio-group>
        <el-button type="primary" size="small" icon="el-icon-refresh" style="margin-left:auto;" @click="loadData">刷新</el-button>
      </div>
    </el-card>

    <!-- 图谱区 -->
    <el-card v-show="viewMode === 'tree'" class="chart-card" shadow="never">
      <div class="legend-row">
        <span class="legend-item"><span class="dot" style="background:#1677FF;"></span>正常经营</span>
        <span class="legend-item"><span class="dot" style="background:#FA8C16;"></span>连续亏损</span>
        <span class="legend-item"><span class="dot" style="background:#F5222D;"></span>层级超限/异常</span>
        <span class="legend-item"><span class="dot" style="background:#8C8C8C;"></span>参股企业</span>
      </div>
      <div v-if="!treeData && !loading" style="text-align:center;padding:80px 0;color:#999;">
        <i class="el-icon-warning-outline" style="font-size:48px;"></i>
        <p style="margin-top:12px;">暂无股权穿透数据，请确认后端服务已启动并已录入产权登记数据</p>
      </div>
      <div v-show="treeData" ref="treeChart" style="width:100%;height:560px;"></div>
    </el-card>

    <!-- 明细表 -->
    <el-card v-show="viewMode === 'table'" class="table-card" shadow="never">
      <el-table :data="flatList" size="small" border :row-class-name="rowClass">
        <el-table-column label="层级" prop="equityLevel" width="70" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.equityLevel > 5 ? 'danger' : 'primary'" size="mini">第{{ row.equityLevel }}级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="企业名称" prop="companyName" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column label="上级企业" prop="parentName" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column label="持股比例" prop="equityRatio" width="100" align="right">
          <template slot-scope="{row}">{{ row.equityRatio }}%</template>
        </el-table-column>
        <el-table-column label="经营状态" prop="businessStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="bsType(row.businessStatus)" size="mini">{{ bsLabel(row.businessStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="层级预警" prop="isWarning" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.isWarning" type="danger" size="mini">预警</el-tag>
            <span v-else style="color:#52C41A;">正常</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 节点详情弹窗 -->
    <el-dialog title="企业产权详情" :visible.sync="nodeDialogVisible" width="480px">
      <el-descriptions v-if="currentNode" :column="2" border size="small">
        <el-descriptions-item label="企业名称" :span="2">{{ currentNode.companyName }}</el-descriptions-item>
        <el-descriptions-item label="股权层级">第 {{ currentNode.equityLevel }} 级</el-descriptions-item>
        <el-descriptions-item label="持股比例">{{ currentNode.equityRatio }}%</el-descriptions-item>
        <el-descriptions-item label="经营状态">
          <el-tag :type="bsType(currentNode.businessStatus)" size="mini">{{ bsLabel(currentNode.businessStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="层级预警">
          <el-tag v-if="currentNode.isWarning" type="danger" size="mini">超限预警</el-tag>
          <span v-else style="color:#52C41A;">正常</span>
        </el-descriptions-item>
        <el-descriptions-item label="子企业数量" :span="2">{{ (currentNode.children || []).length }} 家</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="nodeDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="openRegistryDialog">查看台账</el-button>
      </div>
    </el-dialog>

    <!-- 产权台账弹窗 -->
    <el-dialog title="产权登记台账" :visible.sync="registryDialogVisible" width="900px" top="5vh" v-loading="registryLoading">
      <template v-if="registryData">
        <!-- 企业基本信息 -->
        <el-descriptions :column="3" border size="small" title="企业基本信息" style="margin-bottom:16px;">
          <el-descriptions-item label="企业名称" :span="2">{{ registryData.company.companyName }}</el-descriptions-item>
          <el-descriptions-item label="企业编码">{{ registryData.company.companyId }}</el-descriptions-item>
          <el-descriptions-item label="产权类型">{{ rightTypeLabel(registryData.company.rightType) }}</el-descriptions-item>
          <el-descriptions-item label="持股比例">{{ registryData.company.equityRatio }}%</el-descriptions-item>
          <el-descriptions-item label="股权层级">第 {{ registryData.company.equityLevel }} 级</el-descriptions-item>
          <el-descriptions-item label="投资金额">{{ formatAmount(registryData.company.investAmount) }} 万元</el-descriptions-item>
          <el-descriptions-item label="注册资本">{{ formatAmount(registryData.company.registeredCapital) }} 万元</el-descriptions-item>
          <el-descriptions-item label="经营状态">
            <el-tag :type="bsType(registryData.company.businessStatus)" size="mini">{{ bsLabel(registryData.company.businessStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">{{ registryData.company.industry || '—' }}</el-descriptions-item>
          <el-descriptions-item label="所在地区">{{ registryData.company.region || '—' }}</el-descriptions-item>
          <el-descriptions-item label="登记状态">{{ regStatusLabel(registryData.company.registrationStatus) }}</el-descriptions-item>
          <el-descriptions-item label="登记编号">{{ registryData.company.registrationNo || '—' }}</el-descriptions-item>
          <el-descriptions-item label="统一信用代码">{{ registryData.company.unifiedCreditCode || '—' }}</el-descriptions-item>
          <el-descriptions-item label="上级企业" :span="2">{{ registryData.company.parentCompanyName || '—' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 直接子企业列表 -->
        <div style="margin-bottom:8px;font-weight:600;font-size:14px;">
          直接子企业（{{ registryData.childrenCount }} 家）
        </div>
        <el-table :data="registryData.children" size="mini" border style="width:100%;" max-height="280">
          <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column label="产权类型" prop="rightType" width="90" align="center">
            <template slot-scope="{row}">{{ rightTypeLabel(row.rightType) }}</template>
          </el-table-column>
          <el-table-column label="持股比例" prop="equityRatio" width="90" align="right">
            <template slot-scope="{row}">{{ row.equityRatio }}%</template>
          </el-table-column>
          <el-table-column label="投资金额(万)" prop="investAmount" width="110" align="right">
            <template slot-scope="{row}">{{ formatAmount(row.investAmount) }}</template>
          </el-table-column>
          <el-table-column label="层级" prop="equityLevel" width="60" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.equityLevel > 5 ? 'danger' : 'primary'" size="mini">L{{ row.equityLevel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="经营状态" prop="businessStatus" width="80" align="center">
            <template slot-scope="{row}">
              <el-tag :type="bsType(row.businessStatus)" size="mini">{{ bsLabel(row.businessStatus) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="行业" prop="industry" width="80" show-overflow-tooltip></el-table-column>
          <el-table-column label="地区" prop="region" width="70" show-overflow-tooltip></el-table-column>
        </el-table>
      </template>
      <div v-else-if="!registryLoading" style="text-align:center;padding:40px 0;color:#999;">
        暂无产权登记数据
      </div>
      <div slot="footer">
        <el-button @click="registryDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getEquityTree, getEquityStats, getEquityRegistryDetail } from '@/api/stateAssets/propertyRight'

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
  name: 'PropertyEquityChart',
  data() {
    return {
      selectedRoot: '',
      maxDepth: 5,
      viewMode: 'tree',
      rootList: [],
      treeData: null,
      flatList: [],
      kpiList: [],
      nodeDialogVisible: false,
      currentNode: null,
      loading: false,
      chartInstance: null,
      // 台账弹窗
      registryDialogVisible: false,
      registryLoading: false,
      registryData: null,
    }
  },
  watch: {
    viewMode(val) {
      if (val === 'tree') {
        this.$nextTick(() => this.renderChart())
      }
    },
  },
  mounted() {
    this.loadData()
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
      this.chartInstance = null
    }
  },
  methods: {
    /** 加载统计概览和顶层企业列表 */
    async loadData() {
      this.loading = true
      try {
        const statsRes = await getEquityStats()
        this.rootList = (statsRes.data && statsRes.data.rootList) || []
        if (this.rootList.length > 0) {
          // 如果当前选中的公司不在列表中，默认选第一个
          const ids = this.rootList.map(r => r.companyId)
          if (!this.selectedRoot || !ids.includes(this.selectedRoot)) {
            this.selectedRoot = this.rootList[0].companyId
          }
        }
      } catch (e) {
        this.rootList = []
        this.$message.error('获取企业列表失败，请检查后端服务是否启动')
      }
      // 加载选中企业的股权树
      await this.loadTreeData()
      this.loading = false
    },
    /** 加载指定企业的股权穿透树 */
    async loadTreeData() {
      if (!this.selectedRoot) {
        this.treeData = null
        this.flatList = []
        this.buildKpi()
        return
      }
      try {
        const res = await getEquityTree(this.selectedRoot, this.maxDepth)
        this.treeData = res.data || null
      } catch (e) {
        this.treeData = null
        this.$message.error('获取股权穿透数据失败')
      }
      // 构建扁平列表
      this.flatList = []
      if (this.treeData) {
        this.buildFlat(this.treeData, null)
      }
      this.buildKpi()
      this.$nextTick(() => this.renderChart())
    },
    /** 切换顶层企业时重新加载数据 */
    handleRootChange() {
      this.loadTreeData()
    },
    /** 穿透层数变化时重新加载数据 */
    handleDepthChange() {
      this.loadTreeData()
    },
    buildFlat(node, parentName) {
      if (!node) return
      this.flatList.push({ ...node, parentName: parentName || '—' })
      ;(node.children || []).forEach(c => this.buildFlat(c, node.companyName))
    },
    buildKpi() {
      const all = this.flatList
      const maxLevel = all.length > 0 ? Math.max(...all.map(n => n.equityLevel || 0)) : 0
      const warningCount = all.filter(n => n.isWarning || n.equityLevel > 5).length
      const lossCount = all.filter(n => n.businessStatus === 'LOSS').length
      this.kpiList = [
        { key: 'total', label: '企业总数', value: all.length, unit: '家', color: '#1677FF' },
        { key: 'maxLevel', label: '最深层级', value: maxLevel, unit: '级', color: maxLevel > 5 ? '#F5222D' : '#52C41A' },
        { key: 'warning', label: '超限企业', value: warningCount, unit: '家', color: warningCount > 0 ? '#F5222D' : '#52C41A' },
        { key: 'loss', label: '亏损企业', value: lossCount, unit: '家', color: lossCount > 0 ? '#FA8C16' : '#52C41A' },
      ]
    },
    renderChart() {
      if (this.viewMode !== 'tree') return
      if (!this.$refs.treeChart) return
      if (!this.treeData) return
      // 销毁旧实例避免内存泄漏
      if (this.chartInstance) {
        this.chartInstance.dispose()
      }
      this.chartInstance = echarts.init(this.$refs.treeChart)

      const buildEchartData = (node) => ({
        name: `${node.companyName}\n${node.equityRatio}%`,
        rawData: node,
        itemStyle: {
          color: node.isWarning || node.equityLevel > 5 ? '#F5222D'
            : node.businessStatus === 'LOSS' ? '#FA8C16'
            : node.equityRatio < 50 ? '#8C8C8C'
            : '#1677FF',
          borderColor: '#fff',
          borderWidth: 2,
        },
        children: (node.children || []).map(buildEchartData),
      })

      this.chartInstance.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            const d = params.data.rawData
            if (!d) return ''
            return `<b>${d.companyName}</b><br/>层级：第${d.equityLevel}级<br/>持股：${d.equityRatio}%<br/>状态：${d.businessStatus === 'LOSS' ? '亏损' : '正常'}`
          }
        },
        series: [{
          type: 'tree',
          data: [buildEchartData(this.treeData)],
          left: '2%', right: '2%', top: '8%', bottom: '10%',
          layout: 'orthogonal', orient: 'TB',
          symbol: 'circle', symbolSize: 14,
          expandAndCollapse: true, initialTreeDepth: this.maxDepth,
          label: {
            position: 'bottom', rotate: 0, verticalAlign: 'middle',
            fontSize: 11, overflow: 'breakAll', width: 80,
          },
          lineStyle: { color: '#B0C4DE', width: 1.5 },
        }]
      })

      this.chartInstance.on('click', (params) => {
        if (params.data && params.data.rawData) {
          this.currentNode = params.data.rawData
          this.nodeDialogVisible = true
        }
      })
    },
    showDetail(row) {
      this.currentNode = row
      this.nodeDialogVisible = true
    },
    /** 打开产权台账弹窗 */
    async openRegistryDialog() {
      if (!this.currentNode || !this.currentNode.companyId) {
        this.$message.warning('当前节点无企业编码信息')
        return
      }
      this.registryDialogVisible = true
      this.registryLoading = true
      this.registryData = null
      try {
        const res = await getEquityRegistryDetail(this.currentNode.companyId)
        this.registryData = res.data || null
      } catch (e) {
        this.registryData = null
        this.$message.error('获取产权台账信息失败')
      }
      this.registryLoading = false
    },
    /** 格式化金额（万元） */
    formatAmount(val) {
      if (val === null || val === undefined) return '—'
      return Number(val).toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },
    /** 产权类型标签 */
    rightTypeLabel(v) {
      return { SOLE: '国有独资', HOLDING: '国有控股', PARTICIPATING: '国有参股' }[v] || v || '—'
    },
    /** 登记状态标签 */
    regStatusLabel(v) {
      return { REGISTERED: '已登记', PENDING: '待登记', CHANGING: '变更中' }[v] || v || '—'
    },
    rowClass({ row }) {
      if (row.isWarning || row.equityLevel > 5) return 'row-danger'
      if (row.businessStatus === 'LOSS') return 'row-warning'
      return ''
    },
    bsLabel(v) { return { NORMAL: '正常', LOSS: '亏损', LIQUIDATION: '清算中', CANCELLED: '已注销' }[v] || v },
    bsType(v) { return { NORMAL: 'success', LOSS: 'warning', LIQUIDATION: 'info', CANCELLED: 'info' }[v] || '' },
  }
}
</script>

<style scoped>
.equity-chart { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
}
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.ctrl-card { margin-bottom: 12px; }
.ctrl-bar { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.ctrl-label { font-size: 13px; color: #666; white-space: nowrap; }
.depth-val { font-size: 13px; font-weight: 600; color: #1677FF; }
.chart-card, .table-card { }
.legend-row { display: flex; gap: 20px; margin-bottom: 12px; }
.legend-item { display: flex; align-items: center; gap: 6px; font-size: 12px; color: #555; }
.dot { width: 12px; height: 12px; border-radius: 50%; display: inline-block; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
</style>
