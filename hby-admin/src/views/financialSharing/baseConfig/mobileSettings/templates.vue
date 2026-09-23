<template>
  <div class="mobile-settings-templates-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="goBack" :content="`${settingInfo.settingName} - 模板配置`" />
    </div>

    <!-- 设置信息卡片 -->
    <el-card class="setting-info-card" shadow="never">
      <div class="info-row">
        <div class="info-item">
          <span class="label">设置编码：</span>
          <span class="value">{{ settingInfo.settingCode }}</span>
        </div>
        <div class="info-item">
          <span class="label">设置类型：</span>
          <el-tag size="small">{{ settingInfo.settingTypeName }}</el-tag>
        </div>
        <div class="info-item">
          <span class="label">适用平台：</span>
          <el-tag size="small" :type="getPlatformColor(settingInfo.platform)">
            {{ settingInfo.platformName }}
          </el-tag>
        </div>
      </div>
    </el-card>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增模板</el-button>
      <el-button type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
    </div>

    <!-- 模板列表 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
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
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleApply(scope.row)">应用</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
    </el-card>

    <!-- 新增/编辑模板对话框 -->
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
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="formData.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="适用平台" prop="platform">
          <el-select v-model="formData.platform" placeholder="请选择适用平台" style="width: 100%">
            <el-option label="iOS" value="IOS" />
            <el-option label="Android" value="ANDROID" />
            <el-option label="微信小程序" value="WECHAT" />
            <el-option label="支付宝小程序" value="ALIPAY" />
            <el-option label="全平台" value="ALL" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板数据" prop="templateData">
          <el-input
            v-model="formData.templateData"
            type="textarea"
            :rows="10"
            placeholder="请输入JSON格式的模板数据"
          />
        </el-form-item>
        <el-form-item label="模板描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="formData.isEnabled" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mobileSettingsApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'MobileSettingsTemplates',
  data() {
    return {
      settingId: '',
      settingInfo: {
        settingCode: '',
        settingName: '',
        settingType: '',
        settingTypeName: '',
        platform: '',
        platformName: ''
      },
      loading: false,
      saveLoading: false,
      tableData: [],
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增模板',
      formData: {
        templateId: null,
        templateName: '',
        templateData: '',
        platform: '',
        isEnabled: true,
        description: '',
        remark: ''
      },
      formRules: {
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
    this.settingId = this.$route.query.settingId
    if (this.settingId) {
      this.loadSettingInfo()
      this.loadData()
    } else {
      this.$message.error('缺少设置ID参数')
      this.goBack()
    }
  },
  methods: {
    async loadSettingInfo() {
      try {
        const response = await mobileSettingsApi.getDetail(this.settingId)
        if (response.code === 1) {
          this.settingInfo = {
            ...response.data,
            settingTypeName: this.getSettingTypeName(response.data.settingType),
            platformName: this.getPlatformName(response.data.platform)
          }
        } else {
          this.$message.error(response.msg || '获取设置信息失败')
        }
      } catch (error) {
        this.$message.error('获取设置信息失败：' + error.message)
      }
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          settingId: this.settingId
        }
        const response = await mobileSettingsApi.getTemplates(params)
        if (response.code === 1) {
          const list = response.data.tlist || []
          this.tableData = list.map(item => {
            return {
              ...item,
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
    handleAdd() {
      this.dialogTitle = '新增模板'
      this.formData = {
        templateId: null,
        templateName: '',
        templateData: '',
        platform: this.settingInfo.platform || '',
        isEnabled: true,
        description: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑模板'
      this.formData = {
        ...row,
        isEnabled: row.isEnabled === 1
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.dialogTitle = '查看模板'
      this.formData = {
        ...row,
        isEnabled: row.isEnabled === 1
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        // 设置表单为只读模式
        const formInputs = this.$refs.formRef.$el.querySelectorAll('input, textarea, .el-select')
        formInputs.forEach(input => {
          input.setAttribute('disabled', 'disabled')
        })
      })
    },
    async handleApply(row) {
      try {
        await this.$confirm('确定要将此模板应用到当前设置吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await mobileSettingsApi.applyTemplate({
          settingId: this.settingId,
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
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除此模板吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await mobileSettingsApi.deleteTemplate(row.templateId)
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
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true

        // 转换布尔值为整数
        const saveData = {
          ...this.formData,
          settingId: this.settingId,
          isEnabled: this.formData.isEnabled ? 1 : 0
        }

        const response = await mobileSettingsApi.saveTemplate(saveData)
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
    handleDialogClose() {
      this.$refs.formRef.resetFields()
      // 移除只读属性
      const formInputs = this.$refs.formRef.$el.querySelectorAll('input, textarea, .el-select')
      formInputs.forEach(input => {
        input.removeAttribute('disabled')
      })
    },
    goBack() {
      this.$router.back()
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
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    }
  }
}
</script>

<style scoped>
.mobile-settings-templates-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.setting-info-card {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-item .label {
  font-weight: 500;
  color: #606266;
}

.info-item .value {
  color: #303133;
}

.toolbar {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}
</style>

