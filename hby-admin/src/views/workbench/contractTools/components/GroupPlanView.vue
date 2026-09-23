<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15" v-loading="loading">
        <el-form
          :disabled="alldisabled"
          ref="elForm"
          label-width="140px"
          :model="formData"
          :rules="rules"
          size="medium"
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
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId || alldisabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估计划编号" prop="plancode">
              <el-input
                v-model="formData.plancode"
                readonly
                placeholder="请输入评估计划编号"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估计划名称" prop="planName">
              <el-input
                v-model="formData.planName"
                clearable
                placeholder="请输入评估计划名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划开始时间" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择计划开始时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划结束时间" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                clearable
                format="yyyy-MM-dd"
                placeholder="请选择计划结束时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估标准" prop="assname">
              <el-input
                placeholder="请选择评估标准"
                readonly
                v-model="formData.assname"
                @click.native="handleSetAss"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select
                v-model="formData.planType"
                clearable
                placeholder="请选择计划类型"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in typeOptions"
                  :key="index"
                  :disabled="item.disabled"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制定机构" prop="orgname">
              <el-input
                v-model="formData.orgname"
                disabled
                clearable
                placeholder="请输入制定机构"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="录入人员" prop="recorder">
              <el-input
                v-model="formData.recorder"
                clearable
                disabled
                placeholder="请输入录入人员"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.toIssued == 1">
            <el-form-item label="下发人员信息" prop="issuedStaffName">
              <el-input
                v-model="formData.issuedStaffName"
                clearable
                disabled
                placeholder="下发人员信息"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.toIssued == 1">
            <el-form-item label="下发时间" prop="issueddate">
              <el-input
                v-model="formData.issueddate"
                clearable
                disabled
                placeholder="下发时间"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评估计划描述" prop="plandes">
              <el-input
                v-model="formData.plandes"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入评估计划描述"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              template="JTPGJH"
            />
          </el-col>

          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
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
                  <el-button type="text" @click="handleDeleteAttach(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>

    <standard ref="setStandard" @handelStandard="handelStandard" />

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import UEditor from '@/components/UEditor'
  import {
    riplanadd,
    riPlanAddPage,
    riplaninfo,
    saveUsers,
  } from '@/api/systemLog'
  import { downFieldById, getGroupPlanCode } from '@/api/risk/riskEvents'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'PlanEdit',
    components: {
      standard: () => import('@/views/risk/assessment/components/standard.vue'),

      CompanySelectUserByTree,
      CandidateUserSelect,
      UEditor,
      ZXPerson,
    },
    data() {
      return {
        title: '',
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        isAdd: '',
        loading: false,
        dialogFormVisible: false,
        fileList: [],
        assrisk: {},
        formData: {
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          planType: undefined,
          orgname: undefined,
          recorder: undefined,
          plandes: undefined,
          content: undefined,
          id: 0,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
          realname: undefined,
          assId: undefined,
          assname: undefined,
        },
        fileIdList: [],
        templates: [],
        removeIds: [],
        list: [],
        showRow: {},
        tableData: [],
        tableDataFile: [],
        rules: {
          plancode: [
            {
              required: true,
              message: '请输入评估计划编号',
              trigger: 'blur',
            },
          ],
          planName: [
            {
              required: true,
              message: '请输入评估计划名称',
              trigger: 'blur',
            },
          ],
          startDate: [
            {
              required: true,
              message: '请选择计划开始时间',
              trigger: 'blur',
            },
          ],
          endDate: [
            {
              required: true,
              message: '请选择计划结束时间',
              trigger: 'blur',
            },
          ],
          assname: [
            {
              required: true,
              message: '请选择评估标准',
              trigger: 'blur',
            },
          ],
          planType: [
            {
              required: true,
              message: '请选择计划类型',
              trigger: 'blur',
            },
          ],
          orgname: [],
          realname: [],
          plandes: [
            {
              required: true,
              message: '请输入计划描述',
              trigger: 'blur',
            },
          ],
        },
        typeOptions: [
          {
            label: '年度计划',
            value: '1',
          },
          {
            label: '临时性计划',
            value: '2',
          },
        ],
        showRow: {},
        showMJ: false,
        MJoption: [],
        alldisabled: false,
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
      if (this.showMJ) {
        const res = await hasMJ('AssessmentPlan')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      handleSetAss(row) {
        this.$refs['setStandard'].showEdit(this.formData.secrectLevelId)
      },
      changeMJ(selectedValue) {
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
            this.formData.assId = undefined
            this.formData.assname = undefined
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      /**
       * @description: 保存
       * @return {*}
       */
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let content = this.formData.content || ''
            const info = {
              ...this.formData,
              content,
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
            }
            riplanadd(info).then((res) => {
              if (res.code == 1) {
                this.$baseMessage(
                  '编辑成功',
                  'success',
                  'vab-hey-message-success'
                )
                this.$emit('fetchData')
                this.close()
              }
            })
          }
        })
      },
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      async showEdit(row, type) {
        this.loading = true
        this.dialogFormVisible = true
        this.alldisabled = false
        if (!row) {
          this.isAdd = 'add'
          this.title = '添加'
          // {
          //   tblName: 'TBL_RISK_ASSPLAN',
          //   column: 'PLANCODE',
          //   orgCol: 'UNIT',
          //   noId: 259,
          // }
          getGroupPlanCode().then((res) => {
            this.$set(this.formData, 'plancode', res.data)
          })
          riPlanAddPage().then((res) => {
            this.$set(this.formData, 'recorder', res.data.Staff.realname)
            this.$set(this.formData, 'orgname', res.data.orgByUser.orgname)
            this.$set(this.formData, 'unit', res.data.orgByUser.orgid)

            this.loading = false
          })
        } else {
          if (type == 'detail') {
            this.isAdd = 'detail'
            this.title = '详情'
            this.alldisabled = true
          } else {
            this.isAdd = 'edit'
            this.title = '编辑'
            this.alldisabled = false
          }
          await riplaninfo({ id: row.id }).then(async (res) => {
            console.log(res, 'aaa')

            this.$set(this, 'formData', {
              id: res.data.plan.id,
              plancode: res.data.plan.plancode,
              planName: res.data.plan.planName,
              startDate: res.data.plan.startDate,
              endDate: res.data.plan.endDate,
              assname: res.data.plan.assessmentstd.assname,
              planType: res.data.plan.planType,
              orgname: res.data.plan.organization.memo,
              plandes: res.data.plan.plandes,
              recorder: res.data.plan.recorder,
              assId: res.data.plan.assessmentstd.assstdid,
              assstdid: res.data.plan.assessmentstd.assstdid,
              status: res.data.plan.status,
              content: res.data.plan.content || '',
              unit: res.data.plan.unit || '',
              memo: res.data.plan.memo || '',
              recorddate: res.data.plan.recorddate || '',
              asshead: res.data.plan.asshead || '',
              version: res.data.plan.version || '',
              status: res.data.plan.status || '',
              secrectLevelId: res.data.plan.secrectLevelId || '',
              staffScopeIds: res.data.plan.staffScopeIds || '',
              staffScopeNames: res.data.plan.staffScopeNames || '',
              linkdeptid: res.data.plan.linkdeptid || '',
              createstaffid: res.data.plan.createstaffid || '',
              issueddate: res.data.plan.issueddate || '',
              issuedStaffid: res.data.plan.issuedStaffid || '',
              issuedStaffName: res.data.plan.issuedStaffName || '',
              createtime: res.data.plan.createtime || '',
              toIssued: res.data.plan.toIssued || '',
              lssuedUnit: res.data.plan.lssuedUnit || '',
              issuedUnitName: res.data.plan.issuedUnitName || '',
            })
            this.tableDataFile = res.data.plan.tblAttachments || []
            //这块是点击确定按钮，保存时候要传的reporteds
            this.fileIdList = []
            res.data.plan.tblAttachments.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
            //删除列表为空
            this.removeIds = []
            let list = res.data.plan.riskAssplanRiskList || []
            let tList = []
            list.map((item) => {
              if (item.risk) {
                tList.push(item)
              }
            })
            this.list = tList
            console.log(this.list, 'this.list')
            this.loading = false
          })
        }
      },
      /**
       * @description: 选择成功 回调
       * @return {*}
       */
      handelStandard(row) {
        this.$set(this.formData, 'assId', row.assstdid)
        this.$set(this.formData, 'assname', row.assname)
        this.$refs['elForm'].clearValidate()
      },
      /**
       * @description: 去重
       * @return {*}
       */
      uniqueFunc(arr, uniId) {
        const res = new Map()
        return arr.filter(
          (item) => !res.has(item[uniId]) && res.set(item[uniId], 1)
        )
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.list = []
        this.tableDataFile = []

        this.formData = {
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          planType: undefined,
          orgname: undefined,
          realname: undefined,
          plandes: undefined,
          content: undefined,
          id: 0,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
          assId: undefined,
          assname: undefined,
        }
        this.fileIdList = []
        this.removeIds = []
      },
      /**
       * @description: 下载
       * @return {*}
       */
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
       * @description: 删除附件
       * @return {*}
       */
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        })
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
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]
          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })
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
<style></style>
