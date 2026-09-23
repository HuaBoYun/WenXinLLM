<template>
  <div>
    <vab-icon icon="font-color" @click="handleOpenAI" />
    <!-- <VabAiTest /> -->
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  // import VabAiTest from './components/test'
  export default {
    name: 'VabAi',
    // components: { VabAiTest },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    methods: {
      handleOpenAI() {
        this.$baseEventBus.$emit('newAi')
      },
    },
  }
</script>
<style lang="scss" scoped>
  .module-view {
    text-align: center;
    width: 100%;
    max-height: 470px;
    overflow-x: hidden;
    margin-bottom: -10px;
    .module-item {
      background: white;
      padding: 1px 18px;
      margin: 2px 4px 15px 4px;
      box-shadow: 1px 1px 3px #888888;
      cursor: pointer;
      .menu-img {
        font-size: 48px;
      }
    }
    .module-item:hover {
      box-shadow: 1px 1px 8px #888888;
    }
  }
  .tank {
    height: 450px !important;
  }
  .five {
    padding: 1px 11px !important;
  }
  .two {
    padding: 1px 22px !important;
  }
  .three {
    padding: 1px 22px !important;
  }
</style>
