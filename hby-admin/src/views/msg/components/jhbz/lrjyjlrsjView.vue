<template>
  <!-- 二级单位及成员离任审计 新增修改 -->
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="leaveNo">
            <el-input
              v-model="formData.leaveNo"
              :style="{ width: '100%' }"
              clearable
              disabled
              placeholder="请输入序号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <!-- 选择公司组件 -->
          <el-form-item label="被审计单位" prop="auditOrg.orgname">
            <el-input
              v-model="formData.auditOrg.orgname"
              clearable
              placeholder="请输入被审计单位"
              :style="{ width: '266px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.audiTree.showEdit()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 47px">
          <el-form-item label="委托书编号" prop="entrustNo">
            <el-input
              v-model="formData.entrustNo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入委托书编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="委托时间" prop="entrustTime">
            <el-date-picker
              v-model="formData.entrustTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择委托时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="审计任职期间" prop="auditTimeRange">
            <el-date-picker
              v-model="formData.auditTimeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="审计任职开始日期"
              end-placeholder="审计任职结束日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="项目类型" prop="projectType">
            <el-input
              v-model="formData.projectType"
              disabled
              placeholder="请选择"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="$refs['typeView'].showEdit()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              type="date"
              readonly
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- 选择单位（公司） -->
    <!-- <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    /> -->
    <SelectDepartment ref="audiTree" @submit="handleCompanyTreeSelected" />

    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <typeView ref="typeView" @submit="setType" />

    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
  import { ejdwlrsjUpdate, ejdwlrsjDetail } from '@/oapi/audit/plan'
  import { formatDay } from '@/utils/index'
  import { baseURL } from '@/config'
  import store from '@/store'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from '@/views/oilAudit/lrjjzr/components/type.vue'

  // import {
  //   editDataSource,
  //   getDataSourceDefaultInfo,
  //   LinkTest,
  // } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: { Resubmit, SelectDepartment, typeView },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          projectName: '',
          auditOrgId: null,
          auditOrg: {
            orgid: '',
            orgname: '',
          },
          entrustNo: '',
          entrustTime: undefined,
          auditTimeRange: [],
          createUser: '',
          createTime: '',
          leaveNo: '',
          projectType: '',
          remarks: '',
        },
        footer: true,
        rules: {
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          'auditOrg.orgname': [
            {
              required: true,
              message: '请输入被审计单位',
              trigger: 'blur',
            },
          ],
          entrustNo: [
            {
              required: true,
              message: '请输入委托书编号',
              trigger: 'blur',
            },
          ],
          entrustTime: [
            {
              required: true,
              message: '请输入委托书时间',
              trigger: 'blur',
            },
          ],
          auditTimeRange: [
            {
              type: 'array',
              required: true,
              message: '请选择审计任职期间',
              fields: {
                // tpye类型试情况而定,所以如果返回的是date就改成date
                0: { type: 'date', required: true, message: '请选择开始日期' },
                1: { type: 'date', required: true, message: '请选择结束日期' },
              },
            },
          ],
        },
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      ymsubmit() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) this.$refs.resubmit.ymsubmit()
        })
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.footer = title === 'edit'
        this.fromId = formId
        this.ymFromId = ymFromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.fromIdcopy = formId
        this.status = status
        const res = await ejdwlrsjDetail({ id: formId }) // 请求详情接口 回显
        if (res.code === 1) {
          // 详情数据序列化
          const detailDataArrays = Object.entries(res.data.data)

          // 然后，有多少数据，回填复制formData中多少项
          detailDataArrays.forEach((item) => {
            this.formData[item[0]] = item[1]
          })

          this.$set(this.formData, 'auditTimeRange', [
            new Date(res.data.data.auditStartTime),
            new Date(res.data.data.auditEndTime),
          ])
          this.$set(
            this.formData,
            'createUser',
            res.data.data?.createUser?.realname || ''
          ) // 后台数据创建人为对象，会引发显示问题
          this.$set(this.formData, 'auditOrg', {
            orgid: res.data.data?.auditOrg?.orgid,
            orgname: res.data.data?.auditOrg?.orgname,
          })
        }
      },
      setType(e) {
        this.formData.projectType = e.auditType
        this.$forceUpdate()
      },
      // 选公司后处理
      handleCompanyTreeSelected(val) {
        this.formData.auditOrg.orgid = val.id
        this.formData.auditOrg.orgname = val.label
        this.formData.auditOrgId = val.id
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      close() {
        this.formData = {
          id: '',
          projectName: '',
          auditOrgId: null,
          auditOrg: {
            orgid: '',
            orgname: '',
          },
          entrustNo: '',
          entrustTime: undefined,
          auditStartTime: '',
          auditEndTime: '',
          auditTimeRange: [],
          createUser: '',
          createTime: '',
          projectType: '',
          remarks: '',
        }
        this.$set(this.formData, 'auditTimeRange', []) // 避免清除失效
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
      },

      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            params.auditStartTime = formatDay(this.formData.auditTimeRange[0])
            params.auditEndTime = formatDay(this.formData.auditTimeRange[1])
            delete params['createUser'] // 去掉创建人，不然无法提交
            const res = await ejdwlrsjUpdate(params)
            if (res && res.code === 1) {
              this.$emit('fetchData')
              this.$message({
                message: '成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '失败',
                type: 'error',
              })
            }
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
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
