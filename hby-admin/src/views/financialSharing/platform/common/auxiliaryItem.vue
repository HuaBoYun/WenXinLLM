<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>辅助核算项管理</span>
      </div>
      
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="demo-form-inline">
        <el-form-item label="核算项编码" prop="auxiliaryCode">
          <el-input
            v-model="queryForm.auxiliaryCode"
            placeholder="请输入核算项编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item label="核算项名称" prop="auxiliaryName">
          <el-input
            v-model="queryForm.auxiliaryName"
            placeholder="请输入核算项名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item label="核算类型" prop="auxiliaryType">
          <el-select
            v-model="queryForm.auxiliaryType"
            placeholder="请选择核算类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(name, value) in AUXILIARY_TYPE_NAME"
              :key="value"
              :label="name"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="启用状态" prop="isEnabled">
          <el-select
            v-model="queryForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="(name, value) in ENABLED_STATUS_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="box-card" style="margin-top: 20px">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
          >导出</el-button>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="auxiliaryItemList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        border
        stripe
        style="width: 100%"
        :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
      >
        <el-table-column type="selection" width="55" align="center" fixed />
        <el-table-column label="核算项编码" prop="auxiliaryCode" min-width="120" show-overflow-tooltip />
        <el-table-column label="核算项名称" prop="auxiliaryName" min-width="150" show-overflow-tooltip />
        <el-table-column label="核算类型" prop="auxiliaryTypeName" min-width="100" align="center" />
        <el-table-column label="上级核算项" prop="parentName" min-width="150" show-overflow-tooltip />
        <el-table-column label="是否末级" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isLeaf === 1 ? 'success' : 'info'" size="mini">
              {{ IS_LEAF_NAME[scope.row.isLeaf] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序号" prop="sortOrder" width="80" align="center" />
        <el-table-column label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-show="total > 0"
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 添加或修改辅助核算项对话框 -->
    <auxiliary-item-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import {
  getAuxiliaryItemPage,
  saveOrUpdateAuxiliaryItem,
  deleteAuxiliaryItem,
  batchDeleteAuxiliaryItems,
  updateAuxiliaryItemStatus,
  batchUpdateAuxiliaryItemStatus
} from '@/api/financialSharing/system'
import {
  AUXILIARY_TYPE_NAME,
  IS_LEAF_NAME,
  ENABLED_STATUS_NAME,
  DEFAULT_PAGE_CONFIG,
  DEFAULT_TENANT_CONFIG
} from '../consts'
import AuxiliaryItemForm from './components/AuxiliaryItemForm'
// import Pagination from '@/components/Pagination' // 使用 Element UI 的 el-pagination

export default {
  name: 'AuxiliaryItem',
  components: {
    AuxiliaryItemForm
    // Pagination // 使用 Element UI 的 el-pagination，不需要注册组件
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 辅助核算项表格数据
      auxiliaryItemList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      formVisible: false,
      // 表单类型 add/edit/view
      formType: 'add',
      // 表单数据
      formData: {},
      // 查询参数
      queryForm: {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        auxiliaryCode: null,
        auxiliaryName: null,
        auxiliaryType: null,
        isEnabled: null
      },
      // 常量
      AUXILIARY_TYPE_NAME,
      IS_LEAF_NAME,
      ENABLED_STATUS_NAME
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询辅助核算项列表 */
    getList() {
      this.loading = true
      getAuxiliaryItemPage(this.queryForm).then(response => {
        if (response.code === 1) {
          // 修复数据映射问题：从records获取实际数据而不是tlist中的JSON引用
          let list = response.data.records || response.data.tlist
          // 修复辅助核算类型显示问题：将英文代码转换为中文
          this.auxiliaryItemList = list.map(item => {
            if (!item.auxiliaryTypeName || /^[A-Z_]+$/.test(item.auxiliaryTypeName)) {
              item.auxiliaryTypeName = this.getAuxiliaryTypeName(item.auxiliaryType)
            }
            return item
          })
          this.total = response.data.totalRecord
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    /** 获取辅助核算类型中文名称 */
    getAuxiliaryTypeName(type) {
      const typeName = {
        'DEPT': '部门',
        'DEPARTMENT': '部门',
        'PERSON': '人员',
        'EMPLOYEE': '人员',
        'PROJECT': '项目',
        'CUSTOMER': '客户',
        'SUPPLIER': '供应商',
        'PRODUCT': '产品',
        'AREA': '地区',
        'OTHER': '其他'
      }
      return typeName[type] || type || '未知'
    },
    
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    
    /** 重置按钮操作 */
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        auxiliaryCode: null,
        auxiliaryName: null,
        auxiliaryType: null,
        isEnabled: null
      }
      this.handleQuery()
    },
    
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {}
      this.formType = 'add'
      this.formVisible = true
    },
    
    /** 修改按钮操作 */
    handleUpdate(row) {
      const auxiliaryId = row ? row.auxiliaryId : this.ids[0]
      if (!auxiliaryId) {
        this.$message.warning('请选择要修改的数据')
        return
      }
      
      this.formData = row || this.auxiliaryItemList.find(item => item.auxiliaryId === auxiliaryId)
      this.formType = 'edit'
      this.formVisible = true
    },
    
    /** 查看按钮操作 */
    handleView(row) {
      this.formData = row
      this.formType = 'view'
      this.formVisible = true
    },
    
    /** 删除按钮操作 */
    handleDelete(row) {
      const auxiliaryIds = row ? [row.auxiliaryId] : this.ids
      const auxiliaryNames = row ? [row.auxiliaryName] : this.auxiliaryItemList.filter(item => this.ids.includes(item.auxiliaryId)).map(item => item.auxiliaryName)

      this.$confirm(`是否确认删除辅助核算项"${auxiliaryNames.join('、')}"？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const deletePromise = auxiliaryIds.length === 1
          ? deleteAuxiliaryItem(auxiliaryIds[0])
          : batchDeleteAuxiliaryItems(auxiliaryIds)

        deletePromise.then(response => {
          if (response.code === 1) {
            this.getList()
            this.$message.success('删除成功')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },
    
    /** 导出按钮操作 */
    handleExport() {
      const data = this.tableData || this.list || []
      if (!data.length) { this.$message.warning('暂无数据可导出'); return }
      const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '数据导出.json'
      link.click()
      window.URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },
    
    /** 状态修改 */
    handleStatusChange(row) {
      const text = row.isEnabled === 1 ? '启用' : '禁用'
      this.$confirm(`确认要${text}"${row.auxiliaryName}"吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateAuxiliaryItemStatus(row.auxiliaryId, row.isEnabled).then(response => {
          if (response.code === 1) {
            this.$message.success(`${text}成功`)
          } else {
            this.$message.error(response.msg || `${text}失败`)
            // 恢复原状态
            row.isEnabled = row.isEnabled === 1 ? 0 : 1
          }
        }).catch(() => {
          // 恢复原状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
        })
      }).catch(() => {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
      })
    },
    
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.auxiliaryId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    
    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table && this.$refs.table.toggleRowSelection(row)
    },
    
    /** 表单成功回调 */
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    },

    /** 改变每一页请求数量 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },

    /** 跳转页数 */
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getList()
    }
  }
}
</script>

<style scoped>
.demo-form-inline .el-form-item {
  margin-bottom: 10px;
}
</style>
