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
          :rules="dynamicRules"
          size="medium"
        >
          <el-col :span="12" v-if="showMJ">
            <el-form-item
              label="密级"
              prop="secrectLevelId"
              :rules="[
                { required: true, trigger: 'change', message: '请选择密级' },
              ]"
            >
              <el-select
                v-model="formData.secrectLevelId"
                clearable
                placeholder="密级"
                style="width: 100%"
                @change="changeMJ"
              >
                <el-option
                  v-for="item in MJoption"
                  :key="item.levelId"
                  :label="item.levelName"
                  :value="item.levelId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="showMJ">
            <el-form-item label="知悉范围" prop="staffScopeNames">
              <el-input
                v-model="formData.staffScopeNames"
                readonly
                placeholder="请选择知悉范围"
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编号" prop="templenumber ">
              <el-input
                v-model="formData.templenumber"
                clearable
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
                disabled
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
                @click="handleEditTree"
              >
                选择机构
              </el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div class="save">
        <!-- <el-button @click="close">取 消</el-button> -->
        <!-- <el-button type="primary" @click="saveFirst">保 存</el-button> -->
      </div>

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
              <el-button type="success" @click="handleAdd" :disabled="footer">
                增加一行
              </el-button>
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
                  <el-button type="text" @click="handleDelete(scope.row)">
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
    <choose-mechanism
      ref="tree"
      @selected="handleSelectCompany"
      :strictMode="true"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    <select-company
      ref="companyTree"
      @handleChooseCompany="handleChooseCompany"
      :multiple="true"
    />
  </div>
</template>
<script>
  // import { doDelete } from '@/api/table'
  import {
    defTmplAdd,
    defTmplIndex,
    catSave,
    detTempList,
    deleteYaosu,
  } from '@/api/internal/evaluationTemplate'
  import { createProjectCode } from '@/api/internal/project'
  import AddFactor from './AddFactor'
  import ChooseMechanism from './ChooseMechanism.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import selectCompany from '@/views/internal/internalTest/components/ChooseCompanys.vue'
  export default {
    name: 'PlanEdit',
    components: { AddFactor, ChooseMechanism, ZXPerson, selectCompany },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        footer: true,
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
          tableData: [],
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
    },
    computed: {
      dynamicRules() {
        const baseRules = {
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
        }

        if (this.showMJ) {
          baseRules.secrectLevelId = [
            {
              required: true,
              message: '请选择密级',
              trigger: 'change',
            },
          ]
        }

        return baseRules
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('InternalEvaluationTemplate')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      save() {
        if (!this.tmplId || this.tmplId == '') {
          this.saveFirst()
        } else {
          this.formData.asstemid = this.tmplId
          this.saveFirst(() => {
            this.saveSecond()
          })
        }
      },
      sum(arr) {
        let arrSum = 0
        arr.forEach((res) => {
          arrSum = arrSum + parseInt(res)
          //
        })
        return arrSum
      },
      async showEdit(row) {
        if (!row) {
          this.title = '添加'
          const autoNew = {
            tblName: 'TBL_ASSESSTEMPLE',
            column: 'TEMPLENUMBER',
            orgCol: 'ORGID',
            noId: 315,
          }
          createProjectCode(autoNew).then((res) => {
            this.formData.templenumber = res.data
          })
          this.tableData = []
        } else {
          this.title = '编辑'
          this.footer = false
          this.tmplId = row.asstemid
          //重置formData
          this.formData = {}

          let dataList = {}
          let list = []
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
          //获取模板信息
          defTmplAdd(editInof).then((res) => {
            dataList = res.data.assesstemple
            //
            this.formData = dataList
          })
          //获取权重列表
          detTempList(editInof).then((res) => {
            res.data.assesscategories.forEach((res) => {
              ;(tableList.name = res.catname),
                (tableList.desc = res.catdes),
                (tableList.weight = res.catweight),
                (tableList.asscatid = res.asscatid),
                (tableList.asstemid = res.asstemid),
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
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          templenumber: undefined,
          templename: undefined,
          templedes: undefined,
          reorgText: undefined,
          reorg: undefined,
          tableData: [],
        }
        this.tmplId = ''
        this.tableAdd = false
        this.footer = true
        this.dialogFormVisible = false
        this.$refs.elForm.resetFields()
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
      handleDelete(row) {
        // this.tableData.splice(index, 1)
        //

        if (row.asscatid) {
          deleteYaosu(row.asscatid).then((res) => {
            if (res.code == 1) {
              this.$baseMessage(
                '删除' + res.msg,
                'success',
                'vab-hey-message-success'
              )
              this.getYaosuList()
            }
          })
        } else {
          const indexNo = this.getIndexInArr(this.tableData, row)
          //
          this.tableData.splice(indexNo, 1)
        }
      },
      //判断对象是否一样
      isObjectValueEqual(a, b) {
        if (typeof a != 'object' && typeof b != 'object') {
          if (a == b) {
            return true
          } else {
            return false
          }
        }
        let aProps = Object.getOwnPropertyNames(a)
        let bProps = Object.getOwnPropertyNames(b)

        if (aProps.length != bProps.length) {
          return false
        }

        for (let i = 0; i < aProps.length; i++) {
          let propName = aProps[i]

          if (a[propName] !== b[propName]) {
            return false
          }
        }

        return true
      },
      //获取index
      getIndexInArr(_arr, _obj) {
        let len = _arr.length
        for (let i = 0; i < len; i++) {
          if (this.isObjectValueEqual(_arr[i], _obj)) {
            return i
          }
        }
        return -1
      },
      getYaosuList() {
        this.tableData = []
        const editInof = {
          tmplId: this.tmplId,
          choiceSearch: '',
          view: '',
          type: '',
        }
        let list = []
        let tableList = {
          name: '',
          desc: '',
          weight: '',
          asscatid: '',
          asstemid: '',
        }
        //获取权重列表
        detTempList(editInof).then((res) => {
          res.data.assesscategories.forEach((res) => {
            ;(tableList.name = res.catname),
              (tableList.desc = res.catdes),
              (tableList.weight = res.catweight),
              (tableList.asscatid = res.asscatid),
              (tableList.asstemid = res.asstemid),
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
      handleEditTree() {
        const checkbox = true
        this.formData.reorgText = ''
        this.formData.reorg = []
        this.$refs['tree'].show(checkbox, [])
      },
      handleChooseCompany(node) {
        this.$set(
          this.formData,
          'reorgText',
          node.map((item) => item.label).join(',')
        )
        this.$set(this.formData, 'reorg', node.map((item) => item.id).join(','))
      },
      handleSelectCompany(res) {
        const orgName = []
        const orgId = []

        res.forEach((e) => {
          orgName.push(e.name)
          orgId.push(e.id)
          //
          //
        })
        //
        this.formData.reorgText = orgName.toString()
        this.formData.reorg = orgId.toString()
        //
      },
      saveFirst(callback) {
        const { tableData, ...other } = this.formData
        //第一步保存的表单验证
        this.$refs['elForm'].validate((valid) => {
          if (valid) {
            defTmplIndex(other).then((res) => {
              if (res.code === 1) {
                if (this.title == '添加' && this.tmplId == '') {
                  this.$baseMessage(
                    '基本信息保存' + res.msg,
                    'success',
                    'vab-hey-message-success'
                  )
                  this.tableAdd = true
                } else {
                  // this.$baseMessage(
                  //   '基本信息保存' + res.msg,
                  //   'success',
                  //   'vab-hey-message-success'
                  // )
                  this.tableAdd = true
                }
              }
              this.tmplId = res.data
              this.footer = false
              this.$emit('fetch-data')

              // 执行回调函数（如果提供）
              if (callback && typeof callback === 'function') {
                callback()
              }
            })
          }
        })

        //
      },
      saveSecond() {
        const name = []
        const desc = []
        const weight = []
        let successNum = true
        let successDesc = true
        let successName = true
        //根据接口要求新建数组
        const table = this.tableData
        table.forEach((res) => {
          name.push(res.name)
          desc.push(res.desc)
          weight.push(res.weight)
        })
        weight.forEach((res) => {
          //
          if (parseInt(res) <= 0) {
            successNum = false
          }
        })
        desc.forEach((res) => {
          if ((res = '' || !res)) {
            successDesc = false
          }
        })
        name.forEach((res) => {
          if ((res = '' || !res)) {
            successName = false
          }
        })
        //
        const weightSum = this.sum(weight)
        //判断权重的和是否为100
        if (weightSum === 100 && successNum) {
          const info = {
            tmplId: this.tmplId,
            weightSum: weightSum,
          }
          if (successDesc && successName) {
            //
            catSave(table, info).then((res) => {
              if (res.code == 1) {
                this.$baseMessage(
                  '保存' + res.msg,
                  'success',
                  'vab-hey-message-success'
                )
                //成功后刷新权重列表，获取catId，才能选择要素
                this.getYaosuList()
              }
            })
          } else {
            this.$baseMessage(
              '名字与描述不能为空',
              'error',
              'vab-hey-message-error'
            )
          }
        } else {
          this.$baseMessage(
            '权重之和必须等于100,且权重必须>0',
            'error',
            'vab-hey-message-error'
          )
        }
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
