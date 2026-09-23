<template>
  <div>
    <el-row>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item prop="qualitativecode">
          <el-input
            v-model="queryForm.qualitativecode"
            clearable
            placeholder="定性编码"
          />
        </el-form-item>
        <el-form-item prop="qualitativeName">
          <el-input
            v-model="queryForm.qualitativeName"
            clearable
            placeholder="定性名称"
          />
        </el-form-item>
        <el-form-item prop="scopeProblem">
          <el-input
            v-model="queryForm.scopeProblem"
            clearable
            placeholder="问题适用范围"
          />
        </el-form-item>
        <el-form-item prop="regulatoryNames">
          <el-input
            v-model="queryForm.regulatoryNames"
            clearable
            placeholder="法律法规名称"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="fetchData"
          >
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button native-type="submit" type="primary" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-row>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="定性编码" prop="qualitativeCode" />
      <el-table-column align="center" label="定性名称" prop="qualitativeName" />
      <el-table-column
        align="center"
        label="是否填写金额"
        prop="amountFilledIn"
        :formatter="(row) => (row.amountFilledIn ? '是' : '否')"
      />
      <el-table-column
        align="center"
        label="是否实质性问题"
        prop="substantiveIssue"
        :formatter="(row) => (row.substantiveIssue ? '是' : '否')"
      />
      <el-table-column
        align="center"
        label="问题适用范围"
        prop="scopeProblem"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="法律法规名称"
        prop="regulatoryNames"
      />
      <el-table-column align="center" label="文号" prop="regulatoryNumber" />
      <el-table-column
        align="center"
        label="引用的法规制度条款"
        prop="regulatoryProvisions"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        :formatter="
          (row) => dayjs(row.createTime).format('YYYY/MM/DD HH:mm:ss')
        "
      />
      <el-table-column
        align="center"
        label="操作"
        #default="{ row }"
        width="80"
      >
        <el-button icon="el-icon-view" type="text" @click="() => onDetail(row)">
          详情
        </el-button>
      </el-table-column>
    </el-table>

    <el-dialog
      title="详情"
      :visible.sync="previewVisible"
      width="80%"
      :close-on-click-modal="false"
      append-to-body
    >
      <Detail v-if="previewVisible" :formData="currentRow" />
    </el-dialog>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import Detail from './detail.vue'
  import * as dayjs from 'dayjs'
  import { getWTDXList } from '@/api/setting/sjzy'
  export default {
    name: 'AuditQuestion',
    props: ['guide'],
    components: { Detail },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          qualitativeCode: '',
          qualitativeName: '',
          scopeProblem: '',
          regulatoryNames: '',
          pageNumber: 1,
          pageSize: 20,
        },
        previewVisible: false,
        previewHtml: '',
        currentRow: null,
        dayjs,
      }
    },
    created() {
      if (this.guide) {
        this.queryForm.guide = this.guide
      }
      this.fetchData()
    },
    methods: {
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          qualitativeCode: '',
          qualitativeName: '',
          scopeProblem: '',
          regulatoryNames: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      onDetail(row) {
        this.previewVisible = true
        this.currentRow = row
      },
      async fetchData() {
        const params = { ...this.queryForm }
        const res = await getWTDXList(params)
        // const res = {
        //   code: 1,
        //   msg: '成功',
        //   data: {
        //     pageSize: 20,
        //     pageNumber: 1,
        //     totalRecord: 802,
        //     totalPage: 41,
        //     tlist: [
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '资金管理问题 -101',
        //         amountFilledIn: false,
        //         createTime: 1712533645000,
        //         regulatoryProvisions: null,
        //         regulatoryNumber: null,
        //         scopeProblem: null,
        //         qualitativeCode: '101',
        //         regulatoryNames: null,
        //         id: 1,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违规开立、关闭和使用银行账户-10101',
        //         amountFilledIn: false,
        //         createTime: 1712533279000,
        //         regulatoryProvisions: null,
        //         regulatoryNumber: null,
        //         scopeProblem: null,
        //         qualitativeCode: '10101',
        //         regulatoryNames: null,
        //         id: 2,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName:
        //           '违规开立、关闭银行账户（个数）(不填金额)-1010101',
        //         amountFilledIn: false,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第十六条 集团公司实行账户一级审批制度。所属企业开立、变更和撤销账户由财务资产部审批，未经批准不得在任何机构以任何形式设立账户。',
        //         regulatoryNumber: '中油财〔2011〕438号',
        //         scopeProblem:
        //           '账户开户机构未优先选择合作银行、隐瞒各银行账户使用情况、多头开户',
        //         qualitativeCode: '1010101',
        //         regulatoryNames: '《中国石油天然气集团公司司库管理办法》',
        //         id: 3,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违规开立、关闭外汇账户（个数）-1010102',
        //         amountFilledIn: false,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第四章 第二十七条 各单位所有账户的开立、变更、撤销应事先向股份公司资金部提出申请，经批复后，方可在开户机构办理相关业务。',
        //         regulatoryNumber: '油资金〔2014〕283号',
        //         scopeProblem: '未经审批开立、变更、关闭银行账户',
        //         qualitativeCode: '1010102',
        //         regulatoryNames:
        //           '《中国石油天然气股份有限公司账户管理实施细则》',
        //         id: 4,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName: '违规办理高息存款业务（发生数)-1010103',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第二百八十一条 办理银行存款业务时，严禁办理违规高息存款业务。',
        //         regulatoryNumber: '财资字〔2004〕73号',
        //         scopeProblem:
        //           '利用银行变相提高的存款利率，违规存款，即：“高息揽储”。',
        //         qualitativeCode: '1010103',
        //         regulatoryNames:
        //           '《中国石油天然气集团公司财务会计基础工作规范》',
        //         id: 5,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '未按规定范围使用账户（发生数）-1010104',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第二章 第七条 股份公司按照集中管理账户、非集中管理账户、特殊账户和表外账户对账户进行分类管理。第十四条  纳税、烟草购进、POS报销、铁路运费等付款业务应纳入支出账户管理，不再单独开设专用账户。确需单独开设专用账户的，专用账户日终资金余额不得超过批复额度。',
        //         regulatoryNumber: '油资金〔2014〕283号',
        //         scopeProblem: '账户未进行分类管理、专业账户使用不规范等问题。',
        //         qualitativeCode: '1010104',
        //         regulatoryNames:
        //           '《中国石油天然气股份有限公司账户管理实施细则》',
        //         id: 6,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违反限额管理规定（发生数）-1010105',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第三章 第十九条 在合作银行开设的收入账户实行零余额管理，账户资金应按日、全额归集至上级账户。在非合作银行开设的收入账户实行限额管理，账户日终余额达到限额时，应全额上划，上述账户资金限额由股份公司资金部确定。\n第二十条 在合作银行开设的支出账户实行日终零余额和累计支出限额管理。\n日终零余额管理是日间透支支付，日终由上级账户清算补平。\n累计支出限额管理是以账户计划支出额度为限，控制账户付款，账户计划支出额度包括本账户的计划付款额度和下级支出账户的计划付款额度。\n第二十一条 地区公司本部支出账户累计支出限额由股份公司资金部设置，地区公司所属二级单位支出账户累计支出限额由地区公司本部设置。\n第二十二条 支出账户累计支出限额应通过银企直连方式设置。银企直连未设置成功时，应书面通知总部主账户开户行进行手工设置。\n第二十四条 封闭结算账户资金按日清算，实行零余额管理。未经股份公司资金部批准，封闭结算账户不得用于对外付款。',
        //         regulatoryNumber: '油资金〔2014〕283号',
        //         scopeProblem: '收入、支出账户未实行零余额管理等问题',
        //         qualitativeCode: '1010105',
        //         regulatoryNames:
        //           '《中国石油天然气股份有限公司账户管理实施细则》',
        //         id: 7,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName: '出租出借本单位银行账户（次数）-1010106',
        //         amountFilledIn: false,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第四十五条 存款人应按照本办法的规定使用银行结算账户办理结算业务。存款人不得出租、出借银行结算账户，不得利用银行结算账户套取银行信用。',
        //         regulatoryNumber: '中国人民银行令〔2003〕第5号令',
        //         scopeProblem:
        //           '对外出租、出借单位银行账户为外单位或个人代收、代转款项',
        //         qualitativeCode: '1010106',
        //         regulatoryNames: '《人民币银行结算账户管理办法》',
        //         id: 8,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '预付款超规定（金额）-1010107',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第二十三条 各单位预付账款一般不得超过合同总价款的30%。签订定金合同的，应严格依法给付定金，不得超过主合同价款20%的限制。',
        //         regulatoryNumber: '中油资金〔2017〕622号',
        //         scopeProblem: '预付款超集团文件或合同规定比例',
        //         qualitativeCode: '1010107',
        //         regulatoryNames: '《中国石油天然气集团公司应收款项管理办法》',
        //         id: 9,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName:
        //           '对账不及时，银行未达账未及时清理（笔数及金额）-1010108',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '银行应按规定与存款人核对账务。银行结算账户的存款人收到对账单或对账信息后，应及时核对账务并在规定期限内向银行发出对账回单或确认信息。',
        //         regulatoryNumber: '中国人民银行令〔2003〕第5号令',
        //         scopeProblem: '对账不及时，银行未达账未及时清理等问题',
        //         qualitativeCode: '1010108',
        //         regulatoryNames: '《人民币银行结算账户管理办法》',
        //         id: 10,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违反融资筹资管理规定 -10102',
        //         amountFilledIn: false,
        //         createTime: 1712533749000,
        //         regulatoryProvisions: null,
        //         regulatoryNumber: null,
        //         scopeProblem: null,
        //         qualitativeCode: '10102',
        //         regulatoryNames: null,
        //         id: 11,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName:
        //           '违规进行长贷短贷和其他渠道筹资融资(发生数)-1010201',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第六条 融资工作由股份公司总部统一办理，全资子公司债务也应纳入股份公司统一管理范围，在贷款条件优于股份公司条件情况下，报股份公司批准，可由子公司直接办理贷款。\n第七条 根据股份公司的业务发展计划和债务控制目标，制定债务融资计划及融资方案。外资借款计划需要得到国家发展计划委员会批准。主要金融条件需要得到国家外汇管理局批准。第八条  新增中长期、短期银行借款，在银行授信限额内，由财务部取得贷款并办理有关手续。第十五条  融资工作程序。1.根据股份公司的业务发展计划和年度框架预算，及经批准的债务融资计划，进行市场调研，制定具体的融资方案，包括贷款人的选定以及贷款金额、利率、宽限期和还款期等主要金融条款。2.与银行等金融机构具体进行借款合同的谈判，办理借款合同的签约等有关手续。如果个别种类借款，债权人要求集团公司提供担保，要报请集团公司批准。国内外汇借款和外资借款等在借款协议签订后，到国家外汇管理局办理债务登记手续。3.根据债务融资协议的规定及融资头寸的需要，办理提款手续，实现资金到位。',
        //         regulatoryNumber: '石油财字〔1999〕25号',
        //         scopeProblem: '违规进行长贷短贷和其他渠道筹资融资等问题',
        //         qualitativeCode: '1010201',
        //         regulatoryNames:
        //           '《中国石油天然气股份有限公司债务融资管理暂行办法》',
        //         id: 12,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName:
        //           '截留、挪用债务资金和偿债资金（发生数）-1010202',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第三十九条 违反本办法，有下列行为之一的，给予批评教育并责令改正；应当承担纪律责任的，依照股份公司违纪违规行为处分规定对责任人给予处分；涉嫌犯罪的，移送司法机关处理。（二）截留、挪用境内债务融资资金和偿债资金的。',
        //         regulatoryNumber: '油资金〔2014〕334号',
        //         scopeProblem: '截留、挪用债务资金和偿债资金',
        //         qualitativeCode: '1010202',
        //         regulatoryNames:
        //           '《中国石油天然气股份有限公司所属子公司境内债务融资管理办法（试行）》',
        //         id: 13,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName:
        //           '未经授权擅自进行汇率风险管理操作（发生数）-1010203',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第二十七条 对于有下列情形的所属企业，给予通报批评并限期改正；按照集团公司管理人员违纪违规行为处分规定处理：（六）未履行规定程序或者未经授权擅自进行汇率风险管理操作的。',
        //         regulatoryNumber: '资金〔2014〕248号',
        //         scopeProblem:
        //           '资金调剂、外币资金的集中管理和预算管理未授权审批等问题',
        //         qualitativeCode: '1010203',
        //         regulatoryNames:
        //           '《中国石油天然气集团公司汇率风险管理实施细则》',
        //         id: 14,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违反融资筹资管理的其他问题 -1010204',
        //         amountFilledIn: false,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第六节 筹资管理内部控制\n第一百一十九条 筹资管理关键控制环节包括：筹资计划、审批、执行、记录等。\n第一百二十条 各单位应当建立筹资业务的岗位责任制，明确相关部门和岗位的职责、权限，确保办理筹资业务的不相容岗位相互分离、制约和监督。筹资业务不相容岗位一般包括：（一）筹资计划的编制与审批；（二）筹资合同（或协议）的订立与审核；（三）与筹资有关的各种款项偿付的审批与执行；（四）筹资业务的执行与相关会计记录。任何单位不得由同一部门或个人办理筹资业务的全过程。\n第一百二十一条 各单位应当在筹资管理的各环节设置相应的记录或凭证，如实记载各环节业务的开展情况，确保筹资全过程得到有效控制。\n第一百二十二条  各单位应根据年度资金预算编制筹资计划。筹资计划应当符合国家有关法规、政策和单位筹资预算要求，明确筹资规模、筹资结构和筹资方式，并对筹资时机选择、预计筹资成本、潜在筹资风险和具体应对措施等做出安排和说明。\n第一百二十三条 各单位应明确筹资业务的审批程序及权责范围，对审批结果进行书面记录，并按集团公司的下列规定对筹资业务进行管理：（一）严格控制债务规模......（二）外汇融资活动，原则上由集团公司统一组织进行，......（三）收购上市公司或利用资本市场进行股权融资，必须事先向集团公司报告，经批准后方能进行。\n第一百二十四条 筹资业务经授权审批后......定期与账簿及外部机构进行核对。\n第一百二十五条 单位应当按照筹资合同（或协议）的约定及时取得相关资产.......明确保管责任，定期或不定期进行检查。\n第一百二十六条 单位应当按照筹资方案所规定的用途使用对外筹集的资金。由于市场环境变化等特殊情况导致确需改变资金用途的，应当履行审批手续，并对审批过程进行完整的书面记录。\n第一百二十七条 单位应当加强对筹资费用的计算、核对工作，确保筹资费用符合筹资合同（或协议）的规定。\n第一百二十八条  各单位应当对利息、租金、股利（利润）及本金等的计算、核对、支付做出明确规定，确保各项款项偿付符合筹资合同（或协议）的规定。（一）、（二）、（三）、（四）......\n第一百二十九条 财务部门在办理筹资业务款项偿付过程中，发现已审批拟偿付的各种款项的支付方式、金额或币种等与有关合同（或协议）不符的，应当及时向有关部门报告，有关部门应当查明原因，做出处理。',
        //         regulatoryNumber: '财资字〔2004〕73号',
        //         scopeProblem:
        //           '融资筹资不签订合同协议、筹资外汇手续不完善等问题',
        //         qualitativeCode: '1010204',
        //         regulatoryNames:
        //           '《中国石油天然气集团公司财务会计基础工作规范》',
        //         id: 15,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违规担保 -10103',
        //         amountFilledIn: false,
        //         createTime: 1709801066000,
        //         regulatoryProvisions: null,
        //         regulatoryNumber: null,
        //         scopeProblem: null,
        //         qualitativeCode: '10103',
        //         regulatoryNames: null,
        //         id: 16,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName: '违规为系统外单位担保(发生数 ）-1010301',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第五条 所属企业未经集团公司批准或授权不得提供任何形式的担保。集团公司及所属企业不得为无股权关系的集团外企业提供担保；不得为控股、参股企业超股权比例提供无反担保措施的担保；为控股、参股企业超股权比例提供担保，但其他股东提供了足额保障措施，可视为按股比提供担保。集团公司及所属企业的分支机构、职能部门不能作为保证人。分支机构有法人书面授权的，可以在授权范围内提供保证。',
        //         regulatoryNumber: '中油资金〔2019〕49号',
        //         scopeProblem: '为无股权关系的集团外企业提供担保',
        //         qualitativeCode: '1010301',
        //         regulatoryNames: '《中国石油天然气集团有限公司担保管理办法》',
        //         id: 17,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName: '违规为改制单位担保(发生数)-1010302',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第十九条 被担保企业出现以下情形之一的，原则上不得为其提供担保：（一）已进入重组、托管、兼并或破产清算程序的。',
        //         regulatoryNumber: '中油资金〔2019〕49号',
        //         scopeProblem: '为重组、托管、兼并或破产的单位担保。',
        //         qualitativeCode: '1010302',
        //         regulatoryNames: '《中国石油天然气集团有限公司担保管理办法》',
        //         id: 18,
        //       },
        //       {
        //         substantiveIssue: true,
        //         qualitativeName:
        //           '违规使用保证、抵押或质押方式担保(发生数) -1010303',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '第四十三条 办理接受担保事项时，接受担保企业应严格审查担保人资质及抵押物、质押物价值，做好权属确认。提供担保企业应符合所在国担保法律要求，且设定的担保金额不得超过其最近一期经审计净资产的50%。担保人为金融机构的，优选穆迪A1、标普A+、惠誉A+或以上评级。以抵押、质押提供担保的，抵押、质押标的应符合所在国担保法律规定，且其公允价值应完全覆盖担保金额，公允价值波动较大的，应适当提高覆盖比例。集团公司及所属企业不得接受以受益人股权作为质押权的标的。',
        //         regulatoryNumber: '中油资金〔2019〕49号',
        //         scopeProblem:
        //           '担保金额超净资产50%；受益人股权作为质押权的标的等问题。',
        //         qualitativeCode: '1010303',
        //         regulatoryNames: '《中国石油天然气集团有限公司担保管理办法》',
        //         id: 19,
        //       },
        //       {
        //         substantiveIssue: false,
        //         qualitativeName: '违反其他担保规定问题 -1010399',
        //         amountFilledIn: true,
        //         createTime: 1709801066000,
        //         regulatoryProvisions:
        //           '根据审计发现问题定性，分析、判断、选择适用的条款。',
        //         regulatoryNumber: '中油资金〔2019〕49号',
        //         scopeProblem: null,
        //         qualitativeCode: '1010399',
        //         regulatoryNames: '《中国石油天然气集团有限公司担保管理办法》',
        //         id: 20,
        //       },
        //     ],
        //   },
        //   result: null,
        // }

        console.log('fetchData', res)

        this.total = res.data.totalRecord
        this.list = res.data.tlist
      },
    },
  }
</script>
