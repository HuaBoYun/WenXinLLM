<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form
      v-if="show == 0"
      ref="form"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-row :gutter="15">
        <el-col :span="12">
          <el-form-item label="登记编号" prop="disputeno">
            <el-input
              v-model.trim="form.disputeno"
              clearable
              placeholder="请输入登记编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题" prop="disputeitem">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              placeholder="请输入纠纷主题"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷类型" prop="disputetype">
            <el-input
              v-model.trim="form.disputetype"
              clearable
              placeholder="请输入纠纷类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择合同名称"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.xzht.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号">
            <el-input
              v-model.trim="form.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同执行人">
            <el-input
              v-model.trim="form.realname"
              clearable
              placeholder="请输入合同执行人"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告" prop="plaintiff">
            <el-input
              v-model.trim="form.plaintiff"
              clearable
              placeholder="请输入原告"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告" prop="defendant">
            <el-input
              v-model.trim="form.defendant"
              clearable
              placeholder="请输入被告"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="争议焦点" prop="disputecours">
            <el-input
              v-model.trim="form.disputecours"
              clearable
              placeholder="请输入争议焦点"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷承办人" prop="zxstaffname">
            <el-input
              v-model.trim="form.zxstaffname"
              clearable
              placeholder="请选择纠纷承办人"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <el-radio-group v-model.trim="form.isuegent">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起诉类型" prop="whethersued">
            <el-radio-group v-model.trim="form.whethersued">
              <el-radio :label="1">起诉</el-radio>
              <el-radio :label="2">被诉</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最晚办结时间" prop="enddate1">
            <el-date-picker
              v-model.trim="form.enddate1"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入最晚办结时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外聘律师" prop="isattorney">
            <el-radio-group v-model.trim="form.isattorney">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :span="12">
          <el-form-item label="代理律师" prop="attorney">
            <el-input
              v-model.trim="form.attorney"
              clearable
              placeholder="请输入代理律师"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理律师联系电话" prop="attorneyphont">
            <el-input
              v-model.trim="form.attorneyphont"
              clearable
              placeholder="请输入代理律师联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <el-input
              v-model="form.solutionsuggestions"
              placeholder="请输入初步解决建议"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button v-if="!form.disputeid" type="success" @click="hold()">
              上传
            </el-button>
            <el-upload
              v-else
              ref="upload"
              :accept="accept"
              :action="baseApi + api"
              :before-upload="handleBeforeUpload"
              :data="uploadData"
              :file-list="fileList"
              :headers="headers"
              :limit="1"
              :on-error="onError"
              :on-remove="handleRemove"
              :on-success="onSuccess"
              :on-exceed="onExceed"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="uploadlist">
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
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="登记编号" prop="disputeno">
            <span>{{ form.disputeno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷类型" prop="disputetype">
            <span>{{ form.disputetype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号">
            <span>{{ form.contractno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同执行人">
            <span>{{ form.realname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告" prop="plaintiff">
            <span>{{ form.plaintiff }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告" prop="defendant">
            <span>{{ form.defendant }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="争议焦点" prop="disputecours">
            <span>{{ form.disputecours }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷承办人" prop="zxstaffname">
            <span>{{ form.zxstaffname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <span>{{ form.isuegent ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起诉类型" prop="whethersued">
            <span>{{ form.whethersued ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最晚办结时间" prop="enddate1">
            <span>{{ form.enddate1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外聘律师" prop="isattorney">
            <span>{{ form.isattorney ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理律师" prop="attorney">
            <span>{{ form.attorney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代理律师联系电话" prop="attorneyphont">
            <span>{{ form.attorneyphont }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <span>{{ form.solutionsuggestions }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="uploadlist">
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
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <xzht-options ref="xzht" @selecteded="handleSsjd" />
    <executor-options ref="executor" @selected="handleSelected" />
  </el-dialog>
</template>

<script>
  import {
    caseInformationSave,
    caseInformationModify,
    findAttacheMent,
    deleAttacheMent,
    disputeRegisterDetail,
  } from '@/api/contract/legal'
  import xzhtOptions from './options/xzht.vue'
  // import ExecutorOptions from '@/views/contract/legal/components/options/executor'
  import ExecutorOptions from '@/components/CompanySelectUserByTree'
  import store from '@/store'
  import { baseURL } from '@/config/net.config'
  export default {
    name: 'DraftEdit',
    components: { xzhtOptions, ExecutorOptions },
    data() {
      return {
        accept: '.pdf, .doc, .docx, .xls, .xlsx',
        api: '/contract/uploadFileAttInfo',
        // data: {
        //   attpath: '1649657363411.xlsx',
        //   attname: '1649657363411.xlsx',
        //   token: store.getters['user/token'],
        // },
        headers: {
          token: store.getters['user/token'],
          // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        uploadlist: [],
        value: {},
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
        currentValue: this.value || null,
        previewUrl: undefined,
        initialed: false,
        form: {
          zxstaffid: undefined,
          contractId: undefined,
          contractid: undefined,
          disputeno: undefined,
          disputeitem: undefined,
          disputetype: undefined,
          contractname: undefined,
          contractno: undefined,
          realname: undefined,
          plaintiff: undefined,
          defendant: undefined,
          disputecours: undefined,
          zxstaffname: undefined,
          isuegent: 1,
          whethersued: 1,
          isattorney: 1,
          attorneyphont: undefined,
          solutionsuggestions: undefined,
          enddate1: undefined,
          attorney: undefined,
          disputeid: undefined,
        },
        rules: {
          disputeno: [
            {
              required: true,
              message: '请输入登记编号',
              trigger: 'blur',
            },
          ],
          disputeitem: [
            {
              required: true,
              message: '请输入纠纷主题',
              trigger: 'blur',
            },
          ],
          disputetype: [
            {
              required: true,
              message: '请输入纠纷类型',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请选择合同',
              trigger: 'blur',
            },
          ],
          plaintiff: [
            {
              required: true,
              message: '请输入原告',
              trigger: 'blur',
            },
          ],
          defendant: [
            {
              required: true,
              message: '请输入被告',
              trigger: 'blur',
            },
          ],
          disputecours: [
            {
              required: true,
              message: '请输入争议焦点',
              trigger: 'blur',
            },
          ],
          zxstaffname: [
            {
              required: true,
              message: '请选择纠纷承办人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
      }
    },
    computed: {
      uploadData() {
        return {
          type: 1,
          bid: this.form.disputeid,
        }
      },
    },
    created() {},
    methods: {
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      //上传前置校验
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onExceed(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      //回调
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.title = '添加'
        } else {
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          this.form.disputeid = row.disputeid
          this.uploadList()
          this.getDetail(row)
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      async getDetail(row) {
        const { dispute } = await disputeRegisterDetail({
          disputeId: row.disputeid,
        })
        Object.keys(this.form).forEach((key) => {
          this.form[key] = dispute[key]
        })
        this.form.enddate1 = row.lastdealdate
        this.form.contractId = dispute.contractid
        this.form.contractid = dispute.contractid
        this.form.realname = dispute.realname
        this.form.isuegent = dispute.isuegent
        this.form.zxstaffid = dispute.disputeundertaker
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.uploadlist = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            const bCreate = !this.form.disputeid
            const func = bCreate ? caseInformationSave : caseInformationModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.disputeid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 1,
          bid: this.form.disputeid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
      },
      //附件删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 1 })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.uploadList()
        })
      },
      // 下载数据
      downloadData(row) {
        const fileName = row.attname
        const link = document.createElement('a')
        link.download = fileName
        link.href = this.baseApi + '/download?id=' + row.attid
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      //回调
      handleSelected(val) {
        this.form.zxstaffid = val.staffid
        this.form.zxstaffname = val.realname
      },
      //回调
      handleSsjd(item) {
        console.warn('handleSsjd', item)
        this.form.contractname = item.contractname
        this.form.contractno = item.contractno
        this.form.realname = item.realname
        this.form.contractId = item.contractid
        this.form.contractid = item.contractid
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
