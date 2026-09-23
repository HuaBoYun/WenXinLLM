<template>
  <div class="system-log-container">
    <el-dialog
      title="审计档案"
      :visible.sync="dialogVisible"
      width="80%"
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
                v-model="queryForm.prjoectName"
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
        <vab-query-form-left-panel>
          <span></span>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel>
          <el-button type="success" @click="handleAdd">选择</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="项目名称"
          prop="prjoectName"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="项目经理"
          prop="pmStaff.realname"
        />
        <el-table-column
          align="center"
          label="被审计单位"
          prop="audiOrgInfo.orgname"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.auditStaffName ? row.auditStaffName : row.auditOrgName }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="项目类型"
          prop="auditType"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="项目来源"
          prop="projectSource"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="开始时间"
          prop="startDate"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="实际开始时间"
          prop="assigbedpmTime"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="实际结束时间"
          prop="endDate"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="状态"
          prop="status"
          show-overflow-tooltip
          width="120"
        >
          <template>已归档</template>
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
    </el-dialog>
  </div>
</template>

<script>
  import { listProjectIdSave, planListPlanIdIn } from '@/oapi/audit/preparation'
  import { UTCformat } from '@/utils'

  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        list: [],
        listLoading: true,
        dialogVisible: false,
        projectIdStr: '',
        multipleSelection: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
      },
      async handleAdd() {
        let res = await listProjectIdSave({
          ids: this.projectIdStr,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.dialogVisible = false
          this.$emit('fetch-data')
        }
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
        let arr = []
        this.multipleSelection.forEach((item) => {
          arr.push(item.projectId)
        })
        this.projectIdStr = arr.join(',')
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        // this.listLoading = true
        // const {
        //   data: {
        //     pageInfo: { tlist, totalRecord },
        //   },
        // } = await planListPlanIdIn(this.queryForm)
        // this.list = tlist
        // this.list.forEach((item) => {
        //   item.startDate = UTCformat(item.startDate)
        //   item.assigbedpmTime = UTCformat(item.assigbedpmTime)
        //   item.endDate = UTCformat(item.endDate)
        // })
        // this.total = totalRecord
        // this.listLoading = false
      },
      close() {
        this.dialogVisible = false
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
</style>
