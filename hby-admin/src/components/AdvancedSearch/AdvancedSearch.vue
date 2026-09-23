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
        <el-form-item prop="keyword">
          <el-input
            v-model="queryForm.keyword"
            clearable
            placeholder="关键词"
          />
        </el-form-item>
        <el-form-item prop="idxstr">
          <el-select v-model="queryForm.idxstr" clearable placeholder="索引">
            <el-option label="审计管理指引" value="sjglzy" />
            <el-option label="法规制度指引" value="flzdzy" />
            <el-option label="审计对象指引" value="sjdxzy" />
            <el-option label="审计程序方法指引" value="sjcxffzy" />
            <el-option label="审计经验指引" value="sjjyzy" />
          </el-select>
        </el-form-item>
        <el-form-item prop="searchPattern">
          <el-select
            v-model="queryForm.searchPattern"
            clearable
            placeholder="搜索模式"
          >
            <el-option label="精确匹配" value="0" />
            <el-option label="模糊匹配" value="1" />
          </el-select>
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

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="油田" name="first"></el-tab-pane>
      <el-tab-pane label="集团" name="second"></el-tab-pane>
      <el-tab-pane label="国家" name="third"></el-tab-pane>
    </el-tabs>

    <el-table
      v-loading="listLoading"
      :data="list"
      :show-header="false"
      :key="tableKey"
    >
      <el-table-column align="left" label="">
        <template #default="{ row }">
          <div class="single-cell-style">
            <div class="title">
              <span style="font-size: 18px; margin-right: 20px">
                {{ row.dname }}
              </span>
              <span>
                {{ '油田' }}
                <el-button
                  icon="el-icon-view"
                  type="text"
                  @click="onPreview(row)"
                >
                  在线查阅
                </el-button>
                <el-button
                  icon="el-icon-download"
                  type="text"
                  @click="onDownload(row)"
                >
                  下载文档
                </el-button>
              </span>
            </div>
            <div class="desc" v-html="row.frameContent"></div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="在线预览"
      :visible.sync="previewVisible"
      width="50%"
      :close-on-click-modal="false"
      append-to-body
    >
      <div v-html="previewHtml" ref="content" />
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
  import { getGJSUList, getGJOLDetail } from '@/api/setting/sjzy'
  export default {
    name: 'AdvancedSearch',
    props: ['guide'],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          keyword: '',
          idxstr: '',
          searchPattern: '0',
          level: 10,
          pageNumber: 1,
          pageSize: 20,
        },
        activeName: 'first',
        tableKey: new Date().getTime(),
        previewVisible: false,
        previewHtml: '',
      }
    },
    created() {
      if (this.guide) {
        this.queryForm.idxstr = this.guide
      }
    },
    methods: {
      resetSearch() {
        this.resetQueryForm()
        // this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          keyword: '',
          idxstr: this.guide,
          searchPattern: '0',
          level: 10,
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
      handleClick(tab, event) {
        if (this.activeName === 'first') {
          this.queryForm.level = 10
          this.fetchData()
        } else if (this.activeName === 'second') {
          this.queryForm.level = 20
          this.fetchData()
        } else {
          this.queryForm.level = 30
          this.fetchData()
        }
        this.tableKey = new Date().getTime()
      },
      async onPreview(row) {
        const res = await getGJOLDetail({ did: row.id })
        // const res = {
        //   code: 0,
        //   data: {
        //     did: 3837,
        //     htmlContent:
        //       '<html>\r\n<head>\r\n<META http-equiv="Content-Type" content="text/html; charset=utf-8">\r\n<style type="text/css">.b1{white-space-collapsing:preserve;}\r\n.b2{margin: 1.4569445in 1.0236111in 1.3784722in 1.1027777in;}\r\n.p1{text-align:center;hyphenate:auto;font-family:方正小标宋简体;font-size:22pt;}\r\n.p2{text-align:center;hyphenate:auto;font-family:方正黑体简体;font-size:16pt;}\r\n.p3{text-align:center;hyphenate:auto;font-family:方正小标宋简体;font-size:16pt;}\r\n.p4{text-align:center;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.p5{text-indent:0.45833334in;text-align:justify;hyphenate:auto;font-family:方正黑体简体;font-size:16pt;}\r\n.p6{text-indent:0.44444445in;text-align:start;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.p7{text-indent:0.45833334in;text-align:justify;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.p8{text-align:justify;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.p9{text-indent:0.45694444in;text-align:justify;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.p10{text-align:center;hyphenate:auto;font-family:方正仿宋简体;font-size:14pt;}\r\n.p11{text-indent:0.44444445in;text-align:justify;hyphenate:auto;font-family:方正黑体简体;font-size:16pt;}\r\n.p12{text-indent:0.44444445in;text-align:justify;hyphenate:auto;font-family:方正仿宋简体;font-size:16pt;}\r\n.s1{font-family:方正仿宋简体;}\r\n.s2{color:black;}\r\n.s3{font-family:Times New Roman;}\r\n.td1{width:0.8888889in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-left:thin dashed black;border-right:thin dashed black;border-top:thin dashed black;}\r\n.td2{width:0.9291667in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-right:thin dashed black;border-top:thin dashed black;}\r\n.td3{width:0.8486111in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-right:thin dashed black;border-top:thin dashed black;}\r\n.td4{width:0.8888889in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-left:thin dashed black;border-right:thin dashed black;}\r\n.td5{width:0.9291667in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-right:thin dashed black;}\r\n.td6{width:0.8486111in;padding-start:0.0in;padding-end:0.0in;border-bottom:thin dashed black;border-right:thin dashed black;}\r\n.td7{width:0.8888889in;padding-start:0.0in;padding-end:0.0in;border-left:thin dashed black;border-right:thin dashed black;}\r\n.td8{width:0.9291667in;padding-start:0.0in;padding-end:0.0in;border-right:thin dashed black;}\r\n.td9{width:0.8486111in;padding-start:0.0in;padding-end:0.0in;border-right:thin dashed black;}\r\n.r1{height:0.6944444in;keep-together:always;}\r\n.r2{height:0.7625in;keep-together:always;}\r\n.r3{height:0.79097223in;keep-together:always;}\r\n.t1{table-layout:fixed;border-collapse:collapse;border-spacing:0;}\r\n</style>\r\n<title>大庆油田有限责任公司</title>\r\n<meta content="user" name="author">\r\n</head>\r\n<body class="b1 b2">\r\n<p class="p1">\r\n<span>大庆油田有限责任公司工资总额管理办法</span>\r\n</p>\r\n<p class="p2"></p>\r\n<p class="p3">\r\n<span>第一章  总  则</span>\r\n</p>\r\n<p class="p4"></p>\r\n<p class="p5">\r\n<span>第一条</span><span class="s1">  为建立健全与劳动力市场基本适应、与企业经济效益和劳动生产率挂钩的工资决定和正常增长机制，进一步规范大庆油田有限责任公司、大庆石油管理局有限公司（以下统称油田公司）工资总额管理，优化激励约束机制，推进新时代油田高质量发展，根据中国石油天然气集团有限公司（以下简称集团公司）工资总额管理办法有关规定，结合油田公司实际，制定本办法。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二条</span><span class="s1">  本办法所称工资总额，是指油田公司所属单位（以下简称所属单位）在一个会计年度内直接支付给与本单位建立劳动关系的全部员工的劳动报酬总额，包括工资、奖金、津贴、补贴、加班加点工资、特殊情况下支付的工资等。</span>\r\n</p>\r\n<p class="p6">\r\n<span>本办法所称工资总额管理，是指油田公司依据年度生产经营目标、效益情况和人力资源管理要求，对所属单位工资总额实行预算管理，包括预算编制、预算申报、预算核准或备案、预算执行和清算评价，以及员工工资水平调控等，并进行有效控制和监督的全过程管理。油田公司</span><span class="s2">对所属单位原则上实行工资总额核准制管理，并探索在部分单位实行工资总额备案制、周期制管理。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三条</span><span class="s1">  工资总额管理遵循以下原则：</span>\r\n</p>\r\n<p class="p6">\r\n<span class="s2">（一）坚持战略引领。围绕新时代油田高质量发展总体战略布局，推进实施人才强企工程，强化科技创新激励保障，实行与战略规划相匹配的薪酬策略，引领企业高质量发展，促进战略目标实现。</span>\r\n</p>\r\n<p class="p6">\r\n<span class="s2">（二）坚持效益效率导向。所属单位工资总额和员工工资水平的确定以及增长，应当与本单位经济效益和劳动生产率的提高相联系，切实实现员工工资能增能减。</span>\r\n</p>\r\n<p class="p6">\r\n<span class="s2">（三）坚持市场化改革方向。充分发挥市场在工资分配中的决定性作用，统筹处理好不同业务、不同单位和各类各层级员工之间的收入分配关系，实现员工工资水平与劳动力市场价位相适应、与企业市场竞争力相匹配。</span>\r\n</p>\r\n<p class="p6">\r\n<span class="s2">（四）坚持分级分类管理。按照&ldquo;油田公司调控总量、所属单位自主分配&rdquo;的管理模式，实行工资总额分级管理；根据企业功能定位、业务特点和市场化程度差异，实行工资总额分类管理。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四条</span><span class="s1">  本办法适用于油田公司机关、所属单位。</span>\r\n</p>\r\n<p class="p7">\r\n<span>油田公司及所属单位的全资子公司通过法定程序实施本办法。</span>\r\n</p>\r\n<p class="p7">\r\n<span>油田公司及所属单位的控股公司应依据《公司法》要求建立健全相关规章制度，实现企业规范管理。</span>\r\n</p>\r\n<p class="p8"></p>\r\n<p class="p3">\r\n<span>第二章  管理机构及职责</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第五条</span><span class="s1">  油田党委组织部（人事部）是油田公司工资总额的归口管理部门，主要职责如下：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）贯彻落实国家、集团公司有关工资总额的方针政策、法律法规和制度规定；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）负责起草油田公司工资总额管理制度，并组织实施；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）指导所属单位编制和申报工资总额预算，核准及备案其工资总额预算控制数；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（四）负责所属单位工资总额的审批、调控、清算评价及统计分析等工作； </span>\r\n</p>\r\n<p class="p7">\r\n<span>（五）指导所属单位工资总额管理工作；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（六）监督、检查所属单位工资总额执行情况。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第六条 </span><span class="s1"> 所属单位主要职责：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）负责制定内部工效挂钩办法，建立收入与绩效挂钩的能增能减机制；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）负责编制和申报工资总额预算，并按油田党委组织部（人事部）核准或备案数进行控制；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）负责编制月度工资总额使用计划，并报油田党委组织部（人事部）进行审批；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（四）负责建立薪酬分配激励约束机制，实施内部分配； </span>\r\n</p>\r\n<p class="p7">\r\n<span>（五）负责监督检查内部工资总额执行情况，规范分配秩序；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（六）负责本单位工资总额的发放使用、统计分析等日常管理。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第三章  工资总额核准制管理</span>\r\n</p>\r\n<p class="p4"></p>\r\n<p class="p5">\r\n<span>第七条</span><span class="s1">  对实行工资总额核准制管理的所属单位，构建由工资效益效率联动、效能对标调节和工资水平调控等共同组成、协调运转的工资总额决定机制。油田公司根据所属单位原油（天然气）产量、净利润和劳动生产率等指标完成情况，核定年度工资总额预算控制数。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p2">\r\n<span>第一节  工资效益效率联动机制</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第八条</span><span class="s1">  所属单位工资总额与关键效益效率指标挂钩联动。</span>\r\n</p>\r\n<p class="p7">\r\n<span>效益指标主要考核原油（天然气）产量、净利润；</span>\r\n</p>\r\n<p class="p7">\r\n<span>效率指标主要考核劳动生产率，以人均油气当量产量、人均营业收入等指标为主。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第九条</span><span class="s1">  所属单位年度工资总额包括工资总额基数、工效挂钩增量、工资总额特别奖励和工资总额单列额度等四个部分。</span>\r\n</p>\r\n<p class="p7">\r\n<span>年度工资总额=工资总额基数+工效挂钩增量+工资总额特别奖励+工资总额单列额度</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十条</span><span class="s1">  工资总额基数以上年清算数为基础，剔除减员减资额度和单列额度等，并考虑当年用工计划内增人增资、岗位（技）工资考核晋档增资等额度后，区分油气生产型单位、市场经营型单位、科研单位和其他单位四种类型，按一定比例核定，其中：</span>\r\n</p>\r\n<p class="p7">\r\n<span>市场经营型单位核定比例根据本单位上年及当年效益盈亏情况，分连续两年盈利、一年盈利一年亏损、连续两年亏损，设置三档标准，按照每档2%~3%的档差，在100%~90%区间内确定；</span>\r\n</p>\r\n<p class="p7">\r\n<span>油气生产型单位、科研单位核定比例原则上参照市场经营型单位最高档标准；</span>\r\n</p>\r\n<p class="p7">\r\n<span>其他单位核定比例参照市场经营型单位中间档标准。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十一条</span><span class="s1">  工效挂钩增量区分油气生产型单位、市场经营型单位、科研单位和其他单位四种类型，根据工资总额基数和工效联动增幅（N）确定。</span>\r\n</p>\r\n<p class="p7">\r\n<span>工效挂钩增量=工资总额基数&times;工效联动增幅（N）</span>\r\n</p>\r\n<p class="p7">\r\n<span>工效联动增幅（N）依据所属单位考核指标完成情况分别确定（见附件）。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十二条</span><span class="s1">  对油气生产型单位中的亏损单位，奖励标准原则上按规定比例的80%执行，扣罚标准按规定比例的120%执行。</span>\r\n</p>\r\n<p class="p7">\r\n<span>对市场经营型单位中利润贡献占比小于2%的单位，奖罚标准应根据实际经营情况予以适当调整。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十三条</span><span class="s1">  工资总额特别奖励纳入所属单位次年工资总额基数，包括以下事项：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）对通过内部盘活消化新增产能、新建装置等用工需求且成效明显的单位，按消化新增人员工资总额的50%予以奖励。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）对主动开展劳务输出、业务承揽（不含工程类承揽）的单位，分别对输出、输入单位予以奖励，其中：</span>\r\n</p>\r\n<p class="p7">\r\n<span>1. 输出到集团公司以外的，每输出1人奖励2万元；</span>\r\n</p>\r\n<p class="p7">\r\n<span>2. 输出到集团公司内部的，每输出1人奖励1万元；</span>\r\n</p>\r\n<p class="p7">\r\n<span>3. 输出到油田公司内部其他单位的，每输出1人奖励0.5万元，输入单位按输出单位奖励额度的50%予以奖励。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）对在实施人才强企工程、推进三项制度改革等重点工作中取得显著成效的所属单位，油田公司视情况给予一定额度工资总额特别奖励。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十四条</span><span class="s1">  工资总额单列额度实行特殊事项清单管理。列入特殊事项清单范围的事项，在所属单位年度工资总额预算内实行专项核算、单列管理，不纳入工资总额基数、不参与挂钩考核：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）对承担国家重大专项任务和高新工程，设立国家重点实验室、国家工程实验室等国家级创新平台，吸引保留院士、海外高层次人才引进计划专家，承担关键核心技术攻关、打造能源与化工领域原创技术策源地和培育油气产业链&ldquo;链长&rdquo;科技创新核心能力相关重大基础研究等任务的科研领军人才及团队，所需工资总额予以单列。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）对承担国家科技重大专项、国家重点研发计划项目间接费用中提取的绩效奖励，科技型企业实施分红激励，&ldquo;科改示范企业&rdquo;改革增资额度，以及开展模拟分红等，所需工资总额予以单列。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（三）按照国家和国资委要求，对强化科技创新人员激励、系统推进科技创新激励保障机制建设涉及的其他重大事项所需工资总额，按照&ldquo;一事一议&rdquo;的方式，经油田党委组织部（人事部）认定后可予以单列。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（四）通过市场化机制选聘、按照市场化原则确定业绩目标、实行协议工资制的高层次国际化人才、石油领域科学家、高层次职业经理人等高精尖缺人才，所需工资总额予以单列。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（五）对涉及境外业务的单位，其境外员工工资总额实行单列管理。境外单位应按照工效同向联动、合理匹配的原则，申报境外员工工资总额单列额度，油田公司在下达全年预算控制数时予以单列。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（六）从事外部市场开发的单位，其按外部市场开发收现金额一定比例计提的工资总额实行单列管理，包括项目收入计提额、年度收入增长计提额两部分。项目收入计提额以外部市场开发项目形成的现金收入为基数，区分国际、国内非中石油、国内中石油内部、大庆本地外部四个市场类型，盈利单位分别按4%、3%、2%、1%比例计提；亏损单位计提比例按盈利单位的50%确定。年度收入增长计提额以</span><span class="s3">外部市场开发年度现金收入</span><span>增长值为基数，按3%~5%比例计提。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（七）具有发展潜力、代表油田公司未来发展方向的新能源、新材料、新事业等战略新兴领域，以及需要重点培育的经济增长点、重大改革试点等，所需工资总额可予以单列。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（八）油田公司专项奖励额度予以单列管理。</span>\r\n</p>\r\n<p class="p9">\r\n<span>（九）其他需要单列管理事项，按照&ldquo;一事一议&rdquo;的方式，经油田党委组织部（人事部）认定后可予以单列。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十五条</span><span class="s1">  油田公司根据生产经营和效益形势，视集团公司下达工资总额预算控制数情况，明确当年工资总额预算编制要求，确定工资总额基数核定比例、工效联动增幅奖罚比例，以及盈利和亏损单位工资总额增幅封顶线和保底线等，合理调控所属单位工资水平，重点向利润贡献大、劳动生产率和人工成本投入产出效率高、科技创新成效显著以及工作条件较为艰苦的单位倾斜。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p2">\r\n<span>第二节  效能对标调节机制</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第十六条</span><span class="s1">  油田公司根据人工成本利润率、人事费用率等人工成本投入产出效率指标对标结果，引入人工成本效能系数，对所属单位工效联动增幅进行调整。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第十七条</span><span class="s1">  人工成本效能系数按照所属单位人工成本投入产出效率指标的纵向对标，以及同类型单位内横向对标的双维度九宫格模型确定。其中：纵向对标结果按所属单位同比上年实际完成值变化情况确定；横向对标结果按所属单位实际完成值与同类型单位对标结果确定，分位值越高代表指标在行业越优秀。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<table class="t1">\r\n<tbody>\r\n<tr class="r1">\r\n<td class="td1">\r\n<p class="p10">\r\n<span>持平</span>\r\n</p>\r\n</td><td class="td2">\r\n<p class="p10">\r\n<span>适度</span>\r\n<br>\r\n<span>奖励</span>\r\n</p>\r\n</td><td class="td3">\r\n<p class="p10">\r\n<span>重点</span>\r\n<br>\r\n<span>奖励</span>\r\n</p>\r\n</td>\r\n</tr>\r\n<tr class="r2">\r\n<td class="td4">\r\n<p class="p10">\r\n<span>适度</span>\r\n<br>\r\n<span>压缩</span>\r\n</p>\r\n</td><td class="td5">\r\n<p class="p10">\r\n<span>持平</span>\r\n</p>\r\n</td><td class="td6">\r\n<p class="p10">\r\n<span>适度</span>\r\n<br>\r\n<span>奖励</span>\r\n</p>\r\n</td>\r\n</tr>\r\n<tr class="r3">\r\n<td class="td7">\r\n<p class="p10">\r\n<span>严格</span>\r\n<br>\r\n<span>压缩</span>\r\n</p>\r\n</td><td class="td8">\r\n<p class="p10">\r\n<span>适度</span>\r\n<br>\r\n<span>压缩</span>\r\n</p>\r\n</td><td class="td9">\r\n<p class="p10">\r\n<span>持平</span>\r\n</p>\r\n</td>\r\n</tr>\r\n</tbody>\r\n</table>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第十八条</span><span class="s1">  根据效能对标结果，划分为&ldquo;严格压缩、适度压缩、持平、适度奖励和重点奖励&rdquo;五种情形，在0.8~1.2的区间范围内确定所属单位人工成本效能系数，相应调减或调增工效联动增幅。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p2">\r\n<span>第三节  工资水平调控机制</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第十九条</span><span class="s1">  油田公司根据所属单位员工平均工资与油田公司整体水平对标结果，建立工资水平调控机制，按照0.8~1.2的调控系数，适度平衡不同单位间工资水平，合理调节各类各层级员工的工资收入差距，统筹兼顾油田公司整体分配公平。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十条</span><span class="s1">  对人均工资增幅较高且上年员工平均工资高于油田公司整体水平较多的单位，按0.8~1.0的调控系数，适当调减工效联动增幅。因员工队伍结构差异、所在行业劳动力市场价位较高等因素导致员工平均工资水平较高的，可适当考虑有关因素，不予调减或适度调减工效联动增幅。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十一条</span><span class="s1">  对上年员工平均工资低于油田公司整体水平较多的单位，按1.0~1.2的调控系数，适当调增工效联动增幅。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十二条</span><span class="s1">  油田公司机关、所属单位机关员工平均工资增幅，原则上应当分别不高于油田公司、所属单位全部员工平均工资增幅。对机关员工平均工资水平不合理过高、偏高的单位，油田公司应对其工资增幅进行控制。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第四章  工资总额备案制管理</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第二十三条</span><span class="s1">  对纳入国资委重大改革试点范围，或处于充分竞争行业和领域，且已建立规范董事会、法人治理结构健全、三项制度改革到位、收入分配管理规范的单位，以及战略新兴领域的新建单位，可申请实行工资总额备案制管理。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十四条</span><span class="s1">  工资总额备案制管理是指符合条件的所属单位，在油田公司授权、指导下，根据本单位生产经营特点，自主确定工效挂钩指标、工资增幅等预算规则，经油田公司审核同意后，合理确定工资总额的管理方式。油田公司审核申请单位工资总额管理办法，并对申请单位年度工资总额预算方案及执行情况进行备案、监督和评价。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十五条</span><span class="s1">  申请单位依据授权拟订本单位工资总额管理办法，报油田公司审核同意后，按照办法规定自主编制年度工资总额预算方案并组织实施。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）申请单位应遵循效益增长与效益下降时激励约束总体平衡的原则，执行国家政策规定和集团公司、油田公司管理制度要求，建立健全员工工资与经济效益同向联动、能增能减的工资总额管理办法，合理有序确定工资总额增减幅度。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）经济效益较上年增长的，在不超过经济效益增幅、且人工成本利润率提高的前提下，工资总额增幅可采取与净利润完成情况紧密挂钩的方式合理确定，工资总额增幅最高线可适当放宽1~2个百分点，但未完成预算目标的单位工资总额不得增长。经济效益较上年下降的，原则上工资总额应下降，降幅根据经济效益下降幅度等情况合理确定，最低降至上年实发数的80%。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）在按效益确定工资总额增幅的基础上，申请单位应建立业绩薪酬双对标体系，依据劳动生产率、人工成本投入产出效率和薪酬水平市场化对标等情况调节工资总额预算水平。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十六条</span><span class="s1">  申请单位工资总额预算方案应当包括预算基数及口径、年度经济效益指标和工资总额预算安排、人工成本投入产出效率情况及机关人员工资预算安排等内容。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十七条</span><span class="s1">  申请单位工资总额预算方案和预算调整方案履行内部决策程序后，分别于每年4月和10月以书面形式报送油田党委组织部（人事部）备案。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第五章  工资总额周期制管理</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第二十八条</span><span class="s1">  对行业周期性明显，或处于转方式、调结构等特殊发展阶段，在一定时期内经济效益年度间波动较大、且内控机制健全、收入分配管理规范的所属单位，可申请实行工资总额周期制管理。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第二十九条</span><span class="s1">  工资总额周期制管理是指在工资总额核准制管理基础上，允许符合条件的所属单位根据生产经营节奏变化，对工资总额年度间发放节奏进行合理安排的管理方式。一般以三个会计年度为一个管理周期。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十条</span><span class="s1">  实行工资总额周期制管理的所属单位按以下规定执行：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）周期内各年度按工资总额核准制计算当年理论预算控制数、实行记账管理，并以此为基础核定周期内下一年度工资总额基数。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）各年度实际发放工资总额低于理论预算控制数形成的剩余额度，不在财务报表中通过工资结余的方式予以体现，实行记账管理，可在周期内以后年度调剂使用。单位经济效益下降时，可适当使用部分剩余额度，最高可保持工资总额零增长，发挥以丰补歉的作用。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）周期内各年度实际使用工资总额预算需报油田公司事前备案。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（四）超出当年理论预算控制数发放的工资总额，不得影响当期生产经营任务目标的完成。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第六章  专项奖励管理</span>\r\n</p>\r\n<p class="p4"></p>\r\n<p class="p5">\r\n<span>第三十一条</span><span class="s1">  油田公司设立专项奖励，对在贯彻落实油田公司发展战略且未纳入工效挂钩和业绩考核的重点工作、重大项目中，做出突出贡献的单位和个人给予奖励。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十二条</span><span class="s1">  奖励项目按照&ldquo;少而精&rdquo;的设奖原则，控制在10项以内；奖励总额原则上按每年不超过工资总额1%的比例进行控制，计入所属单位工资总额。</span>\r\n</p>\r\n<p class="p11">\r\n<span>第三十三条</span><span class="s1">  专项奖励由设奖（申请）部门制定奖励方案，经油田公司分管领导审核和主要领导审批，并履行规定程序后，报油田</span><span class="s3">党委组织部（人事部）办理支付审批手续。具体实施程序按油田公司专项奖励管理规定执行。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十四条</span><span class="s1">  </span><span class="s3">专项奖励应向在专项工作中做出突出贡献的单位和个人发放，不得向企业领导人员和与专项工作无关的其他人员发放。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十五条</span><span class="s1">  </span><span class="s3">下级单位不得以任何名义、任何形式向上级单位员工发放任何性质的专项奖励，具有经济业务合作关系的单位也不得向合作单位员工发放专项奖励。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十六条</span><span class="s1">  除有明确保密要求的奖励项目以外，专项奖励必须履行公示程序。专项奖励按税前标准下达，获奖个人应依法缴纳个人所得税。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十七条</span><span class="s1">  所属单位层面的专项奖励，应按照油田公司有关规定执行，奖励项目、奖励标准、奖励总额、管理程序等应符合油田公司相关要求。对所属单位自行设立的专项奖励，油田公司不予核增工资总额，由所属单位在油田公司核定的工资总额预算控制数内自行消化解决。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第七章  工资总额管理程序</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第三十八条</span><span class="s1">  工资总额管理程序包括预算编制、预算申报、预算核准或备案、预算执行、清算评价等环节。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第三十九条</span><span class="s1">  所属单位按照油田公司明确的预算编制规则，依据本单位经济效益、劳动生产率等指标完成情况和成本承受能力，合理编制年度工资总额预算，并按规定时间节点进行申报。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十条</span><span class="s1">  每年年初，油田公司根据集团公司当年工资总额预算管理具体要求，指导所属单位预算编制、申报工作，并下达所属单位年度工资总额预算计划数；</span>\r\n</p>\r\n<p class="p7">\r\n<span>每年年底，油田公司在集团公司核定的工资总额预算控制数内，组织所属单位申报全年工资总额预算，并根据油田公司机关相关部门审核后的挂钩指标预计完成情况以及上年清算评价结果，核定下达所属单位年度工资总额预算控制数；</span>\r\n</p>\r\n<p class="p7">\r\n<span>次年，根据总考核结果，对上年工资总额预算执行情况进行清算评价，并对净利润指标预测值高于实际值10%以上、其他指标预测值高于实际值5%以上的所属单位，因高出部分导致上年相应多预付的工资额度，按2倍予以扣减。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第八章  企业内部分配管理</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第四十一条</span><span class="s1">  所属单位应充分发挥内部分配主体作用，探索符合行业特点、满足自身发展需要的内部分配方法，引导员工牢固树立&ldquo;单位总额靠效益、个人收入凭贡献&rdquo;&ldquo;效益升薪酬升、效益降薪酬降&rdquo;的分配理念，进一步激发动力活力。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十二条</span><span class="s1">  所属单位应坚持以岗位价值为依据、以绩效贡献为导向，根据本单位经济效益、劳动效率并参照劳动力市场价位，合理确定各类各层级员工薪酬水平，提高关键岗位和紧缺人才薪酬市场竞争力，合理调整收入分配差距，防止出现超出社会认知和员工接受程度的薪酬水平。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十三条</span><span class="s1">  所属单位应逐级建立内部工效挂钩办法，健全完善全员绩效考核体系，实现员工工资收入与工作业绩和实际贡献紧密挂钩，能增能减。对基层单位，重点突出效益、效率、安全环保等要素，加大向效益贡献大、投入产出效率高的基层单位倾斜，工效挂钩，奖优罚劣；对员工个人，重点突出工作量、工作质量、工作效率、劳动纪律等要素，加大向核心骨干、生产一线及紧缺急需的高层次人才倾斜，以绩定酬，多劳多得。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十四条  </span><span class="s1">所属单位应清理规范工资外收入，不得以各种形式设立和使用&ldquo;小金库&rdquo;发放工资福利。所有工资性支出应当按照有关财务会计制度规定，全部纳入工资总额核算，不得在工资总额之外列支任何工资性支出。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十五条  </span><span class="s1">所属单位应健全完善内部监督机制，增强自律意识，规范管理行为。对收入分配制度、中长期激励计划以及实施方案等关系员工切身利益的重大分配事项，应当履行必要的决策程序和民主程序。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第九章  其他管理规定</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第四十六条</span><span class="s1">  油田公司下达给所属单位的工资总额预算控制数，是所属单位当年计提、发放工资总额的最高限额和财务决算与税务审计的依据，且计提数和实发数应保持一致。年末财务决算时，油田公司对所属单位年度工资总额的提取和使用情况进行专项审核，未经审核确认的，不予进行财务决算。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十七条 </span><span class="s1"> 出现以下特殊情况的，油田公司相应调整其工资总额：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）对人员成建制划转单位，油田公司按其上年实际使用工资总额相应进行划转。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）对新建单位，油田公司参照内部同类型单位工资水平，综合考虑用工人数、队伍结构、所处地域以及业绩指标完成情况等因素确定其工资总额。</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）对发生安全环保责任事故的单位，按照油田公司与所属单位签订的《安全环保责任书》相关约定，扣减当年工资总额。其中：</span>\r\n</p>\r\n<p class="p7">\r\n<span>1. 发生一般A级工业生产安全事故或一般A、B级环境事件的，以及较大及以上道路交通责任事故（主要责任及以上）的，扣减比例为2%；</span>\r\n</p>\r\n<p class="p7">\r\n<span>2. 发生较大工业生产安全事故或环境事件的，扣减比例为4%；</span>\r\n</p>\r\n<p class="p7">\r\n<span>3. 发生重大及以上工业生产安全事故或环境事件的，扣减比例为6%。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十八条  </span><span class="s1">所属单位应在油田公司下达的工资总额预算控制数内提取和使用工资总额，并按规定用于支付员工岗位（技）工资、津贴补贴及奖金等工资性项目，不得改变其使用用途。未经人事部门批准，其他部门不得擅自动用工资总额发放任何名目的奖金。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第四十九条</span><span class="s1">  所属单位应按照有序发放、均衡使用的原则，将油田公司下达的工资总额分解到各月，并结合生产经营节奏及月度关键绩效指标完成情况，合理安排月度工资总额的使用，其中：季节性工作量变化较大的单位，应做到忙季多发、淡季少发。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十条</span><span class="s1">  所属单位应建立人事与财务部门之间的工作协调机制，每月核对工资总额数据，保证口径一致、账实相符。财务部门依据人事部门提供的工资支付审批单和工资发放表，从工资总额中列支项目、支付资金。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十一条</span><span class="s1">  所属单位应按照&ldquo;保工资收入、控其他支出&rdquo;的原则，控制人工成本不合理增长。油田公司将引导所属单位由关注工资总额发生情况向关注全口径人工费用发生情况转变，逐步建立起与人工成本水平、劳动生产率挂钩的薪酬分配体系，实现由工资总额调控向人工成本调控的转变。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第十章  监督与检查</span>\r\n</p>\r\n<p class="p3"></p>\r\n<p class="p5">\r\n<span>第五十二条</span><span class="s1">  油田公司建立工资总额执行动态监控机制，对所属单位工资总额提取发放、人工成本投入产出、工效匹配等情况进行跟踪监测。对执行中工资增长与经济效益不匹配及异常波动的单位，采取发放提示函、约谈等措施，督促所属单位规范执行。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十三条</span><span class="s1">  油田党委组织部（人事部）会同油田公司财务、审计、纪检等部门，通过人事或财务报表审核、专项检查、审计、巡视等多种方式，监督检查所属单位工资总额使用情况。对履行主体责任不到位、工资增长与经济效益严重不匹配、内部收入分配管理不规范、收入分配关系明显不合理的所属单位，油田公司将对其工资总额从严调控。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十四条</span><span class="s1">  所属单位出现超提、超发工资总额行为的，除应当清退并进行相关账务处理外，油田公司还要相应核减其下一年度工资总额基数。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十五条</span><span class="s1">  实行工资总额备案制或周期制管理的单位，出现违反油田公司工资总额管理有关规定的，油田公司将责成其进行整改，情节严重的，还要将其工资总额由备案制或周期制管理调整为核准制管理。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十六条</span><span class="s1">  违反本办法，有下列情形之一的，给予责任单位通报批评；造成严重后果和不良社会影响的，按照集团公司和油田公司有关规定追究责任人员责任：</span>\r\n</p>\r\n<p class="p7">\r\n<span>（一）违反规定超提、超发工资总额的；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（二）工资总额管理中弄虚作假的；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（三）严重违反会计核算、收入分配政策规定的；</span>\r\n</p>\r\n<p class="p7">\r\n<span>（四）违反本办法的其他行为。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p3">\r\n<span>第十一章  附  则</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p5">\r\n<span>第五十七条  </span><span class="s1">本办法由油田党委组织部（人事部）负责解释。</span>\r\n</p>\r\n<p class="p5">\r\n<span>第五十八条  </span><span class="s1">本办法自印发之日起施行。《大庆油田有限责任公司工资总额管理办法（试行）》（庆油发〔2014〕52号）、《大庆油田有限责任公司工资总额预算管理办法》（庆油发〔2022〕46号）同时废止。</span>\r\n</p>\r\n<p class="p7"></p>\r\n<p class="p12">\r\n<span>附件：工效联动增幅（N）确定方式</span>\r\n</p>\r\n<p class="p7"></p>\r\n</body>\r\n</html>\r\n',
        //     wordConetnt:
        //       '\r\n\r\n\r\n大庆油田有限责任公司工资总额管理办法\r\n\r\n第一章  总  则\r\n\r\n第一条  为建立健全与劳动力市场基本适应、与企业经济效益和劳动生产率挂钩的工资决定和正常增长机制，进一步规范大庆油田有限责任公司、大庆石油管理局有限公司（以下统称油田公司）工资总额管理，优化激励约束机制，推进新时代油田高质量发展，根据中国石油天然气集团有限公司（以下简称集团公司）工资总额管理办法有关规定，结合油田公司实际，制定本办法。\r\n第二条  本办法所称工资总额，是指油田公司所属单位（以下简称所属单位）在一个会计年度内直接支付给与本单位建立劳动关系的全部员工的劳动报酬总额，包括工资、奖金、津贴、补贴、加班加点工资、特殊情况下支付的工资等。\r\n本办法所称工资总额管理，是指油田公司依据年度生产经营目标、效益情况和人力资源管理要求，对所属单位工资总额实行预算管理，包括预算编制、预算申报、预算核准或备案、预算执行和清算评价，以及员工工资水平调控等，并进行有效控制和监督的全过程管理。油田公司对所属单位原则上实行工资总额核准制管理，并探索在部分单位实行工资总额备案制、周期制管理。\r\n第三条  工资总额管理遵循以下原则：\r\n（一）坚持战略引领。围绕新时代油田高质量发展总体战略布局，推进实施人才强企工程，强化科技创新激励保障，实行与战略规划相匹配的薪酬策略，引领企业高质量发展，促进战略目标实现。\r\n（二）坚持效益效率导向。所属单位工资总额和员工工资水平的确定以及增长，应当与本单位经济效益和劳动生产率的提高相联系，切实实现员工工资能增能减。\r\n（三）坚持市场化改革方向。充分发挥市场在工资分配中的决定性作用，统筹处理好不同业务、不同单位和各类各层级员工之间的收入分配关系，实现员工工资水平与劳动力市场价位相适应、与企业市场竞争力相匹配。\r\n（四）坚持分级分类管理。按照“油田公司调控总量、所属单位自主分配”的管理模式，实行工资总额分级管理；根据企业功能定位、业务特点和市场化程度差异，实行工资总额分类管理。\r\n第四条  本办法适用于油田公司机关、所属单位。\r\n油田公司及所属单位的全资子公司通过法定程序实施本办法。\r\n油田公司及所属单位的控股公司应依据《公司法》要求建立健全相关规章制度，实现企业规范管理。\r\n\r\n第二章  管理机构及职责\r\n\r\n第五条  油田党委组织部（人事部）是油田公司工资总额的归口管理部门，主要职责如下：\r\n（一）贯彻落实国家、集团公司有关工资总额的方针政策、法律法规和制度规定；\r\n（二）负责起草油田公司工资总额管理制度，并组织实施；\r\n（三）指导所属单位编制和申报工资总额预算，核准及备案其工资总额预算控制数；\r\n（四）负责所属单位工资总额的审批、调控、清算评价及统计分析等工作； \r\n（五）指导所属单位工资总额管理工作；\r\n（六）监督、检查所属单位工资总额执行情况。\r\n第六条  所属单位主要职责：\r\n（一）负责制定内部工效挂钩办法，建立收入与绩效挂钩的能增能减机制；\r\n（二）负责编制和申报工资总额预算，并按油田党委组织部（人事部）核准或备案数进行控制；\r\n（三）负责编制月度工资总额使用计划，并报油田党委组织部（人事部）进行审批；\r\n（四）负责建立薪酬分配激励约束机制，实施内部分配； \r\n（五）负责监督检查内部工资总额执行情况，规范分配秩序；\r\n（六）负责本单位工资总额的发放使用、统计分析等日常管理。\r\n\r\n第三章  工资总额核准制管理\r\n\r\n第七条  对实行工资总额核准制管理的所属单位，构建由工资效益效率联动、效能对标调节和工资水平调控等共同组成、协调运转的工资总额决定机制。油田公司根据所属单位原油（天然气）产量、净利润和劳动生产率等指标完成情况，核定年度工资总额预算控制数。\r\n\r\n第一节  工资效益效率联动机制\r\n\r\n第八条  所属单位工资总额与关键效益效率指标挂钩联动。\r\n效益指标主要考核原油（天然气）产量、净利润；\r\n效率指标主要考核劳动生产率，以人均油气当量产量、人均营业收入等指标为主。\r\n第九条  所属单位年度工资总额包括工资总额基数、工效挂钩增量、工资总额特别奖励和工资总额单列额度等四个部分。\r\n年度工资总额=工资总额基数+工效挂钩增量+工资总额特别奖励+工资总额单列额度\r\n第十条  工资总额基数以上年清算数为基础，剔除减员减资额度和单列额度等，并考虑当年用工计划内增人增资、岗位（技）工资考核晋档增资等额度后，区分油气生产型单位、市场经营型单位、科研单位和其他单位四种类型，按一定比例核定，其中：\r\n市场经营型单位核定比例根据本单位上年及当年效益盈亏情况，分连续两年盈利、一年盈利一年亏损、连续两年亏损，设置三档标准，按照每档2%~3%的档差，在100%~90%区间内确定；\r\n油气生产型单位、科研单位核定比例原则上参照市场经营型单位最高档标准；\r\n其他单位核定比例参照市场经营型单位中间档标准。\r\n第十一条  工效挂钩增量区分油气生产型单位、市场经营型单位、科研单位和其他单位四种类型，根据工资总额基数和工效联动增幅（N）确定。\r\n工效挂钩增量=工资总额基数×工效联动增幅（N）\r\n工效联动增幅（N）依据所属单位考核指标完成情况分别确定（见附件）。\r\n第十二条  对油气生产型单位中的亏损单位，奖励标准原则上按规定比例的80%执行，扣罚标准按规定比例的120%执行。\r\n对市场经营型单位中利润贡献占比小于2%的单位，奖罚标准应根据实际经营情况予以适当调整。\r\n第十三条  工资总额特别奖励纳入所属单位次年工资总额基数，包括以下事项：\r\n（一）对通过内部盘活消化新增产能、新建装置等用工需求且成效明显的单位，按消化新增人员工资总额的50%予以奖励。\r\n（二）对主动开展劳务输出、业务承揽（不含工程类承揽）的单位，分别对输出、输入单位予以奖励，其中：\r\n1. 输出到集团公司以外的，每输出1人奖励2万元；\r\n2. 输出到集团公司内部的，每输出1人奖励1万元；\r\n3. 输出到油田公司内部其他单位的，每输出1人奖励0.5万元，输入单位按输出单位奖励额度的50%予以奖励。\r\n（三）对在实施人才强企工程、推进三项制度改革等重点工作中取得显著成效的所属单位，油田公司视情况给予一定额度工资总额特别奖励。\r\n第十四条  工资总额单列额度实行特殊事项清单管理。列入特殊事项清单范围的事项，在所属单位年度工资总额预算内实行专项核算、单列管理，不纳入工资总额基数、不参与挂钩考核：\r\n（一）对承担国家重大专项任务和高新工程，设立国家重点实验室、国家工程实验室等国家级创新平台，吸引保留院士、海外高层次人才引进计划专家，承担关键核心技术攻关、打造能源与化工领域原创技术策源地和培育油气产业链“链长”科技创新核心能力相关重大基础研究等任务的科研领军人才及团队，所需工资总额予以单列。\r\n（二）对承担国家科技重大专项、国家重点研发计划项目间接费用中提取的绩效奖励，科技型企业实施分红激励，“科改示范企业”改革增资额度，以及开展模拟分红等，所需工资总额予以单列。\r\n（三）按照国家和国资委要求，对强化科技创新人员激励、系统推进科技创新激励保障机制建设涉及的其他重大事项所需工资总额，按照“一事一议”的方式，经油田党委组织部（人事部）认定后可予以单列。\r\n（四）通过市场化机制选聘、按照市场化原则确定业绩目标、实行协议工资制的高层次国际化人才、石油领域科学家、高层次职业经理人等高精尖缺人才，所需工资总额予以单列。\r\n（五）对涉及境外业务的单位，其境外员工工资总额实行单列管理。境外单位应按照工效同向联动、合理匹配的原则，申报境外员工工资总额单列额度，油田公司在下达全年预算控制数时予以单列。\r\n（六）从事外部市场开发的单位，其按外部市场开发收现金额一定比例计提的工资总额实行单列管理，包括项目收入计提额、年度收入增长计提额两部分。项目收入计提额以外部市场开发项目形成的现金收入为基数，区分国际、国内非中石油、国内中石油内部、大庆本地外部四个市场类型，盈利单位分别按4%、3%、2%、1%比例计提；亏损单位计提比例按盈利单位的50%确定。年度收入增长计提额以外部市场开发年度现金收入增长值为基数，按3%~5%比例计提。\r\n（七）具有发展潜力、代表油田公司未来发展方向的新能源、新材料、新事业等战略新兴领域，以及需要重点培育的经济增长点、重大改革试点等，所需工资总额可予以单列。\r\n（八）油田公司专项奖励额度予以单列管理。\r\n（九）其他需要单列管理事项，按照“一事一议”的方式，经油田党委组织部（人事部）认定后可予以单列。\r\n第十五条  油田公司根据生产经营和效益形势，视集团公司下达工资总额预算控制数情况，明确当年工资总额预算编制要求，确定工资总额基数核定比例、工效联动增幅奖罚比例，以及盈利和亏损单位工资总额增幅封顶线和保底线等，合理调控所属单位工资水平，重点向利润贡献大、劳动生产率和人工成本投入产出效率高、科技创新成效显著以及工作条件较为艰苦的单位倾斜。\r\n\r\n第二节  效能对标调节机制\r\n\r\n第十六条  油田公司根据人工成本利润率、人事费用率等人工成本投入产出效率指标对标结果，引入人工成本效能系数，对所属单位工效联动增幅进行调整。\r\n第十七条  人工成本效能系数按照所属单位人工成本投入产出效率指标的纵向对标，以及同类型单位内横向对标的双维度九宫格模型确定。其中：纵向对标结果按所属单位同比上年实际完成值变化情况确定；横向对标结果按所属单位实际完成值与同类型单位对标结果确定，分位值越高代表指标在行业越优秀。\r\n\r\n\r\n\r\n\r\n持平\t适度\r\n奖励\t重点\r\n奖励\r\n适度\r\n压缩\t持平\t适度\r\n奖励\r\n严格\r\n压缩\t适度\r\n压缩\t持平\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n第十八条  根据效能对标结果，划分为“严格压缩、适度压缩、持平、适度奖励和重点奖励”五种情形，在0.8~1.2的区间范围内确定所属单位人工成本效能系数，相应调减或调增工效联动增幅。\r\n\r\n第三节  工资水平调控机制\r\n\r\n第十九条  油田公司根据所属单位员工平均工资与油田公司整体水平对标结果，建立工资水平调控机制，按照0.8~1.2的调控系数，适度平衡不同单位间工资水平，合理调节各类各层级员工的工资收入差距，统筹兼顾油田公司整体分配公平。\r\n第二十条  对人均工资增幅较高且上年员工平均工资高于油田公司整体水平较多的单位，按0.8~1.0的调控系数，适当调减工效联动增幅。因员工队伍结构差异、所在行业劳动力市场价位较高等因素导致员工平均工资水平较高的，可适当考虑有关因素，不予调减或适度调减工效联动增幅。\r\n第二十一条  对上年员工平均工资低于油田公司整体水平较多的单位，按1.0~1.2的调控系数，适当调增工效联动增幅。\r\n第二十二条  油田公司机关、所属单位机关员工平均工资增幅，原则上应当分别不高于油田公司、所属单位全部员工平均工资增幅。对机关员工平均工资水平不合理过高、偏高的单位，油田公司应对其工资增幅进行控制。\r\n\r\n第四章  工资总额备案制管理\r\n\r\n第二十三条  对纳入国资委重大改革试点范围，或处于充分竞争行业和领域，且已建立规范董事会、法人治理结构健全、三项制度改革到位、收入分配管理规范的单位，以及战略新兴领域的新建单位，可申请实行工资总额备案制管理。\r\n第二十四条  工资总额备案制管理是指符合条件的所属单位，在油田公司授权、指导下，根据本单位生产经营特点，自主确定工效挂钩指标、工资增幅等预算规则，经油田公司审核同意后，合理确定工资总额的管理方式。油田公司审核申请单位工资总额管理办法，并对申请单位年度工资总额预算方案及执行情况进行备案、监督和评价。\r\n第二十五条  申请单位依据授权拟订本单位工资总额管理办法，报油田公司审核同意后，按照办法规定自主编制年度工资总额预算方案并组织实施。\r\n（一）申请单位应遵循效益增长与效益下降时激励约束总体平衡的原则，执行国家政策规定和集团公司、油田公司管理制度要求，建立健全员工工资与经济效益同向联动、能增能减的工资总额管理办法，合理有序确定工资总额增减幅度。\r\n（二）经济效益较上年增长的，在不超过经济效益增幅、且人工成本利润率提高的前提下，工资总额增幅可采取与净利润完成情况紧密挂钩的方式合理确定，工资总额增幅最高线可适当放宽1~2个百分点，但未完成预算目标的单位工资总额不得增长。经济效益较上年下降的，原则上工资总额应下降，降幅根据经济效益下降幅度等情况合理确定，最低降至上年实发数的80%。\r\n（三）在按效益确定工资总额增幅的基础上，申请单位应建立业绩薪酬双对标体系，依据劳动生产率、人工成本投入产出效率和薪酬水平市场化对标等情况调节工资总额预算水平。\r\n第二十六条  申请单位工资总额预算方案应当包括预算基数及口径、年度经济效益指标和工资总额预算安排、人工成本投入产出效率情况及机关人员工资预算安排等内容。\r\n第二十七条  申请单位工资总额预算方案和预算调整方案履行内部决策程序后，分别于每年4月和10月以书面形式报送油田党委组织部（人事部）备案。\r\n\r\n第五章  工资总额周期制管理\r\n\r\n第二十八条  对行业周期性明显，或处于转方式、调结构等特殊发展阶段，在一定时期内经济效益年度间波动较大、且内控机制健全、收入分配管理规范的所属单位，可申请实行工资总额周期制管理。\r\n第二十九条  工资总额周期制管理是指在工资总额核准制管理基础上，允许符合条件的所属单位根据生产经营节奏变化，对工资总额年度间发放节奏进行合理安排的管理方式。一般以三个会计年度为一个管理周期。\r\n第三十条  实行工资总额周期制管理的所属单位按以下规定执行：\r\n（一）周期内各年度按工资总额核准制计算当年理论预算控制数、实行记账管理，并以此为基础核定周期内下一年度工资总额基数。\r\n（二）各年度实际发放工资总额低于理论预算控制数形成的剩余额度，不在财务报表中通过工资结余的方式予以体现，实行记账管理，可在周期内以后年度调剂使用。单位经济效益下降时，可适当使用部分剩余额度，最高可保持工资总额零增长，发挥以丰补歉的作用。\r\n（三）周期内各年度实际使用工资总额预算需报油田公司事前备案。\r\n（四）超出当年理论预算控制数发放的工资总额，不得影响当期生产经营任务目标的完成。\r\n\r\n第六章  专项奖励管理\r\n\r\n第三十一条  油田公司设立专项奖励，对在贯彻落实油田公司发展战略且未纳入工效挂钩和业绩考核的重点工作、重大项目中，做出突出贡献的单位和个人给予奖励。\r\n第三十二条  奖励项目按照“少而精”的设奖原则，控制在10项以内；奖励总额原则上按每年不超过工资总额1%的比例进行控制，计入所属单位工资总额。\r\n第三十三条  专项奖励由设奖（申请）部门制定奖励方案，经油田公司分管领导审核和主要领导审批，并履行规定程序后，报油田党委组织部（人事部）办理支付审批手续。具体实施程序按油田公司专项奖励管理规定执行。\r\n第三十四条  专项奖励应向在专项工作中做出突出贡献的单位和个人发放，不得向企业领导人员和与专项工作无关的其他人员发放。\r\n第三十五条  下级单位不得以任何名义、任何形式向上级单位员工发放任何性质的专项奖励，具有经济业务合作关系的单位也不得向合作单位员工发放专项奖励。\r\n第三十六条  除有明确保密要求的奖励项目以外，专项奖励必须履行公示程序。专项奖励按税前标准下达，获奖个人应依法缴纳个人所得税。\r\n第三十七条  所属单位层面的专项奖励，应按照油田公司有关规定执行，奖励项目、奖励标准、奖励总额、管理程序等应符合油田公司相关要求。对所属单位自行设立的专项奖励，油田公司不予核增工资总额，由所属单位在油田公司核定的工资总额预算控制数内自行消化解决。\r\n\r\n第七章  工资总额管理程序\r\n\r\n第三十八条  工资总额管理程序包括预算编制、预算申报、预算核准或备案、预算执行、清算评价等环节。\r\n第三十九条  所属单位按照油田公司明确的预算编制规则，依据本单位经济效益、劳动生产率等指标完成情况和成本承受能力，合理编制年度工资总额预算，并按规定时间节点进行申报。\r\n第四十条  每年年初，油田公司根据集团公司当年工资总额预算管理具体要求，指导所属单位预算编制、申报工作，并下达所属单位年度工资总额预算计划数；\r\n每年年底，油田公司在集团公司核定的工资总额预算控制数内，组织所属单位申报全年工资总额预算，并根据油田公司机关相关部门审核后的挂钩指标预计完成情况以及上年清算评价结果，核定下达所属单位年度工资总额预算控制数；\r\n次年，根据总考核结果，对上年工资总额预算执行情况进行清算评价，并对净利润指标预测值高于实际值10%以上、其他指标预测值高于实际值5%以上的所属单位，因高出部分导致上年相应多预付的工资额度，按2倍予以扣减。\r\n\r\n第八章  企业内部分配管理\r\n\r\n第四十一条  所属单位应充分发挥内部分配主体作用，探索符合行业特点、满足自身发展需要的内部分配方法，引导员工牢固树立“单位总额靠效益、个人收入凭贡献”“效益升薪酬升、效益降薪酬降”的分配理念，进一步激发动力活力。\r\n第四十二条  所属单位应坚持以岗位价值为依据、以绩效贡献为导向，根据本单位经济效益、劳动效率并参照劳动力市场价位，合理确定各类各层级员工薪酬水平，提高关键岗位和紧缺人才薪酬市场竞争力，合理调整收入分配差距，防止出现超出社会认知和员工接受程度的薪酬水平。\r\n第四十三条  所属单位应逐级建立内部工效挂钩办法，健全完善全员绩效考核体系，实现员工工资收入与工作业绩和实际贡献紧密挂钩，能增能减。对基层单位，重点突出效益、效率、安全环保等要素，加大向效益贡献大、投入产出效率高的基层单位倾斜，工效挂钩，奖优罚劣；对员工个人，重点突出工作量、工作质量、工作效率、劳动纪律等要素，加大向核心骨干、生产一线及紧缺急需的高层次人才倾斜，以绩定酬，多劳多得。\r\n第四十四条  所属单位应清理规范工资外收入，不得以各种形式设立和使用“小金库”发放工资福利。所有工资性支出应当按照有关财务会计制度规定，全部纳入工资总额核算，不得在工资总额之外列支任何工资性支出。\r\n第四十五条  所属单位应健全完善内部监督机制，增强自律意识，规范管理行为。对收入分配制度、中长期激励计划以及实施方案等关系员工切身利益的重大分配事项，应当履行必要的决策程序和民主程序。\r\n\r\n第九章  其他管理规定\r\n\r\n第四十六条  油田公司下达给所属单位的工资总额预算控制数，是所属单位当年计提、发放工资总额的最高限额和财务决算与税务审计的依据，且计提数和实发数应保持一致。年末财务决算时，油田公司对所属单位年度工资总额的提取和使用情况进行专项审核，未经审核确认的，不予进行财务决算。\r\n第四十七条  出现以下特殊情况的，油田公司相应调整其工资总额：\r\n（一）对人员成建制划转单位，油田公司按其上年实际使用工资总额相应进行划转。\r\n（二）对新建单位，油田公司参照内部同类型单位工资水平，综合考虑用工人数、队伍结构、所处地域以及业绩指标完成情况等因素确定其工资总额。\r\n（三）对发生安全环保责任事故的单位，按照油田公司与所属单位签订的《安全环保责任书》相关约定，扣减当年工资总额。其中：\r\n1. 发生一般A级工业生产安全事故或一般A、B级环境事件的，以及较大及以上道路交通责任事故（主要责任及以上）的，扣减比例为2%；\r\n2. 发生较大工业生产安全事故或环境事件的，扣减比例为4%；\r\n3. 发生重大及以上工业生产安全事故或环境事件的，扣减比例为6%。\r\n第四十八条  所属单位应在油田公司下达的工资总额预算控制数内提取和使用工资总额，并按规定用于支付员工岗位（技）工资、津贴补贴及奖金等工资性项目，不得改变其使用用途。未经人事部门批准，其他部门不得擅自动用工资总额发放任何名目的奖金。\r\n第四十九条  所属单位应按照有序发放、均衡使用的原则，将油田公司下达的工资总额分解到各月，并结合生产经营节奏及月度关键绩效指标完成情况，合理安排月度工资总额的使用，其中：季节性工作量变化较大的单位，应做到忙季多发、淡季少发。\r\n第五十条  所属单位应建立人事与财务部门之间的工作协调机制，每月核对工资总额数据，保证口径一致、账实相符。财务部门依据人事部门提供的工资支付审批单和工资发放表，从工资总额中列支项目、支付资金。\r\n第五十一条  所属单位应按照“保工资收入、控其他支出”的原则，控制人工成本不合理增长。油田公司将引导所属单位由关注工资总额发生情况向关注全口径人工费用发生情况转变，逐步建立起与人工成本水平、劳动生产率挂钩的薪酬分配体系，实现由工资总额调控向人工成本调控的转变。\r\n\r\n第十章  监督与检查\r\n\r\n第五十二条  油田公司建立工资总额执行动态监控机制，对所属单位工资总额提取发放、人工成本投入产出、工效匹配等情况进行跟踪监测。对执行中工资增长与经济效益不匹配及异常波动的单位，采取发放提示函、约谈等措施，督促所属单位规范执行。\r\n第五十三条  油田党委组织部（人事部）会同油田公司财务、审计、纪检等部门，通过人事或财务报表审核、专项检查、审计、巡视等多种方式，监督检查所属单位工资总额使用情况。对履行主体责任不到位、工资增长与经济效益严重不匹配、内部收入分配管理不规范、收入分配关系明显不合理的所属单位，油田公司将对其工资总额从严调控。\r\n第五十四条  所属单位出现超提、超发工资总额行为的，除应当清退并进行相关账务处理外，油田公司还要相应核减其下一年度工资总额基数。\r\n第五十五条  实行工资总额备案制或周期制管理的单位，出现违反油田公司工资总额管理有关规定的，油田公司将责成其进行整改，情节严重的，还要将其工资总额由备案制或周期制管理调整为核准制管理。\r\n第五十六条  违反本办法，有下列情形之一的，给予责任单位通报批评；造成严重后果和不良社会影响的，按照集团公司和油田公司有关规定追究责任人员责任：\r\n（一）违反规定超提、超发工资总额的；\r\n（二）工资总额管理中弄虚作假的；\r\n（三）严重违反会计核算、收入分配政策规定的；\r\n（四）违反本办法的其他行为。\r\n\r\n第十一章  附  则\r\n\r\n第五十七条  本办法由油田党委组织部（人事部）负责解释。\r\n第五十八条  本办法自印发之日起施行。《大庆油田有限责任公司工资总额管理办法（试行）》（庆油发〔2014〕52号）、《大庆油田有限责任公司工资总额预算管理办法》（庆油发〔2022〕46号）同时废止。\r\n\r\n附件：工效联动增幅（N）确定方式\r\n\r\n\r\nP0           P50         P100\r\n\r\n纵\r\n向\r\n对\r\n标\r\n结\r\n果\r\n︵同\r\n历\r\n史\r\n比︶\r\n\r\n提\r\n升\r\n\r\n基\r\n本\r\n持\r\n平\r\n\r\n下\r\n降\r\n\r\n横向对标结果（同类型单位比）\r\n\r\n\r\n\r\nPAGE  \r\n\r\n\r\n\r\n20\r\n\r\n\r\n\r\n',
        //     esUrl: 'flzdzy',
        //     id: 3686,
        //   },
        //   msg: '',
        // }

        this.previewHtml = this.highlightKeywords(
          res.data.htmlContent,
          this.queryForm.keyword
        )
        this.previewVisible = true
        setTimeout(() => {
          this.scrollToFirstKeyword()
        }, 0.5)
      },
      onDownload(row) {
        window.open(row.furl)
      },
      // 高亮关键词的函数
      highlightKeywords(text, keywords) {
        // 创建正则表达式，匹配所有关键词
        var regex = new RegExp(keywords, 'gi')
        return text.replace(regex, function (match) {
          return (
            '<span style="color:red;background:yellow" class="highlight">' +
            match +
            '</span>'
          )
        })
      },
      scrollToFirstKeyword() {
        const content = this.$refs.content
        const highlights = content.querySelectorAll('.highlight')
        if (highlights.length > 0) {
          highlights[0].scrollIntoView({ behavior: 'smooth' })
        }
      },
      async fetchData() {
        if (!this.queryForm.keyword) {
          this.$message.error('请输入关键词')
          return
        }
        const params = { ...this.queryForm }
        const res = await getGJSUList(params)
        // const res = {
        //   code: 1,
        //   msg: '成功',
        //   data: {
        //     pageSize: 10,
        //     pageNumber: 1,
        //     totalRecord: 13,
        //     totalPage: 2,
        //     tlist: [
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 2,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442853000,
        //             name: '企管法规管理',
        //             tenantId: 0,
        //             id: 48,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "，截止停业日期，XXXX公司共有应收账款   万元，其他应收款   万元；应付款   万元，其他应付款   万元；固定资产原值   万元，净值   万元（如有纠纷、担保、抵押贷款、账外资金、账外账、“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>XX年XX月XX日\t \t \t要求：\t随停业承诺书一并上报以下资料：企业债权情况明细表；企业债务情况明细表；企业资产情况盘点表；纠纷、担保、抵押贷款、账外资金、账外账、“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/23ee1b39d446485f9d3ad3e125bd3359cc468fb41ccbffd024a829af502e0773.docx',
        //         id: 3489,
        //         dname:
        //           '大庆油田有限责任公司停业法人企业管理办法（有效）-庆油发〔2021〕165号-20211118.docx',
        //         did: 3637,
        //         agcId: 48,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 2,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442853000,
        //             name: '企管法规管理',
        //             tenantId: 0,
        //             id: 48,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "油田公司审计部（审计中心）在对所属单位开展各项审计业务过程中，发现油田公司员工未履行或未正确履行职责，可能存在下列情形之一的，应移送油田纪委办公室进行处置处理：\n（一）涉嫌贪污、截留挪用公款的；\n（二）私设“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”，对审计查证“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”的资金形成、使用去向、用途及金额等不予配合的；\n（三）未履行或未正确履行岗位管理职责，导致国有资产流失或企业利益严重受损的；\n（四）在经营管理工作中存在其他失职、渎职行为，造成严重后果的",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/00cfd3dc829047c6db57ba3047675eccf551382b34af3e3d006bc795e88bb136.docx',
        //         id: 3500,
        //         dname:
        //           '大庆油田有限责任公司法律纠纷和审计发现问题线索移送管理规定（有效）-庆油发〔2022〕129号-20220816.docx',
        //         did: 3648,
        //         agcId: 48,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 6,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442893000,
        //             name: '人事劳资管理',
        //             tenantId: 0,
        //             id: 52,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "第四十四条  所属单位应清理规范工资外收入，不得以各种形式设立和使用“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”发放工资福利。所有工资性支出应当按照有关财务会计制度规定，全部纳入工资总额核算，不得在工资总额之外列支任何工资性支出。",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/c0894f2d2437fd42709ba68a64c218a117d3f27fc142ebc0da8e008756c3af7a.doc',
        //         id: 3686,
        //         dname:
        //           '《大庆油田有限责任公司工资总额管理办法》（有效）-庆油发〔2022〕215号-20221220.doc',
        //         did: 3837,
        //         agcId: 52,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "（一）商业财产保险赔款、退保等收入未按规定及时入账，形成账外账和<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>；\n（二）收取回扣或者变相回扣；\n（三）以保险为手段谋取私利或其他不合法利益；\n（四）未按规定选择保险机构；\n（五）未按规定审批或备案",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/017b8a4506fa9fa11910c6b88f73b5090e1e98d01d987b57db72e7a05cfc19c9.docx',
        //         id: 3804,
        //         dname:
        //           '大庆油田有限责任公司商业财产保险管理办法（有效）-庆油发〔2022〕169号-20221110.docx',
        //         did: 3969,
        //         agcId: 47,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "（四）设立“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”、账外设账。\r\n（五）从事融资性贸易、非法集资、违规拆借资金及违规对外融资。\r\n（六）违规办理理财、投资股票及期货等高风险业务。",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/2bf1a3427160580c44418ec877affbfb128f570bf74012ceab64fbf590e0482c.doc',
        //         id: 3809,
        //         dname:
        //           '大庆油田有限责任公司资金内部控制管理实施细则（有效）-庆油发〔2021〕197号-20211223.doc',
        //         did: 3974,
        //         agcId: 47,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "在金融机构的存款或代保管的有价证券到期不能收回；\t（二）银行账户或境外汇款被冻结；\t（三）银行存款所在的境内外金融机构关闭、停业整顿，或因其他原因不能正常营业；\t（四）违反国家法律法规和会计制度规定，设置账外账、<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/053f942a64220cc48273556b1ed24a36295550683cca5a2d88d476be1f582d62.docx',
        //         id: 3844,
        //         dname:
        //           '大庆油田有限责任公司重大财务会计事项报告制度（有效）-庆油发〔2021〕168号-20211116.docx',
        //         did: 4009,
        //         agcId: 47,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "（一）保留账外公款，私设“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”的； \r\n（二）截留、挤占、挪用食堂资金的； \r\n（三）出租、出借和转让食堂账户的；\r\n（四）将食堂资金存入个人账户的； \r\n（五）编造用途套取食堂现金的；\r\n（六）有其他违反资金管理制度情形的",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/bf08adf590bad14d50fcaebab73b9fd6c6d02ffe37dcc98263b0be827ef77c09.doc',
        //         id: 3856,
        //         dname:
        //           '大庆油田有限责任公司食堂财务管理办法（有效）-庆油发〔2021〕196号-20211223.doc',
        //         did: 4021,
        //         agcId: 47,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "（一）利用特殊资金设立“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”的；\r\n（二）挤占、截留、挪用、私存特殊资金的；\r\n（三）擅自改变特殊资金用途的；\r\n（四）擅自开立、未按规定开立特殊资金银行账户，或者出借特殊资金银行账户的；\r\n（五）",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 0,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/2081693ea0800b0bff140c3b87f3870adbc5e9af148ba88a0f5f166e413589b4.doc',
        //         id: 3870,
        //         dname:
        //           '大庆油田有限责任公司特殊资金管理实施细则（有效）-庆油发〔2021〕199号-20211223.doc',
        //         did: 4035,
        //         agcId: 47,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 2,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442853000,
        //             name: '企管法规管理',
        //             tenantId: 0,
        //             id: 48,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "第六条  公司审计部门在对公司所属单位开展各项审计业务过程中，发现公司员工未履行或未正确履行职责，可能存在下列情形之一的，应向纪检监察部门移交处理：\t（一）涉嫌贪污、截留挪用公款的；\t（二）私设“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”，对审计查证“<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>”的资金形成、使用去向、用途及金额等不予配合的；\t（三）未履行或未正确履行岗位管理职责，导致国有资金、国有资产流失，企业利益严重受损的；\t（四）在经营管理工作中存在其他失职、渎职行为",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 1,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/5b35c8ad22740681362b5fef968134811d00cfc90705ff887f0661f9e4d6d715.docx',
        //         id: 3479,
        //         dname:
        //           '大庆油田有限责任公司法律纠纷和审计工作发现涉嫌违纪违规问题线索移交管理办法（试行）（失效）-庆油发〔2017〕70号.docx',
        //         did: 3627,
        //         agcId: 48,
        //       },
        //       {
        //         creator: '1',
        //         splitWords: ['金', '库', '小'],
        //         esUrl: 'flzdzy',
        //         level: 10,
        //         updateTime: 1726216462000,
        //         agcList: [
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1679868062000,
        //             sort: 0,
        //             parentId: 0,
        //             updater: '',
        //             deleted: false,
        //             createTime: 1679610402000,
        //             name: '总纲',
        //             tenantId: 0,
        //             id: 1,
        //             ancestors: '',
        //             status: 0,
        //           },
        //           {
        //             creator: '',
        //             isparent: null,
        //             updateTime: 1715361647000,
        //             sort: 2,
        //             parentId: 1,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679610439000,
        //             name: '法规制度指引',
        //             tenantId: 0,
        //             id: 3,
        //             ancestors: ',1',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 3,
        //             parentId: 3,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1679901501000,
        //             name: '油田公司规章制度',
        //             tenantId: 0,
        //             id: 11,
        //             ancestors: '1,3',
        //             status: 0,
        //           },
        //           {
        //             creator: '1',
        //             isparent: null,
        //             updateTime: 1695160254000,
        //             sort: 1,
        //             parentId: 11,
        //             updater: '1',
        //             deleted: false,
        //             createTime: 1690442842000,
        //             name: '财务资产管理',
        //             tenantId: 0,
        //             id: 47,
        //             ancestors: '1,3,11',
        //             status: 0,
        //           },
        //         ],
        //         updater: '1',
        //         htmlContent: null,
        //         frameContent:
        //           "一）在金融机构的存款或代保管的有价证券到期不能收回；\t（二）银行帐户被冻结；\t（三）单位存款所在的金融机构关闭、停业整顿，或因其他原因不能正常营业；\t（四）违反国家法律法规和会计制度规定，设置帐外帐、<span style='color:red'>小</span><span style='color:red'>金</span><span style='color:red'>库</span>",
        //         wordConetnt: null,
        //         deleted: false,
        //         createTime: 1726216462000,
        //         fileStatus: 1,
        //         furl: 'http://192.0.2.11:48080/admin-api/infra/file/5/get/1fafbddae455ecd81dde34999b1a2f151fee0297db1ff90fb3c674411256509f.docx',
        //         id: 3805,
        //         dname:
        //           '大庆石油管理局重大财务会计事项报告制度（失效）-庆局发[2001]76号.docx',
        //         did: 3970,
        //         agcId: 47,
        //       },
        //     ],
        //   },
        //   result: null,
        // }

        this.total = res.data.totalRecord
        this.list = res.data.tlist
      },
    },
  }
</script>

<style scoped>
  .highlight {
    color: red !important;
  }
</style>
