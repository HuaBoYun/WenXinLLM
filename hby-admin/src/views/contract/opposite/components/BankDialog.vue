<!--
 * @Date: 2022-04-20 15:17:51
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-21 18:00:56
 * @FilePath: /hb-admin/src/views/contract/opposite/components/BankDialog.vue
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
  >
    <Bank :opposite="opposite" />
  </el-dialog>
</template>
<script>
  import Bank from './Bank.vue'
  export default {
    components: {
      Bank,
    },
    data() {
      return {
        dialogVisible: false,
        title: '',
        opposite: {},
      }
    },
    methods: {
      show(row) {
        this.opposite = row
        this.dialogVisible = true
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
