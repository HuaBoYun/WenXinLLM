<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :append-to-body="true"
    width="40%"
    @close="closeCurrent"
    :close-on-click-modal="false"
  >
    <el-table :data="tableData">
      <el-table-column align="center" label="附件名称" prop="attname" />
      <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="150"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDown(row)">下载</el-button>
          <el-button type="text" @click="handlePreviewOpen(row)">
            预览
          </el-button>
          <!-- <el-button
                v-if="!readonly"
                type="text"
                @click="handleDelete(row, $index)"
              >
                删除
              </el-button> -->
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import {
    fildDownload,
    fileList,
    getPrivewAttInfo,
  } from '@/api/contract/manage.js'
  import store from '@/store'
  import { getLCfile } from '@/api/setting/system'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'AttListModel',
    props: {},
    data() {
      return {
        tableData: [],
        visible: false,
        title: '附件列表',
        headers: { token: token },
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
      }
    },
    methods: {
      async getFileList(flowTaskOperatorId, flowTaskId) {
        const { data, code } = await fileList({
          flowTaskOperatorId,
          flowTaskId,
        })
        if (code == 1) {
          this.tableData = data
        }
      },
      async show(row) {
        this.visible = true
        //
        this.getFileList(row.taskOperatorId, row.taskId)
      },
      async handleDown(row) {
        const data = await getLCfile({ fileId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {})
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handlePreviewOpen(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 3,
        })
        if (data.zxtoken) {
          const url = data.previewurl + '?url=' + data.zxtoken
          this.$iFrameDialog({ iframeUrl: url })
        } else {
          const url =
            data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
          this.$iFrameDialog({ iframeUrl: url })
        }
      },
      // //下载公共方法调用
      // async handleDowns(row) {
      //   try {
      //     // 调用 handleDown 并传递自定义的下载接口
      //     await handleDown(row, this.headers, this.lodeapi)
      //   } catch (error) {
      //     console.error('自定义下载失败:', error)
      //   }
      // },
      // handlePreviewFile(row) {
      //   if (row.isEncrypted === '1') {
      //     // 当文件是加密状态时，使用指定的在线预览链接
      //     const previewUrl = row.previewUrl
      //     window.open(previewUrl, '_blank')
      //   } else {
      //     this.$iFrameDialog({ attid: row.attid })
      //   }
      // },
      closeCurrent() {
        this.visible = false
        this.tableData = []
      },
    },
  }
</script>

<style></style>
