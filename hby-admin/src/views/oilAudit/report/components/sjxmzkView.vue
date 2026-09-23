<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
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
          <el-form-item label="审计结果确认单" prop="resultcode">
            <el-input
              v-model="formData.resultcode"
              :style="{ width: '266px' }"
              clearable
              placeholder="请选择审计结果确认单"
              disabled
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="handleChoicePlan"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-select
              v-model="formData.projectName"
              class="filter-item"
              style="width: 100%"
            >
              <el-option
                v-for="item in auditProjectSelectList"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="auditOrgName">
            <el-input
              v-model="formData.auditOrgName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入被审计单位"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="追款金额（万元）" prop="money">
            <el-input
              v-model.number="formData.money"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入追款金额（万元）"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="追款相对方" prop="zkOrgName">
            <el-input
              v-model="formData.zkOrgName"
              :style="{ width: '80%' }"
              disabled
              clearable
              placeholder="请输入追款相对方"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="$refs.department.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="追款事由（逐项填写）" prop="reason">
            <el-input
              v-model="formData.reason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入追款事由（逐项填写）"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              clearable
              placeholder="备注"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>

    <!-- 计划项目子组件 -->
    <auditResult ref="result" @resultList="getChildlist" />

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { implementPlanList } from '@/oapi/audit/project'
  import {
    auditProjectZkDetail,
    auditProjectZkSaveOrUpdate,
  } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import auditResult from './options/auditResult.vue'
  const token = store.getters['user/token']

  export default {
    components: { DepartmentOptions, auditResult },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          resultcode: '',
          projectName: '',
          auditOrgName: '',
          reason: '',
          money: '',
          zkOrgName: '',
          remark: '',
          resultId: '',
        },
        radio: '',
        footer: true,
        rules: {
          resultcode: [
            {
              required: true,
              message: '请选择审计结果确认单',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          auditOrgName: [
            {
              required: true,
              message: '请选择被审计单位',
              trigger: 'blur',
            },
          ],
          reason: [
            {
              required: true,
              message: '请输入追款事由',
              trigger: 'blur',
            },
          ],
          money: [
            {
              required: true,
              message: '请输入追款金额',
              trigger: 'blur',
            },
            {
              type: 'number',
              message: '请输入数字',
            },
          ],
          zkOrgId: [
            {
              required: true,
              message: '请输入追款相对方',
              trigger: 'blur',
            },
          ],
        },
        auditProjectSelectList: [],
        dialogFormVisible: false,
        title: '新增',
        depType: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.depType = type
        this.$refs.department.show()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, 'zkOrgId', node.id)
        this.$set(this.formData, 'zkOrgName', node.label)
      },
      async handleChoicePlan() {
        // const res = await resultList()
        this.$refs['result'].showEdit()
      },
      getChildlist(val) {
        this.formData.resultId = val[0].projectid || ''
        this.formData.auditOrgName = val[0].orgidnames || ''
        this.formData.resultcode = val[0].projectname || ''
        this.formData.auditOrgId = val[0].orgids || ''
        this.formData.reason = val[0].overview || ''
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        const {
          data: { tlist },
        } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
        if (tlist.length) {
          this.auditProjectSelectList = tlist.map((item) => {
            return {
              value: item.id,
              label: item.projectName,
            }
          })
        }
        if (row) {
          const res = await auditProjectZkDetail({ id: row.id })
          console.log('🚀 ~ showEdit ~ res:', res)
          Object.assign(this.formData, res.data.data)
          this.formData.zkOrgName = res.data.data.zkOrg.orgname
          this.formData.auditOrgName = res.data.data.auditOrg.orgname || ''
          this.formData.resultcode = res.data.data?.result?.resultcode || ''
          this.formData.resultId = res.data.data?.resultId || ''
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          this.formData = {
            ...this.formData,
          }
        }
      },
      close() {
        this.formData.id = ''
        this.formData.resultcode = ''
        this.formData.projectName = ''
        this.formData.auditOrgName = ''
        this.formData.reason = ''
        this.formData.money = ''
        this.formData.zkOrgName = ''
        this.formData.remark = ''
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let params = { ...this.formData }
            console.log(params)
            const data = await auditProjectZkSaveOrUpdate({
              ...params,
            })
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
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
