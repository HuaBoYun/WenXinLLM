<template>
  <div>
    <el-row :gutter="20">
      <!-- 左侧：原始数据 -->
      <el-col :span="12" v-if="type != '新增'">
        <el-card>
          <div slot="header">原始数据</div>
          <el-form
            ref="oldform"
            :class="{ disabled: true }"
            :disabled="disabled"
            label-width="140px"
            :model="oldform"
            :rules="rules"
          >
            <el-form-item label="公司编号" prop="orgnumber">
              <el-input v-model.trim="oldform.orgnumber" />
            </el-form-item>
            <el-form-item label="公司名称" prop="orgname">
              <el-input v-model.trim="oldform.orgname" />
            </el-form-item>
            <el-form-item label="公司介绍">
              <el-input v-model.trim="oldform.orgmeno" type="textarea" />
            </el-form-item>
            <el-form-item label="公司备注">
              <el-input v-model.trim="oldform.memo" type="textarea" />
            </el-form-item>
            <el-form-item label="是否开启望远镜功能">
              <el-radio-group v-model="oldform.iszy">
                <el-radio
                  v-for="(item, index) in iszyoptions"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否使用自动编号">
              <el-radio-group v-model="oldform.isautonumber">
                <el-radio
                  v-for="(item, index) in isautonumberoptions"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否启用密级管理">
              <el-radio-group v-model="oldform.useSecrect">
                <el-radio
                  v-for="(item, index) in mjOption"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- 右侧：编辑表单 -->
      <el-col :span="type == '新增' ? 24 : 12">
        <el-card>
          <div slot="header">新数据</div>
          <el-form
            ref="form"
            :class="{ disabled: disabled }"
            :disabled="disabled"
            label-width="140px"
            :model="form"
            :rules="rules"
          >
            <el-form-item label="公司编号" prop="orgnumber">
              <el-input v-model.trim="form.orgnumber" />
            </el-form-item>
            <el-form-item label="公司名称" prop="orgname">
              <el-input v-model.trim="form.orgname" />
            </el-form-item>
            <el-form-item label="公司介绍">
              <el-input v-model.trim="form.orgmeno" type="textarea" />
            </el-form-item>
            <el-form-item label="公司备注">
              <el-input v-model.trim="form.memo" type="textarea" />
            </el-form-item>
            <el-form-item label="是否开启望远镜功能">
              <el-radio-group v-model="form.iszy">
                <el-radio
                  v-for="(item, index) in iszyoptions"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否使用自动编号">
              <el-radio-group v-model="form.isautonumber">
                <el-radio
                  v-for="(item, index) in isautonumberoptions"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="是否启用密级管理">
              <el-radio-group v-model="form.useSecrect">
                <el-radio
                  v-for="(item, index) in mjOption"
                  :key="index"
                  :label="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <div class="footer" v-if="!disabled" style="text-align: right">
      <el-button v-if="!disabled" type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { orgSaveorg } from '@/api/setting/org'
  import { getFlowList } from '@/api/setting/auth'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'CompanyEdit',
    components: {
      Resubmit,
    },
    data() {
      return {
        disabled: false,
        olddata: {},
        form: {
          orgname: '',
          orgnumber: '',
          orgmeno: '',
          memo: '',
          writtenByDept: '',
          iszy: 0,
          isautonumber: 2,
          fatherorgid: '',
          orgtype: 2,
          auditType: 0,
          telescope: 2,
          unCreditCode: '',
          useSecrect: 0,
        },
        oldform: {
          orgname: '',
          orgnumber: '',
          orgmeno: '',
          memo: '',
          writtenByDept: '',
          iszy: 0,
          isautonumber: 2,
          fatherorgid: '',
          orgtype: 2,
          auditType: 0,
          telescope: 2,
          unCreditCode: '',
          useSecrect: 0,
        },
        rules: {
          orgnumber: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          orgname: [{ required: true, trigger: 'blur', message: '请输入名称' }],
          writtenByDept: [
            { required: true, trigger: 'blur', message: '请输入发文代字' },
          ],
          unCreditCode: [
            {
              required: true,
              trigger: 'blur',
              message: '请输入社会统一信用代码',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        isautonumberoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 2,
            label: '否',
          },
        ],
        iszyoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 0,
            label: '否',
          },
        ],
        mjOption: [
          {
            value: 1,
            label: '使用',
          },
          {
            value: 0,
            label: '不使用',
          },
        ],
        requireValuedata: false, // 是否需要流程校验
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },

    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type
      ) {
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
        this.disabled = title == 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        // 处理oldData - 左侧原始数据
        this.form = Object.assign({}, newData)
        this.oldform = Object.assign({}, oldData)
        this.form.iszy = +newData.iszy
        this.oldform.iszy = +oldData.iszy
      },
      close() {
        this.$refs['form'] && this.$refs['form'].resetFields()
        this.olddata = {}
        this.form = Object.assign({}, this.$options.data().form)
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, data } = await orgSaveorg(this.form)
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.close()
          }
        })
      },
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>

<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .footer {
    /* position: absolute;
    bottom: 0;
    right: 0;
    background: #fff;
    padding: 15px 20px; */
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
