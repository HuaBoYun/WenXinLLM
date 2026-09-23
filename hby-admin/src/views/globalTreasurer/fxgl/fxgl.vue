<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="风险类型编码">
          <el-input
            v-model="queryParams.riskTypeCode"
            placeholder="请输入风险类型编码"
            clearable
            size="small"
            style="width: 200px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="风险类型名称">
          <el-input
            v-model="queryParams.riskTypeName"
            placeholder="请输入风险类型名称"
            clearable
            size="small"
            style="width: 200px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="风险类别">
          <el-select
            v-model="queryParams.riskCategory"
            placeholder="请选择风险类别"
            clearable
            size="small"
            style="width: 150px"
          >
            <el-option label="市场风险" value="MARKET" />
            <el-option label="信用风险" value="CREDIT" />
            <el-option label="流动性风险" value="LIQUIDITY" />
            <el-option label="操作风险" value="OPERATIONAL" />
            <el-option label="合规风险" value="COMPLIANCE" />
            <el-option label="声誉风险" value="REPUTATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="影响程度">
          <el-select
            v-model="queryParams.impactLevel"
            placeholder="请选择影响程度"
            clearable
            size="small"
            style="width: 120px"
          >
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
            <el-option label="极高" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="发生概率">
          <el-select
            v-model="queryParams.probabilityLevel"
            placeholder="请选择发生概率"
            clearable
            size="small"
            style="width: 120px"
          >
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
            <el-option label="极高" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryParams.isEnabled"
            placeholder="请选择状态"
            clearable
            size="small"
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="filter-container">
      <el-button
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['risk:type:add']"
      >新增</el-button>
      <el-button
        type="success"
        plain
        icon="el-icon-edit"
        size="mini"
        :disabled="single"
        @click="handleUpdate"
        v-hasPermi="['risk:type:edit']"
      >修改</el-button>
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="mini"
        :disabled="multiple"
        @click="handleDelete"
        v-hasPermi="['risk:type:delete']"
      >删除</el-button>
      <el-button
        type="warning"
        plain
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
        v-hasPermi="['risk:type:export']"
      >导出</el-button>
      <el-button
        type="info"
        plain
        icon="el-icon-s-data"
        size="mini"
        @click="handleStatistics"
        v-hasPermi="['risk:query']"
      >统计分析</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="riskTypeList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="风险类型编码" align="center" prop="riskTypeCode" width="150" />
      <el-table-column label="风险类型名称" align="center" prop="riskTypeName" width="200" />
      <el-table-column label="风险类别" align="center" prop="riskCategory" width="120">
        <template slot-scope="scope">
          <dict-tag :options="riskCategoryOptions" :value="scope.row.riskCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="影响程度" align="center" prop="impactLevel" width="100">
        <template slot-scope="scope">
          <el-tag
            :type="getImpactLevelType(scope.row.impactLevel)"
            size="mini"
          >
            {{ getImpactLevelText(scope.row.impactLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发生概率" align="center" prop="probabilityLevel" width="100">
        <template slot-scope="scope">
          <el-tag
            :type="getProbabilityLevelType(scope.row.probabilityLevel)"
            size="mini"
          >
            {{ getProbabilityLevelText(scope.row.probabilityLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" align="center" prop="riskLevel" width="100">
        <template slot-scope="scope">
          <el-tag
            :type="getRiskLevelType(scope.row.riskLevel)"
            size="mini"
          >
            {{ getRiskLevelText(scope.row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负责部门" align="center" prop="responsibleDepartment" width="150" />
      <el-table-column label="负责人" align="center" prop="responsiblePerson" width="100" />
      <el-table-column label="审查频率" align="center" prop="reviewFrequency" width="100" />
      <el-table-column label="下次审查日期" align="center" prop="nextReviewDate" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.nextReviewDate">{{ parseTime(scope.row.nextReviewDate, '{y}-{m}-{d}') }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="isEnabled" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['risk:type:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['risk:type:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['risk:type:delete']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleAssessment(scope.row)"
            v-hasPermi="['risk:assessment:add']"
          >评估</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleMonitoring(scope.row)"
            v-hasPermi="['risk:monitoring:add']"
          >监控</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改风险类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="风险类型编码" prop="riskTypeCode">
              <el-input v-model="form.riskTypeCode" placeholder="请输入风险类型编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险类型名称" prop="riskTypeName">
              <el-input v-model="form.riskTypeName" placeholder="请输入风险类型名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="风险类别" prop="riskCategory">
              <el-select v-model="form.riskCategory" placeholder="请选择风险类别" style="width: 100%">
                <el-option label="市场风险" value="MARKET" />
                <el-option label="信用风险" value="CREDIT" />
                <el-option label="流动性风险" value="LIQUIDITY" />
                <el-option label="操作风险" value="OPERATIONAL" />
                <el-option label="合规风险" value="COMPLIANCE" />
                <el-option label="声誉风险" value="REPUTATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响程度" prop="impactLevel">
              <el-select v-model="form.impactLevel" placeholder="请选择影响程度" style="width: 100%">
                <el-option label="低" value="LOW" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="高" value="HIGH" />
                <el-option label="极高" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发生概率" prop="probabilityLevel">
              <el-select v-model="form.probabilityLevel" placeholder="请选择发生概率" style="width: 100%">
                <el-option label="低" value="LOW" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="高" value="HIGH" />
                <el-option label="极高" value="CRITICAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审查频率" prop="reviewFrequency">
              <el-select v-model="form.reviewFrequency" placeholder="请选择审查频率" style="width: 100%">
                <el-option label="月度" value="MONTHLY" />
                <el-option label="季度" value="QUARTERLY" />
                <el-option label="半年度" value="SEMI_ANNUALLY" />
                <el-option label="年度" value="ANNUALLY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责部门" prop="responsibleDepartment">
              <el-input v-model="form.responsibleDepartment" placeholder="请输入负责部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="responsiblePerson">
              <el-input v-model="form.responsiblePerson" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="下次审查日期" prop="nextReviewDate">
              <el-date-picker
                v-model="form.nextReviewDate"
                type="date"
                placeholder="选择下次审查日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="isEnabled">
              <el-radio-group v-model="form.isEnabled">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="风险描述" prop="riskDescription">
          <el-input
            v-model="form.riskDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入风险描述"
          />
        </el-form-item>
        <el-form-item label="控制措施" prop="controlMeasures">
          <el-input
            v-model="form.controlMeasures"
            type="textarea"
            :rows="3"
            placeholder="请输入控制措施"
          />
        </el-form-item>
        <el-form-item label="监控指标" prop="monitoringIndicators">
          <el-input
            v-model="form.monitoringIndicators"
            type="textarea"
            :rows="2"
            placeholder="请输入监控指标"
          />
        </el-form-item>
        <el-form-item label="应对策略" prop="responseStrategy">
          <el-input
            v-model="form.responseStrategy"
            type="textarea"
            :rows="2"
            placeholder="请输入应对策略"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看风险类型详情对话框 -->
    <el-dialog title="风险类型详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="风险类型编码">{{ viewForm.riskTypeCode }}</el-descriptions-item>
        <el-descriptions-item label="风险类型名称">{{ viewForm.riskTypeName }}</el-descriptions-item>
        <el-descriptions-item label="风险类别">
          <dict-tag :options="riskCategoryOptions" :value="viewForm.riskCategory"/>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelType(viewForm.riskLevel)" size="mini">
            {{ getRiskLevelText(viewForm.riskLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="影响程度">
          <el-tag :type="getImpactLevelType(viewForm.impactLevel)" size="mini">
            {{ getImpactLevelText(viewForm.impactLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发生概率">
          <el-tag :type="getProbabilityLevelType(viewForm.probabilityLevel)" size="mini">
            {{ getProbabilityLevelText(viewForm.probabilityLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责部门">{{ viewForm.responsibleDepartment }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ viewForm.responsiblePerson }}</el-descriptions-item>
        <el-descriptions-item label="审查频率">{{ viewForm.reviewFrequency }}</el-descriptions-item>
        <el-descriptions-item label="下次审查日期">
          {{ viewForm.nextReviewDate ? parseTime(viewForm.nextReviewDate, '{y}-{m}-{d}') : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewForm.isEnabled === 1 ? 'success' : 'danger'" size="mini">
            {{ viewForm.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ parseTime(viewForm.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="风险描述" :span="2">{{ viewForm.riskDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="控制措施" :span="2">{{ viewForm.controlMeasures || '-' }}</el-descriptions-item>
        <el-descriptions-item label="监控指标" :span="2">{{ viewForm.monitoringIndicators || '-' }}</el-descriptions-item>
        <el-descriptions-item label="应对策略" :span="2">{{ viewForm.responseStrategy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 统计分析对话框 -->
    <el-dialog title="风险管理统计分析" :visible.sync="statisticsOpen" width="1200px" append-to-body>
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="基础统计" name="basic">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="statistic-card">
                <div class="statistic-title">总风险类型</div>
                <div class="statistic-value">{{ statistics.totalRiskTypes || 0 }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-card">
                <div class="statistic-title">启用风险类型</div>
                <div class="statistic-value">{{ statistics.enabledRiskTypes || 0 }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-card">
                <div class="statistic-title">高风险类型</div>
                <div class="statistic-value">{{ statistics.highRiskTypes || 0 }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="statistic-card">
                <div class="statistic-title">关键风险类型</div>
                <div class="statistic-value">{{ statistics.criticalRiskTypes || 0 }}</div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="分类统计" name="category">
          <div id="categoryChart" style="width: 100%; height: 400px;"></div>
        </el-tab-pane>
        <el-tab-pane label="趋势分析" name="trend">
          <div id="trendChart" style="width: 100%; height: 400px;"></div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="statisticsOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRiskType, getRiskType, delRiskType, addRiskType, updateRiskType, toggleRiskTypeStatus, getRiskStatistics } from "@/api/globalTreasurer/fxgl"
import { parseTime } from '@/utils'

export default {
  name: "RiskManagement",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 风险类型表格数据
      riskTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 是否显示统计分析弹出层
      statisticsOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        riskTypeCode: null,
        riskTypeName: null,
        riskCategory: null,
        impactLevel: null,
        probabilityLevel: null,
        isEnabled: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 统计数据
      statistics: {},
      // 活动标签页
      activeTab: 'basic',
      // 表单校验
      rules: {
        riskTypeCode: [
          { required: true, message: "风险类型编码不能为空", trigger: "blur" }
        ],
        riskTypeName: [
          { required: true, message: "风险类型名称不能为空", trigger: "blur" }
        ],
        riskCategory: [
          { required: true, message: "风险类别不能为空", trigger: "change" }
        ],
        impactLevel: [
          { required: true, message: "影响程度不能为空", trigger: "change" }
        ],
        probabilityLevel: [
          { required: true, message: "发生概率不能为空", trigger: "change" }
        ]
      },
      // 风险类别选项
      riskCategoryOptions: [
        { label: "市场风险", value: "MARKET" },
        { label: "信用风险", value: "CREDIT" },
        { label: "流动性风险", value: "LIQUIDITY" },
        { label: "操作风险", value: "OPERATIONAL" },
        { label: "合规风险", value: "COMPLIANCE" },
        { label: "声誉风险", value: "REPUTATION" }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询风险类型列表 */
    getList() {
      this.loading = true;
      listRiskType(this.queryParams).then(response => {
        this.riskTypeList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        riskTypeId: null,
        riskTypeCode: null,
        riskTypeName: null,
        riskCategory: null,
        riskDescription: null,
        impactLevel: null,
        probabilityLevel: null,
        controlMeasures: null,
        monitoringIndicators: null,
        responseStrategy: null,
        responsibleDepartment: null,
        responsiblePerson: null,
        reviewFrequency: null,
        nextReviewDate: null,
        isEnabled: 1,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.riskTypeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 行点击事件
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加风险类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const riskTypeId = row.riskTypeId || this.ids
      getRiskType(riskTypeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改风险类型";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      const riskTypeId = row.riskTypeId;
      getRiskType(riskTypeId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.riskTypeId != null) {
            updateRiskType(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRiskType(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const riskTypeIds = row.riskTypeId || this.ids;
      this.$modal.confirm('是否确认删除风险类型编号为"' + riskTypeIds + '"的数据项？').then(function() {
        return delRiskType(riskTypeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.riskTypeName + '"风险类型吗？').then(function() {
        return toggleRiskTypeStatus(row.riskTypeId, row.isEnabled);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('risk/type/export', {
        ...this.queryParams
      }, `risk_type_${new Date().getTime()}.xlsx`)
    },
    /** 统计分析按钮操作 */
    handleStatistics() {
      this.statisticsOpen = true;
      this.loadStatistics();
    },
    /** 加载统计数据 */
    loadStatistics() {
      getRiskStatistics().then(response => {
        this.statistics = response.data;
        this.$nextTick(() => {
          this.renderCharts();
        });
      });
    },
    /** 渲染图表 */
    renderCharts() {
      // 这里可以使用 ECharts 渲染图表
      // 由于篇幅限制，这里只是示例
      console.log('渲染统计图表', this.statistics);
    },
    /** 风险评估按钮操作 */
    handleAssessment(row) {
      this.$router.push({
        path: '/globalTreasurer/fxpg',
        query: { riskTypeId: row.riskTypeId }
      });
    },
    /** 风险监控按钮操作 */
    handleMonitoring(row) {
      this.$router.push({
        path: '/globalTreasurer/fxjk',
        query: { riskTypeId: row.riskTypeId }
      });
    },
    // 获取影响程度标签类型
    getImpactLevelType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      };
      return typeMap[level] || 'info';
    },
    // 获取影响程度文本
    getImpactLevelText(level) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '极高'
      };
      return textMap[level] || level;
    },
    // 获取发生概率标签类型
    getProbabilityLevelType(level) {
      return this.getImpactLevelType(level);
    },
    // 获取发生概率文本
    getProbabilityLevelText(level) {
      return this.getImpactLevelText(level);
    },
    // 获取风险等级标签类型
    getRiskLevelType(level) {
      return this.getImpactLevelType(level);
    },
    // 获取风险等级文本
    getRiskLevelText(level) {
      return this.getImpactLevelText(level);
    }
  }
};
</script>

<style scoped>
.statistic-card {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.statistic-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}
</style>
