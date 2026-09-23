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
      <el-form-item label="债券代码" prop="bondCode">
        <el-input
          v-model="queryParams.bondCode"
          placeholder="请输入债券代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="债券类型" prop="bondType">
        <el-select v-model="queryParams.bondType" placeholder="请选择债券类型" clearable>
          <el-option label="政府债券" value="GOVERNMENT" />
          <el-option label="企业债券" value="CORPORATE" />
          <el-option label="金融债券" value="FINANCIAL" />
          <el-option label="可转换债券" value="CONVERTIBLE" />
        </el-select>
      </el-form-item>
      <el-form-item label="发行人" prop="issuer">
        <el-input
          v-model="queryParams.issuer"
          placeholder="请输入发行人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投资状态" prop="investmentStatus">
        <el-select v-model="queryParams.investmentStatus" placeholder="请选择投资状态" clearable>
          <el-option label="活跃" value="ACTIVE" />
          <el-option label="已到期" value="MATURED" />
          <el-option label="已出售" value="SOLD" />
          <el-option label="已取消" value="CANCELLED" />
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
          v-hasPermi="['investment:bond:add']"
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
          v-hasPermi="['investment:bond:edit']"
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
          v-hasPermi="['investment:bond:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['investment:bond:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="bondList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投资编号" align="center" prop="investmentNo" />
      <el-table-column label="债券代码" align="center" prop="bondCode" />
      <el-table-column label="债券类型" align="center" prop="bondType">
        <template slot-scope="scope">
          <dict-tag :options="bondTypeOptions" :value="scope.row.bondType"/>
        </template>
      </el-table-column>
      <el-table-column label="发行人" align="center" prop="issuer" show-overflow-tooltip />
      <el-table-column label="购买价格" align="center" prop="purchasePrice">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.purchasePrice).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="购买数量" align="center" prop="purchaseQuantity">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.purchaseQuantity).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前价格" align="center" prop="currentPrice">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.currentPrice || 0).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前价值" align="center" prop="currentValue">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.currentValue || 0).toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="票面利率" align="center" prop="couponRate">
        <template slot-scope="scope">
          <span>{{ (scope.row.couponRate * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="到期收益率" align="center" prop="yieldToMaturity">
        <template slot-scope="scope">
          <span>{{ ((scope.row.yieldToMaturity || 0) * 100).toFixed(2) }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="投资状态" align="center" prop="investmentStatus">
        <template slot-scope="scope">
          <el-tag :type="getInvestmentStatusType(scope.row.investmentStatus)">{{ getInvestmentStatusText(scope.row.investmentStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="购买日期" align="center" prop="purchaseDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.purchaseDate, '{y}-{m}-{d}') }}</span>
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
            v-hasPermi="['investment:bond:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['investment:bond:edit']"
          >修改</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['investment:bond:purchase']">
            <span class="el-dropdown-link">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="purchase" v-if="scope.row.investmentStatus === 'ACTIVE'">购买</el-dropdown-item>
              <el-dropdown-item command="sell" v-if="scope.row.investmentStatus === 'ACTIVE'">出售</el-dropdown-item>
              <el-dropdown-item command="coupon" v-if="scope.row.investmentStatus === 'ACTIVE'">付息</el-dropdown-item>
              <el-dropdown-item command="updateValuation">更新估值</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['investment:bond:remove']"
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

    <!-- 添加或修改债券投资对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="投资编号" prop="investmentNo">
              <el-input v-model="form.investmentNo" placeholder="请输入投资编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="债券代码" prop="bondCode">
              <el-input v-model="form.bondCode" placeholder="请输入债券代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="债券类型" prop="bondType">
              <el-select v-model="form.bondType" placeholder="请选择债券类型">
                <el-option label="政府债券" value="GOVERNMENT" />
                <el-option label="企业债券" value="CORPORATE" />
                <el-option label="金融债券" value="FINANCIAL" />
                <el-option label="可转换债券" value="CONVERTIBLE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发行人" prop="issuer">
              <el-input v-model="form.issuer" placeholder="请输入发行人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="购买价格" prop="purchasePrice">
              <el-input v-model="form.purchasePrice" placeholder="请输入购买价格" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购买数量" prop="purchaseQuantity">
              <el-input v-model="form.purchaseQuantity" placeholder="请输入购买数量" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="面值" prop="faceValue">
              <el-input v-model="form.faceValue" placeholder="请输入面值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票面利率" prop="couponRate">
              <el-input v-model="form.couponRate" placeholder="请输入票面利率（如：0.05）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="购买日期" prop="purchaseDate">
              <el-date-picker
                v-model="form.purchaseDate"
                type="date"
                placeholder="选择购买日期"
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
            <el-form-item label="下次付息日期" prop="nextCouponDate">
              <el-date-picker
                v-model="form.nextCouponDate"
                type="date"
                placeholder="选择下次付息日期"
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

    <!-- 查看债券投资详情对话框 -->
    <el-dialog title="债券投资详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="投资编号">{{ viewForm.investmentNo }}</el-descriptions-item>
        <el-descriptions-item label="债券代码">{{ viewForm.bondCode }}</el-descriptions-item>
        <el-descriptions-item label="债券类型">{{ viewForm.bondType }}</el-descriptions-item>
        <el-descriptions-item label="发行人">{{ viewForm.issuer }}</el-descriptions-item>
        <el-descriptions-item label="购买价格">{{ parseFloat(viewForm.purchasePrice || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="购买数量">{{ parseFloat(viewForm.purchaseQuantity || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="当前价格">{{ parseFloat(viewForm.currentPrice || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="当前价值">{{ parseFloat(viewForm.currentValue || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="面值">{{ parseFloat(viewForm.faceValue || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="票面利率">{{ ((viewForm.couponRate || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="到期收益率">{{ ((viewForm.yieldToMaturity || 0) * 100).toFixed(2) }}%</el-descriptions-item>
        <el-descriptions-item label="到期价值">{{ parseFloat(viewForm.maturityValue || 0).toLocaleString() }}</el-descriptions-item>
        <el-descriptions-item label="应计利息">{{ parseFloat(viewForm.accruedInterest || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="未实现损益">{{ parseFloat(viewForm.unrealizedGainLoss || 0).toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="投资状态">{{ getInvestmentStatusText(viewForm.investmentStatus) }}</el-descriptions-item>
        <el-descriptions-item label="购买日期">{{ parseTime(viewForm.purchaseDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(viewForm.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="下次付息日期">{{ parseTime(viewForm.nextCouponDate, '{y}-{m}-{d}') }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 购买债券对话框 -->
    <el-dialog title="购买债券" :visible.sync="purchaseOpen" width="500px" append-to-body>
      <el-form ref="purchaseForm" :model="purchaseForm" :rules="purchaseRules" label-width="100px">
        <el-form-item label="债券代码">
          <el-input v-model="purchaseForm.bondCode" disabled />
        </el-form-item>
        <el-form-item label="购买价格" prop="purchasePrice">
          <el-input v-model="purchaseForm.purchasePrice" placeholder="请输入购买价格" />
        </el-form-item>
        <el-form-item label="购买数量" prop="purchaseQuantity">
          <el-input v-model="purchaseForm.purchaseQuantity" placeholder="请输入购买数量" />
        </el-form-item>
        <el-form-item label="购买日期" prop="purchaseDate">
          <el-date-picker
            v-model="purchaseForm.purchaseDate"
            type="date"
            placeholder="选择购买日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPurchaseForm">确 定</el-button>
        <el-button @click="purchaseOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 出售债券对话框 -->
    <el-dialog title="出售债券" :visible.sync="sellOpen" width="500px" append-to-body>
      <el-form ref="sellForm" :model="sellForm" :rules="sellRules" label-width="100px">
        <el-form-item label="投资编号">
          <el-input v-model="sellForm.investmentNo" disabled />
        </el-form-item>
        <el-form-item label="持有数量">
          <el-input v-model="sellForm.holdingQuantity" disabled />
        </el-form-item>
        <el-form-item label="出售价格" prop="sellPrice">
          <el-input v-model="sellForm.sellPrice" placeholder="请输入出售价格" />
        </el-form-item>
        <el-form-item label="出售数量" prop="sellQuantity">
          <el-input v-model="sellForm.sellQuantity" placeholder="请输入出售数量" />
        </el-form-item>
        <el-form-item label="出售日期" prop="sellDate">
          <el-date-picker
            v-model="sellForm.sellDate"
            type="date"
            placeholder="选择出售日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSellForm">确 定</el-button>
        <el-button @click="sellOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { bondInvestmentApi } from "@/api/globalTreasurer/tzlc";

export default {
  name: "BondInvestment",
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
      // 债券投资表格数据
      bondList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 是否显示购买弹出层
      purchaseOpen: false,
      // 是否显示出售弹出层
      sellOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        investmentNo: null,
        bondCode: null,
        bondType: null,
        issuer: null,
        investmentStatus: null
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 购买表单参数
      purchaseForm: {},
      // 出售表单参数
      sellForm: {},
      // 表单校验
      rules: {
        investmentNo: [
          { required: true, message: "投资编号不能为空", trigger: "blur" }
        ],
        bondCode: [
          { required: true, message: "债券代码不能为空", trigger: "blur" }
        ],
        bondType: [
          { required: true, message: "债券类型不能为空", trigger: "change" }
        ],
        issuer: [
          { required: true, message: "发行人不能为空", trigger: "blur" }
        ],
        purchasePrice: [
          { required: true, message: "购买价格不能为空", trigger: "blur" }
        ],
        purchaseQuantity: [
          { required: true, message: "购买数量不能为空", trigger: "blur" }
        ],
        faceValue: [
          { required: true, message: "面值不能为空", trigger: "blur" }
        ],
        couponRate: [
          { required: true, message: "票面利率不能为空", trigger: "blur" }
        ],
        purchaseDate: [
          { required: true, message: "购买日期不能为空", trigger: "blur" }
        ],
        maturityDate: [
          { required: true, message: "到期日期不能为空", trigger: "blur" }
        ]
      },
      // 购买校验
      purchaseRules: {
        purchasePrice: [
          { required: true, message: "购买价格不能为空", trigger: "blur" }
        ],
        purchaseQuantity: [
          { required: true, message: "购买数量不能为空", trigger: "blur" }
        ],
        purchaseDate: [
          { required: true, message: "购买日期不能为空", trigger: "blur" }
        ]
      },
      // 出售校验
      sellRules: {
        sellPrice: [
          { required: true, message: "出售价格不能为空", trigger: "blur" }
        ],
        sellQuantity: [
          { required: true, message: "出售数量不能为空", trigger: "blur" }
        ],
        sellDate: [
          { required: true, message: "出售日期不能为空", trigger: "blur" }
        ]
      },
      // 债券类型选项
      bondTypeOptions: [
        { label: "政府债券", value: "GOVERNMENT" },
        { label: "企业债券", value: "CORPORATE" },
        { label: "金融债券", value: "FINANCIAL" },
        { label: "可转换债券", value: "CONVERTIBLE" }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询债券投资列表 */
    getList() {
      this.loading = true;
      bondInvestmentApi.getList(this.queryParams).then(response => {
        this.bondList = response.rows;
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
        investmentId: null,
        planId: null,
        accountId: null,
        investmentNo: null,
        bondCode: null,
        bondType: null,
        issuer: null,
        purchasePrice: null,
        purchaseQuantity: null,
        faceValue: null,
        couponRate: null,
        purchaseDate: null,
        maturityDate: null,
        nextCouponDate: null
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
      this.ids = selection.map(item => item.investmentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加债券投资";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const investmentId = row.investmentId || this.ids;
      bondInvestmentApi.getInfo(investmentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改债券投资";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      bondInvestmentApi.getInfo(row.investmentId).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.investmentId != null) {
            bondInvestmentApi.update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            bondInvestmentApi.add(this.form).then(response => {
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
      const investmentIds = row.investmentId || this.ids;
      this.$modal.confirm('是否确认删除债券投资编号为"' + investmentIds + '"的数据项？').then(function() {
        return bondInvestmentApi.delete(investmentIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('investment/bond/export', {
        ...this.queryParams
      }, `bond_${new Date().getTime()}.xlsx`)
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case 'purchase':
          this.handlePurchase(row);
          break;
        case 'sell':
          this.handleSell(row);
          break;
        case 'coupon':
          this.handleCoupon(row);
          break;
        case 'updateValuation':
          this.handleUpdateValuation(row);
          break;
      }
    },
    /** 购买债券 */
    handlePurchase(row) {
      this.purchaseForm = {
        investmentId: row.investmentId,
        bondCode: row.bondCode,
        purchasePrice: null,
        purchaseQuantity: null,
        purchaseDate: null
      };
      this.purchaseOpen = true;
    },
    /** 提交购买 */
    submitPurchaseForm() {
      this.$refs["purchaseForm"].validate(valid => {
        if (valid) {
          bondInvestmentApi.purchase(this.purchaseForm).then(response => {
            this.$modal.msgSuccess("购买成功");
            this.purchaseOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 出售债券 */
    handleSell(row) {
      this.sellForm = {
        investmentId: row.investmentId,
        investmentNo: row.investmentNo,
        holdingQuantity: row.purchaseQuantity,
        sellPrice: null,
        sellQuantity: null,
        sellDate: null
      };
      this.sellOpen = true;
    },
    /** 提交出售 */
    submitSellForm() {
      this.$refs["sellForm"].validate(valid => {
        if (valid) {
          bondInvestmentApi.sell(
            this.sellForm.investmentId,
            this.sellForm.sellPrice,
            this.sellForm.sellQuantity,
            this.sellForm.sellDate
          ).then(response => {
            this.$modal.msgSuccess("出售成功");
            this.sellOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 处理付息 */
    handleCoupon(row) {
      this.$prompt('请输入付息金额', '处理债券付息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^\d+(\.\d+)?$/,
        inputErrorMessage: '付息金额格式不正确'
      }).then(({ value }) => {
        return bondInvestmentApi.processCoupon(row.investmentId, value, new Date().toISOString().split('T')[0]);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("付息处理成功");
      }).catch(() => {});
    },
    /** 更新估值 */
    handleUpdateValuation(row) {
      this.$prompt('请输入新的价格和收益率（格式：价格,收益率）', '更新债券估值', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        const [price, yieldValue] = value.split(',');
        return bondInvestmentApi.updateValuation(row.investmentId, parseFloat(price), parseFloat(yieldValue));
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
        'SOLD': 'warning',
        'CANCELLED': 'danger'
      };
      return statusMap[status] || 'info';
    },
    /** 获取投资状态文本 */
    getInvestmentStatusText(status) {
      const statusMap = {
        'ACTIVE': '活跃',
        'MATURED': '已到期',
        'SOLD': '已出售',
        'CANCELLED': '已取消'
      };
      return statusMap[status] || status;
    }
  }
};
</script>
