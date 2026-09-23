<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-card class="filter-container" shadow="never">
      <el-form :inline="true" :model="queryParams" ref="queryForm" size="small">
        <el-form-item label="源系统" prop="sourceSystem">
          <el-input v-model="queryParams.sourceSystem" placeholder="请输入源系统" clearable />
        </el-form-item>
        <el-form-item label="目标系统" prop="targetSystem">
          <el-input v-model="queryParams.targetSystem" placeholder="请输入目标系统" clearable />
        </el-form-item>
        <el-form-item label="映射类型" prop="mappingType">
          <el-select v-model="queryParams.mappingType" placeholder="请选择映射类型" clearable>
            <el-option label="字段映射" value="FIELD" />
            <el-option label="数据映射" value="DATA" />
            <el-option label="接口映射" value="INTERFACE" />
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
      <el-table v-loading="loading" :data="mappingList" border fit @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="源系统" align="center" prop="sourceSystem" />
        <el-table-column label="源字段" align="center" prop="sourceField" />
        <el-table-column label="目标系统" align="center" prop="targetSystem" />
        <el-table-column label="目标字段" align="center" prop="targetField" />
        <el-table-column label="映射类型" align="center" prop="mappingType">
          <template slot-scope="scope">
            <el-tag :type="getMappingTypeTag(scope.row.mappingType)">
              {{ getMappingTypeText(scope.row.mappingType) }}
            </el-tag>
          </template>
        </el-table-column>
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
              icon="el-icon-connection"
              @click="handleTest(scope.row)"
            >测试</el-button>
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

    <!-- 添加或修改数据映射对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="源系统" prop="sourceSystem">
              <el-input v-model="form.sourceSystem" placeholder="请输入源系统" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源字段" prop="sourceField">
              <el-input v-model="form.sourceField" placeholder="请输入源字段" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目标系统" prop="targetSystem">
              <el-input v-model="form.targetSystem" placeholder="请输入目标系统" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标字段" prop="targetField">
              <el-input v-model="form.targetField" placeholder="请输入目标字段" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="映射类型" prop="mappingType">
              <el-select v-model="form.mappingType" placeholder="请选择映射类型">
                <el-option label="字段映射" value="FIELD" />
                <el-option label="数据映射" value="DATA" />
                <el-option label="接口映射" value="INTERFACE" />
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
            <el-form-item label="转换规则" prop="conversionRule">
              <el-input v-model="form.conversionRule" type="textarea" placeholder="请输入转换规则" :rows="3" />
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
  listDataMapping,
  getDataMappingPage,
  getDataMapping,
  createDataMapping,
  updateDataMapping,
  deleteDataMapping,
  batchDeleteDataMapping,
  testDataMapping
} from "@/api/treasuryCommon/basicConfigDataMappingConfig"

export default {
  name: "DataMappingConfig",
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
      // 数据映射表格数据
      mappingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sourceSystem: null,
        targetSystem: null,
        mappingType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sourceSystem: [
          { required: true, message: "源系统不能为空", trigger: "blur" }
        ],
        sourceField: [
          { required: true, message: "源字段不能为空", trigger: "blur" }
        ],
        targetSystem: [
          { required: true, message: "目标系统不能为空", trigger: "blur" }
        ],
        targetField: [
          { required: true, message: "目标字段不能为空", trigger: "blur" }
        ],
        mappingType: [
          { required: true, message: "映射类型不能为空", trigger: "change" }
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
    /** 查询数据映射列表 */
    getList() {
      this.loading = true;
      getDataMappingPage({
        pageNo: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
        sourceSystem: this.queryParams.sourceSystem,
        targetSystem: this.queryParams.targetSystem,
        mappingType: this.queryParams.mappingType
      }).then(response => {
        if (response.code === 1) {
          this.mappingList = response.data.tlist || [];
          this.total = response.data.totalRecord || 0;
        } else {
          this.$modal.msgError(response.message || "查询失败");
        }
        this.loading = false;
      }).catch(error => {
        console.error('查询数据映射列表失败:', error);
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
        sourceSystem: null,
        sourceField: null,
        targetSystem: null,
        targetField: null,
        mappingType: 'FIELD',
        conversionRule: null,
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
      this.title = "添加数据映射";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getDataMapping(id).then(response => {
        if (response.code === 1) {
          this.form = response.data;
          this.open = true;
          this.title = "修改数据映射";
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
            updateDataMapping(this.form).then(response => {
              if (response.code === 1) {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.message || "修改失败");
              }
            }).catch(error => {
              console.error('修改数据映射失败:', error);
              this.$modal.msgError("修改失败");
            });
          } else {
            createDataMapping(this.form).then(response => {
              if (response.code === 1) {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.$modal.msgError(response.message || "新增失败");
              }
            }).catch(error => {
              console.error('新增数据映射失败:', error);
              this.$modal.msgError("新增失败");
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id ? [row.id] : this.ids;
      this.$modal.confirm('是否确认删除选中的数据项？').then(() => {
        if (ids.length === 1) {
          return deleteDataMapping(ids[0]);
        } else {
          return batchDeleteDataMapping(ids);
        }
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 测试映射 */
    handleTest(row) {
      this.$modal.loading("正在测试映射...");
      testDataMapping({ id: row.id }).then(response => {
        this.$modal.closeLoading();
        if (response.code === 1) {
          this.$modal.msgSuccess("映射测试成功");
        } else {
          this.$modal.msgError("映射测试失败：" + response.message);
        }
      }).catch(error => {
        this.$modal.closeLoading();
        console.error('测试映射失败:', error);
        this.$modal.msgError("映射测试异常");
      });
    },
    /** 获取映射类型标签样式 */
    getMappingTypeTag(type) {
      const tagMap = {
        'FIELD': 'success',
        'DATA': 'primary',
        'INTERFACE': 'warning'
      };
      return tagMap[type] || 'info';
    },
    /** 获取映射类型文本 */
    getMappingTypeText(type) {
      const textMap = {
        'FIELD': '字段映射',
        'DATA': '数据映射',
        'INTERFACE': '接口映射'
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
