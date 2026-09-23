<template>
  <div class="workflow-container">
    <!-- 辅助工具栏 -->
    <Control v-if="lf" class="vab-control" :lf="lf" @cat-data="$_catData" />
    <!-- 节点面板 -->
    <NodePanel :lf="lf" />
    <!-- 画布 -->
    <div id="left-view"></div>
    <!-- 用户节点自定义操作面板 -->
    <AddPanel
      v-if="showAddPanel"
      class="add-panel"
      :lf="lf"
      :node-data="addClickNode"
      :style="addPanelStyle"
      @addNodeFinish="hideAddPanel"
    />
    <!-- 属性面板 -->
    <el-drawer
      :before-close="closeDialog"
      direction="rtl"
      size="400px"
      title="设置节点属性"
      :visible.sync="dialogVisible"
    >
      <PropertyDialog
        v-if="dialogVisible"
        :lf="lf"
        :node-data="clickNode"
        @setPropertiesFinish="closeDialog"
      />
    </el-drawer>
    <!-- 数据查看面板 -->
    <el-dialog
      title="数据"
      :visible.sync="dataVisible"
      width="50%"
      :close-on-click-modal="false"
    >
      <DataDialog :graph-data="graphData" />
    </el-dialog>
  </div>
</template>

<script>
  import { getList } from '@/api/workflow'

  import LogicFlow from '@logicflow/core'
  import '@logicflow/core/dist/style/index.css'
  import { Menu, Snapshot } from '@logicflow/extension'
  import '@logicflow/extension/lib/style/index.css'
  import AddPanel from './components/lFComponents/AddPanel'
  import Control from './components/lFComponents/Control'
  import DataDialog from './components/lFComponents/DataDialog'
  import NodePanel from './components/lFComponents/NodePanel'
  import PropertyDialog from './components/propertySetting/PropertyDialog'

  import {
    registerDownload,
    registerEnd,
    registerPolyline,
    registerPush,
    registerStart,
    registerUser,
  } from './components/registerNode'

  export default {
    name: 'Workflow',
    components: { NodePanel, AddPanel, Control, PropertyDialog, DataDialog },
    data() {
      return {
        data: [],
        lf: null,
        showAddPanel: false,
        addPanelStyle: {
          top: 0,
          left: 0,
        },
        addClickNode: null,
        clickNode: null,
        dialogVisible: false,
        graphData: null,
        dataVisible: false,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const { data } = await getList()
        this.data = data
        this.$nextTick(() => {
          this.$_initLf()
        })
      },
      $_initLf() {
        const _this = this
        // 画布配置
        const config = {
          container: document.querySelector('#left-view'),
          background: {
            color: '#f7f9ff',
          },
          grid: {
            size: 10,
            visible: false,
          },
          keyboard: {
            enabled: true,
          },
          edgeTextDraggable: true,
          guards: {
            beforeClone(data) {
              return true
            },
            beforeDelete(data) {
              // 可以根据data数据判断是否允许删除，允许返回true,不允许返回false
              // 文档： http://logic-flow.org/guide/basic/keyboard.html#%E5%A6%82%E4%BD%95%E9%98%BB%E6%AD%A2%E5%88%A0%E9%99%A4%E6%88%96%E8%80%85%E6%8B%B7%E8%B4%9D%E8%A1%8C%E4%B8%BA

              // _this.$message('不允许删除', 'error')
              return true
            },
          },
        }
        // 使用插件
        LogicFlow.use(Menu)
        LogicFlow.use(Snapshot)
        const lf = new LogicFlow({ ...config })
        this.lf = lf
        // 菜单配置文档：http://logic-flow.org/guide/extension/extension-components.html#%E8%8F%9C%E5%8D%95
        // 重置，增加，节点自由配置(以user节点为示例)
        lf.setMenuConfig({
          nodeMenu: [],
          edgeMenu: [],
        })
        lf.addMenuConfig({
          nodeMenu: [
            {
              text: '分享',
              callback() {
                _this.$baseAlert('分享成功！')
              },
            },
            {
              text: '属性',
              callback(node) {
                _this.$baseAlert(`
                节点id：${node.id}
                节点类型：${node.type}
                节点坐标：(x: ${node.x}, y: ${node.y})`)
              },
            },
          ],
          edgeMenu: [
            {
              text: '属性',
              callback(edge) {
                _this.$baseAlert(`
                边id：${edge.id}
                边类型：${edge.type}
                边坐标：(x: ${edge.x}, y: ${edge.y})
                源节点id：${edge.sourceNodeId}
                目标节点id：${edge.targetNodeId}`)
              },
            },
          ],
        })
        // 设置主题
        lf.setTheme({
          circle: {
            r: 20,
            stroke: '#000000',
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          rect: {
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          polygon: {
            strokeWidth: 1,
          },
          polyline: {
            stroke: '#000000',
            hoverStroke: '#000000',
            selectedStroke: '#000000',
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          nodeText: {
            color: '#000000',
          },
          edgeText: {
            color: '#000000',
            background: {
              fill: '#f7f9ff',
            },
          },
        })
        this.$_registerNode()
      },
      // 自定义
      $_registerNode() {
        registerStart(this.lf)
        registerUser(this.lf)
        registerEnd(this.lf)
        registerPush(this.lf, this.clickPlus, this.mouseDownPlus)
        registerDownload(this.lf)
        registerPolyline(this.lf)
        this.$_render()
      },
      $_render() {
        this.lf.render(this.data)
        this.$_LfEvent()
      },
      $_getData() {
        const data = this.lf.getGraphData()
      },
      $_LfEvent() {
        this.lf.on('node:click', ({ data }) => {
          this.clickNode = data
          this.dialogVisible = true
        })
        this.lf.on('edge:click', ({ data }) => {
          this.clickNode = data
          this.dialogVisible = true
        })
        this.lf.on('element:click', () => {
          this.hideAddPanel()
        })
        this.lf.on('blank:click', () => {
          this.hideAddPanel()
        })
        this.lf.on('connection:not-allowed', (data) => {
          this.$message({
            type: 'error',
            message: data.msg,
          })
        })
        this.lf.on('node:mousemove', () => {})
      },
      clickPlus(e, attributes) {
        e.stopPropagation()

        const { clientX, clientY } = e

        this.addPanelStyle.top = clientY - 40 + 'px'
        this.addPanelStyle.left = clientX + 'px'
        this.showAddPanel = true
        this.addClickNode = attributes
      },
      mouseDownPlus(e, attributes) {
        e.stopPropagation()
      },
      hideAddPanel() {
        this.showAddPanel = false
        this.addPanelStyle.top = 0
        this.addPanelStyle.left = 0
        this.addClickNode = null
      },
      closeDialog() {
        this.dialogVisible = false
      },
      $_catData() {
        this.graphData = this.lf.getGraphData()
        this.dataVisible = true
      },
    },
  }
</script>

<style lang="scss" scoped>
  .workflow-container {
    position: relative;

    .vab-control {
      position: absolute;
      top: $base-margin * 2;
      left: $base-margin * 2;
      z-index: 2;
    }

    #left-view {
      height: $base-keep-alive-height;
      outline: none;
    }

    .time-plus {
      cursor: pointer;
    }

    .add-panel {
      position: absolute;
      z-index: 11;
    }
  }
</style>
