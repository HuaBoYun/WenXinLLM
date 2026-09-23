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
        <el-table-column align="center" label="项目经理" prop="realname" />
        <el-table-column
          align="center"
          label="被审计单位"
          prop="audiOrgInfo.orgname"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            {{ row.auditStaffName ? row.auditStaffName : row.orgIdNames }}
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
        <!-- <el-table-column
          align="center"
          label="实际开始时间"
          prop="assigbedpmTime"
          show-overflow-tooltip
          width="120"
        /> -->
        <el-table-column
          align="center"
          label="结束时间"
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
  import { listProjectIdSave, planListPlanIdIn } from '@/api/audit/preparation'
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
      /**
       * @description: 组件初始化，调用列表接口
       * @param {*}
       * @return {*}
       */
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
      },
      /**
       * @description:  确定触发，调用接口保存档案，然后关闭组件，回调函数，刷新列表
       * @param {*}
       * @return {*}
       */
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
      /**
       * @description: 重置筛选，把筛选条件清空
       * @param {*}
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description: 重置按钮，点击触发的函数
       * @param {*}
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description:  分页，选择每页几条数据，查询每页多少条数据
       * @param {*}
       * @return {*}
       */
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
       * @description:  分页，选择页码，查询第几页的数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 列表选择数据 触发，把选中的数据存入projectIdStr
       * @param {*}
       * @return {*}
       */
      handleSelectionChange(val) {
        this.multipleSelection = val
        let arr = []
        this.multipleSelection.forEach((item) => {
          arr.push(item.projectId)
        })
        this.projectIdStr = arr.join(',')
      },
      /**
       * @description: 查询按钮，回到第一页，查询数据
       * @param {*}
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 查询接口，条件查询
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await planListPlanIdIn(this.queryForm)
        this.list = tlist
        this.list.forEach((item) => {
          item.startDate = UTCformat(item.startDate)
          item.assigbedpmTime = UTCformat(item.assigbedpmTime)
          item.endDate = UTCformat(item.endDate)
        })
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 关闭函数，关闭组件
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
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
