<template>
  <div
    class="logo-container"
    :class="{
      ['logo-container-' + 'float']: true,
    }"
    style="background: transparent !important"
  >
    <transition name="fade">
      <div class="arc-shadow" v-show="!isHovering"></div>
    </transition>
    <span class="logo">
      <!-- 使用自定义svg示例 -->

      <img
        :src="imgUrl"
        alt=""
        style="width: 32px"
        @click="showAllMenu"
        v-if="
          imgUrl && theme.layout !== 'custom' && theme.layout !== 'tradition'
        "
      />

      <img
        :src="customUrl"
        alt=""
        style="
          width: 32px;
          margin-left: 5px;
          position: absolute;
          z-index: 100;
          top: 50%;
          transform: translateY(-50%);
        "
        @click="showAllMenu"
        @mouseenter="showTooltip"
        @mouseleave="hideTooltip"
        v-if="
          customUrl &&
          (theme.layout === 'custom' || theme.layout === 'tradition')
        "
      />

      <vab-icon
        v-if="!imgUrl"
        :icon="logo"
        is-custom-svg
        @click="showAllMenu"
      />
      <div
        v-show="isTooltipVisible"
        class="tooltip"
        :style="tooltipStyle"
        @mouseenter="showTooltip"
        @mouseleave="hideTooltip"
      >
        <RecentMenu />
      </div>
    </span>
    <span
      class="title"
      v-if="theme.layout !== 'custom' && theme.layout !== 'tradition'"
      :class="{
        'hidden-xs-only': theme.layout === 'horizontal',
      }"
    >
      XingGuang
    </span>
    <AllMenu ref="allMenu" />
    <!-- 弧形阴影背景 -->
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import defaultModuleLogo from '@/assets/module_logo.png'
  import customLogo from '@/assets/y_logo.png'
  import AllMenu from '@/components/AllMenu/AllMenu'
  import RecentMenu from './menu.vue'

  export default {
    name: 'VabLogoCustom',
    components: { AllMenu, RecentMenu },
    data() {
      return {
        imgUrl: defaultModuleLogo,
        customUrl: customLogo,
        isMenuOpen: false,
        isTooltipVisible: false,
        isHovering: false,
        tooltipStyle: {
          left: '0px',
          top: '0px',
        },
        tooltipTimer: null,
      }
    },
    computed: {
      ...mapGetters({
        logo: 'settings/logo',
        title: 'settings/title',
        theme: 'settings/theme',
      }),
    },
    created() {
      this.imgUrl =
        localStorage.getItem('UsingModuleLogoPic') || defaultModuleLogo
    },
    methods: {
      showAllMenu() {
        if (this.$refs.allMenu) {
          if (this.isMenuOpen) {
            this.$refs.allMenu.close()
            this.isMenuOpen = false
          } else {
            this.$refs.allMenu.show()
            this.isMenuOpen = true
          }
        }
      },
      showTooltip(event) {
        if (this.tooltipTimer) {
          clearTimeout(this.tooltipTimer)
          this.tooltipTimer = null
        }

        this.isHovering = true

        if (event.target.tagName.toLowerCase() === 'img') {
          const imgElement = event.target
          const rect = imgElement.getBoundingClientRect()

          this.tooltipStyle = {
            left: `${rect.left + 100}px`,
            top: `${rect.bottom + 10}px`,
          }
        }
        this.isTooltipVisible = true
      },
      hideTooltip() {
        this.tooltipTimer = setTimeout(() => {
          this.isTooltipVisible = false
          this.isHovering = false
        }, 100)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .el-drawer__wrapper {
    position: fixed;
    top: 60px;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    margin: 0;
  }
  @mixin container {
    position: relative;
    height: $base-top-bar-height;
    overflow: hidden;
    line-height: $base-top-bar-height;
    background: transparent !important;
  }

  @mixin logo {
    display: inline-block;
    width: 32px;
    height: 32px;
    color: $base-title-color;
    vertical-align: middle;
  }

  @mixin title {
    display: inline-block;
    margin-left: 10px;
    overflow: hidden;
    font-size: 20px;
    line-height: 55px;
    color: $base-title-color;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
    font-weight: 600;
  }

  .logo-container {
    position: relative;
    width: 100px;
    &-horizontal,
    &-common {
      @include container;

      .logo {
        svg,
        img {
          @include logo;
        }
      }

      .title {
        @include title;
      }
    }

    &-vertical,
    &-column,
    &-comprehensive,
    &-float {
      @include container;

      height: $base-logo-height;
      line-height: $base-logo-height;
      // text-align: center;

      .logo {
        svg,
        img {
          @include logo;
        }
      }

      .title {
        @include title;
        max-width: $base-left-menu-width - 60;
      }
    }

    &-column {
      background: $base-column-second-menu-background !important;

      .logo {
        position: fixed;
        top: 0;
        display: block;
        width: $base-left-menu-width-min;
        height: $base-logo-height;
        margin: 0;
        background: $base-column-first-menu-background;
      }

      .title {
        padding-right: 15px;
        padding-left: 15px;
        margin-left: $base-left-menu-width-min !important;
        color: $base-color-black !important;
        background: $base-column-second-menu-background !important;
        @include title;
      }
    }
  }

  .tooltip {
    position: fixed;
    padding: 8px 12px;
    color: white;
    border-radius: 4px;
    font-size: 14px;
    z-index: 9999;
    transform: translateX(-50%); // 使tooltip水平居中对齐
  }
  .arc-shadow {
    position: absolute;
    right: 38px;
    bottom: 0;
    z-index: 1;
    border-radius: 0 0 25% 0;
    width: 200px;
    height: 200px;
    background: rgba(255, 255, 255, 0.2);
    transition: opacity 0.3s ease;
  }

  .fade-enter-active,
  .fade-leave-active {
    transition: opacity 0.3s ease;
  }

  .fade-enter-from,
  .fade-leave-to {
    opacity: 0;
  }
  .vab-theme-white {
    .arc-shadow {
      position: absolute;
      right: 38px;
      bottom: 0;
      z-index: 1;
      width: 200px;
      height: 200px;
      background: rgba(255, 255, 255, 0.2);
      transition: opacity 0.3s ease;
      border-right: none;
      border-radius: 0;

      &::after {
        content: '';
        position: absolute;
        top: 76%;
        right: 0;
        width: 2.5px;
        height: 22%;
        background: linear-gradient(
          to bottom,
          transparent 0%,
          rgba(0, 0, 0, 0.5) 50%,
          rgba(0, 0, 0, 0.5) 50%,
          rgba(0, 0, 0, 0.5) 50%,
          transparent 100%
        );
      }
    }
  }
</style>
