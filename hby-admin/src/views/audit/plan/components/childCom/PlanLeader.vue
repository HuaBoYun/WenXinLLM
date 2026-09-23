<template>
  <el-dialog
    title="计划负责人"
    :visible.sync="dialogVisible"
    width="1400px"
    :modal-append-to-body="false"
    :append-to-body="true"
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
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,

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
    }
  },
  methods: {
     /**
       * @description: 组件初始化，唤起，调用左侧树接口，右侧列表接口
       * @param {*} 
       * @return {*}
       */
    showEdit() {
      this.dialogVisible = true
      this.current = undefined
      this.getExecutorTree()
      this.getExecutorList()
    },
    /**
       * @description: 左侧树接口，返回数据处理
       * @param {*} 
       * @return {*}
       */ 
    async getExecutorTree() {
      const res = await orgList(this.queryForm)
      this.dataTree = JSON.parse(res.data.orgTree)
    },
    /**
       * @description:  右侧列表接口
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
       * @description:  左侧树点击触发，重新请求右侧列表接口，重新渲染
       * @param {*} val
       * @return {*}
       */   
    handleNodeClick(val) {
      this.queryForm.pid = val.id
      this.getExecutorList()
    },
      /**
       * @description:  右侧列表分页函数,选择每页 val条数据
       * @param {*} val
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
       * @description:   右侧列表分页函数,选择第val页数据
       * @param {*} val
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
       * @description:  右侧列表勾选函数,把勾选的数据存入multipleSelection
       * @param {*} val
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
       * @description:  组件确认函数,把选中的数据返回到父组件,并关闭组件
       * @param {*} val
       * @return {*}
       */   
    save() {
      if (!this.current) {
        this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
        return
      }
      this.$emit('planList', this.multipleSelection)
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
  margin-right: 100px;
  padding-right: 10px;
}

.lr-layout > .right {
  width: 75%;
}
</style>
