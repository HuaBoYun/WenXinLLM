<template>
  <div class="errorLog-container">
    <el-button type="primary" @click="handleError">
      点击模拟一个chuzhixinjiayou的错误
    </el-button>
    <error-test v-if="show" />
  </div>
</template>

<script>
  import ErrorTest from './components/ErrorTest'

  export default {
    name: 'ErrorLog',
    components: { ErrorTest },
    data() {
      return { show: false }
    },
    methods: {
      handleError() {
        if (process.env.NODE_ENV === 'production') {
          this.$baseMessage(
            '\u4e3a\u4e86\u9632\u6b62\u5f15\u8d77\u6b67\u4e49\uff0c\u6f14\u793a\u73af\u5883\u4e0d\u5141\u8bb8\u6a21\u62df\u9519\u8bef\uff0c\u8bf7\u4e0b\u8f7d\u6e90\u7801\u540e\u4f53\u9a8c\u6b64\u529f\u80fd\u3002',
            'error',
            'vab-hey-message-error'
          )
        }
        this.show = true
      },
    },
  }
</script>
