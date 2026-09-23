<template>
  <div class="employee-table">
    <!-- 查询条件 -->
    <div class="search-bar">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="员工编码">
          <el-input v-model="searchForm.code" placeholder="请输入员工编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="员工姓名">
          <el-input v-model="searchForm.name" placeholder="请输入员工姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="searchForm.department" placeholder="请输入所属部门" clearable></el-input>
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="searchForm.position" placeholder="请选择职位" clearable>
            <el-option label="经理" value="经理"></el-option>
            <el-option label="主管" value="主管"></el-option>
            <el-option label="专员" value="专员"></el-option>
            <el-option label="助理" value="助理"></el-option>
          </el-select>
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
      <el-table-column prop="code" label="员工编码" width="120" fixed="left"></el-table-column>
      <el-table-column prop="name" label="员工姓名" width="120" fixed="left"></el-table-column>
      <el-table-column prop="department" label="所属部门" width="120"></el-table-column>
      <el-table-column prop="position" label="职位" width="100"></el-table-column>
      <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱地址" min-width="180"></el-table-column>
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
  name: 'EmployeeTable',
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
        department: '',
        position: '',
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

      if (this.searchForm.department) {
        data = data.filter(item => item.department && item.department.includes(this.searchForm.department))
      }

      if (this.searchForm.position) {
        data = data.filter(item => item.position === this.searchForm.position)
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
        department: '',
        position: '',
        status: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.employee-table {
  .search-bar {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
  }
}
</style>