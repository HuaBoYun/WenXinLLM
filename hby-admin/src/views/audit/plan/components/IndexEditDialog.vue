<template>
  <div>
    <!-- <el-dialog :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :append-to-body="true"
      @close="close"
    > -->
    <el-row :gutter="15">
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="medium"
        style="display: flex; flex-wrap: wrap"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="disabled"
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
              v-model="formData.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划编号">
            <el-input
              v-model="formData.plancode"
              clearable
              placeholder="请输入计划编号"
              :style="{ width: '100%' }"
              :disabled="true"
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="year"
              :style="{ width: '100%' }"
              placeholder="请选择计划年度"
              style="width: 266px"
              value-format="yyyy"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类别" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划类别"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划费用估算(万元)">
            <el-input
              v-model="formData.palncost"
              clearable
              placeholder="请输入计划费用估算"
              :style="{ width: '100%' }"
              :disabled="disabled"
              @input="inputMoney($event, 'palncost')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划时间" prop="spanDate">
            <el-date-picker
              :key="itemKey"
              @input="changeTime"
              v-model="formData.spanDate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划负责人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请选择计划负责人"
              style="width: 256px"
              disabled
            />
            <el-button
              @click="showGroupLeader"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外审" prop="isauditor">
            <el-radio-group
              v-model="formData.isauditor"
              size="medium"
              :disabled="disabled"
            >
              <el-radio
                v-for="(item, index) in fieldOptions"
                :key="index"
                :disabled="item.disabled"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制人">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期">
            <el-input
              v-model="formData.createtime"
              clearable
              placeholder="请输入编制日期"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计单位">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入审计单位"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>计划项目</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="margin-bottom: 5px; display: flex; justify-content: flex-end"
          >
            <div
              v-if="!disabled"
              style="
                display: flex;
                justify-content: flex-end;
                margin-right: 10px;
              "
            >
              <el-button
                type="primary"
                @click="handleDownloadTemplate"
                style="margin-right: 10px"
              >
                下载模板
              </el-button>
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + importApi"
                :headers="headers"
                :on-success="handleSuccessImport"
              >
                <el-button type="success" style="margin-right: 10px">
                  导入
                </el-button>
              </el-upload>
              <el-button type="success" @click="openProjectDialog()">
                增加
              </el-button>
            </div>
            <el-button type="success" @click="handleExport">导出</el-button>
          </div>

          <!-- 表格 -->
          <el-table
            border
            :data="tableDataProject"
            style="width: 100%; margin-bottom: 25px"
          >
            <el-table-column
              align="center"
              label="项目名称"
              prop="projectname"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <!-- <span>{{ scope.row.projectname }}</span> -->
                <el-button
                  type="text"
                  @click="openProjectDialog(scope.row, scope.$index, true)"
                >
                  {{ scope.row.projectname }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="工作目标"
              prop="targetname"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.targetname }}</span>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="计划完成时间"
              prop="finishtime"
              min-width="180px"
            >
              <template slot-scope="scope">
                <span>
                  {{
                    scope.row.finishtime ? formatDate(scope.row.finishtime) : ''
                  }}
                </span>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="被审计对象"
              prop="orgidnames"
              min-width="180px"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.orgidnames }}</span>
              </template>
            </el-table-column>

            <el-table-column align="center" label="是否外委" min-width="130">
              <template slot-scope="scope">
                <span>
                  {{
                    scope.row.externalassig === 1
                      ? '是'
                      : scope.row.externalassig === 0
                      ? '否'
                      : ''
                  }}
                </span>
              </template>
            </el-table-column>

            <el-table-column
              v-if="!disabled"
              align="center"
              label="操作"
              min-width="160"
              prop=""
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="openProjectDialog(scope.row, scope.$index)"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :show-file-list="false"
            action=""
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
            :multiple="true"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
          <el-table :data="tableData">
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
              <template slot-scope="scope">
                <el-button type="text" @click="handleDowns(scope.row)">
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(scope.row)"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row, 1)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>决策文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData2">
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
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDown(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row, 2)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <!-- <template v-if="!disabled" #footer> -->
      <el-button @click="close" v-if="!isedit && isFlow == 0">取 消</el-button>
      <el-button @click="close" v-if="isedit && isFlow == 0">关 闭</el-button>

      <el-button type="primary" @click="save">确 定</el-button>
      <!-- <el-button type="primary" v-if="isFlow == 1&& (formData.opinionstatus == 2 || formData.opinionstatus == 3) && jurisdictionCode == 1" @click="ymsubmit">
        提 交
      </el-button> -->
      <el-button
        type="primary"
        v-if="isFlow == 2"
        @click="ymsubmit"
        :disabled="btnLoading"
      >
        提 交
      </el-button>
      <!-- </template> -->
    </div>
    <!-- </el-dialog> -->
    <!-- <plan-leader ref="plan" @planList="planList"></plan-leader> -->
    <!-- <Company
      ref="auditee"
      :leftApi="getOrgTreeByDepartment"
      :lazy="false"
      :defaultExpandedH="3"
      @submit="auditee"
    ></Company> -->

    <!-- 选择项目主管部门弹窗 -->
    <DepartmentOptions ref="department" @submit="handleDepartmentSelected" />

    <company-select-modal ref="audiTree" @select="handleCompanyTreeSelected" />

    <!-- 选择组长组员子组件 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />

    <AuditeeDialog ref="auditee" @projectManage="getAuditee" />

    <!-- 计划项目弹窗 -->
    <plan-project-dialog
      v-model="projectDialogVisible"
      :project-data="projectFormData"
      :audit-type-options="auditTypeArr"
      @save="handleSaveProject"
      @close="closeProjectDialog"
      @show-auditee="showProjectObj"
      @select-project-org="$refs.audiTree.showEdit()"
      @select-department="$refs.department.showEdit()"
      @audit-type-change="handleProjectAuditTypeChange"
      ref="planProjectDialog"
      :isReadonly="isReadonly"
    />
  </div>
</template>

<script>
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import dayjs from 'dayjs'
  import {
    getPlanProjectListByPlanId,
    mergePlanProjectManageInfoList,
    mergeAuditPlanInfo,
    getAuditPlanViewDetail,
    getAuditPlanAttInfo,
    getAudittwoPlanAttInfo,
    removePlanProjectInfo,
    deleteFileById,
    deletetwoFileById,
    getAutoCodeByJhgl,
    exportPlanProjectListByPlanId,
  } from '@/api/audit/plan'
  import { getNbsjTypeListForMerge } from '@/api/audit/project'
  import { getOrgTreeByDepartment } from '@/api/common'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import { download } from '@/api/audit/implement'
  const { baseURL } = require('@/config')
  import { getFlowTaskInfo } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import AuditeeDialog from '@/views/audit/project/components/formComponents/AuditeeDialog.vue'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  import CompanySelectModal from '@/components/departments.vue'
  import DepartmentOptions from '@/components/departmentSelect.vue'
  import PlanProjectDialog from './PlanProjectDialog.vue'

  export default {
    name: 'IndexEdit',
    components: {
      Resubmit,
      selectTeam,
      CompanyTreeModel,
      AuditeeDialog,
      ZXPerson,
      ExecutorOptions,
      CompanySelectModal,
      DepartmentOptions,
      PlanProjectDialog,
    },
    props: {
      isedit: {
        type: Boolean,
        default: true,
      },
      isFlow: {
        type: Number,
        default: 0,
      },
    },
    data() {
      return {
        getOrgTreeByDepartment,
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        disabled: false,
        value: '',
        planRow: {},
        formData: {
          plancode: '',
          planname: undefined,
          palnyear: undefined,
          plantype: '',
          palncost: undefined,
          spanDate: [],
          realname: '',
          isauditor: '',
          organizationName: '',
          createtime: '',
          orgname: '',
          auditorgid: '',
          remarks: '',
          opinionstatus: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        fileList: [],
        fileList2: [],
        planid: '',
        createPerson: '',
        itemKey: '',
        rules: {
          plancode: [
            { required: true, message: '请输入计划编号', trigger: 'blur' },
          ],
          planname: [
            { required: true, message: '请输入计划名称', trigger: 'blur' },
          ],
          palnyear: [
            { required: true, message: '请输入计划年度', trigger: 'change' },
          ],
          plantype: [
            { required: true, message: '请输入计划类别', trigger: 'change' },
          ],
          spanDate: [
            {
              required: true,
              message: '请选择计划时间',
              trigger: 'change',
            },
          ],
          realname: [
            { required: true, message: '请选择计划负责人', trigger: 'change' },
          ],
          isauditor: [
            { required: true, message: '请选择是否外审', trigger: 'change' },
          ],
          orgname: [
            { required: true, message: '请输入审计单位', trigger: 'change' },
          ],
          remarks: [
            { required: true, message: '请输入活动名称', trigger: 'change' },
          ],
        },
        rulesProject: {
          projectname: [
            { required: true, message: '请输入项目名称', trigger: 'change' },
          ],
          targetname: [
            { required: true, message: '请输入工作目标', trigger: 'change' },
          ],
        },
        fieldOptions: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
        title: '',
        plancode: '',
        tableData: [],
        tableData2: [],
        tableDataProject: [],
        sIndex: 0,
        isSendBack: false,
        showLoading: true,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        jurisdictionCode: 0,
        btnLoading: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
        auditTypeArr: [],
        saveFlag: false, //保存新增行校验
        projectDialogVisible: false,
        projectFormData: {
          projectname: '',
          targetname: '',
          finishtime: '',
          orgidnames: '',
          orgids: '',
          externalassig: '',
          bsjtype: '',
          planprojectid: '',
          show: true,
          projectorg: '',
          projectorgid: '',
          projectType: '',
          costs: '',
          appproyear: [],
          projectmgdeptid: '',
          projectmgdeptname: '',
          startDate: '',
          endDate: '',
          projectorgaddress: '',
          projectlinkman: '',
          projectlinktel: '',
          implementaion: '',
          auditType: '',
          auditCode: '',
        },
        editingProjectIndex: -1,
        isReadonly: false,
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('PlanIndex')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      // 格式化日期
      formatDate(date) {
        return dayjs(date).format('YYYY-MM-DD')
      },
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      //选择项目单位
      handleCompanyTreeSelected(val) {
        if (this.projectDialogVisible) {
          // 更新项目弹窗中的数据
          this.$refs.planProjectDialog.updateProjectOrg(val)
        } else {
          // 原有主表单的实现
          const names = val.map((item) => item.name).join(',')
          const ids = val.map((item) => item.id).join(',')
          this.$set(this.formData, 'projectorg', names)
          this.$set(this.formData, 'projectorgid', ids)
        }
      },
      // 选择部门
      handleDepartmentSelected(node) {
        if (this.projectDialogVisible) {
          // 更新项目弹窗中的数据
          this.$refs.planProjectDialog.updateDepartment(node)
        } else {
          // 原有主表单的实现
          this.$set(this.formData, `projectmgdeptname`, node.label)
          this.$set(this.formData, `projectmgdeptid`, node.id)
        }
      },
      changeTime() {
        //设置key，使组件重新渲染生成
        this.itemKey = Math.random()
      },
      showObj(sIndex) {
        this.sIndex = sIndex
        this.$refs['auditee'].showEdit()
      },
      //被审计对象
      getAuditee(val, data) {
        if (this.projectDialogVisible) {
          // 更新弹窗中的数据
          this.$refs.planProjectDialog.updateAuditee(val, data)
        } else {
          // 旧的实现，用于表格中的选择（如果需要保留）
          if (data == 'left') {
            const names = val.map((res) => res.label).toString()
            const ids = val.map((res) => res.id).toString()
            this.$set(this.tableDataProject[this.sIndex], 'orgidnames', names)
            this.$set(this.tableDataProject[this.sIndex], 'orgids', ids)
            this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'bm')
          } else {
            this.$set(
              this.tableDataProject[this.sIndex],
              'orgidnames',
              val[0].realname
            )
            this.$set(
              this.tableDataProject[this.sIndex],
              'orgids',
              val[0].staffid
            )
            this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'ry')
          }
        }
      },
      close() {
        this.$emit('fetch-data')
        this.$bus.$emit('updateMsg', 0)
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.tableDataProject = []
        this.$emit('close')
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          const arrAttid = this.tableData.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          } else {
            this.formData.attids = ''
          }

          if (valid) {
            //上层表单参数
            let params = {
              planid: this.formData.planid,
              plancode: this.formData.plancode,
              planname: this.formData.planname,
              palnyear: this.formData.palnyear,
              plantype: this.formData.plantype,
              palncost: this.formData.palncost,
              planStartTime:
                this.formData.spanDate && this.formData.spanDate[0]
                  ? dayjs(this.formData.spanDate[0]).format('YYYY-MM-DD')
                  : '',
              planEndTime:
                this.formData.spanDate && this.formData.spanDate[1]
                  ? dayjs(this.formData.spanDate[1]).format('YYYY-MM-DD')
                  : '',
              isauditor: this.formData.isauditor,
              createstaffid: this.formData.staffid,
              principalid: this.formData.principalid,
              attIds: this.formData.attids,
              organizationName: this.formData.realname,
              // createtime: this.formData.createtime,
              orgname: this.formData.orgname,
              auditorgid: this.formData.auditorgid,
              remarks: this.formData.remarks,
              secrectLevelId: this.formData.secrectLevelId,
              staffScopeIds: this.formData.staffScopeIds,
              staffScopeNames: this.formData.staffScopeNames,
              opinionstatus: this.formData.opinionstatus,
            }

            // if (this.disabled) {
            //   params.opinionstatus = 0
            // }
            //审计计划上层表单提交
            let resForm = await mergeAuditPlanInfo(params)
            if (resForm.code === 1) {
              this.$baseMessage(
                resForm.msg,
                'success',
                'vab-hey-message-success'
              )
              this.planid = resForm.data.auditPlan.planid
              this.formData.planid = resForm.data.auditPlan.planid
              this.saveFlag = false
              this.tableDataProject.map((row) => {
                if (!row.projectname || !row.targetname) {
                  this.$baseMessage('请填写项目名称和工作目标', 'error')
                  this.saveFlag = true
                  return
                }
                // 确保每个项目都有planid
                row.planid = resForm.data.auditPlan.planid
                this.saveProject(row)
              })
              console.log(this.saveFlag)
              if (this.isFlow != 2) {
                this.close()
              }
            }
          }
        })
      },
      // 给表头加必填符号*
      addRedStar(h, { column }) {
        return [
          h('span', { style: 'color: red' }, '*'),
          h('span', ' ' + column.label),
        ]
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      // 保存
      async saveProject(row) {
        row.show = false
        console.log('保存项目:', row)
        const data = {
          planid: this.planid,
          plancode: this.formData.plancode,
          ...row,
        }
        // 确保planid总是被正确设置
        if (!data.planid && this.planid) {
          data.planid = this.planid
        }
        let res = await mergePlanProjectManageInfoList(data)
        return res
      },
      //下载模板
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/计划项目导入模板.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '计划项目导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      // 批量导入
      handleSuccessImport(response) {
        if (response.code == 1) {
          // 将导入的数据追加到现有数据中，而不是替换
          this.tableDataProject = [...this.tableDataProject, ...response.data]
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      // 打开计划项目弹窗
      openProjectDialog(row, index, type) {
        if (type) {
          this.isReadonly = true
        } else {
          this.isReadonly = false
        }
        if (row) {
          // 编辑模式
          this.projectFormData = JSON.parse(JSON.stringify(row))
          this.editingProjectIndex = index
        } else {
          // 新增模式
          this.projectFormData = {
            projectname: '',
            targetname: '',
            finishtime: '',
            orgidnames: '',
            orgids: '',
            externalassig: '',
            bsjtype: '',
            planprojectid: '',
            show: true,
            projectorg: '',
            projectorgid: '',
            projectType: '',
            costs: '',
            appproyear: [],
            projectmgdeptid: '',
            projectmgdeptname: '',
            startDate: '',
            endDate: '',
            projectorgaddress: '',
            projectlinkman: '',
            projectlinktel: '',
            implementaion: '',
            auditType: '',
            auditCode: '',
          }
          this.editingProjectIndex = -1
        }
        this.projectDialogVisible = true
      },
      // 关闭计划项目弹窗
      closeProjectDialog() {
        this.projectFormData = {
          projectname: '',
          targetname: '',
          finishtime: '',
          orgidnames: '',
          orgids: '',
          externalassig: '',
          bsjtype: '',
          planprojectid: '',
          show: true,
          projectorg: '',
          projectorgid: '',
          projectType: '',
          costs: '',
          appproyear: [],
          projectmgdeptid: '',
          projectmgdeptname: '',
          startDate: '',
          endDate: '',
          projectorgaddress: '',
          projectlinkman: '',
          projectlinktel: '',
          implementaion: '',
          auditType: '',
          auditCode: '',
        }
        this.editingProjectIndex = -1
      },

      // 处理项目中的审计类型变更
      handleProjectAuditTypeChange(val) {
        // 可以在这里处理审计类型变更的逻辑
        console.log('项目审计类型变更:', val)
      },

      // 保存计划项目弹窗
      handleSaveProject(formData) {
        if (this.editingProjectIndex !== -1) {
          // 更新现有项目
          this.$set(this.tableDataProject, this.editingProjectIndex, formData)
        } else {
          // 添加新项目
          this.tableDataProject.push(formData)
        }
        // // 如果有计划ID，直接保存到后台
        // if (this.planid) {
        //   this.saveProject(formData)
        // }
      },

      // 显示被审计对象选择弹窗
      showProjectObj() {
        this.$refs['auditee'].showEdit()
      },

      // 被审计对象选择回调函数
      getAuditee(val, data) {
        if (this.projectDialogVisible) {
          // 更新弹窗中的数据
          this.$refs.planProjectDialog.updateAuditee(val, data)
        } else {
          // 旧的实现，用于表格中的选择（如果需要保留）
          if (data == 'left') {
            const names = val.map((res) => res.label).toString()
            const ids = val.map((res) => res.id).toString()
            this.$set(this.tableDataProject[this.sIndex], 'orgidnames', names)
            this.$set(this.tableDataProject[this.sIndex], 'orgids', ids)
            this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'bm')
          } else {
            this.$set(
              this.tableDataProject[this.sIndex],
              'orgidnames',
              val[0].realname
            )
            this.$set(
              this.tableDataProject[this.sIndex],
              'orgids',
              val[0].staffid
            )
            this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'ry')
          }
        }
      },

      // 原有的添加行按钮方法 - 现在改为弹窗
      handleAdd() {
        this.openProjectDialog()
      },
      handleDeleteFile(index, row, key) {
        const deleteApi = key === 1 ? deleteFileById : deletetwoFileById
        deleteApi({ attId: row.attid }).then((res) => {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          if (key === 1) this.tableData.splice(index, 1)
          else this.tableData2.splice(index, 1)
        })
      },

      handleDelete(index, row) {
        console.log('rrrr', row)

        if (row.planprojectid) {
          removePlanProjectInfo({
            planprojectid: row.planprojectid,
          }).then((res) => {
            if (res.code == 1) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.tableDataProject.splice(index, 1)
            }
          })
        } else {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.tableDataProject.splice(index, 1)
        }
      },
      showGroupLeader() {
        if (this.showMJ) {
          if (this.formData.secrectLevelId) {
            this.$refs.executor.showEdit(null, this.formData.secrectLevelId)
          } else {
            this.$message.error('请选择密级')
          }
        } else {
          this.$refs.executor.showEdit(null)
        }
      },
      handleExecutorSelected(val) {
        if (val) {
          this.$set(this.formData, 'realname', val[0].realname)
          this.$set(this.formData, 'principalid', val[0].staffid)
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['form'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
      },
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      // 审批页面获取密级数据
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showEdit(
        row,
        disabled,
        planNum,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        status,
        flowType
      ) {
        let resType = await getNbsjTypeListForMerge()
        this.auditTypeArr = resType.data

        // 如果有flowType，则获取审批页面密级数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.showLoading = false
        this.showLoading = true
        // 退回之后的情况，根据url获取planid
        if (row.planid) {
          this.planid = row.planid
          this.planRow = row
        } else if (row.cyurl) {
          this.planid = this.getQueryVariable(row.cyurl, 'planid')
        } else {
          this.planid = ''
        }
        this.plancode = row.plancode
        //新建按钮入口
        if (!row && !disabled) {
          // 获取计划编号
          let resC = await getAutoCodeByJhgl()

          this.$nextTick(() => {
            let planNumRes =
              planNum.split('-')[0] +
              '-' +
              (parseInt(planNum.split('-')[1]) + 1)

            this.createPerson = JSON.parse(
              localStorage.getItem('userInfo')
            ).realname

            this.tableDataProject = []
            this.tableData = []
            this.tableData2 = []
            this.fileList = []
            this.fileList2 = []
            this.formData = {
              plancode: resC.data.autoCode,
              organizationName: JSON.parse(localStorage.getItem('userInfo'))
                .realname,
              createtime: dayjs().format('YYYY-MM-DD'),
              orgname: JSON.parse(localStorage.getItem('userInfo')).currentOrg
                .orgname,
              auditorgid: JSON.parse(localStorage.getItem('userInfo'))
                .currentOrg.orgid,
            }
            this.$refs['form'].resetFields()
          })

          this.disabled = false
        } else if (row && disabled) {
          //查看按钮入口
          this.title = '查看'
          this.disabled = disabled
          let resD = await getAuditPlanViewDetail({
            planid: this.planid,
          })
          console.log('resD', resD)
          // 保存审批用的密级id
          if (resD.data.auditPlan.secrectLevelId) {
            localStorage.setItem(
              'SPsecrectLevelId',
              resD.data.auditPlan.secrectLevelId
            )
          }
          //获取审计计划附件
          let resF = await getAuditPlanAttInfo({
            planId: this.planid,
          })
          this.tableData = resF.data || []

          this.$nextTick(() => {
            const auditPlan = resD.data.auditPlan || {}
            const principalStaff = auditPlan.principalStaff || {}
            const createStaff = auditPlan.createStaff || {}
            const auditOrgInfo = auditPlan.auditOrgInfo || {}
            this.formData = {
              opinionstatus: row.opinionstatus,
              plancode: auditPlan.plancode,
              planname: auditPlan.planname,
              palnyear: auditPlan.palnyear,
              plantype: auditPlan.plantype,
              palncost: auditPlan.palncost,
              realname: principalStaff && principalStaff.realname,
              isauditor: auditPlan.isauditor,
              organizationName: createStaff && createStaff.realname,
              createtime: dayjs(auditPlan.createtime).format('YYYY-MM-DD'),
              orgname: auditOrgInfo.orgname,
              auditorgid: auditOrgInfo.orgid,
              remarks: auditPlan.remarks,
              principalid: principalStaff && principalStaff.staffid,
              createstaffid: createStaff && createStaff.staffid,
              planStartTime: auditPlan.starttime,
              planEndTime: auditPlan.endtime,
              spanDate: [
                dayjs(auditPlan.starttime).format('YYYY-MM-DD'),
                dayjs(auditPlan.endtime).format('YYYY-MM-DD'),
              ],
              secrectLevelId: auditPlan.secrectLevelId,
              staffScopeIds: auditPlan.staffScopeIds,
              staffScopeNames: auditPlan.staffScopeNames,
            }
            this.uploader = row.createStaff && row.createStaff.realname
          })

          //计划项目接口
          let res = await getPlanProjectListByPlanId({ planId: this.planid })
          if (res.data && res.data.planList) {
            res.data.planList.forEach((item) => {
              item.show = true
              // 处理批复的项目起止年限
              if (item.appproyearstart && item.appproyearend) {
                item.appproyear = [
                  dayjs(item.appproyearstart).format('YYYY-MM-DD'),
                  dayjs(item.appproyearend).format('YYYY-MM-DD'),
                ]
              }
            })
            this.tableDataProject = res.data.planList
          }
        } else {
          // 保存审批用的密级id
          if (row.secrectLevelId) {
            localStorage.setItem('SPsecrectLevelId', row.secrectLevelId)
          }
          //编辑按钮入口
          this.title = '编辑'
          this.disabled = false
          //表格修改按钮入口
          let resD = await getAuditPlanViewDetail({
            planid: this.planid,
          })
          // 保存审批用的密级id
          if (resD.data.auditPlan.secrectLevelId) {
            localStorage.setItem(
              'SPsecrectLevelId',
              resD.data.auditPlan.secrectLevelId
            )
          }
          //获取审计计划附件
          let resF = await getAuditPlanAttInfo({
            planId: this.planid,
          })
          this.tableData = resF.data || []

          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
          if (!this.isedit) {
            if (row.cystaffid == userInfo.staffid && row.cystate == '需调整') {
              this.disabled = false
            } else {
              this.disabled = true
            }
          } else {
            this.disabled = false
          }

          this.$nextTick(() => {
            const auditPlan = resD.data.auditPlan || {}
            const principalStaff = auditPlan.principalStaff || {}
            const createStaff = auditPlan.createStaff || {}
            const auditOrgInfo = auditPlan.auditOrgInfo || {}
            this.formData = {
              opinionstatus: row.opinionstatus,
              planid: auditPlan.planid,
              plancode: auditPlan.plancode,
              planname: auditPlan.planname,
              palnyear: auditPlan.palnyear,
              plantype: auditPlan.plantype,
              palncost: auditPlan.palncost,
              realname: principalStaff && principalStaff.realname,
              isauditor: auditPlan.isauditor,
              organizationName: createStaff && createStaff.realname,
              createtime: dayjs(auditPlan.createtime).format('YYYY-MM-DD'),
              orgname: auditOrgInfo && auditOrgInfo.orgname,
              auditorgid: auditOrgInfo && auditOrgInfo.orgid,
              remarks: auditPlan.remarks,
              principalid: principalStaff && principalStaff.staffid,
              createstaffid: createStaff && createStaff.staffid,
              planStartTime: auditPlan.starttime,
              planEndTime: auditPlan.endtime,
              spanDate: [
                dayjs(auditPlan.starttime).format('YYYY-MM-DD'),
                dayjs(auditPlan.endtime).format('YYYY-MM-DD'),
              ],
              secrectLevelId: auditPlan.secrectLevelId,
              staffScopeIds: auditPlan.staffScopeIds,
              staffScopeNames: auditPlan.staffScopeNames,
            }
            this.uploader = row.createStaff && row.createStaff.realname
          })

          //计划项目接口
          let res = await getPlanProjectListByPlanId({ planId: this.planid })
          if (res.data && res.data.planList) {
            res.data.planList.forEach((item) => {
              item.show = true
              // 处理批复的项目起止年限
              if (item.appproyearstart && item.appproyearend) {
                item.appproyear = [
                  dayjs(item.appproyearstart).format('YYYY-MM-DD'),
                  dayjs(item.appproyearend).format('YYYY-MM-DD'),
                ]
              }
            })
            this.tableDataProject = res.data.planList
          }
        }
      },
      getQueryVariable(url, variable) {
        var query = url && url.substring(1)
        var vars = query && query.split('?')
        if (vars && vars.length > 0) {
          for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=')
            if (pair[0] == variable) {
              return pair[1]
            }
          }
        }
        return false
      },
      // 导出计划项目
      async handleExport() {
        if (!this.planid) {
          this.$message.warning('请先保存计划信息')
          return
        }
        try {
          const response = await exportPlanProjectListByPlanId({
            planId: this.planid,
          })
          // 创建一个blob对象
          const blob = new Blob([response], {
            type: 'application/vnd.ms-excel',
          })
          // 创建下载链接
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          // 设置文件名
          link.download = `计划项目列表_${this.formData.plancode}.xlsx`
          // 触发下载
          document.body.appendChild(link)
          link.click()
          // 清理
          document.body.removeChild(link)
          window.URL.revokeObjectURL(link.href)
        } catch (error) {
          console.error('导出失败:', error)
          this.$message.error('导出失败')
        }
      },
    },
  }
</script>
