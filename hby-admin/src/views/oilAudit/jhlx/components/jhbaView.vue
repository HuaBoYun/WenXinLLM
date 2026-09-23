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
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="planprojectname">
            <el-input
              v-model="formData.planprojectname"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位名称" prop="planauditunitname">
            <el-input
              v-model.trim="formData.planauditunitname"
              placeholder="请选择被审计单位名称"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              :disabled="!footer"
              @click="chooseUnit('planauditunitname')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施类型" prop="planimplementationtype">
            <el-select
              v-model="formData.planimplementationtype"
              placeholder="请选择"
              :disabled="!footer"
              style="width: 100%"
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
          <el-form-item label="实施审计机构" prop="planimplementationunitname">
            <el-input
              v-model.trim="formData.planimplementationunitname"
              placeholder="请选择"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              :disabled="!footer"
              @click="chooseUnit('planimplementationunitname')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 46px">
          <el-form-item label="项目负责处(科)室" prop="planprojectleadername">
            <el-input
              v-model="formData.planprojectleadername"
              clearable
              placeholder="请选择"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="openDep('planprojectleadername')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目类型" prop="planauditprojecttypename">
            <el-input
              v-model="formData.planauditprojecttypename"
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
          <el-form-item label="立项依据" prop="planprojectbasis">
            <el-select
              v-model="formData.planprojectbasis"
              clearable
              :disabled="!footer"
              :style="{ width: '100%' }"
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
          <el-form-item label="计划投入人日" prop="planinvestmentpersionnel">
            <el-input-number
              v-model="formData.planinvestmentpersionnel"
              :min="1"
              :max="999"
              :disabled="!footer"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划实施月份" prop="planimplementationmonth">
            <el-input-number
              v-model="formData.planimplementationmonth"
              :min="1"
              :max="12"
              :disabled="!footer"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人" prop="planpreparedbyname">
            <el-input
              v-model="formData.planpreparedbyname"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="plancreationtime">
            <el-input
              v-model="formData.plancreationtime"
              clearable
              placeholder="请输入"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="立项单位" prop="planprojectapprovalunitname">
            <el-input
              v-model.trim="formData.planprojectapprovalunitname"
              placeholder="请选择"
              :style="{ width: '100%' }"
              disabled
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 10px"
              :disabled="!footer"
              @click="chooseUnit('planprojectapprovalunitname')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目年度" prop="planprojectyear">
            <el-date-picker
              v-model="formData.planprojectyear"
              format="yyyy"
              value-format="yyyy"
              type="year"
              placeholder="选择"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="planprojecttype">
            <el-select
              v-model="formData.planprojecttype"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in planTypeoptions"
                :key="item.value"
                :label="item.label"
                :value="String(item.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="是否对被审计单位的全部经营活动进行审计"
            prop="planyesandnoaudit"
          >
            <el-select
              style="width: 100%"
              v-model="formData.planyesandnoaudit"
              placeholder="请选择"
              :disabled="!footer"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="String(item.value)"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计范围" prop="planauditscope">
            <el-input
              v-model="formData.planauditscope"
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
              <div v-if="!disabled" style="margin-right: 10px">
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
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId || btnLoading"
      >
        提交审批
      </el-button>
    </div>
    <ProcessList ref="process" @fetchData="close" />

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
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import departmentMultiple from '@/views/oilAudit/jhlx/components/departmentMultiple.vue'
  const { baseURL } = require('@/config')
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { toPreAdd, getDetailById } from '@/api/oilAudit/jhgl/jhcg'
  import { getPlanFilingSave, getPlanFilingDetail } from '@/oapi/audit/plan'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: {
      Tinymce,
      UEditor,
      SelectDepartment,
      DepartmentOptions,
      departmentMultiple,
      typeView,
      ProcessList,
    },
    data() {
      return {
        loading: false,
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
          planprojectname: '',
          planimplementationunitid: '',
          planauditunitname: '',
          planauditunitid: '',
          planimplementationtype: '',
          planimplementationunitname: '',
          planimplementationunitid: '',
          planprojectleadername: '',
          planprojectleaderid: '',
          planauditprojecttypename: '',
          planauditprojecttypeid: '',
          planprojectbasis: '',
          planinvestmentpersionnel: '',
          planimplementationmonth: '',
          planpreparedbyname: '',
          plancreationtime: '',
          planprojectapprovalunitname: '',
          planprojectapprovalunitid: '',
          planprojectyear: '',
          planprojecttype: '',
          planyesandnoaudit: '',
          planauditscope: '',
        },
        footer: true,
        listLoading: true,
        tableData: [],
        list: [],
        rules: {
          planprojectname: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planimplementationunitid: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planimplementationtype: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planimplementationunitname: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planprojectleadername: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planauditprojecttypename: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planprojectbasis: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planinvestmentpersionnel: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planimplementationmonth: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planpreparedbyname: [
            {
              required: true,
              message: '请输入',
            },
          ],
          createTime: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planprojectapprovalunitname: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planprojectyear: [
            {
              required: true,
              message: '请输入',
            },
          ],
          planprojecttype: [
            {
              required: true,
              message: '请输入',
              trigger: 'change',
            },
          ],
          planyesandnoaudit: [
            {
              required: true,
              message: '请输入',
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
        editId: '',
        btnLoading: false,
      }
    },
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

    methods: {
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
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
        this.$set(this.formData, this.depType, node.label)
        this.$set(this.formData, `planprojectleaderid`, node.id)
      },
      getDepartmentInfo(node) {
        this.$set(this.formData, this.deptType, node.label)
        if (this.deptType == 'planauditunitname') {
          this.$set(this.formData, 'planimplementationunitid', node.id)
        } else if (this.deptType == 'planprojectapprovalunitname') {
          this.$set(this.formData, 'planprojectapprovalunitid', node.id)
        }
      },
      getDepartmentInfoMultiple(node) {
        if (this.deptType == 'planauditunitname') {
          this.$set(
            this.formData,
            'planimplementationunitid',
            node
              .map((item) => {
                return item.id
              })
              .join(',')
          )
          this.$set(
            this.formData,
            'planauditunitname',
            node
              .map((item) => {
                return item.name
              })
              .join(',')
          )
        }
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.gettoPreAdd()
        if (title == 'edit') {
          this.getInfo(row)
          this.title = '编辑'
          this.footer = true
        } else if (title == 'detail') {
          this.getInfo(row)
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.footer = true
        }
      },
      async gettoPreAdd() {
        const res = await toPreAdd({})
        this.implTypeoptions = res.data.implTypeList.map((item) => {
          return {
            label: item,
            value: item,
          }
        })
        console.log(
          '🚀 ~ this.implTypeoptions=res.data.implTypeList.map ~   this.implTypeoptions:',
          this.implTypeoptions
        )
        this.formData.planpreparedbyid = res.data.createStaffId
        this.formData.planpreparedbyname = res.data.createStaffName
        this.formData.plancreationtime = res.data.createTime
        this.formData.planprojectapprovalunitid = res.data.projectInitUnit
        this.formData.planprojectapprovalunitname = res.data.projectInitUnitName
      },
      async getInfo(row) {
        this.editId = row.planfilingid
        const res = await getPlanFilingDetail({
          planfilingid: row.planfilingid,
        })
        this.formData = Object.assign(this.formData, res.data)
        this.formData.planprojectyear = res.data.planprojectyear + ''
        this.tableData = res.data.attachments
      },
      close() {
        this.formData = this.$options.data().formData
        this.$refs['ruleForm'].resetFields()
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            let params = { ...this.formData }
            delete params.attachments
            const data = await getPlanFilingSave({
              ...params,
              attIds,
            })
            if (data.code != 1) return
            this.$baseMessage('保存成功', 'success')
            this.editId = data.data.planfilingid
            this.loading = false
          } else {
            return false
          }
        })
      },
      // async handleDown(row) {
      //   if (!row.attid) return this.$message.error('请先上传文件')
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
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
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
        console.log('🚀 ~ setType ~ e:', e)
        this.formData.planauditprojecttypename = e.auditType
        this.formData.planauditprojecttypeid = e.typeId
        this.$forceUpdate()
      },
      handleApproval() {
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(187, this.editId)
        } catch (error) {
          this.btnLoading = false
        }
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
