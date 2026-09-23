<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @row-click="handleSelected"
    >
      <el-table-column align="center" label="纠纷主题" prop="disputeitem" />
      <el-table-column align="center" label="案号" prop="proceedno" />
      <el-table-column align="center" label="原告" prop="plaintiff" />
      <el-table-column align="center" label="被告" prop="orgname" />
      <el-table-column align="center" label="诉讼阶段" prop="porceedstage" />
      <el-table-column align="center" label="审理法院" prop="court" />
      <el-table-column align="center" label="诉讼标的物" prop="actionobject" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @row-click="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </el-dialog>
</template>
<script>
  import { RecordInfo } from '@/api/contract/legal'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          idname: 'proceedId',
          textname: 'porceedStage',
          othername: 'court',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogFormVisible = true
        this.getList()
      },
      async getList() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await RecordInfo(this.queryForm)
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
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      //回调
      handleSelected(val) {
        this.$emit('selected', val)
        this.dialogFormVisible = false
      },
      close() {},
    },
  }
</script>
