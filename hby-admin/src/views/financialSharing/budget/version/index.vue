<template>
  <div class="budget-version-container">
    <!-- 头部操作区域 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>预算版本管理</span>
        <el-button-group class="header-actions">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleCreateVersion"
          >
            新建版本
          </el-button>
          <el-button
            type="success"
            size="small"
            icon="el-icon-copy-document"
            @click="handleCopyVersion"
            :disabled="!selectedVersion"
          >
            复制版本
          </el-button>
          <el-button
            type="warning"
            size="small"
            icon="el-icon-refresh-left"
            @click="handleRollbackVersion"
            :disabled="!selectedVersion || selectedVersion.status === 'active'"
          >
            版本回滚
          </el-button>
          <el-button
            type="info"
            size="small"
            icon="el-icon-refresh"
            @click="handleRefresh"
          >
            刷新
          </el-button>
        </el-button-group>
      </div>

      <!-- 筛选条件 -->
      <el-form :inline="true" :model="filterForm" ref="filterForm" size="small">
        <el-form-item label="预算名称">
          <el-input
            v-model="filterForm.budgetName"
            placeholder="请输入预算名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="版本状态">
          <el-select
            v-model="filterForm.status"
            placeholder="请选择版本状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" value="draft" />
            <el-option label="审批中" value="pending" />
            <el-option label="已生效" value="active" />
            <el-option label="已归档" value="archived" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 版本列表 -->
    <el-card class="list-card">
      <el-table
        v-loading="loading"
        :data="versionList"
        border
        stripe
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="60" label="序号" align="center" />

        <el-table-column prop="versionCode" label="版本编号" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVersionTagType(scope.row)" size="small">
              {{ scope.row.versionCode }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="budgetName" label="预算名称" min-width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewVersion(scope.row)">
              {{ scope.row.budgetName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="budgetType" label="预算类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBudgetTypeTag(scope.row.budgetType)">
              {{ scope.row.budgetType }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="versionName" label="版本名称" min-width="150" show-overflow-tooltip />

        <el-table-column prop="budgetAmount" label="预算金额" width="130" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatCurrency(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createBy" label="创建人" width="100" align="center" />

        <el-table-column prop="createTime" label="创建时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.createTime | formatDate }}
          </template>
        </el-table-column>

        <el-table-column prop="effectiveTime" label="生效时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.effectiveTime | formatDate }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleViewVersion(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEditVersion(scope.row)"
              v-if="scope.row.status === 'draft'"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-document"
              @click="handleCompareVersion(scope.row)"
              v-if="scope.row.status !== 'draft'"
            >
              对比
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="handleDeleteVersion(scope.row)"
              v-if="scope.row.status === 'draft'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </el-card>

    <!-- 新建版本对话框 -->
    <el-dialog
      :title="versionDialogTitle"
      :visible.sync="versionDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleVersionDialogClose"
    >
      <el-form
        ref="versionForm"
        :model="versionForm"
        :rules="versionRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算名称" prop="budgetName">
              <el-input
                v-model="versionForm.budgetName"
                placeholder="请输入预算名称"
                :disabled="editMode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算类型" prop="budgetType">
              <el-select
                v-model="versionForm.budgetType"
                placeholder="请选择预算类型"
                style="width: 100%"
                :disabled="editMode"
              >
                <el-option label="年度预算" value="annual" />
                <el-option label="季度预算" value="quarterly" />
                <el-option label="月度预算" value="monthly" />
                <el-option label="项目预算" value="project" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="版本名称" prop="versionName">
              <el-input
                v-model="versionForm.versionName"
                placeholder="请输入版本名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算期间" prop="period">
              <el-date-picker
                v-model="versionForm.period"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number
                v-model="versionForm.budgetAmount"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效时间" prop="effectiveTime">
              <el-date-picker
                v-model="versionForm.effectiveTime"
                type="datetime"
                placeholder="选择生效时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="版本描述" prop="description">
          <el-input
            type="textarea"
            v-model="versionForm.description"
            :rows="3"
            placeholder="请输入版本描述"
          />
        </el-form-item>

        <!-- 预算明细表格 -->
        <el-form-item label="预算明细">
          <el-table
            :data="versionForm.details"
            border
            size="small"
            max-height="300"
          >
            <el-table-column type="index" width="60" label="序号" align="center" />
            <el-table-column prop="item" label="预算项目" min-width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.item" placeholder="请输入项目名称" />
              </template>
            </el-table-column>
            <el-table-column prop="category" label="费用类别" width="120">
              <template slot-scope="scope">
                <el-select v-model="scope.row.category" placeholder="请选择类别">
                  <el-option label="人工成本" value="labor" />
                  <el-option label="材料费用" value="material" />
                  <el-option label="设备费用" value="equipment" />
                  <el-option label="其他费用" value="other" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="120" align="right">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.amount"
                  :min="0"
                  :precision="2"
                  size="small"
                  @change="calculateTotal"
                />
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入备注" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-delete"
                  @click="handleDeleteDetail(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top: 10px; text-align: right;">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddDetail">
              添加明细
            </el-button>
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="versionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitVersion" :loading="submitting">
          确认保存
        </el-button>
      </div>
    </el-dialog>

    <!-- 版本对比对话框 -->
    <el-dialog
      title="版本对比"
      :visible.sync="compareDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
    >
      <div v-if="compareData.source && compareData.target">
        <!-- 对比头部信息 -->
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="12">
            <el-card size="small">
              <div slot="header">源版本</div>
              <p><strong>版本名称:</strong> {{ compareData.source.versionName }}</p>
              <p><strong>预算金额:</strong> {{ formatCurrency(compareData.source.budgetAmount) }}</p>
              <p><strong>创建时间:</strong> {{ compareData.source.createTime | formatDate }}</p>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card size="small">
              <div slot="header">目标版本</div>
              <p><strong>版本名称:</strong> {{ compareData.target.versionName }}</p>
              <p><strong>预算金额:</strong> {{ formatCurrency(compareData.target.budgetAmount) }}</p>
              <p><strong>创建时间:</strong> {{ compareData.target.createTime | formatDate }}</p>
            </el-card>
          </el-col>
        </el-row>

        <!-- 差异统计 -->
        <el-card size="small" style="margin-bottom: 20px;">
          <div slot="header">差异统计</div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="diff-stat">
                <div class="diff-label">预算差异</div>
                <div class="diff-value" :class="budgetDiffClass">
                  {{ formatCurrency(budgetDiff) }}
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="diff-stat">
                <div class="diff-label">明细变更</div>
                <div class="diff-value">{{ detailChanges }}项</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="diff-stat">
                <div class="diff-label">新增项目</div>
                <div class="diff-value positive">{{ addedItems }}项</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="diff-stat">
                <div class="diff-label">删除项目</div>
                <div class="diff-value negative">{{ deletedItems }}项</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 明细对比表格 -->
        <el-table
          :data="compareTableData"
          border
          size="small"
          max-height="400"
        >
          <el-table-column type="index" width="60" label="序号" align="center" />
          <el-table-column prop="item" label="预算项目" min-width="150" />
          <el-table-column prop="sourceAmount" label="源版本金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.sourceAmount">{{ formatCurrency(scope.row.sourceAmount) }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="targetAmount" label="目标版本金额" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.targetAmount">{{ formatCurrency(scope.row.targetAmount) }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="diff" label="差异" width="120" align="right">
            <template slot-scope="scope">
              <span
                v-if="scope.row.diff !== undefined"
                :class="scope.row.diff > 0 ? 'positive' : 'negative'"
              >
                {{ formatCurrency(scope.row.diff) }}
              </span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="changeType" label="变更类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag
                :type="getChangeTypeTag(scope.row.changeType)"
                size="mini"
              >
                {{ getChangeTypeText(scope.row.changeType) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="compareDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportCompare">导出对比</el-button>
      </div>
    </el-dialog>

    <!-- 版本详情对话框 -->
    <el-dialog
      title="版本详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedVersion">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="3" border>
          <el-descriptions-item label="版本编号">{{ selectedVersion.versionCode }}</el-descriptions-item>
          <el-descriptions-item label="预算名称">{{ selectedVersion.budgetName }}</el-descriptions-item>
          <el-descriptions-item label="版本名称">{{ selectedVersion.versionName }}</el-descriptions-item>
          <el-descriptions-item label="预算类型">{{ selectedVersion.budgetType }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ formatCurrency(selectedVersion.budgetAmount) }}</el-descriptions-item>
          <el-descriptions-item label="版本状态">
            <el-tag :type="getStatusTag(selectedVersion.status)">
              {{ getStatusText(selectedVersion.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建人">{{ selectedVersion.createBy }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ selectedVersion.createTime | formatDate }}</el-descriptions-item>
          <el-descriptions-item label="生效时间">{{ selectedVersion.effectiveTime | formatDate }}</el-descriptions-item>
        </el-descriptions>

        <!-- 版本描述 -->
        <div style="margin: 20px 0;">
          <h4>版本描述</h4>
          <p>{{ selectedVersion.description || '暂无描述' }}</p>
        </div>

        <!-- 预算明细 -->
        <div style="margin-top: 20px;">
          <h4>预算明细</h4>
          <el-table :data="selectedVersion.details" border size="small">
            <el-table-column type="index" width="60" label="序号" align="center" />
            <el-table-column prop="item" label="预算项目" min-width="150" />
            <el-table-column prop="category" label="费用类别" width="120" />
            <el-table-column prop="amount" label="金额" width="120" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="200" />
          </el-table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportDetail">导出详情</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatCurrency, formatDate } from '@/utils/index'

export default {
  name: 'BudgetVersion',
  data() {
    return {
      // 筛选表单
      filterForm: {
        budgetName: '',
        status: '',
        dateRange: []
      },

      // 版本列表数据
      versionList: [],
      loading: false,

      // 分页参数
      pagination: {
        pageNo: 1,
        pageSize: 20,
        total: 0
      },

      // 选中的版本
      selectedVersion: null,
      multipleSelection: [],

      // 版本对话框
      versionDialogVisible: false,
      versionDialogTitle: '新建版本',
      editMode: false,

      // 版本表单
      versionForm: {
        budgetId: null,
        budgetName: '',
        budgetType: '',
        versionName: '',
        period: [],
        budgetAmount: 0,
        effectiveTime: '',
        description: '',
        details: []
      },
      versionRules: {
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        budgetType: [
          { required: true, message: '请选择预算类型', trigger: 'change' }
        ],
        versionName: [
          { required: true, message: '请输入版本名称', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '请选择预算期间', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' }
        ]
      },

      // 版本对比
      compareDialogVisible: false,
      compareData: {
        source: null,
        target: null
      },
      compareTableData: [],

      // 版本详情
      detailDialogVisible: false,

      // 提交状态
      submitting: false
    }
  },

  created() {
    this.fetchVersionList()
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  methods: {
    // 格式化货币
    formatCurrency,

    // 获取版本列表
    async fetchVersionList() {
      this.loading = true
      try {
        const params = {
          ...this.filterForm,
          pageNo: this.pagination.pageNo,
          pageSize: this.pagination.pageSize
        }

        // 处理日期范围
        if (this.filterForm.dateRange && this.filterForm.dateRange.length === 2) {
          params.startDate = this.filterForm.dateRange[0]
          params.endDate = this.filterForm.dateRange[1]
        }

        const response = await budgetApi.getVersionList(params)
        if (response.code === 1) {
          this.versionList = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取版本列表失败')
        }
      } catch (error) {
        console.error('获取版本列表异常:', error)
        this.$message.error('获取版本列表失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.pageNo = 1
      this.fetchVersionList()
    },

    // 重置
    handleReset() {
      this.$refs.filterForm.resetFields()
      this.filterForm.dateRange = []
      this.pagination.pageNo = 1
      this.fetchVersionList()
    },

    // 刷新
    handleRefresh() {
      this.fetchVersionList()
    },

    // 表格多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
      this.selectedVersion = selection.length === 1 ? selection[0] : null
    },

    // 行点击
    handleRowClick(row) {
      this.selectedVersion = row
    },

    // 新建版本
    handleCreateVersion() {
      this.editMode = false
      this.versionDialogTitle = '新建版本'
      this.versionForm = {
        budgetId: null,
        budgetName: '',
        budgetType: '',
        versionName: '',
        period: [],
        budgetAmount: 0,
        effectiveTime: '',
        description: '',
        details: []
      }
      this.versionDialogVisible = true
    },

    // 编辑版本
    handleEditVersion(row) {
      this.editMode = true
      this.versionDialogTitle = '编辑版本'
      this.versionForm = {
        budgetId: row.budgetId,
        budgetName: row.budgetName,
        budgetType: row.budgetType,
        versionName: row.versionName,
        period: row.period ? row.period.split(',') : [],
        budgetAmount: row.budgetAmount,
        effectiveTime: row.effectiveTime,
        description: row.description,
        details: row.details || []
      }
      this.versionDialogVisible = true
    },

    // 提交版本
    async handleSubmitVersion() {
      try {
        await this.$refs.versionForm.validate()

        this.submitting = true
        const response = await budgetApi.saveVersion(this.versionForm)

        if (response.code === 1) {
          this.$message.success(this.editMode ? '版本更新成功' : '版本创建成功')
          this.versionDialogVisible = false
          this.fetchVersionList()
        } else {
          this.$message.error(response.message || '保存版本失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('保存版本异常:', error)
          this.$message.error('保存版本失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 版本对话框关闭
    handleVersionDialogClose() {
      this.$refs.versionForm && this.$refs.versionForm.resetFields()
    },

    // 复制版本
    handleCopyVersion() {
      if (!this.selectedVersion) {
        this.$message.warning('请选择要复制的版本')
        return
      }

      this.editMode = false
      this.versionDialogTitle = '复制版本'
      this.versionForm = {
        budgetId: this.selectedVersion.budgetId,
        budgetName: this.selectedVersion.budgetName,
        budgetType: this.selectedVersion.budgetType,
        versionName: this.selectedVersion.versionName + '_副本',
        period: this.selectedVersion.period ? this.selectedVersion.period.split(',') : [],
        budgetAmount: this.selectedVersion.budgetAmount,
        effectiveTime: '',
        description: this.selectedVersion.description,
        details: this.selectedVersion.details ? JSON.parse(JSON.stringify(this.selectedVersion.details)) : []
      }
      this.versionDialogVisible = true
    },

    // 版本回滚
    handleRollbackVersion() {
      if (!this.selectedVersion) {
        this.$message.warning('请选择要回滚的版本')
        return
      }

      if (this.selectedVersion.status === 'active') {
        this.$message.warning('当前版本已生效，无法回滚')
        return
      }

      this.$confirm('确定要回滚到选中的版本吗？此操作将覆盖当前生效的版本。', '版本回滚确认', {
        confirmButtonText: '确定回滚',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetApi.rollbackVersion({
            versionId: this.selectedVersion.budgetId
          })

          if (response.code === 1) {
            this.$message.success('版本回滚成功')
            this.fetchVersionList()
          } else {
            this.$message.error(response.message || '版本回滚失败')
          }
        } catch (error) {
          console.error('版本回滚异常:', error)
          this.$message.error('版本回滚失败，请稍后重试')
        }
      }).catch(() => {})
    },

    // 删除版本
    handleDeleteVersion(row) {
      this.$confirm('确定要删除此版本吗？删除后不可恢复。', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetApi.deleteVersion(row.budgetId)

          if (response.code === 1) {
            this.$message.success('版本删除成功')
            this.fetchVersionList()
          } else {
            this.$message.error(response.message || '删除版本失败')
          }
        } catch (error) {
          console.error('删除版本异常:', error)
          this.$message.error('删除版本失败，请稍后重试')
        }
      }).catch(() => {})
    },

    // 查看版本
    handleViewVersion(row) {
      this.selectedVersion = { ...row }
      this.detailDialogVisible = true
    },

    // 对比版本
    async handleCompareVersion(row) {
      try {
        // 获取版本详情用于对比
        const response = await budgetApi.getVersionDetail(row.budgetId)
        if (response.code === 1) {
          this.compareData.target = response.data

          // 获取上一个版本作为源版本
          const previousVersion = this.getPreviousVersion(row)
          if (previousVersion) {
            const previousResponse = await budgetApi.getVersionDetail(previousVersion.budgetId)
            if (previousResponse.code === 1) {
              this.compareData.source = previousResponse.data
              this.calculateCompareData()
              this.compareDialogVisible = true
            }
          } else {
            this.$message.info('没有找到可对比的版本')
          }
        }
      } catch (error) {
        console.error('获取版本详情异常:', error)
        this.$message.error('获取版本详情失败，请稍后重试')
      }
    },

    // 获取上一个版本
    getPreviousVersion(currentVersion) {
      const versions = this.versionList
        .filter(v => v.budgetName === currentVersion.budgetName && v.budgetId !== currentVersion.budgetId)
        .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))

      return versions[0] || null
    },

    // 计算对比数据
    calculateCompareData() {
      const source = this.compareData.source
      const target = this.compareData.target

      if (!source || !target) return

      const sourceDetails = source.details || []
      const targetDetails = target.details || []

      // 生成对比表格数据
      this.compareTableData = this.generateCompareTableData(sourceDetails, targetDetails)
    },

    // 生成对比表格数据
    generateCompareTableData(sourceDetails, targetDetails) {
      const compareData = []
      const itemMap = new Map()

      // 处理源版本数据
      sourceDetails.forEach(item => {
        itemMap.set(item.item, {
          item: item.item,
          sourceAmount: item.amount,
          targetAmount: null,
          diff: null,
          changeType: 'deleted'
        })
      })

      // 处理目标版本数据
      targetDetails.forEach(item => {
        if (itemMap.has(item.item)) {
          const existing = itemMap.get(item.item)
          existing.targetAmount = item.amount
          existing.diff = item.amount - existing.sourceAmount
          existing.changeType = existing.diff === 0 ? 'unchanged' : 'modified'
        } else {
          itemMap.set(item.item, {
            item: item.item,
            sourceAmount: null,
            targetAmount: item.amount,
            diff: item.amount,
            changeType: 'added'
          })
        }
      })

      return Array.from(itemMap.values())
    },

    // 导出对比
    handleExportCompare() {
      try {
        const data = this.compareData || this.compareList || []
        const exportData = Array.isArray(data) ? data : [data]
        if (!exportData.length || (exportData.length === 1 && !exportData[0])) {
          this.$message.warning('暂无对比数据可导出'); return
        }
        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '版本对比.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) { this.$message.error('导出失败') }
    },

    // 导出详情
    handleExportDetail() {
      try {
        const data = this.detailData || this.tableData || []
        const exportData = Array.isArray(data) ? data : [data]
        if (!exportData.length || (exportData.length === 1 && !exportData[0])) {
          this.$message.warning('暂无详情数据可导出'); return
        }
        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '版本详情.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) { this.$message.error('导出失败') }
    },

    // 添加明细
    handleAddDetail() {
      this.versionForm.details.push({
        item: '',
        category: '',
        amount: 0,
        remark: ''
      })
    },

    // 删除明细
    handleDeleteDetail(index) {
      this.versionForm.details.splice(index, 1)
      this.calculateTotal()
    },

    // 计算总金额
    calculateTotal() {
      const total = this.versionForm.details.reduce((sum, item) => sum + (item.amount || 0), 0)
      this.versionForm.budgetAmount = total
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNo = 1
      this.fetchVersionList()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.pageNo = page
      this.fetchVersionList()
    },

    // 获取版本标签类型
    getVersionTagType(row) {
      if (row.status === 'active') return 'success'
      if (row.status === 'pending') return 'warning'
      return 'info'
    },

    // 获取预算类型标签
    getBudgetTypeTag(type) {
      const tagMap = {
        'annual': '',
        'quarterly': 'success',
        'monthly': 'warning',
        'project': 'info'
      }
      return tagMap[type] || ''
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'draft': 'info',
        'pending': 'warning',
        'active': 'success',
        'archived': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'draft': '草稿',
        'pending': '审批中',
        'active': '已生效',
        'archived': '已归档'
      }
      return textMap[status] || '未知'
    },

    // 获取变更类型标签
    getChangeTypeTag(type) {
      const tagMap = {
        'added': 'success',
        'deleted': 'danger',
        'modified': 'warning',
        'unchanged': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取变更类型文本
    getChangeTypeText(type) {
      const textMap = {
        'added': '新增',
        'deleted': '删除',
        'modified': '修改',
        'unchanged': '未变更'
      }
      return textMap[type] || '未知'
    }
  },

  computed: {
    // 预算差异
    budgetDiff() {
      if (!this.compareData.source || !this.compareData.target) return 0
      return this.compareData.target.budgetAmount - this.compareData.source.budgetAmount
    },

    // 预算差异样式
    budgetDiffClass() {
      return this.budgetDiff >= 0 ? 'positive' : 'negative'
    },

    // 明细变更数量
    detailChanges() {
      return this.compareTableData.filter(item => item.changeType !== 'unchanged').length
    },

    // 新增项目数量
    addedItems() {
      return this.compareTableData.filter(item => item.changeType === 'added').length
    },

    // 删除项目数量
    deletedItems() {
      return this.compareTableData.filter(item => item.changeType === 'deleted').length
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-version-container {
  padding: 20px;

  .header-card {
    margin-bottom: 16px;

    .header-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  .list-card {
    .pagination {
      margin-top: 20px;
      text-align: right;
    }

    .amount-text {
      font-weight: 600;
      color: #E6A23C;
    }
  }

  .diff-stat {
    text-align: center;
    padding: 16px;

    .diff-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .diff-value {
      font-size: 20px;
      font-weight: 600;

      &.positive {
        color: #67C23A;
      }

      &.negative {
        color: #F56C6C;
      }
    }
  }

  .text-muted {
    color: #909399;
  }

  .positive {
    color: #67C23A;
    font-weight: 600;
  }

  .negative {
    color: #F56C6C;
    font-weight: 600;
  }
}

.dialog-footer {
  text-align: right;
}
</style>