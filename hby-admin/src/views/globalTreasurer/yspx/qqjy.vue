<template>
  <div class="option-transaction-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>期权交易管理</h2>
      <p>管理期权合约的创建、定价、行权和到期处理</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="合约编号">
          <el-input v-model="searchForm.contractCode" placeholder="请输入合约编号" clearable />
        </el-form-item>
        <el-form-item label="期权类型">
          <el-select v-model="searchForm.optionType" placeholder="请选择期权类型" clearable>
            <el-option label="看涨期权" value="CALL" />
            <el-option label="看跌期权" value="PUT" />
          </el-select>
        </el-form-item>
        <el-form-item label="期权风格">
          <el-select v-model="searchForm.optionStyle" placeholder="请选择期权风格" clearable>
            <el-option label="欧式期权" value="EUROPEAN" />
            <el-option label="美式期权" value="AMERICAN" />
            <el-option label="亚式期权" value="ASIAN" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="searchForm.status" placeholder="请选择交易状态" clearable>
            <el-option label="待生效" value="PENDING" />
            <el-option label="生效中" value="ACTIVE" />
            <el-option label="已行权" value="EXERCISED" />
            <el-option label="已到期" value="EXPIRED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker
            v-model="searchForm.expiryDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新建期权</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-data-analysis" size="mini" @click="handlePricingModel">定价模型</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="transactionList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="合约编号" prop="contractCode" width="150" show-overflow-tooltip />
        <el-table-column label="期权类型" prop="optionType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOptionTypeTag(scope.row.optionType)">
              {{ getOptionTypeText(scope.row.optionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="期权风格" prop="optionStyle" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOptionStyleTag(scope.row.optionStyle)" size="mini">
              {{ getOptionStyleText(scope.row.optionStyle) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标的资产" prop="underlyingAsset" width="120" show-overflow-tooltip />
        <el-table-column label="行权价格" prop="strikePrice" width="100" align="right">
          <template slot-scope="scope">
            <span class="price-text">{{ formatPrice(scope.row.strikePrice) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="期权费" prop="premium" width="100" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.premium) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="理论价值" prop="theoreticalValue" width="100" align="right">
          <template slot-scope="scope">
            <span class="value-text">{{ formatAmount(scope.row.theoreticalValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Delta" prop="delta" width="80" align="right">
          <template slot-scope="scope">
            <span class="greek-text">{{ formatGreek(scope.row.delta) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Gamma" prop="gamma" width="80" align="right">
          <template slot-scope="scope">
            <span class="greek-text">{{ formatGreek(scope.row.gamma) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Theta" prop="theta" width="80" align="right">
          <template slot-scope="scope">
            <span class="greek-text">{{ formatGreek(scope.row.theta) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Vega" prop="vega" width="80" align="right">
          <template slot-scope="scope">
            <span class="greek-text">{{ formatGreek(scope.row.vega) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" prop="expiryDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expiryDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="pricing" icon="el-icon-money">重新定价</el-dropdown-item>
                <el-dropdown-item command="greeks" icon="el-icon-data-line">希腊字母</el-dropdown-item>
                <el-dropdown-item command="exercise" v-if="scope.row.status === 'ACTIVE'" icon="el-icon-check">行权</el-dropdown-item>
                <el-dropdown-item command="expire" v-if="scope.row.status === 'ACTIVE'" icon="el-icon-time">到期处理</el-dropdown-item>
                <el-dropdown-item command="cancel" v-if="scope.row.status === 'PENDING'" icon="el-icon-close">取消</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合约编号" prop="contractCode">
                  <el-input v-model="form.contractCode" placeholder="请输入合约编号" :disabled="form.transactionId != null" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="期权类型" prop="optionType">
                  <el-select v-model="form.optionType" placeholder="请选择期权类型" style="width: 100%">
                    <el-option label="看涨期权" value="CALL" />
                    <el-option label="看跌期权" value="PUT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="期权风格" prop="optionStyle">
                  <el-select v-model="form.optionStyle" placeholder="请选择期权风格" style="width: 100%">
                    <el-option label="欧式期权" value="EUROPEAN" />
                    <el-option label="美式期权" value="AMERICAN" />
                    <el-option label="亚式期权" value="ASIAN" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="标的资产" prop="underlyingAsset">
                  <el-input v-model="form.underlyingAsset" placeholder="请输入标的资产" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="合约数量" prop="contractSize">
                  <el-input v-model="form.contractSize" placeholder="请输入合约数量" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="行权价格" prop="strikePrice">
                  <el-input v-model="form.strikePrice" placeholder="请输入行权价格" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="期权费" prop="premium">
                  <el-input v-model="form.premium" placeholder="请输入期权费" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种" prop="currency">
                  <el-select v-model="form.currency" placeholder="请选择币种" style="width: 100%">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易日期" prop="tradeDate">
                  <el-date-picker
                    v-model="form.tradeDate"
                    type="date"
                    placeholder="选择交易日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="expiryDate">
                  <el-date-picker
                    v-model="form.expiryDate"
                    type="date"
                    placeholder="选择到期日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="定价参数" name="pricing">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标的价格" prop="underlyingPrice">
                  <el-input v-model="form.underlyingPrice" placeholder="请输入标的价格" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="无风险利率" prop="riskFreeRate">
                  <el-input v-model="form.riskFreeRate" placeholder="请输入无风险利率(%)" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="波动率" prop="volatility">
                  <el-input v-model="form.volatility" placeholder="请输入波动率(%)" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="股息率" prop="dividendYield">
                  <el-input v-model="form.dividendYield" placeholder="请输入股息率(%)" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="定价模型" prop="pricingModel">
                  <el-select v-model="form.pricingModel" placeholder="请选择定价模型" style="width: 100%">
                    <el-option label="Black-Scholes" value="BLACK_SCHOLES" />
                    <el-option label="二叉树模型" value="BINOMIAL_TREE" />
                    <el-option label="蒙特卡洛" value="MONTE_CARLO" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="行权方式" prop="exerciseType">
                  <el-select v-model="form.exerciseType" placeholder="请选择行权方式" style="width: 100%">
                    <el-option label="现金交割" value="CASH_SETTLEMENT" />
                    <el-option label="实物交割" value="PHYSICAL_SETTLEMENT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="交易对手" name="counterparty">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易对手" prop="counterparty">
                  <el-input v-model="form.counterparty" placeholder="请输入交易对手" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="对手评级" prop="counterpartyRating">
                  <el-select v-model="form.counterpartyRating" placeholder="请选择对手评级" style="width: 100%">
                    <el-option label="AAA" value="AAA" />
                    <el-option label="AA" value="AA" />
                    <el-option label="A" value="A" />
                    <el-option label="BBB" value="BBB" />
                    <el-option label="BB" value="BB" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="交易员" prop="trader">
                  <el-input v-model="form.trader" placeholder="请输入交易员" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="销售员" prop="salesperson">
                  <el-input v-model="form.salesperson" placeholder="请输入销售员" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="期权交易详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="合约编号">{{ detailData.contractCode }}</el-descriptions-item>
        <el-descriptions-item label="期权类型">
          <el-tag :type="getOptionTypeTag(detailData.optionType)">
            {{ getOptionTypeText(detailData.optionType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="期权风格">
          <el-tag :type="getOptionStyleTag(detailData.optionStyle)" size="mini">
            {{ getOptionStyleText(detailData.optionStyle) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="标的资产">{{ detailData.underlyingAsset }}</el-descriptions-item>
        <el-descriptions-item label="合约数量">{{ detailData.contractSize }}</el-descriptions-item>
        <el-descriptions-item label="行权价格">{{ formatPrice(detailData.strikePrice) }}</el-descriptions-item>
        <el-descriptions-item label="期权费">{{ formatAmount(detailData.premium) }}</el-descriptions-item>
        <el-descriptions-item label="理论价值">{{ formatAmount(detailData.theoreticalValue) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailData.currency }}</el-descriptions-item>
        <el-descriptions-item label="Delta">{{ formatGreek(detailData.delta) }}</el-descriptions-item>
        <el-descriptions-item label="Gamma">{{ formatGreek(detailData.gamma) }}</el-descriptions-item>
        <el-descriptions-item label="Theta">{{ formatGreek(detailData.theta) }}</el-descriptions-item>
        <el-descriptions-item label="Vega">{{ formatGreek(detailData.vega) }}</el-descriptions-item>
        <el-descriptions-item label="Rho">{{ formatGreek(detailData.rho) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交易日期">{{ parseTime(detailData.tradeDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(detailData.expiryDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="交易对手">{{ detailData.counterparty }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 定价模型对话框 -->
    <el-dialog title="期权定价模型" :visible.sync="pricingOpen" width="600px" append-to-body>
      <el-form ref="pricingForm" :model="pricingForm" label-width="120px">
        <el-form-item label="标的价格">
          <el-input v-model="pricingForm.underlyingPrice" placeholder="请输入标的价格" />
        </el-form-item>
        <el-form-item label="行权价格">
          <el-input v-model="pricingForm.strikePrice" placeholder="请输入行权价格" />
        </el-form-item>
        <el-form-item label="无风险利率">
          <el-input v-model="pricingForm.riskFreeRate" placeholder="请输入无风险利率(%)" />
        </el-form-item>
        <el-form-item label="波动率">
          <el-input v-model="pricingForm.volatility" placeholder="请输入波动率(%)" />
        </el-form-item>
        <el-form-item label="到期时间">
          <el-input v-model="pricingForm.timeToExpiry" placeholder="请输入到期时间(年)" />
        </el-form-item>
        <el-form-item label="期权类型">
          <el-select v-model="pricingForm.optionType" placeholder="请选择期权类型" style="width: 100%">
            <el-option label="看涨期权" value="CALL" />
            <el-option label="看跌期权" value="PUT" />
          </el-select>
        </el-form-item>
        <el-form-item label="定价模型">
          <el-select v-model="pricingForm.pricingModel" placeholder="请选择定价模型" style="width: 100%">
            <el-option label="Black-Scholes" value="BLACK_SCHOLES" />
            <el-option label="二叉树模型" value="BINOMIAL_TREE" />
            <el-option label="蒙特卡洛" value="MONTE_CARLO" />
          </el-select>
        </el-form-item>
        <el-form-item label="计算结果" v-if="pricingResult">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="理论价值">{{ formatAmount(pricingResult.theoreticalValue) }}</el-descriptions-item>
            <el-descriptions-item label="Delta">{{ formatGreek(pricingResult.delta) }}</el-descriptions-item>
            <el-descriptions-item label="Gamma">{{ formatGreek(pricingResult.gamma) }}</el-descriptions-item>
            <el-descriptions-item label="Theta">{{ formatGreek(pricingResult.theta) }}</el-descriptions-item>
            <el-descriptions-item label="Vega">{{ formatGreek(pricingResult.vega) }}</el-descriptions-item>
            <el-descriptions-item label="Rho">{{ formatGreek(pricingResult.rho) }}</el-descriptions-item>
          </el-descriptions>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="calculatePrice">计算价格</el-button>
        <el-button @click="pricingOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getOptionTransactionPage, 
  getOptionTransaction, 
  createOptionContract, 
  updateOptionContract, 
  deleteOptionTransaction,
  calculateOptionPrice,
  exerciseOption,
  expireOption,
  calculateGreeks
} from '@/api/globalTreasurer/yspx'
import { parseTime } from '@/utils'

export default {
  name: 'OptionTransaction',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 交易列表
      transactionList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 是否显示定价弹出层
      pricingOpen: false,
      // 详情数据
      detailData: {},
      // 活动标签页
      activeTab: 'basic',
      // 定价结果
      pricingResult: null,
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        contractCode: null,
        optionType: null,
        optionStyle: null,
        status: null,
        expiryDateStart: null,
        expiryDateEnd: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        contractCode: '',
        optionType: '',
        optionStyle: '',
        status: '',
        expiryDateRange: []
      },
      // 表单参数
      form: {},
      // 定价表单
      pricingForm: {},
      // 表单校验
      rules: {
        contractCode: [
          { required: true, message: '合约编号不能为空', trigger: 'blur' }
        ],
        optionType: [
          { required: true, message: '期权类型不能为空', trigger: 'change' }
        ],
        optionStyle: [
          { required: true, message: '期权风格不能为空', trigger: 'change' }
        ],
        underlyingAsset: [
          { required: true, message: '标的资产不能为空', trigger: 'blur' }
        ],
        contractSize: [
          { required: true, message: '合约数量不能为空', trigger: 'blur' }
        ],
        strikePrice: [
          { required: true, message: '行权价格不能为空', trigger: 'blur' }
        ],
        premium: [
          { required: true, message: '期权费不能为空', trigger: 'blur' }
        ],
        currency: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        tradeDate: [
          { required: true, message: '交易日期不能为空', trigger: 'change' }
        ],
        expiryDate: [
          { required: true, message: '到期日期不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    /** 查询交易列表 */
    getList() {
      this.loading = true
      getOptionTransactionPage(this.queryParams).then(response => {
        this.transactionList = response.data.records
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      if (this.searchForm.expiryDateRange && this.searchForm.expiryDateRange.length === 2) {
        this.queryParams.expiryDateStart = this.searchForm.expiryDateRange[0]
        this.queryParams.expiryDateEnd = this.searchForm.expiryDateRange[1]
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        contractCode: '',
        optionType: '',
        optionStyle: '',
        status: '',
        expiryDateRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        contractCode: null,
        optionType: null,
        optionStyle: null,
        status: null,
        expiryDateStart: null,
        expiryDateEnd: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新建期权合约'
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const transactionId = row.transactionId || this.ids
      getOptionTransaction(transactionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改期权合约'
        this.activeTab = 'basic'
      })
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 定价模型 */
    handlePricingModel() {
      this.pricingForm = {
        underlyingPrice: null,
        strikePrice: null,
        riskFreeRate: null,
        volatility: null,
        timeToExpiry: null,
        optionType: 'CALL',
        pricingModel: 'BLACK_SCHOLES'
      }
      this.pricingResult = null
      this.pricingOpen = true
    },
    /** 计算价格 */
    calculatePrice() {
      calculateOptionPrice(this.pricingForm).then(response => {
        this.pricingResult = response.data
        this.$modal.msgSuccess('计算完成')
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.transactionId != null) {
            updateOptionContract(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            createOptionContract(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const transactionIds = row.transactionId || this.ids
      this.$modal.confirm('是否确认删除期权交易编号为"' + transactionIds + '"的数据项？').then(function() {
        return deleteOptionTransaction(transactionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('derivatives/option/export', {
        ...this.queryParams
      }, `option_transaction_${new Date().getTime()}.xlsx`)
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.transactionId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'pricing':
          this.handleRepricing(row)
          break
        case 'greeks':
          this.handleGreeksCalculation(row)
          break
        case 'exercise':
          this.handleExercise(row)
          break
        case 'expire':
          this.handleExpire(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
      }
    },
    /** 重新定价 */
    handleRepricing(row) {
      calculateOptionPrice({ transactionId: row.transactionId }).then(response => {
        this.$modal.msgSuccess('重新定价完成，理论价值：' + this.formatAmount(response.data.theoreticalValue))
        this.getList()
      })
    },
    /** 希腊字母计算 */
    handleGreeksCalculation(row) {
      calculateGreeks(row.transactionId, {}).then(response => {
        const greeks = response.data
        this.$modal.msgSuccess(`希腊字母计算完成：
          Delta: ${this.formatGreek(greeks.delta)}
          Gamma: ${this.formatGreek(greeks.gamma)}
          Theta: ${this.formatGreek(greeks.theta)}
          Vega: ${this.formatGreek(greeks.vega)}`)
      })
    },
    /** 行权处理 */
    handleExercise(row) {
      this.$modal.confirm('是否确认行权期权合约"' + row.contractCode + '"？').then(() => {
        return exerciseOption(row.transactionId, {})
      }).then(() => {
        this.$modal.msgSuccess('行权成功')
        this.getList()
      }).catch(() => {})
    },
    /** 到期处理 */
    handleExpire(row) {
      this.$modal.confirm('是否确认处理到期期权合约"' + row.contractCode + '"？').then(() => {
        return expireOption(row.transactionId)
      }).then(() => {
        this.$modal.msgSuccess('到期处理成功')
        this.getList()
      }).catch(() => {})
    },
    /** 取消合约 */
    handleCancel(row) {
      this.$prompt('请输入取消原因', '取消合约', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        // 取消合约逻辑
        this.$modal.msgSuccess('取消成功')
        this.getList()
      }).catch(() => {})
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        transactionId: null,
        contractCode: null,
        optionType: null,
        optionStyle: null,
        underlyingAsset: null,
        contractSize: null,
        strikePrice: null,
        premium: null,
        currency: null,
        tradeDate: null,
        expiryDate: null,
        underlyingPrice: null,
        riskFreeRate: null,
        volatility: null,
        dividendYield: null,
        pricingModel: null,
        exerciseType: null,
        counterparty: null,
        counterpartyRating: null,
        trader: null,
        salesperson: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },
    /** 获取期权类型标签 */
    getOptionTypeTag(type) {
      const tagMap = {
        'CALL': 'success',
        'PUT': 'danger'
      }
      return tagMap[type] || ''
    },
    /** 获取期权类型文本 */
    getOptionTypeText(type) {
      const textMap = {
        'CALL': '看涨',
        'PUT': '看跌'
      }
      return textMap[type] || type
    },
    /** 获取期权风格标签 */
    getOptionStyleTag(style) {
      const tagMap = {
        'EUROPEAN': 'primary',
        'AMERICAN': 'success',
        'ASIAN': 'warning'
      }
      return tagMap[style] || ''
    },
    /** 获取期权风格文本 */
    getOptionStyleText(style) {
      const textMap = {
        'EUROPEAN': '欧式',
        'AMERICAN': '美式',
        'ASIAN': '亚式'
      }
      return textMap[style] || style
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'ACTIVE': 'success',
        'EXERCISED': 'warning',
        'EXPIRED': 'primary',
        'CANCELLED': 'danger'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待生效',
        'ACTIVE': '生效中',
        'EXERCISED': '已行权',
        'EXPIRED': '已到期',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 格式化价格 */
    formatPrice(price) {
      if (price == null) return '0.0000'
      return parseFloat(price).toFixed(4)
    },
    /** 格式化希腊字母 */
    formatGreek(greek) {
      if (greek == null) return '0.0000'
      return parseFloat(greek).toFixed(4)
    }
  }
}
</script>

<style scoped>
.option-transaction-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: bold;
  color: #409eff;
}

.price-text {
  font-weight: bold;
  color: #67c23a;
}

.value-text {
  font-weight: bold;
  color: #e6a23c;
}

.greek-text {
  font-weight: bold;
  color: #909399;
  font-size: 12px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
