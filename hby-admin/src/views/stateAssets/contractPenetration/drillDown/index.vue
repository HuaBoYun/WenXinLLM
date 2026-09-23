<template>
  <div class="drill-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同穿透分析</div>
        <div class="banner-sub">从集团视角逐层穿透：集团总览 → 企业合同分类 → 类型合同列表 → 单份合同详情</div>
      </div>
      <div class="banner-right">
        <el-button size="small" icon="el-icon-refresh" class="banner-btn" @click="fetchDrillData">刷新数据</el-button>
        <el-button size="small" icon="el-icon-download" class="banner-btn" :loading="exportLoading" @click="handleExport">导出报告</el-button>
        <el-tag effect="dark" size="medium" :type="levelTagType" style="margin-left:12px">{{ levelLabel }}</el-tag>
      </div>
    </div>

    <!-- 面包屑 + 步骤条 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <el-row align="middle" type="flex">
        <el-col :span="14">
          <el-button v-if="level>0" type="text" icon="el-icon-back" style="margin-right:12px;font-size:14px" @click="goLevel(level-1)">返回上级</el-button>
          <el-breadcrumb separator="/" style="font-size:14px;display:inline-block">
            <el-breadcrumb-item><a @click="goLevel(0)" style="cursor:pointer;color:#1677FF">集团总览</a></el-breadcrumb-item>
            <el-breadcrumb-item v-if="level>=1"><a @click="goLevel(1)" style="cursor:pointer;color:#1677FF">{{ selectedEnterprise && selectedEnterprise.name }}</a></el-breadcrumb-item>
            <el-breadcrumb-item v-if="level>=2"><a @click="goLevel(2)" style="cursor:pointer;color:#1677FF">{{ selectedType && selectedType.type }}</a></el-breadcrumb-item>
            <el-breadcrumb-item v-if="level>=3">{{ selectedContract && selectedContract.contractName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col :span="10">
          <el-steps :active="level" simple style="background:transparent">
            <el-step title="集团层" icon="el-icon-office-building"/>
            <el-step title="企业层" icon="el-icon-s-custom"/>
            <el-step title="类型层" icon="el-icon-document-copy"/>
            <el-step title="合同层" icon="el-icon-document"/>
          </el-steps>
        </el-col>
      </el-row>
    </el-card>

    <!-- 第0层：集团企业卡片总览 -->
    <div v-if="level===0">
      <el-row :gutter="16">
        <el-col v-for="ent in drillData.enterprises" :key="ent.name" :span="8" style="margin-bottom:16px">
          <el-card shadow="hover" class="ent-card" @click.native="drillToEnterprise(ent)">
            <div class="ent-header">
              <span class="ent-name">{{ ent.name }}</span>
              <el-tag :type="riskTagType(ent.riskLevel)" size="small">{{ riskLabel(ent.riskLevel) }}</el-tag>
              <el-badge v-if="ent.warnCount>0" :value="ent.warnCount" class="warn-badge">
                <i class="el-icon-bell" style="color:#F5222D;font-size:16px"></i>
              </el-badge>
            </div>
            <div class="ent-stats">
              <div class="ent-stat-item">
                <span class="ent-stat-num" style="color:#1677FF">{{ ent.totalContracts }}</span>
                <span class="ent-stat-label">合同总量</span>
              </div>
              <div class="ent-stat-item">
                <span class="ent-stat-num" style="color:#0050A0">{{ ent.totalAmount }}</span>
                <span class="ent-stat-label">金额(亿)</span>
              </div>
              <div class="ent-stat-item">
                <span class="ent-stat-num" :style="{color:ent.complianceRate>=95?'#52C41A':'#FA8C16'}">{{ ent.complianceRate }}%</span>
                <span class="ent-stat-label">合规率</span>
              </div>
            </div>
            <div style="margin-top:10px">
              <el-progress :percentage="ent.complianceRate" :stroke-width="6"
                :color="ent.complianceRate>=95?'#52C41A':ent.complianceRate>=90?'#FA8C16':'#F5222D'"/>
            </div>
            <div style="margin-top:12px;text-align:right">
              <el-button v-if="ent.warnCount>0" type="warning" size="mini" plain icon="el-icon-bell" @click.stop="showWarningDialog(ent)">查看预警({{ ent.warnCount }})</el-button>
              <el-button type="primary" size="mini" plain icon="el-icon-arrow-right" @click.stop="drillToEnterprise(ent)">穿透查看</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 第1层：企业合同分类 -->
    <div v-if="level===1 && selectedEnterprise">
      <el-row :gutter="16">
        <el-col :span="16">
          <el-card shadow="never">
            <div slot="header" class="card-header">
              <span>{{ selectedEnterprise.name }} — 合同类型分布</span>
            </div>
            <el-table :data="selectedEnterprise.contractTypes||[]" border size="small">
              <el-table-column label="合同类型" prop="type" width="120">
                <template slot-scope="{row}">
                  <span class="tag-pill" :style="contractTypeTagStyle(row.type)">{{ row.type }}</span>
                </template>
              </el-table-column>
              <el-table-column label="合同数量" prop="count" width="100" align="center">
                <template slot-scope="{row}"><span style="color:#1677FF;font-weight:600">{{ row.count }}</span></template>
              </el-table-column>
              <el-table-column label="合同金额(亿)" prop="amount" width="120" align="right">
                <template slot-scope="{row}"><span style="font-weight:600;color:#0050A0">{{ row.amount }}</span></template>
              </el-table-column>
              <el-table-column label="合规率" prop="complianceRate" width="120">
                <template slot-scope="{row}">
                  <el-progress :percentage="row.complianceRate" :stroke-width="6"
                    :color="row.complianceRate>=95?'#52C41A':row.complianceRate>=90?'#FA8C16':'#F5222D'"/>
                </template>
              </el-table-column>
              <el-table-column label="风险评分" prop="riskScore" width="90" align="center">
                <template slot-scope="{row}">
                  <span :style="{fontWeight:'700',color:row.riskScore>=70?'#F5222D':row.riskScore>=50?'#FA8C16':'#52C41A'}">{{ row.riskScore }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="{row}">
                  <el-button v-if="row.contracts&&row.contracts.length" type="text" size="mini" icon="el-icon-arrow-right" @click="drillToType(row)">穿透</el-button>
                  <span v-else style="color:#ccc;font-size:12px">无明细</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div slot="header" class="card-header"><span>企业概况</span></div>
            <el-descriptions :column="1" size="small" border>
              <el-descriptions-item label="企业名称">{{ selectedEnterprise.name }}</el-descriptions-item>
              <el-descriptions-item label="风险等级">
                <el-tag :type="riskTagType(selectedEnterprise.riskLevel)" size="mini">{{ riskLabel(selectedEnterprise.riskLevel) }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="合同总量">{{ selectedEnterprise.totalContracts }} 份</el-descriptions-item>
              <el-descriptions-item label="合同总额">{{ selectedEnterprise.totalAmount }} 亿元</el-descriptions-item>
              <el-descriptions-item label="综合合规率">{{ selectedEnterprise.complianceRate }}%</el-descriptions-item>
              <el-descriptions-item label="活跃预警">{{ selectedEnterprise.warnCount }} 条</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 第2层：合同类型明细 -->
    <div v-if="level===2 && selectedType">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ selectedEnterprise.name }} — {{ selectedType.type }} — 合同明细</span>
          <el-tag size="small" style="margin-left:8px">{{ (selectedType.contracts||[]).length }} 份</el-tag>
        </div>
        <el-table :data="selectedType.contracts||[]" border size="small">
          <el-table-column label="合同编号" prop="contractNo" width="160"/>
          <el-table-column label="合同名称" prop="contractName" min-width="200" show-overflow-tooltip/>
          <el-table-column label="合同金额(万)" prop="amount" width="130" align="right">
            <template slot-scope="{row}"><span style="font-weight:600;color:#0050A0">{{ row.amount.toLocaleString() }}</span></template>
          </el-table-column>
          <el-table-column label="履行状态" prop="status" width="110" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.status==='EXECUTING'?'primary':row.status==='DONE'?'success':'warning'" size="mini">
                {{ row.status==='EXECUTING'?'履行中':row.status==='DONE'?'已完成':'待履行' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="合规状态" prop="complianceStatus" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.complianceStatus==='COMPLIANT'?'success':'danger'" size="mini">
                {{ row.complianceStatus==='COMPLIANT'?'合规':'违规' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="履行进度" prop="progress" width="160">
            <template slot-scope="{row}">
              <el-progress :percentage="row.progress" :stroke-width="8" :color="row.progress>=80?'#52C41A':row.progress>=50?'#FA8C16':'#F5222D'"/>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="{row}">
              <el-button type="text" size="mini" icon="el-icon-view" @click="drillToContract(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 第3层：单份合同详情 -->
    <div v-if="level===3 && selectedContract">
      <el-row :gutter="16">
        <el-col :span="16">
          <el-card shadow="never">
            <div slot="header" class="card-header">
              <i class="el-icon-document" style="margin-right:6px;color:#1677FF"></i>
              <span>{{ selectedContract.contractName }}</span>
              <el-button type="primary" size="mini" style="margin-left:auto" icon="el-icon-document" @click="showContractDialog(selectedContract)">查看完整合同</el-button>
            </div>
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="合同编号">{{ selectedContract.contractNo }}</el-descriptions-item>
              <el-descriptions-item label="合同金额">{{ selectedContract.amount ? selectedContract.amount.toLocaleString() : 0 }} 万元</el-descriptions-item>
              <el-descriptions-item label="对方单位">{{ selectedContract.counterpartyName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="签订日期">{{ selectedContract.signDate || '-' }}</el-descriptions-item>
              <el-descriptions-item label="到期日期">{{ selectedContract.expiryDate || '-' }}</el-descriptions-item>
              <el-descriptions-item label="履行状态">
                <el-tag :type="selectedContract.status==='EXECUTING'?'primary':selectedContract.status==='DONE'?'success':'warning'" size="mini">
                  {{ selectedContract.status==='EXECUTING'?'履行中':selectedContract.status==='DONE'?'已完成':'待履行' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="合规状态">
                <el-tag :type="selectedContract.complianceStatus==='COMPLIANT'?'success':'danger'" size="mini">
                  {{ selectedContract.complianceStatus==='COMPLIANT'?'合规':'违规' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="履行进度" :span="2">
                <el-progress :percentage="selectedContract.progress || 0" :stroke-width="12" style="width:100%"/>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never">
            <div slot="header" class="card-header"><span>穿透路径</span></div>
            <el-steps direction="vertical" :active="4" style="padding:10px 0">
              <el-step title="集团层" :description="'示例集团总部'" status="finish" icon="el-icon-office-building"/>
              <el-step title="企业层" :description="selectedEnterprise && selectedEnterprise.name" status="finish" icon="el-icon-s-custom"/>
              <el-step title="合同类型层" :description="selectedType && selectedType.type" status="finish" icon="el-icon-document-copy"/>
              <el-step title="合同层" :description="selectedContract.contractNo" status="finish" icon="el-icon-document"/>
            </el-steps>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 预警弹窗 -->
    <el-dialog :title="'预警信息 — ' + (warningEntName || '')" :visible.sync="warningDialogVisible" width="700px" top="8vh">
      <div v-loading="warningLoading">
        <el-alert v-if="warningList.length===0 && !warningLoading" title="该企业暂无预警记录" type="info" :closable="false" show-icon/>
        <el-table v-else :data="warningList" border size="small" max-height="400">
          <el-table-column label="预警编号" prop="warnNo" width="140"/>
          <el-table-column label="预警内容" prop="warnType" min-width="180" show-overflow-tooltip/>
          <el-table-column label="预警等级" prop="level" width="90" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.level==='HIGH'?'danger':row.level==='MEDIUM'?'warning':'info'" size="mini">
                {{ row.level==='HIGH'?'高危':row.level==='MEDIUM'?'中危':'低危' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="90" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.status==='CLOSED'?'success':row.status==='PROCESSING'?'warning':'danger'" size="mini">
                {{ row.status==='CLOSED'?'已关闭':row.status==='PROCESSING'?'处置中':'待处置' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="触发时间" prop="warnTime" width="150"/>
        </el-table>
      </div>
    </el-dialog>

    <!-- 合同详情弹窗 -->
    <el-dialog :title="'合同详情 — ' + (contractDialogData.contractName || '')" :visible.sync="contractDialogVisible" width="700px" top="8vh">
      <el-descriptions :column="2" border size="small" v-if="contractDialogData">
        <el-descriptions-item label="合同编号">{{ contractDialogData.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="合同名称">{{ contractDialogData.contractName }}</el-descriptions-item>
        <el-descriptions-item label="合同金额">{{ contractDialogData.amount ? contractDialogData.amount.toLocaleString() : 0 }} 万元</el-descriptions-item>
        <el-descriptions-item label="对方单位">{{ contractDialogData.counterpartyName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="签订日期">{{ contractDialogData.signDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ contractDialogData.expiryDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所属企业">{{ selectedEnterprise ? selectedEnterprise.name : '-' }}</el-descriptions-item>
        <el-descriptions-item label="合同类型">{{ selectedType ? selectedType.type : '-' }}</el-descriptions-item>
        <el-descriptions-item label="履行状态">
          <el-tag :type="contractDialogData.status==='EXECUTING'?'primary':contractDialogData.status==='DONE'?'success':'warning'" size="mini">
            {{ contractDialogData.status==='EXECUTING'?'履行中':contractDialogData.status==='DONE'?'已完成':'待履行' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="合规状态">
          <el-tag :type="contractDialogData.complianceStatus==='COMPLIANT'?'success':'danger'" size="mini">
            {{ contractDialogData.complianceStatus==='COMPLIANT'?'合规':'违规' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="履行进度" :span="2">
          <el-progress :percentage="contractDialogData.progress || 0" :stroke-width="14" style="width:100%"
            :color="contractDialogData.progress>=80?'#52C41A':contractDialogData.progress>=50?'#FA8C16':'#F5222D'"/>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="contractDialogVisible=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getContractDrillData, exportDrillReport, getContractWarningList } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractDrillDown',
  data() {
    return {
      drillData: { enterprises: [] },
      level: 0,
      selectedEnterprise: null,
      selectedType: null,
      selectedContract: null,
      exportLoading: false,
      // 预警弹窗
      warningDialogVisible: false,
      warningLoading: false,
      warningEntName: '',
      warningList: [],
      // 合同详情弹窗
      contractDialogVisible: false,
      contractDialogData: {},
    }
  },
  computed: {
    levelLabel() {
      const labels = ['集团层', '企业层', '合同类型层', '合同层']
      return labels[this.level] || '集团层'
    },
    levelTagType() {
      const types = ['primary', 'success', 'warning', 'danger']
      return types[this.level] || 'primary'
    },
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
  created() { this.fetchDrillData() },
  methods: {
    async fetchDrillData() {
      try {
        const res = await getContractDrillData()
        if (res && res.result === 200 && res.data && res.data.enterprises && res.data.enterprises.length > 0) {
          const rawEnterprises = res.data.enterprises || []
          this.drillData = {
            enterprises: rawEnterprises.map(ent => {
              const total = ent.total || ent.totalContracts || 0
              const violationCount = ent.violationCount || 0
              const complianceRate = total > 0 ? Math.round((total - violationCount) / total * 100) : 100
              const rawAmount = parseFloat(ent.totalAmount) || 0
              const totalAmount = (rawAmount / 10000).toFixed(2)
              const types = ent.types || ent.contractTypes || []
              const contractTypes = types.map(t => {
                const typeContracts = (t.contracts || []).map(c => {
                  const amt = parseFloat(c.amount) || 0
                  let status = 'EXECUTING'
                  const rawStatus = (c.status || '').toUpperCase()
                  if (['DONE', 'COMPLETED', 'FINISHED', 'TERMINATED'].some(s => rawStatus.includes(s))) {
                    status = 'DONE'
                  } else if (['DRAFT', 'PENDING', 'SIGNED'].some(s => rawStatus.includes(s))) {
                    status = 'PENDING'
                  }
                  const complianceStatus = c.complianceStatus || (c.risk === 'HIGH' ? 'VIOLATION' : 'COMPLIANT')
                  const progress = c.progress != null ? Number(c.progress) : (status === 'DONE' ? 100 : 0)
                  return {
                    ...c,
                    contractNo: c.contractNo || c.contractCode || '',
                    contractName: c.contractName || '',
                    amount: amt,
                    status,
                    complianceStatus,
                    progress,
                    counterpartyName: c.counterpartyName || '',
                    signDate: c.signDate || '',
                    expiryDate: c.expiryDate || '',
                  }
                })
                const typeCount = t.count || typeContracts.length
                const typeCompliant = typeContracts.filter(c => c.complianceStatus === 'COMPLIANT').length
                const typeComplianceRate = typeCount > 0 ? Math.round(typeCompliant / typeCount * 100) : 100
                const riskScore = t.riskScore != null ? t.riskScore : Math.max(0, 100 - typeComplianceRate)
                return {
                  type: t.typeName || t.type || '其他',
                  count: typeCount,
                  amount: (typeContracts.reduce((s, c) => s + c.amount, 0) / 10000).toFixed(2),
                  complianceRate: typeComplianceRate,
                  riskScore: Math.min(riskScore, 100),
                  contracts: typeContracts
                }
              })
              return {
                name: ent.name || '未知企业',
                totalContracts: total,
                totalAmount,
                complianceRate,
                riskLevel: ent.riskLevel || 'LOW',
                warnCount: violationCount,
                contractTypes
              }
            })
          }
        } else {
          this.drillData = { enterprises: [] }
          this.$message.info('暂无穿透数据')
        }
      } catch (e) {
        console.error('穿透数据加载失败', e)
        this.drillData = { enterprises: [] }
        this.$message.error('穿透数据加载失败')
      }
    },
    // ========== 导出穿透报告 ==========
    async handleExport() {
      this.exportLoading = true
      try {
        const res = await exportDrillReport({})
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'text/csv;charset=utf-8' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `合同穿透分析报告_${new Date().toISOString().slice(0, 10)}.csv`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }
      } catch (e) {
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
    },
    // ========== 预警弹窗 ==========
    async showWarningDialog(ent) {
      this.warningEntName = ent.name
      this.warningList = []
      this.warningDialogVisible = true
      this.warningLoading = true
      try {
        const res = await getContractWarningList({ companyName: ent.name, pageNumber: 1, pageSize: 50 })
        if (res && res.result === 200 && res.data) {
          this.warningList = res.data.tlist || res.data.list || []
        }
      } catch (e) {
        this.$message.error('获取预警数据失败')
      } finally {
        this.warningLoading = false
      }
    },
    // ========== 合同详情弹窗 ==========
    showContractDialog(contract) {
      this.contractDialogData = contract || {}
      this.contractDialogVisible = true
    },
    // ========== 层级导航 ==========
    goLevel(l) {
      this.level = l
      if (l === 0) { this.selectedEnterprise = null; this.selectedType = null; this.selectedContract = null }
      if (l === 1) { this.selectedType = null; this.selectedContract = null }
      if (l === 2) { this.selectedContract = null }
    },
    drillToEnterprise(ent) { this.selectedEnterprise = ent; this.level = 1 },
    drillToType(t) { this.selectedType = t; this.level = 2 },
    drillToContract(c) { this.selectedContract = c; this.level = 3 },
    riskLabel(r) { const m = { LOW: '低风险', MEDIUM: '中风险', HIGH: '高风险' }; return m[r] || r },
    riskTagType(r) { const m = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger' }; return m[r] || '' },
    contractTypeTagStyle(v) {
      const map = {
        '采购合同': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' },
        '销售合同': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '工程合同': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '租赁合同': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '金融合同': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '劳务合同': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' },
        '服务合同': { background: '#F0FFF4', color: '#389E0D', border: '1px solid #95DE64' },
      }
      return map[v] || {}
    },
  },
}
</script>

<style scoped lang="scss">
.drill-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.75); }
  .banner-right { display: flex; align-items: center; gap: 8px; }
}
.banner-btn {
  background: rgba(255,255,255,0.15);
  border: 1px solid rgba(255,255,255,0.5);
  color: #fff;
  &:hover { background: rgba(255,255,255,0.3); border-color: #fff; color: #fff; }
  &:focus { background: rgba(255,255,255,0.15); border-color: rgba(255,255,255,0.5); color: #fff; }
}

.ent-card { border-radius: 8px; cursor: pointer; transition: all 0.25s;
  &:hover { transform: translateY(-3px); box-shadow: 0 6px 20px rgba(22,119,255,0.18); }
}
.ent-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px;
  .ent-name { font-size: 15px; font-weight: 700; color: #1a1a2e; flex: 1; }
  .warn-badge { margin-left: auto; }
}
.ent-stats { display: flex; gap: 0; justify-content: space-around; }
.ent-stat-item { text-align: center;
  .ent-stat-num { display: block; font-size: 20px; font-weight: 700; }
  .ent-stat-label { font-size: 11px; color: #999; }
}

.card-header { display: flex; align-items: center; font-weight: 600; }
.tag-pill { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; }

::v-deep .el-steps--simple { background: transparent; }
</style>
