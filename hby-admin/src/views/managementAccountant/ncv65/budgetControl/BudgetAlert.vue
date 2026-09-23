<template>
  <div class="budget-alert">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算警报管理</h2>
      <p>管理预算警报配置和通知，提供实时警报监控和处理</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateAlert">创建警报</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportAlerts">导入警报</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchAcknowledge">批量确认</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportAlerts">导出警报</el-button>
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

    <!-- 警报统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ alertStats.totalAlerts }}</div>
            <div class="stat-label">警报总数</div>
            <div class="stat-description">所有警报规则数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bell"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ alertStats.activeAlerts }}</div>
            <div class="stat-label">活跃警报</div>
            <div class="stat-description">当前生效的警报规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="alertStats.activeRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card today-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ alertStats.todayAlerts }}</div>
            <div class="stat-label">今日警报</div>
            <div class="stat-description">今天触发的警报数量</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="alertStats.todayRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning-outline"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card unread-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ alertStats.unreadAlerts }}</div>
            <div class="stat-label">未读警报</div>
            <div class="stat-description">未确认的警报消息</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="alertStats.unreadRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-message"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="警报名称">
          <el-input
            v-model="queryForm.alertName"
            placeholder="请输入警报名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="警报类型">
          <el-select
            v-model="queryForm.alertType"
            placeholder="请选择警报类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in alertTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="严重程度">
          <el-select
            v-model="queryForm.severity"
            placeholder="请选择严重程度"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in severityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="警报状态">
          <el-select
            v-model="queryForm.alertStatus"
            placeholder="请选择警报状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in alertStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="确认状态">
          <el-select
            v-model="queryForm.acknowledgeStatus"
            placeholder="请选择确认状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in acknowledgeStatusOptions"
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
    </el-card>

    <!-- 警报列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算警报列表</span>
        <div class="table-tools">
          <el-tooltip content="实时推送" placement="top">
            <el-switch
              v-model="realTimePush"
              active-text="实时推送"
              @change="handlePushChange"
            />
          </el-tooltip>
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
        :data="alertList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.alertCode" prop="alertCode" label="警报编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.alertName" prop="alertName" label="警报名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.alertType" prop="alertType" label="警报类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getAlertTypeColor(scope.row.alertType)">
              {{ getAlertTypeText(scope.row.alertType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.severity" prop="severity" label="严重程度" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getSeverityColor(scope.row.severity)">
              {{ getSeverityText(scope.row.severity) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.triggerCondition" prop="triggerCondition" label="触发条件" width="150" show-overflow-tooltip />
        
        <el-table-column v-if="columnVisible.triggerCount" prop="triggerCount" label="触发次数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.triggerCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.notificationCount" prop="notificationCount" label="通知次数" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <span class="number-text">{{ scope.row.notificationCount || 0 }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.alertStatus" prop="alertStatus" label="警报状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAlertStatusType(scope.row.alertStatus)" size="mini">
              {{ getAlertStatusText(scope.row.alertStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.acknowledgeStatus" prop="acknowledgeStatus" label="确认状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAcknowledgeStatusType(scope.row.acknowledgeStatus)" size="mini">
              {{ getAcknowledgeStatusText(scope.row.acknowledgeStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.lastTriggerTime" prop="lastTriggerTime" label="最后触发" width="150" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <div @click.stop>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-view"
                @click="handleView(scope.row)"
              >详情</el-button>
              <el-button
                v-if="canAcknowledge(scope.row)"
                type="text"
                size="mini"
                icon="el-icon-check"
                class="success-text"
                @click="handleAcknowledge(scope.row)"
              >确认</el-button>
              <el-button
                v-if="canEdit(scope.row)"
                type="text"
                size="mini"
                icon="el-icon-edit"
                @click="handleEdit(scope.row)"
              >编辑</el-button>
              <el-dropdown
                trigger="click"
                @command="(command) => handleCommand(command, scope.row)"
              >
                <el-button type="text" size="mini">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="test" icon="el-icon-s-data">测试警报</el-dropdown-item>
                  <el-dropdown-item command="history" icon="el-icon-time">触发历史</el-dropdown-item>
                  <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                  <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                  <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
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

    <!-- 新增/编辑警报对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="alertForm"
        :model="alertForm"
        :rules="alertRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="警报名称" prop="alertName">
              <el-input
                v-model="alertForm.alertName"
                placeholder="请输入警报名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="警报编码" prop="alertCode">
              <el-input
                v-model="alertForm.alertCode"
                placeholder="请输入警报编码"
                :disabled="!!alertForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="警报类型" prop="alertType">
              <el-select
                v-model="alertForm.alertType"
                placeholder="请选择警报类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in alertTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="严重程度" prop="severity">
              <el-select
                v-model="alertForm.severity"
                placeholder="请选择严重程度"
                style="width: 100%"
              >
                <el-option
                  v-for="item in severityOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="警报描述" prop="alertDescription">
          <el-input
            v-model="alertForm.alertDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入警报描述"
          />
        </el-form-item>
        
        <el-form-item label="触发条件" prop="triggerCondition">
          <el-input
            v-model="alertForm.triggerCondition"
            placeholder="请输入触发条件表达式"
          />
        </el-form-item>
        
        <!-- 通知配置 -->
        <el-form-item label="通知配置">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="通知方式" prop="notificationMethods">
                <el-checkbox-group v-model="alertForm.notificationMethods">
                  <el-checkbox label="EMAIL">邮件</el-checkbox>
                  <el-checkbox label="SMS">短信</el-checkbox>
                  <el-checkbox label="SYSTEM">系统通知</el-checkbox>
                  <el-checkbox label="WECHAT">微信</el-checkbox>
                  <el-checkbox label="WEBHOOK">Webhook</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="通知人员" prop="notificationUsers">
                <el-select
                  v-model="alertForm.notificationUsers"
                  placeholder="请选择通知人员"
                  multiple
                  style="width: 100%"
                >
                  <el-option
                    v-for="user in userOptions"
                    :key="user.id"
                    :label="user.name"
                    :value="user.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <!-- 高级配置 -->
        <el-form-item label="高级配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="重复间隔" prop="repeatInterval">
                <el-input-number
                  v-model="alertForm.repeatInterval"
                  :min="0"
                  placeholder="分钟"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="最大重复" prop="maxRepeat">
                <el-input-number
                  v-model="alertForm.maxRepeat"
                  :min="1"
                  :max="100"
                  placeholder="次数"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="静默时间" prop="silenceTime">
                <el-input-number
                  v-model="alertForm.silenceTime"
                  :min="0"
                  placeholder="小时"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        
        <el-form-item label="警报配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="alertForm.isActive">启用警报</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="alertForm.autoAcknowledge">自动确认</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="alertForm.logTrigger">记录触发日志</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleTestAlert">测试警报</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存警报</el-button>
      </div>
    </el-dialog>

    <!-- 警报确认对话框 -->
    <el-dialog
      title="警报确认"
      :visible.sync="acknowledgeDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="alert-acknowledge" v-if="currentAlert">
        <div class="alert-info">
          <el-alert
            :title="currentAlert.alertName"
            :type="getSeverityColor(currentAlert.severity)"
            :description="currentAlert.alertDescription"
            show-icon
          />
        </div>

        <el-form :model="acknowledgeForm" label-width="120px" size="small">
          <el-form-item label="确认说明" prop="acknowledgeRemark">
            <el-input
              v-model="acknowledgeForm.acknowledgeRemark"
              type="textarea"
              :rows="4"
              placeholder="请输入确认说明"
            />
          </el-form-item>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="acknowledgeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAcknowledge">确认警报</el-button>
      </div>
    </el-dialog>

    <!-- 警报升级对话框 -->
    <el-dialog
      title="警报升级"
      :visible.sync="escalateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="alert-escalate" v-if="currentAlert">
        <div class="alert-info">
          <h4>当前警报信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="警报名称">{{ currentAlert.alertName }}</el-descriptions-item>
            <el-descriptions-item label="当前级别">
              <el-tag :type="getSeverityColor(currentAlert.severity)" size="mini">
                {{ getSeverityText(currentAlert.severity) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="触发次数">{{ currentAlert.triggerCount }}</el-descriptions-item>
            <el-descriptions-item label="最后触发">{{ currentAlert.lastTriggerTime }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <el-form :model="escalateForm" label-width="120px" size="small">
          <el-form-item label="升级级别" prop="newSeverity">
            <el-select
              v-model="escalateForm.newSeverity"
              placeholder="请选择升级级别"
              style="width: 100%"
            >
              <el-option
                v-for="item in severityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :disabled="item.value === currentAlert.severity"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="升级原因" prop="escalateReason">
            <el-input
              v-model="escalateForm.escalateReason"
              type="textarea"
              :rows="4"
              placeholder="请输入升级原因"
            />
          </el-form-item>

          <el-form-item label="通知升级">
            <el-checkbox v-model="escalateForm.notifyEscalation">发送升级通知</el-checkbox>
          </el-form-item>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="escalateDialogVisible = false">取消</el-button>
        <el-button type="warning" @click="handleSubmitEscalate">确认升级</el-button>
      </div>
    </el-dialog>

    <!-- 警报详情抽屉 -->
    <el-drawer
      title="警报详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="alert-detail" v-if="currentAlert">
        <!-- 基本信息 -->
        <div class="detail-section">
          <h3>基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="警报编码">{{ currentAlert.alertCode }}</el-descriptions-item>
            <el-descriptions-item label="警报名称">{{ currentAlert.alertName }}</el-descriptions-item>
            <el-descriptions-item label="警报类型">
              <el-tag :type="getAlertTypeColor(currentAlert.alertType)" size="mini">
                {{ getAlertTypeText(currentAlert.alertType) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="严重程度">
              <el-tag :type="getSeverityColor(currentAlert.severity)" size="mini">
                {{ getSeverityText(currentAlert.severity) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="警报状态">
              <el-tag :type="getAlertStatusType(currentAlert.alertStatus)" size="mini">
                {{ getAlertStatusText(currentAlert.alertStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="确认状态">
              <el-tag :type="getAcknowledgeStatusType(currentAlert.acknowledgeStatus)" size="mini">
                {{ getAcknowledgeStatusText(currentAlert.acknowledgeStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 触发统计 -->
        <div class="detail-section">
          <h3>触发统计</h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ currentAlert.triggerCount || 0 }}</div>
                <div class="stat-label">总触发次数</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ currentAlert.notificationCount || 0 }}</div>
                <div class="stat-label">通知次数</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-value">{{ currentAlert.acknowledgeCount || 0 }}</div>
                <div class="stat-label">确认次数</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 触发历史图表 -->
        <div class="detail-section">
          <h3>触发趋势</h3>
          <div id="alertTrendChart" class="chart-container"></div>
        </div>

        <!-- 最近触发记录 -->
        <div class="detail-section">
          <h3>最近触发记录</h3>
          <el-table :data="alertTriggerHistory" border size="mini">
            <el-table-column prop="triggerTime" label="触发时间" width="150" />
            <el-table-column prop="triggerValue" label="触发值" width="100" />
            <el-table-column prop="triggerCondition" label="触发条件" show-overflow-tooltip />
            <el-table-column prop="notificationStatus" label="通知状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.notificationStatus === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                  {{ scope.row.notificationStatus === 'SUCCESS' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 操作按钮 -->
        <div class="detail-actions">
          <el-button type="primary" @click="handleAcknowledge(currentAlert)">确认警报</el-button>
          <el-button type="warning" @click="handleEscalate(currentAlert)">升级警报</el-button>
          <el-button type="info" @click="handleTest(currentAlert)">测试警报</el-button>
          <el-button @click="detailDrawerVisible = false">关闭</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 列设置对话框 -->
    <el-dialog title="列设置" :visible.sync="columnSettingVisible" width="400px">
      <el-checkbox-group v-model="columnChecked">
        <el-row :gutter="10">
          <el-col v-for="col in columnOptions" :key="col.key" :span="12">
            <el-checkbox :label="col.key" style="margin-bottom: 8px;">{{ col.label }}</el-checkbox>
          </el-col>
        </el-row>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleResetColumns">重置</el-button>
        <el-button @click="columnSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApplyColumns">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入警报对话框 -->
    <el-dialog title="导入警报规则" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
      <el-upload
        ref="importUpload"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleImportFileChange"
        :limit="1"
        accept=".xlsx,.xls"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传 xlsx/xls 文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="handleSubmitImport">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 触发历史对话框 -->
    <el-dialog :title="'触发历史 - ' + historyAlertName" :visible.sync="historyDialogVisible" width="800px">
      <el-table :data="alertTriggerHistory" v-loading="historyLoading" border size="mini" max-height="400">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="triggerTime" label="触发时间" width="160" align="center" />
        <el-table-column prop="alertLevel" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.alertLevel === 'CRITICAL' ? 'danger' : scope.row.alertLevel === 'ERROR' ? 'warning' : 'info'" size="mini">
              {{ scope.row.alertLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerCondition" label="触发条件" show-overflow-tooltip />
        <el-table-column prop="triggerValue" label="触发值" width="100" align="center" />
        <el-table-column prop="thresholdValue" label="阈值" width="100" align="center" />
        <el-table-column prop="notificationStatus" label="通知状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.notificationStatus === 'SUCCESS' ? 'success' : 'danger'" size="mini">
              {{ scope.row.notificationStatus === 'SUCCESS' ? '已通知' : '未通知' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetAlertApi } from '@/api/managementAccountant/ncv65/budgetControl'
import * as echarts from 'echarts'

export default {
  name: 'BudgetAlert',
  data() {
    return {
      // 查询参数
      queryForm: {
        alertName: '',
        alertType: '',
        severity: '',
        alertStatus: '',
        acknowledgeStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      alertList: [],
      total: 0,
      selectedRows: [],
      
      // 实时推送
      realTimePush: false,
      pushSocket: null,
      
      // 统计数据
      alertStats: {
        totalAlerts: 0,
        activeAlerts: 0,
        todayAlerts: 0,
        unreadAlerts: 0,
        activeRate: 0,
        todayRate: 0,
        unreadRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      acknowledgeDialogVisible: false,
      escalateDialogVisible: false,
      detailDrawerVisible: false,
      currentAlert: null,
      alertForm: {
        id: null,
        alertName: '',
        alertCode: '',
        alertType: '',
        severity: '',
        alertDescription: '',
        triggerCondition: '',
        notificationMethods: [],
        notificationUsers: [],
        repeatInterval: 30,
        maxRepeat: 3,
        silenceTime: 2,
        isActive: true,
        autoAcknowledge: false,
        logTrigger: true
      },
      alertRules: {
        alertName: [
          { required: true, message: '请输入警报名称', trigger: 'blur' }
        ],
        alertCode: [
          { required: true, message: '请输入警报编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '警报编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        alertType: [
          { required: true, message: '请选择警报类型', trigger: 'change' }
        ],
        severity: [
          { required: true, message: '请选择严重程度', trigger: 'change' }
        ]
      },
      
      // 警报确认
      acknowledgeForm: {
        acknowledgeRemark: ''
      },

      // 警报升级
      escalateForm: {
        newSeverity: '',
        escalateReason: '',
        notifyEscalation: true
      },

      // 触发历史
      alertTriggerHistory: [],
      
      // 选项数据
      alertTypeOptions: [
        { value: 'BUDGET_EXCEED', label: '预算超支' },
        { value: 'EXECUTION_ABNORMAL', label: '执行异常' },
        { value: 'APPROVAL_TIMEOUT', label: '审批超时' },
        { value: 'DATA_ANOMALY', label: '数据异常' },
        { value: 'SYSTEM_ERROR', label: '系统错误' },
        { value: 'CUSTOM', label: '自定义警报' }
      ],
      severityOptions: [
        { value: 'INFO', label: '信息' },
        { value: 'WARNING', label: '警告' },
        { value: 'ERROR', label: '错误' },
        { value: 'CRITICAL', label: '严重' }
      ],
      alertStatusOptions: [
        { value: 'PENDING', label: '待处理' },
        { value: 'ACKNOWLEDGED', label: '已确认' },
        { value: 'PROCESSING', label: '处理中' },
        { value: 'RESOLVED', label: '已解决' },
        { value: 'ESCALATED', label: '已升级' },
        { value: 'CLOSED', label: '已关闭' }
      ],
      acknowledgeStatusOptions: [
        { value: 'UNREAD', label: '未读' },
        { value: 'READ', label: '已读' },
        { value: 'ACKNOWLEDGED', label: '已确认' }
      ],
      userOptions: [],

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'alertCode', label: '警报编码' },
        { key: 'alertName', label: '警报名称' },
        { key: 'alertType', label: '警报类型' },
        { key: 'severity', label: '严重程度' },
        { key: 'triggerCondition', label: '触发条件' },
        { key: 'triggerCount', label: '触发次数' },
        { key: 'notificationCount', label: '通知次数' },
        { key: 'alertStatus', label: '警报状态' },
        { key: 'acknowledgeStatus', label: '确认状态' },
        { key: 'lastTriggerTime', label: '最后触发' }
      ],
      columnChecked: ['alertCode', 'alertName', 'alertType', 'severity', 'triggerCondition', 'triggerCount', 'notificationCount', 'alertStatus', 'acknowledgeStatus', 'lastTriggerTime'],
      columnVisible: {
        alertCode: true, alertName: true, alertType: true,
        severity: true, triggerCondition: true, triggerCount: true,
        notificationCount: true, alertStatus: true, acknowledgeStatus: true,
        lastTriggerTime: true
      },

      // 导入对话框
      importDialogVisible: false,
      importLoading: false,
      importFile: null,

      // 触发历史对话框
      historyDialogVisible: false,
      historyLoading: false,
      historyAlertName: ''
    }
  },
  
  created() {
    this.getList()
    this.loadStats()
    this.loadUserOptions()
  },
  
  beforeDestroy() {
    if (this.pushSocket) {
      this.pushSocket.close()
    }
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetAlertApi.getStatistics({})
        // request.js 将 code:1 转为 200 后 return data，response 就是 {code:1, msg, data}
        const d = (response && response.data) ? response.data : response
        if (d) {
          const total = d.totalCount || 0
          const active = d.activeCount || 0
          const today = d.todayCount || 0
          const unread = d.unreadCount || 0
          this.alertStats = {
            totalAlerts: total,
            activeAlerts: active,
            todayAlerts: today,
            unreadAlerts: unread,
            // el-progress percentage 必须是 0-100 整数
            activeRate: total > 0 ? Math.min(100, Math.round((active / total) * 100)) : 0,
            todayRate: total > 0 ? Math.min(100, Math.round((today / total) * 100)) : 0,
            unreadRate: total > 0 ? Math.min(100, Math.round((unread / total) * 100)) : 0
          }
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        // 查询参数字段映射：前端 queryForm -> 后端实体字段
        const { severity, acknowledgeStatus, ...restQuery } = this.queryForm
        const params = {
          ...restQuery,
          ...this.queryParams
        }
        // severity -> alertLevel
        if (severity) {
          params.alertLevel = severity
        }
        // acknowledgeStatus 后端无此字段，映射为 alertStatus
        if (acknowledgeStatus) {
          if (acknowledgeStatus === 'UNREAD') {
            params.alertStatus = 'PENDING'
          } else if (acknowledgeStatus === 'ACKNOWLEDGED') {
            params.alertStatus = 'ACKNOWLEDGED'
          } else if (acknowledgeStatus === 'READ') {
            params.alertStatus = 'PROCESSING'
          }
        }
        const response = await budgetAlertApi.getPage(params)
        if (response.code === 1) {
          const rawList = response.data.tlist || []
          // 字段映射：后端 BudgetAlert 实体字段 -> 前端显示字段
          this.alertList = rawList.map(item => ({
            ...item,
            id: item.alertId,
            severity: item.alertLevel,
            triggerCondition: item.alertMessage || item.alertDetail || '-',
            triggerCount: item.priority || 0,
            notificationCount: item.isNotified ? 1 : 0,
            lastTriggerTime: item.triggerTime,
            acknowledgeStatus: item.alertStatus === 'PENDING' ? 'UNREAD'
              : item.alertStatus === 'ACKNOWLEDGED' ? 'ACKNOWLEDGED'
              : item.alertStatus === 'RESOLVED' ? 'ACKNOWLEDGED'
              : item.alertStatus === 'ESCALATED' ? 'READ'
              : item.alertStatus === 'PROCESSING' ? 'READ'
              : 'ACKNOWLEDGED'
          }))
          this.total = response.data.totalRecord || 0
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetAlertApi.getUsers()
        if (response.code === 1) {
          this.userOptions = response.data
        }
      } catch (error) {
        console.error('加载用户选项失败：', error)
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
        alertName: '',
        alertType: '',
        severity: '',
        alertStatus: '',
        acknowledgeStatus: ''
      }
      this.handleQuery()
    },
    
    // 实时推送切换
    handlePushChange(value) {
      if (value) {
        this.initWebSocket()
        this.$message.success('已开启实时推送')
      } else {
        if (this.pushSocket) {
          this.pushSocket.close()
          this.pushSocket = null
        }
        this.$message.info('已关闭实时推送')
      }
    },
    
    // 初始化WebSocket
    initWebSocket() {
      // 这里应该连接到实际的WebSocket服务
      // this.pushSocket = new WebSocket('ws://localhost:8080/budget-alert')
      // this.pushSocket.onmessage = (event) => {
      //   const alert = JSON.parse(event.data)
      //   this.handleNewAlert(alert)
      // }
      console.log('WebSocket连接已初始化')
    },
    
    // 处理新警报
    handleNewAlert(alert) {
      // 显示新警报通知
      this.$notify({
        title: '新警报',
        message: alert.alertName,
        type: this.getSeverityColor(alert.severity),
        duration: 0
      })
      
      // 刷新列表
      this.getList()
    },
    
    // 创建警报
    handleCreateAlert() {
      this.dialogTitle = '创建预算警报'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑警报
    handleEdit(row) {
      this.dialogTitle = '编辑预算警报'
      this.dialogVisible = true
      // 后端字段 -> 前端表单字段的反向映射
      const notificationMethods = row.notificationMethod
        ? row.notificationMethod.split(',').filter(Boolean)
        : []
      let notificationUsers = []
      try {
        notificationUsers = row.notificationReceivers
          ? JSON.parse(row.notificationReceivers)
          : []
      } catch (e) {
        notificationUsers = []
      }
      this.alertForm = {
        id: row.alertId || row.id,
        alertName: row.alertName || '',
        alertCode: row.alertCode || '',
        alertType: row.alertType || '',
        severity: row.alertLevel || row.severity || '',
        alertDescription: row.alertMessage || '',
        triggerCondition: row.alertDetail || '',
        notificationMethods,
        notificationUsers,
        repeatInterval: row.repeatInterval || 30,
        maxRepeat: row.maxRepeat || 3,
        silenceTime: row.silenceTime || 2,
        isActive: row.alertStatus ? row.alertStatus !== 'CLOSED' : true,
        autoAcknowledge: row.autoClose === 1,
        logTrigger: row.logTrigger !== undefined ? row.logTrigger : true
      }
    },
    
    // 查看警报
    handleView(row) {
      this.detailDrawerVisible = true
      this.currentAlert = row
      this.loadAlertTriggerHistory(row.id)
      this.$nextTick(() => {
        this.initAlertTrendChart()
      })
    },
    
    // 确认警报
    handleAcknowledge(row) {
      this.acknowledgeDialogVisible = true
      this.currentAlert = row
      this.acknowledgeForm = {
        acknowledgeRemark: ''
      }
    },
    
    // 测试警报（对话框内 - 编辑时可测试，新建时提示先保存）
    async handleTestAlert() {
      if (!this.alertForm.id) {
        this.$message.warning('请先保存警报后再进行测试')
        return
      }
      try {
        await budgetAlertApi.sendNotification(this.alertForm.id)
        this.$message.success('测试通知已发送')
      } catch (error) {
        this.$message.error('测试失败：' + (error.message || '未知错误'))
      }
    },
    
    // 前端表单字段 -> 后端 BudgetAlert 实体字段转换
    buildBackendParams() {
      const form = this.alertForm
      const params = {
        alertCode: form.alertCode,
        alertName: form.alertName,
        alertType: form.alertType,
        // severity -> alertLevel
        alertLevel: form.severity,
        // alertDescription -> alertMessage
        alertMessage: form.alertDescription,
        // triggerCondition -> alertDetail
        alertDetail: form.triggerCondition,
        // notificationMethods (数组) -> notificationMethod (逗号分隔字符串)
        notificationMethod: Array.isArray(form.notificationMethods)
          ? form.notificationMethods.join(',')
          : form.notificationMethods || '',
        // notificationUsers (数组) -> notificationReceivers (JSON字符串)
        notificationReceivers: Array.isArray(form.notificationUsers)
          ? JSON.stringify(form.notificationUsers)
          : form.notificationUsers || '[]',
        // isActive -> alertStatus
        alertStatus: form.isActive ? 'PENDING' : 'CLOSED',
        // autoAcknowledge -> autoClose (0/1)
        autoClose: form.autoAcknowledge ? 1 : 0
      }
      return params
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.alertForm.validate()

        const params = this.buildBackendParams()

        if (this.alertForm.id) {
          // 编辑：带 alertId 调用 update
          params.alertId = this.alertForm.id
          await budgetAlertApi.update(this.alertForm.id, params)
          this.$message.success('更新成功')
        } else {
          await budgetAlertApi.create(params)
          this.$message.success('创建成功')
        }

        this.dialogVisible = false
        this.getList()
      } catch (error) {
        if (error && error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },
    
    // 提交确认
    async handleSubmitAcknowledge() {
      try {
        const params = {
          alertId: this.currentAlert.id,
          acknowledgeRemark: this.acknowledgeForm.acknowledgeRemark
        }
        await budgetAlertApi.acknowledgeAlert(params)
        this.$message.success('确认成功')
        this.acknowledgeDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('确认失败：' + error.message)
      }
    },

    // 升级警报
    handleEscalate(row) {
      this.escalateDialogVisible = true
      this.currentAlert = row
      this.escalateForm = {
        newSeverity: '',
        escalateReason: '',
        notifyEscalation: true
      }
    },

    // 提交升级
    async handleSubmitEscalate() {
      try {
        const params = {
          alertId: this.currentAlert.id,
          newSeverity: this.escalateForm.newSeverity,
          escalateReason: this.escalateForm.escalateReason,
          notifyEscalation: this.escalateForm.notifyEscalation
        }
        await budgetAlertApi.escalateAlert(params)
        this.$message.success('升级成功')
        this.escalateDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('升级失败：' + error.message)
      }
    },

    // 加载触发历史
    async loadAlertTriggerHistory(alertId) {
      try {
        const response = await budgetAlertApi.getTriggerHistory(alertId)
        this.alertTriggerHistory = response.data
      } catch (error) {
        console.error('加载触发历史失败：', error)
      }
    },

    // 初始化趋势图表
    initAlertTrendChart() {
      const chartDom = document.getElementById('alertTrendChart')
      if (!chartDom) return

      const myChart = echarts.init(chartDom)
      const option = {
        title: {
          text: '警报触发趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          name: '触发次数',
          type: 'line',
          data: [],
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      myChart.setOption(option)
    },
    
    // 重置表单
    resetForm() {
      this.alertForm = {
        id: null,
        alertName: '',
        alertCode: '',
        alertType: '',
        severity: '',
        alertDescription: '',
        triggerCondition: '',
        notificationMethods: [],
        notificationUsers: [],
        repeatInterval: 30,
        maxRepeat: 3,
        silenceTime: 2,
        isActive: true,
        autoAcknowledge: false,
        logTrigger: true
      }
      this.$nextTick(() => {
        this.$refs.alertForm && this.$refs.alertForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入警报
    handleImportAlerts() {
      this.importDialogVisible = true
      this.importFile = null
    },

    // 导入文件变化
    handleImportFileChange(file) {
      this.importFile = file.raw
    },

    // 提交导入
    async handleSubmitImport() {
      if (!this.importFile) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        // 若后端暂无 import 接口，提示用户
        this.$message.info('警报导入功能后端接口尚未实现，请联系后端开发')
        this.importDialogVisible = false
        this.importFile = null
      } catch (error) {
        this.$message.error('导入失败：' + (error.message || '未知错误'))
      } finally {
        this.importLoading = false
      }
    },

    // 批量确认
    async handleBatchAcknowledge() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要确认的警报')
        return
      }

      try {
        await this.$confirm('确认批量确认选中的 ' + this.selectedRows.length + ' 条警报记录？', '提示', {
          type: 'warning'
        })
        // 使用 alertId 而非 id，后端期望 alertId
        const ids = this.selectedRows.map(row => row.alertId || row.id)
        await budgetAlertApi.batchAcknowledge(ids)
        this.$message.success('批量确认成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量确认失败：' + error.message)
        }
      }
    },

    // 导出警报
    async handleExportAlerts() {
      try {
        const { severity, acknowledgeStatus, ...restQuery } = this.queryForm
        const params = { ...restQuery }
        if (severity) params.alertLevel = severity
        const response = await budgetAlertApi.export(params)
        // 处理 blob 下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算警报数据_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
    },
    
    // 设置
    handleSettings() {
      this.$router.push('/managementAccountant/ncv65/budgetControl/alertSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'test':
          this.handleTest(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    // 测试警报（更多菜单）
    async handleTest(row) {
      const alertId = row.alertId || row.id
      if (!alertId) {
        this.$message.warning('警报ID不存在，无法测试')
        return
      }
      try {
        await budgetAlertApi.sendNotification(alertId)
        this.$message.success('测试通知已发送')
      } catch (error) {
        this.$message.error('测试失败：' + (error.message || '未知错误'))
      }
    },

    // 触发历史（弹出框）
    async handleHistory(row) {
      const alertId = row.alertId || row.id
      this.historyAlertName = row.alertName || '警报'
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const response = await budgetAlertApi.getTriggerHistory(alertId)
        if (response.code === 1) {
          this.alertTriggerHistory = (response.data || []).map(item => ({
            ...item,
            triggerCondition: item.alertMessage || item.alertDetail || '-',
            notificationStatus: item.isNotified === 1 ? 'SUCCESS' : 'FAILED'
          }))
        } else {
          this.alertTriggerHistory = []
        }
      } catch (error) {
        this.alertTriggerHistory = []
        this.$message.error('加载触发历史失败：' + (error.message || '未知错误'))
      } finally {
        this.historyLoading = false
      }
    },
    
    // 复制（复用 handleEdit 的字段映射逻辑，清空 id 和 alertCode）
    handleCopy(row) {
      this.handleEdit(row)
      this.dialogTitle = '复制预算警报'
      this.alertForm.id = null
      this.alertForm.alertCode = ''
    },
    
    // 导出单个
    async handleExportSingle(row) {
      const alertId = row.alertId || row.id
      try {
        const response = await budgetAlertApi.exportSingle(alertId)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '警报_' + (row.alertName || alertId) + '_' + new Date().getTime() + '.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 删除
    async handleDelete(row) {
      const alertId = row.alertId || row.id
      try {
        await this.$confirm('确认删除该警报记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetAlertApi.delete(alertId)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
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
      this.columnChecked = Object.keys(this.columnVisible).filter(k => this.columnVisible[k])
      this.columnSettingVisible = true
    },
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 判断是否可以确认
    canAcknowledge(row) {
      return row.acknowledgeStatus === 'UNREAD'
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.creator === this.$store.getters.name || this.$store.getters.roles.includes('admin')
    },
    
    // 获取警报类型颜色
    getAlertTypeColor(type) {
      const colorMap = {
        'BUDGET_EXCEED': 'danger',
        'EXECUTION_ABNORMAL': 'warning',
        'APPROVAL_TIMEOUT': 'primary',
        'DATA_ANOMALY': 'warning',
        'SYSTEM_ERROR': 'danger',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取警报类型文本
    getAlertTypeText(type) {
      const item = this.alertTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取严重程度颜色
    getSeverityColor(severity) {
      const colorMap = {
        'INFO': 'info',
        'WARNING': 'warning',
        'ERROR': 'danger',
        'CRITICAL': 'danger'
      }
      return colorMap[severity] || 'info'
    },
    
    // 获取严重程度文本
    getSeverityText(severity) {
      const item = this.severityOptions.find(opt => opt.value === severity)
      return item ? item.label : severity
    },
    
    // 获取警报状态类型
    getAlertStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TRIGGERED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取警报状态文本
    getAlertStatusText(status) {
      const item = this.alertStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },
    
    // 获取确认状态类型
    getAcknowledgeStatusType(status) {
      const statusMap = {
        'UNREAD': 'danger',
        'READ': 'warning',
        'ACKNOWLEDGED': 'success'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取确认状态文本
    getAcknowledgeStatusText(status) {
      const item = this.acknowledgeStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-alert {
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
      
      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.active-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.today-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.unread-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }
      
      .stat-content {
        position: relative;
        z-index: 2;
        
        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }
        
        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }
        
        .stat-progress {
          margin-top: 8px;
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
      align-items: center;
      gap: 12px;
    }
  }
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .alert-acknowledge {
    .alert-info {
      margin-bottom: 20px;
    }
  }
  
  .text-right {
    text-align: right;
  }

  // 警报详情抽屉样式
  .alert-detail {
    padding: 20px;

    .detail-section {
      margin-bottom: 30px;

      h3 {
        color: #303133;
        font-size: 16px;
        margin: 0 0 15px 0;
        padding-bottom: 8px;
        border-bottom: 2px solid #E4E7ED;
      }
    }

    .stat-item {
      text-align: center;
      padding: 20px;
      background: #F5F7FA;
      border-radius: 8px;

      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #409EFF;
        margin-bottom: 8px;
      }

      .stat-label {
        font-size: 14px;
        color: #606266;
      }
    }

    .chart-container {
      height: 300px;
      width: 100%;
    }

    .detail-actions {
      margin-top: 30px;
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #E4E7ED;

      .el-button {
        margin: 0 8px;
      }
    }
  }

  // 警报升级对话框样式
  .alert-escalate {
    .alert-info {
      margin-bottom: 20px;

      h4 {
        color: #303133;
        font-size: 14px;
        margin: 0 0 10px 0;
      }
    }
  }
}
</style>
