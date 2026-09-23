<template>
  <div
    class="logo-container"
    :class="{
      ['logo-container-' + theme.layout]: true,
    }"
  >
    <router-link to="/">
      <span class="logo">
        <!-- 使用自定义svg示例 -->
        <img
          :src="imgUrl"
          alt=""
          style="width: 32px"
          @click="changeModel"
          v-if="imgUrl"
        />
        <vab-icon
          v-if="!imgUrl"
          :icon="logo"
          is-custom-svg
          @click="changeModel"
        />
        <!-- <div style="width: 74px; height: 67px" @click="changeModel">1231</div> -->
      </span>
      <span
        class="title"
        :class="{ 'hidden-xs-only': theme.layout === 'horizontal' }"
      >
        {{ title }}
      </span>
    </router-link>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import defaultModuleLogo from '@/assets/module_logo.png' //默认模块logo图

  export default {
    name: 'VabLogo',
    data() {
      return {
        imgUrl: defaultModuleLogo,
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
      changeModel() {
        localStorage.setItem('model', 'home')
        localStorage.setItem('modelname', '切换模块')
        location.href = '/'
      },
    },
  }
</script>

<style lang="scss" scoped>
  @mixin container {
    position: relative;
    height: $base-top-bar-height;
    overflow: hidden;
    line-height: $base-top-bar-height;
    background: transparent;
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
    margin-left: 5px;
    overflow: hidden;
    font-size: 20px;
    line-height: 55px;
    color: $base-title-color;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
  }

  .logo-container {
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
    &-common {
      margin-left: 45px;
    }

    &-vertical,
    &-column,
    &-comprehensive,
    &-float {
      @include container;

      height: $base-logo-height;
      line-height: $base-logo-height;
      text-align: center;

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
</style>
