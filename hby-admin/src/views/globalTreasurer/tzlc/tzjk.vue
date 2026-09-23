<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总监控数量</span>
          </div>
          <div class="text item">
            <span class="number">{{ dashboardData.totalMonitoring || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>活跃监控</span>
          </div>
          <div class="text item">
            <span class="number success">{{ dashboardData.activeMonitoring || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>高风险预警</span>
          </div>
          <div class="text item">
            <span class="number danger">{{ dashboardData.highAlerts || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平均绩效评分</span>
          </div>
          <div class="text item">
            <span class="number primary">{{ (dashboardData.avgPerformanceScore || 0).toFixed(1) }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="投资ID" prop="investmentId">
        <el-input
          v-model="queryParams.investmentId"
          placeholder="请输入投资ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="监控类型" prop="monitoringType">
        <el-select v-model="queryParams.monitoringType" placeholder="请选择监控类型" clearable>
          <el-option label="日常监控" value="DAILY" />
          <el-option label="周度监控" value="WEEKLY" />
          <el-option label="月度监控" value="MONTHLY" />
          <el-option label="季度监控" value="QUARTERLY" />
          <el-option label="年度监控" value="ANNUAL" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别" prop="alertLevel">
        <el-select v-model="queryParams.alertLevel" placeholder="请选择预警级别" clearable>
          <el-option label="低风险" value="LOW" />
          <el-option label="中风险" value="MEDIUM" />
          <el-option label="高风险" value="HIGH" />
        </el-select>
      </el-form-item>
      <el-form-item label="监控状态" prop="monitoringStatus">
        <el-select v-model="queryParams.monitoringStatus" placeholder="请选择监控状态" clearable>
          <el-option label="活跃" value="ACTIVE" />
          <el-option label="暂停" value="SUSPENDED" />
          <el-option label="完成" value="COMPLETED" />
        </el-select>
      </el-form-item>
      <el-form-item label="监控日期" prop="startDate">
        <el-date-picker
          v-model="queryParams.startDate"
          type="date"
          placeholder="开始日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item label="至" prop="endDate">
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          @click="handleAdd"
          v-hasPermi="['investment:monitoring:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['investment:monitoring:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['investment:monitoring:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleGenerate"
          v-hasPermi="['investment:monitoring:generate']"
        >生成监控</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['investment:monitoring:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="monitoringList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投资ID" align="center" prop="investmentId" />
      <el-table-column label="监控日期" align="center" prop="monitoringDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.monitoringDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监控类型" align="center" prop="monitoringType">
        <template slot-scope="scope">
          <dict-tag :options="monitoringTypeOptions" :value="scope.row.monitoringType"/>
        </template>
      </el-table-column>
      <el-table-column label="当前价值" align="center" prop="currentValue">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.currentValue || 0).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价值变化" align="center" prop="valueChange">
        <template slot-scope="scope">
          <span :class="scope.row.valueChange >= 0 ? 'text-success' : 'text-danger'">
            {{ parseFloat(scope.row.valueChange || 0).toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="变化率" align="center" prop="valueChangeRate">
        <template slot-scope="scope">
          <span :class="scope.row.valueChangeRate >= 0 ? 'text-success' : 'text-danger'">
            {{ (scope.row.valueChangeRate * 100).toFixed(2) }}%
          </span>
        </template>
      </el-table-column>
      <el-table-column label="绩效评分" align="center" prop="performanceScore">
        <template slot-scope="scope">
          <el-progress 
            :percentage="scope.row.performanceScore" 
            :color="getPerformanceColor(scope.row.performanceScore)"
            :show-text="false"
            style="width: 80px;"
          />
          <span style="margin-left: 10px;">{{ scope.row.performanceScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险评分" align="center" prop="riskScore">
        <template slot-scope="scope">
          <el-progress 
            :percentage="scope.row.riskScore" 
            :color="getRiskColor(scope.row.riskScore)"
            :show-text="false"
            style="width: 80px;"
          />
          <span style="margin-left: 10px;">{{ scope.row.riskScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预警级别" align="center" prop="alertLevel">
        <template slot-scope="scope">
          <el-tag :type="getAlertLevelType(scope.row.alertLevel)">{{ scope.row.alertLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="监控状态" align="center" prop="monitoringStatus">
        <template slot-scope="scope">
          <el-tag :type="getMonitoringStatusType(scope.row.monitoringStatus)">{{ getMonitoringStatusText(scope.row.monitoringStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['investment:monitoring:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['investment:monitoring:edit']"
          >修改</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['investment:monitoring:process']">
            <span class="el-dropdown-link">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="processAlert">处理预警</el-dropdown-item>
              <el-dropdown-item command="updateRecord">更新记录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['investment:monitoring:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改投资监控对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资ID" prop="investmentId">
              <el-input v-model="form.investmentId" placeholder="请输入投资ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监控日期" prop="monitoringDate">
              <el-date-picker
                v-model="form.monitoringDate"
                type="date"
                placeholder="选择监控日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="监控类型" prop="monitoringType">
              <el-select v-model="form.monitoringType" placeholder="请选择监控类型">
                <el-option label="日常监控" value="DAILY" />
                <el-option label="周度监控" value="WEEKLY" />
                <el-option label="月度监控" value="MONTHLY" />
                <el-option label="季度监控" value="QUARTERLY" />
                <el-option label="年度监控" value="ANNUAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前价值" prop="currentValue">
              <el-input v-model="form.currentValue" placeholder="请输入当前价值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="绩效评分" prop="performanceScore">
              <el-input v-model="form.performanceScore" placeholder="请输入绩效评分（0-100）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评分" prop="riskScore">
              <el-input v-model="form.riskScore" placeholder="请输入风险评分（0-100）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预警级别" prop="alertLevel">
              <el-select v-model="form.alertLevel" placeholder="请选择预警级别">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监控状态" prop="monitoringStatus">
              <el-select v-model="form.monitoringStatus" placeholder="请选择监控状态">
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
                <el-option label="完成" value="COMPLETED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预警信息" prop="alertMessage">
          <el-input v-model="form.alertMessage" placeholder="请输入预警信息" />
        </el-form-item>
        <el-form-item label="监控备注" prop="monitoringNotes">
          <el-input v-model="form.monitoringNotes" type="textarea" placeholder="请输入监控备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看投资监控详情对话框 -->
    <el-dialog title="投资监控详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投资ID">{{ viewForm.investmentId }}</el-descriptions-item>
        <el-descriptions-item label="监控日期">{{ parseTime(viewForm.monitoringDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="监控类型">{{ viewForm.monitoringType }}</el-descriptions-item>
        <el-descriptions-item label="当前价值">{{ parseFloat(viewForm.currentValue || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="前期价值">{{ parseFloat(viewForm.previousValue || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="价值变化">{{ parseFloat(viewForm.valueChange || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="变化率">{{ ((viewForm.valueChangeRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="绩效评分">{{ viewForm.performanceScore }}</el-descriptions-item>
        <el-descriptions-item label="风险评分">{{ viewForm.riskScore }}</el-descriptions-item>
        <el-descriptions-item label="预警级别">{{ viewForm.alertLevel }}</el-descriptions-item>
        <el-descriptions-item label="监控状态">{{ getMonitoringStatusText(viewForm.monitoringStatus) }}</el-descriptions-item>
        <el-descriptions-item label="预警信息">{{ viewForm.alertMessage }}</el-descriptions-item>
        <el-descriptions-item label="监控备注" :span="2">{{ viewForm.monitoringNotes }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { investmentMonitoringApi } from "@/api/globalTreasurer/tzlc";

export default {
  name: "InvestmentMonitoring",
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
      // 投资监控表格数据
      monitoringList: [],
      // 仪表盘数据
      dashboardData: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        investmentId: null,
        monitoringType: null,
        alertLevel: null,
        monitoringStatus: null,
        startDate: null,
        endDate: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 表单校验
      rules: {
        investmentId: [
          { required: true, message: "投资ID不能为空", trigger: "blur" }
        ],
        monitoringDate: [
          { required: true, message: "监控日期不能为空", trigger: "blur" }
        ],
        monitoringType: [
          { required: true, message: "监控类型不能为空", trigger: "change" }
        ],
        currentValue: [
          { required: true, message: "当前价值不能为空", trigger: "blur" }
        ],
        performanceScore: [
          { required: true, message: "绩效评分不能为空", trigger: "blur" }
        ],
        riskScore: [
          { required: true, message: "风险评分不能为空", trigger: "blur" }
        ],
        alertLevel: [
          { required: true, message: "预警级别不能为空", trigger: "change" }
        ],
        monitoringStatus: [
          { required: true, message: "监控状态不能为空", trigger: "change" }
        ]
      },
      // 监控类型选项
      monitoringTypeOptions: [
        { label: "日常监控", value: "DAILY" },
        { label: "周度监控", value: "WEEKLY" },
        { label: "月度监控", value: "MONTHLY" },
        { label: "季度监控", value: "QUARTERLY" },
        { label: "年度监控", value: "ANNUAL" }
      ]
    };
  },
  created() {
    this.getList();
    this.getDashboardData();
  },
  methods: {
    /** 查询投资监控列表 */
    getList() {
      this.loading = true;
      investmentMonitoringApi.getList(this.queryParams).then(response => {
        this.monitoringList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取仪表盘数据 */
    getDashboardData() {
      investmentMonitoringApi.getDashboard().then(response => {
        this.dashboardData = response.data;
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
        monitoringId: null,
        investmentId: null,
        monitoringDate: null,
        monitoringType: null,
        currentValue: null,
        performanceScore: null,
        riskScore: null,
        alertLevel: null,
        alertMessage: null,
        monitoringStatus: null,
        monitoringNotes: null
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
      this.ids = selection.map(item => item.monitoringId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加投资监控";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const monitoringId = row.monitoringId || this.ids;
      investmentMonitoringApi.getInfo(monitoringId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改投资监控";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      investmentMonitoringApi.getInfo(row.monitoringId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.monitoringId != null) {
            investmentMonitoringApi.update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            investmentMonitoringApi.add(this.form).then(response => {
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
      const monitoringIds = row.monitoringId || this.ids;
      this.$modal.confirm('是否确认删除投资监控编号为"' + monitoringIds + '"的数据项？').then(function() {
        return investmentMonitoringApi.delete(monitoringIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 生成监控记录 */
    handleGenerate() {
      this.$modal.confirm('是否确认生成今日的监控记录？').then(() => {
        return investmentMonitoringApi.generateRecords(new Date().toISOString().split('T')[0]);
      }).then(() => {
        this.getList();
        this.getDashboardData();
        this.$modal.msgSuccess("生成成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('investment/monitoring/export', {
        ...this.queryParams
      }, `monitoring_${new Date().getTime()}.xlsx`)
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case 'processAlert':
          this.handleProcessAlert(row);
          break;
        case 'updateRecord':
          this.handleUpdateRecord(row);
          break;
      }
    },
    /** 处理预警 */
    handleProcessAlert(row) {
      this.$prompt('请输入预警处理信息', '处理监控预警', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return investmentMonitoringApi.processAlert(row.monitoringId, row.alertLevel, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("预警处理成功");
      }).catch(() => {});
    },
    /** 更新记录 */
    handleUpdateRecord(row) {
      this.$prompt('请输入更新信息（格式：当前价值,绩效评分,风险评分）', '更新监控记录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        const [currentValue, performanceScore, riskScore] = value.split(',');
        const data = {
          currentValue: parseFloat(currentValue),
          performanceScore: parseInt(performanceScore),
          riskScore: parseInt(riskScore)
        };
        return investmentMonitoringApi.updateRecord(row.monitoringId, data);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("记录更新成功");
      }).catch(() => {});
    },
    /** 获取预警级别类型 */
    getAlertLevelType(level) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      };
      return levelMap[level] || 'info';
    },
    /** 获取监控状态类型 */
    getMonitoringStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'COMPLETED': 'info'
      };
      return statusMap[status] || 'info';
    },
    /** 获取监控状态文本 */
    getMonitoringStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'SUSPENDED': '暂停',
        'COMPLETED': '完成'
      };
      return statusMap[status] || status;
    },
    /** 获取绩效颜色 */
    getPerformanceColor(score) {
      if (score >= 80) return '#67c23a';
      if (score >= 60) return '#e6a23c';
      return '#f56c6c';
    },
    /** 获取风险颜色 */
    getRiskColor(score) {
      if (score <= 30) return '#67c23a';
      if (score <= 70) return '#e6a23c';
      return '#f56c6c';
    }
  }
};
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
.number {
  font-size: 24px;
  font-weight: bold;
}
.success {
  color: #67c23a;
}
.danger {
  color: #f56c6c;
}
.primary {
  color: #409eff;
}
.text-success {
  color: #67c23a;
}
.text-danger {
  color: #f56c6c;
}
</style>
