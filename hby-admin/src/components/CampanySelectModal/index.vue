<template>
  <el-dialog
    append-to-body
    v-loading="loading"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
  >
    <vab-query-form>
      <company-tree always-root not-default-select @select="handleNodeClick" />
    </vab-query-form>
  </el-dialog>
</template>

<script>
  import CompanyTree from '@/components/change/company/components/tree.vue'
  export default {
    name: 'CampanySelectModal',
    components: { CompanyTree },
    data() {
      return {
        loading: true,
        title: '集团',
        key: '',
        dialogTreeVisible: false,
      }
    },
    methods: {
      show(data) {
        this.labelKey = data.labelKey
        this.idKey = data.idKey
        this.title = data.title || '集团'
        this.dialogTreeVisible = true
      },
      close() {
        this.dialogTreeVisible = false
      },
      handleNodeClick(data) {
        data.labelKey = this.labelKey
        data.idKey = this.idKey
        this.$emit('selected', data)
        this.close()
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
