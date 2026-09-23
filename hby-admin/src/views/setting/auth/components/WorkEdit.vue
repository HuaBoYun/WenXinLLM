<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="新增"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="工作移交原因" prop="tranenablereason">
            <el-input
              v-model="formData.tranenablereason"
              placeholder="请输入工作移交原因"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对接人姓名" prop="dockstaffname">
            <el-input
              v-model="formData.dockstaffname"
              clearable
              placeholder="请选择对接人姓名"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('dockstaffname')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移交人姓名" prop="transefrstaffname">
            <el-input
              v-model="formData.transefrstaffname"
              clearable
              placeholder="请选择移交人姓名"
              style="width: 78%"
              disabled
            />
            <el-button
              @click="projectManager('transefrstaffname')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移交部门数据" prop="tranorgnamestrs">
            <el-input
              v-model="formData.tranorgnamestrs"
              disabled
              placeholder="请输入移交部门数据"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input
              v-model="formData.createstaffname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="formData.createdate"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col> -->
      </el-form>
    </el-row>
    <yjbmsjEdit ref="edit" @selected="selectChange"></yjbmsjEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <project-manage1 @projectManage="getChildlistPro1" ref="manage1" />
    <project-manage2 @projectManage="getChildlistPro2" ref="manage2" />

    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { transferMenger, transferDetail } from '@/api/setting/auth'
  import yjbmsjEdit from './yjbmsjEdit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import projectManage2 from '@/components/selectPerson.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    components: {
      yjbmsjEdit,
      SelectDepartment,
      projectManage1,
      projectManage2,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        formData: {
          transferid: '',
          tranenablereason: '',
          dockstaffname: '',
          dockstaffid: '',
          transefrstaffname: '',
          transferstaffid: '',
          tranorgidstrs: '',
          tranorgnamestrs: '',
          createdate: formatDate(new Date().toString()),
          createstaffname: JSON.parse(localStorage.getItem('userInfo')).realname,
        },
        rules: {
          tranenablereason: [
            {
              required: true,
              message: '请输入工作移交原因',
              trigger: 'blur',
            },
          ],
          dockstaffname: [
            {
              required: true,
              message: '请输入对接人姓名',
              trigger: 'blur',
            },
          ],
          transefrstaffname: [
            {
              required: true,
              message: '请输入移交人姓名',
              trigger: 'blur',
            },
          ],
          tranorgnamestrs: [
            {
              required: true,
              message: '请输入移交部门数据',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        select: [],
        editId: '',
        proType: '',
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      async showEdit(row, title) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'

        if (row) {
          const {
            data
          } = await transferDetail({ transferid: row.transferid })
          this.formData.transferid = data.transferid
          this.formData.tranenablereason = data.tranenablereason
          this.formData.dockstaffname = data.dockstaffname
          this.formData.dockstaffid = data.dockstaffid
          this.formData.transefrstaffname = data.transefrstaffname
          this.formData.transferstaffid = data.transferstaffid
          this.formData.tranorgnamestrs = data.tranorgnamestrs
          this.formData.tranorgidstrs = data.tranorgidstrs

          // this.formData.createdate = data.createdate
          this.formData.createstaffname = data.createstaffname
          // this.fetchData()
        }
      },
      close() {
        this.formData = {
          transferid: '',
          tranenablereason: '',
          dockstaffname: '',
          dockstaffid: '',
          transefrstaffname: '',
          transferstaffid: '',
          tranorgidstrs: '',
          tranorgnamestrs: '',
          createdate: formatDate(new Date().toString()),
          createstaffname: JSON.parse(localStorage.getItem('userInfo')).realname,
        }
        this.tableData = []
        this.select = []
        this.dialogJdVisible = false
      },
      projectManager(type) {
        this.proType = type
        this.$refs['manage1'].showEdit()
      },
      async fetchData(data) {
        const arr = JSON.parse(JSON.stringify(this.tableData))
        if (!data) {
          this.tableData = [...arr]
          return false
        }
        if (data?.index) {
          arr[data.index - 1] = data.data[0]
          this.tableData = [...arr]
        } else {
          this.tableData = [...arr, ...data.data]
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
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await fundAuditProjectDelete({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
            await this.fetchData()
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.name
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await transferMenger({
              ...this.formData,
            })
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, 'detail')
      },
      // handleSelection(val) {
      //   if (val.length > 1) {
      //     let del = val.shift()
      //     this.$refs.multipleTable.toggleRowSelection(del, false)
      //   }
      //   this.select = val
      // },
      push1() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage1'].showEdit()
      },
      push2() {
        if (this.select.length === 0) {
          this.$message({
            type: 'error',
            message: '请先选择项目',
          })
          return
        }
        this.$refs['manage2'].showEdit()
      },
      selectChange(data) {
        let ids = data.map((res) => res.orgid)
        let names = data.map((res) => res.orgTreeNames)
        this.$set(this.formData, 'tranorgidstrs', ids.join())
        this.$set(this.formData, 'tranorgnamestrs', names.join())
      },
      async getChildlistPro1(val) {
        this.$set(this.formData, this.proType, val[0].realname)
        if (this.proType == 'transefrstaffname') {
          this.$set(this.formData, 'transferstaffid', val[0].staffid)
          this.$refs['edit'].showEdit(val[0].staffid)
        } else if (this.proType == 'dockstaffname') {
          this.$set(this.formData, 'dockstaffid', val[0].staffid)
        }   
      },
      async getChildlistPro2(val) {
        const ids1 = val.map((res) => res.staffid).toString()
        const names1 = val.map((res) => res.realname).toString()
        const arr1 = this.select.map((res) => res.id).toString()
        const res = await cwfpddksry({ ids: arr1, names: names1, ryids: ids1 })
        if (res.msg === '成功') {
          this.$message({
            type: 'success',
            message: '下发成功!',
          })
          this.xiafa(val)
          // this.tableData.forEach((v) => {
          //   v.fpksrynames = names1
          // })
          const {
            data: { data },
          } = await fundAuditOutProjectDetail({ tbid: this.editId })
          this.tableData = data.list.map((v) => {
            v.fpksrynames = v.fpksrynames || ''
            return v
          })
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
