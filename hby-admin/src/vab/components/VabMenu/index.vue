<template>
  <component
    :is="menuComponent"
    v-if="item.meta && !item.meta.hidden"
    :item-or-menu="item"
  >
    <template v-if="item.children && item.children.length">
      <el-scrollbar
        v-if="
          (layout === 'horizontal' && item.children.length > 18) ||
          (layout !== 'horizontal' && collapse && item.children.length > 18)
        "
        class="vab-menu-children-height"
      >
        <vab-menu
          v-for="route in item.children"
          :key="route.path"
          :item="route"
        />
      </el-scrollbar>
      <template v-else>
        <vab-menu
          v-for="route in item.children"
          :key="route.path"
          :item="route"
        />
      </template>
    </template>
  </component>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'VabMenu',
    props: {
      item: {
        type: Object,
        required: true,
      },
      layout: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        menuComponent: 'VabMenuItem',
      }
    },
    computed: {
      ...mapGetters({
        collapse: 'settings/collapse',
      }),
    },
    created() {
      if (
        this.item.children &&
        this.item.children.some((route) => {
          return route.meta && route.meta.hidden !== true
        })
      )
        this.menuComponent = 'VabSubmenu'
    },
  }
</script>

<style lang="scss" scoped>
  .vab-menu-children-height {
    height: 60vh !important;
  }
</style>
