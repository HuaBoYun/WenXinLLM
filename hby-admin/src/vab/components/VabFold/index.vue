<template>
  <vab-icon
    class="fold-unfold"
    :icon="collapse ? 'menu-unfold-line' : 'menu-fold-line'"
    @click="toggleCollapse"
  />
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'

  export default {
    name: 'VabFold',
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        collapse: 'settings/collapse',
      }),
    },
    methods: {
      ...mapActions({
        toggleCollapse: 'settings/toggleCollapse',
      }),
    },
  }
</script>

<style lang="scss" scoped>
  .fold-unfold {
    color: $base-color-grey;
    cursor: pointer;
  }
</style>
