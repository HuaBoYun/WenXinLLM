<!--
 * @Date: 2022-04-19 16:06:49
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-19 17:24:16
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/SigningList.vue
-->
<template>
  <div>
    <el-table :data="list">
      <el-table-column align="center" label="附件名称" prop="singingName" />
      <el-table-column align="center" label="文件大小(KB)" prop="singingSize" />
      <el-table-column align="center" label="创建人" prop="uploaderName" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-link
            :href="`${baseApi}/contract/downloadFtp/upload?singingId=${row.singingId}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
            v-if="!$store.state.work.processMobile && !row.oaattid"
          >
            下载
          </el-link>
          <el-button v-if="row.oaattid" type="text" @click="handleDown(row)">
            下载
          </el-button>
          <el-button
            v-if="!row.oaattid"
            type="text"
            @click="handlePreview(row)"
          >
            预览
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
  import { getPrivewAttInfo, downloadOldFile } from '@/api/contract/manage'
  const { baseURL } = require('@/config')
  const Base64 = require('js-base64').Base64
  export default {
    name: 'NodeList',
    props: {
      list: {
        type: Array,
        default: () => [],
      },
      attType: {
        type: Number,
        default: 1,
      },
    },
    data() {
      return {
        baseApi: baseURL,
        formData: {
          url: undefined,
        },
      }
    },
    methods: {
      //预览
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.singingId,
          attType: this.attType,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      //下载
      async handleDown(row) {
        const res = await downloadOldFile({ singingId: row.singingId })
        window.open(res.url)
      },
    },
  }
</script>
