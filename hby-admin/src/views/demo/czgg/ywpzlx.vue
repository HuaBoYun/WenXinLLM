<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.typeCode"
        placeholder="类型编码"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.typeName"
        placeholder="类型名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.isEnabled"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        查询
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >
        新增
      </el-button>
      <el-button
        v-show="multipleSelection.length > 0"
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchDelete"
      >
        批量删除
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
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型编码" prop="typeCode" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.typeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型名称" prop="typeName" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.typeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上级类型" prop="parentTypeName" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.parentTypeName || '无' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="层级" prop="level" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.level }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sortOrder" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.sortOrder }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="isEnabled" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" align="center" min-width="200">
        <template slot-scope="{row}">
          <span>{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.isEnabled === 1"
            size="mini"
            type="warning"
            @click="handleModifyStatus(row, 0)"
          >
            禁用
          </el-button>
          <el-button
            v-else
            size="mini"
            type="success"
            @click="handleModifyStatus(row, 1)"
          >
            启用
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

    <!-- 编辑对话框 -->
    <BusinessTypeEditDialog
      ref="editDialog"
      @refresh="getList"
    />
  </div>
</template>

<script>
import { getBusinessTypeList, deleteBusinessType, batchDeleteBusinessType, updateBusinessTypeStatus } from '@/api/globalTreasurer/czgg'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import BusinessTypeEditDialog from './components/BusinessTypeEditDialog'

export default {
  name: 'BusinessTypeManagement',
  components: { Pagination, BusinessTypeEditDialog },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        pageNumber: 1,
        pageSize: 20,
        typeCode: '',
        typeName: '',
        isEnabled: null
      },
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getBusinessTypeList(this.listQuery).then(response => {
        if (response.success) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.getList()
    },
    handleCreate() {
      this.$refs.editDialog.show()
    },
    handleUpdate(row) {
      this.$refs.editDialog.show(row)
    },
    handleDelete(row) {
      this.$confirm('确定要删除该业务品种类型吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBusinessType(row.typeId).then(response => {
          if (response.success) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确定要删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.typeId)
        batchDeleteBusinessType(ids).then(response => {
          if (response.success) {
            this.$message.success('批量删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        })
      })
    },
    handleModifyStatus(row, status) {
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}该业务品种类型吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateBusinessTypeStatus({
          id: row.typeId,
          isEnabled: status,
          updateUser: this.$store.getters.userId
        }).then(response => {
          if (response.success) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(response.message || `${statusText}失败`)
          }
        })
      })
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    }
  }
}
</script>

<style scoped>
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
