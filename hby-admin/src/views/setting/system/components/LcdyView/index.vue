<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-29 10:23:54
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyView/index.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogTableVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button
          type="success"
          @click="
            $refs['edit'].showEdit(processSetting, queryForm.definitionName)
          "
        >
          添加流程
        </el-button>
        <!-- <el-button type="danger" @click="handleDelete">删除</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" /> -->
      <el-table-column
        align="center"
        label="流程定义Id"
        prop="id"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="流程定义名称"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="流程定义Key" prop="key" />
      <el-table-column align="center" label="流程定义版本" prop="version" />
      <el-table-column
        align="center"
        label="资源名称bpmn文件"
        prop="resourceName"
        show-overflow-tooltip
        width="150"
      />
      <el-table-column align="center" label="部署对象ID" prop="deploymentId" />
      <el-table-column
        align="center"
        label="资源名称png文件"
        prop="DiagramResourceName"
        show-overflow-tooltip
        width="150"
      />
      <el-table-column align="center" label="是否挂起">
        <template #default="{ row }">
          {{ row.suspended ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="200"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleInstance(row)">
            流程实例
          </el-button>
          <el-button type="text" @click="handleHistory(row)">
            历史流程
          </el-button>
          <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
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
    <FlowEdit ref="edit" @fetch-data="fetchData" />
    <FlowInstance ref="instance" />
    <FlowHistory ref="history" />
  </el-dialog>
</template>

<script>
  import { deleteFlowForView, getFlowListForView } from '@/api/setting/system'
  import FlowEdit from './FlowEdit.vue'
  import FlowHistory from './FlowHistory.vue'
  import FlowInstance from './FLowInstance.vue'

  export default {
    name: 'LcdyView',
    components: { FlowEdit, FlowHistory, FlowInstance },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '流程定义',
        dialogTableVisible: false,
        queryForm: {
          definitionName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        processSetting: undefined,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      show(row) {
        this.queryForm.definitionName = row.module
        this.fetchData()
        this.dialogTableVisible = true
      },
      close() {
        this.dialogTableVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
            processSetting,
          },
        } = await getFlowListForView(this.queryForm)
        this.processSetting = processSetting
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleInstance(row) {
        this.$refs['instance'].show(row, this.processSetting)
      },
      handleHistory(row) {
        this.$refs['history'].show(row, this.processSetting)
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleDelete() {
        if (!this.multipleSelection.length) {
          this.$baseMessage('请选择流程', 'error', 'vab-hey-message-error')
          return
        }
        const ids = this.multipleSelection.map((item) => item.id)

        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code, msg } = await deleteFlowForView({
            definitionName: this.queryForm.definitionName,
            ids,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
    },
  }
</script>
