<template>
  <div class="bpmn-viewer-container">
    <div ref="canvas" class="bpmn-canvas"></div>
  </div>
</template>

<script>
  import NavigatedViewer from 'bpmn-js/lib/NavigatedViewer'
  import 'bpmn-js/dist/assets/diagram-js.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'

  export default {
    name: 'BpmnViewer',
    props: {
      // URL 编码的 BPMN XML 字符串（来自 flowInfo.flowXml）
      flowXml: {
        type: String,
        default: '',
      },
      // 节点进度列表（来自 dataJson.progressList）
      // 每项: { nodeCode, nodeStatus, nodeType }
      // nodeStatus: 1=已完成, 4=进行中, 其他=未处理
      progressList: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        viewer: null,
      }
    },
    watch: {
      flowXml(val) {
        if (val) this.renderDiagram()
      },
      progressList() {
        this.highlightNodes()
      },
    },
    mounted() {
      this.initViewer()
    },
    beforeDestroy() {
      if (this.viewer) {
        this.viewer.destroy()
        this.viewer = null
      }
    },
    methods: {
      initViewer() {
        this.viewer = new NavigatedViewer({
          container: this.$refs.canvas,
        })
        if (this.flowXml) {
          this.renderDiagram()
        }
      },

      async renderDiagram() {
        if (!this.viewer || !this.flowXml) return
        try {
          // flowXml 是 URL 编码的，先解码
          const xmlStr = decodeURIComponent(this.flowXml)
          await this.viewer.importXML(xmlStr)
          // 自适应视口
          const canvas = this.viewer.get('canvas')
          canvas.zoom('fit-viewport')
          // 高亮节点
          this.highlightNodes()
        } catch (err) {
          console.error('BPMN 渲染失败:', err)
        }
      },

      highlightNodes() {
        if (!this.viewer || !this.progressList || !this.progressList.length) return
        const canvas = this.viewer.get('canvas')
        this.progressList.forEach((node) => {
          if (!node.nodeCode) return
          try {
            // nodeStatus: 1=已完成(绿), 4=进行中(蓝), 其他=默认
            if (node.nodeStatus === 1) {
              canvas.addMarker(node.nodeCode, 'highlight-done')
            } else if (node.nodeStatus === 4) {
              canvas.addMarker(node.nodeCode, 'highlight-current')
            }
          } catch (e) {
            // 节点不存在时忽略
          }
        })
      },
    },
  }
</script>

<style scoped>
  .bpmn-viewer-container {
    width: 100%;
    height: 500px;
    background: #fff;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    overflow: hidden;
  }
  .bpmn-canvas {
    width: 100%;
    height: 100%;
  }
</style>

<style>
  /* 已完成节点 - 绿色 */
  .highlight-done .djs-visual > :nth-child(1) {
    stroke: #67c23a !important;
    fill: #f0f9eb !important;
  }
  /* 进行中节点 - 蓝色 */
  .highlight-current .djs-visual > :nth-child(1) {
    stroke: #1890ff !important;
    fill: #e6f7ff !important;
  }
</style>
