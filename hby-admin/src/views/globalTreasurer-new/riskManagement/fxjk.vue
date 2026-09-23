<template>
  <div class="app-container">
    <!-- 概览卡片 -->
    <el-row :gutter="20" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon total">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="card-info">
              <div class="card-title">总监控数</div>
              <div class="card-value">{{ overviewData.totalMonitorings || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon normal">
              <i class="el-icon-success"></i>
            </div>
            <div class="card-info">
              <div class="card-title">正常状态</div>
              <div class="card-value">{{ overviewData.normalCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon warning">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-info">
              <div class="card-title">预警状态</div>
              <div class="card-value">{{ overviewData.warningCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="card-content">
            <div class="card-icon critical">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="card-info">
              <div class="card-title">严重风险</div>
              <div class="card-value">{{ overviewData.criticalCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="query-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="监控编号" prop="monitoringNo">
          <el-input v-model="queryParams.monitoringNo" placeholder="请输入监控编号" clearable style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="风险类型" prop="riskTypeId">
          <el-select v-model="queryParams.riskTypeId" placeholder="请选择风险类型" clearable style="width: 150px;">
            <el-option label="信用风险" value="1"></el-option>
            <el-option label="流动性风险" value="2"></el-option>
            <el-option label="市场风险" value="3"></el-option>
            <el-option label="操作风险" value="4"></el-option>
            <el-option label="合规风险" value="5"></el-option>
            <el-option label="集中度风险" value="6"></el-option>
            <el-option label="利率风险" value="7"></el-option>
            <el-option label="汇率风险" value="8"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险状态" prop="riskStatus">
          <el-select v-model="queryParams.riskStatus" placeholder="请选择风险状态" clearable style="width: 120px;">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="预警" value="WARNING"></el-option>
            <el-option label="严重" value="CRITICAL"></el-option>
            <el-option label="违约" value="BREACH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监控日期" prop="monitoringDateRange">
          <el-date-picker
            v-model="queryParams.monitoringDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px;">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card">
      <el-row>
        <el-col :span="12">
          <el-button type="primary" @click="handleAdd">新增监控</el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selection.length === 0">批量删除</el-button>
          <el-button type="warning" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" @close="resetForm">
      <el-form :model="formData" :rules="formRules" ref="monitoringForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="监控编号" prop="monitoringNo">
              <el-input v-model="formData.monitoringNo" placeholder="留空自动生成"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险类型" prop="riskTypeId">
              <el-select v-model="formData.riskTypeId" placeholder="请选择" style="width:100%" @change="onRiskTypeChange">
                <el-option v-for="t in riskTypeOptions" :key="t.value" :label="t.label" :value="t.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险状态" prop="riskStatus">
              <el-select v-model="formData.riskStatus" placeholder="请选择" style="width:100%">
                <el-option label="正常" value="NORMAL"></el-option>
                <el-option label="预警" value="WARNING"></el-option>
                <el-option label="严重" value="CRITICAL"></el-option>
                <el-option label="违约" value="BREACH"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前风险值" prop="currentRiskValue">
              <el-input-number v-model="formData.currentRiskValue" :precision="2" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值" prop="thresholdValue">
              <el-input-number v-model="formData.thresholdValue" :precision="2" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警值" prop="warningValue">
              <el-input-number v-model="formData.warningValue" :precision="2" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="临界值" prop="criticalValue">
              <el-input-number v-model="formData.criticalValue" :precision="2" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监控日期" prop="monitoringDate">
              <el-date-picker v-model="formData.monitoringDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次监控日期" prop="nextMonitoringDate">
              <el-date-picker v-model="formData.nextMonitoringDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="formData.remark" type="textarea" :rows="2"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="监控详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="监控编号">{{ detailData.monitoringNo }}</el-descriptions-item>
        <el-descriptions-item label="风险类型">{{ detailData.riskTypeName }}</el-descriptions-item>
        <el-descriptions-item label="风险状态">
          <el-tag :type="getRiskStatusTagType(detailData.riskStatus)" size="small">{{ getRiskStatusText(detailData.riskStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="当前风险值">{{ detailData.currentRiskValue }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ detailData.thresholdValue }}</el-descriptions-item>
        <el-descriptions-item label="预警值">{{ detailData.warningValue }}</el-descriptions-item>
        <el-descriptions-item label="临界值">{{ detailData.criticalValue }}</el-descriptions-item>
        <el-descriptions-item label="警报状态">
          <el-tag :type="detailData.alertTriggered ? 'danger' : 'success'" size="small">{{ detailData.alertTriggered ? '已触发' : '正常' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="警报消息" :span="2">{{ detailData.alertMessage }}</el-descriptions-item>
        <el-descriptions-item label="监控日期">{{ detailData.monitoringDate ? new Date(detailData.monitoringDate).toISOString().substring(0, 10) : '' }}</el-descriptions-item>
        <el-descriptions-item label="下次监控日期">{{ detailData.nextMonitoringDate ? new Date(detailData.nextMonitoringDate).toISOString().substring(0, 10) : '' }}</el-descriptions-item>
        <el-descriptions-item label="处理措施" :span="2">{{ detailData.actionTaken }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime ? new Date(detailData.createTime).toISOString().substring(0, 19).replace('T', ' ') : '' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        :data="monitoringList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="monitoringNo" label="监控编号" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="riskTypeName" label="风险类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeTagType(scope.row.riskTypeId)" size="small">
              {{ scope.row.riskTypeName || getRiskTypeText(scope.row.riskTypeId) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskStatus" label="风险状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskStatusTagType(scope.row.riskStatus)" size="small">
              {{ getRiskStatusText(scope.row.riskStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentRiskValue" label="当前风险值" width="120"></el-table-column>
        <el-table-column prop="thresholdValue" label="阈值" width="100"></el-table-column>
        <el-table-column prop="warningValue" label="预警值" width="100"></el-table-column>
        <el-table-column prop="criticalValue" label="临界值" width="100"></el-table-column>
        <el-table-column prop="alertTriggered" label="警报状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.alertTriggered ? 'danger' : 'success'" size="small">
              {{ scope.row.alertTriggered ? '已触发' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="monitoringDate" label="监控日期" width="120">
          <template slot-scope="scope">{{ scope.row.monitoringDate ? new Date(scope.row.monitoringDate).toISOString().substring(0, 10) : '' }}</template>
        </el-table-column>
        <el-table-column prop="nextMonitoringDate" label="下次监控日期" width="140">
          <template slot-scope="scope">{{ scope.row.nextMonitoringDate ? new Date(scope.row.nextMonitoringDate).toISOString().substring(0, 10) : '' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-dropdown @command="(command) => handleOperation(command, scope.row)">
              <el-button type="text" size="small">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="detail">查看详情</el-dropdown-item>
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="triggerAlert" v-if="!scope.row.alertTriggered">触发警报</el-dropdown-item>
                <el-dropdown-item command="handle">处理</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import {
  getRiskMonitoringPage,
  getRiskMonitoring,
  createRiskMonitoring,
  updateRiskMonitoring,
  deleteRiskMonitoring,
  batchDeleteRiskMonitorings,
  triggerRiskAlert,
  handleRiskMonitoring,
  getRiskMonitoringOverview,
  exportRiskMonitoring
} from "@/api/globalTreasurer/fxgl";

export default {
  name: "RiskMonitoring",
  data() {
    return {
      overviewData: { totalMonitorings: 0, normalCount: 0, warningCount: 0, criticalCount: 0 },
      queryParams: {
        pageNum: 1, pageSize: 10, monitoringNo: '', riskTypeId: null,
        riskStatus: '', monitoringDateRange: [], orgId: 1000
      },
      monitoringList: [],
      total: 0,
      loading: false,
      selection: [],
      // 对话框
      dialogVisible: false,
      dialogTitle: '新增监控',
      submitLoading: false,
      isEdit: false,
      formData: {
        recordId: null, monitoringNo: '', riskTypeId: null, riskTypeName: '',
        riskStatus: 'NORMAL', currentRiskValue: null, thresholdValue: null,
        warningValue: null, criticalValue: null, monitoringDate: null,
        nextMonitoringDate: null, remark: '', orgId: 1000
      },
      formRules: {
        riskTypeId: [{ required: true, message: '请选择风险类型', trigger: 'change' }],
        riskStatus: [{ required: true, message: '请选择风险状态', trigger: 'change' }],
        monitoringDate: [{ required: true, message: '请选择监控日期', trigger: 'change' }]
      },
      // 详情对话框
      detailVisible: false,
      detailData: null,
      // 风险类型选项
      riskTypeOptions: [
        { value: 1, label: '信用风险' }, { value: 2, label: '流动性风险' },
        { value: 3, label: '市场风险' }, { value: 4, label: '操作风险' },
        { value: 5, label: '合规风险' }, { value: 6, label: '集中度风险' },
        { value: 7, label: '利率风险' }, { value: 8, label: '汇率风险' }
      ]
    };
  },
  created() {
    this.applyRouteRiskTypeFilter();
    this.loadOverviewData();
    this.loadMonitoringList();
  },
  watch: {
    '$route.query.riskTypeId'(newVal, oldVal) {
      if (newVal === oldVal) {
        return;
      }
      this.applyRouteRiskTypeFilter();
      this.queryParams.pageNum = 1;
      this.loadMonitoringList();
    }
  },
  methods: {
    getRouteRiskTypeId() {
      const routeRiskTypeId = this.$route.query.riskTypeId;
      if (routeRiskTypeId === undefined || routeRiskTypeId === null || routeRiskTypeId === '') {
        return null;
      }
      const parsedRiskTypeId = Number(routeRiskTypeId);
      return Number.isNaN(parsedRiskTypeId) ? null : parsedRiskTypeId;
    },

    applyRouteRiskTypeFilter() {
      this.queryParams.riskTypeId = this.getRouteRiskTypeId();
    },

    /** 加载概览数据 */
    loadOverviewData() {
      getRiskMonitoringOverview(this.queryParams.orgId).then(response => {
        const d = (response && response.data) || {};
        this.overviewData = {
          totalMonitorings: d.total || 0,
          normalCount: d.normal || 0,
          warningCount: d.warning || 0,
          criticalCount: d.critical || 0
        };
      }).catch(() => {
        this.overviewData = { totalMonitorings: 0, normalCount: 0, warningCount: 0, criticalCount: 0 };
      });
    },

    /** 加载监控列表 */
    loadMonitoringList() {
      this.loading = true;
      const query = { ...this.queryParams };

      // 处理日期范围
      if (query.monitoringDateRange && query.monitoringDateRange.length === 2) {
        query.monitoringDateStart = query.monitoringDateRange[0];
        query.monitoringDateEnd = query.monitoringDateRange[1];
      }
      delete query.monitoringDateRange;

      getRiskMonitoringPage(query).then(response => {
        this.monitoringList = response.data || [];
        this.total = (response.result && response.result.total) || 0;
      }).catch(error => {
        console.error('加载监控列表失败:', error);
        this.monitoringList = [];
        this.total = 0;
        this.$message.error('加载监控列表失败');
      }).finally(() => {
        this.loading = false;
      });
    },

    /** 查询 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.loadMonitoringList();
    },

    /** 重置查询条件 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        monitoringNo: '',
        riskTypeId: this.getRouteRiskTypeId(),
        riskStatus: '',
        monitoringDateRange: [],
        orgId: 1000
      };
      this.loadMonitoringList();
    },

    /** 分页大小改变 */
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.loadMonitoringList();
    },

    /** 当前页改变 */
    handleCurrentChange(val) {
      this.queryParams.pageNum = val;
      this.loadMonitoringList();
    },

    /** 选择改变 */
    handleSelectionChange(selection) {
      this.selection = selection;
    },

    /** 新增监控 */
    handleAdd() {
      this.isEdit = false;
      this.dialogTitle = '新增监控';
      this.formData = {
        recordId: null, monitoringNo: '', riskTypeId: this.getRouteRiskTypeId(), riskTypeName: '',
        riskStatus: 'NORMAL', currentRiskValue: null, thresholdValue: null,
        warningValue: null, criticalValue: null, monitoringDate: null,
        nextMonitoringDate: null, remark: '', orgId: this.queryParams.orgId
      };
      this.dialogVisible = true;
    },

    /** 批量删除 */
    handleBatchDelete() {
      if (this.selection.length === 0) {
        this.$message.warning('请选择要删除的记录');
        return;
      }

      this.$confirm('确认删除选中的监控记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const recordIds = this.selection.map(item => item.recordId);
        batchDeleteRiskMonitorings(recordIds).then(() => {
          this.$message.success('删除成功');
          this.loadMonitoringList();
          this.loadOverviewData();
        }).catch(error => {
          console.error('批量删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    /** 导出数据 */
    handleExport() {
      let body = {};
      if (this.selection.length > 0) {
        body.ids = this.selection.map(item => item.recordId);
      } else {
        body = { ...this.queryParams };
        if (body.monitoringDateRange && body.monitoringDateRange.length === 2) {
          body.monitoringDateStart = body.monitoringDateRange[0];
          body.monitoringDateEnd = body.monitoringDateRange[1];
        }
        delete body.monitoringDateRange;
      }
      exportRiskMonitoring(body).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = '风险监控数据.xlsx';
        link.click();
        window.URL.revokeObjectURL(url);
        this.$message.success('导出成功');
      }).catch(() => {
        this.$message.error('导出失败');
      });
    },

    /** 操作处理 */
    handleOperation(command, row) {
      switch (command) {
        case 'detail':
          this.viewDetail(row);
          break;
        case 'edit':
          this.editMonitoring(row);
          break;
        case 'triggerAlert':
          this.triggerAlert(row);
          break;
        case 'handle':
          this.handleMonitoring(row);
          break;
        case 'delete':
          this.deleteMonitoring(row);
          break;
      }
    },

    /** 查看详情 */
    viewDetail(row) {
      getRiskMonitoring(row.recordId).then(response => {
        this.detailData = (response && response.data) || row;
        this.detailVisible = true;
      }).catch(() => {
        this.detailData = row;
        this.detailVisible = true;
      });
    },

    /** 编辑监控 */
    editMonitoring(row) {
      getRiskMonitoring(row.recordId).then(response => {
        this.isEdit = true;
        this.dialogTitle = '编辑监控';
        const d = Object.assign({}, (response && response.data) || row);
        this.formatEditDates(d);
        this.formData = d;
        this.dialogVisible = true;
      }).catch(() => {
        this.isEdit = true;
        this.dialogTitle = '编辑监控';
        const d = Object.assign({}, row);
        this.formatEditDates(d);
        this.formData = d;
        this.dialogVisible = true;
      });
    },

    formatEditDates(d) {
      if (d.monitoringDate && typeof d.monitoringDate === 'number') {
        d.monitoringDate = new Date(d.monitoringDate).toISOString().substring(0, 10);
      }
      if (d.nextMonitoringDate && typeof d.nextMonitoringDate === 'number') {
        d.nextMonitoringDate = new Date(d.nextMonitoringDate).toISOString().substring(0, 10);
      }
      if (d.riskTypeId != null) {
        d.riskTypeId = Number(d.riskTypeId);
      }
    },

    /** 提交表单 */
    submitForm() {
      this.$refs.monitoringForm.validate(valid => {
        if (!valid) return;
        this.submitLoading = true;
        const api = this.isEdit ? updateRiskMonitoring : createRiskMonitoring;
        api(this.formData).then(() => {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功');
          this.dialogVisible = false;
          this.loadMonitoringList();
          this.loadOverviewData();
        }).catch(err => {
          this.$message.error((this.isEdit ? '更新' : '创建') + '失败');
        }).finally(() => {
          this.submitLoading = false;
        });
      });
    },

    /** 重置表单 */
    resetForm() {
      this.$refs.monitoringForm && this.$refs.monitoringForm.resetFields();
    },

    /** 风险类型变化 */
    onRiskTypeChange(val) {
      const opt = this.riskTypeOptions.find(o => o.value === val);
      this.formData.riskTypeName = opt ? opt.label : '';
    },

    /** 触发警报 */
    triggerAlert(row) {
      this.$prompt('请输入警报消息', '触发警报', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '警报消息不能为空'
      }).then(({ value }) => {
        triggerRiskAlert(row.recordId, value, 1).then(() => {
          this.$message.success('触发警报成功');
          this.loadMonitoringList();
          this.loadOverviewData();
        }).catch(error => {
          console.error('触发警报失败:', error);
          this.$message.error('触发警报失败');
        });
      });
    },

    /** 处理监控 */
    handleMonitoring(row) {
      this.$prompt('请输入处理措施', '处理监控', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '处理措施不能为空'
      }).then(({ value }) => {
        handleRiskMonitoring(row.recordId, value, 1).then(() => {
          this.$message.success('处理成功');
          this.loadMonitoringList();
          this.loadOverviewData();
        }).catch(error => {
          console.error('处理失败:', error);
          this.$message.error('处理失败');
        });
      });
    },

    /** 删除监控 */
    deleteMonitoring(row) {
      this.$confirm(`确认删除监控记录"${row.monitoringNo}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRiskMonitoring(row.recordId).then(() => {
          this.$message.success('删除成功');
          this.loadMonitoringList();
          this.loadOverviewData();
        }).catch(error => {
          console.error('删除失败:', error);
          this.$message.error('删除失败');
        });
      });
    },

    // 辅助方法
    getRiskTypeText(riskTypeId) {
      const typeMap = {
        1: '信用风险',
        2: '流动性风险',
        3: '市场风险',
        4: '操作风险',
        5: '合规风险',
        6: '集中度风险',
        7: '利率风险',
        8: '汇率风险'
      };
      return typeMap[riskTypeId] || '未知';
    },

    getRiskTypeTagType(riskTypeId) {
      const typeMap = {
        1: 'danger',    // 信用风险
        2: 'warning',   // 流动性风险
        3: 'primary',   // 市场风险
        4: 'info',      // 操作风险
        5: 'success',   // 合规风险
        6: 'warning',   // 集中度风险
        7: 'primary',   // 利率风险
        8: 'danger'     // 汇率风险
      };
      return typeMap[riskTypeId] || '';
    },

    getRiskStatusText(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'CRITICAL': '严重',
        'BREACH': '违约'
      };
      return statusMap[status] || status;
    },

    getRiskStatusTagType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger',
        'BREACH': 'danger'
      };
      return statusMap[status] || '';
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

/* 概览卡片样式 */
.overview-cards {
  margin-bottom: 20px;
}

.overview-card {
  height: 100px;
}

.card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.card-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-icon.normal {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-icon.warning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-icon.critical {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

/* 卡片间距 */
.query-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

/* 分页样式 */
.pagination-container {
  margin-top: 20px;
  text-align: right;
}

/* 表格样式优化 */
.el-table {
  font-size: 14px;
}

.el-table .el-table__header th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .overview-cards .el-col {
    margin-bottom: 20px;
  }

  .card-content {
    flex-direction: column;
    text-align: center;
  }

  .card-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }
}
</style>
