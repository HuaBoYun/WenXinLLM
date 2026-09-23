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
          v-hasPermi="['investment:product:add']"
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
          v-hasPermi="['investment:product:edit']"
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
          v-hasPermi="['investment:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['investment:product:export']"
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
          <dict-tag :options="productTypeOptions" :value="scope.row.productType"/>
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
            v-hasPermi="['investment:product:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['investment:product:edit']"
          >修改</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['investment:product:launch']">
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
            v-hasPermi="['investment:product:remove']"
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

export default {
  name: "InvestmentProduct",
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
        if (response && (response.rows || response.data)) {
          this.productList = response.rows || response.data?.tlist || response.data || [];
          this.total = response.total || response.data?.totalRecord || this.productList.length;
        } else {
          throw new Error('API返回数据格式异常');
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('投资产品API调用失败，使用模拟数据:', error);
        this.productList = this.generateMockProductData();
        this.total = this.productList.length;

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        });
      } finally {
        this.loading = false;
      }
    },
    /** 生成模拟投资产品数据 */
    generateMockProductData() {
      const productTypes = ['BANK_WEALTH', 'BOND', 'FUND', 'TRUST', 'INSURANCE'];
      const riskLevels = ['R1', 'R2', 'R3', 'R4', 'R5'];
      const currencies = ['CNY', 'USD', 'EUR'];
      const statuses = ['AVAILABLE', 'SOLD_OUT', 'SUSPENDED'];
      const issuers = ['工商银行', '建设银行', '招商银行', '中信银行', '平安银行'];

      return Array.from({ length: 8 }, (_, index) => ({
        productId: index + 1,
        productCode: `PRD${String(Date.now() + index).slice(-6)}`,
        productName: `投资产品${index + 1}`,
        productType: productTypes[Math.floor(Math.random() * productTypes.length)],
        issuer: issuers[Math.floor(Math.random() * issuers.length)],
        riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
        expectedReturnRate: (Math.random() * 8 + 2).toFixed(2),
        minInvestmentAmount: Math.floor(Math.random() * 900000) + 100000,
        maxInvestmentAmount: Math.floor(Math.random() * 9000000) + 1000000,
        currencyCode: currencies[Math.floor(Math.random() * currencies.length)],
        investmentTerm: Math.floor(Math.random() * 360) + 30,
        termUnit: 'DAY',
        status: statuses[Math.floor(Math.random() * statuses.length)],
        saleStartDate: new Date(Date.now() - Math.floor(Math.random() * 30) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        saleEndDate: new Date(Date.now() + Math.floor(Math.random() * 60) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        valueDate: new Date(Date.now() + Math.floor(Math.random() * 7) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        maturityDate: new Date(Date.now() + Math.floor(Math.random() * 365 + 30) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
        description: `这是投资产品${index + 1}的详细描述，包含产品特点和投资策略。`,
        createTime: new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000)).toISOString().replace('T', ' ').split('.')[0],
        updateTime: new Date(Date.now() - Math.floor(Math.random() * 7 * 24 * 60 * 60 * 1000)).toISOString().replace('T', ' ').split('.')[0]
      }));
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
        redemptionAllowed: true,
        productDescription: null
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
      this.reset();
      const productId = row.productId || this.ids;
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
          if (this.form.productId != null) {
            investmentProductApi.update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            investmentProductApi.add(this.form).then(response => {
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
    handleExport() {
      this.download('investment/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
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
      this.navForm = {
        productId: row.productId,
        productCode: row.productCode,
        productName: row.productName,
        currentNetValue: row.netValue,
        newNetValue: null
      };
      this.navOpen = true;
    },
    /** 提交净值更新 */
    submitNavForm() {
      this.$refs["navForm"].validate(valid => {
        if (valid) {
          investmentProductApi.updateNav(this.navForm.productId, this.navForm.newNetValue).then(response => {
            this.$modal.msgSuccess("净值更新成功");
            this.navOpen = false;
            this.getList();
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
    }
  }
};
</script>
