<!--
 * @Date: 2022-04-18 14:56:13
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-31 22:39:01
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/contractsEdit/ChangeDetailContent.vue
-->
<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="150px" :model="formData">
      <el-col :span="24">
        <el-divider>合同变更内容</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同变更类型" prop="changetype">
          {{ formData.changetype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同变更时间" prop="changedate">
          {{ formData.changedate }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="合同变更内容" prop="changedesc">
          {{ formData.changedesc }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>合同基本信息</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号" prop="contractno">
          {{ formData.contractno }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item ref="contractname" label="合同名称" prop="contractname">
          {{ formData.contractname }}
        </el-form-item>
      </el-col>
      <!-- <el-col :span="12">
        <el-form-item label="项目编号" prop="contractitem">
          {{ formData.contractitem }}
        </el-form-item>
      </el-col> -->
      <el-col :span="12">
        <el-form-item label="项目名称" prop="topicname">
          {{ formData.topicname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同类型" prop="contracttype">
          {{ formData.contracttype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="formFields['startdate'].label" prop="startdate">
          {{ formData.startdate }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="formFields['enddate'].label" prop="enddate">
          {{ formData.enddate }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item :label="formFields['budgetname'].label" prop="budgetname">
          {{ formData.budgetname }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['counterparttype']" :span="12">
        <el-form-item
          :label="formFields['counterparttype'].label"
          prop="counterparttype"
        >
          {{ formData.counterparttype }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['director']" :span="12">
        <el-form-item :label="formFields['director'].label" prop="director">
          {{ formData.director }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['counterpartaddress']" :span="12">
        <el-form-item
          :label="formFields['counterpartaddress'].label"
          prop="counterpartaddress"
        >
          {{ formData.counterpartaddress }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contacts']" :span="12">
        <el-form-item :label="formFields['contacts'].label" prop="contacts">
          {{ formData.contacts }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contactsphone']" :span="12">
        <el-form-item
          :label="formFields['contactsphone'].label"
          prop="contactsphone"
        >
          {{ formData.contactsphone }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contractbd']" :span="12">
        <el-form-item :label="formFields['contractbd'].label" prop="contractbd">
          {{ formData.contractbd }}
        </el-form-item>
      </el-col>
      <!-- <el-col v-if="formFields['contractzd']" :span="12">
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
      <el-col :span="12">
        <el-form-item label="银行账号" prop="bankaccount">
          {{ formData.bankaccount }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="开户银行" prop="">
          {{ formData.bankkhyh }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contractdatetype']" :span="12">
        <el-form-item
          :label="formFields['contractdatetype'].label"
          prop="contractdatetype"
        >
          {{ formData.contractdatetype }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contractplan']" :span="12">
        <el-form-item
          :label="formFields['contractplan'].label"
          prop="contractplan"
        >
          {{ formData.contractplan }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contractxz']" :span="12">
        <el-form-item :label="formFields['contractxz'].label" prop="contractxz">
          {{ formData.contractxz }}
        </el-form-item>
      </el-col>
      <el-col v-if="formFields['contractlink']" :span="12">
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
        :span="12"
      >
        <el-form-item :label="formFields['jijiatype'].label" prop="jijiatype">
          {{ formData.jijiatype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="我方签署主体" prop="orgname">
          {{ formData.orgname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="承办部门" prop="orgmeno">
          {{ formData.orgmeno }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="承办人" prop="realname">
          {{ formData.realname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="授权委托人" prop="entrustStaffName">
          {{ formData.entrustStaffName }}
        </el-form-item>
      </el-col>
      <!-- <el-col v-if="currentEdit !== 'moren'" :span="12"> -->
      <!-- <el-col :span="12">
        <el-form-item label="项目名称" prop="topicid">
          {{ formData.topicid }}
        </el-form-item>
      </el-col> -->
      <template v-if="formFields['contractchildren']">
        <el-col :span="12">
          <el-form-item label="是否关联合同" prop="contractchildren">
            {{ formData.contractchildren }}
          </el-form-item>
        </el-col>
        <!-- 详情关联合同的展示在底部tab的合同版本信息 -->
        <el-col v-show="formData['contractchildren'] == '是'" :span="12">
          <el-form-item label="关联合同" prop="parentname">
            {{ formData.parentname }}
          </el-form-item>
        </el-col>
      </template>
      <el-col :span="24">
        <el-form-item :label="formFields['momoconcat'].label">
          <el-input
            v-model="formData.momoconcat"
            :placeholder="`请输入${formFields['momoconcat'].label}`"
            :style="{ width: '100%' }"
            readonly
            :rows="8"
            type="textarea"
          />
        </el-form-item>
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
      <el-col :span="24">
        <el-divider>合同金额信息</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="收付方向" prop="dctype">
          {{ formData.dctype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          :label="formFields['contractmoney'].label"
          prop="contractmoney"
        >
          {{ formData.contractmoney }}
        </el-form-item>
      </el-col>
      <el-col v-if="currentEdit == 'moren'" :span="12">
        <el-form-item label="计价方式" prop="jijiatype">
          {{ formData.jijiatype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="币种" prop="moneytype">
          {{ formData.moneytype ? formData.moneytype : '人民币' }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="人民币大写" prop="hzsumowing">
          {{ formData.hzsumowing }}
        </el-form-item>
      </el-col>
      <el-col v-if="showSubFields" :span="24">
        <el-divider>{{ subTitle }}</el-divider>
        <SubList :contract="formData" :fields="subFields" :readonly="true" />
      </el-col>

      <el-col>
        <el-divider>相对方信息</el-divider>
      </el-col>
      <el-col :span="24">
        <!-- 新增可编辑表格 -->
        <el-table :data="formData.oppositeList">
          <el-table-column align="center" label="相对方类型" prop="budgettype">
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
          <el-table-column align="center" label="相对方名称" prop="budgetname">
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
      <!--
      <el-col>
        <el-divider>关联OA公文/协同列表</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="tableDataOA">
          <el-table-column align="center" label="标题" prop="subject" />
          <el-table-column align="center" label="状态" prop="stateName" />
          <el-table-column align="center" label="发送人" prop="sendName" />
          <el-table-column align="center" label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="handleDetail(scope.row)">
                详细
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col> -->
      <el-col>
        <el-divider>合同正文</el-divider>
      </el-col>
      <el-col :span="24">
        <el-form-item label-width="0" prop="describe">
          <!-- <UEditor v-model="formData.describe" disabled :height="300" /> -->
          <el-button
            class="add-btn"
            size="mini"
            type="primary"
            @click="handleOffice()"
          >
            预览合同正文
          </el-button>
          <!-- <el-button
            class="add-btn"
            size="mini"
            type="primary"
            @click="openWebOffice()"
          >
            编辑合同内容
          </el-button> -->
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24" v-if="fwType">
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
          <el-button type="primary" :disabled="!formData.contractid">
            上传
          </el-button>
        </el-upload>
      </el-col>
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
            width="70"
            prop="realname"
          />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleDownFilePDf(row)"
                size="mini"
              >
                下载
              </el-button>
              <el-button
                type="text"
                @click="handlePreviewFilePDf(row)"
                size="mini"
              >
                预览
              </el-button>
              <el-button
                v-if="!isDialog && fwType"
                type="text"
                @click="handleDeleteFilePDF(row, $index)"
                size="mini"
              >
                删除
              </el-button>
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
          <el-tab-pane v-if="showPlanList" label="合同履行阶段" name="first">
            <JieduanList :contract="formData" :readonly="true" />
          </el-tab-pane>
          <!-- <el-tab-pane label="相对方信息" name="second">
            <OppositeInfo :list="formData.oppositeList" />
          </el-tab-pane> -->
          <el-tab-pane label="盖章文件信息" name="third">
            <SigningList
              :list="formData.signingList"
              :readonly="true"
              :att-type="1"
            />
          </el-tab-pane>
          <el-tab-pane label="合同附件" name="fourth">
            <AttachList
              :att-list="formData.attList"
              :readonly="true"
              :att-type="2"
            />
          </el-tab-pane>
          <el-tab-pane
            v-if="formData.dctype == '收款'"
            label="收款管理"
            name="fifth"
          >
            <CollectionInfo :list="formData.colList" />
          </el-tab-pane>
          <el-tab-pane v-else label="付款管理" name="fifth">
            <CollectionInfo :list="formData.payList" />
          </el-tab-pane>
          <el-tab-pane label="合同版本信息" name="sixth">
            <RelatedContractInfo :list="formData.parentList" />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-form>
    <TextEditor ref="webOffice" />
  </el-row>
</template>

<script>
  import UEditor from '@/components/UEditor'
  import AttachList from '../AttachList.vue'
  import JieduanList from '../block/JieduanList.vue'
  import SubList from '../block/SubList.vue'
  import CollectionInfo from '../CollectionInfo.vue'
  import OppositeInfo from '../OppositeInfo.vue'
  import RelatedContractInfo from '../RelatedContractInfo.vue'
  import SigningList from '../SigningList.vue'
  import TextEditor from './TextEditor.vue'
  import {
    getPrivewAttInfo,
    downContractPdf,
    deleteContractPdf,
    deleteContractExam,
    downContractExam,
    deleteAttach,
    downloadOldpdfFile,
  } from '@/api/contract/manage'
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
      CollectionInfo,
      UEditor,
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
      showSubFields: {
        type: Boolean,
        default: false,
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
      },
    },
    mounted() {},
    methods: {
      //上传前置
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
      //删除
      async handleDeleteFilePDF(list, index) {
        await deleteContractPdf({ contentPdfId: list.contentPdfId })
        this.pdfList.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      //删除
      async handleDeleteFilePDF2(list, index) {
        await deleteContractExam({ id: list.id })
        this.pdfList2.splice(index, 1)
        this.$forceUpdate()
        this.$baseMessage('删除成功', 'success')
      },
      //office
      openWebOffice() {
        this.$store.commit('acl/contractidd', this.formData.contractid)
        this.$refs['webOffice'].show()
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
      //回调
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
  .el-divider {
    margin-top: 10px;
  }
</style>
