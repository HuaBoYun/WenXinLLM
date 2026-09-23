<template>
  <el-dialog
    title="审计通知书"
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
            <el-input
              v-model="queryForm.advicecoed"
              clearable
              placeholder="审计通知书编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.advicename"
              clearable
              placeholder="审计通知书名称"
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
      <el-table-column align="center" label="审计通知书编号" prop="advicecoed">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEditTable5(row, true)"
            style="white-space: pre-line; line-height: 16px"
          >
            {{ row.advicecoed }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="审计通知书名称"
        prop="advicename"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
      >
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
      <!-- <el-table-column
        prop="externalassig"
        label="是否外委"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.externalassig == 0 ? '否' : '是' }}
        </template>
      </el-table-column> -->
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
    <NoticeInfo ref="table5View" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { jhgljhcgList, detailJhCg } from '@/api/monitor/question'
  import NoticeInfo from './NoticeInfo.vue'
  import { getNoticeList } from '@/oapi/audit/preparation'
  export default {
    components: {
      NoticeInfo,
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
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
        },
      }
    },
    methods: {
      handleEditTable5(row, disabled) {
        this.$refs['table5View'].showEdit(row, disabled)
      },
      async showEdit(row) {
        this.fetchData()
        this.dialogVisible = true
      },
      resetQueryForm() {
        this.queryForm = {
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }

        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNoticeList({ ...other, startDate, endDate })
        this.list = tlist
        this.list.forEach((item) => {
          item.creatrtime = UTCformat(item.creatrtime)
        })
        this.total = totalRecord
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
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
