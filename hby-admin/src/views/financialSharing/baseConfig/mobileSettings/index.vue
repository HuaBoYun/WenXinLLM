<template>
  <div class="mobile-settings-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="设置名称" prop="settingName">
          <el-input
            v-model="searchForm.settingName"
            placeholder="请输入设置名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="设置类型" prop="settingType">
          <el-select
            v-model="searchForm.settingType"
            placeholder="请选择设置类型"
            clearable
            style="width: 150px"
          >
            <el-option label="界面设置" value="UI" />
            <el-option label="功能设置" value="FUNCTION" />
            <el-option label="权限设置" value="PERMISSION" />
            <el-option label="推送设置" value="NOTIFICATION" />
            <el-option label="安全设置" value="SECURITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="success" @click="handleSync" :loading="syncLoading">同步配置</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="settingCode" label="设置编码" width="150" />
        <el-table-column prop="settingName" label="设置名称" min-width="200" />
        <el-table-column prop="settingTypeName" label="设置类型" width="120" />
        <el-table-column prop="settingValue" label="设置值" min-width="180" show-overflow-tooltip />
        <el-table-column prop="platform" label="适用平台" width="120">
          <template slot-scope="scope">
            <el-tag size="small" :type="getPlatformColor(scope.row.platform)">
              {{ scope.row.platformName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="100" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastSyncTime" label="最后同步时间" width="160" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="400" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleTemplates(scope.row)">模板配置</el-button>
            <el-button size="mini" type="success" @click="handleTest(scope.row)">测试</el-button>
            <el-button size="mini" type="warning" @click="handleSyncSingle(scope.row)">同步</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设置编码" prop="settingCode">
              <el-input v-model="formData.settingCode" placeholder="请输入设置编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设置名称" prop="settingName">
              <el-input v-model="formData.settingName" placeholder="请输入设置名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设置类型" prop="settingType">
              <el-select v-model="formData.settingType" placeholder="请选择设置类型" style="width: 100%">
                <el-option label="界面设置" value="UI" />
                <el-option label="功能设置" value="FUNCTION" />
                <el-option label="权限设置" value="PERMISSION" />
                <el-option label="推送设置" value="NOTIFICATION" />
                <el-option label="安全设置" value="SECURITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用平台" prop="platform">
              <el-select v-model="formData.platform" placeholder="请选择适用平台" style="width: 100%">
                <el-option label="iOS" value="IOS" />
                <el-option label="Android" value="ANDROID" />
                <el-option label="微信小程序" value="WECHAT" />
                <el-option label="支付宝小程序" value="ALIPAY" />
                <el-option label="全平台" value="ALL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="设置值" prop="settingValue">
          <el-input v-model="formData.settingValue" placeholder="请输入设置值" />
        </el-form-item>
        <el-form-item label="默认值" prop="defaultValue">
          <el-input v-model="formData.defaultValue" placeholder="请输入默认值" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="版本" prop="version">
              <el-input v-model="formData.version" placeholder="请输入版本号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="formData.priority" :min="1" :max="100" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="是否必需">
              <el-switch v-model="formData.isRequired" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="用户可修改">
              <el-switch v-model="formData.isUserEditable" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否启用">
              <el-switch v-model="formData.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="生效条件" prop="effectiveCondition">
          <el-input v-model="formData.effectiveCondition" placeholder="请输入生效条件" />
        </el-form-item>
        <el-form-item label="设置描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入设置描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog
      title="移动设置测试"
      :visible.sync="testDialogVisible"
      width="600px"
    >
      <el-form :model="testForm" ref="testFormRef" label-width="120px">
        <el-form-item label="测试平台" prop="testPlatform">
          <el-select v-model="testForm.testPlatform" placeholder="请选择测试平台" style="width: 100%">
            <el-option label="iOS" value="IOS" />
            <el-option label="Android" value="ANDROID" />
            <el-option label="微信小程序" value="WECHAT" />
            <el-option label="支付宝小程序" value="ALIPAY" />
          </el-select>
        </el-form-item>
        <el-form-item label="测试数据" prop="testData">
          <el-input
            v-model="testForm.testData"
            type="textarea"
            :rows="6"
            placeholder="请输入JSON格式的测试数据"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmTest" :loading="testLoading">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 模板配置对话框 -->
    <el-dialog
      :title="`${currentSetting ? currentSetting.settingName : ''} - 模板配置`"
      :visible.sync="templateDialogVisible"
      width="90%"
      top="5vh"
      @close="handleTemplateDialogClose"
    >
      <!-- 设置信息 -->
      <el-card shadow="never" style="margin-bottom: 20px;">
        <div style="display: flex; gap: 30px; flex-wrap: wrap;">
          <div>
            <span style="font-weight: 500; color: #606266;">设置编码：</span>
            <span>{{ currentSetting ? currentSetting.settingCode : '' }}</span>
          </div>
          <div>
            <span style="font-weight: 500; color: #606266;">设置类型：</span>
            <el-tag size="small">{{ currentSetting ? getSettingTypeName(currentSetting.settingType) : '' }}</el-tag>
          </div>
          <div>
            <span style="font-weight: 500; color: #606266;">适用平台：</span>
            <el-tag size="small" :type="getPlatformColor(currentSetting ? currentSetting.platform : '')">
              {{ currentSetting ? getPlatformName(currentSetting.platform) : '' }}
            </el-tag>
          </div>
        </div>
      </el-card>

      <!-- 工具栏 -->
      <div style="margin-bottom: 20px;">
        <el-button type="primary" icon="el-icon-plus" @click="handleAddTemplate">新增模板</el-button>
        <el-button type="success" icon="el-icon-refresh" @click="loadTemplates(currentSetting.settingId)">刷新</el-button>
      </div>

      <!-- 模板列表 -->
      <el-table
        :data="templateTableData"
        v-loading="templateLoading"
        stripe
        border
        max-height="400"
      >
        <el-table-column prop="templateName" label="模板名称" min-width="200" />
        <el-table-column prop="platformName" label="适用平台" width="120">
          <template slot-scope="scope">
            <el-tag size="small" :type="getPlatformColor(scope.row.platform)">
              {{ scope.row.platformName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="模板描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'" size="small">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEditTemplate(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleApplyTemplate(scope.row)">应用</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteTemplate(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
          @size-change="handleTemplateSizeChange"
          @current-change="handleTemplateCurrentChange"
          :current-page="templatePagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="templatePagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="templatePagination.total"
        />
      </div>
    </el-dialog>

    <!-- 新增/编辑模板对话框 -->
    <el-dialog
      :title="templateFormTitle"
      :visible.sync="templateFormVisible"
      width="700px"
      @close="handleTemplateFormClose"
    >
      <el-form
        :model="templateFormData"
        :rules="templateFormRules"
        ref="templateFormRef"
        label-width="120px"
      >
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="templateFormData.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="适用平台" prop="platform">
          <el-select v-model="templateFormData.platform" placeholder="请选择适用平台" style="width: 100%">
            <el-option label="iOS" value="IOS" />
            <el-option label="Android" value="ANDROID" />
            <el-option label="微信小程序" value="WECHAT" />
            <el-option label="支付宝小程序" value="ALIPAY" />
            <el-option label="全平台" value="ALL" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板数据" prop="templateData">
          <el-input
            v-model="templateFormData.templateData"
            type="textarea"
            :rows="10"
            placeholder="请输入JSON格式的模板数据"
          />
        </el-form-item>
        <el-form-item label="模板描述" prop="description">
          <el-input
            v-model="templateFormData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="templateFormData.isEnabled" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="templateFormData.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="templateFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTemplate" :loading="templateSaveLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mobileSettingsApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'MobileSettings',
  data() {
    return {
      loading: false,
      saveLoading: false,
      testLoading: false,
      syncLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        settingName: '',
        settingType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增移动设置',
      formData: {
        settingId: null,
        settingCode: '',
        settingName: '',
        settingType: '',
        platform: '',
        settingValue: '',
        defaultValue: '',
        version: '1.0.0',
        priority: 1,
        isRequired: false,
        isUserEditable: true,
        isEnabled: true,
        effectiveCondition: '',
        description: ''
      },
      formRules: {
        settingCode: [
          { required: true, message: '请输入设置编码', trigger: 'blur' }
        ],
        settingName: [
          { required: true, message: '请输入设置名称', trigger: 'blur' }
        ],
        settingType: [
          { required: true, message: '请选择设置类型', trigger: 'change' }
        ],
        platform: [
          { required: true, message: '请选择适用平台', trigger: 'change' }
        ]
      },
      testDialogVisible: false,
      testForm: {
        testPlatform: '',
        testData: ''
      },
      currentTestRow: null,
      // 模板配置相关
      templateDialogVisible: false,
      currentSetting: null,
      templateLoading: false,
      templateSaveLoading: false,
      templateTableData: [],
      templatePagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      templateFormVisible: false,
      templateFormTitle: '新增模板',
      templateFormData: {
        templateId: null,
        templateName: '',
        templateData: '',
        platform: '',
        isEnabled: true,
        description: '',
        remark: ''
      },
      templateFormRules: {
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        platform: [
          { required: true, message: '请选择适用平台', trigger: 'change' }
        ],
        templateData: [
          { required: true, message: '请输入模板数据', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await mobileSettingsApi.getList(params)
        if (response.code === 1) {
          // 处理数据，添加中文名称
          const list = response.data.tlist || []
          this.tableData = list.map(item => {
            return {
              ...item,
              settingTypeName: this.getSettingTypeName(item.settingType),
              platformName: this.getPlatformName(item.platform)
            }
          })
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增移动设置'
      this.formData = {
        settingId: null,
        settingCode: '',
        settingName: '',
        settingType: '',
        platform: '',
        settingValue: '',
        defaultValue: '',
        version: '1.0.0',
        priority: 1,
        isRequired: false,
        isUserEditable: true,
        isEnabled: true,
        effectiveCondition: '',
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑移动设置'
      // 将整数（0/1）转换为布尔值（false/true）
      this.formData = {
        ...row,
        isRequired: row.isRequired === 1,
        isUserEditable: row.isUserEditable === 1,
        isEnabled: row.isEnabled === 1
      }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        // 转换布尔值为整数（0/1）
        const saveData = {
          ...this.formData,
          isRequired: this.formData.isRequired ? 1 : 0,
          isUserEditable: this.formData.isUserEditable ? 1 : 0,
          isEnabled: this.formData.isEnabled ? 1 : 0
        }

        const response = await mobileSettingsApi.save(saveData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该移动设置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await mobileSettingsApi.delete(row.settingId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的移动设置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const settingIds = this.multipleSelection.map(item => item.settingId)
        const response = await mobileSettingsApi.batchDelete(settingIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    async handleToggleStatus(row) {
      try {
        const response = await mobileSettingsApi.updateStatus(row.settingId, !row.isEnabled)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      }
    },
    handleTemplates(row) {
      // 打开模板配置对话框
      this.currentSetting = { ...row }
      this.templateDialogVisible = true
      this.loadTemplates(row.settingId)
    },
    handleTest(row) {
      this.currentTestRow = row
      this.testForm = {
        testPlatform: row.platform,
        testData: '{\n  "userId": "USER001",\n  "deviceId": "DEVICE001",\n  "version": "1.0.0"\n}'
      }
      this.testDialogVisible = true
    },
    async handleConfirmTest() {
      try {
        this.testLoading = true
        let testData
        try {
          testData = JSON.parse(this.testForm.testData)
        } catch (e) {
          this.$message.error('测试数据格式错误，请输入有效的JSON格式')
          return
        }
        
        const response = await mobileSettingsApi.test(this.currentTestRow.settingId, {
          platform: this.testForm.testPlatform,
          testData
        })
        if (response.code === 1) {
          const result = response.data
          this.$alert(
            `测试结果：${result.testResult}\n测试消息：${result.testMessage}\n响应时间：${result.responseTime}ms`,
            '测试结果',
            { type: result.testResult === 'SUCCESS' ? 'success' : 'warning' }
          )
          this.testDialogVisible = false
        } else {
          this.$message.error(response.msg || '测试失败')
        }
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      } finally {
        this.testLoading = false
      }
    },
    async handleSync() {
      try {
        this.syncLoading = true
        const response = await mobileSettingsApi.sync({
          settingIds: this.multipleSelection.map(item => item.settingId)
        })
        if (response.code === 1) {
          this.$message.success('同步成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败：' + error.message)
      } finally {
        this.syncLoading = false
      }
    },
    async handleSyncSingle(row) {
      try {
        const response = await mobileSettingsApi.sync({
          settingIds: [row.settingId]
        })
        if (response.code === 1) {
          this.$message.success('同步成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '同步失败')
        }
      } catch (error) {
        this.$message.error('同步失败：' + error.message)
      }
    },
    getPlatformColor(platform) {
      const colors = {
        'IOS': 'primary',
        'ANDROID': 'success',
        'WECHAT': 'warning',
        'ALIPAY': 'info',
        'ALL': 'danger'
      }
      return colors[platform] || 'default'
    },
    getPlatformName(platform) {
      const names = {
        'IOS': 'iOS',
        'ANDROID': 'Android',
        'WECHAT': '微信小程序',
        'ALIPAY': '支付宝小程序',
        'ALL': '全平台'
      }
      return names[platform] || platform
    },
    getSettingTypeName(settingType) {
      const names = {
        'UI': '界面设置',
        'FUNCTION': '功能设置',
        'PERMISSION': '权限设置',
        'NOTIFICATION': '推送设置',
        'SECURITY': '安全设置'
      }
      return names[settingType] || settingType
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    },
    // 模板配置相关方法
    async loadTemplates(settingId) {
      this.templateLoading = true
      try {
        const params = {
          page: this.templatePagination.currentPage - 1,
          size: this.templatePagination.pageSize,
          settingId: settingId
        }
        const response = await mobileSettingsApi.getTemplates(params)
        if (response.code === 1) {
          const list = response.data.tlist || []
          this.templateTableData = list.map(item => {
            return {
              ...item,
              platformName: this.getPlatformName(item.platform)
            }
          })
          this.templatePagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询模板失败')
        }
      } catch (error) {
        this.$message.error('查询模板失败：' + error.message)
      } finally {
        this.templateLoading = false
      }
    },
    handleAddTemplate() {
      this.templateFormTitle = '新增模板'
      this.templateFormData = {
        templateId: null,
        templateName: '',
        templateData: '',
        platform: this.currentSetting.platform || '',
        isEnabled: true,
        description: '',
        remark: ''
      }
      this.templateFormVisible = true
    },
    handleEditTemplate(row) {
      this.templateFormTitle = '编辑模板'
      this.templateFormData = {
        ...row,
        isEnabled: row.isEnabled === 1
      }
      this.templateFormVisible = true
    },
    async handleSaveTemplate() {
      try {
        await this.$refs.templateFormRef.validate()
        this.templateSaveLoading = true

        const saveData = {
          ...this.templateFormData,
          settingId: this.currentSetting.settingId,
          isEnabled: this.templateFormData.isEnabled ? 1 : 0
        }

        const response = await mobileSettingsApi.saveTemplate(saveData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.templateFormVisible = false
          this.loadTemplates(this.currentSetting.settingId)
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.templateSaveLoading = false
      }
    },
    async handleDeleteTemplate(row) {
      try {
        await this.$confirm('确定要删除此模板吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await mobileSettingsApi.deleteTemplate(row.templateId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadTemplates(this.currentSetting.settingId)
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleApplyTemplate(row) {
      try {
        await this.$confirm('确定要将此模板应用到当前设置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await mobileSettingsApi.applyTemplate({
          settingId: this.currentSetting.settingId,
          templateId: row.templateId
        })

        if (response.code === 1) {
          this.$message.success('应用模板成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '应用模板失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('应用模板失败：' + error.message)
        }
      }
    },
    handleTemplateDialogClose() {
      this.templateDialogVisible = false
      this.currentSetting = null
      this.templateTableData = []
    },
    handleTemplateFormClose() {
      this.$refs.templateFormRef.resetFields()
    },
    handleTemplateSizeChange(size) {
      this.templatePagination.pageSize = size
      this.loadTemplates(this.currentSetting.settingId)
    },
    handleTemplateCurrentChange(page) {
      this.templatePagination.currentPage = page
      this.loadTemplates(this.currentSetting.settingId)
    }
  }
}
</script>

<style scoped>
.mobile-settings-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
