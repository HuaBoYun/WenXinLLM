<template>
  <div class="seal-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-stamp"></i>
            印鉴类型管理
          </h2>
          <p class="page-description">管理企业印鉴类型配置，包括公章、财务章、法人章等</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增印鉴类型
          </el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="类型名称">
            <el-input
              v-model="listQuery.name"
              placeholder="请输入印鉴类型名称"
              clearable
              style="width: 200px;"
              @keyup.enter.native="handleFilter"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="印鉴级别">
            <el-select
              v-model="listQuery.sealLevel"
              placeholder="请选择印鉴级别"
              clearable
              style="width: 150px;"
            >
              <el-option label="一级印鉴" value="1">
                <i class="el-icon-medal-1"></i> 一级印鉴
              </el-option>
              <el-option label="二级印鉴" value="2">
                <i class="el-icon-medal"></i> 二级印鉴
              </el-option>
              <el-option label="三级印鉴" value="3">
                <i class="el-icon-trophy"></i> 三级印鉴
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
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
          <span class="title-text">印鉴类型列表</span>
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
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />

        <el-table-column label="印鉴类型" prop="name" align="center" width="180" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="seal-type-info">
              <i class="el-icon-stamp seal-icon"></i>
              <div class="seal-details">
                <div class="seal-name">{{ row.name }}</div>
                <div class="seal-code">{{ row.code || 'ST' + String(row.id).padStart(3, '0') }}</div>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="印鉴级别" prop="sealLevel" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getSealLevelColor(row.sealLevel)" size="small">
              <i :class="getSealLevelIcon(row.sealLevel)"></i>
              {{ getSealLevelText(row.sealLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="适用范围" prop="scope" align="center" width="150">
          <template slot-scope="{row}">
            <el-tag size="mini" type="info">{{ row.scope || '通用' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span class="description-text">{{ row.description || '暂无描述' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态" class-name="status-col" width="100" align="center">
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

        <el-table-column label="创建时间" prop="createTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="create-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.createTime) }}
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
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { getSealTypeList, saveOrUpdateSealType, deleteSealType, updateSealTypeStatus } from '@/api/globalTreasurer/czgg'

export default {
  name: 'SealTypeManage',
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
        name: undefined,
        sealLevel: undefined,
        status: undefined
      },
      multipleSelection: [],
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        id: undefined,
        name: '',
        code: '',
        sealLevel: '',
        scope: '',
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '印鉴类型名称不能为空', trigger: 'blur' }],
        code: [{ required: true, message: '类型编码不能为空', trigger: 'blur' }],
        sealLevel: [{ required: true, message: '印鉴级别不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getSealTypeList(this.listQuery).then(response => {
        if (response.code === 1) {
          this.list = response.data || []
          this.total = response.result ? response.result.total : (response.total || 0)
        } else {
          this.$message.error(response.msg || '获取数据失败')
        }
        this.listLoading = false
      }).catch(error => {
        console.error('获取印鉴类型列表失败:', error)
        this.$message.error('获取数据失败')
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        name: undefined,
        sealLevel: undefined,
        status: undefined
      }
      this.getList()
    },
    handleExport() {
      this.$message.info('导出功能开发中...')
    },
    handleTableSetting() {
      this.$message.info('表格设置功能开发中...')
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getSealLevelColor(level) {
      const colorMap = {
        '1': 'danger',
        '2': 'warning',
        '3': 'success'
      }
      return colorMap[level] || 'info'
    },
    getSealLevelIcon(level) {
      const iconMap = {
        '1': 'el-icon-medal-1',
        '2': 'el-icon-medal',
        '3': 'el-icon-trophy'
      }
      return iconMap[level] || 'el-icon-trophy'
    },
    getSealLevelText(level) {
      const textMap = {
        '1': '一级印鉴',
        '2': '二级印鉴',
        '3': '三级印鉴'
      }
      return textMap[level] || '未知级别'
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    handleStatusChange(row) {
      updateSealTypeStatus({ id: row.id, isEnabled: row.status }).then(response => {
        if (response.code === 1) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.msg || '状态更新失败')
          row.status = row.status === 1 ? 0 : 1 // 回滚状态
        }
      }).catch(() => {
        this.$message.error('状态更新失败')
        row.status = row.status === 1 ? 0 : 1 // 回滚状态
      })
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        code: '',
        sealLevel: '',
        scope: '',
        description: '',
        status: 1
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          saveOrUpdateSealType(this.temp).then(response => {
            if (response.code === 1) {
              this.$message.success('创建成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '创建失败')
            }
          }).catch(() => {
            this.$message.error('创建失败')
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          saveOrUpdateSealType(this.temp).then(response => {
            if (response.code === 1) {
              this.$message.success('更新成功')
              this.dialogFormVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '更新失败')
            }
          }).catch(() => {
            this.$message.error('更新失败')
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSealType(row.id).then(response => {
          if (response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.seal-type-manage {
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

    .seal-type-info {
      display: flex;
      align-items: center;

      .seal-icon {
        font-size: 20px;
        color: #409eff;
        margin-right: 12px;
      }

      .seal-details {
        .seal-name {
          font-weight: 600;
          color: #303133;
        }

        .seal-code {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }

    .description-text {
      color: #606266;
    }

    .create-time {
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
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}

::v-deep .el-dialog__body {
  padding: 20px 20px 10px 20px;
}
</style>
