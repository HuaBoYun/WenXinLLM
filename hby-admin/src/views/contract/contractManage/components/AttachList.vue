<!--
 * @Date: 2022-04-19 11:31:15
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-26 10:51:01
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/AttachList.vue
-->
<template>
  <div>
    <div v-if="!readonly" style="text-align: right; margin-bottom: 5px">
      <file-upload
        v-model="formData.url"
        accept="*"
        api="/contract/uploadFileAttInfo"
        :show-file-list="false"
        @success="handleSuccess"
        v-if="!$store.state.work.processMobile"
      >
        <el-button type="success">上传</el-button>
      </file-upload>
    </div>
    <el-table :data="list">
      <el-table-column
        align="center"
        label="序号"
        type="index"
        width="50"
      ></el-table-column>
      <el-table-column align="center" label="附件名称" prop="attname" />
      <!-- <el-table-column align="center" label="文件大小(KB)" prop="attsize" /> -->
      <el-table-column align="center" label="创建人" prop="uploader" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="220"
      >
        <template #default="{ row, $index }">
          <el-link
            v-show="attType == 1"
            :href="`${baseApi}/contract/downloadFtp/upload?singingId=${row.attid}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
          >
            下载
          </el-link>
          <el-link
            v-show="attType == 2"
            :href="`${baseApi}/contract/download?id=${row.attid}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
          >
            下载
          </el-link>
          <!-- <el-button type="text" @click="handleDown(row)">下载</el-button> -->
          <el-button type="text" @click="handlePreview(row)">预览</el-button>
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
        tableList: [],
      }
    },
    computed: {
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
      // async handleDown(row) {
      //   const data = await download({
      //     singingId: row.attid,
      //   })
      // },
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
      handleDelete(row, index) {
        this.$emit('delete-att', row, index)
      },
      handleSuccess(val) {
        this.$emit('upload-success', val)
      },
    },
  }
</script>
