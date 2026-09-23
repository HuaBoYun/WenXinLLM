<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="经营状况"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          招投标信息
        </div>
      </div>
      <el-table v-loading="listLoading" :data="findByManageList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="发布时间"
          prop="publishTime"
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="标题"
          prop="title"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="采购人"
          prop="purchaser"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          债券信息
        </div>
      </div>
      <el-table v-loading="listLoading" :data="zqxxList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="发行日期"
          :formatter="formatDate"
          prop="publishTime"
        />
        <el-table-column
          align="center"
          label="债券名称"
          prop="bondName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="债券代码"
          prop="bondNum"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="债券类型"
          prop="bondType"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="最新评级"
          prop="debtRating"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">招聘</div>
      </div>
      <el-table v-loading="listLoading" :data="zpxxList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="招聘职称" prop="title" />
        <el-table-column
          align="center"
          label="招聘公司"
          prop="companyName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="薪水"
          prop="oriSalary"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="来源"
          prop="source"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="所在区"
          prop="district"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="工作经验"
          prop="experience"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="学历"
          prop="education"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="时间"
          prop="createTime"
          :formatter="formatDate"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          税务评级
        </div>
      </div>
      <el-table v-loading="listLoading" :data="swpjList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="年份" prop="year" />
        <el-table-column
          align="center"
          label="纳税评级"
          prop="grade"
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
          label="纳税人识别号"
          prop="idNumber"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="评价单位"
          prop="evalDepartment"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          抽查检查
        </div>
      </div>
      <el-table v-loading="listLoading" :data="ccjcList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="日期" prop="checkDate" />
        <el-table-column
          align="center"
          label="类型"
          prop="checkType"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="结果"
          prop="checkResult"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="检查实施机关"
          prop="checkOrg"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import {
    findbyManage,
    zqxxList,
    zpxxList,
    swpjList,
    ccjcList,
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
        zpxxList: [],
        swpjList: [],
        ccjcList: [],
        zqxxList: [],
      }
    },
    created() {},
    methods: {
      formatDate(item, column) {
        return parseTime(item[column.property], '{y}-{m}-{d}')
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
        this.fetchFindbyManage()
        this.fetchzzqxx()
        this.fetchzpxxList()
        this.fetchswpjList()
        this.fetchccjcList()
      },
      // 招投标
      async fetchFindbyManage() {
        let dataList = await findbyManage({
          pageNum: 1,
          id: this.id,
          name: this.companyName,
        })
        this.findByManageList = dataList.data
        this.findByManageList = this.findByManageList
          ? this.findByManageList
          : []
        for (let i = 0; i < this.findByManageList.length; i++) {
          this.findByManageList[i].index = i + 1
        }
      },
      // 债券信息
      async fetchzzqxx() {
        let dataList = await zqxxList({
          keyword: this.companyName,
          pageNum: 1,
          pageSize: 9999,
        })
        this.zqxxList = dataList.data
        console.dir(this.zqxxList)
        this.zqxxList = this.zqxxList ? this.zqxxList : []
        for (let i = 0; i < this.zqxxList.length; i++) {
          this.zqxxList[i].index = i + 1
        }
      },
      // 招聘信息
      async fetchzpxxList() {
        let dataList = await zpxxList({
          keyword: this.companyName,
          pageNum: 1,
          pageSize: 9999,
        })
        this.zpxxList = dataList.data
        this.zpxxList = this.zpxxList ? this.zpxxList : []
        for (let i = 0; i < this.zpxxList.length; i++) {
          this.zpxxList[i].index = i + 1
        }
      },
      // 税务评级
      async fetchswpjList() {
        let dataList = await swpjList({
          keyword: this.companyName,
          pageNum: 1,
          pageSize: 9999,
        })
        this.swpjList = dataList.data
        this.swpjList = this.swpjList ? this.swpjList : []
        for (let i = 0; i < this.swpjList.length; i++) {
          this.swpjList[i].index = i + 1
        }
      },
      // 抽查检查
      async fetchccjcList() {
        let dataList = await ccjcList({
          companyName: this.companyName,
          pageNum: 1,
          pageSize: 9999,
        })
        this.ccjcList = dataList.data
        this.ccjcList = this.ccjcList ? this.ccjcList : []
        for (let i = 0; i < this.ccjcList.length; i++) {
          this.ccjcList[i].index = i + 1
        }
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
