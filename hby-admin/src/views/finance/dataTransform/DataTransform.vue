<template>
  <div class="data-transform-container">
    <!-- 页面标题 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="title-text">数据转换</span>
      </div>

      <!-- 查询表单 -->
      <el-form :model="queryForm" label-width="100px" class="query-form">
        <el-row :gutter="20">
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="任务名称">
              <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="任务状态">
              <el-select v-model="queryForm.status" placeholder="请选择状态">
                <el-option label="全部" value="" />
                <el-option label="运行中" value="RUNNING" />
                <el-option label="已暂停" value="PAUSED" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已取消" value="CANCELLED" />
                <el-option label="失败" value="FAILED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="24" :sm="24">
            <el-form-item>
              <el-button type="primary" @click="handleQuery">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
              <el-button type="success" @click="handleCreate">新建转换任务</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 任务列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        :loading="loading"
        stripe
        border
        :default-sort="{ prop: 'startTime', order: 'descending' }"
      >
        <el-table-column prop="taskId" label="任务ID" width="120" align="center" />

        <el-table-column prop="taskName" label="任务名称" min-width="180" show-overflow-tooltip />

        <el-table-column prop="collectionTaskId" label="采集任务ID" width="130" align="center" />

        <el-table-column prop="targetTable" label="目标表名" width="140" align="center" show-overflow-tooltip />

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="progress" label="进度" width="140" align="center">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center; gap: 8px;">
              <el-progress
                :percentage="scope.row.progress"
                :color="getProgressColor(scope.row.progress)"
                style="flex: 1;"
              />
              <span style="min-width: 35px; font-weight: 500;">{{ scope.row.progress }}%</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="transformCount" label="转换记录数" width="120" align="right">
          <template slot-scope="scope">
            <span style="font-weight: 500; color: #409EFF;">{{ scope.row.transformCount | formatNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="startTime" label="开始时间" width="160" align="center" sortable />

        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template slot-scope="scope">
            <div style="display: flex; gap: 4px; justify-content: center; flex-wrap: wrap;">
              <el-button
                v-if="scope.row.status === 'RUNNING'"
                size="mini"
                type="warning"
                @click="handlePause(scope.row)"
              >
                暂停
              </el-button>
              <el-button
                v-if="scope.row.status === 'PAUSED'"
                size="mini"
                type="success"
                @click="handleResume(scope.row)"
              >
                恢复
              </el-button>
              <el-button
                v-if="scope.row.status === 'RUNNING' || scope.row.status === 'PAUSED'"
                size="mini"
                type="danger"
                @click="handleCancel(scope.row)"
              >
                取消
              </el-button>
              <el-button
                size="mini"
                type="info"
                @click="handleDetail(scope.row)"
              >
                详情
              </el-button>
              <el-button
                size="mini"
                type="danger"
                plain
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; text-align: right"
      />
    </el-card>

    <!-- 新建转换任务对话框 - 5步向导 -->
    <el-dialog
      title="新建转换任务"
      :visible.sync="createDialogVisible"
      width="1200px"
      :close-on-click-modal="false"
      @close="handleCreateDialogClose"
    >
      <!-- 步骤条 -->
      <el-steps :active="currentStep" finish-status="success" align-center style="margin-bottom: 30px;">
        <el-step title="基本信息" />
        <el-step title="字段映射" />
        <el-step title="算法配置" />
        <el-step title="验证预览" />
        <el-step title="执行转换" />
      </el-steps>

      <!-- 步骤1: 基本信息 -->
      <div v-show="currentStep === 0">
        <el-form :model="createForm" label-width="120px" :rules="step1Rules" ref="step1FormRef">
          <el-form-item label="任务名称" prop="taskName">
            <el-input v-model="createForm.taskName" placeholder="请输入任务名称" style="width: 400px;" />
          </el-form-item>
          <el-form-item label="转换方案" prop="planId">
            <el-select
              v-model="createForm.planId"
              placeholder="请选择转换方案"
              :loading="planLoading"
              filterable
              clearable
              style="width: 400px;"
              @change="handlePlanChange"
            >
              <el-option-group
                v-for="group in fversionList"
                :key="group.fid"
                :label="group.handtext"
              >
                <el-option
                  v-for="item in group.childrenList"
                  :key="item.fid"
                  :label="item.handtext"
                  :value="item.fid"
                />
              </el-option-group>
            </el-select>
          </el-form-item>
          <el-form-item label="采集任务" prop="collectionTaskId">
            <el-select
              v-model="createForm.collectionTaskId"
              placeholder="请选择采集任务"
              :loading="collectionTaskLoading"
              filterable
              clearable
              style="width: 400px;"
              @change="handleCollectionTaskChange"
            >
              <el-option
                v-for="task in collectionTaskList"
                :key="task.taskId"
                :label="`${task.taskName} (${task.taskId})`"
                :value="task.taskId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="createForm.remark" type="textarea" rows="3" placeholder="请输入备注" style="width: 600px;" />
          </el-form-item>
        </el-form>

        <!-- SQL转换配置信息展示区域 -->
        <el-alert
          v-if="sqlConfigList && sqlConfigList.length > 0"
          title="已检测到SQL转换配置"
          type="info"
          :closable="false"
          style="margin-top: 20px;"
        >
          <div slot="title" style="font-size: 14px; font-weight: bold;">
            <i class="el-icon-info"></i>
            已检测到财务版本"{{ financeVersionInfo.fname }}"的SQL转换配置
          </div>
          <div style="margin-top: 10px;">
            <p style="margin-bottom: 10px; color: #606266;">
              <i class="el-icon-warning"></i>
              <strong>转换优先级说明:</strong> 系统将优先使用以下SQL配置进行数据转换,之后再执行字段映射和算法配置
            </p>
            <p style="margin-bottom: 5px; color: #303133; font-weight: 500;">
              已配置的转换内容(共{{ sqlConfigList.length }}项):
            </p>
            <div style="padding: 10px; background-color: #f5f7fa; border-radius: 4px;">
              <el-tag
                v-for="(config, index) in sqlConfigList"
                :key="config.fid"
                type="success"
                size="medium"
                style="margin-right: 8px; margin-bottom: 8px;"
              >
                {{ index + 1 }}. {{ config.fname }}
              </el-tag>
            </div>
          </div>
        </el-alert>
      </div>

      <!-- 步骤2: 字段映射 -->
      <div v-show="currentStep === 1">
        <div style="margin-bottom: 15px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddMapping">添加映射规则</el-button>
          <span style="margin-left: 15px; color: #909399;">已配置 {{ createForm.mappings.length }} 条映射规则</span>
        </div>

        <el-table :data="createForm.mappings" border style="width: 100%;" max-height="450">
          <el-table-column type="index" label="序号" width="60" align="center" />

          <el-table-column label="映射模式" width="120">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.mappingMode"
                placeholder="选择模式"
                size="small"
                @change="handleMappingModeChange(scope.$index)"
              >
                <el-option label="字段映射" value="FIELD" />
                <el-option label="固定值" value="FIXED" />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="源表" width="180">
            <template slot-scope="scope">
              <el-select
                v-if="scope.row.mappingMode === 'FIELD'"
                v-model="scope.row.sourceTable"
                placeholder="选择源表"
                filterable
                size="small"
                @change="handleSourceTableChange(scope.$index)"
              >
                <el-option
                  v-for="table in sourceTableList"
                  :key="table.tableName"
                  :label="table.tableName"
                  :value="table.tableName"
                />
              </el-select>
              <span v-else style="color: #909399;">-</span>
            </template>
          </el-table-column>

          <el-table-column label="源字段/固定值" width="200">
            <template slot-scope="scope">
              <!-- 字段映射模式 -->
              <el-select
                v-if="scope.row.mappingMode === 'FIELD'"
                v-model="scope.row.sourceField"
                placeholder="选择源字段"
                filterable
                size="small"
                :disabled="!scope.row.sourceTable"
                @change="handleSourceFieldChange(scope.$index)"
              >
                <el-option
                  v-for="field in getSourceFieldList(scope.$index)"
                  :key="field.fieldName"
                  :label="`${field.fieldName} (${field.fieldType})`"
                  :value="field.fieldName"
                />
              </el-select>
              <!-- 固定值模式 -->
              <el-input
                v-else
                v-model="scope.row.defaultValue"
                placeholder="输入固定值"
                size="small"
                clearable
              >
                <template slot="prepend">
                  <i class="el-icon-edit" />
                </template>
              </el-input>
            </template>
          </el-table-column>

          <el-table-column label="系统表" width="180">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.targetTable"
                placeholder="选择系统表"
                filterable
                size="small"
                @change="handleTargetTableChange(scope.$index)"
              >
                <el-option
                  v-for="table in systemTableList"
                  :key="table.tableName"
                  :label="`${table.tableName} - ${table.tableComment}`"
                  :value="table.tableName"
                />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="系统字段" width="180">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.targetField"
                placeholder="选择系统字段"
                filterable
                size="small"
                :disabled="!scope.row.targetTable"
                @change="handleTargetFieldChange(scope.$index)"
              >
                <el-option
                  v-for="field in getSystemFieldList(scope.$index)"
                  :key="field.fieldName"
                  :label="`${field.fieldName} (${field.fieldType})`"
                  :value="field.fieldName"
                />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="类型匹配" width="100" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.typeMatchStatus === 'success'" type="success" size="mini">✓ 匹配</el-tag>
              <el-tag v-else-if="scope.row.typeMatchStatus === 'warning'" type="warning" size="mini">⚠ 需转换</el-tag>
              <el-tag v-else-if="scope.row.typeMatchStatus === 'error'" type="danger" size="mini">✗ 不匹配</el-tag>
              <span v-else style="color: #909399;">-</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="80" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="danger" icon="el-icon-delete" size="mini" circle @click="handleDeleteMapping(scope.$index)" />
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 步骤3: 算法配置 -->
      <div v-show="currentStep === 2">
        <el-alert
          title="算法配置"
          type="info"
          description="选择需要执行的转化算法,算法将在字段映射完成后按顺序执行"
          :closable="false"
          style="margin-bottom: 15px;"
        />

        <div style="margin-bottom: 15px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddAlgorithm">添加算法</el-button>
          <span style="margin-left: 15px; color: #909399;">已配置 {{ createForm.algorithms.length }} 个算法</span>
        </div>

        <el-table :data="createForm.algorithms" border style="width: 100%;" max-height="450">
          <el-table-column type="index" label="序号" width="60" align="center" />

          <el-table-column label="算法名称" width="200">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.algorithmCode"
                placeholder="选择算法"
                filterable
                size="small"
                @change="handleAlgorithmChange(scope.$index)"
              >
                <el-option
                  v-for="algo in availableAlgorithms"
                  :key="algo.algorithmCode"
                  :label="algo.algorithmName"
                  :value="algo.algorithmCode"
                />
              </el-select>
            </template>
          </el-table-column>

          <el-table-column label="算法说明" min-width="250">
            <template slot-scope="scope">
              <span style="color: #606266;">{{ getAlgorithmDescription(scope.row.algorithmCode) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="执行顺序" width="120" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-model="scope.row.executeOrder"
                :min="1"
                :max="99"
                size="small"
                controls-position="right"
              />
            </template>
          </el-table-column>

          <el-table-column label="是否启用" width="100" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.isEnabled" :active-value="1" :inactive-value="0" />
            </template>
          </el-table-column>

          <el-table-column label="操作" width="80" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="danger" icon="el-icon-delete" size="mini" circle @click="handleDeleteAlgorithm(scope.$index)" />
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 步骤4: 计算规则 -->
      <div v-show="currentStep === 3">
        <el-alert
          title="计算规则配置"
          type="info"
          description="为每个字段映射配置计算规则,支持直接映射、单字段运算、多字段运算、跨表关联、聚合统计等"
          :closable="false"
          style="margin-bottom: 15px;"
        />

        <el-table :data="createForm.mappings" border style="width: 100%;" max-height="450">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column label="源表.源字段" width="200">
            <template slot-scope="scope">
              <span>{{ scope.row.sourceTable }}.{{ scope.row.sourceField }}</span>
            </template>
          </el-table-column>
          <el-table-column label="系统表.系统字段" width="200">
            <template slot-scope="scope">
              <span>{{ scope.row.targetTable }}.{{ scope.row.targetField }}</span>
            </template>
          </el-table-column>
          <el-table-column label="计算规则" min-width="400">
            <template slot-scope="scope">
              <div style="display: flex; gap: 5px; align-items: flex-start; flex-direction: column;">
                <!-- 规则类型选择 -->
                <el-select
                  v-model="scope.row.ruleType"
                  placeholder="选择计算规则"
                  size="small"
                  style="width: 100%;"
                  @change="handleRuleTypeChange(scope.$index)"
                >
                  <el-option label="直接映射" value="DIRECT" />
                  <el-option label="单字段运算" value="SINGLE_CALC" />
                  <el-option label="多字段运算" value="MULTI_CALC" />
                  <el-option label="聚合统计" value="AGGREGATE" />
                  <el-option label="条件判断" value="CONDITION" />
                  <el-option label="自定义表达式" value="CUSTOM" />
                </el-select>

                <!-- 单字段运算配置 -->
                <div v-if="scope.row.ruleType === 'SINGLE_CALC'" style="width: 100%; display: flex; gap: 5px; align-items: center;">
                  <span style="white-space: nowrap;">{{ scope.row.sourceField }}</span>
                  <el-select v-model="scope.row.operator" placeholder="运算符" size="small" style="width: 80px;">
                    <el-option label="+" value="+" />
                    <el-option label="-" value="-" />
                    <el-option label="*" value="*" />
                    <el-option label="/" value="/" />
                  </el-select>
                  <el-input v-model="scope.row.operand" placeholder="数值" size="small" style="width: 100px;" />
                  <el-button size="mini" type="primary" @click="buildSingleCalcExpression(scope.$index)">生成</el-button>
                </div>

                <!-- 多字段运算配置 -->
                <div v-if="scope.row.ruleType === 'MULTI_CALC'" style="width: 100%;">
                  <div v-for="(field, idx) in scope.row.calcFields" :key="idx" style="display: flex; gap: 5px; margin-bottom: 5px; align-items: center;">
                    <el-select v-model="field.fieldName" placeholder="选择字段" size="small" style="width: 150px;" filterable>
                      <el-option
                        v-for="f in getSourceFieldList(scope.$index)"
                        :key="f.fieldName"
                        :label="f.fieldName"
                        :value="f.fieldName"
                      />
                    </el-select>
                    <el-select v-model="field.operator" placeholder="运算符" size="small" style="width: 70px;" v-if="idx < scope.row.calcFields.length - 1">
                      <el-option label="+" value="+" />
                      <el-option label="-" value="-" />
                      <el-option label="*" value="*" />
                      <el-option label="/" value="/" />
                    </el-select>
                    <el-button size="mini" icon="el-icon-delete" circle @click="removeCalcField(scope.$index, idx)" v-if="scope.row.calcFields.length > 1" />
                  </div>
                  <div style="display: flex; gap: 5px;">
                    <el-button size="mini" icon="el-icon-plus" @click="addCalcField(scope.$index)">添加字段</el-button>
                    <el-button size="mini" type="primary" @click="buildMultiCalcExpression(scope.$index)">生成表达式</el-button>
                  </div>
                </div>

                <!-- 聚合统计配置 -->
                <div v-if="scope.row.ruleType === 'AGGREGATE'" style="width: 100%;">
                  <div style="display: flex; gap: 5px; margin-bottom: 5px; align-items: center;">
                    <el-select v-model="scope.row.aggregateFunc" placeholder="聚合函数" size="small" style="width: 100px;">
                      <el-option label="SUM" value="SUM" />
                      <el-option label="AVG" value="AVG" />
                      <el-option label="MAX" value="MAX" />
                      <el-option label="MIN" value="MIN" />
                      <el-option label="COUNT" value="COUNT" />
                    </el-select>
                    <span>(</span>
                    <el-select v-model="scope.row.aggregateField" placeholder="选择字段" size="small" style="width: 150px;" filterable>
                      <el-option
                        v-for="f in getSourceFieldList(scope.$index)"
                        :key="f.fieldName"
                        :label="f.fieldName"
                        :value="f.fieldName"
                      />
                    </el-select>
                    <span>)</span>
                  </div>
                  <div style="display: flex; gap: 5px; align-items: center;">
                    <span style="white-space: nowrap;">GROUP BY:</span>
                    <el-select v-model="scope.row.groupByFields" placeholder="选择分组字段" size="small" style="width: 100%;" multiple filterable>
                      <el-option
                        v-for="f in getSourceFieldList(scope.$index)"
                        :key="f.fieldName"
                        :label="f.fieldName"
                        :value="f.fieldName"
                      />
                    </el-select>
                  </div>
                  <el-button size="mini" type="primary" @click="buildAggregateExpression(scope.$index)" style="margin-top: 5px;">生成表达式</el-button>
                </div>

                <!-- 条件判断配置 -->
                <div v-if="scope.row.ruleType === 'CONDITION'" style="width: 100%;">
                  <div style="display: flex; gap: 5px; margin-bottom: 5px; align-items: center;">
                    <span>IF (</span>
                    <el-select v-model="scope.row.conditionField" placeholder="字段" size="small" style="width: 120px;" filterable>
                      <el-option
                        v-for="f in getSourceFieldList(scope.$index)"
                        :key="f.fieldName"
                        :label="f.fieldName"
                        :value="f.fieldName"
                      />
                    </el-select>
                    <el-select v-model="scope.row.conditionOperator" placeholder="条件" size="small" style="width: 80px;">
                      <el-option label="=" value="=" />
                      <el-option label=">" value=">" />
                      <el-option label="<" value="<" />
                      <el-option label=">=" value=">=" />
                      <el-option label="<=" value="<=" />
                      <el-option label="!=" value="!=" />
                    </el-select>
                    <el-input v-model="scope.row.conditionValue" placeholder="值" size="small" style="width: 100px;" />
                    <span>)</span>
                  </div>
                  <div style="display: flex; gap: 5px; align-items: center;">
                    <span style="white-space: nowrap;">THEN:</span>
                    <el-input v-model="scope.row.thenValue" placeholder="满足条件的值" size="small" style="width: 150px;" />
                    <span style="white-space: nowrap;">ELSE:</span>
                    <el-input v-model="scope.row.elseValue" placeholder="不满足条件的值" size="small" style="width: 150px;" />
                  </div>
                  <el-button size="mini" type="primary" @click="buildConditionExpression(scope.$index)" style="margin-top: 5px;">生成表达式</el-button>
                </div>

                <!-- 自定义表达式 -->
                <el-input
                  v-if="scope.row.ruleType === 'CUSTOM'"
                  v-model="scope.row.expression"
                  placeholder="请输入自定义表达式,如: field1 + field2 * 100"
                  size="small"
                  style="width: 100%;"
                />

                <!-- 生成的表达式显示 -->
                <div v-if="scope.row.expression && scope.row.ruleType !== 'CUSTOM'" style="width: 100%; padding: 5px; background: #f5f7fa; border-radius: 4px; font-size: 12px; color: #606266;">
                  <strong>表达式:</strong> {{ scope.row.expression }}
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 步骤4: 验证预览 -->
      <div v-show="currentStep === 4">
        <el-alert
          title="配置验证"
          type="success"
          :closable="false"
          style="margin-bottom: 15px;"
        >
          <div slot="title">
            <i class="el-icon-success" style="color: #67C23A;"></i>
            <span style="margin-left: 5px;">配置验证通过</span>
          </div>
        </el-alert>

        <el-descriptions title="任务配置摘要" :column="2" border>
          <el-descriptions-item label="任务名称">{{ createForm.taskName }}</el-descriptions-item>
          <el-descriptions-item label="转换方案">{{ getPlanName(createForm.planId) }}</el-descriptions-item>
          <el-descriptions-item label="采集任务ID">{{ createForm.collectionTaskId }}</el-descriptions-item>
          <el-descriptions-item label="字段映射数量">{{ createForm.mappings.length }} 条</el-descriptions-item>
          <el-descriptions-item label="算法配置数量">{{ createForm.algorithms.length }} 个</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ createForm.remark || '无' }}</el-descriptions-item>
        </el-descriptions>

        <div style="margin-top: 20px;">
          <h4>字段映射详情</h4>
          <el-table :data="createForm.mappings" border style="width: 100%;" max-height="250">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="sourceTable" label="源表" width="150" />
            <el-table-column prop="sourceField" label="源字段" width="150" />
            <el-table-column prop="targetTable" label="系统表" width="150" />
            <el-table-column prop="targetField" label="系统字段" width="150" />
            <el-table-column label="计算规则" min-width="200">
              <template slot-scope="scope">
                <el-tag size="small">{{ getRuleTypeText(scope.row.ruleType) }}</el-tag>
                <span v-if="scope.row.expression" style="margin-left: 5px; color: #606266;">{{ scope.row.expression }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div style="margin-top: 20px;">
          <h4>算法配置详情</h4>
          <el-table :data="createForm.algorithms" border style="width: 100%;" max-height="200">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="algorithmName" label="算法名称" width="200" />
            <el-table-column label="算法说明" min-width="300">
              <template slot-scope="scope">
                <span style="color: #606266;">{{ getAlgorithmDescription(scope.row.algorithmCode) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="executeOrder" label="执行顺序" width="100" align="center" />
            <el-table-column label="状态" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'info'" size="small">
                  {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 步骤5: 执行转换 -->
      <div v-show="currentStep === 5">
        <el-result
          v-if="!transformExecuting && !transformResult"
          icon="info"
          title="准备执行转换"
          subTitle="点击下方"执行转换"按钮开始数据转换"
        />

        <div v-if="transformExecuting" style="text-align: center; padding: 50px 0;">
          <i class="el-icon-loading" style="font-size: 48px; color: #409EFF;"></i>
          <p style="margin-top: 20px; font-size: 16px; color: #606266;">正在执行转换任务,请稍候...</p>
        </div>

        <el-result
          v-if="transformResult && transformResult.success"
          icon="success"
          title="转换成功"
          :subTitle="`成功转换 ${transformResult.successRecords} 条记录`"
        />

        <el-result
          v-if="transformResult && !transformResult.success"
          icon="error"
          title="转换失败"
          :subTitle="transformResult.message"
        />
      </div>

      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button v-if="currentStep > 0 && currentStep < 5" @click="handlePrevStep">上一步</el-button>
        <el-button v-if="currentStep < 4" type="primary" @click="handleNextStep">下一步</el-button>
        <el-button v-if="currentStep === 4" type="primary" @click="handleNextStep">确认配置</el-button>
        <el-button v-if="currentStep === 5 && !transformExecuting && !transformResult" @click="handleFinish">保存并关闭</el-button>
        <el-button v-if="currentStep === 5 && !transformExecuting && !transformResult" type="success" @click="handleExecuteTransform">执行转换</el-button>
        <el-button v-if="currentStep === 5 && transformResult" type="primary" @click="handleFinish">完成</el-button>
      </span>
    </el-dialog>

    <!-- 任务详情对话框 -->
    <el-dialog
      title="转换任务详情"
      :visible.sync="detailDialogVisible"
      width="700px"
    >
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="任务ID">{{ detailData.taskId }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ detailData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="采集任务ID">{{ detailData.collectionTaskId }}</el-descriptions-item>
        <el-descriptions-item label="目标表名">{{ detailData.targetTable }}</el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getStatusType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="进度">
          <el-progress :percentage="detailData.progress" />
        </el-descriptions-item>
        <el-descriptions-item label="转换记录数">{{ detailData.transformCount }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detailData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detailData.endTime || '进行中' }}</el-descriptions-item>
        <el-descriptions-item label="转换规则" :span="2">
          {{ detailData.transformRule }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTransformTaskList,
  startTransform,
  pauseTransform,
  resumeTransform,
  cancelTransform,
  deleteTransformTask,
  getTransformTaskDetail,
  getSourceTables,
  getSourceFields,
  getSystemTables,
  getSystemFields,
  validateTypeConversion,
  createTransformTask,
  executeTransformTask,
  getAvailableAlgorithms,
  saveFieldMappings,
  saveAlgorithmConfigs,
  getFinanceVersionByCollectionTask,
  getTransformConfigByVersion
} from '@/api/finance/dataTransform'
import { getCollectionTaskList } from '@/api/finance/dataCollection'
import { getCwbbxxList } from '@/api/cwsc'

export default {
  name: 'DataTransform',
  filters: {
    formatNumber(value) {
      if (!value) return '0'
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      queryForm: {
        taskName: '',
        status: ''
      },
      tableData: [],
      loading: false,
      pageNumber: 1,
      pageSize: 20,
      total: 0,
      createDialogVisible: false,
      detailDialogVisible: false,
      detailData: null,
      collectionTaskList: [],
      collectionTaskLoading: false,

      // 转换方案相关
      fversionList: [],
      planLoading: false,

      // 5步向导相关
      currentStep: 0,
      sourceTableList: [],
      systemTableList: [],
      sourceFieldsCache: {}, // 缓存源表字段 { tableName: [fields] }
      systemFieldsCache: {}, // 缓存系统表字段 { tableName: [fields] }
      transformExecuting: false,
      transformResult: null,
      availableAlgorithms: [], // 可用算法列表
      createdTaskId: null, // 已创建的任务ID(步骤3保存配置时生成)

      // 财务版本和转换配置信息
      financeVersionInfo: null, // 财务版本信息 { fid, fname, pid }
      sqlConfigList: [], // BD_INIT_SQLCONFIG 配置列表

      createForm: {
        taskName: '',
        planId: '',
        collectionTaskId: '',
        remark: '',
        mappings: [], // 字段映射数组
        algorithms: [], // 算法配置数组
        rules: [] // 计算规则数组
      },

      step1Rules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        planId: [
          { required: true, message: '请选择转换方案', trigger: 'change' }
        ],
        collectionTaskId: [
          { required: true, message: '请选择采集任务', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.handleQuery()
  },
  methods: {
    handleQuery() {
      this.pageNumber = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        taskName: '',
        status: ''
      }
      this.pageNumber = 1
      this.loadData()
    },
    loadData() {
      this.loading = true
      getTransformTaskList(this.pageNumber, this.pageSize).then(res => {
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '加载数据失败')
          // 接口失败时使用 mock 数据
          this.loadMockData()
        }
      }).catch(err => {
        this.$message.warning('接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用 mock 数据
        this.loadMockData()
      }).finally(() => {
        this.loading = false
      })
    },
    loadMockData() {
      // Mock 数据 - 当后端接口报404或异常时使用
      const mockDataList = [
        {
          taskId: 'TRANS001',
          taskName: '财务数据转换任务',
          collectionTaskId: 'TASK001',
          targetTable: 'TBL_FINANCE_DATA',
          status: 'RUNNING',
          progress: 72,
          transformCount: 18500,
          startTime: '2025-01-21 10:30:00',
          endTime: null,
          transformType: 'INSERT',
          transformRule: 'SELECT * FROM SOURCE_TABLE WHERE STATUS = 1',
          validateData: true,
          remark: '示例数据 - 财务共享数据转换'
        },
        {
          taskId: 'TRANS002',
          taskName: '预算数据转换',
          collectionTaskId: 'TASK002',
          targetTable: 'TBL_BUDGET_DATA',
          status: 'COMPLETED',
          progress: 100,
          transformCount: 12000,
          startTime: '2025-01-20 14:00:00',
          endTime: '2025-01-20 16:30:00',
          transformType: 'UPSERT',
          transformRule: 'SELECT * FROM BUDGET_SOURCE WHERE YEAR = 2025',
          validateData: true,
          remark: '示例数据 - 预算管理数据转换'
        },
        {
          taskId: 'TRANS003',
          taskName: '成本数据转换',
          collectionTaskId: 'TASK003',
          targetTable: 'TBL_COST_DATA',
          status: 'PAUSED',
          progress: 55,
          transformCount: 8900,
          startTime: '2025-01-21 09:15:00',
          endTime: null,
          transformType: 'UPDATE',
          transformRule: 'UPDATE TBL_COST_DATA SET AMOUNT = AMOUNT * 1.1',
          validateData: false,
          remark: '示例数据 - 成本管理数据转换'
        },
        {
          taskId: 'TRANS004',
          taskName: '固定资产数据转换',
          collectionTaskId: 'TASK004',
          targetTable: 'TBL_ASSET_DATA',
          status: 'FAILED',
          progress: 35,
          transformCount: 4200,
          startTime: '2025-01-19 11:00:00',
          endTime: '2025-01-19 12:15:00',
          transformType: 'INSERT',
          transformRule: 'SELECT * FROM ASSET_SOURCE WHERE TYPE = "FIXED"',
          validateData: true,
          remark: '示例数据 - 固定资产转换失败'
        },
        {
          taskId: 'TRANS005',
          taskName: '人力资源数据转换',
          collectionTaskId: 'TASK005',
          targetTable: 'TBL_HR_DATA',
          status: 'COMPLETED',
          progress: 100,
          transformCount: 15600,
          startTime: '2025-01-18 08:00:00',
          endTime: '2025-01-18 10:45:00',
          transformType: 'UPSERT',
          transformRule: 'SELECT * FROM HR_SOURCE WHERE DEPT_ID IN (SELECT ID FROM DEPT)',
          validateData: true,
          remark: '示例数据 - 人力资源数据转换'
        },
        {
          taskId: 'TRANS006',
          taskName: '供应链采购数据转换',
          collectionTaskId: 'TASK006',
          targetTable: 'TBL_PURCHASE_DATA',
          status: 'RUNNING',
          progress: 88,
          transformCount: 28500,
          startTime: '2025-01-21 08:30:00',
          endTime: null,
          transformType: 'INSERT',
          transformRule: 'SELECT * FROM PURCHASE_SOURCE WHERE STATUS IN ("APPROVED", "COMPLETED")',
          validateData: true,
          remark: '示例数据 - 供应链采购数据转换'
        },
        {
          taskId: 'TRANS007',
          taskName: '库存数据转换',
          collectionTaskId: 'TASK007',
          targetTable: 'TBL_INVENTORY_DATA',
          status: 'CANCELLED',
          progress: 30,
          transformCount: 5800,
          startTime: '2025-01-17 15:00:00',
          endTime: '2025-01-17 15:30:00',
          transformType: 'UPDATE',
          transformRule: 'UPDATE TBL_INVENTORY_DATA SET QTY = QTY - SOLD_QTY',
          validateData: false,
          remark: '示例数据 - 库存数据转换已取消'
        },
        {
          taskId: 'TRANS008',
          taskName: '应收应付数据转换',
          collectionTaskId: 'TASK008',
          targetTable: 'TBL_AR_AP_DATA',
          status: 'COMPLETED',
          progress: 100,
          transformCount: 22300,
          startTime: '2025-01-16 10:00:00',
          endTime: '2025-01-16 13:20:00',
          transformType: 'UPSERT',
          transformRule: 'SELECT * FROM AR_AP_SOURCE WHERE AMOUNT > 0',
          validateData: true,
          remark: '示例数据 - 应收应付数据转换'
        }
      ]

      // 根据分页参数返回对应的数据
      const startIndex = (this.pageNumber - 1) * this.pageSize
      const endIndex = startIndex + this.pageSize
      this.tableData = mockDataList.slice(startIndex, endIndex)
      this.total = mockDataList.length
    },
    handlePageChange(pageNumber) {
      this.pageNumber = pageNumber
      this.loadData()
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNumber = 1
      this.loadData()
    },
    handleCreate() {
      // 重置表单和步骤
      this.currentStep = 0
      this.createForm = {
        taskName: '',
        planId: '',
        collectionTaskId: '',
        remark: '',
        mappings: [],
        algorithms: [],
        rules: []
      }
      this.sourceTableList = []
      this.systemTableList = []
      this.sourceFieldsCache = {}
      this.systemFieldsCache = {}
      this.transformExecuting = false
      this.transformResult = null
      this.createdTaskId = null // 重置任务ID

      // 加载转换方案列表、采集任务列表、系统表列表和可用算法列表
      this.loadFVersionList()
      this.loadCollectionTaskList()
      this.loadSystemTables()
      this.loadAvailableAlgorithms()
      this.createDialogVisible = true
    },
    // 加载转换方案列表
    loadFVersionList() {
      this.planLoading = true
      getCwbbxxList().then(res => {
        if (res.code === 1 && res.data) {
          this.fversionList = res.data || []
        } else {
          this.$message.error('加载转换方案列表失败')
        }
      }).catch(err => {
        console.error('加载转换方案列表失败', err)
        this.$message.error('加载转换方案列表失败')
      }).finally(() => {
        this.planLoading = false
      })
    },

    // 转换方案改变事件
    handlePlanChange(planId) {
      if (!planId) {
        return
      }
      // 可以在这里根据选择的方案加载相关配置
      console.log('选择的转换方案ID:', planId)
    },

    loadCollectionTaskList() {
      this.collectionTaskLoading = true
      // 获取所有采集任务，然后过滤已完成的任务
      getCollectionTaskList(1, 1000).then(res => {
        if (res.code === 1 && res.data) {
          // 过滤状态为 SUCCESS 的采集任务(后端返回的已完成状态是SUCCESS)
          const completedTasks = (res.data.list || []).filter(task => task.status === 'SUCCESS')
          this.collectionTaskList = completedTasks
          if (completedTasks.length === 0) {
            this.$message.warning('暂无已完成的采集任务，请先完成采集任务')
          }
        } else {
          this.$message.error('加载采集任务列表失败')
          // 接口失败时使用 mock 数据
          this.loadMockCollectionTasks()
        }
      }).catch(err => {
        this.$message.warning('采集任务列表接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用 mock 数据
        this.loadMockCollectionTasks()
      }).finally(() => {
        this.collectionTaskLoading = false
      })
    },
    loadMockCollectionTasks() {
      // Mock 数据 - 当后端接口报404或异常时使用
      const mockCompletedTasks = [
        {
          taskId: 'TASK001',
          taskName: '财务数据采集',
          status: 'COMPLETED',
          progress: 100,
          recordCount: 5000,
          startTime: '2025-01-20 10:00:00',
          endTime: '2025-01-20 12:30:00'
        },
        {
          taskId: 'TASK002',
          taskName: '预算数据采集',
          status: 'COMPLETED',
          progress: 100,
          recordCount: 3000,
          startTime: '2025-01-19 14:00:00',
          endTime: '2025-01-19 16:00:00'
        },
        {
          taskId: 'TASK005',
          taskName: '人力资源数据采集',
          status: 'COMPLETED',
          progress: 100,
          recordCount: 8000,
          startTime: '2025-01-18 08:00:00',
          endTime: '2025-01-18 10:45:00'
        },
        {
          taskId: 'TASK008',
          taskName: '应收应付数据采集',
          status: 'COMPLETED',
          progress: 100,
          recordCount: 12000,
          startTime: '2025-01-16 10:00:00',
          endTime: '2025-01-16 13:20:00'
        }
      ]
      this.collectionTaskList = mockCompletedTasks
    },
    handleCreateDialogClose() {
      if (this.$refs.step1FormRef) {
        this.$refs.step1FormRef.clearValidate()
      }
      // 清空财务版本和SQL配置信息
      this.financeVersionInfo = null
      this.sqlConfigList = []
      // 如果已经创建了任务,关闭对话框时刷新列表
      if (this.createdTaskId) {
        this.loadData()
      }
    },

    // ==================== 5步向导相关方法 ====================

    // 加载系统表列表
    loadSystemTables() {
      getSystemTables().then(res => {
        if (res.code === 1) {
          this.systemTableList = res.data || []
        } else {
          this.$message.error('加载系统表列表失败')
        }
      }).catch(err => {
        console.error('加载系统表列表失败', err)
      })
    },

    // 选择采集任务后加载源表列表
    handleCollectionTaskChange(taskId) {
      if (!taskId) {
        this.sourceTableList = []
        return
      }

      // 加载源表列表
      getSourceTables(taskId).then(res => {
        if (res.code === 1) {
          this.sourceTableList = res.data || []
        } else {
          this.$message.error('加载源表列表失败')
        }
      }).catch(err => {
        console.error('加载源表列表失败', err)
      })

      // 查询采集任务关联的财务版本信息
      this.checkFinanceVersionAndConfig(taskId)
    },

    // 检查财务版本和转换配置
    checkFinanceVersionAndConfig(collectionTaskId) {
      // 清空之前的配置信息
      this.financeVersionInfo = null
      this.sqlConfigList = []

      // 第一步:查询采集任务关联的财务版本FID
      getFinanceVersionByCollectionTask(collectionTaskId).then(res => {
        if (res.code === 1 && res.data) {
          const versionFid = res.data.fid
          const versionName = res.data.fname || '未知版本'

          if (!versionFid) {
            this.$message.warning('该采集任务未关联财务版本信息')
            return
          }

          // 保存财务版本信息
          this.financeVersionInfo = {
            fid: versionFid,
            fname: versionName,
            pid: res.data.pid
          }

          // 第二步:根据财务版本FID查询转换配置
          getTransformConfigByVersion(versionFid).then(configRes => {
            if (configRes.code === 1 && configRes.data) {
              const configs = configRes.data
              if (configs && configs.length > 0) {
                // 保存配置列表,持续展示在页面上
                this.sqlConfigList = configs
                this.$message.success(`已加载财务版本"${versionName}"的${configs.length}条SQL转换配置`)
              } else {
                // 不存在转换配置
                this.$message.warning(`财务版本"${versionName}"还没有配置SQL转换信息`)
              }
            } else {
              this.$message.warning(`财务版本"${versionName}"还没有配置SQL转换信息`)
            }
          }).catch(err => {
            console.error('查询转换配置失败', err)
            this.$message.error('查询转换配置失败')
          })
        } else {
          this.$message.warning('未找到该采集任务关联的财务版本信息')
        }
      }).catch(err => {
        console.error('查询财务版本失败', err)
        this.$message.error('查询财务版本失败')
      })
    },



    // 添加映射规则
    handleAddMapping() {
      this.createForm.mappings.push({
        mappingMode: 'FIELD',  // 默认为字段映射模式
        sourceTable: '',
        sourceField: '',
        sourceFieldType: '',
        targetTable: '',
        targetField: '',
        targetFieldType: '',
        typeMatchStatus: '',
        defaultValue: '',  // 固定值
        ruleType: 'DIRECT',
        expression: '',
        // 单字段运算
        operator: '+',
        operand: '',
        // 多字段运算
        calcFields: [{ fieldName: '', operator: '+' }],
        // 聚合统计
        aggregateFunc: 'SUM',
        aggregateField: '',
        groupByFields: [],
        // 条件判断
        conditionField: '',
        conditionOperator: '=',
        conditionValue: '',
        thenValue: '',
        elseValue: ''
      })
    },

    // 删除映射规则
    handleDeleteMapping(index) {
      this.createForm.mappings.splice(index, 1)
    },

    // 映射模式改变
    handleMappingModeChange(index) {
      const mapping = this.createForm.mappings[index]

      if (mapping.mappingMode === 'FIXED') {
        // 切换到固定值模式,清空字段映射相关数据
        mapping.sourceTable = ''
        mapping.sourceField = ''
        mapping.sourceFieldType = ''
        mapping.typeMatchStatus = 'success'  // 固定值模式默认匹配成功
      } else {
        // 切换到字段映射模式,清空固定值
        mapping.defaultValue = ''
        mapping.typeMatchStatus = ''  // 清空类型匹配状态
      }
    },

    // 源表改变时加载字段列表
    handleSourceTableChange(index) {
      const mapping = this.createForm.mappings[index]
      mapping.sourceField = ''
      mapping.sourceFieldType = ''
      mapping.typeMatchStatus = ''

      if (!mapping.sourceTable) return

      // 检查缓存
      if (this.sourceFieldsCache[mapping.sourceTable]) {
        return
      }

      // 加载字段列表
      getSourceFields(this.createForm.collectionTaskId, mapping.sourceTable).then(res => {
        if (res.code === 1) {
          this.$set(this.sourceFieldsCache, mapping.sourceTable, res.data || [])
        }
      }).catch(err => {
        console.error('加载源表字段失败', err)
      })
    },

    // 源字段改变时记录类型并验证
    handleSourceFieldChange(index) {
      const mapping = this.createForm.mappings[index]
      const fieldList = this.sourceFieldsCache[mapping.sourceTable] || []
      const field = fieldList.find(f => f.fieldName === mapping.sourceField)

      if (field) {
        mapping.sourceFieldType = field.fieldType
        this.validateFieldTypeMatch(index)
      }
    },

    // 系统表改变时加载字段列表
    handleTargetTableChange(index) {
      const mapping = this.createForm.mappings[index]
      mapping.targetField = ''
      mapping.targetFieldType = ''
      mapping.typeMatchStatus = ''

      if (!mapping.targetTable) return

      // 检查缓存
      if (this.systemFieldsCache[mapping.targetTable]) {
        return
      }

      // 加载字段列表
      getSystemFields(mapping.targetTable).then(res => {
        if (res.code === 1) {
          this.$set(this.systemFieldsCache, mapping.targetTable, res.data || [])
        }
      }).catch(err => {
        console.error('加载系统表字段失败', err)
      })
    },

    // 系统字段改变时记录类型并验证
    handleTargetFieldChange(index) {
      const mapping = this.createForm.mappings[index]
      const fieldList = this.systemFieldsCache[mapping.targetTable] || []
      const field = fieldList.find(f => f.fieldName === mapping.targetField)

      if (field) {
        mapping.targetFieldType = field.fieldType

        // 固定值模式直接标记为匹配成功
        if (mapping.mappingMode === 'FIXED') {
          mapping.typeMatchStatus = 'success'
        } else {
          // 字段映射模式需要验证类型匹配
          this.validateFieldTypeMatch(index)
        }
      }
    },

    // 验证字段类型匹配
    validateFieldTypeMatch(index) {
      const mapping = this.createForm.mappings[index]

      if (!mapping.sourceFieldType || !mapping.targetFieldType) {
        mapping.typeMatchStatus = ''
        return
      }

      validateTypeConversion({
        sourceType: mapping.sourceFieldType,
        targetType: mapping.targetFieldType,
        sampleData: []
      }).then(res => {
        if (res.code === 1) {
          const result = res.data
          if (result.canConvert) {
            mapping.typeMatchStatus = result.needsConversion ? 'warning' : 'success'
          } else {
            mapping.typeMatchStatus = 'error'
          }
        }
      }).catch(err => {
        console.error('类型验证失败', err)
        mapping.typeMatchStatus = 'error'
      })
    },

    // 获取源表字段列表
    getSourceFieldList(index) {
      const mapping = this.createForm.mappings[index]
      return this.sourceFieldsCache[mapping.sourceTable] || []
    },

    // 获取系统表字段列表
    getSystemFieldList(index) {
      const mapping = this.createForm.mappings[index]
      return this.systemFieldsCache[mapping.targetTable] || []
    },

    // 计算规则类型改变
    handleRuleTypeChange(index) {
      const mapping = this.createForm.mappings[index]
      if (mapping.ruleType === 'DIRECT') {
        mapping.expression = ''
      } else if (mapping.ruleType === 'MULTI_CALC') {
        // 初始化多字段运算
        if (!mapping.calcFields || mapping.calcFields.length === 0) {
          mapping.calcFields = [{ fieldName: '', operator: '+' }]
        }
      } else if (mapping.ruleType === 'AGGREGATE') {
        // 初始化聚合统计
        if (!mapping.groupByFields) {
          mapping.groupByFields = []
        }
      }
    },

    // 添加计算字段
    addCalcField(index) {
      const mapping = this.createForm.mappings[index]
      if (!mapping.calcFields) {
        mapping.calcFields = []
      }
      mapping.calcFields.push({ fieldName: '', operator: '+' })
    },

    // 删除计算字段
    removeCalcField(mappingIndex, fieldIndex) {
      const mapping = this.createForm.mappings[mappingIndex]
      mapping.calcFields.splice(fieldIndex, 1)
    },

    // 生成单字段运算表达式
    buildSingleCalcExpression(index) {
      const mapping = this.createForm.mappings[index]
      if (!mapping.operator || !mapping.operand) {
        this.$message.warning('请选择运算符并输入数值')
        return
      }
      mapping.expression = `${mapping.sourceField} ${mapping.operator} ${mapping.operand}`
      this.$message.success('表达式生成成功')
    },

    // 生成多字段运算表达式
    buildMultiCalcExpression(index) {
      const mapping = this.createForm.mappings[index]
      if (!mapping.calcFields || mapping.calcFields.length === 0) {
        this.$message.warning('请至少添加一个字段')
        return
      }

      const invalidField = mapping.calcFields.find(f => !f.fieldName)
      if (invalidField) {
        this.$message.warning('请选择所有字段')
        return
      }

      let expression = ''
      mapping.calcFields.forEach((field, idx) => {
        expression += field.fieldName
        if (idx < mapping.calcFields.length - 1) {
          expression += ` ${field.operator} `
        }
      })

      mapping.expression = expression
      this.$message.success('表达式生成成功')
    },

    // 生成聚合统计表达式
    buildAggregateExpression(index) {
      const mapping = this.createForm.mappings[index]
      if (!mapping.aggregateFunc || !mapping.aggregateField) {
        this.$message.warning('请选择聚合函数和字段')
        return
      }

      let expression = `${mapping.aggregateFunc}(${mapping.aggregateField})`

      if (mapping.groupByFields && mapping.groupByFields.length > 0) {
        expression += ` GROUP BY ${mapping.groupByFields.join(', ')}`
      }

      mapping.expression = expression
      this.$message.success('表达式生成成功')
    },

    // 生成条件判断表达式
    buildConditionExpression(index) {
      const mapping = this.createForm.mappings[index]
      if (!mapping.conditionField || !mapping.conditionOperator || !mapping.conditionValue) {
        this.$message.warning('请完善条件配置')
        return
      }
      if (!mapping.thenValue || !mapping.elseValue) {
        this.$message.warning('请输入THEN和ELSE的值')
        return
      }

      mapping.expression = `IF(${mapping.conditionField} ${mapping.conditionOperator} ${mapping.conditionValue}, ${mapping.thenValue}, ${mapping.elseValue})`
      this.$message.success('表达式生成成功')
    },

    // 获取计算规则数量
    getRuleCount() {
      return this.createForm.mappings.filter(m => m.ruleType && m.ruleType !== 'DIRECT').length
    },

    // 获取规则类型文本
    getRuleTypeText(ruleType) {
      const typeMap = {
        'DIRECT': '直接映射',
        'SINGLE_CALC': '单字段运算',
        'MULTI_CALC': '多字段运算',
        'CROSS_TABLE': '跨表关联',
        'AGGREGATE': '聚合统计',
        'CONDITION': '条件判断',
        'CUSTOM': '自定义表达式'
      }
      return typeMap[ruleType] || ruleType
    },

    // 上一步
    handlePrevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },

    // 下一步
    handleNextStep() {
      // 步骤1验证
      if (this.currentStep === 0) {
        this.$refs.step1FormRef.validate(valid => {
          if (valid) {
            if (!this.createForm.collectionTaskId) {
              this.$message.warning('请选择采集任务')
              return
            }
            this.currentStep++
          }
        })
        return
      }

      // 步骤2验证
      if (this.currentStep === 1) {
        // 如果存在SQL转换配置,允许不填写字段映射
        if (this.sqlConfigList && this.sqlConfigList.length > 0) {
          if (this.createForm.mappings.length === 0) {
            this.$confirm(
              '检测到已有SQL转换配置,可以不配置字段映射。是否继续下一步?',
              '提示',
              {
                confirmButtonText: '继续',
                cancelButtonText: '配置映射',
                type: 'info'
              }
            ).then(() => {
              this.currentStep++
            }).catch(() => {
              // 用户选择配置映射,停留在当前步骤
            })
            return
          }
        } else {
          // 没有SQL转换配置,必须填写字段映射
          if (this.createForm.mappings.length === 0) {
            this.$message.warning('请至少添加一条字段映射规则')
            return
          }
        }

        // 如果有字段映射,验证所有映射是否完整
        if (this.createForm.mappings.length > 0) {
          const invalidMapping = this.createForm.mappings.find(m => {
            // 必须有目标表和目标字段
            if (!m.targetTable || !m.targetField) {
              return true
            }

            // 字段映射模式:必须有源表和源字段
            if (m.mappingMode === 'FIELD') {
              if (!m.sourceTable || !m.sourceField) {
                return true
              }
            }

            // 固定值模式:必须有固定值
            if (m.mappingMode === 'FIXED') {
              if (!m.defaultValue || m.defaultValue.trim() === '') {
                return true
              }
            }

            return false
          })

          if (invalidMapping) {
            this.$message.warning('请完善所有字段映射配置')
            return
          }

          // 检查类型匹配状态(仅检查字段映射模式)
          const errorMapping = this.createForm.mappings.find(m =>
            m.mappingMode === 'FIELD' && m.typeMatchStatus === 'error'
          )
          if (errorMapping) {
            this.$message.error('存在类型不匹配的字段映射,请修正后继续')
            return
          }
        }

        this.currentStep++
        return
      }

      // 步骤3验证(算法配置)
      if (this.currentStep === 2) {
        // 如果有字段映射,验证规则配置
        if (this.createForm.mappings.length > 0) {
          // 验证所有映射都配置了规则类型
          const noRuleMapping = this.createForm.mappings.find(m => !m.ruleType)
          if (noRuleMapping) {
            this.$message.warning('请为所有字段映射配置计算规则')
            return
          }

          // 验证非直接映射的规则是否有表达式
          const noExpressionMapping = this.createForm.mappings.find(m =>
            m.ruleType !== 'DIRECT' && !m.expression
          )
          if (noExpressionMapping) {
            this.$message.warning('请为非直接映射的规则配置计算表达式')
            return
          }
        }

        this.currentStep++
        return
      }

      // 步骤3到步骤4: 验证预览 -> 执行转换
      // 在这一步需要先保存配置(创建任务、保存字段映射、保存算法配置)
      if (this.currentStep === 3) {
        this.handleSaveConfiguration()
        return
      }

      // 步骤4到步骤5
      if (this.currentStep === 4) {
        this.currentStep++
        return
      }
    },

    // 保存配置(创建任务+保存字段映射+保存算法配置)
    handleSaveConfiguration() {
      // 显示加载提示
      const loading = this.$loading({
        lock: true,
        text: '正在保存配置...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      // 构建任务数据
      const taskData = {
        taskName: this.createForm.taskName,
        collectionTaskId: this.createForm.collectionTaskId,
        remark: this.createForm.remark
      }

      let createdTaskId = null

      // 1. 创建转换任务
      createTransformTask(taskData).then(res => {
        if (res.code === 1) {
          createdTaskId = res.data.taskId
          this.$message.success('转换任务创建成功')

          // 2. 保存字段映射配置(如果有的话)
          if (this.createForm.mappings.length > 0) {
            const mappingData = this.createForm.mappings.map(m => ({
              transformTaskId: createdTaskId,
              sourceTable: m.sourceTable || '',  // 固定值模式可能为空
              sourceField: m.sourceField || '',  // 固定值模式可能为空
              targetTable: m.targetTable,
              targetField: m.targetField,
              defaultValue: m.defaultValue || '',  // ✅ 新增:固定值
              transformRule: m.ruleType || 'DIRECT',
              ruleExpression: m.expression || '',
              isEnabled: 1
            }))
            return saveFieldMappings(mappingData, createdTaskId)
          } else {
            // 没有字段映射,直接返回成功
            return Promise.resolve({ code: 1, msg: '无字段映射配置' })
          }
        } else {
          throw new Error(res.msg || '创建任务失败')
        }
      }).then(res => {
        if (res.code === 1) {
          if (this.createForm.mappings.length > 0) {
            this.$message.success('字段映射配置保存成功')
          }

          // 3. 保存算法配置(如果有的话)
          if (this.createForm.algorithms.length > 0) {
            const algorithmData = this.createForm.algorithms.map(a => ({
              transformTaskId: createdTaskId,
              algorithmCode: a.algorithmCode,
              algorithmName: a.algorithmName,
              executeOrder: a.executeOrder,
              isEnabled: a.isEnabled,
              algorithmParams: a.algorithmParams || '{}'
            }))
            return saveAlgorithmConfigs(algorithmData, createdTaskId)
          } else {
            // 没有算法配置,直接返回成功
            return Promise.resolve({ code: 1, msg: '无算法配置' })
          }
        } else {
          throw new Error(res.msg || '保存字段映射失败')
        }
      }).then(res => {
        if (res.code === 1) {
          if (this.createForm.algorithms.length > 0) {
            this.$message.success('算法配置保存成功')
          }

          // 保存创建的任务ID,供后续执行使用
          this.createdTaskId = createdTaskId

          // 关闭加载提示
          loading.close()

          // 提示用户配置保存情况
          if (this.sqlConfigList && this.sqlConfigList.length > 0) {
            this.$message.success(`任务配置保存成功,将使用${this.sqlConfigList.length}条SQL配置进行转换`)
          } else {
            this.$message.success('任务配置保存成功')
          }

          // 进入下一步(执行转换)
          this.currentStep++
        } else {
          throw new Error(res.msg || '保存算法配置失败')
        }
      }).catch(error => {
        loading.close()
        this.$message.error('保存配置失败: ' + error.message)
        console.error('保存配置失败:', error)
      })
    },

    // 执行转换
    handleExecuteTransform() {
      // 检查是否已经创建了任务
      if (!this.createdTaskId) {
        this.$message.error('未找到转换任务ID,请重新配置')
        return
      }

      this.transformExecuting = true
      this.transformResult = null

      // 直接执行转换(任务和配置已在步骤3保存)
      executeTransformTask(this.createdTaskId).then(res => {
        if (res.code === 1) {
          this.transformResult = {
            success: true,
            successRecords: res.data.successCount || 0,
            message: '转换成功'
          }
          this.$message.success('转换任务执行成功')
        } else {
          throw new Error(res.msg || '执行失败')
        }
      }).catch(err => {
        this.transformResult = {
          success: false,
          message: err.message || '转换失败'
        }
        this.$message.error(err.message || '转换失败')
      }).finally(() => {
        this.transformExecuting = false
      })
    },

    // 完成
    handleFinish() {
      this.createDialogVisible = false
      this.loadData()
      this.$message.success('转换任务已完成')
    },

    handlePause(row) {
      this.$confirm('确定要暂停该转换任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pauseTransform(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('暂停成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '暂停失败')
          }
        }).catch(err => {
          this.$message.error('暂停失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleResume(row) {
      this.$confirm('确定要恢复该转换任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resumeTransform(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('恢复成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '恢复失败')
          }
        }).catch(err => {
          this.$message.error('恢复失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleCancel(row) {
      this.$confirm('确定要取消该转换任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelTransform(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('取消成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '取消失败')
          }
        }).catch(err => {
          this.$message.error('取消失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleDetail(row) {
      getTransformTaskDetail(row.taskId).then(res => {
        if (res.code === 1) {
          this.detailData = res.data
          this.detailDialogVisible = true
        } else {
          this.$message.warning('详情接口暂不可用，已加载示例数据')
          // 接口失败时使用当前行数据作为详情
          this.detailData = row
          this.detailDialogVisible = true
        }
      }).catch(err => {
        this.$message.warning('详情接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用当前行数据作为详情
        this.detailData = row
        this.detailDialogVisible = true
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该转换任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTransformTask(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        }).catch(err => {
          this.$message.error('删除失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      const statusMap = {
        'RUNNING': 'success',
        'PAUSED': 'warning',
        'COMPLETED': 'info',
        'CANCELLED': 'danger',
        'FAILED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'RUNNING': '运行中',
        'PAUSED': '已暂停',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'FAILED': '失败'
      }
      return statusMap[status] || status
    },
    getProgressColor(percentage) {
      if (percentage >= 100) {
        return '#67C23A'
      } else if (percentage >= 75) {
        return '#409EFF'
      } else if (percentage >= 50) {
        return '#E6A23C'
      } else if (percentage >= 25) {
        return '#F56C6C'
      } else {
        return '#F56C6C'
      }
    },

    // ========== 算法配置相关方法 ==========
    // 添加算法
    handleAddAlgorithm() {
      this.createForm.algorithms.push({
        algorithmCode: '',
        algorithmName: '',
        executeOrder: this.createForm.algorithms.length + 1,
        isEnabled: 1,
        algorithmParams: '{}'
      })
    },

    // 删除算法
    handleDeleteAlgorithm(index) {
      this.createForm.algorithms.splice(index, 1)
      // 重新排序
      this.createForm.algorithms.forEach((algo, idx) => {
        algo.executeOrder = idx + 1
      })
    },

    // 算法改变时更新算法名称
    handleAlgorithmChange(index) {
      const algorithm = this.createForm.algorithms[index]
      const selectedAlgo = this.availableAlgorithms.find(a => a.algorithmCode === algorithm.algorithmCode)
      if (selectedAlgo) {
        algorithm.algorithmName = selectedAlgo.algorithmName
      }
    },

    // 获取算法说明
    getAlgorithmDescription(algorithmCode) {
      const descriptions = {
        'CALC_BALANCE': '根据借贷方向计算科目余额(借方余额=借方发生额-贷方发生额)',
        'CALC_BOTTOM': '判断是否为底层科目(无下级科目)',
        'CALC_PARENT': '计算上级科目编码(从右向左截取)',
        'CALC_SUMMARY': '逐级汇总科目余额(从底层向上汇总)',
        'CALC_AUX_BALANCE': '计算辅助账余额(按辅助核算项分组汇总)'
      }
      return descriptions[algorithmCode] || '请选择算法'
    },

    // 加载可用算法列表
    loadAvailableAlgorithms() {
      getAvailableAlgorithms().then(res => {
        if (res.code === 1) {
          this.availableAlgorithms = res.data || []
        }
      }).catch(err => {
        console.error('加载算法列表失败', err)
        // 使用默认算法列表
        this.availableAlgorithms = [
          { algorithmCode: 'CALC_BALANCE', algorithmName: '科目余额计算' },
          { algorithmCode: 'CALC_BOTTOM', algorithmName: '底层科目计算' },
          { algorithmCode: 'CALC_PARENT', algorithmName: '上级科目编码计算' },
          { algorithmCode: 'CALC_SUMMARY', algorithmName: '逐级汇总' },
          { algorithmCode: 'CALC_AUX_BALANCE', algorithmName: '辅助账余额计算' }
        ]
      })
    },

    // 获取转换方案名称
    getPlanName(planId) {
      if (!planId) return '未选择'

      for (const group of this.fversionList) {
        if (group.childrenList) {
          const plan = group.childrenList.find(item => item.fid === planId)
          if (plan) {
            return plan.handtext
          }
        }
      }
      return planId
    }
  }
}
</script>

<style scoped>
.data-transform-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.box-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.title-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  letter-spacing: 0.5px;
}

.query-form {
  margin-bottom: 20px;
  padding: 10px 0;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}

/* 表格样式优化 */
/deep/ .el-table {
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
}

/deep/ .el-table__header {
  background-color: #f5f7fa;
}

/deep/ .el-table__header th {
  background-color: #f5f7fa;
  color: #303133;
  font-weight: 600;
  border-bottom: 2px solid #dfe6e9;
}

/deep/ .el-table__body tr:hover > td {
  background-color: #f0f9ff !important;
}

/deep/ .el-table__body td {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

/deep/ .el-table__row {
  height: 50px;
}

/deep/ .el-progress {
  width: 100%;
}

/deep/ .el-button--mini {
  padding: 5px 10px;
  font-size: 12px;
}

/deep/ .el-tag {
  padding: 4px 12px;
  border-radius: 3px;
  font-weight: 500;
}

/* 分页样式 */
/deep/ .el-pagination {
  text-align: right;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}
</style>

