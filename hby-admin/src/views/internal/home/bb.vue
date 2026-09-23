<!-- <template>
  <div>
    <div v-if="loading" class="loading-container">
      <el-loading :loading="loading" text="正在加载数据大屏..."></el-loading>
    </div>
    <div v-else-if="error" class="error-container">
      <el-alert
        title="加载失败"
        :description="error"
        type="error"
        show-icon
        :closable="false"
      ></el-alert>
    </div>
    <iframe
      v-else-if="url"
      :src="url"
      frameborder="0"
      width="95%"
      height="650px"
    ></iframe>
  </div>
</template>

<script>
  import { SSOToJNFD } from '@/api/setting/system'
  export default {
    name: 'Cwztfx',
    data() {
      return {
        url: '',
        loading: true,
        error: null,
      }
    },
    async mounted() {
      try {
        const res = await SSOToJNFD()
        if (!res.data?.ymToken) {
          throw new Error('获取SSO token失败')
        }

        const token = res.data.ymToken

        // 使用环境变量
        const baseUrl = 'https://www.example.com'
        this.url = `${baseUrl}/DataV/view/726734613208114373?token=${encodeURIComponent(
          token
        )}`

        console.log(this.url, '2312321')
        localStorage.setItem('jnpf_token', token)
        localStorage.setItem('token', token)

        this.loading = false
      } catch (error) {
        console.error('加载数据大屏失败:', error)
        this.error = error.message
        this.loading = false
      }
    },
  }
</script>

<style scoped>
  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 700px;
  }

  .error-container {
    padding: 20px;
    max-width: 600px;
    margin: 50px auto;
  }
</style> -->

<template>
  <div>
    <div v-if="loading" class="loading-container">
      <el-loading :loading="loading" text="正在加载数据大屏..."></el-loading>
    </div>
    <div v-else-if="error" class="error-container">
      <el-alert
        title="加载失败"
        :description="error"
        type="error"
        show-icon
        :closable="false"
      ></el-alert>
    </div>
    <div v-else-if="url" ref="iframeContainer" class="iframe-container">
      <!-- 简化的全屏按钮 -->
      <button
        v-show="!isFullscreen"
        class="fullscreen-btn"
        @click="toggleFullscreen"
        title="全屏切换"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path d="M8 3H5a2 2 0 0 0-2 2v3m18 0V5a2 2 0 0 0-2-2h-3m0 18h3a2 2 0 0 0 2-2v-3M3 16v3a2 2 0 0 0 2 2h3"></path>
        </svg>
      </button>
      <iframe
        ref="mainIframe"
        class="iframe-fullscreen"
        :class="{ 'iframe-refreshing': refreshing }"
        :src="url"
        frameborder="0"
        allow="fullscreen"
        title="数据大屏内容"
      ></iframe>
      <div v-if="refreshing" class="refresh-overlay">
        <div class="refresh-text">大屏适配中...</div>
      </div>
    </div>
  </div>
</template>

<script>
  import { SSOToJNFD } from '@/api/setting/system'
  export default {
    name: 'Cwztfx',
    data() {
      return {
        url: '',
        loading: true,
        error: null,
        isFullscreen: false,
        refreshing: false,
      }
    },
    async mounted() {
      try {
        const res = await SSOToJNFD()
        if (!res.data?.ymToken) {
          throw new Error('获取SSO token失败')
        }

        const token = res.data.ymToken

        // 使用环境变量
        const baseUrl = 'https://www.example.com'
        this.url = `${baseUrl}/DataV/view/726734613208114373?token=${encodeURIComponent(
          token
        )}`

        console.log(this.url, '2312321')
        localStorage.setItem('jnpf_token', token)
        localStorage.setItem('token', token)

        document.addEventListener(
          'fullscreenchange',
          this.handleFullscreenChange
        )
        this.loading = false
      } catch (error) {
        console.error('加载数据大屏失败:', error)
        this.error = error.message
        this.loading = false
      }
    },
    beforeDestroy() {
      document.removeEventListener(
        'fullscreenchange',
        this.handleFullscreenChange
      )
    },
    methods: {
      toggleFullscreen() {
        const container = this.$refs.iframeContainer
        if (!container) return

        const doc = document

        if (!doc.fullscreenElement) {
          const request =
            container.requestFullscreen ||
            container.webkitRequestFullscreen ||
            container.msRequestFullscreen

          if (request) {
            request.call(container)
          }
        } else {
          const exit =
            doc.exitFullscreen ||
            doc.webkitExitFullscreen ||
            doc.msExitFullscreen
          if (exit) {
            exit.call(doc)
          }
        }
      },
      handleFullscreenChange() {
        const wasFullscreen = this.isFullscreen
        this.isFullscreen = !!document.fullscreenElement
        
        // 全屏状态改变时都刷新iframe,让DataV大屏自适应新尺寸
        // 进入全屏或退出全屏都需要刷新
        if (wasFullscreen !== this.isFullscreen) {
          this.refreshing = true
          
          // 延迟一下确保全屏动画完成
          setTimeout(() => {
            const iframe = this.$refs.mainIframe
            if (iframe) {
              // 通过重新设置src来刷新iframe
              const currentSrc = iframe.src
              iframe.src = 'about:blank'
              
              // 再次延迟后恢复真实地址
              setTimeout(() => {
                iframe.src = currentSrc
                
                // 监听iframe加载完成
                iframe.onload = () => {
                  setTimeout(() => {
                    this.refreshing = false
                  }, 500)
                }
              }, 100)
            }
          }, 200)
        }
      },
    },
  }
</script>

<style scoped>
  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 700px;
  }

  .error-container {
    padding: 20px;
    max-width: 600px;
    margin: 50px auto;
  }

  .iframe-container {
    position: relative;
    width: 100%;
    height: calc(100vh - 140px);
    min-height: 650px;
    background: #000;
    border-radius: 12px;
    overflow: hidden;
  }

  /* 全屏按钮样式 */
  .fullscreen-btn {
    position: absolute;
    top: 20px;
    right: 20px;
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.95);
    border: none;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    cursor: pointer;
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #409eff;
    transition: all 0.3s ease;
    backdrop-filter: blur(6px);
  }

  .fullscreen-btn:hover {
    background: rgba(255, 255, 255, 1);
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.2);
    transform: scale(1.05);
  }

  .fullscreen-btn:active {
    transform: scale(0.95);
  }

  /* iframe样式 */
  .iframe-fullscreen {
    width: 100%;
    height: 100%;
    border: none;
    background: #000;
    transition: opacity 0.3s ease;
  }

  .iframe-refreshing {
    opacity: 0.3;
  }

  .refresh-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.5);
    z-index: 100;
    pointer-events: none;
  }

  .refresh-text {
    padding: 16px 32px;
    background: rgba(0, 0, 0, 0.8);
    color: #fff;
    border-radius: 8px;
    font-size: 16px;
    letter-spacing: 1px;
  }

  /* 全屏模式下的样式 */
  .iframe-container:fullscreen {
    width: 100vw;
    height: 100vh;
    border-radius: 0;
  }

  .iframe-container:fullscreen .iframe-fullscreen {
    width: 100%;
    height: 100%;
  }

  /* 兼容webkit内核浏览器 */
  .iframe-container:-webkit-full-screen {
    width: 100vw;
    height: 100vh;
    border-radius: 0;
  }

  .iframe-container:-webkit-full-screen .iframe-fullscreen {
    width: 100%;
    height: 100%;
  }
</style>
