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
            <el-form-item label="纠纷主题" prop="porceedstage">
              <span>{{ form.disputeitem }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纠纷类型" prop="porceedstage">
              <span>{{ form.disputetype }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="porceedstage">
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
            <el-form-item label="纠纷承办人" prop="porceedstage">
              <span>{{ form.zxstaffname }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否紧急事项">
              <span>{{ form.isUegent == 1 ? '是' : '否' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="起诉类型">
              <span>{{ form.whethersued == 1 ? '起诉' : '被诉' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最晚办结时间">
              <span>{{ form.enddate1 }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否外聘律师">
              <span>{{ form.isattorney == 1 ? '是' : '否' }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代理律师">
              <span>{{ form.attorney }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代理律师联系电话">
              <span>{{ form.attorneyphont }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="初步解决建议">
              <span>{{ form.solutionsuggestions }}</span>
            </el-form-item>
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
              <el-table-column
                align="center"
                label="案件名称"
                prop="firstcourt"
              />
              <el-table-column
                align="center"
                label="审判长"
                prop="presidingjudge"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDetail('lawsuitEdit', row)"
                  >
                    {{ row.presidingjudge }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="诉讼标的物"
                prop="actionobject"
              />
              <el-table-column
                align="center"
                label="受理日期"
                prop="dealdate"
              />
              <el-table-column
                align="center"
                label="首次开庭日期"
                prop="firsthearingdate"
              />
              <el-table-column
                align="center"
                label="结案日期"
                prop="litigationenddate"
              />
              <el-table-column
                align="center"
                label="诉讼金额"
                prop="litigationamount"
              />
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
        <el-tab-pane label="资产保全" name="fourth">
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
        </el-tab-pane>
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
  </div>
</template>

<script>
  import {
    frozenAccountSave,
    frozenAccountModify,
    disputeRegisterDetail,
    getnegotiatedSettlementInfoList,
    getlitigationSettlement,
    getArbitratSettlementInfoList,
    getqualification,
    getdispute,
    getfrozen,
    findAttacheMent,
  } from '@/api/contract/legal'
  import ssjdOptions from './options/ssjd.vue'
  import ConsultEdit from './ConsultEdit'
  import LawsuitEdit from './LawsuitEdit'
  import ArbitrationEdit from './ArbitrationEdit'
  import PreserveEdit from './PreserveEdit'
  import FreezeEdit from './FreezeEdit'
  import CloseEdit from './CloseEdit'
  import { baseURL } from '@/config/net.config'

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
    },
    data() {
      return {
        activeName: 'first',
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
            this.getqualification()
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
      handleDetail(name, row) {
        this.$refs[name].showEdit(row, true)
      },
      //请求数据
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
      //请求数据
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
      //请求列表
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
      //下载
      downloadData(row) {
        const fileName = row.attname
        const link = document.createElement('a')
        link.download = fileName
        link.href = this.baseApi + '/download?id=' + row.attid
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      handleClick(tab, event) {},
      showDetail(row) {
        this.disputeId = row.disputeid
        this.title = '查看'
        this.show = 1
        this.dialogFormVisible = true
        this.getList()
        this.getnegotiatedSettlementInfoList()
      },
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
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
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
      //回调处理数据
      handleSsjd(val) {
        //
        this.form.porceedstage = val.porceedstage
        this.form.disputeitem = val.disputeitem
        this.form.proceedno = val.proceedno
        this.form.court = val.court
        this.form.plaintiff = val.plaintiff
        this.form.defendant = val.defendant
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
