<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1300px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-row :gutter="15">
        <el-form
          ref="form"
          :disabled="disabled"
          label-width="100px"
          :model="form"
          :rules="rules"
        >
          <el-col :span="12">
            <el-form-item label="范本编号" prop="contractno">
              <el-input
                v-model="form.contractno"
                clearable
                placeholder="请输入范本编号"
                readonly
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="范本名称" prop="contractname">
              <el-input
                v-model="form.contractname"
                clearable
                placeholder="请输入范本名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属类型" prop="contracttype">
              <el-input v-model="form.contracttype" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属公司" prop="orgname">
              <el-input
                v-model="form.orgname"
                disabled
                placeholder="请选择所属公司"
                :style="{ width: '80%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.unit.show()"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="范本使用说明">
              <el-input
                v-model="form.momoconcat"
                :autosize="{ minRows: 4, maxRows: 4 }"
                placeholder="请输入范本使用说明"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4" :push="2">
            <div style="display: flex">
              <el-button
                class="add-btn"
                size="mini"
                type="primary"
                @click="openOffice()"
                v-if="contractid"
              >
                编辑范本
              </el-button>
              <el-upload
                ref="upload"
                accept=".doc,.docx"
                :show-file-list="false"
                :action="uploadUrl"
                :on-success="handleSuccess"
                :before-upload="handleFileBefore"
                name="uploadedFile"
                v-if="contractid"
                style="margin-left: 20px"
              >
                <el-button class="add-btn" size="mini" type="primary">
                  上传本地文件
                </el-button>
              </el-upload>
            </div>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
        <el-button
          v-if="
            (form.contractstatus == 2 || form.contractstatus == 3) &&
            jurisdictionCode == 1
          "
          @click="ymsubmit"
          type="primary"
        >
          提交
        </el-button>
      </template>
      <TextEditor ref="webOffice" />
      <unit-options ref="unit" @selected="handleUnitSelected" />
    </el-dialog>

    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { checkStatus, generateNo, saveContract } from '@/api/contract/manage'
  import UnitOptions from './options/unit.vue'
  import UEditor from '@/components/UEditor'
  import TextEditor from './contractsEdit/TextEditor'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import axios from 'axios'
  export default {
    name: 'TemplateEdit',
    components: {
      UEditor,
      TextEditor,
      UnitOptions,
      CandidateUserSelect,
      Resubmit,
    },
    data() {
      return {
        form: {
          flowId: 733271,
          contractid: undefined,
          contractno: '',
          contractname: '',
          contracttype: '',
          recordtype: 'HTGL007',
          momoconcat: '',
          contractstatus: 0,
        },
        contractid: undefined,
        rules: {
          contractname: [
            {
              required: true,
              message: '请输入范本名称',
              trigger: 'blur',
            },
          ],
          momoconcat: [
            {
              required: true,
              message: '请输入范本使用说明',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        typeOptions: [],
        disabled: false,
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
        uploadUrl: '',
      }
    },
    created() {
      this.userInfos = JSON.parse(localStorage.getItem('userInfo'))
    },
    methods: {
      //offcie打开处理
      async openWebOffice() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, data } = await saveContract(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.contractid = data.contractid
            await this.$store.commit('acl/contractidd', data.contractid)
            this.$refs['webOffice'].show()
          }
        })
      },
      //打开office
      async openOffice() {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/createContract?fileType=word&contractName=jokey-test&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
      },
      handleFocus(event) {
        event.currentTarget.select()
      },
      //编号获取
      async getNo() {
        const res = await generateNo({
          flowId: this.form.flowId,
        })
        this.form.contractno = res.data.contractno
      },
      showDetail(row) {
        this.title = '查看'
        this.disabled = true
        Object.keys(this.form).forEach((key) => {
          this.form[key] = row[key]
        })
        this.dialogFormVisible = true
      },
      async showEdit(row, typeItem) {
        if (typeItem) {
          this.form.contracttype = typeItem.typename
        }
        if (row && row.contractid) {
          const { code } = await checkStatus({ contractId: row.contractid })
          if (code != 1) {
            return
          }
        }
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.$set(this.form, 'jbunit', userInfo.linkOrg.orgid)
        this.$set(this.form, 'orgname', userInfo.linkOrg.orgname)
        if (!row || !row.flowid) {
          this.title = '添加'
          this.getNo()
        } else {
          this.title = '编辑'

          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.form.flowId = row.flowid
          this.form.jbunit = row.jbunit
          this.form.orgname = row.orgname
          this.contractid = row.contractid
          this.form.contractstatus = row.contractstatus

          if (row.contractstatus == 2 || row.contractstatus == 3) {
            const res2 = await getFlowTaskInfo({
              tableId: 4,
              formId: row.contractid,
            })
            this.jurisdictionCode = res2.data.isFlowInfo
            if (res2.data.isFlowInfo) {
              this.flowtaskinfoflowid = res2.data.flowId + ''
              this.fromId = row.contractid + ''
              this.fromIdcopy = row.contractid + ''
              this.ymFromId = res2.data.id + ''

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$emit('fetch-data')
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.contractid = ''
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, data } = await saveContract(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.contractid = data.contractid
            const { flowid, describe, ...other } = data
            let form = {
              ...other,
              flowId: 733271,
            }
            this.form = form
            // this.close()
          }
        })
      },
      //回调函数
      handleUnitSelected(node) {
        this.$set(this.form, 'jbunit', node.id)
        this.$set(this.form, 'orgname', node.label)
      },
      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      handleSuccess() {
        this.openOffice()
      },
      handleFileBefore(file) {
        return new Promise((resolve, reject) => {
          const info = JSON.parse(localStorage.getItem('userInfo'))
          // 此处动态配置action URL
          this.uploadUrl = `https://office.wenxin.example.com/api/office/createContract?fileType=word&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}&upload=true`
          // dom上传地址更新完成后，触发上传
          this.$nextTick(() => resolve())
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
