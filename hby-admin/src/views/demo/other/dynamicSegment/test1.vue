<template>
  <div class="test1-container">
    <el-alert
      :closable="false"
      show-icon
      title="取值方式：this.params"
      type="success"
    />
    <vab-json-viewer copyable :expand-depth="5" sort :value="route" />
  </div>
</template>

<script>
  import { mapActions } from 'vuex'
  import VabJsonViewer from 'vue-json-viewer'

  export default {
    name: 'Test1',
    components: { VabJsonViewer },
    data() {
      return {
        route: {},
      }
    },
    created() {
      this.handleParams()
    },
    methods: {
      ...mapActions({
        changeTabsMeta: 'tabs/changeTabsMeta',
      }),
      handleParams() {
        const route = this.$route
        this.$nextTick(() => {
          this.changeTabsMeta({
            title: 'Params',
            meta: {
              title: `Params Id=${route.params.id}`,
            },
          })
          this.route = {
            path: route.matched[route.matched.length - 1].path,
            params: route.params,
            query: route.query,
            name: route.name,
            meta: route.meta,
          }
        })
      },
    },
  }
</script>
