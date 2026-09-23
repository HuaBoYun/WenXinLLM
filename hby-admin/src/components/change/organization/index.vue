<template>
  <div>
    <span class="change-entry" @click="dialogVisible = true">
      <vab-icon icon="exchange-line" />
      我的组织（{{ orgName || '未选择' }}）
    </span>
    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      title="我的组织"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div v-loading="loading" class="tree-wrapper">
        <el-row>
          <el-col :span="16">
            <p v-for="item in userList" :key="`${item.orgId}-${item.deptId}`">
              <el-tag
                style="cursor: pointer"
                :type="isCurrentOrgItem(item) ? '' : 'info'"
                @click="setOrgin(item)"
              >
                {{ item.longName }}
              </el-tag>
            </p>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getLoginUserOrgRelationList,
    setLoginUserOrgInfo,
  } from '@/api/setting/auth'
  import { mapMutations } from 'vuex'
  import { getAuthListForUser } from '@/api/setting/auths'

  export default {
    name: 'ChangeOrganization',
    data() {
      return {
        dialogVisible: false,
        loading: false,
        orgName: '',
        userInfo: {
          currentOrg: {},
        },
        userList: [],
      }
    },
    created() {
      this.getUserInfo()
      this.getMyOrg()
    },
    mounted() {
      const that = this
      window.addEventListener('setItemEvent', function (e) {
        const newdata = JSON.parse(e.newValue)
        that.orgName = newdata.currentOrg.orgname
        that.$forceUpdate()
      })
    },
    methods: {
      ...mapMutations({
        resetRoutesState: 'routes/resetRoutesState',
      }),
      getUserInfo() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        this.userInfo = {
          currentOrg: userInfo.currentOrg || {},
          linkDetp: userInfo.linkDetp || {},
        }
        this.orgName =
          (userInfo.linkDetp && userInfo.linkDetp.orgname) ||
          (userInfo.currentOrg && userInfo.currentOrg.orgname) ||
          ''
      },
      isCurrentOrgItem(item) {
        const currentDeptId =
          this.userInfo && this.userInfo.linkDetp
            ? this.userInfo.linkDetp.orgid
            : ''
        const currentOrgId =
          this.userInfo && this.userInfo.currentOrg
            ? this.userInfo.currentOrg.orgid
            : ''

        if (currentDeptId !== '' && currentDeptId !== null) {
          return String(currentDeptId) === String(item.deptId)
        }
        return String(currentOrgId) === String(item.orgId)
      },
      async getMyOrg() {
        const res = await getLoginUserOrgRelationList()
        this.userList = (res.data && res.data.relaList) || []
        this.userInfo = (res.data && res.data.userInfo) || {
          currentOrg: {},
          linkDetp: {},
        }
        this.orgName =
          (this.userInfo.linkDetp && this.userInfo.linkDetp.orgname) ||
          (this.userInfo.currentOrg && this.userInfo.currentOrg.orgname) ||
          this.orgName
        this.$forceUpdate()
      },
      async setOrgin(item) {
        if (this.isCurrentOrgItem(item)) {
          return
        }
        this.loading = true
        try {
          const res = await setLoginUserOrgInfo({
            orgId: item.orgId,
            deptId: item.deptId,
          })
          if (res.code == 1) {
            this.$message.success('切换成功，正在刷新权限...')
            await getAuthListForUser({ switchType: 'org' }).catch(() => {})
            localStorage.removeItem('allMenu')
            localStorage.removeItem('renderMenu')
            localStorage.removeItem('modelname')
            localStorage.setItem('model', 'home')
            this.resetRoutesState()
            setTimeout(() => {
              location.href = '/'
            }, 600)
          }
        } finally {
          this.loading = false
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .change-entry {
    display: inline-block;
    max-width: 260px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
  }
  .tree-wrapper {
    height: auto;
    max-height: 500px;
    overflow-y: auto;
  }
</style>
