<template>
  <el-menu-item :index="itemOrMenu.path" @click="handleLink">
    <vab-icon
      v-if="itemOrMenu.meta && itemOrMenu.meta.icon"
      :icon="itemOrMenu.meta.icon"
      :is-custom-svg="true"
      :title="translateTitle(itemOrMenu.meta.title)"
    />
    <span :title="translateTitle(itemOrMenu.meta.title)">
      {{ translateTitle(itemOrMenu.meta.title) }}
    </span>
    <el-tag
      v-if="itemOrMenu.meta && itemOrMenu.meta.badge"
      effect="dark"
      type="danger"
    >
      {{ itemOrMenu.meta.badge }}
    </el-tag>
    <span
      v-if="itemOrMenu.meta && itemOrMenu.meta.dot"
      class="vab-dot vab-dot-error"
    >
      <span />
    </span>
  </el-menu-item>
</template>

<script>
  import { routerMode } from '@/config'
  import { translateTitle } from '@/utils/i18n'
  import { isExternal } from '@/utils/validate'
  import { mapActions, mapGetters } from 'vuex'

  export default {
    name: 'VabMenuItem',
    props: {
      itemOrMenu: {
        type: Object,
        default() {
          return null
        },
      },
    },
    computed: {
      ...mapGetters({
        device: 'settings/device',
      }),
    },
    methods: {
      translateTitle,
      ...mapActions({
        foldSideBar: 'settings/foldSideBar',
      }),
      handleLink() {
        const routePath = this.itemOrMenu.path
        const target = this.itemOrMenu.meta.target
        if (target === '_blank') {
          if (isExternal(routePath)) window.open(routePath)
          else if (this.$route.fullPath !== routePath)
            routerMode === 'hash'
              ? window.open('/#' + routePath)
              : window.open(routePath)
        } else {
          if (isExternal(routePath)) window.location.href = routePath
          else if (this.$route.fullPath !== routePath) {
            if (this.device === 'mobile') this.foldSideBar()
            this.$router.push(this.itemOrMenu.path)
          }
        }
      },
    },
  }
</script>

<style lang="scss" scoped>
  ::v-deep {
    .el-tag {
      float: right;
      height: 16px;
      padding-right: 4px;
      padding-left: 4px;
      margin-top: ($base-menu-item-height - 16) / 2;
      line-height: 16px;
      border: 0;
    }
  }

  .vab-dot {
    float: right;
    margin-top: ($base-menu-item-height - 6) / 2 + 1;
  }
</style>
