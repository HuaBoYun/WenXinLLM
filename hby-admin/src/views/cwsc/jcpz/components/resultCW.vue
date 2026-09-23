<template>
  <div>
    <el-dialog
      :visible.sync="visible"
      title="结果"
      width="20%"
      :close-on-click-modal="false"
      @close="close"
      :append-to-body="true"
    >
      <div>{{ result }}</div>
    </el-dialog>
  </div>
</template>

<script>
  import { CaiJiLogResult } from '@/api/cwsc'
  export default {
    data() {
      return {
        visible: false,
        columns: [],
        tableData: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          recordid: '',
          recordname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        result: '',
      }
    },
    methods: {
      show(result) {
        this.queryForm.recordid = result.recordid
        this.fetchData()
        this.visible = true
      },
      fetchData() {
        CaiJiLogResult({
          ...this.queryForm,
        }).then((res) => {
          this.result = res.data.recordmemo
        })
      },
      close() {
        this.visible = false
        this.tableData = []
      },
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          recordname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
    },
  }
</script>

<style scoped></style>
