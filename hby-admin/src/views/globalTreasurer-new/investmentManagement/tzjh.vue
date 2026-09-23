<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="计划编号" prop="planNo">
        <el-input
          v-model="queryParams.planNo"
          placeholder="请输入计划编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入计划名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划状态" prop="planStatus">
        <el-select v-model="queryParams.planStatus" placeholder="请选择计划状态" clearable>
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已提交" value="SUBMITTED" />
          <el-option label="已审批" value="APPROVED" />
          <el-option label="执行中" value="EXECUTING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
      </el-form-item>
      <el-form-item label="投资类型" prop="investmentType">
        <el-select v-model="queryParams.investmentType" placeholder="请选择投资类型" clearable>
          <el-option label="银行理财" value="BANK_WEALTH" />
          <el-option label="债券投资" value="BOND" />
          <el-option label="股权投资" value="EQUITY" />
          <el-option label="基金投资" value="FUND" />
          <el-option label="衍生品" value="DERIVATIVE" />
        </el-select>
      </el-form-item>
      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker
          v-model="queryParams.startDate"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker
          v-model="queryParams.endDate"
          type="date"
          placeholder="选择结束日期"
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
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="计划编号" align="center" prop="planNo" />
      <el-table-column label="计划名称" align="center" prop="planName" show-overflow-tooltip />
      <el-table-column label="投资类型" align="center" prop="investmentType">
        <template slot-scope="scope">
          <el-tag :type="getInvestmentTypeTagType(scope.row.investmentType)">
            {{ getInvestmentTypeText(scope.row.investmentType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计划金额" align="center" prop="planAmount">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.planAmount).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="已投资金额" align="center" prop="investedAmount">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.investedAmount || 0).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预期收益率" align="center" prop="expectedReturnRate">
        <template slot-scope="scope">
          <span>{{ (scope.row.expectedReturnRate * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" align="center" prop="riskLevel">
        <template slot-scope="scope">
          <el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="计划状态" align="center" prop="planStatus">
        <template slot-scope="scope">
          <el-tag :type="getPlanStatusType(scope.row.planStatus)">{{ getPlanStatusText(scope.row.planStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" align="center" prop="planStartDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planStartDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="planEndDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <span class="el-dropdown-link">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="submit" v-if="scope.row.planStatus === 'DRAFT'">提交</el-dropdown-item>
              <el-dropdown-item command="approve" v-if="scope.row.planStatus === 'SUBMITTED'">审批</el-dropdown-item>
              <el-dropdown-item command="execute" v-if="scope.row.planStatus === 'APPROVED'">执行</el-dropdown-item>
              <el-dropdown-item command="complete" v-if="scope.row.planStatus === 'EXECUTING'">完成</el-dropdown-item>
              <el-dropdown-item command="cancel" v-if="['DRAFT', 'SUBMITTED', 'APPROVED'].includes(scope.row.planStatus)">取消</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
      :page-sizes="[10, 20, 30, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 添加或修改投资计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划编号" prop="planNo">
              <el-input v-model="form.planNo" placeholder="请输入计划编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资类型" prop="investmentType">
              <el-select v-model="form.investmentType" placeholder="请选择投资类型">
                <el-option label="银行理财" value="BANK_WEALTH" />
                <el-option label="债券投资" value="BOND" />
                <el-option label="股权投资" value="EQUITY" />
                <el-option label="基金投资" value="FUND" />
                <el-option label="衍生品" value="DERIVATIVE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划金额" prop="planAmount">
              <el-input v-model="form.planAmount" placeholder="请输入计划金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预期收益率" prop="expectedReturnRate">
              <el-input v-model="form.expectedReturnRate" placeholder="请输入预期收益率（如：0.05）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="planStartDate">
              <el-date-picker
                v-model="form.planStartDate"
                type="date"
                placeholder="选择开始日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="planEndDate">
              <el-date-picker
                v-model="form.planEndDate"
                type="date"
                placeholder="选择结束日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="计划描述" prop="planDescription">
          <el-input v-model="form.planDescription" type="textarea" placeholder="请输入计划描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看投资计划详情对话框 -->
    <el-dialog title="投资计划详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="计划编号">{{ viewForm.planNo }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ viewForm.planName }}</el-descriptions-item>
        <el-descriptions-item label="投资类型">{{ viewForm.investmentType }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ viewForm.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="计划金额">{{ parseFloat(viewForm.planAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="已投资金额">{{ parseFloat(viewForm.investedAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="剩余金额">{{ parseFloat(viewForm.remainingAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="预期收益率">{{ ((viewForm.expectedReturnRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="实际收益率">{{ ((viewForm.actualReturnRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="计划状态">{{ getPlanStatusText(viewForm.planStatus) }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ parseTime(viewForm.planStartDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ parseTime(viewForm.planEndDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="计划描述" :span="2">{{ viewForm.planDescription }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { investmentPlanApi } from "@/api/globalTreasurer/tzlc";
import request from '@/utils/request';
import Pagination from '@/components/Pagination';

export default {
  name: "InvestmentPlan",
  components: {
    Pagination
  },
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
      // 投资计划表格数据
      planList: [],
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
        planNo: null,
        planName: null,
        planStatus: null,
        investmentType: null,
        startDate: null,
        endDate: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 表单校验
      rules: {
        planNo: [
          { required: true, message: "计划编号不能为空", trigger: "blur" }
        ],
        planName: [
          { required: true, message: "计划名称不能为空", trigger: "blur" }
        ],
        investmentType: [
          { required: true, message: "投资类型不能为空", trigger: "change" }
        ],
        planAmount: [
          { required: true, message: "计划金额不能为空", trigger: "blur" }
        ],
        expectedReturnRate: [
          { required: true, message: "预期收益率不能为空", trigger: "blur" }
        ],
        riskLevel: [
          { required: true, message: "风险等级不能为空", trigger: "change" }
        ],
        planStartDate: [
          { required: true, message: "开始日期不能为空", trigger: "blur" }
        ],
        planEndDate: [
          { required: true, message: "结束日期不能为空", trigger: "blur" }
        ]
      },
      // 投资类型选项
      investmentTypeOptions: [
        { label: "银行理财", value: "BANK_WEALTH" },
        { label: "债券投资", value: "BOND" },
        { label: "股权投资", value: "EQUITY" },
        { label: "基金投资", value: "FUND" },
        { label: "衍生品", value: "DERIVATIVE" }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询投资计划列表 */
    getList() {
      this.loading = true;
      investmentPlanApi.getList(this.queryParams).then(response => {
        if (response && response.code === 1) {
          this.planList = response.data?.rows || response.data?.tlist || [];
          this.total = parseInt(response.data?.total) || response.data?.totalRecord || 0;
        } else {
          this.planList = [];
          this.total = 0;
          this.$message.error(response?.msg || '查询投资计划列表失败');
        }
        this.loading = false;
      }).catch(error => {
        console.error('查询投资计划列表失败:', error);
        this.$message.error('查询投资计划列表失败,请稍后重试');
        this.planList = [];
        this.total = 0;
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
        planId: null,
        planNo: null,
        planName: null,
        planType: null,
        investmentType: null,
        planAmount: null,
        expectedReturnRate: null,
        riskLevel: null,
        planStartDate: null,
        planEndDate: null,
        planDescription: null
      };
      // 重置表单验证
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      // 重置查询表单
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields();
      }
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.planId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加投资计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // 判断row是否是数据行对象(有planId属性)还是事件对象
      let planId;
      if (row && row.planId) {
        // 从表格行点击的修改按钮,row是数据对象
        planId = row.planId;
      } else {
        // 从顶部工具栏点击的修改按钮,使用选中的第一条数据
        if (this.ids && this.ids.length > 0) {
          planId = this.ids[0];
        } else {
          this.$modal.msgError("请选择要修改的数据");
          return;
        }
      }
      investmentPlanApi.getInfo(planId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改投资计划";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      investmentPlanApi.getInfo(row.planId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.planId != null) {
            investmentPlanApi.update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            investmentPlanApi.add(this.form).then(response => {
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
      const planIds = row.planId || this.ids;
      this.$modal.confirm('是否确认删除投资计划编号为"' + planIds + '"的数据项？').then(function() {
        return investmentPlanApi.delete(planIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    async handleExport() {
      const loading = this.$baseLoading(undefined, "导出中...");
      try {
        const response = await request({
          method: 'get',
          url: '/qqsk/investment/plan/export',
          params: this.queryParams,
          responseType: 'blob'
        });

        const blob = new Blob([response]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `投资计划列表_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);

        loading.close();
        this.$modal.msgSuccess("导出成功");
      } catch (error) {
        console.error('导出失败:', error);
        loading.close();
        this.$modal.msgError("导出失败");
      }
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case 'submit':
          this.handleSubmit(row);
          break;
        case 'approve':
          this.handleApprove(row);
          break;
        case 'execute':
          this.handleExecute(row);
          break;
        case 'complete':
          this.handleComplete(row);
          break;
        case 'cancel':
          this.handleCancel(row);
          break;
      }
    },
    /** 提交计划 */
    handleSubmit(row) {
      this.$modal.confirm('是否确认提交该投资计划？').then(() => {
        return investmentPlanApi.submit(row.planId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("提交成功");
      }).catch(() => {});
    },
    /** 审批计划 */
    handleApprove(row) {
      this.$prompt('请输入审批意见', '审批投资计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return investmentPlanApi.approve(row.planId, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("审批成功");
      }).catch(() => {});
    },
    /** 执行计划 */
    handleExecute(row) {
      this.$modal.confirm('是否确认执行该投资计划？').then(() => {
        return investmentPlanApi.execute(row.planId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("执行成功");
      }).catch(() => {});
    },
    /** 完成计划 */
    handleComplete(row) {
      this.$prompt('请输入完成备注', '完成投资计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return investmentPlanApi.complete(row.planId, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("完成成功");
      }).catch(() => {});
    },
    /** 取消计划 */
    handleCancel(row) {
      this.$prompt('请输入取消原因', '取消投资计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return investmentPlanApi.cancel(row.planId, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("取消成功");
      }).catch(() => {});
    },
    /** 获取计划状态类型 */
    getPlanStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'success',
        'EXECUTING': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    /** 获取计划状态文本 */
    getPlanStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    },
    /** 获取风险等级类型 */
    getRiskLevelType(level) {
      const levelMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      };
      return levelMap[level] || 'info';
    },
    /** 获取投资类型文本 */
    getInvestmentTypeText(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股权投资',
        'FUND': '基金投资',
        'DERIVATIVE': '衍生品'
      };
      return typeMap[type] || type;
    },
    /** 获取投资类型标签类型 */
    getInvestmentTypeTagType(type) {
      const typeMap = {
        'BANK_WEALTH': 'success',
        'BOND': 'warning',
        'EQUITY': 'danger',
        'FUND': 'primary',
        'DERIVATIVE': 'info'
      };
      return typeMap[type] || '';
    }
  }
};
</script>
