<template>
  <div class="travel-archive-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="档案名称" prop="archiveName">
          <el-input
            v-model="searchForm.archiveName"
            placeholder="请输入档案名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="档案类型" prop="archiveType">
          <el-select
            v-model="searchForm.archiveType"
            placeholder="请选择档案类型"
            clearable
            style="width: 150px"
          >
            <el-option label="酒店" value="HOTEL" />
            <el-option label="航空公司" value="AIRLINE" />
            <el-option label="火车" value="TRAIN" />
            <el-option label="出租车" value="TAXI" />
            <el-option label="租车" value="CAR_RENTAL" />
            <el-option label="餐饮" value="RESTAURANT" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="archiveCode" label="档案编码" width="150" />
        <el-table-column prop="archiveName" label="档案名称" min-width="200" />
        <el-table-column prop="archiveType" label="档案类型" width="120">
          <template slot-scope="scope">
            {{ getArchiveTypeName(scope.row.archiveType) }}
          </template>
        </el-table-column>
        <el-table-column prop="providerName" label="供应商名称" min-width="180" />
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="140" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="starLevel" label="星级" width="80">
          <template slot-scope="scope">
            <el-rate
              v-model="scope.row.starLevel"
              :max="5"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}星"
            />
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleViewPrices(scope.row)">价格</el-button>
            <el-button size="mini" type="success" @click="handleViewAgreements(scope.row)">协议</el-button>
            <el-button size="mini" type="warning" @click="handleEvaluation(scope.row)">评价</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="档案编码" prop="archiveCode">
              <el-input v-model="formData.archiveCode" placeholder="请输入档案编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="档案名称" prop="archiveName">
              <el-input v-model="formData.archiveName" placeholder="请输入档案名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="档案类型" prop="archiveType">
              <el-select v-model="formData.archiveType" placeholder="请选择档案类型" style="width: 100%">
                <el-option label="酒店" value="HOTEL" />
                <el-option label="航空公司" value="AIRLINE" />
                <el-option label="火车" value="TRAIN" />
                <el-option label="出租车" value="TAXI" />
                <el-option label="租车" value="CAR_RENTAL" />
                <el-option label="餐饮" value="RESTAURANT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="providerName">
              <el-input v-model="formData.providerName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商编码" prop="providerCode">
              <el-input v-model="formData.providerCode" placeholder="请输入供应商编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="星级评定" prop="starLevel">
              <el-rate v-model="formData.starLevel" :max="5" show-text />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="formData.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="isEnabled">
              <el-switch v-model="formData.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="营业执照" prop="businessLicense">
              <el-input v-model="formData.businessLicense" placeholder="请输入营业执照号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税号" prop="taxNumber">
              <el-input v-model="formData.taxNumber" placeholder="请输入税号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="银行账户" prop="bankAccount">
              <el-input v-model="formData.bankAccount" placeholder="请输入银行账户" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankName">
              <el-input v-model="formData.bankName" placeholder="请输入开户银行" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="档案描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入档案描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 价格信息对话框 -->
    <el-dialog
      :title="`价格信息 - ${currentPriceRow ? currentPriceRow.archiveName : ''}`"
      :visible.sync="priceDialogVisible"
      width="900px"
    >
      <div style="margin-bottom: 15px;">
        <el-button type="primary" size="small" @click="handleAddPrice">新增价格</el-button>
      </div>
      <el-table
        :data="priceList"
        v-loading="priceLoading"
        border
        style="width: 100%"
      >
        <el-table-column prop="priceType" label="价格类型" width="120" />
        <el-table-column prop="priceDescription" label="价格说明" min-width="150" />
        <el-table-column prop="unitPrice" label="单价" width="100">
          <template slot-scope="scope">
            {{ scope.row.unitPrice ? `¥${scope.row.unitPrice}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="currency" label="币种" width="80" />
        <el-table-column prop="effectiveDate" label="生效日期" width="110" />
        <el-table-column prop="expiryDate" label="失效日期" width="110" />
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditPrice(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeletePrice(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="priceDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 价格表单对话框 -->
    <el-dialog
      :title="priceForm.priceId ? '编辑价格' : '新增价格'"
      :visible.sync="priceFormVisible"
      width="600px"
      append-to-body
    >
      <el-form :model="priceForm" label-width="100px">
        <el-form-item label="价格类型" required>
          <el-select v-model="priceForm.priceType" placeholder="请选择价格类型" style="width: 100%">
            <el-option label="标准间" value="STANDARD_ROOM" />
            <el-option label="豪华间" value="DELUXE_ROOM" />
            <el-option label="套房" value="SUITE" />
            <el-option label="经济舱" value="ECONOMY_CLASS" />
            <el-option label="商务舱" value="BUSINESS_CLASS" />
            <el-option label="头等舱" value="FIRST_CLASS" />
            <el-option label="二等座" value="SECOND_CLASS_SEAT" />
            <el-option label="一等座" value="FIRST_CLASS_SEAT" />
            <el-option label="商务座" value="BUSINESS_SEAT" />
            <el-option label="起步价" value="STARTING_PRICE" />
            <el-option label="公里价" value="PER_KM_PRICE" />
            <el-option label="日租" value="DAILY_RENTAL" />
            <el-option label="周租" value="WEEKLY_RENTAL" />
            <el-option label="月租" value="MONTHLY_RENTAL" />
            <el-option label="人均消费" value="PER_CAPITA" />
            <el-option label="包间费" value="PRIVATE_ROOM" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格说明">
          <el-input v-model="priceForm.priceDescription" placeholder="请输入价格说明" />
        </el-form-item>
        <el-form-item label="单价" required>
          <el-input-number v-model="priceForm.unitPrice" :precision="2" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="priceForm.currency" placeholder="请选择币种" style="width: 100%">
            <el-option label="人民币" value="CNY" />
            <el-option label="美元" value="USD" />
            <el-option label="欧元" value="EUR" />
            <el-option label="日元" value="JPY" />
          </el-select>
        </el-form-item>
        <el-form-item label="生效日期" required>
          <el-date-picker
            v-model="priceForm.effectiveDate"
            type="date"
            placeholder="选择生效日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="失效日期">
          <el-date-picker
            v-model="priceForm.expiryDate"
            type="date"
            placeholder="选择失效日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="priceForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="priceFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePrice">保存</el-button>
      </div>
    </el-dialog>

    <!-- 合作协议对话框 -->
    <el-dialog
      :title="`合作协议 - ${currentAgreementRow ? currentAgreementRow.archiveName : ''}`"
      :visible.sync="agreementDialogVisible"
      width="1000px"
    >
      <div style="margin-bottom: 15px;">
        <el-button type="primary" size="small" @click="handleAddAgreement">新增协议</el-button>
      </div>
      <el-table
        :data="agreementList"
        v-loading="agreementLoading"
        border
        style="width: 100%"
      >
        <el-table-column prop="agreementName" label="协议名称" min-width="150" />
        <el-table-column prop="agreementType" label="协议类型" width="120" />
        <el-table-column prop="signDate" label="签订日期" width="110" />
        <el-table-column prop="effectiveDate" label="生效日期" width="110" />
        <el-table-column prop="expiryDate" label="到期日期" width="110" />
        <el-table-column prop="agreementAmount" label="协议金额" width="120">
          <template slot-scope="scope">
            {{ scope.row.agreementAmount ? `¥${scope.row.agreementAmount}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditAgreement(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteAgreement(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="agreementDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 协议表单对话框 -->
    <el-dialog
      :title="agreementForm.agreementId ? '编辑协议' : '新增协议'"
      :visible.sync="agreementFormVisible"
      width="700px"
      append-to-body
    >
      <el-form :model="agreementForm" label-width="100px">
        <el-form-item label="协议名称" required>
          <el-input v-model="agreementForm.agreementName" placeholder="请输入协议名称" />
        </el-form-item>
        <el-form-item label="协议类型" required>
          <el-select v-model="agreementForm.agreementType" placeholder="请选择协议类型" style="width: 100%">
            <el-option label="服务协议" value="SERVICE" />
            <el-option label="价格协议" value="PRICE" />
            <el-option label="框架协议" value="FRAMEWORK" />
            <el-option label="补充协议" value="SUPPLEMENTARY" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签订日期" required>
              <el-date-picker
                v-model="agreementForm.signDate"
                type="date"
                placeholder="选择签订日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协议金额">
              <el-input-number v-model="agreementForm.agreementAmount" :precision="2" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生效日期" required>
              <el-date-picker
                v-model="agreementForm.effectiveDate"
                type="date"
                placeholder="选择生效日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期">
              <el-date-picker
                v-model="agreementForm.expiryDate"
                type="date"
                placeholder="选择到期日期"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="付款条款">
          <el-input v-model="agreementForm.paymentTerms" placeholder="请输入付款条款" />
        </el-form-item>
        <el-form-item label="协议内容">
          <el-input
            v-model="agreementForm.agreementContent"
            type="textarea"
            :rows="4"
            placeholder="请输入协议内容"
          />
        </el-form-item>
        <el-form-item label="附件地址">
          <el-input v-model="agreementForm.attachmentUrl" placeholder="请输入附件URL" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="agreementFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAgreement">保存</el-button>
      </div>
    </el-dialog>

    <!-- 评价对话框 -->
    <el-dialog
      title="商旅档案评价"
      :visible.sync="evaluationDialogVisible"
      width="600px"
    >
      <el-form :model="evaluationForm" ref="evaluationFormRef" label-width="120px">
        <el-form-item label="服务评分" prop="serviceScore" :rules="[{required: true, message: '请进行服务评分'}]">
          <el-rate v-model="evaluationForm.serviceScore" :max="5" show-text />
        </el-form-item>
        <el-form-item label="价格评分" prop="priceScore" :rules="[{required: true, message: '请进行价格评分'}]">
          <el-rate v-model="evaluationForm.priceScore" :max="5" show-text />
        </el-form-item>
        <el-form-item label="环境评分" prop="environmentScore" :rules="[{required: true, message: '请进行环境评分'}]">
          <el-rate v-model="evaluationForm.environmentScore" :max="5" show-text />
        </el-form-item>
        <el-form-item label="评价内容" prop="evaluationContent">
          <el-input
            v-model="evaluationForm.evaluationContent"
            type="textarea"
            :rows="4"
            placeholder="请输入评价内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="evaluationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmEvaluation" :loading="evaluationLoading">提交评价</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { travelArchiveApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'TravelArchive',
  data() {
    return {
      loading: false,
      saveLoading: false,
      evaluationLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        archiveName: '',
        archiveType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增商旅档案',
      formData: {
        archiveId: null,
        archiveCode: '',
        archiveName: '',
        archiveType: '',
        providerName: '',
        providerCode: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        starLevel: 3,
        isEnabled: true,
        businessLicense: '',
        taxNumber: '',
        bankAccount: '',
        bankName: '',
        description: ''
      },
      formRules: {
        archiveCode: [
          { required: true, message: '请输入档案编码', trigger: 'blur' }
        ],
        archiveName: [
          { required: true, message: '请输入档案名称', trigger: 'blur' }
        ],
        archiveType: [
          { required: true, message: '请选择档案类型', trigger: 'change' }
        ],
        providerName: [
          { required: true, message: '请输入供应商名称', trigger: 'blur' }
        ],
        contactPerson: [
          { required: true, message: '请输入联系人', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' }
        ]
      },
      evaluationDialogVisible: false,
      evaluationForm: {
        serviceScore: 5,
        priceScore: 5,
        environmentScore: 5,
        evaluationContent: ''
      },
      currentEvaluationRow: null,
      // 价格信息对话框
      priceDialogVisible: false,
      currentPriceRow: null,
      priceList: [],
      priceLoading: false,
      priceFormVisible: false,
      priceForm: {
        priceId: null,
        priceType: '',
        priceDescription: '',
        unitPrice: null,
        currency: 'CNY',
        effectiveDate: '',
        expiryDate: '',
        remark: ''
      },
      // 合作协议对话框
      agreementDialogVisible: false,
      currentAgreementRow: null,
      agreementList: [],
      agreementLoading: false,
      agreementFormVisible: false,
      agreementForm: {
        agreementId: null,
        agreementName: '',
        agreementType: '',
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        agreementAmount: null,
        paymentTerms: '',
        agreementContent: '',
        attachmentUrl: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    getArchiveTypeName(type) {
      const typeMap = {
        'HOTEL': '酒店',
        'AIRLINE': '航空公司',
        'TRAIN': '火车',
        'TAXI': '出租车',
        'CAR_RENTAL': '租车',
        'RESTAURANT': '餐饮'
      }
      return typeMap[type] || type
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await travelArchiveApi.getList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增商旅档案'
      this.formData = {
        archiveId: null,
        archiveCode: '',
        archiveName: '',
        archiveType: '',
        providerName: '',
        providerCode: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        starLevel: 3,
        isEnabled: true,
        businessLicense: '',
        taxNumber: '',
        bankAccount: '',
        bankName: '',
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑商旅档案'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await travelArchiveApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该商旅档案吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await travelArchiveApi.delete(row.archiveId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的商旅档案吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const archiveIds = this.multipleSelection.map(item => item.archiveId)
        const response = await travelArchiveApi.batchDelete(archiveIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    async handleToggleStatus(row) {
      try {
        // 将布尔值转换为整数：true -> 1, false -> 0
        const newStatus = row.isEnabled ? 0 : 1
        const response = await travelArchiveApi.updateStatus(row.archiveId, newStatus)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      }
    },
    handleViewPrices(row) {
      // 打开价格信息对话框
      this.currentPriceRow = row
      this.priceDialogVisible = true
      this.loadPriceList(row.archiveId)
    },
    async loadPriceList(archiveId) {
      this.priceLoading = true
      try {
        const response = await travelArchiveApi.getPrices(archiveId)
        if (response.code === 1) {
          this.priceList = response.data || []
        } else {
          this.$message.error(response.msg || '查询价格信息失败')
        }
      } catch (error) {
        this.$message.error('查询价格信息失败：' + error.message)
      } finally {
        this.priceLoading = false
      }
    },
    handleAddPrice() {
      this.priceFormVisible = true
      this.priceForm = {
        priceId: null,
        priceType: '',
        priceDescription: '',
        unitPrice: null,
        currency: 'CNY',
        effectiveDate: '',
        expiryDate: '',
        remark: ''
      }
    },
    handleEditPrice(row) {
      this.priceFormVisible = true
      this.priceForm = { ...row }
    },
    async handleSavePrice() {
      try {
        const data = {
          ...this.priceForm,
          archiveId: this.currentPriceRow.archiveId
        }
        const response = await travelArchiveApi.savePrices(this.currentPriceRow.archiveId, [data])
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.priceFormVisible = false
          this.loadPriceList(this.currentPriceRow.archiveId)
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },
    async handleDeletePrice(row) {
      try {
        await this.$confirm('确定要删除这条价格信息吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // TODO: 调用删除价格接口
        this.$message.success('删除成功')
        this.loadPriceList(this.currentPriceRow.archiveId)
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    handleViewAgreements(row) {
      // 打开合作协议对话框
      this.currentAgreementRow = row
      this.agreementDialogVisible = true
      this.loadAgreementList(row.archiveId)
    },
    async loadAgreementList(archiveId) {
      this.agreementLoading = true
      try {
        const response = await travelArchiveApi.getAgreements(archiveId)
        if (response.code === 1) {
          this.agreementList = response.data || []
        } else {
          this.$message.error(response.msg || '查询协议信息失败')
        }
      } catch (error) {
        this.$message.error('查询协议信息失败：' + error.message)
      } finally {
        this.agreementLoading = false
      }
    },
    handleAddAgreement() {
      this.agreementFormVisible = true
      this.agreementForm = {
        agreementId: null,
        agreementName: '',
        agreementType: '',
        signDate: '',
        effectiveDate: '',
        expiryDate: '',
        agreementAmount: null,
        paymentTerms: '',
        agreementContent: '',
        attachmentUrl: ''
      }
    },
    handleEditAgreement(row) {
      this.agreementFormVisible = true
      this.agreementForm = { ...row }
    },
    async handleSaveAgreement() {
      try {
        const data = {
          ...this.agreementForm,
          archiveId: this.currentAgreementRow.archiveId
        }
        const response = await travelArchiveApi.saveAgreements(this.currentAgreementRow.archiveId, [data])
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.agreementFormVisible = false
          this.loadAgreementList(this.currentAgreementRow.archiveId)
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },
    async handleDeleteAgreement(row) {
      try {
        await this.$confirm('确定要删除这条协议信息吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        // TODO: 调用删除协议接口
        this.$message.success('删除成功')
        this.loadAgreementList(this.currentAgreementRow.archiveId)
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    handleEvaluation(row) {
      this.currentEvaluationRow = row
      this.evaluationForm = {
        serviceScore: 5,
        priceScore: 5,
        environmentScore: 5,
        evaluationContent: ''
      }
      this.evaluationDialogVisible = true
    },
    async handleConfirmEvaluation() {
      try {
        await this.$refs.evaluationFormRef.validate()
        this.evaluationLoading = true
        const response = await travelArchiveApi.evaluation(this.currentEvaluationRow.archiveId, this.evaluationForm)
        if (response.code === 1) {
          this.$message.success('评价提交成功')
          this.evaluationDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '评价提交失败')
        }
      } catch (error) {
        this.$message.error('评价提交失败：' + error.message)
      } finally {
        this.evaluationLoading = false
      }
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    }
  }
}
</script>

<style scoped>
.travel-archive-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
