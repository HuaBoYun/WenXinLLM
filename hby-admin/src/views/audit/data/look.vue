<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <!-- <project-situation-list @getChildParam="getChildParam" /> -->
        <!-- <task-tree @getChildParam="getChildParam" /> -->
        <LookTree @getChildParam="getChildParam" :projectId="projectId" />
      </div>
      <div class="right">
        <RightTable
          :nodeName="nodeName"
          :projectId="projectId"
          :targetId="currentTagetId"
        />
        <!-- <project-view /> -->
        <!-- <record-list-view v-else /> -->
      </div>
    </div>
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import RightTable from './components/rightTable.vue'
  // import ProjectSituationList from '@/views/audit/plan/components/ProjectSituationList'
  // import ProjectView from '@/views/audit/plan/components/ProjectView'
  import LookTree from './components/lookTree'
  // import RecordListView from '@/views/audit/plan/components/RecordListView'

  export default {
    name: 'Download',
    components: {
      RightTable,
      // ProjectSituationList,
      // TaskTree,
      LookTree,
      // ProjectView,
      // RecordListView,
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
        currentTagetId: 0,
        showForm: true,
      }
    },
    watch: {
      projectId() {
        this.nodeName = ''
      },
    },
    created() {
      // this.fetchData()
    },
    methods: {
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(type, row) {
        this.$refs[type].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
        this.title = data
        this.nodeName = data.topName ? data.topName : data.name
        if (data.id !== 0 && data.id !== -1) {
          this.currentTagetId = data.id
        } else {
          this.currentTagetId = 0
        }
        // if (data === 2.1) {
        //   this.title = '指引模板节点一'
        //   this.showForm = false
        // } else if (data === 1) {
        //   this.showForm = true
        //   this.title = '项目查看'
        // }
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
    width: 85%;
  }
</style>
