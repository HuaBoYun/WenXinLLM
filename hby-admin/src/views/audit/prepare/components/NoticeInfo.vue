<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="disabled"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="审计通知书编号"
            label-width="140px"
            prop="advicecoed"
          >
            <el-input
              v-model="formData.advicecoed"
              clearable
              placeholder="请输入审计通知书编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="审计通知书名称"
            label-width="140px"
            prop="advicename"
          >
            <el-input
              v-model="formData.advicename"
              clearable
              placeholder="请输入审计通知书名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="关联OA" label-width="140px" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请选择OA信息"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              style="margin-left: 20px"
              type="primary"
              @click="openOA()"
            >
              选择
            </el-button>
            <el-button
              v-if="formData.url"
              style="margin-left: 20px"
              type="primary"
              @click="detail()"
            >
              详情
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="disabled"
              template="sjtzs"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            style="text-align: right; margin-bottom: 5px"
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
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
          <el-table :data="tableDataFile">
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
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(scope.row)"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确定</el-button>
    </div>
    <oaList ref="oaList" @selected="handleOA" />
    <!-- <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template> -->

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  // import Tinymce from '@/components/Tinymce'
  import { getOaurl } from '@/api/contract/manage'
  import { download } from '@/api/audit/implement'
  import oaList from './oaList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import {
    createNoticeCode,
    deleteNoticeFile,
    getNoticeDefaultData,
    noticeAdd,
    noticeFileList,
  } from '@/api/audit/preparation'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const { baseURL } = require('@/config')
  export default {
    name: 'ProjectDataInfo',
    components: {
      UEditor,
      oaList,
      ZXPerson,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        footerFlag: false,
        formData: {
          advicecoed: '',
          advicename: '',
          adviceid: '',
          content: '',
          title: '',
          oaid: '',
          h5url: '',
          url: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        templates: [],
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        disabled: false,
        rules: {},
        dialogFormVisible: false,
        title: '',
        adviceid: '',

        rules: {
          advicecoed: [
            { required: true, message: '请输入通知书编号', trigger: 'change' },
          ],
          advicename: [
            { required: true, message: '请输入通知书名称', trigger: 'change' },
          ],
        },
        showMJ: false,
        MJoption: [],
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
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
    async created() {
      this.showMJ = couldMJ()
      console.log(this.showMJ)
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('PrepareNotice')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {},
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        console.log(val)
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, disabled) {
        //新建按钮入口
        if (!row && !disabled) {
          createNoticeCode().then((res) => {
            this.$set(this.formData, 'advicecoed', res.data.autoCode.toString())
          })
          //新建
          this.title = '新建'
          this.disabled = false
          this.formData = {
            advicecoed: '',
            advicename: '',
            adviceid: '',
            content: '',
            title: '',
            oaid: '',
            h5url: '',
            url: '',
            secrectLevelId: '',
            staffScopeNames: '',
            staffScopeIds: '',
          }
          this.tableDataFile = []
          this.fileIds = []
          this.fileList = []
        } else if (row && !disabled) {
          //编辑按钮
          let res = await noticeFileList({
            adviceid: row.adviceid,
          })
          //回填上传文件表格
          const arr = res.data.data
          const arr1 = arr.map((item) => {
            return {
              ...item,
              name: item.attname,
              size: item.attsize,
              createPerson: item.uploader,
            }
          })
          const arr2 = arr.map((res) => {
            return res.attid
          })
          //收集id
          this.fileIds = arr2
          this.tableDataFile = arr1

          let res1 = await getNoticeDefaultData({
            adviceid: row.adviceid,
          })
          this.title = '修改'
          this.disabled = false
          this.formData = {
            advicecoed: res1.data.Doubtfulpoint.advicecoed,
            advicename: res1.data.Doubtfulpoint.advicename,
            adviceid: res1.data.Doubtfulpoint.adviceid,
            content: res1.data.Doubtfulpoint.content,
            title: res1.data.Doubtfulpoint.title,
            oaid: res1.data.Doubtfulpoint.oaid,
            h5url: res1.data.Doubtfulpoint.h5url,
            url: res1.data.Doubtfulpoint.url,
            secrectLevelId: res1.data.Doubtfulpoint.secrectLevelId,
            staffScopeNames: res1.data.Doubtfulpoint.staffScopeNames,
            staffScopeIds: res1.data.Doubtfulpoint.staffScopeIds,
          }
        } else if (row && disabled) {
          //查看按钮
          let res1 = await getNoticeDefaultData({
            adviceid: row.adviceid,
          })
          let res = await noticeFileList({
            adviceid: row.adviceid,
          })
          //回填上传文件表格
          const arr = res.data.data
          const arr1 = arr.map((item) => {
            return {
              ...item,
              name: item.attname,
              size: item.attsize,
              createPerson: item.uploader,
              attid: item.attid,
            }
          })
          this.tableDataFile = arr1
          this.formData = {
            advicecoed: res1.data.Doubtfulpoint.advicecoed,
            advicename: res1.data.Doubtfulpoint.advicename,
            adviceid: res1.data.Doubtfulpoint.adviceid,
            content: res1.data.Doubtfulpoint.content,
            title: res1.data.Doubtfulpoint.title,
            oaid: res1.data.Doubtfulpoint.oaid,
            h5url: res1.data.Doubtfulpoint.h5url,
            url: res1.data.Doubtfulpoint.url,
            secrectLevelId: res1.data.Doubtfulpoint.secrectLevelId,
            staffScopeNames: res1.data.Doubtfulpoint.staffScopeNames,
            staffScopeIds: res1.data.Doubtfulpoint.staffScopeIds,
          }
          this.disabled = true
          this.title = '查看'
        }
        this.dialogFormVisible = true
      },
      async handleDeleteFile(index, row) {
        let res = await deleteNoticeFile({ attId: row.attid })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.fileIds.splice(index, 1)
          this.tableDataFile.splice(index, 1)
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['elForm'].resetFields()
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableDataFile.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            let { code, msg } = await noticeAdd({
              ...this.formData,
              advicecoed: this.formData.advicecoed,
              adviceid: this.formData.adviceid,
              advicename: this.formData.advicename,
              content: this.formData.content,
              attids: attids,
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          }
        })
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },
      openOA() {
        this.$refs.oaList.show()
      },
      handleOA(e) {
        this.formData.title = e[0].subject
        this.formData.oaid = e[0].id
        this.formData.h5url = e[0].h5Url
        this.formData.url = e[0].url
      },
      async detail() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        window.open(`${this.oaurl}${this.formData.url}&ticket=${this.ticket}`)
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
          this.fileList = [...this.fileList, ...file.data]
          this.tableDataFile = [...this.tableDataFile, ...file.data]
          // this.tableData = list
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
