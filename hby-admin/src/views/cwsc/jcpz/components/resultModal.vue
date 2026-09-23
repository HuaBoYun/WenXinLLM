<template>
  <div>
    <el-dialog
      :visible.sync="visible"
      title="执行结果"
      width="80%"
      :close-on-click-modal="false"
      @close="close"
      :append-to-body="true"
    >
      <div class="table-container">
        <el-table :data="tableData" style="width: 100%" border height="400">
          <el-table-column
            v-for="(col, index) in columns"
            :key="index"
            :prop="col"
            :label="col"
            min-width="100"
          />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        visible: false,
        columns: [],
        tableData: [],
      }
    },
    methods: {
      show(result) {
        // 设置列名
        this.columns = result.colName

        // 转换数据格式
        this.tableData = result.dataList.map((row) => {
          const obj = {}
          this.columns.forEach((col, index) => {
            obj[col] = row[index]
          })
          return obj
        })

        this.visible = true
      },
      close() {
        this.visible = false
        this.columns = []
        this.tableData = []
      },
    },
  }
</script>

<style scoped>
  .el-dialog {
    margin-top: 8vh !important;
  }
  .table-container {
    height: 400px;
    overflow-y: hidden;
    overflow-x: auto; /* 允许横向滚动 */
    width: 100%;
  }
  .el-table {
    height: 100%;
  }
</style>
