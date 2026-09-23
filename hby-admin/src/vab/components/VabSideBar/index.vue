<template>
  <el-scrollbar
    class="vab-side-bar"
    :class="{ 'is-collapse': collapse, 'side-bar-common': layout === 'common' }"
  >
    <vab-logo
      v-if="
        layout === 'vertical' ||
        layout === 'comprehensive' ||
        layout === 'float'
      "
    />
    <el-menu
      :active-text-color="variables['menu-color-active']"
      :background-color="variables['menu-background']"
      :collapse="collapse"
      :collapse-transition="false"
      :default-active="activeMenu"
      :default-openeds="defaultOpeneds"
      menu-trigger="click"
      mode="vertical"
      :text-color="variables['menu-color']"
      :unique-opened="uniqueOpened"
    >
      <template v-for="(route, index) in handleRoutes">
        <vab-menu
          v-if="route.meta && !route.meta.hidden"
          :key="index + route.name"
          :item="route"
        />
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
  import variables from '@/vab/styles/variables/variables.scss'
  import { mapGetters } from 'vuex'
  import { defaultOpeneds, uniqueOpened } from '@/config'
  import { handleActivePath } from '@/utils/routes'

  export default {
    name: 'VabSideBar',
    props: {
      layout: {
        type: String,
        default: 'vertical',
      },
    },
    data() {
      return {
        activeMenu: '',
        defaultOpeneds,
        uniqueOpened,
        variables,
      }
    },
    computed: {
      ...mapGetters({
        collapse: 'settings/collapse',
        extra: 'settings/extra',
        routes: 'routes/routes',
        _activeMenu: 'routes/activeMenu',
      }),
      handleRoutes() {
        return this.layout === 'comprehensive'
          ? this.handlePartialRoutes
          : this.routes.flatMap((route) => {
              // 非分栏布局下，消息中心(msgcenter)展开显示子菜单
              const isLevelHidden =
                route.meta && route.meta.levelHidden === true
              const isMsgCenter =
                route.name === 'msgcenter' &&
                route.meta &&
                route.meta.hidden === true
              if ((isLevelHidden || isMsgCenter) && route.children) {
                return route.children
              }
              return route
            })
      },
      handlePartialRoutes() {
        const activeMenu = this.routes.find(
          (route) => route.name === this.extra.first
        )
        return activeMenu ? activeMenu.children : []
      },
    },
    watch: {
      $route: {
        handler(route) {
          this.activeMenu = handleActivePath(route)
        },
        immediate: true,
      },
      _activeMenu: {
        handler(val) {
          this.activeMenu = val
        },
        deep: true,
      },
    },
  }
</script>

<style lang="scss" scoped>
  @mixin active {
    &:hover {
      color: $base-color-white;
      background-color: $base-menu-background-active !important;
    }

    &.is-active {
      color: $base-color-white;
      background-color: $base-menu-background-active !important;
    }
  }

  .vab-side-bar {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    z-index: $base-z-index + 1;
    width: $base-left-menu-width;
    height: 100vh;
    overflow: hidden;
    background: $base-menu-background;
    box-shadow: $base-box-shadow;
    transition: $base-transition;

    &.side-bar-common {
      top: $base-top-bar-height;
      height: calc(100vh - #{$base-top-bar-height});
    }

    &.is-collapse {
      width: $base-left-menu-width-min;
      border-right: 0;

      ::v-deep {
        .el-menu--collapse.el-menu {
          > .el-menu-item,
          > .el-submenu {
            text-align: center;

            .el-tag {
              display: none;
            }
          }
        }

        .el-menu-item,
        .el-submenu {
          text-align: left;
        }

        .el-menu--collapse {
          border-right: 0;

          .el-submenu__icon-arrow {
            right: 10px;
            margin-top: -3px;
          }
        }
      }
    }

    ::v-deep {
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }

      .el-menu {
        border: 0;
      }

      .el-menu-item,
      .el-submenu__title {
        height: $base-menu-item-height;
        overflow: hidden;
        line-height: $base-menu-item-height;
        text-overflow: ellipsis;
        white-space: nowrap;
        vertical-align: middle;

        i {
          color: inherit;
        }
      }

      .el-menu-item {
        @include active;
      }
    }
  }
</style>
