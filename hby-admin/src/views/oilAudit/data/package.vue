<template>
  <div>
    <div class="system-log-container">
      <vab-query-form>
        <el-card shadow="never">
          <vab-query-form-top-panel>
            <el-form
              :inline="true"
              ref="queryForm"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item prop="qdcode" label="档案编号">
                <el-input
                  v-model="queryForm.qdcode"
                  placeholder="档案编号"
                  style="width: 250px"
                  disabled
                />
              </el-form-item>
              <el-form-item prop="projectName" label="档案名称">
                <el-input
                  v-model="queryForm.projectName"
                  placeholder="档案名称"
                  style="width: 250px"
                  disabled
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="submitApproval"
                >
                  申请归档
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel>
        </el-card>
      </vab-query-form>
    </div>
    <el-card>
      <Look class="bottom-container" />
    </el-card>
    <ProcessList ref="process" />
  </div>
</template>

<script>
  import { projectArchiveList } from '@/oapi/audit/projectData'
  import Look from '@/views/oilAudit/data/look.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'Download',
    components: { Look, ProcessList },
    data() {
      return {
        list: [],
        queryForm: {
          projectName: '',
          qdcode: '',
        },
        projectId: '',
        zsstaffid: '',
        gdstatus: null,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      /**
       * @description 流程提交接口
       * @param {*}
       * @return {*}
       */
      async submitApproval() {
        const info = JSON.parse(localStorage.getItem('userInfo')).staffid
        if (info != this.zsstaffid) {
          this.$message.error('项目主审才能归档')
          return
        }
        if (this.gdstatus) {
          return this.$message.error('该项目已提交审批')
        }
        this.$refs['process'].save(183, this.projectId)
      },

      async fetchData() {
        this.listLoading = true
        const {
          data: { tree, pj },
        } = await projectArchiveList()
        this.listLoading = false
        this.projectId = pj.id
        this.gdstatus = pj.gdstatus ? pj.gdstatus : null
        this.zsstaffid = pj.zsstaffid
        this.queryForm.projectName = pj.projectName
        this.queryForm.qdcode = pj.qdcode
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .bottom-container {
    background: #fff !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
