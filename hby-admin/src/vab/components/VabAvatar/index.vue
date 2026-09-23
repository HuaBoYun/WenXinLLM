<template>
  <el-dropdown @command="handleCommand" @visible-change="handleVisibleChange">
    <span class="avatar-dropdown">
      <el-avatar class="user-avatar" :src="avatar" />
      <div class="hidden-xs-only">
        <div class="user-name">
          <span class="hidden-xs-only">{{ realName }}</span>
          <vab-icon
            class="vab-dropdown"
            :class="{ 'vab-dropdown-active': active }"
            icon="arrow-down-s-line"
          />
        </div>
      </div>
      <!-- <span class="version-info">
        <span>当前登录人数：{{ refreshInfo.activeUserNum }}</span>
        <span>当前版本：{{ refreshInfo.systemVersion }}</span>
      </span> -->
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <!-- <el-dropdown-item disabled>
          <span>当前登录人数：{{ refreshInfo.activeUsersNum }}</span>
        </el-dropdown-item>
        <el-dropdown-item disabled>
          <span>当前版本：{{ refreshInfo.systemVersion }}</span>
        </el-dropdown-item> -->
        <el-dropdown-item>
          <change-company />
        </el-dropdown-item>
        <el-dropdown-item command="organization">
          <Organization></Organization>
        </el-dropdown-item>

        <!-- <el-dropdown-item command="setPage">
          <vab-icon icon="settings-5-line" />
          {{ translateTitle('设置') }}
        </el-dropdown-item> -->
        <el-dropdown-item command="personalCenter">
          <information></information>
        </el-dropdown-item>
        <el-dropdown-item command="modifyPassword">
          <modify></modify>
        </el-dropdown-item>
        <el-dropdown-item command="helpPage">
          <help></help>
        </el-dropdown-item>
        <el-dropdown-item command="logout">
          <vab-icon icon="logout-circle-r-line" />
          {{ translateTitle('退出登录') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script>
  import { translateTitle } from '@/utils/i18n'
  import { mapActions, mapGetters } from 'vuex'
  import { toLoginRoute } from '@/utils/routes'
  import ChangeCompany from '@/components/change/company'
  import Information from '@/components/change/information'
  import Help from '@/components/change/help'
  import Modify from '@/components/change/modify'
  import Organization from '@/components/change/organization'
  import AiAvator from '@/views/index/components/AI/img/ai-avator.png'
  import { updateOnlineUser } from '@/api/setting/auth'

  export default {
    name: 'VabAvatar',
    components: { ChangeCompany, Information, Modify, Help, Organization },
    data() {
      return {
        active: false,
        realName: '',
        avatar: AiAvator,
        refreshTimer: null,
      }
    },
    computed: {
      ...mapGetters({
        // avatar: 'user/avatar',
        username: 'user/username',
      }),
    },
    created() {
      this.syncUserInfo()
    },

    methods: {
      translateTitle,
      ...mapActions({
        _logout: 'user/logout',
      }),
      syncUserInfo() {
        const userInfoStr = localStorage.getItem('userInfo')
        if (!userInfoStr) {
          this.realName = ''
          return
        }
        try {
          const userInfo = JSON.parse(userInfoStr)
          this.realName = userInfo.realname || ''
        } catch {
          this.realName = ''
        }
      },

      handleCommand(command) {
        switch (command) {
          case 'logout':
            this.logout()
            break
          case 'personalCenter':
            this.personalCenter()
            break
          case 'modifyPassword':
            this.modifyPassword()
            break
          case 'setPage':
            this.toSetting()
            break
          case 'helpPage':
            this.personalCenter()
            break
        }
      },
      handleVisibleChange(val) {
        this.active = val
        if (val) {
          this.syncUserInfo()
        }
      },
      personalCenter() {},
      modifyPassword() {},
      toSetting() {
        localStorage.setItem('model', 'setting')
        location.href = '/'
      },
      async logout() {
        // 退出登录，更新在线人数
        updateOnlineUser()
        await this._logout()
        await this.$router.push(toLoginRoute(this.$route.path))
        localStorage.setItem('model', 'home')
      },
    },
  }
</script>

<style lang="scss" scoped>
  .avatar-dropdown {
    display: flex;
    align-content: center;
    align-items: center;
    justify-content: center;
    justify-items: center;

    .user-avatar {
      width: 40px;
      height: 40px;
      margin-left: 15px;
      cursor: pointer;
      border-radius: 50%;
    }

    .user-name {
      position: relative;
      display: flex;
      align-content: center;
      align-items: center;
      height: 20px;
      margin-left: 6px;
      line-height: 20px;
      cursor: pointer;
      white-space: nowrap;
      [class*='ri-'] {
        margin-left: 0 !important;
      }
    }
  }
  .index-menu .user-name {
    color: white !important;
  }
  .el-avatar {
    background-color: transparent !important;
  }
</style>
