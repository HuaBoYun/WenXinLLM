<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="线索编号" prop="cluenaber">
            <el-input
              v-model="formData.cluenaber"
              clearable
              placeholder="请输入线索编号"
              :style="{ width: '246px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.list.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核实内容" prop="verifycontent">
            <el-input
              v-model="formData.verifycontent"
              clearable
              placeholder="请输入核实内容"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核实范围" prop="cluehsfw">
            <el-input
              v-model="formData.cluehsfw"
              clearable
              placeholder="请输入核实范围"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作组织" prop="workorganization">
            <el-input
              v-model="formData.workorganization"
              clearable
              placeholder="请输入工作组织"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间安排" prop="timearrange">
            <el-input
              v-model="formData.timearrange"
              clearable
              placeholder="请输入时间安排"
              :style="{ width: '256px', height: '28px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否需要移送纪检部门" prop="wghsscope">
            <el-radio-group
              v-model="formData.wghsscope"
              :disabled="!footer"
              @input="handleChange"
            >
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="handlerid">
            <el-input
              v-model="formData.handlername"
              clearable
              placeholder="请输入经办人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.manage.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="核查工作开展过程情况" prop="cluename">
            <el-input
              v-model="formData.cluename"
              clearable
              placeholder="请输入核查工作开展过程情况"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="发现的主要问题" prop="cluewgqx">
            <el-input
              v-model="formData.cluewgqx"
              clearable
              placeholder="请输入发现的主要问题"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="责任定性情况及依据" prop="cluessqk">
            <el-input
              v-model="formData.cluessqk"
              clearable
              placeholder="请输入责任定性情况及依据"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资产损失认定情况" prop="cluewjsm">
            <el-input
              v-model="formData.cluewjsm"
              clearable
              placeholder="请输入资产损失认定情况"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="责任追究处理情况" prop="repdesc">
            <el-input
              v-model="formData.repdesc"
              clearable
              placeholder="请输入责任追究处理情况"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="!footer"
              template="nbsj"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDowns(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <projectManage
      :modal="false"
      ref="manage"
      @projectManage="reviewTypeSelect"
    />
    <projectList ref="list" @selected="handleProjectSelected" :type="2" />
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import { wghsSave, wghsRemoveFile, wgbgFileListNew } from '@/api/audit/wgzz'
  import store from '@/store'
  import projectManage from '@/components/danxuanPerson.vue'
  import projectList from './options/projectList.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {
      UEditor: () => import('@/components/UEditor'),
      projectManage,
      projectList,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          clueid: undefined,
          cluenaber: undefined,
          verifycontent: undefined,
          content: undefined,
          cluehsfw: undefined,
          wghsscope: undefined,
          workorganization: undefined,
          timearrange: undefined,
          handlername: undefined,
          handlerid: undefined,
          cluename: undefined,
          cluewgqx: undefined,
          cluessqk: undefined,
          cluewjsm: undefined,
          repdesc: undefined,
        },
        templates: [],
        footer: true,
        tableFlag: false,
        tableData: [],
        rules: {
          cluenaber: [
            {
              required: true,
              message: '请输入线索编号',
              trigger: 'blur',
            },
          ],
          verifycontent: [
            {
              required: true,
              message: '请输入核实内容',
              trigger: 'blur',
            },
          ],
          wghsscope: [
            {
              required: true,
              message: '请选择是否需要移送纪检部门',
              trigger: 'blur',
            },
          ],
          timearrange: [
            {
              required: true,
              message: '请选择时间',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        staffid: '',
        arr: [],
      }
    },
    computed: {},
    watch: {
      'formData.content'(val) {
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
    methods: {
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = Object.assign(this.formData, row)
          this.getFileList(row.id)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.$set(this.formData, 'handlername', userInfo.realname)
          this.$set(this.formData, 'handlerid', userInfo.staffid)
        }
      },
      async getFileList(id) {
        // const data = await wghsFileList({ clueid: id })
        const data = await wgbgFileListNew({ id })

        if (data.code == '1') {
          this.tableData = data.data.data || []
        } else {
          this.tableData = []
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData.clueid = undefined
        this.formData.cluenaber = undefined
        this.formData.verifycontent = undefined
        this.formData.content = undefined
        this.formData.cluehsfw = undefined
        this.formData.wghsscope = undefined
        this.formData.workorganization = undefined
        this.formData.timearrange = undefined
        this.formData.handlername = undefined
        this.formData.handlerid = undefined
        this.formData.cluename = undefined
        this.formData.cluewgqx = undefined
        this.formData.cluessqk = undefined
        this.formData.cluewjsm = undefined
        this.formData.repdesc = undefined
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)

            delete this.formData.impcreateusername
            const data = await wghsSave({
              ...this.formData,
              attIds,
            })

            if (data.code == 1) {
              this.formData = {}
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData || []
        list = list.filter((item) => item.attid != row.attid)
        await wghsRemoveFile({ attid: row.attid })
        this.$message.success('删除成功')
        this.tableData = list
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, 'handlername', e[0].realname)
        this.$set(this.formData, 'handlerid', e[0].staffid)
      },
      handleProjectSelected(node) {
        this.$set(this.formData, 'cluenaber', node.cluenaber)
        this.$set(this.formData, 'clueid', node.clueid)
      },

      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
