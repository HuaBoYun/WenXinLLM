<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="data"
            lazy
            :load="fetchData"
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
              prop="realname"
            ></el-table-column>
            <el-table-column prop="orgname" label="所属部门"></el-table-column>
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
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import { findOrganization } from '@/api/setting/org'
  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
    },
    components: {},
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,

        dialogVisible: false,
        list: [],
        data: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        reviewType: '',
      }
    },
    methods: {
      /**
       * @description  组件初始化，调用左侧树，列表接口
       * @param {*}
       * @return {*}
       */
      showEdit(e) {
        if (e) {
          this.reviewType = e
        }
        this.dialogVisible = true
        this.current = undefined
        // this.getExecutorTree()
        this.fetchData()
        this.getExecutorList()
      },
      fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        let cs = {}
        if (node && node.data) {
          cs.fatherorgid = node.data.id
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganization(cs)
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
      /**
       * @description  处理数据，处理成指定格式
       * @param {*}
       * @return {*}
       */
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
       * @description  左侧树接口
       * @param {*}
       * @return {*}
       */
      async getExecutorTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        this.dataTree = res
      },
      /**
       * @description  列表接口
       * @param {*}
       * @return {*}
       */
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description  点击左侧树，重新获取列表接口
       * @param {*}
       * @return {*}
       */
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      /**
       * @description  分页，选择每页几条数据，查询每页多少条数据
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
       * @description 分页，选择页码，查询第几页的数据
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
       * @description  点击左侧树，重新获取列表接口
       * @param {*}
       * @return {*}
       */
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      /**
       * @description 选择列表数据,把选择的数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description  点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.multipleSelection)
        //  选择 复核人
        this.$emit('reviewTypeSelect', {
          reviewType: this.reviewType,
          id: this.multipleSelection,
        })
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
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
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
  ::v-deep {
    .el-tree {
      width: 100%;
      overflow: scroll;
      overflow-x: auto;
    }
    .el-tree > .el-tree-node {
      display: inline-block;
      min-width: 100%;
    }
  }
</style>
