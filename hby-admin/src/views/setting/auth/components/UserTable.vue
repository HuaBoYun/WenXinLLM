<template>
  <el-dialog
    :append-to-body="true"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <div class="lr-layout">
      <div class="left">
        <el-input
          v-model="orgName"
          clearable
          placeholder="公司名称"
          @change="change()"
          style="margin-bottom: 10px"
        />
        <el-button icon="el-icon-search" type="primary" @click="queryData">
          查询
        </el-button>
        <el-button
          type="primary"
          @click="resetSearchTree"
          style="margin-bottom: 10px"
        >
          重置
        </el-button>
        <el-tree
          ref="treeDialog"
          :data="data"
          v-loading="loading"
          :default-expanded-keys="expandedKeys"
          :default-expand-all="false"
          :expand-on-click-node="false"
          :highlight-current="true"
          node-key="id"
          lazy
          :load="fetchData"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <el-select
            v-model="userId"
            multiple
            style="width: 400px; margin-bottom: 10px"
            @remove-tag="removeTag"
          >
            <el-option
              v-for="item in userList"
              :key="item.staffid"
              :label="item.realname"
              :value="item.staffid"
            ></el-option>
          </el-select>
        </vab-query-form>
        <vab-query-form>
          <vab-query-form-left-panel :span="18">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.userName"
                  clearable
                  placeholder="用户名"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.realName"
                  clearable
                  placeholder="用户真实名"
                />
              </el-form-item>
              <el-form-item>
                <el-checkbox v-model="queryForm.isAll">
                  是否筛选全集团
                </el-checkbox>
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="getExecutorList"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="6">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          ref="multipleTable"
          :row-key="getRowKeys"
          @selection-change="handleSelectionChange"
          @select="onSelect"
        >
          <el-table-column
            width="48"
            type="selection"
            :selectable="checkSelectable"
            :reserve-selection="true"
          ></el-table-column>
          <el-table-column align="center" label="用户真实名" prop="realname" />
          <el-table-column
            align="center"
            label="所属公司"
            prop="companyname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="所属部门"
            prop="orgname"
            show-overflow-tooltip
          />
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
  </el-dialog>
</template>
<script>
  import { getAllOrgInfoTree, getSystemRightStaffList } from '@/api/setting/org'
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
          isLeaf: 'isLeaf',
        },
        data: [],
        queryForm: {
          roleId: '',
          pid: undefined,
          realName: '',
          userName: '',
          isAll: false,
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        userId: [],
        userList: [],
        loading: false,
        expandedKeys: [],
        orgName: '',
      }
    },
    computed: {
      currentOrg() {
        const orgStr = window.sessionStorage.getItem('current-org')
        if (orgStr && !this.alwaysRoot) {
          try {
            const org = JSON.parse(orgStr)
            if (org.id && org.label) {
              return org
            }
          } catch (e) {
            console.log(e)
          }
        }
        return {
          id: 1,
          label: '长江投资（中国）有限公司',
        }
      },
    },
    created() {
      this.fetchData()
    },
    methods: {
      checkSelectable(row) {
        console.log('row', row.checked > 0)
        return row.checked < 1
      },
      async show(roleId) {
        this.queryForm.roleId = roleId
        this.current = undefined
        this.select = []
        this.dialogFormVisible = true
        await this.fetchData()
        await this.getExecutorList()
      },
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
      change() {
        this.$forceUpdate()
      },
      resetSearchTree() {
        this.orgName = ''
        this.$refs.treeDialog.$data.store.lazy = true // 开启懒加载
        this.fetchData()
        this.$forceUpdate()
      },
      queryData() {
        if (this.orgName == '') {
          this.$refs.treeDialog.$data.store.lazy = true // 开启懒加载
          this.fetchData()
        } else {
          this.data = []
          console.log('..')
          this.$refs.treeDialog.$data.store.lazy = false
          this.fetchData() //加载数据
        }
        this.$forceUpdate()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getSystemRightStaffList({
          ...this.queryForm,
          isAll: this.queryForm.isAll ? 1 : 0,
          orgId: this.queryForm.pid,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      async handleNodeClick(val) {
        this.queryForm.pageNumber = 1
        this.queryForm.pid = val.id
        await this.getExecutorList()
        await this.setSelection(this.userList)
      },
      //两数组取不相同
      getNewArr(a, b) {
        const arr = [...a, ...b]
        const newArr = arr.filter((item) => {
          return !(a.includes(item) && b.includes(item))
        })
        return newArr
      },
      resetSearch() {
        this.queryForm.realName = ''
        this.queryForm.userName = ''
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },
      confirm() {
        if (this.userList.length == 0) {
          this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.userList)
        this.close()
      },
      fetchData(node, resolve) {
        console.log('node', node)
        console.log('node', this.data)
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        getAllOrgInfoTree({
          fatherorgid: !node ? '' : node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            const tree = this.formatTree(res.data)
            console.log('tree:data', res)
            if (node && node.level > 0) {
              resolve(tree[0].children)
              return
            }
            if (!node) {
              // this.$emit('select', {
              //   id: tree[0].id,
              //   label: tree[0].name,
              // })
              this.expandedKeys.push(tree[0].id)
              console.log('tree:lll', tree)
              this.data = tree
              this.$nextTick(() => {
                this.$refs.treeDialog.$refs.tree.updateKeyChildren()
              })
            }

            if (this.orgName) {
              let that = this
              this.data = tree
              this.treeDataStatus = true
              var Date2 = window.setTimeout(function () {
                console.log('Date2', Date2)
                that.setAllExpand()
              }, 1000)
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      setAllExpand() {
        for (
          var i = 0;
          i < this.$refs.treeDialog.store._getAllNodes().length;
          i++
        ) {
          this.$refs.treeDialog.store._getAllNodes()[i].expanded = true
        }
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
        this.queryForm = {
          roleId: '',
          pid: undefined,
          realName: '',
          userName: '',
          isAll: false,
          pageNumber: 1,
          pageSize: 20,
        }
        this.userList = []
        this.userId = []
        this.orgName = ''
        this.data = []
        this.expandedKeys = []
        this.list = []
        this.total = 0
        this.select = []
        // 清空表格选择状态
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {},
      onSelect(rows, row) {
        console.log('ss', row)
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
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

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
</style>
