<template>
  <!--浮动布局 -->
  <div
    class="vab-layout-float"
    :class="{
      fixed: fixedHeader,
      'no-tabs-bar': !showTabs,
    }"
  >
    <vab-side-bar layout="float" />
    <div class="vab-main">
      <div
        class="vab-layout-header"
        :class="{
          'fixed-header': fixedHeader,
        }"
      >
        <vab-nav layout="float" />
        <vab-tabs v-show="showTabs" />
      </div>
      <vab-app-main />
    </div>
  </div>
</template>

<script>
  import { mapActions } from 'vuex'

  export default {
    name: 'VabLayoutFloat',
    props: {
      fixedHeader: {
        type: Boolean,
        default() {
          return true
        },
      },
      showTabs: {
        type: Boolean,
        default() {
          return true
        },
      },
    },
    created() {
      //浮动布局不允许合并展开
      this.foldSideBar()
    },
    destroyed() {
      this.openSideBar()
    },
    methods: {
      ...mapActions({
        foldSideBar: 'settings/foldSideBar',
        openSideBar: 'settings/openSideBar',
      }),
    },
  }
</script>

<style lang="scss" scoped>
  .vab-layout-float {
    .vab-main {
      margin-left: $base-left-menu-width-min;
      .fixed-header {
        left: $base-left-menu-width-min;
        width: calc(100% - #{$base-left-menu-width-min});
      }
    }
    ::v-deep {
      .el-menu--collapse.el-menu li.el-submenu.is-active {
        .el-submenu__title {
          background-color: transparent !important;
        }
        > .el-submenu__title {
          background-color: $base-color-blue !important;
        }
      }

      .vab-menu-children-height {
        height: auto !important;
      }

      .el-menu {
        &--vertical {
          .el-menu--popup-right-start {
            width: 335px !important;
            .el-submenu__title,
            .el-menu-item {
              float: left;
              width: 160px;
              min-width: 160px;
              margin: 0 0 5px 5px;
              border-radius: $base-border-radius;
            }
            /* .el-submenu__title:nth-child(even),
            .el-menu-item:nth-child(even) {
              margin: 5px;
            } */
          }
        }
      }
    }
  }
</style>
