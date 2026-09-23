<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          :data="data"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <el-table v-loading="listLoading" :data="list">
          <el-table-column align="center" label="编号" prop="ELEMENTCODE">
            <template #default="{ row }">
              <el-button style="color: red" type="text" @click="handInfo(row)">
                {{ row.ELEMENTCODE }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="业务描述"
            prop="BUSINESSDESC"
          />
          <el-table-column align="center" label="风险描述" prop="RISKTYPE" />
          <el-table-column align="center" label="检查方法" prop="CHECKMETHOD" />
          <el-table-column
            align="center"
            label="控制目标"
            prop="CONTROLTARGET"
          />
          <el-table-column
            align="center"
            label="控制方法"
            prop="CONTROLMETHOD"
          />
          <!-- <el-table-column align="center" label="测试程序" prop="PROCEDURES" /> -->
          <el-table-column align="center" label="测试结果" prop="TESTRESULT" />
          <el-table-column
            align="center"
            label="设计有效性"
            prop="DESIGNPOINTVALIDITY"
          />
          <el-table-column
            align="center"
            label="执行有效性"
            prop="EXECUTEPOINTVALIDITY"
          />
          <el-table-column
            align="center"
            label="测试有效性"
            prop="TESTPOINTVALIDITY"
          >
            <template #default="{ row }">
              {{
                row.TESTPOINTVALIDITY == '1'
                  ? '有效'
                  : row.TESTPOINTVALIDITY == '2'
                  ? '无效'
                  : '不适用'
              }}
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <ResultsInfo ref="ResultsInfo" />
    <component :is="dynamicComponent" v-if="dynamicComponent" ref="edit" />
  </el-dialog>
</template>
<script>
  // import { getList } from '@/api/systemLog'
  import {
    gettreeAll,
    gettreeBxy,
    gettreeWx,
    gettreeYx,
    gettreeNumber,
    defListAll,
    defListBxy,
    defListWx,
    defListYx,
    defListNum,
  } from '@/api/internal/results'
  import ResultsInfo from '@/views/internal/internalTest/components/ResultsInfo.vue'
  export default {
    name: 'ResultsView',
    components: {
      ResultsInfo,
    },
    created() {
      import('@/views/internal/internalTest/components/TaskForm.vue').then(
        (module) => {
          this.dynamicComponent = module.default
        }
      )
    },
    data() {
      return {
        dynamicComponent: null, // 用于存储动态加载的组件

        defaultProps: {
          children: 'children',
          label: 'name',
        },
        data: [
          {
            id: 1,
            label: '123',
            children: [],
          },
        ],
        title: '测试任务',
        list: [],
        type: '',
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planid: '',
          templId: '',
          node: '',
          userid: '',
          pageNumber: 1,
          pageSize: 5,
        },
        dialogFormVisible: false,
        arr: [],
      }
    },
    methods: {
      handleNodeClick(data) {
        this.queryForm.node = data.id
        this.queryData()
        // console.log(data)
      },
      showEdit(row, type) {
        this.list = this.$options.list
        if (type == 'ALLCOUNT') {
          this.type = 'ALLCOUNT'
          this.getAllTree(row)
        } else if (type == 'YCOUNT') {
          this.type = 'YCOUNT'
          this.getYTree(row)
        } else if (type == 'WCOUNT') {
          this.type = 'WCOUNT'
          this.getWTree(row)
        } else if (type == 'BCOUNT') {
          this.type = 'BCOUNT'
          this.getBTree(row)
        } else if (type == 'NUMBER') {
          this.type = 'NUMBER'
          this.getNumberTree(row)
        }
        this.dialogFormVisible = true
      },
      async getAllTree(row) {
        const data = await gettreeAll({ planid: row.TESTPLANID })
        this.data = data.data.tree
        this.queryForm.planid = data.data.planid
        this.queryForm.templId = data.data.templId
      },
      async getYTree(row) {
        const data = await gettreeYx({ planid: row.TESTPLANID })
        this.data = data.data.tree
        this.queryForm.planid = data.data.planid
        this.queryForm.templId = data.data.templId
      },
      async getWTree(row) {
        const data = await gettreeWx({ planid: row.TESTPLANID })
        this.data = data.data.tree
        this.queryForm.planid = data.data.planid
        this.queryForm.templId = data.data.templId
      },
      async getBTree(row) {
        const data = await gettreeBxy({ planid: row.TESTPLANID })
        this.data = data.data.tree
        this.queryForm.planid = data.data.planid
        this.queryForm.templId = data.data.templId
      },
      async getNumberTree(row) {
        const data = await gettreeNumber({
          planid: row.PLANID,
          userid: row.CPUSERID,
        })
        this.data = data.data.tree
        this.queryForm.planid = data.data.planid
        this.queryForm.templId = data.data.templId
        this.queryForm.userid = data.data.userid
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        let fun = defListAll
        if (this.type == 'ALLCOUNT') {
          fun = defListAll
        } else if (this.type == 'YCOUNT') {
          fun = defListYx
        } else if (this.type == 'WCOUNT') {
          fun = defListWx
        } else if (this.type == 'BCOUNT') {
          fun = defListBxy
        } else if (this.type == 'NUMBER') {
          fun = defListNum
        }
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
          },
        } = await fun(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handInfo(row) {
        // this.$refs['ResultsInfo'].showEdit(row, this.queryForm.planid)
        this.$refs['edit'].showEdit(
          row,
          'view',
          this.queryForm.planid,
          this.queryForm.templId,
          this.queryForm.node
        )
      },
      showModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      close() {
        this.dialogFormVisible = false
      },
      generateIncrementalArray(start = 1, end = 10) {
        this.arr = Array.from({ length: end - start + 1 }, (_, i) => start + i)
        return this.arr
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
  .el-table thead.is-group th.el-table__cell {
    background: #fff;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:before {
    content: '日期';
    text-align: center;
    position: absolute;
    width: 152px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:after {
    content: '配送新增';
    text-align: center;
    position: absolute;
    width: 152px;
    top: 10px;
    left: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(38deg);
    transform-origin: top left;
    -ms-transform: rotate(38deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(38deg);
    -webkit-transform-origin: top left;
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
  // .left /deep/ .el-tree {
  //   overflow: auto;
  // }
  .left {
    :deep(.el-tree) {
      overflow: auto;
    }
  }
  .lr-layout > .right {
    // width: 100%;
    flex: 1;
  }
</style>
