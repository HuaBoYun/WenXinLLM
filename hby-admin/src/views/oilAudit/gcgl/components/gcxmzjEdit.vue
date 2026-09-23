<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="serialNumber">
            <el-input
              v-model="formData.serialNumber"
              clearable
              placeholder="请输入序号"
              :style="{ width: '100%' }"
              type="number"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="htbh">
            <el-input
              v-model="formData.htbh"
              clearable
              placeholder="请输入合同编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程名称" prop="gcmc">
            <el-input
              v-model="formData.gcmc"
              clearable
              placeholder="请选择工程名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="jsdw">
            <el-input
              v-model="formData.jsdw"
              clearable
              placeholder="请选择建设单位"
              :style="{ width: '247px' }"
              readonly
            />
            <el-button
              :style="{ marginLeft: '10px', padding: '7px 15px' }"
              type="primary"
              @click="openDep('jsdw')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二审审查金额（元）" prop="esscje">
            <el-input
              v-model="formData.esscje"
              clearable
              placeholder="请输入二审审查金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="sgdw">
            <el-input
              v-model="formData.sgdw"
              clearable
              placeholder="施工单位"
              :style="{ width: '100%' }"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px', padding: '7px 15px' }"
              type="primary"
              @click="openDep('sgdw')"
              size="small"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="lxr">
            <el-input
              v-model="formData.lxr"
              clearable
              placeholder="请输入联系人"
              :style="{ width: '247px' }"
            />
            <el-button
              @click="projectManager"
              :style="{ marginLeft: '10px', padding: '7px 15px' }"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="lxdh">
            <el-input
              v-model="formData.lxdh"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="xmzttype">
            {{ formData.xmzttype }}
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="项目状态" prop="xmstatus">
            {{ formData.xmstatus == 1 ? '已做审计项目' : '未做审计项目' }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- 部门 -->
    <!-- <DepartmentOptions ref="department" @selected="handleDepartmentSelected" /> -->
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <SelectDepartment ref="department" @submit="handleDepartmentSelected" />
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { implementPlanList } from '@/oapi/audit/project'
  import { saveOrUpdate } from '@/api/oilAudit/jhgl/gcxmzj'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/selectPerson.vue'

  export default {
    name: 'gcxmzjEdit',
    inheritAttrs: false,
    components: {
      SelectDepartment,
      projectManage,
    },
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        gcOpts: [], // 工程名称列表
        loading: false,
        formData: {
          htbh: '',
          gcmc: '',
          gcid: '',
          lxr: '',
          lxrid: '',
          lxdh: '',
          jsdw: '',
          jsdwid: '',
          esscje: '',
          sgdw: '',
          sgdwid: '',
          xmzttype: '',
          xmstatus: '',
          cjr: '',
          cjsj: '',
          serialNumber: '',
        },
        gcxmzjid: '',
        formDisabled: true,
        rules: {
          esscje: [
            {
              validator,
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: 'jsdw',
      }
    },
    async created() {
      const {
        data: { tlist },
      } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
      this.gcOpts = tlist
    },
    methods: {
      changeGcmc(val) {
        this.formData.gcid = this.gcOpts.find(
          (item) => (item.projectName = val)
        ).id
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      openDep(type) {
        this.depType = type
        this.$refs.department.showEdit()
      },
      handleDepartmentSelected(node) {
        //保存名称和对应的ID
        this.$set(this.formData, `${this.depType}`, node.label)
        this.$set(this.formData, `${this.depType}id`, node.id)
      },
      showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (row) {
          this.title = disabled ? '详细' : '编辑'
          this.gcxmzjid = row.gcxmzjid
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.cjr = createUserName
          this.formData.cjsj = createTime
        }
      },
      close() {
        this.formData = {
          htbh: '',
          gcmc: '',
          gcid: '',
          lxr: '',
          lxrid: '',
          lxdh: '',
          jsdw: '',
          jsdwid: '',
          esscje: '',
          sgdw: '',
          sgdwid: '',
          xmzttype: '',
          xmstatus: '',
          cjr: '',
          cjsj: '',
          serialNumber: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = true
        this.gcxmzjid = ''
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            if (this.title === '编辑') params.gcxmzjid = this.gcxmzjid
            const res = await saveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetch-data')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      async getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        this.$set(this.formData, 'lxrid', ids.toString())
        this.$set(this.formData, 'lxr', names.toString())
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
