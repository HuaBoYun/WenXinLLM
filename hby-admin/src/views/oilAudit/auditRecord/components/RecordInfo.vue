<template>
  <el-drawer
    :before-close="close"
    size="80%"
    :title="title"
    :visible.sync="dialogFormVisible"
    v-if="dialogFormVisible"
  >
    <Look :projectId="projectId" />
  </el-drawer>
</template>

<script>
  import Look from '@/views/oilAudit/data/look.vue'
  export default {
    name: 'RecordInfo',
    components: { Look },
    data() {
      return {
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        projectId: -1,
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        title: '项目情况一览',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
        showForm: true,
      }
    },
    created() {},
    methods: {
      async showEdit(row) {
        this.dialogFormVisible = true
        this.projectId = row.id
        console.log('🚀 ~ showEdit ~ this.projectId :', this.projectId)
      },
      close() {
        this.dialogFormVisible = false
      },
      getChildParam(data) {
        this.title = data
        if (data === 2.1) {
          this.title = '指引模板节点一'
          this.showForm = false
        } else if (data === 1) {
          this.showForm = true
          this.title = '项目查看'
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
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
