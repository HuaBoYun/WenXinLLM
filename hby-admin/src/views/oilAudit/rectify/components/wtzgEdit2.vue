<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="200px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="在报告中的对应编号" prop="reportnum">
            <el-input
              v-model="formData.reportnum"
              clearable
              placeholder="请输入在报告中的对应编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题所属单位名称" prop="unitname">
            <el-input
              v-model="formData.unitname"
              clearable
              placeholder="请输入问题所属单位名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="报告中的问题定性"
            prop="wtdx"
            label-width="200px"
          >
            <el-input
              v-model="formData.wtdx"
              clearable
              placeholder="请输入报告中的问题定性"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="事实表述" prop="ssms">
            <el-input
              v-model="formData.ssms"
              clearable
              placeholder="请输入事实表述"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="处理意见或整改建议" prop="clyj">
            <el-input
              v-model="formData.clyj"
              clearable
              placeholder="请输入处理意见或整改建议"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题金额（元）" prop="wtje">
            <el-input
              v-model="formData.wtje"
              clearable
              placeholder="请输入问题金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联底稿" prop="gldg">
            <el-input
              v-model="formData.gldg"
              clearable
              placeholder="请输入关联底稿"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="定性（定性词典）" prop="dx" label-width="200px">
            <el-input
              v-model="formData.dx"
              clearable
              placeholder="请输入定性（定性词典）"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="定性法规依据" prop="dxfgyj">
            <el-input
              v-model="formData.dxfgyj"
              clearable
              placeholder="请输入定性法规依据"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="整改督促牵头部门或单位"
            prop="zgqtbm"
            label-width="180px"
          >
            <el-input
              v-model="formData.zgqtbm"
              clearable
              placeholder="请输入整改督促牵头部门或单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改时限" prop="zgsx">
            <!-- <el-input
              v-model="formData.zgsx"
              clearable
              placeholder="请输入整改时限"
              :style="{ width: '100%' }"
            /> -->
            <el-date-picker
              v-model="formData.zgsx"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择整改时限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="整改分类" prop="zgfl">
            <el-input
              v-model="formData.zgfl"
              clearable
              placeholder="请输入整改分类"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="资产损失（万元）" prop="zcss">
            <el-input
              v-model="formData.zcss"
              clearable
              placeholder="请输入资产损失（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="风险程度" prop="fxcd">
            <el-input
              v-model="formData.fxcd"
              clearable
              placeholder="请输入风险程度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="已填报累计整改金额（元）" prop="ljzgje">
            <el-input
              v-model="formData.ljzgje"
              clearable
              placeholder="请输入已填报累计整改金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="已填报累计经济成果（元）" prop="ljjjcg">
            <el-input
              v-model="formData.ljjjcg"
              clearable
              placeholder="请输入已填报累计经济成果（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="当期整改状态" prop="dqzgzt" label-width="180px">
            <el-select
              v-model="formData.dqzgzt"
              placeholder="请选择当期整改状态"
              :style="{ width: '100%' }"
            >
              <el-option label="整改完毕" value="3" />
              <el-option label="正在整改" value="2" />
              <el-option label="尚未开始整改" value="1" />
              <el-option label="不接受审计意见" value="0" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期整改金额（元）"
            prop="dqzgje"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqzgje"
              clearable
              placeholder="请输入当期整改金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期直接经济成果类型"
            prop="dqzjjjcgtype"
            label-width="180px"
          >
            <el-select
              v-model="formData.dqzjjjcgtype"
              placeholder="请选择当期直接经济成果类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in typeOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期直接经济成果（元）"
            prop="dqzjjjcg"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqzjjjcg"
              clearable
              placeholder="请输入当期直接经济成果（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期其他经济成果类型"
            prop="dqqtjjcgtype"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqqtjjcgtype"
              clearable
              placeholder="请输入当期其他经济成果类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="当期其他经济成果（元）"
            prop="dqqtjjcg"
            label-width="180px"
          >
            <el-input
              v-model="formData.dqqtjjcg"
              clearable
              placeholder="请输入当期其他经济成果（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="当期整改描述" prop="dqzgms">
            <el-input
              v-model="formData.dqzgms"
              clearable
              placeholder="请输入当期整改描述"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否移送" prop="sfys" label-width="180px">
            <el-select
              v-model="formData.sfys"
              placeholder="请选择是否移送"
              :style="{ width: '100%' }"
            >
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <template v-if="this.formData.sfys == 1">
          <el-col :span="24">
            <el-divider>移送内容</el-divider>
          </el-col>

          <el-col :span="12">
            <el-form-item label="移送处理事项" prop="ysclsx">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclsx"
                clearable
                placeholder="请输入移送处理事项"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项"
              prop="sjsfjgyssx"
              label-width="240px"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssx"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理涉及金额（元）" prop="ysclsjje">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclsjje"
                clearable
                placeholder="请输入移送处理涉及金额（元）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理人员（人）" prop="ysclr">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.ysclr"
                clearable
                placeholder="请输入移送处理人员（人） "
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="其中涉及向司法机关移送或报告事项涉及人员（人）"
              prop="sjsfjgyssxr"
              label-width="340px"
            >
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjsfjgyssxr"
                clearable
                placeholder="请输入其中涉及向司法机关移送或报告事项涉及人员（人） "
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="落实移送处理事项" prop="lsysclsx">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.lsysclsx"
                clearable
                placeholder="请输入落实移送处理事项"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移送处理落实情况（人）" prop="yscllsqk">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.yscllsqk"
                clearable
                placeholder="请输入移送处理落实情况（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及党纪处分（人）" prop="sjdjcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjdjcf"
                clearable
                placeholder="请输入其中涉及党纪处分（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及政务处分（人）" prop="sjzwcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjzwcf"
                clearable
                placeholder="请输入其中涉及政务处分（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其中涉及内部纪律处分（人）" prop="sjnbjlcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.sjnbjlcf"
                clearable
                placeholder="请输入其中涉及内部纪律处分（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他经济处分（人）" prop="qtjjcf">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.qtjjcf"
                clearable
                placeholder="请输入其他经济处分（人）"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="cjr">
              <el-input
                v-model="formData.tblYqnsSjzgYsnr.cjr"
                disabled
                placeholder="请输入创建人"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="cjsj">
              <el-date-picker
                v-model="formData.tblYqnsSjzgYsnr.cjsj"
                placeholder="请输入创建时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="24">
          <el-divider>上传审计整改报告及相关附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" v-if="!formDisabled" @click="handleEditTable1(null)">新增</el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column 
            align="center"
            label="文号"
            prop="document"
          ></el-table-column>
          <el-table-column 
            align="center"
            label="标题"
            prop="title"
          ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleEditTable1(row)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handletDeleteTable1(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>审计整改统计信息维护</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" v-if="!formDisabled" @click="handleEditTable2(null)">新增</el-button>
          </div>
          <el-table :data="tableData2">
            <el-table-column align="center" label="序号" type="index" />
            <el-table-column 
            align="center"
            label="制订规章制度数"
            prop="zdgzzds"
          ></el-table-column>
          <el-table-column 
            align="center"
            label="修订规章制度数"
            prop="xggzzds"
          ></el-table-column>

          <el-table-column 
            align="center"
            label="移送处理事项"
            prop="ysclsx"
          ></el-table-column>
          <el-table-column 
            align="center"
            label="其中涉及向司法机关移送或报告事项"
            prop="sjsfjgysbg"
          ></el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="handleEditTable2( row, $index)"
                  :disabled="formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable2(row, $index)"
                  :disabled="formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
            <el-upload
              style="text-align: right; margin-bottom: 5px"
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>

    <scsjbgEdit ref="table1"   />
    <sjzgtjxxEdit ref="table2" />
  </el-dialog>
</template>

<script>
import { editInfo, getDetailInfor, deleteFileInfo } from '@/oapi/yqns_sjzg/wtzg'
import store from '@/store'
const { baseURL } = require('@/config')
import { download } from '@/oapi/audit/report'
import scsjbgEdit from './scsjbgEdit'
import sjzgtjxxEdit from './sjzgtjxxEdit'
import { customUpload } from '@/utils/Uploader'
import { handleDown } from '@/utils/fileHandler'
  export default {
  name: 'wtzgEdit',
  components: {   scsjbgEdit,sjzgtjxxEdit },
  inheritAttrs: false,
  props: [],
  data() {
    return {
      baseApi: baseURL,
      // api: '/oiaudit/fileManage/upload',
      headers: {
        token: store.getters['user/token'],
      },
      fileList: [],
      importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
      api: 'https://www.wenxin.example.com/api/file/file/upload',
      lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
      formData: {
        reportnum: '',
        unitname: '',
        wtdx: '',
        ssms: '',
        clyj: '',
        wtje: '',
        gldg: '',
        dx: '',
        dxfgyj: '',
        zgqtbm: '',
        zgsx: '',
        zgfl: '',
        zcss: '',
        fxcd: '',
        ljzgje: '',
        ljjjcg: '',
        dqzgzt: '',
        dqzgje: '',
        dqzjjjcgtype: '',
        dqzjjjcg: '',
        dqqtjjcgtype: '',
        dqqtjjcg: '',
        dqzgms: '',
        sfys: '',
        tblYqnsSjzgYsnr: {
          sjsfjgyssx: '',
          ysclsx: '',
          ysclsjje: '',
          ysclr: '',
          sjsfjgyssxr: '',
          lsysclsx: '',
          yscllsqk: '',
          sjdjcf: '',
          sjzwcf: '',
          sjnbjlcf: '',
          qtjjcf: '',
          cjr: '',
          cjsj: '',
        },
        wtzgid: '',
        attIds: [],
      },
      tableData1:[],
      tableData2:[],
      tableData3:[],
      formDisabled: true,
      tableData: [],
      rules: {
        sfys: [
          {
            required: true,
            message: '请选择是否移送',
            trigger: 'change',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
      typeOptions: [
        {
          label: '节约各类开支',
          value: '节约各类开支',
        },
        {
          label: '建设工程项目审减',
          value: '建设工程项目审减',
        },
        {
          label: '经济合同审减',
          value: '经济合同审减',
        },
        {
          label: '物资采购项目审减',
          value: '物资采购项目审减',
        },
        {
          label: '招投标审减',
          value: '招投标审减',
        },
        {
          label: '剔除联合账簿不合理费用',
          value: '剔除联合账簿不合理费用',
        },
        {
          label: '挽回各类损失',
          value: '挽回各类损失',
        },
        {
          label: '挽回债券性和股权性投资损失',
          value: '挽回债券性和股权性投资损失',
        },
        {
          label: '挽回货币性资产损失',
          value: '挽回货币性资产损失',
        },
        {
          label: '挽回非货币性资产损失',
          value: '挽回非货币性资产损失',
        },
        {
          label: '内部收缴',
          value: '内部收缴',
        },
        {
          label: '罚款金额',
          value: '罚款金额',
        },
        {
          label: '收缴小金库',
          value: '收缴小金库',
        },
        {
          label: '账外资金',
          value: '账外资金',
        },
        {
          label: '收缴/罚没其他违规违纪资金',
          value: '收缴/罚没其他违规违纪资金',
        },
      ],
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    async handleEditTable1(row, disabled) {
        this.$refs['table1'].showEdit(row, disabled)
      },
      async handleEditTable2(row, disabled) {
        this.$refs['table2'].showEdit(row, disabled)
      },
    async showEdit(row, disabled) {
      this.dialogFormVisible = true
      this.formDisabled = disabled
      if (!row) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.tblYqnsSjzgYsnr.cjr = userInfo.realname
        const year = new Date().getFullYear() //得到年份
        const month = new Date().getMonth() //得到月份
        const date = new Date().getDate() //得到日期
        this.formData.tblYqnsSjzgYsnr.cjsj = `${year}-${
          month < 10 ? '0' + month : month
        }-${date < 10 ? '0' + date : date}`
        return
      } else {
        const res = await getDetailInfor({ wtzgid: row.wtzgid })
        // this.formData = {
        this.formData.reportnum = res.data.reportnum
        this.formData.unitname = res.data.unitname
        this.formData.wtdx = res.data.wtdx
        this.formData.ssms = res.data.ssms
        this.formData.clyj = res.data.clyj
        this.formData.wtje = res.data.wtje
        this.formData.gldg = res.data.gldg
        this.formData.dx = res.data.dx
        this.formData.dxfgyj = res.data.dxfgyj
        this.formData.zgqtbm = res.data.zgqtbm
        this.formData.zgsx = res.data.zgsx
        this.formData.zgfl = res.data.zgfl
        this.formData.zcss = res.data.zcss
        this.formData.fxcd = res.data.fxcd
        this.formData.ljzgje = res.data.ljzgje
        this.formData.ljjjcg = res.data.ljjjcg
        this.formData.dqzgzt = res.data.dqzgzt
        this.formData.dqzgje = res.data.dqzgje
        this.formData.dqzjjjcgtype = res.data.dqzjjjcgtype
        this.formData.dqzjjjcg = res.data.dqzjjjcg
        this.formData.dqqtjjcgtype = res.data.dqqtjjcgtype
        this.formData.dqqtjjcg = res.data.dqqtjjcg
        this.formData.dqzgms = res.data.dqzgms
        this.formData.sfys = res.data.sfys
        this.formData.tblYqnsSjzgYsnr = res.data.tblYqnsSjzgYsnr
        // cjr: res.data.cjr
        // cjsj: res.data.cjsj
        this.formData.wtzgid = res.data.wtzgid
        // }
        this.tableData =
          res.data.attachments && res.data.attachments.length
            ? res.data.attachments
            : []
      }
      if (row && !disabled) {
        this.title = '编辑'
      } else if (row && disabled) {
        this.title = '详细'
      }
    },
    close() {
      this.formData = {
        reportnum: '',
        unitname: '',
        wtdx: '',
        ssms: '',
        clyj: '',
        wtje: '',
        gldg: '',
        dx: '',
        dxfgyj: '',
        zgqtbm: '',
        zgsx: '',
        zgfl: '',
        zcss: '',
        fxcd: '',
        ljzgje: '',
        ljjjcg: '',
        dqzgzt: '',
        dqzgje: '',
        dqzjjjcgtype: '',
        dqzjjjcg: '',
        dqqtjjcgtype: '',
        dqqtjjcg: '',
        dqzgms: '',
        sfys: '',
        tblYqnsSjzgYsnr: {
          sjsfjgyssx: '',
          ysclsx: '',
          ysclsjje: '',
          ysclr: '',
          sjsfjgyssxr: '',
          lsysclsx: '',
          yscllsqk: '',
          sjdjcf: '',
          sjzwcf: '',
          sjnbjlcf: '',
          qtjjcf: '',
          cjr: '',
          cjsj: '',
        },
        wtzgid: '',
        attIds: [],
      }
      this.dialogFormVisible = false
      this.tableData = []
      this.formDisabled = true
    },
    add() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          let attIds = []
          this.tableData.map((item) => {
            attIds.push(item.attid)
            // attIds += item.attid
            // attIds += ','
          })
          // attIds = attIds.substring(0, attIds.length - 1)
          const data = await editInfo({
            ...this.formData,
            tblYqnsSjzgYsnr:
              this.formData.sfys == 1 ? this.formData.tblYqnsSjzgYsnr : null,
            attIds,
          })
          this.$emit('fetch-data')
          this.close()
        } else {
          return false
        }
      })
    },
    // async handleDown(row) {
    //   const data = await download({ attId: row.attid })
    //   let filename = row.attname
    //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
    //   let url = window.URL.createObjectURL(blob, {
    //     type: 'application/vnd.ms-excel',
    //   })
    //   const link = document.createElement('a')
    //   link.style.display = 'none'
    //   link.href = url
    //   link.setAttribute('download', filename)
    //   document.documentElement.appendChild(link)
    //   link.click()
    //   document.documentElement.removeChild(link)
    // },
    async handleDelete(row) {
      let list = this.tableData
      list = list.filter((item) => item.attid != row.attid)
      this.tableData = list
      await deleteFileInfo({ attid: row.attid })
    },
    customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }

        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
</style>
