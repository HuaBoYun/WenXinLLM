<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      append-to-body
      :title="title"
      :visible.sync="visabled"
      width="1000px"
      @close="close"
      v-if="!isDiv"
    >
      <slot></slot>
    </el-dialog>
    <div style="padding: 40px; margin-bottom: 20px" v-else>
      <slot></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'DialogOrDiv',
    props: {
      title: {
        type: String,
        default: '新增',
      },
      dialogFormVisible: {
        type: Boolean,
        default: false,
      },
      isDiv: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {}
    },
    computed: {
      visabled() {
        return this.dialogFormVisible
      },
    },
    methods: {},
  }
</script>
