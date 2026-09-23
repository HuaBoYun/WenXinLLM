<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-07-20 23:13:27
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-21 22:34:06
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\options\sealDepartment.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
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
      :props="defaultProps"
      show-checkbox
      :check-strictly="true"
      v-loading="loading"
      :default-expanded-keys="expandedKeys"
      :default-expand-all="false"
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      lazy
      :load="fetchData"
      @node-click="handleNodeClick"
    />
    <!-- 123
    <vab-tree></vab-tree> -->
  </el-dialog>
</template>

<script>
  import { findOrganization } from '@/api/setting/org'
  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        loading: true,
        title: '公司授权',
        keys: [],
        dialogTreeVisible: false,
        checkbox: true,
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
          disabled: 'disabled',
        },
        data: [],
        expandedKeys: [],
        queryForm: {
          roleId: '',
          orgId: '',
        },
        orgName: '',
      }
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
        this.orgName = '' //清空搜索框
        this.$refs.treeDialog.$data.store.lazy = true // 开启懒加载
        this.fetchData()
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
      async showEdit(rid) {
        this.dialogTreeVisible = true
        this.queryForm.roleId = rid
        this.fetchData()
      },
      async fetchData(node, resolve) {
        if (this.queryForm.roleId == '') {
          return
        }
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
        findOrganization({
          roleId: this.queryForm.roleId,
          fatherorgid: !node ? '' : node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            const tree = this.formatTree(res.data)
            console.log('tree:data', res.data)
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
            disabled: i.disabled,
          }
        })
        return list
      },
      handleNodeClick(data) {},
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.$refs['treeDialog'].getCheckedNodes()
        this.$emit('selected', checked)
        this.dialogTreeVisible = false
        this.keys = []
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
