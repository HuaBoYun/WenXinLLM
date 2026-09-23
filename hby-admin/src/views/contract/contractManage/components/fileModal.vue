<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      append-to-body
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row>
        <el-col :span="24" v-if="!isDisabled">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
            :file-list="pdfList"
            style="text-align: right"
            :data="{ contractId: info.contractid, budgetid: info.budgetid }"
          >
            <el-button type="success">上传</el-button>
          </el-upload>
        </el-col>
        <el-col :span="24">
          <el-table :data="pdfList">
            <el-table-column
              align="center"
              label="附件名称"
              prop="singingName"
            />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="singingSize"
            />
            <el-table-column align="center" label="创建人" prop="realname">
              <template #default="{ row }">
                <div>
                  {{ row.realname || row.uploaderName }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="150"
            >
              <template #default="{ row, $index }">
                <el-link
                  :href="`${baseApi}/contract/downloadFtp/upload?singingId=${row.singingId}`"
                  style="font-size: 12px; margin: 0 10px"
                  type="primary"
                >
                  下载
                </el-link>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf(row)"
                  size="mini"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!isDisabled"
                  type="text"
                  @click="handleDelete(row, $index)"
                  size="mini"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
      <div style="text-align: right; margin-top: 20px">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="handlePush">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getPrivewAttInfo,
    fileSure,
    getContractSealDetail,
  } from '@/api/contract/manage'
  import { download } from '@/api/audit/implement'
  import { removeFileFromContract } from '@/api/contract/fulfil'
  const { baseURL } = require('@/config')
  import store from '@/store'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '签署文件',
        baseApi: baseURL,
        api: '/contract/contract/importFile',
        headers: {
          token: store.getters['user/token'],
        },
        pdfList: [],
        info: {},
        isDisabled: false,
      }
    },
    methods: {
      async openModal(row) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getContractSealDetail({
            budgetId: row.budgetid,
            flowId: row.flowid,
          })
          this.pdfList = res.data.signingList
          this.info = row
          // 如果状态是7完成，则不能上传
          if (row.contractstatus >= 7) {
            this.isDisabled = true
          }
        }
      },
      handleSuccess(file) {
        if (file.code == '1') {
          this.pdfList.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      async handlePreviewFilePDf(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.singingId,
          attType: 1,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      //下载附件
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.singingName
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
      handleDelete(row, index) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeFileFromContract({
            singingId: row.singingId,
          })
          this.pdfList.splice(index, 1)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        })
      },
      close() {
        this.dialogFormVisible = false
        this.pdfList = []
        this.isDisabled = false
        this.$emit('fetchData')
      },
      //上传校验
      handlePush() {
        this.$baseConfirm('确认完成后无法再次上传删除文件', null, async () => {
          const res = await fileSure({ contractId: this.info.contractid })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.close()
          }
        })
      },
    },
  }
</script>
