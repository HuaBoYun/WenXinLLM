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
        <el-button type="success" @click="handleExport()">导出</el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="底稿编号"
        prop="sheetCode"
        width="170"
      >
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
        prop="orgname"
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
      <!-- <el-table-column
        align="center"
        label="审批人"
        prop="approver"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          {{ row.yjfh && row.ejfh ? row.yjfh + ',' + row.ejfh : '' }}
        </template>
      </el-table-column> -->
      <el-table-column
        align="center"
        label="拟稿日期"
        prop="createTime"
        show-overflow-tooltip
        :formatter="formatDates"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="state"
        show-overflow-tooltip
      >
        <!-- <template #default="{ row }">
          {{
            row.state == '1'
              ? '未复核'
              : row.state == '2'
              ? '复核中'
              : row.state == '3'
              ? '复核终止'
              : row.state == '4'
              ? '复核通过'
              : '已退回'
          }}
        </template> -->
        <template #default="{ row }">
          {{
            row.state == 1
              ? '审批中'
              : row.state == 2
              ? '已退回'
              : row.state == 3
              ? '已撤回'
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

      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="220"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            :disabled="row.state != 0"
            @click="handleEdit(row)"
          >
            修改
          </el-button>
          <el-button
            :disabled="row.state"
            type="text"
            @click="handleReview(row)"
          >
            提交审批
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="row.state"
          >
            删除
          </el-button>
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
    <MyDraftInfo ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <ProcessList ref="process" />
  </div>
</template>

<script>
  import {
    doDelete,
    myDraftDetail,
    myDraftExport,
    myDraftList,
    saveDraft,
  } from '@/oapi/audit/implement'
  import { formatDay } from '@/utils/index'
  import MyDraftInfo from './components/myDraftInfo'
  import ExecutorOptions from './components/options/executor.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'Download',
    components: { MyDraftInfo, ExecutorOptions, ProcessList },
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
      }
    },
    async created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.staffid = node.staffid
      },
      formatDates(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        } = await myDraftList(this.queryForm)
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await myDraftExport({ ...this.queryForm, ids: ids.join() })
        let fileName = '我的底稿'
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
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('edit', data.data)
      },
      handleReview(row) {
        this.$baseConfirm('你确定要复核当前项吗', null, async () => {
          const tableId = 10
          const fromId = row.sheetId
          this.$refs['process'].save(tableId, fromId)
          // const { msg, code } = await saveDraft({ sheetid: row.sheetId })
          // if (code == 1) {
          //   this.$baseMessage(msg, 'success')
          // }
          // await this.fetchData()
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await doDelete({ sheetid: row.sheetId })
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
