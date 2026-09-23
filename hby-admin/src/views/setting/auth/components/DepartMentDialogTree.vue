<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    :append-to-body="true"
    width="1400px"
    @close="close"
  >
    <div class="system-log-container">
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
            <vab-query-form-left-panel :span="16">
              <el-select
                v-if="isMultiple"
                v-model="userId"
                multiple
                style="width: 600px"
                @remove-tag="removeTag"
              >
                <el-option
                  v-for="item in userList"
                  :key="item.orgid"
                  :label="item.orgname"
                  :value="item.orgid"
                ></el-option>
              </el-select>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel :span="8">
              <el-button @click="close">取 消</el-button>
              <el-button type="primary" @click="confirm">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <vab-query-form>
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item>
                  <el-input
                    v-model="queryForm.deptNumber"
                    clearable
                    placeholder="部门编号"
                  />
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="queryForm.deptName"
                    clearable
                    placeholder="部门名称"
                  />
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
            <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
           <el-button type="primary">导出</el-button>
      </vab-query-form-right-panel> -->
          </vab-query-form>
          <el-table
            ref="multipleTable"
            :data="list"
            style="width: 100%"
            highlight-current-row
            @select="onSelect"
            @current-change="handleSelected"
          >
            <el-table-column
              v-if="isMultiple"
              type="selection"
              :selectable="checkSelectable"
              width="55"
            ></el-table-column>
            <el-table-column
              label="部门编号"
              align="center"
              prop="orgnumber"
            ></el-table-column>
            <el-table-column
              label="部门名称"
              prop="orgname"
              align="center"
            ></el-table-column>

            <!-- <el-table-column prop="orgName" label="所属部门"></el-table-column> -->
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
    <!-- <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
  import {
    getAllCompanyTree,
    getGrantDataRightDeptList,
  } from '@/api/setting/org'
  import { findOrganization } from '@/api/setting/org'

  export default {
    name: 'DepartMentDialog',
    components: {},
    props: {
      isMultiple: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        title: '选择部门',
        disabled: false,
        dialogFormVisible: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        current: null,
        list: [],
        queryForm: {
          deptNumber: '',
          deptName: '',
          pid: undefined,
          roleId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        loading: false,
        expandedKeys: [],
        userId: [],
        userList: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        selectedItem: undefined,
        company: '',
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
    updated() {
      // 在组件更新后渲染 el-tree 组件
      this.$nextTick(() => {
        this.$refs.treeDialog.$refs.tree.updateKeyChildren()
      })
    },
    methods: {
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
          this.$refs.treeDialog.$data.store.lazy = false
          this.fetchData() //加载数据
        }
        this.$forceUpdate()
      },
      checkSelectable(row) {
        // console.log("row",row)
        return !row.isChecked
      },
      handleSelected(val) {
        if (this.isMultiple) return
        this.current = {
          ...val,
          orgname: this.company.orgname + '/' + val.orgname,
          companyId: this.company.orgid,
        }
        // this.dialogFormVisible = false
      },
      handleNodeClick(data) {
        this.queryForm.pid = data.id
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },
      async show(list, roleId) {
        await this.fetchData()
        console.log('list', list)
        this.queryForm.roleId = roleId
        this.getExecutorList()
        if (list && list.length > 0) {
          this.userList = list
          this.userId = list.map((item) => item.orgid)
          await this.setSelection(list)
        }
        this.dialogFormVisible = true
      },
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.orgid == item.orgid
              }),
              true
            )
          })
        })
      },
      onSelect(rows, row) {
        console.log('ss', row)
        let selected = rows.length && rows.indexOf(row) !== -1
        console.log(selected) // true就是选中，0或者false是取消选中
        let list = this.userList || []
        if (selected) {
          list.push({
            ...row,
            orgname: this.company.orgname + '/' + row.orgname,
            companyId: this.company.orgid,
          })
        } else {
          list = list.filter((item) => item.orgid !== row.orgid)
        }
        list = Array.from(new Set(list))
        this.userId = list.map((item) => item.orgid)
        this.userList = list
        this.$forceUpdate()
      },
      async removeTag(e) {
        let list = this.userList
        let id = this.userId
        list = await list.filter((item) => item && item.orgid != e)
        id = await id.filter((item) => item !== e)
        this.userList = list
        this.userId = id
        this.$forceUpdate()
        await this.$refs.multipleTable.clearSelection()
        await this.setSelection(list)
      },
      close() {
        this.dialogFormVisible = false
        this.selectedItem = undefined
      },
      confirm() {
        if (this.isMultiple) {
          // if (this.userList.length == 0) {
          //   this.$message.error('请选择部门！')
          //   return
          // }
          this.$emit('select', this.userList)
          this.userList = []
          this.userId = []
          this.close()
        } else {
          if (!this.current) {
            this.$message.error('请选择部门！')
            return
          }

          console.log('current', this.current)
          this.$emit('select', this.current)
          this.current = null
          this.close()
        }
      },
      // async organization() {
      //   const res = await findOrganization()
      //   this.data = res
      // },
      resetSearch() {
        this.queryForm.deptNumber = ''
        this.queryForm.deptName = ''
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },
      async getExecutorList() {
        const res = await getGrantDataRightDeptList(this.queryForm)
        const {
          data: {
            pageInfo: { tlist, totalRecord },
            company,
          },
        } = res
        this.company = company
        this.list = tlist
        console.log('list', tlist)
        this.total = totalRecord
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
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
        this.loading = true
        // {nodeId: !node ? this.currentOrg.id : node.data.id,}
        findOrganization({
          fatherorgid: !node ? '' : node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            const tree = this.formatTree(res.data)
            if (node && node.level > 0) {
              resolve(tree[0].children)
              return
            }
            if (!node) {
              // this.$emit('select', {
              //   id: tree[0].id,
              //   label: tree[0].name,
              // })
              this.expandedKeys.push(this.currentOrg.id)
              console.log('company tree:', tree)
              this.data = tree
            }
            if (this.orgName) {
              let that = this
              this.data = tree
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
    width: 350px;
    border-right: 1px solid ghostwhite;
    // margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 70%;
  }
</style>
