<template>
  <div class="vab-app-main">
    <section>
      <vab-router-view />
    </section>
    <vab-footer />
    <VabAiTest />
  </div>
</template>

<script>
  import VabAiTest from '@/vab/components/VabAi/components/test'
  export default {
    name: 'VabAppMain',
    components: { VabAiTest }
  }
</script>
