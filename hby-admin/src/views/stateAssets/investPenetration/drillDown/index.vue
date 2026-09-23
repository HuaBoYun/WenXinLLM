<template>
  <div class="drill-page" :style="themeVars">
    <!-- 页面标题 -->
    <div class="page-banner">
      <div class="banner-left">
        <i class="el-icon-share banner-icon" />
        <div>
          <h2>投资穿透分析</h2>
          <p>多层股权穿透，可视化呈现投资链路，核查最终受益人与控制结构</p>
        </div>
      </div>
    </div>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="穿透企业">
          <el-select v-model="queryForm.company" placeholder="请选择企业" style="width:140px" @change="handleCompanyChange">
            <el-option v-for="c in companyList" :key="c.value" :label="c.label" :value="c.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="穿透项目">
          <el-select v-model="queryForm.projectId" placeholder="请选择项目" style="width:220px" @change="handleProjectChange">
            <el-option v-for="p in projectOptions" :key="p.value" :label="p.label" :value="p.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="穿透层级">
          <el-select v-model="queryForm.maxLevel" style="width:100px">
            <el-option label="3层" :value="3" />
            <el-option label="5层" :value="5" />
            <el-option label="全部" :value="99" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleDrill">开始穿透</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16" style="margin-top:12px">
      <!-- 穿透树形结构 -->
      <el-col :span="10">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-grid" /> 股权穿透树</span>
            <el-tag size="mini" type="warning">共 {{ drillStats.totalNodes }} 个主体</el-tag>
          </div>
          <el-tree
            :data="treeData"
            :props="treeProps"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
          >
            <div class="custom-tree-node" slot-scope="{ node, data }">
              <div class="node-main">
                <i :class="data.type === 'ROOT' ? 'el-icon-office-building' : data.type === 'SUBSIDIARY' ? 'el-icon-s-shop' : 'el-icon-user'" :style="{color: data.type === 'ROOT' ? ipPrimary : data.type === 'SUBSIDIARY' ? ipSecondary : ipAccent}" />
                <span class="node-name">{{ data.label }}</span>
              </div>
              <div class="node-meta">
                <el-tag size="mini" :type="data.isMainBiz === false ? 'warning' : 'info'">{{ data.isMainBiz === false ? '非主业' : '主业' }}</el-tag>
                <span class="node-ratio">{{ data.shareholdRatio }}</span>
                <el-tag v-if="data.hasAlert" size="mini" type="danger">预警</el-tag>
              </div>
            </div>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 穿透详情 -->
      <el-col :span="14">
        <!-- 被选节点信息 -->
        <el-card shadow="never" class="detail-card" v-if="selectedNode">
          <div slot="header" class="card-header">
            <span><i class="el-icon-info" /> 主体详情：{{ selectedNode.label }}</span>
            <el-tag :type="selectedNode.isMainBiz === false ? 'warning' : 'success'" size="small">{{ selectedNode.isMainBiz === false ? '非主业投资' : '主业投资' }}</el-tag>
          </div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="企业名称">{{ selectedNode.label }}</el-descriptions-item>
            <el-descriptions-item label="股权比例">{{ selectedNode.shareholdRatio }}</el-descriptions-item>
            <el-descriptions-item label="企业类型">{{ selectedNode.companyType }}</el-descriptions-item>
            <el-descriptions-item label="注册资本">{{ selectedNode.regCapital }}</el-descriptions-item>
            <el-descriptions-item label="投资金额">{{ selectedNode.investAmount }}</el-descriptions-item>
            <el-descriptions-item label="账面价值">{{ selectedNode.bookValue }}</el-descriptions-item>
            <el-descriptions-item label="实际收益率">
              <span :style="{color: selectedNode.returnAlert ? '#FF4D4F' : '#52C41A', fontWeight:600}">{{ selectedNode.actualReturn }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="经营状态">
              <el-tag :type="selectedNode.bizStatus === '正常' ? 'success' : 'danger'" size="mini">{{ selectedNode.bizStatus }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="穿透层级">第 {{ selectedNode.level }} 层</el-descriptions-item>
            <el-descriptions-item label="合规状态">
              <el-tag :type="selectedNode.complianceOk ? 'success' : 'danger'" size="mini">{{ selectedNode.complianceOk ? '合规' : '存在问题' }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <div v-if="selectedNode.alerts && selectedNode.alerts.length" style="margin-top:10px">
            <el-alert v-for="a in selectedNode.alerts" :key="a" :title="a" type="warning" show-icon :closable="false" style="margin-bottom:6px" />
          </div>
        </el-card>

        <!-- 穿透统计 -->
        <el-card shadow="never" style="margin-top:12px">
          <div slot="header" class="card-header"><span><i class="el-icon-s-data" /> 穿透分析摘要</span></div>
          <el-row :gutter="12">
            <el-col :span="8" v-for="s in drillSummary" :key="s.label">
              <div class="summary-item" :style="{borderLeftColor: s.color}">
                <div class="si-value" :style="{color: s.color}">{{ s.value }}</div>
                <div class="si-label">{{ s.label }}</div>
              </div>
            </el-col>
          </el-row>
          <div style="margin-top:12px">
            <div class="section-title">穿透发现问题</div>
            <el-table :data="drillIssues" size="small" border>
              <el-table-column label="问题类型" prop="issueType" width="110" />
              <el-table-column label="涉及主体" prop="entity" width="130" />
              <el-table-column label="问题描述" prop="desc" show-overflow-tooltip />
              <el-table-column label="风险级别" width="80" align="center">
                <template slot-scope="{row}">
                  <el-tag :type="row.risk === 'HIGH' ? 'danger' : row.risk === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ row.riskLabel }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getInvestDrillData, getInvestProjectList } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

export default {
  name: 'InvestDrillDown',
  mixins: [investThemeMixin],
  data() {
    return {
      queryForm: { company: '', projectId: '', maxLevel: 5 },
      companyList: [
        { value: 'HBNY', label: '示例能源' },
        { value: 'HBKJ', label: '示例科技' },
        { value: 'HBJR', label: '示例金融' },
        { value: 'HBDC', label: '示例地产' },
        { value: 'HBWL', label: '示例物流' },
      ],
      projectOptions: [],
      treeProps: { children: 'children', label: 'label' },
      selectedNode: null,
      treeData: [],
      drillStats: { totalNodes: 0 },
      drillSummary: [],
      drillIssues: [],
    }
  },
  created() {
    if (this.$route.query.projectId) this.queryForm.projectId = this.$route.query.projectId
    if (this.$route.query.company) this.queryForm.company = this.$route.query.company
    this.loadProjectOptions()
    this.handleDrill()
  },
  methods: {
    async loadOptions() {
      try {
        const res = await getInvestProjectList({ pageNumber: 1, pageSize: 200 })
        if (res && res.result === 200 && res.data) {
          const projects = res.data.tlist || []
          const companies = [...new Map(projects.map(p => [p.companyId, { value: p.companyId, label: p.companyName }])).values()]
          this.companyList = companies
          this.projectOptions = projects.map(p => ({ value: p.projectId, label: p.projectName }))
        }
      } catch (e) { console.error('加载选项失败:', e) }
    },
    async handleDrill() {
      try {
        const params = {}
        if (this.queryForm.company) params.companyId = this.queryForm.company
        const res = await getInvestDrillData(params)
        if (res && res.result === 200 && res.data) {
          const data = res.data
          const children = data.children || []
          // Build tree: Root -> Companies -> Projects
          const companyGroups = {}
          children.forEach(p => {
            const cName = p.companyName || '未知企业'
            if (!companyGroups[cName]) {
              companyGroups[cName] = {
                id: 'C-' + cName, label: cName, type: 'SUBSIDIARY',
                shareholdRatio: '-', isMainBiz: null,
                companyType: '控股企业', regCapital: '-',
                investAmount: '-', bookValue: '-',
                actualReturn: '-', returnAlert: false,
                bizStatus: '正常', complianceOk: true,
                level: 1, hasAlert: false, alerts: [], children: [],
              }
            }
            const isNonMain = p.isMainBiz === 'N'
            const actualReturn = p.actualReturn != null ? Number(p.actualReturn) : null
            const expectedReturn = p.expectedReturn != null ? Number(p.expectedReturn) : null
            const returnAlert = actualReturn != null && expectedReturn != null && actualReturn < expectedReturn * 0.5
            const hasAlert = isNonMain || returnAlert
            const alerts = []
            if (returnAlert) alerts.push('实际收益率严重偏离预期')
            if (isNonMain) alerts.push('属于非主业投资标的')
            companyGroups[cName].children.push({
              id: p.id || p.projectId,
              label: p.name || p.projectName,
              type: 'ENTITY',
              shareholdRatio: p.investType === 'EQUITY' ? '参股' : '债权',
              isMainBiz: !isNonMain,
              companyType: p.investType === 'EQUITY' ? '股权投资' : '债权投资',
              regCapital: '-',
              investAmount: p.investAmount ? (p.investAmount + '万') : '-',
              bookValue: '-',
              actualReturn: actualReturn != null ? (actualReturn + '%') : '-',
              returnAlert,
              bizStatus: '正常',
              complianceOk: !hasAlert,
              level: 2,
              hasAlert,
              alerts,
              children: [],
            })
            if (hasAlert) companyGroups[cName].hasAlert = true
            if (hasAlert) companyGroups[cName].complianceOk = false
          })
          const rootChildren = Object.values(companyGroups)
          this.treeData = [{
            id: 'ROOT', label: data.name || '集团总部', type: 'ROOT',
            shareholdRatio: '100%（集团直接控股）', isMainBiz: null,
            companyType: '国有控股公司', regCapital: '-',
            investAmount: '-', bookValue: '-',
            actualReturn: '-', returnAlert: false,
            bizStatus: '正常', complianceOk: true,
            level: 0, hasAlert: rootChildren.some(c => c.hasAlert),
            alerts: [], children: rootChildren,
          }]
          const stats = data.stats || {}
          this.drillStats = { totalNodes: stats.totalNodes || 0 }
          const alertCount = children.filter(p => {
            const ar = p.actualReturn != null ? Number(p.actualReturn) : null
            const er = p.expectedReturn != null ? Number(p.expectedReturn) : null
            return p.isMainBiz === 'N' || (ar != null && er != null && ar < er * 0.5)
          }).length
          const complianceIssues = children.filter(p => p.isMainBiz === 'N').length
          this.drillSummary = [
            { label: '穿透层级', value: '3层', color: this.ipPrimary },
            { label: '涉及主体', value: (stats.totalNodes || 0) + '个', color: this.ipSecondary },
            { label: '非主业主体', value: (stats.nonMainBizCount || 0) + '个', color: '#FA8C16' },
            { label: '存在预警', value: alertCount + '个', color: '#FF4D4F' },
            { label: '合规问题', value: complianceIssues + '个', color: '#FF4D4F' },
            { label: '最终受益人', value: '国资委', color: '#52C41A' },
          ]
          this.drillIssues = children.filter(p => {
            const ar = p.actualReturn != null ? Number(p.actualReturn) : null
            const er = p.expectedReturn != null ? Number(p.expectedReturn) : null
            return p.isMainBiz === 'N' || (ar != null && er != null && ar < er * 0.5)
          }).map(p => {
            const issues = []
            if (p.isMainBiz === 'N') {
              issues.push({ issueType: '非主业超限', entity: p.name || p.companyName, desc: '属于非主业投资标的', risk: 'MEDIUM', riskLabel: '中' })
            }
            const ar = p.actualReturn != null ? Number(p.actualReturn) : null
            const er = p.expectedReturn != null ? Number(p.expectedReturn) : null
            if (ar != null && er != null && ar < er * 0.5) {
              issues.push({ issueType: '收益严重偏离', entity: p.name || p.companyName, desc: `实际收益率${p.actualReturn}%，较预期${p.expectedReturn}%偏差显著`, risk: 'HIGH', riskLabel: '高' })
            }
            return issues
          }).flat()
          if (this.treeData.length > 0) this.selectedNode = this.treeData[0]
        }
      } catch (e) {
        console.error('穿透分析失败:', e)
      }
    },
    handleCompanyChange() { this.selectedNode = null },
    handleProjectChange() { this.selectedNode = null },
    async loadProjectOptions() {
      try {
        const res = await getInvestProjectList({ pageNumber: 1, pageSize: 100 })
        if (res.result === 200 && res.data) {
          this.projectOptions = (res.data.tlist || []).map(p => ({ value: p.projectId, label: p.projectName }))
        }
      } catch (e) {
        console.error('加载项目列表失败', e)
      }
    },
    async handleDrill() {
      try {
        const params = {}
        if (this.queryForm.company) params.companyId = this.queryForm.company
        const res = await getInvestDrillData(params)
        if (res.result === 200 && res.data) {
          const data = res.data
          const children = (data.children || []).map(c => ({
            id: c.id,
            label: c.name,
            type: 'SUBSIDIARY',
            shareholdRatio: c.investAmount ? c.investAmount + '万元' : '-',
            isMainBiz: c.isMainBiz === 'Y' ? true : c.isMainBiz === 'N' ? false : null,
            companyType: c.investType === 'EQUITY' ? '股权投资' : c.investType === 'DEBT' ? '债权投资' : '其他',
            regCapital: '-',
            investAmount: (c.investAmount || 0) + '万元',
            bookValue: '-',
            actualReturn: (c.actualReturn || '-') + '%',
            returnAlert: c.actualReturn && c.expectedReturn && c.actualReturn < c.expectedReturn * 0.7,
            bizStatus: '正常',
            complianceOk: true,
            level: 1,
            hasAlert: false,
            alerts: [],
            children: [],
          }))
          this.treeData = [{
            id: 'ROOT',
            label: data.name || '集团总部',
            type: 'ROOT',
            shareholdRatio: '100%',
            isMainBiz: null,
            companyType: '国有控股公司',
            regCapital: '-',
            investAmount: '-',
            bookValue: '-',
            actualReturn: '-',
            returnAlert: false,
            bizStatus: '正常',
            complianceOk: true,
            level: 0,
            hasAlert: false,
            alerts: [],
            children,
          }]
          this.selectedNode = this.treeData[0]
          this.drillStats = data.stats || { totalNodes: children.length + 1 }
          this.drillSummary = [
            { label: '穿透层级', value: '2层', color: this.ipPrimary },
            { label: '涉及主体', value: (children.length + 1) + '个', color: this.ipSecondary },
            { label: '非主业主体', value: (data.stats && data.stats.nonMainBizCount || 0) + '个', color: '#FA8C16' },
            { label: '存在预警', value: '0个', color: '#FF4D4F' },
            { label: '合规问题', value: '0个', color: '#FF4D4F' },
            { label: '最终受益人', value: '国资委', color: '#52C41A' },
          ]
          this.drillIssues = children.filter(c => c.returnAlert).map(c => ({
            issueType: '收益偏离',
            entity: c.label,
            desc: '实际收益率低于预期',
            risk: 'MEDIUM',
            riskLabel: '中',
          }))
          this.$message.success('穿透分析完成，共发现 ' + this.drillStats.totalNodes + ' 个主体')
        }
      } catch (e) {
        console.error('穿透分析失败', e)
        this.$message.error('穿透分析失败')
      }
    },
    resetQuery() {
      this.queryForm = { company: '', projectId: '', maxLevel: 5 }
      this.selectedNode = null
      this.treeData = []
      this.drillStats = { totalNodes: 0 }
      this.drillSummary = []
      this.drillIssues = []
    },
    handleNodeClick(data) {
      this.selectedNode = data
    },
  },
}
</script>

<style lang="scss" scoped>
.drill-page { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; padding: 16px 24px; margin-bottom: 12px; color: #fff;
  .banner-left { display: flex; align-items: center; gap: 14px; }
  .banner-icon { font-size: 32px; color: var(--ip-accent, #FAAD14); }
  h2 { margin: 0; font-size: 18px; }
  p { margin: 4px 0 0; font-size: 12px; opacity: 0.8; }
}
.query-card { border-left: 3px solid var(--ip-primary, #1A3A6B); }
.query-card .el-form-item { margin-bottom: 0; }
.card-header {
  display: flex; justify-content: space-between; align-items: center;
  font-size: 14px; font-weight: 600; color: var(--ip-primary, #1A3A6B);
  padding-bottom: 6px; border-bottom: 2px solid var(--ip-accent, #FAAD14);
}
.tree-card { min-height: 400px; }
.custom-tree-node {
  flex: 1; display: flex; justify-content: space-between; align-items: center;
  padding: 4px 0; font-size: 13px;
  .node-main { display: flex; align-items: center; gap: 6px; }
  .node-name { color: #333; }
  .node-meta { display: flex; align-items: center; gap: 6px; }
  .node-ratio { font-size: 12px; color: #888; }
}
::v-deep .el-tree-node__content { height: auto; padding: 4px 0; }
::v-deep .el-tree-node__content:hover { background: #f0f5ff; }
.detail-card {}
.summary-item {
  border-left: 4px solid var(--ip-primary, #1A3A6B);
  padding: 8px 12px;
  background: #fafafa;
  border-radius: 4px;
  margin-bottom: 8px;
  .si-value { font-size: 18px; font-weight: 700; }
  .si-label { font-size: 12px; color: #888; }
}
.section-title { font-size: 13px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 8px; }
</style>
