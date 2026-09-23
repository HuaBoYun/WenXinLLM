<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="700px"
      @close="close"
    >
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        :disabled="disabled"
        label-width="100px"
        :model="form"
        :rules="rules"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="form.username" />
          <span v-if="!disabled" class="color-red">
            合法的账号名应该由a-z的英文字母、0-9的数字组成。长度为5-16个字符之间(一个英文字母或数字算一个字符，请勿使用空白键，请选择不会引起歧义的账号名).例如:abc007
          </span>
        </el-form-item>
        <!-- <el-form-item v-if="!disabled" label="密码" prop="password">
          <el-input
            v-model.trim="form.password"
            class="input-psword"
            type="password"
          />
          <span class="color-red">
            为了保证您的密码安全,请设定密码长度为6个字符以上,由a-z的英文字母(注意区分大小写)、0-9的数字组成.同时请保管好您的密码
          </span>
        </el-form-item> -->
        <!-- <el-form-item v-if="!disabled" label="密码确认" prop="password1">
          <el-input
            v-model.trim="form.password1"
            class="input-psword"
            type="text"
          />
          <span class="color-red">请再次输入您的密码，以便确认没输错密码</span>
        </el-form-item> -->
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model.trim="form.realname" />
        </el-form-item>
        <el-form-item
          label="密级"
          prop="secrectLevelId"
          :rules="[
            { required: true, trigger: 'change', message: '请选择密级' },
          ]"
        >
          <el-select
            v-model="form.secrectLevelId"
            clearable
            placeholder="密级"
            style="width: 100%"
          >
            <el-option
              v-for="item in MJoption"
              :key="item.levelId"
              :label="item.levelName"
              :value="item.levelId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model.trim="form.idCard" />
        </el-form-item>
        <el-form-item label="用户地址信息" prop="address">
          <el-input v-model.trim="form.address" />
        </el-form-item>
        <el-form-item label="主责部门" prop="orgname">
          <!-- <el-input
            v-model.trim="form.orgname"
            readonly
            :style="{ width: 'calc(100% - 66px)' }"
          /> -->
          <el-select
            v-model="form.orgname"
            style="width: 80%"
            multiple
            disabled
          >
            <el-option
              v-for="item in userList"
              :key="item.deptId"
              :label="item.orgname"
              :value="item.deptId"
            ></el-option>
          </el-select>
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleDep1"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="兼职部门" prop="orgname2">
          <el-select
            v-model="form.orgname2"
            style="width: 80%"
            multiple
            disabled
          >
            <el-option
              v-for="item in userList2"
              :key="item.deptId"
              :label="item.orgname"
              :value="item.deptId"
            ></el-option>
          </el-select>
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleDep2"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="角色" prop="roleIdStrs">
          <el-select
            v-model="form.roleIdStrs"
            style="width: 80%"
            multiple
            disabled
          >
            <el-option
              v-for="item in roleList"
              :key="item.rid"
              :label="item.roleName"
              :value="item.rid"
            ></el-option>
          </el-select>
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            :disabled="form.orgname.length == 0"
            @click="handleRole"
          >
            选择
          </el-button>
        </el-form-item>
        <!-- <el-form-item label="审批角色">
          <el-select
            v-model="form.roleid"
            filterable
            placeholder="请选择角色"
            style="width: 100%"
          >
            <el-option
              v-for="item in roles"
              :key="item.rid"
              :label="item.rname"
              :value="item.rid"
            />
          </el-select>
        </el-form-item> -->
        <el-form-item label="职位">
          <el-input
            v-model.trim="form.jobName"
            disabled
            :style="{ width: '79%' }"
            placeholder="请选择职位"
          />
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            :disabled="form.orgname.length == 0"
            @click="handleJobidSelect()"
          >
            选择
          </el-button>
          <!-- <el-select
            v-model="form.jobid"
            filterable
            placeholder="请选择职位"
            style="width: 100%"
          >
            <el-option
              v-for="item in jobs"
              :key="item.jobid"
              :label="item.jobName"
              :value="item.jobid"
            />
          </el-select> -->
        </el-form-item>
        <el-form-item label="分管领导">
          <el-input
            v-model.trim="form.leaderName"
            readonly
            :style="{ width: '79%' }"
            placeholder="请选择分管领导"
          />
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleUserSelect(['leaderName', 'chargeLeaderStaffId'])"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model.trim="form.email" />
        </el-form-item>

        <!-- <el-form-item label="负责部门">
          <el-input
            v-model.trim="manageorgnames"
            readonly
            :style="{ width: 'calc(100% - 66px)' }"
          />
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleDepMulti('manage')"
          >
            选择
          </el-button>
        </el-form-item>
        <el-form-item label="分管部门">
          <el-input
            v-model.trim="fgorgnames"
            readonly
            :style="{ width: 'calc(100% - 66px)' }"
          />
          <el-button
            v-if="!disabled"
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleDepMulti('fg')"
          >
            选择
          </el-button>
        </el-form-item> -->
        <el-form-item label="电话号码">
          <el-input v-model.trim="form.fixedphone" />
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker
            v-model="form.entrytime"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="是否禁用">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="(item, index) in options"
              :key="index"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="移动电话">
          <el-input v-model.trim="form.miblephone" />
        </el-form-item>
        <el-form-item label="简短描述">
          <el-input v-model.trim="form.memo" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button v-if="!disabled" type="primary" @click="save">
          确 定
        </el-button>
      </template>
    </el-dialog>

    <org-user-dialog ref="orgUserDialog" @select="handSelectOrgUser" />
    <depart-ment-dialog ref="depart1" @select="handleSelectDep1" />
    <depart-ment-dialog
      ref="depart2"
      @select="handleSelectDep2"
      :isMultiple="true"
    />
    <depart-ment-multi-select-dialog
      ref="departmulti"
      @select="handleMultiSelectDep"
    />

    <JobidList ref="jobid" @selected="handleSelectJobid" />
    <RolesList ref="roles" @selected="handleSelectRoles" />
    <ProcessList ref="process" />
  </div>
</template>

<script>
  import { qxInfo, userSave, getFlowList } from '@/api/setting/auth'
  import DepartMentDialog from '@/views/setting/auth/components/DepartMentDialogTree2'
  import DepartMentMultiSelectDialog from '@/views/setting/auth/components/DepartMentMultiSelectDialog.vue'
  import OrgUserDialog from '@/views/setting/specialist/components/OrgUserDialog.vue'
  import JobidList from '@/views/setting/auth/components/JobidList'
  import RolesList from '@/views/setting/auth/components/RolesList'
  import { getMJdata } from '@/api/setting/mjsz.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'UserEdit',
    components: {
      DepartMentDialog,
      DepartMentMultiSelectDialog,
      OrgUserDialog,
      JobidList,
      RolesList,
      ProcessList,
    },
    data() {
      return {
        disabled: false,
        userList: [],
        userList2: [],
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
          jobName: '',
          roleIdStrs: '',
          roleid: '',
          fatherorgid: 1,
          status: 1,
          orgid: undefined,
          orgname: '',
          orgname2: '',
          memo: '',
          address: '',
          manageorgnames: '',
          fgorgnames: '',
          leaderName: '',
          chargeLeaderStaffId: '',
          idCard: '',
          entrytime: '',
          secrectLevelId: '',
        },
        manageorgs: '',
        manageorgnames: '',
        fgorgs: '',
        fgorgnames: '',
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
          roleIdStrs: [
            { required: true, trigger: 'blur', message: '请选择角色' },
          ],
          // idCard: [
          //   { required: true, trigger: 'blur', message: '请输入身份证号' },
          // ],
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
        orgIds: [],
        roleList: [],
        MJoption: [],
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      getMJdata({ levelType: 3 }).then((res) => {
        if (res.code == 1) {
          this.MJoption = res.data
        }
      })
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      handleRole() {
        let list = this.userList.concat(this.userList2) || []
        let roleList = []
        this.roleList.map((item) => {
          const { rid, ...other } = item
          roleList.push({ ...other, rname: item.roleName, rid: parseInt(rid) })
        })
        this.$refs.roles.show(
          list
            .map((item) => {
              return item.companyId
            })
            .join(',')
        )
      },
      handleJobidSelect() {
        let list = this.userList.concat(this.userList2) || []
        this.$refs.jobid.show(
          list
            .map((item) => {
              return item.companyId
            })
            .join(',')
        )
      },
      handleDep1() {
        this.$refs.depart1.show()
      },
      handleDep2() {
        let list = []
        this.userList2.map((item) => {
          list.push({ ...item, orgid: item.deptId })
        })
        this.$refs.depart2.show()
      },
      handleSelectRoles(val) {
        console.log('val', val)
        let roleArr = []
        let roleIds = []
        val.map((item) => {
          roleArr.push({ ...item, roleName: item.rname })
          roleIds.push(item.rid)
        })

        this.form.roleIdStrs = roleIds
        this.roleList = roleArr
        this.$forceUpdate()
      },
      handleSelectJobid(val) {
        console.log('val', val)
        this.form.jobName = val.jobname
        this.form.jobid = val.jobid
        this.$forceUpdate()
      },
      handleDepMulti(orgType) {
        if (orgType === 'manage') {
          this.manageorgs = ''
          this.manageorgnames = ''
        } else if (orgType === 'fg') {
          this.fgorgs = ''
          this.fgorgnames = ''
        }
        this.$refs.departmulti.show(orgType)
      },
      handleSelectDep1(data) {
        console.log('data', data)
        let list = []
        let orgids = []
        list.push({ ...data, deptId: data.orgid })
        orgids.push(data.orgid)
        this.userList = list
        this.form.orgname = orgids
        this.$forceUpdate()
      },
      handleSelectDep2(data) {
        let orgids = []
        let companyIds = []
        let list = []
        data.map((item) => {
          orgids.push(item.orgid)
          companyIds.push(item.companyId)
          list.push({ ...item, deptId: item.orgid })
        })
        console.log('d', list)
        this.orgIds = companyIds
        // this.menuRole()
        // this.menuPosition()
        this.userList2 = list
        this.form.orgname2 = orgids
      },
      handleMultiSelectDep(data) {
        for (let i = 0; i < data.selectedItem.length; i++) {
          if (i < data.selectedItem.length - 1) {
            if (data.orgType === 'manage') {
              this.manageorgs = this.manageorgs + data.selectedItem[i].id + ','
              this.manageorgnames =
                this.manageorgnames + data.selectedItem[i].label + ','
            }
            if (data.orgType === 'fg') {
              this.fgorgs = this.fgorgs + data.selectedItem[i].id + ','
              this.fgorgnames =
                this.fgorgnames + data.selectedItem[i].label + ','
            }
          } else {
            if (data.orgType === 'manage') {
              this.manageorgs = this.manageorgs + data.selectedItem[i].id
              this.manageorgnames =
                this.manageorgnames + data.selectedItem[i].label
            }
            if (data.orgType === 'fg') {
              this.fgorgs = this.fgorgs + data.selectedItem[i].id
              this.fgorgnames = this.fgorgnames + data.selectedItem[i].label
            }
          }
        }
      },

      async showEdit(row, disabled) {
        this.disabled = disabled

        if (!row || !row.staffid) {
          this.title = '添加'
          // Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
        } else {
          this.disabled === true ? (this.title = '查看') : (this.title = '编辑')
          const data = await qxInfo({ staffid: row.staffid })

          Object.keys(this.form).forEach(
            (key) => (this.form[key] = data.org[key])
          )
          let userList = []
          let companyIds = []
          data.org.relaList.map((item) => {
            userList.push({
              companyId: item.orgId,
              deptId: item.deptId,
              orgname: item.longName,
            })

            companyIds.push(item.orgId)
          })
          this.orgIds = companyIds

          let setOrgId = data.org.orgid
          let userId1 = []
          let userId2 = []
          let list1 = []
          let list2 = []
          userList.map((item) => {
            if (item.deptId == setOrgId) {
              userId1.push(item.deptId)
              list1.push(item)
            } else {
              userId2.push(item.deptId)
              list2.push(item)
            }
          })
          this.userList = list1
          this.userList2 = list2
          this.form.orgname = userId1
          this.form.orgname2 = userId2

          if (data.org.roleIdStrs) {
            let roleArr = []
            let roleIds = data.org.roleIdStrs.split(',')
            let roleNames = data.org.roleNames.split(',')
            roleIds.map((item, index) => {
              roleArr.push({
                rid: item,
                roleName: roleNames[index],
              })
            })

            this.form.roleIdStrs = roleIds
            this.roleList = roleArr

            this.$forceUpdate()
          }
          this.manageorgnames = this.form.manageorgnames
          this.fgorgnames = this.form.fgorgnames
        }
        // this.menuRole()
        // this.menuPosition()
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.manageorgs = ''
        this.manageorgnames = ''
        this.fgorgs = ''
        this.fgorgnames = ''
        this.userList = []
        this.userList2 = []
        this.roleList = []
        this.orgIds = []
        this.form.orgname = []
        this.form.orgname2 = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let list = []
            // 确保 orgname2 是数组
            const orgname2Array = Array.isArray(this.form.orgname2)
              ? this.form.orgname2
              : []
            orgname2Array.map((item) => {
              if (item != this.form.orgname[0]) {
                list.push(item)
              }
            })
            let deptIdStrs =
              list.length > 0
                ? this.form.orgname.join(',') + ',' + list.join(',')
                : this.form.orgname.join(',')

            let param = JSON.parse(JSON.stringify(this.form))
            param.roleIdStrs = param.roleIdStrs && param.roleIdStrs.join(',')
            param.roleid = param.roleid
            param.manageorgs = this.manageorgs
            param.manageorgnames = this.manageorgnames
            param.fgorgs = this.fgorgs
            param.fgorgnames = this.fgorgnames
            param.orgname = undefined
            param.deptIdStrs = deptIdStrs
            const { msg, data } = await userSave(param)
            // 流程校验
            if (this.requireValuedata && this.title != '添加') {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'user',
                operationType: this.title == '添加' ? 1 : 2,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.$emit('fetch-data')
                  this.close()
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else if (this.title == '添加' && this.requireValuedata) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            } else {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      // async menuRole() {
      //   const {
      //     data: { tlist },
      //   } = await roleList({
      //     orgIds: this.orgIds.join(','),
      //     pageNumber: 1,
      //     pageSize: 1000,
      //   })
      //   this.roles = tlist
      // },
      // async menuPosition() {
      //   const {
      //     pageInfo: { tlist },
      //   } = await jobList({
      //     orgIds: this.orgIds.join(','),
      //     pageNumber: 1,
      //     pageSize: 1000,
      //   })
      //   this.jobs = tlist
      // },
      handleUserSelect(userKeys) {
        this.userKeys = userKeys
        this.$refs.orgUserDialog.show()
      },
      handSelectOrgUser(data) {
        this.form[this.userKeys[0]] = data.realname
        this.form[this.userKeys[1]] = data.staffid
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }
</style>
