<template>
  <div class="reminder-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>催报管理</h2>
      <p>预算填报催报策略配置和执行管理，支持多种催报方式和自动化催报流程</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateStrategy">创建策略</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-message" @click="handleSendReminder">发送催报</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewStatistics">催报统计</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">催报设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">催报报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 催报统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover" style="cursor:pointer" @click.native="handleStatCardClick('all')">
          <div class="stat-content">
            <div class="stat-number">{{ reminderStats.totalStrategies }}</div>
            <div class="stat-label">催报策略</div>
            <div class="stat-description">总催报策略数量</div>
            <div class="stat-trend">
              <i class="el-icon-bell"></i>
              <span>智能催报</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bell"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover" style="cursor:pointer" @click.native="handleStatCardClick('ACTIVE')">
          <div class="stat-content">
            <div class="stat-number">{{ reminderStats.activeTasks }}</div>
            <div class="stat-label">待催报任务</div>
            <div class="stat-description">需要催报的任务</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>待处理</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card sent-card" shadow="hover" style="cursor:pointer" @click.native="handleStatCardClick('today')">
          <div class="stat-content">
            <div class="stat-number">{{ reminderStats.sentToday }}</div>
            <div class="stat-label">今日催报</div>
            <div class="stat-description">今日发送催报数</div>
            <div class="stat-trend">
              <i class="el-icon-message"></i>
              <span>已发送</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-message"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card response-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ reminderStats.responseRate }}%</div>
            <div class="stat-label">响应率</div>
            <div class="stat-description">催报响应成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高效率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 催报方式选择 -->
    <el-card class="reminder-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>催报方式</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshReminderTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="reminderType in reminderTypes" :key="reminderType.id">
          <el-card 
            class="reminder-type-item" 
            shadow="hover" 
            @click.native="handleSelectReminderType(reminderType)"
            :class="{ 'selected': selectedReminderType === reminderType.id }"
          >
            <div class="reminder-type-icon">
              <i :class="reminderType.icon"></i>
            </div>
            <div class="reminder-type-title">{{ reminderType.name }}</div>
            <div class="reminder-type-description">{{ reminderType.description }}</div>
            <div class="reminder-type-stats">
              <span class="strategy-count">{{ reminderType.strategyCount }} 个策略</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 催报策略列表 -->
    <el-card class="reminder-strategies-card" shadow="never">
      <div slot="header" class="card-header">
        <span>催报策略</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索策略"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getReminderStrategyList"
            clearable
            @clear="getReminderStrategyList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getReminderStrategyList">刷新</el-button>
        </div>
      </div>
      
      <el-table
        :data="reminderStrategyList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="strategyName" label="策略名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.strategyName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="reminderType" label="催报方式" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getReminderTypeColor(scope.row.reminderType)" size="mini">
              {{ getReminderTypeText(scope.row.reminderType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerCondition" label="触发条件" width="150" show-overflow-tooltip />
        <el-table-column prop="reminderFrequency" label="催报频率" width="120" align="center" />
        <el-table-column prop="targetCount" label="目标数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="target-count">{{ scope.row.targetCount }} 个</span>
          </template>
        </el-table-column>
        <el-table-column prop="sentCount" label="已发送" width="100" align="center">
          <template slot-scope="scope">
            <span class="sent-count">{{ scope.row.sentCount }} 次</span>
          </template>
        </el-table-column>
        <el-table-column prop="responseRate" label="响应率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.responseRate" 
              :color="getResponseRateColor(scope.row.responseRate)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="response-text">{{ scope.row.responseRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-message"
              @click="handleSend(scope.row)"
              :disabled="scope.row.status !== 'ACTIVE'"
            >发送</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="enable">启用</el-dropdown-item>
                <el-dropdown-item command="disable">禁用</el-dropdown-item>
                <el-dropdown-item command="test">测试</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 催报策略详情抽屉 -->
    <el-drawer
      title="催报策略详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="60%"
    >
      <div class="detail-content" v-if="currentStrategy">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="策略基本信息" :column="2" border>
              <el-descriptions-item label="策略名称">{{ currentStrategy.strategyName }}</el-descriptions-item>
              <el-descriptions-item label="催报方式">{{ getReminderTypeText(currentStrategy.reminderType) }}</el-descriptions-item>
              <el-descriptions-item label="触发条件">{{ currentStrategy.triggerCondition }}</el-descriptions-item>
              <el-descriptions-item label="催报频率">{{ currentStrategy.reminderFrequency }}</el-descriptions-item>
              <el-descriptions-item label="目标数量">{{ currentStrategy.targetCount }} 个</el-descriptions-item>
              <el-descriptions-item label="已发送">{{ currentStrategy.sentCount }} 次</el-descriptions-item>
              <el-descriptions-item label="响应率">{{ currentStrategy.responseRate }}%</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentStrategy.status)" size="mini">
                  {{ getStatusText(currentStrategy.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentStrategy.creator }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentStrategy.createTime }}</el-descriptions-item>
              <el-descriptions-item label="策略描述" :span="2">{{ currentStrategy.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="催报配置" name="config">
            <el-form label-width="120px" size="small">
              <el-form-item label="催报模板">
                <el-input :value="currentStrategy.reminderTemplate" readonly type="textarea" :rows="3" />
              </el-form-item>
              <el-form-item label="发送时间">
                <el-input :value="currentStrategy.sendTime" readonly />
              </el-form-item>
              <el-form-item label="重复间隔">
                <el-input :value="currentStrategy.repeatInterval" readonly />
              </el-form-item>
              <el-form-item label="最大次数">
                <el-input :value="currentStrategy.maxRetries" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="目标对象" name="targets">
            <el-table :data="reminderTargets" border size="mini">
              <el-table-column prop="targetName" label="目标名称" width="200" />
              <el-table-column prop="targetType" label="目标类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getTargetTypeColor(scope.row.targetType)" size="mini">
                    {{ getTargetTypeText(scope.row.targetType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="contactInfo" label="联系方式" width="150" />
              <el-table-column prop="lastSentTime" label="最后催报" width="150" />
              <el-table-column prop="responseStatus" label="响应状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getResponseStatusColor(scope.row.responseStatus)" size="mini">
                    {{ getResponseStatusText(scope.row.responseStatus) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="发送记录" name="records">
            <el-table :data="reminderRecords" border size="mini">
              <el-table-column prop="sendTime" label="发送时间" width="150" />
              <el-table-column prop="reminderType" label="催报方式" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getReminderTypeColor(scope.row.reminderType)" size="mini">
                    {{ getReminderTypeText(scope.row.reminderType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="targetCount" label="目标数量" width="100" align="center" />
              <el-table-column prop="successCount" label="成功数量" width="100" align="center" />
              <el-table-column prop="failureCount" label="失败数量" width="100" align="center" />
              <el-table-column prop="responseCount" label="响应数量" width="100" align="center" />
              <el-table-column prop="operator" label="操作人" width="100" />
              <el-table-column prop="remarks" label="备注" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑催报策略对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="strategyForm"
        :model="strategyForm"
        :rules="strategyRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="策略名称" prop="strategyName">
              <el-input v-model="strategyForm.strategyName" placeholder="请输入策略名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催报方式" prop="reminderType">
              <el-select v-model="strategyForm.reminderType" placeholder="请选择催报方式" style="width: 100%">
                <el-option value="EMAIL" label="邮件催报" />
                <el-option value="SMS" label="短信催报" />
                <el-option value="SYSTEM" label="系统通知" />
                <el-option value="WECHAT" label="微信催报" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="触发条件" prop="triggerCondition">
              <el-input v-model="strategyForm.triggerCondition" placeholder="如：逾期3天" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催报频率" prop="reminderFrequency">
              <el-select v-model="strategyForm.reminderFrequency" placeholder="请选择催报频率" style="width: 100%">
                <el-option value="ONCE" label="仅一次" />
                <el-option value="DAILY" label="每日" />
                <el-option value="WEEKLY" label="每周" />
                <el-option value="CUSTOM" label="自定义" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发送时间" prop="sendTime">
              <el-time-picker
                v-model="strategyForm.sendTime"
                placeholder="选择发送时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大次数" prop="maxRetries">
              <el-input-number v-model="strategyForm.maxRetries" :min="1" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="催报模板" prop="reminderTemplate">
          <el-input
            v-model="strategyForm.reminderTemplate"
            type="textarea"
            :rows="4"
            placeholder="请输入催报模板内容"
          />
        </el-form-item>
        <el-form-item label="策略描述" prop="description">
          <el-input
            v-model="strategyForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入策略描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 发送催报弹窗 -->
    <el-dialog title="发送催报" :visible.sync="sendReminderDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="100px" size="small">
        <el-form-item label="选择策略">
          <el-select v-model="sendReminderForm.reminderIds" multiple placeholder="不选则发送所有活跃策略" style="width:100%">
            <el-option
              v-for="item in reminderStrategyList.filter(r => r.status === 'ACTIVE')"
              :key="item.strategyId"
              :label="item.strategyName"
              :value="item.strategyId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="sendReminderForm.message" type="textarea" :rows="3" placeholder="可选备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="sendReminderDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="sendReminderLoading" @click="confirmSendReminder">确认发送</el-button>
      </div>
    </el-dialog>

    <!-- 催报统计弹窗 -->
    <el-dialog title="催报统计" :visible.sync="statisticsDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="总策略数">{{ statisticsData.totalStrategies || 0 }}</el-descriptions-item>
        <el-descriptions-item label="活跃策略数">{{ statisticsData.activeTasks || 0 }}</el-descriptions-item>
        <el-descriptions-item label="今日发送数">{{ statisticsData.sentToday || 0 }}</el-descriptions-item>
        <el-descriptions-item label="响应率">{{ statisticsData.responseRate || 0 }}%</el-descriptions-item>
        <el-descriptions-item label="邮件策略数">{{ statisticsData.method_email || 0 }}</el-descriptions-item>
        <el-descriptions-item label="短信策略数">{{ statisticsData.method_sms || 0 }}</el-descriptions-item>
        <el-descriptions-item label="系统通知数">{{ statisticsData.method_system || 0 }}</el-descriptions-item>
        <el-descriptions-item label="微信策略数">{{ statisticsData.method_wechat || 0 }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="statisticsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 催报设置弹窗 -->
    <el-dialog title="催报设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="settingsForm" label-width="120px" size="small">
        <el-form-item label="默认频率">
          <el-select v-model="settingsForm.defaultFrequency" style="width:100%">
            <el-option value="ONCE" label="仅一次" />
            <el-option value="DAILY" label="每日" />
            <el-option value="WEEKLY" label="每周" />
            <el-option value="CUSTOM" label="自定义" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大重试次数">
          <el-input-number v-model="settingsForm.maxRetries" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="发送时间段">
          <el-input v-model="settingsForm.sendTimeStart" placeholder="开始时间" style="width:120px" /> 至
          <el-input v-model="settingsForm.sendTimeEnd" placeholder="结束时间" style="width:120px" />
        </el-form-item>
        <el-form-item label="启用邮件">
          <el-switch v-model="settingsForm.enableEmail" />
        </el-form-item>
        <el-form-item label="启用短信">
          <el-switch v-model="settingsForm.enableSms" />
        </el-form-item>
        <el-form-item label="启用系统通知">
          <el-switch v-model="settingsForm.enableSystem" />
        </el-form-item>
        <el-form-item label="启用微信">
          <el-switch v-model="settingsForm.enableWechat" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="settingsLoading" @click="saveSettings">保存</el-button>
      </div>
    </el-dialog>

    <!-- 催报报告弹窗 -->
    <el-dialog title="催报报告" :visible.sync="reportDialogVisible" width="900px">
      <div v-loading="reportLoading">
        <el-descriptions title="汇总统计" :column="4" border style="margin-bottom:20px">
          <el-descriptions-item label="总策略数">{{ (reportData.summary && reportData.summary.totalStrategies) || 0 }}</el-descriptions-item>
          <el-descriptions-item label="活跃策略">{{ (reportData.summary && reportData.summary.activeTasks) || 0 }}</el-descriptions-item>
          <el-descriptions-item label="今日发送">{{ (reportData.summary && reportData.summary.sentToday) || 0 }}</el-descriptions-item>
          <el-descriptions-item label="响应率">{{ (reportData.summary && reportData.summary.responseRate) || 0 }}%</el-descriptions-item>
        </el-descriptions>
        <el-table :data="reportData.records || []" border size="mini" max-height="400">
          <el-table-column prop="sendTime" label="发送时间" width="150" />
          <el-table-column prop="reminderType" label="催报方式" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getReminderTypeColor(scope.row.reminderType)" size="mini">{{ getReminderTypeText(scope.row.reminderType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="targetCount" label="目标数" width="80" align="center" />
          <el-table-column prop="successCount" label="成功数" width="80" align="center" />
          <el-table-column prop="failureCount" label="失败数" width="80" align="center" />
          <el-table-column prop="responseCount" label="响应数" width="80" align="center" />
          <el-table-column prop="operator" label="操作人" width="100" />
          <el-table-column prop="remarks" label="备注" />
        </el-table>
      </div>
      <div slot="footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="催报管理帮助" :visible.sync="helpDialogVisible" width="700px">
      <div style="line-height:1.8;color:#606266;font-size:14px">
        <h4 style="color:#303133">功能说明</h4>
        <p>催报管理模块用于配置和执行预算填报催报策略，支持邮件、短信、系统通知、微信四种催报方式。</p>
        <h4 style="color:#303133">操作指南</h4>
        <ul>
          <li><strong>创建策略</strong>：点击"创建策略"按钮，填写策略名称、催报方式、触发条件等信息</li>
          <li><strong>发送催报</strong>：点击"发送催报"按钮，选择要发送的策略，点击确认发送</li>
          <li><strong>催报统计</strong>：查看各催报方式的策略数量和发送统计</li>
          <li><strong>催报设置</strong>：配置默认催报频率、发送时间段、启用的催报方式等</li>
          <li><strong>催报报告</strong>：查看历史催报发送记录和统计汇总</li>
        </ul>
        <h4 style="color:#303133">状态说明</h4>
        <ul>
          <li><strong>活跃（ACTIVE）</strong>：策略正在运行，会按配置自动发送催报</li>
          <li><strong>非活跃（INACTIVE）</strong>：策略已暂停，不会自动发送</li>
          <li><strong>草稿（DRAFT）</strong>：策略尚未启用</li>
          <li><strong>禁用（DISABLED）</strong>：策略已被禁用</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button @click="helpDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'ReminderManagement',
  data() {
    return {
      // 统计数据
      reminderStats: {
        totalStrategies: 0,
        activeTasks: 0,
        sentToday: 0,
        responseRate: 0
      },

      // 催报方式
      reminderTypes: [
        { id: 'EMAIL', name: '邮件催报', description: '通过邮件发送催报通知', icon: 'el-icon-message', strategyCount: 0 },
        { id: 'SMS', name: '短信催报', description: '通过短信发送催报通知', icon: 'el-icon-mobile-phone', strategyCount: 0 },
        { id: 'SYSTEM', name: '系统通知', description: '系统内部消息通知', icon: 'el-icon-bell', strategyCount: 0 },
        { id: 'WECHAT', name: '微信催报', description: '通过微信发送催报通知', icon: 'el-icon-chat-dot-round', strategyCount: 0 }
      ],
      selectedReminderType: null,
      
      // 催报策略列表
      reminderStrategyList: [],
      loading: false,
      searchKeyword: '',
      filterStatus: null,
      
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentStrategy: null,
      reminderTargets: [],
      reminderRecords: [],
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      
      // 表单数据
      strategyForm: {
        strategyName: '',
        reminderType: '',
        triggerCondition: '',
        reminderFrequency: '',
        sendTime: '',
        maxRetries: 3,
        reminderTemplate: '',
        description: ''
      },
      
      // 表单验证规则
      strategyRules: {
        strategyName: [
          { required: true, message: '请输入策略名称', trigger: 'blur' }
        ],
        reminderType: [
          { required: true, message: '请选择催报方式', trigger: 'change' }
        ],
        triggerCondition: [
          { required: true, message: '请输入触发条件', trigger: 'blur' }
        ],
        reminderFrequency: [
          { required: true, message: '请选择催报频率', trigger: 'change' }
        ]
      },

      // 发送催报弹窗
      sendReminderDialogVisible: false,
      sendReminderForm: { reminderIds: [], message: '' },
      sendReminderLoading: false,

      // 催报统计弹窗
      statisticsDialogVisible: false,
      statisticsData: {},

      // 催报设置弹窗
      settingsDialogVisible: false,
      settingsForm: {
        defaultFrequency: 'DAILY',
        maxRetries: 3,
        enableEmail: true,
        enableSms: true,
        enableSystem: true,
        enableWechat: false,
        sendTimeStart: '09:00',
        sendTimeEnd: '18:00'
      },
      settingsLoading: false,

      // 催报报告弹窗
      reportDialogVisible: false,
      reportData: { summary: {}, records: [] },
      reportLoading: false,

      // 帮助弹窗
      helpDialogVisible: false
    }
  },
  
  created() {
    this.getReminderStrategyList()
    this.getReminderStats()
  },
  
  methods: {
    // 获取催报策略列表
    async getReminderStrategyList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedReminderType) {
          params.reminderMethod = this.selectedReminderType
        }
        if (this.filterStatus) {
          params.reminderStatus = this.filterStatus
        }
        const response = await advancedFeaturesApi.getReminderStrategyList(params)
        if (response && response.code === 1) {
          const data = response.data || {}
          this.reminderStrategyList = (data.list || []).map(item => ({
            ...item,
            strategyId: item.reminderId,
            strategyName: item.reminderName,
            triggerCondition: item.reminderRule || '-',
            reminderFrequency: item.reminderFrequency || '',
            reminderTemplate: item.messageTemplate || '',
            description: item.remark || '',
            targetCount: 0,
            sentCount: 0,
            responseRate: 0,
            status: item.reminderStatus,
            creator: item.createBy
          }))
        }
      } catch (error) {
        this.$message.error('获取催报策略列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getReminderStats() {
      try {
        const response = await advancedFeaturesApi.getReminderStats()
        if (response && response.code === 1 && response.data) {
          this.reminderStats = response.data
          // 更新催报方式卡片的策略数量
          const data = response.data
          this.reminderTypes.forEach(type => {
            const key = 'method_' + type.id.toLowerCase()
            type.strategyCount = data[key] || 0
          })
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 创建策略
    handleCreateStrategy() {
      this.dialogTitle = '创建催报策略'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑策略
    handleEdit(row) {
      this.dialogTitle = '编辑催报策略'
      this.dialogVisible = true
      // 将列表行数据映射回表单字段
      this.strategyForm = {
        strategyId: row.strategyId || row.reminderId,
        strategyName: row.strategyName || row.reminderName || '',
        reminderType: row.reminderType || '',
        triggerCondition: row.triggerCondition || row.reminderRule || '',
        reminderFrequency: row.reminderFrequency || '',
        sendTime: row.sendTime || '',
        maxRetries: row.maxRetries || 3,
        reminderTemplate: row.reminderTemplate || row.messageTemplate || '',
        description: row.description || row.remark || ''
      }
    },
    
    // 查看详情
    async handleView(row) {
      this.currentStrategy = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getReminderTargets(row.strategyId)
      await this.getReminderRecords(row.strategyId)
    },
    
    // 获取催报目标
    async getReminderTargets(strategyId) {
      try {
        const response = await advancedFeaturesApi.getReminderTargets(strategyId)
        this.reminderTargets = response.data
      } catch (error) {
        console.error('获取催报目标失败：', error)
      }
    },
    
    // 获取发送记录
    async getReminderRecords(strategyId) {
      try {
        const response = await advancedFeaturesApi.getReminderRecords(strategyId)
        this.reminderRecords = response.data
      } catch (error) {
        console.error('获取发送记录失败：', error)
      }
    },
    
    // 发送催报
    async handleSend(row) {
      this.$confirm('确定发送催报吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.sendReminder({ reminderIds: [row.strategyId] })
          this.$message.success('催报已发送')
          this.getReminderStrategyList()
        } catch (error) {
          this.$message.error('发送失败：' + error.message)
        }
      })
    },
    
    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'enable':
          this.handleEnableStrategy(row)
          break
        case 'disable':
          this.handleDisableStrategy(row)
          break
        case 'test':
          this.handleTestStrategy(row)
          break
        case 'copy':
          this.handleCopyStrategy(row)
          break
        case 'delete':
          this.handleDeleteStrategy(row)
          break
      }
    },
    
    // 启用策略
    async handleEnableStrategy(row) {
      try {
        await advancedFeaturesApi.enableReminderStrategy(row.strategyId)
        this.$message.success('策略已启用')
        this.getReminderStrategyList()
      } catch (error) {
        this.$message.error('启用失败：' + error.message)
      }
    },
    
    // 禁用策略
    async handleDisableStrategy(row) {
      try {
        await advancedFeaturesApi.disableReminderStrategy(row.strategyId)
        this.$message.success('策略已禁用')
        this.getReminderStrategyList()
      } catch (error) {
        this.$message.error('禁用失败：' + error.message)
      }
    },
    
    // 测试策略
    async handleTestStrategy(row) {
      try {
        await advancedFeaturesApi.testReminderStrategy(row.strategyId)
        this.$message.success('测试催报已发送')
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      }
    },
    
    // 复制策略
    async handleCopyStrategy(row) {
      try {
        await advancedFeaturesApi.copyReminderStrategy(row.strategyId)
        this.$message.success('复制成功')
        this.getReminderStrategyList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },
    
    // 删除策略
    handleDeleteStrategy(row) {
      this.$confirm('确定删除该催报策略吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteReminderStrategy(row.strategyId)
          this.$message.success('删除成功')
          this.getReminderStrategyList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    
    // 提交表单
    async handleSubmitForm() {
      this.$refs.strategyForm.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const payload = {
              reminderId: this.strategyForm.strategyId || undefined,
              reminderName: this.strategyForm.strategyName,
              reminderType: this.strategyForm.reminderType,
              reminderMethod: this.strategyForm.reminderType,
              reminderRule: this.strategyForm.triggerCondition,
              reminderFrequency: this.strategyForm.reminderFrequency,
              messageTemplate: this.strategyForm.reminderTemplate,
              remark: this.strategyForm.description,
              reminderStatus: 'ACTIVE',
              isEnabled: 1,
              delFlag: 0
            }
            if (this.strategyForm.strategyId) {
              await advancedFeaturesApi.updateReminderStrategy(payload)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createReminderStrategy(payload)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getReminderStrategyList()
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    
    // 重置表单
    resetForm() {
      this.strategyForm = {
        strategyName: '',
        reminderType: '',
        triggerCondition: '',
        reminderFrequency: '',
        sendTime: '',
        maxRetries: 3,
        reminderTemplate: '',
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.strategyForm && this.$refs.strategyForm.clearValidate()
      })
    },
    
    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },
    
    // 统计卡片点击筛选
    handleStatCardClick(type) {
      if (type === 'all') {
        this.selectedReminderType = null
        this.filterStatus = null
      } else if (type === 'ACTIVE') {
        this.filterStatus = 'ACTIVE'
        this.selectedReminderType = null
      } else if (type === 'today') {
        // 今日催报：打开报告弹窗
        this.handleReports()
        return
      }
      this.getReminderStrategyList()
    },

    // 刷新
    handleRefresh() {
      this.getReminderStrategyList()
      this.getReminderStats()
    },

    // 发送催报（弹窗）
    handleSendReminder() {
      this.sendReminderForm = { reminderIds: [], message: '' }
      this.sendReminderDialogVisible = true
    },

    // 确认发送催报
    async confirmSendReminder() {
      if (!this.sendReminderForm.reminderIds || this.sendReminderForm.reminderIds.length === 0) {
        this.$message.warning('请选择要发送催报的策略')
        return
      }
      this.sendReminderLoading = true
      try {
        const res = await advancedFeaturesApi.sendReminder({
          reminderIds: this.sendReminderForm.reminderIds,
          message: this.sendReminderForm.message
        })
        if (res && res.code === 1) {
          this.$message.success('催报发送成功')
          this.sendReminderDialogVisible = false
          this.getReminderStats()
        } else {
          this.$message.error(res.msg || '发送失败')
        }
      } catch (error) {
        this.$message.error('发送失败：' + error.message)
      } finally {
        this.sendReminderLoading = false
      }
    },

    // 查看统计（弹窗）
    handleViewStatistics() {
      this.statisticsData = { ...this.reminderStats }
      this.statisticsDialogVisible = true
    },

    // 催报设置（弹窗）
    async handleSettings() {
      this.settingsDialogVisible = true
      try {
        const res = await advancedFeaturesApi.getReminderSettings()
        if (res && res.code === 1 && res.data) {
          this.settingsForm = { ...res.data }
        }
      } catch (e) {
        console.error('获取设置失败', e)
      }
    },

    // 保存催报设置
    async saveSettings() {
      this.settingsLoading = true
      try {
        const res = await advancedFeaturesApi.saveReminderSettings(this.settingsForm)
        if (res && res.code === 1) {
          this.$message.success('设置保存成功')
          this.settingsDialogVisible = false
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      } catch (e) {
        this.$message.error('保存失败：' + e.message)
      } finally {
        this.settingsLoading = false
      }
    },

    // 催报报告（弹窗）
    async handleReports() {
      this.reportDialogVisible = true
      this.reportLoading = true
      try {
        const res = await advancedFeaturesApi.getReminderReport()
        if (res && res.code === 1 && res.data) {
          this.reportData = res.data
        }
      } catch (e) {
        console.error('获取报告失败', e)
      } finally {
        this.reportLoading = false
      }
    },

    // 帮助（弹窗）
    handleHelp() {
      this.helpDialogVisible = true
    },
    
    // 刷新催报方式
    refreshReminderTypes() {
      this.getReminderStrategyList()
      this.getReminderStats()
      this.$message.success('已刷新')
    },

    // 选择催报方式
    handleSelectReminderType(reminderType) {
      if (this.selectedReminderType === reminderType.id) {
        // 再次点击取消筛选
        this.selectedReminderType = null
      } else {
        this.selectedReminderType = reminderType.id
      }
      this.filterStatus = null
      this.getReminderStrategyList()
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 获取催报方式颜色
    getReminderTypeColor(type) {
      const colorMap = {
        'EMAIL': 'primary',
        'SMS': 'success',
        'SYSTEM': 'warning',
        'WECHAT': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取催报方式文本
    getReminderTypeText(type) {
      const textMap = {
        'EMAIL': '邮件',
        'SMS': '短信',
        'SYSTEM': '系统',
        'WECHAT': '微信'
      }
      return textMap[type] || type
    },
    
    // 获取响应率颜色
    getResponseRateColor(rate) {
      if (rate >= 80) return '#67C23A'
      if (rate >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'DISABLED': 'danger',
        'DRAFT': 'info'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'DISABLED': '禁用',
        'DRAFT': '草稿'
      }
      return textMap[status] || status
    },
    
    // 获取目标类型颜色
    getTargetTypeColor(type) {
      const colorMap = {
        'USER': 'primary',
        'DEPARTMENT': 'success',
        'ROLE': 'warning'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取目标类型文本
    getTargetTypeText(type) {
      const textMap = {
        'USER': '用户',
        'DEPARTMENT': '部门',
        'ROLE': '角色'
      }
      return textMap[type] || type
    },
    
    // 获取响应状态颜色
    getResponseStatusColor(status) {
      const colorMap = {
        'RESPONDED': 'success',
        'PENDING': 'warning',
        'NO_RESPONSE': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取响应状态文本
    getResponseStatusText(status) {
      const textMap = {
        'RESPONDED': '已响应',
        'PENDING': '待响应',
        'NO_RESPONSE': '未响应'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.reminder-management {
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

  .toolbar-card {
    margin-bottom: 20px;

    .text-right {
      text-align: right;
    }
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
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.sent-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      &.response-card {
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

        .stat-trend {
          font-size: 12px;
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

  .reminder-types-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .reminder-type-item {
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      &.selected {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .reminder-type-icon {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 12px;

        i {
          font-size: 24px;
          color: white;
        }
      }

      .reminder-type-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 8px;
      }

      .reminder-type-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 12px;
      }

      .reminder-type-stats {
        .strategy-count {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .reminder-strategies-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        align-items: center;
      }
    }

    .target-count {
      color: #409EFF;
      font-weight: 500;
    }

    .sent-count {
      color: #67C23A;
      font-weight: 500;
    }

    .response-text {
      margin-left: 8px;
      font-size: 12px;
      color: #606266;
    }
  }

  .detail-content {
    padding: 20px;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
