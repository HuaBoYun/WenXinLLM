<template>
  <el-dialog
    append-to-body
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    v-if="dialogVisible"
    @close="close"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="data"
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :load="fetchData"
            lazy
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-left-panel :span="16">
              <el-select
                v-model="userId"
                multiple
                style="width: 400px"
                @remove-tag="removeTag"
              >
                <el-option
                  v-for="item in userList"
                  :key="item.staffid"
                  :label="item.realname"
                  :value="item.staffid"
                ></el-option>
              </el-select>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel :span="8">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            ref="multipleTable"
            :data="list"
            tooltip-effect="dark"
            @selection-change="handleSelection"
            @select="onSelect"
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
  import { getStaffPageList } from '@/api/setting/personnel'

  export default {
    components: {},
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        flagTitle: false,
        dialogVisible: false,
        list: [],
        data: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'label',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        userId: [],
        userList: [],
        pid: undefined,
        val: [],
      }
    },
    methods: {
      /**
       * @description  关闭组件
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogVisible = false
        this.queryForm = {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      /**
       *@description: 组件初始化，调用左侧树，列表接口
       *@param {*}
       *@return {*}
       */
      showEdit(flag) {
        if (flag == 'leader') {
          this.flagTitle = true
        } else {
          this.flagTitle = false
        }
        this.dialogVisible = true
        this.current = undefined
        // this.getExecutorTree()
        this.fetchData()
        this.getExecutorList()
        this.val = []
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
       *@description: 左侧树接口
       *@param {*}
       *@return {*}
       */
      async getExecutorTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
        const aa = {
          id: -2,
          isParent: false,
          name: '已经入库审计人员',
          children: [],
          open: true,
          pId: -2,
          target: 'mainFramex',
          url: 'xxxxxx',
        }
        res[0].children.unshift(aa)
        this.dataTree = res
      },
      /**
       *@description:列表接口
       *@param {*}
       *@return {*}
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
       *@description: 选择列表数据
       *@param {*}
       *@return {*}
       */
      onSelect(rows, row) {
        let selected = rows.length && rows.indexOf(row) !== -1

        let list = this.userList || []
        if (selected) {
          list.push(row)
        } else {
          list = list.filter((item) => item.staffid !== row.staffid)
        }
        this.userId = list.map((item) => item.staffid)
        this.userList = list
        this.$forceUpdate()
      },
      /**
       *@description: 移除已选择的数据
       *@param {*}
       *@return {*}
       */
      async removeTag(e) {
        let list = this.userList
        let id = this.userId
        list = await list.filter((item) => item && item.staffid != e)
        id = await id.filter((item) => item !== e)

        this.userList = list
        this.userId = id
        this.$forceUpdate()
        await this.$refs.multipleTable.clearSelection()
        await this.setSelection(list)
      },
      /**
       *@description: 数据处理，把选中的数据，显示出来
       *@param {*}
       *@return {*}
       */
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.staffid == item.staffid
              }),
              true
            )
          })
        })
      },
      /**
       * @description  点击左侧树，重新获取列表接口
       * @param {*}
       * @return {*}
       */
      async handleNodeClick(val) {
        if (val.id === -2) {
          const orgId = JSON.parse(localStorage.getItem('userInfo')).linkDetp
            .orgid

          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await getStaffPageList({
            pageNo: 1,
            pageSize: 20,
            realName: '',
            education: '',
            jobExperiences: '',
            jobName: '',
            major: '',
            orgId,
          })
          const info = list.map((item) => {
            return { ...item, orgName: '已经入库审计人员' }
          })
          this.list = info
          this.total = total
        } else {
          this.queryForm.pageNumber = 1 //重置起始页
          this.queryForm.orgid = val.id
          await this.getExecutorList()
          await this.setSelection(this.userList)
        }
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
        if (this.flagTitle) {
          if (val.length > 1) {
            let del = val.shift()
            this.$refs.multipleTable.toggleRowSelection(del, false)
          }
          this.multipleSelection = val
        } else {
          this.multipleSelection = val
        }
      },
      /**
       * @description  点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (this.userList.length == 0) {
          this.$baseMessage('请选择组员！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selectTeamList', this.userList, this.flagTitle)
        this.userList = []
        this.userId = []
        // this.$emit('selectTeamList', this.multipleSelection, this.flagTitle)
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
