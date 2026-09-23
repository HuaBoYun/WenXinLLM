<template>
  <div class="data-mapping">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>数据字段映射</h2>
      <p>配置不同系统间的数据字段映射关系，支持数据转换和格式化</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateMapping">创建映射</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-connection" @click="handleTestMapping">测试映射</el-button>
            <el-button type="info" icon="el-icon-document" @click="handleImportMapping">导入映射</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">映射设置</el-button>
            <el-button icon="el-icon-download" @click="handleExport">导出映射</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据映射统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mappingStats.totalMappings }}</div>
            <div class="stat-label">映射总数</div>
            <div class="stat-description">已配置映射规则数</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>数据映射</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card active-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mappingStats.activeMappings }}</div>
            <div class="stat-label">活跃映射</div>
            <div class="stat-description">正在使用的映射</div>
            <div class="stat-trend">
              <i class="el-icon-loading"></i>
              <span>运行中</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card fields-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mappingStats.totalFields }}</div>
            <div class="stat-label">字段总数</div>
            <div class="stat-description">已映射字段数量</div>
            <div class="stat-trend">
              <i class="el-icon-document"></i>
              <span>字段映射</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card success-rate-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ mappingStats.successRate }}%</div>
            <div class="stat-label">映射成功率</div>
            <div class="stat-description">数据映射成功率</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高成功率</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 映射类型选择 -->
    <el-card class="mapping-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>映射类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshMappingTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="mappingType in mappingTypes" :key="mappingType.id">
          <el-card
            class="mapping-type-item"
            shadow="hover"
            @click.native="handleSelectMappingType(mappingType)"
            :class="{ 'selected': selectedMappingType === mappingType.id }"
          >
            <div class="mapping-type-icon">
              <i :class="mappingType.icon"></i>
            </div>
            <div class="mapping-type-title">{{ mappingType.name }}</div>
            <div class="mapping-type-description">{{ mappingType.description }}</div>
            <div class="mapping-type-stats">
              <span class="mapping-count">{{ mappingType.mappingCount }} 个映射</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据映射列表 -->
    <el-card class="mapping-list-card" shadow="never">
      <div slot="header" class="card-header">
        <span>数据映射管理</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索映射"
            size="mini"
            style="width: 200px; margin-right: 10px;"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getMappingList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="filteredMappingList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="mappingName" label="映射名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.mappingName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="sourceSystem" label="源系统" width="150" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSystemColor(scope.row.sourceSystem)" size="mini">
              {{ getSystemText(scope.row.sourceSystem) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetSystem" label="目标系统" width="150" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSystemColor(scope.row.targetSystem)" size="mini">
              {{ getSystemText(scope.row.targetSystem) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="mappingType" label="映射类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMappingTypeColor(scope.row.mappingType)" size="mini">
              {{ getMappingTypeText(scope.row.mappingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fieldCount" label="字段数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="field-count">{{ scope.row.fieldCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="successRate" label="成功率" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getSuccessRateClass(scope.row.successRate)">
              {{ scope.row.successRate }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="lastSync" label="最后同步" width="150" align="center">
          <template slot-scope="scope">
            <span class="last-sync">{{ scope.row.lastSync }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-connection"
              @click="handleTestMapping(scope.row)"
            >测试</el-button>
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
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="export">导出</el-dropdown-item>
                <el-dropdown-item command="logs">日志</el-dropdown-item>
                <el-dropdown-item command="preview">预览</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 映射详情抽屉 -->
    <el-drawer
      title="数据映射详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="80%"
    >
      <div class="detail-content" v-if="currentMapping">
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="映射基本信息" :column="2" border>
              <el-descriptions-item label="映射名称">{{ currentMapping.mappingName }}</el-descriptions-item>
              <el-descriptions-item label="映射类型">{{ getMappingTypeText(currentMapping.mappingType) }}</el-descriptions-item>
              <el-descriptions-item label="源系统">{{ getSystemText(currentMapping.sourceSystem) }}</el-descriptions-item>
              <el-descriptions-item label="目标系统">{{ getSystemText(currentMapping.targetSystem) }}</el-descriptions-item>
              <el-descriptions-item label="字段数量">{{ currentMapping.fieldCount }}</el-descriptions-item>
              <el-descriptions-item label="成功率">{{ currentMapping.successRate }}%</el-descriptions-item>
              <el-descriptions-item label="最后同步">{{ currentMapping.lastSync }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentMapping.status)" size="mini">
                  {{ getStatusText(currentMapping.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentMapping.createTime }}</el-descriptions-item>
              <el-descriptions-item label="映射描述" :span="2">{{ currentMapping.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="字段映射" name="fields">
            <div class="field-mapping-container">
              <el-button type="primary" size="mini" @click="handleAddFieldMapping" style="margin-bottom: 10px;">
                添加字段映射
              </el-button>
              <el-table :data="fieldMappings" border size="mini">
                <el-table-column prop="sourceField" label="源字段" width="200" />
                <el-table-column prop="sourceType" label="源类型" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getFieldTypeColor(scope.row.sourceType)" size="mini">
                      {{ scope.row.sourceType }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="targetField" label="目标字段" width="200" />
                <el-table-column prop="targetType" label="目标类型" width="100" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getFieldTypeColor(scope.row.targetType)" size="mini">
                      {{ scope.row.targetType }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="transformRule" label="转换规则" width="150" />
                <el-table-column prop="defaultValue" label="默认值" width="120" />
                <el-table-column prop="required" label="必填" width="80" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="scope.row.required ? 'danger' : 'info'" size="mini">
                      {{ scope.row.required ? '是' : '否' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" align="center">
                  <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="handleEditFieldMapping(scope.row)">
                      编辑
                    </el-button>
                    <el-button type="text" size="mini" @click="handleDeleteFieldMapping(scope.row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="转换规则" name="transform">
            <el-form label-width="120px" size="small">
              <el-form-item label="数据格式">
                <el-input :value="currentMapping.dataFormat" readonly />
              </el-form-item>
              <el-form-item label="字符编码">
                <el-input :value="currentMapping.encoding" readonly />
              </el-form-item>
              <el-form-item label="日期格式">
                <el-input :value="currentMapping.dateFormat" readonly />
              </el-form-item>
              <el-form-item label="数字格式">
                <el-input :value="currentMapping.numberFormat" readonly />
              </el-form-item>
              <el-form-item label="空值处理">
                <el-input :value="currentMapping.nullHandling" readonly />
              </el-form-item>
              <el-form-item label="错误处理">
                <el-input :value="currentMapping.errorHandling" readonly />
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="同步日志" name="logs">
            <el-table :data="mappingLogs" border size="mini">
              <el-table-column prop="syncTime" label="同步时间" width="150" />
              <el-table-column prop="recordCount" label="记录数" width="100" align="center" />
              <el-table-column prop="successCount" label="成功数" width="100" align="center" />
              <el-table-column prop="errorCount" label="错误数" width="100" align="center" />
              <el-table-column prop="duration" label="耗时" width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row.duration }}ms</span>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getSyncStatusColor(scope.row.status)" size="mini">
                    {{ getSyncStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="errorMessage" label="错误信息" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑映射对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="mappingForm"
        :model="mappingForm"
        :rules="mappingRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="映射名称" prop="mappingName">
              <el-input v-model="mappingForm.mappingName" placeholder="请输入映射名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="映射类型" prop="mappingType">
              <el-select v-model="mappingForm.mappingType" placeholder="请选择映射类型" style="width: 100%">
                <el-option value="FIELD" label="字段映射" />
                <el-option value="TABLE" label="表映射" />
                <el-option value="OBJECT" label="对象映射" />
                <el-option value="CUSTOM" label="自定义映射" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源系统" prop="sourceSystem">
              <el-select v-model="mappingForm.sourceSystem" placeholder="请选择源系统" style="width: 100%">
                <el-option value="ERP" label="ERP系统" />
                <el-option value="CRM" label="CRM系统" />
                <el-option value="DATABASE" label="数据库" />
                <el-option value="API" label="API接口" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标系统" prop="targetSystem">
              <el-select v-model="mappingForm.targetSystem" placeholder="请选择目标系统" style="width: 100%">
                <el-option value="NCV65" label="NCV65预算系统" />
                <el-option value="DATABASE" label="数据库" />
                <el-option value="FILE" label="文件" />
                <el-option value="API" label="API接口" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="转换类型">
              <el-select v-model="mappingForm.transformType" placeholder="请选择转换类型" style="width: 100%">
                <el-option value="DIRECT" label="直接映射" />
                <el-option value="FORMULA" label="公式转换" />
                <el-option value="LOOKUP" label="查找转换" />
                <el-option value="SCRIPT" label="脚本转换" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="映射描述" prop="description">
          <el-input
            v-model="mappingForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入映射描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button @click="handleTestMappingForm">测试映射</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量测试映射对话框 -->
    <el-dialog title="测试映射" :visible.sync="testDialogVisible" width="600px">
      <div v-if="!testResult">
        <p>请选择要测试的映射：</p>
        <el-checkbox-group v-model="selectedTestIds">
          <el-checkbox v-for="item in mappingList" :key="item.id" :label="item.id" style="display: block; margin: 8px 0;">
            {{ item.mappingName }} ({{ getStatusText(item.status) }})
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <div v-else>
        <el-result :icon="testResult.failCount > 0 ? 'warning' : 'success'" :title="'测试完成：成功 ' + testResult.successCount + ' 个，失败 ' + testResult.failCount + ' 个'">
          <template slot="extra">
            <el-table :data="testResult.details" border size="mini" style="margin-top: 10px;">
              <el-table-column prop="mappingName" label="映射名称" />
              <el-table-column prop="success" label="结果" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.success ? 'success' : 'danger'" size="mini">{{ scope.row.success ? '成功' : '失败' }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-result>
      </div>
      <div slot="footer">
        <el-button @click="testDialogVisible = false; testResult = null">关闭</el-button>
        <el-button v-if="!testResult" type="primary" @click="executeBatchTest" :loading="testLoading" :disabled="selectedTestIds.length === 0">开始测试</el-button>
      </div>
    </el-dialog>

    <!-- 导入映射对话框 -->
    <el-dialog title="导入映射" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        ref="importUpload"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleImportFileChange"
        :limit="1"
        accept=".json"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">仅支持 .json 格式文件</div>
      </el-upload>
      <div v-if="importPreview" style="margin-top: 15px;">
        <el-descriptions title="预览导入数据" :column="1" border size="mini">
          <el-descriptions-item label="映射名称">{{ importPreview.mappingName || importPreview.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="映射类型">{{ getMappingTypeText(importPreview.mappingType) }}</el-descriptions-item>
          <el-descriptions-item label="源系统">{{ getSystemText(importPreview.sourceSystem) }}</el-descriptions-item>
          <el-descriptions-item label="目标系统">{{ getSystemText(importPreview.targetSystem) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="importDialogVisible = false; importPreview = null">取消</el-button>
        <el-button type="primary" @click="executeImport" :loading="importLoading" :disabled="!importPreview">确认导入</el-button>
      </div>
    </el-dialog>

    <!-- 映射设置对话框 -->
    <el-dialog title="映射设置" :visible.sync="settingsDialogVisible" width="600px">
      <el-form :model="settingsForm" label-width="140px" size="small" v-loading="settingsLoading">
        <el-form-item label="默认映射类型">
          <el-select v-model="settingsForm.defaultMappingType" style="width: 100%">
            <el-option value="FIELD" label="字段映射" />
            <el-option value="TABLE" label="表映射" />
            <el-option value="OBJECT" label="对象映射" />
            <el-option value="CUSTOM" label="自定义映射" />
          </el-select>
        </el-form-item>
        <el-form-item label="默认转换类型">
          <el-select v-model="settingsForm.defaultTransformType" style="width: 100%">
            <el-option value="DIRECT" label="直接映射" />
            <el-option value="FORMULA" label="公式转换" />
            <el-option value="LOOKUP" label="查找转换" />
            <el-option value="SCRIPT" label="脚本转换" />
          </el-select>
        </el-form-item>
        <el-form-item label="空值处理">
          <el-select v-model="settingsForm.nullValueHandling" style="width: 100%">
            <el-option value="SKIP" label="跳过" />
            <el-option value="DEFAULT" label="使用默认值" />
            <el-option value="ERROR" label="报错" />
          </el-select>
        </el-form-item>
        <el-form-item label="错误处理">
          <el-select v-model="settingsForm.errorHandling" style="width: 100%">
            <el-option value="CONTINUE" label="继续执行" />
            <el-option value="STOP" label="停止执行" />
            <el-option value="ROLLBACK" label="回滚" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期格式">
          <el-input v-model="settingsForm.dateFormat" />
        </el-form-item>
        <el-form-item label="数字格式">
          <el-input v-model="settingsForm.numberFormat" />
        </el-form-item>
        <el-form-item label="字符编码">
          <el-select v-model="settingsForm.encoding" style="width: 100%">
            <el-option value="UTF-8" label="UTF-8" />
            <el-option value="GBK" label="GBK" />
            <el-option value="GB2312" label="GB2312" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大字段数">
          <el-input-number v-model="settingsForm.maxFieldCount" :min="1" :max="10000" />
        </el-form-item>
        <el-form-item label="创建时自动测试">
          <el-switch v-model="settingsForm.autoTestOnCreate" />
        </el-form-item>
        <el-form-item label="启用批量映射">
          <el-switch v-model="settingsForm.enableBatchMapping" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSettings" :loading="settingsSaveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 导出映射对话框 -->
    <el-dialog title="导出映射" :visible.sync="exportDialogVisible" width="500px">
      <el-form label-width="100px" size="small">
        <el-form-item label="导出范围">
          <el-radio-group v-model="exportScope">
            <el-radio label="all">全部映射</el-radio>
            <el-radio label="selected">当前筛选结果</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="导出格式">
          <el-radio-group v-model="exportFormat">
            <el-radio label="json">JSON</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div style="margin-top: 10px; color: #909399; font-size: 13px;">
        <p>将导出 {{ exportScope === 'all' ? mappingList.length : filteredMappingList.length }} 条映射记录</p>
      </div>
      <div slot="footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="executeExport">确认导出</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="数据映射帮助" :visible.sync="helpDialogVisible" width="650px">
      <div style="line-height: 2;">
        <h4>功能说明</h4>
        <p>数据映射管理模块用于配置不同系统间的数据字段映射关系，支持数据转换和格式化。</p>
        <h4>操作指南</h4>
        <ul>
          <li><b>创建映射</b>：点击"创建映射"按钮，填写映射名称、类型、源系统、目标系统等信息。</li>
          <li><b>测试映射</b>：选择映射后点击"测试映射"，验证映射配置是否正确。</li>
          <li><b>导入映射</b>：支持从 JSON 文件导入映射配置。</li>
          <li><b>导出映射</b>：将映射配置导出为 JSON 文件，便于备份和迁移。</li>
          <li><b>映射设置</b>：配置全局映射参数，如默认类型、空值处理、错误处理等。</li>
        </ul>
        <h4>映射类型说明</h4>
        <ul>
          <li><b>字段映射</b>：单个字段之间的一对一映射。</li>
          <li><b>表映射</b>：整张表的结构映射。</li>
          <li><b>对象映射</b>：复杂对象之间的映射。</li>
          <li><b>自定义映射</b>：通过脚本或公式实现的自定义映射逻辑。</li>
        </ul>
        <h4>状态说明</h4>
        <ul>
          <li><b>启用</b>：映射正在使用中。</li>
          <li><b>停用</b>：映射已停用。</li>
          <li><b>待启用</b>：映射已创建但尚未启用。</li>
        </ul>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpDialogVisible = false">知道了</el-button>
      </div>
    </el-dialog>

    <!-- 日志查看对话框 -->
    <el-dialog title="映射日志" :visible.sync="logsDialogVisible" width="700px">
      <el-table :data="currentLogs" border size="mini" v-loading="logsLoading">
        <el-table-column prop="syncTime" label="同步时间" width="160">
          <template slot-scope="scope">{{ formatDate(scope.row.syncTime) }}</template>
        </el-table-column>
        <el-table-column prop="recordCount" label="记录数" width="80" align="center" />
        <el-table-column prop="successCount" label="成功数" width="80" align="center" />
        <el-table-column prop="errorCount" label="错误数" width="80" align="center" />
        <el-table-column prop="duration" label="耗时" width="80" align="center">
          <template slot-scope="scope">{{ scope.row.duration }}ms</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSyncStatusColor(scope.row.status)" size="mini">{{ getSyncStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorMessage" label="错误信息" />
      </el-table>
      <div v-if="currentLogs.length === 0 && !logsLoading" style="text-align: center; padding: 20px; color: #909399;">暂无日志记录</div>
      <div slot="footer">
        <el-button @click="logsDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog title="映射预览" :visible.sync="previewDialogVisible" width="700px">
      <div v-if="previewData">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="映射名称">{{ previewData.mappingName }}</el-descriptions-item>
          <el-descriptions-item label="映射编码">{{ previewData.mappingCode }}</el-descriptions-item>
          <el-descriptions-item label="映射类型">{{ getMappingTypeText(previewData.mappingType) }}</el-descriptions-item>
          <el-descriptions-item label="转换类型">{{ previewData.transformType || '-' }}</el-descriptions-item>
          <el-descriptions-item label="源系统">{{ getSystemText(previewData.sourceSystem) }}</el-descriptions-item>
          <el-descriptions-item label="目标系统">{{ getSystemText(previewData.targetSystem) }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusColor(previewData.status)" size="mini">{{ getStatusText(previewData.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="成功率">{{ previewData.successRate }}%</el-descriptions-item>
          <el-descriptions-item label="字段数量">{{ previewData.fieldCount }}</el-descriptions-item>
          <el-descriptions-item label="最后同步">{{ formatDate(previewData.lastSync) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ formatDate(previewData.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ previewData.description || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="previewDialogVisible = false; handleEdit(previewData)">编辑</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { systemIntegrationApi } from '@/api/managementAccountant/ncv65/systemIntegration'

export default {
  name: 'DataMapping',
  data() {
    return {
      // 统计数据
      mappingStats: {
        totalMappings: 0,
        activeMappings: 0,
        totalFields: 0,
        successRate: 0,
      },
      // 映射类型
      mappingTypes: [],
      selectedMappingType: null,
      // 映射列表
      mappingList: [],
      loading: false,
      searchKeyword: '',
      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentMapping: null,
      fieldMappings: [],
      mappingLogs: [],
      // 创建/编辑对话框
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      mappingForm: {
        mappingName: '',
        mappingType: '',
        sourceSystem: '',
        targetSystem: '',
        transformType: '',
        description: '',
      },
      mappingRules: {
        mappingName: [{ required: true, message: '请输入映射名称', trigger: 'blur' }],
        mappingType: [{ required: true, message: '请选择映射类型', trigger: 'change' }],
        sourceSystem: [{ required: true, message: '请选择源系统', trigger: 'change' }],
        targetSystem: [{ required: true, message: '请选择目标系统', trigger: 'change' }],
      },
      // 批量测试对话框
      testDialogVisible: false,
      testLoading: false,
      testResult: null,
      selectedTestIds: [],
      // 导入对话框
      importDialogVisible: false,
      importLoading: false,
      importPreview: null,
      importFileData: null,
      // 设置对话框
      settingsDialogVisible: false,
      settingsLoading: false,
      settingsSaveLoading: false,
      settingsForm: {
        defaultMappingType: 'FIELD',
        defaultTransformType: 'DIRECT',
        nullValueHandling: 'SKIP',
        errorHandling: 'CONTINUE',
        dateFormat: 'yyyy-MM-dd HH:mm:ss',
        numberFormat: '#,##0.00',
        encoding: 'UTF-8',
        maxFieldCount: 500,
        autoTestOnCreate: false,
        enableBatchMapping: true,
      },
      // 导出对话框
      exportDialogVisible: false,
      exportScope: 'all',
      exportFormat: 'json',
      // 帮助对话框
      helpDialogVisible: false,
      // 日志对话框
      logsDialogVisible: false,
      logsLoading: false,
      currentLogs: [],
      // 预览对话框
      previewDialogVisible: false,
      previewData: null,
    }
  },

  computed: {
    filteredMappingList() {
      let list = this.mappingList
      // 按映射类型筛选
      if (this.selectedMappingType) {
        list = list.filter((item) => item.mappingType === this.selectedMappingType)
      }
      // 按关键字搜索
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(
          (item) =>
            (item.mappingName && item.mappingName.toLowerCase().includes(kw)) ||
            (item.mappingType && item.mappingType.toLowerCase().includes(kw)) ||
            (item.sourceSystem && item.sourceSystem.toLowerCase().includes(kw)) ||
            (item.targetSystem && item.targetSystem.toLowerCase().includes(kw)) ||
            (item.status && item.status.toLowerCase().includes(kw))
        )
      }
      return list
    },
  },

  created() {
    this.getMappingList()
    this.getMappingStats()
    this.getMappingTypes()
  },

  methods: {
    // ==================== 数据加载 ====================
    async getMappingList() {
      this.loading = true
      try {
        const response = await systemIntegrationApi.getMappingList()
        if (response.code === 1 && response.data) {
          this.mappingList = response.data
        }
      } catch (error) {
        this.$message.error('获取映射列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async getMappingStats() {
      try {
        const response = await systemIntegrationApi.getMappingStats()
        if (response.code === 1 && response.data) {
          this.mappingStats = response.data
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    async getMappingTypes() {
      try {
        const response = await systemIntegrationApi.getMappingList()
        if (response.code === 1 && response.data) {
          const typeMap = {}
          const iconMap = {
            FIELD: 'el-icon-document',
            TABLE: 'el-icon-s-grid',
            OBJECT: 'el-icon-box',
            CUSTOM: 'el-icon-s-tools',
          }
          response.data.forEach((item) => {
            const t = item.mappingType || 'OTHER'
            if (!typeMap[t]) {
              typeMap[t] = {
                id: t,
                name: this.getMappingTypeText(t),
                description: this.getMappingTypeText(t) + '配置',
                icon: iconMap[t] || 'el-icon-document',
                mappingCount: 0,
              }
            }
            typeMap[t].mappingCount++
          })
          this.mappingTypes = Object.values(typeMap)
        }
      } catch (error) {
        console.error('获取映射类型失败：', error)
      }
    },
    refreshMappingTypes() {
      this.getMappingTypes()
      this.$message.success('映射类型已刷新')
    },
    // 刷新所有数据
    refreshAll() {
      this.getMappingList()
      this.getMappingStats()
      this.getMappingTypes()
    },

    // ==================== 映射类型筛选 ====================
    handleSelectMappingType(mappingType) {
      this.selectedMappingType =
        this.selectedMappingType === mappingType.id ? null : mappingType.id
    },

    // ==================== 工具栏按钮 ====================
    handleCreateMapping() {
      this.dialogTitle = '创建数据映射'
      this.mappingForm = {
        mappingName: '',
        mappingType: '',
        sourceSystem: '',
        targetSystem: '',
        transformType: '',
        description: '',
      }
      this.dialogVisible = true
    },
    handleRefresh() {
      this.refreshAll()
      this.$message.success('数据已刷新')
    },
    handleTestMapping(row) {
      if (row && row.id) {
        // 单个映射测试
        this.$confirm('确认测试映射「' + row.mappingName + '」？', '提示', { type: 'warning' })
          .then(async () => {
            try {
              const response = await systemIntegrationApi.dataMapping.test(row.id)
              if (response.code === 1) {
                this.$message.success('映射测试成功')
                this.refreshAll()
              } else {
                this.$message.error(response.msg || '映射测试失败')
              }
            } catch (error) {
              this.$message.error('映射测试失败：' + error.message)
            }
          })
          .catch(() => {})
      } else {
        // 打开批量测试对话框
        this.testResult = null
        this.selectedTestIds = []
        this.testDialogVisible = true
      }
    },
    async executeBatchTest() {
      if (this.selectedTestIds.length === 0) {
        this.$message.warning('请至少选择一个映射')
        return
      }
      this.testLoading = true
      try {
        const response = await systemIntegrationApi.dataMapping.batchTest(this.selectedTestIds)
        if (response.code === 1) {
          this.testResult = response.data
          this.refreshAll()
        } else {
          this.$message.error(response.msg || '批量测试失败')
        }
      } catch (error) {
        this.$message.error('批量测试失败：' + error.message)
      } finally {
        this.testLoading = false
      }
    },
    handleImportMapping() {
      this.importPreview = null
      this.importFileData = null
      this.importDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.importUpload) {
          this.$refs.importUpload.clearFiles()
        }
      })
    },
    handleImportFileChange(file) {
      const reader = new FileReader()
      reader.onload = (event) => {
        try {
          this.importFileData = JSON.parse(event.target.result)
          this.importPreview = this.importFileData
        } catch (err) {
          this.$message.error('文件解析失败，请确保是有效的 JSON 文件')
          this.importPreview = null
          this.importFileData = null
        }
      }
      reader.readAsText(file.raw)
    },
    async executeImport() {
      if (!this.importFileData) return
      this.importLoading = true
      try {
        const response = await systemIntegrationApi.dataMapping.importMapping(this.importFileData)
        if (response.code === 1) {
          this.$message.success('导入映射成功')
          this.importDialogVisible = false
          this.importPreview = null
          this.importFileData = null
          this.refreshAll()
        } else {
          this.$message.error(response.msg || '导入失败')
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      } finally {
        this.importLoading = false
      }
    },
    async handleSettings() {
      this.settingsDialogVisible = true
      this.settingsLoading = true
      try {
        const response = await systemIntegrationApi.dataMapping.getSettings()
        if (response.code === 1 && response.data) {
          this.settingsForm = { ...this.settingsForm, ...response.data }
        }
      } catch (error) {
        console.error('获取设置失败：', error)
      } finally {
        this.settingsLoading = false
      }
    },
    async saveSettings() {
      this.settingsSaveLoading = true
      try {
        const response = await systemIntegrationApi.dataMapping.saveSettings(this.settingsForm)
        if (response.code === 1) {
          this.$message.success('设置保存成功')
          this.settingsDialogVisible = false
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.settingsSaveLoading = false
      }
    },
    handleExport() {
      if (!this.mappingList || this.mappingList.length === 0) {
        this.$message.info('暂无数据可导出')
        return
      }
      this.exportScope = 'all'
      this.exportFormat = 'json'
      this.exportDialogVisible = true
    },
    executeExport() {
      const data = this.exportScope === 'all' ? this.mappingList : this.filteredMappingList
      if (!data || data.length === 0) {
        this.$message.info('暂无数据可导出')
        return
      }
      const dataStr = JSON.stringify(data, null, 2)
      const blob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = '数据映射_' + new Date().toISOString().slice(0, 10) + '.json'
      a.click()
      URL.revokeObjectURL(url)
      this.exportDialogVisible = false
      this.$message.success('导出成功')
    },
    handleHelp() {
      this.helpDialogVisible = true
    },

    // ==================== 列表操作 ====================
    handleView(row) {
      this.currentMapping = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
    },
    handleRowClick(row) {
      this.currentMapping = row
    },
    handleEdit(row) {
      this.dialogTitle = '编辑数据映射'
      this.mappingForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除映射「' + row.mappingName + '」？此操作不可恢复。', '警告', { type: 'warning' })
        .then(async () => {
          try {
            const response = await systemIntegrationApi.dataMapping.delete(row.id)
            if (response.code === 1) {
              this.$message.success('删除成功')
              this.refreshAll()
            } else {
              this.$message.error(response.msg || '删除失败')
            }
          } catch (error) {
            this.$message.error('删除失败：' + error.message)
          }
        })
        .catch(() => {})
    },
    handleMoreAction(command, row) {
      const actions = {
        copy: () => this.handleCopy(row),
        export: () => this.handleExportSingle(row),
        logs: () => this.handleViewLogs(row),
        preview: () => this.handlePreview(row),
        delete: () => this.handleDelete(row),
      }
      if (actions[command]) actions[command]()
    },
    async handleCopy(row) {
      try {
        const response = await systemIntegrationApi.dataMapping.copy(row.id)
        if (response.code === 1) {
          this.$message.success('复制成功')
          this.refreshAll()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    async handleExportSingle(row) {
      try {
        const response = await systemIntegrationApi.dataMapping.exportMapping(row.id)
        if (response.code === 1 && response.data) {
          const dataStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([dataStr], { type: 'application/json' })
          const url = URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = (row.mappingName || 'mapping') + '.json'
          a.click()
          URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    async handleViewLogs(row) {
      this.currentLogs = []
      this.logsDialogVisible = true
      this.logsLoading = true
      try {
        const response = await systemIntegrationApi.dataMapping.getLogs(row.id)
        if (response.code === 1 && response.data) {
          this.currentLogs = response.data
        }
      } catch (error) {
        this.$message.error('获取日志失败：' + error.message)
      } finally {
        this.logsLoading = false
      }
    },
    handlePreview(row) {
      this.previewData = row
      this.previewDialogVisible = true
    },

    // ==================== 表单提交 ====================
    handleSubmitForm() {
      this.$refs.mappingForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          let response
          if (this.mappingForm.id) {
            response = await systemIntegrationApi.dataMapping.update(this.mappingForm.id, this.mappingForm)
          } else {
            response = await systemIntegrationApi.dataMapping.create(this.mappingForm)
          }
          if (response.code === 1) {
            this.$message.success(this.mappingForm.id ? '更新成功' : '创建成功')
            this.dialogVisible = false
            this.refreshAll()
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          this.$message.error('操作失败：' + error.message)
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDialogClose() {
      this.$refs.mappingForm && this.$refs.mappingForm.resetFields()
    },
    handleTestMappingForm() {
      if (this.mappingForm.id) {
        this.handleTestMapping(this.mappingForm)
      } else {
        this.$message.info('请先保存映射后再测试')
      }
    },

    // ==================== 字段映射方法 ====================
    handleAddFieldMapping() {
      this.$prompt('请输入源字段名称', '添加字段映射', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      })
        .then(({ value }) => {
          if (value) {
            this.fieldMappings.push({
              sourceField: value,
              targetField: '',
              fieldType: 'STRING',
              transformRule: '',
              required: false,
            })
            this.$message.success('字段映射已添加')
          }
        })
        .catch(() => {})
    },
    handleEditFieldMapping(row) {
      this.$prompt('编辑目标字段', '编辑映射: ' + row.sourceField, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.targetField,
      })
        .then(({ value }) => {
          row.targetField = value
          this.$message.success('字段映射已更新')
        })
        .catch(() => {})
    },
    handleDeleteFieldMapping(row) {
      this.$confirm('确认删除字段映射: ' + row.sourceField + '？', '警告', { type: 'warning' })
        .then(() => {
          const idx = this.fieldMappings.indexOf(row)
          if (idx > -1) {
            this.fieldMappings.splice(idx, 1)
          }
          this.$message.success('字段映射已删除')
        })
        .catch(() => {})
    },

    // ==================== 辅助方法 ====================
    getMappingTypeColor(type) {
      const m = { FIELD: 'primary', TABLE: 'success', OBJECT: 'warning', CUSTOM: 'danger' }
      return m[type] || 'info'
    },
    getMappingTypeText(type) {
      const m = { FIELD: '字段映射', TABLE: '表映射', OBJECT: '对象映射', CUSTOM: '自定义映射' }
      return m[type] || type || '未知'
    },
    getStatusColor(status) {
      const m = { ACTIVE: 'success', INACTIVE: 'danger', PENDING: 'warning' }
      return m[status] || 'info'
    },
    getStatusText(status) {
      const m = { ACTIVE: '启用', INACTIVE: '停用', PENDING: '待启用' }
      return m[status] || status || '未知'
    },
    getSystemColor(system) {
      const m = { ERP: 'primary', CRM: 'success', DATABASE: 'warning', API: 'danger', NCV65: '', FILE: 'info' }
      return m[system] || 'info'
    },
    getSystemText(system) {
      const m = { ERP: 'ERP系统', CRM: 'CRM系统', DATABASE: '数据库', API: 'API接口', NCV65: 'NCV65预算系统', FILE: '文件' }
      return m[system] || system || '-'
    },
    getSuccessRateClass(rate) {
      if (rate >= 90) return 'success'
      if (rate >= 70) return 'warning'
      return 'danger'
    },
    getSyncStatusColor(status) {
      const m = { SUCCESS: 'success', PARTIAL: 'warning', FAILURE: 'danger', RUNNING: '' }
      return m[status] || 'info'
    },
    getSyncStatusText(status) {
      const m = { SUCCESS: '成功', PARTIAL: '部分成功', FAILURE: '失败', RUNNING: '运行中' }
      return m[status] || status || '未知'
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      if (isNaN(d.getTime())) return '-'
      const pad = (n) => (n < 10 ? '0' + n : n)
      return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
    },
  },
}
</script>
