<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :lg="12" :md="12" :sm="24">
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
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="200"
              :templates="templates"
              template="jcjy"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="footer && !$store.state.work.processMobile"
          >
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
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  import { uploadApi } from '@/api/fwgl/api'
  import { dailyManagementOther, fetchApi } from '@/api/fwgl/api'
  import { download } from '@/api/fwgl/zzxx'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  const token = store.getters['user/token']
  const { saveOrUpdate, detail } = dailyManagementOther
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

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
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    computed: {},
    watch: {
      'formData.describe'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    created() {},
    mounted() {},
    methods: {
      async showEdit(title, formId, flowtaskinfoflowid, ymFromId, status) {
        this.dialogFormVisible = true
        if (formId) {
          this.loading = true
          const res = await fetchApi(detail, { id: formId })

          this.loading = false
          if (res && res.code === 200 && res.data) {
            this.formData = res.data.otherFileMessage
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

        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      close() {
        this.formData = {}

        this.$bus.$emit('updateMsg', 0)
        this.tableData = []
        this.footer = true
      },
      add() {
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
            // this.close()
          } else {
            return false
          }
        })
      },
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
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
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      //提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },

      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
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
