<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 监管机构管理 -->
      <el-tab-pane label="监管机构管理" name="authority">
        <div class="authority-container">
          <!-- 查询条件 -->
          <el-form :model="authorityQueryParams" ref="authorityQueryForm" size="small" :inline="true" v-show="authorityShowSearch" label-width="68px">
            <el-form-item label="机构编码" prop="authorityCode">
              <el-input
                v-model="authorityQueryParams.authorityCode"
                placeholder="请输入机构编码"
                clearable
                @keyup.enter.native="handleAuthorityQuery"
              />
            </el-form-item>
            <el-form-item label="机构名称" prop="authorityName">
              <el-input
                v-model="authorityQueryParams.authorityName"
                placeholder="请输入机构名称"
                clearable
                @keyup.enter.native="handleAuthorityQuery"
              />
            </el-form-item>
            <el-form-item label="机构类型" prop="authorityType">
              <el-select v-model="authorityQueryParams.authorityType" placeholder="请选择机构类型" clearable>
                <el-option label="央行" value="CENTRAL_BANK" />
                <el-option label="证监会" value="SECURITIES_COMMISSION" />
                <el-option label="银保监会" value="BANKING_REGULATOR" />
                <el-option label="外汇局" value="FOREX_REGULATOR" />
                <el-option label="税务局" value="TAX_AUTHORITY" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="isActive">
              <el-select v-model="authorityQueryParams.isActive" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAuthorityQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAuthorityQuery">重置</el-button>
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
                @click="handleAuthorityAdd"
                v-hasPermi="['globalTreasurer:regulatory:authority:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="authoritySingle"
                @click="handleAuthorityUpdate"
                v-hasPermi="['globalTreasurer:regulatory:authority:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="authorityMultiple"
                @click="handleAuthorityDelete"
                v-hasPermi="['globalTreasurer:regulatory:authority:remove']"
              >删除</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="el-icon-download"
                size="mini"
                @click="handleAuthorityExport"
                v-hasPermi="['globalTreasurer:regulatory:authority:export']"
              >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="authorityShowSearch" @queryTable="getAuthorityList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="authorityLoading" :data="authorityList" @selection-change="handleAuthoritySelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="机构编码" align="center" prop="authorityCode" />
            <el-table-column label="机构名称" align="center" prop="authorityName" />
            <el-table-column label="英文名称" align="center" prop="authorityNameEng" />
            <el-table-column label="机构类型" align="center" prop="authorityType">
              <template slot-scope="scope">
                <dict-tag :options="authorityTypeOptions" :value="scope.row.authorityType"/>
              </template>
            </el-table-column>
            <el-table-column label="国家代码" align="center" prop="countryCode" />
            <el-table-column label="联系人" align="center" prop="contactPerson" />
            <el-table-column label="联系电话" align="center" prop="contactPhone" />
            <el-table-column label="状态" align="center" prop="isActive">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isActive"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleAuthorityStatusChange(scope.row)"
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
                  @click="handleAuthorityUpdate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:authority:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleAuthorityDelete(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:authority:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="authorityTotal>0"
            :total="authorityTotal"
            :page.sync="authorityQueryParams.pageNum"
            :limit.sync="authorityQueryParams.pageSize"
            @pagination="getAuthorityList"
          />
        </div>
      </el-tab-pane>

      <!-- 报告模板管理 -->
      <el-tab-pane label="报告模板管理" name="template">
        <div class="template-container">
          <!-- 查询条件 -->
          <el-form :model="templateQueryParams" ref="templateQueryForm" size="small" :inline="true" v-show="templateShowSearch" label-width="68px">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input
                v-model="templateQueryParams.templateCode"
                placeholder="请输入模板编码"
                clearable
                @keyup.enter.native="handleTemplateQuery"
              />
            </el-form-item>
            <el-form-item label="模板名称" prop="templateName">
              <el-input
                v-model="templateQueryParams.templateName"
                placeholder="请输入模板名称"
                clearable
                @keyup.enter.native="handleTemplateQuery"
              />
            </el-form-item>
            <el-form-item label="监管机构" prop="authorityId">
              <el-select v-model="templateQueryParams.authorityId" placeholder="请选择监管机构" clearable>
                <el-option
                  v-for="authority in authorityOptions"
                  :key="authority.authorityId"
                  :label="authority.authorityName"
                  :value="authority.authorityId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="isEnabled">
              <el-select v-model="templateQueryParams.isEnabled" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1" />
                <el-option label="停用" value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleTemplateQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetTemplateQuery">重置</el-button>
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
                @click="handleTemplateAdd"
                v-hasPermi="['globalTreasurer:regulatory:template:add']"
              >新增</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="templateSingle"
                @click="handleTemplateUpdate"
                v-hasPermi="['globalTreasurer:regulatory:template:edit']"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="templateMultiple"
                @click="handleTemplateDelete"
                v-hasPermi="['globalTreasurer:regulatory:template:remove']"
              >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="templateShowSearch" @queryTable="getTemplateList"></right-toolbar>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="templateLoading" :data="templateList" @selection-change="handleTemplateSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="模板编码" align="center" prop="templateCode" />
            <el-table-column label="模板名称" align="center" prop="templateName" />
            <el-table-column label="监管机构" align="center" prop="authorityName" />
            <el-table-column label="报告频率" align="center" prop="reportFrequency">
              <template slot-scope="scope">
                <dict-tag :options="reportFrequencyOptions" :value="scope.row.reportFrequency"/>
              </template>
            </el-table-column>
            <el-table-column label="版本" align="center" prop="version" />
            <el-table-column label="状态" align="center" prop="isEnabled">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.isEnabled"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleTemplateStatusChange(scope.row)"
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
                  @click="handleTemplateUpdate(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-copy-document"
                  @click="handleTemplateCopy(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:add']"
                >复制</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleTemplateDelete(scope.row)"
                  v-hasPermi="['globalTreasurer:regulatory:template:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <pagination
            v-show="templateTotal>0"
            :total="templateTotal"
            :page.sync="templateQueryParams.pageNum"
            :limit.sync="templateQueryParams.pageSize"
            @pagination="getTemplateList"
          />
        </div>
      </el-tab-pane>

      <!-- 监管报告管理 -->
      <el-tab-pane label="监管报告管理" name="report">
        <div class="report-container">
          <el-alert
            title="监管报告管理功能正在开发中..."
            type="info"
            :closable="false">
          </el-alert>
        </div>
      </el-tab-pane>

      <!-- 合规检查管理 -->
      <el-tab-pane label="合规检查管理" name="compliance">
        <div class="compliance-container">
          <el-alert
            title="合规检查管理功能正在开发中..."
            type="info"
            :closable="false">
          </el-alert>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 监管机构添加或修改对话框 -->
    <el-dialog :title="authorityTitle" :visible.sync="authorityOpen" width="600px" append-to-body>
      <el-form ref="authorityForm" :model="authorityForm" :rules="authorityRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构编码" prop="authorityCode">
              <el-input v-model="authorityForm.authorityCode" placeholder="请输入机构编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构名称" prop="authorityName">
              <el-input v-model="authorityForm.authorityName" placeholder="请输入机构名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="英文名称" prop="authorityNameEng">
              <el-input v-model="authorityForm.authorityNameEng" placeholder="请输入英文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="authorityType">
              <el-select v-model="authorityForm.authorityType" placeholder="请选择机构类型">
                <el-option label="央行" value="CENTRAL_BANK" />
                <el-option label="证监会" value="SECURITIES_COMMISSION" />
                <el-option label="银保监会" value="BANKING_REGULATOR" />
                <el-option label="外汇局" value="FOREX_REGULATOR" />
                <el-option label="税务局" value="TAX_AUTHORITY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="国家代码" prop="countryCode">
              <el-input v-model="authorityForm.countryCode" placeholder="请输入国家代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="管辖范围" prop="jurisdiction">
              <el-input v-model="authorityForm.jurisdiction" placeholder="请输入管辖范围" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="authorityForm.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="authorityForm.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="authorityForm.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网站" prop="website">
              <el-input v-model="authorityForm.website" placeholder="请输入网站" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="authorityForm.address" type="textarea" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="authorityForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAuthorityForm">确 定</el-button>
        <el-button @click="cancelAuthority">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAuthorityList,
  getAuthorityById,
  addAuthority,
  updateAuthority,
  delAuthority,
  toggleAuthorityStatus,
  exportAuthorityData,
  getActiveAuthorities,
  getTemplateList,
  getTemplateById,
  addTemplate,
  updateTemplate,
  delTemplate,
  toggleTemplateStatus,
  copyTemplate
} from "@/api/globalTreasurer/jgbs";

export default {
  name: "RegulatoryReporting",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "authority",

      // 监管机构管理相关数据
      authorityLoading: true,
      authorityIds: [],
      authoritySingle: true,
      authorityMultiple: true,
      authorityShowSearch: true,
      authorityTotal: 0,
      authorityList: [],
      authorityTitle: "",
      authorityOpen: false,
      authorityQueryParams: {
        pageNum: 1,
        pageSize: 10,
        authorityCode: null,
        authorityName: null,
        authorityType: null,
        isActive: null
      },
      authorityForm: {},
      authorityRules: {
        authorityCode: [
          { required: true, message: "机构编码不能为空", trigger: "blur" }
        ],
        authorityName: [
          { required: true, message: "机构名称不能为空", trigger: "blur" }
        ],
        authorityType: [
          { required: true, message: "机构类型不能为空", trigger: "change" }
        ]
      },

      // 报告模板管理相关数据
      templateLoading: true,
      templateIds: [],
      templateSingle: true,
      templateMultiple: true,
      templateShowSearch: true,
      templateTotal: 0,
      templateList: [],
      templateTitle: "",
      templateOpen: false,
      templateQueryParams: {
        pageNum: 1,
        pageSize: 10,
        templateCode: null,
        templateName: null,
        authorityId: null,
        isEnabled: null
      },
      templateForm: {},
      templateRules: {
        templateCode: [
          { required: true, message: "模板编码不能为空", trigger: "blur" }
        ],
        templateName: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        authorityId: [
          { required: true, message: "监管机构不能为空", trigger: "change" }
        ],
        reportFrequency: [
          { required: true, message: "报告频率不能为空", trigger: "change" }
        ]
      },

      // 选项数据
      authorityOptions: [],
      authorityTypeOptions: [
        { label: "央行", value: "CENTRAL_BANK" },
        { label: "证监会", value: "SECURITIES_COMMISSION" },
        { label: "银保监会", value: "BANKING_REGULATOR" },
        { label: "外汇局", value: "FOREX_REGULATOR" },
        { label: "税务局", value: "TAX_AUTHORITY" }
      ],
      reportFrequencyOptions: [
        { label: "日报", value: "DAILY" },
        { label: "周报", value: "WEEKLY" },
        { label: "月报", value: "MONTHLY" },
        { label: "季报", value: "QUARTERLY" },
        { label: "半年报", value: "SEMI_ANNUAL" },
        { label: "年报", value: "ANNUAL" },
        { label: "临时报告", value: "AD_HOC" }
      ]
    };
  },
  created() {
    this.getAuthorityList();
    this.getAuthorityOptions();
  },
  methods: {
    // ==================== 标签页切换 ====================
    handleTabClick(tab) {
      if (tab.name === "authority") {
        this.getAuthorityList();
      } else if (tab.name === "template") {
        this.getTemplateList();
      }
    },

    // ==================== 监管机构管理方法 ====================
    /** 查询监管机构列表 */
    getAuthorityList() {
      this.authorityLoading = true;
      getAuthorityList(this.authorityQueryParams).then(response => {
        this.authorityList = response.rows;
        this.authorityTotal = response.total;
        this.authorityLoading = false;
      });
    },

    /** 获取监管机构选项 */
    getAuthorityOptions() {
      getActiveAuthorities().then(response => {
        this.authorityOptions = response.data;
      });
    },

    /** 取消按钮 */
    cancelAuthority() {
      this.authorityOpen = false;
      this.resetAuthorityForm();
    },

    /** 表单重置 */
    resetAuthorityForm() {
      this.authorityForm = {
        authorityId: null,
        authorityCode: null,
        authorityName: null,
        authorityNameEng: null,
        authorityType: null,
        countryCode: null,
        jurisdiction: null,
        contactPerson: null,
        contactPhone: null,
        contactEmail: null,
        address: null,
        website: null,
        isActive: 1,
        remark: null
      };
      this.resetForm("authorityForm");
    },

    /** 搜索按钮操作 */
    handleAuthorityQuery() {
      this.authorityQueryParams.pageNum = 1;
      this.getAuthorityList();
    },

    /** 重置按钮操作 */
    resetAuthorityQuery() {
      this.resetForm("authorityQueryForm");
      this.handleAuthorityQuery();
    },

    /** 多选框选中数据 */
    handleAuthoritySelectionChange(selection) {
      this.authorityIds = selection.map(item => item.authorityId);
      this.authoritySingle = selection.length !== 1;
      this.authorityMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAuthorityAdd() {
      this.resetAuthorityForm();
      this.authorityOpen = true;
      this.authorityTitle = "添加监管机构";
    },

    /** 修改按钮操作 */
    handleAuthorityUpdate(row) {
      this.resetAuthorityForm();
      const authorityId = row.authorityId || this.authorityIds;
      getAuthorityById(authorityId).then(response => {
        this.authorityForm = response.data;
        this.authorityOpen = true;
        this.authorityTitle = "修改监管机构";
      });
    },

    /** 提交按钮 */
    submitAuthorityForm() {
      this.$refs["authorityForm"].validate(valid => {
        if (valid) {
          if (this.authorityForm.authorityId != null) {
            updateAuthority(this.authorityForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.authorityOpen = false;
              this.getAuthorityList();
            });
          } else {
            addAuthority(this.authorityForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.authorityOpen = false;
              this.getAuthorityList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleAuthorityDelete(row) {
      const authorityIds = row.authorityId || this.authorityIds;
      this.$modal.confirm('是否确认删除监管机构编号为"' + authorityIds + '"的数据项？').then(function() {
        return delAuthority(authorityIds);
      }).then(() => {
        this.getAuthorityList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 状态修改 */
    handleAuthorityStatusChange(row) {
      let text = row.isActive === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.authorityName + '"监管机构吗？').then(function() {
        return toggleAuthorityStatus(row.authorityId, row.isActive);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isActive = row.isActive === 0 ? 1 : 0;
      });
    },

    /** 导出按钮操作 */
    handleAuthorityExport() {
      this.download('globalTreasurer/regulatory/authority/export', {
        ...this.authorityQueryParams
      }, `authority_${new Date().getTime()}.xlsx`)
    },

    // ==================== 报告模板管理方法 ====================
    /** 查询报告模板列表 */
    getTemplateList() {
      this.templateLoading = true;
      getTemplateList(this.templateQueryParams).then(response => {
        this.templateList = response.rows;
        this.templateTotal = response.total;
        this.templateLoading = false;
      });
    },

    /** 取消按钮 */
    cancelTemplate() {
      this.templateOpen = false;
      this.resetTemplateForm();
    },

    /** 表单重置 */
    resetTemplateForm() {
      this.templateForm = {
        templateId: null,
        templateCode: null,
        templateName: null,
        templateNameEng: null,
        authorityId: null,
        reportFrequency: null,
        reportFormat: null,
        templateStructure: null,
        dataSourceConfig: null,
        validationRules: null,
        version: null,
        effectiveDate: null,
        expiryDate: null,
        isEnabled: 1,
        remark: null
      };
      this.resetForm("templateForm");
    },

    /** 搜索按钮操作 */
    handleTemplateQuery() {
      this.templateQueryParams.pageNum = 1;
      this.getTemplateList();
    },

    /** 重置按钮操作 */
    resetTemplateQuery() {
      this.resetForm("templateQueryForm");
      this.handleTemplateQuery();
    },

    /** 多选框选中数据 */
    handleTemplateSelectionChange(selection) {
      this.templateIds = selection.map(item => item.templateId);
      this.templateSingle = selection.length !== 1;
      this.templateMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleTemplateAdd() {
      this.resetTemplateForm();
      this.templateOpen = true;
      this.templateTitle = "添加报告模板";
    },

    /** 修改按钮操作 */
    handleTemplateUpdate(row) {
      this.resetTemplateForm();
      const templateId = row.templateId || this.templateIds;
      getTemplateById(templateId).then(response => {
        this.templateForm = response.data;
        this.templateOpen = true;
        this.templateTitle = "修改报告模板";
      });
    },

    /** 提交按钮 */
    submitTemplateForm() {
      this.$refs["templateForm"].validate(valid => {
        if (valid) {
          if (this.templateForm.templateId != null) {
            updateTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.templateOpen = false;
              this.getTemplateList();
            });
          } else {
            addTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.templateOpen = false;
              this.getTemplateList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleTemplateDelete(row) {
      const templateIds = row.templateId || this.templateIds;
      this.$modal.confirm('是否确认删除报告模板编号为"' + templateIds + '"的数据项？').then(function() {
        return delTemplate(templateIds);
      }).then(() => {
        this.getTemplateList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 状态修改 */
    handleTemplateStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.templateName + '"报告模板吗？').then(function() {
        return toggleTemplateStatus(row.templateId, row.isEnabled);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
      });
    },

    /** 复制模板 */
    handleTemplateCopy(row) {
      this.$prompt('请输入新模板编码', '复制模板', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z0-9_-]+$/,
        inputErrorMessage: '模板编码格式不正确'
      }).then(({ value }) => {
        const newTemplateName = row.templateName + '_副本';
        copyTemplate(row.templateId, value, newTemplateName).then(response => {
          this.$modal.msgSuccess("复制成功");
          this.getTemplateList();
        });
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.authority-container,
.template-container,
.report-container,
.compliance-container {
  margin-top: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.el-table {
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}

.el-alert {
  margin: 20px 0;
}
</style>
