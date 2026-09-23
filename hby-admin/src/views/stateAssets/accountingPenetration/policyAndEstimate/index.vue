<template>
  <div class="policy-estimate-container">
    <!-- 页面头部 -->
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="header-left">
        <span class="header-icon">📋</span>
        <div>
          <h2 class="header-title">会计政策与估计</h2>
          <p class="header-desc">政策台账管理 · 一致性检查 · 估计变更监控 · 合规评估</p>
        </div>
      </div>
      <div class="header-right">
        <el-tag type="warning" size="small" style="margin-right:8px">
          <i class="el-icon-warning-outline"></i> 待核查估计变更 {{ warningCount }} 项
        </el-tag>
        <el-button size="small" type="primary" plain @click="exportData">
          <i class="el-icon-download"></i> 导出
        </el-button>
      </div>
    </div>

    <!-- 顶部统计条 -->
    <div class="stat-bar">
      <div class="stat-item" v-for="s in statList" :key="s.label">
        <span class="stat-value" :style="{ color: s.color }">{{ s.value }}</span>
        <span class="stat-label">{{ s.label }}</span>
      </div>
    </div>

    <!-- Tab 内容 -->
    <el-tabs v-model="activeTab" class="main-tabs" @tab-click="handleTabChange">
      <!-- Tab1: 会计政策台账 -->
      <el-tab-pane label="会计政策台账" name="policy">
        <div class="tab-toolbar">
          <el-form inline size="small">
            <el-form-item label="政策类型" v-if="policyActiveFilters.includes('type')">
              <el-select v-model="policyQuery.type" placeholder="全部" clearable style="width:130px">
                <el-option v-for="t in policyTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
            <el-form-item label="适用企业" v-if="policyActiveFilters.includes('company')">
              <el-input v-model="policyQuery.company" placeholder="企业名称" clearable style="width:140px" />
            </el-form-item>
            <el-form-item label="状态" v-if="policyActiveFilters.includes('status')">
              <el-select v-model="policyQuery.status" placeholder="全部" clearable style="width:100px">
                <el-option label="有效" value="有效" />
                <el-option label="变更中" value="变更中" />
                <el-option label="已废止" value="已废止" />
              </el-select>
            </el-form-item>
            <el-form-item label="一致性" v-if="policyActiveFilters.includes('isConsistent')">
              <el-select v-model="policyQuery.isConsistent" placeholder="全部" clearable style="width:100px">
                <el-option label="一致" value="1" />
                <el-option label="偏差" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item label="报告年度" v-if="policyActiveFilters.includes('reportYear')">
              <el-date-picker v-model="policyQuery.reportYear" type="year" value-format="yyyy"
                placeholder="年度" style="width:100px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadPolicyList">查询</el-button>
              <el-button @click="resetPolicyQuery">重置</el-button>
              <el-button type="success" @click="openAddPolicy">+ 新增政策</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar-right">
            <el-popover placement="bottom" width="200" trigger="click">
              <div class="column-selector">
                <div class="selector-title">筛选条件</div>
                <el-checkbox-group v-model="policyActiveFilters">
                  <el-checkbox v-for="f in policyFilterOptions" :key="f.key" :label="f.key">{{ f.label }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <el-button slot="reference" size="mini" icon="el-icon-setting" circle title="筛选条件设置" />
            </el-popover>
            <el-popover placement="bottom" width="200" trigger="click">
              <div class="column-selector">
                <div class="selector-title">显示列</div>
                <el-checkbox-group v-model="policyVisibleColumns">
                  <el-checkbox v-for="c in policyColumnOptions" :key="c.key" :label="c.key">{{ c.label }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <el-button slot="reference" size="mini" icon="el-icon-menu" circle title="列显示设置" />
            </el-popover>
          </div>
        </div>
        <el-table :data="policyList" border stripe size="small" style="width:100%">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column v-if="policyVisibleColumns.includes('policyId')" prop="policyId" label="政策编号" width="90" />
          <el-table-column v-if="policyVisibleColumns.includes('companyName')" prop="companyName" label="适用企业" width="150" show-overflow-tooltip />
          <el-table-column v-if="policyVisibleColumns.includes('policyType')" prop="policyType" label="政策类型" width="110">
            <template slot-scope="{ row }">
              <el-tag size="mini" :type="getPolicyTypeTag(row.policyType)">{{ row.policyType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="policyVisibleColumns.includes('policyName')" prop="policyName" label="政策名称" min-width="160" show-overflow-tooltip />
          <el-table-column v-if="policyVisibleColumns.includes('policyDetail')" prop="policyDetail" label="政策内容摘要" min-width="200" show-overflow-tooltip />
          <el-table-column v-if="policyVisibleColumns.includes('groupStandard')" prop="groupStandard" label="集团统一政策" min-width="160" show-overflow-tooltip />
          <el-table-column v-if="policyVisibleColumns.includes('isConsistent')" prop="isConsistent" label="一致性" width="80" align="center">
            <template slot-scope="{ row }">
              <el-tag v-if="row.isConsistent === '1'" type="success" size="mini">一致</el-tag>
              <el-tag v-else type="danger" size="mini">偏差</el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="policyVisibleColumns.includes('deviationDesc')" prop="deviationDesc" label="偏差说明" min-width="160" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span v-if="row.deviationDesc" class="text-danger">{{ row.deviationDesc }}</span>
              <span v-else class="text-gray">—</span>
            </template>
          </el-table-column>
          <el-table-column v-if="policyVisibleColumns.includes('effectiveDate')" prop="effectiveDate" label="生效日期" width="100" align="center" />
          <el-table-column v-if="policyVisibleColumns.includes('auditOpinionType')" prop="auditOpinionType" label="审计意见" width="110" align="center">
            <template slot-scope="{ row }">
              <el-tag v-if="translateAuditOpinion(row.auditOpinionType)" size="mini" :type="getAuditOpinionTag(row.auditOpinionType)">
                {{ translateAuditOpinion(row.auditOpinionType) }}
              </el-tag>
              <span v-else class="text-gray">—</span>
            </template>
          </el-table-column>
          <el-table-column v-if="policyVisibleColumns.includes('auditEmphasis')" prop="auditEmphasis" label="审计强调事项" width="130" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span v-if="row.auditEmphasis" style="color:#FA8C16">{{ row.auditEmphasis }}</span>
              <span v-else class="text-gray">—</span>
            </template>
          </el-table-column>
          <el-table-column v-if="policyVisibleColumns.includes('status')" prop="status" label="状态" width="80" align="center">
            <template slot-scope="{ row }">
              <el-tag size="mini" :type="row.status === '有效' ? 'success' : row.status === '变更中' ? 'warning' : 'info'">{{ row.status || '有效' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template slot-scope="{ row }">
              <el-button type="text" size="mini" @click="viewPolicy(row)">详情</el-button>
              <el-button type="text" size="mini" @click="editPolicy(row)">变更</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="pagination"
          :current-page="policyPage.page"
          :page-size="policyPage.size"
          :total="policyPage.total"
          layout="total, prev, pager, next"
          @current-change="(p) => { policyPage.page = p; loadPolicyList() }"
        />
      </el-tab-pane>

      <!-- Tab2: 政策一致性检查 -->
      <el-tab-pane label="政策一致性检查" name="consistency">
        <div class="tab-toolbar">
          <el-form inline size="small">
            <el-form-item label="检查事项">
              <el-select v-model="consistencyQuery.item" placeholder="全部" clearable style="width:160px">
                <el-option v-for="i in consistencyItems" :key="i" :label="i" :value="i" />
              </el-select>
            </el-form-item>
            <el-form-item label="差异级别">
              <el-select v-model="consistencyQuery.level" placeholder="全部" clearable style="width:100px">
                <el-option label="重大差异" value="重大" />
                <el-option label="一般差异" value="一般" />
                <el-option label="无差异" value="无" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadConsistencyList">查询</el-button>
              <el-button @click="runConsistencyCheck">执行一致性检查</el-button>
            </el-form-item>
          </el-form>
          <div class="check-summary">
            <span class="summary-item danger">重大差异 <b>{{ consistencyStat.major }}</b> 项</span>
            <span class="summary-item warning">一般差异 <b>{{ consistencyStat.minor }}</b> 项</span>
            <span class="summary-item success">无差异 <b>{{ consistencyStat.none }}</b> 项</span>
          </div>
        </div>
        <el-table :data="consistencyList" border stripe size="small" style="width:100%">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="item" label="检查事项" width="150" />
          <el-table-column prop="standard" label="集团统一政策" min-width="160" show-overflow-tooltip />
          <el-table-column prop="company" label="子企业" width="120" show-overflow-tooltip />
          <el-table-column prop="actual" label="子企业实际政策" min-width="160" show-overflow-tooltip />
          <el-table-column prop="diff" label="差异说明" min-width="150" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span :class="['diff-text', row.diffLevel === '重大' ? 'danger' : row.diffLevel === '一般' ? 'warning' : 'success']">
                {{ row.diff || '无差异' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="diffLevel" label="差异级别" width="90" align="center">
            <template slot-scope="{ row }">
              <el-tag size="mini" :type="row.diffLevel === '重大' ? 'danger' : row.diffLevel === '一般' ? 'warning' : 'success'">
                {{ row.diffLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="impact" label="利润影响(万元)" width="130" align="right">
            <template slot-scope="{ row }">
              <span :class="row.impact > 0 ? 'text-danger' : ''">{{ row.impact > 0 ? '+' + row.impact : row.impact }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="suggest" label="处理建议" width="100" align="center">
            <template slot-scope="{ row }">
              <el-button v-if="row.diffLevel === '重大'" type="text" size="mini" style="color:#FF4D4F" @click="openRectification(row)">下发整改</el-button>
              <span v-else class="text-gray">—</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- Tab3: 会计估计台账 -->
      <el-tab-pane label="会计估计台账" name="estimate">
        <div class="tab-toolbar">
          <el-form inline size="small">
            <el-form-item label="估计类型">
              <el-select v-model="estimateQuery.type" placeholder="全部" clearable style="width:150px">
                <el-option v-for="t in estimateTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
            <el-form-item label="适用企业">
              <el-input v-model="estimateQuery.company" placeholder="企业名称" clearable style="width:140px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadEstimateList">查询</el-button>
              <el-button type="success" @click="openAddEstimate">+ 新增估计</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="estimateList" border stripe size="small" style="width:100%"
          :row-class-name="estimateRowClass">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="estimateType" label="估计类型" width="140" />
          <el-table-column prop="item" label="估计事项" min-width="160" show-overflow-tooltip />
          <el-table-column prop="company" label="适用企业" width="120" show-overflow-tooltip />
          <el-table-column prop="currentValue" label="当前估计值" width="120" align="right">
            <template slot-scope="{ row }">
              {{ row.currentValue }}{{ row.unit }}
            </template>
          </el-table-column>
          <el-table-column prop="industryAvg" label="行业均值" width="100" align="right">
            <template slot-scope="{ row }">
              {{ row.industryAvg }}{{ row.unit }}
            </template>
          </el-table-column>
          <el-table-column prop="deviation" label="偏离度" width="90" align="center">
            <template slot-scope="{ row }">
              <span :class="Math.abs(row.deviation) > 20 ? 'text-danger' : Math.abs(row.deviation) > 10 ? 'text-warning' : 'text-success'">
                {{ row.deviation > 0 ? '+' : '' }}{{ row.deviation }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="basis" label="估计依据" min-width="150" show-overflow-tooltip />
          <el-table-column prop="lastChangeDate" label="上次变更" width="100" align="center" />
          <el-table-column prop="remark" label="风险提示" width="120" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span v-if="row.riskFlag" class="risk-flag"><i class="el-icon-warning"></i> {{ row.remark }}</span>
              <span v-else class="text-gray">—</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" fixed="right">
            <template slot-scope="{ row }">
              <el-button type="text" size="mini" @click="editEstimate(row)">变更</el-button>
              <el-button type="text" size="mini" @click="viewEstimateHistory(row)">历史</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="pagination"
          :current-page="estimatePage.page"
          :page-size="estimatePage.size"
          :total="estimatePage.total"
          layout="total, prev, pager, next"
          @current-change="(p) => { estimatePage.page = p; loadEstimateList() }"
        />
      </el-tab-pane>

      <!-- Tab4: 估计变更分析 -->
      <el-tab-pane label="估计变更分析" name="changeAnalysis">
        <div class="tab-toolbar">
          <el-form inline size="small">
            <el-form-item label="报告期">
              <el-date-picker v-model="changeQuery.period" type="month" value-format="yyyy-MM"
                placeholder="选择月份" style="width:140px" />
            </el-form-item>
            <el-form-item label="关注程度">
              <el-select v-model="changeQuery.attention" placeholder="全部" clearable style="width:100px">
                <el-option label="重点关注" value="高" />
                <el-option label="一般" value="中" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadChangeList">查询</el-button>
            </el-form-item>
          </el-form>
          <!-- 关注规则说明 -->
          <el-alert type="warning" :closable="false" style="margin-left:auto;max-width:500px">
            <span slot="title">
              监控规则：变更导致利润增加 &gt; 当期净利润 10% 时，标记为"重点关注"
            </span>
          </el-alert>
        </div>

        <el-table :data="changeList" border stripe size="small" style="width:100%"
          :row-class-name="changeRowClass">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="company" label="企业名称" width="130" show-overflow-tooltip />
          <el-table-column prop="estimateItem" label="估计事项" width="150" show-overflow-tooltip />
          <el-table-column prop="before" label="变更前" width="120" align="center">
            <template slot-scope="{ row }">{{ row.before }}{{ row.unit }}</template>
          </el-table-column>
          <el-table-column prop="after" label="变更后" width="120" align="center">
            <template slot-scope="{ row }">{{ row.after }}{{ row.unit }}</template>
          </el-table-column>
          <el-table-column prop="changeDate" label="变更日期" width="100" align="center" />
          <el-table-column prop="profitImpact" label="利润影响(万元)" width="130" align="right">
            <template slot-scope="{ row }">
              <span :class="row.profitImpact > 0 ? 'text-danger bold' : ''">
                {{ row.profitImpact > 0 ? '+' : '' }}{{ row.profitImpact.toLocaleString() }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="profitRatio" label="占净利润比" width="110" align="center">
            <template slot-scope="{ row }">
              <span :class="row.profitRatio > 10 ? 'text-danger bold' : ''">
                {{ row.profitRatio }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="changeReason" label="变更理由" min-width="180" show-overflow-tooltip />
          <el-table-column prop="attention" label="关注程度" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag size="mini" :type="row.attention === '高' ? 'danger' : 'warning'">
                {{ row.attention === '高' ? '重点关注' : '一般关注' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="90" align="center" fixed="right">
            <template slot-scope="{ row }">
              <el-button v-if="row.attention === '高'" type="text" size="mini" style="color:#FF4D4F" @click="openVerification(row)">
                核查
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 变更趋势小图 -->
        <div class="trend-section">
          <div class="section-title">近12期估计变更对利润影响趋势</div>
          <div class="trend-bars">
            <div v-for="m in trendMonths" :key="m.month" class="trend-bar-item">
              <div class="bar-wrap">
                <div class="bar-fill" :style="{ height: m.ratio + '%', background: m.ratio > 10 ? '#FF4D4F' : '#FA8C16' }"></div>
              </div>
              <div class="bar-label">{{ m.month }}</div>
              <div class="bar-value" :style="{ color: m.ratio > 10 ? '#FF4D4F' : '#606266' }">
                {{ m.ratio }}%
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- Tab5: 政策合规评估 -->
      <el-tab-pane label="政策合规评估" name="compliance">
        <div class="tab-toolbar">
          <el-form inline size="small">
            <el-form-item label="评估企业">
              <el-select v-model="complianceQuery.company" placeholder="全部" clearable style="width:140px">
                <el-option v-for="c in companyList" :key="c" :label="c" :value="c" />
              </el-select>
            </el-form-item>
            <el-form-item label="评估年度">
              <el-date-picker v-model="complianceQuery.year" type="year" value-format="yyyy"
                placeholder="选择年度" style="width:120px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadComplianceList">查询</el-button>
              <el-button @click="runComplianceEval">执行合规评估</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 合规评分卡 -->
        <div class="compliance-cards">
          <div v-for="c in complianceCards" :key="c.company" class="compliance-card">
            <div class="cc-header">
              <span class="cc-company">{{ c.company }}</span>
              <span class="cc-score" :style="{ color: c.score >= 90 ? '#52C41A' : c.score >= 75 ? '#FA8C16' : '#FF4D4F' }">
                {{ c.score }}分
              </span>
            </div>
            <div class="cc-dimensions">
              <div v-for="d in c.dimensions" :key="d.name" class="cc-dim">
                <span class="dim-name">{{ d.name }}</span>
                <el-progress
                  :percentage="d.score"
                  :color="d.score >= 90 ? '#52C41A' : d.score >= 75 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="10"
                  style="flex:1;margin:0 10px"
                />
                <span class="dim-score">{{ d.score }}</span>
              </div>
            </div>
            <div class="cc-issues" v-if="c.issues && c.issues.length">
              <div v-for="issue in c.issues" :key="issue" class="cc-issue">
                <i class="el-icon-warning" style="color:#FA8C16;margin-right:4px"></i>{{ issue }}
              </div>
            </div>
          </div>
        </div>

        <!-- 合规详细列表 -->
        <el-table :data="complianceList" border stripe size="small" style="width:100%;margin-top:16px">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="company" label="企业名称" width="130" />
          <el-table-column prop="checkItem" label="检查项目" min-width="200" show-overflow-tooltip />
          <el-table-column prop="standard" label="准则要求" min-width="160" show-overflow-tooltip />
          <el-table-column prop="actual" label="实际情况" min-width="160" show-overflow-tooltip />
          <el-table-column prop="result" label="检查结论" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag size="mini" :type="row.result === '符合' ? 'success' : row.result === '基本符合' ? 'warning' : 'danger'">
                {{ row.result }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="risk" label="风险说明" min-width="150" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <span :class="row.result === '不符合' ? 'text-danger' : ''">{{ row.risk || '—' }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 政策详情弹窗 -->
    <el-dialog title="会计政策详情" :visible.sync="detailDialog.visible" width="720px">
      <div v-if="detailDialog.data" class="policy-detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="政策编号">{{ detailDialog.data.policyId }}</el-descriptions-item>
          <el-descriptions-item label="报告年度">{{ detailDialog.data.reportYear }}</el-descriptions-item>
          <el-descriptions-item label="适用企业">{{ detailDialog.data.companyName }}</el-descriptions-item>
          <el-descriptions-item label="政策类型">
            <el-tag size="mini" :type="getPolicyTypeTag(detailDialog.data.policyType)">{{ detailDialog.data.policyType }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="政策名称" :span="2">{{ detailDialog.data.policyName }}</el-descriptions-item>
          <el-descriptions-item label="政策内容" :span="2">{{ detailDialog.data.policyDetail }}</el-descriptions-item>
          <el-descriptions-item label="集团统一政策" :span="2">{{ detailDialog.data.groupStandard }}</el-descriptions-item>
          <el-descriptions-item label="一致性">
            <el-tag v-if="detailDialog.data.isConsistent === '1'" type="success" size="mini">与集团一致</el-tag>
            <el-tag v-else type="danger" size="mini">存在偏差</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ detailDialog.data.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="偏差说明" :span="2" v-if="detailDialog.data.isConsistent !== '1'">
            <span style="color:#FF4D4F">{{ detailDialog.data.deviationDesc || '—' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="审计意见类型">
            <el-tag v-if="translateAuditOpinion(detailDialog.data.auditOpinionType)" size="mini" :type="getAuditOpinionTag(detailDialog.data.auditOpinionType)">
              {{ translateAuditOpinion(detailDialog.data.auditOpinionType) }}
            </el-tag>
            <span v-else style="color:#c0c4cc">未填写</span>
          </el-descriptions-item>
          <el-descriptions-item label="审计强调事项">
            <span v-if="detailDialog.data.auditEmphasis" style="color:#FA8C16">{{ detailDialog.data.auditEmphasis }}</span>
            <span v-else style="color:#c0c4cc">无</span>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ detailDialog.data.createTime }}</el-descriptions-item>
          <el-descriptions-item label="最后更新">{{ detailDialog.data.updateTime }}</el-descriptions-item>
        </el-descriptions>
        <!-- 偏差风险提示 -->
        <el-alert v-if="detailDialog.data.isConsistent !== '1'" type="error" :closable="false" style="margin-top:16px">
          <span slot="title">
            <i class="el-icon-warning" style="margin-right:4px"></i>
            政策偏差预警：该企业政策与集团标准存在偏差，{{ detailDialog.data.deviationDesc }}，审计意见为「{{ translateAuditOpinion(detailDialog.data.auditOpinionType) || '未填写' }}」
          </span>
        </el-alert>
        <el-alert v-else-if="detailDialog.data.auditEmphasis" type="warning" :closable="false" style="margin-top:16px">
          <span slot="title">
            <i class="el-icon-warning" style="margin-right:4px"></i>
            审计强调事项：{{ detailDialog.data.auditEmphasis }}
          </span>
        </el-alert>
      </div>
      <div slot="footer">
        <el-button size="small" @click="detailDialog.visible = false">关闭</el-button>
        <el-button size="small" type="primary" @click="detailDialog.visible = false; editPolicy(detailDialog.data)">发起变更</el-button>
      </div>
    </el-dialog>

    <!-- 新增/变更政策弹窗 -->
    <el-dialog :title="policyDialog.isEdit ? '变更会计政策' : '新增会计政策'"
      :visible.sync="policyDialog.visible" width="680px" :close-on-click-modal="false">
      <el-form :model="policyForm" :rules="policyRules" ref="policyForm" label-width="100px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="政策类型" prop="policyType">
              <el-select v-model="policyForm.policyType" placeholder="请选择" style="width:100%">
                <el-option v-for="t in policyTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政策名称" prop="policyName">
              <el-input v-model="policyForm.policyName" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="政策内容" prop="policyDetail">
          <el-input type="textarea" v-model="policyForm.policyDetail" :rows="3" placeholder="请描述具体政策内容" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="适用企业" prop="companyName">
              <el-input v-model="policyForm.companyName" placeholder="填写适用范围" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="policyForm.effectiveDate" type="date" value-format="yyyy-MM-dd"
                placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="制定依据">
          <el-input v-model="policyForm.groupStandard" placeholder="如：企业会计准则第X号" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="审计意见">
              <el-select v-model="policyForm.auditOpinionType" placeholder="请选择" style="width:100%" clearable>
                <el-option label="标准无保留" value="标准无保留" />
                <el-option label="带强调事项段" value="带强调事项段" />
                <el-option label="保留意见" value="保留意见" />
                <el-option label="否定意见" value="否定意见" />
                <el-option label="无法表示意见" value="无法表示意见" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="强调事项" v-if="policyForm.auditOpinionType === '带强调事项段'">
              <el-input v-model="policyForm.auditEmphasis" placeholder="审计强调事项" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="报告年度">
              <el-date-picker v-model="policyForm.reportYear" type="year" value-format="yyyy"
                placeholder="年度" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="一致性">
              <el-select v-model="policyForm.isConsistent" placeholder="请选择" style="width:100%">
                <el-option label="与集团一致" value="1" />
                <el-option label="存在偏差" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-select v-model="policyForm.status" placeholder="请选择" style="width:100%">
                <el-option label="有效" value="有效" />
                <el-option label="变更中" value="变更中" />
                <el-option label="已废止" value="已废止" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="变更说明" v-if="policyDialog.isEdit">
          <el-input type="textarea" v-model="policyForm.deviationDesc" :rows="2" placeholder="请说明本次变更原因及偏差说明" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="policyDialog.visible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitPolicy" :loading="policyDialog.loading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 新增/变更估计弹窗 -->
    <el-dialog :title="estimateDialog.isEdit ? '变更会计估计' : '新增会计估计'"
      :visible.sync="estimateDialog.visible" width="620px" :close-on-click-modal="false">
      <el-form :model="estimateForm" ref="estimateForm" label-width="110px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="估计类型">
              <el-select v-model="estimateForm.estimateType" placeholder="请选择" style="width:100%">
                <el-option v-for="t in estimateTypes" :key="t" :label="t" :value="t" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用企业">
              <el-input v-model="estimateForm.company" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="估计事项">
          <el-input v-model="estimateForm.item" placeholder="如：固定资产折旧年限" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="10">
            <el-form-item label="当前估计值">
              <el-input v-model="estimateForm.currentValue" placeholder="数值" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="单位">
              <el-input v-model="estimateForm.unit" placeholder="年/%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="估计依据">
          <el-input type="textarea" v-model="estimateForm.basis" :rows="2" placeholder="技术评估/市场惯例/历史经验" />
        </el-form-item>
        <el-form-item label="变更说明" v-if="estimateDialog.isEdit">
          <el-input type="textarea" v-model="estimateForm.changeReason" :rows="2" placeholder="请说明变更原因及对财务报表的影响" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="estimateDialog.visible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitEstimate" :loading="estimateDialog.loading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 下发整改弹窗 -->
    <el-dialog title="下发整改通知" :visible.sync="rectificationDialog.visible" width="560px" :close-on-click-modal="false">
      <el-form :model="rectificationForm" label-width="100px" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="rectificationForm.companyName" disabled />
        </el-form-item>
        <el-form-item label="检查事项">
          <el-input v-model="rectificationForm.policyType" disabled />
        </el-form-item>
        <el-form-item label="差异级别">
          <el-tag :type="rectificationForm.diffLevel === '重大' ? 'danger' : 'warning'" size="small">
            {{ rectificationForm.diffLevel }}
          </el-tag>
        </el-form-item>
        <el-form-item label="差异说明">
          <el-input v-model="rectificationForm.diffDesc" disabled type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="整改要求" required>
          <el-input type="textarea" v-model="rectificationForm.requirement" :rows="3"
            placeholder="请填写具体整改要求，如：要求在规定期限内统一采用集团标准政策..." />
        </el-form-item>
        <el-form-item label="整改期限" required>
          <el-date-picker v-model="rectificationForm.deadline" type="date" value-format="yyyy-MM-dd"
            placeholder="选择整改截止日期" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="rectificationDialog.visible = false">取消</el-button>
        <el-button size="small" type="danger" @click="submitRectification" :loading="rectificationDialog.loading">确认下发</el-button>
      </div>
    </el-dialog>

    <!-- 核查弹窗 -->
    <el-dialog title="估计变更核查" :visible.sync="verificationDialog.visible" width="560px" :close-on-click-modal="false">
      <el-form :model="verificationForm" label-width="100px" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="verificationForm.companyName" disabled />
        </el-form-item>
        <el-form-item label="估计事项">
          <el-input v-model="verificationForm.estimateItem" disabled />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="变更前">
              <el-input v-model="verificationForm.changeBefore" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变更后">
              <el-input v-model="verificationForm.changeAfter" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="利润影响">
          <el-input v-model="verificationForm.profitImpact" disabled>
            <template slot="append">万元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="核查意见" required>
          <el-input type="textarea" v-model="verificationForm.opinion" :rows="3"
            placeholder="请填写核查意见，如：经核查该估计变更缺乏充分依据..." />
        </el-form-item>
        <el-form-item label="核查结论" required>
          <el-radio-group v-model="verificationForm.result">
            <el-radio label="合理">合理</el-radio>
            <el-radio label="需关注">需关注</el-radio>
            <el-radio label="不合理">不合理</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="verificationDialog.visible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitVerification" :loading="verificationDialog.loading">提交核查</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPolicyList,
  addPolicy,
  updatePolicy,
  getEstimateList,
  addEstimate,
  updateEstimate,
  getConsistencyCheckList,
  getChangeAnalysisList,
  getComplianceEvalData,
  getAccountingStatistics,
  runConsistencyCheck as apiRunConsistency,
  runComplianceEval as apiRunCompliance,
  getEstimateHistory,
  exportPolicy,
  issueRectification,
  submitVerification as apiSubmitVerification
} from '@/api/stateAssets/accountingPenetration'
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
  name: 'PolicyAndEstimate',
  data() {
    return {
      activeTab: 'policy',
      warningCount: 0,
      statList: [],

      // ===== 会计政策台账 =====
      policyTypes: ['收入确认', '固定资产折旧', '无形资产摊销', '存货计价', '坏账准备', '借款费用', '租赁', '研发支出', '政府补助', '金融工具', '存货跌价', '长期股权投资', '所得税', '其他'],
      policyQuery: { type: '', company: '', status: '有效', isConsistent: '', reportYear: '' },
      policyPage: { page: 1, size: 10, total: 0 },
      policyList: [],
      // 筛选条件可选项
      policyFilterOptions: [
        { key: 'type', label: '政策类型' },
        { key: 'company', label: '适用企业' },
        { key: 'status', label: '状态' },
        { key: 'isConsistent', label: '一致性' },
        { key: 'reportYear', label: '报告年度' }
      ],
      policyActiveFilters: ['type', 'company', 'status'],
      // 列显示控制
      policyColumnOptions: [
        { key: 'policyId', label: '政策编号' },
        { key: 'companyName', label: '适用企业' },
        { key: 'policyType', label: '政策类型' },
        { key: 'policyName', label: '政策名称' },
        { key: 'policyDetail', label: '政策内容摘要' },
        { key: 'groupStandard', label: '集团统一政策' },
        { key: 'isConsistent', label: '一致性' },
        { key: 'deviationDesc', label: '偏差说明' },
        { key: 'effectiveDate', label: '生效日期' },
        { key: 'auditOpinionType', label: '审计意见' },
        { key: 'auditEmphasis', label: '审计强调事项' },
        { key: 'status', label: '状态' }
      ],
      policyVisibleColumns: ['policyId', 'companyName', 'policyType', 'policyName', 'policyDetail', 'groupStandard', 'isConsistent', 'deviationDesc', 'effectiveDate', 'auditOpinionType', 'auditEmphasis', 'status'],

      // ===== 政策一致性检查 =====
      consistencyItems: ['收入确认时点', '存货计价方法', '固定资产折旧方法', '坏账计提比例', '研发费用资本化', '合并范围认定'],
      consistencyQuery: { item: '', level: '', company: '' },
      consistencyList: [],
      consistencyStat: { major: 0, minor: 0, none: 0 },
      consistencyFilterOptions: [
        { key: 'item', label: '检查事项' },
        { key: 'level', label: '差异级别' },
        { key: 'company', label: '子企业' }
      ],
      consistencyActiveFilters: ['item', 'level'],
      consistencyColumnOptions: [
        { key: 'item', label: '检查事项' },
        { key: 'standard', label: '集团统一政策' },
        { key: 'company', label: '子企业' },
        { key: 'actual', label: '子企业实际政策' },
        { key: 'diff', label: '差异说明' },
        { key: 'diffLevel', label: '差异级别' },
        { key: 'impact', label: '利润影响' },
        { key: 'suggest', label: '处理建议' }
      ],
      consistencyVisibleColumns: ['item', 'standard', 'company', 'actual', 'diff', 'diffLevel', 'impact', 'suggest'],

      // ===== 会计估计台账 =====
      estimateTypes: ['固定资产折旧年限', '无形资产摊销年限', '坏账准备比例', '存货跌价标准', '预计负债', '股份支付公允价值', '其他'],
      estimateQuery: { type: '', company: '', riskOnly: false },
      estimatePage: { page: 1, size: 10, total: 0 },
      estimateList: [],
      estimateFilterOptions: [
        { key: 'type', label: '估计类型' },
        { key: 'company', label: '适用企业' },
        { key: 'riskOnly', label: '仅风险项' }
      ],
      estimateActiveFilters: ['type', 'company'],
      estimateColumnOptions: [
        { key: 'estimateType', label: '估计类型' },
        { key: 'item', label: '估计事项' },
        { key: 'company', label: '适用企业' },
        { key: 'currentValue', label: '当前估计值' },
        { key: 'industryAvg', label: '行业均值' },
        { key: 'deviation', label: '偏离度' },
        { key: 'basis', label: '估计依据' },
        { key: 'lastChangeDate', label: '上次变更' },
        { key: 'remark', label: '风险提示' }
      ],
      estimateVisibleColumns: ['estimateType', 'item', 'company', 'currentValue', 'industryAvg', 'deviation', 'basis', 'lastChangeDate', 'remark'],

      // ===== 估计变更分析 =====
      changeQuery: { period: '', attention: '', company: '', estimateItem: '' },
      changeList: [],
      trendMonths: [],
      changeFilterOptions: [
        { key: 'period', label: '报告期' },
        { key: 'attention', label: '关注程度' },
        { key: 'company', label: '企业名称' },
        { key: 'estimateItem', label: '估计事项' }
      ],
      changeActiveFilters: ['period', 'attention'],
      changeColumnOptions: [
        { key: 'company', label: '企业名称' },
        { key: 'estimateItem', label: '估计事项' },
        { key: 'before', label: '变更前' },
        { key: 'after', label: '变更后' },
        { key: 'changeDate', label: '变更日期' },
        { key: 'profitImpact', label: '利润影响' },
        { key: 'profitRatio', label: '占净利润比' },
        { key: 'changeReason', label: '变更理由' },
        { key: 'attention', label: '关注程度' }
      ],
      changeVisibleColumns: ['company', 'estimateItem', 'before', 'after', 'changeDate', 'profitImpact', 'profitRatio', 'changeReason', 'attention'],

      // ===== 政策合规评估 =====
      complianceQuery: { company: '', year: '', result: '' },
      companyList: [],
      complianceCards: [],
      complianceList: [],
      complianceFilterOptions: [
        { key: 'company', label: '评估企业' },
        { key: 'year', label: '评估年度' },
        { key: 'result', label: '检查结论' }
      ],
      complianceActiveFilters: ['company', 'year'],
      complianceColumnOptions: [
        { key: 'company', label: '企业名称' },
        { key: 'checkItem', label: '检查项目' },
        { key: 'standard', label: '准则要求' },
        { key: 'actual', label: '实际情况' },
        { key: 'result', label: '检查结论' },
        { key: 'risk', label: '风险说明' }
      ],
      complianceVisibleColumns: ['company', 'checkItem', 'standard', 'actual', 'result', 'risk'],

      // ===== 详情弹窗 =====
      detailDialog: { visible: false, data: null },

      // ===== 政策弹窗 =====
      policyDialog: { visible: false, isEdit: false, loading: false },
      policyForm: { policyType: '', policyName: '', policyDetail: '', companyName: '', effectiveDate: '', groupStandard: '', deviationDesc: '', auditOpinionType: '', auditEmphasis: '', reportYear: '', isConsistent: '1', status: '有效' },
      policyRules: {
        policyType: [{ required: true, message: '请选择政策类型', trigger: 'change' }],
        policyName: [{ required: true, message: '请输入政策名称', trigger: 'blur' }],
        policyDetail: [{ required: true, message: '请输入政策内容', trigger: 'blur' }],
        companyName: [{ required: true, message: '请输入适用企业', trigger: 'blur' }]
      },

      // ===== 估计弹窗 =====
      estimateDialog: { visible: false, isEdit: false, loading: false },
      estimateForm: { estimateType: '', company: '', item: '', currentValue: '', unit: '%', basis: '', changeReason: '', estimateName: '', isCompliant: '1' },

      // ===== 下发整改弹窗 =====
      rectificationDialog: { visible: false, loading: false },
      rectificationForm: { companyName: '', policyType: '', diffLevel: '', diffDesc: '', requirement: '', deadline: '' },

      // ===== 核查弹窗 =====
      verificationDialog: { visible: false, loading: false },
      verificationForm: { companyName: '', estimateItem: '', changeBefore: '', changeAfter: '', profitImpact: '', opinion: '', result: '' }
    }
  },
  created() {
    this.loadPolicyList()
    this.loadConsistencyList()
    this.loadStatData()
  },
  methods: {
    async loadStatData() {
      try {
        const res = await getAccountingStatistics()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.statList = [
            { label: '会计政策总数', value: String(d.policyTotal || 0), color: '#1A3A5C' },
            { label: '有效政策', value: String(d.policyActive || 0), color: '#52C41A' },
            { label: '变更中', value: String(d.policyChanging || 0), color: '#FA8C16' },
            { label: '政策差异项', value: String(d.policyDiff || 0), color: '#FF4D4F' },
            { label: '估计台账数', value: String(d.estimateTotal || 0), color: '#1A3A5C' },
            { label: '重点关注估计变更', value: String(d.estimateFocus || 0), color: '#FF4D4F' }
          ]
          this.warningCount = d.estimateFocus || 0
        }
      } catch (e) { /* keep defaults */ }
    },
    handleTabChange(tab) {
      const name = tab.name
      if (name === 'estimate') this.loadEstimateList()
      else if (name === 'changeAnalysis') this.loadChangeList()
      else if (name === 'compliance') this.loadComplianceData()
    },

    // ---- 会计政策台账 ----
    async loadPolicyList() {
      try {
        const params = {
          policyType: this.policyQuery.type || '',
          companyName: this.policyQuery.company || '',
          status: this.policyQuery.status || '',
          isConsistent: this.policyQuery.isConsistent || '',
          reportYear: this.policyQuery.reportYear || '',
          pageNumber: this.policyPage.page,
          pageSize: this.policyPage.size
        }
        const res = await getPolicyList(params)
        if (res && res.result === 200) {
          this.policyList = (res.data && res.data.tlist) || []
          this.policyPage.total = (res.data && res.data.totalRecord) || 0
        } else { this.policyList = []; this.policyPage.total = 0 }
      } catch (e) { this.policyList = []; this.policyPage.total = 0 }
    },

    resetPolicyQuery() {
      this.policyQuery = { type: '', company: '', status: '有效', isConsistent: '', reportYear: '' }
      this.policyPage.page = 1
      this.loadPolicyList()
    },

    getPolicyTypeTag(type) {
      const map = { '收入确认': 'primary', '固定资产折旧': '', '坏账准备': 'danger', '研发支出': 'warning', '金融工具': 'success', '存货计价': 'info', '无形资产摊销': '', '借款费用': 'warning', '租赁': 'success', '政府补助': 'info', '存货跌价': 'danger', '长期股权投资': '', '所得税': 'primary' }
      return map[type] || 'info'
    },

    translateAuditOpinion(val) {
      if (!val) return ''
      const map = {
        'STANDARD': '标准无保留', 'standard': '标准无保留', 'unqualified': '标准无保留',
        'UNQUALIFIED': '标准无保留',
        'EMPHASIS': '带强调事项段', 'emphasis': '带强调事项段',
        'QUALIFIED': '保留意见', 'qualified': '保留意见',
        'ADVERSE': '否定意见', 'adverse': '否定意见',
        'DISCLAIMER': '无法表示意见', 'disclaimer': '无法表示意见',
        '标准无保留': '标准无保留', '带强调事项段': '带强调事项段',
        '保留意见': '保留意见', '否定意见': '否定意见', '无法表示意见': '无法表示意见'
      }
      return map[val] || val
    },

    getAuditOpinionTag(val) {
      const translated = this.translateAuditOpinion(val)
      if (translated === '标准无保留') return 'success'
      if (translated === '保留意见' || translated === '否定意见' || translated === '无法表示意见') return 'danger'
      return 'warning'
    },

    openAddPolicy() {
      this.policyDialog.isEdit = false
      this.policyDialog.visible = true
      this.policyForm = { policyType: '', policyName: '', policyDetail: '', companyName: '', effectiveDate: '', groupStandard: '', deviationDesc: '', auditOpinionType: '', auditEmphasis: '', reportYear: new Date().getFullYear().toString(), isConsistent: '1', status: '有效' }
    },

    editPolicy(row) {
      this.policyDialog.isEdit = true
      this.policyDialog.visible = true
      this.policyForm = { ...row }
    },

    viewPolicy(row) {
      this.detailDialog.data = { ...row }
      this.detailDialog.visible = true
    },

    submitPolicy() {
      this.$refs.policyForm.validate(valid => {
        if (!valid) return
        this.policyDialog.loading = true
        const fn = this.policyDialog.isEdit ? updatePolicy : addPolicy
        fn(this.policyForm).then(res => {
          if (res && res.result === 200) {
            this.$message.success('保存成功')
            this.policyDialog.visible = false
            this.loadPolicyList()
          } else {
            this.$message.error((res && res.message) || '保存失败')
          }
        }).catch(() => {
          this.$message.error('保存失败，请稍后重试')
        }).finally(() => { this.policyDialog.loading = false })
      })
    },

    // ---- 政策一致性检查 ----
    async loadConsistencyList() {
      try {
        const res = await getConsistencyCheckList(this.consistencyQuery)
        if (res && res.result === 200) {
          this.consistencyList = (res.data && res.data.tlist) || []
          if (res.data.consistencyStat) this.consistencyStat = res.data.consistencyStat
        } else { this.consistencyList = [] }
      } catch (e) { this.consistencyList = [] }
    },

    runConsistencyCheck() {
      this.$message.info('正在执行一致性检查，请稍候...')
      apiRunConsistency().then(res => {
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.consistencyStat = { major: d.major || 0, minor: d.minor || 0, none: d.none || 0 }
          this.$message.success(d.message || '一致性检查完成')
          this.loadConsistencyList()
        } else {
          this.$message.error((res && res.message) || '一致性检查失败')
        }
      }).catch(() => { this.$message.error('一致性检查失败') })
    },

    // ---- 会计估计台账 ----
    async loadEstimateList() {
      try {
        const res = await getEstimateList({ ...this.estimateQuery, page: this.estimatePage.page, size: this.estimatePage.size })
        if (res && res.result === 200) {
          this.estimateList = (res.data && res.data.tlist) || []
          this.estimatePage.total = (res.data && res.data.totalRecord) || 0
        } else { this.estimateList = []; this.estimatePage.total = 0 }
      } catch (e) { this.estimateList = []; this.estimatePage.total = 0 }
    },

    estimateRowClass({ row }) {
      if (Math.abs(row.deviation) > 20) return 'row-danger'
      if (Math.abs(row.deviation) > 10) return 'row-warning'
      return ''
    },

    openAddEstimate() {
      this.estimateDialog.isEdit = false
      this.estimateDialog.visible = true
      this.estimateForm = { estimateType: '', company: '', item: '', currentValue: '', unit: '', basis: '', changeReason: '' }
    },

    editEstimate(row) {
      this.estimateDialog.isEdit = true
      this.estimateDialog.visible = true
      this.estimateForm = { ...row }
    },

    viewEstimateHistory(row) {
      const params = {}
      if (row.company) params.companyName = row.company
      if (row.estimateType) params.estimateItem = row.estimateType
      getEstimateHistory(params).then(res => {
        if (res && res.result === 200 && res.data && res.data.tlist && res.data.tlist.length) {
          const historyText = res.data.tlist.map(h =>
            `${h.period || '-'}: ${h.previousValue || '-'} → ${h.currentValue || '-'}（${h.keyAssumption || ''}）`
          ).join('\n')
          this.$alert(historyText || '暂无历史变更记录', `${row.item} 历史变更`, {
            confirmButtonText: '关闭',
            customClass: 'history-dialog',
          })
        } else {
          this.$message.info('暂无历史变更记录')
        }
      }).catch(() => { this.$message.error('历史记录加载失败') })
    },

    submitEstimate() {
      this.estimateDialog.loading = true
      const fn = this.estimateDialog.isEdit ? updateEstimate : addEstimate
      fn(this.estimateForm).then(res => {
        if (res && res.result === 200) {
          this.$message.success('保存成功')
          this.estimateDialog.visible = false
          this.loadEstimateList()
        } else {
          this.$message.error((res && res.message) || '保存失败')
        }
      }).catch(() => {
        this.$message.error('保存失败，请稍后重试')
      }).finally(() => { this.estimateDialog.loading = false })
    },

    // ---- 估计变更分析 ----
    async loadChangeList() {
      try {
        const res = await getChangeAnalysisList(this.changeQuery)
        if (res && res.result === 200) {
          this.changeList = (res.data && res.data.tlist) || []
          if (res.data.trendMonths) this.trendMonths = res.data.trendMonths
        } else { this.changeList = [] }
      } catch (e) { this.changeList = [] }
    },

    changeRowClass({ row }) {
      return row.attention === '高' ? 'row-warning' : ''
    },

    // ---- 政策合规评估 ----
    async loadComplianceData() {
      try {
        const res = await getComplianceEvalData(this.complianceQuery)
        if (res && res.result === 200 && res.data) {
          this.complianceCards = res.data.complianceCards || []
          this.complianceList = res.data.complianceList || []
          if (res.data.companyList) this.companyList = res.data.companyList
        } else { this.complianceCards = []; this.complianceList = [] }
      } catch (e) { this.complianceCards = []; this.complianceList = [] }
    },

    loadComplianceList() {
      this.loadComplianceData()
    },

    runComplianceEval() {
      this.$message.info('正在执行合规评估...')
      apiRunCompliance().then(res => {
        if (res && res.result === 200 && res.data) {
          const d = res.data
          const s = d.summary || {}
          this.$message.success(`${d.message || '合规评估完成'}（共 ${s.totalCompany || 0} 家，通过 ${s.passCount || 0} 家，不通过 ${s.failCount || 0} 家）`)
          this.loadComplianceData()
        } else {
          this.$message.error((res && res.message) || '合规评估失败')
        }
      }).catch(() => { this.$message.error('合规评估失败') })
    },

    exportData() {
      const tabExportMap = {
        policy: { fn: exportPolicy, params: { policyType: this.policyQuery.type, companyName: this.policyQuery.company, status: this.policyQuery.status }, name: '会计政策台账' },
        consistency: { fn: exportPolicy, params: {}, name: '政策一致性检查' },
        estimate: { fn: exportPolicy, params: { type: this.estimateQuery.type, company: this.estimateQuery.company }, name: '会计估计台账' },
        changeAnalysis: { fn: exportPolicy, params: { period: this.changeQuery.period, attention: this.changeQuery.attention }, name: '估计变更分析' },
        compliance: { fn: exportPolicy, params: { company: this.complianceQuery.company, year: this.complianceQuery.year }, name: '政策合规评估' }
      }
      const config = tabExportMap[this.activeTab]
      if (!config) { this.$message.warning('当前页签暂不支持导出'); return }
      this.$message.info('正在导出，请稍候...')
      config.fn(config.params).then(res => {
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = config.name + '.xlsx'
          a.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败')
        }
      }).catch(() => { this.$message.error('导出失败，请稍后重试') })
    },

    // ---- 下发整改 ----
    openRectification(row) {
      this.rectificationDialog.visible = true
      this.rectificationForm = {
        companyName: row.company || '',
        policyType: row.item || '',
        diffLevel: row.diffLevel || '重大',
        diffDesc: row.diff || '',
        requirement: '',
        deadline: ''
      }
    },

    submitRectification() {
      if (!this.rectificationForm.requirement) {
        this.$message.warning('请填写整改要求')
        return
      }
      if (!this.rectificationForm.deadline) {
        this.$message.warning('请选择整改期限')
        return
      }
      this.rectificationDialog.loading = true
      issueRectification(this.rectificationForm).then(res => {
        if (res && res.result === 200) {
          this.$message.success('整改通知已下发')
          this.rectificationDialog.visible = false
          this.loadConsistencyList()
        } else {
          this.$message.error((res && res.msg) || '下发失败')
        }
      }).catch(() => {
        this.$message.error('下发失败，请稍后重试')
      }).finally(() => { this.rectificationDialog.loading = false })
    },

    // ---- 核查 ----
    openVerification(row) {
      this.verificationDialog.visible = true
      this.verificationForm = {
        companyName: row.company || '',
        estimateItem: row.estimateItem || '',
        changeBefore: String(row.before || ''),
        changeAfter: String(row.after || ''),
        profitImpact: String(row.profitImpact || ''),
        opinion: '',
        result: ''
      }
    },

    submitVerification() {
      if (!this.verificationForm.opinion) {
        this.$message.warning('请填写核查意见')
        return
      }
      if (!this.verificationForm.result) {
        this.$message.warning('请选择核查结论')
        return
      }
      this.verificationDialog.loading = true
      apiSubmitVerification(this.verificationForm).then(res => {
        if (res && res.result === 200) {
          this.$message.success('核查意见已提交')
          this.verificationDialog.visible = false
          this.loadChangeList()
        } else {
          this.$message.error((res && res.msg) || '提交失败')
        }
      }).catch(() => {
        this.$message.error('提交失败，请稍后重试')
      }).finally(() => { this.verificationDialog.loading = false })
    }
  }
}
</script>

<style lang="scss" scoped>
.policy-detail {
  ::v-deep .el-descriptions-item__label { width: 120px; font-weight: 600; background: #fafafa; }
  ::v-deep .el-descriptions-item__content { word-break: break-all; }
}

.policy-estimate-container {
  padding: 16px;
  background: #f5f7fa;
  min-height: calc(100vh - 80px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  border-radius: 8px;
  padding: 16px 24px;
  margin-bottom: 16px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .header-icon {
      font-size: 28px;
    }

    .header-title {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
    }

    .header-desc {
      margin: 4px 0 0;
      font-size: 13px;
      opacity: 0.85;
    }
  }
}

.stat-bar {
  display: flex;
  background: #fff;
  border-radius: 8px;
  padding: 16px 0;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);

  .stat-item {
    flex: 1;
    text-align: center;
    border-right: 1px solid #ebeef5;

    &:last-child { border-right: none; }

    .stat-value {
      display: block;
      font-size: 24px;
      font-weight: 700;
      line-height: 1.2;
    }

    .stat-label {
      display: block;
      font-size: 12px;
      color: #909399;
      margin-top: 4px;
    }
  }
}

.main-tabs {
  background: #fff;
  border-radius: 8px;
  padding: 0 16px 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);

  ::v-deep .el-tabs__header {
    margin-bottom: 16px;
  }
}

.tab-toolbar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 0 12px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 12px;
}

.check-summary {
  display: flex;
  gap: 16px;
  margin-left: auto;

  .summary-item {
    font-size: 13px;
    padding: 4px 12px;
    border-radius: 4px;
    b { font-size: 16px; margin-left: 4px; }

    &.danger { color: #FF4D4F; background: #FFF2F0; }
    &.warning { color: #FA8C16; background: #FFF7E6; }
    &.success { color: #52C41A; background: #F6FFED; }
  }
}

.diff-text {
  font-size: 12px;
  &.danger { color: #FF4D4F; }
  &.warning { color: #FA8C16; }
  &.success { color: #52C41A; }
}

.text-danger { color: #FF4D4F; }
.text-warning { color: #FA8C16; }
.text-success { color: #52C41A; }
.text-gray { color: #C0C4CC; }
.bold { font-weight: 700; }

.risk-flag {
  color: #FA8C16;
  font-size: 12px;
}

::v-deep .row-danger td { background: #fff2f0 !important; }
::v-deep .row-warning td { background: #fff7e6 !important; }

.pagination {
  margin-top: 12px;
  text-align: right;
}

// 估计变更趋势
.trend-section {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;

  .section-title {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 12px;
  }
}

.trend-bars {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  height: 120px;
  padding: 0 8px;

  .trend-bar-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;

    .bar-wrap {
      flex: 1;
      width: 100%;
      display: flex;
      align-items: flex-end;
      background: #f5f7fa;
      border-radius: 3px 3px 0 0;
      overflow: hidden;
    }

    .bar-fill {
      width: 100%;
      border-radius: 3px 3px 0 0;
      transition: height 0.3s;
      min-height: 2px;
    }

    .bar-label {
      font-size: 11px;
      color: #909399;
      margin-top: 4px;
    }

    .bar-value {
      font-size: 11px;
      font-weight: 600;
    }
  }
}

// 政策合规评分卡
.compliance-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 16px;

  @media (max-width: 1400px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.compliance-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px;
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }

  .cc-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .cc-company {
      font-size: 15px;
      font-weight: 600;
      color: #303133;
    }

    .cc-score {
      font-size: 22px;
      font-weight: 700;
    }
  }

  .cc-dimensions {
    .cc-dim {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      .dim-name {
        width: 80px;
        font-size: 12px;
        color: #606266;
        flex-shrink: 0;
      }

      .dim-score {
        width: 28px;
        font-size: 12px;
        font-weight: 600;
        text-align: right;
        color: #303133;
      }
    }
  }

  .cc-issues {
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px solid #fafafa;

    .cc-issue {
      font-size: 12px;
      color: #FA8C16;
      margin-bottom: 4px;
    }
  }
}

.tab-toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 12px;

  .toolbar-right {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-left: auto;
    padding-top: 4px;
  }
}

.column-selector {
  .selector-title {
    font-size: 13px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 8px;
    padding-bottom: 6px;
    border-bottom: 1px solid #ebeef5;
  }

  .el-checkbox {
    display: block;
    margin-left: 0;
    margin-bottom: 4px;
  }
}
</style>
