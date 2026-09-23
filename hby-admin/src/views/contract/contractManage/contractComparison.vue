<template>
  <div>
    <compareConcent :render="render" />
  </div>
</template>

<script>
  import compareConcent from '@/views/File/components/compareConcent.vue'
  export default {
    components: { compareConcent },
    data() {
      return {
        render: {},
      }
    },
    methods: {
      show(data) {
        this.render = data
      },
      close() {
        
      },
    },
  }
</script>

<style scoped lang="scss"></style>
