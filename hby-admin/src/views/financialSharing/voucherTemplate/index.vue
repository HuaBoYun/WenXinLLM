<template>
  <div class="app-container">
    <el-page-header content="凭证模板管理" @back="goBack" />

    <!-- 查询表单 -->
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form :inline="true" :model="queryForm" @submit.native.prevent>
          <el-form-item label="关键字">
            <el-input
              v-model="queryForm.keyword"
              placeholder="模板编码/名称/描述"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="模板类型">
            <el-select v-model="queryForm.templateType" placeholder="请选择" clearable>
              <el-option
                v-for="item in templateTypes"
                :key="item.typeCode"
                :label="item.typeName"
                :value="item.typeCode"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryForm.status" placeholder="请选择" clearable>
              <el-option label="草稿" value="DRAFT" />
              <el-option label="启用" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">
              查询
            </el-button>
            <el-button icon="el-icon-refresh-left" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>

      <vab-query-form-left-panel>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增模板
        </el-button>
        <el-button
          type="success"
          icon="el-icon-upload2"
          @click="handleImport"
        >
          导入模板
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-download"
          @click="handleExport"
          :disabled="multipleSelection.length === 0"
        >
          导出模板
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleBatchDelete"
          :disabled="multipleSelection.length === 0"
        >
          批量删除
        </el-button>
        <el-button
          type="info"
          icon="el-icon-setting"
          @click="handleBatchUpdateStatus"
          :disabled="multipleSelection.length === 0"
        >
          批量更新状态
        </el-button>
      </vab-query-form-left-panel>
    </vab-query-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :element-loading-text="elementLoadingText"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="模板编码" prop="templateCode" width="120" />
      <el-table-column label="模板名称" prop="templateName" width="150" show-overflow-tooltip />
      <el-table-column label="模板类型" prop="templateType" width="120">
        <template slot-scope="scope">
          <el-tag :type="getTemplateTypeTag(scope.row.templateType)">
            {{ getTemplateTypeName(scope.row.templateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ getStatusName(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="版本" prop="version" width="80" />
      <el-table-column label="使用次数" prop="usedCount" width="80" />
      <el-table-column label="创建人" prop="creatorName" width="100" />
      <el-table-column label="创建时间" prop="createTime" width="150" />
      <el-table-column label="操作" width="280" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="handleView(scope.row)">
            查看
          </el-button>
          <el-button type="text" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button type="text" @click="handleCopy(scope.row)">
            复制
          </el-button>
          <el-button type="text" @click="handleTest(scope.row)">
            测试
          </el-button>
          <el-button type="text" @click="handleVersions(scope.row)">
            版本
          </el-button>
          <el-button type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :page-size="queryForm.pageSize"
      :layout="layout"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 编辑/查看对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="dialogMode === 'view' ? {} : rules"
        label-width="120px"
        class="demo-ruleForm"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="form.templateCode" placeholder="请输入模板编码" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模板名称" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="form.templateType" placeholder="请选择模板类型" :disabled="dialogMode === 'view'">
                <el-option
                  v-for="item in templateTypes"
                  :key="item.typeCode"
                  :label="item.typeName"
                  :value="item.typeCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" :disabled="dialogMode === 'view'">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="凭证类型" prop="voucherType">
          <el-select v-model="form.voucherType" placeholder="请选择凭证类型" :disabled="dialogMode === 'view'">
            <el-option label="记账凭证" value="ACCOUNTING" />
            <el-option label="收款凭证" value="RECEIPT" />
            <el-option label="付款凭证" value="PAYMENT" />
            <el-option label="转账凭证" value="TRANSFER" />
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入模板描述"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <el-form-item label="摘要规则" prop="summaryRule">
          <el-input
            v-model="form.summaryRule"
            placeholder="例如：${BUSINESS_TYPE} - ${CUSTOMER_NAME}"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="借方科目" prop="debitSubject">
              <el-input v-model="form.debitSubject" placeholder="请输入借方科目编码" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贷方科目" prop="creditSubject">
              <el-input v-model="form.creditSubject" placeholder="请输入贷方科目编码，多个用逗号分隔" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="金额字段" prop="amountField">
              <el-input v-model="form.amountField" placeholder="请输入金额字段名" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种字段" prop="currencyField">
              <el-input v-model="form.currencyField" placeholder="请输入币种字段名" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="生成条件">
          <el-input
            v-model="form.generateCondition"
            type="textarea"
            placeholder="请输入生成条件SQL表达式"
            :disabled="dialogMode === 'view'"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序规则">
              <el-input v-model="form.sortRule" placeholder="例如：ORDER_DATE ASC" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分组规则">
              <el-input v-model="form.groupRule" placeholder="例如：CUSTOMER_ID" :disabled="dialogMode === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 查看模式下显示额外信息 -->
        <template v-if="dialogMode === 'view'">
          <el-divider content-position="left">其他信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="版本">
                <el-input :value="form.version" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="使用次数">
                <el-input :value="form.usedCount" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="创建人">
                <el-input :value="form.creatorName" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间">
                <el-input :value="form.createTime" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="更新时间">
                <el-input :value="form.updateTime" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ dialogMode === 'view' ? '关闭' : '取消' }}</el-button>
        <el-button v-if="dialogMode !== 'view'" type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制凭证模板"
      :visible.sync="copyDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="copyForm"
        :model="copyForm"
        :rules="copyRules"
        label-width="120px"
      >
        <el-form-item label="源模板">
          <el-input :value="copyForm.sourceTemplateName" disabled />
        </el-form-item>
        <el-form-item label="新模板编码" prop="newTemplateCode">
          <el-input v-model="copyForm.newTemplateCode" placeholder="请输入新模板编码" />
        </el-form-item>
        <el-form-item label="新模板名称" prop="newTemplateName">
          <el-input v-model="copyForm.newTemplateName" placeholder="请输入新模板名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCopyConfirm">确认复制</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入凭证模板" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".json"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传JSON文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确认导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getVoucherTemplateList,
  getVoucherTemplateById,
  saveOrUpdateVoucherTemplate,
  deleteVoucherTemplate,
  copyVoucherTemplate,
  testVoucherTemplate,
  getTemplateVersions,
  importVoucherTemplate,
  exportVoucherTemplate,
  batchUpdateTemplateStatus,
  getTemplateTypes
} from '@/api/financialSharing/voucherTemplate'

export default {
  name: 'VoucherTemplate',
  data() {
    return {
      listLoading: true,
      elementLoadingText: '正在加载...',
      list: [],
      total: 0,
      layout: 'total, sizes, prev, pager, next, jumper',
      multipleSelection: [],
      queryForm: {
        pageNo: 1,
        pageSize: 10,
        keyword: '',
        templateType: '',
        status: ''
      },
      templateTypes: [],
      dialogVisible: false,
      dialogTitle: '',
      dialogMode: 'add', // add, edit, view
      form: {
        templateId: null,
        templateCode: '',
        templateName: '',
        templateType: '',
        status: 'DRAFT',
        description: '',
        voucherType: '',
        summaryRule: '',
        debitSubject: '',
        creditSubject: '',
        amountField: '',
        currencyField: '',
        generateCondition: '',
        sortRule: '',
        groupRule: ''
      },
      // 复制对话框
      copyDialogVisible: false,
      copyForm: {
        sourceTemplateId: null,
        sourceTemplateName: '',
        newTemplateCode: '',
        newTemplateName: ''
      },
      copyRules: {
        newTemplateCode: [
          { required: true, message: '请输入新模板编码', trigger: 'blur' }
        ],
        newTemplateName: [
          { required: true, message: '请输入新模板名称', trigger: 'blur' }
        ]
      },
      rules: {
        templateCode: [
          { required: true, message: '请输入模板编码', trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        voucherType: [
          { required: true, message: '请选择凭证类型', trigger: 'change' }
        ],
        summaryRule: [
          { required: true, message: '请输入摘要规则', trigger: 'blur' }
        ],
        debitSubject: [
          { required: true, message: '请输入借方科目', trigger: 'blur' }
        ],
        creditSubject: [
          { required: true, message: '请输入贷方科目', trigger: 'blur' }
        ],
        amountField: [
          { required: true, message: '请输入金额字段', trigger: 'blur' }
        ]
      },
      importDialogVisible: false,
      fileList: []
    }
  },
  created() {
    this.fetchData()
    this.fetchTemplateTypes()
  },
  methods: {
    async fetchData() {
      this.listLoading = true
      const { data } = await getVoucherTemplateList(this.queryForm)
      this.list = data.tlist
      this.total = data.totalRecord
      this.listLoading = false
    },
    async fetchTemplateTypes() {
      const { data } = await getTemplateTypes()
      this.templateTypes = data
    },
    handleQuery() {
      this.queryForm.pageNo = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        pageNo: 1,
        pageSize: 10,
        keyword: '',
        templateType: '',
        status: ''
      }
      this.fetchData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增凭证模板'
      this.dialogMode = 'add'
      this.form = {
        templateId: null,
        templateCode: '',
        templateName: '',
        templateType: '',
        status: 'DRAFT',
        description: '',
        voucherType: '',
        summaryRule: '',
        debitSubject: '',
        creditSubject: '',
        amountField: '',
        currencyField: '',
        generateCondition: '',
        sortRule: '',
        groupRule: ''
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑凭证模板'
      this.dialogMode = 'edit'
      try {
        const response = await getVoucherTemplateById(row.templateId)
        if (response.code === 200 || response.code === 1) {
          this.form = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      }
    },
    async handleView(row) {
      this.dialogTitle = '查看凭证模板'
      this.dialogMode = 'view'
      try {
        const response = await getVoucherTemplateById(row.templateId)
        if (response.code === 200 || response.code === 1) {
          this.form = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
        this.$message.error('获取模板详情失败')
      }
    },
    handleCopy(row) {
      // 打开复制对话框
      this.copyForm = {
        sourceTemplateId: row.templateId,
        sourceTemplateName: row.templateName,
        newTemplateCode: row.templateCode + '_COPY',
        newTemplateName: row.templateName + '_副本'
      }
      this.copyDialogVisible = true
    },
    async handleCopyConfirm() {
      this.$refs.copyForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await copyVoucherTemplate(this.copyForm.sourceTemplateId, {
              newTemplateCode: this.copyForm.newTemplateCode,
              newTemplateName: this.copyForm.newTemplateName
            })
            if (response.code === 200 || response.code === 1) {
              this.$message.success('模板复制成功')
              this.copyDialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          } catch (error) {
            console.error('复制失败:', error)
            this.$message.error('复制失败')
          }
        }
      })
    },
    handleTest(row) {
      // 测试模板逻辑
    },
    handleVersions(row) {
      // 查看版本历史逻辑
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该模板吗？', '提示', {
          type: 'warning'
        })
        await deleteVoucherTemplate(row.templateId)
        this.$message.success('删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要批量删除选中的模板吗？', '提示', {
          type: 'warning'
        })
        // 批量删除逻辑
        this.$message.success('批量删除成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },
    handleBatchUpdateStatus() {
      // 批量更新状态逻辑
    },
    async handleSave() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            await saveOrUpdateVoucherTemplate(this.form)
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    },
    handleDialogClose() {
      this.$refs.form.resetFields()
    },
    handleImport() {
      this.importDialogVisible = true
    },
    handleExport() {
      // 导出逻辑
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    async handleImportConfirm() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      // 导入确认逻辑
    },
    goBack() {
      this.$router.back()
    },
    getTemplateTypeTag(type) {
      const tagMap = {
        SALES: 'success',
        PURCHASE: 'warning',
        EXPENSE: 'danger',
        ASSET: 'info',
        OTHER: ''
      }
      return tagMap[type] || ''
    },
    getTemplateTypeName(type) {
      const item = this.templateTypes.find(item => item.typeCode === type)
      return item ? item.typeName : type
    },
    getStatusTag(status) {
      const tagMap = {
        DRAFT: 'info',
        ACTIVE: 'success',
        INACTIVE: 'danger'
      }
      return tagMap[status] || ''
    },
    getStatusName(status) {
      const nameMap = {
        DRAFT: '草稿',
        ACTIVE: '启用',
        INACTIVE: '停用'
      }
      return nameMap[status] || status
    }
  }
}
</script>

<style scoped>
.el-table {
  margin-top: 20px;
}
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>