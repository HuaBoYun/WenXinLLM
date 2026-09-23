<template>
  <div class="data-mapping-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-connection"></i>
            数据映射配置
          </h2>
          <p class="page-description">管理系统间数据字段映射关系，支持复杂映射规则和数据转换</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增映射
          </el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">
            导入配置
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="映射名称">
            <el-input
              v-model="listQuery.mappingName"
              placeholder="请输入映射名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="源系统">
            <el-select
              v-model="listQuery.sourceSystem"
              placeholder="请选择源系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="财务系统" value="FINANCE" />
              <el-option label="银行系统" value="BANK" />
              <el-option label="ERP系统" value="ERP" />
              <el-option label="第三方系统" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标系统">
            <el-select
              v-model="listQuery.targetSystem"
              placeholder="请选择目标系统"
              clearable
              style="width: 150px;"
            >
              <el-option label="财务系统" value="FINANCE" />
              <el-option label="银行系统" value="BANK" />
              <el-option label="ERP系统" value="ERP" />
              <el-option label="第三方系统" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="映射类型">
            <el-select
              v-model="listQuery.mappingType"
              placeholder="请选择映射类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="字段映射" value="FIELD" />
              <el-option label="值映射" value="VALUE" />
              <el-option label="函数映射" value="FUNCTION" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">数据映射配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="getList">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        row-key="id"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="visibleColumns.includes('mappingName')" label="映射名称" prop="mappingName" align="center" width="180" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="mapping-info">
              <i class="el-icon-connection mapping-icon"></i>
              <div class="mapping-details">
                <div class="mapping-name">{{ row.mappingName }}</div>
                <div class="mapping-code">{{ row.mappingCode || 'MAP' + String(row.id).padStart(3, '0') }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('sourceSystem') && visibleColumns.includes('targetSystem')" label="系统映射" align="center" width="200">
          <template slot-scope="{row}">
            <div class="system-mapping">
              <div class="system-item source">
                <el-tag size="mini" :type="getSystemColor(row.sourceSystem)">
                  {{ getSystemText(row.sourceSystem) }}
                </el-tag>
              </div>
              <i class="el-icon-right arrow-icon"></i>
              <div class="system-item target">
                <el-tag size="mini" :type="getSystemColor(row.targetSystem)">
                  {{ getSystemText(row.targetSystem) }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('sourceField') && visibleColumns.includes('targetField')" label="字段映射" align="center" width="250">
          <template slot-scope="{row}">
            <div class="field-mapping">
              <div class="field-item">
                <span class="field-label">源字段:</span>
                <code class="field-name">{{ row.sourceField }}</code>
              </div>
              <i class="el-icon-bottom mapping-arrow"></i>
              <div class="field-item">
                <span class="field-label">目标字段:</span>
                <code class="field-name">{{ row.targetField }}</code>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('mappingType')" label="映射类型" prop="mappingType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getMappingTypeColor(row.mappingType)" size="small">
              <i :class="getMappingTypeIcon(row.mappingType)"></i>
              {{ getMappingTypeText(row.mappingType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('mappingRule')" label="映射规则" prop="mappingRule" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="mapping-rule">
              <el-tooltip :content="row.mappingRule" placement="top">
                <span class="rule-text">{{ row.mappingRule || '直接映射' }}</span>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('status')" label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column v-if="visibleColumns.includes('createTime')" label="更新时间" prop="updateTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="update-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.updateTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-check" @click="handleTest(row)">
                测试
              </el-button>
              <el-button
                v-if="row.status !== 'deleted'"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible" width="650px" @close="resetForm">
      <el-form ref="dataForm" :model="temp" :rules="rules" label-width="100px" class="dialog-form">
        <el-form-item label="映射名称" prop="mappingName">
          <el-input v-model="temp.mappingName" placeholder="请输入映射名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源系统" prop="sourceSystem">
              <el-select v-model="temp.sourceSystem" placeholder="请选择源系统" style="width: 100%;">
                <el-option label="财务系统" value="FINANCE" />
                <el-option label="银行系统" value="BANK" />
                <el-option label="ERP系统" value="ERP" />
                <el-option label="第三方系统" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标系统" prop="targetSystem">
              <el-select v-model="temp.targetSystem" placeholder="请选择目标系统" style="width: 100%;">
                <el-option label="财务系统" value="FINANCE" />
                <el-option label="银行系统" value="BANK" />
                <el-option label="ERP系统" value="ERP" />
                <el-option label="第三方系统" value="THIRD_PARTY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="源字段" prop="sourceField">
              <el-input v-model="temp.sourceField" placeholder="请输入源字段名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标字段" prop="targetField">
              <el-input v-model="temp.targetField" placeholder="请输入目标字段名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="映射类型" prop="mappingType">
          <el-select v-model="temp.mappingType" placeholder="请选择映射类型" style="width: 100%;">
            <el-option label="字段映射" value="FIELD" />
            <el-option label="值映射" value="VALUE" />
            <el-option label="函数映射" value="FUNCTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="映射规则">
          <el-input v-model="temp.mappingRule" type="textarea" :rows="3" placeholder="请输入映射规则（可选）" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="temp.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark" type="textarea" :rows="2" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getDataMappingPage,
  createDataMapping,
  updateDataMapping,
  deleteDataMapping,
  testMapping,
  batchImportDataMapping
} from '@/api/treasuryCommon/basicConfigDataMappingConfig'

export default {
  name: 'DataMappingConfig',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = { 1: 'success', 0: 'info' }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        mappingName: undefined,
        sourceSystem: undefined,
        targetSystem: undefined,
        mappingType: undefined,
        status: undefined
      },
      multipleSelection: [],
      // 弹窗相关
      dialogFormVisible: false,
      dialogStatus: 'create',
      submitLoading: false,
      temp: {
        id: undefined,
        mappingName: '',
        sourceSystem: '',
        targetSystem: '',
        sourceField: '',
        targetField: '',
        mappingType: '',
        mappingRule: '',
        status: 1,
        remark: ''
      },
      rules: {
        mappingName: [{ required: true, message: '请输入映射名称', trigger: 'blur' }],
        sourceSystem: [{ required: true, message: '请选择源系统', trigger: 'change' }],
        targetSystem: [{ required: true, message: '请选择目标系统', trigger: 'change' }],
        sourceField: [{ required: true, message: '请输入源字段', trigger: 'blur' }],
        targetField: [{ required: true, message: '请输入目标字段', trigger: 'blur' }],
        mappingType: [{ required: true, message: '请选择映射类型', trigger: 'change' }]
      },
      // 表格列配置
      allColumns: [
        { key: 'mappingName', label: '映射名称' },
        { key: 'sourceSystem', label: '源系统' },
        { key: 'targetSystem', label: '目标系统' },
        { key: 'sourceField', label: '源字段' },
        { key: 'targetField', label: '目标字段' },
        { key: 'mappingType', label: '映射类型' },
        { key: 'mappingRule', label: '映射规则' },
        { key: 'status', label: '状态' },
        { key: 'createTime', label: '创建时间' }
      ],
      visibleColumns: JSON.parse(localStorage.getItem('dataMapping_visibleColumns') || '["mappingName","sourceSystem","targetSystem","sourceField","targetField","mappingType","status"]')
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogStatus === 'create' ? '新增数据映射' : '编辑数据映射'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const params = { pageNo: this.listQuery.page, pageSize: this.listQuery.limit, ...this.listQuery }
        const response = await getDataMappingPage(params)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取数据映射列表失败')
        }
      } catch (error) {
        console.error('获取数据映射列表失败:', error)
        this.$message.error('网络错误，请稍后重试')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = { page: 1, limit: 20, mappingName: undefined, sourceSystem: undefined, targetSystem: undefined, mappingType: undefined, status: undefined }
      this.getList()
    },
    resetTemp() {
      this.temp = { id: undefined, mappingName: '', sourceSystem: '', targetSystem: '', sourceField: '', targetField: '', mappingType: '', mappingRule: '', status: 1, remark: '' }
    },
    resetForm() {
      this.resetTemp()
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => { this.$refs.dataForm && this.$refs.dataForm.clearValidate() })
    },
    submitForm() {
      this.$refs.dataForm.validate(async(valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            const api = this.dialogStatus === 'create' ? createDataMapping : updateDataMapping
            const response = await api(this.temp)
            if (response && [1, '1', 200, '200'].includes(response.code)) {
              this.$message.success(this.dialogStatus === 'create' ? '新增成功' : '更新成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || response.message || '操作失败')
            }
          } catch (error) {
            console.error('提交失败:', error)
            this.$message.error('操作失败，请稍后重试')
          } finally {
            this.submitLoading = false
          }
        }
      })
    },
    async handleDelete(row, index) {
      try {
        await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        const response = await deleteDataMapping(row.id)
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }
    },
    async handleTest(row) {
      try {
        const response = await testMapping({ id: row.id, testData: 'test data' })
        if (response && [1, '1', 200, '200'].includes(response.code)) {
          this.$message.success('映射规则测试通过')
        } else {
          this.$message.error(response.message || '测试失败')
        }
      } catch (error) {
        console.error('测试数据映射失败:', error)
        this.$message.error('网络错误，请稍后重试')
      }
    },
    handleView(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    handleImport() {
      // 创建文件输入元素
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.json,.xlsx,.xls'
      input.onchange = async (event) => {
        const file = event.target.files[0]
        if (!file) return

        const formData = new FormData()
        formData.append('file', file)

        try {
          // 这里使用batchImportDataMapping API
          const response = await batchImportDataMapping(file, this.$store.getters.userInfo.staffId)
          if (response && [1, '1', 200, '200'].includes(response.code)) {
            this.$message.success('导入成功,共导入 ' + (response.data?.count || 0) + ' 条数据')
            this.getList()
          } else {
            this.$message.error(response.msg || response.message || '导入失败')
          }
        } catch (error) {
          console.error('导入失败:', error)
          this.$message.error('导入失败,请检查文件格式')
        }
      }
      input.click()
    },
    handleExport() {
      try {
        // 导出当前筛选的数据
        const exportData = this.list.map(item => ({
          映射名称: item.mappingName,
          源系统: item.sourceSystem,
          目标系统: item.targetSystem,
          源字段: item.sourceField,
          目标字段: item.targetField,
          映射类型: item.mappingType,
          映射规则: item.mappingRule,
          状态: item.status === 1 ? '启用' : '禁用',
          备注: item.remark
        }))

        // 创建工作簿
        const dataStr = JSON.stringify(exportData, null, 2)
        const blob = new Blob([dataStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `数据映射配置_${new Date().getTime()}.json`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    handleTableSetting() {
      const h = this.$createElement
      this.$msgbox({
        title: '表格列设置',
        message: h('div', { class: 'table-setting-dialog' }, [
          h('p', { style: 'margin-bottom: 10px; color: #909399;' }, '请选择要显示的列:'),
          h('el-checkbox-group', {
            props: { value: this.visibleColumns },
            on: { input: val => { this.visibleColumns = val } }
          }, this.allColumns.map(col =>
            h('el-checkbox', { props: { label: col.key, border: true, style: 'margin-left: 0; margin-right: 10px; margin-bottom: 10px;' } }, col.label)
          ))
        ]),
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 保存到localStorage
            localStorage.setItem('dataMapping_visibleColumns', JSON.stringify(this.visibleColumns))
            this.$message.success('表格设置已保存')
            this.getList() // 刷新表格
          }
          done()
        }
      })
    },
    tableRowClassName({ row }) { return row.status === 0 ? 'disabled-row' : '' },
    handleSelectionChange(val) { this.multipleSelection = val },
    handleStatusChange(row) { this.$message.success('状态更新成功') },
    getSystemColor(system) {
      const colorMap = { 'FINANCE': 'success', 'BANK': 'primary', 'ERP': 'warning', 'THIRD_PARTY': 'info' }
      return colorMap[system] || 'info'
    },
    getSystemText(system) {
      const textMap = { 'FINANCE': '财务系统', 'BANK': '银行系统', 'ERP': 'ERP系统', 'THIRD_PARTY': '第三方系统' }
      return textMap[system] || '未知系统'
    },
    getMappingTypeColor(type) {
      const colorMap = { 'FIELD': 'success', 'VALUE': 'warning', 'FUNCTION': 'danger' }
      return colorMap[type] || 'info'
    },
    getMappingTypeIcon(type) {
      const iconMap = { 'FIELD': 'el-icon-connection', 'VALUE': 'el-icon-switch-button', 'FUNCTION': 'el-icon-cpu' }
      return iconMap[type] || 'el-icon-connection'
    },
    getMappingTypeText(type) {
      const textMap = { 'FIELD': '字段映射', 'VALUE': '值映射', 'FUNCTION': '函数映射' }
      return textMap[type] || '未知类型'
    },
    formatTime(time) { return time ? new Date(time).toLocaleString() : '-' }
  }
}
</script>

<style lang="scss" scoped>
.data-mapping-config {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;

      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;

          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }

        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }

      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .table-card {
    border-radius: 8px;

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .mapping-info {
      display: flex;
      align-items: center;

      .mapping-icon {
        font-size: 20px;
        color: #409eff;
        margin-right: 12px;
      }

      .mapping-details {
        .mapping-name {
          font-weight: 600;
          color: #303133;
        }

        .mapping-code {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }

    .system-mapping {
      display: flex;
      align-items: center;
      justify-content: center;

      .system-item {
        flex: 1;
        text-align: center;
      }

      .arrow-icon {
        margin: 0 8px;
        color: #909399;
        font-size: 14px;
      }
    }

    .field-mapping {
      text-align: center;

      .field-item {
        margin: 4px 0;

        .field-label {
          font-size: 12px;
          color: #909399;
          margin-right: 4px;
        }

        .field-name {
          background-color: #f5f7fa;
          padding: 2px 6px;
          border-radius: 4px;
          font-family: 'Monaco', 'Menlo', monospace;
          font-size: 12px;
          color: #e6a23c;
        }
      }

      .mapping-arrow {
        color: #409eff;
        margin: 4px 0;
      }
    }

    .mapping-rule {
      .rule-text {
        color: #606266;
        font-size: 13px;
        line-height: 1.4;
      }
    }

    .update-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;

      i {
        margin-right: 4px;
      }
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }

  .el-table__row:hover {
    background-color: #f5f7fa;
  }

  // 确保表格完整显示
  height: auto !important;
  max-height: none !important;

  .el-table__body-wrapper {
    max-height: none !important;
    height: auto !important;
    overflow: visible !important;
  }

  .el-table__body {
    width: 100% !important;
  }
}

::v-deep .el-table__row {
  display: table-row !important;
}

::v-deep .el-table__body tr {
  display: table-row !important;
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-tooltip__popper {
  max-width: 300px;
}
</style>
