<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="文件名称" prop="fileName">
            <el-input
              v-model="formData.fileName"
              clearable
              placeholder="请输入文件名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文件内容" prop="content">
            <el-input
              v-model="formData.content"
              clearable
              type="textarea"
              placeholder="请输入文件内容"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
            <!-- <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="200"
              :templates="templates"
              template="jcjy"
              style="margin-left: -100px"
            /> -->
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            >
              <template #default="{ row }">
                <div>
                  {{ row.fileSize / 1000 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button
        v-if="
          (formData.state == 2 || formData.state == 3) && jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </div>

    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  import { dailyManagementOther, fetchApi } from '@/api/fwgl/api'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { download } from '@/api/fwgl/zzxx'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']
  const { saveOrUpdate, detail } = dailyManagementOther

  export default {
    name: 'SummanyInfo',
    components: { Tinymce, UEditor, CandidateUserSelect, Resubmit },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
        formData: {
          fileName: '',
          content: '',
        },
        templates: [],
        footer: true,
        rules: {
          fileName: [
            {
              required: true,
              message: '请输入文件名',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入内容',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {},
    watch: {
      // 'formData.describe'(val) {
      //
      //
      //   if (this.$refs['ueditor'].editor.openTemplate) {
      //     this.$refs['ueditor'].editor.openTemplate = false
      //     let s = val
      //     const arr = [
      //       ['$[contract.contractno]', 'contractno'],
      //       ['$[contract.contractname]', 'contractname'],
      //       ['$[contract.contractamount]', 'contractmoney'],
      //       ['$[contract.contractItem]', 'contractitem'],
      //       ['$[contract.executor]', 'realname'],
      //       ['$[contract.rmbinwords]', 'hzsumowing'],
      //       ['$[counterpart.coupersion]', 'counterpartcode'],
      //       ['$[counterpart.personincharge]', 'contractbd'],
      //       ['$[counterpart.counterpartHank]', 'bankkhyh'],
      //       ['$[counterpart.counumber]', 'counterpartno'],
      //       ['$[counterpart.couname]', 'budgetname'],
      //       ['$[counterpart.couaddress]', 'counterpartaddress'],
      //       ['$[counterpart.coupersion]', 'contacts'],
      //       ['$[counterpart.contactsPhone]', 'contactsphone'],
      //       ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
      //       ['$[counterpart.legarepresentative]', 'contacts'],
      //       ['$[counterpart.pctelephonenumber]', 'contractzd'],
      //       // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
      //     ]
      //     arr.forEach((i) => {
      //       if (this.formData[i[1]]) {
      //         s = s.replace(i[0], this.formData[i[1]])
      //       }
      //     })
      //     this.formData.content = s
      //   }
      // },
    },
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })

          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data.otherFileMessage

            if (
              res.data.otherFileMessage.state == 2 ||
              res.data.otherFileMessage.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 27,
                formId: row.id,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.id + ''
                this.fromIdcopy = row.id + ''
                this.ymFromId = res2.data.id + ''

                const res3 = await getFaqiInfo({
                  id: res2.data.id,
                  flowId: res2.data.flowId,
                })
                if (res3.code == 1) {
                  this.status = res3.data.dataJson.flowTaskInfo.status
                }
              }
            }
            this.tableData = res.data.files || []
          } else {
            this.$message({
              message: '获取详情失败',
              type: 'error',
            })
          }
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetchData')
        this.tableData = []
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.fileIds = this.tableData
              .map((x) => x.fileId)
              .join(',')
            const res = await fetchApi(saveOrUpdate, this.formData)
            if (res && res.code === 200) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage('操作失败！', 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
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
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
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
      /**
       * @description: 引迈流程提交
       * @return {*}
       */      
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
