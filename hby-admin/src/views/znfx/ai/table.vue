<template>
  <div class="table_content">
    <el-dialog :visible.sync="dialogVisible" :before-close="handleClose" width="60vw" title="数据详情">
      <el-radio-group size="small" v-model="tabPosition" @input="radioChange" style="margin-bottom: 30px;">
        <el-radio-button v-for="(i,index) in tableList" :key="index" :label="index">{{ i.customTable }}</el-radio-button>
      </el-radio-group>
      <el-table
        :data="tableData"
        v-loading="loading"
        max-height="430px"
        size="small"
        style="width: 100%">
        <el-table-column
          v-for="(item,index) in tableList[tabPosition].settings"
          :key="index"
          :prop="item.mappingColumn"
          :label="item.columnName">
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination_style"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="currentSize"
        :layout="layout"
        :total="total">
      </el-pagination>
    </el-dialog>
  </div>
</template>

<script>
import { getTableList, getTableData } from '@/api/ai/index.js'
export default {
  data(){
    return{
      tabPosition:'',
      tableData:[],
      currentPage:1,
      currentSize: 10,
      total: 0,
      layout: 'total, sizes, prev, pager, next, jumper',
      dialogVisible: false,
      tableList: [],
      loading: false
    }
  },
  methods:{
    radioChange(value){
      this.tabPosition = value
      this.tableData = []
      this.total = 0
      this.getTableInfo()
    },
    handleSizeChange(val) {
      this.currentSize = val
      this.getTableInfo()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getTableInfo()
    },
    getTableInfo() {
      this.loading = true
      getTableData({tableId: this.tableList[this.tabPosition].tableId, pageNo: this.currentPage, pageSize: this.currentSize}).then(res => {
        console.log(res)
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    open(id){
      getTableList({batchId: id}).then(res => {
        console.log(res)
        this.tableList = res.data
        this.radioChange(0)
        this.dialogVisible = true
      })
    },
    handleClose(){
      this.dialogVisible = false
    },
  }
}
</script>

<style scoped lang="scss">
.pagination_style{
  margin-top: 16px;
  text-align: right;
}
.el-table__header{
    background: rgb(247, 247, 249);
    color: rgb(92, 95, 102);
    font-weight: 400;
}
.el-table{
  color: rgb(21, 27, 38);
  font-size: 12px;
}
.el-radio-group .is-active{
  //border: 1px solid #1677ff!important;
}
:deep(.el-dialog__body) {
  padding-top: 0;
}
:deep(.el-radio-group) {
  margin-bottom: 10px!important;
}
</style>
