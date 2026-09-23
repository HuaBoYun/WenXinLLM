<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="85%"
    @close="close"
    append-to-body
    v-if="dialogFormVisible"
  >
    <h2 class="text-center">{{ formData.weeklytitle }}</h2>
    <div class="indented">{{ formData.memo }}</div>
    <div
      v-for="(item, index) in tableData"
      :key="index"
      style="margin-bottom: 10px; height: 600px; overflow: hidden"
    >
      <iframe
        v-if="item.url"
        ref="iframe"
        width="100%"
        height="600px"
        allowfullscreen="true"
        webkitallowfullscreen="true"
        mozallowfullscreen="true"
        :src="
          item.type.includes('.pdf')
            ? item.url + '&officePreviewType=pdf'
            : item.url
        "
      ></iframe>
    </div>
    <div class="color-red">
      如果周刊内容长时间无法加载，请点击下方“预览”按钮进行查看
    </div>
    <el-divider>附件</el-divider>
    <el-table :data="filesList">
      <el-table-column align="center" label="附件名称" prop="attname" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDowns(row)">下载</el-button>
          <el-button
            type="text"
            :disabled="false"
            @click="handlePreviewFile(row, 2)"
          >
            预览
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
  import {
    getComplianceWeenlyDetail,
    getPrivewAttInfo,
    download,
  } from '@/api/fwgl/hgzk'
  import store from '@/store'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '查看详情',
        formData: {},
        tableData: [],
        filesList: [],
        headers: { token: token },
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
      }
    },
    mounted() {},
    methods: {
      show(id) {
        this.dialogFormVisible = true
        this.getDetail(id)
      },
      async getDetail(id) {
        const { data, code, msg } = await getComplianceWeenlyDetail({
          id: id,
        })
        if (code != 1) return
        this.formData = data.tblComplianceWeekly
        this.filesList = data.files
        const promises = data.files.map(async (v) => {
          return await this.handlePreview(v)
        })
        const videoList = await Promise.all(promises)
        this.tableData = videoList
      },
      // 预览
      // async handlePreview(row, type) {
      //   if (!type) {
      //     try {
      //       const { data } = await getPrivewAttInfo({
      //         attId: row.attid,
      //         attType: 2,
      //       })
      //       return {
      //         url:
      //           data.previewurl +
      //           '?url=' +
      //           encodeURIComponent(Base64.encode(data.ftpUrl)),
      //         type: data.ftpUrl,
      //       }
      //     } catch (error) {
      //       return '' // 或者返回其他默认值
      //     }
      //   } else {
      //     const { data } = await getPrivewAttInfo({
      //       attId: row.attid,
      //       attType: 4,
      //     })
      //     let url = data.ftpUrl.includes('.pdf')
      //       ? data.previewurl +
      //         '?url=' +
      //         encodeURIComponent(Base64.encode(data.ftpUrl)) +
      //         '&officePreviewType=pdf'
      //       : data.previewurl +
      //         '?url=' +
      //         encodeURIComponent(Base64.encode(data.ftpUrl))
      //     window.open(url)
      //   }
      // },
      // 下载
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      close() {
        this.dialogFormVisible = false
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
    },
  }
</script>
<style lang="scss" scoped>
  .text-center {
    text-align: center;
  }
  .indented {
    font-size: 16px;
    text-indent: 20px;
    margin-bottom: 20px;
  }
  video {
    width: 100%; /* Make the video player take the full width of its container */
    height: auto; /* Maintain aspect ratio */
  }
  .color-red {
    color: red;
    font-size: 12px;
    margin-bottom: 5px;
  }
  .plyr video {
    height: 600px;
  }
</style>
