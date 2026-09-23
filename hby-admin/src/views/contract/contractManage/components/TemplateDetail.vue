<!--
 * @Date: 2022-04-18 11:40:51
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-18 16:07:35
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/TemplateDetail.vue
-->
<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <TemplateDetailContent :form-data="form" />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <TemplateDetailContent v-else :form-data="form" />
</template>

<script>
import TemplateDetailContent from './TemplateDetailContent.vue'

export default {
  name: 'TemplateDetail',
  components: { TemplateDetailContent },
  props: {
    isDialog: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      form: {
        flowId: 733271,
        contractid: undefined,
        contractno: '',
        contractname: '',
        contracttype: '',
        recordtype: 'HTGL007',
        momoconcat: '',
        describe: '',
        jborgName: '',
      },
      title: '查看',
      dialogFormVisible: false,
      typeOptions: [],
      disabled: false,
    }
  },

  created() {},
  methods: {
    //赋值处理
    showDetail(row) {
      this.title = '查看'
      this.disabled = true
      Object.keys(this.form).forEach((key) => {
        this.form[key] = row[key]
      })
      this.dialogFormVisible = true
    },
    close() {
      // this.$refs['form'].resetFields()
      this.form = this.$options.data().form
      this.dialogFormVisible = false
    },
  },
}
</script>
<style scoped>
.formula .el-form-item--small.el-form-item {
  margin-bottom: 5px;
}
</style>
