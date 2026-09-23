<template>
  <div class="iframe-container">
    <iframe frameborder="0" :src="url" />
  </div>
</template>

<script>
  import { mapActions } from 'vuex'

  export default {
    name: 'Iframe',
    data() {
      return { url: '' }
    },
    created() {
      this.handleIframe()
    },
    methods: {
      ...mapActions({
        changeTabsMeta: 'tabs/changeTabsMeta',
      }),
      handleIframe() {
        this.url = this.$route.query.url
        if (this.$route.query.title)
          this.$nextTick(() => {
            this.changeTabsMeta({
              title: 'Iframe',
              meta: {
                title: this.$route.query.title,
              },
            })
          })
      },
    },
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
