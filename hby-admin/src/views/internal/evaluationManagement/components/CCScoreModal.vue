<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
    @close="close"
  >
    <div class="system-log-container">
      <div>
        <div>
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save" v-if="!isActive">
                确 定
              </el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table ref="multipleTable" :data="list">
            <el-table-column
              label="参评人ID"
              prop="username"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="realname"
              label="参评人名称"
              align="center"
            ></el-table-column>
            <el-table-column prop="score" label="权重" align="center">
              <template slot-scope="scope">
                <el-input
                  :disabled="isActive"
                  @input="handleInput(scope.$index, scope.row)"
                  v-model="commitData[scope.$index].score"
                  size="mini"
                  style="width: 90%"
                >
                  <template slot="append">%</template>
                </el-input>
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
    </div>
  </el-dialog>
</template>

<script>
  import { ccScoreList, saveCCScoreInfo } from '@/api/internal/project'
  export default {
    name: 'xxxxxx',
    data() {
      return {
        isActive: false,
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
        assmarkid: undefined, //用于分数保存
        select: [],
        commitData: [], //用于提交分数保存数组
        totalScore: 0, //总分，用于判断是否可以保存
      }
    },
    methods: {
      showEdit(row, isActive) {
        this.isActive = isActive
        // console.log(this.isActive)
        this.assmarkid = row.assmarkid
        this.dialogVisible = true
        this.getExecutorList()
      },
      save() {
        if (+this.totalScore !== 100) {
          this.$baseMessage(
            '权重比一定要等于100%',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        const ids = this.commitData.map((res) => +res.assstaffid)
        const values = this.commitData.map((res) => +res.score)
        saveCCScoreInfo({
          values: values.toString(),
          ids: ids.toString(),
          assMarkId: this.assmarkid,
        }).then((res) => {
          if (res.msg == 'success') {
            this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
            this.$emit('fetchData')
            this.dialogVisible = false
          }
        })
      },

      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },

      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await ccScoreList({ ...this.queryForm, assMarkId: this.assmarkid })
        this.list = records
        this.total = total
        this.listLoading = false
        const info = records.map((res) => {
          return {
            assmarkid: res.assmarkid,
            assstaffid: res.assstaffid,
            score: res.assweight,
          }
        })
        this.commitData = info
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleInput() {
        const info = this.commitData.map((res) => res.score)
        const arr = info.reduce((cur, pre) => {
          return +cur + +pre
        })
        this.totalScore = arr
        // if (arr !== 100) {
        //   this.$baseMessage(
        //     '权重比一定要等于100%',
        //     'error',
        //     'vab-hey-message-error'
        //   )
        // }
      },
      close() {
        this.dialogFormVisible = false
        this.select = []
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
