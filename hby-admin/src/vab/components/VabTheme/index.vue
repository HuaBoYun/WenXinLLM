<template>
  <span v-if="theme.showTheme">
    <vab-icon
      icon="t-shirt-line"
      @click="handleOpenTheme"
      :style="{ cursor: 'pointer' }"
      title="主题配置"
    />
  </span>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'VabTheme',
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    methods: {
      handleOpenTheme() {
        console.log('🎨 点击主题配置按钮')
        this.$baseEventBus.$emit('theme')
      },
    },
  }
</script>
