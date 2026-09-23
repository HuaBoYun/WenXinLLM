<template>
  <div class="activity-cost-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.activityName"
                clearable
                placeholder="作业名称"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.activityType"
                clearable
                placeholder="作业类型"
                style="width: 180px"
              >
                <el-option label="生产作业" value="1" />
                <el-option label="辅助作业" value="2" />
                <el-option label="管理作业" value="3" />
                <el-option label="销售作业" value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.costDriver"
                clearable
                placeholder="成本动因"
                style="width: 180px"
              >
                <el-option label="机器小时" value="machine_hours" />
                <el-option label="人工小时" value="labor_hours" />
                <el-option label="产品数量" value="product_quantity" />
                <el-option label="订单数量" value="order_quantity" />
                <el-option label="检验次数" value="inspection_times" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="queryForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
        
        <vab-query-form-right-panel>
          <el-button
            icon="el-icon-plus"
            type="primary"
            @click="handleAdd"
          >
            新增作业
          </el-button>
          <el-button
            icon="el-icon-setting"
            @click="handleAllocation"
          >
            成本分配
          </el-button>
          <el-button
            icon="el-icon-download"
            @click="handleExport"
          >
            导出
          </el-button>
          <el-button
            icon="el-icon-refresh-right"
            @click="fetchData"
          >
            刷新
          </el-button>
        </vab-query-form-right-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <el-table
        ref="tableSort"
        v-loading="listLoading"
        :data="list"
        :element-loading-text="elementLoadingText"
        @selection-change="setSelectRows"
        @sort-change="tableSortChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          label="序号"
          type="index"
          width="80"
          :index="getTableIndex"
        />
        <el-table-column
          prop="activityCode"
          label="作业编码"
          min-width="120"
          sortable="custom"
          show-overflow-tooltip
        />
        <el-table-column
          prop="activityName"
          label="作业名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="activityType"
          label="作业类型"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getActivityTypeTag(scope.row.activityType)">
              {{ getActivityTypeName(scope.row.activityType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="costDriver"
          label="成本动因"
          width="120"
        >
          <template slot-scope="scope">
            {{ getCostDriverName(scope.row.costDriver) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="driverQuantity"
          label="动因数量"
          width="100"
          align="right"
        />
        <el-table-column
          prop="totalCost"
          label="总成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.totalCost) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="unitCost"
          label="单位成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.unitCost) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="allocatedCost"
          label="已分配成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.allocatedCost) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="unallocatedCost"
          label="未分配成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            <span :class="getUnallocatedClass(scope.row.unallocatedCost)">
              {{ formatAmount(scope.row.unallocatedCost) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="department"
          label="所属部门"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="status"
          label="状态"
          width="80"
        >
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="150"
          sortable="custom"
        />
        <el-table-column
          label="操作"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              @click="handleAllocationDetail(scope.row)"
            >
              分配
            </el-button>
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <activity-cost-edit
      ref="edit"
      @fetch-data="fetchData"
    />

    <!-- 作业详情对话框 -->
    <activity-cost-detail
      ref="detail"
    />

    <!-- 成本分配对话框 -->
    <activity-cost-allocation
      ref="allocation"
      @fetch-data="fetchData"
    />
  </div>
</template>

<script>
import { getActivityCostPage, deleteActivityCost } from '@/api/financialSharing/specialCost'
import ActivityCostEdit from './components/ActivityCostEdit'
import ActivityCostDetail from './components/ActivityCostDetail'
import ActivityCostAllocation from './components/ActivityCostAllocation'

export default {
  name: 'ActivityCostIndex',
  components: {
    ActivityCostEdit,
    ActivityCostDetail,
    ActivityCostAllocation
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      selectRows: '',
      elementLoadingText: '正在加载...',
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        activityName: '',
        activityType: '',
        costDriver: '',
        dateRange: []
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    setSelectRows(val) {
      this.selectRows = val
    },
    handleAdd() {
      this.$refs.edit.showEdit()
    },
    handleEdit(row) {
      this.$refs.edit.showEdit(row)
    },
    handleView(row) {
      this.$refs.detail.showDetail(row)
    },
    handleAllocation() {
      if (!this.selectRows || this.selectRows.length === 0) {
        this.$baseMessage('请选择要分配的作业', 'warning')
        return
      }
      this.$refs.allocation.showAllocation(this.selectRows)
    },
    handleAllocationDetail(row) {
      this.$refs.allocation.showAllocation([row])
    },
    async handleDelete(row) {
      if (
        await this.$baseConfirm('你确定要删除当前作业吗', null, {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
      ) {
        const { code, msg } = await deleteActivityCost(row.id)
        if (code === 200) {
          this.$baseMessage(msg, 'success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error')
        }
      }
    },
    handleExport() {
      try {
        const data = this.list || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '作业成本数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    async fetchData() {
      this.listLoading = true
      const { code, data } = await getActivityCostPage(this.queryForm)
      if (code === 200) {
        this.list = data.records
        this.total = data.total
      }
      this.listLoading = false
    },
    resetSearch() {
      this.$refs.form.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 20,
        activityName: '',
        activityType: '',
        costDriver: '',
        dateRange: []
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    tableSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderType = order === 'ascending' ? 'asc' : 'desc'
      this.fetchData()
    },
    getTableIndex(index) {
      return (this.queryForm.pageNumber - 1) * this.queryForm.pageSize + index + 1
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getActivityTypeName(type) {
      const typeMap = {
        '1': '生产作业',
        '2': '辅助作业',
        '3': '管理作业',
        '4': '销售作业'
      }
      return typeMap[type] || '未知'
    },
    getActivityTypeTag(type) {
      const tagMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return tagMap[type] || 'info'
    },
    getCostDriverName(driver) {
      const driverMap = {
        'machine_hours': '机器小时',
        'labor_hours': '人工小时',
        'product_quantity': '产品数量',
        'order_quantity': '订单数量',
        'inspection_times': '检验次数'
      }
      return driverMap[driver] || driver
    },
    getStatusName(status) {
      const statusMap = {
        '1': '活跃',
        '2': '暂停',
        '3': '停用'
      }
      return statusMap[status] || '未知'
    },
    getStatusTag(status) {
      const tagMap = {
        '1': 'success',
        '2': 'warning',
        '3': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getUnallocatedClass(amount) {
      if (amount > 0) return 'text-warning'
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
.activity-cost-container {
  padding: 0;
}

.text-warning {
  color: #e6a23c;
}
</style>
