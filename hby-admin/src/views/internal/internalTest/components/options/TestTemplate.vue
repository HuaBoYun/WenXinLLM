<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    :modal="false"
    width="1400px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.templeNumber"
              clearable
              placeholder="模板编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.templename"
              clearable
              placeholder="模板名称"
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
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      v-loading="listLoading"
      highlight-current-row
      @current-change="handleSelected"
    >
      <el-table-column align="center" label="编号" prop="templenumber">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.templenumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="名称" prop="templename" />
      <el-table-column align="center" label="来源" prop="source" />
      <el-table-column
        align="center"
        label="创建人"
        prop="chuangjianren.realname"
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createtime"
        :formatter="formatDate"
      />
      <el-table-column align="center" label="说明" prop="templedesc" />
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
    <!-- <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
    <TemplateEditStep1 ref="step1" />
  </el-dialog>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import { defTmplList, tmplCount } from '@/api/internal/plan'
  import TemplateEditStep1 from '@/views/workbench/internalTools/components/TemplateEditStep1.vue'

  export default {
    name: 'TrackView',
    components: {
      TemplateEditStep1,
    },
    data() {
      return {
        queryForm: {
          templeNumber: '',
          templename: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        total: 3,
        list: [],
        title: '测试模板',
        current: undefined,
        dialogFormVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        secrectLevelId: '',
      }
    },
    created() {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      show(secrectLevelId = '') {
        this.secrectLevelId = secrectLevelId
        this.queryData()
        this.dialogFormVisible = true
      },
      async confirm() {
        const { data, code } = await tmplCount({
          templeid: this.current.testtemid,
        })
        console.log('🚀 ~ confirm ~ code:', code)
        if (code == 0) return
        if (!this.current) {
          this.$baseMessage(
            '请选择测试模板！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
          },
        } = await defTmplList({
          ...this.queryForm,
          secrectLevelId: this.secrectLevelId,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['step1'].show(row, 'view')
      },
    },
  }
</script>
