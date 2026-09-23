<template>
  <el-breadcrumb class="vab-breadcrumb" separator=">">
    <template v-for="(item, index) in breadcrumbList">
      <el-breadcrumb-item :key="index" :to="{ path: item.redirect }">
        <vab-icon v-if="item.meta && item.meta.icon" :icon="item.meta.icon" />
        {{ translateTitle(item.meta.title) }}
      </el-breadcrumb-item>
    </template>
  </el-breadcrumb>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { translateTitle } from '@/utils/i18n'

  export default {
    name: 'VabBreadcrumb',
    computed: {
      ...mapGetters({
        routes: 'routes/routes',
      }),
      breadcrumbList() {
        return this.getBreadcrumbList(this.routes)
      },
    },
    methods: {
      translateTitle,
      getBreadcrumbList(routes) {
        return routes
          .filter(
            (route) => route.childrenPathList.indexOf(this.$route.path) + 1
          )
          .flatMap((route) =>
            route.children
              ? [route, ...this.getBreadcrumbList(route.children)]
              : [route]
          )
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-breadcrumb {
    height: $base-nav-height;
    font-size: $base-font-size-default;
    line-height: $base-nav-height;

    ::v-deep {
      .el-breadcrumb__item {
        .el-breadcrumb__inner {
          a {
            font-weight: normal;
            color: #515a6e;
          }
        }

        .is-link {
          font-weight: normal;
          color: #515a6e;
        }

        &:last-child {
          .el-breadcrumb__inner {
            a {
              color: #999;
            }
          }
        }
      }
    }
  }
</style>
