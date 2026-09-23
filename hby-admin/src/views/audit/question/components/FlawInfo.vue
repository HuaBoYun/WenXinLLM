<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="closeAll"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="120px"
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
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
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
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="缺陷编号" prop="bugnumber">
            <el-input
              v-model="formData.bugnumber"
              clearable
              placeholder="请输入缺陷编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷名称" prop="defectsname">
            <el-input
              placeholder="请输入缺陷名称"
              v-model.trim="formData.defectsname"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷种类" prop="defecttype">
            <el-select
              v-model="formData.defecttype"
              style="width: 100%"
              :disabled="!footer"
              @change="handleDefectTypeChange"
            >
              <el-option label="财报缺陷" value="财报缺陷"></el-option>
              <el-option label="非财报缺陷" value="非财报缺陷"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷等级" prop="bugcriid">
            <el-select
              v-model="formData.bugcriid"
              placeholder="请输入缺陷等级"
              :disabled="!footer"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in bugcriidList"
                :label="item.bugcrilevel"
                :value="item.bugcriid"
                :key="item.bugcriid"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发生时间" prop="discovertime">
            <el-date-picker
              v-model="formData.discovertime"
              placeholder="请输入发生时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及金额(万元)" prop="amount">
            <el-input
              v-model.trim="formData.amount"
              :disabled="!footer"
              @input="numChange"
              placeholder="请输入涉及金额"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="缺陷类别"
            prop="defectcategory"
            style="height: 28px"
          >
            <el-select
              v-model="formData.defectcategory"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option
                label="内控体系设计缺陷"
                value="内控体系设计缺陷"
              ></el-option>
              <el-option label="内控制度缺陷" value="内控制度缺陷"></el-option>
              <el-option label="内控执行缺陷" value="内控执行缺陷"></el-option>
              <el-option label="内控监督缺陷" value="内控监督缺陷"></el-option>
              <el-option
                label="重大风险防控缺陷"
                value="重大风险防控缺陷"
              ></el-option>
              <el-option label="其它内控缺陷" value="其它内控缺陷"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否涉诉" prop="litigation" style="height: 28px">
            <el-radio-group v-model="formData.litigation" :disabled="!footer">
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否境外" prop="overseas">
            <el-radio-group v-model="formData.overseas" :disabled="!footer">
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="原因分析" prop="causeanalysis">
            <el-input
              v-model.trim="formData.causeanalysis"
              :disabled="!footer"
              placeholder="请输入原因分析"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="发现人" prop="discoverperson">
            <el-input
              v-model="formData.discoverperson"
              clearable
              placeholder="请输入发现人"
              :style="{ width: '305px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.showEdit()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="是否财务相关" prop="bugsource">
            <el-radio-group v-model="formData.bugsource" :disabled="!footer">
              <el-radio label="是" value="是" />
              <el-radio label="否" value="否" />
            </el-radio-group>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="是否需要整改" prop="needreform">
            <el-radio-group v-model="formData.needreform" :disabled="!footer">
              <el-radio label="是" value="是" />
              <el-radio label="否" value="否" />
            </el-radio-group>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="缺陷性质" prop="bugproperty">
            <el-select
              v-model="formData.bugproperty"
              placeholder="请输入缺陷性质"
              :disabled="!footer"
              :style="{ width: '100%' }"
            >
              <el-option label="执行缺陷" value="执行缺陷" />
              <el-option label="设计缺陷" value="设计缺陷" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="缺陷部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入缺陷部门"
              :style="{ width: '305px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="业务单元" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              placeholder="请输入业务单元"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="业务描述" prop="businessDescription">
            <el-input
              v-model="formData.businessDescription"
              clearable
              type="textarea"
              :rows="2"
              placeholder="请输入业务描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24" v-if="formData.needreform == '否'">
          <el-form-item label="不整改原因" prop="resonfornoreform">
            <el-input
              v-model="formData.resonfornoreform"
              clearable
              placeholder="请输入不整改原因"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="缺陷描述及依据" prop="bugdescripte">
            <el-input
              v-model="formData.bugdescripte"
              clearable
              type="textarea"
              :rows="4"
              placeholder="请输入缺陷描述及依据"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <template>
                <i class="el-icon-search"></i>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :before-upload="handleBeforeUpload"
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
    <template #footer v-if="footer">
      <el-button @click="closeAll">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { deleteFile, download } from '@/api/audit/implement'
  import {
    bugcriidList,
    createFlawCode,
    defectAdd,
    defectFileList,
  } from '@/api/audit/question'
  import store from '@/store'
  import { parseTime, formatDay } from '@/utils/index'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: { ZXPerson },
    inheritAttrs: false,
    props: {
      scene: {
        type: String,
        default: 'default', // 可选值: default, draft 我的底稿传入的是draft
      },
    },
    data() {
      return {
        bugcriidList: [],

        baseApi: baseURL,
        // api: '/audit/fileManage/upload',
        // headers: {
        //   token: store.getters['user/token'],
        // },
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          bugnumber: undefined,
          defectsname: undefined,
          discovertime: undefined,
          bugcriid: undefined,
          bugcrilevel: undefined,
          amount: undefined,
          defectcategory: undefined,
          defecttype: undefined,
          litigation: undefined,
          overseas: undefined,
          causeanalysis: undefined,
          bugdescripte: undefined,
          bugid: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        footer: true,
        tableData: [],
        rules: {
          bugnumber: [
            {
              required: true,
              message: '请输入缺陷编号',
              trigger: 'blur',
            },
          ],
          bugcriid: [
            {
              required: true,
              message: '请选择缺陷等级',
              trigger: 'change',
            },
          ],
          discovertime: [
            {
              required: true,
              message: '请输入发生时间',
              trigger: 'blur',
            },
          ],
          discoverperson: [
            {
              required: true,
              message: '请输入发现人',
              trigger: 'blur',
            },
          ],
          bugsource: [
            {
              required: false,
              message: '是否财务相关',
              trigger: 'blur',
            },
          ],
          bugproperty: [
            {
              required: true,
              message: '请输入缺陷性质',
              trigger: 'blur',
            },
          ],
          defecttype: [
            {
              required: true,
              message: '请选择缺陷种类',
              trigger: 'change',
            },
          ],
          bugdescripte: [],
          needreform: [],
        },
        dialogFormVisible: false,
        title: '新增',
        MJoption: [],
        showMJ: false,
      }
    },

    async created() {
      this.showMJ = couldMJ()
      console.log('🚀 ~ created ~ this.showMJ:', this.showMJ)
      if (this.showMJ) {
        // 获取密级,菜单id
        // const res = await hasMJ('QuestionFlaw')
        // console.log('🚀 ~ created ~ res:', res)
        // this.menuId = res[0].menuid
        // 请求密级下拉数据
        // const res2 = await getMJ({ rightId: res[0].menuid })
        const res2 = await getMJ({ rightId: 800095 })
        console.log('🚀 ~ created ~ res2:', res2)
        this.MJoption = res2.data
        console.log('QuestionFlaw', this.MJoption)
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
    methods: {
      changeMJ(selectedValue) {
        // 切换密级时清空知悉范围
        this.formData.staffScopeIds = ''
        this.formData.staffScopeNames = ''
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = undefined
            this.formData.staffScopeIds = undefined
          }
        }
        // 切换密级时清空附件列表
        this.tableData = []
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      //选择缺陷种类
      handleDefectTypeChange(val) {
        this.formData.bugcriid = ''
        this.fectchBugcriidList(val)
      },
      numChange(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        if (!regex.test(event)) {
          this.formData.num = this.formData.num
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      async fetchFile(bugid) {
        const res = await defectFileList({
          bugId: bugid,
        })
        this.tableData = res.data.data
      },
      async fectchBugcriidList(type) {
        const res = await bugcriidList({ bugtype: type })
        this.bugcriidList = res.data.list
      },
      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },

      async showEdit(title, row, info, data, hamId) {
        // this.fectchBugcriidList()
        //从疑点管理过来时，data就会有值，用于回填附件信息
        if (data) {
          let list = []
          list.push(data.attachment)
          this.tableData = list
        }

        //我的底稿-新建跳转专用
        if (hamId) {
          if (this.showMJ && this.MJoption.length == 0) {
            // 获取密级,菜单id
            const res = await hasMJ(hamId)
            this.menuId = res[0].menuid
            // 请求密级下拉数据
            const res2 = await getMJ({ rightId: res[0].menuid })
            console.log('🚀 ~ showEdit ~ res3:', res2)
            this.MJoption = res2.data
          }
        }

        this.dialogFormVisible = true
        if (row) {
          console.log('🚀 ~ showEdit ~ row:', row)
          // this.formData = row
          Object.keys(this.formData).forEach(
            (key) => (this.formData[key] = row[key])
          )
          this.formData.bugid = row.bugid
          this.formData.secrectLevelId = row.secrectLevelId
          this.formData.staffScopeNames = row.staffScopeNames
          this.formData.staffScopeIds = row.staffScopeIds
          this.formData.discovertime = formatDay(this.formData.discovertime)
          this.fetchFile(row.bugid)
          this.fectchBugcriidList(row.defecttype)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.bugdepartment = userInfo.linkDetp.orgid
          this.formData.discoverperson = userInfo.realname
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
          // this.formData.businessType = row.businessType
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          createFlawCode().then((res) => {
            this.$set(this.formData, 'bugnumber', res.data.autoCode.toString())
          })
        }
      },
      closeTop() {
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      closeAll() {
        this.reset()
        this.closeTop()
      },
      reset() {
        this.formData = {
          bugnumber: undefined,
          defectsname: undefined,
          discovertime: undefined,
          bugcriid: undefined,
          bugcrilevel: undefined,
          amount: undefined,
          defectcategory: undefined,
          defecttype: undefined,
          litigation: undefined,
          overseas: undefined,
          causeanalysis: undefined,
          bugdescripte: undefined,
          bugid: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.bugcriidList = []
        this.$refs['ruleForm']?.resetFields()
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const info = this.bugcriidList.filter(
              (res) => res.bugcriid == this.formData.bugcriid
            )
            this.formData.bugcrilevel = info[0].bugcrilevel
            const data = await defectAdd({
              ...this.formData,
              attids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              console.log(
                "🚀 ~ this.$refs['ruleForm'].validate ~ this.scene:",
                this.scene
              )
              if (this.scene == 'draft') {
                console.log("🚀 ~ this.$refs['ruleForm'].validate ~ draft:")
                this.$emit('bug-data', data.data.WorkReport)
              }
              this.closeAll()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
        this.$message.success('删除成功')
      },
      // async handlePreview(row) {
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
        console.log('🚀 ~ row:', row)
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
