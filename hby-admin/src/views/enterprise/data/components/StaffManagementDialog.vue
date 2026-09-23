<template>
  <el-dialog
    title="人员管理"
    :visible.sync="dialogVisible"
    width="1200px"
    @close="handleClose"
  >
    <div class="toolbar">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.keyword"
            placeholder="搜索姓名/工号"
            clearable
            @keyup.enter.native="handleSearch"
          >
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.department" placeholder="选择部门" clearable>
            <el-option label="全部部门" value=""></el-option>
            <el-option label="技术部" value="tech"></el-option>
            <el-option label="市场部" value="market"></el-option>
            <el-option label="财务部" value="finance"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable>
            <el-option label="全部状态" value=""></el-option>
            <el-option label="在职" value="active"></el-option>
            <el-option label="离职" value="inactive"></el-option>
            <el-option label="休假" value="leave"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="addStaff">添加人员</el-button>
          <el-button @click="batchImport">批量导入</el-button>
          <el-button @click="exportStaff">导出</el-button>
        </el-col>
      </el-row>
    </div>
    
    <el-table
      :data="staffList"
      border
      v-loading="loading"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="avatar" label="头像" width="80">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar" size="small">
            {{ scope.row.name ? scope.row.name.charAt(0) : 'U' }}
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column prop="employeeCode" label="工号" width="120"></el-table-column>
      <el-table-column prop="department" label="部门" width="150"></el-table-column>
      <el-table-column prop="position" label="职位" width="150"></el-table-column>
      <el-table-column prop="level" label="职级" width="100"></el-table-column>
      <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip></el-table-column>
      <el-table-column prop="joinDate" label="入职时间" width="120"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="viewStaff(scope.row)">
            查看
          </el-button>
          <el-button type="text" size="small" @click="editStaff(scope.row)">
            编辑
          </el-button>
          <el-button type="text" size="small" @click="transferStaff(scope.row)">
            调动
          </el-button>
          <el-dropdown @command="handleCommand" trigger="click">
            <el-button type="text" size="small">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'resetPassword', row: scope.row}">
                重置密码
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'changeStatus', row: scope.row}">
                状态变更
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'viewHistory', row: scope.row}">
                查看履历
              </el-dropdown-item>
              <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pagination.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      style="margin-top: 20px; text-align: right;"
    ></el-pagination>
    
    <!-- 批量操作栏 -->
    <div class="batch-actions" v-if="selectedStaff.length > 0">
      <span>已选择 {{ selectedStaff.length }} 项</span>
      <el-button size="small" @click="batchTransfer">批量调动</el-button>
      <el-button size="small" @click="batchChangeStatus">批量状态变更</el-button>
      <el-button size="small" type="danger" @click="batchDelete">批量删除</el-button>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'StaffManagementDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    orgData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      searchForm: {
        keyword: '',
        department: '',
        status: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      staffList: [],
      selectedStaff: []
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadStaffData()
      }
    }
  },
  methods: {
    loadStaffData() {
      this.loading = true
      request({
        url: '/monitor/v1/enterprise/data/entry/staff/list',
        method: 'post',
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        data: {
          orgId: this.orgData.id,
          keyword: this.searchKeyword,
          pageNum: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
      }).then((res) => {
        this.staffList = (res.data && res.data.list) || []
        this.pagination.total = (res.data && res.data.total) || 0
      }).catch((error) => {
        this.$message.error(error.message || '加载人员数据失败')
      }).finally(() => {
        this.loading = false
      })
    },
    getStatusType(status) {
      const statusMap = {
        'active': 'success',
        'inactive': 'info',
        'leave': 'warning',
        'suspended': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'active': '在职',
        'inactive': '离职',
        'leave': '休假',
        'suspended': '停职'
      }
      return textMap[status] || status
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadStaffData()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadStaffData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadStaffData()
    },
    handleSelectionChange(selection) {
      this.selectedStaff = selection
    },
    addStaff() {
      this.$emit('add-staff', this.orgData)
    },
    viewStaff(row) {
      this.$emit('view-staff', row)
    },
    editStaff(row) {
      this.$emit('edit-staff', row)
    },
    transferStaff(row) {
      this.$emit('transfer-staff', row)
    },
    batchImport() {
      this.$emit('batch-import')
    },
    exportStaff() {
      this.$message.success('人员数据导出中...')
    },
    batchTransfer() {
      this.$emit('batch-transfer', this.selectedStaff)
    },
    batchChangeStatus() {
      this.$emit('batch-change-status', this.selectedStaff)
    },
    batchDelete() {
      this.$confirm('确认删除选中的人员吗？', '批量删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量删除成功')
        this.loadStaffData()
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'resetPassword':
          this.$message.success('密码重置成功')
          break
        case 'changeStatus':
          this.$emit('change-status', row)
          break
        case 'viewHistory':
          this.$emit('view-history', row)
          break
        case 'delete':
          this.$confirm('确认删除此人员吗？', '删除确认', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$message.success('删除成功')
            this.loadStaffData()
          })
          break
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.toolbar {
  margin-bottom: 20px;
}

.batch-actions {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  padding: 10px 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.batch-actions span {
  margin-right: 15px;
  color: #606266;
}
</style>
