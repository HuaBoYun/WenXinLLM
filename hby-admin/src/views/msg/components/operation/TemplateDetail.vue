<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <TemplateDetailContent v-if="!isSendBack" :form-data="form" />
    <TemplateDetailContentEdit v-else :form-data="form" />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <div v-else>
    <TemplateDetailContent v-if="!isSendBack" :form-data="form" />
    <TemplateDetailContentEdit v-else :form-data="form" />
  </div>
</template>

<script>
  import TemplateDetailContent from '@/views/contract/contractManage/components/TemplateDetailContent.vue'
  import TemplateDetailContentEdit from '@/views/contract/contractManage/components/TemplateDetailContentEdit.vue'

  export default {
    name: 'TemplateDetail',
    components: { TemplateDetailContent, TemplateDetailContentEdit },
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
        },
        title: '查看',
        dialogFormVisible: false,
        typeOptions: [],
        disabled: false,
        isSendBack: false,
      }
    },
    mounted() {},
    created() {},
    methods: {
      showDetail(row) {
        this.title = '查看'
        this.disabled = true
        Object.keys(this.form).forEach((key) => {
          this.form[key] = row[key]
        })
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        var that = this
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (row.cystaffid == userInfo.staffid && row.cystate == '需调整') {
          that.isSendBack = true
        }
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
