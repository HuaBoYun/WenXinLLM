<template>
  <div class="backup-restore">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>备份恢复</h2>
      <p>管理预算系统的数据备份、恢复操作，确保数据安全和业务连续性</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-upload" @click="handleCreateBackup">创建备份</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" @click="handleDownloadBackup">下载备份</el-button>
            <el-button type="info" icon="el-icon-upload2" @click="handleUploadBackup">上传备份</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">备份设置</el-button>
            <el-button icon="el-icon-time" @click="handleSchedule">定时备份</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 备份统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ backupStats.totalBackups }}</div>
            <div class="stat-label">备份总数</div>
            <div class="stat-description">系统中的备份文件</div>
            <div class="stat-trend">
              <i class="el-icon-files"></i>
              <span>数据安全</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-files"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card size-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ backupStats.totalSize }} GB</div>
            <div class="stat-label">总大小</div>
            <div class="stat-description">备份文件总大小</div>
            <div class="stat-trend">
              <i class="el-icon-coin"></i>
              <span>GB</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-coin"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card last-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ backupStats.lastBackupDays >= 0 ? backupStats.lastBackupDays + ' 天' : '无记录' }}</div>
            <div class="stat-label">最后备份</div>
            <div class="stat-description">距离上次备份天数</div>
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
      <el-col :span="6">
        <el-card class="stat-card auto-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ backupStats.autoBackups }}</div>
            <div class="stat-label">自动备份</div>
            <div class="stat-description">自动备份任务数</div>
            <div class="stat-trend">
              <i class="el-icon-timer"></i>
              <span>定时执行</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-timer"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 备份类型选择 -->
    <el-card class="backup-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>备份类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshBackupTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="backupType in backupTypes" :key="backupType.id">
          <el-card
            class="backup-type-item"
            shadow="hover"
            @click.native="handleSelectBackupType(backupType)"
            :class="{ 'selected': selectedBackupType === backupType.id }"
          >
            <div class="backup-type-icon">
              <i :class="backupType.icon"></i>
            </div>
            <div class="backup-type-title">{{ backupType.name }}</div>
            <div class="backup-type-description">{{ backupType.description }}</div>
            <div class="backup-type-stats">
              <span class="backup-count">{{ backupType.backupCount }} 个备份</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 备份列表 -->
    <el-card class="backup-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>备份列表</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索备份"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getBackupList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="backupList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="backupName" label="备份名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.backupName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="backupType" label="备份类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getBackupTypeColor(scope.row.backupType)" size="mini">
              {{ getBackupTypeText(scope.row.backupType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="backupScope" label="备份范围" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ { ALL: '全部', BUDGET_DATA: '预算数据', CONFIG: '配置' }[scope.row.backupScope] || scope.row.backupScope }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="backupSize" label="文件大小" width="100" align="center">
          <template slot-scope="scope">
            <span class="file-size">{{ formatFileSize(scope.row.backupSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="备份时间" width="160" align="center" />
        <el-table-column prop="backupStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.backupStatus)" size="mini">
              {{ getStatusText(scope.row.backupStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tableCount" label="表数" width="70" align="center" />
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="260" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-refresh-left"
              @click.stop="handleRestore(scope.row)"
              :disabled="scope.row.backupStatus !== 'SUCCESS'"
            >恢复</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-download"
              @click.stop="handleDownload(scope.row)"
              :disabled="scope.row.backupStatus !== 'SUCCESS'"
            >下载</el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, scope.row)" @click.native.stop>
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="verify">验证</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="rename">重命名</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 备份详情抽屉 -->
    <el-drawer
      title="备份详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="50%"
    >
      <div class="detail-content" v-if="currentBackup">
        <el-descriptions title="备份基本信息" :column="2" border>
          <el-descriptions-item label="备份名称">{{ currentBackup.backupName }}</el-descriptions-item>
          <el-descriptions-item label="备份类型">{{ getBackupTypeText(currentBackup.backupType) }}</el-descriptions-item>
          <el-descriptions-item label="备份范围">{{ { ALL: '全部数据', BUDGET_DATA: '预算数据', CONFIG: '系统配置' }[currentBackup.backupScope] || currentBackup.backupScope }}</el-descriptions-item>
          <el-descriptions-item label="文件大小">{{ formatFileSize(currentBackup.backupSize) }}</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ currentBackup.startTime }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ currentBackup.endTime }}</el-descriptions-item>
          <el-descriptions-item label="耗时">{{ currentBackup.durationSeconds ? currentBackup.durationSeconds + '秒' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(currentBackup.backupStatus)" size="mini">
              {{ getStatusText(currentBackup.backupStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="表数量">{{ currentBackup.tableCount || '-' }}</el-descriptions-item>
          <el-descriptions-item label="记录数">{{ currentBackup.recordCount || '-' }}</el-descriptions-item>
          <el-descriptions-item label="存储路径">{{ currentBackup.backupPath }}</el-descriptions-item>
          <el-descriptions-item label="是否压缩">{{ currentBackup.isCompressed === 1 ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentBackup.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">备份内容</el-divider>
        <el-table :data="currentBackup.backupContent" border size="mini" v-if="currentBackup.backupContent">
          <el-table-column prop="tableName" label="表名" width="200" />
          <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
          <el-table-column prop="dataSize" label="数据大小" width="120" align="center">
            <template slot-scope="scope">
              {{ formatFileSize(scope.row.dataSize) }}
            </template>
          </el-table-column>
          <el-table-column prop="backupStatus" label="备份状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.backupStatus === 'SUCCESS' ? 'success' : 'danger'" size="mini">
                {{ scope.row.backupStatus === 'SUCCESS' ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" />
        </el-table>

        <el-divider content-position="left">备份日志</el-divider>
        <el-timeline>
          <el-timeline-item
            v-for="log in currentBackup.backupLogs"
            :key="log.id"
            :timestamp="log.logTime"
            :type="getLogType(log.logLevel)"
          >
            {{ log.logMessage }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-drawer>

    <!-- 创建备份对话框 -->
    <el-dialog
      title="创建备份"
      :visible.sync="createBackupDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleCreateBackupDialogClose"
    >
      <el-form
        ref="backupForm"
        :model="backupForm"
        :rules="backupRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="备份名称" prop="backupName">
          <el-input v-model="backupForm.backupName" placeholder="请输入备份名称" />
        </el-form-item>
        <el-form-item label="备份类型" prop="backupType">
          <el-select v-model="backupForm.backupType" placeholder="请选择备份类型" style="width: 100%">
            <el-option value="FULL" label="完整备份" />
            <el-option value="INCREMENTAL" label="增量备份" />
            <el-option value="DIFFERENTIAL" label="差异备份" />
            <el-option value="CUSTOM" label="自定义备份" />
          </el-select>
        </el-form-item>
        <el-form-item label="备份范围" prop="backupScope">
          <el-checkbox-group v-model="backupForm.backupScope">
            <el-checkbox label="BUDGET_DATA">预算数据</el-checkbox>
            <el-checkbox label="USER_DATA">用户数据</el-checkbox>
            <el-checkbox label="SYSTEM_CONFIG">系统配置</el-checkbox>
            <el-checkbox label="AUDIT_LOGS">审计日志</el-checkbox>
            <el-checkbox label="ATTACHMENTS">附件文件</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="压缩方式" prop="compressionType">
          <el-select v-model="backupForm.compressionType" placeholder="请选择压缩方式" style="width: 100%">
            <el-option value="NONE" label="不压缩" />
            <el-option value="ZIP" label="ZIP压缩" />
            <el-option value="GZIP" label="GZIP压缩" />
            <el-option value="7Z" label="7Z压缩" />
          </el-select>
        </el-form-item>
        <el-form-item label="加密备份" prop="encrypted">
          <el-switch v-model="backupForm.encrypted" />
        </el-form-item>
        <el-form-item label="备份描述" prop="description">
          <el-input
            v-model="backupForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入备份描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createBackupDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitBackup" :loading="backupLoading">开始备份</el-button>
      </div>
    </el-dialog>

    <!-- 编辑备份对话框 -->
    <el-dialog
      title="编辑备份"
      :visible.sync="editDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="备份名称" prop="backupName">
          <el-input v-model="editForm.backupName" placeholder="请输入备份名称" />
        </el-form-item>
        <el-form-item label="备份类型" prop="backupType">
          <el-select v-model="editForm.backupType" placeholder="请选择备份类型" style="width: 100%">
            <el-option value="FULL" label="完整备份" />
            <el-option value="INCREMENTAL" label="增量备份" />
            <el-option value="DIFFERENTIAL" label="差异备份" />
            <el-option value="CUSTOM" label="自定义备份" />
          </el-select>
        </el-form-item>
        <el-form-item label="备份范围" prop="backupScope">
          <el-select v-model="editForm.backupScope" placeholder="请选择备份范围" style="width: 100%">
            <el-option value="ALL" label="全部数据" />
            <el-option value="BUDGET_DATA" label="预算数据" />
            <el-option value="CONFIG" label="系统配置" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEdit" :loading="editLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 恢复确认对话框 -->
    <el-dialog
      title="恢复确认"
      :visible.sync="restoreDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="restore-warning">
        <el-alert
          title="警告"
          type="warning"
          description="恢复操作将覆盖当前数据，请确保已做好数据备份。此操作不可逆，请谨慎操作！"
          show-icon
          :closable="false"
        />
      </div>
      <el-form :model="restoreForm" label-width="120px" size="small" style="margin-top: 20px;">
        <el-form-item label="恢复范围">
          <el-checkbox-group v-model="restoreForm.restoreScope">
            <el-checkbox label="BUDGET_DATA">预算数据</el-checkbox>
            <el-checkbox label="USER_DATA">用户数据</el-checkbox>
            <el-checkbox label="SYSTEM_CONFIG">系统配置</el-checkbox>
            <el-checkbox label="AUDIT_LOGS">审计日志</el-checkbox>
            <el-checkbox label="ATTACHMENTS">附件文件</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="恢复模式">
          <el-radio-group v-model="restoreForm.restoreMode">
            <el-radio label="OVERWRITE">覆盖恢复</el-radio>
            <el-radio label="MERGE">合并恢复</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="确认密码" v-if="currentBackup && currentBackup.encrypted">
          <el-input v-model="restoreForm.password" type="password" placeholder="请输入备份密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="restoreDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleConfirmRestore" :loading="restoreLoading">确认恢复</el-button>
      </div>
    </el-dialog>

    <!-- 备份设置对话框 -->
    <el-dialog title="备份设置" :visible.sync="settingsDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="settingsList" v-loading="settingsLoading" border size="small" style="width: 100%">
        <el-table-column prop="settingName" label="设置名称" width="160" />
        <el-table-column prop="settingKey" label="设置键" width="180" />
        <el-table-column label="设置值" min-width="180">
          <template slot-scope="scope">
            <el-input v-model="scope.row.settingValue" size="mini" />
          </template>
        </el-table-column>
        <el-table-column prop="settingDesc" label="说明" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleSaveSetting(scope.row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 定时备份对话框 -->
    <el-dialog title="定时备份管理" :visible.sync="scheduleDialogVisible" width="850px" :close-on-click-modal="false">
      <div style="margin-bottom: 12px;">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleCreateSchedule">新增计划</el-button>
      </div>
      <el-table :data="scheduleList" v-loading="scheduleLoading" border size="small" style="width: 100%">
        <el-table-column prop="scheduleName" label="计划名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="scheduleType" label="执行频率" width="100">
          <template slot-scope="scope">
            <span>{{ { DAILY: '每天', WEEKLY: '每周', MONTHLY: '每月' }[scope.row.scheduleType] || scope.row.scheduleType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="cronExpression" label="Cron表达式" width="140" show-overflow-tooltip />
        <el-table-column prop="backupType" label="备份类型" width="100">
          <template slot-scope="scope">
            <span>{{ getBackupTypeText(scope.row.backupType) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'info'" size="mini">
              {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleToggleSchedule(scope.row)">
              {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button type="text" size="mini" @click="handleEditSchedule(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteSchedule(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 定时备份表单对话框 -->
      <el-dialog :title="scheduleFormTitle" :visible.sync="scheduleFormVisible" width="500px" append-to-body :close-on-click-modal="false">
        <el-form ref="scheduleForm" :model="scheduleForm" :rules="scheduleRules" label-width="100px" size="small">
          <el-form-item label="计划名称" prop="scheduleName">
            <el-input v-model="scheduleForm.scheduleName" placeholder="请输入计划名称" />
          </el-form-item>
          <el-form-item label="执行频率" prop="scheduleType">
            <el-select v-model="scheduleForm.scheduleType" placeholder="请选择" style="width: 100%">
              <el-option label="每天" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
            </el-select>
          </el-form-item>
          <el-form-item label="Cron表达式">
            <el-input v-model="scheduleForm.cronExpression" placeholder="如: 0 2 * * * (每天凌晨2点)" />
          </el-form-item>
          <el-form-item label="备份类型" prop="backupType">
            <el-select v-model="scheduleForm.backupType" placeholder="请选择" style="width: 100%">
              <el-option label="完整备份" value="FULL" />
              <el-option label="增量备份" value="INCREMENTAL" />
              <el-option label="差异备份" value="DIFFERENTIAL" />
              <el-option label="自定义备份" value="CUSTOM" />
            </el-select>
          </el-form-item>
          <el-form-item label="备份范围">
            <el-select v-model="scheduleForm.backupScope" placeholder="请选择" style="width: 100%">
              <el-option label="全部数据" value="ALL" />
              <el-option label="预算数据" value="BUDGET_DATA" />
              <el-option label="系统配置" value="CONFIG" />
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="scheduleForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="scheduleFormVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitSchedule">确定</el-button>
        </div>
      </el-dialog>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="备份恢复帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>备份类型说明</h4>
        <ul>
          <li><b>完整备份</b>：备份所有数据，恢复时可独立使用，适合定期全量备份。</li>
          <li><b>增量备份</b>：仅备份上次备份后变更的数据，速度快、占用空间小。</li>
          <li><b>差异备份</b>：备份上次完整备份后所有变更的数据，恢复速度介于完整和增量之间。</li>
          <li><b>自定义备份</b>：可自由选择备份范围和内容，灵活性最高。</li>
        </ul>
        <h4>操作说明</h4>
        <ul>
          <li><b>创建备份</b>：选择备份类型和范围，系统将自动执行备份任务。</li>
          <li><b>恢复备份</b>：选择已有备份进行数据恢复，支持覆盖和合并两种模式。</li>
          <li><b>下载备份</b>：将备份文件下载到本地保存。</li>
          <li><b>上传备份</b>：上传本地备份文件到系统中。</li>
          <li><b>定时备份</b>：配置自动备份计划，支持每天、每周、每月执行。</li>
        </ul>
        <h4>注意事项</h4>
        <ul>
          <li>恢复操作会覆盖当前数据，请谨慎操作。</li>
          <li>建议定期执行完整备份，日常使用增量备份。</li>
          <li>重要操作前请先创建备份，以便出现问题时快速恢复。</li>
        </ul>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetSystemApi } from '@/api/managementAccountant/ncv65/budgetSystem'

export default {
  name: 'BackupRestore',
  data() {
    return {
      // 统计数据
      backupStats: {
        totalBackups: 0,
        totalSize: '0',
        lastBackupDays: 0,
        autoBackups: 0
      },

      // 备份类型
      backupTypes: [
        { id: 'FULL', name: '完整备份', description: '备份所有数据', icon: 'el-icon-files', backupCount: 0 },
        { id: 'INCREMENTAL', name: '增量备份', description: '备份变更数据', icon: 'el-icon-upload', backupCount: 0 },
        { id: 'DIFFERENTIAL', name: '差异备份', description: '备份差异数据', icon: 'el-icon-upload2', backupCount: 0 },
        { id: 'CUSTOM', name: '自定义备份', description: '自定义备份范围', icon: 'el-icon-setting', backupCount: 0 }
      ],
      selectedBackupType: null,

      // 备份列表
      backupList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      currentBackup: null,

      // 创建备份对话框
      createBackupDialogVisible: false,
      backupLoading: false,
      backupForm: {
        backupName: '',
        backupType: '',
        backupScope: [],
        compressionType: 'ZIP',
        encrypted: false,
        description: ''
      },
      backupRules: {
        backupName: [
          { required: true, message: '请输入备份名称', trigger: 'blur' }
        ],
        backupType: [
          { required: true, message: '请选择备份类型', trigger: 'change' }
        ],
        backupScope: [
          { required: true, message: '请选择备份范围', trigger: 'change' }
        ]
      },

      // 恢复对话框
      restoreDialogVisible: false,
      restoreLoading: false,
      restoreForm: {
        restoreScope: [],
        restoreMode: 'OVERWRITE',
        password: ''
      },

      // 备份设置对话框
      settingsDialogVisible: false,
      settingsLoading: false,
      settingsList: [],

      // 定时备份对话框
      scheduleDialogVisible: false,
      scheduleLoading: false,
      scheduleList: [],
      scheduleFormVisible: false,
      scheduleFormTitle: '新增定时备份',
      scheduleForm: {
        scheduleName: '',
        scheduleType: 'DAILY',
        cronExpression: '',
        backupType: 'FULL',
        backupScope: 'ALL',
        isEnabled: 1,
        remark: ''
      },
      scheduleRules: {
        scheduleName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        scheduleType: [{ required: true, message: '请选择执行频率', trigger: 'change' }],
        backupType: [{ required: true, message: '请选择备份类型', trigger: 'change' }]
      },

      // 帮助对话框
      helpDialogVisible: false,

      // 编辑对话框
      editDialogVisible: false,
      editLoading: false,
      editForm: {
        backupId: '',
        backupName: '',
        backupType: '',
        backupScope: '',
        remark: ''
      },
      editRules: {
        backupName: [{ required: true, message: '请输入备份名称', trigger: 'blur' }],
        backupType: [{ required: true, message: '请选择备份类型', trigger: 'change' }]
      }
    }
  },

  created() {
    this.getBackupList()
    this.getBackupStats()
    this.getBackupTypeStats()
  },

  methods: {
    // 获取备份列表
    async getBackupList() {
      this.loading = true
      try {
        const params = {}
        if (this.selectedBackupType) {
          params.backupType = this.selectedBackupType
        }
        if (this.searchKeyword) {
          params.backupName = this.searchKeyword
        }
        const response = await budgetSystemApi.getBackupList(params)
        if (response.code === 1 && response.data) {
          const data = response.data
          this.backupList = data.tlist || data.list || data || []
        }
      } catch (error) {
        this.$message.error('获取备份列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getBackupStats() {
      try {
        const response = await budgetSystemApi.getBackupStats()
        if (response.code === 1 && response.data) {
          this.backupStats = {
            totalBackups: response.data.totalBackups || 0,
            totalSize: response.data.totalSize || '0',
            lastBackupDays: response.data.lastBackupDays != null ? response.data.lastBackupDays : -1,
            autoBackups: response.data.autoBackups || 0
          }
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      }
    },

    // 获取备份类型统计
    async getBackupTypeStats() {
      try {
        const response = await budgetSystemApi.getBackupTypeStats()
        if (response.code === 1 && response.data) {
          // 后端返回 [{backupType: "FULL", count: 8}, ...] 数组格式
          const statsArray = Array.isArray(response.data) ? response.data : []
          const countMap = {}
          statsArray.forEach(item => {
            countMap[item.backupType] = item.count || 0
          })
          // Vue 2 响应式：替换整个数组触发更新
          this.backupTypes = this.backupTypes.map(bt => ({
            ...bt,
            backupCount: countMap[bt.id] !== undefined ? countMap[bt.id] : 0
          }))
        }
      } catch (error) {
        console.warn('获取备份类型统计失败', error)
      }
    },

    // 创建备份
    handleCreateBackup() {
      this.createBackupDialogVisible = true
      this.resetBackupForm()
    },

    // 查看详情
    async handleView(row) {
      try {
        const response = await budgetSystemApi.getBackupDetail(row.backupId)
        if (response.code === 1 && response.data) {
          this.currentBackup = response.data
          this.detailDrawerVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败：' + error.message)
      }
    },

    // 恢复备份
    handleRestore(row) {
      this.currentBackup = row
      this.restoreDialogVisible = true
      this.resetRestoreForm()
    },

    // 下载备份
    async handleDownload(row) {
      try {
        const response = await budgetSystemApi.downloadBackup(row.backupId)
        if (response) {
          const blob = new Blob([response], { type: 'application/octet-stream' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = (row.backupName || '备份文件') + '.bak'
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('下载成功')
        }
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'verify':
          this.handleVerifyBackup(row)
          break
        case 'copy':
          this.handleCopyBackup(row)
          break
        case 'rename':
          this.handleRenameBackup(row)
          break
        case 'delete':
          this.handleDeleteBackup(row)
          break
      }
    },

    // 验证备份
    async handleVerifyBackup(row) {
      try {
        const response = await budgetSystemApi.verifyBackup(row.backupId)
        if (response.code === 1) {
          this.$message.success('验证成功：备份文件完整')
        } else {
          this.$message.error(response.msg || '验证失败')
        }
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },

    // 复制备份
    async handleCopyBackup(row) {
      try {
        const response = await budgetSystemApi.copyBackup(row.backupId)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.getBackupList()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 重命名备份
    handleRenameBackup(row) {
      this.$prompt('请输入新的备份名称', '重命名', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.backupName
      }).then(async ({ value }) => {
        try {
          const response = await budgetSystemApi.renameBackup(row.backupId, value)
          if (response.code === 1) {
            this.$message.success('重命名成功')
            this.getBackupList()
          } else {
            this.$message.error(response.msg || '重命名失败')
          }
        } catch (error) {
          this.$message.error('重命名失败：' + error.message)
        }
      })
    },

    // 删除备份
    handleDeleteBackup(row) {
      this.$confirm('确定删除该备份吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetSystemApi.deleteBackup(row.backupId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getBackupList()
            this.getBackupStats()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交备份
    async handleSubmitBackup() {
      this.$refs.backupForm.validate(async (valid) => {
        if (valid) {
          this.backupLoading = true
          try {
            const response = await budgetSystemApi.createBackup(this.backupForm)
            if (response.code === 1) {
              this.$message.success('备份任务已启动')
              this.createBackupDialogVisible = false
              this.getBackupList()
              this.getBackupStats()
            } else {
              this.$message.error(response.msg || '创建备份失败')
            }
          } catch (error) {
            this.$message.error('创建备份失败：' + error.message)
          } finally {
            this.backupLoading = false
          }
        }
      })
    },

    // 确认恢复
    async handleConfirmRestore() {
      this.restoreLoading = true
      try {
        const restoreData = {
          backupId: this.currentBackup.backupId,
          ...this.restoreForm
        }
        const response = await budgetSystemApi.restoreBackup(restoreData)
        if (response.code === 1) {
          this.$message.success('恢复任务已启动')
          this.restoreDialogVisible = false
          this.getBackupList()
        } else {
          this.$message.error(response.msg || '恢复失败')
        }
      } catch (error) {
        this.$message.error('恢复失败：' + error.message)
      } finally {
        this.restoreLoading = false
      }
    },

    // 重置备份表单
    resetBackupForm() {
      this.backupForm = {
        backupName: '',
        backupType: '',
        backupScope: [],
        compressionType: 'ZIP',
        encrypted: false,
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.backupForm && this.$refs.backupForm.clearValidate()
      })
    },

    // 重置恢复表单
    resetRestoreForm() {
      this.restoreForm = {
        restoreScope: [],
        restoreMode: 'OVERWRITE',
        password: ''
      }
    },

    // 创建备份对话框关闭
    handleCreateBackupDialogClose() {
      this.resetBackupForm()
    },

    // 刷新
    handleRefresh() {
      this.getBackupList()
      this.getBackupStats()
      this.getBackupTypeStats()
    },

    // 下载备份（顶部按钮）
    handleDownloadBackup() {
      if (!this.backupList || this.backupList.length === 0) {
        this.$message.warning('暂无可下载的备份')
        return
      }
      this.$message.info('请在备份列表中选择需要下载的备份，点击操作列的下载按钮')
    },

    // 上传备份
    handleUploadBackup() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.bak,.sql,.zip,.gz'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        const formData = new FormData()
        formData.append('file', file)
        try {
          const response = await budgetSystemApi.uploadBackup(formData)
          if (response.code === 1) {
            this.$message.success('上传成功')
            this.getBackupList()
            this.getBackupStats()
          } else {
            this.$message.error(response.msg || '上传失败')
          }
        } catch (error) {
          this.$message.error('上传失败：' + error.message)
        }
      }
      input.click()
    },

    // 备份设置
    handleSettings() {
      this.settingsDialogVisible = true
      this.loadSettings()
    },

    // 加载设置列表
    async loadSettings() {
      this.settingsLoading = true
      try {
        const response = await budgetSystemApi.getBackupSettings()
        if (response.code === 1 && response.data) {
          this.settingsList = response.data
        }
      } catch (error) {
        this.$message.error('加载设置失败：' + error.message)
      } finally {
        this.settingsLoading = false
      }
    },

    // 保存单个设置
    async handleSaveSetting(row) {
      try {
        const response = await budgetSystemApi.saveBackupSetting({
          settingKey: row.settingKey,
          settingName: row.settingName,
          settingValue: row.settingValue,
          settingDesc: row.settingDesc
        })
        if (response.code === 1) {
          this.$message.success('保存成功')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 定时备份
    handleSchedule() {
      this.scheduleDialogVisible = true
      this.loadSchedules()
    },

    // 加载定时备份列表
    async loadSchedules() {
      this.scheduleLoading = true
      try {
        const response = await budgetSystemApi.getScheduleList({})
        if (response.code === 1 && response.data) {
          const data = response.data
          this.scheduleList = data.tlist || data.list || data || []
        }
      } catch (error) {
        this.$message.error('加载定时备份列表失败：' + error.message)
      } finally {
        this.scheduleLoading = false
      }
    },

    // 新增定时备份
    handleCreateSchedule() {
      this.scheduleFormTitle = '新增定时备份'
      this.scheduleForm = {
        scheduleName: '',
        scheduleType: 'DAILY',
        cronExpression: '',
        backupType: 'FULL',
        backupScope: 'ALL',
        isEnabled: 1,
        remark: ''
      }
      this.scheduleFormVisible = true
    },

    // 编辑定时备份
    handleEditSchedule(row) {
      this.scheduleFormTitle = '编辑定时备份'
      this.scheduleForm = { ...row }
      this.scheduleFormVisible = true
    },

    // 提交定时备份表单
    async handleSubmitSchedule() {
      this.$refs.scheduleForm.validate(async (valid) => {
        if (!valid) return
        try {
          let response
          if (this.scheduleForm.scheduleId) {
            response = await budgetSystemApi.updateSchedule(this.scheduleForm)
          } else {
            response = await budgetSystemApi.createSchedule(this.scheduleForm)
          }
          if (response.code === 1) {
            this.$message.success(this.scheduleForm.scheduleId ? '更新成功' : '创建成功')
            this.scheduleFormVisible = false
            this.loadSchedules()
            this.getBackupStats()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        }
      })
    },

    // 删除定时备份
    handleDeleteSchedule(row) {
      this.$confirm('确定删除该定时备份计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await budgetSystemApi.deleteSchedule(row.scheduleId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadSchedules()
            this.getBackupStats()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 启用/禁用定时备份
    async handleToggleSchedule(row) {
      try {
        const response = await budgetSystemApi.toggleSchedule(row.scheduleId)
        if (response.code === 1) {
          this.$message.success(row.isEnabled === 1 ? '已禁用' : '已启用')
          this.loadSchedules()
          this.getBackupStats()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },

    // 帮助
    handleHelp() {
      this.helpDialogVisible = true
    },

    // 刷新备份类型
    refreshBackupTypes() {
      this.getBackupList()
      this.getBackupStats()
      this.getBackupTypeStats()
      this.$message.success('备份类型已刷新')
    },

    // 选择备份类型
    handleSelectBackupType(backupType) {
      if (this.selectedBackupType === backupType.id) {
        this.selectedBackupType = null
      } else {
        this.selectedBackupType = backupType.id
      }
      this.getBackupList()
    },

    // 行点击（已移除@row-click绑定，保留方法避免引用报错）

    // 编辑备份
    handleEdit(row) {
      this.editForm = {
        backupId: row.backupId,
        backupName: row.backupName,
        backupType: row.backupType,
        backupScope: row.backupScope || '',
        remark: row.remark || ''
      }
      this.editDialogVisible = true
    },

    // 提交编辑
    async handleSubmitEdit() {
      this.$refs.editForm.validate(async (valid) => {
        if (!valid) return
        this.editLoading = true
        try {
          const response = await budgetSystemApi.updateBackup(this.editForm)
          if (response.code === 1) {
            this.$message.success('更新成功')
            this.editDialogVisible = false
            this.getBackupList()
            this.getBackupTypeStats()
          } else {
            this.$message.error(response.msg || '更新失败')
          }
        } catch (error) {
          this.$message.error('更新失败：' + error.message)
        } finally {
          this.editLoading = false
        }
      })
    },

    // 格式化文件大小（后端返回MB单位）
    formatFileSize(size) {
      if (size == null || size === '') return '0 MB'
      const mb = parseFloat(size)
      if (isNaN(mb)) return '0 MB'
      if (mb < 1) {
        return (mb * 1024).toFixed(0) + ' KB'
      } else if (mb >= 1024) {
        return (mb / 1024).toFixed(2) + ' GB'
      } else {
        return mb.toFixed(2) + ' MB'
      }
    },

    // 获取备份类型颜色
    getBackupTypeColor(type) {
      const colorMap = {
        'FULL': 'primary',
        'INCREMENTAL': 'success',
        'DIFFERENTIAL': 'warning',
        'CUSTOM': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取备份类型文本
    getBackupTypeText(type) {
      const textMap = {
        'FULL': '完整备份',
        'INCREMENTAL': '增量备份',
        'DIFFERENTIAL': '差异备份',
        'CUSTOM': '自定义备份'
      }
      return textMap[type] || type
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'RUNNING': 'warning',
        'PENDING': 'info'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'RUNNING': '进行中',
        'PENDING': '等待中'
      }
      return textMap[status] || status
    },

    // 获取日志类型
    getLogType(level) {
      const typeMap = {
        'INFO': 'primary',
        'WARN': 'warning',
        'ERROR': 'danger',
        'SUCCESS': 'success'
      }
      return typeMap[level] || 'primary'
    }
  }
}
</script>


<style scoped>
.help-content h4 {
  margin: 16px 0 8px;
  color: #303133;
}
.help-content ul {
  padding-left: 20px;
  margin: 0 0 12px;
}
.help-content li {
  line-height: 1.8;
  color: #606266;
}
</style>