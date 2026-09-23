<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15"></el-row>

      <el-col :span="24">
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <!-- <el-button type="success" @click="handleDelete()">
              批量删除
            </el-button>
            <el-button type="success" @click="handleEdit(tmplId, catId)">
              导入要素
            </el-button> -->
          </div>
          <el-table
            border
            :data="tableData"
            fit
            highlight-current-row
            style="width: 100%; margin-bottom: 25px"
            @selection-change="handleSelectionChange"
          >
            <!-- <el-table-column type="selection" /> -->
            <el-table-column
              align="center"
              label="序号"
              prop="id"
              min-width="25%"
            >
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="要素名称"
              prop="elementname"
              min-width="50%"
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
              min-width="50%"
            >
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
              label="审查要点"
              min-width="200%"
              prop="auditpoint"
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
              label="评分规则"
              prop="assessrules"
              min-width="50%"
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
              label="标准分"
              prop="standardscore"
              min-width="50%"
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
              fixed="right"
              label="操作"
              min-width="50%"
              v-if="modalType !== 'view'"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-col>

      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <!-- <el-button type="primary" @click="save">确 定</el-button> -->
      </template>
    </el-dialog>
    <choose-factor ref="edit" @getData="getData" @fetchData="fetchData" />
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  import { detTempList, yaosudelete } from '@/api/internal/evaluationTemplate'
  import ChooseFactor from './ChooseFactor.vue'
  export default {
    name: 'PlanEdit',
    components: { ChooseFactor },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        tmplId: '',
        catId: '',
        title: '',
        delId: [],
        dialogFormVisible: false,
        tableData: [],
        modalType: null,
        formData: {
          field101: undefined,
          field102: undefined,
          field103: undefined,
          field104: undefined,
          // tableData: [
          //   { name: 'xxx' },
          //   { name: 'xxx' },
          //   { name: 'xxx' },
          //   { name: 'xxx' },
          // ],
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入缺陷级别',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请选择缺陷状态',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请输入缺陷定义',
              trigger: 'change',
            },
          ],
          field104: [
            {
              required: true,
              message: '请输入定量标准',
              trigger: 'change',
            },
          ],
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      save() {
        let idList = []
        const params = {
          nodeld: this.catId,
          tmplId: this.tmplId,
          id: '',
        }
        this.tableData.forEach((res) => {
          idList.push(res.asseleid)
        })
        params.id = idList.toString()

        // yaosusave()
      },
      showEdit(row, type) {
        //
        this.modalType = type
        if (!row) {
          this.title = '添加'
          const autoNew = {
            tblName: 'TBL_ASSESSTEMPLE',
            column: 'TEMPLENUMBER',
            orgCol: 'ORGID',
            noId: 315,
          }
          // createProjectCode(autoNew).then((res) => {
          //
          //   this.formData.field101 = res.data
          // })
        } else {
          this.tmplId = row.asstemid
          this.catId = row.asscatid
          this.delId = []
          const cat = {
            nodeId: this.catId,
            tmplId: this.tmplId,
          }
          const tableList = []
          //先清空，再获取
          this.tableData = tableList
          this.fetchData()
          this.title = '查看要素'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      // handleDelete(row) {
      // },
      handleAdd() {
        //formData的表格新增一行
        this.formData.tableData.push({
          riskSn: '',
          // periodId: '',
          // performanceTarget: '',
          // mzyj: '',
          // performanceDeductRatio: '',
          // outpatientPerformance: '',
          // outpatientProfit: '',
          // show: true,
        })
      },
      handleDelete(row) {
        if (row) {
          // elementGategoryIds = row.asseleid
          this.$baseConfirm('你确定要删除当前项吗', null, async () => {
            const { msg } = await yaosudelete({
              elementGategoryIds: row.elementcategoryid,
            })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          })
        } else {
          if (this.delId.length > 0) {
            this.$baseConfirm('你确定要删除当前项吗', null, async () => {
              const { msg } = await yaosudelete({
                elementGategoryIds: this.delId.toString(),
              })
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            })
          }
        }
        // yaosudelete()
        // this.tableData.splice(row, 1)
      },
      handleEdit(tmplId, catId) {
        this.$refs['edit'].showEdit(tmplId, catId)
      },
      fetchData() {
        const cat = {
          nodeId: this.catId,
          tmplId: this.tmplId,
        }
        const tableList = []
        //先清空，再获取
        this.tableData = tableList
        detTempList(cat).then((res) => {
          if (res.code == 1) {
            if (res.data.assesscategory && res.data.assesscategory != '') {
              res.data.assesscategory.forEach((res) => {
                const item = res.assesselement
                item.standardscore = res.standardscore
                item.elementcategoryid = res.elementcategoryid

                tableList.push(item)
              })
              this.tableData = tableList
            }
          }
        })
      },
      getData(val) {
        //
        val.forEach((res) => {
          this.tableData.push(res)
        })

        //
      },
      handleSelectionChange(val) {
        this.delId = []
        val.forEach((res) => {
          this.delId.push(res.elementcategoryid)
        })
      },
    },
  }
</script>
<style></style>
