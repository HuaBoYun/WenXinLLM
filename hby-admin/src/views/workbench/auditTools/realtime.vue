<template>
  <div style="width: 100%; height: 700px; border: none">
    <!-- <iframe
      :src="url"
      style="width: 100%; height: 700px; border: none"
    ></iframe> -->
  </div>
</template>

<script>
  export default {
    name: 'Realtime',
    data() {
      return {
        // url: 'http://hbbs.dev.cn/?token=7828249DA5BF5C99',
      }
    },

    created() {},
    mounted() {
      window.open(
        'http://hbbs.dev.cn/?token=7828249DA5BF5C99',
        '_blank'
      )
    },
    methods: {},
  }
</script>
