<template>
  <div class="smart-finance-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">财务共享监管平台</h1>
          <p class="welcome-desc">财务决策 · 智能分析 · 应收应付 · 收入费用 · 集团管控 · AI财务审查</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-data-analysis" round @click="$router.push('/znfx/cwztfx/cwzl')">财务总览</el-button>
            <el-button icon="el-icon-cpu" round @click="$router.push('/znfx/tzfx/tzfx')">AI 财务助手</el-button>
            <el-button icon="el-icon-document" round @click="$router.push('/financialSharing/reports/index')">报表分析</el-button>
          </div>
        </div>
        <div class="welcome-stats">
          <div class="ws-item" v-for="s in welcomeStats" :key="s.label">
            <div class="ws-value" :style="{color: s.color}">{{ s.value }}</div>
            <div class="ws-label">{{ s.label }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== KPI 数据卡片 ========== -->
    <div class="kpi-section">
      <div class="kpi-card" v-for="k in kpiCards" :key="k.label" :class="k.theme">
        <div class="kpi-top">
          <div class="kpi-icon-wrap" :style="{background: k.iconBg}">
            <i :class="k.icon" :style="{color: k.iconColor}" />
          </div>
          <div class="kpi-trend" v-if="k.trend" :class="k.trend > 0 ? 'trend-up' : 'trend-down'">
            <i :class="k.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'" />
            <span>{{ Math.abs(k.trend) }}%</span>
          </div>
        </div>
        <div class="kpi-value">{{ k.value }}<span class="kpi-unit">{{ k.unit }}</span></div>
        <div class="kpi-label">{{ k.label }}</div>
        <div class="kpi-bar" :style="{background: k.barBg}">
          <div class="kpi-bar-fill" :style="{width: k.barWidth + '%', background: k.barColor}" />
        </div>
      </div>
    </div>

    <!-- ========== 财务共享全链路 ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-sort" /> 财务共享全链路</div>
        <div class="section-legend">
          <span><i class="dot green" />正常</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />预警</span>
        </div>
      </div>
      <div class="lifecycle-track">
        <div v-for="(node, idx) in lifecycleNodes" :key="node.key" class="lc-step">
          <div class="lc-node" :class="node.status" @click="jumpTo(node.route)">
            <div class="lc-icon"><i :class="node.icon" /></div>
            <div class="lc-label">{{ node.name }}</div>
            <div class="lc-num" v-if="node.count">{{ node.count }}</div>
            <div class="lc-alert-dot" v-if="node.alert"></div>
          </div>
          <div v-if="idx < lifecycleNodes.length - 1" class="lc-connector">
            <div class="lc-line" :class="node.status === 'alert' ? 'line-alert' : ''"></div>
            <i class="el-icon-arrow-right lc-arrow" />
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 中部：财务决策 + 应收应付 + 财务数据 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 财务决策分析 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 财务决策分析</span>
            <span class="card-more" @click="jumpTo('/znfx/cwztfx/cwzl')">总览 →</span>
          </div>
          <el-tabs v-model="decisionActive" class="inner-tabs">
            <el-tab-pane name="profit">
              <span slot="label">利润分析</span>
              <div class="analysis-list">
                <div v-for="item in profitData" :key="item.name" class="analysis-item" @click="jumpTo('/znfx/cwztfx/lrfx')">
                  <div class="analysis-left">
                    <span class="analysis-name">{{ item.name }}</span>
                  </div>
                  <div class="analysis-right">
                    <span class="analysis-val" :style="{color: item.value >= 0 ? '#52C41A' : '#FF4D4F'}">{{ item.value >= 0 ? '+' : '' }}{{ item.value }}万</span>
                    <el-tag :type="item.trend > 0 ? 'success' : 'danger'" size="mini">{{ item.trend > 0 ? '↑' : '↓' }}{{ Math.abs(item.trend) }}%</el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="revenue">
              <span slot="label">收入分析</span>
              <div class="analysis-list">
                <div v-for="item in revenueData" :key="item.name" class="analysis-item" @click="jumpTo('/znfx/cwztfx/srfx')">
                  <div class="analysis-left">
                    <span class="analysis-name">{{ item.name }}</span>
                  </div>
                  <div class="analysis-right">
                    <span class="analysis-val">{{ item.value }}万</span>
                    <el-tag :type="item.trend > 0 ? 'success' : 'danger'" size="mini">{{ item.trend > 0 ? '↑' : '↓' }}{{ Math.abs(item.trend) }}%</el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="cost">
              <span slot="label">成本费用</span>
              <div class="analysis-list">
                <div v-for="item in costData" :key="item.name" class="analysis-item" @click="jumpTo('/znfx/cwztfx/cbfyfx')">
                  <div class="analysis-left">
                    <span class="analysis-name">{{ item.name }}</span>
                  </div>
                  <div class="analysis-right">
                    <span class="analysis-val">{{ item.value }}万</span>
                    <el-tag :type="item.trend <= 0 ? 'success' : 'danger'" size="mini">{{ item.trend > 0 ? '↑' : '↓' }}{{ Math.abs(item.trend) }}%</el-tag>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 应收应付概览 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-coin" /> 应收应付概览</span>
            <span class="card-more" @click="jumpTo('/znfx/cwztfx/ysyfzkfx')">详情 →</span>
          </div>
          <div class="ar-ap-section">
            <div class="ar-ap-block">
              <div class="ar-ap-title">应收管理</div>
              <div class="ar-ap-row" v-for="item in receivableStats" :key="item.label">
                <span class="ar-ap-label">{{ item.label }}</span>
                <span class="ar-ap-val" :style="{color: item.color}">{{ item.value }}万</span>
              </div>
            </div>
            <div class="ar-ap-divider"></div>
            <div class="ar-ap-block">
              <div class="ar-ap-title">应付管理</div>
              <div class="ar-ap-row" v-for="item in payableStats" :key="item.label">
                <span class="ar-ap-label">{{ item.label }}</span>
                <span class="ar-ap-val" :style="{color: item.color}">{{ item.value }}万</span>
              </div>
            </div>
          </div>
          <div class="ar-ap-bar-group">
            <div class="ar-ap-bar-item">
              <span class="bar-label">回款率</span>
              <div class="bar-bg"><div class="bar-fill" style="width:78%;background:#52C41A"></div></div>
              <span class="bar-val">78%</span>
            </div>
            <div class="ar-ap-bar-item">
              <span class="bar-label">付款率</span>
              <div class="bar-bg"><div class="bar-fill" style="width:65%;background:#1890FF"></div></div>
              <span class="bar-val">65%</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 财务数据入口 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-folder-opened" /> 财务数据</span>
            <span class="card-more" @click="jumpTo('/cwsc/jcpz/companyData/statement')">详情 →</span>
          </div>
          <div class="data-entry-list">
            <div v-for="d in financeDataEntries" :key="d.name" class="data-entry-item" @click="jumpTo(d.route)">
              <div class="de-icon" :style="{background: d.bgColor}">
                <i :class="d.icon" :style="{color: d.iconColor}" />
              </div>
              <div class="de-info">
                <div class="de-name">{{ d.name }}</div>
                <div class="de-desc">{{ d.desc }}</div>
              </div>
              <i class="el-icon-arrow-right de-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块 + 规则配置 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-grid" /> 功能模块</span>
          </div>
          <div class="module-grid">
            <div v-for="m in modules" :key="m.name" class="module-card" @click="jumpTo(m.route)">
              <div class="mc-icon" :style="{background: m.iconBg}">
                <i :class="m.icon" :style="{color: m.iconColor}" />
              </div>
              <div class="mc-name">{{ m.name }}</div>
              <div class="mc-desc">{{ m.desc }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-setting" /> 基础配置与规则</span>
          </div>
          <div class="config-grid">
            <div v-for="c in configItems" :key="c.name" class="config-card" @click="jumpTo(c.route)">
              <div class="cf-icon" :style="{background: c.bgColor}">
                <i :class="c.icon" :style="{color: c.iconColor}" />
              </div>
              <div class="cf-body">
                <div class="cf-name">{{ c.name }}</div>
                <div class="cf-desc">{{ c.desc }}</div>
              </div>
              <i class="el-icon-arrow-right cf-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== AI财务 + 外规内规 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> AI 财务智能体</span>
            <span class="card-more" @click="jumpTo('/znfx/tzfx/tzfx')">进入AI →</span>
          </div>
          <div class="ai-grid">
            <div v-for="ai in aiFeatures" :key="ai.name" class="ai-card" @click="jumpTo(ai.route)">
              <div class="ai-icon" :style="{background: ai.bgColor}">
                <i :class="ai.icon" :style="{color: ai.iconColor}" />
              </div>
              <div class="ai-body">
                <div class="ai-name">{{ ai.name }}</div>
                <div class="ai-desc">{{ ai.desc }}</div>
              </div>
              <i class="el-icon-arrow-right ai-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-reading" /> 外规内规与知识库</span>
          </div>
          <div class="law-grid">
            <div v-for="l in lawItems" :key="l.name" class="law-card" @click="jumpTo(l.route)">
              <div class="law-icon-wrap" :style="{background: l.bgColor}">
                <i :class="l.icon" :style="{color: l.iconColor}" />
              </div>
              <div class="law-body">
                <div class="law-name">{{ l.name }}</div>
                <div class="law-desc">{{ l.desc }}</div>
              </div>
              <i class="el-icon-arrow-right law-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'SmartFinanceHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      decisionActive: 'profit',
      welcomeStats: [
        { label: '本年营收', value: '8.56亿', color: '#1890FF' },
        { label: '利润总额', value: '1.23亿', color: '#52C41A' },
        { label: '应收余额', value: '2.18亿', color: '#FA8C16' },
        { label: '应付余额', value: '1.65亿', color: '#722ED1' },
        { label: '成本费用', value: '5.89亿', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '营业收入', value: '85,620', unit: '万元', icon: 'el-icon-coin', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 75, trend: 6.8, theme: '' },
        { label: '利润总额', value: '12,350', unit: '万元', icon: 'el-icon-data-line', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 62, trend: 3.2, theme: '' },
        { label: '应收余额', value: '21,800', unit: '万元', icon: 'el-icon-s-claim', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 45, trend: -2.4, theme: 'kpi-warning' },
        { label: '应付余额', value: '16,500', unit: '万元', icon: 'el-icon-wallet', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 38, trend: 1.1, theme: '' },
        { label: '成本费用', value: '58,900', unit: '万元', icon: 'el-icon-s-order', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 68, trend: 4.5, theme: '' },
        { label: '回款率', value: '78.3', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 78, trend: -0.8, theme: 'kpi-warning' },
      ],
      lifecycleNodes: [
        { key: 'data', name: '数据采集', icon: 'el-icon-download', status: 'normal', route: '/financialSharing/dataCollection/dataSource' },
        { key: 'account', name: '账簿处理', icon: 'el-icon-notebook-2', status: 'normal', count: '156', route: '/cwsc/jcpz/companyData/ZNFXaccountData' },
        { key: 'receivable', name: '应收管理', icon: 'el-icon-coin', status: 'warn', route: '/financialSharing/financial/receivables/index' },
        { key: 'payable', name: '应付管理', icon: 'el-icon-wallet', status: 'normal', route: '/financialSharing/financial/payables/index' },
        { key: 'revenue', name: '收入管理', icon: 'el-icon-data-line', status: 'normal', route: '/financialSharing/financial/revenueManagement/index' },
        { key: 'report', name: '报表分析', icon: 'el-icon-document', status: 'normal', route: '/financialSharing/reports/index' },
        { key: 'analysis', name: '智能分析', icon: 'el-icon-cpu', status: 'alert', alert: true, route: '/znfx/ai/index' },
        { key: 'decision', name: '财务决策', icon: 'el-icon-s-data', status: 'normal', route: '/znfx/cwztfx/cwzl' },
      ],
      profitData: [
        { name: '营业利润', value: 12850, trend: 5.2 },
        { name: '利润总额', value: 12350, trend: 3.2 },
        { name: '净利润', value: 9260, trend: 4.8 },
        { name: '其他综合收益', value: 560, trend: -1.5 },
      ],
      revenueData: [
        { name: '主营业务收入', value: 68920, trend: 7.1 },
        { name: '其他业务收入', value: 12800, trend: 2.3 },
        { name: '投资收益', value: 3900, trend: 8.6 },
      ],
      costData: [
        { name: '营业成本', value: 42500, trend: 5.3 },
        { name: '管理费用', value: 8200, trend: 3.1 },
        { name: '销售费用', value: 5600, trend: 2.6 },
        { name: '财务费用', value: 2600, trend: -4.2 },
      ],
      receivableStats: [
        { label: '应收总额', value: '21,800', color: '#FA8C16' },
        { label: '已回款', value: '17,067', color: '#52C41A' },
        { label: '逾期应收', value: '3,120', color: '#FF4D4F' },
        { label: '坏账准备', value: '890', color: '#999' },
      ],
      payableStats: [
        { label: '应付总额', value: '16,500', color: '#722ED1' },
        { label: '已付款', value: '10,725', color: '#52C41A' },
        { label: '逾期应付', value: '1,850', color: '#FF4D4F' },
        { label: '票据待付', value: '2,460', color: '#1890FF' },
      ],
      financeDataEntries: [
        { name: '报表数据', icon: 'el-icon-document', desc: '财务报表数据汇总', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/cwsc/jcpz/companyData/statement' },
        { name: '账簿数据', icon: 'el-icon-notebook-2', desc: '科目/余额/明细/日记账', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/cwsc/jcpz/companyData/ZNFXaccountData' },
        { name: '账簿管理', icon: 'el-icon-folder-opened', desc: '账簿数据维护管理', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/workbench/companyData/accountManage' },
        { name: '凭证库', icon: 'el-icon-tickets', desc: '会计凭证查询', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/cwsc/jcpz/companyData/sub/voucherLib' },
        { name: '业务数据', icon: 'el-icon-s-order', desc: '业务数据采集查看', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/workbench/companyData/sub/businessData' },
        { name: '放大镜', icon: 'el-icon-search', desc: '数据穿透查询工具', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/workbench/auditTools/magnifyingGlass' },
      ],
      modules: [
        { name: '应收管理', icon: 'el-icon-coin', desc: '登记/收款/客户/坏账', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/financialSharing/financial/receivables/index' },
        { name: '应付管理', icon: 'el-icon-wallet', desc: '登记/付款/供应商/票据', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/financialSharing/financial/payables/index' },
        { name: '收入管理', icon: 'el-icon-data-line', desc: '确认/分配/合同/递延', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/financialSharing/financial/revenueManagement/index' },
        { name: '费用管理', icon: 'el-icon-s-order', desc: '差旅/稽核/预算管控', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/financialSharing/baseConfig/auditRule' },
        { name: '固定资产', icon: 'el-icon-office-building', desc: '卡片/折旧/处置/盘点', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/financialSharing/financial/fixedAssets/index' },
        { name: '存货管理', icon: 'el-icon-box', desc: '入库/出库/盘点/估值', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/financialSharing/financial/inventory/index' },
        { name: '成本管理', icon: 'el-icon-s-data', desc: '成本核算/结转/分析', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/financialSharing/financial/inventory/costTransfer' },
        { name: '报表分析', icon: 'el-icon-document', desc: '企业报表/合并报表', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/financialSharing/reports/index' },
        { name: '预算管理', icon: 'el-icon-data-board', desc: '编制/执行/管控/分析', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/financialSharing/budget/index' },
        { name: '数据采集', icon: 'el-icon-download', desc: '数据源/采集/质量', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/financialSharing/dataCollection/dataSource' },
        { name: '集团管控', icon: 'el-icon-s-custom', desc: 'AI分析/敏捷分析', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/znfx/ai/aifx' },
        { name: '行业数据', icon: 'el-icon-view', desc: '望远镜/行业对标', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/workbench/industryData/telescope' },
      ],
      configItems: [
        { name: '入账设置_规则', icon: 'el-icon-setting', desc: '集团入账规则配置', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/financialSharing/jcpz/rzszgz' },
        { name: '入账设置_对照', icon: 'el-icon-sort', desc: '业务单元对照配置', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/financialSharing/jcpz/rzszdz' },
        { name: '转换模板_集团', icon: 'el-icon-document-copy', desc: '集团转换模板管理', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/financialSharing/jcpz/zhmbjt' },
        { name: '分类定义_集团', icon: 'el-icon-s-grid', desc: '集团分类标准定义', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/financialSharing/jcpz/fldyjt' },
        { name: '科目对照表_集团', icon: 'el-icon-notebook-1', desc: '集团科目对照管理', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/financialSharing/jcpz/kmdzbjt' },
        { name: '要素对照表_集团', icon: 'el-icon-collection-tag', desc: '集团要素对照管理', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/financialSharing/jcpz/ysdzbjt' },
        { name: '基础档案转换', icon: 'el-icon-switch-button', desc: '基础档案映射转换', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/financialSharing/jcpz/jcdazh' },
        { name: '平台设置', icon: 'el-icon-s-tools', desc: '平台参数与日志', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/financialSharing/jcpz/ptsz' },
      ],
      aiFeatures: [
        { name: 'AI 财务分析', icon: 'el-icon-data-analysis', desc: '智能财务数据多维度分析', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/znfx/ai/index' },
        { name: 'AI 决策辅助', icon: 'el-icon-s-data', desc: 'AI辅助财务决策与预测', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/znfx/ai/aifx' },
        { name: '敏捷分析', icon: 'el-icon-magic-stick', desc: '快速灵活的数据探索', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/znfx/ai/mjfx' },
        { name: '财务总览', icon: 'el-icon-data-board', desc: '财务状态综合看板', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/znfx/cwztfx/cwzl' },
        { name: '资产负债分析', icon: 'el-icon-s-finance', desc: '资产负债结构深度分析', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/znfx/cwztfx/zcfzfx' },
        { name: '应收应付分析', icon: 'el-icon-coin', desc: '应收应付账龄与风险', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/znfx/cwztfx/ysyfzkfx' },
      ],
      lawItems: [
        { name: '法律搜索', icon: 'el-icon-search', desc: '法律法规智能检索', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/workbench/auditTools/lawSearch' },
        { name: '法律规章', icon: 'el-icon-reading', desc: '外部法律规章库', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/workbench/controlLib/law' },
        { name: '法律法规库', icon: 'el-icon-notebook-2', desc: '合规法律法规数据库', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/internal/new/hggfk/flfgk' },
        { name: '制度搜索', icon: 'el-icon-zoom-in', desc: '内部制度智能检索', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/workbench/auditTools/orderSearch' },
        { name: '集团规章制度', icon: 'el-icon-document', desc: '集团规章制度管理', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/internal/new/hggfk/jtgsgzzdk' },
        { name: '管理制度', icon: 'el-icon-s-order', desc: '内部管理制度文件', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/workbench/controlLib/manage' },
        { name: '行业知识库', icon: 'el-icon-collection', desc: '行业专业知识库', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/workbench/controlLib/knowledge' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '系统消息与通知', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/homes/personal/newIndex' },
      ],
    }
  },
  computed: {
    greetingWord() {
      const h = new Date().getHours()
      if (h < 6) return '凌晨好'
      if (h < 12) return '上午好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
  },
  watch: {
    ipBright: {
      immediate: true,
      handler() { this._patchPrimaryColors() },
    },
  },
  created() {
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo ? userInfo.realname || userInfo.username || '' : ''
    } catch (e) {
      this.userName = ''
    }
    this._patchPrimaryColors()
  },
  methods: {
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBgLight = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[0]) this.welcomeStats[0].color = bright
      if (this.kpiCards && this.kpiCards[0]) {
        this.kpiCards[0].iconBg = brightBg
        this.kpiCards[0].iconColor = bright
        this.kpiCards[0].barBg = brightBg
        this.kpiCards[0].barColor = bright
      }
      ;[3, 7].forEach(i => {
        if (this.modules && this.modules[i]) {
          this.modules[i].iconBg = brightBg
          this.modules[i].iconColor = bright
        }
      })
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBgLight
        this.aiFeatures[0].iconColor = bright
      }
      if (this.configItems && this.configItems[0]) {
        this.configItems[0].bgColor = brightBgLight
        this.configItems[0].iconColor = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-finance-home {
  padding: 16px;
  background: #f0f2f5;
  min-height: 100vh;
}

/* ========== 欢迎区 ========== */
.welcome-section { position: relative; border-radius: 12px; overflow: hidden; margin-bottom: 16px; }
.welcome-bg {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  z-index: 0;
}
.welcome-content { position: relative; z-index: 1; padding: 28px 32px; display: flex; justify-content: space-between; align-items: center; color: #fff; }
.welcome-left { flex: 1; }
.greeting { display: flex; align-items: center; gap: 6px; margin-bottom: 8px;
  .greeting-icon { font-size: 20px; color: #FAAD14; }
  .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); }
}
.welcome-title {
  margin: 0 0 8px; font-size: 26px; font-weight: 700; letter-spacing: 2px;
  background: linear-gradient(90deg, #fff, var(--ip-light-bg, #B2D4FF));
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
}
.welcome-desc { font-size: 14px; color: rgba(255,255,255,0.6); margin-bottom: 18px; }
.welcome-actions { display: flex; gap: 10px; }
.welcome-stats { display: flex; gap: 24px; }
.ws-item { text-align: center;
  .ws-value { font-size: 22px; font-weight: 700; }
  .ws-label { font-size: 12px; color: rgba(255,255,255,0.6); margin-top: 4px; }
}

/* ========== KPI ========== */
.kpi-section { display: grid; grid-template-columns: repeat(6, 1fr); gap: 14px; margin-bottom: 16px; }
.kpi-card {
  background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; }
  &.kpi-danger { border-top: 3px solid #FF4D4F; }
  .kpi-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
  .kpi-icon-wrap { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; i { font-size: 18px; } }
  .kpi-trend { font-size: 12px; &.trend-up { color: #52C41A; } &.trend-down { color: #FF4D4F; } }
  .kpi-value { font-size: 24px; font-weight: 700; color: var(--ip-primary); }
  .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; color: #999; }
  .kpi-label { font-size: 12px; color: #888; margin: 4px 0 8px; }
  .kpi-bar { height: 4px; border-radius: 2px; overflow: hidden; }
  .kpi-bar-fill { height: 100%; border-radius: 2px; transition: width 0.6s ease; }
}

/* ========== 生命周期 ========== */
.lifecycle-section { background: #fff; border-radius: 10px; padding: 16px 20px; margin-bottom: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.section-title { font-size: 14px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.section-legend { font-size: 12px; color: #888; span { margin-left: 14px; }
  .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle;
    &.green { background: #52C41A; } &.orange { background: #FA8C16; } &.red { background: #FF4D4F; }
  }
}
.lifecycle-track { display: flex; align-items: center; justify-content: center; flex-wrap: wrap; gap: 0; }
.lc-step { display: flex; align-items: center; }
.lc-node {
  position: relative; display: flex; flex-direction: column; align-items: center; width: 78px; padding: 10px 4px;
  border-radius: 10px; border: 2px solid #d9d9d9; cursor: pointer; transition: all 0.25s; background: #fafafa;
  &:hover { transform: translateY(-3px); box-shadow: 0 6px 16px rgba(0,0,0,.12); }
  &.normal { border-color: #52C41A; } &.warn { border-color: #FA8C16; background: #FFFBE6; } &.alert { border-color: #FF4D4F; background: #FFF1F0; }
  .lc-icon i { font-size: 20px; color: var(--ip-primary); }
  .lc-label { font-size: 11px; margin-top: 4px; color: #333; font-weight: 500; }
  .lc-num { font-size: 14px; font-weight: 700; color: var(--ip-primary); }
  .lc-alert-dot { position: absolute; top: -4px; right: -4px; width: 10px; height: 10px; border-radius: 50%; background: #FF4D4F; animation: pulse 1.5s infinite; }
}
.lc-connector { display: flex; align-items: center;
  .lc-line { width: 16px; height: 2px; background: #52C41A; &.line-alert { background: #FF4D4F; } }
  .lc-arrow { font-size: 12px; color: #52C41A; }
}
@keyframes pulse { 0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); } 70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); } 100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); } }

/* ========== 通用面板 ========== */
.content-row { margin-bottom: 16px; }
.card-panel { background: #fff; border-radius: 10px; padding: 16px 18px; box-shadow: 0 1px 6px rgba(0,0,0,.06); height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 15px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.card-more { font-size: 12px; color: var(--ip-bright); cursor: pointer; &:hover { opacity: 0.8; } }

/* ========== 财务决策 ========== */
.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.analysis-item {
  display: flex; justify-content: space-between; align-items: center; padding: 10px 12px; border-radius: 8px;
  cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; } & + .analysis-item { border-top: 1px solid #f5f5f5; }
}
.analysis-name { font-size: 13px; color: #333; font-weight: 500; }
.analysis-right { display: flex; align-items: center; gap: 8px; }
.analysis-val { font-size: 15px; font-weight: 600; }

/* ========== 应收应付 ========== */
.ar-ap-section { display: flex; gap: 0; }
.ar-ap-block { flex: 1; }
.ar-ap-title { font-size: 13px; font-weight: 600; color: var(--ip-primary); margin-bottom: 10px; padding-bottom: 6px; border-bottom: 2px solid var(--ip-accent, #FAAD14); }
.ar-ap-row { display: flex; justify-content: space-between; align-items: center; padding: 6px 0;
  .ar-ap-label { font-size: 12px; color: #888; } .ar-ap-val { font-size: 14px; font-weight: 600; }
}
.ar-ap-divider { width: 1px; background: #f0f0f0; margin: 0 14px; }
.ar-ap-bar-group { margin-top: 12px; padding-top: 10px; border-top: 1px solid #f0f0f0; }
.ar-ap-bar-item { display: flex; align-items: center; gap: 8px; margin-bottom: 8px;
  .bar-label { font-size: 12px; color: #888; width: 50px; flex-shrink: 0; }
  .bar-bg { flex: 1; height: 8px; background: #f5f5f5; border-radius: 4px; overflow: hidden; }
  .bar-fill { height: 100%; border-radius: 4px; }
  .bar-val { font-size: 13px; font-weight: 600; color: #333; width: 36px; text-align: right; }
}

/* ========== 数据入口 ========== */
.data-entry-item {
  display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s; margin-bottom: 6px;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .de-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .de-info { flex: 1; min-width: 0; }
  .de-name { font-size: 13px; font-weight: 500; color: #333; }
  .de-desc { font-size: 11px; color: #999; margin-top: 1px; }
  .de-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 模块网格 ========== */
.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; }
  .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* ========== 配置 ========== */
.config-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.config-card {
  display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .cf-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 16px; } }
  .cf-body { flex: 1; min-width: 0; }
  .cf-name { font-size: 13px; font-weight: 500; color: #333; }
  .cf-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .cf-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== AI ========== */
.ai-panel { background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%); }
.ai-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.ai-card {
  display: flex; align-items: center; gap: 10px; padding: 12px; border-radius: 10px;
  border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-2px); }
  .ai-icon { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 18px; } }
  .ai-body { flex: 1; min-width: 0; }
  .ai-name { font-size: 13px; font-weight: 600; color: #333; }
  .ai-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .ai-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 法规 ========== */
.law-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.law-card {
  display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .law-icon-wrap { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 16px; } }
  .law-body { flex: 1; min-width: 0; }
  .law-name { font-size: 13px; font-weight: 500; color: #333; }
  .law-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .law-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}
</style>
