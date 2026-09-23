<template>
  <el-dialog
    title="被审计单位"
    :visible.sync="dialogVisible"
    width="1400px"
    :close-on-click-modal="false"
    :modal="false"
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

export default {
  components: {},
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
      },
      queryForm: {
        pid: undefined,
        pageNumber: 1,
        pageSize: 20,
      },
      current: undefined,
      currentTree: undefined,
    }
  },
  methods: {
      /**
       * @description 组件初始化，调用左侧树，列表接口
       * @param {*}  
        * @return {*}
       */  
    showEdit() {
      this.dialogVisible = true
      this.current = undefined
      this.currentTree = undefined
      this.getExecutorTree()
      this.getExecutorList()
    },
      /**
       * @description   左侧树接口
       * @param {*}  
        * @return {*}
       */ 
    async getExecutorTree() {
      const res = await orgList(this.queryForm)
      this.dataTree = JSON.parse(res.data.orgTree)
    },
      /**
       * @description  列表接口
       * @param {*}  
        * @return {*}
       */  
    async getExecutorList() {
      this.listLoading = true
      const {
        data: {
          pageInfo: { tlist, totalRecord },
        },
      } = await userList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
      /**
       * @description  点击左侧树，重新获取列表接口，并把左侧树节点存入currentTree
       * @param {*}  
        * @return {*}
       */  
    handleNodeClick(val) {
      this.currentTree = val
      this.selectTree = val
      this.queryForm.pid = val.id
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
       * @description  选择列表数据,把选择的数据存入multipleSelection
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
      this.$emit('objList', this.multipleSelection, 'right')
      this.dialogVisible = false
    },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}  
        * @return {*}
       */ 
    saveTree() {
      if (!this.currentTree) {
        this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
        return
      }
      this.$emit('objList', this.selectTree, 'left')
      this.dialogVisible = false
    },
  },
}
</script>
<style scoped lang="scss">
  /**
       * @description  编辑按钮触发，唤起新建弹框
       * @param {*}  
        * @return {*}
       */ 隐藏表头全选框
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
