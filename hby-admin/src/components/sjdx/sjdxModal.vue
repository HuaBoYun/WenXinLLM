<template>
  <el-dialog
    :visible.sync="dialogVisible"
    fullscreen
    :close-on-click-modal="false"
    :modal="false"
  >
    <div>
      <div class="lr-layout">
        <div class="left">
          <DeepTree @select="handleNodeClick" :companyName="companyName" />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-left-panel :span="24">
              <el-input
                v-model="queryForm.fileName"
                placeholder="请输入文档名称"
                clearable
                style="width: 50%; margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="getExecutorList"
                style="margin-top: 10px !important"
              >
                查询
              </el-button>
              <el-button
                type="primary"
                @click="reset"
                style="margin-top: 10px !important"
              >
                重置
              </el-button>
              <el-button
                type="primary"
                @click="onAdvancedSearch"
                style="margin-top: 10px !important"
              >
                高级搜索
              </el-button>
            </vab-query-form-left-panel>
          </vab-query-form>
          <el-table :data="list" style="width: 100%">
            <el-table-column label="文档名称" prop="name"></el-table-column>
            <el-table-column
              prop="fileNo"
              label="文号"
              width="200"
            ></el-table-column>
            <el-table-column
              prop="fileNo"
              label="状态"
              width="50"
              align="center"
            >
              <template #default="{ row }">
                {{ row.fileStatus == '1' ? '在用' : '废止' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="filePublicationDate"
              label="发文时间"
              align="center"
              width="100"
            >
              <template #default="{ row }">
                {{ thirteenBitTimestampForYear(row.filePublicationDate) }}
              </template>
            </el-table-column>

            <el-table-column
              prop="231"
              label="操作"
              width="100"
              align="center"
              #default="{ row }"
            >
              <el-button type="text" @click="handleExport(row)">预览</el-button>
              <el-button type="text" @click="download(row)">下载</el-button>
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
      <el-dialog
        :visible.sync="auditQuestionVisible"
        fullscreen
        :close-on-click-modal="false"
        append-to-body
      >
        <div style="height: 100vh">
          <iframe
            :src="ingUrl"
            width="100%"
            height="100%"
            frameborder="0"
          ></iframe>
        </div>
      </el-dialog>
      <el-dialog
        :visible.sync="auditQuestionVisibleExcel"
        fullscreen
        :close-on-click-modal="false"
        append-to-body
      >
        <div style="height: 100vh">
          <vue-office-excel :src="ingUrl" />
        </div>
      </el-dialog>
      <el-dialog
        title="高级搜索"
        :visible.sync="advancedSearchVisible"
        fullscreen
        :close-on-click-modal="false"
        append-to-body
      >
        <AdvancedSearch :guide="guide" v-if="advancedSearchVisible" />
      </el-dialog>
    </div>
  </el-dialog>
</template>
<script>
  import DeepTree from './tree.vue'
  import {
    sjdxList,
    exportWord,
    exportExcel,
    exportPdf,
  } from '@/api/setting/sjzy'
  import { thirteenBitTimestampForYear } from '@/utils'
  import AdvancedSearch from '@/components/AdvancedSearch/AdvancedSearch.vue'
  import AuditQuestion from '@/components/AuditQuestion/AuditQuestion.vue'
  //引入VueOfficeDocx组件
  import VueOfficeDocx from '@vue-office/excel'
  //引入相关样式
  import '@vue-office/excel/lib/index.css'

  export default {
    props: {},
    components: { DeepTree, AdvancedSearch, AuditQuestion, VueOfficeDocx },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dialogVisible: false,
        list: [],
        queryForm: {
          agcId: undefined,
          fileName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        advancedSearchVisible: false,
        auditQuestionVisible: false,
        auditQuestionVisibleExcel: false,
        companyName: '',
        guide: '',
        ingUrl: '',
      }
    },
    methods: {
      thirteenBitTimestampForYear,
      showEdit(row) {
        this.companyName = row.companyName
        this.guide = row.guide
        this.dialogVisible = true
        // this.list = info.data.tlist
        // this.total = info.data.totalRecord
      },
      async getExecutorList() {
        const res = await sjdxList(this.queryForm)
        this.list = res.data.tlist
        this.total = res.data.totalRecord
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.agcId = val.id
        this.getExecutorList()
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      reset() {
        this.queryForm.fileName = ''
        this.getExecutorList()
      },
      download(row) {
        window.open(row.url)
      },
      onAdvancedSearch() {
        this.advancedSearchVisible = true
      },
      async handleExport(row) {
        if (row.path.includes('doc')) {
          console.log('进指引的word啦')
          exportWord({
            id: row.id,
          }).then((res) => {
            this.ingUrl = window.URL.createObjectURL(res.data)
            setTimeout(() => {
              this.auditQuestionVisible = true
            }, 500)
          })
        }
        if (row.path.includes('xlsx')) {
          console.log('进指引的excel啦')
          exportExcel({
            id: row.id,
          }).then((res) => {
            this.ingUrl = window.URL.createObjectURL(res.data)
            setTimeout(() => {
              this.auditQuestionVisibleExcel = true
            }, 500)
          })
        }
        if (row.path.includes('pdf')) {
          console.log('进指引的pdf啦')
          exportPdf({
            id: row.id,
          }).then((res) => {
            this.ingUrl = window.URL.createObjectURL(res.data)
            setTimeout(() => {
              this.auditQuestionVisible = true
            }, 500)
          })
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: block !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
