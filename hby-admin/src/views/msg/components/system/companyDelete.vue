<template>
  <div>
    <div>{{ operationMemo }}</div>
    <div class="footer" v-if="!disabled" style="text-align: right">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'ResetPassword',
    components: {
      Resubmit,
    },
    data() {
      return {
        password: '',
        operationMemo: '',
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },
    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        operationMemo
      ) {
        this.disabled = title == 'detail'
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
        this.operationMemo = operationMemo //重置密码只展示提示信息就行
      },
      save() {
        this.close()
      },
      async ymsubmit() {
        this.$refs.resubmit.ymsubmit()
      },
      close() {
        this.$message.success('保存成功')
      },
    },
  }
</script>
