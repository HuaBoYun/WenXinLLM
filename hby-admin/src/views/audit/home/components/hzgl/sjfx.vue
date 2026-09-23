<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="审计发现"
        prop="auditDiscoverable"
        width="100"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.auditDiscoverable }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="被审计对象"
        show-overflow-tooltip
        prop="orgidnames"
      ></el-table-column>

      <el-table-column
        align="center"
        label="发现人"
        prop="realname"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="审计事项"
        prop="businessaffiliation"
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <DraftManageInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    discoverDelete,
    discoverList,
    discoverStatus,
    draftManageDelete,
    whetherLeader,
  } from '@/api/audit/implement'
  import DraftManageInfo from '@/views/audit/implement/components/myDraftInfo'
  import ExecutorOptions from '@/views/audit/implement/components/options/executor.vue'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'

  export default {
    name: 'Download',
    components: { LcdyEdit, ExecutorOptions, DraftManageInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          findPeople: '',
          realname: '',
          recStatus: '',
          status: '',
          businessAffiliation: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        cloudEdit: false,
      }
    },
    async created() {
      this.fetchData()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.cloudEdit = true
      } else {
        this.cloudEdit = false
      }
    },
    methods: {
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.findPeople = node.staffid
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
              project,
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await discoverList(this.queryForm) // 发起请求

          this.projectInfo = project || {} // 如果 project 为 null 或 undefined，设置为空对象
          this.list = list || [] // 如果 list 为 null 或 undefined，设置为空数组
          this.total = total || 0 // 如果 total 为 null 或 undefined，设置为 0
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
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await discoverDelete({
            questionid: row.questionId,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleStatus(row) {
        this.$baseConfirm('你确定要发起整改吗', null, async () => {
          const { msg, code } = await discoverStatus({
            questionid: row.questionId,
          })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
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
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await draftManageDelete({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
    },
  }
</script>
