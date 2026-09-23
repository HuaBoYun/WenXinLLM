<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LookTree @getChildParam="getChildParam" :projectid="projectId" />
      </div>
      <div class="right">
        <newRightTable
          :targetId="currentTargetId"
          :projectId="projectId"
          :nodeName="nodeName"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import { getList } from '@/oapi/systemLog'
  import { doDelete } from '@/oapi/table'

  export default {
    name: 'look',
    components: {
      newRightTable: () => import('./components/newRightTable.vue'),
      LookTree: () => import('./components/lookTree'),
    },
    props: {
      projectId: {
        type: Number,
        default: undefined,
      },
    },
    data() {
      return {
        nodeName: '',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        currentId: '',
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        currentTargetId: 0,
        showForm: true,
      }
    },
    watch: {
      projectId() {
        this.nodeName = ''
      },
    },
    created() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(type, row) {
        this.$refs[type].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      getChildParam(data) {
        console.log(data)
        this.nodeName = data.data.name
        if (data.projectId !== 0 && data.projectId !== -1) {
          this.currentTargetId = data.projectId
        } else {
          this.currentTargetId = 0
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
    min-width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
</style>
