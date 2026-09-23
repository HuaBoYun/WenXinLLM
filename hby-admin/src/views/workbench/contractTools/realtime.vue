<template>
  <div>
    <iframe
      :src="url"
      style="width: 100%; height: 700px; border: none"
    ></iframe>
  </div>
</template>

<script>
  export default {
    name: 'Realtime',
    data() {
      return {
        url: 'http://192.0.2.12:9001/reportCenter?pack_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWduVGltZSI6MTYzMjc0OTU2MiwiZXhwIjoxNjMyNzcxMTYyLCJ1c2VybmFtZSI6IjJGNTU2RjE5NDU4MjQ5MjVCNzUzRTg3NjA5RDk2M0MyIn0.U8H-YN_MfetXZmTwhTX5pswFb6mL75ybj0RukCmwGj8&pack_cookieAge=900000',
      }
    },
    created() {},
    methods: {},
  }
</script>
