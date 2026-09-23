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
  export default {
    name: '',
    props: {},
    data() {
      return {
        tableData: [],
        visible: false,
        title: '附件列表',
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
        const data = await fildDownload({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
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

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      closeCurrent() {
        this.visible = false
        this.tableData = []
      },
    },
  }
</script>

<style></style>
