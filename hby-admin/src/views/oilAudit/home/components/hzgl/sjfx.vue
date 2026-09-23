<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="审计发现"
        prop="nbsjSheet.auditDiscoverable"
        width="100"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.nbsjSheet.auditDiscoverable }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="被审计对象"
        show-overflow-tooltip
        prop="nbsjSheet.organization.orgname"
      >
        <template>
          {{
            projectInfo.auditStaffName
              ? projectInfo.auditStaffName
              : projectInfo.auditOrgName
          }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="发现人"
        prop="nbsjSheet.createStaff.realname"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="是否整改"
        prop="recStatus"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ row.recStatus === 1 ? '是' : '否' }}
        </template>
      </el-table-column>
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
  } from '@/oapi/audit/implement'
  import DraftManageInfo from '@/views/oilAudit/implement/components/myDraftInfo'
  import ExecutorOptions from '@/views/oilAudit/implement/components/options/executor.vue'
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
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await discoverList(this.queryForm)
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
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
      async handleDetail(row) {
        const data = await draftManageDelete({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
    },
  }
</script>
