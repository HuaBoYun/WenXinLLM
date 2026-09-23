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
  import Look from '@/views/audit/data/look.vue'
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
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit(row) {
        this.dialogFormVisible = true
        this.projectId = row.projectId
        // let res = await getProjectProposalDetail({
        //   projectId: row.projectId,
        // })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
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
