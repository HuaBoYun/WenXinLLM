<template>
  <div>
    <el-form
      ref="elForm"
      :rules="rules"
      label-width="140px"
      :model="formData"
      size="medium"
    >
      <!-- <el-col :span="24"> -->
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="风险创建" name="1">
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
                :disabled="formDisabled"
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
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId || formDisabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>业务单元</el-divider>
          </el-col>
          <el-form-item class="form-inlink" label="流程名称" prop="flowname">
            <el-select
              v-model="formData.flowname"
              :disabled="formDisabled"
              clearable
              placeholder="请选择"
              :style="{ width: '100%' }"
              @change="riskProcesChange"
            >
              <el-option
                v-for="item in riskProcesList"
                :key="item.processno"
                :label="item.processname"
                :value="item.processname"
              />
            </el-select>
            <!-- <el-input style="width: 100%" v-model="formData.flowname" :disabled="formDisabled"></el-input> -->
          </el-form-item>
          <el-form-item
            label="新增/修改"
            class="form-inlink"
            prop="revisiontype"
          >
            <el-select
              style="width: 100%"
              v-model="formData.revisiontype"
              :disabled="formDisabled"
              @change="handleRevisionTypeChange"
            >
              <el-option
                v-for="item in revisionTypeList"
                :label="item.label"
                :value="item.value"
                :key="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-col :span="12" v-if="formData.revisiontype == 2">
            <el-form-item label="关联风险信息" prop="riskextname">
              <el-input
                v-model="formData.riskextname"
                clearable
                placeholder="请选择关联风险信息"
                style="width: 75%"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.selectFxxx.showEdit(formData.riskcatid, 1)"
                :disabled="formDisabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-form-item
            class="form-inlink"
            label="业务名称"
            prop="bussinessname"
          >
            <!-- <el-select
                v-model="formData.bussinessname"
                :disabled="formDisabled"
                clearable
                placeholder="请选择"
                :style="{ width: '100%' }"
                @change="riskBusinessChange"
              >
                <el-option
                  v-for="item in riskBusinessList"
                  :key="item.businessno"
                  :label="item.businessname"
                  :value="item.businessno"
                />
              </el-select> -->
            <el-input
              placeholder="请输入业务名称"
              :disabled="formDisabled"
              v-model="formData.bussinessname"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item
              class="form-inlink"
              label="业务编号"
              prop="flownumber"
            >
              <el-input
                v-model="formData.flownumber"
                :disabled="true"
              ></el-input>
            </el-form-item> -->

          <!-- </el-col> -->
          <!-- <el-col :span="12"> -->

          <!-- </el-col> -->
          <el-form-item
            class="form-inlink"
            label="牵头责任部门"
            prop="belongstoName"
          >
            <el-input
              v-model="formData.belongstoName"
              disabled
              style="width: 75%; margin-right: 8px"
            ></el-input>
            <el-button
              type="primary"
              @click="handleShowCompent('zr')"
              :disabled="formDisabled"
            >
              选择
            </el-button>
          </el-form-item>

          <el-form-item label="业务描述" prop="bussinessdes">
            <el-input
              :disabled="formDisabled"
              type="textarea"
              :rows="4"
              v-model="formData.bussinessdes"
            ></el-input>
          </el-form-item>
          <!-- </el-col> -->
          <!-- <el-col :span="24"> -->
          <el-divider>风险描述</el-divider>
          <!-- </el-col> -->
          <!-- <el-col :span="12"> -->
          <el-form-item class="form-inlink" label="编码规则" prop="risknumber">
            <el-input v-model="formData.risknumber" disabled></el-input>
          </el-form-item>
          <!-- </el-col> -->
          <!-- <el-col :span="12"> -->

          <!-- </el-col> -->
          <!-- <el-col :span="12"> -->

          <el-form-item class="form-inlink" label="风险领域" prop="riskcatname">
            <el-input v-model="formData.riskcatname" disabled></el-input>
          </el-form-item>
          <el-form-item
            class="form-inlink"
            label="二级风险"
            prop="riskcatnametwo"
          >
            <el-input v-model="formData.riskcatnametwo" disabled></el-input>
          </el-form-item>
          <el-form-item class="form-inlink" label="三级风险" prop="riskname">
            <el-input
              v-model="formData.riskname"
              :disabled="formDisabled"
            ></el-input>
          </el-form-item>
          <el-form-item
            class="form-inlink"
            label="四级风险"
            prop="levelFourRisk"
          >
            <el-input
              v-model="formData.levelFourRisk"
              :disabled="formDisabled"
            ></el-input>
          </el-form-item>
          <el-form-item
            class="form-inlink"
            label="版本"
            prop="version"
            v-if="formDisabled"
          >
            <el-input
              v-model="formData.version"
              :disabled="formDisabled"
            ></el-input>
          </el-form-item>
          <el-form-item class="form-inlink" label="风险来源" prop="risklevel">
            <el-select
              v-model="formData.risklevel"
              :disabled="formDisabled"
              clearable
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in risklevelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <!-- </el-col> -->
          <!-- <el-col :span="24"> -->
          <el-form-item label="风险描述" prop="riskdes">
            <el-input
              type="textarea"
              :disabled="formDisabled"
              v-model="formData.riskdes"
              :rows="4"
            ></el-input>
          </el-form-item>
          <el-form-item label="风险原因" prop="riskcause">
            <el-input
              type="textarea"
              :disabled="formDisabled"
              v-model="formData.riskcause"
              :rows="4"
            ></el-input>
          </el-form-item>
          <el-form-item class="form-inlink" label="所属公司" prop="unitname">
            <el-input v-model="formData.unitname" disabled></el-input>
          </el-form-item>
          <el-form-item
            class="form-inlink"
            label="所属部门"
            prop="linkDeptName"
          >
            <el-input v-model="formData.linkDeptName" disabled></el-input>
          </el-form-item>
          <el-form-item class="form-inlink" label="创建人" prop="staffname">
            <el-input v-model="formData.staffname" disabled></el-input>
          </el-form-item>
          <el-form-item
            class="form-inlink"
            label="创建时间"
            prop="riskcreatedt"
          >
            <el-input v-model="formData.riskcreatedt" disabled></el-input>
          </el-form-item>
          <el-divider>合规要求</el-divider>
          <el-form-item label="外部规定" prop="riskexternal">
            <el-input
              type="textarea"
              :rows="4"
              :disabled="formDisabled"
              v-model="formData.riskexternal"
            ></el-input>
          </el-form-item>
          <el-form-item label="公司规定" prop="riskcompany">
            <el-input
              type="textarea"
              :rows="4"
              :disabled="formDisabled"
              v-model="formData.riskcompany"
            ></el-input>
          </el-form-item>
          <el-form-item label="合规红线" prop="riskcompliance">
            <el-input
              type="textarea"
              :rows="4"
              :disabled="formDisabled"
              v-model="formData.riskcompliance"
            ></el-input>
          </el-form-item>
          <el-form-item label="合规义务" prop="complianceobligation">
            <el-input
              type="textarea"
              :disabled="formDisabled"
              v-model="formData.complianceobligation"
              :rows="4"
            ></el-input>
          </el-form-item>
          <!-- </el-col> -->

          <!-- 风险应对 -->
          <el-col :span="12">
            <el-form-item label="风险应对策略类型" prop="copingPlot">
              <el-select
                v-model="formData.copingPlot"
                clearable
                placeholder="请选择风险应对策略类型"
                :style="{ width: '100%' }"
                :disabled="formDisabled"
              >
                <el-option
                  v-for="(item, index) in field106Options"
                  :key="index"
                  :disabled="item.disabled"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险期望值" prop="riskHopeValue">
              <el-input
                v-model="formData.riskHopeValue"
                clearable
                placeholder="请输入风险期望值"
                :style="{ width: '100%' }"
                :disabled="formDisabled"
                @input="handleInput"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险管控负责人" prop="ydusername">
              <el-input
                placeholder="请选择风险管控负责人"
                v-model="formData.ydusername"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="handleShowUser('ydusername')"
                :disabled="formDisabled"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="关联风险模型" prop="associatedRiskModel">
              <el-input
                placeholder="请选择关联风险模型"
                v-model="formData.associatedRiskModel"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="handleShowRiskModel('associatedRiskModel')"
                :disabled="formDisabled"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
              <el-button
                type="primary"
                @click="check"
                v-if="sqlstr && bookid"
                :style="{ marginLeft: '10px' }"
              >
                结果
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="应对方案" prop="yddes">
              <el-input
                v-model="formData.yddes"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入应对方案"
                :style="{ width: '100%' }"
                type="textarea"
                disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>一体化管控措施</el-divider>
          </el-col>
          <el-col :span="24" style="margin-bottom: 20px">
            <div
              style="text-align: right; margin-bottom: 5px; margin-right: 10px"
            >
              <el-button
                type="success"
                @click="hanAddControl"
                :disabled="formDisabled || controlList.length >= 1"
              >
                新增
              </el-button>
            </div>
            <el-table :data="controlList">
              <el-table-column
                align="center"
                label="一体化管控措施编号"
                prop="controlnumber"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <el-button type="text" @click="showControlDetail(row)">
                    {{ row.controlnumber }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="一体化控制目标"
                prop="controldes"
                show-overflow-tooltip
              />
              <el-table-column align="center" label="控制措施" prop="conkzcs">
                <template #default="{ row }">
                  <div
                    v-html="
                      row.conkzcs ? row.conkzcs.replace(/\n/g, '<br>') : ''
                    "
                  ></div>
                </template>
              </el-table-column>
              <el-table-column align="center" label="操作" v-if="!formDisabled">
                <template #default="{ row }">
                  <el-button type="text" @click="handleEditControl(row)">
                    修改
                  </el-button>
                  <el-button type="text" @click="handleDeleteControl(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-tab-pane>
      </el-tabs>

      <!-- <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="formData.content"
            :height="300"
            :disabled="formDisabled"
            :templates="templates"
            template="nbsj"
          />
        </el-col> -->
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px" v-if="!formDisabled">
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
            <div style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
        </div>

        <el-table :data="tableDataFile">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDowns(row)">下载</el-button>
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button
                type="text"
                @click="handleDeleteAttach(row)"
                v-if="!formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <!-- </el-col> -->
    </el-form>

    <div slot="footer" style="text-align: right" v-if="!formDisabled">
      <el-button @click="save" type="primary" :loading="saveLoading">
        确定
      </el-button>
      <el-button @click="ymsubmit" type="primary" :disable="btnLoading">
        提交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <CompanyTreeModel
      ref="comTreeRef"
      :multiple="checkbox"
      @selected="handleSelectCompany"
    />
    <rules ref="rulesRef" @setRules="setRules" />
    <selectFxxx ref="selectFxxx" @selected="handleFxxxSelected" />
    <controlMeasures
      ref="controlMeasures"
      @fetch-data="getData"
      @add="addControl"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <RiskModal ref="riskModelRef" @selected="handleRiskModelSelected" />
    <SqlModal ref="check"></SqlModal>
  </div>
</template>

<script>
  import BpmnModeler from '@/components/bpmnjs/BpmnModeler'
  import controlMeasures from '@/views/risk/treatment/controlMeasures.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import {
    getPrivewAttInfo,
    getRiskProcess,
    getRiskBusiness,
    getRiskProcessNo,
  } from '@/api/contract/manage'
  import '@logicflow/core/dist/style/index.css'
  import '@logicflow/extension/lib/style/index.css'
  import workflow from '@/views/risk/identify/creation/workflow'
  import CustomList from '@/views/risk/identify/creation/components/CustomList.vue'
  import { zgjkLeft } from '@/api/setting/org'
  import { formatOptions } from '@/utils/validate'
  import { createLevelByParent } from '@/api/internal/project'
  import {
    getCreationFlowList,
    createRisk,
    riskAnalysisDetail,
    controlDelete,
    updateControlResponseplan,
  } from '@/api/risk'
  //  '@/oapi/risk/index.js'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import rules from '@/views/risk/identify/creation/components/rules.vue'
  import { downFieldById, deleteById } from '@/api/risk/riskEvents'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import selectFxxx from '@/views/risk/identify/components/selectFxxx.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { riPlanInfo } from '@/api/systemLog'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import dayjs from 'dayjs'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  import RiskModal from '@/views/risk/identify/creation/components/riskModal.vue'
  import SqlModal from '@/views/risk/base/components/sqlCheck.vue'
  export default {
    name: 'ProjectEdit',
    components: {
      BpmnModeler,
      workflow,
      CustomList,
      CompanySelectUserByTree,
      CompanyTreeModel,
      rules,
      CandidateUserSelect,
      UEditor,
      selectFxxx,
      controlMeasures,
      Resubmit,
      ZXPerson,
      RiskModal,
      SqlModal,
    },
    props: ['riskcatid', 'treeId'],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        activeName: '1',
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        total1: 0,
        total2: 0,
        pageBean: [],
        pageBeanTWO: [],
        fileIdList: [],
        queryForm1: {
          isFlowdb: '1',
          pageNo: 1,
          pageSize: 20,
        },
        formDisabled: false,
        queryForm2: {
          isFlowdb: '1',
          pageNo: 1,
          pageSize: 20,
        },
        controlForm: {
          pid: '',
          pageNo: 1,
          pageSize: 20,
        },
        tableDataFile: [],
        formData: {
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
          associatedRiskModel: undefined,
          stepid: undefined,
        },
        toplevelflowcatOptions: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],

        controltypeOptions: [
          {
            label: '预防性控制',
            value: '1',
          },
          {
            label: '发现性控制',
            value: '2',
          },
          {
            label: '纠正性控制',
            value: '3',
          },
        ],
        riskcatnameOptions: [
          {
            label: '战略风险',
            value: '1',
          },
          {
            label: '财务风险',
            value: '2',
          },
          {
            label: '市场风险',
            value: '3',
          },
          {
            label: '运营风险',
            value: '4',
          },
          {
            label: '法律合规风险',
            value: '5',
          },
          {
            label: '核安全环保风险',
            value: '6',
          },
          {
            label: '工程建设风险',
            value: '7',
          },
        ],
        risklevelOptions: [
          {
            label: '内部风险',
            value: '1',
          },
          {
            label: '外部风险',
            value: '2',
          },
        ],

        controlmethodOptions: [
          {
            label: '手工',
            value: '1',
          },
          {
            label: '自动',
            value: '2',
          },
          {
            label: '依赖手工的自动化',
            value: '3',
          },
        ],
        financialreportidentifyOptions: [
          {
            label: '存在与发生',
            value: '1',
          },
          {
            label: '完整性',
            value: '2',
          },
          {
            label: '权利与义务',
            value: '3',
          },
          {
            label: '估计与平摊',
            value: '4',
          },
          {
            label: '表达与披露',
            value: '5',
          },
        ],
        revisionTypeList: [
          {
            label: '制定',
            value: 1,
          },
          {
            label: '修订',
            value: 2,
          },
        ],
        belongstoNameOptions: [],
        rules: {
          flownumber: [
            { required: true, message: '请输入业务编号', trigger: 'blur' },
          ],
          bussinessname: [
            {
              required: true,
              message: '请选择业务名称',
              trigger: 'change',
            },
          ],
          flowname: [
            // { required: true, message: '请输入流程名称', trigger: 'blur' },
            {
              required: true,
              message: '请选择流程名称',
            },
          ],
          risknumber: [
            { required: true, message: '请输入风险编号', trigger: 'blur' },
          ],
          belongstoName: [
            {
              required: true,
              message: '请选择牵头责任部门',
              trigger: 'change',
            },
          ],
          reorgName: [
            { required: true, message: '请输入相关部门', trigger: 'blur' },
          ],
          revisiontype: [
            {
              required: true,
              message: '请选择制/修订',
              trigger: 'change',
            },
          ],
          riskextname: [
            {
              required: true,
              message: '请选择关联风险信息',
              trigger: 'change',
            },
          ],
          riskname: [
            { required: true, message: '请输入风险名称', trigger: 'blur' },
          ],
          version: [{ required: true, message: '请输入版本', trigger: 'blur' }],
          controlnumber: [
            {
              required: true,
              message: '请输入风险控制点编号',
              trigger: 'blur',
            },
          ],
          controldes: [
            {
              required: true,
              message: '请输入风险控制点描述',
              trigger: 'blur',
            },
          ],
          conkzcs: [
            { required: true, message: '请输入控制措施', trigger: 'blur' },
          ],
          controlmanager: [
            {
              required: true,
              message: '请选择控制责任人',
              trigger: 'change',
            },
          ],
          copingPlot: [
            {
              required: true,
              message: '请选择风险应对策略类型',
              trigger: 'change',
            },
          ],
          riskcause: [
            {
              required: true,
              message: '请选择风险原因',
              trigger: 'blur',
            },
          ],
          riskdes: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        dialogFormVisible2: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
        current: '',
        multipleSelection: [],
        itemRow: {},
        //提交

        jurisdictionCode: 0,
        templates: [],
        //流程联动
        riskProcesList: [],
        riskBusinessList: [],
        //风险应对
        controlList: [],
        field106Options: [
          {
            label: '承担',
            value: '1',
          },
          {
            label: '转移',
            value: '2',
          },
          {
            label: '规避',
            value: '3',
          },
          {
            label: '降低（风险控制）',
            value: '4',
          },
        ],
        riskcopingid: '',
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        footer: true,
        showMJ: false,
        MJoption: [],
        btnLoading: false,
        saveLoading: false,
        type: '',
        checkbox: false,
        sqlstr: '',
        bookid: '',
      }
    },
    mounted() {
      console.log(this.treeId, 'treeId===')
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      handleInput(value) {
        // 使用正则表达式过滤掉非数字字符
        this.formData.riskHopeValue = value.replace(/[^\d]/g, '')
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      // 选择内规
      setNG(type) {
        this.$refs['rulesRef'].showEdit(type)
      },
      // 内外规
      setRules(val) {
        console.log('----', val)
      },
      async pageInit() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoNameOptions = newValue
        this.reorgNameOptions = newValue
        const flow = await getCreationFlowList()
        this.toplevelflowcatOptions = formatOptions(flow, 'flowname', 'flowid')
      },
      //获取风险一体化数据
      async fetchData() {
        this.loading = true
        await riPlanInfo({
          riskid: this.riskid,
        })
          .then(async (res) => {
            this.controlList = res.data.controls || []
          })
          .finally(() => {
            this.loading = false
          })
      },
      treeFindPath(tree, func, field = '', path = []) {
        if (!tree) return []
        for (const data of tree) {
          field === '' ? path.push(data) : path.push(data[field])
          if (func(data)) return path
          if (data.children) {
            const findChildren = this.treeFindPath(
              data.children,
              func,
              field,
              path
            )
            if (findChildren.length) return findChildren
          }
          path.pop()
        }
        return []
      },
      /**
       * @description: 选择人员回调
       * @return {*}
       */
      handleExecutorSelected(e) {
        if (this.type == 'ydusername') {
          this.$set(this.formData, 'ydusername', e.realname)
          this.$set(this.formData, 'userId', e.staffid)
        } else {
          this.$set(this.formData, 'leadershipName', e.realname)
          this.$set(this.formData, 'leadership', e.staffid)
        }
      },
      /**
       * @description:选择部门回调
       * @return {*}
       */
      handleSelectCompany(e) {
        console.log('handleSelectCompany', e)
        if (this.bmType === 'zr') {
          this.$set(this.formData, 'belongstoName', e.name)
          this.$set(this.formData, 'belongsto', e.id)
        } else {
          this.$set(
            this.formData,
            'cooperateOrgName',
            e.map((v) => v.name).join(',')
          )
          this.$set(this.formData, 'cooperateOrg', e.map((v) => v.id).join(','))
        }
      },
      /**
       * @description: 选择table 行
       * @return {*}
       */
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      selectControlUser(row) {
        this.formData.controlmanager = row.realname
        this.dialogFormVisible2 = false
      },
      /**
       * @description: 打开人员页面
       * @return {*}
       */
      handleShowUser(type) {
        this.type = type
        this.$refs['userTreeRef'].show()
      },
      handleShowCompent(type) {
        this.bmType = type
        this.checkbox = false
        if (this.bmType === 'ph') {
          this.checkbox = true
        }
        if (this.formData.reorg) {
          var arr = this.formData.reorg.split(',')
        }
        this.$refs['comTreeRef'].show(false, [])
      },

      handleChange(data) {
        console.log(data)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleControlSizeChange(val) {
        this.controlForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleControlCurrentChange(val) {
        this.controlForm.pageNo = val
        this.fetchData()
      },
      handleClick() {},
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        this.getAssociationInfo()
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        if (formId) {
          this.riskid = formId
          await this.getData()
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.formDisabled = true
        } else if (title == 'add') {
          this.title = '新增'
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      async getData() {
        const { data } = await riskAnalysisDetail({
          riskcatid: this.riskcatid,
          riskid: this.riskid,
        })
        if (data.risk?.secrectLevelId) {
          localStorage.setItem('SPsecrectLevelId', data.risk.secrectLevelId)
        }
        await this.fetchData()
        this.riskProcesSave(data.flow && data.flow.flowname)
        this.formData =
          {
            ...data.controls[0],
            status: data.risk.status || 0,
            risklevel: data.risk.risklevel || undefined,
            riskcatname: data.risk?.riskcatname,
            // data.risk &&
            // data.risk.riskcatname &&
            // data.risk.riskcatname.split(','),
            riskcatid: data.risk.riskcatid || undefined,
            riskid: data.risk.riskid || undefined,
            riskcatnametwo: data.risk.riskcatnametwo || undefined,
            riskcatidone: data.risk.riskcatidone || undefined,
            complianceobligation: data.risk.complianceobligation || undefined,
            riskcause: data.risk.riskcause || undefined,
            flownumber: (data.flow && data.flow.flownumber) || undefined,
            flowname: (data.flow && data.flow.flowname) || undefined,
            belongstoName: data.risk.zrbmName || undefined,
            reorgName: data.risk.xgbmName || undefined,
            reorg: data.risk.reorg || undefined,
            belongsto: data.risk.belongsto || undefined,
            bussinessname:
              data.riskBussiness && data.riskBussiness.bussinessname,
            bussinessdes: data.riskBussiness && data.riskBussiness.bussinessdes,
            risknumber: data.risk.risknumber || undefined,
            riskname: data.risk.riskname || undefined,
            levelFourRisk: data.risk.levelFourRisk || undefined,
            version: parseInt(data.risk.version) + '.0' || undefined,
            riskdes: data.risk.riskdes || undefined,
            flowid: (data.flow && data.flow.flowid) || undefined,
            toplevelflowcat: '',

            riskprogram: data.risk.riskprogram || undefined,
            riskexternal: data.risk.riskexternal || undefined,
            riskcompany: data.risk.riskcompany || undefined,
            riskcompliance: data.risk.riskcompliance || undefined,
            content: data.risk.content || undefined,
            bussinessid:
              (data.riskBussiness && data.riskBussiness.bussinessid) ||
              undefined,
            revisiontype: data.risk.revisiontype,
            riskextname: data.risk.riskextname,
            riskextid: data.risk.riskextid,
            copingPlot: data.copings?.copingplot,
            riskHopeValue: data.copings?.riskhopevalue,
            yddes: data.copings?.yddes,
            ydusername: data.copings.copingheadname,
            userId: data.copings.copinghead,
            copingId: data.copings.riskcopingid,
            riskcopingid: data.copings.riskcopingid,
            leadership: data.risk.leadership,
            leadershipName: data.risk.leadershipName,
            cooperateOrg: data.risk.cooperateOrg,
            cooperateOrgName: data.risk.cooperateOrgName,
            secrectLevelId: data.risk.secrectLevelId,
            staffScopeNames: data.risk.staffScopeNames,
            staffScopeIds: data.risk.staffScopeIds,
            riskcreatedt: dayjs(data.risk.riskcreatedt).format('YYYY-MM-DD'),
            staffname: data.risk.staffname || '',
            linkDeptName: data.risk.linkDeptName || '',
            unitname: data.risk.unitname || '',
            associatedRiskModel: data.risk.steptitle,
            stepid: data.risk.stepid,
          } || {}
        this.riskcopingid = data.copings?.riskcopingid
        this.sqlstr = data.risk.sql || ''
        this.bookid = data.risk.bookid || ''
        this.tableDataFile = data.attachmentList || []
        data.attachmentList.forEach((res) => {
          this.fileIdList.push(res.attid)
        })

        this.$forceUpdate()
        this.pageInit()
      },
      /**
       * @description: 删除附件
       * @return {*}
       */
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const deleteId = row.attid
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })

          await deleteById({ attid: row.attid })
          this.$forceUpdate()
          // console.log(this.tableDataFile, 'this.fileIdList')
          this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      /**
       * @description: 选择风险回调
       * @return {*}
       */
      handleFxxxSelected(val) {
        this.$set(this.formData, 'riskextname', val[0].risknumber)
        this.$set(this.formData, 'riskextid', val[0].riskid)
      },
      /**
       * @description: 处理新增/修改切换
       * @return {*}
       */
      handleRevisionTypeChange() {
        // 清空关联风险信息
        this.formData.riskextname = ''
        this.formData.riskextid = ''
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.$refs['elForm'].resetFields()
        this.formData.belongstoName = []
        this.formData = {}
        this.tableDataFile = []
        this.fileIdList = []
        this.$bus.$emit('updateMsg', 0)
      },
      close2() {
        this.dialogFormVisible2 = false
      },
      addControl(val) {
        console.log('🚀 ~ addControl ~ val:', val)
        this.controlList = [val]
        // 将一体化管控措施信息整合到formData中，但主要信息放在param对象中
        this.formData = { ...this.formData, ...val }
        this.formData.yddes = `一体化管控措施编号：${val.controlnumber}\n一体化控制目标：${val.controldes}\n控制措施：${val.conkzcs}`

        // 确保一体化管控措施信息在param对象中可用
        this.formData.controlInfo = {
          controlnumber: val.controlnumber,
          controldes: val.controldes,
          conkzcs: val.conkzcs,
          controltype: val.controltype,
          controlmethod: val.controlmethod,
          controlfrequency: val.controlfrequency,
          conmatid: val.conmatid,
        }
      },
      async save() {
        // 防止重复点击
        if (this.saveLoading) {
          return
        }

        this.saveLoading = true

        try {
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              // 检查是否有管控措施
              if (this.controlList.length === 0) {
                this.$message.error('请至少添加一条一体化管控措施')
                return
              }
            }
            console.log('data12312313123121231231231231312312')
            if (valid) {
              const { reorgName, belongstoName, riskcatname, ...data } =
                this.formData

              console.log('data12312313123121231231231231312312', data)
              // riskcatid
              let attids = ''
              if (this.fileIdList.length > 0) {
                this.fileIdList.forEach((res) => {
                  attids = attids + res + ','
                })
              }

              // 构建请求数据，按照后端期望的param格式组织
              const requestData = {
                // 外部信息与param同级
                ...data,
                riskcatid: this.formData.riskcatid,
                riskcatname: this.formData.riskcatname,
                attids,
                // param对象包含一体化管控措施的详细信息
                param:
                  this.controlList.length > 0 ? { ...this.controlList[0] } : {},
              }

              console.log('🚀 ~ save ~ requestData:', requestData)
              console.log('🚀 ~ save ~ requestData.param:', requestData.param)
              console.log('🚀 ~ save ~ controlList:', this.controlList)
              const res = await createRisk(requestData)
              if (res.code == 1) {
                this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.riskid = res.data.risk.riskid
                this.riskcatid = res.data.risk.riskcatid
                await this.getData()
              } else {
                this.close()
                this.$baseMessage(res.msg, 'error', 'vab-hey-message-error')
              }
            }
          })
        } catch (error) {
          console.error('保存失败:', error)
          this.$baseMessage(
            '保存失败，请重试',
            'error',
            'vab-hey-message-error'
          )
        } finally {
          this.saveLoading = false
        }
      },
      //流程三级关联
      async getAssociationInfo() {
        let res = await getRiskProcess()
        this.riskProcesList = res.data
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async riskProcesChange(value) {
        let res = await getRiskBusiness({ processname: value })
        this.riskBusinessList = res.data
        // this.formData.flownumber = ''
        // this.formData.risknumber = ''
        // this.formData.bussinessname = ''
        this.$forceUpdate()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async riskProcesSave(value) {
        let res = await getRiskBusiness({ processname: value })
        this.riskBusinessList = res.data
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async riskBusinessChange(value) {
        // let res = await getRiskProcessNo({ businessno: value })
        // this.formData.risknumber = res.data
        // this.formData.flownumber = value
        // this.$set(this.formData, 'bussinessname', value)
        // this.$forceUpdate()
        // console.log(this.formData.risknumber)
      },

      // 风险应对
      // 控制措施
      hanAddControl() {
        if (!this.riskcopingid) {
          this.$baseMessage(
            '请先保存基本信息',
            'error',
            'vab-hey-message-error'
          )
          return false
        }
        this.$refs.controlMeasures.showEdit(null, {
          riskcopingid: this.riskcopingid,
          riskHopeValue: this.formData.riskHopeValue,
          risknumber: this.formData.risknumber,
          riskid: this.riskid,
        })
      },
      /**
       * @description: 一体化 打开详情
       * @return {*}
       */
      showControlDetail(row) {
        this.$refs.controlMeasures.showEdit(
          row,
          {
            riskcopingid: this.riskcopingid,
            riskHopeValue: this.formData.riskHopeValue,
            risknumber: this.formData.risknumber,
          },
          true
        )
      },
      /**
       * @description: 一体化 打开编辑
       * @return {*}
       */
      handleEditControl(row) {
        this.$refs.controlMeasures.showEdit(row, {
          riskcopingid: this.riskcopingid,
          riskHopeValue: this.formData.riskHopeValue,
          riskid: this.riskid,
        })
      },
      /**
       * @description: 删除一体化管控措施
       * @return {*}
       */
      handleDeleteControl(row) {
        console.log('🚀 ~ handleDeleteControl ~ row:', row)
        if (row.conmatid) {
          controlDelete(row.conmatid).then((res) => {
            if (res.code == 200) {
              this.$baseMessage('成功', 'success')
              this.upControlData()
              this.getData()
              this.fetchData()
            }
          })
        } else {
          this.controlList = []
          this.formData.yddes = ''
          this.$message.success('成功')
        }
      },
      //操作一体化管控以后的更新
      async upControlData() {
        const { data, code, msg } = await updateControlResponseplan({
          riskid: this.riskid,
        })
      },
      async ymsubmit() {
        try {
          this.btnLoading = true
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false

          console.log('🚀 ~ ymsubmit ~ error:', error)
        }
      },

      // 部门选择
      handleCompanyTreeSelected(val) {
        if (!this.isMultiple) {
          this.$set(this.formData, 'belongstoName', val.label)
          this.$set(this.formData, 'belongsto', val.id)
        } else {
          this.$set(
            this.formData,
            'reorgName',
            val.map((v) => v.label).join(',')
          )
          this.$set(this.formData, 'reorg', val.map((v) => v.id).join(','))
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
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]

          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })

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
      handleShowRiskModel() {
        this.$refs['riskModelRef'].showEdit()
      },
      handleRiskModelSelected(val) {
        this.formData.associatedRiskModel = val[0].steptitle
        this.formData.stepid = val[0].stepid
        this.sqlstr = val[0].sqlstr
        this.bookid = val[0].bookid
      },
      check() {
        this.$refs['check'].show(this.sqlstr, this.bookid)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }
  .form-inlink {
    width: 50%;
    display: inline-block;
  }
</style>
