<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="700px"
    @close="close"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="作业名称 "
        prop="jobName "
        width="80%"
      />
      <el-table-column align="center" label="状态" prop="status" width="80%" />
      <el-table-column align="center" label="日志" prop="logfield">
        <template #default="{ row }">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 5 }"
            v-model="row.logField"
            readonly
          />
        </template>
      </el-table-column>
    </el-table>
    <div slot="footer" v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <!-- <el-button @click="save" type="primary">保 存</el-button>
      <el-button
        @click="test"
        type="primary"
        v-if="formData.libraryType == '数据库资源库'"
      >
        测试连接
      </el-button> -->
    </div>
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { jobLog } from '@/api/sjzt/etl/etl'
  import { formatDate } from '@/utils'
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        listLoading: false,
        list: [],
        baseURL: baseURL,
        headers: { token: token },
        tableData: [],
        formData: {
          jobLogLevel: 'basic',
          jobType: 'File',
          jobPath: '/',
          jobRepositoryId: '',
          creator: '',
          staffid: '',
          belongGroupName: '',
          orgid: '',
          createdTime: '',
        },
        footer: true,
        dialogFormVisible: false,
        title: '查看日志',
      }
    },
    computed: {},
    watch: {},
    created() {
      this.getList()
    },
    mounted() {},
    methods: {
      changeCron(val) {
        this.formData.cron = val
      },
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      handleDown(row) {},
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      async showEdit(row) {
        const queryForm = {
          jobsname: row.jobName,
          pageNum: 1,
          pageSize: 10,
        }
        jobLog(queryForm).then((res) => {})
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {}
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.loading = true
        this.loading = false
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
