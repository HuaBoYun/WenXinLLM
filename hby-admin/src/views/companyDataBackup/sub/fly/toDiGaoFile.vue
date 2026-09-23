<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
    :close-on-click-modal="false"
  >
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
              v-model="queryForm.sheetcode"
              clearable
              placeholder="底稿编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.sheetname"
              clearable
              placeholder="底稿名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.realname"
              clearable
              placeholder="拟稿人"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="审核状态">
              <el-option label="未复核" value="1" />
              <el-option label="复核中" value="2" />
              <el-option label="复核终止" value="3" />
              <el-option label="复核通过" value="4" />
              <el-option label="需调整" value="5" />
            </el-select>
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
        <el-button type="success" @click="handleTo()">发送</el-button>
        <!-- <el-button type="success" @click="handleExport()">导出</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      @row-click="singleElection"
      ref="multipleTable"
    >
      <el-table-column align="center" width="55" label="">
        <template slot-scope="scope">
          <!-- 可以手动的修改label的值，从而控制选择哪一项 -->
          <el-radio
            class="radio"
            v-model="templateSelection"
            :label="scope.row.sheetId"
          >
            &nbsp;
          </el-radio>
        </template>
      </el-table-column>
      <el-table-column align="center" label="底稿编号" prop="sheetCode">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.sheetCode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="底稿名称"
        prop="sheetName"
        show-overflow-tooltip
      />
      <!-- <el-table-column
        align="center"
        label="审计目标"
        prop="sheetTarget"
        show-overflow-tooltip
      /> -->
      <el-table-column
        align="center"
        label="被审计对象"
        prop="targetName"
        show-overflow-tooltip
      >
        <template>
          {{
            projectInfo.auditStaffName
              ? projectInfo.auditStaffName
              : projectInfo.orgIdNames
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="拟稿人"
        prop="realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审批人"
        prop="approver"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.yjfh + ',' + row.ejfh }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="拟稿日期"
        prop="createTime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="state"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.state == 1
              ? '审批中'
              : row.state == 2
              ? '已退回'
              : row.state == 3
              ? '已通过'
              : row.state == 4
              ? '已终止'
              : row.state == 5
              ? '已跟踪'
              : row.state == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <DraftManageInfo ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
  import {
    draftManageDelete,
    draftManageList,
    myDraftExport,
    myDraftDetail,
    newMyDraftSave,
  } from '@/api/audit/implement'
  import { doDelete } from '@/api/table'
  import { formatDate } from '@/utils/index'
  import DraftManageInfo from '@/views/audit/implement/components/myDraftInfo'
  import ExecutorOptions from '@/views/audit/implement/components/options/executor.vue'

  export default {
    name: 'Download',
    components: { DraftManageInfo, ExecutorOptions },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sheetname: '',
          sheetcode: '',
          realname: '',
          status: '',
          staffid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        dialogFormVisible: false,
        select: [],
        templateSelection: '',
        fileInfo: {},
      }
    },
    created() {},
    methods: {
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.staffid = node.staffid
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        } = await draftManageList(this.queryForm)
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      async handleDetail(row) {
        const data = await draftManageDelete({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
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
      async handleExport() {
        const data = await myDraftExport({ type: 1 })
        let fileName = '底稿管理'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
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
      close() {
        this.dialogFormVisible = false
      },
      showEdit(fileInfo) {
        this.fileInfo = fileInfo //附件信息
        this.dialogFormVisible = true
        this.fetchData()
      },
      async handleTo() {
        if (!this.templateSelection) {
          this.$message({
            message: '请选择底稿',
            type: 'error',
          })
          return false
        }
        const data = await myDraftDetail({ sheetid: this.templateSelection })
        if (data.code == 1) {
          let params = {
            attid: this.fileInfo.attid,
            sheetid: data.data.sheet.sheetId,
          }
          const res = await newMyDraftSave(params)
          if (res.code == 1) {
            this.dialogFormVisible = false
          } else {
            this.$message({
              message: res.msg,
              type: 'error',
            })
          }
        }
      },
      //单选
      singleElection(row) {
        this.templateSelection = row.sheetId
      },
    },
  }
</script>
