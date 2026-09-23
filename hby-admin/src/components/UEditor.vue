<template>
  <div>
    <vue-ueditor-wrap
      ref="editor"
      v-model="currentValue"
      :config="config"
      @ready="ready"
      @beforeInit="initCustomBtn"
    />
    <UEditorTemplate @selectTemp="selectTemp" ref="template" />
    <UEditorTemplateSave ref="templateSave" />
    <UEditorAnnotation ref="annotation" />
    <UEditorRisk
      ref="risklist"
      v-if="isCopyRisk"
      @insertContent="handleInsertContent"
    />
    <UEditorReport ref="report" v-if="isReport" />
  </div>
</template>
<script>
  import VueUeditorWrap from 'vue-ueditor-wrap'
  import UEditorTemplate from '@/components/UEditor/UEditorTemplate'
  import UEditorTemplateSave from '@/components/UEditor/UEditorTemplateSave'
  import UEditorAnnotation from '@/components/UEditor/UEditorAnnotation'
  import UEditorRisk from '@/components/UEditor/UEditorRisk'
  import UEditorReport from '@/components/UEditor/UEditorReport'
  export default {
    components: {
      UEditorAnnotation,
      UEditorTemplateSave,
      UEditorTemplate,
      VueUeditorWrap,
      UEditorRisk,
      UEditorReport,
    },
    props: {
      templates: {
        type: Array,
        default: () => [],
      },
      value: {
        type: String,
        default: () => {},
      },
      height: {
        type: Number,
        default: 600,
      },
      disabled: {
        type: Boolean,
        default: false,
      },
      //风险报告-风险管理报告编辑器传入值,判断是否有查看风险详情列表
      isCopyRisk: {
        type: Boolean,
        default: false,
      },
      //内控报告-评价报告编制,判断是否有内控评价汇总
      isReport: {
        type: Boolean,
        default: false,
      },
      template: {
        type: String,
        default: '',
      },
    },
    created() {
      const info = localStorage.getItem('model')
      // if (info === 'znsj') {
      //   this.config.initialFrameWidth = 960
      // }
    },
    beforeDestroy() {
      if (this.editor) {
        this.editor.destroy()
        this.editor = null
      }
    },
    data: function () {
      return {
        currentValue: this.value,
        editor: null,
        config: {
          serverUrl: '',
          UEDITOR_HOME_URL: '/UEditor/',
          readonly: false,
          initialFrameHeight: this.height,
          autoFloatEnabled: false,
          maximumWords: 5000,
          // initialFrameWidth: 960,
        },
        loadOver: false,
      }
    },
    watch: {
      currentValue(val) {
        this.$emit('input', val)
      },
      value(val) {
        // console.warn('Ueditor,val', val)
        this.currentValue = val
      },
      templates(val) {
        console.warn('Ueditor,templates', val)
        if (this.editor) {
          this.editor.templates = val
        }
      },
      disabled(newVal) {
        if (newVal) {
          this.disableEditor()
        } else {
          this.enableEditor()
        }
      },
    },
    methods: {
      // 添加处理插入内容的方法
      handleInsertContent(html) {
        if (this.editor) {
          // 获取当前光标位置
          const range = this.editor.selection.getRange()
          // 插入HTML内容
          this.editor.execCommand('inserthtml', html)
          // 恢复光标位置
          range.collapse(false)
          range.select()
        }
      },
      ready(instance) {
        this.editor = instance
        // 确保在编辑器实例准备好后立即设置禁用状态
        this.$nextTick(() => {
          if (this.disabled) {
            this.disableEditor()
          } else {
            this.enableEditor()
          }
        })
        this.editor.templates = this.templates
      }, // 新增方法：禁用编辑器
      disableEditor() {
        if (this.editor) {
          this.editor.setDisabled()
          this.editor.body.contentEditable = false
          this.editor.body.setAttribute('contenteditable', false)
          this.config.readonly = true
          const toolbarBox = this.editor.ui.toolbarbox
          if (toolbarBox && toolbarBox.style) {
            toolbarBox.style.display = 'none'
          }
        }
      },
      // 新增方法：启用编辑器
      enableEditor() {
        if (this.editor) {
          this.editor.setEnabled()
          this.editor.body.contentEditable = true
          this.editor.body.setAttribute('contenteditable', true)
          this.config.readonly = false
          const toolbarBox = this.editor.ui.toolbarbox
          if (toolbarBox && toolbarBox.style) {
            toolbarBox.style.display = 'block'
          }
        }
      },
      initCustomBtn() {
        // console.log(window.UE.ui.Button(), 121231223112)
        if (this.template !== '') {
          this.initTemplate()
          this.initTemplateSave()
        }
        if (this.isCopyRisk) {
          this.copyRisk()
        }
        if (this.isReport) {
          this.copyReport()
        }
      },
      initTemplate() {
        //添加模板按钮
        let that = this
        window.UE.registerUI('template', function (editor, uiName) {
          return new window.UE.ui.Button({
            name: uiName,
            title: '模板', // 这里是设置当鼠标指向这个按扭时显示的文字
            cssRules: 'background-position: -339px -40px;',
            onclick: function () {
              that.$refs['template'].showDialog(that.template)
            },
          })
        })
      },
      initTemplateSave() {
        //添加保存模板按钮
        let that = this
        window.UE.registerUI('templatesave', function (editor, uiName) {
          return new window.UE.ui.Button({
            name: uiName,
            title: '保存模板', // 这里是设置当鼠标指向这个按扭时显示的文字
            cssRules:
              "background-image: url('/icons/save-template.png') !important; background-size: 100%;",
            onclick: function () {
              that.$refs['templateSave'].showDialog(
                that.template,
                that.editor.getContent()
              )
            },
          })
        })
      },
      initAnnotationEdit() {
        //添加保存模板按钮
        let that = this
        window.UE.registerUI('annotationedit', function (editor, uiName) {
          return new window.UE.ui.Button({
            name: uiName,
            title: '保存模板', // 这里是设置当鼠标指向这个按扭时显示的文字
            cssRules:
              "background-image: url('/icons/edit.png') !important; background-size: 100%;",
            onclick: function () {
              // that.$refs['templateSave'].showDialog(that.template, that.editor.getContent())
              let range = that.editor.selection.getRange()
              let fragment = range.cloneContents()

              if (fragment == null) {
                that.$message.warning('请选择内容')
                return
              }
              let node = document.createElement('p')
              node.appendChild(fragment)
              that.$refs['annotation'].showDialog(that.editor, node)
            },
          })
        })
      },

      copyRisk() {
        //添加保存模板按钮
        let that = this
        window.UE.registerUI('risklist', function (editor, uiName) {
          return new window.UE.ui.Button({
            name: uiName,
            title: '查看风险数据库信息', // 这里是设置当鼠标指向这个按扭时显示的文字
            cssRules:
              "background-image: url('/icons/copy-template.png') !important; background-size: 100%;",
            onclick: function () {
              that.$refs['risklist'].showDialog()
            },
          })
        })
      },
      copyReport() {
        //添加保存模板按钮
        let that = this
        window.UE.registerUI('report', function (editor, uiName) {
          return new window.UE.ui.Button({
            name: uiName,
            title: '查看测试结果汇总', // 这里是设置当鼠标指向这个按扭时显示的文字
            cssRules:
              "background-image: url('/icons/copy-template.png') !important; background-size: 100%;",
            onclick: function () {
              that.$refs['report'].showDialog()
            },
          })
        })
      },
      selectTemp(content) {
        this.editor.setContent(content)
      },

      // 添加销毁按钮方法
      destroyCustomBtns() {
        const btnNames = [
          'template',
          'templatesave',
          'annotationedit',
          'risklist',
          'report',
        ]
        btnNames.forEach((name) => {
          if (UE._customizeUI[name]) {
            delete UE._customizeUI[name]
          }
        })
      },

      initCustomBtn() {
        // 先销毁已有按钮
        this.destroyCustomBtns()

        if (this.template !== '') {
          this.initTemplate()
          this.initTemplateSave()
        }
        console.log('🚀 ~ initCustomBtn ~ this.isCopyRisk:', this.isCopyRisk)
        if (this.isCopyRisk) {
          this.copyRisk()
        }
        if (this.isReport) {
          this.copyReport()
        }
      },
    },
  }
</script>

<style>
  #edui_fixedlayer {
    z-index: 20000 !important;
  }
  .edui-editor-iframeholder {
    /* overflow: scroll !important; */
    height: 600px !important;
  }
  #edui1_bottombar {
    display: none;
  }
</style>
