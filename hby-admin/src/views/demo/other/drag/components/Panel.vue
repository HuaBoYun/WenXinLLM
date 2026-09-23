<template>
  <div v-if="easyFlowVisible" style="min-width: 1500px; overflow: hidden">
    <el-row>
      <!--顶部工具菜单-->
      <el-col :span="24">
        <div class="vab-tooltar">
          <el-button icon="el-icon-plus" @click="zoomAdd">放大</el-button>
          <el-button icon="el-icon-minus" @click="zoomSubtract">缩小</el-button>
          <el-button @click="downloadData">
            <vab-icon icon="download-2-line" />
            下载
          </el-button>
          <el-button :disabled="!activeElement.type" @click="deleteElement">
            <vab-icon icon="delete-bin-4-line" />
            删除
          </el-button>
        </div>
      </el-col>
    </el-row>
    <div style="display: flex; height: calc(100vh - 300px)">
      <node-menu ref="nodeMenu" @add-node="addNode" />

      <div
        id="efContainer"
        ref="efContainer"
        v-flowDrag
        class="content"
        style="overflow: hidden"
      >
        <template v-for="node in data.nodeList">
          <node
            :id="node.id"
            :key="node.id"
            :active-element="activeElement"
            :node="node"
            @change-node-site="changeNodeSite"
            @click-node="clickNode"
            @nodeRightMenu="nodeRightMenu"
          />
        </template>
      </div>
      <!-- 右侧表单 -->
      <div style="padding-top: 20px">
        <node-form
          ref="nodeForm"
          @repaintEverything="repaintEverything"
          @set-line-label="setLineLabel"
        />
      </div>
    </div>
  </div>
</template>

<script>
  import { jsPlumb } from 'jsplumb'
  import { easyFlowMixin } from './mixins'
  import Node from './Node'
  import NodeMenu from './NodeMenu'
  import NodeForm from './NodeForm'
  import lodash from 'lodash'
  import { getData } from './data'

  export default {
    components: { NodeMenu, Node, NodeForm },
    directives: {
      flowDrag: {
        bind(el, binding) {
          if (!binding) {
            return
          }
          el.onmousedown = (e) => {
            if (e.button === 2) {
              // 右键不管
              return
            }
            // 鼠标按下，计算当前原始距离可视区的高度
            let disX = e.clientX
            let disY = e.clientY
            el.style.cursor = 'move'

            document.onmousemove = function (e) {
              // 移动时禁止默认事件
              e.preventDefault()
              const left = e.clientX - disX
              disX = e.clientX
              el.scrollLeft += -left

              const top = e.clientY - disY
              disY = e.clientY
              el.scrollTop += -top
            }

            document.onmouseup = function () {
              el.style.cursor = 'auto'
              document.onmousemove = null
              document.onmouseup = null
            }
          }
        },
      },
    },
    // 一些基础配置移动该文件中
    mixins: [easyFlowMixin],
    data() {
      return {
        // jsPlumb 实例
        jsPlumb: null,
        // 控制画布销毁
        easyFlowVisible: true,
        // 控制流程数据显示与隐藏
        flowInfoVisible: false,
        // 是否加载完毕标志位
        loadEasyFlowFinish: false,
        flowHelpVisible: false,
        // 数据
        data: {},
        // 激活的元素、可能是节点、可能是连线
        activeElement: {
          // 可选值 node 、line
          type: undefined,
          // 节点ID
          nodeId: undefined,
          // 连线ID
          sourceId: undefined,
          targetId: undefined,
        },
        zoom: 0.5,
      }
    },
    mounted() {
      this.jsPlumb = jsPlumb.getInstance()
      this.dataReload(getData())
    },
    methods: {
      // 返回唯一标识
      getUUID() {
        return Math.random().toString(36).substr(3, 10)
      },
      jsPlumbInit() {
        this.jsPlumb.ready(() => {
          // 导入默认配置
          this.jsPlumb.importDefaults(this.jsplumbSetting)
          // 会使整个jsPlumb立即重绘。
          this.jsPlumb.setSuspendDrawing(false, true)
          // 初始化节点
          this.loadEasyFlow()
          // 单点击了连接线, https://www.cnblogs.com/ysx215/p/7615677.html
          this.jsPlumb.bind('click', (conn) => {
            this.activeElement.type = 'line'
            this.activeElement.sourceId = conn.sourceId
            this.activeElement.targetId = conn.targetId
            this.$refs.nodeForm.lineInit({
              from: conn.sourceId,
              to: conn.targetId,
              label: conn.getLabel(),
            })
          })
          // 连线
          this.jsPlumb.bind('connection', (evt) => {
            const from = evt.source.id
            const to = evt.target.id
            if (this.loadEasyFlowFinish) {
              this.data.lineList.push({ from: from, to: to })
            }
          })

          // 删除连线回调
          this.jsPlumb.bind('connectionDetached', (evt) => {
            this.deleteLine(evt.sourceId, evt.targetId)
          })

          // 改变线的连接节点
          this.jsPlumb.bind('connectionMoved', (evt) => {
            this.changeLine(evt.originalSourceId, evt.originalTargetId)
          })

          // 连线右击
          this.jsPlumb.bind('contextmenu', () => {
            //
          })

          // 连线
          this.jsPlumb.bind('beforeDrop', (evt) => {
            const from = evt.sourceId
            const to = evt.targetId
            if (from === to) {
              this.$message.error('节点不支持连接自己')
              return false
            }
            if (this.hasLine(from, to)) {
              this.$message.error('该关系已存在,不允许重复创建')
              return false
            }
            if (this.hashOppositeLine(from, to)) {
              this.$message.error('不支持两个节点之间连线回环')
              return false
            }
            this.$message.success('连接成功')
            return true
          })

          // beforeDetach
          this.jsPlumb.bind('beforeDetach', () => {
            //
          })
          this.jsPlumb.setContainer(this.$refs.efContainer)
        })
      },
      // 加载流程图
      loadEasyFlow() {
        // 初始化节点
        for (let i = 0; i < this.data.nodeList.length; i++) {
          const node = this.data.nodeList[i]
          // 设置源点，可以拖出线连接其他节点
          this.jsPlumb.makeSource(
            node.id,
            lodash.merge(this.jsplumbSourceOptions, {})
          )
          // // 设置目标点，其他源点拖出的线可以连接该节点
          this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
          if (!node.viewOnly) {
            this.jsPlumb.draggable(node.id, {
              containment: 'parent',
              stop: function () {
                // 拖拽节点结束后的对调
                //
              },
            })
          }
        }
        // 初始化连线
        for (let i = 0; i < this.data.lineList.length; i++) {
          const line = this.data.lineList[i]
          const connParam = {
            source: line.from,
            target: line.to,
            label: line.label ? line.label : '',
            connector: line.connector ? line.connector : '',
            anchors: line.anchors ? line.anchors : undefined,
            paintStyle: line.paintStyle ? line.paintStyle : undefined,
          }
          this.jsPlumb.connect(connParam, this.jsplumbConnectOptions)
        }
        this.loadEasyFlowFinish = true
      },
      // 设置连线条件
      setLineLabel(from, to, label) {
        const conn = this.jsPlumb.getConnections({
          source: from,
          target: to,
        })[0]
        if (!label || label === '') {
          conn.removeClass('flowLabel')
          conn.addClass('emptyFlowLabel')
        } else {
          conn.addClass('flowLabel')
        }
        conn.setLabel({
          label: label,
        })
        this.data.lineList.forEach(function (line) {
          if (line.from === from && line.to === to) {
            line.label = label
          }
        })
      },
      // 删除激活的元素
      deleteElement() {
        if (this.activeElement.type === 'node') {
          this.deleteNode(this.activeElement.nodeId)
        } else if (this.activeElement.type === 'line') {
          this.$confirm('确定删除所点击的线吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })
            .then(() => {
              const conn = this.jsPlumb.getConnections({
                source: this.activeElement.sourceId,
                target: this.activeElement.targetId,
              })[0]
              this.jsPlumb.deleteConnection(conn)
            })
            .catch(() => {})
        }
      },
      // 删除线
      deleteLine(from, to) {
        this.data.lineList = this.data.lineList.filter(function (line) {
          return !(line.from === from && line.to === to)
        })
      },
      // 改变连线
      changeLine(oldFrom, oldTo) {
        this.deleteLine(oldFrom, oldTo)
      },
      // 改变节点的位置
      changeNodeSite(data) {
        for (let i = 0; i < this.data.nodeList.length; i++) {
          const node = this.data.nodeList[i]
          if (node.id === data.nodeId) {
            node.left = data.left
            node.top = data.top
          }
        }
      },
      /**
       * 拖拽结束后添加新的节点
       * @param evt
       * @param nodeMenu 被添加的节点对象
       * @param mousePosition 鼠标拖拽结束的坐标
       */
      addNode(evt, nodeMenu) {
        const screenX = evt.originalEvent.clientX,
          screenY = evt.originalEvent.clientY
        const efContainer = this.$refs.efContainer
        const containerRect = efContainer.getBoundingClientRect()
        let left = screenX,
          top = screenY
        // 计算是否拖入到容器中
        if (
          left < containerRect.x ||
          left > containerRect.width + containerRect.x ||
          top < containerRect.y ||
          containerRect.y > containerRect.y + containerRect.height
        ) {
          this.$message.error('请把节点拖入到画布中')
          return
        }
        left = left - containerRect.x + efContainer.scrollLeft
        top = top - containerRect.y + efContainer.scrollTop
        // 居中
        left -= 85
        top -= 16
        const nodeId = this.getUUID()
        // 动态生成名字
        const origName = nodeMenu.name
        let nodeName = origName
        let index = 1
        while (index < 10000) {
          let repeat = false
          for (let i = 0; i < this.data.nodeList.length; i++) {
            const node = this.data.nodeList[i]
            if (node.name === nodeName) {
              nodeName = origName + index
              repeat = true
            }
          }
          if (repeat) {
            index++
            continue
          }
          break
        }
        const node = {
          id: nodeId,
          name: nodeName,
          type: nodeMenu.type,
          left: left + 'px',
          top: top + 'px',
          ico: nodeMenu.ico,
          state: 'success',
        }
        /**
         * 这里可以进行业务判断、是否能够添加该节点
         */
        this.data.nodeList.push(node)
        this.$nextTick(() => {
          this.jsPlumb.makeSource(nodeId, this.jsplumbSourceOptions)
          this.jsPlumb.makeTarget(nodeId, this.jsplumbTargetOptions)
          this.jsPlumb.draggable(nodeId, {
            containment: 'parent',
            stop: function () {
              // 拖拽节点结束后的对调
              //
            },
          })
        })
      },
      /**
       * 删除节点
       * @param nodeId 被删除节点的ID
       */
      deleteNode(nodeId) {
        this.$confirm('确定要删除节点' + nodeId + '?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          closeOnClickModal: false,
        })
          .then(() => {
            /**
             * 这里需要进行业务判断，是否可以删除
             */
            this.data.nodeList = this.data.nodeList.filter(function (node) {
              // 伪删除，将节点隐藏，否则会导致位置错位
              return node.id !== nodeId
            })
            this.$nextTick(() => {
              this.jsPlumb.removeAllEndpoints(nodeId)
            })
          })
          .catch(() => {})
        return true
      },
      clickNode(nodeId) {
        this.activeElement.type = 'node'
        this.activeElement.nodeId = nodeId
        this.$refs.nodeForm.nodeInit(this.data, nodeId)
      },
      // 是否具有该线
      hasLine(from, to) {
        for (let i = 0; i < this.data.lineList.length; i++) {
          const line = this.data.lineList[i]
          if (line.from === from && line.to === to) {
            return true
          }
        }
        return false
      },
      // 是否含有相反的线
      hashOppositeLine(from, to) {
        return this.hasLine(to, from)
      },
      nodeRightMenu(nodeId, evt) {
        this.menu.show = true
        this.menu.curNodeId = nodeId
        this.menu.left = evt.x + 'px'
        this.menu.top = evt.y + 'px'
      },
      repaintEverything() {
        this.jsPlumb.repaint()
      },
      // 加载流程图
      dataReload(data) {
        this.easyFlowVisible = false
        this.data.nodeList = []
        this.data.lineList = []
        data = lodash.cloneDeep(data)
        this.easyFlowVisible = true
        this.data = data
        this.$nextTick(() => {
          this.jsPlumb = jsPlumb.getInstance()
          this.jsPlumbInit()
        })
      },
      zoomAdd() {
        if (this.zoom >= 1) {
          return
        }
        this.zoom = this.zoom + 0.1
        this.$refs.efContainer.style.transform = `scale(${this.zoom})`
        this.jsPlumb.setZoom(this.zoom)
      },
      zoomSubtract() {
        if (this.zoom <= 0) {
          return
        }
        this.zoom = this.zoom - 0.1
        this.$refs.efContainer.style.transform = `scale(${this.zoom})`
        this.jsPlumb.setZoom(this.zoom)
      },
      // 下载数据
      downloadData() {
        this.$confirm('确定要下载该流程数据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          closeOnClickModal: false,
        })
          .then(() => {
            const datastr =
              'data:text/json;charset=utf-8,' +
              encodeURIComponent(JSON.stringify(this.data, null, '\t'))
            const downloadAnchorNode = document.createElement('a')
            downloadAnchorNode.setAttribute('href', datastr)
            downloadAnchorNode.setAttribute('download', 'data.json')
            downloadAnchorNode.click()
            downloadAnchorNode.remove()
            this.$message.success('正在下载中,请稍后...')
          })
          .catch(() => {})
      },
    },
  }
</script>
