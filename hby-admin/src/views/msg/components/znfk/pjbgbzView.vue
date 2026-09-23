<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
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
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告名称" prop="reportname">
            <el-input
              v-model="formData.reportname"
              clearable
              placeholder="请输入报告名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告时间" prop="reporttime">
            <el-date-picker
              v-model="formData.reporttime"
              placeholder="请输入报告时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告类型" prop="reporttype">
            <el-select
              v-model="formData.reporttype"
              clearable
              placeholder="请选择报告类型"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in reporttypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告方式" prop="reportmode">
            <el-select
              v-model="formData.reportmode"
              clearable
              placeholder="请选择报告方式"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in reportmodeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告部门" prop="reportdepartment">
            <el-input
              v-model="formData.reportdepartment"
              clearable
              placeholder="请输入报告部门"
              :style="{ width: '80%' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告人" prop="reporter">
            <el-input
              v-model="formData.reporter"
              clearable
              placeholder="请输入报告人"
              :style="{ width: '80%' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>评价缺陷</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="primary" @click="openFlawDialog">选择</el-button>
          </div>
          <el-table :data="flawList">
            <el-table-column
              align="center"
              label="缺陷编号"
              prop="bugnumber"
              width="170"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.bugnumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="缺陷名称"
              prop="defectsname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="缺陷描述"
              prop="bugdescripte"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="发生时间"
              prop="discovertime"
              show-overflow-tooltip
              :formatter="formatDate"
            />

            <el-table-column
              align="center"
              label="缺陷等级"
              prop="bugcrilevel"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="状态" prop="status">
              <template #default="{ row }">
                {{
                  row.status == 1
                    ? '审批中'
                    : row.status == 2
                    ? '已退回'
                    : row.status == 3
                    ? '已撤回'
                    : row.status == 4
                    ? '已终止'
                    : row.status == 5
                    ? '已跟踪'
                    : row.status == 6
                    ? '已完成'
                    : '未审批'
                }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button
                  :disabled="!footer"
                  type="text"
                  @click="removeFlaw(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="formData.repdesc"
            :height="300"
            :disabled="!footer"
            :templates="templates"
            template="PJBGBZ"
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
              :action="baseApi + (formData.reportid ? api1 : api)"
              :data="{ reportid: formData.reportid }"
              :headers="headers"
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
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
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
    <Flaw ref="flaw" @confirm="handleFlawConfirm" />
    <flaw-info ref="edit" />
  </div>
</template>

<script>
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import { formatDay } from '@/utils/index'
  import store from '@/store'
  import DepartmentOptions from '@/views/internal/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  const { baseURL } = require('@/config')
  import { download } from '@/api/internal/score'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import Flaw from '@/views/internal/report/components/Flaw.vue'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import { defectDetail } from '@/api/audit/question'
  import {
    spDelFj,
    modifyReport,
    isExistUpdate,
    isExistAdd,
    deleteFile,
  } from '@/api/internal/report'

  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import { getPrivewAttInfo } from '@/api/internal/project'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import ZXPerson from '@/components/selectPerson.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'IndexView',
    components: {
      Tinymce,
      DepartmentOptions,
      ExecutorOptions,
      UEditor,
      CandidateUserSelect,
      Resubmit,
      ZXPerson,
      Flaw,
      FlawInfo,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        // api: '/nkhg/nbkz/pjbg/addupload',
        // api1: '/nkhg/nbkz/pjbg/updateupload',
        // headers: {
        //   token: store.getters['user/token'],
        // },
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          reportid: '',
          reportname: undefined,
          reporttime: undefined,
          reporttype: undefined,
          reportmode: undefined,
          repdesc: undefined,
          reporter: undefined,
          reporterid: undefined,
          reportdepartment: undefined,
          reportdepartmentid: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请输入报告时间',
              trigger: 'blur',
            },
          ],
          reporttype: [
            {
              required: true,
              message: '请选择报告类型',
              trigger: 'blur',
            },
          ],
          reportmode: [
            {
              required: true,
              message: '请选择报告方式',
              trigger: 'blur',
            },
          ],
          repdesc: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
          reporter: [
            {
              required: true,
              message: '请输入报告人',
              trigger: 'blur',
            },
          ],
          reportdepartment: [
            {
              required: true,
              message: '请输入报告部门',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        footer: true,
        tableData: [],
        templates: [],
        reporttypeOptions: [
          {
            label: '对内报告',
            value: '对内报告',
          },
          {
            label: '对外报告',
            value: '对外报告',
          },
        ],
        reportmodeOptions: [
          {
            label: '定期报告',
            value: '定期报告',
          },
          {
            label: '非定期报告',
            value: '非定期报告',
          },
        ],
        //提交

        status: 0,
        jurisdictionCode: 0,
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
        flawList: [],
      }
    },
    watch: {
      'formData.repdesc'(val) {
        // console.log('formData.repdesc', val)
        // console.log('ueditor', this.$refs['ueditor'].editor.openTemplate)
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
          this.formData.repdesc = s
        }
      },
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
    methods: {
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
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      handleDepartmentSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })
        this.$set(this.formData, 'reportdepartmentid', node.id)
        this.$set(this.formData, 'reportdepartment', node.name)
        // this.formData.reportdepartmentid = node.id
        // this.formData.reportdepartment = node.name
      },
      handleExecutorSelected(node) {
        this.$nextTick(() => {
          this.$refs['ruleForm'].clearValidate()
        })

        this.$set(this.formData, 'reporter', node[0].realname)
        this.$set(this.formData, 'reporterid', node[0].staffid)
        // this.formData.reporter = node.realname
        // this.formData.reporterid = node.staffid
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      showEdit(
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
        this.getInfo(formId)
      },
      async getInfo(row) {
        const data = await modifyReport({ id: row })
        this.formData = data.data.report
        this.tableData = data.data.atts
        this.flawList = data.data.listPjqx
        if (data.data.report?.secrectLevelId) {
          localStorage.setItem(
            'SPsecrectLevelId',
            data.data.report.secrectLevelId
          )
        }
      },
      close() {
        this.title = ''
        this.formData = {}
        this.tableData = []
        this.flawList = []
        this.footer = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.$bus.$emit('updateMsg', 0)
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        if (!this.formData.reportid) {
          await deleteFile({ attid: row.attid })
          this.$message.success('删除成功')
        } else {
          await spDelFj({ attid: row.attid, reportid: this.formData.reportid })
          this.$message.success('删除成功')
        }
      },
      // handlePreview(file) {},
      // handleSuccess(file) {
      //   if (file.code == '200') {
      //     let list = this.tableData
      //     list.push(file.data.Attachment)
      //     this.tableData = list
      //     this.$baseMessage('上传成功', 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            if (this.formData.reportid) {
              let attids = ''
              this.tableData.map((item) => {
                attids += item.attid
                attids += ','
              })
              attids = attids.substring(0, attids.length - 1)
              let bugIds = ''
              this.flawList.map((item) => {
                bugIds += item.bugid
                bugIds += ','
              })
              const { reporttime, repdesc, ...other } = this.formData
              const params = {
                ...other,
                type: 'nk',
                startdate: formatDay(reporttime),
                attids,
                bugIds,
              }
              const body = {
                desc: repdesc,
              }
              const data = await isExistUpdate(params, body)
              if (data.code == 200) {
                this.$baseMessage('修改成功', 'success')
              } else {
                this.$baseMessage(data.msg, 'error')
              }
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
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
      //   console.log(12331212312)
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
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      /**
       * @description: 打开缺陷选择弹窗
       */
      openFlawDialog() {
        this.$refs.flaw.showDialog()
      },

      /**
       * @description: 处理缺陷选择确认
       * @param {Array} selectedFlaws 选中的缺陷列表
       */
      handleFlawConfirm(selectedFlaws) {
        // 如果当前列表为空，直接赋值
        if (!this.flawList || this.flawList.length === 0) {
          this.flawList = selectedFlaws
          return
        }
        // 遍历选中的缺陷
        selectedFlaws.forEach((selectedFlaw) => {
          // 查找当前列表中是否已存在该缺陷
          const existIndex = this.flawList.findIndex(
            (item) => item.bugid === selectedFlaw.bugid
          )
          if (existIndex !== -1) {
            // 如果已存在，替换
            this.$set(this.flawList, existIndex, selectedFlaw)
          } else {
            // 如果不存在，新增
            this.flawList.push(selectedFlaw)
          }
        })
      },
      /**
       * @description: 删除缺陷
       */
      removeFlaw(row) {
        this.$confirm('确认删除该缺陷?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.flawList = this.flawList.filter(
            (item) => item.bugid !== row.bugid
          )
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
        })
      },
      //查看详情
      async handleDetail(row) {
        const { data } = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('detail', data.bug)
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
