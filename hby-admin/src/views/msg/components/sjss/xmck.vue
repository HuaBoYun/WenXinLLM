<template>
  <div class="xmck">
    <Look />
    <!-- 流程相关 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <div v-if="!disabled" style="text-align: right; margin-top: 10px">
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
  </div>
</template>
<script>
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    components: {
      Look: () => import('@/views/oilAudit/data/look.vue'),
      Resubmit,
    },
    data() {
      return {
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        disabled: false,
      }
    },
    mounted() {},
    methods: {
      async ymsubmit() {
        this.$refs.resubmit.ymsubmit()
      },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        this.uploadShow = true
        //流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      close() {
        this.$bus.$emit('updateMsg', 0)
      },
    },
  }
</script>
<style lang="scss" scoped></style>
