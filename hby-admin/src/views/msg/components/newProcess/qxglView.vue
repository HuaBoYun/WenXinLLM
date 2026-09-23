<template>
  <div>
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
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷类别" prop="defectcategory">
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
        <el-col :span="12" style="height: 48px">
          <el-form-item label="缺陷种类" prop="defecttype">
            <el-select
              v-model="formData.defecttype"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="财报缺陷" value="财报缺陷"></el-option>
              <el-option label="非财报缺陷" value="非财报缺陷"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否涉诉" prop="litigation" style="height: 30px">
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
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="closeAll"
      :status="status"
    />
  </div>
</template>

<script>
  import { deleteFile, download } from '@/api/audit/implement'
  import {
    bugcriidList,
    defectDetail,
    defectAdd,
    defectFileList,
  } from '@/api/audit/question'
  import store from '@/store'
  import { parseTime, formatDay } from '@/utils/index'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const { baseURL } = require('@/config')
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    name: 'FlawInfo',
    inheritAttrs: false,
    components: {
      ZXPerson,
      Resubmit,
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
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
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
              trigger: 'blur',
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
          bugdescripte: [],
          needreform: [],
        },
        dialogFormVisible: false,
        title: '新增',

        //提交
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
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
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
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
        nextNodeName,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        const {
          data: { bug },
        } = await defectDetail({ bugid: formId })
        await this.fectchBugcriidList(bug.defecttype)
        Object.keys(this.formData).forEach(
          (key) => (this.formData[key] = bug[key])
        )
        if (bug.secrectLevelId) {
          localStorage.setItem('SPsecrectLevelId', bug.secrectLevelId)
        }
        this.formData.bugid = formId
        this.formData.discovertime = formatDay(this.formData.discovertime)
        this.fetchFile(formId)
        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
          // this.formData.businessType = row.businessType
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
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
        this.$bus.$emit('updateMsg', 0)
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
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        }
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
      //提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
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
