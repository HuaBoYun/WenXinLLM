<template>
  <div class="system-log-container"></div>
</template>

<script>
  import { toSsfx } from '@/api/common'

  export default {
    name: '',
    data() {
      return {}
    },
    mounted() {
      this.toSsfxPage()
    },
    created() {},
    methods: {
      async toSsfxPage() {
        const res = await toSsfx()

        window.open(res.data.reporturl, '_blank')
      },
    },
  }
</script>
