<template>
  <div></div>
</template>

<script>
  export default {
    name: 'ruleConfiguration',
    components: {},
    data() {
      return {
        list: [],
      }
    },
    beforeCreated() {},
    created() {
      this.redirectUrl()
    },
    methods: {
      redirectUrl() {
        window.open('http://192.0.2.19:8090/urule/frame')
      },
    },
  }
</script>
