<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <compareConcent v-if="dialogFormVisible" :render="render" />
  </el-dialog>
</template>

<script>
  import compareConcent from './compareConcent.vue'
  export default {
    components: { compareConcent },
    data() {
      return {
        dialogFormVisible: false,
        title: '文件对比',
        render: {},
      }
    },
    methods: {
      show(data) {
        // if (!data.file_url1 || !data.file_url2) return undefined

        this.dialogFormVisible = true
        this.render = data
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style scoped lang="scss"></style>
