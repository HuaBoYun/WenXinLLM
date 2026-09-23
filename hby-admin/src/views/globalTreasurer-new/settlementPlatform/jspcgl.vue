<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="批次状态" prop="batchStatus">
        <el-select v-model="queryParams.batchStatus" placeholder="请选择批次状态" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="失败" value="FAILED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="batchList" border>
      <el-table-column label="批次编号" prop="batchNo" width="180" />
      <el-table-column label="批次名称" prop="batchName" />
      <el-table-column label="批次状态" prop="batchStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.batchStatus)" size="mini">{{ scope.row.batchStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="总笔数" prop="totalCount" width="100" />
      <el-table-column label="成功笔数" prop="successCount" width="100" />
      <el-table-column label="失败笔数" prop="failedCount" width="100" />
      <el-table-column label="创建时间" prop="createTime" width="160" />
    </el-table>

    <el-pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
export default {
  name: 'JspcglPage',
  data() {
    return {
      loading: false,
      total: 0,
      batchList: [],
      queryParams: { pageNum: 1, pageSize: 10, batchStatus: '' }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = false
      this.batchList = []
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    getStatusType(status) {
      const map = { PENDING: 'warning', PROCESSING: '', COMPLETED: 'success', FAILED: 'danger' }
      return map[status] || 'info'
    }
  }
}
</script>

