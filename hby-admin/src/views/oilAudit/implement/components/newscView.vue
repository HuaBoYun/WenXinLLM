<template>
  <el-dialog
    :append-to-body="true"
    title="工程审计程序方法指引一跟踪审计"
    :visible.sync="dialogFormVisible"
    @close="close"
    :close-on-click-modal="false"
    fullscreen
  >
    <!-- width="1400px" -->
    <el-tabs type="border-card" v-model="tabValue" @tab-click="tabClick">
      <el-tab-pane name="sc" label="审查">
        <el-row :gutter="14">
          <el-table
            v-loading="listLoading"
            :data="list"
            border
            :span-method="arraySpanMethod"
            tooltip-effect="dark"
          >
            <el-table-column
              align="center"
              label="序号"
              prop="index"
              width="80"
            />
            <el-table-column align="left" label="名称" width="240">
              <div slot-scope="{ row }" :class="{ bold: row.index }">
                {{ row.name }}
              </div>
            </el-table-column>
            <el-table-column align="center" label="审查资料并存档">
              <div slot-scope="{ row }" v-if="row.sczlbcd">
                <el-button type="text" @click="showUploadDialog(row)">
                  查看
                </el-button>
              </div>
              <div class="bold" v-else>/</div>
            </el-table-column>
            <el-table-column align="center" label="记事本" prop="notepad">
              <div slot-scope="{ row }" v-if="row.notepad !== null">
                {{ row.notepad }}
              </div>
              <div class="bold" v-else>/</div>
            </el-table-column>

            <el-table-column
              align="center"
              label="生成底稿"
              prop="scdg"
              width="140"
            >
              <div slot-scope="{ row }" v-if="row.scdg">
                <el-button type="text" @click="showCkdgDialog(row)">
                  查看底稿
                </el-button>
              </div>
              <div class="bold" v-else>/</div>
            </el-table-column>
            <!-- <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
            >
              <template #default="{ row }">
                <el-button type="text" @click="edit(row)" v-if="row.scdg">
                  编辑
                </el-button> 
                <div class="bold" v-else>/</div>
              </template>
            </el-table-column> -->
            <el-table-column align="center" label="备注" prop="remarks">
              <div slot-scope="{ row }" v-if="row.remarks !== null">
                {{ row.remarks }}
              </div>
              <div class="bold" v-else>/</div>
            </el-table-column>
            <el-table-column align="center" label="督导意见">
              <div slot-scope="{ row }" v-if="row.supervisionOpinions !== null">
                <el-input
                  v-model="row.supervisionOpinions"
                  clearable
                  type="textarea"
                  :rows="1"
                  autosize
                ></el-input>
              </div>
              <div class="bold" v-else>/</div>
            </el-table-column>
            <el-table-column
              align="center"
              label="审计内容和方法"
              prop="methodContent"
              show-overflow-tooltip
            />
          </el-table>
        </el-row>
      </el-tab-pane>
      <el-tab-pane name="sjjgqrd" label="审计结果签认单">
        <Sjjgqrd :row="row" ref="sjjgqrd" />
      </el-tab-pane>
      <el-tab-pane name="sjnr" label="审减内容">
        <el-row :gutter="14">
          <el-card shadow="never" class="secondCard">
            <el-table v-loading="listLoadingSj" :data="listSj">
              <el-table-column align="center" label="工程量计算" prop="gcljs" />
              <el-table-column align="center" label="定额套用" prop="dety" />
              <el-table-column align="center" label="现场实测" prop="xcsc" />
              <el-table-column align="center" label="物资价格" prop="wzjg" />
              <el-table-column align="center" label="其他审减" prop="qtsj" />
              <el-table-column align="center" label="审减额" prop="sje" />
              <el-table-column align="center" label="审计人员" prop="sjry" />
              <el-table-column align="center" label="备注" prop="bz" />
            </el-table>
            <el-pagination
              class="pagination"
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </el-card>
        </el-row>
      </el-tab-pane>
      <el-tab-pane name="xcsczynr" label="现场审查主要内容">
        <Xcsczynr :row="row" ref="xcsczynr" />
      </el-tab-pane>
    </el-tabs>

    <ScUploadView ref="scUploadView" @updateList="updateList"></ScUploadView>
    <ScdgView ref="scdgView"></ScdgView>
    <CkdgView ref="ckdgView"></CkdgView>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="handleSubmit" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
    <sjEdit ref="sjEdit" @queryData="fetchData" />
    <scEditView ref="edit" @close="backSc()" />
  </el-dialog>
</template>

<script>
  import { getSubTemplateNext } from '@/oapi/baseConfig/gcsjmb'
  import { addOrUpdateScList, getList, handleDelete } from '@/oapi/audit/task'
  import ScUploadView from './sc/scUploadView.vue'
  import ScdgView from './sc/newMyDraftView.vue'
  import scEditView from './sc/scEditView.vue'
  import CkdgView from './sc/ckdgView.vue'
  import sjEdit from './sc/sjEdit'
  import Sjjgqrd from './sc/sjjgqrd'
  import Xcsczynr from './sc/xcsczynr'
  export default {
    name: 'newscView',
    components: {
      ScUploadView,
      ScdgView,
      CkdgView,
      sjEdit,
      Sjjgqrd,
      Xcsczynr,
      scEditView,
    },
    data() {
      return {
        id: '',
        list: [],
        listLoading: false,
        dialogFormVisible: false,
        loading: false,
        tabValue: 'sc',
        listLoadingSj: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectId: undefined,
        },
        total: 0,
        listSj: [],
        row: null,
      }
    },
    methods: {
      edit(row) {
        console.log('row', row)
        this.$refs.edit.showEdit({ ...this.row, scId: row.typeNameId })
      },

      tabClick(val) {
        if (val.name == 'sjnr') {
          this.fetchData()
        } else if (val.name == 'sjjgqrd') {
          this.$refs.sjjgqrd.fetchData()
        } else if (val.name == 'xcsczynr') {
          this.$refs.xcsczynr.fetchData()
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handlerDelete(row) {
        // 删除题目
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDelete({ ids: row.sjnrid }).then(() => {
              this.fetchData()
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      async fetchData() {
        this.listLoadingSj = true
        const {
          data: { tlist, totalRecord },
        } = await getList({
          ...this.queryForm,
          projectId: this.row.projectId,
          templateId: this.row.templateId,
        })
        this.listSj = tlist
        this.total = totalRecord
        this.listLoadingSj = false
      },
      handleAddOrUpdate(row) {
        this.$refs['sjEdit'].showModal(row, this.row)
      },
      showEdit(row, type) {
        this.dialogFormVisible = true
        this.tabValue = type || 'sc'
        this.row = row
        this.id = row.projecttempId
        this.queryForm.projectId = row.projectId
        if (type && type == 'sjnr') {
          this.fetchData()
        }
        this.getSubTemplate()
      },
      //获取数据
      async getSubTemplate() {
        this.listLoading = true
        const {
          data: { auditTypeList },
        } = await getSubTemplateNext({
          id: this.id,
          templateId: this.row.templateId,
        })
        console.log(auditTypeList)
        const arr = []
        let methodId = null
        auditTypeList.forEach((item, index) => {
          const auditMethodMaintainEntity =
            item.auditMethodMaintainEntity || null
          let methodContentRowSpan = auditMethodMaintainEntity ? 0 : 1 //默认占1行
          if (
            auditMethodMaintainEntity &&
            auditMethodMaintainEntity.methodId !== methodId
          ) {
            const methodContentItemArr = auditTypeList.filter(
              (el) =>
                auditMethodMaintainEntity.methodId ===
                el.auditMethodMaintainEntity?.methodId
            ) //找出相同methodId的方法类型
            methodContentRowSpan = methodContentItemArr.reduce(
              (prev, cur) => prev + cur.auditTypeNameEntityList.length + 1,
              0
            ) //算出该项需要合并多少行
            methodId = auditMethodMaintainEntity.methodId
          }
          const subNameList = [
            {
              index: this.toChinesNum(index + 1), //序号
              indexRowSpan: item.auditTypeNameEntityList.length + 1, //序号合并多少行
              name: item.typeName, //名称
              gcjssj: '', //工程结算审计
              jgjssj: '', //竣工结算审计
              gzsj: '', //跟踪审计
              methodContent: auditMethodMaintainEntity?.methodContent, //审计内容和方法
              methodContentRowSpan, //审计内容和方法合并多少行
              sczlbcd: false, //审查资料并存档
              scdg: false, //生成底稿
              scdgRowSpan: 1, //生成底稿合并行的数量
              notepad: null, //记事本
              remarks: null, //备注
              supervisionOpinions: null, //督导意见
              attIds: null, //上传弹窗查询列表所传参数
              typeNameId: null, //新增编辑时传的typeNameId
              id: null, //编辑时传的id
              attachments: null, //文件上传列表
              templateId: null, //模板id(生成底稿用)
              typeId: null, //类型id(生成底稿用)
            },
          ]
          item.auditTypeNameEntityList.forEach((el, index) => {
            subNameList.push({
              index: '', //序号
              indexRowSpan: 0, //序号合并行的数量
              name: el.name, //名称
              gcjssj: '√', //工程结算审计
              jgjssj: '√', //竣工结算审计
              gzsj: '√', //跟踪审计
              methodContent: null, //审计内容和方法
              methodContentRowSpan: 0, //审计内容和方法合并行的数量
              sczlbcd: true, //审查资料并存档
              scdg: true, //生成底稿
              scdgRowSpan:
                index === 0 ? item.auditTypeNameEntityList.length : 0, //生成底稿合并行的数量
              notepad: el.tblYqnsMyTaskReviewEntity?.notepad || '', //记事本
              remarks: el.tblYqnsMyTaskReviewEntity?.remarks || '', //备注
              supervisionOpinions:
                el.tblYqnsMyTaskReviewEntity?.supervisionOpinions || '', //督导意见
              attIds: el.tblYqnsMyTaskReviewEntity?.attIds || [], //上传弹窗查询列表所传参数
              typeNameId: el.id, //新增编辑时传的typeNameId
              id: el.tblYqnsMyTaskReviewEntity?.id || '', //编辑时传的id
              attachments: el.tblYqnsMyTaskReviewEntity?.attachments || [], //文件上传列表
              templateId: item.templateId, //模板id(生成底稿用)
              typeId: item.id, //类型id(生成底稿用)
            })
          })
          arr.push(...subNameList)
        })
        this.list = arr
        // console.log("arr:",arr)
        this.listLoading = false
      },
      //更新已上传文件数据
      updateList(data) {
        const currentIndex = this.list.findIndex(
          (item) => item.typeNameId === data.typeNameId && !item.index
        )
        this.$set(this.list, currentIndex, data)
      },
      handleSubmit() {
        if (this.tabValue == 'sc') {
          const arrTemp = this.list.filter((item) => !item.index) //全部有输入框的行
          const entityList = arrTemp.map((item) => ({
            attIds: item.attIds,
            notepad: item.notepad,
            remarks: item.remarks,
            supervisionOpinions: item.supervisionOpinions,
            typeNameId: item.typeNameId,
            id: item.id || undefined,
            templateId: this.row.templateId,
          }))
          this.listLoading = true
          this.loading = true
          addOrUpdateScList(entityList)
            .then(() => {
              this.$baseMessage('保存成功', 'success')
              this.$emit('queryData')
              this.close()
            })
            .catch((res) => {
              this.$baseMessage(res.msg, 'error')
            })
            .finally(() => {
              this.listLoading = false
              this.loading = false
            })
        }
      },
      close() {
        this.dialogFormVisible = false
        this.list = []
        this.id = ''
      },
      //点击上传打开文件上传弹窗
      showUploadDialog(row) {
        console.log('rpw', row)
        this.$refs['scUploadView'].showEdit(row)
      },
      //点击生成底稿
      showScdgDialog(row) {
        this.$refs['scdgView'].showModal(row, true, null, this.row) //从我的任务页面新建底稿
      },
      //点击查看底稿
      showCkdgDialog(row) {
        this.$refs['ckdgView'].showModal(row, this.row.templateId)
      },
      //合并单元格    index-3是因为临时隐藏了三项数据
      arraySpanMethod({ row, columnIndex }) {
        if (columnIndex === 0) {
          if (row.indexRowSpan) {
            return [row.indexRowSpan, 1]
          } else {
            return [0, 0]
          }
        } else if (columnIndex === 10 - 3) {
          if (row.methodContentRowSpan) {
            return [row.methodContentRowSpan, 1]
          } else {
            return [0, 0]
          }
        }
        // else if (columnIndex === 7 - 3) {
        //   if (row.scdg) {
        //     if (row.scdgRowSpan) {
        //       return [row.scdgRowSpan, 1]
        //     } else {
        //       return [0, 0]
        //     }
        //   } else {
        //     return [1, 1]
        //   }
        // }
      },
      //数字转中文
      toChinesNum(num) {
        const changeNum = [
          '零',
          '一',
          '二',
          '三',
          '四',
          '五',
          '六',
          '七',
          '八',
          '九',
        ]
        const unit = ['', '十', '百', '千', '万']
        num = parseInt(num)
        const getWan = (temp) => {
          const strArr = temp.toString().split('').reverse()
          let newNum = ''
          for (let i = 0; i < strArr.length; i++) {
            newNum =
              (i == 0 && strArr[i] == 0
                ? ''
                : i > 0 && strArr[i] == 0 && strArr[i - 1] == 0
                ? ''
                : changeNum[strArr[i]] + (strArr[i] == 0 ? unit[0] : unit[i])) +
              newNum
          }
          return newNum
        }
        const overWan = Math.floor(num / 10000)
        let noWan = num % 10000
        if (noWan.toString().length < 4) {
          noWan = '0' + noWan
        }
        return overWan ? getWan(overWan) + '万' + getWan(noWan) : getWan(num)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .bold {
    font-weight: bold;
  }
</style>
