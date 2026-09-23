<template>
  <div class="container">
    <div class="left-content">
      <AIChat :msgData="msgData" />
    </div>
    <div class="right-content">
      <iframe
        :src="iframeUrl"
        frameborder="0"
        style="width: 100%; height: 100%"
      ></iframe>
    </div>
  </div>
</template>

<script>
  import AIChat from './AIChat.vue'
  import img1 from './img/canvas-image-1.png'
  import img2 from './img/canvas-image-2.png'
  import img3 from './img/canvas-image-3.png'
  import img4 from './img/canvas-image-4.png'
  import img5 from './img/canvas-image-5.png'
  import img6 from './img/canvas-image-6.png'
  import img7 from './img/canvas-image-7.png'
  import img8 from './img/canvas-image-8.png'
  import img9 from './img/canvas-image-9.png'
  import img10 from './img/canvas-image-10.png'
  import { getPrivewAttInfo } from '@/api/contract/manage'

  export default {
    components: { AIChat },
    props: [ 'msgData' ],
    data() {
      return {
        img1,
        img2,
        img3,
        img4,
        img5,
        img6,
        img7,
        img8,
        img9,
        img10,
        iframeUrl: 'http://192.0.2.17:8012/onlinePreview?url=ZnRwOi8vMTIzLjU2LjIzNi4yNToyMS91cGxvYWRmaWxlLzE4OTQ0MDA2MzYzMDU5NTI3NjgucGRm&officePreviewType=pdf',
      }
    },
    mounted() {
      this.getFileUrlFirst('648619692200005')
    },
    methods: {
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
       async getFileUrlFirst(attid) {
        // const { data } = await getPrivewAttInfo({
        //   attId: attid,
        //   attType: 2,
        // })
        // if (data.zxtoken) {
        //   const url = data.previewurl + '?url=' + data.zxtoken
        //   this.show({ iframeUrl: url })
        // } else {
        //   const url =
        //     data.previewurl +
        //     '?url=' +
        //     encodeURIComponent(Base64.encode(data.ftpUrl))
        //   this.show({ iframeUrl: url })
        // }
      },
    },
  }
</script>

<style scoped>
.container {
  display: flex;
  /* height: calc(100% -50px); */
}

.left-content {
  position: relative;
  padding: 0 20px;
  width: 36%;
}

.left-content >>> .search-input {
  width: 100%;
  position: absolute;
}

.right-content {
  flex: 1;
  background: #000;
}
</style>