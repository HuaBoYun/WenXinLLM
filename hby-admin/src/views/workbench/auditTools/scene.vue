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
      window.open('http://192.0.2.12:9001/reportCenter?pack_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWduVGltZSI6MTY1OTg3MjQ3MCwiZXhwIjoxNjU5ODk0MDcwLCJ1c2VybmFtZSI6IjJGNTU2RjE5NDU4MjQ5MjVCNzUzRTg3NjA5RDk2M0MyIn0.0t3IaTxRgVh0w5KjDf6QcJ6AYuy3L7YTWyHvlSi87ek&pack_cookieAge=900000', '_blank');
    },
    methods: {},
  }
</script>
