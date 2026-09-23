<template>
  <div class="rolling-budget">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>滚动预算管理</h2>
      <p>动态滚动预算管理，支持多种滚动周期和预测模型，实现预算的持续更新和优化</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreatePlan">创建计划</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-video-play" @click="handleExecuteRolling">执行滚动</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewAnalysis">滚动分析</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">滚动设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">滚动报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 滚动预算统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ rollingStats.totalPlans }}</div>
            <div class="stat-label">滚动计划</div>
            <div class="stat-description">总滚动预算计划数</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>动态管理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ rollingStats.activePlans }}</div>
            <div class="stat-label">活跃计划</div>
            <div class="stat-description">正在执行的计划</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>运行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ rollingStats.accuracy }}%</div>
            <div class="stat-label">预测准确率</div>
            <div class="stat-description">滚动预测准确度</div>
            <div class="stat-trend">
              <i class="el-icon-data-analysis"></i>
              <span>高精度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-data-analysis"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card periods-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ rollingStats.rollingPeriods }}</div>
            <div class="stat-label">滚动期数</div>
            <div class="stat-description">平均滚动期数</div>
            <div class="stat-trend">
              <i class="el-icon-time"></i>
              <span>个月</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 滚动类型选择 -->
    <el-card class="rolling-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>滚动类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshRollingTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="rollingType in rollingTypes" :key="rollingType.id">
          <el-card 
            class="rolling-type-item" 
            shadow="hover" 
            @click.native="handleSelectRollingType(rollingType)"
            :class="{ 'selected': selectedRollingType === rollingType.id }"
          >
            <div class="rolling-type-icon">
              <i :class="rollingType.icon"></i>
            </div>
            <div class="rolling-type-title">{{ rollingType.name }}</div>
            <div class="rolling-type-description">{{ rollingType.description }}</div>
            <div class="rolling-type-stats">
              <span class="plan-count">{{ rollingType.planCount }} 个计划</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 滚动计划列表 -->
    <el-card class="rolling-plans-card" shadow="never">
      <div slot="header" class="card-header">
        <span>滚动计划</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索计划"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getRollingPlanList"
            clearable
            @clear="getRollingPlanList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getRollingPlanList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="rollingPlanList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="rollingName" label="计划名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.rollingName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="rollingType" label="滚动类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRollingTypeColor(scope.row.rollingType)" size="mini">
              {{ getRollingTypeText(scope.row.rollingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rollingCycle" label="滚动周期" width="100" align="center" />
        <el-table-column prop="rollingWindow" label="滚动期数" width="100" align="center">
          <template slot-scope="scope">
            <span class="period-count">{{ scope.row.rollingWindow }} 期</span>
          </template>
        </el-table-column>
        <el-table-column prop="lastRollingDate" label="上次滚动" width="150" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.lastRollingDate ? scope.row.lastRollingDate.slice(0, 10) : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="rollingCount" label="已滚动次数" width="110" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.rollingCount }} 次</span>
          </template>
        </el-table-column>
        <el-table-column prop="rollingStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.rollingStatus)" size="mini">
              {{ getStatusText(scope.row.rollingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-video-play"
              @click="handleExecute(scope.row)"
              :disabled="scope.row.rollingStatus !== 'ACTIVE'"
            >执行</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="pause">暂停</el-dropdown-item>
                <el-dropdown-item command="resume">恢复</el-dropdown-item>
                <el-dropdown-item command="analysis">分析</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 滚动计划详情抽屉 -->
    <el-drawer
      title="滚动计划详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentPlan">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="计划基本信息" :column="2" border>
              <el-descriptions-item label="计划名称">{{ currentPlan.rollingName }}</el-descriptions-item>
              <el-descriptions-item label="滚动类型">{{ getRollingTypeText(currentPlan.rollingType) }}</el-descriptions-item>
              <el-descriptions-item label="滚动周期">{{ currentPlan.rollingCycle }}</el-descriptions-item>
              <el-descriptions-item label="滚动期数">{{ currentPlan.rollingWindow }} 期</el-descriptions-item>
              <el-descriptions-item label="开始日期">{{ currentPlan.startDate ? currentPlan.startDate.slice(0, 10) : '-' }}</el-descriptions-item>
              <el-descriptions-item label="上次滚动">{{ currentPlan.lastRollingDate ? currentPlan.lastRollingDate.slice(0, 10) : '-' }}</el-descriptions-item>
              <el-descriptions-item label="已滚动次数">{{ currentPlan.rollingCount }} 次</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentPlan.rollingStatus)" size="mini">
                  {{ getStatusText(currentPlan.rollingStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentPlan.createBy }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentPlan.createTime ? currentPlan.createTime.slice(0, 10) : '-' }}</el-descriptions-item>
              <el-descriptions-item label="备注" :span="2">{{ currentPlan.remark }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="滚动配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="滚动方式">
                <el-input :value="getRollingMethodText(currentPlan.rollingMethod)" readonly />
              </el-form-item>
              <el-form-item label="调整系数">
                <el-input :value="currentPlan.adjustmentFactor" readonly />
              </el-form-item>
              <el-form-item label="计算公式">
                <el-input :value="currentPlan.calculationFormula || '-'" readonly />
              </el-form-item>
              <el-form-item label="滚动编码">
                <el-input :value="currentPlan.rollingCode || '-'" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="滚动历史" name="history">
            <el-table :data="rollingHistory" border size="mini">
              <el-table-column prop="rollingDate" label="滚动日期" width="150" />
              <el-table-column prop="rollingPeriod" label="滚动期间" width="150" />
              <el-table-column prop="adjustmentAmount" label="调整金额" width="120" align="right">
                <template slot-scope="scope">
                  <span class="amount-text">{{ formatAmount(scope.row.adjustmentAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="accuracy" label="准确率" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.accuracy }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="operator" label="操作人" width="100" />
              <el-table-column prop="remarks" label="备注" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="预测分析" name="forecast">
            <div id="forecastChart" style="height: 400px;"></div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 执行滚动弹窗 -->
    <el-dialog
      title="执行滚动"
      :visible.sync="executeDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form label-width="100px" size="small">
        <el-form-item label="执行范围">
          <el-select v-model="executeForm.scope" placeholder="请选择执行范围" style="width:100%">
            <el-option label="全部活跃计划" value="ALL" />
            <el-option label="仅月度滚动" value="MONTHLY" />
            <el-option label="仅季度滚动" value="QUARTERLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行说明">
          <el-input v-model="executeForm.remark" type="textarea" :rows="3" placeholder="请输入执行说明（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="executeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="executeLoading" @click="confirmExecuteRolling">确认执行</el-button>
      </div>
    </el-dialog>

    <!-- 滚动分析弹窗 -->
    <el-dialog
      :title="analysisPlan && analysisPlan.rollingName ? '滚动分析 - ' + analysisPlan.rollingName : '滚动分析概览'"
      :visible.sync="analysisDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="analysisPlan && analysisPlan.rollingId">
        <el-descriptions title="计划基本信息" :column="2" border size="mini">
          <el-descriptions-item label="计划名称">{{ analysisPlan.rollingName }}</el-descriptions-item>
          <el-descriptions-item label="滚动类型">{{ getRollingTypeText(analysisPlan.rollingType) }}</el-descriptions-item>
          <el-descriptions-item label="滚动期数">{{ analysisPlan.rollingWindow }} 期</el-descriptions-item>
          <el-descriptions-item label="已滚动次数">{{ analysisPlan.rollingCount }} 次</el-descriptions-item>
          <el-descriptions-item label="上次滚动">{{ analysisPlan.lastRollingDate ? analysisPlan.lastRollingDate.slice(0, 10) : '-' }}</el-descriptions-item>
          <el-descriptions-item label="调整系数">{{ analysisPlan.adjustmentFactor }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(analysisPlan.rollingStatus)" size="mini">
              {{ getStatusText(analysisPlan.rollingStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建人">{{ analysisPlan.createBy }}</el-descriptions-item>
          <el-descriptions-item label="滚动方式">{{ getRollingMethodText(analysisPlan.rollingMethod) }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ analysisPlan.remark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else>
        <el-descriptions title="整体统计" :column="2" border size="mini">
          <el-descriptions-item label="总计划数">{{ rollingPlanList.length }} 个</el-descriptions-item>
          <el-descriptions-item label="活跃计划">{{ rollingPlanList.filter(p => p.rollingStatus === 'ACTIVE').length }} 个</el-descriptions-item>
          <el-descriptions-item label="月度滚动">{{ rollingPlanList.filter(p => p.rollingType === 'MONTHLY').length }} 个</el-descriptions-item>
          <el-descriptions-item label="季度滚动">{{ rollingPlanList.filter(p => p.rollingType === 'QUARTERLY').length }} 个</el-descriptions-item>
          <el-descriptions-item label="已完成计划">{{ rollingPlanList.filter(p => p.rollingStatus === 'COMPLETED').length }} 个</el-descriptions-item>
          <el-descriptions-item label="草稿计划">{{ rollingPlanList.filter(p => p.rollingStatus === 'DRAFT').length }} 个</el-descriptions-item>
        </el-descriptions>
        <el-table :data="rollingPlanList.slice(0, 5)" border size="mini" style="margin-top:16px">
          <el-table-column prop="rollingName" label="计划名称" show-overflow-tooltip />
          <el-table-column prop="rollingType" label="类型" width="100" align="center">
            <template slot-scope="scope">{{ getRollingTypeText(scope.row.rollingType) }}</template>
          </el-table-column>
          <el-table-column prop="rollingCount" label="已滚动" width="80" align="center">
            <template slot-scope="scope">{{ scope.row.rollingCount }} 次</template>
          </el-table-column>
          <el-table-column prop="rollingStatus" label="状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusColor(scope.row.rollingStatus)" size="mini">{{ getStatusText(scope.row.rollingStatus) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑滚动计划对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="planForm"
        :model="planForm"
        :rules="planRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划名称" prop="rollingName">
              <el-input v-model="planForm.rollingName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="滚动类型" prop="rollingType">
              <el-select v-model="planForm.rollingType" placeholder="请选择滚动类型" style="width: 100%">
                <el-option value="MONTHLY" label="月度滚动" />
                <el-option value="QUARTERLY" label="季度滚动" />
                <el-option value="YEARLY" label="年度滚动" />
                <el-option value="CUSTOM" label="自定义滚动" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="滚动周期" prop="rollingCycle">
              <el-input v-model="planForm.rollingCycle" placeholder="如：每月1日" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="滚动期数" prop="rollingWindow">
              <el-input-number v-model="planForm.rollingWindow" :min="1" :max="36" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="planForm.startDate"
                type="date"
                placeholder="选择开始日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="planForm.endDate"
                type="date"
                placeholder="选择结束日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="滚动方式" prop="rollingMethod">
              <el-select v-model="planForm.rollingMethod" placeholder="请选择滚动方式" style="width: 100%">
                <el-option value="ADD_MONTH" label="按月递推" />
                <el-option value="ADD_QUARTER" label="按季递推" />
                <el-option value="ADD_YEAR" label="按年递推" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调整系数" prop="adjustmentFactor">
              <el-input-number v-model="planForm.adjustmentFactor" :min="0.1" :max="10" :step="0.01" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="planForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'
import * as echarts from 'echarts'

export default {
  name: 'RollingBudget',
  data() {
    return {
      // 统计数据
      rollingStats: {
        totalPlans: 0,
        activePlans: 0,
        accuracy: 0,
        rollingPeriods: 0
      },

      // 滚动类型
      rollingTypes: [
        { id: 1, name: '月度滚动', description: '每月滚动更新', icon: 'el-icon-date', planCount: 0 },
        { id: 2, name: '季度滚动', description: '每季度滚动更新', icon: 'el-icon-time', planCount: 0 },
        { id: 3, name: '年度滚动', description: '每年滚动更新', icon: 'el-icon-calendar', planCount: 0 },
        { id: 4, name: '自定义滚动', description: '自定义滚动周期', icon: 'el-icon-setting', planCount: 0 }
      ],
      selectedRollingType: null,
      
      // 滚动计划列表
      rollingPlanList: [],
      loading: false,
      searchKeyword: '',
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentPlan: null,
      rollingHistory: [],
      
      // 执行滚动弹窗
      executeDialogVisible: false,
      executeLoading: false,
      executeForm: { scope: 'ALL', remark: '' },

      // 滚动分析弹窗
      analysisDialogVisible: false,
      analysisPlan: null,

      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      planForm: {
        rollingName: '',
        rollingType: '',
        rollingCycle: '',
        rollingWindow: 12,
        startDate: '',
        endDate: '',
        rollingMethod: '',
        adjustmentFactor: 1.0,
        remark: ''
      },

      // 表单验证规则
      planRules: {
        rollingName: [
          { required: true, message: '请输入计划名称', trigger: 'blur' }
        ],
        rollingType: [
          { required: true, message: '请选择滚动类型', trigger: 'change' }
        ],
        rollingCycle: [
          { required: true, message: '请输入滚动周期', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ]
      }
    }
  },
  
  created() {
    this.getRollingPlanList()
    this.getRollingStats()
  },
  
  methods: {
    // 获取滚动计划列表
    async getRollingPlanList() {
      this.loading = true
      try {
        const params = { pageNum: 1, pageSize: 100 }
        if (this.searchKeyword) params.keyword = this.searchKeyword
        if (this.selectedRollingType) params.rollingType = this.selectedRollingType
        const response = await advancedFeaturesApi.getRollingBudgetPlanList(params)
        if (response && response.code === 1) {
          const list = (response.data && response.data.list) || []
          this.rollingPlanList = list
          this.updateRollingTypeCounts(list)
        }
      } catch (error) {
        this.$message.error('获取滚动计划列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 动态统计各类型计划数
    updateRollingTypeCounts(list) {
      const typeMap = { MONTHLY: 0, QUARTERLY: 0, YEARLY: 0, CUSTOM: 0 }
      list.forEach(item => {
        if (typeMap[item.rollingType] !== undefined) typeMap[item.rollingType]++
        else typeMap['CUSTOM']++
      })
      this.rollingTypes = [
        { id: 'MONTHLY', name: '月度滚动', description: '每月滚动更新', icon: 'el-icon-date', planCount: typeMap.MONTHLY },
        { id: 'QUARTERLY', name: '季度滚动', description: '每季度滚动更新', icon: 'el-icon-time', planCount: typeMap.QUARTERLY },
        { id: 'YEARLY', name: '年度滚动', description: '每年滚动更新', icon: 'el-icon-calendar', planCount: typeMap.YEARLY },
        { id: 'CUSTOM', name: '自定义滚动', description: '自定义滚动周期', icon: 'el-icon-setting', planCount: typeMap.CUSTOM }
      ]
    },

    // 获取统计数据
    async getRollingStats() {
      try {
        const response = await advancedFeaturesApi.getRollingBudgetStats()
        if (response && response.code === 1 && response.data) {
          this.rollingStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 创建计划
    handleCreatePlan() {
      this.dialogTitle = '创建滚动计划'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑计划
    handleEdit(row) {
      this.dialogTitle = '编辑滚动计划'
      this.dialogVisible = true
      this.planForm = { ...row }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentPlan = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getRollingHistory(row.planId)
    },
    
    // 获取滚动历史
    async getRollingHistory(planId) {
      try {
        const response = await advancedFeaturesApi.getRollingHistory(planId)
        this.rollingHistory = response.data
      } catch (error) {
        console.error('获取滚动历史失败：', error)
      }
    },
    
    // 执行滚动（行按钮）
    async handleExecute(row) {
      this.$confirm(`确定执行滚动计划【${row.rollingName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.executeRollingBudget(row.rollingId)
          this.$message.success('滚动执行成功')
          this.getRollingPlanList()
        } catch (error) {
          this.$message.error('滚动执行失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'pause':
          this.handlePausePlan(row)
          break
        case 'resume':
          this.handleResumePlan(row)
          break
        case 'analysis':
          this.handleAnalysisPlan(row)
          break
        case 'copy':
          this.handleCopyPlan(row)
          break
        case 'delete':
          this.handleDeletePlan(row)
          break
      }
    },
    
    // 暂停计划
    async handlePausePlan(row) {
      try {
        await advancedFeaturesApi.pauseRollingBudget(row.rollingId)
        this.$message.success('计划已暂停')
        this.getRollingPlanList()
      } catch (error) {
        this.$message.error('暂停失败：' + error.message)
      }
    },

    // 恢复计划
    async handleResumePlan(row) {
      try {
        await advancedFeaturesApi.resumeRollingBudget(row.rollingId)
        this.$message.success('计划已恢复')
        this.getRollingPlanList()
      } catch (error) {
        this.$message.error('恢复失败：' + error.message)
      }
    },
    
    // 分析计划（改为弹窗）
    handleAnalysisPlan(row) {
      this.analysisPlan = row
      this.analysisDialogVisible = true
    },
    
    // 复制计划
    async handleCopyPlan(row) {
      try {
        await advancedFeaturesApi.copyRollingBudget(row.rollingId)
        this.$message.success('复制成功')
        this.getRollingPlanList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除计划
    handleDeletePlan(row) {
      this.$confirm('确定删除该滚动计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteRollingBudget(row.rollingId)
          this.$message.success('删除成功')
          this.getRollingPlanList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交表单
    async handleSubmitForm() {
      this.$refs.planForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            if (this.planForm.rollingId) {
              await advancedFeaturesApi.updateRollingBudgetPlan(this.planForm)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createRollingBudgetPlan(this.planForm)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getRollingPlanList()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.planForm = {
        rollingName: '',
        rollingType: '',
        rollingCycle: '',
        rollingWindow: 12,
        startDate: '',
        endDate: '',
        rollingMethod: '',
        adjustmentFactor: 1.0,
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.planForm && this.$refs.planForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 刷新
    handleRefresh() {
      this.getRollingPlanList()
      this.getRollingStats()
    },
    
    // 执行滚动（顶部按钮，改为弹窗）
    handleExecuteRolling() {
      this.executeForm = { scope: 'ALL', remark: '' }
      this.executeDialogVisible = true
    },

    // 确认执行滚动（从列表中选第一个 ACTIVE 计划执行）
    async confirmExecuteRolling() {
      const activePlan = this.rollingPlanList.find(p => p.rollingStatus === 'ACTIVE')
      if (!activePlan) {
        this.$message.warning('当前没有活跃状态的滚动计划可执行')
        return
      }
      this.executeLoading = true
      try {
        await advancedFeaturesApi.executeRollingBudget(activePlan.rollingId)
        this.$message.success(`计划【${activePlan.rollingName}】滚动执行成功`)
        this.executeDialogVisible = false
        this.getRollingPlanList()
      } catch (error) {
        this.$message.error('执行失败：' + error.message)
      } finally {
        this.executeLoading = false
      }
    },

    // 查看分析（顶部按钮，展示整体统计概览）
    handleViewAnalysis() {
      this.analysisPlan = null
      this.analysisDialogVisible = true
    },
    
    // 滚动设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/rollingSettings')
    },
    
    // 滚动报告
    handleReports() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/rollingReports')
    },
    
    // 帮助
    handleHelp() {
      this.$router.push('/managementAccountant/ncv65/advancedFeatures/rollingHelp')
    },
    
    // 刷新滚动类型
    refreshRollingTypes() {
      this.getRollingPlanList()
      this.getRollingStats()
      this.$message.success('已刷新')
    },

    // 选择滚动类型
    handleSelectRollingType(rollingType) {
      this.selectedRollingType = rollingType.id
      this.getRollingPlanList()
    },
    

    
    // 格式化金额
    formatAmount(amount) {
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY'
      }).format(amount)
    },
    
    // 获取滚动类型颜色
    getRollingTypeColor(type) {
      const colorMap = {
        'MONTHLY': 'primary',
        'QUARTERLY': 'success',
        'YEARLY': 'warning',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取滚动类型文本
    getRollingTypeText(type) {
      const textMap = {
        'MONTHLY': '月度滚动',
        'QUARTERLY': '季度滚动',
        'YEARLY': '年度滚动',
        'CUSTOM': '自定义滚动'
      }
      return textMap[type] || type
    },
    
    // 获取准确率颜色
    getAccuracyColor(accuracy) {
      if (accuracy >= 90) return '#67C23A'
      if (accuracy >= 80) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'STOPPED': 'danger',
        'DRAFT': 'info',
        'PENDING': 'info',
        'COMPLETED': '',
        'INACTIVE': 'danger',
        'RUNNING': 'primary'
      }
      return colorMap[status] !== undefined ? colorMap[status] : 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'PAUSED': '已暂停',
        'STOPPED': '已停止',
        'DRAFT': '草稿',
        'PENDING': '待启动',
        'COMPLETED': '已完成',
        'INACTIVE': '已停用',
        'RUNNING': '执行中'
      }
      return textMap[status] || status || '-'
    },

    // 获取滚动方式文本
    getRollingMethodText(method) {
      const textMap = {
        'ADD_MONTH': '按月递推',
        'ADD_QUARTER': '按季递推',
        'ADD_YEAR': '按年递推'
      }
      return textMap[method] || method || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.rolling-budget {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card {
    margin-bottom: 20px;

    .text-right {
      text-align: right;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.accuracy-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.periods-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .rolling-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .rolling-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      &.selected {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .rolling-type-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px;

        i {
          font-size: 24px;
          color: white;
        }
      }

      .rolling-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .rolling-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .rolling-type-stats {
        .plan-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .rolling-plans-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
      }
    }

    .period-count {
      color: #409EFF;
      font-weight: 500;
    }

    .accuracy-text {
      margin-left: 8px;
      font-size: 12px;
      color: #606266;
    }

    .amount-text {
      font-family: 'Courier New', monospace;
      font-weight: 500;
    }
  }

  .detail-content {
    padding: 20px;

    .amount-text {
      font-family: 'Courier New', monospace;
      font-weight: 500;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
