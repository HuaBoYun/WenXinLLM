<template>
  <div class="app-container accounting-page">
    <div class="ap-header ap-header-danger" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-s-flag"></i><span>财务造假识别</span></div>
      <div class="ap-header-desc">重点打击五类造假 · 模型自动检测 · 线索自动派单</div>
    </div>

    <!-- 搜索筛选 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <el-form :model="searchForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="searchForm.companyName" placeholder="请输入企业名称" clearable style="width:180px" @keyup.enter.native="handleSearch" />
        </el-form-item>
        <el-form-item label="造假类型">
          <el-select v-model="searchForm.fraudType" placeholder="全部" clearable style="width:140px">
            <el-option label="业绩假" value="PERF" />
            <el-option label="杠杆假" value="LEVER" />
            <el-option label="出清假" value="CLEAR" />
            <el-option label="研发假" value="RD" />
            <el-option label="两金假" value="TWOGOLD" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="全部" clearable style="width:120px">
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MED" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 五类造假仪表盘 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="4" v-for="(f, i) in fraudIndexes" :key="i">
        <div class="fraud-index-card" :class="['fi-' + f.cls, activeTab === f.tab ? 'fi-active' : '']" @click="switchTab(f.tab)">
          <div class="fi-name">{{ f.name }}</div>
          <div class="fi-score">
            <span class="fi-num" :class="f.score >= 70 ? 'fi-red' : f.score >= 40 ? 'fi-orange' : 'fi-green'">{{ f.score }}</span>
            <span class="fi-unit">分</span>
          </div>
          <div class="fi-level">
            <el-tag :type="f.score >= 70 ? 'danger' : f.score >= 40 ? 'warning' : 'success'" size="mini" effect="plain">
              {{ f.score >= 70 ? '高风险' : f.score >= 40 ? '中风险' : '低风险' }}
            </el-tag>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <!-- 风险总览 -->
        <el-tab-pane label="造假风险总览" name="overview">
          <el-table v-loading="loading" :data="riskList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="综合风险指数" prop="totalScore" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.totalScore >= 70 ? 'score-red' : s.row.totalScore >= 40 ? 'score-orange' : 'score-green'" style="font-weight:700;font-size:16px">{{ s.row.totalScore }}</span>
              </template>
            </el-table-column>
            <el-table-column label="业绩假" prop="perfScore" width="75" align="center">
              <template slot-scope="s"><span :class="s.row.perfScore >= 70 ? 'score-red' : ''">{{ s.row.perfScore }}</span></template>
            </el-table-column>
            <el-table-column label="杠杆假" prop="leverScore" width="75" align="center">
              <template slot-scope="s"><span :class="s.row.leverScore >= 70 ? 'score-red' : ''">{{ s.row.leverScore }}</span></template>
            </el-table-column>
            <el-table-column label="出清假" prop="clearScore" width="75" align="center">
              <template slot-scope="s"><span :class="s.row.clearScore >= 70 ? 'score-red' : ''">{{ s.row.clearScore }}</span></template>
            </el-table-column>
            <el-table-column label="研发假" prop="rdScore" width="75" align="center">
              <template slot-scope="s"><span :class="s.row.rdScore >= 70 ? 'score-red' : ''">{{ s.row.rdScore }}</span></template>
            </el-table-column>
            <el-table-column label="两金假" prop="twoGoldScore" width="75" align="center">
              <template slot-scope="s"><span :class="s.row.twoGoldScore >= 70 ? 'score-red' : ''">{{ s.row.twoGoldScore }}</span></template>
            </el-table-column>
            <el-table-column label="预警级别" prop="alertLevel" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.alertLevel === 'RED' ? 'danger' : s.row.alertLevel === 'ORANGE' ? 'warning' : 'success'" size="mini">
                  {{ s.row.alertLevel === 'RED' ? '红色' : s.row.alertLevel === 'ORANGE' ? '橙色' : '绿色' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="待处置线索" prop="clueCount" width="100" align="center">
              <template slot-scope="s">
                <el-button v-if="s.row.clueCount > 0" type="text" size="mini" @click="viewClues(s.row)">
                  <el-badge :value="s.row.clueCount" type="danger">线索</el-badge>
                </el-button>
                <span v-else style="color:#c0c4cc">-</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 经营业绩分析 -->
        <el-tab-pane label="经营业绩分析" name="perf">
          <div class="rule-desc">
            <i class="el-icon-info" style="color:#1890ff;margin-right:6px"></i>
            识别目标：虚增收入、虚减成本。检测收入增长率偏离（>2倍标准差）、净利润现金含量（&lt;0.5）、期末大额收入（12月>40%）
          </div>
          <el-table v-loading="loading" :data="perfList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="收入增长率" prop="revenueGrowth" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.revenueAnomaly ? 'score-red' : ''">{{ s.row.revenueGrowth }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="行业均值" prop="industryAvg" width="90" align="center" />
            <el-table-column label="Z-score" prop="zScore" width="80" align="center">
              <template slot-scope="s">
                <span :class="s.row.zScore > 2 ? 'score-red' : ''">{{ s.row.zScore }}</span>
              </template>
            </el-table-column>
            <el-table-column label="毛利率" prop="grossMargin" width="80" align="center" />
            <el-table-column label="净利润现金含量" prop="cashContent" width="120" align="center">
              <template slot-scope="s">
                <span :class="s.row.cashContent < 0.5 ? 'score-red' : ''">{{ s.row.cashContent }}</span>
              </template>
            </el-table-column>
            <el-table-column label="12月收入占比" prop="dec12Rate" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.dec12Rate > 0.4 ? 'score-red' : ''">{{ (s.row.dec12Rate * 100).toFixed(1) }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'HIGH' ? 'danger' : s.row.riskLevel === 'MED' ? 'warning' : 'success'" size="mini">
                  {{ { HIGH: '高', MED: '中', LOW: '低' }[s.row.riskLevel] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 杠杆造假分析 -->
        <el-tab-pane label="杠杆造假分析" name="lever">
          <div class="rule-desc">
            <i class="el-icon-info" style="color:#1890ff;margin-right:6px"></i>
            识别目标：隐匿负债、虚降杠杆。检测资产负债率异常、利息保障倍数过低、表外负债及担保金额异常
          </div>
          <el-table v-loading="loading" :data="leverList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="资产负债率(%)" prop="debtRatio" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.debtRatio > 70 ? 'score-red' : s.row.debtRatio > 60 ? 'score-orange' : ''">{{ s.row.debtRatio }}</span>
              </template>
            </el-table-column>
            <el-table-column label="利息保障倍数" prop="interestCoverage" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.interestCoverage < 1.5 ? 'score-red' : ''">{{ s.row.interestCoverage }}</span>
              </template>
            </el-table-column>
            <el-table-column label="短期借款(万)" prop="shortDebt" width="110" align="right" />
            <el-table-column label="长期借款(万)" prop="longDebt" width="110" align="right" />
            <el-table-column label="表外负债(万)" prop="offBalanceDebt" width="110" align="right">
              <template slot-scope="s">
                <span :class="s.row.offBalanceDebt > 0 ? 'score-orange' : ''">{{ s.row.offBalanceDebt }}</span>
              </template>
            </el-table-column>
            <el-table-column label="担保金额(万)" prop="guaranteeAmount" width="110" align="right" />
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'HIGH' ? 'danger' : s.row.riskLevel === 'MED' ? 'warning' : 'success'" size="mini">
                  {{ { HIGH: '高', MED: '中', LOW: '低' }[s.row.riskLevel] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 出清造假分析 -->
        <el-tab-pane label="出清造假分析" name="clear">
          <div class="rule-desc">
            <i class="el-icon-info" style="color:#1890ff;margin-right:6px"></i>
            识别目标：虚假资产处置、关联方利益输送。检测处置收益占比异常、关联交易金额过大、公允价值偏差
          </div>
          <el-table v-loading="loading" :data="clearList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="处置金额(万)" prop="disposalAmount" width="110" align="right" />
            <el-table-column label="处置收益(万)" prop="disposalGain" width="110" align="right" />
            <el-table-column label="收益占比(%)" prop="disposalRate" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.disposalRate > 50 ? 'score-red' : s.row.disposalRate > 30 ? 'score-orange' : ''">{{ s.row.disposalRate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="关联交易(万)" prop="relatedPartyDeal" width="110" align="right">
              <template slot-scope="s">
                <span :class="s.row.relatedPartyDeal > 1000 ? 'score-red' : ''">{{ s.row.relatedPartyDeal }}</span>
              </template>
            </el-table-column>
            <el-table-column label="公允价值偏差(%)" prop="fairValueGap" width="120" align="center">
              <template slot-scope="s">
                <span :class="s.row.fairValueGap > 20 ? 'score-red' : ''">{{ s.row.fairValueGap }}</span>
              </template>
            </el-table-column>
            <el-table-column label="时点异常" prop="timingAnomaly" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.timingAnomaly === '1'" type="danger" size="mini">是</el-tag>
                <span v-else style="color:#c0c4cc">否</span>
              </template>
            </el-table-column>
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'HIGH' ? 'danger' : s.row.riskLevel === 'MED' ? 'warning' : 'success'" size="mini">
                  {{ { HIGH: '高', MED: '中', LOW: '低' }[s.row.riskLevel] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 研发统计核查 -->
        <el-tab-pane label="研发统计核查" name="rd">
          <div class="rule-desc">
            <i class="el-icon-info" style="color:#1890ff;margin-right:6px"></i>
            研发资本化率>50%橙色预警，>70%红色预警；检测日常费用混入研发、人均研发产出异常低
          </div>
          <el-table v-loading="loading" :data="rdList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="研发投入(万)" prop="rdAmount" width="110" align="right" />
            <el-table-column label="资本化金额(万)" prop="capitalAmount" width="120" align="right" />
            <el-table-column label="资本化率" prop="capitalRate" width="100" align="center">
              <template slot-scope="s">
                <span :class="s.row.capitalRate > 70 ? 'score-red' : s.row.capitalRate > 50 ? 'score-orange' : ''">{{ s.row.capitalRate }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="研发人员数" prop="rdStaff" width="100" align="center" />
            <el-table-column label="人均产出(万/人)" prop="outputPerPerson" width="120" align="right" />
            <el-table-column label="专利产出数" prop="patents" width="90" align="center" />
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'HIGH' ? 'danger' : s.row.riskLevel === 'MED' ? 'warning' : 'success'" size="mini">
                  {{ { HIGH: '高', MED: '中', LOW: '低' }[s.row.riskLevel] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 两金造假分析 -->
        <el-tab-pane label="两金造假分析" name="twogold">
          <div class="rule-desc">
            <i class="el-icon-info" style="color:#1890ff;margin-right:6px"></i>
            识别目标：虚增应收、虚增存货。检测应收/存货增长率异常、周转天数恶化、坏账/减值计提率偏低
          </div>
          <el-table v-loading="loading" :data="twogoldList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="应收增长率(%)" prop="receivableGrowth" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.receivableGrowth > 50 ? 'score-red' : s.row.receivableGrowth > 30 ? 'score-orange' : ''">{{ s.row.receivableGrowth }}</span>
              </template>
            </el-table-column>
            <el-table-column label="存货增长率(%)" prop="inventoryGrowth" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.inventoryGrowth > 50 ? 'score-red' : s.row.inventoryGrowth > 30 ? 'score-orange' : ''">{{ s.row.inventoryGrowth }}</span>
              </template>
            </el-table-column>
            <el-table-column label="应收周转(天)" prop="receivableTurnover" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.receivableTurnover > 180 ? 'score-red' : ''">{{ s.row.receivableTurnover }}</span>
              </template>
            </el-table-column>
            <el-table-column label="存货周转(天)" prop="inventoryTurnover" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.inventoryTurnover > 180 ? 'score-red' : ''">{{ s.row.inventoryTurnover }}</span>
              </template>
            </el-table-column>
            <el-table-column label="坏账计提率(%)" prop="badDebtRate" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.badDebtRate < 3 ? 'score-red' : ''">{{ s.row.badDebtRate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="减值计提率(%)" prop="impairmentRate" width="110" align="center">
              <template slot-scope="s">
                <span :class="s.row.impairmentRate < 2 ? 'score-red' : ''">{{ s.row.impairmentRate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="风险" prop="riskLevel" width="80" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'HIGH' ? 'danger' : s.row.riskLevel === 'MED' ? 'warning' : 'success'" size="mini">
                  {{ { HIGH: '高', MED: '中', LOW: '低' }[s.row.riskLevel] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 风险线索管理 -->
        <el-tab-pane label="风险线索管理" name="clue">
          <div style="margin-bottom:10px;display:flex;justify-content:space-between;align-items:center">
            <span class="tab-tip">
              <i class="el-icon-info"></i>
              <span v-if="clueCompanyFilter">当前筛选：<strong style="color:#303133">{{ clueCompanyFilter }}</strong> <el-button type="text" size="mini" @click="clearClueFilter">清除</el-button></span>
              <span v-else>高风险线索自动派单给责任监管人员</span>
            </span>
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="addClue">手动补录线索</el-button>
          </div>
          <el-table :data="clueList" border size="small" style="width:100%">
            <el-table-column label="企业名称" prop="companyName" min-width="120" show-overflow-tooltip />
            <el-table-column label="线索类型" prop="clueType" min-width="120">
              <template slot-scope="s"><el-tag type="danger" size="mini">{{ clueTypeMap[s.row.clueType] || s.row.clueType }}</el-tag></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="s.row.riskLevel === 'RED' ? 'danger' : 'warning'" size="mini">{{ s.row.riskLevel === 'RED' ? '红色' : '橙色' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="详细描述" prop="description" min-width="200" show-overflow-tooltip />
            <el-table-column label="发现时间" prop="foundTime" width="100" align="center" />
            <el-table-column label="负责人" prop="assignee" width="80" align="center" />
            <el-table-column label="处置状态" prop="status" width="90" align="center">
              <template slot-scope="s">
                <el-tag :type="{ PENDING: 'warning', PROCESSING: 'primary', DONE: 'success', CLOSED: 'info' }[s.row.status]" size="mini">
                  {{ { PENDING: '待核查', PROCESSING: '核查中', DONE: '已处置', CLOSED: '已关闭' }[s.row.status] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
              <template slot-scope="s">
                <el-button type="text" size="mini" @click="editClue(s.row)">编辑</el-button>
                <el-button type="text" size="mini" @click="updateStatus(s.row)">更新状态</el-button>
                <el-button type="text" size="mini" @click="viewDetail(s.row)">详情</el-button>
                <el-button type="text" size="mini" style="color:#F56C6C" @click="deleteClue(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增/编辑线索对话框 -->
    <el-dialog :visible.sync="clueDialog.visible" :title="clueDialog.isEdit ? '编辑线索' : '手动补录线索'" width="560px" append-to-body>
      <el-form :model="clueForm" :rules="clueRules" ref="clueForm" label-width="90px" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="clueForm.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="线索类型" prop="clueType">
          <el-select v-model="clueForm.clueType" placeholder="请选择" style="width:100%">
            <el-option label="收入跳涨" value="REVENUE_JUMP" />
            <el-option label="关联交易异常" value="RELATED_PARTY" />
            <el-option label="现金流异常" value="CASH_ABNORMAL" />
            <el-option label="研发资本化过高" value="RD_CAPITAL" />
            <el-option label="期末突击" value="END_RUSH" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="造假类型" prop="fraudType">
          <el-select v-model="clueForm.fraudType" placeholder="请选择" style="width:100%">
            <el-option label="业绩造假" value="PERF" />
            <el-option label="杠杆造假" value="LEVER" />
            <el-option label="出清造假" value="CLEAR" />
            <el-option label="研发造假" value="RD" />
            <el-option label="两金造假" value="TWOGOLD" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-radio-group v-model="clueForm.riskLevel">
            <el-radio label="RED">红色（高）</el-radio>
            <el-radio label="ORANGE">橙色（中）</el-radio>
            <el-radio label="YELLOW">黄色（低）</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="clueForm.description" type="textarea" :rows="3" placeholder="线索详细描述" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="clueForm.assignee" placeholder="指派责任监管人员" />
        </el-form-item>
        <el-form-item label="发现时间">
          <el-date-picker v-model="clueForm.foundTime" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="clueDialog.visible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="clueDialog.loading" @click="submitClue">保存</el-button>
      </div>
    </el-dialog>

    <!-- 线索详情 -->
    <el-dialog :visible.sync="clueDetailDialog.visible" title="线索详情" width="600px" append-to-body>
      <el-descriptions v-if="clueDetailDialog.data" :column="1" border size="small">
        <el-descriptions-item label="企业">{{ clueDetailDialog.data.companyName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="线索类型">{{ clueTypeMap[clueDetailDialog.data.clueType] || clueDetailDialog.data.clueType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="造假类型">{{ fraudTypeMap[clueDetailDialog.data.fraudType] || clueDetailDialog.data.fraudType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ riskLevelMap[clueDetailDialog.data.riskLevel] || clueDetailDialog.data.riskLevel || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ clueDetailDialog.data.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发现时间">{{ clueDetailDialog.data.foundTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ clueDetailDialog.data.assignee || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusMap[clueDetailDialog.data.status] || clueDetailDialog.data.status || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button size="small" @click="clueDetailDialog.visible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 状态更新 -->
    <el-dialog :visible.sync="statusDialog.visible" title="更新线索状态" width="360px" append-to-body>
      <el-form label-width="60px" size="small">
        <el-form-item label="状态">
          <el-select v-model="statusDialog.status" style="width:100%">
            <el-option label="待核查" value="PENDING" />
            <el-option label="核查中" value="PROCESSING" />
            <el-option label="已处置" value="DONE" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="statusDialog.visible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="statusDialog.loading" @click="submitStatus">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFraudRiskList, getFraudClueList, getFraudPerfList, getFraudRdList,
  getFraudLeverList, getFraudClearList, getFraudTwogoldList,
  addFraudClue, updateFraudClue, deleteFraudClue, getFraudClueDetail, updateFraudClueStatus
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
  name: 'AccountingFraudDetection',
  data() {
    return {
      loading: false,
      activeTab: 'overview',
      searchForm: { companyName: '', fraudType: '', riskLevel: '' },
      // 枚举中文映射
      clueTypeMap: { REVENUE_JUMP: '收入跳涨', RELATED_PARTY: '关联交易异常', CASH_ABNORMAL: '现金流异常', RD_CAPITAL: '研发资本化过高', END_RUSH: '期末突击', OTHER: '其他' },
      fraudTypeMap: { PERF: '业绩造假', LEVER: '杠杆造假', CLEAR: '出清造假', RD: '研发造假', TWOGOLD: '两金造假' },
      riskLevelMap: { RED: '红色（高）', ORANGE: '橙色（中）', YELLOW: '黄色（低）', HIGH: '高风险', MED: '中风险', LOW: '低风险' },
      statusMap: { PENDING: '待核查', PROCESSING: '核查中', DONE: '已处置', CLOSED: '已关闭', OPEN: '待处理' },
      fraudIndexes: [],
      riskList: [],
      perfList: [],
      leverList: [],
      clearList: [],
      rdList: [],
      twogoldList: [],
      clueList: [],
      clueCompanyFilter: '',
      clueDialog: { visible: false, isEdit: false, loading: false },
      clueForm: { clueId: '', companyName: '', clueType: '', fraudType: '', riskLevel: 'ORANGE', description: '', assignee: '', foundTime: '' },
      clueRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        clueType: [{ required: true, message: '请选择线索类型', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
      },
      clueDetailDialog: { visible: false, data: null },
      statusDialog: { visible: false, loading: false, clueId: '', status: 'PROCESSING' },
    }
  },
  created() { this.loadData() },
  watch: {
    activeTab(val) {
      if (val === 'overview') this.loadData()
      else if (val === 'perf') this.loadPerfList()
      else if (val === 'lever') this.loadLeverList()
      else if (val === 'clear') this.loadClearList()
      else if (val === 'rd') this.loadRdList()
      else if (val === 'twogold') this.loadTwogoldList()
      else if (val === 'clue') this.loadClueList()
    }
  },
  methods: {
    // ========== 搜索筛选 ==========
    handleSearch() {
      // 根据当前激活的tab重新加载对应数据
      const tab = this.activeTab
      if (tab === 'overview') this.loadData()
      else if (tab === 'perf') this.loadPerfList()
      else if (tab === 'lever') this.loadLeverList()
      else if (tab === 'clear') this.loadClearList()
      else if (tab === 'rd') this.loadRdList()
      else if (tab === 'twogold') this.loadTwogoldList()
      else if (tab === 'clue') this.loadClueList()
    },
    handleReset() {
      this.searchForm = { companyName: '', fraudType: '', riskLevel: '' }
      this.handleSearch()
    },
    // 构建搜索参数
    buildSearchParams() {
      const params = {}
      if (this.searchForm.companyName) params.companyName = this.searchForm.companyName
      if (this.searchForm.fraudType) params.fraudType = this.searchForm.fraudType
      if (this.searchForm.riskLevel) params.riskLevel = this.searchForm.riskLevel
      return params
    },
    // ========== 卡片切换 ==========
    switchTab(tab) {
      this.activeTab = tab
    },
    // ========== 数据加载 ==========
    async loadData() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudRiskList(params)
        if (res && res.result === 200 && res.data) {
          this.riskList = (res.data.tlist || res.data.list) || []
          if (res.data.fraudIndexes) this.fraudIndexes = res.data.fraudIndexes
        }
      } catch (e) { this.riskList = [] } finally { this.loading = false }
    },
    async loadPerfList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudPerfList(params)
        if (res && res.result === 200) { this.perfList = (res.data && res.data.tlist) || [] }
        else { this.perfList = [] }
      } catch (e) { this.perfList = [] } finally { this.loading = false }
    },
    async loadLeverList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudLeverList(params)
        if (res && res.result === 200) { this.leverList = (res.data && res.data.tlist) || [] }
        else { this.leverList = [] }
      } catch (e) { this.leverList = [] } finally { this.loading = false }
    },
    async loadClearList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudClearList(params)
        if (res && res.result === 200) { this.clearList = (res.data && res.data.tlist) || [] }
        else { this.clearList = [] }
      } catch (e) { this.clearList = [] } finally { this.loading = false }
    },
    async loadRdList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudRdList(params)
        if (res && res.result === 200) { this.rdList = (res.data && res.data.tlist) || [] }
        else { this.rdList = [] }
      } catch (e) { this.rdList = [] } finally { this.loading = false }
    },
    async loadTwogoldList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        const res = await getFraudTwogoldList(params)
        if (res && res.result === 200) { this.twogoldList = (res.data && res.data.tlist) || [] }
        else { this.twogoldList = [] }
      } catch (e) { this.twogoldList = [] } finally { this.loading = false }
    },
    // ========== 线索管理 ==========
    viewClues(row) {
      this.clueCompanyFilter = row.companyName
      this.activeTab = 'clue'
      this.loadClueList()
    },
    clearClueFilter() {
      this.clueCompanyFilter = ''
      this.loadClueList()
    },
    async loadClueList() {
      this.loading = true
      try {
        const params = this.buildSearchParams()
        if (this.clueCompanyFilter) params.companyName = this.clueCompanyFilter
        const res = await getFraudClueList(params)
        if (res && res.result === 200) { this.clueList = (res.data && res.data.tlist) || [] }
        else { this.clueList = [] }
      } catch (e) { this.clueList = [] } finally { this.loading = false }
    },
    addClue() {
      this.clueDialog.isEdit = false
      this.clueDialog.visible = true
      this.clueForm = {
        clueId: '', companyName: this.clueCompanyFilter || '', clueType: '', fraudType: '',
        riskLevel: 'ORANGE', description: '', assignee: '',
        foundTime: new Date().toISOString().substring(0, 10),
        status: 'PENDING',
      }
    },
    editClue(row) {
      this.clueDialog.isEdit = true
      this.clueDialog.visible = true
      this.clueForm = {
        clueId: row.clueId || row.id || '',
        companyName: row.companyName || '',
        clueType: row.clueType || '',
        fraudType: row.fraudType || '',
        riskLevel: row.riskLevel || 'ORANGE',
        description: row.description || '',
        assignee: row.assignee || '',
        foundTime: row.foundTime || '',
        status: row.status || 'PENDING',
      }
    },
    deleteClue(row) {
      const id = row.clueId || row.id
      if (!id) {
        this.$message.warning('该线索缺少 ID，无法删除')
        return
      }
      this.$confirm('确认删除该线索？删除后不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        deleteFraudClue(id).then(res => {
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.loadClueList()
          } else {
            this.$message.error((res && res.message) || '删除失败')
          }
        }).catch(() => { this.$message.error('删除失败') })
      }).catch(() => {})
    },
    submitClue() {
      this.$refs.clueForm.validate(valid => {
        if (!valid) return
        this.clueDialog.loading = true
        const payload = { ...this.clueForm }
        const fn = this.clueDialog.isEdit ? updateFraudClue : addFraudClue
        fn(payload).then(res => {
          if (res && res.result === 200) {
            this.$message.success('保存成功')
            this.clueDialog.visible = false
            this.loadClueList()
          } else {
            this.$message.error((res && res.message) || '保存失败')
          }
        }).catch(() => { this.$message.error('保存失败') })
          .finally(() => { this.clueDialog.loading = false })
      })
    },
    updateStatus(row) {
      if (!row.clueId && !row.id) {
        this.$message.warning('该线索缺少 ID，无法更新状态')
        return
      }
      this.statusDialog.visible = true
      this.statusDialog.clueId = row.clueId || row.id
      this.statusDialog.status = row.status || 'PROCESSING'
    },
    submitStatus() {
      this.statusDialog.loading = true
      updateFraudClueStatus({ clueId: this.statusDialog.clueId, status: this.statusDialog.status })
        .then(res => {
          if (res && res.result === 200) {
            this.$message.success('状态已更新')
            this.statusDialog.visible = false
            this.loadClueList()
          } else {
            this.$message.error((res && res.message) || '更新失败')
          }
        })
        .catch(() => { this.$message.error('更新失败') })
        .finally(() => { this.statusDialog.loading = false })
    },
    viewDetail(row) {
      const id = row.clueId || row.id
      if (!id) {
        this.clueDetailDialog.data = { ...row }
        this.clueDetailDialog.visible = true
        return
      }
      getFraudClueDetail(id).then(res => {
        if (res && res.result === 200) {
          this.clueDetailDialog.data = res.data || row
          this.clueDetailDialog.visible = true
        }
      }).catch(() => { this.$message.error('详情加载失败') })
    },
  },
}
</script>

<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f5f7fa; min-height: calc(100vh - 84px); }
.ap-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px; border-radius: 8px; color: #fff; margin-bottom: 14px;
  &.ap-header-danger { }
  .ap-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
  .ap-header-desc { font-size: 13px; opacity: 0.8; }
}
.fraud-index-card {
  background: #fff; border-radius: 10px; padding: 14px 16px; cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07); transition: all 0.25s; text-align: center;
  border-top: 3px solid #d9d9d9;
  &:hover { transform: translateY(-3px); box-shadow: 0 6px 16px rgba(0,0,0,0.12); }
  &.fi-active { transform: translateY(-3px); box-shadow: 0 6px 16px rgba(0,0,0,0.15); border-bottom: 2px solid #409EFF; }
  &.fi-red { border-top-color: #FF4D4F; }
  &.fi-orange { border-top-color: #FA8C16; }
  &.fi-green { border-top-color: #52C41A; }
  .fi-name { font-size: 12px; color: #909399; margin-bottom: 8px; }
  .fi-score { display: flex; align-items: baseline; justify-content: center; gap: 2px; }
  .fi-num { font-size: 28px; font-weight: 700; &.fi-red { color: #FF4D4F; } &.fi-orange { color: #FA8C16; } &.fi-green { color: #52C41A; } }
  .fi-unit { font-size: 13px; color: #909399; }
  .fi-level { margin-top: 8px; }
}
.rule-desc { font-size: 12px; color: #606266; background: #e6f7ff; border: 1px solid #91d5ff; border-radius: 6px; padding: 8px 12px; margin-bottom: 12px; }
.tab-tip { font-size: 12px; color: #909399; i { margin-right: 4px; color: #1A3A5C; } }
.score-red { color: #FF4D4F; font-weight: 700; }
.score-orange { color: #FA8C16; font-weight: 600; }
.score-green { color: #52C41A; font-weight: 600; }
::v-deep .el-table th { background: #f0f4f8; }
</style>
