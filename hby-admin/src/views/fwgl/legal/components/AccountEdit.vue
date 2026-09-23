<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row v-if="show == 1" :gutter="15">
        <el-form ref="form" label-width="140px" :model="form" :rules="rules">
          <el-col :span="12">
            <el-form-item label="登记编号" prop="porceedstage">
              <span>{{ form.disputeno }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纠纷名称" prop="disputeitem">
              <span>{{ form.disputeitem }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纠纷类型" prop="porceedstage">
              <span>{{ form.disputetype }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="合同名称"
              prop="contractname"
              v-if="form.glht == 1"
            >
              <span>{{ form.contractname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同编号">
              <span>{{ form.contractno }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同执行人">
              <span>{{ form.jbstaff }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原告" prop="porceedstage">
              <span>{{ form.plaintiff }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被告" prop="porceedstage">
              <span>{{ form.defendant }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="争议焦点" prop="porceedstage">
              <span>{{ form.disputecours }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司经办人" prop="porceedstage">
              <span>{{ form.zxstaffname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否紧急事项">
              <span>{{ form.isUegent == 1 ? '是' : '否' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="诉讼地位" prop="whethersued">
              <span>
                {{
                  form.whethersued == 1
                    ? '原告'
                    : form.whethersued == 2
                    ? '被告'
                    : form.whethersued == 5
                    ? '第三人'
                    : ''
                }}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计办结时间" prop="enddate1">
              <span>{{ form.enddate1 }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="初步解决建议">
              <span>{{ form.solutionsuggestions }}</span>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>我方代理人</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table
              :data="dllsData"
              style="width: 100%; margin: 10px 0 50px 0%"
            >
              <el-table-column
                align="center"
                prop="isattorney"
                label="是否外聘"
              >
                <template #default="{ row }">
                  {{ row.isattorney === 1 ? '是' : '否' }}
                </template>
              </el-table-column>
              <el-table-column align="center" prop="attorney" label="代理人" />
              <el-table-column
                align="center"
                prop="attorneyphont"
                label="联系方式"
              />
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <el-tabs
        v-model="activeName"
        class="demo-tabs"
        type="card"
        @tab-click="handleClick"
      >
        <el-tab-pane label="协商信息" name="first">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listXS"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="对方谈判人"
                prop="counterpart"
              />
              <el-table-column
                align="center"
                label="对方谈判人联系电话"
                prop="counterpartphone"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('consultEdit', row)"
                  >
                    {{ row.counterpartphone }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="法院名称"
                prop="courtname"
              />
              <el-table-column
                align="center"
                label="解决方式"
                prop="solutionmode"
              >
                <template slot-scope="scope">
                  <span>
                    {{ scope.row.solutionmode === 1 ? '私下调解' : '司法调解' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="是否协商一致"
                prop="isaggree"
              >
                <template slot-scope="scope">
                  <span>
                    {{ scope.row.isaggree === 1 ? '是' : '否' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建人"
                prop="createtime"
              />
              <el-table-column
                align="center"
                label="创建日期"
                prop="createtime"
              />
            </el-table>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="诉讼过程" name="second">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listSS"
              style="margin-bottom: 20px"
            >
              <el-table-column align="center" label="填报单位" prop="fillunit">
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('lawsuitEdit', row)"
                  >
                    {{ row.fillunit }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="案由" prop="causecase" />
              <el-table-column
                align="center"
                label="预计办结时间"
                prop="lastdealdate"
              />
              <el-table-column
                align="center"
                label="纠纷类型"
                prop="disputetype"
              />
              <!-- <el-table-column
                align="center"
                label="标的额（万元）"
                prop="subjectamount"
              /> -->
            </el-table>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="仲裁过程" name="third">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listZC"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="案件名称"
                prop="courtfirst"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('arbitrationEdit', row)"
                  >
                    {{ row.preservednature }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="仲裁受理日期"
                prop="createtime"
              />
              <el-table-column
                align="center"
                label="仲裁首次开庭日期"
                prop="asfirsthearingdate"
              />
              <el-table-column
                align="center"
                label="仲裁结案日期"
                prop="arbitrationenddate"
              />
              <el-table-column
                align="center"
                label="仲裁结果"
                prop="arbitrationresult"
              />
              <el-table-column
                align="center"
                label="仲裁金额"
                prop="arbitrationamount"
              />
            </el-table>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="执行管理" name="fourth">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listZX"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="纠纷名称"
                prop="disputename"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleDetail1('', row)">
                    {{ row.disputename }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="执行案号" prop="execuno" />
              <el-table-column
                align="center"
                label="执行法院"
                prop="execucourt"
              />
              <el-table-column
                align="center"
                label="执行方式"
                prop="executype"
              />
              <el-table-column
                align="center"
                label="执行总金额(万元)"
                prop="execuamount"
              />
            </el-table>
          </el-col>
        </el-tab-pane>
        <!-- <el-tab-pane label="资产保全" name="fourth">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listBQ"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="纠纷主题"
                prop="disputeitem"
              />
              <el-table-column
                align="center"
                label="保全资产性质"
                prop="preservednature"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('preserveEdit', row)"
                  >
                    {{ row.preservednature }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="是否申请保全"
                prop="applypreservation"
              >
                <template slot-scope="scope">
                  <span>
                    {{ scope.row.applypreservation === 1 ? '是' : '否' }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="是否执行扣划"
                prop="isperformed"
              >
                <template slot-scope="scope">
                  <span>{{ scope.row.isperformed === 1 ? '是' : '否' }}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="是否接触保全"
                prop="iscancel"
              >
                <template slot-scope="scope">
                  <span>{{ scope.row.iscancel === 1 ? '是' : '否' }}</span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="(被)保全资产数额(万元)"
                prop="preservedamount"
              />
              <el-table-column
                align="center"
                label="(被)执金额(万元)"
                prop="exceteamount"
              />
            </el-table>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="账户冻结" name="fifth">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listDJ"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="被冻结账户开户行"
                prop="frozenblank"
              />
              <el-table-column
                align="center"
                label="被冻结账户账号"
                prop="frozenaccount"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('freezeEdit', row)"
                  >
                    {{ row.frozenaccount }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="诉讼阶段"
                prop="porceedstage"
              />
              <el-table-column
                align="center"
                label="冻结起始日"
                prop="startdate"
              />
              <el-table-column
                align="center"
                label="冻结期届满日"
                prop="enddate"
              />
              <el-table-column
                align="center"
                label="申请冻结金额（元）"
                prop="applyamount"
              />
              <el-table-column
                align="center"
                label="实际被冻结金额（元）"
                prop="frozenamount"
              />
              <el-table-column
                align="center"
                label="被扣划金额（元）"
                prop="kouhuaamount"
              />
            </el-table>
          </el-col>
        </el-tab-pane>
        <el-tab-pane label="结案信息" name="sixth">
          <el-col :span="24">
            <el-table
              v-loading="listLoading"
              :data="listJA"
              style="margin-bottom: 20px"
            >
              <el-table-column
                align="center"
                label="结项时间"
                prop="closedate"
              />
              <el-table-column
                align="center"
                label="纠纷主题"
                prop="disputeitem"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('closeEdit', row)"
                  >
                    {{ row.disputeitem }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="	录入人" prop="realname" />
              <el-table-column
                align="center"
                label="判决金额"
                prop="judgementamount"
              />
              <el-table-column
                align="center"
                label="结项处理结果"
                prop="closeresult"
              />
            </el-table>
          </el-col>
        </el-tab-pane> -->
        <el-tab-pane label="附件信息" name="seven">
          <el-col :span="24">
            <el-table :data="listAM">
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreviewFile(row)">
                    预览
                  </el-button>
                  <el-button type="text" @click="downloadData(row)">
                    下载
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <!-- <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button> -->
      </template>
      <ssjd-options ref="ssjd" @selected="handleSsjd" />
    </el-dialog>
    <ConsultEdit ref="consultEdit" />
    <LawsuitEdit ref="lawsuitEdit" />
    <ArbitrationEdit ref="arbitrationEdit" />
    <preserveEdit ref="preserveEdit" />
    <FreezeEdit ref="freezeEdit" />
    <CloseEdit ref="closeEdit" />
    <zxglEdit ref="zxglAddRef" />
  </div>
</template>

<script>
  import {
    disputeRegisterDetail,
    findAttacheMent,
    frozenAccountModify,
    frozenAccountSave,
    getArbitratSettlementInfoList,
    getdispute,
    getfrozen,
    getlitigationSettlement,
    getnegotiatedSettlementInfoList,
    getqualification,
    legalAttorney,
    getZXList,
  } from '@/api/fwgl/legal'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config/net.config'
  import ArbitrationEdit from './ArbitrationEdit'
  import CloseEdit from './CloseEdit'
  import ConsultEdit from './ConsultEdit'
  import FreezeEdit from './FreezeEdit'
  import LawsuitEdit from './LawsuitEdit'
  import zxglEdit from './zxglEdit.vue'
  import ssjdOptions from './options/ssjd.vue'
  import PreserveEdit from './PreserveEdit'
  import { downloads } from '@/api/fwgl/zzxx'

  export default {
    name: 'DraftEdit',
    components: {
      ssjdOptions,
      ConsultEdit,
      LawsuitEdit,
      ArbitrationEdit,
      PreserveEdit,
      FreezeEdit,
      CloseEdit,
      zxglEdit,
    },
    data() {
      return {
        activeName: 'first',
        dllsData: [],
        form: {
          zxstaffid: undefined,
          contractId: undefined,
          disputeno: undefined,
          disputeitem: undefined,
          disputetype: undefined,
          contractname: undefined,
          contractno: undefined,
          jbstaff: undefined,
          plaintiff: undefined,
          defendant: undefined,
          disputecours: undefined,
          zxstaffname: undefined,
          isUegent: undefined,
          whethersued: undefined,
          isattorney: undefined,
          attorneyphont: undefined,
          solutionsuggestions: undefined,
          enddate1: undefined,
          attorney: undefined,
          disputeid: undefined,
        },
        list1: [],
        list2: [],
        rules: {
          porceedstage: [
            {
              required: true,
              message: '请输入诉讼阶段',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        listLoading: false,
        radio: '',
        show: 0,
        disputeId: '',
        listXS: [],
        listSS: [],
        listZC: [],
        listBQ: [],
        listDJ: [],
        listJA: [],
        listAM: [],
        listZX: [],
        baseApi: baseURL,
      }
    },
    watch: {
      activeName(val) {
        switch (val) {
          case 'first':
            this.getnegotiatedSettlementInfoList()
            break
          case 'second':
            this.getlitigationSettlement()
            break
          case 'third':
            this.getArbitratSettlementInfoList()
            break
          case 'fourth':
            this.getZXList()
            // this.getqualification()
            break
          case 'fifth':
            this.getfrozen()
            break
          case 'sixth':
            this.getdispute()
            break
          case 'seven':
            this.findAttacheMent()
        }
      },
    },
    created() {},
    methods: {
      /**
       * @description: 打开对应的详情弹窗
       * @param {*} name
       * @param {*} row
       * @return {*}
       */
      handleDetail(name, row) {
        this.$refs[name].showEdit(row, true)
      },
      handleDetail1(name, row) {
        this.$refs['zxglAddRef'].show({}, '详情', row)
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getnegotiatedSettlementInfoList() {
        //this.disputeId
        this.listLoading = true
        getnegotiatedSettlementInfoList({
          disputeid: this.disputeId,
          flowid: 698864,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listXS = res.date.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getlitigationSettlement() {
        this.listLoading = true
        getlitigationSettlement({
          disputeid: this.disputeId,
          flowid: 698869,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listSS = res.data.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getZXList() {
        this.listLoading = true
        getZXList({
          disputeid: this.disputeId,
        })
          .then((res) => {
            this.listZX = res.data.date
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getArbitratSettlementInfoList() {
        this.listLoading = true
        getArbitratSettlementInfoList({
          disputeid: this.disputeId,
          flowid: 698874,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listZC = res.date.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getqualification() {
        this.listLoading = true
        getqualification({
          disputeid: this.disputeId,
          flowid: 779383,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listBQ = res.date.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getfrozen() {
        this.listLoading = true
        getfrozen({
          disputeid: this.disputeId,
          flowid: 779387,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listDJ = res.date.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      getdispute() {
        this.listLoading = true
        getdispute({
          disputeid: this.disputeId,
          flowid: 698879,
          pageNumber: 1,
          pageSize: 1000,
        })
          .then((res) => {
            this.listJA = res.date.tlist
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      /**
       * @description: 对应详情的详情接口数据
       * @return {*}
       */
      findAttacheMent() {
        this.listLoading = true
        findAttacheMent({
          bid: this.disputeId,
          type: 1,
        })
          .then((res) => {
            this.listAM = res.data
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      // downloadData(row) {
      //   const fileName = row.attname
      //   const link = document.createElement('a')
      //   link.download = fileName
      //   link.href = this.baseApi + '/download?id=' + row.attid
      //   link.style.display = 'none'
      //   document.body.appendChild(link)
      //   link.click()
      //   document.body.removeChild(link)
      // },
      /**
       * @description: 下载数据
       * @param {*} row
       * @return {*}
       */
      async downloadData(row) {
        const data = await downloads({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
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
      handleClick(tab, event) {},
      /**
       * @description: 弹窗入口
       * @param {*} row
       * @return {*}
       */
      showDetail(row) {
        this.disputeId = row.disputeid
        this.title = '查看'
        this.show = 1
        this.dialogFormVisible = true
        this.getList()
        this.getnegotiatedSettlementInfoList()
        this.fetchLawerList()
      },
      /**
       * @description: 获取详情数据
       * @return {*}
       */
      async getList() {
        const { dispute } = await disputeRegisterDetail({
          disputeId: this.disputeId,
        })
        Object.keys(this.form).forEach((key) => {
          this.form[key] = dispute[key]
        })
        this.form.enddate1 = dispute.lastdealdate
        this.form.contractId = dispute.contractid
        this.form.jbstaff = dispute.realname
        this.form.isUegent = dispute.isuegent
        this.form.zxstaffid = dispute.disputeundertaker
      },
      async fetchLawerList() {
        this.dllsData = []

        if (!this.disputeId) return
        const res = await legalAttorney({ disputeid: this.disputeId })
        //
        this.dllsData = res.data
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dllsData = []
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存
       * @return {*}
       */
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.title === '添加') {
              const { msg } = await frozenAccountSave(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title === '编辑') {
              const { msg } = await frozenAccountModify(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      /**
       * @description: 选择回调
       * @param {*} val
       * @return {*}
       */
      handleSsjd(val) {
        //
        this.form.porceedstage = val.porceedstage
        this.form.disputeitem = val.disputeitem
        this.form.proceedno = val.proceedno
        this.form.court = val.court
        this.form.plaintiff = val.plaintiff
        this.form.defendant = val.defendant
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
