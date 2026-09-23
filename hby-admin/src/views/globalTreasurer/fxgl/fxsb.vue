<template>
  <div class="risk-identification-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>风险识别</h2>
      <p>识别和分类各类财务风险，建立风险档案</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="风险编号">
          <el-input v-model="searchForm.riskCode" placeholder="请输入风险编号" clearable />
        </el-form-item>
        <el-form-item label="风险类型">
          <el-select v-model="searchForm.riskType" placeholder="请选择风险类型" clearable>
            <el-option label="市场风险" value="MARKET_RISK" />
            <el-option label="信用风险" value="CREDIT_RISK" />
            <el-option label="操作风险" value="OPERATIONAL_RISK" />
            <el-option label="流动性风险" value="LIQUIDITY_RISK" />
            <el-option label="合规风险" value="COMPLIANCE_RISK" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
            <el-option label="极高" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="识别状态">
          <el-select v-model="searchForm.status" placeholder="请选择识别状态" clearable>
            <el-option label="待识别" value="PENDING" />
            <el-option label="已识别" value="IDENTIFIED" />
            <el-option label="评估中" value="ASSESSING" />
            <el-option label="已评估" value="ASSESSED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="识别日期">
          <el-date-picker
            v-model="searchForm.identificationDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增风险</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-data-analysis" size="mini" @click="handleBatchAssess">批量评估</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="riskList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="风险编号" prop="riskCode" width="120" show-overflow-tooltip />
        <el-table-column label="风险名称" prop="riskName" width="150" show-overflow-tooltip />
        <el-table-column label="风险类型" prop="riskType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeTag(scope.row.riskType)">
              {{ getRiskTypeText(scope.row.riskType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTag(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="影响程度" prop="impactLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="getImpactPercentage(scope.row.impactLevel)"
              :color="getImpactColor(scope.row.impactLevel)"
              :stroke-width="8"
              text-inside
            />
          </template>
        </el-table-column>
        <el-table-column label="发生概率" prop="probability" width="100" align="center">
          <template slot-scope="scope">
            <span class="probability-text">{{ formatPercent(scope.row.probability) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险值" prop="riskValue" width="120" align="right">
          <template slot-scope="scope">
            <span class="risk-value-text">{{ formatAmount(scope.row.riskValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="识别人" prop="identifier" width="100" align="center" />
        <el-table-column label="识别日期" prop="identificationDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.identificationDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="assess" v-if="scope.row.status === 'IDENTIFIED'" icon="el-icon-data-analysis">评估</el-dropdown-item>
                <el-dropdown-item command="monitor" icon="el-icon-view">监控</el-dropdown-item>
                <el-dropdown-item command="control" icon="el-icon-setting">控制措施</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">变更历史</el-dropdown-item>
                <el-dropdown-item command="close" v-if="scope.row.status !== 'CLOSED'" icon="el-icon-circle-close">关闭</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风险编号" prop="riskCode">
                  <el-input v-model="form.riskCode" placeholder="请输入风险编号" :disabled="form.riskId != null" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险名称" prop="riskName">
                  <el-input v-model="form.riskName" placeholder="请输入风险名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风险类型" prop="riskType">
                  <el-select v-model="form.riskType" placeholder="请选择风险类型" style="width: 100%">
                    <el-option label="市场风险" value="MARKET_RISK" />
                    <el-option label="信用风险" value="CREDIT_RISK" />
                    <el-option label="操作风险" value="OPERATIONAL_RISK" />
                    <el-option label="流动性风险" value="LIQUIDITY_RISK" />
                    <el-option label="合规风险" value="COMPLIANCE_RISK" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级" prop="riskLevel">
                  <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%">
                    <el-option label="低" value="LOW" />
                    <el-option label="中" value="MEDIUM" />
                    <el-option label="高" value="HIGH" />
                    <el-option label="极高" value="CRITICAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="影响程度" prop="impactLevel">
                  <el-select v-model="form.impactLevel" placeholder="请选择影响程度" style="width: 100%">
                    <el-option label="轻微" value="MINOR" />
                    <el-option label="一般" value="MODERATE" />
                    <el-option label="严重" value="MAJOR" />
                    <el-option label="灾难性" value="CATASTROPHIC" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发生概率" prop="probability">
                  <el-input v-model="form.probability" placeholder="请输入发生概率(0-1)" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风险值" prop="riskValue">
                  <el-input v-model="form.riskValue" placeholder="请输入风险值" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="识别日期" prop="identificationDate">
                  <el-date-picker
                    v-model="form.identificationDate"
                    type="date"
                    placeholder="选择识别日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="风险描述">
              <el-input v-model="form.riskDescription" type="textarea" placeholder="请输入风险描述" :rows="3" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="风险来源" name="source">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="风险来源" prop="riskSource">
                  <el-select v-model="form.riskSource" placeholder="请选择风险来源" style="width: 100%">
                    <el-option label="内部因素" value="INTERNAL" />
                    <el-option label="外部因素" value="EXTERNAL" />
                    <el-option label="系统性因素" value="SYSTEMATIC" />
                    <el-option label="非系统性因素" value="UNSYSTEMATIC" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务领域" prop="businessArea">
                  <el-select v-model="form.businessArea" placeholder="请选择业务领域" style="width: 100%">
                    <el-option label="投资业务" value="INVESTMENT" />
                    <el-option label="融资业务" value="FINANCING" />
                    <el-option label="现金管理" value="CASH_MANAGEMENT" />
                    <el-option label="衍生品交易" value="DERIVATIVES" />
                    <el-option label="外汇业务" value="FOREX" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="关联产品" prop="relatedProduct">
                  <el-input v-model="form.relatedProduct" placeholder="请输入关联产品" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="关联交易对手" prop="relatedCounterparty">
                  <el-input v-model="form.relatedCounterparty" placeholder="请输入关联交易对手" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="触发条件">
              <el-input v-model="form.triggerConditions" type="textarea" placeholder="请输入触发条件" :rows="3" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="识别信息" name="identification">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="识别人" prop="identifier">
                  <el-input v-model="form.identifier" placeholder="请输入识别人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="识别方法" prop="identificationMethod">
                  <el-select v-model="form.identificationMethod" placeholder="请选择识别方法" style="width: 100%">
                    <el-option label="专家判断" value="EXPERT_JUDGMENT" />
                    <el-option label="历史数据分析" value="HISTORICAL_ANALYSIS" />
                    <el-option label="情景分析" value="SCENARIO_ANALYSIS" />
                    <el-option label="压力测试" value="STRESS_TEST" />
                    <el-option label="模型分析" value="MODEL_ANALYSIS" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="识别依据" prop="identificationBasis">
                  <el-input v-model="form.identificationBasis" placeholder="请输入识别依据" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="确认人" prop="confirmer">
                  <el-input v-model="form.confirmer" placeholder="请输入确认人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="识别备注">
              <el-input v-model="form.identificationRemark" type="textarea" placeholder="请输入识别备注" :rows="3" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="风险详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="风险编号">{{ detailData.riskCode }}</el-descriptions-item>
        <el-descriptions-item label="风险名称">{{ detailData.riskName }}</el-descriptions-item>
        <el-descriptions-item label="风险类型">
          <el-tag :type="getRiskTypeTag(detailData.riskType)">
            {{ getRiskTypeText(detailData.riskType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelTag(detailData.riskLevel)" size="mini">
            {{ getRiskLevelText(detailData.riskLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="影响程度">{{ getImpactLevelText(detailData.impactLevel) }}</el-descriptions-item>
        <el-descriptions-item label="发生概率">{{ formatPercent(detailData.probability) }}</el-descriptions-item>
        <el-descriptions-item label="风险值">{{ formatAmount(detailData.riskValue) }}</el-descriptions-item>
        <el-descriptions-item label="风险来源">{{ getRiskSourceText(detailData.riskSource) }}</el-descriptions-item>
        <el-descriptions-item label="业务领域">{{ getBusinessAreaText(detailData.businessArea) }}</el-descriptions-item>
        <el-descriptions-item label="识别人">{{ detailData.identifier }}</el-descriptions-item>
        <el-descriptions-item label="识别日期">{{ parseTime(detailData.identificationDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险描述" :span="3">{{ detailData.riskDescription }}</el-descriptions-item>
        <el-descriptions-item label="触发条件" :span="3">{{ detailData.triggerConditions }}</el-descriptions-item>
        <el-descriptions-item label="识别备注" :span="3">{{ detailData.identificationRemark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils'

export default {
  name: 'RiskIdentification',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 风险列表
      riskList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 活动标签页
      activeTab: 'basic',
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        riskCode: null,
        riskType: null,
        riskLevel: null,
        status: null,
        identificationDateStart: null,
        identificationDateEnd: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        riskCode: '',
        riskType: '',
        riskLevel: '',
        status: '',
        identificationDateRange: []
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        riskCode: [
          { required: true, message: '风险编号不能为空', trigger: 'blur' }
        ],
        riskName: [
          { required: true, message: '风险名称不能为空', trigger: 'blur' }
        ],
        riskType: [
          { required: true, message: '风险类型不能为空', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '风险等级不能为空', trigger: 'change' }
        ],
        impactLevel: [
          { required: true, message: '影响程度不能为空', trigger: 'change' }
        ],
        probability: [
          { required: true, message: '发生概率不能为空', trigger: 'blur' }
        ],
        identificationDate: [
          { required: true, message: '识别日期不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    /** 查询风险列表 */
    getList() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.riskList = [
          {
            riskId: 1,
            riskCode: 'RISK001',
            riskName: '汇率波动风险',
            riskType: 'MARKET_RISK',
            riskLevel: 'HIGH',
            impactLevel: 'MAJOR',
            probability: 0.3,
            riskValue: 1000000,
            identifier: '张三',
            identificationDate: '2025-01-15',
            status: 'IDENTIFIED'
          }
        ]
        this.total = 1
        this.loading = false
      }, 1000)
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      if (this.searchForm.identificationDateRange && this.searchForm.identificationDateRange.length === 2) {
        this.queryParams.identificationDateStart = this.searchForm.identificationDateRange[0]
        this.queryParams.identificationDateEnd = this.searchForm.identificationDateRange[1]
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        riskCode: '',
        riskType: '',
        riskLevel: '',
        status: '',
        identificationDateRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        riskCode: null,
        riskType: null,
        riskLevel: null,
        status: null,
        identificationDateStart: null,
        identificationDateEnd: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增风险'
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.form = { ...row }
      this.open = true
      this.title = '修改风险'
      this.activeTab = 'basic'
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.$modal.msgSuccess(this.form.riskId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const riskIds = row.riskId || this.ids
      this.$modal.confirm('是否确认删除风险编号为"' + riskIds + '"的数据项？').then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('risk/identification/export', {
        ...this.queryParams
      }, `risk_identification_${new Date().getTime()}.xlsx`)
    },
    /** 批量评估 */
    handleBatchAssess() {
      this.$modal.confirm('是否确认对所有选中的风险进行评估？').then(() => {
        this.$modal.msgSuccess('评估完成')
        this.getList()
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.riskId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'assess':
          this.handleAssess(row)
          break
        case 'monitor':
          this.handleMonitor(row)
          break
        case 'control':
          this.handleControl(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'close':
          this.handleClose(row)
          break
      }
    },
    /** 评估风险 */
    handleAssess(row) {
      this.$router.push({ path: '/globalTreasurer/fxgl/fxpg', query: { riskId: row.riskId } })
    },
    /** 监控风险 */
    handleMonitor(row) {
      this.$router.push({ path: '/globalTreasurer/fxgl/fxjk', query: { riskId: row.riskId } })
    },
    /** 控制措施 */
    handleControl(row) {
      this.$router.push({ path: '/globalTreasurer/fxgl/fxkz', query: { riskId: row.riskId } })
    },
    /** 变更历史 */
    handleHistory(row) {
      this.$modal.msgSuccess('查看变更历史')
    },
    /** 关闭风险 */
    handleClose(row) {
      this.$prompt('请输入关闭原因', '关闭风险', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$modal.msgSuccess('关闭成功')
        this.getList()
      }).catch(() => {})
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        riskId: null,
        riskCode: null,
        riskName: null,
        riskType: null,
        riskLevel: null,
        impactLevel: null,
        probability: null,
        riskValue: null,
        riskDescription: null,
        riskSource: null,
        businessArea: null,
        relatedProduct: null,
        relatedCounterparty: null,
        triggerConditions: null,
        identifier: null,
        identificationMethod: null,
        identificationBasis: null,
        confirmer: null,
        identificationDate: null,
        identificationRemark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },
    /** 获取风险类型标签 */
    getRiskTypeTag(type) {
      const tagMap = {
        'MARKET_RISK': 'danger',
        'CREDIT_RISK': 'warning',
        'OPERATIONAL_RISK': 'primary',
        'LIQUIDITY_RISK': 'info',
        'COMPLIANCE_RISK': 'success'
      }
      return tagMap[type] || ''
    },
    /** 获取风险类型文本 */
    getRiskTypeText(type) {
      const textMap = {
        'MARKET_RISK': '市场风险',
        'CREDIT_RISK': '信用风险',
        'OPERATIONAL_RISK': '操作风险',
        'LIQUIDITY_RISK': '流动性风险',
        'COMPLIANCE_RISK': '合规风险'
      }
      return textMap[type] || type
    },
    /** 获取风险等级标签 */
    getRiskLevelTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[level] || ''
    },
    /** 获取风险等级文本 */
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '极高'
      }
      return textMap[level] || level
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'IDENTIFIED': 'primary',
        'ASSESSING': 'warning',
        'ASSESSED': 'success',
        'CLOSED': 'info'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待识别',
        'IDENTIFIED': '已识别',
        'ASSESSING': '评估中',
        'ASSESSED': '已评估',
        'CLOSED': '已关闭'
      }
      return textMap[status] || status
    },
    /** 获取影响程度百分比 */
    getImpactPercentage(level) {
      const percentageMap = {
        'MINOR': 25,
        'MODERATE': 50,
        'MAJOR': 75,
        'CATASTROPHIC': 100
      }
      return percentageMap[level] || 0
    },
    /** 获取影响程度颜色 */
    getImpactColor(level) {
      const colorMap = {
        'MINOR': '#67c23a',
        'MODERATE': '#e6a23c',
        'MAJOR': '#f56c6c',
        'CATASTROPHIC': '#f56c6c'
      }
      return colorMap[level] || '#67c23a'
    },
    /** 获取影响程度文本 */
    getImpactLevelText(level) {
      const textMap = {
        'MINOR': '轻微',
        'MODERATE': '一般',
        'MAJOR': '严重',
        'CATASTROPHIC': '灾难性'
      }
      return textMap[level] || level
    },
    /** 获取风险来源文本 */
    getRiskSourceText(source) {
      const textMap = {
        'INTERNAL': '内部因素',
        'EXTERNAL': '外部因素',
        'SYSTEMATIC': '系统性因素',
        'UNSYSTEMATIC': '非系统性因素'
      }
      return textMap[source] || source
    },
    /** 获取业务领域文本 */
    getBusinessAreaText(area) {
      const textMap = {
        'INVESTMENT': '投资业务',
        'FINANCING': '融资业务',
        'CASH_MANAGEMENT': '现金管理',
        'DERIVATIVES': '衍生品交易',
        'FOREX': '外汇业务'
      }
      return textMap[area] || area
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 格式化百分比 */
    formatPercent(percent) {
      if (percent == null) return '0.00%'
      return (parseFloat(percent) * 100).toFixed(2) + '%'
    }
  }
}
</script>

<style scoped>
.risk-identification-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.probability-text {
  font-weight: bold;
  color: #e6a23c;
}

.risk-value-text {
  font-weight: bold;
  color: #f56c6c;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
