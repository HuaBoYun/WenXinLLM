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
    <h2 class="text-center">{{ formData.rulename }}</h2>
    <div class="indented">{{ formData.rulenumber }}</div>
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
      如果内容长时间无法加载，请点击下方“预览”按钮进行查看
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
          <el-button
            type="text"
            :disabled="false"
            @click="handlePreview(row, 1)"
          >
            预览
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
  import { getPrivewAttInfo, download } from '@/api/fwgl/hgzk'
  import { selectInnerRuleInfo } from '@/api/workbench/auditTools'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '查看详情',
        formData: {},
        tableData: [],
        filesList: [],
      }
    },
    mounted() {},
    methods: {
      show(id) {
        this.dialogFormVisible = true
        this.getDetail(id)
      },
      async getDetail(id) {
        const { data, code, msg } = await selectInnerRuleInfo(id)
        if (code != 1) return
        this.formData = data.tblNbsjInnerrule
        this.filesList = data.attList
        const promises = data.attList.map(async (v) => {
          return await this.handlePreview(v)
        })
        const videoList = await Promise.all(promises)
        this.tableData = videoList
      },
      // 预览
      async handlePreview(row, type) {
        if (!type) {
          try {
            const { data } = await getPrivewAttInfo({
              attId: row.attid,
              attType: 0,
            })
            return {
              url:
                data.previewurl +
                '?url=' +
                encodeURIComponent(Base64.encode(data.ftpUrl)),
              type: data.ftpUrl,
            }
          } catch (error) {
            return '' // 或者返回其他默认值
          }
        } else {
          const { data } = await getPrivewAttInfo({
            attId: row.attid,
            attType: 0,
          })
          let url = data.ftpUrl.includes('.pdf')
            ? data.previewurl +
              '?url=' +
              encodeURIComponent(Base64.encode(data.ftpUrl)) +
              '&officePreviewType=pdf'
            : data.previewurl +
              '?url=' +
              encodeURIComponent(Base64.encode(data.ftpUrl))
          window.open(url)
        }
      },
      close() {
        this.dialogFormVisible = false
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
    text-align: center;
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
