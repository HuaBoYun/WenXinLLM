<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 商业汇票管理 -->
      <el-tab-pane label="商业汇票管理" name="commercialBill">
        <div class="commercial-bill-container">
          <!-- 查询条件 -->
          <el-form :model="billQuery" ref="billQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="票据号码" prop="billNo">
              <el-input
                v-model="billQuery.billNo"
                placeholder="请输入票据号码"
                clearable
                @keyup.enter.native="handleBillQuery"
              />
            </el-form-item>
            <el-form-item label="票据类型" prop="billType">
              <el-select v-model="billQuery.billType" placeholder="请选择票据类型" clearable>
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                <el-option label="电子汇票" value="ELECTRONIC_BILL" />
              </el-select>
            </el-form-item>
            <el-form-item label="票据状态" prop="billStatus">
              <el-select v-model="billQuery.billStatus" placeholder="请选择票据状态" clearable>
                <el-option label="已开立" value="ISSUED" />
                <el-option label="已承兑" value="ACCEPTED" />
                <el-option label="已背书" value="ENDORSED" />
                <el-option label="已贴现" value="DISCOUNTED" />
                <el-option label="已到期" value="MATURED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleBillQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetBillQuery">重置</el-button>
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
                @click="handleBillAdd"
              >开立票据</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="billSingle"
                @click="handleBillUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-document"
                size="mini"
                :disabled="billSingle"
                @click="handleBillEndorse"
              >背书</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-money"
                size="mini"
                :disabled="billSingle"
                @click="handleBillDiscount"
              >贴现</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="billLoading" :data="billList" @selection-change="handleBillSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="票据号码" align="center" prop="billNo" />
            <el-table-column label="票据类型" align="center" prop="billType">
              <template slot-scope="scope">
                <dict-tag :options="billTypeOptions" :value="scope.row.billType"/>
              </template>
            </el-table-column>
            <el-table-column label="票面金额" align="center" prop="billAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.billAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="出票人" align="center" prop="drawer" />
            <el-table-column label="收款人" align="center" prop="payee" />
            <el-table-column label="到期日" align="center" prop="maturityDate" width="100">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="票据状态" align="center" prop="billStatus">
              <template slot-scope="scope">
                <dict-tag :options="billStatusOptions" :value="scope.row.billStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleBillView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleBillUpdate(scope.row)"
                  v-if="scope.row.billStatus === 'ISSUED'"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-document"
                  @click="handleBillEndorse(scope.row)"
                  v-if="['ISSUED', 'ACCEPTED'].includes(scope.row.billStatus)"
                >背书</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="billQuery.pageNum"
            :layout="layout"
            :page-size="billQuery.pageSize"
            :total="billTotal"
            @current-change="handleBillCurrentChange"
            @size-change="handleBillSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 银行承兑汇票 -->
      <el-tab-pane label="银行承兑汇票" name="bankAcceptance">
        <div class="bank-acceptance-container">
          <p>银行承兑汇票功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 信用证管理 -->
      <el-tab-pane label="信用证管理" name="letterOfCredit">
        <div class="letter-of-credit-container">
          <p>信用证管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 保函管理 -->
      <el-tab-pane label="保函管理" name="guarantee">
        <div class="guarantee-container">
          <p>保函管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 票据查询 -->
      <el-tab-pane label="票据查询" name="billQuery">
        <div class="bill-query-container">
          <p>票据查询功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改票据对话框 -->
    <el-dialog :title="billTitle" :visible.sync="billOpen" width="800px" append-to-body>
      <el-form ref="billForm" :model="billForm" :rules="billRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="票据类型" prop="billType">
              <el-select v-model="billForm.billType" placeholder="请选择票据类型">
                <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                <el-option label="电子汇票" value="ELECTRONIC_BILL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="票面金额" prop="billAmount">
              <el-input v-model="billForm.billAmount" placeholder="请输入票面金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票人" prop="drawer">
              <el-input v-model="billForm.drawer" placeholder="请输入出票人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款人" prop="payee">
              <el-input v-model="billForm.payee" placeholder="请输入收款人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出票日期" prop="issueDate">
              <el-date-picker
                v-model="billForm.issueDate"
                type="date"
                placeholder="选择出票日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="maturityDate">
              <el-date-picker
                v-model="billForm.maturityDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="承兑银行" prop="acceptingBank">
          <el-input v-model="billForm.acceptingBank" placeholder="请输入承兑银行" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="billForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBillForm">确 定</el-button>
        <el-button @click="cancelBill">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInstrumentPage,
  getInstrument,
  createInstrument,
  updateInstrument,
  deleteInstrument
} from "@/api/globalTreasurer/pzgl";
import { parseTime } from '@/utils'

export default {
  name: "InstrumentManagement",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "commercialBill",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",
      
      // 票据相关数据
      billLoading: true,
      billIds: [],
      billSingle: true,
      billMultiple: true,
      billTotal: 0,
      billList: [],
      billTitle: "",
      billOpen: false,
      billQuery: {
        pageNum: 1,
        pageSize: 10,
        billNo: null,
        billType: null,
        billStatus: null
      },
      billForm: {},
      billRules: {
        billType: [
          { required: true, message: "票据类型不能为空", trigger: "change" }
        ],
        billAmount: [
          { required: true, message: "票面金额不能为空", trigger: "blur" }
        ],
        drawer: [
          { required: true, message: "出票人不能为空", trigger: "blur" }
        ],
        payee: [
          { required: true, message: "收款人不能为空", trigger: "blur" }
        ],
        issueDate: [
          { required: true, message: "出票日期不能为空", trigger: "change" }
        ],
        maturityDate: [
          { required: true, message: "到期日期不能为空", trigger: "change" }
        ]
      },
      
      // 字典选项
      billTypeOptions: [
        { label: "银行承兑汇票", value: "BANK_ACCEPTANCE" },
        { label: "商业承兑汇票", value: "COMMERCIAL_ACCEPTANCE" },
        { label: "电子汇票", value: "ELECTRONIC_BILL" }
      ],
      billStatusOptions: [
        { label: "已开立", value: "ISSUED" },
        { label: "已承兑", value: "ACCEPTED" },
        { label: "已背书", value: "ENDORSED" },
        { label: "已贴现", value: "DISCOUNTED" },
        { label: "已到期", value: "MATURED" }
      ]
    };
  },
  created() {
    this.getBillList();
  },
  methods: {
    parseTime,
    
    /** 格式化金额 */
    formatAmount(amount) {
      if (!amount) return '0.00';
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + ' 元';
    },
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'commercialBill') {
        this.getBillList();
      }
    },

    /** 查询票据列表 */
    async getBillList() {
      this.billLoading = true;
      try {
        const response = await getInstrumentPage(this.billQuery);
        
        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.billList = response.data.tlist || [];
            this.billTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.billList = response.data.list || [];
            this.billTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.billList = response.data || [];
            this.billTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            // 直接PageInfo格式：{ code: 200, tlist: [], totalRecord: 0 }
            this.billList = response.tlist || [];
            this.billTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.billList = response.list || [];
            this.billTotal = response.total || 0;
          } else {
            // 兜底处理
            this.billList = [];
            this.billTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.billList = [];
          this.billTotal = 0;
        }
      } catch (error) {
        console.error('获取票据列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.billList = [];
        this.billTotal = 0;
      }
      this.billLoading = false;
    },

    /** 搜索按钮操作 */
    handleBillQuery() {
      this.billQuery.pageNum = 1;
      this.getBillList();
    },

    /** 重置按钮操作 */
    resetBillQuery() {
      this.resetForm("billQueryForm");
      this.handleBillQuery();
    },

    /** 多选框选中数据 */
    handleBillSelectionChange(selection) {
      this.billIds = selection.map(item => item.instrumentId);
      this.billSingle = selection.length !== 1;
      this.billMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleBillAdd() {
      this.resetBillForm();
      this.billOpen = true;
      this.billTitle = "开立票据";
    },

    /** 修改按钮操作 */
    handleBillUpdate(row) {
      this.resetBillForm();
      const instrumentId = row.instrumentId || this.billIds;
      getInstrument(instrumentId).then(response => {
        this.billForm = response.data;
        this.billOpen = true;
        this.billTitle = "修改票据";
      });
    },

    /** 查看按钮操作 */
    handleBillView(row) {
      this.$router.push(`/globalTreasurer/pzgl/bill/detail/${row.instrumentId}`);
    },

    /** 背书按钮操作 */
    handleBillEndorse(row) {
      this.$router.push(`/globalTreasurer/pzgl/bill/endorse/${row.instrumentId}`);
    },

    /** 贴现按钮操作 */
    handleBillDiscount(row) {
      this.$router.push(`/globalTreasurer/pzgl/bill/discount/${row.instrumentId}`);
    },

    /** 提交按钮 */
    submitBillForm() {
      this.$refs["billForm"].validate(valid => {
        if (valid) {
          if (this.billForm.instrumentId != null) {
            updateInstrument(this.billForm).then(response => {
              this.$message.success("修改成功");
              this.billOpen = false;
              this.getBillList();
            });
          } else {
            createInstrument(this.billForm).then(response => {
              this.$message.success("新增成功");
              this.billOpen = false;
              this.getBillList();
            });
          }
        }
      });
    },

    /** 取消按钮 */
    cancelBill() {
      this.billOpen = false;
      this.resetBillForm();
    },

    /** 表单重置 */
    resetBillForm() {
      this.billForm = {
        instrumentId: null,
        billType: null,
        billAmount: null,
        drawer: null,
        payee: null,
        issueDate: null,
        maturityDate: null,
        acceptingBank: null,
        remark: null
      };
      this.resetForm("billForm");
    },

    /** 分页相关方法 */
    handleBillCurrentChange(val) {
      this.billQuery.pageNum = val;
      this.getBillList();
    },

    handleBillSizeChange(val) {
      this.billQuery.pageSize = val;
      this.getBillList();
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
