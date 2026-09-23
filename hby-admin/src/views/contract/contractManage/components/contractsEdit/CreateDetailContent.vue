<!--
 * @Date: 2022-04-18 14:29:10
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-29 21:30:53
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/contractsEdit/CreateDetailContent.vue
-->
<template>
  <el-row :gutter="10">
    <div class="myTableStyle">
      <el-form
        ref="form"
        :label-width="$store.state.work.processMobile ? '120px' : '150px'"
        :model="formData"
        style="padding: 0 10px"
      >
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <div class="mycol">
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="合同编号" prop="contractno">
              {{ formData.contractno }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item
              ref="contractname"
              label="合同名称"
              prop="contractname"
            >
              {{ formData.contractname }}
            </el-form-item>
          </el-col>
          <!-- <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="项目编号" prop="contractitem">
              {{ formData.contractitem }}
            </el-form-item>
          </el-col> -->
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="项目名称" prop="topicid">
              {{ formData.topicname }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="合同类型" prop="contracttype">
              {{ formData.contracttype }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="合同分类" prop="typefl">
              {{ formData.typefl }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['startdate'].label"
              prop="startdate"
            >
              {{ formData.startdate }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item :label="formFields['enddate'].label" prop="enddate">
              {{ formData.enddate }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="事项审议机构" prop="matterorg">
              {{ formData.matterorg }}
            </el-form-item>
          </el-col>
          <el-col
            :lg="12"
            :md="12"
            :sm="24"
            v-if="formData.matterorg == '董事会'"
          >
            <el-form-item label="是否三重一大事项" prop="isbigmatter">
              {{ formData.isbigmatter }}
            </el-form-item>
          </el-col>
          <!-- <el-col :lg="12" :md="12" :sm="24">
            <el-form-item :label="formFields['budgetname'].label" prop="budgetname">
              {{ formData.budgetname }}
            </el-form-item>
          </el-col> -->
          <el-col
            v-if="formFields['counterparttype']"
            :lg="12"
            :md="12"
            :sm="24"
          >
            <el-form-item
              :label="formFields['counterparttype'].label"
              prop="counterparttype"
            >
              {{ formData.counterparttype }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['director']" :lg="12" :md="12" :sm="24">
            <el-form-item :label="formFields['director'].label" prop="director">
              {{ formData.director }}
            </el-form-item>
          </el-col>
          <el-col
            v-if="formFields['counterpartaddress']"
            :lg="12"
            :md="12"
            :sm="12"
          >
            <el-form-item
              :label="formFields['counterpartaddress'].label"
              prop="counterpartaddress"
            >
              {{ formData.counterpartaddress }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contacts']" :lg="12" :md="12" :sm="24">
            <el-form-item :label="formFields['contacts'].label" prop="contacts">
              {{ formData.contacts }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contactsphone']" :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contactsphone'].label"
              prop="contactsphone"
            >
              {{ formData.contactsphone }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractbd']" :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contractbd'].label"
              prop="contractbd"
            >
              {{ formData.contractbd }}
            </el-form-item>
          </el-col>
          <!-- <el-col v-if="formFields['contractzd']" :lg="12" :md="12" :sm="24">
            <el-form-item :label="formFields['contractzd'].label" prop="contractzd">
              {{
                formData.contractzd == "1"
                  ? "一般"
                  : formData.contractzd == "2"
                  ? "重要"
                  : "重大"
              }}
            </el-form-item>
          </el-col> -->
          <!-- <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="银行账号" prop="bankaccount">
              {{ formData.bankaccount }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="开户银行" prop="">
              {{ formData.bankkhyh }}
            </el-form-item>
          </el-col> -->
          <el-col
            v-if="formFields['contractdatetype']"
            :lg="12"
            :md="12"
            :sm="24"
          >
            <el-form-item
              :label="formFields['contractdatetype'].label"
              prop="contractdatetype"
            >
              {{ formData.contractdatetype }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractplan']" :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contractplan'].label"
              prop="contractplan"
            >
              {{ formData.contractplan }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractxz']" :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contractxz'].label"
              prop="contractxz"
            >
              {{ formData.contractxz }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractlink']" :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contractlink'].label"
              prop="contractlink"
            >
              {{ formData.contractlink }}
            </el-form-item>
          </el-col>
          <!-- jijiatype字段在金额信息部分也有用，在非默认模板情况下才用在合同基本信息 -->
          <el-col
            v-if="currentEdit !== 'moren' && formFields['jijiatype']"
            :lg="12"
            :md="12"
            :sm="12"
          >
            <el-form-item
              :label="formFields['jijiatype'].label"
              prop="jijiatype"
            >
              {{ formData.jijiatype }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="我方签署主体" prop="orgname">
              {{ formData.orgname }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="承办部门" prop="orgmeno">
              {{ formData.orgmeno }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="承办人" prop="realname">
              {{ formData.realname }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="授权委托人" prop="entrustStaffName">
              {{ formData.entrustStaffName }}
            </el-form-item>
          </el-col>
          <!-- <el-col v-if="currentEdit !== 'moren'" :lg="12" :md="12" :sm="24"> -->
          <!-- <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="项目名称" prop="topicid">
              {{ formData.topicname }}
            </el-form-item>
          </el-col> -->
          <template v-if="formFields['contractchildren']">
            <el-col :lg="12" :md="12" :sm="24">
              <el-form-item label="是否关联合同" prop="contractchildren">
                {{ formData.contractchildren }}
              </el-form-item>
            </el-col>
            <!-- 详情关联合同的展示在底部tab的合同版本信息 -->
            <el-col
              v-show="formData['contractchildren'] == '是'"
              :lg="12"
              :md="12"
              :sm="12"
            >
              <el-form-item label="关联合同" prop="parentname">
                {{ formData.parentname }}
              </el-form-item>
            </el-col>
          </template>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="是否为多个合同" prop="ismany">
              {{ formData.ismany }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24" v-if="formData.ismany == '是'">
            <el-form-item label="多个合同数量" prop="agreementcount">
              {{ formData.agreementcount }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <div class="textareaOther">
              <el-form-item :label="formFields['momoconcat'].label">
                <!-- <el-input
                v-model="formData.momoconcat"
                :style="{ width: '100%' }"
                readonly
                :rows="8"
                type="textarea"
              /> -->
                {{ formData.momoconcat }}
              </el-form-item>
            </div>
          </el-col>
          <el-col v-if="formFields['riskcontrol']" :span="24">
            <el-form-item :label="formFields['riskcontrol'].label">
              <el-input
                v-model="formData.riskcontrol"
                :autosize="{ minRows: 4, maxRows: 4 }"
                :placeholder="`请输入${formFields['riskcontrol'].label}`"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </div>
        <el-col :span="24">
          <el-divider>合同金额信息</el-divider>
        </el-col>
        <div class="mycol">
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="收付方向" prop="dctype">
              {{ formData.dctype }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item
              :label="formFields['contractmoney'].label"
              prop="contractmoney"
            >
              {{ formData.contractmoney }}
            </el-form-item>
          </el-col>
          <el-col v-if="currentEdit == 'moren'" :lg="12" :md="12" :sm="24">
            <el-form-item label="计价方式" prop="jijiatype">
              {{ formData.jijiatype }}
            </el-form-item>
          </el-col>
          <el-col :lg="12" :md="12" :sm="24">
            <el-form-item label="币种" prop="moneytype">
              {{ formData.moneytype }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="人民币大写" prop="hzsumowing">
              {{ formData.hzsumowing }}
            </el-form-item>
          </el-col>
          <el-col v-if="showSubFields" :span="24">
            <el-divider>{{ subTitle }}</el-divider>
            <SubList
              :contract="formData"
              :fields="subFields"
              :readonly="true"
              :show-total="formData.contracttype == '产品销售合同'"
            />
          </el-col>
        </div>
        <el-col>
          <el-divider>相对方信息</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- 新增可编辑表格 -->
          <el-table :data="formData.oppositeList">
            <el-table-column
              align="center"
              label="相对方类型"
              prop="budgettype"
            >
              <!-- <template slot-scope="scope">
                  <el-select disabled placeholder="请选择" v-model="scope.row.budgettype">
                    <el-option
                      v-for="item in SSoptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </template> -->
            </el-table-column>
            <el-table-column
              align="center"
              label="相对方名称"
              prop="budgetname"
            >
              <!-- <template slot-scope="scope">
                  <div style="margin-left: 40px; display: flex">
                    <el-input
                      disabled
                      v-model="scope.row.budgetname"
                      style="width: 60%; margin-left: 40px"
                    />
                    <el-button
                      :style="{ marginLeft: '10px' }"
                      type="primary"
                      @click="
                        $refs.xdfINfo.show('HTGL001', 'budgetname', scope.$index)
                      "
                    >
                      选择
                    </el-button>
                  </div>
                </template> -->
            </el-table-column>

            <!-- <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button type="text" @click="handleDelete(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column> -->
          </el-table>
        </el-col>

        <!-- <el-col>
          <el-divider>关联OA公文/协同列表</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableDataOA">
            <el-table-column align="center" label="标题" prop="subject" />
            <el-table-column align="center" label="类别" prop="cateGoryName">
              <template slot-scope="scope">
                <span v-if="scope.row.cateGoryName == 'edoc'">公文</span>
                <span v-if="scope.row.cateGoryName == 'col'">协同</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="发送人" prop="sendName" />
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" @click="handleDetail(scope.row)">
                  详细
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>上传审批附件</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
            :readonly="true"
            :att-type="2"
          />
        </el-col> -->
        <el-col :span="24">
          <el-divider>合同正文</el-divider>
        </el-col>
        <el-col :span="4" style="margin-top: 20px">
          <el-button
            class="add-btn"
            size="mini"
            type="primary"
            @click="handleOffice()"
            v-if="!$store.state.work.processMobile"
          >
            预览合同正文
          </el-button>
        </el-col>
        <!-- <el-col :span="4" style="margin-top: 20px">
          <el-button
            class="add-btn"
            size="mini"
            type="primary"
            @click="openWebOffice()"
            v-if="!$store.state.work.processMobile"
          >
            编辑合同内容
          </el-button>
        </el-col> -->
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <!-- <el-col :span="24">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api + '?contractId=' + formData.contractid"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="pdfList"
              :before-upload="handleBeforeUpload"
              style="text-align: right"
            >
              <el-button type="primary" :disabled="!formData.contractid">上传</el-button>
            </el-upload>
          </el-col> -->
        <el-col :span="24">
          <el-table :data="pdfList">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            ></el-table-column>
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="contentPdfName"
            />
            <el-table-column
              align="center"
              label="上传人"
              prop="realname"
              width="70"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  v-if="row.oaattid"
                  type="text"
                  @click="handleDown(row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handleDownFilePDf(row)"
                  size="mini"
                  v-if="!row.oaattid"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf(row)"
                  size="mini"
                  v-if="!row.oaattid"
                >
                  预览
                </el-button>
                <!-- <el-button
                    type="text"
                    @click="handleDeleteFilePDF(row, $index)"
                    size="mini"
                  >
                    删除
                  </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>审核合同文本</el-divider>
        </el-col>
        <el-col :span="24" v-if="fwryType">
          <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api2 + '?contractId=' + formData.contractid"
            :headers="headers"
            :on-preview="handlePreview"
            :on-success="handleSuccess2"
            :file-list="pdfList2"
            :before-upload="handleBeforeUpload"
            style="text-align: right"
          >
            <el-button type="primary" :disabled="!formData.contractid">
              上传
            </el-button>
          </el-upload>
        </el-col>
        <el-col :span="24">
          <el-table :data="pdfList2">
            <el-table-column
              align="center"
              label="序号"
              type="index"
              width="50"
            ></el-table-column>
            <el-table-column
              align="center"
              label="合同文件名称"
              prop="fileName"
            />
            <el-table-column
              align="center"
              label="审核人"
              prop="uploaderName"
              width="70"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleDownFilePDf2(row)"
                  size="mini"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFilePDf2(row)"
                  size="mini"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteFilePDF2(row, $index)"
                  size="mini"
                  v-if="fwryType"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->

        <el-col :span="24">
          <el-tabs v-model="activeName" type="card">
            <el-tab-pane
              v-if="showPlanList && formData.contractplan === '是'"
              label="合同履行阶段"
              name="first"
            >
              <JieduanList
                v-if="Jie"
                :contract="formData"
                :readonly="true"
                @data-change="formData.nodeList = $event"
              />
            </el-tab-pane>

            <el-tab-pane label="盖章文件信息" name="third">
              <SigningList
                :list="formData.signingList"
                :readonly="true"
                :att-type="1"
              />
            </el-tab-pane>
            <template v-if="!$store.state.work.processMobile">
              <el-tab-pane
                v-if="formData.dctype == '收款'"
                label="收款管理"
                name="fifth"
              >
                <CollectionInfo :type="'收款'" :list="formData.colList" />
              </el-tab-pane>
              <el-tab-pane v-else label="付款管理" name="fifth">
                <CollectionInfo :type="'付款'" :list="formData.payList" />
              </el-tab-pane>
            </template>
            <el-tab-pane
              label="合同版本信息"
              name="sixth"
              v-if="!$store.state.work.processMobile"
            >
              <RelatedContractInfo :list="formData.parentList" />
            </el-tab-pane>
            <el-tab-pane
              label="合同履行落实"
              name="seven"
              v-if="showPlanList && formData.contractplan === '是'"
            >
              <practicableList
                ref="practicableList"
                :formData="formData"
                :disabled="true"
              />
            </el-tab-pane>
            <!-- <el-tab-pane label="展示水印合同" name="watermark">
              <WatermarkInfo :list="formData.watermarkList" />
            </el-tab-pane> -->
          </el-tabs>
        </el-col>
      </el-form>
    </div>
    <TextEditor ref="webOffice" />
  </el-row>
</template>

<script>
  import {
    getPrivewAttInfo,
    downContractPdf,
    deleteContractPdf,
    deleteContractExam,
    downContractExam,
    deleteAttach,
    downloadOldpdfFile,
  } from '@/api/contract/manage'
  import { formatDate } from '@/utils/index'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'
  import practicableList from '../options/practicableList.vue'
  import UEditor from '@/components/UEditor'
  import WebOffice from '@/components/WebOffice'
  import AttachList from '../AttachList.vue'
  import JieduanList from '../block/JieduanList.vue'
  import SubList from '../block/SubList.vue'
  import CollectionInfo from '../CollectionInfo.vue'
  import OppositeInfo from '../OppositeInfo.vue'
  import RelatedContractInfo from '../RelatedContractInfo.vue'
  import SigningList from '../SigningList.vue'
  import WatermarkInfo from '../WatermarkInfo.vue'
  import TextEditor from './TextEditor.vue'
  import oaList from '../options/oaList.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'

  export default {
    components: {
      SubList,
      JieduanList,
      AttachList,
      SigningList,
      OppositeInfo,
      RelatedContractInfo,
      WatermarkInfo,
      CollectionInfo,
      oaList,
      UEditor,
      WebOffice,
      practicableList,
      TextEditor,
    },
    props: {
      formFields: {
        type: Object,
        default: () => {},
      },
      form: {
        type: Object,
        default: () => {},
      },
      currentEdit: {
        type: String,
        default: 'moren',
      },
      subFields: {
        type: Array,
        default: () => [],
      },
      pdfList: {
        type: Array,
        default: () => [],
      },
      pdfList2: {
        type: Array,
        default: () => [],
      },
      tableDataOA: {
        type: Array,
        default: () => [],
      },
      showSubFields: {
        type: Boolean,
        default: false,
      },
      subTitle: {
        type: String,
        default: '',
      },
      oaurl: {
        type: String,
        default: '',
      },
      ticket: {
        type: String,
        default: '',
      },
      fwType: {
        type: Boolean,
        default: false,
      },
      fwryType: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/contract/importContractPdf',
        api2: '/contract/importContractExam',
        headers: {
          token: store.getters['user/token'],
        },
        activeName: 'first',
        formData: this.form,
        // tableDataOA: this.tableDataOA,
        Jie: true,

        localList: [], // 本地缓存新增的附件列表
      }
    },
    computed: {
      showPlanList() {
        if (this.currentEdit !== 'moren') {
          if (this.currentEdit == 'zhanlue') {
            return false
          }
          return true
        } else {
          if (this.formData.contractplan) {
            return true
          }
          return false
        }
      },
    },
    watch: {
      form(val) {
        this.formData = val
        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      activeName(val) {
        if (val) {
          if (val == 'seven') {
            console.log('zzz', val)
            this.$refs.practicableList.show(this.formData, 'track')
          } else {
            this.$refs.practicableList.clear()
          }
        }
      },
      'formData.contractname'(val) {
        if (val) {
          this.$nextTick(() => {
            setTimeout(() => {
              this.syncInputHeight()
            }, 100)
          })
        }
      },
      tableDataOA: {
        immediate: true,
        handler(newValue, oldValue) {
          this.tableDataOA = newValue
        },
      },
    },
    mounted() {},
    methods: {
      clear() {
        this.activeName = 'first'
      },
      //过滤
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      //上传前置校验
      handleBeforeUpload(file) {
        // if (this.pdfList.length > 0) {
        //   this.$baseMessage('只能上传一个文件,请先删除！', 'error')
        //   return false
        // }
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handlePreview(file) {},
      //回调
      handleSuccess(file) {
        if (file.code == '1') {
          this.pdfList.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //回调
      handleSuccess2(file) {
        if (file.code == '1') {
          this.pdfList2.push(file.data)
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //回调
      async handleDeleteFilePDF(list, index) {
        await deleteContractPdf({ contentPdfId: list.contentPdfId })
        this.pdfList.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      //回调
      async handleDeleteFilePDF2(list, index) {
        await deleteContractExam({ id: list.id })
        this.pdfList2.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      //office
      openWebOffice() {
        this.$store.commit('acl/contractidd', this.formData.contractid)
        this.$refs['webOffice'].show(1)
      },
      async handleDown(row) {
        const res = await downloadOldpdfFile({ contentPdfId: row.contentPdfId })
        window.open(res.url)
      },
      async handleDownFilePDf(list) {
        const data = await downContractPdf({
          contentPdfId: list.contentPdfId,
        })
        let filename = list.contentPdfName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/pdf',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      //回调
      async handleDownFilePDf2(list) {
        const data = await downContractExam({
          id: list.id,
        })
        let filename = list.fileName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          // type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handlePreviewFilePDf(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.contentPdfId,
          attType: 4,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async handlePreviewFilePDf2(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.id,
          attType: 5,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        if (this.$store.state.work.processMobile) {
          window.open(`${this.oaurl}${row.h5url}&ticket=${this.ticket}`)
        } else {
          window.open(`${this.oaurl}${row.url}&ticket=${this.ticket}`)
        }
      },
      async handleDeleteAtt(row, index) {
        const { code } = await deleteAttach({
          attid: row.attid,
        })
        if (code == 1) {
          if (row.type == 'local') {
            this.localList.splice(index - this.formData.attList.length, 1)
          } else {
            this.formData.attList.splice(index, 1)
          }
        }
      },
      //回调
      handleUploadSuccess(val) {
        this.localList.push(val.data)
      },
      // 同步每一行最高，为了省事，懒得写css
      syncInputHeight() {
        const _mycol = document.getElementsByClassName('mycol')
        const _elFormItems = _mycol[0].getElementsByClassName(
          'el-form-item__content'
        )
        let first = true,
          cacheFirst = 0
        _elFormItems.forEach((x, i) => {
          if (i === _elFormItems.length - 1) return
          const curH = _elFormItems[i].clientHeight
          if (first === true) {
            cacheFirst = curH
            first = false
          } else {
            const m = curH - cacheFirst
            if (m > 0) {
              _elFormItems[i - 1].style.height = curH + 'px'
            } else {
              _elFormItems[i].style.height = cacheFirst + 'px'
            }
            first = true
            cacheFirst = 0
          }
        })
      },
      //office
      handleOffice() {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        window.open(
          `https://office.wenxin.example.com/api/office/getContractReview?fileType=word&contractId=${this.formData.contractid}&uid=${info.staffid}&name=${info.username}`
        )
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }

  :deep .myTableStyle .el-col {
    padding: 0 !important;
  }
  :deep .myTableStyle .el-divider {
    margin-top: 30px;
  }
  :deep .mycol {
    display: flow-root;
    border: 1px solid rgba(0, 0, 0, 0.4);
  }
  :deep .mycol .el-col .el-form-item {
    margin-bottom: 0 !important;
    margin-left: -1px;
    margin-top: -1px;
    margin-right: -1px;
    border-top: 1px solid rgba(0, 0, 0, 0.4);
    border-bottom: 1px solid rgba(0, 0, 0, 0.4);
    border-right: 1px solid rgba(0, 0, 0, 0.4);
  }

  :deep .mycol .el-col:last-child .el-form-item {
    border-bottom: 0;
  }

  :deep .mycol .el-col .el-form-item__label {
    padding: 8px;
  }
  :deep .mycol .el-col .el-form-item__content {
    margin-bottom: 0 !important;
    padding: 8px 4px 4px 10px;
    border-left: 1px solid rgba(0, 0, 0, 0.4);
  }

  /* .textareaOther /deep/ .el-textarea__inner {
    outline: none;
    border: none;
    resize: none;
    box-shadow: none
  } */
</style>
