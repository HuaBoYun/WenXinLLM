<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="项目编号"
        prop="projectCode"
        show-overflow-tooltip
      />

      <el-table-column align="center" label="项目名称" prop="prjoectName">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit('recordInfo', row)">
            {{ row.prjoectName }}
          </el-button>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="项目类别"
        prop="auditType"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="档案年度"
        prop="planYear"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计单位"
        prop="auditOrg"
        show-overflow-tooltip
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
    <RecordInfo ref="recordInfo" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getArchivesList } from '@/api/audit/archives'
  import { doDelete } from '@/api/table'
  import RecordInfo from '@/views/audit/auditRecord/components/RecordInfo.vue'

  export default {
    name: 'List',
    components: { RecordInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectCode: '',
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
          projectYear: '',
          auditType: '',
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetSearch() {
        this.queryForm = {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
          projectYear: '',
          projectCode: '',
          auditType: '',
        }
        this.fetchData()
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getArchivesList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
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
      handleDown(row) {},
    },
  }
</script>
