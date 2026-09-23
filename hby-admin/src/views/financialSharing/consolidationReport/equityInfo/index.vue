<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px">
      <el-form-item label="合并模型">
        <el-select v-model="queryForm.modelId" placeholder="请选择合并模型" clearable @change="handleQuery">
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="母公司名称">
        <el-input v-model="queryForm.parentOrgName" placeholder="请输入母公司名称" clearable />
      </el-form-item>
      <el-form-item label="子公司名称">
        <el-input v-model="queryForm.subsidiaryOrgName" placeholder="请输入子公司名称" clearable />
      </el-form-item>
      <el-form-item label="股权类型">
        <el-select v-model="queryForm.equityType" placeholder="请选择股权类型" clearable>
          <el-option label="直接持股" value="DIRECT" />
          <el-option label="间接持股" value="INDIRECT" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.isActive" placeholder="请选择状态" clearable>
          <el-option label="启用" value="Y" />
          <el-option label="停用" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="equityList" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="模型名称" prop="modelName" width="180" />
      <el-table-column label="母公司ID" prop="parentOrgId" width="150" />
      <el-table-column label="母公司名称" prop="parentOrgName" width="180" />
      <el-table-column label="子公司ID" prop="subsidiaryOrgId" width="150" />
      <el-table-column label="子公司名称" prop="subsidiaryOrgName" width="180" />
      <el-table-column label="股权类型" prop="equityType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.equityType === 'DIRECT'" type="success">直接持股</el-tag>
          <el-tag v-else-if="scope.row.equityType === 'INDIRECT'" type="warning">间接持股</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="持股比例(%)" prop="holdingRatio" width="120" align="right">
        <template slot-scope="scope">
          {{ scope.row.holdingRatio ? scope.row.holdingRatio + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="投资日期" prop="investmentDate" width="120" />
      <el-table-column label="生效起始期间" prop="effectiveStartPeriod" width="120" />
      <el-table-column label="生效终止期间" prop="effectiveEndPeriod" width="120" />
      <el-table-column label="状态" prop="isActive" width="80" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isActive === 'Y'" type="success">启用</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            v-if="scope.row.isActive === 'Y'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleUpdateStatus(scope.row, 'N')"
          >停用</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleUpdateStatus(scope.row, 'Y')"
          >启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNum"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 表单对话框 -->
    <equity-info-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :equity-id="currentEquityId"
      :model-id="queryForm.modelId"
      :form-type="formType"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import { getEquityList, deleteEquity, updateEquityStatus } from '@/api/financialSharing/consolidationReport/equityInfo'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'
import EquityInfoForm from './components/EquityInfoForm.vue'
import Pagination from '@/components/Pagination'

export default {
  name: 'EquityInfo',
  components: {
    EquityInfoForm,
    Pagination
  },
  data() {
    return {
      // 查询参数
      queryForm: {
        modelId: '',
        parentOrgName: '',
        subsidiaryOrgName: '',
        equityType: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      },
      // 加载状态
      loading: false,
      // 数据列表
      equityList: [],
      // 总记录数
      total: 0,
      // 表单对话框
      formVisible: false,
      // 当前股权信息ID
      currentEquityId: null,
      // 表单类型：add/edit/view
      formType: 'add',
      // 模型选项
      modelOptions: []
    }
  },
  created() {
    this.loadModelOptions()
    this.getList()
  },
  methods: {
    /** 加载模型选项 */
    loadModelOptions() {
      getModelList({ status: 'ACTIVE', pageNum: 1, pageSize: 1000 }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          this.modelOptions = res.data.list.map(item => ({
            value: item.modelId,
            label: item.modelName
          }))
        }
      })
    },
    /** 查询列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getEquityList(this.queryForm).then(res => {
        this.loading = false
        if (res.code === 1 || res.code === 200) {
          this.equityList = res.data.list || res.data.records || []
          this.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
          this.equityList = []
          this.total = 0
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.equityList = []
        this.total = 0
      })
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNum = 1
      this.getList()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryForm = {
        modelId: '',
        parentOrgName: '',
        subsidiaryOrgName: '',
        equityType: '',
        isActive: '',
        pageNum: 1,
        pageSize: 10
      }
      this.getList()
    },
    /** 新增按钮 */
    handleAdd() {
      if (!this.queryForm.modelId) {
        this.$message.warning('请先选择合并模型')
        return
      }
      this.currentEquityId = null
      this.formType = 'add'
      this.formVisible = true
    },
    /** 查看按钮 */
    handleView(row) {
      this.currentEquityId = row.equityId
      this.formType = 'view'
      this.formVisible = true
    },
    /** 编辑按钮 */
    handleEdit(row) {
      this.currentEquityId = row.equityId
      this.formType = 'edit'
      this.formVisible = true
    },
    /** 删除按钮 */
    handleDelete(row) {
      this.$confirm('确认删除该股权信息吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteEquity({ equityId: row.equityId }).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    /** 更新状态 */
    handleUpdateStatus(row, isActive) {
      const statusText = isActive === 'Y' ? '启用' : '停用'
      this.$confirm(`确认${statusText}该股权信息吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateEquityStatus({ equityId: row.equityId, isActive }).then(res => {
          if (res.code === 200) {
            this.$message.success(`${statusText}成功`)
            this.getList()
          } else {
            this.$message.error(res.msg || `${statusText}失败`)
          }
        })
      }).catch(() => {})
    },
    /** 表单提交成功 */
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


