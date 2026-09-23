<template>
  <div class="budget-warning">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算预警管理</h2>
      <p>配置预警规则和阈值，及时发现预算执行异常，提供预警处理和分析</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateWarning">创建预警</el-button>
            <el-button type="success" icon="el-icon-upload2" @click="handleImportWarnings">导入预警</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchProcess">批量处理</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportWarnings">导出预警</el-button>
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

    <!-- 预警统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ warningStats.totalWarnings }}</div>
            <div class="stat-label">预警总数</div>
            <div class="stat-description">所有预警规则数量</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ warningStats.activeWarnings }}</div>
            <div class="stat-label">活跃预警</div>
            <div class="stat-description">当前生效的预警规则</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="warningStats.activeRate" 
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
        <el-card class="stat-card triggered-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ warningStats.triggeredCount }}</div>
            <div class="stat-label">触发预警</div>
            <div class="stat-description">本月触发的预警数量</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="warningStats.triggerRate" 
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
        <el-card class="stat-card pending-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ warningStats.pendingCount }}</div>
            <div class="stat-label">待处理</div>
            <div class="stat-description">需要处理的预警事项</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="warningStats.pendingRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-time"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预警名称">
          <el-input
            v-model="queryForm.warningName"
            placeholder="请输入预警名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select
            v-model="queryForm.warningType"
            placeholder="请选择预警类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in warningTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select
            v-model="queryForm.warningLevel"
            placeholder="请选择预警级别"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in warningLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态">
          <el-select
            v-model="queryForm.warningStatus"
            placeholder="请选择预警状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in warningStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select
            v-model="queryForm.processStatus"
            placeholder="请选择处理状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in processStatusOptions"
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

    <!-- 预警列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算预警列表</span>
        <div class="table-tools">
          <el-tooltip content="实时监控" placement="top">
            <el-switch
              v-model="realTimeMonitor"
              active-text="实时监控"
              @change="handleMonitorChange"
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
        :data="warningList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column v-if="columnVisible.warningCode" prop="warningCode" label="预警编码" width="150" show-overflow-tooltip />
        <el-table-column v-if="columnVisible.warningName" prop="warningName" label="预警名称" min-width="200" show-overflow-tooltip />

        <el-table-column v-if="columnVisible.warningType" prop="warningType" label="预警类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getWarningTypeColor(scope.row.warningType)">
              {{ getWarningTypeText(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.warningLevel" prop="warningLevel" label="预警级别" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getWarningLevelColor(scope.row.warningLevel)">
              {{ getWarningLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.thresholdValue" prop="thresholdValue" label="预警阈值" width="120" align="center">
          <template slot-scope="scope">
            <span class="threshold-text">{{ scope.row.thresholdValue }}{{ scope.row.thresholdUnit }}</span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.currentValue" prop="currentValue" label="当前值" width="120" align="center" sortable="custom">
          <template slot-scope="scope">
            <span :class="getValueClass(scope.row.currentValue, scope.row.thresholdValue)">
              {{ scope.row.currentValue }}{{ scope.row.thresholdUnit }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.exceedRate" prop="exceedRate" label="超出率" width="100" align="center" sortable="custom">
          <template slot-scope="scope">
            <el-progress
              :percentage="Math.abs(scope.row.exceedRate || 0)"
              :stroke-width="6"
              :text-inside="true"
              :color="getExceedRateColor(scope.row.exceedRate)"
            />
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.warningStatus" prop="warningStatus" label="预警状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getWarningStatusType(scope.row.warningStatus)" size="mini">
              {{ getWarningStatusText(scope.row.warningStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.processStatus" prop="processStatus" label="处理状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getProcessStatusType(scope.row.processStatus)" size="mini">
              {{ getProcessStatusText(scope.row.processStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column v-if="columnVisible.lastTriggerTime" prop="lastTriggerTime" label="最后触发" width="150" align="center" />
        
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >详情</el-button>
            <el-button
              v-if="canProcess(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-check"
              class="success-text"
              @click="handleProcess(scope.row)"
            >处理</el-button>
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
                <el-dropdown-item command="history" icon="el-icon-time">触发历史</el-dropdown-item>
                <el-dropdown-item command="test" icon="el-icon-s-data">测试预警</el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
                <el-dropdown-item command="delete" icon="el-icon-delete" divided>删除</el-dropdown-item>
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

    <!-- 新增/编辑预警对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="warningForm"
        :model="warningForm"
        :rules="warningRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警名称" prop="warningName">
              <el-input
                v-model="warningForm.warningName"
                placeholder="请输入预警名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警编码" prop="warningCode">
              <el-input
                v-model="warningForm.warningCode"
                placeholder="请输入预警编码"
                :disabled="!!warningForm.id"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警类型" prop="warningType">
              <el-select
                v-model="warningForm.warningType"
                placeholder="请选择预警类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in warningTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警级别" prop="warningLevel">
              <el-select
                v-model="warningForm.warningLevel"
                placeholder="请选择预警级别"
                style="width: 100%"
              >
                <el-option
                  v-for="item in warningLevelOptions"
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
            <el-form-item label="预警阈值" prop="thresholdValue">
              <el-input-number
                v-model="warningForm.thresholdValue"
                :precision="2"
                placeholder="请输入预警阈值"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值单位" prop="thresholdUnit">
              <el-select
                v-model="warningForm.thresholdUnit"
                placeholder="请选择阈值单位"
                style="width: 100%"
              >
                <el-option value="%" label="百分比" />
                <el-option value="万元" label="万元" />
                <el-option value="次" label="次数" />
                <el-option value="天" label="天数" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="预警描述" prop="warningDescription">
          <el-input
            v-model="warningForm.warningDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入预警描述"
          />
        </el-form-item>
        
        <!-- 预警条件配置 -->
        <el-form-item label="预警条件" prop="warningConditions">
          <div class="warning-conditions">
            <div class="conditions-header">
              <el-button type="primary" size="mini" @click="handleAddCondition">添加条件</el-button>
              <el-button type="success" size="mini" @click="handleImportConditions">导入条件</el-button>
            </div>
            
            <el-table
              :data="warningForm.warningConditions"
              border
              size="mini"
              max-height="300"
            >
              <el-table-column type="index" label="序号" width="60" align="center" />
              
              <el-table-column label="监控字段" width="150">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.monitorField"
                    placeholder="监控字段"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="EXECUTION_RATE" label="执行率" />
                    <el-option value="VARIANCE_RATE" label="差异率" />
                    <el-option value="BUDGET_AMOUNT" label="预算金额" />
                    <el-option value="ACTUAL_AMOUNT" label="实际金额" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="比较操作符" width="120">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.operator"
                    placeholder="操作符"
                    size="mini"
                    style="width: 100%"
                  >
                    <el-option value="GT" label="大于" />
                    <el-option value="GTE" label="大于等于" />
                    <el-option value="LT" label="小于" />
                    <el-option value="LTE" label="小于等于" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="阈值" width="120">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.thresholdValue"
                    :precision="2"
                    size="mini"
                    style="width: 100%"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="逻辑关系" width="100">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.logicOperator"
                    placeholder="逻辑关系"
                    size="mini"
                    style="width: 100%"
                    :disabled="scope.$index === warningForm.warningConditions.length - 1"
                  >
                    <el-option value="AND" label="并且" />
                    <el-option value="OR" label="或者" />
                  </el-select>
                </template>
              </el-table-column>
              
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    class="danger-text"
                    @click="handleRemoveCondition(scope.$index)"
                  >删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form-item>
        
        <!-- 通知配置 -->
        <el-form-item label="通知配置">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="通知方式" prop="notificationMethods">
                <el-checkbox-group v-model="warningForm.notificationMethods">
                  <el-checkbox label="EMAIL">邮件</el-checkbox>
                  <el-checkbox label="SMS">短信</el-checkbox>
                  <el-checkbox label="SYSTEM">系统通知</el-checkbox>
                  <el-checkbox label="WECHAT">微信</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="通知人员" prop="notificationUsers">
                <el-select
                  v-model="warningForm.notificationUsers"
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
        
        <el-form-item label="预警配置">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-checkbox v-model="warningForm.isActive">启用预警</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="warningForm.autoProcess">自动处理</el-checkbox>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="warningForm.logTrigger">记录触发日志</el-checkbox>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleTestWarning">测试预警</el-button>
        <el-button type="primary" @click="handleSubmitForm">保存预警</el-button>
      </div>
    </el-dialog>

    <!-- 预警处理对话框 -->
    <el-dialog
      title="预警处理"
      :visible.sync="processDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="warning-process" v-if="currentWarning">
        <div class="warning-info">
          <h4>预警信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="预警名称">{{ currentWarning.warningName }}</el-descriptions-item>
            <el-descriptions-item label="预警级别">
              <el-tag :type="getWarningLevelColor(currentWarning.warningLevel)" size="mini">
                {{ getWarningLevelText(currentWarning.warningLevel) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="预警阈值">{{ currentWarning.thresholdValue }}{{ currentWarning.thresholdUnit }}</el-descriptions-item>
            <el-descriptions-item label="当前值">{{ currentWarning.currentValue }}{{ currentWarning.thresholdUnit }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-form :model="processForm" label-width="120px" size="small">
          <el-form-item label="处理方式" prop="processType">
            <el-radio-group v-model="processForm.processType">
              <el-radio label="IGNORE">忽略</el-radio>
              <el-radio label="ADJUST">调整</el-radio>
              <el-radio label="ESCALATE">升级</el-radio>
              <el-radio label="RESOLVE">解决</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="处理说明" prop="processRemark">
            <el-input
              v-model="processForm.processRemark"
              type="textarea"
              :rows="4"
              placeholder="请输入处理说明"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitProcess">确认处理</el-button>
      </div>
    </el-dialog>

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

    <!-- 详情对话框 -->
    <el-dialog title="预警详情" :visible.sync="detailDialogVisible" width="800px" :close-on-click-modal="false">
      <div v-if="detailData" v-loading="detailLoading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="预警编码">{{ detailData.warningCode }}</el-descriptions-item>
          <el-descriptions-item label="预警名称">{{ detailData.warningName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">
            <el-tag size="mini" :type="getWarningTypeColor(detailData.warningType)">{{ getWarningTypeText(detailData.warningType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag size="mini" :type="getWarningLevelColor(detailData.warningLevel)">{{ getWarningLevelText(detailData.warningLevel) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警阈值">{{ detailData.thresholdValue }}{{ detailData.thresholdUnit }}</el-descriptions-item>
          <el-descriptions-item label="预警状态">
            <el-tag size="mini" :type="getWarningStatusType(detailData.warningStatus)">{{ getWarningStatusText(detailData.warningStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否启用">
            <el-tag size="mini" :type="detailData.isActive ? 'success' : 'info'">{{ detailData.isActive ? '启用' : '停用' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="自动处理">{{ detailData.autoProcess ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="记录触发日志">{{ detailData.logTrigger ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="最后触发时间">{{ detailData.lastTriggerTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预警描述" :span="2">{{ detailData.warningDescription || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="detailData.warningConditions && detailData.warningConditions.length" style="margin-top:16px;">
          <div style="font-weight:500;margin-bottom:8px;">预警条件</div>
          <el-table :data="detailData.warningConditions" border size="mini">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="monitorField" label="监控字段" width="120">
              <template slot-scope="scope">{{ getMonitorFieldText(scope.row.monitorField) }}</template>
            </el-table-column>
            <el-table-column prop="operator" label="操作符" width="100">
              <template slot-scope="scope">{{ getOperatorText(scope.row.operator) }}</template>
            </el-table-column>
            <el-table-column prop="thresholdValue" label="阈值" width="100" />
            <el-table-column prop="logicOperator" label="逻辑关系" width="100">
              <template slot-scope="scope">{{ scope.row.logicOperator === 'AND' ? '并且' : '或者' }}</template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="detailData.notificationMethods && detailData.notificationMethods.length" style="margin-top:16px;">
          <div style="font-weight:500;margin-bottom:8px;">通知方式</div>
          <el-tag v-for="m in detailData.notificationMethods" :key="m" style="margin-right:8px;">{{ getNotificationMethodText(m) }}</el-tag>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleEditFromDetail">编辑</el-button>
      </div>
    </el-dialog>

    <!-- 触发历史对话框 -->
    <el-dialog title="触发历史" :visible.sync="historyDialogVisible" width="800px">
      <el-table v-loading="historyLoading" :data="historyList" border size="small">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="triggerTime" label="触发时间" width="160" />
        <el-table-column prop="triggerValue" label="触发值" width="120" />
        <el-table-column prop="processStatus" label="处理状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getProcessStatusType(scope.row.processStatus)">{{ getProcessStatusText(scope.row.processStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="processRemark" label="处理说明" show-overflow-tooltip />
        <el-table-column prop="processTime" label="处理时间" width="160" />
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :current-page="historyParams.current"
          :page-size="historyParams.size"
          :total="historyTotal"
          background
          layout="total, prev, pager, next"
          @current-change="handleHistoryPageChange"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 测试预警对话框 -->
    <el-dialog title="测试预警" :visible.sync="testDialogVisible" width="500px" :close-on-click-modal="false">
      <div v-if="testWarning">
        <p style="color:#606266;margin-bottom:16px;">对预警规则「{{ testWarning.warningName }}」进行测试，系统将模拟触发条件并返回测试结果。</p>
        <el-form :model="testForm" label-width="100px" size="small">
          <el-form-item label="测试值">
            <el-input-number v-model="testForm.testValue" :precision="2" style="width:100%" placeholder="请输入测试值" />
          </el-form-item>
          <el-form-item label="测试说明">
            <el-input v-model="testForm.testRemark" type="textarea" :rows="2" placeholder="可选" />
          </el-form-item>
        </el-form>
        <div v-if="testResult" class="test-result">
          <el-alert
            :title="testResult.triggered ? '预警触发' : '未触发预警'"
            :type="testResult.triggered ? 'warning' : 'success'"
            :description="testResult.message"
            show-icon
            :closable="false"
          />
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" :loading="testLoading" @click="handleSubmitTest">执行测试</el-button>
      </div>
    </el-dialog>

    <!-- 导入预警对话框 -->
    <el-dialog title="导入预警规则" :visible.sync="importDialogVisible" width="500px" :close-on-click-modal="false">
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
  </div>
</template>

<script>
import { budgetWarningApi } from '@/api/managementAccountant/ncv65/budgetControl'

export default {
  name: 'BudgetWarning',
  data() {
    return {
      // 查询参数
      queryForm: {
        warningName: '',
        warningType: '',
        warningLevel: '',
        warningStatus: '',
        processStatus: ''
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      warningList: [],
      total: 0,
      selectedRows: [],
      
      // 实时监控
      realTimeMonitor: false,
      monitorTimer: null,
      
      // 统计数据
      warningStats: {
        totalWarnings: 0,
        activeWarnings: 0,
        triggeredCount: 0,
        pendingCount: 0,
        activeRate: 0,
        triggerRate: 0,
        pendingRate: 0
      },
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      processDialogVisible: false,
      currentWarning: null,
      warningForm: {
        id: null,
        warningName: '',
        warningCode: '',
        warningType: '',
        warningLevel: '',
        thresholdValue: 0,
        thresholdUnit: '%',
        warningDescription: '',
        warningConditions: [],
        notificationMethods: [],
        notificationUsers: [],
        isActive: true,
        autoProcess: false,
        logTrigger: true
      },
      warningRules: {
        warningName: [
          { required: true, message: '请输入预警名称', trigger: 'blur' }
        ],
        warningCode: [
          { required: true, message: '请输入预警编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '预警编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        warningType: [
          { required: true, message: '请选择预警类型', trigger: 'change' }
        ],
        warningLevel: [
          { required: true, message: '请选择预警级别', trigger: 'change' }
        ]
      },
      
      // 预警处理
      processForm: {
        processType: 'IGNORE',
        processRemark: ''
      },
      
      // 选项数据
      warningTypeOptions: [
        { value: 'BUDGET_EXCEED', label: '预算超支' },
        { value: 'EXECUTION_LOW', label: '执行率低' },
        { value: 'VARIANCE_HIGH', label: '差异过大' },
        { value: 'TIME_OVERDUE', label: '时间超期' },
        { value: 'APPROVAL_DELAY', label: '审批延迟' },
        { value: 'CUSTOM', label: '自定义预警' }
      ],
      warningLevelOptions: [
        { value: 'LOW', label: '低级' },
        { value: 'MEDIUM', label: '中级' },
        { value: 'HIGH', label: '高级' },
        { value: 'CRITICAL', label: '严重' }
      ],
      warningStatusOptions: [
        { value: 'ACTIVE', label: '活跃' },
        { value: 'INACTIVE', label: '停用' },
        { value: 'TRIGGERED', label: '已触发' }
      ],
      processStatusOptions: [
        { value: 'PENDING', label: '待处理' },
        { value: 'PROCESSING', label: '处理中' },
        { value: 'RESOLVED', label: '已解决' },
        { value: 'IGNORED', label: '已忽略' }
      ],
      userOptions: [],

      // 详情对话框
      detailDialogVisible: false,
      detailLoading: false,
      detailData: null,

      // 触发历史
      historyDialogVisible: false,
      historyLoading: false,
      historyList: [],
      historyTotal: 0,
      historyParams: { current: 1, size: 10, warningId: null },

      // 测试预警
      testDialogVisible: false,
      testLoading: false,
      testWarning: null,
      testForm: { testValue: 0, testRemark: '' },
      testResult: null,

      // 列设置
      columnSettingVisible: false,
      columnOptions: [
        { key: 'warningCode', label: '预警编码' },
        { key: 'warningName', label: '预警名称' },
        { key: 'warningType', label: '预警类型' },
        { key: 'warningLevel', label: '预警级别' },
        { key: 'thresholdValue', label: '预警阈值' },
        { key: 'currentValue', label: '当前值' },
        { key: 'exceedRate', label: '超出率' },
        { key: 'warningStatus', label: '预警状态' },
        { key: 'processStatus', label: '处理状态' },
        { key: 'lastTriggerTime', label: '最后触发' }
      ],
      columnChecked: ['warningCode', 'warningName', 'warningType', 'warningLevel', 'thresholdValue', 'currentValue', 'exceedRate', 'warningStatus', 'processStatus', 'lastTriggerTime'],
      columnVisible: {
        warningCode: true, warningName: true, warningType: true,
        warningLevel: true, thresholdValue: true, currentValue: true,
        exceedRate: true, warningStatus: true, processStatus: true,
        lastTriggerTime: true
      },

      // 导入对话框
      importDialogVisible: false,
      importLoading: false,
      importFile: null
    }
  },
  
  created() {
    this.getList()
    this.loadStats()
    this.loadUserOptions()
  },
  
  beforeDestroy() {
    if (this.monitorTimer) {
      clearInterval(this.monitorTimer)
    }
  },
  
  methods: {
    // 加载统计数据
    async loadStats() {
      try {
        const response = await budgetWarningApi.getStatistics()
        if (response.code === 1 && response.data) {
          const d = response.data
          const total = d.totalCount || 0
          const enabled = d.enabledCount || 0
          this.warningStats = {
            totalWarnings: total,
            activeWarnings: enabled,
            triggeredCount: d.triggeredCount || 0,
            pendingCount: d.pendingCount || 0,
            activeRate: total > 0 ? parseFloat(((enabled / total) * 100).toFixed(1)) : 0,
            triggerRate: d.triggerRate || 0,
            pendingRate: d.pendingRate || 0
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
        const params = {
          current: this.queryParams.pageNum,
          size: this.queryParams.pageSize,
          ...this.queryForm
        }
        const response = await budgetWarningApi.getPage(params)
        const data = response.data || {}
        this.warningList = data.records || data.tlist || data.list || []
        this.total = data.total || data.totalRecord || 0
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载用户选项
    async loadUserOptions() {
      try {
        const response = await budgetWarningApi.getUsers()
        this.userOptions = response.data
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
        warningName: '',
        warningType: '',
        warningLevel: '',
        warningStatus: '',
        processStatus: ''
      }
      this.handleQuery()
    },
    
    // 实时监控切换
    handleMonitorChange(value) {
      if (value) {
        this.monitorTimer = setInterval(() => {
          this.getList()
        }, 10000) // 10秒刷新一次
        this.$message.success('已开启实时监控')
      } else {
        if (this.monitorTimer) {
          clearInterval(this.monitorTimer)
          this.monitorTimer = null
        }
        this.$message.info('已关闭实时监控')
      }
    },
    
    // 创建预警
    handleCreateWarning() {
      this.dialogTitle = '创建预算预警'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑预警
    async handleEdit(row) {
      this.dialogTitle = '编辑预算预警'
      this.resetForm()
      try {
        const response = await budgetWarningApi.get(row.id)
        const data = response.data || row
        this.warningForm = {
          id: data.id,
          warningName: data.warningName || '',
          warningCode: data.warningCode || '',
          warningType: data.warningType || '',
          warningLevel: data.warningLevel || '',
          thresholdValue: data.thresholdValue || 0,
          thresholdUnit: data.thresholdUnit || '%',
          warningDescription: data.warningDescription || '',
          warningConditions: data.warningConditions || [],
          notificationMethods: data.notificationMethods || [],
          notificationUsers: data.notificationUsers || [],
          isActive: data.isActive !== undefined ? data.isActive : true,
          autoProcess: data.autoProcess || false,
          logTrigger: data.logTrigger !== undefined ? data.logTrigger : true
        }
      } catch (e) {
        this.warningForm = { ...row }
      }
      this.dialogVisible = true
    },

    // 查看预警
    async handleView(row) {
      this.detailDialogVisible = true
      this.detailLoading = true
      this.detailData = null
      try {
        const response = await budgetWarningApi.get(row.id)
        this.detailData = response.data || row
      } catch (e) {
        this.detailData = row
      } finally {
        this.detailLoading = false
      }
    },

    // 从详情弹窗跳转编辑
    handleEditFromDetail() {
      this.detailDialogVisible = false
      this.handleEdit(this.detailData)
    },
    
    // 处理预警
    handleProcess(row) {
      this.processDialogVisible = true
      this.currentWarning = row
      this.processForm = {
        processType: 'IGNORE',
        processRemark: ''
      }
    },
    
    // 添加条件
    handleAddCondition() {
      this.warningForm.warningConditions.push({
        monitorField: '',
        operator: 'GT',
        thresholdValue: 0,
        logicOperator: 'AND'
      })
    },
    
    // 删除条件
    handleRemoveCondition(index) {
      this.warningForm.warningConditions.splice(index, 1)
    },
    
    // 导入条件
    handleImportConditions() {
      this.$message.info('条件导入功能开发中...')
    },
    
    // 测试预警
    handleTestWarning() {
      this.$message.info('预警测试功能开发中...')
    },
    
    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.warningForm.validate()

        if (this.warningForm.id) {
          // 编辑：带 id 调用 update
          const params = { ...this.warningForm }
          await budgetWarningApi.update(this.warningForm.id, params)
          this.$message.success('更新成功')
        } else {
          // 创建：去掉 id 字段，后端不接受
          const { id, ...params } = this.warningForm
          await budgetWarningApi.create(params)
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
    
    // 提交处理
    async handleSubmitProcess() {
      try {
        const params = {
          warningId: this.currentWarning.warningRuleId || this.currentWarning.id,
          processType: this.processForm.processType,
          processRemark: this.processForm.processRemark
        }
        await budgetWarningApi.processWarning(params)
        this.$message.success('处理成功')
        this.processDialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('处理失败：' + error.message)
      }
    },
    
    // 重置表单
    resetForm() {
      this.warningForm = {
        id: null,
        warningName: '',
        warningCode: '',
        warningType: '',
        warningLevel: '',
        thresholdValue: 0,
        thresholdUnit: '%',
        warningDescription: '',
        warningConditions: [],
        notificationMethods: [],
        notificationUsers: [],
        isActive: true,
        autoProcess: false,
        logTrigger: true
      }
      this.$nextTick(() => {
        this.$refs.warningForm && this.$refs.warningForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 导入预警
    handleImportWarnings() {
      this.importDialogVisible = true
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
        await budgetWarningApi.import(formData)
        this.$message.success('导入成功')
        this.importDialogVisible = false
        this.importFile = null
        this.getList()
      } catch (error) {
        this.$message.error('导入失败：' + (error.message || '未知错误'))
      } finally {
        this.importLoading = false
      }
    },

    // 批量处理
    async handleBatchProcess() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要处理的预警')
        return
      }

      try {
        await this.$confirm('确认批量处理选中的 ' + this.selectedRows.length + ' 条预警记录？', '提示', {
          type: 'warning'
        })
        const ruleIds = this.selectedRows.map(row => row.warningRuleId || row.id)
        await budgetWarningApi.batchProcess(ruleIds)
        this.$message.success('批量处理成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量处理失败：' + error.message)
        }
      }
    },

    // 导出预警
    async handleExportWarnings() {
      try {
        const params = { ...this.queryForm }
        const response = await budgetWarningApi.export(params)
        // 处理blob下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算预警规则_' + new Date().getTime() + '.xlsx'
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
      this.$router.push('/managementAccountant/ncv65/budgetControl/warningSettings')
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'history':
          this.handleHistory(row)
          break
        case 'test':
          this.handleTest(row)
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
    handleHistory(row) {
      this.historyParams = { current: 1, size: 10, warningId: row.id }
      this.historyList = []
      this.historyTotal = 0
      this.historyDialogVisible = true
      this.loadHistoryList()
    },

    async loadHistoryList() {
      this.historyLoading = true
      try {
        const response = await budgetWarningApi.getHistory({
          pageNum: this.historyParams.current,
          pageSize: this.historyParams.size,
          ruleId: this.historyParams.warningId
        })
        const data = response.data || {}
        let records = data.records || data.tlist || data.list || []
        // 字段映射：后端字段 -> 前端显示字段
        records = records.map(item => ({
          ...item,
          triggerTime: item.createTime,
          triggerValue: item.warningData ? this.extractTriggerValue(item.warningData) : '-',
          processStatus: item.handleStatus,
          processRemark: item.handleNote,
          processTime: item.handleTime
        }))
        this.historyList = records
        this.historyTotal = data.total || data.totalRecord || 0
      } catch (e) {
        this.historyList = []
      } finally {
        this.historyLoading = false
      }
    },

    // 从warningData JSON中提取触发值
    extractTriggerValue(warningData) {
      try {
        if (typeof warningData === 'string') {
          const parsed = JSON.parse(warningData)
          return parsed.currentValue || parsed.value || parsed.amount || warningData
        }
        return warningData.currentValue || warningData.value || warningData.amount || '-'
      } catch (e) {
        return warningData || '-'
      }
    },

    handleHistoryPageChange(page) {
      this.historyParams.current = page
      this.loadHistoryList()
    },

    // 测试预警
    handleTest(row) {
      this.testWarning = row
      this.testForm = { testValue: 0, testRemark: '' }
      this.testResult = null
      this.testDialogVisible = true
    },

    async handleSubmitTest() {
      this.testLoading = true
      try {
        // 调用测试接口，若后端暂无则模拟结果
        const triggered = this.testForm.testValue > (this.testWarning.thresholdValue || 0)
        this.testResult = {
          triggered,
          message: triggered
            ? `测试值 ${this.testForm.testValue} 超过阈值 ${this.testWarning.thresholdValue}，预警将被触发`
            : `测试值 ${this.testForm.testValue} 未超过阈值 ${this.testWarning.thresholdValue}，预警不会触发`
        }
      } catch (e) {
        this.$message.error('测试失败：' + e.message)
      } finally {
        this.testLoading = false
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算预警'
      this.resetForm()
      this.warningForm = {
        ...row,
        id: null,
        warningCode: '',
        warningName: row.warningName ? '（复制）' + row.warningName : ''
      }
      this.dialogVisible = true
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        await budgetWarningApi.exportSingle(row.id)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该预警记录？删除后不可恢复', '提示', {
          type: 'warning'
        })
        await budgetWarningApi.delete(row.id)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 行点击 - 不自动跳转，避免误触
    handleRowClick(row) {
      // 仅高亮，不弹窗
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

    // 应用列设置
    handleApplyColumns() {
      this.columnOptions.forEach(col => {
        this.$set(this.columnVisible, col.key, this.columnChecked.includes(col.key))
      })
      this.columnSettingVisible = false
    },

    // 重置列设置
    handleResetColumns() {
      this.columnChecked = this.columnOptions.map(col => col.key)
    },
    
    // 判断是否可以处理
    canProcess(row) {
      return row.processStatus === 'PENDING'
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return row.creator === this.$store.getters.name || this.$store.getters.roles.includes('admin')
    },
    
    // 获取值样式类
    getValueClass(currentValue, thresholdValue) {
      if (currentValue > thresholdValue) {
        return 'exceed-value'
      }
      return 'normal-value'
    },
    
    // 获取超出率颜色
    getExceedRateColor(rate) {
      if (rate <= 10) return '#67C23A'
      if (rate <= 30) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取预警类型颜色
    getWarningTypeColor(type) {
      const colorMap = {
        'BUDGET_EXCEED': 'danger',
        'EXECUTION_LOW': 'warning',
        'VARIANCE_HIGH': 'primary',
        'TIME_OVERDUE': 'danger',
        'APPROVAL_DELAY': 'warning',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取预警类型文本
    getWarningTypeText(type) {
      const item = this.warningTypeOptions.find(opt => opt.value === type)
      return item ? item.label : type
    },
    
    // 获取预警级别颜色
    getWarningLevelColor(level) {
      const colorMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取预警级别文本
    getWarningLevelText(level) {
      const item = this.warningLevelOptions.find(opt => opt.value === level)
      return item ? item.label : level
    },
    
    // 获取预警状态类型
    getWarningStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TRIGGERED': 'warning'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取预警状态文本
    getWarningStatusText(status) {
      const item = this.warningStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },
    
    // 获取处理状态类型
    getProcessStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'RESOLVED': 'success',
        'IGNORED': 'info'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取处理状态文本
    getProcessStatusText(status) {
      const item = this.processStatusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    },

    // 获取监控字段文本
    getMonitorFieldText(field) {
      const map = {
        'EXECUTION_RATE': '执行率',
        'VARIANCE_RATE': '差异率',
        'BUDGET_AMOUNT': '预算金额',
        'ACTUAL_AMOUNT': '实际金额'
      }
      return map[field] || field
    },

    // 获取操作符文本
    getOperatorText(op) {
      const map = { 'GT': '大于', 'GTE': '大于等于', 'LT': '小于', 'LTE': '小于等于' }
      return map[op] || op
    },

    // 获取通知方式文本
    getNotificationMethodText(method) {
      const map = { 'EMAIL': '邮件', 'SMS': '短信', 'SYSTEM': '系统通知', 'WECHAT': '微信' }
      return map[method] || method
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-warning {
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
      
      &.triggered-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.pending-card {
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
  
  .threshold-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .normal-value {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #67C23A;
  }
  
  .exceed-value {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #F56C6C;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .warning-conditions {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .conditions-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .warning-process {
    .warning-info {
      margin-bottom: 20px;
      
      h4 {
        color: #303133;
        font-size: 14px;
        margin: 0 0 10px 0;
      }
    }
  }
  
  .danger-text {
    color: #F56C6C;
  }

  .text-right {
    text-align: right;
  }

  .test-result {
    margin-top: 16px;
  }
}
</style>
