<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <div class="query-form">
      <el-form :model="queryForm" :inline="true" label-width="100px">
        <el-form-item label="维度编码">
          <el-input v-model="queryForm.dimensionCode" placeholder="请输入维度编码" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="维度名称">
          <el-input v-model="queryForm.dimensionName" placeholder="请输入维度名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="维度类型">
          <el-select v-model="queryForm.dimensionType" placeholder="请选择维度类型" clearable style="width: 200px">
            <el-option label="预置" value="PRESET" />
            <el-option label="自定义" value="CUSTOM" />
            <el-option label="档案引入" value="ARCHIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 200px">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div style="margin-bottom: 10px">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="dataList"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="维度编码" prop="dimensionCode" width="150" show-overflow-tooltip />
      <el-table-column label="维度名称" prop="dimensionName" width="150" show-overflow-tooltip />
      <el-table-column label="维度类型" prop="dimensionType" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.dimensionType === 'PRESET'" type="primary">预置</el-tag>
          <el-tag v-else-if="scope.row.dimensionType === 'CUSTOM'" type="success">自定义</el-tag>
          <el-tag v-else-if="scope.row.dimensionType === 'ARCHIVE'" type="info">档案引入</el-tag>
          <span v-else>{{ scope.row.dimensionType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="维度分类" prop="dimensionCategory" width="120" align="center" show-overflow-tooltip />
      <el-table-column label="是否层级" prop="isHierarchy" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isHierarchy === 'Y'" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最大层级" prop="maxLevel" width="100" align="center" />
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="ACTIVE"
            inactive-value="INACTIVE"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="queryForm.pageNumber"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="queryForm.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; text-align: right"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 新增/编辑对话框 -->
    <dimension-form
      v-if="dialogVisible"
      :visible.sync="dialogVisible"
      :dimension-id="currentDimensionId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getDimensionList, deleteDimension, batchDeleteDimension, updateDimensionStatus } from '@/api/groupControl/dimension'
import DimensionForm from './components/DimensionForm'

export default {
  name: 'Dimension',
  components: {
    DimensionForm
  },
  data() {
    return {
      // 加载状态
      loading: false,
      // 查询表单
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        dimensionCode: null,
        dimensionName: null,
        dimensionType: null,
        status: null
      },
      // 数据列表
      dataList: [],
      // 总记录数
      total: 0,
      // 选中的ID列表
      selectedIds: [],
      // 对话框显示
      dialogVisible: false,
      // 当前维度ID
      currentDimensionId: null
    }
  },
  created() {
    this.getList()
  },
  methods: {


    // 查询列表
    getList() {
      this.loading = true
      // 强制使用 Mock 数据（后端调试期间）
      console.warn('🔧 开发模式：使用 Mock 数据')
      this.dataList = [
        { dimensionId: 1, dimensionCode: 'DIM001', dimensionName: '公司维度', dimensionType: 'PRESET', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 5, status: 'ACTIVE', createTime: '2024-01-01 10:00:00' },
        { dimensionId: 2, dimensionCode: 'DIM002', dimensionName: '部门维度', dimensionType: 'PRESET', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 4, status: 'ACTIVE', createTime: '2024-01-02 10:00:00' },
        { dimensionId: 3, dimensionCode: 'DIM003', dimensionName: '期间维度', dimensionType: 'PRESET', dimensionCategory: 'PERIOD', enableHierarchy: false, maxLevel: 1, status: 'ACTIVE', createTime: '2024-01-03 10:00:00' },
        { dimensionId: 4, dimensionCode: 'DIM004', dimensionName: '版本维度', dimensionType: 'CUSTOM', dimensionCategory: 'VERSION', enableHierarchy: false, maxLevel: 1, status: 'ACTIVE', createTime: '2024-01-04 10:00:00' },
        { dimensionId: 5, dimensionCode: 'DIM005', dimensionName: '币种维度', dimensionType: 'PRESET', dimensionCategory: 'CURRENCY', enableHierarchy: false, maxLevel: 1, status: 'ACTIVE', createTime: '2024-01-05 10:00:00' },
        { dimensionId: 6, dimensionCode: 'DIM006', dimensionName: '产品维度', dimensionType: 'CUSTOM', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 3, status: 'ACTIVE', createTime: '2024-01-06 10:00:00' },
        { dimensionId: 7, dimensionCode: 'DIM007', dimensionName: '客户维度', dimensionType: 'ARCHIVE', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 3, status: 'ACTIVE', createTime: '2024-01-07 10:00:00' },
        { dimensionId: 8, dimensionCode: 'DIM008', dimensionName: '供应商维度', dimensionType: 'ARCHIVE', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 3, status: 'INACTIVE', createTime: '2024-01-08 10:00:00' },
        { dimensionId: 9, dimensionCode: 'DIM009', dimensionName: '项目维度', dimensionType: 'CUSTOM', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 4, status: 'ACTIVE', createTime: '2024-01-09 10:00:00' },
        { dimensionId: 10, dimensionCode: 'DIM010', dimensionName: '成本中心维度', dimensionType: 'CUSTOM', dimensionCategory: 'SUBJECT', enableHierarchy: true, maxLevel: 4, status: 'ACTIVE', createTime: '2024-01-10 10:00:00' }
      ]
      this.total = 10
      this.loading = false

      // 如果需要调用后端接口，取消下面的注释
      /*
      getDimensionList(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      */
    },

    // 查询按钮
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置按钮
    handleReset() {
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        dimensionCode: null,
        dimensionName: null,
        dimensionType: null,
        status: null
      }
      this.getList()
    },

    // 新增按钮
    handleAdd() {
      this.currentDimensionId = null
      this.dialogVisible = true
    },

    // 编辑按钮
    handleEdit(row) {
      this.currentDimensionId = row.dimensionId
      this.dialogVisible = true
    },

    // 删除按钮
    handleDelete(row) {
      this.$confirm('确认删除该维度吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDimension({ dimensionId: row.dimensionId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的维度吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteDimension(this.selectedIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 状态切换
    handleStatusChange(row) {
      const statusText = row.status === 'ACTIVE' ? '启用' : '停用'
      this.$confirm(`确认${statusText}该维度吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateDimensionStatus({
          dimensionId: row.dimensionId,
          status: row.status
        }).then(response => {
          if (response.code === 1) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            // 恢复原状态
            row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
          }
        }).catch(() => {
          // 恢复原状态
          row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
        })
      }).catch(() => {
        // 取消操作，恢复原状态
        row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      })
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.dimensionId)
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getList()
    },

    // 操作成功回调
    handleSuccess() {
      this.dialogVisible = false
      this.getList()
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}
</style>
