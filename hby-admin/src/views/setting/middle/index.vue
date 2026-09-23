<template>
  <div>
    <iframe
      :src="openUrl"
      width="100%"
      frameborder="0"
      scrolling="no"
      style="min-height: 80vh; vertical-align: top"
    ></iframe>
  </div>
</template>

<script>
import { ssfx } from '@/api/setting/themeRepertory'
export default {
  data() {
    return {
      openUrl: '',
    }
  },
  beforeCreate() {
    ssfx().then((res) => {
      //获取路由的最后的参数
      const info = this.$route.path.split('/')[2]
      //分局最后的参数获取到跳转的地址
      const url = localStorage.getItem(info)
      const openUrl =
        `${res.data.loginUrl}?loginName=${res.data.name}&sign=${res.data.sign}&timestamp=${res.data.time}&url=` +
        this.urlencode(url)
      this.openUrl = openUrl
      console.log(openUrl, 'openUrl')
      // window.open(openUrl)
    })
  },
  methods: {
    urlencode(str) {
      str = (str + '').toString()
      return encodeURIComponent(str)
        .replace(/!/g, '%21')
        .replace(/'/g, '%27')
        .replace(/\(/g, '%28')
        .replace(/\)/g, '%29')
        .replace(/\*/g, '%2A')
        .replace(/%20/g, '+')
    },
  },
}
</script>

<style></style>
