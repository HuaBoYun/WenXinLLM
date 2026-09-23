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
          <el-form-item :label="$translateTitle('部门')" prop="departmentName">
            <el-input
              v-model.trim="formData.departmentName"
              placeholder="请选择部门"
              style="width: 75%"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$translateTitle('岗位名称')" prop="postName">
            <el-input
              v-model="formData.postName"
              clearable
              placeholder="请输入岗位名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            :label="$translateTitle('合规职责')"
            prop="complianceTdr"
          >
            <el-input
              v-model="formData.complianceTdr"
              clearable
              type="textarea"
              :rows="2"
              placeholder="请输入合规职责"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$translateTitle('备注')" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              type="textarea"
              :rows="2"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>
      <!-- <el-col :span="24">
        <UEditor
          ref="ueditor"
          v-model="formData.content"
          :height="300"
          :templates="templates"
          template="nbsj"
        />
      </el-col> -->
      <el-col :span="24">
        <el-divider>{{ $translateTitle('文件上传') }}</el-divider>
      </el-col>
      <el-col :span="24">
        <div
          style="text-align: right; margin-bottom: 5px"
          v-if="title != '详情'"
        >
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleAvatarSuccess"
          >
            <div style="margin-right: 10px">
              <el-button type="success">
                {{ $translateTitle('上传') }}
              </el-button>
            </div>
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
              <!-- <el-button type="text" :disabled="false" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
              <el-button type="text" @click="handleDownload(row)">
                {{ $translateTitle('下载') }}
              </el-button>
              <el-button
                type="text"
                @click="handleDeleteAttach(row)"
                v-if="title != '详情'"
              >
                {{ $translateTitle('删除') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <template #footer v-if="title != '详情'">
      <el-button @click="close">{{ $translateTitle('关闭') }}</el-button>
      <el-button type="primary" @click="add">
        {{ $translateTitle('确定') }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import UEditor from '@/components/UEditor'
  import { editHGZR, deleteFile, getHGZRDefaultInfo } from '@/api/hggl/hgjhgl'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import store from '@/store'
  import { baseURL } from '@/config'
  import { download } from '@/api/hggl/hgjhgl'
  const token = store.getters['user/token']
  export default {
    name: '',
    components: { DepartmentOptions, UEditor },

    data() {
      return {
        baseApi: baseURL,
        api: '/hggl/api-auth/fileManage/upload',
        headers: { token: token },
        loading: false,
        templates: [],
        formData: {
          complianceTdr: '',
          department: '',
          departmentName: '',
          id: '',
          postName: '',
          remark: '',
        },
        footer: true,
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
        tableData: [],
        allDisabled: false,
        fileList: [],
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

        if (row) {
          getHGZRDefaultInfo({
            id: row.id,
          }).then((res) => {
            this.formData = Object.assign(this.formData, res.data.dty)
            this.fileList = res.data.file || []
          })
        }
      },
      handleDeleteAttach(row) {
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
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          attId: row.fileId,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
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
          this.$baseMessage('上传成功', 'success')
          this.fileList.push(res.data.fileIds[0])
        }
      },
      close() {
        this.formData = {
          complianceTdr: '',
          department: '',
          departmentName: '',
          id: '',
          postName: '',
          remark: '',
        }
        this.$refs['ruleForm'].resetFields()
        this.dialogFormVisible = false
        this.fileList = []
        this.footer = true
      },
      handleSuccess(res, file) {},
      handleDepartmentSelected(node) {
        //保存名称
        this.$set(this.formData, `departmentName`, node.name)
        //保存名称对应的ID
        this.$set(this.formData, `department`, node.id)
      },
      add() {
        const aa = []
        this.fileList.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.fileIds = aa.toString()
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            editHGZR(this.formData).then((res) => {
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
