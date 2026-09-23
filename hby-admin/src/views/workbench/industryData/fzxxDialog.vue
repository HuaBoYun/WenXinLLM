<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="发展信息"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          融资历史
        </div>
      </div>
      <el-table v-loading="listLoading" :data="rzlsList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="时间"
          :formatter="formatDate"
          prop="pubTime"
        ></el-table-column>
        <el-table-column
          align="center"
          label="轮次"
          prop="round"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="估值"
          prop="value"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="金额"
          prop="money"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="比例"
          prop="share"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="投资方"
          prop="tzrIds"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="新闻来源"
          prop="newsTitle"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          核心团队
        </div>
      </div>
      <el-table v-loading="listLoading" :data="hxtdList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="姓名"
          prop="name"
        ></el-table-column>
        <el-table-column
          align="center"
          label="标签"
          prop="title"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="个人信息"
          prop="desc"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          :formatter="formatDate"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          企业业务
        </div>
      </div>
      <el-table v-loading="listLoading" :data="qyywList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="公司名称"
          prop="companyName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="产品"
          prop="product"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="上线时间"
          prop="setupDate"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="业务范围"
          prop="yewu"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          投资事件
        </div>
      </div>
      <el-table v-loading="listLoading" :data="tzsjList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="时间"
          prop="tzdate"
          :formatter="formatDate"
        ></el-table-column>
        <el-table-column
          align="center"
          label="轮次"
          prop="lunci"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="金额"
          prop="money"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="投资方"
          prop="organization_name"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="产品"
          prop="product"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="地区"
          prop="location"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="行业"
          prop="hangye1"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="业务"
          prop="yewu"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          企业业务
        </div>
      </div>
      <el-table v-loading="listLoading" :data="qyywList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="公司名称"
          prop="companyName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="产品"
          prop="product"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="上线时间"
          prop="setupDate"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="业务范围"
          prop="yewu"
          show-overflow-tooltip
        />
      </el-table>
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          竞品信息
        </div>
      </div>
      <el-table v-loading="listLoading" :data="jpxxList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column
          align="center"
          label="产品"
          prop="product"
        ></el-table-column>
        <el-table-column
          align="center"
          label="地区"
          prop="location"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="当前轮次"
          prop="round"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="行业"
          prop="hangye"
          :formatter="formatDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="业务"
          prop="product"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="成立时间"
          prop="setupDate"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column
          align="center"
          label="估值"
          prop="value"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import {
    rzlsList,
    hxtdist,
    qyywList,
    tzsjList,
    jpxxList,
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
        rzlsList: [],
        hxtdList: [],
        qyywList: [],
        tzsjList: [],
        jpxxList: [],
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
        this.fetrzlsList()
        this.fetchHxtdList()
        this.fetchqyywList()
        this.fetchtzsjList()
        this.fetchjpxxjList()
      },
      // 融资历史
      async fetrzlsList() {
        let dataList = await rzlsList({
          id: this.id,
          name: this.companyName,
        })
        this.rzlsList = dataList.data
        this.rzlsList = this.rzlsList ? this.rzlsList : []
        for (let i = 0; i < this.rzlsList.length; i++) {
          this.rzlsList[i].index = i + 1
        }
      },
      // 核心团队
      async fetchHxtdList() {
        let dataList = await hxtdist({
          id: this.id,
          name: this.companyName,
        })
        this.hxtdList = dataList.data
        this.hxtdList = this.hxtdList ? this.hxtdList : []
        for (let i = 0; i < this.hxtdList.length; i++) {
          this.hxtdList[i].index = i + 1
        }
      },
      // 企业业务
      async fetchqyywList() {
        let dataList = await qyywList({
          id: this.id,
          name: this.companyName,
        })
        this.qyywList = dataList.data
        this.qyywList = this.qyywList ? this.qyywList : []
        for (let i = 0; i < this.qyywList.length; i++) {
          this.qyywList[i].index = i + 1
        }
      },
      // 投资事件
      async fetchtzsjList() {
        let dataList = await tzsjList({
          id: this.id,
          name: this.companyName,
        })
        this.tzsjList = dataList.data
        this.tzsjList = this.tzsjList ? this.tzsjList : []
        for (let i = 0; i < this.tzsjList.length; i++) {
          this.tzsjList[i].index = i + 1
        }
      },
      // 竞品信息
      async fetchjpxxjList() {
        let dataList = await jpxxList({
          id: this.id,
          name: this.companyName,
        })
        this.jpxxList = dataList.data
        console.dir(dataList)
        this.jpxxList = this.jpxxList ? this.jpxxList : []
        for (let i = 0; i < this.jpxxList.length; i++) {
          this.jpxxList[i].index = i + 1
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
