<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="投资编号" prop="investmentNo">
        <el-input
          v-model="queryParams.investmentNo"
          placeholder="请输入投资编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品代码" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入产品代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="银行代码" prop="bankCode">
        <el-input
          v-model="queryParams.bankCode"
          placeholder="请输入银行代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投资状态" prop="investmentStatus">
        <el-select v-model="queryParams.investmentStatus" placeholder="请选择投资状态" clearable>
          <el-option label="活跃" value="ACTIVE" />
          <el-option label="已到期" value="MATURED" />
          <el-option label="已赎回" value="REDEEMED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
      </el-form-item>
      <el-form-item label="投资日期" prop="startDate">
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
    <el-table v-loading="loading" :data="investmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投资编号" align="center" prop="investmentNo" />
      <el-table-column label="产品代码" align="center" prop="productCode" />
      <el-table-column label="银行代码" align="center" prop="bankCode" />
      <el-table-column label="投资金额" align="center" prop="investmentAmount">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.investmentAmount).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前价值" align="center" prop="currentValue">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.currentValue || 0).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预期收益率" align="center" prop="expectedReturnRate">
        <template slot-scope="scope">
          <span>{{ (scope.row.expectedReturnRate * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="实际收益率" align="center" prop="actualReturnRate">
        <template slot-scope="scope">
          <span>{{ ((scope.row.actualReturnRate || 0) * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="投资状态" align="center" prop="investmentStatus">
        <template slot-scope="scope">
          <el-tag :type="getInvestmentStatusType(scope.row.investmentStatus)">{{ getInvestmentStatusText(scope.row.investmentStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="投资日期" align="center" prop="investmentDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.investmentDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期日期" align="center" prop="maturityDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
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
              <el-dropdown-item command="subscribe" v-if="scope.row.investmentStatus === 'ACTIVE'">申购</el-dropdown-item>
              <el-dropdown-item command="redeem" v-if="scope.row.investmentStatus === 'ACTIVE'">赎回</el-dropdown-item>
              <el-dropdown-item command="updateValuation">更新估值</el-dropdown-item>
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

    <!-- 添加或修改银行理财投资对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资编号" prop="investmentNo">
              <el-input v-model="form.investmentNo" placeholder="请输入投资编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品代码" prop="productCode">
              <el-input v-model="form.productCode" placeholder="请输入产品代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行代码" prop="bankCode">
              <el-input v-model="form.bankCode" placeholder="请输入银行代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资金额" prop="investmentAmount">
              <el-input v-model="form.investmentAmount" placeholder="请输入投资金额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预期收益率" prop="expectedReturnRate">
              <el-input v-model="form.expectedReturnRate" placeholder="请输入预期收益率（如：0.05）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资日期" prop="investmentDate">
              <el-date-picker
                v-model="form.investmentDate"
                type="date"
                placeholder="选择投资日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="maturityDate">
              <el-date-picker
                v-model="form.maturityDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看银行理财投资详情对话框 -->
    <el-dialog title="银行理财投资详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投资编号">{{ viewForm.investmentNo }}</el-descriptions-item>
        <el-descriptions-item label="产品代码">{{ viewForm.productCode }}</el-descriptions-item>
        <el-descriptions-item label="银行代码">{{ viewForm.bankCode }}</el-descriptions-item>
        <el-descriptions-item label="投资金额">{{ parseFloat(viewForm.investmentAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="当前价值">{{ parseFloat(viewForm.currentValue || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="预期收益">{{ parseFloat(viewForm.expectedReturn || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="实际收益">{{ parseFloat(viewForm.actualReturn || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="预期收益率">{{ ((viewForm.expectedReturnRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="实际收益率">{{ ((viewForm.actualReturnRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="投资状态">{{ getInvestmentStatusText(viewForm.investmentStatus) }}</el-descriptions-item>
        <el-descriptions-item label="投资日期">{{ parseTime(viewForm.investmentDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(viewForm.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="赎回日期">{{ parseTime(viewForm.redeemDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="赎回金额">{{ parseFloat(viewForm.redeemAmount || 0).toLocaleString() }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 申购对话框 -->
    <el-dialog title="申购理财产品" :visible.sync="subscribeOpen" width="500px" append-to-body>
      <el-form ref="subscribeForm" :model="subscribeForm" :rules="subscribeRules" label-width="100px">
        <el-form-item label="产品代码">
          <el-input v-model="subscribeForm.productCode" disabled />
        </el-form-item>
        <el-form-item label="银行代码">
          <el-input v-model="subscribeForm.bankCode" disabled />
        </el-form-item>
        <el-form-item label="申购金额" prop="subscribeAmount">
          <el-input v-model="subscribeForm.subscribeAmount" placeholder="请输入申购金额" />
        </el-form-item>
        <el-form-item label="申购日期" prop="subscribeDate">
          <el-date-picker
            v-model="subscribeForm.subscribeDate"
            type="date"
            placeholder="选择申购日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSubscribeForm">确 定</el-button>
        <el-button @click="subscribeOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 赎回对话框 -->
    <el-dialog title="赎回理财产品" :visible.sync="redeemOpen" width="500px" append-to-body>
      <el-form ref="redeemForm" :model="redeemForm" :rules="redeemRules" label-width="100px">
        <el-form-item label="投资编号">
          <el-input v-model="redeemForm.investmentNo" disabled />
        </el-form-item>
        <el-form-item label="当前价值">
          <el-input v-model="redeemForm.currentValue" disabled />
        </el-form-item>
        <el-form-item label="赎回金额" prop="redeemAmount">
          <el-input v-model="redeemForm.redeemAmount" placeholder="请输入赎回金额" />
        </el-form-item>
        <el-form-item label="赎回日期" prop="redeemDate">
          <el-date-picker
            v-model="redeemForm.redeemDate"
            type="date"
            placeholder="选择赎回日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRedeemForm">确 定</el-button>
        <el-button @click="redeemOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { bankWealthInvestmentApi } from "@/api/globalTreasurer/tzlc";
import request from '@/utils/request';
import Pagination from '@/components/Pagination';

export default {
  name: "BankWealthInvestment",
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
      // 银行理财投资表格数据
      investmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 是否显示申购弹出层
      subscribeOpen: false,
      // 是否显示赎回弹出层
      redeemOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        investmentNo: null,
        productCode: null,
        bankCode: null,
        investmentStatus: null,
        startDate: null,
        endDate: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 申购表单参数
      subscribeForm: {},
      // 赎回表单参数
      redeemForm: {},
      // 表单校验
      rules: {
        investmentNo: [
          { required: true, message: "投资编号不能为空", trigger: "blur" }
        ],
        productCode: [
          { required: true, message: "产品代码不能为空", trigger: "blur" }
        ],
        bankCode: [
          { required: true, message: "银行代码不能为空", trigger: "blur" }
        ],
        investmentAmount: [
          { required: true, message: "投资金额不能为空", trigger: "blur" }
        ],
        expectedReturnRate: [
          { required: true, message: "预期收益率不能为空", trigger: "blur" }
        ],
        investmentDate: [
          { required: true, message: "投资日期不能为空", trigger: "blur" }
        ],
        maturityDate: [
          { required: true, message: "到期日期不能为空", trigger: "blur" }
        ]
      },
      // 申购校验
      subscribeRules: {
        subscribeAmount: [
          { required: true, message: "申购金额不能为空", trigger: "blur" }
        ],
        subscribeDate: [
          { required: true, message: "申购日期不能为空", trigger: "blur" }
        ]
      },
      // 赎回校验
      redeemRules: {
        redeemAmount: [
          { required: true, message: "赎回金额不能为空", trigger: "blur" }
        ],
        redeemDate: [
          { required: true, message: "赎回日期不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询银行理财投资列表 */
    getList() {
      this.loading = true;
      bankWealthInvestmentApi.getList(this.queryParams).then(response => {
        if (response && response.code === 1) {
          this.investmentList = response.data?.rows || response.data?.tlist || [];
          this.total = parseInt(response.data?.total) || response.data?.totalRecord || 0;
        } else {
          this.investmentList = [];
          this.total = 0;
          this.$message.error(response?.msg || '查询银行理财投资列表失败');
        }
        this.loading = false;
      }).catch(error => {
        console.error('查询银行理财投资列表失败:', error);
        this.$message.error('查询银行理财投资列表失败,请稍后重试');
        this.investmentList = [];
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
        investmentId: null,
        planId: null,
        productId: null,
        accountId: null,
        investmentNo: null,
        productCode: null,
        bankCode: null,
        investmentAmount: null,
        expectedReturnRate: null,
        investmentDate: null,
        maturityDate: null
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
      this.ids = selection.map(item => item.bankWealthInvestmentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加银行理财投资";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // 判断row是否是数据行对象(有bankWealthInvestmentId属性)还是事件对象
      const investmentId = row && row.bankWealthInvestmentId ? row.bankWealthInvestmentId : this.ids[0];
      if (!investmentId) {
        this.$message.error('请选择要修改的数据');
        return;
      }
      bankWealthInvestmentApi.getInfo(investmentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改银行理财投资";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      bankWealthInvestmentApi.getInfo(row.bankWealthInvestmentId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bankWealthInvestmentId != null) {
            bankWealthInvestmentApi.update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            bankWealthInvestmentApi.add(this.form).then(response => {
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
      // 判断row是否是数据行对象(有bankWealthInvestmentId属性)还是事件对象
      const investmentIds = row && row.bankWealthInvestmentId ? [row.bankWealthInvestmentId] : this.ids;

      if (!investmentIds || investmentIds.length === 0) {
        this.$message.error('请选择要删除的数据');
        return;
      }

      const confirmMsg = investmentIds.length === 1
        ? '是否确认删除该银行理财投资数据项？'
        : `是否确认删除选中的${investmentIds.length}条银行理财投资数据项？`;

      this.$modal.confirm(confirmMsg).then(() => {
        // 根据数量决定调用单个删除还是批量删除
        const deletePromises = investmentIds.map(id => {
          return bankWealthInvestmentApi.delete(id);
        });
        return Promise.all(deletePromises);
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
          url: '/qqsk/investment/bankwealth/export',  // ✅ 修改为全小写bankwealth
          params: this.queryParams,
          responseType: 'blob'
        });

        const blob = new Blob([response]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `银行理财列表_${new Date().getTime()}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);

        loading.close();
        this.$modal.msgSuccess("导出成功");
      } catch (error) {
        console.error('导出失败:', error);
        loading.close();
        this.$modal.msgError("导出功能开发中,敬请期待!");
      }
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case 'subscribe':
          this.handleSubscribe(row);
          break;
        case 'redeem':
          this.handleRedeem(row);
          break;
        case 'updateValuation':
          this.handleUpdateValuation(row);
          break;
      }
    },
    /** 申购理财产品 */
    handleSubscribe(row) {
      this.subscribeForm = {
        investmentId: row.bankWealthInvestmentId,
        productCode: row.productCode,
        bankCode: row.bankCode,
        subscribeAmount: null,
        subscribeDate: null
      };
      this.subscribeOpen = true;
    },
    /** 提交申购 */
    submitSubscribeForm() {
      this.$refs["subscribeForm"].validate(valid => {
        if (valid) {
          // 构建符合后端DTO的数据结构
          const subscribeData = {
            bankWealthInvestmentId: this.subscribeForm.investmentId,
            productCode: this.subscribeForm.productCode,
            bankCode: this.subscribeForm.bankCode,
            investmentAmount: this.subscribeForm.subscribeAmount,
            investmentDate: this.subscribeForm.subscribeDate
          };
          bankWealthInvestmentApi.subscribe(subscribeData).then(response => {
            this.$modal.msgSuccess("申购成功");
            this.subscribeOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 赎回理财产品 */
    handleRedeem(row) {
      this.redeemForm = {
        investmentId: row.bankWealthInvestmentId,
        investmentNo: row.investmentNo,
        currentValue: row.currentValue,
        redeemAmount: null,
        redeemDate: null
      };
      this.redeemOpen = true;
    },
    /** 提交赎回 */
    submitRedeemForm() {
      this.$refs["redeemForm"].validate(valid => {
        if (valid) {
          bankWealthInvestmentApi.redeem(
            this.redeemForm.investmentId,
            this.redeemForm.redeemDate,
            this.redeemForm.redeemAmount
          ).then(response => {
            this.$modal.msgSuccess("赎回成功");
            this.redeemOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 更新估值 */
    handleUpdateValuation(row) {
      this.$prompt('请输入新的净值', '更新投资估值', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+(\.\d+)?$/,
        inputErrorMessage: '净值格式不正确'
      }).then(({ value }) => {
        return bankWealthInvestmentApi.updateValuation(row.bankWealthInvestmentId, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("估值更新成功");
      }).catch(() => {});
    },
    /** 获取投资状态类型 */
    getInvestmentStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'MATURED': 'info',
        'REDEEMED': 'warning',
        'CANCELLED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    /** 获取投资状态文本 */
    getInvestmentStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'MATURED': '已到期',
        'REDEEMED': '已赎回',
        'CANCELLED': '已取消',
        'PENDING': '待处理'
      };
      return statusMap[status] || status;
    },
    /** 日期格式化 */
    parseTime(time, pattern) {
      if (!time) return '';

      let date;
      if (typeof time === 'object') {
        date = time;
      } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
          time = parseInt(time);
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
          time = time * 1000;
        }
        date = new Date(time);
      }

      const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
      };

      const time_str = pattern.replace(/{([ymdhisa])+}/g, (result, key) => {
        const value = formatObj[key];
        if (key === 'a') {
          return ['日', '一', '二', '三', '四', '五', '六'][value];
        }
        return value.toString().padStart(2, '0');
      });

      return time_str;
    }
  }
};
</script>
