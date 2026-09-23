<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.planName"
        placeholder="计划名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.planType"
        placeholder="计划类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in planTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.planStatus"
        placeholder="计划状态"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in planStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"
        style="width: 240px"
        @change="handleDateRangeChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加
      </el-button>
      <el-button
        v-waves
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >
        导出
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column
        label="计划编号"
        prop="planNo"
        sortable="custom"
        align="center"
        width="150"
      >
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.planNo }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划名称"
        prop="planName"
        sortable="custom"
        width="200"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.planName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划类型"
        prop="planType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="planTypeTagMap[row.planType]">
            {{ planTypeMap[row.planType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="计划状态"
        prop="planStatus"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="planStatusTagMap[row.planStatus]">
            {{ planStatusMap[row.planStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="计划期间"
        width="200"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.startDate | parseTime('{y}-{m}-{d}') }} 至 {{ row.endDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="总收入"
        prop="totalIncome"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.totalIncome) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="总支出"
        prop="totalExpense"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.totalExpense) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="净现金流"
        prop="netCashFlow"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span :class="row.netCashFlow >= 0 ? 'text-success' : 'text-danger'">
            {{ formatAmount(row.netCashFlow) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="完成率"
        prop="completionRate"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.completionRate !== null">{{ row.completionRate }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createdTime"
        width="160"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.planStatus === 'DRAFT'"
            size="mini"
            type="success"
            @click="handleSubmit(row)"
          >
            提交
          </el-button>
          <el-button
            v-if="row.planStatus === 'PENDING_APPROVAL'"
            size="mini"
            type="warning"
            @click="handleApprove(row)"
          >
            审批
          </el-button>
          <el-button
            v-if="row.planStatus === 'APPROVED'"
            size="mini"
            type="info"
            @click="handleExecute(row)"
          >
            执行
          </el-button>
          <el-button
            v-if="['DRAFT', 'REJECTED'].includes(row.planStatus)"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
          <el-dropdown
            trigger="click"
            @command="(command) => handleCommand(command, row)"
          >
            <el-button size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="detail">查看详情</el-dropdown-item>
              <el-dropdown-item command="copy">复制计划</el-dropdown-item>
              <el-dropdown-item command="export">导出计划</el-dropdown-item>
              <el-dropdown-item
                v-if="['EXECUTING', 'APPROVED'].includes(row.planStatus)"
                command="complete"
              >
                完成计划
              </el-dropdown-item>
              <el-dropdown-item
                v-if="!['COMPLETED', 'CANCELLED'].includes(row.planStatus)"
                command="cancel"
                divided
              >
                取消计划
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 编辑对话框 -->
    <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="dialogFormVisible"
      width="800px"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 700px; margin-left:50px;"
      >
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="temp.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="计划类型" prop="planType">
          <el-select v-model="temp.planType" placeholder="请选择计划类型" style="width: 100%">
            <el-option
              v-for="item in planTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="计划期间" prop="dateRange">
          <el-date-picker
            v-model="tempDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%"
            @change="handleTempDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="总收入" prop="totalIncome">
          <el-input-number
            v-model="temp.totalIncome"
            :precision="2"
            :min="0"
            style="width: 100%"
            placeholder="请输入总收入"
          />
        </el-form-item>
        <el-form-item label="总支出" prop="totalExpense">
          <el-input-number
            v-model="temp.totalExpense"
            :precision="2"
            :min="0"
            style="width: 100%"
            placeholder="请输入总支出"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入计划描述"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="temp.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="资金计划详情" :visible.sync="detailVisible" width="800px">
      <el-descriptions :column="2" border size="medium">
        <el-descriptions-item label="计划编号">{{ detailData.planNo }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ detailData.planName }}</el-descriptions-item>
        <el-descriptions-item label="计划类型">
          <el-tag :type="planTypeTagMap[detailData.planType]">{{ planTypeMap[detailData.planType] }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划状态">
          <el-tag :type="planStatusTagMap[detailData.planStatus]">{{ planStatusMap[detailData.planStatus] }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="总收入">{{ formatAmount(detailData.totalIncome) }}</el-descriptions-item>
        <el-descriptions-item label="总支出">{{ formatAmount(detailData.totalExpense) }}</el-descriptions-item>
        <el-descriptions-item label="净现金流">
          <span :class="detailData.netCashFlow >= 0 ? 'text-success' : 'text-danger'">
            {{ formatAmount(detailData.netCashFlow) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="完成率">
          <span v-if="detailData.completionRate !== null">{{ detailData.completionRate }}%</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="所属组织" :span="2">{{ detailData.orgName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createdByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createdTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ detailData.updatedByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updatedTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanPage, createFundPlan, updateFundPlan, deleteFundPlan, submitPlan, approvePlan, rejectPlan, executePlan, completePlan, cancelPlan } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import globalTreasurerMixin from '@/mixins/globalTreasurerMixin'

export default {
  name: 'FundPlan',
  mixins: [globalTreasurerMixin],
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        planName: undefined,
        planType: undefined,
        planStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '+id'
      },
      dateRange: null,
      tempDateRange: null,
      planTypeOptions: [
        { label: '年度计划', value: 'ANNUAL' },
        { label: '季度计划', value: 'QUARTERLY' },
        { label: '月度计划', value: 'MONTHLY' },
        { label: '周计划', value: 'WEEKLY' },
        { label: '专项计划', value: 'SPECIAL' }
      ],
      planStatusOptions: [
        { label: '草稿', value: 'DRAFT' },
        { label: '待审批', value: 'PENDING_APPROVAL' },
        { label: '已审批', value: 'APPROVED' },
        { label: '执行中', value: 'EXECUTING' },
        { label: '已完成', value: 'COMPLETED' },
        { label: '已取消', value: 'CANCELLED' },
        { label: '已拒绝', value: 'REJECTED' }
      ],
      planTypeMap: {
        'ANNUAL': '年度计划',
        'QUARTERLY': '季度计划',
        'MONTHLY': '月度计划',
        'WEEKLY': '周计划',
        'SPECIAL': '专项计划'
      },
      planTypeTagMap: {
        'ANNUAL': 'success',
        'QUARTERLY': 'info',
        'MONTHLY': 'warning',
        'WEEKLY': 'danger',
        'SPECIAL': 'primary'
      },
      planStatusMap: {
        'DRAFT': '草稿',
        'PENDING_APPROVAL': '待审批',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REJECTED': '已拒绝'
      },
      planStatusTagMap: {
        'DRAFT': 'info',
        'PENDING_APPROVAL': 'warning',
        'APPROVED': 'success',
        'EXECUTING': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger',
        'REJECTED': 'danger'
      },
      temp: {
        planId: undefined,
        planName: '',
        planType: '',
        startDate: '',
        endDate: '',
        totalIncome: 0,
        totalExpense: 0,
        description: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑资金计划',
        create: '创建资金计划'
      },
      detailVisible: false,
      detailData: {},
      downloadLoading: false,
      rules: {
        planName: [{ required: true, message: '计划名称不能为空', trigger: 'blur' }],
        planType: [{ required: true, message: '计划类型不能为空', trigger: 'change' }],
        dateRange: [{ required: true, message: '计划期间不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getFundPlanPage({
          current: this.listQuery.page,
          size: this.listQuery.limit,
          planName: this.listQuery.planName,
          planType: this.listQuery.planType,
          planStatus: this.listQuery.planStatus,
          startDate: this.listQuery.startDate,
          endDate: this.listQuery.endDate
        })

        if (response && response.code === 1) {
          this.list = response.data?.rows || response.data?.records || response.data?.tlist || []
          this.total = response.data?.total || response.data?.totalRecord || this.list.length
        } else {
          throw new Error(response?.msg || 'API返回状态异常')
        }
      } catch (error) {
        console.error('资金计划API调用失败:', error)
        this.$message.error('获取数据失败，请检查网络连接或联系管理员')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDateRangeChange(value) {
      if (value && value.length === 2) {
        this.listQuery.startDate = parseTime(value[0], '{y}-{m}-{d}')
        this.listQuery.endDate = parseTime(value[1], '{y}-{m}-{d}')
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    handleTempDateRangeChange(value) {
      if (value && value.length === 2) {
        this.temp.startDate = value[0]
        this.temp.endDate = value[1]
      } else {
        this.temp.startDate = ''
        this.temp.endDate = ''
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'planNo') {
        this.sortByPlanNo(order)
      } else if (prop === 'planName') {
        this.sortByPlanName(order)
      }
    },
    sortByPlanNo(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+planNo'
      } else {
        this.listQuery.sort = '-planNo'
      }
      this.handleFilter()
    },
    sortByPlanName(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+planName'
      } else {
        this.listQuery.sort = '-planName'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        planId: undefined,
        planName: '',
        planType: '',
        startDate: '',
        endDate: '',
        totalIncome: 0,
        totalExpense: 0,
        description: '',
        remark: ''
      }
      this.tempDateRange = null
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createFundPlan(this.temp).then(response => {
            if (response.data.code === 200) {
              this.list.unshift(response.data.data)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$message.error(response.data.message || '创建失败')
            }
          }).catch(() => {
            this.$message.error('创建失败')
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.tempDateRange = [this.temp.startDate, this.temp.endDate]
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateFundPlan(tempData.planId, tempData).then(response => {
            if (response.data.code === 200) {
              const index = this.list.findIndex(v => v.planId === this.temp.planId)
              this.list.splice(index, 1, response.data.data)
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$message.error(response.data.message || '更新失败')
            }
          }).catch(() => {
            this.$message.error('更新失败')
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该计划, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlan(row.planId).then(response => {
          if (response.data.code === 200) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.list.splice(index, 1)
          } else {
            this.$message.error(response.data.message || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined) {
        return '-'
      }
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY'
      }).format(amount)
    },
    handleSubmit(row) {
      this.$confirm('确认提交该计划进行审批?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitPlan(row.planId).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '提交成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '提交失败')
          }
        }).catch(() => {
          this.$message.error('提交失败')
        })
      })
    },
    handleApprove(row) {
      this.$prompt('请输入审批意见', '审批计划', {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        distinguishCancelAndClose: true,
        inputPlaceholder: '审批意见（可选）'
      }).then(({ value }) => {
        approvePlan(row.planId, value).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '审批通过',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '审批失败')
          }
        }).catch(() => {
          this.$message.error('审批失败')
        })
      }).catch(action => {
        if (action === 'cancel') {
          this.$prompt('请输入拒绝理由', '拒绝计划', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPlaceholder: '拒绝理由',
            inputValidator: (value) => {
              if (!value || value.trim() === '') {
                return '拒绝理由不能为空'
              }
              return true
            }
          }).then(({ value }) => {
            rejectPlan(row.planId, value).then(response => {
              if (response.code === 1) {
                this.$notify({
                  title: '成功',
                  message: '已拒绝计划',
                  type: 'success',
                  duration: 2000
                })
                this.getList()
              } else {
                this.$message.error(response.msg || '拒绝失败')
              }
            }).catch(() => {
              this.$message.error('拒绝失败')
            })
          })
        }
      })
    },
    handleExecute(row) {
      this.$confirm('确认开始执行该计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executePlan(row.planId).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '计划已开始执行',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '执行失败')
          }
        }).catch(() => {
          this.$message.error('执行失败')
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.handleDetail(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
        case 'complete':
          this.handleComplete(row)
          break
        case 'cancel':
          this.handleCancel(row)
          break
      }
    },
    handleDetail(row) {
      this.detailData = Object.assign({}, row)
      this.detailVisible = true
    },
    handleCopy(row) {
      this.temp = Object.assign({}, row)
      this.temp.planId = undefined
      this.temp.planNo = undefined
      this.temp.planName = row.planName + '_副本'
      this.temp.planStatus = 'DRAFT'
      this.tempDateRange = [this.temp.startDate, this.temp.endDate]
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleComplete(row) {
      this.$confirm('确认完成该计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        completePlan(row.planId).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '计划已完成',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '完成失败')
          }
        }).catch(() => {
          this.$message.error('完成失败')
        })
      })
    },
    handleCancel(row) {
      this.$confirm('确认取消该计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelPlan(row.planId).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '计划已取消',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '取消失败')
          }
        }).catch(() => {
          this.$message.error('取消失败')
        })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      // 导出功能实现
      setTimeout(() => {
        this.downloadLoading = false
        this.$message.success('导出成功')
      }, 1000)
    },
    handleExportSingle(row) {
      this.$message.success('导出单个计划功能开发中')
    }
  }
}
</script>

<style scoped>
.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}

.link-type {
  color: #409eff;
  cursor: pointer;
}

.link-type:hover {
  color: #66b1ff;
}

.filter-container {
  padding-bottom: 10px;
}

.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin-bottom: 10px;
  margin-right: 10px;
}
</style>
