<template>
  <div class="data-backup-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>数据备份概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          刷新
        </el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="statistic-item">
            <div class="statistic-head">
              <i class="el-icon-folder" style="color: #409EFF"></i>
              <span class="statistic-title">备份总数</span>
            </div>
            <div class="statistic-value">{{ overview.totalBackups || 0 }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="statistic-item">
            <div class="statistic-head">
              <i class="el-icon-success" style="color: #67C23A"></i>
              <span class="statistic-title">成功备份</span>
            </div>
            <div class="statistic-value">{{ overview.successBackups || 0 }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="statistic-item">
            <div class="statistic-head">
              <i class="el-icon-files" style="color: #E6A23C"></i>
              <span class="statistic-title">备份大小</span>
            </div>
            <div class="statistic-value">{{ overview.totalSize || 0 }}<span class="statistic-suffix">GB</span></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="statistic-item">
            <div class="statistic-head">
              <i class="el-icon-time" style="color: #F56C6C"></i>
              <span class="statistic-title">最近备份</span>
            </div>
            <div class="statistic-value">{{ overview.lastBackupDays || 0 }}<span class="statistic-suffix">天前</span></div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card class="table-card">
      <div slot="header">
        <span>备份记录</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="createBackup">立即备份</el-button>
          <el-button type="success" size="small" @click="exportData">导出记录</el-button>
        </div>
      </div>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="备份类型">
          <el-select v-model="queryForm.backupType" placeholder="请选择备份类型" clearable>
            <el-option label="全量备份" value="full"></el-option>
            <el-option label="增量备份" value="incremental"></el-option>
            <el-option label="差异备份" value="differential"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备份状态">
          <el-select v-model="queryForm.status" placeholder="请选择备份状态" clearable>
            <el-option label="成功" value="success"></el-option>
            <el-option label="失败" value="failed"></el-option>
            <el-option label="进行中" value="running"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="backupList" border v-loading="loading">
        <el-table-column prop="backupId" label="备份ID" width="120"></el-table-column>
        <el-table-column prop="backupName" label="备份名称" width="200"></el-table-column>
        <el-table-column prop="backupType" label="备份类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getBackupTypeTag(scope.row.backupType)">
              {{ getBackupTypeText(scope.row.backupType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataSource" label="数据源" width="150"></el-table-column>
        <el-table-column prop="backupSize" label="备份大小" width="120" align="right">
          <template slot-scope="scope">
            {{ formatSize(scope.row.backupSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="160"></el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="160"></el-table-column>
        <el-table-column prop="duration" label="耗时" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.duration }}分钟
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">
              详情
            </el-button>
            <el-button type="text" size="small" @click="restoreBackup(scope.row)" :disabled="scope.row.status !== 'success'">
              恢复
            </el-button>
            <el-button type="text" size="small" @click="downloadBackup(scope.row)" :disabled="scope.row.status !== 'success'">
              下载
            </el-button>
            <el-button type="text" size="small" @click="deleteBackup(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right;"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import { getDataBackupList, createDataBackup, deleteDataBackup, restoreDataBackup, exportBackupList } from '@/api/enterprise/data'

export default {
  name: 'DataBackupTab',
  props: {
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      queryForm: {
        backupType: '',
        status: '',
        dateRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      overview: {
        totalBackups: 0,
        successBackups: 0,
        totalSize: 0,
        lastBackupDays: 0
      },
      backupList: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      await this.loadBackupList()
      this.pagination.total = this.backupList.length
      this.loading = false
    },
    async loadBackupList() {
      try {
        const params = {
          pageNumber: this.pagination.current,
          pageSize: this.pagination.size,
          enterpriseId: this.enterpriseId
        }
        // 传递筛选条件
        if (this.queryForm.backupType) params.backupType = this.queryForm.backupType
        if (this.queryForm.status) params.status = this.queryForm.status
        if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
          const fmt = (d) => { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth() + 1).padStart(2, '0') + '-' + String(dt.getDate()).padStart(2, '0') }
          params.entryDateStart = fmt(this.queryForm.dateRange[0])
          params.entryDateEnd = fmt(this.queryForm.dateRange[1])
        }

        const res = await getDataBackupList(params)

        if (res && res.data) {
          this.backupList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0

          // 计算概览数据
          const successCount = this.backupList.filter(i => i.status === 'success').length
          const totalSize = this.backupList.reduce((sum, i) => sum + (parseFloat(i.backupSize) || 0), 0)

          this.overview = {
            totalBackups: this.pagination.total,
            successBackups: successCount,
            totalSize: Math.round(totalSize * 10) / 10,
            lastBackupDays: this.backupList.length > 0 ? 0 : 0
          }
        }
      } catch (e) {
        console.error('加载备份列表失败', e)
        this.$message.error('加载备份列表失败')
      }
    },
    refreshData() {
      this.loadData()
      this.$message.success('数据刷新成功')
    },
    queryData() {
      this.pagination.current = 1
      this.loadData()
    },
    resetQuery() {
      this.queryForm = {
        backupType: '',
        status: '',
        dateRange: []
      }
      this.queryData()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    getBackupTypeTag(type) {
      const typeMap = {
        'full': 'primary',
        'incremental': 'success',
        'differential': 'warning'
      }
      return typeMap[type] || 'info'
    },
    getBackupTypeText(type) {
      const textMap = {
        'full': '全量备份',
        'incremental': '增量备份',
        'differential': '差异备份'
      }
      return textMap[type] || type
    },
    getStatusType(status) {
      const statusMap = {
        'success': 'success',
        'failed': 'danger',
        'running': 'primary'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'success': '成功',
        'failed': '失败',
        'running': '进行中'
      }
      return textMap[status] || status
    },
    formatSize(size) {
      if (size >= 1024) {
        return (size / 1024).toFixed(2) + 'GB'
      }
      return size + 'MB'
    },
    createBackup() {
      this.$confirm('确认立即备份数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在创建备份...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await createDataBackup({
            enterpriseId: this.enterpriseId,
            backupType: 'full'
          })
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('备份创建成功')
            this.loadData()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '备份创建失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('备份创建失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },
    async exportData() {
      try {
        const loading = this.$loading({ lock: true, text: '正在导出备份记录...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
        const response = await exportBackupList(this.enterpriseId)
        loading.close()
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `备份记录_${new Date().getTime()}.csv`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    viewDetail(row) {
      this.$alert(`<div style="line-height:2">
        <p><strong>备份ID：</strong>${row.backupId || '-'}</p>
        <p><strong>备份名称：</strong>${row.backupName || '-'}</p>
        <p><strong>备份类型：</strong>${this.getBackupTypeText(row.backupType)}</p>
        <p><strong>数据源：</strong>${row.dataSource || '-'}</p>
        <p><strong>备份大小：</strong>${this.formatSize(row.backupSize)}</p>
        <p><strong>状态：</strong>${this.getStatusText(row.status)}</p>
        <p><strong>备份时间：</strong>${row.backupTime || '-'}</p>
        <p><strong>过期时间：</strong>${row.expireTime || '-'}</p>
        <p><strong>操作人：</strong>${row.operator || '-'}</p>
        <p><strong>备注：</strong>${row.remark || '-'}</p>
      </div>`, '备份详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    restoreBackup(row) {
      this.$confirm('确认恢复该备份数据？此操作将覆盖当前数据！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在恢复数据...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await restoreDataBackup(row.backupId)
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('数据恢复成功')
            this.loadData()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '数据恢复失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('数据恢复失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },
    async downloadBackup(row) {
      try {
        const loading = this.$loading({ lock: true, text: '正在下载备份文件...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
        const { exportDataEntry } = require('@/api/enterprise/data')
        const response = await exportDataEntry()
        loading.close()
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${row.backupName || '备份'}_${new Date().getTime()}.csv`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('下载成功')
      } catch (error) {
        this.$message.error('下载失败：' + (error.message || '未知错误'))
      }
    },
    deleteBackup(row) {
      this.$confirm('确认删除该备份？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在删除...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await deleteDataBackup(row.backupId)
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('删除成功')
            this.loadData()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.data-backup-tab {
  padding: 20px;
}
.overview-card {
  margin-bottom: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.query-form {
  margin-bottom: 20px;
}
.statistic-item {
  text-align: center;
  padding: 10px 0;
}
.statistic-head {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  margin-bottom: 8px;
}
.statistic-title {
  font-size: 14px;
  color: #909399;
}
.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.statistic-suffix {
  font-size: 14px;
  font-weight: normal;
  color: #909399;
  margin-left: 4px;
}
</style>
