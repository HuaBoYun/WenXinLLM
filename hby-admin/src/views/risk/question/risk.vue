<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <el-card shadow="never">
          <project-data-tree @getChildParam="setTree" />
        </el-card>
      </div>
      <div class="right">
        <vab-query-form class="margin-b0">
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  :prop="item.key"
                  v-for="(item, index) in searchItem"
                  :key="index"
                >
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    v-if="item.name === '风险编号'"
                    placeholder="风险编号"
                  />
                  <el-input
                    v-if="item.name === '风险名称'"
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="风险名称"
                  />

                  <el-date-picker
                    v-if="item.name === '发现日期'"
                    v-model="queryForm.Date"
                    clearable
                    end-placeholder="发现结束日期"
                    format="yyyy-MM-dd"
                    range-separator="-"
                    start-placeholder="发现开始日期"
                    :style="{ width: '100%' }"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    native-type="submit"
                    type="primary"
                    @click="fetchData"
                  >
                    查询
                  </el-button>
                </el-form-item>
                <el-form-item>
                  <el-button native-type="submit" type="primary" @click="resetSearch">
                    重置
                  </el-button>
                </el-form-item>

                <el-form-item>
                  <el-tooltip
                    class="item"
                    effect="dark"
                    content="搜索筛选"
                    placement="top"
                  >
                    <el-popover placement="left" trigger="click">
                      <filter-search
                        v-if="true"
                        :list="searchAll"
                        :name="localKey"
                        @updateSearchShow="initSearch"
                      />
                      <el-button slot="reference" style="height: 32px">
                        <vab-icon icon="filter" :is-custom-svg="true" />
                      </el-button>
                    </el-popover>
                  </el-tooltip>
                </el-form-item>
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>
        <el-card shadow="never">
          <vab-query-form-right-panel :span="24">
            <el-tooltip class="item" effect="dark" content="表格筛选" placement="top">
              <el-popover placement="right" trigger="click">
                <filter-table
                  :list="filedAll"
                  :name="tableKey"
                  @updateTableShow="initTable"
                />
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <el-button type="success" @click="handleAdd">新建</el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="风险编号"
              prop="risknumber"
              width="100"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                v-if="item.name === '风险名称'"
                align="center"
                label="风险名称"
                prop="riskname"
              />
              <el-table-column
                v-if="item.name === '发现时间'"
                align="center"
                label="发现时间"
                prop="discovereddate"
              />
              <el-table-column
                align="center"
                label="责任部门"
                v-if="item.name === '责任部门'"
                prop="tblOrganiDem.orgname"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                v-if="item.name === '发现人'"
                label="发现人"
                prop="realname"
                show-overflow-tooltip
              />
            </div>

            <el-table-column align="center" label="操作" show-overflow-tooltip>
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">修改</el-button>
                <el-button type="text" @click="handleDelete(row)"> 删除 </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <risk-info ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
import { riskList, riskDel, riskDetail } from "@/api/risk/question";
import RiskInfo from "./components/RiskInfo";
import ProjectDataTree from "./components/ProjectDataTree";
import { formatDate, parseTime } from "@/utils/index";
import filterSearch from "@/components/filterSearch";
import filterTable from "@/components/filterTable";
import { searchTableMixis } from "@/mixis/index";
export default {
  name: "Download",
  components: { RiskInfo, ProjectDataTree, filterSearch, filterTable },
  mixins: [searchTableMixis],
  data() {
    return {
      list: [],
      listLoading: true,
      layout: "total, sizes, prev, pager, next, jumper",
      total: 0,
      queryForm: {
        risknumber: "",
        riskname: "",
        orgid: "",
        Date: [],
        pageNo: 1,
        pageSize: 20,
      },

      filedAll: [
        { name: "风险名称" },
        { name: "发现时间" },
        { name: "责任部门" },
        { name: "发现人" },
      ], //所有表格项
      filedNow: [], //当前表格项
      searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: "audit-question-risk-search",
      tableKey: "audit-question-risk-list",
      searchMore: true,
    };
  },
  created() {
    this.fetchData();

    this.initTable();
    this.searchNow = this.getFiled();
    this.searchItem = this.searchNow.slice(0, 4);
    this.initSearch();
  },
  methods: {
    getFiled() {
      return [
        { name: "风险编号", key: "risknumber" },
        { name: "风险名称", key: "riskname" },
        { name: "发现日期", key: "Date" },
      ];
    },
    setTree(node) {
      let id = node.pId === 1 ? undefined : node.id;
      this.queryForm.orgid = id;
      this.fetchData();
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val;
      this.fetchData();
    },
    handleCurrentChange(val) {
      this.queryForm.pageNo = val;
      this.fetchData();
    },
    queryData() {
      this.queryForm.pageNo = 1;
      this.fetchData();
    },
    async fetchData() {
      this.listLoading = true;
      const { Date, ...other } = this.queryForm;
      let startDate = "";
      let endDate = "";
      if (Date) {
        startDate = Date[0];
        endDate = Date[1];
      }
      const {
        data: {
          pageInfo: { tlist: list, totalRecord: total },
        },
      } = await riskList({ ...other, startDate, endDate });
      this.list = list;
      this.list.forEach((item) => {
        if (item.occureddate) {
          item.occureddate = parseTime(item.occureddate, "{y}-{m}-{d}");
        }
        if (item.discovereddate) {
          item.discovereddate = parseTime(item.discovereddate, "{y}-{m}-{d}");
        }
      });
      console.dir(this.list);
      this.total = total;
      this.listLoading = false;
    },
    handleExport(row) {},
    handleAdd() {
      this.$refs["edit"].showEdit("add", null);
    },
    async handleDetail(row) {
      const data = await riskDetail({ riskid: row.riskid });
      await this.$refs["edit"].showEdit("detail", data.data);
    },
    async handleEdit(row) {
      const data = await riskDetail({ riskid: row.riskid });
      data.data.risk.occureddate = parseTime(data.data.risk.occureddate, "{y}-{m}-{d}");
      data.data.risk.discovereddate = parseTime(
        data.data.risk.discovereddate,
        "{y}-{m}-{d}"
      );
      console.dir(data);
      await this.$refs["edit"].showEdit("edit", data.data);
    },
    handleDelete(row) {
      this.$baseConfirm("你确定要删除当前项吗", null, async () => {
        const { msg, code } = await riskDel({
          riskid: row.riskid,
        });
        if (code == 1) {
          this.$baseMessage(msg, "success");
        } else {
          this.$baseMessage(msg, "error");
        }
        await this.fetchData();
      });
    },
    sendModel() {
      this.$refs["sendModel"].showEdit();
    },
    send() {
      this.$refs["send"].showEdit();
    },
    resetSearch() {
      this.resetQueryForm();
      this.fetchData();
    },
    resetQueryForm() {
      // this.queryForm = this.$options.data().queryForm;
      this.queryForm = {
        risknumber: "",
        riskname: "",
        orgid: "",
        Date: [],
        pageNo: 1,
        pageSize: 20,
      };
    },
  },
};
</script>
<style scoped>
.lr-layout {
  display: flex;
}

.lr-layout > .left {
  /* width: 200px; */
  border-right: 1px solid ghostwhite;
  margin-right: 10px;
  padding-right: 10px;
}

.lr-layout > .right {
  width: 85%;
}
</style>
<style scoped lang="scss">
.system-log-container {
  padding: 0 !important;
  background: #f6f8f9 !important;
}
.margin-b0 {
  margin-bottom: 0;
}
</style>
