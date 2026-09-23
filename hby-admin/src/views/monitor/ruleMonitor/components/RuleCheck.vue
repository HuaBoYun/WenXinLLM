<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div>
      <!-- <div style="text-align: right; margin-bottom: 20px">
        <el-button type="success" @click="handleExportDataBase">导出</el-button>
      </div> -->
      <el-table v-loading="listLoading" :data="list">
        <div v-for="item in this.renderTitle" :key="item">
          <el-table-column
            align="center"
            :label="item"
            :prop="item"
            :key="item"
          />
        </div>
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </el-dialog>
</template>
<script>
  import { getSQLList, exportDataBase } from '@/api/monitor/rule/index'
  export default {
    name: 'ExecutorOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        renderTitle: [],
        sql: '',
      }
    },
    created() {},
    methods: {
      show(info) {
        console.log(info)
        this.current = undefined
        let arr = Object.keys(info.tlist[0])
        arr.splice(arr.indexOf('RN'), 1)
        arr.splice(arr.indexOf('EXECTIME'), 1)
        arr.splice(arr.indexOf('ID'), 1)
        console.log(arr)
        this.renderTitle = arr
        this.list = info.tlist
        this.total = info.totalRecord
        this.dialogFormVisible = true
      },
      async getExecutorList(info, id) {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getSQLList({ sql: info, ...this.queryForm, bookid: id })
        let arr = Object.keys(tlist[0])
        arr.splice(arr.indexOf('RN'), 1)
        this.renderTitle = arr
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList(this.sql, this.bookid)
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList(this.sql, this.bookid)
      },

      close() {
        this.dialogFormVisible = false
      },
      async handleExportDataBase() {
        const data = await exportDataBase({
          sql: this.sql,
          bookid: this.bookid,
        })
        let fileName = '审计模型sql查询结果'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
