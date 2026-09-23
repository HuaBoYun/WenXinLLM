<template>
  <div class="project-table">
    <!-- 查询条件 -->
    <div class="search-bar">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="项目编码">
          <el-input v-model="searchForm.code" placeholder="请输入项目编码" clearable></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="searchForm.name" placeholder="请输入项目名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="searchForm.type" placeholder="请选择项目类型" clearable>
            <el-option label="研发项目" value="研发项目"></el-option>
            <el-option label="市场项目" value="市场项目"></el-option>
            <el-option label="内部项目" value="内部项目"></el-option>
            <el-option label="客户项目" value="客户项目"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目经理">
          <el-input v-model="searchForm.manager" placeholder="请输入项目经理" clearable></el-input>
        </el-form-item>
        <el-form-item label="项目状态">
          <el-select v-model="searchForm.status" placeholder="请选择项目状态" clearable>
            <el-option label="规划中" value="planning"></el-option>
            <el-option label="进行中" value="in_progress"></el-option>
            <el-option label="已完成" value="completed"></el-option>
            <el-option label="已取消" value="cancelled"></el-option>
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
      <el-table-column prop="code" label="项目编码" width="120" fixed="left"></el-table-column>
      <el-table-column prop="name" label="项目名称" min-width="200" fixed="left"></el-table-column>
      <el-table-column prop="type" label="项目类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getProjectTypeTag(scope.row.type)" size="mini">
            {{ scope.row.type }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="manager" label="项目经理" width="120"></el-table-column>
      <el-table-column prop="startDate" label="开始日期" width="110" align="center"></el-table-column>
      <el-table-column prop="endDate" label="结束日期" width="110" align="center"></el-table-column>
      <el-table-column prop="budget" label="预算金额" width="120" align="right">
        <template slot-scope="scope">
          <span>¥{{ formatAmount(scope.row.budget) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="项目状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getProjectStatusTag(scope.row.status)" size="mini">
            {{ getProjectStatusText(scope.row.status) }}
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
  name: 'ProjectTable',
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
        type: '',
        manager: '',
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

      if (this.searchForm.type) {
        data = data.filter(item => item.type === this.searchForm.type)
      }

      if (this.searchForm.manager) {
        data = data.filter(item => item.manager && item.manager.includes(this.searchForm.manager))
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
        type: '',
        manager: '',
        status: ''
      }
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    getProjectTypeTag(type) {
      const tagMap = {
        '研发项目': 'primary',
        '市场项目': 'success',
        '内部项目': 'warning',
        '客户项目': 'info'
      }
      return tagMap[type] || 'info'
    },
    getProjectStatusTag(status) {
      const tagMap = {
        'planning': 'info',
        'in_progress': 'primary',
        'completed': 'success',
        'cancelled': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getProjectStatusText(status) {
      const textMap = {
        'planning': '规划中',
        'in_progress': '进行中',
        'completed': '已完成',
        'cancelled': '已取消'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.project-table {
  .search-bar {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
  }
}
</style>