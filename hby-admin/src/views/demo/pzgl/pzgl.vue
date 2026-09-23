<template>
  <div class="ticket-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>票证管理</h2>
      <p>管理各类金融票证，包括银行承兑汇票、商业承兑汇票、支票等</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="票据编号">
          <el-input v-model="searchForm.ticketNumber" placeholder="请输入票据编号" clearable />
        </el-form-item>
        <el-form-item label="票据类型">
          <el-select v-model="searchForm.ticketType" placeholder="请选择票据类型" clearable>
            <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
            <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
            <el-option label="支票" value="CHECK" />
            <el-option label="本票" value="PROMISSORY_NOTE" />
            <el-option label="汇票" value="DRAFT" />
            <el-option label="信用证" value="LETTER_OF_CREDIT" />
          </el-select>
        </el-form-item>
        <el-form-item label="票据状态">
          <el-select v-model="searchForm.status" placeholder="请选择票据状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审核" value="PENDING_APPROVAL" />
            <el-option label="已签发" value="ISSUED" />
            <el-option label="已背书" value="ENDORSED" />
            <el-option label="已贴现" value="DISCOUNTED" />
            <el-option label="已托收" value="COLLECTED" />
            <el-option label="已兑付" value="PAID" />
            <el-option label="已退票" value="RETURNED" />
            <el-option label="已作废" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="出票人">
          <el-input v-model="searchForm.drawer" placeholder="请输入出票人" clearable />
        </el-form-item>
        <el-form-item label="收款人">
          <el-input v-model="searchForm.payee" placeholder="请输入收款人" clearable />
        </el-form-item>
        <el-form-item label="到期日期">
          <el-date-picker
            v-model="searchForm.maturityDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增票据</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-check" size="mini" :disabled="multiple" @click="handleBatchApprove">批量审核</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="ticketList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        stripe
        border
        height="500"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="票据编号" prop="ticketNumber" width="150" show-overflow-tooltip />
        <el-table-column label="票据类型" prop="ticketType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTicketTypeTag(scope.row.ticketType)" size="mini">
              {{ getTicketTypeText(scope.row.ticketType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="票面金额" prop="faceValue" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.faceValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currency" width="80" align="center" />
        <el-table-column label="出票人" prop="drawer" width="150" show-overflow-tooltip />
        <el-table-column label="收款人" prop="payee" width="150" show-overflow-tooltip />
        <el-table-column label="出票日期" prop="issueDate" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.issueDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="到期日期" prop="maturityDate" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getMaturityClass(scope.row.maturityDate)">
              {{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="剩余天数" prop="remainingDays" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getRemainingDaysClass(scope.row.remainingDays)">
              {{ scope.row.remainingDays }}天
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" style="margin-left: 10px">
              <span class="el-dropdown-link">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="approve" v-if="scope.row.status === 'PENDING_APPROVAL'" icon="el-icon-check">审核</el-dropdown-item>
                <el-dropdown-item command="endorse" v-if="scope.row.status === 'ISSUED'" icon="el-icon-edit-outline">背书</el-dropdown-item>
                <el-dropdown-item command="discount" v-if="scope.row.status === 'ISSUED'" icon="el-icon-money">贴现</el-dropdown-item>
                <el-dropdown-item command="collect" v-if="scope.row.status === 'ISSUED'" icon="el-icon-collection">托收</el-dropdown-item>
                <el-dropdown-item command="pay" v-if="scope.row.status === 'COLLECTED'" icon="el-icon-success">兑付</el-dropdown-item>
                <el-dropdown-item command="return" v-if="scope.row.status === 'COLLECTED'" icon="el-icon-refresh-left">退票</el-dropdown-item>
                <el-dropdown-item command="cancel" v-if="scope.row.status === 'DRAFT'" icon="el-icon-circle-close">作废</el-dropdown-item>
                <el-dropdown-item command="history" icon="el-icon-time">操作历史</el-dropdown-item>
                <el-dropdown-item command="print" icon="el-icon-printer">打印</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.current"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据编号" prop="ticketNumber">
                  <el-input v-model="form.ticketNumber" placeholder="请输入票据编号" :disabled="form.ticketId != null" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据类型" prop="ticketType">
                  <el-select v-model="form.ticketType" placeholder="请选择票据类型" style="width: 100%">
                    <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
                    <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
                    <el-option label="支票" value="CHECK" />
                    <el-option label="本票" value="PROMISSORY_NOTE" />
                    <el-option label="汇票" value="DRAFT" />
                    <el-option label="信用证" value="LETTER_OF_CREDIT" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票面金额" prop="faceValue">
                  <el-input v-model="form.faceValue" placeholder="请输入票面金额" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="币种" prop="currency">
                  <el-select v-model="form.currency" placeholder="请选择币种" style="width: 100%">
                    <el-option label="人民币" value="CNY" />
                    <el-option label="美元" value="USD" />
                    <el-option label="欧元" value="EUR" />
                    <el-option label="日元" value="JPY" />
                    <el-option label="英镑" value="GBP" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="出票日期" prop="issueDate">
                  <el-date-picker
                    v-model="form.issueDate"
                    type="date"
                    placeholder="选择出票日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期日期" prop="maturityDate">
                  <el-date-picker
                    v-model="form.maturityDate"
                    type="date"
                    placeholder="选择到期日期"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="出票人" prop="drawer">
                  <el-input v-model="form.drawer" placeholder="请输入出票人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收款人" prop="payee">
                  <el-input v-model="form.payee" placeholder="请输入收款人" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="付款人" prop="payer">
                  <el-input v-model="form.payer" placeholder="请输入付款人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="承兑银行" prop="acceptingBank">
                  <el-input v-model="form.acceptingBank" placeholder="请输入承兑银行" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="票据用途">
              <el-input v-model="form.purpose" type="textarea" placeholder="请输入票据用途" :rows="3" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="详细信息" name="detail">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="票据来源" prop="source">
                  <el-select v-model="form.source" placeholder="请选择票据来源" style="width: 100%">
                    <el-option label="自开" value="SELF_ISSUED" />
                    <el-option label="收取" value="RECEIVED" />
                    <el-option label="背书转入" value="ENDORSED_IN" />
                    <el-option label="贴现" value="DISCOUNTED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="票据性质" prop="nature">
                  <el-select v-model="form.nature" placeholder="请选择票据性质" style="width: 100%">
                    <el-option label="融资性" value="FINANCING" />
                    <el-option label="贸易性" value="TRADE" />
                    <el-option label="其他" value="OTHER" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="保证人" prop="guarantor">
                  <el-input v-model="form.guarantor" placeholder="请输入保证人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="保证方式" prop="guaranteeMethod">
                  <el-select v-model="form.guaranteeMethod" placeholder="请选择保证方式" style="width: 100%">
                    <el-option label="一般保证" value="GENERAL_GUARANTEE" />
                    <el-option label="连带保证" value="JOINT_GUARANTEE" />
                    <el-option label="质押担保" value="PLEDGE_GUARANTEE" />
                    <el-option label="抵押担保" value="MORTGAGE_GUARANTEE" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="利率" prop="interestRate">
                  <el-input v-model="form.interestRate" placeholder="请输入利率(%)" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手续费" prop="handlingFee">
                  <el-input v-model="form.handlingFee" placeholder="请输入手续费" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="附件信息" name="attachment">
            <el-upload
              ref="upload"
              :limit="5"
              accept=".jpg,.jpeg,.png,.pdf,.doc,.docx"
              :action="upload.url"
              :headers="upload.headers"
              :file-list="upload.fileList"
              :on-progress="handleFileUploadProgress"
              :on-success="handleFileSuccess"
              :before-upload="beforeUpload"
              multiple
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf/doc文件，且不超过10MB</div>
            </el-upload>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="票据详情" :visible.sync="detailOpen" width="1000px" append-to-body>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="票据编号">{{ detailData.ticketNumber }}</el-descriptions-item>
        <el-descriptions-item label="票据类型">
          <el-tag :type="getTicketTypeTag(detailData.ticketType)" size="mini">
            {{ getTicketTypeText(detailData.ticketType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="票面金额">{{ formatAmount(detailData.faceValue) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detailData.currency }}</el-descriptions-item>
        <el-descriptions-item label="出票人">{{ detailData.drawer }}</el-descriptions-item>
        <el-descriptions-item label="收款人">{{ detailData.payee }}</el-descriptions-item>
        <el-descriptions-item label="付款人">{{ detailData.payer }}</el-descriptions-item>
        <el-descriptions-item label="承兑银行">{{ detailData.acceptingBank }}</el-descriptions-item>
        <el-descriptions-item label="出票日期">{{ parseTime(detailData.issueDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ parseTime(detailData.maturityDate, '{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="剩余天数">{{ detailData.remainingDays }}天</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="票据来源">{{ getSourceText(detailData.source) }}</el-descriptions-item>
        <el-descriptions-item label="票据性质">{{ getNatureText(detailData.nature) }}</el-descriptions-item>
        <el-descriptions-item label="保证人">{{ detailData.guarantor }}</el-descriptions-item>
        <el-descriptions-item label="利率">{{ detailData.interestRate }}%</el-descriptions-item>
        <el-descriptions-item label="手续费">{{ formatAmount(detailData.handlingFee) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
        <el-descriptions-item label="票据用途" :span="3">{{ detailData.purpose }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 背书对话框 -->
    <el-dialog title="票据背书" :visible.sync="endorseOpen" width="600px" append-to-body>
      <el-form ref="endorseForm" :model="endorseForm" :rules="endorseRules" label-width="120px">
        <el-form-item label="被背书人" prop="endorsee">
          <el-input v-model="endorseForm.endorsee" placeholder="请输入被背书人" />
        </el-form-item>
        <el-form-item label="背书日期" prop="endorseDate">
          <el-date-picker
            v-model="endorseForm.endorseDate"
            type="date"
            placeholder="选择背书日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="背书用途" prop="endorsePurpose">
          <el-select v-model="endorseForm.endorsePurpose" placeholder="请选择背书用途" style="width: 100%">
            <el-option label="转让" value="TRANSFER" />
            <el-option label="质押" value="PLEDGE" />
            <el-option label="委托收款" value="COLLECTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="背书说明">
          <el-input v-model="endorseForm.endorseRemark" type="textarea" placeholder="请输入背书说明" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEndorseForm">确 定</el-button>
        <el-button @click="endorseOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils'
import { getCommercialBillPage, getCommercialBill, createCommercialBill, updateCommercialBill, deleteCommercialBill } from '@/api/globalTreasurer/pzgl'

export default {
  name: 'TicketManagement',
  data() {
    return {
      // 加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 票据列表
      ticketList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 是否显示背书弹出层
      endorseOpen: false,
      // 详情数据
      detailData: {},
      // 活动标签页
      activeTab: 'basic',
      // 上传参数
      upload: {
        headers: { Authorization: 'Bearer ' + this.$store.getters.token },
        url: process.env.VUE_APP_BASE_API + '/ticket/upload',
        fileList: []
      },
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        ticketNumber: null,
        ticketType: null,
        status: null,
        drawer: null,
        payee: null,
        maturityDateStart: null,
        maturityDateEnd: null,
        orgId: this.$store.getters.orgId
      },
      // 搜索表单
      searchForm: {
        ticketNumber: '',
        ticketType: '',
        status: '',
        drawer: '',
        payee: '',
        maturityDateRange: []
      },
      // 表单参数
      form: {},
      // 背书表单
      endorseForm: {},
      // 表单校验
      rules: {
        ticketNumber: [
          { required: true, message: '票据编号不能为空', trigger: 'blur' }
        ],
        ticketType: [
          { required: true, message: '票据类型不能为空', trigger: 'change' }
        ],
        faceValue: [
          { required: true, message: '票面金额不能为空', trigger: 'blur' }
        ],
        currency: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        issueDate: [
          { required: true, message: '出票日期不能为空', trigger: 'change' }
        ],
        maturityDate: [
          { required: true, message: '到期日期不能为空', trigger: 'change' }
        ],
        drawer: [
          { required: true, message: '出票人不能为空', trigger: 'blur' }
        ],
        payee: [
          { required: true, message: '收款人不能为空', trigger: 'blur' }
        ]
      },
      // 背书校验
      endorseRules: {
        endorsee: [
          { required: true, message: '被背书人不能为空', trigger: 'blur' }
        ],
        endorseDate: [
          { required: true, message: '背书日期不能为空', trigger: 'change' }
        ],
        endorsePurpose: [
          { required: true, message: '背书用途不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    /** 查询票据列表 */
    async getList() {
      this.loading = true
      try {
        const response = await getCommercialBillPage(this.queryParams)

        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.records !== undefined) {
            // MyBatis Plus分页格式
            this.ticketList = response.data.records || []
            this.total = response.data.total || 0
          } else if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式
            this.ticketList = response.data.tlist || []
            this.total = response.data.totalRecord || 0
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式
            this.ticketList = response.data.list || []
            this.total = response.data.total || 0
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式
            this.ticketList = response.data || []
            this.total = response.data.length || 0
          } else {
            // 兜底处理
            this.ticketList = []
            this.total = 0
          }
        } else {
          this.$message.error(response.message || '获取数据失败')
          this.ticketList = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取票据列表失败:', error)
        this.$message.error('获取数据失败，请检查网络连接')
        this.ticketList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    /** 搜索按钮操作 */
    handleSearch() {
      this.queryParams.current = 1
      Object.assign(this.queryParams, this.searchForm)
      if (this.searchForm.maturityDateRange && this.searchForm.maturityDateRange.length === 2) {
        this.queryParams.maturityDateStart = this.searchForm.maturityDateRange[0]
        this.queryParams.maturityDateEnd = this.searchForm.maturityDateRange[1]
      }
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.searchForm = {
        ticketNumber: '',
        ticketType: '',
        status: '',
        drawer: '',
        payee: '',
        maturityDateRange: []
      }
      this.queryParams = {
        current: 1,
        size: 10,
        ticketNumber: null,
        ticketType: null,
        status: null,
        drawer: null,
        payee: null,
        maturityDateStart: null,
        maturityDateEnd: null,
        orgId: this.$store.getters.orgId
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增票据'
      this.activeTab = 'basic'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.form = { ...row }
      this.open = true
      this.title = '修改票据'
      this.activeTab = 'basic'
    },
    /** 查看详情 */
    handleView(row) {
      this.detailData = row
      this.detailOpen = true
    },
    /** 导入按钮操作 */
    handleImport() {
      this.$modal.msgSuccess('导入功能')
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      // 上传进度处理
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.fileList = fileList
      this.$modal.msgSuccess('上传成功')
    },
    /** 上传前校验格式和大小 */
    beforeUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$modal.msgError('上传文件大小不能超过 10MB!')
      }
      return isLt10M
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.$modal.msgSuccess(this.form.ticketId ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ticketIds = row.ticketId || this.ids
      this.$modal.confirm('是否确认删除票据编号为"' + ticketIds + '"的数据项？').then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ticket/export', {
        ...this.queryParams
      }, `ticket_${new Date().getTime()}.xlsx`)
    },
    /** 批量审核 */
    handleBatchApprove() {
      this.$modal.confirm('是否确认批量审核所有选中的票据？').then(() => {
        this.$modal.msgSuccess('审核成功')
        this.getList()
      }).catch(() => {})
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.ticketId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    /** 更多操作命令 */
    handleCommand(command, row) {
      switch (command) {
        case 'approve':
          this.handleApprove(row)
          break
        case 'endorse':
          this.handleEndorse(row)
          break
        case 'discount':
          this.handleDiscount(row)
          break
        case 'collect':
          this.handleCollect(row)
          break
        case 'pay':
          this.handlePay(row)
          break
        case 'return':
          this.handleReturn(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
        case 'history':
          this.handleHistory(row)
          break
        case 'print':
          this.handlePrint(row)
          break
      }
    },
    /** 审核票据 */
    handleApprove(row) {
      this.$modal.confirm('是否确认审核通过票据"' + row.ticketNumber + '"？').then(() => {
        this.$modal.msgSuccess('审核成功')
        this.getList()
      }).catch(() => {})
    },
    /** 背书票据 */
    handleEndorse(row) {
      this.endorseForm = {
        ticketId: row.ticketId,
        ticketNumber: row.ticketNumber,
        endorsee: '',
        endorseDate: '',
        endorsePurpose: '',
        endorseRemark: ''
      }
      this.endorseOpen = true
    },
    /** 提交背书表单 */
    submitEndorseForm() {
      this.$refs['endorseForm'].validate(valid => {
        if (valid) {
          this.$modal.msgSuccess('背书成功')
          this.endorseOpen = false
          this.getList()
        }
      })
    },
    /** 贴现票据 */
    handleDiscount(row) {
      this.$modal.confirm('是否确认贴现票据"' + row.ticketNumber + '"？').then(() => {
        this.$modal.msgSuccess('贴现成功')
        this.getList()
      }).catch(() => {})
    },
    /** 托收票据 */
    handleCollect(row) {
      this.$modal.confirm('是否确认托收票据"' + row.ticketNumber + '"？').then(() => {
        this.$modal.msgSuccess('托收成功')
        this.getList()
      }).catch(() => {})
    },
    /** 兑付票据 */
    handlePay(row) {
      this.$modal.confirm('是否确认兑付票据"' + row.ticketNumber + '"？').then(() => {
        this.$modal.msgSuccess('兑付成功')
        this.getList()
      }).catch(() => {})
    },
    /** 退票 */
    handleReturn(row) {
      this.$prompt('请输入退票原因', '退票', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$modal.msgSuccess('退票成功')
        this.getList()
      }).catch(() => {})
    },
    /** 作废票据 */
    handleCancel(row) {
      this.$prompt('请输入作废原因', '作废票据', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$modal.msgSuccess('作废成功')
        this.getList()
      }).catch(() => {})
    },
    /** 操作历史 */
    handleHistory(row) {
      this.$modal.msgSuccess('查看操作历史')
    },
    /** 打印票据 */
    handlePrint(row) {
      this.$modal.msgSuccess('打印票据')
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        ticketId: null,
        ticketNumber: null,
        ticketType: null,
        faceValue: null,
        currency: 'CNY',
        drawer: null,
        payee: null,
        payer: null,
        acceptingBank: null,
        issueDate: null,
        maturityDate: null,
        purpose: null,
        source: null,
        nature: null,
        guarantor: null,
        guaranteeMethod: null,
        interestRate: null,
        handlingFee: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.upload.fileList = []
      this.resetForm('form')
    },
    /** 获取票据类型标签 */
    getTicketTypeTag(type) {
      const tagMap = {
        'BANK_ACCEPTANCE': 'primary',
        'COMMERCIAL_ACCEPTANCE': 'success',
        'CHECK': 'info',
        'PROMISSORY_NOTE': 'warning',
        'DRAFT': 'danger',
        'LETTER_OF_CREDIT': 'primary'
      }
      return tagMap[type] || ''
    },
    /** 获取票据类型文本 */
    getTicketTypeText(type) {
      const textMap = {
        'BANK_ACCEPTANCE': '银承',
        'COMMERCIAL_ACCEPTANCE': '商承',
        'CHECK': '支票',
        'PROMISSORY_NOTE': '本票',
        'DRAFT': '汇票',
        'LETTER_OF_CREDIT': '信用证'
      }
      return textMap[type] || type
    },
    /** 获取状态标签 */
    getStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'PENDING_APPROVAL': 'warning',
        'ISSUED': 'primary',
        'ENDORSED': 'success',
        'DISCOUNTED': 'success',
        'COLLECTED': 'warning',
        'PAID': 'success',
        'RETURNED': 'danger',
        'CANCELLED': 'info'
      }
      return tagMap[status] || ''
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'PENDING_APPROVAL': '待审核',
        'ISSUED': '已签发',
        'ENDORSED': '已背书',
        'DISCOUNTED': '已贴现',
        'COLLECTED': '已托收',
        'PAID': '已兑付',
        'RETURNED': '已退票',
        'CANCELLED': '已作废'
      }
      return textMap[status] || status
    },
    /** 获取票据来源文本 */
    getSourceText(source) {
      const textMap = {
        'SELF_ISSUED': '自开',
        'RECEIVED': '收取',
        'ENDORSED_IN': '背书转入',
        'DISCOUNTED': '贴现'
      }
      return textMap[source] || source
    },
    /** 获取票据性质文本 */
    getNatureText(nature) {
      const textMap = {
        'FINANCING': '融资性',
        'TRADE': '贸易性',
        'OTHER': '其他'
      }
      return textMap[nature] || nature
    },
    /** 获取到期日期样式类 */
    getMaturityClass(maturityDate) {
      const now = new Date()
      const maturity = new Date(maturityDate)
      const diffDays = Math.ceil((maturity - now) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'overdue'
      if (diffDays <= 7) return 'urgent'
      if (diffDays <= 30) return 'warning'
      return 'normal'
    },
    /** 获取剩余天数样式类 */
    getRemainingDaysClass(days) {
      if (days < 0) return 'overdue'
      if (days <= 7) return 'urgent'
      if (days <= 30) return 'warning'
      return 'normal'
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.ticket-management-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: bold;
  color: #409eff;
}

.overdue {
  color: #f56c6c;
  font-weight: bold;
}

.urgent {
  color: #e6a23c;
  font-weight: bold;
}

.warning {
  color: #e6a23c;
}

.normal {
  color: #303133;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}

.el-dropdown-link:hover {
  color: #66b1ff;
}
</style>
