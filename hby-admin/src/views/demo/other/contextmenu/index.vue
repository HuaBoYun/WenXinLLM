<template>
  <div class="contextmenu-container">
    <vab-contextmenu ref="contextmenu">
      <vab-contextmenu-item
        v-for="(item, index) in list"
        :key="index"
        :disabled="item.disabled"
        @click="handleMessage(item.title)"
      >
        <vab-icon :icon="item.icon" />
        {{ item.title }}
      </vab-contextmenu-item>
    </vab-contextmenu>

    <el-button v-contextmenu:contextmenu>右键点击此按钮</el-button>
  </div>
</template>

<script>
  import {
    directive,
    VabContextmenu,
    VabContextmenuItem,
  } from '@/extra/VabContextmenu'

  export default {
    name: 'Contextmenu',
    directives: {
      contextmenu: directive,
    },
    components: {
      VabContextmenu,
      VabContextmenuItem,
    },
    data() {
      return {
        list: [
          {
            icon: '24-hours-fill',
            title: '右键菜单-1',
          },
          {
            icon: '4k-line',
            title: '右键菜单-2',
          },
          {
            icon: 'a-b',
            title: '右键菜单-3',
          },
          {
            icon: 'account-box-line',
            title: '右键菜单-4',
          },
          {
            icon: 'account-circle-line',
            title: '右键菜单-5',
            disabled: true,
          },
        ],
      }
    },
    methods: {
      handleMessage(title) {
        this.$baseMessage(
          `您点击了${title}`,
          'success',
          'vab-hey-message-success'
        )
      },
    },
  }
</script>
