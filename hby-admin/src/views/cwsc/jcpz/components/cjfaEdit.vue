<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="方案名称" prop="fname">
            <el-input
              v-model="formData.fname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入方案名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采集方案方式" prop="financetype">
            <el-radio-group v-model="formData.financetype">
              <el-radio :label="1">全量</el-radio>
              <el-radio :label="2">增量</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采集范围" prop="financeRange">
            <el-radio-group v-model="formData.financeRange">
              <el-radio :label="1">财务</el-radio>
              <el-radio :label="2">业务</el-radio>
              <el-radio :label="3">全部</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配置公司 " prop="financeorgname">
            <el-input
              v-model="formData.financeorgname"
              disabled
              clearable
              placeholder="请输入配置公司"
              :style="{ width: '75%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handleSelectCompany">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配置版本 " prop="fversionname">
            <el-input
              v-model="formData.fversionname"
              disabled
              clearable
              placeholder="请输入配置版本"
              :style="{ width: '75%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handleSelectVersion">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>数据源列表</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-dropdown>
              <el-button type="success" style="margin-bottom: 5px">
                新增数据源
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  @click.native="handleAddDataSource('财务数据源')"
                >
                  财务数据源
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleAddDataSource('业务数据源')"
                >
                  业务数据源
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <el-table :data="tableData">
            <el-table-column
              align="center"
              label="名称"
              prop="fname"
              width="300"
            >
              <template #default="{ row }">
                <el-input v-model="row.fname"></el-input>
              </template>
            </el-table-column>
            <el-table-column align="center" label="数据源" prop="dataconfig">
              <template #default="{ row, $index }">
                <el-input
                  style="width: 70%; margin-right: 5px"
                  v-model="row.dbconfigname"
                  :disabled="true"
                ></el-input>
                <el-button
                  type="primary"
                  @click="handleSelectDataSource($index)"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="数据源类型"
              prop="ftype"
              width="120"
            >
              <template #default="{ row }">
                <el-input
                  v-model="row.ftype"
                  readonly
                  style="border: none"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleDeletByid(row, $index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="数据源 " prop="dbconfigname">
            <el-input
              v-model="formData.dbconfigname"
              disabled
              clearable
              placeholder="请输入数据源"
              :style="{ width: '75%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handleSelectDataSource">
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <departmentSelect
      ref="departmentSelect"
      @submit="handleDepartmentSelected"
    />
    <bbModal ref="bbModal" @versionManage="handleVersionManage" />
    <sjyModal ref="sjyModal" @dataSourceManage="handleDataSourceManage" />
  </el-dialog>
</template>

<script>
  import { saveCjfa, getCjfaDetail } from '@/api/cwsc'
  import departmentSelect from '@/components/department.vue'
  import bbModal from '@/views/cwsc/jcpz/components/bbModal.vue'
  import sjyModal from '@/views/cwsc/jcpz/components/shujuyuanModal.vue'
  export default {
    components: { departmentSelect, bbModal, sjyModal },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        // 添加数据源类型映射
        dataSourceTypeMap: {
          财务数据源: 1,
          业务数据源: 2,
        },
        dataSourceTypeMapReverse: {
          1: '财务数据源',
          2: '业务数据源',
        },
        tableData: [],
        formData: {
          fid: '',
          fname: '',
          financetype: '',
          financeorgname: '',
          fversionid: '',
          fversionname: '',
          dataconfig: '',
          dbconfigname: '',
          financeRange: '',
        },
        footer: true,
        rules: {
          dataBaseType: [
            {
              required: true,
              message: '请选择数据库类型',
              trigger: 'blur',
            },
          ],
          dataBaseConnectionAddress: [
            {
              required: true,
              message: '请输入数据库连接',
              trigger: 'blur',
            },
          ],
          dataBaseUsers: [
            {
              required: true,
              message: '请输入数据库用户',
              trigger: 'blur',
            },
          ],
          dataBasePassWord: [
            {
              required: true,
              message: '请输入数据库密码',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editIndex: null, //当前编辑的索引
      }
    },
    computed: {},

    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getCjfaDetail({ fid: row.fid })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data[key]
          })
          // 转换数据源类型为文字
          this.tableData = res.data.dataConfigList.map((item) => ({
            ...item,
            ftype: this.dataSourceTypeMapReverse[item.ftype] || item.ftype,
            dbconfigname: item.dataconfigname,
            dataconfig: item.dataconfig,
          }))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {
          fid: '',
          fname: '',
          financetype: '',
          financeorgname: '',
          fversionid: '',
          fversionname: '',
          dbconfigid: '',
          dbconfigname: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            // 转换数据源类型
            params.dataConfigList = this.tableData.map((item) => ({
              ...item,
              ftype: this.dataSourceTypeMap[item.ftype] || item.ftype,
            }))
            const res = await saveCjfa(params)
            if (res && res.code == 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },

      /**
       * @description: 选择公司
       * @param {*} val
       * @return {*}
       */

      handleSelectCompany() {
        this.$refs.departmentSelect.showEdit()
      },
      /**
       * @description: 选择版本
       * @param {*} val
       * @return {*}
       */
      handleSelectVersion() {
        this.$refs.bbModal.showEdit()
      },
      /**
       * @description: 选择数据源
       * @param {*} val
       * @return {*}
       */
      handleSelectDataSource(index) {
        this.editIndex = index
        this.$refs.sjyModal.showEdit()
      },
      handleDepartmentSelected(val) {
        this.formData.financeorgid = val.id
        this.formData.financeorgname = val.name
      },
      handleDataSourceManage(val) {
        if (this.editIndex !== null) {
          // 更新表格中对应行的数据
          this.tableData[this.editIndex].dataconfig = val[0].fid
          this.tableData[this.editIndex].dbconfigname = val[0].fintext
        } else {
          // 更新表单数据
          this.formData.dbconfigid = val[0].fid
          this.formData.dbconfigname = val[0].fintext
        }
        this.editIndex = null // 重置编辑索引
      },
      handleVersionManage(val) {
        this.formData.fversionid = val[0].fid
        this.formData.fversionname = val[0].handtext
      },
      handleAddDataSource(command) {
        // 只检查财务数据源是否已存在
        if (command === '财务数据源') {
          const existingFinanceDataSource = this.tableData.find(
            (item) => item.ftype === '财务数据源'
          )
          if (existingFinanceDataSource) {
            this.$message({
              message: '已存在财务数据源，不能重复添加',
              type: 'warning',
            })
            return
          }
        }

        const obj = {
          fname: '',
          dataconfig: '',
          dbconfigname: '',
          ftype: command,
        }
        this.tableData.push(obj)
      },
      async handleDeletByid(row, index) {
        this.tableData.splice(index, 1)
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
