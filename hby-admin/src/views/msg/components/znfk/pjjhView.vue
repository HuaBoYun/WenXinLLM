<template>
  <div>
    <el-form
      ref="form"
      label-width="120px"
      :model="form"
      :rules="rules"
      :validate-on-rule-change="false"
    >
      <el-row>
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级1"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="form.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="!footer"
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
              v-model="form.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价计划编号" prop="assessid">
            <el-input v-model.trim="form.assessid" :disabled="!footer" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价计划名称" prop="assessname">
            <el-input v-model.trim="form.assessname" :disabled="!footer" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价期限" prop="date">
            <el-date-picker
              v-model="form.date"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              :disabled="!footer"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价负责人" prop="realname">
            <el-input
              v-model="form.realname"
              clearable
              placeholder="请选择评价负责人"
              disabled
              style="width: 256px"
            />
            <el-button
              @click="openPersonModal"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价对象" prop="orgnames">
            <el-input
              v-model="form.orgnames"
              clearable
              placeholder="请选择评价对象"
              disabled
              style="width: 256px"
            />
            <el-button
              @click="openObjectModal"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="form.content"
            :height="300"
            :templates="templates"
            template="PJJH"
          />
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="
                assid ? baseApi + api + '?assId=' + assid : baseApi + api2
              "
              :headers="{
                token: token,
              }"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
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
                <el-button type="text" @click="handleDowns(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button v-if="footer" type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>

    <div style="text-align: right; margdivin-top: 10px" v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <SelectPersonModal ref="SelectPersonModal" @projectManage="selectP" />
    <SelectObjectModal
      ref="SelectObjectModal"
      @selectO="selectO"
      :multiple="true"
    />
    <SelectTemplateModal
      ref="SelectTemplateModal"
      @selectT="selectT"
      :orgids="this.form.orgids"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <executor-options
      ref="executor"
      @projectManage="handleExecutorSelected"
      :secrectLevelId="form.secrectLevelId"
    />
  </div>
</template>

<script>
  import {
    getPlanDefaultInfo,
    planAdd,
    planUpdate,
    deleteFile,
    download,
  } from '@/api/internal/project'
  import { UTCformat } from '@/utils'
  import SelectObjectModal from '@/views/internal/evaluationManagement/components/selectObject.vue'
  import SelectPersonModal from '@/components/danxuanPerson.vue'
  import SelectTemplateModal from '@/views/internal/evaluationManagement/components/selectTemplateModal.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import UEditor from '@/components/UEditor'
  import { getPrivewAttInfo } from '@/api/contract/manage.js'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'ProjectView',
    components: {
      SelectPersonModal,
      SelectObjectModal,
      SelectTemplateModal,
      Resubmit,
      UEditor: () => import('@/components/UEditor'),
      ZXPerson,
      ExecutorOptions,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/nkhg/nbkz/pjgl/updateupload ',
        api2: '/nkhg/nbkz/pjbg/addupload',
        token: store.getters['user/token'],
        headers: { token: token },
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        form: {
          assessid: '',
          assessname: '',
          date: [],
          templatekey: '',
          orgids: [],
          staffid: '',
          realname: '',
          orgnames: '',
          content: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
        },
        templates: [],
        tableData: [],
        renderData: [],
        modalType: 'new',
        assid: '', // 用于修改保存
        rules: {
          assessid: [
            { required: true, trigger: 'blur', message: '请输入评价计划编号' },
          ],
          assessname: [
            { required: true, trigger: 'blur', message: '请输入计划名称' },
          ],
          date: [{ required: true, trigger: 'blur', message: '请输入期限' }],
          realname: [
            { required: true, trigger: 'blur', message: '请输入负责人' },
          ],
          orgnames: [
            { required: true, trigger: 'blur', message: '请输入评价对象' },
          ],
        },
        footer: true,
        title: '',
        dialogFormVisible: false,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
      }
    },
    watch: {
      'form.content'(val) {
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
            if (this.form[i[1]]) {
              s = s.replace(i[0], this.form[i[1]])
            }
          })
          this.form.content = s
        }
      },
    },
    computed: {
      getFormLevel() {
        if (!this.form.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.form.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
            this.form.staffid = ''
            this.form.realname = ''
          }
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.footer = title === 'edit'
        if (formId) {
          const res = await getPlanDefaultInfo({ selectedPlans: formId })
          this.form.date = [
            UTCformat(res.data.assess.startdate),
            UTCformat(res.data.assess.enddate),
          ]
          this.form.assessid = res.data.assess.assessid
          this.form.assessname = res.data.assess.assessname
          this.form.staffid = res.data.fuzeren.staffid
          this.form.realname = res.data.fuzeren.realname
          this.assid = res.data.assess.assid
          this.form.orgids =
            res.data.assess.assobjids && res.data.assess.assobjids.split(',')
          this.form.orgnames = res.data.assess.assobjnames
          this.form.content = res.data.assess.content
          this.form.secrectLevelId = res.data.assess.secrectLevelId
          this.form.staffScopeIds = res.data.assess.staffScopeIds
          this.form.staffScopeNames = res.data.assess.staffScopeNames
          this.tableData = res.data.atts || []
          if (res.data.assess?.secrectLevelId) {
            localStorage.setItem(
              'SPsecrectLevelId',
              res.data.assess.secrectLevelId
            )
          }
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.assid = ''
        this.tableData = []
        this.footer = true
        this.form = this.$options.data().form
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          console.log(this.form)
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const params = {
              ...this.form,
              orgids: this.form.orgids ? this.form.orgids.toString() : '',
              start: this.form.date[0],
              end: this.form.date[1],
              templatekey: +this.form.templatekey,
              attids: attids,
            }
            delete params.date

            const { attid, ...other } = params
            const { msg, code } = await planUpdate({
              ...other,
              assid: this.assid,
            })
            if (code == 200) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
            }
          }
        })
      },
      openPersonModal() {
        // this.$refs['SelectPersonModal'].showEdit()
        this.$refs.executor.showEdit(null, this.form.secrectLevelId)
      },
      // openPersonModal1() {
      //   this.$refs['SelectPersonModal1'].showEdit()
      // },
      // openPersonModal2() {
      //   this.$refs['executor'].show()
      // },
      openObjectModal() {
        this.$refs['SelectObjectModal'].showEdit()
      },
      openTemplateModal() {
        this.$refs['SelectTemplateModal'].showEdit()
      },
      selectP(val) {
        this.$set(this.form, 'staffid', val[0].staffid)
        this.$set(this.form, 'realname', val[0].realname)
      },
      selectO(val) {
        const orgids = val.map((x) => x.id).join(',')
        const orgnames = val.map((x) => x.name).join(',')
        this.$set(this.form, 'orgids', orgids)
        this.$set(this.form, 'orgnames', orgnames)
      },
      selectT(val) {
        this.$set(this.form, 'templatename', val[0].TEMPLENAME)
        this.$set(this.form, 'templatekey', val[0].ASSTEMID)
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
      // handleBeforeUpload() {
      //   if (!this.assid) {
      //     this.$baseMessage('请先保存！', 'error')
      //     return false
      //   } else {
      //     return true
      //   }
      // },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        const res = await deleteFile({ attid: row.attid })
        if (res && res.code === 200) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData || []
      //     list.push(file.data.Attachment)
      //     this.tableData = list
      //     this.$baseMessage('上传成功', 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      //提交
      async ymsubmit() {
        try {
          this.$refs['form'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      // },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.form.staffScopeIds = ids
        this.form.staffScopeNames = names
      },
      handleExecutorSelected(val) {
        this.$set(this.form, 'realname', val[0].realname)
        this.$set(this.form, 'staffid', val[0].staffid)
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
          // this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        // if (!this.assid) {
        //   this.$baseMessage('请先保存！', 'error')
        //   return false
        // }
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
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
