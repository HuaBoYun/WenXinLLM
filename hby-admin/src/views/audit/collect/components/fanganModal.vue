<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form class="margin-b0">
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
              v-model="queryForm.prjoectName"
              clearable
              placeholder="审计项目名称"
              :style="{ width: '256px' }"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.projectSource"
              clearable
              placeholder="项目来源"
              :style="{ width: '256px' }"
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
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      ref="multipleTable"
      tooltip-effect="dark"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="项目编号" prop="projectCode" />
      <el-table-column align="center" label="项目名称" prop="prjoectName" />
      <el-table-column
        align="center"
        label="项目来源"
        prop="projectSource"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目经理"
        prop="pmStaff.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目目前状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.status == '0'
              ? '未启动'
              : row.status == '1'
              ? '启动'
              : row.status == '2'
              ? '实施 '
              : row.status == '3'
              ? '完成'
              : '归档'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="计划审计时间"
        prop="startDate"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划验收时间"
        prop="endDate"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目实施期间(天)"
        prop="days"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="批复总投资(经费:万元)"
        prop="costs"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审批状态"
        prop="examineType"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.examineType == 1
              ? '审批中'
              : row.examineType == 2
              ? '已退回'
              : row.examineType == 3
              ? '已撤回'
              : row.examineType == 4
              ? '已终止'
              : row.examineType == 5
              ? '已跟踪'
              : row.examineType == 6
              ? '已完成'
              : '未审批'
          }}
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
  </el-dialog>
</template>

<script>
  import { projectList } from '@/api/audit/rectify'
  export default {
    name: 'modal',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        title: '项目编号',
        queryForm: {
          prjoectName: '',
          // status: '',
          projectSource: '',
          pageNumber: 1,
          pageSize: 10,
        },
        list: [],
        current: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        const {
          data: {
            pageInfo: { tlist, totalRecord },
            currProjectId,
          },
        } = await projectList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          prjoectName: '',
          // status: '',
          projectSource: '',
          pageNumber: 1,
          pageSize: 10,
        }
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

      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.list = []
        this.footer = true
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        if (this.current.length == 0) {
          this.$baseMessage(
            '请选择项目编号！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selectList', this.current)
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
