<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-row :gutter="14">
      <el-form
        ref="form"
        label-width="120px"
        :model="form"
        :rules="rules"
        :disabled="disabled"
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
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="form.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="form.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="方案编号" prop="plannumber">
            <el-input v-model.trim="form.plannumber" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案类型" prop="planType">
            <el-select
              v-model="form.planType"
              clearable
              placeholder="请选择方案类型"
              :style="{ width: '100%' }"
              @change="handlePlanTypeChange"
            >
              <el-option label="计划内" :value="0" />
              <el-option label="计划外" :value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.planType == 0" style="height: 47px">
          <el-form-item label="集团测试计划" prop="groupPlanName">
            <el-input
              v-model="form.groupPlanName"
              clearable
              placeholder="请选择集团测试计划"
              disabled
              style="width: 73%"
            />
            <el-button
              @click="openSelecePlanModal"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="方案名称" prop="planname">
            <el-input
              v-model.trim="form.planname"
              placeholder="请输入计划名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="计划年度" prop="planyear">
            <el-date-picker
              v-model="form.planyear"
              format="yyyy"
              placeholder="请选择计划年度"
              :style="{ width: '100%' }"
              type="year"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="计划时间" prop="time">
            <el-date-picker
              v-model="form.time"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="测试模板" prop="testtemid">
            <el-input
              v-model.trim="form.templename"
              :style="{ width: '256px' }"
              placeholder="请选择测试模板"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.template.show(form.secrectLevelId)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="方案指定部门" prop="planmadedep">
            <el-input
              v-model.trim="form.planmadedep"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.departmentTree.show(0, null, '方案指定部门')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="负责人" prop="staffid">
            <el-input
              v-model.trim="form.planleader"
              :style="{ width: '256px' }"
              placeholder="请选择负责人"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="chooseFzr()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 47px">
          <el-form-item label="测试类型" prop="testtype">
            <el-select
              v-model="form.testtype"
              clearable
              placeholder="请选择测试类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in field103Options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="开展费用(元)" prop="planfee">
            <el-input
              v-model.trim="form.planfee"
              placeholder="请输入开展费用"
              type="number"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 47px">
          <el-form-item label="投入人力" prop="numberofpeople">
            <el-input
              v-model.trim="form.numberofpeople"
              placeholder="请输入投入人力"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="被测试部门" prop="testedorgs">
            <el-input
              v-model.trim="form.testedorgs"
              placeholder="请选择被测试部门"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs['chooseMechanism'].show(true)"
            >
              多选
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input v-model="form.createName" disabled />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="!disabled">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <test-template ref="template" @selected="handleTemplateSelected" />
    <ChooseMechanism
      ref="chooseMechanism"
      :checkStrictly="true"
      @selected="handleChooseMechanism"
    />
    <DepartmentTreeModal
      ref="departmentTree"
      @selected="handleDepartmentTreeSelected"
    />
    <CompanySelectUserByTree
      ref="executor"
      @selected="handleExecutorSelected"
    />

    <selectPlanModal ref="selectPlanModal" @selected="handlePlanSelected" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <executor-options
      ref="executorls"
      @projectManage="handleExecutor"
      :secrectLevelId="form.secrectLevelId"
    />
  </el-dialog>
</template>

<script>
  // import { doEdit } from '@/api/table'
  import {
    findAutoNumber,
    nkcsSaveOrUpdate,
    updatePlan,
    getPlanDetail,
  } from '@/api/internal/plan'
  import { getDefaultRenderData } from '@/api/internal/project'
  import TestTemplate from './options/TestTemplate.vue'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
  import DepartmentTreeModal from '@/components/DepartmentTreeModal'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CustormForm from '@/components/customForm/index.vue'
  import ChooseMechanism from '@/views/workbench/internalTools/components/ChooseMechanism.vue'
  import selectPlanModal from '@/views/internal/internalTest/components/companyModal.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'

  import { hasMJ, couldMJ } from '@/utils'
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  export default {
    name: 'PlanView',
    components: {
      TestTemplate,
      // CompanyTreeModal,
      DepartmentTreeModal,
      CompanySelectUserByTree,
      CustormForm,
      ChooseMechanism,
      selectPlanModal,
      ZXPerson,
      ExecutorOptions,
    },
    data() {
      return {
        form: {
          testplanid: '',
          plannumber: '',
          planname: '',
          planyear: '',
          testtype: '',
          planmadedepId: '',
          planmadedep: '',
          testedorgs: '',
          time: '',
          planfee: '',
          staffid: '',
          planleader: '',
          testtemid: '',
          numberofpeople: '',
          selectProjectid: '',
          // assessname: '',
          // assessid: '',
          // assid: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          createName: '',
          groupid: '',
          groupPlanName: '',
        },
        rules: {
          plannumber: [
            { required: true, trigger: 'blur', message: '请输入计划编号' },
          ],
          planname: [
            { required: true, trigger: 'blur', message: '请输入计划名称' },
          ],
          // assessname: [
          //   { required: true, trigger: 'blur', message: '集团测试计划' },
          // ],
          planyear: [
            { required: true, trigger: 'blur', message: '请选择计划年度' },
          ],
          staffid: [
            { required: true, trigger: 'blur', message: '请选择负责人' },
          ],
          planmadedep: [
            { required: true, trigger: 'blur', message: '请选择被测试部门' },
          ],
          testtemid: [
            { required: true, trigger: 'blur', message: '请选择测试模板' },
          ],
          testedorgs: [
            { required: true, trigger: 'blur', message: '请选择被测试部门' },
          ],
          planType: [
            { required: true, trigger: 'change', message: '请选择方案类型' },
          ],
        },
        field103Options: [
          {
            label: '穿行测试',
            value: '穿行测试',
          },
          {
            label: '控制测试',
            value: '控制测试',
          },
        ],
        title: '',
        dialogFormVisible: false,
        renderData: [],
        disabled: true,
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('InternalTestPan')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
        console.log('🚀 ~ created ~ this.MJoption:', this.MJoption)
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
            this.form.staffid = ''
            this.form.planleader = ''
            this.form.templename = ''
            this.form.testtemid = ''
          }
        }
      },
      showEdit(row, disabled = false) {
        this.disabled = !!disabled
        // getDefaultRenderData({
        //   sceneId: 810575,
        // }).then((res) => {
        //   let arr = {}
        //   this.renderData = res.data
        //   res.data.forEach((i) => {
        //     arr[i.field] = [
        //       {
        //         required:
        //           i.isSystemRequired == 1
        //             ? true
        //             : i.isRequired == 1
        //             ? true
        //             : false,
        //         message: `请输入${i.name}`,
        //         trigger: 'blur',
        //       },
        //     ]
        //   })
        //   this.rules = arr
        // })

        if (!row) {
          this.title = '添加'
          this.getNumber()
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.createName = userInfo.realname
          this.form.planleader = userInfo.realname
          this.form.staffid = userInfo.staffid
          this.form.time = []
        } else {
          this.getInfo(row)
          this.title = '编辑'
        }
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        const { data, code, msg } = await getPlanDetail({
          selectProjectid: row.testplanid,
        })
        if (code === 1) {
          const { testplanid, starttime, endtime, testtemple, ...other } =
            data.test

          this.form = {
            ...other,
            time: [starttime, endtime],
            selectProjectid: testplanid,
            testplanid: testplanid,
            templename: testtemple.templename,
            testtemid: testtemple.testtemid,
          }
        }
      },
      close() {
        this.form = {
          testplanid: '',
          plannumber: '',
          planname: '',
          planyear: '',
          testtype: '',
          planmadedepId: '',
          planmadedep: '',
          testedorgs: '',
          time: '',
          planfee: '',
          staffid: '',
          planleader: '',
          testtemid: '',
          numberofpeople: '',
          selectProjectid: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          testedorgIds: '',
          groupid: '',
          groupPlanName: '',
        }
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { time, planyear, ...other } = this.form
            const { msg, code, data } = await nkcsSaveOrUpdate({
              ...other,
              planyear:
                planyear instanceof Date ? planyear.getFullYear() : planyear,
              starttime: time ? time[0] : '',
              endtime: time ? time[1] : '',
            })
            if (code == 1) {
              this.$baseMessage(
                '保存成功',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      async getNumber() {
        const { msg, code, data } = await findAutoNumber({
          column: 'PLANNUMBER',
          noId: '282',
          orgCol: 'ORGID',
          tblName: 'TBL_TESTPLAN',
        })
        if (code == 1) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.planmadedepId = userInfo.linkDetp.orgid
          this.form.planmadedep = userInfo.linkDetp.orgname
          this.form.plannumber = data
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      handleExecutorSelected(node) {
        const { planleader, staffid, ...other } = this.form
        let form = {
          ...other,
          staffid: node.staffid,
          planleader: node.realname,
        }
        this.form = form
      },
      handleExecutor(val) {
        this.$set(this.form, 'planleader', val[0].realname)
        this.$set(this.form, 'staffid', val[0].staffid)
      },
      handleTemplateSelected(node) {
        const { testtemid, templename, ...other } = this.form
        let form = {
          ...other,
          templename: node.templename,
          testtemid: node.testtemid,
        }
        this.form = form
      },
      handleDepartmentTreeSelected(val) {
        this.$set(this.form, 'planmadedepId', val.id)
        this.$set(this.form, 'planmadedep', val.name)
      },
      handleChooseMechanism(node) {
        // this.form.testedorgs = node.map((item) => item.name).join(',')
        this.$set(
          this.form,
          'testedorgs',
          node.map((item) => item.name).join(',')
        )
        this.$set(
          this.form,
          'testedorgIds',
          node.map((item) => item.id).join(',')
        )
      },
      openSelecePlanModal() {
        this.$refs.selectPlanModal.showEdit()
      },
      handlePlanSelected(val) {
        if (val && val[0]) {
          this.$set(this.form, 'planname', val[0].planname)
          this.$set(this.form, 'planyear', val[0].planyear)
          this.$set(this.form, 'testtype', val[0].testtype)
          this.$set(this.form, 'groupPlanName', val[0].planname)
          this.$set(this.form, 'groupid', val[0].id)
          this.$set(this.form, 'testtemid', val[0].testtemid)
          this.$set(this.form, 'templename', val[0].testtemple.templename)
          this.$set(this.form, 'time', [val[0].starttime, val[0].endtime])
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.form.staffScopeIds = ids
        this.form.staffScopeNames = names
      },
      chooseFzr() {
        this.$refs.executorls.showEdit(null, this.form.secrectLevelId)
      },
      handlePlanTypeChange(value) {
        // 当切换到计划外时，清空集团测试计划相关字段
        if (value === 1) {
          this.$set(this.form, 'groupPlanName', '')
          this.$set(this.form, 'groupid', '')
        }
      },
    },
  }
</script>
