<template>
  <el-dialog
    :close-on-click-modal="false"
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1400px"
    title="被审计对象"
    append-to-body
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <div
            style="
              display: flex;
              flex-direction: row-reverse;
              margin-bottom: 10px;
            "
          >
            <el-button type="primary" @click="saveCompany">确 定</el-button>
            <el-button
              @click="dialogVisible = false"
              style="margin-right: 10px"
            >
              取 消
            </el-button>
          </div>
          <!-- <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            :default-expanded-keys="defaultExpandedKeys"
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
            show-checkbox
            @check-change="handleCheckChange"
          /> -->
          <el-tree
            ref="tree"
            v-loading="loading"
            :data="data"
            :default-expanded-keys="expandedKeys"
            :default-expand-all="false"
            :expand-on-click-node="false"
            :highlight-current="true"
            node-key="id"
            :props="defaultProps"
            :load="fetchData"
            lazy
            show-checkbox
            :check-strictly="true"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
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
  import { getUserList } from '@/api/contract/manage.js'
  import { getAllOrgInfoTree } from '@/api/setting/org'
  import { selectPerson } from '@/api/audit/project'
  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
      defaultExpandedH: {
        // 需要展示的层级
        type: Number,
        default: 3,
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
        multipleSelection: [],
        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: '',
        },
        current: undefined,
        reviewType: '',
        defaultExpandedKeys: [],
        arr: [],
        arrData: [],
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        nodeid: '',
        orgName: '',
      }
    },
    created() {
      this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      this.fetchData()
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
    methods: {
      showEdit(e) {
        if (e) {
          this.reviewType = e
        }
        this.dialogVisible = true
        this.current = undefined
        this.fetchData()
        this.getExecutorList()
      },

      async fetchData(node, resolve) {
        console.log('node', node)
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        this.loading = true

        getAllOrgInfoTree({
          fatherorgid: !node ? this.orgid : node.data && node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            console.log('res', res)
            const tree = this.formatTree(res.data)
            console.log('tree:data', res.data)

            if (node && node.level > 0) {
              console.log('tree:', tree)
              resolve(tree[0].children)
              return
            }
            console.log('node', !node)
            if (!node) {
              this.$emit('select', {
                id: tree[0].id,
                label: tree[0].name,
              })
              this.expandedKeys.push(tree[0].id)
              console.log('tree:lll', tree)
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
        this.loading = false
      },
      setAllExpand() {
        for (var i = 0; i < this.$refs.tree.store._getAllNodes().length; i++) {
          this.$refs.tree.store._getAllNodes()[i].expanded = true
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
      // async getExecutorList() {
      //   this.listLoading = true
      //   const {
      //     data: { list, total },
      //   } = await getUserList(this.queryForm)
      //   this.list = list
      //   this.total = total
      //   this.listLoading = false
      // },
      async getExecutorList() {
        if (this.queryForm.realname) {
          this.queryForm.pageSize = 20
          this.queryForm.pageNumber = 1
        }
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson({ ...this.queryForm })
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
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        if (!this.current) {
          this.$baseMessage('请选择用户！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.multipleSelection, 'right')
        this.queryForm.realname = ''
        this.dialogVisible = false
      },
      saveCompany() {
        if (!this.$refs.tree.getCheckedNodes().length) {
          this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.$refs.tree.getCheckedNodes(), 'left')
        this.dialogVisible = false
      },
      /**
       * 默认展开层级
       * @param {Array} treeData tree数据
       * @param {Number} h 需要展开的层级
       */
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
    width: 500px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
