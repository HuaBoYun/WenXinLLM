<template>
  <el-dialog
    :close-on-click-modal="false"
    append-to-body
    :visible.sync="dialogVisible"
    width="1400px"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            :default-expanded-keys="defaultExpandedKeys"
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-left-panel :span="16">
              <el-select
                v-if="isUserName"
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
          </vab-query-form>
          <vab-query-form>
            <vab-query-form-left-panel :span="12">
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="姓名"
                style="width: 50%; margin: 0 10px 10px 0 !important"
              />
              <el-button type="primary" @click="queryName">查询</el-button>
              <el-button type="primary" @click="resetQueryName">重置</el-button>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel :span="12">
              <el-button @click="close">取 消</el-button>
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
  import { selectPerson } from '@/api/audit/project'
  import { getExecutorOptionsList } from '@/api/contract/manage.js'
  import { getOrgTreeByDepartment } from '@/api/common'
  export default {
    components: {},
    props: {
      defaultExpandedH: {
        // 需要展示的层级
        type: Number,
        default: 3,
      },
      // isUserName: {
      //   type: Boolean,
      //   default: false,
      // },
    },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        flagTitle: false,
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
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: '',
        },
        current: undefined,
        defaultExpandedKeys: [],
        userId: [],
        userList: [],
        isUserName: false,
      }
    },
    methods: {
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
      showEdit(flag) {
        this.flag = flag
        if (flag == 'leader' || flag == 'other') {
          this.flagTitle = true
          this.isUserName = false
        } else {
          this.flagTitle = false
          this.isUserName = true
        }
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorTree()
        this.getExecutorList()
      },
      async getExecutorTree() {
        const res = await getOrgTreeByDepartment(this.queryForm)
        this.defaultExpandedKeys = this.showExpandedNode(
          res,
          this.defaultExpandedH
        )
        this.dataTree = res
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      queryName() {
        // 姓名查询
        this.queryForm.orgid = undefined
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.getExecutorList()
      },
      resetQueryName() {
        // 重置姓名查询
        this.queryForm.realname = ''
        this.queryName()
      },
      async handleNodeClick(val) {
        console.log(val)
        this.queryForm.pageNumber = 1
        this.queryForm.orgid = val.id
        await this.getExecutorList()
        await this.setSelection(this.userList)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      //两数组取不相同
      getNewArr(a, b) {
        const arr = [...a, ...b]
        const newArr = arr.filter((item) => {
          return !(a.includes(item) && b.includes(item))
        })
        return newArr
      },
      handleSelection(rows, row) {
        console.log('rows', rows)
        console.log('row', row)
        if (!this.isUserName) {
          console.log(rows)
          this.current = rows
          if (this.flagTitle) {
            if (rows.length > 1) {
              let del = rows.shift()
              this.$refs.multipleTable.toggleRowSelection(del, false)
            }
            this.multipleSelection = rows
          } else {
            this.multipleSelection = rows
          }
        } else {
          let selected = rows.length && rows.indexOf(row) !== -1
          console.log(selected) // true就是选中，0或者false是取消选中
          let list = this.userList || []
          if (selected) {
            list.push(row)
          } else {
            list = list.filter((item) => item.staffid !== row.staffid)
          }
          this.userId = list.map((item) => item.staffid)
          this.userList = list
          this.$forceUpdate()
        }
      },
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
      save() {
        if (!this.isUserName) {
          if (!this.current) {
            this.$baseMessage(
              '请选择执行人！',
              'error',
              'vab-hey-message-error'
            )
            return
          }
          if (this.flag === 'other') {
            this.$emit('auditeeSelect', this.multipleSelection, 'right')
          } else {
            this.$emit('selectTeamList', this.multipleSelection, this.flagTitle)
          }
          this.queryForm.realname = ''
          this.dialogVisible = false
        } else {
          if (this.userList.length == 0) {
            this.$baseMessage(
              '请选择执行人！',
              'error',
              'vab-hey-message-error'
            )
            return
          }
          if (this.flag === 'other') {
            this.$emit('auditeeSelect', this.userList, 'right')
          } else {
            this.$emit('selectTeamList', this.userList, this.flagTitle)
          }
          this.queryForm.realname = ''
          this.userList = []
          this.userId = []
          this.dialogVisible = false
        }
      },
      close() {
        this.queryForm.realname = ''
        this.dialogVisible = false
      },
      /**
       * 默认展开层级
       * @param {Array} treeData tree数据
       * @param {Number} h 需要展开的层级
       */
      showExpandedNode(treeData, h) {
        const _t = []
        if (h && typeof h === 'number' && treeData && treeData.length) {
          let tempH = 0
          const recurrence = (arr) => {
            arr.forEach((x) => {
              tempH++
              _t.push(x.id)
              if (tempH < h && x.children && x.children.length)
                recurrence(x.children)
            })
          }
          recurrence(treeData)
        }
        return _t || []
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
    width: 400px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
