<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="portal-detail">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicForm"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            :disabled="mode === 'view'"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="门户名称" prop="portalName">
                  <el-input v-model="formData.portalName" placeholder="请输入门户名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="门户编码" prop="portalCode">
                  <el-input v-model="formData.portalCode" placeholder="请输入门户编码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="门户类型" prop="portalType">
                  <el-select v-model="formData.portalType" placeholder="请选择门户类型" style="width: 100%">
                    <el-option label="员工门户" value="EMPLOYEE" />
                    <el-option label="客户门户" value="CUSTOMER" />
                    <el-option label="合作伙伴门户" value="PARTNER" />
                    <el-option label="管理门户" value="ADMIN" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="门户状态" prop="portalStatus">
                  <el-select v-model="formData.portalStatus" placeholder="请选择门户状态" style="width: 100%">
                    <el-option label="活跃" value="ACTIVE" />
                    <el-option label="非活跃" value="INACTIVE" />
                    <el-option label="维护中" value="MAINTENANCE" />
                    <el-option label="已归档" value="ARCHIVED" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="门户URL">
                  <el-input v-model="formData.portalUrl" placeholder="请输入门户URL" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="门户优先级">
                  <el-input-number
                    v-model="formData.portalPriority"
                    :min="1"
                    :max="10"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="门户所有者">
                  <el-input v-model="formData.portalOwnerName" placeholder="请输入所有者名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="门户管理员">
                  <el-input v-model="formData.portalAdminName" placeholder="请输入管理员名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="部门名称">
                  <el-input v-model="formData.departmentName" placeholder="请输入部门名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="公司名称">
                  <el-input v-model="formData.companyName" placeholder="请输入公司名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="门户语言">
                  <el-select v-model="formData.portalLanguage" placeholder="请选择语言" style="width: 100%">
                    <el-option label="中文" value="zh-CN" />
                    <el-option label="英文" value="en-US" />
                    <el-option label="日文" value="ja-JP" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="门户时区">
                  <el-select v-model="formData.portalTimezone" placeholder="请选择时区" style="width: 100%">
                    <el-option label="北京时间" value="Asia/Shanghai" />
                    <el-option label="东京时间" value="Asia/Tokyo" />
                    <el-option label="纽约时间" value="America/New_York" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="是否激活">
                  <el-switch
                    v-model="formData.isActive"
                    :active-value="1"
                    :inactive-value="0"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否默认">
                  <el-switch
                    v-model="formData.isDefault"
                    :active-value="1"
                    :inactive-value="0"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否公开">
                  <el-switch
                    v-model="formData.isPublic"
                    :active-value="1"
                    :inactive-value="0"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="门户描述">
              <el-input
                v-model="formData.portalDescription"
                type="textarea"
                :rows="3"
                placeholder="请输入门户描述"
              />
            </el-form-item>
            <el-form-item label="门户标签">
              <el-input v-model="formData.portalTags" placeholder="请输入门户标签，多个标签用逗号分隔" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="formData.remarks"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 配置信息 -->
        <el-tab-pane label="配置信息" name="config">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-card title="布局配置" shadow="never">
                <el-form label-width="100px" size="small">
                  <el-form-item label="布局模式">
                    <el-select v-model="configData.layoutMode" style="width: 100%">
                      <el-option label="经典布局" value="classic" />
                      <el-option label="现代布局" value="modern" />
                      <el-option label="简约布局" value="simple" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="侧边栏">
                    <el-switch v-model="configData.showSidebar" />
                  </el-form-item>
                  <el-form-item label="顶部导航">
                    <el-switch v-model="configData.showTopNav" />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card title="主题配置" shadow="never">
                <el-form label-width="100px" size="small">
                  <el-form-item label="主题色">
                    <el-color-picker v-model="configData.primaryColor" />
                  </el-form-item>
                  <el-form-item label="背景色">
                    <el-color-picker v-model="configData.backgroundColor" />
                  </el-form-item>
                  <el-form-item label="暗色模式">
                    <el-switch v-model="configData.darkMode" />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="24" style="margin-top: 16px">
            <el-col :span="12">
              <el-card title="功能配置" shadow="never">
                <el-form label-width="100px" size="small">
                  <el-form-item label="搜索功能">
                    <el-switch v-model="configData.enableSearch" />
                  </el-form-item>
                  <el-form-item label="通知功能">
                    <el-switch v-model="configData.enableNotification" />
                  </el-form-item>
                  <el-form-item label="个性化">
                    <el-switch v-model="configData.enablePersonalization" />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card title="安全配置" shadow="never">
                <el-form label-width="100px" size="small">
                  <el-form-item label="单点登录">
                    <el-switch v-model="configData.enableSSO" />
                  </el-form-item>
                  <el-form-item label="双因子认证">
                    <el-switch v-model="configData.enable2FA" />
                  </el-form-item>
                  <el-form-item label="会话超时">
                    <el-input-number
                      v-model="configData.sessionTimeout"
                      :min="5"
                      :max="480"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 统计信息 -->
        <el-tab-pane label="统计信息" name="statistics">
          <el-row :gutter="24">
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-item">
                  <div class="stat-value">{{ formatAccessCount(formData.accessCount) }}</div>
                  <div class="stat-label">总访问量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-item">
                  <div class="stat-value">{{ statisticsData.uniqueUsers || 0 }}</div>
                  <div class="stat-label">独立用户</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-item">
                  <div class="stat-value">{{ statisticsData.avgSessionTime || 0 }}分钟</div>
                  <div class="stat-label">平均会话时长</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-item">
                  <div class="stat-value">{{ calculateHealthScore(formData) }}%</div>
                  <div class="stat-label">健康度</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          
          <el-row :gutter="24" style="margin-top: 16px">
            <el-col :span="12">
              <el-card title="访问趋势" shadow="never">
                <div id="accessTrendChart" style="height: 300px"></div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card title="用户分布" shadow="never">
                <div id="userDistributionChart" style="height: 300px"></div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 访问日志 -->
        <el-tab-pane label="访问日志" name="logs">
          <el-table :data="accessLogs" stripe border style="width: 100%">
            <el-table-column prop="accessTime" label="访问时间" width="160">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.accessTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="userName" label="用户名称" width="120" />
            <el-table-column prop="userIp" label="访问IP" width="120" />
            <el-table-column prop="userAgent" label="用户代理" show-overflow-tooltip />
            <el-table-column prop="sessionDuration" label="会话时长" width="100">
              <template slot-scope="scope">
                {{ scope.row.sessionDuration }}分钟
              </template>
            </el-table-column>
            <el-table-column prop="pageViews" label="页面浏览" width="100" />
          </el-table>
          <div class="pagination-container">
            <el-pagination
              :current-page="logPagination.current"
              :page-size="logPagination.size"
              :total="logPagination.total"
              layout="prev, pager, next"
              @current-change="handleLogPageChange"
            />
          </div>
        </el-tab-pane>

        <!-- 维护记录 -->
        <el-tab-pane label="维护记录" name="maintenance">
          <el-table :data="maintenanceRecords" stripe border style="width: 100%">
            <el-table-column prop="maintenanceTime" label="维护时间" width="160">
              <template slot-scope="scope">
                {{ formatDateTime(scope.row.maintenanceTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="maintenanceType" label="维护类型" width="120">
              <template slot-scope="scope">
                <el-tag :type="getMaintenanceTypeColor(scope.row.maintenanceType)" size="small">
                  {{ formatMaintenanceType(scope.row.maintenanceType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="maintenanceReason" label="维护原因" show-overflow-tooltip />
            <el-table-column prop="maintenanceResult" label="维护结果" width="120">
              <template slot-scope="scope">
                <el-tag :type="scope.row.maintenanceResult === 'SUCCESS' ? 'success' : 'danger'" size="small">
                  {{ scope.row.maintenanceResult === 'SUCCESS' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="maintainer" label="维护人员" width="120" />
            <el-table-column prop="duration" label="维护时长" width="100">
              <template slot-scope="scope">
                {{ scope.row.duration }}分钟
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="mode !== 'view'" type="primary" @click="handleSave">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getServicePortalById,
  createServicePortal,
  updateServicePortal,
  getPortalAccessLogs,
  getPortalMaintenanceRecords,
  utils
} from '@/api/managementAccountant/ss/servicePortal'

export default {
  name: 'ServicePortalDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    portalId: {
      type: [String, Number],
      default: null
    },
    mode: {
      type: String,
      default: 'view' // view, edit, create
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      formData: {
        portalName: '',
        portalCode: '',
        portalType: '',
        portalStatus: 'INACTIVE',
        portalUrl: '',
        portalDescription: '',
        portalPriority: 5,
        portalOwnerName: '',
        portalAdminName: '',
        departmentName: '',
        companyName: '',
        portalLanguage: 'zh-CN',
        portalTimezone: 'Asia/Shanghai',
        portalTags: '',
        isActive: 0,
        isDefault: 0,
        isPublic: 0,
        isCustomizable: 1,
        accessCount: 0,
        remarks: ''
      },
      configData: {
        layoutMode: 'classic',
        showSidebar: true,
        showTopNav: true,
        primaryColor: '#409EFF',
        backgroundColor: '#FFFFFF',
        darkMode: false,
        enableSearch: true,
        enableNotification: true,
        enablePersonalization: true,
        enableSSO: false,
        enable2FA: false,
        sessionTimeout: 30
      },
      statisticsData: {
        uniqueUsers: 0,
        avgSessionTime: 0
      },
      accessLogs: [],
      maintenanceRecords: [],
      logPagination: {
        current: 1,
        size: 10,
        total: 0
      },
      formRules: {
        portalName: [
          { required: true, message: '请输入门户名称', trigger: 'blur' }
        ],
        portalCode: [
          { required: true, message: '请输入门户编码', trigger: 'blur' }
        ],
        portalType: [
          { required: true, message: '请选择门户类型', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        view: '查看门户',
        edit: '编辑门户',
        create: '新建门户'
      }
      return titleMap[this.mode] || '门户详情'
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
      }
    }
  },
  methods: {
    // 初始化数据
    async initData() {
      if (this.mode === 'create') {
        this.resetForm()
      } else if (this.portalId) {
        await this.loadPortalData()
        await this.loadAccessLogs()
        await this.loadMaintenanceRecords()
      }
    },

    // 加载门户数据
    async loadPortalData() {
      this.loading = true
      try {
        const response = await getServicePortalById(this.portalId, this.tenantId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
          // 解析配置数据
          if (response.data.layoutConfig) {
            try {
              const layoutConfig = JSON.parse(response.data.layoutConfig)
              this.configData = { ...this.configData, ...layoutConfig }
            } catch (e) {
              console.warn('解析布局配置失败:', e)
            }
          }
        }
      } catch (error) {
        this.$message.error('加载门户数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 加载访问日志
    async loadAccessLogs() {
      try {
        const endTime = new Date()
        const startTime = new Date(endTime.getTime() - 30 * 24 * 60 * 60 * 1000) // 30天前
        
        const response = await getPortalAccessLogs(
          this.portalId,
          startTime.toISOString(),
          endTime.toISOString(),
          this.tenantId
        )
        if (response.success) {
          this.accessLogs = response.data || []
        }
      } catch (error) {
        console.warn('加载访问日志失败:', error)
      }
    },

    // 加载维护记录
    async loadMaintenanceRecords() {
      try {
        const response = await getPortalMaintenanceRecords(this.portalId, this.tenantId)
        if (response.success) {
          this.maintenanceRecords = response.data || []
        }
      } catch (error) {
        console.warn('加载维护记录失败:', error)
      }
    },

    // 重置表单
    resetForm() {
      this.formData = {
        portalName: '',
        portalCode: '',
        portalType: '',
        portalStatus: 'INACTIVE',
        portalUrl: '',
        portalDescription: '',
        portalPriority: 5,
        portalOwnerName: '',
        portalAdminName: '',
        departmentName: '',
        companyName: '',
        portalLanguage: 'zh-CN',
        portalTimezone: 'Asia/Shanghai',
        portalTags: '',
        isActive: 0,
        isDefault: 0,
        isPublic: 0,
        isCustomizable: 1,
        accessCount: 0,
        remarks: ''
      }
      this.configData = {
        layoutMode: 'classic',
        showSidebar: true,
        showTopNav: true,
        primaryColor: '#409EFF',
        backgroundColor: '#FFFFFF',
        darkMode: false,
        enableSearch: true,
        enableNotification: true,
        enablePersonalization: true,
        enableSSO: false,
        enable2FA: false,
        sessionTimeout: 30
      }
    },

    // 保存
    async handleSave() {
      try {
        await this.$refs.basicForm.validate()
        
        const data = {
          ...this.formData,
          layoutConfig: JSON.stringify(this.configData)
        }
        
        if (this.mode === 'create') {
          await createServicePortal(data, this.tenantId)
          this.$message.success('创建成功')
        } else {
          await updateServicePortal(data, this.tenantId)
          this.$message.success('更新成功')
        }
        
        this.$emit('refresh')
        this.handleClose()
      } catch (error) {
        if (error.message) {
          this.$message.error('保存失败: ' + error.message)
        }
      }
    },

    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.activeTab = 'basic'
      this.$nextTick(() => {
        if (this.$refs.basicForm) {
          this.$refs.basicForm.clearValidate()
        }
      })
    },

    // 日志分页变化
    handleLogPageChange(page) {
      this.logPagination.current = page
      this.loadAccessLogs()
    },

    // 格式化访问量
    formatAccessCount(count) {
      return utils.formatAccessCount(count)
    },

    // 计算健康度
    calculateHealthScore(portal) {
      return utils.calculateHealthScore(portal)
    },

    // 格式化维护类型
    formatMaintenanceType(type) {
      const typeMap = {
        'ROUTINE': '例行维护',
        'EMERGENCY': '紧急维护',
        'UPGRADE': '升级维护',
        'REPAIR': '修复维护'
      }
      return typeMap[type] || type
    },

    // 获取维护类型颜色
    getMaintenanceTypeColor(type) {
      const colorMap = {
        'ROUTINE': 'primary',
        'EMERGENCY': 'danger',
        'UPGRADE': 'success',
        'REPAIR': 'warning'
      }
      return colorMap[type] || 'info'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-detail {
  .stat-card {
    text-align: center;
    
    .stat-item {
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #409EFF;
        margin-bottom: 8px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #666;
      }
    }
  }

  .pagination-container {
    margin-top: 16px;
    text-align: right;
  }
}
</style>
