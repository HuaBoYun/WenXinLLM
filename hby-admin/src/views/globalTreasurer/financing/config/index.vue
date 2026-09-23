<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 融资基础参数 -->
      <el-tab-pane label="融资基础参数" name="basicParams">
        <div class="basic-params-container">
          <!-- 查询条件 -->
          <el-form :model="basicParamsQuery" ref="basicParamsQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="参数名称" prop="paramName">
              <el-input
                v-model="basicParamsQuery.paramName"
                placeholder="请输入参数名称"
                clearable
                @keyup.enter.native="handleBasicParamsQuery"
              />
            </el-form-item>
            <el-form-item label="参数类型" prop="paramType">
              <el-select v-model="basicParamsQuery.paramType" placeholder="请选择参数类型" clearable>
                <el-option label="预提周期" value="ACCRUAL_CYCLE" />
                <el-option label="入账参数" value="ACCOUNTING_PARAM" />
                <el-option label="协同参数" value="COLLABORATION_PARAM" />
                <el-option label="预算参数" value="BUDGET_PARAM" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="isEnabled">
              <el-select v-model="basicParamsQuery.isEnabled" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleBasicParamsQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetBasicParamsQuery">重置</el-button>
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
                @click="handleBasicParamsAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="basicParamsSingle"
                @click="handleBasicParamsUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="basicParamsMultiple"
                @click="handleBasicParamsDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="basicParamsLoading" :data="basicParamsList" @selection-change="handleBasicParamsSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="参数编码" align="center" prop="paramCode" />
            <el-table-column label="参数名称" align="center" prop="paramName" />
            <el-table-column label="参数类型" align="center" prop="paramType">
              <template slot-scope="scope">
                <dict-tag :options="paramTypeOptions" :value="scope.row.paramType"/>
              </template>
            </el-table-column>
            <el-table-column label="参数值" align="center" prop="paramValue" />
            <el-table-column label="状态" align="center" prop="isEnabled">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isEnabled"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleBasicParamsStatusChange(scope.row)"
                ></el-switch>
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
                  @click="handleBasicParamsUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleBasicParamsDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="basicParamsTotal>0"
            :total="basicParamsTotal"
            :page.sync="basicParamsQuery.pageNum"
            :limit.sync="basicParamsQuery.pageSize"
            @pagination="getBasicParamsList"
          />
        </div>
      </el-tab-pane>

      <!-- 债券类别管理 -->
      <el-tab-pane label="债券类别管理" name="bondCategory">
        <div class="bond-category-container">
          <p>债券类别管理功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 授信类别管理 -->
      <el-tab-pane label="授信类别管理" name="creditCategory">
        <div class="credit-category-container">
          <p>授信类别管理功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改融资基础参数对话框 -->
    <el-dialog :title="basicParamsTitle" :visible.sync="basicParamsOpen" width="500px" append-to-body>
      <el-form ref="basicParamsForm" :model="basicParamsForm" :rules="basicParamsRules" label-width="80px">
        <el-form-item label="参数编码" prop="paramCode">
          <el-input v-model="basicParamsForm.paramCode" placeholder="请输入参数编码" />
        </el-form-item>
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="basicParamsForm.paramName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数类型" prop="paramType">
          <el-select v-model="basicParamsForm.paramType" placeholder="请选择参数类型">
            <el-option label="预提周期" value="ACCRUAL_CYCLE" />
            <el-option label="入账参数" value="ACCOUNTING_PARAM" />
            <el-option label="协同参数" value="COLLABORATION_PARAM" />
            <el-option label="预算参数" value="BUDGET_PARAM" />
          </el-select>
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input v-model="basicParamsForm.paramValue" placeholder="请输入参数值" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="basicParamsForm.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBasicParamsForm">确 定</el-button>
        <el-button @click="cancelBasicParams">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFinancingBasicParamsList,
  getFinancingBasicParam,
  addFinancingBasicParam,
  updateFinancingBasicParam,
  delFinancingBasicParam,
  toggleFinancingBasicParamStatus
} from "@/api/globalTreasurer/rzgl";
import { parseTime } from '@/utils'

export default {
  name: "FinancingConfig",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "basicParams",
      // 显示搜索条件
      showSearch: true,
      
      // 融资基础参数相关数据
      basicParamsLoading: true,
      basicParamsIds: [],
      basicParamsSingle: true,
      basicParamsMultiple: true,
      basicParamsTotal: 0,
      basicParamsList: [],
      basicParamsTitle: "",
      basicParamsOpen: false,
      basicParamsQuery: {
        pageNum: 1,
        pageSize: 10,
        paramName: null,
        paramType: null,
        isEnabled: null
      },
      basicParamsForm: {},
      basicParamsRules: {
        paramCode: [
          { required: true, message: "参数编码不能为空", trigger: "blur" }
        ],
        paramName: [
          { required: true, message: "参数名称不能为空", trigger: "blur" }
        ],
        paramType: [
          { required: true, message: "参数类型不能为空", trigger: "change" }
        ],
        paramValue: [
          { required: true, message: "参数值不能为空", trigger: "blur" }
        ]
      },
      
      // 字典选项
      paramTypeOptions: [
        { label: "预提周期", value: "ACCRUAL_CYCLE" },
        { label: "入账参数", value: "ACCOUNTING_PARAM" },
        { label: "协同参数", value: "COLLABORATION_PARAM" },
        { label: "预算参数", value: "BUDGET_PARAM" }
      ]
    };
  },
  created() {
    this.getBasicParamsList();
  },
  methods: {
    parseTime,
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'basicParams') {
        this.getBasicParamsList();
      }
    },

    /** 查询融资基础参数列表 */
    getBasicParamsList() {
      this.basicParamsLoading = true;
      getFinancingBasicParamsList(this.basicParamsQuery).then(response => {
        this.basicParamsList = response.rows;
        this.basicParamsTotal = response.total;
        this.basicParamsLoading = false;
      });
    },

    /** 搜索按钮操作 */
    handleBasicParamsQuery() {
      this.basicParamsQuery.pageNum = 1;
      this.getBasicParamsList();
    },

    /** 重置按钮操作 */
    resetBasicParamsQuery() {
      this.resetForm("basicParamsQueryForm");
      this.handleBasicParamsQuery();
    },

    /** 多选框选中数据 */
    handleBasicParamsSelectionChange(selection) {
      this.basicParamsIds = selection.map(item => item.paramId);
      this.basicParamsSingle = selection.length !== 1;
      this.basicParamsMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleBasicParamsAdd() {
      this.resetBasicParamsForm();
      this.basicParamsOpen = true;
      this.basicParamsTitle = "添加融资基础参数";
    },

    /** 修改按钮操作 */
    handleBasicParamsUpdate(row) {
      this.resetBasicParamsForm();
      const paramId = row.paramId || this.basicParamsIds;
      getFinancingBasicParam(paramId).then(response => {
        this.basicParamsForm = response.data;
        this.basicParamsOpen = true;
        this.basicParamsTitle = "修改融资基础参数";
      });
    },

    /** 提交按钮 */
    submitBasicParamsForm() {
      this.$refs["basicParamsForm"].validate(valid => {
        if (valid) {
          if (this.basicParamsForm.paramId != null) {
            updateFinancingBasicParam(this.basicParamsForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.basicParamsOpen = false;
              this.getBasicParamsList();
            });
          } else {
            addFinancingBasicParam(this.basicParamsForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.basicParamsOpen = false;
              this.getBasicParamsList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleBasicParamsDelete(row) {
      const paramIds = row.paramId || this.basicParamsIds;
      this.$modal.confirm('是否确认删除融资基础参数编号为"' + paramIds + '"的数据项？').then(function() {
        return delFinancingBasicParam(paramIds);
      }).then(() => {
        this.getBasicParamsList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 状态修改 */
    handleBasicParamsStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.paramName + '"参数吗？').then(function() {
        return toggleFinancingBasicParamStatus(row.paramId, row.isEnabled);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
      });
    },

    /** 取消按钮 */
    cancelBasicParams() {
      this.basicParamsOpen = false;
      this.resetBasicParamsForm();
    },

    /** 表单重置 */
    resetBasicParamsForm() {
      this.basicParamsForm = {
        paramId: null,
        paramCode: null,
        paramName: null,
        paramType: null,
        paramValue: null,
        remark: null,
        isEnabled: 1
      };
      this.resetForm("basicParamsForm");
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
