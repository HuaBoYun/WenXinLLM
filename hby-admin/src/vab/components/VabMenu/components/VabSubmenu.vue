<template>
  <div v-if="itemOrMenu.meta && itemOrMenu.meta.levelHidden">
    <template v-for="route in itemOrMenu.children">
      <vab-menu :key="route.path" :item="route" />
    </template>
  </div>
  <el-submenu
    v-else
    ref="subMenu"
    :index="itemOrMenu.path"
    :popper-append-to-body="false"
  >
    <template slot="title">
      <vab-icon
        v-if="itemOrMenu.meta && itemOrMenu.meta.icon"
        :icon="itemOrMenu.meta.icon"
        :is-custom-svg="true"
        :title="translateTitle(itemOrMenu.meta.title)"
      />
      <span :title="translateTitle(itemOrMenu.meta.title)">
        {{ translateTitle(itemOrMenu.meta.title) }}
      </span>
    </template>
    <slot />
  </el-submenu>
</template>

<script>
  import { translateTitle } from '@/utils/i18n'

  export default {
    name: 'VabSubmenu',
    props: {
      itemOrMenu: {
        type: Object,
        default() {
          return null
        },
      },
    },
    methods: {
      translateTitle,
    },
  }
</script>
