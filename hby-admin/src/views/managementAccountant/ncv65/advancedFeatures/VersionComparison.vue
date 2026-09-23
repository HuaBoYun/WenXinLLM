<template>
  <div class="version-comparison">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>版本对比管理</h2>
      <p>预算版本对比分析，支持多版本差异对比、变更追踪和版本回滚</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateComparison">创建对比</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button type="warning" icon="el-icon-s-operation" @click="handleBatchCompare">批量对比</el-button>
            <el-button type="info" icon="el-icon-view" @click="handleViewHistory">对比历史</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleSettings">对比设置</el-button>
            <el-button icon="el-icon-document" @click="handleReports">对比报告</el-button>
            <el-button icon="el-icon-help" @click="handleHelp">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 对比统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.totalComparisons }}</div>
            <div class="stat-label">对比任务</div>
            <div class="stat-description">总对比任务数量</div>
            <div class="stat-trend">
              <i class="el-icon-s-operation"></i>
              <span>版本对比</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card versions-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.totalVersions }}</div>
            <div class="stat-label">版本总数</div>
            <div class="stat-description">可对比版本数量</div>
            <div class="stat-trend">
              <i class="el-icon-files"></i>
              <span>多版本</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-files"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card differences-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.avgDifferences }}</div>
            <div class="stat-label">平均差异</div>
            <div class="stat-description">平均差异项数量</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>差异分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card accuracy-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ comparisonStats.accuracy }}%</div>
            <div class="stat-label">对比准确率</div>
            <div class="stat-description">对比结果准确度</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>高精度</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 对比类型选择 -->
    <el-card class="comparison-types-card" shadow="never">
      <div slot="header" class="card-header">
        <span>对比类型</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshComparisonTypes">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="comparisonType in comparisonTypes" :key="comparisonType.id">
          <el-card
            class="comparison-type-item"
            shadow="hover"
            @click.native="handleSelectComparisonType(comparisonType)"
            :class="{ 'selected': selectedComparisonType === comparisonType.id }"
          >
            <div class="comparison-type-icon">
              <i :class="comparisonType.icon"></i>
            </div>
            <div class="comparison-type-title">{{ comparisonType.name }}</div>
            <div class="comparison-type-description">{{ comparisonType.description }}</div>
            <div class="comparison-type-stats">
              <span class="comparison-count">{{ comparisonType.comparisonCount }} 次对比</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <!-- 版本对比列表 -->
    <el-card class="version-comparisons-card" shadow="never">
      <div slot="header" class="card-header">
        <span>版本对比</span>
        <div class="header-tools">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索对比"
            size="mini"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter.native="getVersionComparisonList"
            clearable
            @clear="getVersionComparisonList"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button icon="el-icon-refresh" size="mini" @click="getVersionComparisonList">刷新</el-button>
        </div>
      </div>

      <el-table
        :data="versionComparisonList"
        border
        stripe
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="comparisonName" label="对比名称" width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.comparisonName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="comparisonType" label="对比类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getComparisonTypeColor(scope.row.comparisonType)" size="mini">
              {{ getComparisonTypeText(scope.row.comparisonType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sourceVersion" label="源版本" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="primary" size="mini">{{ scope.row.sourceVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetVersion" label="目标版本" width="120" align="center">
          <template slot-scope="scope">
            <el-tag type="success" size="mini">{{ scope.row.targetVersion }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="differenceCount" label="差异数量" width="100" align="center">
          <template slot-scope="scope">
            <span class="difference-count" :class="getDifferenceCountClass(scope.row.differenceCount)">
              {{ scope.row.differenceCount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="对比进度" width="150" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="getProgressByStatus(scope.row.comparisonStatus)"
              :color="getProgressColor(getProgressByStatus(scope.row.comparisonStatus))"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ getProgressByStatus(scope.row.comparisonStatus) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="comparisonStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.comparisonStatus)" size="mini">
              {{ getStatusText(scope.row.comparisonStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="260" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              type="text"
              size="mini"
              icon="el-icon-refresh"
              @click.stop="handleRecompare(scope.row)"
              :disabled="scope.row.comparisonStatus === 'RUNNING'"
            >重新对比</el-button>
            <span @click.stop>
              <el-dropdown @command="(command) => handleMoreAction(command, scope.row)">
                <el-button type="text" size="mini">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="export">导出结果</el-dropdown-item>
                  <el-dropdown-item command="merge">合并版本</el-dropdown-item>
                  <el-dropdown-item command="rollback">版本回滚</el-dropdown-item>
                  <el-dropdown-item command="copy">复制对比</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 对比详情抽屉 -->
    <el-drawer
      title="版本对比详情"
      :visible.sync="detailDrawerVisible"
      direction="rtl"
      size="80%"
    >
      <div class="detail-content" v-if="currentComparison">
        <div style="margin-bottom: 16px; text-align: right;">
          <el-button type="primary" size="small" icon="el-icon-edit" @click="handleEdit(currentComparison)">编辑</el-button>
          <el-button size="small" @click="detailDrawerVisible = false">关闭</el-button>
        </div>
        <el-tabs v-model="detailActiveTab" type="card">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions title="对比基本信息" :column="2" border>
              <el-descriptions-item label="对比名称">{{ currentComparison.comparisonName }}</el-descriptions-item>
              <el-descriptions-item label="对比类型">{{ getComparisonTypeText(currentComparison.comparisonType) }}</el-descriptions-item>
              <el-descriptions-item label="源版本">{{ currentComparison.sourceVersion }}</el-descriptions-item>
              <el-descriptions-item label="目标版本">{{ currentComparison.targetVersion }}</el-descriptions-item>
              <el-descriptions-item label="差异数量">{{ currentComparison.differenceCount }}</el-descriptions-item>
              <el-descriptions-item label="对比进度">{{ getProgressByStatus(currentComparison.comparisonStatus) }}%</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusColor(currentComparison.comparisonStatus)" size="mini">
                  {{ getStatusText(currentComparison.comparisonStatus) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建人">{{ currentComparison.createBy }}</el-descriptions-item>
              <el-descriptions-item label="创建时间">{{ currentComparison.createTime }}</el-descriptions-item>
              <el-descriptions-item label="对比描述" :span="2">{{ currentComparison.description }}</el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="差异详情" name="differences">
            <el-table :data="comparisonDifferences" border size="mini" max-height="500">
              <el-table-column prop="fieldName" label="字段名称" width="150" />
              <el-table-column prop="fieldPath" label="字段路径" width="200" />
              <el-table-column prop="sourceValue" label="源版本值" width="150" align="center">
                <template slot-scope="scope">
                  <span class="source-value">{{ formatValue(scope.row.sourceValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="targetValue" label="目标版本值" width="150" align="center">
                <template slot-scope="scope">
                  <span class="target-value">{{ formatValue(scope.row.targetValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="differenceType" label="差异类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getDifferenceTypeColor(scope.row.differenceType)" size="mini">
                    {{ getDifferenceTypeText(scope.row.differenceType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="impact" label="影响程度" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getImpactColor(scope.row.impact)" size="mini">
                    {{ getImpactText(scope.row.impact) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="recommendation" label="建议" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="对比图表" name="charts">
            <div class="chart-container" v-if="comparisonDifferences && comparisonDifferences.length > 0">
              <el-row :gutter="20" style="margin-bottom: 20px;">
                <el-col :span="8">
                  <el-card shadow="hover">
                    <div slot="header"><span>差异类型分布</span></div>
                    <div v-for="type in diffTypeStats" :key="type.name" style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                      <el-tag :type="getDifferenceTypeColor(type.name)" size="small">{{ getDifferenceTypeText(type.name) }}</el-tag>
                      <span style="font-weight: bold;">{{ type.count }} 项</span>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card shadow="hover">
                    <div slot="header"><span>影响程度分布</span></div>
                    <div v-for="imp in impactStats" :key="imp.name" style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                      <el-tag :type="getImpactColor(imp.name)" size="small">{{ getImpactText(imp.name) }}</el-tag>
                      <span style="font-weight: bold;">{{ imp.count }} 项</span>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card shadow="hover">
                    <div slot="header"><span>差异总览</span></div>
                    <div style="text-align: center; padding: 10px 0;">
                      <div style="font-size: 36px; font-weight: bold; color: #409EFF;">{{ comparisonDifferences.length }}</div>
                      <div style="color: #909399; margin-top: 4px;">总差异数</div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
            <el-empty v-else description="暂无差异数据" />
          </el-tab-pane>
          <el-tab-pane label="操作日志" name="logs">
            <el-table :data="comparisonLogs" border size="mini">
              <el-table-column prop="logTime" label="时间" width="150" />
              <el-table-column prop="operation" label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getOperationColor(scope.row.operation)" size="mini">
                    {{ getOperationText(scope.row.operation) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="operator" label="操作人" width="100" />
              <el-table-column prop="description" label="操作描述" />
              <el-table-column prop="result" label="结果" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getResultColor(scope.row.result)" size="mini">
                    {{ getResultText(scope.row.result) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

    <!-- 新增/编辑对比对话框 -->
    <el-dialog
      :title="editMode ? '编辑版本对比' : '创建版本对比'"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="comparisonForm"
        :model="comparisonForm"
        :rules="comparisonRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="对比名称" prop="comparisonName">
              <el-input v-model="comparisonForm.comparisonName" placeholder="请输入对比名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对比类型" prop="comparisonType">
              <el-select v-model="comparisonForm.comparisonType" placeholder="请选择对比类型" style="width: 100%">
                <el-option value="FULL_COMPARISON" label="全量对比" />
                <el-option value="INCREMENTAL_COMPARISON" label="增量对比" />
                <el-option value="FIELD_COMPARISON" label="字段对比" />
                <el-option value="STRUCTURE_COMPARISON" label="结构对比" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源版本" prop="sourceVersion">
              <el-select v-model="comparisonForm.sourceVersion" placeholder="请选择源版本" style="width: 100%">
                <el-option v-for="version in availableVersions" :key="'src_' + version.id" :value="version.version" :label="version.versionName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标版本" prop="targetVersion">
              <el-select v-model="comparisonForm.targetVersion" placeholder="请选择目标版本" style="width: 100%">
                <el-option v-for="version in availableVersions" :key="'tgt_' + version.id" :value="version.version" :label="version.versionName" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="对比范围" prop="comparisonScope">
          <el-checkbox-group v-model="comparisonForm.comparisonScope">
            <el-checkbox label="BUDGET_DATA">预算数据</el-checkbox>
            <el-checkbox label="STRUCTURE">组织结构</el-checkbox>
            <el-checkbox label="FORMULAS">计算公式</el-checkbox>
            <el-checkbox label="PERMISSIONS">权限设置</el-checkbox>
            <el-checkbox label="WORKFLOWS">工作流程</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="对比描述" prop="description">
          <el-input
            v-model="comparisonForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入对比描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitForm" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量对比弹窗 -->
    <el-dialog title="批量版本对比" :visible.sync="batchCompareVisible" width="600px">
      <el-alert title="选择多个版本进行批量对比分析" type="info" :closable="false" style="margin-bottom: 16px;" />
      <el-form label-width="100px" size="small">
        <el-form-item label="对比类型">
          <el-select v-model="batchCompareForm.comparisonType" placeholder="请选择" style="width: 100%">
            <el-option value="FULL_COMPARISON" label="全量对比" />
            <el-option value="INCREMENTAL_COMPARISON" label="增量对比" />
          </el-select>
        </el-form-item>
        <el-form-item label="版本范围">
          <el-select v-model="batchCompareForm.versions" multiple placeholder="请选择版本" style="width: 100%">
            <el-option v-for="v in availableVersions" :key="v.id" :value="v.version" :label="v.versionName" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batchCompareVisible = false">取消</el-button>
        <el-button type="primary" @click="batchCompareVisible = false">开始对比</el-button>
      </div>
    </el-dialog>

    <!-- 对比历史弹窗 -->
    <el-dialog title="对比历史记录" :visible.sync="historyVisible" width="800px">
      <el-table :data="versionComparisonList" border size="mini" max-height="400">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="comparisonName" label="对比名称" show-overflow-tooltip />
        <el-table-column prop="comparisonType" label="类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getComparisonTypeColor(scope.row.comparisonType)" size="mini">{{ getComparisonTypeText(scope.row.comparisonType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comparisonStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.comparisonStatus)" size="mini">{{ getStatusText(scope.row.comparisonStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
      </el-table>
      <div slot="footer">
        <el-button @click="historyVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 对比设置弹窗 -->
    <el-dialog title="对比设置" :visible.sync="settingsVisible" width="600px">
      <el-form label-width="120px" size="small">
        <el-form-item label="默认对比类型">
          <el-select v-model="settingsForm.defaultType" placeholder="请选择" style="width: 100%">
            <el-option value="FULL_COMPARISON" label="全量对比" />
            <el-option value="INCREMENTAL_COMPARISON" label="增量对比" />
            <el-option value="FIELD_COMPARISON" label="字段对比" />
            <el-option value="STRUCTURE_COMPARISON" label="结构对比" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动对比">
          <el-switch v-model="settingsForm.autoCompare" />
        </el-form-item>
        <el-form-item label="差异阈值">
          <el-input-number v-model="settingsForm.threshold" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 对比报告弹窗 -->
    <el-dialog title="对比报告" :visible.sync="reportsVisible" width="700px">
      <el-alert title="版本对比报告汇总" type="success" :closable="false" style="margin-bottom: 16px;" />
      <el-descriptions :column="2" border>
        <el-descriptions-item label="总对比次数">{{ comparisonStats.totalComparisons }}</el-descriptions-item>
        <el-descriptions-item label="可用版本数">{{ comparisonStats.totalVersions }}</el-descriptions-item>
        <el-descriptions-item label="平均差异数">{{ comparisonStats.avgDifferences }}</el-descriptions-item>
        <el-descriptions-item label="完成率">{{ comparisonStats.accuracy }}%</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="reportsVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="版本对比帮助" :visible.sync="helpVisible" width="600px">
      <div style="line-height: 2;">
        <h4>功能说明</h4>
        <p>版本对比管理支持对预算版本进行多维度对比分析，包括：</p>
        <ul>
          <li><b>全量对比</b>：对两个版本的所有数据进行完整对比</li>
          <li><b>增量对比</b>：仅对比两个版本之间的变更部分</li>
          <li><b>字段对比</b>：针对特定字段进行精确对比</li>
          <li><b>结构对比</b>：对比数据结构和组织架构的变化</li>
        </ul>
        <h4>操作流程</h4>
        <p>1. 点击"创建对比"按钮 → 2. 选择源版本和目标版本 → 3. 设置对比范围 → 4. 执行对比 → 5. 查看结果</p>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="helpVisible = false">知道了</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'VersionComparison',
  data() {
    return {
      // 统计数据
      comparisonStats: {
        totalComparisons: 0,
        totalVersions: 0,
        avgDifferences: 0,
        accuracy: 0
      },

      // 对比类型 - id 使用后端实际的 COMPARISON_TYPE 值
      comparisonTypes: [
        { id: 'FULL_COMPARISON', name: '全量对比', description: '完整版本全量对比', icon: 'el-icon-files', comparisonCount: 0 },
        { id: 'INCREMENTAL_COMPARISON', name: '增量对比', description: '版本增量差异对比', icon: 'el-icon-plus', comparisonCount: 0 },
        { id: 'FIELD_COMPARISON', name: '字段对比', description: '特定字段对比', icon: 'el-icon-edit-outline', comparisonCount: 0 },
        { id: 'STRUCTURE_COMPARISON', name: '结构对比', description: '数据结构对比', icon: 'el-icon-s-grid', comparisonCount: 0 }
      ],
      selectedComparisonType: null,

      // 对比列表
      versionComparisonList: [],
      loading: false,
      searchKeyword: '',

      // 详情抽屉
      detailDrawerVisible: false,
      detailActiveTab: 'basic',
      currentComparison: null,
      comparisonDifferences: [],
      comparisonLogs: [],

      // 对话框
      dialogVisible: false,
      submitLoading: false,
      editMode: false,
      editingId: '',

      // 表单数据
      comparisonForm: {
        comparisonName: '',
        comparisonType: '',
        sourceVersion: '',
        targetVersion: '',
        comparisonScope: [],
        description: ''
      },

      // 可用版本
      availableVersions: [],

      // 弹窗可见性
      batchCompareVisible: false,
      historyVisible: false,
      settingsVisible: false,
      reportsVisible: false,
      helpVisible: false,

      // 批量对比表单
      batchCompareForm: {
        comparisonType: '',
        versions: []
      },

      // 设置表单
      settingsForm: {
        defaultType: 'FULL_COMPARISON',
        autoCompare: false,
        threshold: 10
      },

      // 表单验证规则
      comparisonRules: {
        comparisonName: [
          { required: true, message: '请输入对比名称', trigger: 'blur' }
        ],
        comparisonType: [
          { required: true, message: '请选择对比类型', trigger: 'change' }
        ],
        sourceVersion: [
          { required: true, message: '请选择源版本', trigger: 'change' }
        ],
        targetVersion: [
          { required: true, message: '请选择目标版本', trigger: 'change' }
        ],
        comparisonScope: [
          { required: true, message: '请选择对比范围', trigger: 'change' }
        ]
      }
    }
  },

  created() {
    this.getVersionComparisonList()
    this.getComparisonStats()
    this.getAvailableVersions()
  },

  computed: {
    diffTypeStats() {
      if (!this.comparisonDifferences || this.comparisonDifferences.length === 0) return []
      const counts = {}
      this.comparisonDifferences.forEach(d => {
        const t = d.differenceType || 'UNKNOWN'
        counts[t] = (counts[t] || 0) + 1
      })
      return Object.keys(counts).map(name => ({ name, count: counts[name] }))
    },
    impactStats() {
      if (!this.comparisonDifferences || this.comparisonDifferences.length === 0) return []
      const counts = {}
      this.comparisonDifferences.forEach(d => {
        const i = d.impact || 'UNKNOWN'
        counts[i] = (counts[i] || 0) + 1
      })
      return Object.keys(counts).map(name => ({ name, count: counts[name] }))
    },
  },

  methods: {
    // 获取对比列表
    async getVersionComparisonList() {
      this.loading = true
      try {
        const params = { keyword: this.searchKeyword }
        if (this.selectedComparisonType) {
          params.comparisonType = this.selectedComparisonType
        }
        const response = await advancedFeaturesApi.getVersionComparisonList(params)
        if (response && response.code === 1 && response.data) {
          this.versionComparisonList = response.data.list || []
        }
      } catch (error) {
        this.$message.error('获取对比列表失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 获取统计数据
    async getComparisonStats() {
      try {
        const response = await advancedFeaturesApi.getVersionComparisonStats()
        if (response && response.code === 1 && response.data) {
          this.comparisonStats = response.data
          // 同步更新对比类型卡片的计数
          const typeCountMap = {
            'FULL_COMPARISON': response.data.fullComparisonCount || 0,
            'INCREMENTAL_COMPARISON': response.data.incrementalComparisonCount || 0,
            'FIELD_COMPARISON': response.data.fieldComparisonCount || 0,
            'STRUCTURE_COMPARISON': response.data.structureComparisonCount || 0
          }
          this.comparisonTypes.forEach(t => {
            t.comparisonCount = typeCountMap[t.id] || 0
          })
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },

    // 获取可用版本
    async getAvailableVersions() {
      try {
        const response = await advancedFeaturesApi.getAvailableVersions()
        if (response && response.code === 1) {
          this.availableVersions = response.data || []
        }
      } catch (error) {
        console.error('获取可用版本失败：', error)
      }
    },

    // 创建对比
    handleCreateComparison() {
      this.editMode = false
      this.editingId = ''
      this.dialogVisible = true
      this.resetForm()
    },

    // 查看详情
    async handleView(row) {
      this.currentComparison = row
      this.detailDrawerVisible = true
      this.detailActiveTab = 'basic'
      await this.getComparisonDifferences(row.comparisonId)
      await this.getComparisonLogs(row.comparisonId)
    },

    // 编辑对比
    handleEdit(row) {
      this.editMode = true
      this.editingId = row.comparisonId
      this.comparisonForm = {
        comparisonName: row.comparisonName || '',
        comparisonType: row.comparisonType || '',
        sourceVersion: row.sourceVersion || '',
        targetVersion: row.targetVersion || '',
        comparisonScope: row.comparisonScope ? row.comparisonScope.split(',') : [],
        description: row.description || ''
      }
      this.dialogVisible = true
    },

    // 获取对比差异
    async getComparisonDifferences(comparisonId) {
      try {
        const response = await advancedFeaturesApi.getVersionComparisonDifferences(comparisonId)
        this.comparisonDifferences = response.data
      } catch (error) {
        console.error('获取对比差异失败：', error)
      }
    },

    // 获取对比日志
    async getComparisonLogs(comparisonId) {
      try {
        const response = await advancedFeaturesApi.getVersionComparisonLogs(comparisonId)
        this.comparisonLogs = response.data
      } catch (error) {
        console.error('获取对比日志失败：', error)
      }
    },

    // 重新对比
    async handleRecompare(row) {
      this.$confirm('确定重新执行该版本对比吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.recompareVersions(row.comparisonId)
          this.$message.success('重新对比已启动')
          this.getVersionComparisonList()
        } catch (error) {
          this.$message.error('重新对比失败：' + error.message)
        }
      })
    },

    // 更多操作
    handleMoreAction(command, row) {
      switch (command) {
        case 'export':
          this.handleExportComparison(row)
          break
        case 'merge':
          this.handleMergeVersions(row)
          break
        case 'rollback':
          this.handleRollbackVersion(row)
          break
        case 'copy':
          this.handleCopyComparison(row)
          break
        case 'delete':
          this.handleDeleteComparison(row)
          break
      }
    },

    // 导出结果
    async handleExportComparison(row) {
      try {
        const response = await advancedFeaturesApi.exportVersionComparison(row.comparisonId)
        if (response && response.code === 1 && response.data) {
          const dataStr = JSON.stringify(response.data, null, 2)
          const blob = new Blob([dataStr], { type: 'application/json' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '版本对比_' + (row.comparisonName || row.comparisonId) + '.json'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败：' + ((response && response.msg) || '无数据'))
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 合并版本
    async handleMergeVersions(row) {
      this.$confirm('确定合并这两个版本吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.mergeVersions({
            comparisonId: row.comparisonId,
            sourceVersion: row.sourceVersion,
            targetVersion: row.targetVersion,
          })
          this.$message.success('版本合并成功')
          this.getVersionComparisonList()
        } catch (error) {
          this.$message.error('版本合并失败：' + error.message)
        }
      })
    },

    // 版本回滚
    async handleRollbackVersion(row) {
      this.$confirm('确定回滚到源版本吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.rollbackVersion({
            comparisonId: row.comparisonId,
            sourceVersion: row.sourceVersion,
            targetVersion: row.targetVersion,
          })
          this.$message.success('版本回滚成功')
          this.getVersionComparisonList()
        } catch (error) {
          this.$message.error('版本回滚失败：' + error.message)
        }
      })
    },

    // 复制对比
    async handleCopyComparison(row) {
      try {
        await advancedFeaturesApi.copyVersionComparison(row.comparisonId)
        this.$message.success('复制成功')
        this.getVersionComparisonList()
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 删除对比
    handleDeleteComparison(row) {
      this.$confirm('确定删除该版本对比吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await advancedFeaturesApi.deleteVersionComparison(row.comparisonId)
          this.$message.success('删除成功')
          this.getVersionComparisonList()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 提交表单
    async handleSubmitForm() {
      this.$refs.comparisonForm.validate(async (valid) => {
        if (valid) {
          if (this.comparisonForm.sourceVersion === this.comparisonForm.targetVersion) {
            this.$message.warning('源版本和目标版本不能相同')
            return
          }

          this.submitLoading = true
          try {
            const submitData = {
              ...this.comparisonForm,
              comparisonScope: this.comparisonForm.comparisonScope.join(',')
            }
            if (this.editMode && this.editingId) {
              await advancedFeaturesApi.updateVersionComparison(this.editingId, submitData)
              this.$message.success('更新成功')
            } else {
              await advancedFeaturesApi.createVersionComparison(submitData)
              this.$message.success('创建成功')
            }
            this.dialogVisible = false
            this.getVersionComparisonList()
            this.getComparisonStats()
          } catch (error) {
            this.$message.error((this.editMode ? '更新' : '创建') + '失败：' + error.message)
          } finally {
            this.submitLoading = false
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      this.comparisonForm = {
        comparisonName: '',
        comparisonType: '',
        sourceVersion: '',
        targetVersion: '',
        comparisonScope: [],
        description: ''
      }
      this.$nextTick(() => {
        this.$refs.comparisonForm && this.$refs.comparisonForm.clearValidate()
      })
    },

    // 对话框关闭
    handleDialogClose() {
      this.editMode = false
      this.editingId = ''
      this.resetForm()
    },

    // 刷新
    handleRefresh() {
      this.getVersionComparisonList()
      this.getComparisonStats()
    },

    // 批量对比
    handleBatchCompare() {
      this.batchCompareVisible = true
    },

    // 查看历史
    handleViewHistory() {
      this.historyVisible = true
    },

    // 对比设置
    handleSettings() {
      this.settingsVisible = true
    },

    // 对比报告
    handleReports() {
      this.reportsVisible = true
    },

    // 帮助
    handleHelp() {
      this.helpVisible = true
    },

    // 刷新对比类型
    refreshComparisonTypes() {
      this.getVersionComparisonList()
      this.getComparisonStats()
      this.$message.success('已刷新')
    },

    // 选择对比类型 - 传递类型字符串进行筛选
    handleSelectComparisonType(comparisonType) {
      if (this.selectedComparisonType === comparisonType.id) {
        // 再次点击取消筛选
        this.selectedComparisonType = null
      } else {
        this.selectedComparisonType = comparisonType.id
      }
      this.getVersionComparisonList()
    },

    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },

    // 格式化值
    formatValue(value) {
      if (typeof value === 'number') {
        return new Intl.NumberFormat('zh-CN').format(value)
      }
      return value
    },

    // 获取对比类型颜色
    getComparisonTypeColor(type) {
      const colorMap = {
        'FULL_COMPARISON': 'primary',
        'INCREMENTAL_COMPARISON': 'success',
        'FIELD_COMPARISON': 'warning',
        'STRUCTURE_COMPARISON': 'danger'
      }
      return colorMap[type] || 'info'
    },

    // 获取对比类型文本
    getComparisonTypeText(type) {
      const textMap = {
        'FULL_COMPARISON': '全量对比',
        'INCREMENTAL_COMPARISON': '增量对比',
        'FIELD_COMPARISON': '字段对比',
        'STRUCTURE_COMPARISON': '结构对比'
      }
      return textMap[type] || type
    },

    // 获取差异数量样式类
    getDifferenceCountClass(count) {
      if (count > 20) return 'high-difference'
      if (count > 10) return 'medium-difference'
      return 'low-difference'
    },

    // 获取差异类型颜色
    getDifferenceTypeColor(type) {
      const colorMap = {
        'ADDED': 'success',
        'DELETED': 'danger',
        'MODIFIED': 'warning',
        'MOVED': 'primary'
      }
      return colorMap[type] || 'info'
    },

    // 获取差异类型文本
    getDifferenceTypeText(type) {
      const textMap = {
        'ADDED': '新增',
        'DELETED': '删除',
        'MODIFIED': '修改',
        'MOVED': '移动'
      }
      return textMap[type] || type
    },

    // 获取影响程度颜色
    getImpactColor(impact) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return colorMap[impact] || 'info'
    },

    // 获取影响程度文本
    getImpactText(impact) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[impact] || impact
    },

    // 获取操作颜色
    getOperationColor(operation) {
      const colorMap = {
        'CREATE': 'primary',
        'COMPARE': 'success',
        'EXPORT': 'warning',
        'DELETE': 'danger'
      }
      return colorMap[operation] || 'info'
    },

    // 获取操作文本
    getOperationText(operation) {
      const textMap = {
        'CREATE': '创建',
        'COMPARE': '对比',
        'EXPORT': '导出',
        'DELETE': '删除'
      }
      return textMap[operation] || operation
    },

    // 获取结果颜色
    getResultColor(result) {
      const colorMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PARTIAL': 'warning'
      }
      return colorMap[result] || 'info'
    },

    // 获取结果文本
    getResultText(result) {
      const textMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'PARTIAL': '部分成功'
      }
      return textMap[result] || result
    },

    // 获取进度颜色
    getProgressColor(progress) {
      if (progress >= 90) return '#67C23A'
      if (progress >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 根据状态计算进度百分比
    getProgressByStatus(status) {
      const progressMap = {
        'COMPLETED': 100,
        'RUNNING': 50,
        'COMPARING': 50,
        'PENDING': 0,
        'FAILED': 0
      }
      return progressMap[status] || 0
    },

    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'PENDING': 'warning'
      }
      return colorMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'PENDING': '待执行'
      }
      return textMap[status] || status
    }
  }
}
</script>