<template>
  <div class="right-toolbar">
    <el-button
      size="mini"
      icon="el-icon-search"
      circle
      @click="toggleSearch"
    />
  </div>
</template>

<script>
export default {
  name: 'RightToolbar',
  props: {
    showSearch: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    toggleSearch() {
      this.$emit('update:showSearch', !this.showSearch)
      this.$emit('queryTable')
    }
  }
}
</script>

<style lang="scss" scoped>
.right-toolbar {
  display: inline-block;
  float: right;
}
</style>
