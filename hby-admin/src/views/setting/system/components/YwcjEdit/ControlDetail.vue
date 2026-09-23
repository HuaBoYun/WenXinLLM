<!--
 * @Date: 2022-05-06 15:16:01
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-06 15:33:18
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/ControlDetail.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div v-html="data" />
  </el-dialog>
</template>
<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        data: undefined,
      }
    },
    methods: {
      show(row) {
        this.data = row.bodyinfo
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
