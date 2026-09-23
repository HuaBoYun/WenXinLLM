<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="映射编码">
        <el-input v-model="queryForm.mappingCode" placeholder="请输入映射编码" clearable />
      </el-form-item>
      <el-form-item label="映射名称">
        <el-input v-model="queryForm.mappingName" placeholder="请输入映射名称" clearable />
      </el-form-item>
      <el-form-item label="来源系统">
        <el-select v-model="queryForm.sourceSystem" placeholder="请选择来源系统" clearable>
          <el-option label="凭证系统" value="VOUCHER" />
          <el-option label="付款系统" value="PAYMENT" />
          <el-option label="合同系统" value="CONTRACT" />
          <el-option label="采购系统" value="PURCHASE" />
        </el-select>
      </el-form-item>
      <el-form-item label="映射类型">
        <el-select v-model="queryForm.mappingType" placeholder="请选择映射类型" clearable>
          <el-option label="科目" value="SUBJECT" />
          <el-option label="组织" value="ORG" />
          <el-option label="项目" value="PROJECT" />
          <el-option label="维度" value="DIMENSION" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="映射编码" prop="mappingCode" width="150" />
      <!-- 映射名称用 min-width 而不是 width, 让这一列吸收表格剩余空间, 避免和 fixed=right 的操作列之间出现空白带 -->
      <el-table-column label="映射名称" prop="mappingName" min-width="200" show-overflow-tooltip />
      <el-table-column label="来源系统" prop="sourceSystem" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.sourceSystem === 'VOUCHER'" type="primary" size="small">凭证系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'PAYMENT'" type="success" size="small">付款系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'CONTRACT'" type="warning" size="small">合同系统</el-tag>
          <el-tag v-else-if="scope.row.sourceSystem === 'PURCHASE'" type="info" size="small">采购系统</el-tag>
          <span v-else style="color:#909399">{{ scope.row.sourceSystem || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="映射类型" prop="mappingType" width="100">
        <template slot-scope="scope">
          {{ mappingTypeText(scope.row.mappingType) }}
        </template>
      </el-table-column>
      <el-table-column label="是否必填" prop="isMandatory" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isMandatory === 'Y'" type="danger" size="small">是</el-tag>
          <el-tag v-else type="info" size="small">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" prop="isEnabled" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isEnabled === 'Y'" type="success" size="small">启用</el-tag>
          <el-tag v-else type="info" size="small">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="排序号" prop="sortNo" width="80" align="center" />
      <el-table-column label="创建时间" prop="createTime" width="170" align="center" />
      <el-table-column label="操作" fixed="right" width="130" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form ref="dataForm" :model="dataForm" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="映射编码" prop="mappingCode">
              <el-input v-model="dataForm.mappingCode" placeholder="请输入映射编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="映射名称" prop="mappingName">
              <el-input v-model="dataForm.mappingName" placeholder="请输入映射名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="来源系统" prop="sourceSystem">
              <el-select v-model="dataForm.sourceSystem" placeholder="请选择来源系统">
                <el-option label="凭证系统" value="VOUCHER" />
                <el-option label="付款系统" value="PAYMENT" />
                <el-option label="合同系统" value="CONTRACT" />
                <el-option label="采购系统" value="PURCHASE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="映射类型" prop="mappingType">
              <el-select v-model="dataForm.mappingType" placeholder="请选择映射类型">
                <el-option label="科目" value="SUBJECT" />
                <el-option label="组织" value="ORG" />
                <el-option label="项目" value="PROJECT" />
                <el-option label="维度" value="DIMENSION" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="映射规则" prop="mappingRules">
              <el-input
                v-model="dataForm.mappingRules"
                type="textarea"
                :rows="4"
                placeholder="请输入映射规则(JSON格式)"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否必填">
              <el-radio-group v-model="dataForm.isMandatory">
                <el-radio label="Y">是</el-radio>
                <el-radio label="N">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-radio-group v-model="dataForm.isEnabled">
                <el-radio label="Y">启用</el-radio>
                <el-radio label="N">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryMappingConfigPage, addMappingConfig, modifyMappingConfig, removeMappingConfig } from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'

export default {
  name: 'MappingConfig',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        mappingCode: '',
        mappingName: '',
        sourceSystem: '',
        mappingType: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      dataForm: {},
      rules: {
        mappingCode: [{ required: true, message: '请输入映射编码', trigger: 'blur' }],
        mappingName: [{ required: true, message: '请输入映射名称', trigger: 'blur' }],
        sourceSystem: [{ required: true, message: '请选择来源系统', trigger: 'change' }],
        mappingType: [{ required: true, message: '请选择映射类型', trigger: 'change' }],
        mappingRules: [{ required: true, message: '请输入映射规则', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 映射类型文本映射: SUBJECT=科目 / ORG=组织 / PROJECT=项目 / DIMENSION=维度 */
    mappingTypeText(v) {
      const map = { SUBJECT: '科目', ORG: '组织', PROJECT: '项目', DIMENSION: '维度' }
      return map[v] || v || '-'
    },
    getList() {
      this.loading = true
      queryMappingConfigPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records
          this.total = response.data.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    handleReset() {
      this.queryForm = {
        mappingCode: '',
        mappingName: '',
        sourceSystem: '',
        mappingType: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    handleAdd() {
      this.dialogTitle = '新增映射配置'
      this.dataForm = { isEnabled: 'Y', isMandatory: 'Y', sortNo: 0 }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑映射配置'
      this.dataForm = { ...row }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          const api = this.dataForm.mappingId ? modifyMappingConfig : addMappingConfig
          api(this.dataForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg)
              this.dialogVisible = false
              this.getList()
            }
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该映射配置吗?', '提示', {
        type: 'warning'
      }).then(() => {
        removeMappingConfig(row.mappingId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    }
  }
}
</script>

