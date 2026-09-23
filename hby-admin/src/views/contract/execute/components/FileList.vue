<!--
 * @Date: 2022-03-31 15:03:53
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-18 15:25:43
 * @FilePath: /hb-admin/src/views/contract/execute/components/FileList.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="附件名称" prop="singingName" />
      <el-table-column align="center" label="文件大小(KB)" prop="singingSize" />
      <el-table-column align="center" label="创建人" prop="realname" />
      <el-table-column align="center" label="创建时间" prop="uploadTime" />
      <el-table-column align="center" label="操作" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="handlePreview(row)">预览</el-button>
          <el-link
            :href="`${baseApi}/contract/downloadFtp/upload?singingId=${row.singingId}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
          >
            下载
          </el-link>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { getFileList, removeFileFromContract } from '@/api/contract/fulfil'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const Base64 = require('js-base64').Base64
  const { baseURL } = require('@/config')
  export default {
    name: 'FileList',
    data() {
      return {
        baseApi: baseURL,
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          contractId: undefined,
          singingName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        dialogVisible: false,
        title: '',
      }
    },
    created() {},
    methods: {
      show(row) {
        this.dialogVisible = true
        this.queryForm.contractId = row.contractid
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getFileList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      // handlePreview(row) {
      //   window.open(`/filePreviewing?singingId=${row.singingId}`)
      // },
      //预览
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.singingId,
          attType: 1,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      //删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeFileFromContract({
            singingId: row.singingId,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
