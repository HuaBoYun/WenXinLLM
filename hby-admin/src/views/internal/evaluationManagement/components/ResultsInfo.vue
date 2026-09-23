<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form">
      <el-col :span="12">
        <el-form-item label="要素名称" prop="ASSELEID">
          <el-input
            readonlyclass="inputDeep"
            v-model="form.ELEMENTNAME"
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="标准分">
          <el-input
            readonlyclass="inputDeep"
            v-model="form.STANDARDSCORE"
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审查要点">
          <el-input
            disablereadonly
            class="inputDeep"
            v-model="form.AUDITPOINT"
            type="textarea"
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价人">
          <el-input
            readonlyclass="inputDeep"
            v-model="form.REALNAME"
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价分">
          <el-input readonlyclass="inputDeep" v-model="form.SCORE"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="评价依据">
          <el-input readonlyclass="inputDeep" v-model="form.REASON"></el-input>
        </el-form-item>
      </el-col>
    </el-form>
    <el-table :data="tableData">
      <el-table-column align="center" label="附件名称" prop="attname" />
      <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
      <el-table-column align="center" label="创建人" prop="uploader" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDowns(row)">下载</el-button>
          <el-button type="text" @click="handlePreviewFile(row)">
            预览
          </el-button>
          <!-- <el-button type="text" @click="downloadFile(row)">下载</el-button> -->
          <!-- <el-button type="text" @click="handleEdit2(row)">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { YSInfo } from '@/api/internal/result'
  import { download } from '@/api/internal/score'
  import { handleDown } from '@/utils/fileHandler'
  import store from '@/store'

  export default {
    name: 'ResultsInfo',
    data() {
      return {
        title: '评价结果-详情页',
        dialogFormVisible: false,
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        form: {
          ASSELEID: undefined,
          ATTID: undefined,
          AUDITPOINT: undefined,
          ELEMENTNAME: undefined,
          REALNAME: undefined,
          REASON: undefined,
          SCORE: undefined,
          STANDARDSCORE: undefined,
        },
        queryForm: {},
        tableData: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm = row
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        const info = {
          asscatid: '',
          assmarkid: '',
        }
        info.asscatid = this.queryForm.ASSCATID
        info.assmarkid = this.queryForm.ASSMARKID
        const {
          code,
          data: { listAtt, obj },
        } = await YSInfo(info)
        this.form = obj[0]
        this.tableData = listAtt
        console.log(this.tableData)
      },
      async downloadFile(row) {
        // console.log(row)
        const file = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([file]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
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
<style scoped lang="scss">
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
  // .inputDeep {
  //   :deep(.el-input__wrapper) {
  //     border: none;
  //     box-shadow: 0 0 0 0px var(--el-input-border-color, var(--el-border-color))
  //       inset;
  //     cursor: default;
  //     .el-input__inner {
  //       cursor: default !important;
  //       border: none !important;
  //     }
  //   }
  //   :deep(.el-textarea__inner) {
  //     box-shadow: 0 0 0 0px var(--el-input-border-color, var(--el-border-color))
  //       inset;
  //     resize: none;
  //     cursor: default;
  //     border: none;
  //   }
  // }
</style>
