<template>
  <el-dialog
    title="审计意见及决定书"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-top-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input v-model="queryForm.title" clearable placeholder="编号" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.xmname" clearable placeholder="名称" />
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
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column align="center" label="序号" type="index" /> -->
      <el-table-column align="center" label="序号" type="index" />
      <el-table-column align="center" label="编号" prop="code">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.code }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="名称" prop="bgname" />
      <el-table-column align="center" label="编制人" prop="createname" />
      <el-table-column align="center" label="编制时间" prop="createdate" />
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
    <!-- <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
    <sjjysView ref="table6View" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { jhgljhcgList, detailJhCg } from '@/api/monitor/question'
  import { sjjysList, deleteSjjys } from '@/oapi/audit/report'
  import sjjysView from './sjjysView.vue'
  import { getNoticeList } from '@/oapi/audit/preparation'
  export default {
    components: {
      sjjysView,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          document: '',
          title: '',
          pageNumber: 1,
          pageSize: 20,
          checkRight: 1,
        },
      }
    },
    methods: {
      handleDetail(row) {
        this.$refs['table6View'].showEdit(row, 'detail')
      },
      async showEdit(row) {
        this.fetchData()
        this.dialogVisible = true
      },
      resetQueryForm() {
        this.queryForm = {
          document: '',
          title: '',
          pageNumber: 1,
          pageSize: 20,
          checkRight: 1,
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
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await sjjysList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async save() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择一条数据')
          return
        }
        this.$emit('seTtable', this.multipleSelection)
        this.dialogVisible = false
      },
      close() {
        this.resetQueryForm()
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
