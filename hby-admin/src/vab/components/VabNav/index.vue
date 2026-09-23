<template>
  <div class="vab-nav">
    <el-row :gutter="15">
      <el-col :lg="12" :md="12" :sm="12" :xl="12" :xs="4">
        <div class="left-panel">
          <vab-fold v-if="layout !== 'float'" />
          <el-tabs
            v-if="layout === 'comprehensive'"
            v-model="extra.first"
            tab-position="top"
            @tab-click="handleTabClick"
          >
            <template v-for="(route, index) in handleRoutes">
              <el-tab-pane :key="index + route.name" :name="route.name">
                <template #label>
                  <vab-icon
                    v-if="route.meta.icon"
                    :icon="route.meta.icon"
                    :is-custom-svg="true"
                    style="min-width: 16px"
                  />
                  {{ translateTitle(route.meta.title) }}
                </template>
              </el-tab-pane>
            </template>
          </el-tabs>
          <vab-breadcrumb v-else class="hidden-xs-only" />
        </div>
      </el-col>
      <el-col :lg="12" :md="12" :sm="12" :xl="12" :xs="20">
        <div class="right-panel">
          <!--          <vab-error-log />-->
          <change-module :moduleLists="moduleLists" />
          <vab-search />
          <vab-notice />
          <vab-ai />
          <vab-ai-edit />
          <vab-full-screen />
          <vab-language />
          <vab-theme />
          <!--          <vab-refresh />-->
          <vab-avatar />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { translateTitle } from '@/utils/i18n'
  import { mapGetters } from 'vuex'
  import { openFirstMenu } from '@/config'
  import ChangeModule from '@/components/change/module'
  import { getModuleList } from '@/api/setting/system'

  export default {
    name: 'VabNav',
    components: { ChangeModule },
    props: {
      layout: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        firstMenu: '',
        moduleLists: [],
      }
    },
    computed: {
      ...mapGetters({
        extra: 'settings/extra',
        routes: 'routes/routes',
      }),
      handleRoutes() {
        return this.routes.filter(
          (route) => route.meta && route.meta.hidden !== true
        )
      },
      handleActiveMenu() {
        return this.routes.find((route) => route.name === this.extra.first)
      },
      handlePartialRoutes() {
        const activeMenu = this.handleActiveMenu
        return activeMenu ? activeMenu.children : []
      },
    },
    mounted() {
      this.moduleList()
    },
    watch: {
      $route: {
        handler(route) {
          const firstMenu = route.matched[0].name
          if (this.extra.first !== firstMenu) {
            this.extra.first = firstMenu
            this.handleTabClick(true)
          }
        },
        immediate: true,
      },
    },
    methods: {
      translateTitle,
      handleTabClick(handler) {
        if (handler !== true && openFirstMenu)
          this.$router.push(this.handleActiveMenu)
      },
      moduleList() {
        getModuleList({
          pageNumber: 1,
          pageSize: 20,
        }).then((res) => {
          this.moduleLists = res.data
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-nav {
    position: relative;
    height: $base-nav-height;
    padding-right: $base-padding;
    padding-left: $base-padding;
    overflow: hidden;
    user-select: none;
    background: $base-color-white;
    box-shadow: $base-box-shadow;

    .left-panel {
      display: flex;
      align-items: center;
      justify-items: center;
      height: $base-nav-height;

      ::v-deep {
        .fold-unfold {
          margin-right: $base-margin;
        }

        .el-tabs {
          width: 100%;
          margin-left: $base-margin;

          .el-tabs__header {
            margin: 0;

            > .el-tabs__nav-wrap {
              display: flex;
              align-items: center;

              .el-icon-arrow-left,
              .el-icon-arrow-right {
                font-weight: 600;
                color: $base-color-grey;
              }
            }
          }

          .el-tabs__item {
            > div {
              display: flex;
              align-items: center;

              i {
                margin-right: 3px;
              }
            }
          }
        }

        .el-tabs__nav-wrap::after {
          display: none;
        }
      }
    }

    .right-panel {
      display: flex;
      align-content: center;
      align-items: center;
      justify-content: flex-end;
      height: $base-nav-height;

      ::v-deep {
        [class*='ri-'] {
          margin-left: $base-margin;
          color: $base-color-grey;
          cursor: pointer;
        }

        button {
          [class*='ri-'] {
            margin-left: 0;
            color: $base-color-white;
            cursor: pointer;
          }
        }
      }
    }
  }
</style>
