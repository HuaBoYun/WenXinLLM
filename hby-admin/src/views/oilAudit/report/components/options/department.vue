<!--
 * @Date: 2022-03-28 15:04:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 17:05:56
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/department.vue
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
    <el-tree
      ref="tree"
      :check-strictly="true"
      :data="data"
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      :load="fetchData"
      lazy
      default-expand-all
      @check-change="handleCheckChange"
      @node-click="handleNodeClick"
    />
  </el-dialog>
</template>

<script>
  import { getDepartmentOptions } from '@/api/contract/manage'
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        title: '部门',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
          value: 'id',
          isLeaf: 'isLeaf',
        },
        data: [],
        multipleSelection: null,
        clickTimer: null,  // 计时器
        lastClickTime: 0,  // 上次点击时间
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogTreeVisible = true
        // this.fetchTree()
        this.fetchData()
      },
      async fetchTree() {
        const res = await getDepartmentOptions()
        this.data = res
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
      handleNodeClick(data) {
        console.log(data)
        this.multipleSelection = data

        const now = Date.now();
        // 判断是否为双击（300ms内）
        if (now - this.lastClickTime < 300) {
          clearTimeout(this.clickTimer);
          this.lastClickTime = 0;
          console.log('双击节点:', data);
          this.confirm();
        } else {
          this.lastClickTime = now;
          this.clickTimer = setTimeout(() => {
            // 处理单击事件（如果需要）
            this.lastClickTime = 0;
          }, 300);
        }
      },
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
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
      //提交
      confirm() {
        if (!this.multipleSelection) {
          this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.close()
        // const checked = this.$refs['tree'].getCurrentNode()
        // this.$emit('selected', checked)
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
