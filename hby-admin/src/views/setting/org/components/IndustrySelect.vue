<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" />
        <el-table-column align="center" label="行业名称" prop="orgname" />
      </el-table>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { authorizeDel, getAuthorizeDelList } from '@/api/setting/org'
  export default {
    name: 'IndustryListSelect',
    data() {
      return {
        title: '行业取消授权',
        listLoading: false,
        dialogFormVisible: false,
        orgid: undefined,
        companyid: undefined,
        multipleSelection: [],
        list: [],
      }
    },
    created() {},
    methods: {
      show(orgid) {
        this.dialogFormVisible = true
        this.orgid = orgid
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const { code, data } = await getAuthorizeDelList({
          hyid: this.orgid,
        })
        this.listLoading = false
        if (code == 1) {
          this.list = data
        }
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      async save() {
        const ids = this.multipleSelection.map((item) => item.orgid)
        const { msg, code } = await authorizeDel({
          hyid: this.orgid,
          orgids: ids,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
      },
    },
  }
</script>
