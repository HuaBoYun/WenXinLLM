<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="100%"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    :fullscreen="true"
    :show-close="false"
  >
    <div class="custom-form-layout">
      <CustomFormItem />
      <CustomFormList />
      <CustomFormAttr />
    </div>
    <div class="action-view">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="close">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import draggable from 'vuedraggable'
  import CustomFormItem from '@/views/setting/activiti/components/CustomFormItem'
  import CustomFormList from '@/views/setting/activiti/components/CustomFormList'
  import CustomFormAttr from '@/views/setting/activiti/components/CustomFormAttr'
  export default {
    name: 'CustomFormEdit',
    components: {
      CustomFormAttr,
      CustomFormList,
      CustomFormItem,
      draggable,
    },
    data() {
      return {
        title: '自定义表单',
        dialogFormVisible: false,
        arr1: [
          { id: 1, name: 'www.itxst.com（不允许停靠）' },
          { id: 2, name: 'www.jd.com' },
          { id: 3, name: 'www.baidu.com' },
          { id: 5, name: 'www.google.com' },
          { id: 4, name: 'www.taobao.com（不允许拖拽）' },
        ],
        arr2: [{ id: 11, name: '常用菜单' }],
      }
    },
    created() {},
    updated() {},
    methods: {
      show() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      //左边往右边拖动时的事件
      end1(e) {
        var that = this
        var items = this.arr2.filter(function (m) {
          return m.id === that.moveId
        })
        //如果左边
        if (items.length < 2) return
        this.arr2.splice(e.newDraggableIndex, 1)
      },
      //右边往左边拖动时的事件
      end2(e) {
        var that = this
        var items = this.arr1.filter(function (m) {
          return m.id === that.moveId
        })
        //如果左边
        if (items.length < 2) return
        this.arr1.splice(e.newDraggableIndex, 1)
      },
      //move回调方法
      onMove(e, originalEvent) {
        this.moveId = e.relatedContext.element.id
        //不允许停靠
        if (e.relatedContext.element.id === 1) return false
        //不允许拖拽
        if (e.draggedContext.element.id === 4) return false
        if (e.draggedContext.element.id === 11) return false
        return true
      },
    },
  }
</script>
<style scoped>
  .action-view {
    position: absolute;
    top: 15px;
    right: 15px;
  }
  .custom-form-layout {
    display: flex;
    justify-content: space-between;
  }
</style>
