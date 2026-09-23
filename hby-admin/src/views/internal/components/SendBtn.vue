<template>
  <div>
    <el-dropdown style="margin: 0 10px">
      <el-button type="primary">操作</el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="$refs['Send'].showEdit()">
          发送至底稿
        </el-dropdown-item>
        <el-dropdown-item @click.native="$refs['Send2'].showEdit()">
          发送至底稿附件
        </el-dropdown-item>
        <el-dropdown-item @click.native="$refs['Send3'].showEdit()">
          发送至疑点
        </el-dropdown-item>
        <el-dropdown-item @click.native="$refs['Send4'].showEdit()">
          发送至缺陷
        </el-dropdown-item>
        <el-dropdown-item @click.native="$refs['Send5'].showEdit()">
          发送至风险
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <Send ref="Send" />
    <Send2 ref="Send2" />
    <Send3 ref="Send3" />
    <Send4 ref="Send4" />
    <Send5 ref="Send5" />
  </div>
</template>
<script>
  import Send from '@/views/internal/components/Send.vue'
  import Send2 from '@/views/internal/components/Send2.vue'
  import Send3 from '@/views/internal/components/Send3.vue'
  import Send4 from '@/views/internal/components/Send4.vue'
  import Send5 from '@/views/internal/components/Send5.vue'
  export default {
    name: 'SendBtn',
    components: { Send, Send2, Send3, Send4, Send5 },
    data() {
      return {}
    },
    created() {},
    methods: {
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
