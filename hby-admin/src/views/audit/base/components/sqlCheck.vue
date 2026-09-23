<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div>
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="success" @click="handleExportDataBase">导出</el-button>
      </div>
      <el-table v-loading="listLoading" :data="list" v-if="showColor">
        <div v-for="item in this.renderTitle" :key="item">
          <el-table-column
            v-if="item === colorKey || item === '阈值'"
            align="center"
            :label="item"
            :prop="item"
            :key="item"
          >
            <template #default="{ row }">
              <el-tag
                :type="getThresholdType(String(row[item] || ''))"
                :class="getThresholdClass(String(row[item] || ''))"
              >
                {{ row[item] }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            v-else
            align="center"
            :label="item"
            :prop="item"
            :key="item"
            :width="getColumnWidth(item)"
          />
        </div>
      </el-table>
      <el-table v-loading="listLoading" :data="list" v-else>
        <div v-for="item in this.renderTitle" :key="item">
          <el-table-column
            align="center"
            :label="item"
            :prop="item"
            :key="item"
            :width="getColumnWidth(item)"
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
  import { getSQLList, exportDataBase } from '@/api/setting/org'
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
        showColor: false, //展示色块,主要是红绿灯三个字的时候
        colorKey: '',
      }
    },
    created() {},
    methods: {
      getColumnWidth(item) {
        if (item.includes('预警描述')) {
          return '200px'
        }
        return undefined
      },
      show(info, id) {
        this.bookid = id
        this.current = undefined
        this.dialogFormVisible = true
        this.sql = info
        this.getExecutorList(info, id)
      },
      async getExecutorList(info, id) {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getSQLList({ sql: info, ...this.queryForm, bookid: id })
        // 过滤掉空对象
        const filteredList = tlist.filter(item => Object.keys(item).length > 0)
        
        if (filteredList.length === 0) {
          this.list = []
          this.total = 0
          this.listLoading = false
          return
        }
        
        let arr = Object.keys(filteredList[0])
        arr.splice(arr.indexOf('RN'), 1)
        console.log(arr, '111')
        this.renderTitle = arr
        // 检测arr中是否存在红绿灯三个字字段或阈值字段
        if (arr.some((item) => item.includes('红绿灯') || item === '阈值')) {
          this.showColor = true
          this.colorKey = arr.find((item) => item.includes('红绿灯')) || ''
        }
        this.list = filteredList
        this.total = totalRecord
        this.listLoading = false
      },
      tableCellClassName({ row, column }) {
        if (!this.showColor || !this.colorKey) return ''
        if (column && column.property !== this.colorKey) return ''
        const value = String(row[this.colorKey] || '')
        // if (value.includes('红')) return 'cell-red'
        // if (value.includes('黄')) return 'cell-yellow'
        // if (value.includes('绿')) return 'cell-green'
        return ''
      },
      getThresholdType(threshold) {
        if (threshold.includes('红')) return 'danger'
        if (threshold.includes('黄')) return 'warning'
        if (threshold.includes('绿')) return 'success'
        return 'info'
      },
      getThresholdClass(threshold) {
        if (threshold.includes('红')) return 'threshold-red'
        if (threshold.includes('黄')) return 'threshold-yellow'
        if (threshold.includes('绿')) return 'threshold-green'
        return ''
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList(this.sql, this.bookid)
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList(this.sql, this.bookid)
      },

      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
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

  ::v-deep .cell-red {
    background-color: #ff0000 !important;
    color: #ffffff !important;
  }
  ::v-deep .cell-red .cell {
    color: #ffffff !important;
  }
  ::v-deep .cell-yellow {
    background-color: #fff8e1 !important;
  }
  ::v-deep .cell-green {
    background-color: #00ff15 !important;
    color: #000000 !important;
  }
  ::v-deep .cell-green .cell {
    color: #000000 !important;
  }

  .traffic-light-cell {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
  }

  .traffic-light-dot {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    display: inline-block;
  }

  .dot-red {
    background-color: #ff4444;
  }

  .dot-yellow {
    background-color: #ffcc00;
  }

  .dot-green {
    background-color: #44ff44;
  }

  .traffic-light-text {
    font-size: 14px;
    color: #333;
  }
  
  /* 阈值标签样式 */
  .threshold-red {
    background-color: #f56c6c !important;
    border-color: #f56c6c !important;
    color: #fff !important;
  }

  .threshold-yellow {
    background-color: #e6a23c !important;
    border-color: #e6a23c !important;
    color: #fff !important;
  }

  .threshold-green {
    background-color: #67c23a !important;
    border-color: #67c23a !important;
    color: #fff !important;
  }
</style>
