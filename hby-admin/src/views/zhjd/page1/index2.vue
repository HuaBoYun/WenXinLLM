<template>
  <div class="issue-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.code"
              clearable
              placeholder="问题线索编码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="问题线索名称"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.category" placeholder="问题线索类别">
              <el-option label="类型一" value="category1"></el-option>
              <el-option label="类型二" value="category2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.lossLevel" placeholder="资产损失等级">
              <el-option label="等级一" value="level1"></el-option>
              <el-option label="等级二" value="level2"></el-option>
              <el-option label="等级三" value="level3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="当前状态">
              <el-option label="状态一" value="status1"></el-option>
              <el-option label="状态二" value="status2"></el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item>
            <el-date-picker
              v-model="queryForm.time"
              end-placeholder="报告开始时间"
              range-separator="至"
              start-placeholder="报告结束时间"
              type="daterange"
            />
          </el-form-item> -->
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
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span>
          全部：
          <span class="c-blue padding-right-16 border-right">{{ allNum }}</span>
        </span>
        <span class="margin-left-10">
          受理：
          <span class="c-green padding-right-16 border-right">
            {{ draftingNum }}
          </span>
        </span>
        <span class="margin-left-10">
          初步核实：
          <span class="c-green padding-right-16 border-right">
            {{ approvalNum }}
          </span>
        </span>
        <span class="margin-left-10">
          分类处置：
          <span class="c-green">{{ acceptedNum }}</span>
        </span>
        <span class="margin-left-10">
          核查：
          <span class="c-green">{{ acceptedNum }}</span>
        </span>
        <span class="margin-left-10">
          处理：
          <span class="c-green">{{ acceptedNum }}</span>
        </span>
        <span class="margin-left-10">
          整改：
          <span class="c-green">{{ acceptedNum }}</span>
        </span>
        <span class="margin-left-10">
          销号：
          <span class="c-green">{{ acceptedNum }}</span>
        </span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        label="序号"
        type="index"
        :index="indexMethod"
      ></el-table-column>
      <el-table-column align="center" label="问题线索编码" prop="data" />
      <el-table-column align="center" label="问题线索名称" prop="data" />
      <el-table-column align="center" label="涉及企业名称" prop="data" />
      <el-table-column align="center" label="问题线索类别" prop="data" />
      <el-table-column align="center" label="问题线索来源" prop="data" />
      <el-table-column align="center" label="资产损失等级" prop="data" />
      <el-table-column align="center" label="状态" prop="data" />
      <el-table-column align="center" label="操作" width="120">
        <template #default="{ row }">
          <el-button type="text" @click="handleFollowUpReport(row)">
            后续报告
          </el-button>
          <el-button type="text" @click="handleViewDetail(row)">查看</el-button>
          <!-- <el-button type="text">导出</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <editModal v-if="editModalShow" @close="close" :title="title" />
  </div>
</template>

<script>
  import editModal from './components/editIssueModal.vue'
  export default {
    name: 'realTimeReportList',
    components: { editModal },
    data () {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        allNum: 4,
        title: '新增',
        editModalShow: false,
        draftingNum: 1,
        approvalNum: 2,
        acceptedNum: 1,
        queryForm: {
          code: '',
          name: '',
          category: '',
          lossLevel: '',
          // time: '',
          status: ''
        }
      }
    },
    created () {
      console.log('created')
      this.fetchData()
      this.listLoading = false
    },
    methods: {
      fetchData () {
        console.log('fetchData')
      },
      resetSearch () {
        this.queryForm.code = ''
        this.queryForm.name = ''
        this.queryForm.category = ''
        this.queryForm.lossLevel = ''
        // this.queryForm.time = ''
        this.queryForm.status = ''
        this.fetchData()
        console.log('resetSearch')
      },
      handleAdd () {
        this.editModalShow = true
        console.log('handleAdd')
      },
      handleExport () {
        console.log('handleExport')
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      indexMethod (index) {
        return index
      },
      close () {
        this.editModalShow = false
      },
    }
  }
</script>

<style scoped>
.c-blue {
  color: #409eff;
}

.c-green {
  color: #67c23a;
}

.margin-left-10 {
  margin-left: 10px;
}

.margin-right-10 {
  margin-right: 10px;
}

.border-right {
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

.padding-right-16 {
  padding-right: 16px;
}
</style>
