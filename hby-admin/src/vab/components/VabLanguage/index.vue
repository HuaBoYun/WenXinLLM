<template>
  <el-dropdown v-if="theme.showLanguage" @command="handleCommand">
    <vab-icon icon="translate" />
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item command="zh">中文简体</el-dropdown-item>
        <el-dropdown-item command="en">English</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import getPageTitle from '@/utils/pageTitle'

  export default {
    name: 'VabLanguage',
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    methods: {
      ...mapActions({
        changeLanguage: 'settings/changeLanguage',
      }),
      handleCommand(command) {
        this.changeLanguage(command)
        this.$i18n.locale = command
        document.title = getPageTitle(this.$route.meta.title)
      },
    },
  }
</script>
