<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="70%"
    @close="close"
    :close-on-click-modal="false"
  >
    <iframe
      v-if="iframeUrl"
      :src="iframeUrl"
      frameborder="0"
      style="width: 100%; height: 700px"
    ></iframe>
  </el-dialog>
</template>

<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'

  export default {
    name: 'previewIFrameModal',
    data() {
      return {
        title: '文件预览',
        iframeUrl: undefined,
        dialogFormVisible: false,
      }
    },
    methods: {
      show(opt) {
        if (!opt.iframeUrl) {
          return this.getFileUrlFirst(opt)
        }

        const clientWidth = document.body.clientWidth
        if (clientWidth > 640) {
          window.open(
            opt.iframeUrl,
            '文件预览',
            'height=800,width=1000,top=240,left=360,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no'
          )
          return
        }
        // 移动端直接打开
        window.open(opt.iframeUrl)
        return
        this.dialogFormVisible = true
        this.title = opt.title || '文件预览'
        this.iframeUrl = opt.iframeUrl
      },
      close() {
        this.dialogFormVisible = false
        this.iframeUrl = undefined
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async getFileUrlFirst(file) {
        console.log('file', file)
        const { data } = await getPrivewAttInfo({
          attId: file.attid,
          attType: 2,
        })
        if (data.zxtoken) {
          const url = data.previewurl + '?url=' + data.zxtoken
          this.show({ iframeUrl: url })
        } else {
          const url =
            data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
          this.show({ iframeUrl: url })
        }
      },
    },
  }
</script>
