<template>
  <el-dialog
    v-if="dialogFormVisible"
    v-show="editorShow"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    @close="close"
    :modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    fullscreen
  >
    <div class="content">
      <iframe
        id="onlineIframe"
        frameborder="0"
        style="width: 100%; height: 100%"
        :src="url"
      ></iframe>

    </div>
    <template #title>
      <div style="display: flex;justify-content: space-between;align-items: center;">
        <h3>{{ title }}</h3>
        <div>
          <!-- <el-button @click="hide">隐藏</el-button> -->
          <el-button type="danger" @click="hide">关闭</el-button>
        </div>
      </div>
    </template>
    
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        editorShow: false,
        title: '在线文档',
        url: null,
      }
    },
    methods: {
      show(url) {
        // if (!data.file_url1 || !data.file_url2) return undefined

        this.dialogFormVisible = true
        this.editorShow = true
        this.url = url
        setTimeout(() => {
          const iframeWindow = window.frames["onlineIframe"];
          console.log('this.$refs.onlineIframe', iframeWindow.contentWindow.config)
        }, 2000)
      },
      close() {
        this.editorShow = false
        this.dialogFormVisible = false
      },
      hide() {
        this.editorShow = false
      }
    },
  }
</script>

<style scoped lang="scss">
  .content {
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 85vh;
    max-height: 85vh;
  }
</style>
