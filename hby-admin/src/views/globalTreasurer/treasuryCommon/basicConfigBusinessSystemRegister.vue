<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-card class="filter-container" shadow="never">
      <el-form :inline="true" :model="queryParams" ref="queryForm" size="small">
        <el-form-item label="系统编码" prop="systemCode">
          <el-input v-model="queryParams.systemCode" placeholder="请输入系统编码" clearable />
        </el-form-item>
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="queryParams.systemName" placeholder="请输入系统名称" clearable />
        </el-form-item>
        <el-form-item label="系统类型" prop="systemType">
          <el-select v-model="queryParams.systemType" placeholder="请选择系统类型" clearable>
            <el-option
              v-for="dict in dict.type.system_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
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
            v-hasPermi="['treasury:businessSystem:add']"
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
            v-hasPermi="['treasury:businessSystem:edit']"
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
            v-hasPermi="['treasury:businessSystem:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="small"
            @click="handleExport"
            v-hasPermi="['treasury:businessSystem:export']"
          >导出</el-button>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="systemList" row-key="id" border fit @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="系统编码" align="center" prop="systemCode" />
        <el-table-column label="系统名称" align="center" prop="systemName" show-overflow-tooltip />
        <el-table-column label="系统类型" align="center" prop="systemType">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.system_type" :value="scope.row.systemType"/>
          </template>
        </el-table-column>
        <el-table-column label="系统版本" align="center" prop="systemVersion" />
        <el-table-column label="连接地址" align="center" prop="connectionUrl" show-overflow-tooltip />
        <el-table-column label="认证方式" align="center" prop="authType" />
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['treasury:businessSystem:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['treasury:businessSystem:remove']"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-connection"
              @click="handleTestConnection(scope.row)"
              v-hasPermi="['treasury:businessSystem:test']"
            >测试连接</el-button>
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

    <!-- 添加或修改业务系统对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统编码" prop="systemCode">
              <el-input v-model="form.systemCode" placeholder="请输入系统编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="form.systemName" placeholder="请输入系统名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="系统类型" prop="systemType">
              <el-select v-model="form.systemType" placeholder="请选择系统类型">
                <el-option
                  v-for="dict in dict.type.system_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="系统描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入系统描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="API地址" prop="apiUrl">
              <el-input v-model="form.apiUrl" placeholder="请输入API地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="认证方式" prop="authType">
              <el-select v-model="form.authType" placeholder="请选择认证方式">
                <el-option
                  v-for="dict in dict.type.auth_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
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
            <el-form-item label="认证配置" prop="authConfig">
              <el-input v-model="form.authConfig" type="textarea" placeholder="请输入认证配置JSON" :rows="4" />
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
  listBusinessSystem,
  searchBusinessSystem,
  getBusinessSystem,
  getEditData,
  delBusinessSystem,
  batchDeleteBusinessSystem,
  addBusinessSystem,
  updateBusinessSystem,
  getInitData,
  checkSystemCode,
  validateForm,
  validateEditForm,
  checkDeleteDependencies,
  enableBusinessSystem,
  disableBusinessSystem,
  batchEnableBusinessSystem,
  batchDisableBusinessSystem,
  testConnection,
  batchTestConnection,
  exportBusinessSystem
} from "@/api/treasuryCommon/basicConfigBusinessSystemRegister"

export default {
  name: "BusinessSystemRegister",
  dicts: ['system_type', 'auth_type'],
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
      // 业务系统表格数据
      systemList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        systemCode: null,
        systemName: null,
        systemType: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        systemCode: [
          { required: true, message: "系统编码不能为空", trigger: "blur" }
        ],
        systemName: [
          { required: true, message: "系统名称不能为空", trigger: "blur" }
        ],
        systemType: [
          { required: true, message: "系统类型不能为空", trigger: "change" }
        ],
        apiUrl: [
          { required: true, message: "API地址不能为空", trigger: "blur" }
        ],
        authType: [
          { required: true, message: "认证方式不能为空", trigger: "change" }
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
    /** 查询业务系统列表 */
    getList() {
      this.loading = true;
      listBusinessSystem({
        pageNo: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }).then(response => {
        if (response.code === 1) {
          this.systemList = response.data.tlist;
          this.total = response.data.totalRecord;
        } else {
          this.$modal.msgError(response.message || "查询失败");
        }
        this.loading = false;
      }).catch(error => {
        console.error('查询业务系统列表失败:', error);
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
        systemCode: null,
        systemName: null,
        systemType: null,
        description: null,
        apiUrl: null,
        authType: null,
        authConfig: null,
        status: 1,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.handleSearch();
    },
    /** 搜索处理 */
    handleSearch() {
      this.loading = true;
      const searchData = {
        pageNo: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
        systemCode: this.queryParams.systemCode,
        systemName: this.queryParams.systemName,
        status: this.queryParams.status ? this.queryParams.status.toString() : null
      };
      searchBusinessSystem(searchData).then(response => {
        if (response.code === 1) {
          this.systemList = response.data.tlist;
          this.total = response.data.totalRecord;
        } else {
          this.$modal.msgError(response.message || "查询失败");
        }
        this.loading = false;
      }).catch(error => {
        console.error('搜索业务系统失败:', error);
        this.$modal.msgError("查询失败");
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      // 获取初始化数据
      getInitData().then(response => {
        if (response.code === 1) {
          if (response.data.defaultValues) {
            this.form = { ...this.form, ...response.data.defaultValues };
          }
        }
        this.open = true;
        this.title = "添加业务系统";
      }).catch(error => {
        console.error('获取初始化数据失败:', error);
        this.open = true;
        this.title = "添加业务系统";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getEditData(id).then(response => {
        if (response.code === 1) {
          this.form = response.data;
          this.open = true;
          this.title = "修改业务系统";
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
            // 更新前验证表单
            validateEditForm({
              id: this.form.id,
              systemName: this.form.systemName,
              systemCode: this.form.systemCode,
              systemType: this.form.systemType,
              apiUrl: this.form.apiUrl,
              authType: this.form.authType
            }).then(validateResponse => {
              if (validateResponse.code === 1) {
                updateBusinessSystem(this.form).then(response => {
                  if (response.code === 1) {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  } else {
                    this.$modal.msgError(response.message || "修改失败");
                  }
                }).catch(error => {
                  console.error('修改业务系统失败:', error);
                  this.$modal.msgError("修改失败");
                });
              } else {
                this.$modal.msgError(validateResponse.message || "表单验证失败");
              }
            }).catch(error => {
              console.error('表单验证失败:', error);
              this.$modal.msgError("表单验证失败");
            });
          } else {
            // 新增前验证表单
            validateForm({
              systemName: this.form.systemName,
              systemCode: this.form.systemCode
            }).then(validateResponse => {
              if (validateResponse.code === 1) {
                addBusinessSystem(this.form).then(response => {
                  if (response.code === 1) {
                    this.$modal.msgSuccess("新增成功");
                    this.open = false;
                    this.getList();
                  } else {
                    this.$modal.msgError(response.message || "新增失败");
                  }
                }).catch(error => {
                  console.error('新增业务系统失败:', error);
                  this.$modal.msgError("新增失败");
                });
              } else {
                this.$modal.msgError(validateResponse.message || "表单验证失败");
              }
            }).catch(error => {
              console.error('表单验证失败:', error);
              this.$modal.msgError("表单验证失败");
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const idArray = Array.isArray(ids) ? ids : [ids];

      // 检查删除依赖
      checkDeleteDependencies(idArray).then(response => {
        if (response.code === 1) {
          // 可以删除，进行确认
          this.$modal.confirm('是否确认删除业务系统编号为"' + ids + '"的数据项？').then(() => {
            delBusinessSystem(idArray).then(deleteResponse => {
              if (deleteResponse.code === 1) {
                this.getList();
                this.$modal.msgSuccess("删除成功");
              } else {
                this.$modal.msgError(deleteResponse.message || "删除失败");
              }
            }).catch(error => {
              console.error('删除业务系统失败:', error);
              this.$modal.msgError("删除失败");
            });
          });
        } else {
          this.$modal.msgError(response.message || "无法删除，存在依赖关系");
        }
      }).catch(error => {
        console.error('检查删除依赖失败:', error);
        this.$modal.msgError("检查失败");
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      const exportParams = {
        searchParams: JSON.stringify({
          systemCode: this.queryParams.systemCode,
          systemName: this.queryParams.systemName,
          systemType: this.queryParams.systemType,
          status: this.queryParams.status ? this.queryParams.status.toString() : null
        })
      };

      exportBusinessSystem(exportParams).then(response => {
        if (response.code === 1) {
          // 创建下载链接
          const blob = new Blob([JSON.stringify(response.data, null, 2)], { type: 'application/json' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `businessSystem_${new Date().getTime()}.json`;
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(url);
          this.$modal.msgSuccess("导出成功");
        } else {
          this.$modal.msgError(response.message || "导出失败");
        }
      }).catch(error => {
        console.error('导出业务系统失败:', error);
        this.$modal.msgError("导出失败");
      });
    },
    /** 测试连接按钮操作 */
    handleTestConnection(row) {
      this.$modal.loading("正在测试连接...");
      testConnection({ id: row.id }).then(response => {
        this.$modal.closeLoading();
        if (response.code === 1) {
          this.$modal.msgSuccess("连接测试成功");
        } else {
          this.$modal.msgError("连接测试失败：" + response.message);
        }
      }).catch(error => {
        this.$modal.closeLoading();
        console.error('测试连接失败:', error);
        this.$modal.msgError("连接测试异常");
      });
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