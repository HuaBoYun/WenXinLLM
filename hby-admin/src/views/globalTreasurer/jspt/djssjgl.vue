<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="业务类型" prop="businessType">
        <el-select v-model="queryParams.businessType" placeholder="请选择业务类型" clearable>
          <el-option label="现金收款" value="CASH_RECEIPT" />
          <el-option label="现金付款" value="CASH_PAYMENT" />
          <el-option label="现金转账" value="CASH_TRANSFER" />
          <el-option label="银行转账" value="BANK_TRANSFER" />
        </el-select>
      </el-form-item>
      <el-form-item label="结算状态" prop="settlementStatus">
        <el-select v-model="queryParams.settlementStatus" placeholder="请选择结算状态" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="已审批" value="APPROVED" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已结算" value="SETTLED" />
          <el-option label="失败" value="FAILED" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择优先级" clearable>
          <el-option label="紧急" value="URGENT" />
          <el-option label="高" value="HIGH" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="低" value="LOW" />
        </el-select>
      </el-form-item>
      <el-form-item label="币种" prop="currencyCode">
        <el-select v-model="queryParams.currencyCode" placeholder="请选择币种" clearable>
          <el-option label="人民币" value="CNY" />
          <el-option label="美元" value="USD" />
          <el-option label="欧元" value="EUR" />
          <el-option label="日元" value="JPY" />
        </el-select>
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['settlement:pending-data:add']"
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
          v-hasPermi="['settlement:pending-data:edit']"
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
          v-hasPermi="['settlement:pending-data:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleApprove"
          v-hasPermi="['settlement:pending-data:approve']"
        >批量审批</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleExport"
          v-hasPermi="['settlement:pending-data:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="pendingDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="业务编号" align="center" prop="businessNo" width="180" />
      <el-table-column label="业务类型" align="center" prop="businessType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.settlement_business_type" :value="scope.row.businessType"/>
        </template>
      </el-table-column>
      <el-table-column label="结算金额" align="center" prop="settlementAmount" width="120">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.settlementAmount).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="对方账户" align="center" prop="counterpartyAccount" width="180" />
      <el-table-column label="对方名称" align="center" prop="counterpartyName" width="150" />
      <el-table-column label="结算状态" align="center" prop="settlementStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.settlement_status" :value="scope.row.settlementStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.priority === 'URGENT'" type="danger">紧急</el-tag>
          <el-tag v-else-if="scope.row.priority === 'HIGH'" type="warning">高</el-tag>
          <el-tag v-else-if="scope.row.priority === 'MEDIUM'" type="info">中</el-tag>
          <el-tag v-else type="success">低</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预期结算日期" align="center" prop="expectedSettlementDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expectedSettlementDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审批状态" align="center" prop="approvalStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.approval_status" :value="scope.row.approvalStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['settlement:pending-data:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['settlement:pending-data:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleApprove(scope.row)"
            v-hasPermi="['settlement:pending-data:approve']"
            v-if="scope.row.settlementStatus === 'PENDING'"
          >审批</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['settlement:pending-data:remove']"
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

    <!-- 添加或修改待结算数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="业务编号" prop="businessNo">
              <el-input v-model="form.businessNo" placeholder="请输入业务编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务类型" prop="businessType">
              <el-select v-model="form.businessType" placeholder="请选择业务类型">
                <el-option label="现金收款" value="CASH_RECEIPT" />
                <el-option label="现金付款" value="CASH_PAYMENT" />
                <el-option label="现金转账" value="CASH_TRANSFER" />
                <el-option label="银行转账" value="BANK_TRANSFER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="结算金额" prop="settlementAmount">
              <el-input v-model="form.settlementAmount" placeholder="请输入结算金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="form.currencyCode" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="对方账户" prop="counterpartyAccount">
              <el-input v-model="form.counterpartyAccount" placeholder="请输入对方账户" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对方名称" prop="counterpartyName">
              <el-input v-model="form.counterpartyName" placeholder="请输入对方名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="对方银行" prop="counterpartyBank">
              <el-input v-model="form.counterpartyBank" placeholder="请输入对方银行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级">
                <el-option label="紧急" value="URGENT" />
                <el-option label="高" value="HIGH" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="低" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预期结算日期" prop="expectedSettlementDate">
              <el-date-picker
                v-model="form.expectedSettlementDate"
                type="date"
                placeholder="选择预期结算日期"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算方式" prop="settlementMethod">
              <el-select v-model="form.settlementMethod" placeholder="请选择结算方式">
                <el-option label="实时结算" value="REAL_TIME" />
                <el-option label="批量结算" value="BATCH" />
                <el-option label="定时结算" value="SCHEDULED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="业务描述" prop="businessDescription">
          <el-input v-model="form.businessDescription" type="textarea" placeholder="请输入业务描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="待结算数据详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="业务编号">{{ detailData.businessNo }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">{{ detailData.businessType }}</el-descriptions-item>
        <el-descriptions-item label="结算金额">{{ detailData.settlementAmount }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailData.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="对方账户">{{ detailData.counterpartyAccount }}</el-descriptions-item>
        <el-descriptions-item label="对方名称">{{ detailData.counterpartyName }}</el-descriptions-item>
        <el-descriptions-item label="对方银行">{{ detailData.counterpartyBank }}</el-descriptions-item>
        <el-descriptions-item label="结算状态">{{ detailData.settlementStatus }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ detailData.priority }}</el-descriptions-item>
        <el-descriptions-item label="预期结算日期">{{ detailData.expectedSettlementDate }}</el-descriptions-item>
        <el-descriptions-item label="实际结算日期">{{ detailData.actualSettlementDate }}</el-descriptions-item>
        <el-descriptions-item label="结算方式">{{ detailData.settlementMethod }}</el-descriptions-item>
        <el-descriptions-item label="结算费用">{{ detailData.settlementFee }}</el-descriptions-item>
        <el-descriptions-item label="银行交易号">{{ detailData.bankTransactionNo }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">{{ detailData.approvalStatus }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailData.approvalUser }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detailData.approvalTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="业务描述" :span="2">{{ detailData.businessDescription }}</el-descriptions-item>
        <el-descriptions-item label="审批备注" :span="2">{{ detailData.approvalNotes }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="审批待结算数据" :visible.sync="approvalOpen" width="600px" append-to-body>
      <el-form ref="approvalForm" :model="approvalForm" :rules="approvalRules" label-width="100px">
        <el-form-item label="审批结果" prop="approvalStatus">
          <el-radio-group v-model="approvalForm.approvalStatus">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批备注" prop="approvalNotes">
          <el-input v-model="approvalForm.approvalNotes" type="textarea" placeholder="请输入审批备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApproval">确 定</el-button>
        <el-button @click="cancelApproval">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getPendingDataPage, 
  getPendingDataById, 
  addPendingData, 
  updatePendingData, 
  deletePendingData,
  approvePendingData
} from "@/api/globalTreasurer/jspt";

export default {
  name: "PendingData",
  dicts: ['settlement_business_type', 'settlement_status', 'approval_status'],
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
      // 待结算数据表格数据
      pendingDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹出层
      detailOpen: false,
      // 审批弹出层
      approvalOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        businessType: null,
        settlementStatus: null,
        priority: null,
        currencyCode: null,
        accountId: null
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 审批表单
      approvalForm: {},
      // 表单校验
      rules: {
        businessNo: [
          { required: true, message: "业务编号不能为空", trigger: "blur" }
        ],
        businessType: [
          { required: true, message: "业务类型不能为空", trigger: "change" }
        ],
        settlementAmount: [
          { required: true, message: "结算金额不能为空", trigger: "blur" }
        ],
        currencyCode: [
          { required: true, message: "币种不能为空", trigger: "change" }
        ]
      },
      // 审批校验
      approvalRules: {
        approvalStatus: [
          { required: true, message: "审批结果不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询待结算数据列表 */
    getList() {
      this.loading = true;
      const params = this.addDateRange(this.queryParams, this.dateRange);
      getPendingDataPage(params).then(response => {
        this.pendingDataList = response.rows;
        this.total = response.total;
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
        pendingId: null,
        businessNo: null,
        businessType: null,
        settlementAmount: null,
        currencyCode: null,
        accountId: null,
        counterpartyAccount: null,
        counterpartyName: null,
        counterpartyBank: null,
        priority: "MEDIUM",
        expectedSettlementDate: null,
        settlementMethod: null,
        businessDescription: null
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.pendingId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加待结算数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const pendingId = row.pendingId || this.ids;
      getPendingDataById(pendingId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改待结算数据";
      });
    },
    /** 详情按钮操作 */
    handleView(row) {
      getPendingDataById(row.pendingId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 审批按钮操作 */
    handleApprove(row) {
      if (row && row.pendingId) {
        this.approvalForm = {
          pendingIds: [row.pendingId],
          approvalStatus: null,
          approvalNotes: null
        };
      } else {
        this.approvalForm = {
          pendingIds: this.ids,
          approvalStatus: null,
          approvalNotes: null
        };
      }
      this.approvalOpen = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pendingId != null) {
            updatePendingData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPendingData(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交审批 */
    submitApproval() {
      this.$refs["approvalForm"].validate(valid => {
        if (valid) {
          approvePendingData(this.approvalForm).then(response => {
            this.$modal.msgSuccess("审批成功");
            this.approvalOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 取消审批 */
    cancelApproval() {
      this.approvalOpen = false;
      this.approvalForm = {};
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const pendingIds = row.pendingId || this.ids;
      this.$modal.confirm('是否确认删除待结算数据编号为"' + pendingIds + '"的数据项？').then(function() {
        return deletePendingData(pendingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('settlement/pending-data/export', {
        ...this.queryParams
      }, `pending_data_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
