<template>
  <div class="system-log-container">
    <ul>
      <li v-for="item in dataList" :key="item.org_code || item.id">
        <p class="unit_name">{{ item.company_name || item.org_name || item.name }}</p>
        <el-row :gutter="20">
          <el-col :span="8" class="font_13">
            组织机构代码：{{ item.org_code }}
          </el-col>
          <el-col :span="8" class="font_13">
            企业代码：{{ item.enterprise_code }}
          </el-col>
          <el-col :span="8" class="font_13">
            公司代码：{{ item.company_code }}
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8" class="font_13">
            集团名称：{{ item.group_name }}
          </el-col>
          <el-col :span="8" class="font_13">
            集团代码：{{ item.group_code }}
          </el-col>
          <el-col :span="8" class="font_13">
            组织名称：{{ item.org_name }}
          </el-col>
        </el-row>
        <el-row :gutter="20" class="btn_list">
          <el-col :span="3">
            <el-button type="text" @click="handleStatus(item)">
              经营状况
            </el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleRisk(item)">
              经营风险
            </el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleSfRisk(item)">
              司法风险
            </el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleFzxx(item)">
              发展信息
            </el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleyqxx(item)">
              舆情信息
            </el-button>
          </el-col>
          <el-col :span="3"><el-button type="text">风控报告</el-button></el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleEnterprise(item)">
              企业画像
            </el-button>
          </el-col>
        </el-row>
      </li>
    </ul>
    <business-risks ref="risk"></business-risks>
    <business-status ref="status"></business-status>
    <enterprise-image ref="enterprise"></enterprise-image>
    <sf-risk-dialog ref="sfRisk"></sf-risk-dialog>
    <yqxx-dialog ref="yqxx"></yqxx-dialog>
    <fzxx-dialog ref="fzxx"></fzxx-dialog>
  </div>
</template>

<script>
  import businessRisks from './businessRisks.vue'
  import businessStatus from './businessStatus.vue'
  import enterpriseImage from './enterpriseImage.vue'
  import sfRiskDialog from './sfRiskDialog.vue'
  import yqxxDialog from './yqxxDialog.vue'
  import fzxxDialog from './fzxxDialog.vue'
  import { search, findbyManage } from '@/api/workbench/telescope'

  export default {
    name: 'Consult',
    components: {
      businessRisks,
      businessStatus,
      enterpriseImage,
      sfRiskDialog,
      yqxxDialog,
      fzxxDialog,
    },
    data() {
      return {
        dataList: [],
      }
    },
    created() {},
    methods: {
      dicCompanyType(companyType) {
        //  companyType;//公司类型 1-公司，2-香港公司，3-社会组织，4-律所，5-事业单位，6-基金会
        switch (companyType) {
          case '1':
            return '公司'
          case '2':
            return '香港公司'
          case '3':
            return '社会组织'
          case '4':
            return '律所'
          case '5':
            return '事业单位'
          case '6':
            return '基金会'
        }
      },
      handleRisk(item) {
        // 使用 org_code 或 enterprise_code 作为 id, company_name 或 org_name 作为 name
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['risk'].showEdit(id, name)
      },
      handleStatus(item) {
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['status'].showEdit(id, name)
      },
      handleEnterprise(item) {
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['enterprise'].showEdit(id, name)
      },
      handleSfRisk(item) {
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['sfRisk'].showEdit(id, name)
      },
      handleyqxx(item) {
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['yqxx'].showEdit(id, name)
      },
      handleFzxx(item) {
        const id = item.org_code || item.enterprise_code || item.id
        const name = item.company_name || item.org_name || item.name
        this.$refs['fzxx'].showEdit(id, name)
      },
      async searchResult(info) {
        console.dir(info)
        let dataList = await search({
          word: info,
        })
        this.dataList = dataList.data.list
        console.dir(dataList.data.list)
      },
    },
  }
</script>
<style scoped>
  .search-view {
    padding: 30px;
    text-align: center;
  }
  .search-view .input-with-select {
    max-width: 500px;
    border-color: red !important;
  }
  ul {
    margin: 0;
    padding: 0;
  }
  ul li {
    padding: 10px;
    list-style: none;
  }
  ul li:hover {
    background-color: rgba(24, 144, 255, 0.1);
  }
  .unit_name {
    margin: 15px 0;
    font-size: 18px;
    color: #333;
  }
  .font_13 {
    font-size: 14px;
    margin-bottom: 10px;
  }
  .btn_list button {
    font-size: 15px;
  }
</style>
