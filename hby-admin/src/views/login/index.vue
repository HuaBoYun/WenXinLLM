<template>
  <div id="login-container">
    <el-row>
      <el-col :lg="14" :md="11" :sm="24" :xl="14" :xs="24">
        <div style="color: transparent">占位符</div>
      </el-col>
      <el-col :lg="9" :md="12" :sm="24" :xl="9" :xs="24">
        <el-form
          ref="form"
          class="login-form"
          label-position="left"
          :model="form"
          :rules="rules"
        >
          <!--          <div class="title">hello !</div>-->
          <div class="title-tips">
            <!-- {{ translateTitle('欢迎来到') }}{{ title }}{{ '数字化平台' }}！ -->
            星光问心AI大模型平台
          </div>
          <el-tabs v-model="activeName" class="login-tabs">
            <el-tab-pane label="账号密码登录" name="account">
              <el-form-item prop="userName">
                <el-input
                  v-model.trim="form.userName"
                  v-focus
                  :placeholder="translateTitle('请输入用户名')"
                  tabindex="1"
                  type="text"
                  @keyup.enter.native="handleLogin"
                >
                  <template #prefix>
                    <vab-icon icon="user-line" />
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  :key="passwordType"
                  ref="password"
                  v-model.trim="form.password"
                  :placeholder="translateTitle('请输入密码')"
                  tabindex="2"
                  :type="passwordType"
                  @keyup.enter.native="handleLogin"
                >
                  <template #prefix>
                    <vab-icon icon="lock-line" />
                  </template>
                  <template v-if="passwordType === 'password'" #suffix>
                    <vab-icon
                      class="show-password"
                      icon="eye-off-line"
                      @click="handlePassword"
                    />
                  </template>
                  <template v-else #suffix>
                    <vab-icon
                      class="show-password"
                      icon="eye-line"
                      @click="handlePassword"
                    />
                  </template>
                </el-input>
              </el-form-item>
              <!-- 验证码验证逻辑需自行开发，如不需要验证码功能建议注释 -->
              <el-form-item prop="verifyCode">
                <el-input
                  v-model.trim="form.verifyCode"
                  :placeholder="translateTitle('验证码') + previewText"
                  tabindex="3"
                  type="text"
                  @keyup.enter.native="handleLogin"
                >
                  <template #prefix>
                    <vab-icon icon="barcode-box-line" />
                  </template>
                </el-input>
                <el-image class="code" :src="codeUrl" @click="changeCode" />
              </el-form-item>
              <el-button
                class="login-btn"
                :loading="loading"
                type="primary"
                @click="handleLogin"
              >
                {{ translateTitle('登录') }}
              </el-button>
            </el-tab-pane>
            <el-tab-pane label="扫码登录" name="qrcode">
              <div class="tab-qrcode">
                <!-- <img
                  alt=""
                  src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01ad3657e6a42c0000012e7e6693c1.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1640199235&t=3198c3b26cb92fd83c8620cf0802d79c"
                  style="width: 100%; padding: 10px"
                /> -->
                <div id="container">
                  <div
                    id="wxLogin"
                    style="width: 100%; text-align: center"
                  ></div>
                </div>
                <!-- <div style="color: white; text-align: center">
                  请使用微信扫描二维码登录
                  <br />
                  “客户A投资”
                </div> -->
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </el-col>
      <el-col :lg="1" :md="1" :sm="24" :xl="1" :xs="24">
        <div style="color: transparent">占位符</div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'
  import { baseURL } from '@/config'
  import { translateTitle } from '@/utils/i18n'
  import { isPassword } from '@/utils/validate'
  import { getCompanyImg, codeLogin } from '@/api/setting/loginPage'
  import defaultImg from '@/assets/login_images/background-old.jpg' //默认背景图

  export default {
    name: 'Login',
    directives: {
      focus: {
        inserted(el) {
          el.querySelector('input').focus()
        },
      },
    },
    beforeRouteLeave(to, from, next) {
      clearInterval(this.timer)
      next()
    },
    data() {
      const validateUsername = (rule, value, callback) => {
        if ('' === value)
          callback(new Error(this.translateTitle('用户名不能为空')))
        else callback()
      }
      const validatePassword = (rule, value, callback) => {
        if (!isPassword(value))
          callback(new Error(this.translateTitle('密码不能少于6位')))
        else callback()
      }
      return {
        activeName: 'account',
        wxLoginLoaded: false,
        form: {
          userName: '',
          password: '',
          verifyCode: '',
        },
        rules: {
          userName: [
            {
              required: true,
              trigger: 'blur',
              validator: validateUsername,
            },
          ],
          password: [
            {
              required: true,
              trigger: 'blur',
              validator: validatePassword,
            },
          ],
          /* verifyCode: [
              {
                required: true,
                trigger: 'blur',
                message: '验证码不能空',
              },
            ], */
        },
        loading: false,
        passwordType: 'password',
        redirect: undefined,
        timer: 0,
        codeUrl:
          baseURL +
          `/setting/login/getVerificationCode?timestamp=${new Date().getTime()}`,
        previewText: '',
        QRCodeStyle: `data:text/css;base64,QGNoYXJzZXQgIlVURi04IjsNCi5pbXBvd2VyQm94IC50aXRsZSB7ZGlzcGxheTogbm9uZTt9DQouaW1wb3dlckJveCAuaW5mbyB7ZGlzcGxheTogbm9uZTt9DQouc3RhdHVzX2ljb24ge2Rpc3BsYXk6IG5vbmV9DQouaW1wb3dlckJveCAuc3RhdHVzIHt0ZXh0LWFsaWduOiBjZW50ZXI7fSANCg==`,
      }
    },
    computed: {
      ...mapGetters({
        title: 'settings/title',
      }),
    },
    mounted() {
      this.setBackgroundImg()
    },
    watch: {
      activeName(value) {
        if (value === 'qrcode') {
          this.$nextTick(() => this.initWxLogin())
        }
      },
      $route: {
        async handler(route) {
          this.redirect = (route.query && route.query.redirect) || '/'
          let param = this.getOtherQuery(this.$route.query)
          this.handTokenLogin(param)
          //监听路由变化切换背景图
          if (route.query.code) {
            let res = await codeLogin(this.$route.query.code)
            if (res.data.token) {
              await this.tokenLogin(res.data.token)
              await this.$router.replace(this.handleRoute())
            }
          }
          if (route.query.companyId) {
            const id = route.query ? route.query.companyId : ''
            this.loadCompanyAssets(id, true)
          }
        },
        immediate: true,
      },
    },
    // mounted() {
    //   this.form.userName = ''
    //   this.form.password = ''
    //   // 为了演示效果，会在官网演示页自动登录到首页，正式开发可删除
    //   if (
    //     document.domain === 'vue-admin-beautiful.com' ||
    //     document.domain === 'chu1204505056.gitee.io'
    //   ) {
    //     this.previewText = '（演示地址验证码可不填）'
    //     this.timer = setTimeout(() => {
    //       this.handleLogin()
    //     }, 5000)
    //   }
    // },
    methods: {
      ...mapActions({
        login: 'user/login',
        tokenLogin: 'user/tokenLogin',
      }),
      translateTitle,
      handlePassword() {
        this.passwordType === 'password'
          ? (this.passwordType = '')
          : (this.passwordType = 'password')
        this.$nextTick(() => {
          this.$refs.password.focus()
        })
      },
      handleRoute() {
        const model = localStorage.getItem('model')
        if (!model || model === 'home') return '/'
        return this.redirect === '/404' || this.redirect === '/403'
          ? '/'
          : this.redirect
      },
      async handTokenLogin(param) {
        // if (param.token) {
        //   await this.tokenLogin(this.form)
        //   location.href = '/'
        // }
      },
      handleLogin() {
        this.$refs.form.validate(async (valid) => {
          if (valid)
            try {
              this.loading = true
              await this.login(this.form)
              localStorage.setItem('isFirstLogin', 1)
              await this.$router.replace(this.handleRoute())
            } finally {
              this.loading = false
            }
        })
      },
      changeCode() {
        this.codeUrl =
          baseURL +
          `/setting/login/getVerificationCode?timestamp=${new Date().getTime()}`
      },
      getOtherQuery(query) {
        return Object.keys(query).reduce((acc, cur) => {
          if (cur !== 'redirect') {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
      },
      async setBackgroundImg() {
        const info = localStorage.getItem('UsingBackgroundPic')
        if (info && info != 'undefined') {
          this.applyBackground(info)
        }
        const id = localStorage.getItem('companyId')
        if (id) {
          // 背景/logo按需刷新，不阻塞账号密码登录首屏。
          this.loadCompanyAssets(id, !info)
        }
      },
      applyBackground(url) {
        const loginContainer = document.getElementById('login-container')
        if (!loginContainer || !url || url === 'undefined') return
        loginContainer.style.background =
          'url("' + url + '") center center fixed no-repeat'
        loginContainer.style.backgroundSize = 'cover'
      },
      async loadCompanyAssets(id, applyBackground = false) {
        if (!id) return
        const cacheKey = 'LoginCompanyAssetId'
        const hasCachedAssets =
          localStorage.getItem(cacheKey) === String(id) &&
          localStorage.getItem('UsingBackgroundPic')
        if (hasCachedAssets && !applyBackground) return
        const res = await getCompanyImg({ id })
        if (res.code === 200) {
          const info = res.data ? res.data.homePicture : defaultImg
          localStorage.setItem(
            'UsingBackgroundPic',
            res.data ? res.data.homePicture : ''
          )
          localStorage.setItem(
            'UsingHomeLogoPic',
            res.data ? res.data.leftUpperPicture : ''
          )
          localStorage.setItem(
            'UsingModuleLogoPic',
            res.data ? res.data.logoPicture : ''
          )
          localStorage.setItem(cacheKey, String(id))
          if (applyBackground) this.applyBackground(info)
        }
      },
      initWxLogin() {
        if (this.wxLoginLoaded || typeof WxLogin === 'undefined') return
        this.wxLoginLoaded = true
        let url = 'https://www.wenxin.example.com/#/login'
        new WxLogin({
          id: 'wxLogin',
          appid: 'wxfd02d92a524bef99',
          scope: 'snsapi_login',
          // 扫码成功后 跳转的地址
          redirect_uri: encodeURIComponent(url), // 授权成功后回调的url
          state: Math.ceil(Math.random() * 1000), // 可设置为简单的随机数加session用来校验
          style: 'black',
          // href: this.QRCodeStyle
        })
      },
    },
  }
</script>

<style lang="scss" scoped>
  #login-container {
    height: 100vh;
    background: url('~@/assets/login_images/background-old.jpg') center center
      fixed no-repeat;
    background-size: cover;
  }

  .login-form {
    position: relative;
    max-width: 100%;
    padding: 4.5vh;
    margin: calc((100vh - 475px) / 2) 5vw 5vw;
    overflow: hidden;
    background: url('~@/assets/login_images/login_form.png');
    background-size: 100% 100%;

    .title {
      font-size: 54px;
      font-weight: 500;
      color: $base-color-white;
    }

    .title-tips {
      margin-top: 29px;
      font-size: 26px;
      font-weight: 400;
      color: $base-color-white;
    }

    .login-btn {
      display: inherit;
      width: 220px;
      height: 50px;
      margin-top: 5px;
      border: 0;

      &:hover {
        opacity: 0.9;
      }

      .forget-passwordword {
        width: 100%;
        margin-top: 40px;
        text-align: left;

        .forget-password {
          width: 129px;
          height: 19px;
          font-size: 20px;
          font-weight: 400;
          color: rgba(92, 102, 240, 1);
        }
      }
    }

    .tips {
      margin-bottom: 10px;
      font-size: $base-font-size-default;
      color: $base-color-white;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .title-container {
      position: relative;

      .title {
        margin: 0 auto 40px auto;
        font-size: 34px;
        font-weight: bold;
        color: $base-color-blue;
        text-align: center;
      }
    }

    i {
      position: absolute;
      top: 8px;
      left: 5px;
      z-index: $base-z-index;
      font-size: 16px;
      color: #d7dee3;
      cursor: pointer;
      user-select: none;
    }

    .show-password {
      position: absolute;
      right: 25px;
      left: -35px;
      font-size: 16px;
      color: #d7dee3;
      cursor: pointer;
      user-select: none;
    }

    ::v-deep {
      .el-form-item {
        padding-right: 0;
        margin: 20px 0;
        color: #454545;
        background: transparent;
        border: 1px solid transparent;
        border-radius: 2px;

        &__content {
          min-height: $base-input-height;
          line-height: $base-input-height;
        }

        &__error {
          position: absolute;
          top: 100%;
          left: 18px;
          font-size: $base-font-size-small;
          line-height: 18px;
          color: $base-color-red;
        }
      }

      .el-input {
        box-sizing: border-box;

        input {
          height: 48px;
          padding-left: 35px;
          font-size: $base-font-size-default;
          line-height: 58px;
          background: #f6f4fc;
          border: 0;
        }
      }

      .code {
        position: absolute;
        top: 4px;
        right: 4px;
        cursor: pointer;
        border-radius: $base-border-radius;
      }
    }
  }
  .login-tabs {
    margin-top: 20px;
  }
  .tab-qrcode {
    padding: 10px 30px;
  }
</style>
<style>
  .login-tabs .el-tabs__item {
    color: #fefefe;
    width: 120px;
    text-align: center;
    padding: 0;
  }
  .login-tabs .el-tabs__item:hover {
    color: #1890ff !important;
  }
  .login-tabs .el-tabs__item.is-active {
    color: #1890ff !important;
    background-color: #fefefe;
  }
</style>
