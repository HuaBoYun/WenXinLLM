<template>
  <div class="compare-container">
    <!-- 左侧：oldData -->
    <div class="compare-panel" v-if="type != '新增'">
      <div class="panel-header">
        <h3>原始数据</h3>
      </div>
      <el-form
        ref="oldForm"
        :class="{ disabled: true }"
        :disabled="true"
        label-width="100px"
        :model="oldForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="oldForm.username" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model.trim="oldForm.realname" />
        </el-form-item>
        <el-form-item label="密级" prop="secrectLevelId">
          <el-select
            v-model="oldForm.secrectLevelId"
            clearable
            placeholder="密级"
            style="width: 100%"
            disabled
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
          <el-input v-model.trim="oldForm.idCard" />
        </el-form-item>
        <el-form-item label="用户地址信息" prop="address">
          <el-input v-model.trim="oldForm.address" />
        </el-form-item>
        <el-form-item label="主责部门" prop="orgname">
          <el-tooltip
            :content="getDeptNames(oldUserList)"
            placement="top-start"
            :disabled="!getDeptNames(oldUserList)"
          >
            <div class="dept-select-tooltip-trigger">
              <el-select
                v-model="oldForm.orgname"
                style="width: 100%"
                multiple
                disabled
              >
                <el-option
                  v-for="item in oldUserList"
                  :key="item.deptId"
                  :label="item.orgname"
                  :value="item.deptId"
                ></el-option>
              </el-select>
            </div>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="兼职部门" prop="orgname2">
          <el-tooltip
            :content="getDeptNames(oldUserList2)"
            placement="top-start"
            :disabled="!getDeptNames(oldUserList2)"
          >
            <div class="dept-select-tooltip-trigger">
              <el-select
                v-model="oldForm.orgname2"
                style="width: 100%"
                multiple
                disabled
              >
                <el-option
                  v-for="item in oldUserList2"
                  :key="item.deptId"
                  :label="item.orgname"
                  :value="item.deptId"
                ></el-option>
              </el-select>
            </div>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="角色" prop="roleIdStrs">
          <el-select
            v-model="oldForm.roleIdStrs"
            style="width: 80%"
            multiple
            disabled
          >
            <el-option
              v-for="item in oldRoleList"
              :key="item.rid"
              :label="item.roleName"
              :value="item.rid"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职位">
          <el-input
            v-model.trim="oldForm.jobName"
            disabled
            :style="{ width: '79%' }"
            placeholder="请选择职位"
          />
        </el-form-item>
        <el-form-item label="分管领导">
          <el-input
            v-model.trim="oldForm.leaderName"
            readonly
            :style="{ width: '79%' }"
            placeholder="请选择分管领导"
          />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model.trim="oldForm.email" />
        </el-form-item>
        <el-form-item label="电话号码">
          <el-input v-model.trim="oldForm.fixedphone" />
        </el-form-item>
        <el-form-item label="入职时间">
          <el-date-picker
            v-model="oldForm.entrytime"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            disabled
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="是否禁用">
          <el-radio-group v-model="oldForm.status" disabled>
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
          <el-input v-model.trim="oldForm.miblephone" />
        </el-form-item>
        <el-form-item label="简短描述">
          <el-input v-model.trim="oldForm.memo" type="textarea" />
        </el-form-item>
      </el-form>
    </div>

    <!-- 右侧：newData -->
    <div class="compare-panel">
      <div class="panel-header">
        <h3>新数据</h3>
      </div>
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
          <el-tooltip
            :content="getDeptNames(userList)"
            placement="top-start"
            :disabled="!getDeptNames(userList)"
          >
            <div class="dept-select-tooltip-trigger">
              <el-select
                v-model="form.orgname"
                style="width: 100%"
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
            </div>
          </el-tooltip>
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
          <el-tooltip
            :content="getDeptNames(userList2)"
            placement="top-start"
            :disabled="!getDeptNames(userList2)"
          >
            <div class="dept-select-tooltip-trigger">
              <el-select
                v-model="form.orgname2"
                style="width: 100%"
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
            </div>
          </el-tooltip>
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
    </div>

    <div class="footer" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>

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
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'UserEdit',
    components: {
      DepartMentDialog,
      DepartMentMultiSelectDialog,
      OrgUserDialog,
      JobidList,
      RolesList,
      Resubmit,
    },
    data() {
      return {
        disabled: false,
        userList: [],
        userList2: [],
        oldUserList: [],
        oldUserList2: [],
        oldRoleList: [],
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
        oldForm: {
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
        type: '',
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },
    created() {
      getMJdata({ levelType: 3 }).then((res) => {
        if (res.code == 1) {
          this.MJoption = res.data
        }
      })
    },
    methods: {
      getDeptNames(list) {
        return (list || [])
          .map((item) => item.orgname)
          .filter((item) => item)
          .join('，')
      },
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

      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type
      ) {
        this.title = title
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
        this.disabled = title === 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        // 处理oldData - 左侧原始数据
        if (oldData) {
          Object.keys(this.oldForm).forEach(
            (key) => (this.oldForm[key] = oldData[key])
          )

          let oldUserList = []
          let oldCompanyIds = []
          oldData.relaList &&
            oldData.relaList.map((item) => {
              oldUserList.push({
                companyId: item.orgId,
                deptId: item.deptId,
                orgname: item.longName,
              })
              oldCompanyIds.push(item.orgId)
            })

          let oldSetOrgId = oldData.orgid
          let oldUserId1 = []
          let oldUserId2 = []
          let oldList1 = []
          let oldList2 = []
          oldUserList.map((item) => {
            if (item.deptId == oldSetOrgId) {
              oldUserId1.push(item.deptId)
              oldList1.push(item)
            } else {
              oldUserId2.push(item.deptId)
              oldList2.push(item)
            }
          })
          this.oldUserList = oldList1
          this.oldUserList2 = oldList2
          this.oldForm.orgname = oldUserId1
          this.oldForm.orgname2 = oldUserId2

          if (oldData.roleIdStrs) {
            let oldRoleArr = []
            let oldRoleIds = oldData.roleIdStrs.split(',')
            let oldRoleNames = oldData.roleNames.split(',')
            oldRoleIds.map((item, index) => {
              oldRoleArr.push({
                rid: item,
                roleName: oldRoleNames[index],
              })
            })
            this.oldForm.roleIdStrs = oldRoleIds
            this.oldRoleList = oldRoleArr
          }
        }

        // 处理newData - 右侧新数据
        Object.keys(this.form).forEach((key) => (this.form[key] = newData[key]))
        let userList = []
        let companyIds = []
        newData.relaList.map((item) => {
          userList.push({
            companyId: item.orgId,
            deptId: item.deptId,
            orgname: item.longName,
          })

          companyIds.push(item.orgId)
        })
        this.orgIds = companyIds

        let setOrgId = newData.orgid
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

        if (newData.roleIdStrs) {
          let roleArr = []
          let roleIds = newData.roleIdStrs.split(',')
          let roleNames = newData.roleNames ? newData.roleNames.split(',') : []
          roleIds.map((item, index) => {
            roleArr.push({
              rid: item,
              roleName: roleNames[index] || '',
            })
          })

          this.form.roleIdStrs = roleIds
          this.roleList = roleArr

          this.$forceUpdate()

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
        this.oldForm = this.$options.data().oldForm
        this.manageorgs = ''
        this.manageorgnames = ''
        this.fgorgs = ''
        this.fgorgnames = ''
        this.userList = []
        this.oldUserList = []
        this.roleList = []
        this.oldRoleList = []
        this.orgIds = []
        this.form.orgname = []
        this.oldForm.orgname = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let list = []
            this.form.orgname2.map((item) => {
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
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
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
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .dept-select-tooltip-trigger {
    display: inline-block;
    width: 80%;
    vertical-align: top;
  }

  .footer {
    position: absolute;
    bottom: 0;
    right: 0;
    background: #fff;
    padding: 15px 20px;
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
