<template>
  <div class="social-container">
    <el-button type="primary" @click="handleSocialLogin">
      点击进行Github登录
    </el-button>
    <p v-if="data">{{ data }}</p>
  </div>
</template>

<script>
  import { login } from '@/utils/social'

  export default {
    name: 'Social',
    data() {
      return {
        loading: null,
        data: null,
      }
    },
    methods: {
      handleSocialLogin() {
        this.loading = this.$baseLoading()
        login(`https://github.com/login/oauth/authorize`, {
          client_id:
            this.env === 'development'
              ? 'bb8dfd34f6c6a57367e3'
              : 'e104bdc488d009840c4f',
        })
          .then((data) => {
            this.data = data
            this.$store
              .dispatch('user/socialLogin', data)
              .then(() => {
                // 登录页面使用，取消注释
                // const routerPath =
                //  this.redirect === "/404" || this.redirect === "/403"
                //    ? "/"
                //    : this.redirect;
                // this.$router.push(routerPath).catch(() => {});
                this.loading.close()
              })
              .catch(() => {
                this.loading.close()
              })
          })
          .catch(() => {
            this.$baseMessage(
              '第三方登录失败，未返回令牌',
              'error',
              'vab-hey-message-error'
            )
            this.loading.close()
          })
      },
    },
  }
</script>
