<!--
 * @Date: 2022-04-29 11:00:47
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-29 11:00:49
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/CustomForm.vue
-->
<template>
  <el-dialog
    append-to-body
    class="pt"
    fullscreen
    :title="title"
    :visible.sync="dialogFormVisible"
    @close="close"
    :close-on-click-modal="false"
  >
    <Home />
  </el-dialog>
</template>
<script>
  import Home from '@/components/form-generator/views/index/Home.vue'
  export default {
    name: 'CustomForm',
    components: { Home },
    data() {
      return {
        title: '',
        dialogFormVisible: false,
      }
    },
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      save() {},
    },
  }
</script>
<style lang="scss">
  .pt .el-dialog__body {
    padding-top: 0;
  }
</style>
