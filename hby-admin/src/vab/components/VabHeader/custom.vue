<template>
  <div class="vab-header">
    <!-- 正常布局 -->
    <div
      class="vab-main"
      v-if="theme.layout !== 'custom' && theme.layout !== 'tradition'"
    >
      <div class="custom-header">
        <div class="left-section">
          <vab-logo-custom />
        </div>
        <div class="right-panel">
          <div class="right-view">
            <div style="display: flex; align-items: center; float: right">
              <vab-search />
              <vab-notice />
              <vab-ai />
              <vab-ai-edit />
              <vab-full-screen />
              <vab-theme />
              <vab-avatar />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 经典模式 -->
    <div
      class="vab-main"
      v-if="theme.layout === 'custom'"
      style="padding-left: 0 !important"
    >
      <div class="custom-header">
        <div class="left-section">
          <vab-logo-custom />
          <TopNav />
        </div>
        <div class="center-section">
          <img :src="midUrl" alt="" style="width: 85px" />
        </div>
        <div class="right-panel">
          <div class="right-view">
            <div style="display: flex; align-items: center; float: right">
              <vab-search />
              <vab-notice />
              <vab-ai />
              <!-- <vab-ai-edit /> -->
              <!-- <vab-full-screen /> -->
              <vab-theme />
              <vab-avatar />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 传统模式 -->
    <div
      class="vab-main"
      v-if="theme.layout === 'tradition'"
      style="padding-left: 0 !important"
    >
      <div class="custom-header">
        <div class="left-section">
          <vab-logo-custom />
          <TopNav />
        </div>
        <div class="center-section">
          XingGuang
          <!-- <img :src="midUrl" alt="" style="width: 85px" /> -->
        </div>
        <div class="right-panel">
          <div class="right-view">
            <div style="display: flex; align-items: center; float: right">
              <vab-search />
              <vab-notice />
              <vab-ai />
              <!-- <vab-ai-edit /> -->
              <!-- <vab-full-screen /> -->
              <vab-theme />
              <vab-avatar />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import variables from '@/vab/styles/variables/variables.scss'
  import { mapGetters } from 'vuex'
  import { handleActivePath } from '@/utils/routes'
  import ChangeModule from '@/components/change/module'
  import { getModuleList } from '@/api/setting/system'
  import VabLogoCustom from '@/vab/components/VabLogo/custom'
  import { getAuthListForUser } from '@/api/setting/auths'
  import customLogo from '@/assets/mid_logo.png'
  import TopNav from './TopNav.vue'

  export default {
    name: 'VabHeaderCustom',
    components: { VabLogoCustom, ChangeModule, TopNav },
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
        midUrl: customLogo,
      }
    },
    computed: {
      ...mapGetters({
        routes: 'routes/routes',
        _activeMenu: 'routes/activeMenu',
        theme: 'settings/theme',
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
    mounted() {
      // this.moduleList()
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
    methods: {
      async moduleList() {
        getModuleList({
          pageNumber: 1,
          pageSize: 20,
        }).then(async (res) => {
          let tempData = res.data
          for (let i = 0; i < tempData.length; i++) {
            tempData[i]['menu'] = await this.getMenuList(
              tempData[i]['uniqueIdentification']
            )
          }
          this.moduleLists = tempData
          console.log(tempData, 'tempData')
          localStorage.setItem('allMenu', JSON.stringify(tempData))
        })
      },
      async getMenuList(model) {
        let resultMenu = await getAuthListForUser({ moduleType: model })
        return resultMenu.data.rightList
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-header {
    display: flex;
    align-items: center;
    justify-items: flex-end;
    height: 50px;
    background: linear-gradient(to right, #2c3239, #444e5d) !important;
    // height: $base-top-bar-height;
    // background: #3b424e;
    // background: $base-menu-background;

    .vab-main {
      background: linear-gradient(to right, #2c3239, #444e5d) !important;
      // padding: 0 $base-padding 0 $base-padding;
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
              height: 76px;
              border-bottom: 0 solid transparent !important;

              .el-menu-item,
              .el-submenu__title {
                height: $base-top-bar-height/1.3;
                padding: 0 $base-padding;
                line-height: $base-top-bar-height/1.3;
              }

              > .el-menu-item,
              > .el-submenu {
                height: 55px;
                line-height: 55px;

                .el-submenu__icon-arrow {
                  float: right;
                  margin-top: ($base-menu-item-height - 16) / 2;
                }

                > .el-submenu__title {
                  height: $base-top-bar-height;
                  line-height: $base-top-bar-height;
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
  .custom-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    position: relative;
    height: 50px;
    .left-section {
      flex: 0 0 200px;
      display: flex;
      align-items: center;
    }

    .center-section {
      flex: 1;
      text-align: center;
      padding: 0 20px;
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      color: red;
      font-size: 22px;
      font-weight: 400;
    }

    .right-panel {
      flex: 0 0 auto;
    }
  }
</style>
