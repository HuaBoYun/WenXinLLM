<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.code"
              clearable
              placeholder="问题编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.audiorgName"
              style="width: 300px"
              clearable
              placeholder="被审计单位"
            />
          </el-form-item>
          <el-button
            @click="handleObject"
            style="margin-left: 10px; margin-right: 10px"
            type="primary"
            :disabled="disabled"
            size="small"
          >
            选择
          </el-button>
          <el-form-item>
            <el-input
              v-model="queryForm.projectname"
              clearable
              placeholder="项目名称"
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
      </vab-query-form-left-panel>
      <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="问题编号" prop="code" width="100">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.code }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="被审计单位" prop="company" />
      <el-table-column
        align="center"
        label="问题详情"
        prop="details"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="问题来源" prop="source" />
      <el-table-column align="center" label="发现人" prop="discoverer" />
      <el-table-column align="center" label="整改执行人" prop="zgzxxrname" />
      <el-table-column
        align="center"
        label="状态"
        prop="runstatus"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.runstatus == '0'
              ? '未分派'
              : row.runstatus == '1'
              ? '开始整改'
              : row.runstatus == '2'
              ? '整改中'
              : row.runstatus == '3'
              ? '方案已结束'
              : '整改完成'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="solution(row)">跟踪</el-button>
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
    <unregisteredProblemForm ref="unregisteredProble" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </div>
</template>

<script>
  import {
    getwxhContentsList,
    getzgjgReformByid,
    wxhXfSolutionRy,
  } from '@/oapi/audit/rectify'
  import { doDelete } from '@/oapi/table'
  import { formatDate } from '@/utils/index'
  import unregisteredProblemForm from './components/form/unregisteredProblemForm'
  import SelectDepartment from './components/selectDepartment.vue'

  export default {
    name: 'UnregisteredProblem',
    components: { unregisteredProblemForm, SelectDepartment },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          projectname: '',
          audiorgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getwxhContentsList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(row) {
        const data = await getzgjgReformByid({ reformid: row.reformid })
        await this.$refs['unregisteredProble'].showEdit(data.data)
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      solution(row) {
        this.$baseConfirm('你确定要跟踪当前项吗', null, async () => {
          const { msg } = await wxhXfSolutionRy({ reformid: row.reformid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.$set(this.queryForm, 'audiorgName', val.name)
        this.$set(this.queryForm, 'audiorgid', val.id)
      },
    },
  }
</script>
