<template>
  <div class="risk-assessment">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>风险评估管理</h2>
      <p>全面的预算风险识别、评估和监控，提供智能风险预警和应对策略</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAssessment">创建评估</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-warning" @click="handleRiskAnalysis">风险分析</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleRiskReport">风险报告</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">评估设置</el-button>
            <el-button icon="el-icon-bell" @click="handleAlerts">风险预警</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 风险统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ riskStats.totalRisks }}</div>
            <div class="stat-label">风险总数</div>
            <div class="stat-description">识别的风险数量</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>风险识别</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card high-risk-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ riskStats.highRisks }}</div>
            <div class="stat-label">高风险</div>
            <div class="stat-description">需要重点关注</div>
            <div class="stat-trend">
              <i class="el-icon-close"></i>
              <span>高危预警</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-close"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card coverage-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ riskStats.coverage }}%</div>
            <div class="stat-label">覆盖率</div>
            <div class="stat-description">风险评估覆盖率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>全面覆盖</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card mitigation-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ riskStats.mitigationRate }}%</div>
            <div class="stat-label">缓解率</div>
            <div class="stat-description">风险缓解成功率</div>
            <div class="stat-trend">
              <i class="el-icon-check"></i>
              <span>有效缓解</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 风险类型选择 -->
    <el-card class="risk-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>风险类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshRiskTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="riskType in riskTypes" :key="riskType.category">
          <el-card
            class="risk-type-item"
            shadow="hover"
            @click.native="handleSelectRiskType(riskType)"
            :class="{ 'selected': selectedRiskType === riskType.category }"
          >
            <div class="risk-type-icon">
              <i :class="riskType.icon"></i>
            </div>
            <div class="risk-type-title">{{ riskType.name }}</div>
            <div class="risk-type-description">{{ riskType.description }}</div>
            <div class="risk-type-stats">
              <span class="risk-count">{{ riskType.riskCount }} 个风险</span>
              <span class="risk-level" :class="getRiskLevelClass(riskType.avgLevel)">
                {{ getRiskLevelText(riskType.avgLevel) }}
              </span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 风险评估列表 -->
    <el-card class="risk-assessments-card" shadow="never">
      <div slot="header" class="card-header">
        <span>风险评估</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索风险"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getRiskAssessmentList"
            clearable
            @clear="getRiskAssessmentList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getRiskAssessmentList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="riskAssessmentList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="riskItem" label="风险名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.riskItem }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="riskCategory" label="风险类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeColor(scope.row.riskCategory)" size="mini">
              {{ getRiskTypeText(scope.row.riskCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="occurrenceProbability" label="发生概率" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.occurrenceProbability || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="impactDegree" label="影响程度" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.impactDegree || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100" align="center">
          <template slot-scope="scope">
            <span class="risk-score" :class="getRiskScoreClass(scope.row.riskScore)">
              {{ scope.row.riskScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessor" label="评估人" width="100" align="center" />
        <el-table-column prop="assessmentDate" label="评估日期" width="120" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="mitigate">缓解措施</el-dropdown-item>
                <el-dropdown-item command="monitor">监控设置</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 创建/编辑评估弹窗 -->
    <el-dialog :title="formDialogTitle" :visible.sync="formDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="assessmentForm" :rules="formRules" ref="assessmentForm" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险名称" prop="riskItem">
              <el-input v-model="assessmentForm.riskItem" placeholder="请输入风险名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险类别" prop="riskCategory">
              <el-select v-model="assessmentForm.riskCategory" placeholder="请选择" style="width:100%">
                <el-option label="财务风险" value="FINANCIAL" />
                <el-option label="市场风险" value="MARKET" />
                <el-option label="操作风险" value="OPERATIONAL" />
                <el-option label="合规风险" value="COMPLIANCE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="assessmentForm.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高风险" value="HIGH" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="低风险" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发生概率">
              <el-input v-model="assessmentForm.occurrenceProbability" placeholder="如：高/中/低 或 70%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="影响程度">
              <el-input v-model="assessmentForm.impactDegree" placeholder="如：高/中/低" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评分">
              <el-input-number v-model="assessmentForm.riskScore" :min="0" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评估人">
              <el-input v-model="assessmentForm.assessor" placeholder="请输入评估人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="assessmentForm.status" placeholder="请选择" style="width:100%">
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="监控中" value="MONITORING" />
                <el-option label="已缓解" value="MITIGATED" />
                <el-option label="已关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评估日期">
              <el-date-picker v-model="assessmentForm.assessmentDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择评估日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="风险描述">
          <el-input v-model="assessmentForm.riskDescription" type="textarea" :rows="3" placeholder="请输入风险描述" />
        </el-form-item>
        <el-form-item label="应对措施">
          <el-input v-model="assessmentForm.countermeasures" type="textarea" :rows="3" placeholder="请输入应对措施" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitAssessmentForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="风险评估详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="viewDetail">
        <el-descriptions-item label="风险名称">{{ viewDetail.riskItem }}</el-descriptions-item>
        <el-descriptions-item label="风险类别">{{ getRiskTypeText(viewDetail.riskCategory) }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelColor(viewDetail.riskLevel)" size="mini">{{ getRiskLevelText(viewDetail.riskLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发生概率">{{ viewDetail.occurrenceProbability || '-' }}</el-descriptions-item>
        <el-descriptions-item label="影响程度">{{ viewDetail.impactDegree || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险评分">{{ viewDetail.riskScore != null ? viewDetail.riskScore : '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusColor(viewDetail.status)" size="mini">{{ getStatusText(viewDetail.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估人">{{ viewDetail.assessor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="评估日期" :span="2">{{ viewDetail.assessmentDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险描述" :span="2">{{ viewDetail.riskDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="应对措施" :span="2">{{ viewDetail.countermeasures || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="viewDialogVisible = false; handleEdit(viewDetail)">编 辑</el-button>
      </div>
    </el-dialog>

    <!-- 风险分析弹窗 -->
    <el-dialog title="风险分析" :visible.sync="analysisDialogVisible" width="700px">
      <div v-loading="analysisLoading">
        <el-descriptions :column="2" border v-if="analysisData">
          <el-descriptions-item label="总风险数">{{ analysisData.totalRisks || 0 }}</el-descriptions-item>
          <el-descriptions-item label="高风险数">{{ analysisData.highRisks || 0 }}</el-descriptions-item>
          <el-descriptions-item label="覆盖率">{{ analysisData.coverage || 0 }}%</el-descriptions-item>
          <el-descriptions-item label="缓解率">{{ analysisData.mitigationRate || 0 }}%</el-descriptions-item>
        </el-descriptions>
        <el-table :data="riskAssessmentList" border stripe style="margin-top:15px" max-height="300">
          <el-table-column prop="riskItem" label="风险名称" show-overflow-tooltip />
          <el-table-column prop="riskLevel" label="等级" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="getRiskLevelColor(scope.row.riskLevel)" size="mini">{{ getRiskLevelText(scope.row.riskLevel) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="riskScore" label="评分" width="80" align="center" />
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer"><el-button @click="analysisDialogVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- 风险报告弹窗 -->
    <el-dialog title="风险报告" :visible.sync="reportDialogVisible" width="700px">
      <div v-loading="reportLoading">
        <h4 style="margin-bottom:10px">风险评估报告摘要</h4>
        <el-descriptions :column="2" border v-if="analysisData">
          <el-descriptions-item label="报告日期">{{ new Date().toLocaleDateString() }}</el-descriptions-item>
          <el-descriptions-item label="风险总数">{{ analysisData.totalRisks || 0 }}</el-descriptions-item>
          <el-descriptions-item label="高风险">{{ analysisData.highRisks || 0 }}</el-descriptions-item>
          <el-descriptions-item label="缓解率">{{ analysisData.mitigationRate || 0 }}%</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin:15px 0 10px">风险类型分布</h4>
        <el-table :data="riskTypes" border stripe max-height="200">
          <el-table-column prop="name" label="类型" />
          <el-table-column prop="riskCount" label="数量" width="80" align="center" />
          <el-table-column prop="avgLevel" label="主要等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getRiskLevelColor(scope.row.avgLevel)" size="mini">{{ getRiskLevelText(scope.row.avgLevel) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer"><el-button @click="reportDialogVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- 评估设置弹窗 -->
    <el-dialog title="评估设置" :visible.sync="settingsDialogVisible" width="600px">
      <el-form label-width="120px">
        <el-form-item label="评估周期">
          <el-select v-model="settingsForm.assessmentCycle" style="width:100%">
            <el-option label="每月" value="MONTHLY" />
            <el-option label="每季度" value="QUARTERLY" />
            <el-option label="每半年" value="SEMI_ANNUAL" />
            <el-option label="每年" value="ANNUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险阈值(高)">
          <el-input-number v-model="settingsForm.highThreshold" :min="0" :max="100" style="width:100%" />
        </el-form-item>
        <el-form-item label="风险阈值(中)">
          <el-input-number v-model="settingsForm.mediumThreshold" :min="0" :max="100" style="width:100%" />
        </el-form-item>
        <el-form-item label="自动预警">
          <el-switch v-model="settingsForm.autoAlert" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 风险预警弹窗 -->
    <el-dialog title="风险预警" :visible.sync="alertsDialogVisible" width="650px">
      <el-alert v-if="riskStats.highRisks > 0" title="存在高风险项目，请及时处理" type="error" show-icon style="margin-bottom:10px" />
      <el-alert v-else title="当前无高风险预警" type="success" show-icon style="margin-bottom:10px" />
      <el-table :data="riskAssessmentList.filter(r => r.riskLevel === 'HIGH')" border stripe max-height="300">
        <el-table-column prop="riskItem" label="风险名称" show-overflow-tooltip />
        <el-table-column prop="riskCategory" label="类别" width="100" align="center">
          <template slot-scope="scope">{{ getRiskTypeText(scope.row.riskCategory) }}</template>
        </el-table-column>
        <el-table-column prop="riskScore" label="评分" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer"><el-button @click="alertsDialogVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="帮助" :visible.sync="helpDialogVisible" width="550px">
      <div style="line-height:2">
        <h4>风险评估管理使用说明</h4>
        <p><b>1. 创建评估：</b>点击"创建评估"按钮，填写风险信息后提交。</p>
        <p><b>2. 风险类型筛选：</b>点击风险类型卡片可按类别筛选列表。</p>
        <p><b>3. 风险分析：</b>查看当前风险的统计分析数据。</p>
        <p><b>4. 风险报告：</b>生成风险评估报告摘要。</p>
        <p><b>5. 评估设置：</b>配置评估周期和风险阈值。</p>
        <p><b>6. 风险预警：</b>查看高风险预警信息。</p>
      </div>
      <div slot="footer"><el-button @click="helpDialogVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- 缓解措施弹窗 -->
    <el-dialog title="缓解措施" :visible.sync="mitigateDialogVisible" width="600px">
      <el-descriptions :column="1" border v-if="currentRow">
        <el-descriptions-item label="风险名称">{{ currentRow.riskItem }}</el-descriptions-item>
        <el-descriptions-item label="当前等级">
          <el-tag :type="getRiskLevelColor(currentRow.riskLevel)" size="mini">{{ getRiskLevelText(currentRow.riskLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="应对措施">{{ currentRow.countermeasures || '暂无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="mitigateDialogVisible = false">关 闭</el-button></div>
    </el-dialog>

    <!-- 监控设置弹窗 -->
    <el-dialog title="监控设置" :visible.sync="monitorDialogVisible" width="600px">
      <el-descriptions :column="1" border v-if="currentRow">
        <el-descriptions-item label="风险名称">{{ currentRow.riskItem }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusColor(currentRow.status)" size="mini">{{ getStatusText(currentRow.status) }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <el-form label-width="100px" style="margin-top:15px">
        <el-form-item label="监控频率">
          <el-select v-model="monitorForm.frequency" style="width:100%">
            <el-option label="每日" value="DAILY" />
            <el-option label="每周" value="WEEKLY" />
            <el-option label="每月" value="MONTHLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警通知">
          <el-switch v-model="monitorForm.alertEnabled" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="monitorDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="monitorDialogVisible = false; $message.success('监控设置已保存')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'RiskAssessment',
  data() {
    return {
      // 统计数据
      riskStats: {
        totalRisks: 0,
        highRisks: 0,
        coverage: 0,
        mitigationRate: 0
      },

      // 风险类型
      riskTypes: [],
      selectedRiskType: null,

      // 风险评估列表
      riskAssessmentList: [],
      loading: false,
      searchKeyword: '',

      // 弹窗控制
      formDialogVisible: false,
      formDialogTitle: '创建评估',
      isEdit: false,
      submitLoading: false,
      viewDialogVisible: false,
      viewDetail: null,
      analysisDialogVisible: false,
      analysisLoading: false,
      analysisData: null,
      reportDialogVisible: false,
      reportLoading: false,
      settingsDialogVisible: false,
      alertsDialogVisible: false,
      helpDialogVisible: false,
      mitigateDialogVisible: false,
      monitorDialogVisible: false,
      currentRow: null,

      // 表单数据
      assessmentForm: {
        riskItem: '',
        riskCategory: '',
        riskLevel: '',
        occurrenceProbability: '',
        impactDegree: '',
        riskScore: 0,
        assessor: '',
        status: 'ACTIVE',
        riskDescription: '',
        countermeasures: '',
        assessmentDate: ''
      },
      formRules: {
        riskItem: [{ required: true, message: '请输入风险名称', trigger: 'blur' }],
        riskCategory: [{ required: true, message: '请选择风险类别', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
      },
      settingsForm: {
        assessmentCycle: 'QUARTERLY',
        highThreshold: 80,
        mediumThreshold: 60,
        autoAlert: true
      },
      monitorForm: {
        frequency: 'WEEKLY',
        alertEnabled: true
      }
    }
  },

  created() {
    this.getRiskAssessmentList()
    this.getRiskStats()
    this.getRiskCategoryStats()
  },

  methods: {
    // 获取风险评估列表
    async getRiskAssessmentList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedRiskType) {
          const categoryToTypeMap = {
            'FINANCIAL': 1,
            'MARKET': 2,
            'OPERATIONAL': 3,
            'COMPLIANCE': 4
          }
          params.riskType = categoryToTypeMap[this.selectedRiskType] || this.selectedRiskType
        }
        const response = await advancedFeaturesApi.getRiskAssessmentList(params)
        if (response && response.code === 1) {
          const d = response.data || {}
          this.riskAssessmentList = d.list || d || []
          this.total = d.totalCount || d.total || 0
        }
      } catch (error) {
        this.$message.error('获取风险评估列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getRiskStats() {
      try {
        const response = await advancedFeaturesApi.getRiskAssessmentStats()
        if (response && response.code === 1 && response.data) {
          this.riskStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 创建评估
    handleCreateAssessment() {
      this.isEdit = false
      this.formDialogTitle = '创建评估'
      this.assessmentForm = {
        riskItem: '', riskCategory: '', riskLevel: '',
        occurrenceProbability: '', impactDegree: '',
        riskScore: 0, assessor: '', status: 'ACTIVE',
        riskDescription: '', countermeasures: '',
        assessmentDate: ''
      }
      this.formDialogVisible = true
    },

    // 编辑评估
    handleEdit(row) {
      this.isEdit = true
      this.formDialogTitle = '编辑评估'
      this.assessmentForm = {
        riskId: row.riskId,
        riskItem: row.riskItem || '',
        riskCategory: row.riskCategory || '',
        riskLevel: row.riskLevel || '',
        occurrenceProbability: row.occurrenceProbability || '',
        impactDegree: row.impactDegree || '',
        riskScore: row.riskScore || 0,
        assessor: row.assessor || '',
        status: row.status || 'ACTIVE',
        riskDescription: row.riskDescription || '',
        countermeasures: row.countermeasures || '',
        assessmentDate: row.assessmentDate || ''
      }
      this.formDialogVisible = true
    },

    // 查看详情
    async handleView(row) {
      try {
        const response = await advancedFeaturesApi.getRiskAssessmentDetail(row.riskId)
        if (response && response.code === 1 && response.data) {
          this.viewDetail = response.data
        } else {
          this.viewDetail = row
        }
      } catch (e) {
        this.viewDetail = row
      }
      this.viewDialogVisible = true
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'mitigate':
          this.handleMitigate(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'export':
          this.handleExport(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 缓解措施
    handleMitigate(row) {
      this.currentRow = row
      this.mitigateDialogVisible = true
    },

    // 风险监控
    handleMonitor(row) {
      this.currentRow = row
      this.monitorDialogVisible = true
    },

    // 导出
    async handleExport(row) {
      try {
        await advancedFeaturesApi.exportRiskAssessment(row.riskId)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 复制
    async handleCopy(row) {
      try {
        await advancedFeaturesApi.copyRiskAssessment(row.riskId)
        this.$message.success('复制成功')
        this.getRiskAssessmentList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定删除该风险评估吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteRiskAssessment(row.riskId)
          this.$message.success('删除成功')
          this.getRiskAssessmentList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 刷新
    handleRefresh() {
      this.getRiskAssessmentList()
      this.getRiskStats()
      this.getRiskCategoryStats()
    },

    // 风险分析
    async handleRiskAnalysis() {
      this.analysisDialogVisible = true
      this.analysisLoading = true
      try {
        const response = await advancedFeaturesApi.getRiskAssessmentStats()
        if (response && response.code === 1 && response.data) {
          this.analysisData = response.data
        }
      } catch (e) {
        console.error('获取分析数据失败', e)
      } finally {
        this.analysisLoading = false
      }
    },

    // 风险报告
    async handleRiskReport() {
      this.reportDialogVisible = true
      this.reportLoading = true
      try {
        const response = await advancedFeaturesApi.getRiskAssessmentStats()
        if (response && response.code === 1 && response.data) {
          this.analysisData = response.data
        }
      } catch (e) {
        console.error('获取报告数据失败', e)
      } finally {
        this.reportLoading = false
      }
    },

    // 评估设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 风险预警
    handleAlerts() {
      this.alertsDialogVisible = true
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新风险类型
    refreshRiskTypes() {
      this.getRiskAssessmentList()
      this.getRiskStats()
      this.getRiskCategoryStats()
      this.$message.success('已刷新')
    },

    // 选择风险类型
    handleSelectRiskType(riskType) {
      if (this.selectedRiskType === riskType.category) {
        this.selectedRiskType = null
      } else {
        this.selectedRiskType = riskType.category
      }
      this.getRiskAssessmentList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 获取风险类型颜色
    getRiskTypeColor(type) {
      const colorMap = {
        'FINANCIAL': 'danger',
        'MARKET': 'warning',
        'OPERATIONAL': 'primary',
        'COMPLIANCE': 'success'
      }
      return colorMap[type] || 'info'
    },

    // 获取风险类型文本
    getRiskTypeText(type) {
      const textMap = {
        'FINANCIAL': '财务风险',
        'MARKET': '市场风险',
        'OPERATIONAL': '操作风险',
        'COMPLIANCE': '合规风险'
      }
      return textMap[type] || type
    },

    // 获取风险等级颜色
    getRiskLevelColor(level) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[level] || 'info'
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const textMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return textMap[level] || level
    },

    // 获取风险等级样式类
    getRiskLevelClass(level) {
      return `risk-level-${level.toLowerCase()}`
    },

    // 获取风险评分样式类
    getRiskScoreClass(score) {
      if (score >= 80) return 'high-score'
      if (score >= 60) return 'medium-score'
      return 'low-score'
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'danger',
        'MONITORING': 'warning',
        'MITIGATED': 'success',
        'CLOSED': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'MONITORING': '监控中',
        'MITIGATED': '已缓解',
        'CLOSED': '已关闭'
      }
      return textMap[status] || status
    },

    // 获取风险类型分类统计
    async getRiskCategoryStats() {
      try {
        const response = await advancedFeaturesApi.getRiskCategoryStats()
        if (response && response.code === 1 && response.data) {
          const iconMap = {
            'FINANCIAL': 'el-icon-money',
            'MARKET': 'el-icon-trend-charts',
            'OPERATIONAL': 'el-icon-setting',
            'COMPLIANCE': 'el-icon-document'
          }
          const nameMap = {
            'FINANCIAL': '财务风险',
            'MARKET': '市场风险',
            'OPERATIONAL': '操作风险',
            'COMPLIANCE': '合规风险'
          }
          const descMap = {
            'FINANCIAL': '资金流动性和财务状况风险',
            'MARKET': '市场变化和竞争风险',
            'OPERATIONAL': '业务流程和操作风险',
            'COMPLIANCE': '法规合规和政策风险'
          }
          const data = response.data
          if (Array.isArray(data)) {
            this.riskTypes = data.map(item => ({
              category: item.category || item.riskCategory,
              name: nameMap[item.category || item.riskCategory] || item.category,
              description: descMap[item.category || item.riskCategory] || '',
              icon: iconMap[item.category || item.riskCategory] || 'el-icon-warning',
              riskCount: item.count || item.riskCount || 0,
              avgLevel: item.avgLevel || 'MEDIUM'
            }))
          } else {
            // 如果返回的是对象格式 {FINANCIAL: 5, MARKET: 3, ...}
            const categories = ['FINANCIAL', 'MARKET', 'OPERATIONAL', 'COMPLIANCE']
            this.riskTypes = categories.map(cat => ({
              category: cat,
              name: nameMap[cat],
              description: descMap[cat],
              icon: iconMap[cat],
              riskCount: data[cat] || 0,
              avgLevel: 'MEDIUM'
            }))
          }
        } else {
          // API 失败时使用默认数据
          this.riskTypes = [
            { category: 'FINANCIAL', name: '财务风险', description: '资金流动性和财务状况风险', icon: 'el-icon-money', riskCount: 0, avgLevel: 'MEDIUM' },
            { category: 'MARKET', name: '市场风险', description: '市场变化和竞争风险', icon: 'el-icon-trend-charts', riskCount: 0, avgLevel: 'MEDIUM' },
            { category: 'OPERATIONAL', name: '操作风险', description: '业务流程和操作风险', icon: 'el-icon-setting', riskCount: 0, avgLevel: 'MEDIUM' },
            { category: 'COMPLIANCE', name: '合规风险', description: '法规合规和政策风险', icon: 'el-icon-document', riskCount: 0, avgLevel: 'MEDIUM' }
          ]
        }
      } catch (error) {
        console.error('获取风险类型统计失败：', error)
        this.riskTypes = [
          { category: 'FINANCIAL', name: '财务风险', description: '资金流动性和财务状况风险', icon: 'el-icon-money', riskCount: 0, avgLevel: 'MEDIUM' },
          { category: 'MARKET', name: '市场风险', description: '市场变化和竞争风险', icon: 'el-icon-trend-charts', riskCount: 0, avgLevel: 'MEDIUM' },
          { category: 'OPERATIONAL', name: '操作风险', description: '业务流程和操作风险', icon: 'el-icon-setting', riskCount: 0, avgLevel: 'MEDIUM' },
          { category: 'COMPLIANCE', name: '合规风险', description: '法规合规和政策风险', icon: 'el-icon-document', riskCount: 0, avgLevel: 'MEDIUM' }
        ]
      }
    },

    // 提交评估表单
    submitAssessmentForm() {
      this.$refs.assessmentForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.isEdit) {
            response = await advancedFeaturesApi.updateRiskAssessment(this.assessmentForm)
          } else {
            response = await advancedFeaturesApi.createRiskAssessment(this.assessmentForm)
          }
          if (response && response.code === 1) {
            this.$message.success(this.isEdit ? '编辑成功' : '创建成功')
            this.formDialogVisible = false
            this.getRiskAssessmentList()
            this.getRiskStats()
            this.getRiskCategoryStats()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.risk-assessment { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { margin: 0 0 5px; font-size: 20px; }
.page-header p { margin: 0; color: #909399; font-size: 14px; }
.toolbar-card { margin-bottom: 20px; }
.text-right { text-align: right; }
.stats-row { margin-bottom: 20px; }
.stat-card { position: relative; overflow: hidden; }
.stat-card .stat-content { position: relative; z-index: 1; }
.stat-card .stat-number { font-size: 28px; font-weight: bold; color: #303133; }
.stat-card .stat-label { font-size: 14px; color: #606266; margin-top: 5px; }
.stat-card .stat-description { font-size: 12px; color: #909399; margin-top: 3px; }
.stat-card .stat-trend { font-size: 12px; color: #909399; margin-top: 8px; }
.stat-card .stat-icon { position: absolute; right: 15px; top: 15px; font-size: 48px; opacity: 0.15; }
.total-card .stat-number { color: #409EFF; }
.total-card .stat-icon { color: #409EFF; }
.high-risk-card .stat-number { color: #F56C6C; }
.high-risk-card .stat-icon { color: #F56C6C; }
.coverage-card .stat-number { color: #67C23A; }
.coverage-card .stat-icon { color: #67C23A; }
.mitigation-card .stat-number { color: #E6A23C; }
.mitigation-card .stat-icon { color: #E6A23C; }
.risk-types-card { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.risk-type-item { cursor: pointer; text-align: center; transition: all 0.3s; }
.risk-type-item:hover { transform: translateY(-3px); box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.risk-type-item.selected { border-color: #409EFF; box-shadow: 0 0 8px rgba(64,158,255,0.3); }
.risk-type-icon { font-size: 36px; color: #409EFF; margin-bottom: 10px; }
.risk-type-title { font-size: 16px; font-weight: bold; color: #303133; }
.risk-type-description { font-size: 12px; color: #909399; margin-top: 5px; }
.risk-type-stats { margin-top: 10px; display: flex; justify-content: space-between; align-items: center; }
.risk-count { font-size: 13px; color: #606266; }
.risk-level-high { color: #F56C6C; font-weight: bold; }
.risk-level-medium { color: #E6A23C; font-weight: bold; }
.risk-level-low { color: #67C23A; font-weight: bold; }
.header-tools { display: flex; align-items: center; }
.high-score { color: #F56C6C; font-weight: bold; }
.medium-score { color: #E6A23C; font-weight: bold; }
.low-score { color: #67C23A; font-weight: bold; }
</style>