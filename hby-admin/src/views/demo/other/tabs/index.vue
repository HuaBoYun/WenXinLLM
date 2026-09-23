<template>
  <div class="tabs-container">
    <el-button type="primary" @click="closeOthersTabs">关闭其他</el-button>
    <el-button type="primary" @click="closeLeftTabs">关闭左侧</el-button>
    <el-button type="primary" @click="closeRightTabs">关闭右侧</el-button>
    <el-button type="primary" @click="closeAllTabs">关闭全部</el-button>
    <el-button type="primary" @click="handleTabRemove($route.path)">
      关闭当前
    </el-button>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import { handleActivePath } from '@/utils/routes'

  export default {
    name: 'Tabs',
    data() {
      return {
        hoverRoute: null,
      }
    },
    computed: {
      ...mapGetters({
        visitedRoutes: 'tabs/visitedRoutes',
        routes: 'routes/routes',
      }),
    },
    methods: {
      ...mapActions({
        delVisitedRoute: 'tabs/delVisitedRoute',
        delOthersVisitedRoutes: 'tabs/delOthersVisitedRoutes',
        delLeftVisitedRoutes: 'tabs/delLeftVisitedRoutes',
        delRightVisitedRoutes: 'tabs/delRightVisitedRoutes',
        delAllVisitedRoutes: 'tabs/delAllVisitedRoutes',
      }),
      /**
       * 根据原生路径删除标签中的标签
       * @param rawPath 原生路径
       * @returns {Promise<void>}
       */
      async handleTabRemove(rawPath) {
        await this.delVisitedRoute(rawPath)
        if (this.isActive(rawPath)) this.toLastTab()
      },
      handleCommand(command) {
        switch (command) {
          case 'closeOthersTabs':
            this.closeOthersTabs()
            break
          case 'closeLeftTabs':
            this.closeLeftTabs()
            break
          case 'closeRightTabs':
            this.closeRightTabs()
            break
          case 'closeAllTabs':
            this.closeAllTabs()
            break
        }
      },
      /**
       * 删除其他标签页
       * @returns {Promise<void>}
       */
      async closeOthersTabs() {
        if (this.hoverRoute) {
          await this.$router.push(this.hoverRoute)
          await this.delOthersVisitedRoutes(this.hoverRoute.path)
        } else
          await this.delOthersVisitedRoutes(handleActivePath(this.$route, true))
      },
      /**
       * 删除左侧标签页
       * @returns {Promise<void>}
       */
      async closeLeftTabs() {
        if (this.hoverRoute) {
          await this.$router.push(this.hoverRoute)
          await this.delLeftVisitedRoutes(this.hoverRoute.path)
        } else
          await this.delLeftVisitedRoutes(handleActivePath(this.$route, true))
      },
      /**
       * 删除右侧标签页
       * @returns {Promise<void>}
       */
      async closeRightTabs() {
        if (this.hoverRoute) {
          await this.$router.push(this.hoverRoute)
          await this.delRightVisitedRoutes(this.hoverRoute.path)
        } else
          await this.delRightVisitedRoutes(handleActivePath(this.$route, true))
      },
      /**
       * 删除所有标签页
       * @returns {Promise<void>}
       */
      async closeAllTabs() {
        await this.delAllVisitedRoutes()
        this.toLastTab()
      },
      /**
       * 跳转最后一个标签页
       */
      toLastTab() {
        const latestView = this.visitedRoutes.slice(-1)[0]
        if (latestView) this.$router.push(latestView)
        else this.$router.push('/')
      },
      isActive(path) {
        return path === handleActivePath(this.$route, true)
      },
      isNoClosable(tag) {
        return tag.meta && tag.meta.noClosable
      },
    },
  }
</script>
