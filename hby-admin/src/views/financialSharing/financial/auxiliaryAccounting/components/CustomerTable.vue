<template>
  <div class="customer-table">
    <!-- 查询条件 -->
    <div class="search-bar">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="客户编码">
          <el-input v-model="searchForm.code" placeholder="请输入客户编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.name" placeholder="请输入客户名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="searchForm.contact" placeholder="请输入联系人" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="active"></el-option>
            <el-option label="停用" value="inactive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格 -->
    <el-table
      v-loading="loading"
      :data="filteredData"
      stripe
      border
      style="width: 100%">
      <el-table-column prop="code" label="客户编码" width="120" fixed="left"></el-table-column>
      <el-table-column prop="name" label="客户名称" min-width="200" fixed="left"></el-table-column>
      <el-table-column prop="contact" label="联系人" width="120"></el-table-column>
      <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱地址" min-width="180"></el-table-column>
      <el-table-column prop="address" label="地址" min-width="200"></el-table-column>
      <el-table-column prop="creditLimit" label="信用额度" width="120" align="right">
        <template slot-scope="scope">
          <span>¥{{ formatAmount(scope.row.creditLimit) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'" size="mini">
            {{ scope.row.status === 'active' ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="$emit('edit', scope.row)">
            编辑
          </el-button>
          <el-button size="mini" type="text" style="color: #f56c6c;" @click="$emit('delete', scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'CustomerTable',
  props: {
    tableData: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      searchForm: {
        code: '',
        name: '',
        contact: '',
        status: ''
      }
    }
  },
  computed: {
    filteredData() {
      let data = [...this.tableData]

      if (this.searchForm.code) {
        data = data.filter(item => item.code.includes(this.searchForm.code))
      }

      if (this.searchForm.name) {
        data = data.filter(item => item.name.includes(this.searchForm.name))
      }

      if (this.searchForm.contact) {
        data = data.filter(item => item.contact && item.contact.includes(this.searchForm.contact))
      }

      if (this.searchForm.status) {
        data = data.filter(item => item.status === this.searchForm.status)
      }

      return data
    }
  },
  methods: {
    handleSearch() {
      // 查询逻辑已经在computed中实现
    },
    handleReset() {
      this.searchForm = {
        code: '',
        name: '',
        contact: '',
        status: ''
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.customer-table {
  .search-bar {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
  }
}
</style>