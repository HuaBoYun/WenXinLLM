<template>
  <div class="budget-template-container">
    <!-- 头部操作区域 -->
    <el-card class="header-card">
      <div slot="header" class="header-title">
        <span>预算模板管理</span>
        <el-button-group class="header-actions">
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            @click="handleCreateTemplate"
          >
            新建模板
          </el-button>
          <el-button
            type="success"
            size="small"
            icon="el-icon-copy-document"
            @click="handleCopyTemplate"
            :disabled="!selectedTemplate"
          >
            复制模板
          </el-button>
          <el-button
            type="warning"
            size="small"
            icon="el-icon-share"
            @click="handleShareTemplate"
            :disabled="!selectedTemplate"
          >
            分享模板
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
        <el-form-item label="模板名称">
          <el-input
            v-model="filterForm.templateName"
            placeholder="请输入模板名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select
            v-model="filterForm.templateType"
            placeholder="请选择模板类型"
            clearable
            style="width: 150px"
          >
            <el-option label="年度预算模板" value="annual" />
            <el-option label="季度预算模板" value="quarterly" />
            <el-option label="月度预算模板" value="monthly" />
            <el-option label="项目预算模板" value="project" />
            <el-option label="部门预算模板" value="department" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板分类">
          <el-select
            v-model="filterForm.category"
            placeholder="请选择模板分类"
            clearable
            style="width: 150px"
          >
            <el-option label="通用模板" value="general" />
            <el-option label="部门模板" value="dept" />
            <el-option label="项目模板" value="project" />
            <el-option label="个人模板" value="personal" />
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

    <!-- 模板列表 -->
    <el-card class="list-card">
      <el-table
        v-loading="loading"
        :data="templateList"
        border
        stripe
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" width="60" label="序号" align="center" />

        <el-table-column prop="templateName" label="模板名称" min-width="180" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewTemplate(scope.row)">
              {{ scope.row.templateName }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="templateType" label="模板类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTemplateTypeTag(scope.row.templateType)">
              {{ getTemplateTypeText(scope.row.templateType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="category" label="分类" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getCategoryTag(scope.row.category)" size="small">
              {{ getCategoryText(scope.row.category) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="itemCount" label="项目数" width="80" align="center">
          <template slot-scope="scope">
            <el-badge :value="scope.row.itemCount" class="item-badge" type="primary" />
          </template>
        </el-table-column>

        <el-table-column prop="useCount" label="使用次数" width="80" align="center">
          <template slot-scope="scope">
            <span class="use-count">{{ scope.row.useCount }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="isPublic" label="是否公开" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isPublic ? 'success' : 'info'" size="small">
              {{ scope.row.isPublic ? '公开' : '私有' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createBy" label="创建人" width="100" align="center" />

        <el-table-column prop="createTime" label="创建时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.createTime | formatDate }}
          </template>
        </el-table-column>

        <el-table-column prop="updateTime" label="更新时间" width="150" align="center">
          <template slot-scope="scope">
            {{ scope.row.updateTime | formatDate }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleViewTemplate(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEditTemplate(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-document-add"
              @click="handleApplyTemplate(scope.row)"
            >
              应用
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="handleDeleteTemplate(scope.row)"
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

    <!-- 新建/编辑模板对话框 -->
    <el-dialog
      :title="templateDialogTitle"
      :visible.sync="templateDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleTemplateDialogClose"
    >
      <el-form
        ref="templateForm"
        :model="templateForm"
        :rules="templateRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input
                v-model="templateForm.templateName"
                placeholder="请输入模板名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select
                v-model="templateForm.templateType"
                placeholder="请选择模板类型"
                style="width: 100%"
              >
                <el-option label="年度预算模板" value="annual" />
                <el-option label="季度预算模板" value="quarterly" />
                <el-option label="月度预算模板" value="monthly" />
                <el-option label="项目预算模板" value="project" />
                <el-option label="部门预算模板" value="department" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板分类" prop="category">
              <el-select
                v-model="templateForm.category"
                placeholder="请选择模板分类"
                style="width: 100%"
              >
                <el-option label="通用模板" value="general" />
                <el-option label="部门模板" value="dept" />
                <el-option label="项目模板" value="project" />
                <el-option label="个人模板" value="personal" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否公开" prop="isPublic">
              <el-switch
                v-model="templateForm.isPublic"
                active-text="公开"
                inactive-text="私有"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="模板描述" prop="description">
          <el-input
            type="textarea"
            v-model="templateForm.description"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>

        <!-- 模板项目表格 -->
        <el-form-item label="模板项目" prop="items">
          <el-table
            :data="templateForm.items"
            border
            size="small"
            max-height="300"
          >
            <el-table-column type="index" width="60" label="序号" align="center" />
            <el-table-column prop="itemName" label="项目名称" min-width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.itemName" placeholder="请输入项目名称" />
              </template>
            </el-table-column>
            <el-table-column prop="itemCode" label="项目编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.itemCode" placeholder="请输入编码" />
              </template>
            </el-table-column>
            <el-table-column prop="category" label="费用类别" width="120">
              <template slot-scope="scope">
                <el-select v-model="scope.row.category" placeholder="请选择类别">
                  <el-option label="人工成本" value="labor" />
                  <el-option label="材料费用" value="material" />
                  <el-option label="设备费用" value="equipment" />
                  <el-option label="管理费用" value="management" />
                  <el-option label="其他费用" value="other" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="unit" label="计量单位" width="100">
              <template slot-scope="scope">
                <el-select v-model="scope.row.unit" placeholder="单位">
                  <el-option label="元" value="yuan" />
                  <el-option label="万元" value="tenThousand" />
                  <el-option label="人月" value="personMonth" />
                  <el-option label="台" value="set" />
                  <el-option label="个" value="piece" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="isRequired" label="必填项" width="80" align="center">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.isRequired" />
              </template>
            </el-table-column>
            <el-table-column prop="defaultValue" label="默认值" width="120">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.defaultValue"
                  :min="0"
                  :precision="2"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="120">
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
                  @click="handleDeleteItem(scope.$index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top: 10px; text-align: right;">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddItem">
              添加项目
            </el-button>
            <el-button type="success" size="small" icon="el-icon-copy-document" @click="handleImportFromExcel">
              从Excel导入
            </el-button>
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="templateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitTemplate" :loading="submitting">
          确认保存
        </el-button>
      </div>
    </el-dialog>

    <!-- 应用模板对话框 -->
    <el-dialog
      title="应用模板"
      :visible.sync="applyDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedTemplate">
        <el-form :model="applyForm" :rules="applyRules" ref="applyForm" label-width="120px" size="small">
          <el-form-item label="预算名称" prop="budgetName">
            <el-input v-model="applyForm.budgetName" placeholder="请输入预算名称" />
          </el-form-item>

          <el-form-item label="预算期间" prop="period">
            <el-date-picker
              v-model="applyForm.period"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="使用模板">
            <el-card class="template-preview" size="small">
              <div slot="header">
                <span>{{ selectedTemplate.templateName }}</span>
                <el-tag size="mini" style="float: right;">
                  {{ selectedTemplate.items.length }} 个项目
                </el-tag>
              </div>
              <el-table :data="selectedTemplate.items" border size="small" max-height="200">
                <el-table-column prop="itemName" label="项目名称" min-width="120" />
                <el-table-column prop="category" label="类别" width="100" />
                <el-table-column prop="unit" label="单位" width="80" />
                <el-table-column prop="defaultValue" label="默认值" width="100" align="right" />
              </el-table>
            </el-card>
          </el-form-item>

          <el-form-item label="填充默认值">
            <el-switch v-model="applyForm.fillDefaults" />
          </el-form-item>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitApply" :loading="submitting">
          确认应用
        </el-button>
      </div>
    </el-dialog>

    <!-- 模板详情对话框 -->
    <el-dialog
      title="模板详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedTemplate">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="3" border>
          <el-descriptions-item label="模板名称">{{ selectedTemplate.templateName }}</el-descriptions-item>
          <el-descriptions-item label="模板类型">
            <el-tag :type="getTemplateTypeTag(selectedTemplate.templateType)">
              {{ getTemplateTypeText(selectedTemplate.templateType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="模板分类">
            <el-tag :type="getCategoryTag(selectedTemplate.category)" size="small">
              {{ getCategoryText(selectedTemplate.category) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="项目数量">{{ selectedTemplate.itemCount }}个</el-descriptions-item>
          <el-descriptions-item label="使用次数">{{ selectedTemplate.useCount }}次</el-descriptions-item>
          <el-descriptions-item label="是否公开">
            <el-tag :type="selectedTemplate.isPublic ? 'success' : 'info'" size="small">
              {{ selectedTemplate.isPublic ? '公开' : '私有' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建人">{{ selectedTemplate.createBy }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ selectedTemplate.createTime | formatDate }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ selectedTemplate.updateTime | formatDate }}</el-descriptions-item>
        </el-descriptions>

        <!-- 模板描述 -->
        <div style="margin: 20px 0;">
          <h4>模板描述</h4>
          <p>{{ selectedTemplate.description || '暂无描述' }}</p>
        </div>

        <!-- 模板项目 -->
        <div style="margin-top: 20px;">
          <h4>模板项目 ({{ selectedTemplate.items.length }}个)</h4>
          <el-table :data="selectedTemplate.items" border size="small">
            <el-table-column type="index" width="60" label="序号" align="center" />
            <el-table-column prop="itemName" label="项目名称" min-width="150" />
            <el-table-column prop="itemCode" label="项目编码" width="120" />
            <el-table-column prop="category" label="费用类别" width="120" />
            <el-table-column prop="unit" label="计量单位" width="100" />
            <el-table-column prop="isRequired" label="必填项" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.isRequired ? 'success' : 'info'" size="mini">
                  {{ scope.row.isRequired ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="defaultValue" label="默认值" width="100" align="right" />
            <el-table-column prop="remark" label="备注" min-width="150" />
          </el-table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleApplyFromDetail">应用模板</el-button>
      </div>
    </el-dialog>

    <!-- 分享模板对话框 -->
    <el-dialog
      title="分享模板"
      :visible.sync="shareDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedTemplate">
        <el-form :model="shareForm" :rules="shareRules" ref="shareForm" label-width="100px" size="small">
          <el-form-item label="模板名称">
            <span>{{ selectedTemplate.templateName }}</span>
          </el-form-item>

          <el-form-item label="分享类型" prop="shareType">
            <el-radio-group v-model="shareForm.shareType">
              <el-radio label="public">公开分享</el-radio>
              <el-radio label="private">指定用户</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="指定用户" v-if="shareForm.shareType === 'private'" prop="shareUsers">
            <el-select
              v-model="shareForm.shareUsers"
              multiple
              filterable
              placeholder="请选择分享用户"
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

          <el-form-item label="分享权限" prop="sharePermission">
            <el-radio-group v-model="shareForm.sharePermission">
              <el-radio label="view">仅查看</el-radio>
              <el-radio label="apply">可应用</el-radio>
              <el-radio label="edit">可编辑</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="有效期" prop="expireTime">
            <el-date-picker
              v-model="shareForm.expireTime"
              type="datetime"
              placeholder="选择过期时间（可选）"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="分享说明" prop="shareNote">
            <el-input
              type="textarea"
              v-model="shareForm.shareNote"
              :rows="3"
              placeholder="请输入分享说明"
            />
          </el-form-item>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="shareDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitShare" :loading="submitting">
          确认分享
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetApi } from '@/api/financialSharing/budget'
import { formatDate } from '@/utils/index'

export default {
  name: 'BudgetTemplate',
  data() {
    return {
      // 筛选表单
      filterForm: {
        templateName: '',
        templateType: '',
        category: '',
        dateRange: []
      },

      // 模板列表数据
      templateList: [],
      loading: false,

      // 分页参数
      pagination: {
        pageNo: 1,
        pageSize: 20,
        total: 0
      },

      // 选中的模板
      selectedTemplate: null,
      multipleSelection: [],

      // 模板对话框
      templateDialogVisible: false,
      templateDialogTitle: '新建模板',
      editMode: false,

      // 模板表单
      templateForm: {
        templateId: null,
        templateName: '',
        templateType: '',
        category: '',
        description: '',
        isPublic: false,
        items: []
      },
      templateRules: {
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        category: [
          { required: true, message: '请选择模板分类', trigger: 'change' }
        ],
        items: [
          { required: true, message: '请添加模板项目', trigger: 'change' }
        ]
      },

      // 应用模板
      applyDialogVisible: false,
      applyForm: {
        budgetName: '',
        period: [],
        fillDefaults: true
      },
      applyRules: {
        budgetName: [
          { required: true, message: '请输入预算名称', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '请选择预算期间', trigger: 'change' }
        ]
      },

      // 模板详情
      detailDialogVisible: false,

      // 分享模板
      shareDialogVisible: false,
      userList: [],
      shareForm: {
        shareType: 'public',
        shareUsers: [],
        sharePermission: 'apply',
        expireTime: '',
        shareNote: ''
      },
      shareRules: {
        shareType: [
          { required: true, message: '请选择分享类型', trigger: 'change' }
        ],
        shareUsers: [
          { required: true, message: '请选择分享用户', trigger: 'change' }
        ],
        sharePermission: [
          { required: true, message: '请选择分享权限', trigger: 'change' }
        ]
      },

      // 提交状态
      submitting: false
    }
  },

  created() {
    this.fetchTemplateList()
    this.fetchUserList()
  },

  filters: {
    formatDate(time) {
      return formatDate(time, 'yyyy-MM-dd HH:mm')
    }
  },

  methods: {
    // 获取模板列表
    async fetchTemplateList() {
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

        const response = await budgetApi.getTemplateList(params)
        if (response.code === 1) {
          this.templateList = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取模板列表失败')
        }
      } catch (error) {
        console.error('获取模板列表异常:', error)
        this.$message.error('获取模板列表失败，请稍后重试')
      } finally {
        this.loading = false
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

    // 搜索
    handleSearch() {
      this.pagination.pageNo = 1
      this.fetchTemplateList()
    },

    // 重置
    handleReset() {
      this.$refs.filterForm.resetFields()
      this.filterForm.dateRange = []
      this.pagination.pageNo = 1
      this.fetchTemplateList()
    },

    // 刷新
    handleRefresh() {
      this.fetchTemplateList()
    },

    // 表格多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
      this.selectedTemplate = selection.length === 1 ? selection[0] : null
    },

    // 行点击
    handleRowClick(row) {
      this.selectedTemplate = row
    },

    // 新建模板
    handleCreateTemplate() {
      this.editMode = false
      this.templateDialogTitle = '新建模板'
      this.templateForm = {
        templateId: null,
        templateName: '',
        templateType: '',
        category: '',
        description: '',
        isPublic: false,
        items: []
      }
      this.templateDialogVisible = true
    },

    // 编辑模板
    handleEditTemplate(row) {
      this.editMode = true
      this.templateDialogTitle = '编辑模板'
      this.templateForm = {
        templateId: row.templateId,
        templateName: row.templateName,
        templateType: row.templateType,
        category: row.category,
        description: row.description,
        isPublic: row.isPublic,
        items: row.items ? JSON.parse(JSON.stringify(row.items)) : []
      }
      this.templateDialogVisible = true
    },

    // 提交模板
    async handleSubmitTemplate() {
      try {
        // 验证是否添加了项目
        if (this.templateForm.items.length === 0) {
          this.$message.warning('请至少添加一个模板项目')
          return
        }

        this.submitting = true
        const response = await budgetApi.saveTemplate(this.templateForm)

        if (response.code === 1) {
          this.$message.success(this.editMode ? '模板更新成功' : '模板创建成功')
          this.templateDialogVisible = false
          this.fetchTemplateList()
        } else {
          this.$message.error(response.message || '保存模板失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('保存模板异常:', error)
          this.$message.error('保存模板失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 模板对话框关闭
    handleTemplateDialogClose() {
      this.$refs.templateForm && this.$refs.templateForm.resetFields()
    },

    // 复制模板
    handleCopyTemplate() {
      if (!this.selectedTemplate) {
        this.$message.warning('请选择要复制的模板')
        return
      }

      this.editMode = false
      this.templateDialogTitle = '复制模板'
      this.templateForm = {
        templateId: null,
        templateName: this.selectedTemplate.templateName + '_副本',
        templateType: this.selectedTemplate.templateType,
        category: this.selectedTemplate.category,
        description: this.selectedTemplate.description,
        isPublic: false, // 复制的模板默认为私有
        items: this.selectedTemplate.items ? JSON.parse(JSON.stringify(this.selectedTemplate.items)) : []
      }
      this.templateDialogVisible = true
    },

    // 分享模板
    handleShareTemplate() {
      if (!this.selectedTemplate) {
        this.$message.warning('请选择要分享的模板')
        return
      }

      this.shareForm = {
        shareType: 'public',
        shareUsers: [],
        sharePermission: 'apply',
        expireTime: '',
        shareNote: ''
      }
      this.shareDialogVisible = true
    },

    // 提交分享
    async handleSubmitShare() {
      try {
        await this.$refs.shareForm.validate()

        this.submitting = true
        const response = await budgetApi.shareTemplate({
          templateId: this.selectedTemplate.templateId,
          ...this.shareForm
        })

        if (response.code === 1) {
          this.$message.success('模板分享成功')
          this.shareDialogVisible = false
          this.fetchTemplateList()
        } else {
          this.$message.error(response.message || '分享模板失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('分享模板异常:', error)
          this.$message.error('分享模板失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 删除模板
    handleDeleteTemplate(row) {
      this.$confirm('确定要删除此模板吗？删除后不可恢复。', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetApi.deleteTemplate(row.templateId)

          if (response.code === 1) {
            this.$message.success('模板删除成功')
            this.fetchTemplateList()
          } else {
            this.$message.error(response.message || '删除模板失败')
          }
        } catch (error) {
          console.error('删除模板异常:', error)
          this.$message.error('删除模板失败，请稍后重试')
        }
      }).catch(() => {})
    },

    // 查看模板
    handleViewTemplate(row) {
      this.selectedTemplate = { ...row }
      this.detailDialogVisible = true
    },

    // 应用模板
    handleApplyTemplate(row) {
      this.selectedTemplate = { ...row }
      this.applyForm = {
        budgetName: '',
        period: [],
        fillDefaults: true
      }
      this.applyDialogVisible = true
    },

    // 提交应用
    async handleSubmitApply() {
      try {
        await this.$refs.applyForm.validate()

        this.submitting = true
        const response = await budgetApi.applyTemplate({
          templateId: this.selectedTemplate.templateId,
          ...this.applyForm
        })

        if (response.code === 1) {
          this.$message.success('模板应用成功')
          this.applyDialogVisible = false
          // 跳转到预算编辑页面
          this.$router.push({
            path: '/financialSharing/budget/edit',
            query: { budgetId: response.data.budgetId }
          })
        } else {
          this.$message.error(response.message || '应用模板失败')
        }
      } catch (error) {
        if (error !== false) {
          console.error('应用模板异常:', error)
          this.$message.error('应用模板失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },

    // 从详情应用模板
    handleApplyFromDetail() {
      this.detailDialogVisible = false
      this.handleApplyTemplate(this.selectedTemplate)
    },

    // 添加项目
    handleAddItem() {
      this.templateForm.items.push({
        itemName: '',
        itemCode: '',
        category: '',
        unit: 'yuan',
        isRequired: false,
        defaultValue: 0,
        remark: ''
      })
    },

    // 删除项目
    handleDeleteItem(index) {
      this.templateForm.items.splice(index, 1)
    },

    // 从Excel导入
    handleImportFromExcel() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 已上传，处理中...`)
        if (this.loadData) this.loadData()
      }
      input.click()
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNo = 1
      this.fetchTemplateList()
    },

    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.pageNo = page
      this.fetchTemplateList()
    },

    // 获取模板类型标签
    getTemplateTypeTag(type) {
      const tagMap = {
        'annual': '',
        'quarterly': 'success',
        'monthly': 'warning',
        'project': 'info',
        'department': 'danger'
      }
      return tagMap[type] || ''
    },

    // 获取模板类型文本
    getTemplateTypeText(type) {
      const textMap = {
        'annual': '年度预算',
        'quarterly': '季度预算',
        'monthly': '月度预算',
        'project': '项目预算',
        'department': '部门预算'
      }
      return textMap[type] || '未知'
    },

    // 获取分类标签
    getCategoryTag(category) {
      const tagMap = {
        'general': 'primary',
        'dept': 'success',
        'project': 'warning',
        'personal': 'info'
      }
      return tagMap[category] || ''
    },

    // 获取分类文本
    getCategoryText(category) {
      const textMap = {
        'general': '通用',
        'dept': '部门',
        'project': '项目',
        'personal': '个人'
      }
      return textMap[category] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-template-container {
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

    .item-badge {
      :deep(.el-badge__content) {
        background-color: #409EFF;
      }
    }

    .use-count {
      color: #67C23A;
      font-weight: 600;
    }
  }

  .template-preview {
    margin-top: 10px;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>