<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :modal="false"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="form"
        label-width="120px"
        :model="form"
        :rules="rules"
        :validate-on-rule-change="false"
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
        <el-col :span="12">
          <el-form-item label="疑似问题" prop="suspectedIssue">
            <el-input v-model.trim="form.suspectedIssue" :disabled="footer" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题类型" prop="questionType">
            <el-input
              v-model.trim="form.questionType"
              placeholder="请输入问题类型"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现时间" prop="discoverTime">
            <el-date-picker
              v-model="form.discoverTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择发现时间"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务领域" prop="businessArea">
            <el-input
              v-model.trim="form.businessArea"
              placeholder="请输入业务领域"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="确认情况" prop="isConfirm">
            <el-select
              v-model="form.isConfirm"
              clearable
              placeholder="请选择确认情况"
              :style="{ width: '100%' }"
              :disabled="footer"
            >
              <el-option label="已确认" :value="1" />
              <el-option label="未确认" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="待整改问题" prop="isRectification">
            <el-select
              v-model="form.isRectification"
              clearable
              placeholder="请选择待整改问题"
              :style="{ width: '100%' }"
              :disabled="footer"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="描述" prop="describe">
            <el-input
              v-model.trim="form.describe"
              placeholder="请输入描述"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creatorName">
            <el-input
              v-model.trim="form.creatorName"
              placeholder="请输入创建人"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createdTime">
            <el-date-picker
              v-model="form.createdTime"
              :style="{ width: '100%' }"
              type="date"
              disabled
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <!-- <tinymce
              v-model="formData.content"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="form.content"
              :height="300"
              :templates="templates"
              template="lcjy"
              :disabled="footer"
              v-if="dialogFormVisible"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>底稿上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <!-- <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!footer"
                >
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
</template>

<script>
  // import { doEdit } from '@/api/table'
  import {
    findAutoNumber,
    getImpInfo,
    impSaveOrUpdate,
  } from '@/api/internal/new/plan'
  import { deleteFile, download } from '@/api/hggl/hgjhgl'
  import { getDefaultRenderData } from '@/api/internal/project'
  import TestTemplate from './options/TestTemplate.vue'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
  import DepartmentTreeModal from '@/components/DepartmentTreeModal'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CustormForm from '@/components/customForm/index.vue'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    name: 'PlanView',
    components: {
      TestTemplate,
      CompanyTreeModal,
      DepartmentTreeModal,
      CompanySelectUserByTree,
      CustormForm,
      UEditor,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/hggl/api-auth/fileManage/upload',
        headers: { token: token },
        form: {},
        rules: {
          suspectedIssue: [
            {
              required: true,
              message: '请输入可疑问题',
              trigger: 'blur',
            },
          ],
          questionType: [
            {
              required: true,
              message: '请选择问题类型',
              trigger: 'change',
            },
          ],
          discoverTime: [
            {
              required: true,
              message: '请选择发现时间',
              trigger: 'change',
            },
          ],
          businessArea: [
            {
              required: true,
              message: '请选择业务区域',
              trigger: 'change',
            },
          ],
          // isConfirm: [
          //   {
          //     required: true,
          //     message: '请选择是否确认',
          //     trigger: 'change',
          //   },
          // ],
          // isRectification: [
          //   {
          //     required: true,
          //     message: '请选择是否整改',
          //     trigger: 'change',
          //   },
          // ],
        },

        title: '',
        dialogFormVisible: false,
        renderData: [],
        tableData: [],
        templates: [],
        footer: false,
        fileList: [],
      }
    },
    created() {},
    watch: {
      'form.content'(val) {
        // console.log('formData.describe', val)
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
            if (this.form[i[1]]) {
              s = s.replace(i[0], this.form[i[1]])
            }
          })
          this.form.content = s
        }
      },
    },
    methods: {
      showEdit(row, title) {
        if (title == 'add') {
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.creatorName = userInfo.realname
          this.form.createdTime = new Date()
          this.form.isConfirm = 0
          // this.getNumber()
        } else {
          this.getInfo(row)

          if (title == 'edit') {
            this.form.isConfirm = 0
            this.title = '编辑'
          } else {
            this.title = '查看'
            this.footer = true
          }
        }
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        const { data, code, msg } = await getImpInfo({
          id: row.id,
        })
        if (code === 1) {
          this.form = {
            ...data.data,
          }
          this.fileList = data.file || []
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.footer = false
        this.fileList = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { createdTime, ...other } = this.form
            const aa = []
            this.fileList.forEach((e) => {
              aa.push(e.fileId)
            })
            const { msg, code, data } = await impSaveOrUpdate({
              ...other,
              fileIds: aa.toString(),
            })
            if (code == 1) {
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
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
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
