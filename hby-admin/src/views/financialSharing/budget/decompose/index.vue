<template>
  <div class="budget-decompose-container">
    <!-- 头部信息 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>预算分解管理</span>
        <el-button-group class="header-actions">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleCreateScheme"
          >
            新建分解方案
          </el-button>
          <el-button
            type="success"
            size="small"
            icon="el-icon-check"
            @click="handleSaveDecompose"
            :disabled="!currentScheme || decomposeTreeData.length === 0"
          >
            保存分解结果
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

      <!-- 分解方案选择 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="选择预算">
            <el-select
              v-model="selectedBudget"
              placeholder="请选择要分解的预算"
              filterable
              @change="handleBudgetChange"
            >
              <el-option
                v-for="budget in budgetList"
                :key="budget.budgetId"
                :label="budget.budgetName"
                :value="budget.budgetId"
              >
                <span>{{ budget.budgetName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ formatCurrency(budget.budgetAmount) }}
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="分解方案">
            <el-select
              v-model="selectedScheme"
              placeholder="请选择分解方案"
              filterable
              @change="handleSchemeChange"
            >
              <el-option
                v-for="scheme in schemeList"
                :key="scheme.schemeId"
                :label="scheme.schemeName"
                :value="scheme.schemeId"
              >
                <span>{{ scheme.schemeName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ scheme.createTime | formatDate }}
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="分解状态">
            <el-tag :type="getSchemeStatusType(currentScheme?.status)">
              {{ getSchemeStatusText(currentScheme?.status) }}
            </el-tag>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 分解配置区域 -->
    <el-card class="config-card" v-if="selectedBudget">
      <div slot="header">分解配置</div>

      <el-form :model="decomposeConfig" :inline="true" size="small">
        <el-form-item label="分解维度">
          <el-checkbox-group v-model="decomposeConfig.dimensions" @change="handleDimensionChange">
            <el-checkbox label="department">按部门</el-checkbox>
            <el-checkbox label="project">按项目</el-checkbox>
            <el-checkbox label="period">按期间</el-checkbox>
            <el-checkbox label="category">按类别</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="分配方式">
          <el-select v-model="decomposeConfig.method" @change="handleMethodChange">
            <el-option label="按比例分配" value="percentage" />
            <el-option label="按固定金额" value="amount" />
            <el-option label="按公式计算" value="formula" />
          </el-select>
        </el-form-item>

        <el-form-item label="计算精度" v-if="decomposeConfig.method === 'percentage'">
          <el-input-number
            v-model="decomposeConfig.precision"
            :min="0"
            :max="4"
            :step="1"
            size="small"
          />
          <span style="margin-left: 8px; color: #909399;">小数位</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleCalculateDecompose" :loading="calculating">
            <i class="el-icon-s-operation"></i> 计算分解
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 分解结果展示 -->
    <el-card class="result-card" v-if="decomposeTreeData.length > 0">
      <div slot="header" class="result-header">
        <span>分解结果</span>
        <div class="result-actions">
          <el-button-group size="small">
            <el-button icon="el-icon-expand" @click="handleExpandAll">全部展开</el-button>
            <el-button icon="el-icon-s-unfold" @click="handleCollapseAll">全部收起</el-button>
            <el-button icon="el-icon-download" @click="handleExportResult">导出结果</el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 总览信息 -->
      <div class="overview-info" v-if="currentBudget">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-item">
              <div class="label">预算总额</div>
              <div class="value">{{ formatCurrency(currentBudget.budgetAmount) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="label">已分解金额</div>
              <div class="value allocated">{{ formatCurrency(totalAllocated) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="label">剩余金额</div>
              <div class="value remaining">{{ formatCurrency(currentBudget.budgetAmount - totalAllocated) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="label">分解完成率</div>
              <div class="value">{{ completionRate }}%</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 分解树形表格 -->
      <el-table
        ref="decomposeTree"
        :data="decomposeTreeData"
        border
        stripe
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="false"
        :expand-row-keys="expandedKeys"
        @expand-change="handleExpandChange"
        style="margin-top: 20px;"
      >
        <el-table-column prop="name" label="分解项目" min-width="200">
          <template slot-scope="scope">
            <div class="tree-node">
              <el-tag
                v-if="scope.row.type"
                :type="getNodeTypeTag(scope.row.type)"
                size="mini"
                style="margin-right: 8px;"
              >
                {{ getNodeTypeText(scope.row.type) }}
              </el-tag>
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="code" label="编码" width="120" align="center" />

        <el-table-column prop="budgetAmount" label="预算金额" width="150" align="right">
          <template slot-scope="scope">
            <span v-if="!scope.row.children || scope.row.children.length === 0">
              {{ formatCurrency(scope.row.budgetAmount) }}
            </span>
            <span v-else class="parent-amount">
              {{ formatCurrency(scope.row.budgetAmount) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="allocatedAmount" label="已分解金额" width="150" align="right">
          <template slot-scope="scope">
            <span v-if="!scope.row.children || scope.row.children.length === 0">
              {{ formatCurrency(scope.row.allocatedAmount) }}
            </span>
            <span v-else class="parent-amount">
              {{ formatCurrency(scope.row.allocatedAmount) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="分配比例" width="120" align="center" v-if="decomposeConfig.method === 'percentage'">
          <template slot-scope="scope">
            <el-input-number
              v-if="!scope.row.children || scope.row.children.length === 0"
              v-model="scope.row.percentage"
              :min="0"
              :max="100"
              :precision="decomposeConfig.precision"
              size="mini"
              @change="handlePercentageChange(scope.row)"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column label="分配金额" width="150" align="right" v-if="decomposeConfig.method === 'amount'">
          <template slot-scope="scope">
            <el-input-number
              v-if="!scope.row.children || scope.row.children.length === 0"
              v-model="scope.row.allocatedAmount"
              :min="0"
              :max="currentBudget?.budgetAmount"
              size="mini"
              @change="handleAmountChange(scope.row)"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column prop="responsiblePerson" label="责任人" width="120" align="center" />

        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEditNode(scope.row)"
              v-if="!scope.row.children || scope.row.children.length === 0"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="handleDeleteNode(scope.row)"
              v-if="scope.row.type !== 'root'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>

        <!-- 合计行 -->
        <el-table-column
          label="合计"
          align="right"
          fixed="right"
          width="150"
          header-align="center"
        >
          <template slot="header">
            <span>合计</span>
          </template>
          <template slot-scope="scope">
            <span v-if="scope.$index === 0" class="total-amount">
              {{ formatCurrency(totalAllocated) }}
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分解历史记录 -->
    <el-card class="history-card">
      <div slot="header">
        <span>分解历史</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          icon="el-icon-refresh"
          @click="handleRefreshHistory"
        >
          刷新历史
        </el-button>
      </div>

      <el-table :data="decomposeHistory" border stripe>
        <el-table-column type="index" width="60" label="序号" align="center" />
        <el-table-column prop="schemeName" label="方案名称" min-width="150" />
        <el-table-column prop="budgetName" label="预算名称" min-width="150" />
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.createTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSchemeStatusType(scope.row.status)">
              {{ getSchemeStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleViewHistory(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-copy-document"
              @click="handleCopyScheme(scope.row)"
            >
              复制
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建分解方案对话框 -->
    <el-dialog
      title="新建分解方案"
      :visible.sync="schemeDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="schemeForm"
        :model="schemeForm"
        :rules="schemeRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="方案名称" prop="schemeName">
          <el-input v-model="schemeForm.schemeName" placeholder="请输入分解方案名称" />
        </el-form-item>

        <el-form-item label="方案描述" prop="description">
          <el-input
            type="textarea"
            v-model="schemeForm.description"
            :rows="3"
            placeholder="请输入方案描述"
          />
        </el-form-item>

        <el-form-item label="选择预算" prop="budgetId">
          <el-select
            v-model="schemeForm.budgetId"
            placeholder="请选择要分解的预算"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="budget in budgetList"
              :key="budget.budgetId"
              :label="budget.budgetName"
              :value="budget.budgetId"
            >
              <span>{{ budget.budgetName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">
                {{ formatCurrency(budget.budgetAmount) }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="分解维度" prop="dimensions">
          <el-checkbox-group v-model="schemeForm.dimensions">
            <el-checkbox label="department">按部门</el-checkbox>
            <el-checkbox label="project">按项目</el-checkbox>
            <el-checkbox label="period">按期间</el-checkbox>
            <el-checkbox label="category">按类别</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="分配方式" prop="method">
          <el-select v-model="schemeForm.method" style="width: 100%">
            <el-option label="按比例分配" value="percentage" />
            <el-option label="按固定金额" value="amount" />
            <el-option label="按公式计算" value="formula" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="schemeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitScheme" :loading="submitting">
          确认创建
        </el-button>
      </div>
    </el-dialog>

    <!-- 编辑节点对话框 -->
    <el-dialog
      title="编辑分解节点"
      :visible.sync="nodeDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="nodeForm"
        :model="nodeForm"
        :rules="nodeRules"
        label-width="100px"
        size="small"
      >
        <el-form-item label="节点名称" prop="name">
          <el-input v-model="nodeForm.name" placeholder="请输入节点名称" />
        </el-form-item>

        <el-form-item label="责任人" prop="responsiblePerson">
          <el-select
            v-model="nodeForm.responsiblePerson"
            placeholder="请选择责任人"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="user in userList"
              :key="user.userId"
              :label="user.userName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="分配比例" prop="percentage" v-if="decomposeConfig.method === 'percentage'">
          <el-input-number
            v-model="nodeForm.percentage"
            :min="0"
            :max="100"
            :precision="decomposeConfig.precision"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="分配金额" prop="allocatedAmount" v-if="decomposeConfig.method === 'amount'">
          <el-input-number
            v-model="nodeForm.allocatedAmount"
            :min="0"
            :max="currentBudget?.budgetAmount"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            type="textarea"
            v-model="nodeForm.remark"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="nodeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitNode" :loading="submitting">
          确认保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatCurrency, formatDate } from '@/utils/index'

export default {
  name: 'BudgetDecompose',
  data() {
    return {
      // 预算相关数据
      budgetList: [],
      selectedBudget: null,
      currentBudget: null,

      // 分解方案相关数据
      schemeList: [],
      selectedScheme: null,
      currentScheme: null,

      // 分解配置
      decomposeConfig: {
        dimensions: [],
        method: 'percentage',
        precision: 2
      },

      // 分解树数据
      decomposeTreeData: [],
      expandedKeys: [],
      calculating: false,

      // 分解历史
      decomposeHistory: [],

      // 用户列表
      userList: [],

      // 对话框状态
      schemeDialogVisible: false,
      nodeDialogVisible: false,

      // 方案表单
      schemeForm: {
        schemeName: '',
        description: '',
        budgetId: null,
        dimensions: [],
        method: 'percentage'
      },
      schemeRules: {
        schemeName: [
          { required: true, message: '请输入方案名称', trigger: 'blur' }
        ],
        budgetId: [
          { required: true, message: '请选择预算', trigger: 'change' }
        ],
        dimensions: [
          { required: true, message: '请选择分解维度', trigger: 'change' }
        ],
        method: [
          { required: true, message: '请选择分配方式', trigger: 'change' }
        ]
      },

      // 节点表单
      nodeForm: {
        name: '',
        responsiblePerson: null,
        percentage: 0,
        allocatedAmount: 0,
        remark: ''
      },
      nodeRules: {
        name: [
          { required: true, message: '请输入节点名称', trigger: 'blur' }
        ],
        responsiblePerson: [
          { required: true, message: '请选择责任人', trigger: 'change' }
        ]
      },

      // 提交状态
      submitting: false
    }
  },

  computed: {
    // 已分配金额总计
    totalAllocated() {
      return this.calculateTotalAllocated(this.decomposeTreeData)
    },

    // 分解完成率
    completionRate() {
      if (!this.currentBudget || this.currentBudget.budgetAmount === 0) return 0
      return ((this.totalAllocated / this.currentBudget.budgetAmount) * 100).toFixed(2)
    }
  },

  created() {
    this.fetchBudgetList()
    this.fetchSchemeList()
    this.fetchUserList()
    this.fetchDecomposeHistory()
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  methods: {
    // 格式化货币
    formatCurrency,

    // 获取预算列表
    async fetchBudgetList() {
      try {
        const response = await budgetApi.getApprovalList({ pageNo: 1, pageSize: 1000 })
        if (response.code === 1) {
          this.budgetList = response.data.tlist || []
        }
      } catch (error) {
        console.error('获取预算列表异常:', error)
      }
    },

    // 获取分解方案列表
    async fetchSchemeList() {
      try {
        const response = await budgetApi.getDecomposeScheme({ pageNo: 1, pageSize: 1000 })
        if (response.code === 1) {
          this.schemeList = response.data.tlist || []
        }
      } catch (error) {
        console.error('获取分解方案列表异常:', error)
      }
    },

    // 获取用户列表
    async fetchUserList() {
      try {
        const response = await budgetApi.getDepartmentList()
        if (response.code === 1) {
          this.userList = response.data || []
        }
      } catch (error) {
        console.error('获取用户列表异常:', error)
      }
    },

    // 获取分解历史
    async fetchDecomposeHistory() {
      try {
        const response = await budgetApi.getDecomposeHistory({ pageNo: 1, pageSize: 10 })
        if (response.code === 1) {
          this.decomposeHistory = response.data.tlist || []
        }
      } catch (error) {
        console.error('获取分解历史异常:', error)
      }
    },

    // 预算变更
    handleBudgetChange(budgetId) {
      const budget = this.budgetList.find(item => item.budgetId === budgetId)
      this.currentBudget = budget || null
      this.resetDecomposeData()
    },

    // 方案变更
    handleSchemeChange(schemeId) {
      const scheme = this.schemeList.find(item => item.schemeId === schemeId)
      this.currentScheme = scheme || null
      if (scheme) {
        this.loadSchemeData(scheme)
      }
    },

    // 维度变更
    handleDimensionChange() {
      this.resetDecomposeData()
    },

    // 分配方式变更
    handleMethodChange() {
      this.resetDecomposeData()
    },

    // 重置分解数据
    resetDecomposeData() {
      this.decomposeTreeData = []
      this.expandedKeys = []
    },

    // 加载方案数据
    async loadSchemeData(scheme) {
      try {
        const response = await budgetApi.getDecomposeScheme({ schemeId: scheme.schemeId })
        if (response.code === 1) {
          this.decomposeTreeData = response.data.decomposeTree || []
          this.decomposeConfig = {
            dimensions: scheme.dimensions || [],
            method: scheme.method || 'percentage',
            precision: scheme.precision || 2
          }
          this.selectedBudget = scheme.budgetId
          this.handleBudgetChange(scheme.budgetId)
        }
      } catch (error) {
        console.error('加载方案数据异常:', error)
      }
    },

    // 创建分解方案
    handleCreateScheme() {
      this.schemeForm = {
        schemeName: '',
        description: '',
        budgetId: this.selectedBudget,
        dimensions: this.decomposeConfig.dimensions,
        method: this.decomposeConfig.method
      }
      this.schemeDialogVisible = true
    },

    // 提交方案
    async handleSubmitScheme() {
      try {
        await this.$refs.schemeForm.validate()

        this.submitting = true
        const response = await budgetApi.createDecompose(this.schemeForm)

        if (response.code === 1) {
          this.$message.success('分解方案创建成功')
          this.schemeDialogVisible = false
          this.fetchSchemeList()
        } else {
          this.$message.error(response.message || '创建分解方案失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('创建分解方案异常:', error)
          this.$message.error('创建分解方案失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 计算分解
    async handleCalculateDecompose() {
      if (!this.selectedBudget || this.decomposeConfig.dimensions.length === 0) {
        this.$message.warning('请选择预算和分解维度')
        return
      }

      this.calculating = true
      try {
        const response = await budgetApi.calculateDecompose({
          budgetId: this.selectedBudget,
          ...this.decomposeConfig
        })

        if (response.code === 1) {
          this.decomposeTreeData = response.data || []
          this.expandedKeys = this.decomposeTreeData.map(item => item.id)
          this.$message.success('分解计算完成')
        } else {
          this.$message.error(response.message || '分解计算失败')
        }
      } catch (error) {
        console.error('分解计算异常:', error)
        this.$message.error('分解计算失败，请稍后重试')
      } finally {
        this.calculating = false
      }
    },

    // 保存分解结果
    async handleSaveDecompose() {
      if (!this.currentScheme || this.decomposeTreeData.length === 0) {
        this.$message.warning('没有可保存的分解数据')
        return
      }

      try {
        this.submitting = true
        const response = await budgetApi.saveDecompose({
          schemeId: this.currentScheme.schemeId,
          decomposeTree: this.decomposeTreeData,
          totalAmount: this.totalAllocated
        })

        if (response.code === 1) {
          this.$message.success('分解结果保存成功')
          this.fetchDecomposeHistory()
        } else {
          this.$message.error(response.message || '保存分解结果失败')
        }
      } catch (error) {
        console.error('保存分解结果异常:', error)
        this.$message.error('保存分解结果失败，请稍后重试')
      } finally {
        this.submitting = false
      }
    },

    // 编辑节点
    handleEditNode(node) {
      this.nodeForm = {
        id: node.id,
        name: node.name,
        responsiblePerson: node.responsiblePerson,
        percentage: node.percentage,
        allocatedAmount: node.allocatedAmount,
        remark: node.remark
      }
      this.nodeDialogVisible = true
    },

    // 提交节点编辑
    async handleSubmitNode() {
      try {
        await this.$refs.nodeForm.validate()

        // 更新树数据中的节点
        this.updateNodeInTree(this.decomposeTreeData, this.nodeForm)

        this.nodeDialogVisible = false
        this.$message.success('节点更新成功')
      } catch (error) {
        if (error !== false) {
          console.error('节点更新异常:', error)
          this.$message.error('节点更新失败，请稍后重试')
        }
      }
    },

    // 更新树中的节点
    updateNodeInTree(tree, nodeForm) {
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.id === nodeForm.id) {
          Object.assign(node, nodeForm)
          return true
        }
        if (node.children && node.children.length > 0) {
          if (this.updateNodeInTree(node.children, nodeForm)) {
            return true
          }
        }
      }
      return false
    },

    // 删除节点
    handleDeleteNode(node) {
      this.$confirm('确定要删除此节点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteNodeFromTree(this.decomposeTreeData, node.id)
        this.$message.success('节点删除成功')
      }).catch(() => {})
    },

    // 从树中删除节点
    deleteNodeFromTree(tree, nodeId) {
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.id === nodeId) {
          tree.splice(i, 1)
          return true
        }
        if (node.children && node.children.length > 0) {
          if (this.deleteNodeFromTree(node.children, nodeId)) {
            return true
          }
        }
      }
      return false
    },

    // 全部展开
    handleExpandAll() {
      this.expandedKeys = this.getAllNodeIds(this.decomposeTreeData)
    },

    // 全部收起
    handleCollapseAll() {
      this.expandedKeys = []
    },

    // 获取所有节点ID
    getAllNodeIds(tree) {
      let ids = []
      for (const node of tree) {
        ids.push(node.id)
        if (node.children && node.children.length > 0) {
          ids = ids.concat(this.getAllNodeIds(node.children))
        }
      }
      return ids
    },

    // 展开变化
    handleExpandChange(row, expanded) {
      if (expanded) {
        this.expandedKeys.push(row.id)
      } else {
        const index = this.expandedKeys.indexOf(row.id)
        if (index > -1) {
          this.expandedKeys.splice(index, 1)
        }
      }
    },

    // 比例变更
    handlePercentageChange(row) {
      const parentBudget = this.getParentBudget(row, this.decomposeTreeData)
      if (parentBudget) {
        row.allocatedAmount = (parentBudget * row.percentage / 100).toFixed(2)
      }
    },

    // 金额变更
    handleAmountChange(row) {
      const parentBudget = this.getParentBudget(row, this.decomposeTreeData)
      if (parentBudget && parentBudget > 0) {
        row.percentage = ((row.allocatedAmount / parentBudget) * 100).toFixed(this.decomposeConfig.precision)
      }
    },

    // 获取父级预算
    getParentBudget(targetNode, tree, parentBudget = null) {
      for (const node of tree) {
        if (node.id === targetNode.id) {
          return parentBudget
        }
        if (node.children && node.children.length > 0) {
          const result = this.getParentBudget(targetNode, node.children, node.budgetAmount)
          if (result !== null) {
            return result
          }
        }
      }
      return null
    },

    // 计算已分配金额总计
    calculateTotalAllocated(tree) {
      let total = 0
      for (const node of tree) {
        if (!node.children || node.children.length === 0) {
          total += parseFloat(node.allocatedAmount || 0)
        } else {
          total += this.calculateTotalAllocated(node.children)
        }
      }
      return total
    },

    // 导出结果
    handleExportResult() {
      try {
        const data = this.tableData || this.list || this.decomposeResult || []
        const exportData = Array.isArray(data) ? data : [data]
        if (!exportData.length || (exportData.length === 1 && !exportData[0])) {
          this.$message.warning('暂无数据可导出'); return
        }
        const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算分解结果.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) { this.$message.error('导出失败') }
    },

    // 查看历史详情
    handleViewHistory(history) {
      this.selectedScheme = history.schemeId
      this.handleSchemeChange(history.schemeId)
    },

    // 复制方案
    handleCopyScheme(history) {
      this.schemeForm = {
        schemeName: history.schemeName + '_副本',
        description: history.description,
        budgetId: history.budgetId,
        dimensions: history.dimensions || [],
        method: history.method || 'percentage'
      }
      this.schemeDialogVisible = true
    },

    // 刷新
    handleRefresh() {
      this.fetchSchemeList()
      this.fetchDecomposeHistory()
    },

    // 刷新历史
    handleRefreshHistory() {
      this.fetchDecomposeHistory()
    },

    // 获取方案状态类型
    getSchemeStatusType(status) {
      const typeMap = {
        'draft': 'info',
        'calculated': 'warning',
        'saved': 'success',
        'approved': 'success'
      }
      return typeMap[status] || 'info'
    },

    // 获取方案状态文本
    getSchemeStatusText(status) {
      const textMap = {
        'draft': '草稿',
        'calculated': '已计算',
        'saved': '已保存',
        'approved': '已审批'
      }
      return textMap[status] || '未知'
    },

    // 获取节点类型标签
    getNodeTypeTag(type) {
      const tagMap = {
        'root': 'danger',
        'department': 'primary',
        'project': 'success',
        'period': 'warning',
        'category': 'info'
      }
      return tagMap[type] || ''
    },

    // 获取节点类型文本
    getNodeTypeText(type) {
      const textMap = {
        'root': '总预算',
        'department': '部门',
        'project': '项目',
        'period': '期间',
        'category': '类别'
      }
      return textMap[type] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-decompose-container {
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

  .config-card {
    margin-bottom: 16px;
  }

  .result-card {
    margin-bottom: 16px;

    .result-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .result-actions {
        display: flex;
        gap: 8px;
      }
    }

    .overview-info {
      margin-bottom: 20px;
      padding: 20px;
      background: #F8F9FA;
      border-radius: 8px;

      .overview-item {
        text-align: center;
        padding: 16px;
        background: white;
        border-radius: 6px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

        .label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 8px;
        }

        .value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;

          &.allocated {
            color: #E6A23C;
          }

          &.remaining {
            color: #67C23A;
          }
        }
      }
    }

    .tree-node {
      display: flex;
      align-items: center;
    }

    .parent-amount {
      font-weight: 600;
      color: #409EFF;
    }

    .total-amount {
      font-weight: 600;
      color: #E6A23C;
      font-size: 16px;
    }
  }

  .history-card {
    .el-table {
      margin-top: 0;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>