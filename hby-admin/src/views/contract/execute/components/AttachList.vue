<!--
 * @Date: 2022-04-24 17:00:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 17:00:13
 * @FilePath: /hb-admin/src/views/contract/execute/components/AttachList.vue
-->
<template>
  <div>
    <div v-if="!readonly" style="text-align: right; margin-bottom: 5px">
      <file-upload
        v-model="formData.url"
        accept="*"
        api="/contract/uploadFileAttInfo"
        :data="{ type: 6, bid: bid }"
        :show-file-list="false"
        @success="handleSuccess"
      >
        <el-button type="success">上传</el-button>
      </file-upload>
    </div>
    <el-table :data="list">
      <el-table-column align="center" label="附件名称" prop="attname" />
      <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
      <el-table-column align="center" label="创建人" prop="uploader" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="150"
      >
        <template #default="{ row, $index }">
          <el-link
            :href="`${baseApi}/contract/download?id=${row.attid}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
          >
            下载
          </el-link>
          <el-link type="primary" style="font-size: 12px; margin-right: 10px" @click="handlePreview(row)">预览</el-link>
          <el-button
            v-if="!readonly"
            type="text"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
  import FileUpload from '@/views/contract/contractManage/components/FileUpload.vue'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const Base64 = require('js-base64').Base64

  export default {
    name: 'AttachList',
    components: { FileUpload },
    props: {
      nodeId: {
        type: Number,
        default: 0,
      },
      attList: {
        type: Array,
        default: () => [],
      },
      localList: {
        type: Array,
        default: () => [],
      },
      readonly: {
        type: Boolean,
        default: false,
      },
      attType: {
        type: Number,
        default: 2,
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
    computed: {
      bid() {
        return this.nodeId
      },
      list() {
        const arr1 = this.attList.map((item) => {
          return {
            ...item,
            type: 'att',
          }
        })
        const arr2 = this.localList.map((item) => {
          return {
            ...item,
            type: 'local',
          }
        })
        return arr1.concat(arr2)
      },
    },

    methods: {
      //预览
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: this.attType,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      //回调
      handleDelete(row, index) {
        this.$emit('delete-att', row, index)
      },
      //回调
      handleSuccess(val) {
        this.$emit('upload-success', val)
      },
    },
  }
</script>
