<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="司法风险"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          法律诉讼(裁判文书当事人)
        </div>
      </div>
      <el-table v-loading="listLoading" :data="flssList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="案件编码"
          prop="judgment_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人序号"
          prop="serial_number"
          width="100"
        />
        <el-table-column
          align="center"
          label="当事人"
          prop="party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人种类"
          prop="party_kind"
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyKind(scope.row.party_kind) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="当事人类型"
          prop="party_type"
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyType(scope.row.party_type) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="胜败诉"
          prop="verdict"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="集团名称"
          prop="group_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="发布时间"
          prop="insert_time"
          show-overflow-tooltip
          width="150"
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          法院公告
        </div>
      </div>
      <el-table v-loading="listLoading" :data="fyggList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="公告类型"
          prop="announcement_type"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="案件号"
          prop="case_number"
          show-overflow-tooltip
          width="180"
        />
        <el-table-column
          align="center"
          label="当事人"
          prop="party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="原告"
          prop="plaintiff"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="法院名"
          prop="court_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="省份"
          prop="state"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="刊登日期"
          prop="publ_date"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="刊登版面"
          prop="publ_page"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="法官"
          prop="judge_name"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="案件内容"
          prop="content"
          show-overflow-tooltip
          width="200"
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">裁判文书(司法风险)</div>
      </div>
      <el-table v-loading="listLoading" :data="sxrList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="标题"
          prop="info_title"
          show-overflow-tooltip
          width="200"
        />
        <el-table-column
          align="center"
          label="当事人名称"
          prop="party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="案号"
          prop="case_number"
          show-overflow-tooltip
          width="180"
        />
        <el-table-column
          align="center"
          label="案由"
          prop="subject_matter"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="案件类型"
          prop="case_type"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="审理程序"
          prop="procedures"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="胜败诉"
          prop="verdict"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="法院"
          prop="court_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="省份"
          prop="state"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="判决时间"
          prop="referee_date"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="执行标的(万元)"
          prop="execute_target"
          show-overflow-tooltip
          width="130"
        />
        <el-table-column
          align="center"
          label="发布日期"
          prop="pub_date"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="数据源链接"
          prop="link_address"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.link_address"
              type="text"
              size="small"
              @click="openLink(scope.row.link_address)"
            >
              查看详情
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          被执行人(法院公告当事人)
        </div>
      </div>
      <el-table v-loading="listLoading" :data="bzxrList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="法院公告ID"
          prop="rid"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人序号"
          prop="serial_number"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="当事人姓名"
          prop="party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人种类"
          prop="party_kind"
          show-overflow-tooltip
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyKind(scope.row.party_kind) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="当事人类型"
          prop="party_type"
          show-overflow-tooltip
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyType(scope.row.party_type) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="集团名称"
          prop="group_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="发布时间"
          prop="insert_time"
          show-overflow-tooltip
          width="150"
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          开庭公告(立案当事人)
        </div>
      </div>
      <el-table v-loading="listLoading" :data="ktggList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="RID"
          prop="rid"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人名称"
          prop="party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="当事人类型"
          prop="party_type"
          show-overflow-tooltip
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyType(scope.row.party_type) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="当事人种类"
          prop="party_kind"
          show-overflow-tooltip
          width="110"
        >
          <template slot-scope="scope">
            <span>{{ formatPartyKind(scope.row.party_kind) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="当事人种类描述"
          prop="party_kind_desc"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="集团名称"
          prop="group_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="发布时间"
          prop="insert_time"
          show-overflow-tooltip
          width="150"
        />
      </el-table>

      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          限制高消费
        </div>
      </div>
      <el-table v-loading="listLoading" :data="xzgxfList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="被执行人类型"
          prop="people_enforced_type"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="被执行人名称"
          prop="people_enforced"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="关联企业名称"
          prop="related_party_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="性别"
          prop="gender"
          show-overflow-tooltip
          width="80"
        />
        <el-table-column
          align="center"
          label="立案时间"
          prop="filing_time"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="案号"
          prop="case_number"
          show-overflow-tooltip
          width="180"
        />
        <el-table-column
          align="center"
          label="执行法院"
          prop="court_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="申请执行人"
          prop="execute_apply_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="案由"
          prop="subject_matter"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="信息发布日期"
          prop="info_publ_date"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="集团名称"
          prop="group_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="链接地址"
          prop="link_address"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            <el-button v-if="scope.row.link_address" type="text" size="small" @click="openLink(scope.row.link_address)">
              查看详情
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="发布时间"
          prop="insert_time"
          show-overflow-tooltip
          width="150"
        />
      </el-table>

      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          被执行人
        </div>
      </div>
      <el-table v-loading="listLoading" :data="hbbzxrList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="被执行人类型"
          prop="people_enforced_type"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="被执行人名称"
          prop="people_enforced"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="性别"
          prop="gender"
          show-overflow-tooltip
          width="80"
        />
        <el-table-column
          align="center"
          label="身份证号码/组织机构代码"
          prop="cert_code"
          show-overflow-tooltip
          width="180"
        />
        <el-table-column
          align="center"
          label="执行法院"
          prop="court_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="立案时间"
          prop="filing_time"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="案号"
          prop="case_number"
          show-overflow-tooltip
          width="180"
        />
        <el-table-column
          align="center"
          label="执行标的"
          prop="execute_target"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="执行状态"
          prop="execute_state"
          show-overflow-tooltip
          width="120"
        />
        <el-table-column
          align="center"
          label="是否历史"
          prop="if_history"
          show-overflow-tooltip
          width="100"
        />
        <el-table-column
          align="center"
          label="公司名称"
          prop="org_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="公司代码"
          prop="org_code"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="集团名称"
          prop="group_name"
          show-overflow-tooltip
          width="150"
        />
        <el-table-column
          align="center"
          label="发布时间"
          prop="insert_time"
          show-overflow-tooltip
          width="150"
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import {
    flssList,
    fyggList,
    sxrList,
    bzxrList,
    ktggList,
    xzgxfList,
    hbbzxrList,
  } from '@/api/workbench/telescope'
  import { parseTime } from '@/utils/index'
  export default {
    name: 'UserEdit',
    data() {
      return {
        disabled: false,
        form: {
          staffid: undefined,
          username: '',
          realname: '',
          email: '',
          miblephone: '',
          fixedphone: '',
          check: '0',
          name: '0',
          jobid: '',
          roleIdStrs: '',
          fatherorgid: 1,
          status: 1,
          orgid: undefined,
          orgname: '',
          memo: '',
          address: '',
        },
        rules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入用户名' },
          ],
          realname: [
            { required: true, trigger: 'blur', message: '请输入真实姓名' },
          ],
          email: [{ required: false, trigger: 'blur', message: '请输入email' }],
          orgname: [
            { required: true, trigger: 'blur', message: '请选择所属机构' },
          ],
        },
        list: [],
        jobs: [],
        roles: [],
        title: '',
        id: '',
        companyName: '',
        dialogFormVisible: false,
        options: [
          {
            value: 1,
            label: '否',
          },
          {
            value: 0,
            label: '是',
          },
        ],
        data: [],
        findByManageList: [],
        listLoading: false,
        flssList: [],
        fyggList: [],
        sxrList: [],
        bzxrList: [],
        ktggList: [],
        xzgxfList: [],
        hbbzxrList: [],
        zqxxList: [],
      }
    },
    created() {},
    methods: {
      formatDate(item, column) {
        return parseTime(item[column.property], '{y}-{m}-{d}')
      },
      // 打开数据源链接
      openLink(url) {
        if (url) {
          window.open(url, '_blank')
        }
      },
      // 格式化当事人种类
      formatPartyKind(kind) {
        const kindMap = {
          1: '原告',
          2: '被告',
          3: '第三人',
          4: '上诉人',
          5: '被上诉人',
          6: '申请人',
          7: '被申请人',
          8: '其他'
        }
        return kindMap[kind] || kind
      },
      // 格式化当事人类型
      formatPartyType(type) {
        const typeMap = {
          1: '自然人',
          2: '企业',
          3: '其他组织'
        }
        return typeMap[type] || type
      },
      handleDep() {
        this.$refs.depart.show()
      },
      handleSelectDep(data) {
        this.form.orgid = data.id
        this.form.orgname = data.label
      },
      showEdit(id, name) {
        this.id = id
        this.companyName = name
        this.dialogFormVisible = true
        this.fetchFlss()
        this.fetchfygg()
        this.fetchsxrList()
        this.fetchbzxrList()
        this.fetchktggist()
        this.fetchxzgxfList()
        this.fetchhbbzxrList()
      },
      // 法律诉讼(裁判文书当事人)
      async fetchFlss() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询法律诉讼,org_name:', this.companyName)

        let dataList = await flssList({
          pageNum: 1,
          pageSize: 9999,
          keyword: this.companyName,  // 使用 org_name
          group_name: this.companyName,  // group_name 与 org_name 相同
        })
        console.log('法律诉讼数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.flssList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.flssList.length; i++) {
          this.flssList[i].index = i + 1
        }

        console.log('处理后的法律诉讼列表:', this.flssList)
      },
      // 法院公告
      async fetchfygg() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询法院公告,org_name:', this.companyName)

        let dataList = await fyggList({
          keyword: this.companyName,  // 使用 org_name
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('法院公告数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.fyggList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.fyggList.length; i++) {
          this.fyggList[i].index = i + 1
        }

        console.log('处理后的法院公告列表:', this.fyggList)
      },
      // 失信人(司法风险)
      async fetchsxrList() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询司法风险,org_name:', this.companyName)

        let dataList = await sxrList({
          keyword: this.companyName,  // 使用 org_name
          group_name: this.companyName,  // group_name 与 org_name 相同
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('司法风险数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.sxrList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.sxrList.length; i++) {
          this.sxrList[i].index = i + 1
        }

        console.log('处理后的司法风险列表:', this.sxrList)
      },
      // 被执行人(法院公告当事人)
      async fetchbzxrList() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询被执行人(法院公告当事人),org_name:', this.companyName)

        let dataList = await bzxrList({
          keyword: this.companyName,  // 使用 org_name
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('被执行人数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.bzxrList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.bzxrList.length; i++) {
          this.bzxrList[i].index = i + 1
        }

        console.log('处理后的被执行人列表:', this.bzxrList)
      },
      // 开庭公告(立案当事人)
      async fetchktggist() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询开庭公告(立案当事人),org_name:', this.companyName)

        let dataList = await ktggList({
          keyword: this.companyName,  // 使用 org_name
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('开庭公告数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.ktggList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.ktggList.length; i++) {
          this.ktggList[i].index = i + 1
        }

        console.log('处理后的开庭公告列表:', this.ktggList)
      },
      // 限制高消费
      async fetchxzgxfList() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询限制高消费,org_name:', this.companyName)

        let dataList = await xzgxfList({
          keyword: this.companyName,  // 使用 org_name
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('限制高消费数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.xzgxfList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.xzgxfList.length; i++) {
          this.xzgxfList[i].index = i + 1
        }

        console.log('处理后的限制高消费列表:', this.xzgxfList)
      },
      // 被执行人
      async fetchhbbzxrList() {
        // 使用 this.companyName (org_name) 而不是 this.id
        console.log('查询被执行人,org_name:', this.companyName)

        let dataList = await hbbzxrList({
          keyword: this.companyName,  // 使用 org_name
          pageNum: 1,
          pageSize: 9999,
        })
        console.log('被执行人数据:', dataList)

        // 新API返回格式: { data: { list: [...], total: 10 } }
        // 获取 list 数组
        this.hbbzxrList = dataList.data && dataList.data.list ? dataList.data.list : []

        // 添加序号
        for (let i = 0; i < this.hbbzxrList.length; i++) {
          this.hbbzxrList[i].index = i + 1
        }

        console.log('处理后的被执行人列表:', this.hbbzxrList)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let param = JSON.parse(JSON.stringify(this.form))
            param.roleIdStrs = param.roleIdStrs.join(',')
            const { msg } = await userSave(param)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      async menuRole() {
        const {
          data: { tlist },
        } = await roleList({
          pageNumber: 1,
          pageSize: 1000,
        })
        this.roles = tlist
      },
      async menuPosition() {
        const {
          pageInfo: { tlist },
        } = await jobList({
          pageNumber: 1,
          pageSize: 1000,
        })
        this.jobs = tlist
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }
</style>
