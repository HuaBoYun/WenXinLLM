<template>
  <div class="iframe-container">
    <iframe frameborder="0" :src="url" />
  </div>
</template>

<script>
  export default {
    name: 'Model',
    data() {
      return { url: 'https://echarts.apache.org/examples/zh/index.html' }
    },
    created() {},
  }
</script>

<style lang="scss" scoped>
  .iframe-container {
    iframe {
      width: 100%;
      height: $base-keep-alive-height;
    }
  }
</style>
