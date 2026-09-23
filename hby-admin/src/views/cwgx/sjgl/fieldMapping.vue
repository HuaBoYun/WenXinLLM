<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <el-form :inline="true" :model="queryParams" size="small" label-width="100px">
          <el-form-item label="数据源">
            <el-select v-model="queryParams.dataSourceId" placeholder="请选择数据源" clearable style="width: 250px" @change="handleDataSourceChange">
              <el-option v-for="item in dataSourceList" :key="item.configId" :label="item.configName" :value="item.configId" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标表">
            <el-input v-model="queryParams.targetTable" placeholder="请输入目标表名" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="源字段">
            <el-input v-model="queryParams.sourceField" placeholder="请输入源字段名" clearable style="width: 150px" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="目标字段">
            <el-input v-model="queryParams.targetField" placeholder="请输入目标字段名" clearable style="width: 150px" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.isEnabled" placeholder="请选择状态" clearable style="width: 100px">
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </el-form-item>
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button style="margin-left: 10px" size="small" icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form>
      </div>
      <div style="margin-top: 20px" v-if="queryParams.dataSourceId">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增映射</el-button>
        <el-button type="success" size="small" icon="el-icon-download" @click="handleImport">导入映射</el-button>
        <el-button type="warning" size="small" icon="el-icon-upload2" @click="handleExport">导出映射</el-button>
        <el-button type="info" size="small" icon="el-icon-copy-document" @click="handleCopy">复制映射</el-button>
        <el-button :disabled="multiple" type="danger" size="small" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="success" size="small" icon="el-icon-magic-stick" @click="handleAutoMatch">自动匹配</el-button>
      </div>
    </el-card>

    <el-card class="table-container" shadow="never">
      <el-table
        v-loading="listLoading"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        row-key="mappingId"
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column label="源字段" prop="sourceField" min-width="120" show-overflow-tooltip />
        <el-table-column label="目标字段" prop="targetField" min-width="120" show-overflow-tooltip />
        <el-table-column label="字段类型" prop="fieldType" width="100" align="center" />
        <el-table-column label="长度" prop="fieldLength" width="80" align="center" />
        <el-table-column label="是否可为空" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isNullable" type="success">是</el-tag>
            <el-tag v-else type="danger">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="默认值" prop="defaultValue" width="100" show-overflow-tooltip />
        <el-table-column label="转换规则" prop="transformationRule" min-width="150" show-overflow-tooltip />
        <el-table-column label="验证规则" prop="validationRule" min-width="120" show-overflow-tooltip />
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="排序" width="80" align="center">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.sortOrder" :min="1" size="mini" @change="handleSortChange(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="queryParams.pageNo"
          :page-sizes="[10, 20, 50, 100]"
          :page-size.sync="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源" prop="dataSourceId">
              <el-select v-model="form.dataSourceId" placeholder="请选择数据源" style="width: 100%" :disabled="form.mappingId">
                <el-option v-for="item in dataSourceList" :key="item.configId" :label="item.configName" :value="item.configId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标表" prop="targetTable">
              <el-input v-model="form.targetTable" placeholder="请输入目标表名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源字段" prop="sourceField">
              <el-input v-model="form.sourceField" placeholder="请输入源字段名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标字段" prop="targetField">
              <el-input v-model="form.targetField" placeholder="请输入目标字段名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="字段类型">
              <el-select v-model="form.fieldType" placeholder="请选择字段类型" style="width: 100%">
                <el-option label="VARCHAR" value="VARCHAR" />
                <el-option label="CHAR" value="CHAR" />
                <el-option label="NUMBER" value="NUMBER" />
                <el-option label="DECIMAL" value="DECIMAL" />
                <el-option label="INTEGER" value="INTEGER" />
                <el-option label="DATE" value="DATE" />
                <el-option label="TIMESTAMP" value="TIMESTAMP" />
                <el-option label="CLOB" value="CLOB" />
                <el-option label="BLOB" value="BLOB" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="字段长度">
              <el-input-number v-model="form.fieldLength" :min="1" style="width: 100%" placeholder="字段长度" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="可为空">
              <el-switch v-model="form.isNullable" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="默认值">
              <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号">
              <el-input-number v-model="form.sortOrder" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="转换规则">
              <el-input v-model="form.transformationRule" type="textarea" rows="2" placeholder="例如：UPPER({source_field}) 或 SUBSTRING({source_field}, 1, 10)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="验证规则">
              <el-input v-model="form.validationRule" type="textarea" rows="2" placeholder="例如：NOT NULL, UNIQUE, CHECK(value > 0)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" rows="2" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="form.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入字段映射" :visible.sync="importDialogVisible" width="600px">
      <el-form :model="importForm" label-width="100px">
        <el-form-item label="导入方式">
          <el-radio-group v-model="importForm.overwrite">
            <el-radio :label="false">追加模式</el-radio>
            <el-radio :label="true">覆盖模式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="导入文件">
          <el-upload
            class="upload-demo"
            drag
            action=""
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            accept=".json,.csv,.xlsx"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">支持json、csv、xlsx格式文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 复制映射对话框 -->
    <el-dialog title="复制字段映射" :visible.sync="copyDialogVisible" width="500px">
      <el-form :model="copyForm" label-width="100px">
        <el-form-item label="源数据源">
          <el-select v-model="copyForm.sourceDataSourceId" placeholder="请选择源数据源" style="width: 100%" @change="handleSourceDataSourceChange">
            <el-option v-for="item in dataSourceList" :key="item.configId" :label="item.configName" :value="item.configId" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标数据源">
          <el-select v-model="copyForm.targetDataSourceId" placeholder="请选择目标数据源" style="width: 100%">
            <el-option v-for="item in dataSourceList" :key="item.configId" :label="item.configName" :value="item.configId" :disabled="item.configId === copyForm.sourceDataSourceId" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCopySubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDataSourceList } from '@/api/cwgx/dataSource'
import { getFieldMappingList, getFieldMapping, saveFieldMapping, updateFieldMapping, deleteFieldMapping, batchOperationFieldMapping, importFieldMapping, exportFieldMapping, copyFieldMapping, updateSortOrder } from '@/api/cwgx/fieldMapping'

export default {
  name: 'FieldMapping',
  data() {
    return {
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        dataSourceId: '',
        targetTable: '',
        sourceField: '',
        targetField: '',
        isEnabled: null
      },
      list: [],
      total: 0,
      listLoading: true,
      multiple: true,
      multipleSelection: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      rules: {
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        targetTable: [
          { required: true, message: '请输入目标表名', trigger: 'blur' }
        ],
        sourceField: [
          { required: true, message: '请输入源字段名', trigger: 'blur' }
        ],
        targetField: [
          { required: true, message: '请输入目标字段名', trigger: 'blur' }
        ]
      },
      dataSourceList: [],
      importDialogVisible: false,
      importForm: {
        overwrite: false
      },
      fileList: [],
      copyDialogVisible: false,
      copyForm: {
        sourceDataSourceId: '',
        targetDataSourceId: ''
      }
    }
  },
  created() {
    this.getDataSourceList()
  },
  methods: {
    async getDataSourceList() {
      try {
        const res = await getDataSourceList({
          pageNo: 1,
          pageSize: 1000,
          isEnabled: true
        })
        if (res.code === 1) {
          this.dataSourceList = res.data.tlist || []
        }
      } catch (error) {
        console.error(error)
      }
    },
    async getList() {
      if (!this.queryParams.dataSourceId) {
        this.list = []
        this.total = 0
        this.listLoading = false
        return
      }

      this.listLoading = true
      try {
        const res = await getFieldMappingList(this.queryParams)
        if (res.code === 1) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.message || '获取数据失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取数据失败')
      }
      this.listLoading = false
    },
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 10,
        dataSourceId: this.queryParams.dataSourceId,
        targetTable: '',
        sourceField: '',
        targetField: '',
        isEnabled: null
      }
      this.getList()
    },
    handleDataSourceChange() {
      this.handleQuery()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.multiple = !val.length
    },
    handleAdd() {
      this.dialogTitle = '新增字段映射'
      this.form = {
        dataSourceId: this.queryParams.dataSourceId,
        isNullable: true,
        isEnabled: true
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    async handleUpdate(row) {
      try {
        const res = await getFieldMapping(row.mappingId)
        if (res.code === 1) {
          this.form = res.data
          this.dialogTitle = '编辑字段映射'
          this.dialogVisible = true
          this.$nextTick(() => {
            this.$refs.form && this.$refs.form.clearValidate()
          })
        }
      } catch (error) {
        console.error(error)
        this.$message.error('获取详情失败')
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (this.form.mappingId) {
              res = await updateFieldMapping(this.form.mappingId, this.form)
            } else {
              res = await saveFieldMapping(this.form)
            }
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.getList()
            } else {
              this.$message.error(res.message || '保存失败')
            }
          } catch (error) {
            console.error(error)
            this.$message.error('保存失败')
          }
        }
      })
    },
    async handleDelete(row) {
      this.$confirm('确认删除该字段映射吗？', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteFieldMapping(row.mappingId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error(error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确认删除选中的${this.multipleSelection.length}条数据吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          const mappingIds = this.multipleSelection.map(item => item.mappingId)
          const res = await batchOperationFieldMapping({
            operationType: 'DELETE',
            mappingIds: mappingIds
          })
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error(error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    async handleStatusChange(row) {
      try {
        const res = await batchOperationFieldMapping({
          operationType: row.isEnabled ? 'ENABLE' : 'DISABLE',
          mappingIds: [row.mappingId]
        })
        if (res.code !== 1) {
          row.isEnabled = !row.isEnabled
          this.$message.error(res.message || '状态更新失败')
        }
      } catch (error) {
        row.isEnabled = !row.isEnabled
        console.error(error)
        this.$message.error('状态更新失败')
      }
    },
    async handleSortChange(row) {
      try {
        const res = await updateSortOrder(row.mappingId, row.sortOrder)
        if (res.code !== 1) {
          this.$message.error(res.message || '排序更新失败')
          this.getList()
        }
      } catch (error) {
        console.error(error)
        this.$message.error('排序更新失败')
        this.getList()
      }
    },
    handleImport() {
      if (!this.queryParams.dataSourceId) {
        this.$message.warning('请先选择数据源')
        return
      }
      this.importDialogVisible = true
      this.fileList = []
    },
    async handleExport() {
      if (!this.queryParams.dataSourceId) {
        this.$message.warning('请先选择数据源')
        return
      }
      try {
        const res = await exportFieldMapping(this.queryParams.dataSourceId)
        if (res.code === 1) {
          // 下载文件
          const blob = new Blob([JSON.stringify(res.data, null, 2)], { type: 'application/json' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = `field_mapping_${Date.now()}.json`
          a.click()
          window.URL.revokeObjectURL(url)
        } else {
          this.$message.error(res.message || '导出失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('导出失败')
      }
    },
    handleCopy() {
      this.copyDialogVisible = true
      this.copyForm = {
        sourceDataSourceId: '',
        targetDataSourceId: ''
      }
    },
    async handleAutoMatch() {
      this.$message.info('自动匹配功能开发中...')
    },
    handleSourceDataSourceChange() {
      this.copyForm.targetDataSourceId = ''
    },
    handleFileChange(file) {
      this.fileList = [file]
    },
    async handleImportSubmit() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      this.$message.info('导入功能开发中...')
    },
    async handleCopySubmit() {
      if (!this.copyForm.sourceDataSourceId || !this.copyForm.targetDataSourceId) {
        this.$message.warning('请选择源数据源和目标数据源')
        return
      }
      if (this.copyForm.sourceDataSourceId === this.copyForm.targetDataSourceId) {
        this.$message.warning('源数据源和目标数据源不能相同')
        return
      }
      try {
        const res = await copyFieldMapping(this.copyForm.sourceDataSourceId, this.copyForm.targetDataSourceId)
        if (res.code === 1) {
          this.$message.success('复制成功')
          this.copyDialogVisible = false
          this.getList()
        } else {
          this.$message.error(res.message || '复制失败')
        }
      } catch (error) {
        console.error(error)
        this.$message.error('复制失败')
      }
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNo = val
      this.getList()
    }
  }
}
</script>

<style scoped>
.filter-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}

.upload-demo {
  width: 100%;
}
</style>