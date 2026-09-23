<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1080px"
      @close="close"
    >
      <el-card shadow="never">
        <vab-query-form class="margin-b0">
          <vab-query-form-top-panel :span="24">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <!-- <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            > -->
              <el-form-item>
                <el-input
                  v-model="queryForm.elementnumber"
                  clearable
                  placeholder="要素编号"
                  width="50px"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.elementname"
                  clearable
                  placeholder="要素名称"
                  width="50px"
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
                <el-button
                  native-type="submit"
                  type="primary"
                  @click="resetSearch"
                >
                  重置
                </el-button>
              </el-form-item>
              <!-- <el-form-item>
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
            </el-form-item> -->
            </el-form>
          </vab-query-form-top-panel>
        </vab-query-form>
      </el-card>
      <el-col :span="24">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="24">
            <!-- <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success" @click="handleAdd">导入要素</el-button>
            </div> -->
            <el-table
              border
              :data="tableData"
              fit
              highlight-current-row
              style="width: 100%; margin-bottom: 25px"
              @selection-change="handleSelectionChange"
            >
              <!-- <el-table-column align="center" label="序号" prop="id">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column> -->
              <el-table-column type="selection" />
              <el-table-column
                align="center"
                label="要素编号"
                prop="elementnumber"
                min-width="10%"
              />

              <el-table-column
                align="center"
                label="要素名称"
                prop="elementname"
                min-width="10%"
              >
                <!-- <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.riskSn"
                    size="mini"
                    style="width: 90%"
                  />
                </template> -->
              </el-table-column>
              <el-table-column
                align="center"
                label="业务类别"
                prop="businesstype"
                min-width="10%"
              >
                <!-- <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.unitName"
                    size="mini"
                    style="width: 90%"
                  />
                </template> -->
              </el-table-column>
              <el-table-column align="center" label="业务属性" min-width="10%">
                <template #default="{ row }">
                  {{
                    row.businessattribute == 1
                      ? '独立评价'
                      : row.businessattribute == 2
                      ? '自我评价'
                      : '其他'
                  }}
                </template>
                <!-- <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.unitName"
                    size="mini"
                    style="width: 90%"
                  />
                </template> -->
              </el-table-column>
              <el-table-column
                align="center"
                label="评分规则"
                prop="assessrules"
                min-width="5%"
              >
                <!-- <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.secondUnit"
                    size="mini"
                    style="width: 50%"
                  />
                </template> -->
              </el-table-column>
              <el-table-column
                align="center"
                label="审查要点"
                min-width="50%"
                prop="data"
              >
                <!-- <template #defualt="{ row }">{{ row.businesstype }}</template> -->
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.auditpoint"
                    style="width: 100%"
                    type="textarea"
                    readonly
                    class="inputDeep"
                  />
                </template>
              </el-table-column>

              <!-- <el-table-column
                align="center"
                label="标准分"
                prop="standardscore"
                min-width="5%"
              >

              </el-table-column> -->

              <!-- <el-table-column
                align="center"
                fixed="right"
                label="操作"
                width="150"
              >
                <template slot-scope="scope">
                  <el-button type="text" @click="handleDelete(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column> -->
            </el-table>
            <el-pagination
              background
              class="pager"
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </el-col>
        </el-form>
      </el-col>

      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  import { createProjectCode } from '@/api/internal/project'
  import { importYaosu, yaosuAdd } from '@/api/internal/evaluationTemplate'
  export default {
    name: 'PlanEdit',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        tmplId: '',
        catId: '',
        total: 0,
        title: '',
        layout: 'total, sizes, prev, pager, next, jumper',
        dialogFormVisible: false,
        queryForm: {
          pageSize: 20,
          pageNumber: 1,
        },
        formData: {
          field101: undefined,
          field102: undefined,
          field103: undefined,
          field104: undefined,
        },
        emitTable: [],
        rules: {},
        tableData: [],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      save() {
        let idList = []
        const importList = {
          nodeId: this.catId,
          tmplId: this.tmplId,
          id: '',
        }
        this.emitTable.forEach((res) => {
          idList.push(res.asseleid)
        })
        importList.id = idList.toString() + ','
        yaosuAdd(importList).then((res) => {
          if (res.code == 1) {
            this.$emit('fetchData')
            this.close()
          }
        })
        //
        //
      },
      showEdit(tmplId, catId) {
        //重置搜索框

        this.title = '选择要素'
        //
        this.tmplId = tmplId
        this.catId = catId
        this.queryForm = {
          pageSize: 20,
          pageNumber: 1,
          tmplId: this.tmplId,
        }
        this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleAdd() {
        //formData的表格新增一行
      },
      handleDelete(index) {
        // this.formData.tableData.splice(index, 1)
      },
      handleEdit() {},
      handleSelectionChange(val) {
        this.emitTable = val
      },
      resetSearch() {
        this.queryForm = {
          pageSize: 20,
          pageNumber: 1,
          tmplId: this.tmplId,
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        // const info = this.queryForm
        const { pageNumber, pageSize, tmplId, ...other } = this.queryForm
        const data = other
        const params = {
          pageNumber: pageNumber,
          pageSize: pageSize,
          choiceSearch: '',
          tmplId: tmplId,
        }
        //
        const {
          data: {
            pageBean: { records, total },
          },
        } = await importYaosu(data, params)

        // 禁用状态的要素过滤掉
        this.tableData = records.filter((x) => x.status === '1')
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss">
  .inputDeep {
    :deep(.el-textarea__inner) {
      box-shadow: 0 0 0 0px var(--el-input-border-color, var(--el-border-color))
        inset;
      resize: none;
      cursor: default;
      border: none;
    }
  }
</style>
