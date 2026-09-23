<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="经营风险"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          经营异常
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="列入日期" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="列入原因"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="决定机关"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          行政处罚
        </div>
      </div>
      <el-table v-loading="listLoading" :data="xzcfList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="决定日期" prop="publishDate" />
        <el-table-column
          align="center"
          label="决定书文号"
          prop="punishNumber"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="类型"
          prop="type"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="决定机关"
          prop="departmentName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="操作"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          行政处罚-信用中国
        </div>
      </div>
      <el-table v-loading="listLoading" :data="xzcfChinaList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="公司名" prop="punishmentName" />
        <el-table-column
          align="center"
          label="地区"
          prop="areaName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="处罚"
          prop="url"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          严重违法
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="列入日期" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="列入原因"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="决定机关"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          股权出质
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="公告时间" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="登记编号"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="出质人"
          prop="ORGNAME"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="质权人" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="状态"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="操作"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          动产抵押
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="登记日期" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="担保范围"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="概况数额"
          prop="ORGNAME"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="概况种类" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="被担保债权数额"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="债务人履行债务的期限"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          欠税公告
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="部门" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="纳税人识别号"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="经营地点"
          prop="ORGNAME"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="税务类型" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="欠税税种"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="欠税余额"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          司法拍卖
        </div>
      </div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          prop="PROJECTCODE"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="公告日期" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="拍卖公告"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="详细信息"
          prop="ORGNAME"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="介绍" prop="PRJOECTNAME" />
        <el-table-column
          align="center"
          label="执行法院"
          prop="AUDITORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="拍卖期限"
          prop="ORGNAME"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="公告url"
          prop="ORGNAME"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import DepartMentDialog from '@/views/setting/auth/components/DepartMentDialog'
  import { jyycList, xzcfList, xzcfChinaList } from '@/api/workbench/telescope'
  export default {
    name: 'UserEdit',
    components: { DepartMentDialog },
    data() {
      return {
        id: '',
        companyName: '',
        disabled: false,
        jyycList: [],
        xzcfList: [],
        xzcfChinaList: [],
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
        listLoading: false,
      }
    },
    created() {},
    methods: {
      // 经营异常
      async fetchJyyc() {
        let dataList = await jyycList({
          pageNum: 1,
          pageSize: 20,
          keyword: this.companyName,
        })
        this.jyycList = dataList.data
        this.jyycList = this.jyycList ? this.jyycList : []
        for (let i = 0; i < this.jyycList.length; i++) {
          this.jyycList[i].index = i + 1
        }
      },
      // 行政处罚
      async fetchxzcf() {
        let dataList = await xzcfList({
          pageNum: 1,
          pageSize: 20,
          keyword: this.companyName,
        })
        this.xzcfList = dataList.data
        this.xzcfList = this.xzcfList ? this.xzcfList : []
        for (let i = 0; i < this.xzcfList.length; i++) {
          this.xzcfList[i].index = i + 1
        }
      },
      // 行政处罚-信用中国
      async fetchxzcfChinaList() {
        let dataList = await xzcfChinaList({
          pageNum: 1,
          pageSize: 20,
          name: this.companyName,
          id: this.id,
        })
        console.dir(dataList)
        this.xzcfChinaList = dataList.data
        this.xzcfChinaList = this.xzcfChinaList ? this.xzcfChinaList : []
        for (let i = 0; i < this.xzcfChinaList.length; i++) {
          this.xzcfChinaList[i].index = i + 1
        }
      },
      handleDep() {
        this.$refs.depart.show()
      },
      handleSelectDep(data) {
        this.form.orgid = data.id
        this.form.orgname = data.label
      },
      showEdit(id, name) {
        this.dialogFormVisible = true
        this.id = id
        this.companyName = name
        this.fetchJyyc()
        this.fetchxzcf()
        this.fetchxzcfChinaList()
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
