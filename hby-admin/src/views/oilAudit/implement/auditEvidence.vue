<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.projectName"
              clearable
              placeholder="项目名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.auditMatter"
              clearable
              placeholder="审计事项"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.auditAbstract"
              clearable
              placeholder="审计事项摘要"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="项目名称" prop="projectName">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.projectName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="审计事项" prop="auditMatter" />
      <el-table-column
        align="center"
        label="审计事项摘要"
        prop="auditAbstract"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="证据提供单位意见"
        prop="evidenceOpinion"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="证据提供者"
        prop="certificateUser"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计日期"
        prop="createDate"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == 1
              ? '审批中'
              : row.status == 2
              ? '已退回'
              : row.status == 3
              ? '已撤回'
              : row.status == 4
              ? '已终止'
              : row.status == 5
              ? '已跟踪'
              : row.status == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="240px">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleShenPi(row)"
            :disabled="row.status"
            v-if="id == row.auditUserId"
          >
            提交审批
          </el-button>
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="row.status != 0"
            v-if="id == row.auditUserId"
          >
            修改
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="row.status"
            v-if="id == row.auditUserId"
          >
            删除
          </el-button>
          <el-button type="text" @click="handleExport(row)">导出</el-button>
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
    <DoubtfulInfo
      ref="edit"
      @fetch-data="fetchData"
      :effectDetail="effectDetail"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    createImPlementDetail,
    deleteImPlementOrder,
    getImPlementOrder,
    imPlementOrderDetail,
    qZDExport,
    sendDefect,
    sendDoubtful,
    sendManuscript,
    sendManuscriptGzdg,
    sendRisk,
  } from '@/oapi/audit/implement'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import DoubtfulInfo from '@/views/oilAudit/implement/components/auditEvidenceInfo'

  export default {
    name: 'Download',
    components: {
      DoubtfulInfo,
      ProcessList,
    },
    data() {
      return {
        flawStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          auditMatter: '',
          auditAbstract: '',
          pageNumber: 1,
          pageSize: 20,
        },
        token: store.getters['user/token'],
        effectDetail: {},
        id: '',
      }
    },
    created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
      this.id = JSON.parse(localStorage.getItem('userInfo')).staffid
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
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
            pageInfo: { tlist, totalRecord },
          },
        } = await getImPlementOrder(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleAdd() {
        this.$refs['edit'].showEdit('add')
        const result = await createImPlementDetail()
        this.effectDetail = result.data.pj
      },
      async handleDetail(row) {
        const data = await imPlementOrderDetail({
          certificateId: row.certificateId,
        })
        this.$refs['edit'].showEdit('detail', data.data.certificate)
      },
      async handleEdit(row) {
        const data = await imPlementOrderDetail({
          certificateId: row.certificateId,
        })
        await this.$refs['edit'].showEdit('edit', data.data.certificate)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteImPlementOrder({
            certificateId: row.certificateId,
          })
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
      async handleExport(row) {
        const data = await qZDExport({ certificateId: row.certificateId })

        let fileName = row.projectName
        let blob = new Blob([data], {
          type: 'application/msword',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleShenPi(row) {
        this.$baseConfirm('你确定要审核当前项吗', null, async () => {
          const tableId = 16
          const fromId = row.certificateId
          this.$refs['process'].save(tableId, fromId)
          // const { msg, code } = await saveDraft({ sheetid: row.sheetId })
          // if (code == 1) {
          //   this.$baseMessage(msg, 'success')
          // }
          // await this.fetchData()
        })
      },
    },
  }
</script>
