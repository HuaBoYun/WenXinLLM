<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :append-to-body="true"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
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
                :disabled="modalType === 'view'"
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
                :disabled="!formData.secrectLevelId || modalType === 'view'"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编号" prop="templeNumber">
              <el-input
                v-model="formData.templeNumber"
                clearable
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
                readonly
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
                :disabled="modalType === 'view'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="模板说明" prop="templeDesc">
              <el-input
                v-model="formData.templeDesc"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入模板说明"
                :style="{ width: '100%' }"
                type="textarea"
                :disabled="modalType === 'view'"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <el-col :span="24">
        <el-divider>模板明细</el-divider>
      </el-col>
      <el-col :span="24">
        <div
          style="text-align: right; margin-bottom: 5px"
          v-if="modalType !== 'view'"
        >
          <el-button
            type="success"
            @click="handleAddTPL"
            :disabled="!this.formData.testtemid"
          >
            增加一行
          </el-button>
        </div>
        <el-table
          :data="tableData"
          fit
          highlight-current-row
          style="width: 100%; margin-bottom: 25px"
        >
          <el-table-column align="center" label="编号" prop="typecode">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.typecode"
                size="mini"
                disabled
                style="width: 90%"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" label="名称" prop="typename">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.typename"
                size="mini"
                style="width: 90%"
              />
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            fixed="right"
            label="操作"
            width="250"
            v-if="modalType === 'view'"
          >
            <template slot-scope="scope">
              <el-button type="success" @click="handleEdit(scope.row, 'view')">
                详情
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            fixed="right"
            label="操作"
            width="250"
            v-if="modalType !== 'view'"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="handleDelete(scope.row, scope.$index)"
              >
                删除
              </el-button>
              <el-button
                type="success"
                @click="handleOK(scope.row, scope.$index)"
              >
                保存
              </el-button>
              <el-button
                type="success"
                :disabled="!scope.row.typeid"
                @click="handleEdit(scope.row)"
              >
                新增内容
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" v-if="modalType !== 'view'">
          保 存
        </el-button>
        <!-- <el-button type="primary" @click="next">下一步</el-button> -->
      </template>
    </el-dialog>

    <TemplateEditStep2 ref="step2" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { createProjectCode, getTmplDetail } from '@/api/internal/project'

  import {
    defTmplModify,
    defTmplSave,
    typeSave,
    typeModify,
    typeList,
    typeRemove,
    findAutoNextNumberForLevel,
  } from '@/api/internal/testTemplate'
  import TemplateEditStep2 from './TemplateEditStep2.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'TemplateEditStep1',
    inheritAttrs: false,
    components: { TemplateEditStep2, ZXPerson },
    props: [],
    data() {
      return {
        title: '新建',
        dialogFormVisible: false,
        modalType: 'new',
        MJoption: [],
        menuId: 0,
        formData: {
          templeNumber: undefined,
          templename: undefined,
          templeDesc: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        tableData: [],
        rules: {
          templeNumber: [
            {
              required: true,
              message: '请输入模板编号',
              trigger: 'blur',
            },
          ],
          templename: [
            {
              required: true,
              message: '请输入模板名称',
              trigger: 'blur',
            },
          ],
          templeDesc: [
            {
              required: true,
              message: '请输入模板说明',
              trigger: 'blur',
            },
          ],
        },
        showMJ: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('InternalTestTemplate')
        this.menuId = res[0].menuid
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
      getNumber() {
        createProjectCode({
          tblName: 'TBL_TESTTEMPLE',
          column: 'TEMPLENUMBER',
          orgCol: 'TBLCOMANY',
          noId: '317',
        }).then((res) => {
          if (res.code === 200) {
            this.$set(this.formData, 'templeNumber', res.data)
          }
        })
      },
      async getTabList(testtemid) {
        const { code, data } = await typeList({ testtempletaid: testtemid })
        this.tableData = data
      },
      async getNumberFenLei(testtemid) {
        const { code, data } = await findAutoNextNumberForLevel({
          parentId: testtemid,
          chNumberCol: 'TYPECODE',
          chTblName: 'TBL_TESTTEMPL_TYPE',
          chirldIdCol: 'TESTTEMPLETAID',
          grandFatherIdCol: 'TESTTEMID',
          grandFatherOrgCol: 'TBLCOMANY',
          grandFatherTblName: 'TBL_TESTTEMPLE',
          noId: '317',
          parentIdCol: 'TESTTEMID',
          parentNumberCol: 'TEMPLENUMBER',
          parentTblName: 'TBL_TESTTEMPLE',
          chOrgCol: '',
        })
        return data || ''
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const params = {
              ...this.formData,
            }
            let fu
            if (this.formData.testtemid) {
              fu = defTmplModify
            } else {
              fu = defTmplSave
            }
            const { code, data } = await fu(params)
            if (code == 1) {
              this.$message.success('成功')
              if (!this.formData.testtemid) {
                // this.close()

                let form = this.formData
                this.formData = {}
                form.testtemid = data
                this.formData = form
              }
              this.$emit('fetch-data')
            }
          } else {
            return false
          }
        })
      },
      next() {
        this.close()
        this.$emit('next')
      },
      async show(row, type) {
        this.modalType = type
        if (!row) {
          this.title = '新建'
          this.getNumber()
        } else {
          this.title = '编辑'
          //获取测试模板详情
          const { code, data } = await getTmplDetail({
            templeid: row.testtemid,
          })
          if (code == 1) {
            this.formData = data.template
          }
          // this.formData = JSON.parse(JSON.stringify(row))
          this.getTabList(row.testtemid)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {}
        this.tableData = []
        this.dialogFormVisible = false
        this.$refs['elForm'].resetFields()
      },
      async handleAddTPL() {
        const data = await this.getNumberFenLei(this.formData.testtemid)

        await this.tableData.push({
          testtempletaid: this.formData.testtemid,
          typecode: data,
        })
      },

      handleEdit(row, type) {
        this.$refs['step2'].show(row, this.formData.testtemid, type)
      },
      async handleOK(row, index) {
        if (!row.typecode || !row.typename) {
          this.$message.error('请填写完整')
          return
        }
        let fu
        if (row.typeid) {
          fu = typeModify
        } else {
          fu = typeSave
        }
        const { code, data } = await fu(row)
        if (code == 1) {
          this.$message.success('成功')
          this.getTabList(this.formData.testtemid)
        }
      },
      handleDelete(row, index) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          if (row.typeid) {
            const { msg } = await typeRemove({ typeid: row.typeid })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          let list = this.tableData
          list.splice(index, 1)
          this.tableData = list
        })
      },
    },
  }
</script>
<style></style>
