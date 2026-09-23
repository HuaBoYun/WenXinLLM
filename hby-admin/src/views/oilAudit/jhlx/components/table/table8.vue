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
        ref="ruleForm"
        label-width="130px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="auditItemName">
            <el-input
              v-model="formData.auditItemName"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位名称" prop="auditOrgidStrs">
            <el-input
              v-model.trim="formData.auditOrgNameStrs"
              placeholder="请选择被审计单位名称"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              :disabled="!footer"
              @click="chooseUnit('auditOrgNameStrs')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施类型" prop="implType">
            <!-- <el-input
              v-model="formData.implType"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            /> -->
            <el-select
              v-model="formData.implType"
              placeholder="请选择"
              :disabled="!footer"
            >
              <el-option
                v-for="item in implTypeoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施审计机构" prop="implOrgName">
            <el-input
              v-model.trim="formData.implOrgName"
              placeholder="请选择"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              :disabled="!footer"
              @click="chooseUnit('implOrgName')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 46px">
          <el-form-item label="项目负责处(科)室" prop="itemDeptName">
            <el-input
              v-model="formData.itemDeptName"
              clearable
              placeholder="请选择"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="openDep('itemDeptName')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目类型" prop="auditItemType">
            <el-input
              v-model="formData.auditItemType"
              disabled
              clearable
              placeholder="请选择"
              :style="{ width: '75%' }"
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click.native="showtypeView"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="立项依据" prop="projectBasis">
            <el-select
              v-model="formData.projectBasis"
              clearable
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option label="相关部门委托" value="相关部门委托" />
              <el-option label="风险评估" value="风险评估" />
              <el-option label="制度规定" value="制度规定" />
              <el-option label="综合评定" value="综合评定" />
              <el-option label="总部下方权限" value="总部下方权限" />
              <el-option label="总部安排必审" value="总部安排必审" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划投入人日" prop="planPersonDays">
            <el-input-number
              v-model="formData.planPersonDays"
              :min="1"
              :max="999"
              :disabled="!footer"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划实施月份" prop="planImplMonth">
            <el-input-number
              v-model="formData.planImplMonth"
              :min="1"
              :max="12"
              :disabled="!footer"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="createStaffName">
            <el-input
              v-model="formData.createStaffName"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-input
              v-model="formData.createTime"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="立项单位" prop="projectInitUnitName">
            <el-input
              v-model="formData.projectInitUnitName"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目年度" prop="projectYear">
            <el-date-picker
              v-model="formData.projectYear"
              format="yyyy"
              value-format="yyyy"
              type="year"
              placeholder="选择"
              :disabled="!footer"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="planType">
            <el-select
              v-model="formData.planType"
              placeholder="请选择"
              :disabled="!footer"
            >
              <el-option
                v-for="item in planTypeoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="是否对被审计单位的全部经营活动进行审计"
            prop="isOrNotAll"
          >
            <el-select
              v-model="formData.isOrNotAll"
              placeholder="请选择"
              :disabled="!footer"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计范围" prop="auditScope">
            <el-input
              v-model="formData.auditScope"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
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
                <el-button type="text" @click="handleDowns(row)">下载</el-button>
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

    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <departmentMultiple
      ref="departmentMultiple"
      @submit="getDepartmentInfoMultiple"
    />

    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <typeView ref="typeView" @submit="setType" />
  </el-dialog>
</template>

<script>
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import SelectDepartment from '../department.vue'
  import departmentMultiple from '../departmentMultiple.vue'
  const { baseURL } = require('@/config')
  import { download, deleteFile } from '@/oapi/audit/implement'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import {
    toPreAdd,
    removeAttInfo,
    mengerEntity,
    getDetailById,
  } from '@/api/oilAudit/jhgl/jhcg'

  export default {
    name: 'table8',
    components: {
      Tinymce,
      UEditor,
      SelectDepartment,
      DepartmentOptions,
      departmentMultiple,
      typeView,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          auditItemName: undefined,
          auditOrgidStrs: undefined,
          auditOrgNameStrs: undefined,
          implType: undefined,
          implOrgName: undefined,
          itemDeptName: undefined,
          auditItemType: undefined,
          projectBasis: undefined,
          planPersonDays: undefined,
          planImplMonth: undefined,
          createStaffName: undefined,
          createTime: undefined,
          projectInitUnitName: undefined,
          projectYear: undefined,
          planType: undefined,
          isOrNotAll: undefined,
          createStaffId: undefined,
          projectInitUnit: undefined,
          auditOrgidStrs: undefined,
          implOrgId: undefined,
          itemDeptId: undefined,
          auditScope: undefined,
        },
        footer: true,
        listLoading: true,
        tableData: [],
        tableData2: [],
        templates: [],
        list: [],
        rules: {
          auditItemName: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          auditOrgidStrs: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          implType: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          implOrgName: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          itemDeptName: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          auditItemType: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          projectBasis: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          planPersonDays: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          planImplMonth: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          createStaffName: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          createTime: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          projectInitUnitName: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          projectYear: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          planType: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          isOrNotAll: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
        },
        options: [
          {
            value: 0,
            label: '是',
          },
          {
            value: 1,
            label: '否',
          },
        ],
        planTypeoptions: [
          {
            value: 1,
            label: '新增计划',
          },
          {
            value: 2,
            label: '境外项目',
          },
        ],
        implTypeoptions: [],
        dialogFormVisible: false,
        title: '新增',
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
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      openDep(type) {
        this.depType = type
        this.$refs.department.show()
      },
      chooseUnit(type) {
        this.deptType = type
        this.$refs.audiTree.showEdit()
      },
      chooseUnitMultiple(type) {
        this.deptType = type
        this.$refs.departmentMultiple.showEdit()
      },
      handleDepartmentSelected(node) {
        console.log('node', node)
        this.$set(this.formData, this.depType, node.label)
        if (this.depType == 'exePhraseUnit') {
          //保存名称和对应的ID
          this.$set(this.formData, `exePhraseUnitId`, node.id)
        } else if (this.depType == 'itemDeptName') {
          this.$set(this.formData, `itemDeptId`, node.id)
          this.$set(this.formData, `itemDeptName`, node.label)
        } else {
          this.$set(this.formData, `auditUnitId`, node.id)
        }
      },
      getDepartmentInfo(node) {
        this.$set(this.formData, this.deptType, node.label)
        if (this.deptType == 'lxdwmc') {
          this.$set(this.formData, 'lxdwid', node.id)
        } else if (this.deptType == 'bsjdwmc') {
          this.$set(this.formData, 'bsjdwid', node.id)
        } else if (this.deptType == 'sssjjgmc') {
          this.$set(this.formData, 'sssjjgid', node.id)
        } else if (this.deptType == 'auditOrgNameStrs') {
          this.$set(this.formData, 'auditOrgidStrs', node.id)
        } else if (this.deptType == 'implOrgName') {
          this.$set(this.formData, 'implOrgId', node.id)
        }
      },
      getDepartmentInfoMultiple(node) {
        console.log(node)
        if (this.deptType == 'auditOrgNameStrs') {
          this.$set(
            this.formData,
            'auditOrgidStrs',
            node
              .map((item) => {
                return item.id
              })
              .join(',')
          )
          this.$set(
            this.formData,
            'auditOrgNameStrs',
            node
              .map((item) => {
                return item.name
              })
              .join(',')
          )
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.gettoPreAdd()
        if (row) {
          this.getInfo(row)
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.footer = true
        }
      },
      async getInfo(row) {
        const res = await getDetailById({ auditId: row.auditId })
        this.formData = Object.assign({}, res.data)
        this.formData.projectYear = res.data.projectYear + ''
        this.tableData = res.data.attachments
        this.tableData2 = JSON.parse(JSON.stringify(res.data.attachments || []))
        // this.getFileList(row.auditId)
      },
      async gettoPreAdd() {
        const res = await toPreAdd({})
        this.implTypeoptions = res.data.implTypeList.map((item) => {
          return {
            label: item,
            value: item,
          }
        })

        this.formData.createStaffId = res.data.createStaffId
        this.formData.createStaffName = res.data.createStaffName
        this.formData.createTime = res.data.createTime
        this.formData.projectInitUnit = res.data.projectInitUnit
        this.formData.projectInitUnitName = res.data.projectInitUnitName
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.tableData2 = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            const tableData = this.tableData.filter((item) =>
              this.tableData2.every((subItem) => subItem.attid !== item.attid)
            )

            attids = attids.substring(0, attids.length - 1)
            const { creatrTime, attachments, ...other } = this.formData
            const data = await mengerEntity({
              ...other,
              attIds: tableData
                .map((item) => {
                  return item.attid
                })
                .join(','),
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            if (this.formData.auditId) {
            } else {
              this.$emit('fetch', {
                ...this.formData,
                auditId: data.data.auditId,
              })
            }
            this.close()
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
        await removeAttInfo({ attId: row.attid })
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      showtypeView() {
        this.$refs['typeView'].showEdit()
      },
      setType(e) {
        this.formData.auditItemType = e.auditType
        this.$forceUpdate()
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
