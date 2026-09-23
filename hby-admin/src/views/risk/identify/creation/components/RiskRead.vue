<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-dialog
      :close-on-click-modal="false"
      width="50%"
      title="详情"
      :visible.sync="innerVisible"
      append-to-body
    >
      <p v-html="details"></p>
    </el-dialog>
    <el-row :gutter="15">
      <el-form ref="elForm" label-width="125px" :model="formData" size="medium">
        <el-col :span="24">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="基本信息" name="1">
              <el-col :span="24">
                <el-divider>业务单元</el-divider>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务编号" prop="flownumber">
                  <el-input v-model="formData.flownumber" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="流程名称" prop="flowname">
                  <el-input v-model="formData.flowname" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="责任部门" prop="belongstoName">
                  <el-input
                    v-model="formData.belongstoName"
                    disabled
                  ></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="相关部门" prop="reorgName">
                  <el-input disabled v-model="formData.reorgName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务名称" prop="bussinessname">
                  <el-input
                    v-model="formData.bussinessname"
                    disabled
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="业务描述" prop="bussinessdes">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.bussinessdes"
                    disabled
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-divider>风险描述</el-divider>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险编号" prop="risknumber">
                  <el-input v-model="formData.risknumber" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险名称" prop="riskname">
                  <el-input v-model="formData.riskname" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="版本" prop="version">
                  <el-input v-model="formData.version" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险领域" prop="riskcatname">
                  <el-select
                    v-model="formData.riskcatname"
                    clearable
                    multiple
                    disabled
                    placeholder="请输入控制类型"
                    :style="{ width: '100%' }"
                  >
                    <el-option
                      v-for="item in riskcatnameOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  class="form-inlink"
                  label="风险来源"
                  prop="risklevel"
                >
                  <el-select
                    v-model="formData.risklevel"
                    clearable
                    disabled
                    placeholder="请选择"
                    :style="{ width: '100%' }"
                  >
                    <el-option
                      v-for="item in risklevelOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="风险描述" prop="riskdes">
                  <el-input
                    type="textarea"
                    v-model="formData.riskdes"
                    disabled
                    :rows="4"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-form-item label="风险原因" prop="riskcause">
                <el-input
                  type="textarea"
                  disabled
                  v-model="formData.riskcause"
                  :rows="4"
                ></el-input>
              </el-form-item>
              <el-col :span="24">
                <el-divider>合规要求</el-divider>
              </el-col>
              <el-col :span="24">
                <el-form-item label="外部规定" prop="riskexternal">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.riskexternal"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="公司规定" prop="riskcompany">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.riskcompany"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="合规红线" prop="riskcompliance">
                  <el-input
                    type="textarea"
                    :rows="4"
                    v-model="formData.riskcompliance"
                    disabled
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-form-item label="合规义务" prop="complianceobligation">
                <el-input
                  type="textarea"
                  disabled
                  v-model="formData.complianceobligation"
                  :rows="4"
                ></el-input>
              </el-form-item>

              <el-col :span="24">
                <UEditor
                  ref="ueditor"
                  v-model="formData.content"
                  :height="300"
                  :disabeld="true"
                  template="nbsj"
                />
              </el-col>
              <el-col :span="24">
                <el-divider>附件</el-divider>
              </el-col>
              <el-col :span="24">
                <!-- <div style="text-align: right; margin-bottom: 5px">
                  <el-upload
                    style="text-align: right; margin-bottom: 5px"
                    class="upload-demo"
                    :action="baseApi + api"
                    :data="{
                      moduleType: 'PGJH',
                      moduleId: this.formData.id,
                    }"
                    :headers="headers"
                    :on-success="handleSuccess"
                    :show-file-list="false"
                    multiple
                    :file-list="fileList"
                  >
                    <div style="margin-right: 10px">
                      <el-button type="success">上传</el-button>
                    </div>
                  </el-upload>
                </div> -->

                <el-table :data="tableDataFile">
                  <el-table-column
                    align="center"
                    label="附件名称"
                    prop="attname"
                  />
                  <el-table-column
                    align="center"
                    label="文件大小(KB)"
                    prop="attsize"
                  />
                  <el-table-column
                    align="center"
                    label="创建人"
                    prop="uploader"
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
                  >
                    <template #default="{ row }">
                      <el-button type="text" @click="handleDownload(row)">
                        下载
                      </el-button>
                      <el-button type="text" @click="handlePreviewFile(row)">
                        预览
                      </el-button>
                      <!-- <el-button type="text" @click="handleDeleteAttach(row)">
                        删除
                      </el-button> -->
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
            </el-tab-pane>
            <!-- <el-tab-pane label="流程图" name="second">
              <workflow />
            </el-tab-pane> -->
            <!-- <el-tab-pane label="流程描述" name="third">
              <el-table :data="list0">
                <el-table-column align="center" type="selection" width="55" />
                <el-table-column
                  align="center"
                  label="制度名称"
                  prop="data"
                  width="100"
                />
                <el-table-column
                  align="center"
                  label="发文文号"
                  prop="data"
                  width="100"
                />
                <el-table-column
                  align="center"
                  label="发文机构"
                  prop="data"
                  width="100"
                />
                <el-table-column
                  align="center"
                  label="发文日期"
                  prop="data"
                  width="100"
                />
                <el-table-column align="center" label="操作">
                  <template #default="{ row }">
                    <el-button type="text" @click="handleEdit2(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane> -->
            <!-- <el-tab-pane label="内外规" name="2">
              <el-col :span="24">
                <el-divider>内规</el-divider>
                <el-table :data="pageBean">
                  <el-table-column
                    align="center"
                    label="制度名称"
                    prop="rulename"
                  >
                    <template #default="{ row }">
                      <el-button type="text" @click="handleView(row)">
                        {{ row.rulename }}
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="发文文号"
                    prop="rulenumber"
                  />
                  <el-table-column
                    align="center"
                    label="发文机构"
                    prop="publishorgname"
                  />
                  <el-table-column
                    align="center"
                    label="发文日期"
                    prop="publishdate"
                  />
                </el-table>
                <el-pagination
                  background
                  :current-page="queryForm1.pageNo"
                  :layout="layout"
                  :page-size="queryForm1.pageSize"
                  :total="total1"
                  @current-change="handleCurrentChange"
                  @size-change="handleSizeChange"
                />
              </el-col>
              <el-col :span="24">
                <el-divider>外规</el-divider>
                <el-table :data="pageBeanTWO">
                  <el-table-column
                    align="center"
                    label="制度名称"
                    prop="rulename"
                  >
                    <template #default="{ row }">
                      <el-button type="text" @click="handleView(row)">
                        {{ row.rulename }}
                      </el-button>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="发文文号"
                    prop="rulenumber"
                  />
                  <el-table-column
                    align="center"
                    label="发文机构"
                    prop="orgname"
                  />
                  <el-table-column
                    align="center"
                    label="发文日期"
                    prop="publishdate"
                  />
                </el-table>
                <el-pagination
                  background
                  :current-page="queryForm2.pageNo"
                  :layout="layout"
                  :page-size="queryForm2.pageSize"
                  :total="total2"
                  @current-change="handleCurrentChange"
                  @size-change="handleSizeChange"
                />
              </el-col>
            </el-tab-pane> -->
            <!-- <el-tab-pane label="工作流编辑" name="fifth">
              <BpmnModeler />
            </el-tab-pane> -->
            <!-- <el-tab-pane label="自定义表单" name="sixth">
              <CustomList />
            </el-tab-pane> -->
          </el-tabs>
        </el-col>
      </el-form>
    </el-row>

    <!-- <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'
  import { downFieldById } from '@/api/risk/riskEvents'
  import BpmnModeler from '@/components/bpmnjs/BpmnModeler'
  import LogicFlow from '@logicflow/core'
  import { Menu, Snapshot } from '@logicflow/extension'
  import '@logicflow/core/dist/style/index.css'
  import '@logicflow/extension/lib/style/index.css'
  import { getList } from '@/api/workflow'
  import workflow from '../workflow'
  import CustomList from './CustomList.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { zgjkLeft } from '@/api/setting/org'
  import { formatOptions } from '@/utils/validate'
  import { riskAnalysisDetail } from '@/api/risk'
  import { dispaddanalysis, innerrule, outerrule } from '@/api/systemLog'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')

  // import { firstRisks, secondRisks, thirdRisks } from '../riskOptions'

  export default {
    name: 'ProjectEdit',
    components: { BpmnModeler, workflow, CustomList, UEditor },
    data() {
      return {
        baseApi: baseURL,
        // api: '/audit/fileManage/upload',
        api: '/riskcontrol/attachment/uploadFileAttInfo',
        headers: {
          token: store.getters['user/token'],
        },
        title: '',
        dialogFormVisible: false,
        activeName: '1',
        innerVisible: false,
        details: '',
        list0: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total1: 0,
        total2: 0,
        pageBean: [],
        pageBeanTWO: [],
        queryForm1: {
          isFlowdb: '1',
          pageNo: 1,
          pageSize: 20,
        },
        queryForm2: {
          isFlowdb: '1',
          pageNo: 1,
          pageSize: 20,
        },
        formData: {
          flowname: '',
          flownumber: '',
          bussinessname: '',
          bussinessdes: '',
          risknumber: '',
          riskname: '',
          version: '',
          riskdes: '',
          controlnumber: '',
          controltype: '',
          keycontrol: '',
          effective: '',
          controltest: '',
          financialreportidentify: '',
          controlfrequency: '',
          controlmethod: '',
          controldes: '',
          conkzcs: '',
          riskprogram: '',
          riskexternal: '',
          riskcompany: '',
          riskcompliance: '',
          belongstoText: '',
          reorgText: '',
          toplevelflowcat: '',
          controlmanager: '',
          riskcatname: '',
          risklevel: '',
          content: '',
        },
        toplevelflowcatOptions: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field104Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field113Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        riskcatnameOptions: [
          {
            label: '战略风险',
            value: '1',
          },
          {
            label: '财务风险',
            value: '2',
          },
          {
            label: '市场风险',
            value: '3',
          },
          {
            label: '运营风险',
            value: '4',
          },
          {
            label: '法律合规风险',
            value: '5',
          },
          {
            label: '核安全环保风险',
            value: '6',
          },
          {
            label: '工程建设风险',
            value: '7',
          },
        ],
        risklevelOptions: [
          {
            label: '内部风险',
            value: '1',
          },
          {
            label: '外部风险',
            value: '2',
          },
        ],
        controltypeOptions: [
          {
            label: '预防性控制',
            value: '1',
          },
          {
            label: '发现性控制',
            value: '2',
          },
          {
            label: '纠正性控制',
            value: '3',
          },
        ],
        controlmethodOptions: [
          {
            label: '手工',
            value: '1',
          },
          {
            label: '自动',
            value: '2',
          },
          {
            label: '依赖手工的自动化',
            value: '3',
          },
        ],
        financialreportidentifyOptions: [
          {
            label: '存在与发生',
            value: '1',
          },
          {
            label: '完整性',
            value: '2',
          },
          {
            label: '权利与义务',
            value: '3',
          },
          {
            label: '估计与平摊',
            value: '4',
          },
          {
            label: '表达与披露',
            value: '5',
          },
        ],
        belongstoTextOptions: [],
        tableDataFile: [],
        fileIdList: [],
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
      }
    },
    created() {
      // this.fetchData()
      this.getBelongstoTextOptions()
    },
    methods: {
      handleShowUser() {
        this.$refs['userTreeRef'].show()
      },
      handleView(row) {
        this.details = row.bodyinfo
        this.innerVisible = true
      },
      async fetchData() {
        const { data } = await getList()
        this.data = data
        this.$nextTick(() => {
          this.$_initLf()
        })
      },
      $_initLf() {
        const _this = this
        // 画布配置
        const config = {
          container: document.querySelector('#left-view'),
          background: {
            color: '#f7f9ff',
          },
          grid: {
            size: 10,
            visible: false,
          },
          keyboard: {
            enabled: true,
          },
          edgeTextDraggable: true,
          guards: {
            beforeClone(data) {
              console.log('beforeClone', data)
              return true
            },
            beforeDelete(data) {
              // 可以根据data数据判断是否允许删除，允许返回true,不允许返回false
              // 文档： http://logic-flow.org/guide/basic/keyboard.html#%E5%A6%82%E4%BD%95%E9%98%BB%E6%AD%A2%E5%88%A0%E9%99%A4%E6%88%96%E8%80%85%E6%8B%B7%E8%B4%9D%E8%A1%8C%E4%B8%BA
              console.log('beforeDelete', data)
              // _this.$message('不允许删除', 'error')
              return true
            },
          },
        }
        // 使用插件
        LogicFlow.use(Menu)
        LogicFlow.use(Snapshot)
        const lf = new LogicFlow({ ...config })
        this.lf = lf
        // 菜单配置文档：http://logic-flow.org/guide/extension/extension-components.html#%E8%8F%9C%E5%8D%95
        // 重置，增加，节点自由配置(以user节点为示例)
        lf.setMenuConfig({
          nodeMenu: [],
          edgeMenu: [],
        })
        lf.addMenuConfig({
          nodeMenu: [
            {
              text: '分享',
              callback() {
                _this.$baseAlert('分享成功！')
              },
            },
            {
              text: '属性',
              callback(node) {
                _this.$baseAlert(`
                节点id：${node.id}
                节点类型：${node.type}
                节点坐标：(x: ${node.x}, y: ${node.y})`)
              },
            },
          ],
          edgeMenu: [
            {
              text: '属性',
              callback(edge) {
                _this.$baseAlert(`
                边id：${edge.id}
                边类型：${edge.type}
                边坐标：(x: ${edge.x}, y: ${edge.y})
                源节点id：${edge.sourceNodeId}
                目标节点id：${edge.targetNodeId}`)
              },
            },
          ],
        })
        // 设置主题
        lf.setTheme({
          circle: {
            r: 20,
            stroke: '#000000',
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          rect: {
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          polygon: {
            strokeWidth: 1,
          },
          polyline: {
            stroke: '#000000',
            hoverStroke: '#000000',
            selectedStroke: '#000000',
            outlineColor: '#88f',
            strokeWidth: 1,
          },
          nodeText: {
            color: '#000000',
          },
          edgeText: {
            color: '#000000',
            background: {
              fill: '#f7f9ff',
            },
          },
        })
        this.$_registerNode()
      },
      handleSizeChange(val) {
        this.queryformData.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryformData.pageNo = val
        this.fetchData()
      },
      handleSuccess(e) {
        console.log(e)
        if (e.code === 200) {
          // this.showEdit(this.showRow)
          let attInfo = {}
          attInfo = e.data
          this.fileIdList.push(attInfo.attid)
          this.tableDataFile.push(attInfo)
          console.log(this.tableDataFile)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
      },
      handleClick(v) {
        this.getData()
      },
      async showRead(row, riskcatid) {
        this.dialogFormVisible = true
        const { data } = await riskAnalysisDetail({
          riskcatid: riskcatid,
          riskid: row.riskid,
        })
        console.log(data, '详情====')
        this.tableDataFile = data.attachmentList || []

        this.formData = {
          risklevel: data.risk.risklevel || undefined,
          riskcatname:
            data.risk &&
            data.risk.riskcatname &&
            data.risk.riskcatname.split(','),
          riskcatid: data.risk.riskcatid || undefined,
          riskid: data.risk.riskid || undefined,
          flownumber: (data.flow && data.flow.flownumber) || undefined,
          complianceobligation: data.risk.complianceobligation || undefined,
          riskcause: data.risk.riskcause || undefined,
          flowname: (data.flow && data.flow.flowname) || undefined,
          belongstoName: data.risk.zrbmName || undefined,
          reorgName: data.risk.xgbmName || undefined,
          reorg: data.risk.reorg || undefined,
          belongsto: data.risk.belongsto || undefined,
          bussinessname: data.riskBussiness && data.riskBussiness.bussinessname,
          bussinessdes: data.riskBussiness && data.riskBussiness.bussinessdes,
          risknumber: data.risk.risknumber || undefined,
          riskname: data.risk.riskname || undefined,
          version: data.risk.version || undefined,
          riskdes: data.risk.riskdes || undefined,

          toplevelflowcat: '',

          content: data.risk.content || undefined,
          riskprogram: data.risk.riskprogram || undefined,
          riskexternal: data.risk.riskexternal || undefined,
          riskcompany: data.risk.riskcompany || undefined,
          riskcompliance: data.risk.riskcompliance || undefined,
        }
      },

      close() {
        // this.form = this.$options.data().form
        this.dialogFormVisible = false
        this.$refs['elForm'].resetFields()
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.formData)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用？
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      async handleDownload(row) {
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          // console.log(this.tableDataFile, 'this.fileIdList')
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      async getBelongstoTextOptions() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
      },
    },
  }
</script>
<style scoped></style>
