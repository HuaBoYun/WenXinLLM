<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-card class="filter-container" shadow="never">
      <el-form :inline="true" :model="queryParams" ref="queryForm" size="small">
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="queryParams.accountName" placeholder="请输入账户名称" clearable />
        </el-form-item>
        <el-form-item label="银行名称" prop="bankName">
          <el-input v-model="queryParams.bankName" placeholder="请输入银行名称" clearable />
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="queryParams.accountType" placeholder="请选择账户类型" clearable>
            <el-option label="对公账户" value="CORPORATE" />
            <el-option label="对私账户" value="PERSONAL" />
            <el-option label="保证金账户" value="MARGIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="box-card" shadow="never">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="small"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="small"
            :disabled="single"
            @click="handleUpdate"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="small"
            :disabled="multiple"
            @click="handleDelete"
          >删除</el-button>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="accountList" border fit @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="账户名称" align="center" prop="accountName" show-overflow-tooltip />
        <el-table-column label="银行名称" align="center" prop="bankName" />
        <el-table-column label="账户类型" align="center" prop="accountType">
          <template slot-scope="scope">
            <el-tag :type="getAccountTypeTag(scope.row.accountType)">
              {{ getAccountTypeText(scope.row.accountType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="账号" align="center" prop="accountNumber" show-overflow-tooltip />
        <el-table-column label="开户行" align="center" prop="bankBranch" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="同步状态" align="center" prop="syncStatus">
          <template slot-scope="scope">
            <el-tag :type="scope.row.syncStatus === 1 ? 'success' : 'warning'">
              {{ scope.row.syncStatus === 1 ? '已同步' : '未同步' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-refresh"
              @click="handleSync(scope.row)"
            >同步</el-button>
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
    </el-card>

    <!-- 添加或修改电票账户对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="账户名称" prop="accountName">
              <el-input v-model="form.accountName" placeholder="请输入账户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户类型" prop="accountType">
              <el-select v-model="form.accountType" placeholder="请选择账户类型">
                <el-option label="对公账户" value="CORPORATE" />
                <el-option label="对私账户" value="PERSONAL" />
                <el-option label="保证金账户" value="MARGIN" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="form.bankName" placeholder="请输入银行名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户行" prop="bankBranch">
              <el-input v-model="form.bankBranch" placeholder="请输入开户行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="账号" prop="accountNumber">
              <el-input v-model="form.accountNumber" placeholder="请输入账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="户名" prop="accountHolder">
              <el-input v-model="form.accountHolder" placeholder="请输入户名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="网银账号" prop="ebankAccount">
              <el-input v-model="form.ebankAccount" placeholder="请输入网银账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="CA证书" prop="caCertificate">
              <el-input v-model="form.caCertificate" placeholder="请输入CA证书" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listETicketAccount,
  getETicketAccountPage,
  getETicketAccount,
  createETicketAccount,
  updateETicketAccount,
  deleteETicketAccount,
  batchDeleteETicketAccount,
  syncAccountStatus
} from "@/api/treasuryCommon/basicConfigETicketAccountConfig"

export default {
  name: "ETicketAccountConfig",
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
      // 总条数
      total: 0,
      // 电票账户表格数据
      accountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountName: null,
        bankName: null,
        accountType: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        accountName: [
          { required: true, message: "账户名称不能为空", trigger: "blur" }
        ],
        bankName: [
          { required: true, message: "银行名称不能为空", trigger: "blur" }
        ],
        accountType: [
          { required: true, message: "账户类型不能为空", trigger: "change" }
        ],
        accountNumber: [
          { required: true, message: "账号不能为空", trigger: "blur" }
        ],
        accountHolder: [
          { required: true, message: "户名不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询电票账户列表 */
    getList() {
      this.loading = true;
      getETicketAccountPage({
        pageNo: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
        accountName: this.queryParams.accountName,
        bankName: this.queryParams.bankName,
        accountType: this.queryParams.accountType,
        status: this.queryParams.status
      }).then(response => {
        if (response.code === 1) {
          this.accountList = response.data.tlist || [];
          this.total = response.data.totalRecord || 0;
        } else {
          this.$modal.msgError(response.message || "查询失败");
        }
        this.loading = false;
      }).catch(error => {
        console.error('查询电票账户列表失败:', error);
        this.$modal.msgError("查询失败");
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
        id: null,
        accountName: null,
        bankName: null,
        bankBranch: null,
        accountType: 'CORPORATE',
        accountNumber: null,
        accountHolder: null,
        ebankAccount: null,
        caCertificate: null,
        status: 1,
        remark: null
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加电票账户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getETicketAccount(id).then(response => {
        if (response.code === 1) {
          this.form = response.data;
          this.open = true;
          this.title = "修改电票账户";
        } else {
          this.$modal.msgError(response.message || "获取数据失败");
        }
      }).catch(error => {
        console.error('获取编辑数据失败:', error);
        this.$modal.msgError("获取数据失败");
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateETicketAccount(this.form).then(response => {
              if (response.code === 1) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.message || "修改失败");
              }
            }).catch(error => {
              console.error('修改电票账户失败:', error);
              this.$modal.msgError("修改失败");
            });
          } else {
            createETicketAccount(this.form).then(response => {
              if (response.code === 1) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.message || "新增失败");
              }
            }).catch(error => {
              console.error('新增电票账户失败:', error);
              this.$modal.msgError("新增失败");
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id ? [row.id] : this.ids;
      this.$modal.confirm('是否确认删除选中的账户？').then(() => {
        if (ids.length === 1) {
          return deleteETicketAccount(ids[0]);
        } else {
          return batchDeleteETicketAccount(ids);
        }
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 同步账户状态 */
    handleSync(row) {
      this.$modal.loading("正在同步状态...");
      syncAccountStatus(row.id).then(response => {
        this.$modal.closeLoading();
        if (response.code === 1) {
          this.$modal.msgSuccess("同步成功");
          this.getList();
        } else {
          this.$modal.msgError("同步失败：" + response.message);
        }
      }).catch(error => {
        this.$modal.closeLoading();
        console.error('同步账户状态失败:', error);
        this.$modal.msgError("同步异常");
      });
    },
    /** 获取账户类型标签样式 */
    getAccountTypeTag(type) {
      const tagMap = {
        'CORPORATE': 'success',
        'PERSONAL': 'primary',
        'MARGIN': 'warning'
      };
      return tagMap[type] || 'info';
    },
    /** 获取账户类型文本 */
    getAccountTypeText(type) {
      const textMap = {
        'CORPORATE': '对公账户',
        'PERSONAL': '对私账户',
        'MARGIN': '保证金账户'
      };
      return textMap[type] || type;
    }
  }
};
</script>

<style scoped>
/* 确保表格完整显示，不被容器高度限制 */
.app-container .el-table {
  width: 100%;
  height: auto !important;
  max-height: none !important;
}

.app-container .el-table__body-wrapper {
  max-height: none !important;
  height: auto !important;
  overflow: visible !important;
}

.app-container .el-table__body {
  width: 100% !important;
}

.app-container {
  min-height: auto !important;
  height: auto !important;
  max-height: none !important;
  overflow: visible !important;
}

/* 确保所有表格行都可见 */
.el-table__row {
  display: table-row !important;
}

.el-table__body tr {
  display: table-row !important;
}
</style>
