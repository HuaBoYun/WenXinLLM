<template>
  <el-dialog
    title="被审计单位"
    :visible.sync="dialogVisible"
    width="1400px"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
  >
    <!-- :load="loadNode" -->

    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <company-tree ref="leftList" @select="handleTreeSelect" />
          <!-- <el-tree
            ref="tree"
            lazy
            :load="loadNode"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
            @node-expand="getTreeChider"
          /> -->
        </div>
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button type="primary" @click="saveTree">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
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
            <el-table-column prop="orgName" label="所属部门"></el-table-column>
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
  import { orgList, userList } from '@/api/audit/implement'
  import CompanyTree from './conpanyTree.vue'
  export default {
    components: { CompanyTree },
    data() {
      return {
        listLoading: false,
        selectTree: {},
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',

        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
          isLeaf: 'isParent',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        currentTree: undefined,
      }
    },
    methods: {
       /** 
       *@description: 组件初始化 
       *@param {*} 
       *@return {*}
      */
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.currentTree = undefined
        // this.getExecutorTree()
      },
        /**
       *@description: 重置列表请求
       *@param {*} 
       *@return {*}
      */
      handleTreeSelect(v, b) {
        this.queryForm.orgid = v.id
        this.selectTree = b
        this.currentTree = v
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },
        /**
       *@description: 左侧树接口
       *@param {*} 
       *@return {*}
      */
      getExecutorTree(id, resolve) {
        if (id) {
          let data = []
          orgList({ pid: id }).then((res) => {
            if (res.code === 1) {
              data = JSON.parse(res.data.orgTree)
              if (data && data.length) {
              }
              resolve(data)
            } else {
              resolve([])
            }
          })
          return
        }

        orgList().then((res) => {
          if (res.code === 1) {
            this.dataTree = JSON.parse(res.data.orgTree)
            if (this.dataTree && this.dataTree.length) {
              this.queryForm.orgid = this.dataTree[0].id
              this.getExecutorList()
            }
          }
        })
      },
        /**
       *@description: 列表接口
       *@param {*} 
       *@return {*}
      */
      getExecutorList() {
        this.listLoading = true
        userList(this.queryForm).then((res) => {
          this.list = res.data.pageInfo.tlist
          this.total = res.data.pageInfo.totalRecord
          this.listLoading = false
        })
      },
        /**
       *@description: 树懒加载
       *@param {*} 
       *@return {*}
      */
      loadNode(node, resolve) {
        let that = this
        if (node.level === 0) {
          this.getExecutorTree()
        }
        if (node.level > 0) {
          if (node.data.isParent) {
            that.getExecutorTree(node.data.id, resolve)
          } else {
            return resolve([])
          }
        }
      },
      getTreeChider(v, n, m) {},
        /**
       * @description  点击左侧树，重新获取列表接口
       * @param {*}  
        * @return {*}
       */  
      handleNodeClick(val) {
        this.currentTree = val
        this.selectTree = val
        this.queryForm.pageNumber = 1
        this.queryForm.orgid = val.id
        this.getExecutorList()
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
       * @description  点击右边确定,把数据回传到父组件,关闭当前组件
       * @param {*}  
        * @return {*}
       */ 
      save() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.multipleSelection, 'right')
        this.dialogVisible = false
      },
        /**
       * @description  点击左边确定,把数据回传到父组件,关闭当前组件
       * @param {*}  
        * @return {*}
       */ 
      saveTree() {
        if (!this.currentTree) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.selectTree, 'left')
        this.dialogVisible = false
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
    margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
