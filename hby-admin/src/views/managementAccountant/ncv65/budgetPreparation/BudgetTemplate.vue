<template>
  <div class="budget-template">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算模板管理</h2>
      <p>管理预算编制模板，支持模板创建、编辑、复制、导入导出等操作</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateTemplate">创建模板</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportTemplate">导入模板</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportTemplate">导出模板</el-button>
            <el-button type="info" icon="el-icon-document-copy" @click="handleBatchCopy">批量复制</el-button>
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

    <!-- 模板分类 -->
    <el-card class="category-card" shadow="never">
      <div class="category-header">
        <h3>模板分类</h3>
        <el-button type="text" @click="handleCategoryManagement">分类管理</el-button>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="4">
          <div class="category-tree">
            <el-tree
              :data="categoryTree"
              :props="{ children: 'children', label: 'name' }"
              node-key="id"
              :current-node-key="currentCategoryId"
              @node-click="handleCategoryClick"
              :expand-on-click-node="false"
            >
              <span class="tree-node" slot-scope="{ node, data }">
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">({{ data.templateCount || 0 }})</span>
              </span>
            </el-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <!-- 查询条件 -->
          <div class="search-form">
            <el-form :model="queryForm" :inline="true" size="small">
              <el-form-item label="模板名称">
                <el-input
                  v-model="queryForm.templateName"
                  placeholder="请输入模板名称"
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="模板类型">
                <el-select
                  v-model="queryForm.templateType"
                  placeholder="请选择模板类型"
                  clearable
                  style="width: 150px"
                >
                  <el-option
                    v-for="item in templateTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="模板状态">
                <el-select
                  v-model="queryForm.templateStatus"
                  placeholder="请选择模板状态"
                  clearable
                  style="width: 150px"
                >
                  <el-option
                    v-for="item in templateStatusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
                <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 模板列表 -->
          <div class="template-grid">
            <el-row :gutter="20">
              <el-col :span="6" v-for="template in templateList" :key="template.id">
                <el-card class="template-card" shadow="hover" @click.native="handleViewTemplate(template)">
                  <div class="template-header">
                    <div class="template-icon">
                      <i :class="getTemplateIcon(template.templateType)"></i>
                    </div>
                    <div class="template-actions">
                      <el-dropdown trigger="click" @command="(command) => handleTemplateCommand(command, template)" @click.native.stop>
                        <el-button type="text" size="mini" icon="el-icon-more" />
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
                          <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                          <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                          <el-dropdown-item command="preview" icon="el-icon-view">预览</el-dropdown-item>
                          <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                    </div>
                  </div>
                  
                  <div class="template-content">
                    <h4 class="template-name">{{ template.templateName }}</h4>
                    <p class="template-description">{{ template.templateDescription || '暂无描述' }}</p>
                    
                    <div class="template-meta">
                      <div class="meta-item">
                        <el-tag :type="getTemplateTypeColor(template.templateType)" size="mini">
                          {{ getTemplateTypeText(template.templateType) }}
                        </el-tag>
                      </div>
                      <div class="meta-item">
                        <el-tag :type="getTemplateStatusType(template.templateStatus)" size="mini">
                          {{ getTemplateStatusText(template.templateStatus) }}
                        </el-tag>
                      </div>
                    </div>
                    
                    <div class="template-stats">
                      <div class="stat-item">
                        <i class="el-icon-document"></i>
                        <span>{{ template.fieldCount || 0 }} 字段</span>
                      </div>
                      <div class="stat-item">
                        <i class="el-icon-s-data"></i>
                        <span>{{ template.usageCount || 0 }} 次使用</span>
                      </div>
                    </div>
                    
                    <div class="template-footer">
                      <div class="creator-info">
                        <el-avatar :size="20" :src="template.creatorAvatar">
                          {{ template.creator ? template.creator.charAt(0) : 'U' }}
                        </el-avatar>
                        <span class="creator-name">{{ template.creator }}</span>
                      </div>
                      <div class="create-time">
                        {{ formatDate(template.createTime) }}
                      </div>
                    </div>
                    <!-- 操作按钮区 -->
                    <div class="template-action-bar" @click.stop>
                      <el-button
                        type="primary"
                        size="mini"
                        icon="el-icon-edit"
                        plain
                        @click.stop="handleEditTemplate(template)"
                      >编辑</el-button>
                      <el-button
                        type="info"
                        size="mini"
                        icon="el-icon-view"
                        plain
                        @click.stop="handlePreviewSingle(template)"
                      >预览</el-button>
                      <el-button
                        type="success"
                        size="mini"
                        icon="el-icon-document-copy"
                        plain
                        @click.stop="handleCopyTemplate(template)"
                      >复制</el-button>
                      <el-button
                        type="danger"
                        size="mini"
                        icon="el-icon-delete"
                        plain
                        @click.stop="handleDeleteTemplate(template)"
                      >删除</el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <!-- 空状态 -->
            <div v-if="templateList.length === 0" class="empty-state">
              <el-empty description="暂无模板数据">
                <el-button type="primary" @click="handleCreateTemplate">创建第一个模板</el-button>
              </el-empty>
            </div>
          </div>

          <!-- 分页组件 -->
          <div class="pagination-container">
            <el-pagination
              :current-page="queryParams.pageNum"
              :page-sizes="[12, 24, 48, 96]"
              :page-size="queryParams.pageSize"
              :total="total"
              background
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 新增/编辑模板对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
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
                <el-option
                  v-for="item in templateTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-cascader
                v-model="templateForm.categoryId"
                :options="categoryOptions"
                :props="{ checkStrictly: true, value: 'id', label: 'name' }"
                placeholder="请选择所属分类"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用年度" prop="applicableYear">
              <el-date-picker
                v-model="templateForm.applicableYear"
                type="year"
                placeholder="选择适用年度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="模板描述" prop="templateDescription">
          <el-input
            v-model="templateForm.templateDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        
        <!-- 模板字段配置 -->
        <el-form-item label="字段配置" prop="templateFields">
          <div class="template-fields">
            <div class="fields-header">
              <el-button type="primary" size="mini" @click="handleAddField">添加字段</el-button>
              <el-button type="success" size="mini" @click="handleImportFields">导入字段</el-button>
              <el-button type="warning" size="mini" @click="handlePreviewTemplate">预览模板</el-button>
            </div>
            
            <el-table
              :data="templateForm.templateFields"
              border
              size="mini"
              max-height="400"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="字段名称" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.fieldName"
                    placeholder="字段名称"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="字段标签" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.fieldLabel"
                    placeholder="字段标签"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="字段类型" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.fieldType"
                    placeholder="字段类型"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option
                      v-for="type in fieldTypeOptions"
                      :key="type.value"
                      :label="type.label"
                      :value="type.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="是否必填" width="100" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.required"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="默认值" width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.defaultValue"
                    placeholder="默认值"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="验证规则" width="150">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.validationRule"
                    placeholder="验证规则"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="排序" width="80">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.sortOrder"
                    :min="1"
                    size="mini"
                    controls-position="right"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="备注" min-width="120">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remark"
                    placeholder="备注"
                    size="mini"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-arrow-up"
                    @click="handleMoveFieldUp(scope.$index)"
                    :disabled="scope.$index === 0"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-arrow-down"
                    @click="handleMoveFieldDown(scope.$index)"
                    :disabled="scope.$index === templateForm.templateFields.length - 1"
                  />
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveField(scope.$index)"
                  />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <el-form-item label="模板设置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="templateForm.isPublic">公开模板</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="templateForm.allowCopy">允许复制</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="templateForm.isDefault">默认模板</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveTemplate">保存模板</el-button>
        <el-button type="primary" @click="handlePublishTemplate">发布模板</el-button>
      </div>
    </el-dialog>

    <!-- 模板预览对话框 -->
    <el-dialog
      title="模板预览"
      :visible.sync="previewDialogVisible"
      width="80%"
      :close-on-click-modal="false"
    >
      <div class="template-preview">
        <div class="preview-header">
          <h3>{{ previewTemplate.templateName }}</h3>
          <p>{{ previewTemplate.templateDescription }}</p>
        </div>
        
        <div class="preview-content">
          <el-form
            :model="previewData"
            label-width="120px"
            size="small"
          >
            <el-form-item
              v-for="field in previewTemplate.templateFields"
              :key="field.fieldName"
              :label="field.fieldLabel"
              :required="field.required"
            >
              <!-- 根据字段类型渲染不同的组件 -->
              <el-input
                v-if="field.fieldType === 'TEXT'"
                v-model="previewData[field.fieldName]"
                :placeholder="field.defaultValue"
              />
              <el-input-number
                v-else-if="field.fieldType === 'NUMBER'"
                v-model="previewData[field.fieldName]"
                :placeholder="field.defaultValue"
                style="width: 100%"
              />
              <el-date-picker
                v-else-if="field.fieldType === 'DATE'"
                v-model="previewData[field.fieldName]"
                type="date"
                :placeholder="field.defaultValue"
                style="width: 100%"
              />
              <el-select
                v-else-if="field.fieldType === 'SELECT'"
                v-model="previewData[field.fieldName]"
                :placeholder="field.defaultValue"
                style="width: 100%"
              >
                <el-option label="选项1" value="option1" />
                <el-option label="选项2" value="option2" />
              </el-select>
              <el-input
                v-else-if="field.fieldType === 'TEXTAREA'"
                v-model="previewData[field.fieldName]"
                type="textarea"
                :rows="3"
                :placeholder="field.defaultValue"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleUseTemplate">使用此模板</el-button>
      </div>
    </el-dialog>

    <!-- 导入模板对话框 -->
    <el-dialog
      title="导入模板"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="import-content">
        <el-upload
          ref="uploadTemplate"
          :action="uploadUrl"
          :headers="uploadHeaders"
          :on-success="handleImportSuccess"
          :on-error="handleImportError"
          :before-upload="handleBeforeUpload"
          :file-list="importFileList"
          accept=".xlsx,.xls,.json"
          drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            支持 .xlsx、.xls、.json 格式文件，且不超过10MB
          </div>
        </el-upload>
        
        <div class="import-options">
          <el-form label-width="120px" size="small">
            <el-form-item label="导入模式">
              <el-radio-group v-model="importMode">
                <el-radio label="CREATE">创建新模板</el-radio>
                <el-radio label="UPDATE">更新现有模板</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="冲突处理">
              <el-radio-group v-model="conflictMode">
                <el-radio label="SKIP">跳过</el-radio>
                <el-radio label="OVERWRITE">覆盖</el-radio>
                <el-radio label="RENAME">重命名</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmImport">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 模板设置对话框 -->
    <el-dialog title="模板设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="默认模板类型">
          <el-select v-model="settingsForm.defaultType" placeholder="请选择" style="width: 100%">
            <el-option label="预算表单" value="BUDGET_FORM" />
            <el-option label="报表模板" value="REPORT_TEMPLATE" />
            <el-option label="分析模板" value="ANALYSIS_TEMPLATE" />
          </el-select>
        </el-form-item>
        <el-form-item label="允许自定义字段">
          <el-switch v-model="settingsForm.allowCustomFields" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 分类管理对话框 -->
    <el-dialog title="模板分类管理" :visible.sync="categoryMgmtDialogVisible" width="600px" :close-on-click-modal="false">
      <el-tree :data="categoryTree" :props="{ label: 'name', children: 'children' }" default-expand-all />
      <div slot="footer">
        <el-button @click="categoryMgmtDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetTemplateApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetTemplate',
  data() {
    return {
      // 查询参数
      queryForm: {
        templateName: '',
        templateType: '',
        templateStatus: '',
        categoryId: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 12
      },
      
      // 表格数据
      loading: false,
      templateList: [],
      total: 0,
      
      // 分类相关
      categoryTree: [],
      currentCategoryId: null,
      categoryOptions: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      templateForm: {
        templateId: null,
        templateName: '',
        templateType: '',
        categoryId: null,
        applicableYear: null,
        templateDescription: '',
        templateFields: [],
        isPublic: false,
        allowCopy: true,
        isDefault: false
      },
      templateRules: {
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        categoryId: [
          { required: true, message: '请选择所属分类', trigger: 'change' }
        ]
      },
      
      // 预览对话框
      previewDialogVisible: false,
      previewTemplate: {},
      previewData: {},
      
      // 导入对话框
      importDialogVisible: false,
      importFileList: [],
      importMode: 'CREATE',
      conflictMode: 'SKIP',
      uploadUrl: process.env.VUE_APP_BASE_API + '/accountant/ncv65/budget-template/import',
      uploadHeaders: {
        Authorization: 'Bearer ' + this.$store.getters.token
      },
      
      // 选项数据
      templateTypeOptions: [
        { value: 'BUDGET_FORM', label: '预算表单' },
        { value: 'REPORT_TEMPLATE', label: '报表模板' },
        { value: 'ANALYSIS_TEMPLATE', label: '分析模板' },
        { value: 'IMPORT_TEMPLATE', label: '导入模板' }
      ],
      templateStatusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'PUBLISHED', label: '已发布' },
        { value: 'ARCHIVED', label: '已归档' }
      ],
      fieldTypeOptions: [
        { value: 'TEXT', label: '文本' },
        { value: 'NUMBER', label: '数字' },
        { value: 'DATE', label: '日期' },
        { value: 'SELECT', label: '下拉选择' },
        { value: 'TEXTAREA', label: '多行文本' },
        { value: 'CHECKBOX', label: '复选框' },
        { value: 'RADIO', label: '单选框' }
      ],

      settingsDialogVisible: false,
      settingsForm: { defaultType: 'BUDGET_FORM', allowCustomFields: true },

      categoryMgmtDialogVisible: false
    }
  },
  
  created() {
    this.getList()
    this.loadCategoryTree()
    this.loadCategoryOptions()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = { ...this.queryParams }
        if (this.queryForm.templateName) params.templateName = this.queryForm.templateName
        if (this.queryForm.templateType) params.templateType = this.queryForm.templateType
        if (this.queryForm.templateStatus) params.templateStatus = this.queryForm.templateStatus
        if (this.currentCategoryId) params.categoryId = this.currentCategoryId
        const response = await budgetTemplateApi.getPage(params)
        this.templateList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载分类树
    async loadCategoryTree() {
      try {
        const response = await budgetTemplateApi.getCategoryTree()
        this.categoryTree = response.data
      } catch (error) {
        console.error('加载分类树失败：', error)
      }
    },
    
    // 加载分类选项
    async loadCategoryOptions() {
      try {
        const response = await budgetTemplateApi.getCategories()
        this.categoryOptions = response.data
      } catch (error) {
        console.error('加载分类选项失败：', error)
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
        templateName: '',
        templateType: '',
        templateStatus: ''
      }
      this.currentCategoryId = null
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 分类点击
    handleCategoryClick(data) {
      this.currentCategoryId = data.id
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 创建模板
    handleCreateTemplate() {
      this.dialogTitle = '创建预算模板'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 查看模板
    handleViewTemplate(template) {
      this.handlePreviewSingle(template)
    },
    
    // 模板操作
    async handleTemplateCommand(command, template) {
      switch (command) {
        case 'edit':
          this.handleEditTemplate(template)
          break
        case 'copy':
          this.handleCopyTemplate(template)
          break
        case 'export':
          this.handleExportSingle(template)
          break
        case 'preview':
          this.handlePreviewSingle(template)
          break
        case 'delete':
          this.handleDeleteTemplate(template)
          break
      }
    },
    
    // 编辑模板
    handleEditTemplate(template) {
      this.dialogTitle = '编辑预算模板'
      this.dialogVisible = true
      // 从 templateConfig JSON 字符串中解析出 templateFields 数组
      let fields = []
      if (template.templateConfig) {
        try {
          const config = JSON.parse(template.templateConfig)
          fields = Array.isArray(config.fields) ? config.fields : (Array.isArray(config) ? config : [])
        } catch (e) {
          fields = []
        }
      }
      this.templateForm = {
        ...template,
        templateFields: fields,
        isPublic: !!template.isPublic,
        allowCopy: template.allowCopy !== 0,
        isDefault: !!template.isDefault
      }
    },
    
    // 复制模板（调用后端复制接口，直接生成副本，不走弹窗）
    async handleCopyTemplate(template) {
      try {
        await this.$confirm(`确认复制模板「${template.templateName}」吗？`, '提示', { type: 'info' })
        const response = await budgetTemplateApi.copy(template.templateId)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.getList()
        } else {
          this.$message.error('复制失败：' + response.msg)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复制失败：' + (error.message || error))
        }
      }
    },
    
    // 预览单个模板
    handlePreviewSingle(template) {
      this.previewTemplate = template
      this.previewData = {}
      this.previewDialogVisible = true
    },
    
    // 删除模板
    async handleDeleteTemplate(template) {
      try {
        await this.$confirm('确认删除该模板吗？删除后不可恢复。', '提示', {
          type: 'warning'
        })
        const response = await budgetTemplateApi.delete(template.templateId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error('删除失败：' + response.msg)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + (error.message || error))
        }
      }
    },
    
    // 导出单个模板
    async handleExportSingle(template) {
      try {
        const response = await budgetTemplateApi.exportSingle(template.templateId)
        const blob = new Blob([response], { type: 'application/json;charset=UTF-8' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = (template.templateName || template.templateId) + '.json'
        link.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 添加字段
    handleAddField() {
      this.templateForm.templateFields.push({
        fieldName: '',
        fieldLabel: '',
        fieldType: 'TEXT',
        required: false,
        defaultValue: '',
        validationRule: '',
        sortOrder: this.templateForm.templateFields.length + 1,
        remark: ''
      })
    },
    
    // 删除字段
    handleRemoveField(index) {
      this.templateForm.templateFields.splice(index, 1)
      // 重新排序
      this.templateForm.templateFields.forEach((field, idx) => {
        field.sortOrder = idx + 1
      })
    },
    
    // 上移字段
    handleMoveFieldUp(index) {
      if (index > 0) {
        const temp = this.templateForm.templateFields[index]
        this.$set(this.templateForm.templateFields, index, this.templateForm.templateFields[index - 1])
        this.$set(this.templateForm.templateFields, index - 1, temp)
        
        // 更新排序
        this.templateForm.templateFields[index].sortOrder = index + 1
        this.templateForm.templateFields[index - 1].sortOrder = index
      }
    },
    
    // 下移字段
    handleMoveFieldDown(index) {
      if (index < this.templateForm.templateFields.length - 1) {
        const temp = this.templateForm.templateFields[index]
        this.$set(this.templateForm.templateFields, index, this.templateForm.templateFields[index + 1])
        this.$set(this.templateForm.templateFields, index + 1, temp)
        
        // 更新排序
        this.templateForm.templateFields[index].sortOrder = index + 1
        this.templateForm.templateFields[index + 1].sortOrder = index + 2
      }
    },
    
    // 导入字段
    handleImportFields() {
      this.$message.info('字段导入功能开发中...')
    },
    
    // 预览模板
    handlePreviewTemplate() {
      if (this.templateForm.templateFields.length === 0) {
        this.$message.warning('请先添加字段')
        return
      }
      
      this.previewTemplate = { ...this.templateForm }
      this.previewData = {}
      this.previewDialogVisible = true
    },
    
    // 使用模板
    handleUseTemplate() {
      this.$message.success('已应用模板: ' + (this.previewTemplate.templateName || ''))
      this.previewDialogVisible = false
    },
    
    // 保存模板
    async handleSaveTemplate() {
      try {
        await this.$refs.templateForm.validate()

        if (this.templateForm.templateFields.length === 0) {
          this.$message.warning('请添加至少一个字段')
          return
        }

        const params = this.buildSubmitParams('DRAFT')

        if (this.templateForm.templateId) {
          await budgetTemplateApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetTemplateApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 发布模板
    async handlePublishTemplate() {
      try {
        await this.$refs.templateForm.validate()

        if (this.templateForm.templateFields.length === 0) {
          this.$message.warning('请添加至少一个字段')
          return
        }

        const params = this.buildSubmitParams('PUBLISHED')

        if (this.templateForm.templateId) {
          await budgetTemplateApi.update(params)
          this.$message.success('发布成功')
        } else {
          await budgetTemplateApi.create(params)
          this.$message.success('创建并发布成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('发布失败：' + error.message)
      }
    },

    // 构建提交参数（templateFields 序列化为 templateConfig；Boolean 转 Integer）
    buildSubmitParams(status) {
      const { id, templateFields, isPublic, allowCopy, isDefault, ...rest } = this.templateForm
      return {
        ...rest,
        templateId: this.templateForm.templateId || null,
        templateStatus: status,
        isPublic: isPublic ? 1 : 0,
        allowCopy: allowCopy ? 1 : 0,
        isDefault: isDefault ? 1 : 0,
        fieldCount: (templateFields || []).length,
        templateConfig: JSON.stringify({ fields: templateFields || [] })
      }
    },
    
    // 重置表单
    resetForm() {
      this.templateForm = {
        templateId: null,
        templateName: '',
        templateType: '',
        categoryId: null,
        applicableYear: null,
        templateDescription: '',
        templateFields: [],
        isPublic: false,
        allowCopy: true,
        isDefault: false
      }
      this.$nextTick(() => {
        this.$refs.templateForm && this.$refs.templateForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入模板
    handleImportTemplate() {
      this.importDialogVisible = true
      this.importFileList = []
    },
    
    // 上传前检查
    handleBeforeUpload(file) {
      const isValidType = ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 
                          'application/vnd.ms-excel', 
                          'application/json'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10
      
      if (!isValidType) {
        this.$message.error('只能上传 Excel 或 JSON 文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        return false
      }
      return true
    },
    
    // 导入成功
    handleImportSuccess(response, file) {
      if (response.code === 1) {
        this.$message.success('导入成功')
        this.importDialogVisible = false
        this.getList()
      } else {
        this.$message.error('导入失败：' + response.message)
      }
    },
    
    // 导入失败
    handleImportError(error) {
      this.$message.error('导入失败：' + error.message)
    },
    
    // 确认导入
    handleConfirmImport() {
      if (this.importFileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      
      this.$refs.uploadTemplate.submit()
    },
    
    // 导出模板
    async handleExportTemplate() {
      try {
        const params = { ...this.queryForm, ...this.queryParams }
        if (this.currentCategoryId) params.categoryId = this.currentCategoryId
        const response = await budgetTemplateApi.export(params)
        const blob = new Blob([response], { type: 'application/json;charset=UTF-8' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算模板列表.json'
        link.click()
        URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 批量复制
    handleBatchCopy() {
      this.$message.info('批量复制功能开发中...')
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.loadCategoryTree()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 分类管理
    handleCategoryManagement() {
      this.categoryMgmtDialogVisible = true
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
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleDateString()
    },
    
    // 获取模板图标
    getTemplateIcon(type) {
      const iconMap = {
        'BUDGET_FORM': 'el-icon-document',
        'REPORT_TEMPLATE': 'el-icon-s-data',
        'ANALYSIS_TEMPLATE': 'el-icon-pie-chart',
        'IMPORT_TEMPLATE': 'el-icon-upload2'
      }
      return iconMap[type] || 'el-icon-document'
    },
    
    // 获取模板类型颜色
    getTemplateTypeColor(type) {
      const colorMap = {
        'BUDGET_FORM': 'primary',
        'REPORT_TEMPLATE': 'success',
        'ANALYSIS_TEMPLATE': 'warning',
        'IMPORT_TEMPLATE': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取模板类型文本
    getTemplateTypeText(type) {
      const item = this.templateTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取模板状态类型
    getTemplateStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'PUBLISHED': 'success',
        'ARCHIVED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取模板状态文本
    getTemplateStatusText(status) {
      const item = this.templateStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-template {
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
  .category-card {
    margin-bottom: 20px;
  }
  
  .category-card {
    .category-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        margin: 0;
      }
    }
    
    .category-tree {
      border-right: 1px solid #EBEEF5;
      padding-right: 20px;
      
      .tree-node {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        
        .node-count {
          color: #909399;
          font-size: 12px;
        }
      }
    }
    
    .search-form {
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #EBEEF5;
    }
  }
  
  .template-grid {
    min-height: 400px;
    
    .template-card {
      margin-bottom: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }
      
      .template-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        
        .template-icon {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, #409EFF, #36CFC9);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          
          i {
            font-size: 20px;
            color: white;
          }
        }
      }
      
      .template-content {
        .template-name {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin: 0 0 8px 0;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .template-description {
          font-size: 14px;
          color: #606266;
          margin: 0 0 12px 0;
          height: 40px;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        
        .template-meta {
          display: flex;
          gap: 8px;
          margin-bottom: 12px;
        }
        
        .template-stats {
          display: flex;
          justify-content: space-between;
          margin-bottom: 12px;
          
          .stat-item {
            display: flex;
            align-items: center;
            font-size: 12px;
            color: #909399;
            
            i {
              margin-right: 4px;
            }
          }
        }
        
        .template-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-top: 12px;
          border-top: 1px solid #F5F7FA;
          
          .creator-info {
            display: flex;
            align-items: center;
            
            .creator-name {
              margin-left: 8px;
              font-size: 12px;
              color: #606266;
            }
          }
          
          .create-time {
            font-size: 12px;
            color: #909399;
          }
        }

        .template-action-bar {
          display: flex;
          gap: 6px;
          margin-top: 10px;
          padding-top: 10px;
          border-top: 1px solid #F5F7FA;
          flex-wrap: wrap;

          .el-button {
            flex: 1;
            min-width: 0;
            padding: 5px 6px;
            font-size: 12px;
          }
        }
      }
    }
  }
  
  .empty-state {
    text-align: center;
    padding: 60px 0;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: center;
  }
  
  .template-fields {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .fields-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .template-preview {
    .preview-header {
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #EBEEF5;
      
      h3 {
        color: #303133;
        margin: 0 0 8px 0;
      }
      
      p {
        color: #606266;
        margin: 0;
      }
    }
  }
  
  .import-content {
    .import-options {
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #EBEEF5;
    }
  }
  
  .danger-text {
    color: #F56C6C;
  }
}
</style>
