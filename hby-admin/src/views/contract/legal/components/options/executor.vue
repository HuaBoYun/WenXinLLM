<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="880px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
          :expand-on-click-node="false"
          highlight-current
          node-key="id"
          :props="defaultProps"
          :load="fetchData"
          lazy
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
  import { pjlxLeftTree, pjlxList } from '@/api/contract/financing'
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
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
          label: 'label',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [],
        queryForm: {
          pid: 1,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogFormVisible = true
        // this.getExecutorTree()
        this.fetchData()
        this.getExecutorList()
      },
      fetchData(node, resolve, orgId) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganizationByTreeAllss({ fatherorgid: !node ? '' : node.data.id })
          .then((res) => {
            const tree = this.formatTree(res.data)
            if (node && node.level > 0) {
              resolve(tree[0].children)
              return
            }
            if (!node) {
              this.$emit('select', {
                id: tree[0].id,
                label: tree[0].name,
              })
              // this.expandedKeys.push(this.currentOrg.id)

              this.data = tree
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      //请求树结构
      async getExecutorTree() {
        const res = await pjlxLeftTree(this.queryForm)
        this.data = res
      },
      //请求数据
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
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            isLeaf: !i.isParent,
            children: this.formatTree(i.children || []),
          }
        })
        return list
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
      //回调
      handleNodeClick(val) {
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      //回调
      handleSelected(val) {
        //
        this.$emit('selected', val)
        this.close()
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
