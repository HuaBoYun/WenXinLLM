<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="false"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container" style="width: 100%">
      <div class="lr-layout">
        <div class="left">
          <!-- <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          /> -->
          <el-tree
            v-loading="loading"
            :data="data"
            :default-expanded-keys="expandedKeys"
            :expand-on-click-node="false"
            :highlight-current="true"
            lazy
            :load="fetchData"
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
            style="width: 300px"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-left-panel>
              <el-select
                style="width: 600px"
                v-model="value1"
                @remove-tag="removeTag"
                multiple
                placeholder="请选择"
                @change="$forceUpdate()"
              >
                <el-option
                  v-for="item in options"
                  :key="item.staffid"
                  :label="item.realname"
                  :value="item.staffid"
                ></el-option>
              </el-select>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel>
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
            <vab-query-form-left-panel>
              <el-input
                v-model="queryForm.realname"
                placeholder="请输入用户名"
                clearable
                style="width: 30%; margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="getExecutorList"
                style="margin-top: 10px !important"
              >
                查询
              </el-button>
              <el-button
                type="primary"
                @click="reset"
                style="margin-top: 10px !important"
              >
                重置
              </el-button>
            </vab-query-form-left-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            ref="multipleTable"
            :data="list"
            tooltip-effect="dark"
            @select="handleSelection"
            style="width: 80%"
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
  import { findOrganizationData } from '@/api/setting/org'

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
        options: [],
        dialogVisible: false,
        list: [],
        dataTree: [],
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
          realname: '',
        },
        current: undefined,
        reviewType: '',
        expandedKeys: [],
        nodeid: '',
        notDefaultSelect: false,
        data: [],
      }
    },
    methods: {
      removeTag(tag) {
        this.options = this.options.filter((item) => item.staffid !== tag)
        this.multipleSelection = this.multipleSelection.filter(
          (item) => item.staffid !== tag
        )
      },
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
      async getExecutorTree() {
        const res = await findOrganizationByTreeAllss(this.queryForm)
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
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      reset() {
        this.queryForm.realname = ''
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.getExecutorList()
      },
      handleSelection(val) {
        let options = this.options
        options.push(...val)
        this.options = Array.from(new Set(options))
        let value1 = options.map((item) => item.staffid)
        this.value1 = Array.from(new Set(value1))
        this.multipleSelection = val
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (!this.options) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('projectManages', this.options)
        this.dialogVisible = false
        this.options = []
        this.value1 = []
      },
      async fetchData(node, resolve, orgId) {
        if (node && node.level === 0) {
          return
        }
        const res1 = await findOrganizationByTreeAllss({
          nodeId: !node ? '' : node.data.id,
        })
        if (node && node.level === 1) {
          resolve(
            this.formatTree(res1)[0].children.concat(...this.data[0].children)
          )
          return
        }
        // this.loading = true
        findOrganizationData({ nodeId: !node ? '' : node.data.id })
          .then((res) => {
            const tree = this.formatTree(res)
            if (node && node.level > 0) {
              resolve(
                this.formatTree(res1)[0].children.concat(...tree[0].children)
              )
              return
            }
            if (!node) {
              this.data = tree
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      // fetchData(node, resolve, orgId) {
      //   if (node && node.level === 0) {
      //     return
      //   }
      //   this.loading = true
      //   // {nodeId: !node ? this.currentOrg.id : node.data.id,}
      //   getTreeData({ nodeId: !node ? '' : node.data.id })
      //     .then((res) => {
      //       const tree = this.formatTree(res.data.data)
      //       if (node && node.level > 0) {
      //         resolve(tree)
      //         return
      //       }
      //       if (!node) {
      //         this.data = tree
      //       }
      //     })
      //     .finally(() => {
      //       this.loading = false
      //     })
      // },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            isLeaf: !i.isParent && !(i.name.indexOf('有限公司') > -1),
            parentId: i.pId || '',
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
    width: 100%;
  }

  .lr-layout > .left {
    width: 300px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
