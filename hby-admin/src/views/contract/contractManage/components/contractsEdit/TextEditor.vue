<template>
  <el-dialog
    :close-on-click-modal="false"
    :modal="false"
    append-to-body
    fullscreen
    title="编辑器"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <WebOffice />
  </el-dialog>
</template>

<script>
  import WebOffice from '@/components/WebOffice'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        type: 0,
      }
    },
    components: {
      WebOffice,
    },
    mounted() {
      this.$bus.on('closeEdit', (value) => {
        if (value == 2) {
          this.close()
        }
      })
    },
    methods: {
      show(type) {
        this.type = type
        this.dialogFormVisible = true
      },
      close() {
        this.$store.commit('acl/contractidd', '')
        this.$store.commit('acl/contractidd2', '')
        if (this.type != 1) {
          this.$bus.emit('WebClose2', 'WebClose2')
        }
        this.dialogFormVisible = false
        this.type = 0
      },
    },
  }
</script>

<style></style>
