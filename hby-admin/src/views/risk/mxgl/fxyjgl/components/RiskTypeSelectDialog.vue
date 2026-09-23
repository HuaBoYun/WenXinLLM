<template>
  <el-dialog
    title="选择风险类型"
    :visible.sync="visible"
    width="500px"
    :close-on-click-modal="false"
    append-to-body
    @close="handleClose"
  >
    <div class="risk-type-select-dialog">
      <p class="tip-text">请选择要创建风险的最下级节点：</p>
      <div class="tree-wrapper" v-loading="loading">
        <el-tree
          ref="riskTree"
          :data="treeData"
          :props="defaultProps"
          :highlight-current="true"
          :expand-on-click-node="false"
          :default-expanded-keys="defaultExpandedKeys"
          node-key="value"
          @node-click="handleNodeClick"
        >
          <span slot-scope="{ node, data }" :class="{ 'is-disabled-node': data.children && data.children.length > 0 }">
            <i :class="data.children && data.children.length > 0 ? 'el-icon-folder' : 'el-icon-document'" style="margin-right: 4px;" />
            <span>{{ node.label }}</span>
          </span>
        </el-tree>
      </div>
      <div v-if="selectedNode" class="selected-info">
        <el-tag type="success">已选择：{{ selectedNode.label }}</el-tag>
      </div>
      <div v-if="nonLeafClicked" class="selected-info">
        <el-tag type="warning">该节点下还有子分类，请选择最下级节点</el-tag>
      </div>
    </div>
    <template slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :disabled="!selectedNode" @click="handleConfirm">
        确 定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getCreationTreeData } from '@/api/risk'
  import { formatOptions } from '@/utils/validate'

  export default {
    name: 'RiskTypeSelectDialog',
    data() {
      return {
        visible: false,
        loading: false,
        treeData: [],
        rawTreeData: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        defaultExpandedKeys: [],
        selectedNode: null,
        selectedRawNode: null,
        nonLeafClicked: false,
      }
    },
    methods: {
      /** 打开弹窗并加载树数据 */
      open() {
        this.visible = true
        this.selectedNode = null
        this.selectedRawNode = null
        this.nonLeafClicked = false
        this.fetchTreeData()
      },
      /** 加载风险类型树 */
      async fetchTreeData() {
        this.loading = true
        try {
          const result = await getCreationTreeData({})
          if (result.data && result.data.tree && result.data.tree.length > 0) {
            this.rawTreeData = result.data.tree
            this.treeData = formatOptions(result.data.tree, 'riskcatname', 'riskcatid')
            this.defaultExpandedKeys = [result.data.tree[0].riskcatid]
          } else {
            this.treeData = []
            this.rawTreeData = []
          }
        } catch (error) {
          console.error('加载风险类型树失败:', error)
          this.$message.error('加载风险类型树失败')
        } finally {
          this.loading = false
        }
      },
      /** 节点点击 - 只有叶子节点可选 */
      handleNodeClick(data, node) {
        if (data.children && data.children.length > 0) {
          // 非叶子节点：不选中，展开/收起
          this.nonLeafClicked = true
          this.selectedNode = null
          node.expanded = !node.expanded
          return
        }
        // 叶子节点：选中
        this.nonLeafClicked = false
        this.selectedNode = data
        this.selectedRawNode = this.findRawNode(this.rawTreeData, data.value)
      },
      /** 在原始树数据中递归查找节点 */
      findRawNode(tree, riskcatid) {
        for (const node of tree) {
          if (node.riskcatid === riskcatid) return node
          if (node.children && node.children.length > 0) {
            const found = this.findRawNode(node.children, riskcatid)
            if (found) return found
          }
        }
        return null
      },
      /** 在原始树数据中递归查找父节点 */
      findParentNode(tree, riskcatid, parent = null) {
        for (const node of tree) {
          if (node.riskcatid === riskcatid) return parent
          if (node.children && node.children.length > 0) {
            const found = this.findParentNode(node.children, riskcatid, node)
            if (found) return found
          }
        }
        return null
      },
      /** 确认选择 */
      handleConfirm() {
        if (!this.selectedNode) {
          this.$message.warning('请先选择一个分类节点')
          return
        }
        // 检查是否为叶子节点
        if (this.selectedNode.children && this.selectedNode.children.length > 0) {
          this.$message.error('请选择最下级节点！')
          return
        }
        // 构建与 creation/index.vue handleAdd 一致的 resultData
        const nodeInfo = this.selectedNode
        const parentNode = this.findParentNode(this.rawTreeData, nodeInfo.value, null)
        const resultData = { ...nodeInfo }
        if (parentNode && parentNode.riskcatname === '风险类型') {
          resultData.riskcatname = nodeInfo.label
          resultData.riskcatnametwo = ''
          resultData.fatherriskcatid = parentNode.riskcatid
          resultData.fatherriskcatname = parentNode.riskcatname
        } else {
          resultData.fatherriskcatid = parentNode ? parentNode.riskcatid : 0
          resultData.fatherriskcatname = parentNode ? parentNode.riskcatname : ''
          resultData.riskcatname = parentNode ? parentNode.riskcatname : ''
          resultData.riskcatnametwo = nodeInfo.label
        }
        this.$emit('confirm', resultData)
        this.visible = false
      },
      handleClose() {
        this.visible = false
        this.selectedNode = null
      },
    },
  }
</script>

<style lang="scss" scoped>
  .risk-type-select-dialog {
    .tip-text {
      margin-bottom: 12px;
      color: #606266;
      font-size: 14px;
    }
    .tree-wrapper {
      max-height: 400px;
      overflow-y: auto;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      padding: 10px;
    }
    .selected-info {
      margin-top: 12px;
    }
    .is-disabled-node {
      color: #909399;
      cursor: default;
    }
  }
</style>
