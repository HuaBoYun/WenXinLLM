<template>
  <transition mode="out-in" name="fade-transform">
    <!--    <keep-alive :include="keepAliveNameList" :max="keepAliveMaxNum">-->
    <router-view :key="routerKey" />
    <!--    </keep-alive>-->
  </transition>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { keepAliveMaxNum } from '@/config'

  export default {
    name: 'VabRouterView',
    data() {
      return {
        routerKey: null,
        keepAliveMaxNum,
        keepAliveNameList: [],
      }
    },
    computed: {
      ...mapGetters({
        visitedRoutes: 'tabs/visitedRoutes',
      }),
    },
    watch: {
      $route: {
        handler() {
          this.routerKey = this.$route.fullPath
        },
        immediate: true,
      },
      visitedRoutes: {
        handler() {
          this.updateKeepAliveNameList()
        },
      },
    },
    created() {
      this.updateKeepAliveNameList()
      this.$baseEventBus.$on('reload-router-view', () => {
        this.routerKey = null
        this.updateKeepAliveNameList(this.$route.name)
        this.$nextTick(() => {
          this.routerKey = this.$route.path
          this.updateKeepAliveNameList()
        })
      })
    },
    methods: {
      updateKeepAliveNameList(refreshRouteName = null) {
        this.keepAliveNameList = [
          ...new Set(
            this.visitedRoutes
              .filter(
                (item) =>
                  !item.meta.noKeepAlive && item.name !== refreshRouteName
              )
              .flatMap((item) => item.matched)
          ),
        ]
      },
    },
  }
</script>
