<template>
  <div>
    <span class="change-entry" @click="dialogVisible = true">
      <vab-icon icon="exchange-line" />
      组织穿透（{{ orgName || '未选择' }}）
    </span>
    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      title="组织穿透"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <div class="tree-wrapper">
        <company-tree
          always-root
          :current-node-id="currentOrgId"
          @select="handleNodeClick"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button v-loading="loading" type="primary" @click="save">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { changeOrgInfo } from '@/oapi/user'
  import { mapMutations } from 'vuex'
  import { getAuthListForUser } from '@/api/setting/auths'
  import CompanyTree from '@/components/change/company/components/tree.vue'

  export default {
    name: 'ChangeCompany',
    components: { CompanyTree },
    data() {
      return {
        dialogVisible: false,
        loading: false,
        currentOrgId: '',
        orgid: '',
        orgName: '',
      }
    },
    created() {
      this.getUserInfo()
    },
    methods: {
      ...mapMutations({
        resetRoutesState: 'routes/resetRoutesState',
      }),
      getUserInfo() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        this.currentOrgId =
          (userInfo.currentOrg && userInfo.currentOrg.orgid) || ''
        this.orgid = this.currentOrgId
        this.orgName =
          (userInfo.currentOrg && userInfo.currentOrg.orgname) || ''
      },
      handleNodeClick(data) {
        this.orgid = data.id
      },
      async save() {
        if (!this.orgid) {
          this.$message.warning('请先选择组织')
          return
        }
        this.loading = true
        try {
          const res = await changeOrgInfo({ orgid: this.orgid })
          if (res.msg === '切换成功') {
            this.$message.success('切换成功，正在刷新权限...')
            this.dialogVisible = false
            await getAuthListForUser({ switchType: 'company' }).catch(() => {})
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
