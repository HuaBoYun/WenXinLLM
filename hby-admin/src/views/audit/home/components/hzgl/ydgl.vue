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
  } from '@/api/audit/implement'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  // import RiskInfo from '@/views/audit/question/components/RiskInfo'
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
        this.listLoading = true // 开始加载
        try {
          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await doubtfulList(this.queryForm) // 发起请求
          this.list = list // 更新列表数据
          this.total = total // 更新总记录数
        } catch (error) {
          console.error('Error fetching data:', error) // 捕获并打印错误
          // 你可以在这里处理错误，比如显示错误提示
        } finally {
          this.listLoading = false // 无论成功或失败，都关闭加载状态
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const data = await doubtfulDetail({ dpointid: row.dpointid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
