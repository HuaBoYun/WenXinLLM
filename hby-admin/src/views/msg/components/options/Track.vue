<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="24">
      <el-col :span="24">
        <img
          alt="审批图"
          :src="imgurl"
          style="margin-bottom: 20px; width: 100%"
        />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import { gzlct } from '@/api/setting/msg'

  export default {
    name: 'DraftEdit',
    data() {
      return {
        listLoading: true,
        title: '流程图',
        dialogFormVisible: false,
        imgurl: '',
      }
    },
    created() {},
    methods: {
      async fetchInfo(row) {
        this.listLoading = true

        if (
          row.cyurl.indexOf('contract') != -1 ||
          row.cyurl.indexOf('cyhw') != -1
        ) {
          const { url } = await gzlct({
            cyid: row.cyid,
          })
          this.imgurl = url
        } else if (
          row.cyurl.indexOf('nbsj') != -1 ||
          row.cyurl.indexOf('audit') != -1
        ) {
          this.imgurl =
            baseURL + `/audit/nbsjapproval/picture?taskId=` + row.taskid
        }

        this.listLoading = false
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.fetchInfo(row)
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
