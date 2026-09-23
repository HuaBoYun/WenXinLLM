<template>
  <vab-icon
    v-if="theme.showRefresh"
    icon="refresh-line"
    @click="refreshRoute"
  />
</template>

<script>
  import { mapGetters } from 'vuex'
  import VabProgress from 'nprogress'

  export default {
    name: 'VabRefresh',
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
        extra: 'settings/extra',
        visitedRoutes: 'tabs/visitedRoutes',
      }),
    },
    methods: {
      async refreshRoute() {
        if (this.theme.showProgressBar) VabProgress.start()
        this.$baseEventBus.$emit('reload-router-view')
        setTimeout(() => {
          if (this.theme.showProgressBar) VabProgress.done()
        }, 200)
      },
    },
  }
</script>
