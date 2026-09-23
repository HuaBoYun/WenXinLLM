<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 担保申请管理 -->
      <el-tab-pane label="担保申请管理" name="application">
        <div class="application-container">
          <!-- 查询条件 -->
          <el-form :model="applicationQuery" ref="applicationQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="申请编号" prop="applicationNo">
              <el-input
                v-model="applicationQuery.applicationNo"
                placeholder="请输入申请编号"
                clearable
                @keyup.enter.native="handleApplicationQuery"
              />
            </el-form-item>
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="applicationQuery.guaranteeType" placeholder="请选择担保类型" clearable>
                <el-option label="保证担保" value="GUARANTEE" />
                <el-option label="抵押担保" value="MORTGAGE" />
                <el-option label="质押担保" value="PLEDGE" />
                <el-option label="信用担保" value="CREDIT" />
              </el-select>
            </el-form-item>
            <el-form-item label="申请状态" prop="applicationStatus">
              <el-select v-model="applicationQuery.applicationStatus" placeholder="请选择申请状态" clearable>
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审批" value="PENDING_APPROVAL" />
                <el-option label="已审批" value="APPROVED" />
                <el-option label="已拒绝" value="REJECTED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleApplicationQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetApplicationQuery">重置</el-button>
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
                @click="handleApplicationAdd"
              >新增申请</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="applicationSingle"
                @click="handleApplicationUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="applicationMultiple"
                @click="handleApplicationDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="applicationLoading" :data="applicationList" @selection-change="handleApplicationSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="申请编号" align="center" prop="applicationNo" />
            <el-table-column label="担保类型" align="center" prop="guaranteeType">
              <template slot-scope="scope">
                <dict-tag :options="guaranteeTypeOptions" :value="scope.row.guaranteeType"/>
              </template>
            </el-table-column>
            <el-table-column label="担保金额" align="center" prop="guaranteeAmount">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.guaranteeAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="被担保方" align="center" prop="guaranteedParty" />
            <el-table-column label="申请状态" align="center" prop="applicationStatus">
              <template slot-scope="scope">
                <dict-tag :options="applicationStatusOptions" :value="scope.row.applicationStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="申请时间" align="center" prop="applicationTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.applicationTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleApplicationView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleApplicationUpdate(scope.row)"
                  v-if="scope.row.applicationStatus === 'DRAFT'"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleApplicationDelete(scope.row)"
                  v-if="scope.row.applicationStatus === 'DRAFT'"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="applicationTotal>0"
            :total="applicationTotal"
            :page.sync="applicationQuery.pageNum"
            :limit.sync="applicationQuery.pageSize"
            @pagination="getApplicationList"
          />
        </div>
      </el-tab-pane>

      <!-- 担保合同管理 -->
      <el-tab-pane label="担保合同管理" name="contract">
        <div class="contract-container">
          <p>担保合同管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 担保物管理 -->
      <el-tab-pane label="担保物管理" name="collateral">
        <div class="collateral-container">
          <p>担保物管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 担保监控 -->
      <el-tab-pane label="担保监控" name="monitoring">
        <div class="monitoring-container">
          <p>担保监控功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改担保申请对话框 -->
    <el-dialog :title="applicationTitle" :visible.sync="applicationOpen" width="800px" append-to-body>
      <el-form ref="applicationForm" :model="applicationForm" :rules="applicationRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="applicationForm.guaranteeType" placeholder="请选择担保类型">
                <el-option label="保证担保" value="GUARANTEE" />
                <el-option label="抵押担保" value="MORTGAGE" />
                <el-option label="质押担保" value="PLEDGE" />
                <el-option label="信用担保" value="CREDIT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保金额" prop="guaranteeAmount">
              <el-input v-model="applicationForm.guaranteeAmount" placeholder="请输入担保金额">
                <template slot="append">万元</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="被担保方" prop="guaranteedParty">
              <el-input v-model="applicationForm.guaranteedParty" placeholder="请输入被担保方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保期限" prop="guaranteeTerm">
              <el-input v-model="applicationForm.guaranteeTerm" placeholder="请输入担保期限">
                <template slot="append">月</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="担保事由" prop="guaranteeReason">
          <el-input v-model="applicationForm.guaranteeReason" type="textarea" placeholder="请输入担保事由" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="applicationForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApplicationForm">确 定</el-button>
        <el-button @click="cancelApplication">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getGuaranteeApplicationPage,
  getGuaranteeApplication,
  createGuaranteeApplication,
  updateGuaranteeApplication,
  deleteGuaranteeApplication
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'

export default {
  name: "GuaranteeManagement",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "application",
      // 显示搜索条件
      showSearch: true,
      
      // 担保申请相关数据
      applicationLoading: true,
      applicationIds: [],
      applicationSingle: true,
      applicationMultiple: true,
      applicationTotal: 0,
      applicationList: [],
      applicationTitle: "",
      applicationOpen: false,
      applicationQuery: {
        pageNum: 1,
        pageSize: 10,
        applicationNo: null,
        guaranteeType: null,
        applicationStatus: null
      },
      applicationForm: {},
      applicationRules: {
        guaranteeType: [
          { required: true, message: "担保类型不能为空", trigger: "change" }
        ],
        guaranteeAmount: [
          { required: true, message: "担保金额不能为空", trigger: "blur" }
        ],
        guaranteedParty: [
          { required: true, message: "被担保方不能为空", trigger: "blur" }
        ],
        guaranteeTerm: [
          { required: true, message: "担保期限不能为空", trigger: "blur" }
        ],
        guaranteeReason: [
          { required: true, message: "担保事由不能为空", trigger: "blur" }
        ]
      },
      
      // 字典选项
      guaranteeTypeOptions: [
        { label: "保证担保", value: "GUARANTEE" },
        { label: "抵押担保", value: "MORTGAGE" },
        { label: "质押担保", value: "PLEDGE" },
        { label: "信用担保", value: "CREDIT" }
      ],
      applicationStatusOptions: [
        { label: "草稿", value: "DRAFT" },
        { label: "待审批", value: "PENDING_APPROVAL" },
        { label: "已审批", value: "APPROVED" },
        { label: "已拒绝", value: "REJECTED" }
      ]
    };
  },
  created() {
    this.getApplicationList();
  },
  methods: {
    parseTime,
    
    /** 格式化金额 */
    formatAmount(amount) {
      if (!amount) return '0.00';
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + ' 万元';
    },
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'application') {
        this.getApplicationList();
      }
    },

    /** 查询担保申请列表 */
    getApplicationList() {
      this.applicationLoading = true;
      getGuaranteeApplicationPage(this.applicationQuery).then(response => {
        this.applicationList = response.rows;
        this.applicationTotal = response.total;
        this.applicationLoading = false;
      });
    },

    /** 搜索按钮操作 */
    handleApplicationQuery() {
      this.applicationQuery.pageNum = 1;
      this.getApplicationList();
    },

    /** 重置按钮操作 */
    resetApplicationQuery() {
      this.resetForm("applicationQueryForm");
      this.handleApplicationQuery();
    },

    /** 多选框选中数据 */
    handleApplicationSelectionChange(selection) {
      this.applicationIds = selection.map(item => item.applicationId);
      this.applicationSingle = selection.length !== 1;
      this.applicationMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleApplicationAdd() {
      this.resetApplicationForm();
      this.applicationOpen = true;
      this.applicationTitle = "添加担保申请";
    },

    /** 修改按钮操作 */
    handleApplicationUpdate(row) {
      this.resetApplicationForm();
      const applicationId = row.applicationId || this.applicationIds;
      getGuaranteeApplication(applicationId).then(response => {
        this.applicationForm = response.data;
        this.applicationOpen = true;
        this.applicationTitle = "修改担保申请";
      });
    },

    /** 查看按钮操作 */
    handleApplicationView(row) {
      // 跳转到详情页面
      this.$router.push(`/globalTreasurer/financing/guarantee/detail/${row.applicationId}`);
    },

    /** 提交按钮 */
    submitApplicationForm() {
      this.$refs["applicationForm"].validate(valid => {
        if (valid) {
          if (this.applicationForm.applicationId != null) {
            updateGuaranteeApplication(this.applicationForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.applicationOpen = false;
              this.getApplicationList();
            });
          } else {
            createGuaranteeApplication(this.applicationForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.applicationOpen = false;
              this.getApplicationList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleApplicationDelete(row) {
      const applicationIds = row.applicationId || this.applicationIds;
      this.$modal.confirm('是否确认删除担保申请编号为"' + applicationIds + '"的数据项？').then(function() {
        return deleteGuaranteeApplication(applicationIds);
      }).then(() => {
        this.getApplicationList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 取消按钮 */
    cancelApplication() {
      this.applicationOpen = false;
      this.resetApplicationForm();
    },

    /** 表单重置 */
    resetApplicationForm() {
      this.applicationForm = {
        applicationId: null,
        guaranteeType: null,
        guaranteeAmount: null,
        guaranteedParty: null,
        guaranteeTerm: null,
        guaranteeReason: null,
        remark: null
      };
      this.resetForm("applicationForm");
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
</style>
