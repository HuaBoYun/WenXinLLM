<template>
  <div class="system-configuration">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>系统配置</h2>
      <p>管理预算系统的全局配置参数、业务规则、系统设置和运行参数</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-check" @click="handleSaveAll">保存所有配置</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshDialog">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleExportDialog">导出配置</el-button>
            <el-button type="info" icon="el-icon-upload2" @click="handleImportDialog">导入配置</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh-left" @click="handleResetAll">重置配置</el-button>
            <el-button icon="el-icon-view" @click="handlePreviewDialog">预览配置</el-button>
            <el-button icon="el-icon-help" @click="handleHelpDialog">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 刷新确认弹窗 -->
    <el-dialog title="刷新配置" :visible.sync="refreshDialogVisible" width="400px">
      <span>确定要重新加载所有配置数据吗？未保存的修改将会丢失。</span>
      <span slot="footer">
        <el-button @click="refreshDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doRefresh">确定刷新</el-button>
      </span>
    </el-dialog>

    <!-- 导出配置弹窗 -->
    <el-dialog title="导出配置" :visible.sync="exportDialogVisible" width="500px">
      <p>将当前所有系统配置导出为 CSV 文件，可用于备份或迁移。</p>
      <p style="color:#909399;font-size:12px;">导出内容包含：配置编码、名称、类型、键值、默认值等信息。</p>
      <span slot="footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="warning" icon="el-icon-download" @click="doExport">确定导出</el-button>
      </span>
    </el-dialog>

    <!-- 导入配置弹窗 -->
    <el-dialog title="导入配置" :visible.sync="importDialogVisible" width="500px">
      <p>选择本地 JSON 配置文件进行导入，导入后将覆盖当前配置。</p>
      <p style="color:#E6A23C;font-size:12px;">⚠ 导入操作不可撤销，请确保文件格式正确。</p>
      <el-upload
        ref="importUpload"
        action="#"
        :auto-upload="false"
        :limit="1"
        accept=".json"
        :on-change="handleImportFileChange"
      >
        <el-button slot="trigger" type="primary">选择文件</el-button>
      </el-upload>
      <span slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="success" @click="doImport" :disabled="!importFile">确定导入</el-button>
      </span>
    </el-dialog>

    <!-- 预览配置弹窗 -->
    <el-dialog title="配置预览" :visible.sync="previewDialogVisible" width="700px">
      <el-table :data="previewTableData" size="small" max-height="400">
        <el-table-column prop="configType" label="分类" width="100" />
        <el-table-column prop="configName" label="配置名称" width="160" />
        <el-table-column prop="configKey" label="配置键" width="180" />
        <el-table-column prop="configValue" label="当前值" />
        <el-table-column prop="defaultValue" label="默认值" />
      </el-table>
      <span slot="footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="系统配置帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>配置说明</h4>
        <ul>
          <li><strong>基础配置</strong>：系统名称、版本、语言、时区、日期格式等基本参数。</li>
          <li><strong>业务配置</strong>：预算年度、周期、货币单位、审批规则等业务参数。</li>
          <li><strong>安全配置</strong>：密码策略、登录限制、会话超时等安全参数。</li>
          <li><strong>性能配置</strong>：缓存、分页、日志等性能调优参数。</li>
          <li><strong>通知配置</strong>：邮件、短信、微信等通知渠道配置。</li>
          <li><strong>集成配置</strong>：ERP、OA、BI 等外部系统集成配置。</li>
          <li><strong>高级配置</strong>：调试模式、API限流、连接池等高级参数。</li>
        </ul>
        <h4>操作说明</h4>
        <ul>
          <li>点击左侧分类切换配置项，修改后点击"保存"按钮保存当前分类。</li>
          <li>点击"保存所有配置"可一次性保存所有分类的修改。</li>
          <li>点击"重置"可将配置恢复为数据库中的值（不影响已保存数据）。</li>
        </ul>
      </div>
      <span slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button>
      </span>
    </el-dialog>

    <!-- 配置统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.totalConfigs }}</div>
            <div class="stat-label">配置项总数</div>
            <div class="stat-description">系统配置参数数量</div>
            <div class="stat-trend">
              <i class="el-icon-setting"></i>
              <span>全面配置</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-setting"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card modified-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.modifiedConfigs }}</div>
            <div class="stat-label">已修改配置</div>
            <div class="stat-description">已修改的配置项</div>
            <div class="stat-trend">
              <i class="el-icon-edit"></i>
              <span>待保存</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-edit"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card categories-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.categories }}</div>
            <div class="stat-label">配置分类</div>
            <div class="stat-description">配置分类数量</div>
            <div class="stat-trend">
              <i class="el-icon-menu"></i>
              <span>分类管理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-menu"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card last-update-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ configStats.lastUpdateDays }}</div>
            <div class="stat-label">最后更新</div>
            <div class="stat-description">距离上次更新天数</div>
            <div class="stat-trend">
              <i class="el-icon-time"></i>
              <span>天前</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 配置内容 -->
    <el-row :gutter="20">
      <!-- 左侧配置分类 -->
      <el-col :span="6">
        <el-card class="category-card" shadow="never">
          <div slot="header" class="card-header">
            <span>配置分类</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshCategories">刷新</el-button>
          </div>
          <el-menu
            :default-active="activeCategory"
            class="config-menu"
            @select="handleCategorySelect"
          >
            <el-menu-item index="basic">
              <i class="el-icon-setting"></i>
              <span>基础配置</span>
            </el-menu-item>
            <el-menu-item index="business">
              <i class="el-icon-s-cooperation"></i>
              <span>业务配置</span>
            </el-menu-item>
            <el-menu-item index="security">
              <i class="el-icon-lock"></i>
              <span>安全配置</span>
            </el-menu-item>
            <el-menu-item index="performance">
              <i class="el-icon-odometer"></i>
              <span>性能配置</span>
            </el-menu-item>
            <el-menu-item index="notification">
              <i class="el-icon-message"></i>
              <span>通知配置</span>
            </el-menu-item>
            <el-menu-item index="integration">
              <i class="el-icon-connection"></i>
              <span>集成配置</span>
            </el-menu-item>
            <el-menu-item index="advanced">
              <i class="el-icon-cpu"></i>
              <span>高级配置</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
      
      <!-- 右侧配置详情 -->
      <el-col :span="18">
        <el-card class="config-detail-card" shadow="never">
          <div slot="header" class="card-header">
            <span>{{ getCategoryTitle(activeCategory) }}</span>
            <div class="header-tools">
              <el-button icon="el-icon-check" size="mini" @click="handleSaveCategory">保存</el-button>
              <el-button icon="el-icon-refresh-left" size="mini" @click="handleResetCategory">重置</el-button>
            </div>
          </div>
          
          <div class="config-content">
            <!-- 基础配置 -->
            <div v-if="activeCategory === 'basic'" class="config-section">
              <el-form :model="basicConfig" label-width="150px" size="small">
                <el-form-item label="系统名称">
                  <el-input v-model="basicConfig.systemName" placeholder="请输入系统名称" />
                </el-form-item>
                <el-form-item label="系统版本">
                  <el-input v-model="basicConfig.systemVersion" placeholder="请输入系统版本" />
                </el-form-item>
                <el-form-item label="公司名称">
                  <el-input v-model="basicConfig.companyName" placeholder="请输入公司名称" />
                </el-form-item>
                <el-form-item label="系统Logo">
                  <el-upload
                    class="logo-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleLogoSuccess"
                  >
                    <img v-if="basicConfig.logoUrl" :src="basicConfig.logoUrl" class="logo">
                    <i v-else class="el-icon-plus logo-uploader-icon"></i>
                  </el-upload>
                </el-form-item>
                <el-form-item label="默认语言">
                  <el-select v-model="basicConfig.defaultLanguage" placeholder="请选择默认语言">
                    <el-option label="中文简体" value="zh-CN" />
                    <el-option label="中文繁体" value="zh-TW" />
                    <el-option label="English" value="en-US" />
                  </el-select>
                </el-form-item>
                <el-form-item label="时区设置">
                  <el-select v-model="basicConfig.timezone" placeholder="请选择时区">
                    <el-option label="北京时间 (UTC+8)" value="Asia/Shanghai" />
                    <el-option label="东京时间 (UTC+9)" value="Asia/Tokyo" />
                    <el-option label="纽约时间 (UTC-5)" value="America/New_York" />
                  </el-select>
                </el-form-item>
                <el-form-item label="日期格式">
                  <el-select v-model="basicConfig.dateFormat" placeholder="请选择日期格式">
                    <el-option label="YYYY-MM-DD" value="YYYY-MM-DD" />
                    <el-option label="MM/DD/YYYY" value="MM/DD/YYYY" />
                    <el-option label="DD/MM/YYYY" value="DD/MM/YYYY" />
                  </el-select>
                </el-form-item>
                <el-form-item label="数字格式">
                  <el-select v-model="basicConfig.numberFormat" placeholder="请选择数字格式">
                    <el-option label="1,234.56" value="1,234.56" />
                    <el-option label="1.234,56" value="1.234,56" />
                    <el-option label="1 234.56" value="1 234.56" />
                  </el-select>
                </el-form-item>
              </el-form>
            </div>

            <!-- 业务配置 -->
            <div v-if="activeCategory === 'business'" class="config-section">
              <el-form :model="businessConfig" label-width="150px" size="small">
                <el-form-item label="预算年度">
                  <el-date-picker
                    v-model="businessConfig.budgetYear"
                    type="year"
                    placeholder="选择预算年度"
                  />
                </el-form-item>
                <el-form-item label="预算周期">
                  <el-select v-model="businessConfig.budgetCycle" placeholder="请选择预算周期">
                    <el-option label="月度" value="MONTHLY" />
                    <el-option label="季度" value="QUARTERLY" />
                    <el-option label="年度" value="YEARLY" />
                  </el-select>
                </el-form-item>
                <el-form-item label="货币单位">
                  <el-select v-model="businessConfig.currency" placeholder="请选择货币单位">
                    <el-option label="人民币 (CNY)" value="CNY" />
                    <el-option label="美元 (USD)" value="USD" />
                    <el-option label="欧元 (EUR)" value="EUR" />
                  </el-select>
                </el-form-item>
                <el-form-item label="金额精度">
                  <el-input-number v-model="businessConfig.amountPrecision" :min="0" :max="6" />
                </el-form-item>
                <el-form-item label="自动审批">
                  <el-switch v-model="businessConfig.autoApproval" />
                </el-form-item>
                <el-form-item label="审批超时时间">
                  <el-input-number v-model="businessConfig.approvalTimeout" :min="1" :max="30" />
                  <span style="margin-left: 8px;">天</span>
                </el-form-item>
                <el-form-item label="预算锁定">
                  <el-switch v-model="businessConfig.budgetLock" />
                </el-form-item>
                <el-form-item label="数据保留期">
                  <el-input-number v-model="businessConfig.dataRetentionDays" :min="30" :max="3650" />
                  <span style="margin-left: 8px;">天</span>
                </el-form-item>
              </el-form>
            </div>

            <!-- 安全配置 -->
            <div v-if="activeCategory === 'security'" class="config-section">
              <el-form :model="securityConfig" label-width="150px" size="small">
                <el-form-item label="密码策略">
                  <el-checkbox-group v-model="securityConfig.passwordPolicy">
                    <el-checkbox label="UPPERCASE">包含大写字母</el-checkbox>
                    <el-checkbox label="LOWERCASE">包含小写字母</el-checkbox>
                    <el-checkbox label="NUMBERS">包含数字</el-checkbox>
                    <el-checkbox label="SPECIAL">包含特殊字符</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="密码最小长度">
                  <el-input-number v-model="securityConfig.minPasswordLength" :min="6" :max="20" />
                </el-form-item>
                <el-form-item label="密码有效期">
                  <el-input-number v-model="securityConfig.passwordExpireDays" :min="30" :max="365" />
                  <span style="margin-left: 8px;">天</span>
                </el-form-item>
                <el-form-item label="登录失败锁定">
                  <el-switch v-model="securityConfig.loginFailureLock" />
                </el-form-item>
                <el-form-item label="最大失败次数">
                  <el-input-number v-model="securityConfig.maxLoginFailures" :min="3" :max="10" />
                </el-form-item>
                <el-form-item label="锁定时间">
                  <el-input-number v-model="securityConfig.lockoutDuration" :min="5" :max="60" />
                  <span style="margin-left: 8px;">分钟</span>
                </el-form-item>
                <el-form-item label="会话超时">
                  <el-input-number v-model="securityConfig.sessionTimeout" :min="30" :max="480" />
                  <span style="margin-left: 8px;">分钟</span>
                </el-form-item>
                <el-form-item label="IP白名单">
                  <el-input
                    v-model="securityConfig.ipWhitelist"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入IP地址，每行一个"
                  />
                </el-form-item>
              </el-form>
            </div>

            <!-- 性能配置 -->
            <div v-if="activeCategory === 'performance'" class="config-section">
              <el-form :model="performanceConfig" label-width="150px" size="small">
                <el-form-item label="缓存启用">
                  <el-switch v-model="performanceConfig.cacheEnabled" />
                </el-form-item>
                <el-form-item label="缓存过期时间">
                  <el-input-number v-model="performanceConfig.cacheExpireTime" :min="60" :max="3600" />
                  <span style="margin-left: 8px;">秒</span>
                </el-form-item>
                <el-form-item label="分页大小">
                  <el-input-number v-model="performanceConfig.pageSize" :min="10" :max="100" />
                </el-form-item>
                <el-form-item label="最大导出行数">
                  <el-input-number v-model="performanceConfig.maxExportRows" :min="1000" :max="100000" />
                </el-form-item>
                <el-form-item label="查询超时时间">
                  <el-input-number v-model="performanceConfig.queryTimeout" :min="30" :max="300" />
                  <span style="margin-left: 8px;">秒</span>
                </el-form-item>
                <el-form-item label="连接池大小">
                  <el-input-number v-model="performanceConfig.connectionPoolSize" :min="10" :max="100" />
                </el-form-item>
                <el-form-item label="日志级别">
                  <el-select v-model="performanceConfig.logLevel" placeholder="请选择日志级别">
                    <el-option label="DEBUG" value="DEBUG" />
                    <el-option label="INFO" value="INFO" />
                    <el-option label="WARN" value="WARN" />
                    <el-option label="ERROR" value="ERROR" />
                  </el-select>
                </el-form-item>
                <el-form-item label="日志保留天数">
                  <el-input-number v-model="performanceConfig.logRetentionDays" :min="7" :max="90" />
                  <span style="margin-left: 8px;">天</span>
                </el-form-item>
              </el-form>
            </div>

            <!-- 通知配置 -->
            <div v-if="activeCategory === 'notification'" class="config-section">
              <el-form :model="notificationConfig" label-width="150px" size="small">
                <el-form-item label="邮件通知">
                  <el-switch v-model="notificationConfig.emailEnabled" />
                </el-form-item>
                <el-form-item label="SMTP服务器">
                  <el-input v-model="notificationConfig.smtpServer" placeholder="请输入SMTP服务器地址" />
                </el-form-item>
                <el-form-item label="SMTP端口">
                  <el-input-number v-model="notificationConfig.smtpPort" :min="25" :max="65535" />
                </el-form-item>
                <el-form-item label="发件人邮箱">
                  <el-input v-model="notificationConfig.senderEmail" placeholder="请输入发件人邮箱" />
                </el-form-item>
                <el-form-item label="短信通知">
                  <el-switch v-model="notificationConfig.smsEnabled" />
                </el-form-item>
                <el-form-item label="短信服务商">
                  <el-select v-model="notificationConfig.smsProvider" placeholder="请选择短信服务商">
                    <el-option label="阿里云" value="ALIYUN" />
                    <el-option label="腾讯云" value="TENCENT" />
                    <el-option label="华为云" value="HUAWEI" />
                  </el-select>
                </el-form-item>
                <el-form-item label="微信通知">
                  <el-switch v-model="notificationConfig.wechatEnabled" />
                </el-form-item>
                <el-form-item label="企业微信ID">
                  <el-input v-model="notificationConfig.wechatCorpId" placeholder="请输入企业微信ID" />
                </el-form-item>
              </el-form>
            </div>

            <!-- 集成配置 -->
            <div v-if="activeCategory === 'integration'" class="config-section">
              <el-form :model="integrationConfig" label-width="150px" size="small">
                <el-form-item label="ERP集成">
                  <el-switch v-model="integrationConfig.erpEnabled" />
                </el-form-item>
                <el-form-item label="ERP系统">
                  <el-select v-model="integrationConfig.erpSystem" placeholder="请选择ERP系统">
                    <el-option label="SAP" value="SAP" />
                    <el-option label="Oracle" value="ORACLE" />
                    <el-option label="用友" value="YONYOU" />
                    <el-option label="金蝶" value="KINGDEE" />
                  </el-select>
                </el-form-item>
                <el-form-item label="ERP接口地址">
                  <el-input v-model="integrationConfig.erpApiUrl" placeholder="请输入ERP接口地址" />
                </el-form-item>
                <el-form-item label="OA集成">
                  <el-switch v-model="integrationConfig.oaEnabled" />
                </el-form-item>
                <el-form-item label="OA系统">
                  <el-select v-model="integrationConfig.oaSystem" placeholder="请选择OA系统">
                    <el-option label="钉钉" value="DINGTALK" />
                    <el-option label="企业微信" value="WECHAT_WORK" />
                    <el-option label="飞书" value="FEISHU" />
                  </el-select>
                </el-form-item>
                <el-form-item label="BI集成">
                  <el-switch v-model="integrationConfig.biEnabled" />
                </el-form-item>
                <el-form-item label="BI系统">
                  <el-select v-model="integrationConfig.biSystem" placeholder="请选择BI系统">
                    <el-option label="Tableau" value="TABLEAU" />
                    <el-option label="Power BI" value="POWERBI" />
                    <el-option label="帆软" value="FINEREPORT" />
                  </el-select>
                </el-form-item>
              </el-form>
            </div>

            <!-- 高级配置 -->
            <div v-if="activeCategory === 'advanced'" class="config-section">
              <el-form :model="advancedConfig" label-width="150px" size="small">
                <el-form-item label="调试模式">
                  <el-switch v-model="advancedConfig.debugMode" />
                </el-form-item>
                <el-form-item label="API限流">
                  <el-switch v-model="advancedConfig.rateLimitEnabled" />
                </el-form-item>
                <el-form-item label="每分钟请求数">
                  <el-input-number v-model="advancedConfig.requestsPerMinute" :min="60" :max="10000" />
                </el-form-item>
                <el-form-item label="数据库连接池">
                  <el-input-number v-model="advancedConfig.dbPoolSize" :min="5" :max="50" />
                </el-form-item>
                <el-form-item label="Redis连接池">
                  <el-input-number v-model="advancedConfig.redisPoolSize" :min="5" :max="50" />
                </el-form-item>
                <el-form-item label="文件上传大小">
                  <el-input-number v-model="advancedConfig.maxFileSize" :min="1" :max="100" />
                  <span style="margin-left: 8px;">MB</span>
                </el-form-item>
                <el-form-item label="临时文件清理">
                  <el-switch v-model="advancedConfig.tempFileCleanup" />
                </el-form-item>
                <el-form-item label="清理间隔">
                  <el-input-number v-model="advancedConfig.cleanupInterval" :min="1" :max="24" />
                  <span style="margin-left: 8px;">小时</span>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'

// 每个分类下的配置键定义（与数据库 CONFIG_KEY 对应）
const CONFIG_KEYS = {
  basic: ['systemName', 'systemVersion', 'companyName', 'logoUrl', 'defaultLanguage', 'timezone', 'dateFormat', 'numberFormat'],
  business: ['budgetYear', 'budgetCycle', 'currency', 'amountPrecision', 'autoApproval', 'approvalTimeout', 'budgetLock', 'dataRetentionDays'],
  security: ['passwordPolicy', 'minPasswordLength', 'passwordExpireDays', 'loginFailureLock', 'maxLoginFailures', 'lockoutDuration', 'sessionTimeout', 'ipWhitelist'],
  performance: ['cacheEnabled', 'cacheExpireTime', 'pageSize', 'maxExportRows', 'queryTimeout', 'connectionPoolSize', 'logLevel', 'logRetentionDays'],
  notification: ['emailEnabled', 'smtpServer', 'smtpPort', 'senderEmail', 'smsEnabled', 'smsProvider', 'wechatEnabled', 'wechatCorpId'],
  integration: ['erpEnabled', 'erpSystem', 'erpApiUrl', 'oaEnabled', 'oaSystem', 'biEnabled', 'biSystem'],
  advanced: ['debugMode', 'rateLimitEnabled', 'requestsPerMinute', 'dbPoolSize', 'redisPoolSize', 'maxFileSize', 'tempFileCleanup', 'cleanupInterval']
}

// 配置键的中文名称
const CONFIG_NAMES = {
  systemName: '系统名称', systemVersion: '系统版本', companyName: '公司名称', logoUrl: '系统Logo',
  defaultLanguage: '默认语言', timezone: '时区设置', dateFormat: '日期格式', numberFormat: '数字格式',
  budgetYear: '预算年度', budgetCycle: '预算周期', currency: '货币单位', amountPrecision: '金额精度',
  autoApproval: '自动审批', approvalTimeout: '审批超时时间', budgetLock: '预算锁定', dataRetentionDays: '数据保留期',
  passwordPolicy: '密码策略', minPasswordLength: '密码最小长度', passwordExpireDays: '密码有效期',
  loginFailureLock: '登录失败锁定', maxLoginFailures: '最大失败次数', lockoutDuration: '锁定时间', sessionTimeout: '会话超时', ipWhitelist: 'IP白名单',
  cacheEnabled: '缓存启用', cacheExpireTime: '缓存过期时间', pageSize: '分页大小', maxExportRows: '最大导出行数',
  queryTimeout: '查询超时时间', connectionPoolSize: '连接池大小', logLevel: '日志级别', logRetentionDays: '日志保留天数',
  emailEnabled: '邮件通知', smtpServer: 'SMTP服务器', smtpPort: 'SMTP端口', senderEmail: '发件人邮箱',
  smsEnabled: '短信通知', smsProvider: '短信服务商', wechatEnabled: '微信通知', wechatCorpId: '企业微信ID',
  erpEnabled: 'ERP集成', erpSystem: 'ERP系统', erpApiUrl: 'ERP接口地址', oaEnabled: 'OA集成', oaSystem: 'OA系统', biEnabled: 'BI集成', biSystem: 'BI系统',
  debugMode: '调试模式', rateLimitEnabled: 'API限流', requestsPerMinute: '每分钟请求数', dbPoolSize: '数据库连接池',
  redisPoolSize: 'Redis连接池', maxFileSize: '文件上传大小', tempFileCleanup: '临时文件清理', cleanupInterval: '清理间隔'
}

export default {
  name: 'SystemConfiguration',
  data() {
    return {
      // 统计数据
      configStats: {
        totalConfigs: 0,
        modifiedConfigs: 0,
        categories: 0,
        lastUpdateDays: 0
      },
      // 原始配置列表（来自数据库，用于保存时携带configId）
      rawConfigList: [],
      // 当前活跃分类
      activeCategory: 'basic',
      // 基础配置
      basicConfig: {
        systemName: '', systemVersion: '', companyName: '', logoUrl: '',
        defaultLanguage: 'zh-CN', timezone: 'Asia/Shanghai', dateFormat: 'YYYY-MM-DD', numberFormat: '1,234.56'
      },
      // 业务配置
      businessConfig: {
        budgetYear: null, budgetCycle: 'MONTHLY', currency: 'CNY', amountPrecision: 2,
        autoApproval: false, approvalTimeout: 7, budgetLock: false, dataRetentionDays: 365
      },
      // 安全配置
      securityConfig: {
        passwordPolicy: ['UPPERCASE', 'LOWERCASE', 'NUMBERS'], minPasswordLength: 8,
        passwordExpireDays: 90, loginFailureLock: true, maxLoginFailures: 5,
        lockoutDuration: 30, sessionTimeout: 120, ipWhitelist: ''
      },
      // 性能配置
      performanceConfig: {
        cacheEnabled: true, cacheExpireTime: 3600, pageSize: 20, maxExportRows: 10000,
        queryTimeout: 60, connectionPoolSize: 20, logLevel: 'INFO', logRetentionDays: 30
      },
      // 通知配置
      notificationConfig: {
        emailEnabled: true, smtpServer: '', smtpPort: 587, senderEmail: '',
        smsEnabled: false, smsProvider: '', wechatEnabled: false, wechatCorpId: ''
      },
      // 集成配置
      integrationConfig: {
        erpEnabled: false, erpSystem: '', erpApiUrl: '',
        oaEnabled: false, oaSystem: '', biEnabled: false, biSystem: ''
      },
      // 高级配置
      advancedConfig: {
        debugMode: false, rateLimitEnabled: true, requestsPerMinute: 1000,
        dbPoolSize: 20, redisPoolSize: 10, maxFileSize: 10, tempFileCleanup: true, cleanupInterval: 6
      },
      // 弹窗控制
      refreshDialogVisible: false,
      exportDialogVisible: false,
      importDialogVisible: false,
      previewDialogVisible: false,
      helpDialogVisible: false,
      importFile: null,
      previewTableData: []
    }
  },
  
  created() {
    this.loadConfigurations()
    this.getConfigStats()
  },
  
  methods: {
    // 将后端返回的配置列表（按type分组的configKey/configValue）映射到表单对象
    mapConfigListToForm(configList, category) {
      const formData = {}
      if (!configList || !configList.length) return formData
      configList.forEach(item => {
        const key = item.configKey
        let val = item.configValue
        // 类型转换
        if (val === 'true') val = true
        else if (val === 'false') val = false
        else if (val !== null && val !== '' && !isNaN(Number(val)) && typeof val === 'string' && val.trim() !== '') {
          // 只对纯数字字符串转换（排除日期等）
          if (['amountPrecision', 'approvalTimeout', 'dataRetentionDays', 'minPasswordLength', 'passwordExpireDays',
            'maxLoginFailures', 'lockoutDuration', 'sessionTimeout', 'cacheExpireTime', 'pageSize', 'maxExportRows',
            'queryTimeout', 'connectionPoolSize', 'logRetentionDays', 'smtpPort', 'requestsPerMinute', 'dbPoolSize',
            'redisPoolSize', 'maxFileSize', 'cleanupInterval'].includes(key)) {
            val = Number(val)
          }
        }
        // 数组类型（passwordPolicy）
        if (key === 'passwordPolicy' && typeof val === 'string') {
          try { val = JSON.parse(val) } catch (e) { val = val.split(',').filter(Boolean) }
        }
        formData[key] = val
      })
      return formData
    },

    // 将表单对象转换为后端期望的 List<BudgetSystemConfigEntity>
    formToConfigList(category, formData) {
      const keys = CONFIG_KEYS[category] || []
      return keys.map(key => {
        // 查找已有的配置项（携带configId用于更新）
        const existing = this.rawConfigList.find(c => c.configType === category && c.configKey === key)
        let val = formData[key]
        // 数组转JSON字符串
        if (Array.isArray(val)) val = JSON.stringify(val)
        else if (typeof val === 'boolean') val = String(val)
        else if (val === null || val === undefined) val = ''
        else val = String(val)
        return {
          configId: existing ? existing.configId : '',
          configCode: existing ? existing.configCode : (category + '_' + key),
          configName: CONFIG_NAMES[key] || key,
          configType: category,
          configKey: key,
          configValue: val,
          valueType: existing ? existing.valueType : 'STRING',
          defaultValue: existing ? existing.defaultValue : val,
          isEnabled: 1,
          isDeleted: 0
        }
      })
    },

    // 加载配置（从数据库）
    async loadConfigurations() {
      try {
        const response = await budgetSystemApi.getSystemConfigurations()
        if (response.code === 1 && response.data) {
          // 后端返回 { basic: [...], business: [...], ... }，每项是配置实体列表
          const grouped = response.data
          // 保存原始列表用于后续保存时携带configId
          this.rawConfigList = []
          Object.keys(grouped).forEach(type => {
            if (Array.isArray(grouped[type])) {
              grouped[type].forEach(item => this.rawConfigList.push(item))
            }
          })
          // 映射到各表单
          if (grouped.basic) this.basicConfig = Object.assign({}, this.basicConfig, this.mapConfigListToForm(grouped.basic, 'basic'))
          if (grouped.business) this.businessConfig = Object.assign({}, this.businessConfig, this.mapConfigListToForm(grouped.business, 'business'))
          if (grouped.security) this.securityConfig = Object.assign({}, this.securityConfig, this.mapConfigListToForm(grouped.security, 'security'))
          if (grouped.performance) this.performanceConfig = Object.assign({}, this.performanceConfig, this.mapConfigListToForm(grouped.performance, 'performance'))
          if (grouped.notification) this.notificationConfig = Object.assign({}, this.notificationConfig, this.mapConfigListToForm(grouped.notification, 'notification'))
          if (grouped.integration) this.integrationConfig = Object.assign({}, this.integrationConfig, this.mapConfigListToForm(grouped.integration, 'integration'))
          if (grouped.advanced) this.advancedConfig = Object.assign({}, this.advancedConfig, this.mapConfigListToForm(grouped.advanced, 'advanced'))
        }
      } catch (error) {
        this.$message.error('加载配置失败')
      }
    },

    // 获取统计数据
    async getConfigStats() {
      try {
        const response = await budgetSystemApi.getConfigStats()
        if (response.code === 1 && response.data) {
          const d = response.data
          this.configStats.totalConfigs = d.totalConfigs || 0
          // 已修改配置：用enabledConfigs近似
          this.configStats.modifiedConfigs = d.enabledConfigs || 0
          // 配置分类数：typeDistribution的key数量
          this.configStats.categories = d.typeDistribution ? Object.keys(d.typeDistribution).length : 0
          // 最后更新天数：暂无接口，显示0
          this.configStats.lastUpdateDays = 0
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      }
    },

    // 分类选择
    handleCategorySelect(category) {
      this.activeCategory = category
    },

    // 获取分类标题
    getCategoryTitle(category) {
      const titleMap = {
        basic: '基础配置', business: '业务配置', security: '安全配置',
        performance: '性能配置', notification: '通知配置', integration: '集成配置', advanced: '高级配置'
      }
      return titleMap[category] || '配置'
    },

    // 保存所有配置（转为配置项列表发送）
    async handleSaveAll() {
      try {
        const allList = [
          ...this.formToConfigList('basic', this.basicConfig),
          ...this.formToConfigList('business', this.businessConfig),
          ...this.formToConfigList('security', this.securityConfig),
          ...this.formToConfigList('performance', this.performanceConfig),
          ...this.formToConfigList('notification', this.notificationConfig),
          ...this.formToConfigList('integration', this.integrationConfig),
          ...this.formToConfigList('advanced', this.advancedConfig)
        ]
        const response = await budgetSystemApi.saveSystemConfigurations(allList)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.loadConfigurations()
          this.getConfigStats()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 保存当前分类配置
    async handleSaveCategory() {
      try {
        const formData = this.getCurrentCategoryFormData()
        const configList = this.formToConfigList(this.activeCategory, formData)
        const response = await budgetSystemApi.saveCategoryConfiguration(this.activeCategory, configList)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.loadConfigurations()
          this.getConfigStats()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 获取当前分类的表单数据
    getCurrentCategoryFormData() {
      const map = {
        basic: this.basicConfig, business: this.businessConfig, security: this.securityConfig,
        performance: this.performanceConfig, notification: this.notificationConfig,
        integration: this.integrationConfig, advanced: this.advancedConfig
      }
      return map[this.activeCategory] || {}
    },

    // 重置分类配置（从数据库重新加载）
    handleResetCategory() {
      this.$confirm('确定重置当前分类的配置吗？未保存的修改将丢失。', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.loadConfigurations()
        this.$message.success('重置成功')
      })
    },

    // 重置所有配置
    handleResetAll() {
      this.$confirm('确定重置所有配置吗？未保存的修改将丢失。', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.loadConfigurations()
        this.$message.success('重置成功')
      })
    },

    // 刷新按钮 -> 弹窗
    handleRefreshDialog() { this.refreshDialogVisible = true },
    doRefresh() {
      this.refreshDialogVisible = false
      this.loadConfigurations()
      this.getConfigStats()
      this.$message.success('配置已刷新')
    },

    // 导出按钮 -> 弹窗
    handleExportDialog() { this.exportDialogVisible = true },
    async doExport() {
      try {
        const response = await budgetSystemApi.exportSystemConfigurations()
        // 后端直接返回文件流
        const blob = new Blob([response], { type: 'text/csv;charset=utf-8;' })
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = 'system_config.csv'
        a.click()
        URL.revokeObjectURL(url)
        this.exportDialogVisible = false
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导入按钮 -> 弹窗
    handleImportDialog() {
      this.importFile = null
      this.importDialogVisible = true
    },
    handleImportFileChange(file) {
      this.importFile = file.raw
    },
    async doImport() {
      if (!this.importFile) return
      const reader = new FileReader()
      reader.onload = async (event) => {
        try {
          const configs = JSON.parse(event.target.result)
          // 导入的JSON应为配置项列表
          const list = Array.isArray(configs) ? configs : []
          const response = await budgetSystemApi.saveSystemConfigurations(list)
          if (response.code === 1) {
            this.$message.success('导入成功')
            this.importDialogVisible = false
            this.loadConfigurations()
            this.getConfigStats()
          } else {
            this.$message.error(response.msg || '导入失败')
          }
        } catch (error) {
          this.$message.error('导入失败：文件格式错误')
        }
      }
      reader.readAsText(this.importFile)
    },

    // 预览配置 -> 弹窗
    handlePreviewDialog() {
      this.previewTableData = this.rawConfigList.map(item => ({
        configType: this.getCategoryTitle(item.configType),
        configName: item.configName,
        configKey: item.configKey,
        configValue: item.configValue,
        defaultValue: item.defaultValue
      }))
      this.previewDialogVisible = true
    },

    // 帮助 -> 弹窗
    handleHelpDialog() { this.helpDialogVisible = true },

    // 刷新分类
    refreshCategories() {
      this.loadConfigurations()
    },

    // Logo上传成功
    handleLogoSuccess(response) {
      this.basicConfig.logoUrl = response.data.url
      this.$message.success('Logo上传成功')
    }
  }
}
</script>
