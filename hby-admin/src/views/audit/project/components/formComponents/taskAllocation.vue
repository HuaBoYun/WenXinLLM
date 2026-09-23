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
                show-overflow-tooltip
                width="200"
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
  import { getProjectRwList, getTreeTask } from '@/api/audit/project'
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
        projectCode: '',
      }
    },
    methods: {
      /**
       * @description 关闭组件 ，回调父组件函数
       * @param {*}
       * @return {*}
       */
      beforeClose() {
        this.$emit('close')
      },
      /**
       * @description 树，懒加载
       * @param {*}
       * @return {*}
       */
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
      /**
       * @description 组件初始化
       * @param {*}
       * @return {*}
       */
      showEdit(row) {
        this.projectId = row.projectId
        this.projectCode = row.projectCode
        this.dialogVisible = true
        this.current = undefined
        // this.rowData = row
        // this.getExecutorTree(row)
      },
      /**
       * @description 树接口
       * @param {*}
       * @return {*}
       */
      async getExecutorTree(row) {
        // const res = await findOrganizationByTreeAllss(this.queryForm)
        getTreeTask({
          type: 'all',
          projectId: row.projectId,
        }).then((res) => {
          if (res.code == 1) {
            this.queryForm.projectId = row.projectId
            this.dataTree = res.data.tree

            if (res.data.tree && res.data.tree.length) {
              for (let i = 0; i < res.data.tree.length; i++) {
                const e = res.data.tree[i]
                getTreeTask({
                  type: 'all',
                  projectid: row.projectId,
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
      /**
       * @description 列表解决
       * @param {*}
       * @return {*}
       */
      getExecutorList() {
        this.listLoading = true
        let data = {
          projectid: this.projectId,
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
      /**
       * @description 分页，选择每页几条数据，查询每页多少条数据
       * @param {*}
       * @return {*}
       */
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
       * @description  分页，选择页码，查询第几页的数据
       * @param {*}
       * @return {*}
       */
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
       * @description 点击左侧树，重新获取列表接口
       * @param {*}
       * @return {*}
       */
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
      /**
       * @description 选择列表数据,把选择的数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        this.current = val
        this.multipleSelection = val
      },
      /**
       * @description 选择列表数据,把选择的数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelectionAll(val) {
        //
        this.multipleSelection = val
      },
      /**
       * @description 点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }

        // this.$emit('projectManage', this.multipleSelection)
        this.$refs['allocationChild'].showEdit(
          this.multipleSelection,
          this.projectId,
          this.projectCode
        )
        // this.dialogVisible = false
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
