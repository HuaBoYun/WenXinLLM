<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
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
          <vab-query-form-left-panel :span="18">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.realname"
                  placeholder="请输入姓名"
                  clearable
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="getExecutorList"
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
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="6">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column align="center" label="用户真实名" prop="realname" />
          <el-table-column align="center" label="所属部门" prop="orgName" />
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
  </el-dialog>
</template>
<script>
  import {
    getExecutorOptionsList,
    getExecutorOptionsTree,
  } from '@/api/contract/manage'
  export default {
    name: 'ExecutorOptions',
    props: {
      type: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        multipleSelection: [],
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      show() {
        this.current = undefined
        this.dialogFormVisible = true
        this.getExecutorTree()
        this.getExecutorList()
      },
      resetSearch() {
        let queryForm = {
          pid: this.queryForm.pid,
          realname: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.queryForm = queryForm
        this.getExecutorList()
      },
      async getExecutorTree() {
        const res = await getExecutorOptionsTree(this.queryForm)
        this.data = res
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          pageInfo: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getExecutorOptionsList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      confirm() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.multipleSelection)

        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    min-width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
    width: 100%;
  }
</style>
