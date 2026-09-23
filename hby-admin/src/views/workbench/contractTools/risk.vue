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
    name: 'Risk',
    data() {
      return {
        url: 'http://www.huabao.example.com/fxxt/flfxyc',
      }
    },
    created() {},
    methods: {},
  }
</script>
