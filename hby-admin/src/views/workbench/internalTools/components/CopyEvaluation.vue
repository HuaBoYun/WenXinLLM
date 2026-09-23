<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
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
          <el-col :span="12">
            <el-form-item label="模板编号" prop="templenumber ">
              <el-input
                v-model="formData.templenumber"
                clearable
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templename">
              <el-input
                v-model="formData.templename"
                clearable
                placeholder="请输入模板名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="模板说明" prop="templedes">
              <el-input
                v-model="formData.templedes"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入模板说明"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="适用机构" prop="reorgText">
              <el-input
                v-model="formData.reorgText"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请选择适用机构"
                :style="{ width: '100%' }"
                readonly
                type="textarea"
              />
              <el-button
                type="primary"
                style="margin: 10px 0px"
                @click="handleEditTree()"
              >
                选择机构
              </el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <!-- <div class="save">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="saveFirst">保 存</el-button>
      </div> -->

      <el-col :span="24">
        <el-divider>评价体系条目</el-divider>
      </el-col>
      <el-col :span="24">
        <el-form
          ref="elForm2"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success" @click="handleAdd">增加一行</el-button>
            </div>
            <el-table
              border
              :data="tableData"
              fit
              highlight-current-row
              style="width: 100%; margin-bottom: 25px"
            >
              <el-table-column
                align="center"
                label="序号"
                prop="id"
                width="65%"
              >
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="评价类别名称" prop="name">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.name"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="评价类别描述" prop="desc">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.desc"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="权重"
                prop="weight"
                width="150"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.weight"
                    size="mini"
                    style="width: 50%"
                  />
                  %
                </template>
              </el-table-column>

              <el-table-column
                align="center"
                fixed="right"
                label="操作"
                width="150"
              >
                <template slot-scope="scope">
                  <el-button type="text" @click="handleDelete(scope.$index)">
                    删除
                  </el-button>
                  <el-button type="success" @click="handleEdit(scope.row)">
                    选择要素
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-col>

      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <add-factor ref="edit" />
    <choose-mechanism ref="tree" @selected="handleSelectCompany" />
  </div>
</template>
<script>
  // import { doDelete } from '@/api/table'
  import {
    copy,
    defTmplIndex,
    catSave,
    detTempList,
    defTmplAdd,
  } from '@/api/internal/evaluationTemplate'
  import { createProjectCode } from '@/api/internal/project'
  import AddFactor from './AddFactor'
  import ChooseMechanism from './ChooseMechanism.vue'
  export default {
    name: 'PlanEdit',
    components: { AddFactor, ChooseMechanism },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        templeNo: '',
        tmplId: '',
        title: '',
        tableAdd: false,
        dialogFormVisible: false,
        tableData: [],
        formData: {
          templenumber: undefined,
          templename: undefined,
          templedes: undefined,
          reorgText: undefined,
          reorg: undefined,
          beforeTempId: undefined,
        },

        rules: {
          templenumber: [
            {
              required: true,
              message: '请输入模板ID',
              trigger: 'blur',
            },
          ],
          templename: [
            {
              required: true,
              message: '请选择模板名称',
              trigger: 'blur',
            },
          ],
          templedes: [
            {
              required: true,
              message: '请输入模板说明',
              trigger: 'change',
            },
          ],
          reorgText: [
            {
              required: true,
              message: '请选择适用机构',
              trigger: 'change',
            },
          ],
        },
      }
    },
    computed: {},
    watch: {
      templeNo(newVal, oldVal) {
        //
        //
        this.formData.templenumber = newVal
      },
    },
    created() {
      // this.getNo()
    },
    mounted() {},
    methods: {
      save() {
        const { asstemid, ...other } = this.formData
        console.log('🚀 ~ save ~ this.formData:', this.formData)
        copy(other, asstemid).then((res) => {
          // if (res.cod == 0) {
          //   this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          // }
          this.$emit('fetch-data')
        })
        this.close()
      },
      sum(arr) {
        let arrSum = 0
        arr.forEach((res) => {
          arrSum = arrSum + parseInt(res)
        })
        return arrSum
      },
      showEdit(row) {
        //重置formData
        this.templeNo = ''
        this.formData = {
          templenumber: undefined,
          templename: undefined,
          templedes: undefined,
          reorgText: undefined,
          reorg: undefined,
          beforeTempId: undefined,
        }

        this.getNo()
        this.formData.templedes = row.templedes
        this.formData.templename = row.templename
        this.formData.reorgText = row.reorgText
        this.formData.reorg = row.reorg
        // this.formData.templenumber = this.templeNo
        this.title = '复制'
        this.formData.beforeTempId = row.asstemid
        console.log('🚀 ~ showEdit ~ row.asstemid:', row.asstemid)
        // this.beforeTempId = 'aaa'
        let tableList = {
          name: '',
          desc: '',
          weight: '',
          asscatid: '',
          asstemid: '',
        }
        const editInof = {
          tmplId: row.asstemid,
          choiceSearch: '',
          view: '',
          type: '',
        }
        let dataList = {}
        let list = []
        //获取模板信息
        defTmplAdd(editInof).then((res) => {
          dataList = res.data.assesstemple
          //
          this.formData = dataList
          // 复制时不复制模板编号，使用新生成的编号
          this.formData.templenumber = this.templeNo
        })
        //获取权重列表
        detTempList(editInof).then((res) => {
          res.data.assesscategories.forEach((res) => {
            tableList.name = res.catname
            tableList.desc = res.catdes
            tableList.weight = res.catweight
            tableList.asscatid = res.asscatid
            tableList.asstemid = res.asstemid
            list.push(tableList)
            tableList = {
              name: '',
              desc: '',
              weight: '',
              asscatid: '',
              asstemid: '',
            }
          })
          this.tableData = list
        })

        this.tableAdd = true

        this.dialogFormVisible = true
      },
      async getNo() {
        const autoNew = {
          tblName: 'TBL_ASSESSTEMPLE',
          column: 'TEMPLENUMBER',
          orgCol: 'ORGID',
          noId: 315,
        }
        const res = await createProjectCode(autoNew)
        this.templeNo = res.data
        //
      },
      close() {
        this.formData = {
          templenumber: undefined,
          templename: undefined,
          templedes: undefined,
          reorgText: undefined,
          reorg: undefined,
          beforeTempId: undefined,
        }
        this.tableAdd = false
        this.dialogFormVisible = false
      },
      // handleDelete(row) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg } = await doDelete({ ids: row.id })
      //     this.$baseMessage(msg, 'success', 'vab-hey-message-success')
      //     // await this.fetchData()
      //   })
      // },
      handleAdd() {
        //formData的表格新增一行
        if (this.tableAdd) {
          this.tableData.push({
            name: '',
            desc: '',
            weight: '',
          })
        } else {
          this.$baseMessage(
            '请先保存基本信息',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      handleDelete(index) {
        this.tableData.splice(index, 1)
      },
      handleEdit(row) {
        if (row.asscatid == '' || !row.asscatid) {
          this.$baseMessage(
            '请先点击确定保存评价体系条目',
            'error',
            'vab-hey-message-error'
          )
        } else {
          this.$refs['edit'].showEdit(row)
        }
      },
      handleEditTree(row) {
        const checkbox = true
        this.formData.reorgText = ''
        this.formData.reorg = []
        this.$refs['tree'].show(checkbox, row)
      },
      handleSelectCompany(res) {
        const orgName = []
        const orgId = []

        res.forEach((e) => {
          orgName.push(e.name)
          orgId.push(e.id)
        })

        this.formData.reorgText = orgName.toString()
        this.formData.reorg = orgId.toString()
      },
      saveFirst() {
        const { tableData, ...other } = this.formData
        //第一步保存的表单验证
        this.$refs['elForm'].validate((valid) => {
          if (valid) {
            defTmplIndex(other).then((res) => {
              if (res.code === 1) {
                if (this.title == '添加') {
                  this.$baseMessage(
                    '保存' + res.msg + '，请创建评价体系条目',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.tableAdd = true
                } else {
                  this.$baseMessage(
                    '保存' + res.msg + '，请修改评价体系条目',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.tableAdd = true
                }
              }
              this.tmplId = res.data
            })
          } else {
            this.$baseMessage('失败', 'error', 'vab-hey-message-error')
          }
        })

        //
      },
    },
  }
</script>
<style scoped lang="scss">
  .save {
    // padding: 20px;
    // padding-top: 10px;
    text-align: right;
    box-sizing: border-box;
  }
</style>
