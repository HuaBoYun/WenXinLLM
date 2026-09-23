<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div>
      <img :src="imgUrl" />
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: 'htfbkDetail',
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        imgUrl: '',
      }
    },
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        this.title = row.name
        this.imgUrl = row.url
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
