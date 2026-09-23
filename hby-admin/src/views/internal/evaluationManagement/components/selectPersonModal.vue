<template>
  <el-dialog
    title="评价负责人"
    :visible.sync="dialogVisible"
    width="1400px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="close"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            ref="multipleTable"
            :data="list"
            tooltip-effect="dark"
            @select="handleSelection"
            style="width: 100%"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              label="用户真实名"
              prop="REALNAME"
            ></el-table-column>
            <el-table-column prop="ORGNAME" label="所属部门"></el-table-column>
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
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { userList } from '@/api/internal/project'
  import { zgjkLeft } from '@/api/setting/org'
  export default {
    data() {
      return {
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorTree()
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      save() {
        if (!this.current) {
          this.$baseMessage(
            '请选择评价负责人',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selectP', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      async getExecutorTree() {
        const res = await zgjkLeft(this.queryForm)
        this.dataTree = res
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await userList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      close() {
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
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
