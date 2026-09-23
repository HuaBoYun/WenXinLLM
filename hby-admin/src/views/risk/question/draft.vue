<template>
  <div class="system-log-container">
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
                v-model="queryForm.sheetcode"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />

              <el-input
                v-model="queryForm.sheetname"
                clearable
                v-if="item.name === '底稿名称'"
                placeholder="底稿名称"
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
              <el-tooltip class="item" effect="dark" content="搜索筛选" placement="top">
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="底稿编号" prop="worksheetnumber" width="100">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.worksheetnumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '底稿名称'"
            align="center"
            label="底稿名称"
            prop="worksheetname"
          />
          <el-table-column
            v-if="item.name === '审计目标'"
            align="center"
            label="审计目标"
            prop="audittarget"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '被审计单位'"
            align="center"
            label="被审计单位"
            prop="auditedorg"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿人'"
            align="center"
            label="拟稿人"
            prop="recorder"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿日期'"
            align="center"
            label="拟稿日期"
            prop="recordingdate"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="state"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.state == "2"
                  ? "复核中"
                  : row.state == "3"
                  ? "复核终止"
                  : row.state == "4"
                  ? "复核通过"
                  : row.state == "5"
                  ? "需调整"
                  : "未复核"
              }}
            </template>
          </el-table-column>
        </div>
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
    <DraftInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
import { dgAllPageList ,dgDetail } from "@/api/risk/question";
import DraftInfo from "./components/DraftInfo";
import { formatDate } from "@/utils/index";

import filterSearch from "@/components/filterSearch";
import filterTable from "@/components/filterTable";
import { searchTableMixis } from "@/mixis/index";
export default {
  name: "Download",
  components: { DraftInfo, filterSearch, filterTable },

  mixins: [searchTableMixis],
  data() {
    return {
      list: [],
      listLoading: true,
      layout: "total, sizes, prev, pager, next, jumper",
      total: 0,
      queryForm: {
        sheetcode: "",
        sheetname: "",
        pageNo: 1,
        pageSize: 20,
      },
      filedAll: [
        { name: "底稿名称" },
        { name: "审计目标" },
        { name: "被审计单位" },
        { name: "拟稿人" },
        { name: "拟稿日期" },
        { name: "所属项目" },
        { name: "状态" },
      ], //所有表格项
      filedNow: [], //当前表格项
      searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: "audit-question-draft-search",
      tableKey: "audit-question-draft-list",
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
        { name: "底稿编号", key: "sheetcode" },
        { name: "底稿名称", key: "sheetname" },
      ];
    },
    resetQueryForm() {
      // this.queryForm = this.$options.data().queryForm;
      this.queryForm = {
        sheetcode: "",
        sheetname: "",
        pageNo: 1,
        pageSize: 20,
      };
    },
    resetSearch() {
      this.resetQueryForm();
      this.fetchData();
    },
    formatDate(row, column) {
      // 获取单元格数据
      let data = row[column.property];
      return formatDate(data);
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
      const {
        data: {
          pageBean: { records: list, total: total },
        },
      } = await dgAllPageList(this.queryForm);
      this.list = list;
      this.total = total;
      this.listLoading = false;
    },
    // handleAdd() {
    //   this.$refs['edit'].showEdit('add', null)
    // },
    async handleDetail(row) {
      const data = await dgDetail({ worksheetid: row.worksheetid });
      await this.$refs["edit"].showEdit("detail", data.data);
    },
    // async handleEdit(row) {
    //   const data = await dgDetail({ sheetid: row.sheetid })
    //   await this.$refs['edit'].showEdit('edit', data.data)
    // },
    // handleDelete(row) {
    //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
    //     const { msg, code } = await riskDel({
    //       riskid: row.riskid,
    //     })
    //     if (code == 0) {
    //       this.$baseMessage(msg, 'success')
    //     } else {
    //       this.$baseMessage(msg, 'error')
    //     }
    //     await this.fetchData()
    //   })
    // },
    sendModel() {
      this.$refs["sendModel"].showEdit();
    },
    send() {
      this.$refs["send"].showEdit();
    },
  },
};
</script>

<style scoped lang="scss">
.system-log-container {
  padding: 0 !important;
  background: #f6f8f9 !important;
}
.margin-b0 {
  margin-bottom: 0;
}
</style>
