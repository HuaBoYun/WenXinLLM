<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div
      class="lr-layout"
      :style="
        $store.state.work.processMobile ? 'display: block;' : 'display: flex;'
      "
    >
      <el-popover
        placement="bottom-end"
        width="400"
        trigger="click"
        :offset="100"
        v-if="$store.state.work.processMobile"
      >
        <div style="height: 300px; overflow-y: auto">
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
        <el-button slot="reference">选择部门</el-button>
      </el-popover>
      <div class="left" v-else>
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
          class="my-pagination"
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
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
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
      handleNodeClick(val) {
        this.queryForm.pid = val.id
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      //前置校验
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.current)

        this.dialogFormVisible = false
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
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
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

  .my-pagination {
    white-space: break-spaces !important;
  }
</style>
