<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div class="">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="项目名称"
          prop="implementationProjectName"
        />
        <el-table-column
          align="center"
          label="申报单位"
          prop="approvalBelongGroupName"
        />
        <el-table-column align="center" label="创建时间" prop="createdTime" />
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
    </div>
  </el-dialog>
</template>
<script>
  import { xmpysbList } from '@/oapi/audit/xmpy'
  export default {
    name: 'ExecutorOptions',
    props: {
      isCheckout: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        data: [],
        queryForm: {
          projectName: undefined,
          pageNumber: 1,
          pageSize: 20,
          state: 6,
          noGroup: 0,
        },
        current: undefined,
        select: [],
        noIds: [],
        filterArr: [], //要过滤掉的数据
      }
    },
    created() {},
    methods: {
      show(info) {
        this.filterArr = info
        this.dialogFormVisible = true
        this.getExecutorList()
      },

      async getExecutorList() {
        const {
          data: { tlist, totalRecord },
        } = await xmpysbList({ ...this.queryForm })
        // 过滤已选中的数据
        const filteredA = tlist.filter(
          (item) => !this.filterArr.map((bItem) => bItem.id).includes(item.id)
        )
        this.list = filteredA
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      confirm() {
        if (!this.select) {
          this.$baseMessage(
            '请选择申报项目！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.select)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          state: 6,
          noGroup: 0,
          pageNumber: 1,
          pageSize: 20,
        }
        this.list = []
        this.select = []
        this.filterArr = []
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
