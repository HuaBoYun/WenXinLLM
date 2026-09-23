<template>
  <div class="vab-header">
    <div class="vab-main">
      <el-row :gutter="20">
        <el-col :span="6">
          <vab-logo />
        </el-col>
        <el-col :span="18">
          <div class="right-panel">
            <el-menu
              v-if="'horizontal' === layout"
              :active-text-color="variables['menu-color-active']"
              :background-color="variables['menu-background']"
              :default-active="activeMenu"
              menu-trigger="hover"
              mode="horizontal"
              :text-color="variables['menu-color']"
            >
              <template v-for="(route, index) in handleRoutes">
                <vab-menu
                  v-if="route.meta && !route.meta.hidden"
                  :key="index + route.name"
                  :item="route"
                  :layout="layout"
                />
              </template>
            </el-menu>
            <div class="right-view">
              <div style="display: flex; align-items: center; float: right">
                <!--            <vab-error-log />-->
                <change-module :module-lists="moduleLists" />
                <vab-search />
                <vab-notice />
                <vab-ai />
                <vab-ai-edit />
                <vab-full-screen />
                <!--            <vab-language />-->
                <vab-theme />
                <!--            <vab-refresh />-->
                <vab-avatar />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import variables from '@/vab/styles/variables/variables.scss'
  import { mapGetters } from 'vuex'
  import { handleActivePath } from '@/utils/routes'
  import ChangeModule from '@/components/change/module'

  export default {
    name: 'VabHeader',
    components: { ChangeModule },
    props: {
      layout: {
        type: String,
        default: 'horizontal',
      },
    },
    data() {
      return {
        activeMenu: '',
        menuTrigger: 'hover',
        moduleLists: [],
      }
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
        routes: 'routes/routes',
        _activeMenu: 'routes/activeMenu',
      }),
      variables() {
        return variables
      },
      handleRoutes() {
        return this.routes.flatMap((route) => {
          return route.meta && route.meta.levelHidden === true && route.children
            ? route.children
            : route
        })
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
    mounted() {
      this.moduleList()
    },
    methods: {
      async moduleList() {
        const allMenu = await this.$store.dispatch(
          'routes/getAuthorizedModules'
        )
        this.moduleLists = (allMenu || []).filter(
          (item) => item.uniqueIdentification !== 'wdyg'
        )
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-header {
    display: flex;
    align-items: center;
    justify-items: flex-end;
    height: $base-top-bar-height;
    background: $base-menu-background;
    // background: linear-gradient(to right, #2c3239, #444e5d) !important;
    .vab-main-custom {
      padding: 0 $base-padding 0 0 !important;
    }
    .logo-container-horizontal {
      background-color: transparent !important;
    }
    .vab-main {
      padding: 0 $base-padding 0 $base-padding;
      // background: linear-gradient(to right, #2c3239, #444e5d) !important;
      .right-panel {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: $base-top-bar-height;
        ::v-deep {
          > .el-menu--horizontal.el-menu
            > .el-submenu
            > .el-submenu__title
            > .el-submenu__icon-arrow {
            float: right;
            margin-top: ($base-top-bar-height - 11) / 2 !important;
          }

          > .el-menu--horizontal.el-menu > .el-menu-item {
            .el-tag {
              margin-top: $base-top-bar-height / 2 - 7.5 !important;
              margin-left: 5px;
            }

            .vab-dot {
              float: right;
              margin-top: ($base-top-bar-height - 6) / 2 + 1;
            }

            @media only screen and (max-width: 1199px) {
              .el-tag {
                display: none;
              }
            }
          }

          .el-menu {
            &.el-menu--horizontal {
              display: flex;
              align-items: center;
              overflow-y: hidden;
              height: 60px;
              line-height: 60px;
              border-bottom: 0 solid transparent !important;
              .el-menu-item,
              .el-submenu__title {
                height: $base-top-bar-height/1.3;
                padding: 0 $base-padding;
                line-height: $base-top-bar-height/1.3;
              }

              > .el-menu-item,
              > .el-submenu {
                height: 50px;
                line-height: 50px;

                .el-submenu__icon-arrow {
                  float: right;
                  margin-top: ($base-menu-item-height - 16) / 2;
                }

                > .el-submenu__title {
                  height: $base-top-bar-height + 10;
                  line-height: $base-top-bar-height + 10;
                }
              }
            }

            [class*='ri-'] {
              margin-left: 0;
              color: rgba($base-color-white, 0.9);
              cursor: pointer;
            }

            .el-submenu,
            .el-menu-item {
              i {
                color: inherit;
              }

              &.is-active {
                border-bottom: 0 solid transparent;

                .el-submenu__title {
                  border-bottom: 0 solid transparent;
                }
              }
            }

            .el-menu-item {
              &.is-active {
                background: $base-color-blue !important;
              }
            }
          }

          .user-name {
            color: rgba($base-color-white, 0.9);
          }

          .user-name + i {
            color: rgba($base-color-white, 0.9);
          }

          [class*='ri-'] {
            margin-left: $base-margin;
            color: rgba($base-color-white, 0.9);
            cursor: pointer;
          }

          button {
            svg {
              margin-right: 0;
              color: rgba($base-color-white, 0.9);
              cursor: pointer;
              fill: rgba($base-color-white, 0.9);
            }
          }
        }
      }
    }
  }
  .vab-layout-horizontal .el-col-6 {
    width: 120px;
  }
  .vab-layout-horizontal .el-col-18 {
    width: calc(100% - 120px);
  }
  .vab-layout-common .right-view {
    width: 100%;
  }
</style>
