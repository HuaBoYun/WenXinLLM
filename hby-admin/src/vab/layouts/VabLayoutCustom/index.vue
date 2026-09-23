<template>
  <!-- 横向布局 -->
  <div
    class="vab-layout-custom"
    :class="{
      fixed: fixedHeader,
      'no-tabs-bar': !showTabs,
    }"
  >
    <div
      class="vab-layout-header"
      :class="{
        'fixed-header': fixedHeader,
      }"
    >
      <vab-header-custom />
      <div
        v-show="showTabs"
        :class="{
          'vab-tabs-custom': showTabs,
        }"
      >
        <div class="vab-main">
          <vab-tabs />
        </div>
      </div>
    </div>
    <div class="vab-main main-padding">
      <vab-app-main />
    </div>
  </div>
</template>

<script>
  import VabHeaderCustom from "@/vab/components/VabHeader/custom";
  export default {
    name: 'VabLayoutCustom',
    components: {VabHeaderCustom},
    props: {
      collapse: {
        type: Boolean,
        default() {
          return false
        },
      },
      fixedHeader: {
        type: Boolean,
        default() {
          return true
        },
      },
      showTabs: {
        type: Boolean,
        default() {
          return true
        },
      },
      device: {
        type: String,
        default() {
          return 'desktop'
        },
      },
    },
  }
</script>

<style lang="scss" scoped>
  .vab-layout-custom {
    ::v-deep {
      .vab-main {
        width: 100% !important;
        margin: auto;
      }
    }

    .vab-tabs-custom {
      background: $base-color-white;
      box-shadow: $base-box-shadow;
    }

    .vab-nav {
      .fold-unfold {
        display: none;
      }
    }
  }
</style>
