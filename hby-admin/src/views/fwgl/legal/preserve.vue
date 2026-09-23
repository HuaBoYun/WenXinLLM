<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.disputeitem"
              clearable
              placeholder="纠纷主题"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.preservednature"
              clearable
              placeholder="保全资质性质"
            />
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="11">
                <el-input
                  v-model="queryForm.minPreservedAmount"
                  clearable
                  placeholder="最小保全资产数额"
                />
              </el-col>
              <el-col class="line" :span="2" style="text-align: center">
                -
              </el-col>
              <el-col :span="11">
                <el-input
                  v-model="queryForm.preservedamount"
                  clearable
                  placeholder="最大保全资产数额"
                />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="11">
                <el-input
                  v-model="queryForm.minExceteAmount"
                  clearable
                  placeholder="最小执行金额"
                />
              </el-col>
              <el-col class="line" :span="2" style="text-align: center">
                -
              </el-col>
              <el-col :span="11">
                <el-input
                  v-model="queryForm.exceteamount"
                  clearable
                  placeholder="最大执行金额"
                />
              </el-col>
            </el-row>
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
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="24">
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="纠纷主题" prop="disputeitem" />
      <el-table-column
        align="center"
        label="保全资产性质"
        prop="preservednature"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.preservednature }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="是否申请保全"
        prop="applypreservation"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.applypreservation === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否执行扣划" prop="isperformed">
        <template slot-scope="scope">
          <span>{{ scope.row.isperformed === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否接触保全" prop="iscancel">
        <template slot-scope="scope">
          <span>{{ scope.row.iscancel === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="(被)保全资产数额(万元)"
        prop="preservedamount"
      />
      <el-table-column
        align="center"
        label="(被)执金额(万元)"
        prop="exceteamount"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <PreserveEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getqualification, Removequalification } from '@/api/fwgl/legal'
  import PreserveEdit from '@/views/fwgl/legal/components/PreserveEdit'
  export default {
    name: 'Preserve',
    components: { PreserveEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '779383',
          disputeitem: undefined,
          preservednature: undefined,
          minPreservedAmount: undefined,
          minExceteAmount: undefined,
          exceteamount: undefined,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await getqualification(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await Removequalification({ qualId: row.qualid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
