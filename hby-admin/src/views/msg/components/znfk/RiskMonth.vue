<template>
  <div>
    <el-row :gutter="15">
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
                  @click="$refs.selectFxxx.showEdit(formData.riskcatid)"
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
            <el-form-item
              label="公司责任领导"
              prop="leadershipName"
              class="form-inlink"
            >
              <el-input
                placeholder="请选择公司责任领导"
                v-model="formData.leadershipName"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="handleShowUser('leadershipName')"
                :disabled="formDisabled"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
            </el-form-item>
            <el-form-item label="配合单位或部门" class="form-inlink">
              <el-input
                placeholder="请选择配合单位或部门"
                v-model="formData.cooperateOrgName"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="handleShowCompent('ph')"
                :disabled="formDisabled"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
            </el-form-item>
            <!-- <el-col :span="12"> -->
            <!-- <el-form-item class="form-inlink" label="相关部门" prop="reorgName">

              <el-input
                v-model="formData.reorgName"
                disabled
                style="width: 75%; margin-right: 8px"
              ></el-input>
              <el-button
                type="primary"
                @click="handleShowCompent('xg')"
                :disabled="formDisabled"
              >
                选择
              </el-button>
            </el-form-item> -->

            <!-- </el-col> -->
            <!-- <el-col :span="12"> -->

            <!-- </el-col> -->
            <!-- <el-col :span="24"> -->
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
            <el-form-item
              class="form-inlink"
              label="编码规则"
              prop="risknumber"
            >
              <el-input v-model="formData.risknumber" disabled></el-input>
            </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="12"> -->

            <!-- </el-col> -->
            <!-- <el-col :span="12"> -->

            <el-form-item
              class="form-inlink"
              label="风险领域"
              prop="riskcatname"
            >
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
              <el-form-item label="应对方案" prop="yddes">
                <el-input
                  v-model="formData.yddes"
                  :autosize="{ minRows: 4, maxRows: 8 }"
                  placeholder="请输入应对方案"
                  :style="{ width: '100%' }"
                  type="textarea"
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-divider>一体化管控措施</el-divider>
            </el-col>
            <el-col :span="24" style="margin-bottom: 20px">
              <div
                style="
                  text-align: right;
                  margin-bottom: 5px;
                  margin-right: 10px;
                "
              >
                <el-button
                  type="success"
                  @click="hanAddControl"
                  :disabled="formDisabled"
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
                <el-table-column
                  align="center"
                  label="操作"
                  v-if="!formDisabled"
                >
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
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <el-upload
              class="upload-demo"
              :action="baseApi + api"
              :data="{
                moduleType: 'FXCJ',
                moduleId: this.formData.riskid || '',
              }"
              :headers="headers"
              :on-success="handleSuccess"
              :show-file-list="false"
              multiple
            >
              <!-- :file-list="fileList" -->
              <div style="margin-right: 10px">
                <el-button type="success">上传</el-button>
              </div>
            </el-upload>
          </div>

          <el-table :data="tableDataFile">
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
                <el-button type="text" @click="handleDownload(row)">
                  下载
                </el-button>
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
    </el-row>

    <div slot="footer" style="text-align: right" v-if="!formDisabled">
      <el-button @click="save" type="primary">确定</el-button>
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
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
    <rules ref="rulesRef" @setRules="setRules" />
    <selectFxxx ref="selectFxxx" @selected="handleFxxxSelected" />
    <controlMeasures ref="controlMeasures" @fetch-data="getData" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import BpmnModeler from '@/components/bpmnjs/BpmnModeler'
  import controlMeasures from '@/views/risk/treatment/controlMeasures.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
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
    },
    props: ['riskcatid', 'treeId'],
    data() {
      return {
        baseApi: baseURL,
        // api: '/audit/fileManage/upload',
        api: '/riskcontrol/attachment/uploadFileAttInfo',
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
      /**
       * @description: 文件上传成功
       * @return {*}
       */
      handleSuccess(e) {
        console.log(e)
        if (e.code === 200) {
          let attInfo = {}
          attInfo = e.data
          this.fileIdList.push(attInfo.attid)
          this.tableDataFile.push(attInfo)
          console.log(this.tableDataFile)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
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
          this.$set(this.formData, 'cooperateOrgName', e.name)
          this.$set(this.formData, 'cooperateOrg', e.id)
          // this.$set(this.formData, 'reorgName', e.map((v) => v.name).join('、'))
          // this.$set(this.formData, 'reorg', e.map((v) => v.id).join(','))
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
      handleShowUser() {
        this.type = type
        this.$refs['userTreeRef'].show()
      },
      handleShowCompent(type) {
        this.bmType = type
        let checkbox = false

        if (this.formData.reorg) {
          var arr = this.formData.reorg.split(',')
        }
        this.$refs['comTreeRef'].show(checkbox, arr)
      },
      /**
       * @description: 下载
       * @return {*}
       */
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 预览
       * @return {*}
       */
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
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
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
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
        console.log('🚀 ~ showEdit ~ formId:', formId)
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
        if (data.risk.secrectLevelId) {
          localStorage.setItem('SPsecrectLevelId', data.risk.secrectLevelId)
        }
        await this.fetchData()
        this.riskProcesSave(data.flow && data.flow.flowname)
        this.formData =
          {
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
          } || {}
        this.riskcopingid = data.copings?.riskcopingid

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
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { reorgName, belongstoName, riskcatname, ...data } =
              this.formData

            console.log('data', data)
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
              // param对象为空，因为RiskMonth没有一体化管控措施
              param: {},
            }

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
        this.loading = true
        controlDelete(row.conmatid)
          .then((res) => {
            if (res.code == 200) {
              this.$baseMessage('成功', 'success')
              this.upControlData()
              this.getData()
              this.fetchData()
            }
          })
          .finally(() => {
            this.loading = false
          })
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
