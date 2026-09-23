<template>
  <div class="security-param-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-setting"></i>
            安全参数配置
          </h2>
          <p class="page-description">管理系统安全参数、业务参数和接口配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增参数
          </el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="参数名称">
            <el-input
              v-model="listQuery.paramName"
              placeholder="请输入参数名称"
              clearable
              style="width: 200px;"
              @keyup.enter.native="handleFilter"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="参数类型">
            <el-select
              v-model="listQuery.paramType"
              placeholder="请选择参数类型"
              clearable
              style="width: 150px;"
            >
              <el-option
                v-for="item in paramTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.isEnabled"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="启用" :value="1">
                <i class="el-icon-check"></i> 启用
              </el-option>
              <el-option label="禁用" :value="0">
                <i class="el-icon-close"></i> 禁用
              </el-option>
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
          <span class="title-text">参数列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column v-if="isColumnVisible('paramCode')" label="参数编码" prop="paramCode" align="center" width="150" show-overflow-tooltip>
          <template slot-scope="{row}">
            <el-tag size="small" type="info">{{ row.paramCode }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('paramName')" label="参数名称" prop="paramName" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="param-name">
              <i :class="getParamIcon(row.paramType)"></i>
              <span>{{ row.paramName }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('paramType')" label="参数类型" prop="paramType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getParamTypeColor(row.paramType)" size="small">
              {{ getParamTypeText(row.paramType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('paramValue')" label="参数值" prop="paramValue" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="param-value">
              <span v-if="row.paramType === 'SECURITY'" class="security-value">
                {{ maskSecurityValue(row.paramValue) }}
              </span>
              <span v-else>{{ row.paramValue }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="状态" class-name="status-col" width="100" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>

        <el-table-column v-if="isColumnVisible('updateTime')" label="更新时间" prop="updateTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="update-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.updateTime) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                查看
              </el-button>
              <el-button
                v-if="row.isEnabled !== 'deleted'"
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
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="fetchData" />
      </div>

    </el-card>

    <!-- 表格设置对话框 -->
    <el-dialog
      title="表格设置"
      :visible.sync="dialogTableSettingVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="table-setting-content">
        <el-form label-width="120px" size="small">
          <el-form-item label="显示列设置">
            <el-checkbox-group v-model="visibleColumns">
              <el-checkbox
                v-for="column in allColumns"
                :key="column.prop"
                :label="column.prop"
              >
                {{ column.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="每页显示条数">
            <el-select v-model="customPageSize" placeholder="选择每页显示条数" style="width: 120px;">
              <el-option label="10条" :value="10" />
              <el-option label="20条" :value="20" />
              <el-option label="50条" :value="50" />
              <el-option label="100条" :value="100" />
            </el-select>
          </el-form-item>
          <el-form-item label="表格高度">
            <el-radio-group v-model="tableHeight">
              <el-radio label="auto">自适应</el-radio>
              <el-radio label="400">固定400px</el-radio>
              <el-radio label="600">固定600px</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableSettingVisible = false">取消</el-button>
        <el-button type="primary" @click="applyTableSettings">确定</el-button>
      </div>
    </el-dialog>

    <!-- 参数配置对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增参数' : '编辑参数'"
      :visible.sync="dialogFormVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="100px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="参数编码" prop="paramCode">
          <el-input v-model="temp.paramCode" placeholder="请输入参数编码" />
        </el-form-item>
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="temp.paramName" placeholder="请输入参数名称" />
        </el-form-item>
        <el-form-item label="参数类型" prop="paramType">
          <el-select v-model="temp.paramType" placeholder="请选择参数类型" style="width: 100%;">
            <el-option label="系统参数" value="SYSTEM" />
            <el-option label="业务参数" value="BUSINESS" />
            <el-option label="安全参数" value="SECURITY" />
            <el-option label="接口参数" value="INTERFACE" />
          </el-select>
        </el-form-item>
        <el-form-item label="参数值" prop="paramValue">
          <el-input
            v-model="temp.paramValue"
            :type="temp.paramType === 'SECURITY' ? 'password' : 'text'"
            placeholder="请输入参数值"
            show-password
          />
        </el-form-item>
        <el-form-item label="参数描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入参数描述"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="temp.isEnabled"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getTreasuryParameterList, deleteTreasuryParameter, saveOrUpdateParameter, updateParameterStatus } from '@/api/globalTreasurer/treasuryCommon'
import { isResponseSuccess, handleResponseData, getErrorMessage } from '../../utils'
import { PARAM_TYPES, SUCCESS_CODE, PAGINATION_CONFIG } from '../../consts'

export default {
  name: 'SecurityParamConfig',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
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
        paramName: undefined,
        paramType: undefined,
        isEnabled: undefined
      },
      multipleSelection: [],
      dialogFormVisible: false,
      dialogTableSettingVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        paramCode: '',
        paramName: '',
        paramType: '',
        paramValue: '',
        description: '',
        isEnabled: 1
      },
      rules: {
        paramCode: [{ required: true, message: '参数编码不能为空', trigger: 'blur' }],
        paramName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
        paramType: [{ required: true, message: '参数类型不能为空', trigger: 'change' }],
        paramValue: [{ required: true, message: '参数值不能为空', trigger: 'blur' }]
      },
      paramTypes: PARAM_TYPES,
      // 表格设置相关
      allColumns: [
        { prop: 'paramCode', label: '参数编码' },
        { prop: 'paramName', label: '参数名称' },
        { prop: 'paramType', label: '参数类型' },
        { prop: 'paramValue', label: '参数值' },
        { prop: 'updateTime', label: '更新时间' }
      ],
      visibleColumns: ['paramCode', 'paramName', 'paramType', 'paramValue', 'updateTime'],
      customPageSize: 10,
      tableHeight: 'auto'
    }
  },
  created() {
    this.loadTableSettings()
    this.fetchData()
  },
  methods: {
    /**
     * 获取数据列表
     */
    async fetchData() {
      this.listLoading = true
      try {
        const response = await getTreasuryParameterList(this.listQuery)

        if (isResponseSuccess(response)) {
          const rawData = response.data || {}
          const list = rawData.tlist || rawData.list || []

          // 确保数据是数组
          if (Array.isArray(list)) {
            this.list = list
            this.total = Number(rawData.totalRecord || rawData.total) || this.list.length
          } else {
            this.$message.error('数据格式错误:list不是数组')
            this.list = []
            this.total = 0
          }
        } else {
          const errorMsg = getErrorMessage(response) || '获取数据失败'
          this.$message.error(errorMsg)
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取数据异常:', error)
        this.$message.error('获取数据失败: ' + error.message)
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 搜索过滤
     */
    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },

    /**
     * 重置搜索
     */
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        paramName: undefined,
        paramType: undefined,
        isEnabled: undefined
      }
      this.fetchData()
    },

    /**
     * 导出配置
     */
    async handleExport() {
      try {
        this.listLoading = true
        const params = new URLSearchParams(this.listQuery)
        const response = await fetch(`/qqsk/financial/basicConfig/securityParam/export?${params}`, {
          method: 'GET',
          headers: {
            'Authorization': localStorage.getItem('token') || ''
          }
        })

        if (response.ok) {
          const blob = await response.blob()
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `安全参数配置_${new Date().toLocaleDateString()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error('导出失败，请稍后重试')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },

    /**
     * 表格设置
     */
    handleTableSetting() {
      this.dialogTableSettingVisible = true
    },

    /**
     * 应用表格设置
     */
    applyTableSettings() {
      // 应用每页显示条数
      this.listQuery.limit = this.customPageSize
      this.listQuery.page = 1

      // 刷新表格
      this.fetchData()

      // 保存设置到localStorage
      const settings = {
        visibleColumns: this.visibleColumns,
        customPageSize: this.customPageSize,
        tableHeight: this.tableHeight
      }
      localStorage.setItem('securityParam_tableSettings', JSON.stringify(settings))

      this.dialogTableSettingVisible = false
      this.$message.success('表格设置已保存')
    },

    /**
     * 加载表格设置
     */
    loadTableSettings() {
      try {
        const settings = localStorage.getItem('securityParam_tableSettings')
        if (settings) {
          const parsed = JSON.parse(settings)
          this.visibleColumns = parsed.visibleColumns || this.visibleColumns
          this.customPageSize = parsed.customPageSize || this.customPageSize
          this.tableHeight = parsed.tableHeight || this.tableHeight

          if (parsed.customPageSize) {
            this.listQuery.limit = parsed.customPageSize
          }
        }
      } catch (error) {
        console.warn('加载表格设置失败:', error)
      }
    },

    /**
     * 判断列是否显示
     */
    isColumnVisible(prop) {
      return this.visibleColumns.includes(prop)
    },

    /**
     * 表格行样式
     */
    tableRowClassName({ row, rowIndex }) {
      if (row.isEnabled === 0) {
        return 'disabled-row'
      }
      return ''
    },

    /**
     * 多选变化
     */
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    /**
     * 获取参数图标
     */
    getParamIcon(type) {
      const iconMap = {
        'SYSTEM': 'el-icon-cpu',
        'BUSINESS': 'el-icon-office-building',
        'SECURITY': 'el-icon-lock',
        'INTERFACE': 'el-icon-connection'
      }
      return iconMap[type] || 'el-icon-setting'
    },

    /**
     * 掩码安全值
     */
    maskSecurityValue(value) {
      if (!value) return ''
      return value.length > 6 ? value.substring(0, 3) + '***' + value.substring(value.length - 3) : '***'
    },

    /**
     * 格式化时间
     */
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },

    /**
     * 状态变化
     */
    async handleStatusChange(row) {
      try {
        const response = await updateParameterStatus({
          id: row.id,
          isEnabled: row.isEnabled
        })
        if (isResponseSuccess(response)) {
          this.$message.success('状态更新成功')
        } else {
          // 回滚状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
          this.$message.error(getErrorMessage(response))
        }
      } catch (error) {
        // 回滚状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
        this.$message.error('状态更新失败，请检查网络连接')
      }
    },

    /**
     * 查看详情
     */
    handleView(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'view'
      this.dialogFormVisible = true
    },

    /**
     * 创建新记录
     */
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 编辑记录
     */
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    /**
     * 重置临时对象
     */
    resetTemp() {
      this.temp = {
        id: undefined,
        paramCode: '',
        paramName: '',
        paramType: '',
        paramValue: '',
        description: '',
        isEnabled: 1
      }
    },

    /**
     * 创建数据
     */
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdateParameter(this.temp)
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
              this.fetchData()
            } else {
              this.$message.error(response.message || '创建失败')
            }
          } catch (error) {
            this.$message.error('创建失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 更新数据
     */
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await saveOrUpdateParameter(this.temp)
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.fetchData()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            this.$message.error('更新失败，请检查网络连接')
          }
        }
      })
    },

    /**
     * 删除记录
     */
    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteTreasuryParameter(row.id)
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取数据
            this.fetchData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除参数配置失败:', error)
          this.$message.error('删除失败，请检查网络连接')
        }
      })
    },

    /**
     * 获取参数类型颜色
     */
    getParamTypeColor(type) {
      const colorMap = {
        'SYSTEM': 'primary',
        'BUSINESS': 'success',
        'SECURITY': 'warning',
        'INTERFACE': 'info'
      }
      return colorMap[type] || 'info'
    },

    /**
     * 获取参数类型文本
     */
    getParamTypeText(type) {
      const textMap = {
        'SYSTEM': '系统参数',
        'BUSINESS': '业务参数',
        'SECURITY': '安全参数',
        'INTERFACE': '接口参数'
      }
      return textMap[type] || type
    }
  }
}
</script>

<style lang="scss" scoped>
.security-param-config {
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

    .search-form {
      .demo-form-inline {
        .el-form-item {
          margin-bottom: 0;
        }
      }
    }
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

    .param-name {
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .param-value {
      .security-value {
        font-family: monospace;
        color: #f56c6c;
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
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-dialog__body {
  padding: 20px 20px 10px 20px;
}

// 表格设置对话框样式
.table-setting-content {
  .el-form-item {
    margin-bottom: 15px;
  }

  .el-checkbox-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .el-checkbox {
    margin-right: 0;
  }
}
</style>
