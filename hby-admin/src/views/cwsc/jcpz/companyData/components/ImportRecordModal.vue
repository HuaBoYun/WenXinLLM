<template>
  <el-dialog
    title="导入记录"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="record-container">
      <div class="search-section">
        <el-form :inline="true" :model="queryForm" @submit.native.prevent>
          <el-form-item label="导入人">
            <el-input
              v-model="queryForm.creatname"
              placeholder="请输入导入人"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchData">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table v-loading="listLoading" :data="list" style="width: 100%">
        <el-table-column
          prop="startdate"
          label="导入时间"
          align="center"
          width="180"
        />
        <el-table-column
          prop="recordname"
          label="文件名"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="creatname"
          label="导入人"
          align="center"
          width="120"
        />
        <el-table-column prop="status" label="状态" align="center" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isresult == '1' ? 'success' : 'danger'">
              {{ row.isresult == '1' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="220">
          <template #default="{ row }">
            <el-button type="text" @click="viewDetail(row)">详情</el-button>
            <el-button type="text" @click="deleteRecord(row)">
              清除导入数据
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        style="margin-top: 20px; text-align: right"
      />
    </div>

    <!-- 错误详情对话框 -->
    <el-dialog
      title="导入结果"
      :visible.sync="detailVisible"
      width="600px"
      append-to-body
      @close="handleCloseDetail"
    >
      <div v-if="result">
        <pre>{{ result }}</pre>
      </div>
      <div v-else class="no-data">暂无错误详情</div>
    </el-dialog>
  </el-dialog>
</template>

<script>
  import {
    getImportRecord,
    getImportRecordDetail,
    clearImportData,
  } from '@/api/workbench/accountManage'

  export default {
    name: 'ImportRecordModal',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      tableId: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          creatname: '',
        },
        detailVisible: false,
        errorDetails: [],
        result: '',
      }
    },
    watch: {
      visible(val) {
        if (val) {
          this.fetchData()
        }
      },
    },
    methods: {
      async fetchData() {
        this.listLoading = true

        const params = {
          ...this.queryForm,
          tableId: this.tableId,
        }

        const res = await getImportRecord(params)
        if (res && res.code == 1) {
          this.listLoading = false
          this.list = res.data.records || []
          this.total = res.data.total || 0
        }
      },

      resetSearch() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          creatname: '',
        }
        this.fetchData()
      },

      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.queryForm.pageNumber = 1
        this.fetchData()
      },

      viewDetail(row) {
        this.detailVisible = true
        getImportRecordDetail({
          tableId: this.tableId,
          importId: row.importId,
        }).then((res) => {
          if (res && res.code == 1) {
            this.result = res.data.recordmemo
          }
        })
      },

      handleCloseDetail() {
        this.result = ''
        this.detailVisible = false
      },

      handleClose() {
        this.$emit('update:visible', false)
        this.$emit('close')
      },

      deleteRecord(row) {
        clearImportData({
          tableId: this.tableId,
          importId: row.importId,
        }).then((res) => {
          if (res && res.code == 1) {
            this.$message.success('清除成功')
            this.fetchData()
          }
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  .record-container {
    .search-section {
      margin-bottom: 20px;
    }

    .no-data {
      text-align: center;
      color: #999;
      padding: 40px 0;
    }
  }

  // pre标签自动换行样式
  pre {
    white-space: pre-wrap; // 保留空格和换行，允许自动换行
    word-wrap: break-word; // 长单词换行
    word-break: break-all; // 允许在任意字符间换行
    overflow-wrap: break-word; // 现代浏览器的换行属性
    max-width: 100%; // 最大宽度限制
    margin: 0; // 重置默认边距
    padding: 10px; // 添加内边距
    background-color: #f5f5f5; // 背景色
    border-radius: 4px; // 圆角
    font-family: 'Courier New', monospace; // 等宽字体
    font-size: 12px; // 字体大小
    line-height: 1.4; // 行高
  }
</style>
