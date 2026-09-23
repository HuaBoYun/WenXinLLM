<template>
  <footer class="vab-footer">
    <div class="footer-left">
      <span>当前登录人数：{{ refreshInfo.activeUsersNum }}</span>
      <span style="margin: 0 10px">|</span>
      <span>当前版本：{{ refreshInfo.systemVersion }}</span>
    </div>
    <div class="footer-middle">
      {{ title }}
      {{ title2 }}
      <!-- <vab-icon icon="copyright-line" /> -->
      @
      {{ fullYear }}
    </div>
    <div class="footer-right"></div>
  </footer>
</template>

<script>
  // import { title } from '@/config'
  import { getRefreshInfo } from '@/api/setting/auth'
  export default {
    name: 'VabFooter',
    data() {
      return {
        fullYear: new Date().getFullYear(),
        // title: '黑客户D',
        //title: '中核四0四有限公司',
        title: '星光AI',
        title2: '版权所有',
        refreshInfo: {},
        refreshTimer: null,
      }
    },
    mounted() {
      getRefreshInfo().then((res) => {
        this.refreshInfo = res.data
        this.startRefreshTimer()
      })
    },
    methods: {
      startRefreshTimer() {
        this.refreshTimer = setInterval(() => {
          getRefreshInfo().then((res) => {
            this.refreshInfo = res.data
          })
        }, 1000 * 60 * 3)
      },
      clearRefreshTimer() {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
        }
      },
    },
    beforeDestroy() {
      this.clearRefreshTimer()
    },
  }
</script>

<style lang="scss" scoped>
  .vab-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    min-height: 55px;
    padding: 0 $base-padding 0 $base-padding;
    color: rgba(0, 0, 0, 0.45);
    background: $base-color-white;
    border-top: 1px dashed $base-border-color;

    .footer-left {
      flex: 1;
    }

    .footer-middle {
      flex: 1;
      text-align: center;
    }

    .footer-right {
      flex: 1;
      text-align: right;
    }

    i {
      margin: 0 5px;
    }
  }
</style>
