<template>
  <div class="system-log-container">
    <!-- 返回数据画像按钮 - 仅在从数据画像跳转过来时显示 -->
    <div v-if="showBackButtons" class="back-buttons-wrapper">
      <data-portrait-back-buttons />
    </div>

    <cjbdi-telescope></cjbdi-telescope>
  </div>
</template>

<script>
  import cjbdiTelescope from './cjbdi/cjbdiTelescope.vue'
  import DataPortraitBackButtons from '@/components/DataPortraitBackButtons'

  export default {
    name: 'Consult',
    components: {
      cjbdiTelescope,
      DataPortraitBackButtons
    },
    data() {
      return {
        showBackButtons: false
      }
    },
    created() {
      this.checkFromDataPortrait()
    },
    methods: {
      checkFromDataPortrait() {
        const { from } = this.$route.query
        if (from === 'dataPortrait' || from === 'dataPortraitScreen') {
          this.showBackButtons = true
        }
      },
    },
  }
</script>
<style scoped>
  /* 返回数据画像按钮样式 */
  .back-buttons-wrapper {
    margin-bottom: 16px;
    padding: 12px 16px;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
</style>
