<template>
  <el-dialog
    title="计划项目"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
  >
    <el-row>
      <el-col :span="24">
        <el-divider>
          <template v-if="!isGcsjxm">
            <template v-if="isCwsjxm">
              一、专项审计{{ tableData1.length }}项
            </template>
            <template v-else>
              一、专项审计{{ tableData1.length + tableData2.length }}项
            </template>
          </template>
          <template v-else>
            一、专项审计{{ tableData2.length }}项
          </template>
        </el-divider>
        <div class="table-title" v-show="!isGcsjxm">
          <span>（一）生产经营管理专项审计{{ tableData1.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData1', 'table1')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable11"
          :data="tableData1"
          @current-change="handleSelected11"
          highlight-current-row
          v-show="!isGcsjxm"
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLxjyzypgEdit(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="被审计单位" prop="unitRange" />
          <el-table-column align="center" label="审计范围" prop="auditScope" />
          <el-table-column align="center" label="备注" prop="remark" />
          <el-table-column align="center" label="项目类型" prop="projectType" />

          <!-- <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
             <el-button
                type="text"
                @click="handleEdit('tableData1', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button>  
              <el-button
                type="text"
                @click="handleEditDelete('tableData1', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
        <div class="table-title" v-show="!isCwsjxm">
          <span>{{isGcsjxm?'（一）': '（二）'}}基建与投资专项审计{{ tableData2.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData2', 'table1')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable12"
          :data="tableData2"
          @current-change="handleSelected12"
          highlight-current-row
           v-show="!isCwsjxm"
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLxjyzypgEdit(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="被审计单位" prop="unitRange" />
          <el-table-column align="center" label="审计范围" prop="auditScope" />
          <el-table-column align="center" label="备注" prop="remark" />
          <el-table-column align="center" label="项目类型" prop="projectType" />

          <!--    <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
       <template #default="{ row, $index }">
          <el-button
                type="text"
                @click="handleEdit('tableData2', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button>  
              <el-button
                type="text"
                @click="handleEditDelete('tableData2', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-col>
      <el-col :span="24" style="margin-top: 20px" v-show="!isGcsjxm">
        <el-divider>
          二、经济责任审计{{
            tableData3.length + tableData4.length + tableData5.length
          }}项
        </el-divider>
        <div class="table-title">
          <span>
            （一）二级单位及所属成员单位离任经济责任审计{{
              tableData3.length
            }}项
          </span>
          <!-- <el-button type="success" @click="handleAdd('tableData3', 'table21')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable21"
          :data="tableData3"
          @current-change="handleSelected21"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button type="text" @click="handleDetailLRJYJLRSJview(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="auditOrgId"
            label="被审计单位"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.auditOrg.orgname }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="审计范围" prop="auditScope" />
          <el-table-column align="center" label="委托时间" prop="entrustTime" />
          <el-table-column align="center" label="委托书编号" prop="entrustNo" />
          <el-table-column align="center" label="备注" prop="remarks" />
          <el-table-column align="center" label="项目类型" prop="projectType" />
          <!--  <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
           <el-button
                type="text"
                @click="handleEdit('tableData3', row, $index, 'table21')"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData3', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
        <div class="table-title">
          <span>（二）二级单位任中经济责任审计{{ tableData4.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData4', 'table22')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable22"
          :data="tableData4"
          @current-change="handleSelected22"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="项目名称" prop="projectName">
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleDetailRzsjmxView(row)"
                style="white-space: pre-line; line-height: 16px"
              >
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="orgId"
            label="被审计单位"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.org && row.org.orgname }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="审计范围" prop="auditScope" />
          <el-table-column align="center" label="委托时间" prop="entrustTime" />
          <el-table-column align="center" label="备注" prop="remarks" />
          <el-table-column align="center" label="项目类型" prop="projectType" />
          <!--    <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
            <el-button
                type="text"
                @click="handleEdit('tableData4', row, $index, 'table22')"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData4', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
        <div class="table-title">
          <span>（三）三级单位离任经济责任审计{{ tableData5.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData5', 'table23')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable23"
          :data="tableData5"
          @current-change="handleSelected23"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleSJDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <!--     <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
         <el-button
                type="text"
                @click="handleEdit('tableData5', row, $index, 'table23')"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData5', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-col>
      <el-col :span="24" style="margin-top: 20px" v-show="!isCwsjxm">
        <el-divider>
          {{isGcsjxm?'二': '三'}}、工程建设项目审计{{ tableData6.length + tableData7.length }}项
        </el-divider>
        <div class="table-title">
          <span>（一）工程建设项目结算审计{{ tableData6.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData6', 'table31')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable31"
          :data="tableData6"
          @current-change="handleSelected31"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleGCJSDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            label="项目金额"
            prop="projectAmount"
          ></el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <!--   <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
             <el-button
                type="text"
                @click="handleEdit('tableData6', row, $index, 'table31')"
                :disabled="formDisabled"
              >
                编辑
              </el-button> 
              <el-button
                type="text"
                @click="handleEditDelete('tableData6', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column>-->
        </el-table>
        <div class="table-title">
          <span>
            （二）选择建设项目投资基本情况表内容{{ tableData7.length }}项
          </span>
          <!-- <el-button type="success" @click="handleAdd('tableData7', 'table32')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable32"
          :data="tableData7"
          @current-change="handleSelected32"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            label="项目名称"
            width="120"
            prop="projectName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="relaOrgName"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="projectCount"
            label="单位数量"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleJSTZDetail(row)">
                {{ row.projectCount }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            label="项目金额"
            prop="projectAmount"
          ></el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
            show-overflow-tooltip
          ></el-table-column>
          <!--    <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
          <el-button
                type="text"
                @click="handleEdit('tableData7', row, $index, 'table32')"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData7', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-col>

      <el-col :span="24" style="margin-top: 20px" v-show="!isGcsjxm">
        <el-divider>{{isCwsjxm?'三': '四'}} 、绩效考核审计{{ tableData8.length }}项</el-divider>
        <div class="table-title">
          <span>（一）其他审计{{ tableData8.length }}项</span>
          <!-- <el-button type="success" @click="handleAdd('tableData8')" v-if="!formDisabled">
            新增
          </el-button> -->
        </div>
        <el-table
          ref="multipleTable41"
          :data="tableData8"
          @current-change="handleSelected41"
          highlight-current-row
        >
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="auditItemName"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.auditItemName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="auditOrgNameStrs"
            label="被审计单位"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="实施类型"
            width="120"
            prop="implType"
          ></el-table-column>
          <el-table-column
            label="审计项目类型"
            width="120"
            prop="auditItemType"
          ></el-table-column>
          <el-table-column
            prop="projectInitUnitName"
            label="立项单位"
            show-overflow-tooltip
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row, $index }">
              <el-button
                type="text"
                @click="handleEdit('tableData8', row, $index)"
                :disabled="formDisabled"
              >
                编辑
              </el-button>
              <el-button
                type="text"
                @click="handleEditDelete('tableData8', $index)"
                :disabled="formDisabled"
              >
                删除
              </el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-col>
    </el-row>
    <table8 ref="table8" @fetch="table8Fetch" />
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <lxjyzypgEdit ref="lxjyzypgEdit" />
    <LRJYJLRSJview ref="LRJYJLRSJview"></LRJYJLRSJview>
    <rzsjmxView ref="rzsjmxView"></rzsjmxView>
    <SJmodal ref="SJmodal" @fetchData="table5Change"></SJmodal>
    <GCJSmodal ref="GCJSmodal"></GCJSmodal>
    <JSTZmodal ref="JSTZmodal"></JSTZmodal>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { jhgljhDetail } from '@/api/monitor/question'
  import table8 from '@/views/oilAudit/jhlx/components/table/table8'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit.vue'
  import LRJYJLRSJview from '@/views/oilAudit/lrjjzr/components/lrjyjlrsjView.vue'
  import rzsjmxView from '@/views/oilAudit/lrjjzr/components/rzsjmxView.vue'
  import SJmodal from '@/views/oilAudit/jhlx/components/zgModal/CWSJModal.vue'  //复制的SJModal-财务审计专用
  import GCJSmodal from '@/views/oilAudit/jhlx/components/zgModal/GCJSModal.vue'
  import JSTZmodal from '@/views/oilAudit/jhlx/components/zgModal/JSTZModal.vue'

  export default {
    name: 'planNameModal',
    components: {
      table8,
      lxjyzypgEdit,
      LRJYJLRSJview,
      rzsjmxView,
      SJmodal,
      GCJSmodal,
      JSTZmodal,
    },
    props: {
      // 财务审计项目安排-编辑专用
      isCwsjxm: {
        type: Boolean,
        default: false,
      },
      // 工程审计项目安排-编辑专用
      isGcsjxm: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        tableData1: [],
        tableData12: [],
        tableData2: [],
        tableData22: [],
        tableData3: [],
        tableData32: [],
        tableData4: [],
        tableData42: [],
        tableData5: [],
        tableData52: [],
        tableData6: [],
        tableData62: [],
        tableData7: [],
        tableData72: [],
        tableData8: [],
        type: '',
        table5: null
      }
    },
    mounted() {
      console.log('plan组件加载成功')
    },
    methods: {
      table8Fetch(row) {
        this.tableData8.push(row)
      },
      table5Change(ids) {
        this.table5.projectCount = ids.length
        this.table5.ids = ids.join(',')
        if(this.type == '23') {
          this.current.ids = this.table5.ids
          this.current.projectCount = this.table5.projectCount
        }
        console.log(this.current)
      },
      handleSJDetail(row) {
        this.table5 = row
        this.$refs['SJmodal'].showEdit(row)
      },
      handleGCJSDetail(row) {
        this.$refs['GCJSmodal'].showEdit(row)
      },
      handleJSTZDetail(row) {
        this.$refs['JSTZmodal'].showEdit(row)
      },
      //二.2
      handleDetailRzsjmxView(row) {
        this.$refs['rzsjmxView'].showEdit({ id: row.id }, '详情')
      },
      // 二.1
      handleDetailLRJYJLRSJview(row) {
        this.$refs['LRJYJLRSJview'].showEdit(row, true)
      },
      //一.1&2
      handleDetailLxjyzypgEdit(row) {
        this.$refs['lxjyzypgEdit'].showEdit('detail', row)
      },
      /**
       * @description  初始化
       * @param {*}
       * @return {*}
       */
      async showEdit(row) {
        console.log(row)
        this.current = null
        this.type = ''
        this.dialogVisible = true
        if (row) {
          const res = await jhgljhDetail({ jhid: row.jhcgid })
          this.formData = res.data
          this.tableData1 = res.data.mx11 || []
          this.tableData12 = JSON.parse(JSON.stringify(res.data.mx11 || []))
          this.tableData2 = res.data.mx12 || []
          this.tableData22 = JSON.parse(JSON.stringify(res.data.mx12 || []))
          this.tableData3 = res.data.leaveAudit2LEntityList || []
          this.tableData32 = JSON.parse(
            JSON.stringify(res.data.leaveAudit2LEntityList || [])
          )
          this.tableData4 = res.data.auditSuggestion2LEntityList || []
          this.tableData42 = JSON.parse(
            JSON.stringify(res.data.auditSuggestion2LEntityList || [])
          )

          this.tableData5 = res.data.leaveAudit3LEntityList || []
          this.tableData52 = JSON.parse(
            JSON.stringify(res.data.leaveAudit3LEntityList || [])
          )
          this.tableData6 = res.data.tblYqnsJsxmJbqkList || []
          this.tableData62 = JSON.parse(
            JSON.stringify(res.data.tblYqnsJsxmJbqkList || [])
          )
          this.tableData7 = res.data.tblYqnsGcxmjgYsjhList || []
          this.tableData72 = JSON.parse(
            JSON.stringify(res.data.tblYqnsGcxmjgYsjhList || [])
          )
          this.tableData8 = res.data.oauList || []
          this.tableData82 = JSON.parse(JSON.stringify(res.data.oauList || []))
        }
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      handleSelected11(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.unitRange,
          unitRangeId: val.unitRangeId,
        }
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '11'
      },
      handleSelected12(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.unitRange,
          unitRangeId: val.unitRangeId,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '12'
      },
      handleSelected21(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.auditOrg.orgname,
          unitRangeId: val.auditOrg.orgid,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '21'
      },
      handleSelected22(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.org ? val.org.orgname : '',
          unitRangeId: val.org ? val.org.orgid : '',
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '22'
      },
      handleSelected23(val) {
        this.current = {
          ...val,
          id: val.id,
          ids: val.ids || '',
          name: val.projectName,
          unitRange: val.relaOrgName,
          unitRangeId: val.relaOrgId,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '23'
      },
      handleSelected31(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.relaOrgName,
          unitRangeId: val.relaOrgId,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '31'
      },
      handleSelected32(val) {
        this.current = {
          ...val,
          id: val.id,
          name: val.projectName,
          unitRange: val.relaOrgName,
          unitRangeId: val.relaOrgId,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable41.setCurrentRow()
        this.type = '32'
      },
      handleSelected41(val) {
        this.current = {
          ...val,
          id: val.auditId,
          name: val.auditItemName,
          unitRange: val.auditOrgNameStrs,
          unitRangeId: val.auditOrgidStrs,
        }
        this.$refs.multipleTable11.setCurrentRow()
        this.$refs.multipleTable12.setCurrentRow()
        this.$refs.multipleTable21.setCurrentRow()
        this.$refs.multipleTable22.setCurrentRow()
        this.$refs.multipleTable23.setCurrentRow()
        this.$refs.multipleTable31.setCurrentRow()
        this.$refs.multipleTable32.setCurrentRow()
        this.type = '41'
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (!this.current) {
          this.$message({
            type: 'error',
            message: '请选择!',
          })
          return
        }
        console.log(this.current, this.type)
        this.$emit('selected', this.current, this.type)
        this.dialogVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleDetail(row) {
        this.$refs['table8'].showEdit('detail', row)
      },
    },
  }
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .table-title {
    padding: 10px;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
