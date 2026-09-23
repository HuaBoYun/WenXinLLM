<template>
  <el-dialog
    :close-on-click-modal="false"
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="880px"
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
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @row-click="handleSelected"
        >
          <el-table-column
            align="center"
            label="用户真实名11"
            prop="realname"
          />
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
  import { pjlxList, pjlxLeftTree } from '@/api/contract/financing'
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
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      /**
       * @description: 打开弹窗
       * @return {*}
       */      
      show() {
        this.dialogFormVisible = true
        this.getExecutorTree()
      },
      async getExecutorTree() {
        const res = await pjlxLeftTree(this.queryForm)
        let data = []
        // data.push(JSON.parse(res.data))
        this.data = res
        this.queryForm.pid = res[0].id
        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          pageInfo: {
            pageInfo: { tlist, totalRecord },
          },
        } = await pjlxList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      /**
       * @description: 节点选择
       * @param {*} val
       * @return {*}
       */      
      handleNodeClick(val) {
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      /**
       * @description: 选择回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleSelected(val) {
        //
        this.$emit('selected', val)
        this.close()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
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
