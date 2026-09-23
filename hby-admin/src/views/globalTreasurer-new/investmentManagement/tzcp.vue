<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品代码" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入产品代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品类型" prop="productType">
        <el-select v-model="queryParams.productType" placeholder="请选择产品类型" clearable>
          <el-option label="银行理财" value="BANK_WEALTH" />
          <el-option label="债券投资" value="BOND" />
          <el-option label="股权投资" value="EQUITY" />
          <el-option label="基金投资" value="FUND" />
          <el-option label="衍生品" value="DERIVATIVE" />
        </el-select>
      </el-form-item>
      <el-form-item label="发行机构" prop="issuer">
        <el-input
          v-model="queryParams.issuer"
          placeholder="请输入发行机构"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品状态" prop="productStatus">
        <el-select v-model="queryParams.productStatus" placeholder="请选择产品状态" clearable>
          <el-option label="活跃" value="ACTIVE" />
          <el-option label="暂停" value="SUSPENDED" />
          <el-option label="到期" value="MATURED" />
          <el-option label="终止" value="TERMINATED" />
        </el-select>
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
    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品代码" align="center" prop="productCode" />
      <el-table-column label="产品名称" align="center" prop="productName" show-overflow-tooltip />
      <el-table-column label="产品类型" align="center" prop="productType">
        <template slot-scope="scope">
          <el-tag :type="getProductTypeTagType(scope.row.productType)">
            {{ getProductTypeText(scope.row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发行机构" align="center" prop="issuer" show-overflow-tooltip />
      <el-table-column label="风险等级" align="center" prop="riskLevel">
        <template slot-scope="scope">
          <el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预期收益率" align="center" prop="expectedReturnRate">
        <template slot-scope="scope">
          <span>{{ (scope.row.expectedReturnRate * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="最小投资金额" align="center" prop="minInvestmentAmount">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.minInvestmentAmount).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="净值" align="center" prop="netValue">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.netValue || 1).toFixed(4) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品状态" align="center" prop="productStatus">
        <template slot-scope="scope">
          <el-tag :type="getProductStatusType(scope.row.productStatus)">{{ getProductStatusText(scope.row.productStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发行日期" align="center" prop="launchDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.launchDate, '{y}-{m}-{d}') }}</span>
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
              <el-dropdown-item command="launch" v-if="scope.row.productStatus === 'SUSPENDED'">上架</el-dropdown-item>
              <el-dropdown-item command="suspend" v-if="scope.row.productStatus === 'ACTIVE'">下架</el-dropdown-item>
              <el-dropdown-item command="updateNav">更新净值</el-dropdown-item>
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

    <!-- 添加或修改投资产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品代码" prop="productCode">
              <el-input v-model="form.productCode" placeholder="请输入产品代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品类型" prop="productType">
              <el-select v-model="form.productType" placeholder="请选择产品类型">
                <el-option label="银行理财" value="BANK_WEALTH" />
                <el-option label="债券投资" value="BOND" />
                <el-option label="股权投资" value="EQUITY" />
                <el-option label="基金投资" value="FUND" />
                <el-option label="衍生品" value="DERIVATIVE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发行机构" prop="issuer">
              <el-input v-model="form.issuer" placeholder="请输入发行机构" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="LOW" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="高风险" value="HIGH" />
              </el-select>
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
            <el-form-item label="最小投资金额" prop="minInvestmentAmount">
              <el-input v-model="form.minInvestmentAmount" placeholder="请输入最小投资金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大投资金额" prop="maxInvestmentAmount">
              <el-input v-model="form.maxInvestmentAmount" placeholder="请输入最大投资金额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资期限" prop="investmentTerm">
              <el-input v-model="form.investmentTerm" placeholder="请输入投资期限（天）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="净值" prop="netValue">
              <el-input v-model="form.netValue" placeholder="请输入净值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发行日期" prop="launchDate">
              <el-date-picker
                v-model="form.launchDate"
                type="date"
                placeholder="选择发行日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
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
        <el-row>
          <el-col :span="12">
            <el-form-item label="申购开始日期" prop="subscriptionStartDate">
              <el-date-picker
                v-model="form.subscriptionStartDate"
                type="date"
                placeholder="选择申购开始日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申购结束日期" prop="subscriptionEndDate">
              <el-date-picker
                v-model="form.subscriptionEndDate"
                type="date"
                placeholder="选择申购结束日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="允许赎回" prop="redemptionAllowed">
              <el-radio-group v-model="form.redemptionAllowed">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="产品描述" prop="productDescription">
          <el-input v-model="form.productDescription" type="textarea" placeholder="请输入产品描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看投资产品详情对话框 -->
    <el-dialog title="投资产品详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="产品代码">{{ viewForm.productCode }}</el-descriptions-item>
        <el-descriptions-item label="产品名称">{{ viewForm.productName }}</el-descriptions-item>
        <el-descriptions-item label="产品类型">{{ viewForm.productType }}</el-descriptions-item>
        <el-descriptions-item label="发行机构">{{ viewForm.issuer }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ viewForm.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="预期收益率">{{ ((viewForm.expectedReturnRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="最小投资金额">{{ parseFloat(viewForm.minInvestmentAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="最大投资金额">{{ parseFloat(viewForm.maxInvestmentAmount || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="投资期限">{{ viewForm.investmentTerm }}天</el-descriptions-item>
        <el-descriptions-item label="净值">{{ parseFloat(viewForm.netValue || 1).toFixed(4) }}</el-descriptions-item>
        <el-descriptions-item label="发行日期">{{ parseTime(viewForm.launchDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(viewForm.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="申购开始日期">{{ parseTime(viewForm.subscriptionStartDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="申购结束日期">{{ parseTime(viewForm.subscriptionEndDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="允许赎回">{{ viewForm.redemptionAllowed ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="产品状态">{{ getProductStatusText(viewForm.productStatus) }}</el-descriptions-item>
        <el-descriptions-item label="产品描述" :span="2">{{ viewForm.productDescription }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 更新净值对话框 -->
    <el-dialog title="更新产品净值" :visible.sync="navOpen" width="400px" append-to-body>
      <el-form ref="navForm" :model="navForm" :rules="navRules" label-width="100px">
        <el-form-item label="产品代码">
          <el-input v-model="navForm.productCode" disabled />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="navForm.productName" disabled />
        </el-form-item>
        <el-form-item label="当前净值">
          <el-input v-model="navForm.currentNetValue" disabled />
        </el-form-item>
        <el-form-item label="新净值" prop="newNetValue">
          <el-input v-model="navForm.newNetValue" placeholder="请输入新净值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitNavForm">确 定</el-button>
        <el-button @click="navOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { investmentProductApi } from "@/api/globalTreasurer/tzlc";
import request from '@/utils/request';
import Pagination from '@/components/Pagination';

export default {
  name: "InvestmentProduct",
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
      // 投资产品表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 是否显示净值更新弹出层
      navOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productCode: null,
        productName: null,
        productType: null,
        issuer: null,
        productStatus: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 净值更新表单参数
      navForm: {},
      // 表单校验
      rules: {
        productCode: [
          { required: true, message: "产品代码不能为空", trigger: "blur" }
        ],
        productName: [
          { required: true, message: "产品名称不能为空", trigger: "blur" }
        ],
        productType: [
          { required: true, message: "产品类型不能为空", trigger: "change" }
        ],
        issuer: [
          { required: true, message: "发行机构不能为空", trigger: "blur" }
        ],
        expectedReturnRate: [
          { required: true, message: "预期收益率不能为空", trigger: "blur" }
        ],
        riskLevel: [
          { required: true, message: "风险等级不能为空", trigger: "change" }
        ],
        minInvestmentAmount: [
          { required: true, message: "最小投资金额不能为空", trigger: "blur" }
        ],
        launchDate: [
          { required: true, message: "发行日期不能为空", trigger: "blur" }
        ]
      },
      // 净值更新校验
      navRules: {
        newNetValue: [
          { required: true, message: "新净值不能为空", trigger: "blur" }
        ]
      },
      // 产品类型选项
      productTypeOptions: [
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
    /** 查询投资产品列表 */
    async getList() {
      this.loading = true;
      try {
        const response = await investmentProductApi.getList(this.queryParams);
        if (response && response.code === 1) {
          // 处理响应数据: response.data.rows 或 response.data.tlist
          this.productList = response.data?.rows || response.data?.tlist || response.data || [];
          this.total = parseInt(response.data?.total) || response.data?.totalRecord || this.productList.length;
        } else {
          throw new Error('API返回数据格式异常');
        }
      } catch (error) {
        console.error('查询投资产品列表失败:', error);
        this.$message.error('查询投资产品列表失败,请稍后重试');
        this.productList = [];
        this.total = 0;
      } finally {
        this.loading = false;
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        productId: null,
        productCode: null,
        productName: null,
        productType: null,
        issuer: null,
        riskLevel: null,
        expectedReturnRate: null,
        minInvestmentAmount: null,
        maxInvestmentAmount: null,
        investmentTerm: null,
        netValue: 1.0000,
        launchDate: null,
        maturityDate: null,
        subscriptionStartDate: null,
        subscriptionEndDate: null,
        redemptionAllowed: 1,
        productDescription: null
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
      this.ids = selection.map(item => item.productId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加投资产品";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log('=== handleUpdate 调试信息 ===');
      console.log('row:', row);
      console.log('this.ids:', this.ids);
      console.log('this.ids.length:', this.ids.length);
      
      this.reset();
      // 获取产品ID: 判断row.productId是否存在(区分数据对象和事件对象)
      const productId = (row && row.productId) ? row.productId : (this.ids.length > 0 ? this.ids[0] : null);
      console.log('最终的 productId:', productId);
      
      if (!productId) {
        this.$modal.msgError("请选择要修改的产品");
        return;
      }
      investmentProductApi.getInfo(productId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改投资产品";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      investmentProductApi.getInfo(row.productId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 调试日志: 查看提交的表单数据
          console.log('=== 提交的表单数据 ===');
          console.log('完整表单对象:', this.form);
          console.log('JSON格式:', JSON.stringify(this.form, null, 2));
          
          // 转换数据类型以匹配后端
          const submitData = {
            ...this.form,
            redemptionAllowed: this.form.redemptionAllowed ? 1 : 0,
            expectedReturnRate: parseFloat(this.form.expectedReturnRate),
            minInvestmentAmount: parseFloat(this.form.minInvestmentAmount),
            maxInvestmentAmount: this.form.maxInvestmentAmount ? parseFloat(this.form.maxInvestmentAmount) : null,
            investmentTerm: this.form.investmentTerm ? parseInt(this.form.investmentTerm) : null,
            netValue: parseFloat(this.form.netValue)
          };
          
          console.log('转换后的提交数据:', JSON.stringify(submitData, null, 2));
          
          if (this.form.productId != null) {
            investmentProductApi.update(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            investmentProductApi.add(submitData).then(response => {
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
      const productIds = row.productId || this.ids;
      this.$modal.confirm('是否确认删除投资产品编号为"' + productIds + '"的数据项？').then(function() {
        return investmentProductApi.delete(productIds);
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
          url: '/qqsk/investment/product/export',
          params: this.queryParams,
          responseType: 'blob'
        });

        const blob = new Blob([response]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `投资产品列表_${new Date().getTime()}.xlsx`;
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
        case 'launch':
          this.handleLaunch(row);
          break;
        case 'suspend':
          this.handleSuspend(row);
          break;
        case 'updateNav':
          this.handleUpdateNav(row);
          break;
      }
    },
    /** 上架产品 */
    handleLaunch(row) {
      this.$modal.confirm('是否确认上架该投资产品？').then(() => {
        return investmentProductApi.launch(row.productId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("上架成功");
      }).catch(() => {});
    },
    /** 下架产品 */
    handleSuspend(row) {
      this.$prompt('请输入下架原因', '下架投资产品', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return investmentProductApi.suspend(row.productId, value);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("下架成功");
      }).catch(() => {});
    },
    /** 更新净值 */
    handleUpdateNav(row) {
      // 从后端获取最新的产品信息,确保当前净值是最新的
      investmentProductApi.getInfo(row.productId).then(response => {
        const product = response.data;
        this.navForm = {
          productId: product.productId,
          productCode: product.productCode,
          productName: product.productName,
          currentNetValue: product.netValue,
          newNetValue: null
        };
        this.navOpen = true;
      }).catch(() => {
        this.$modal.msgError("获取产品信息失败");
      });
    },
    /** 提交净值更新 */
    submitNavForm() {
      this.$refs["navForm"].validate(valid => {
        if (valid) {
          console.log('=== 提交净值更新 ===');
          console.log('productId:', this.navForm.productId);
          console.log('当前净值:', this.navForm.currentNetValue);
          console.log('新净值:', this.navForm.newNetValue);
          
          investmentProductApi.updateNav(this.navForm.productId, this.navForm.newNetValue).then(response => {
            console.log('=== 更新成功响应 ===');
            console.log('response:', response);
            
            this.$modal.msgSuccess("净值更新成功");
            this.navOpen = false;
            this.getList();
          }).catch(error => {
            console.log('=== 更新失败 ===');
            console.log('error:', error);
          });
        }
      });
    },
    /** 获取产品状态类型 */
    getProductStatusType(status) {
      const statusMap = {
        'ACTIVE': 'success',
        'SUSPENDED': 'warning',
        'MATURED': 'info',
        'TERMINATED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    /** 获取产品状态文本 */
    getProductStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'SUSPENDED': '暂停',
        'MATURED': '到期',
        'TERMINATED': '终止'
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
    /** 获取产品类型文本 */
    getProductTypeText(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股权投资',
        'FUND': '基金投资',
        'DERIVATIVE': '衍生品'
      };
      return typeMap[type] || type;
    },
    /** 获取产品类型标签类型 */
    getProductTypeTagType(type) {
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
