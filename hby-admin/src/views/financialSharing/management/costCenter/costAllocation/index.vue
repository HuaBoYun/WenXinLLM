<template>
  <div class="cost-allocation-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="分摊期间" prop="allocationPeriod">
          <el-date-picker
            v-model="searchForm.allocationPeriod"
            type="month"
            placeholder="请选择分摊期间"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="源成本中心" prop="sourceCostCenterId">
          <el-select
            v-model="searchForm.sourceCostCenterId"
            placeholder="请选择源成本中心"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊方法" prop="allocationMethod">
          <el-select
            v-model="searchForm.allocationMethod"
            placeholder="请选择分摊方法"
            clearable
            style="width: 150px"
          >
            <el-option label="数量基础" value="QUANTITY_BASED" />
            <el-option label="金额基础" value="AMOUNT_BASED" />
            <el-option label="比例基础" value="RATIO_BASED" />
            <el-option label="工时基础" value="HOUR_BASED" />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊状态" prop="allocationStatus">
          <el-select
            v-model="searchForm.allocationStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待分摊" :value="1" />
            <el-option label="分摊中" :value="2" />
            <el-option label="已分摊" :value="3" />
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
      <el-button type="primary" @click="handleStartAllocation">开始分摊</el-button>
      <el-button type="success" @click="handleBatchAllocate" :disabled="!multipleSelection.length">
        批量分摊
      </el-button>
      <el-button type="primary" @click="handleBatchAudit" :disabled="!multipleSelection.length">
        批量审核
      </el-button>
      <el-button type="warning" @click="handleAllocationRule">分摊规则</el-button>
      <el-button type="info" @click="handleAllocationBasis">分摊基础</el-button>
      <el-button type="danger" @click="handleBatchCancel" :disabled="!multipleSelection.length">
        批量取消
      </el-button>
    </div>

    <!-- 分摊概览 -->
    <div class="allocation-overview">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="overview-card">
            <div class="card-header">
              <h4>本月分摊总额</h4>
            </div>
            <div class="card-content">
              <div class="amount">{{ formatAmount(overview.totalAmount) }}</div>
              <div class="trend">
                <i class="el-icon-top" style="color: #67C23A"></i>
                <span>较上月增长 {{ overview.growthRate }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="overview-card">
            <div class="card-header">
              <h4>分摊中心数量</h4>
            </div>
            <div class="card-content">
              <div class="amount">{{ overview.centerCount }}</div>
              <div class="trend">
                <span>活跃中心 {{ overview.activeCenters }} 个</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="overview-card">
            <div class="card-header">
              <h4>平均分摊率</h4>
            </div>
            <div class="card-content">
              <div class="amount">{{ overview.avgAllocationRate }}%</div>
              <div class="trend">
                <span>分摊效率良好</span>
              </div>
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
        :key="tableKey"
        row-key="allocationId"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="allocationNo" label="分摊单号" width="150" />
        <el-table-column prop="allocationPeriod" label="分摊期间" width="120" />
        <el-table-column prop="sourceCenterName" label="源成本中心" min-width="180" show-overflow-tooltip />
        <el-table-column prop="allocationMethodName" label="分摊方法" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAllocationMethodTag(scope.row.allocationMethod)">
              {{ scope.row.allocationMethodName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="分摊总额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatCurrency(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="targetCenterCount" label="目标中心数" width="100" align="center" />
        <el-table-column prop="allocationStatusName" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.allocationStatus)">
              {{ scope.row.allocationStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allocationDate" label="分摊日期" width="120" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">明细</el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleAllocate(scope.row)"
              v-if="scope.row.allocationStatus < 3"
            >
              分摊
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleAudit(scope.row)"
              v-if="scope.row.allocationStatus === 3"
            >
              审核
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleCancel(scope.row)"
              v-if="scope.row.allocationStatus < 4"
            >
              取消
            </el-button>
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

    <!-- 开始分摊对话框 -->
    <el-dialog
      title="开始成本分摊"
      :visible.sync="allocationDialogVisible"
      width="800px"
    >
      <el-form :model="allocationForm" :rules="allocationRules" ref="allocationFormRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分摊期间" prop="allocationPeriod">
              <el-date-picker
                v-model="allocationForm.allocationPeriod"
                type="month"
                placeholder="请选择分摊期间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="源成本中心" prop="sourceCostCenterId">
              <el-select
                v-model="allocationForm.sourceCostCenterId"
                placeholder="请选择源成本中心"
                style="width: 100%"
              >
                <el-option
                  v-for="item in costCenterOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分摊方法" prop="allocationMethod">
              <el-select
                v-model="allocationForm.allocationMethod"
                placeholder="请选择分摊方法"
                style="width: 100%"
              >
                <el-option label="数量基础" value="QUANTITY_BASED" />
                <el-option label="金额基础" value="AMOUNT_BASED" />
                <el-option label="比例基础" value="RATIO_BASED" />
                <el-option label="工时基础" value="HOUR_BASED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分摊总额" prop="totalAmount">
              <el-input-number
                v-model="allocationForm.totalAmount"
                placeholder="请输入分摊总额"
                :precision="2"
                :min="0"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="目标成本中心" prop="targetCostCenterIds">
          <el-select
            v-model="allocationForm.targetCostCenterIds"
            placeholder="请选择目标成本中心"
            multiple
            style="width: 100%"
          >
            <el-option
              v-for="item in costCenterOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊基础">
          <el-table :data="allocationBasisData" border>
            <el-table-column prop="centerName" label="目标中心" />
            <el-table-column prop="basisValue" label="分摊基础值" width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.basisValue"
                  :precision="2"
                  :min="0"
                  size="mini"
                />
              </template>
            </el-table-column>
            <el-table-column prop="allocationRatio" label="分摊比例" width="120">
              <template slot-scope="scope">
                {{ (scope.row.basisValue / totalBasisValue * 100).toFixed(2) }}%
              </template>
            </el-table-column>
            <el-table-column prop="allocatedAmount" label="分摊金额" width="120">
              <template slot-scope="scope">
                {{ (allocationForm.totalAmount * scope.row.basisValue / totalBasisValue).toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="是否生成凭证" prop="isGenerateVoucher">
          <el-radio-group v-model="allocationForm.isGenerateVoucher">
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="allocationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAllocation">开始分摊</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostAllocationList,
  startCostAllocation,
  auditCostAllocation,
  getCostAllocationOverview,
  getCostCenterOptions,
  getAllocationDetailList,
  batchAuditCostAllocation,
  batchDeleteCostAllocation,
  getAllocationRules,
  getAllocationBasis,
  cancelCostAllocation,
  batchAllocateCostAllocation,
  allocateCostAllocation
} from '@/api/financialSharing/costCenter'

export default {
  name: 'CostAllocation',
  data() {
    return {
      loading: false,
      tableKey: Date.now(),
      searchForm: {
        allocationPeriod: '',
        sourceCostCenterId: '',
        allocationMethod: '',
        allocationStatus: ''
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      overview: {
        totalAmount: 0,
        growthRate: 0,
        centerCount: 0,
        activeCenters: 0,
        avgAllocationRate: 0
      },
      allocationDialogVisible: false,
      allocationForm: {
        allocationPeriod: '',
        sourceCostCenterId: '',
        allocationMethod: 'QUANTITY_BASED',
        totalAmount: 0,
        targetCostCenterIds: [],
        isGenerateVoucher: true
      },
      allocationRules: {
        allocationPeriod: [
          { required: true, message: '请选择分摊期间', trigger: 'change' }
        ],
        sourceCostCenterId: [
          { required: true, message: '请选择源成本中心', trigger: 'change' }
        ],
        allocationMethod: [
          { required: true, message: '请选择分摊方法', trigger: 'change' }
        ],
        totalAmount: [
          { required: true, message: '请输入分摊总额', trigger: 'blur' }
        ],
        targetCostCenterIds: [
          { required: true, message: '请选择目标成本中心', trigger: 'change' }
        ]
      },
      allocationBasisData: [],
      costCenterOptions: []
    }
  },
  computed: {
    totalBasisValue() {
      return this.allocationBasisData.reduce((sum, item) => sum + (item.basisValue || 0), 0)
    }
  },
  mounted() {
    // 按顺序加载数据，避免并发问题
    this.initializeData()
  },
  methods: {
    async initializeData() {
      try {
        console.log('开始初始化数据')

        // 先加载成本中心选项，为后续功能提供支持
        await this.loadCostCenterOptions()

        // 并行加载主要数据
        await Promise.all([
          this.loadData(),
          this.loadOverview()
        ])

        console.log('数据初始化完成')
      } catch (error) {
        console.error('数据初始化失败:', error)
        // 初始化失败只记录日志，不显示错误消息
      }
    },
    async loadData() {
      this.loading = true
      try {
        const { bookId, tenantId } = this.getUserContext()

        const requestParams = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          bookId,
          tenantId,
          ...this.searchForm
        }

        console.log('发送请求参数:', requestParams)
        const response = await getCostAllocationList(requestParams)
        console.log('收到API响应:', response)

        // 检查响应格式
        if (!response) {
          console.error('API响应为空')
          this.$message.error('服务器响应为空')
          return
        }

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          // 处理数据格式转换 - 后端返回的是大写字段名，前端需要转换为小写
          const rawData = response.data.tlist || []
          console.log('后端返回的原始数据:', rawData)

          try {
            this.tableData = rawData.map(item => {
              // 打印每个item的详细信息用于调试
              console.log('处理数据项:', item)

              const mappedItem = {
                allocationNo: item.ALLOCATIONNO || item.allocationNo || '',
                allocationPeriod: item.ALLOCATIONPERIOD || item.allocationPeriod || '',
                sourceCenterName: item.SOURCECENTERNAME || item.sourceCenterName || '',
                sourceCenterId: String(item.SOURCECENTERID || item.sourceCenterId || ''),
                allocationMethod: item.ALLOCATIONMETHOD || item.allocationMethod || '',
                allocationMethodName: this.getAllocationMethodName(item.ALLOCATIONMETHOD || item.allocationMethod),
                totalAmount: parseFloat(item.TOTALAMOUNT || item.totalAmount || 0),
                targetCenterCount: parseInt(item.TARGETCENTERCOUNT || item.targetCenterCount || 0),
                allocationStatus: parseInt(item.ALLOCATIONSTATUS || item.allocationStatus || 1),
                allocationStatusName: this.getAllocationStatusName(item.ALLOCATIONSTATUS || item.allocationStatus),
                allocationDate: item.ALLOCATIONDATE || item.allocationDate || '',
                allocationId: String(item.ALLOCATIONID || item.allocationId || ''),
                isGenerateVoucher: parseInt(item.ISGENERATEVOUCHER || item.isGenerateVoucher || 0),
                totalAllocatedAmount: parseFloat(item.TOTALALLOCATEDAMOUNT || item.totalAllocatedAmount || 0),
                createTime: item.CREATETIME || item.createTime || ''
              }

              console.log('映射后的数据项:', mappedItem)
              return mappedItem
            })

            console.log('数据映射成功，映射后的表格数据:', this.tableData)
          } catch (mapError) {
            console.error('数据映射过程中发生错误:', mapError)
            this.$message.error('数据处理失败: ' + mapError.message)
            return
          }
          console.log('转换后的表格数据:', this.tableData)
          console.log('表格数据长度:', this.tableData.length)

          // 验证第一个数据项的字段值
          if (this.tableData.length > 0) {
            console.log('第一行数据的字段值:', {
              allocationNo: this.tableData[0].allocationNo,
              sourceCenterName: this.tableData[0].sourceCenterName,
              totalAmount: this.tableData[0].totalAmount,
              allocationStatusName: this.tableData[0].allocationStatusName
            })
          }

          this.pagination.total = response.data.totalRecord || 0
          console.log('设置分页总数:', this.pagination.total)

          // 更新表格key以触发重新渲染
          this.tableKey = Date.now()
          console.log('已更新表格key:', this.tableKey)

          // 强制重新渲染
          this.$forceUpdate()
          console.log('已强制更新组件')
        } else {
          console.error('API返回错误码:', response.code, '错误信息:', response.msg)
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败 - 详细错误信息:', error)
        console.error('错误对象:', error)

        // 根据错误类型显示不同的错误信息
        let errorMessage = '加载数据失败'
        if (error.response) {
          // 服务器响应了错误状态码
          console.error('错误状态码:', error.response.status)
          console.error('错误响应数据:', error.response.data)
          errorMessage = `服务器错误 (${error.response.status}): ${error.response.data?.message || error.response.data?.msg || '未知错误'}`
        } else if (error.request) {
          // 请求发出但没有收到响应
          console.error('无响应错误:', error.request)
          errorMessage = '网络连接失败，请检查网络连接'
        } else {
          // 其他错误
          console.error('请求配置错误:', error.config)
          errorMessage = error.message || '加载数据失败'
        }

        this.$message.error(errorMessage)
      } finally {
        this.loading = false
      }
    },
    async loadOverview() {
      try {
        console.log('开始加载概览数据')
        const response = await getCostAllocationOverview()
        console.log('概览数据API响应:', response)

        if (response && response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const raw = response.data || {}

          // 兼容达梦 alias 大小写差异（mapper alias 加双引号前是大写连写，加双引号后是小驼峰）
          const pickField = (obj, lowerCamel, upperConnected, upperSnake) => {
            if (obj[lowerCamel] !== undefined) return obj[lowerCamel]
            if (upperConnected && obj[upperConnected] !== undefined) return obj[upperConnected]
            if (upperSnake && obj[upperSnake] !== undefined) return obj[upperSnake]
            return undefined
          }
          const totalAmount = parseFloat(pickField(raw, 'totalAmount', 'TOTALAMOUNT', 'TOTAL_AMOUNT')) || 0
          const allocationCount = parseInt(pickField(raw, 'allocationCount', 'ALLOCATIONCOUNT', 'ALLOCATION_COUNT')) || 0
          const centerCount = parseInt(pickField(raw, 'centerCount', 'CENTERCOUNT', 'CENTER_COUNT')) || 0
          const activeCenters = parseInt(pickField(raw, 'activeCenters', 'ACTIVECENTERS', 'ACTIVE_CENTERS')) || centerCount
          const avgAllocationRate = parseFloat(pickField(raw, 'avgAllocationRate', 'AVGALLOCATIONRATE', 'AVG_ALLOCATION_RATE')) || 0
          const growthRate = parseFloat(pickField(raw, 'growthRate', 'GROWTHRATE', 'GROWTH_RATE')) || 0

          this.overview = {
            totalAmount,
            allocationCount,
            centerCount,
            activeCenters,
            avgAllocationRate,
            growthRate
          }
          console.log('概览数据加载成功（已规范化）:', this.overview)
        } else {
          console.error('概览数据API返回错误:', response)
          // 概览数据加载失败不显示错误消息，避免影响主要功能
          console.warn('概览数据加载失败，但不影响主要功能')
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
        // 概览数据加载失败不显示错误消息，避免影响主要功能
        console.warn('概览数据加载异常，但不影响主要功能')
      }
    },
    async loadCostCenterOptions() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        console.log('开始加载成本中心选项，参数:', { bookId, tenantId })

        const response = await getCostCenterOptions({ bookId, tenantId })
        console.log('API响应数据:', response)

        if (response && response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          // 处理数据格式转换 - 后端返回的是大写字段名，前端需要小写
          const rawData = response.data || []
          console.log('成本中心选项原始数据:', rawData)

          this.costCenterOptions = rawData.map(item => ({
            value: item.VALUE || item.value || item.centerId,
            label: item.LABEL || item.label || item.centerName,
            code: item.CODE || item.code || item.centerCode,
            centerType: item.CENTERTYPE || item.centerType,
            level: item.LEVEL || item.level,
            isLeaf: item.ISLEAF || item.isLeaf
          }))

          console.log('转换后的成本中心选项:', this.costCenterOptions)
        } else {
          console.error('成本中心选项API返回错误:', response)
          // 成本中心选项加载失败不显示错误消息，使用空数组
          console.warn('成本中心选项加载失败，使用空数组')
          this.costCenterOptions = []
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
        // 成本中心选项加载失败不显示错误消息，使用空数组
        console.warn('成本中心选项加载异常，使用空数组')
        this.costCenterOptions = []
      }
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
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleStartAllocation() {
      this.allocationForm = {
        allocationPeriod: '',
        sourceCostCenterId: '',
        allocationMethod: 'QUANTITY_BASED',
        totalAmount: 0,
        targetCostCenterIds: [],
        isGenerateVoucher: true
      }
      this.allocationBasisData = []
      this.allocationDialogVisible = true
    },
    handleSubmitAllocation() {
      this.$refs.allocationFormRef.validate(async (valid) => {
        if (valid) {
          try {
            const { bookId, tenantId } = this.getUserContext()

            // 添加ID验证日志
            console.log('提交分摊请求 - 源成本中心ID:', this.allocationForm.sourceCostCenterId)
            console.log('提交分摊请求 - 目标成本中心IDs:', this.allocationForm.targetCostCenterIds)
            console.log('源成本中心ID类型:', typeof this.allocationForm.sourceCostCenterId)
            console.log('目标成本中心IDs类型:', this.allocationForm.targetCostCenterIds.map(id => typeof id))

            // 准备分摊数据
            const allocationData = {
              allocationPeriod: this.formatMonth(this.allocationForm.allocationPeriod),
              sourceCostCenterId: this.allocationForm.sourceCostCenterId,
              allocationMethod: this.allocationForm.allocationMethod,
              totalAmount: this.allocationForm.totalAmount,
              targetCostCenterIds: this.allocationForm.targetCostCenterIds,
              allocationBasis: this.convertAllocationBasisToMap(this.allocationBasisData),
              isGenerateVoucher: this.allocationForm.isGenerateVoucher,
              bookId,
              tenantId
            }

            console.log('即将发送的分摊数据:', allocationData)

            // 调用API开始分摊
            const response = await startCostAllocation(allocationData)

            if (response.code === 1) {
              // axios拦截器会将后端的 code=1 转换为 code=200
              this.$message.success('成本分摊已开始')
              this.allocationDialogVisible = false
              this.loadData()
            } else {
              this.$message.error(response.msg || '开始分摊失败')
            }
          } catch (error) {
            console.error('开始分摊失败:', error)
            this.$message.error('开始分摊失败')
          }
        }
      })
    },
    async handleAllocationRule() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        const response = await getAllocationRules({
          bookId,
          tenantId
        })

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const rules = response.data || []

          if (rules.length === 0) {
            this.$message.info('暂无分摊规则数据')
            return
          }

          // 构建规则表格HTML
          let tableHtml = `
            <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
              <thead>
                <tr style="background-color: #f5f7fa;">
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">规则名称</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">规则类型</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">描述</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">公式</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: center;">状态</th>
                </tr>
              </thead>
              <tbody>
          `

          rules.forEach(rule => {
            const statusText = rule.isEnabled === 1 ? '启用' : '禁用'
            const statusColor = rule.isEnabled === 1 ? '#67c23a' : '#f56c6c'

            tableHtml += `
              <tr>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${rule.ruleName}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${rule.ruleTypeName}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${rule.description}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; font-family: monospace;">${rule.formula}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: center;">
                  <span style="color: ${statusColor}; font-weight: bold;">${statusText}</span>
                </td>
              </tr>
            `
          })

          tableHtml += `
              </tbody>
            </table>
          `

          // 显示规则对话框
          this.$alert(`
            <div style="text-align: left;">
              <h3>成本分摊规则</h3>
              <p>以下是系统中可用的成本分摊规则：</p>
              ${tableHtml}
            </div>
          `, '分摊规则', {
            dangerouslyUseHTMLString: true,
            customClass: 'allocation-rule-dialog'
          })
        } else {
          this.$message.error(response.msg || '获取分摊规则失败')
        }
      } catch (error) {
        console.error('获取分摊规则失败:', error)
        this.$message.error('获取分摊规则失败')
      }
    },
    async handleAllocationBasis() {
      try {
        const { bookId, tenantId } = this.getUserContext()
        const currentPeriod = this.searchForm.allocationPeriod || new Date().toISOString().substring(0, 7)

        const response = await getAllocationBasis({
          period: currentPeriod,
          bookId,
          tenantId
        })

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const basisData = response.data

          if (!basisData || !basisData.basisData || basisData.basisData.length === 0) {
            this.$message.info('当前期间暂无分摊基础数据')
            return
          }

          // 构建基础数据表格HTML
          let tableHtml = `
            <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
              <thead>
                <tr style="background-color: #f5f7fa;">
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">成本中心</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">基础类型</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">基础值</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">占比</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">单位</th>
                </tr>
              </thead>
              <tbody>
          `

          const totalBasisValue = basisData.totalBasisValue || 0
          basisData.basisData.forEach(basis => {
            const percentage = totalBasisValue > 0 ? (basis.basisValue / totalBasisValue * 100).toFixed(2) : '0.00'

            tableHtml += `
              <tr>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${basis.centerName}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${basis.basisTypeName}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">${basis.basisValue}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">${percentage}%</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${basis.unit}</td>
              </tr>
            `
          })

          tableHtml += `
              </tbody>
              <tfoot>
                <tr style="background-color: #f5f7fa; font-weight: bold;">
                  <td style="border: 1px solid #dcdfe6; padding: 8px;">合计</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px;">-</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">${totalBasisValue}</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">100.00%</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px;">-</td>
                </tr>
              </tfoot>
            </table>
          `

          // 显示基础数据对话框
          this.$alert(`
            <div style="text-align: left;">
              <h3>成本分摊基础数据</h3>
              <p><strong>统计期间：</strong>${currentPeriod}</p>
              <p><strong>数据说明：</strong>各成本中心的分摊基础数据，用于计算分摊比例</p>
              ${tableHtml}
              <p style="margin-top: 15px; color: #909399; font-size: 12px;">
                提示：分摊比例 = 各中心基础值 ÷ 总基础值 × 100%
              </p>
            </div>
          `, '分摊基础数据', {
            dangerouslyUseHTMLString: true,
            customClass: 'allocation-basis-dialog'
          })
        } else {
          this.$message.error(response.msg || '获取分摊基础数据失败')
        }
      } catch (error) {
        console.error('获取分摊基础数据失败:', error)
        this.$message.error('获取分摊基础数据失败')
      }
    },
    async handleBatchAudit() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请选择要审核的记录')
        return
      }

      // 过滤出可以审核的记录（状态为已分摊）
      const auditableRecords = this.multipleSelection.filter(item => item.allocationStatus === 3)
      if (auditableRecords.length === 0) {
        this.$message.warning('选中的记录中没有可审核的记录（仅已分摊状态可审核）')
        return
      }

      this.$confirm(`确认审核选中的 ${auditableRecords.length} 条记录吗？`, '批量审核确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const allocationIds = auditableRecords.map(item => item.allocationId)
          const response = await batchAuditCostAllocation(allocationIds, {
            auditResult: 1, // 审核通过
            auditRemark: '批量审核'
          })

          if (response.code === 1) {
            // axios拦截器会将后端的 code=1 转换为 code=200
            const result = response.data
            if (result.failedCount > 0) {
              this.$message.warning(`审核完成，成功${result.successCount}条，失败${result.failedCount}条`)
            } else {
              this.$message.success(`批量审核成功，共审核${result.successCount}条记录`)
            }
            this.loadData()
          } else {
            this.$message.error(response.msg || '批量审核失败')
          }
        } catch (error) {
          console.error('批量审核失败:', error)
          this.$message.error('批量审核失败')
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    async handleBatchAllocate() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请选择要分摊的记录')
        return
      }

      // 过滤出可以分摊的记录（状态为待分摊或分摊中）
      const allocatableRecords = this.multipleSelection.filter(item => item.allocationStatus < 3)
      if (allocatableRecords.length === 0) {
        this.$message.warning('选中的记录中没有可分摊的记录（仅待分摊或分摊中状态可分摊）')
        return
      }

      this.$confirm(`确认将选中的 ${allocatableRecords.length} 条记录标记为已分摊吗？`, '批量分摊确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const allocationIds = allocatableRecords.map(item => item.allocationId)
          const response = await batchAllocateCostAllocation(allocationIds, {
            allocateResult: 1, // 分摊成功
            allocateRemark: '批量分摊'
          })

          if (response.code === 1) {
            // axios拦截器会将后端的 code=1 转换为 code=200
            const result = response.data
            if (result.failedCount > 0) {
              this.$message.warning(`分摊完成，成功${result.successCount}条，失败${result.failedCount}条`)
            } else {
              this.$message.success(`批量分摊成功，共分摊${result.successCount}条记录`)
            }
            this.loadData()
          } else {
            this.$message.error(response.msg || '批量分摊失败')
          }
        } catch (error) {
          console.error('批量分摊失败:', error)
          this.$message.error('批量分摊失败')
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    async handleAllocate(row) {
      this.$confirm('确认将该记录标记为已分摊吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await allocateCostAllocation(row.allocationId, {
            allocateResult: 1, // 分摊成功
            allocateRemark: '单个分摊'
          })

          if (response.code === 1) {
            // axios拦截器会将后端的 code=1 转换为 code=200
            this.$message.success('分摊成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '分摊失败')
          }
        } catch (error) {
          console.error('分摊失败:', error)
          this.$message.error('分摊失败')
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    async handleBatchCancel() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }

      // 过滤出可以取消的记录（状态不是已审核的）
      const cancellableRecords = this.multipleSelection.filter(item => item.allocationStatus < 4)
      if (cancellableRecords.length === 0) {
        this.$message.warning('选中的记录中没有可删除的记录（已审核状态不可删除）')
        return
      }

      this.$confirm(`确认删除选中的 ${cancellableRecords.length} 条记录吗？删除后将无法恢复。`, '批量删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const allocationIds = cancellableRecords.map(item => item.allocationId)
          const response = await batchDeleteCostAllocation(allocationIds)

          if (response.code === 1) {
            // axios拦截器会将后端的 code=1 转换为 code=200
            const result = response.data
            if (result.failedCount > 0) {
              this.$message.warning(`删除完成，成功${result.successCount}条，失败${result.failedCount}条`)
            } else {
              this.$message.success(`批量删除成功，共删除${result.successCount}条记录`)
            }
            this.loadData()
          } else {
            this.$message.error(response.msg || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }).catch(() => {
        // 用户取消操作
      })
    },
    async handleView(row) {
      try {
        // 构建详情信息 - 直接使用当前行数据
        const detailInfo = {
          allocationNo: row.allocationNo,
          allocationPeriod: row.allocationPeriod,
          sourceCenterName: row.sourceCenterName,
          allocationMethodName: row.allocationMethodName,
          totalAmount: row.totalAmount,
          allocationStatusName: row.allocationStatusName,
          allocationDate: row.allocationDate,
          totalAllocatedAmount: row.totalAllocatedAmount || 0,
          targetCenterCount: row.targetCenterCount || 0,
          isGenerateVoucher: row.isGenerateVoucher === 1
        }

        // 显示详情对话框
        this.$alert(`
          <div style="text-align: left;">
            <h3>成本分摊详情</h3>
            <p><strong>分摊单号：</strong>${detailInfo.allocationNo}</p>
            <p><strong>分摊期间：</strong>${detailInfo.allocationPeriod}</p>
            <p><strong>源成本中心：</strong>${detailInfo.sourceCenterName}</p>
            <p><strong>分摊方法：</strong>${detailInfo.allocationMethodName}</p>
            <p><strong>分摊总额：</strong>￥${this.formatCurrency(detailInfo.totalAmount)}</p>
            <p><strong>状态：</strong>${detailInfo.allocationStatusName}</p>
            <p><strong>分摊日期：</strong>${detailInfo.allocationDate}</p>
            <p><strong>目标中心数：</strong>${detailInfo.targetCenterCount}</p>
            <p><strong>已分摊金额：</strong>￥${this.formatCurrency(detailInfo.totalAllocatedAmount)}</p>
            <p><strong>是否生成凭证：</strong>${detailInfo.isGenerateVoucher ? '是' : '否'}</p>
          </div>
        `, '成本分摊详情', {
          dangerouslyUseHTMLString: true,
          customClass: 'allocation-detail-dialog'
        })
      } catch (error) {
        console.error('查看成本分摊详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    async handleDetail(row) {
      try {
        const response = await getAllocationDetailList(row.allocationId, {
          pageNumber: 1,
          pageSize: 100
        })

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          const details = response.data.tlist || []

          if (details.length === 0) {
            this.$message.info('暂无分摊明细数据')
            return
          }

          // 构建明细表格HTML
          let tableHtml = `
            <table style="width: 100%; border-collapse: collapse; margin-top: 10px;">
              <thead>
                <tr style="background-color: #f5f7fa;">
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">目标成本中心</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">分摊基础值</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">分摊比例</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">分摊金额</th>
                  <th style="border: 1px solid #dcdfe6; padding: 8px; text-align: left;">备注</th>
                </tr>
              </thead>
              <tbody>
          `

          let totalAllocatedAmount = 0
          details.forEach(detail => {
            const targetCenterName = detail.targetCenterName || `成本中心${detail.targetCenterId}`
            const basisValue = detail.allocationBasisValue || 0
            const ratio = (detail.allocationRatio || 0) * 100
            const allocatedAmount = detail.allocatedAmount || 0
            const remark = detail.remark || '-'

            totalAllocatedAmount += parseFloat(allocatedAmount)

            tableHtml += `
              <tr>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${targetCenterName}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">${basisValue}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">${ratio.toFixed(2)}%</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">￥${this.formatCurrency(allocatedAmount)}</td>
                <td style="border: 1px solid #dcdfe6; padding: 8px;">${remark}</td>
              </tr>
            `
          })

          tableHtml += `
              </tbody>
              <tfoot>
                <tr style="background-color: #f5f7fa; font-weight: bold;">
                  <td style="border: 1px solid #dcdfe6; padding: 8px;">合计</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">-</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">100.00%</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px; text-align: right;">￥${this.formatCurrency(totalAllocatedAmount)}</td>
                  <td style="border: 1px solid #dcdfe6; padding: 8px;">-</td>
                </tr>
              </tfoot>
            </table>
          `

          // 显示明细对话框
          this.$alert(`
            <div style="text-align: left;">
              <h3>分摊明细 - ${row.allocationNo}</h3>
              <p><strong>分摊期间：</strong>${row.allocationPeriod}</p>
              <p><strong>源成本中心：</strong>${row.sourceCenterName}</p>
              <p><strong>分摊总额：</strong>￥${this.formatCurrency(row.totalAmount)}</p>
              ${tableHtml}
            </div>
          `, '分摊明细', {
            dangerouslyUseHTMLString: true,
            customClass: 'allocation-detail-dialog'
          })
        } else {
          this.$message.error(response.msg || '获取明细失败')
        }
      } catch (error) {
        console.error('获取分摊明细失败:', error)
        this.$message.error('获取明细失败')
      }
    },
    async handleAudit(row) {
      try {
        await this.$confirm('确认审核该分摊记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await auditCostAllocation(row.allocationId, {
          auditResult: 1, // 审核通过
          auditRemark: '单个审核'
        })

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('审核成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '审核失败')
        }
      } catch (error) {
        if (error !== 'cancel') { // 用户取消操作不显示错误
          console.error('审核失败:', error)
          this.$message.error('审核失败')
        }
      }
    },
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该分摊记录吗？取消后将无法恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await cancelCostAllocation(row.allocationId)

        if (response.code === 1) {
          // axios拦截器会将后端的 code=1 转换为 code=200
          this.$message.success('取消成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') { // 用户取消操作不显示错误
          console.error('取消成本分摊失败:', error)
          this.$message.error('取消失败')
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
    formatCurrency(amount) {
      if (amount === null || amount === undefined || amount === '') {
        return '0.00'
      }
      // 处理数字格式，保留两位小数
      const num = parseFloat(amount)
      if (isNaN(num)) {
        return '0.00'
      }
      return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    getAllocationMethodTag(method) {
      const tagMap = {
        // 前端 dropdown 的长名（新建数据使用）
        'QUANTITY_BASED': 'primary',
        'AMOUNT_BASED': 'success',
        'RATIO_BASED': 'warning',
        'HOUR_BASED': 'info',
        // 数据库 seed/历史数据的简短名（兼容）
        'AVG': 'success',
        'RATIO': 'warning',
        'STAIR': 'danger',
        'QUANTITY': 'primary',
        'AMOUNT': 'success',
        'HOUR': 'info'
      }
      return tagMap[method] || 'info'
    },
    updateAllocationBasisData(targetCenterIds) {
      // 更新分摊基础数据，确保显示正确的成本中心名称
      this.allocationBasisData = targetCenterIds.map(centerId => {
        // 从成本中心选项列表中查找对应的名称
        const costCenter = this.costCenterOptions.find(option => option.value === centerId);
        return {
          centerId,
          centerName: costCenter ? costCenter.label : `成本中心${centerId}`,
          basisValue: 1 // 设置默认分摊基础值为1，而不是0
        };
      });

      console.log('更新分摊基础数据:', this.allocationBasisData)
    },
    formatMonth(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      return `${year}-${month}`
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
    getAllocationMethodName(method) {
      const methodMap = {
        // 前端 dropdown 的长名（新建数据使用）
        'QUANTITY_BASED': '数量基础',
        'AMOUNT_BASED': '金额基础',
        'RATIO_BASED': '比例基础',
        'HOUR_BASED': '工时基础',
        // 数据库 seed/历史数据的简短名（兼容）
        'AVG': '平均分摊',
        'RATIO': '比例分摊',
        'STAIR': '阶梯分摊',
        'QUANTITY': '数量基础',
        'AMOUNT': '金额基础',
        'HOUR': '工时基础'
      }
      return methodMap[method] || method || '-'
    },
    getAllocationStatusName(status) {
      const statusMap = {
        1: '待分摊',
        2: '分摊中',
        3: '已分摊',
        4: '已审核'
      }
      return statusMap[status] || '未知状态'
    },
    // 将分摊基础数据从数组格式转换为Map格式，适配后端期望的数据结构
    convertAllocationBasisToMap(allocationBasisData) {
      const allocationBasisMap = {}
      console.log('转换分摊基础数据 - 原始数据:', allocationBasisData)

      allocationBasisData.forEach(item => {
        if (item.centerId) {
          // 确保basisValue是有效的数字，如果没有则使用默认值1
          const basisValue = item.basisValue > 0 ? item.basisValue : 1
          allocationBasisMap[item.centerId] = basisValue
          console.log(`添加分摊基础数据 - 中心ID: ${item.centerId}, 基础值: ${basisValue}`)
        }
      })

      console.log('转换后的分摊基础Map:', allocationBasisMap)
      return allocationBasisMap
    }
  },
  watch: {
    'allocationForm.targetCostCenterIds': {
      handler(newVal) {
        // 当选择目标成本中心时，初始化分摊基础数据
        this.updateAllocationBasisData(newVal);
      },
      deep: true
    },
    costCenterOptions: {
      handler(newOptions) {
        // 当成本中心选项加载完成后，更新分摊基础数据中的名称
        if (newOptions.length > 0 && this.allocationForm.targetCostCenterIds.length > 0) {
          this.updateAllocationBasisData(this.allocationForm.targetCostCenterIds);
        }
      },
      deep: true
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-allocation-container {
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

.allocation-overview {
  margin-bottom: 20px;
  
  .overview-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    
    .card-header {
      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 16px;
      }
    }
    
    .card-content {
      .amount {
        font-size: 28px;
        font-weight: 600;
        color: #409EFF;
        margin-bottom: 8px;
      }
      
      .trend {
        font-size: 14px;
        color: #909399;
        
        i {
          margin-right: 4px;
        }
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
</style>
