<template>
  <el-dialog
    :close-on-click-modal="false"
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @row-click="handleSelected"
    >
      <el-table-column align="center" label="一审法院" prop="courtfirst" />
      <el-table-column align="center" label="仲裁受理日期" prop="asdealdate" />
      <el-table-column
        align="center"
        label="仲裁首次开庭日期"
        prop="asfirsthearingdate"
      />
      <el-table-column
        align="center"
        label="仲裁结案日期"
        prop="arbitrationenddate"
      />
      <el-table-column
        align="center"
        label="仲裁结果"
        prop="arbitrationresult"
      />
      <el-table-column
        align="center"
        label="仲裁金额"
        prop="arbitrationamount"
      />
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
  </el-dialog>
</template>
<script>
  import { getArbitratSettlementInfoList } from '@/api/fwgl/legal'
  export default {
    name: 'GlzcOptions',
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          flowid: '698874',
          pageNumber: 1,
          pageSize: 10,
        },
      }
    },
    created() {},
    methods: {
      /**
       * @description: 打开弹窗
       * @return {*}
       */      
      show() {
        this.dialogFormVisible = true
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await getArbitratSettlementInfoList(this.queryForm)

        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getXdf()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getXdf()
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleSelected(val) {
        this.$emit('selected', val)
        this.dialogFormVisible = false
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {},
    },
  }
</script>
