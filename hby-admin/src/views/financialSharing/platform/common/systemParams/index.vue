<template>
  <div class="financial-common-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-setting"></i>
          系统参数配置
        </h1>
        <p class="page-description">配置财务系统运行所需的基础参数</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增参数
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportConfig">
          导出配置
        </el-button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <el-row :gutter="24">
        <!-- 左侧分类导航 -->
        <el-col :span="6">
          <el-card class="category-card">
            <div slot="header" class="clearfix">
              <span>参数分类</span>
            </div>
            <el-menu
              :default-active="activeCategory"
              class="category-menu"
              @select="handleCategorySelect"
            >
              <el-menu-item index="all">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-menu menu-icon"></i>
                    <span class="menu-text">全部配置</span>
                  </div>
                  <el-badge :value="totalCount" class="menu-badge" type="primary" />
                </div>
              </el-menu-item>
              <el-menu-item index="BASE_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-s-tools menu-icon"></i>
                    <span class="menu-text">基础配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('BASE_CONFIG')" class="menu-badge" type="success" />
                </div>
              </el-menu-item>
              <el-menu-item index="WORKFLOW_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-s-operation menu-icon"></i>
                    <span class="menu-text">工作流配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('WORKFLOW_CONFIG')" class="menu-badge" type="warning" />
                </div>
              </el-menu-item>
              <el-menu-item index="VOUCHER_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-document menu-icon"></i>
                    <span class="menu-text">凭证配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('VOUCHER_CONFIG')" class="menu-badge" type="info" />
                </div>
              </el-menu-item>
              <el-menu-item index="REPORT_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-data-analysis menu-icon"></i>
                    <span class="menu-text">报表配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('REPORT_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="AUDIT_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-view menu-icon"></i>
                    <span class="menu-text">审计配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('AUDIT_CONFIG')" class="menu-badge" type="danger" />
                </div>
              </el-menu-item>
              <el-menu-item index="BACKUP_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-download menu-icon"></i>
                    <span class="menu-text">备份配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('BACKUP_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="RATE_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-coin menu-icon"></i>
                    <span class="menu-text">汇率配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('RATE_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="TAX_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-s-finance menu-icon"></i>
                    <span class="menu-text">税务配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('TAX_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="ASSET_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-office-building menu-icon"></i>
                    <span class="menu-text">资产配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('ASSET_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="COST_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-money menu-icon"></i>
                    <span class="menu-text">成本配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('COST_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="SYSTEM_LIMIT_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-lock menu-icon"></i>
                    <span class="menu-text">权限配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('SYSTEM_LIMIT_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
              <el-menu-item index="NOTIFICATION_CONFIG">
                <div class="menu-item-content">
                  <div class="menu-item-left">
                    <i class="el-icon-bell menu-icon"></i>
                    <span class="menu-text">通知配置</span>
                  </div>
                  <el-badge :value="getCategoryCount('NOTIFICATION_CONFIG')" class="menu-badge" />
                </div>
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>

        <!-- 右侧参数列表 -->
        <el-col :span="18">
          <el-card class="params-card">
            <!-- 搜索栏 -->
            <div class="search-bar">
              <el-input
                v-model="searchKeyword"
                placeholder="请输入参数名称或编码"
                prefix-icon="el-icon-search"
                clearable
                @input="handleSearch"
                style="width: 300px"
              />
            </div>

            <!-- 参数列表表格 -->
            <el-table
              v-loading="loading"
              :data="paramsList"
              @selection-change="handleSelectionChange"
              border
              stripe
              style="width: 100%"
            >
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="参数编码" prop="paramCode" width="180" show-overflow-tooltip />
              <el-table-column label="参数名称" prop="paramName" width="200" show-overflow-tooltip />
              <el-table-column label="参数值" prop="paramValue" width="180" show-overflow-tooltip>
                <template slot-scope="scope">
                  <span v-if="scope.row.paramType === 'BOOLEAN'">
                    <el-tag :type="scope.row.paramValue === 'true' || scope.row.paramValue === '1' ? 'success' : 'info'" size="mini">
                      {{ scope.row.paramValue === 'true' || scope.row.paramValue === '1' ? '是' : '否' }}
                    </el-tag>
                  </span>
                  <span v-else>{{ scope.row.paramValue }}</span>
                </template>
              </el-table-column>
              <el-table-column label="参数类型" prop="paramTypeName" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="getParamTypeColor(scope.row.paramType)">
                    {{ scope.row.paramTypeName || getParamTypeName(scope.row.paramType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="是否必填" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.isRequired === 1 ? 'success' : 'info'" size="mini">
                    {{ scope.row.isRequired === 1 ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="状态" width="80" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.isEnabled"
                    :active-value="1"
                    :inactive-value="0"
                    @change="handleStatusChange(scope.row)"
                  />
                </template>
              </el-table-column>
              <el-table-column label="更新时间" prop="updateTime" width="160" align="center">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.updateTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-view"
                    @click="handleView(scope.row)"
                  >查看</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleEdit(scope.row)"
                  >编辑</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
              v-show="pageParams.total > 0"
              background
              :current-page="pageParams.pageNo"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageParams.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pageParams.total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              style="margin-top: 20px; text-align: right"
            />
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 参数表单对话框 -->
    <el-dialog
      :title="formTitle"
      :visible.sync="formVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="参数编码" prop="paramCode">
          <el-input v-model="form.paramCode" placeholder="请输入参数编码" :disabled="formType === 'edit'" />
        </el-form-item>
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="form.paramName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数分类" prop="categoryCode">
          <el-select v-model="form.categoryCode" placeholder="请选择参数分类" style="width: 100%">
            <el-option label="基础配置" value="BASE_CONFIG" />
            <el-option label="工作流配置" value="WORKFLOW_CONFIG" />
            <el-option label="凭证配置" value="VOUCHER_CONFIG" />
            <el-option label="报表配置" value="REPORT_CONFIG" />
            <el-option label="审计配置" value="AUDIT_CONFIG" />
            <el-option label="备份配置" value="BACKUP_CONFIG" />
            <el-option label="汇率配置" value="RATE_CONFIG" />
            <el-option label="税务配置" value="TAX_CONFIG" />
            <el-option label="资产配置" value="ASSET_CONFIG" />
            <el-option label="成本配置" value="COST_CONFIG" />
            <el-option label="权限配置" value="SYSTEM_LIMIT_CONFIG" />
            <el-option label="通知配置" value="NOTIFICATION_CONFIG" />
          </el-select>
        </el-form-item>
        <el-form-item label="参数类型" prop="paramType">
          <el-select v-model="form.paramType" placeholder="请选择参数类型" style="width: 100%">
            <el-option label="字符串" value="STRING" />
            <el-option label="数字" value="NUMBER" />
            <el-option label="日期" value="DATE" />
            <el-option label="布尔值" value="BOOLEAN" />
          </el-select>
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input
            v-if="form.paramType === 'NUMBER'"
            v-model.number="form.paramValue"
            placeholder="请输入数字"
          />
          <el-switch
            v-else-if="form.paramType === 'BOOLEAN'"
            v-model="form.paramValue"
            :active-value="'1'"
            :inactive-value="'0'"
          />
          <el-date-picker
            v-else-if="form.paramType === 'DATE'"
            v-model="form.paramValue"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          />
          <el-input
            v-else
            v-model="form.paramValue"
            placeholder="请输入参数值"
          />
        </el-form-item>
        <el-form-item label="默认值">
          <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
        </el-form-item>
        <el-form-item label="是否必填">
          <el-switch v-model="form.isRequired" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="参数描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入参数描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import { transData } from '@/utils/requestData'

export default {
  name: 'SystemParams',
  data() {
    return {
      // 搜索和筛选
      searchKeyword: '',
      activeCategory: 'all',
      // 加载状态
      loading: true,
      // 参数列表
      paramsList: [],
      // 分类统计
      categoryCounts: {},
      // 选中的参数
      selectedParams: [],
      // 分页参数
      pageParams: {
        pageNo: 1,
        pageSize: 20,
        total: 0
      },
      // 表单相关
      formVisible: false,
      formType: 'add', // add/edit/view
      formTitle: '',
      submitting: false,
      form: {
        id: null,
        paramCode: '',
        paramName: '',
        categoryCode: 'BASE_CONFIG',
        paramType: 'STRING',
        paramValue: '',
        defaultValue: '',
        isRequired: 0,
        isEnabled: 1,
        description: '',
        tenantId: 1,
        bookId: 1
      },
      formRules: {
        paramCode: [
          { required: true, message: '请输入参数编码', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '参数编码格式不正确', trigger: 'blur' }
        ],
        paramName: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ],
        categoryCode: [
          { required: true, message: '请选择参数分类', trigger: 'change' }
        ],
        paramType: [
          { required: true, message: '请选择参数类型', trigger: 'change' }
        ],
        paramValue: [
          { required: true, message: '请输入参数值', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    totalCount() {
      return this.pageParams.total || 0
    }
  },
  mounted() {
    this.loadParamsList()
  },
  methods: {
    /** 加载参数列表 */
    async loadParamsList() {
      this.loading = true
      try {
        // 准备请求参数
        const params = {
          pageNo: this.pageParams.pageNo,
          pageSize: this.pageParams.pageSize,
          tenantId: 1,
          bookId: 1
        }

        // 只有在有值的情况下才添加其他参数
        if (this.searchKeyword) {
          params.paramName = this.searchKeyword
        }
        if (this.activeCategory !== 'all') {
          params.categoryCode = this.activeCategory
        }

        // 调用API获取参数列表 - 使用正确的接口路径
        const response = await request({
          url: '/zbgl/financial/common/systemParams/list',
          method: 'post',
          data: transData(params)
        })

        // 添加响应数据验证
        if (!response) {
          throw new Error('响应数据为空')
        }

        if (response.code === 1) {
          // 正确处理PageResult格式的响应数据
          const pageData = response.data
          if (pageData) {
            this.paramsList = pageData.tlist || []
            this.pageParams.total = pageData.totalRecord || 0
            this.pageParams.pageNo = pageData.pageNo || 1
            this.pageParams.pageSize = pageData.pageSize || 20
            // 计算分类统计
            this.calculateCategoryCounts()
          } else {
            this.paramsList = []
            this.pageParams.total = 0
          }
        } else {
          this.$message.error(response.msg || '获取参数列表失败')
          // 接口失败时降级为空状态
          this.loadMockData()
        }
      } catch (error) {
        console.error('加载参数列表失败:', error)
        this.$message.error('加载参数列表失败')
        // 接口失败时降级为空状态
        this.loadMockData()
      } finally {
        this.loading = false
      }
    },

    /** 加载空状态数据（API 不可用时降级展示） */
    loadMockData() {
      this.paramsList = []
      this.pageParams.total = 0
      this.calculateCategoryCounts()
    },

    /** 计算分类统计 */
    calculateCategoryCounts() {
      this.categoryCounts = {}
      const allCategories = [
        'BASE_CONFIG', 'WORKFLOW_CONFIG', 'VOUCHER_CONFIG', 'REPORT_CONFIG', 'AUDIT_CONFIG', 'BACKUP_CONFIG',
        'RATE_CONFIG', 'TAX_CONFIG', 'ASSET_CONFIG', 'COST_CONFIG', 'SYSTEM_LIMIT_CONFIG', 'NOTIFICATION_CONFIG'
      ]

      allCategories.forEach(category => {
        this.categoryCounts[category] = this.paramsList.filter(item => item.categoryCode === category).length
      })
    },

    /** 获取分类数量 */
    getCategoryCount(category) {
      return this.categoryCounts[category] || 0
    },

    /** 搜索处理 */
    handleSearch() {
      this.pageParams.pageNo = 1
      this.loadParamsList()
    },

    /** 分类选择处理 */
    handleCategorySelect(category) {
      this.activeCategory = category
      this.pageParams.pageNo = 1
      this.loadParamsList()
    },

    /** 表格选择变化处理 */
    handleSelectionChange(selection) {
      this.selectedParams = selection
    },

    /** 分页大小变化处理 */
    handleSizeChange(val) {
      this.pageParams.pageSize = val
      this.loadParamsList()
    },

    /** 当前页变化处理 */
    handleCurrentChange(val) {
      this.pageParams.pageNo = val
      this.loadParamsList()
    },

    /** 状态变化处理 */
    handleStatusChange(row) {
      // 调用API更新状态
      this.updateStatus(row.id, row.isEnabled)
    },

    /** 更新状态 */
    async updateStatus(id, isEnabled) {
      try {
        const response = await request({
          url: '/zbgl/financial/common/systemParams/status',
          method: 'put',
          params: { id, isEnabled }
        })
        if (response.code === 1) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.msg || '状态更新失败')
          // 回滚状态
          row.isEnabled = isEnabled === 1 ? 0 : 1
        }
      } catch (error) {
        console.error('更新状态失败:', error)
        this.$message.error('状态更新失败')
        // 回滚状态
        row.isEnabled = isEnabled === 1 ? 0 : 1
      }
    },

    /** 新增参数 */
    handleAdd() {
      this.formType = 'add'
      this.formTitle = '新增参数'
      this.resetForm()
      this.formVisible = true
    },

    /** 查看参数 */
    handleView(row) {
      this.formType = 'view'
      this.formTitle = '查看参数'
      this.form = { ...row }
      this.formVisible = true
    },

    /** 编辑参数 */
    handleEdit(row) {
      this.formType = 'edit'
      this.formTitle = '编辑参数'
      this.form = { ...row }
      this.formVisible = true
    },

    /** 删除参数 */
    handleDelete(row) {
      this.$confirm('确定要删除该参数吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await request({
            url: `/zbgl/financial/common/systemParams/${row.id}`,
            method: 'delete'
          })
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadParamsList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除参数失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    /** 重置表单 */
    resetForm() {
      this.form = {
        id: null,
        paramCode: '',
        paramName: '',
        categoryCode: 'BASE_CONFIG',
        paramType: 'STRING',
        paramValue: '',
        defaultValue: '',
        isRequired: 0,
        isEnabled: 1,
        description: '',
        tenantId: 1,
        bookId: 1
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    /** 提交表单 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          this.saveParam()
        }
      })
    },

    /** 保存参数 */
    async saveParam() {
      try {
        const url = this.formType === 'add'
          ? '/zbgl/financial/common/systemParams/save'
          : '/zbgl/financial/common/systemParams/update'

        const method = this.formType === 'add' ? 'post' : 'put'
        const response = await request({
          url: url,
          method: method,
          data: transData(this.form)
        })

        if (response.code === 1) {
          this.$message.success(this.formType === 'add' ? '新增成功' : '更新成功')
          this.formVisible = false
          this.loadParamsList()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存参数失败:', error)
        this.$message.error('保存失败')
      } finally {
        this.submitting = false
      }
    },

    /** 导出配置 */
    exportConfig() {
      // 这里实现导出功能
      this.$message.success('导出成功')
    },

    /** 获取参数类型名称 */
    getParamTypeName(type) {
      const typeMap = {
        'STRING': '字符串',
        'NUMBER': '数字',
        'DATE': '日期',
        'BOOLEAN': '布尔值'
      }
      return typeMap[type] || type
    },

    /** 获取参数类型颜色 */
    getParamTypeColor(type) {
      const colorMap = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'DATE': 'warning',
        'BOOLEAN': 'info'
      }
      return colorMap[type] || 'info'
    },

    /** 格式化日期时间 */
    formatDateTime(dateTime) {
      if (!dateTime) return '-'

      try {
        const date = new Date(dateTime)
        if (isNaN(date.getTime())) return '-'

        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      } catch (error) {
        console.error('日期格式化错误:', error)
        return '-'
      }
    }
  }
}
</script>

<style scoped>
.financial-common-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
  display: flex;
  align-items: center;
}

.page-title i {
  margin-right: 8px;
  color: #409eff;
}

.page-description {
  margin: 8px 0 0 0;
  font-size: 14px;
  color: #909399;
}

.content-area {
  min-height: 400px;
}

.category-card {
  height: fit-content;
}

.category-menu {
  border: none;
}

.category-menu .el-menu-item {
  padding: 0;
  height: 56px;
  line-height: 56px;
}

.menu-item-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 0 20px;
}

.menu-item-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.menu-icon {
  font-size: 18px;
  color: #606266;
  margin-right: 10px;
  width: 20px;
  text-align: center;
}

.menu-text {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.menu-badge {
  flex-shrink: 0;
}

/* 菜单项hover和选中状态 */
.category-menu .el-menu-item:hover .menu-icon,
.category-menu .el-menu-item.is-active .menu-icon {
  color: #409eff;
}

.category-menu .el-menu-item:hover .menu-text,
.category-menu .el-menu-item.is-active .menu-text {
  color: #409eff;
  font-weight: 600;
}

/* 自定义徽章样式 */
.menu-badge :deep(.el-badge__content) {
  font-size: 11px;
  height: 18px;
  line-height: 18px;
  padding: 0 6px;
  border-radius: 9px;
  min-width: 18px;
}

.menu-badge :deep(.el-badge__content.is-fixed) {
  position: static;
  transform: none;
}

.params-card {
  min-height: 600px;
}

.search-bar {
  margin-bottom: 20px;
}
</style>