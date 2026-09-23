<template>
  <div class="container">
    <!-- 侧边栏 -->
    <transition
      name="custom-classes-transition"
      enter-active-class="animate__animated animate__slideInLeft"
      leave-active-class="animate__animated animate__slideOutLeft"
    >
      <div class="side-bar" v-show="sideBarVisible">
        <div class="side-bar-header">
          <div class="avator">
            <img :src="AiAvator" alt="" />
            <div class="title">问心AI</div>
          </div>
          <img
            :src="eclipes"
            alt=""
            @click="sideBarVisible = !sideBarVisible"
            style="width: 20px;height: 20px;cursor: pointer;opacity: 0.3;"
          >
        </div>
        <AIMenu @chatNow="onChatNow" @onMenuItemDefaultClick="onMenuItemDefaultClick" @onMenuItemClick="onMenuItemClick" :dialogueList="dialogueList" />
      </div>
    </transition>

    <!-- 内容区 -->
    <transition>
      <div
        class="content"
        :class="{
          narrow_width: sideBarVisible,
          enlarge_width: !sideBarVisible,
        }"
      >
        <!-- 内容头部 -->
        <div class="content-header df aic">
          <div
            @click="sideBarVisible = !sideBarVisible"
            @mouseenter="onMenuIconMouseenter"
            @mouseleave="onMenuIconMouseleave"
            v-show="!sideBarVisible"
            class="eclipse-icon animate__animated animate__slideInLeft animate__delay0.5s"
          >
            <img
              :src="eclipes"
              alt=""
              style="width: 20px;height: 20px;"
            >
            <transition
              name="custom-classes-transition"
              enter-active-class="animate__animated animate__slideInLeft"
              leave-active-class="animate__animated animate__slideOutLeft"
            >
              <div class="side-bar side-bar-float" v-show="floatMenuVisible">
                <AIMenu @chatNow="onChatNow" @onMenuItemDefaultClick="onMenuItemDefaultClick" @onMenuItemClick="onMenuItemClick" :dialogueList="dialogueList" />
              </div>
            </transition>
          </div>
          <el-button v-show="!sideBarVisible" round class="icon-btn" @click="onChatNow" style="color: #75a4ff;border-color: #75a4ff;">
            <i :class="`icon el-icon-plus`" style="margin-right: 4px"></i>
            <span>新对话</span>
          </el-button>
        </div>

        <!-- 内容主体 -->
        <div class="search-content">
          <transition
              name="custom-classes-transition"
              enter-active-class="animate__animated animate__slideInUp"
            >
            <component
              :is="componentName"
              :key="componentKey"
              @chatNow="onChatNow"
              @readChat="onReadChat"
              :msgData="msgData"
              :menuInfo="menuInfo"
              @fetchAgentDialogue="fetchAgentDialogue"
            />
          </transition>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
  import AIMenu from './AI/AIMenu.vue'
  import AIChat from './AI/AIChat.vue'
  import AINewConversation from './AI/AINewConversation.vue'
  import AISearch from './AI/AISearch.vue'
  import AISearchL from './AI/AISearchL.vue'
  import AIWrite from './AI/AIWrite.vue'
  import AIWriteLegacy from './AI/AIWriteLegacy.vue'
  import AIConsult from './AI/AIConsult.vue'
  import AIProgramming from './AI/AIProgramming.vue'
  import AIImage from './AI/AIImage.vue'
  import AIRead from './AI/AIRead.vue'
  import AICode from './AI/AICode.vue'
  import AIReadDetail from './AI/AIReadDetail.vue'
  import AISmartModel from './AI/AISmartModel.vue'
  import ctrl from '@/views/index/components/AI/icon/ctrlk.svg'
  import eclipes from '@/views/index/components/AI/icon/eclipes.svg'
  import AiAvator from './AI/img/ai-avator.png'
  import { getAgentDialogueList, sageOrUpdateAgentDialogue } from '@/api/ai/index.js'

  export default {
    name: 'AI',
    components: { AIMenu, AINewConversation, AISearch, AISearchL, AIWrite, AIWriteLegacy, AIConsult, AIProgramming, AIChat, AIImage, AIRead, AICode, AIReadDetail, AISmartModel },
    data() {
      return {
        drawer: false,
        direction: 'ltr',
        sideBarVisible: false,
        floatMenuVisible: false,
        ctrl,
        eclipes,
        AiAvator,
        msgData: {},
        componentName: 'AINewConversation',
        componentKey: 0,
        menuInfo: {},
        dialogueList: []
      }
    },
    created() {
      this.onMenuIconMouseenterDebonce = this.debounce(this.onMenuIconMouseenterDebonce, 150);
      this.onMenuIconMouseleaveDebonce = this.debounce(this.onMenuIconMouseleaveDebonce, 150);
      this.fetchAgentDialogue()
    },
    mounted() {
      document.addEventListener('keydown', this.ctrlK)
    },
    beforeDestroy() {
      document.removeEventListener('keydown', this.ctrlK)
    },
    methods: {
      async fetchAgentDialogue() {
        getAgentDialogueList({pageSize: 5}).then(res => {
          console.log('res', res)
          if (res && res.code == 200) {
            this.dialogueList = res.data.tlist
          }
        })
      },
      onChatNow(data) {
        this.msgData = data
        this.componentName = 'AIChat'
        this.componentKey += 1
      },
      onReadChat(data) {
        this.msgData = data
        this.componentName = 'AIReadDetail'
        this.componentKey += 1
      },
      ctrlK(event) {
        if (event.ctrlKey) {
          if (event.key === 'k' || event.key === 'K') {
              event.preventDefault();
              this.onChatNow()
          }
        }
      },
      onMenuItemDefaultClick(componentName) {
        this.componentName = componentName
        this.componentKey += 1
      },
      onMenuItemClick(menuInfo, msgData) {
        this.msgData = msgData
        this.menuInfo = menuInfo || {}
        this.componentName = menuInfo.name
        this.componentKey += 1
      },
      debounce(fn, delay) {
        let timer = null;
        return function (...args) {
          if (timer) clearTimeout(timer);
          timer = setTimeout(() => {
            fn.apply(this, args);
          }, delay);
        };
      },
      onMenuIconMouseenter() {
        this.onMenuIconMouseenterDebonce()
      },
      onMenuIconMouseenterDebonce() {
        this.floatMenuVisible = true
      },
      onMenuIconMouseleave() {
        this.onMenuIconMouseleaveDebonce()
      },
      onMenuIconMouseleaveDebonce() {
        this.floatMenuVisible = false
      },
    },
  }
</script>

<style scoped>

  #app {
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }
  #app::-webkit-scrollbar {
    width: 0;
    height: 0;
  }
  .container {
    /* overflow: hidden; */
    background: #f7f8faff;
  }

  .content {
    background: #f7f8faff;
    width: 100%;
    height: calc(100vh - 50px);
    padding: 0 0;
    /* overflow: auto; */
    margin: 0 auto;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }

  .content::-webkit-scrollbar {
      width: 0;
      height: 0;
  }

  .content-header {
    position: fixed;
    left: 0;
    top: 50px;
    width: 100%;
    z-index: 10;
  }

  .side-bar {
    height: calc(100vh - 50px);
    width: 280px;
    background: #f3f4f6;
    border-right: 0.5px solid rgba(0, 0, 0, 0.08);
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
    padding-bottom: 12px;
    position: fixed;
    left: 0;
    top: 50px;
    z-index: 11;
    overflow: auto;
  }

  .eclipse-icon {
    width: 50px;
    height: 50px;
    line-height: 32x;
    font-size: 24px;
    cursor: pointer;
    padding: 12px 16px 20px 16px;
  }

  .side-bar-float {
    height: 760px;
    width: 260px;
    display: flex;
    padding-bottom: 12px;
    flex-direction: column;
    position: relative;
    flex-shrink: 0;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
    border-radius: 12px;
    font-size: 14px;
    top: 8px;
  }

  .animate__animated {
    --animate-duration: 0.5s;
  }

  .narrow_width {
    -webkit-animation: narrow 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: narrow 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }
  .enlarge_width {
    -webkit-animation: enlarge 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: enlarge 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }

  @-webkit-keyframes narrow {
    0% {
      padding-left: 0;
    }
    100% {
      padding-left: 280px;
    }
  }
  @keyframes narrow {
    0% {
      padding-left: 0;
    }
    100% {
      padding-left: 280px;
    }
  }
  @-webkit-keyframes enlarge {
    0% {
      padding-left: 280px;
    }
    100% {
      padding-left: 0;
    }
  }
  @keyframes enlarge {
    0% {
      padding-left: 280px;
    }
    100% {
      padding-left: 0;
    }
  }

  .animate__slideInLeft {
    -webkit-animation: slideInLeft 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: slideInLeft 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }
  @keyframes slideInLeft {
    from { transform: translateX(-100%); }
    to { transform: translateX(0); }
  }
  .animate__slideOutLeft {
    -webkit-animation: slideOutLeft 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: slideOutLeft 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }
  @keyframes slideOutLeft {
    from { transform: translateX(0); }
    to { transform: translateX(-100%); }
  }
  .animate__slideInUp {
    -webkit-animation: slideInUp 0.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: slideInUp 0.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }
  @keyframes slideInUp {
    from { transform: translateY(100%); }
    to { transform: translateY(0); }
  }
  .animate__slideInRight {
    -webkit-animation: slideInRight 0.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
    animation: slideInRight 0.2s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  }
 @keyframes slideInRight {
     from {
      -webkit-transform: translate3d(100%, 0, 0);
      transform: translate3d(100%, 0, 0);
      visibility: visible;
     }
 
     to {
      -webkit-transform: translate3d(0, 0, 0);
      transform: translate3d(0, 0, 0);
     }
 }

  .side-bar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    padding-bottom: 6px;
    padding-left: 16px;
    padding-right: 10px;
    position: sticky;
    top: 0;
    background: #f3f4f6;
    z-index: 10;
  }

  .side-bar-header .avator {
    display: flex;
    align-items: center;
  }

  .side-bar-header .title {
    margin-left: 8px;
    color: #1f2329;
    font-size: 16px;
  }

  .side-bar-header .avator img {
    width: 36px;
    height: 36px;
    border-radius: 50%;
  }

  .side-bar-menu {
    padding: 0 12px;
  }

  .menu-item {
    display: flex;
    align-items: center;
    height: 38px;
    border-radius: 12px;
    cursor: pointer;
    padding: 8px 6px;
    margin-bottom: 4px;
  }

  .menu-item-actived {
    background: #fff;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    font-weight: 600;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .menu-item-default {
    background: rgba(0, 87, 255, 0.06);
    border: 0.5px solid rgba(0, 102, 255, 0.15);
    margin-top: 16px;
    margin-bottom: 6px;
    font-weight: 600;
  }

  .menu-icon-default {
    color: #0057ff;
  }
  .menu-title-default {
    color: #0057ff;
  }

  .menu-item-hover {
    background: rgba(0, 0, 0, 0.04);
  }

  .menu-title {
    margin-left: 8px;
  }

  .search-content {
    position: relative;
    height: 100%;
    /* width: 810px; */
    margin: 0 auto;
    overflow: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
    display: flex;
    justify-content: center;
  }

  .icon-style {
    width: 100%;
    height: 100%;
  }
  
  .df {
    display: flex;
  }

  .aic {
    align-items: center;
  }
</style>
