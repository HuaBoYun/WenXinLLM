<!--
 * @Date: 2022-04-18 11:40:51
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-18 13:04:52
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/TemplateDetailContent.vue
-->
<template>
  <div>
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
              :disabled="true"
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
            <el-input v-model="form.contracttype" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属公司" prop="jborgName">
            <el-input
              v-model="form.jborgName"
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
        <el-col :span="24">
          <el-row>
            <el-col :span="18">
              <el-row>
                <el-col :span="24">
                  <!-- <UEditor v-model="form.describe" /> -->
                  <div style="display: flex">
                    <el-button
                      :disabled="!contractid"
                      class="add-btn"
                      size="mini"
                      type="primary"
                      @click="openOffice()"
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

                  <!-- <el-button
                    :disabled="!contractid"
                    class="add-btn"
                    size="mini"
                    type="primary"
                    @click="openWebOffice()"
                  >
                    编辑范本内容
                  </el-button> -->
                </el-col>
              </el-row>
            </el-col>
            <!-- <el-col :span="6">
              <el-row class="formula">
                <div class="custom">
                  <el-col :span="24">
                    <el-divider>相对方信息</el-divider>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="邮编">
                      <el-input
                        readonly
                        size="mini"
                        value="$[counterpart.coupersion]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="负责人">
                      <el-input
                        readonly
                        value="$[counterpart.personincharge]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="开户银行">
                      <el-input
                        readonly
                        value="$[counterpart.counterpartHank]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="相对方编号">
                      <el-input
                        readonly
                        value="$[counterpart.counumber]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="相对方名称">
                      <el-input
                        readonly
                        value="$[counterpart.couname]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="相对方地址">
                      <el-input
                        readonly
                        value="$[counterpart.couaddress]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="相对方联系人">
                      <el-input
                        readonly
                        value="$[counterpart.coupersion]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="联系人电话">
                      <el-input
                        readonly
                        value="$[counterpart.contactsPhone]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="开户行账号">
                      <el-input
                        readonly
                        value="$[counterpart.counterpartHankAccount]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="法定代表人">
                      <el-input
                        readonly
                        value="$[counterpart.legarepresentative]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="负责人电话">
                      <el-input
                        readonly
                        value="$[counterpart.pctelephonenumber]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="纳税人识别号">
                      <el-input
                        readonly
                        value="$[counterpart.taxpayeridentification]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-divider>合同信息</el-divider>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="合同编号">
                      <el-input
                        readonly
                        value="$[contract.contractno]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="合同名称">
                      <el-input
                        readonly
                        value="$[contract.contractname]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="合同金额">
                      <el-input
                        readonly
                        value="$[contract.contractamount]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="合同项目">
                      <el-input
                        readonly
                        value="$[contract.contractItem]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="合同执行人">
                      <el-input
                        readonly
                        value="$[contract.executor]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                    <el-form-item label="人民币大写">
                      <el-input
                        readonly
                        value="$[contract.rmbinwords]"
                        @focus="handleFocus($event)"
                      />
                    </el-form-item>
                  </el-col>
                </div>
              </el-row>
            </el-col> -->
          </el-row>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" :disabled="changeSaveBtn">
        确 定
      </el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <TextEditor ref="webOffice" />
    <unit-options ref="unit" @selected="handleUnitSelected" />
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import UnitOptions from './options/unit.vue'
  import UEditor from '@/components/UEditor'
  import TextEditor from './contractsEdit/TextEditor'
  export default {
    name: 'TemplateEdit',
    components: {
      UEditor,
      TextEditor,
      Resubmit,
      UnitOptions,
    },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        contractid: '',

        changeSaveBtn: false,
        form: this.formData,
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
        fzoptions: [],
        disabled: false,

        fromId: null,
        fromIdcopy: null,
        flowtaskinfoflowid: null,
        ymFromId: null,
        status: 0,
        uploadUrl: '',
      }
    },
    watch: {
      formData(val) {
        this.form = val
      },
    },
    mounted() {
      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })
    },
    methods: {
      //offcie
      openWebOffice() {
        //
        this.$store.commit('acl/contractidd', this.contractid)
        this.$refs['webOffice'].show()
      },
      //回调函数
      handleUnitSelected(node) {
        this.$set(this.form, 'jbunit', node.id)
        this.$set(this.form, 'jborgName', node.label)
      },
      handleFocus(event) {
        event.currentTarget.select()
      },
      //获取编号
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
      async showEdit(
        row,
        typeItem,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        status
      ) {
        if (typeItem) {
          this.form.contracttype = typeItem.typename
        }
        // if (row && row.contractid) {
        //   const { code } = await checkStatus({ contractId: row.contractid })
        //   if (code != 1) {
        //     return
        //   }
        // }
        if (!row || !row.flowid) {
          this.title = '添加'
          this.getNo()
        } else {
          this.title = '编辑'
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          console.log('row', row)
          console.log('formData', this.formData)
          this.contractid = row.contractid
          this.form.flowId = row.flowid
        }

        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.contractid = ''

        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.form.flowId = 733271
            const { describe, ...other } = this.form
            const { msg } = await saveContract({ ...other })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            // this.close();
          }
        })
      },
      //引迈流程提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      //office处理
      async openOffice() {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        let temp = document.createElement('form')
        temp.action = `https://office.wenxin.example.com/api/office/createContract?fileType=word&contractName=jokey-test&contractId=${this.contractid}&uid=${info.staffid}&name=${info.username}`
        temp.method = 'post'
        temp.target = '_blank'
        document.body.appendChild(temp)
        temp.submit()
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
  .custom /deep/ .el-form-item {
    margin-bottom: 4px !important;
  }
</style>
