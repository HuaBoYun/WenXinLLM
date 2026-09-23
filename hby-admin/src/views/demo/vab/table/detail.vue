<template>
  <div class="detail-container">
    <el-page-header
      :content="'【' + route.query.title + '】详情页面'"
      @back="goBack"
    />
    <el-alert
      :closable="false"
      show-icon
      title="详情页菜单高亮，并缓存详情页，返回上一页再次打开可看到效果，dynamicNewTab可配置详情页只打开一个tab页还是可以根据参数不同无限打开多个，当前展示的是可以打开多个"
      type="success"
    />
    <el-form :inline="true" :model="form">
      <el-form-item label="输入框缓存">
        <el-input v-model="form.text" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { mapActions } from 'vuex'
  import { handleActivePath } from '@/utils/routes'

  export default {
    name: 'Detail',
    data() {
      return {
        route: {},
        form: {
          text: '',
        },
      }
    },
    created() {
      const route = this.$route
      this.$nextTick(() => {
        this.changeTabsMeta({
          title: '详情页',
          meta: {
            title: `${route.query.title} 详情页`,
          },
        })
        this.route = {
          path: route.path,
          params: route.params,
          query: route.query,
          name: route.name,
          meta: route.meta,
        }
      })
    },
    methods: {
      ...mapActions({
        changeTabsMeta: 'tabs/changeTabsMeta',
        delVisitedRoute: 'tabs/delVisitedRoute',
      }),
      async goBack() {
        const detailPath = await handleActivePath(this.$route, true)
        await this.$router.push('/vab/table/comprehensiveTable')
        await this.delVisitedRoute(detailPath)
      },
    },
  }
</script>
