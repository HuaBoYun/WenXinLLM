<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原职务" prop="oldJob">
            <el-input
              v-model="formData.oldJob"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入原职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原行政级别" prop="oldLevel">
            <el-input
              v-model="formData.oldLevel"
              clearable
              placeholder="请输入原行政级别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原单位" prop="orgname">
            <el-input
              v-model="formData.orgname"
              disabled
              placeholder="请选择原单位"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="handleObject"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原任职起止时间" prop="oldJobTimeRange">
            <el-date-picker
              v-model="formData.oldJobTimeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="原任职开始日期"
              end-placeholder="原任职结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现职务" prop="job">
            <el-input
              v-model="formData.job"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入现职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="现行政级别" prop="levels">
            <el-input
              v-model="formData.levels"
              clearable
              placeholder="请输入现行政级别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现单位">
            <el-input
              v-model="formData.org.orgname"
              clearable
              readonly
              placeholder="请输入现单位"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="handleObjects"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起始时间" prop="jobTimeRange">
            <el-date-picker
              v-model="formData.jobTimeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否审计" prop="isAudit">
            <el-radio-group v-model="formData.isAudit">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <template v-if="isAll">
          <el-col :span="12">
            <el-form-item label="部门职能分类" prop="deptType">
              <el-input
                v-model="formData.deptType"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入部门职能分类"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门历史沿革" prop="deptHistory">
              <el-input
                v-model="formData.deptHistory"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入部门历史沿革"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主责主业" prop="mainDuty">
              <el-input
                v-model="formData.mainDuty"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入主责主业"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要权利" prop="mainPower">
              <el-input
                v-model="formData.mainPower"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入主要权利"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对外业务" prop="business">
              <el-input
                v-model="formData.business"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入对外业务"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" style="height: 29px">
            <el-form-item label="人员构成" prop="person">
              <el-input
                v-model="formData.person"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入人员构成"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否独立核算" prop="isSeparateAccount">
              <el-radio-group v-model="formData.isSeparateAccount">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资产情况（万元）" prop="assetInfo">
              <el-input
                v-model="formData.assetInfo"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入资产情况（万元）"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要费用" prop="mainCost">
              <el-input
                v-model="formData.mainCost"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入主要费用"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收入（万元）" prop="income">
              <el-input
                v-model="formData.income"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入收入（万元）"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成本（万元）" prop="cost">
              <el-input
                v-model="formData.cost"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入成本（万元）"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" style="height: 29px">
            <el-form-item label="可控成本（万元）" prop="controllableCost">
              <el-input
                v-model="formData.controllableCost"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入可控成本（万元）"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否有外雇人员" prop="hasExternalPerson">
              <el-radio-group v-model="formData.hasExternalPerson">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="非常规性工作" prop="unconventJob">
              <el-input
                v-model="formData.unconventJob"
                :style="{ width: '100%' }"
                clearable
                placeholder="请输入非常规性工作"
              />
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              v-model="formData.remarks"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- 选择单位（公司） -->
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <SelectDepartment ref="audiTrees" @submit="getDepartmentInfos" />
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { sjdwlrsjlrUpdate, sjdwlrsjlrDetail } from '@/oapi/audit/plan'
  import { formatDay } from '@/utils/index'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  export default {
    components: { SelectDepartment },
    inheritAttrs: false,
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        tableData: [],
        formDisabled: false,
        formData: {
          id: '',
          name: '',
          oldJob: '',
          oldLevel: '',
          oldOrgId: '',
          orgname: '',
          oldOrg: {
            orgname: '',
          },
          oldJobStartTime: '',
          oldJobEndTime: '',
          oldJobTimeRange: [], // 原任职时间区间
          job: '',
          levels: '',
          orgId: '',
          org: {
            orgname: '',
          },
          jobTimeRange: [],
          deptType: '',
          deptHistory: '',
          mainDuty: '',
          mainPower: '',
          business: '',
          person: '',
          isSeparateAccount: null,
          assetInfo: '',
          mainCost: '',
          income: '',
          cost: '',
          controllableCost: '',
          hasExternalPerson: null,
          unconventJob: '',
          isAudit: 1,
          quarterId: '', //季度id
          quarterName: '', //季度名称
          tbdw: '', //填报单位
          remarks: '',
          dataIndex: '',
        },
        rules: {
          name: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur',
            },
          ],
          oldJob: [
            {
              required: true,
              message: '请输入原职务',
              trigger: 'blur',
            },
          ],
          oldLevel: [
            {
              required: true,
              message: '请输入原行政级别',
              trigger: 'blur',
            },
          ],
          orgname: [
            {
              required: true,
              message: '请输入原单位',
              trigger: 'change',
            },
          ],
          oldJobTimeRange: [
            {
              type: 'array',
              required: true,
              message: '请选择原任职起止时间',
              trigger: 'change',
            },
          ],
          // job: [
          //   {
          //     required: true,
          //     message: '请输入现职务',
          //     trigger: 'blur',
          //   },
          // ],
          // levels: [
          //   {
          //     required: true,
          //     message: '请输入现行政级别',
          //     trigger: 'blur',
          //   },
          // ],
          // org: [
          //   {
          //     required: true,
          //     message: '请输入现单位',
          //     trigger: 'blur',
          //   },
          // ],
          // jobTimeRange: [
          //   {
          //     type: 'array',
          //     required: true,
          //     message: '请选择现任职起止时间',
          //     trigger: 'change',
          //   },
          // ],
          deptType: [
            {
              required: true,
              message: '请输入部门职能分类',
              trigger: 'change',
            },
          ],
          deptHistory: [
            {
              required: true,
              message: '请输入部门历史沿革',
              trigger: 'blur',
            },
          ],
          mainDuty: [
            {
              required: true,
              message: '请输入主责主业',
              trigger: 'blur',
            },
          ],
          mainPower: [
            {
              required: true,
              message: '请输入主要权利',
              trigger: 'blur',
            },
          ],
          income: [
            {
              validator,
              trigger: 'blur',
            },
          ],
          cost: [
            {
              validator,
              trigger: 'blur',
            },
          ],
          controllableCost: [
            {
              validator,
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        type: 1,
        disabled: false,
        isAll: false,
      }
    },
    methods: {
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.oldOrgId = val.id
        this.formData.oldOrg.orgname = val.label
        this.formData.orgname = val.label
      },
      handleObjects() {
        this.$refs['audiTrees'].showEdit()
      },
      getDepartmentInfos(val) {
        this.formData.orgId = val.id
        this.formData.org.orgname = val.label
      },
      // 选公司
      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'orgname',
          // idKey: 'company',
          title: '单位',
        })
      },
      /**
       * type类型: 1-新增, 2-编辑, 3-季度弹窗中编辑
       */
      async showEdit(row, title, isAll = false) {
        this.isAll = isAll
        this.dialogFormVisible = true
        // this.formDisabled = !!disabled
        this.formDisabled = title == '详情'
        if (row) {
          this.formData.dataIndex = row.index

          const { quarter } = row
          this.formData.quarterId = row.quarterId

          const orgName = quarter?.org.orgname || ''
          this.formData.quarterName = `第${
            quarter?.quarter || ''
          }季度(${orgName})`
          this.formData.tbdw = orgName
          console.log('row', row)
          if (row) {
            const res = await sjdwlrsjlrDetail({ id: row.id })
            if (res) {
              Object.assign(this.formData, res.data.data)
              this.formData.orgname = res.data.data.oldOrg.orgname
            }
            this.$set(this.formData, 'oldJobTimeRange', [
              new Date(this.formData.oldJobStartTime),
              new Date(this.formData.oldJobEndTime),
            ])
            if(this.formData.jobStartTime && this.formData.jobEndTime) {
              this.$set(this.formData, 'jobTimeRange', [
                new Date(this.formData.jobStartTime),
                new Date(this.formData.jobEndTime),
              ])
            }
          } else {
            const detailDataArrays = Object.entries(row)
            // 然后，有多少数据，回填复制formData中多少项
            detailDataArrays.forEach((item) => {
              this.formData[item[0]] = item[1]
            })
            this.$set(this.formData, 'oldJobTimeRange', [
              new Date(row.oldJobStartTime),
              new Date(row.oldJobEndTime),
            ])
            if(this.formData.jobStartTime && this.formData.jobEndTime) {
              this.$set(this.formData, 'jobTimeRange', [
                new Date(this.formData.jobStartTime),
                new Date(this.formData.jobEndTime),
              ])
            }
          }
        }
      },
      close() {
        this.formData = {
          id: '',
          name: '',
          oldJob: '',
          oldLevel: '',
          oldOrgId: '',
          orgname: '',
          oldOrg: {
            orgname: '',
          },
          oldJobStartTime: '',
          oldJobEndTime: '',
          oldJobTimeRange: [],
          job: '',
          levels: '',
          orgId: '',
          org: {
            orgname: '',
          },
          jobTimeRange: [],
          deptType: '',
          deptHistory: '',
          mainDuty: '',
          mainPower: '',
          business: '',
          person: '',
          isSeparateAccount: null,
          assetInfo: '',
          mainCost: '',
          income: '',
          cost: '',
          controllableCost: '',
          hasExternalPerson: null,
          unconventJob: '',
          isAudit: 1,
          quarterId: '', //季度id
          quarterName: '', //季度名称
          tbdw: '', //填报单位
          dataIndex: '',
        }
        this.type = 1
        this.$set(this.formData, 'oldJobTimeRange', []) // 避免清除失效
        this.$set(this.formData, 'jobTimeRange', [])
        this.dialogFormVisible = false
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            params.oldJobStartTime = formatDay(this.formData.oldJobTimeRange[0])
            params.oldJobEndTime = formatDay(this.formData.oldJobTimeRange[1])
            params.jobStartTime = formatDay(this.formData.jobTimeRange[0])
            params.jobEndTime = formatDay(this.formData.jobTimeRange[1])
            const res = await sjdwlrsjlrUpdate(params)
            this.loading = false
            if (res && res.code === 1) {
              this.$emit('fetchData', {
                data: [res.data],
                index: this.formData.dataIndex,
              })
              this.close()
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
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
