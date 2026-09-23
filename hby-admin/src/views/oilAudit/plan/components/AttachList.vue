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
        api="/fileManage/upload"
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
        width="120"
      >
        <template #default="{ row, $index }">
          <el-link
            :href="`${baseApi}/contract/download?id=${row.attid}`"
            style="font-size: 12px; margin: 0 10px"
            type="primary"
          >
            下载
          </el-link>
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
      handleDelete(row, index) {
        this.$emit('delete-att', row, index)
      },
      handleSuccess(val) {
        this.$emit('upload-success', val)
      },
    },
  }
</script>
