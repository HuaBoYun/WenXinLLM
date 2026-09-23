<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="疑点编号"
        prop="dpnumber"
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.dpnumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="疑点名称" prop="dpname" />

      <el-table-column
        align="center"
        label="测试结果"
        prop="testresult"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="编制人"
        prop="editor"
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
    <DoubtfulInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    doubtfulDelete,
    doubtfulDetail,
    doubtfulList,
    sendDefect,
    sendDoubtful,
    sendManuscript,
    sendManuscriptGzdg,
    sendRisk,
  } from '@/oapi/audit/implement'
  import DoubtfulInfo from '@/views/oilAudit/implement/components/DoubtfulInfo'
  // import RiskInfo from '@/views/oilAudit/question/components/RiskInfo'
  // import DoubtfulInfo2 from './components/doubtful/DoubtfulInfo'
  // import FlawInfo from './components/doubtful/FlawInfo'
  // import MyDraftInfo from './components/myDraftInfo'

  export default {
    name: 'Download',
    components: {
      DoubtfulInfo,
    },
    data() {
      return {
        flawStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          dpnumber: '',
          dpname: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async sendToManuscript(row) {
        const data = await sendManuscript({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['manuscript'].showEdit('疑点', data.data)
      },
      async sendToManuscriptAtt(row) {
        const data = await sendManuscriptGzdg({
          auditStaff: row.editor,
          // auditedUnit: row.auditedunit,
          selectIds: row.dpointid,
          type: 'nbsj',
        })
      },
      async sendToDoubtful(row) {
        const data = await sendDoubtful({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['doubtful'].showEdit(data.data)
      },
      async sendToDefect(row) {
        this.flawStatus = true

        const data = await sendDefect({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$nextTick(() => {
          this.$refs['flaw'].showEdit(data.data)
        })
      },
      async sendToRisk(row) {
        const data = await sendRisk({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['risk'].showEdit('发送至风险', data.data)
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await doubtfulList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add')
      },
      async handleDetail(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await doubtfulDelete({ dpointid: row.dpointid })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
