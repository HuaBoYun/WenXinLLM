<!--
 * @Date: 2022-05-05 16:37:34
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-05 17:53:08
 * @FilePath: /hb-admin/src/views/setting/system/components/executor.vue
-->
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
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
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
  import { getExecutorOptionsList, getTreeLevel } from '@/api/setting/system'
  export default {
    name: 'ExecutorOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'text',
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
      async getExecutorTree() {
        const res = await getTreeLevel(this.queryForm)
        this.data = [res]
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getExecutorOptionsList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
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
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
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
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
