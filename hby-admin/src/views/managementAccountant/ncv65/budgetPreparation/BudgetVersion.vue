<template>
  <div class="budget-version">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算版本管理</h2>
      <p>管理预算版本，支持版本创建、比较、回滚、合并等操作</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateVersion">创建版本</el-button>
            <el-button type="success" icon="el-icon-copy-document" @click="handleCopyVersion">复制版本</el-button>
            <el-button type="warning" icon="el-icon-s-operation" @click="handleCompareVersions">版本比较</el-button>
            <el-button type="info" icon="el-icon-refresh-left" @click="handleMergeVersions">版本合并</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 版本统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ versionStats.activeCount }}</div>
            <div class="stat-label">活跃版本</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-star-on"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card draft-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ versionStats.draftCount }}</div>
            <div class="stat-label">草稿版本</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-edit-outline"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card archived-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ versionStats.archivedCount }}</div>
            <div class="stat-label">归档版本</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-folder"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ versionStats.totalCount }}</div>
            <div class="stat-label">总版本数</div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="版本名称">
          <el-input
            v-model="queryForm.versionName"
            placeholder="请输入版本名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="版本状态">
          <el-select
            v-model="queryForm.versionStatus"
            placeholder="请选择版本状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in versionStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度">
          <el-date-picker
            v-model="queryForm.fiscalYear"
            type="year"
            placeholder="选择预算年度"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="创建人">
          <el-input
            v-model="queryForm.creatorName"
            placeholder="请输入创建人"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="queryForm.createDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 版本列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算版本列表</span>
        <div class="table-tools">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="versionList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="versionCode" label="版本编号" width="150" show-overflow-tooltip />
        
        <el-table-column prop="versionName" label="版本名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="version-name">
              <i v-if="scope.row.isActive" class="el-icon-star-on active-icon"></i>
              <span>{{ scope.row.versionName }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="versionType" label="版本类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVersionTypeColor(scope.row.versionType)" size="mini">
              {{ getVersionTypeText(scope.row.versionType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="versionStatus" label="版本状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getVersionStatusType(scope.row.versionStatus)" size="mini">
              {{ getVersionStatusText(scope.row.versionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="fiscalYear" label="预算年度" width="100" align="center" />
        
        <el-table-column prop="baseVersionId" label="基础版本" width="150" show-overflow-tooltip />

        <el-table-column prop="budgetAmount" label="预算总额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="creatorName" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />

        <el-table-column prop="updateTime" label="最后修改" width="150" align="center" />
        
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              v-if="canActivate(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-star-on"
              class="success-text"
              @click.stop="handleActivate(scope.row)"
            >激活</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="compare" icon="el-icon-s-operation">比较</el-dropdown-item>
                <el-dropdown-item command="rollback" icon="el-icon-refresh-left">回滚</el-dropdown-item>
                <el-dropdown-item command="archive" icon="el-icon-folder">归档</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑版本对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      append-to-body
      @close="handleDialogClose"
    >
      <el-form
        ref="versionForm"
        :model="versionForm"
        :rules="versionRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="版本名称" prop="versionName">
          <el-input
            v-model="versionForm.versionName"
            placeholder="请输入版本名称"
          />
        </el-form-item>
        
        <el-form-item label="版本类型" prop="versionType">
          <el-select
            v-model="versionForm.versionType"
            placeholder="请选择版本类型"
            style="width: 100%"
          >
            <el-option
              v-for="item in versionTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="预算年度" prop="fiscalYear">
          <el-date-picker
            v-model="versionForm.fiscalYear"
            type="year"
            placeholder="选择预算年度"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="基础版本" prop="baseVersionId">
          <el-select
            v-model="versionForm.baseVersionId"
            placeholder="请选择基础版本（可选）"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in baseVersionOptions"
              :key="item.versionId"
              :label="item.versionName"
              :value="item.versionId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="版本描述" prop="versionDescription">
          <el-input
            v-model="versionForm.versionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入版本描述"
          />
        </el-form-item>
        
        <el-form-item label="是否激活">
          <el-switch
            v-model="versionForm.isActive"
            active-text="是"
            inactive-text="否"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 版本比较对话框 -->
    <el-dialog
      title="版本比较"
      :visible.sync="compareDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      append-to-body
    >
      <div class="compare-content">
        <div class="compare-header">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="源版本">
                <el-select
                  v-model="compareForm.sourceVersionId"
                  placeholder="请选择源版本"
                  style="width: 100%"
                  @change="handleCompareVersionChange"
                >
                  <el-option
                    v-for="item in versionList"
                    :key="item.versionId"
                    :label="item.versionName"
                    :value="item.versionId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="目标版本">
                <el-select
                  v-model="compareForm.targetVersionId"
                  placeholder="请选择目标版本"
                  style="width: 100%"
                  @change="handleCompareVersionChange"
                >
                  <el-option
                    v-for="item in versionList"
                    :key="item.versionId"
                    :label="item.versionName"
                    :value="item.versionId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        
        <div class="compare-result" v-if="compareResult">
          <el-tabs v-model="activeCompareTab">
            <el-tab-pane label="差异汇总" name="summary">
              <div class="summary-stats">
                <el-row :gutter="20">
                  <el-col :span="6">
                    <div class="summary-item added">
                      <div class="summary-number">{{ compareResult.summary.addedCount }}</div>
                      <div class="summary-label">新增项目</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="summary-item modified">
                      <div class="summary-number">{{ compareResult.summary.modifiedCount }}</div>
                      <div class="summary-label">修改项目</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="summary-item deleted">
                      <div class="summary-number">{{ compareResult.summary.deletedCount }}</div>
                      <div class="summary-label">删除项目</div>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="summary-item unchanged">
                      <div class="summary-number">{{ compareResult.summary.unchangedCount }}</div>
                      <div class="summary-label">未变更项目</div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="详细差异" name="details">
              <el-table
                :data="compareResult.details"
                border
                size="small"
                max-height="400"
              >
                <el-table-column prop="budgetAccount" label="预算科目" width="200" />
                <el-table-column prop="changeType" label="变更类型" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getChangeTypeColor(scope.row.changeType)" size="mini">
                      {{ getChangeTypeText(scope.row.changeType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="sourceAmount" label="源版本金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.sourceAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="targetAmount" label="目标版本金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span class="amount-text">{{ formatAmount(scope.row.targetAmount) }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="differenceAmount" label="差异金额" width="120" align="right">
                  <template slot-scope="scope">
                    <span :class="getDifferenceClass(scope.row.differenceAmount)">
                      {{ formatAmount(scope.row.differenceAmount) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="differencePercent" label="差异比例" width="100" align="right">
                  <template slot-scope="scope">
                    <span :class="getDifferenceClass(scope.row.differenceAmount)">
                      {{ scope.row.differencePercent }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" min-width="150" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="compareDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExportCompare">导出比较结果</el-button>
      </div>
    </el-dialog>

    <!-- 版本详情对话框 -->
    <el-dialog title="版本详情" :visible.sync="detailDialogVisible" width="700px" :close-on-click-modal="false" append-to-body>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="版本名称">{{ versionDetail.versionName }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ versionDetail.versionNumber }}</el-descriptions-item>
        <el-descriptions-item label="版本类型">{{ versionDetail.versionType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ versionDetail.versionStatus }}</el-descriptions-item>
        <el-descriptions-item label="基础版本">{{ versionDetail.baseVersionId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ versionDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="说明" :span="2">{{ versionDetail.versionDescription || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 版本设置对话框 -->
    <el-dialog title="版本设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="自动版本号">
          <el-switch v-model="settingsForm.autoVersionNumber" />
        </el-form-item>
        <el-form-item label="保留历史版本数">
          <el-input-number v-model="settingsForm.maxVersions" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetVersionApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetVersion',
  data() {
    return {
      // 查询参数
      queryForm: {
        versionName: '',
        versionStatus: '',
        fiscalYear: null,
        creatorName: '',
        createDateRange: []
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      versionList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      versionStats: {
        activeCount: 0,
        draftCount: 0,
        archivedCount: 0,
        totalCount: 0
      },
      
      // 版本对话框
      dialogVisible: false,
      dialogTitle: '',
      versionForm: {
        versionId: null,
        versionName: '',
        versionType: '',
        fiscalYear: null,
        baseVersionId: null,
        versionDescription: '',
        isActive: false
      },
      versionRules: {
        versionName: [
          { required: true, message: '请输入版本名称', trigger: 'blur' }
        ],
        versionType: [
          { required: true, message: '请选择版本类型', trigger: 'change' }
        ],
        fiscalYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ]
      },
      
      // 版本比较对话框
      compareDialogVisible: false,
      compareForm: {
        sourceVersionId: null,
        targetVersionId: null
      },
      compareResult: null,
      activeCompareTab: 'summary',
      
      // 选项数据
      versionStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'ACTIVE', label: '活跃' },
        { value: 'ARCHIVED', label: '归档' },
        { value: 'DELETED', label: '已删除' }
      ],
      versionTypeOptions: [
        { value: 'INITIAL', label: '初始版本' },
        { value: 'REVISION', label: '修订版本' },
        { value: 'ADJUSTMENT', label: '调整版本' },
        { value: 'FORECAST', label: '预测版本' }
      ],
      baseVersionOptions: [],

      detailDialogVisible: false,
      versionDetail: {},

      settingsDialogVisible: false,
      settingsForm: { autoVersionNumber: true, maxVersions: 10 }
    }
  },
  
  created() {
    this.getList()
    this.getVersionStats()
    this.loadBaseVersions()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        // el-date-picker type="year" 返回 Date 对象，后端需要整数年份
        if (params.fiscalYear instanceof Date) {
          params.fiscalYear = params.fiscalYear.getFullYear()
        } else if (typeof params.fiscalYear === 'string' && params.fiscalYear) {
          params.fiscalYear = new Date(params.fiscalYear).getFullYear()
        }
        const response = await budgetVersionApi.getPage(params)
        this.versionList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 获取版本统计
    async getVersionStats() {
      try {
        const response = await budgetVersionApi.getStats()
        this.versionStats = response.data
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 加载基础版本选项
    async loadBaseVersions() {
      try {
        const response = await budgetVersionApi.getBaseVersions()
        this.baseVersionOptions = response.data
      } catch (error) {
        console.error('加载基础版本失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        versionName: '',
        versionStatus: '',
        fiscalYear: null,
        creatorName: '',
        createDateRange: []
      }
      this.handleQuery()
    },
    
    // 创建版本
    handleCreateVersion() {
      this.dialogTitle = '创建预算版本'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 复制版本
    handleCopyVersion() {
      if (this.selectedRows.length !== 1) {
        this.$message.warning('请选择一个版本进行复制')
        return
      }
      
      const sourceVersion = this.selectedRows[0]
      this.dialogTitle = '复制预算版本'
      this.dialogVisible = true
      this.versionForm = {
        ...sourceVersion,
        versionId: null,
        versionName: sourceVersion.versionName + '_副本',
        versionCode: null,
        isActive: false
      }
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算版本'
      this.dialogVisible = true
      this.versionForm = { ...row }
    },
    
    // 查看
    handleView(row) {
      this.versionDetail = { ...row }
      this.detailDialogVisible = true
    },
    
    // 激活版本
    async handleActivate(row) {
      try {
        await this.$confirm('确认激活该版本吗？激活后其他版本将变为非活跃状态。', '提示', {
          type: 'warning'
        })
        await budgetVersionApi.activate(row.id)
        this.$message.success('版本激活成功')
        this.getList()
        this.getVersionStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本激活失败：' + error.message)
        }
      }
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.versionForm.validate()

        const submitData = { ...this.versionForm }
        // el-date-picker type="year" 返回的是 Date 对象，后端需要整数年份
        if (submitData.fiscalYear instanceof Date) {
          submitData.fiscalYear = submitData.fiscalYear.getFullYear()
        } else if (typeof submitData.fiscalYear === 'string' && submitData.fiscalYear) {
          submitData.fiscalYear = new Date(submitData.fiscalYear).getFullYear()
        }

        if (submitData.versionId) {
          await budgetVersionApi.update(submitData)
          this.$message.success('更新成功')
        } else {
          await budgetVersionApi.create(submitData)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.getList()
        this.getVersionStats()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.versionForm = {
        versionId: null,
        versionName: '',
        versionType: '',
        fiscalYear: null,
        baseVersionId: null,
        versionDescription: '',
        isActive: false
      }
      this.$nextTick(() => {
        this.$refs.versionForm && this.$refs.versionForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 版本比较
    handleCompareVersions() {
      this.compareDialogVisible = true
      this.compareForm = {
        sourceVersionId: null,
        targetVersionId: null
      }
      this.compareResult = null
    },
    
    // 比较版本改变
    async handleCompareVersionChange() {
      if (this.compareForm.sourceVersionId && this.compareForm.targetVersionId) {
        if (this.compareForm.sourceVersionId === this.compareForm.targetVersionId) {
          this.$message.warning('源版本和目标版本不能相同')
          return
        }
        
        try {
          const response = await budgetVersionApi.compare(
            this.compareForm.sourceVersionId,
            this.compareForm.targetVersionId
          )
          this.compareResult = response.data
        } catch (error) {
          this.$message.error('版本比较失败：' + error.message)
        }
      }
    },
    
    // 导出比较结果
    async handleExportCompare() {
      try {
        await budgetVersionApi.exportCompare(
          this.compareForm.sourceVersionId,
          this.compareForm.targetVersionId
        )
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 版本合并
    async handleMergeVersions() {
      if (this.selectedRows.length < 2) {
        this.$message.warning('请选择至少两个版本进行合并')
        return
      }
      
      try {
        await this.$confirm('确认合并选中的版本吗？', '提示', {
          type: 'warning'
        })
        
        const versionIds = this.selectedRows.map(row => row.id)
        await budgetVersionApi.merge(versionIds)
        this.$message.success('版本合并成功')
        this.getList()
        this.getVersionStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本合并失败：' + error.message)
        }
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getVersionStats()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopyVersion()
          break
        case 'compare':
          this.handleCompareWithCurrent(row)
          break
        case 'rollback':
          this.handleRollback(row)
          break
        case 'archive':
          this.handleArchive(row)
          break
        case 'export':
          this.handleExport(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    
    // 与当前版本比较
    handleCompareWithCurrent(row) {
      const activeVersion = this.versionList.find(v => v.isActive)
      if (!activeVersion) {
        this.$message.warning('没有找到活跃版本')
        return
      }
      
      this.compareDialogVisible = true
      this.compareForm = {
        sourceVersionId: activeVersion.versionId,
        targetVersionId: row.id
      }
      this.handleCompareVersionChange()
    },
    
    // 回滚版本
    async handleRollback(row) {
      try {
        await this.$confirm('确认回滚到该版本吗？', '提示', {
          type: 'warning'
        })
        await budgetVersionApi.rollback(row.id)
        this.$message.success('版本回滚成功')
        this.getList()
        this.getVersionStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本回滚失败：' + error.message)
        }
      }
    },
    
    // 归档版本
    async handleArchive(row) {
      try {
        await this.$confirm('确认归档该版本吗？', '提示', {
          type: 'warning'
        })
        await budgetVersionApi.archive(row.id)
        this.$message.success('版本归档成功')
        this.getList()
        this.getVersionStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('版本归档失败：' + error.message)
        }
      }
    },
    
    // 导出版本
    async handleExport(row) {
      try {
        await budgetVersionApi.export(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 删除版本
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该版本吗？删除后不可恢复。', '提示', {
          type: 'warning'
        })
        await budgetVersionApi.delete(row.versionId)
        this.$message.success('删除成功')
        this.getList()
        this.getVersionStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.versionStatus === 'DRAFT' || row.versionStatus === 'REVISION'
    },
    
    // 判断是否可以激活
    canActivate(row) {
      return row.versionStatus !== 'ACTIVE' && row.versionStatus !== 'DELETED'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取版本类型颜色
    getVersionTypeColor(type) {
      const colorMap = {
        'INITIAL': 'primary',
        'REVISION': 'success',
        'ADJUSTMENT': 'warning',
        'FORECAST': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取版本类型文本
    getVersionTypeText(type) {
      const textMap = {
        'INITIAL': '初始版本',
        'REVISION': '修订版本',
        'ADJUSTMENT': '调整版本',
        'FORECAST': '预测版本'
      }
      return textMap[type] || type
    },
    
    // 获取版本状态类型
    getVersionStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'ARCHIVED': 'warning',
        'DELETED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取版本状态文本
    getVersionStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '活跃',
        'ARCHIVED': '归档',
        'DELETED': '已删除'
      }
      return statusMap[status] || status
    },
    
    // 获取变更类型颜色
    getChangeTypeColor(type) {
      const colorMap = {
        'ADDED': 'success',
        'MODIFIED': 'warning',
        'DELETED': 'danger',
        'UNCHANGED': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取变更类型文本
    getChangeTypeText(type) {
      const textMap = {
        'ADDED': '新增',
        'MODIFIED': '修改',
        'DELETED': '删除',
        'UNCHANGED': '未变更'
      }
      return textMap[type] || type
    },
    
    // 获取差异样式
    getDifferenceClass(amount) {
      if (!amount) return ''
      return parseFloat(amount) >= 0 ? 'success-text' : 'danger-text'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-version {
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
  
  .toolbar-card,
  .search-card,
  .table-card {
    margin-bottom: 20px;
  }
  
  .stats-row {
    margin-bottom: 20px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      
      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.draft-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.archived-card {
        background: linear-gradient(135deg, #909399, #B1B3B8);
        color: white;
      }
      
      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      .stat-content {
        position: relative;
        z-index: 2;
        
        .stat-number {
          font-size: 32px;
          font-weight: 600;
          margin-bottom: 8px;
        }
        
        .stat-label {
          font-size: 14px;
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
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      gap: 8px;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .version-name {
    display: flex;
    align-items: center;
    
    .active-icon {
      color: #F7BA2A;
      margin-right: 8px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
  
  .compare-content {
    .compare-header {
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #EBEEF5;
    }
    
    .summary-stats {
      margin-bottom: 20px;
      
      .summary-item {
        text-align: center;
        padding: 20px;
        border-radius: 8px;
        
        &.added {
          background: linear-gradient(135deg, #67C23A, #85CE61);
          color: white;
        }
        
        &.modified {
          background: linear-gradient(135deg, #E6A23C, #EEBE77);
          color: white;
        }
        
        &.deleted {
          background: linear-gradient(135deg, #F56C6C, #F78989);
          color: white;
        }
        
        &.unchanged {
          background: linear-gradient(135deg, #909399, #B1B3B8);
          color: white;
        }
        
        .summary-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 8px;
        }
        
        .summary-label {
          font-size: 14px;
          opacity: 0.9;
        }
      }
    }
  }
}
</style>
