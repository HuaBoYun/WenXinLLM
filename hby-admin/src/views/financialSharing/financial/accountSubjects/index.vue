<template>
  <div class="account-subjects-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-order"></i>
          会计科目管理
        </h1>
        <p class="page-description">维护会计科目体系，包括科目编码、名称、属性等基础信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增科目
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出科目
        </el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="handleImport">
          导入科目
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" label-width="80px">
        <el-form-item label="科目编码">
          <el-input
            v-model="searchForm.subjectCode"
            placeholder="请输入科目编码"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="科目名称">
          <el-input
            v-model="searchForm.subjectName"
            placeholder="请输入科目名称"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="科目类别">
          <el-select v-model="searchForm.subjectCategory" placeholder="请选择科目类别" clearable>
            <el-option label="资产类" value="assets"></el-option>
            <el-option label="负债类" value="liabilities"></el-option>
            <el-option label="所有者权益类" value="equity"></el-option>
            <el-option label="成本类" value="cost"></el-option>
            <el-option label="损益类" value="profit_loss"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="科目级次">
          <el-select v-model="searchForm.subjectLevel" placeholder="请选择科目级次" clearable>
            <el-option label="一级科目" value="1"></el-option>
            <el-option label="二级科目" value="2"></el-option>
            <el-option label="三级科目" value="3"></el-option>
            <el-option label="四级科目" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="科目状态">
          <el-select v-model="searchForm.status" placeholder="请选择科目状态" clearable>
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

    <!-- 科目表格 -->
    <div class="subjects-table">
      <el-table
        v-loading="loading"
        :data="subjectsList"
        stripe
        border
        style="width: 100%"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        row-key="id"
        default-expand-all>
        <el-table-column prop="subjectCode" label="科目编码" width="120" fixed="left"></el-table-column>
        <el-table-column prop="subjectName" label="科目名称" min-width="200" fixed="left"></el-table-column>
        <el-table-column prop="subjectCategory" label="科目类别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSubjectCategoryTag(scope.row.subjectCategory)" size="mini">
              {{ getSubjectCategoryName(scope.row.subjectCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="subjectLevel" label="级次" width="80" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.subjectLevel }}级</span>
          </template>
        </el-table-column>
        <el-table-column prop="balanceDirection" label="余额方向" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.balanceDirection === 'debit' ? 'primary' : 'success'" size="mini">
              {{ scope.row.balanceDirection === 'debit' ? '借方' : '贷方' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auxiliaryAccounting" label="辅助核算" width="200">
          <template slot-scope="scope">
            <el-tag v-for="item in scope.row.auxiliaryTypes" :key="item" size="mini" style="margin-right: 5px;">
              {{ getAuxiliaryTypeName(item) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isCashEquivalent" label="现金等价物" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isCashEquivalent ? 'success' : 'info'" size="mini">
              {{ scope.row.isCashEquivalent ? '是' : '否' }}
            </el-tag>
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
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleAddChild(scope.row)" v-if="scope.row.subjectLevel < 4">
              添加下级
            </el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>

    <!-- 新增/编辑科目对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false">
      <el-form :model="subjectForm" :rules="subjectRules" ref="subjectForm" label-width="100px">
        <el-form-item label="上级科目" prop="parentId" v-if="!isEdit">
          <el-cascader
            v-model="subjectForm.parentId"
            :options="subjectOptions"
            :props="{
              label: 'subjectName',
              value: 'id',
              children: 'children',
              checkStrictly: true,
              emitPath: false
            }"
            placeholder="请选择上级科目"
            clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input v-model="subjectForm.subjectCode" placeholder="请输入科目编码" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="科目名称" prop="subjectName">
          <el-input v-model="subjectForm.subjectName" placeholder="请输入科目名称"></el-input>
        </el-form-item>
        <el-form-item label="科目类别" prop="subjectCategory">
          <el-select v-model="subjectForm.subjectCategory" placeholder="请选择科目类别">
            <el-option label="资产类" value="assets"></el-option>
            <el-option label="负债类" value="liabilities"></el-option>
            <el-option label="所有者权益类" value="equity"></el-option>
            <el-option label="成本类" value="cost"></el-option>
            <el-option label="损益类" value="profit_loss"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向" prop="balanceDirection">
          <el-radio-group v-model="subjectForm.balanceDirection">
            <el-radio label="debit">借方</el-radio>
            <el-radio label="credit">贷方</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="辅助核算" prop="auxiliaryTypes">
          <el-checkbox-group v-model="subjectForm.auxiliaryTypes">
            <el-checkbox label="department">部门核算</el-checkbox>
            <el-checkbox label="employee">职员核算</el-checkbox>
            <el-checkbox label="customer">客户核算</el-checkbox>
            <el-checkbox label="supplier">供应商核算</el-checkbox>
            <el-checkbox label="project">项目核算</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="现金等价物" prop="isCashEquivalent">
          <el-switch v-model="subjectForm.isCashEquivalent"></el-switch>
        </el-form-item>
        <el-form-item label="科目状态" prop="status">
          <el-radio-group v-model="subjectForm.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="inactive">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="subjectForm.remark" placeholder="请输入备注信息" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog
      title="导入科目"
      :visible.sync="importDialogVisible"
      width="500px"
      :close-on-click-modal="false">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :on-change="handleFileChange"
        :limit="1"
        accept=".xlsx,.xls">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getAccountSubjectPage as getAccountSubjectsList,
  saveOrUpdateAccountSubject,
  deleteAccountSubject,
  exportAccountSubjects,
  importAccountSubjects
} from '@/api/financialSharing/common'

export default {
  name: 'AccountSubjectsIndex',
  data() {
    return {
      loading: false,
      subjectsList: [],
      subjectOptions: [],
      dialogVisible: false,
      importDialogVisible: false,
      isEdit: false,
      currentSubject: null,
      searchForm: {
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        subjectLevel: '',
        status: ''
      },
      subjectForm: {
        id: '',
        parentId: '',
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        balanceDirection: 'debit',
        auxiliaryTypes: [],
        isCashEquivalent: false,
        status: 'active',
        remark: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      subjectRules: {
        subjectCode: [
          { required: true, message: '请输入科目编码', trigger: 'blur' },
          { pattern: /^\d+$/, message: '科目编码只能为数字', trigger: 'blur' }
        ],
        subjectName: [
          { required: true, message: '请输入科目名称', trigger: 'blur' }
        ],
        subjectCategory: [
          { required: true, message: '请选择科目类别', trigger: 'change' }
        ],
        balanceDirection: [
          { required: true, message: '请选择余额方向', trigger: 'change' }
        ]
      },
      importFile: null
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑科目' : '新增科目'
    }
  },
  mounted() {
    this.loadSubjectsList()
  },
  methods: {
    async loadSubjectsList() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }

        const response = await getAccountSubjectsList(params)
        if (response.code === 200) {
          this.subjectsList = this.buildTree(response.data.records || [])
          this.pagination.total = response.data.total || 0

          // 构建科目选项树（用于选择上级科目）
          this.subjectOptions = this.buildSubjectOptions(this.subjectsList)
        }
      } catch (error) {
        this.$message.error('加载科目列表失败：' + error.message)
        // 接口失败时降级为空状态
        this.subjectsList = this.generateMockData()
        this.subjectOptions = this.buildSubjectOptions(this.subjectsList)
        this.pagination.total = this.subjectsList.length
      } finally {
        this.loading = false
      }
    },
    buildTree(data) {
      // 将平铺数据转换为树形结构
      const map = {}
      data.forEach(item => {
        map[item.id] = { ...item, children: [] }
      })

      const tree = []
      data.forEach(item => {
        if (item.parentId) {
          if (map[item.parentId]) {
            map[item.parentId].children.push(map[item.id])
          }
        } else {
          tree.push(map[item.id])
        }
      })

      return tree
    },
    buildSubjectOptions(tree) {
      const options = []
      tree.forEach(item => {
        const option = {
          id: item.id,
          subjectName: item.subjectName,
          children: []
        }
        if (item.children && item.children.length > 0) {
          option.children = this.buildSubjectOptions(item.children)
        }
        options.push(option)
      })
      return options
    },
    generateMockData() {
      // 数据加载失败时的空状态降级（不再使用模拟数据）
      return []
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadSubjectsList()
    },
    handleReset() {
      this.searchForm = {
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        subjectLevel: '',
        status: ''
      }
      this.handleSearch()
    },
    handleAdd() {
      this.isEdit = false
      this.currentSubject = null
      this.subjectForm = {
        id: '',
        parentId: '',
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        balanceDirection: 'debit',
        auxiliaryTypes: [],
        isCashEquivalent: false,
        status: 'active',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleAddChild(row) {
      this.isEdit = false
      this.currentSubject = null
      this.subjectForm = {
        id: '',
        parentId: row.id,
        subjectCode: '',
        subjectName: '',
        subjectCategory: row.subjectCategory,
        balanceDirection: row.balanceDirection,
        auxiliaryTypes: [],
        isCashEquivalent: false,
        status: 'active',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.currentSubject = row
      this.subjectForm = {
        id: row.id,
        parentId: row.parentId,
        subjectCode: row.subjectCode,
        subjectName: row.subjectName,
        subjectCategory: row.subjectCategory,
        balanceDirection: row.balanceDirection,
        auxiliaryTypes: row.auxiliaryTypes || [],
        isCashEquivalent: row.isCashEquivalent || false,
        status: row.status,
        remark: row.remark || ''
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确定要删除科目"${row.subjectName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteAccountSubject(row.id)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.loadSubjectsList()
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    handleSubmit() {
      this.$refs.subjectForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdateAccountSubject(this.subjectForm)
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '修改成功' : '新增成功')
              this.dialogVisible = false
              this.loadSubjectsList()
            }
          } catch (error) {
            this.$message.error('保存失败：' + error.message)
          }
        }
      })
    },
    async handleExport() {
      try {
        const response = await exportAccountSubjects(this.searchForm)
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `会计科目_${new Date().getTime()}.xlsx`
        link.click()
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    handleImport() {
      this.importFile = null
      this.importDialogVisible = true
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    async handleImportSubmit() {
      if (!this.importFile) {
        this.$message.error('请选择要导入的文件')
        return
      }

      const formData = new FormData()
      formData.append('file', this.importFile)

      try {
        const response = await importAccountSubjects(formData)
        if (response.code === 200) {
          this.$message.success('导入成功')
          this.importDialogVisible = false
          this.loadSubjectsList()
        }
      } catch (error) {
        this.$message.error('导入失败：' + error.message)
      }
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadSubjectsList()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadSubjectsList()
    },
    getSubjectCategoryTag(category) {
      const tags = {
        assets: 'primary',
        liabilities: 'success',
        equity: 'warning',
        cost: 'danger',
        profit_loss: 'info'
      }
      return tags[category] || 'default'
    },
    getSubjectCategoryName(category) {
      const names = {
        assets: '资产类',
        liabilities: '负债类',
        equity: '所有者权益类',
        cost: '成本类',
        profit_loss: '损益类'
      }
      return names[category] || category
    },
    getAuxiliaryTypeName(type) {
      const names = {
        department: '部门',
        employee: '职员',
        customer: '客户',
        supplier: '供应商',
        project: '项目'
      }
      return names[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.account-subjects-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.search-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.subjects-table {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .el-table {
    border-radius: 8px;
    overflow: hidden;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.upload-demo {
  text-align: center;
}
</style>