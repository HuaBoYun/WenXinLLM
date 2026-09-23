<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="queryParams.ruleName" placeholder="请输入规则名称" clearable />
      </el-form-item>
      <el-form-item label="规则状态" prop="ruleStatus">
        <el-select v-model="queryParams.ruleStatus" placeholder="请选择规则状态" clearable>
          <el-option label="启用" value="ENABLED" />
          <el-option label="禁用" value="DISABLED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" border>
      <el-table-column label="规则编号" prop="ruleNo" width="150" />
      <el-table-column label="规则名称" prop="ruleName" />
      <el-table-column label="规则类型" prop="ruleType" width="120" />
      <el-table-column label="优先级" prop="priority" width="80" />
      <el-table-column label="状态" prop="ruleStatus" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.ruleStatus === 'ENABLED' ? 'success' : 'info'" size="mini">
            {{ scope.row.ruleStatus === 'ENABLED' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
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
  name: 'JsgzglPage',
  data() {
    return {
      loading: false,
      total: 0,
      ruleList: [],
      queryParams: { pageNum: 1, pageSize: 10, ruleName: '', ruleStatus: '' }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = false
      this.ruleList = []
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleAdd() { this.$message.info('新增功能开发中') },
    handleEdit(row) { this.$message.info('编辑功能开发中') },
    handleDelete(row) { this.$message.warning('删除功能开发中') }
  }
}
</script>

