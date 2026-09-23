<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="$translateTitle(title)"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="allDisabled"
      >
        <el-col :span="12">
          <el-form-item :label="$translateTitle('手册名称')" prop="manualName">
            <el-input
              v-model="formData.manualName"
              clearable
              placeholder="请输入手册名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$translateTitle('版本号')" prop="versionNumber">
            <el-input
              v-model="formData.versionNumber"
              clearable
              placeholder="请输入版本号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="$translateTitle('经办人')"
            prop="transactorName"
          >
            <el-input
              v-model.trim="formData.transactorName"
              placeholder="请选部经办人"
              style="width: 75%"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
            >
              {{ $translateTitle('选择') }}
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="$translateTitle('部门负责人')"
            prop="departmentHeadName"
          >
            <el-input
              v-model.trim="formData.departmentHeadName"
              placeholder="请选部门负责人"
              style="width: 75%"
              disabled
            />
            <el-button
              @click="projectManager1"
              style="margin-left: 10px"
              type="primary"
            >
              {{ $translateTitle('选择') }}
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="formData.content"
            :height="300"
            :templates="templates"
            template="hgscgl"
            v-if="dialogFormVisible"
            :disabled="allDisabled"
          />
        </el-col>

        <el-col :span="24">
          <el-divider>{{ $translateTitle('文件上传') }}</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!allDisabled"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">
                {{ $translateTitle('上传') }}
              </el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column
              align="center"
              :label="$translateTitle('附件名称')"
              prop="fileName"
            />
            <el-table-column
              align="center"
              :label="$translateTitle('文件大小') + '(KB)'"
              prop="fileSize"
            />
            <el-table-column
              align="center"
              :label="$translateTitle('创建人')"
              prop="uploader"
            />
            <el-table-column
              align="center"
              :label="$translateTitle('操作')"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <!-- <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
                <el-button
                  type="text"
                  @click="handleDownload(row)"
                  :disabled="false"
                >
                  {{ $translateTitle('下载') }}
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  :disabled="false"
                  v-if="!allDisabled"
                >
                  {{ $translateTitle('删除') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <project-manage1
      @projectManage="getChildlistPro1"
      ref="manage1"
    ></project-manage1>

    <template #footer v-if="title != '详情'">
      <el-button @click="close">{{ $translateTitle('关闭') }}</el-button>
      <el-button type="primary" @click="add">
        {{ $translateTitle('确定') }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { download } from '@/api/hggl/hgjhgl'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import {
    editSCGL,
    getSCGLDefaultInfo,
    deleteFile,
    getOrgMain,
  } from '@/api/hggl/hgjhgl'
  import projectManage from './selectPerson.vue'
  import projectManage1 from './selectPerson.vue'
  export default {
    name: '',
    components: { projectManage, projectManage1, UEditor },

    data() {
      return {
        baseApi: baseURL,
        api: '/hggl/api-auth/fileManage/upload',
        headers: { token: token },
        loading: false,
        formData: {
          departmentHead: '',
          departmentHeadName: '',
          id: '',
          manualName: '',
          transactor: '',
          transactorName: '',
          versionNumber: '',
        },
        footer: true,
        templates: [],
        rules: {
          teamName: [
            {
              required: true,
              message: '请输入团队名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        fileList: [],
        allDisabled: false,
      }
    },
    computed: {},
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
    created() {},
    mounted() {},
    methods: {
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.title = title
        this.allDisabled = title == '详情'
        this.fileList = []

        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.transactorName = userInfo.realname
        this.formData.transactor = userInfo.staffid
        //部门负责人等接口
        const org = { orgid: userInfo.linkDetp.orgid }
        const res = await getOrgMain(org)
        console.log(res, 'res')
        this.formData.departmentHeadName = res.data.realname
        this.formData.departmentHead = res.data.staffid
        if (row) {
          getSCGLDefaultInfo({
            id: row.id,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.manualMgt)
            if (res.data.file) {
              this.fileList = res.data.file
            }
          })
        }
      },

      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      getChildlistPro(val) {
        const names = val.map((res) => res.realname).toString()
        const ids = val.map((res) => res.staffid).toString()

        this.$set(this.formData, 'transactorName', names)
        this.$set(this.formData, 'transactor', ids)
      },
      getChildlistPro1(val) {
        const names = val.map((res) => res.realname).toString()
        const ids = val.map((res) => res.staffid).toString()
        this.$set(this.formData, 'departmentHeadName', names)
        this.$set(this.formData, 'departmentHead', ids)
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      projectManager1() {
        this.$refs['manage1'].showEdit()
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            editSCGL({
              ...this.formData,
              fileIds: this.fileList.map((x) => x.fileId).join(','),
            }).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false
                this.$baseMessage('成功', 'success')
                this.$emit('fetchData')
              }
            })
          } else {
            return false
          }
        })
      },
      async handleDownload(row) {
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
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.$message.success(res.msg)
          this.fileList.push(res.data.fileIds[0])
        } else {
          this.$message.error(res.msg)
        }
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
