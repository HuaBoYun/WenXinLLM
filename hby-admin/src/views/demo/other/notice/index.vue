<template>
  <div class="notice-container">
    <el-row :gutter="20">
      <el-col :lg="8" :md="12" :sm="24" :xl="8" :xs="24">
        <el-card shadow="hover">
          <template #header>
            <span>Alert 警告</span>
          </template>
          <el-alert show-icon title="成功提示的文案" type="success" />
          <el-alert show-icon title="消息提示的文案" type="info" />
          <el-alert show-icon title="警告提示的文案" type="warning" />
          <el-alert show-icon title="错误提示的文案" type="error" />
        </el-card>
      </el-col>
      <el-col :lg="8" :md="12" :sm="24" :xl="8" :xs="24">
        <el-card shadow="hover">
          <template #header>
            <span>element-plus风格 Message 消息提示</span>
          </template>
          <el-button plain type="primary" @click="open1">消息</el-button>
          <el-button plain type="success" @click="open2">成功</el-button>
          <el-button plain type="warning" @click="open3">警告</el-button>
          <el-button plain type="danger" @click="open4">错误</el-button>
        </el-card>
        <el-card shadow="hover">
          <template #header>
            <span>hey-ui风格 消息提示</span>
          </template>
          <el-button plain type="primary" @click="open5">消息</el-button>
          <el-button plain type="success" @click="open6">成功</el-button>
          <el-button plain type="warning" @click="open7">警告</el-button>
          <el-button plain type="danger" @click="open8">错误</el-button>
        </el-card>
      </el-col>
      <el-col :lg="8" :md="12" :sm="24" :xl="8" :xs="24">
        <el-card shadow="hover">
          <template #header>
            <span>Notification Message 消息提示</span>
          </template>
          <el-button plain type="info" @click="open9">消息</el-button>
          <el-button plain type="primary" @click="open10">成功</el-button>
          <el-button plain type="warning" @click="open11">警告</el-button>
          <el-button plain type="danger" @click="open12">错误</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'Notice',
    data() {
      return {}
    },
    methods: {
      open1() {
        this.$baseMessage('这是一条消息提示', 'info')
      },
      open2() {
        this.$baseMessage('恭喜你，这是一条成功消息', 'success')
      },
      open3() {
        this.$baseMessage('警告哦，这是一条警告消息', 'warning')
      },
      open4() {
        this.$baseMessage('错了哦，这是一条错误消息', 'error')
      },
      open5() {
        this.$baseMessage('这是一条消息提示', 'info', 'vab-hey-message-info')
      },
      open6() {
        this.$baseMessage(
          '恭喜你，这是一条成功消息',
          'success',
          'vab-hey-message-success'
        )
      },
      open7() {
        this.$baseMessage(
          '警告哦，这是一条警告消息',
          'warning',
          'vab-hey-message-warning'
        )
      },
      open8() {
        this.$baseMessage(
          '错了哦，这是一条错误消息',
          'error',
          'vab-hey-message-error'
        )
      },
      open9() {
        this.$baseNotify(
          '这是一条消息的提示消息',
          '提示',
          'info',
          'bottom-right'
        )
      },
      open10() {
        this.$baseNotify(
          '这是一条成功的提示消息',
          '成功',
          'success',
          'bottom-right'
        )
      },
      open11() {
        this.$baseNotify(
          '这是一条警告的提示消息',
          '警告',
          'warning',
          'bottom-right'
        )
      },
      open12() {
        this.$baseNotify(
          '这是一条错误的提示消息',
          '错误',
          'error',
          'bottom-right'
        )
      },
    },
  }
</script>

<style lang="scss" scoped>
  $base: '.notice';
  #{$base}-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
