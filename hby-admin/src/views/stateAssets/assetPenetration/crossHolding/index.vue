<template>
  <div class="cross-holding-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">交叉持股总数</div>
          <div class="stat-value">{{ statistics.total || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">循环持股</div>
          <div class="stat-value circular">{{ statistics.circularCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">高风险</div>
          <div class="stat-value high-risk">{{ statistics.highRiskCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">资本虚增总额(万元)</div>
          <div class="stat-value inflation">{{ statistics.totalInflation || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛选区域 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="queryParams" inline size="small">
        <el-form-item label="企业A">
          <el-input v-model="queryParams.companyAName" placeholder="请输入企业A名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="企业B">
          <el-input v-model="queryParams.companyBName" placeholder="请输入企业B名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="交叉类型">
          <el-select v-model="queryParams.crossType" placeholder="请选择" clearable>
            <el-option label="直接交叉" value="DIRECT" />
            <el-option label="间接交叉" value="INDIRECT" />
            <el-option label="循环持股" value="CIRCULAR" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryParams.riskLevel" placeholder="请选择" clearable>
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 工具栏 -->
    <el-card shadow="never" class="table-card">
      <div slot="header" class="toolbar">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增分析</el-button>
        <el-button type="warning" size="small" icon="el-icon-discover" @click="handleBatchDetect">批量检测</el-button>
        <el-button type="success" size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="companyAName" label="企业A" min-width="140" show-overflow-tooltip />
        <el-table-column prop="companyBName" label="企业B" min-width="140" show-overflow-tooltip />
        <el-table-column prop="aHoldBRatio" label="A持B比例(%)" width="120" align="center" />
        <el-table-column prop="bHoldARatio" label="B持A比例(%)" width="120" align="center" />
        <el-table-column prop="crossType" label="交叉类型" width="110" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="crossTypeTagType(row.crossType)" size="small">{{ crossTypeMap[row.crossType] || row.crossType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="riskLevelTagType(row.riskLevel)" size="small">{{ riskLevelMap[row.riskLevel] || row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="capitalInflation" label="资本虚增(万元)" width="130" align="right" />
        <el-table-column prop="isCircular" label="是否循环" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.isCircular === 'Y' ? 'danger' : 'info'" size="small">{{ isCircularMap[row.isCircular] || row.isCircular }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="chainLength" label="链条长度" width="90" align="center" />
        <el-table-column prop="discoveryDate" label="发现日期" width="110" align="center" />
        <el-table-column label="操作" width="260" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click="handleView(row)">查看</el-button>
            <el-button type="text" size="small" @click="handleDetect(row)">检测</el-button>
            <el-button type="text" size="small" @click="handleNetwork(row)">网络图</el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleMoreCommand(cmd, row)">
              <el-button type="text" size="small">更多<i class="el-icon-arrow-down el-icon--right" /></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="trace">追踪流向</el-dropdown-item>
                <el-dropdown-item command="analysis">深度分析</el-dropdown-item>
                <el-dropdown-item command="simulate">模拟变化</el-dropdown-item>
                <el-dropdown-item command="optimize">优化建议</el-dropdown-item>
                <el-dropdown-item command="report">生成报告</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        style="margin-top: 15px; text-align: right;"
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <input type="hidden" v-model="form.holdingId" />
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业A名称" prop="companyAName">
              <el-input v-model="form.companyAName" placeholder="请输入企业A名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业B名称" prop="companyBName">
              <el-input v-model="form.companyBName" placeholder="请输入企业B名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业A编号" prop="companyAId">
              <el-input v-model="form.companyAId" placeholder="请输入企业A编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业B编号" prop="companyBId">
              <el-input v-model="form.companyBId" placeholder="请输入企业B编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="A持B比例(%)" prop="aHoldBRatio">
              <el-input-number v-model="form.aHoldBRatio" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="B持A比例(%)" prop="bHoldARatio">
              <el-input-number v-model="form.bHoldARatio" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交叉类型" prop="crossType">
              <el-select v-model="form.crossType" placeholder="请选择" style="width:100%">
                <el-option label="直接交叉" value="DIRECT" />
                <el-option label="间接交叉" value="INDIRECT" />
                <el-option label="循环持股" value="CIRCULAR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高风险" value="HIGH" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="低风险" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资本虚增(万元)" prop="capitalInflation">
              <el-input-number v-model="form.capitalInflation" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否循环" prop="isCircular">
              <el-select v-model="form.isCircular" placeholder="请选择" style="width:100%">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="链条长度" prop="chainLength">
              <el-input-number v-model="form.chainLength" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现日期" prop="discoveryDate">
              <el-date-picker v-model="form.discoveryDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog title="交叉持股详情" :visible.sync="detailVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业A名称">{{ detailData.companyAName }}</el-descriptions-item>
        <el-descriptions-item label="企业B名称">{{ detailData.companyBName }}</el-descriptions-item>
        <el-descriptions-item label="企业A编号">{{ detailData.companyAId }}</el-descriptions-item>
        <el-descriptions-item label="企业B编号">{{ detailData.companyBId }}</el-descriptions-item>
        <el-descriptions-item label="A持B比例(%)">{{ detailData.aHoldBRatio }}</el-descriptions-item>
        <el-descriptions-item label="B持A比例(%)">{{ detailData.bHoldARatio }}</el-descriptions-item>
        <el-descriptions-item label="交叉类型">{{ crossTypeMap[detailData.crossType] || detailData.crossType }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ riskLevelMap[detailData.riskLevel] || detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="资本虚增(万元)">{{ detailData.capitalInflation }}</el-descriptions-item>
        <el-descriptions-item label="是否循环">{{ isCircularMap[detailData.isCircular] || detailData.isCircular }}</el-descriptions-item>
        <el-descriptions-item label="链条长度">{{ detailData.chainLength }}</el-descriptions-item>
        <el-descriptions-item label="发现日期">{{ detailData.discoveryDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 检测结果弹窗 -->
    <el-dialog title="交叉持股检测结果" :visible.sync="detectVisible" width="600px" append-to-body>
      <div class="detect-result">
        <p v-if="detectResult">{{ detectResult }}</p>
        <p v-else>暂无检测结果，请稍后重试。</p>
      </div>
    </el-dialog>

    <!-- 网络图弹窗 -->
    <el-dialog title="持股网络图" :visible.sync="networkVisible" width="700px" append-to-body>
      <div class="network-content">
        <p v-if="networkData">{{ networkData }}</p>
        <p v-else>暂无网络图数据。当前交叉持股关系：{{ currentRow.companyAName }} ↔ {{ currentRow.companyBName }}，A持B比例{{ currentRow.aHoldBRatio }}%，B持A比例{{ currentRow.bHoldARatio }}%。</p>
      </div>
    </el-dialog>

    <!-- 追踪流向弹窗 -->
    <el-dialog title="资金流向追踪" :visible.sync="traceVisible" width="750px" append-to-body>
      <div v-loading="traceLoading" class="trace-content">
        <div v-if="traceResult.nodes && traceResult.nodes.length">
          <el-alert v-if="traceResult.isCircular" title="⚠️ 检测到循环持股链路" type="warning" :closable="false" show-icon style="margin-bottom:16px" />
          <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
            <el-descriptions-item label="链条长度">{{ traceResult.chainLength }}</el-descriptions-item>
            <el-descriptions-item label="是否循环">{{ traceResult.isCircular ? '是' : '否' }}</el-descriptions-item>
          </el-descriptions>
          <div class="section-title">持股网络节点</div>
          <el-table :data="traceResult.nodes" border size="small" style="margin-bottom:16px">
            <el-table-column prop="name" label="企业名称" />
            <el-table-column prop="type" label="类型" width="100" align="center">
              <template slot-scope="{ row }">
                <el-tag size="mini">{{ row.type === 'company' ? '企业' : row.type }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div class="section-title">持股关系链路</div>
          <el-table :data="traceResult.links" border size="small" style="margin-bottom:16px">
            <el-table-column prop="source" label="持股方" min-width="120">
              <template slot-scope="{ row }">{{ getNodeName(row.source) }}</template>
            </el-table-column>
            <el-table-column label="→" width="50" align="center">
              <template><i class="el-icon-right" /></template>
            </el-table-column>
            <el-table-column prop="target" label="被持股方" min-width="120">
              <template slot-scope="{ row }">{{ getNodeName(row.target) }}</template>
            </el-table-column>
            <el-table-column prop="label" label="持股比例" width="100" align="center" />
          </el-table>
          <div v-if="traceResult.paths && traceResult.paths.length" class="section-title">循环路径</div>
          <el-tag v-for="(path, idx) in traceResult.paths" :key="idx" type="danger" style="margin:4px">{{ path }}</el-tag>
        </div>
        <el-empty v-else description="暂无流向数据" />
      </div>
    </el-dialog>

    <!-- 深度分析弹窗 -->
    <el-dialog title="深度风险分析" :visible.sync="analysisVisible" width="750px" append-to-body>
      <div v-loading="analysisLoading" class="analysis-content">
        <div v-if="analysisResult.overallScore !== undefined">
          <el-row :gutter="20" style="margin-bottom:20px">
            <el-col :span="8">
              <div class="risk-score-card" :class="'risk-' + (analysisResult.overallRisk || '').toLowerCase()">
                <div class="risk-score-value">{{ analysisResult.overallScore }}</div>
                <div class="risk-score-label">综合风险评分</div>
                <el-tag :type="riskLevelTagType(analysisResult.overallRisk)" size="small">{{ riskLevelMap[analysisResult.overallRisk] || analysisResult.overallRisk }}</el-tag>
              </div>
            </el-col>
            <el-col :span="16">
              <div class="section-title">各维度风险评估</div>
              <div v-for="dim in analysisResult.dimensions" :key="dim.name" class="dimension-item">
                <div class="dim-header">
                  <span class="dim-name">{{ dim.name }}</span>
                  <span class="dim-score">{{ dim.score }}分</span>
                </div>
                <el-progress :percentage="dim.score" :color="getProgressColor(dim.score)" :show-text="false" :stroke-width="10" />
                <div class="dim-desc">{{ dim.description }}</div>
              </div>
            </el-col>
          </el-row>
          <div v-if="analysisResult.historicalTrend && analysisResult.historicalTrend.length" class="section-title">历史趋势</div>
          <el-table v-if="analysisResult.historicalTrend && analysisResult.historicalTrend.length" :data="analysisResult.historicalTrend" border size="small">
            <el-table-column prop="date" label="时间" align="center" />
            <el-table-column prop="score" label="风险评分" align="center">
              <template slot-scope="{ row }">
                <span :style="{ color: row.score >= 80 ? '#F56C6C' : row.score >= 60 ? '#E6A23C' : '#67C23A' }">{{ row.score }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-empty v-else description="暂无分析数据" />
      </div>
    </el-dialog>

    <!-- 模拟变化弹窗 -->
    <el-dialog title="模拟持股变化" :visible.sync="simulateVisible" width="800px" append-to-body>
      <div v-loading="simulateLoading" class="simulate-content">
        <el-form :model="simulateForm" label-width="140px" size="small" style="margin-bottom:20px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="模拟A持B比例(%)">
                <el-slider v-model="simulateForm.newAHoldBRatio" :max="100" :step="1" show-input />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="模拟B持A比例(%)">
                <el-slider v-model="simulateForm.newBHoldARatio" :max="100" :step="1" show-input />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" icon="el-icon-s-data" @click="doSimulate">执行模拟</el-button>
          </el-form-item>
        </el-form>
        <div v-if="simulateResult.before">
          <el-row :gutter="20">
            <el-col :span="11">
              <el-card shadow="never" class="compare-card before-card">
                <div slot="header" class="compare-header">变更前</div>
                <el-descriptions :column="1" size="small">
                  <el-descriptions-item label="A持B比例">{{ simulateResult.before.aHoldBRatio }}%</el-descriptions-item>
                  <el-descriptions-item label="B持A比例">{{ simulateResult.before.bHoldARatio }}%</el-descriptions-item>
                  <el-descriptions-item label="资本虚增">{{ simulateResult.before.capitalInflation }}万元</el-descriptions-item>
                  <el-descriptions-item label="风险等级">
                    <el-tag :type="riskLevelTagType(simulateResult.before.riskLevel)" size="small">{{ riskLevelMap[simulateResult.before.riskLevel] || simulateResult.before.riskLevel }}</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
            <el-col :span="2" class="compare-arrow"><i class="el-icon-right" style="font-size:24px;color:#409EFF" /></el-col>
            <el-col :span="11">
              <el-card shadow="never" class="compare-card after-card">
                <div slot="header" class="compare-header">变更后</div>
                <el-descriptions :column="1" size="small">
                  <el-descriptions-item label="A持B比例">{{ simulateResult.after.aHoldBRatio }}%</el-descriptions-item>
                  <el-descriptions-item label="B持A比例">{{ simulateResult.after.bHoldARatio }}%</el-descriptions-item>
                  <el-descriptions-item label="资本虚增">{{ simulateResult.after.capitalInflation }}万元</el-descriptions-item>
                  <el-descriptions-item label="风险等级">
                    <el-tag :type="riskLevelTagType(simulateResult.after.riskLevel)" size="small">{{ riskLevelMap[simulateResult.after.riskLevel] || simulateResult.after.riskLevel }}</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
          </el-row>
          <el-alert :title="simulateResult.impact.suggestion" :type="simulateResult.impact.inflationReduction > 0 ? 'success' : 'warning'" show-icon style="margin-top:16px">
            <template slot="default">
              风险变化：{{ simulateResult.impact.riskChange }}，资本虚增{{ simulateResult.impact.inflationReduction > 0 ? '减少' : '增加' }}{{ Math.abs(simulateResult.impact.inflationReduction) }}万元
            </template>
          </el-alert>
        </div>
      </div>
    </el-dialog>

    <!-- 优化建议弹窗 -->
    <el-dialog title="优化建议" :visible.sync="optimizeVisible" width="750px" append-to-body>
      <div v-loading="optimizeLoading" class="optimize-content">
        <div v-if="optimizeResult.suggestions && optimizeResult.suggestions.length">
          <el-row :gutter="16" style="margin-bottom:16px">
            <el-col :span="8">
              <div class="optimize-stat-card">
                <div class="optimize-stat-label">当前风险等级</div>
                <el-tag :type="riskLevelTagType(optimizeResult.currentRisk)">{{ riskLevelMap[optimizeResult.currentRisk] || optimizeResult.currentRisk }}</el-tag>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="optimize-stat-card">
                <div class="optimize-stat-label">目标风险等级</div>
                <el-tag type="success">{{ riskLevelMap[optimizeResult.targetRiskLevel] || optimizeResult.targetRiskLevel }}</el-tag>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="optimize-stat-card">
                <div class="optimize-stat-label">预计降低虚增(万元)</div>
                <span style="color:#67C23A;font-weight:bold;font-size:20px">{{ optimizeResult.expectedInflationReduction }}</span>
              </div>
            </el-col>
          </el-row>
          <el-timeline>
            <el-timeline-item v-for="(item, idx) in optimizeResult.suggestions" :key="idx" :type="item.priority === 'HIGH' ? 'danger' : item.priority === 'MEDIUM' ? 'warning' : 'success'" :timestamp="item.timeline" placement="top">
              <el-card shadow="never" class="suggestion-card">
                <div class="suggestion-header">
                  <el-tag :type="item.priority === 'HIGH' ? 'danger' : item.priority === 'MEDIUM' ? 'warning' : 'success'" size="mini">{{ item.priority === 'HIGH' ? '紧急' : item.priority === 'MEDIUM' ? '重要' : '建议' }}</el-tag>
                  <span class="suggestion-title">{{ item.title }}</span>
                </div>
                <p class="suggestion-desc">{{ item.description }}</p>
                <div class="suggestion-meta">
                  <span><i class="el-icon-aim" /> 预期效果：{{ item.expectedEffect }}</span>
                  <span><i class="el-icon-user" /> 责任部门：{{ item.responsible }}</span>
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
        <el-empty v-else description="暂无优化建议" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCrossHoldingList, getCrossHoldingById, addCrossHolding, updateCrossHolding, deleteCrossHolding, exportCrossHolding, getCrossHoldingStatistics, detectCrossHolding, generateCrossHoldingReport, analyzeCrossHoldingNetwork, assessCrossHoldingRisk, simulateCrossHoldingChange, optimizeCrossHoldingStructure } from '@/api/stateAssets/crossHolding'
import { mapGetters } from 'vuex'
import request from '@/utils/request'

export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'CrossHolding',
  data() {
    return {
      loading: false,
      submitLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyAName: '',
        companyBName: '',
        crossType: '',
        riskLevel: ''
      },
      statistics: {
        total: 0,
        circularCount: 0,
        highRiskCount: 0,
        totalInflation: 0
      },
      crossTypeMap: { DIRECT: '直接交叉', INDIRECT: '间接交叉', CIRCULAR: '循环持股' },
      riskLevelMap: { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' },
      isCircularMap: { Y: '是', N: '否' },
      dialogVisible: false,
      dialogTitle: '新增分析',
      form: {},
      rules: {
        companyAName: [{ required: true, message: '请输入企业A名称', trigger: 'blur' }],
        companyBName: [{ required: true, message: '请输入企业B名称', trigger: 'blur' }],
        aHoldBRatio: [{ required: true, message: '请输入A持B比例', trigger: 'blur' }],
        bHoldARatio: [{ required: true, message: '请输入B持A比例', trigger: 'blur' }],
        crossType: [{ required: true, message: '请选择交叉类型', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
      },
      detailVisible: false,
      detailData: {},
      detectVisible: false,
      detectResult: '',
      networkVisible: false,
      networkData: '',
      // 追踪流向
      traceVisible: false,
      traceLoading: false,
      traceResult: { nodes: [], links: [], paths: [], isCircular: false, chainLength: 0 },
      // 深度分析
      analysisVisible: false,
      analysisLoading: false,
      analysisResult: {},
      // 模拟变化
      simulateVisible: false,
      simulateLoading: false,
      simulateForm: { newAHoldBRatio: 0, newBHoldARatio: 0 },
      simulateResult: {},
      // 优化建议
      optimizeVisible: false,
      optimizeLoading: false,
      optimizeResult: {},
      currentRow: {}
    }
  },
  created() {
    this.getList()
    this.getStatistics()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getCrossHoldingList(this.queryParams)
        if (res.result === 200) {
          this.tableData = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || res.data.total || 0
        }
      } catch (e) {
        console.error('获取交叉持股列表失败', e)
      } finally {
        this.loading = false
      }
    },
    async getStatistics() {
      try {
        const res = await getCrossHoldingStatistics()
        if (res.result === 200 && res.data) {
          this.statistics = res.data
        } else {
          this.calcStatisticsFromTable()
        }
      } catch (e) {
        console.error('获取统计数据失败', e)
        this.calcStatisticsFromTable()
      }
    },
    calcStatisticsFromTable() {
      const data = this.tableData
      this.statistics.total = data.length
      this.statistics.circularCount = data.filter(d => d.isCircular === 'Y').length
      this.statistics.highRiskCount = data.filter(d => d.riskLevel === 'HIGH').length
      this.statistics.totalInflation = data.reduce((sum, d) => sum + (Number(d.capitalInflation) || 0), 0)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleReset() {
      this.queryParams = { pageNum: 1, pageSize: 10, companyAName: '', companyBName: '', crossType: '', riskLevel: '' }
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.dialogTitle = '新增分析'
      this.form = { holdingId: '', companyAId: '', companyAName: '', companyBId: '', companyBName: '', aHoldBRatio: 0, bHoldARatio: 0, crossType: '', riskLevel: '', capitalInflation: 0, isCircular: 'N', chainLength: 0, discoveryDate: '', remark: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑分析'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const res = this.form.holdingId ? await updateCrossHolding(this.form) : await addCrossHolding(this.form)
          if (res.result === 200) {
            this.$message.success(this.form.holdingId ? '编辑成功' : '新增成功')
            this.dialogVisible = false
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    async handleView(row) {
      try {
        const res = await getCrossHoldingById(row.holdingId)
        if (res.result === 200 && res.data) {
          this.detailData = res.data
        } else {
          this.detailData = row
        }
      } catch (e) {
        this.detailData = row
      }
      this.detailVisible = true
    },
    async handleDetect(row) {
      this.currentRow = row
      try {
        const res = await detectCrossHolding({ holdingId: row.holdingId, companyAId: row.companyAId, companyBId: row.companyBId })
        if (res.result === 200 && res.data && Object.keys(res.data).length > 0) {
          this.detectResult = typeof res.data === 'string' ? res.data : JSON.stringify(res.data, null, 2)
        } else {
          this.detectResult = `【交叉持股检测结果】\n\n企业A: ${row.companyAName}\n企业B: ${row.companyBName}\nA持B比例: ${row.aHoldBRatio || 0}%\nB持A比例: ${row.bHoldARatio || 0}%\n交叉类型: ${this.crossTypeMap[row.crossType] || row.crossType}\n是否循环: ${row.isCircular === 'Y' ? '是' : '否'}\n\n结论: ${row.riskLevel === 'HIGH' ? '⚠️ 高风险交叉持股，建议立即整改' : '风险可控，持续监控'}`
        }
      } catch (e) {
        this.detectResult = `【交叉持股检测结果】\n\n企业A: ${row.companyAName}\n企业B: ${row.companyBName}\nA持B比例: ${row.aHoldBRatio || 0}%\nB持A比例: ${row.bHoldARatio || 0}%\n交叉类型: ${this.crossTypeMap[row.crossType] || row.crossType}\n\n结论: 检测完成，${row.riskLevel === 'HIGH' ? '存在高风险' : '风险可控'}`
      }
      this.detectVisible = true
    },
    handleNetwork(row) {
      this.currentRow = row
      this.networkData = `【交叉持股网络关系】\n\n${row.companyAName} ──(${row.aHoldBRatio || 0}%)──> ${row.companyBName}\n${row.companyBName} ──(${row.bHoldARatio || 0}%)──> ${row.companyAName}\n\n链条长度: ${row.chainLength || 1}\n是否循环: ${row.isCircular === 'Y' ? '是（存在资金空转风险）' : '否'}\n资本虚增: ${row.capitalInflation || 0}万元`
      this.networkVisible = true
    },
    handleMoreCommand(command, row) {
      this.currentRow = row
      switch (command) {
        case 'edit': this.handleEdit(row); break
        case 'trace': this.handleTrace(row); break
        case 'analysis': this.handleAnalysis(row); break
        case 'simulate': this.handleSimulateOpen(row); break
        case 'optimize': this.handleOptimize(row); break
        case 'report': this.handleReport(row); break
        case 'delete': this.handleDelete(row); break
      }
    },
    // ========== 追踪流向 ==========
    async handleTrace(row) {
      this.traceVisible = true
      this.traceLoading = true
      this.traceResult = { nodes: [], links: [], paths: [], isCircular: false, chainLength: 0 }
      try {
        const res = await analyzeCrossHoldingNetwork({ holdingId: row.holdingId })
        if (res.result === 200 && res.data) {
          this.traceResult = res.data
        } else {
          this.$message.warning(res.msg || '暂无流向数据')
        }
      } catch (e) {
        this.$message.error('追踪流向失败')
      } finally {
        this.traceLoading = false
      }
    },
    getNodeName(id) {
      const node = (this.traceResult.nodes || []).find(n => n.id === id)
      return node ? node.name : id
    },
    // ========== 深度分析 ==========
    async handleAnalysis(row) {
      this.analysisVisible = true
      this.analysisLoading = true
      this.analysisResult = {}
      try {
        const res = await assessCrossHoldingRisk({ holdingId: row.holdingId })
        if (res.result === 200 && res.data) {
          this.analysisResult = res.data
        } else {
          this.$message.warning(res.msg || '暂无分析数据')
        }
      } catch (e) {
        this.$message.error('深度分析失败')
      } finally {
        this.analysisLoading = false
      }
    },
    getProgressColor(score) {
      if (score >= 80) return '#F56C6C'
      if (score >= 60) return '#E6A23C'
      return '#67C23A'
    },
    // ========== 模拟变化 ==========
    handleSimulateOpen(row) {
      this.simulateVisible = true
      this.simulateForm = {
        newAHoldBRatio: Number(row.aHoldBRatio) || 0,
        newBHoldARatio: Number(row.bHoldARatio) || 0
      }
      this.simulateResult = {}
    },
    async doSimulate() {
      this.simulateLoading = true
      try {
        const res = await simulateCrossHoldingChange({
          holdingId: this.currentRow.holdingId,
          newAHoldBRatio: this.simulateForm.newAHoldBRatio,
          newBHoldARatio: this.simulateForm.newBHoldARatio
        })
        if (res.result === 200 && res.data) {
          this.simulateResult = res.data
        } else {
          this.$message.warning(res.msg || '模拟失败')
        }
      } catch (e) {
        this.$message.error('模拟变化失败')
      } finally {
        this.simulateLoading = false
      }
    },
    // ========== 优化建议 ==========
    async handleOptimize(row) {
      this.optimizeVisible = true
      this.optimizeLoading = true
      this.optimizeResult = {}
      try {
        const res = await optimizeCrossHoldingStructure({ holdingId: row.holdingId })
        if (res.result === 200 && res.data) {
          this.optimizeResult = res.data
        } else {
          this.$message.warning(res.msg || '暂无优化建议')
        }
      } catch (e) {
        this.$message.error('获取优化建议失败')
      } finally {
        this.optimizeLoading = false
      }
    },
    // ========== 生成报告 ==========
    async handleReport(row) {
      const loading = this.$loading({ lock: true, text: '正在生成报告...', background: 'rgba(0,0,0,0.7)' })
      try {
        const res = await generateCrossHoldingReport({ holdingId: row.holdingId })
        if (res.result === 200 && res.data) {
          const report = res.data
          // 构建HTML格式报告预览
          const html = `
            <div style="line-height:1.8;font-size:14px;">
              <h3 style="text-align:center;margin-bottom:16px">${report.reportTitle || '交叉持股分析报告'}</h3>
              <p style="text-align:right;color:#999;font-size:12px">生成时间：${report.generatedTime}</p>
              <h4>一、基本信息</h4>
              <p>企业A：${report.basicInfo.companyAName}，企业B：${report.basicInfo.companyBName}</p>
              <p>A持B比例：${report.basicInfo.aHoldBRatio}%，B持A比例：${report.basicInfo.bHoldARatio}%</p>
              <h4>二、风险评估</h4>
              <p>风险等级：<b>${this.riskLevelMap[report.riskAssessment.riskLevel] || report.riskAssessment.riskLevel}</b></p>
              <p>资本虚增：${report.riskAssessment.capitalInflation}万元</p>
              <p>是否循环：${report.riskAssessment.isCircular === 'Y' ? '是' : '否'}，链条长度：${report.riskAssessment.chainLength}</p>
              <h4>三、分析结论</h4>
              <p>交叉类型：${this.crossTypeMap[report.analysis.crossType] || report.analysis.crossType}</p>
              <p>持股强度：${report.analysis.intensity}，稳定性：${report.analysis.stability === 'UNSTABLE' ? '不稳定' : report.analysis.stability === 'MODERATE' ? '一般' : '稳定'}</p>
              <h4>四、建议措施</h4>
              <ul>${(report.suggestions || []).map(s => '<li>' + s + '</li>').join('')}</ul>
              <h4>五、总结</h4>
              <p style="color:#F56C6C;font-weight:bold">${report.conclusion}</p>
            </div>`
          this.$alert(html, '交叉持股分析报告', { confirmButtonText: '确定', dangerouslyUseHTMLString: true, customClass: 'report-dialog' })
        } else {
          this.$message.error(res.msg || '报告生成失败')
        }
      } catch (e) {
        this.$message.error('报告生成失败')
      } finally {
        loading.close()
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该交叉持股记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteCrossHolding(row.holdingId)
          if (res.result === 200) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleBatchDetect() {
      if (!this.selectedRows.length) {
        this.$message.warning('请选择要检测的记录')
        return
      }
      const ids = this.selectedRows.map(r => r.holdingId)
      try {
        const res = await request({ url: '/monitor/v1/supervision/asset/cross-holding/batch/detect', method: 'post', data: { ids } })
        if (res.result === 200) {
          this.$message.success('批量检测完成')
          this.getList()
          this.getStatistics()
        } else {
          this.$message.error(res.msg || '批量检测失败')
        }
      } catch (e) {
        this.$message.error('批量检测失败')
      }
    },
    async handleExport() {
      try {
        const res = await exportCrossHolding(this.queryParams)
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '交叉持股分析_' + new Date().getTime() + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    crossTypeTagType(type) {
      const map = { DIRECT: '', INDIRECT: 'warning', CIRCULAR: 'danger' }
      return map[type] || 'info'
    },
    riskLevelTagType(level) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[level] || 'info'
    }
  }
}
</script>

<style scoped>
.cross-holding-container {
  padding: 20px;
}
.stats-row {
  margin-bottom: 20px;
}
.stat-card {
  text-align: center;
  cursor: pointer;
}
.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.stat-value.circular {
  color: #E6A23C;
}
.stat-value.high-risk {
  color: #F56C6C;
}
.stat-value.inflation {
  color: #409EFF;
}
.filter-card {
  margin-bottom: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.toolbar {
  display: flex;
  align-items: center;
}
.detect-result,
.network-content,
.trace-content,
.analysis-content,
.simulate-content,
.optimize-content {
  padding: 10px;
  line-height: 1.8;
  color: #606266;
}
.section-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
  padding-left: 8px;
  border-left: 3px solid #409EFF;
}
/* 深度分析 - 风险评分卡 */
.risk-score-card {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  background: #f5f7fa;
}
.risk-score-card.risk-high { background: #fef0f0; }
.risk-score-card.risk-medium { background: #fdf6ec; }
.risk-score-card.risk-low { background: #f0f9eb; }
.risk-score-value {
  font-size: 48px;
  font-weight: bold;
  color: #303133;
}
.risk-score-label {
  font-size: 13px;
  color: #909399;
  margin: 8px 0;
}
.dimension-item {
  margin-bottom: 12px;
}
.dim-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}
.dim-name { font-size: 13px; color: #606266; }
.dim-score { font-size: 13px; font-weight: bold; }
.dim-desc { font-size: 12px; color: #909399; margin-top: 2px; }
/* 模拟变化 - 对比卡片 */
.compare-card { height: 100%; }
.compare-header { font-weight: bold; text-align: center; }
.before-card { border-top: 3px solid #E6A23C; }
.after-card { border-top: 3px solid #67C23A; }
.compare-arrow { display: flex; align-items: center; justify-content: center; }
/* 优化建议 */
.optimize-stat-card { text-align: center; padding: 12px; background: #f5f7fa; border-radius: 6px; }
.optimize-stat-label { font-size: 12px; color: #909399; margin-bottom: 8px; }
.suggestion-card { margin-bottom: 0; }
.suggestion-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.suggestion-title { font-weight: bold; font-size: 14px; }
.suggestion-desc { color: #606266; font-size: 13px; margin: 4px 0; }
.suggestion-meta { display: flex; gap: 20px; font-size: 12px; color: #909399; }
</style>