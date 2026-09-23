<template>
  <div>
    <span @click="getMyOrg">
      <vab-icon icon="user-line" />
      个人信息
    </span>
    <el-dialog
      :append-to-body="true"
      title="个人信息"
      :visible.sync="dialogVisible"
      width="40%"
      :close-on-click-modal="false"
    >
      <el-row :gutter="20">
        <el-col>
          <el-form ref="form" label-width="100px" :model="form">
            <el-form-item label="用户名">
              <el-col :span="16">
                <el-input disabled v-model="form.username" />
              </el-col>
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-col :span="16">
                <el-input disabled v-model="form.realname" />
              </el-col>
            </el-form-item>
            <el-form-item label="用户信息地址">
              <el-col :span="16">
                <el-input disabled v-model="form.nickname" />
              </el-col>
            </el-form-item>
            <el-form-item label="角色">
              <el-col :span="16">
                <el-input disabled v-model="form.roleNames" />
              </el-col>
            </el-form-item>
            <el-form-item label="Email">
              <el-col :span="16">
                <el-input disabled v-model="form.email" />
              </el-col>
            </el-form-item>
            <el-form-item label="主责部门" prop="orgname">
              <el-col :span="16">
                <el-input disabled v-model="form.orgname" />
              </el-col>
              <!-- <el-col :span="16">
                <el-select
                  style="width: 100%"
                  v-model="form.orgname"
                  multiple
                  disabled
                >
                  <el-option
                    v-for="item in userList"
                    :key="item.orgid"
                    :label="item.orgname"
                    :value="item.orgid"
                  ></el-option>
                </el-select>
              </el-col> -->
            </el-form-item>
            <el-form-item label="兼职部门" prop="orgname2">
              <el-col :span="16">
                <el-input disabled v-model="form.orgname2" />
              </el-col>
              <!-- <el-col :span="16">
                <el-select
                  style="width: 100%"
                  v-model="form.orgname2"
                  multiple
                  disabled
                >
                  <el-option
                    v-for="item in userList2"
                    :key="item.orgid"
                    :label="item.orgname"
                    :value="item.orgid"
                  ></el-option>
                </el-select>
              </el-col> -->
            </el-form-item>
            <el-form-item label="电话号码">
              <el-col :span="16">
                <el-input disabled v-model="form.miblephone" />
              </el-col>
            </el-form-item>
            <el-form-item label="移动电话">
              <el-col :span="16">
                <el-input disabled v-model="form.nickname" />
              </el-col>
            </el-form-item>
            <el-form-item label="简短描述">
              <el-col :span="16">
                <el-input disabled v-model="form.nickname" type="textarea" />
              </el-col>
            </el-form-item>
            <!-- <el-form-item label="我的组织">
                <el-col :span="16">
                 <p v-for="item in userList">
                    <el-tag style="cursor: pointer;" @click="setOrgin(item)" :type="userInfo.linkDetp.orgid == item.deptId?'':'info'">{{item.longName}}</el-tag>
                </p>
                </el-col>
              </el-form-item> -->
          </el-form>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">
          取 消
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getLoginUserOrgRelationList,
    setLoginUserOrgInfo,
  } from '@/api/setting/auth'

  export default {
    data() {
      return {
        dialogVisible: false,
        userList: [],
        userList2: [],
        form: {},
      }
    },
    created() {
      // let resL = JSON.parse(localStorage.getItem('userInfo'))
      // this.form = resL
      // this.getMyOrg()
    },
    methods: {
      onBack() {
        this.$router.go(-1)
      },
      async getMyOrg() {
        this.dialogVisible = true
        const res = await getLoginUserOrgRelationList()
        // this.userList = res.data.relaList
        console.log(res)
        this.userInfo = res.data.userInfo
        window.localStorage.setItem(
          'gsStorename',
          JSON.stringify(res.data.userInfo)
        )
        this.form = res.data.userInfo

        let userList = []
        let userList2 = []
        let companyIds = []
        res.data.relaList.map((item) => {
          if(item.numno == 0) {
            userList.push(item.longName)
          } else {
            userList2.push(item.longName)
          }

          // userList.push({
          //   orgid: item.orgid,
          //   orgname: item.orgname, 
          // })

          // companyIds.push(item.orgId)
        })
        // this.orgIds = companyIds

        // let setOrgId = res.data.userInfo.currentOrg.orgid
        // let userId1 = []
        // let userId2 = []
        // let list1 = []
        // let list2 = []
        // userList.map((item) => {
        //   if (item.orgid == setOrgId) {
        //     userId1.push(item.orgid)
        //     list1.push(item)
        //   } else {
        //     userId2.push(item.orgid)
        //     list2.push(item)
        //   }
        // })

        // this.userList = list1
        // this.userList2 = list2
        // this.form.orgname = userId1
        // this.form.orgname2 = userId2

        this.form.orgname = userList.join()
        this.form.orgname2 = userList2.join()

        this.$forceUpdate()
      },
      async setOrgin(item) {
        if (this.userInfo.linkDetp.orgid != item.deptId) {
          const res = await setLoginUserOrgInfo({
            orgId: item.orgId,
            deptId: item.deptId,
          })
          if (res.code == 1) {
            this.$message.success('切换成功')
            this.getMyOrg()
          }
        }
      },
    },
  }
</script>
