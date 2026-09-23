<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      append-to-body
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="24">
            <el-col :span="12">
              <el-form-item label="方案编号" prop="solutioncode">
                <el-input
                  v-model="formData.solutioncode"
                  clearable
                  placeholder="请输入方案编号"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方案名称" prop="solutionname">
                <el-input
                  v-model="formData.solutionname"
                  clearable
                  placeholder="请输入方案名称"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改日期" prop="date">
                <el-date-picker
                  value-format=" yyyy-MM-dd HH:ss:mm"
                  format=" yyyy-MM-dd "
                  v-model="formData.date"
                  type="daterange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参与人数" prop="participantnum">
                <el-input
                  v-model="formData.participantnum"
                  clearable
                  placeholder="请选择参与人数"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="被整改部门" prop="rectifydepart">
                <el-input
                  v-model="formData.rectifydepartName"
                  readonly
                  style="width: 75%; margin-right: 8px"
                ></el-input>
                <el-button type="primary" @click="handleShowCompent">
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="整改负责人" prop="rectifyhead">
                <div style="display: flex">
                  <el-input
                    v-model="formData.rectifyheadName"
                    clearable
                    placeholder="请输入整改负责人"
                    disabled
                  />
                  <el-button
                    @click="showGroupLeader"
                    style="margin-left: 10px"
                    type="primary"
                  >
                    选择
                  </el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="整改建议" prop="suggestion">
                <el-input
                  v-model="formData.suggestion"
                  clearable
                  placeholder="请输入整改建议"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="录入人" prop="recorder">
                <el-input
                  v-model="formData.recorder"
                  disabled
                  placeholder="请输入录入人"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="录入时间" prop="recorddate">
                <el-date-picker
                  v-model="formData.recorddate"
                  clearable
                  placeholder="请选择录入时间"
                  format="yyyy-MM-dd  "
                  value-format="yyyy-MM-dd  HH:ss:mm"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>附件</el-divider>
            </el-col>
            <el-col :span="24">
              <div style="text-align: right; margin-bottom: 5px">
                <el-button type="success">上传</el-button>
              </div>
              <el-table>
                <el-table-column align="center" label="附件名称" prop="name" />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="name"
                />
                <el-table-column align="center" label="创建人" prop="name" />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button type="text" @click="handleEdit2(row)">
                      下载
                    </el-button>
                    <el-button type="text" @click="handleEdit2(row)">
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
  </div>
</template>
<script>
  // import { doDelete } from "@/api/table";

  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import { rectifyAdd } from '@/api/risk/riskEvents'
  export default {
    name: 'EventEdit',
    components: {
      CompanySelectUserByTree,
      CompanyTreeModel,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        formData: {
          solutioncode: undefined,
          solutionname: undefined,
          date: null,
          participantnum: null,
          rectifydepart: undefined,
          rectifyhead: undefined,
          suggestion: undefined,
          recorder: undefined,
          recorddate: undefined,
          // field111: null,
        },
        list: [{ name: 'XXXXX' }],
        tableData: [{ name: 'XXXXX' }],
        rules: {
          solutioncode: [
            {
              required: true,
              message: '请输入方案编号',
              trigger: 'blur',
            },
          ],
          solutionname: [
            {
              required: true,
              message: '请输入方案名称',
              trigger: 'blur',
            },
          ],
          date: [
            {
              required: true,
              message: '请输入整改日期',
              trigger: 'change',
            },
          ],
          participantnum: [
            {
              required: true,
              message: '请输入参与人数',
              trigger: 'change',
            },
          ],
          rectifydepart: [
            {
              required: true,
              message: '请选择被整改部门',
              trigger: 'change',
            },
          ],
          rectifyhead: [
            {
              required: true,
              message: '请输入整改负责人',
              trigger: 'change',
            },
          ],
        },
        rectifydepartOptions: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // 调起部门选择
      handleShowCompent() {
        this.$refs['comTreeRef'].show()
      },
      // 部门选择赋值
      handleSelectCompany(e) {
        this.$set(this.formData, 'rectifydepartName', e.name)
        this.$set(this.formData, 'rectifydepart', e.name)
      },
      showGroupLeader() {
        this.$refs['userTreeRef'].show()
      },
      handleExecutorSelected(e) {
        this.$set(this.formData, 'rectifyhead', e.realname)
        this.$set(this.formData, 'rectifyheadName', e.realname)
      },
      handleClick(tab, event) {
        console.log(tab, event)
      },
      save() {
        this.formData.startdate = this.formData.date[0]
        this.formData.enddate = this.formData.date[1]
        delete this.formData.date
        delete this.formData.rectifydepartName
        delete this.formData.rectifyheadName

        rectifyAdd(this.formData).then((res) => {
          if (res.code == 1) {
            this.$message({
              type: 'success',
              message: '成功',
            })
            this.dialogFormVisible = false
          }
        })
      },
      showEdit(row) {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        this.$set(this.formData, 'recorder', info.realname)
        this.$set(this.formData, 'recorderid', info.staffid)
        this.$set(this.formData, 'riseveid', row.riseveid)
        this.formData.riskevent = {
          // losseventcategory: row.losseventcategory,
          discovereddate: row.discovereddate,
          occureddate: row.occureddate,
        }

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
        this.dialogFormVisible = false
      },
      handleSetStaff(row) {
        console.log('set staff', row)
      },
      handleSetWeight(row) {
        console.log('set weight', row)
      },
      handleDownload(row) {
        console.log('downlaod', row)
      },
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleDeleteRisk(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
    },
  }
</script>
<style></style>
