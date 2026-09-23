<template>
  <div>
    <el-dialog
      :visible.sync="dialogVisible"
      @close="beforeClose"
      width="1400px"
      :close-on-click-modal="false"
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
              :load="loadNode"
              lazy
              @node-click="handleNodeClick"
            />
          </div>
          <div class="right">
            <vab-query-form>
              <vab-query-form-right-panel :span="24">
                <el-button
                  @click="
                    () => {
                      this.$emit('close')
                    }
                  "
                >
                  取 消
                </el-button>
                <el-button type="primary" @click="save">分 配</el-button>
                <el-button type="primary" @click="close">确 定</el-button>
              </vab-query-form-right-panel>
            </vab-query-form>
            <el-table
              v-loading="listLoading"
              ref="multipleTable"
              :data="list"
              tooltip-effect="dark"
              @select="handleSelection"
              @select-all="handleSelectionAll"
              style="width: 100%"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                label="问题单元"
                prop="businessType"
              ></el-table-column>
              <el-table-column
                prop="riskSource"
                label="问题类型"
              ></el-table-column>
              <el-table-column
                prop="suditProcess"
                label="审计程序"
              ></el-table-column>

              <el-table-column
                prop="control"
                label="重点关注事项"
              ></el-table-column>
              <el-table-column
                prop="riskPoint"
                label="审计问题"
              ></el-table-column>
              <el-table-column prop="renyuan" label="人员"></el-table-column>
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
    <task-allocation-child
      @getExecutorList="getExecutorList"
      ref="allocationChild"
    ></task-allocation-child>
  </div>
</template>
<script>
  import { getProjectRwList, getTreeTask } from '@/oapi/audit/project'
  import taskAllocationChild from './taskAllocationChild/taskAllocationChild.vue'
  export default {
    components: { taskAllocationChild },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        rootId: '', //根节点ID
        dialogVisible: false,
        list: [],
        dataTree: [],
        rowData: {},
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    methods: {
      beforeClose() {
        this.$emit('close')
      },
      loadNode(node, resolve) {
        if (node.level === 0) {
          getTreeTask({
            type: 'all',
            projectId: this.projectId,
          }).then((res) => {
            resolve(res.data.tree)
          })
        }
        if (node.level === 1) {
          this.rootId = node.data.id
          this.handleNodeClick(node.data)
        }
        if (node.level > 0) {
          getTreeTask({
            type: 'all',
            projectId: this.projectId,
            nodeId: node.data.id,
          }).then((res) => {
            resolve(res.data.tree || [])
          })
        }
        resolve([])
      },
      showEdit(row) {
        this.projectId = row.id
        this.dialogVisible = true
        this.current = undefined
        this.ZSinfo = { name: row.zsname, staffid: row.zsstaffid }
        // this.rowData = row
        // this.getExecutorTree(row)
      },
      async getExecutorTree(row) {
        // const res = await findOrganizationByTreeAllss(this.queryForm)
        getTreeTask({
          type: 'all',
          projectId: row.id,
        }).then((res) => {
          if (res.code == 1) {
            this.queryForm.projectId = row.id
            this.dataTree = res.data.tree

            if (res.data.tree && res.data.tree.length) {
              for (let i = 0; i < res.data.tree.length; i++) {
                const e = res.data.tree[i]
                getTreeTask({
                  type: 'all',
                  projectId: row.id,
                  nodeId: e.id,
                }).then((resD) => {
                  this.dataTree[i].children = resD.data.tree
                })
              }

              this.handleNodeClick(res.data.tree[0])
            }
          }
        })
      },
      getExecutorList() {
        this.listLoading = true
        let data = {
          projectId: this.projectId,
          targetId: this.targetId,
        }
        getProjectRwList({ ...data, ...this.queryForm }).then((res) => {
          if (res.code == 1) {
            this.list = res.data.pageInfo.tlist
            this.total = res.data.pageInfo.totalRecord
          }
          this.listLoading = false
        })
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
        if (val.id === this.rootId) {
          this.targetId = undefined
        } else {
          this.targetId = val.id
        }
        // this.queryForm.nodeId = val.id
        // this.getExecutorTree()
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        this.multipleSelection = val
      },
      handleSelectionAll(val) {
        //
        this.multipleSelection = val
      },
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }

        // this.$emit('projectManage', this.multipleSelection)
        this.$refs['allocationChild'].showEdit(
          this.multipleSelection,
          this.projectId,
          null,
          this.ZSinfo
        )
        // this.dialogVisible = false
      },
      close() {
        this.$emit('close')
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  // ::v-deep thead {
  //   .el-table-column--selection {
  //     .el-checkbox__inner {
  //       display: none !important;
  //     }
  //   }
  // }

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
