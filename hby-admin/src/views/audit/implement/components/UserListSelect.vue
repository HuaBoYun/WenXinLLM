<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row>
      <el-col :span="6">
        <el-tree
          ref="tree"
          :data="treeData"
          :props="defaultProps"
          :check-strictly="true"
          show-checkbox
          node-key="id"
          default-expand-all
          highlight-current
          @check="getname"
        ></el-tree>
      </el-col>
      <el-col :span="18">
        <el-table
          :data="list"
          ref="multipleTable"
          v-loading="listLoading"
          @select-all="onSelectAll"
          @selection-change="selectItem"
          @row-click="onSelectOp"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column align="center" label="姓名" prop="realname" />
          <el-table-column align="center" label="部门" prop="orgName" />
          <el-table-column align="center" label="邮箱" prop="email" />
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
      </el-col>
    </el-row>

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { userList, orgList } from '@/api/audit/implement'
  export default {
    name: 'UserListSelect',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        dialogFormVisible: false,
        title: '选择报告人',
        queryForm: {
          orgid: null,
          pageNumber: 1,
          pageSize: 10,
        },
        info: null,
        list: [],
        treeData: [],
        listLoading: true,
        multipleSelection: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
        },
      }
    },
    computed: {},
    async created() {
      await this.fetchTree()
      await this.fetchData()
    },
    mounted() {},
    methods: {
      // 选择会触发getname方法
      getname(data) {
        this.$refs.tree.setCheckedKeys([])
        this.$refs.tree.setCheckedKeys([data.id])
        this.queryForm.orgid = data.id
        this.fetchData()
      },

      handleCheckChange(data, checked, indeterminate) {
        if (checked) {
          // this.formData.orgid = data.id
          this.$refs['tree'].setCheckedKeys([data.id])
        }
      },
      async fetchTree() {
        const {
          data: { orgTree },
        } = await orgList()
        let treeData = JSON.parse(orgTree)
        this.treeData = treeData
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await userList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit() {
        this.queryForm.orgid = null
        this.dialogFormVisible = true
        await this.fetchTree()
        await this.fetchData()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      onSelectAll() {
        this.$refs.multipleTable.clearSelection()
      },
      selectItem(rows) {
        if (rows.length > 1) {
          var newRows = rows.filter((it, index) => {
            if (index == rows.length - 1) {
              this.$refs.multipleTable.toggleRowSelection(it, true)
              return true
            } else {
              this.$refs.multipleTable.toggleRowSelection(it, false)
              return false
            }
          })
          this.multipleSelection = newRows
          this.info = newRows[0]
        } else {
          this.multipleSelection = rows
          this.info = rows[0]
        }
      },
      onSelectOp(row) {
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row, true)
        this.multipleSelection = []
        this.multipleSelection.push(row)
      },
      add() {
        if (!this.info) {
          this.$baseMessage('请选择报告人！', 'error')
        } else {
          this.$emit('getUserInfo', this.info)
          this.close()
        }
      },
    },
  }
</script>

<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
