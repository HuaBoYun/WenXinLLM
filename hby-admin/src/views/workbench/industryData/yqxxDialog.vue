<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="舆情信息"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div data-v-b123885a="" class="el-divider el-divider--horizontal">
        <div data-v-b123885a="" class="el-divider__text is-center">
          舆情信息
        </div>
      </div>
      <el-table v-loading="listLoading" :data="qixxList">
        <el-table-column
          align="center"
          label="序号"
          prop="index"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="新闻标题" prop="title">
          <template slot-scope="scope">
            <a :href="scope.row.uri" target="_blank">{{ scope.row.title }}</a>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="来源"
          prop="website"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="时间"
          prop="time"
          show-overflow-tooltip
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { userSave, roleList, jobList } from '@/api/setting/auth'
  import { yqxxList } from '@/api/workbench/telescope'
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
        qixxList: [],
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
        this.fetchyqxxList()
      },
      // 舆情信息
      async fetchyqxxList() {
        let dataList = await yqxxList({
          id: this.id,
          name: this.companyName,
        })
        this.qixxList = dataList.data
        console.dir(dataList)
        this.qixxList = this.qixxList ? this.qixxList : []
        for (let i = 0; i < this.qixxList.length; i++) {
          this.qixxList[i].index = i + 1
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
