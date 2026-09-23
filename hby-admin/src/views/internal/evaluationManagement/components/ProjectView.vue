<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="650px"
      @close="close"
      append-to-body
      v-if="dialogFormVisible"
    >
      <el-form
        ref="form"
        label-width="120px"
        :model="form"
        :rules="rules"
        :validate-on-rule-change="false"
        :disabled="formDisable"
      >
        <!-- <el-col
          v-for="item in renderData"
          :key="item.field"
          :span="item.componentWidth == '50' ? 12 : 24"
        >
          <CustormForm
            :item="item"
            :form="form"
            :ref="item.field"
          ></CustormForm>
        </el-col> -->

        <el-form-item
          label="密级"
          prop="secrectLevelId"
          :rules="[
            { required: true, trigger: 'change', message: '请选择密级' },
          ]"
          v-if="showMJ"
        >
          <el-select
            v-model="form.secrectLevelId"
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

        <el-form-item label="知悉范围" prop="staffScopeNames" v-if="showMJ">
          <el-input
            v-model="form.staffScopeNames"
            readonly
            placeholder="请选择知悉范围"
            :style="{ width: '85%' }"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
            :disabled="!(form.secrectLevelId && !formDisable)"
          >
            选择
          </el-button>
        </el-form-item>

        <el-divider v-if="showMJ">基本信息</el-divider>

        <el-form-item label="评价计划名称" prop="planname">
          <el-input
            v-model="form.planname"
            clearable
            placeholder="请选择评价计划"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openSelecePlanModal"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="评价项目编号" prop="assessid">
          <el-input v-model.trim="form.assessid" disabled />
        </el-form-item>
        <el-form-item label="评价项目名称" prop="assessname">
          <el-input v-model.trim="form.assessname" />
        </el-form-item>
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
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="评价负责人" prop="realname">
          <el-input
            v-model="form.realname"
            clearable
            placeholder="请选择评价负责人"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openPersonModal"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="评价对象" prop="orgname">
          <el-input
            v-model="form.orgname"
            clearable
            placeholder="请选择评价对象"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openObjectModal"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="评价模板" prop="templatename">
          <el-input
            v-model="form.templatename"
            clearable
            placeholder="请选择评价模板"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openTemplateModal"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
          <div class="el-form-item__error">*模板选定之后将不能修改*</div>
        </el-form-item>

        <!-- <el-form-item label="评价小组名称" prop="assteamname">
          <el-input v-model="form.assteamname" @change="$forceUpdate()" />
        </el-form-item>
        <el-form-item label="评价小组组长" prop="assteamleadname">
          <el-input
            v-model="form.assteamleadname"
            clearable
            placeholder="请选择评价小组组长"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openPersonModal1"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="评价小组组员" prop="assteammember">
          <el-input
            v-model="form.assteammember"
            clearable
            placeholder="请选择评价小组组员"
            disabled
            style="width: 423px"
          />
          <el-button
            @click="openPersonModal2"
            style="margin-left: 10px"
            type="primary"
          >
            选择
          </el-button>
        </el-form-item> -->
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisable"
          >
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
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!formDisable"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>

      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" v-if="!formDisable">
          确 定
        </el-button>
        <!-- <el-button
          v-if="(form.status == 2 || form.status == 3) && jurisdictionCode == 1"
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button> -->
      </template>
    </el-dialog>
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :secrectLevelId="form.secrectLevelId"
    />
    <SelectPersonModal ref="SelectPersonModal1" @projectManage="selectP1" />
    <SelectObjectModal ref="SelectObjectModal" @selectO="selectO" />
    <SelectTemplateModal
      ref="SelectTemplateModal"
      @selectT="selectT"
      :orgids="this.form.orgid"
      :secrectLevelId="form.secrectLevelId"
    />
    <CompanySelectUserByTree
      :isUserName="true"
      ref="executor"
      @projectManage="handleExecutorSelected"
    />
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
    <selectPlanModal ref="selectPlanModal" @selected="handlePlanSelected" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    createProjectCode,
    getProjectDefaultInfo,
    projectAdd,
    projectUpdate,
    getDefaultRenderData,
    download,
  } from '@/api/internal/project'
  import { delGroupTestPlanAtt } from '@/api/internal/plan'
  import { UTCformat, hasMJ, couldMJ } from '@/utils'
  import SelectObjectModal from './selectObject.vue'
  import SelectPersonModal from '@/components/danxuanPerson.vue'
  import selectPlanModal from './selectPlanModal.vue'
  import SelectTemplateModal from './selectTemplateModal.vue'
  import CompanySelectUserByTree from '@/components/selectPerson.vue'
  import CustormForm from '@/components/customForm/index.vue'
  import { getFlowTaskInfo } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getFaqiInfo } from '@/api/setting/msg'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage.js'
  import { getMJ } from '@/api/setting/mjsz'
  import ZXPerson from '@/components/selectPerson.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'ProjectView',
    components: {
      SelectPersonModal,
      SelectObjectModal,
      SelectTemplateModal,
      CustormForm,
      CompanySelectUserByTree,
      CandidateUserSelect,
      Resubmit,
      selectPlanModal,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/nkhg/nbkz/pjgl/updateupload ',
        api2: '/nkhg/nbkz/pjbg/addupload',
        token: store.getters['user/token'],
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        form: {
          assessid: '',
          assessname: '',
          date: [],
          templatekey: '',
          orgid: '',
          staffid: '',
          realname: '',
          orgname: '',
          templatename: '',
          assteammember: '',
          assteamlead: '',
          assteamleadname: '',
          assteammemberids: '',
          field1: '',
          planname: '',
          field2Id: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
        },
        tableData: [],
        renderData: [],
        modalType: 'new',
        assid: '', // 用于修改保存
        rules: {
          planname: [
            { required: true, trigger: 'blur', message: '请选择评价计划' },
          ],
          number: [
            { required: true, trigger: 'blur', message: '请输入项目编号' },
          ],
          assessname: [
            { required: true, trigger: 'blur', message: '请输入项目名称' },
          ],
          date: [{ required: true, trigger: 'blur', message: '请输入期限' }],
          realname: [
            { required: true, trigger: 'blur', message: '请输入负责人' },
          ],
          orgname: [
            { required: true, trigger: 'blur', message: '请输入评价对象' },
          ],
          templatename: [
            { required: true, trigger: 'blur', message: '请输入评价模板' },
          ],
          assteamname: [
            { required: true, trigger: 'blur', message: '请输入评价小组名称' },
          ],
          assteamleadname: [
            { required: true, trigger: 'blur', message: '请选择评价小组组长' },
          ],
          assteammember: [
            { required: true, trigger: 'blur', message: '请选择评价小组组员' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        jurisdictionCode: 0,
        formDisable: false,
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
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
    async created() {
      if (couldMJ()) {
        // 获取密级,菜单id
        const res = await hasMJ('EvaluationManagementProject')
        console.log('hasMJres', res)
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
      this.showMJ = couldMJ()
    },
    methods: {
      async showEdit(row, type) {
        // getDefaultRenderData({
        //   sceneCode: 'nkgl-pjlx',
        // }).then((res) => {
        //   let arr = {}
        //   this.renderData = res.data
        //   res.data.forEach((i) => {
        //     arr[i.field] = [
        //       {
        //         required:
        //           i.isSystemRequired == 1
        //             ? true
        //             : i.isRequired == 1
        //             ? true
        //             : false,
        //         message: `请输入${i.name}`,
        //         trigger: 'blur',
        //       },
        //     ]
        //   })
        //   this.rules = arr
        // })
        if (!row) {
          this.modalType = 'new'
          this.title = '添加'
          createProjectCode({
            column: 'ASSESSID',
            noId: 309,
            orgCol: 'TBLCOMANY',
            tblName: 'TBL_ASSESS',
          }).then((res) => {
            this.form.assessid = res.data || ''
          })
          // this.form.assessid = 'LX-2023-999'
        } else {
          this.title = '编辑'
          this.modalType = 'edit'
          // this.form = Object.assign({}, row)
          let res = await getProjectDefaultInfo({ selectedPlans: row.assid })
          this.form.date = [
            UTCformat(res.data.assess.startdate),
            UTCformat(res.data.assess.enddate),
          ]
          this.form.assessid = res.data.assess.assessid
          this.form.assessname = res.data.assess.assessname
          this.form.staffid = res.data.fuzeren.staffid
          this.form.realname = res.data.fuzeren.realname
          this.form.assteamname = res.data.assess.assteamname
          this.form.templatekey = res.data.moban.asstemid
          this.form.templatename = res.data.moban.templename
          this.form.assteammember = res.data.assess.assteammember
          this.form.assteamlead = res.data.assess.assteamlead
          this.form.assteamleadname = res.data.assess.assteamleadname
          this.form.assteammemberids = res.data.assess.assteammemberids
          this.form.planname = res.data.assess.planname
          this.form.planid = res.data.assess.planid
          this.assid = res.data.assess.assid
          this.form.orgid = res.data.orgids && res.data.orgids.split(',')
          this.form.orgname = res.data.orgName
          this.form.secrectLevelId = res.data.assess.secrectLevelId
          this.form.staffScopeIds = res.data.assess.staffScopeIds
          this.form.staffScopeNames = res.data.assess.staffScopeNames
          this.tableData = res.data.atts
        }
        if (type) {
          this.title = '详情'
          this.formDisable = true
        }
        // if (row.status == 2 || row.status == 3) {
        //   const res2 = await getFlowTaskInfo({ tableId: 42, formId: row.assid })
        //   this.jurisdictionCode = res2.code
        //   if (res2.code == 1) {
        //     this.flowtaskinfoflowid = res2.data.flowId
        //     this.fromId = row.assid
        //     this.fromIdcopy = row.assid
        //     this.ymFromId = res2.data.id

        //     const res3 = await getFaqiInfo({
        //       id: res2.data.id,
        //       flowId: res2.data.flowId,
        //     })
        //     if (res3.code == 1) {
        //       this.status = res3.data.dataJson.flowTaskInfo.status
        //     }
        //   }
        // }
        this.dialogFormVisible = true
      },
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
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
            this.form.planname = ''
            this.form.planid = ''
            this.form.templatename = ''
            this.form.templatekey = ''
          }
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.assid = ''
        this.tableData = []
        this.form = this.$options.data().form
        this.formDisable = false
        this.dialogFormVisible = false
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
              orgid: this.form.orgid ? this.form.orgid.toString() : '',
              start: this.form.date[0],
              end: this.form.date[1],
              templatekey: +this.form.templatekey,
              attids: attids,
            }
            delete params.date
            if (this.title == '添加') {
              const { msg, data, code } = await projectAdd(params)
              if (code == 1) {
                // this.assid = data.assid
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.close()
              }
            }
            if (this.title == '编辑') {
              const { attid, ...other } = params
              const { msg, code } = await projectUpdate({
                ...other,
                assid: this.assid,
              })
              if (code == 200) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.close()
              }
            }
          }
        })
      },
      openPersonModal() {
        this.$refs['SelectPersonModal'].showEdit()
      },
      openPersonModal1() {
        this.$refs['SelectPersonModal1'].showEdit()
      },
      openPersonModal2() {
        this.$refs['executor'].showEdit()
      },
      openObjectModal() {
        this.$refs['SelectObjectModal'].showEdit()
      },
      openTemplateModal() {
        console.log(!this.form.orgid, 'ori')
        if (!this.form.orgid) {
          this.$baseMessage(
            '请先选择评价对象',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$refs['SelectTemplateModal'].showEdit()
      },
      selectP(val) {
        this.$set(this.form, 'staffid', val[0].staffid)
        this.$set(this.form, 'realname', val[0].realname)
      },
      selectP1(val) {
        this.$set(this.form, 'assteamlead', val[0].staffid)
        this.$set(this.form, 'assteamleadname', val[0].realname)
      },
      handleExecutorSelected(val) {
        let assteammember = val.map((item) => {
          return item.realname
        })
        let assteammemberids = val.map((item) => {
          return item.staffid
        })
        this.$set(this.form, 'assteammember', assteammember.join(','))
        this.$set(this.form, 'assteammemberids', assteammemberids.join(','))
      },
      selectO(val) {
        this.$set(this.form, 'orgid', val.id)
        this.$set(this.form, 'orgname', val.name)
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
        await delGroupTestPlanAtt({ attid: row.attid })
        this.$message.success('删除成功')
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData || []
      //     list.push(file.data.Attachment)
      //     this.tableData = list
      //     this.$message({
      //       type: 'success',
      //       message: '删除成功!',
      //     })
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      openSelecePlanModal() {
        this.$refs.selectPlanModal.showEdit(this.form.secrectLevelId)
      },
      handlePlanSelected(val) {
        console.log('🚀 ~ handlePlanSelected ~ val:', val)
        if (val) {
          this.form.planname = val[0].assessname
          this.form.planid = val[0].assid
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
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // if (!this.assid) {
        //   this.$baseMessage('请先保存！', 'error')
        //   return false
        // }
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
