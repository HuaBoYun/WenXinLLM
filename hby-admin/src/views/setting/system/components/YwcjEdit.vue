<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="24">
          <div class="model-show">
            <div>基本信息</div>
            <div>流程图</div>
            <div>流程描述</div>
            <div>内外控</div>
            <div>工作流编辑</div>
            <div>自定义表单</div>
          </div>
        </el-col>
        <el-col :span="24">
          <el-divider>业务单元</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务编号" prop="field101">
            <el-input
              v-model="formData.field101"
              clearable
              placeholder="请输入业务编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程名称" prop="field102">
            <el-input
              v-model="formData.field102"
              clearable
              placeholder="请输入流程名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="field103">
            <el-select
              v-model="formData.field103"
              clearable
              filterable
              placeholder="请选择责任部门"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field103Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相关部门" prop="field104">
            <el-select
              v-model="formData.field104"
              clearable
              filterable
              placeholder="请选择相关部门"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field104Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务名称" prop="field106">
            <el-input
              v-model="formData.field106"
              clearable
              placeholder="请输入业务名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="业务描述" prop="field105">
            <el-input
              v-model="formData.field105"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入业务描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>风险描述</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险编号" prop="field108">
            <el-input
              v-model="formData.field108"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="field109">
            <el-input
              v-model="formData.field109"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="版本" prop="field111">
            <el-select
              v-model="formData.field111"
              clearable
              filterable
              placeholder="请选择版本"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field111Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="field112">
            <el-input
              v-model="formData.field112"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>控制措施</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险控制点编号" prop="field113">
            <el-input
              v-model="formData.field113"
              clearable
              placeholder="请输入风险控制点编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制责任人" prop="field114">
            <el-select
              v-model="formData.field114"
              clearable
              filterable
              placeholder="请选择控制责任人"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field114Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程分类" prop="field115">
            <el-select
              v-model="formData.field115"
              clearable
              filterable
              placeholder="请选择流程分类"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field115Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制频率" prop="field116">
            <el-input
              v-model="formData.field116"
              clearable
              placeholder="请输入控制频率"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制类型" prop="field117">
            <el-select
              v-model="formData.field117"
              clearable
              filterable
              placeholder="请选择控制类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field117Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制手段" prop="field124">
            <el-select
              v-model="formData.field124"
              clearable
              filterable
              placeholder="请选择控制手段"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field124Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否关键控制" prop="field125">
            <el-select
              v-model="formData.field125"
              clearable
              filterable
              placeholder="请选择是否关键控制"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field125Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制是否有效" prop="field126">
            <el-select
              v-model="formData.field126"
              clearable
              filterable
              placeholder="请选择控制是否有效"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field126Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否进行控制测试" prop="field127">
            <el-select
              v-model="formData.field127"
              clearable
              filterable
              placeholder="请选择是否进行控制测试"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field127Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="财务报表认定" prop="field128">
            <el-select
              v-model="formData.field128"
              clearable
              filterable
              placeholder="请选择财务报表认定"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field128Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险控制点描述" prop="field123">
            <el-input
              v-model="formData.field123"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入风险控制点描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="措施控制" prop="field122">
            <el-input
              v-model="formData.field122"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入措施控制"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>审计程序</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计程序" prop="field121">
            <el-input
              v-model="formData.field121"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入审计程序"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'YwcjEdit',
    data() {
      return {
        formData: {
          field101: undefined,
          field102: undefined,
          field103: undefined,
          field104: undefined,
          field106: undefined,
          field105: undefined,
          field108: undefined,
          field109: undefined,
          field111: undefined,
          field112: undefined,
          field113: undefined,
          field114: undefined,
          field115: undefined,
          field116: undefined,
          field117: undefined,
          field124: undefined,
          field125: undefined,
          field126: undefined,
          field127: undefined,
          field128: undefined,
          field123: undefined,
          field122: undefined,
          field121: undefined,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入业务编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入流程名称',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请选择责任部门',
              trigger: 'change',
            },
          ],
          field104: [],
          field106: [],
          field105: [],
          field108: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          field109: [
            {
              required: true,
              message: '请输入风险名称',
              trigger: 'blur',
            },
          ],
          field111: [
            {
              required: true,
              message: '请选择版本',
              trigger: 'change',
            },
          ],
          field112: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          field113: [
            {
              required: true,
              message: '请输入风险控制点编号',
              trigger: 'blur',
            },
          ],
          field114: [],
          field115: [
            {
              required: true,
              message: '请选择流程分类',
              trigger: 'change',
            },
          ],
          field116: [],
          field117: [],
          field124: [],
          field125: [],
          field126: [],
          field127: [],
          field128: [],
          field123: [
            {
              required: true,
              message: '请输入风险控制点描述',
              trigger: 'blur',
            },
          ],
          field122: [
            {
              required: true,
              message: '请输入措施控制',
              trigger: 'blur',
            },
          ],
          field121: [],
        },
        field103Options: [
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
        field111Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field114Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field115Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field117Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field124Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field125Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field126Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field127Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        field128Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: '1',
            label: '党委办公室',
          },
          {
            value: '2',
            label: '审计监察部',
          },
          {
            value: '3',
            label: '风险管理部',
          },
          {
            value: '4',
            label: '总会办公室',
          },
          {
            value: '5',
            label: '总经理办公室',
          },
          {
            value: '6',
            label: '人力资源部',
          },
          {
            value: '7',
            label: '合同管理部',
          },
          {
            value: '8',
            label: '安全管理部',
          },
          {
            value: '9',
            label: '设备运营部',
          },
          {
            value: '10',
            label: '工程管理部',
          },
          {
            value: '11',
            label: '综合管理部',
          },
          {
            value: '12',
            label: '信息运维管理部',
          },
        ],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
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
    },
  }
</script>
<style scoped>
  .model-show {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
  }
  .model-show > div {
    background: red;
    margin: 5px;
    padding: 10px;
    color: white;
    font-size: 16px;
    font-weight: 500;
  }
</style>
