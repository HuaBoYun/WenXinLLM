<template>
  <div class="my-approved-container">
    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预算期间">
          <el-input v-model="queryForm.period" placeholder="请输入预算期间" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="预算版本">
          <el-input v-model="queryForm.version" placeholder="请输入预算版本" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="模型名称">
          <el-input v-model="queryForm.modelId" placeholder="请输入模型ID" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="审批状态">
          <el-select v-model="queryForm.approvalStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批时间">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <span>我的已审批</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
        <el-table-column prop="period" label="预算期间" width="120" align="center" />
        <el-table-column prop="version" label="预算版本" width="120" align="center" />
        <el-table-column prop="nodeName" label="审批节点" width="120" align="center" />
        <el-table-column prop="submitUserName" label="提交人" width="100" align="center" />
        <el-table-column label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.approvalStatus === 'APPROVED'" type="success" size="small">已通过</el-tag>
            <el-tag v-else-if="scope.row.approvalStatus === 'REJECTED'" type="danger" size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approvalComment" label="审批意见" min-width="150" show-overflow-tooltip />
        <el-table-column prop="approvalTime" label="审批时间" width="160" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">
              查看详情
            </el-button>
            <el-button type="text" size="small" icon="el-icon-document" @click="handleViewHistory(scope.row)">
              审批历史
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="queryForm.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script>
import { getMyApprovedList } from '@/api/financialSharing/budgetPlanning/budgetApproval'

export default {
  name: 'MyApproved',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      dateRange: [],
      queryForm: {
        period: '',
        version: '',
        modelId: '',
        approvalStatus: '',
        startTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        // 处理日期范围
        if (this.dateRange && this.dateRange.length === 2) {
          this.queryForm.startTime = this.dateRange[0]
          this.queryForm.endTime = this.dateRange[1]
        } else {
          this.queryForm.startTime = ''
          this.queryForm.endTime = ''
        }

        const res = await getMyApprovedList(this.queryForm)
        if (res.code === 1) {
          this.tableData = res.data.list
          this.total = res.data.total
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    // 重置
    handleReset() {
      this.dateRange = []
      this.queryForm = {
        period: '',
        version: '',
        modelId: '',
        approvalStatus: '',
        startTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 10
      }
      this.loadData()
    },
    // 分页
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.loadData()
    },
    // 查看详情
    handleView(row) {
      this.$router.push({
        path: '/financialSharing/budgetPlanning/budgetData/detail',
        query: { dataId: row.dataId }
      })
    },
    // 查看审批历史
    handleViewHistory(row) {
      this.$router.push({
        path: '/financialSharing/budgetPlanning/budgetApproval/history',
        query: { dataId: row.dataId }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.my-approved-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>

