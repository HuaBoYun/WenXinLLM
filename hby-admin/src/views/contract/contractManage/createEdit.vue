<template>
  <div>
    <CreateEdit ref="common" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import CreateEdit from './components/contractsEdit/CreateEdit.vue'

  export default {
    name: 'createEdit',
    components: { CreateEdit },
    mounted() {
      const { row, disabled, item, typename } = this.$route.query
      if (row) {
        if (disabled) this.$refs['common'].showDetail(row, row.contracttype, true)
        else this.$refs['common'].showEdit(row, row.contracttype, undefined, true)
        return
      }
      this.$refs['common'].showEdit(undefined, item.typename, typename, true)
    },
    methods: {
      fetchData() {},
    },
  }
</script>
