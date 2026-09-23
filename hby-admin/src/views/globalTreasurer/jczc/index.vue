<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 决策模型管理 -->
      <el-tab-pane label="决策模型管理" name="decisionModel">
        <div class="decision-model-container">
          <!-- 查询条件 -->
          <el-form :model="modelQuery" ref="modelQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="模型名称" prop="modelName">
              <el-input
                v-model="modelQuery.modelName"
                placeholder="请输入模型名称"
                clearable
                @keyup.enter.native="handleModelQuery"
              />
            </el-form-item>
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="modelQuery.modelType" placeholder="请选择模型类型" clearable>
                <el-option label="风险评估模型" value="RISK_ASSESSMENT" />
                <el-option label="投资决策模型" value="INVESTMENT_DECISION" />
                <el-option label="融资决策模型" value="FINANCING_DECISION" />
                <el-option label="现金流预测模型" value="CASHFLOW_FORECAST" />
              </el-select>
            </el-form-item>
            <el-form-item label="模型状态" prop="modelStatus">
              <el-select v-model="modelQuery.modelStatus" placeholder="请选择模型状态" clearable>
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
                <el-option label="测试中" value="TESTING" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleModelQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetModelQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleModelAdd"
              >新增模型</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-setting"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelConfig"
              >配置</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-data-analysis"
                size="mini"
                :disabled="modelSingle"
                @click="handleModelTest"
              >测试</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="modelLoading" :data="modelList" @selection-change="handleModelSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="模型编号" align="center" prop="modelCode" />
            <el-table-column label="模型名称" align="center" prop="modelName" />
            <el-table-column label="模型类型" align="center" prop="modelType">
              <template slot-scope="scope">
                <dict-tag :options="modelTypeOptions" :value="scope.row.modelType"/>
              </template>
            </el-table-column>
            <el-table-column label="准确率" align="center" prop="accuracy">
              <template slot-scope="scope">
                <span>{{ scope.row.accuracy ? (scope.row.accuracy * 100).toFixed(2) + '%' : '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="最后训练时间" align="center" prop="lastTrainTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.lastTrainTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="模型状态" align="center" prop="modelStatus">
              <template slot-scope="scope">
                <dict-tag :options="modelStatusOptions" :value="scope.row.modelStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleModelView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleModelUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-setting"
                  @click="handleModelConfig(scope.row)"
                >配置</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-data-analysis"
                  @click="handleModelTest(scope.row)"
                >测试</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="modelQuery.pageNum"
            :layout="layout"
            :page-size="modelQuery.pageSize"
            :total="modelTotal"
            @current-change="handleModelCurrentChange"
            @size-change="handleModelSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 数据分析任务 -->
      <el-tab-pane label="数据分析任务" name="dataAnalysis">
        <div class="data-analysis-container">
          <p>数据分析任务功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- KPI指标管理 -->
      <el-tab-pane label="KPI指标管理" name="kpiIndicator">
        <div class="kpi-indicator-container">
          <p>KPI指标管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 预测分析 -->
      <el-tab-pane label="预测分析" name="predictiveAnalysis">
        <div class="predictive-analysis-container">
          <p>预测分析功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 决策建议 -->
      <el-tab-pane label="决策建议" name="decisionRecommendation">
        <div class="decision-recommendation-container">
          <p>决策建议功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改决策模型对话框 -->
    <el-dialog :title="modelTitle" :visible.sync="modelOpen" width="800px" append-to-body>
      <el-form ref="modelForm" :model="modelForm" :rules="modelRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模型名称" prop="modelName">
              <el-input v-model="modelForm.modelName" placeholder="请输入模型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型类型" prop="modelType">
              <el-select v-model="modelForm.modelType" placeholder="请选择模型类型">
                <el-option label="风险评估模型" value="RISK_ASSESSMENT" />
                <el-option label="投资决策模型" value="INVESTMENT_DECISION" />
                <el-option label="融资决策模型" value="FINANCING_DECISION" />
                <el-option label="现金流预测模型" value="CASHFLOW_FORECAST" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="算法类型" prop="algorithmType">
              <el-select v-model="modelForm.algorithmType" placeholder="请选择算法类型">
                <el-option label="线性回归" value="LINEAR_REGRESSION" />
                <el-option label="逻辑回归" value="LOGISTIC_REGRESSION" />
                <el-option label="决策树" value="DECISION_TREE" />
                <el-option label="随机森林" value="RANDOM_FOREST" />
                <el-option label="神经网络" value="NEURAL_NETWORK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模型状态" prop="modelStatus">
              <el-select v-model="modelForm.modelStatus" placeholder="请选择模型状态">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
                <el-option label="测试中" value="TESTING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模型描述" prop="modelDescription">
          <el-input v-model="modelForm.modelDescription" type="textarea" placeholder="请输入模型描述" />
        </el-form-item>
        <el-form-item label="输入参数" prop="inputParameters">
          <el-input v-model="modelForm.inputParameters" type="textarea" placeholder="请输入输入参数（JSON格式）" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="modelForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitModelForm">确 定</el-button>
        <el-button @click="cancelModel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDecisionModelPage,
  getDecisionModel,
  createDecisionModel,
  updateDecisionModel,
  deleteDecisionModel
} from "@/api/globalTreasurer/jczc";
import { parseTime } from '@/utils'

export default {
  name: "DecisionSupport",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "decisionModel",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",
      
      // 决策模型相关数据
      modelLoading: true,
      modelIds: [],
      modelSingle: true,
      modelMultiple: true,
      modelTotal: 0,
      modelList: [],
      modelTitle: "",
      modelOpen: false,
      modelQuery: {
        pageNum: 1,
        pageSize: 10,
        modelName: null,
        modelType: null,
        modelStatus: null
      },
      modelForm: {},
      modelRules: {
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ],
        modelType: [
          { required: true, message: "模型类型不能为空", trigger: "change" }
        ],
        algorithmType: [
          { required: true, message: "算法类型不能为空", trigger: "change" }
        ],
        modelStatus: [
          { required: true, message: "模型状态不能为空", trigger: "change" }
        ]
      },

      // 字典选项
      modelTypeOptions: [
        { label: "风险评估模型", value: "RISK_ASSESSMENT" },
        { label: "投资决策模型", value: "INVESTMENT_DECISION" },
        { label: "融资决策模型", value: "FINANCING_DECISION" },
        { label: "现金流预测模型", value: "CASHFLOW_FORECAST" }
      ],
      modelStatusOptions: [
        { label: "启用", value: "ACTIVE" },
        { label: "停用", value: "INACTIVE" },
        { label: "测试中", value: "TESTING" }
      ]
    };
  },
  created() {
    this.getModelList();
  },
  methods: {
    parseTime,

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'decisionModel') {
        this.getModelList();
      }
    },

    /** 查询决策模型列表 */
    async getModelList() {
      this.modelLoading = true;
      try {
        const response = await getDecisionModelPage(this.modelQuery);
        
        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.modelList = response.data.tlist || [];
            this.modelTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.modelList = response.data.list || [];
            this.modelTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.modelList = response.data || [];
            this.modelTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            // 直接PageInfo格式：{ code: 200, tlist: [], totalRecord: 0 }
            this.modelList = response.tlist || [];
            this.modelTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.modelList = response.list || [];
            this.modelTotal = response.total || 0;
          } else {
            // 兜底处理
            this.modelList = [];
            this.modelTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.modelList = [];
          this.modelTotal = 0;
        }
      } catch (error) {
        console.error('获取决策模型列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.modelList = [];
        this.modelTotal = 0;
      }
      this.modelLoading = false;
    },

    /** 搜索按钮操作 */
    handleModelQuery() {
      this.modelQuery.pageNum = 1;
      this.getModelList();
    },

    /** 重置按钮操作 */
    resetModelQuery() {
      this.resetForm("modelQueryForm");
      this.handleModelQuery();
    },

    /** 多选框选中数据 */
    handleModelSelectionChange(selection) {
      this.modelIds = selection.map(item => item.modelId);
      this.modelSingle = selection.length !== 1;
      this.modelMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleModelAdd() {
      this.resetModelForm();
      this.modelOpen = true;
      this.modelTitle = "添加决策模型";
    },

    /** 修改按钮操作 */
    handleModelUpdate(row) {
      this.resetModelForm();
      const modelId = row.modelId || this.modelIds;
      getDecisionModel(modelId).then(response => {
        this.modelForm = response.data;
        this.modelOpen = true;
        this.modelTitle = "修改决策模型";
      });
    },

    /** 查看按钮操作 */
    handleModelView(row) {
      this.$router.push(`/globalTreasurer/jczc/model/detail/${row.modelId}`);
    },

    /** 配置按钮操作 */
    handleModelConfig(row) {
      this.$router.push(`/globalTreasurer/jczc/model/config/${row.modelId}`);
    },

    /** 测试按钮操作 */
    handleModelTest(row) {
      this.$router.push(`/globalTreasurer/jczc/model/test/${row.modelId}`);
    },

    /** 提交按钮 */
    submitModelForm() {
      this.$refs["modelForm"].validate(valid => {
        if (valid) {
          if (this.modelForm.modelId != null) {
            updateDecisionModel(this.modelForm).then(response => {
              this.$message.success("修改成功");
              this.modelOpen = false;
              this.getModelList();
            });
          } else {
            createDecisionModel(this.modelForm).then(response => {
              this.$message.success("新增成功");
              this.modelOpen = false;
              this.getModelList();
            });
          }
        }
      });
    },

    /** 取消按钮 */
    cancelModel() {
      this.modelOpen = false;
      this.resetModelForm();
    },

    /** 表单重置 */
    resetModelForm() {
      this.modelForm = {
        modelId: null,
        modelName: null,
        modelType: null,
        algorithmType: null,
        modelStatus: "TESTING",
        modelDescription: null,
        inputParameters: null,
        remark: null
      };
      this.resetForm("modelForm");
    },

    /** 分页相关方法 */
    handleModelCurrentChange(val) {
      this.modelQuery.pageNum = val;
      this.getModelList();
    },

    handleModelSizeChange(val) {
      this.modelQuery.pageSize = val;
      this.getModelList();
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
