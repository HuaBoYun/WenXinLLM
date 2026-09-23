<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      append-to-body
    >
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button
            type="success"
            @click="handleAddTPL"
            v-if="modalType !== 'view'"
          >
            新增
          </el-button>
        </div>
        <el-table
          :data="list"
          fit
          highlight-current-row
          style="width: 100%; margin-bottom: 25px"
        >
          <el-table-column align="center" label="编号">
            <template #default="{ row }">
              <el-button type="text" @click="handleShowTPL(row)">
                {{ row.elementcode }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="业务描述"
            prop="businessdesc"
          ></el-table-column>
          <el-table-column
            align="center"
            label="风险描述"
            prop="risktype"
          ></el-table-column>
          <el-table-column
            align="center"
            label="控制目标"
            prop="controltarget"
          ></el-table-column>
          <el-table-column
            align="center"
            label="控制措施"
            prop="controlmeasures"
          ></el-table-column>

          <el-table-column
            align="center"
            fixed="right"
            label="操作"
            width="150"
            v-if="modalType !== 'view'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEditTPL(row)">
                修改
              </el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" v-if="modalType !== 'view'">
          保 存
        </el-button>
      </template>
      <TemplateEditStep3 ref="step3" @fetch="fetchData" />
    </el-dialog>
  </div>
</template>
<script>
  import TemplateEditStep3 from './TemplateEditStep3.vue'
  import { elementList, elementremove } from '@/api/internal/testTemplate'
  import { doDelete } from '@/api/table'
  export default {
    name: 'TemplateEditStep2',
    inheritAttrs: false,
    props: [],
    components: { TemplateEditStep3 },
    data() {
      return {
        loading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '新建',
        modalType: 'new',
        dialogFormVisible: false,
        formData: {
          elementcode: undefined,
          controlmethod: '自动',
          controltype: '预防性',
          controlreq: '随时',
          businessdesc: undefined,
          risktype: undefined,
          controltarget: undefined,
          controlmeasures: undefined,
          checkmethod: undefined,
          field110: undefined,
        },
        tableData: [],
        list: [],
        queryForm: {
          templid: '',
          typeid: '',
          businessdesc: '',
          elementcode: '',
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(type) {
        if (type && type === 'reset') {
          // TODO 清空
        }
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await elementList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      save() {
        this.close()
      },
      show(row, testtemid, type) {
        this.dialogFormVisible = true
        if (row) {
          this.title = '编辑'
          this.modalType = type
          this.queryForm.templid = testtemid
          this.queryForm.typeid = row.typeid
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
      },
      handleAddTPL() {
        this.$refs['step3'].show(null, this.queryForm)
      },
      handleEditTPL(row) {
        this.$refs['step3'].show(row, this.queryForm)
      },
      handleShowTPL(row) {
        this.$refs['step3'].show(row, this.queryForm, 'show')
      },
      handleIssued() {},
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await elementremove({
            elementid: row.elementid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.fetchData()
          }
          // await this.fetchData()
        })
      },
    },
  }
</script>
<style></style>
