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
                <span>{{ getParamTypeLabel(scope.row.paramType) }}</span>
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
          <!-- 查询条件 -->
          <el-form :model="bondCategoryQuery" ref="bondCategoryQueryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
            <el-form-item label="类别编码" prop="categoryCode">
              <el-input
                v-model="bondCategoryQuery.categoryCode"
                placeholder="请输入类别编码"
                clearable
                @keyup.enter.native="handleBondCategoryQuery"
              />
            </el-form-item>
            <el-form-item label="类别名称" prop="categoryName">
              <el-input
                v-model="bondCategoryQuery.categoryName"
                placeholder="请输入类别名称"
                clearable
                @keyup.enter.native="handleBondCategoryQuery"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="bondCategoryQuery.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleBondCategoryQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetBondCategoryQuery">重置</el-button>
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
                @click="handleBondCategoryAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="bondCategorySingle"
                @click="handleBondCategoryUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="bondCategoryMultiple"
                @click="handleBondCategoryDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="bondCategoryLoading" :data="bondCategoryList" @selection-change="handleBondCategorySelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="类别编码" align="center" prop="categoryCode" width="150" />
            <el-table-column label="类别名称" align="center" prop="categoryName" show-overflow-tooltip />
            <el-table-column label="类别层级" align="center" prop="categoryLevel" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.categoryLevel === 1">一级类别</span>
                <span v-else-if="scope.row.categoryLevel === 2">二级类别</span>
                <span v-else-if="scope.row.categoryLevel === 3">三级类别</span>
                <span v-else>{{ scope.row.categoryLevel }}级</span>
              </template>
            </el-table-column>
            <el-table-column label="父级ID" align="center" prop="parentId" width="100" />
            <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
            <el-table-column label="状态" align="center" prop="status" width="80">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  active-value="1"
                  inactive-value="0"
                  @change="handleBondCategoryStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleBondCategoryUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleBondCategoryDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="bondCategoryTotal>0"
            :total="bondCategoryTotal"
            :page.sync="bondCategoryQuery.pageNum"
            :limit.sync="bondCategoryQuery.pageSize"
            @pagination="getBondCategoryList"
          />
        </div>
      </el-tab-pane>

      <!-- 授信类别管理 -->
      <el-tab-pane label="授信类别管理" name="creditCategory">
        <div class="credit-category-container">
          <!-- 查询条件 -->
          <el-form :model="creditCategoryQuery" ref="creditCategoryQueryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
            <el-form-item label="类别编码" prop="categoryCode">
              <el-input
                v-model="creditCategoryQuery.categoryCode"
                placeholder="请输入类别编码"
                clearable
                @keyup.enter.native="handleCreditCategoryQuery"
              />
            </el-form-item>
            <el-form-item label="类别名称" prop="categoryName">
              <el-input
                v-model="creditCategoryQuery.categoryName"
                placeholder="请输入类别名称"
                clearable
                @keyup.enter.native="handleCreditCategoryQuery"
              />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="creditCategoryQuery.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleCreditCategoryQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetCreditCategoryQuery">重置</el-button>
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
                @click="handleCreditCategoryAdd"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="creditCategorySingle"
                @click="handleCreditCategoryUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="creditCategoryMultiple"
                @click="handleCreditCategoryDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="creditCategoryLoading" :data="creditCategoryList" @selection-change="handleCreditCategorySelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="类别编码" align="center" prop="categoryCode" width="150" />
            <el-table-column label="类别名称" align="center" prop="categoryName" show-overflow-tooltip />
            <el-table-column label="父级ID" align="center" prop="parentId" width="100" />
            <el-table-column label="类别层级" align="center" prop="categoryLevel" width="100" />
            <el-table-column label="排序号" align="center" prop="sortOrder" width="100" />
            <el-table-column label="状态" align="center" prop="status" width="80">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  active-value="1"
                  inactive-value="0"
                  @change="handleCreditCategoryStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createdTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleCreditCategoryUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleCreditCategoryDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="creditCategoryTotal>0"
            :total="creditCategoryTotal"
            :page.sync="creditCategoryQuery.pageNum"
            :limit.sync="creditCategoryQuery.pageSize"
            @pagination="getCreditCategoryList"
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改融资基础参数对话框 -->
    <el-dialog
      :title="basicParamsTitle"
      :visible.sync="basicParamsOpen"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
      custom-class="financing-basic-params-dialog"
      z-index="9999">
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

    <!-- 添加或修改债券类别对话框 -->
    <el-dialog
      :title="bondCategoryTitle"
      :visible.sync="bondCategoryOpen"
      width="600px"
      append-to-body
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      custom-class="financing-bond-category-dialog">
      <el-form ref="bondCategoryForm" :model="bondCategoryForm" :rules="bondCategoryRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="类别编码" prop="categoryCode">
              <el-input v-model="bondCategoryForm.categoryCode" placeholder="请输入类别编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别名称" prop="categoryName">
              <el-input v-model="bondCategoryForm.categoryName" placeholder="请输入类别名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="父级ID" prop="parentId">
              <el-input-number v-model="bondCategoryForm.parentId" :min="0" style="width: 100%" placeholder="0表示顶级" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别层级" prop="categoryLevel">
              <el-input-number v-model="bondCategoryForm.categoryLevel" :min="1" :max="3" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number v-model="bondCategoryForm.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="bondCategoryForm.status">
                <el-radio label="1">启用</el-radio>
                <el-radio label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="bondCategoryForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBondCategoryForm">确 定</el-button>
        <el-button @click="cancelBondCategory">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改授信类别对话框 -->
    <el-dialog
      :title="creditCategoryTitle"
      :visible.sync="creditCategoryOpen"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      custom-class="financing-credit-category-dialog">
      <el-form ref="creditCategoryForm" :model="creditCategoryForm" :rules="creditCategoryRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="类别编码" prop="categoryCode">
              <el-input v-model="creditCategoryForm.categoryCode" placeholder="请输入类别编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别名称" prop="categoryName">
              <el-input v-model="creditCategoryForm.categoryName" placeholder="请输入类别名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="父级ID" prop="parentId">
              <el-input-number v-model="creditCategoryForm.parentId" :min="0" style="width: 100%" placeholder="0表示顶级" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别层级" prop="categoryLevel">
              <el-input-number v-model="creditCategoryForm.categoryLevel" :min="1" :max="3" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortOrder">
              <el-input-number v-model="creditCategoryForm.sortOrder" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="creditCategoryForm.status">
                <el-radio label="1">启用</el-radio>
                <el-radio label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="creditCategoryForm.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCreditCategoryForm">确 定</el-button>
        <el-button @click="cancelCreditCategory">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 导入融资基础参数API
import {
  getBasicParamsPage,
  getBasicParamsDetail,
  addBasicParams,
  updateBasicParams,
  deleteBasicParams,
  toggleBasicParamsStatus
} from "@/api/globalTreasurer-new/financingManagement/basicParams";

// 导入债券类别API
import {
  getBondCategoryPage,
  getBondCategoryDetail,
  addBondCategory,
  updateBondCategory,
  deleteBondCategory,
  toggleBondCategoryStatus
} from "@/api/globalTreasurer-new/financingManagement/bondCategory";

// 导入授信类别API
import {
  getCreditCategoryPage,
  getCreditCategoryDetail,
  addCreditCategory,
  updateCreditCategory,
  deleteCreditCategory,
  toggleCreditCategoryStatus
} from "@/api/globalTreasurer-new/financingManagement/creditCategory";

import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'

export default {
  name: "FinancingConfig",
  components: {
    Pagination
  },
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
      ],

      // 债券类别相关数据
      bondCategoryLoading: true,
      bondCategoryIds: [],
      bondCategorySingle: true,
      bondCategoryMultiple: true,
      bondCategoryTotal: 0,
      bondCategoryList: [],
      bondCategoryTitle: "",
      bondCategoryOpen: false,
      bondCategoryQuery: {
        pageNum: 1,
        pageSize: 10,
        categoryCode: null,
        categoryName: null,
        status: null
      },
      bondCategoryForm: {},
      bondCategoryRules: {
        categoryCode: [
          { required: true, message: "类别编码不能为空", trigger: "blur" }
        ],
        categoryName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ]
      },

      // 授信类别相关数据
      creditCategoryLoading: true,
      creditCategoryIds: [],
      creditCategorySingle: true,
      creditCategoryMultiple: true,
      creditCategoryTotal: 0,
      creditCategoryList: [],
      creditCategoryTitle: "",
      creditCategoryOpen: false,
      creditCategoryQuery: {
        pageNum: 1,
        pageSize: 10,
        categoryCode: null,
        categoryName: null,
        status: null
      },
      creditCategoryForm: {},
      creditCategoryRules: {
        categoryCode: [
          { required: true, message: "类别编码不能为空", trigger: "blur" }
        ],
        categoryName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getBasicParamsList();
  },
  methods: {
    parseTime,

    /** 获取参数类型标签 */
    getParamTypeLabel(paramType) {
      const option = this.paramTypeOptions.find(item => item.value === paramType);
      return option ? option.label : paramType || '未设置';
    },

    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'basicParams') {
        this.getBasicParamsList();
      } else if (tab.name === 'bondCategory') {
        this.getBondCategoryList();
      } else if (tab.name === 'creditCategory') {
        this.getCreditCategoryList();
      }
    },

    /** 查询融资基础参数列表 */
    getBasicParamsList() {
      this.basicParamsLoading = true;
      getBasicParamsPage(this.basicParamsQuery).then(response => {
        try {
          console.log('融资基础参数响应数据:', response);
          // 支持多种响应格式
          if (response && response.data && response.data.rows) {
            // data.rows格式 (实际后端返回格式)
            this.basicParamsList = response.data.rows;
            this.basicParamsTotal = response.data.total || 0;
          } else if (response && response.data && response.data.records) {
            // PageResult格式
            this.basicParamsList = response.data.records;
            this.basicParamsTotal = response.data.total || 0;
          } else if (response && response.data && Array.isArray(response.data)) {
            // 数组格式
            this.basicParamsList = response.data;
            this.basicParamsTotal = response.data.length;
          } else if (response && response.rows) {
            // rows格式
            this.basicParamsList = response.rows;
            this.basicParamsTotal = response.total || 0;
          } else if (Array.isArray(response)) {
            // 直接数组格式
            this.basicParamsList = response;
            this.basicParamsTotal = response.length;
          } else {
            // 默认空数据
            console.warn('未识别的数据格式:', response);
            this.basicParamsList = [];
            this.basicParamsTotal = 0;
          }
          console.log('融资基础参数列表:', this.basicParamsList);
          console.log('融资基础参数总数:', this.basicParamsTotal);
        } catch (error) {
          console.error('解析融资基础参数列表数据失败', error);
          this.basicParamsList = [];
          this.basicParamsTotal = 0;
          this.$message.error('数据格式错误');
        } finally {
          this.basicParamsLoading = false;
        }
      }).catch(error => {
        console.error('查询融资基础参数列表失败', error);
        this.basicParamsList = [];
        this.basicParamsTotal = 0;
        this.basicParamsLoading = false;
        this.$message.error('查询失败，请稍后重试');
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
      console.log('=== 新增按钮被点击 ===');
      console.log('当前 basicParamsOpen:', this.basicParamsOpen);
      console.log('Vue实例:', this);

      this.resetBasicParamsForm();

      // 使用$set确保响应式更新
      this.$set(this, 'basicParamsOpen', true);
      this.basicParamsTitle = "添加融资基础参数";

      console.log('设置后 basicParamsOpen:', this.basicParamsOpen);

      // 强制更新视图
      this.$forceUpdate();

      // 使用nextTick确保DOM更新
      this.$nextTick(() => {
        console.log('nextTick basicParamsOpen:', this.basicParamsOpen);
        const dialog = document.querySelector('.financing-basic-params-dialog');
        console.log('对话框DOM存在:', !!dialog);
        if (dialog) {
          console.log('对话框display:', window.getComputedStyle(dialog).display);
          console.log('对话框visibility:', window.getComputedStyle(dialog).visibility);
        }
      });
    },

    /** 修改按钮操作 */
    handleBasicParamsUpdate(row) {
      this.resetBasicParamsForm();
      const paramId = row.paramId || this.basicParamsIds;
      getBasicParamsDetail(paramId).then(response => {
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
            updateBasicParams(this.basicParamsForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.basicParamsOpen = false;
              this.getBasicParamsList();
            });
          } else {
            addBasicParams(this.basicParamsForm).then(response => {
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
        return deleteBasicParams(paramIds);
      }).then(() => {
        this.getBasicParamsList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 状态修改 */
    handleBasicParamsStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.paramName + '"参数吗？').then(function() {
        return toggleBasicParamsStatus({ id: row.paramId, isEnabled: row.isEnabled });
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
      console.log('=== 执行表单重置 ===');
      this.basicParamsForm = {
        paramId: null,
        paramCode: null,
        paramName: null,
        paramType: null,
        paramValue: null,
        remark: null,
        isEnabled: 1
      };
      // 暂时注释掉resetForm，避免清空表单验证状态导致的问题
      // this.resetForm("basicParamsForm");
      console.log('表单重置完成:', this.basicParamsForm);
    },

    // ==================== 债券类别管理方法 ====================

    /** 查询债券类别列表 */
    getBondCategoryList() {
      this.bondCategoryLoading = true;
      getBondCategoryPage(this.bondCategoryQuery).then(response => {
        try {
          console.log('债券类别响应数据:', response);
          // 支持多种响应格式
          if (response && response.data && response.data.rows) {
            // data.rows格式 (实际后端返回格式)
            this.bondCategoryList = response.data.rows;
            this.bondCategoryTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.bondCategoryList = response.data;
            this.bondCategoryTotal = response.total || response.data.length;
          } else if (response.rows) {
            this.bondCategoryList = response.rows;
            this.bondCategoryTotal = response.total || 0;
          } else if (Array.isArray(response)) {
            this.bondCategoryList = response;
            this.bondCategoryTotal = response.length;
          } else {
            console.warn('未识别的债券类别数据格式:', response);
            this.bondCategoryList = [];
            this.bondCategoryTotal = 0;
          }
          console.log('债券类别列表:', this.bondCategoryList);
          console.log('债券类别总数:', this.bondCategoryTotal);
        } catch (error) {
          console.error('解析债券类别列表数据失败', error);
          this.bondCategoryList = [];
          this.bondCategoryTotal = 0;
          this.$message.error('数据格式错误');
        } finally {
          this.bondCategoryLoading = false;
        }
      }).catch(error => {
        console.error('查询债券类别列表失败', error);
        this.bondCategoryList = [];
        this.bondCategoryTotal = 0;
        this.bondCategoryLoading = false;
        this.$message.error('查询失败，请稍后重试');
      });
    },

    /** 搜索按钮操作 */
    handleBondCategoryQuery() {
      this.bondCategoryQuery.pageNum = 1;
      this.getBondCategoryList();
    },

    /** 重置按钮操作 */
    resetBondCategoryQuery() {
      this.resetForm("bondCategoryQueryForm");
      this.handleBondCategoryQuery();
    },

    /** 多选框选中数据 */
    handleBondCategorySelectionChange(selection) {
      this.bondCategoryIds = selection.map(item => item.categoryId);
      this.bondCategorySingle = selection.length !== 1;
      this.bondCategoryMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleBondCategoryAdd() {
      console.log('=== 点击债券类别新增按钮 ===');
      this.resetBondCategoryForm();
      // 使用 nextTick 确保 DOM 更新
      this.$nextTick(() => {
        this.bondCategoryOpen = true;
        this.bondCategoryTitle = "添加债券类别";
        console.log('bondCategoryOpen in nextTick:', this.bondCategoryOpen);
        // 再次强制更新
        this.$forceUpdate();
      });
    },

    /** 修改按钮操作 */
    handleBondCategoryUpdate(row) {
      console.log('=== 点击债券类别修改按钮 ===');
      this.resetBondCategoryForm();
      const id = row.categoryId || this.bondCategoryIds[0];
      console.log('修改债券类别ID:', id);
      getBondCategoryDetail(id).then(response => {
        try {
          this.bondCategoryForm = response.data || response;
          this.$nextTick(() => {
            this.bondCategoryOpen = true;
            this.bondCategoryTitle = "修改债券类别";
            console.log('bondCategoryOpen in nextTick:', this.bondCategoryOpen);
            this.$forceUpdate();
          });
        } catch (error) {
          console.error('获取债券类别详情失败', error);
          this.$message.error('获取详情失败');
        }
      }).catch(error => {
        console.error('查询债券类别详情失败', error);
        this.$message.error('查询失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitBondCategoryForm() {
      this.$refs["bondCategoryForm"].validate(valid => {
        if (valid) {
          if (this.bondCategoryForm.categoryId != null) {
            updateBondCategory(this.bondCategoryForm).then(response => {
              try {
                const code = response.code || response.status;
                if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
                  this.$message.success("修改成功");
                  this.bondCategoryOpen = false;
                  this.getBondCategoryList();
                } else {
                  this.$message.error(response.message || response.msg || "修改失败");
                }
              } catch (error) {
                console.error('处理修改响应失败', error);
                this.$message.error('操作失败');
              }
            }).catch(error => {
              console.error('修改债券类别失败', error);
              this.$message.error('修改失败，请稍后重试');
            });
          } else {
            addBondCategory(this.bondCategoryForm).then(response => {
              try {
                const code = response.code || response.status;
                if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
                  this.$message.success("新增成功");
                  this.bondCategoryOpen = false;
                  this.getBondCategoryList();
                } else {
                  this.$message.error(response.message || response.msg || "新增失败");
                }
              } catch (error) {
                console.error('处理新增响应失败', error);
                this.$message.error('操作失败');
              }
            }).catch(error => {
              console.error('新增债券类别失败', error);
              this.$message.error('新增失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleBondCategoryDelete(row) {
      const ids = row.categoryId || this.bondCategoryIds.join(",");
      this.$confirm('是否确认删除选中的债券类别？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteBondCategory(ids);
      }).then(response => {
        try {
          const code = response.code || response.status;
          if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
            this.getBondCategoryList();
            this.$message.success("删除成功");
          } else {
            this.$message.error(response.message || response.msg || "删除失败");
          }
        } catch (error) {
          console.error('处理删除响应失败', error);
          this.$message.error('操作失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('删除债券类别失败', error);
          this.$message.error('删除失败，请稍后重试');
        }
      });
    },

    /** 状态修改 */
    handleBondCategoryStatusChange(row) {
      let text = row.status === "1" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.categoryName + '"吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return toggleBondCategoryStatus({ id: row.id, status: row.status });
      }).then(response => {
        try {
          const code = response.code || response.status;
          if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
            this.$message.success(text + "成功");
          } else {
            this.$message.error(response.message || response.msg || text + "失败");
            row.status = row.status === "1" ? "0" : "1";
          }
        } catch (error) {
          console.error('处理状态修改响应失败', error);
          this.$message.error('操作失败');
          row.status = row.status === "1" ? "0" : "1";
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('修改债券类别状态失败', error);
          this.$message.error('操作失败，请稍后重试');
        }
        row.status = row.status === "1" ? "0" : "1";
      });
    },

    /** 取消按钮 */
    cancelBondCategory() {
      this.bondCategoryOpen = false;
      this.resetBondCategoryForm();
    },

    /** 表单重置 */
    resetBondCategoryForm() {
      this.bondCategoryForm = {
        categoryId: null,
        categoryCode: null,
        categoryName: null,
        parentId: 0,
        categoryLevel: 1,
        sortOrder: 0,
        status: "1",
        remark: null
      };
      // 暂时注释掉resetForm，避免清空表单验证状态导致的问题
      // this.resetForm("bondCategoryForm");
    },

    // ==================== 授信类别管理方法 ====================

    /** 查询授信类别列表 */
    getCreditCategoryList() {
      this.creditCategoryLoading = true;
      getCreditCategoryPage(this.creditCategoryQuery).then(response => {
        try {
          console.log('授信类别响应数据:', response);
          // 支持多种响应格式
          if (response && response.data && response.data.rows) {
            // data.rows格式 (实际后端返回格式)
            this.creditCategoryList = response.data.rows;
            this.creditCategoryTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            this.creditCategoryList = response.data;
            this.creditCategoryTotal = response.total || response.data.length;
          } else if (response.rows) {
            this.creditCategoryList = response.rows;
            this.creditCategoryTotal = response.total || 0;
          } else if (Array.isArray(response)) {
            this.creditCategoryList = response;
            this.creditCategoryTotal = response.length;
          } else {
            console.warn('未识别的授信类别数据格式:', response);
            this.creditCategoryList = [];
            this.creditCategoryTotal = 0;
          }
          console.log('授信类别列表:', this.creditCategoryList);
          console.log('授信类别总数:', this.creditCategoryTotal);
        } catch (error) {
          console.error('解析授信类别列表数据失败', error);
          this.creditCategoryList = [];
          this.creditCategoryTotal = 0;
          this.$message.error('数据格式错误');
        } finally {
          this.creditCategoryLoading = false;
        }
      }).catch(error => {
        console.error('查询授信类别列表失败', error);
        this.creditCategoryList = [];
        this.creditCategoryTotal = 0;
        this.creditCategoryLoading = false;
        this.$message.error('查询失败，请稍后重试');
      });
    },

    /** 搜索按钮操作 */
    handleCreditCategoryQuery() {
      this.creditCategoryQuery.pageNum = 1;
      this.getCreditCategoryList();
    },

    /** 重置按钮操作 */
    resetCreditCategoryQuery() {
      this.resetForm("creditCategoryQueryForm");
      this.handleCreditCategoryQuery();
    },

    /** 多选框选中数据 */
    handleCreditCategorySelectionChange(selection) {
      this.creditCategoryIds = selection.map(item => item.categoryId);
      this.creditCategorySingle = selection.length !== 1;
      this.creditCategoryMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleCreditCategoryAdd() {
      console.log('=== 点击授信类别新增按钮 ===');
      this.resetCreditCategoryForm();
      this.$nextTick(() => {
        this.creditCategoryOpen = true;
        this.creditCategoryTitle = "添加授信类别";
        console.log('creditCategoryOpen in nextTick:', this.creditCategoryOpen);
        this.$forceUpdate();
      });
    },

    /** 修改按钮操作 */
    handleCreditCategoryUpdate(row) {
      console.log('=== 点击授信类别修改按钮 ===');
      this.resetCreditCategoryForm();
      const id = row.categoryId || this.creditCategoryIds[0];
      console.log('修改授信类别ID:', id);
      getCreditCategoryDetail(id).then(response => {
        try {
          this.creditCategoryForm = response.data || response;
          this.$nextTick(() => {
            this.creditCategoryOpen = true;
            this.creditCategoryTitle = "修改授信类别";
            console.log('creditCategoryOpen in nextTick:', this.creditCategoryOpen);
            this.$forceUpdate();
          });
        } catch (error) {
          console.error('获取授信类别详情失败', error);
          this.$message.error('获取详情失败');
        }
      }).catch(error => {
        console.error('查询授信类别详情失败', error);
        this.$message.error('查询失败，请稍后重试');
      });
    },

    /** 提交按钮 */
    submitCreditCategoryForm() {
      this.$refs["creditCategoryForm"].validate(valid => {
        if (valid) {
          if (this.creditCategoryForm.categoryId != null) {
            updateCreditCategory(this.creditCategoryForm).then(response => {
              try {
                const code = response.code || response.status;
                if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
                  this.$message.success("修改成功");
                  this.creditCategoryOpen = false;
                  this.getCreditCategoryList();
                } else {
                  this.$message.error(response.message || response.msg || "修改失败");
                }
              } catch (error) {
                console.error('处理修改响应失败', error);
                this.$message.error('操作失败');
              }
            }).catch(error => {
              console.error('修改授信类别失败', error);
              this.$message.error('修改失败，请稍后重试');
            });
          } else {
            addCreditCategory(this.creditCategoryForm).then(response => {
              try {
                const code = response.code || response.status;
                if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
                  this.$message.success("新增成功");
                  this.creditCategoryOpen = false;
                  this.getCreditCategoryList();
                } else {
                  this.$message.error(response.message || response.msg || "新增失败");
                }
              } catch (error) {
                console.error('处理新增响应失败', error);
                this.$message.error('操作失败');
              }
            }).catch(error => {
              console.error('新增授信类别失败', error);
              this.$message.error('新增失败，请稍后重试');
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleCreditCategoryDelete(row) {
      const ids = row.categoryId || this.creditCategoryIds.join(",");
      this.$confirm('是否确认删除选中的授信类别？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return deleteCreditCategory(ids);
      }).then(response => {
        try {
          const code = response.code || response.status;
          if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
            this.getCreditCategoryList();
            this.$message.success("删除成功");
          } else {
            this.$message.error(response.message || response.msg || "删除失败");
          }
        } catch (error) {
          console.error('处理删除响应失败', error);
          this.$message.error('操作失败');
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('删除授信类别失败', error);
          this.$message.error('删除失败，请稍后重试');
        }
      });
    },

    /** 状态修改 */
    handleCreditCategoryStatusChange(row) {
      let text = row.status === "1" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.categoryName + '"吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return toggleCreditCategoryStatus({ id: row.id, status: row.status });
      }).then(response => {
        try {
          const code = response.code || response.status;
          if ([200, 0, '200', '0', '1', 1, 2].includes(code)) {
            this.$message.success(text + "成功");
          } else {
            this.$message.error(response.message || response.msg || text + "失败");
            row.status = row.status === "1" ? "0" : "1";
          }
        } catch (error) {
          console.error('处理状态修改响应失败', error);
          this.$message.error('操作失败');
          row.status = row.status === "1" ? "0" : "1";
        }
      }).catch(error => {
        if (error !== 'cancel') {
          console.error('修改授信类别状态失败', error);
          this.$message.error('操作失败，请稍后重试');
        }
        row.status = row.status === "1" ? "0" : "1";
      });
    },

    /** 取消按钮 */
    cancelCreditCategory() {
      this.creditCategoryOpen = false;
      this.resetCreditCategoryForm();
    },

    /** 表单重置 */
    resetCreditCategoryForm() {
      this.creditCategoryForm = {
        categoryId: null,
        categoryCode: null,
        categoryName: null,
        parentId: 0,
        categoryLevel: 1,
        sortOrder: 0,
        status: "1",
        remark: null
      };
      // 暂时注释掉resetForm，避免清空表单验证状态导致的问题
      // this.resetForm("creditCategoryForm");
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

/* 确保对话框可见 */
.financing-basic-params-dialog {
  z-index: 9999 !important;
}

.financing-basic-params-dialog .el-dialog {
  z-index: 9999 !important;
}
</style>
