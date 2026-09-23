<template>
  <div class="cost-collection-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="归集期间" prop="collectionPeriod">
          <el-date-picker
            v-model="searchForm.collectionPeriod"
            type="month"
            placeholder="请选择归集期间"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="成本中心" prop="costCenterId">
          <el-select
            v-model="searchForm.costCenterId"
            placeholder="请选择成本中心"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.VALUE"
              :label="item.LABEL"
              :value="item.VALUE"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="归集类型" prop="collectionType">
          <el-select
            v-model="searchForm.collectionType"
            placeholder="请选择归集类型"
            clearable
            style="width: 150px"
          >
            <el-option label="直接成本" :value="1" />
            <el-option label="间接成本" :value="2" />
            <el-option label="制造费用" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="归集状态" prop="collectionStatus">
          <el-select
            v-model="searchForm.collectionStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待归集" :value="1" />
            <el-option label="归集中" :value="2" />
            <el-option label="已归集" :value="3" />
            <el-option label="已审核" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleStartCollection">开始归集</el-button>
      <el-button type="success" @click="handleBatchAudit" :disabled="!multipleSelection.length">
        批量审核
      </el-button>
      <el-button type="warning" @click="handleAutoCollection">自动归集</el-button>
      <el-button type="info" @click="handleCollectionRule">归集规则</el-button>
      <el-button type="danger" @click="handleBatchCancel" :disabled="!multipleSelection.length">
        批量取消
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon direct-cost">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.directCost) }}</div>
              <div class="stat-label">直接成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon indirect-cost">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.indirectCost) }}</div>
              <div class="stat-label">间接成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon manufacturing-cost">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.manufacturingCost) }}</div>
              <div class="stat-label">制造费用</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-cost">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalCost) }}</div>
              <div class="stat-label">总成本</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="collectionNo" label="归集单号" width="150" />
        <el-table-column prop="collectionPeriod" label="归集期间" width="120" />
        <el-table-column prop="costCenterName" label="成本中心" min-width="180" show-overflow-tooltip />
        <el-table-column prop="collectionTypeName" label="归集类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCollectionTypeTag(scope.row.collectionType)">
              {{ scope.row.collectionTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectionAmount" label="归集金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.collectionAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="collectionStatusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.collectionStatus)">
              {{ scope.row.collectionStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="collectionDate" label="归集日期" width="120" />
        <el-table-column prop="auditorName" label="审核人" width="100" />
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
              <el-button size="mini" type="primary" @click="handleAudit(scope.row)" v-if="scope.row.collectionStatus === 3">审核</el-button>
              <el-button size="mini" type="primary" @click="handleCancel(scope.row)" v-if="scope.row.collectionStatus < 4">取消</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
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

    <!-- 开始归集对话框 -->
    <el-dialog
      title="开始成本归集"
      :visible.sync="collectionDialogVisible"
      width="600px"
    >
      <el-form :model="collectionForm" :rules="collectionRules" ref="collectionFormRef" label-width="120px">
        <el-form-item label="归集期间" prop="collectionPeriod">
          <el-date-picker
            v-model="collectionForm.collectionPeriod"
            type="month"
            placeholder="请选择归集期间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="成本中心" prop="costCenterIds">
          <el-select
            v-model="collectionForm.costCenterIds"
            placeholder="请选择成本中心"
            multiple
            style="width: 100%"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.VALUE"
              :label="item.LABEL"
              :value="item.VALUE"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="归集类型" prop="collectionTypes">
          <el-checkbox-group v-model="collectionForm.collectionTypes">
            <el-checkbox :label="1">直接成本</el-checkbox>
            <el-checkbox :label="2">间接成本</el-checkbox>
            <el-checkbox :label="3">制造费用</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="归集方式" prop="collectionMethod">
          <el-radio-group v-model="collectionForm.collectionMethod">
            <el-radio :label="1">手工归集</el-radio>
            <el-radio :label="2">自动归集</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="collectionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitCollection">开始归集</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="成本归集详情"
      :visible.sync="detailDialogVisible"
      width="900px"
    >
      <div v-if="detailData">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集单号：</span>
              <span class="value">{{ detailData.collectionNo }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集期间：</span>
              <span class="value">{{ detailData.collectionPeriod }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">成本中心：</span>
              <span class="value">{{ detailData.costCenterName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集类型：</span>
              <span class="value">{{ detailData.collectionTypeName }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集金额：</span>
              <span class="value amount-text">{{ formatAmount(detailData.collectionAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集状态：</span>
              <el-tag :type="getStatusTag(detailData.collectionStatus)">{{ detailData.collectionStatusName }}</el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集方式：</span>
              <span class="value">{{ detailData.collectionMethodName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="label">归集日期：</span>
              <span class="value">{{ detailData.collectionDate }}</span>
            </div>
          </el-col>
        </el-row>

        <!-- 明细列表 -->
        <div v-if="detailData.detailList && detailData.detailList.length > 0" style="margin-top: 20px;">
          <h4>归集明细</h4>
          <el-table :data="detailData.detailList" stripe border size="small">
            <el-table-column prop="costElementName" label="成本要素" width="150" />
            <el-table-column prop="accountName" label="会计科目" width="150" />
            <el-table-column prop="originalAmount" label="原始金额" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.originalAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="allocationRate" label="分摊率" width="100" align="right">
              <template slot-scope="scope">
                {{ (scope.row.allocationRate * 100).toFixed(2) }}%
              </template>
            </el-table-column>
            <el-table-column prop="allocatedAmount" label="分摊金额" width="120" align="right">
              <template slot-scope="scope">
                {{ formatAmount(scope.row.allocatedAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="departmentName" label="部门" min-width="120" />
            <el-table-column prop="projectName" label="项目" min-width="120" />
          </el-table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 归集规则对话框 -->
    <el-dialog
      title="成本归集规则"
      :visible.sync="rulesDialogVisible"
      width="800px"
    >
      <el-table :data="rulesData" stripe border>
        <el-table-column prop="ruleName" label="规则名称" min-width="180" show-overflow-tooltip />
        <el-table-column label="成本中心" width="140" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ formatCenterName(scope.row.costCenterId) }}
          </template>
        </el-table-column>
        <el-table-column label="归集类型" width="110">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getRuleTypeTag(scope.row.collectionType)">
              {{ formatCollectionType(scope.row.collectionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="归集方法" width="110">
          <template slot-scope="scope">
            <el-tag size="mini" effect="plain" :type="scope.row.collectionMethod === 'DIRECT' ? 'success' : 'warning'">
              {{ formatCollectionMethod(scope.row.collectionMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ruleDescription" label="规则描述" min-width="220" show-overflow-tooltip />
        <el-table-column prop="priority" label="优先级" width="80" align="center" />
        <el-table-column prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'" size="mini">
              {{ scope.row.isEnabled === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="rulesDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostCollectionList,
  startCostCollection,
  auditCostCollection,
  cancelCostCollection,
  getCostCollectionStats,
  getCostCenterOptions,
  getCostCollectionDetail,
  batchAuditCostCollection,
  batchCancelCostCollection,
  autoCostCollection,
  getCollectionRules
} from '@/api/financialSharing/costCenter'

export default {
  name: 'CostCollection',
  data() {
    return {
      loading: false,
      searchForm: {
        collectionPeriod: '',
        costCenterId: '',
        collectionType: '',
        collectionStatus: ''
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      stats: {
        directCost: 0,
        indirectCost: 0,
        manufacturingCost: 0,
        totalCost: 0
      },
      collectionDialogVisible: false,
      collectionForm: {
        collectionPeriod: '',
        costCenterIds: [],
        collectionTypes: [1, 2, 3],
        collectionMethod: 1
      },
      collectionRules: {
        collectionPeriod: [
          { required: true, message: '请选择归集期间', trigger: 'change' },
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error('请选择归集期间'));
                return;
              }

              const formatted = this.formatCollectionPeriod(value);
              if (!formatted.match(/^\d{4}-\d{2}$/)) {
                callback(new Error('归集期间格式应为 YYYY-MM'));
                return;
              }

              const [year, month] = formatted.split('-').map(Number);
              if (year < 2000 || year > 2100) {
                callback(new Error('年份应在2000-2100之间'));
                return;
              }

              if (month < 1 || month > 12) {
                callback(new Error('月份应在1-12之间'));
                return;
              }

              callback();
            },
            trigger: 'change'
          }
        ],
        costCenterIds: [
          { required: true, message: '请选择成本中心', trigger: 'change' }
        ],
        collectionTypes: [
          { required: true, message: '请选择归集类型', trigger: 'change' }
        ]
      },
      costCenterOptions: [],
      detailDialogVisible: false,
      detailData: null,
      rulesDialogVisible: false,
      rulesData: []
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
    this.loadCostCenterOptions()
  },
  methods: {
    // 格式化归集期间 - 将ISO日期格式转换为YYYY-MM格式
    formatCollectionPeriod(date) {
      if (!date) return '';

      if (typeof date === 'string') {
        // 处理ISO日期时间字符串，如 "2025-11-30T16:00:00.000Z"
        if (date.includes('T')) {
          return date.substring(0, 7); // 提取 "2025-11"
        }
        // 处理已有的YYYY-MM格式，防止截断错误
        return date.length > 7 ? date.substring(0, 7) : date;
      }

      if (date instanceof Date) {
        return date.toISOString().substring(0, 7);
      }

      return '';
    },
    async loadData() {
      this.loading = true
      try {
        // 获取用户上下文信息
        const { bookId, tenantId } = this.getUserContext()

        // 格式化搜索参数中的归集期间
        const searchParams = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          bookId,
          tenantId,
          ...this.searchForm,
          collectionPeriod: this.formatCollectionPeriod(this.searchForm.collectionPeriod)
        };

        // 添加请求前日志验证
        console.log('=== 成本归集查询调试 ===')
        console.log('1. 发送查询参数:', searchParams)
        console.log('2. 用户上下文 bookId:', bookId, 'tenantId:', tenantId)
        console.log('3. 搜索表单:', this.searchForm)
        console.log('4. 格式化后期间:', this.formatCollectionPeriod(this.searchForm.collectionPeriod))

        const response = await getCostCollectionList(searchParams)
        console.log('5. 接收API响应:', response)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          console.log('6. 响应码正确，处理数据')
          console.log('7. 原始tlist数据:', response.data.tlist)
          console.log('8. 原始totalRecord:', response.data.totalRecord)

          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0

          console.log('9. 赋值后 tableData 长度:', this.tableData.length)
          console.log('10. 赋值后 pagination.total:', this.pagination.total)
        } else {
          console.log('6. 响应码错误:', response.code, '消息:', response.msg)
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStats() {
      try {
        // 获取当前选择的归集期间，如果没有则使用当前月份
        const period = this.formatCollectionPeriod(this.searchForm.collectionPeriod) ||
                     new Date().toISOString().substring(0, 7)

        const response = await getCostCollectionStats({ period })
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.stats = response.data || {}
        } else {
          this.$message.error(response.msg || '加载统计数据失败')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败')
      }
    },
    async loadCostCenterOptions() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        const response = await getCostCenterOptions({ bookId, tenantId })
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.costCenterOptions = response.data || []
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
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
    handleStartCollection() {
      this.collectionForm = {
        collectionPeriod: '',
        costCenterIds: [],
        collectionTypes: [1, 2, 3],
        collectionMethod: 1
      }
      this.collectionDialogVisible = true
    },
    handleSubmitCollection() {
      this.$refs.collectionFormRef.validate(async (valid) => {
        if (valid) {
          try {
            // 提交前格式化归集期间，防止字符串截断错误
            const collectionData = {
              ...this.collectionForm,
              collectionPeriod: this.formatCollectionPeriod(this.collectionForm.collectionPeriod)
            };

            // 确保成本中心ID使用字符串类型传递，避免JavaScript大整数精度丢失
            if (collectionData.costCenterIds && Array.isArray(collectionData.costCenterIds)) {
              collectionData.costCenterIds = collectionData.costCenterIds.map(id => String(id));
            }

            const response = await startCostCollection(collectionData)
            if (response.code === 1) {
              // axios拦截器会将后端的 code=1 转换为 code=200
              this.$message.success('成本归集已开始')
              this.collectionDialogVisible = false
              this.loadData()
              this.loadStats()
            } else {
              this.$message.error(response.msg || '开始归集失败')
            }
          } catch (error) {
            console.error('开始归集失败:', error)
            this.$message.error('开始归集失败')
          }
        }
      })
    },
    async handleAutoCollection() {
      try {
        await this.$confirm('确认执行自动归集吗？系统将根据规则自动归集成本数据', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })

        const { bookId, tenantId } = this.getUserContext()
        const period = this.formatCollectionPeriod(this.searchForm.collectionPeriod) || new Date().toISOString().substring(0, 7)

        const autoData = {
          period,
          bookId,
          tenantId
          // 可以添加更多参数，如指定成本中心、归集类型等
        }

        this.loading = true
        const response = await autoCostCollection(autoData)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success(`自动归集完成：${response.data.message}`)
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '自动归集失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('自动归集失败:', error)
          this.$message.error('自动归集失败')
        }
      } finally {
        this.loading = false
      }
    },
    async handleCollectionRule() {
      try {
        const { bookId, tenantId } = this.getUserContext()

        this.loading = true
        const response = await getCollectionRules({ bookId, tenantId })
        // 调试：response 结构
        console.log('[归集规则] 原始 response:', response)
        console.log('[归集规则] response.code:', response && response.code)
        console.log('[归集规则] response.data 类型:', Object.prototype.toString.call(response && response.data))

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const rawRules = response.data || []
          console.log('[归集规则] rawRules 长度:', rawRules.length)
          console.log('[归集规则] rawRules[0] keys:', rawRules[0] ? Object.keys(rawRules[0]) : '空')

          if (rawRules.length === 0) {
            this.$message.info('暂无归集规则')
            return
          }

          // 兼容达梦 alias 大小写差异 + axios 拦截器可能的字段大小写转换
          // 用 pickField 函数尝试多种命名（lowerCamel / UPPER_CONNECTED / UPPER_SNAKE）
          const pickField = (obj, lowerCamel, upperConnected, upperSnake) => {
            if (obj[lowerCamel] !== undefined) return obj[lowerCamel]
            if (upperConnected && obj[upperConnected] !== undefined) return obj[upperConnected]
            if (upperSnake && obj[upperSnake] !== undefined) return obj[upperSnake]
            return undefined
          }
          const rules = rawRules.map((it) => ({
            ruleId:           pickField(it, 'ruleId',           'RULEID',           'RULE_ID'),
            ruleName:         pickField(it, 'ruleName',         'RULENAME',         'RULE_NAME'),
            costCenterId:     pickField(it, 'costCenterId',     'COSTCENTERID',     'COST_CENTER_ID'),
            sourceAccount:    pickField(it, 'sourceAccount',    'SOURCEACCOUNT',    'SOURCE_ACCOUNT'),
            collectionMethod: pickField(it, 'collectionMethod', 'COLLECTIONMETHOD', 'COLLECTION_METHOD'),
            collectionType:   pickField(it, 'collectionType',   'COLLECTIONTYPE',   'COLLECTION_TYPE'),
            ruleDescription:  pickField(it, 'ruleDescription',  'RULEDESCRIPTION',  'RULE_DESCRIPTION'),
            isEnabled:        pickField(it, 'isEnabled',        'ISENABLED',        'IS_ENABLED'),
            priority:         pickField(it, 'priority',         'PRIORITY',         'PRIORITY'),
            createTime:       pickField(it, 'createTime',       'CREATETIME',       'CREATE_TIME')
          }))
          console.log('[归集规则] normalize 后 rules[0]:', rules[0])

          // 显示归集规则对话框
          this.showCollectionRulesDialog(rules)
          // 调试：dialog 渲染后看 rulesData
          this.$nextTick(() => {
            console.log('[归集规则] rulesData 已绑定，长度:', this.rulesData.length, '首条:', this.rulesData[0])
          })
        } else {
          this.$message.error(response.msg || '获取归集规则失败')
        }
      } catch (error) {
        console.error('获取归集规则失败:', error)
        this.$message.error('获取归集规则失败')
      } finally {
        this.loading = false
      }
    },
    async handleBatchAudit() {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要审核的记录')
          return
        }

        await this.$confirm(`确认审核选中的 ${this.multipleSelection.length} 条记录吗？`, '批量审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const collectionIds = this.multipleSelection.map(item => item.collectionIdStr || String(item.collectionId))
        const auditData = {
          auditResult: 'approved',
          auditRemark: '批量审核通过'
        }

        this.loading = true
        const response = await batchAuditCostCollection(collectionIds, auditData)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success(`批量审核成功：${response.data.message}`)
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '批量审核失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量审核失败:', error)
          this.$message.error('批量审核失败')
        }
      } finally {
        this.loading = false
      }
    },
    async handleBatchCancel() {
      try {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要取消的记录')
          return
        }

        await this.$confirm(`确认取消选中的 ${this.multipleSelection.length} 条记录吗？此操作不可恢复！`, '批量取消', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })

        const collectionIds = this.multipleSelection.map(item => item.collectionIdStr || String(item.collectionId))

        this.loading = true
        const response = await batchCancelCostCollection(collectionIds)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success(response.msg || '批量取消成功')
          this.loadData()
          this.loadStats()
        } else {
          this.$message.error(response.msg || '批量取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量取消失败:', error)
          this.$message.error('批量取消失败')
        }
      } finally {
        this.loading = false
      }
    },
    async handleView(row) {
      try {
        const collectionIdStr = row.collectionIdStr || String(row.collectionId)

        this.loading = true
        const response = await getCostCollectionDetail(collectionIdStr)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const detailData = response.data
          this.showDetailDialog(detailData)
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('查看详情失败:', error)
        this.$message.error('获取详情失败')
      } finally {
        this.loading = false
      }
    },
    async handleAudit(row) {
      try {
        await this.$confirm('确认审核该归集记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 使用字符串类型的ID，避免JavaScript大整数精度丢失
        const collectionIdStr = row.collectionIdStr || String(row.collectionId)
        const response = await auditCostCollection(collectionIdStr, { auditResult: 'approved' })
        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('审核成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '审核失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审核失败:', error)
          this.$message.error('审核失败')
        }
      }
    },
    async handleCancel(row) {
      try {
        // 使用字符串类型的ID，避免JavaScript大整数精度丢失
        const collectionIdStr = row.collectionIdStr || String(row.collectionId)

        console.log('=== 取消归集操作调试信息 ===')
        console.log('原始collectionId:', row.collectionId)
        console.log('collectionIdStr:', row.collectionIdStr)
        console.log('使用的ID:', collectionIdStr)
        console.log('ID类型:', typeof collectionIdStr)
        console.log('Row data:', row)

        await this.$confirm('确认取消该归集记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        console.log('用户确认取消，调用API...')
        // 使用字符串类型的ID调用API
        const response = await cancelCostCollection(collectionIdStr)

        console.log('API响应:', response)
        console.log('响应码:', response.code, '响应消息:', response.msg)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('取消成功')
          this.loadData()
        } else {
          console.error('取消操作失败:', response)
          this.$message.error(response.msg || '取消失败，请检查数据或联系管理员')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消操作异常:', error)
          console.error('错误详情:', {
            message: error.message,
            stack: error.stack,
            response: error.response
          })
          this.$message.error('取消失败，请检查数据或联系管理员')
        }
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
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    getCollectionTypeTag(type) {
      const tagMap = {
        1: 'primary',
        2: 'success',
        3: 'warning'
      }
      return tagMap[type] || 'info'
    },
    // 获取用户信息中的bookId和tenantId
    getUserContext() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')

        let bookId = userInfo.currentBook?.bookId ||
                     userInfo.bookId ||
                     userInfo.bookInfo?.bookId ||
                     1

        let tenantId = userInfo.currentOrg?.orgid ||
                       userInfo.tenantId ||
                       userInfo.orgId ||
                       1000

        return { bookId, tenantId }
      } catch (error) {
        console.error('获取用户上下文失败:', error)
        return { bookId: 1, tenantId: 1000 }
      }
    },
    getStatusTag(status) {
      const tagMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'primary'
      }
      return tagMap[status] || 'info'
    },

    // 显示详情对话框
    showDetailDialog(detailData) {
      this.detailData = detailData
      this.detailDialogVisible = true
    },

    // 显示归集规则对话框
    showCollectionRulesDialog(rules) {
      this.rulesData = rules
      this.rulesDialogVisible = true
    },

    // ==================== 归集规则字段格式化 ====================
    // 后端返回的字段是后端 alias（ruleName/collectionMethod/collectionType/ruleDescription...）
    // 这里只做枚举到中文的翻译，以及 costCenterId 反查中心名

    // 通过 costCenterOptions 反查中心名
    formatCenterName(centerId) {
      if (centerId === null || centerId === undefined || centerId === '') return '-'
      const opt = (this.costCenterOptions || []).find(
        (o) => String(o.value) === String(centerId) || String(o.id) === String(centerId)
      )
      return opt ? (opt.label || opt.name || opt.centerName || String(centerId)) : String(centerId)
    },

    // 归集类型枚举 → 中文
    formatCollectionType(type) {
      const map = {
        MATERIAL: '材料成本',
        LABOR: '人工成本',
        OVERHEAD: '制造费用'
      }
      return map[type] || type || '-'
    },

    // 归集方法枚举 → 中文
    formatCollectionMethod(method) {
      const map = {
        DIRECT: '直接归集',
        ALLOCATION: '分摊归集'
      }
      return map[method] || method || '-'
    },

    // 归集类型对应的 el-tag 颜色
    getRuleTypeTag(type) {
      const map = {
        MATERIAL: 'success',
        LABOR: 'warning',
        OVERHEAD: 'info'
      }
      return map[type] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-collection-container {
  padding: 20px;
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;

  .el-button {
    margin-right: 10px;
  }
}

// 表格操作列：按钮强制单行展示
.action-buttons {
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: flex-start;
  gap: 6px;

  .el-button + .el-button {
    margin-left: 0; // 由 gap 接管间距，避免 element-ui 默认 margin 累加
  }
}

.stats-cards {
  margin-bottom: 20px;
  
  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    
    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
      
      &.direct-cost {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      
      &.indirect-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
      
      &.manufacturing-cost {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
      
      &.total-cost {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }
    
    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.table-container {
  background: white;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount-text {
  font-weight: 600;
  color: #E6A23C;
}

.dialog-footer {
  text-align: right;
}

.detail-item {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.detail-item .label {
  font-weight: 500;
  color: #606266;
  min-width: 100px;
}

.detail-item .value {
  color: #303133;
  flex: 1;
}
</style>
