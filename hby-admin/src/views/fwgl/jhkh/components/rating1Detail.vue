<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1500px"
      @close="close"
    >
      <el-row :gutter="24">
        <div class="tableA">
          <el-table
            :data="tableData"
            :span-method="objectSpanMethod"
            :key="tableKey"
            border
            style="width: 100%"
          >
            <el-table-column prop="ONE" label="考评重点" width="150">
              <template #default="{ row }">
                {{ row.cols[0] }}
              </template>
            </el-table-column>
            <el-table-column prop="TWO" label="分值" width="60">
              <template #default="{ row }">
                {{ row.cols[1] }}
              </template>
            </el-table-column>
            <el-table-column prop="THREE" label="考评内容" width="220">
              <template #default="{ row }">
                {{ row.cols[2] }}
              </template>
            </el-table-column>
            <el-table-column prop="explain" label="评分标准">
              <template #default="{ row }">
                {{ row.cols[3] }}
              </template>
            </el-table-column>
            <!-- <el-table-column prop="id" label="主键id"></el-table-column> -->

            <!-- <el-table-column prop="score" label="自评分" width="100">
              <template #default="{ row }">
                <el-input
                  type="number"
                  placeholder="分数"
                  :readonly="row.id === 'total'"
                  @change="handleScoreChange"
                  v-model="selfGrade[row.id]"
                ></el-input>
              </template>
            </el-table-column> -->

            <el-table-column prop="scope" label="自评分" width="100">
              <template #default="{ row }">
                <el-input
                  type="number"
                  placeholder="分数"
                  :readonly="row.id === 'total'"
                  @change="handleScoreChange"
                  v-model="deductMarks[row.id]"
                  disabled
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              prop="scope"
              label="相关材料情况（上传附件）"
              width="100"
            >
              <!-- <template v-if="!files[row.id]">
                <el-upload
                  class="upload-demo"
                  :show-file-list="false"
                  :action="baseURL + uploadApi"
                  :headers="headers"
                  :on-success="(res) => handleSuccess(row.id, res)"
                >
                  <el-button type="primary">上传</el-button>
                </el-upload>
              </template>
              <template v-else>
                {{ fileNames[row.id] }}
                <p style="margin-top: ;10px">
                  <el-button
                    type="text"
                    @click="handlePreviewFile({ fileId: files[row.id] })"
                  >
                    预览
                  </el-button>
                  <el-button
                    type="text"
                    @click="
                      handleDown({
                        fileId: files[row.id],
                        fileName: fileNames[row.id],
                      })
                    "
                  >
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handleDelete({ fileId: files[row.id], id: row.id })"
                  >
                    删除
                  </el-button>
                </p>
              </template> -->

              <template #default="{ row }">
                <el-button
                  type="primary"
                  @click="openFileModal(row.annualExamineTopicExtId)"
                >
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
      </template>
      <FileModal ref="file" @getFileInfo="getFileInfo" />
    </el-dialog>
  </div>
</template>
<script>
  import {
    getExamineQuestionList,
    asveExamineScore,
    getExamineAnswerList,
    deleteFile,
  } from '@/api/fwgl/jhkh'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import FileModal from './fileDetail.vue'
  import { download } from '@/api/fwgl/zzxx'
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  const token = store.getters['user/token']

  export default {
    props: [],
    components: { FileModal },
    data() {
      return {
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        dialogFormVisible: false,
        title: '外部监管考核',
        footer: true,
        formData: {},
        tableData2: [],
        tableData: [],
        selfGrade: {},
        deductMarks: {},
        files: {},
        fileNames: {},
        mainId: '',
        tableKey: 'v',
        len: 0,
        keyId: {},
      }
    },
    methods: {
      show(id, mainId) {
        this.dialogFormVisible = true
        this.fetchData(id)
        this.mainId = mainId
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.selfGrade = {}
        this.deductMarks = {}
        this.files = {}
        this.fileNames = {}
        this.mainId = ''
      },
      async fetchData(id) {
        if (id) {
          const scoreRes = await getExamineAnswerList({ scoreTransaction: id })
          if (scoreRes && scoreRes.code === 200) {
            const selfGrade = {} //自评分
            const deductMarks = {} //分数
            const files = {}
            const fileNames = {}
            const keyIds = {}
            this.len = scoreRes.data.length
            scoreRes.data.forEach((x) => {
              selfGrade[x.annualExamineTopicExtId] = x.selfGrade
              deductMarks[x.annualExamineTopicExtId] = x.deductMarks
              files[x.annualExamineTopicExtId] = x.fileIds
              fileNames[x.annualExamineTopicExtId] = x.fileName
              keyIds[x.annualExamineTopicExtId] = x.annualExamineScoreExtId
            })
            this.selfGrade = selfGrade
            this.deductMarks = deductMarks
            this.files = files
            this.fileNames = fileNames
            this.keyId = keyIds
            this.handleScoreChange()
          }
        }
        const res = await getExamineQuestionList({ examineType: 1 })
        if (res && res.code === 200) {
          this.len = res.data.length
          this.formTableData((res.data || []).reverse())
        }
      },
      formTableData(arr) {
        this.tableData2 = arr.map((x) => {
          if (!x.cols) x.cols = []
          x.cols.push(x.examineEmphasis || '')
          x.cols.push(x.score || '')
          x.cols.push(x.content || '')
          x.cols.push(x.gradeCriterion || '')
          x.rowSpan = x.rowspan.split(',').map((y) => Number(y))
          x.colSpan = x.colspan.split(',').map((y) => Number(y))
          x.id = x.annualExamineTopicExtId
          return x
        })
        this.tableData = JSON.parse(JSON.stringify(this.tableData2))
        this.tableData.push({
          id: 'total',
          cols: ['合计', '', '', ''],
          colSpan: [1, 1, 1, 1],
          rowSpan: [1, 1, 1, 1],
        })
      },
      handleSuccess(id, res) {
        if (res && res.data && res.data.fileIds && res.data.fileIds.length) {
          this.$message({
            message: '上传成功',
            type: 'success',
          })
          const ids = res.data.fileIds.map((x) => x.fileId).join(',')
          const fileName = res.data.fileIds.map((x) => x.fileName).join(',')
          this.files[id] = ids
          this.fileNames[id] = fileName
          this.tableKey = Math.random()
        }
      },
      // 计算总分
      handleScoreChange() {
        let total1 = 0
        let total2 = 0
        // 因为数据库是从id是从77开始的
        for (let i = 77; i <= 77 + this.len - 1; i++) {
          if (this.selfGrade[i]) {
            let num = Number(this.selfGrade[i])
            total1 += num
          }
          if (this.deductMarks[i]) {
            let num = Number(this.deductMarks[i])
            total2 += num
          }
        }

        this.$set(this.selfGrade, 'total', total1)
        this.$set(this.deductMarks, 'total', total2)
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        const params = []
        for (let i = 77; i <= this.len + 77 - 1; i++) {
          const obj = {}
          obj.annualExamineId = this.mainId
          obj.examineType = 1
          obj.annualExamineTopicExtId = i
          obj.selfGrade = Number(this.selfGrade[i]) || 0
          obj.deductMarks = Number(this.deductMarks[i]) || 0
          // obj.fileIds = this.files[i] || ''
          obj.annualExamineScoreExtId = this.keyId[i] || ''
          params.push(obj)
        }
        const res = await asveExamineScore(params)
        const totalScore = params.reduce((cur, pre, index) => {
          return +cur + +pre.deductMarks
        }, 0)
        if (res.code == 200) {
          this.$message({
            message: '操作成功',
            type: 'success',
          })

          this.$emit('on-save-success', { type: 1, id: res.data, totalScore })
          this.close()
        } else {
          this.$message({
            message: '操作失败',
            type: 'error',
          })
        }
      },
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        const rowSpan = row.rowSpan[columnIndex]
        const colSpan = row.colSpan[columnIndex]
        if (columnIndex <= 3) {
          return {
            rowspan: rowSpan,
            colspan: colSpan,
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1,
          }
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.files[row.id] = ''
            this.$forceUpdate()
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      openFileModal(id) {
        this.$refs['file'].showEdit(id, this.keyId[id])
      },
      getFileInfo(a, b) {
        this.keyId[b] = a
      },
    },
  }
</script>
<style scoped>
  .tableB {
    margin-top: 80px;
  }

  /* // 一定要写这个，评分那一列使用了 换行符 "\n",加这个才能生效 */
  .el-table .cell {
    white-space: pre-line;
  }
</style>
