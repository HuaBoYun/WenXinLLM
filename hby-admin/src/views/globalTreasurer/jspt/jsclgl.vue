<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="处理类型" prop="processType">
        <el-select v-model="queryParams.processType" placeholder="请选择处理类型" clearable>
          <el-option label="实时处理" value="REAL_TIME" />
          <el-option label="批量处理" value="BATCH" />
          <el-option label="定时处理" value="SCHEDULED" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="processStatus">
        <el-select v-model="queryParams.processStatus" placeholder="请选择处理状态" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="成功" value="SUCCESS" />
          <el-option label="失败" value="FAILED" />
          <el-option label="超时" value="TIMEOUT" />
        </el-select>
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
        <el-input
          v-model="queryParams.batchNo"
          placeholder="请输入批次号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          :disabled="multiple"
          @click="handleRetry"
          v-hasPermi="['settlement:processing-record:retry']"
        >重试处理</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleExport"
          v-hasPermi="['settlement:processing-record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="recordId" width="100" />
      <el-table-column label="待结算数据ID" align="center" prop="pendingId" width="120" />
      <el-table-column label="批次号" align="center" prop="batchNo" width="180" />
      <el-table-column label="处理类型" align="center" prop="processType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.process_type" :value="scope.row.processType"/>
        </template>
      </el-table-column>
      <el-table-column label="处理状态" align="center" prop="processStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.processStatus === 'SUCCESS'" type="success">成功</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'PROCESSING'" type="warning">处理中</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'FAILED'" type="danger">失败</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'TIMEOUT'" type="info">超时</el-tag>
          <el-tag v-else type="info">待处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理时长(分钟)" align="center" prop="processingDuration" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.processingDuration">{{ scope.row.processingDuration }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="银行交易号" align="center" prop="bankTransactionNo" width="180" />
      <el-table-column label="重试次数" align="center" prop="retryCount" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.retryCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="错误信息" align="center" prop="errorMessage" width="200" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['settlement:processing-record:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleRetry(scope.row)"
            v-hasPermi="['settlement:processing-record:retry']"
            v-if="scope.row.processStatus === 'FAILED'"
          >重试</el-button>
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

    <!-- 详情对话框 -->
    <el-dialog title="处理记录详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="记录ID">{{ detailData.recordId }}</el-descriptions-item>
        <el-descriptions-item label="待结算数据ID">{{ detailData.pendingId }}</el-descriptions-item>
        <el-descriptions-item label="批次号">{{ detailData.batchNo }}</el-descriptions-item>
        <el-descriptions-item label="处理类型">{{ detailData.processType }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">{{ detailData.processStatus }}</el-descriptions-item>
        <el-descriptions-item label="银行接口ID">{{ detailData.bankInterfaceId }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detailData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detailData.endTime }}</el-descriptions-item>
        <el-descriptions-item label="处理时长">{{ detailData.processingDuration }}分钟</el-descriptions-item>
        <el-descriptions-item label="银行交易号">{{ detailData.bankTransactionNo }}</el-descriptions-item>
        <el-descriptions-item label="重试次数">{{ detailData.retryCount }}</el-descriptions-item>
        <el-descriptions-item label="最大重试次数">{{ detailData.maxRetryCount }}</el-descriptions-item>
        <el-descriptions-item label="下次重试时间">{{ detailData.nextRetryTime }}</el-descriptions-item>
        <el-descriptions-item label="错误代码">{{ detailData.errorCode }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2">{{ detailData.errorMessage }}</el-descriptions-item>
      </el-descriptions>
      
      <!-- 银行请求响应信息 -->
      <el-divider content-position="left">银行接口交互信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>银行请求</h4>
          <el-input
            v-model="detailData.bankRequest"
            type="textarea"
            :rows="10"
            readonly
            placeholder="无请求数据"
          />
        </el-col>
        <el-col :span="12">
          <h4>银行响应</h4>
          <el-input
            v-model="detailData.bankResponse"
            type="textarea"
            :rows="10"
            readonly
            placeholder="无响应数据"
          />
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getProcessingRecordPage, 
  getProcessingRecordById,
  retryProcessing
} from "@/api/globalTreasurer/jspt";

export default {
  name: "ProcessingRecord",
  dicts: ['process_type'],
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
      // 处理记录表格数据
      recordList: [],
      // 详情弹出层
      detailOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processType: null,
        processStatus: null,
        batchNo: null
      },
      // 详情数据
      detailData: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询处理记录列表 */
    getList() {
      this.loading = true;
      const params = this.addDateRange(this.queryParams, this.dateRange);
      getProcessingRecordPage(params).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.recordId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 详情按钮操作 */
    handleView(row) {
      getProcessingRecordById(row.recordId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 重试按钮操作 */
    handleRetry(row) {
      const recordIds = row && row.recordId ? [row.recordId] : this.ids;
      this.$modal.confirm('是否确认重试选中的处理记录？').then(() => {
        const promises = recordIds.map(id => retryProcessing(id));
        return Promise.all(promises);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("重试操作已提交");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('settlement/processing-record/export', {
        ...this.queryParams
      }, `processing_record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
