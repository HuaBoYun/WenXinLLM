<template>
  <div class="containers">
    <div ref="canvas" class="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
    <div class="buttons">
      <!--      <button @click="saveDiagram()">下载XML</button>-->
    </div>
  </div>
</template>
<script>
  // 以下为bpmn工作流绘图工具的样式
  // 左边工具栏以及编辑节点的样式
  import 'bpmn-js/dist/assets/diagram-js.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
  import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
  // 右边工具栏样式
  import 'bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css'

  // 引入Bpmn相关的依赖
  import BpmnModeler from 'bpmn-js/lib/Modeler'
  // 这里引入的是右侧属性栏这个框
  import propertiesPanelModule from 'bpmn-js-properties-panel'
  // 而这个引入的是右侧属性栏里的内容
  import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
  // 右侧属性栏扩展，不然报错
  import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
  // 汉化组件
  import customTranslate from './customTranslate/customTranslate'
  var customTranslateModule = {
    translate: ['value', customTranslate],
  }
  import { defaultXmlStr } from './defaultXmlStr'
  export default {
    name: 'BpmnModeler',
    data() {
      return {
        // bpmn建模器
        bpmnModeler: null,
        container: null,
        canvas: null,
        xmlStr: '',
      }
    },
    mounted() {
      this.init()
    },
    // 方法集合
    methods: {
      async init() {
        this.xmlStr = await this.getXmlUrl()
        this.$nextTick(() => {
          this.initBpmn()
        })
      },
      getXmlUrl() {
        return new Promise((resolve) => {
          setTimeout(() => {
            // const url = 'https://hexo-blog-1256114407.cos.ap-shenzhen-fsi.myqcloud.com/mock1.bpmn'
            const url = ''
            resolve(url)
          }, 1000)
        })
      },
      initBpmn() {
        // 获取到属性ref为“canvas”的dom节点
        const canvas = this.$refs.canvas
        // 建模
        this.bpmnModeler = new BpmnModeler({
          container: canvas,
          //添加左侧控制板
          propertiesPanel: {
            parent: '#js-properties-panel',
          },
          additionalModules: [
            //添加右侧属性面板
            propertiesProviderModule,
            propertiesPanelModule,
            //汉化
            customTranslateModule,
          ],
          moddleExtensions: {
            //如果要在属性面板中修改属性，必须添加
            camunda: camundaModdleDescriptor,
          },
        })
        this.createDiagram()
      },
      async createDiagram() {
        if (this.xmlStr === '') {
          this.xmlStr = defaultXmlStr
        }
        try {
          const result = await this.bpmnModeler.importXML(this.xmlStr)
          const { warnings } = result
        } catch (err) {}
        // 让图能自适应屏幕
        var canvas = this.bpmnModeler.get('canvas')
        canvas.zoom('fit-viewport')
      },
      // 下载为bpmn格式,done是个函数，调用的时候传入的
      async saveDiagram() {
        try {
          const result = await this.bpmnModeler.saveXML({
            format: true,
          })
          const { xml } = result
        } catch (err) {}
      },
    },
  }
</script>
<style scoped>
  .containers {
    background-color: #ffffff;
    width: 100%;
    height: calc(100vh - 220px);
  }

  .canvas {
    width: 100%;
    height: 100%;
  }

  .panel {
    position: absolute;
    right: 20px;
    top: 40px;
    width: 300px;
    height: calc(100vh - 300px);
    overflow-y: scroll;
  }

  .buttons {
    position: absolute;
    right: 350px;
    top: 120px;
  }
</style>
