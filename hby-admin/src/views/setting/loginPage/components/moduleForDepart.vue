<template>
  <div>
    <el-dialog
      :append-to-body="true"
      title="授权公司"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
      v-if="dialogVisible"
    >
      <div class="tree-wrapper">
        <company-tree
          always-root
          @select="handleNodeClick"
          :projectId="projectId"
          :defaultTreeData="defaultTreeData"
          v-if="dialogVisible"
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
  import CompanyTree from './departTree.vue'
  import {
    saveHomeModuleInfo,
    getDefaultHomeRelateCompany,
  } from '@/api/setting/system'
  export default {
    name: '',
    components: { CompanyTree },
    props: {},
    data() {
      return {
        dialogVisible: false,
        data: null,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        loading: false,
        orgName: '',
        orgid: '',
        projectId: '',
        submitInfo: [],
        defaultTreeData: [],
        orgList: [],
      }
    },
    computed: {
      currentOrg() {
        const orgStr = window.localStorage.getItem('current-org')
        if (orgStr) {
          try {
            const org = JSON.parse(orgStr)
            if (org.id && org.label) {
              return org
            }
          } catch (e) {}
        }
        return {
          id: 1,
          label: '切换公司',
        }
      },
    },
    created() {
      this.getUserInfo()
    },
    mounted() {},
    methods: {
      showEdit(id) {
        this.dialogVisible = true
        this.projectId = id
        getDefaultHomeRelateCompany({ homePageId: id }).then((res) => {
          if (res.code == 200) {
            this.defaultTreeData = res.data
          }
        })
      },
      getUserInfo() {
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.orgName = userInfo.currentOrg.orgname
      },
      handleNodeClick(data) {
        this.orgList = data
        this.submitInfo = data.map((res) => {
          return {
            authOrgId: res,
            projectId: this.projectId,
          }
        })
      },
      save() {
        saveHomeModuleInfo({
          homePageId: this.projectId,
          list: this.orgList,
        }).then((res) => {
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '保存成功',
            })
          }
        })
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  .tree-wrapper {
    height: auto;
    max-height: 500px;
    overflow-y: auto;
  }
</style>
