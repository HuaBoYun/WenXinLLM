<template>
  <div>
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="dialogFormVisible" width="1000px"
      @close="close">
      <el-row :gutter="15">
        <el-form ref="form" label-width="140px" :model="form" :rules="rules" size="medium" :disabled="footer">
          <el-col :span="24">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件编号" prop="riskNumber">
              <el-input v-model="form.riskNumber" placeholder="请输入事件编号" :style="{ width: '100%' }" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="事件名称" prop="riskName">
              <el-input v-model="form.riskName" clearable placeholder="请输入事件名称" :style="{ width: '100%' }" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报送部门" prop="departmentName">
              <!-- <el-input
                      v-model="form.departmentname"
                      clearable
                      placeholder="请输入报送部门"
                      :style="{ width: '100%' }"
                    /> -->
              <el-input v-model="form.departmentName" readonly style="width: 75%; margin-right: 8px"></el-input>
              <el-button type="primary" @click="handleShowCompent('zr')">
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报送日期" prop="findTime">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.findTime" clearable
                placeholder="请选择报送日期" :style="{ width: '100%' }" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现日期" prop="discoveryTime">
              <el-date-picker format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="form.discoveryTime" clearable
                placeholder="请输入报送日期" :style="{ width: '100%' }" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险事件类别" prop="riskType">
              <el-select v-model="form.riskType" clearable placeholder="请选择风险事件类别" :style="{ width: '100%' }">
                <el-option v-for="(item, index) in field106Options" :key="index" :disabled="item.disabled"
                  :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经办人" prop="creatorName">
              <el-input v-model="form.creatorName" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经办部门" prop="workUnitName">
              <el-input v-model="form.workUnitName" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="事件说明" prop="remark">
              <el-input v-model="form.remark" clearable placeholder="请输入事件说明" :style="{ width: '100%' }"
                type="textarea" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
              <el-upload class="upload-demo" :show-file-list="false" :action="baseApi + api" :headers="headers"
                :on-success="handleAvatarSuccess">
                <el-button type="success">上传</el-button>
              </el-upload>
            </div>
            <el-table :data="fileList">
              <el-table-column align="center" label="附件名称" prop="fileName" />
              <el-table-column align="center" label="文件大小(KB)" prop="fileSize" />
              <el-table-column align="center" label="创建人" prop="uploader" />
              <el-table-column align="center" label="操作" show-overflow-tooltip width="120">
                <template #default="{ row }">
                  <!-- <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
                  <el-button type="text" @click="handleDown(row)">
                    下载
                  </el-button>
                  <el-button type="text" @click="handleDelete(row)" v-if="!footer">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer v-if="!footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
  </div>
</template>
<script>
import UEditor from '@/components/UEditor'
import { deleteFile, download } from '@/api/hggl/hgjhgl'
import CompanyTreeModel from '@/components/CompanyTreeModel'
import dayjs from 'dayjs'
import {
  getFlowTaskInfo,
  ymWorkCandidates,
  ymWorkSubmit,
} from '@/api/contract/manage'
import { riskSaveOrUpdate,getRiskInfo } from '@/api/internal/new/plan'
import CandidateUserSelect from '@/components/CandidateUserSelect'
import store from '@/store'
import { baseURL } from '@/config'
const token = store.getters['user/token']

export default {
  name: 'EventEdit',
  components: {
    CompanyTreeModel,
    CandidateUserSelect,
    UEditor,
  },
  inheritAttrs: false,
  data() {
    return {
      baseApi: baseURL,
      api: '/hggl/api-auth/fileManage/upload',
      headers: { token: token },
      tableDataFile: [],
      fileIdList: [],
      templates: [],
      dayjs: dayjs,
      activeName: 'first',
      title: '',
      dialogFormVisible: false,
      form: {},
      list: [{ name: 'XXXXX' }],
      tableData: [{ name: 'XXXXX' }],
      rules: {
        riskNumber: [
          {
            required: true,
            message: '请输入事件编号',
            trigger: 'blur',
          },
        ],
        riskName: [
          {
            required: true,
            message: '请输入事件名称',
            trigger: 'blur',
          },
        ],
        departmentName: [
          {
            required: true,
            message: '请输入报送部门',
            trigger: 'blur',
          },
        ],
        findTime: [
          {
            required: true,
            message: '请选择报送日期',
            trigger: 'change',
          },
        ],
        discoveryTime: [
          {
            required: true,
            message: '请选择发现日期',
            trigger: 'change',
          },
        ],
        riskType: [
          {
            required: true,
            message: '请选择损失事件定义类别',
            trigger: 'change',
          },
        ],
      },
      fileList: [],
      footer: false,
      field106Options: [
        {
          label: '重大',
          value: 1,
        },
        {
          label: '非重大',
          value: 2,
        },
      ],
      bmType: 'zr',
      saveLoading: false,
      showRow: {},
      //提交

      status: 0,
      jurisdictionCode: 0,
    }
  },
  computed: {},
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
  created() { },
  mounted() { },
  methods: {
    showEdit(row, title) {
      if (title == 'add') {
        this.title = '添加'
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.form.creatorName = userInfo.realname
        this.form.workUnitName = userInfo.linkOrg.orgname
        this.form.createdTime = new Date()
        // this.getNumber()
      } else {
        this.getInfo(row)

        if (title == 'edit') {
          this.title = '编辑'
          this.footer = false
        } else {
          this.title = '查看'
          this.footer = true
        }
      }
      this.dialogFormVisible = true
    },
    async getInfo(row) {
      const { data, code, msg } = await getRiskInfo({
        id: row.id,
      })
      console.log('data', data)
      if (code === 200) {
        this.form = {
          ...data.data,
        }
        this.fileList = data.file || []
        this.$forceUpdate()
      }
    },
    close() {
      this.$refs['form'].resetFields()
      this.form = this.$options.data().form
      this.footer = false
      this.fileList = []
      this.dialogFormVisible = false
    },
    handleShowCompent(type) {
      this.bmType = type
      let checkbox = false
      if (this.bmType === 'xg') {
        checkbox = true
      }
      this.$refs['comTreeRef'].show(checkbox)
    },
    handleSelectCompany(e) {
      console.log("ssssssssssssssssssssssssssssss",e)
      this.$set(this.form, 'departmentName', e.name)
      this.$set(this.form, 'department', e.id)

    },
    save() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          const { createdTime, ...other } = this.form
          const aa = []
          this.fileList.forEach((e) => {
            aa.push(e.fileId)
          })
          const { msg, code, data } = await riskSaveOrUpdate({
            ...other,
            fileIds: aa.toString(),
          })
          if (code == 200) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          } else {
            // this.$baseMessage(data, 'error', 'vab-hey-message-error')
          }
        }
      })
    },
    async getNumber() {
      const { msg, code, data } = await findAutoNumber({
        column: 'PLANNUMBER',
        noId: '282',
        orgCol: 'ORGID',
        tblName: 'TBL_COM_EXT_TESTPLAN',
      })
      if (code == 1) {
        this.dialogFormVisible = true
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        // console.log(userInfo, 'userInfo')
        this.form = {
          oid2: userInfo.linkDetp.orgid,
          org2: userInfo.linkDetp.orgname,
          plannumber: data,
          person: userInfo.realname,
        }
      } else {
        this.$baseMessage(msg, 'error')
      }
    },
    handleAvatarSuccess(res) {
      if (res.code == 200) {
        this.fileList.push(res.data.fileIds[0])
      }
    },
    async handleDown(row) {
      const data = await download({ fileId: row.fileId })
      let filename = row.fileName
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
    handleDelete(row) {
      this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        const { msg, code } = await deleteFile({ id: row.fileId })
        if (code == 200) {
          this.fileList.splice(
            this.fileList.findIndex((x) => x.fileId == row.fileId),
            1
          )
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        }
      })
    },
  },
}
</script>
<style></style>
