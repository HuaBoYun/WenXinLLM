<template>
  <el-dialog
    title="数据历史记录"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="handleClose"
  >
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索关键词"
          clearable
          @keyup.enter.native="handleSearch"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
      </el-col>
      <el-col :span="4">
        <el-select v-model="searchForm.changeType" placeholder="变更类型" clearable>
          <el-option label="全部" value=""></el-option>
          <el-option label="数据新增" value="create"></el-option>
          <el-option label="数据修改" value="update"></el-option>
          <el-option label="数据删除" value="delete"></el-option>
          <el-option label="状态变更" value="status"></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-col>
    </el-row>
    
    <el-table :data="historyList" border v-loading="loading">
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="changeTime" label="变更时间" width="180"></el-table-column>
      <el-table-column prop="changeUser" label="操作人" width="120"></el-table-column>
      <el-table-column prop="changeType" label="变更类型" width="120">
        <template slot-scope="scope">
          <el-tag size="small" :type="getChangeTypeTag(scope.row.changeType)">
            {{ getChangeTypeText(scope.row.changeType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="fieldName" label="变更字段" width="150"></el-table-column>
      <el-table-column prop="oldValue" label="原值" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column prop="newValue" label="新值" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column prop="changeReason" label="变更原因" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="viewDetail(scope.row)">
            查看详情
          </el-button>
          <el-button type="text" size="small" @click="rollback(scope.row)" v-if="scope.row.canRollback">
            回滚
          </el-button>
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
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportHistory">导出历史</el-button>
    </div>
    
    <!-- 详情对话框 -->
    <el-dialog
      title="变更详情"
      :visible.sync="detailVisible"
      width="600px"
      append-to-body
    >
      <el-descriptions :column="1" border v-if="currentDetail">
        <el-descriptions-item label="变更时间">
          {{ currentDetail.changeTime }}
        </el-descriptions-item>
        <el-descriptions-item label="操作人">
          {{ currentDetail.changeUser }}
        </el-descriptions-item>
        <el-descriptions-item label="变更类型">
          <el-tag :type="getChangeTypeTag(currentDetail.changeType)">
            {{ getChangeTypeText(currentDetail.changeType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="变更字段">
          {{ currentDetail.fieldName }}
        </el-descriptions-item>
        <el-descriptions-item label="原值">
          <pre>{{ currentDetail.oldValue }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="新值">
          <pre>{{ currentDetail.newValue }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="变更原因">
          {{ currentDetail.changeReason }}
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">
          {{ currentDetail.ipAddress }}
        </el-descriptions-item>
        <el-descriptions-item label="用户代理">
          {{ currentDetail.userAgent }}
        </el-descriptions-item>
      </el-descriptions>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getDataEntryHistory, exportDataHistory } from '@/api/enterprise/data'

export default {
  name: 'DataHistoryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      detailVisible: false,
      currentDetail: null,
      searchForm: {
        keyword: '',
        changeType: '',
        dateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      historyList: []
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
        this.loadHistoryData()
      }
    }
  },
  methods: {
    loadHistoryData() {
      this.loading = true
      const params = {
        enterpriseId: this.dataId,
        keyword: this.searchForm.keyword,
        changeType: this.searchForm.changeType,
        startDate: this.searchForm.dateRange && this.searchForm.dateRange[0],
        endDate: this.searchForm.dateRange && this.searchForm.dateRange[1],
        pageNumber: this.pagination.currentPage,
        pageSize: this.pagination.pageSize
      }
      getDataEntryHistory(params).then(response => {
        const data = response.data || {}
        const list = data.tlist || []
        this.historyList = this.transformHistoryData(list)
        this.pagination.total = data.totalRecord || 0
        this.loading = false
      }).catch(() => {
        this.historyList = []
        this.loading = false
      })
    },
    /**
     * 将后端返回的数据转换为表格展示格式
     */
    transformHistoryData(list) {
      const statusMap = {
        '草稿': 'create',
        '已提交': 'status',
        '已审核': 'status',
        '已退回': 'update',
        '已删除': 'delete'
      }
      return list.map(item => ({
        id: item.id,
        changeTime: item.updateTime || item.createTime,
        changeUser: item.submitter || '',
        changeType: statusMap[item.status] || 'update',
        fieldName: item.dataType || '',
        oldValue: '',
        newValue: item.enterpriseName || '',
        changeReason: item.status || '',
        canRollback: item.status !== '已审核' && item.status !== '已删除',
        _raw: item
      }))
    },
    getChangeTypeTag(type) {
      const typeMap = {
        'create': 'success',
        'update': 'warning',
        'delete': 'danger',
        'status': 'info'
      }
      return typeMap[type] || 'info'
    },
    getChangeTypeText(type) {
      const textMap = {
        'create': '数据新增',
        'update': '数据修改',
        'delete': '数据删除',
        'status': '状态变更'
      }
      return textMap[type] || type
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadHistoryData()
    },
    handleReset() {
      this.searchForm = {
        keyword: '',
        changeType: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadHistoryData()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadHistoryData()
    },
    viewDetail(row) {
      this.currentDetail = row
      this.detailVisible = true
    },
    rollback(row) {
      this.$confirm('确认要回滚此次变更吗？', '回滚确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('回滚操作成功')
        this.loadHistoryData()
        this.$emit('refresh')
      })
    },
    exportHistory() {
      const params = {
        enterpriseId: this.dataId,
        keyword: this.searchForm.keyword,
        changeType: this.searchForm.changeType,
        startDate: this.searchForm.dateRange && this.searchForm.dateRange[0],
        endDate: this.searchForm.dateRange && this.searchForm.dateRange[1]
      }
      this.$message.info('历史记录导出中...')
      exportDataHistory(params).then(response => {
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '数据历史记录.csv'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.$message.error('导出失败，请稍后重试')
      })
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
}
</style>
